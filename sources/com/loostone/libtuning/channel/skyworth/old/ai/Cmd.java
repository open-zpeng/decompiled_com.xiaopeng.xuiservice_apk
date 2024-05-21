package com.loostone.libtuning.channel.skyworth.old.ai;

import java.io.Serializable;
/* loaded from: classes4.dex */
public class Cmd implements Serializable {
    private String Song;
    private String cmd;
    private String page;
    private String singer;
    private String value;

    public String getCmd() {
        return this.cmd;
    }

    public String getPage() {
        return this.page;
    }

    public String getSinger() {
        return this.singer;
    }

    public String getSong() {
        return this.Song;
    }

    public String getValue() {
        return this.value;
    }

    public void setCmd(String str) {
        this.cmd = str;
    }

    public void setPage(String str) {
        this.page = str;
    }

    public void setSinger(String str) {
        this.singer = str;
    }

    public void setSong(String str) {
        this.Song = str;
    }

    public void setValue(String str) {
        this.value = str;
    }
}
