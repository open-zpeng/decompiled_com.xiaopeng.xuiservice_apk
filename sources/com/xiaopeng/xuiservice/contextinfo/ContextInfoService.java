package com.xiaopeng.xuiservice.contextinfo;

import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import com.xiaopeng.xuimanager.contextinfo.Camera;
import com.xiaopeng.xuimanager.contextinfo.CameraInterval;
import com.xiaopeng.xuimanager.contextinfo.Cross;
import com.xiaopeng.xuimanager.contextinfo.HomeCompanyRouteInfo;
import com.xiaopeng.xuimanager.contextinfo.IContextInfo;
import com.xiaopeng.xuimanager.contextinfo.IContextInfoEventListener;
import com.xiaopeng.xuimanager.contextinfo.Lane;
import com.xiaopeng.xuimanager.contextinfo.Location;
import com.xiaopeng.xuimanager.contextinfo.Maneuver;
import com.xiaopeng.xuimanager.contextinfo.Navi;
import com.xiaopeng.xuimanager.contextinfo.NaviStatus;
import com.xiaopeng.xuimanager.contextinfo.RemainInfo;
import com.xiaopeng.xuimanager.contextinfo.Sapa;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.XUIServiceBase;
import com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import com.xiaopeng.xuiservice.debug.WatchDog;
import com.xiaopeng.xuiservice.utils.CommonUtils;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: classes5.dex */
public class ContextInfoService extends IContextInfo.Stub implements XUIServiceBase, ContextInfoHalService.ContextInfoHalListener {
    private static final String ChaoPaoAppName = "com.car.chaopaoshenglangxp";
    public static final boolean DBG = true;
    public static final String TAG = "CONTEXTINFO.SERVICE.ContextInfoService";
    private static IBinder chaopaoBinder;
    private static ContextInfoService mContextInfoService = null;
    private final Context mContext;
    private final ContextInfoHalService mContextInfoHal;
    private final Map<IBinder, IContextInfoEventListener> mListenersMap = new ConcurrentHashMap();
    private final Map<IBinder, Integer> mListenersLevelMap = new ConcurrentHashMap();
    private final Map<IBinder, ContextInfoDeathRecipient> mDeathRecipientMap = new HashMap();
    private final Set<IMiniNaviInfoListener> mNaviInfoListenerSet = Collections.synchronizedSet(new HashSet());

    /* loaded from: classes5.dex */
    public interface IMiniNaviInfoListener {
        void onUpdateNaviInfo(String str);
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        String info = "code=" + code + ",pid=" + Binder.getCallingPid() + "@" + SystemClock.elapsedRealtimeNanos();
        WatchDog.getInstance().binderEnter("ContextInfoService", info);
        try {
            try {
                boolean ret = super.onTransact(code, data, reply, flags);
                return ret;
            } catch (RemoteException e) {
                LogUtil.w(TAG, "onTransact e=" + e + ",code=" + code);
                throw e;
            }
        } finally {
            WatchDog.getInstance().binderLeave("ContextInfoService", info);
        }
    }

    public ContextInfoService(Context context) {
        this.mContext = context;
        this.mContextInfoHal = new ContextInfoHalService(context);
        mContextInfoService = this;
        DumpDispatcher.registerDump(XUIConfig.PROPERTY_CONTEXTINFO, new DumpDispatcher.DebugDumper() { // from class: com.xiaopeng.xuiservice.contextinfo.-$$Lambda$ContextInfoService$k_35z1fgBilhDCQc9PhCYykbqzA
            @Override // com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
            public final void debugDump(PrintWriter printWriter, String[] strArr) {
                ContextInfoService.this.lambda$new$0$ContextInfoService(printWriter, strArr);
            }
        });
    }

    public static ContextInfoService getInstance() {
        return mContextInfoService;
    }

    /* renamed from: dump */
    public void lambda$new$0$ContextInfoService(PrintWriter pw, String[] args) {
        pw.println("dump-CONTEXTINFO.SERVICE.ContextInfoService");
        pw.println("IContextInfoEventListenerMaps size:" + this.mListenersMap.size());
        for (IContextInfoEventListener listener : this.mListenersMap.values()) {
            pw.println(listener);
        }
        pw.println("dump-CONTEXTINFO.SERVICE.ContextInfoService,end");
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void init() {
        LogUtil.i(TAG, "init");
        this.mContextInfoHal.init();
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void release() {
        for (ContextInfoDeathRecipient recipient : this.mDeathRecipientMap.values()) {
            recipient.release();
        }
        this.mDeathRecipientMap.clear();
        this.mListenersMap.clear();
        this.mNaviInfoListenerSet.clear();
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void dump(PrintWriter writer) {
    }

    public synchronized void registerListener(IContextInfoEventListener listener) {
        LogUtil.d(TAG, "registerListener");
        registerListenerWithLevel(listener, 0);
    }

    public synchronized void registerListeners(IContextInfoEventListener listener, int level) {
        LogUtil.d(TAG, "registerListener " + level);
        registerListenerWithLevel(listener, level);
    }

    private void registerListenerWithLevel(IContextInfoEventListener listener, int level) {
        if (listener == null) {
            LogUtil.e(TAG, "registerListener: Listener is null.");
            throw new IllegalArgumentException("listener cannot be null.");
        }
        IBinder listenerBinder = listener.asBinder();
        if (this.mListenersMap.containsKey(listenerBinder)) {
            return;
        }
        LogUtil.d(TAG, "registerListenerWithLevel " + CommonUtils.getAppPkg(this.mContext, Binder.getCallingPid(), Binder.getCallingUid()));
        if (CommonUtils.getAppPkg(this.mContext, Binder.getCallingPid(), Binder.getCallingUid()).equals(ChaoPaoAppName)) {
            chaopaoBinder = listenerBinder;
            this.mContextInfoHal.addSpecialFeatureCallback(1);
        }
        ContextInfoDeathRecipient deathRecipient = new ContextInfoDeathRecipient(listenerBinder);
        try {
            listenerBinder.linkToDeath(deathRecipient, 0);
            this.mDeathRecipientMap.put(listenerBinder, deathRecipient);
            if (this.mListenersMap.isEmpty()) {
                this.mContextInfoHal.setListener(this);
            }
            this.mListenersMap.put(listenerBinder, listener);
            this.mListenersLevelMap.put(listenerBinder, Integer.valueOf(level));
        } catch (RemoteException e) {
            LogUtil.e(TAG, "Failed to link death for recipient. " + e);
            throw new IllegalStateException("XUIServiceNotConnected");
        }
    }

    public synchronized void unregisterListener(IContextInfoEventListener listener) {
        LogUtil.d(TAG, "unregisterListener" + CommonUtils.getAppPkg(this.mContext, Binder.getCallingPid(), Binder.getCallingUid()));
        IBinder listenerBinder = listener.asBinder();
        if (!this.mListenersMap.containsKey(listenerBinder)) {
            LogUtil.e(TAG, "unregisterListener: Listener was not previously registered.");
        }
        unregisterListenerLocked(listenerBinder);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterListenerLocked(IBinder listenerBinder) {
        Object status = this.mListenersMap.remove(listenerBinder);
        LogUtil.d(TAG, "unregisterListenerLocked");
        IBinder iBinder = chaopaoBinder;
        if ((listenerBinder == iBinder && iBinder != null) || CommonUtils.getAppPkg(this.mContext, Binder.getCallingPid(), Binder.getCallingUid()).equals(ChaoPaoAppName)) {
            LogUtil.d(TAG, "unregisterListenerLocked com.car.chaopaoshenglangxp");
            this.mContextInfoHal.removeSpecialFeatureCallback(1);
        }
        this.mListenersLevelMap.remove(listenerBinder);
        if (status != null) {
            this.mDeathRecipientMap.get(listenerBinder).release();
            this.mDeathRecipientMap.remove(listenerBinder);
        }
        if (this.mListenersMap.isEmpty()) {
            this.mContextInfoHal.setListener(null);
        }
    }

    private <K, V> K getKeyByLoop(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (Objects.equals(entry.getValue(), value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void setCarSpeed(float speed, int deltaTime) {
        this.mContextInfoHal.setCarSpeed(speed, deltaTime);
    }

    public void setCarAngular(float angularValue, int deltaTime) {
        this.mContextInfoHal.setCarAngular(angularValue, deltaTime);
    }

    public void setCarAngularVelocity(float angularVelocityValue) {
        this.mContextInfoHal.setCarAngularVelocity(angularVelocityValue);
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onCommonEvent(int eventType, int eventValue) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                IBinder mbinder = (IBinder) getKeyByLoop(this.mListenersMap, l);
                if (this.mListenersLevelMap.get(mbinder).intValue() > 0) {
                    l.onCommonEvent(eventType, eventValue);
                }
            } catch (Exception ex) {
                LogUtil.e(TAG, "onCommonEvent calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onAccelerationEvent(float accelerationValue) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onAccelerationEvent(accelerationValue);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onAccelerationEvent calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onAngularVelocityEvent(float angularVelocityValue) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onAngularVelocityEvent(angularVelocityValue);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onAngularVelocityEvent calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onManeuverEvent(Maneuver maneuver, int msgType) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onManeuverEvent(maneuver, msgType);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onManeuverEvent calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onRemainInfoEvent(RemainInfo remainInfo, int msgType) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onRemainInfoEvent(remainInfo, msgType);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onRemainInfoEvent calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onNaviStatus(NaviStatus naviStatus, int msgType) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onNaviStatus(naviStatus, msgType);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onNaviStatus calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onHomeCompanyRouteInfo(HomeCompanyRouteInfo info, int msgType) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onHomeCompanyRouteInfo(info, msgType);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onNaviStatus calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onNaviEvent(Navi navi, int msgType) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onNaviEvent(navi, msgType);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onNaviEvent calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onLaneEvent(Lane lane, int msgType) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onLaneEvent(lane, msgType);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onLaneEvent calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onCameraEvent(Camera camera, int msgType) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onCameraEvent(camera, msgType);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onCameraEvent calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onCameraIntervalEvent(CameraInterval cameraInterval, int msgType) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onCameraIntervalEvent(cameraInterval, msgType);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onCameraIntervalEvent calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onSapaEvent(Sapa sapa, int msgType) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onSapaEvent(sapa, msgType);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onSapaEvent calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onCrossEvent(Cross cross, int msgType) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onCrossEvent(cross, msgType);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onCrossEvent calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onLocationEvent(Location location, int msgType) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onLocationEvent(location, msgType);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onLocationEvent calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onNavigationInfo(String navInfo) {
        Set<IMiniNaviInfoListener> set = this.mNaviInfoListenerSet;
        if (set != null) {
            synchronized (set) {
                for (IMiniNaviInfoListener mIMiniNaviInfoListener : this.mNaviInfoListenerSet) {
                    mIMiniNaviInfoListener.onUpdateNaviInfo(navInfo);
                }
            }
        }
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onNavigationInfo(navInfo);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onNavigationInfo calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onNavigationEnable(boolean enable) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onNavigationEnable(enable);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onNavigationEnable calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onMsgEvent(int msgType) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onMsgEvent(msgType);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onMsgEvent calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onWeatherInfo(String weatherInfo) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onWeatherInfo(weatherInfo);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onWeatherInfo calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onGearChanged(int gear) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onGearChanged(gear);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onGearChanged calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onCarSpeed(float speed) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onCarSpeed(speed);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onCarSpeed calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onIGStatus(int status) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onIGStatus(status);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onIGStatus calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onAutoBrightness(int lux, int autolight) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onAutoBrightness(lux, autolight);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onAutoBrightness calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onXPilotWarning(int type, int status) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onXPilotWarning(type, status);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onXPilotWarning calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onAvpWifi(int status) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onAvpWifi(status);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onAvpWifi calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onRemoteCommand(int remoteCmd) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onRemoteCommand(remoteCmd);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onRemoteCommand calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onLLUStatus(int type, int status) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onLLUStatus(type, status);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onLLUStatus calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onATLSStatus(int type, int status) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onATLSStatus(type, status);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onATLSStatus calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onBeltStatus(int status) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onBeltStatus(status);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onBeltStatus calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onPowerAction(int powerAction) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onPowerAction(powerAction);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onPowerAction calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onChargingGunStatus(int status) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onChargingGunStatus(status);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onChargingGunStatus calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onDeviceChargeStatus(int status) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onDeviceChargeStatus(status);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onDeviceChargeStatus calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onDriverSeatStatus(int status) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onDriverSeatStatus(status);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onDriverSeatStatus calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onBcmLightMode(int type, int mode) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onBcmLightMode(type, mode);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onBcmLightMode calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onVpmEvent(int type, int mode) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onVpmEvent(type, mode);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onVpmEvent calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onPsdMoto(int status) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onPsdMoto(status);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onPsdMoto calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onPowerOffCount(int cnt) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onPowerOffCount(cnt);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onPowerOffCount calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoHalService.ContextInfoHalListener
    public void onError(int errorCode, int operation) {
        for (IContextInfoEventListener l : this.mListenersMap.values()) {
            try {
                l.onError(errorCode, operation);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onError calling failed: " + ex);
            }
        }
    }

    public boolean getIntelligentEffectEnable() {
        return this.mContextInfoHal.getIntelligentEffectEnable();
    }

    public void setIntelligentEffectEnable(boolean enable) {
        this.mContextInfoHal.setIntelligentEffectEnable(enable);
    }

    public void setNavigationInfo(String navInfo) {
        this.mContextInfoHal.setNavigationInfo(navInfo);
    }

    public void setNavigationEnable(boolean enable) {
        this.mContextInfoHal.setNavigationEnable(enable);
    }

    public String getWeather() {
        return this.mContextInfoHal.getWeather();
    }

    public void updateWeatherFromServer() {
        this.mContextInfoHal.updateWeatherFromServer();
    }

    public void sendContextEvent(int eventType, int eventValue) {
        this.mContextInfoHal.sendContextEvent(eventType, eventValue);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public class ContextInfoDeathRecipient implements IBinder.DeathRecipient {
        private static final String TAG = "CONTEXTINFO.SERVICE.ContextInfoService.ContextInfoDeathRecipient";
        private IBinder mListenerBinder;

        ContextInfoDeathRecipient(IBinder listenerBinder) {
            this.mListenerBinder = listenerBinder;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            LogUtil.d(TAG, "binderDied " + this.mListenerBinder);
            ContextInfoService.this.unregisterListenerLocked(this.mListenerBinder);
        }

        void release() {
            this.mListenerBinder.unlinkToDeath(this, 0);
        }
    }

    public void registerMiniNaviInfoListener(IMiniNaviInfoListener miniNaviInfoListener) {
        Set<IMiniNaviInfoListener> set = this.mNaviInfoListenerSet;
        if (set == null || miniNaviInfoListener == null) {
            LogUtil.d(TAG, "registerMiniNaviInfoListener failure ");
        } else {
            set.add(miniNaviInfoListener);
        }
    }

    public void unRegisterMiniNaviInfoListener(IMiniNaviInfoListener miniNaviInfoListener) {
        Set<IMiniNaviInfoListener> set = this.mNaviInfoListenerSet;
        if (set != null && set.contains(miniNaviInfoListener)) {
            this.mNaviInfoListenerSet.remove(miniNaviInfoListener);
        }
    }
}
