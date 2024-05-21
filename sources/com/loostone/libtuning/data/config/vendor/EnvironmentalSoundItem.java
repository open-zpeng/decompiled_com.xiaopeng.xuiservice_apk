package com.loostone.libtuning.data.config.vendor;

import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u001e\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b!\u0010\"J\u000f\u0010\u0003\u001a\u00020\u0002H\u0016¢\u0006\u0004\b\u0003\u0010\u0004R\"\u0010\u0006\u001a\u00020\u00058\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0006\u0010\u0007\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\"\u0010\f\u001a\u00020\u00058\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\f\u0010\u0007\u001a\u0004\b\r\u0010\t\"\u0004\b\u000e\u0010\u000bR\"\u0010\u000f\u001a\u00020\u00058\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u000f\u0010\u0007\u001a\u0004\b\u0010\u0010\t\"\u0004\b\u0011\u0010\u000bR\"\u0010\u0012\u001a\u00020\u00058\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0012\u0010\u0007\u001a\u0004\b\u0013\u0010\t\"\u0004\b\u0014\u0010\u000bR\"\u0010\u0015\u001a\u00020\u00058\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0015\u0010\u0007\u001a\u0004\b\u0016\u0010\t\"\u0004\b\u0017\u0010\u000bR\"\u0010\u0018\u001a\u00020\u00058\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0018\u0010\u0007\u001a\u0004\b\u0019\u0010\t\"\u0004\b\u001a\u0010\u000bR\"\u0010\u001b\u001a\u00020\u00058\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u001b\u0010\u0007\u001a\u0004\b\u001c\u0010\t\"\u0004\b\u001d\u0010\u000bR\"\u0010\u001e\u001a\u00020\u00058\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u001e\u0010\u0007\u001a\u0004\b\u001f\u0010\t\"\u0004\b \u0010\u000b¨\u0006#"}, d2 = {"Lcom/loostone/libtuning/data/config/vendor/EnvironmentalSoundItem;", "", "", "toString", "()Ljava/lang/String;", "", "aef2ReverbVolume", "I", "getAef2ReverbVolume", "()I", "setAef2ReverbVolume", "(I)V", "aef2ReverbMode", "getAef2ReverbMode", "setAef2ReverbMode", "aef2EchoGain", "getAef2EchoGain", "setAef2EchoGain", "aef2ReverbTime", "getAef2ReverbTime", "setAef2ReverbTime", "aef2EchoMode", "getAef2EchoMode", "setAef2EchoMode", "aef2EchoFeedback", "getAef2EchoFeedback", "setAef2EchoFeedback", "aef2EchoTime", "getAef2EchoTime", "setAef2EchoTime", SpeechConstants.KEY_COMMAND_TYPE, "getType", "setType", "<init>", "()V", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public final class EnvironmentalSoundItem {
    private int aef2EchoFeedback;
    private int aef2EchoGain;
    private int aef2EchoMode = 2;
    private int aef2EchoTime;
    private int aef2ReverbMode;
    private int aef2ReverbTime;
    private int aef2ReverbVolume;
    private int type;

    public final int getAef2EchoFeedback() {
        return this.aef2EchoFeedback;
    }

    public final int getAef2EchoGain() {
        return this.aef2EchoGain;
    }

    public final int getAef2EchoMode() {
        return this.aef2EchoMode;
    }

    public final int getAef2EchoTime() {
        return this.aef2EchoTime;
    }

    public final int getAef2ReverbMode() {
        return this.aef2ReverbMode;
    }

    public final int getAef2ReverbTime() {
        return this.aef2ReverbTime;
    }

    public final int getAef2ReverbVolume() {
        return this.aef2ReverbVolume;
    }

    public final int getType() {
        return this.type;
    }

    public final void setAef2EchoFeedback(int i) {
        this.aef2EchoFeedback = i;
    }

    public final void setAef2EchoGain(int i) {
        this.aef2EchoGain = i;
    }

    public final void setAef2EchoMode(int i) {
        this.aef2EchoMode = i;
    }

    public final void setAef2EchoTime(int i) {
        this.aef2EchoTime = i;
    }

    public final void setAef2ReverbMode(int i) {
        this.aef2ReverbMode = i;
    }

    public final void setAef2ReverbTime(int i) {
        this.aef2ReverbTime = i;
    }

    public final void setAef2ReverbVolume(int i) {
        this.aef2ReverbVolume = i;
    }

    public final void setType(int i) {
        this.type = i;
    }

    @NotNull
    public String toString() {
        return "EnvironmentalSoundItem(type=" + this.type + ", aef2ReverbMode=" + this.aef2ReverbMode + ", aef2ReverbVolume=" + this.aef2ReverbVolume + ", aef2ReverbTime=" + this.aef2ReverbTime + ", aef2EchoMode=" + this.aef2EchoMode + ", aef2EchoGain=" + this.aef2EchoGain + ", aef2EchoTime=" + this.aef2EchoTime + ", aef2EchoFeedback=" + this.aef2EchoFeedback + ')';
    }
}
