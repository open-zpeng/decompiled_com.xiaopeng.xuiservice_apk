package com.loostone.libtuning.channel.skyworth;

import OooO00o.OooO0O0.OooO0O0.OooO00o;
import OooO00o.OooO0O0.OooO0Oo.OooO0O0.OooOO0O;
import OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO;
import OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0;
import OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0;
import OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0o;
import android.app.Instrumentation;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import com.loostone.libtuning.R;
import com.loostone.libtuning.channel.BaseChannelMgr;
import com.loostone.libtuning.channel.skyworth.util.SkyworthBlueToothMgr;
import com.loostone.libtuning.inf.extern.IBaseKeyService;
import com.loostone.libtuning.inf.extern.IKeyListener;
import com.lzy.okgo.model.Progress;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.jetbrains.annotations.NotNull;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0016\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 52\u00020\u00012\u00020\u00022\u00020\u00032\b\u0012\u0004\u0012\u00020\u00050\u00042\u00020\u00062\u00020\u0007:\u00015B\u0011\u0012\b\b\u0002\u00102\u001a\u000201¢\u0006\u0004\b3\u00104J\u000f\u0010\t\u001a\u00020\bH\u0016¢\u0006\u0004\b\t\u0010\nJ\u000f\u0010\u000b\u001a\u00020\bH\u0016¢\u0006\u0004\b\u000b\u0010\nJ\u0017\u0010\u000e\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\fH\u0016¢\u0006\u0004\b\u000e\u0010\u000fJ\u000f\u0010\u0010\u001a\u00020\bH\u0016¢\u0006\u0004\b\u0010\u0010\nJ\u0017\u0010\u0013\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u0011H\u0016¢\u0006\u0004\b\u0013\u0010\u0014J\u0017\u0010\u0015\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u0011H\u0016¢\u0006\u0004\b\u0015\u0010\u0014J\u0017\u0010\u0016\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u0011H\u0016¢\u0006\u0004\b\u0016\u0010\u0014J\u001f\u0010\u001a\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\u0005H\u0016¢\u0006\u0004\b\u001a\u0010\u001bJ\u001f\u0010\u001c\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\u0005H\u0016¢\u0006\u0004\b\u001c\u0010\u001bJ\u001f\u0010\u001d\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\u0005H\u0016¢\u0006\u0004\b\u001d\u0010\u001bJ\u001f\u0010\u001e\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\u0005H\u0016¢\u0006\u0004\b\u001e\u0010\u001bJ\u001f\u0010\u001f\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\u0005H\u0016¢\u0006\u0004\b\u001f\u0010\u001bJ\u0017\u0010!\u001a\u00020\b2\u0006\u0010 \u001a\u00020\u0005H\u0016¢\u0006\u0004\b!\u0010\"J\u000f\u0010#\u001a\u00020\bH\u0016¢\u0006\u0004\b#\u0010\nJ\u0017\u0010%\u001a\u00020\b2\u0006\u0010$\u001a\u00020\u0017H\u0016¢\u0006\u0004\b%\u0010&J\u0017\u0010(\u001a\u00020\b2\u0006\u0010'\u001a\u00020\u0005H\u0016¢\u0006\u0004\b(\u0010\"J\u0017\u0010*\u001a\u00020\b2\u0006\u0010)\u001a\u00020\u0017H\u0016¢\u0006\u0004\b*\u0010&J\u000f\u0010+\u001a\u00020\bH\u0016¢\u0006\u0004\b+\u0010\nR\u0016\u0010,\u001a\u00020\u00058\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b,\u0010-R\u0016\u0010/\u001a\u00020.8\u0002@\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b/\u00100¨\u00066"}, d2 = {"Lcom/loostone/libtuning/channel/skyworth/SkyworthBMgr;", "Lcom/loostone/libtuning/channel/BaseChannelMgr;", "LOooO00o/OooO0O0/OooO0o0/OooO0Oo/OooO0O0;", "Lcom/loostone/libtuning/inf/extern/IKeyListener;", "LOooO00o/OooO0O0/OooO0Oo/OooO0O0/OooOO0O;", "", "LOooO00o/OooO0O0/OooO0o0/OooO0Oo/OooO0o;", "Lcom/loostone/libtuning/channel/skyworth/util/SkyworthBlueToothMgr$IBTDeviceConnectedChangedListener;", "", "init", "()V", "release", "Landroid/hardware/usb/UsbDevice;", "usbDevice", "onDonglePermissionGranted", "(Landroid/hardware/usb/UsbDevice;)V", "onDonglePermissionDenied", "LOooO00o/OooO0O0/OooO0O0/OooO0Oo/OooO0O0/OooO00o;", "cachedDevice", "onDeviceAddedWithHid", "(LOooO00o/OooO0O0/OooO0O0/OooO0Oo/OooO0O0/OooO00o;)V", "onDeviceDeniedByHid", "onDeviceRemoved", "", "mic", "actionDown", "onPower", "(IZ)V", "onVoice", "onTVPhoneSwitch", "onVolumeUp", "onVolumeDown", "arg", "onSuccess", "(Z)V", "onError", "volume", "onVolumeChange", "(I)V", "on", "onScreenChange", "key", "onSkyworthKey", "onDeviceChanged", "strState", "Z", "Landroid/os/Handler;", "mHandler", "Landroid/os/Handler;", "Landroid/content/Context;", "context", "<init>", "(Landroid/content/Context;)V", "Companion", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public final class SkyworthBMgr extends BaseChannelMgr implements OooO0O0, IKeyListener, OooOO0O<Boolean>, OooO0o, SkyworthBlueToothMgr.IBTDeviceConnectedChangedListener {
    @NotNull
    public static final Companion Companion = new Companion(null);
    private static final int KEYCODE_ASSIST = 219;
    @NotNull
    private static final String TAG = "SkyworthBMgr";
    @NotNull
    private final Handler mHandler;
    private boolean strState;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\b\u0010\tR\u0016\u0010\u0003\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004R\u0016\u0010\u0006\u001a\u00020\u00058\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0006\u0010\u0007¨\u0006\n"}, d2 = {"Lcom/loostone/libtuning/channel/skyworth/SkyworthBMgr$Companion;", "", "", "KEYCODE_ASSIST", "I", "", "TAG", "Ljava/lang/String;", "<init>", "()V", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes4.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public SkyworthBMgr() {
        this(null, 1, null);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public SkyworthBMgr(android.content.Context r1, int r2, kotlin.jvm.internal.DefaultConstructorMarker r3) {
        /*
            r0 = this;
            r2 = r2 & 1
            if (r2 == 0) goto L10
            android.content.Context r1 = OooO00o.OooO0O0.OooO0O0.OooO00o.OooO0O0
            if (r1 == 0) goto L9
            goto L10
        L9:
            java.lang.String r1 = "instance"
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r1)
            r1 = 0
            throw r1
        L10:
            r0.<init>(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loostone.libtuning.channel.skyworth.SkyworthBMgr.<init>(android.content.Context, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onDeviceChanged$lambda-4  reason: not valid java name */
    public static final void m94onDeviceChanged$lambda4(SkyworthBMgr this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (SkyworthBlueToothMgr.getInstance().isConnectAudioBt() && this$0.getPowerStatus() == 3) {
            Context context = OooO00o.OooO0O0;
            if (context != null) {
                Toast.makeText(context, R.string.mic_power_on_bt, 1).show();
                SkyworthBlueToothMgr.getInstance().disconnectBtAudioDevice();
                return;
            }
            Intrinsics.throwUninitializedPropertyAccessException("instance");
            throw null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onPower$lambda-2  reason: not valid java name */
    public static final void m95onPower$lambda2() {
        Context context = OooO00o.OooO0O0;
        if (context != null) {
            Toast.makeText(context, R.string.mic_power_on_bt, 1).show();
            SkyworthBlueToothMgr.getInstance().disconnectBtAudioDevice();
            return;
        }
        Intrinsics.throwUninitializedPropertyAccessException("instance");
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onPower$lambda-3  reason: not valid java name */
    public static final void m96onPower$lambda3() {
        Context context = OooO00o.OooO0O0;
        if (context != null) {
            Toast.makeText(context, R.string.mic_power_off_bt, 1).show();
            SkyworthBlueToothMgr.getInstance().connectBtAudioDevice();
            return;
        }
        Intrinsics.throwUninitializedPropertyAccessException("instance");
        throw null;
    }

    @Override // com.loostone.libtuning.channel.BaseChannelMgr
    public void init() {
        super.init();
        getLocalDongleManager().getMEventManager().OooO00o(this);
        getKeyService().registerKeyListener(this);
        getInfoReceiver().OooO00o(this);
        SkyworthBlueToothMgr skyworthBlueToothMgr = SkyworthBlueToothMgr.getInstance();
        Context context = OooO00o.OooO0O0;
        if (context != null) {
            skyworthBlueToothMgr.init(context);
            SkyworthBlueToothMgr.getInstance().setListener(this);
            return;
        }
        Intrinsics.throwUninitializedPropertyAccessException("instance");
        throw null;
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0
    public void onDeviceAddedWithHid(@NotNull OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO00o cachedDevice) {
        Intrinsics.checkNotNullParameter(cachedDevice, "cachedDevice");
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(" ------> 设备验证通过, 打开so或者ko", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", TAG + " ->  ------> 设备验证通过, 打开so或者ko");
        }
        if (this.strState) {
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter(" ------> STR待机状态，不响应USB插入事件", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " ->  ------> STR待机状态，不响应USB插入事件");
                return;
            }
            return;
        }
        int OooO00o2 = OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO00o(cachedDevice.OooO0oO);
        Integer OooO00o3 = OooO.f279OooO00o.OooOO0O().OooO00o();
        if (OooO00o3 != null && OooO00o3.intValue() == 3) {
            OooOO0 oooOO0 = OooOO0.f398OooO00o;
            oooOO0.o000OO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<String>) "000");
            oooOO0.o0000Oo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<String>) (OooO00o2 + TarConstants.VERSION_POSIX));
            String msg = " ------> 设置capture和playback节点 c: 000 p: " + OooO00o2 + TarConstants.VERSION_POSIX;
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter(msg, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " -> " + msg);
            }
        }
        startKaraoke();
        IBaseKeyService keyService = getKeyService();
        keyService.setDevice(cachedDevice.f228OooO00o);
        keyService.start();
        OooO00o.OooO0O0.OooO0Oo.OooO0O0.OooOO0 OooO00o4 = getNetworkMgr().OooO00o();
        OooO00o4.OooO00o(cachedDevice, this);
        OooO00o4.OooO00o(cachedDevice);
    }

    @Override // com.loostone.libtuning.channel.skyworth.util.SkyworthBlueToothMgr.IBTDeviceConnectedChangedListener
    public void onDeviceChanged() {
        if (getPowerStatus() == 3) {
            this.mHandler.postDelayed(new Runnable() { // from class: com.loostone.libtuning.channel.skyworth.-$$Lambda$SkyworthBMgr$N4uPUXnYLdjXLBBZCKSNyNi66mM
                @Override // java.lang.Runnable
                public final void run() {
                    SkyworthBMgr.m94onDeviceChanged$lambda4(SkyworthBMgr.this);
                }
            }, 5000L);
        }
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0
    public void onDeviceDeniedByHid(@NotNull OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO00o cachedDevice) {
        Intrinsics.checkNotNullParameter(cachedDevice, "cachedDevice");
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0
    public void onDeviceRemoved(@NotNull OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO00o cachedDevice) {
        Intrinsics.checkNotNullParameter(cachedDevice, "cachedDevice");
        String msg = Intrinsics.stringPlus(" ------> 设备移除 ", cachedDevice.OooO0oO);
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 1) {
            Log.i("LogTuning", TAG + " -> " + msg);
        }
        stopKaraoke();
        getKeyService().stop();
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0
    public void onDonglePermissionDenied() {
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0
    public void onDonglePermissionGranted(@NotNull UsbDevice usbDevice) {
        Intrinsics.checkNotNullParameter(usbDevice, "usbDevice");
        String msg = Intrinsics.stringPlus(" ------> 权限申请成功 ", usbDevice.getProductName());
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> " + msg);
        }
        String msg2 = Intrinsics.stringPlus(" ------> 设备数量 ", Integer.valueOf(((ArrayList) getLocalDongleManager().getMCachedDongleDeviceManager().OooO00o()).size()));
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(msg2, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> " + msg2);
        }
    }

    @Override // OooO00o.OooO0O0.OooO0Oo.OooO0O0.OooOO0O
    public void onError() {
        if (getVerifyStatus() != 2) {
            stopKaraoke();
            getKeyService().stop();
        }
    }

    @Override // com.loostone.libtuning.inf.extern.IKeyListener
    public void onPower(int i, boolean z) {
        if (z) {
            String msg = Intrinsics.stringPlus(" ------> 电源按下 ", Integer.valueOf(i));
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter(msg, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " -> " + msg);
            }
        } else {
            String msg2 = Intrinsics.stringPlus(" ------> 电源弹起 ", Integer.valueOf(i));
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter(msg2, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " -> " + msg2);
            }
        }
        if (i != 0) {
            return;
        }
        if (z) {
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter(" ------> 电源按下", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " ->  ------> 电源按下");
            }
            getFullSceneMode().OooO00o(1);
            if (SkyworthBlueToothMgr.getInstance().isConnectAudioBt()) {
                this.mHandler.post(new Runnable() { // from class: com.loostone.libtuning.channel.skyworth.-$$Lambda$SkyworthBMgr$cESh7gtWYJmTG-2F8Dz1s6-EJXs
                    @Override // java.lang.Runnable
                    public final void run() {
                        SkyworthBMgr.m95onPower$lambda2();
                    }
                });
                return;
            }
            return;
        }
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(" ------> 电源弹起", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " ->  ------> 电源弹起");
        }
        getFullSceneMode().OooO00o(0);
        if ((true ^ SkyworthBlueToothMgr.getInstance().isAddressNull()) && (!SkyworthBlueToothMgr.getInstance().isConnectAudioBt())) {
            this.mHandler.post(new Runnable() { // from class: com.loostone.libtuning.channel.skyworth.-$$Lambda$SkyworthBMgr$IapWMM_lO4211PT6Qldy8AtpBg4
                @Override // java.lang.Runnable
                public final void run() {
                    SkyworthBMgr.m96onPower$lambda3();
                }
            });
        }
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0o
    public void onScreenChange(boolean z) {
        this.strState = !z;
        if (z) {
            getFullSceneMode().OooO0O0();
        } else {
            getFullSceneMode().OooO0OO();
        }
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(" ------> 亮屏息屏", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " ->  ------> 亮屏息屏");
        }
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0o
    public void onSkyworthKey(int i) {
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(" ------> 按键监听", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " ->  ------> 按键监听");
        }
    }

    @Override // OooO00o.OooO0O0.OooO0Oo.OooO0O0.OooOO0O
    public /* bridge */ /* synthetic */ void onSuccess(Boolean bool) {
        onSuccess(bool.booleanValue());
    }

    public void onSuccess(boolean z) {
    }

    @Override // com.loostone.libtuning.inf.extern.IKeyListener
    public void onTVPhoneSwitch(int i, boolean z) {
        if (i != 0) {
            return;
        }
        if (z) {
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter(" ------> TV按下", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " ->  ------> TV按下");
                return;
            }
            return;
        }
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(" ------> TV弹起", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " ->  ------> TV弹起");
        }
    }

    @Override // com.loostone.libtuning.inf.extern.IKeyListener
    public void onVoice(int i, boolean z) {
        if (i == 0 && !this.strState) {
            if (z) {
                getVolumeAdjustment().setMicVolumeEnable(false);
                new Instrumentation().sendKeyDownUpSync(KEYCODE_ASSIST);
                Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
                Intrinsics.checkNotNullParameter(" ------> 语音按下", NotificationCompat.CATEGORY_MESSAGE);
                if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                    Log.i("LogTuning", TAG + " ->  ------> 语音按下");
                    return;
                }
                return;
            }
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter(" ------> 语音弹起", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " ->  ------> 语音弹起");
            }
            getVolumeAdjustment().setMicVolumeEnable(true);
        }
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0o
    public void onVolumeChange(int i) {
        String msg = Intrinsics.stringPlus(" ------> 设置音量 ", Integer.valueOf(i));
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> " + msg);
        }
    }

    @Override // com.loostone.libtuning.inf.extern.IKeyListener
    public void onVolumeDown(int i, boolean z) {
        if (i != 0) {
            return;
        }
        if (z) {
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter(" ------> 音量减按下", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " ->  ------> 音量减按下");
            }
            getVolumeAdjustment().volumeDown(1);
            return;
        }
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(" ------> 音量减弹起", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " ->  ------> 音量减弹起");
        }
    }

    @Override // com.loostone.libtuning.inf.extern.IKeyListener
    public void onVolumeUp(int i, boolean z) {
        if (i != 0) {
            return;
        }
        if (z) {
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter(" ------> 音量加按下", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " ->  ------> 音量加按下");
            }
            getVolumeAdjustment().volumeUp(1);
            return;
        }
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(" ------> 音量加弹起", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " ->  ------> 音量加弹起");
        }
    }

    @Override // com.loostone.libtuning.channel.BaseChannelMgr
    public void release() {
        super.release();
        getLocalDongleManager().getMEventManager().OooO0O0(this);
        getKeyService().unRegisterKeyListener(this);
        getInfoReceiver().OooO0O0(this);
        SkyworthBlueToothMgr.getInstance().deinit();
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SkyworthBMgr(@NotNull Context context) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
        this.mHandler = new Handler(Looper.getMainLooper());
    }
}
