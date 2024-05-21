package com.xiaopeng.xuiservice.uvccamera.opengl;

import android.graphics.Bitmap;
import java.io.IOException;
/* loaded from: classes5.dex */
public interface ITexture {
    void bind();

    int getTexHeight();

    void getTexMatrix(float[] fArr, int i);

    float[] getTexMatrix();

    int getTexTarget();

    int getTexWidth();

    int getTexture();

    void loadTexture(Bitmap bitmap) throws NullPointerException;

    void loadTexture(String str) throws NullPointerException, IOException;

    void release();

    void unbind();
}
