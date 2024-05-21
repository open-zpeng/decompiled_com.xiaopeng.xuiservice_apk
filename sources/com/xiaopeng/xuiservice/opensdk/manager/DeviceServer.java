package com.xiaopeng.xuiservice.opensdk.manager;

import android.car.Car;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import com.car.opensdk.device.data.AutoPilotOs;
import com.car.opensdk.device.data.VehicleModel;
import com.car.opensdk.device.data.VehicleOs;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.utils.CommonUtils;
import java.io.PrintWriter;
import java.util.ArrayList;
/* loaded from: classes5.dex */
public class DeviceServer extends BaseManager {
    private static final String MODULE_NAME = "device";
    private static final String TAG = "OSDKDeviceS";
    private boolean mDebug;

    /* loaded from: classes5.dex */
    private static class SingletonHolder {
        private static final DeviceServer INSTANCE = new DeviceServer();

        private SingletonHolder() {
        }
    }

    public static DeviceServer getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private DeviceServer() {
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void init() {
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public int ioControl(String cmd, String[] params, int pid, int uid) {
        LogUtil.i(TAG, "ioControl, cmd=" + cmd + ",params=" + arrayToString(params));
        return handleIoControlWithPocket(cmd, params, null);
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public int ioControlWithPocket(String cmd, String[] params, String[] results, int pid, int uid) {
        LogUtil.i(TAG, "ioControlWithPocket, cmd=" + cmd + ",params=" + arrayToString(params) + " ,results=" + arrayToString(results));
        return handleIoControlWithPocket(cmd, params, results);
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void addListener(IPipeListener listener) {
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void removeListener() {
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public String getModuleName() {
        LogUtil.i(TAG, "MODULE_NAME=device");
        return MODULE_NAME;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void dump(PrintWriter pw, String[] args) {
        pw.println("dump:OSDKDeviceS");
        if (args == null || args.length == 0) {
            pw.println("please input params!");
            return;
        }
        for (String str : args) {
            char c = 65535;
            int hashCode = str.hashCode();
            if (hashCode != 3025710) {
                if (hashCode == 93803109 && str.equals("d-out")) {
                    c = 1;
                }
            } else if (str.equals("d-in")) {
                c = 0;
            }
            if (c == 0) {
                this.mDebug = true;
                return;
            } else if (c == 1) {
                this.mDebug = false;
                return;
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private int handleIoControlWithPocket(String cmd, String[] params, String[] results) {
        char c;
        int ret = 0;
        switch (cmd.hashCode()) {
            case -1993740422:
                if (cmd.equals("getVehicleOs")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -1281602716:
                if (cmd.equals("getMultiSndChSupport")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case -1249348379:
                if (cmd.equals("getVIN")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -792186419:
                if (cmd.equals("getDolbySupport")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case -420042285:
                if (cmd.equals("getVehicleModel")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 98245361:
                if (cmd.equals("getID")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 801595693:
                if (cmd.equals("getExpandDevices")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 1694036183:
                if (cmd.equals("getAutoPilotOs")) {
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
                getID(results);
                return 0;
            case 1:
                getVIN(results);
                return 0;
            case 2:
                getVehicleModel(results);
                return 0;
            case 3:
                getAutoPilotOs(results);
                return 0;
            case 4:
                getVehicleOs(results);
                return 0;
            case 5:
                getExpandDevices(results);
                return 0;
            case 6:
                int type = Integer.parseInt(params[0]);
                if (2 == type) {
                    LogUtil.d(TAG, "dolby vision not support");
                    return -200;
                } else if (1 == type) {
                    if (isDolbySupport()) {
                        return 0;
                    }
                    LogUtil.d(TAG, "DEVICE_GET_DOLBY_SUPPORT,ret=-200");
                    return -200;
                } else {
                    LogUtil.w(TAG, "DEVICE_GET_DOLBY_SUPPORT,unknown type:" + type);
                    return -200;
                }
            case 7:
                if (!isMultiSoundChannelSupport()) {
                    ret = -200;
                }
                LogUtil.i(TAG, "DEVICE_GET_MULTI_SOUND_CHANNEL_SUPPORT,ret=" + ret);
                return ret;
            default:
                return -1;
        }
    }

    private void getID(String[] results) {
        if (results == null || results.length == 0) {
            return;
        }
        results[0] = SystemProperties.get("persist.sys.mcu.hardwareId");
        Log.i(TAG, "getID " + results[0]);
    }

    private void getVIN(String[] results) {
        if (results == null || results.length == 0) {
            return;
        }
        results[0] = SystemProperties.get("sys.xiaopeng.vin");
        Log.i(TAG, "getVIN " + results[0]);
    }

    private void getVehicleModel(String[] results) {
        if (results == null || results.length == 0) {
            return;
        }
        String id = null;
        String type = Car.getXpCduType();
        char c = 65535;
        int hashCode = type.hashCode();
        switch (hashCode) {
            case 2064:
                if (type.equals("A1")) {
                    c = 0;
                    break;
                }
                break;
            case 2065:
                if (type.equals("A2")) {
                    c = 1;
                    break;
                }
                break;
            case 2066:
                if (type.equals("A3")) {
                    c = 2;
                    break;
                }
                break;
            default:
                switch (hashCode) {
                    case 2560:
                        if (type.equals("Q1")) {
                            c = 5;
                            break;
                        }
                        break;
                    case 2561:
                        if (type.equals(CommonUtils.CAR_HARDWARE_CORE_Q2)) {
                            c = 3;
                            break;
                        }
                        break;
                    case 2562:
                        if (type.equals("Q3")) {
                            c = 6;
                            break;
                        }
                        break;
                    default:
                        switch (hashCode) {
                            case 2564:
                                if (type.equals(CommonUtils.CAR_HARDWARE_CORE_Q5)) {
                                    c = 4;
                                    break;
                                }
                                break;
                            case 2565:
                                if (type.equals("Q6")) {
                                    c = 7;
                                    break;
                                }
                                break;
                            case 2566:
                                if (type.equals("Q7")) {
                                    c = '\b';
                                    break;
                                }
                                break;
                            case 2567:
                                if (type.equals("Q8")) {
                                    c = '\t';
                                    break;
                                }
                                break;
                            case 2568:
                                if (type.equals("Q9")) {
                                    c = '\n';
                                    break;
                                }
                                break;
                        }
                }
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                id = "G3";
                break;
            case 5:
                id = "P7";
                break;
            case 6:
                id = "P5";
                break;
            case 7:
                id = "G3i";
                break;
        }
        VehicleModel vehicleModel = new VehicleModel();
        vehicleModel.setId(id);
        vehicleModel.setName(id);
        vehicleModel.setStyleId("550P");
        vehicleModel.setStyleName("550P");
        results[0] = vehicleModel.toJson();
        Log.i(TAG, "getVehicleModel " + results[0]);
    }

    private void getVehicleOs(String[] results) {
        if (results == null || results.length == 0) {
            return;
        }
        String info = SystemProperties.get(CommonUtils.HARDWARE_SOFTWARE_VERSION_PROPERTY);
        if (TextUtils.isEmpty(info) || info.length() < 5) {
            return;
        }
        VehicleOs vehicleOs = new VehicleOs();
        String os = info.substring(0, 5);
        vehicleOs.setName(os);
        int vs = info.indexOf("_");
        int ve = info.indexOf("_", vs + 1);
        if (vs != -1 && ve != -1) {
            String ver = info.substring(vs + 1, ve);
            vehicleOs.setVersion(ver);
        }
        String ver2 = vehicleOs.toJson();
        results[0] = ver2;
        Log.i(TAG, "getVehicleOs " + results[0]);
    }

    private void getAutoPilotOs(String[] results) {
        if (results == null || results.length == 0) {
            return;
        }
        AutoPilotOs data = new AutoPilotOs();
        results[0] = data.toJson();
        Log.i(TAG, "getExpandDevices " + results[0]);
    }

    private void getExpandDevices(String[] results) {
        if (results == null || results.length == 0) {
            return;
        }
        new ArrayList();
        Log.i(TAG, "getExpandDevices " + results[0]);
    }

    private boolean isDolbySupport() {
        return XUIConfig.hasFeature(XUIConfig.PROPERTY_DOLBY);
    }

    private boolean isMultiSoundChannelSupport() {
        return XUIConfig.hasFeature(XUIConfig.PROPERTY_AMP);
    }
}
