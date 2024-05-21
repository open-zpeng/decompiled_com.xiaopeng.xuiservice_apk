package com.loostone.libserver.version1.entity.request.machine;

import java.util.List;
/* loaded from: classes4.dex */
public class Cards {
    private String id;
    private List<PcmInfo> pcmInfoList;

    public String getId() {
        return this.id;
    }

    public List<PcmInfo> getPcmInfoList() {
        return this.pcmInfoList;
    }

    public void setId(String str) {
        this.id = str;
    }

    public void setPcmInfoList(List<PcmInfo> list) {
        this.pcmInfoList = list;
    }
}
