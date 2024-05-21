package com.xiaopeng.xuiservice.themeoperation.effect;

import com.xiaopeng.xuimanager.soundresource.data.SoundEffectTheme;
import com.xiaopeng.xuimanager.themeoperation.AbilityEffect;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.soundresource.SoundResourceLoader;
import com.xiaopeng.xuiservice.themeoperation.ThemeResManager;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes5.dex */
public class SoundThemeEffect implements BaseEffect {
    private static final String EFFECT_TYPE = "sndtheme";
    private static final String KEY_EFFECT_TYPE = "theme_sndtheme";
    private static final String TAG = SoundThemeEffect.class.getSimpleName();
    private AbilityEffect mDefaultActiveEffect = null;
    private final SoundResourceLoader sndResourceLoader = SoundResourceLoader.getInstance();

    /* loaded from: classes5.dex */
    private static class SoundThemeEffectHolder {
        private static final SoundThemeEffect instance = new SoundThemeEffect();

        private SoundThemeEffectHolder() {
        }
    }

    public static SoundThemeEffect getInstance() {
        return SoundThemeEffectHolder.instance;
    }

    @Override // com.xiaopeng.xuiservice.themeoperation.effect.BaseEffect
    public void init() {
        this.mDefaultActiveEffect = getSavedEffect(KEY_EFFECT_TYPE);
    }

    @Override // com.xiaopeng.xuiservice.themeoperation.effect.BaseEffect
    public List<AbilityEffect> loadEffect(String path) {
        List<SoundEffectTheme> themeList = SoundResourceLoader.loadThemeRscFromDisk(path);
        if (themeList == null) {
            String str = TAG;
            LogUtil.w(str, "loadEffect,no effec on:" + path);
            return null;
        }
        List<AbilityEffect> effectList = new ArrayList<>();
        int storageType = getStorageType(path);
        for (SoundEffectTheme theme : themeList) {
            AbilityEffect effect = new AbilityEffect();
            effect.setEffectAbilityType(EFFECT_TYPE);
            effect.setFriendlyName(theme.getFriendlyName());
            effect.setEffectId(String.valueOf(theme.getThemeId()));
            effect.setPropertyType(storageType);
            effectList.add(effect);
        }
        return effectList;
    }

    @Override // com.xiaopeng.xuiservice.themeoperation.effect.BaseEffect
    public void unloadEffect(List<AbilityEffect> effectList) {
        this.sndResourceLoader.unloadEffect(effectList);
    }

    @Override // com.xiaopeng.xuiservice.themeoperation.effect.BaseEffect
    public AbilityEffect[] getEffects() {
        SoundEffectTheme[] themes = this.sndResourceLoader.getAvailableThemes();
        if (themes == null || themes.length == 0) {
            return null;
        }
        AbilityEffect[] effects = new AbilityEffect[themes.length];
        int i = 0;
        for (SoundEffectTheme theme : themes) {
            effects[i] = new AbilityEffect();
            effects[i].setEffectAbilityType(EFFECT_TYPE);
            effects[i].setFriendlyName(theme.getFriendlyName());
            effects[i].setEffectId(String.valueOf(theme.getThemeId()));
            if (theme.getThemeId() < 200) {
                effects[i].setPropertyType(1);
            } else {
                effects[i].setPropertyType(2);
            }
            i++;
        }
        return effects;
    }

    @Override // com.xiaopeng.xuiservice.themeoperation.effect.BaseEffect
    public boolean selectEffect(AbilityEffect effect) {
        if (ThemeResManager.getInstance().isActiveThemeDefault()) {
            AbilityEffect defaultEffect = getCurrentEffect();
            saveEffect(KEY_EFFECT_TYPE, defaultEffect);
        }
        int resId = Integer.parseInt(effect.getEffectId());
        int ret = this.sndResourceLoader.setActiveSoundThemIndex(resId);
        return ret == 0;
    }

    @Override // com.xiaopeng.xuiservice.themeoperation.effect.BaseEffect
    public AbilityEffect getCurrentEffect() {
        AbilityEffect effect = new AbilityEffect();
        SoundEffectTheme theme = this.sndResourceLoader.getActiveSoundTheme();
        if (theme == null) {
            LogUtil.d(TAG, "getCurrentEffect,no effect available");
            return null;
        }
        effect.setEffectAbilityType(EFFECT_TYPE);
        effect.setFriendlyName(theme.getFriendlyName());
        effect.setEffectId(String.valueOf(theme.getThemeId()));
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
