package com.xiaopeng.xuiservice.smart;

import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.tbox.CarTboxManager;
import android.content.Context;
import android.os.Message;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.capabilities.XpAutoManager;
import com.xiaopeng.xuiservice.capabilities.XpAvpManager;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import java.io.PrintWriter;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class SmartWifiService extends BaseSmartService implements XpAvpManager.WifiListener {
    private static final boolean DBG = true;
    public static final int MESSAGE_CONNECT_NETWORK = 3;
    public static final int MESSAGE_DISCONNECT_NETWORK = 4;
    public static final int MESSAGE_ENABLE_NETWORK = 2;
    public static final int MESSAGE_INIT_AUTO_WIFI = 5;
    public static final int MESSAGE_OPEN_WIFI = 0;
    public static final int MESSAGE_SCAN_WIFI = 1;
    private static final String TAG = "SmartWifiService";
    private CarTboxManager.CarTboxEventCallback mCarTboxEventCallback;
    private CarTboxManager mCarTboxManager;
    private SmartWifiHandler mSmartWifiHandler;

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService, com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
    public /* bridge */ /* synthetic */ void debugDump(PrintWriter printWriter, String[] strArr) {
        super.debugDump(printWriter, strArr);
    }

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService
    public /* bridge */ /* synthetic */ void dump(PrintWriter printWriter, String[] strArr) {
        super.dump(printWriter, strArr);
    }

    private SmartWifiService(Context context) {
        super(context);
        this.mSmartWifiHandler = new SmartWifiHandler(context);
        XpAvpManager.getInstance(context).setListener(this);
        SmartWifiHandler smartWifiHandler = this.mSmartWifiHandler;
        smartWifiHandler.sendMessageDelayed(smartWifiHandler.obtainMessage(5), 5000L);
    }

    public static SmartWifiService getInstance() {
        return InstanceHolder.sService;
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void initCarListener() {
        this.mCarTboxEventCallback = new CarTboxManager.CarTboxEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartWifiService.1
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557846552) {
                    if (((Integer) value.getValue()).intValue() == 0) {
                        LogUtil.e(SmartWifiService.TAG, "Disconnect AVP Wifi");
                        SmartWifiService.this.mSmartWifiHandler.sendMessage(SmartWifiService.this.mSmartWifiHandler.obtainMessage(4));
                    } else if (((Integer) value.getValue()).intValue() == 1) {
                        LogUtil.e(SmartWifiService.TAG, "Open AVP Wifi");
                        XpAvpManager.getInstance(SmartWifiService.this.mContext).enableAVPWifi(true);
                        SmartWifiService.this.mSmartWifiHandler.sendMessage(SmartWifiService.this.mSmartWifiHandler.obtainMessage(0));
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        addTboxManagerListener(this.mCarTboxEventCallback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        LogUtil.d(TAG, "onCarManagerInited");
        this.mCarTboxManager = getCarManager("xp_tbox");
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        removeTboxManagerListener(this.mCarTboxEventCallback);
        XpAutoManager.getInstance().release();
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAvpManager.WifiListener
    public synchronized void scanResultComplete() {
        this.mSmartWifiHandler.sendMessage(this.mSmartWifiHandler.obtainMessage(2));
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAvpManager.WifiListener
    public synchronized void setWifiGwInfo(int wifiState, int wifiType, String wifiIp) {
        LogUtil.d(TAG, "setWifiGwInfo wifiState:" + wifiState + " wifiType:" + wifiType + " wifiIp:" + wifiIp);
        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        try {
            subJson.put("state", wifiState);
            subJson.put(SpeechConstants.KEY_COMMAND_TYPE, wifiType);
            subJson.put("gw", wifiIp);
            jsonObject.put("id", "wifi_state");
            jsonObject.put("content", subJson);
        } catch (JSONException e) {
        }
        try {
            this.mCarTboxManager.setWifiGwInfo(jsonObject.toString());
        } catch (Exception e2) {
            handleException(e2);
        }
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final SmartWifiService sService = new SmartWifiService(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    /* loaded from: classes5.dex */
    public class SmartWifiHandler extends XuiWorkHandler {
        private Context context;

        public SmartWifiHandler(Context context) {
            this.context = context;
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LogUtil.d(SmartWifiService.TAG, "handleMessage: msg.what=" + msg.what);
            int i = msg.what;
            if (i == 0) {
                XpAvpManager.getInstance(this.context).openWifi();
                SmartWifiService.this.mSmartWifiHandler.sendMessageDelayed(SmartWifiService.this.mSmartWifiHandler.obtainMessage(1), 100L);
            } else if (i != 1) {
                if (i == 2) {
                    XpAvpManager.getInstance(this.context).addNetWork(XpAvpManager.getInstance(this.context).createAVPWifiInfo());
                    SmartWifiService.this.mSmartWifiHandler.sendMessageDelayed(SmartWifiService.this.mSmartWifiHandler.obtainMessage(3), 100L);
                    return;
                } else if (i == 3) {
                    XpAvpManager.getInstance(this.context).connect(XpAvpManager.getInstance(this.context).createAVPWifiInfo());
                    return;
                } else if (i == 4) {
                    XpAvpManager.getInstance(this.context).disconnectWifi();
                    return;
                } else if (i == 5) {
                    XpAutoManager.getInstance().init();
                    return;
                } else {
                    return;
                }
            }
            XpAvpManager.getInstance(this.context).startScan();
            SmartWifiService.this.mSmartWifiHandler.sendMessageDelayed(SmartWifiService.this.mSmartWifiHandler.obtainMessage(2), 1000L);
        }
    }
}
