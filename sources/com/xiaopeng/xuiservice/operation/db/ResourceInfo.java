package com.xiaopeng.xuiservice.operation.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
@Entity(primaryKeys = {"resourceId"})
/* loaded from: classes5.dex */
public class ResourceInfo {
    @ColumnInfo(name = "createTime")
    public long createTime;
    @ColumnInfo(name = "description")
    public String description;
    @ColumnInfo(name = "effectTime")
    public long effectTime;
    @ColumnInfo(name = "expireTime")
    public long expireTime;
    @ColumnInfo(name = "extraInfo")
    public String extraInfo;
    @ColumnInfo(name = "md5Hash")
    public String md5Hash;
    @ColumnInfo(name = "price")
    public String price;
    @ColumnInfo(name = "resourceFrom")
    public String resourceFrom;
    @ColumnInfo(name = "resourceIcon")
    public String resourceIcon;
    @NonNull
    @ColumnInfo(name = "resourceId")
    public String resourceId;
    @NonNull
    @ColumnInfo(name = "resourceName")
    public String resourceName;
    @NonNull
    @ColumnInfo(name = "resourceType")
    public int resourceType;
    @ColumnInfo(name = "status")
    public int status;
    @ColumnInfo(name = "targetPath")
    public String targetPath;
    @ColumnInfo(name = "updateTime")
    public long updateTime;

    public String toString() {
        return "ResourceInfo{resourceId='" + this.resourceId + "', resourceType=" + this.resourceType + ", resourceName='" + this.resourceName + "', targetPath='" + this.targetPath + "', md5Hash='" + this.md5Hash + "', effectTime=" + this.effectTime + ", expireTime=" + this.expireTime + ", resourceFrom='" + this.resourceFrom + "', extraInfo='" + this.extraInfo + "', status=" + this.status + ", createTime=" + this.createTime + ", updateTime=" + this.updateTime + ", resourceIcon='" + this.resourceIcon + "', description='" + this.description + "', price='" + this.price + "'}";
    }
}
