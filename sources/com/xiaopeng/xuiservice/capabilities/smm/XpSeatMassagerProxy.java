package com.xiaopeng.xuiservice.capabilities.smm;

import android.app.ActivityThread;
import android.car.hardware.mcu.CarMcuManager;
import android.content.res.AssetFileDescriptor;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import com.xiaopeng.xuimanager.seatmassager.ISeatMassager;
import com.xiaopeng.xuimanager.seatmassager.ISeatMassagerEventListener;
import com.xiaopeng.xuimanager.seatmassager.Seat;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.XUIServiceBase;
import com.xiaopeng.xuiservice.capabilities.smm.XpSeatMassagerService;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.debug.WatchDog;
import com.xiaopeng.xuiservice.utils.FileUtils;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/* loaded from: classes5.dex */
public class XpSeatMassagerProxy extends ISeatMassager.Stub implements XUIServiceBase, XpSeatMassagerService.SeatMassagerListener {
    private static final String SYS_SMM_EFFECT_PATH = "/system/etc/xuiservice/smm";
    private static final String SYS_SVM_EFFECT_PATH = "/system/etc/xuiservice/svm";
    private static final String TAG = "XpSeatMassagerProxy";
    private static final String USER_SMM_EFFECT_PATH = "/data/xuiservice/rsc/smm";
    private static final String USER_SVM_EFFECT_PATH = "/data/xuiservice/rsc/svm";
    private static XpSeatMassagerService mService;
    private static final boolean hasMassager = XUIConfig.isSeatMassagerSupport();
    private static final boolean hasVibrate = XUIConfig.hasFeature(XUIConfig.PROPERTY_SVM);
    private static final Map<IBinder, ISeatMassagerEventListener> mListenerMap = new HashMap();
    private static final Map<IBinder, SeatMassagerDeathRecipient> mDeathRecipientMap = new HashMap();
    private static CarClientManager mCar = CarClientManager.getInstance();

    private boolean hasIgOn() {
        try {
            CarMcuManager mcu = mCar.getCarManager("xp_mcu");
            return mcu.getIgStatusFromMcu() == 1;
        } catch (Exception e) {
            LogUtil.e(TAG, "getIgStatusFromMcu failed " + e);
            return false;
        }
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
            mService.unsetSeatMassagerListener(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class SeatMassagerDeathRecipient implements IBinder.DeathRecipient {
        private static final String TAG = "XpSeatMassagerProxy.SeatMassagerDeathRecipient";
        private IBinder mBinder;

        SeatMassagerDeathRecipient(IBinder binder) {
            this.mBinder = binder;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            LogUtil.w(TAG, "binderDied " + this.mBinder);
            XpSeatMassagerProxy.this.unregisterListenerLocked(this.mBinder);
        }

        void release() {
            this.mBinder.unlinkToDeath(this, 0);
        }
    }

    public int getSeatMassagerNumbers() {
        return mService.getSeatMassagerNumbers();
    }

    public int getSeatVibrateNumbers() {
        return mService.getSeatVibrateNumbers();
    }

    public synchronized void registerListener(ISeatMassagerEventListener listener) {
        LogUtil.i(TAG, "registerListener");
        if (listener != null) {
            IBinder binder = listener.asBinder();
            if (!mListenerMap.containsKey(binder)) {
                SeatMassagerDeathRecipient deathRecipient = new SeatMassagerDeathRecipient(binder);
                try {
                    binder.linkToDeath(deathRecipient, 0);
                    mDeathRecipientMap.put(binder, deathRecipient);
                } catch (RemoteException e) {
                    LogUtil.e(TAG, "registerListener failed, " + e);
                    throw new IllegalStateException("XUIServiceNotConnected");
                }
            }
            if (mListenerMap.isEmpty()) {
                mService.setSeatMassagerListener(this);
            }
            mListenerMap.put(binder, listener);
        }
    }

    public synchronized void unregisterListener(ISeatMassagerEventListener listener) {
        LogUtil.i(TAG, "unregisterListener");
        if (listener != null) {
            IBinder binder = listener.asBinder();
            if (mListenerMap.containsKey(binder)) {
                unregisterListenerLocked(binder);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.smm.XpSeatMassagerService.SeatMassagerListener
    public void onMassagerEvent(int id, int type, String effect, int data) {
        for (ISeatMassagerEventListener listener : mListenerMap.values()) {
            try {
                listener.onMassagerEvent(id, type, effect, data);
            } catch (RemoteException e) {
                LogUtil.e(TAG, "onMassagerEvent failed, " + e);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.smm.XpSeatMassagerService.SeatMassagerListener
    public void onVibrateEvent(List<Integer> ids, int type, String effect, int data) {
        List<String> strIds = (List) ids.stream().map($$Lambda$znfQj8LqOvyui6ncUHU4komPIHY.INSTANCE).collect(Collectors.toList());
        for (ISeatMassagerEventListener listener : mListenerMap.values()) {
            try {
                listener.onVibrateEvent(strIds, type, effect, data);
            } catch (RemoteException e) {
                LogUtil.e(TAG, "onVibrateEvent failed, " + e);
            }
        }
    }

    public void loadMassagerEffect() {
        LogUtil.d(TAG, "loadMassagerEffect");
        mService.clearMassagerEffect();
        List<String> userFiles = FileUtils.getAllFiles(USER_SMM_EFFECT_PATH, "json");
        for (String file : userFiles) {
            mService.loadMassagerEffect(file);
        }
        List<String> sysFiles = FileUtils.getAllFiles(SYS_SMM_EFFECT_PATH, "json");
        for (String file2 : sysFiles) {
            mService.loadMassagerEffect(file2);
        }
    }

    public List<String> showMassagerEffect() {
        LogUtil.d(TAG, "showMassagerEffect");
        return mService.showMassagerEffect();
    }

    public int startMassager(List<Seat> seats, String effect) {
        LogUtil.d(TAG, "startMassager " + effect);
        if (hasMassager && hasIgOn()) {
            return mService.startMassager(seats, effect);
        }
        return -1;
    }

    public int stopMassager(List<String> ids) {
        LogUtil.d(TAG, "stopMassager");
        if (hasMassager) {
            return mService.stopMassager((List) ids.stream().map($$Lambda$JWLUSa4hQWAZf1TfuyVGazguidI.INSTANCE).collect(Collectors.toList()));
        }
        return -1;
    }

    public int setMassagerEffect(List<String> ids, String effect) {
        LogUtil.d(TAG, "setMassagerEffect " + effect);
        if (hasMassager) {
            return mService.setMassagerEffect((List) ids.stream().map($$Lambda$JWLUSa4hQWAZf1TfuyVGazguidI.INSTANCE).collect(Collectors.toList()), effect);
        }
        return -1;
    }

    public String getMassagerEffect(int id) {
        LogUtil.d(TAG, "getMassagerEffect " + id);
        return hasMassager ? mService.getMassagerEffect(id) : new String();
    }

    public int setMassagerIntensity(List<String> ids, int intensity) {
        LogUtil.d(TAG, "setMassagerIntensity " + intensity);
        if (hasMassager) {
            return mService.setMassagerIntensity((List) ids.stream().map($$Lambda$JWLUSa4hQWAZf1TfuyVGazguidI.INSTANCE).collect(Collectors.toList()), intensity);
        }
        return -1;
    }

    public int getMassagerIntensity(int id) {
        LogUtil.d(TAG, "getMassagerIntensity " + id);
        if (hasMassager) {
            return mService.getMassagerIntensity(id);
        }
        return -1;
    }

    public int getMassagerStatus(int id) {
        LogUtil.d(TAG, "getMassagerStatus " + id);
        if (hasMassager) {
            return mService.getMassagerStatus(id);
        }
        return -1;
    }

    public void loadVibrateEffect() {
        LogUtil.d(TAG, "loadVibrateEffect");
        mService.clearVibrateEffect();
        List<String> userFiles = FileUtils.getAllFiles(USER_SVM_EFFECT_PATH, "wav");
        for (String file : userFiles) {
            mService.loadVibrateEffect(file);
        }
        List<String> sysFiles = FileUtils.getAllFiles(SYS_SVM_EFFECT_PATH, "wav");
        for (String file2 : sysFiles) {
            mService.loadVibrateEffect(file2);
        }
    }

    public int loadVibrate(AssetFileDescriptor fd, String effect) {
        LogUtil.d(TAG, "loadVibrate, effect=" + effect);
        return mService.loadVibrate(fd, effect);
    }

    public List<String> showVibrateEffect() {
        LogUtil.d(TAG, "showVibrateEffect");
        return mService.showVibrateEffect();
    }

    public int setRhythmEnable(List<String> ids, boolean enable) {
        StringBuilder sb = new StringBuilder();
        sb.append("setRhythmEnable ");
        sb.append(enable ? "enable" : "disable");
        LogUtil.d(TAG, sb.toString());
        if (hasVibrate) {
            return mService.setRhythmEnable((List) ids.stream().map($$Lambda$JWLUSa4hQWAZf1TfuyVGazguidI.INSTANCE).collect(Collectors.toList()), enable);
        }
        return -1;
    }

    public int getRhythmEnable(int id) {
        LogUtil.d(TAG, "getRhythmEnable " + id);
        if (hasVibrate) {
            return mService.getRhythmEnable(id);
        }
        return -1;
    }

    public int doVibrate(List<Seat> seats, String effect, int loop, int position) {
        LogUtil.d(TAG, "doVibrate " + effect + ", loop=" + loop);
        if (hasVibrate && hasIgOn()) {
            return mService.doVibrate(seats, effect, loop, position);
        }
        return -1;
    }

    public int stopVibrate() {
        LogUtil.d(TAG, "stopVibrate");
        if (hasVibrate) {
            return mService.stopVibrate();
        }
        return -1;
    }

    public int setVibrateIntensity(List<String> ids, int intensity) {
        LogUtil.d(TAG, "setVibrateIntensity " + intensity);
        if (hasVibrate) {
            return mService.setVibrateIntensity((List) ids.stream().map($$Lambda$JWLUSa4hQWAZf1TfuyVGazguidI.INSTANCE).collect(Collectors.toList()), intensity);
        }
        return -1;
    }

    public int getVibrateIntensity(int id) {
        LogUtil.d(TAG, "getVibrateIntensity " + id);
        if (hasVibrate) {
            return mService.getVibrateIntensity(id);
        }
        return -1;
    }

    public int setRhythmIntensity(List<String> ids, int intensity) {
        LogUtil.d(TAG, "setRhythmIntensity " + intensity);
        if (hasVibrate) {
            return mService.setRhythmIntensity((List) ids.stream().map($$Lambda$JWLUSa4hQWAZf1TfuyVGazguidI.INSTANCE).collect(Collectors.toList()), intensity);
        }
        return -1;
    }

    public int getRhythmIntensity(int id) {
        LogUtil.d(TAG, "getRhythmIntensity " + id);
        if (hasVibrate) {
            return mService.getRhythmIntensity(id);
        }
        return -1;
    }

    public int setRhythmPattern(int pattern) {
        LogUtil.d(TAG, "setRhythmPattern " + pattern);
        if (hasVibrate) {
            return mService.setRhythmPattern(pattern);
        }
        return -1;
    }

    public int getRhythmPattern() {
        LogUtil.d(TAG, "getRhythmPattern");
        if (hasVibrate) {
            return mService.getRhythmPattern();
        }
        return -1;
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void init() {
        LogUtil.d(TAG, "init");
        loadMassagerEffect();
        loadVibrateEffect();
        addOperationListener();
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void release() {
        LogUtil.d(TAG, "release");
        for (SeatMassagerDeathRecipient deathRecipient : mDeathRecipientMap.values()) {
            deathRecipient.release();
        }
        mDeathRecipientMap.clear();
        mListenerMap.clear();
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void dump(PrintWriter writer) {
    }

    private void addOperationListener() {
    }

    public XpSeatMassagerProxy() {
        mService = XpSeatMassagerService.getInstance(ActivityThread.currentActivityThread().getApplication());
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final XpSeatMassagerProxy sService = new XpSeatMassagerProxy();

        private InstanceHolder() {
        }
    }

    public static XpSeatMassagerProxy getInstance() {
        return InstanceHolder.sService;
    }
}
