package com.xiaopeng.xuiservice.themeoperation.effect;

import com.xiaopeng.xuimanager.soundresource.data.BootSoundResource;
import com.xiaopeng.xuimanager.themeoperation.AbilityEffect;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.soundresource.SoundResourceLoader;
import com.xiaopeng.xuiservice.themeoperation.ThemeResManager;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes5.dex */
public class BootSoundEffect implements BaseEffect {
    private static final String EFFECT_TYPE = "bootsound";
    private static final String KEY_EFFECT_TYPE = "theme_bootsound";
    private static final String TAG = BootSoundEffect.class.getSimpleName();
    private AbilityEffect mDefaultActiveEffect;
    private final SoundResourceLoader sndResourceLoader;

    /* loaded from: classes5.dex */
    private static class BootSoundEffectHolder {
        private static final BootSoundEffect instance = new BootSoundEffect();

        private BootSoundEffectHolder() {
        }
    }

    public static BootSoundEffect getInstance() {
        return BootSoundEffectHolder.instance;
    }

    private BootSoundEffect() {
        this.sndResourceLoader = SoundResourceLoader.getInstance();
        this.mDefaultActiveEffect = null;
    }

    @Override // com.xiaopeng.xuiservice.themeoperation.effect.BaseEffect
    public void init() {
        this.mDefaultActiveEffect = getSavedEffect(KEY_EFFECT_TYPE);
    }

    @Override // com.xiaopeng.xuiservice.themeoperation.effect.BaseEffect
    public List<AbilityEffect> loadEffect(String path) {
        List<BootSoundResource> resList = SoundResourceLoader.loadBootRscFromDisk(path);
        if (resList == null) {
            String str = TAG;
            LogUtil.w(str, "loadEffect,no effec on:" + path);
            return null;
        }
        List<AbilityEffect> effectList = new ArrayList<>();
        int storageType = getStorageType(path);
        for (BootSoundResource bootRes : resList) {
            AbilityEffect effect = new AbilityEffect();
            effect.setEffectAbilityType(EFFECT_TYPE);
            effect.setFriendlyName(bootRes.getFriendlyName());
            effect.setEffectId(String.valueOf(bootRes.getResId()));
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
        BootSoundResource[] bootSoundResources = this.sndResourceLoader.getBootSoundResource();
        AbilityEffect[] effects = new AbilityEffect[bootSoundResources.length];
        for (BootSoundResource res : bootSoundResources) {
            effects[0] = new AbilityEffect();
            effects[0].setEffectAbilityType(EFFECT_TYPE);
            effects[0].setFriendlyName(res.getFriendlyName());
            effects[0].setEffectId(String.valueOf(res.getResId()));
            if (res.getResId() < 200) {
                effects[0].setPropertyType(1);
            } else {
                effects[0].setPropertyType(2);
            }
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
        int ret = this.sndResourceLoader.setBootSoundResource(resId);
        return ret == 0;
    }

    @Override // com.xiaopeng.xuiservice.themeoperation.effect.BaseEffect
    public AbilityEffect getCurrentEffect() {
        AbilityEffect effect = new AbilityEffect();
        BootSoundResource resource = this.sndResourceLoader.getActiveBootSoundResource();
        if (resource == null) {
            LogUtil.d(TAG, "getCurrentEffect,no effect available");
            return null;
        }
        effect.setEffectAbilityType(EFFECT_TYPE);
        effect.setFriendlyName(resource.getFriendlyName());
        int resId = resource.getResId();
        effect.setEffectId(String.valueOf(resId));
        if (resId < 200) {
            effect.setPropertyType(1);
        } else {
            effect.setPropertyType(2);
        }
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
