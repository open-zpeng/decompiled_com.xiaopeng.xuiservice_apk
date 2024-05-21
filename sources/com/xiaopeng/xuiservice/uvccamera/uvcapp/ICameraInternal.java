package com.xiaopeng.xuiservice.uvccamera.uvcapp;

import com.xiaopeng.xuiservice.uvccamera.usb.Format;
import com.xiaopeng.xuiservice.uvccamera.usb.IButtonCallback;
import com.xiaopeng.xuiservice.uvccamera.usb.IFrameCallback;
import com.xiaopeng.xuiservice.uvccamera.usb.Size;
import com.xiaopeng.xuiservice.uvccamera.usb.UVCControl;
import com.xiaopeng.xuiservice.uvccamera.usb.UVCParam;
import java.util.List;
/* loaded from: classes5.dex */
interface ICameraInternal {

    /* loaded from: classes5.dex */
    public interface StateCallback {
        void onCameraClose();

        void onCameraOpen();
    }

    void addSurface(Object obj, boolean z);

    void clearCallbacks();

    void closeCamera();

    Size getPreviewSize();

    List<Format> getSupportedFormatList();

    List<Size> getSupportedSizeList();

    UVCControl getUVCControl();

    boolean isCameraOpened();

    void openCamera(UVCParam uVCParam, CameraPreviewConfig cameraPreviewConfig);

    void registerCallback(StateCallback stateCallback);

    void release();

    void removeSurface(Object obj);

    void setButtonCallback(IButtonCallback iButtonCallback);

    void setFrameCallback(IFrameCallback iFrameCallback, int i);

    void setPreviewConfig(CameraPreviewConfig cameraPreviewConfig);

    void setPreviewSize(Size size);

    void startPreview();

    void stopPreview();

    void unregisterCallback(StateCallback stateCallback);
}
