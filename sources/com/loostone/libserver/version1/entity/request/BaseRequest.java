package com.loostone.libserver.version1.entity.request;

import java.io.Serializable;
/* loaded from: classes4.dex */
public class BaseRequest implements Serializable {
    public String _time;
    private String mac;
    private String uuid;

    public String getMac() {
        return this.mac;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String get_time() {
        return this._time;
    }

    public void setMac(String str) {
        this.mac = str;
    }

    public void setUuid(String str) {
        this.uuid = str;
    }

    public void set_time(String str) {
        this._time = str;
    }
}
