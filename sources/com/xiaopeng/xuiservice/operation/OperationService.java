package com.xiaopeng.xuiservice.operation;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import com.xiaopeng.xuimanager.operation.IOperation;
import com.xiaopeng.xuimanager.operation.IOperationEventListener;
import com.xiaopeng.xuimanager.operation.OperationResource;
import com.xiaopeng.xuimanager.operation.utils.ResourceBuild;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.IXUIServiceImpl;
import com.xiaopeng.xuiservice.XUIServiceBase;
import com.xiaopeng.xuiservice.debug.WatchDog;
import com.xiaopeng.xuiservice.operation.OperationHalService;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class OperationService extends IOperation.Stub implements XUIServiceBase, OperationHalService.OperationHalListener {
    private static final String TAG = "OperationService";
    private final Context mContext;
    private final Object mLock = new Object();
    private final ArrayList<ClientListenerRecord> mClientListeners = new ArrayList<>();
    private OperationHalService mOperationHalService = OperationHalService.getInstance();

    public OperationService(Context context, IXUIServiceImpl XUIServiceInstance) {
        this.mContext = context;
        this.mOperationHalService.init();
        this.mOperationHalService.addEventListener(this);
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        String info = "code=" + code + ",pid=" + Binder.getCallingPid() + "@" + SystemClock.elapsedRealtimeNanos();
        WatchDog.getInstance().binderEnter(TAG, info);
        try {
            try {
                boolean ret = super.onTransact(code, data, reply, flags);
                return ret;
            } catch (Exception e) {
                LogUtil.w(TAG, "onTransact e=" + e + ",code=" + code);
                StringBuilder sb = new StringBuilder();
                sb.append("exception stack=");
                sb.append(Log.getStackTraceString(e).replaceAll("[\\t\\n\\r]", "##"));
                LogUtil.w(TAG, sb.toString());
                throw e;
            }
        } finally {
            WatchDog.getInstance().binderLeave(TAG, info);
        }
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void init() {
        LogUtil.i(TAG, "init");
        this.mOperationHalService.init();
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void release() {
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void dump(PrintWriter writer) {
    }

    public void registerListener(IOperationEventListener listener) {
        LogUtil.d(TAG, "registerListener=" + listener);
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        synchronized (this.mLock) {
            int index = findIndexOfClientListenerLocked(listener);
            if (index != -1) {
                LogUtil.w(TAG, "IOperationEventListener is already added, ignoring");
                return;
            }
            ClientListenerRecord record = new ClientListenerRecord(listener, pid, uid);
            try {
                listener.asBinder().linkToDeath(record, 0);
                this.mClientListeners.add(record);
            } catch (RemoteException e) {
                LogUtil.e(TAG, "client is dead, ignoring it");
            }
        }
    }

    public void unregisterListener(IOperationEventListener listener) {
        LogUtil.d(TAG, "unregisterListener=" + listener);
        synchronized (this.mLock) {
            int index = findIndexOfClientListenerLocked(listener);
            if (index != -1) {
                ClientListenerRecord record = this.mClientListeners.remove(index);
                try {
                    record.listener.asBinder().unlinkToDeath(record, 0);
                } catch (Exception e) {
                }
            }
        }
    }

    public int addNewResource(String id, int type, String rsc_name, String rsc_path) throws RemoteException {
        this.mOperationHalService.addNewResource(id, type, rsc_name, rsc_path, "", "download_resource");
        return 0;
    }

    public String getLocalResourceList(int type) throws RemoteException {
        return this.mOperationHalService.getLocalResourceList(type);
    }

    public boolean checkResourceExist(int type, String rsc_name) throws RemoteException {
        return false;
    }

    public int deleteResource(String id, int type, String rsc_name) throws RemoteException {
        return 0;
    }

    public String getDownLoadResourceList(int type) throws RemoteException {
        return this.mOperationHalService.getDownLoadResourceList(type);
    }

    public String getResource(int type, String params) throws RemoteException {
        return this.mOperationHalService.getResource(type, params);
    }

    public void downloadResource(int type, String params, PendingIntent pendingIntent) throws RemoteException {
        int pid = Binder.getCallingPid();
        LogUtil.i(TAG, "downloadResource type=" + type + ", params=" + params + ", pendingIntent=" + pendingIntent + ", call pid=" + pid);
        if (!TextUtils.isEmpty(params)) {
            try {
                JSONObject jsonObject = new JSONObject(params);
                boolean useSystemTraffic = jsonObject.optBoolean("useSystemTraffic", false);
                OperationResource operationResource = ResourceBuild.fromJson(jsonObject);
                downloadOperationResource(operationResource, useSystemTraffic, pendingIntent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public void operateResource(String cmd, int type, String params) throws RemoteException {
        char c;
        int pid = Binder.getCallingPid();
        LogUtil.i(TAG, "operateResource cmd=" + cmd + ",type=" + type + ",call pid=" + pid);
        switch (cmd.hashCode()) {
            case -1472290782:
                if (cmd.equals("delete_resource")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -1344712818:
                if (cmd.equals("set_auto_sync")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case -715126965:
                if (cmd.equals("set_resource")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -637755474:
                if (cmd.equals("remove_monitor_resource")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -526694939:
                if (cmd.equals("download_resource")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -367258534:
                if (cmd.equals("resume_download")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case -224824147:
                if (cmd.equals("cancel_download")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 596076600:
                if (cmd.equals("unselect_resource")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1393830065:
                if (cmd.equals("pause_download")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 1649111789:
                if (cmd.equals("show_download_resource_detail")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 2057092401:
                if (cmd.equals("add_monitor_resource")) {
                    c = 0;
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
                Iterator<ClientListenerRecord> it = this.mClientListeners.iterator();
                while (it.hasNext()) {
                    ClientListenerRecord record = it.next();
                    if (record.pid == pid) {
                        record.addResource(type);
                    }
                }
                return;
            case 1:
                Iterator<ClientListenerRecord> it2 = this.mClientListeners.iterator();
                while (it2.hasNext()) {
                    ClientListenerRecord record2 = it2.next();
                    if (record2.pid == pid) {
                        record2.removeResource(type);
                    }
                }
                return;
            case 2:
                if (!TextUtils.isEmpty(params)) {
                    try {
                        JSONObject jsonObject = new JSONObject(params);
                        OperationResource operationResource = ResourceBuild.fromJson(jsonObject);
                        selectOperationResource(operationResource);
                        return;
                    } catch (JSONException e) {
                        LogUtil.e(TAG, "setOperationResource failed:" + e.getMessage());
                        e.printStackTrace();
                        return;
                    }
                }
                return;
            case 3:
                if (!TextUtils.isEmpty(params)) {
                    try {
                        JSONObject jsonObject2 = new JSONObject(params);
                        OperationResource operationResource2 = ResourceBuild.fromJson(jsonObject2);
                        unselectOperationResource(operationResource2);
                        return;
                    } catch (JSONException e2) {
                        LogUtil.e(TAG, "unselectOperationResource failed:" + e2.getMessage());
                        e2.printStackTrace();
                        return;
                    }
                }
                return;
            case 4:
                if (!TextUtils.isEmpty(params)) {
                    try {
                        JSONObject jsonObject3 = new JSONObject(params);
                        boolean useSystemTraffic = jsonObject3.optBoolean("useSystemTraffic", false);
                        OperationResource operationResource3 = ResourceBuild.fromJson(jsonObject3);
                        downloadOperationResource(operationResource3, useSystemTraffic, null);
                        return;
                    } catch (JSONException e3) {
                        e3.printStackTrace();
                        return;
                    }
                }
                return;
            case 5:
                if (!TextUtils.isEmpty(params)) {
                    deleteOperationResource(params);
                    return;
                }
                return;
            case 6:
                if (!TextUtils.isEmpty(params)) {
                    try {
                        JSONObject jsonObject4 = new JSONObject(params);
                        OperationResource operationResource4 = ResourceBuild.fromJson(jsonObject4);
                        cancelDownloadResource(operationResource4);
                        return;
                    } catch (JSONException e4) {
                        e4.printStackTrace();
                        return;
                    }
                }
                return;
            case 7:
                if (!TextUtils.isEmpty(params)) {
                    try {
                        JSONObject jsonObject5 = new JSONObject(params);
                        OperationResource operationResource5 = ResourceBuild.fromJson(jsonObject5);
                        pauseDownloadResource(operationResource5);
                        return;
                    } catch (JSONException e5) {
                        e5.printStackTrace();
                        return;
                    }
                }
                return;
            case '\b':
                if (!TextUtils.isEmpty(params)) {
                    try {
                        JSONObject jsonObject6 = new JSONObject(params);
                        OperationResource operationResource6 = ResourceBuild.fromJson(jsonObject6);
                        resumeDownloadResource(operationResource6);
                        return;
                    } catch (JSONException e6) {
                        e6.printStackTrace();
                        return;
                    }
                }
                return;
            case '\t':
                if (!TextUtils.isEmpty(params)) {
                    try {
                        JSONObject jsonObject7 = new JSONObject(params);
                        OperationResource operationResource7 = ResourceBuild.fromJson(jsonObject7);
                        requestShowDownloadResourceDetail(operationResource7);
                        return;
                    } catch (JSONException e7) {
                        e7.printStackTrace();
                        return;
                    }
                }
                return;
            case '\n':
                setAutoSync(type);
                return;
            default:
                return;
        }
    }

    public String getInfo(String cmd, int type, String params) throws RemoteException {
        int pid = Binder.getCallingPid();
        LogUtil.i(TAG, "getInfo cmd=" + cmd + ",type=" + type + ",call pid=" + pid);
        if (((cmd.hashCode() == -2111702819 && cmd.equals("get_download_status_list")) ? (char) 0 : (char) 65535) == 0) {
            return this.mOperationHalService.getDownloadStatusList(type);
        }
        return "";
    }

    @Override // com.xiaopeng.xuiservice.operation.OperationHalService.OperationHalListener
    public void onEvent(int code, String id, int type, String event) {
        LogUtil.i(TAG, "onEvent id=" + id + ",type=" + type + ",event=" + event);
        synchronized (this.mLock) {
            if (!this.mClientListeners.isEmpty()) {
                Iterator<ClientListenerRecord> it = this.mClientListeners.iterator();
                while (it.hasNext()) {
                    ClientListenerRecord listenerRecord = it.next();
                    listenerRecord.onOperationEvent(code, id, type, event);
                }
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.operation.OperationHalService.OperationHalListener
    public void onError(int errorCode, int operation) {
        synchronized (this.mLock) {
            if (!this.mClientListeners.isEmpty()) {
                Iterator<ClientListenerRecord> it = this.mClientListeners.iterator();
                while (it.hasNext()) {
                    ClientListenerRecord listenerRecord = it.next();
                    try {
                        listenerRecord.listener.onError(errorCode, operation);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private int findIndexOfClientListenerLocked(IOperationEventListener listener) {
        for (int i = this.mClientListeners.size() - 1; i >= 0; i--) {
            if (this.mClientListeners.get(i).listener.asBinder() == listener.asBinder()) {
                return i;
            }
        }
        return -1;
    }

    private void selectOperationResource(OperationResource resource) {
        this.mOperationHalService.selectOperationResource(resource);
    }

    private void unselectOperationResource(OperationResource resource) {
        this.mOperationHalService.unselectOperationResource(resource);
    }

    private void downloadOperationResource(OperationResource resource, boolean useSystemTraffic, PendingIntent pendingIntent) {
        this.mOperationHalService.downloadOperationResource(resource, useSystemTraffic, pendingIntent);
    }

    private void cancelDownloadResource(OperationResource resource) {
        this.mOperationHalService.cancelDownloadResource(resource);
    }

    private void pauseDownloadResource(OperationResource resource) {
        this.mOperationHalService.pauseDownloadResource(resource);
    }

    private void resumeDownloadResource(OperationResource resource) {
        this.mOperationHalService.resumeDownloadResource(resource);
    }

    private void deleteOperationResource(String id) {
        this.mOperationHalService.deleteOperationResource(id);
    }

    private void requestShowDownloadResourceDetail(OperationResource resource) {
        this.mOperationHalService.requestShowDownloadResourceDetail(resource);
    }

    private void setAutoSync(int resourceType) {
        this.mOperationHalService.setAutoSync(resourceType);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public final class ClientListenerRecord implements IBinder.DeathRecipient {
        public final IOperationEventListener listener;
        public final int pid;
        public List<Integer> resourceTypeList;
        public final int uid;

        ClientListenerRecord(IOperationEventListener listener, int pid, int uid) {
            this.listener = listener;
            this.pid = pid;
            this.uid = uid;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (OperationService.this.mLock) {
                LogUtil.w(OperationService.TAG, "binder died,pid=" + this.pid);
                OperationService.this.mClientListeners.remove(this);
            }
        }

        public void addResource(int type) {
            synchronized (OperationService.this.mLock) {
                if (this.resourceTypeList == null) {
                    this.resourceTypeList = new ArrayList();
                }
                if (!this.resourceTypeList.contains(Integer.valueOf(type))) {
                    this.resourceTypeList.add(Integer.valueOf(type));
                }
            }
        }

        public void removeResource(int type) {
            synchronized (OperationService.this.mLock) {
                if (this.resourceTypeList.contains(Integer.valueOf(type))) {
                    this.resourceTypeList.remove(type);
                }
            }
        }

        public void onOperationEvent(int code, String id, int type, String event) {
            LogUtil.i(OperationService.TAG, "onOperationEvent:resourceTypeList size:" + this.resourceTypeList.size());
            synchronized (OperationService.this.mLock) {
                if (this.resourceTypeList != null && this.resourceTypeList.contains(Integer.valueOf(type))) {
                    try {
                        LogUtil.i(OperationService.TAG, "onOperationEvent ipc calling");
                        this.listener.onEvent(code, id, type, event);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
