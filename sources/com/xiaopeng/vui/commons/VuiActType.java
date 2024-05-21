package com.xiaopeng.vui.commons;

import com.loostone.libtuning.channel.skyworth.old.ai.config.AiCmd;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public enum VuiActType {
    SEARCH(AiCmd.SEARCH),
    SELECT("Select"),
    EDIT("Edit"),
    OPEN("Open"),
    DELETE("Delete"),
    DETAIL("Detail"),
    EXPANDFOLD("ExpandFold"),
    ROLL("Roll"),
    TAB("Tab"),
    SELECTTAB("SelectTab"),
    SLIDE("Slide"),
    UP("Up"),
    DOWN("Down"),
    LEFT("Left"),
    RIGHT("Right"),
    SET("Set"),
    SORT("Sort"),
    EXPAND("Expand"),
    ADD("Add"),
    PLAY(AiCmd.PLAY),
    NULL("Null");
    
    private String type;

    VuiActType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static List<String> getVuiActTypeList() {
        VuiActType[] values;
        List<String> actTypeList = new ArrayList<>();
        for (VuiActType actType : values()) {
            actTypeList.add(actType.getType());
        }
        return actTypeList;
    }
}
