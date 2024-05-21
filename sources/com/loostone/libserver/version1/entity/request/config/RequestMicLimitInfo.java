package com.loostone.libserver.version1.entity.request.config;

import OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o;
import com.loostone.libserver.version1.entity.MachineInfoConfig;
import com.loostone.libserver.version1.entity.request.BaseRequest;
/* loaded from: classes4.dex */
public class RequestMicLimitInfo extends BaseRequest {
    private String deviceName;
    private int hidResult;
    private String hidSn;
    private String hidVersion;
    private String pid;
    private String serial;
    private String vendor;
    private String vid;
    private String brand = MachineInfoConfig.getBrand();
    private String model = MachineInfoConfig.getModel();
    private String hardware = MachineInfoConfig.getHardWare();
    private String manufacturer = MachineInfoConfig.getManufacturer();
    private String device = MachineInfoConfig.getDevice();
    private String customer = OooO00o.f237OooO00o.OooO00o(OooO00o.OooO0O0.OooO0O0.OooO00o.OooO0O0);

    public String getBrand() {
        return this.brand;
    }

    public String getCustomer() {
        return this.customer;
    }

    public String getDevice() {
        return this.device;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String getHardware() {
        return this.hardware;
    }

    public int getHidResult() {
        return this.hidResult;
    }

    public String getHidSn() {
        return this.hidSn;
    }

    public String getHidVersion() {
        return this.hidVersion;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public String getModel() {
        return this.model;
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

    public void setCustomer(String str) {
        this.customer = str;
    }

    public void setDevice(String str) {
        this.device = str;
    }

    public void setDeviceName(String str) {
        this.deviceName = str;
    }

    public void setHardware(String str) {
        this.hardware = str;
    }

    public void setHidResult(int i) {
        this.hidResult = i;
    }

    public void setHidSn(String str) {
        this.hidSn = str;
    }

    public void setHidVersion(String str) {
        this.hidVersion = str;
    }

    public void setManufacturer(String str) {
        this.manufacturer = str;
    }

    public void setModel(String str) {
        this.model = str;
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

    public String toString() {
        return "RequestMicLimitInfo{hidResult=" + this.hidResult + ", serial='" + this.serial + "', hidSn='" + this.hidSn + "', hidVersion='" + this.hidVersion + "', vid='" + this.vid + "', pid='" + this.pid + "', vendor='" + this.vendor + "', deviceName='" + this.deviceName + "', brand='" + this.brand + "', model='" + this.model + "', hardware='" + this.hardware + "', manufacturer='" + this.manufacturer + "', device='" + this.device + "', customer='" + this.customer + "', _time='" + this._time + "'}";
    }
}
