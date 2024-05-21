package com.xiaopeng.xuiservice.smart.action.impl;

import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
/* loaded from: classes5.dex */
public class ActionAudio extends ActionMediaPlayer {
    @Override // com.xiaopeng.xuiservice.smart.action.impl.ActionMediaPlayer, com.xiaopeng.xuiservice.smart.action.ActionBase
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public ActionAudio(String file, int repeat, int audioFocus) {
        super(file, repeat, audioFocus);
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    protected void onStart() {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.action.impl.-$$Lambda$ActionAudio$mFnEDeUuiqEs3On1Z8phgsw6uJI
            @Override // java.lang.Runnable
            public final void run() {
                ActionAudio.this.startAudio();
            }
        });
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    protected void onStop() {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.action.impl.-$$Lambda$ActionAudio$GQ_qz7y3c2tIER5c59Rx4HIyL_c
            @Override // java.lang.Runnable
            public final void run() {
                ActionAudio.this.stopAudio();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startAudio() {
        if (initMediaPlayer()) {
            this.mediaPlayer.prepareAsync();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopAudio() {
        stopAndReleaseMediaPlayer();
    }
}
