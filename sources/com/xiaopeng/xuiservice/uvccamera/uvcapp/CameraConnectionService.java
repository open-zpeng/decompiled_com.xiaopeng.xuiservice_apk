package com.xiaopeng.xuiservice.uvccamera.uvcapp;

import android.hardware.usb.UsbDevice;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.uvccamera.usb.DeviceFilter;
import com.xiaopeng.xuiservice.uvccamera.usb.Format;
import com.xiaopeng.xuiservice.uvccamera.usb.IButtonCallback;
import com.xiaopeng.xuiservice.uvccamera.usb.IFrameCallback;
import com.xiaopeng.xuiservice.uvccamera.usb.Size;
import com.xiaopeng.xuiservice.uvccamera.usb.USBMonitor;
import com.xiaopeng.xuiservice.uvccamera.usb.UVCControl;
import com.xiaopeng.xuiservice.uvccamera.usb.UVCParam;
import com.xiaopeng.xuiservice.uvccamera.utils.UVCUtils;
import com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper;
import com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraInternal;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes5.dex */
public class CameraConnectionService {
    private static final String TAG = CameraConnectionService.class.getSimpleName();
    private static volatile CameraConnectionService mInstance;

    CameraConnectionService() {
    }

    public static CameraConnectionService getInstance() {
        if (mInstance == null) {
            synchronized (CameraConnectionService.class) {
                if (mInstance == null) {
                    mInstance = new CameraConnectionService();
                }
            }
        }
        return mInstance;
    }

    public ICameraConnection newConnection() {
        return new CameraConnection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public final class CameraConnection implements ICameraConnection {
        private Handler mListenerHandler;
        private USBMonitor mUSBMonitor;
        private WeakReference<ICameraHelper.StateCallback> mWeakStateCallback;
        private final String LOG_PREFIX = "CameraConnection#";
        private final Object mConnectionSync = new Object();
        private final HashMap<String, CameraInternal> mCameras = new HashMap<>();
        private HandlerThread mListenerHandlerThread = new HandlerThread("CameraConnection#" + hashCode());

        CameraConnection() {
            this.mListenerHandlerThread.start();
            this.mListenerHandler = new Handler(this.mListenerHandlerThread.getLooper());
            this.mUSBMonitor = new USBMonitor(UVCUtils.getApplication(), new MyOnDeviceConnectListener(), this.mListenerHandler);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public CameraInternal addCamera(UsbDevice device, USBMonitor.UsbControlBlock ctrlBlock) {
            CameraInternal cameraInternal;
            String str = CameraConnectionService.TAG;
            LogUtil.d(str, "addCamera:device=" + device.getDeviceName());
            String key = getCameraKey(device);
            synchronized (this.mConnectionSync) {
                cameraInternal = this.mCameras.get(key);
                if (cameraInternal != null) {
                    LogUtil.d(CameraConnectionService.TAG, "Camera already exist");
                } else {
                    cameraInternal = new CameraInternal(UVCUtils.getApplication(), ctrlBlock, device.getVendorId(), device.getProductId());
                    this.mCameras.put(key, cameraInternal);
                }
                this.mConnectionSync.notifyAll();
            }
            checkExistCamera();
            return cameraInternal;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeCamera(UsbDevice device) {
            String str = CameraConnectionService.TAG;
            LogUtil.d(str, "removeCamera:device=" + device.getDeviceName());
            String key = getCameraKey(device);
            synchronized (this.mConnectionSync) {
                CameraInternal service = this.mCameras.get(key);
                if (service != null) {
                    service.release();
                }
                this.mCameras.remove(key);
                this.mConnectionSync.notifyAll();
            }
            checkExistCamera();
        }

        private CameraInternal getCamera(UsbDevice device, boolean isBlocking) {
            CameraInternal cameraInternal;
            synchronized (this.mConnectionSync) {
                cameraInternal = null;
                if (device == null) {
                    if (this.mCameras.size() > 0) {
                        cameraInternal = (CameraInternal) this.mCameras.values().toArray()[0];
                    }
                } else {
                    String cameraKey = getCameraKey(device);
                    CameraInternal cameraInternal2 = this.mCameras.get(cameraKey);
                    if (cameraInternal2 == null && isBlocking) {
                        Log.i(CameraConnectionService.TAG, "wait for service is ready");
                        try {
                            this.mConnectionSync.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    cameraInternal = this.mCameras.get(cameraKey);
                }
            }
            return cameraInternal;
        }

        private CameraInternal getCamera(UsbDevice device) {
            return getCamera(device, true);
        }

        private boolean checkExistCamera() {
            boolean z;
            synchronized (this.mConnectionSync) {
                int n = this.mCameras.size();
                String str = CameraConnectionService.TAG;
                LogUtil.d(str, "number of existed camera=" + n);
                z = n == 0;
            }
            return z;
        }

        private String getCameraKey(UsbDevice device) {
            return USBMonitor.getDeviceKey(device);
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraConnection
        public void register(ICameraHelper.StateCallback callback) {
            this.mWeakStateCallback = new WeakReference<>(callback);
            if (this.mUSBMonitor != null) {
                LogUtil.d(CameraConnectionService.TAG, "mUSBMonitor#register:");
                List<DeviceFilter> filters = DeviceFilter.getDeviceFilters(UVCUtils.getApplication(), R.xml.device_filter);
                this.mUSBMonitor.setDeviceFilter(filters);
                this.mUSBMonitor.register();
            }
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraConnection
        public void unregister(ICameraHelper.StateCallback callback) {
            if (this.mUSBMonitor != null) {
                LogUtil.d(CameraConnectionService.TAG, "mUSBMonitor#unregister:");
                this.mUSBMonitor.unregister();
                this.mUSBMonitor = null;
            }
            this.mWeakStateCallback.clear();
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraConnection
        public List<UsbDevice> getDeviceList() {
            return this.mUSBMonitor.getDeviceList();
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraConnection
        public void selectDevice(UsbDevice device) {
            String str = CameraConnectionService.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("CameraConnection#selectDevice:device=");
            sb.append(device != null ? device.getDeviceName() : null);
            Log.d(str, sb.toString());
            String cameraKey = getCameraKey(device);
            synchronized (this.mConnectionSync) {
                Log.i(CameraConnectionService.TAG, "request permission");
                this.mUSBMonitor.requestPermission(device);
                CameraInternal cameraInternal = this.mCameras.get(cameraKey);
                if (cameraInternal == null) {
                    Log.i(CameraConnectionService.TAG, "wait for getting permission");
                    try {
                        this.mConnectionSync.wait();
                    } catch (Exception e) {
                        Log.e(CameraConnectionService.TAG, "selectDevice:", e);
                    }
                    Log.i(CameraConnectionService.TAG, "check CameraInternal again");
                    CameraInternal cameraInternal2 = this.mCameras.get(cameraKey);
                    if (cameraInternal2 == null) {
                        throw new RuntimeException("failed to open USB device(has no permission)");
                    }
                }
            }
            String str2 = CameraConnectionService.TAG;
            Log.i(str2, "success to get service:serviceId=" + cameraKey);
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraConnection
        public List<Format> getSupportedFormatList(UsbDevice device) {
            CameraInternal cameraInternal = getCamera(device);
            if (cameraInternal != null) {
                return cameraInternal.getSupportedFormatList();
            }
            return null;
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraConnection
        public List<Size> getSupportedSizeList(UsbDevice device) {
            CameraInternal cameraInternal = getCamera(device);
            if (cameraInternal != null) {
                return cameraInternal.getSupportedSizeList();
            }
            return null;
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraConnection
        public Size getPreviewSize(UsbDevice device) {
            CameraInternal cameraInternal = getCamera(device);
            if (cameraInternal != null) {
                return cameraInternal.getPreviewSize();
            }
            return null;
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraConnection
        public void setPreviewSize(UsbDevice device, Size size) {
            LogUtil.d(CameraConnectionService.TAG, "CameraConnection#setPreviewSize:");
            CameraInternal cameraInternal = getCamera(device);
            if (cameraInternal == null) {
                throw new IllegalArgumentException("invalid device");
            }
            cameraInternal.setPreviewSize(size);
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraConnection
        public void addSurface(UsbDevice device, Object surface, boolean isRecordable) {
            String str = CameraConnectionService.TAG;
            Log.d(str, "CameraConnection#addSurface:surface=" + surface);
            CameraInternal cameraInternal = getCamera(device, false);
            if (cameraInternal != null) {
                cameraInternal.addSurface(surface, isRecordable);
            }
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraConnection
        public void removeSurface(UsbDevice device, Object surface) {
            String str = CameraConnectionService.TAG;
            LogUtil.d(str, "CameraConnection#removeSurface:surface=" + surface);
            CameraInternal cameraInternal = getCamera(device, false);
            if (cameraInternal != null) {
                cameraInternal.removeSurface(surface);
            }
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraConnection
        public void setButtonCallback(UsbDevice device, IButtonCallback callback) {
            String str = CameraConnectionService.TAG;
            Log.d(str, "CameraConnection#setButtonCallback:callback=" + callback);
            CameraInternal cameraInternal = getCamera(device);
            if (cameraInternal != null) {
                cameraInternal.setButtonCallback(callback);
            }
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraConnection
        public void setFrameCallback(UsbDevice device, IFrameCallback callback, int pixelFormat) {
            String str = CameraConnectionService.TAG;
            Log.d(str, "CameraConnection#setFrameCallback:pixelFormat=" + pixelFormat);
            CameraInternal cameraInternal = getCamera(device);
            if (cameraInternal != null) {
                cameraInternal.setFrameCallback(callback, pixelFormat);
            }
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraConnection
        public void openCamera(UsbDevice device, UVCParam param, CameraPreviewConfig previewConfig) {
            LogUtil.d(CameraConnectionService.TAG, "CameraConnection#openCamera:");
            CameraInternal cameraInternal = getCamera(device);
            if (cameraInternal == null) {
                throw new IllegalArgumentException("invalid device");
            }
            cameraInternal.openCamera(param, previewConfig);
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraConnection
        public void closeCamera(UsbDevice device) {
            LogUtil.d(CameraConnectionService.TAG, "CameraConnection#closeCamera:");
            CameraInternal cameraInternal = getCamera(device);
            if (cameraInternal == null) {
                throw new IllegalArgumentException("invalid device");
            }
            cameraInternal.closeCamera();
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraConnection
        public void startPreview(UsbDevice device) {
            LogUtil.d(CameraConnectionService.TAG, "CameraConnection#startPreview:");
            CameraInternal cameraInternal = getCamera(device);
            if (cameraInternal == null) {
                throw new IllegalArgumentException("invalid device");
            }
            cameraInternal.startPreview();
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraConnection
        public void stopPreview(UsbDevice device) {
            LogUtil.d(CameraConnectionService.TAG, "CameraConnection#stopPreview:");
            CameraInternal cameraInternal = getCamera(device);
            if (cameraInternal == null) {
                throw new IllegalArgumentException("invalid device");
            }
            cameraInternal.stopPreview();
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraConnection
        public UVCControl getUVCControl(UsbDevice device) {
            CameraInternal cameraInternal = getCamera(device);
            if (cameraInternal != null) {
                return cameraInternal.getUVCControl();
            }
            return null;
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraConnection
        public boolean isCameraOpened(UsbDevice device) {
            CameraInternal cameraInternal = getCamera(device, false);
            return cameraInternal != null && cameraInternal.isCameraOpened();
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraConnection
        public void releaseCamera(UsbDevice device) {
            LogUtil.d(CameraConnectionService.TAG, "CameraConnection#release:");
            String cameraKey = getCameraKey(device);
            synchronized (this.mConnectionSync) {
                CameraInternal cameraInternal = this.mCameras.get(cameraKey);
                if (cameraInternal != null) {
                    cameraInternal.release();
                }
                this.mCameras.remove(cameraKey);
            }
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraConnection
        public void releaseAllCamera() {
            LogUtil.d(CameraConnectionService.TAG, "CameraConnection#releaseAll:");
            synchronized (this.mConnectionSync) {
                for (CameraInternal cameraInternal : this.mCameras.values()) {
                    if (cameraInternal != null) {
                        cameraInternal.release();
                    }
                }
                this.mCameras.clear();
            }
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraConnection
        public void setPreviewConfig(UsbDevice device, CameraPreviewConfig config) {
            LogUtil.d(CameraConnectionService.TAG, "CameraConnection#setCameraPreviewConfig:");
            String cameraKey = getCameraKey(device);
            synchronized (this.mConnectionSync) {
                CameraInternal cameraInternal = this.mCameras.get(cameraKey);
                if (cameraInternal != null) {
                    cameraInternal.setPreviewConfig(config);
                }
            }
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraConnection
        public void release() {
            USBMonitor uSBMonitor = this.mUSBMonitor;
            if (uSBMonitor != null) {
                uSBMonitor.destroy();
                this.mUSBMonitor = null;
            }
            this.mListenerHandlerThread.quitSafely();
            this.mWeakStateCallback.clear();
        }

        /* loaded from: classes5.dex */
        private class MyOnDeviceConnectListener implements USBMonitor.OnDeviceConnectListener {
            private MyOnDeviceConnectListener() {
            }

            @Override // com.xiaopeng.xuiservice.uvccamera.usb.USBMonitor.OnDeviceConnectListener
            public void onAttach(UsbDevice device) {
                LogUtil.d(CameraConnectionService.TAG, "OnDeviceConnectListener#onAttach:");
                if (CameraConnection.this.mWeakStateCallback.get() != null) {
                    try {
                        ((ICameraHelper.StateCallback) CameraConnection.this.mWeakStateCallback.get()).onAttach(device);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override // com.xiaopeng.xuiservice.uvccamera.usb.USBMonitor.OnDeviceConnectListener
            public void onDeviceOpen(final UsbDevice device, USBMonitor.UsbControlBlock ctrlBlock, boolean createNew) {
                LogUtil.d(CameraConnectionService.TAG, "OnDeviceConnectListener#onDeviceOpen:");
                CameraInternal camera = CameraConnection.this.addCamera(device, ctrlBlock);
                camera.registerCallback(new ICameraInternal.StateCallback() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.CameraConnectionService.CameraConnection.MyOnDeviceConnectListener.1
                    boolean mIsCameraOpened = false;

                    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraInternal.StateCallback
                    public void onCameraOpen() {
                        if (!this.mIsCameraOpened) {
                            if (CameraConnection.this.mWeakStateCallback.get() != null) {
                                try {
                                    ((ICameraHelper.StateCallback) CameraConnection.this.mWeakStateCallback.get()).onCameraOpen(device);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            this.mIsCameraOpened = true;
                        }
                    }

                    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraInternal.StateCallback
                    public void onCameraClose() {
                        if (this.mIsCameraOpened) {
                            if (CameraConnection.this.mWeakStateCallback.get() != null) {
                                try {
                                    ((ICameraHelper.StateCallback) CameraConnection.this.mWeakStateCallback.get()).onCameraClose(device);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            this.mIsCameraOpened = false;
                        }
                    }
                });
                if (CameraConnection.this.mWeakStateCallback.get() != null) {
                    try {
                        ((ICameraHelper.StateCallback) CameraConnection.this.mWeakStateCallback.get()).onDeviceOpen(device, createNew);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override // com.xiaopeng.xuiservice.uvccamera.usb.USBMonitor.OnDeviceConnectListener
            public void onDeviceClose(UsbDevice device, USBMonitor.UsbControlBlock ctrlBlock) {
                LogUtil.d(CameraConnectionService.TAG, "OnDeviceConnectListener#onDeviceClose:");
                if (CameraConnection.this.mWeakStateCallback.get() != null) {
                    try {
                        ((ICameraHelper.StateCallback) CameraConnection.this.mWeakStateCallback.get()).onDeviceClose(device);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                CameraConnection.this.removeCamera(device);
            }

            @Override // com.xiaopeng.xuiservice.uvccamera.usb.USBMonitor.OnDeviceConnectListener
            public void onDetach(UsbDevice device) {
                LogUtil.d(CameraConnectionService.TAG, "OnDeviceConnectListener#onDetach:");
                if (CameraConnection.this.mWeakStateCallback.get() != null) {
                    try {
                        ((ICameraHelper.StateCallback) CameraConnection.this.mWeakStateCallback.get()).onDetach(device);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                CameraConnection.this.removeCamera(device);
            }

            @Override // com.xiaopeng.xuiservice.uvccamera.usb.USBMonitor.OnDeviceConnectListener
            public void onCancel(UsbDevice device) {
                LogUtil.d(CameraConnectionService.TAG, "OnDeviceConnectListener#onCancel:");
                if (CameraConnection.this.mWeakStateCallback.get() != null) {
                    try {
                        ((ICameraHelper.StateCallback) CameraConnection.this.mWeakStateCallback.get()).onCancel(device);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                synchronized (CameraConnection.this.mConnectionSync) {
                    CameraConnection.this.mConnectionSync.notifyAll();
                }
            }
        }
    }
}
