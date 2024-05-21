package com.xiaopeng.xuiservice.uvccamera.usb;

import androidx.annotation.NonNull;
/* loaded from: classes5.dex */
public class UVCParam implements Cloneable {
    private Size previewSize;
    private int quirks;

    public UVCParam() {
    }

    public UVCParam(Size previewSize, int quirks) {
        this.previewSize = previewSize;
        this.quirks = quirks;
    }

    public Size getPreviewSize() {
        return this.previewSize;
    }

    public void setPreviewSize(Size previewSize) {
        this.previewSize = previewSize;
    }

    public int getQuirks() {
        return this.quirks;
    }

    public void setQuirks(int quirks) {
        this.quirks = quirks;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @NonNull
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return new UVCParam(this.previewSize, this.quirks);
        }
    }
}
