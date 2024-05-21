package com.loostone.libtuning.inf.extern;
/* loaded from: classes4.dex */
public interface IAudioTempoProcess {
    void deinit();

    int getBrightness();

    int getColor();

    boolean getColorChangeOrNot();

    void init();

    void initSong();

    void setColors(int[] iArr);

    void setListener(ITempoCallBack iTempoCallBack);

    void setMode(int i, int i2);
}
