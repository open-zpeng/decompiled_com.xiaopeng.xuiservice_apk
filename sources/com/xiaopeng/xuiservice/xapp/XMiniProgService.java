package com.xiaopeng.xuiservice.xapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.mcu.CarMcuManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import com.alipay.arome.aromecli.AromeInit;
import com.alipay.arome.aromecli.AromeServiceInvoker;
import com.alipay.arome.aromecli.AromeServiceTask;
import com.alipay.arome.aromecli.requst.AromeExitAppRequest;
import com.alipay.arome.aromecli.requst.AromeExtendBridgeRequest;
import com.alipay.arome.aromecli.requst.AromeGetAppStatusRequest;
import com.alipay.arome.aromecli.requst.AromeGetUserInfoRequest;
import com.alipay.arome.aromecli.requst.AromeLaunchAppRequest;
import com.alipay.arome.aromecli.requst.AromeLaunchCustomServiceRequest;
import com.alipay.arome.aromecli.requst.AromeLoginRequest;
import com.alipay.arome.aromecli.requst.AromeLogoutRequest;
import com.alipay.arome.aromecli.requst.AromePreloadAppRequest;
import com.alipay.arome.aromecli.requst.AromeUploadLogRequest;
import com.alipay.arome.aromecli.response.AromeGetAppStatusResponse;
import com.alipay.arome.aromecli.response.AromeResponse;
import com.android.internal.content.PackageMonitor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IResponse;
import com.xiaopeng.speech.protocol.bean.stats.SkillStatisticsBean;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuimanager.xapp.MiniProgramResponse;
import com.xiaopeng.xuiservice.carcontrol.HalServiceBaseCarListener;
import com.xiaopeng.xuiservice.contextinfo.ContextInfoService;
import com.xiaopeng.xuiservice.contextinfo.IHttpManager;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import com.xiaopeng.xuiservice.xapp.miniprog.AlipayConstants;
import com.xiaopeng.xuiservice.xapp.miniprog.ApiConstants;
import com.xiaopeng.xuiservice.xapp.miniprog.AromeParam;
import com.xiaopeng.xuiservice.xapp.miniprog.AromeServiceCallback;
import com.xiaopeng.xuiservice.xapp.miniprog.MiniProgManager;
import com.xiaopeng.xuiservice.xapp.miniprog.MiniProgManagerInitCallBack;
import com.xiaopeng.xuiservice.xapp.miniprog.MiniProgramBeanUtil;
import com.xiaopeng.xuiservice.xapp.miniprog.bean.DeviceData;
import com.xiaopeng.xuiservice.xapp.miniprog.bean.LocationInfo;
import com.xiaopeng.xuiservice.xapp.miniprog.bean.MiniBean;
import com.xiaopeng.xuiservice.xapp.miniprog.bean.MiniConfig;
import com.xiaopeng.xuiservice.xapp.miniprog.bean.MiniUploadData;
import com.xiaopeng.xuiservice.xapp.miniprog.bean.OrderBean;
import com.xiaopeng.xuiservice.xapp.miniprog.manager.HttpManagerUtil;
import com.xiaopeng.xuiservice.xapp.util.GsonUtil;
import com.xiaopeng.xuiservice.xapp.util.WorkThreadUtil;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.opencv.videoio.Videoio;
/* loaded from: classes5.dex */
public class XMiniProgService extends HalServiceBaseCarListener implements OnAccountsUpdateListener {
    private static final boolean DBG = true;
    private static final String LOCATION_INFO_ERROR = "255";
    private static final double MAX_CHINA_LAT = 55.5783446722d;
    private static final double MAX_CHINA_LON = 140.2734375d;
    private static final int MINI_PROG_EVENT = 1;
    private static final double MIN_CHINA_LAT = -0.8788717828d;
    private static final double MIN_CHINA_LON = 69.08203125d;
    private static final String NAVI_CLASS_NAME = "com.xiaopeng.montecarlo.MainActivity";
    private static final String NAVI_PKG_NAME = "com.xiaopeng.montecarlo";
    private static final int RETRY_UPLOAD_INTERVAL = 10000;
    private static final int RETRY_UPLOAD_MAX_TIMES = 3;
    private static final String TAG = "XMiniProgService";
    private static final int UPLOAD_EVENT = 2;
    private static MiniProgEventHandler mHandler;
    private String alipayVersion;
    private AccountManager mAccountManager;
    private CarMcuManager.CarMcuEventCallback mCarMcuEventCallback;
    private Context mContext;
    private ContextInfoService mContextInfoService;
    private Account mCurrentAccount;
    private LocationInfo mLocationInfo;
    private ContextInfoService.IMiniNaviInfoListener mMiniNaviInfoListener;
    private XMiniProgHalListener mMiniProgEventListener;
    private AlipayPackageMonitor mPackageMonitor;
    private int retryTime;

    /* loaded from: classes5.dex */
    public interface XMiniProgHalListener {
        void onMiniProgCallBack(int i, MiniProgramResponse miniProgramResponse);
    }

    public XMiniProgService(Context context) {
        super(context);
        this.mPackageMonitor = new AlipayPackageMonitor();
        this.alipayVersion = "";
        this.retryTime = 0;
        this.mContext = context;
        mHandler = new MiniProgEventHandler();
        registerReceiver();
        IHttpManager.init(ActivityThread.currentActivityThread().getApplication());
        this.mAccountManager = AccountManager.get(context);
        putPackageVersionName("com.alipay.arome.app");
        this.mCurrentAccount = getCurrentAccountInfo();
        if (Build.VERSION.SDK_INT >= 26) {
            this.mAccountManager.addOnAccountsUpdatedListener(this, null, true, new String[]{"com.xiaopeng.accountservice.ACCOUNT_TYPE_XP_VEHICLE"});
        }
        this.mMiniNaviInfoListener = new ContextInfoService.IMiniNaviInfoListener() { // from class: com.xiaopeng.xuiservice.xapp.XMiniProgService.1
            @Override // com.xiaopeng.xuiservice.contextinfo.ContextInfoService.IMiniNaviInfoListener
            public void onUpdateNaviInfo(String naviInfo) {
                LogUtil.d(XMiniProgService.TAG, "onNavigationInfo: " + naviInfo);
                XMiniProgService xMiniProgService = XMiniProgService.this;
                xMiniProgService.pushValidValue(xMiniProgService.getNaviInfo(naviInfo));
            }
        };
        initNaviListener();
    }

    private void initNaviListener() {
        this.mContextInfoService = ContextInfoService.getInstance();
        if (this.mContextInfoService == null) {
            LogUtil.d(TAG, "mContextInfoService is null");
        } else {
            registerNaviInfoListener();
        }
    }

    private boolean isInChina(double lon, double lat) {
        if (MIN_CHINA_LAT >= lat || MAX_CHINA_LAT <= lat || MIN_CHINA_LON >= lon || MAX_CHINA_LON <= lon) {
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pushValidValue(LocationInfo locationInfo) {
        if (locationInfo != null) {
            double carLat = locationInfo.getCarLat();
            double carLon = locationInfo.getCarLon();
            if (isInChina(carLon, carLat)) {
                LogUtil.d(TAG, "onNavigationInfo: is China !");
                this.mLocationInfo = locationInfo;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public LocationInfo getNaviInfo(String naviInfo) {
        try {
            JSONObject bodyJSONObject = new JSONObject(naviInfo);
            String msgType = bodyJSONObject.getString("msgType");
            if (msgType.equals(LOCATION_INFO_ERROR)) {
                LogUtil.d(TAG, "Location info is error:");
                return null;
            }
            String dataJSONObject = bodyJSONObject.getString("locInfo");
            return (LocationInfo) GsonUtil.fromJson(dataJSONObject, (Class<Object>) LocationInfo.class);
        } catch (Exception e) {
            LogUtil.d(TAG, "json error:" + e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void putPackageVersionName(String packageName) {
        try {
            PackageManager manager = this.mContext.getPackageManager();
            PackageInfo packageInfo = manager.getPackageInfo(packageName, 0);
            this.alipayVersion = packageInfo.versionName;
            LogUtil.d(TAG, "apliapy verisonName " + this.alipayVersion + " code " + packageInfo.versionCode);
            ContentResolver contentResolver = this.mContext.getContentResolver();
            Settings.System.putString(contentResolver, AlipayConstants.MINI_ALIPAY_VERISON_NAME, this.alipayVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            LogUtil.d(TAG, e.getMessage());
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void initCarListener() {
        this.mCarMcuEventCallback = new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.xapp.XMiniProgService.2
            public void onChangeEvent(CarPropertyValue value) {
                int propertyId = value.getPropertyId();
                if (propertyId == 557847561) {
                    LogUtil.d(XMiniProgService.TAG, "propertyId:" + propertyId + "isTempAccount:" + XMiniProgService.this.isTempAccount());
                    if (((Integer) value.getValue()).intValue() == 0 && XMiniProgService.this.isTempAccount()) {
                        XMiniProgService.this.logoutAccount();
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
                LogUtil.e(XMiniProgService.TAG, "CarMcuManager.CarMcuEventCallback onErrorEvent");
            }
        };
        addMcuManagerListener(this.mCarMcuEventCallback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        LogUtil.d(TAG, "onCarManagerInited");
    }

    private void registerReceiver() {
        LogUtil.i(TAG, "registerReceiver");
        AlipayPackageMonitor alipayPackageMonitor = this.mPackageMonitor;
        if (alipayPackageMonitor != null) {
            alipayPackageMonitor.register(this.mContext, null, false);
        }
    }

    private void registerNaviInfoListener() {
        ContextInfoService.IMiniNaviInfoListener iMiniNaviInfoListener;
        LogUtil.i(TAG, "registerNaviInfoListener : " + this.mContextInfoService + " listener: " + this.mMiniNaviInfoListener);
        ContextInfoService contextInfoService = this.mContextInfoService;
        if (contextInfoService != null && (iMiniNaviInfoListener = this.mMiniNaviInfoListener) != null) {
            contextInfoService.registerMiniNaviInfoListener(iMiniNaviInfoListener);
            LogUtil.i(TAG, "registerNaviInfoListener success");
        }
    }

    public void unregisterNaviInfoListener() {
        ContextInfoService.IMiniNaviInfoListener iMiniNaviInfoListener;
        LogUtil.i(TAG, "unregisterNaviInfoListener");
        ContextInfoService contextInfoService = this.mContextInfoService;
        if (contextInfoService != null && (iMiniNaviInfoListener = this.mMiniNaviInfoListener) != null) {
            contextInfoService.unRegisterMiniNaviInfoListener(iMiniNaviInfoListener);
            LogUtil.i(TAG, "unregisterNaviInfoListener success");
        }
    }

    public void unregisterReceiver() {
        LogUtil.i(TAG, "unregisterReceiver.");
        AlipayPackageMonitor alipayPackageMonitor = this.mPackageMonitor;
        if (alipayPackageMonitor != null) {
            alipayPackageMonitor.unregister();
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
        initService();
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        removeMcuManagerListener(this.mCarMcuEventCallback);
        MiniProgManager.getInstance().release();
        AccountManager accountManager = this.mAccountManager;
        if (accountManager != null) {
            accountManager.removeOnAccountsUpdatedListener(this);
        }
        unregisterReceiver();
        unregisterNaviInfoListener();
    }

    @Override // android.accounts.OnAccountsUpdateListener
    public void onAccountsUpdated(Account[] accounts) {
        LogUtil.d(TAG, "onAccountsUpdated  lenth=" + accounts.length);
        if (accounts.length == 0 && this.mCurrentAccount != null) {
            logoutAccount();
        }
        AccountManager accountManager = this.mAccountManager;
        if (accountManager != null && this.mCurrentAccount != null && accounts.length == 1 && accounts[0] != null) {
            String newUid = accountManager.getUserData(accounts[0], "uid");
            String oldUid = this.mAccountManager.getUserData(this.mCurrentAccount, "uid");
            if (!TextUtils.equals(newUid, oldUid)) {
                LogUtil.d(TAG, "onAccountsUpdated  exchange account");
                logoutAccount();
            }
        }
        this.mCurrentAccount = getCurrentAccountInfo();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isTempAccount() {
        return this.mCurrentAccount == null;
    }

    private Account getCurrentAccountInfo() {
        Account[] accounts = this.mAccountManager.getAccountsByType("com.xiaopeng.accountservice.ACCOUNT_TYPE_XP_VEHICLE");
        if (accounts.length > 0) {
            Account account = accounts[0];
            LogUtil.d(TAG, "getCurrentAccountInfo accounts length=" + accounts.length + ";account[0].name=" + account.name);
            try {
                String avatar = this.mAccountManager.getUserData(account, "avatar");
                String update = this.mAccountManager.getUserData(account, "update");
                String uid = this.mAccountManager.getUserData(account, "uid");
                String type = this.mAccountManager.getUserData(account, "user_type");
                LogUtil.d(TAG, "getCurrentAccountInfo name=" + account.name + ";icon=" + avatar + ";update=" + update + ";type=" + type + ";uid=" + uid);
            } catch (Exception e) {
                LogUtil.d(TAG, "getCurrentAccountInfo Exception=" + e.getMessage());
            }
            return account;
        }
        LogUtil.d(TAG, "getCurrentAccountInfo account is empty");
        return null;
    }

    public void registerMiniProgListener(XMiniProgHalListener listener) {
        this.mMiniProgEventListener = listener;
    }

    public void unregisterMiniProgListener(XMiniProgHalListener listener) {
        this.mMiniProgEventListener = null;
    }

    public void attachContext() {
    }

    public void initService() {
        LogUtil.d(TAG, "initService...");
        MiniProgManager.getInstance().init(new MiniProgManagerInitCallBack() { // from class: com.xiaopeng.xuiservice.xapp.XMiniProgService.3
            @Override // com.xiaopeng.xuiservice.xapp.miniprog.MiniProgManagerInitCallBack
            public void onInitMiniProg(int code, boolean isSuccess) {
                LogUtil.d(XMiniProgService.TAG, "onInitMiniProg:" + isSuccess);
                XMiniProgService.this.registerNavigation();
                XMiniProgService.this.sendMiniProgMessage(0, MiniProgramBeanUtil.toBean(code));
            }
        });
    }

    public boolean isServiceOnline() {
        boolean isServiceOnline = AromeInit.isServiceOnline();
        LogUtil.d(TAG, "isServiceOnline:" + isServiceOnline);
        return isServiceOnline;
    }

    public void activeArome(Map params) {
    }

    public int getCurrentMiniAppStatus(String appId) {
        LogUtil.d(TAG, "getCurrentMiniAppStatus:appid " + appId);
        if (!isServiceOnline() || TextUtils.isEmpty(appId)) {
            return 3;
        }
        final Object object = new Object();
        AromeGetAppStatusRequest request = new AromeGetAppStatusRequest();
        request.appId = appId;
        final AromeGetAppStatusResponse aromeResponse = new AromeGetAppStatusResponse();
        aromeResponse.isForeground = false;
        aromeResponse.isRunnning = false;
        AromeServiceInvoker.invoke(request, new AromeServiceTask.Callback<AromeGetAppStatusResponse>() { // from class: com.xiaopeng.xuiservice.xapp.XMiniProgService.4
            @Override // com.alipay.arome.aromecli.AromeServiceTask.Callback
            public void onCallback(AromeGetAppStatusResponse response) {
                if (response != null) {
                    aromeResponse.code = response.code;
                    aromeResponse.message = response.message;
                    aromeResponse.success = response.success;
                    aromeResponse.isForeground = response.isForeground;
                    aromeResponse.isRunnning = response.isRunnning;
                    LogUtil.d(XMiniProgService.TAG, "getCurrentMiniAppStatus:" + response.toString());
                }
                synchronized (object) {
                    object.notify();
                }
            }
        });
        synchronized (object) {
            try {
                object.wait(AlipayConstants.AROME_API_TIME_OUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
                LogUtil.d(TAG, "getCurrentMiniAppStatus:" + e.getMessage());
                return 2;
            }
        }
        handlerError(aromeResponse.code);
        int code = !aromeResponse.isForeground.booleanValue();
        return code;
    }

    public void startMiniProgram(final String miniAppId, String name, final Map params) {
        LogUtil.d(TAG, "startMiniProgram");
        if (getCurrentMiniAppStatus(miniAppId) == 0) {
            LogUtil.d(TAG, "current appid:" + miniAppId + " is foreground");
        } else if (!isServiceOnline()) {
            MiniProgManager.getInstance().init(new MiniProgManagerInitCallBack() { // from class: com.xiaopeng.xuiservice.xapp.XMiniProgService.5
                @Override // com.xiaopeng.xuiservice.xapp.miniprog.MiniProgManagerInitCallBack
                public void onInitMiniProg(int code, boolean isSuccess) {
                    LogUtil.d(XMiniProgService.TAG, "startMiniProgram==>onInitMiniProg:" + isSuccess);
                    if (isSuccess) {
                        XMiniProgService.this.requestLoginInfoInternal(miniAppId, params);
                    } else {
                        XMiniProgService.this.sendMiniProgMessage(3, MiniProgramBeanUtil.toBean(code));
                    }
                }
            });
        } else {
            requestLoginInfoInternal(miniAppId, params);
        }
    }

    public void loginAccount() {
        LogUtil.d(TAG, "loginAccount");
        if (!isServiceOnline()) {
            MiniProgManager.getInstance().init(new MiniProgManagerInitCallBack() { // from class: com.xiaopeng.xuiservice.xapp.XMiniProgService.6
                @Override // com.xiaopeng.xuiservice.xapp.miniprog.MiniProgManagerInitCallBack
                public void onInitMiniProg(int code, boolean isSuccess) {
                    LogUtil.d(XMiniProgService.TAG, "loginAccount==>onInitMiniProg:" + isSuccess);
                    if (isSuccess) {
                        XMiniProgService.this.loginAccountInternal("", null);
                    } else {
                        XMiniProgService.this.sendMiniProgMessage(1, MiniProgramBeanUtil.toBean(code));
                    }
                }
            });
        } else {
            loginAccountInternal("", null);
        }
    }

    public void logoutAccount() {
        LogUtil.d(TAG, "logoutAccount");
        if (!isServiceOnline()) {
            MiniProgManager.getInstance().init(new MiniProgManagerInitCallBack() { // from class: com.xiaopeng.xuiservice.xapp.XMiniProgService.7
                @Override // com.xiaopeng.xuiservice.xapp.miniprog.MiniProgManagerInitCallBack
                public void onInitMiniProg(int code, boolean isSuccess) {
                    LogUtil.d(XMiniProgService.TAG, "logoutAccount==>onInitMiniProg:" + isSuccess);
                    if (isSuccess) {
                        XMiniProgService.this.logoutAccountInternal();
                    } else {
                        XMiniProgService.this.sendMiniProgMessage(2, MiniProgramBeanUtil.toBean(code));
                    }
                }
            });
        } else {
            logoutAccountInternal();
        }
    }

    public void exitApp(final String id) {
        LogUtil.d(TAG, "exitApp:" + id);
        if (TextUtils.isEmpty(id)) {
            LogUtil.d(TAG, "mini app id not be null");
        }
        if (!isServiceOnline()) {
            MiniProgManager.getInstance().init(new MiniProgManagerInitCallBack() { // from class: com.xiaopeng.xuiservice.xapp.XMiniProgService.8
                @Override // com.xiaopeng.xuiservice.xapp.miniprog.MiniProgManagerInitCallBack
                public void onInitMiniProg(int code, boolean isSuccess) {
                    LogUtil.d(XMiniProgService.TAG, "exitApp==>onInitMiniProg:" + isSuccess);
                    if (isSuccess) {
                        XMiniProgService.this.exitAppInternal(id);
                    } else {
                        XMiniProgService.this.sendMiniProgMessage(5, MiniProgramBeanUtil.toBean(code));
                    }
                }
            });
        } else {
            exitAppInternal(id);
        }
    }

    public void preloadApp(final String appId, final boolean loadToMemory) {
        LogUtil.d(TAG, "preloadApp");
        if (!isServiceOnline()) {
            MiniProgManager.getInstance().init(new MiniProgManagerInitCallBack() { // from class: com.xiaopeng.xuiservice.xapp.XMiniProgService.9
                @Override // com.xiaopeng.xuiservice.xapp.miniprog.MiniProgManagerInitCallBack
                public void onInitMiniProg(int code, boolean isSuccess) {
                    LogUtil.d(XMiniProgService.TAG, "preloadApp==>onInitMiniProg:" + isSuccess);
                    if (isSuccess) {
                        XMiniProgService.this.preloadAppInternal(appId, loadToMemory);
                    } else {
                        XMiniProgService.this.sendMiniProgMessage(4, MiniProgramBeanUtil.toBean(code));
                    }
                }
            });
        } else {
            preloadAppInternal(appId, loadToMemory);
        }
    }

    public void requestLoginInfo() {
        LogUtil.d(TAG, "requestLoginInfo:");
        if (!isServiceOnline()) {
            MiniProgManager.getInstance().init(new MiniProgManagerInitCallBack() { // from class: com.xiaopeng.xuiservice.xapp.XMiniProgService.10
                @Override // com.xiaopeng.xuiservice.xapp.miniprog.MiniProgManagerInitCallBack
                public void onInitMiniProg(int code, boolean isSuccess) {
                    LogUtil.d(XMiniProgService.TAG, "requestLoginInfo==>onInitMiniProg:" + isSuccess);
                    if (isSuccess) {
                        XMiniProgService.this.requestLoginInfoInternal("", null);
                    } else {
                        XMiniProgService.this.sendMiniProgMessage(6, MiniProgramBeanUtil.toBean(code));
                    }
                }
            });
        } else {
            requestLoginInfoInternal("", null);
        }
    }

    public void uploadAlipayLog() {
        LogUtil.d(TAG, "uploadAlipayLog");
        if (!isServiceOnline()) {
            MiniProgManager.getInstance().init(new MiniProgManagerInitCallBack() { // from class: com.xiaopeng.xuiservice.xapp.XMiniProgService.11
                @Override // com.xiaopeng.xuiservice.xapp.miniprog.MiniProgManagerInitCallBack
                public void onInitMiniProg(int code, boolean isSuccess) {
                    LogUtil.d(XMiniProgService.TAG, "uploadAlipayLog==>onInitMiniProg:" + isSuccess);
                    if (isSuccess) {
                        XMiniProgService.this.uploadAlipayLogInternal();
                    }
                }
            });
        } else {
            uploadAlipayLogInternal();
        }
    }

    public void requestMiniList(String alipayVersion) {
        String param = ApiConstants.ALIPAY_MINI_LIST_PREFEX + alipayVersion;
        String url = ApiConstants.ALIPAY_MINI_LIST_URL + param;
        LogUtil.d(TAG, "url:" + url + " alipayVersion:" + alipayVersion);
        IHttpManager.getInstance().getHttp().bizHelper().get(url).extendBizHeader(HttpManagerUtil.REQUEST_XP_MO, HttpManagerUtil.getCarType()).buildWithSecretKey(ApiConstants.getSecret()).execute(new Callback() { // from class: com.xiaopeng.xuiservice.xapp.XMiniProgService.12
            @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback
            public void onSuccess(IResponse iResponse) {
                String body = iResponse == null ? "" : iResponse.body();
                LogUtil.d(XMiniProgService.TAG, "request mini list data success..." + body);
                XMiniProgService.this.sendMiniProgMessage(7, MiniProgramBeanUtil.parseResponse(iResponse));
            }

            @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback
            public void onFailure(IResponse iResponse) {
                LogUtil.d(XMiniProgService.TAG, "request mini list data failure!!!" + iResponse.getException());
                XMiniProgService.this.sendMiniProgMessage(7, MiniProgramBeanUtil.toBean(iResponse.code()));
            }
        });
    }

    public void checkIdentityValid(final String userKey) {
        LogUtil.d(TAG, "checkIdentityValid userKey" + userKey);
        if (!isServiceOnline()) {
            MiniProgManager.getInstance().init(new MiniProgManagerInitCallBack() { // from class: com.xiaopeng.xuiservice.xapp.XMiniProgService.13
                @Override // com.xiaopeng.xuiservice.xapp.miniprog.MiniProgManagerInitCallBack
                public void onInitMiniProg(int code, boolean isSuccess) {
                    LogUtil.d(XMiniProgService.TAG, "checkIdentityValid==>onInitMiniProg:" + isSuccess);
                    if (isSuccess) {
                        XMiniProgService.this.checkIdentityValidInternal(userKey);
                    } else {
                        XMiniProgService.this.sendMiniProgMessage(10, MiniProgramBeanUtil.toBean(code));
                    }
                }
            });
        } else {
            checkIdentityValidInternal(userKey);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkIdentityValidInternal(String userKey) {
        LogUtil.d(TAG, "checkIdentityValidInternal:");
        AromeGetUserInfoRequest request = new AromeGetUserInfoRequest();
        AromeParam param = new AromeParam();
        Map parmas = new HashMap();
        parmas.put("userKey", userKey);
        param.setParams(parmas);
        param.setType(10);
        AromeServiceInvoker.invoke(request, new AromeServiceCallback(param, this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loginSuccessGetUserInfo() {
        LogUtil.d(TAG, "loginSuccessGetUserInfo:");
        AromeGetUserInfoRequest request = new AromeGetUserInfoRequest();
        AromeParam param = new AromeParam();
        param.setType(13);
        AromeServiceInvoker.invoke(request, new AromeServiceCallback(param, this));
    }

    public void startCustomService(final String serviceCode, final String userIdentity) {
        LogUtil.d(TAG, "startCustomService serviceCode: " + serviceCode + " userIdentity: " + userIdentity);
        if (!isServiceOnline()) {
            MiniProgManager.getInstance().init(new MiniProgManagerInitCallBack() { // from class: com.xiaopeng.xuiservice.xapp.XMiniProgService.14
                @Override // com.xiaopeng.xuiservice.xapp.miniprog.MiniProgManagerInitCallBack
                public void onInitMiniProg(int code, boolean isSuccess) {
                    LogUtil.d(XMiniProgService.TAG, "startCustomService==>onInitMiniProg:" + isSuccess);
                    if (isSuccess) {
                        XMiniProgService.this.startCustomServiceInternal(serviceCode, userIdentity);
                    } else {
                        XMiniProgService.this.sendMiniProgMessage(12, MiniProgramBeanUtil.toBean(code));
                    }
                }
            });
        } else {
            startCustomServiceInternal(serviceCode, userIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startCustomServiceInternal(String serviceCode, String userIdentity) {
        LogUtil.d(TAG, "startCustomServiceInternal:");
        AromeParam param = new AromeParam();
        param.setType(12);
        AromeLaunchCustomServiceRequest request = new AromeLaunchCustomServiceRequest();
        request.mServiceCode = serviceCode;
        request.mUserIdentity = userIdentity;
        AromeServiceInvoker.invoke(request, new AromeServiceCallback(param, this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void uploadAlipayLogInternal() {
        LogUtil.d(TAG, "uploadAlipayLogInternal:");
        AromeUploadLogRequest request = new AromeUploadLogRequest();
        AromeParam param = new AromeParam();
        param.setType(8);
        AromeServiceInvoker.invoke(request, new AromeServiceCallback(param, this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestLoginInfoInternal(String miniAppId, Map parmas) {
        LogUtil.d(TAG, "requestLoginInfoInternal:" + miniAppId);
        AromeGetUserInfoRequest request = new AromeGetUserInfoRequest();
        AromeParam param = new AromeParam();
        param.setParams(parmas);
        param.setType(6);
        param.setMiniAppId(miniAppId);
        AromeServiceInvoker.invoke(request, new AromeServiceCallback(param, this));
    }

    private void requestMiniAppConfig(final String miniAppId, String name, final Map parmas) {
        final MiniProgramResponse miniProgramResponse = new MiniProgramResponse();
        WorkThreadUtil.getInstance().executeNetworkTask(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.XMiniProgService.15
            @Override // java.lang.Runnable
            public void run() {
                miniProgramResponse.setMiniAppId(miniAppId);
                MiniBean miniBean = XMiniProgService.this.requestMiniAppDetails(miniAppId);
                Map localMap = parmas;
                if (localMap == null) {
                    localMap = new HashMap();
                }
                if (miniBean != null) {
                    LogUtil.d(XMiniProgService.TAG, "miniBean:" + miniBean.toString());
                    localMap.put("config", miniBean.getParam());
                }
                miniProgramResponse.setParams(localMap);
                XMiniProgService.this.sendMiniProgMessage(9, miniProgramResponse);
            }
        });
    }

    private void startMiniProgramInternal(String miniAppId, String name, Map params) {
        LogUtil.d(TAG, "startMiniProgramInternal:" + miniAppId);
        AromeLaunchAppRequest request = new AromeLaunchAppRequest();
        request.appId = miniAppId;
        request.closeAllApp = true;
        request.launchWidth = AlipayConstants.CONFIG_WIDTH;
        MiniConfig config = MiniProgramBeanUtil.parseMini(params);
        if (config != null) {
            request.page = config.getPage();
            request.query = config.getQuery();
            Bundle showConfig = new Bundle();
            showConfig.putInt("showType", config.getShowType());
            showConfig.putString("titleBarType", SkillStatisticsBean.PAGE_CAR);
            LogUtil.d(TAG, "config:" + config.getShowType());
            request.themeConfig = showConfig;
        }
        AromeParam param = new AromeParam();
        param.setType(3);
        param.setMiniAppId(miniAppId);
        AromeServiceInvoker.invoke(request, new AromeServiceCallback(param, this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loginAccountInternal(String miniAppId, Map map) {
        LogUtil.d(TAG, "loginAccountInternal:" + miniAppId);
        AromeLoginRequest request = new AromeLoginRequest();
        AromeParam param = new AromeParam();
        param.setType(1);
        param.setMiniAppId(miniAppId);
        param.setParams(map);
        AromeServiceInvoker.invoke(request, new AromeServiceCallback(param, this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logoutAccountInternal() {
        LogUtil.d(TAG, "logoutAccountInternal");
        AromeLogoutRequest request = new AromeLogoutRequest();
        AromeParam param = new AromeParam();
        param.setType(2);
        AromeServiceInvoker.invoke(request, new AromeServiceCallback(param, this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exitAppInternal(String id) {
        LogUtil.d(TAG, "exitAppInternal:" + id);
        AromeExitAppRequest request = new AromeExitAppRequest();
        request.appId = id;
        AromeParam param = new AromeParam();
        param.setType(5);
        param.setMiniAppId(id);
        AromeServiceInvoker.invoke(request, new AromeServiceCallback(param, this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void preloadAppInternal(String appId, boolean loadToMemory) {
        LogUtil.d(TAG, "preloadAppInternal:" + appId);
        AromePreloadAppRequest request = new AromePreloadAppRequest();
        request.appId = appId;
        request.loadToMemory = loadToMemory;
        AromeParam param = new AromeParam();
        param.setType(4);
        param.setMiniAppId(appId);
        AromeServiceInvoker.invoke(request, new AromeServiceCallback(param, this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MiniBean requestMiniAppDetails(String appId) {
        List<String> appIds = new ArrayList<>();
        appIds.add(appId);
        List<MiniBean> miniBeans = requestMiniAppDetails(appIds);
        if (miniBeans == null || miniBeans.size() == 0) {
            return null;
        }
        return miniBeans.get(0);
    }

    private List<MiniBean> requestMiniAppDetails(List<String> appIds) {
        if (appIds == null || appIds.size() == 0) {
            LogUtil.d(TAG, "appId is null");
        }
        Map<String, List<String>> param = new HashMap<>();
        param.put(ApiConstants.MINI_DETAIL_URL_PARAMS, appIds);
        try {
            String body = GsonUtil.toJson(param);
            LogUtil.d(TAG, "url:" + ApiConstants.ALIPAY_MINI_DETAIL_URL + " request body:" + body);
            IResponse response = IHttpManager.getInstance().getHttp().bizHelper().post(ApiConstants.ALIPAY_MINI_DETAIL_URL, body).extendBizHeader(ApiConstants.XP_ALIPAY_HEAD, this.alipayVersion).extendBizHeader(HttpManagerUtil.REQUEST_XP_MO, HttpManagerUtil.getCarType()).buildWithSecretKey(ApiConstants.getSecret()).execute();
            String responseBody = response.body();
            LogUtil.d(TAG, "requestMiniAppDetails success..." + responseBody);
            JsonObject jsonObject = new JsonParser().parse(responseBody).getAsJsonObject();
            JsonElement jsonElement = jsonObject.get("data");
            LogUtil.d(TAG, "requestMiniAppDetails jsonElement" + jsonElement.toString());
            List<MiniBean> miniBeanList = (List) GsonUtil.fromJson(jsonElement, new TypeToken<List<MiniBean>>() { // from class: com.xiaopeng.xuiservice.xapp.XMiniProgService.16
            }.getType());
            return miniBeanList;
        } catch (Exception e) {
            LogUtil.d(TAG, "exception " + e);
            e.printStackTrace();
            return null;
        }
    }

    private void handlerError(int code) {
        if (code == 1001) {
            MiniProgManager.getInstance().init();
        } else if (code == 2006) {
            MiniProgManager.getInstance().putStringValue(AlipayConstants.KEY_MINI_PROG_SIGNATURE, "");
            MiniProgManager.getInstance().init();
        }
    }

    public void sendMiniProgMessage(int msgType, MiniProgramResponse response) {
        MiniProgEventHandler miniProgEventHandler = mHandler;
        if (miniProgEventHandler != null) {
            Message message = miniProgEventHandler.obtainMessage();
            message.what = 1;
            message.obj = response;
            message.arg1 = msgType;
            message.arg2 = response.getCode();
            mHandler.sendMessage(message);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doMiniProgFunction(int event, Object data, int code) {
        LogUtil.d(TAG, "doMiniProgFunction  " + event + "  code:" + code);
        handlerError(code);
        MiniProgramResponse response = new MiniProgramResponse();
        if (data instanceof MiniProgramResponse) {
            response = (MiniProgramResponse) data;
        }
        LogUtil.d(TAG, " response: " + response);
        if (event == 1 && code == 0) {
            LogUtil.d(TAG, "login success, will get the information of user");
            loginSuccessGetUserInfo();
        }
        if (event == 13 && code == 0) {
            LogUtil.d(TAG, "get user info success, will upload the information of device");
            uploadInfo(response);
        }
        XMiniProgHalListener xMiniProgHalListener = this.mMiniProgEventListener;
        if (xMiniProgHalListener != null) {
            xMiniProgHalListener.onMiniProgCallBack(event, response);
        }
        doLogic(event, response);
    }

    private void doLogic(int event, MiniProgramResponse response) {
        if (response == null) {
            LogUtil.d(TAG, "response is null");
            return;
        }
        String miniAppId = response.getMiniAppId();
        LogUtil.d(TAG, "response :" + response.toString());
        if (TextUtils.isEmpty(miniAppId)) {
            return;
        }
        if (event == 6) {
            if (response.isLogin()) {
                requestMiniAppConfig(miniAppId, "", response.getParams());
            } else {
                loginAccountInternal(miniAppId, response.getParams());
            }
        } else if (event == 1) {
            if (response.getCode() == 0) {
                requestMiniAppConfig(miniAppId, "", response.getParams());
            }
        } else if (event == 9) {
            startMiniProgramInternal(miniAppId, "", response.getParams());
        }
    }

    public void handleHalEvents() {
    }

    private void dispatchEventToListener(XMiniProgHalListener listener) {
        LogUtil.d(TAG, "handleHalEvents event: ");
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.HalServiceBaseCarListener
    public void dump(PrintWriter writer) {
        writer.println("*XMiniProg HAL*");
    }

    /* loaded from: classes5.dex */
    public class Constants {
        public static final String ACCOUNT_TYPE_XP_VEHICLE = "com.xiaopeng.accountservice.ACCOUNT_TYPE_XP_VEHICLE";
        public static final String ACCOUNT_USER_TYPE = "user_type";
        public static final String AUTH_INFO_EXTRA_APP_ID = "app_id";
        public static final String AUTH_INFO_EXTRA_APP_SECRET = "app_secret";
        public static final String AUTH_TYPE_AUTH_CODE = "com.xiaopeng.accountservice.AUTH_TYPE_AUTH_CODE";
        public static final String AUTH_TYPE_AUTH_OTP = "com.xiaopeng.accountservice.AUTH_TYPE_AUTH_OTP";
        public static final String USER_DATA_EXTRA_AVATAR = "avatar";
        public static final String USER_DATA_EXTRA_UID = "uid";
        public static final String USER_DATA_EXTRA_UPDATE = "update";
        private static final int USER_TYPE_DRIVER = 4;
        private static final int USER_TYPE_TENANT = 3;
        private static final int USER_TYPE_TOWNER = 1;
        private static final int USER_TYPE_USER = 2;

        public Constants() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public final class MiniProgEventHandler extends XuiWorkHandler {
        public MiniProgEventHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            LogUtil.d(XMiniProgService.TAG, "handleMessage: msg.what=" + msg.what);
            int event = msg.arg1;
            Object data = msg.obj;
            int code = msg.arg2;
            int i = msg.what;
            if (i == 1) {
                XMiniProgService.this.doMiniProgFunction(event, data, code);
            } else if (i == 2) {
                XMiniProgService.this.loginSuccessGetUserInfo();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class AlipayPackageMonitor extends PackageMonitor {
        private AlipayPackageMonitor() {
        }

        public void onPackageUpdateFinished(String packageName, int uid) {
            if (packageName.equals("com.alipay.arome.app")) {
                LogUtil.d(XMiniProgService.TAG, "packageName:" + packageName);
                XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.XMiniProgService.AlipayPackageMonitor.1
                    @Override // java.lang.Runnable
                    public void run() {
                        XMiniProgService.this.putPackageVersionName("com.alipay.arome.app");
                    }
                });
            }
        }
    }

    private void uploadInfo(MiniProgramResponse response) {
        if (response == null) {
            LogUtil.d(TAG, "uploadInfo response is null ");
        } else if (this.mLocationInfo == null) {
            LogUtil.d(TAG, "uploadInfo mLocationInfo is null ");
            initNaviListener();
            int i = this.retryTime;
            this.retryTime = i + 1;
            if (i < 3) {
                LogUtil.d(TAG, "uploadInfo failure: retry time " + this.retryTime);
                if (mHandler == null) {
                    mHandler = new MiniProgEventHandler();
                }
                mHandler.sendEmptyMessageDelayed(2, 10000L);
                return;
            }
            this.retryTime = 0;
        } else {
            this.retryTime = 0;
            String userKey = response.getUserKey();
            String userRoute = response.getUserRoute();
            String latitude = String.valueOf(this.mLocationInfo.getCarLat());
            String longitude = String.valueOf(this.mLocationInfo.getCarLon());
            try {
                DeviceData deviceData = new DeviceData(latitude, longitude);
                String deviceDataJson = GsonUtil.toJsonNoHTML(deviceData);
                LogUtil.d(TAG, "deviceData json: " + deviceDataJson);
                MiniUploadData miniReportData = new MiniUploadData(userKey, userRoute, deviceDataJson);
                String jsonData = GsonUtil.toJsonNoHTML(miniReportData);
                LogUtil.d(TAG, "uploadInfo url:" + ApiConstants.ALIPAY_MINI_UPLOAD_DATA_URL + " upload data: " + jsonData);
                IResponse httpResponse = IHttpManager.getInstance().getHttp().bizHelper().post(ApiConstants.ALIPAY_MINI_UPLOAD_DATA_URL, jsonData).extendBizHeader(HttpManagerUtil.REQUEST_XP_MO, HttpManagerUtil.getCarType()).buildWithSecretKey(ApiConstants.getSecret()).execute();
                String httpResponseBody = httpResponse.body();
                JSONObject bodyJSONObject = new JSONObject(httpResponseBody);
                LogUtil.d(TAG, "uploadInfo success..." + httpResponseBody);
                int code = Integer.parseInt(bodyJSONObject.getString("code"));
                LogUtil.d(TAG, "uploadInfo code: " + code);
                if (code == 200) {
                    boolean data = Boolean.parseBoolean(bodyJSONObject.getString("data"));
                    if (data) {
                        LogUtil.d(TAG, "uploadInfo success !!! ");
                    }
                }
            } catch (Exception e) {
                LogUtil.d(TAG, "exception " + e);
                e.printStackTrace();
            }
        }
    }

    public boolean checkOrderValid(String orderId) {
        try {
            OrderBean orderBean = new OrderBean(orderId);
            String jsonData = GsonUtil.toJsonNoHTML(orderBean);
            LogUtil.d(TAG, "checkOrderValid url:" + ApiConstants.ALIPAY_MINI_ORDER_CHECK_URL + " orderId: " + orderId);
            IResponse httpResponse = IHttpManager.getInstance().getHttp().bizHelper().post(ApiConstants.ALIPAY_MINI_ORDER_CHECK_URL, jsonData).extendBizHeader(HttpManagerUtil.REQUEST_XP_MO, HttpManagerUtil.getCarType()).buildWithSecretKey(ApiConstants.getSecret()).execute();
            String httpResponseBody = httpResponse.body();
            JSONObject bodyJSONObject = new JSONObject(httpResponseBody);
            LogUtil.d(TAG, "checkOrderValid success..." + httpResponseBody);
            int code = Integer.parseInt(bodyJSONObject.getString("code"));
            LogUtil.d(TAG, "checkOrderValid code: " + code);
            if (code == 200) {
                boolean data = Boolean.parseBoolean(bodyJSONObject.getString("data"));
                if (data) {
                    return true;
                }
                return false;
            }
            return false;
        } catch (Exception e) {
            LogUtil.d(TAG, "exception " + e);
            e.printStackTrace();
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerNavigation() {
        LogUtil.d(TAG, "registerNavigation");
        AromeExtendBridgeRequest request = new AromeExtendBridgeRequest();
        ArrayList<String> extensionList = new ArrayList<>();
        extensionList.add("startNavigation");
        request.mExtensionList = extensionList;
        AromeServiceInvoker.invoke(request, new AromeServiceTask.Callback<AromeResponse>() { // from class: com.xiaopeng.xuiservice.xapp.XMiniProgService.17
            @Override // com.alipay.arome.aromecli.AromeServiceTask.Callback
            public void onCallback(AromeResponse aromeResponse) {
                LogUtil.d(XMiniProgService.TAG, "AromeServiceInvoker aromeResponse: " + aromeResponse);
            }
        });
        AromeServiceInvoker.registerBridgeExtension(new AromeServiceInvoker.BridgeExtension() { // from class: com.xiaopeng.xuiservice.xapp.XMiniProgService.18
            @Override // com.alipay.arome.aromecli.AromeServiceInvoker.BridgeExtension
            public void onCalled(String action, String params, AromeServiceInvoker.BridgeCallback bridgeCallback) {
                LogUtil.d(XMiniProgService.TAG, "registerNavigation action: " + action + " params: " + params);
                try {
                    JSONObject bodyJSONObject = new JSONObject(params);
                    LogUtil.d(XMiniProgService.TAG, "registerNavigation info: " + bodyJSONObject);
                    double latitude = Double.parseDouble(bodyJSONObject.getString("latitude"));
                    double longitude = Double.parseDouble(bodyJSONObject.getString("longitude"));
                    LogUtil.d(XMiniProgService.TAG, "registerNavigation info latitude: " + latitude + " longitude: " + longitude);
                    XMiniProgService.this.launchNavigation(latitude, longitude);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void launchNavigation(double latitude, double longitude) {
        LogUtil.d(TAG, "launchNavigation");
        Intent intent = new Intent();
        intent.setClassName("com.xiaopeng.montecarlo", "com.xiaopeng.montecarlo.MainActivity");
        intent.setFlags(Videoio.CAP_INTELPERC_IMAGE_GENERATOR);
        intent.addCategory("android.intent.category.HOME");
        Bundle extras = new Bundle();
        extras.putString("uri", "xpengmap://map/navi?data={\"dest\":{\"lat\":" + latitude + ",\"lon\":" + longitude + "}}");
        extras.putString("action", "android.intent.action.VIEW");
        intent.putExtras(extras);
        this.mContext.startActivity(intent);
    }
}
