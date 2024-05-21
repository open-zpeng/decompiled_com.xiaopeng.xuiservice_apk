package com.xiaopeng.xuiservice.utils;

import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.bcm.CarBcmManager;
import android.content.Context;
import android.os.SystemProperties;
import android.provider.Settings;
import com.xiaopeng.speech.protocol.event.OOBEEvent;
import com.xiaopeng.util.FeatureOption;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.carcontrol.CarClientProxy;
import java.util.Random;
/* loaded from: classes5.dex */
public class CarControlUtils extends CarClientProxy {
    private static final int LAMP_TURNOFF_MIN_INTERVAL = 400;
    public static final int RESTORED_STATUS = -1;
    private static final String TAG = "CarControlUtils";
    public static final int TURNOFF_LAMP_LANGLIGHT = 3;
    public static final int TURNOFF_LAMP_LIGHTDANCE = 1;
    public static final int TURNOFF_LAMP_MODEMONITER = 4;
    public static final int TURNOFF_LAMP_SAYHI = 0;
    public static final int TURNOFF_LAMP_XSAYHI = 2;
    public static final int Waiting_For_SayHi = 1;
    private Context mContext;
    private int saveDayLightMode;
    private int saveHeadLampMode;
    private static final boolean hasSeriesD = XUIConfig.isProductSeriesD();
    private static CarClientManager mCar = CarClientManager.getInstance();
    private static long last_turnoff_time = 0;
    private static long last_turnOnOff_settime = 0;
    private static int savedTurnOnOff = -1;
    private static int lastTurnOffReason = -1;
    private static Object turnOffLightLock = new Object();
    private static Object mLock = new Object();
    private static Object mTurnOff = new Object();
    private static Object mRestore = new Object();
    private static CarBcmManager mCarBcmManager = null;

    private static void getCarBcmManager() {
        mCarBcmManager = CarClientManager.getInstance().getCarManager("xp_bcm");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class CarControlUtilsHolder {
        private static final CarControlUtils sInstance = new CarControlUtils();

        private CarControlUtilsHolder() {
        }
    }

    private CarControlUtils() {
        this.saveHeadLampMode = -1;
        this.saveDayLightMode = 0;
        this.mContext = ActivityThread.currentActivityThread().getApplication();
        setCarControl(CarClientManager.getInstance());
        getCarBcmManager();
        if (!hasSeriesD) {
            CarClientManager.getInstance().addBcmManagerListener(new CarBcmManager.CarBcmEventCallback() { // from class: com.xiaopeng.xuiservice.utils.CarControlUtils.1
                public void onChangeEvent(CarPropertyValue value) {
                    if (value.getPropertyId() != 557849640) {
                        return;
                    }
                    LogUtil.d(CarControlUtils.TAG, "headLight onChangeEvent " + ((Integer) value.getValue()).intValue());
                    if (((Integer) value.getValue()).intValue() == 0) {
                        synchronized (CarControlUtils.mTurnOff) {
                            CarControlUtils.mTurnOff.notify();
                        }
                        return;
                    }
                    synchronized (CarControlUtils.mRestore) {
                        CarControlUtils.mRestore.notify();
                    }
                }

                public void onErrorEvent(int propertyId, int zone) {
                }
            });
        }
    }

    private void setLastStatus(int status) {
        SystemProperties.set("sys.xiaopeng.headlight_status", String.valueOf(status));
    }

    private int getLastStatus() {
        return SystemProperties.getInt("sys.xiaopeng.headlight_status", -1);
    }

    public static CarControlUtils getInstance() {
        return CarControlUtilsHolder.sInstance;
    }

    public void setHeadLight(boolean restore) {
        synchronized (mLock) {
            int i = -1;
            if (restore) {
                if (getLastStatus() == -1) {
                    return;
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("setHeadLight ");
            sb.append(restore ? "RESTORE" : "OFF");
            LogUtil.i(TAG, sb.toString());
            if (XUIConfig.isProductSeriesD()) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(restore ? "0:1:" : "1:0:");
                sb2.append(new Random().nextInt());
                String cmd = sb2.toString();
                LogUtil.i(TAG, "setHeadLight,cmd=" + cmd + ",restore=" + restore);
                Settings.System.putString(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "lamp_control", cmd);
                if (!restore) {
                    i = 1;
                }
                setLastStatus(i);
            } else {
                try {
                    CarBcmManager bcm = mCar.getCarManager("xp_bcm");
                    int lampStatus = getLastStatus();
                    if (restore) {
                        int on = (lampStatus == 3 && FeatureOption.FO_PARKING_FMB_LIGHT_AUTO_TYPE == 1 && bcm.getParkLightRelatedFMBLightConfigState() == 1) ? 4 : lampStatus;
                        LogUtil.d(TAG, "restore lampStatus=" + on + " (last=" + lampStatus + ")");
                        if (lampStatus != bcm.getHeadLampGroup() || lampStatus != on) {
                            synchronized (mRestore) {
                                try {
                                    bcm.setHeadLampGroup(on);
                                    mRestore.wait(500L);
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append("restore HeadLight ");
                                    sb3.append(lampStatus == bcm.getHeadLampGroup() ? "succeed" : "failed");
                                    LogUtil.d(TAG, sb3.toString());
                                } catch (Exception e) {
                                }
                            }
                        }
                    } else {
                        int lampStatus2 = bcm.getHeadLampGroup();
                        setLastStatus(lampStatus2);
                        LogUtil.d(TAG, "save lampStatus=" + lampStatus2);
                        if (lampStatus2 != 0) {
                            synchronized (mTurnOff) {
                                try {
                                    bcm.setHeadLampGroup(0);
                                    mTurnOff.wait(500L);
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append("turn off HeadLight ");
                                    sb4.append(bcm.getHeadLampGroup() == 0 ? "succeed" : "failed");
                                    LogUtil.d(TAG, sb4.toString());
                                } catch (Exception e2) {
                                }
                            }
                        }
                    }
                } catch (Exception e3) {
                    LogUtil.e(TAG, "setHeadLight failed " + e3);
                }
            }
        }
    }

    public int turnOffAndRestoreLight(int turnoffReason, boolean turnoff) {
        int lampGroup;
        if (XUIConfig.isProductSeriesD()) {
            StringBuilder sb = new StringBuilder();
            sb.append(turnoff ? "1:0:" : "0:1:");
            sb.append(new Random().nextInt());
            String cmd = sb.toString();
            LogUtil.i(TAG, "turnOffAndRestoreLight,turnoff=" + turnoff + ",cmd=" + cmd);
            synchronized (turnOffLightLock) {
                Settings.System.putString(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "lamp_control", cmd);
            }
            return 0;
        }
        synchronized (turnOffLightLock) {
            if (turnoff) {
                try {
                    lastTurnOffReason = turnoffReason;
                } catch (Exception e) {
                    LogUtil.e(TAG, "turnOffAndRestoreLight e=" + e);
                }
            }
            if (savedTurnOnOff == turnoff) {
                LogUtil.d(TAG, "turnOffAndRestoreLight same setted :" + turnoffReason + "|turnoff:" + turnoff);
                return 0;
            }
            LogUtil.i(TAG, "turnOffAndRestoreLight reason:" + turnoffReason + "savedTurnOnOff:" + savedTurnOnOff + " |turnoff:" + turnoff + ",saveHeadLampMode=" + this.saveHeadLampMode);
            getCarBcmManager();
            if (mCarBcmManager != null) {
                savedTurnOnOff = -1;
                SystemProperties.set("persist.sys.xp.brightness.dark.ignore", turnoff ? OOBEEvent.STRING_TRUE : OOBEEvent.STRING_FALSE);
                if (turnoff) {
                    LogUtil.d(TAG, "setHeadLampGroup & DayLight OFF");
                    int tempHeadLampMode = mCarBcmManager.getHeadLampGroup();
                    this.saveHeadLampMode = tempHeadLampMode;
                    lampGroup = 0;
                } else {
                    lampGroup = this.saveHeadLampMode;
                }
                if (lampGroup != -1) {
                    mCarBcmManager.setHeadLampGroup(lampGroup);
                    savedTurnOnOff = turnoff ? 1 : 0;
                }
                mCarBcmManager.setDayLightMode(turnoff ? 0 : 1);
            } else {
                LogUtil.w(TAG, "turnOffAndRestoreLight, CarBcmManager not available");
            }
            return 0;
        }
    }

    public void saveLampOffstatus(boolean turnOff) {
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isLluTriggerLampOff", turnOff ? 1 : 0);
    }

    public boolean getLampOffstatus() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isLluTriggerLampOff", 0) == 1;
    }

    public void recoveryLampSaveMode(int mode) {
        if (mode == -1) {
            if (System.currentTimeMillis() - last_turnOnOff_settime > 200) {
                this.saveHeadLampMode = -1;
                return;
            }
            return;
        }
        this.saveHeadLampMode = mode;
    }

    public void recoveryTurnOnoffSaveMode(int mode) {
        savedTurnOnOff = mode;
    }

    public static boolean getTurnLampStatus() {
        try {
            getCarBcmManager();
            if (mCarBcmManager != null) {
                int[] lampStatus = mCarBcmManager.getLeftAndRightTurnLampsActiveStatus();
                if (lampStatus[0] != 1) {
                    if (lampStatus[1] != 1) {
                        return false;
                    }
                }
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public void setHighBeamOnOff(int type) {
        try {
            LogUtil.i(TAG, "setHighBeamOnOff " + type);
            getCarBcmManager();
            if (mCarBcmManager != null) {
                mCarBcmManager.setHighBeamOnOff(type);
            }
        } catch (Exception e) {
        }
    }

    public void setWindowOnOff(int type) {
        try {
            getCarBcmManager();
            if (mCarBcmManager != null) {
                int pos = 0;
                if (type == 1) {
                    pos = 100;
                }
                mCarBcmManager.setWindowsMovePositions(pos, pos, -1.0f, -1.0f);
            }
        } catch (Exception e) {
        }
    }

    public void setScissorsLeftGateOnOff(int type) {
        try {
            getCarBcmManager();
            if (mCarBcmManager != null) {
                mCarBcmManager.setLeftSdcAutoControl(type);
            }
        } catch (Exception e) {
        }
    }

    public void setScissorsRightGateOnOff(int type) {
        try {
            getCarBcmManager();
            if (mCarBcmManager != null) {
                mCarBcmManager.setRightSdcAutoControl(type);
            }
        } catch (Exception e) {
        }
    }

    public void setMirrorOnOff(int type) {
        try {
            getCarBcmManager();
            if (mCarBcmManager != null) {
                LogUtil.d(TAG, "setMirrorOnOff :" + type);
                mCarBcmManager.setRearViewMirror(type);
            }
        } catch (Exception e) {
        }
    }
}
