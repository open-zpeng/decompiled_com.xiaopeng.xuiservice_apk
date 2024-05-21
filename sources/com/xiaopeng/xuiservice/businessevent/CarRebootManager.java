package com.xiaopeng.xuiservice.businessevent;

import android.app.ActivityThread;
import android.app.AlarmManager;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.bcm.CarBcmManager;
import android.car.hardware.vcu.CarVcuManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import androidx.core.app.NotificationCompat;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
/* loaded from: classes5.dex */
public class CarRebootManager {
    private static final long CHECK_REBOOT_POLICY_INTERVAL = 600000;
    private static final String OTA_STATE_IDLE = "0";
    private static final String OTA_STATE_PROPERTY = "sys.xiaopeng.ota.state";
    private static final String OTA_STATE_RUNNING = "1";
    private static final long SCREEN_RUNTIME = 86400000;
    private static final String TAG = CarRebootManager.class.getSimpleName();
    private AlarmManager.OnAlarmListener mAlarmListener;
    private AlarmManager mAlarmManager;
    private CarBcmManager.CarBcmEventCallback mCarBcmEventCallback;
    private CarVcuManager.CarVcuEventCallback mCarVcuEventCallback;
    private WorkHandler mHandler;
    private boolean mIsCarCharging;
    private boolean mIsCarLock;

    /* loaded from: classes5.dex */
    private static class CarRebootManagerHolder {
        private static final CarRebootManager sInstance = new CarRebootManager();

        private CarRebootManagerHolder() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class WorkHandler extends Handler {
        public WorkHandler(Looper looper) {
            super(looper);
        }

        public void optPost(Runnable runnable) {
            if (getLooper() == Looper.myLooper()) {
                runnable.run();
            } else {
                super.post(runnable);
            }
        }

        public void optPostDelay(Runnable runnable, long delayMillis) {
            if (getLooper() == Looper.myLooper() && delayMillis == 0) {
                runnable.run();
            } else {
                super.postDelayed(runnable, delayMillis);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleChargeStatus(final int status) {
        this.mHandler.optPost(new Runnable() { // from class: com.xiaopeng.xuiservice.businessevent.CarRebootManager.4
            @Override // java.lang.Runnable
            public void run() {
                if (status == 2) {
                    if (!CarRebootManager.this.mIsCarCharging) {
                        CarRebootManager.this.mIsCarCharging = true;
                        CarRebootManager.this.tryToRebootPolicyTask();
                    }
                } else if (CarRebootManager.this.mIsCarCharging) {
                    CarRebootManager.this.mIsCarCharging = false;
                    CarRebootManager.this.cancelRebootPolicyTask();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBcmATWS(final int status) {
        this.mHandler.optPost(new Runnable() { // from class: com.xiaopeng.xuiservice.businessevent.CarRebootManager.5
            @Override // java.lang.Runnable
            public void run() {
                if (status != 0) {
                    if (CarRebootManager.this.mIsCarLock) {
                        CarRebootManager.this.cancelRebootPolicyTask();
                        CarRebootManager.this.mIsCarLock = false;
                    }
                } else if (!CarRebootManager.this.mIsCarLock) {
                    CarRebootManager.this.tryToRebootPolicyTask();
                    CarRebootManager.this.mIsCarLock = true;
                }
            }
        });
    }

    private boolean isInOTA() {
        String state = SystemProperties.get(OTA_STATE_PROPERTY, "0");
        return "1".equals(state);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelRebootPolicyTask() {
        this.mHandler.optPost(new Runnable() { // from class: com.xiaopeng.xuiservice.businessevent.CarRebootManager.6
            @Override // java.lang.Runnable
            public void run() {
                LogUtil.i(CarRebootManager.TAG, "cancelRebootPolicyTask");
                CarRebootManager.this.mAlarmManager.cancel(CarRebootManager.this.mAlarmListener);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryToRebootPolicyTask() {
        this.mHandler.optPost(new Runnable() { // from class: com.xiaopeng.xuiservice.businessevent.CarRebootManager.7
            @Override // java.lang.Runnable
            public void run() {
                boolean isInCharging = CarRebootManager.this.isInCharging();
                boolean isCarLock = CarRebootManager.this.isCarLock();
                String str = CarRebootManager.TAG;
                LogUtil.i(str, "tryToRebootPolicyTask, isInCharging: " + isInCharging + ", isCarLock: " + isCarLock);
                if (isInCharging && isCarLock) {
                    CarRebootManager.this.cancelRebootPolicyTask();
                    LogUtil.i(CarRebootManager.TAG, "schedule reboot alarm");
                    CarRebootManager.this.mAlarmManager.setExact(3, SystemClock.elapsedRealtime() + CarRebootManager.CHECK_REBOOT_POLICY_INTERVAL, "tryToRebootPolicyTask", CarRebootManager.this.mAlarmListener, CarRebootManager.this.mHandler);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isRebootPolicyMeet() {
        boolean isInCharging = isInCharging();
        boolean isCarLock = isCarLock();
        long time = SystemClock.elapsedRealtime();
        boolean isOTA = isInOTA();
        String str = TAG;
        LogUtil.i(str, "isRebootPolicyMeet, isInCharging: " + isInCharging + ", isCarLock: " + isCarLock + ", time: " + time + ", isOTA: " + isOTA);
        return isInCharging && isCarLock && time > 86400000 && !isOTA;
    }

    private CarRebootManager() {
        this.mAlarmListener = new AlarmManager.OnAlarmListener() { // from class: com.xiaopeng.xuiservice.businessevent.CarRebootManager.1
            @Override // android.app.AlarmManager.OnAlarmListener
            public void onAlarm() {
                boolean isRebootPolicyMeet = CarRebootManager.this.isRebootPolicyMeet();
                String str = CarRebootManager.TAG;
                LogUtil.i(str, "onAlarm, isRebootPolicyMeet: " + isRebootPolicyMeet);
                if (!isRebootPolicyMeet) {
                    CarRebootManager.this.tryToRebootPolicyTask();
                    return;
                }
                PowerManager pManager = (PowerManager) ActivityThread.currentActivityThread().getApplication().getSystemService("power");
                pManager.reboot("CarRebootPolicy");
            }
        };
        this.mCarBcmEventCallback = new CarBcmManager.CarBcmEventCallback() { // from class: com.xiaopeng.xuiservice.businessevent.CarRebootManager.2
            public void onChangeEvent(CarPropertyValue carPropertyValue) {
                int id = carPropertyValue.getPropertyId();
                Object propertyValue = carPropertyValue.getValue();
                if (id == 557849647) {
                    String str = CarRebootManager.TAG;
                    LogUtil.d(str, "get ID_BCM_ATWS,val:" + propertyValue);
                    CarRebootManager.this.handleBcmATWS(((Integer) propertyValue).intValue());
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarVcuEventCallback = new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xuiservice.businessevent.CarRebootManager.3
            public void onChangeEvent(CarPropertyValue carPropertyValue) {
                int id = carPropertyValue.getPropertyId();
                Object propertyValue = carPropertyValue.getValue();
                if (id == 557847081) {
                    String str = CarRebootManager.TAG;
                    LogUtil.d(str, "get ID_VCU_CHARGE_STATUS,val:" + propertyValue);
                    CarRebootManager.this.handleChargeStatus(((Integer) propertyValue).intValue());
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
    }

    public static CarRebootManager getInstance() {
        return CarRebootManagerHolder.sInstance;
    }

    public void init() {
        boolean policyEnable = SystemProperties.getBoolean("persist.sys.xiaopeng.fo.reboot", false);
        if (policyEnable) {
            HandlerThread handlerThread = new HandlerThread(TAG);
            handlerThread.start();
            this.mHandler = new WorkHandler(handlerThread.getLooper());
            this.mAlarmManager = (AlarmManager) ActivityThread.currentActivityThread().getApplication().getSystemService(NotificationCompat.CATEGORY_ALARM);
            addCarListener();
        }
    }

    public void destroy() {
        this.mAlarmManager.cancel(this.mAlarmListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCarLock() {
        CarBcmManager carBcmManager = CarClientManager.getInstance().getCarManager("xp_bcm");
        if (carBcmManager != null) {
            try {
                return carBcmManager.getAtwsState() == 0;
            } catch (Exception e) {
                String str = TAG;
                LogUtil.e(str, "isCarLock-->" + e.getMessage());
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInCharging() {
        CarVcuManager carVcuManager = CarClientManager.getInstance().getCarManager("xp_vcu");
        if (carVcuManager != null) {
            try {
                return carVcuManager.getChargeStatus() == 2;
            } catch (Exception e) {
                String str = TAG;
                LogUtil.e(str, "isInCharging-->" + e.getMessage());
            }
        }
        return false;
    }

    private void addCarListener() {
        CarClientManager.getInstance().addBcmManagerListener(this.mCarBcmEventCallback);
        CarClientManager.getInstance().addVcuManagerListener(this.mCarVcuEventCallback);
    }
}
