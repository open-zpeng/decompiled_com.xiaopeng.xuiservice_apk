package com.xiaopeng.xuiservice.xapp.mode.effect;

import android.util.SparseIntArray;
import com.xiaopeng.xuiservice.R;
/* loaded from: classes5.dex */
public class EffectResources {
    private static SparseIntArray mEffectBgResources = new SparseIntArray();
    private static SparseIntArray mEffectBorderResources = new SparseIntArray();
    private static SparseIntArray mAvatarBgResources = new SparseIntArray();
    private static SparseIntArray mAvatarDesResources = new SparseIntArray();

    static {
        mEffectBgResources.put(0, R.drawable.bg_effect_ktv);
        mEffectBgResources.put(3, R.drawable.bg_effect_pop);
        mEffectBgResources.put(2, R.drawable.bg_effect_profession);
        mEffectBgResources.put(8, R.drawable.bg_effect_electric);
        mEffectBgResources.put(9, R.drawable.bg_effect_female);
        mEffectBgResources.put(4, R.drawable.bg_effect_folk);
        mEffectBgResources.put(5, R.drawable.bg_effect_rap);
        mEffectBgResources.put(10, R.drawable.bg_effect_male);
        mEffectBgResources.put(14, R.drawable.bg_effect_surround);
        mEffectBgResources.put(1, R.drawable.bg_effect_live);
        mAvatarBgResources.put(0, R.drawable.bg_effect_avatar_default);
        mAvatarBgResources.put(14, R.drawable.bg_effect_avatar_hot);
        mAvatarDesResources.put(0, R.string.effect_item_default);
        mAvatarDesResources.put(14, R.string.effect_item_hot);
        mEffectBorderResources.put(0, R.drawable.bg_effect_ktv_border);
        mEffectBorderResources.put(3, R.drawable.bg_effect_pop_border);
        mEffectBorderResources.put(2, R.drawable.bg_effect_profession_border);
        mEffectBorderResources.put(8, R.drawable.bg_effect_electric_border);
        mEffectBorderResources.put(9, R.drawable.bg_effect_female_border);
        mEffectBorderResources.put(4, R.drawable.bg_effect_folk_border);
        mEffectBorderResources.put(5, R.drawable.bg_effect_rap_border);
        mEffectBorderResources.put(10, R.drawable.bg_effect_male_border);
        mEffectBorderResources.put(14, R.drawable.bg_effect_surround_border);
        mEffectBorderResources.put(1, R.drawable.bg_effect_live_border);
    }

    public static int getEffectBg(int index) {
        return mEffectBgResources.get(index, R.drawable.bg_effect_surround);
    }

    public static int getEffectBorder(int index) {
        return mEffectBorderResources.get(index, R.drawable.bg_effect_ktv_border);
    }

    public static int getAvatarBg(int index) {
        return mAvatarBgResources.get(index, R.drawable.bg_effect_avatar_default);
    }

    public static int getAvatarDes(int index) {
        return mAvatarDesResources.get(index, R.string.effect_item_default);
    }
}
