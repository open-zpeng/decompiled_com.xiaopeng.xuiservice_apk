package com.xiaopeng.xuiservice.mediacenter.controller;

import android.media.session.MediaController;
import android.media.session.PlaybackState;
import android.os.Bundle;
import com.xiaopeng.xuimanager.mediacenter.MediaInfo;
import com.xiaopeng.xuimanager.mediacenter.lyric.LyricInfo;
/* loaded from: classes5.dex */
public interface IMediaController {
    PlaybackState getCurrentPlayState();

    LyricInfo getLyricInfo();

    MediaInfo getMediaInfo();

    int getMediaStyle();

    int getPlayStatus();

    long[] getPositions();

    boolean playbackControl(int i, int i2, int i3);

    boolean sendCustomAction(int i, Bundle bundle);

    void setFavorite(boolean z, String str);

    void switchSource(int i);

    void updateMediaController(MediaController mediaController);
}
