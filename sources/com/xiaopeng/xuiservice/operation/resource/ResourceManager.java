package com.xiaopeng.xuiservice.operation.resource;

import android.app.ActivityThread;
import android.app.PendingIntent;
import android.text.TextUtils;
import android.util.SparseArray;
import com.xiaopeng.xuimanager.operation.DownloadStatusInfo;
import com.xiaopeng.xuimanager.operation.OperationResource;
import com.xiaopeng.xuimanager.operation.internal.OperationConstants;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.operation.db.ResourceInfo;
import com.xiaopeng.xuiservice.operation.db.ResourceRepository;
import com.xiaopeng.xuiservice.operation.db.SelectedInfo;
import com.xiaopeng.xuiservice.operation.legacy.ResourceConvert;
import com.xiaopeng.xuiservice.operation.remote.StoreProviderManagerWarpper;
import com.xiaopeng.xuiservice.operation.utils.DataBuild;
import com.xiaopeng.xuiservice.utils.FileUtils;
import java.io.File;
import java.util.List;
/* loaded from: classes5.dex */
public class ResourceManager {
    private static final String TAG = "ResourceManager";
    private SparseArray<String> mPathArray;
    private PresetFileProcessor mPresetFileProcessor;
    private StoreProviderManagerWarpper mStoreProviderManagerWarpper;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class ResourceManagerHolder {
        private static final ResourceManager sInstance = new ResourceManager();

        private ResourceManagerHolder() {
        }
    }

    private ResourceManager() {
        this.mPathArray = new SparseArray<>();
        this.mPresetFileProcessor = new PresetFileProcessor();
        this.mStoreProviderManagerWarpper = StoreProviderManagerWarpper.get();
    }

    public static ResourceManager getInstance() {
        return ResourceManagerHolder.sInstance;
    }

    public void processPresetFile() {
        this.mPresetFileProcessor.start();
    }

    public void setDownloadPath(int resourceType, String path) {
        synchronized (this.mPathArray) {
            if (!TextUtils.isEmpty(path) && !path.endsWith("/")) {
                path = path + "/";
            }
            this.mPathArray.put(resourceType, path);
        }
    }

    public ResourceInfo addNewResource(AddResourceInfo addResourceInfo) {
        try {
            String targetPath = generateTargetPath(addResourceInfo);
            createAndSetPermission(targetPath);
            LogUtil.log(1, TAG, "addNewResource targetPath:" + targetPath);
            File file = new File(addResourceInfo.rsc_path);
            FileUtils.unZip(file, targetPath);
            FileUtils.setOperationResourcePermission(targetPath);
            ResourceInfo resourceInfo = generateResourceInfo(addResourceInfo, targetPath);
            saveFileInfoToDB(resourceInfo);
            return resourceInfo;
        } catch (Exception e) {
            LogUtil.log(4, TAG, "addNewResource error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void createAndSetPermission(String targetPath) {
        File targetDir = new File(targetPath);
        File targetDirParent = targetDir.getParentFile();
        if (!targetDirParent.exists()) {
            targetDir.mkdirs();
            FileUtils.setOperationResourcePermission(targetDirParent.getPath());
        }
    }

    public void deleteOperationResource(ResourceInfo resourceInfo) {
        String targetPath = resourceInfo.targetPath;
        boolean success = FileUtils.deleteFile(targetPath);
        if (success) {
            deleteInfoFromDB(resourceInfo);
        }
    }

    public void selectOperationResource(int type, String resourceId) {
        SelectedInfo selectedInfo = new SelectedInfo(type, resourceId);
        ResourceRepository.getInstance(ActivityThread.currentActivityThread().getApplication()).setSelectedInfo(selectedInfo);
    }

    public void unselectOperationResource(int type, String resourceId) {
        SelectedInfo selectedInfo = new SelectedInfo(type, resourceId);
        ResourceRepository.getInstance(ActivityThread.currentActivityThread().getApplication()).deleteSelectInfo(selectedInfo);
    }

    public List<OperationResource> getRemoteResources(int type) {
        return this.mStoreProviderManagerWarpper.getRemoteResourceList(type);
    }

    public List<DownloadStatusInfo> getDownloadStatusList(int type) {
        return this.mStoreProviderManagerWarpper.getDownloadInfoList(type);
    }

    public void downloadOperationResource(OperationResource resource, boolean useSystemTraffic, PendingIntent pendingIntent) {
        this.mStoreProviderManagerWarpper.downloadOperationResource(resource, useSystemTraffic, pendingIntent);
    }

    public void cancelDownloadResource(OperationResource resource) {
        this.mStoreProviderManagerWarpper.cancelDownloadResource(resource);
    }

    public void pauseDownloadResource(OperationResource resource) {
        this.mStoreProviderManagerWarpper.pauseDownloadResource(resource);
    }

    public void resumeDownloadResource(OperationResource resource) {
        this.mStoreProviderManagerWarpper.resumeDownloadResource(resource);
    }

    public ResourceInfo getSelectedResourceByType(int resourceType) {
        return ResourceRepository.getInstance(ActivityThread.currentActivityThread().getApplication()).querySelectedResourceInfo(resourceType);
    }

    public List<ResourceInfo> getLocalResourceByType(int resourceType) {
        return ResourceRepository.getInstance(ActivityThread.currentActivityThread().getApplication()).queryLocalResourceInfo(resourceType);
    }

    public List<ResourceInfo> getDownloadResourceByType(int resourceType) {
        return ResourceRepository.getInstance(ActivityThread.currentActivityThread().getApplication()).queryDownloadResourceInfo(resourceType);
    }

    public List<ResourceInfo> getAllResources() {
        return ResourceRepository.getInstance(ActivityThread.currentActivityThread().getApplication()).queryAllResourceInfo();
    }

    public List<ResourceInfo> getResourcesByType(int resourceType) {
        return ResourceRepository.getInstance(ActivityThread.currentActivityThread().getApplication()).queryResourceInfo(resourceType);
    }

    public void updateResourceInfo(String resourceId, int newStatus) {
        ResourceRepository.getInstance(ActivityThread.currentActivityThread().getApplication()).updateResourceInfoStatus(resourceId, newStatus);
    }

    public ResourceInfo getResourceInfoById(String resourceId) {
        return ResourceRepository.getInstance(ActivityThread.currentActivityThread().getApplication()).getResourceInfoById(resourceId);
    }

    private ResourceInfo generateResourceInfo(AddResourceInfo addResourceInfo, String targetPath) {
        ResourceInfo resourceInfo = new ResourceInfo();
        resourceInfo.resourceId = addResourceInfo.id;
        resourceInfo.resourceType = addResourceInfo.type;
        resourceInfo.resourceName = addResourceInfo.rsc_name;
        resourceInfo.targetPath = targetPath;
        resourceInfo.resourceFrom = addResourceInfo.sourceType;
        resourceInfo.expireTime = addResourceInfo.expireTime;
        resourceInfo.effectTime = addResourceInfo.effectTime;
        resourceInfo.createTime = addResourceInfo.createTime;
        resourceInfo.updateTime = addResourceInfo.updateTime;
        resourceInfo.resourceIcon = addResourceInfo.resourceIcon;
        resourceInfo.description = addResourceInfo.description;
        resourceInfo.price = addResourceInfo.price;
        resourceInfo.extraInfo = DataBuild.appendMetaData(addResourceInfo.extraData, parserOperationDesFile(targetPath), ResourceConvert.getLegacyContent(targetPath));
        return resourceInfo;
    }

    private String generateTargetPath(AddResourceInfo addResourceInfo) {
        String targetFolder = this.mPathArray.get(addResourceInfo.type);
        if (TextUtils.isEmpty(targetFolder)) {
            targetFolder = OperationConstants.getPathByType(addResourceInfo.type);
        }
        return targetFolder + addResourceInfo.id + "/";
    }

    private String parserOperationDesFile(String targetPath) {
        String meteFile = targetPath + "description.json";
        String meteData = FileUtils.readFromFile(meteFile);
        return meteData;
    }

    private void processFile(String rscPath, ResourceInfo resourceInfo) {
        if (resourceInfo != null) {
            FileUtils.copyFile(rscPath, resourceInfo.targetPath);
        }
        saveFileInfoToDB(resourceInfo);
    }

    public void saveFileInfoToDB(ResourceInfo resourceInfo) {
        ResourceRepository.getInstance(ActivityThread.currentActivityThread().getApplication()).saveResourceInfo(resourceInfo);
    }

    public void deleteInfoFromDB(ResourceInfo resourceInfo) {
        ResourceRepository.getInstance(ActivityThread.currentActivityThread().getApplication()).deleteResource(resourceInfo);
    }

    private String generateFolderPath(String zipPath, String zipName) {
        return zipPath + File.separator + zipName;
    }
}
