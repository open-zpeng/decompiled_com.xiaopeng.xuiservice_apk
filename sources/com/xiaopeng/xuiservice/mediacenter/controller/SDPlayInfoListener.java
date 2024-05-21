package com.xiaopeng.xuiservice.mediacenter.controller;

import com.xiaopeng.xuimanager.mediacenter.MediaInfo;
import com.xiaopeng.xuimanager.mediacenter.lyric.LyricInfo;
/* loaded from: classes5.dex */
public interface SDPlayInfoListener {
    void onLyricUpdated(int i, LyricInfo lyricInfo);

    void onMediaInfoChanged(int i, MediaInfo mediaInfo);

    void onPlayStatusChanged(int i, int i2);

    void onPositionChanged(int i, long j, long j2);
}
