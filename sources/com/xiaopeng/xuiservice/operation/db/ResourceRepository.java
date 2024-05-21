package com.xiaopeng.xuiservice.operation.db;

import android.content.Context;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.util.List;
/* loaded from: classes5.dex */
public class ResourceRepository {
    private static final String TAG = "ResourceRepository";
    private static volatile ResourceRepository mInstance;
    private final ResourceDao mResourceDao;
    private final SelectedDao mSelectedDao;

    private ResourceRepository(Context context) {
        ResourceDatabase resourceDatabase = ResourceDatabase.getDatabase(context);
        this.mResourceDao = resourceDatabase.resourceDao();
        this.mSelectedDao = resourceDatabase.selectedDao();
    }

    public static ResourceRepository getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ResourceRepository.class) {
                if (mInstance == null) {
                    mInstance = new ResourceRepository(context);
                }
            }
        }
        return mInstance;
    }

    public List<ResourceInfo> queryAllResourceInfo() {
        return this.mResourceDao.queryAllResource();
    }

    public List<ResourceInfo> queryResourceInfo(int type) {
        return this.mResourceDao.queryResourceByType(type);
    }

    public void updateResourceInfoStatus(String resourceId, int status) {
        this.mResourceDao.updateResourceStatus(resourceId, status);
    }

    public List<ResourceInfo> queryLocalResourceInfo(int type) {
        return this.mResourceDao.queryResourceByTypeAndFrom(type, "preset_resource");
    }

    public List<ResourceInfo> queryDownloadResourceInfo(int type) {
        return this.mResourceDao.queryResourceByTypeAndFrom(type, "download_resource");
    }

    public void saveResourceInfo(ResourceInfo info) {
        if (info != null) {
            LogUtil.d(TAG, "saveResourceInfo");
            this.mResourceDao.insertResource(info);
        }
    }

    public void saveResourceInfo(List<ResourceInfo> info) {
        if (info != null && !info.isEmpty()) {
            ResourceInfo[] array = (ResourceInfo[]) info.toArray(new ResourceInfo[info.size()]);
            this.mResourceDao.insertResources(array);
        }
    }

    public void deleteResource(List<ResourceInfo> info) {
        if (info != null && !info.isEmpty()) {
            ResourceInfo[] array = (ResourceInfo[]) info.toArray(new ResourceInfo[info.size()]);
            this.mResourceDao.deleteResources(array);
        }
    }

    public void deleteResource(ResourceInfo info) {
        if (info != null) {
            this.mResourceDao.deleteResource(info);
        }
    }

    public ResourceInfo querySelectedResourceInfo(int type) {
        SelectedInfo info = this.mSelectedDao.querySelectedByType(type);
        if (info != null) {
            return this.mResourceDao.queryResourceById(info.resourceId);
        }
        return null;
    }

    public ResourceInfo getResourceInfoById(String id) {
        return this.mResourceDao.queryResourceById(id);
    }

    public void setSelectedInfo(SelectedInfo info) {
        this.mSelectedDao.insertSelectInfo(info);
    }

    public void deleteSelectInfo(SelectedInfo info) {
        this.mSelectedDao.deleteSelectedInfo(info);
    }
}
