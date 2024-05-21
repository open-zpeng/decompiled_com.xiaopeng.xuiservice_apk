package com.loostone.libserver.version1.entity.response.AppUpdate;
/* loaded from: classes4.dex */
public class AppUpdate {
    private int needUpdate;
    private String url;
    private int versionCode;
    private String versionContent;
    private String versionName;

    public int getNeedUpdate() {
        return this.needUpdate;
    }

    public String getUrl() {
        return this.url;
    }

    public int getVersionCode() {
        return this.versionCode;
    }

    public String getVersionContent() {
        return this.versionContent;
    }

    public String getVersionName() {
        return this.versionName;
    }

    public void setNeedUpdate(int i) {
        this.needUpdate = i;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public void setVersionCode(int i) {
        this.versionCode = i;
    }

    public void setVersionContent(String str) {
        this.versionContent = str;
    }

    public void setVersionName(String str) {
        this.versionName = str;
    }
}
