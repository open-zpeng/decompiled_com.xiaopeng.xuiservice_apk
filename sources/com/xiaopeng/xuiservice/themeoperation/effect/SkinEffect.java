package com.xiaopeng.xuiservice.themeoperation.effect;

import com.xiaopeng.xuimanager.themeoperation.AbilityEffect;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.themeoperation.ThemeResManager;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes5.dex */
public class SkinEffect implements BaseEffect {
    private static final String EFFECT_TYPE = "skin";
    private static final String KEY_EFFECT_TYPE = "theme_skin";
    private static final String TAG = SkinEffect.class.getSimpleName();
    private AbilityEffect mDefaultActiveEffect;

    /* loaded from: classes5.dex */
    private static class SkinEffectHolder {
        private static final SkinEffect instance = new SkinEffect();

        private SkinEffectHolder() {
        }
    }

    private SkinEffect() {
        this.mDefaultActiveEffect = null;
    }

    public static SkinEffect getInstance() {
        return SkinEffectHolder.instance;
    }

    @Override // com.xiaopeng.xuiservice.themeoperation.effect.BaseEffect
    public void init() {
        this.mDefaultActiveEffect = getSavedEffect(KEY_EFFECT_TYPE);
    }

    @Override // com.xiaopeng.xuiservice.themeoperation.effect.BaseEffect
    public List<AbilityEffect> loadEffect(String path) {
        String str = TAG;
        LogUtil.i(str, "loadEffect on:" + path);
        List<AbilityEffect> effectList = new ArrayList<>();
        int storageType = getStorageType(path);
        AbilityEffect effect = new AbilityEffect();
        effect.setEffectAbilityType(EFFECT_TYPE);
        effect.setFriendlyName("天外飞仙");
        effect.setEffectId("skin001");
        effect.setPropertyType(storageType);
        effectList.add(effect);
        return effectList;
    }

    @Override // com.xiaopeng.xuiservice.themeoperation.effect.BaseEffect
    public void unloadEffect(List<AbilityEffect> effectList) {
        LogUtil.i(TAG, "unloadEffect");
    }

    @Override // com.xiaopeng.xuiservice.themeoperation.effect.BaseEffect
    public AbilityEffect[] getEffects() {
        LogUtil.i(TAG, "getEffects");
        return new AbilityEffect[0];
    }

    @Override // com.xiaopeng.xuiservice.themeoperation.effect.BaseEffect
    public boolean selectEffect(AbilityEffect effect) {
        String str = TAG;
        LogUtil.i(str, "selectEffect:" + effect);
        if (ThemeResManager.getInstance().isActiveThemeDefault()) {
            AbilityEffect defaultEffect = getCurrentEffect();
            saveEffect(KEY_EFFECT_TYPE, defaultEffect);
            return true;
        }
        return true;
    }

    @Override // com.xiaopeng.xuiservice.themeoperation.effect.BaseEffect
    public AbilityEffect getCurrentEffect() {
        AbilityEffect effect = new AbilityEffect();
        effect.setEffectAbilityType(EFFECT_TYPE);
        effect.setFriendlyName("大鹏展翅");
        effect.setEffectId("skin000");
        effect.setPropertyType(1);
        return effect;
    }

    @Override // com.xiaopeng.xuiservice.themeoperation.effect.BaseEffect
    public AbilityEffect getDefaultActiveEffect() {
        if (this.mDefaultActiveEffect == null) {
            this.mDefaultActiveEffect = getSavedEffect(KEY_EFFECT_TYPE);
            if (this.mDefaultActiveEffect == null) {
                this.mDefaultActiveEffect = getCurrentEffect();
                LogUtil.w(TAG, "getCurrentEffect as default effect");
            }
        }
        return this.mDefaultActiveEffect;
    }
}
