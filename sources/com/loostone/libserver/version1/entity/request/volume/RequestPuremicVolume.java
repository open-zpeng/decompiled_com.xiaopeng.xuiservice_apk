package com.loostone.libserver.version1.entity.request.volume;

import com.loostone.libserver.version1.entity.request.BaseRequest;
/* loaded from: classes4.dex */
public class RequestPuremicVolume extends BaseRequest {
    private String brand;
    private String channel;
    private String device;
    private String hardware;
    private String manufacture;
    private String model;
    private String packageName;

    public String getBrand() {
        return this.brand;
    }

    public String getChannel() {
        return this.channel;
    }

    public String getDevice() {
        return this.device;
    }

    public String getHardware() {
        return this.hardware;
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

    public void setChannel(String str) {
        this.channel = str;
    }

    public void setDevice(String str) {
        this.device = str;
    }

    public void setHardware(String str) {
        this.hardware = str;
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
