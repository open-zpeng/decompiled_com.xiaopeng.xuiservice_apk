package com.loostone.libtuning.channel.skyworth;

import OooO00o.OooO0O0.OooO0O0.OooO00o;
import OooO00o.OooO0O0.OooO0Oo.OooO0O0.OooOO0O;
import OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO;
import OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0;
import OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0;
import OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0o;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import com.loostone.libtuning.R;
import com.loostone.libtuning.channel.BaseChannelMgr;
import com.loostone.libtuning.channel.skyworth.inf.ISkySystemProperty;
import com.loostone.libtuning.channel.skyworth.old.ai.AiControlFactory;
import com.loostone.libtuning.channel.skyworth.old.record.RecordMgr;
import com.loostone.libtuning.channel.skyworth.util.SkySystemPropertyService;
import com.loostone.libtuning.channel.skyworth.util.SkyworthAIControl;
import com.loostone.libtuning.channel.skyworth.util.SkyworthBlueToothMgr;
import com.loostone.libtuning.inf.extern.IBaseKeyService;
import com.loostone.libtuning.inf.extern.IKeyListener;
import com.lzy.okgo.model.Progress;
import java.util.ArrayList;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.jetbrains.annotations.NotNull;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0016\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 A2\u00020\u00012\u00020\u00022\u00020\u00032\b\u0012\u0004\u0012\u00020\u00050\u00042\u00020\u00062\u00020\u00072\u00020\b:\u0001AB\u0011\u0012\b\b\u0002\u0010>\u001a\u00020=¢\u0006\u0004\b?\u0010@J\u000f\u0010\n\u001a\u00020\tH\u0016¢\u0006\u0004\b\n\u0010\u000bJ\u000f\u0010\f\u001a\u00020\tH\u0016¢\u0006\u0004\b\f\u0010\u000bJ\u0017\u0010\u000f\u001a\u00020\t2\u0006\u0010\u000e\u001a\u00020\rH\u0016¢\u0006\u0004\b\u000f\u0010\u0010J\u000f\u0010\u0011\u001a\u00020\tH\u0016¢\u0006\u0004\b\u0011\u0010\u000bJ\u0017\u0010\u0014\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\u0012H\u0016¢\u0006\u0004\b\u0014\u0010\u0015J\u0017\u0010\u0016\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\u0012H\u0016¢\u0006\u0004\b\u0016\u0010\u0015J\u0017\u0010\u0017\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\u0012H\u0016¢\u0006\u0004\b\u0017\u0010\u0015J\u001f\u0010\u001b\u001a\u00020\t2\u0006\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u001a\u001a\u00020\u0005H\u0016¢\u0006\u0004\b\u001b\u0010\u001cJ\u001f\u0010\u001d\u001a\u00020\t2\u0006\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u001a\u001a\u00020\u0005H\u0016¢\u0006\u0004\b\u001d\u0010\u001cJ\u001f\u0010\u001e\u001a\u00020\t2\u0006\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u001a\u001a\u00020\u0005H\u0016¢\u0006\u0004\b\u001e\u0010\u001cJ\u001f\u0010\u001f\u001a\u00020\t2\u0006\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u001a\u001a\u00020\u0005H\u0016¢\u0006\u0004\b\u001f\u0010\u001cJ\u001f\u0010 \u001a\u00020\t2\u0006\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u001a\u001a\u00020\u0005H\u0016¢\u0006\u0004\b \u0010\u001cJ\u0017\u0010\"\u001a\u00020\t2\u0006\u0010!\u001a\u00020\u0005H\u0016¢\u0006\u0004\b\"\u0010#J\u000f\u0010$\u001a\u00020\tH\u0016¢\u0006\u0004\b$\u0010\u000bJ\u0017\u0010&\u001a\u00020\t2\u0006\u0010%\u001a\u00020\u0018H\u0016¢\u0006\u0004\b&\u0010'J\u0017\u0010)\u001a\u00020\t2\u0006\u0010(\u001a\u00020\u0005H\u0016¢\u0006\u0004\b)\u0010#J\u0017\u0010+\u001a\u00020\t2\u0006\u0010*\u001a\u00020\u0018H\u0016¢\u0006\u0004\b+\u0010'J\u0017\u0010-\u001a\u00020\t2\u0006\u0010,\u001a\u00020\u0005H\u0016¢\u0006\u0004\b-\u0010#J\u000f\u0010.\u001a\u00020\tH\u0016¢\u0006\u0004\b.\u0010\u000bR\u001d\u00104\u001a\u00020/8B@\u0002X\u0082\u0084\u0002¢\u0006\f\n\u0004\b0\u00101\u001a\u0004\b2\u00103R\u0016\u00106\u001a\u0002058\u0002@\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b6\u00107R\u001d\u0010<\u001a\u0002088B@\u0002X\u0082\u0084\u0002¢\u0006\f\n\u0004\b9\u00101\u001a\u0004\b:\u0010;¨\u0006B"}, d2 = {"Lcom/loostone/libtuning/channel/skyworth/SkyworthAMgr;", "Lcom/loostone/libtuning/channel/BaseChannelMgr;", "LOooO00o/OooO0O0/OooO0o0/OooO0Oo/OooO0O0;", "Lcom/loostone/libtuning/inf/extern/IKeyListener;", "LOooO00o/OooO0O0/OooO0Oo/OooO0O0/OooOO0O;", "", "LOooO00o/OooO0O0/OooO0o0/OooO0Oo/OooO0o;", "Lcom/loostone/libtuning/channel/skyworth/inf/ISkySystemProperty;", "Lcom/loostone/libtuning/channel/skyworth/util/SkyworthBlueToothMgr$IBTDeviceConnectedChangedListener;", "", "init", "()V", "release", "Landroid/hardware/usb/UsbDevice;", "usbDevice", "onDonglePermissionGranted", "(Landroid/hardware/usb/UsbDevice;)V", "onDonglePermissionDenied", "LOooO00o/OooO0O0/OooO0O0/OooO0Oo/OooO0O0/OooO00o;", "cachedDevice", "onDeviceAddedWithHid", "(LOooO00o/OooO0O0/OooO0O0/OooO0Oo/OooO0O0/OooO00o;)V", "onDeviceDeniedByHid", "onDeviceRemoved", "", "mic", "actionDown", "onPower", "(IZ)V", "onVoice", "onTVPhoneSwitch", "onVolumeUp", "onVolumeDown", "arg", "onSuccess", "(Z)V", "onError", "volume", "onVolumeChange", "(I)V", "on", "onScreenChange", "key", "onSkyworthKey", "mute", "onDormancy", "onDeviceChanged", "Lcom/loostone/libtuning/channel/skyworth/util/SkySystemPropertyService;", "systemProperty$delegate", "Lkotlin/Lazy;", "getSystemProperty", "()Lcom/loostone/libtuning/channel/skyworth/util/SkySystemPropertyService;", "systemProperty", "Landroid/os/Handler;", "mHandler", "Landroid/os/Handler;", "Lcom/loostone/libtuning/channel/skyworth/util/SkyworthAIControl;", "skyworthAIControl$delegate", "getSkyworthAIControl", "()Lcom/loostone/libtuning/channel/skyworth/util/SkyworthAIControl;", "skyworthAIControl", "Landroid/content/Context;", "context", "<init>", "(Landroid/content/Context;)V", "Companion", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public final class SkyworthAMgr extends BaseChannelMgr implements OooO0O0, IKeyListener, OooOO0O<Boolean>, OooO0o, ISkySystemProperty, SkyworthBlueToothMgr.IBTDeviceConnectedChangedListener {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private static final String DONGLE_CARD = "third.pmkara.mic.card";
    @NotNull
    private static final String DONGLE_PRODUCT_NAME = "third.pmkara.mic.name";
    @NotNull
    private static final String MIC_STATUS = "third.pmkara.mic.status";
    @NotNull
    private static final String TAG = "SkyworthAMgr";
    @NotNull
    private final Handler mHandler;
    @NotNull
    private final Lazy skyworthAIControl$delegate;
    @NotNull
    private final Lazy systemProperty$delegate;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\b\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\b\u0010\tR\u0016\u0010\u0003\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004R\u0016\u0010\u0005\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0005\u0010\u0004R\u0016\u0010\u0006\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0006\u0010\u0004R\u0016\u0010\u0007\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0007\u0010\u0004¨\u0006\n"}, d2 = {"Lcom/loostone/libtuning/channel/skyworth/SkyworthAMgr$Companion;", "", "", "DONGLE_CARD", "Ljava/lang/String;", "DONGLE_PRODUCT_NAME", "MIC_STATUS", "TAG", "<init>", "()V", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes4.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public SkyworthAMgr() {
        this(null, 1, null);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public SkyworthAMgr(android.content.Context r1, int r2, kotlin.jvm.internal.DefaultConstructorMarker r3) {
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
        throw new UnsupportedOperationException("Method not decompiled: com.loostone.libtuning.channel.skyworth.SkyworthAMgr.<init>(android.content.Context, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    private final SkyworthAIControl getSkyworthAIControl() {
        return (SkyworthAIControl) this.skyworthAIControl$delegate.getValue();
    }

    private final SkySystemPropertyService getSystemProperty() {
        return (SkySystemPropertyService) this.systemProperty$delegate.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onDeviceChanged$lambda-4  reason: not valid java name */
    public static final void m90onDeviceChanged$lambda4(SkyworthAMgr this$0) {
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
    public static final void m91onPower$lambda2() {
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
    public static final void m92onPower$lambda3() {
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
        getSystemProperty().registerCallback(this);
        SkyworthBlueToothMgr skyworthBlueToothMgr = SkyworthBlueToothMgr.getInstance();
        Context context = OooO00o.OooO0O0;
        if (context != null) {
            skyworthBlueToothMgr.init(context);
            SkyworthBlueToothMgr.getInstance().setListener(this);
            Context context2 = OooO00o.OooO0O0;
            if (context2 != null) {
                AiControlFactory.createAiControl(context2, 0);
                getSystemProperty().start();
                return;
            }
            Intrinsics.throwUninitializedPropertyAccessException("instance");
            throw null;
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
        int OooO00o2 = OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO00o(cachedDevice.OooO0oO);
        if (OooO00o2 > 0) {
            try {
                Class.forName("android.os.SystemProperties").getMethod("set", String.class, String.class).invoke(null, DONGLE_CARD, 'c' + OooO00o2 + "d0");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Class.forName("android.os.SystemProperties").getMethod("set", String.class, String.class).invoke(null, DONGLE_PRODUCT_NAME, cachedDevice.OooO0oO);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else {
            try {
                Class.forName("android.os.SystemProperties").getMethod("set", String.class, String.class).invoke(null, DONGLE_CARD, "null");
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            try {
                Class.forName("android.os.SystemProperties").getMethod("set", String.class, String.class).invoke(null, DONGLE_PRODUCT_NAME, "null");
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        }
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
        getSkyworthAIControl().micChanged(1);
    }

    @Override // com.loostone.libtuning.channel.skyworth.util.SkyworthBlueToothMgr.IBTDeviceConnectedChangedListener
    public void onDeviceChanged() {
        if (getPowerStatus() == 3) {
            this.mHandler.postDelayed(new Runnable() { // from class: com.loostone.libtuning.channel.skyworth.-$$Lambda$SkyworthAMgr$RZZlNJfjGBpQzBFidjtN5SB5TtY
                @Override // java.lang.Runnable
                public final void run() {
                    SkyworthAMgr.m90onDeviceChanged$lambda4(SkyworthAMgr.this);
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
        try {
            Class.forName("android.os.SystemProperties").getMethod("set", String.class, String.class).invoke(null, DONGLE_CARD, "null");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class.forName("android.os.SystemProperties").getMethod("set", String.class, String.class).invoke(null, DONGLE_PRODUCT_NAME, "null");
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        stopKaraoke();
        getKeyService().stop();
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0
    public void onDonglePermissionDenied() {
        getSkyworthAIControl().micChanged(0);
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

    @Override // com.loostone.libtuning.channel.skyworth.inf.ISkySystemProperty
    public void onDormancy(boolean z) {
        String msg = Intrinsics.stringPlus("休眠静音监听 ------> ", Boolean.valueOf(z));
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> " + msg);
        }
        getVolumeAdjustment().setMicVolumeEnable(!z);
    }

    @Override // OooO00o.OooO0O0.OooO0Oo.OooO0O0.OooOO0O
    public void onError() {
        if (getVerifyStatus() != 2) {
            getSkyworthAIControl().micChanged(0);
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
            try {
                Class.forName("android.os.SystemProperties").getMethod("set", String.class, String.class).invoke(null, MIC_STATUS, "1");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (SkyworthBlueToothMgr.getInstance().isConnectAudioBt()) {
                this.mHandler.post(new Runnable() { // from class: com.loostone.libtuning.channel.skyworth.-$$Lambda$SkyworthAMgr$ONV66CxX-RA5huYw3MpoLn8BODo
                    @Override // java.lang.Runnable
                    public final void run() {
                        SkyworthAMgr.m91onPower$lambda2();
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
        try {
            Class.forName("android.os.SystemProperties").getMethod("set", String.class, String.class).invoke(null, MIC_STATUS, "0");
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        if ((!SkyworthBlueToothMgr.getInstance().isConnectAudioBt()) && (!SkyworthBlueToothMgr.getInstance().isAddressNull())) {
            this.mHandler.post(new Runnable() { // from class: com.loostone.libtuning.channel.skyworth.-$$Lambda$SkyworthAMgr$200DZLtBu_uA86drM1fW2MI41Hc
                @Override // java.lang.Runnable
                public final void run() {
                    SkyworthAMgr.m92onPower$lambda3();
                }
            });
        }
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0o
    public void onScreenChange(boolean z) {
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
        if (i != 0) {
            return;
        }
        if (z) {
            RecordMgr.getInstance().create();
            getVolumeAdjustment().setMicVolumeEnable(false);
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter(" ------> 语音按下", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " ->  ------> 语音按下");
            }
        } else {
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter(" ------> 语音弹起", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " ->  ------> 语音弹起");
            }
            getVolumeAdjustment().setMicVolumeEnable(true);
        }
        if (AiControlFactory.getAiControl() != null) {
            AiControlFactory.getAiControl().onVoiceKey(z ? 1 : 0);
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
        getSystemProperty().unRegisterCallback(this);
        SkyworthBlueToothMgr.getInstance().deinit();
        getSystemProperty().stop();
    }

    public void onSuccess(boolean z) {
        getSkyworthAIControl().micChanged(1);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SkyworthAMgr(@NotNull Context context) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
        this.skyworthAIControl$delegate = LazyKt.lazy(new Function0<SkyworthAIControl>() { // from class: com.loostone.libtuning.channel.skyworth.SkyworthAMgr$skyworthAIControl$2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // kotlin.jvm.functions.Function0
            @NotNull
            public final SkyworthAIControl invoke() {
                return new SkyworthAIControl(null, 1, null);
            }
        });
        this.systemProperty$delegate = LazyKt.lazy(new Function0<SkySystemPropertyService>() { // from class: com.loostone.libtuning.channel.skyworth.SkyworthAMgr$systemProperty$2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // kotlin.jvm.functions.Function0
            @NotNull
            public final SkySystemPropertyService invoke() {
                return new SkySystemPropertyService();
            }
        });
        this.mHandler = new Handler(Looper.getMainLooper());
    }
}
