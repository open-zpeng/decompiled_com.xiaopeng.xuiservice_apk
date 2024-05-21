package com.xiaopeng.xuiservice.uvccamera.uvcapp;
/* loaded from: classes5.dex */
class ImageRawData {
    private byte[] mData;
    private int mHeight;
    private int mWidth;

    public ImageRawData(byte[] data, int width, int height) {
        this.mData = data;
        this.mWidth = width;
        this.mHeight = height;
    }

    public byte[] getData() {
        return this.mData;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }
}
