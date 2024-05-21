package com.loostone.libserver.version1.entity.request.statistical;

import com.loostone.libserver.version1.entity.request.BaseRequest;
/* loaded from: classes4.dex */
public class RequestStatisticalPlayer extends BaseRequest {
    private String brand;
    private String model;
    private String packageName;
    private String romVersion;

    public String getBrand() {
        return this.brand;
    }

    public String getModel() {
        return this.model;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public String getRomVersion() {
        return this.romVersion;
    }

    public void setBrand(String str) {
        this.brand = str;
    }

    public void setModel(String str) {
        this.model = str;
    }

    public void setPackageName(String str) {
        this.packageName = str;
    }

    public void setRomVersion(String str) {
        this.romVersion = str;
    }
}
