package com.xiaopeng.xuiservice.uvccamera.opengl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.text.TextUtils;
import java.io.IOException;
/* loaded from: classes5.dex */
public class GLTexture implements ITexture {
    int mImageHeight;
    int mImageWidth;
    int mTexHeight;
    final float[] mTexMatrix;
    int mTexWidth;
    int mTextureId;
    final int mTextureTarget;
    final int mTextureUnit;

    public GLTexture(int width, int height, int filter_param) {
        this(ShaderConst.GL_TEXTURE_2D, 33984, width, height, filter_param);
    }

    public GLTexture(int texTarget, int texUnit, int width, int height, int filter_param) {
        this.mTexMatrix = new float[16];
        this.mTextureTarget = texTarget;
        this.mTextureUnit = texUnit;
        int w = 32;
        while (w < width) {
            w <<= 1;
        }
        int h = 32;
        while (h < height) {
            h <<= 1;
        }
        if (this.mTexWidth != w || this.mTexHeight != h) {
            this.mTexWidth = w;
            this.mTexHeight = h;
        }
        this.mTextureId = GLHelper.initTex(this.mTextureTarget, filter_param);
        GLES20.glTexImage2D(this.mTextureTarget, 0, 6408, this.mTexWidth, this.mTexHeight, 0, 6408, 5121, null);
        Matrix.setIdentityM(this.mTexMatrix, 0);
        float[] fArr = this.mTexMatrix;
        fArr[0] = width / this.mTexWidth;
        fArr[5] = height / this.mTexHeight;
    }

    protected void finalize() throws Throwable {
        try {
            release();
        } finally {
            super.finalize();
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.ITexture
    public void release() {
        int i = this.mTextureId;
        if (i > 0) {
            GLHelper.deleteTex(i);
            this.mTextureId = 0;
        }
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.ITexture
    public void bind() {
        GLES20.glActiveTexture(this.mTextureUnit);
        GLES20.glBindTexture(this.mTextureTarget, this.mTextureId);
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.ITexture
    public void unbind() {
        GLES20.glActiveTexture(this.mTextureUnit);
        GLES20.glBindTexture(this.mTextureTarget, 0);
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.ITexture
    public int getTexTarget() {
        return this.mTextureTarget;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.ITexture
    public int getTexture() {
        return this.mTextureId;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.ITexture
    public float[] getTexMatrix() {
        return this.mTexMatrix;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.ITexture
    public void getTexMatrix(float[] matrix, int offset) {
        float[] fArr = this.mTexMatrix;
        System.arraycopy(fArr, 0, matrix, offset, fArr.length);
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.ITexture
    public int getTexWidth() {
        return this.mTexWidth;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.ITexture
    public int getTexHeight() {
        return this.mTexHeight;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.ITexture
    public void loadTexture(String filePath) throws NullPointerException, IOException {
        if (TextUtils.isEmpty(filePath)) {
            throw new NullPointerException("image file path should not be a null");
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;
        int inSampleSize = 1;
        if (imageHeight > this.mTexHeight || imageWidth > this.mTexWidth) {
            if (imageWidth > imageHeight) {
                inSampleSize = (int) Math.ceil(imageHeight / this.mTexHeight);
            } else {
                inSampleSize = (int) Math.ceil(imageWidth / this.mTexWidth);
            }
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        loadTexture(BitmapFactory.decodeFile(filePath, options));
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.ITexture
    public void loadTexture(Bitmap bitmap) throws NullPointerException {
        this.mImageWidth = bitmap.getWidth();
        this.mImageHeight = bitmap.getHeight();
        Bitmap texture = Bitmap.createBitmap(this.mTexWidth, this.mTexHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(texture);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        bitmap.recycle();
        Matrix.setIdentityM(this.mTexMatrix, 0);
        float[] fArr = this.mTexMatrix;
        fArr[0] = this.mImageWidth / this.mTexWidth;
        fArr[5] = this.mImageHeight / this.mTexHeight;
        bind();
        GLUtils.texImage2D(this.mTextureTarget, 0, texture, 0);
        unbind();
        texture.recycle();
    }
}
