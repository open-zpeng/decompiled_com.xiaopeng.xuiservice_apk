package com.xiaopeng.xuiservice.capabilities.XpAmbientLightService;

import android.content.Context;
/* loaded from: classes5.dex */
public class AmbientLight {
    private static final boolean DBG = false;
    private static final String EFFECT_FOLLOW_SPEED = "follow_speed";
    private static final String EFFECT_GENTLE_BREATHING = "gentle_breathing";
    private static final String EFFECT_STABLE_EFFECT = "stable_effect";
    private static final String TAG = "AmbientLight";
    private AmbientLightEffect mAmbientLightEffect;

    public AmbientLight(Context context) {
        this.mAmbientLightEffect = new AmbientLightEffect(context);
    }

    public void stopDefaultEffect() {
        this.mAmbientLightEffect.stopDefaultEffect();
    }

    public void stopSpeechEffect() {
        this.mAmbientLightEffect.stopSpeechEffect();
    }

    public void stopAllAmbilentEffect() {
        this.mAmbientLightEffect.stopAllAmbilentEffect();
    }

    public void setDefaultOrMusicGroup(int groupNum) {
        this.mAmbientLightEffect.setDefaultOrMusicGroup(groupNum);
    }

    public void setAmbientLightEffect(String effectType, boolean doubleThemeColorEnable, int firstThemeColor, int secondThemeColor, int bright) {
        if (effectType.equals("stable_effect")) {
            this.mAmbientLightEffect.setAmbientLightEffect("noeffect", doubleThemeColorEnable, firstThemeColor, secondThemeColor, bright);
        } else if (effectType.equals("gentle_breathing")) {
            this.mAmbientLightEffect.setAmbientLightEffect("breath", doubleThemeColorEnable, firstThemeColor, secondThemeColor, bright);
        } else {
            this.mAmbientLightEffect.setAmbientLightEffect(effectType, doubleThemeColorEnable, firstThemeColor, secondThemeColor, bright);
        }
    }

    public void updateLightEffectPara(String effectType, boolean doubleThemeColorEnable, int firstThemeColor, int secondThemeColor, int bright) {
        if (effectType.equals("stable_effect")) {
            this.mAmbientLightEffect.updateLightEffectPara("noeffect", doubleThemeColorEnable, firstThemeColor, secondThemeColor, bright);
        } else if (effectType.equals("gentle_breathing")) {
            this.mAmbientLightEffect.updateLightEffectPara("breath", doubleThemeColorEnable, firstThemeColor, secondThemeColor, bright);
        } else {
            this.mAmbientLightEffect.updateLightEffectPara(effectType, doubleThemeColorEnable, firstThemeColor, secondThemeColor, bright);
        }
    }

    public void setSpeechEffectEnable(boolean enable) {
        this.mAmbientLightEffect.setSpeechEffectEnable(enable);
    }

    public void setAmbientLightBrightPara(int bright) {
        this.mAmbientLightEffect.setAmbientLightBrightPara(bright);
    }

    public void setSpeechEffect(int[] groupNum, String effectType) {
        this.mAmbientLightEffect.setSpeechEffect(groupNum, effectType);
    }

    public void setSpeechSpectrumEffect(byte color, byte bright, byte fade) {
        this.mAmbientLightEffect.setSpeechSpectrumEffect(color, bright, fade);
    }

    public void setMusicSpectrumEffect(byte color, byte bright, byte fade) {
        this.mAmbientLightEffect.setMusicSpectrumEffect(color, bright, fade);
    }

    public void setFollowCarSpeedEffect(int color, float speed, int fade) {
        this.mAmbientLightEffect.setFollowCarSpeedEffect(color, speed, fade);
    }

    public void setAmbientLightDataEffect(int groupNum, byte color, byte bright, byte fade) {
        this.mAmbientLightEffect.setGroupLightData(groupNum, color, bright, fade);
    }

    public void setAmbientLightDefaultGroupEffect(byte color, byte bright, byte fade) {
        this.mAmbientLightEffect.setAmbientLightDefaultGroupEffect(color, bright, fade);
    }

    public void setIgOnStatus(boolean enable) {
        this.mAmbientLightEffect.setIgOnStatus(enable);
    }

    public void setTwoLightData(byte protocol, byte[] lightPosition, byte[] color, byte[] bright, byte[] fade) {
        this.mAmbientLightEffect.setTwoLightData(protocol, lightPosition, color, bright, fade);
    }
}
