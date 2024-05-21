package com.loostone.libtuning.inf.extern;

import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0002\b\u001c\n\u0002\u0010\u0006\n\u0002\b\b\bf\u0018\u00002\u00020\u0001J\u0017\u0010\u0004\u001a\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H&¢\u0006\u0004\b\u0004\u0010\u0005J\u000f\u0010\u0006\u001a\u00020\u0002H&¢\u0006\u0004\b\u0006\u0010\u0007J\u0017\u0010\t\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\u0002H&¢\u0006\u0004\b\t\u0010\u0005J\u0017\u0010\n\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\u0002H&¢\u0006\u0004\b\n\u0010\u0005J\u0017\u0010\u000b\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\u0002H&¢\u0006\u0004\b\u000b\u0010\u0005J\u0017\u0010\f\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\u0002H&¢\u0006\u0004\b\f\u0010\u0005J\u0017\u0010\r\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\u0002H&¢\u0006\u0004\b\r\u0010\u0005J\u0017\u0010\u000e\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\u0002H&¢\u0006\u0004\b\u000e\u0010\u0005J\u0017\u0010\u000f\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\u0002H&¢\u0006\u0004\b\u000f\u0010\u0005J\u0017\u0010\u0010\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\u0002H&¢\u0006\u0004\b\u0010\u0010\u0005J\u0017\u0010\u0011\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\u0002H&¢\u0006\u0004\b\u0011\u0010\u0005J\u000f\u0010\u0012\u001a\u00020\u0002H&¢\u0006\u0004\b\u0012\u0010\u0007J\u000f\u0010\u0013\u001a\u00020\u0002H&¢\u0006\u0004\b\u0013\u0010\u0007J\u000f\u0010\u0014\u001a\u00020\u0002H&¢\u0006\u0004\b\u0014\u0010\u0007J\u000f\u0010\u0015\u001a\u00020\u0002H&¢\u0006\u0004\b\u0015\u0010\u0007J\u000f\u0010\u0016\u001a\u00020\u0002H&¢\u0006\u0004\b\u0016\u0010\u0007J\u000f\u0010\u0017\u001a\u00020\u0002H&¢\u0006\u0004\b\u0017\u0010\u0007J\u000f\u0010\u0018\u001a\u00020\u0002H&¢\u0006\u0004\b\u0018\u0010\u0007J\u000f\u0010\u0019\u001a\u00020\u0002H&¢\u0006\u0004\b\u0019\u0010\u0007J\u000f\u0010\u001a\u001a\u00020\u0002H&¢\u0006\u0004\b\u001a\u0010\u0007J\u0017\u0010\u001c\u001a\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0002H&¢\u0006\u0004\b\u001c\u0010\u0005J\u0017\u0010\u001e\u001a\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u0002H&¢\u0006\u0004\b\u001e\u0010\u0005J\u0017\u0010 \u001a\u00020\u001f2\u0006\u0010\u001d\u001a\u00020\u0002H&¢\u0006\u0004\b \u0010!J\u0017\u0010\"\u001a\u00020\u001f2\u0006\u0010\u001d\u001a\u00020\u0002H&¢\u0006\u0004\b\"\u0010!J\u0017\u0010#\u001a\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u0002H&¢\u0006\u0004\b#\u0010\u0005J\u000f\u0010$\u001a\u00020\u0002H\u0016¢\u0006\u0004\b$\u0010\u0007J\u0017\u0010&\u001a\u00020\u00022\u0006\u0010%\u001a\u00020\u0002H&¢\u0006\u0004\b&\u0010\u0005¨\u0006'"}, d2 = {"Lcom/loostone/libtuning/inf/extern/IMicControl;", "", "", SpeechConstants.KEY_COMMAND_TYPE, "setAudioEffect", "(I)I", "getAudioEffect", "()I", "value", "setMicEQ1", "setMicEQ2", "setMicEQ3", "setMicEQ4", "setMicEQ5", "setMicEQ6", "setMicEQ7", "setMicEQ8", "setMicEQ9", "getMicEQ1", "getMicEQ2", "getMicEQ3", "getMicEQ4", "getMicEQ5", "getMicEQ6", "getMicEQ7", "getMicEQ8", "getMicEQ9", "status", "setMicSwitch", "mic", "getBaseStatus", "", "getMicBattery", "(I)D", "getMicBatteryPercentage", "getMicVolumeHW", "getMicShiftFrequency", "fsEnable", "setMicShiftFrequency", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public interface IMicControl {

    @Metadata(bv = {1, 0, 3}, d1 = {}, d2 = {}, k = 3, mv = {1, 5, 1})
    /* loaded from: classes4.dex */
    public static final class DefaultImpls {
        public static int getMicShiftFrequency(@NotNull IMicControl iMicControl) {
            Intrinsics.checkNotNullParameter(iMicControl, "this");
            return -1;
        }
    }

    int getAudioEffect();

    int getBaseStatus(int i);

    double getMicBattery(int i);

    double getMicBatteryPercentage(int i);

    int getMicEQ1();

    int getMicEQ2();

    int getMicEQ3();

    int getMicEQ4();

    int getMicEQ5();

    int getMicEQ6();

    int getMicEQ7();

    int getMicEQ8();

    int getMicEQ9();

    int getMicShiftFrequency();

    int getMicVolumeHW(int i);

    int setAudioEffect(int i);

    int setMicEQ1(int i);

    int setMicEQ2(int i);

    int setMicEQ3(int i);

    int setMicEQ4(int i);

    int setMicEQ5(int i);

    int setMicEQ6(int i);

    int setMicEQ7(int i);

    int setMicEQ8(int i);

    int setMicEQ9(int i);

    int setMicShiftFrequency(int i);

    int setMicSwitch(int i);
}
