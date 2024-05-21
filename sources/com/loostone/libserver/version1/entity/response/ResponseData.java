package com.loostone.libserver.version1.entity.response;
/* loaded from: classes4.dex */
public class ResponseData<T> {
    private T data;
    private String msg;
    private String state;
    private String uuid;

    public T getData() {
        return this.data;
    }

    public String getMsg() {
        return this.msg;
    }

    public String getState() {
        return this.state;
    }

    public String getUuid() {
        return this.uuid;
    }

    public boolean isSuccess() {
        return "000000".equals(this.state);
    }

    public void setData(T t) {
        this.data = t;
    }

    public void setMsg(String str) {
        this.msg = str;
    }

    public void setState(String str) {
        this.state = str;
    }

    public void setUuid(String str) {
        this.uuid = str;
    }
}
