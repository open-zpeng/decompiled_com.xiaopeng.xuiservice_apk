package com.loostone.libserver.version1.entity.request.machine;

import com.loostone.libserver.version1.entity.request.BaseRequest;
/* loaded from: classes4.dex */
public class RequestKaraokeApp extends BaseRequest {
    private String brand;
    private String channel;
    private String model;
    private String packageName;

    public String getBrand() {
        return this.brand;
    }

    public String getChannel() {
        return this.channel;
    }

    public String getModel() {
        return this.model;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setBrand(String str) {
        this.brand = str;
    }

    public void setChannel(String str) {
        this.channel = str;
    }

    public void setModel(String str) {
        this.model = str;
    }

    public void setPackageName(String str) {
        this.packageName = str;
    }
}
