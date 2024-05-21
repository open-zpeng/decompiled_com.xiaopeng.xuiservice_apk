package com.xiaopeng.xuiservice.xapp.miniprog.bean;

import com.google.gson.annotations.SerializedName;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
/* loaded from: classes5.dex */
public class MiniBean {
    @SerializedName(SpeechConstants.KEY_ALI_ID)
    private String aliId;
    @SerializedName("alipay_version")
    private String alipayVersion;
    @SerializedName("id")
    private String id;
    @SerializedName("logo")
    private String logo;
    @SerializedName("name")
    private String name;
    @SerializedName("param")
    private String param;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAliId() {
        return this.aliId;
    }

    public void setAliId(String aliId) {
        this.aliId = aliId;
    }

    public String getAlipayVersion() {
        return this.alipayVersion;
    }

    public void setAlipayVersion(String alipayVersion) {
        this.alipayVersion = alipayVersion;
    }

    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getParam() {
        return this.param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String toString() {
        return "MiniBean{id='" + this.id + "', name='" + this.name + "', aliId='" + this.aliId + "', alipayVersion='" + this.alipayVersion + "', logo='" + this.logo + "', param='" + this.param + "'}";
    }
}
