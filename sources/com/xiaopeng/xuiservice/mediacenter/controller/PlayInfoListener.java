package com.xiaopeng.xuiservice.mediacenter.controller;

import com.xiaopeng.xuimanager.mediacenter.MediaInfo;
import com.xiaopeng.xuimanager.mediacenter.lyric.LyricInfo;
/* loaded from: classes5.dex */
public interface PlayInfoListener {
    void onLyricUpdated(LyricInfo lyricInfo);

    void onMediaInfoChanged(MediaInfo mediaInfo);

    void onPlayStatusChanged(int i);

    void onPositionChanged(long j, long j2);
}
