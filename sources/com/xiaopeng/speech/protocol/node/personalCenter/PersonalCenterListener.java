package com.xiaopeng.speech.protocol.node.personalCenter;

import com.xiaopeng.speech.INodeListener;
/* loaded from: classes.dex */
public interface PersonalCenterListener extends INodeListener {
    default void onControlProfileHabitSelect(int number) {
    }

    default void onControlProfileHabitSelectNext() {
    }
}
