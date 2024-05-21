package com.xiaopeng.xuiservice.xapp.mode.octopus.bean;

import android.graphics.Rect;
import java.util.List;
/* loaded from: classes5.dex */
public class KeyMapInfos {
    private String configName;
    private List<KeyMapInfo> keyMap;
    private Rect touchRect;

    public String getConfigName() {
        return this.configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public Rect getTouchRect() {
        return this.touchRect;
    }

    public void setTouchRect(Rect touchRect) {
        this.touchRect = touchRect;
    }

    public List<KeyMapInfo> getKeyMap() {
        return this.keyMap;
    }

    public void setKeyMap(List<KeyMapInfo> keyMap) {
        this.keyMap = keyMap;
    }
}
