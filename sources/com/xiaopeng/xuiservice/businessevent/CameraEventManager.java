package com.xiaopeng.xuiservice.businessevent;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.mcu.CarMcuManager;
import android.car.hardware.tbox.CarTboxManager;
import android.car.hardware.vcu.CarVcuManager;
import android.content.Context;
import android.content.Intent;
import android.os.SystemProperties;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class CameraEventManager {
    private static final String KEY_MESSAGE_ID = "msg_id";
    private static final String KEY_MESSAGE_REF = "msg_ref";
    private static final String KEY_MESSAGE_TYPE = "msg_type";
    private static final String KEY_SERVICE_TYPE = "service_type";
    private static final String MSG_FROM_CLIENT_PREFIX = "20";
    private static final String MSG_JASON_BODY_BASIC = "{\"msg_id\":\"123\",\"msg_type\":1,\"service_type\":11,\"msg_content\":{\"code\":9},\"msg_ref\":\"123\"}";
    private static final String MSG_TYPE_FROM_CLIENT = "1";
    private static final String VALUE_MSG_EXCLUDE_TYPE = "4";
    private static final String VALUE_SERVICE_TYPE = "11";
    private static final String notifyPackageName = "com.xiaopeng.xmart.camera";
    private static final String TAG = CameraEventManager.class.getSimpleName();
    private static boolean notifyApp = false;

    /* loaded from: classes5.dex */
    private static class CameraEventManagerHolder {
        private static final CameraEventManager sInstance = new CameraEventManager();

        private CameraEventManagerHolder() {
        }
    }

    private CameraEventManager() {
        addCanSignalListener();
        DumpDispatcher.registerDump("business_camera", new DumpDispatcher.DebugDumper() { // from class: com.xiaopeng.xuiservice.businessevent.-$$Lambda$CameraEventManager$adNJYJrP2oChhLuQfBsGxW2y9Yo
            @Override // com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
            public final void debugDump(PrintWriter printWriter, String[] strArr) {
                CameraEventManager.this.lambda$new$0$CameraEventManager(printWriter, strArr);
            }
        });
    }

    public static CameraEventManager getInstance() {
        return CameraEventManagerHolder.sInstance;
    }

    public void init() {
        ArrayList<String> filter = new ArrayList<>();
        filter.add("com.xiaopeng.xui.businessevent");
        BroadcastManager.getInstance().registerListener(new BroadcastManager.BroadcastListener() { // from class: com.xiaopeng.xuiservice.businessevent.-$$Lambda$CameraEventManager$w_e-NZzPOhWkjdxMHchkLnWnh2U
            @Override // com.xiaopeng.xuiservice.utils.BroadcastManager.BroadcastListener
            public final void onReceive(Context context, Intent intent) {
                CameraEventManager.lambda$init$1(context, intent);
            }
        }, filter);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$init$1(Context context, Intent intent) {
        LogUtil.i(TAG, "get intent=" + intent);
        if ("com.xiaopeng.xui.businessevent".equals(intent.getAction())) {
            String moduleName = null;
            String status = null;
            if (intent.hasExtra("module")) {
                moduleName = intent.getStringExtra("module");
            }
            if (intent.hasExtra("status")) {
                status = intent.getStringExtra("status");
            }
            if (notifyPackageName.equals(moduleName)) {
                LogUtil.i(TAG, "module=" + moduleName + ",stat=" + status);
                char c = 65535;
                int hashCode = status.hashCode();
                if (hashCode != 48) {
                    if (hashCode == 49 && status.equals("1")) {
                        c = 0;
                    }
                } else if (status.equals("0")) {
                    c = 1;
                }
                if (c == 0) {
                    notifyApp = false;
                } else if (c == 1) {
                    notifyApp = true;
                }
            }
        }
    }

    /* renamed from: dump */
    public void lambda$new$0$CameraEventManager(PrintWriter pw, String[] args) {
        String str = TAG;
        LogUtil.i(str, "dump " + TAG);
        HashMap<String, String> map = new HashMap<>();
        map.put("key1", "val1");
        map.put("key2", "val2");
        map.put("key3", "val3");
        feedbackMessageWhenIgOn("abcdefg1234567##");
    }

    private void addCanSignalListener() {
        CarClientManager.getInstance().addMcuManagerListener(new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.businessevent.CameraEventManager.1
            public void onChangeEvent(CarPropertyValue value) {
                CameraEventManager.this.handlePropertyChange(value);
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        });
        CarClientManager.getInstance().addTboxManagerListener(new CarTboxManager.CarTboxEventCallback() { // from class: com.xiaopeng.xuiservice.businessevent.CameraEventManager.2
            public void onChangeEvent(CarPropertyValue value) {
                CameraEventManager.this.handlePropertyChange(value);
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        });
        CarClientManager.getInstance().addVcuManagerListener(new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xuiservice.businessevent.CameraEventManager.3
            public void onChangeEvent(CarPropertyValue carPropertyValue) {
                CameraEventManager.this.handlePropertyChange(carPropertyValue);
            }

            public void onErrorEvent(int i, int i1) {
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePropertyChange(CarPropertyValue value) {
        int propertyId = value.getPropertyId();
        switch (propertyId) {
            case 554700825:
                int status = getIgStatus();
                String str = TAG;
                LogUtil.d(str, "get ig status=" + status);
                if (1 != status) {
                    if (2 == status && notifyApp) {
                        Object propValue = value.getValue();
                        if (propValue instanceof String) {
                            HashMap<String, String> eventMap = new HashMap<>();
                            eventMap.put(String.valueOf(554700825), String.valueOf(propValue));
                            BusinessEventManager.getInstance().notifyApp(notifyPackageName, eventMap);
                            return;
                        }
                        return;
                    }
                    return;
                }
                Object propValue2 = value.getValue();
                if (propValue2 instanceof String) {
                    try {
                        JSONObject msg = new JSONObject((String) propValue2);
                        String service_type = null;
                        String msg_ref = null;
                        if (msg.has(KEY_SERVICE_TYPE)) {
                            service_type = msg.getString(KEY_SERVICE_TYPE);
                        }
                        String str2 = TAG;
                        LogUtil.d(str2, "ID_TBOX_CAMERA_REMOTE_CTRL,service type:" + service_type);
                        if (VALUE_SERVICE_TYPE.equals(service_type)) {
                            if (msg.has(KEY_MESSAGE_ID)) {
                                msg_ref = msg.getString(KEY_MESSAGE_ID);
                            }
                            feedbackMessageWhenIgOn(msg_ref);
                            return;
                        }
                        return;
                    } catch (Exception e) {
                        String str3 = TAG;
                        LogUtil.w(str3, "handle ID_TBOX_CAMERA_REMOTE_CTRL e=" + e.toString());
                        return;
                    }
                }
                String str4 = TAG;
                LogUtil.w(str4, "handle ID_TBOX_CAMERA_REMOTE_CTRL,invalid value class=" + propValue2.getClass());
                return;
            case 557847161:
                int status2 = getIgStatus();
                String str5 = TAG;
                LogUtil.i(str5, "ID_VCU_BAT_BUMP_RECRD,ig status=" + status2 + ",id=" + propertyId);
                if (1 == status2 && ((Integer) value.getValue()).intValue() == 1) {
                    try {
                        HashMap<String, String> eventMap2 = new HashMap<>();
                        String eventValue = String.valueOf((Integer) value.getValue());
                        eventMap2.put(String.valueOf(propertyId), eventValue);
                        LogUtil.i(TAG, "notify:com.xiaopeng.xmart.camera");
                        BusinessEventManager.getInstance().notifyApp(notifyPackageName, eventMap2);
                        return;
                    } catch (Exception e2) {
                        String str6 = TAG;
                        LogUtil.w(str6, "handle propertyId e=" + e2.toString());
                        return;
                    }
                }
                return;
            case 557847561:
                String str7 = TAG;
                LogUtil.i(str7, "##ID_MCU_IG_STATUS:" + value.getValue());
                int status3 = ((Integer) value.getValue()).intValue();
                if (2 == status3) {
                    HashMap<String, String> eventMap3 = new HashMap<>();
                    eventMap3.put(String.valueOf(557847561), String.valueOf(status3));
                    BusinessEventManager.getInstance().notifyApp(notifyPackageName, eventMap3);
                    notifyApp = true;
                    return;
                }
                notifyApp = false;
                return;
            case 557912117:
                int status4 = getIgStatus();
                String str8 = TAG;
                LogUtil.i(str8, "ID_TBOX_GUARD,ig status=" + status4 + ",id=" + propertyId);
                if (1 != status4) {
                    try {
                        HashMap<String, String> eventMap4 = new HashMap<>();
                        eventMap4.put(String.valueOf(propertyId), "1");
                        LogUtil.i(TAG, "notify:com.xiaopeng.xmart.camera");
                        BusinessEventManager.getInstance().notifyApp(notifyPackageName, eventMap4);
                        return;
                    } catch (Exception e3) {
                        String str9 = TAG;
                        LogUtil.w(str9, "handle propertyId e=" + e3.toString());
                        return;
                    }
                }
                return;
            default:
                return;
        }
    }

    private int getIgStatus() {
        CarMcuManager manager = CarClientManager.getInstance().getCarManager("xp_mcu");
        if (manager == null) {
            return -1;
        }
        try {
            int status = manager.getIgStatusFromMcu();
            return status;
        } catch (Exception e) {
            String str = TAG;
            LogUtil.w(str, "getIgStatusFromMcu e=" + e.toString());
            return -1;
        }
    }

    private int getVehicleId() {
        return SystemProperties.getInt("persist.sys.vehicle_id", -1);
    }

    private void feedbackMessageWhenIgOn(String msgRef) {
        String msgId = "20" + getVehicleId() + System.currentTimeMillis() + ((int) (Math.random() * 10.0d)) + ((int) (Math.random() * 10.0d));
        try {
            JSONObject msg = new JSONObject(MSG_JASON_BODY_BASIC);
            msg.put(KEY_MESSAGE_ID, msgId);
            msg.put(KEY_MESSAGE_REF, msgRef);
            String msgString = msg.toString();
            LogUtil.d(TAG, "feedbackMessageWhenIgOn,msg=" + msgString);
            CarTboxManager manager = CarClientManager.getInstance().getCarManager("xp_tbox");
            if (manager != null) {
                manager.setCameraRemoteControlFeedback(msgString);
            } else {
                LogUtil.w(TAG, "get CarTboxManager null");
            }
        } catch (Exception e) {
            LogUtil.w(TAG, "feedbackMessageWhenIgOn e=" + e);
        }
    }
}
