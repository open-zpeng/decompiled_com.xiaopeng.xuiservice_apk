package com.xiaopeng.xuiservice.xapp.mode.effect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes5.dex */
public class EffectConfig {
    public static final int DEFAULT_EFFECT_INDEX = 1;
    public static final int HOT_EFFECT_INDEX = 0;
    public static final int INDEX_EFFECT_ELECTRIC = 8;
    public static final int INDEX_EFFECT_FEMALE = 9;
    public static final int INDEX_EFFECT_FOLK = 4;
    public static final int INDEX_EFFECT_KTV = 0;
    public static final int INDEX_EFFECT_LIVE = 1;
    public static final int INDEX_EFFECT_MALE = 10;
    public static final int INDEX_EFFECT_POP = 3;
    public static final int INDEX_EFFECT_PROFESSION = 2;
    public static final int INDEX_EFFECT_RAP = 5;
    public static final int INDEX_EFFECT_SURROUND = 14;
    public static final int[] SHOW_EFFECT_ARRAY = {14, 0, 2, 5, 1, 4, 10, 9, 8};
    private static final String TAG = "EffectConfig";
    public static final int TYPE_EFFECT = 4;

    public static List<EffectItem> getEffectList(HashMap<String, Integer> source) {
        List<EffectItem> effectItems = new ArrayList<>();
        for (int i = 0; i < SHOW_EFFECT_ARRAY.length; i++) {
            for (Map.Entry<String, Integer> entry : source.entrySet()) {
                if (entry.getValue().intValue() == SHOW_EFFECT_ARRAY[i]) {
                    effectItems.add(new EffectItem(entry.getValue().intValue(), entry.getKey()));
                }
            }
        }
        return effectItems;
    }

    public static String getEffectInfo(HashMap<String, Integer> source) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Integer> entry : source.entrySet()) {
            builder.append(entry.getValue());
            builder.append(":");
            builder.append(entry.getKey());
            builder.append("|");
        }
        return builder.toString();
    }
}
