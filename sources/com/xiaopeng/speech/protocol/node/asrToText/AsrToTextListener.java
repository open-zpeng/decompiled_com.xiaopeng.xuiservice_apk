package com.xiaopeng.speech.protocol.node.asrToText;

import com.xiaopeng.speech.INodeListener;
/* loaded from: classes.dex */
public interface AsrToTextListener extends INodeListener {
    void onResult(String str);
}
