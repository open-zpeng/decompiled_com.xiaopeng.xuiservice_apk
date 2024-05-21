package com.xiaopeng.xuiservice.uvccamera.uvcapp.activity;

import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.TextureView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.xiaopeng.xui.app.XToast;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.uvccamera.usb.IButtonCallback;
import com.xiaopeng.xuiservice.uvccamera.usb.Size;
import com.xiaopeng.xuiservice.uvccamera.usb.USBMonitor;
import com.xiaopeng.xuiservice.uvccamera.uvcapp.CameraHelper;
import com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper;
import com.xiaopeng.xuiservice.uvccamera.widget.AspectRatioTextureView;
/* loaded from: classes5.dex */
public class PreviewActivity extends AppCompatActivity {
    private static final int DEFAULT_HEIGHT = 480;
    private static final int DEFAULT_WIDTH = 640;
    private static final String SAVED_PREVIEW_SIZE = "saved_preview_size";
    private static final String TAG = PreviewActivity.class.getSimpleName();
    private ICameraHelper mCameraHelper;
    private UsbDevice mUsbDevice;
    private TextView tvConnectUSBCameraTip;
    private AspectRatioTextureView viewMainPreview;
    private int mPreviewWidth = 640;
    private int mPreviewHeight = 480;
    private final ICameraHelper.StateCallback mStateCallback = new MyCameraHelperCallback();
    private boolean mIsCameraConnected = false;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(TAG, "create instance");
        setContentView(R.layout.activity_preview);
        this.viewMainPreview = (AspectRatioTextureView) findViewById(R.id.viewMainPreview);
        this.tvConnectUSBCameraTip = (TextView) findViewById(R.id.tvConnectUSBCameraTip);
        checkCameraHelper();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtil.d(TAG, "onNewIntent");
        if (intent.getAction().equals(USBMonitor.ACTION_USB_DEVICE_ATTACHED) && !this.mIsCameraConnected) {
            this.mUsbDevice = (UsbDevice) intent.getParcelableExtra("device");
            selectDevice(this.mUsbDevice);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        initPreviewView();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        clearCameraHelper();
    }

    private void checkCameraHelper() {
        if (!this.mIsCameraConnected) {
            clearCameraHelper();
        }
        initCameraHelper();
    }

    private void initCameraHelper() {
        if (this.mCameraHelper == null) {
            this.mCameraHelper = new CameraHelper();
            this.mCameraHelper.setStateCallback(this.mStateCallback);
        }
    }

    private void clearCameraHelper() {
        LogUtil.v(TAG, "clearCameraHelper:");
        ICameraHelper iCameraHelper = this.mCameraHelper;
        if (iCameraHelper != null) {
            iCameraHelper.release();
            this.mCameraHelper = null;
        }
    }

    private void initPreviewView() {
        this.viewMainPreview.setAspectRatio(this.mPreviewWidth, this.mPreviewHeight);
        this.viewMainPreview.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.activity.PreviewActivity.1
            @Override // android.view.TextureView.SurfaceTextureListener
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                if (PreviewActivity.this.mCameraHelper != null) {
                    PreviewActivity.this.mCameraHelper.addSurface(surface, false);
                }
            }

            @Override // android.view.TextureView.SurfaceTextureListener
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {
            }

            @Override // android.view.TextureView.SurfaceTextureListener
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                if (PreviewActivity.this.mCameraHelper != null) {
                    PreviewActivity.this.mCameraHelper.removeSurface(surface);
                    return false;
                }
                return false;
            }

            @Override // android.view.TextureView.SurfaceTextureListener
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {
            }
        });
    }

    public void attachNewDevice(UsbDevice device) {
        if (this.mUsbDevice == null) {
            this.mUsbDevice = device;
            selectDevice(device);
        }
    }

    protected void selectDevice(UsbDevice device) {
        String str = TAG;
        LogUtil.v(str, "selectDevice:device=" + device.getDeviceName());
        this.mIsCameraConnected = false;
        ICameraHelper iCameraHelper = this.mCameraHelper;
        if (iCameraHelper != null) {
            iCameraHelper.selectDevice(device);
        }
    }

    /* loaded from: classes5.dex */
    private class MyCameraHelperCallback implements ICameraHelper.StateCallback {
        private MyCameraHelperCallback() {
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper.StateCallback
        public void onAttach(UsbDevice device) {
            String str = PreviewActivity.TAG;
            LogUtil.v(str, "onAttach:device=" + device.getDeviceName());
            PreviewActivity.this.attachNewDevice(device);
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper.StateCallback
        public void onDeviceOpen(UsbDevice device, boolean isFirstOpen) {
            String str = PreviewActivity.TAG;
            LogUtil.v(str, "onDeviceOpen:device=" + device.getDeviceName());
            PreviewActivity.this.mCameraHelper.openCamera(PreviewActivity.this.getSavedPreviewSize());
            PreviewActivity.this.mCameraHelper.setButtonCallback(new IButtonCallback() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.activity.PreviewActivity.MyCameraHelperCallback.1
                @Override // com.xiaopeng.xuiservice.uvccamera.usb.IButtonCallback
                public void onButton(int button, int state) {
                    PreviewActivity previewActivity = PreviewActivity.this;
                    Toast.makeText(previewActivity, "onButton(button=" + button + "; state=" + state + ")", 0).show();
                }
            });
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper.StateCallback
        public void onCameraOpen(UsbDevice device) {
            String str = PreviewActivity.TAG;
            LogUtil.v(str, "onCameraOpen:device=" + device.getDeviceName());
            PreviewActivity.this.mCameraHelper.startPreview();
            Size size = PreviewActivity.this.mCameraHelper.getPreviewSize();
            if (size != null) {
                PreviewActivity.this.resizePreviewView(size);
            }
            if (PreviewActivity.this.viewMainPreview.getSurfaceTexture() != null) {
                PreviewActivity.this.mCameraHelper.addSurface(PreviewActivity.this.viewMainPreview.getSurfaceTexture(), false);
            }
            PreviewActivity.this.mIsCameraConnected = true;
            XToast.show((int) R.string.uvc_connection_success);
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper.StateCallback
        public void onCameraClose(UsbDevice device) {
            String str = PreviewActivity.TAG;
            LogUtil.v(str, "onCameraClose:device=" + device.getDeviceName());
            if (PreviewActivity.this.mCameraHelper != null && PreviewActivity.this.viewMainPreview.getSurfaceTexture() != null) {
                PreviewActivity.this.mCameraHelper.removeSurface(PreviewActivity.this.viewMainPreview.getSurfaceTexture());
            }
            PreviewActivity.this.mIsCameraConnected = false;
            XToast.show((int) R.string.uvc_connection_fail);
            PreviewActivity.this.finish();
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper.StateCallback
        public void onDeviceClose(UsbDevice device) {
            String str = PreviewActivity.TAG;
            LogUtil.v(str, "onDeviceClose:device=" + device.getDeviceName());
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper.StateCallback
        public void onDetach(UsbDevice device) {
            String str = PreviewActivity.TAG;
            LogUtil.v(str, "onDetach:device=" + device.getDeviceName());
            if (device.equals(PreviewActivity.this.mUsbDevice)) {
                PreviewActivity.this.mUsbDevice = null;
            }
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper.StateCallback
        public void onCancel(UsbDevice device) {
            String str = PreviewActivity.TAG;
            LogUtil.v(str, "onCancel:device=" + device.getDeviceName());
            if (device.equals(PreviewActivity.this.mUsbDevice)) {
                PreviewActivity.this.mUsbDevice = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resizePreviewView(Size size) {
        this.mPreviewWidth = size.width;
        this.mPreviewHeight = size.height;
        this.viewMainPreview.setAspectRatio(this.mPreviewWidth, this.mPreviewHeight);
    }

    private void updateUIControls() {
        runOnUiThread(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.activity.-$$Lambda$PreviewActivity$6fO8qxHt9Na4o5astlwG79rAYvc
            @Override // java.lang.Runnable
            public final void run() {
                PreviewActivity.this.lambda$updateUIControls$0$PreviewActivity();
            }
        });
    }

    public /* synthetic */ void lambda$updateUIControls$0$PreviewActivity() {
        if (this.mIsCameraConnected) {
            this.viewMainPreview.setVisibility(0);
            this.tvConnectUSBCameraTip.setVisibility(8);
            XToast.show((int) R.string.uvc_connection_success);
            return;
        }
        this.viewMainPreview.setVisibility(8);
        this.tvConnectUSBCameraTip.setVisibility(0);
        XToast.show((int) R.string.uvc_connection_fail);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Size getSavedPreviewSize() {
        String key = SAVED_PREVIEW_SIZE + USBMonitor.getProductKey(this.mUsbDevice);
        String sizeStr = getPreferences(0).getString(key, null);
        if (TextUtils.isEmpty(sizeStr)) {
            return null;
        }
        Gson gson = new Gson();
        return (Size) gson.fromJson(sizeStr, (Class<Object>) Size.class);
    }

    private void setSavedPreviewSize(Size size) {
        String key = SAVED_PREVIEW_SIZE + USBMonitor.getProductKey(this.mUsbDevice);
        Gson gson = new Gson();
        String json = gson.toJson(size);
        getPreferences(0).edit().putString(key, json).apply();
    }
}
