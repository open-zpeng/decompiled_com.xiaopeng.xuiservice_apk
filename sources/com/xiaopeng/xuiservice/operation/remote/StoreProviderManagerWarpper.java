package com.xiaopeng.xuiservice.operation.remote;

import android.app.ActivityThread;
import android.app.PendingIntent;
import android.util.ArrayMap;
import com.xiaopeng.appstore.storeprovider.AssembleConstants;
import com.xiaopeng.appstore.storeprovider.AssembleInfo;
import com.xiaopeng.appstore.storeprovider.AssembleRequest;
import com.xiaopeng.appstore.storeprovider.IAssembleClientListener;
import com.xiaopeng.appstore.storeprovider.RequestContinuation;
import com.xiaopeng.appstore.storeprovider.Resource;
import com.xiaopeng.appstore.storeprovider.ResourceContainer;
import com.xiaopeng.appstore.storeprovider.ResourceRequest;
import com.xiaopeng.appstore.storeprovider.SimpleRequest;
import com.xiaopeng.appstore.storeprovider.StoreProviderManager;
import com.xiaopeng.xuimanager.operation.DownloadStatusInfo;
import com.xiaopeng.xuimanager.operation.OperationResource;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.operation.utils.DataBuild;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes5.dex */
public class StoreProviderManagerWarpper implements IAssembleClientListener {
    private static final String EXTRA_KEY_PARAMS_DOWNLOAD_LIST_OPEN_ACTION = "extra_key_params_download_list_open_action";
    private static final String TAG = "StoreProviderManagerWarpper";
    private static final String TASK_KEY = "com.xiaopeng.xuiservice";
    private static final String TASK_NAME = "download_operation_resource";
    private final ArrayMap<String, OperationResource> downloadingResources;
    private final List<StoreDownloadStatusListener> mStoreDownloadStatusListeners;

    /* loaded from: classes5.dex */
    public static class SingletonHolder {
        public static StoreProviderManagerWarpper instance = new StoreProviderManagerWarpper();
    }

    /* loaded from: classes5.dex */
    public interface StoreDownloadStatusListener {
        void onDownloadStatusChanged(DownloadStatusInfo downloadStatusInfo);

        void onResourceDownloaded(OperationResource operationResource, String str);
    }

    private StoreProviderManagerWarpper() {
        this.mStoreDownloadStatusListeners = new ArrayList();
        this.downloadingResources = new ArrayMap<>();
        StoreProviderManager.get().initialize(ActivityThread.currentActivityThread().getApplication());
    }

    public static StoreProviderManagerWarpper get() {
        return SingletonHolder.instance;
    }

    public void registerDownloadListener(StoreDownloadStatusListener listener) {
        synchronized (this.mStoreDownloadStatusListeners) {
            if (this.mStoreDownloadStatusListeners.isEmpty()) {
                StoreProviderManager.get().registerListener(4000, this);
                StoreProviderManager.get().startObserve();
            }
            if (!this.mStoreDownloadStatusListeners.contains(listener)) {
                this.mStoreDownloadStatusListeners.add(listener);
            }
        }
    }

    public void unregisterDownloadListener(StoreDownloadStatusListener listener) {
        synchronized (this.mStoreDownloadStatusListeners) {
            if (this.mStoreDownloadStatusListeners.contains(listener)) {
                this.mStoreDownloadStatusListeners.remove(listener);
            }
            if (this.mStoreDownloadStatusListeners.isEmpty()) {
                StoreProviderManager.get().unregisterListener(this);
                StoreProviderManager.get().stopObserve();
            }
        }
    }

    public List<OperationResource> getRemoteResourceList(int type) {
        ResourceRequest request = new ResourceRequest.Builder(4000, 200).putExtra("remote_res_type_key", type).build();
        ResourceContainer container = StoreProviderManager.get().query(request);
        List<OperationResource> operationResources = new ArrayList<>();
        if (container != null) {
            LogUtil.d(TAG, "getRemoteResourceList result:" + container.toString());
            List<Resource> storeResources = container.getResourceList();
            if (storeResources != null && storeResources.size() > 0) {
                for (Resource resource : storeResources) {
                    OperationResource operationResource = new OperationResource();
                    operationResource.setResourceType(resource.getResType());
                    operationResource.setId(resource.getRscId());
                    operationResource.setResourceName(resource.getRscName());
                    operationResource.setStatus(resource.getClientStatus());
                    operationResource.setDownloadUrl(resource.getDownloadUrl());
                    operationResource.setExtraData(DataBuild.generateExtraData(resource.getExpandContent()));
                    operationResource.setCreateTime(resource.getCreateTime());
                    operationResource.setUpdateTime(resource.getUpdateTime());
                    operationResource.setResourceIcon(resource.getRscIcon());
                    operationResource.setDescription(resource.getDes());
                    operationResource.setPrice(resource.getPrice());
                    operationResources.add(operationResource);
                }
            }
        }
        return operationResources;
    }

    public void downloadOperationResource(OperationResource resource, boolean useSystemTraffic, PendingIntent pendingIntent) {
        this.downloadingResources.put(resource.getId(), resource);
        RequestContinuation requestContinuation = AssembleRequest.enqueue(4000, resource.getDownloadUrl(), TASK_NAME).putExtra("id", resource.getId()).putExtra("resourceName", resource.getResourceName()).putExtra("resourceType", String.valueOf(resource.getResourceType())).putExtra("extraData", resource.getExtraData()).setDownloadUrl(resource.getDownloadUrl()).setUseSystemUidDownload(useSystemTraffic);
        if (pendingIntent != null) {
            requestContinuation.putExtra("extra_key_params_download_list_open_action", pendingIntent);
            requestContinuation.setNotificationVisibility(100);
        } else {
            requestContinuation.setNotificationVisibility(1);
        }
        StoreProviderManager.get().assemble(requestContinuation.request());
    }

    public void cancelDownloadResource(OperationResource resource) {
        LogUtil.d(TAG, "cancelDownloadResource:" + resource.toString());
        SimpleRequest simpleRequest = AssembleRequest.cancel(4000, resource.getDownloadUrl());
        StoreProviderManager.get().assemble(simpleRequest);
        this.downloadingResources.remove(resource.getId());
    }

    public void pauseDownloadResource(OperationResource resource) {
        LogUtil.d(TAG, "pauseDownloadResource:" + resource.toString());
        SimpleRequest simpleRequest = AssembleRequest.pause(4000, resource.getDownloadUrl());
        StoreProviderManager.get().assemble(simpleRequest);
    }

    public void resumeDownloadResource(OperationResource resource) {
        if (!this.downloadingResources.containsKey(resource.getId())) {
            this.downloadingResources.put(resource.getId(), resource);
        }
        LogUtil.d(TAG, "resumeDownloadResource:" + resource.toString());
        SimpleRequest simpleRequest = AssembleRequest.resume(4000, resource.getDownloadUrl());
        StoreProviderManager.get().assemble(simpleRequest);
    }

    public List<DownloadStatusInfo> getDownloadInfoList(int type) {
        List<DownloadStatusInfo> downloadStatusInfoList = null;
        List<AssembleInfo> assembleInfoList = StoreProviderManager.get().getAssembleInfoList();
        if (assembleInfoList != null && !assembleInfoList.isEmpty()) {
            downloadStatusInfoList = new ArrayList<>();
            for (AssembleInfo assembleInfo : assembleInfoList) {
                int assembleInfoType = Integer.valueOf(assembleInfo.getExtraString("resourceType")).intValue();
                if (assembleInfoType == type) {
                    DownloadStatusInfo info = getDownloadStatusInfo(assembleInfo);
                    downloadStatusInfoList.add(info);
                }
            }
        }
        return downloadStatusInfoList;
    }

    @Override // com.xiaopeng.appstore.storeprovider.IAssembleClientListener
    public void onAssembleEvent(int i, AssembleInfo assembleInfo) {
        if (i == 1000 && AssembleInfo.isCompleted(assembleInfo.getState())) {
            notifyDownloadCompleteEvent(assembleInfo);
        }
        notifyDownloadStatusEvent(assembleInfo);
    }

    private void notifyDownloadCompleteEvent(AssembleInfo assembleInfo) {
        String resourceId = assembleInfo.getExtraString("id");
        OperationResource operationResource = this.downloadingResources.get(resourceId);
        if (operationResource == null) {
            LogUtil.e(TAG, "notifyDownloadCompleteEvent can't get downloading OperationResource, drop");
            return;
        }
        this.downloadingResources.remove(resourceId);
        String resourcePath = assembleInfo.getExtraString(AssembleConstants.EXTRA_KEY_PARAMS_FILE_PATH);
        synchronized (this.mStoreDownloadStatusListeners) {
            for (StoreDownloadStatusListener listener : this.mStoreDownloadStatusListeners) {
                listener.onResourceDownloaded(operationResource, resourcePath);
            }
        }
    }

    private DownloadStatusInfo getDownloadStatusInfo(AssembleInfo info) {
        DownloadStatusInfo downloadStatusInfo = new DownloadStatusInfo();
        downloadStatusInfo.setResourceId(info.getExtraString("id"));
        downloadStatusInfo.setResourceType(Integer.valueOf(info.getExtraString("resourceType")).intValue());
        downloadStatusInfo.setResourceName(info.getExtraString("resourceName"));
        downloadStatusInfo.setProgress(info.getProgress());
        downloadStatusInfo.setState(info.getState());
        return downloadStatusInfo;
    }

    private void notifyDownloadStatusEvent(AssembleInfo assembleInfo) {
        DownloadStatusInfo downloadStatusInfo = getDownloadStatusInfo(assembleInfo);
        synchronized (this.mStoreDownloadStatusListeners) {
            for (StoreDownloadStatusListener listener : this.mStoreDownloadStatusListeners) {
                listener.onDownloadStatusChanged(downloadStatusInfo);
            }
        }
    }
}
