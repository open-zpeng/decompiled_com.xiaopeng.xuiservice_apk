package com.xiaopeng.xuiservice.uvccamera.uvcapp;

import android.hardware.usb.UsbDevice;
import com.xiaopeng.xuiservice.uvccamera.usb.Format;
import com.xiaopeng.xuiservice.uvccamera.usb.IButtonCallback;
import com.xiaopeng.xuiservice.uvccamera.usb.IFrameCallback;
import com.xiaopeng.xuiservice.uvccamera.usb.Size;
import com.xiaopeng.xuiservice.uvccamera.usb.UVCControl;
import com.xiaopeng.xuiservice.uvccamera.usb.UVCParam;
import java.util.List;
/* loaded from: classes5.dex */
public interface ICameraHelper {

    /* loaded from: classes5.dex */
    public interface StateCallback {
        void onAttach(UsbDevice usbDevice);

        void onCameraClose(UsbDevice usbDevice);

        void onCameraOpen(UsbDevice usbDevice);

        void onCancel(UsbDevice usbDevice);

        void onDetach(UsbDevice usbDevice);

        void onDeviceClose(UsbDevice usbDevice);

        void onDeviceOpen(UsbDevice usbDevice, boolean z);
    }

    void addSurface(Object obj, boolean z);

    void closeCamera();

    List<UsbDevice> getDeviceList();

    CameraPreviewConfig getPreviewConfig();

    Size getPreviewSize();

    List<Format> getSupportedFormatList();

    List<Size> getSupportedSizeList();

    UVCControl getUVCControl();

    boolean isCameraOpened();

    void openCamera();

    void openCamera(Size size);

    void openCamera(UVCParam uVCParam);

    void release();

    void releaseAll();

    void removeSurface(Object obj);

    void selectDevice(UsbDevice usbDevice);

    void setButtonCallback(IButtonCallback iButtonCallback);

    void setFrameCallback(IFrameCallback iFrameCallback, int i);

    void setPreviewConfig(CameraPreviewConfig cameraPreviewConfig);

    void setPreviewSize(Size size);

    void setStateCallback(StateCallback stateCallback);

    void startPreview();

    void stopPreview();
}
