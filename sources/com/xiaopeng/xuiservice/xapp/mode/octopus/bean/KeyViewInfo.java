package com.xiaopeng.xuiservice.xapp.mode.octopus.bean;

import android.graphics.Point;
/* loaded from: classes5.dex */
public class KeyViewInfo {
    public int mHeight;
    public int mKeyCode;
    public String mKeyCodeIndexName;
    public int mKeyImgResourceId;
    public Point mPoint;
    public int mWidth;

    public KeyViewInfo(int keyCode, String keyCodeIndexName, int resId, Point point, int width, int height) {
        this.mKeyCode = keyCode;
        this.mKeyCodeIndexName = keyCodeIndexName;
        this.mKeyImgResourceId = resId;
        this.mPoint = point;
        this.mWidth = width;
        this.mHeight = height;
    }

    public String toString() {
        return "KeyInfo{mKeyCode=" + this.mKeyCode + ", mKeyCodeIndexName='" + this.mKeyCodeIndexName + "', mKeyImgResourceId=" + this.mKeyImgResourceId + ", mPoint=" + this.mPoint + ", mWidth=" + this.mWidth + ", mHeight=" + this.mHeight + '}';
    }
}
