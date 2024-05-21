package com.loostone.libserver.version1.entity.request.RefreshCache;

import com.loostone.libserver.version1.entity.request.BaseRequest;
/* loaded from: classes4.dex */
public class RefreshCacheInfo extends BaseRequest {
    private String brand;
    private String customer;
    private String lastUuid;
    private String mac;
    private String manufacture;
    private String model;
    private String packageName;

    public String getBrand() {
        return this.brand;
    }

    public String getCustomer() {
        return this.customer;
    }

    public String getLastUuid() {
        return this.lastUuid;
    }

    @Override // com.loostone.libserver.version1.entity.request.BaseRequest
    public String getMac() {
        return this.mac;
    }

    public String getManufacture() {
        return this.manufacture;
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

    public void setCustomer(String str) {
        this.customer = str;
    }

    public void setLastUuid(String str) {
        this.lastUuid = str;
    }

    @Override // com.loostone.libserver.version1.entity.request.BaseRequest
    public void setMac(String str) {
        this.mac = str;
    }

    public void setManufacture(String str) {
        this.manufacture = str;
    }

    public void setModel(String str) {
        this.model = str;
    }

    public void setPackageName(String str) {
        this.packageName = str;
    }
}
