package com.xiaopeng.xuiservice.uvccamera.opengl;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes5.dex */
public class EGLBase10 extends EGLBase {
    private static final String TAG = EGLBase10.class.getSimpleName();
    private static final Context EGL_NO_CONTEXT = new Context(EGL10.EGL_NO_CONTEXT);
    private EGL10 mEgl = null;
    private EGLDisplay mEglDisplay = null;
    private Config mEglConfig = null;
    private int mGlVersion = 2;
    @NonNull
    private Context mContext = EGL_NO_CONTEXT;

    /* loaded from: classes5.dex */
    public static class Context extends EGLBase.IContext {
        public final EGLContext eglContext;

        private Context(EGLContext context) {
            this.eglContext = context;
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase.IContext
        public long getNativeHandle() {
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
    public static class MySurfaceHolder implements SurfaceHolder {
        private final Surface surface;

        public MySurfaceHolder(Surface surface) {
            this.surface = surface;
        }

        @Override // android.view.SurfaceHolder
        public Surface getSurface() {
            return this.surface;
        }

        @Override // android.view.SurfaceHolder
        public void addCallback(SurfaceHolder.Callback callback) {
        }

        @Override // android.view.SurfaceHolder
        public void removeCallback(SurfaceHolder.Callback callback) {
        }

        @Override // android.view.SurfaceHolder
        public boolean isCreating() {
            return false;
        }

        @Override // android.view.SurfaceHolder
        public void setType(int type) {
        }

        @Override // android.view.SurfaceHolder
        public void setFixedSize(int width, int height) {
        }

        @Override // android.view.SurfaceHolder
        public void setSizeFromLayout() {
        }

        @Override // android.view.SurfaceHolder
        public void setFormat(int format) {
        }

        @Override // android.view.SurfaceHolder
        public void setKeepScreenOn(boolean screenOn) {
        }

        @Override // android.view.SurfaceHolder
        public Canvas lockCanvas() {
            return null;
        }

        @Override // android.view.SurfaceHolder
        public Canvas lockCanvas(Rect dirty) {
            return null;
        }

        @Override // android.view.SurfaceHolder
        public void unlockCanvasAndPost(Canvas canvas) {
        }

        @Override // android.view.SurfaceHolder
        public Rect getSurfaceFrame() {
            return null;
        }
    }

    /* loaded from: classes5.dex */
    public static class EglSurface implements EGLBase.IEglSurface {
        private final EGLBase10 mEglBase;
        private EGLSurface mEglSurface;

        private EglSurface(EGLBase10 eglBase, Object surface) throws IllegalArgumentException {
            this.mEglSurface = EGL10.EGL_NO_SURFACE;
            this.mEglBase = eglBase;
            if ((surface instanceof Surface) && Build.VERSION.SDK_INT < 17) {
                this.mEglSurface = this.mEglBase.createWindowSurface(new MySurfaceHolder((Surface) surface));
            } else if ((surface instanceof Surface) || (surface instanceof SurfaceHolder) || (surface instanceof SurfaceTexture) || (surface instanceof SurfaceView)) {
                this.mEglSurface = this.mEglBase.createWindowSurface(surface);
            } else {
                throw new IllegalArgumentException("unsupported surface");
            }
        }

        private EglSurface(EGLBase10 eglBase, int width, int height) {
            this.mEglSurface = EGL10.EGL_NO_SURFACE;
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
            return eGLSurface != null && eGLSurface != EGL10.EGL_NO_SURFACE && this.mEglBase.getSurfaceWidth(this.mEglSurface) > 0 && this.mEglBase.getSurfaceHeight(this.mEglSurface) > 0;
        }

        @Override // com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase.IEglSurface
        public void release() {
            this.mEglBase.makeDefault();
            this.mEglBase.destroyWindowSurface(this.mEglSurface);
            this.mEglSurface = EGL10.EGL_NO_SURFACE;
        }
    }

    public EGLBase10(Context sharedContext, int maxClientVersion, boolean withDepthBuffer, int stencilBits, boolean isRecordable) {
        init(sharedContext, maxClientVersion, withDepthBuffer, stencilBits, isRecordable);
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase
    public void release() {
        destroyContext();
        this.mContext = EGL_NO_CONTEXT;
        EGL10 egl10 = this.mEgl;
        if (egl10 == null) {
            return;
        }
        egl10.eglTerminate(this.mEglDisplay);
        this.mEglDisplay = null;
        this.mEglConfig = null;
        this.mEgl = null;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase
    public EglSurface createFromSurface(Object nativeWindow) {
        EglSurface eglSurface = new EglSurface(nativeWindow);
        eglSurface.makeCurrent();
        return eglSurface;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase
    public EglSurface createOffscreen(int width, int height) {
        EglSurface eglSurface = new EglSurface(width, height);
        eglSurface.makeCurrent();
        return eglSurface;
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
        if (!this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT)) {
            String str = TAG;
            Log.w(str, "makeDefault:eglMakeCurrent:err=" + this.mEgl.eglGetError());
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase
    public void sync() {
        this.mEgl.eglWaitGL();
        this.mEgl.eglWaitNative(12379, null);
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase
    public String queryString(int what) {
        return this.mEgl.eglQueryString(this.mEglDisplay, what);
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase
    public int getGlVersion() {
        return this.mGlVersion;
    }

    private void init(@Nullable Context sharedContext, int maxClientVersion, boolean withDepthBuffer, int stencilBits, boolean isRecordable) {
        Context context;
        EGLConfig config;
        Context sharedContext2 = sharedContext != null ? sharedContext : EGL_NO_CONTEXT;
        if (this.mEgl == null) {
            this.mEgl = (EGL10) EGLContext.getEGL();
            this.mEglDisplay = this.mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            if (this.mEglDisplay == EGL10.EGL_NO_DISPLAY) {
                throw new RuntimeException("eglGetDisplay failed");
            }
            int[] version = new int[2];
            if (!this.mEgl.eglInitialize(this.mEglDisplay, version)) {
                throw new RuntimeException("eglInitialize failed");
            }
        }
        if (maxClientVersion >= 3 && (config = getConfig(3, withDepthBuffer, stencilBits, isRecordable)) != null) {
            EGLContext context2 = createContext(sharedContext2, config, 3);
            if (this.mEgl.eglGetError() == 12288) {
                this.mEglConfig = new Config(config);
                this.mContext = new Context(context2);
                this.mGlVersion = 3;
            }
        }
        if (maxClientVersion >= 2 && ((context = this.mContext) == null || context.eglContext == EGL10.EGL_NO_CONTEXT)) {
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
        if (context5 == null || context5.eglContext == EGL10.EGL_NO_CONTEXT) {
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
        this.mEgl.eglQueryContext(this.mEglDisplay, this.mContext.eglContext, EGLBase.EGL_CONTEXT_CLIENT_VERSION, values);
        String str = TAG;
        Log.d(str, "EGLContext created, client version " + values[0]);
        makeDefault();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean makeCurrent(EGLSurface surface) {
        if (surface == null || surface == EGL10.EGL_NO_SURFACE) {
            int error = this.mEgl.eglGetError();
            if (error == 12299) {
                Log.e(TAG, "makeCurrent:EGL_BAD_NATIVE_WINDOW");
            }
            return false;
        } else if (!this.mEgl.eglMakeCurrent(this.mEglDisplay, surface, surface, this.mContext.eglContext)) {
            Log.w("TAG", "eglMakeCurrent" + this.mEgl.eglGetError());
            return false;
        } else {
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int swap(EGLSurface surface) {
        if (!this.mEgl.eglSwapBuffers(this.mEglDisplay, surface)) {
            int err = this.mEgl.eglGetError();
            return err;
        }
        return 12288;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int swap(EGLSurface surface, long ignored) {
        if (!this.mEgl.eglSwapBuffers(this.mEglDisplay, surface)) {
            int err = this.mEgl.eglGetError();
            return err;
        }
        return 12288;
    }

    private EGLContext createContext(@NonNull Context sharedContext, EGLConfig config, int version) {
        int[] attrib_list = {EGLBase.EGL_CONTEXT_CLIENT_VERSION, version, 12344};
        EGLContext context = this.mEgl.eglCreateContext(this.mEglDisplay, config, sharedContext.eglContext, attrib_list);
        return context;
    }

    private void destroyContext() {
        if (!this.mEgl.eglDestroyContext(this.mEglDisplay, this.mContext.eglContext)) {
            Log.e("destroyContext", "display:" + this.mEglDisplay + " context: " + this.mContext.eglContext);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("eglDestroyContext:");
            sb.append(this.mEgl.eglGetError());
            Log.e(str, sb.toString());
        }
        this.mContext = EGL_NO_CONTEXT;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSurfaceWidth(EGLSurface surface) {
        int[] value = new int[1];
        boolean ret = this.mEgl.eglQuerySurface(this.mEglDisplay, surface, 12375, value);
        if (!ret) {
            value[0] = 0;
        }
        return value[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSurfaceHeight(EGLSurface surface) {
        int[] value = new int[1];
        boolean ret = this.mEgl.eglQuerySurface(this.mEglDisplay, surface, 12374, value);
        if (!ret) {
            value[0] = 0;
        }
        return value[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public EGLSurface createWindowSurface(Object nativeWindow) {
        int[] surfaceAttribs = {12344};
        try {
            EGLSurface result = this.mEgl.eglCreateWindowSurface(this.mEglDisplay, this.mEglConfig.eglConfig, nativeWindow, surfaceAttribs);
            if (result != null && result != EGL10.EGL_NO_SURFACE) {
                makeCurrent(result);
                return result;
            }
            int error = this.mEgl.eglGetError();
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
        int[] surfaceAttribs = {12375, width, 12374, height, 12344};
        this.mEgl.eglWaitGL();
        EGLSurface result = null;
        try {
            result = this.mEgl.eglCreatePbufferSurface(this.mEglDisplay, this.mEglConfig.eglConfig, surfaceAttribs);
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
        if (surface != EGL10.EGL_NO_SURFACE) {
            this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
            this.mEgl.eglDestroySurface(this.mEglDisplay, surface);
        }
        EGLSurface surface2 = EGL10.EGL_NO_SURFACE;
    }

    private void checkEglError(String msg) {
        int error = this.mEgl.eglGetError();
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
            attribList[offset2] = 8;
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
        if (this.mEgl.eglChooseConfig(this.mEglDisplay, attribList, configs, configs.length, numConfigs) && numConfigs[0] >= 0) {
            return configs[0];
        }
        return null;
    }
}
