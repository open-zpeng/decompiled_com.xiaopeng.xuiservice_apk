package com.loostone.libserver.version1.entity.request.statistical;

import com.loostone.libserver.version1.entity.request.BaseRequest;
/* loaded from: classes4.dex */
public class RequestStatisticalMic extends BaseRequest {
    private String brand;
    private String deviceName;
    private String model;
    private String packageName;
    private String pid;
    private String serial;
    private String vendor;
    private String vid;

    public String getBrand() {
        return this.brand;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String getModel() {
        return this.model;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public String getPid() {
        return this.pid;
    }

    public String getSerial() {
        return this.serial;
    }

    public String getVendor() {
        return this.vendor;
    }

    public String getVid() {
        return this.vid;
    }

    public void setBrand(String str) {
        this.brand = str;
    }

    public void setDeviceName(String str) {
        this.deviceName = str;
    }

    public void setModel(String str) {
        this.model = str;
    }

    public void setPackageName(String str) {
        this.packageName = str;
    }

    public void setPid(String str) {
        this.pid = str;
    }

    public void setSerial(String str) {
        this.serial = str;
    }

    public void setVendor(String str) {
        this.vendor = str;
    }

    public void setVid(String str) {
        this.vid = str;
    }
}
