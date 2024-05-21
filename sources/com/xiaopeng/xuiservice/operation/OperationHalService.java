package com.xiaopeng.xuiservice.operation;

import android.annotation.SuppressLint;
import android.app.ActivityThread;
import android.app.PendingIntent;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.mcu.CarMcuManager;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Pair;
import android.util.SparseArray;
import com.xiaopeng.xuimanager.operation.DownloadStatusInfo;
import com.xiaopeng.xuimanager.operation.FailedInfo;
import com.xiaopeng.xuimanager.operation.OperationResource;
import com.xiaopeng.xuimanager.operation.OperationType;
import com.xiaopeng.xuimanager.operation.utils.ResourceBuild;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.carcontrol.HalServiceBaseCarListener;
import com.xiaopeng.xuiservice.message.ResourceMessageHelper;
import com.xiaopeng.xuiservice.operation.OperationResourceSyncer;
import com.xiaopeng.xuiservice.operation.db.ResourceInfo;
import com.xiaopeng.xuiservice.operation.legacy.LegacyResourceManager;
import com.xiaopeng.xuiservice.operation.remote.StoreProviderManagerWarpper;
import com.xiaopeng.xuiservice.operation.resource.AddResourceInfo;
import com.xiaopeng.xuiservice.operation.resource.ResourceManager;
import com.xiaopeng.xuiservice.operation.utils.DataBuild;
import com.xiaopeng.xuiservice.operation.utils.ThreadUtils;
import com.xiaopeng.xuiservice.smart.condition.Condition;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionIG;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.FileUtils;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class OperationHalService extends HalServiceBaseCarListener {
    private static final int MSG_ADD_NEW_RESOURCE = 1;
    private static final int MSG_CANCEL_DOWNLOAD = 7;
    private static final int MSG_DELETE_RESOURCE = 5;
    private static final int MSG_DOWNLOAD_RESOURCE = 4;
    private static final int MSG_EXPIRE_RESOURCE = 11;
    private static final int MSG_PAUSE_DOWNLOAD = 12;
    private static final int MSG_PROCESS_PRESET_RESOURCE = 2;
    private static final int MSG_QUERY_RESOURCE = 6;
    private static final int MSG_RESUME_DOWNLOAD = 13;
    private static final int MSG_SELECT_RESOURCE = 3;
    private static final int MSG_SET_EXPIRE_TASK = 10;
    private static final int MSG_SHOW_RESOURCE_DETAIL = 8;
    private static final int MSG_SYNC_ABORTED = 16;
    private static final int MSG_SYNC_COMPLETED = 14;
    private static final int MSG_SYNC_FAILED = 15;
    private static final int MSG_UNSELECT_RESOURCE = 9;
    private static final String TAG = "OperationHalService";
    private final OperationResourceSyncer.Callback callback;
    private ConditionIG conditionIG;
    private final HashMap<String, Runnable> delayedExpireTask;
    private boolean isRegisterDownloadListener;
    private final List<OperationHalListener> mEventListeners;
    private Handler mHandler;
    private final SparseArray<OperationResourceSyncer> syncers;

    /* loaded from: classes5.dex */
    public interface OperationHalListener {
        void onError(int i, int i2);

        void onEvent(int i, String str, int i2, String str2);
    }

    /* loaded from: classes5.dex */
    private static class OperationHalServiceHolder {
        private static final OperationHalService instance = new OperationHalService(ActivityThread.currentActivityThread().getApplication());

        private OperationHalServiceHolder() {
        }
    }

    public static OperationHalService getInstance() {
        return OperationHalServiceHolder.instance;
    }

    private OperationHalService(Context context) {
        super(context);
        this.mEventListeners = new ArrayList();
        this.delayedExpireTask = new HashMap<>();
        this.syncers = new SparseArray<>();
        this.callback = new OperationResourceSyncer.Callback() { // from class: com.xiaopeng.xuiservice.operation.OperationHalService.1
            @Override // com.xiaopeng.xuiservice.operation.OperationResourceSyncer.Callback
            public void onSyncCompleted(int resourceType) {
                LogUtil.i(OperationHalService.TAG, "onSyncCompleted resourceType" + resourceType);
                Message.obtain(OperationHalService.this.mHandler, 14, resourceType, 0).sendToTarget();
            }

            @Override // com.xiaopeng.xuiservice.operation.OperationResourceSyncer.Callback
            public void onSyncFailed(int resourceType) {
                LogUtil.i(OperationHalService.TAG, "onSyncFailed resourceType" + resourceType);
                Message.obtain(OperationHalService.this.mHandler, 15, resourceType, 0).sendToTarget();
            }

            @Override // com.xiaopeng.xuiservice.operation.OperationResourceSyncer.Callback
            public void onSyncAborted(int resourceType) {
                LogUtil.i(OperationHalService.TAG, "onSyncAborted resourceType" + resourceType);
                Message.obtain(OperationHalService.this.mHandler, 16, resourceType, 0).sendToTarget();
            }
        };
        LogUtil.w(TAG, "started OperationHalService!");
        this.mContext = context;
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    public void init() {
        initHandler();
        if (BroadcastManager.isLockedBootComplete()) {
            LogUtil.w(TAG, "lockedBootComplete!");
            processPresetFile();
        }
        BroadcastManager.getInstance().addLockedBootCompleteTask(new Runnable() { // from class: com.xiaopeng.xuiservice.operation.-$$Lambda$OperationHalService$iiIOsu-m1LebclWri6EdCvg_kyI
            @Override // java.lang.Runnable
            public final void run() {
                OperationHalService.this.lambda$init$0$OperationHalService();
            }
        });
        Message.obtain(this.mHandler, 10).sendToTarget();
    }

    public /* synthetic */ void lambda$init$0$OperationHalService() {
        LogUtil.w(TAG, "receiver LockedBootComplete!");
        processPresetFile();
    }

    public synchronized void setAutoSync(int type) {
        LogUtil.i(TAG, "setAutoSync type=" + type);
        if (this.syncers.get(type) != null) {
            return;
        }
        final OperationResourceSyncer syncer = new OperationResourceSyncer(type, this.callback);
        this.syncers.put(type, syncer);
        initIgCondition();
        ResourceMessageHelper.getInstance().registerResourceUpdateListener(String.valueOf(type), new ResourceMessageHelper.ResourceUpdateListener() { // from class: com.xiaopeng.xuiservice.operation.-$$Lambda$OperationHalService$yCqeSnZFkEAjwLiyQWSDNYcEC-I
            @Override // com.xiaopeng.xuiservice.message.ResourceMessageHelper.ResourceUpdateListener
            public final void onResourceUpdated(String str) {
                OperationHalService.this.lambda$setAutoSync$1$OperationHalService(syncer, str);
            }
        });
    }

    public /* synthetic */ void lambda$setAutoSync$1$OperationHalService(final OperationResourceSyncer syncer, String resourceType) {
        LogUtil.i(TAG, "get resource update message from DC, do sync");
        Handler handler = this.mHandler;
        Objects.requireNonNull(syncer);
        handler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.operation.-$$Lambda$TmGPzUZpEVRWln6nIIE8Tz2HHxs
            @Override // java.lang.Runnable
            public final void run() {
                OperationResourceSyncer.this.sync();
            }
        });
    }

    private void initIgCondition() {
        if (this.conditionIG == null) {
            LogUtil.i(TAG, "create ConditionIG target=IG_ON");
            this.conditionIG = new ConditionIG(1);
            this.conditionIG.addConditionChangeListener(new Condition.ConditionChangeListener() { // from class: com.xiaopeng.xuiservice.operation.-$$Lambda$OperationHalService$cA9PWC2uupuQdBS5M_5_nTv7x8o
                @Override // com.xiaopeng.xuiservice.smart.condition.Condition.ConditionChangeListener
                public final void onConditionChange(Condition condition) {
                    OperationHalService.this.lambda$initIgCondition$2$OperationHalService(condition);
                }
            });
            this.conditionIG.startWatch();
        }
    }

    public /* synthetic */ void lambda$initIgCondition$2$OperationHalService(Condition condition) {
        this.mHandler.removeCallbacks(new Runnable() { // from class: com.xiaopeng.xuiservice.operation.-$$Lambda$OperationHalService$HxbLprv3jkrVmH6USVrs9sDRa98
            @Override // java.lang.Runnable
            public final void run() {
                OperationHalService.this.syncAll();
            }
        });
        if (condition.isMatch()) {
            this.mHandler.postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.operation.-$$Lambda$OperationHalService$HxbLprv3jkrVmH6USVrs9sDRa98
                @Override // java.lang.Runnable
                public final void run() {
                    OperationHalService.this.syncAll();
                }
            }, 10000L);
        } else {
            this.mHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.operation.-$$Lambda$OperationHalService$e0mWenk0nyTcSTUY4pN-UvizEDY
                @Override // java.lang.Runnable
                public final void run() {
                    OperationHalService.this.abortSyncAll();
                }
            });
        }
    }

    private void releaseIgCondition() {
        ConditionIG conditionIG = this.conditionIG;
        if (conditionIG != null) {
            conditionIG.stopWatch();
            this.conditionIG = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void syncAll() {
        int size = this.syncers.size();
        for (int i = 0; i < size; i++) {
            int key = this.syncers.keyAt(i);
            OperationResourceSyncer syncer = this.syncers.get(key);
            if (syncer != null) {
                syncer.sync();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void abortSyncAll() {
        int size = this.syncers.size();
        for (int i = 0; i < size; i++) {
            int key = this.syncers.keyAt(i);
            OperationResourceSyncer syncer = this.syncers.get(key);
            if (syncer != null) {
                syncer.abortIfSyncing();
            }
        }
    }

    public void addEventListener(OperationHalListener listener) {
        synchronized (this.mEventListeners) {
            if (!this.mEventListeners.contains(listener)) {
                this.mEventListeners.add(listener);
            }
        }
    }

    public void removeEventListener(OperationHalListener listener) {
        synchronized (this.mEventListeners) {
            this.mEventListeners.remove(listener);
        }
    }

    public void setDownloadPath(int resourceType, String path) {
        LogUtil.d(TAG, "setDownloadPath resourceType:" + resourceType + " &path:" + path);
        ResourceManager.getInstance().setDownloadPath(resourceType, path);
    }

    private void initHandler() {
        HandlerThread mOperationWorkThread = new HandlerThread("OperationThread");
        mOperationWorkThread.start();
        this.mHandler = new OperationHandler(mOperationWorkThread.getLooper());
    }

    private void processPresetFile() {
        LogUtil.d(TAG, "processPresetFile");
        Message.obtain(this.mHandler, 2).sendToTarget();
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.HalServiceBaseCarListener
    public void dump(PrintWriter writer) {
        LogUtil.d(TAG, "dumpsys");
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
        LogUtil.d(TAG, "onInit");
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        LogUtil.d(TAG, "onRelease");
    }

    public int addNewResource(String id, int type, String rsc_name, String rsc_path, String extraData, String sourceType) {
        LogUtil.i(TAG, "addNewResource " + id + " type:" + type + " name:" + rsc_name + " path:" + rsc_path + "extraData:" + extraData);
        AddResourceInfo addResourceInfo = new AddResourceInfo();
        addResourceInfo.id = id;
        addResourceInfo.type = type;
        addResourceInfo.rsc_name = rsc_name;
        addResourceInfo.rsc_path = rsc_path;
        addResourceInfo.extraData = extraData;
        addResourceInfo.sourceType = sourceType;
        addResourceInfo.createTime = System.currentTimeMillis();
        addResourceInfo.updateTime = addResourceInfo.createTime;
        addResourceInfo.description = null;
        addResourceInfo.resourceIcon = null;
        addResourceInfo.price = null;
        return addNewResource(addResourceInfo);
    }

    public int addNewResource(AddResourceInfo addResourceInfo) {
        LogUtil.i(TAG, "addNewResource resourceInfo=" + addResourceInfo);
        Message message = Message.obtain(this.mHandler, 1, addResourceInfo);
        message.sendToTarget();
        return 0;
    }

    public String getLocalResourceList(int type) {
        return LegacyResourceManager.getInstance().getLocalResourceList(type);
    }

    public String getDownLoadResourceList(int type) {
        return LegacyResourceManager.getInstance().getDownLoadResourceList(type);
    }

    public String getResource(int type, String params) {
        if ("get_remote".equals(params)) {
            queryRemoteResources(type);
            return "";
        } else if ("get_selected".equals(params)) {
            ResourceInfo resourceInfo = ResourceManager.getInstance().getSelectedResourceByType(type);
            return getJsonString(resourceInfo);
        } else {
            List<ResourceInfo> infos = ResourceManager.getInstance().getResourcesByType(type);
            return getJsonString(infos);
        }
    }

    public ResourceInfo getResourceById(String resourceId) {
        return ResourceManager.getInstance().getResourceInfoById(resourceId);
    }

    public String getDownloadStatusList(int type) {
        List<DownloadStatusInfo> list = ResourceManager.getInstance().getDownloadStatusList(type);
        if (list != null && !list.isEmpty()) {
            return ResourceBuild.toDownloadInfoJsonArray(list).toString();
        }
        return "";
    }

    public void selectOperationResource(OperationResource resource) {
        Message message = Message.obtain(this.mHandler, 3, resource);
        message.sendToTarget();
    }

    public void unselectOperationResource(OperationResource resource) {
        Message message = Message.obtain(this.mHandler, 9, resource);
        message.sendToTarget();
    }

    public void downloadOperationResource(OperationResource resource, boolean useSystemTraffic, PendingIntent pendingIntent) {
        Message message = Message.obtain(this.mHandler, 4, useSystemTraffic ? 1 : 0, useSystemTraffic ? 1 : 0, new Pair(resource, pendingIntent));
        message.sendToTarget();
    }

    public void cancelDownloadResource(OperationResource resource) {
        Message message = Message.obtain(this.mHandler, 7, resource);
        message.sendToTarget();
    }

    public void pauseDownloadResource(OperationResource resource) {
        Message message = Message.obtain(this.mHandler, 12, resource);
        message.sendToTarget();
    }

    public void resumeDownloadResource(OperationResource resource) {
        Message message = Message.obtain(this.mHandler, 13, resource);
        message.sendToTarget();
    }

    public void deleteOperationResource(String id) {
        Message message = Message.obtain(this.mHandler, 5, id);
        message.sendToTarget();
    }

    public void requestShowDownloadResourceDetail(OperationResource resource) {
        Message message = Message.obtain(this.mHandler, 8, resource);
        message.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: expireResource */
    public void lambda$addExpireTask$3$OperationHalService(ResourceInfo resource) {
        Message message = Message.obtain(this.mHandler, 11, resource);
        message.sendToTarget();
    }

    private String getJsonString(List<ResourceInfo> infos) {
        if (infos != null && !infos.isEmpty()) {
            JSONArray jsonArray = new JSONArray();
            for (ResourceInfo resourceInfo : infos) {
                jsonArray.put(getJsonObject(resourceInfo));
            }
            return jsonArray.toString();
        }
        return "";
    }

    private String getJsonString(ResourceInfo info) {
        if (info != null) {
            return getJsonObject(info).toString();
        }
        return "";
    }

    private JSONObject getJsonObject(ResourceInfo info) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", info.resourceId);
            jsonObject.put("resourceType", info.resourceType);
            jsonObject.put("resourceName", info.resourceName);
            jsonObject.put("targetPath", info.targetPath);
            jsonObject.put("md5Hash", info.md5Hash);
            jsonObject.put("effectTime", info.effectTime);
            jsonObject.put("expireTime", info.expireTime);
            jsonObject.put("resourceFrom", info.resourceFrom);
            jsonObject.put("status", info.status);
            jsonObject.put("createTime", info.createTime);
            jsonObject.put("updateTime", info.updateTime);
            jsonObject.put("resourceIcon", info.resourceIcon);
            jsonObject.put("description", info.description);
            jsonObject.put("price", info.price);
            jsonObject.put("extraData", info.extraInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void queryRemoteResources(final int type) {
        ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.xiaopeng.xuiservice.operation.OperationHalService.2
            @Override // java.lang.Runnable
            public void run() {
                LogUtil.d(OperationHalService.TAG, "thread pool execute queryRemoteResources");
                List<OperationResource> result = ResourceManager.getInstance().getRemoteResources(type);
                OperationHalService.this.notifyQuerySuccessEvent(type, result);
                OperationHalService.this.syncResourcesStatus(result);
            }
        });
    }

    /* loaded from: classes5.dex */
    public class OperationHandler extends Handler {
        public OperationHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    AddResourceInfo addResourceInfo = (AddResourceInfo) msg.obj;
                    if (!FileUtils.isDataDiskSpaceEnough(addResourceInfo.rsc_path)) {
                        OperationHalService.this.notifyDownloadFailedEvent(addResourceInfo.id, addResourceInfo.type, addResourceInfo.rsc_name, "disk space is not enough");
                        return;
                    }
                    ResourceInfo resourceInfo = ResourceManager.getInstance().addNewResource(addResourceInfo);
                    if (resourceInfo != null) {
                        OperationHalService.this.notifyAddEvent(resourceInfo);
                        OperationHalService.this.addExpireTask(resourceInfo);
                        return;
                    }
                    OperationHalService.this.notifyDownloadFailedEvent(addResourceInfo.id, addResourceInfo.type, addResourceInfo.rsc_name, "file not exist or not a valid zip file");
                    return;
                case 2:
                    ResourceManager.getInstance().processPresetFile();
                    return;
                case 3:
                    OperationResource resource = (OperationResource) msg.obj;
                    ResourceManager.getInstance().selectOperationResource(resource.getResourceType(), resource.getId());
                    OperationHalService.this.notifySelectedEvent(resource);
                    return;
                case 4:
                    Pair<OperationResource, PendingIntent> pair = (Pair) msg.obj;
                    OperationResource resource2 = (OperationResource) pair.first;
                    PendingIntent pendingIntent = (PendingIntent) pair.second;
                    boolean useSystemTraffic = msg.arg1 == 1;
                    OperationHalService.this.registerStoreDownloadListener();
                    ResourceManager.getInstance().downloadOperationResource(resource2, useSystemTraffic, pendingIntent);
                    return;
                case 5:
                    String id = (String) msg.obj;
                    ResourceInfo resourceInfo2 = ResourceManager.getInstance().getResourceInfoById(id);
                    if (resourceInfo2 != null) {
                        ResourceManager.getInstance().deleteOperationResource(resourceInfo2);
                        OperationHalService.this.removeExpireTask(resourceInfo2);
                        OperationHalService.this.notifyDeleteEvent(resourceInfo2);
                        return;
                    }
                    return;
                case 6:
                    int queryType = msg.arg1;
                    LogUtil.d(OperationHalService.TAG, "execute MSG_QUERY_RESOURCE");
                    List<OperationResource> result = ResourceManager.getInstance().getRemoteResources(queryType);
                    OperationHalService.this.notifyQuerySuccessEvent(queryType, result);
                    return;
                case 7:
                    ResourceManager.getInstance().cancelDownloadResource((OperationResource) msg.obj);
                    return;
                case 8:
                    OperationHalService.this.notifyShowDetailRequestEvent((OperationResource) msg.obj);
                    return;
                case 9:
                    OperationResource resource3 = (OperationResource) msg.obj;
                    ResourceManager.getInstance().unselectOperationResource(resource3.getResourceType(), resource3.getId());
                    OperationHalService.this.notifyUnselectedEvent(resource3);
                    return;
                case 10:
                    List<ResourceInfo> resourceInfoList = ResourceManager.getInstance().getAllResources();
                    if (resourceInfoList != null) {
                        final OperationHalService operationHalService = OperationHalService.this;
                        resourceInfoList.forEach(new Consumer() { // from class: com.xiaopeng.xuiservice.operation.-$$Lambda$OperationHalService$OperationHandler$nUcydeYMYd2-9TC1FMbbg7yWTRk
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                OperationHalService.this.addExpireTask((ResourceInfo) obj);
                            }
                        });
                        return;
                    }
                    return;
                case 11:
                    ResourceInfo resourceInfo3 = (ResourceInfo) msg.obj;
                    LogUtil.i(OperationHalService.TAG, "MSG_EXPIRE_RESOURCE: " + resourceInfo3);
                    OperationHalService.this.delayedExpireTask.remove(resourceInfo3.resourceId);
                    OperationHalService.this.notifyExpireEvent(resourceInfo3);
                    ResourceInfo selected = ResourceManager.getInstance().getSelectedResourceByType(resourceInfo3.resourceType);
                    if (selected != null && selected.resourceId.equals(resourceInfo3.resourceId)) {
                        OperationResource operationResource = new OperationResource();
                        operationResource.setId(resourceInfo3.resourceId);
                        operationResource.setResourceType(resourceInfo3.resourceType);
                        OperationHalService.this.unselectOperationResource(operationResource);
                    }
                    OperationHalService.this.deleteOperationResource(resourceInfo3.resourceId);
                    return;
                case 12:
                    ResourceManager.getInstance().pauseDownloadResource((OperationResource) msg.obj);
                    return;
                case 13:
                    OperationHalService.this.registerStoreDownloadListener();
                    ResourceManager.getInstance().resumeDownloadResource((OperationResource) msg.obj);
                    return;
                case 14:
                    OperationHalService.this.notifySyncCompleted(msg.arg1);
                    return;
                case 15:
                    OperationHalService.this.notifySyncFailed(msg.arg1);
                    return;
                case 16:
                    OperationHalService.this.notifySyncAborted(msg.arg1);
                    return;
                default:
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addExpireTask(final ResourceInfo resourceInfo) {
        if (resourceInfo.expireTime > 0) {
            removeExpireTask(resourceInfo);
            long expireDelay = resourceInfo.expireTime - System.currentTimeMillis();
            if (expireDelay <= 0) {
                lambda$addExpireTask$3$OperationHalService(resourceInfo);
                return;
            }
            Runnable task = new Runnable() { // from class: com.xiaopeng.xuiservice.operation.-$$Lambda$OperationHalService$LlKGHz3p1rK_D39tJqZH3cSnr5Y
                @Override // java.lang.Runnable
                public final void run() {
                    OperationHalService.this.lambda$addExpireTask$3$OperationHalService(resourceInfo);
                }
            };
            LogUtil.i(TAG, "expireTask postDelayed: " + expireDelay + "ms, " + resourceInfo);
            this.mHandler.postDelayed(task, expireDelay);
            this.delayedExpireTask.put(resourceInfo.resourceId, task);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeExpireTask(ResourceInfo resourceInfo) {
        Runnable task;
        if (resourceInfo.expireTime > 0 && (task = this.delayedExpireTask.get(resourceInfo.resourceId)) != null) {
            this.mHandler.removeCallbacks(task);
            this.delayedExpireTask.remove(resourceInfo.resourceId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyAddEvent(ResourceInfo resourceInfo) {
        LogUtil.d(TAG, "notifyAddEvent mEventListeners size:" + this.mEventListeners.size() + " &resourceInfo:" + resourceInfo);
        synchronized (this.mEventListeners) {
            if (!this.mEventListeners.isEmpty()) {
                for (OperationHalListener listener : this.mEventListeners) {
                    listener.onEvent(1, resourceInfo.resourceId, resourceInfo.resourceType, DataBuild.toJson(resourceInfo).toString());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyExpireEvent(ResourceInfo resourceInfo) {
        LogUtil.d(TAG, "notifyExpireEvent mEventListeners size:" + this.mEventListeners.size() + " &resourceInfo:" + resourceInfo);
        synchronized (this.mEventListeners) {
            if (!this.mEventListeners.isEmpty()) {
                for (OperationHalListener listener : this.mEventListeners) {
                    listener.onEvent(3, resourceInfo.resourceId, resourceInfo.resourceType, DataBuild.toJson(resourceInfo).toString());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDeleteEvent(ResourceInfo resourceInfo) {
        LogUtil.d(TAG, "notifyDeleteEvent mEventListeners size:" + this.mEventListeners.size() + " &resourceInfo:" + resourceInfo);
        synchronized (this.mEventListeners) {
            if (!this.mEventListeners.isEmpty()) {
                for (OperationHalListener listener : this.mEventListeners) {
                    listener.onEvent(9, resourceInfo.resourceId, resourceInfo.resourceType, DataBuild.toJson(resourceInfo).toString());
                }
            }
        }
    }

    private void notifyRemoveEvent(ResourceInfo resourceInfo) {
        LogUtil.d(TAG, "notifyRemoveEvent:" + resourceInfo);
        synchronized (this.mEventListeners) {
            if (!this.mEventListeners.isEmpty()) {
                for (OperationHalListener listener : this.mEventListeners) {
                    listener.onEvent(2, resourceInfo.resourceId, resourceInfo.resourceType, DataBuild.toJson(resourceInfo).toString());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifySelectedEvent(OperationResource resource) {
        LogUtil.d(TAG, "notifySelectedEvent:" + resource);
        synchronized (this.mEventListeners) {
            if (!this.mEventListeners.isEmpty()) {
                for (OperationHalListener listener : this.mEventListeners) {
                    listener.onEvent(4, resource.getId(), resource.getResourceType(), ResourceBuild.toJson(resource).toString());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyUnselectedEvent(OperationResource resource) {
        LogUtil.d(TAG, "notifyUnselectedEvent:" + resource);
        synchronized (this.mEventListeners) {
            if (!this.mEventListeners.isEmpty()) {
                for (OperationHalListener listener : this.mEventListeners) {
                    listener.onEvent(10, resource.getId(), resource.getResourceType(), ResourceBuild.toJson(resource).toString());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyShowDetailRequestEvent(OperationResource resource) {
        LogUtil.d(TAG, "notifyShowDetailRequest mEventListeners size:" + this.mEventListeners.size() + " &resource:" + resource.toString());
        synchronized (this.mEventListeners) {
            if (!this.mEventListeners.isEmpty()) {
                for (OperationHalListener listener : this.mEventListeners) {
                    listener.onEvent(8, resource.getId(), resource.getResourceType(), ResourceBuild.toJson(resource).toString());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyQuerySuccessEvent(int type, List<OperationResource> resources) {
        LogUtil.d(TAG, "notifyQuerySuccessEvent");
        if (resources == null) {
            return;
        }
        synchronized (this.mEventListeners) {
            if (!this.mEventListeners.isEmpty()) {
                for (OperationHalListener listener : this.mEventListeners) {
                    listener.onEvent(5, "", type, ResourceBuild.toJsonArray(resources).toString());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDownloadStatusEvent(DownloadStatusInfo downloadStatusInfo) {
        if (!this.mEventListeners.isEmpty()) {
            for (OperationHalListener listener : this.mEventListeners) {
                listener.onEvent(7, downloadStatusInfo.getResourceId(), downloadStatusInfo.getResourceType(), ResourceBuild.toJson(downloadStatusInfo).toString());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDownloadFailedEvent(String id, int type, String rsc_name, String reason) {
        FailedInfo failedInfo = new FailedInfo();
        failedInfo.setResourceId(id);
        failedInfo.setResourceType(type);
        failedInfo.setResourceName(rsc_name);
        failedInfo.setFailedReason(reason);
        if (!this.mEventListeners.isEmpty()) {
            for (OperationHalListener listener : this.mEventListeners) {
                listener.onEvent(6, id, type, ResourceBuild.toJson(failedInfo).toString());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifySyncCompleted(int type) {
        if (!this.mEventListeners.isEmpty()) {
            for (OperationHalListener listener : this.mEventListeners) {
                listener.onEvent(11, "", type, "");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifySyncFailed(int type) {
        if (!this.mEventListeners.isEmpty()) {
            for (OperationHalListener listener : this.mEventListeners) {
                listener.onEvent(12, "", type, "");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifySyncAborted(int type) {
        if (!this.mEventListeners.isEmpty()) {
            for (OperationHalListener listener : this.mEventListeners) {
                listener.onEvent(13, "", type, "");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerStoreDownloadListener() {
        if (this.isRegisterDownloadListener) {
            return;
        }
        this.isRegisterDownloadListener = true;
        StoreProviderManagerWarpper.get().registerDownloadListener(new StoreProviderManagerWarpper.StoreDownloadStatusListener() { // from class: com.xiaopeng.xuiservice.operation.OperationHalService.3
            @Override // com.xiaopeng.xuiservice.operation.remote.StoreProviderManagerWarpper.StoreDownloadStatusListener
            public void onResourceDownloaded(OperationResource operationResource, String rsc_path) {
                AddResourceInfo addResourceInfo = new AddResourceInfo();
                addResourceInfo.id = operationResource.getId();
                addResourceInfo.type = operationResource.getResourceType();
                addResourceInfo.rsc_name = operationResource.getResourceName();
                addResourceInfo.rsc_path = rsc_path;
                addResourceInfo.extraData = operationResource.getExtraData();
                addResourceInfo.sourceType = "download_resource";
                addResourceInfo.createTime = operationResource.getCreateTime();
                addResourceInfo.updateTime = operationResource.getUpdateTime();
                addResourceInfo.description = operationResource.getDescription();
                addResourceInfo.resourceIcon = operationResource.getResourceIcon();
                addResourceInfo.price = operationResource.getPrice();
                OperationHalService.this.addNewResource(addResourceInfo);
            }

            @Override // com.xiaopeng.xuiservice.operation.remote.StoreProviderManagerWarpper.StoreDownloadStatusListener
            public void onDownloadStatusChanged(DownloadStatusInfo downloadStatusInfo) {
                OperationHalService.this.notifyDownloadStatusEvent(downloadStatusInfo);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncResourcesStatus(List<OperationResource> list) {
        if (list != null && !list.isEmpty()) {
            for (OperationResource resource : list) {
                updateResourceStatus(resource);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncAllResourceStatus() {
        int[] iArr;
        for (int type : OperationType.SUPPORT_TYPES) {
            syncResourceStatus(type);
        }
    }

    private void syncResourceStatus(final int type) {
        ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.xiaopeng.xuiservice.operation.OperationHalService.4
            @Override // java.lang.Runnable
            public void run() {
                LogUtil.d(OperationHalService.TAG, "thread pool execute queryRemoteResources");
                List<OperationResource> result = ResourceManager.getInstance().getRemoteResources(type);
                OperationHalService.this.syncResourcesStatus(result);
            }
        });
    }

    @SuppressLint({"WrongConstant"})
    private void updateResourceStatus(OperationResource resource) {
        ResourceManager.getInstance().updateResourceInfo(resource.getId(), resource.getStatus());
    }

    private void registerIgStatusListener() {
        CarClientManager.getInstance().addMcuManagerListener(new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.operation.OperationHalService.5
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557847561) {
                    LogUtil.i(OperationHalService.TAG, "receiver ig status changed event :" + value.getValue());
                    int igStatus = ((Integer) value.getValue()).intValue();
                    boolean isIgOn = igStatus == 1;
                    if (isIgOn) {
                        OperationHalService.this.syncAllResourceStatus();
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        });
    }
}
