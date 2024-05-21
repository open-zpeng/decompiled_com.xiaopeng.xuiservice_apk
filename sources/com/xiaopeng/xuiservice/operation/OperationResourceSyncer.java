package com.xiaopeng.xuiservice.operation;

import com.xiaopeng.xuimanager.XUIManager;
import com.xiaopeng.xuimanager.operation.DownloadStatusInfo;
import com.xiaopeng.xuimanager.operation.FailedInfo;
import com.xiaopeng.xuimanager.operation.IOperationListener;
import com.xiaopeng.xuimanager.operation.OperationManager;
import com.xiaopeng.xuimanager.operation.OperationResource;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class OperationResourceSyncer implements IOperationListener {
    private static final String TAG = "operationsyncer";
    private final Callback callback;
    private CountDownLatch downloadCountDown;
    private List<String> downloadFailedResourceIds;
    private List<OperationResource> downloadingResources;
    private OperationManager operationManager;
    private CountDownLatch queryCountDown;
    private List<OperationResource> remoteResources;
    private final int resourceType;
    private final AtomicBoolean syncingFlag = new AtomicBoolean(false);
    private volatile boolean abortFlag = false;

    /* loaded from: classes5.dex */
    public interface Callback {
        void onSyncAborted(int i);

        void onSyncCompleted(int i);

        void onSyncFailed(int i);
    }

    public OperationResourceSyncer(int type, Callback callback) {
        this.resourceType = type;
        this.callback = callback;
        init();
    }

    private void init() {
        this.operationManager = (OperationManager) XUIManager.getInstance().getService("operation");
        this.operationManager.registerListener(this.resourceType, this);
        INFO("init " + this);
    }

    private void unInit() {
        this.operationManager.unRegisterListener(this.resourceType, this);
        INFO("unInit " + this);
    }

    public List<OperationResource> getResources() {
        return (List) this.operationManager.getResourceList(this.resourceType).stream().filter(new Predicate() { // from class: com.xiaopeng.xuiservice.operation.-$$Lambda$OperationResourceSyncer$KTBFzHaY1yH3vYl_5GS7UEcGG6E
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return OperationResourceSyncer.lambda$getResources$0((OperationResource) obj);
            }
        }).collect(Collectors.toList());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$getResources$0(OperationResource resource) {
        return resource.getStatus() == 0;
    }

    public void sync() {
        if (!this.syncingFlag.compareAndSet(false, true)) {
            return;
        }
        this.abortFlag = false;
        new Thread(new Runnable() { // from class: com.xiaopeng.xuiservice.operation.-$$Lambda$OperationResourceSyncer$2ZtWyxrG4UA1ssjZgVFi1w0D7OU
            @Override // java.lang.Runnable
            public final void run() {
                OperationResourceSyncer.this.doSync();
            }
        }).start();
    }

    public void abortIfSyncing() {
        if (!this.syncingFlag.get()) {
            return;
        }
        this.abortFlag = true;
        INFO("abort sync, resourceType = " + this.resourceType);
        CountDownLatch countDownLatch = this.queryCountDown;
        if (countDownLatch != null) {
            releaseCountDownLatch(countDownLatch);
        }
        CountDownLatch countDownLatch2 = this.downloadCountDown;
        if (countDownLatch2 != null) {
            releaseCountDownLatch(countDownLatch2);
        }
    }

    private void releaseCountDownLatch(CountDownLatch countDownLatch) {
        while (countDownLatch.getCount() > 0) {
            countDownLatch.countDown();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doSync() {
        String str;
        String str2;
        List<OperationResource> localResources;
        Iterator<OperationResource> it;
        boolean queryFinish;
        String str3;
        String str4;
        boolean z;
        INFO("$$$$$$$$$$$$$ Start sync resource, type = " + this.resourceType + " $$$$$$$$$$$$$");
        List<OperationResource> localResources2 = this.operationManager.getResourceList(this.resourceType);
        INFO("LOCAL resources " + localResources2.size() + ":");
        Iterator<OperationResource> it2 = localResources2.iterator();
        int index = 0;
        while (true) {
            str = "] ";
            str2 = "[";
            if (!it2.hasNext()) {
                break;
            }
            OperationResource resource = it2.next();
            INFO("[" + index + "] " + resource);
            index++;
        }
        boolean queryFinish2 = false;
        boolean z2 = true;
        this.queryCountDown = new CountDownLatch(1);
        this.operationManager.getRemoteResourceList(this.resourceType);
        try {
            queryFinish2 = this.queryCountDown.await(1L, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.queryCountDown = null;
        if (this.abortFlag) {
            notifyAborted();
            return;
        }
        if (queryFinish2 && this.remoteResources != null) {
            INFO("REMOTE resources " + this.remoteResources.size() + ":");
            Iterator<OperationResource> it3 = this.remoteResources.iterator();
            int index2 = 0;
            while (it3.hasNext()) {
                OperationResource remote = it3.next();
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                int index3 = index2 + 1;
                sb.append(index2);
                sb.append(str);
                sb.append(remote);
                INFO(sb.toString());
                String resourceId = remote.getId();
                OperationResource local = findResourceById(localResources2, resourceId);
                if (local != null) {
                    int statusRemote = remote.getStatus();
                    int versionRemote = getOperationResourceVersion(remote);
                    int statusLocal = local.getStatus();
                    int versionLocal = getOperationResourceVersion(local);
                    if (versionLocal != versionRemote || statusLocal != statusRemote) {
                        localResources = localResources2;
                        it = it3;
                        queryFinish = queryFinish2;
                        str3 = str;
                        str4 = str2;
                        if (statusRemote != 0) {
                            z = true;
                            if (statusRemote == 1) {
                                if (versionRemote > versionLocal) {
                                    INFO("Update disabled resource, just delete local, id = " + remote.getId() + ", version " + versionLocal + " -> " + versionRemote);
                                    this.operationManager.deleteResource(resourceId);
                                } else if (versionRemote == versionLocal) {
                                    INFO("Disable resource id = " + resourceId);
                                } else {
                                    INFO("!!! OPERATION ERROR, resource id = " + resourceId + " local version: " + versionLocal + " > remote version: " + versionRemote);
                                }
                            } else if (statusRemote == 2) {
                                INFO("Delete resource, resource id = " + resourceId + ", local status = " + statusLocal);
                                this.operationManager.deleteResource(resourceId);
                            } else {
                                INFO("Unknown remote status = " + statusRemote + ", resource id = " + resourceId);
                            }
                        } else if (versionRemote > versionLocal) {
                            INFO("Update resource, delete local & download remote, id = " + remote.getId() + ", version " + versionLocal + " -> " + versionRemote);
                            this.operationManager.deleteResource(resourceId);
                            if (this.downloadingResources == null) {
                                this.downloadingResources = new ArrayList();
                            }
                            this.downloadingResources.add(remote);
                            z = true;
                        } else if (versionRemote == versionLocal) {
                            INFO("Restore to normal status, resource id = " + resourceId + ", local status = " + statusLocal);
                            z = true;
                        } else {
                            INFO("!!! OPERATION ERROR, resource id = " + resourceId + " local version: " + versionLocal + " > remote version: " + versionRemote);
                            z = true;
                        }
                    } else {
                        localResources = localResources2;
                        StringBuilder sb2 = new StringBuilder();
                        it = it3;
                        sb2.append("No update, resource id = ");
                        sb2.append(resourceId);
                        sb2.append(", version = ");
                        sb2.append(versionLocal);
                        sb2.append(", status = ");
                        sb2.append(statusLocal);
                        INFO(sb2.toString());
                        queryFinish = queryFinish2;
                        str3 = str;
                        str4 = str2;
                        z = true;
                    }
                } else {
                    localResources = localResources2;
                    it = it3;
                    queryFinish = queryFinish2;
                    str3 = str;
                    str4 = str2;
                    z = z2;
                    if (remote.getStatus() == 0) {
                        INFO("Download only remote resource id = " + remote.getId());
                        if (this.downloadingResources == null) {
                            this.downloadingResources = new ArrayList();
                        }
                        this.downloadingResources.add(remote);
                    } else {
                        INFO("IGNORE only remote resource id = " + remote.getId() + ", status = " + remote.getStatus());
                    }
                }
                z2 = z;
                index2 = index3;
                localResources2 = localResources;
                it3 = it;
                queryFinish2 = queryFinish;
                str = str3;
                str2 = str4;
            }
            boolean z3 = z2;
            boolean success = true;
            List<OperationResource> list = this.downloadingResources;
            if (list != null) {
                int size = list.size();
                this.downloadCountDown = new CountDownLatch(size);
                for (OperationResource resource2 : this.downloadingResources) {
                    startOrResumeDownload(resource2);
                }
                boolean downloadFinish = false;
                long startTime = System.currentTimeMillis();
                try {
                    downloadFinish = this.downloadCountDown.await(30L, TimeUnit.MINUTES);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                this.downloadCountDown = null;
                success = (downloadFinish && this.downloadFailedResourceIds == null) ? false : false;
                INFO("Download finish = " + downloadFinish + ", use " + (System.currentTimeMillis() - startTime) + "ms");
            }
            this.downloadingResources = null;
            this.remoteResources = null;
            this.downloadFailedResourceIds = null;
            if (this.abortFlag) {
                notifyAborted();
            } else if (success) {
                notifyCompleted();
            } else {
                notifyFailed();
            }
            INFO("$$$$$$$$$$$$$ End sync resource, type = " + this.resourceType + " $$$$$$$$$$$$$");
            this.syncingFlag.set(false);
            return;
        }
        ERROR("getRemoteResourceList timeout or interrupted.", null);
        notifyFailed();
    }

    private void startOrResumeDownload(final OperationResource resource) {
        INFO("startOrResumeDownload: " + resource);
        DownloadStatusInfo pausedDownload = null;
        List<DownloadStatusInfo> list = this.operationManager.getDownloadStatusList(this.resourceType);
        if (list != null) {
            Optional<DownloadStatusInfo> optional = list.stream().filter(new Predicate() { // from class: com.xiaopeng.xuiservice.operation.-$$Lambda$OperationResourceSyncer$heooMvAPrU3-8KGlyQTtLHn70FM
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return OperationResourceSyncer.lambda$startOrResumeDownload$1(resource, (DownloadStatusInfo) obj);
                }
            }).findFirst();
            if (optional.isPresent()) {
                pausedDownload = optional.get();
            }
        }
        if (pausedDownload != null) {
            INFO("exist paused download task, resume it");
            this.operationManager.resumeDownload(resource);
            return;
        }
        INFO("start a new download task: id = " + resource.getId());
        this.operationManager.downloadResource(false, resource);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$startOrResumeDownload$1(OperationResource resource, DownloadStatusInfo downloadStatusInfo) {
        return downloadStatusInfo.getState() == 200 && resource.getId().equals(downloadStatusInfo.getResourceId());
    }

    private int getOperationResourceVersion(OperationResource resource) {
        try {
            JSONObject jsonObject = new JSONObject(resource.getExtraData());
            return jsonObject.getJSONObject("moduleData").getInt("resource_version");
        } catch (Exception e) {
            ERROR("getOperationResourceVersion fail", e);
            return 0;
        }
    }

    public void onOperationSourceAdd(int type, OperationResource resource) {
        INFO("type=" + type + " onOperationSourceAdd: " + resource + ", this = " + this);
        finishDownload(resource.getId(), true);
    }

    private void finishDownload(String resourceId, boolean success) {
        OperationResource find;
        if (resourceId == null) {
            return;
        }
        synchronized (this) {
            if (this.downloadingResources != null && (find = findResourceById(this.downloadingResources, resourceId)) != null) {
                this.downloadingResources.remove(find);
                if (!success) {
                    if (this.downloadFailedResourceIds == null) {
                        this.downloadFailedResourceIds = new ArrayList();
                    }
                    this.downloadFailedResourceIds.add(resourceId);
                }
                CountDownLatch countDownLatch = this.downloadCountDown;
                if (countDownLatch != null) {
                    countDownLatch.countDown();
                }
            }
        }
    }

    public void onOperationSourceExpire(int type, OperationResource resource) {
        INFO("type=" + type + " onOperationSourceExpire: " + resource + ", this = " + this);
    }

    public void onOperationSourceDelete(int type, OperationResource resource) {
        INFO("type=" + type + " onOperationSourceDelete: " + resource + ", this = " + this);
    }

    public void onResourceDownloadFailed(int type, FailedInfo failedInfo) {
        super.onResourceDownloadFailed(type, failedInfo);
        INFO("type=" + type + " onResourceDownloadFailed: " + failedInfo + ", this = " + this);
        finishDownload(failedInfo.getResourceId(), false);
    }

    public void onDownloadStatusChanged(int type, DownloadStatusInfo downloadStatusInfo) {
        super.onDownloadStatusChanged(type, downloadStatusInfo);
        INFO("type=" + type + " onDownloadStatusChanged: " + downloadStatusInfo + ", this = " + this);
    }

    public void onRemoteSourceQuerySuccess(int type, List<OperationResource> resources) {
        if (resources == null) {
            resources = Collections.emptyList();
        }
        INFO("type=" + type + " onRemoteSourceQuerySuccess: " + Arrays.toString(resources.toArray()) + ", this = " + this);
        this.remoteResources = new ArrayList(resources);
        CountDownLatch countDownLatch = this.queryCountDown;
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }

    private void notifyCompleted() {
        if (this.callback != null) {
            XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.operation.-$$Lambda$OperationResourceSyncer$O7oom6SX2TcTBc3JO8GqVRX57CU
                @Override // java.lang.Runnable
                public final void run() {
                    OperationResourceSyncer.this.lambda$notifyCompleted$2$OperationResourceSyncer();
                }
            });
        }
    }

    public /* synthetic */ void lambda$notifyCompleted$2$OperationResourceSyncer() {
        this.callback.onSyncCompleted(this.resourceType);
    }

    private void notifyFailed() {
        if (this.callback != null) {
            XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.operation.-$$Lambda$OperationResourceSyncer$1buXjbTPZDjCtIr954T8Aor3sDk
                @Override // java.lang.Runnable
                public final void run() {
                    OperationResourceSyncer.this.lambda$notifyFailed$3$OperationResourceSyncer();
                }
            });
        }
    }

    public /* synthetic */ void lambda$notifyFailed$3$OperationResourceSyncer() {
        this.callback.onSyncFailed(this.resourceType);
    }

    private void notifyAborted() {
        if (this.callback != null) {
            XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.operation.-$$Lambda$OperationResourceSyncer$YpKMHkFs6GK1wpPTr-IHDoyxU8Y
                @Override // java.lang.Runnable
                public final void run() {
                    OperationResourceSyncer.this.lambda$notifyAborted$4$OperationResourceSyncer();
                }
            });
        }
    }

    public /* synthetic */ void lambda$notifyAborted$4$OperationResourceSyncer() {
        this.callback.onSyncAborted(this.resourceType);
    }

    private OperationResource findResourceById(List<OperationResource> resources, String id) {
        if (id == null || resources == null) {
            return null;
        }
        for (OperationResource resource : resources) {
            if (id.equals(resource.getId())) {
                return resource;
            }
        }
        return null;
    }

    public String toString() {
        return super.toString() + "{resourceType=" + this.resourceType + '}';
    }

    private static void INFO(String msg) {
        LogUtil.i(TAG, msg);
    }

    private static void ERROR(String msg, Throwable t) {
        LogUtil.e(TAG, msg, t);
    }
}
