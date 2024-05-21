package com.loostone.libtuning.channel.skyworth.bean;

import kotlin.Metadata;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0002\b\t\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\t\u0010\nR\"\u0010\u0003\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b¨\u0006\u000b"}, d2 = {"Lcom/loostone/libtuning/channel/skyworth/bean/SkySystemProperty;", "", "", "lastState", "I", "getLastState", "()I", "setLastState", "(I)V", "<init>", "()V", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public final class SkySystemProperty {
    private int lastState = -1;

    public final int getLastState() {
        return this.lastState;
    }

    public final void setLastState(int i) {
        this.lastState = i;
    }
}
