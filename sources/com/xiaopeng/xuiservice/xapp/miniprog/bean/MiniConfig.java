package com.xiaopeng.xuiservice.xapp.miniprog.bean;

import com.google.gson.annotations.SerializedName;
/* loaded from: classes5.dex */
public class MiniConfig {
    @SerializedName("launch_width")
    private int launchWidth;
    @SerializedName("page")
    private String page;
    @SerializedName("query")
    private String query;
    @SerializedName("save_to_recent")
    private boolean saveToRecent;
    @SerializedName("show_type")
    private int showType = 21;

    public String getPage() {
        return this.page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getQuery() {
        return this.query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getShowType() {
        return this.showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public boolean isSaveToRecent() {
        return this.saveToRecent;
    }

    public void setSaveToRecent(boolean saveToRecent) {
        this.saveToRecent = saveToRecent;
    }

    public int getLaunchWidth() {
        return this.launchWidth;
    }

    public void setLaunchWidth(int launchWidth) {
        this.launchWidth = launchWidth;
    }

    public String toString() {
        return "MiniConfig{showType=" + this.showType + ", saveToRecent=" + this.saveToRecent + ", launchWidth=" + this.launchWidth + '}';
    }
}
