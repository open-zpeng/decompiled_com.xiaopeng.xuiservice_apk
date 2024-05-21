package com.xiaopeng.xuiservice.condition;

import android.app.UiModeManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.Settings;
import com.xiaopeng.xuimanager.condition.IBrightnessLevelListener;
import com.xiaopeng.xuimanager.condition.ICondition;
import com.xiaopeng.xuimanager.condition.IConditionListener;
import com.xiaopeng.xuimanager.condition.ITwilightStateListener;
import com.xiaopeng.xuimanager.condition.TwilightState;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIServiceBase;
import com.xiaopeng.xuiservice.condition.ConditionService;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import com.xiaopeng.xuiservice.debug.WatchDog;
import com.xiaopeng.xuiservice.smart.condition.Condition;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionLocation;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
/* loaded from: classes5.dex */
public class ConditionService extends ICondition.Stub implements XUIServiceBase, Condition.ConditionChangeListener {
    private ConditionLocation conditionLocation;
    private final Context context;
    private final LocationManager locationManager;
    private final ConditionObserver mConditionObserver;
    private final Handler mHandler;
    private final HandlerThread mHandlerThread;
    private final ContentResolver mResolver;
    private static final String TAG = "CONDITION.SERVICE." + ConditionService.class.getSimpleName();
    public static final String LIGHT_INTENSITY_KEY = "autolight";
    public static final Uri URI_LIGHT_INTENSITY = Settings.System.getUriFor(LIGHT_INTENSITY_KEY);
    private int mBrightnessLevel = -1;
    private Map<IBinder, ITwilightStateListener> iTwilightStateListenerMap = new ConcurrentHashMap();
    private Map<IBinder, IBrightnessLevelListener> iBrightnessLevelListenerMap = new ConcurrentHashMap();
    private Map<IBinder, TwilightDeathRecipient> iTwilightdeathRecipientMap = new ConcurrentHashMap();
    private Map<IBinder, BrightnessLevelDeathRecipient> iBrightnessLeveldeathRecipientMap = new ConcurrentHashMap();

    public ConditionService(Context context) {
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService("location");
        DumpDispatcher.registerDump("condition", new DumpDispatcher.DebugDumper() { // from class: com.xiaopeng.xuiservice.condition.-$$Lambda$ConditionService$YsLYEdM2bvrXXC-3P67fDO-v6Z8
            @Override // com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
            public final void debugDump(PrintWriter printWriter, String[] strArr) {
                ConditionService.this.lambda$new$0$ConditionService(printWriter, strArr);
            }
        });
        this.mHandlerThread = new HandlerThread("ConditionhandlerThread");
        this.mHandlerThread.start();
        this.mHandler = new Handler(this.mHandlerThread.getLooper());
        this.mResolver = context.getContentResolver();
        this.mConditionObserver = new ConditionObserver(this.mHandler);
        this.mResolver.registerContentObserver(URI_LIGHT_INTENSITY, false, this.mConditionObserver, -1);
    }

    public /* synthetic */ void lambda$new$0$ConditionService(PrintWriter pw, String[] args) {
        dump(pw);
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        String info = "code=" + code + ",pid=" + Binder.getCallingPid() + "@" + SystemClock.elapsedRealtimeNanos();
        WatchDog.getInstance().binderEnter("ConditionService", info);
        try {
            try {
                boolean ret = super.onTransact(code, data, reply, flags);
                return ret;
            } catch (RemoteException e) {
                LogUtil.w(TAG, "onTransact e=" + e + ",code=" + code);
                throw e;
            }
        } finally {
            WatchDog.getInstance().binderLeave("ConditionService", info);
        }
    }

    public int createCondition(String json) throws RemoteException {
        return 0;
    }

    public void releaseCondition(int conditionId) throws RemoteException {
    }

    public boolean isConditionMatch(int conditionId) throws RemoteException {
        return false;
    }

    public void startWatchCondition(int conditionId) throws RemoteException {
    }

    public void stopWatchCondition(int conditionId) throws RemoteException {
    }

    public void addConditionListener(int conditionId, IConditionListener listener) throws RemoteException {
    }

    public void removeConditionListener(int conditionId, IConditionListener listener) throws RemoteException {
    }

    public TwilightState getTwilightState() throws RemoteException {
        prepareLocation();
        Location location = this.conditionLocation.getCurrentValue();
        String str = TAG;
        LogUtil.i(str, "getTwilightState, location = " + location);
        if (location == null) {
            return null;
        }
        UiModeManager uiModeManager = (UiModeManager) this.context.getSystemService("uimode");
        if (uiModeManager == null) {
            LogUtil.i(TAG, "getTwilightState, error: uiModeManager = null");
            return null;
        }
        long[] twoMs = uiModeManager.getTwilightState(location, System.currentTimeMillis());
        return new TwilightState(twoMs[0], twoMs[1]);
    }

    private void prepareLocation() {
        if (this.conditionLocation == null) {
            this.conditionLocation = new ConditionLocation(new Location("gps"), 60000L, 100.0f);
            this.conditionLocation.addConditionChangeListener(this);
        }
    }

    public void registerTwilightStateListener(ITwilightStateListener listener) throws RemoteException {
        IBinder listenerBinder = listener.asBinder();
        if (this.iTwilightStateListenerMap.containsKey(listenerBinder)) {
            return;
        }
        TwilightDeathRecipient deathRecipient = new TwilightDeathRecipient(listenerBinder);
        try {
            listenerBinder.linkToDeath(deathRecipient, 0);
            this.iTwilightdeathRecipientMap.put(listenerBinder, deathRecipient);
            boolean startWatchLocation = this.iTwilightStateListenerMap.isEmpty();
            this.iTwilightStateListenerMap.put(listenerBinder, listener);
            if (startWatchLocation) {
                prepareLocation();
                this.conditionLocation.startWatch();
            }
        } catch (RemoteException e) {
            String str = TAG;
            LogUtil.e(str, "Failed to link death for recipient. " + e);
            throw new IllegalStateException("XUIServiceNotConnected");
        }
    }

    public void unregisterTwilightStateListener(ITwilightStateListener listener) throws RemoteException {
        if (listener == null) {
            return;
        }
        IBinder listenerBinder = listener.asBinder();
        if (this.iTwilightStateListenerMap.containsKey(listenerBinder)) {
            unregisterTwilightListenerLocked(listenerBinder);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class TwilightDeathRecipient implements IBinder.DeathRecipient {
        private final IBinder mListenerBinder;

        public TwilightDeathRecipient(IBinder mListenerBinder) {
            this.mListenerBinder = mListenerBinder;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            ConditionService.this.unregisterTwilightListenerLocked(this.mListenerBinder);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void release() {
            this.mListenerBinder.unlinkToDeath(this, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterTwilightListenerLocked(IBinder listenerBinder) {
        ConditionLocation conditionLocation;
        TwilightDeathRecipient deathRecipient;
        if (this.iTwilightStateListenerMap.remove(listenerBinder) != null && (deathRecipient = this.iTwilightdeathRecipientMap.remove(listenerBinder)) != null) {
            deathRecipient.release();
        }
        if (this.iTwilightStateListenerMap.isEmpty() && (conditionLocation = this.conditionLocation) != null) {
            conditionLocation.stopWatch();
        }
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void init() {
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void release() {
        ConditionLocation conditionLocation = this.conditionLocation;
        if (conditionLocation != null) {
            conditionLocation.removeConditionChangeListener(this);
            this.conditionLocation.stopWatch();
            this.conditionLocation = null;
        }
        this.iTwilightdeathRecipientMap.values().forEach(new Consumer() { // from class: com.xiaopeng.xuiservice.condition.-$$Lambda$i0S-Cfq4ubDcKu3pdGJWHhhQqUc
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ConditionService.TwilightDeathRecipient) obj).release();
            }
        });
        this.iTwilightdeathRecipientMap.clear();
        this.iTwilightStateListenerMap.clear();
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void dump(PrintWriter pw) {
        pw.println("dump-" + TAG);
        pw.println("TODO");
        pw.println("dump-" + TAG + ",end");
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.Condition.ConditionChangeListener
    public void onConditionChange(Condition condition) {
        if (condition instanceof ConditionLocation) {
            try {
                TwilightState twilightState = getTwilightState();
                if (twilightState != null) {
                    for (ITwilightStateListener l : this.iTwilightStateListenerMap.values()) {
                        try {
                            l.onTwilightStateChanged(twilightState);
                        } catch (Exception e) {
                            String str = TAG;
                            LogUtil.e(str, "onTwilightStateChanged error, " + l);
                        }
                    }
                }
            } catch (Exception e2) {
                String str2 = TAG;
                LogUtil.e(str2, "notify twilight state update error, " + e2.getMessage());
            }
        }
    }

    public int getBrightnessLevel() throws RemoteException {
        String str = TAG;
        LogUtil.d(str, "getBrightnessLevel " + this.mBrightnessLevel);
        return this.mBrightnessLevel;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class BrightnessLevelDeathRecipient implements IBinder.DeathRecipient {
        private final IBinder mListenerBinder;

        public BrightnessLevelDeathRecipient(IBinder mListenerBinder) {
            this.mListenerBinder = mListenerBinder;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            ConditionService.this.unregisterBrightnessLevelListenerLocked(this.mListenerBinder);
        }

        void release() {
            this.mListenerBinder.unlinkToDeath(this, 0);
        }
    }

    public void unregisterBrightnessLevelListener(IBrightnessLevelListener listener) throws RemoteException {
        if (listener == null) {
            return;
        }
        String str = TAG;
        LogUtil.d(str, "unregisterBrightnessLevelListener " + listener);
        IBinder listenerBinder = listener.asBinder();
        if (this.iBrightnessLevelListenerMap.containsKey(listenerBinder)) {
            unregisterBrightnessLevelListenerLocked(listenerBinder);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterBrightnessLevelListenerLocked(IBinder listenerBinder) {
        BrightnessLevelDeathRecipient deathRecipient;
        if (this.iBrightnessLevelListenerMap.remove(listenerBinder) != null && (deathRecipient = this.iBrightnessLeveldeathRecipientMap.remove(listenerBinder)) != null) {
            deathRecipient.release();
        }
    }

    public void registerBrightnessLevelListener(IBrightnessLevelListener listener) throws RemoteException {
        IBinder listenerBinder = listener.asBinder();
        String str = TAG;
        LogUtil.d(str, "registerBrightnessLevelListener " + listener);
        if (this.iBrightnessLevelListenerMap.containsKey(listenerBinder)) {
            return;
        }
        BrightnessLevelDeathRecipient deathRecipient = new BrightnessLevelDeathRecipient(listenerBinder);
        try {
            listenerBinder.linkToDeath(deathRecipient, 0);
            this.iBrightnessLeveldeathRecipientMap.put(listenerBinder, deathRecipient);
            this.iBrightnessLevelListenerMap.put(listenerBinder, listener);
        } catch (RemoteException e) {
            String str2 = TAG;
            LogUtil.e(str2, "Failed to link death for recipient. " + e);
            throw new IllegalStateException("XUIServiceNotConnected");
        }
    }

    /* loaded from: classes5.dex */
    private class ConditionObserver extends ContentObserver {
        public ConditionObserver(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, Uri uri) {
            if (ConditionService.URI_LIGHT_INTENSITY.equals(uri)) {
                ConditionService.this.onLightIntensityChanged(selfChange);
            }
        }
    }

    public synchronized void onLightIntensityChanged(boolean selfChange) {
        int brightness = Settings.System.getIntForUser(this.mResolver, LIGHT_INTENSITY_KEY, -1, -2);
        int brightnessLevel = brightnessToLevel(brightness);
        String str = TAG;
        LogUtil.d(str, "onLightIntensityChanged brightness " + brightness + " currentLevel " + brightnessLevel + " lastLevel " + this.mBrightnessLevel);
        if (this.mBrightnessLevel != brightnessLevel) {
            int lastLevel = this.mBrightnessLevel;
            this.mBrightnessLevel = brightnessLevel;
            for (IBrightnessLevelListener l : this.iBrightnessLevelListenerMap.values()) {
                try {
                    l.onBrightnessLevelChanged(lastLevel, this.mBrightnessLevel);
                } catch (Exception e) {
                    String str2 = TAG;
                    LogUtil.e(str2, "onBrightnessLevelChanged error, " + l);
                }
            }
        }
    }

    private int brightnessToLevel(int brightness) {
        if (brightness < 0) {
            return -1;
        }
        if (brightness <= 20) {
            return 0;
        }
        if (brightness <= 40) {
            return 1;
        }
        if (brightness <= 70) {
            return 2;
        }
        if (brightness > 100) {
            return -1;
        }
        return 3;
    }
}
