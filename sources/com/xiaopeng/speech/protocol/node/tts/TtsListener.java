package com.xiaopeng.speech.protocol.node.tts;

import com.xiaopeng.speech.INodeListener;
/* loaded from: classes.dex */
public interface TtsListener extends INodeListener {
    void ttsEnd(String str);

    void ttsStart(String str);

    default void onTtsTimbreSetting(int type) {
    }

    default void ttsEcho(TtsEchoValue data) {
    }
}
