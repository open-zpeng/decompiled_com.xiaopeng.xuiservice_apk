package com.xiaopeng.speech.protocol.query.media;

import com.xiaopeng.speech.SpeechQuery;
import com.xiaopeng.speech.annotation.QueryAnnotation;
import com.xiaopeng.speech.protocol.event.query.QueryMediaEvent;
/* loaded from: classes2.dex */
public class MediaVideoQuery extends SpeechQuery<IMediaVideoQueryCaller> {
    /* JADX INFO: Access modifiers changed from: protected */
    @QueryAnnotation(event = QueryMediaEvent.GET_MEDIAZ_VIDEO_INFO_QUERY)
    public String getMediaVideoInfo(String event, String data) {
        return ((IMediaVideoQueryCaller) this.mQueryCaller).getMediaVideoInfo();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @QueryAnnotation(event = QueryMediaEvent.DEMAND_VIDEO_APP)
    public String getDemandApp(String event, String data) {
        return ((IMediaVideoQueryCaller) this.mQueryCaller).getDemandApp(data);
    }
}
