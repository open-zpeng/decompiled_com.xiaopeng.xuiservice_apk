package com.xiaopeng.xuiservice.uvccamera.opengl.renderer;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase;
import com.xiaopeng.xuiservice.uvccamera.opengl.EGLTask;
import com.xiaopeng.xuiservice.uvccamera.opengl.GLDrawer2D;
import com.xiaopeng.xuiservice.uvccamera.opengl.GLHelper;
import com.xiaopeng.xuiservice.uvccamera.opengl.ShaderConst;
import com.xiaopeng.xuiservice.uvccamera.usb.UVCCamera;
import com.xiaopeng.xuiservice.uvccamera.utils.UVCUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/* loaded from: classes5.dex */
public class RendererHolder extends EGLTask implements IRendererHolder {
    protected static final int REQUEST_ADD_SLAVE_SURFACE = 4;
    protected static final int REQUEST_CLEAR_SLAVE_SURFACE = 11;
    protected static final int REQUEST_CLEAR_SLAVE_SURFACE_ALL = 12;
    protected static final int REQUEST_DRAW = 2;
    protected static final int REQUEST_INIT = 1;
    protected static final int REQUEST_RECREATE_PRIMARY_SURFACE = 6;
    protected static final int REQUEST_RELEASE = 99;
    protected static final int REQUEST_RELEASE_PRIMARY_SURFACE = 14;
    protected static final int REQUEST_REMOVE_SLAVE_SURFACE = 5;
    protected static final int REQUEST_REMOVE_SLAVE_SURFACE_ALL = 13;
    protected static final int REQUEST_UPDATE_SIZE = 3;
    private static final String TAG = RendererHolder.class.getSimpleName();
    protected volatile boolean isRunning;
    @Nullable
    private final RendererHolderCallback mCallback;
    protected final Context mContext;
    private final Condition mCreatePrimarySurfaceCondition;
    private GLDrawer2D mDrawer;
    private volatile boolean mIsFirstFrameRendered;
    private final Lock mLock;
    protected final float[] mMirrorMatrix;
    private int mMirrorMode;
    protected final float[] mMvpMatrix;
    private Surface mPrimarySurface;
    private SurfaceTexture mPrimaryTexture;
    protected final RendererHandler mRendererHandler;
    protected final float[] mRotationMatrix;
    private final SparseArray<RendererSurface> mSlaveSurfaces;
    protected int mTexId;
    protected final float[] mTexMatrix;
    protected int mVideoHeight;
    protected int mVideoWidth;

    public RendererHolder(int width, int height, @Nullable RendererHolderCallback callback) {
        this(width, height, null, 2, 3, callback);
    }

    public RendererHolder(int width, int height, EGLBase.IContext sharedContext, int flags, int maxClientVersion, @Nullable RendererHolderCallback callback) {
        super(sharedContext, flags, maxClientVersion);
        this.mContext = UVCUtils.getApplication();
        this.mSlaveSurfaces = new SparseArray<>();
        this.mTexMatrix = new float[16];
        this.mMvpMatrix = new float[16];
        this.mRotationMatrix = new float[16];
        this.mMirrorMatrix = new float[16];
        this.mLock = new ReentrantLock();
        this.mCreatePrimarySurfaceCondition = this.mLock.newCondition();
        this.mMirrorMode = 0;
        this.mCallback = callback;
        this.mVideoWidth = width > 0 ? width : UVCCamera.DEFAULT_PREVIEW_WIDTH;
        this.mVideoHeight = height > 0 ? height : 480;
        this.mRendererHandler = new RendererHandler(getLooper());
        this.mRendererHandler.sendEmptyMessage(1);
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.renderer.IRendererHolder
    public boolean isRunning() {
        return this.isRunning;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.renderer.IRendererHolder
    public void release() {
        LogUtil.v(TAG, "release:");
        this.mRendererHandler.sendEmptyMessage(99);
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.renderer.IRendererHolder
    public Surface getPrimarySurface() {
        checkPrimarySurface();
        return this.mPrimarySurface;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.renderer.IRendererHolder
    public SurfaceTexture getPrimarySurfaceTexture() {
        checkPrimarySurface();
        return this.mPrimaryTexture;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.renderer.IRendererHolder
    public void checkPrimarySurface() {
        Surface surface = this.mPrimarySurface;
        if (surface == null || !surface.isValid()) {
            Log.d(TAG, "checkPrimarySurface:invalid primary surface");
            this.mLock.lock();
            try {
                this.mRendererHandler.sendEmptyMessage(6);
                try {
                    this.mCreatePrimarySurfaceCondition.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } finally {
                this.mLock.unlock();
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.renderer.IRendererHolder
    public void updatePrimarySize(int width, int height) throws IllegalStateException {
        if (width <= 0 || height <= 0) {
            return;
        }
        if (this.mVideoWidth != width || this.mVideoHeight != height) {
            this.mLock.lock();
            try {
                this.mRendererHandler.sendMessage(this.mRendererHandler.obtainMessage(3, width, height));
                this.mRendererHandler.sendEmptyMessage(6);
                try {
                    this.mCreatePrimarySurfaceCondition.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } finally {
                this.mLock.unlock();
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.renderer.IRendererHolder
    public void addSlaveSurface(int id, Object surface, boolean isRecordable) throws IllegalStateException, IllegalArgumentException {
        addSlaveSurface(id, surface, isRecordable, -1);
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.renderer.IRendererHolder
    public void addSlaveSurface(int id, Object surface, boolean isRecordable, int maxFps) throws IllegalStateException, IllegalArgumentException {
        String str = TAG;
        LogUtil.v(str, "addSlaveSurface:id=" + id + ",surface=" + surface);
        if (!(surface instanceof SurfaceTexture) && !(surface instanceof Surface) && !(surface instanceof SurfaceHolder)) {
            throw new IllegalArgumentException("Surface should be one of Surface, SurfaceTexture or SurfaceHolder");
        }
        synchronized (this.mSlaveSurfaces) {
            if (this.mSlaveSurfaces.get(id) == null) {
                this.mRendererHandler.sendMessage(this.mRendererHandler.obtainMessage(4, id, maxFps, surface));
                try {
                    this.mSlaveSurfaces.wait(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.renderer.IRendererHolder
    public void removeSlaveSurface(int id) {
        String str = TAG;
        LogUtil.v(str, "removeSlaveSurface:id=" + id);
        synchronized (this.mSlaveSurfaces) {
            if (this.mSlaveSurfaces.get(id) != null) {
                this.mRendererHandler.sendMessage(this.mRendererHandler.obtainMessage(5, id, 0));
                try {
                    this.mSlaveSurfaces.wait(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.renderer.IRendererHolder
    public void removeSlaveSurfaceAll() {
        LogUtil.v(TAG, "removeSlaveSurfaceAll");
        synchronized (this.mSlaveSurfaces) {
            this.mRendererHandler.sendEmptyMessage(13);
            try {
                this.mSlaveSurfaces.wait(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.renderer.IRendererHolder
    public void clearSlaveSurface(int id, int color) {
        RendererHandler rendererHandler = this.mRendererHandler;
        rendererHandler.sendMessage(rendererHandler.obtainMessage(11, id, color));
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.renderer.IRendererHolder
    public void clearSlaveSurfaceAll(int color) {
        RendererHandler rendererHandler = this.mRendererHandler;
        rendererHandler.sendMessage(rendererHandler.obtainMessage(12, Integer.valueOf(color)));
    }

    private void updateMvpMatrix() {
        Matrix.multiplyMM(this.mMvpMatrix, 0, this.mRotationMatrix, 0, this.mMirrorMatrix, 0);
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.renderer.IRendererHolder
    public void rotateTo(int angle) {
        Matrix.setRotateM(this.mRotationMatrix, 0, angle, 0.0f, 0.0f, -1.0f);
        updateMvpMatrix();
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.renderer.IRendererHolder
    public void rotateBy(int angle) {
        Matrix.rotateM(this.mRotationMatrix, 0, angle, 0.0f, 0.0f, -1.0f);
        updateMvpMatrix();
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.renderer.IRendererHolder
    public void setMirrorMode(int mode) {
        if (mode == 0) {
            this.mMirrorMode = mode;
        } else {
            this.mMirrorMode ^= mode;
        }
        setMirrorMode(this.mMirrorMatrix, this.mMirrorMode);
        updateMvpMatrix();
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.renderer.IRendererHolder
    public int getMirrorMode() {
        return this.mMirrorMode;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.renderer.IRendererHolder
    public boolean isSlaveSurfaceEnable(int id) {
        boolean z;
        synchronized (this.mSlaveSurfaces) {
            RendererSurface slaveSurface = this.mSlaveSurfaces.get(id);
            z = slaveSurface != null && slaveSurface.isEnable();
        }
        return z;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.renderer.IRendererHolder
    public void setSlaveSurfaceEnable(int id, boolean enable) {
        synchronized (this.mSlaveSurfaces) {
            RendererSurface slaveSurface = this.mSlaveSurfaces.get(id);
            if (slaveSurface != null) {
                slaveSurface.setEnable(enable);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.renderer.IRendererHolder
    public void requestFrame() {
        this.mRendererHandler.removeMessages(2);
        this.mRendererHandler.sendEmptyMessage(2);
    }

    protected void onDrawSlaveSurface(@NonNull RendererSurface surface, int texId, float[] texMatrix, float[] mvpMatrix) {
        surface.draw(this.mDrawer, texId, texMatrix, mvpMatrix);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onPrimarySurfaceCreate(Surface surface) {
        RendererHolderCallback rendererHolderCallback = this.mCallback;
        if (rendererHolderCallback != null) {
            try {
                rendererHolderCallback.onPrimarySurfaceCreate(surface);
            } catch (Exception e) {
                Log.w(TAG, e);
            }
        }
    }

    protected void onFrameAvailable() {
        RendererHolderCallback rendererHolderCallback = this.mCallback;
        if (rendererHolderCallback != null) {
            try {
                rendererHolderCallback.onFrameAvailable();
            } catch (Exception e) {
                Log.w(TAG, e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onPrimarySurfaceDestroy() {
        RendererHolderCallback rendererHolderCallback = this.mCallback;
        if (rendererHolderCallback != null) {
            try {
                rendererHolderCallback.onPrimarySurfaceDestroy();
            } catch (Exception e) {
                Log.w(TAG, e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes5.dex */
    public class RendererHandler extends Handler {
        protected final SurfaceTexture.OnFrameAvailableListener mOnFrameAvailableListener;

        public RendererHandler(@NonNull Looper looper) {
            super(looper);
            this.mOnFrameAvailableListener = new SurfaceTexture.OnFrameAvailableListener() { // from class: com.xiaopeng.xuiservice.uvccamera.opengl.renderer.RendererHolder.RendererHandler.1
                @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
                public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                    RendererHandler.this.removeMessages(2);
                    if (!RendererHolder.this.mIsFirstFrameRendered) {
                        RendererHolder.this.makeCurrent();
                        RendererHolder.this.mIsFirstFrameRendered = true;
                    }
                    RendererHandler.this.sendEmptyMessage(2);
                }
            };
        }

        @Override // android.os.Handler
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int i = msg.what;
            if (i != 99) {
                switch (i) {
                    case 1:
                        handleInit();
                        return;
                    case 2:
                        handleDraw();
                        return;
                    case 3:
                        handleUpdateSize(msg.arg1, msg.arg2);
                        return;
                    case 4:
                        handleAddSlaveSurface(msg.arg1, msg.obj, msg.arg2);
                        return;
                    case 5:
                        handleRemoveSlaveSurface(msg.arg1);
                        return;
                    case 6:
                        handleReCreatePrimarySurface();
                        return;
                    default:
                        switch (i) {
                            case 11:
                                handleClearSlaveSurface(msg.arg1, msg.arg2);
                                return;
                            case 12:
                                handleClearSlaveSurfaceAll(msg.arg1);
                                return;
                            case 13:
                                handleRemoveSlaveSurfaceAll();
                                return;
                            case 14:
                                handleReleasePrimarySurface();
                                return;
                            default:
                                return;
                        }
                }
            }
            handleRelease();
        }

        private void handleInit() {
            handleReCreatePrimarySurface();
            RendererHolder.this.mDrawer = new GLDrawer2D(true);
            Matrix.setIdentityM(RendererHolder.this.mMvpMatrix, 0);
            Matrix.setIdentityM(RendererHolder.this.mRotationMatrix, 0);
            Matrix.setIdentityM(RendererHolder.this.mMirrorMatrix, 0);
        }

        private void handleRelease() {
            if (RendererHolder.this.mDrawer != null) {
                RendererHolder.this.mDrawer.release();
                RendererHolder.this.mDrawer = null;
            }
            handleReleasePrimarySurface();
            handleRemoveSlaveSurfaceAll();
            RendererHolder.this.quitSafely();
        }

        protected void handleDraw() {
            if (RendererHolder.this.mPrimarySurface == null || !RendererHolder.this.mPrimarySurface.isValid()) {
                Log.e(RendererHolder.TAG, "checkPrimarySurface:invalid primary surface");
                sendEmptyMessage(6);
                return;
            }
            if (RendererHolder.this.mIsFirstFrameRendered) {
                try {
                    RendererHolder.this.mPrimaryTexture.updateTexImage();
                    RendererHolder.this.mPrimaryTexture.getTransformMatrix(RendererHolder.this.mTexMatrix);
                    handleDrawSlaveSurfaces();
                    RendererHolder.this.onFrameAvailable();
                } catch (Exception e) {
                    String str = RendererHolder.TAG;
                    Log.e(str, "draw:thread id =" + Thread.currentThread().getId(), e);
                    sendEmptyMessage(6);
                    return;
                }
            }
            GLES20.glClear(16384);
            GLES20.glFlush();
        }

        protected void handleDrawSlaveSurfaces() {
            synchronized (RendererHolder.this.mSlaveSurfaces) {
                int n = RendererHolder.this.mSlaveSurfaces.size();
                for (int i = n - 1; i >= 0; i--) {
                    RendererSurface slaveSurface = (RendererSurface) RendererHolder.this.mSlaveSurfaces.valueAt(i);
                    if (slaveSurface != null && slaveSurface.canDraw()) {
                        try {
                            RendererHolder.this.onDrawSlaveSurface(slaveSurface, RendererHolder.this.mTexId, RendererHolder.this.mTexMatrix, RendererHolder.this.mMvpMatrix);
                        } catch (Exception e) {
                            Log.e(RendererHolder.TAG, "onDrawSlaveSurface:", e);
                        }
                    }
                }
            }
        }

        protected void handleAddSlaveSurface(int id, Object surface, int maxFps) {
            String str = RendererHolder.TAG;
            LogUtil.v(str, "handleAddSurface:id=" + id);
            checkSurface();
            synchronized (RendererHolder.this.mSlaveSurfaces) {
                RendererSurface slaveSurface = (RendererSurface) RendererHolder.this.mSlaveSurfaces.get(id);
                if (slaveSurface == null) {
                    try {
                        RendererSurface slaveSurface2 = RendererSurface.newInstance(RendererHolder.this.getEgl(), surface, maxFps);
                        RendererHolder.this.mSlaveSurfaces.append(id, slaveSurface2);
                    } catch (Exception e) {
                        String str2 = RendererHolder.TAG;
                        Log.e(str2, "invalid surface: surface=" + surface, e);
                    }
                } else {
                    String str3 = RendererHolder.TAG;
                    Log.w(str3, "surface is already added: id=" + id);
                }
                RendererHolder.this.mSlaveSurfaces.notifyAll();
            }
            RendererHolder.this.makeCurrent();
        }

        protected void handleRemoveSlaveSurface(int id) {
            String str = RendererHolder.TAG;
            LogUtil.v(str, "handleRemoveSurface:id=" + id);
            synchronized (RendererHolder.this.mSlaveSurfaces) {
                RendererSurface slaveSurface = (RendererSurface) RendererHolder.this.mSlaveSurfaces.get(id);
                if (slaveSurface != null) {
                    RendererHolder.this.mSlaveSurfaces.remove(id);
                    if (slaveSurface.isValid()) {
                        slaveSurface.clear(0);
                    }
                    slaveSurface.release();
                }
                RendererHolder.this.mSlaveSurfaces.notifyAll();
            }
            checkSurface();
            RendererHolder.this.makeCurrent();
        }

        protected void handleRemoveSlaveSurfaceAll() {
            LogUtil.v(RendererHolder.TAG, "handleRemoveSurfaceAll:");
            synchronized (RendererHolder.this.mSlaveSurfaces) {
                int n = RendererHolder.this.mSlaveSurfaces.size();
                for (int i = 0; i < n; i++) {
                    RendererSurface slaveSurface = (RendererSurface) RendererHolder.this.mSlaveSurfaces.valueAt(i);
                    if (slaveSurface != null) {
                        if (slaveSurface.isValid()) {
                            slaveSurface.clear(0);
                        }
                        slaveSurface.release();
                    }
                }
                RendererHolder.this.mSlaveSurfaces.clear();
                RendererHolder.this.mSlaveSurfaces.notifyAll();
            }
            RendererHolder.this.makeCurrent();
        }

        protected void checkSurface() {
            LogUtil.v(RendererHolder.TAG, "checkSurface");
            synchronized (RendererHolder.this.mSlaveSurfaces) {
                List<Integer> removeIndexList = new ArrayList<>();
                int n = RendererHolder.this.mSlaveSurfaces.size();
                for (int i = 0; i < n; i++) {
                    RendererSurface slaveSurface = (RendererSurface) RendererHolder.this.mSlaveSurfaces.valueAt(i);
                    if (slaveSurface != null && !slaveSurface.isValid()) {
                        removeIndexList.add(Integer.valueOf(i));
                    }
                }
                for (Integer num : removeIndexList) {
                    int index = num.intValue();
                    ((RendererSurface) RendererHolder.this.mSlaveSurfaces.valueAt(index)).release();
                    RendererHolder.this.mSlaveSurfaces.removeAt(index);
                }
            }
        }

        protected void handleClearSlaveSurface(int id, int color) {
            synchronized (RendererHolder.this.mSlaveSurfaces) {
                RendererSurface slaveSurface = (RendererSurface) RendererHolder.this.mSlaveSurfaces.get(id);
                if (slaveSurface != null && slaveSurface.isValid()) {
                    slaveSurface.clear(color);
                }
            }
        }

        protected void handleClearSlaveSurfaceAll(int color) {
            synchronized (RendererHolder.this.mSlaveSurfaces) {
                int n = RendererHolder.this.mSlaveSurfaces.size();
                for (int i = 0; i < n; i++) {
                    RendererSurface slaveSurface = (RendererSurface) RendererHolder.this.mSlaveSurfaces.valueAt(i);
                    if (slaveSurface != null && slaveSurface.isValid()) {
                        slaveSurface.clear(color);
                    }
                }
            }
        }

        protected void handleReCreatePrimarySurface() {
            RendererHolder.this.mLock.lock();
            try {
                handleReleasePrimarySurface();
                RendererHolder.this.mTexId = GLHelper.initTex(ShaderConst.GL_TEXTURE_EXTERNAL_OES, 9728);
                RendererHolder.this.mPrimaryTexture = new SurfaceTexture(RendererHolder.this.mTexId);
                RendererHolder.this.mPrimarySurface = new Surface(RendererHolder.this.mPrimaryTexture);
                if (Build.VERSION.SDK_INT >= 15) {
                    RendererHolder.this.mPrimaryTexture.setDefaultBufferSize(RendererHolder.this.mVideoWidth, RendererHolder.this.mVideoHeight);
                }
                RendererHolder.this.mIsFirstFrameRendered = false;
                RendererHolder.this.mPrimaryTexture.setOnFrameAvailableListener(this.mOnFrameAvailableListener);
                RendererHolder.this.makeCurrent();
                RendererHolder.this.mCreatePrimarySurfaceCondition.signalAll();
                RendererHolder.this.onPrimarySurfaceCreate(RendererHolder.this.mPrimarySurface);
            } finally {
                RendererHolder.this.mLock.unlock();
            }
        }

        protected void handleReleasePrimarySurface() {
            RendererHolder.this.makeCurrent();
            if (RendererHolder.this.mPrimarySurface != null) {
                try {
                    RendererHolder.this.mPrimarySurface.release();
                } catch (Exception e) {
                    Log.w(RendererHolder.TAG, e);
                }
                RendererHolder.this.mPrimarySurface = null;
                RendererHolder.this.onPrimarySurfaceDestroy();
            }
            if (RendererHolder.this.mPrimaryTexture != null) {
                try {
                    RendererHolder.this.mPrimaryTexture.release();
                } catch (Exception e2) {
                    Log.w(RendererHolder.TAG, e2);
                }
                RendererHolder.this.mPrimaryTexture = null;
            }
            if (RendererHolder.this.mTexId != 0) {
                GLHelper.deleteTex(RendererHolder.this.mTexId);
                RendererHolder.this.mTexId = 0;
            }
            RendererHolder.this.makeCurrent();
        }

        protected void handleUpdateSize(int width, int height) {
            RendererHolder rendererHolder = RendererHolder.this;
            rendererHolder.mVideoWidth = width;
            rendererHolder.mVideoHeight = height;
            rendererHolder.makeCurrent();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void setMirrorMode(float[] mvp, int mode) {
        if (mode == 0) {
            mvp[0] = Math.abs(mvp[0]);
            mvp[5] = Math.abs(mvp[5]);
        } else if (mode == 1) {
            mvp[0] = -Math.abs(mvp[0]);
            mvp[5] = Math.abs(mvp[5]);
        } else if (mode == 2) {
            mvp[0] = Math.abs(mvp[0]);
            mvp[5] = -Math.abs(mvp[5]);
        } else if (mode == 3) {
            mvp[0] = -Math.abs(mvp[0]);
            mvp[5] = -Math.abs(mvp[5]);
        }
    }
}
