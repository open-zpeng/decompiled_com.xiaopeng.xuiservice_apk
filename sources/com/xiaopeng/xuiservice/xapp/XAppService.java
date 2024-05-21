package com.xiaopeng.xuiservice.xapp;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.vcu.CarVcuManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.WindowManager;
import com.xiaopeng.app.xpAppInfo;
import com.xiaopeng.app.xpPackageInfo;
import com.xiaopeng.speech.speechwidget.SpeechWidget;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuimanager.xapp.IXApp;
import com.xiaopeng.xuimanager.xapp.IXAppEventListener;
import com.xiaopeng.xuimanager.xapp.IXMiniProgEventListener;
import com.xiaopeng.xuimanager.xapp.MiniProgramResponse;
import com.xiaopeng.xuimanager.xapp.XAppManager;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.XUIServiceBase;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.debug.WatchDog;
import com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionWindowPos;
import com.xiaopeng.xuiservice.userscenario.UserScenarioService;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.PackageUtils;
import com.xiaopeng.xuiservice.xapp.XAppHalService;
import com.xiaopeng.xuiservice.xapp.XMiniProgService;
import com.xiaopeng.xuiservice.xapp.speech.XAppSpeechService;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.opencv.videoio.Videoio;
/* loaded from: classes5.dex */
public class XAppService extends IXApp.Stub implements XUIServiceBase, XAppHalService.XAppHalListener, XMiniProgService.XMiniProgHalListener, BroadcastManager.BroadcastListener {
    public static final boolean DBG = true;
    private static final int MINIPROG_EVENT = 1;
    public static final String TAG = "XAPP.SERVICE.XAppService";
    private final Context mContext;
    private XMiniProgService mMiniProg;
    private WindowManager mWindowManager;
    private XAppManagerService mXAppManagerService;
    private XAppSpeechService mXSpeechService;
    private boolean isMiniProgEnable = false;
    protected volatile boolean mMiniProgramStarting = false;
    private final Map<IBinder, IXAppEventListener> mListenersMap = new HashMap();
    private final Map<IBinder, XAppDeathRecipient> mDeathRecipientMap = new HashMap();
    private final Map<IBinder, IXMiniProgEventListener> mMiniProgListenersMap = new HashMap();
    private final XAppHalService mXAppHal = XAppHalService.getInstance();

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        String info = "code=" + code + ",pid=" + Binder.getCallingPid() + "@" + SystemClock.elapsedRealtimeNanos();
        WatchDog.getInstance().binderEnter("XAppService", info);
        try {
            try {
                boolean ret = super.onTransact(code, data, reply, flags);
                return ret;
            } catch (RemoteException e) {
                LogUtil.w(TAG, "onTransact e=" + e + ",code=" + code);
                throw e;
            }
        } finally {
            WatchDog.getInstance().binderLeave("XAppService", info);
        }
    }

    @Override // com.xiaopeng.xuiservice.utils.BroadcastManager.BroadcastListener
    public void onReceive(Context context, Intent intent) {
        LogUtil.i(TAG, "XAppService mReceiver" + intent.getAction());
        String action = intent.getAction();
        if (action.equals("android.intent.action.BOOT_COMPLETED")) {
            LogUtil.i(TAG, "onReceive ACTION_BOOT_COMPLETED");
            initXApp();
        }
    }

    private void initXApp() {
        initMiniProgService();
        this.mXSpeechService = XAppSpeechService.getInstance();
        this.mXSpeechService.start();
        this.mXAppManagerService = XAppManagerService.getInstance();
        this.mXAppManagerService.setXAppService(this);
        this.mXAppManagerService.setupWithMiniProgService(this.mMiniProg);
        initCarGearListener();
    }

    public XAppService(Context context) {
        this.mContext = context;
        this.mWindowManager = (WindowManager) this.mContext.getSystemService(ConditionWindowPos.TYPE);
        if (BroadcastManager.isBootComplete()) {
            initXApp();
            return;
        }
        List<String> filter = new ArrayList<>();
        filter.add("android.intent.action.BOOT_COMPLETED");
        BroadcastManager.getInstance().registerListener(this, filter);
    }

    private void initMiniProgService() {
        XMiniProgService xMiniProgService;
        this.isMiniProgEnable = XUIConfig.isMiniProgramEnable();
        LogUtil.i(TAG, "isMiniProgEnable:" + this.isMiniProgEnable);
        if (this.isMiniProgEnable) {
            this.mMiniProg = new XMiniProgService(this.mContext);
            if (!this.mMiniProgListenersMap.isEmpty() && (xMiniProgService = this.mMiniProg) != null) {
                xMiniProgService.registerMiniProgListener(this);
            }
        }
    }

    private void initCarGearListener() {
        CarClientManager.getInstance().addVcuManagerListener(new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xuiservice.xapp.XAppService.1
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557847045) {
                    LogUtil.e(XAppService.TAG, "updateAppScreenFlag :" + value.getValue());
                    XAppService.this.mXAppHal.updateAppScreenFlag(((Integer) value.getValue()).intValue());
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        });
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public synchronized void init() {
        if (this.mMiniProg != null) {
            this.mMiniProg.init();
        }
        if (this.mXAppHal != null) {
            this.mXAppHal.init();
        }
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public synchronized void release() {
        for (XAppDeathRecipient recipient : this.mDeathRecipientMap.values()) {
            recipient.release();
        }
        this.mDeathRecipientMap.clear();
        this.mListenersMap.clear();
        if (this.mMiniProg != null) {
            this.mMiniProg.release();
        }
        if (this.mXAppHal != null) {
            this.mXAppHal.release();
        }
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void dump(PrintWriter writer) {
    }

    public synchronized void registerListener(IXAppEventListener listener) {
        LogUtil.d(TAG, "registerListener");
        if (listener == null) {
            LogUtil.e(TAG, "registerListener: Listener is null.");
            throw new IllegalArgumentException("listener cannot be null.");
        }
        IBinder listenerBinder = listener.asBinder();
        if (this.mListenersMap.containsKey(listenerBinder)) {
            return;
        }
        XAppDeathRecipient deathRecipient = new XAppDeathRecipient(listenerBinder);
        try {
            listenerBinder.linkToDeath(deathRecipient, 0);
            this.mDeathRecipientMap.put(listenerBinder, deathRecipient);
            if (this.mListenersMap.isEmpty()) {
                this.mXAppHal.setListener(this);
            }
            this.mListenersMap.put(listenerBinder, listener);
        } catch (RemoteException e) {
            LogUtil.e(TAG, "Failed to link death for recipient. " + e);
            throw new IllegalStateException("XUIServiceNotConnected");
        }
    }

    public synchronized void unregisterListener(IXAppEventListener listener) {
        LogUtil.d(TAG, "unregisterListener");
        if (listener == null) {
            LogUtil.e(TAG, "unregisterListener: listener was not registered");
            return;
        }
        IBinder listenerBinder = listener.asBinder();
        if (!this.mListenersMap.containsKey(listenerBinder)) {
            LogUtil.e(TAG, "unregisterListener: Listener was not previously registered.");
        }
        unregisterListenerLocked(listenerBinder);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterListenerLocked(IBinder listenerBinder) {
        Object status = this.mListenersMap.remove(listenerBinder);
        if (status != null) {
            this.mDeathRecipientMap.get(listenerBinder).release();
            this.mDeathRecipientMap.remove(listenerBinder);
        }
        if (this.mListenersMap.isEmpty()) {
            this.mXAppHal.setListener(null);
        }
    }

    public void setAppUsedLimitEnable(boolean enable) {
        this.mXAppHal.setAppUsedLimitEnable(enable);
    }

    public boolean getAppUsedLimitEnable() {
        return this.mXAppHal.getAppUsedLimitEnable();
    }

    public int getCarGearLevel() {
        return this.mXAppHal.getCarGearLevel();
    }

    public int checkAppStart(String packageName) {
        return this.mXAppHal.checkAppStartWithPrompt(packageName);
    }

    public int checkAppPolicy(String packageName, Bundle params) {
        return this.mXAppHal.checkAppPolicy(packageName, params);
    }

    public void onAppModeChanged(String pkgName, xpPackageInfo info) {
        this.mXAppHal.onAppModeChanged(pkgName, info);
    }

    public int onAppConfigUpload(String pkgName, String config) {
        MediaCenterHalService.getInstance().updateAppConfig(pkgName, config);
        return this.mXAppHal.onAppConfigUpload(pkgName, config);
    }

    @Override // com.xiaopeng.xuiservice.xapp.XAppHalService.XAppHalListener
    public void onError(int errorCode, int operation) {
    }

    public synchronized void registerMiniProgListener(IXMiniProgEventListener listener) {
        LogUtil.d(TAG, "registerMiniProgListener");
        if (listener == null) {
            LogUtil.e(TAG, "registerMiniProgListener: Listener is null.");
            throw new IllegalArgumentException("listener cannot be null.");
        }
        IBinder listenerBinder = listener.asBinder();
        if (this.mMiniProgListenersMap.containsKey(listenerBinder)) {
            return;
        }
        XAppDeathRecipient deathRecipient = new XAppDeathRecipient(listenerBinder);
        try {
            listenerBinder.linkToDeath(deathRecipient, 0);
            this.mDeathRecipientMap.put(listenerBinder, deathRecipient);
            if (this.mMiniProgListenersMap.isEmpty() && this.mMiniProg != null) {
                this.mMiniProg.registerMiniProgListener(this);
            }
            this.mMiniProgListenersMap.put(listenerBinder, listener);
        } catch (RemoteException e) {
            LogUtil.e(TAG, "Failed to link death for recipient. " + e);
            throw new IllegalStateException("XUIServiceNotConnected");
        }
    }

    public synchronized void unregisterMiniProgListener(IXMiniProgEventListener listener) {
        LogUtil.d(TAG, "unregisterMiniProgListener");
        if (listener == null) {
            LogUtil.e(TAG, "unregisterMiniProgListener: listener was not registered");
            return;
        }
        IBinder listenerBinder = listener.asBinder();
        if (!this.mMiniProgListenersMap.containsKey(listenerBinder)) {
            LogUtil.e(TAG, "unregisterListener: Listener was not previously registered.");
        }
        unregisterMiniProgListenerLocked(listenerBinder);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterMiniProgListenerLocked(IBinder listenerBinder) {
        Object status = this.mMiniProgListenersMap.remove(listenerBinder);
        if (status != null) {
            this.mDeathRecipientMap.get(listenerBinder).release();
            this.mDeathRecipientMap.remove(listenerBinder);
        }
        if (this.mMiniProgListenersMap.isEmpty()) {
            this.mMiniProg.registerMiniProgListener(null);
        }
    }

    public void startMiniProgram(String id, String name, Map params) {
        XMiniProgService xMiniProgService = this.mMiniProg;
        if (xMiniProgService != null) {
            xMiniProgService.startMiniProgram(id, name, params);
            this.mMiniProgramStarting = true;
        }
    }

    public void attachContext() {
        XMiniProgService xMiniProgService = this.mMiniProg;
        if (xMiniProgService != null) {
            xMiniProgService.attachContext();
        }
    }

    public void initService() {
        XMiniProgService xMiniProgService = this.mMiniProg;
        if (xMiniProgService != null) {
            xMiniProgService.initService();
        }
    }

    public boolean isServiceOnline() {
        XMiniProgService xMiniProgService = this.mMiniProg;
        if (xMiniProgService != null) {
            return xMiniProgService.isServiceOnline();
        }
        return false;
    }

    public void activeArome(Map params) {
        XMiniProgService xMiniProgService = this.mMiniProg;
        if (xMiniProgService != null) {
            xMiniProgService.activeArome(params);
        }
    }

    public void loginAccount() {
        XMiniProgService xMiniProgService = this.mMiniProg;
        if (xMiniProgService != null) {
            xMiniProgService.loginAccount();
        }
    }

    public void logoutAccount() {
        XMiniProgService xMiniProgService = this.mMiniProg;
        if (xMiniProgService != null) {
            xMiniProgService.logoutAccount();
        }
    }

    public void exitApp(String id) {
        XMiniProgService xMiniProgService = this.mMiniProg;
        if (xMiniProgService != null) {
            xMiniProgService.exitApp(id);
        }
    }

    public void preloadApp(String appId, boolean loadToMemory) {
        XMiniProgService xMiniProgService = this.mMiniProg;
        if (xMiniProgService != null) {
            xMiniProgService.preloadApp(appId, loadToMemory);
        }
    }

    public void requestLoginInfo() {
        XMiniProgService xMiniProgService = this.mMiniProg;
        if (xMiniProgService != null) {
            xMiniProgService.requestLoginInfo();
        }
    }

    public void requestMiniList(String alipayVerison) {
        XMiniProgService xMiniProgService = this.mMiniProg;
        if (xMiniProgService != null) {
            xMiniProgService.requestMiniList(alipayVerison);
        }
    }

    public void uploadAlipayLog() {
        XMiniProgService xMiniProgService = this.mMiniProg;
        if (xMiniProgService != null) {
            xMiniProgService.uploadAlipayLog();
        }
    }

    public void checkIdentityValid(String userKey) {
        XMiniProgService xMiniProgService = this.mMiniProg;
        if (xMiniProgService != null) {
            xMiniProgService.checkIdentityValid(userKey);
        }
    }

    public void startCustomService(String serviceCode, String userIdentity) {
        XMiniProgService xMiniProgService = this.mMiniProg;
        if (xMiniProgService != null) {
            xMiniProgService.startCustomService(serviceCode, userIdentity);
        }
    }

    public boolean checkOrderValid(String orderId) {
        XMiniProgService xMiniProgService = this.mMiniProg;
        if (xMiniProgService != null) {
            return xMiniProgService.checkOrderValid(orderId);
        }
        return false;
    }

    public int startXpApp(String uriParam, Intent intent) {
        String callingApp = PackageUtils.getProcessNameByPid(this.mContext, Binder.getCallingPid());
        WindowManager windowManager = this.mWindowManager;
        int screenId = windowManager != null ? windowManager.getScreenId(callingApp) : 0;
        startXpApp(uriParam, screenId);
        return 0;
    }

    public void startXpApp(String uriParam, int displayId) {
        Intent i = new Intent();
        i.addFlags(270532608);
        Uri uri = Uri.parse("xiaopeng://napa/show");
        i.setData(uri);
        Bundle bundle = new Bundle();
        String targetUri = "xiaopeng://napa/show?" + uriParam;
        Uri uriWhole = Uri.parse(targetUri);
        if (uriWhole != null && "xiaopeng".equals(uriWhole.getScheme()) && "napa".equals(uriWhole.getAuthority())) {
            String appID = uriWhole.getQueryParameter("appID");
            String appPage = uriWhole.getQueryParameter("page");
            String extra = uriWhole.getQueryParameter(SpeechWidget.WIDGET_EXTRA);
            String resizeable = uriWhole.getQueryParameter("resizeable");
            if (extra != null) {
                appPage = appPage + "&extra=" + extra;
            }
            if (resizeable != null) {
                appPage = appPage + "&resizeable=" + resizeable;
            }
            String appPage2 = appPage;
            try {
                LogUtil.i(TAG, "XAppService.startXpApp,displayId=" + displayId + " appPage=" + appPage2 + " extra=" + extra + " uriParam=" + uriParam);
                List<xpAppInfo> infoList = XAppManager.getXpAppPackageList(displayId);
                if (infoList != null && !infoList.isEmpty()) {
                    for (xpAppInfo info : infoList) {
                        List<xpAppInfo> infoList2 = infoList;
                        if (appID.equals(info.mXpAppId) && appPage2.equals(info.mXpAppPage) && info.mLaunchMode != 0) {
                            LogUtil.i(TAG, "startXpApp,launch mode is not default:" + info.mLaunchMode + ",displayId=" + displayId);
                            launchNapaApp(info, displayId);
                            return;
                        }
                        infoList = infoList2;
                    }
                }
            } catch (Exception e2) {
                LogUtil.w(TAG, "startXpApp,getXpAppPackageList e=" + e2);
            }
        }
        bundle.putString("uri", targetUri);
        bundle.putInt("screen_id", displayId);
        bundle.putString("action", "android.intent.action.VIEW");
        i.putExtras(bundle);
        LogUtil.i(TAG, "startXpApp,targetUri=" + targetUri + ",intent=" + i + ",displayId=" + displayId);
        try {
            if (this.mListenersMap != null && !this.mListenersMap.isEmpty()) {
                Bundle extras = new Bundle();
                extras.putParcelable("intent", i);
                for (IXAppEventListener listener : this.mListenersMap.values()) {
                    if (listener != null) {
                        listener.onStartAppEvent(extras);
                    }
                }
            } else {
                LogUtil.w(TAG, "startXpApp,mListenersMap null or empty");
            }
        } catch (Exception e) {
        }
        LogUtil.d(TAG, "startXpApp done");
    }

    public int getAppType(String pkgName) {
        return this.mXAppHal.getAppType(pkgName);
    }

    public List<String> getInstalledAppList(int appType) {
        return this.mXAppHal.getInstalledAppList(appType);
    }

    public void closeCancelableDialog() {
        this.mXAppHal.closeCancelableDialog();
    }

    @Override // com.xiaopeng.xuiservice.xapp.XMiniProgService.XMiniProgHalListener
    public void onMiniProgCallBack(int type, MiniProgramResponse response) {
        String tagResponse = response == null ? "" : response.toString();
        LogUtil.d(TAG, "onMiniProgCallBack response:" + tagResponse);
        if (!this.mMiniProgListenersMap.isEmpty()) {
            for (IXMiniProgEventListener l : this.mMiniProgListenersMap.values()) {
                try {
                    l.onMiniProgCallBack(type, response);
                } catch (Exception e) {
                    LogUtil.d(TAG, "onMiniProgCallBack Exception:" + e.toString());
                }
            }
        }
    }

    private void dispatchMiniProgCallback(String id, Map data) {
    }

    public int getCurrentMiniAppStatus(String ali_id) {
        XMiniProgService xMiniProgService = this.mMiniProg;
        if (xMiniProgService != null) {
            return xMiniProgService.getCurrentMiniAppStatus(ali_id);
        }
        return 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public class XAppDeathRecipient implements IBinder.DeathRecipient {
        private static final String TAG = "XAPP.SERVICE.XAppService.XAppDeathRecipient";
        private IBinder mListenerBinder;

        XAppDeathRecipient(IBinder listenerBinder) {
            this.mListenerBinder = listenerBinder;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            LogUtil.d(TAG, "binderDied " + this.mListenerBinder);
            XAppService.this.unregisterListenerLocked(this.mListenerBinder);
            XAppService.this.unregisterMiniProgListenerLocked(this.mListenerBinder);
        }

        void release() {
            this.mListenerBinder.unlinkToDeath(this, 0);
        }
    }

    private void launchNapaApp(xpAppInfo info, int displayId) {
        int i = info.mLaunchMode;
        if (i == 1) {
            launchUserScenario(info.mLaunchParam, displayId);
        } else if (i == 2) {
            String packageName = info.mXpAppId;
            String mainAct = info.mXpAppPage;
            String mUri = null;
            String mresizeable = null;
            String isresizeable = null;
            String[] args = mainAct.split("&", 0);
            if (args != null) {
                mainAct = args[0];
                if (args.length > 1 && args[1].startsWith(SpeechWidget.WIDGET_EXTRA) && args[1].split("=", 0).length > 1) {
                    mUri = args[1].split("=", 0)[1];
                }
                if (args.length > 2 && args[2].startsWith("resizeable") && args[2].split("=", 0).length > 1) {
                    mresizeable = args[2].split("=", 0)[0];
                    isresizeable = args[2].split("=", 0)[1];
                }
            }
            String mLaunchParam = info.mLaunchParam;
            String[] argsLaunchParam = mLaunchParam.split("=", 0);
            Intent intent = new Intent();
            if (argsLaunchParam != null && argsLaunchParam[0].equals("isspecialapp") && argsLaunchParam.length == 4) {
                intent.setAction(argsLaunchParam[3]);
                intent.setFlags(Videoio.CAP_INTELPERC_IMAGE_GENERATOR);
                intent.putExtra(argsLaunchParam[1], argsLaunchParam[2]);
            } else {
                intent.setAction("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.LAUNCHER");
                intent.setFlags(270532608);
            }
            if (!TextUtils.isEmpty(packageName) && !TextUtils.isEmpty(mainAct)) {
                intent.setComponent(new ComponentName(packageName, mainAct));
            } else if (TextUtils.isEmpty(packageName)) {
                LogUtil.w(TAG, "launchNapaApp the packageName is null");
                return;
            } else {
                PackageManager mPackageManager = this.mContext.getPackageManager();
                intent = mPackageManager.getLaunchIntentForPackage(packageName);
                if (TextUtils.isEmpty(mainAct)) {
                    LogUtil.w(TAG, "launchNapaApp the mainAct is null");
                }
            }
            if (mUri != null && mresizeable != null) {
                Uri content_url = Uri.parse(mUri);
                intent.setData(content_url);
                intent.putExtra(mresizeable, Boolean.parseBoolean(isresizeable));
            }
            intent.setScreenId(displayId);
            LogUtil.d(TAG, "launchNapaApp next will start 2DActivity displayId=" + displayId + ",info.mLaunchMode= " + info.mLaunchMode + " ,info.mLaunchParam=" + info.mLaunchParam + ",info.mXpAppId=" + info.mXpAppId + ",info.mXpAppPage=" + info.mXpAppPage);
            this.mContext.startActivity(intent);
        }
    }

    private void launchUserScenario(String scenario, int displayId) {
        if (scenario != null) {
            LogUtil.i(TAG, "launchUserScenario:" + scenario);
            JSONObject obj = new JSONObject();
            try {
                obj.put("screenId", displayId);
            } catch (Exception e) {
                LogUtil.w(TAG, "launchUserScenario e=" + e);
            }
            UserScenarioService.getInstance().enterUserScenarioWithExtra(scenario, "activity", obj.toString());
            return;
        }
        LogUtil.w(TAG, "launchUserScenario,null scenarioName:" + scenario);
    }
}
