package com.xiaopeng.xuiservice.xapp.temperature;

import android.car.Car;
import android.car.hardware.hvac.CarHvacManager;
import android.car.hardware.mcu.CarMcuManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes5.dex */
public class CarSettingsManager {
    private static final String TAG = "CarSettingsManager";
    private static CarSettingsManager sInstance;
    private Car mCarApiClient;
    private List<OnServiceConnectCompleteListener> mOnServiceConnectCompleteListenerList = new ArrayList();
    private CarHvacManager mCarHvacManager = CarClientManager.getInstance().getCarManager("hvac");
    private CarMcuManager mCarMcuManager = CarClientManager.getInstance().getCarManager("xp_mcu");

    /* loaded from: classes5.dex */
    public interface OnServiceConnectCompleteListener {
        void onServiceConnectComplete();
    }

    public void addServiceConnectCompleteListener(OnServiceConnectCompleteListener listener) {
        if (!this.mOnServiceConnectCompleteListenerList.contains(listener)) {
            this.mOnServiceConnectCompleteListenerList.add(listener);
        }
    }

    public void removeServiceConnectCompleteListener(OnServiceConnectCompleteListener listener) {
        this.mOnServiceConnectCompleteListenerList.remove(listener);
    }

    public void notifyServiceConnectComplete() {
        for (OnServiceConnectCompleteListener listener : this.mOnServiceConnectCompleteListenerList) {
            listener.onServiceConnectComplete();
        }
    }

    public static CarSettingsManager getInstance() {
        if (sInstance == null) {
            synchronized (CarSettingsManager.class) {
                if (sInstance == null) {
                    sInstance = new CarSettingsManager();
                }
            }
        }
        return sInstance;
    }

    private CarSettingsManager() {
    }

    public int getMinTemperature() {
        int minTemperature = 0;
        LogUtil.d(TAG, "carservice getMaxWindSpeed ");
        CarHvacManager carHvacManager = this.mCarHvacManager;
        if (carHvacManager == null) {
            return 0;
        }
        try {
            minTemperature = carHvacManager.getMinHavcTemperature();
            LogUtil.d(TAG, "carservice getMaxWindSpeed over minTemperature:" + minTemperature);
            return minTemperature;
        } catch (Throwable th) {
            LogUtil.e(TAG, "getMinHavcTemperature error");
            return minTemperature;
        }
    }

    public int getMaxTemperature() {
        int maxTemperature = 0;
        LogUtil.d(TAG, "carservice getMaxWindSpeed ");
        CarHvacManager carHvacManager = this.mCarHvacManager;
        if (carHvacManager == null) {
            return 0;
        }
        try {
            maxTemperature = carHvacManager.getMaxHavcTemperature();
            LogUtil.d(TAG, "carservice getMaxWindSpeed over maxTemperature:" + maxTemperature);
            return maxTemperature;
        } catch (Throwable th) {
            LogUtil.e(TAG, "getMaxHavcTemperature error");
            return maxTemperature;
        }
    }
}
