package com.xiaopeng.xuiservice.xapp.miniprog.bean;

import com.google.gson.annotations.SerializedName;
/* loaded from: classes5.dex */
public class MiniBeanContainer {
    @SerializedName("content_type")
    private int contentType;
    @SerializedName("data")
    private MiniBean mMiniBean;

    public MiniBean getMiniBean() {
        return this.mMiniBean;
    }

    public void setMiniBean(MiniBean miniBean) {
        this.mMiniBean = miniBean;
    }

    public int getContentType() {
        return this.contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }
}
