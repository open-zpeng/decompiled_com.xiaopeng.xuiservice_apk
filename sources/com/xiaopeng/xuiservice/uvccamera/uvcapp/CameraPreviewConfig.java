package com.xiaopeng.xuiservice.uvccamera.uvcapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
/* loaded from: classes5.dex */
public class CameraPreviewConfig implements Cloneable {
    public static final int DEFAULT_MIRROR = 0;
    public static final int DEFAULT_ROTATION = 0;
    private static final String OPTION_MIRROR = "cameraPreview.mirror";
    private static final String OPTION_ROTATION = "cameraPreview.rotation";
    private Bundle mMutableConfig = new Bundle();

    Bundle getMutableConfig() {
        return this.mMutableConfig;
    }

    public CameraPreviewConfig setRotation(int rotation) {
        if (rotation == 0 || rotation == 90 || rotation == 180 || rotation == 270) {
            getMutableConfig().putInt(OPTION_ROTATION, rotation);
            return this;
        }
        throw new IllegalArgumentException("Invalid rotation=" + rotation);
    }

    public int getRotation() {
        return getMutableConfig().getInt(OPTION_ROTATION, 0);
    }

    public CameraPreviewConfig setMirror(int mirror) {
        getMutableConfig().putInt(OPTION_MIRROR, mirror);
        return this;
    }

    public int getMirror() {
        return getMutableConfig().getInt(OPTION_MIRROR, 0);
    }

    @NonNull
    protected Object clone() throws CloneNotSupportedException {
        CameraPreviewConfig obj = (CameraPreviewConfig) super.clone();
        obj.mMutableConfig = (Bundle) this.mMutableConfig.clone();
        return obj;
    }
}
