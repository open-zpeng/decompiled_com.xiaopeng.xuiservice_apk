package com.xiaopeng.xuiservice.xapp.mode.octopus.bean;

import java.io.Serializable;
/* loaded from: classes5.dex */
public class KeyMapInfo implements Serializable {
    public String desc;
    public int keycode;
    public int x;
    public int y;

    public String toString() {
        return "KeyMapInfo{keycode=" + this.keycode + ", desc='" + this.desc + "', x=" + this.x + ", y=" + this.y + '}';
    }
}
