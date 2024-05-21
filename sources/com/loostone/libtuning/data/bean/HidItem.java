package com.loostone.libtuning.data.bean;

import kotlin.Metadata;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0002\b\u0012\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\t\u001a\u00020\u0002\u0012\u0006\u0010\u000f\u001a\u00020\u0002¢\u0006\u0004\b\u0012\u0010\u0013R\"\u0010\u0003\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\"\u0010\t\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\t\u0010\u0004\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR\"\u0010\f\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\f\u0010\u0004\u001a\u0004\b\r\u0010\u0006\"\u0004\b\u000e\u0010\bR\"\u0010\u000f\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u000f\u0010\u0004\u001a\u0004\b\u0010\u0010\u0006\"\u0004\b\u0011\u0010\b¨\u0006\u0014"}, d2 = {"Lcom/loostone/libtuning/data/bean/HidItem;", "", "", "micIndex", "I", "getMicIndex", "()I", "setMicIndex", "(I)V", "hidCode", "getHidCode", "setHidCode", "lastState", "getLastState", "setLastState", "keyCode", "getKeyCode", "setKeyCode", "<init>", "(III)V", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public final class HidItem {
    private int hidCode;
    private int keyCode;
    private int lastState = -1;
    private int micIndex;

    public HidItem(int i, int i2, int i3) {
        this.micIndex = i;
        this.hidCode = i2;
        this.keyCode = i3;
    }

    public final int getHidCode() {
        return this.hidCode;
    }

    public final int getKeyCode() {
        return this.keyCode;
    }

    public final int getLastState() {
        return this.lastState;
    }

    public final int getMicIndex() {
        return this.micIndex;
    }

    public final void setHidCode(int i) {
        this.hidCode = i;
    }

    public final void setKeyCode(int i) {
        this.keyCode = i;
    }

    public final void setLastState(int i) {
        this.lastState = i;
    }

    public final void setMicIndex(int i) {
        this.micIndex = i;
    }
}
