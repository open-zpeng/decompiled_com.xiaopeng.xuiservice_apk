package com.xiaopeng.xuiservice.uvccamera.uvcapp;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import android.view.Surface;
import androidx.annotation.Nullable;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.uvccamera.opengl.EGLBase;
import com.xiaopeng.xuiservice.uvccamera.opengl.GLDrawer2D;
import com.xiaopeng.xuiservice.uvccamera.opengl.renderer.RendererHolder;
import com.xiaopeng.xuiservice.uvccamera.opengl.renderer.RendererHolderCallback;
import com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraRendererHolder;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes5.dex */
public class CameraRendererHolder extends RendererHolder implements ICameraRendererHolder {
    private static final String TAG = CameraRendererHolder.class.getSimpleName();
    private CaptureHolder mCaptureHolder;

    public CameraRendererHolder(int width, int height, @Nullable RendererHolderCallback callback) {
        this(width, height, null, 2, 3, callback);
    }

    public CameraRendererHolder(int width, int height, EGLBase.IContext sharedContext, int flags, int maxClientVersion, @Nullable RendererHolderCallback callback) {
        super(width, height, sharedContext, flags, maxClientVersion, callback);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.renderer.RendererHolder
    public void onPrimarySurfaceCreate(Surface surface) {
        super.onPrimarySurfaceCreate(surface);
        this.mRendererHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.-$$Lambda$CameraRendererHolder$7mSMlJkFiOw2xw-9KpwsknenaUA
            @Override // java.lang.Runnable
            public final void run() {
                CameraRendererHolder.this.lambda$onPrimarySurfaceCreate$0$CameraRendererHolder();
            }
        });
    }

    public /* synthetic */ void lambda$onPrimarySurfaceCreate$0$CameraRendererHolder() {
        this.mCaptureHolder = new CaptureHolder();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.renderer.RendererHolder
    public void onPrimarySurfaceDestroy() {
        super.onPrimarySurfaceDestroy();
        this.mRendererHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.-$$Lambda$CameraRendererHolder$9ebRm0SL7zAVzKMqb6padiguk3M
            @Override // java.lang.Runnable
            public final void run() {
                CameraRendererHolder.this.lambda$onPrimarySurfaceDestroy$1$CameraRendererHolder();
            }
        });
    }

    public /* synthetic */ void lambda$onPrimarySurfaceDestroy$1$CameraRendererHolder() {
        CaptureHolder captureHolder = this.mCaptureHolder;
        if (captureHolder != null) {
            captureHolder.release();
            this.mCaptureHolder = null;
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.uvcapp.ICameraRendererHolder
    public void captureImage(final ICameraRendererHolder.OnImageCapturedCallback callback) {
        this.mRendererHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.uvccamera.uvcapp.-$$Lambda$CameraRendererHolder$Ef2l1D8ARsI2ifPEY8hm2QvqYaw
            @Override // java.lang.Runnable
            public final void run() {
                CameraRendererHolder.this.lambda$captureImage$2$CameraRendererHolder(callback);
            }
        });
    }

    public /* synthetic */ void lambda$captureImage$2$CameraRendererHolder(ICameraRendererHolder.OnImageCapturedCallback callback) {
        ImageRawData data = this.mCaptureHolder.captureImageRawData();
        callback.onCaptureSuccess(data);
    }

    /* loaded from: classes5.dex */
    private class CaptureHolder {
        EGLBase mCaptureEglBase;
        EGLBase.IEglSurface mCaptureSurface;
        int mWidth = -1;
        int mHeight = -1;
        ByteBuffer mBuf = null;
        GLDrawer2D mCaptureDrawer = new GLDrawer2D(true);

        public CaptureHolder() {
            this.mCaptureEglBase = EGLBase.createFrom(CameraRendererHolder.this.getContext(), 3, false, 0, false);
            this.mCaptureSurface = this.mCaptureEglBase.createOffscreen(CameraRendererHolder.this.mVideoWidth, CameraRendererHolder.this.mVideoHeight);
        }

        public ImageRawData captureImageRawData() {
            LogUtil.v(CameraRendererHolder.TAG, "#captureImageData:start");
            ImageRawData data = null;
            if (this.mBuf == null || this.mWidth != CameraRendererHolder.this.mVideoWidth || this.mHeight != CameraRendererHolder.this.mVideoHeight) {
                this.mWidth = CameraRendererHolder.this.mVideoWidth;
                this.mHeight = CameraRendererHolder.this.mVideoHeight;
                this.mBuf = ByteBuffer.allocateDirect(this.mWidth * this.mHeight * 4);
                this.mBuf.order(ByteOrder.LITTLE_ENDIAN);
                EGLBase.IEglSurface iEglSurface = this.mCaptureSurface;
                if (iEglSurface != null) {
                    iEglSurface.release();
                    this.mCaptureSurface = null;
                }
                this.mCaptureSurface = this.mCaptureEglBase.createOffscreen(this.mWidth, this.mHeight);
            }
            if (this.mWidth <= 0 || this.mHeight <= 0) {
                Log.w(CameraRendererHolder.TAG, "#captureImageData:unexpectedly width/height is zero");
            } else {
                float[] mvpMatrix = Arrays.copyOf(CameraRendererHolder.this.mMvpMatrix, 16);
                float[] mirrorMatrix = new float[16];
                Matrix.setIdentityM(mirrorMatrix, 0);
                CameraRendererHolder.setMirrorMode(mirrorMatrix, 2);
                Matrix.multiplyMM(mvpMatrix, 0, mirrorMatrix, 0, mvpMatrix, 0);
                this.mCaptureDrawer.setMvpMatrix(mvpMatrix, 0);
                this.mCaptureSurface.makeCurrent();
                this.mCaptureDrawer.draw(CameraRendererHolder.this.mTexId, CameraRendererHolder.this.mTexMatrix, 0);
                this.mCaptureSurface.swap();
                this.mBuf.clear();
                GLES20.glReadPixels(0, 0, this.mWidth, this.mHeight, 6408, 5121, this.mBuf);
                CameraRendererHolder.this.makeCurrent();
                byte[] bytes = new byte[this.mBuf.capacity()];
                this.mBuf.rewind();
                this.mBuf.get(bytes);
                data = new ImageRawData(bytes, this.mWidth, this.mHeight);
            }
            LogUtil.i(CameraRendererHolder.TAG, "#captureImageData:end");
            return data;
        }

        public void release() {
            GLDrawer2D gLDrawer2D = this.mCaptureDrawer;
            if (gLDrawer2D != null) {
                gLDrawer2D.release();
                this.mCaptureDrawer = null;
            }
            EGLBase.IEglSurface iEglSurface = this.mCaptureSurface;
            if (iEglSurface != null) {
                iEglSurface.makeCurrent();
                this.mCaptureSurface.release();
                this.mCaptureSurface = null;
            }
            EGLBase eGLBase = this.mCaptureEglBase;
            if (eGLBase != null) {
                eGLBase.release();
                this.mCaptureEglBase = null;
            }
        }
    }
}
