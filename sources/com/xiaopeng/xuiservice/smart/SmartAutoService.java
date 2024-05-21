package com.xiaopeng.xuiservice.smart;

import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.mcu.CarMcuManager;
import android.car.hardware.tbox.CarTboxManager;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.text.TextUtils;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.MccTable;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.PrintWriter;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class SmartAutoService extends BaseSmartService {
    private static final String TAG = "SmartAutoService";
    private static final String XP_AUTO_POWER_OFF_INTENT = "com.xiaopeng.intent.action.AUTO_POWER_OFF";
    private int MAX_SECOND;
    private Runnable auto_power_off_timeout_thread;
    private long bootupTimeInterval;
    private boolean isAutoPowerOFF;
    private long lastBootupTime;
    private CarMcuManager.CarMcuEventCallback mCarMcuEventCallback;
    private CarTboxManager.CarTboxEventCallback mCarTboxEventCallback;
    private String strCountry;

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService, com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
    public /* bridge */ /* synthetic */ void debugDump(PrintWriter printWriter, String[] strArr) {
        super.debugDump(printWriter, strArr);
    }

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService
    public /* bridge */ /* synthetic */ void dump(PrintWriter printWriter, String[] strArr) {
        super.dump(printWriter, strArr);
    }

    private SmartAutoService(Context context) {
        super(context);
        this.strCountry = "";
        this.isAutoPowerOFF = false;
        this.bootupTimeInterval = 10000L;
        this.lastBootupTime = 0L;
        this.MAX_SECOND = 600;
        this.auto_power_off_timeout_thread = new Runnable() { // from class: com.xiaopeng.xuiservice.smart.SmartAutoService.1
            @Override // java.lang.Runnable
            public void run() {
                SmartAutoService.this.isAutoPowerOFF = false;
            }
        };
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        removeTboxManagerListener(this.mCarTboxEventCallback);
        removeMcuManagerListener(this.mCarMcuEventCallback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void initCarListener() {
        this.lastBootupTime = System.currentTimeMillis();
        this.mCarTboxEventCallback = new CarTboxManager.CarTboxEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartAutoService.2
            public void onChangeEvent(CarPropertyValue value) {
                if (value != null && value.getValue() != null) {
                    int propertyId = value.getPropertyId();
                    if (propertyId != 554700817) {
                        if (propertyId == 557912113) {
                            LogUtil.d(SmartAutoService.TAG, "CarTboxEventCallback CarPropertyValue:" + value.toString());
                            if (SmartAutoService.this.effectiveTimeCheck()) {
                                SmartAutoService.this.notifyAutoPowerOff();
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(value.getValue().toString());
                        if (jsonObject.has("mcc")) {
                            int mcc = jsonObject.getInt("mcc");
                            String country = MccTable.countryCodeForMcc(mcc);
                            if (!TextUtils.isEmpty(country) && !SmartAutoService.this.strCountry.equals(country)) {
                                SmartAutoService.this.strCountry = country;
                                SmartAutoService.this.notifyWifiCountryCode(SmartAutoService.this.strCountry, true);
                            }
                        }
                    } catch (Exception e) {
                        LogUtil.d(SmartAutoService.TAG, "handleTboxEvent ID_TBOX_CDU_APN e=" + e);
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarMcuEventCallback = new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartAutoService.3
            public void onChangeEvent(CarPropertyValue carPropertyValue) {
                if (carPropertyValue != null && carPropertyValue.getPropertyId() == 557847635) {
                    LogUtil.d(SmartAutoService.TAG, "CarMcuEventCallback CarPropertyValue:" + carPropertyValue.toString());
                    if (carPropertyValue.getValue() instanceof Integer) {
                        int value = ((Integer) carPropertyValue.getValue()).intValue();
                        if (value == 0) {
                            SmartAutoService.this.notifyAutoPowerOff();
                        }
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        addTboxManagerListener(this.mCarTboxEventCallback);
        addMcuManagerListener(this.mCarMcuEventCallback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        LogUtil.d(TAG, "onCarManagerInited");
        initWifiCountryCode();
    }

    public static SmartAutoService getInstance() {
        return InstanceHolder.sService;
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final SmartAutoService sService = new SmartAutoService(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    private void tickAutoPowerOffTime() {
        XuiWorkHandler.getInstance().removeCallbacks(this.auto_power_off_timeout_thread);
        XuiWorkHandler.getInstance().postDelayed(this.auto_power_off_timeout_thread, 5000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyAutoPowerOff() {
        long currentTime = System.currentTimeMillis();
        LogUtil.d(TAG, "notifyAutoPowerOff currentTime:" + currentTime + " &lastBootupTime:" + this.lastBootupTime + " &isAutoPowerOFF:" + this.isAutoPowerOFF);
        if (!this.isAutoPowerOFF && Math.abs(currentTime - this.lastBootupTime) > this.bootupTimeInterval) {
            LogUtil.d(TAG, "SmartAutoService(tbox|mcu) sendBroadcast: AUTO_POWER_OFF");
            this.isAutoPowerOFF = true;
            Intent intent = new Intent(XP_AUTO_POWER_OFF_INTENT);
            intent.addFlags(16777216);
            BroadcastManager.getInstance().sendBroadcast(intent);
        }
        tickAutoPowerOffTime();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyWifiCountryCode(String country, boolean persist) {
        LogUtil.i(TAG, "notifyWifiCountryCode country " + country + " persist " + persist);
        if (!TextUtils.isEmpty(country)) {
            try {
                WifiManager wifiManager = (WifiManager) this.mContext.getSystemService("wifi");
                wifiManager.setCountryCode(country);
                if (persist) {
                    Settings.Global.putString(this.mContext.getContentResolver(), "wifi_country_code", country);
                }
            } catch (Exception e) {
                LogUtil.e(TAG, "initWifiCountryCode failed");
            }
        }
    }

    private void initWifiCountryCode() {
        String country = Settings.Global.getString(this.mContext.getContentResolver(), "wifi_country_code");
        if (TextUtils.isEmpty(country)) {
            LogUtil.i(TAG, "initWifiCountryCode no country");
        } else {
            notifyWifiCountryCode(country, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean effectiveTimeCheck() {
        CarTboxManager carTboxManager = getCarManager("xp_tbox");
        try {
            int[] time = carTboxManager.getPowerOffCountdown();
            if (time != null) {
                LogUtil.d(TAG, "getPowerOffCountdown time--" + time[0] + ":" + time[1]);
            }
            if (time != null && time[0] != 63 && time[1] != 63) {
                if ((time[0] * 60) + time[1] <= this.MAX_SECOND) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
