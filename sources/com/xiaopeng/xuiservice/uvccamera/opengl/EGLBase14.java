package com.xiaopeng.xuiservice.uvccamera.opengl;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.NonNull;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase;
/* JADX INFO: Access modifiers changed from: package-private */
@TargetApi(18)
/* loaded from: classes5.dex */
public class EGLBase14 extends EGLBase {
    private static final String TAG = EGLBase14.class.getSimpleName();
    private static final Context EGL_NO_CONTEXT = new Context(EGL14.EGL_NO_CONTEXT);
    private Config mEglConfig = null;
    @NonNull
    private Context mContext = EGL_NO_CONTEXT;
    private EGLDisplay mEglDisplay = EGL14.EGL_NO_DISPLAY;
    private EGLContext mDefaultContext = EGL14.EGL_NO_CONTEXT;
    private int mGlVersion = 2;
    private final int[] mSurfaceDimension = new int[2];

    /* loaded from: classes5.dex */
    public static class Context extends EGLBase.IContext {
        public final EGLContext eglContext;

        private Context(EGLContext context) {
            this.eglContext = context;
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase.IContext
        @SuppressLint({"NewApi"})
        public long getNativeHandle() {
            if (this.eglContext != null) {
                return Build.VERSION.SDK_INT >= 21 ? this.eglContext.getNativeHandle() : this.eglContext.getHandle();
            }
            return 0L;
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase.IContext
        public Object getEGLContext() {
            return this.eglContext;
        }
    }

    /* loaded from: classes5.dex */
    public static class Config extends EGLBase.IConfig {
        public final EGLConfig eglConfig;

        private Config(EGLConfig eglConfig) {
            this.eglConfig = eglConfig;
        }
    }

    /* loaded from: classes5.dex */
    public static class EglSurface implements EGLBase.IEglSurface {
        private final EGLBase14 mEglBase;
        private EGLSurface mEglSurface;

        private EglSurface(EGLBase14 eglBase, Object surface) throws IllegalArgumentException {
            this.mEglSurface = EGL14.EGL_NO_SURFACE;
            LogUtil.v(EGLBase14.TAG, "EglSurface:");
            this.mEglBase = eglBase;
            if ((surface instanceof Surface) || (surface instanceof SurfaceHolder) || (surface instanceof SurfaceTexture) || (surface instanceof SurfaceView)) {
                this.mEglSurface = this.mEglBase.createWindowSurface(surface);
                return;
            }
            throw new IllegalArgumentException("unsupported surface");
        }

        private EglSurface(EGLBase14 eglBase, int width, int height) {
            this.mEglSurface = EGL14.EGL_NO_SURFACE;
            LogUtil.v(EGLBase14.TAG, "EglSurface:");
            this.mEglBase = eglBase;
            if (width <= 0 || height <= 0) {
                this.mEglSurface = this.mEglBase.createOffscreenSurface(1, 1);
            } else {
                this.mEglSurface = this.mEglBase.createOffscreenSurface(width, height);
            }
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase.IEglSurface
        public void makeCurrent() {
            this.mEglBase.makeCurrent(this.mEglSurface);
            if (this.mEglBase.getGlVersion() >= 2) {
                GLES20.glViewport(0, 0, this.mEglBase.getSurfaceWidth(this.mEglSurface), this.mEglBase.getSurfaceHeight(this.mEglSurface));
            } else {
                GLES10.glViewport(0, 0, this.mEglBase.getSurfaceWidth(this.mEglSurface), this.mEglBase.getSurfaceHeight(this.mEglSurface));
            }
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase.IEglSurface
        public void swap() {
            this.mEglBase.swap(this.mEglSurface);
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase.IEglSurface
        public void swap(long presentationTimeNs) {
            this.mEglBase.swap(this.mEglSurface, presentationTimeNs);
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase.IEglSurface
        public EGLBase.IContext getContext() {
            return this.mEglBase.getContext();
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase.IEglSurface
        public boolean isValid() {
            EGLSurface eGLSurface = this.mEglSurface;
            return eGLSurface != null && eGLSurface != EGL14.EGL_NO_SURFACE && this.mEglBase.getSurfaceWidth(this.mEglSurface) > 0 && this.mEglBase.getSurfaceHeight(this.mEglSurface) > 0;
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase.IEglSurface
        public void release() {
            LogUtil.v(EGLBase14.TAG, "EglSurface:release:");
            this.mEglBase.makeDefault();
            this.mEglBase.destroyWindowSurface(this.mEglSurface);
            this.mEglSurface = EGL14.EGL_NO_SURFACE;
        }
    }

    public EGLBase14(Context sharedContext, int maxClientVersion, boolean withDepthBuffer, int stencilBits, boolean isRecordable) {
        LogUtil.v(TAG, "Constructor:");
        init(sharedContext, maxClientVersion, withDepthBuffer, stencilBits, isRecordable);
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase
    public void release() {
        LogUtil.v(TAG, "release:");
        if (this.mEglDisplay != EGL14.EGL_NO_DISPLAY) {
            destroyContext();
            EGL14.eglTerminate(this.mEglDisplay);
            EGL14.eglReleaseThread();
        }
        this.mEglDisplay = EGL14.EGL_NO_DISPLAY;
        this.mContext = EGL_NO_CONTEXT;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase
    public EglSurface createFromSurface(Object nativeWindow) {
        LogUtil.v(TAG, "createFromSurface:");
        EglSurface eglSurface = new EglSurface(nativeWindow);
        eglSurface.makeCurrent();
        return eglSurface;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase
    public EglSurface createOffscreen(int width, int height) {
        LogUtil.v(TAG, "createOffscreen:");
        EglSurface eglSurface = new EglSurface(width, height);
        eglSurface.makeCurrent();
        return eglSurface;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase
    public String queryString(int what) {
        return EGL14.eglQueryString(this.mEglDisplay, what);
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase
    public int getGlVersion() {
        return this.mGlVersion;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase
    public Context getContext() {
        return this.mContext;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase
    public Config getConfig() {
        return this.mEglConfig;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase
    public void makeDefault() {
        LogUtil.v(TAG, "makeDefault:");
        if (!EGL14.eglMakeCurrent(this.mEglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT)) {
            Log.w("TAG", "makeDefault" + EGL14.eglGetError());
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase
    public void sync() {
        EGL14.eglWaitGL();
        EGL14.eglWaitNative(12379);
    }

    private void init(Context sharedContext, int maxClientVersion, boolean withDepthBuffer, int stencilBits, boolean isRecordable) {
        Context context;
        EGLConfig config;
        LogUtil.v(TAG, "init:");
        this.mEglDisplay = EGL14.eglGetDisplay(0);
        if (this.mEglDisplay == EGL14.EGL_NO_DISPLAY) {
            throw new RuntimeException("eglGetDisplay failed");
        }
        int[] version = new int[2];
        if (!EGL14.eglInitialize(this.mEglDisplay, version, 0, version, 1)) {
            throw new RuntimeException("eglInitialize failed");
        }
        Context sharedContext2 = sharedContext != null ? sharedContext : EGL_NO_CONTEXT;
        if (maxClientVersion >= 3 && (config = getConfig(3, withDepthBuffer, stencilBits, isRecordable)) != null) {
            EGLContext context2 = createContext(sharedContext2, config, 3);
            if (EGL14.eglGetError() == 12288) {
                this.mEglConfig = new Config(config);
                this.mContext = new Context(context2);
                this.mGlVersion = 3;
            }
        }
        if (maxClientVersion >= 2 && ((context = this.mContext) == null || context.eglContext == EGL14.EGL_NO_CONTEXT)) {
            EGLConfig config2 = getConfig(2, withDepthBuffer, stencilBits, isRecordable);
            if (config2 == null) {
                throw new RuntimeException("chooseConfig failed");
            }
            try {
                EGLContext context3 = createContext(sharedContext2, config2, 2);
                checkEglError("eglCreateContext");
                this.mEglConfig = new Config(config2);
                this.mContext = new Context(context3);
                this.mGlVersion = 2;
            } catch (Exception e) {
                if (isRecordable) {
                    EGLConfig config3 = getConfig(2, withDepthBuffer, stencilBits, false);
                    if (config3 == null) {
                        throw new RuntimeException("chooseConfig failed");
                    }
                    EGLContext context4 = createContext(sharedContext2, config3, 2);
                    checkEglError("eglCreateContext");
                    this.mEglConfig = new Config(config3);
                    this.mContext = new Context(context4);
                    this.mGlVersion = 2;
                }
            }
        }
        Context context5 = this.mContext;
        if (context5 == null || context5.eglContext == EGL14.EGL_NO_CONTEXT) {
            EGLConfig config4 = getConfig(1, withDepthBuffer, stencilBits, isRecordable);
            if (config4 == null) {
                throw new RuntimeException("chooseConfig failed");
            }
            EGLContext context6 = createContext(sharedContext2, config4, 1);
            checkEglError("eglCreateContext");
            this.mEglConfig = new Config(config4);
            this.mContext = new Context(context6);
            this.mGlVersion = 1;
        }
        int[] values = new int[1];
        EGL14.eglQueryContext(this.mEglDisplay, this.mContext.eglContext, EGLBase.EGL_CONTEXT_CLIENT_VERSION, values, 0);
        String str = TAG;
        Log.d(str, "EGLContext created, client version " + values[0]);
        makeDefault();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean makeCurrent(EGLSurface surface) {
        LogUtil.v(TAG, "makeCurrent:");
        if (surface == null || surface == EGL14.EGL_NO_SURFACE) {
            int error = EGL14.eglGetError();
            if (error == 12299) {
                Log.e(TAG, "makeCurrent:returned EGL_BAD_NATIVE_WINDOW.");
            }
            return false;
        } else if (!EGL14.eglMakeCurrent(this.mEglDisplay, surface, surface, this.mContext.eglContext)) {
            Log.w("TAG", "eglMakeCurrent" + EGL14.eglGetError());
            return false;
        } else {
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int swap(EGLSurface surface) {
        LogUtil.v(TAG, "swap:");
        if (!EGL14.eglSwapBuffers(this.mEglDisplay, surface)) {
            int err = EGL14.eglGetError();
            String str = TAG;
            LogUtil.w(str, "swap:err=" + err);
            return err;
        }
        return 12288;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int swap(EGLSurface surface, long presentationTimeNs) {
        LogUtil.v(TAG, "swap:");
        EGLExt.eglPresentationTimeANDROID(this.mEglDisplay, surface, presentationTimeNs);
        if (!EGL14.eglSwapBuffers(this.mEglDisplay, surface)) {
            int err = EGL14.eglGetError();
            String str = TAG;
            LogUtil.w(str, "swap:err=" + err);
            return err;
        }
        return 12288;
    }

    private EGLContext createContext(Context sharedContext, EGLConfig config, int version) {
        LogUtil.v(TAG, "createContext:");
        int[] attrib_list = {EGLBase.EGL_CONTEXT_CLIENT_VERSION, version, 12344};
        EGLContext context = EGL14.eglCreateContext(this.mEglDisplay, config, sharedContext.eglContext, attrib_list, 0);
        return context;
    }

    private void destroyContext() {
        LogUtil.v(TAG, "destroyContext:");
        if (!EGL14.eglDestroyContext(this.mEglDisplay, this.mContext.eglContext)) {
            Log.e("destroyContext", "display:" + this.mEglDisplay + " context: " + this.mContext.eglContext);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("eglDestroyContext:");
            sb.append(EGL14.eglGetError());
            Log.e(str, sb.toString());
        }
        this.mContext = EGL_NO_CONTEXT;
        if (this.mDefaultContext != EGL14.EGL_NO_CONTEXT) {
            if (!EGL14.eglDestroyContext(this.mEglDisplay, this.mDefaultContext)) {
                Log.e("destroyContext", "display:" + this.mEglDisplay + " context: " + this.mDefaultContext);
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("eglDestroyContext:");
                sb2.append(EGL14.eglGetError());
                Log.e(str2, sb2.toString());
            }
            this.mDefaultContext = EGL14.EGL_NO_CONTEXT;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSurfaceWidth(EGLSurface surface) {
        boolean ret = EGL14.eglQuerySurface(this.mEglDisplay, surface, 12375, this.mSurfaceDimension, 0);
        if (!ret) {
            this.mSurfaceDimension[0] = 0;
        }
        return this.mSurfaceDimension[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSurfaceHeight(EGLSurface surface) {
        boolean ret = EGL14.eglQuerySurface(this.mEglDisplay, surface, 12374, this.mSurfaceDimension, 1);
        if (!ret) {
            this.mSurfaceDimension[1] = 0;
        }
        return this.mSurfaceDimension[1];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public EGLSurface createWindowSurface(Object nativeWindow) {
        String str = TAG;
        LogUtil.v(str, "createWindowSurface:nativeWindow=" + nativeWindow);
        int[] surfaceAttribs = {12344};
        try {
            EGLSurface result = EGL14.eglCreateWindowSurface(this.mEglDisplay, this.mEglConfig.eglConfig, nativeWindow, surfaceAttribs, 0);
            if (result != null && result != EGL14.EGL_NO_SURFACE) {
                makeCurrent(result);
                return result;
            }
            int error = EGL14.eglGetError();
            if (error == 12299) {
                Log.e(TAG, "createWindowSurface returned EGL_BAD_NATIVE_WINDOW.");
            }
            throw new RuntimeException("createWindowSurface failed error=" + error);
        } catch (Exception e) {
            Log.e(TAG, "eglCreateWindowSurface", e);
            throw new IllegalArgumentException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public EGLSurface createOffscreenSurface(int width, int height) {
        LogUtil.v(TAG, "createOffscreenSurface:");
        int[] surfaceAttribs = {12375, width, 12374, height, 12344};
        EGLSurface result = null;
        try {
            result = EGL14.eglCreatePbufferSurface(this.mEglDisplay, this.mEglConfig.eglConfig, surfaceAttribs, 0);
            checkEglError("eglCreatePbufferSurface");
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "createOffscreenSurface", e);
        } catch (RuntimeException e2) {
            Log.e(TAG, "createOffscreenSurface", e2);
        }
        if (result == null) {
            throw new RuntimeException("Failed to create pixel buffer surface");
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void destroyWindowSurface(EGLSurface surface) {
        LogUtil.v(TAG, "destroySurface:");
        if (surface != EGL14.EGL_NO_SURFACE) {
            EGL14.eglMakeCurrent(this.mEglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
            EGL14.eglDestroySurface(this.mEglDisplay, surface);
        }
        LogUtil.v(TAG, "destroySurface:finished");
    }

    private void checkEglError(String msg) {
        int error = EGL14.eglGetError();
        if (error != 12288) {
            throw new RuntimeException(msg + ": EGL error: 0x" + Integer.toHexString(error));
        }
    }

    private EGLConfig getConfig(int version, boolean hasDepthBuffer, int stencilBits, boolean isRecordable) {
        int renderableType = version >= 3 ? 4 | 64 : 4;
        int[] attribList = {12352, renderableType, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12344, 12344, 12344, 12344, 12344, 12344, 12344};
        int offset = 10;
        if (stencilBits > 0) {
            int offset2 = 10 + 1;
            attribList[10] = 12326;
            offset = offset2 + 1;
            attribList[offset2] = stencilBits;
        }
        if (hasDepthBuffer) {
            int offset3 = offset + 1;
            attribList[offset] = 12325;
            offset = offset3 + 1;
            attribList[offset3] = 16;
        }
        if (isRecordable && Build.VERSION.SDK_INT >= 18) {
            int offset4 = offset + 1;
            attribList[offset] = 12610;
            offset = offset4 + 1;
            attribList[offset4] = 1;
        }
        for (int i = attribList.length - 1; i >= offset; i--) {
            attribList[i] = 12344;
        }
        EGLConfig config = internalGetConfig(attribList);
        if (config == null && version == 2 && isRecordable) {
            int n = attribList.length;
            int i2 = 10;
            while (true) {
                if (i2 >= n - 1) {
                    break;
                } else if (attribList[i2] != 12610) {
                    i2 += 2;
                } else {
                    for (int j = i2; j < n; j++) {
                        attribList[j] = 12344;
                    }
                }
            }
            config = internalGetConfig(attribList);
        }
        if (config == null) {
            Log.w(TAG, "try to fallback to RGB565");
            attribList[3] = 5;
            attribList[5] = 6;
            attribList[7] = 5;
            return internalGetConfig(attribList);
        }
        return config;
    }

    private EGLConfig internalGetConfig(int[] attribList) {
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfigs = new int[1];
        if (EGL14.eglChooseConfig(this.mEglDisplay, attribList, 0, configs, 0, configs.length, numConfigs, 0) && numConfigs[0] >= 0) {
            return configs[0];
        }
        return null;
    }
}
