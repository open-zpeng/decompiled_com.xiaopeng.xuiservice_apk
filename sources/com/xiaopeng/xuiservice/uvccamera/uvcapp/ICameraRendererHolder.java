package com.xiaopeng.xuiservice.uvccamera.uvcapp;

import com.xiaopeng.xuiservice.uvccamera.opengl.renderer.IRendererHolder;
/* loaded from: classes5.dex */
interface ICameraRendererHolder extends IRendererHolder {

    /* loaded from: classes5.dex */
    public interface OnImageCapturedCallback {
        void onCaptureSuccess(ImageRawData imageRawData);
    }

    void captureImage(OnImageCapturedCallback onImageCapturedCallback);
}
