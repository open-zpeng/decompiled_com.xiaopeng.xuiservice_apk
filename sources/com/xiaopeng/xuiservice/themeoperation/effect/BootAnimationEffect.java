package com.xiaopeng.xuiservice.themeoperation.effect;

import android.os.SystemProperties;
import android.text.TextUtils;
import com.xiaopeng.xuimanager.themeoperation.AbilityEffect;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes5.dex */
public class BootAnimationEffect implements BaseEffect {
    private static final String CUSTOM_PROP = "persist.sys.bootanim.custom_path";
    private static final String EFFECT_TYPE = "bootani";
    private static final String KEY_EFFECT_TYPE = "theme_bootani";
    private static final String TAG = BootAnimationEffect.class.getSimpleName();
    private AbilityEffect mDefaultActiveEffect = null;

    /* loaded from: classes5.dex */
    private static class BootAnimationEffectHolder {
        private static final BootAnimationEffect instance = new BootAnimationEffect();

        private BootAnimationEffectHolder() {
        }
    }

    public static BootAnimationEffect getInstance() {
        return BootAnimationEffectHolder.instance;
    }

    @Override // com.xiaopeng.xuiservice.themeoperation.effect.BaseEffect
    public void init() {
    }

    @Override // com.xiaopeng.xuiservice.themeoperation.effect.BaseEffect
    public List<AbilityEffect> loadEffect(String path) {
        String str = TAG;
        LogUtil.d(str, "loadEffect,path=" + path);
        List<AbilityEffect> effectList = new ArrayList<>();
        int storageType = getStorageType(path);
        AbilityEffect effect = new AbilityEffect();
        effect.setEffectAbilityType(EFFECT_TYPE);
        effect.setParam(path + "/");
        if ("default".equals(path)) {
            effect.setFriendlyName("default");
            effect.setEffectId("bootanidefault");
        } else {
            effect.setFriendlyName("动画主题");
            effect.setEffectId("bootani001");
        }
        effect.setPropertyType(storageType);
        effectList.add(effect);
        return effectList;
    }

    @Override // com.xiaopeng.xuiservice.themeoperation.effect.BaseEffect
    public void unloadEffect(List<AbilityEffect> effectList) {
        String str = TAG;
        LogUtil.d(str, "unloadEffect=" + effectList);
    }

    @Override // com.xiaopeng.xuiservice.themeoperation.effect.BaseEffect
    public AbilityEffect[] getEffects() {
        return new AbilityEffect[0];
    }

    @Override // com.xiaopeng.xuiservice.themeoperation.effect.BaseEffect
    public boolean selectEffect(AbilityEffect effect) {
        String str = TAG;
        LogUtil.i(str, "selectEffect=" + effect);
        if (effect.getParam() == null) {
            LogUtil.w(TAG, "selectEffect, invalid null param");
            return false;
        } else if ("default".equals(effect.getParam())) {
            SystemProperties.set(CUSTOM_PROP, "");
            return true;
        } else {
            SystemProperties.set(CUSTOM_PROP, effect.getParam());
            return true;
        }
    }

    @Override // com.xiaopeng.xuiservice.themeoperation.effect.BaseEffect
    public AbilityEffect getCurrentEffect() {
        String targetPath = SystemProperties.get(CUSTOM_PROP);
        if (TextUtils.isEmpty(targetPath)) {
            targetPath = "default";
        }
        List<AbilityEffect> list = loadEffect(targetPath);
        if (list != null) {
            return list.get(0);
        }
        return null;
    }

    @Override // com.xiaopeng.xuiservice.themeoperation.effect.BaseEffect
    public AbilityEffect getDefaultActiveEffect() {
        return null;
    }
}
