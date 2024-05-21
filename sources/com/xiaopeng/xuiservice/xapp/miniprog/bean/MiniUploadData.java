package com.xiaopeng.xuiservice.xapp.miniprog.bean;
/* loaded from: classes5.dex */
public class MiniUploadData {
    String device_data;
    String user_key;
    String user_route;

    public MiniUploadData(String userKey, String userRoute, String deviceData) {
        this.user_key = userKey;
        this.user_route = userRoute;
        this.device_data = deviceData;
    }
}
