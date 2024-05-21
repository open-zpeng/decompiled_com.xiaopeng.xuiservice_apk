package com.xiaopeng.xuiservice.utils;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TtsEngines;
import android.speech.tts.UtteranceProgressListener;
import android.text.TextUtils;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.util.Locale;
import java.util.UUID;
/* loaded from: classes5.dex */
public class XpSpeechTtsHelper {
    private static final boolean DBG = true;
    public static final String KEY_PARAM_PRIORITY = "priority";
    public static final String KEY_PARAM_STREAM = "streamType";
    public static final String KEY_PARAM_TTS_MODE = "ttsMode";
    private static final String TAG = "XpSpeechTtsHelper";
    private static final String TTS_ENGINE_MS = "com.xiaopeng.xpspeechservice.ms";
    public static final int TTS_EVENT_DONE = 1;
    public static final int TTS_EVENT_ERROR = 2;
    public static final int TTS_EVENT_START = 0;
    public static final int TTS_EVENT_STOP = 3;
    private static final String UUID_PREFIX = "xuiservice";
    private static int mStatus = -1;
    private static XpSpeechTtsHelper sHelper;
    private SpeechTtsEventListener mListener = null;
    private TextToSpeech mTextToSpeech;
    private TtsEngines mTtsEngines;

    /* loaded from: classes5.dex */
    public interface SpeechTtsEventListener {
        void onSpeechTtsEventCallBack(int i, String str);
    }

    private XpSpeechTtsHelper() {
    }

    public static XpSpeechTtsHelper instance() {
        if (sHelper == null) {
            synchronized (XpSpeechTtsHelper.class) {
                if (sHelper == null) {
                    sHelper = new XpSpeechTtsHelper();
                }
            }
        }
        return sHelper;
    }

    private String speak(String text, int priority, boolean isShutUp) {
        String id = UUID_PREFIX + UUID.randomUUID().toString() + System.currentTimeMillis();
        LogUtil.i(TAG, "speak start text:" + text + ", priority:" + priority + ", ttsId:" + id + ", mStatus: " + mStatus);
        if (this.mTextToSpeech == null || TextUtils.isEmpty(text) || mStatus != 0) {
            return null;
        }
        Bundle params = new Bundle();
        params.putInt("priority", priority);
        if (priority == 4) {
            params.putInt(KEY_PARAM_STREAM, 9);
        } else {
            params.putInt(KEY_PARAM_STREAM, 10);
        }
        int queue = !isShutUp ? 1 : 0;
        int res = this.mTextToSpeech.speak(text, queue, params, id);
        if (res == 0) {
            return id;
        }
        return null;
    }

    public String speak(String text, boolean isShutUp) {
        if (!TextUtils.isEmpty(text)) {
            String ttsId = speak(text, 1, isShutUp);
            LogUtil.d(TAG, "speak completed text:" + text + ",ttsId:" + ttsId);
            return ttsId;
        }
        return null;
    }

    public String speakByImportant(String text, boolean isShutUp) {
        String ttsId = speak(text, 2, isShutUp);
        LogUtil.d(TAG, "speakByImportant completed text:" + text + ",ttsId:" + ttsId);
        return ttsId;
    }

    public String speakByUrgent(String text, boolean isShutUp) {
        String ttsId = speak(text, 3, isShutUp);
        LogUtil.d(TAG, "speakByUrgent completed text:" + text + ",callback:,ttsId:" + ttsId + " isShutUp:" + isShutUp);
        return ttsId;
    }

    public String speakByInstant(String text, boolean isShutUp) {
        String ttsId = speak(text, 4, isShutUp);
        LogUtil.d(TAG, "speakByInstant completed text:" + text + ",callback:,ttsId:" + ttsId + " isShutUp:" + isShutUp);
        return ttsId;
    }

    public synchronized void registerListener(SpeechTtsEventListener listener) {
        if (listener != null) {
            this.mListener = listener;
        }
    }

    public boolean getInitStatus() {
        return mStatus == 0;
    }

    public void init(Context context) {
        this.mTextToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() { // from class: com.xiaopeng.xuiservice.utils.XpSpeechTtsHelper.1
            @Override // android.speech.tts.TextToSpeech.OnInitListener
            public void onInit(int status) {
                LogUtil.i(XpSpeechTtsHelper.TAG, "TextToSpeech onInit status = " + status);
                int unused = XpSpeechTtsHelper.mStatus = status;
                if (status == 0) {
                    int result = XpSpeechTtsHelper.this.mTextToSpeech.setLanguage(Locale.CHINESE);
                    LogUtil.i(XpSpeechTtsHelper.TAG, "setLanguage CHINESE " + result);
                    XpSpeechTtsHelper.this.mTextToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() { // from class: com.xiaopeng.xuiservice.utils.XpSpeechTtsHelper.1.1
                        @Override // android.speech.tts.UtteranceProgressListener
                        public void onStart(String ttsId) {
                            LogUtil.d(XpSpeechTtsHelper.TAG, "textToSpeech onStart " + ttsId);
                            if (XpSpeechTtsHelper.this.mListener != null) {
                                XpSpeechTtsHelper.this.mListener.onSpeechTtsEventCallBack(0, ttsId);
                            }
                        }

                        @Override // android.speech.tts.UtteranceProgressListener
                        public void onDone(String ttsId) {
                            LogUtil.d(XpSpeechTtsHelper.TAG, "textToSpeech onDone " + ttsId);
                            if (XpSpeechTtsHelper.this.mListener != null) {
                                XpSpeechTtsHelper.this.mListener.onSpeechTtsEventCallBack(1, ttsId);
                            }
                        }

                        @Override // android.speech.tts.UtteranceProgressListener
                        public void onError(String ttsId) {
                            LogUtil.d(XpSpeechTtsHelper.TAG, "textToSpeech onError " + ttsId);
                            if (XpSpeechTtsHelper.this.mListener != null) {
                                XpSpeechTtsHelper.this.mListener.onSpeechTtsEventCallBack(2, ttsId);
                            }
                        }

                        @Override // android.speech.tts.UtteranceProgressListener
                        public void onStop(String ttsId, boolean interrupted) {
                            LogUtil.d(XpSpeechTtsHelper.TAG, "textToSpeech onStop " + ttsId);
                            if (XpSpeechTtsHelper.this.mListener != null) {
                                XpSpeechTtsHelper.this.mListener.onSpeechTtsEventCallBack(3, ttsId);
                            }
                        }
                    });
                }
            }
        });
        this.mTtsEngines = new TtsEngines(context);
    }

    public void shutup() {
        if (this.mTextToSpeech != null) {
            LogUtil.i(TAG, "shutup");
            this.mTextToSpeech.stop();
        }
    }

    public void stop(String ttsId) {
        if (this.mTextToSpeech != null) {
            LogUtil.i(TAG, "stop " + ttsId);
            this.mTextToSpeech.stop(ttsId);
        }
    }

    public void release() {
        if (this.mTextToSpeech != null) {
            LogUtil.i(TAG, "release");
            this.mTextToSpeech.shutdown();
        }
    }

    public void notifyTtsEngineReady() {
        if (this.mTtsEngines != null) {
            LogUtil.i(TAG, "notifyTtsEngineReady");
            this.mTtsEngines.setDefaultEngine(TTS_ENGINE_MS);
        }
    }
}
