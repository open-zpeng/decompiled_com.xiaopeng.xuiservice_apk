package com.xiaopeng.speech.protocol.query.media;

import com.xiaopeng.speech.annotation.IQueryProcessor;
import com.xiaopeng.speech.protocol.event.query.QueryMediaEvent;
/* loaded from: classes2.dex */
public class MediaVideoQuery_Processor implements IQueryProcessor {
    private MediaVideoQuery mTarget;

    public MediaVideoQuery_Processor(MediaVideoQuery target) {
        this.mTarget = target;
    }

    @Override // com.xiaopeng.speech.annotation.IQueryProcessor
    public Object querySensor(String event, String data) {
        char c;
        int hashCode = event.hashCode();
        if (hashCode != -941695413) {
            if (hashCode == -555849819 && event.equals(QueryMediaEvent.GET_MEDIAZ_VIDEO_INFO_QUERY)) {
                c = 0;
            }
            c = 65535;
        } else {
            if (event.equals(QueryMediaEvent.DEMAND_VIDEO_APP)) {
                c = 1;
            }
            c = 65535;
        }
        if (c != 0) {
            if (c == 1) {
                return this.mTarget.getDemandApp(event, data);
            }
            return null;
        }
        return this.mTarget.getMediaVideoInfo(event, data);
    }

    @Override // com.xiaopeng.speech.annotation.IQueryProcessor
    public String[] getQueryEvents() {
        return new String[]{QueryMediaEvent.GET_MEDIAZ_VIDEO_INFO_QUERY, QueryMediaEvent.DEMAND_VIDEO_APP};
    }
}
