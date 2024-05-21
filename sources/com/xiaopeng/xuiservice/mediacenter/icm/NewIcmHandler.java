package com.xiaopeng.xuiservice.mediacenter.icm;

import android.app.ActivityThread;
import android.text.format.DateFormat;
import com.google.gson.Gson;
import com.xiaopeng.xuimanager.mediacenter.MediaInfo;
import com.xiaopeng.xuiservice.mediacenter.bean.MusicBean;
import com.xiaopeng.xuiservice.mediacenter.bean.MusicIcmBean;
import java.util.Date;
/* loaded from: classes5.dex */
public class NewIcmHandler extends ICMHandler {
    private static final String TAG = "NewIcmHandler";
    private Gson mGson;
    private MusicBean mMusicBean;
    private MusicIcmBean mMusicIcmBean;

    @Override // com.xiaopeng.xuiservice.mediacenter.icm.ICMHandler
    public void sendIcmMediaInfoData(MediaInfo mediaInfo, int status, long[] position, boolean withImage) {
        boolean isXp = mediaInfo.isXpMusic();
        if (this.mMusicBean == null) {
            initMusicBean();
        }
        this.mMusicBean.setSong(mediaInfo.getTitle());
        this.mMusicBean.setArtist(mediaInfo.getArtist());
        this.mMusicBean.setTotal(isXp ? String.valueOf(position[1]) : "0");
        this.mMusicBean.setProgress(isXp ? String.valueOf(position[0]) : "0");
        this.mMusicBean.setStatus(status == 0 ? "play" : "pause");
        Date date = new Date();
        String timeString = DateFormat.getTimeFormat(ActivityThread.currentActivityThread().getApplication()).format(date);
        this.mMusicIcmBean.setReporttime(timeString);
        this.mMusicIcmBean.setMsgtype(String.valueOf(mediaInfo.getSource()));
        String beanJson = this.mGson.toJson(this.mMusicIcmBean);
        printMediaDataLog("NewIcmHandler_sendIcmMediaInfoData", beanJson, withImage, mediaInfo.getAlbumBitmap());
        setMusicInfo(beanJson.getBytes(), withImage ? getBitmapByte(mediaInfo.getAlbumBitmap()) : new byte[0]);
    }

    private void initMusicBean() {
        this.mMusicBean = new MusicBean();
        this.mMusicIcmBean = new MusicIcmBean();
        this.mMusicIcmBean.setMusic(this.mMusicBean);
        this.mGson = new Gson();
    }
}
