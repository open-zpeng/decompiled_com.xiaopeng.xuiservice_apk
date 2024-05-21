package com.xiaopeng.xuiservice.businessevent;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.mcu.CarMcuManager;
import android.car.hardware.vcu.CarVcuManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import java.util.HashMap;
/* loaded from: classes5.dex */
public class AppStoreEventManager {
    private static final String TAG = AppStoreEventManager.class.getSimpleName();
    private static final String notifyPackageName = "com.xiaopeng.appstore";
    private int lastGearLevel;
    private CarMcuManager.CarMcuEventCallback mCarMcuEventCallback;
    private CarVcuManager.CarVcuEventCallback mCarVcuEventCallback;

    /* loaded from: classes5.dex */
    private static class AppStoreEventManagerHolder {
        private static final AppStoreEventManager instance = new AppStoreEventManager();

        private AppStoreEventManagerHolder() {
        }
    }

    private AppStoreEventManager() {
        this.lastGearLevel = -1;
        this.mCarMcuEventCallback = new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.businessevent.AppStoreEventManager.1
            public void onChangeEvent(CarPropertyValue value) {
                AppStoreEventManager.this.handlePropertyChange(value);
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarVcuEventCallback = new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xuiservice.businessevent.AppStoreEventManager.2
            public void onChangeEvent(CarPropertyValue value) {
                AppStoreEventManager.this.handlePropertyChange(value);
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
    }

    public static AppStoreEventManager getInstance() {
        return AppStoreEventManagerHolder.instance;
    }

    public void init() {
        addCanSignalListener();
    }

    private void addCanSignalListener() {
        CarClientManager.getInstance().addMcuManagerListener(this.mCarMcuEventCallback);
        CarClientManager.getInstance().addVcuManagerListener(this.mCarVcuEventCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePropertyChange(CarPropertyValue value) {
        int propertyId = value.getPropertyId();
        if (propertyId != 557847045) {
            if (propertyId == 557847561) {
                int status = ((Integer) value.getValue()).intValue();
                String str = TAG;
                LogUtil.i(str, "##ID_MCU_IG_STATUS:" + status);
                if (1 == status) {
                    HashMap<String, String> eventMap = new HashMap<>();
                    eventMap.put(String.valueOf(557847561), String.valueOf(status));
                    BusinessEventManager.getInstance().notifyApp("com.xiaopeng.appstore", eventMap);
                    return;
                }
                return;
            }
            return;
        }
        int gear = ((Integer) value.getValue()).intValue();
        String str2 = TAG;
        LogUtil.i(str2, "gear changed to:" + gear + ",lastGearLevel=" + this.lastGearLevel);
        if (gear == 4 && this.lastGearLevel != gear) {
            HashMap<String, String> eventMap2 = new HashMap<>();
            eventMap2.put(String.valueOf(4), String.valueOf(gear));
            BusinessEventManager.getInstance().notifyApp("com.xiaopeng.appstore", eventMap2);
        }
        this.lastGearLevel = gear;
    }
}
