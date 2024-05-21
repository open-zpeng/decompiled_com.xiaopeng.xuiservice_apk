package com.xiaopeng.xuiservice.mediacenter.icm;

import android.app.ActivityThread;
import android.text.format.DateFormat;
import androidx.core.app.NotificationCompat;
import com.google.gson.Gson;
import com.xiaopeng.xuimanager.mediacenter.MediaInfo;
import com.xiaopeng.xuiservice.capabilities.XpIcmAgent;
import com.xiaopeng.xuiservice.mediacenter.bean.MusicBean;
import com.xiaopeng.xuiservice.mediacenter.bean.MusicIcmBean;
import com.xiaopeng.xuiservice.mediacenter.bean.NetRadioBean;
import com.xiaopeng.xuiservice.mediacenter.bean.NetRadioIcmBean;
import com.xiaopeng.xuiservice.mediacenter.bean.RadioBean;
import com.xiaopeng.xuiservice.mediacenter.bean.RadioIcmBean;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class LegacyICMHandler extends ICMHandler {
    private static final int TYPE_BLUETOOTH = 2;
    private static final int TYPE_FM = 1;
    private static final int TYPE_MUSIC = 1;
    private static final int TYPE_RADIO = 1;
    private static final int TYPE_READING = 2;
    private static final int TYPE_USB = 3;
    private Gson mGson;
    private MusicBean mMusicBean;
    private MusicIcmBean mMusicIcmBean;
    private NetRadioBean mNetRadioBean;
    private NetRadioIcmBean mNetRadioIcmBean;
    private RadioBean mRadioBean;
    private RadioIcmBean mRadioIcmBean;

    public LegacyICMHandler() {
        initBeans();
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.icm.ICMHandler
    public void sendIcmMediaInfoData(MediaInfo mediaInfo, int status, long[] position, boolean withImage) {
        if (mediaInfo == null) {
            return;
        }
        boolean isXp = mediaInfo.isXpMusic();
        if (isXp && (mediaInfo.getSource() == 1 || mediaInfo.getSource() == 2)) {
            sendIcmNetRadioData(mediaInfo, status, position, withImage);
        } else {
            sendIcmMusicData(mediaInfo, isXp, status, position, withImage);
        }
    }

    private void initBeans() {
        this.mMusicBean = new MusicBean();
        this.mMusicIcmBean = new MusicIcmBean();
        this.mMusicIcmBean.setMusic(this.mMusicBean);
        this.mRadioBean = new RadioBean();
        this.mRadioIcmBean = new RadioIcmBean();
        this.mRadioIcmBean.setRadio(this.mRadioBean);
        this.mNetRadioBean = new NetRadioBean();
        this.mNetRadioIcmBean = new NetRadioIcmBean();
        this.mNetRadioIcmBean.setNetRadio(this.mNetRadioBean);
        this.mGson = new Gson();
    }

    private void sendIcmNetRadioData(MediaInfo info, int status, long[] position, boolean withImage) {
        String beanJson = "";
        if (info.getSource() == 1) {
            beanJson = getNetRadioBeanJson(info, status, position);
        } else if (info.getSource() == 2) {
            beanJson = getTalkingBookBeanJson(info, status, position);
        }
        printMediaDataLog("LegacyICMHandler_sendIcmNetRadioData", beanJson, withImage, info.getAlbumBitmap());
        setNetRadioInfo(beanJson.getBytes(), withImage ? getBitmapByte(info.getAlbumBitmap()) : new byte[0]);
    }

    private void sendIcmMusicData(MediaInfo info, boolean isXp, int status, long[] position, boolean withImage) {
        this.mMusicBean.setSong(info.getTitle());
        this.mMusicBean.setArtist(info.getArtist());
        this.mMusicBean.setTotal(isXp ? String.valueOf(position[1]) : "0");
        this.mMusicBean.setProgress(isXp ? String.valueOf(position[0]) : "0");
        this.mMusicBean.setStatus(status == 0 ? "play" : "pause");
        Date date = new Date();
        String timeString = DateFormat.getTimeFormat(ActivityThread.currentActivityThread().getApplication()).format(date);
        this.mMusicIcmBean.setReporttime(timeString);
        if (isXp && info.getSource() == 3) {
            this.mMusicIcmBean.setMsgtype(String.valueOf(2));
        } else {
            this.mMusicIcmBean.setMsgtype(String.valueOf(1));
        }
        String beanJson = this.mGson.toJson(this.mMusicIcmBean);
        setMusicInfo(beanJson.getBytes(), withImage ? getBitmapByte(info.getAlbumBitmap()) : new byte[0]);
        printMediaDataLog("LegacyICMHandler_sendIcmMusicData", beanJson, withImage, info.getAlbumBitmap());
    }

    private String getNetRadioBeanJson(MediaInfo info, int status, long[] position) {
        this.mNetRadioBean.setName(info.getTitle());
        this.mNetRadioBean.setFre(info.getArtist());
        this.mNetRadioBean.setTotal(String.valueOf(position[1]));
        this.mNetRadioBean.setProgress(String.valueOf(position[0]));
        this.mNetRadioBean.setStatus(status == 0 ? "play" : "pause");
        Date date = new Date();
        String timeString = DateFormat.getTimeFormat(ActivityThread.currentActivityThread().getApplication()).format(date);
        this.mNetRadioIcmBean.setReporttime(timeString);
        this.mNetRadioIcmBean.setMsgtype(String.valueOf(1));
        return this.mGson.toJson(this.mNetRadioIcmBean);
    }

    private String getTalkingBookBeanJson(MediaInfo info, int status, long[] position) {
        JSONObject talkingBookJsonObject = new JSONObject();
        JSONObject metaDataJsonObject = new JSONObject();
        try {
            talkingBookJsonObject.put("enable", "1");
            talkingBookJsonObject.put("msgtype", "2");
            metaDataJsonObject.put("fre", info.getArtist());
            metaDataJsonObject.put("name", info.getTitle());
            metaDataJsonObject.put("total", String.valueOf(position[1]));
            metaDataJsonObject.put(NotificationCompat.CATEGORY_PROGRESS, String.valueOf(position[0]));
            metaDataJsonObject.put("status", status == 0 ? "play" : "pause");
            talkingBookJsonObject.put("talking_book", metaDataJsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return talkingBookJsonObject.toString();
    }

    public void setNetRadioInfo(byte[] basic, byte[] image) {
        XpIcmAgent.getInstance(ActivityThread.currentActivityThread().getApplication()).setNetRadioInfo(basic, image);
    }
}
