package com.xiaopeng.speech.protocol.node.wakeup;

import com.xiaopeng.speech.INodeListener;
/* loaded from: classes.dex */
public interface WakeupStatusListener extends INodeListener {
    void onWakeupStatusChanged(int i, int i2, String str);
}
