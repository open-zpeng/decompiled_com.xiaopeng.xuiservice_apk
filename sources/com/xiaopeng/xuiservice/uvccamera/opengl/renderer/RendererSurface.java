package com.xiaopeng.xuiservice.uvccamera.opengl.renderer;

import android.opengl.GLES20;
import android.opengl.Matrix;
import com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase;
import com.xiaopeng.xuiservice.uvccamera.opengl.GLDrawer2D;
import com.xiaopeng.xuiservice.uvccamera.utils.Time;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes5.dex */
public class RendererSurface {
    private EGLBase.IEglSurface mEGLSurface;
    protected volatile boolean mEnable;
    final float[] mMvpMatrix;
    private Object mSurface;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static RendererSurface newInstance(EGLBase egl, Object surface, int maxFps) {
        if (maxFps > 0) {
            return new RendererSurfaceHasWait(egl, surface, maxFps);
        }
        return new RendererSurface(egl, surface);
    }

    private RendererSurface(EGLBase egl, Object surface) {
        this.mMvpMatrix = new float[16];
        this.mEnable = true;
        this.mSurface = surface;
        this.mEGLSurface = egl.createFromSurface(surface);
        Matrix.setIdentityM(this.mMvpMatrix, 0);
    }

    public void release() {
        EGLBase.IEglSurface iEglSurface = this.mEGLSurface;
        if (iEglSurface != null) {
            iEglSurface.release();
            this.mEGLSurface = null;
        }
        this.mSurface = null;
    }

    public boolean isValid() {
        EGLBase.IEglSurface iEglSurface = this.mEGLSurface;
        return iEglSurface != null && iEglSurface.isValid();
    }

    private void check() throws IllegalStateException {
        if (this.mEGLSurface == null) {
            throw new IllegalStateException("already released");
        }
    }

    public boolean isEnable() {
        return this.mEnable;
    }

    public void setEnable(boolean enable) {
        this.mEnable = enable;
    }

    public boolean canDraw() {
        return this.mEnable;
    }

    public void draw(GLDrawer2D drawer, int textId, float[] texMatrix) {
        draw(drawer, textId, texMatrix, this.mMvpMatrix);
    }

    public void draw(GLDrawer2D drawer, int textId, float[] texMatrix, float[] mvpMatrix) {
        EGLBase.IEglSurface iEglSurface;
        if (drawer != null && (iEglSurface = this.mEGLSurface) != null) {
            iEglSurface.makeCurrent();
            GLES20.glClear(16384);
            drawer.setMvpMatrix(mvpMatrix, 0);
            drawer.draw(textId, texMatrix, 0);
            this.mEGLSurface.swap();
        }
    }

    public void clear(int color) {
        EGLBase.IEglSurface iEglSurface = this.mEGLSurface;
        if (iEglSurface != null) {
            iEglSurface.makeCurrent();
            GLES20.glClearColor(((16711680 & color) >>> 16) / 255.0f, ((65280 & color) >>> 8) / 255.0f, (color & 255) / 255.0f, (((-16777216) & color) >>> 24) / 255.0f);
            GLES20.glClear(16384);
            this.mEGLSurface.swap();
        }
    }

    /* loaded from: classes5.dex */
    private static class RendererSurfaceHasWait extends RendererSurface {
        private final long mIntervalsNs;
        private long mNextDraw;

        private RendererSurfaceHasWait(EGLBase egl, Object surface, int maxFps) {
            super(egl, surface);
            this.mIntervalsNs = 1000000000 / maxFps;
            this.mNextDraw = Time.nanoTime() + this.mIntervalsNs;
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.opengl.renderer.RendererSurface
        public boolean canDraw() {
            return this.mEnable && Time.nanoTime() - this.mNextDraw > 0;
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.opengl.renderer.RendererSurface
        public void draw(GLDrawer2D drawer, int textId, float[] texMatrix) {
            this.mNextDraw = Time.nanoTime() + this.mIntervalsNs;
            super.draw(drawer, textId, texMatrix);
        }
    }
}
