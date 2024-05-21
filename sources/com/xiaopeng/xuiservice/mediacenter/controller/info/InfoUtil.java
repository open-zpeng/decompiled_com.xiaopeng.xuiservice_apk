package com.xiaopeng.xuiservice.mediacenter.controller.info;

import android.app.ActivityThread;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import com.xiaopeng.xuimanager.mediacenter.MediaInfo;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.mediacenter.Constants;
import com.xiaopeng.xuiservice.xapp.XAppManagerService;
/* loaded from: classes5.dex */
public class InfoUtil {
    public static MediaInfo getDefaultMediaInfo() {
        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo.setTitle(ActivityThread.currentActivityThread().getApplication().getString(R.string.media_default_title));
        mediaInfo.setArtist(ActivityThread.currentActivityThread().getApplication().getString(R.string.media_default_sub_title));
        mediaInfo.setXpMusic(true);
        mediaInfo.setPackageName(Constants.PACKAGE_XP_MUSIC);
        mediaInfo.setSource(0);
        mediaInfo.setAlbumBitmap(BitmapFactory.decodeResource(ActivityThread.currentActivityThread().getApplication().getResources(), R.drawable.ic_default_music_album));
        return mediaInfo;
    }

    public static boolean isBluetoothMedia(MediaInfo mediaInfo) {
        return mediaInfo != null && mediaInfo.isXpMusic() && mediaInfo.getSource() == 3;
    }

    public static boolean isDefaultMedia(MediaInfo mediaInfo) {
        String isDefaultString = mediaInfo.getString("isDefault");
        if (!TextUtils.isEmpty(isDefaultString)) {
            return Boolean.parseBoolean(isDefaultString);
        }
        return false;
    }

    public static boolean isDefaultStatus(MediaInfo mediaInfo) {
        return isDefaultMedia(mediaInfo) || isDefaultContent(mediaInfo);
    }

    public static boolean isDefaultContent(MediaInfo mediaInfo) {
        String contentString = mediaInfo.getString("isDefaultContent");
        if (!TextUtils.isEmpty(contentString)) {
            return Boolean.parseBoolean(contentString);
        }
        return false;
    }

    public static String getMediaInfoMode(MediaInfo mediaInfo) {
        String playMode = mediaInfo.getString(Constants.KEY_PLAY_MODE);
        return playMode;
    }

    public static boolean isVideoPlayMode(MediaInfo mediaInfo) {
        if (mediaInfo != null) {
            String playMode = mediaInfo.getString(Constants.KEY_PLAY_MODE);
            return Constants.VALUE_PLAY_MODE_VIDEO.equals(playMode);
        }
        return false;
    }

    public static String getPlayMode(MediaInfo mediaInfo) {
        if (mediaInfo != null) {
            String playMode = mediaInfo.getString(Constants.KEY_PLAY_MODE);
            return playMode;
        }
        return "";
    }

    public static MediaInfo getDefaultMediaInfo(String pkgName) {
        if (!TextUtils.isEmpty(pkgName) && !Constants.PACKAGE_XP_MUSIC.equals(pkgName)) {
            MediaInfo mediaInfo = new MediaInfo();
            String appName = XAppManagerService.getInstance().getAppName(pkgName);
            mediaInfo.setTitle(appName);
            mediaInfo.setArtist(appName);
            mediaInfo.setXpMusic(false);
            mediaInfo.setPackageName(pkgName);
            mediaInfo.setSource(0);
            mediaInfo.setAlbumBitmap(IconLoader.getAppIconBitmap(pkgName));
            return mediaInfo;
        }
        return getDefaultMediaInfo();
    }
}
