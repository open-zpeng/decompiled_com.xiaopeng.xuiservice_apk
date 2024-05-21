package com.loostone.libserver.version1.entity.request.mic;

import com.loostone.libserver.version1.entity.request.BaseRequest;
/* loaded from: classes4.dex */
public class MicHandShake extends BaseRequest {
    private String data;
    private int state;
    private int type;

    public String getData() {
        return this.data;
    }

    public int getState() {
        return this.state;
    }

    public int getType() {
        return this.type;
    }

    public void setData(String str) {
        this.data = str;
    }

    public void setState(int i) {
        this.state = i;
    }

    public void setType(int i) {
        this.type = i;
    }
}
