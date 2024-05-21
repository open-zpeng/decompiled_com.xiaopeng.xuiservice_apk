package com.xiaopeng.xuiservice.musicrecognize;

import com.xiaopeng.xuimanager.musicrecognize.MusicRecognizeEvent;
/* loaded from: classes5.dex */
public interface MusicRecognizeEngine {
    public static final int MODE_HUMMING = 2;
    public static final int MODE_ORIGINAL_SOUND = 1;

    /* loaded from: classes5.dex */
    public interface Callback {
        void onFail(String str);

        void onSuccess(MusicRecognizeEvent musicRecognizeEvent);

        void onVolumeChanged(double d);
    }

    float getMinScore();

    int getMode();

    void setCallback(Callback callback);

    void setMinScore(float f);

    void setMode(int i);

    void start();

    void stop();

    void stopAndRecognize();
}
