package com.xiaopeng.xuiservice.mediacenter.controller;

import android.content.Intent;
import android.media.session.PlaybackState;
import android.os.Bundle;
import com.xiaopeng.xuimanager.mediacenter.MediaInfo;
import com.xiaopeng.xuimanager.mediacenter.lyric.LyricInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.mediacenter.Constants;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
/* loaded from: classes5.dex */
public class IPCMediaController extends BaseMediaController {
    private static final String TAG = "IPCMediaController";
    private XpMediaControllerManager mXpMediaControllerManager;

    public IPCMediaController() {
        XpMediaControllerManager xpMediaControllerManager = this.mXpMediaControllerManager;
        this.mXpMediaControllerManager = XpMediaControllerManager.getInstance();
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.BaseMediaController, com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public boolean playbackControl(int displayId, int cmd, int param) {
        boolean processed = super.playbackControl(displayId, cmd, param);
        if (processed) {
            return true;
        }
        LogUtil.d(TAG, "playbackControl cmd:" + cmd + " &params:" + param);
        if (XUIConfig.isXuiProcessHelpLaunch()) {
            sendPlaybackControlBroadcast(cmd, param);
            return true;
        }
        this.mXpMediaControllerManager.playbackControl(cmd, param);
        return true;
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.BaseMediaController, com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public void switchSource(int source) {
        LogUtil.d(TAG, "switchSource source:" + source);
        this.mXpMediaControllerManager.switchSource(source);
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.BaseMediaController, com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public void setFavorite(boolean favorite, String id) {
        LogUtil.d(TAG, "setFavorite favorite:" + favorite + " &id:" + id);
        this.mXpMediaControllerManager.setFavorite(favorite, id);
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public MediaInfo getMediaInfo() {
        return XpMediaControllerManager.getInstance().getMediaInfo();
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public LyricInfo getLyricInfo() {
        return XpMediaControllerManager.getInstance().getLyricInfo();
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public int getPlayStatus() {
        return this.mXpMediaControllerManager.getPlayStatus();
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public long[] getPositions() {
        return this.mXpMediaControllerManager.getPositions();
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public int getMediaStyle() {
        return getMediaInfo().getStyle();
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.BaseMediaController
    public String getTargetPackage() {
        return Constants.PACKAGE_XP_MUSIC;
    }

    private void sendPlaybackControlBroadcast(int cmd, int param) {
        LogUtil.i(TAG, "sendPlaybackControlBroadcast cmd:" + cmd);
        Intent intent = new Intent();
        intent.setAction("com.xiaopeng.xuiservice.playbackControl");
        intent.putExtra("playbackCmd", cmd);
        intent.putExtra("playbackParam", param);
        BroadcastManager.getInstance().sendBroadcast(intent);
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public boolean sendCustomAction(int cmd, Bundle extras) {
        if (cmd == 4) {
            long setTime = extras.getLong(Constants.KEY_LONG_VALUE, 0L);
            seekTo(setTime);
            return true;
        } else if (cmd == 9) {
            String mode = extras.getString(Constants.KEY_TEXT_VALUE, "");
            int value = 0;
            if ("single".equals(mode)) {
                value = 1;
            } else if ("order".equals(mode)) {
                value = 2;
            } else if ("random".equals(mode)) {
                value = 3;
            }
            this.mXpMediaControllerManager.playbackControl(cmd, value);
            return true;
        } else {
            return true;
        }
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public PlaybackState getCurrentPlayState() {
        return null;
    }
}
