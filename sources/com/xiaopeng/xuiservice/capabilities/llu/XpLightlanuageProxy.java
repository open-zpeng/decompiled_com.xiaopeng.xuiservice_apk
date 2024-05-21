package com.xiaopeng.xuiservice.capabilities.llu;

import android.app.ActivityThread;
import android.car.hardware.bcm.CarBcmManager;
import android.car.hardware.llu.CarLluManager;
import android.car.hardware.mcu.CarMcuManager;
import android.car.hardware.vcu.CarVcuManager;
import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioConfig.AudioConfig;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xiaopeng.xuimanager.lightlanuage.ILightLanuage;
import com.xiaopeng.xuimanager.lightlanuage.ILightLanuageEventListener;
import com.xiaopeng.xuimanager.operation.OperationResource;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.XUIServiceBase;
import com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageService;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.debug.WatchDog;
import com.xiaopeng.xuiservice.operation.LocalOperationManager;
import com.xiaopeng.xuiservice.utils.FileUtils;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class XpLightlanuageProxy extends ILightLanuage.Stub implements XUIServiceBase, XpLightlanuageService.LightEffectListener {
    private static final String DANCE_INFO = "/system/etc/xuiservice/dance/LocalDance_Info.json";
    private static final String DEFAULT_DANCE_PATH = "/system/etc/xuiservice/dance";
    private static final String EFFECT_ENABLE = "LangLightEffectEnable";
    private static final String EFFECT_MCU = "LIGHT_EFFECT";
    private static final String EFFECT_RHYTHM = "LangLightMusicEffect";
    private static final String EFFECT_SAYHI = "isSayHiEnable";
    private static final int MCU_SHOWOFF = 10;
    private static final String SYS_EFFECT_PATH = "/system/etc/xuiservice/llu";
    private static final String TAG = "XpLightlanuageProxy";
    private static final String USER_EFFECT_PATH = "/data/xuiservice/llu";
    private static Context mContext;
    private static XpLightlanuageService mService;
    private AudioConfig mAudioConfig;
    private AudioManager mAudioManager;
    private static final Object mLock = new Object();
    private static final Map<IBinder, ILightLanuageEventListener> mListenerMap = new HashMap();
    private static final Map<IBinder, LightLanuageDeathRecipient> mDeathRecipientMap = new HashMap();
    private static boolean mRhythmStatus = false;
    private static String mSayhiName = null;
    private static CarClientManager mCar = CarClientManager.getInstance();

    private boolean hasIgOn() {
        try {
            CarMcuManager mcu = mCar.getCarManager("xp_mcu");
            boolean z = true;
            if (mcu.getIgStatusFromMcu() != 1) {
                z = false;
            }
            boolean res = z;
            LogUtil.d(TAG, res ? "" : "can not play, ig status off");
            return res;
        } catch (Exception e) {
            LogUtil.e(TAG, "hasIgOn failed " + e);
            return false;
        }
    }

    private boolean checkFileExists(String path) {
        return new File(path).exists();
    }

    private JSONObject parseEffectInfo(String path) {
        JSONObject obj = null;
        try {
            List<String> jsonFiles = FileUtils.getAllFiles(path, "json");
            List<String> mediaFiles = FileUtils.getAllFiles(path, "mp4");
            if (jsonFiles.size() != 1 || mediaFiles.size() > 1) {
                return null;
            }
            JSONObject content = new JSONObject();
            String file = jsonFiles.get(0);
            content.put("effectName", file.substring(file.lastIndexOf("/") + 1, file.lastIndexOf(".json")));
            content.put("videoPath", mediaFiles.size() == 1 ? mediaFiles.get(0) : "");
            obj = new JSONObject();
            obj.put("id", path.substring(path.lastIndexOf("/") + 1));
            obj.put("category", 1);
            obj.put("content", content);
            return obj;
        } catch (Exception e) {
            LogUtil.e(TAG, "parseEffectInfo failed " + e);
            return obj;
        }
    }

    private String getAudioSource(String name) {
        List<String> subdirs = FileUtils.getAllDirs(USER_EFFECT_PATH);
        subdirs.add(DEFAULT_DANCE_PATH);
        for (String path : subdirs) {
            String file = path + "/" + name + ".mp3";
            if (checkFileExists(file)) {
                LogUtil.d(TAG, "getAudioSource: " + file);
                return file;
            }
        }
        return null;
    }

    private boolean hasGearStatusEquals(int level) {
        try {
            CarVcuManager vcu = mCar.getCarManager("xp_vcu");
            boolean res = vcu.getDisplayGearLevel() == level;
            if (!res && level == 4) {
                LogUtil.d(TAG, "can not play, current gear status is not P");
            }
            return res;
        } catch (Exception e) {
            LogUtil.e(TAG, "hasGearStatusEquals failed " + e);
            return false;
        }
    }

    private boolean hasTurnLampsStatusOff() {
        try {
            CarBcmManager bcm = mCar.getCarManager("xp_bcm");
            int[] data = bcm.getLeftAndRightTurnLampsActiveStatus();
            boolean z = true;
            if (data[0] == 1 || data[1] == 1) {
                z = false;
            }
            boolean res = z;
            LogUtil.d(TAG, res ? "" : "can not play, turnLamp is not turned off");
            return res;
        } catch (Exception e) {
            LogUtil.e(TAG, "hasTurnLampsStatusOff failed " + e);
            return false;
        }
    }

    private boolean hasCarDrivingStatus() {
        try {
            CarVcuManager vcu = mCar.getCarManager("xp_vcu");
            boolean res = vcu.getRawCarSpeed() > 3.0f;
            LogUtil.d(TAG, res ? "can not play, car speed is greater than 3km/h" : "");
            return res;
        } catch (Exception e) {
            LogUtil.e(TAG, "hasCarDrivingStatus failed " + e);
            return false;
        }
    }

    private boolean hasRearFogLampStatusOff() {
        try {
            CarBcmManager bcm = mCar.getCarManager("xp_bcm");
            boolean res = bcm.getRearFogLamp() == 0;
            LogUtil.d(TAG, res ? "" : "can not play, rearFogLamp is not turned off");
            return res;
        } catch (Exception e) {
            LogUtil.e(TAG, "hasRearFogLampStatusOff failed " + e);
            return false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x001f  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0022  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean hasBtPhoneOnCallingStatus() {
        /*
            r5 = this;
            java.lang.String r0 = "XpLightlanuageProxy"
            r1 = 0
            android.media.AudioConfig.AudioConfig r2 = r5.mAudioConfig     // Catch: java.lang.Exception -> L28
            int r2 = r2.getBtCallOnFlag()     // Catch: java.lang.Exception -> L28
            if (r2 != 0) goto L1c
            android.media.AudioManager r2 = r5.mAudioManager     // Catch: java.lang.Exception -> L28
            java.lang.String r2 = r2.getCurrentAudioFocusPackageName()     // Catch: java.lang.Exception -> L28
            java.lang.String r3 = "com.xiaopeng.btphone"
            boolean r2 = r2.equals(r3)     // Catch: java.lang.Exception -> L28
            if (r2 == 0) goto L1a
            goto L1c
        L1a:
            r2 = r1
            goto L1d
        L1c:
            r2 = 1
        L1d:
            if (r2 == 0) goto L22
            java.lang.String r3 = "can not play, BT phone on calling"
            goto L24
        L22:
            java.lang.String r3 = ""
        L24:
            com.xiaopeng.xuimanager.utils.LogUtil.d(r0, r3)     // Catch: java.lang.Exception -> L28
            return r2
        L28:
            r2 = move-exception
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "hasBtPhoneOnCallingStatus failed "
            r3.append(r4)
            r3.append(r2)
            java.lang.String r3 = r3.toString()
            com.xiaopeng.xuimanager.utils.LogUtil.e(r0, r3)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageProxy.hasBtPhoneOnCallingStatus():boolean");
    }

    private boolean hasAudioFocusAvailable() {
        boolean res = mService.requestAudioFocus();
        LogUtil.d(TAG, res ? "" : "can not play, audioFocus not available");
        return res;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0019  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x001c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean hasAudioStatusReady() {
        /*
            r5 = this;
            java.lang.String r0 = "XpLightlanuageProxy"
            r1 = 0
            android.media.AudioManager r2 = r5.mAudioManager     // Catch: java.lang.Exception -> L22
            boolean r2 = r2.isMusicActive()     // Catch: java.lang.Exception -> L22
            if (r2 != 0) goto L16
            android.media.AudioManager r2 = r5.mAudioManager     // Catch: java.lang.Exception -> L22
            boolean r2 = r2.isKaraokeOn()     // Catch: java.lang.Exception -> L22
            if (r2 == 0) goto L14
            goto L16
        L14:
            r2 = r1
            goto L17
        L16:
            r2 = 1
        L17:
            if (r2 == 0) goto L1c
            java.lang.String r3 = ""
            goto L1e
        L1c:
            java.lang.String r3 = "can not play, audio status not ready"
        L1e:
            com.xiaopeng.xuimanager.utils.LogUtil.d(r0, r3)     // Catch: java.lang.Exception -> L22
            return r2
        L22:
            r2 = move-exception
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "hasAudioStatusReady failed "
            r3.append(r4)
            r3.append(r2)
            java.lang.String r3 = r3.toString()
            com.xiaopeng.xuimanager.utils.LogUtil.e(r0, r3)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageProxy.hasAudioStatusReady():boolean");
    }

    private boolean checkRhythmCondition() {
        return getRhythmEffectEnable() && getLightEffectEnable() && hasAudioStatusReady();
    }

    private boolean checkSayhiCondition() {
        return getSayhiEffectEnable() && getLightEffectEnable() && hasRearFogLampStatusOff() && hasTurnLampsStatusOff();
    }

    private boolean checkPlayCondition(boolean hasDancing) {
        return hasIgOn() && getLightEffectEnable() && (hasGearStatusEquals(4) || mService.hasExpand()) && hasRearFogLampStatusOff() && hasTurnLampsStatusOff() && (!(hasDancing && hasBtPhoneOnCallingStatus()) && (!hasDancing || hasAudioFocusAvailable()));
    }

    private String getSayhiMode() {
        return String.format("%02d", Integer.valueOf(getMcuEffect(10)));
    }

    public void setListener(XpLightlanuageService.LightEffectListener listener) {
        LogUtil.i(TAG, "setListener");
        mService.setLightEffectListener(listener);
    }

    public void unsetListener(XpLightlanuageService.LightEffectListener listener) {
        LogUtil.i(TAG, "unsetListener");
        mService.unsetLightEffectListener(listener);
    }

    public void setAiLightEffectListener(XpLightlanuageService.AvmListener listener) {
    }

    public boolean playRhythmEffect(float ratio) {
        return checkRhythmCondition() && mService.playEffect(ratio);
    }

    public boolean playSayhiEffect() {
        mSayhiName = "android_avh_sayhi_" + getSayhiMode();
        return checkSayhiCondition() && mService.playEffect(mSayhiName, null, 1);
    }

    public void stopSayhiEffect() {
        mService.stopEffect(mSayhiName);
    }

    public int playXSayhiEffect() {
        if (!getLightEffectEnable()) {
            return R.string.llu_toast_disabled;
        }
        if (hasCarDrivingStatus() || !hasTurnLampsStatusOff() || !hasRearFogLampStatusOff()) {
            return R.string.llu_toast_gear_limit;
        }
        String name = "android_xsayhi_" + getSayhiMode();
        mService.playEffect(name, null, 1);
        return R.string.llu_toast_saihi_playing;
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        String info = "code=" + code + ", pid=" + Binder.getCallingPid() + "@" + SystemClock.elapsedRealtimeNanos();
        WatchDog.getInstance().binderEnter(TAG, info);
        try {
            try {
                boolean ret = super.onTransact(code, data, reply, flags);
                return ret;
            } catch (RemoteException e) {
                LogUtil.w(TAG, "onTransact failed, e=" + e + ", code=" + code);
                throw e;
            }
        } finally {
            WatchDog.getInstance().binderLeave(TAG, info);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void unregisterListenerLocked(IBinder binder) {
        Object status = mListenerMap.remove(binder);
        if (status != null) {
            mDeathRecipientMap.get(binder).release();
            mDeathRecipientMap.remove(binder);
        }
        if (mListenerMap.isEmpty()) {
            mService.unsetLightEffectListener(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class LightLanuageDeathRecipient implements IBinder.DeathRecipient {
        private static final String TAG = "XpLightlanuageProxy.LightLanuageDeathRecipient";
        private IBinder mBinder;

        LightLanuageDeathRecipient(IBinder binder) {
            this.mBinder = binder;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            LogUtil.w(TAG, "binderDied " + this.mBinder);
            XpLightlanuageProxy.this.unregisterListenerLocked(this.mBinder);
        }

        void release() {
            this.mBinder.unlinkToDeath(this, 0);
        }
    }

    public synchronized void registerListener(ILightLanuageEventListener listener) {
        LogUtil.i(TAG, "registerListener");
        if (listener != null) {
            IBinder binder = listener.asBinder();
            if (!mListenerMap.containsKey(binder)) {
                LightLanuageDeathRecipient deathRecipient = new LightLanuageDeathRecipient(binder);
                try {
                    binder.linkToDeath(deathRecipient, 0);
                    mDeathRecipientMap.put(binder, deathRecipient);
                } catch (RemoteException e) {
                    LogUtil.e(TAG, "registerListener failed, " + e);
                    throw new IllegalStateException("XUIServiceNotConnected");
                }
            }
            if (mListenerMap.isEmpty()) {
                mService.setLightEffectListener(this);
            }
            mListenerMap.put(binder, listener);
        }
    }

    public synchronized void unregisterListener(ILightLanuageEventListener listener) {
        LogUtil.i(TAG, "unregisterListener");
        if (listener != null) {
            IBinder binder = listener.asBinder();
            if (mListenerMap.containsKey(binder)) {
                unregisterListenerLocked(binder);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageService.LightEffectListener
    public void onStartEffect(String name, String type) {
        for (ILightLanuageEventListener l : mListenerMap.values()) {
            try {
                l.onStartEvent(name, type);
            } catch (RemoteException e) {
                LogUtil.e(TAG, "onStartEffect failed, " + e);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageService.LightEffectListener
    public void onStopEffect(String name, String type) {
        for (ILightLanuageEventListener l : mListenerMap.values()) {
            try {
                l.onStopEvent(name, type);
            } catch (RemoteException e) {
                LogUtil.e(TAG, "onStopEffect failed, " + e);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageService.LightEffectListener
    public void onFinishEffect(String name, String type) {
        for (ILightLanuageEventListener l : mListenerMap.values()) {
            try {
                l.onFinishEvent(name, type);
            } catch (RemoteException e) {
                LogUtil.e(TAG, "onFinishEffect failed, " + e);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageService.LightEffectListener
    public void onErrorEffect(String name, int errCode) {
        for (ILightLanuageEventListener l : mListenerMap.values()) {
            try {
                l.onErrorEvent(name, errCode);
            } catch (RemoteException e) {
                LogUtil.e(TAG, "onErrorEffect failed, " + e);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageService.LightEffectListener
    public void onUpgradeEffect(int name, int mode) {
        for (ILightLanuageEventListener l : mListenerMap.values()) {
            try {
                l.onUpgradeEvent(name, mode);
            } catch (RemoteException e) {
                LogUtil.e(TAG, "onUpgradeEffect failed, " + e);
            }
        }
    }

    public void loadLightEffect() {
        synchronized (mLock) {
            LogUtil.i(TAG, "load lightEffect");
            mService.clearLightEffect();
            List<String> userFiles = FileUtils.getAllFiles(USER_EFFECT_PATH, "json");
            for (String file : userFiles) {
                mService.loadLightEffect(file);
            }
            List<String> sysFiles = FileUtils.getAllFiles(SYS_EFFECT_PATH, "json");
            for (String file2 : sysFiles) {
                mService.loadLightEffect(file2);
            }
        }
    }

    public List<String> getLightEffect() {
        return mService.getLightEffect();
    }

    public String getUserEffectInfoList() {
        JSONArray array = new JSONArray();
        List<String> subdirs = FileUtils.getAllDirs(USER_EFFECT_PATH);
        for (String path : subdirs) {
            JSONObject obj = parseEffectInfo(path);
            if (obj != null) {
                array.put(obj);
            }
        }
        LogUtil.d(TAG, "getUserEffectInfoList: " + array.toString());
        return array.toString();
    }

    public String getUserEffectInfo(String subdir) {
        JSONObject obj = parseEffectInfo("/data/xuiservice/llu/" + subdir);
        String str = obj == null ? null : obj.toString();
        LogUtil.d(TAG, "getUserEffectInfo: " + str);
        return str;
    }

    public String getLocalDanceEffectInfo() {
        String str = null;
        try {
            str = new String(Files.readAllBytes(Paths.get(DANCE_INFO, new String[0])));
            LogUtil.d(TAG, "getLocalDanceEffectInfo: " + str);
            return str;
        } catch (Exception e) {
            LogUtil.e(TAG, "getLocalDanceEffectInfo failed, " + e);
            return str;
        }
    }

    public boolean getSayhiEffectEnable() {
        boolean res = Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), EFFECT_SAYHI, 0) == 1;
        LogUtil.d(TAG, res ? "" : "can not play, sayhi switch disabled");
        return res;
    }

    public void setSayhiEffectEnable(boolean enable) {
        String str;
        String name = "android_avh_sayhi_" + getSayhiMode();
        StringBuilder sb = new StringBuilder();
        sb.append("set sayhiEffect ");
        if (enable) {
            str = "enable";
        } else {
            str = "disable, stop " + name;
        }
        sb.append(str);
        LogUtil.i(TAG, sb.toString());
        if (!enable) {
            mService.stopEffect(name);
        }
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), EFFECT_SAYHI, enable ? 1 : 0);
    }

    public boolean getRhythmEffectEnable() {
        LogUtil.d(TAG, mRhythmStatus ? "" : "can not play, rhythmEffect switch disabled");
        return mRhythmStatus;
    }

    public void setRhythmEffectEnable(boolean enable) {
        StringBuilder sb = new StringBuilder();
        sb.append("set rhythmEffect ");
        sb.append(enable ? "enable" : "disable");
        LogUtil.i(TAG, sb.toString());
        mRhythmStatus = enable;
    }

    public boolean getLightEffectEnable() {
        boolean enable = Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), EFFECT_ENABLE, 1) == 1;
        LogUtil.d(TAG, enable ? "" : "can not play, lightEffect switch disabled");
        return enable;
    }

    public void setLightEffectEnable(boolean enable) {
        int i;
        StringBuilder sb = new StringBuilder();
        sb.append("set lightEffect ");
        sb.append(enable ? "enable" : "disable, stop Effect");
        LogUtil.i(TAG, sb.toString());
        int i2 = 1;
        if (!enable) {
            mService.stopEffect(true);
        }
        try {
            CarLluManager llu = mCar.getCarManager("xp_llu");
            if (enable) {
                i = 1;
            } else {
                i = 0;
            }
            llu.setMcuLluEnableStatus(i);
            ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
            if (!enable) {
                i2 = 0;
            }
            Settings.System.putInt(contentResolver, EFFECT_ENABLE, i2);
        } catch (Exception e) {
            LogUtil.e(TAG, "setLightEffectEnable failed " + e);
        }
    }

    public int getMcuEffect(int name) {
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        return Settings.System.getInt(contentResolver, EFFECT_MCU + name, 1);
    }

    public void setMcuEffect(int name, int mode) {
        if (name == 10 && mode != 0) {
            LogUtil.i(TAG, "set sayhiEffect mode: " + mode);
        } else {
            LogUtil.i(TAG, "set mcuEffect, name=" + name + ", mode=" + mode);
            mService.setMcuEffect(name, mode);
        }
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        Settings.System.putInt(contentResolver, EFFECT_MCU + name, mode);
    }

    public String getRhythmEffect() {
        String name = Settings.System.getString(ActivityThread.currentActivityThread().getApplication().getContentResolver(), EFFECT_RHYTHM);
        return name == null ? "expomode_music1" : name;
    }

    public void setRhythmEffect(String name) {
        if (!TextUtils.isEmpty(name)) {
            LogUtil.i(TAG, "set rhythmEffect, name=" + name);
            mService.setRhythmEffect(name);
            Settings.System.putString(ActivityThread.currentActivityThread().getApplication().getContentResolver(), EFFECT_RHYTHM, name);
        }
    }

    public int getDanceEffectRunnable() {
        int status1 = (hasGearStatusEquals(4) || mService.hasExpand()) ? 0 : 1;
        int status2 = (hasTurnLampsStatusOff() && hasRearFogLampStatusOff()) ? 0 : 2;
        int status3 = hasBtPhoneOnCallingStatus() ? 4 : 0;
        LogUtil.i(TAG, "get danceEffect runnable, status=" + (status1 | status2 | status3));
        return -(status1 | status2 | status3);
    }

    public boolean isDanceEffectRunning() {
        return mService.isDanceEffectRunning();
    }

    public String getRunningEffect() {
        return mService.getRunningEffect();
    }

    public int playEffect(String name, int count) {
        int ret;
        String source = getAudioSource(name);
        if (!TextUtils.isEmpty(name) && name.equals(mService.getRunningEffect())) {
            ret = -1;
        } else {
            ret = 0;
            if (!checkPlayCondition(source != null)) {
                ret = -2;
            } else if (!mService.playEffect(name, source, count)) {
                ret = -3;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("play ");
        sb.append(source != null ? "dance" : ToastUtils.MODE.LIGHT);
        sb.append("Effect, name=");
        sb.append(name);
        sb.append(", count=");
        sb.append(count);
        sb.append(", ret=");
        sb.append(ret);
        LogUtil.i(TAG, sb.toString());
        return ret;
    }

    public void stopEffect() {
        StringBuilder sb = new StringBuilder();
        sb.append("stop ");
        sb.append(mService.isDanceEffectRunning() ? "dance" : ToastUtils.MODE.LIGHT);
        sb.append("Effect");
        LogUtil.i(TAG, sb.toString());
        mService.stopEffect(true);
    }

    public void suspendEffect() {
        StringBuilder sb = new StringBuilder();
        sb.append("suspend ");
        sb.append(mService.isDanceEffectRunning() ? "dance" : ToastUtils.MODE.LIGHT);
        sb.append("Effect");
        LogUtil.i(TAG, sb.toString());
        mService.stopEffect(false);
    }

    public void playSmartEffect(int type) {
    }

    public void stopSmartEffect() {
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void init() {
        LogUtil.d(TAG, "init");
        loadLightEffect();
        addOperationListener();
        setLightEffectEnable(getLightEffectEnable());
        setRhythmEffect(getRhythmEffect());
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void release() {
        LogUtil.d(TAG, "release");
        for (LightLanuageDeathRecipient deathRecipient : mDeathRecipientMap.values()) {
            deathRecipient.release();
        }
        mDeathRecipientMap.clear();
        mListenerMap.clear();
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void dump(PrintWriter writer) {
    }

    private void addOperationListener() {
        LogUtil.i(TAG, "addOperationListener");
        LocalOperationManager.getInstance().setDownloadPath(1010101, USER_EFFECT_PATH);
        LocalOperationManager.getInstance().registerListener(1010101, new LocalOperationManager.IEventListener() { // from class: com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageProxy.1
            @Override // com.xiaopeng.xuiservice.operation.LocalOperationManager.IEventListener
            public void onOperationSourceAdd(int type, OperationResource resource) {
                LogUtil.i(XpLightlanuageProxy.TAG, "onOperationSourceAdd, type=" + type);
                if (type == 1010101) {
                    XpLightlanuageProxy.this.loadLightEffect();
                }
            }

            @Override // com.xiaopeng.xuiservice.operation.LocalOperationManager.IEventListener
            public void onOperationSourceExpire(int type, OperationResource resource) {
            }

            @Override // com.xiaopeng.xuiservice.operation.LocalOperationManager.IEventListener
            public void onOperationSourceDelete(int type, OperationResource resource) {
            }

            @Override // com.xiaopeng.xuiservice.operation.LocalOperationManager.IEventListener
            public void onRemoteSourceQuerySuccess(int type, List<OperationResource> resources) {
            }
        });
    }

    public XpLightlanuageProxy() {
        this.mAudioConfig = null;
        mContext = ActivityThread.currentActivityThread().getApplication();
        mService = XpLightlanuageService.getInstance(mContext);
        this.mAudioManager = (AudioManager) mContext.getSystemService("audio");
        this.mAudioConfig = new AudioConfig(mContext);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class InstanceHolder {
        private static final XpLightlanuageProxy sService = new XpLightlanuageProxy();

        private InstanceHolder() {
        }
    }

    public static XpLightlanuageProxy getInstance() {
        return InstanceHolder.sService;
    }
}
