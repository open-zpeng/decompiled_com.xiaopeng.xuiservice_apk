package com.xiaopeng.xuiservice.opensdk.manager;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.bcm.CarBcmManager;
import android.car.hardware.eps.CarEpsManager;
import android.car.hardware.mcu.CarMcuManager;
import android.car.hardware.srs.CarSrsManager;
import android.car.hardware.tpms.CarTpmsManager;
import android.car.hardware.vcu.CarVcuManager;
import com.car.opensdk.vehicle.car.ICarCondition;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionAccPedal;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionChargePort;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionDrivingDistance;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionSysReady;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionTirePressure;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionWheelAngle;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.PrintWriter;
import java.util.Arrays;
/* loaded from: classes5.dex */
public class CarConditionManager extends BaseManager {
    private static final String MODULE_NAME = ICarCondition.MODULE_NAME;
    private static final String TAG = CarConditionManager.class.getSimpleName();
    private static CarConditionManager instance = null;
    private static IPipeListener mListener = null;
    private final CarMcuManager.CarMcuEventCallback mCarMcuEventCallback = new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.opensdk.manager.CarConditionManager.1
        public void onChangeEvent(CarPropertyValue carPropertyValue) {
            CarConditionManager.this.handleCarPropertyChange(carPropertyValue);
        }

        public void onErrorEvent(int i, int i1) {
        }
    };
    private final CarVcuManager.CarVcuEventCallback mCarVcuEventCallback = new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xuiservice.opensdk.manager.CarConditionManager.2
        public void onChangeEvent(CarPropertyValue carPropertyValue) {
            CarConditionManager.this.handleCarPropertyChange(carPropertyValue);
        }

        public void onErrorEvent(int i, int i1) {
        }
    };
    private final CarBcmManager.CarBcmEventCallback mCarBcmEventCallback = new CarBcmManager.CarBcmEventCallback() { // from class: com.xiaopeng.xuiservice.opensdk.manager.CarConditionManager.3
        public void onChangeEvent(CarPropertyValue value) {
            CarConditionManager.this.handleCarPropertyChange(value);
        }

        public void onErrorEvent(int propertyId, int zone) {
        }
    };
    private final CarSrsManager.CarSrsEventCallback mCarSrsEventCallback = new CarSrsManager.CarSrsEventCallback() { // from class: com.xiaopeng.xuiservice.opensdk.manager.CarConditionManager.4
        public void onChangeEvent(CarPropertyValue carPropertyValue) {
            CarConditionManager.this.handleCarPropertyChange(carPropertyValue);
        }

        public void onErrorEvent(int i, int i1) {
        }
    };
    private final CarTpmsManager.CarTpmsEventCallback mCarTpmsEventCallback = new CarTpmsManager.CarTpmsEventCallback() { // from class: com.xiaopeng.xuiservice.opensdk.manager.CarConditionManager.5
        public void onChangeEvent(CarPropertyValue carPropertyValue) {
            CarConditionManager.this.handleCarPropertyChange(carPropertyValue);
        }

        public void onErrorEvent(int i, int i1) {
        }
    };
    private final CarEpsManager.CarEpsEventCallback mCarEpsEventCallback = new CarEpsManager.CarEpsEventCallback() { // from class: com.xiaopeng.xuiservice.opensdk.manager.CarConditionManager.6
        public void onChangeEvent(CarPropertyValue carPropertyValue) {
            CarConditionManager.this.handleCarPropertyChange(carPropertyValue);
        }

        public void onErrorEvent(int i, int i1) {
        }
    };
    private boolean carListenerAdd = false;

    private CarConditionManager() {
    }

    public static CarConditionManager getInstance() {
        if (instance == null) {
            synchronized (CarConditionManager.class) {
                if (instance == null) {
                    instance = new CarConditionManager();
                }
            }
        }
        return instance;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void init() {
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public int ioControl(String cmd, String[] params, int pid, int uid) {
        String str = TAG;
        LogUtil.d(str, "ioControl, cmd=" + cmd + ",params=" + arrayToString(params));
        return handleIoControlWithPocket(cmd, params, null);
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public int ioControlWithPocket(String cmd, String[] params, String[] results, int pid, int uid) {
        String str = TAG;
        LogUtil.d(str, "ioControlWithPocket, cmd=" + cmd + ",params=" + arrayToString(params));
        return handleIoControlWithPocket(cmd, params, results);
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public synchronized void addListener(IPipeListener listener) {
        String str = TAG;
        LogUtil.d(str, "addListener, listener=" + listener);
        mListener = listener;
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.opensdk.manager.-$$Lambda$CarConditionManager$9kC2n9LKZgfffYapMZFFuzoSTKI
            @Override // java.lang.Runnable
            public final void run() {
                CarConditionManager.this.lambda$addListener$0$CarConditionManager();
            }
        });
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public synchronized void removeListener() {
        LogUtil.d(TAG, "removeListener");
        mListener = null;
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.opensdk.manager.-$$Lambda$CarConditionManager$_bDUimceMN-iwyiodo3ekZrGHJY
            @Override // java.lang.Runnable
            public final void run() {
                CarConditionManager.this.lambda$removeListener$1$CarConditionManager();
            }
        });
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void dump(PrintWriter pw, String[] args) {
        pw.println("dump:" + TAG);
        if (args == null) {
            pw.println("please input params!");
            return;
        }
        for (String str : args) {
            char c = 65535;
            if (str.hashCode() == 3556498 && str.equals("test")) {
                c = 0;
            }
            if (c == 0) {
                pw.println("do test");
                return;
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private int handleIoControlWithPocket(String cmd, String[] params, String[] results) {
        char c;
        switch (cmd.hashCode()) {
            case -2048175559:
                if (cmd.equals("getSeatOccupied")) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            case -1953672116:
                if (cmd.equals("getRearTrunkStatus")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case -1372913209:
                if (cmd.equals("getBrakeStatus")) {
                    c = 17;
                    break;
                }
                c = 65535;
                break;
            case -1200912730:
                if (cmd.equals("getDrivingDistance")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -772235978:
                if (cmd.equals("getDoorStatus")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case -578589758:
                if (cmd.equals("getAccPedalValue")) {
                    c = 16;
                    break;
                }
                c = 65535;
                break;
            case -272392116:
                if (cmd.equals("getSysReady")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -226621042:
                if (cmd.equals("getWindowPos")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case -200656858:
                if (cmd.equals("getIgStatus")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 393214404:
                if (cmd.equals("getBonnetStatus")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case 754110844:
                if (cmd.equals("getEnduranceMileageMode")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 1257102062:
                if (cmd.equals("getTirePressureValue")) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case 1324377646:
                if (cmd.equals("getWheelAngle")) {
                    c = 15;
                    break;
                }
                c = 65535;
                break;
            case 1357010131:
                if (cmd.equals("getBeltStatus")) {
                    c = 14;
                    break;
                }
                c = 65535;
                break;
            case 1568634928:
                if (cmd.equals("getBatterySoc")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 1961911423:
                if (cmd.equals("getGearLevel")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 1977992477:
                if (cmd.equals("getChargePortStatus")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 2054787657:
                if (cmd.equals("getCarSpeed")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                getIgStatus(results);
                break;
            case 1:
                getSysReady(results);
                break;
            case 2:
                getGearLevel(results);
                break;
            case 3:
                getCarSpeed(results);
                break;
            case 4:
                getDrivingDistance(params, results);
                break;
            case 5:
                getEnduranceMileageMode(results);
                break;
            case 6:
                getBatterySoc(results);
                break;
            case 7:
                getDoorStatus(results);
                break;
            case '\b':
                getWindowPos(results);
                break;
            case '\t':
                getRearTrunkStatus(results);
                break;
            case '\n':
                getBonnetStatus(results);
                break;
            case 11:
                getChargePortStatus(results);
                break;
            case '\f':
                getTirePressure(results);
                break;
            case '\r':
                getSeatOccupied(results);
                break;
            case 14:
                getBeltStatus(results);
                break;
            case 15:
                getWheelAngle(results);
                break;
            case 16:
                getAccPedalValue(results);
                break;
            case 17:
                getBrakePedalStatus(results);
                break;
        }
        return 0;
    }

    private void getIgStatus(String[] results) {
        Integer status = null;
        CarMcuManager manager = CarClientManager.getInstance().getCarManager("xp_mcu");
        if (manager != null) {
            try {
                status = Integer.valueOf(manager.getIgStatusFromMcu());
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getIgStatus,e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getIgStatus, get CarMcuManager null!");
        }
        if (status != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getIgStatus, result len=" + results.length);
            results[0] = String.valueOf(status);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "getIgStatus, status:" + status + ",results:" + Arrays.toString(results));
    }

    private void getSysReady(String[] results) {
        Integer status = null;
        CarVcuManager manager = CarClientManager.getInstance().getCarManager("xp_vcu");
        if (manager != null) {
            try {
                status = Integer.valueOf(manager.getEvSysReady());
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getSysReady,e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getSysReady, get CarVcuManager null!");
        }
        if (status != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getSysReady, result len=" + results.length);
            results[0] = String.valueOf(status);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "getSysReady, status:" + status + ",results:" + Arrays.toString(results));
    }

    private void getGearLevel(String[] results) {
        Integer level = null;
        CarVcuManager manager = CarClientManager.getInstance().getCarManager("xp_vcu");
        if (manager != null) {
            try {
                level = Integer.valueOf(manager.getDisplayGearLevel());
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getGearLevel,e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getGearLevel, get CarVcuManager null!");
        }
        if (level != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getGearLevel, result len=" + results.length);
            results[0] = String.valueOf(level);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "getGearLevel, level:" + level + ",results:" + Arrays.toString(results));
    }

    private void getCarSpeed(String[] results) {
        Float speed = null;
        CarVcuManager manager = CarClientManager.getInstance().getCarManager("xp_vcu");
        if (manager != null) {
            try {
                speed = Float.valueOf(manager.getRawCarSpeed());
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getCarSpeed, e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getCarSpeed, get CarVcuManager null!");
        }
        if (speed != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getCarSpeed, result len=" + results.length);
            results[0] = String.valueOf(speed);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "getCarSpeed, speed:" + speed + ",results:" + Arrays.toString(results));
    }

    private void getDrivingDistance(String[] params, String[] results) {
        Integer distance = null;
        CarVcuManager manager = CarClientManager.getInstance().getCarManager("xp_vcu");
        if (manager != null) {
            int mileageMode = 1;
            if (params != null) {
                try {
                    if (params.length > 0) {
                        mileageMode = Integer.parseInt(params[0]);
                    }
                } catch (Exception e) {
                    String str = TAG;
                    LogUtil.w(str, "getDrivingDistance, e=" + e);
                }
            }
            distance = mileageMode == 2 ? Integer.valueOf(manager.getWltpAvailableDrivingDistance()) : Integer.valueOf(manager.getAvalibleDrivingDistance());
        } else {
            LogUtil.w(TAG, "getDrivingDistance, get CarVcuManager null!");
        }
        if (distance != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getDrivingDistance, result len=" + results.length);
            results[0] = String.valueOf(distance);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "getDrivingDistance, distance:" + distance + ",results:" + Arrays.toString(results));
    }

    private void getEnduranceMileageMode(String[] results) {
        Integer mode = null;
        CarVcuManager manager = CarClientManager.getInstance().getCarManager("xp_vcu");
        if (manager != null) {
            try {
                mode = Integer.valueOf(manager.getEnduranceMileageMode());
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getMileageMode e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getMileageMode, get CarVcuManager null!");
        }
        if (mode != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getMileageMode, result len=" + results.length);
            results[0] = String.valueOf(mode);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "getMileageMode, mode:" + mode + ",results:" + Arrays.toString(results));
    }

    private void getBatterySoc(String[] results) {
        Integer percent = null;
        CarVcuManager manager = CarClientManager.getInstance().getCarManager("xp_vcu");
        if (manager != null) {
            try {
                percent = Integer.valueOf(manager.getElectricityPercent());
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getBatterySoc e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getBatterySoc, get CarVcuManager null!");
        }
        if (percent != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getBatterySoc, result len=" + results.length);
            results[0] = String.valueOf(percent);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "getBatterySoc, percent:" + percent + ",results:" + Arrays.toString(results));
    }

    private void getDoorStatus(String[] results) {
        int[] stats = null;
        CarBcmManager manager = CarClientManager.getInstance().getCarManager("xp_bcm");
        if (manager != null) {
            try {
                stats = manager.getDoorsState();
            } catch (Exception e) {
                LogUtil.w(TAG, "getDoorStatus,e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getDoorStatus,get CarBcmManager null!");
        }
        if (stats != null && results != null && results.length >= stats.length) {
            LogUtil.d(TAG, "getDoorStatus,result len=" + results.length);
            int i = 0;
            for (int val : stats) {
                if (i < results.length) {
                    results[i] = String.valueOf(val);
                    i++;
                }
            }
            return;
        }
        LogUtil.w(TAG, "getDoorStatus, stats:" + Arrays.toString(stats) + ",results:" + Arrays.toString(results));
    }

    private void getWindowPos(String[] results) {
        float[] pos = null;
        CarBcmManager manager = CarClientManager.getInstance().getCarManager("xp_bcm");
        if (manager != null) {
            try {
                pos = new float[4];
                if (XUIConfig.isProductSeriesD()) {
                    pos[0] = manager.getFldmWinPstState();
                    pos[1] = manager.getFrdmWinPstState();
                    pos[2] = manager.getRldmWinPstState();
                    pos[3] = manager.getRrdmWinPstState();
                } else {
                    pos[0] = manager.getWindowMovePosition(0);
                    pos[1] = manager.getWindowMovePosition(1);
                    pos[2] = manager.getWindowMovePosition(2);
                    pos[3] = manager.getWindowMovePosition(3);
                }
            } catch (Exception e) {
                LogUtil.w(TAG, "getWindowPos, e=" + e);
                pos = null;
            }
        } else {
            LogUtil.w(TAG, "getWindowPos, get CarBcmManager null!");
        }
        if (pos != null && results != null && results.length >= pos.length) {
            LogUtil.d(TAG, "getWindowPos, result len=" + results.length);
            int i = 0;
            for (float val : pos) {
                if (i < results.length) {
                    results[i] = String.valueOf(val);
                    i++;
                }
            }
            return;
        }
        LogUtil.w(TAG, "getWindowPos, pos:" + Arrays.toString(pos) + ",results:" + Arrays.toString(results));
    }

    private void getRearTrunkStatus(String[] results) {
        Integer status = null;
        CarBcmManager manager = CarClientManager.getInstance().getCarManager("xp_bcm");
        if (manager != null) {
            try {
                status = Integer.valueOf(manager.getTrunk());
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getTrunkStatus, e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getTrunkStatus, get CarBcmManager null!");
        }
        if (status != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getTrunkStatus, result len=" + results.length);
            results[0] = String.valueOf(status);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "getTrunkStatus status:" + status + ",results:" + Arrays.toString(results));
    }

    private void getBonnetStatus(String[] results) {
        Integer status = null;
        CarBcmManager manager = CarClientManager.getInstance().getCarManager("xp_bcm");
        if (manager != null) {
            try {
                status = Integer.valueOf(manager.isBcmBonnetOpened());
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getBonnetStatus, e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getBonnetStatus, get CarBcmManager null!");
        }
        if (status != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getBonnetStatus result len=" + results.length);
            results[0] = String.valueOf(status);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "getBonnetStatus status:" + status + ",results:" + Arrays.toString(results));
    }

    private void getChargePortStatus(String[] results) {
        Integer status = null;
        CarBcmManager manager = CarClientManager.getInstance().getCarManager("xp_bcm");
        if (manager != null) {
            try {
                status = Integer.valueOf(manager.getChargePortStatus(1));
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getChargePortStatus, e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getChargePortStatus, get CarBcmManager null!");
        }
        if (status != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getChargePortStatus result len=" + results.length);
            results[0] = String.valueOf(status);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "getChargePortStatus status:" + status + ",results:" + Arrays.toString(results));
    }

    private void getTirePressure(String[] results) {
        float[] pressure = null;
        CarTpmsManager manager = CarClientManager.getInstance().getCarManager("xp_tpms");
        if (manager != null) {
            try {
                pressure = new float[]{manager.getTirePressureValue(1), manager.getTirePressureValue(2), manager.getTirePressureValue(3), manager.getTirePressureValue(4)};
            } catch (Exception e) {
                LogUtil.w(TAG, "getTirePressure e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getTirePressure get CarBcmManager null!");
        }
        if (pressure != null && results != null && results.length > 0) {
            LogUtil.d(TAG, "getTirePressure, result len=" + results.length);
            int i = 0;
            for (float val : pressure) {
                if (i < results.length) {
                    results[i] = String.valueOf(val);
                    i++;
                }
            }
            return;
        }
        LogUtil.w(TAG, "getTirePressure, pressure:" + Arrays.toString(pressure) + ", result:" + Arrays.toString(results));
    }

    private void getSeatOccupied(String[] results) {
        int[] occupied = null;
        CarBcmManager bcmManager = CarClientManager.getInstance().getCarManager("xp_bcm");
        CarSrsManager srsManager = CarClientManager.getInstance().getCarManager("xp_srs");
        if (bcmManager != null && srsManager != null) {
            try {
                occupied = new int[]{bcmManager.getDriverOnSeat(), srsManager.getPsnOnSeat(), srsManager.getRearLeftSeatOccupancyStatus(), srsManager.getRearMiddleSeatOccupancyStatus(), srsManager.getRearRightSeatOccupancyStatus(), 0, 0};
            } catch (Exception e) {
                LogUtil.w(TAG, "getSeatOccupied, e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getSeatOccupied, get manager is null");
        }
        if (occupied != null && results != null && results.length > 0) {
            LogUtil.d(TAG, "getSeatOccupied result len=" + results.length);
            int i = 0;
            for (int val : occupied) {
                if (i < results.length) {
                    results[i] = String.valueOf(val);
                    i++;
                }
            }
            return;
        }
        LogUtil.w(TAG, "getSeatOccupied, occupied:" + Arrays.toString(occupied) + ",result:" + Arrays.toString(results));
    }

    private void getBeltStatus(String[] results) {
        int[] belt = null;
        CarSrsManager manager = CarClientManager.getInstance().getCarManager("xp_srs");
        if (manager != null) {
            try {
                belt = new int[]{manager.getDriverBeltStatus(), manager.getPsnBeltStatus(), manager.getBackLeftBeltStatus(), manager.getBackMiddleBeltStatus(), manager.getBackRightBeltStatus()};
            } catch (Exception e) {
                LogUtil.w(TAG, "getBeltStatus, e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getBeltStatus, get CarSrsManager is null");
        }
        if (belt != null && results != null && results.length > 0) {
            LogUtil.d(TAG, "getBeltStatus result len=" + results.length);
            int i = 0;
            for (int val : belt) {
                if (i < results.length) {
                    results[i] = String.valueOf(val);
                    i++;
                }
            }
            return;
        }
        LogUtil.w(TAG, "getBeltStatus, belt:" + Arrays.toString(belt) + ",results:" + Arrays.toString(results));
    }

    private void getWheelAngle(String[] results) {
        Float angle = null;
        CarEpsManager manager = CarClientManager.getInstance().getCarManager("xp_eps");
        if (manager != null) {
            try {
                angle = Float.valueOf(manager.getSteeringAngle());
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getWheelAngle, e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getWheelAngle, get CarEpsManager null");
        }
        if (angle != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getWheelAngle, results len=" + results.length);
            results[0] = String.valueOf(angle);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "getWheelAngle, angle:" + angle + ",results:" + Arrays.toString(results));
    }

    private void getAccPedalValue(String[] results) {
        Integer value = null;
        CarVcuManager manager = CarClientManager.getInstance().getCarManager("xp_vcu");
        if (manager != null) {
            try {
                value = Integer.valueOf(manager.getAccPedalStatus());
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getAccPedalValue, e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getAccPedalValue, get CarVcuManager null");
        }
        if (value != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getAccPedalValue, results len=" + results.length);
            results[0] = String.valueOf(value);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "getAccPedalValue, value:" + value + ",results:" + Arrays.toString(results));
    }

    private void getBrakePedalStatus(String[] results) {
        int status = 0;
        CarVcuManager manager = CarClientManager.getInstance().getCarManager("xp_vcu");
        if (manager != null) {
            try {
                status = manager.getBreakPedalStatus();
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getBrakePedalStatus, e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getBrakePedalStatus, get CarVcuManager null");
        }
        if (results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getBrakePedalStatus, results len=" + results.length);
            results[0] = String.valueOf(status);
            return;
        }
        LogUtil.w(TAG, "getBrakePedalStatus, results is empty");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public void handleCarPropertyChange(CarPropertyValue value) {
        LogUtil.d(TAG, "handleCarPropertyChange,v=" + value + ",mListener=" + mListener);
        if (mListener == null) {
            return;
        }
        switch (value.getPropertyId()) {
            case 557847042:
                break;
            case 557847045:
                try {
                    Integer level = (Integer) value.getValue();
                    if (level != null) {
                        String[] result = {String.valueOf(level)};
                        mListener.onPipeBusNotify(MODULE_NAME, "gearLevel", result);
                        return;
                    }
                    return;
                } catch (Exception e) {
                    LogUtil.w(TAG, "gear level onChangeEvent, e=" + e);
                    return;
                }
            case 557847056:
                try {
                    Integer status = (Integer) value.getValue();
                    if (status != null) {
                        String[] result2 = {String.valueOf(status)};
                        mListener.onPipeBusNotify(MODULE_NAME, ConditionSysReady.TYPE, result2);
                        return;
                    }
                    return;
                } catch (Exception e2) {
                    LogUtil.w(TAG, "sys ready onChangeEvent, e=" + e2);
                    return;
                }
            case 557847057:
                try {
                    Integer distance = (Integer) value.getValue();
                    if (distance != null) {
                        String[] result3 = {String.valueOf(distance)};
                        mListener.onPipeBusNotify(MODULE_NAME, ConditionDrivingDistance.TYPE, result3);
                        return;
                    }
                    return;
                } catch (Exception e3) {
                    LogUtil.w(TAG, "driving distance onChangedEvent, e=" + e3);
                    return;
                }
            case 557847063:
                try {
                    Integer status2 = (Integer) value.getValue();
                    if (status2 != null) {
                        String[] results = {String.valueOf(status2)};
                        mListener.onPipeBusNotify(MODULE_NAME, "brakeStatus", results);
                        return;
                    }
                    return;
                } catch (Exception e4) {
                    LogUtil.w(TAG, "brake status onChangeEvent e=" + e4);
                    return;
                }
            case 557847064:
                try {
                    Integer accValue = (Integer) value.getValue();
                    if (accValue != null) {
                        String[] results2 = {String.valueOf(accValue)};
                        mListener.onPipeBusNotify(MODULE_NAME, ConditionAccPedal.TYPE, results2);
                        return;
                    }
                    return;
                } catch (Exception e5) {
                    LogUtil.w(TAG, "acc pedal onChangeEvent e=" + e5);
                    return;
                }
            case 557847127:
                try {
                    Integer mode = (Integer) value.getValue();
                    if (mode != null) {
                        String[] result4 = {String.valueOf(mode)};
                        mListener.onPipeBusNotify(MODULE_NAME, "enduranceMode", result4);
                        break;
                    }
                } catch (Exception e6) {
                    LogUtil.w(TAG, "driving distance onChangedEvent, e=" + e6);
                    break;
                }
                break;
            case 557847561:
                try {
                    Integer status3 = (Integer) value.getValue();
                    if (status3 != null) {
                        String[] result5 = {String.valueOf(status3)};
                        mListener.onPipeBusNotify(MODULE_NAME, "igStat", result5);
                        return;
                    }
                    return;
                } catch (Exception e7) {
                    LogUtil.w(TAG, "ig status onChangeEvent,e=" + e7);
                    return;
                }
            case 557849607:
            case 557849679:
            case 557849800:
            case 557849801:
            case 557849802:
                try {
                    CarBcmManager carBcmManager = CarClientManager.getInstance().getCarManager("xp_bcm");
                    CarSrsManager carSrsManager = CarClientManager.getInstance().getCarManager("xp_srs");
                    if (carBcmManager != null) {
                        String[] occupied = {String.valueOf(carBcmManager.getDriverOnSeat()), String.valueOf(carSrsManager.getPsnOnSeat()), String.valueOf(carSrsManager.getRearLeftSeatOccupancyStatus()), String.valueOf(carSrsManager.getRearMiddleSeatOccupancyStatus()), String.valueOf(carSrsManager.getRearRightSeatOccupancyStatus()), String.valueOf(0), String.valueOf(0)};
                        mListener.onPipeBusNotify(MODULE_NAME, "seatOccupied", occupied);
                    } else {
                        LogUtil.w(TAG, "seat occupied onChangEvent manager is null");
                    }
                    return;
                } catch (Exception e8) {
                    LogUtil.w(TAG, "seat occupied onChangedEvent, e=" + e8);
                    return;
                }
            case 557849610:
                try {
                    Integer status4 = (Integer) value.getValue();
                    if (status4 != null) {
                        String[] result6 = {String.valueOf(status4)};
                        mListener.onPipeBusNotify(MODULE_NAME, "trunkStatus", result6);
                        return;
                    }
                    return;
                } catch (Exception e9) {
                    LogUtil.w(TAG, "trunk onChangEvent, e=" + e9);
                    return;
                }
            case 557849612:
            case 557849613:
            case 557849614:
            case 557849615:
            case 557849616:
                try {
                    CarSrsManager carSrsManager2 = CarClientManager.getInstance().getCarManager("xp_srs");
                    if (carSrsManager2 != null) {
                        String[] belt = {String.valueOf(carSrsManager2.getDriverBeltStatus()), String.valueOf(carSrsManager2.getPsnBeltStatus()), String.valueOf(carSrsManager2.getBackLeftBeltStatus()), String.valueOf(carSrsManager2.getBackMiddleBeltStatus()), String.valueOf(carSrsManager2.getBackRightBeltStatus()), String.valueOf(0), String.valueOf(0)};
                        mListener.onPipeBusNotify(MODULE_NAME, "beltWarning", belt);
                    } else {
                        LogUtil.w(TAG, "belt warning onChangEvent manager is null");
                    }
                    return;
                } catch (Exception e10) {
                    LogUtil.w(TAG, "belt warning onChangEvent, e=" + e10);
                    return;
                }
            case 557849641:
                try {
                    Integer status5 = (Integer) value.getValue();
                    if (status5 != null) {
                        String[] result7 = {String.valueOf(status5)};
                        mListener.onPipeBusNotify(MODULE_NAME, "bonnetStatus", result7);
                        return;
                    }
                    return;
                } catch (Exception e11) {
                    LogUtil.w(TAG, "bonnet onChangEvent, e=" + e11);
                    return;
                }
            case 557849642:
                try {
                    Integer status6 = (Integer) value.getValue();
                    if (status6 != null) {
                        String[] result8 = {String.valueOf(status6)};
                        mListener.onPipeBusNotify(MODULE_NAME, ConditionChargePort.TYPE, result8);
                        return;
                    }
                    return;
                } catch (Exception e12) {
                    LogUtil.w(TAG, "chargePort onChangeEvent, e=" + e12);
                    return;
                }
            case 557915161:
                try {
                    Integer[] array = (Integer[]) value.getValue();
                    if (array != null) {
                        String[] result9 = new String[array.length];
                        for (int i = 0; i < array.length; i++) {
                            result9[i] = String.valueOf(array[i]);
                        }
                        mListener.onPipeBusNotify(MODULE_NAME, "doorStat", result9);
                        return;
                    }
                    return;
                } catch (Exception e13) {
                    LogUtil.w(TAG, "door status onChangeEvent,e=" + e13);
                    return;
                }
            case 559944229:
                try {
                    Float speed = (Float) value.getValue();
                    if (speed != null) {
                        String[] result10 = {String.valueOf(speed)};
                        mListener.onPipeBusNotify(MODULE_NAME, "carSpeed", result10);
                        return;
                    }
                    return;
                } catch (Exception e14) {
                    LogUtil.w(TAG, "car speed onChangeEvent, e=" + e14);
                    return;
                }
            case 559946854:
            case 559946855:
            case 559946856:
            case 559946857:
                CarBcmManager carBcmManager2 = CarClientManager.getInstance().getCarManager("xp_bcm");
                if (carBcmManager2 != null) {
                    try {
                        float[] pos = {carBcmManager2.getFldmWinPstState(), carBcmManager2.getFrdmWinPstState(), carBcmManager2.getRldmWinPstState(), carBcmManager2.getRrdmWinPstState()};
                        String[] result11 = new String[pos.length];
                        for (int i2 = 0; i2 < pos.length; i2++) {
                            result11[i2] = String.valueOf(pos[i2]);
                        }
                        mListener.onPipeBusNotify(MODULE_NAME, "windowPos", result11);
                        return;
                    } catch (Exception e15) {
                        LogUtil.w(TAG, "window pos onChangedEvent, e=" + e15);
                        return;
                    }
                }
                LogUtil.w(TAG, "window pos onChangedEvent, get CarBcmManager null");
                return;
            case 559947266:
            case 559947267:
            case 559947268:
            case 559947269:
                try {
                    CarTpmsManager tpmsManager = CarClientManager.getInstance().getCarManager("xp_tpms");
                    if (tpmsManager != null) {
                        String[] pressure = {String.valueOf(tpmsManager.getTirePressureValue(1)), String.valueOf(tpmsManager.getTirePressureValue(2)), String.valueOf(tpmsManager.getTirePressureValue(3)), String.valueOf(tpmsManager.getTirePressureValue(4))};
                        mListener.onPipeBusNotify(MODULE_NAME, ConditionTirePressure.TYPE, pressure);
                    } else {
                        LogUtil.w(TAG, "tpms onChangeEvent manager is null");
                    }
                    return;
                } catch (Exception e16) {
                    LogUtil.w(TAG, "tpms onChangeEvent, e=" + e16);
                    return;
                }
            case 559948806:
                try {
                    Float angle = (Float) value.getValue();
                    if (angle != null) {
                        String[] result12 = {String.valueOf(angle)};
                        mListener.onPipeBusNotify(MODULE_NAME, ConditionWheelAngle.TYPE, result12);
                        return;
                    }
                    return;
                } catch (Exception e17) {
                    LogUtil.w(TAG, "wheelAngle onChangeEvent e=" + e17);
                    return;
                }
            default:
                return;
        }
        try {
            Integer percent = (Integer) value.getValue();
            if (percent != null) {
                String[] result13 = {String.valueOf(percent)};
                mListener.onPipeBusNotify(MODULE_NAME, "batterySoc", result13);
            }
        } catch (Exception e18) {
            LogUtil.w(TAG, "electricity percent onchangeEvent, e=" + e18);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: carListenersAdd */
    public void lambda$addListener$0$CarConditionManager() {
        String str = TAG;
        LogUtil.i(str, "carListenersAdd,add=" + this.carListenerAdd);
        if (!this.carListenerAdd) {
            this.carListenerAdd = true;
            CarClientManager.getInstance().addBcmManagerListener(this.mCarBcmEventCallback);
            CarClientManager.getInstance().addMcuManagerListener(this.mCarMcuEventCallback);
            CarClientManager.getInstance().addVcuManagerListener(this.mCarVcuEventCallback);
            CarClientManager.getInstance().addSrsManagerListener(this.mCarSrsEventCallback);
            CarClientManager.getInstance().addTpmsManagerListener(this.mCarTpmsEventCallback);
            CarClientManager.getInstance().addEpsManagerListener(this.mCarEpsEventCallback);
        }
        LogUtil.d(TAG, "carListenersAdd done");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: carListenersRemove */
    public void lambda$removeListener$1$CarConditionManager() {
        String str = TAG;
        LogUtil.i(str, "carListenersRemove,add=" + this.carListenerAdd);
        if (this.carListenerAdd) {
            this.carListenerAdd = false;
            CarClientManager.getInstance().removeBcmManagerListener(this.mCarBcmEventCallback);
            CarClientManager.getInstance().removeMcuManagerListener(this.mCarMcuEventCallback);
            CarClientManager.getInstance().removeVcuManagerListener(this.mCarVcuEventCallback);
            CarClientManager.getInstance().removeSrsManagerListener(this.mCarSrsEventCallback);
            CarClientManager.getInstance().removeTpmsManagerListener(this.mCarTpmsEventCallback);
            CarClientManager.getInstance().removeEpsManagerListener(this.mCarEpsEventCallback);
        }
        LogUtil.d(TAG, "carListenersRemove done");
    }
}
