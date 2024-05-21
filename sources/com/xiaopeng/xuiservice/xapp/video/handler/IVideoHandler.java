package com.xiaopeng.xuiservice.xapp.video.handler;

import com.xiaopeng.xuiservice.xapp.video.Callback;
/* loaded from: classes5.dex */
public interface IVideoHandler {
    void backward(Callback callback);

    void clarityDown(Callback callback);

    void claritySet(String str, Callback callback);

    void clarityUP(Callback callback);

    void collect(Callback callback);

    void collectCancel(Callback callback);

    void end(Callback callback);

    void forward(Callback callback);

    String getPackageName();

    void next(Callback callback);

    void pause(Callback callback);

    void play(int i, String str, Callback callback);

    void play(int i, String str, String str2, Callback callback);

    void prev(Callback callback);

    void resume(Callback callback);

    void set(int i, Callback callback);

    void setMediaPlayMode(Callback callback);

    void setTime(double d, Callback callback);

    void setVideoPlayMode(Callback callback);

    void skipBegin(Callback callback);

    void skipEnd(Callback callback);

    void speedDown(Callback callback);

    void speedSet(double d, Callback callback);

    void speedUp(Callback callback);
}
