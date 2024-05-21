package com.xiaopeng.xuiservice.capabilities;

import android.app.ActivityThread;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.media.AudioConfig.AudioConfig;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.SystemProperties;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TtsEngines;
import android.speech.tts.UtteranceProgressListener;
import android.text.TextUtils;
import com.xiaopeng.speech.SpeechClient;
import com.xiaopeng.speech.protocol.event.DialogEvent;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes5.dex */
public class XpSpeechTtsService implements BroadcastManager.BroadcastListener {
    public static final int EVENT_DONE = 1;
    public static final int EVENT_ERROR = 2;
    public static final int EVENT_START = 0;
    public static final int EVENT_STOP = 3;
    private static final String KEY_DRIVERMODE = "XpMainDriverMode";
    private static final String KEY_PARAM_POSITION = "voicePosition";
    private static final String KEY_PARAM_PRIORITY = "priority";
    private static final String KEY_PARAM_STREAM = "streamType";
    private static final String KEY_PARAM_TTS_MODE = "ttsMode";
    private static final String TAG = "XpSpeechTtsService";
    private static final String TTS_ENGINE_MS = "com.xiaopeng.xpspeechservice.ms";
    private static final String UUID_PREFIX = "xuiservice";
    private boolean hasDrivingMode;
    private boolean isDangerousAlertSpeech;
    private AudioConfig mAudioConfig;
    private Context mContext;
    private String mInstantTtsId;
    private TextToSpeech.OnInitListener mListener;
    private CopyOnWriteArrayList<SpeechTtsEventListener> mListeners;
    private int mStatus;
    private TextToSpeech mTextToSpeech;
    private TtsEngines mTtsEngines;
    private Set<String> mWaitQueue;
    private Binder mWakeBinder;
    private static final Object mLock = new Object();
    private static boolean hasBootCompleted = false;

    /* loaded from: classes5.dex */
    public interface SpeechTtsEventListener {
        void onSpeechTtsEventCallBack(int i, String str);
    }

    /* loaded from: classes5.dex */
    static class SpeechContants {
        public static final int PRIORITY_IMPORTANT = 2;
        public static final int PRIORITY_INSTANT = 4;
        public static final int PRIORITY_NORMAL = 1;
        public static final int PRIORITY_URGENT = 3;

        SpeechContants() {
        }
    }

    protected void release() {
        LogUtil.i(TAG, "release");
        TextToSpeech textToSpeech = this.mTextToSpeech;
        if (textToSpeech != null) {
            textToSpeech.shutdown();
            this.mTextToSpeech = null;
        }
        unsetDangerousAlertSpeech();
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final XpSpeechTtsService sService = new XpSpeechTtsService();

        private InstanceHolder() {
        }
    }

    public static XpSpeechTtsService getInstance() {
        return InstanceHolder.sService;
    }

    private void registerObserver() {
        this.hasDrivingMode = Settings.System.getInt(this.mContext.getContentResolver(), KEY_DRIVERMODE, 0) == 1;
        LogUtil.i(TAG, "registerObserver, hasDrivingMode=" + this.hasDrivingMode);
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor(KEY_DRIVERMODE), true, new ContentObserver(new XuiWorkHandler()) { // from class: com.xiaopeng.xuiservice.capabilities.XpSpeechTtsService.1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                XpSpeechTtsService xpSpeechTtsService = XpSpeechTtsService.this;
                xpSpeechTtsService.hasDrivingMode = Settings.System.getInt(xpSpeechTtsService.mContext.getContentResolver(), XpSpeechTtsService.KEY_DRIVERMODE, 0) == 1;
                LogUtil.i(XpSpeechTtsService.TAG, "onChange, hasDrivingMode=" + XpSpeechTtsService.this.hasDrivingMode);
            }
        });
    }

    private XpSpeechTtsService() {
        this.mStatus = -1;
        this.mWaitQueue = new HashSet();
        this.mInstantTtsId = null;
        this.isDangerousAlertSpeech = true;
        this.hasDrivingMode = false;
        this.mWakeBinder = new Binder();
        this.mTtsEngines = null;
        this.mTextToSpeech = null;
        this.mListener = null;
        this.mListeners = new CopyOnWriteArrayList<>();
        LogUtil.i(TAG, "init");
        this.mContext = ActivityThread.currentActivityThread().getApplication();
        this.mTtsEngines = new TtsEngines(this.mContext);
        this.mListener = new TextToSpeech.OnInitListener() { // from class: com.xiaopeng.xuiservice.capabilities.XpSpeechTtsService.2
            @Override // android.speech.tts.TextToSpeech.OnInitListener
            public void onInit(int status) {
                StringBuilder sb = new StringBuilder();
                sb.append("onInit ");
                sb.append(status == 0 ? "succeed" : "failed");
                LogUtil.i(XpSpeechTtsService.TAG, sb.toString());
                synchronized (XpSpeechTtsService.mLock) {
                    XpSpeechTtsService.this.mStatus = status;
                    XpSpeechTtsService.mLock.notify();
                }
            }
        };
        List<String> filter = new ArrayList<>();
        filter.add("android.intent.action.BOOT_COMPLETED");
        BroadcastManager.getInstance().registerListener(this, filter);
        registerObserver();
        if ("1".equals(SystemProperties.get(XUIConfig.PROPERTY_BOOT_COMPLETE)) && !hasBootCompleted) {
            initAudioConfig();
            initSpeech();
            hasBootCompleted = true;
        }
    }

    public boolean getInitStatus() {
        if (this.mTextToSpeech == null || this.mStatus != 0) {
            LogUtil.w(TAG, "getInitStatus failed, reinitializing speech...");
            initSpeech();
        }
        return this.mTextToSpeech != null && this.mStatus == 0;
    }

    public synchronized void addSpeechTtsEventListener(SpeechTtsEventListener listener) {
        if (!this.mListeners.contains(listener)) {
            this.mListeners.add(listener);
        }
    }

    public synchronized void removeSpeechTtsEventListener(SpeechTtsEventListener listener) {
        if (this.mListeners.contains(listener)) {
            this.mListeners.remove(listener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDangerousAlertSpeech() {
        if (this.mAudioConfig != null && !this.isDangerousAlertSpeech) {
            LogUtil.d(TAG, "setDangerousAlertSpeech");
            SpeechClient.instance().getWakeupEngine().suspendDialogWithReason(this.mWakeBinder, "安全驾驶", DialogEvent.SUSPEND_DIALOG_REASON_AUTO_SYS_ALARM);
            this.mAudioConfig.setDangerousTtsStatus(1);
            this.isDangerousAlertSpeech = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unsetDangerousAlertSpeech() {
        if (this.mAudioConfig != null && this.isDangerousAlertSpeech) {
            LogUtil.d(TAG, "unsetDangerousAlertSpeech");
            SpeechClient.instance().getWakeupEngine().resumeDialogWithReason(this.mWakeBinder, "安全驾驶", DialogEvent.RESUME_DIALOG_REASON_AUTO_SYS_ALARM);
            this.mAudioConfig.setDangerousTtsStatus(0);
            this.isDangerousAlertSpeech = false;
        }
    }

    private synchronized String speak(String text, int priority, boolean isShutUp) {
        String ttsId = UUID_PREFIX + UUID.randomUUID().toString() + System.currentTimeMillis();
        LogUtil.d(TAG, "speak, text:" + text + ", priority:" + priority + ", ttsId:" + ttsId + ", isShutUp:" + isShutUp + ", mStatus: " + this.mStatus);
        if (!getInitStatus() || TextUtils.isEmpty(text)) {
            return null;
        }
        Bundle params = new Bundle();
        boolean hasInstant = priority == 4;
        int stream = hasInstant ? 9 : 10;
        int queue = isShutUp ? 0 : 1;
        params.putInt("priority", priority);
        params.putInt("streamType", stream);
        if (!hasInstant && this.hasDrivingMode) {
            params.putInt(KEY_PARAM_POSITION, 23);
        }
        int res = this.mTextToSpeech.speak(text, queue, params, ttsId);
        if (res == 0 && hasInstant) {
            if (isShutUp) {
                this.mInstantTtsId = ttsId;
            } else {
                this.mWaitQueue.add(ttsId);
            }
        }
        return res == 0 ? ttsId : null;
    }

    public String speakByNormal(String text, boolean isShutUp) {
        return speak(text, 1, isShutUp);
    }

    public String speakByImportant(String text, boolean isShutUp) {
        return speak(text, 2, isShutUp);
    }

    public String speakByUrgent(String text, boolean isShutUp) {
        return speak(text, 3, isShutUp);
    }

    public String speakByInstant(String text, boolean isShutUp) {
        return speak(text, 4, isShutUp);
    }

    public synchronized void stopSpeech(String ttsId) {
        if (this.mTextToSpeech != null && !TextUtils.isEmpty(ttsId)) {
            LogUtil.d(TAG, "stopSpeech " + ttsId);
            this.mTextToSpeech.stop(ttsId);
        }
    }

    public synchronized void stopAllSpeech() {
        if (this.mTextToSpeech != null) {
            LogUtil.d(TAG, "stopAllSpeech");
            this.mWaitQueue.clear();
            this.mTextToSpeech.stop();
        }
    }

    public void notifyTtsEngineReady() {
        if (this.mTtsEngines != null) {
            LogUtil.d(TAG, "notifyTtsEngineReady");
            this.mTtsEngines.setDefaultEngine(TTS_ENGINE_MS);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean hasInstantSpeech(String ttsId, int status) {
        if (TextUtils.isEmpty(this.mInstantTtsId) || !this.mInstantTtsId.equals(ttsId)) {
            if (this.mWaitQueue.contains(ttsId)) {
                this.mWaitQueue.remove(ttsId);
                if (status == 0) {
                    this.mInstantTtsId = ttsId;
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    private void initAudioConfig() {
        this.mAudioConfig = new AudioConfig(this.mContext);
        unsetDangerousAlertSpeech();
    }

    private void setTtsUtteranceProgressListener() {
        if (this.mTextToSpeech != null) {
            LogUtil.i(TAG, "setTtsUtteranceProgressListener");
            this.mTextToSpeech.setLanguage(Locale.CHINESE);
            this.mTextToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() { // from class: com.xiaopeng.xuiservice.capabilities.XpSpeechTtsService.3
                @Override // android.speech.tts.UtteranceProgressListener
                public void onStart(String ttsId) {
                    LogUtil.d(XpSpeechTtsService.TAG, "onStart, ttsId: " + ttsId);
                    if (XpSpeechTtsService.this.hasInstantSpeech(ttsId, 0)) {
                        XpSpeechTtsService.this.setDangerousAlertSpeech();
                    }
                    if (!XpSpeechTtsService.this.mListeners.isEmpty()) {
                        Iterator it = XpSpeechTtsService.this.mListeners.iterator();
                        while (it.hasNext()) {
                            SpeechTtsEventListener listener = (SpeechTtsEventListener) it.next();
                            listener.onSpeechTtsEventCallBack(0, ttsId);
                        }
                    }
                }

                @Override // android.speech.tts.UtteranceProgressListener
                public void onDone(String ttsId) {
                    LogUtil.d(XpSpeechTtsService.TAG, "onDone, ttsId: " + ttsId);
                    if (XpSpeechTtsService.this.hasInstantSpeech(ttsId, 1)) {
                        XpSpeechTtsService.this.unsetDangerousAlertSpeech();
                    }
                    if (!XpSpeechTtsService.this.mListeners.isEmpty()) {
                        Iterator it = XpSpeechTtsService.this.mListeners.iterator();
                        while (it.hasNext()) {
                            SpeechTtsEventListener listener = (SpeechTtsEventListener) it.next();
                            listener.onSpeechTtsEventCallBack(1, ttsId);
                        }
                    }
                }

                @Override // android.speech.tts.UtteranceProgressListener
                public void onError(String ttsId) {
                    LogUtil.e(XpSpeechTtsService.TAG, "onError, ttsId: " + ttsId);
                    if (XpSpeechTtsService.this.hasInstantSpeech(ttsId, 2)) {
                        XpSpeechTtsService.this.unsetDangerousAlertSpeech();
                    }
                    if (!XpSpeechTtsService.this.mListeners.isEmpty()) {
                        Iterator it = XpSpeechTtsService.this.mListeners.iterator();
                        while (it.hasNext()) {
                            SpeechTtsEventListener listener = (SpeechTtsEventListener) it.next();
                            listener.onSpeechTtsEventCallBack(2, ttsId);
                        }
                    }
                }

                @Override // android.speech.tts.UtteranceProgressListener
                public void onStop(String ttsId, boolean interrupted) {
                    LogUtil.d(XpSpeechTtsService.TAG, "onStop, ttsId: " + ttsId);
                    if (XpSpeechTtsService.this.hasInstantSpeech(ttsId, 3)) {
                        XpSpeechTtsService.this.unsetDangerousAlertSpeech();
                    }
                    if (!XpSpeechTtsService.this.mListeners.isEmpty()) {
                        Iterator it = XpSpeechTtsService.this.mListeners.iterator();
                        while (it.hasNext()) {
                            SpeechTtsEventListener listener = (SpeechTtsEventListener) it.next();
                            listener.onSpeechTtsEventCallBack(3, ttsId);
                        }
                    }
                }
            });
        }
    }

    private void initSpeech() {
        synchronized (mLock) {
            try {
                this.mTextToSpeech = new TextToSpeech(this.mContext, this.mListener);
                mLock.wait(500L);
                StringBuilder sb = new StringBuilder();
                sb.append("initSpeech ");
                sb.append(this.mStatus == 0);
                LogUtil.i(TAG, sb.toString());
                if (this.mStatus == 0) {
                    setTtsUtteranceProgressListener();
                } else {
                    release();
                }
            } catch (Exception e) {
                LogUtil.e(TAG, "initSpeech failed, " + e);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.utils.BroadcastManager.BroadcastListener
    public void onReceive(Context context, Intent intent) {
        LogUtil.i(TAG, "onReceive " + intent);
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") && !hasBootCompleted) {
            initAudioConfig();
            initSpeech();
            hasBootCompleted = true;
        }
    }
}
