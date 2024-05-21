package com.xiaopeng.speech.protocol.query.media;

import com.xiaopeng.speech.IQueryCaller;
/* loaded from: classes2.dex */
public interface IMediaVideoQueryCaller extends IQueryCaller {
    default String getMediaVideoInfo() {
        return null;
    }

    default String getDemandApp(String data) {
        return null;
    }
}
