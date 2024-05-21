package com.xiaopeng.appstore.storeprovider.store;

import com.xiaopeng.appstore.storeprovider.store.bean.ResourceDownloadInfo;
/* loaded from: classes4.dex */
public interface RMDownloadListener {
    void onDownloadCallback(int type, ResourceDownloadInfo resourceDownloadInfo);

    void onMenuOpenCallback(String resourceId);

    void unbindService();
}
