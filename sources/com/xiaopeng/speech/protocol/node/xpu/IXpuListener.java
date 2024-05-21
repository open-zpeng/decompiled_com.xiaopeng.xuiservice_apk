package com.xiaopeng.speech.protocol.node.xpu;

import com.xiaopeng.speech.INodeListener;
/* loaded from: classes.dex */
public interface IXpuListener extends INodeListener {
    default void laneChange(int direction) {
    }
}
