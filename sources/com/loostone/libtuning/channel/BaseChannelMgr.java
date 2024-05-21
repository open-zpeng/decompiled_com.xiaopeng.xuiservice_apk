package com.loostone.libtuning.channel;

import OooO00o.OooO0O0.OooO0O0.OooO0O0;
import OooO00o.OooO0O0.OooO0o0.OooO00o.OooO00o.OooO00o;
import OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO;
import OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0OO;
import OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.z1.OooO0o;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.media.AudioManager;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.loostone.libtuning.inf.extern.IBaseKeyService;
import com.loostone.libtuning.inf.extern.IChannelInfo;
import com.loostone.libtuning.inf.extern.IKeyListener;
import com.loostone.libtuning.inf.extern.IMicControl;
import com.loostone.libtuning.inf.extern.IVolumeAdjustment;
import com.loostone.libtuning.process.dongle.LocalDongleManager;
import com.lzy.okgo.model.Progress;
import com.xiaopeng.xuiservice.uvccamera.usb.USBMonitor;
import java.util.HashMap;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u007f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\b*\u0001?\b&\u0018\u0000 R2\u00020\u0001:\u0001RB\u0011\u0012\b\b\u0002\u0010,\u001a\u00020+¢\u0006\u0004\bP\u0010QJ\u000f\u0010\u0003\u001a\u00020\u0002H\u0002¢\u0006\u0004\b\u0003\u0010\u0004J\u000f\u0010\u0005\u001a\u00020\u0002H\u0002¢\u0006\u0004\b\u0005\u0010\u0004J\u0017\u0010\b\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0006H\u0002¢\u0006\u0004\b\b\u0010\tJ\u000f\u0010\n\u001a\u00020\u0002H\u0016¢\u0006\u0004\b\n\u0010\u0004J\u000f\u0010\u000b\u001a\u00020\u0002H\u0016¢\u0006\u0004\b\u000b\u0010\u0004J\u000f\u0010\f\u001a\u00020\u0002H\u0016¢\u0006\u0004\b\f\u0010\u0004J\u000f\u0010\r\u001a\u00020\u0002H\u0016¢\u0006\u0004\b\r\u0010\u0004J\u000f\u0010\u000e\u001a\u00020\u0002H\u0016¢\u0006\u0004\b\u000e\u0010\u0004J\u000f\u0010\u000f\u001a\u00020\u0006H\u0016¢\u0006\u0004\b\u000f\u0010\u0010J\u000f\u0010\u0011\u001a\u00020\u0006H\u0016¢\u0006\u0004\b\u0011\u0010\u0010J\u000f\u0010\u0013\u001a\u00020\u0012H\u0016¢\u0006\u0004\b\u0013\u0010\u0014J\u0011\u0010\u0016\u001a\u0004\u0018\u00010\u0015H\u0016¢\u0006\u0004\b\u0016\u0010\u0017J\u000f\u0010\u0019\u001a\u00020\u0018H\u0016¢\u0006\u0004\b\u0019\u0010\u001aR\u001d\u0010 \u001a\u00020\u001b8F@\u0006X\u0086\u0084\u0002¢\u0006\f\n\u0004\b\u001c\u0010\u001d\u001a\u0004\b\u001e\u0010\u001fR\u001d\u0010%\u001a\u00020!8B@\u0002X\u0082\u0084\u0002¢\u0006\f\n\u0004\b\"\u0010\u001d\u001a\u0004\b#\u0010$R\u0016\u0010&\u001a\u00020\u00068\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b&\u0010'R\u0016\u0010)\u001a\u00020(8\u0002@\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b)\u0010*R\u0016\u0010,\u001a\u00020+8\u0002@\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b,\u0010-R\u001d\u00102\u001a\u00020.8D@\u0004X\u0084\u0084\u0002¢\u0006\f\n\u0004\b/\u0010\u001d\u001a\u0004\b0\u00101R\u001d\u00107\u001a\u0002038D@\u0004X\u0084\u0084\u0002¢\u0006\f\n\u0004\b4\u0010\u001d\u001a\u0004\b5\u00106R$\u00109\u001a\u0004\u0018\u0001088\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b9\u0010:\u001a\u0004\b;\u0010<\"\u0004\b=\u0010>R\u0016\u0010@\u001a\u00020?8\u0002@\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b@\u0010AR\u001d\u0010F\u001a\u00020B8F@\u0006X\u0086\u0084\u0002¢\u0006\f\n\u0004\bC\u0010\u001d\u001a\u0004\bD\u0010ER\u0016\u0010G\u001a\u00020\u00068\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bG\u0010'R\u001d\u0010J\u001a\u00020\u00128D@\u0004X\u0084\u0084\u0002¢\u0006\f\n\u0004\bH\u0010\u001d\u001a\u0004\bI\u0010\u0014R\u001d\u0010O\u001a\u00020K8D@\u0004X\u0084\u0084\u0002¢\u0006\f\n\u0004\bL\u0010\u001d\u001a\u0004\bM\u0010N¨\u0006S"}, d2 = {"Lcom/loostone/libtuning/channel/BaseChannelMgr;", "Lcom/loostone/libtuning/inf/extern/IChannelInfo;", "", "registerReceiver", "()V", "unRegisterReceiver", "", "status", "sendDongleInfo", "(I)V", "init", "startKaraoke", "stopKaraoke", "resetKaraoke", "release", "getPowerStatus", "()I", "getVerifyStatus", "Lcom/loostone/libtuning/inf/extern/IBaseKeyService;", "getMicService", "()Lcom/loostone/libtuning/inf/extern/IBaseKeyService;", "Lcom/loostone/libtuning/inf/extern/IMicControl;", "getMicControlService", "()Lcom/loostone/libtuning/inf/extern/IMicControl;", "", "hasDongle", "()Z", "Lcom/loostone/libtuning/inf/extern/IVolumeAdjustment;", "volumeAdjustment$delegate", "Lkotlin/Lazy;", "getVolumeAdjustment", "()Lcom/loostone/libtuning/inf/extern/IVolumeAdjustment;", "volumeAdjustment", "Landroid/media/AudioManager;", "audioManager$delegate", "getAudioManager", "()Landroid/media/AudioManager;", "audioManager", "micStatus", "I", "Lcom/loostone/libtuning/inf/extern/IKeyListener;", "keyListener", "Lcom/loostone/libtuning/inf/extern/IKeyListener;", "Landroid/content/Context;", "context", "Landroid/content/Context;", "LOooO00o/OooO0O0/OooO0o0/OooO00o/OooO00o/OooO00o;", "infoReceiver$delegate", "getInfoReceiver", "()LOooO00o/OooO0O0/OooO0o0/OooO00o/OooO00o/OooO00o;", "infoReceiver", "LOooO00o/OooO0O0/OooO0Oo/OooO00o;", "networkMgr$delegate", "getNetworkMgr", "()LOooO00o/OooO0O0/OooO0Oo/OooO00o;", "networkMgr", "LOooO00o/OooO0O0/OooO0o0/OooO0Oo/OooO0OO;", "micPowerListener", "LOooO00o/OooO0O0/OooO0o0/OooO0Oo/OooO0OO;", "getMicPowerListener", "()LOooO00o/OooO0O0/OooO0o0/OooO0Oo/OooO0OO;", "setMicPowerListener", "(LOooO00o/OooO0O0/OooO0o0/OooO0Oo/OooO0OO;)V", "com/loostone/libtuning/channel/BaseChannelMgr$dongleCallback$1", "dongleCallback", "Lcom/loostone/libtuning/channel/BaseChannelMgr$dongleCallback$1;", "Lcom/loostone/libtuning/process/dongle/LocalDongleManager;", "localDongleManager$delegate", "getLocalDongleManager", "()Lcom/loostone/libtuning/process/dongle/LocalDongleManager;", "localDongleManager", "verifyStatus", "keyService$delegate", "getKeyService", "keyService", "LOooO00o/OooO0O0/OooO0o0/OooO0o0/OooO0O0/z1/OooO00o;", "fullSceneMode$delegate", "getFullSceneMode", "()LOooO00o/OooO0O0/OooO0o0/OooO0o0/OooO0O0/z1/OooO00o;", "fullSceneMode", "<init>", "(Landroid/content/Context;)V", "Companion", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public abstract class BaseChannelMgr implements IChannelInfo {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private static final String TAG = "BaseChannelMgr";
    @NotNull
    private final Lazy audioManager$delegate;
    @NotNull
    private final Context context;
    @NotNull
    private final BaseChannelMgr$dongleCallback$1 dongleCallback;
    @NotNull
    private final Lazy fullSceneMode$delegate;
    @NotNull
    private final Lazy infoReceiver$delegate;
    @NotNull
    private final IKeyListener keyListener;
    @NotNull
    private final Lazy keyService$delegate;
    @NotNull
    private final Lazy localDongleManager$delegate;
    @Nullable
    private OooO0OO micPowerListener;
    private int micStatus;
    @NotNull
    private final Lazy networkMgr$delegate;
    private int verifyStatus;
    @NotNull
    private final Lazy volumeAdjustment$delegate;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0005\u0010\u0006R\u0016\u0010\u0003\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004¨\u0006\u0007"}, d2 = {"Lcom/loostone/libtuning/channel/BaseChannelMgr$Companion;", "", "", "TAG", "Ljava/lang/String;", "<init>", "()V", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes4.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public BaseChannelMgr() {
        this(null, 1, null);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public BaseChannelMgr(android.content.Context r1, int r2, kotlin.jvm.internal.DefaultConstructorMarker r3) {
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
        throw new UnsupportedOperationException("Method not decompiled: com.loostone.libtuning.channel.BaseChannelMgr.<init>(android.content.Context, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final AudioManager getAudioManager() {
        return (AudioManager) this.audioManager$delegate.getValue();
    }

    private final void registerReceiver() {
        OooO00o infoReceiver = getInfoReceiver();
        infoReceiver.getClass();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.media.VOLUME_CHANGED_ACTION");
        intentFilter.addAction("android.media.STREAM_MUTE_CHANGED_ACTION");
        intentFilter.addAction("android.media.MASTER_MUTE_CHANGED_ACTION");
        intentFilter.addAction("android.media.MASTER_VOLUME_CHANGED_ACTION");
        intentFilter.addAction("android.media.EXTRA_MASTER_VOLUME_VALUE");
        intentFilter.addAction("android.media.EXTRA_MASTER_VOLUME_MUTED");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("com.android.sky.SendHotKey");
        intentFilter.addAction(USBMonitor.ACTION_USB_DEVICE_ATTACHED);
        infoReceiver.f260OooO00o.registerReceiver(infoReceiver, intentFilter);
        infoReceiver.OooO0OO();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void sendDongleInfo(int i) {
        Intent intent = new Intent();
        intent.setAction("com.loostone.tuning.BROAD.ACTION.DONGLE_INFO");
        intent.putExtra("dongleStatus", i);
        this.context.sendBroadcast(intent);
    }

    private final void unRegisterReceiver() {
        OooO00o infoReceiver = getInfoReceiver();
        infoReceiver.getClass();
        try {
            infoReceiver.f260OooO00o.unregisterReceiver(infoReceiver);
        } catch (Exception e) {
            String msg = Intrinsics.stringPlus("unRegister()", e.getMessage());
            Intrinsics.checkNotNullParameter("InfoReceiver", Progress.TAG);
            Intrinsics.checkNotNullParameter(msg, "msg");
            if (OooO0O0.OooO0O0 <= 5) {
                Log.e("LogTuning", "InfoReceiver -> " + msg);
            }
        }
        infoReceiver.OooO0o0.removeCallbacksAndMessages(null);
    }

    @NotNull
    public final OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.z1.OooO00o getFullSceneMode() {
        return (OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.z1.OooO00o) this.fullSceneMode$delegate.getValue();
    }

    @NotNull
    public final OooO00o getInfoReceiver() {
        return (OooO00o) this.infoReceiver$delegate.getValue();
    }

    @NotNull
    public final IBaseKeyService getKeyService() {
        return (IBaseKeyService) this.keyService$delegate.getValue();
    }

    @NotNull
    public final LocalDongleManager getLocalDongleManager() {
        return (LocalDongleManager) this.localDongleManager$delegate.getValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IChannelInfo
    @Nullable
    public IMicControl getMicControlService() {
        return getKeyService().getMicControl();
    }

    @Nullable
    public final OooO0OO getMicPowerListener() {
        return this.micPowerListener;
    }

    @Override // com.loostone.libtuning.inf.extern.IChannelInfo
    @NotNull
    public IBaseKeyService getMicService() {
        return getKeyService();
    }

    @NotNull
    public final OooO00o.OooO0O0.OooO0Oo.OooO00o getNetworkMgr() {
        return (OooO00o.OooO0O0.OooO0Oo.OooO00o) this.networkMgr$delegate.getValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IChannelInfo
    public int getPowerStatus() {
        return this.micStatus;
    }

    @Override // com.loostone.libtuning.inf.extern.IChannelInfo
    public int getVerifyStatus() {
        return this.verifyStatus;
    }

    @NotNull
    public final IVolumeAdjustment getVolumeAdjustment() {
        return (IVolumeAdjustment) this.volumeAdjustment$delegate.getValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IChannelInfo
    public boolean hasDongle() {
        HashMap<String, UsbDevice> deviceList = getLocalDongleManager().getMEventManager().OooO0O0().getDeviceList();
        if (deviceList != null) {
            for (UsbDevice usbDevice : deviceList.values()) {
                OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o oooO00o = OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o.f237OooO00o;
                Intrinsics.checkNotNullExpressionValue(usbDevice, "usbDevice");
                if (oooO00o.OooO00o(usbDevice)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void init() {
        getLocalDongleManager().init();
        registerReceiver();
        getLocalDongleManager().getMEventManager().OooO00o(this.dongleCallback);
        getKeyService().registerKeyListener(this.keyListener);
    }

    public void release() {
        getLocalDongleManager().release();
        unRegisterReceiver();
        getFullSceneMode().OooO0OO();
        getLocalDongleManager().getMEventManager().OooO0O0(this.dongleCallback);
        getKeyService().unRegisterKeyListener(this.keyListener);
    }

    public void resetKaraoke() {
        getFullSceneMode().OooO00o();
    }

    public final void setMicPowerListener(@Nullable OooO0OO oooO0OO) {
        this.micPowerListener = oooO0OO;
    }

    public void startKaraoke() {
        if (this.verifyStatus == 2) {
            getFullSceneMode().OooO0O0();
        }
    }

    public void stopKaraoke() {
        getFullSceneMode().OooO0OO();
    }

    /* JADX WARN: Type inference failed for: r2v17, types: [com.loostone.libtuning.channel.BaseChannelMgr$dongleCallback$1] */
    public BaseChannelMgr(@NotNull Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
        this.micStatus = 4;
        this.localDongleManager$delegate = LazyKt.lazy(new Function0<LocalDongleManager>() { // from class: com.loostone.libtuning.channel.BaseChannelMgr$localDongleManager$2
            {
                super(0);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // kotlin.jvm.functions.Function0
            @NotNull
            public final LocalDongleManager invoke() {
                Context context2;
                context2 = BaseChannelMgr.this.context;
                return new LocalDongleManager(context2);
            }
        });
        this.fullSceneMode$delegate = LazyKt.lazy(new Function0<OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.z1.OooO00o>() { // from class: com.loostone.libtuning.channel.BaseChannelMgr$fullSceneMode$2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // kotlin.jvm.functions.Function0
            @NotNull
            public final OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.z1.OooO00o invoke() {
                Integer OooO00o2 = OooO.f279OooO00o.OooOO0O().OooO00o();
                if (OooO00o2 != null && OooO00o2.intValue() == 2) {
                    Intrinsics.checkNotNullParameter("BaseChannelMgr", Progress.TAG);
                    Intrinsics.checkNotNullParameter("workMode() ------> ko mode", NotificationCompat.CATEGORY_MESSAGE);
                    if (OooO0O0.OooO0O0 <= 3) {
                        Log.i("LogTuning", "BaseChannelMgr -> workMode() ------> ko mode");
                    }
                    return new OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.z1.OooO0O0();
                } else if (OooO00o2 != null && OooO00o2.intValue() == 3) {
                    Intrinsics.checkNotNullParameter("BaseChannelMgr", Progress.TAG);
                    Intrinsics.checkNotNullParameter("workMode() ------> so&ko mode", NotificationCompat.CATEGORY_MESSAGE);
                    if (OooO0O0.OooO0O0 <= 3) {
                        Log.i("LogTuning", "BaseChannelMgr -> workMode() ------> so&ko mode");
                    }
                    return new OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.z1.OooO0OO();
                } else {
                    Intrinsics.checkNotNullParameter("BaseChannelMgr", Progress.TAG);
                    Intrinsics.checkNotNullParameter("workMode() ------> so mode", NotificationCompat.CATEGORY_MESSAGE);
                    if (OooO0O0.OooO0O0 <= 3) {
                        Log.i("LogTuning", "BaseChannelMgr -> workMode() ------> so mode");
                    }
                    return new OooO0o();
                }
            }
        });
        this.volumeAdjustment$delegate = LazyKt.lazy(new Function0<IVolumeAdjustment>() { // from class: com.loostone.libtuning.channel.BaseChannelMgr$volumeAdjustment$2
            {
                super(0);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // kotlin.jvm.functions.Function0
            @NotNull
            public final IVolumeAdjustment invoke() {
                IVolumeAdjustment iVolumeAdjustment = BaseChannelMgr.this.getFullSceneMode().f593OooO00o;
                if (iVolumeAdjustment != null) {
                    return iVolumeAdjustment;
                }
                Intrinsics.throwUninitializedPropertyAccessException("volumeAdjustment");
                throw null;
            }
        });
        this.keyService$delegate = LazyKt.lazy(new Function0<IBaseKeyService>() { // from class: com.loostone.libtuning.channel.BaseChannelMgr$keyService$2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // kotlin.jvm.functions.Function0
            @NotNull
            public final IBaseKeyService invoke() {
                OooO.f279OooO00o.getClass();
                Integer num = (Integer) ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooO.OooOOoo.getValue()).OooO00o();
                if (num != null && num.intValue() == 2) {
                    Intrinsics.checkNotNullParameter("BaseChannelMgr", Progress.TAG);
                    Intrinsics.checkNotNullParameter("keyMode KEYBOARD", NotificationCompat.CATEGORY_MESSAGE);
                    if (OooO0O0.OooO0O0 <= 3) {
                        Log.i("LogTuning", "BaseChannelMgr -> keyMode KEYBOARD");
                    }
                    return new OooO00o.OooO0O0.OooO0o0.OooO00o.OooO0O0.OooO0O0();
                }
                Intrinsics.checkNotNullParameter("BaseChannelMgr", Progress.TAG);
                Intrinsics.checkNotNullParameter("keyMode HID", NotificationCompat.CATEGORY_MESSAGE);
                if (OooO0O0.OooO0O0 <= 3) {
                    Log.i("LogTuning", "BaseChannelMgr -> keyMode HID");
                }
                return new OooO00o.OooO0O0.OooO0o0.OooO00o.OooO0O0.OooO00o(null);
            }
        });
        this.networkMgr$delegate = LazyKt.lazy(new Function0<OooO00o.OooO0O0.OooO0Oo.OooO00o>() { // from class: com.loostone.libtuning.channel.BaseChannelMgr$networkMgr$2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // kotlin.jvm.functions.Function0
            @NotNull
            public final OooO00o.OooO0O0.OooO0Oo.OooO00o invoke() {
                Context context2 = OooO00o.OooO0O0.OooO0O0.OooO00o.OooO0O0;
                if (context2 != null) {
                    return new OooO00o.OooO0O0.OooO0Oo.OooO00o(context2);
                }
                Intrinsics.throwUninitializedPropertyAccessException("instance");
                throw null;
            }
        });
        this.audioManager$delegate = LazyKt.lazy(new Function0<AudioManager>() { // from class: com.loostone.libtuning.channel.BaseChannelMgr$audioManager$2
            {
                super(0);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // kotlin.jvm.functions.Function0
            @NotNull
            public final AudioManager invoke() {
                Context context2;
                context2 = BaseChannelMgr.this.context;
                Object systemService = context2.getSystemService("audio");
                if (systemService != null) {
                    return (AudioManager) systemService;
                }
                throw new NullPointerException("null cannot be cast to non-null type android.media.AudioManager");
            }
        });
        this.infoReceiver$delegate = LazyKt.lazy(new Function0<OooO00o>() { // from class: com.loostone.libtuning.channel.BaseChannelMgr$infoReceiver$2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // kotlin.jvm.functions.Function0
            @NotNull
            public final OooO00o invoke() {
                Context context2 = OooO00o.OooO0O0.OooO0O0.OooO00o.OooO0O0;
                if (context2 != null) {
                    return new OooO00o(context2);
                }
                Intrinsics.throwUninitializedPropertyAccessException("instance");
                throw null;
            }
        });
        this.keyListener = new IKeyListener() { // from class: com.loostone.libtuning.channel.BaseChannelMgr$keyListener$1
            @Override // com.loostone.libtuning.inf.extern.IKeyListener
            public void onPower(int i, boolean z) {
                AudioManager audioManager;
                AudioManager audioManager2;
                if (i != 0) {
                    return;
                }
                int i2 = 4;
                BaseChannelMgr.this.sendDongleInfo(z ? 3 : 4);
                BaseChannelMgr baseChannelMgr = BaseChannelMgr.this;
                if (z) {
                    audioManager2 = baseChannelMgr.getAudioManager();
                    audioManager2.setParameters("GlobalMicLowLatency=enable");
                    Intrinsics.checkNotNullParameter("BaseChannelMgr", Progress.TAG);
                    Intrinsics.checkNotNullParameter("低延迟开启 ------> GlobalMicLowLatency=enable", NotificationCompat.CATEGORY_MESSAGE);
                    if (OooO0O0.OooO0O0 <= 3) {
                        Log.i("LogTuning", "BaseChannelMgr -> 低延迟开启 ------> GlobalMicLowLatency=enable");
                    }
                    i2 = 3;
                } else {
                    audioManager = baseChannelMgr.getAudioManager();
                    audioManager.setParameters("GlobalMicLowLatency=disable");
                    Intrinsics.checkNotNullParameter("BaseChannelMgr", Progress.TAG);
                    Intrinsics.checkNotNullParameter("低延迟关闭 ------> GlobalMicLowLatency=disable", NotificationCompat.CATEGORY_MESSAGE);
                    if (OooO0O0.OooO0O0 <= 3) {
                        Log.i("LogTuning", "BaseChannelMgr -> 低延迟关闭 ------> GlobalMicLowLatency=disable");
                    }
                }
                baseChannelMgr.micStatus = i2;
            }

            @Override // com.loostone.libtuning.inf.extern.IKeyListener
            public void onTVPhoneSwitch(int i, boolean z) {
            }

            @Override // com.loostone.libtuning.inf.extern.IKeyListener
            public void onVoice(int i, boolean z) {
            }

            @Override // com.loostone.libtuning.inf.extern.IKeyListener
            public void onVolumeDown(int i, boolean z) {
            }

            @Override // com.loostone.libtuning.inf.extern.IKeyListener
            public void onVolumeUp(int i, boolean z) {
            }
        };
        this.dongleCallback = new OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0() { // from class: com.loostone.libtuning.channel.BaseChannelMgr$dongleCallback$1
            @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0
            public void onDeviceAddedWithHid(@NotNull OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO00o cachedDevice) {
                Intrinsics.checkNotNullParameter(cachedDevice, "cachedDevice");
                BaseChannelMgr.this.verifyStatus = 2;
                BaseChannelMgr.this.sendDongleInfo(2);
            }

            @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0
            public void onDeviceDeniedByHid(@NotNull OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO00o cachedDevice) {
                Intrinsics.checkNotNullParameter(cachedDevice, "cachedDevice");
            }

            @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0
            public void onDeviceRemoved(@NotNull OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO00o cachedDevice) {
                Intrinsics.checkNotNullParameter(cachedDevice, "cachedDevice");
                BaseChannelMgr.this.verifyStatus = 0;
                BaseChannelMgr.this.sendDongleInfo(0);
            }

            @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0
            public void onDonglePermissionDenied() {
                BaseChannelMgr.this.verifyStatus = 0;
            }

            @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0
            public void onDonglePermissionGranted(@NotNull UsbDevice usbDevice) {
                Intrinsics.checkNotNullParameter(usbDevice, "usbDevice");
            }
        };
    }
}
