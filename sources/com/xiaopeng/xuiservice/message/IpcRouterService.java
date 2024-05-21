package com.xiaopeng.xuiservice.message;

import com.xiaopeng.lib.apirouter.server.ApplicationId;
import com.xiaopeng.lib.apirouter.server.IServicePublisher;
import com.xiaopeng.lib.apirouter.server.Publish;
@ApplicationId("com.xiaopeng.xuiservice")
/* loaded from: classes5.dex */
public class IpcRouterService implements IServicePublisher {
    @Publish
    public void onReceiverData(String id, String bundle) {
        PushMessage.getInstance().dispatchCloudMessage(bundle);
    }
}
