package com.loostone.libserver.version1.entity.request.mic;

import com.loostone.libserver.version1.entity.request.BaseRequest;
/* loaded from: classes4.dex */
public class MicCommInfo extends BaseRequest {
    private String brand;
    private String customer;
    private String manufacture;
    private String model;
    private String pid;
    private String vid;

    public String getBrand() {
        return this.brand;
    }

    public String getCustomer() {
        return this.customer;
    }

    public String getManufacture() {
        return this.manufacture;
    }

    public String getModel() {
        return this.model;
    }

    public String getPid() {
        return this.pid;
    }

    public String getVid() {
        return this.vid;
    }

    public void setBrand(String str) {
        this.brand = str;
    }

    public void setCustomer(String str) {
        this.customer = str;
    }

    public void setManufacture(String str) {
        this.manufacture = str;
    }

    public void setModel(String str) {
        this.model = str;
    }

    public void setPid(String str) {
        this.pid = str;
    }

    public void setVid(String str) {
        this.vid = str;
    }
}
