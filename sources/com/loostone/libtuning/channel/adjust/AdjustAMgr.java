package com.loostone.libtuning.channel.adjust;

import OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO00o;
import OooO00o.OooO0O0.OooO0Oo.OooO0O0.OooOO0O;
import OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO;
import OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0;
import OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.google.gson.Gson;
import com.loostone.libtuning.channel.BaseChannelMgr;
import com.loostone.libtuning.data.bean.EQItem;
import com.loostone.libtuning.data.config.DefaultConfig;
import com.loostone.libtuning.inf.extern.IAdjustEffect;
import com.loostone.libtuning.inf.extern.IKeyListener;
import com.lzy.okgo.model.Progress;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 *2\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u00042\b\u0012\u0004\u0012\u00020\u00060\u0005:\u0001*B\u0011\u0012\b\b\u0002\u0010'\u001a\u00020&¢\u0006\u0004\b(\u0010)J\u000f\u0010\b\u001a\u00020\u0007H\u0016¢\u0006\u0004\b\b\u0010\tJ\u000f\u0010\n\u001a\u00020\u0007H\u0016¢\u0006\u0004\b\n\u0010\tJ\u0017\u0010\r\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u000bH\u0016¢\u0006\u0004\b\r\u0010\u000eJ\u000f\u0010\u000f\u001a\u00020\u0007H\u0016¢\u0006\u0004\b\u000f\u0010\tJ\u0017\u0010\u0012\u001a\u00020\u00072\u0006\u0010\u0011\u001a\u00020\u0010H\u0016¢\u0006\u0004\b\u0012\u0010\u0013J\u0017\u0010\u0014\u001a\u00020\u00072\u0006\u0010\u0011\u001a\u00020\u0010H\u0016¢\u0006\u0004\b\u0014\u0010\u0013J\u0017\u0010\u0015\u001a\u00020\u00072\u0006\u0010\u0011\u001a\u00020\u0010H\u0016¢\u0006\u0004\b\u0015\u0010\u0013J\u001f\u0010\u0019\u001a\u00020\u00072\u0006\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u0006H\u0016¢\u0006\u0004\b\u0019\u0010\u001aJ\u001f\u0010\u001b\u001a\u00020\u00072\u0006\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u0006H\u0016¢\u0006\u0004\b\u001b\u0010\u001aJ\u001f\u0010\u001c\u001a\u00020\u00072\u0006\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u0006H\u0016¢\u0006\u0004\b\u001c\u0010\u001aJ\u001f\u0010\u001d\u001a\u00020\u00072\u0006\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u0006H\u0016¢\u0006\u0004\b\u001d\u0010\u001aJ\u001f\u0010\u001e\u001a\u00020\u00072\u0006\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u0006H\u0016¢\u0006\u0004\b\u001e\u0010\u001aJ\u0017\u0010 \u001a\u00020\u00072\u0006\u0010\u001f\u001a\u00020\u0006H\u0016¢\u0006\u0004\b \u0010!J\u000f\u0010\"\u001a\u00020\u0007H\u0016¢\u0006\u0004\b\"\u0010\tJ\u000f\u0010$\u001a\u00020#H\u0016¢\u0006\u0004\b$\u0010%¨\u0006+"}, d2 = {"Lcom/loostone/libtuning/channel/adjust/AdjustAMgr;", "Lcom/loostone/libtuning/channel/BaseChannelMgr;", "LOooO00o/OooO0O0/OooO0o0/OooO0Oo/OooO0O0;", "Lcom/loostone/libtuning/inf/extern/IKeyListener;", "Lcom/loostone/libtuning/inf/extern/IAdjustEffect;", "LOooO00o/OooO0O0/OooO0Oo/OooO0O0/OooOO0O;", "", "", "init", "()V", "release", "Landroid/hardware/usb/UsbDevice;", "usbDevice", "onDonglePermissionGranted", "(Landroid/hardware/usb/UsbDevice;)V", "onDonglePermissionDenied", "LOooO00o/OooO0O0/OooO0O0/OooO0Oo/OooO0O0/OooO00o;", "cachedDevice", "onDeviceAddedWithHid", "(LOooO00o/OooO0O0/OooO0O0/OooO0Oo/OooO0O0/OooO00o;)V", "onDeviceDeniedByHid", "onDeviceRemoved", "", "mic", "actionDown", "onPower", "(IZ)V", "onVoice", "onTVPhoneSwitch", "onVolumeUp", "onVolumeDown", "arg", "onSuccess", "(Z)V", "onError", "", "getConfigAll", "()Ljava/lang/String;", "Landroid/content/Context;", "context", "<init>", "(Landroid/content/Context;)V", "Companion", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public final class AdjustAMgr extends BaseChannelMgr implements OooO0O0, IKeyListener, IAdjustEffect, OooOO0O<Boolean> {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private static final String TAG = "AdjustMgr";

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0005\u0010\u0006R\u0016\u0010\u0003\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004¨\u0006\u0007"}, d2 = {"Lcom/loostone/libtuning/channel/adjust/AdjustAMgr$Companion;", "", "", "TAG", "Ljava/lang/String;", "<init>", "()V", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes4.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public AdjustAMgr() {
        this(null, 1, null);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public AdjustAMgr(android.content.Context r1, int r2, kotlin.jvm.internal.DefaultConstructorMarker r3) {
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
        throw new UnsupportedOperationException("Method not decompiled: com.loostone.libtuning.channel.adjust.AdjustAMgr.<init>(android.content.Context, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    @Override // com.loostone.libtuning.inf.extern.IAdjustEffect
    @NotNull
    public String getConfigAll() {
        DefaultConfig defaultConfig = new DefaultConfig();
        OooO oooO = OooO.f279OooO00o;
        Long OooO00o2 = oooO.OooO0O0().OooO00o();
        defaultConfig.setChannel(OooO00o2 == null ? 0L : OooO00o2.longValue());
        Integer OooO00o3 = oooO.OooOO0O().OooO00o();
        defaultConfig.setWorkModeInt(OooO00o3 == null ? 0 : OooO00o3.intValue());
        OooOO0 oooOO0 = OooOO0.f398OooO00o;
        String OooO00o4 = oooOO0.o0000o0O().OooO00o();
        if (OooO00o4 == null) {
            OooO00o4 = "";
        }
        defaultConfig.setWorkMode(OooO00o4);
        Integer OooO00o5 = oooOO0.o0000O0O().OooO00o();
        defaultConfig.setBeta(OooO00o5 == null ? 0 : OooO00o5.intValue());
        Integer OooO00o6 = oooO.OooO0Oo().OooO00o();
        defaultConfig.setLogLevel(OooO00o6 == null ? 0 : OooO00o6.intValue());
        defaultConfig.setMicVolume(getVolumeAdjustment().getMicVolume());
        defaultConfig.setMusicVolume(getVolumeAdjustment().getMusicVolume());
        defaultConfig.setAef1ReverbEnable(getVolumeAdjustment().isAef1ReverbEnable() ? 1 : 0);
        defaultConfig.setAef1ReverbOutGain(getVolumeAdjustment().getAef1ReverbOutGain());
        defaultConfig.setAef2EQEnable(getVolumeAdjustment().isAef2EQEnable() ? 1 : 0);
        defaultConfig.setAef2MusicEQEnable(getVolumeAdjustment().isAef2MusicEQEnable() ? 1 : 0);
        EQItem eQItem = new EQItem();
        eQItem.setEq1(Integer.valueOf(getVolumeAdjustment().getAef2EQ1()));
        eQItem.setEq2(Integer.valueOf(getVolumeAdjustment().getAef2EQ2()));
        eQItem.setEq3(Integer.valueOf(getVolumeAdjustment().getAef2EQ3()));
        eQItem.setEq4(Integer.valueOf(getVolumeAdjustment().getAef2EQ4()));
        eQItem.setEq5(Integer.valueOf(getVolumeAdjustment().getAef2EQ5()));
        eQItem.setEq6(Integer.valueOf(getVolumeAdjustment().getAef2EQ6()));
        eQItem.setEq7(Integer.valueOf(getVolumeAdjustment().getAef2EQ7()));
        eQItem.setEq8(Integer.valueOf(getVolumeAdjustment().getAef2EQ8()));
        eQItem.setEq9(Integer.valueOf(getVolumeAdjustment().getAef2EQ9()));
        eQItem.setFrequency1(Integer.valueOf(getVolumeAdjustment().getAef2EQ1Frequency()));
        eQItem.setFrequency2(Integer.valueOf(getVolumeAdjustment().getAef2EQ2Frequency()));
        eQItem.setFrequency3(Integer.valueOf(getVolumeAdjustment().getAef2EQ3Frequency()));
        eQItem.setFrequency4(Integer.valueOf(getVolumeAdjustment().getAef2EQ4Frequency()));
        eQItem.setFrequency5(Integer.valueOf(getVolumeAdjustment().getAef2EQ5Frequency()));
        eQItem.setFrequency6(Integer.valueOf(getVolumeAdjustment().getAef2EQ6Frequency()));
        eQItem.setFrequency7(Integer.valueOf(getVolumeAdjustment().getAef2EQ7Frequency()));
        eQItem.setFrequency8(Integer.valueOf(getVolumeAdjustment().getAef2EQ8Frequency()));
        eQItem.setFrequency9(Integer.valueOf(getVolumeAdjustment().getAef2EQ9Frequency()));
        eQItem.setQ1(Integer.valueOf(getVolumeAdjustment().getAef3EqQ1()));
        eQItem.setQ2(Integer.valueOf(getVolumeAdjustment().getAef3EqQ2()));
        eQItem.setQ3(Integer.valueOf(getVolumeAdjustment().getAef3EqQ3()));
        eQItem.setQ4(Integer.valueOf(getVolumeAdjustment().getAef3EqQ4()));
        eQItem.setQ5(Integer.valueOf(getVolumeAdjustment().getAef3EqQ5()));
        eQItem.setQ6(Integer.valueOf(getVolumeAdjustment().getAef3EqQ6()));
        eQItem.setQ7(Integer.valueOf(getVolumeAdjustment().getAef3EqQ7()));
        eQItem.setQ8(Integer.valueOf(getVolumeAdjustment().getAef3EqQ8()));
        eQItem.setQ9(Integer.valueOf(getVolumeAdjustment().getAef3EqQ9()));
        eQItem.setType1(Integer.valueOf(getVolumeAdjustment().getAef3EQ1Type()));
        eQItem.setType2(Integer.valueOf(getVolumeAdjustment().getAef3EQ2Type()));
        eQItem.setType3(Integer.valueOf(getVolumeAdjustment().getAef3EQ3Type()));
        eQItem.setType4(Integer.valueOf(getVolumeAdjustment().getAef3EQ4Type()));
        eQItem.setType5(Integer.valueOf(getVolumeAdjustment().getAef3EQ5Type()));
        eQItem.setType6(Integer.valueOf(getVolumeAdjustment().getAef3EQ6Type()));
        eQItem.setType7(Integer.valueOf(getVolumeAdjustment().getAef3EQ7Type()));
        eQItem.setType8(Integer.valueOf(getVolumeAdjustment().getAef3EQ8Type()));
        eQItem.setType9(Integer.valueOf(getVolumeAdjustment().getAef3EQ9Type()));
        EQItem eQItem2 = new EQItem();
        eQItem2.setEq1(Integer.valueOf(getVolumeAdjustment().getAef2MusicEQ1()));
        eQItem2.setEq2(Integer.valueOf(getVolumeAdjustment().getAef2MusicEQ2()));
        eQItem2.setEq3(Integer.valueOf(getVolumeAdjustment().getAef2MusicEQ3()));
        eQItem2.setEq4(Integer.valueOf(getVolumeAdjustment().getAef2MusicEQ4()));
        eQItem2.setEq5(Integer.valueOf(getVolumeAdjustment().getAef2MusicEQ5()));
        eQItem2.setEq6(Integer.valueOf(getVolumeAdjustment().getAef2MusicEQ6()));
        eQItem2.setEq7(Integer.valueOf(getVolumeAdjustment().getAef2MusicEQ7()));
        eQItem2.setEq8(Integer.valueOf(getVolumeAdjustment().getAef2MusicEQ8()));
        eQItem2.setEq9(Integer.valueOf(getVolumeAdjustment().getAef2MusicEQ9()));
        eQItem2.setFrequency1(Integer.valueOf(getVolumeAdjustment().getAef2MusicEQ1Frequency()));
        eQItem2.setFrequency2(Integer.valueOf(getVolumeAdjustment().getAef2MusicEQ2Frequency()));
        eQItem2.setFrequency3(Integer.valueOf(getVolumeAdjustment().getAef2MusicEQ3Frequency()));
        eQItem2.setFrequency4(Integer.valueOf(getVolumeAdjustment().getAef2MusicEQ4Frequency()));
        eQItem2.setFrequency5(Integer.valueOf(getVolumeAdjustment().getAef2MusicEQ5Frequency()));
        eQItem2.setFrequency6(Integer.valueOf(getVolumeAdjustment().getAef2MusicEQ6Frequency()));
        eQItem2.setFrequency7(Integer.valueOf(getVolumeAdjustment().getAef2MusicEQ7Frequency()));
        eQItem2.setFrequency8(Integer.valueOf(getVolumeAdjustment().getAef2MusicEQ8Frequency()));
        eQItem2.setFrequency9(Integer.valueOf(getVolumeAdjustment().getAef2MusicEQ9Frequency()));
        defaultConfig.setEqList(CollectionsKt.arrayListOf(eQItem, eQItem2));
        defaultConfig.setAef2EchoEnable(getVolumeAdjustment().isAef2EchoEnable() ? 1 : 0);
        defaultConfig.setAef2EchoGain(getVolumeAdjustment().getAef2EchoGain());
        defaultConfig.setAef2EchoType(getVolumeAdjustment().getAef2EchoType());
        defaultConfig.setAef2EchoTime(getVolumeAdjustment().getAef2EchoTime());
        defaultConfig.setAef2EchoFeedback(getVolumeAdjustment().getAef2EchoFeedBack());
        defaultConfig.setAef2ReverbEnable(getVolumeAdjustment().isAef2ReverbEnable() ? 1 : 0);
        defaultConfig.setAef2ReverbType(getVolumeAdjustment().getAef2ReverbMode());
        defaultConfig.setAef2ReverbVolume(getVolumeAdjustment().getAef2ReverbVolume());
        defaultConfig.setAef2ReverbTime(getVolumeAdjustment().getAef2ReverbTime());
        defaultConfig.setAef2VoiceMode(getVolumeAdjustment().getAef2VoiceMode());
        defaultConfig.setAef2CompressorEnable(getVolumeAdjustment().isAef2CompressorEnable() ? 1 : 0);
        defaultConfig.setAef2CompressorThreshold(getVolumeAdjustment().getAef2CompressorThreshold());
        defaultConfig.setAef2CompressorRatio(getVolumeAdjustment().getAef2CompressorRatio());
        defaultConfig.setAef2CompressorAttack(getVolumeAdjustment().getAef2CompressorAttack());
        defaultConfig.setAef2CompressorRelease(getVolumeAdjustment().getAef2CompressorRelease());
        defaultConfig.setAef2CompressorPregain(getVolumeAdjustment().getAef2CompressorPregain());
        String json = new Gson().toJson(defaultConfig);
        Intrinsics.checkNotNullExpressionValue(json, "Gson().toJson(config)");
        return json;
    }

    @Override // com.loostone.libtuning.channel.BaseChannelMgr
    public void init() {
        super.init();
        getLocalDongleManager().getMEventManager().OooO00o(this);
        getKeyService().registerKeyListener(this);
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0
    public void onDeviceAddedWithHid(@NotNull OooO00o cachedDevice) {
        Intrinsics.checkNotNullParameter(cachedDevice, "cachedDevice");
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("设备验证通过, 打开so或者ko", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", TAG + " -> 设备验证通过, 打开so或者ko");
        }
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0
    public void onDeviceDeniedByHid(@NotNull OooO00o cachedDevice) {
        Intrinsics.checkNotNullParameter(cachedDevice, "cachedDevice");
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0
    public void onDeviceRemoved(@NotNull OooO00o cachedDevice) {
        Intrinsics.checkNotNullParameter(cachedDevice, "cachedDevice");
        String msg = Intrinsics.stringPlus("设备移除 ", cachedDevice.OooO0oO);
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 1) {
            Log.i("LogTuning", TAG + " -> " + msg);
        }
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0
    public void onDonglePermissionDenied() {
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0
    public void onDonglePermissionGranted(@NotNull UsbDevice usbDevice) {
        Intrinsics.checkNotNullParameter(usbDevice, "usbDevice");
        String msg = Intrinsics.stringPlus("权限申请成功 ", usbDevice.getProductName());
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> " + msg);
        }
        String msg2 = Intrinsics.stringPlus("设备数量 ", Integer.valueOf(((ArrayList) getLocalDongleManager().getMCachedDongleDeviceManager().OooO00o()).size()));
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(msg2, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> " + msg2);
        }
    }

    @Override // OooO00o.OooO0O0.OooO0Oo.OooO0O0.OooOO0O
    public void onError() {
    }

    @Override // com.loostone.libtuning.inf.extern.IKeyListener
    public void onPower(int i, boolean z) {
        if (i != 0) {
            return;
        }
        if (z) {
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("电源按下", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " -> 电源按下");
                return;
            }
            return;
        }
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("电源弹起", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> 电源弹起");
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
            Intrinsics.checkNotNullParameter("TV按下", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " -> TV按下");
                return;
            }
            return;
        }
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("TV弹起", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> TV弹起");
        }
    }

    @Override // com.loostone.libtuning.inf.extern.IKeyListener
    public void onVoice(int i, boolean z) {
        if (i != 0) {
            return;
        }
        if (z) {
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("语音按下", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " -> 语音按下");
                return;
            }
            return;
        }
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("语音弹起", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> 语音弹起");
        }
    }

    @Override // com.loostone.libtuning.inf.extern.IKeyListener
    public void onVolumeDown(int i, boolean z) {
        if (i != 0) {
            return;
        }
        if (z) {
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("音量减按下", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " -> 音量减按下");
                return;
            }
            return;
        }
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("音量减弹起", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> 音量减弹起");
        }
    }

    @Override // com.loostone.libtuning.inf.extern.IKeyListener
    public void onVolumeUp(int i, boolean z) {
        if (i != 0) {
            return;
        }
        if (z) {
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter("音量加按下", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " -> 音量加按下");
                return;
            }
            return;
        }
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter("音量加弹起", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> 音量加弹起");
        }
    }

    @Override // com.loostone.libtuning.channel.BaseChannelMgr
    public void release() {
        super.release();
        getLocalDongleManager().getMEventManager().OooO0O0(this);
        getKeyService().unRegisterKeyListener(this);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AdjustAMgr(@NotNull Context context) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
    }
}
