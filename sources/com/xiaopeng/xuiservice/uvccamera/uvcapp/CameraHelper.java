package com.xiaopeng.xuiservice.uvccamera.uvcapp;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.usb.UsbDevice;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.uvccamera.usb.Format;
import com.xiaopeng.xuiservice.uvccamera.usb.IButtonCallback;
import com.xiaopeng.xuiservice.uvccamera.usb.IFrameCallback;
import com.xiaopeng.xuiservice.uvccamera.usb.Size;
import com.xiaopeng.xuiservice.uvccamera.usb.UVCControl;
import com.xiaopeng.xuiservice.uvccamera.usb.UVCParam;
import com.xiaopeng.xuiservice.uvccamera.utils.UVCUtils;
import com.xiaopeng.xuiservice.uvccamera.uvcapp.CameraHelper;
import com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper;
import java.lang.ref.WeakReference;
import java.util.List;
/* loaded from: classes5.dex */
public class CameraHelper implements ICameraHelper {
    private static final String TAG = CameraHelper.class.getSimpleName();
    private Handler mAsyncHandler;
    private HandlerThread mAsyncHandlerThread;
    protected ICameraHelper.StateCallback mCallbackWrapper;
    protected ICameraConnection mService;
    private UsbDevice mUsbDevice;
    protected final WeakReference<Context> mWeakContext;
    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    private CameraPreviewConfig mCameraPreviewConfig = new CameraPreviewConfig();

    public CameraHelper() {
        LogUtil.d(TAG, "Constructor:");
        this.mWeakContext = new WeakReference<>(UVCUtils.getApplication());
        this.mAsyncHandlerThread = new HandlerThread(TAG);
        this.mAsyncHandlerThread.start();
        this.mAsyncHandler = new Handler(this.mAsyncHandlerThread.getLooper());
        this.mService = CameraConnectionService.getInstance().newConnection();
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper
    public void setStateCallback(ICameraHelper.StateCallback callback) {
        if (callback != null) {
            this.mCallbackWrapper = new StateCallbackWrapper(callback);
            registerCallback();
            return;
        }
        unregisterCallback();
        this.mCallbackWrapper = null;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper
    public List<UsbDevice> getDeviceList() {
        LogUtil.d(TAG, "getDeviceList:");
        ICameraConnection iCameraConnection = this.mService;
        if (iCameraConnection != null) {
            try {
                return iCameraConnection.getDeviceList();
            } catch (Exception e) {
                LogUtil.e(TAG, "getDeviceList:");
                return null;
            }
        }
        return null;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper
    public void selectDevice(final UsbDevice device) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("selectDevice:device=");
        sb.append(device != null ? device.getDeviceName() : null);
        Log.d(str, sb.toString());
        this.mAsyncHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.-$$Lambda$CameraHelper$9OxcKLmdu8-xwyHygXOZ9YU--Gs
            @Override // java.lang.Runnable
            public final void run() {
                CameraHelper.this.lambda$selectDevice$0$CameraHelper(device);
            }
        });
    }

    public /* synthetic */ void lambda$selectDevice$0$CameraHelper(UsbDevice device) {
        ICameraConnection iCameraConnection = this.mService;
        if (iCameraConnection != null) {
            this.mUsbDevice = device;
            try {
                iCameraConnection.selectDevice(device);
            } catch (Exception e) {
                LogUtil.e(TAG, "selectDevice:");
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper
    public List<Format> getSupportedFormatList() {
        UsbDevice usbDevice;
        LogUtil.d(TAG, "getSupportedFormatList:");
        ICameraConnection iCameraConnection = this.mService;
        if (iCameraConnection != null && (usbDevice = this.mUsbDevice) != null) {
            try {
                return iCameraConnection.getSupportedFormatList(usbDevice);
            } catch (Exception e) {
                LogUtil.e(TAG, "getSupportedFormatList:");
                return null;
            }
        }
        return null;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper
    public List<Size> getSupportedSizeList() {
        UsbDevice usbDevice;
        LogUtil.d(TAG, "getSupportedSizeList:");
        ICameraConnection iCameraConnection = this.mService;
        if (iCameraConnection != null && (usbDevice = this.mUsbDevice) != null) {
            try {
                return iCameraConnection.getSupportedSizeList(usbDevice);
            } catch (Exception e) {
                LogUtil.e(TAG, "getSupportedSizeList:");
                return null;
            }
        }
        return null;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper
    public Size getPreviewSize() {
        UsbDevice usbDevice;
        LogUtil.d(TAG, "getPreviewSize:");
        ICameraConnection iCameraConnection = this.mService;
        if (iCameraConnection != null && (usbDevice = this.mUsbDevice) != null) {
            try {
                return iCameraConnection.getPreviewSize(usbDevice);
            } catch (Exception e) {
                LogUtil.e(TAG, "getPreviewSize:");
                return null;
            }
        }
        return null;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper
    public void setPreviewSize(final Size size) {
        String str = TAG;
        LogUtil.d(str, "setPreviewSize:" + size);
        this.mAsyncHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.-$$Lambda$CameraHelper$W6Y2Swr1bI8wvyMUufb9-JSAIRs
            @Override // java.lang.Runnable
            public final void run() {
                CameraHelper.this.lambda$setPreviewSize$1$CameraHelper(size);
            }
        });
    }

    public /* synthetic */ void lambda$setPreviewSize$1$CameraHelper(Size size) {
        UsbDevice usbDevice;
        ICameraConnection iCameraConnection = this.mService;
        if (iCameraConnection != null && (usbDevice = this.mUsbDevice) != null) {
            try {
                iCameraConnection.setPreviewSize(usbDevice, size);
            } catch (Exception e) {
                LogUtil.e(TAG, "setPreviewSize:");
            }
        }
    }

    private Object fetchSurface(Object surface) {
        Surface sur;
        if (surface instanceof SurfaceView) {
            SurfaceView surfaceView = (SurfaceView) surface;
            sur = surfaceView.getHolder().getSurface();
        } else if (surface instanceof SurfaceHolder) {
            SurfaceHolder holder = (SurfaceHolder) surface;
            sur = holder.getSurface();
        } else if ((surface instanceof Surface) || (surface instanceof SurfaceTexture)) {
            sur = surface;
        } else {
            throw new UnsupportedOperationException("addSurface() can only be called with an instance of Surface, SurfaceView, SurfaceTexture or SurfaceHolder at the moment.");
        }
        if (sur == null) {
            throw new UnsupportedOperationException("surface is null.");
        }
        return sur;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper
    public void addSurface(final Object surface, final boolean isRecordable) {
        String str = TAG;
        LogUtil.d(str, "addSurface:surface=" + surface + ",isRecordable=" + isRecordable);
        this.mAsyncHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.-$$Lambda$CameraHelper$eLG9WHAaEi0LvDvZMwp2sFgnNlw
            @Override // java.lang.Runnable
            public final void run() {
                CameraHelper.this.lambda$addSurface$2$CameraHelper(surface, isRecordable);
            }
        });
    }

    public /* synthetic */ void lambda$addSurface$2$CameraHelper(Object surface, boolean isRecordable) {
        ICameraConnection iCameraConnection;
        UsbDevice usbDevice;
        Object sur = fetchSurface(surface);
        if (sur != null && (iCameraConnection = this.mService) != null && (usbDevice = this.mUsbDevice) != null) {
            try {
                iCameraConnection.addSurface(usbDevice, sur, isRecordable);
            } catch (Exception e) {
                LogUtil.e(TAG, "addSurface:");
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper
    public void removeSurface(final Object surface) {
        String str = TAG;
        LogUtil.d(str, "removeSurface:surface=" + surface);
        this.mAsyncHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.-$$Lambda$CameraHelper$ztlRqdSp6Z-9_vV_uEEh1W1cRVI
            @Override // java.lang.Runnable
            public final void run() {
                CameraHelper.this.lambda$removeSurface$3$CameraHelper(surface);
            }
        });
    }

    public /* synthetic */ void lambda$removeSurface$3$CameraHelper(Object surface) {
        ICameraConnection iCameraConnection;
        UsbDevice usbDevice;
        Object sur = fetchSurface(surface);
        if (sur != null && (iCameraConnection = this.mService) != null && (usbDevice = this.mUsbDevice) != null) {
            try {
                iCameraConnection.removeSurface(usbDevice, sur);
            } catch (Exception e) {
                LogUtil.e(TAG, "handleRemoveSurface:");
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper
    public void setButtonCallback(final IButtonCallback callback) {
        String str = TAG;
        LogUtil.d(str, "setButtonCallback:" + callback);
        this.mAsyncHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.-$$Lambda$CameraHelper$ySNec7JtQD6Jllndyb3ptT9_OP8
            @Override // java.lang.Runnable
            public final void run() {
                CameraHelper.this.lambda$setButtonCallback$4$CameraHelper(callback);
            }
        });
    }

    public /* synthetic */ void lambda$setButtonCallback$4$CameraHelper(IButtonCallback callback) {
        UsbDevice usbDevice;
        ICameraConnection iCameraConnection = this.mService;
        if (iCameraConnection != null && (usbDevice = this.mUsbDevice) != null) {
            try {
                iCameraConnection.setButtonCallback(usbDevice, callback);
            } catch (Exception e) {
                LogUtil.e(TAG, "setButtonCallback:");
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper
    public void setFrameCallback(final IFrameCallback callback, final int pixelFormat) {
        String str = TAG;
        LogUtil.d(str, "setFrameCallback:" + pixelFormat);
        this.mAsyncHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.-$$Lambda$CameraHelper$8LC-IYqFh7IRWTBOM1rP_B6I9fg
            @Override // java.lang.Runnable
            public final void run() {
                CameraHelper.this.lambda$setFrameCallback$5$CameraHelper(callback, pixelFormat);
            }
        });
    }

    public /* synthetic */ void lambda$setFrameCallback$5$CameraHelper(IFrameCallback callback, int pixelFormat) {
        UsbDevice usbDevice;
        ICameraConnection iCameraConnection = this.mService;
        if (iCameraConnection != null && (usbDevice = this.mUsbDevice) != null) {
            try {
                iCameraConnection.setFrameCallback(usbDevice, callback, pixelFormat);
            } catch (Exception e) {
                LogUtil.e(TAG, "setFrameCallback:");
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper
    public void openCamera() {
        openCamera(new UVCParam());
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper
    public void openCamera(Size size) {
        openCamera(new UVCParam(size, 0));
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper
    public void openCamera(final UVCParam param) {
        LogUtil.d(TAG, "openCamera:");
        this.mAsyncHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.-$$Lambda$CameraHelper$8pOMeUrI0I81NaKXUYXeNlaN7BM
            @Override // java.lang.Runnable
            public final void run() {
                CameraHelper.this.lambda$openCamera$6$CameraHelper(param);
            }
        });
    }

    public /* synthetic */ void lambda$openCamera$6$CameraHelper(UVCParam param) {
        UsbDevice usbDevice;
        ICameraConnection iCameraConnection = this.mService;
        if (iCameraConnection != null && (usbDevice = this.mUsbDevice) != null) {
            try {
                if (!iCameraConnection.isCameraOpened(usbDevice)) {
                    this.mService.openCamera(this.mUsbDevice, param, this.mCameraPreviewConfig);
                }
            } catch (Exception e) {
                LogUtil.e(TAG, "openCamera:");
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper
    public void closeCamera() {
        String str = TAG;
        LogUtil.d(str, "closeCamera:" + this);
        this.mAsyncHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.-$$Lambda$CameraHelper$asprfX7B5Zbp6W_qAW_lRc1Y7oU
            @Override // java.lang.Runnable
            public final void run() {
                CameraHelper.this.lambda$closeCamera$7$CameraHelper();
            }
        });
    }

    public /* synthetic */ void lambda$closeCamera$7$CameraHelper() {
        UsbDevice usbDevice;
        ICameraConnection iCameraConnection = this.mService;
        if (iCameraConnection != null && (usbDevice = this.mUsbDevice) != null) {
            try {
                if (iCameraConnection.isCameraOpened(usbDevice)) {
                    this.mService.closeCamera(this.mUsbDevice);
                }
            } catch (Exception e) {
                LogUtil.e(TAG, "closeCamera:");
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper
    public void startPreview() {
        LogUtil.d(TAG, "startPreview:");
        this.mAsyncHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.-$$Lambda$CameraHelper$RXiF_A8fG7K6MprMNfm_GyLc7uc
            @Override // java.lang.Runnable
            public final void run() {
                CameraHelper.this.lambda$startPreview$8$CameraHelper();
            }
        });
    }

    public /* synthetic */ void lambda$startPreview$8$CameraHelper() {
        UsbDevice usbDevice;
        ICameraConnection iCameraConnection = this.mService;
        if (iCameraConnection != null && (usbDevice = this.mUsbDevice) != null) {
            try {
                iCameraConnection.startPreview(usbDevice);
            } catch (Exception e) {
                LogUtil.e(TAG, "startPreview:");
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper
    public void stopPreview() {
        LogUtil.d(TAG, "stopPreview:");
        this.mAsyncHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.-$$Lambda$CameraHelper$3STJFtPrpTb08guglJjIAKJ38qM
            @Override // java.lang.Runnable
            public final void run() {
                CameraHelper.this.lambda$stopPreview$9$CameraHelper();
            }
        });
    }

    public /* synthetic */ void lambda$stopPreview$9$CameraHelper() {
        UsbDevice usbDevice;
        ICameraConnection iCameraConnection = this.mService;
        if (iCameraConnection != null && (usbDevice = this.mUsbDevice) != null) {
            try {
                iCameraConnection.stopPreview(usbDevice);
            } catch (Exception e) {
                LogUtil.e(TAG, "stopPreview:");
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper
    public UVCControl getUVCControl() {
        UsbDevice usbDevice;
        LogUtil.d(TAG, "getUVCControl:");
        ICameraConnection iCameraConnection = this.mService;
        if (iCameraConnection != null && (usbDevice = this.mUsbDevice) != null) {
            try {
                return iCameraConnection.getUVCControl(usbDevice);
            } catch (Exception e) {
                LogUtil.e(TAG, "getUVCControl:");
                return null;
            }
        }
        return null;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper
    public boolean isCameraOpened() {
        UsbDevice usbDevice;
        LogUtil.d(TAG, "isCameraOpened:");
        ICameraConnection iCameraConnection = this.mService;
        if (iCameraConnection != null && (usbDevice = this.mUsbDevice) != null) {
            try {
                return iCameraConnection.isCameraOpened(usbDevice);
            } catch (Exception e) {
                LogUtil.e(TAG, "isCameraOpened:");
                return false;
            }
        }
        return false;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper
    public void release() {
        String str = TAG;
        LogUtil.d(str, "release:" + this);
        this.mAsyncHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.-$$Lambda$CameraHelper$jWLXbA7REsQTHiPeIvGlCoLs0po
            @Override // java.lang.Runnable
            public final void run() {
                CameraHelper.this.lambda$release$10$CameraHelper();
            }
        });
    }

    public /* synthetic */ void lambda$release$10$CameraHelper() {
        ICameraConnection iCameraConnection = this.mService;
        if (iCameraConnection != null) {
            try {
                if (this.mUsbDevice != null) {
                    iCameraConnection.releaseCamera(this.mUsbDevice);
                }
                this.mService.release();
            } catch (Exception e) {
                LogUtil.e(TAG, "release:");
            }
            this.mCallbackWrapper = null;
            this.mService = null;
        }
        this.mUsbDevice = null;
        this.mAsyncHandlerThread.quitSafely();
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper
    public void releaseAll() {
        String str = TAG;
        LogUtil.d(str, "releaseAll:" + this);
        this.mAsyncHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.-$$Lambda$CameraHelper$pnelPzs3VLLoDP8yxZ7wdN4hwS4
            @Override // java.lang.Runnable
            public final void run() {
                CameraHelper.this.lambda$releaseAll$11$CameraHelper();
            }
        });
    }

    public /* synthetic */ void lambda$releaseAll$11$CameraHelper() {
        ICameraConnection iCameraConnection = this.mService;
        if (iCameraConnection != null) {
            try {
                iCameraConnection.releaseAllCamera();
                this.mService.release();
            } catch (Exception e) {
                LogUtil.e(TAG, "release:");
            }
            this.mCallbackWrapper = null;
            this.mService = null;
        }
        this.mUsbDevice = null;
        this.mAsyncHandlerThread.quitSafely();
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper
    public CameraPreviewConfig getPreviewConfig() {
        return this.mCameraPreviewConfig;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper
    public void setPreviewConfig(CameraPreviewConfig config) {
        LogUtil.d(TAG, "setCameraPreviewConfig:");
        this.mCameraPreviewConfig = config;
        this.mAsyncHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.-$$Lambda$CameraHelper$qit10usIlMVS6jh06wGjzt9cQlM
            @Override // java.lang.Runnable
            public final void run() {
                CameraHelper.this.lambda$setPreviewConfig$12$CameraHelper();
            }
        });
    }

    public /* synthetic */ void lambda$setPreviewConfig$12$CameraHelper() {
        ICameraConnection iCameraConnection;
        UsbDevice usbDevice;
        if (isCameraOpened() && (iCameraConnection = this.mService) != null && (usbDevice = this.mUsbDevice) != null) {
            try {
                iCameraConnection.setPreviewConfig(usbDevice, this.mCameraPreviewConfig);
            } catch (Exception e) {
                LogUtil.e(TAG, "setCameraPreviewConfig:");
            }
        }
    }

    public void registerCallback() {
        LogUtil.d(TAG, "registerCallback:");
        ICameraConnection iCameraConnection = this.mService;
        if (iCameraConnection != null) {
            try {
                iCameraConnection.register(this.mCallbackWrapper);
            } catch (Exception e) {
                LogUtil.e(TAG, "registerCallback:");
            }
        }
    }

    public void unregisterCallback() {
        LogUtil.d(TAG, "unregisterCallback:");
        ICameraConnection iCameraConnection = this.mService;
        if (iCameraConnection != null) {
            try {
                iCameraConnection.unregister(this.mCallbackWrapper);
            } catch (Exception e) {
                LogUtil.e(TAG, "unregisterCallback:");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public final class StateCallbackWrapper implements ICameraHelper.StateCallback {
        private ICameraHelper.StateCallback mCallback;

        StateCallbackWrapper(ICameraHelper.StateCallback callback) {
            this.mCallback = callback;
        }

        public /* synthetic */ void lambda$onAttach$0$CameraHelper$StateCallbackWrapper(UsbDevice device) {
            this.mCallback.onAttach(device);
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper.StateCallback
        public void onAttach(final UsbDevice device) {
            CameraHelper.this.mMainHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.-$$Lambda$CameraHelper$StateCallbackWrapper$b_kzaPLmqC5XS6aleT6RmsYmIb8
                @Override // java.lang.Runnable
                public final void run() {
                    CameraHelper.StateCallbackWrapper.this.lambda$onAttach$0$CameraHelper$StateCallbackWrapper(device);
                }
            });
        }

        public /* synthetic */ void lambda$onDeviceOpen$1$CameraHelper$StateCallbackWrapper(UsbDevice device, boolean isFirstOpen) {
            this.mCallback.onDeviceOpen(device, isFirstOpen);
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper.StateCallback
        public void onDeviceOpen(final UsbDevice device, final boolean isFirstOpen) {
            CameraHelper.this.mMainHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.-$$Lambda$CameraHelper$StateCallbackWrapper$i72HX8_CpnC2WKf0M4YKiLlTja8
                @Override // java.lang.Runnable
                public final void run() {
                    CameraHelper.StateCallbackWrapper.this.lambda$onDeviceOpen$1$CameraHelper$StateCallbackWrapper(device, isFirstOpen);
                }
            });
        }

        public /* synthetic */ void lambda$onCameraOpen$2$CameraHelper$StateCallbackWrapper(UsbDevice device) {
            this.mCallback.onCameraOpen(device);
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper.StateCallback
        public void onCameraOpen(final UsbDevice device) {
            CameraHelper.this.mMainHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.-$$Lambda$CameraHelper$StateCallbackWrapper$KYlZkQ4aAMK0xcXAcUcf8r-jMDk
                @Override // java.lang.Runnable
                public final void run() {
                    CameraHelper.StateCallbackWrapper.this.lambda$onCameraOpen$2$CameraHelper$StateCallbackWrapper(device);
                }
            });
        }

        public /* synthetic */ void lambda$onCameraClose$3$CameraHelper$StateCallbackWrapper(UsbDevice device) {
            this.mCallback.onCameraClose(device);
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper.StateCallback
        public void onCameraClose(final UsbDevice device) {
            CameraHelper.this.mMainHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.-$$Lambda$CameraHelper$StateCallbackWrapper$_Uetvk1jFusyyz6UZ2Dy5zpwlXQ
                @Override // java.lang.Runnable
                public final void run() {
                    CameraHelper.StateCallbackWrapper.this.lambda$onCameraClose$3$CameraHelper$StateCallbackWrapper(device);
                }
            });
        }

        public /* synthetic */ void lambda$onDeviceClose$4$CameraHelper$StateCallbackWrapper(UsbDevice device) {
            this.mCallback.onDeviceClose(device);
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper.StateCallback
        public void onDeviceClose(final UsbDevice device) {
            CameraHelper.this.mMainHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.-$$Lambda$CameraHelper$StateCallbackWrapper$YVyfGXcw0O_1rubSkKwjvKXKq74
                @Override // java.lang.Runnable
                public final void run() {
                    CameraHelper.StateCallbackWrapper.this.lambda$onDeviceClose$4$CameraHelper$StateCallbackWrapper(device);
                }
            });
        }

        public /* synthetic */ void lambda$onDetach$5$CameraHelper$StateCallbackWrapper(UsbDevice device) {
            this.mCallback.onDetach(device);
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper.StateCallback
        public void onDetach(final UsbDevice device) {
            CameraHelper.this.mMainHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.-$$Lambda$CameraHelper$StateCallbackWrapper$TvqrMLFsiMi0GVUK5avWOOF_HeQ
                @Override // java.lang.Runnable
                public final void run() {
                    CameraHelper.StateCallbackWrapper.this.lambda$onDetach$5$CameraHelper$StateCallbackWrapper(device);
                }
            });
        }

        public /* synthetic */ void lambda$onCancel$6$CameraHelper$StateCallbackWrapper(UsbDevice device) {
            this.mCallback.onCancel(device);
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper.StateCallback
        public void onCancel(final UsbDevice device) {
            CameraHelper.this.mMainHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.-$$Lambda$CameraHelper$StateCallbackWrapper$wuVKjuen6qdAl79vFNj_HFX_1Ms
                @Override // java.lang.Runnable
                public final void run() {
                    CameraHelper.StateCallbackWrapper.this.lambda$onCancel$6$CameraHelper$StateCallbackWrapper(device);
                }
            });
        }
    }
}
