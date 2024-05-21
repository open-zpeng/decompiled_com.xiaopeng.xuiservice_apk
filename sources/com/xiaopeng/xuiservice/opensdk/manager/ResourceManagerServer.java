package com.xiaopeng.xuiservice.opensdk.manager;

import com.car.opensdk.resourcemanager.CarResourceInfo;
import com.car.opensdk.resourcemanager.CarResourceManager;
import com.car.opensdk.vehicle.air.IAirConditioner;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes5.dex */
public class ResourceManagerServer extends BaseManager {
    private LinkedHashMap<String, CarResourceInfo> mActiveResourceMap;
    private HashMap<String, ResourceListener> mModuleListenerMap;
    private IPipeListener mPipeListener;
    private static final String TAG = ResourceManagerServer.class.getSimpleName();
    private static final String MODULE_NAME = CarResourceManager.moduleName;

    /* loaded from: classes5.dex */
    public interface ResourceListener {
        int onResourceRelease(String str);

        int onResourceRequest(CarResourceInfo carResourceInfo);
    }

    /* loaded from: classes5.dex */
    private static class ResourceManagerServerHolder {
        private static ResourceManagerServer instance = new ResourceManagerServer();

        private ResourceManagerServerHolder() {
        }
    }

    private ResourceManagerServer() {
        this.mPipeListener = null;
        this.mModuleListenerMap = new HashMap<>();
        this.mActiveResourceMap = new LinkedHashMap<>();
    }

    public static ResourceManagerServer getInstance() {
        return ResourceManagerServerHolder.instance;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void init() {
        LogUtil.d(TAG, "init");
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public int ioControl(String cmd, String[] params, int pid, int uid) {
        String str = TAG;
        LogUtil.d(str, "ioControl,cmd=" + cmd + ",params=" + Arrays.toString(params));
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public int ioControlWithPocket(String cmd, String[] params, String[] results, int pid, int uid) {
        char c;
        int hashCode = cmd.hashCode();
        if (hashCode != -529258264) {
            if (hashCode == 1725558809 && cmd.equals("resourceRelease")) {
                c = 1;
            }
            c = 65535;
        } else {
            if (cmd.equals("resourceAcquire")) {
                c = 0;
            }
            c = 65535;
        }
        if (c == 0) {
            int ret = handleResourceAcquire(params, results, pid, uid);
            return ret;
        } else if (c != 1 || params == null) {
            return -1;
        } else {
            int ret2 = handleResourceRelease(params[0], params[1], pid);
            return ret2;
        }
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
        pw.println("active resource size:" + this.mActiveResourceMap.size());
        List<CarResourceInfo> list = getActiveResources(IAirConditioner.MODULE_NAME, "1");
        if (list != null) {
            for (CarResourceInfo info : list) {
                pw.println("  set status to:" + info);
                setResourceStatus(info.getModule(), info.getResourceType(), 0, info.getCallPid());
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void onClientDied(int pid) {
        String str = TAG;
        LogUtil.i(str, "onClientDied, pid=" + pid);
        synchronized (this.mActiveResourceMap) {
            Iterator<Map.Entry<String, CarResourceInfo>> it = this.mActiveResourceMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, CarResourceInfo> item = it.next();
                CarResourceInfo info = item.getValue();
                if (info.getCallPid() == pid) {
                    it.remove();
                }
            }
        }
    }

    public void addModuleListener(String module, ResourceListener listener) {
        synchronized (this.mModuleListenerMap) {
            this.mModuleListenerMap.put(module, listener);
        }
    }

    public void removeModuleListener(String module, ResourceListener listener) {
        synchronized (this.mModuleListenerMap) {
            this.mModuleListenerMap.remove(module);
        }
    }

    public List<CarResourceInfo> getActiveResources(String module, String type) {
        if (this.mActiveResourceMap.size() < 1) {
            return null;
        }
        ArrayList<CarResourceInfo> list = null;
        synchronized (this.mActiveResourceMap) {
            for (CarResourceInfo info : this.mActiveResourceMap.values()) {
                if (info.getModule().equals(module) && info.getResourceType().equals(type)) {
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(info);
                }
            }
        }
        return list;
    }

    public void setResourceStatus(String module, String type, int status, int pid) {
        CarResourceInfo info;
        synchronized (this.mActiveResourceMap) {
            LinkedHashMap<String, CarResourceInfo> linkedHashMap = this.mActiveResourceMap;
            info = linkedHashMap.get(module + type + pid);
        }
        if (info != null) {
            String[] events = {module, type, String.valueOf(status)};
            this.mPipeListener.onPipeBusNotify(MODULE_NAME, "resourceStatus", events, pid);
        }
    }

    private int handleResourceAcquire(String[] params, String[] results, int pid, int uid) {
        ResourceListener listener;
        if (params != null) {
            CarResourceInfo info = CarResourceInfo.fromJSONString(params[0]);
            if (info == null) {
                LogUtil.w(TAG, "handleResourceAcquire,null info");
                return -1;
            }
            info.setCallPid(pid);
            synchronized (this.mModuleListenerMap) {
                listener = this.mModuleListenerMap.get(info.getModule());
            }
            if (listener != null) {
                int ret = listener.onResourceRequest(info);
                String str = TAG;
                LogUtil.i(str, "handleResourceAcquire,ret=" + ret + ",info=" + info);
                if (ret == 0) {
                    info.setStatus(1);
                    synchronized (this.mActiveResourceMap) {
                        LinkedHashMap<String, CarResourceInfo> linkedHashMap = this.mActiveResourceMap;
                        linkedHashMap.put(info.getModule() + info.getResourceType() + pid, info);
                    }
                }
                return ret;
            }
            String str2 = TAG;
            LogUtil.w(str2, "handleResourceAcquire,no handler for " + info.getModule());
        } else {
            LogUtil.w(TAG, "handleResourceAcquire, params null");
        }
        return -1;
    }

    private int handleResourceRelease(String module, String type, int pid) {
        CarResourceInfo info;
        ResourceListener listener;
        int ret;
        String activeKey = module + type + pid;
        synchronized (this.mActiveResourceMap) {
            info = this.mActiveResourceMap.get(activeKey);
        }
        if (info == null) {
            LogUtil.w(TAG, "handleResourceRelease, no active resource for" + module + "/" + type + "/" + pid);
            return 0;
        }
        synchronized (this.mModuleListenerMap) {
            listener = this.mModuleListenerMap.get(info.getModule());
        }
        if (listener == null) {
            ret = 0;
        } else {
            int ret2 = listener.onResourceRelease(type);
            ret = ret2;
        }
        synchronized (this.mActiveResourceMap) {
            this.mActiveResourceMap.remove(activeKey);
        }
        return ret;
    }
}
