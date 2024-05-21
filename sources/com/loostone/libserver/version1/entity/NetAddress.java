package com.loostone.libserver.version1.entity;

import androidx.annotation.NonNull;
/* loaded from: classes4.dex */
public class NetAddress {
    private static final String TAG = "NetAddress";
    private String appUpdate;
    private String cacheRefresh;
    private String communicationKernel;
    private String karaokeVoiceApp;
    private String karaokeVolume;
    private String micCommInfo;
    private String micLimitInfo;
    private String statisticalMicInfo;

    public String getAppUpdate() {
        return this.appUpdate;
    }

    public String getCacheRefresh() {
        return this.cacheRefresh;
    }

    public String getCommunicationKernel() {
        return this.communicationKernel;
    }

    public String getKaraokeVoiceApp() {
        return this.karaokeVoiceApp;
    }

    public String getKaraokeVolume() {
        return this.karaokeVolume;
    }

    public String getMicCommInfo() {
        return this.micCommInfo;
    }

    public String getMicLimitInfo() {
        return this.micLimitInfo;
    }

    public String getStatisticalMicInfo() {
        return this.statisticalMicInfo;
    }

    public void setAppUpdate(String str) {
        this.appUpdate = str;
    }

    public void setCacheRefresh(String str) {
        this.cacheRefresh = str;
    }

    public void setCommunicationKernel(String str) {
        this.communicationKernel = str;
    }

    public void setKaraokeVoiceApp(String str) {
        this.karaokeVoiceApp = str;
    }

    public void setKaraokeVolume(String str) {
        this.karaokeVolume = str;
    }

    public void setMicCommInfo(String str) {
        this.micCommInfo = str;
    }

    public void setMicLimitInfo(String str) {
        this.micLimitInfo = str;
    }

    public void setStatisticalMicInfo(String str) {
        this.statisticalMicInfo = str;
    }

    @NonNull
    public String toString() {
        return "NetAddress{appUpdate='" + this.appUpdate + "', micLimitInfo='" + this.micLimitInfo + "', statisticalMicInfo='" + this.statisticalMicInfo + "', micCommInfo='" + this.micCommInfo + "', cacheRefresh='" + this.cacheRefresh + "', communicationKernel='" + this.communicationKernel + "', karaokeVoiceApp='" + this.karaokeVoiceApp + "', karaokeVolume='" + this.karaokeVolume + "'}";
    }
}
