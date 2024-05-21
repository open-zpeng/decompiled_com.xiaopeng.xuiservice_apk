package com.xiaopeng.xuiservice.smart.action.impl;

import android.app.ActivityThread;
import android.text.TextUtils;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageService;
import com.xiaopeng.xuiservice.smart.action.ActionBase;
import com.xiaopeng.xuiservice.smart.action.Actions;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import com.xiaopeng.xuiservice.xapp.miniprog.manager.HttpManagerUtil;
import java.io.File;
import java.util.Objects;
/* loaded from: classes5.dex */
public class ActionLightLanguage extends ActionBase implements XpLightlanuageService.LightEffectListener {
    private final String effect;
    private final String file;
    private volatile boolean lluPlayed;
    private final int repeat;

    public ActionLightLanguage(String effect, String file, int repeat) {
        this.effect = effect;
        this.file = file;
        this.repeat = repeat;
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    protected void onStart() {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.action.impl.-$$Lambda$ActionLightLanguage$5YgHtf361ly0Z-Un7vg2n8nRZCs
            @Override // java.lang.Runnable
            public final void run() {
                ActionLightLanguage.this.startLLU();
            }
        });
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    protected void onStop() {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.action.impl.-$$Lambda$ActionLightLanguage$LEhKUe9-2zoT7NFxH8LJNTgIgcQ
            @Override // java.lang.Runnable
            public final void run() {
                ActionLightLanguage.this.stopLLU();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startLLU() {
        if (!XUIConfig.hasFeature(XUIConfig.PROPERTY_LLU) || XUIConfig.isInternationalEnable()) {
            Actions.ERROR("LLU not supported in this car! car type = " + HttpManagerUtil.getCarType() + ", international = " + XUIConfig.isInternationalEnable());
            stop();
            return;
        }
        XpLightlanuageService lluService = XpLightlanuageService.getInstance(ActivityThread.currentActivityThread().getApplication());
        boolean loaded = false;
        if (!TextUtils.isEmpty(this.effect)) {
            loaded = lluService.isLightEffectLoaded(this.effect);
            if (!loaded) {
                String str = this.file;
                if (str != null && new File(str).exists()) {
                    lluService.loadLightEffect(this.file);
                    loaded = lluService.isLightEffectLoaded(this.effect);
                    if (!loaded) {
                        Actions.ERROR("load llu fail, invalid json file: " + this.file);
                    }
                } else {
                    Actions.ERROR("llu json file not exists: " + this.file);
                }
            }
        } else {
            Actions.ERROR("LightLanguageAction must have an effect name. " + this);
        }
        if (!loaded) {
            stop();
            return;
        }
        this.lluPlayed = true;
        lluService.setLightEffectListener(this);
        lluService.playEffect(this.effect, null, this.repeat);
        Actions.INFO("start llu: " + this.effect + " count = " + this.repeat);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopLLU() {
        if (this.lluPlayed) {
            XpLightlanuageService lluService = XpLightlanuageService.getInstance(ActivityThread.currentActivityThread().getApplication());
            lluService.stopEffect(this.effect);
            lluService.unsetLightEffectListener(this);
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageService.LightEffectListener
    public void onStartEffect(String name, String type) {
        if (!Objects.equals(name, this.effect)) {
            return;
        }
        Actions.INFO("onStartEffect: name = " + name + ", type = " + type + " " + this);
    }

    @Override // com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageService.LightEffectListener
    public void onStopEffect(String name, String type) {
        if (!Objects.equals(name, this.effect)) {
            return;
        }
        Actions.INFO("onStopEffect: name = " + name + ", type = " + type + " " + this);
        stop();
    }

    @Override // com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageService.LightEffectListener
    public void onFinishEffect(String name, String type) {
        if (!Objects.equals(name, this.effect)) {
            return;
        }
        Actions.INFO("onFinishEffect: name = " + name + ", type = " + type + " " + this);
        stop();
    }

    @Override // com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageService.LightEffectListener
    public void onErrorEffect(String name, int errCode) {
        if (!Objects.equals(name, this.effect)) {
            return;
        }
        Actions.INFO("onErrorEffect: name = " + name + ", errCode = " + errCode + " " + this);
    }

    @Override // com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageService.LightEffectListener
    public void onUpgradeEffect(int name, int mode) {
        Actions.INFO("onUpgradeEffect: name = " + name + ", mode = " + mode + " " + this);
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    public String toString() {
        return super.toString() + "{effect='" + this.effect + "', file='" + this.file + "', repeat=" + this.repeat + '}';
    }
}
