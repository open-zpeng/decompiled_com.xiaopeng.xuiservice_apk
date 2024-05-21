package com.xiaopeng.xuiservice.mediacenter.controller;

import com.xiaopeng.speech.protocol.event.OOBEEvent;
import com.xiaopeng.xuimanager.mediacenter.MediaInfo;
import com.xiaopeng.xuimanager.mediacenter.lyric.LyricInfo;
import com.xiaopeng.xuiservice.mediacenter.controller.info.InfoUtil;
/* loaded from: classes5.dex */
public class DefaultMediaController extends IPCMediaController {
    private static final String TAG = "DefaultMediaController";
    private MediaInfo mDefaultMediaInfo;
    private long[] mDefaultPosition;

    public DefaultMediaController() {
        init();
    }

    private void init() {
        this.mDefaultMediaInfo = InfoUtil.getDefaultMediaInfo();
        this.mDefaultMediaInfo.putString("isDefault", OOBEEvent.STRING_TRUE);
        this.mDefaultPosition = new long[]{0, 0};
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.IPCMediaController, com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public MediaInfo getMediaInfo() {
        return this.mDefaultMediaInfo;
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.IPCMediaController, com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public LyricInfo getLyricInfo() {
        return new LyricInfo();
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.IPCMediaController, com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public int getPlayStatus() {
        return 2;
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.IPCMediaController, com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public long[] getPositions() {
        return this.mDefaultPosition;
    }
}
