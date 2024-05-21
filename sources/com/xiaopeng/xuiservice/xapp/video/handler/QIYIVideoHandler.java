package com.xiaopeng.xuiservice.xapp.video.handler;

import android.app.ActivityThread;
import android.net.Uri;
import android.text.TextUtils;
import com.alipay.mobile.aromeservice.RequestParams;
import com.qiyi.video.client.IQYSManager;
import com.qiyi.video.client.IQYSService;
import com.qiyi.video.qys.IQYSRequest;
import com.qiyi.video.qys.IQYSResponse;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.xapp.Constants;
import com.xiaopeng.xuiservice.xapp.video.Callback;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class QIYIVideoHandler implements IVideoHandler {
    private static final String PATH_PLAYER = "player";
    private static final String PATH_VOICE = "voice";
    private static final int RESPONSE_CODE_CMD_NOT_EXIT = -404;
    private static final int RESPONSE_CODE_PARAMS_ERROR = -400;
    private static final int RESPONSE_CODE_PLAYER_BUY_VIP = -100204;
    private static final int RESPONSE_CODE_PLAYER_LOGIN = -100203;
    private static final int RESPONSE_CODE_PLAYER_NOT_EXIST = -100404;
    private static final int RESPONSE_CODE_PLAYER_SEEK_END = -100206;
    private static final int RESPONSE_CODE_PLAYER_SEEK_HEAD = -100205;
    private static final int RESPONSE_CODE_PLAYER_STATUS_ERROR = -100405;
    private static final int RESPONSE_CODE_PLAYER_TRIAL_END = -100202;
    private static final int RESPONSE_CODE_RESULT_NO_MATCH = -405;
    private static final int RESPONSE_CODE_SUCCESS = 200;
    private static final int RESPONSE_CODE_UNANTICIPATE = -202;
    private static final int RESPONSE_FAILURE_FROM_CONNECT = -6;
    private static final String TAG = "QIYIVideoHandler";
    IQYSManager.ConnectStatusChangedListener mConnectStatusChangedListener = new IQYSManager.ConnectStatusChangedListener() { // from class: com.xiaopeng.xuiservice.xapp.video.handler.QIYIVideoHandler.6
        @Override // com.qiyi.video.client.IQYSManager.ConnectStatusChangedListener
        public void onChanged(boolean connected) {
            LogUtil.w(QIYIVideoHandler.TAG, "ConnectStatusChangedListener onChanged:" + connected);
            if (!connected) {
                QIYIVideoHandler.this.mIQYSService = null;
            }
        }
    };
    private IQYSService mIQYSService;

    public QIYIVideoHandler() {
        init();
    }

    private void init() {
        LogUtil.d(TAG, "init the QIYI handler");
        IQYSManager.getInstance().addConnectStatusChangedListener(this.mConnectStatusChangedListener);
        SharedDisplayManager.getInstance().registerListener(new SharedDisplayManager.ISharedDisplayEventListener() { // from class: com.xiaopeng.xuiservice.xapp.video.handler.QIYIVideoHandler.1
            @Override // com.xiaopeng.xuiservice.utils.SharedDisplayManager.ISharedDisplayEventListener
            public void onActivityChanged(int screenId, String property) {
                LogUtil.d(QIYIVideoHandler.TAG, "onActivityChanged screenId:" + screenId + " &property:" + property);
                try {
                    JSONObject jsonObject = new JSONObject(property);
                    String packageName = jsonObject.optString(RequestParams.REQUEST_KEY_PACKAGE_NAME);
                    boolean isTrue = QIYIVideoHandler.this.getPackageName().equals(packageName);
                    LogUtil.d(QIYIVideoHandler.TAG, "packageName:" + packageName + " &getPackageName:" + QIYIVideoHandler.this.getPackageName() + " &isTrue:" + isTrue);
                    if (QIYIVideoHandler.this.getPackageName().equals(packageName)) {
                        QIYIVideoHandler.this.registerQIYIService();
                    }
                } catch (JSONException e) {
                    LogUtil.d(QIYIVideoHandler.TAG, "json parse error:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void play(int displayId, final String command, final Callback callback) {
        LogUtil.d(TAG, "AI play displayId:" + displayId + " &command:" + command);
        IQYSService iQYSService = this.mIQYSService;
        if (iQYSService == null) {
            IQYSManager.getInstance().register(ActivityThread.currentActivityThread().getApplication().getApplicationContext(), new IQYSManager.RegisterListener() { // from class: com.xiaopeng.xuiservice.xapp.video.handler.QIYIVideoHandler.2
                @Override // com.qiyi.video.client.IQYSManager.RegisterListener
                public void onRegisterSuccess(IQYSService iqysService) {
                    QIYIVideoHandler.this.mIQYSService = iqysService;
                    LogUtil.i(QIYIVideoHandler.TAG, "play onRegisterSuccess");
                    QIYIVideoHandler.this.mIQYSService.executeAsync(new IQYSRequest(command), new IQYSService.Callback() { // from class: com.xiaopeng.xuiservice.xapp.video.handler.QIYIVideoHandler.2.1
                        @Override // com.qiyi.video.client.IQYSService.Callback
                        public void onResponse(IQYSResponse iqysResponse) {
                            LogUtil.d(QIYIVideoHandler.TAG, "play executeAsync responseCode:" + iqysResponse.mCode + " &command:" + command);
                            if (callback != null) {
                                callback.onResult(QIYIVideoHandler.this.getResultCode(iqysResponse));
                            }
                        }
                    });
                }

                @Override // com.qiyi.video.client.IQYSManager.RegisterListener
                public void onRegisterFailure(int code, String s) {
                    LogUtil.w(QIYIVideoHandler.TAG, "play onRegisterFailure code:" + code + " &message:" + s);
                    Callback callback2 = callback;
                    if (callback2 != null) {
                        callback2.onResult(6);
                    }
                }
            });
        } else {
            iQYSService.executeAsync(new IQYSRequest(command), new IQYSService.Callback() { // from class: com.xiaopeng.xuiservice.xapp.video.handler.QIYIVideoHandler.3
                @Override // com.qiyi.video.client.IQYSService.Callback
                public void onResponse(IQYSResponse iqysResponse) {
                    LogUtil.d(QIYIVideoHandler.TAG, "play to execute responseCode:" + iqysResponse.mCode + " &command:" + command);
                    Callback callback2 = callback;
                    if (callback2 != null) {
                        callback2.onResult(QIYIVideoHandler.this.getResultCode(iqysResponse));
                    }
                }
            });
        }
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void play(int displayId, String programName, String artistName, Callback callback) {
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public String getPackageName() {
        return Constants.PACKAGE_QIYI_IV;
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void prev(Callback callback) {
        String fullCommand = generateFullCommand(PATH_PLAYER, "PREVIOUS", "");
        executeAsync(fullCommand, callback);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void next(Callback callback) {
        String fullCommand = generateFullCommand(PATH_PLAYER, "NEXT", "");
        executeAsync(fullCommand, callback);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void set(int value, Callback callback) {
        String fullCommand = generateFullCommand(PATH_PLAYER, "EPISODE", String.valueOf(value));
        executeAsync(fullCommand, callback);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void forward(Callback callback) {
        String fullCommand = generateFullCommand(PATH_PLAYER, "FAST_FORWARD", "15000");
        executeAsync(fullCommand, callback);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void backward(Callback callback) {
        String fullCommand = generateFullCommand(PATH_PLAYER, "FAST_BACKWARD", "15000");
        executeAsync(fullCommand, callback);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void setTime(double time, Callback callback) {
        int timeValue = (int) (1000.0d * time);
        String fullCommand = generateFullCommand(PATH_PLAYER, "SEEK", String.valueOf(timeValue));
        executeAsync(fullCommand, callback);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void pause(Callback callback) {
        String fullCommand = generateFullCommand(PATH_PLAYER, "PAUSE", "");
        executeAsync(fullCommand, callback);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void resume(Callback callback) {
        String fullCommand = generateFullCommand(PATH_PLAYER, "PLAY", "");
        executeAsync(fullCommand, callback);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void speedUp(Callback callback) {
        String fullCommand = generateFullCommand(PATH_PLAYER, "SPEED", "UP");
        executeAsync(fullCommand, callback);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void speedDown(Callback callback) {
        String fullCommand = generateFullCommand(PATH_PLAYER, "SPEED", "DOWN");
        executeAsync(fullCommand, callback);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void speedSet(double speed, Callback callback) {
        int speedValue = (int) (100.0d * speed);
        String fullCommand = generateFullCommand(PATH_PLAYER, "SPEED", String.valueOf(speedValue));
        executeAsync(fullCommand, callback);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void end(Callback callback) {
        String fullCommand = generateFullCommand(PATH_PLAYER, "EXIT_PLAYER", "");
        executeAsync(fullCommand, callback);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void clarityUP(Callback callback) {
        String fullCommand = generateFullCommand(PATH_PLAYER, "CHANGE_RATE", "UP");
        executeAsync(fullCommand, callback);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void clarityDown(Callback callback) {
        String fullCommand = generateFullCommand(PATH_PLAYER, "CHANGE_RATE", "DOWN");
        executeAsync(fullCommand, callback);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void claritySet(String value, Callback callback) {
        String fullCommand = generateFullCommand(PATH_PLAYER, "CHANGE_RATE", value);
        executeAsync(fullCommand, callback);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void collect(Callback callback) {
        String fullCommand = generateFullCommand(PATH_PLAYER, "CHANGE_FAVOR", "TRUE");
        executeAsync(fullCommand, callback);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void collectCancel(Callback callback) {
        String fullCommand = generateFullCommand(PATH_PLAYER, "CHANGE_FAVOR", "FALSE");
        executeAsync(fullCommand, callback);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void skipBegin(Callback callback) {
        String fullCommand = generateFullCommand(PATH_PLAYER, "SKIP_VIDEO_HEAD", "");
        executeAsync(fullCommand, callback);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void skipEnd(Callback callback) {
        String fullCommand = generateFullCommand(PATH_PLAYER, "SKIP_VIDEO_TILE", "");
        executeAsync(fullCommand, callback);
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void setMediaPlayMode(Callback callback) {
        if (callback != null) {
            callback.onResult(3);
        }
    }

    @Override // com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler
    public void setVideoPlayMode(Callback callback) {
        if (callback != null) {
            callback.onResult(3);
        }
    }

    public String generateFullCommand(String path, String command, String param) {
        Uri.Builder builder = new Uri.Builder().scheme("iqiyi").authority(Constants.PACKAGE_QIYI_IV).appendPath(path).appendQueryParameter("from", "xui").appendQueryParameter("command", command);
        if (!TextUtils.isEmpty(param)) {
            builder.appendQueryParameter("param", param);
        }
        return builder.build().toString();
    }

    private void executeAsync(final String command, final Callback callback) {
        IQYSService iQYSService = this.mIQYSService;
        if (iQYSService != null) {
            iQYSService.executeAsync(new IQYSRequest(command), new IQYSService.Callback() { // from class: com.xiaopeng.xuiservice.xapp.video.handler.QIYIVideoHandler.4
                @Override // com.qiyi.video.client.IQYSService.Callback
                public void onResponse(IQYSResponse iqysResponse) {
                    LogUtil.d(QIYIVideoHandler.TAG, "executeAsync responseCode:" + iqysResponse.mCode + " &command:" + command);
                    Callback callback2 = callback;
                    if (callback2 != null) {
                        callback2.onResult(QIYIVideoHandler.this.getResultCode(iqysResponse));
                    }
                }
            });
            return;
        }
        LogUtil.w(TAG, "executeAsync service not can connected");
        callback.onResult(6);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getResultCode(IQYSResponse iqysResponse) {
        if (iqysResponse == null) {
            return 1;
        }
        int i = iqysResponse.mCode;
        if (i != -405 && i != -404 && i != -400) {
            if (i != -202) {
                if (i != -6) {
                    if (i == 200) {
                        return 0;
                    }
                    switch (i) {
                        case -100405:
                        case -100404:
                            break;
                        default:
                            switch (i) {
                                case RESPONSE_CODE_PLAYER_SEEK_END /* -100206 */:
                                case RESPONSE_CODE_PLAYER_SEEK_HEAD /* -100205 */:
                                    break;
                                case RESPONSE_CODE_PLAYER_BUY_VIP /* -100204 */:
                                case RESPONSE_CODE_PLAYER_LOGIN /* -100203 */:
                                case RESPONSE_CODE_PLAYER_TRIAL_END /* -100202 */:
                                    return 4;
                                default:
                                    return 1;
                            }
                    }
                } else {
                    return 6;
                }
            }
            return 5;
        }
        return 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerQIYIService() {
        StringBuilder sb = new StringBuilder();
        sb.append("registerQIYIService getService serviceNull:");
        sb.append(IQYSManager.getInstance().getService() == null);
        LogUtil.d(TAG, sb.toString());
        if (this.mIQYSService == null) {
            IQYSManager.getInstance().register(ActivityThread.currentActivityThread().getApplication().getApplicationContext(), new IQYSManager.RegisterListener() { // from class: com.xiaopeng.xuiservice.xapp.video.handler.QIYIVideoHandler.5
                @Override // com.qiyi.video.client.IQYSManager.RegisterListener
                public void onRegisterSuccess(IQYSService iqysService) {
                    QIYIVideoHandler.this.mIQYSService = iqysService;
                    LogUtil.i(QIYIVideoHandler.TAG, "onRegisterSuccess");
                }

                @Override // com.qiyi.video.client.IQYSManager.RegisterListener
                public void onRegisterFailure(int i, String s) {
                    QIYIVideoHandler.this.mIQYSService = null;
                    LogUtil.w(QIYIVideoHandler.TAG, "onRegisterFailure code:" + i + " &message:" + s);
                }
            });
        }
    }
}
