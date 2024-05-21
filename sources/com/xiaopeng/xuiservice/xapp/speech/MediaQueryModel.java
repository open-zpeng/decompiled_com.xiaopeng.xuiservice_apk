package com.xiaopeng.xuiservice.xapp.speech;

import com.xiaopeng.app.xpPackageInfo;
import com.xiaopeng.speech.protocol.SpeechModel;
import com.xiaopeng.speech.protocol.query.media.IMediaVideoQueryCaller;
import com.xiaopeng.speech.protocol.query.media.MediaVideoQuery;
import com.xiaopeng.speech.speechwidget.SpeechWidget;
import com.xiaopeng.xuimanager.mediacenter.MediaInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService;
import com.xiaopeng.xuiservice.mediacenter.controller.info.InfoUtil;
import com.xiaopeng.xuiservice.utils.PassengerBluetoothManager;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.xapp.XAppManagerService;
import com.xiaopeng.xuiservice.xapp.video.VideoManager;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class MediaQueryModel extends SpeechModel implements IMediaVideoQueryCaller {
    private static final String TAG = "MediaQueryModel";

    public void subscribe() {
        LogUtil.d(TAG, "subscribe node and query");
        subscribe(MediaVideoQuery.class, this);
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaVideoQueryCaller
    public String getMediaVideoInfo() {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject primaryObject = getMediaVideoInfo(0);
            jsonObject.put("homeScreenInfo", primaryObject);
            if (SharedDisplayManager.hasSharedDisplayFeature()) {
                JSONObject secondObject = getMediaVideoInfo(1);
                jsonObject.put("secondaryScreenInfo", secondObject);
                boolean isPassengerBtConnected = PassengerBluetoothManager.getInstance().isDeviceConnected();
                JSONObject btObject = new JSONObject();
                btObject.put("isSecondaryConnected", isPassengerBtConnected);
                jsonObject.put("bluetoothInfo", btObject);
            }
        } catch (JSONException ex) {
            LogUtil.w(TAG, "getMediaVideoInfo ex:" + ex.getMessage());
        }
        LogUtil.d(TAG, "getMediaVideoInfo info:" + jsonObject.toString());
        return jsonObject.toString();
    }

    private JSONObject getMediaVideoInfo(int screenId) {
        boolean isVideo;
        JSONObject jsonObject = new JSONObject();
        try {
            MediaInfo mediaInfo = MediaCenterHalService.getInstance().getCurrentMediaInfo(screenId);
            int status = MediaCenterHalService.getInstance().getCurrentPlayStatus(screenId);
            if (mediaInfo != null) {
                JSONObject mediaInfoObject = new JSONObject();
                mediaInfoObject.put(SpeechWidget.WIDGET_TITLE, mediaInfo.getTitle());
                mediaInfoObject.put(SpeechConstants.KEY_ARTIST, mediaInfo.getArtist());
                mediaInfoObject.put("isXpMusic", mediaInfo.isXpMusic());
                int i = 1;
                boolean isPlaying = status == 0;
                mediaInfoObject.put("isPlaying", isPlaying);
                mediaInfoObject.put("pkgName", mediaInfo.getPackageName());
                mediaInfoObject.put("isDefault", InfoUtil.isDefaultStatus(mediaInfo));
                int appType = XAppManagerService.getInstance().getAppType(mediaInfo.getPackageName());
                if (isPlaying) {
                    isVideo = appType == 2;
                } else {
                    xpPackageInfo info = XAppManagerService.getInstance().getXpPackageInfo(screenId);
                    if (info != null && info.appType == 2) {
                        mediaInfoObject.put("pkgName", info.packageName);
                        mediaInfoObject.put("isDefault", false);
                        isVideo = true;
                    } else {
                        isVideo = appType == 2;
                    }
                }
                if (isVideo) {
                    i = 2;
                }
                mediaInfoObject.put(SpeechConstants.KEY_COMMAND_TYPE, i);
                jsonObject.put("mediaInfo", mediaInfoObject);
            }
        } catch (JSONException ex) {
            LogUtil.w(TAG, "getMediaVideoInfo exception:" + ex.getMessage());
        }
        return jsonObject;
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaVideoQueryCaller
    public String getDemandApp(String data) {
        int displayId = SpeechUtils.getTargetDisplayId(data);
        return VideoManager.getInstance().getTargetApp(displayId);
    }
}
