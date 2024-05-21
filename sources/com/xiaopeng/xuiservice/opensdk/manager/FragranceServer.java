package com.xiaopeng.xuiservice.opensdk.manager;

import com.lzy.okgo.model.HttpHeaders;
import com.xiaopeng.speech.protocol.event.OOBEEvent;
import com.xiaopeng.xuimanager.iot.BaseDevice;
import com.xiaopeng.xuimanager.iot.IoTManager;
import com.xiaopeng.xuimanager.iot.devices.FragranceDevice;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes5.dex */
public class FragranceServer extends BaseManager {
    private static final String MODULE_NAME = "SmartFragrance";
    private static final String TAG = "OSDKFragranceS";
    private boolean mDebug;
    private String mDebugTypes;
    private BaseDevice mDevice;
    private final IoTManager mIoTManager;
    private final HashMap<Integer, Integer> mTypeEffectMap;

    /* loaded from: classes5.dex */
    private static class SingletonHolder {
        private static final FragranceServer INSTANCE = new FragranceServer();

        private SingletonHolder() {
        }
    }

    public static FragranceServer getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private void effectMapInit() {
        this.mTypeEffectMap.put(1, 10);
        this.mTypeEffectMap.put(2, 5);
        this.mTypeEffectMap.put(3, 2);
        this.mTypeEffectMap.put(4, 11);
        this.mTypeEffectMap.put(5, 6);
        this.mTypeEffectMap.put(6, 3);
        this.mTypeEffectMap.put(7, 8);
        this.mTypeEffectMap.put(8, 1);
        this.mTypeEffectMap.put(9, 4);
        this.mTypeEffectMap.put(10, 9);
        this.mTypeEffectMap.put(11, 7);
    }

    private FragranceServer() {
        this.mTypeEffectMap = new HashMap<>();
        this.mDebug = false;
        this.mDevice = null;
        this.mIoTManager = IoTManager.getInstance();
        effectMapInit();
        DumpDispatcher.registerDump("fragrance", new DumpDispatcher.DebugDumper() { // from class: com.xiaopeng.xuiservice.opensdk.manager.-$$Lambda$FragranceServer$OtDLQzFIQodmb8teja83kLXic4o
            @Override // com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
            public final void debugDump(PrintWriter printWriter, String[] strArr) {
                FragranceServer.this.lambda$new$0$FragranceServer(printWriter, strArr);
            }
        });
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
        LogUtil.i(TAG, "MODULE_NAME=SmartFragrance");
        return MODULE_NAME;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    /* renamed from: dump */
    public void lambda$new$0$FragranceServer(PrintWriter pw, String[] args) {
        pw.println("dump:OSDKFragranceS");
        if (args == null || args.length == 0) {
            pw.println("please input params!");
            return;
        }
        for (int i = 0; i < args.length; i++) {
            String str = args[i];
            char c = 65535;
            int hashCode = str.hashCode();
            if (hashCode != 1495) {
                if (hashCode != 1499) {
                    if (hashCode != 1511) {
                        if (hashCode != 45134055) {
                            if (hashCode == 1383770694 && str.equals("-debug")) {
                                c = 0;
                            }
                        } else if (str.equals("-type")) {
                            c = 2;
                        }
                    } else if (str.equals("-t")) {
                        c = 3;
                    }
                } else if (str.equals("-h")) {
                    c = 4;
                }
            } else if (str.equals("-d")) {
                c = 1;
            }
            if (c == 0 || c == 1) {
                if (i + 1 < args.length) {
                    if ("1".equals(args[i + 1]) || OOBEEvent.STRING_TRUE.equals(args[i + 1])) {
                        this.mDebug = true;
                    } else {
                        this.mDebug = false;
                    }
                    pw.println("set debug mode:" + this.mDebug);
                    return;
                }
                pw.println("lack of params,please check -h cmd");
                return;
            } else if (c == 2 || c == 3) {
                if (args.length > 1) {
                    this.mDebugTypes = args[i + 1];
                    pw.println("set types:" + this.mDebugTypes);
                    return;
                }
                pw.println("lack of params,please check -h cmd");
                return;
            } else if (c == 4) {
                pw.println("support cmd:");
                pw.println("-d/-debug [1/0 or true/false],enable/disable debug mode");
                pw.println("-t/-type [1,2,3],simulate plugin channel types,note:type should among[1,12],this only works when debug mode enable");
                return;
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private int handleIoControlWithPocket(String cmd, String[] params, String[] results) {
        char c;
        switch (cmd.hashCode()) {
            case -467400828:
                if (cmd.equals("getAvailableEffect")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -65508229:
                if (cmd.equals("openEffect")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 94756344:
                if (cmd.equals(HttpHeaders.HEAD_VALUE_CONNECTION_CLOSE)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1478018183:
                if (cmd.equals("getSwitchState")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        if (c == 0) {
            getAvailableEffect(results);
        } else if (c == 1) {
            getSwitchState(results);
        } else if (c == 2) {
            openEffect(params, results);
        } else if (c == 3) {
            close();
        }
        return 0;
    }

    private void getAvailableEffect(String[] results) {
        int[] types;
        if (results == null || results.length == 0) {
            return;
        }
        Map<String, String> map = getDeviceProperties();
        LogUtil.i(TAG, "getAvailableEffect " + map + " , effect: " + this.mTypeEffectMap);
        if (map != null && (types = getTypes(map)) != null && types.length != 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < types.length; i++) {
                int type = types[i];
                if (type >= 1 && type < 100 && this.mTypeEffectMap.get(Integer.valueOf(type)) != null) {
                    Integer value = this.mTypeEffectMap.get(Integer.valueOf(type));
                    if (i > 0) {
                        sb.append(";");
                    }
                    sb.append(value);
                }
            }
            results[0] = sb.toString();
        }
    }

    private void getSwitchState(String[] results) {
        if (results == null || results.length == 0) {
            return;
        }
        Map<String, String> map = getDeviceProperties();
        LogUtil.i(TAG, "getSwitchState " + map);
        if (map == null) {
            return;
        }
        String channel = map.get("active");
        String enable = map.get("switch_state");
        int[] types = getTypes(map);
        if (channel == null || !"1".equals(enable) || types == null || types.length == 0) {
            results[0] = "0";
            return;
        }
        int activityType = 0;
        char c = 65535;
        switch (channel.hashCode()) {
            case 48:
                if (channel.equals("0")) {
                    c = 0;
                    break;
                }
                break;
            case 49:
                if (channel.equals("1")) {
                    c = 1;
                    break;
                }
                break;
            case 50:
                if (channel.equals("2")) {
                    c = 2;
                    break;
                }
                break;
        }
        if (c == 0) {
            activityType = types[0];
        } else if (c == 1) {
            if (types.length > 1) {
                activityType = types[1];
            }
        } else if (c == 2 && types.length > 2) {
            activityType = types[2];
        }
        Integer effect = this.mTypeEffectMap.get(Integer.valueOf(activityType));
        if (effect == null) {
            results[0] = "0";
        } else {
            results[0] = String.valueOf(effect);
        }
    }

    private void openEffect(String[] params, String[] results) {
        int[] types;
        if (params == null || params.length == 0 || results == null || results.length == 0) {
            return;
        }
        Map<String, String> map = getDeviceProperties();
        LogUtil.i(TAG, "openEffect " + map + ",mDebug=" + this.mDebug);
        if (map == null || (types = getTypes(map)) == null || types.length == 0) {
            return;
        }
        int effect = Integer.parseInt(params[0]);
        int channel = -1;
        int i = 0;
        while (true) {
            if (i < types.length) {
                Integer value = this.mTypeEffectMap.get(Integer.valueOf(types[i]));
                if (value == null || value.intValue() != effect) {
                    i++;
                } else {
                    channel = i;
                    break;
                }
            } else {
                break;
            }
        }
        if (channel >= 0) {
            Map<String, String> opMap = new HashMap<>();
            opMap.put("active", String.valueOf(channel));
            if (XUIConfig.isFragranceNewProtocol()) {
                opMap.put("switch_state", "1");
            }
            setDeviceProperties(opMap);
            results[0] = String.valueOf(0);
        }
    }

    private void close() {
        LogUtil.d(TAG, HttpHeaders.HEAD_VALUE_CONNECTION_CLOSE);
        setDeviceProperties("switch_state", "0");
    }

    private BaseDevice getFragranceDevice() {
        BaseDevice baseDevice = this.mDevice;
        if (baseDevice != null) {
            return baseDevice;
        }
        List<BaseDevice> devices = this.mIoTManager.getDevice("by_dev_type", "Fragrance");
        if (devices == null || devices.size() <= 0) {
            return null;
        }
        BaseDevice device = devices.get(0);
        this.mDevice = device;
        return device;
    }

    private Map<String, String> getDeviceProperties() {
        BaseDevice device = getFragranceDevice();
        if (device == null) {
            return null;
        }
        return this.mIoTManager.getDeviceProperties(device);
    }

    private int[] getTypes(Map<String, String> map) {
        String typeString = map.get("channel_types");
        if (this.mDebug) {
            typeString = this.mDebugTypes;
        }
        return FragranceDevice.readChannelTypes(typeString);
    }

    private void setDeviceProperties(String key, String value) {
        BaseDevice device = getFragranceDevice();
        if (device == null) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        this.mIoTManager.setDeviceProperties(device, map);
    }

    private void setDeviceProperties(Map<String, String> map) {
        BaseDevice device = getFragranceDevice();
        if (device == null) {
            return;
        }
        this.mIoTManager.setDeviceProperties(device, map);
    }
}
