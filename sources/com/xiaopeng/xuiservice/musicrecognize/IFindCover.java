package com.xiaopeng.xuiservice.musicrecognize;
/* loaded from: classes5.dex */
public interface IFindCover {

    /* loaded from: classes5.dex */
    public interface Callback {
        void findResult(String str);
    }

    void findSongCover(String str, Callback callback);
}
