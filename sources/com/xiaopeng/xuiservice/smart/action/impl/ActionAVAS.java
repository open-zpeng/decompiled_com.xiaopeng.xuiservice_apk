package com.xiaopeng.xuiservice.smart.action.impl;
/* loaded from: classes5.dex */
public class ActionAVAS extends ActionAudio {
    public ActionAVAS(String file, int repeat) {
        super(file, repeat, 0);
    }

    @Override // com.xiaopeng.xuiservice.smart.action.impl.ActionMediaPlayer
    protected int audioSteamType() {
        return 11;
    }
}
