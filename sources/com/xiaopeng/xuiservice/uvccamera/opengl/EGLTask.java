package com.xiaopeng.xuiservice.uvccamera.opengl;

import android.os.Looper;
import android.os.Process;
import androidx.annotation.Nullable;
import com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase;
/* loaded from: classes5.dex */
public abstract class EGLTask extends Thread {
    public static final int EGL_FLAG_DEPTH_BUFFER = 1;
    public static final int EGL_FLAG_RECORDABLE = 2;
    public static final int EGL_FLAG_STENCIL_1BIT = 4;
    public static final int EGL_FLAG_STENCIL_8BIT = 32;
    private static final String TAG = EGLTask.class.getSimpleName();
    private EGLBase mEgl;
    private EGLBase.IEglSurface mEglSurface;
    private Looper mLooper;

    public EGLTask(EGLBase.IContext sharedContext, int flags) {
        this(sharedContext, flags, 3);
    }

    public EGLTask(EGLBase.IContext sharedContext, int flags, int maxClientVersion) {
        this.mEgl = null;
        init(sharedContext, flags, maxClientVersion);
        start();
    }

    protected void init(EGLBase.IContext sharedContext, int flags, int maxClientVersion) {
        int stencilBits;
        if (sharedContext == null) {
            if ((flags & 4) == 4) {
                stencilBits = 1;
            } else {
                stencilBits = (flags & 32) == 32 ? 8 : 0;
            }
            this.mEgl = EGLBase.createFrom(sharedContext, maxClientVersion, (flags & 1) == 1, stencilBits, (flags & 2) == 2);
        }
    }

    private void initEglSurface() {
        EGLBase eGLBase = this.mEgl;
        if (eGLBase == null) {
            throw new RuntimeException("failed to create EglCore");
        }
        this.mEglSurface = eGLBase.createOffscreen(1, 1);
        this.mEglSurface.makeCurrent();
    }

    protected void onRelease() {
        try {
            this.mEglSurface.makeCurrent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.mEglSurface.release();
        this.mEgl.release();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public EGLBase getEgl() {
        return this.mEgl;
    }

    protected EGLBase.IContext getEGLContext() {
        return this.mEgl.getContext();
    }

    protected EGLBase.IConfig getConfig() {
        return this.mEgl.getConfig();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Nullable
    public EGLBase.IContext getContext() {
        EGLBase eGLBase = this.mEgl;
        if (eGLBase != null) {
            return eGLBase.getContext();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void makeCurrent() {
        this.mEglSurface.makeCurrent();
    }

    protected boolean isGLES3() {
        EGLBase eGLBase = this.mEgl;
        return eGLBase != null && eGLBase.getGlVersion() > 2;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        initEglSurface();
        Looper.prepare();
        synchronized (this) {
            this.mLooper = Looper.myLooper();
            notifyAll();
        }
        Process.setThreadPriority(-1);
        Looper.loop();
        onRelease();
    }

    public Looper getLooper() {
        if (!isAlive()) {
            return null;
        }
        synchronized (this) {
            while (isAlive() && this.mLooper == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return this.mLooper;
    }

    public boolean quit() {
        Looper looper = getLooper();
        if (looper != null) {
            looper.quit();
            return true;
        }
        return false;
    }

    public boolean quitSafely() {
        Looper looper = getLooper();
        if (looper != null) {
            looper.quitSafely();
            return true;
        }
        return false;
    }
}
