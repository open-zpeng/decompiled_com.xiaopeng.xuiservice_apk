package com.xiaopeng.xuiservice.xapp.miniprog.bean;

import com.google.gson.annotations.SerializedName;
import com.xiaopeng.speech.speechwidget.SpeechWidget;
import java.util.List;
/* loaded from: classes5.dex */
public class MiniGroupBean {
    @SerializedName(SpeechWidget.WIDGET_TITLE)
    private String mGroupName;
    @SerializedName("id")
    private String mId;
    @SerializedName("list")
    private List<MiniBeanContainer> mMiniBeanContainers;

    public List<MiniBeanContainer> getMiniBeanContainers() {
        return this.mMiniBeanContainers;
    }

    public void setMiniBeanContainers(List<MiniBeanContainer> miniBeanContainers) {
        this.mMiniBeanContainers = miniBeanContainers;
    }

    public String getGroupName() {
        return this.mGroupName;
    }

    public void setGroupName(String groupName) {
        this.mGroupName = groupName;
    }

    public String getId() {
        return this.mId;
    }

    public void setId(String id) {
        this.mId = id;
    }
}
