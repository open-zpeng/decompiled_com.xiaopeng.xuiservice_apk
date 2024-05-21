package com.xiaopeng.xuiservice.musicrecognize;

import android.app.ActivityThread;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Process;
import androidx.core.app.NotificationCompat;
import com.acrcloud.rec.ACRCloudClient;
import com.acrcloud.rec.ACRCloudConfig;
import com.acrcloud.rec.ACRCloudResult;
import com.acrcloud.rec.IACRCloudListener;
import com.xiaopeng.speech.speechwidget.SpeechWidget;
import com.xiaopeng.xuimanager.musicrecognize.MusicRecognizeEvent;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.musicrecognize.MusicRecognizeEngine;
import com.xiaopeng.xuiservice.smart.action.Actions;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import java.io.File;
import org.json.JSONArray;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class MusicRecognizeEngineImpl implements MusicRecognizeEngine {
    private static final int AUTO_CANCEL_DELAY = 13000;
    public static final boolean DBG = true;
    private static final int PRE_RECORD_MILLS = 3000;
    private static final String RECORD_PID = "pid";
    private static final String RECORD_STATE = "state";
    private static final int RECORD_STATE_START = 1;
    private static final int RECORD_STATE_STOP = 0;
    private static final String TAG = "MusicRecognizeEngineImpl";
    private ACRCloudConfig mACRCloudConfig;
    private ACRCloudClient mAcrCloudClient;
    private Runnable mAutoCancelRunnable;
    private MusicRecognizeEngine.Callback mCallback;
    private float mMinScore;
    private int mMode;
    private boolean mNeedCallbackFail;
    private boolean mStopAndRecognize;
    private String path;

    public static MusicRecognizeEngineImpl getInstance() {
        return InstanceHolder.sService;
    }

    @Override // com.xiaopeng.xuiservice.musicrecognize.MusicRecognizeEngine
    public float getMinScore() {
        return this.mMinScore;
    }

    @Override // com.xiaopeng.xuiservice.musicrecognize.MusicRecognizeEngine
    public void setMinScore(float minScore) {
        this.mMinScore = minScore;
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final MusicRecognizeEngineImpl sService = new MusicRecognizeEngineImpl(ActivityThread.currentActivityThread().getApplication(), 1);

        private InstanceHolder() {
        }
    }

    private MusicRecognizeEngineImpl(Context context, int mode) {
        this.path = "";
        this.mMinScore = 0.7f;
        this.mNeedCallbackFail = true;
        this.mStopAndRecognize = false;
        this.mAutoCancelRunnable = new Runnable() { // from class: com.xiaopeng.xuiservice.musicrecognize.MusicRecognizeEngineImpl.1
            @Override // java.lang.Runnable
            public void run() {
                if (MusicRecognizeEngineImpl.this.mNeedCallbackFail) {
                    MusicRecognizeEngineImpl.this.mNeedCallbackFail = false;
                    if (MusicRecognizeEngineImpl.this.mCallback != null) {
                        MusicRecognizeEngineImpl.this.mCallback.onFail("TimeOut");
                    }
                }
            }
        };
        this.mACRCloudConfig = new ACRCloudConfig();
        ACRCloudConfig aCRCloudConfig = this.mACRCloudConfig;
        aCRCloudConfig.context = context;
        aCRCloudConfig.host = "identify-cn-north-1.acrcloud.com";
        this.path = Environment.getExternalStorageDirectory().toString() + "/acrcloud";
        LogUtil.e(TAG, this.path);
        File file = new File(this.path);
        if (!file.exists()) {
            file.mkdirs();
        }
        setMode(mode);
        this.mACRCloudConfig.recorderConfig.rate = 8000;
        this.mACRCloudConfig.recorderConfig.channels = 1;
        this.mACRCloudConfig.protocol = ACRCloudConfig.NetworkProtocol.HTTPS;
        this.mACRCloudConfig.recorderConfig.isVolumeCallback = true;
        this.mACRCloudConfig.acrcloudListener = new IACRCloudListener() { // from class: com.xiaopeng.xuiservice.musicrecognize.MusicRecognizeEngineImpl.2
            @Override // com.acrcloud.rec.IACRCloudListener
            public void onResult(ACRCloudResult acrCloudResult) {
                String modeKey;
                float k;
                String name = "";
                String result = acrCloudResult.getResult();
                LogUtil.d(MusicRecognizeEngineImpl.TAG, result);
                try {
                    JSONObject root = new JSONObject(result);
                    JSONObject statusObject = root.getJSONObject("status");
                    statusObject.getString(NotificationCompat.CATEGORY_MESSAGE);
                    String code = statusObject.getString("code");
                    if ("0".equals(code)) {
                        int i = MusicRecognizeEngineImpl.this.mMode;
                        if (i == 1) {
                            modeKey = "music";
                            k = 0.01f;
                        } else if (i == 2) {
                            modeKey = "humming";
                            k = 1.0f;
                        } else {
                            modeKey = "humming";
                            k = 1.0f;
                        }
                        JSONArray resultArray = root.getJSONObject("metadata").getJSONArray(modeKey);
                        float maxScore = 0.0f;
                        String title = "";
                        String albumName = "";
                        String albumLink = "";
                        int var15 = resultArray.length();
                        String score = "0";
                        int i2 = 0;
                        while (true) {
                            JSONObject root2 = root;
                            int var152 = var15;
                            if (i2 >= var152) {
                                break;
                            }
                            JSONObject resultObject = resultArray.getJSONObject(i2);
                            var15 = var152;
                            JSONObject statusObject2 = statusObject;
                            String value = resultObject.getString("score");
                            float newScore = Float.parseFloat(value);
                            float oldScore = Float.parseFloat(score);
                            if (newScore >= oldScore) {
                                score = value;
                                String title2 = resultObject.getString(SpeechWidget.WIDGET_TITLE);
                                JSONObject albumObject = resultObject.getJSONObject("album");
                                albumName = albumObject.getString("name");
                                String albumLink2 = albumObject.getString(Actions.ACTION_IMAGE);
                                albumLink = albumLink2;
                                name = resultObject.getJSONArray("artists").getJSONObject(0).getString("name");
                                maxScore = newScore * k;
                                title = title2;
                            }
                            i2++;
                            root = root2;
                            statusObject = statusObject2;
                        }
                        if (MusicRecognizeEngineImpl.this.scoreFilter(maxScore)) {
                            MusicRecognizeEngineImpl.this.mNeedCallbackFail = false;
                            MusicRecognizeEngineImpl.this.mCallback.onSuccess(new MusicRecognizeEvent(title, albumName, albumLink, name, String.valueOf(maxScore)));
                            MusicRecognizeEngineImpl.this.stop();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.w(MusicRecognizeEngineImpl.TAG, result);
                }
            }

            @Override // com.acrcloud.rec.IACRCloudListener
            public void onVolumeChanged(double v) {
            }
        };
        this.mAcrCloudClient = new ACRCloudClient();
        this.mAcrCloudClient.initWithConfig(this.mACRCloudConfig);
    }

    @Override // com.xiaopeng.xuiservice.musicrecognize.MusicRecognizeEngine
    public void setMode(int mode) {
        if (mode == 1) {
            ACRCloudConfig aCRCloudConfig = this.mACRCloudConfig;
            aCRCloudConfig.accessKey = "76e3a94d5c417e0161a2663fd6965e1d";
            aCRCloudConfig.accessSecret = "EPVCfI2azIN2ZKuuORbObYnlNC3nnjEQksgzuVnG";
        } else if (mode == 2) {
            ACRCloudConfig aCRCloudConfig2 = this.mACRCloudConfig;
            aCRCloudConfig2.accessKey = "9c7ca9664857adce4463334879ca3b9a";
            aCRCloudConfig2.accessSecret = "0VhYPPtCyXjIui103GZxnY81HQSKkIAvZujCMoYU";
        } else {
            throw new RuntimeException("unknown mode, mode = " + mode);
        }
        this.mMode = mode;
    }

    @Override // com.xiaopeng.xuiservice.musicrecognize.MusicRecognizeEngine
    public int getMode() {
        return this.mMode;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean scoreFilter(float score) {
        return score >= this.mMinScore;
    }

    @Override // com.xiaopeng.xuiservice.musicrecognize.MusicRecognizeEngine
    public void start() {
        LogUtil.d(TAG, "start!");
        Intent intent = new Intent("com.xiaopeng.media.RECORDING_STATE_CHANGED");
        intent.putExtra(RECORD_PID, Process.myPid());
        intent.putExtra("state", 1);
        BroadcastManager.getInstance().sendBroadcast(intent);
        this.mAcrCloudClient.startRecognize();
    }

    @Override // com.xiaopeng.xuiservice.musicrecognize.MusicRecognizeEngine
    public void stop() {
        LogUtil.d(TAG, "stop!");
        Intent intent = new Intent("com.xiaopeng.media.RECORDING_STATE_CHANGED");
        intent.putExtra(RECORD_PID, Process.myPid());
        intent.putExtra("state", 0);
        BroadcastManager.getInstance().sendBroadcast(intent);
        this.mAcrCloudClient.stopRecordToRecognize();
    }

    @Override // com.xiaopeng.xuiservice.musicrecognize.MusicRecognizeEngine
    public void stopAndRecognize() {
        this.mAcrCloudClient.stopRecordToRecognize();
    }

    @Override // com.xiaopeng.xuiservice.musicrecognize.MusicRecognizeEngine
    public void setCallback(MusicRecognizeEngine.Callback callback) {
        this.mCallback = callback;
    }
}
