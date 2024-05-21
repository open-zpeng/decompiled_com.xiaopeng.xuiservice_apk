package com.xiaopeng.speech.protocol.node.meter;

import com.xiaopeng.speech.INodeListener;
/* loaded from: classes.dex */
public interface MeterListener extends INodeListener {
    default void setLeftCard(int index) {
    }

    default void setRightCard(int index) {
    }

    default void onDashboardLightsStatus() {
    }
}
