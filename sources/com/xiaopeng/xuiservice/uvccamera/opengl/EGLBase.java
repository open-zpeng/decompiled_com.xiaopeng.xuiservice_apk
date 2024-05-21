package com.xiaopeng.xuiservice.uvccamera.opengl;

import android.os.Build;
import com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase10;
import com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase14;
/* loaded from: classes5.dex */
public abstract class EGLBase {
    public static final int EGL_CONTEXT_CLIENT_VERSION = 12440;
    public static final Object EGL_LOCK = new Object();
    public static final int EGL_OPENGL_ES2_BIT = 4;
    public static final int EGL_OPENGL_ES3_BIT_KHR = 64;
    public static final int EGL_RECORDABLE_ANDROID = 12610;

    /* loaded from: classes5.dex */
    public static abstract class IConfig {
    }

    /* loaded from: classes5.dex */
    public static abstract class IContext {
        public abstract Object getEGLContext();

        public abstract long getNativeHandle();
    }

    /* loaded from: classes5.dex */
    public interface IEglSurface {
        IContext getContext();

        boolean isValid();

        void makeCurrent();

        void release();

        void swap();

        void swap(long j);
    }

    public abstract IEglSurface createFromSurface(Object obj);

    public abstract IEglSurface createOffscreen(int i, int i2);

    public abstract IConfig getConfig();

    public abstract IContext getContext();

    public abstract int getGlVersion();

    public abstract void makeDefault();

    public abstract String queryString(int i);

    public abstract void release();

    public abstract void sync();

    public static EGLBase createFrom(IContext sharedContext, boolean withDepthBuffer, boolean isRecordable) {
        return createFrom(sharedContext, 3, withDepthBuffer, 0, isRecordable);
    }

    public static EGLBase createFrom(IContext sharedContext, boolean withDepthBuffer, int stencilBits, boolean isRecordable) {
        return createFrom(sharedContext, 3, withDepthBuffer, stencilBits, isRecordable);
    }

    public static EGLBase createFrom(IContext sharedContext, int maxClientVersion, boolean withDepthBuffer, int stencilBits, boolean isRecordable) {
        if (isEGL14Supported() && (sharedContext == null || (sharedContext instanceof EGLBase14.Context))) {
            return new EGLBase14((EGLBase14.Context) sharedContext, maxClientVersion, withDepthBuffer, stencilBits, isRecordable);
        }
        return new EGLBase10((EGLBase10.Context) sharedContext, maxClientVersion, withDepthBuffer, stencilBits, isRecordable);
    }

    public static boolean isEGL14Supported() {
        return Build.VERSION.SDK_INT >= 18;
    }
}
