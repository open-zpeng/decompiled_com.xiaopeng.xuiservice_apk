package com.xiaopeng.xuiservice.operation.legacy;

import com.xiaopeng.xuiservice.operation.db.ResourceInfo;
import com.xiaopeng.xuiservice.operation.resource.ResourceManager;
import java.util.List;
/* loaded from: classes5.dex */
public class LegacyResourceManager {
    private static final String TAG = "LegacyResourceManager";

    /* loaded from: classes5.dex */
    private static class ResourceManagerHolder {
        private static final LegacyResourceManager sInstance = new LegacyResourceManager();

        private ResourceManagerHolder() {
        }
    }

    private LegacyResourceManager() {
    }

    public static LegacyResourceManager getInstance() {
        return ResourceManagerHolder.sInstance;
    }

    public String getLocalResourceList(int type) {
        List<ResourceInfo> result = ResourceManager.getInstance().getLocalResourceByType(type);
        return ResourceConvert.toLegacyInfo(result);
    }

    public String getDownLoadResourceList(int type) {
        List<ResourceInfo> result = ResourceManager.getInstance().getDownloadResourceByType(type);
        return ResourceConvert.toLegacyInfo(result);
    }
}
