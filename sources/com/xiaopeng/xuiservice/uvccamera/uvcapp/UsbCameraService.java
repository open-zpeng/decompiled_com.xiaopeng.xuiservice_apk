package com.xiaopeng.xuiservice.uvccamera.uvcapp;

import android.app.ActivityThread;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.xiaopeng.xui.app.XDialog;
import com.xiaopeng.xui.app.XDialogInterface;
import com.xiaopeng.xui.app.XToast;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.utils.UiHandler;
import com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper;
import com.xiaopeng.xuiservice.uvccamera.uvcapp.UsbCameraService;
import com.xiaopeng.xuiservice.uvccamera.uvcapp.activity.PreviewActivity;
import org.opencv.videoio.Videoio;
/* loaded from: classes5.dex */
public class UsbCameraService extends Service {
    private static final String TAG = UsbCameraService.class.getSimpleName() + "##";
    private ICameraHelper mCameraHelper;
    private final ICameraHelper.StateCallback mStateCallback = new MyCameraHelperCallback(this, null);
    private UsbDevice mUsbDevice;
    private XDialog xDialog;

    @Override // android.app.Service
    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        LogUtil.d(TAG, "onCreate");
        initCameraHelper();
        super.onCreate();
    }

    private void initCameraHelper() {
        if (this.mCameraHelper == null) {
            this.mCameraHelper = new CameraHelper();
            this.mCameraHelper.setStateCallback(this.mStateCallback);
        }
    }

    private void clearCameraHelper() {
        ICameraHelper iCameraHelper = this.mCameraHelper;
        if (iCameraHelper != null) {
            iCameraHelper.release();
            this.mCameraHelper = null;
        }
    }

    /* loaded from: classes5.dex */
    private class MyCameraHelperCallback implements ICameraHelper.StateCallback {
        private MyCameraHelperCallback() {
        }

        /* synthetic */ MyCameraHelperCallback(UsbCameraService x0, AnonymousClass1 x1) {
            this();
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper.StateCallback
        public void onAttach(UsbDevice device) {
            if (device != null) {
                UsbCameraService.this.mUsbDevice = device;
                UsbCameraService.this.popDialog();
            }
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper.StateCallback
        public void onDeviceOpen(UsbDevice device, boolean isFirstOpen) {
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper.StateCallback
        public void onCameraOpen(UsbDevice device) {
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper.StateCallback
        public void onCameraClose(UsbDevice device) {
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper.StateCallback
        public void onDeviceClose(UsbDevice device) {
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper.StateCallback
        public void onDetach(UsbDevice device) {
            if (UsbCameraService.this.xDialog != null) {
                UsbCameraService.this.xDialog.dismiss();
            }
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraHelper.StateCallback
        public void onCancel(UsbDevice device) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xiaopeng.xuiservice.uvccamera.uvcapp.UsbCameraService$1  reason: invalid class name */
    /* loaded from: classes5.dex */
    public class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            UsbCameraService usbCameraService = UsbCameraService.this;
            usbCameraService.xDialog = new XDialog(usbCameraService);
            UsbCameraService.this.xDialog.setTitle(R.string.uvc_dialog_title).setSystemDialog(2008).setMessage(UsbCameraService.this.getResources().getString(R.string.uvc_dialog_content, UsbCameraService.this.mUsbDevice.getProductName())).setPositiveButtonInterceptDismiss(true).setNegativeButtonInterceptDismiss(true).setPositiveButton(R.string.uvc_posbtn, new XDialogInterface.OnClickListener() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.UsbCameraService.1.2
                @Override // com.xiaopeng.xui.app.XDialogInterface.OnClickListener
                public void onClick(XDialog dialog, int which) {
                    Intent intent = new Intent(UsbCameraService.this, PreviewActivity.class);
                    intent.addFlags(Videoio.CAP_INTELPERC_IMAGE_GENERATOR);
                    ActivityThread.currentActivityThread().getApplication().startActivity(intent);
                    UsbCameraService.this.xDialog.dismiss();
                }
            }).setNegativeButton(R.string.uvc_negbtn, new XDialogInterface.OnClickListener() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.-$$Lambda$UsbCameraService$1$DezPsBjB1gDhUJ9ljAVhs6Ps_18
                @Override // com.xiaopeng.xui.app.XDialogInterface.OnClickListener
                public final void onClick(XDialog xDialog, int i) {
                    UsbCameraService.AnonymousClass1.this.lambda$run$0$UsbCameraService$1(xDialog, i);
                }
            }).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.UsbCameraService.1.1
                @Override // android.content.DialogInterface.OnCancelListener
                public void onCancel(DialogInterface dialog) {
                    UsbCameraService.this.xDialog.dismiss();
                    UsbCameraService.this.xDialog = null;
                }
            }).setCancelable(true);
            UsbCameraService.this.xDialog.show();
        }

        public /* synthetic */ void lambda$run$0$UsbCameraService$1(XDialog dialog, int which) {
            XToast.showShort((int) R.string.uvc_msg_negbtn);
            UsbCameraService.this.xDialog.dismiss();
            UsbCameraService.this.xDialog = null;
        }
    }

    public void popDialog() {
        UiHandler.getInstance().post(new AnonymousClass1());
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        clearCameraHelper();
    }
}
