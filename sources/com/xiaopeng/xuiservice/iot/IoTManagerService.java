package com.xiaopeng.xuiservice.iot;

import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import com.xiaopeng.xuimanager.iot.BaseDevice;
import com.xiaopeng.xuimanager.iot.utils.DeviceBuilder;
import com.xiaopeng.xuimanager.pipebus.IPipeBus;
import com.xiaopeng.xuimanager.pipebus.IPipeBusListener;
import com.xiaopeng.xuimanager.pipebus.ParcelableContainer;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.bluetooth.GattManager;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import com.xiaopeng.xuiservice.iot.manager.BaseDeviceManager;
import com.xiaopeng.xuiservice.iot.manager.BleScanManager;
import com.xiaopeng.xuiservice.iot.manager.FragranceManager;
import com.xiaopeng.xuiservice.iot.manager.FridgeManager;
import com.xiaopeng.xuiservice.iot.manager.IDeviceScanListener;
import com.xiaopeng.xuiservice.iot.manager.IEventListener;
import com.xiaopeng.xuiservice.iot.manager.mituwatch.MituWatchManager;
import com.xiaopeng.xuiservice.iot.manager.safetyseat.ChildSaftySeatManager;
import com.xiaopeng.xuiservice.iot.manager.xpengble.AirBedManager;
import com.xiaopeng.xuiservice.iot.manager.xphelper.XpAssistManager;
import com.xiaopeng.xuiservice.utils.AccountUtil;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class IoTManagerService extends IPipeBus.Stub implements IEventListener, IDeviceScanListener {
    private static final String TAG;
    private static final ArrayList<BaseDeviceManager> mDeviceManagers = new ArrayList<>();
    private static final Map<String, BaseDeviceManager> mTypeManagerMap = new HashMap();
    private Context mContext;
    private IEventListener mEventListener;
    private final Object mLock = new Object();
    private final ArrayList<PipeListenerRecord> mPipeListeners = new ArrayList<>();

    static {
        if (FragranceManager.getInstance().isEnabled()) {
            mDeviceManagers.add(FragranceManager.getInstance());
            mTypeManagerMap.put("Fragrance", FragranceManager.getInstance());
        }
        if (FridgeManager.getInstance().isEnabled()) {
            mDeviceManagers.add(FridgeManager.getInstance());
            mTypeManagerMap.put("Fridge", FridgeManager.getInstance());
        }
        if (XpAssistManager.getInstance().isEnabled()) {
            mDeviceManagers.add(XpAssistManager.getInstance());
            mTypeManagerMap.put("XpAssist", XpAssistManager.getInstance());
        }
        if (ChildSaftySeatManager.getInstance().isEnabled()) {
            mDeviceManagers.add(ChildSaftySeatManager.getInstance());
            mTypeManagerMap.put("SafetySeat-GlobalKids", ChildSaftySeatManager.getInstance());
        }
        if (MituWatchManager.getInstance().isEnabled()) {
            mDeviceManagers.add(MituWatchManager.getInstance());
            mTypeManagerMap.put("Mitu-Watch", MituWatchManager.getInstance());
        }
        if (AirBedManager.getInstance().isEnabled()) {
            mDeviceManagers.add(AirBedManager.getInstance());
            mTypeManagerMap.put("XPeng-airbed", AirBedManager.getInstance());
        }
        TAG = IoTManagerService.class.getSimpleName() + "##";
    }

    public IoTManagerService(Context context) {
        LogUtil.setModuleLogLevel(TAG, 0);
        LogUtil.d(TAG, "create IoTManagerService");
        this.mContext = context;
        this.mEventListener = this;
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.IoTManagerService.1
            @Override // java.lang.Runnable
            public void run() {
                CarClientManager.getInstance().init();
                GattManager.getInstance().init();
                AccountUtil.getInstance().init();
                Iterator it = IoTManagerService.mDeviceManagers.iterator();
                while (it.hasNext()) {
                    BaseDeviceManager manager = (BaseDeviceManager) it.next();
                    if (!manager.isEnabled()) {
                        String str = IoTManagerService.TAG;
                        LogUtil.d(str, "not enabled:" + manager);
                    } else {
                        manager.init();
                        manager.addDeviceListener(IoTManagerService.this.mEventListener);
                    }
                }
            }
        });
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public int ioControl(String module, String cmd, String[] params) {
        char c;
        int pid = Binder.getCallingPid();
        LogUtil.i(TAG, "ioControl,module=" + module + ",cmd=" + cmd + ",params=" + params + ",call pid=" + pid);
        switch (cmd.hashCode()) {
            case -696360458:
                if (cmd.equals("remove_monitor_device")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -125959111:
                if (cmd.equals("add_monitor_device")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 377353520:
                if (cmd.equals("set_properties")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1731572968:
                if (cmd.equals("send_device_cmd")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 2147482118:
                if (cmd.equals("request_device")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        if (c != 0) {
            if (c != 1) {
                if (c == 2) {
                    handleSendDeviceCommand(params);
                } else if (c == 3) {
                    ioSetDeviceProperties(params);
                } else if (c != 4) {
                }
            } else if (params != null && params.length > 1) {
                BaseDeviceManager manager = mTypeManagerMap.get(params[0]);
                if (manager != null) {
                    int recordRef = 0;
                    synchronized (this.mPipeListeners) {
                        Iterator<PipeListenerRecord> it = this.mPipeListeners.iterator();
                        while (true) {
                            if (it.hasNext()) {
                                PipeListenerRecord record = it.next();
                                if (record.pid == pid) {
                                    record.removeDevice(params[1]);
                                } else if (record.hasDevice(params[1])) {
                                    recordRef = 0 + 1;
                                }
                            }
                        }
                    }
                    if (recordRef == 0) {
                        LogUtil.i(TAG, "disable monitor@" + manager);
                        manager.monitorDevice(params[1], false);
                    } else {
                        LogUtil.d(TAG, "disable monitor,still has monitors@" + manager);
                    }
                } else {
                    LogUtil.w(TAG, "IO_REMOVE_MONITOR_DEVICE,get device manager fail,id=" + params[1]);
                }
            }
        } else if (params != null && params.length > 1) {
            BaseDeviceManager manager2 = mTypeManagerMap.get(params[0]);
            if (manager2 != null) {
                synchronized (this.mPipeListeners) {
                    Iterator<PipeListenerRecord> it2 = this.mPipeListeners.iterator();
                    while (true) {
                        if (it2.hasNext()) {
                            PipeListenerRecord record2 = it2.next();
                            if (record2.pid == pid) {
                                record2.addDevice(params[1]);
                            }
                        }
                    }
                }
                manager2.monitorDevice(params[1], true);
            } else {
                LogUtil.w(TAG, "IO_ADD_MONITOR_DEVICE,get device manager fail,id=" + params[1]);
            }
        }
        return 0;
    }

    public int ioControlWithPocket(String module, String cmd, String[] params, String[] results) {
        char c;
        LogUtil.d(TAG, "ioControlWithPocket,module=" + module + ",cmd=" + cmd + ",params=" + params);
        int hashCode = cmd.hashCode();
        if (hashCode != -2145877572) {
            if (hashCode == 745520991 && cmd.equals("get_device")) {
                c = 0;
            }
            c = 65535;
        } else {
            if (cmd.equals("get_properties")) {
                c = 1;
            }
            c = 65535;
        }
        if (c == 0) {
            ioPocketGetDevice(params, results);
        } else if (c == 1) {
            ioPocketGetDeviceProperties(params, results);
        }
        return 0;
    }

    public int ioControlWithParcelable(String moduleName, String cmd, ParcelableContainer dataIn, ParcelableContainer dataOut) {
        String str = TAG;
        LogUtil.i(str, "ioControlWithParcelable not implemented yet,mod:" + moduleName + ",cmd:" + cmd);
        return -1;
    }

    public void registerListenerEx(String moduleName, IPipeBusListener listener) {
        String str = TAG;
        LogUtil.i(str, "not implement registerListenerEx,mod:" + moduleName);
    }

    public void unRegisterListenerEx(String moduleName) {
        String str = TAG;
        LogUtil.i(str, "not implement unRegisterListenerEx,mod:" + moduleName);
    }

    public void registerListener(IPipeBusListener listener) throws RemoteException {
        String str = TAG;
        LogUtil.d(str, "registerListener=" + listener);
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        synchronized (this.mLock) {
            int index = findIndexOfPipeListenerLocked(listener);
            if (index != -1) {
                LogUtil.w(TAG, "PipeListener is already added, ignoring");
                return;
            }
            PipeListenerRecord record = new PipeListenerRecord(listener, pid, uid);
            try {
                listener.asBinder().linkToDeath(record, 0);
                synchronized (this.mPipeListeners) {
                    this.mPipeListeners.add(record);
                }
            } catch (RemoteException e) {
                LogUtil.e(TAG, "PipeListener is dead, ignoring it");
            }
        }
    }

    public void unRegisterListener(IPipeBusListener listener) throws RemoteException {
        PipeListenerRecord record;
        String str = TAG;
        LogUtil.d(str, "unRegisterListener=" + listener);
        synchronized (this.mLock) {
            int index = findIndexOfPipeListenerLocked(listener);
            if (index != -1) {
                synchronized (this.mPipeListeners) {
                    record = this.mPipeListeners.remove(index);
                }
                if (record != null) {
                    try {
                        record.listener.asBinder().unlinkToDeath(record, 0);
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.IEventListener
    public void onDeviceEvent(String deviceId, String jsonString) {
        synchronized (this.mPipeListeners) {
            Iterator<PipeListenerRecord> it = this.mPipeListeners.iterator();
            while (it.hasNext()) {
                PipeListenerRecord record = it.next();
                record.onDeviceEvent(deviceId, jsonString);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.IEventListener
    public void onDeviceEvent(String deviceId, String key, String value) {
        synchronized (this.mPipeListeners) {
            Iterator<PipeListenerRecord> it = this.mPipeListeners.iterator();
            while (it.hasNext()) {
                PipeListenerRecord record = it.next();
                record.onDeviceEvent(deviceId, key, value);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.IEventListener
    public void onDeviceAdd(List<BaseDevice> list) {
        String listStr = DeviceBuilder.toJsonArray(list).toString();
        synchronized (this.mPipeListeners) {
            Iterator<PipeListenerRecord> it = this.mPipeListeners.iterator();
            while (it.hasNext()) {
                PipeListenerRecord record = it.next();
                notifyDeviceAdd(record, listStr);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.iot.manager.IDeviceScanListener
    public void onScan(List<BaseDevice> deviceList, int pid) {
        PipeListenerRecord validRecord = null;
        synchronized (this.mPipeListeners) {
            Iterator<PipeListenerRecord> it = this.mPipeListeners.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                PipeListenerRecord record = it.next();
                if (record.pid == pid) {
                    validRecord = record;
                    break;
                }
            }
        }
        notifyDeviceAdd(validRecord, DeviceBuilder.toJsonArray(deviceList).toString());
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        boolean opti;
        char c;
        boolean dumpAll = false;
        if (args == null) {
            opti = false;
        } else {
            for (int opti2 = 0; opti2 < args.length; opti2++) {
                String str = args[opti2];
                switch (str.hashCode()) {
                    case 1492:
                        if (str.equals("-a")) {
                            c = 3;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1504:
                        if (str.equals("-m")) {
                            c = 0;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1437268:
                        if (str.equals("-all")) {
                            c = 4;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1438222:
                        if (str.equals("-ble")) {
                            c = 5;
                            break;
                        }
                        c = 65535;
                        break;
                    case 214175961:
                        if (str.equals("-module")) {
                            c = 1;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1397711826:
                        if (str.equals("-share")) {
                            c = 2;
                            break;
                        }
                        c = 65535;
                        break;
                    default:
                        c = 65535;
                        break;
                }
                if (c == 0 || c == 1) {
                    if (opti2 < args.length - 1) {
                        int opti3 = opti2 + 1;
                        String module = args[opti3];
                        DumpDispatcher.dumpModule(pw, module, args, opti3 + 1);
                        return;
                    }
                    pw.println("lack module name...");
                    return;
                } else if (c == 2) {
                    pw.println("please use: -m share xxx");
                    return;
                } else {
                    if (c == 3 || c == 4) {
                        dumpAll = true;
                    } else if (c == 5) {
                        int opti4 = opti2 + 1;
                        if (opti4 < args.length) {
                            String[] argsToOp = new String[args.length - opti4];
                            for (int i = opti4; i < args.length; i++) {
                                argsToOp[i - opti4] = args[i];
                            }
                            GattManager.getInstance().dump(fd, pw, argsToOp);
                            return;
                        }
                        GattManager.getInstance().dump(fd, pw, new String[0]);
                        return;
                    }
                }
            }
            opti = dumpAll;
        }
        pw.println("dump " + getClass().getName());
        pw.println("thread-" + Process.myPid());
        synchronized (this.mPipeListeners) {
            pw.println(this.mPipeListeners.size() + " pipe listeners");
            pw.println("subscribe devices:");
            Iterator<PipeListenerRecord> it = this.mPipeListeners.iterator();
            while (it.hasNext()) {
                PipeListenerRecord record = it.next();
                if (!record.deviceIdList.isEmpty()) {
                    pw.println("  listen pid:" + record.pid);
                    synchronized (record.deviceIdList) {
                        for (String id : record.deviceIdList) {
                            pw.println("    device:" + id);
                        }
                    }
                }
            }
        }
        if (opti) {
            Iterator<BaseDeviceManager> it2 = mDeviceManagers.iterator();
            while (it2.hasNext()) {
                BaseDeviceManager manager = it2.next();
                pw.println("*******************");
                manager.dump(pw, args);
            }
        }
    }

    private void handleGlobalCommand(String cmd, String args) {
        char c;
        LogUtil.i(TAG, "handleGlobalCommand,cmd=" + cmd + ",args=" + args);
        int hashCode = cmd.hashCode();
        if (hashCode != -2116831762) {
            if (hashCode == -1197288458 && cmd.equals("cmd_scan_device_start")) {
                c = 0;
            }
            c = 65535;
        } else {
            if (cmd.equals("cmd_scan_device_stop")) {
                c = 1;
            }
            c = 65535;
        }
        if (c == 0) {
            handleCommandScanDevice(args, true, Binder.getCallingPid());
        } else if (c == 1) {
            handleCommandScanDevice(args, false, Binder.getCallingPid());
        }
    }

    private void handleCommandScanDevice(String scanType, boolean enabled, int pid) {
        if (((scanType.hashCode() == 1574925912 && scanType.equals("scan_type_ble")) ? (char) 0 : (char) 65535) == 0) {
            if (enabled) {
                BleScanManager.getInstance().registerDeviceScanListener(this);
            }
            BleScanManager.getInstance().scanDevice(enabled, pid);
        }
    }

    private void handleScannedDeviceCommand(String jsonDevice, String cmd, String param) {
        try {
            BaseDevice device = DeviceBuilder.fromJson(new JSONObject(jsonDevice));
            char c = 65535;
            if (cmd.hashCode() == -1993242663 && cmd.equals("cmd_add_device")) {
                c = 0;
            }
            if (c == 0) {
                BaseDeviceManager manager = mTypeManagerMap.get(param);
                if (manager != null) {
                    manager.addDevice(device);
                    return;
                }
                String str = TAG;
                LogUtil.w(str, "add device,no manager for " + param);
            }
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.e(str2, "handleScannedDeviceCommand from json e=" + e.toString());
        }
    }

    private void handleSendDeviceCommand(String[] params) {
        if (params != null && params.length > 3) {
            if (params[0] == null) {
                String globalCmd = params[2];
                String args = params[3];
                handleGlobalCommand(globalCmd, args);
                return;
            }
            String deviceType = params[0];
            char c = 65535;
            if (deviceType.hashCode() == 458627923 && deviceType.equals("ScanDevice")) {
                c = 0;
            }
            if (c != 0) {
                BaseDeviceManager manager = mTypeManagerMap.get(params[0]);
                if (manager != null) {
                    manager.sendCommand(params[1], params[2], params[3]);
                    return;
                }
                LogUtil.w(TAG, "IO_SEND_DEVICE_CMD,get device manager fail,type=" + deviceType + ",id=" + params[1]);
                return;
            }
            handleScannedDeviceCommand(params[1], params[2], params[3]);
        }
    }

    private int findIndexOfPipeListenerLocked(IPipeBusListener listener) {
        synchronized (this.mPipeListeners) {
            for (int i = this.mPipeListeners.size() - 1; i >= 0; i--) {
                if (this.mPipeListeners.get(i).listener.asBinder() == listener.asBinder()) {
                    return i;
                }
            }
            return -1;
        }
    }

    private void ioPocketGetDevice(String[] param, String[] results) {
        LogUtil.d(TAG, "getDevice method-" + param[0]);
        if (results != null) {
            LogUtil.d(TAG, "results size=" + results.length + ",0=" + results[0]);
        }
        String str = param[0];
        char c = 65535;
        if (str.hashCode() == -1119870964 && str.equals("by_dev_type")) {
            c = 0;
        }
        if (c == 0) {
            List<BaseDevice> deviceList = null;
            BaseDeviceManager manager = mTypeManagerMap.get(param[1]);
            if (manager != null) {
                deviceList = manager.getDevice();
            }
            if (deviceList != null) {
                JSONArray array = DeviceBuilder.toJsonArray(deviceList);
                LogUtil.d(TAG, "get device str=" + array);
                if (array != null && results != null) {
                    results[0] = array.toString();
                }
            }
        }
    }

    private void ioSetDeviceProperties(String[] param) {
        String str = TAG;
        LogUtil.d(str, "setDeviceProperties,type:" + param[0] + ",devId-" + param[1]);
        String deviceType = param[0];
        String deviceId = param[1];
        Map<String, String> propMap = DeviceBuilder.jsonStrToPropMap(param[2]);
        BaseDeviceManager manager = mTypeManagerMap.get(deviceType);
        if (manager != null) {
            manager.setPropertyMap(deviceId, propMap);
        } else {
            LogUtil.w(TAG, "get manager null");
        }
    }

    private void ioPocketGetDeviceProperties(String[] param, String[] results) {
        String str = TAG;
        LogUtil.d(str, "GetDeviceProperties by-" + param[0] + ",param-" + param[1]);
        String getMethod = param[0];
        if (((getMethod.hashCode() == -1119870964 && getMethod.equals("by_dev_type")) ? (char) 0 : (char) 65535) == 0) {
            if (param.length > 1) {
                String deviceType = param[1];
                String deviceId = param[2];
                Map<String, String> propMap = null;
                BaseDeviceManager manager = mTypeManagerMap.get(deviceType);
                if (manager != null) {
                    propMap = manager.getPropertyMap(deviceId);
                } else {
                    String str2 = TAG;
                    LogUtil.w(str2, "get null of " + getMethod);
                }
                if (propMap != null) {
                    JSONObject obj = DeviceBuilder.propToJson(propMap);
                    if (obj != null) {
                        if (results != null) {
                            results[0] = obj.toString();
                            String str3 = TAG;
                            LogUtil.d(str3, "result=" + results[0]);
                            return;
                        }
                        return;
                    }
                    String str4 = TAG;
                    LogUtil.d(str4, "prop to json error,propMap=" + propMap);
                    return;
                }
                String str5 = TAG;
                LogUtil.w(str5, "propMap null,getMethod=" + getMethod);
                return;
            }
            return;
        }
        String str6 = TAG;
        LogUtil.w(str6, "ioPocketGetDeviceProperties,unknown get method:" + getMethod);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public final class PipeListenerRecord implements IBinder.DeathRecipient {
        public List<String> deviceIdList = new ArrayList();
        public final IPipeBusListener listener;
        public final int pid;
        public final int uid;

        PipeListenerRecord(IPipeBusListener listener, int pid, int uid) {
            this.listener = listener;
            this.pid = pid;
            this.uid = uid;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (IoTManagerService.this.mPipeListeners) {
                IoTManagerService.this.mPipeListeners.remove(this);
            }
            synchronized (this.deviceIdList) {
                String str = IoTManagerService.TAG;
                LogUtil.w(str, "binder died,pid=" + this.pid);
                this.deviceIdList.clear();
            }
        }

        public void addDevice(String deviceId) {
            synchronized (this.deviceIdList) {
                this.deviceIdList.add(deviceId);
            }
        }

        public void removeDevice(String deviceId) {
            synchronized (this.deviceIdList) {
                this.deviceIdList.remove(deviceId);
            }
        }

        public boolean hasDevice(String deviceId) {
            boolean exist = false;
            synchronized (this.deviceIdList) {
                if (this.deviceIdList.contains(deviceId)) {
                    exist = true;
                }
            }
            return exist;
        }

        /* JADX WARN: Code restructure failed: missing block: B:10:0x0027, code lost:
            r7.listener.onPipeBusEvent("module_iot", "property_update", r1);
         */
        /* JADX WARN: Code restructure failed: missing block: B:12:0x0031, code lost:
            r3 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:13:0x0032, code lost:
            r4 = com.xiaopeng.xuiservice.iot.IoTManagerService.TAG;
            com.xiaopeng.xuimanager.utils.LogUtil.e(r4, "onPipeBusEvent Fail,e=" + r3.toString());
         */
        /* JADX WARN: Code restructure failed: missing block: B:9:0x001b, code lost:
            r1 = new java.lang.String[]{r8, r9, r10};
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onDeviceEvent(java.lang.String r8, java.lang.String r9, java.lang.String r10) {
            /*
                r7 = this;
                java.util.List<java.lang.String> r0 = r7.deviceIdList
                monitor-enter(r0)
                java.util.List<java.lang.String> r1 = r7.deviceIdList     // Catch: java.lang.Throwable -> L52
                java.util.Iterator r1 = r1.iterator()     // Catch: java.lang.Throwable -> L52
            L9:
                boolean r2 = r1.hasNext()     // Catch: java.lang.Throwable -> L52
                if (r2 == 0) goto L50
                java.lang.Object r2 = r1.next()     // Catch: java.lang.Throwable -> L52
                java.lang.String r2 = (java.lang.String) r2     // Catch: java.lang.Throwable -> L52
                boolean r3 = r2.equals(r8)     // Catch: java.lang.Throwable -> L52
                if (r3 == 0) goto L4f
                r1 = 3
                java.lang.String[] r1 = new java.lang.String[r1]     // Catch: java.lang.Throwable -> L52
                r3 = 0
                r1[r3] = r8     // Catch: java.lang.Throwable -> L52
                r3 = 1
                r1[r3] = r9     // Catch: java.lang.Throwable -> L52
                r3 = 2
                r1[r3] = r10     // Catch: java.lang.Throwable -> L52
                com.xiaopeng.xuimanager.pipebus.IPipeBusListener r3 = r7.listener     // Catch: java.lang.Exception -> L31 java.lang.Throwable -> L52
                java.lang.String r4 = "module_iot"
                java.lang.String r5 = "property_update"
                r3.onPipeBusEvent(r4, r5, r1)     // Catch: java.lang.Exception -> L31 java.lang.Throwable -> L52
                goto L50
            L31:
                r3 = move-exception
                java.lang.String r4 = com.xiaopeng.xuiservice.iot.IoTManagerService.access$200()     // Catch: java.lang.Throwable -> L52
                java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L52
                r5.<init>()     // Catch: java.lang.Throwable -> L52
                java.lang.String r6 = "onPipeBusEvent Fail,e="
                r5.append(r6)     // Catch: java.lang.Throwable -> L52
                java.lang.String r6 = r3.toString()     // Catch: java.lang.Throwable -> L52
                r5.append(r6)     // Catch: java.lang.Throwable -> L52
                java.lang.String r5 = r5.toString()     // Catch: java.lang.Throwable -> L52
                com.xiaopeng.xuimanager.utils.LogUtil.e(r4, r5)     // Catch: java.lang.Throwable -> L52
                goto L50
            L4f:
                goto L9
            L50:
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L52
                return
            L52:
                r1 = move-exception
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L52
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xuiservice.iot.IoTManagerService.PipeListenerRecord.onDeviceEvent(java.lang.String, java.lang.String, java.lang.String):void");
        }

        /* JADX WARN: Code restructure failed: missing block: B:10:0x0024, code lost:
            r7.listener.onPipeBusEvent("module_iot", "property_update_with_json", r1);
         */
        /* JADX WARN: Code restructure failed: missing block: B:12:0x002e, code lost:
            r3 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:13:0x002f, code lost:
            r4 = com.xiaopeng.xuiservice.iot.IoTManagerService.TAG;
            com.xiaopeng.xuimanager.utils.LogUtil.e(r4, "onPipeBusEvent Fail,e=" + r3.toString());
         */
        /* JADX WARN: Code restructure failed: missing block: B:9:0x001b, code lost:
            r1 = new java.lang.String[]{r8, r9};
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onDeviceEvent(java.lang.String r8, java.lang.String r9) {
            /*
                r7 = this;
                java.util.List<java.lang.String> r0 = r7.deviceIdList
                monitor-enter(r0)
                java.util.List<java.lang.String> r1 = r7.deviceIdList     // Catch: java.lang.Throwable -> L4f
                java.util.Iterator r1 = r1.iterator()     // Catch: java.lang.Throwable -> L4f
            L9:
                boolean r2 = r1.hasNext()     // Catch: java.lang.Throwable -> L4f
                if (r2 == 0) goto L4d
                java.lang.Object r2 = r1.next()     // Catch: java.lang.Throwable -> L4f
                java.lang.String r2 = (java.lang.String) r2     // Catch: java.lang.Throwable -> L4f
                boolean r3 = r2.equals(r8)     // Catch: java.lang.Throwable -> L4f
                if (r3 == 0) goto L4c
                r1 = 2
                java.lang.String[] r1 = new java.lang.String[r1]     // Catch: java.lang.Throwable -> L4f
                r3 = 0
                r1[r3] = r8     // Catch: java.lang.Throwable -> L4f
                r3 = 1
                r1[r3] = r9     // Catch: java.lang.Throwable -> L4f
                com.xiaopeng.xuimanager.pipebus.IPipeBusListener r3 = r7.listener     // Catch: java.lang.Exception -> L2e java.lang.Throwable -> L4f
                java.lang.String r4 = "module_iot"
                java.lang.String r5 = "property_update_with_json"
                r3.onPipeBusEvent(r4, r5, r1)     // Catch: java.lang.Exception -> L2e java.lang.Throwable -> L4f
                goto L4d
            L2e:
                r3 = move-exception
                java.lang.String r4 = com.xiaopeng.xuiservice.iot.IoTManagerService.access$200()     // Catch: java.lang.Throwable -> L4f
                java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L4f
                r5.<init>()     // Catch: java.lang.Throwable -> L4f
                java.lang.String r6 = "onPipeBusEvent Fail,e="
                r5.append(r6)     // Catch: java.lang.Throwable -> L4f
                java.lang.String r6 = r3.toString()     // Catch: java.lang.Throwable -> L4f
                r5.append(r6)     // Catch: java.lang.Throwable -> L4f
                java.lang.String r5 = r5.toString()     // Catch: java.lang.Throwable -> L4f
                com.xiaopeng.xuimanager.utils.LogUtil.e(r4, r5)     // Catch: java.lang.Throwable -> L4f
                goto L4d
            L4c:
                goto L9
            L4d:
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L4f
                return
            L4f:
                r1 = move-exception
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L4f
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xuiservice.iot.IoTManagerService.PipeListenerRecord.onDeviceEvent(java.lang.String, java.lang.String):void");
        }
    }

    private void notifyDeviceAdd(PipeListenerRecord record, String listString) {
        if (record != null) {
            String[] event = {listString};
            String str = TAG;
            LogUtil.i(str, "notifyDeviceAdd to " + record.pid);
            try {
                record.listener.onPipeBusEvent("module_iot", "device_add", event);
            } catch (Exception e) {
                String str2 = TAG;
                LogUtil.w(str2, "notifyDeviceAdd e=" + e.toString());
            }
        }
    }
}
