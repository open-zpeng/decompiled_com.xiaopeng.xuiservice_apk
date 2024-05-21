package com.xiaopeng.xuiservice.themeoperation.effect;

import com.xiaopeng.xuimanager.themeoperation.AbilityEffect;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.themeoperation.ThemeResManager;
import com.xiaopeng.xuiservice.utils.SharedPreferencesUtil;
import java.util.List;
/* loaded from: classes5.dex */
public interface BaseEffect {
    AbilityEffect getCurrentEffect();

    AbilityEffect getDefaultActiveEffect();

    AbilityEffect[] getEffects();

    void init();

    List<AbilityEffect> loadEffect(String str);

    boolean selectEffect(AbilityEffect abilityEffect);

    void unloadEffect(List<AbilityEffect> list);

    default void saveEffect(String key, AbilityEffect effect) {
        String contents = effect.getEffectAbilityType() + "#" + effect.getEffectId() + "#" + effect.getFriendlyName() + "#" + effect.getPropertyType();
        SharedPreferencesUtil.getInstance().put(key, contents);
    }

    default AbilityEffect getSavedEffect(String key) {
        String contents = SharedPreferencesUtil.getInstance().get(key, null);
        if (contents == null) {
            LogUtil.w("BaseEffect", "getSavedEffect null!");
            return null;
        }
        String[] values = contents.split("#", -1);
        if (values.length < 4) {
            LogUtil.w("BaseEffect", "getSavedEffect,data elements less than 4:" + contents);
            return null;
        }
        AbilityEffect effect = new AbilityEffect();
        effect.setEffectAbilityType(values[0]);
        effect.setEffectId(values[1]);
        effect.setFriendlyName(values[2]);
        effect.setPropertyType(Integer.parseInt(values[3]));
        return effect;
    }

    default int getStorageType(String path) {
        if (path.startsWith(ThemeResManager.THEME_SYSTEM_PATH_PREFIX) || "default".equals(path)) {
            return 1;
        }
        return 2;
    }
}
