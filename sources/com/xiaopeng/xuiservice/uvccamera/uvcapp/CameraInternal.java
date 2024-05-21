package com.xiaopeng.xuiservice.uvccamera.uvcapp;

import android.content.Context;
import android.util.Log;
import android.view.Surface;
import com.xiaopeng.xui.app.XToast;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.uvccamera.opengl.renderer.RendererHolderCallback;
import com.xiaopeng.xuiservice.uvccamera.usb.Format;
import com.xiaopeng.xuiservice.uvccamera.usb.IButtonCallback;
import com.xiaopeng.xuiservice.uvccamera.usb.IFrameCallback;
import com.xiaopeng.xuiservice.uvccamera.usb.Size;
import com.xiaopeng.xuiservice.uvccamera.usb.USBMonitor;
import com.xiaopeng.xuiservice.uvccamera.usb.UVCCamera;
import com.xiaopeng.xuiservice.uvccamera.usb.UVCControl;
import com.xiaopeng.xuiservice.uvccamera.usb.UVCParam;
import com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraInternal;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes5.dex */
public final class CameraInternal implements ICameraInternal {
    private static final int DEFAULT_HEIGHT = 480;
    private static final int DEFAULT_WIDTH = 640;
    private static final String KEY_ARG_1 = "key_arg1";
    private static final String KEY_ARG_2 = "key_arg2";
    private static final String TAG = CameraInternal.class.getSimpleName();
    private final USBMonitor.UsbControlBlock mCtrlBlock;
    private ICameraRendererHolder mRendererHolder;
    private volatile UVCCamera mUVCCamera;
    private final WeakReference<Context> mWeakContext;
    private int mFrameWidth = 640;
    private int mFrameHeight = 480;
    private final Object mSync = new Object();
    private volatile boolean mIsPreviewing = false;
    private final List<ICameraInternal.StateCallback> mCallbacks = new ArrayList();

    public CameraInternal(Context context, USBMonitor.UsbControlBlock ctrlBlock, int vid, int pid) {
        LogUtil.d(TAG, "Constructor:");
        this.mWeakContext = new WeakReference<>(context);
        this.mCtrlBlock = ctrlBlock;
        this.mRendererHolder = new CameraRendererHolder(this.mFrameWidth, this.mFrameHeight, new RendererHolderCallback() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.CameraInternal.1
            @Override // com.xiaopeng.xuiservice.uvccamera.opengl.renderer.RendererHolderCallback
            public void onPrimarySurfaceCreate(Surface surface) {
                if (CameraInternal.this.mIsPreviewing) {
                    CameraInternal.this.startPreview();
                }
            }

            @Override // com.xiaopeng.xuiservice.uvccamera.opengl.renderer.RendererHolderCallback
            public void onFrameAvailable() {
            }

            @Override // com.xiaopeng.xuiservice.uvccamera.opengl.renderer.RendererHolderCallback
            public void onPrimarySurfaceDestroy() {
            }
        });
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraInternal
    public void registerCallback(ICameraInternal.StateCallback callback) {
        LogUtil.d(TAG, "registerCallback:");
        this.mCallbacks.add(callback);
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraInternal
    public void unregisterCallback(ICameraInternal.StateCallback callback) {
        LogUtil.d(TAG, "unregisterCallback:");
        this.mCallbacks.remove(callback);
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraInternal
    public void clearCallbacks() {
        LogUtil.d(TAG, "clearCallbacks:");
        this.mCallbacks.clear();
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraInternal
    public List<Format> getSupportedFormatList() {
        if (this.mUVCCamera != null) {
            return this.mUVCCamera.getSupportedFormatList();
        }
        return null;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraInternal
    public List<Size> getSupportedSizeList() {
        if (this.mUVCCamera != null) {
            return this.mUVCCamera.getSupportedSizeList();
        }
        return null;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraInternal
    public Size getPreviewSize() {
        if (this.mUVCCamera != null) {
            return this.mUVCCamera.getPreviewSize();
        }
        return null;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraInternal
    public void setPreviewSize(Size size) {
        String str = TAG;
        LogUtil.d(str, "setPreviewSize:" + size);
        try {
            this.mUVCCamera.setPreviewSize(size);
        } catch (Exception e) {
            Log.e(TAG, "setPreviewSize:", e);
            this.mUVCCamera.destroy();
            this.mUVCCamera = null;
        }
    }

    private void updateRendererSize(int width, int height) {
        LogUtil.d(TAG, "updateRendererSize:");
        this.mFrameWidth = width;
        this.mFrameHeight = height;
        ICameraRendererHolder iCameraRendererHolder = this.mRendererHolder;
        if (iCameraRendererHolder != null) {
            iCameraRendererHolder.updatePrimarySize(width, height);
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraInternal
    public void addSurface(Object surface, boolean isRecordable) {
        String str = TAG;
        LogUtil.d(str, "addSurface:surface=" + surface);
        ICameraRendererHolder iCameraRendererHolder = this.mRendererHolder;
        if (iCameraRendererHolder != null) {
            iCameraRendererHolder.addSlaveSurface(surface.hashCode(), surface, isRecordable);
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraInternal
    public void removeSurface(Object surface) {
        String str = TAG;
        LogUtil.d(str, "removeSurface:surface=" + surface);
        ICameraRendererHolder iCameraRendererHolder = this.mRendererHolder;
        if (iCameraRendererHolder != null) {
            iCameraRendererHolder.removeSlaveSurface(surface.hashCode());
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraInternal
    public void setButtonCallback(IButtonCallback callback) {
        String str = TAG;
        LogUtil.d(str, "setButtonCallback:callback=" + callback);
        try {
            this.mUVCCamera.setButtonCallback(callback);
        } catch (Exception e) {
            Log.e(TAG, "setButtonCallback:", e);
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraInternal
    public void setFrameCallback(IFrameCallback callback, int pixelFormat) {
        String str = TAG;
        LogUtil.d(str, "setFrameCallback:surface=" + callback);
        try {
            this.mUVCCamera.setFrameCallback(callback, pixelFormat);
        } catch (Exception e) {
            Log.e(TAG, "setFrameCallback:", e);
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraInternal
    public void openCamera(UVCParam param, CameraPreviewConfig previewConfig) {
        LogUtil.d(TAG, "openCamera:");
        if (!isCameraOpened()) {
            openUVCCamera(param, previewConfig);
            return;
        }
        LogUtil.d(TAG, "have already opened camera, just call callback");
        processOnCameraOpen();
    }

    private void resetUVCCamera() {
        LogUtil.d(TAG, "resetUVCCamera:");
        synchronized (this.mSync) {
            if (this.mUVCCamera != null) {
                this.mUVCCamera.stopPreview();
                this.mUVCCamera.destroy(true);
                this.mUVCCamera = null;
            }
            this.mRendererHolder.removeSlaveSurfaceAll();
            this.mSync.notifyAll();
        }
    }

    private void openUVCCamera(UVCParam param, CameraPreviewConfig previewConfig) {
        String tip;
        resetUVCCamera();
        LogUtil.d(TAG, "openUVCCamera:");
        try {
            synchronized (this.mSync) {
                this.mUVCCamera = new UVCCamera(param);
                int result = this.mUVCCamera.open(this.mCtrlBlock);
                if (result != 0) {
                    Context context = this.mWeakContext.get();
                    if (result == -6) {
                        tip = context.getString(R.string.uvc_error_busy_need_replug);
                    } else {
                        tip = context.getString(R.string.uvc_error_unknown_need_replug);
                    }
                    XToast.showShort(tip);
                    throw new IllegalStateException("open failed:result=" + result);
                }
                String str = TAG;
                LogUtil.i(str, "supportedSize:" + this.mUVCCamera.getSupportedSize());
            }
            setPreviewConfig(previewConfig);
            processOnCameraOpen();
        } catch (Exception e) {
            LogUtil.e(TAG, "openUVCCamera:");
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraInternal
    public void closeCamera() {
        LogUtil.d(TAG, "closeCamera:");
        boolean closed = false;
        synchronized (this.mSync) {
            if (this.mUVCCamera != null) {
                this.mUVCCamera.stopPreview();
                this.mUVCCamera.destroy();
                this.mUVCCamera = null;
                closed = true;
            }
            if (closed) {
                processOnCameraClose();
            }
            this.mSync.notifyAll();
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraInternal
    public void startPreview() {
        LogUtil.d(TAG, "startPreview:");
        synchronized (this.mSync) {
            if (this.mUVCCamera == null) {
                return;
            }
            Size size = this.mUVCCamera.getPreviewSize();
            if (size != null) {
                updateRendererSize(size.width, size.height);
            }
            this.mUVCCamera.setPreviewDisplay(this.mRendererHolder.getPrimarySurface());
            this.mUVCCamera.startPreview();
            this.mIsPreviewing = true;
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraInternal
    public void stopPreview() {
        LogUtil.d(TAG, "stopPreview:");
        synchronized (this.mSync) {
            if (this.mUVCCamera != null) {
                this.mUVCCamera.stopPreview();
            }
            this.mIsPreviewing = false;
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraInternal
    public UVCControl getUVCControl() {
        if (this.mUVCCamera != null) {
            return this.mUVCCamera.getControl();
        }
        return null;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraInternal
    public boolean isCameraOpened() {
        return this.mUVCCamera != null;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraInternal
    public void release() {
        LogUtil.d(TAG, "release:");
        if (isCameraOpened()) {
            closeCamera();
        }
        releaseResource();
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraInternal
    public void setPreviewConfig(CameraPreviewConfig config) {
        LogUtil.d(TAG, "setCameraPreviewConfig:");
        int rotation = config.getRotation();
        String str = TAG;
        LogUtil.d(str, "rotateTo:" + rotation);
        ICameraRendererHolder iCameraRendererHolder = this.mRendererHolder;
        if (iCameraRendererHolder != null) {
            iCameraRendererHolder.rotateTo(rotation);
        }
        int mirror = config.getMirror();
        String str2 = TAG;
        LogUtil.d(str2, "setMirrorMode:" + mirror);
        ICameraRendererHolder iCameraRendererHolder2 = this.mRendererHolder;
        if (iCameraRendererHolder2 != null) {
            iCameraRendererHolder2.setMirrorMode(mirror);
        }
    }

    private void releaseResource() {
        LogUtil.d(TAG, "releaseResource");
        synchronized (this.mSync) {
            clearCallbacks();
            if (this.mRendererHolder != null) {
                this.mRendererHolder.release();
                this.mRendererHolder = null;
            }
            this.mSync.notifyAll();
        }
    }

    private void processOnCameraOpen() {
        LogUtil.d(TAG, "processOnCameraOpen:");
        try {
            for (ICameraInternal.StateCallback callback : this.mCallbacks) {
                try {
                    callback.onCameraOpen();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            Log.w(TAG, e2);
        }
    }

    private void processOnCameraClose() {
        LogUtil.d(TAG, "processOnCameraClose:");
        for (ICameraInternal.StateCallback callback : this.mCallbacks) {
            try {
                callback.onCameraClose();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
