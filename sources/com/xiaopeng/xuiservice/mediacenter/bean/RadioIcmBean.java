package com.xiaopeng.xuiservice.mediacenter.bean;
/* loaded from: classes5.dex */
public class RadioIcmBean {
    private String enable = "1";
    private String msgtype = "0";
    private RadioBean radio;
    private String reporttime;

    public String getEnable() {
        return this.enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getMsgtype() {
        return this.msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getReporttime() {
        return this.reporttime;
    }

    public void setReporttime(String reporttime) {
        this.reporttime = reporttime;
    }

    public RadioBean getRadio() {
        return this.radio;
    }

    public void setRadio(RadioBean radio) {
        this.radio = radio;
    }
}
