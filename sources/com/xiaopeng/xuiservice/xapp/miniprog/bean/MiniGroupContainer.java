package com.xiaopeng.xuiservice.xapp.miniprog.bean;

import com.google.gson.annotations.SerializedName;
/* loaded from: classes5.dex */
public class MiniGroupContainer {
    @SerializedName("content_type")
    private int contentType;
    @SerializedName("data")
    private MiniGroupBean mMiniGroupBean;

    public MiniGroupBean getMiniGroupBean() {
        return this.mMiniGroupBean;
    }

    public void setMiniGroupBean(MiniGroupBean miniGroupBean) {
        this.mMiniGroupBean = miniGroupBean;
    }

    public int getContentType() {
        return this.contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }
}
