package com.loostone.libserver.version1.entity.request.AppUpdate;

import com.loostone.libserver.version1.entity.request.BaseRequest;
/* loaded from: classes4.dex */
public class RequestAppUpdate extends BaseRequest {
    private String channel;
    private String packageName;
    private int version_code;

    public String getChannel() {
        return this.channel;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public int getVersion_code() {
        return this.version_code;
    }

    public void setChannel(String str) {
        this.channel = str;
    }

    public void setPackageName(String str) {
        this.packageName = str;
    }

    public void setVersion_code(int i) {
        this.version_code = i;
    }
}
