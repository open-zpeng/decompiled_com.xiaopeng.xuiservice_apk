package com.xiaopeng.xuiservice.opensdk;

import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Pair;
import com.car.opensdk.pipebus.IPipeBus;
import com.car.opensdk.pipebus.IPipeBusListener;
import com.car.opensdk.pipebus.ParcelableData;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.opensdk.manager.ATLManager;
import com.xiaopeng.xuiservice.opensdk.manager.ATLv2Manager;
import com.xiaopeng.xuiservice.opensdk.manager.AirConditionManager;
import com.xiaopeng.xuiservice.opensdk.manager.AmbientManagerService;
import com.xiaopeng.xuiservice.opensdk.manager.BaseManager;
import com.xiaopeng.xuiservice.opensdk.manager.CarConditionManager;
import com.xiaopeng.xuiservice.opensdk.manager.CarManagerServer;
import com.xiaopeng.xuiservice.opensdk.manager.DeviceServer;
import com.xiaopeng.xuiservice.opensdk.manager.FragranceServer;
import com.xiaopeng.xuiservice.opensdk.manager.IPipeListener;
import com.xiaopeng.xuiservice.opensdk.manager.KaraokeManagerServer;
import com.xiaopeng.xuiservice.opensdk.manager.LightLanuageService;
import com.xiaopeng.xuiservice.opensdk.manager.ResourceManagerServer;
import com.xiaopeng.xuiservice.opensdk.manager.SeatMassagerService;
import com.xiaopeng.xuiservice.opensdk.manager.VodManagerServer;
import com.xiaopeng.xuiservice.opensdk.manager.VoiceManager;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: classes5.dex */
public final class OpenCarManagerService extends IPipeBus.Stub implements IPipeListener {
    private static final String TAG;
    private Context mContext;
    private final ConcurrentHashMap<String, BusRecord> mDirectModulesMap = new ConcurrentHashMap<>();
    private final HashMap<Integer, PipeListenerRecord> mListenersMap = new HashMap<>();
    private static boolean serviceReady = false;
    private static boolean hasOld = !XUIConfig.isAmbientNewApi();
    private static final HashMap<String, BaseManager> mManagerMap = new HashMap<>();
    private static final HashMap<String, Integer> mModuleBitsMap = new HashMap<>();
    private static final HashSet<Pair<String, String>> mCmdWhitelist = new HashSet<>();

    static {
        mManagerMap.put(CarManagerServer.getInstance().getModuleName(), CarManagerServer.getInstance());
        mManagerMap.put(CarConditionManager.getInstance().getModuleName(), CarConditionManager.getInstance());
        mManagerMap.put(AirConditionManager.getInstance().getModuleName(), AirConditionManager.getInstance());
        mManagerMap.put(ResourceManagerServer.getInstance().getModuleName(), ResourceManagerServer.getInstance());
        if (hasOld) {
            mManagerMap.put(ATLManager.getInstance().getModuleName(), ATLManager.getInstance());
        } else {
            mManagerMap.put(ATLv2Manager.getInstance().getModuleName(), ATLv2Manager.getInstance());
        }
        mManagerMap.put(AmbientManagerService.getInstance().getModuleName(), AmbientManagerService.getInstance());
        mManagerMap.put(LightLanuageService.getInstance().getModuleName(), LightLanuageService.getInstance());
        mManagerMap.put(FragranceServer.getInstance().getModuleName(), FragranceServer.getInstance());
        mManagerMap.put(DeviceServer.getInstance().getModuleName(), DeviceServer.getInstance());
        mManagerMap.put(SeatMassagerService.getInstance().getModuleName(), SeatMassagerService.getInstance());
        mManagerMap.put(VoiceManager.getInstance().getModuleName(), VoiceManager.getInstance());
        mModuleBitsMap.put(CarConditionManager.getInstance().getModuleName(), 1);
        mModuleBitsMap.put(AirConditionManager.getInstance().getModuleName(), 0);
        if (hasOld) {
            mModuleBitsMap.put(ATLManager.getInstance().getModuleName(), 2);
        } else {
            mModuleBitsMap.put(ATLv2Manager.getInstance().getModuleName(), 2);
        }
        mModuleBitsMap.put(AmbientManagerService.getInstance().getModuleName(), 14);
        mModuleBitsMap.put(LightLanuageService.getInstance().getModuleName(), 9);
        mModuleBitsMap.put(FragranceServer.getInstance().getModuleName(), 11);
        mModuleBitsMap.put(DeviceServer.getInstance().getModuleName(), 5);
        mModuleBitsMap.put(KaraokeManagerServer.getModuleName(), 6);
        mModuleBitsMap.put(SeatMassagerService.getInstance().getModuleName(), 12);
        mModuleBitsMap.put(VodManagerServer.getModuleName(), 13);
        mModuleBitsMap.put(VoiceManager.getInstance().getModuleName(), 15);
        mCmdWhitelist.add(new Pair<>(CarManagerServer.getInstance().getModuleName(), "carManagerInit"));
        TAG = OpenCarManagerService.class.getSimpleName() + "##";
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.IPipeListener
    public void onPipeBusNotify(String module, String type, String[] events) {
        synchronized (this.mListenersMap) {
            for (PipeListenerRecord record : this.mListenersMap.values()) {
                if (record != null && record.moduleList.contains(module)) {
                    try {
                        record.listener.onPipeBusNotify(module, type, events);
                    } catch (Exception e) {
                        String str = TAG;
                        LogUtil.w(str, "onPipeBusNotify, e=" + e);
                    }
                }
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.IPipeListener
    public void onPipeBusNotify(String module, String type, String[] events, int pid) {
        PipeListenerRecord record;
        synchronized (this.mListenersMap) {
            record = this.mListenersMap.get(Integer.valueOf(pid));
        }
        if (record != null && record.moduleList.contains(module)) {
            try {
                record.listener.onPipeBusNotify(module, type, events);
                return;
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "onPipeBusNotify, e=" + e + ",target pid=" + pid);
                return;
            }
        }
        String str2 = TAG;
        LogUtil.w(str2, "onPipeBusNotify,target pid=" + pid + ",module=" + module + ",record=" + record);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public final class PipeListenerRecord implements IBinder.DeathRecipient {
        public IPipeBusListener listener;
        public List<String> moduleList = new ArrayList();
        public final int pid;
        public final int uid;

        PipeListenerRecord(IPipeBusListener listener, int pid, int uid) {
            this.listener = listener;
            this.pid = pid;
            this.uid = uid;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            String str = OpenCarManagerService.TAG;
            LogUtil.w(str, "binder died,pid=" + this.pid);
            synchronized (OpenCarManagerService.this.mListenersMap) {
                OpenCarManagerService.this.mListenersMap.remove(Integer.valueOf(this.pid));
            }
            if (this.moduleList.size() > 0) {
                for (String m : this.moduleList) {
                    OpenCarManagerService.this.moduleListenerRemoveTry(m);
                }
            }
            synchronized (OpenCarManagerService.mManagerMap) {
                for (BaseManager manager : OpenCarManagerService.mManagerMap.values()) {
                    manager.onClientDied(this.pid);
                }
            }
        }
    }

    /* loaded from: classes5.dex */
    private final class BusRecord implements IBinder.DeathRecipient {
        public IPipeBus bus;
        public final String module;
        public final int pid;
        public final int uid;

        BusRecord(IPipeBus bus, int pid, int uid, String module) {
            this.bus = bus;
            this.pid = pid;
            this.uid = uid;
            this.module = module;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            String str = OpenCarManagerService.TAG;
            LogUtil.w(str, "BusRecord,binder died,pid=" + this.pid + ",module=" + this.module);
            synchronized (OpenCarManagerService.this.mDirectModulesMap) {
                OpenCarManagerService.this.mDirectModulesMap.remove(this.module);
            }
        }
    }

    public OpenCarManagerService(Context context) {
        LogUtil.setModuleLogLevel(TAG, 0);
        LogUtil.d(TAG, "create OpenCarManagerService");
        this.mContext = context;
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.opensdk.-$$Lambda$OpenCarManagerService$8tN7BhSY0CWNbkRqG7jXBZ-Yts8
            @Override // java.lang.Runnable
            public final void run() {
                OpenCarManagerService.lambda$new$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$new$0() {
        LogUtil.d(TAG, "manager init begin");
        CarClientManager.getInstance().init();
        for (BaseManager manager : mManagerMap.values()) {
            if (manager.isEnabled()) {
                manager.init();
            } else {
                String str = TAG;
                LogUtil.w(str, "not enabled:" + manager.getModuleName());
            }
        }
        LogUtil.d(TAG, "manager init end");
        synchronized (OpenCarManagerService.class) {
            OpenCarManagerService.class.notifyAll();
        }
        serviceReady = true;
    }

    public static boolean isReady() {
        return serviceReady;
    }

    public int ioControl(String module, String cmd, String[] params) throws RemoteException {
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        String str = TAG;
        LogUtil.d(str, "ioControl,module=" + module + ",cmd=" + cmd + ",call pid=" + pid + ",uid=" + uid);
        if (!isInWhitelist(module, cmd)) {
            if (!CarManagerServer.getInstance().isSdkInit(pid)) {
                return -2;
            }
            if (!CarManagerServer.getInstance().isModuleAuth(pid, uid, module)) {
                return -3;
            }
        }
        BaseManager manager = mManagerMap.get(module);
        if (manager != null) {
            int ret = manager.ioControl(cmd, params, pid, uid);
            return ret;
        }
        String str2 = TAG;
        LogUtil.w(str2, "ioControl, module not found:" + module);
        return -1;
    }

    public int ioControlWithParcelable(String module, String cmd, ParcelableData dataIn, ParcelableData dataOut) throws RemoteException {
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        String str = TAG;
        LogUtil.d(str, "ioControlWithParcelable,module=" + module + ",cmd=" + cmd + ",call pid=" + pid + ",uid=" + uid);
        if (!isInWhitelist(module, cmd)) {
            if (!CarManagerServer.getInstance().isSdkInit(pid)) {
                return -2;
            }
            if (!CarManagerServer.getInstance().isModuleAuth(pid, uid, module)) {
                return -3;
            }
        }
        BaseManager manager = mManagerMap.get(module);
        if (manager != null) {
            int ret = manager.ioControlWithParcelable(cmd, dataIn, dataOut, pid, uid);
            return ret;
        }
        String str2 = TAG;
        LogUtil.w(str2, "ioControlWithParcelable, module not found:" + module);
        return -1;
    }

    public int ioControlWithBytes(String module, String cmd, String[] params, byte[] bytesIn, byte[] bytesOut) throws RemoteException {
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        String str = TAG;
        LogUtil.d(str, "ioControlWithBytes,module=" + module + ",cmd=" + cmd + ",call pid=" + pid + ",uid=" + uid);
        if (!isInWhitelist(module, cmd)) {
            if (!CarManagerServer.getInstance().isSdkInit(pid)) {
                return -2;
            }
            if (!CarManagerServer.getInstance().isModuleAuth(pid, uid, module)) {
                return -3;
            }
        }
        BaseManager manager = mManagerMap.get(module);
        if (manager != null) {
            int ret = manager.ioControlWithBytes(cmd, params, bytesIn, bytesOut, pid, uid);
            return ret;
        }
        String str2 = TAG;
        LogUtil.w(str2, "ioControlWithBytes, module not found:" + module);
        return -1;
    }

    public int ioControlWithPocket(String module, String cmd, String[] params, String[] results) throws RemoteException {
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        String str = TAG;
        LogUtil.d(str, "ioControlWithPocket,module=" + module + ",cmd=" + cmd + ",call pid=" + pid + ",uid=" + uid);
        if (!isInWhitelist(module, cmd)) {
            if (!CarManagerServer.getInstance().isSdkInit(pid)) {
                return -2;
            }
            if (!CarManagerServer.getInstance().isModuleAuth(pid, uid, module)) {
                return -3;
            }
        }
        BaseManager manager = mManagerMap.get(module);
        if (manager != null) {
            int ret = manager.ioControlWithPocket(cmd, params, results, pid, uid);
            return ret;
        }
        String str2 = TAG;
        LogUtil.w(str2, "ioControlWithPocket, module not found:" + module);
        return -1;
    }

    private boolean isInWhitelist(String module, String cmd) {
        Iterator<Pair<String, String>> it = mCmdWhitelist.iterator();
        while (it.hasNext()) {
            Pair<String, String> p = it.next();
            if (((String) p.first).equals(module) && ((String) p.second).equals(cmd)) {
                return true;
            }
        }
        return false;
    }

    public void registerListener(String moduleName, IPipeBusListener iPipeBusListener) throws RemoteException {
        PipeListenerRecord record;
        PipeListenerRecord record2;
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        boolean sdkVerified = CarManagerServer.getInstance().isSdkInit(pid);
        String str = TAG;
        LogUtil.i(str, "registerListener,module=" + moduleName + ",l=" + iPipeBusListener.asBinder() + ",pid=" + pid + ",uid=" + uid + ",sdkVerified=" + sdkVerified);
        if (!sdkVerified) {
            return;
        }
        if (!CarManagerServer.getInstance().isModuleAuth(pid, uid, moduleName)) {
            String str2 = TAG;
            LogUtil.w(str2, "module auth fail:" + moduleName);
            return;
        }
        synchronized (this.mListenersMap) {
            record = this.mListenersMap.get(Integer.valueOf(pid));
        }
        if (record != null) {
            record2 = record;
        } else {
            PipeListenerRecord record3 = new PipeListenerRecord(iPipeBusListener, pid, uid);
            try {
                iPipeBusListener.asBinder().linkToDeath(record3, 0);
                synchronized (this.mListenersMap) {
                    this.mListenersMap.put(Integer.valueOf(pid), record3);
                }
                record2 = record3;
            } catch (Exception e) {
                String str3 = TAG;
                LogUtil.e(str3, "linkToDeath e=" + e.toString());
                return;
            }
        }
        BaseManager manager = mManagerMap.get(moduleName);
        if (manager != null) {
            synchronized (manager) {
                manager.addListener(this);
            }
        } else {
            String str4 = TAG;
            LogUtil.w(str4, "registerListener, manager not found:" + moduleName);
        }
        synchronized (record2.moduleList) {
            if (!record2.moduleList.contains(moduleName)) {
                record2.moduleList.add(moduleName);
            }
        }
    }

    public void unRegisterListener(String moduleName) throws RemoteException {
        PipeListenerRecord record;
        int pid = Binder.getCallingPid();
        boolean sdkVerified = CarManagerServer.getInstance().isSdkInit(pid);
        String str = TAG;
        LogUtil.d(str, "unRegisterListener, module=" + moduleName + ",pid=" + pid + ",sdkVerified=" + sdkVerified);
        if (!sdkVerified) {
            return;
        }
        synchronized (this.mListenersMap) {
            record = this.mListenersMap.get(Integer.valueOf(pid));
        }
        if (record == null) {
            String str2 = TAG;
            LogUtil.w(str2, "no listener for:" + moduleName);
            return;
        }
        synchronized (record.moduleList) {
            record.moduleList.remove(moduleName);
        }
        if (record.moduleList.isEmpty()) {
            synchronized (this.mListenersMap) {
                this.mListenersMap.remove(Integer.valueOf(pid));
            }
            try {
                record.listener.asBinder().unlinkToDeath(record, 0);
                record.listener = null;
            } catch (Exception e) {
                String str3 = TAG;
                LogUtil.w(str3, "unlinkToDeath e=" + e.toString());
            }
        }
        moduleListenerRemoveTry(moduleName);
    }

    public void registerModuleImplementor(String moduleName, IPipeBus iPipeBus) throws RemoteException {
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        String str = TAG;
        LogUtil.i(str, "registerModuleImplementor,moduleName=" + moduleName + ",iPipeBus class=" + iPipeBus.getClass().getName());
        BusRecord record = new BusRecord(iPipeBus, pid, uid, moduleName);
        iPipeBus.asBinder().linkToDeath(record, 0);
        synchronized (this.mDirectModulesMap) {
            this.mDirectModulesMap.put(moduleName, record);
        }
    }

    public void unRegisterModuleImplementor(String moduleName, IPipeBus iPipeBus) throws RemoteException {
        String str = TAG;
        LogUtil.i(str, "unRegisterModuleImplementor,module=" + moduleName);
        BusRecord record = this.mDirectModulesMap.get(moduleName);
        if (record != null) {
            record.bus.asBinder().unlinkToDeath(record, 0);
        }
        synchronized (this.mDirectModulesMap) {
            this.mDirectModulesMap.remove(moduleName);
        }
    }

    public IPipeBus getModuleImplementor(String moduleName) throws RemoteException {
        String str = TAG;
        LogUtil.d(str, "getModuleImplementor,moduleName=" + moduleName);
        IPipeBus pipeBus = null;
        BusRecord record = this.mDirectModulesMap.get(moduleName);
        if (record != null) {
            pipeBus = record.bus;
        }
        if (pipeBus == null) {
            String str2 = TAG;
            LogUtil.d(str2, "getModuleImplementor null for:" + moduleName);
        }
        return pipeBus;
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x0063, code lost:
        r7.println("dump " + com.xiaopeng.xuiservice.opensdk.OpenCarManagerService.TAG);
        r2 = com.xiaopeng.xuiservice.opensdk.OpenCarManagerService.mManagerMap.values().iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0087, code lost:
        if (r2.hasNext() == false) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0089, code lost:
        r3 = r2.next();
        r3.dump(r7, r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0093, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:?, code lost:
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void dump(java.io.FileDescriptor r6, java.io.PrintWriter r7, java.lang.String[] r8) {
        /*
            r5 = this;
            r0 = 0
            r1 = 0
        L2:
            int r2 = r8.length
            if (r1 >= r2) goto L61
            r2 = r8[r1]
            if (r2 == 0) goto L61
            int r3 = r2.length()
            if (r3 > 0) goto L10
            goto L61
        L10:
            int r1 = r1 + 1
            java.lang.String r3 = "-a"
            boolean r3 = r3.equals(r2)
            if (r3 == 0) goto L1c
            r0 = 1
            goto L61
        L1c:
            java.lang.String r3 = "-h"
            boolean r3 = r3.equals(r2)
            if (r3 == 0) goto L28
            com.xiaopeng.xuiservice.debug.DumpDispatcher.dumpHelp(r7)
            return
        L28:
            java.lang.String r3 = "-m"
            boolean r3 = r3.equals(r2)
            if (r3 != 0) goto L53
            java.lang.String r3 = "-module"
            boolean r3 = r3.equals(r2)
            if (r3 == 0) goto L39
            goto L53
        L39:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Unknown argument: "
            r3.append(r4)
            r3.append(r2)
            java.lang.String r4 = "; use -h for help"
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            r7.println(r3)
            goto L60
        L53:
            int r3 = r8.length
            if (r1 >= r3) goto L60
            r2 = r8[r1]
            int r1 = r1 + 1
            if (r2 == 0) goto L60
            com.xiaopeng.xuiservice.debug.DumpDispatcher.dumpModule(r7, r2, r8, r1)
            return
        L60:
            goto L2
        L61:
            if (r0 == 0) goto L93
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "dump "
            r2.append(r3)
            java.lang.String r3 = com.xiaopeng.xuiservice.opensdk.OpenCarManagerService.TAG
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            r7.println(r2)
            java.util.HashMap<java.lang.String, com.xiaopeng.xuiservice.opensdk.manager.BaseManager> r2 = com.xiaopeng.xuiservice.opensdk.OpenCarManagerService.mManagerMap
            java.util.Collection r2 = r2.values()
            java.util.Iterator r2 = r2.iterator()
        L83:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L93
            java.lang.Object r3 = r2.next()
            com.xiaopeng.xuiservice.opensdk.manager.BaseManager r3 = (com.xiaopeng.xuiservice.opensdk.manager.BaseManager) r3
            r3.dump(r7, r8)
            goto L83
        L93:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xuiservice.opensdk.OpenCarManagerService.dump(java.io.FileDescriptor, java.io.PrintWriter, java.lang.String[]):void");
    }

    public static HashMap getModuleBitsMap() {
        return mModuleBitsMap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void moduleListenerRemoveTry(String name) {
        boolean pidLive = false;
        synchronized (this.mListenersMap) {
            if (this.mListenersMap.size() > 0) {
                Iterator<PipeListenerRecord> it = this.mListenersMap.values().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    PipeListenerRecord record1 = it.next();
                    if (record1.moduleList.contains(name)) {
                        pidLive = true;
                        break;
                    }
                }
            }
        }
        if (!pidLive) {
            BaseManager manager = mManagerMap.get(name);
            if (manager != null) {
                String str = TAG;
                LogUtil.i(str, "moduleListenerRemoveTry, clear:" + name);
                synchronized (manager) {
                    manager.removeListener();
                }
                return;
            }
            String str2 = TAG;
            LogUtil.w(str2, "moduleListenerRemoveTry, manager not found:" + name);
        }
    }
}
