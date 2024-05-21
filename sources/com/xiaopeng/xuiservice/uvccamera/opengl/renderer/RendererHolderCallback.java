package com.xiaopeng.xuiservice.uvccamera.opengl.renderer;

import android.view.Surface;
/* loaded from: classes5.dex */
public interface RendererHolderCallback {
    void onFrameAvailable();

    void onPrimarySurfaceCreate(Surface surface);

    void onPrimarySurfaceDestroy();
}
