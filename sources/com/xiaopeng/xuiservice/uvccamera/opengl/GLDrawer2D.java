package com.xiaopeng.xuiservice.uvccamera.opengl;

import android.opengl.GLES20;
import android.opengl.Matrix;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
/* loaded from: classes5.dex */
public class GLDrawer2D implements IDrawer2D {
    private static final int FLOAT_SZ = 4;
    private final int VERTEX_NUM;
    private final int VERTEX_SZ;
    private int hProgram;
    private final float[] mMvpMatrix;
    private final int mTexTarget;
    int maPositionLoc;
    int maTextureCoordLoc;
    int muMVPMatrixLoc;
    int muTexMatrixLoc;
    private final FloatBuffer pTexCoord;
    private final FloatBuffer pVertex;
    private static final float[] VERTICES = {1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, -1.0f};
    private static final float[] TEXCOORD = {1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f};

    public GLDrawer2D(boolean isOES) {
        this(VERTICES, TEXCOORD, isOES);
    }

    public GLDrawer2D(float[] vertices, float[] texcoord, boolean isOES) {
        this.mMvpMatrix = new float[16];
        this.VERTEX_NUM = Math.min(vertices != null ? vertices.length : 0, texcoord != null ? texcoord.length : 0) / 2;
        this.VERTEX_SZ = this.VERTEX_NUM * 2;
        this.mTexTarget = isOES ? ShaderConst.GL_TEXTURE_EXTERNAL_OES : ShaderConst.GL_TEXTURE_2D;
        this.pVertex = ByteBuffer.allocateDirect(this.VERTEX_SZ * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.pVertex.put(vertices);
        this.pVertex.flip();
        this.pTexCoord = ByteBuffer.allocateDirect(this.VERTEX_SZ * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.pTexCoord.put(texcoord);
        this.pTexCoord.flip();
        if (isOES) {
            this.hProgram = GLHelper.loadShader(ShaderConst.VERTEX_SHADER, ShaderConst.FRAGMENT_SHADER_SIMPLE_OES);
        } else {
            this.hProgram = GLHelper.loadShader(ShaderConst.VERTEX_SHADER, ShaderConst.FRAGMENT_SHADER_SIMPLE);
        }
        Matrix.setIdentityM(this.mMvpMatrix, 0);
        init();
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.IDrawer2D
    public void release() {
        int i = this.hProgram;
        if (i >= 0) {
            GLES20.glDeleteProgram(i);
        }
        this.hProgram = -1;
    }

    public boolean isOES() {
        return this.mTexTarget == 36197;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.IDrawer2D
    public float[] getMvpMatrix() {
        return this.mMvpMatrix;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.IDrawer2D
    public IDrawer2D setMvpMatrix(float[] matrix, int offset) {
        System.arraycopy(matrix, offset, this.mMvpMatrix, 0, 16);
        return this;
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.IDrawer2D
    public void getMvpMatrix(float[] matrix, int offset) {
        System.arraycopy(this.mMvpMatrix, 0, matrix, offset, 16);
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.IDrawer2D
    public synchronized void draw(int texId, float[] tex_matrix, int offset) {
        if (this.hProgram < 0) {
            return;
        }
        GLES20.glUseProgram(this.hProgram);
        if (tex_matrix != null) {
            GLES20.glUniformMatrix4fv(this.muTexMatrixLoc, 1, false, tex_matrix, offset);
        }
        GLES20.glUniformMatrix4fv(this.muMVPMatrixLoc, 1, false, this.mMvpMatrix, 0);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(this.mTexTarget, texId);
        GLES20.glDrawArrays(5, 0, this.VERTEX_NUM);
        GLES20.glBindTexture(this.mTexTarget, 0);
        GLES20.glUseProgram(0);
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.IDrawer2D
    public void draw(ITexture texture) {
        draw(texture.getTexture(), texture.getTexMatrix(), 0);
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.IDrawer2D
    public void draw(TextureOffscreen offscreen) {
        draw(offscreen.getTexture(), offscreen.getTexMatrix(), 0);
    }

    public int initTex() {
        return GLHelper.initTex(this.mTexTarget, 9728);
    }

    public void deleteTex(int hTex) {
        GLHelper.deleteTex(hTex);
    }

    public synchronized void updateShader(String vs, String fs) {
        release();
        this.hProgram = GLHelper.loadShader(vs, fs);
        init();
    }

    public void updateShader(String fs) {
        updateShader(ShaderConst.VERTEX_SHADER, fs);
    }

    public void resetShader() {
        release();
        if (isOES()) {
            this.hProgram = GLHelper.loadShader(ShaderConst.VERTEX_SHADER, ShaderConst.FRAGMENT_SHADER_SIMPLE_OES);
        } else {
            this.hProgram = GLHelper.loadShader(ShaderConst.VERTEX_SHADER, ShaderConst.FRAGMENT_SHADER_SIMPLE);
        }
        init();
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.IDrawer2D
    public int glGetAttribLocation(String name) {
        GLES20.glUseProgram(this.hProgram);
        return GLES20.glGetAttribLocation(this.hProgram, name);
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.IDrawer2D
    public int glGetUniformLocation(String name) {
        GLES20.glUseProgram(this.hProgram);
        return GLES20.glGetUniformLocation(this.hProgram, name);
    }

    @Override // com.xiaopeng.xuiservice.uvccamera.opengl.IDrawer2D
    public void glUseProgram() {
        GLES20.glUseProgram(this.hProgram);
    }

    private void init() {
        GLES20.glUseProgram(this.hProgram);
        this.maPositionLoc = GLES20.glGetAttribLocation(this.hProgram, "aPosition");
        this.maTextureCoordLoc = GLES20.glGetAttribLocation(this.hProgram, "aTextureCoord");
        this.muMVPMatrixLoc = GLES20.glGetUniformLocation(this.hProgram, "uMVPMatrix");
        this.muTexMatrixLoc = GLES20.glGetUniformLocation(this.hProgram, "uTexMatrix");
        GLES20.glUniformMatrix4fv(this.muMVPMatrixLoc, 1, false, this.mMvpMatrix, 0);
        GLES20.glUniformMatrix4fv(this.muTexMatrixLoc, 1, false, this.mMvpMatrix, 0);
        GLES20.glVertexAttribPointer(this.maPositionLoc, 2, 5126, false, this.VERTEX_SZ, (Buffer) this.pVertex);
        GLES20.glVertexAttribPointer(this.maTextureCoordLoc, 2, 5126, false, this.VERTEX_SZ, (Buffer) this.pTexCoord);
        GLES20.glEnableVertexAttribArray(this.maPositionLoc);
        GLES20.glEnableVertexAttribArray(this.maTextureCoordLoc);
    }
}
