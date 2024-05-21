package com.loostone.libtuning.channel.skyworth.old.ai;

import java.util.List;
/* loaded from: classes4.dex */
public class KaraokeApp {
    public static int MODE_DFT = 1;
    public static int MODE_DFT_INSTALL = 2;
    public static int MODE_FIRST = 3;
    public static int MODE_FIRST_INSTALL = 4;
    private String firstPackage;
    private List<String> supportList;
    private int type = 1;

    public String getFirstPackage() {
        return this.firstPackage;
    }

    public List<String> getSupportList() {
        return this.supportList;
    }

    public int getType() {
        return this.type;
    }

    public void setFirstPackage(String str) {
        this.firstPackage = str;
    }

    public void setSupportList(List<String> list) {
        this.supportList = list;
    }

    public void setType(int i) {
        this.type = i;
    }
}
