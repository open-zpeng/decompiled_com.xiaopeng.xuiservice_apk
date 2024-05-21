package com.xiaopeng.xuiservice.operation.resource;
/* loaded from: classes5.dex */
public class AddResourceInfo {
    public long createTime;
    public String description;
    public long effectTime;
    public long expireTime;
    public String extraData;
    public String id;
    public String price;
    public String resourceIcon;
    public String rsc_name;
    public String rsc_path;
    public String sourceType;
    public int type;
    public long updateTime;

    public String toString() {
        return "AddResourceInfo{id='" + this.id + "', type=" + this.type + ", rsc_name='" + this.rsc_name + "', rsc_path='" + this.rsc_path + "', extraData='" + this.extraData + "', sourceType='" + this.sourceType + "', effectTime=" + this.effectTime + ", expireTime=" + this.expireTime + ", createTime=" + this.createTime + ", updateTime=" + this.updateTime + ", resourceIcon='" + this.resourceIcon + "', description='" + this.description + "', price='" + this.price + "'}";
    }
}
