package com.xiaopeng.xuiservice.opensdk.manager;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.bcm.CarBcmManager;
import android.car.hardware.hvac.CarHvacManager;
import android.os.SystemProperties;
import com.car.opensdk.resourcemanager.CarResourceInfo;
import com.car.opensdk.vehicle.air.IAirConditioner;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.opensdk.manager.ResourceManagerServer;
import java.io.PrintWriter;
import java.util.Arrays;
/* loaded from: classes5.dex */
public class AirConditionManager extends BaseManager implements ResourceManagerServer.ResourceListener {
    private static final String TAG = AirConditionManager.class.getSimpleName();
    private static final String MODULE_NAME = IAirConditioner.MODULE_NAME;
    private static AirConditionManager instance = null;
    private IPipeListener mPipeListener = null;
    private final CarHvacManager.CarHvacEventCallback carHvacCallback = new CarHvacManager.CarHvacEventCallback() { // from class: com.xiaopeng.xuiservice.opensdk.manager.AirConditionManager.1
        public void onChangeEvent(CarPropertyValue carPropertyValue) {
            AirConditionManager.this.handleHvacChangeEvent(carPropertyValue);
        }

        public void onErrorEvent(int i, int i1) {
        }
    };
    private final CarBcmManager.CarBcmEventCallback carBcmCallback = new CarBcmManager.CarBcmEventCallback() { // from class: com.xiaopeng.xuiservice.opensdk.manager.AirConditionManager.2
        public void onChangeEvent(CarPropertyValue carPropertyValue) {
            AirConditionManager.this.handleHvacChangeEvent(carPropertyValue);
        }

        public void onErrorEvent(int i, int i1) {
        }
    };

    private AirConditionManager() {
    }

    public static AirConditionManager getInstance() {
        if (instance == null) {
            synchronized (AirConditionManager.class) {
                if (instance == null) {
                    instance = new AirConditionManager();
                }
            }
        }
        return instance;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void init() {
        CarClientManager.getInstance().addHvacManagerListener(this.carHvacCallback);
        CarClientManager.getInstance().addBcmManagerListener(this.carBcmCallback);
        ResourceManagerServer.getInstance().addModuleListener(MODULE_NAME, this);
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public int ioControl(String cmd, String[] params, int pid, int uid) {
        String str = TAG;
        LogUtil.d(str, "ioControl, cmd:" + cmd + ",params:" + Arrays.toString(params));
        return handleIoControl(cmd, params);
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public int ioControlWithPocket(String cmd, String[] params, String[] results, int pid, int uid) {
        String str = TAG;
        LogUtil.d(str, "ioControlWithPocket, cmd:" + cmd + ",params:" + Arrays.toString(params));
        return handleIoControlWithPocket(cmd, params, results);
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void addListener(IPipeListener listener) {
        String str = TAG;
        LogUtil.d(str, "addListener, listener:" + listener);
        this.mPipeListener = listener;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void removeListener() {
        LogUtil.d(TAG, "remove listener");
        this.mPipeListener = null;
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
    private int handleIoControl(String cmd, String[] params) {
        char c;
        int i = 1;
        switch (cmd.hashCode()) {
            case -2137465016:
                if (cmd.equals("setCirculationMode")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case -2127340097:
                if (cmd.equals("setOutRearMirrorHeat")) {
                    c = 17;
                    break;
                }
                c = 65535;
                break;
            case -1971230827:
                if (cmd.equals("setPowerStatus")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -1965308767:
                if (cmd.equals("setEcoStatus")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case -1603561859:
                if (cmd.equals("setPsnTemp")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case -867124620:
                if (cmd.equals("setAutoMode")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -223149669:
                if (cmd.equals("setDrvSeatHeat")) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case -222732192:
                if (cmd.equals("setDrvSeatVent")) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            case -178358851:
                if (cmd.equals("setColdHeatNature")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 57290727:
                if (cmd.equals("setAcMode")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 610054874:
                if (cmd.equals("setDrvTemp")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 667051793:
                if (cmd.equals("setTempSync")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 1066490814:
                if (cmd.equals("setPsnSeatHeat")) {
                    c = 14;
                    break;
                }
                c = 65535;
                break;
            case 1066908291:
                if (cmd.equals("setPsnSeatVent")) {
                    c = 15;
                    break;
                }
                c = 65535;
                break;
            case 1134706202:
                if (cmd.equals("setRearTemp")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 1285611202:
                if (cmd.equals("setAirVolume")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 1342329591:
                if (cmd.equals("setBlowMode")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 1752642044:
                if (cmd.equals("setFrontDefrost")) {
                    c = 16;
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
                CarHvacManager manager = CarClientManager.getInstance().getCarManager("hvac");
                if (manager != null) {
                    if (params != null) {
                        try {
                            if (params.length > 0) {
                                boolean status = Boolean.parseBoolean(params[0]);
                                if (!status) {
                                    i = 0;
                                }
                                manager.setHvacPowerMode(i);
                                break;
                            }
                        } catch (Exception e) {
                            LogUtil.w(TAG, "set power, e=" + e);
                            break;
                        }
                    }
                } else {
                    LogUtil.w(TAG, "set power, get CarHvacManager null");
                    break;
                }
                break;
            case 1:
                CarHvacManager manager2 = CarClientManager.getInstance().getCarManager("hvac");
                if (manager2 != null) {
                    if (params != null) {
                        try {
                            if (params.length > 0) {
                                boolean status2 = Boolean.parseBoolean(params[0]);
                                if (!status2) {
                                    i = 0;
                                }
                                manager2.setHvacAutoMode(i);
                                break;
                            }
                        } catch (Exception e2) {
                            LogUtil.w(TAG, "set auto, e=" + e2);
                            break;
                        }
                    }
                } else {
                    LogUtil.w(TAG, "set auto, get CarHvacManager null");
                    break;
                }
                break;
            case 2:
                CarHvacManager manager3 = CarClientManager.getInstance().getCarManager("hvac");
                if (manager3 != null) {
                    if (params != null) {
                        try {
                            if (params.length > 0) {
                                int mode = Integer.parseInt(params[0]);
                                manager3.setAcHeatNatureButtonSt(mode);
                                break;
                            }
                        } catch (Exception e3) {
                            LogUtil.w(TAG, "set coldHeatNature, e=" + e3);
                            break;
                        }
                    }
                } else {
                    LogUtil.w(TAG, "set coldHeatNature, get CarHvacManager null");
                    break;
                }
                break;
            case 3:
                CarHvacManager manager4 = CarClientManager.getInstance().getCarManager("hvac");
                if (manager4 != null) {
                    if (params != null) {
                        try {
                            if (params.length > 0) {
                                boolean status3 = Boolean.parseBoolean(params[0]);
                                if (!status3) {
                                    i = 0;
                                }
                                manager4.setHvacTempAcMode(i);
                                break;
                            }
                        } catch (Exception e4) {
                            LogUtil.w(TAG, "set ac, e=" + e4);
                            break;
                        }
                    }
                } else {
                    LogUtil.w(TAG, "set ac, get CarHvacManager null");
                    break;
                }
                break;
            case 4:
                CarHvacManager manager5 = CarClientManager.getInstance().getCarManager("hvac");
                if (manager5 != null) {
                    if (params != null) {
                        try {
                            if (params.length > 0) {
                                int volume = Integer.parseInt(params[0]);
                                manager5.setHvacWindSpeedLevel(volume);
                                break;
                            }
                        } catch (Exception e5) {
                            LogUtil.w(TAG, "set air volume, e=" + e5);
                            break;
                        }
                    }
                } else {
                    LogUtil.w(TAG, "set air volume, get CarHvacManager null");
                    break;
                }
                break;
            case 5:
                CarHvacManager manager6 = CarClientManager.getInstance().getCarManager("hvac");
                if (manager6 != null) {
                    if (params != null) {
                        try {
                            if (params.length > 0) {
                                float temp = Float.parseFloat(params[0]);
                                manager6.setHvacTempDriverValue(temp);
                                break;
                            }
                        } catch (Exception e6) {
                            LogUtil.w(TAG, "set drv temp, e=" + e6);
                            break;
                        }
                    }
                } else {
                    LogUtil.w(TAG, "set drv temp, get CarHvacManager null");
                    break;
                }
                break;
            case 6:
                CarHvacManager manager7 = CarClientManager.getInstance().getCarManager("hvac");
                if (manager7 != null) {
                    if (params != null) {
                        try {
                            if (params.length > 0) {
                                float temp2 = Float.parseFloat(params[0]);
                                manager7.setHvacTempPsnValue(temp2);
                                break;
                            }
                        } catch (Exception e7) {
                            LogUtil.w(TAG, "set psn temp, e=" + e7);
                            break;
                        }
                    }
                } else {
                    LogUtil.w(TAG, "set psn temp, get CarHvacManager null");
                    break;
                }
                break;
            case 7:
                if (CarClientManager.getInstance().getCarManager("hvac") != null) {
                    if (params != null) {
                        try {
                            if (params.length > 0) {
                                Float.parseFloat(params[0]);
                                break;
                            }
                        } catch (Exception e8) {
                            LogUtil.w(TAG, "set rear temp, e=" + e8);
                            break;
                        }
                    }
                } else {
                    LogUtil.w(TAG, "set rear temp, get CarHvacManager null");
                    break;
                }
                break;
            case '\b':
                CarHvacManager manager8 = CarClientManager.getInstance().getCarManager("hvac");
                if (manager8 != null) {
                    if (params != null) {
                        try {
                            if (params.length > 0) {
                                boolean status4 = Boolean.parseBoolean(params[0]);
                                if (!status4) {
                                    i = 0;
                                }
                                manager8.setHvacTempLeftSyncMode(i);
                                break;
                            }
                        } catch (Exception e9) {
                            LogUtil.w(TAG, "set temp sync, e=" + e9);
                            break;
                        }
                    }
                } else {
                    LogUtil.w(TAG, "set temp sync, get CarHvacManager null");
                    break;
                }
                break;
            case '\t':
                CarHvacManager manager9 = CarClientManager.getInstance().getCarManager("hvac");
                if (manager9 != null) {
                    if (params != null) {
                        try {
                            if (params.length > 0) {
                                int mode2 = Integer.parseInt(params[0]);
                                manager9.setHvacCirculationMode(mode2);
                                break;
                            }
                        } catch (Exception e10) {
                            LogUtil.w(TAG, "set circulation mode, e=" + e10);
                            break;
                        }
                    }
                } else {
                    LogUtil.w(TAG, "set circulation mode, get CarHvacManager null");
                    break;
                }
                break;
            case '\n':
                CarHvacManager manager10 = CarClientManager.getInstance().getCarManager("hvac");
                if (manager10 != null) {
                    if (params != null) {
                        try {
                            if (params.length > 0) {
                                boolean status5 = Boolean.parseBoolean(params[0]);
                                if (!status5) {
                                    i = 0;
                                }
                                manager10.setEconMode(i);
                                break;
                            }
                        } catch (Exception e11) {
                            LogUtil.w(TAG, "set eco, e=" + e11);
                            break;
                        }
                    }
                } else {
                    LogUtil.w(TAG, "set eco, get CarHvacManager null");
                    break;
                }
                break;
            case 11:
                CarHvacManager manager11 = CarClientManager.getInstance().getCarManager("hvac");
                if (manager11 != null) {
                    if (params != null) {
                        try {
                            if (params.length > 0) {
                                int status6 = Integer.parseInt(params[0]);
                                manager11.setHvacWindBlowMode(status6);
                                break;
                            }
                        } catch (Exception e12) {
                            LogUtil.w(TAG, "set blow mode, e=" + e12);
                            break;
                        }
                    }
                } else {
                    LogUtil.w(TAG, "set blow mode, get CarHvacManager null");
                    break;
                }
                break;
            case '\f':
                CarBcmManager manager12 = CarClientManager.getInstance().getCarManager("xp_bcm");
                if (manager12 != null) {
                    if (params != null) {
                        try {
                            if (params.length > 0) {
                                int level = Integer.parseInt(params[0]);
                                manager12.setBcmSeatHeatLevel(level);
                                break;
                            }
                        } catch (Exception e13) {
                            LogUtil.w(TAG, "set drv seat heat, e=" + e13);
                            break;
                        }
                    }
                } else {
                    LogUtil.w(TAG, "set drv seat heat, get CarBcmManager null");
                    break;
                }
                break;
            case '\r':
                CarBcmManager manager13 = CarClientManager.getInstance().getCarManager("xp_bcm");
                if (manager13 != null) {
                    if (params != null) {
                        try {
                            if (params.length > 0) {
                                int level2 = Integer.parseInt(params[0]);
                                manager13.setBcmSeatBlowLevel(level2);
                                break;
                            }
                        } catch (Exception e14) {
                            LogUtil.w(TAG, "set drv seat vent, e=" + e14);
                            break;
                        }
                    }
                } else {
                    LogUtil.w(TAG, "set drv seat vent, get CarBcmManager null");
                    break;
                }
                break;
            case 14:
                CarBcmManager manager14 = CarClientManager.getInstance().getCarManager("xp_bcm");
                if (manager14 != null) {
                    if (params != null) {
                        try {
                            if (params.length > 0) {
                                int level3 = Integer.parseInt(params[0]);
                                manager14.setBcmPsnSeatHeatLevel(level3);
                                break;
                            }
                        } catch (Exception e15) {
                            LogUtil.w(TAG, "set psn seat heat, e=" + e15);
                            break;
                        }
                    }
                } else {
                    LogUtil.w(TAG, "set psn seat heat, get CarBcmManager null");
                    break;
                }
                break;
            case 15:
                CarBcmManager manager15 = CarClientManager.getInstance().getCarManager("xp_bcm");
                if (manager15 != null) {
                    if (params != null) {
                        try {
                            if (params.length > 0) {
                                int level4 = Integer.parseInt(params[0]);
                                manager15.setPassengerSeatBlowLevel(level4);
                                break;
                            }
                        } catch (Exception e16) {
                            LogUtil.w(TAG, "set psn seat vent, e=" + e16);
                            break;
                        }
                    }
                } else {
                    LogUtil.w(TAG, "set psn seat vent, get CarBcmManager null");
                    break;
                }
                break;
            case 16:
                CarHvacManager manager16 = CarClientManager.getInstance().getCarManager("hvac");
                if (manager16 != null) {
                    if (params != null) {
                        try {
                            if (params.length > 0) {
                                boolean status7 = Boolean.parseBoolean(params[0]);
                                if (!status7) {
                                    i = 0;
                                }
                                manager16.setHvacFrontDefrostMode(i);
                                break;
                            }
                        } catch (Exception e17) {
                            LogUtil.w(TAG, "set hva defrost, e=" + e17);
                            break;
                        }
                    }
                } else {
                    LogUtil.w(TAG, "set hvac defrost, get CarHvacManager null");
                    break;
                }
                break;
            case 17:
                CarBcmManager manager17 = CarClientManager.getInstance().getCarManager("xp_bcm");
                if (manager17 != null) {
                    if (params != null) {
                        try {
                            if (params.length > 0) {
                                boolean status8 = Boolean.parseBoolean(params[0]);
                                if (!status8) {
                                    i = 0;
                                }
                                manager17.setBcmBackMirrorHeatMode(i);
                                break;
                            }
                        } catch (Exception e18) {
                            LogUtil.w(TAG, "set mirror heat, e=" + e18);
                            break;
                        }
                    }
                } else {
                    LogUtil.w(TAG, "set mirror heat, get CarBcmManager null");
                    break;
                }
                break;
        }
        return 0;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private int handleIoControlWithPocket(String cmd, String[] params, String[] results) {
        char c;
        switch (cmd.hashCode()) {
            case -1707309060:
                if (cmd.equals("getAirVolumeDistrict")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -1516416693:
                if (cmd.equals("getOutRearMirrorHeat")) {
                    c = 17;
                    break;
                }
                c = 65535;
                break;
            case -1456740278:
                if (cmd.equals("getPsnSeatHeat")) {
                    c = 14;
                    break;
                }
                c = 65535;
                break;
            case -1456322801:
                if (cmd.equals("getPsnSeatVent")) {
                    c = 15;
                    break;
                }
                c = 65535;
                break;
            case -1326750362:
                if (cmd.equals("getDrvTemp")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case -778544792:
                if (cmd.equals("getAutoMode")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -537681472:
                if (cmd.equals("getInnerPm25")) {
                    c = 18;
                    break;
                }
                c = 65535;
                break;
            case -263381426:
                if (cmd.equals("getAirVolume")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -199494623:
                if (cmd.equals("getPowerStatus")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -13970584:
                if (cmd.equals("getSupportOutRearMirrorHeat")) {
                    c = 20;
                    break;
                }
                c = 65535;
                break;
            case 271907803:
                if (cmd.equals("getAcMode")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 754600201:
                if (cmd.equals("getPsnTemp")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 761793969:
                if (cmd.equals("getColdHeatNature")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 780665901:
                if (cmd.equals("getEcoStatus")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case 841889520:
                if (cmd.equals("getFrontDefrost")) {
                    c = 16;
                    break;
                }
                c = 65535;
                break;
            case 919876106:
                if (cmd.equals("getTempSyncSupport")) {
                    c = 19;
                    break;
                }
                c = 65535;
                break;
            case 1223286030:
                if (cmd.equals("getRearTemp")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 1237468628:
                if (cmd.equals("getCirculationMode")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 1430909419:
                if (cmd.equals("getBlowMode")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 1548586535:
                if (cmd.equals("getDrvSeatHeat")) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case 1549004012:
                if (cmd.equals("getDrvSeatVent")) {
                    c = '\r';
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
                getAirPowerStatus(results);
                break;
            case 1:
                getAirAutoStatus(results);
                break;
            case 2:
                getAirColdHeatNature(results);
                break;
            case 3:
                getAirAcStatus(results);
                break;
            case 4:
                getAirVolume(results);
                break;
            case 5:
                getAirVolumeDistrict(results);
                break;
            case 6:
                getAirDrvTemp(results);
                break;
            case 7:
                getAirPsnTemp(results);
                break;
            case '\b':
                getAirRearTemp(results);
                break;
            case '\t':
                getCirculationMode(results);
                break;
            case '\n':
                getEcoMode(results);
                break;
            case 11:
                getBlowMode(results);
                break;
            case '\f':
                getDrvSeatHeatLevel(results);
                break;
            case '\r':
                getDrvSeatVentLevel(results);
                break;
            case 14:
                getPsnSeatHeatLevel(results);
                break;
            case 15:
                getPsnSeatVentLevel(results);
                break;
            case 16:
                getFrontDefrost(results);
                break;
            case 17:
                getRearMirrorHeat(results);
                break;
            case 18:
                getInnerPm25(results);
                break;
            case 19:
                getTempSyncSupport(results);
                break;
            case 20:
                getSupportMirrorHeat(results);
                break;
        }
        return 0;
    }

    private void getAirPowerStatus(String[] results) {
        Integer status = null;
        CarHvacManager manager = CarClientManager.getInstance().getCarManager("hvac");
        if (manager != null) {
            try {
                status = Integer.valueOf(manager.getHvacPowerMode());
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getAirPowerStatus, e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getAirPowerStatus, get CarHvacStatus null");
        }
        if (status != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getAirPowerStatus, results len=" + results.length);
            results[0] = String.valueOf(status);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "getAirPowerStatus, status:" + status + ",results:" + Arrays.toString(results));
    }

    private void getAirAutoStatus(String[] results) {
        Integer status = null;
        CarHvacManager manager = CarClientManager.getInstance().getCarManager("hvac");
        if (manager != null) {
            try {
                status = Integer.valueOf(manager.getHvacAutoMode());
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getAirAutoStatus, e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getAirAutoStatus, getAirAutoStatus null");
        }
        if (status != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getAirAutoStatus, results len=" + results.length);
            results[0] = String.valueOf(status);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "getAirAutoStatus, status:" + status + ",results:" + Arrays.toString(results));
    }

    private void getAirColdHeatNature(String[] results) {
        Integer status = null;
        CarHvacManager manager = CarClientManager.getInstance().getCarManager("hvac");
        if (manager != null) {
            try {
                status = Integer.valueOf(manager.getAcHeatNatureSt());
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getAirColdHeatNature, e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getAirColdHeatNature, get CarHvacManager null");
        }
        if (status != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getAirColdHeatNature results len=" + results.length);
            results[0] = String.valueOf(status);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "getAirColdHeatNature, status:" + status + ",results:" + Arrays.toString(results));
    }

    private void getAirAcStatus(String[] results) {
        Integer status = null;
        CarHvacManager manager = CarClientManager.getInstance().getCarManager("hvac");
        if (manager != null) {
            try {
                status = Integer.valueOf(manager.getHvacTempAcMode());
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getAirAcStatus, e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getAirAcStatus, get CarHvacManager null");
        }
        if (status != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getAirAcStatus results len=" + results.length);
            results[0] = String.valueOf(status);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "getAirAcStatus, status:" + status + ",results:" + Arrays.toString(results));
    }

    private void getAirVolume(String[] results) {
        Integer status = null;
        CarHvacManager manager = CarClientManager.getInstance().getCarManager("hvac");
        if (manager != null) {
            try {
                status = Integer.valueOf(manager.getHvacWindSpeedLevel());
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getAirVolume, e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getAirVolume, get CarHvacManager null");
        }
        if (status != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getAirVolume, results len=" + results.length);
            results[0] = String.valueOf(status);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "getAirVolume, status:" + status + ",results:" + Arrays.toString(results));
    }

    private void getAirVolumeDistrict(String[] results) {
        if (results != null && results.length > 1) {
            try {
                results[0] = String.valueOf(1);
                results[1] = String.valueOf(XUIConfig.getProductSeries() > 0 ? 10 : 7);
                return;
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getAirVolumeDistrict, e=" + e);
                return;
            }
        }
        String str2 = TAG;
        LogUtil.w(str2, "getAirVolumeDistrict, results:" + Arrays.toString(results));
    }

    private void getAirDrvTemp(String[] results) {
        Float temp = null;
        CarHvacManager manager = CarClientManager.getInstance().getCarManager("hvac");
        if (manager != null) {
            try {
                temp = Float.valueOf(manager.getHvacTempDriverValue());
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getAirDrvTemp, e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getAirDrvTemp, get CarHvacManager null");
        }
        if (temp != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "get AirDrvTemp, results len=" + results.length);
            results[0] = String.valueOf(temp);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "get AirDrvTemp, temp:" + temp + ",results:" + Arrays.toString(results));
    }

    private void getAirPsnTemp(String[] results) {
        Float temp = null;
        CarHvacManager manager = CarClientManager.getInstance().getCarManager("hvac");
        if (manager != null) {
            try {
                temp = Float.valueOf(manager.getHvacTempPsnValue());
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getAirPsnTemp, e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getAirPsnTemp, get CarHvacManager null");
        }
        if (temp != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "get getAirPsnTemp, results len=" + results.length);
            results[0] = String.valueOf(temp);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "get getAirPsnTemp, temp:" + temp + ",results:" + Arrays.toString(results));
    }

    private void getAirRearTemp(String[] results) {
        CarHvacManager manager = CarClientManager.getInstance().getCarManager("hvac");
        if (manager == null) {
            LogUtil.w(TAG, "getAirPsnTemp, get CarHvacManager null");
        }
        if (0 != 0 && results != null && results.length > 0) {
            String str = TAG;
            LogUtil.d(str, "get getAirPsnTemp, results len=" + results.length);
            results[0] = String.valueOf((Object) null);
            return;
        }
        String str2 = TAG;
        LogUtil.w(str2, "get getAirPsnTemp, temp:" + ((Object) null) + ",results:" + Arrays.toString(results));
    }

    private void getCirculationMode(String[] results) {
        Integer mode = null;
        CarHvacManager manager = CarClientManager.getInstance().getCarManager("hvac");
        if (manager != null) {
            try {
                mode = Integer.valueOf(manager.getHvacCirculationMode());
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getCirculationMode, e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getCirculationMode, get CarHvacManager null");
        }
        if (mode != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getCirculationMode, results len=" + results.length);
            results[0] = String.valueOf(mode);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "getCirculationMode, mode:" + mode + ",results:" + Arrays.toString(results));
    }

    private void getEcoMode(String[] results) {
        Integer mode = null;
        CarHvacManager manager = CarClientManager.getInstance().getCarManager("hvac");
        if (manager != null) {
            try {
                mode = Integer.valueOf(manager.getEconModeSt());
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getEcoMode, e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getEcoMode, get CarHvacManager null");
        }
        if (mode != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getEcoMode, results len=" + results.length);
            results[0] = String.valueOf(mode);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "getEcoMode, mode:" + mode + ",results:" + Arrays.toString(results));
    }

    private void getBlowMode(String[] results) {
        Integer mode = null;
        CarHvacManager manager = CarClientManager.getInstance().getCarManager("hvac");
        if (manager != null) {
            try {
                mode = Integer.valueOf(manager.getHvacWindBlowMode());
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getBlowMode, e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getBlowMode, get CarHvacManager null");
        }
        if (mode != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getBlowMode, results len=" + results.length);
            results[0] = String.valueOf(mode);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "getBlowMode, mode:" + mode + ",results:" + Arrays.toString(results));
    }

    private void getDrvSeatHeatLevel(String[] results) {
        Integer level = null;
        CarBcmManager manager = CarClientManager.getInstance().getCarManager("xp_bcm");
        if (manager != null) {
            try {
                level = Integer.valueOf(manager.getBcmSeatHeatLevel());
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getDrvSeatHeatLevel, e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getDrvSeatHeatLevel, get CarHvacManager null");
        }
        if (level != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getDrvSeatHeatLevel, results len=" + results.length);
            results[0] = String.valueOf(level);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "getDrvSeatHeatLevel, level:" + level + ",results:" + Arrays.toString(results));
    }

    private void getDrvSeatVentLevel(String[] results) {
        Integer level = null;
        CarBcmManager manager = CarClientManager.getInstance().getCarManager("xp_bcm");
        if (manager != null) {
            try {
                level = Integer.valueOf(manager.getBcmSeatBlowLevel());
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getDrvSeatVentLevel, e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getDrvSeatVentLevel, get CarHvacManager null");
        }
        if (level != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getDrvSeatVentLevel, results len=" + results.length);
            results[0] = String.valueOf(level);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "getDrvSeatVentLevel, level:" + level + ",results:" + Arrays.toString(results));
    }

    private void getPsnSeatHeatLevel(String[] results) {
        Integer level = null;
        CarBcmManager manager = CarClientManager.getInstance().getCarManager("xp_bcm");
        if (manager != null) {
            try {
                level = Integer.valueOf(manager.getBcmPsnSeatHeatLevel());
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getPsnSeatHeatLevel, e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getPsnSeatHeatLevel, get CarHvacManager null");
        }
        if (level != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getPsnSeatHeatLevel, results len=" + results.length);
            results[0] = String.valueOf(level);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "getPsnSeatHeatLevel, level:" + level + ",results:" + Arrays.toString(results));
    }

    private void getPsnSeatVentLevel(String[] results) {
        CarBcmManager manager = CarClientManager.getInstance().getCarManager("xp_bcm");
        if (manager == null) {
            LogUtil.w(TAG, "getPsnSeatVentLevel, get CarHvacManager null");
        }
        if (0 != 0 && results != null && results.length > 0) {
            String str = TAG;
            LogUtil.d(str, "getPsnSeatVentLevel, results len=" + results.length);
            results[0] = String.valueOf((Object) null);
            return;
        }
        String str2 = TAG;
        LogUtil.w(str2, "getPsnSeatVentLevel, level:" + ((Object) null) + ",results:" + Arrays.toString(results));
    }

    private void getFrontDefrost(String[] results) {
        Integer status = null;
        CarHvacManager manager = CarClientManager.getInstance().getCarManager("hvac");
        if (manager != null) {
            try {
                status = Integer.valueOf(manager.getHVACFrontDefrostMode());
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getFrontDefrost, e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getFrontDefrost, get CarHvacManager null");
        }
        if (status != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getFrontDefrost, results len=" + results.length);
            results[0] = String.valueOf(status);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "getFrontDefrost, status:" + status + ",results:" + Arrays.toString(results));
    }

    private void getRearMirrorHeat(String[] results) {
        Integer status = null;
        CarBcmManager manager = CarClientManager.getInstance().getCarManager("xp_bcm");
        if (manager != null) {
            try {
                status = Integer.valueOf(manager.getBcmBackMirrorHeatMode());
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getRearMirrorHeat, e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getRearMirrorHeat, get CarBcmManager null");
        }
        if (status != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getRearMirrorHeat, results len=" + results.length);
            results[0] = String.valueOf(status);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "getRearMirrorHeat, status:" + status + ",results:" + Arrays.toString(results));
    }

    private void getInnerPm25(String[] results) {
        Integer value = null;
        CarHvacManager manager = CarClientManager.getInstance().getCarManager("hvac");
        if (manager != null) {
            try {
                value = Integer.valueOf(manager.getHvacQualityInnerPm25Value());
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "getInnerPm25, e=" + e);
            }
        } else {
            LogUtil.w(TAG, "getInnerPm25, get CarHvacManager null");
        }
        if (value != null && results != null && results.length > 0) {
            String str2 = TAG;
            LogUtil.d(str2, "getInnerPm25, results len=" + results.length);
            results[0] = String.valueOf(value);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "getInnerPm25, value:" + value + ",results:" + Arrays.toString(results));
    }

    private void getTempSyncSupport(String[] results) {
        int i = 1;
        if (XUIConfig.getProductSeries() != 1 && XUIConfig.getCfcCode() <= 1) {
            i = 0;
        }
        int support = i;
        if (results != null && results.length > 0) {
            LogUtil.d(TAG, "getTempSyncSupport, results len=" + results.length);
            results[0] = String.valueOf(support);
            return;
        }
        LogUtil.w(TAG, "getTempSyncSupport, results:" + Arrays.toString(results));
    }

    private void getSupportMirrorHeat(String[] results) {
        String propValue = SystemProperties.get("persist.sys.xiaopeng.MIRROR", "0");
        int i = 1;
        if (XUIConfig.getProductSeries() != 0 && XUIConfig.getCfcCode() <= 1 && !"1".equals(propValue)) {
            i = 0;
        }
        int support = i;
        if (results != null && results.length > 0) {
            LogUtil.d(TAG, "getSupportMirrorHeat, results len=" + results.length);
            results[0] = String.valueOf(support);
            return;
        }
        LogUtil.w(TAG, "getSupportMirrorHeat, results:" + Arrays.toString(results));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleHvacChangeEvent(CarPropertyValue carPropertyValue) {
        String str = TAG;
        LogUtil.d(str, "handleHvacChangeEvent,v=" + carPropertyValue + ",mListener=" + this.mPipeListener);
        if (this.mPipeListener == null) {
            return;
        }
        switch (carPropertyValue.getPropertyId()) {
            case 356517120:
                try {
                    Integer level = (Integer) carPropertyValue.getValue();
                    String[] result = {String.valueOf(level)};
                    this.mPipeListener.onPipeBusNotify(MODULE_NAME, "airVolume", result);
                    return;
                } catch (Exception e) {
                    String str2 = TAG;
                    LogUtil.w(str2, "air volume onChangeEvent, e=" + e);
                    return;
                }
            case 356517121:
                try {
                    Integer mode = (Integer) carPropertyValue.getValue();
                    String[] results = {String.valueOf(mode)};
                    this.mPipeListener.onPipeBusNotify(MODULE_NAME, "airBlowMode", results);
                    return;
                } catch (Exception e2) {
                    String str3 = TAG;
                    LogUtil.w(str3, "blowMode onChangeEvent, e=" + e2);
                    return;
                }
            case 356517128:
                try {
                    Integer mode2 = (Integer) carPropertyValue.getValue();
                    String[] results2 = {String.valueOf(mode2)};
                    this.mPipeListener.onPipeBusNotify(MODULE_NAME, "circulationMode", results2);
                    return;
                } catch (Exception e3) {
                    String str4 = TAG;
                    LogUtil.w(str4, "circulation type onChangedEvent, e=" + e3);
                    return;
                }
            case 356517139:
                try {
                    Integer level2 = (Integer) carPropertyValue.getValue();
                    String[] results3 = {String.valueOf(level2)};
                    this.mPipeListener.onPipeBusNotify(MODULE_NAME, "drvSeatVent", results3);
                    return;
                } catch (Exception e4) {
                    String str5 = TAG;
                    LogUtil.w(str5, "drv seat vent level onChangeEvent, e=" + e4);
                    return;
                }
            case 358614275:
                try {
                    Float temp = (Float) carPropertyValue.getValue();
                    String[] result2 = {String.valueOf(temp)};
                    this.mPipeListener.onPipeBusNotify(MODULE_NAME, "drvTempValue", result2);
                    return;
                } catch (Exception e5) {
                    String str6 = TAG;
                    LogUtil.w(str6, "drv temp onChangeEvent, e=" + e5);
                    return;
                }
            case 557849092:
                try {
                    Integer value = (Integer) carPropertyValue.getValue();
                    String[] results4 = {String.valueOf(value)};
                    this.mPipeListener.onPipeBusNotify(MODULE_NAME, "innerPm25", results4);
                    return;
                } catch (Exception e6) {
                    String str7 = TAG;
                    LogUtil.w(str7, "inner pm25 onChangedEvent, e=" + e6);
                    return;
                }
            case 557849101:
                try {
                    Integer mode3 = (Integer) carPropertyValue.getValue();
                    String[] result3 = {String.valueOf(mode3)};
                    this.mPipeListener.onPipeBusNotify(MODULE_NAME, "tempSyncStatus", result3);
                    return;
                } catch (Exception e7) {
                    String str8 = TAG;
                    LogUtil.w(str8, "air temp sync onChangeEvent, e=" + e7);
                    return;
                }
            case 557849115:
                try {
                    Integer mode4 = (Integer) carPropertyValue.getValue();
                    String[] results5 = {String.valueOf(mode4)};
                    this.mPipeListener.onPipeBusNotify(MODULE_NAME, "ecoStatus", results5);
                    return;
                } catch (Exception e8) {
                    String str9 = TAG;
                    LogUtil.w(str9, "eco onChangeEvent, e=" + e8);
                    return;
                }
            case 557849126:
                try {
                    Integer status = (Integer) carPropertyValue.getValue();
                    String[] results6 = {String.valueOf(status)};
                    this.mPipeListener.onPipeBusNotify(MODULE_NAME, "frontDefrost", results6);
                    return;
                } catch (Exception e9) {
                    String str10 = TAG;
                    LogUtil.w(str10, "frontDefrost onChangeEvent, e=" + e9);
                    return;
                }
            case 557849127:
                try {
                    Integer status2 = (Integer) carPropertyValue.getValue();
                    String[] result4 = {String.valueOf(status2)};
                    this.mPipeListener.onPipeBusNotify(MODULE_NAME, "acStatus", result4);
                    return;
                } catch (Exception e10) {
                    String str11 = TAG;
                    LogUtil.w(str11, "air ac onChangeEvent, e=" + e10);
                    return;
                }
            case 557849129:
                try {
                    Integer status3 = (Integer) carPropertyValue.getValue();
                    String[] result5 = {String.valueOf(status3)};
                    this.mPipeListener.onPipeBusNotify(MODULE_NAME, "autoStatus", result5);
                    return;
                } catch (Exception e11) {
                    String str12 = TAG;
                    LogUtil.w(str12, "air auto onChangeEvent, e=" + e11);
                    return;
                }
            case 557849130:
                try {
                    Integer status4 = (Integer) carPropertyValue.getValue();
                    String[] result6 = {String.valueOf(status4)};
                    this.mPipeListener.onPipeBusNotify(MODULE_NAME, "powerStatus", result6);
                    return;
                } catch (Exception e12) {
                    String str13 = TAG;
                    LogUtil.w(str13, "air power onChangeEvent, e=" + e12);
                    return;
                }
            case 557849638:
                try {
                    Integer level3 = (Integer) carPropertyValue.getValue();
                    String[] results7 = {String.valueOf(level3)};
                    this.mPipeListener.onPipeBusNotify(MODULE_NAME, "drvSeatHeat", results7);
                    return;
                } catch (Exception e13) {
                    String str14 = TAG;
                    LogUtil.w(str14, "drv seat heat level onChangeEvent, e=" + e13);
                    return;
                }
            case 557849665:
                try {
                    Integer status5 = (Integer) carPropertyValue.getValue();
                    String[] results8 = {String.valueOf(status5)};
                    this.mPipeListener.onPipeBusNotify(MODULE_NAME, "rearMirrorHeat", results8);
                    return;
                } catch (Exception e14) {
                    String str15 = TAG;
                    LogUtil.w(str15, "back mirror heat onChangeEvent, e=" + e14);
                    return;
                }
            case 557849701:
                try {
                    Integer level4 = (Integer) carPropertyValue.getValue();
                    String[] results9 = {String.valueOf(level4)};
                    this.mPipeListener.onPipeBusNotify(MODULE_NAME, "psnSeatHeat", results9);
                    return;
                } catch (Exception e15) {
                    String str16 = TAG;
                    LogUtil.w(str16, "psn seat heat level onChangeEvent, e=" + e15);
                    return;
                }
            case 559946242:
                try {
                    Float temp2 = (Float) carPropertyValue.getValue();
                    String[] results10 = {String.valueOf(temp2)};
                    this.mPipeListener.onPipeBusNotify(MODULE_NAME, "psnTempValue", results10);
                    return;
                } catch (Exception e16) {
                    String str17 = TAG;
                    LogUtil.w(str17, "psn temp onChangeEvent, e=" + e16);
                    return;
                }
            default:
                return;
        }
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.ResourceManagerServer.ResourceListener
    public int onResourceRequest(CarResourceInfo info) {
        String str = TAG;
        LogUtil.d(str, "onResourceRequest:" + info);
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.ResourceManagerServer.ResourceListener
    public int onResourceRelease(String resourceType) {
        String str = TAG;
        LogUtil.d(str, "onResourceRelease,type=" + resourceType);
        return 0;
    }
}
