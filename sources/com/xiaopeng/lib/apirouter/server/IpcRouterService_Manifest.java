package com.xiaopeng.lib.apirouter.server;

import java.util.HashSet;
import java.util.Set;
/* loaded from: classes.dex */
public class IpcRouterService_Manifest {
    public static final String DESCRIPTOR = "com.xiaopeng.xuiservice.IpcRouterService";
    public static final int TRANSACTION_onReceiverData = 0;

    public static String toJsonManifest() {
        return "{\"authority\":\"com.xiaopeng.xuiservice.IpcRouterService\",\"DESCRIPTOR\":\"com.xiaopeng.xuiservice.IpcRouterService\",\"TRANSACTION\":[{\"path\":\"onReceiverData\",\"METHOD\":\"onReceiverData\",\"ID\":0,\"parameter\":[{\"alias\":\"id\",\"name\":\"id\"},{\"alias\":\"bundle\",\"name\":\"bundle\"}]}]}";
    }

    public static Set<String> getKey() {
        Set<String> key = new HashSet<>(2);
        key.add("IpcRouterService");
        return key;
    }
}
