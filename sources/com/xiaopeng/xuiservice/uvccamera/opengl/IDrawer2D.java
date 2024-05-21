package com.xiaopeng.xuiservice.uvccamera.opengl;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes5.dex */
public interface IDrawer2D {
    void draw(int i, float[] fArr, int i2);

    void draw(ITexture iTexture);

    void draw(TextureOffscreen textureOffscreen);

    void getMvpMatrix(float[] fArr, int i);

    float[] getMvpMatrix();

    int glGetAttribLocation(String str);

    int glGetUniformLocation(String str);

    void glUseProgram();

    void release();

    IDrawer2D setMvpMatrix(float[] fArr, int i);
}
