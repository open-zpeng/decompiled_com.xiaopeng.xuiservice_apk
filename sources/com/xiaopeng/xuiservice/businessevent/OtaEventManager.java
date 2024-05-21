package com.xiaopeng.xuiservice.businessevent;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.mcu.CarMcuManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import java.io.PrintWriter;
import java.util.HashMap;
/* loaded from: classes5.dex */
public class OtaEventManager {
    private static final String TAG = OtaEventManager.class.getSimpleName();
    private static final String notifyPackageName = "com.xiaopeng.ota";

    /* loaded from: classes5.dex */
    private static class OtaEventManagerHolder {
        private static final OtaEventManager sInstance = new OtaEventManager();

        private OtaEventManagerHolder() {
        }
    }

    private OtaEventManager() {
        addCanSignalListener();
    }

    public static OtaEventManager getInstance() {
        return OtaEventManagerHolder.sInstance;
    }

    public void init() {
    }

    public void dump(PrintWriter pw, String[] args) {
        String str = TAG;
        LogUtil.i(str, "dump " + TAG);
    }

    private void addCanSignalListener() {
        CarClientManager.getInstance().addMcuManagerListener(new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.businessevent.OtaEventManager.1
            public void onChangeEvent(CarPropertyValue value) {
                OtaEventManager.this.handlePropertyChange(value);
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePropertyChange(CarPropertyValue value) {
        int propertyId = value.getPropertyId();
        if (propertyId == 557847561) {
            String str = TAG;
            LogUtil.i(str, "ID_MCU_IG_STATUS:" + value.getValue());
            int status = ((Integer) value.getValue()).intValue();
            if (status == 1 || status == 2) {
                HashMap<String, String> eventMap = new HashMap<>();
                eventMap.put(String.valueOf(557847561), String.valueOf(status));
                LogUtil.i(TAG, "notify:com.xiaopeng.ota");
                BusinessEventManager.getInstance().notifyApp("com.xiaopeng.ota", eventMap);
            }
        }
    }
}
