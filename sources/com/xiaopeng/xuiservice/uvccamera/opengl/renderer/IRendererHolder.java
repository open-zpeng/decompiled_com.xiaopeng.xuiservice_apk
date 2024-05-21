package com.xiaopeng.xuiservice.uvccamera.opengl.renderer;

import android.graphics.SurfaceTexture;
import android.view.Surface;
/* loaded from: classes5.dex */
public interface IRendererHolder {
    void addSlaveSurface(int i, Object obj, boolean z) throws IllegalStateException, IllegalArgumentException;

    void addSlaveSurface(int i, Object obj, boolean z, int i2) throws IllegalStateException, IllegalArgumentException;

    void checkPrimarySurface();

    void clearSlaveSurface(int i, int i2);

    void clearSlaveSurfaceAll(int i);

    int getMirrorMode();

    Surface getPrimarySurface();

    SurfaceTexture getPrimarySurfaceTexture();

    boolean isRunning();

    boolean isSlaveSurfaceEnable(int i);

    void release();

    void removeSlaveSurface(int i);

    void removeSlaveSurfaceAll();

    void requestFrame();

    void rotateBy(int i);

    void rotateTo(int i);

    void setMirrorMode(int i);

    void setSlaveSurfaceEnable(int i, boolean z);

    void updatePrimarySize(int i, int i2) throws IllegalStateException;
}
