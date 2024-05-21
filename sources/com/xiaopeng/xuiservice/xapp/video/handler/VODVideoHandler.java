package com.xiaopeng.xuiservice.xapp.video.handler;

import android.app.ActivityThread;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.mediacenter.Constants;
import com.xiaopeng.xuiservice.opensdk.manager.VodManagerServer;
import com.xiaopeng.xuiservice.xapp.XAppManagerService;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import com.xiaopeng.xuiservice.xapp.video.Callback;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.videoio.Videoio;
/* loaded from: classes5.dex */
public class VODVideoHandler implements IVideoHandler, VodManagerServer.IVodCommandCallback {
    private static final String TAG = "VODVideoHandler";
    private HashMap<String, Callback> mCallbackHashMap;
    private String mId;
    private String mPackageName;
    private VodManagerServer mVodManagerServer;

    public VODVideoHandler(String id, String pkgName) {
        this.mId = id;
        this.mPackageName = pkgName;
        init();
    }

    private void init() {
        this.mCallbackHashMap = new HashMap<>();
        this.mVodManagerServer = VodManagerServer.getInstance();
    }

    public void setCallback(String eventId, Callback callback) {
        LogUtil.d(TAG, "setCallback packageName:" + this.mPackageName + " &eventId:" + eventId);
        this.mCallbackHashMap.put(eventId, callback);
        registerCallback();
    }

    private void registerCallback() {
        this.mVodManagerServer.setVodCommandCallback(this);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void play(int displayId, String command, Callback callback) {
        setCallback("vodSearch", callback);
        try {
            JSONObject object = new JSONObject(command);
            JSONObject data = object.getJSONObject("data");
            String keyword = data.optString(SpeechConstants.KEY_COMMAND_DATA_KEYWORD, "");
            int code = this.mVodManagerServer.onSearch(this.mId, keyword);
            if (code == -300 || !XAppManagerService.getInstance().getTopPackage(displayId).equals(this.mPackageName)) {
                LogUtil.w(TAG, "client not register, launch with keyword");
                launchApp(keyword, displayId);
                callback.onResult(0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void play(int displayId, String programName, String artistName, Callback callback) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("programName", programName);
            jsonObject.put(Constants.LyricRequest.KEY_QUERY_PARAMS_ARTIST, artistName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setCallback("vodSearchAndPlay", callback);
        this.mVodManagerServer.onSearchAndPlay(this.mId, jsonObject.toString());
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public String getPackageName() {
        return this.mPackageName;
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void prev(Callback callback) {
        setCallback("vodPlayPrevious", callback);
        this.mVodManagerServer.onPlayPrevious(this.mId);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void next(Callback callback) {
        LogUtil.d(TAG, "next packageName:" + this.mPackageName);
        setCallback("vodPlayNext", callback);
        this.mVodManagerServer.onPlayNext(this.mId);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void set(int value, Callback callback) {
        setCallback("vodPlayEpisode", callback);
        this.mVodManagerServer.onPlayEpisode(this.mId, String.valueOf(value));
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void forward(Callback callback) {
        setCallback("vodPlayFF", callback);
        this.mVodManagerServer.onPlayFastForward(this.mId, String.valueOf(15000));
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void backward(Callback callback) {
        setCallback("vodPlayFB", callback);
        this.mVodManagerServer.onPlayFastBackward(this.mId, String.valueOf(15000));
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void setTime(double time, Callback callback) {
        setCallback("vodPlaySeek", callback);
        int timeValue = (int) (1000.0d * time);
        this.mVodManagerServer.onPlaySeekTo(this.mId, String.valueOf(timeValue));
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void pause(Callback callback) {
        setCallback("vodPlayPause", callback);
        this.mVodManagerServer.onPlayPause(this.mId);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void resume(Callback callback) {
        setCallback("vodPlayPlay", callback);
        this.mVodManagerServer.onPlayPlay(this.mId);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void speedUp(Callback callback) {
        setCallback("vodPlaySpeed", callback);
        this.mVodManagerServer.onPlaySetSpeed(this.mId, "UP");
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void speedDown(Callback callback) {
        setCallback("vodPlaySpeed", callback);
        this.mVodManagerServer.onPlaySetSpeed(this.mId, "DOWN");
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void speedSet(double speed, Callback callback) {
        setCallback("vodPlaySpeed", callback);
        this.mVodManagerServer.onPlaySetSpeed(this.mId, String.valueOf(speed));
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void end(Callback callback) {
        setCallback("vodPlayExit", callback);
        this.mVodManagerServer.onPlayExit(this.mId);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void clarityUP(Callback callback) {
        setCallback("vodPlayResolution", callback);
        this.mVodManagerServer.onPlaySetResolution(this.mId, "UP");
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void clarityDown(Callback callback) {
        setCallback("vodPlayResolution", callback);
        this.mVodManagerServer.onPlaySetResolution(this.mId, "DOWN");
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void claritySet(String value, Callback callback) {
        setCallback("vodPlayResolution", callback);
        this.mVodManagerServer.onPlaySetResolution(this.mId, value);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void collect(Callback callback) {
        setCallback("vodPlayAddFavor", callback);
        this.mVodManagerServer.onPlayAddFavor(this.mId, true);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void collectCancel(Callback callback) {
        setCallback("vodPlayAddFavor", callback);
        this.mVodManagerServer.onPlayAddFavor(this.mId, false);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void skipBegin(Callback callback) {
        setCallback("vodPlaySkipHead", callback);
        this.mVodManagerServer.onPlaySkipVideoHead(this.mId);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void skipEnd(Callback callback) {
        setCallback("vodPlaySkipTail", callback);
        this.mVodManagerServer.onPlaySkipVideoTail(this.mId);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void setMediaPlayMode(Callback callback) {
        setCallback("vodAudioMode", callback);
        this.mVodManagerServer.onPlayAudioMode(this.mId);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void setVideoPlayMode(Callback callback) {
        setCallback("vodVideoMode", callback);
        this.mVodManagerServer.onPlayVideoMode(this.mId);
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.VodManagerServer.IVodCommandCallback
    public int onVodCommand(String id, String param, String[] result) {
        LogUtil.d(TAG, "onVodCommand id:" + id + " &param:" + param);
        if (id.equals(this.mId) && !TextUtils.isEmpty(param)) {
            try {
                JSONObject object = new JSONObject(param);
                JSONObject opResult = object.optJSONObject("opResult");
                String cmd = opResult.optString("opName", "");
                int code = opResult.optInt("opCode", -1);
                if (!TextUtils.isEmpty(cmd)) {
                    Callback callback = this.mCallbackHashMap.get(cmd);
                    int uniCode = getCode(code);
                    if (callback != null) {
                        LogUtil.d(TAG, "onVodCommand callback uniCode:" + uniCode);
                        callback.onResult(uniCode);
                        return 0;
                    }
                    return 0;
                }
                return 0;
            } catch (JSONException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block
        	at jadx.core.dex.visitors.regions.RegionMaker.processSwitch(RegionMaker.java:817)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:160)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:94)
        	at jadx.core.dex.visitors.regions.RegionMaker.processSwitch(RegionMaker.java:856)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:160)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:94)
        	at jadx.core.dex.visitors.regions.RegionMaker.processSwitch(RegionMaker.java:856)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:160)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:94)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:730)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:155)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:94)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:52)
        */
    private int getCode(int r3) {
        /*
            r2 = this;
            r0 = 0
            r1 = -320(0xfffffffffffffec0, float:NaN)
            if (r3 == r1) goto L15
            switch(r3) {
                case -333: goto L13;
                case -332: goto L13;
                case -331: goto L13;
                case -330: goto L13;
                default: goto L8;
            }
        L8:
            switch(r3) {
                case -309: goto L15;
                case -308: goto L15;
                case -307: goto L11;
                case -306: goto Lf;
                default: goto Lb;
            }
        Lb:
            switch(r3) {
                case -303: goto L11;
                case -302: goto L11;
                case -301: goto L11;
                default: goto Le;
            }
        Le:
            goto L17
        Lf:
            r0 = 5
            goto L17
        L11:
            r0 = 3
            goto L17
        L13:
            r0 = 4
            goto L17
        L15:
            r0 = 2
        L17:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xuiservice.xapp.video.handler.VODVideoHandler.getCode(int):int");
    }

    private void launchApp(String keyword, int displayId) {
        Intent intent = new Intent();
        Uri uri = new Uri.Builder().scheme("youku").authority("soku").appendPath("searchresult").appendQueryParameter("text", keyword).build();
        LogUtil.d(TAG, "launchApp with uri:" + uri.toString());
        intent.setData(uri);
        intent.putExtra("resizeable", false);
        intent.addFlags(Videoio.CAP_INTELPERC_IMAGE_GENERATOR);
        intent.setScreenId(displayId);
        ActivityThread.currentActivityThread().getApplication().startActivity(intent);
    }
}
