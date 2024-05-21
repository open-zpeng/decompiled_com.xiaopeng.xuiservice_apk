package com.xiaopeng.xuiservice.operation.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
@Entity(primaryKeys = {"resourceType"})
/* loaded from: classes5.dex */
public class SelectedInfo {
    @NonNull
    @ColumnInfo(name = "resourceId")
    public String resourceId;
    @NonNull
    @ColumnInfo(name = "resourceType")
    public int resourceType;

    public SelectedInfo() {
    }

    public SelectedInfo(int type, String id) {
        this.resourceType = type;
        this.resourceId = id;
    }

    public String toString() {
        return "SelectedInfo{resourceType=" + this.resourceType + ", resourceId='" + this.resourceId + "'}";
    }
}
