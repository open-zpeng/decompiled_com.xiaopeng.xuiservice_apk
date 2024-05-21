package com.xiaopeng.xuiservice.xapp.mode.octopus.bean;
/* loaded from: classes5.dex */
public class KeyViewBasicInfo {
    private String desc;
    private int height;
    private int keyCode;
    private int resId;
    private int width;

    public KeyViewBasicInfo(int keyCode, int resId, String desc, int width, int height) {
        this.keyCode = keyCode;
        this.resId = resId;
        this.desc = desc;
        this.width = width;
        this.height = height;
    }

    public int getKeyCode() {
        return this.keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getResId() {
        return this.resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
