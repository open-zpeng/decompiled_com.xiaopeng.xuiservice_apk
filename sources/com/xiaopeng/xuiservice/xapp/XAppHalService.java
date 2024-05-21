package com.xiaopeng.xuiservice.xapp;

import android.app.ActivityThread;
import android.app.AppGlobals;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import com.alipay.mobile.aromeservice.RequestParams;
import com.xiaopeng.app.xpPackageInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.HalServiceBase;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.xapp.mode.XAppAudioFocusModeEngine;
import com.xiaopeng.xuiservice.xapp.mode.XAppBaseModeEngine;
import com.xiaopeng.xuiservice.xapp.mode.XAppGameModeEngine;
import com.xiaopeng.xuiservice.xapp.mode.XAppInputSourcePolicyEngine;
import com.xiaopeng.xuiservice.xapp.mode.XAppNavigationModeEngine;
import com.xiaopeng.xuiservice.xapp.mode.octopus.XAppOctopusModeEngine;
import com.xiaopeng.xuiservice.xapp.store.AppStoreStatusProvider;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class XAppHalService extends HalServiceBase {
    private static final String TAG = "XAppHalService";
    private Map<Class<?>, XAppBaseModeEngine> mAppModeEngines;
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    private XAppHalListener mListener;
    private String[] mPackageNames;
    private xpPackageInfo[] mSharedXpPackageInfos;
    private ShowToastReceiver mShowToastReceiver;
    private String[] mTopActivities;
    private XAppAudioFocusModeEngine mXAppAudioFocusModeEngine;
    private XAppCarService mXAppCarService;
    private XAppGameModeEngine mXAppGameModeEngine;
    private XAppInputSourcePolicyEngine mXAppInputSourcePolicyEngine;
    private XAppManagerService mXAppManagerService;
    private XAppNavigationModeEngine mXAppNavigationModeEngine;
    private XAppOctopusModeEngine mXAppOctopusModeEngine;
    private XAppStartManager mXAppStartManager;

    /* loaded from: classes5.dex */
    public interface XAppHalListener {
        void onError(int i, int i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class XAppHalServiceHolder {
        public static final XAppHalService sInstance = new XAppHalService();

        private XAppHalServiceHolder() {
        }
    }

    private XAppHalService() {
        this.mAppModeEngines = new HashMap();
        LogUtil.d(TAG, "started XAppHalService!");
        initHandler();
        this.mXAppStartManager = XAppStartManager.getInstance();
        this.mXAppCarService = XAppCarService.getInstance(ActivityThread.currentActivityThread().getApplication());
        this.mXAppCarService.start();
        this.mXAppGameModeEngine = new XAppGameModeEngine(ActivityThread.currentActivityThread().getApplication(), this.mHandler);
        this.mXAppNavigationModeEngine = new XAppNavigationModeEngine(ActivityThread.currentActivityThread().getApplication(), this.mHandler);
        this.mXAppAudioFocusModeEngine = new XAppAudioFocusModeEngine(ActivityThread.currentActivityThread().getApplication(), this.mHandler);
        this.mXAppOctopusModeEngine = new XAppOctopusModeEngine(ActivityThread.currentActivityThread().getApplication(), this.mHandler);
        this.mXAppInputSourcePolicyEngine = new XAppInputSourcePolicyEngine(ActivityThread.currentActivityThread().getApplication(), this.mHandler);
        this.mAppModeEngines.put(XAppGameModeEngine.class, this.mXAppGameModeEngine);
        this.mAppModeEngines.put(XAppNavigationModeEngine.class, this.mXAppNavigationModeEngine);
        this.mAppModeEngines.put(XAppAudioFocusModeEngine.class, this.mXAppAudioFocusModeEngine);
        this.mAppModeEngines.put(XAppOctopusModeEngine.class, this.mXAppOctopusModeEngine);
        this.mAppModeEngines.put(XAppInputSourcePolicyEngine.class, this.mXAppInputSourcePolicyEngine);
        XAppManagerService xAppManagerService = this.mXAppManagerService;
        this.mXAppManagerService = XAppManagerService.getInstance();
        this.mShowToastReceiver = new ShowToastReceiver();
        this.mSharedXpPackageInfos = new xpPackageInfo[2];
        this.mPackageNames = new String[]{"", ""};
        this.mTopActivities = new String[]{"", ""};
    }

    private void initHandler() {
        this.mHandlerThread = new HandlerThread(TAG);
        this.mHandlerThread.start();
        this.mHandler = new Handler(this.mHandlerThread.getLooper()) { // from class: com.xiaopeng.xuiservice.xapp.XAppHalService.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
    }

    public static XAppHalService getInstance() {
        return XAppHalServiceHolder.sInstance;
    }

    public void setListener(XAppHalListener listener) {
        synchronized (this) {
            this.mListener = listener;
        }
    }

    @Override // com.xiaopeng.xuiservice.HalServiceBase
    public void init() {
        LogUtil.d(TAG, "init()");
        for (XAppBaseModeEngine engine : this.mAppModeEngines.values()) {
            engine.init();
        }
        XAppStartManager xAppStartManager = this.mXAppStartManager;
        if (xAppStartManager != null) {
            xAppStartManager.init();
        }
        this.mShowToastReceiver.register();
        initAppStatusProvider();
        SharedDisplayManager.getInstance().registerListener(new SharedDisplayManager.ISharedDisplayEventListener() { // from class: com.xiaopeng.xuiservice.xapp.XAppHalService.2
            @Override // com.xiaopeng.xuiservice.utils.SharedDisplayManager.ISharedDisplayEventListener
            public void onActivityChanged(int screenId, String property) {
                String packageName = "";
                try {
                    JSONObject jsonObject = new JSONObject(property);
                    packageName = jsonObject.optString(RequestParams.REQUEST_KEY_PACKAGE_NAME);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                xpPackageInfo packageInfo = XAppHalService.this.getXpPackageInfo(packageName);
                XAppHalService.this.mSharedXpPackageInfos[screenId] = packageInfo;
                XAppHalService.this.mPackageNames[screenId] = packageName;
                XAppHalService.this.mTopActivities[screenId] = property;
                XAppHalService xAppHalService = XAppHalService.this;
                xAppHalService.onSharedPackageInfoChanged(xAppHalService.mSharedXpPackageInfos);
                XAppHalService xAppHalService2 = XAppHalService.this;
                xAppHalService2.onSharedActivityChanged(xAppHalService2.mPackageNames, XAppHalService.this.mSharedXpPackageInfos, XAppHalService.this.mTopActivities, false, screenId);
            }

            @Override // com.xiaopeng.xuiservice.utils.SharedDisplayManager.ISharedDisplayEventListener
            public void onChanged(String pkgName, int sharedId) {
                int fromSharedId = Math.abs(sharedId - 1);
                if (XAppHalService.this.mSharedXpPackageInfos[fromSharedId] != null && XAppHalService.this.mSharedXpPackageInfos[fromSharedId].packageName.equals(pkgName)) {
                    xpPackageInfo packageInfo = XAppHalService.this.mSharedXpPackageInfos[fromSharedId];
                    XAppHalService.this.mSharedXpPackageInfos[fromSharedId] = null;
                    XAppHalService.this.mSharedXpPackageInfos[sharedId] = packageInfo;
                    XAppHalService.this.mPackageNames[fromSharedId] = "";
                    XAppHalService.this.mPackageNames[sharedId] = pkgName;
                    XAppHalService xAppHalService = XAppHalService.this;
                    xAppHalService.onSharedPackageInfoChanged(xAppHalService.mSharedXpPackageInfos);
                    XAppHalService xAppHalService2 = XAppHalService.this;
                    xAppHalService2.onSharedActivityChanged(xAppHalService2.mPackageNames, XAppHalService.this.mSharedXpPackageInfos, XAppHalService.this.mTopActivities, true, sharedId);
                }
            }
        });
    }

    private void initAppStatusProvider() {
        if (BroadcastManager.isLockedBootComplete()) {
            LogUtil.w(TAG, "lockedBootComplete!");
            AppStoreStatusProvider.getInstance();
        }
        BroadcastManager.getInstance().addLockedBootCompleteTask(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.-$$Lambda$XAppHalService$SlyR5jeytsing5X-lLt0xcwKDi8
            @Override // java.lang.Runnable
            public final void run() {
                XAppHalService.lambda$initAppStatusProvider$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$initAppStatusProvider$0() {
        LogUtil.w(TAG, "receiver LockedBootComplete!");
        AppStoreStatusProvider.getInstance();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSharedPackageInfoChanged(xpPackageInfo[] packageInfos) {
        for (XAppBaseModeEngine engine : this.mAppModeEngines.values()) {
            engine.onSharedPackageInfoChanged(packageInfos);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSharedActivityChanged(String[] packageNames, xpPackageInfo[] packageInfos, String[] topActivities, boolean sharedEvent, int displayId) {
        this.mXAppManagerService.onSharedActivityChanged(packageNames, packageInfos, topActivities, sharedEvent, displayId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public xpPackageInfo getXpPackageInfo(String pkgName) {
        if (!TextUtils.isEmpty(pkgName) && !pkgName.startsWith("com.xiaopeng")) {
            try {
                return AppGlobals.getPackageManager().getXpPackageInfo(pkgName);
            } catch (RemoteException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override // com.xiaopeng.xuiservice.HalServiceBase
    public void release() {
        LogUtil.d(TAG, "release()");
        this.mListener = null;
        for (XAppBaseModeEngine engine : this.mAppModeEngines.values()) {
            engine.release();
        }
        XAppStartManager xAppStartManager = this.mXAppStartManager;
        if (xAppStartManager != null) {
            xAppStartManager.release();
        }
        this.mShowToastReceiver.unRegister();
    }

    public void setAppUsedLimitEnable(boolean enable) {
        this.mXAppStartManager.setAppUsedLimitEnable(enable);
    }

    public boolean getAppUsedLimitEnable() {
        return this.mXAppStartManager.getAppUsedLimitEnable();
    }

    public int getCarGearLevel() {
        return this.mXAppStartManager.getCarGearLevel();
    }

    public int checkAppStartWithPrompt(String packageName) {
        return this.mXAppStartManager.checkAppStartWithPrompt(packageName);
    }

    public int checkAppPolicy(String packageName, Bundle params) {
        return this.mXAppStartManager.checkAppPolicy(packageName, params);
    }

    public void onAppModeChanged(String pkgName, xpPackageInfo info) {
        LogUtil.i(TAG, "onAppModeChanged, pkgName : " + pkgName);
        if (info != null) {
            LogUtil.i(TAG, "onAppModeChanged, info : " + info);
        }
        for (XAppBaseModeEngine engine : this.mAppModeEngines.values()) {
            engine.onAppInfoChange(pkgName, info);
        }
    }

    public int onAppConfigUpload(String pkgName, String config) {
        this.mXAppOctopusModeEngine.onAppConfigUpload(pkgName, config);
        this.mXAppManagerService.updateBasePackageInfo(pkgName, config);
        try {
            AppGlobals.getPackageManager().setXpPackageInfo(pkgName, config);
            return 0;
        } catch (Exception e) {
            LogUtil.w(TAG, e.toString());
            return -1;
        }
    }

    public void updateAppScreenFlag(int gearLevel) {
        try {
            AppGlobals.getPackageManager().updateAppScreenFlag(gearLevel);
        } catch (Exception e) {
            LogUtil.w(TAG, e.toString());
        }
    }

    public int getAppType(String pkgName) {
        return this.mXAppManagerService.getAppType(pkgName);
    }

    public List<String> getInstalledAppList(int appType) {
        return this.mXAppManagerService.getInstalledAppList(appType);
    }

    public void closeCancelableDialog() {
        this.mXAppManagerService.closeCancelableDialog();
    }

    public void handleHalEvents() {
        XAppHalListener listener;
        synchronized (this) {
            listener = this.mListener;
        }
        if (listener != null) {
            dispatchEventToListener(listener);
        }
    }

    private void dispatchEventToListener(XAppHalListener listener) {
        LogUtil.d(TAG, "handleHalEvents event:");
    }

    @Override // com.xiaopeng.xuiservice.HalServiceBase
    public void dump(PrintWriter writer) {
        writer.println("*XApp HAL*");
    }

    public <T extends XAppBaseModeEngine> T getModeEngineImpl(Class<?> clazz) throws IllegalArgumentException {
        T t = (T) this.mAppModeEngines.get(clazz);
        if (!clazz.isInstance(t)) {
            throw new IllegalArgumentException("Unknown engine mode class: " + clazz.getSimpleName());
        }
        return t;
    }
}
