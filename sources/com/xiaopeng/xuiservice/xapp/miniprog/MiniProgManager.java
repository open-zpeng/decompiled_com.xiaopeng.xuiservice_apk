package com.xiaopeng.xuiservice.xapp.miniprog;

import android.app.ActivityThread;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import com.alipay.arome.aromecli.AromeInit;
import com.alipay.arome.aromecli.AromeInitOptions;
import com.alipay.arome.aromecli.AromeServiceInvoker;
import com.alipay.arome.aromecli.AromeServiceTask;
import com.alipay.arome.aromecli.requst.AromeActivateRequest;
import com.alipay.arome.aromecli.response.AromeActivateResponse;
import com.alipay.mobile.aromeservice.InitOptionParams;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IResponse;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.contextinfo.IHttpManager;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import com.xiaopeng.xuiservice.xapp.miniprog.manager.HttpManagerUtil;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class MiniProgManager {
    private static final int ACTIVATE_SUCCESS_EVENT = 3;
    private static final int AROME_SUCCESS_EVENT = 5;
    private static final int ERROR_EVENT = 6;
    private static final int REGISTER_SUCCESS_EVENT = 1;
    private static MiniProgHandler mHandler;
    private final int DEFAULT_INIT_AROME_COUNT = 1;
    private AtomicBoolean mHasInitArome = new AtomicBoolean();
    private MiniProgManagerInitCallBack mMiniProgManagerInitCallBack;
    private static final String TAG = MiniProgManager.class.getSimpleName();
    private static MiniProgManager sInstance = new MiniProgManager();

    private MiniProgManager() {
        mHandler = new MiniProgHandler();
    }

    public static MiniProgManager getInstance() {
        return sInstance;
    }

    public void init(MiniProgManagerInitCallBack callBack) {
        if (callBack != null) {
            this.mMiniProgManagerInitCallBack = callBack;
        }
        register();
    }

    public void init() {
        init(null);
    }

    private void register() {
        if (hasRegister()) {
            LogUtil.i(TAG, "has register mini prog...");
            handlerEvent(1, 0, 0L);
            return;
        }
        String str = TAG;
        LogUtil.d(str, "url:" + ApiConstants.ALIPAY_REGISTER_URL);
        IHttpManager.getInstance().getHttp().bizHelper().get(ApiConstants.ALIPAY_REGISTER_URL).extendBizHeader(HttpManagerUtil.REQUEST_XP_MO, HttpManagerUtil.getCarType()).buildWithSecretKey(ApiConstants.getSecret()).execute(new Callback() { // from class: com.xiaopeng.xuiservice.xapp.miniprog.MiniProgManager.1
            @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback
            public void onSuccess(IResponse iResponse) {
                String body = iResponse.body();
                String str2 = MiniProgManager.TAG;
                LogUtil.d(str2, "register mini prog device success..." + iResponse.body());
                try {
                    JSONObject bodyJSONObject = new JSONObject(body);
                    JSONObject dataJSONObject = bodyJSONObject.getJSONObject("data");
                    String signature = dataJSONObject.getString("sign");
                    MiniProgManager.this.putStringValue(AlipayConstants.KEY_MINI_PROG_SIGNATURE, signature);
                    MiniProgManager.this.handlerEvent(1, 0, 0L);
                } catch (Exception e) {
                    String str3 = MiniProgManager.TAG;
                    LogUtil.d(str3, "json error:" + e);
                    MiniProgManager.this.handlerEvent(6, AlipayMiniCode.ERROR_REGISTER_NETWORK_OR_JSON_PARSING_FAIL_CODE, 0L);
                }
            }

            @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback
            public void onFailure(IResponse iResponse) {
                String str2 = MiniProgManager.TAG;
                LogUtil.e(str2, "register mini prog device failure!!!" + iResponse.getException());
                MiniProgManager.this.handlerEvent(6, AlipayMiniCode.ERROR_REGISTER_NETWORK_OR_JSON_PARSING_FAIL_CODE, 0L);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initArome() {
        if (isConnectAromeService()) {
            handlerEvent(5, 0, 0L);
            return;
        }
        String str = TAG;
        LogUtil.d(str, "init Arome=>mHasInitArome: " + this.mHasInitArome);
        reset(false);
        Bundle themeConfig = new Bundle();
        themeConfig.putString(SpeechConstants.KEY_MODE, "portrait");
        themeConfig.putString(InitOptionParams.THEME_CONFIG_BG_COLOR, AlipayConstants.THEME_BG_COLOR);
        themeConfig.putString(InitOptionParams.THEME_CONFIG_PANEL_BG_COLOR, AlipayConstants.THEME_PANEL_BG_COLOR);
        themeConfig.putString(InitOptionParams.THEME_CONFIG_TITLE_COLOR, AlipayConstants.THEME_TITLE_COLOR);
        themeConfig.putString(InitOptionParams.THEME_CONFIG_SUB_TITLE_COLOR, "#8fFFFFFF");
        themeConfig.putString(InitOptionParams.THEME_CONFIG_ACCENT_TITLE_COLOR, "#8fFFFFFF");
        themeConfig.putString("logoType", "round");
        AromeInit.attachApplicationContext(ActivityThread.currentActivityThread().getApplication().getApplicationContext());
        AromeInit.init(new AromeInitOptions.Builder().hardwareType(1).hardwareName(AlipayConstants.HARDWARE_NAME).themeConfig(themeConfig).build(), new AromeInit.Callback() { // from class: com.xiaopeng.xuiservice.xapp.miniprog.MiniProgManager.2
            @Override // com.alipay.arome.aromecli.AromeInit.Callback
            public void postInit(boolean success, int errorCode, String errorMsg) {
                String str2 = MiniProgManager.TAG;
                LogUtil.d(str2, "Arome init finish =》 is success：" + success);
                if (!MiniProgManager.this.mHasInitArome.get()) {
                    MiniProgManager.this.reset(true);
                    String str3 = MiniProgManager.TAG;
                    LogUtil.d(str3, " mHasInitArome:" + MiniProgManager.this.mHasInitArome.get());
                    if (success) {
                        MiniProgManager.this.handlerEvent(5, 0, 0L);
                    } else {
                        MiniProgManager.this.handlerEvent(6, errorCode, 0L);
                    }
                }
            }

            @Override // com.alipay.arome.aromecli.AromeInit.Callback
            public void serverDied() {
                LogUtil.d(MiniProgManager.TAG, "Arome init fail =》 serverDied");
                MiniProgManager.this.reset(false);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reset(boolean isReset) {
        this.mHasInitArome.set(isReset);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void activate() {
        String signature = getStringValue(AlipayConstants.KEY_MINI_PROG_SIGNATURE);
        if (TextUtils.isEmpty(signature)) {
            LogUtil.d(TAG, "mSignature is null");
        }
        AromeActivateRequest request = new AromeActivateRequest();
        request.hostAppId = AlipayConstants.HOST_APP_ID;
        request.productId = AlipayConstants.PRODUCT_ID;
        request.deviceId = AlipayConstants.HARDWARE_ID;
        request.signature = signature;
        String str = TAG;
        LogUtil.d(str, "param=>hostAppId:" + request.hostAppId + ";productId:" + request.productId + ";deviceId：" + request.deviceId + ";signature:" + request.signature);
        AromeServiceInvoker.invoke(request, new AromeServiceTask.Callback<AromeActivateResponse>() { // from class: com.xiaopeng.xuiservice.xapp.miniprog.MiniProgManager.3
            @Override // com.alipay.arome.aromecli.AromeServiceTask.Callback
            public void onCallback(AromeActivateResponse response) {
                String tagResponse = response == null ? "" : response.toString();
                String str2 = MiniProgManager.TAG;
                LogUtil.d(str2, "ActivateResponse:" + tagResponse);
                if (response == null) {
                    MiniProgManager.this.handlerEvent(6, AlipayMiniCode.ERROR_RESPONSE_IS_NULL_CODE, 0L);
                } else if (response.code == 0) {
                    MiniProgManager.this.handlerEvent(3, response.code, 0L);
                } else {
                    MiniProgManager.this.handlerEvent(6, response.code, 0L);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlerEvent(int type, int code, long delayMillis) {
        MiniProgHandler miniProgHandler = mHandler;
        if (miniProgHandler == null) {
            return;
        }
        Message message = miniProgHandler.obtainMessage();
        message.what = type;
        message.arg1 = code;
        mHandler.sendMessageDelayed(message, delayMillis);
    }

    public void release() {
        MiniProgHandler miniProgHandler = mHandler;
        if (miniProgHandler != null) {
            miniProgHandler.removeCallbacksAndMessages(null);
        }
        if (this.mMiniProgManagerInitCallBack != null) {
            this.mMiniProgManagerInitCallBack = null;
        }
    }

    public boolean hasRegister() {
        String value = Settings.System.getString(ActivityThread.currentActivityThread().getApplication().getContentResolver(), AlipayConstants.KEY_MINI_PROG_SIGNATURE);
        return !TextUtils.isEmpty(value);
    }

    public boolean isConnectAromeService() {
        return AromeInit.isServiceOnline();
    }

    public void putValue(String key, int value) {
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), key, value);
    }

    public void putStringValue(String key, String value) {
        Settings.System.putString(ActivityThread.currentActivityThread().getApplication().getContentResolver(), key, value);
    }

    public String getStringValue(String key) {
        return Settings.System.getString(ActivityThread.currentActivityThread().getApplication().getContentResolver(), key);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlerError(int code) {
        dispatchInitMiniListener(code, false);
        if (code == 1001) {
            init();
        } else if (code == 2006) {
            putStringValue(AlipayConstants.KEY_MINI_PROG_SIGNATURE, "");
            init();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchInitMiniListener(int code, boolean isSuccess) {
        MiniProgManagerInitCallBack miniProgManagerInitCallBack = this.mMiniProgManagerInitCallBack;
        if (miniProgManagerInitCallBack != null) {
            miniProgManagerInitCallBack.onInitMiniProg(code, isSuccess);
            this.mMiniProgManagerInitCallBack = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public final class MiniProgHandler extends XuiWorkHandler {
        public MiniProgHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            int code = msg.arg1;
            int i = msg.what;
            if (i == 1) {
                MiniProgManager.this.initArome();
            } else if (i == 3) {
                MiniProgManager.this.dispatchInitMiniListener(code, true);
            } else if (i == 5) {
                MiniProgManager.this.activate();
            } else if (i == 6) {
                MiniProgManager.this.handlerError(code);
            }
        }
    }
}
