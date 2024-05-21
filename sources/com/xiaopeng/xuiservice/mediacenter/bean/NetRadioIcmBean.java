package com.xiaopeng.xuiservice.mediacenter.bean;
/* loaded from: classes5.dex */
public class NetRadioIcmBean {
    private String enable = "1";
    private String msgtype = "0";
    private NetRadioBean net_radio;
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

    public NetRadioBean getNetRadio() {
        return this.net_radio;
    }

    public void setNetRadio(NetRadioBean net_radio) {
        this.net_radio = net_radio;
    }
}
