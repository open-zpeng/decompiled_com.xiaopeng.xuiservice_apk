package com.loostone.libtuning.channel;

import OooO00o.OooO0O0.OooO0O0.OooO00o;
import OooO00o.OooO0O0.OooO0Oo.OooO0O0.OooOO0O;
import OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO;
import OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0;
import OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0;
import OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0OO;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.loostone.libtuning.inf.extern.IBaseKeyService;
import com.loostone.libtuning.inf.extern.IKeyListener;
import com.loostone.libtuning.inf.extern.IMicControl;
import com.lzy.okgo.model.Progress;
import com.xiaopeng.speech.jarvisproto.DMEnd;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.jetbrains.annotations.NotNull;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 22\u00020\u00012\u00020\u00022\u00020\u00032\b\u0012\u0004\u0012\u00020\u00050\u0004:\u00012B\u0011\u0012\b\b\u0002\u0010/\u001a\u00020.¢\u0006\u0004\b0\u00101J\u001f\u0010\n\u001a\u00020\t2\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0005H\u0002¢\u0006\u0004\b\n\u0010\u000bJ\u000f\u0010\f\u001a\u00020\tH\u0016¢\u0006\u0004\b\f\u0010\rJ\u000f\u0010\u000e\u001a\u00020\tH\u0016¢\u0006\u0004\b\u000e\u0010\rJ\u000f\u0010\u000f\u001a\u00020\tH\u0016¢\u0006\u0004\b\u000f\u0010\rJ\u0017\u0010\u0012\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\u0010H\u0016¢\u0006\u0004\b\u0012\u0010\u0013J\u000f\u0010\u0014\u001a\u00020\tH\u0016¢\u0006\u0004\b\u0014\u0010\rJ\u0017\u0010\u0017\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\u0015H\u0016¢\u0006\u0004\b\u0017\u0010\u0018J\u0017\u0010\u0019\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\u0015H\u0016¢\u0006\u0004\b\u0019\u0010\u0018J\u0017\u0010\u001a\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\u0015H\u0016¢\u0006\u0004\b\u001a\u0010\u0018J\r\u0010\u001b\u001a\u00020\t¢\u0006\u0004\b\u001b\u0010\rJ\u001f\u0010\u001e\u001a\u00020\t2\u0006\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\b\u001a\u00020\u0005H\u0016¢\u0006\u0004\b\u001e\u0010\u001fJ\u001f\u0010 \u001a\u00020\t2\u0006\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\b\u001a\u00020\u0005H\u0016¢\u0006\u0004\b \u0010\u001fJ\u001f\u0010!\u001a\u00020\t2\u0006\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\b\u001a\u00020\u0005H\u0016¢\u0006\u0004\b!\u0010\u001fJ\u001f\u0010\"\u001a\u00020\t2\u0006\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\b\u001a\u00020\u0005H\u0016¢\u0006\u0004\b\"\u0010\u001fJ\u001f\u0010#\u001a\u00020\t2\u0006\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\b\u001a\u00020\u0005H\u0016¢\u0006\u0004\b#\u0010\u001fJ\u0017\u0010%\u001a\u00020\t2\u0006\u0010$\u001a\u00020\u0005H\u0016¢\u0006\u0004\b%\u0010&J\u000f\u0010'\u001a\u00020\tH\u0016¢\u0006\u0004\b'\u0010\rR\"\u0010(\u001a\u00020\u001c8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b(\u0010)\u001a\u0004\b*\u0010+\"\u0004\b,\u0010-¨\u00063"}, d2 = {"Lcom/loostone/libtuning/channel/FullSceneSpeakMgr;", "Lcom/loostone/libtuning/channel/BaseChannelMgr;", "LOooO00o/OooO0O0/OooO0o0/OooO0Oo/OooO0O0;", "Lcom/loostone/libtuning/inf/extern/IKeyListener;", "LOooO00o/OooO0O0/OooO0Oo/OooO0O0/OooOO0O;", "", "", SpeechConstants.KEY_COMMAND_TYPE, "actionDown", "", "sendMicKey", "(Ljava/lang/String;Z)V", "init", "()V", "release", "startKaraoke", "Landroid/hardware/usb/UsbDevice;", "usbDevice", "onDonglePermissionGranted", "(Landroid/hardware/usb/UsbDevice;)V", "onDonglePermissionDenied", "LOooO00o/OooO0O0/OooO0O0/OooO0Oo/OooO0O0/OooO00o;", "cachedDevice", "onDeviceAddedWithHid", "(LOooO00o/OooO0O0/OooO0O0/OooO0Oo/OooO0O0/OooO00o;)V", "onDeviceDeniedByHid", "onDeviceRemoved", "dynamicAdjustCompPregain", "", "mic", "onPower", "(IZ)V", "onVoice", "onTVPhoneSwitch", "onVolumeUp", "onVolumeDown", "arg", "onSuccess", "(Z)V", "onError", "karaokeMode", "I", "getKaraokeMode", "()I", "setKaraokeMode", "(I)V", "Landroid/content/Context;", "context", "<init>", "(Landroid/content/Context;)V", "Companion", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public final class FullSceneSpeakMgr extends BaseChannelMgr implements OooO0O0, IKeyListener, OooOO0O<Boolean> {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private static final String TAG = "FullSceneSpeakMgr";
    private int karaokeMode;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0005\u0010\u0006R\u0016\u0010\u0003\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004¨\u0006\u0007"}, d2 = {"Lcom/loostone/libtuning/channel/FullSceneSpeakMgr$Companion;", "", "", "TAG", "Ljava/lang/String;", "<init>", "()V", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes4.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public FullSceneSpeakMgr() {
        this(null, 1, null);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public FullSceneSpeakMgr(android.content.Context r1, int r2, kotlin.jvm.internal.DefaultConstructorMarker r3) {
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
        throw new UnsupportedOperationException("Method not decompiled: com.loostone.libtuning.channel.FullSceneSpeakMgr.<init>(android.content.Context, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    private final void sendMicKey(String str, boolean z) {
        Intent intent = new Intent("com.loostone.puremic.dongle_key_changed");
        intent.putExtra("keycode_type", str);
        intent.putExtra("keycode_action", z ? 1 : 0);
        Context context = OooO00o.OooO0O0;
        if (context != null) {
            context.sendBroadcast(intent);
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("instance");
            throw null;
        }
    }

    public final void dynamicAdjustCompPregain() {
        Integer OooO00o2;
        OooO00o.OooO0O0.OooO0o0.OooO0O0.OooO0OO.OooO0O0 oooO0O0 = (OooO00o.OooO0O0.OooO0o0.OooO0O0.OooO0OO.OooO0O0) getMicControlService();
        if (oooO0O0 != null) {
            byte[] OooO0O0 = oooO0O0.OooO0O0();
            if (OooO0O0.length < 2) {
                return;
            }
            String msg = "micKey A = " + ((int) OooO0O0[0]) + ", B = " + ((int) OooO0O0[1]);
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter(msg, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " -> " + msg);
            }
            if (((byte) (OooO0O0[1] & OooO0O0[0])) == 64) {
                OooOO0 oooOO0 = OooOO0.f398OooO00o;
                OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer> OooO0o0 = oooOO0.OooO0o0();
                Intrinsics.checkNotNull(oooOO0.OooO0o().OooO00o());
                OooO0o0.OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf((OooO00o2.intValue() - 4) + getVolumeAdjustment().getCarOutPregainAdd()));
                return;
            }
            OooOO0 oooOO02 = OooOO0.f398OooO00o;
            OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer> OooO0o02 = oooOO02.OooO0o0();
            Integer OooO00o3 = oooOO02.OooO0o().OooO00o();
            Intrinsics.checkNotNull(OooO00o3);
            OooO0o02.OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(OooO00o3.intValue() + getVolumeAdjustment().getCarOutPregainAdd()));
        }
    }

    public final int getKaraokeMode() {
        return this.karaokeMode;
    }

    @Override // com.loostone.libtuning.channel.BaseChannelMgr
    public void init() {
        super.init();
        getLocalDongleManager().getMEventManager().OooO00o(this);
        getKeyService().registerKeyListener(this);
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0
    public void onDeviceAddedWithHid(@NotNull OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO00o cachedDevice) {
        Intrinsics.checkNotNullParameter(cachedDevice, "cachedDevice");
        OooO00o.OooO0O0.OooO0O0.OooO0O0 oooO0O0 = OooO00o.OooO0O0.OooO0O0.OooO0O0.f225OooO00o;
        oooO0O0.OooO00o(TAG, "  设备验证通过, 打开so或者ko");
        oooO0O0.OooO0OO(TAG, "  SDK VERSION 3.1.10 2023-05-30 17:42:06, CommitID:b21bd2ae");
        Integer OooO00o2 = OooO.f279OooO00o.OooOO0O().OooO00o();
        if ((OooO00o2 == null ? 1 : OooO00o2.intValue()) == 1) {
            int OooO00o3 = OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO00o(cachedDevice.OooO0oO);
            OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<String> o000OO = OooOO0.f398OooO00o.o000OO();
            o000OO.OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<String>) (OooO00o3 + TarConstants.VERSION_POSIX));
            oooO0O0.OooO0OO(TAG, " 设置capture card: " + OooO00o3 + " device: 00");
        }
        startKaraoke();
        IBaseKeyService keyService = getKeyService();
        keyService.setDevice(cachedDevice.f228OooO00o);
        keyService.start();
        dynamicAdjustCompPregain();
        OooO00o.OooO0O0.OooO0Oo.OooO0O0.OooOO0 OooO00o4 = getNetworkMgr().OooO00o();
        OooO00o4.OooO00o(cachedDevice, this);
        OooO00o4.OooO00o(cachedDevice);
        IMicControl micControl = getKeyService().getMicControl();
        if (micControl != null) {
            micControl.setMicShiftFrequency(0);
        }
        getVolumeAdjustment().setAef3ReverbFsEnable(1);
        oooO0O0.OooO00o(TAG, "关闭接收器移频，打开软移频");
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0
    public void onDeviceDeniedByHid(@NotNull OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO00o cachedDevice) {
        Intrinsics.checkNotNullParameter(cachedDevice, "cachedDevice");
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0
    public void onDeviceRemoved(@NotNull OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO00o cachedDevice) {
        Intrinsics.checkNotNullParameter(cachedDevice, "cachedDevice");
        String msg = Intrinsics.stringPlus("  设备移除 ", cachedDevice.OooO0oO);
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
        String msg = Intrinsics.stringPlus("  权限申请成功 ", usbDevice.getProductName());
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> " + msg);
        }
        String msg2 = Intrinsics.stringPlus("  设备数量 ", Integer.valueOf(((ArrayList) getLocalDongleManager().getMCachedDongleDeviceManager().OooO00o()).size()));
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
        dynamicAdjustCompPregain();
        if (i != 0) {
            return;
        }
        if (z) {
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("  电源按下", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " ->   电源按下");
            }
            OooO0OO micPowerListener = getMicPowerListener();
            if (micPowerListener != null) {
                micPowerListener.OooO00o(1);
            }
            getFullSceneMode().OooO00o(1);
        } else {
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("  电源弹起", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " ->   电源弹起");
            }
            OooO0OO micPowerListener2 = getMicPowerListener();
            if (micPowerListener2 != null) {
                micPowerListener2.OooO00o(0);
            }
            getFullSceneMode().OooO00o(0);
        }
        sendMicKey("power", z);
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
            Intrinsics.checkNotNullParameter("  TV按下", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " ->   TV按下");
                return;
            }
            return;
        }
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("  TV弹起", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " ->   TV弹起");
        }
    }

    @Override // com.loostone.libtuning.inf.extern.IKeyListener
    public void onVoice(int i, boolean z) {
        if (i != 0) {
            return;
        }
        if (z) {
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("  语音按下", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " ->   语音按下");
            }
        } else {
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("  语音弹起", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " ->   语音弹起");
            }
        }
        sendMicKey(DMEnd.REASON_VOICE, z);
    }

    @Override // com.loostone.libtuning.inf.extern.IKeyListener
    public void onVolumeDown(int i, boolean z) {
        if (i != 0) {
            return;
        }
        if (z) {
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("  音量减按下", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " ->   音量减按下");
            }
            getVolumeAdjustment().volumeDown(1);
            return;
        }
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("  音量减弹起", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " ->   音量减弹起");
        }
    }

    @Override // com.loostone.libtuning.inf.extern.IKeyListener
    public void onVolumeUp(int i, boolean z) {
        if (i != 0) {
            return;
        }
        if (z) {
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("  音量加按下", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " ->   音量加按下");
            }
            getVolumeAdjustment().volumeUp(1);
            return;
        }
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("  音量加弹起", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " ->   音量加弹起");
        }
    }

    @Override // com.loostone.libtuning.channel.BaseChannelMgr
    public void release() {
        super.release();
        getLocalDongleManager().getMEventManager().OooO0O0(this);
        getKeyService().unRegisterKeyListener(this);
        try {
            getKeyService().stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void setKaraokeMode(int i) {
        this.karaokeMode = i;
    }

    @Override // com.loostone.libtuning.channel.BaseChannelMgr
    public void startKaraoke() {
        if (this.karaokeMode == 1) {
            super.startKaraoke();
            return;
        }
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("startKaraoke fail: not in karaoke mode", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> startKaraoke fail: not in karaoke mode");
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FullSceneSpeakMgr(@NotNull Context context) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
    }
}
