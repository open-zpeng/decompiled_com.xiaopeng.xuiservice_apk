package com.xiaopeng.xuiservice.uvccamera.uvcapp;

import android.hardware.usb.UsbDevice;
import com.xiaopeng.xuiservice.uvccamera.usb.Format;
import com.xiaopeng.xuiservice.uvccamera.usb.IButtonCallback;
import com.xiaopeng.xuiservice.uvccamera.usb.IFrameCallback;
import com.xiaopeng.xuiservice.uvccamera.usb.Size;
import com.xiaopeng.xuiservice.uvccamera.usb.UVCControl;
import com.xiaopeng.xuiservice.uvccamera.usb.UVCParam;
import com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper;
import java.util.List;
/* loaded from: classes5.dex */
interface ICameraConnection {
    void addSurface(UsbDevice usbDevice, Object obj, boolean z);

    void closeCamera(UsbDevice usbDevice);

    List<UsbDevice> getDeviceList();

    Size getPreviewSize(UsbDevice usbDevice);

    List<Format> getSupportedFormatList(UsbDevice usbDevice);

    List<Size> getSupportedSizeList(UsbDevice usbDevice);

    UVCControl getUVCControl(UsbDevice usbDevice);

    boolean isCameraOpened(UsbDevice usbDevice);

    void openCamera(UsbDevice usbDevice, UVCParam uVCParam, CameraPreviewConfig cameraPreviewConfig);

    void register(ICameraHelper.StateCallback stateCallback);

    void release();

    void releaseAllCamera();

    void releaseCamera(UsbDevice usbDevice);

    void removeSurface(UsbDevice usbDevice, Object obj);

    void selectDevice(UsbDevice usbDevice);

    void setButtonCallback(UsbDevice usbDevice, IButtonCallback iButtonCallback);

    void setFrameCallback(UsbDevice usbDevice, IFrameCallback iFrameCallback, int i);

    void setPreviewConfig(UsbDevice usbDevice, CameraPreviewConfig cameraPreviewConfig);

    void setPreviewSize(UsbDevice usbDevice, Size size);

    void startPreview(UsbDevice usbDevice);

    void stopPreview(UsbDevice usbDevice);

    void unregister(ICameraHelper.StateCallback stateCallback);
}
