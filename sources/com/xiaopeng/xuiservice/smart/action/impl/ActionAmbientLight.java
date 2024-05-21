package com.xiaopeng.xuiservice.smart.action.impl;

import android.util.Log;
import com.xiaopeng.xuimanager.XUIManager;
import com.xiaopeng.xuimanager.ambientlight.AmbientLightManager;
import com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightCapability;
import com.xiaopeng.xuiservice.smart.action.ActionBase;
import com.xiaopeng.xuiservice.smart.action.Actions;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class ActionAmbientLight extends ActionBase implements AmbientLightManager.AmbientLightEventListener {
    private boolean ambientLightOpen;
    private final int[] brightness;
    private final int[] color;
    private final String effect;
    private final int executeTime;
    private final int[] fade;
    private final int timeInterval;

    public ActionAmbientLight(String effect, int[] color, int[] brightness, int[] fade, int executeTime, int timeInterval) {
        this.effect = effect;
        this.color = color;
        this.brightness = brightness;
        this.fade = fade;
        this.executeTime = executeTime;
        this.timeInterval = timeInterval;
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    protected void onStart() {
        final AmbientLightManager ambientLightManager = (AmbientLightManager) XUIManager.getInstance().getService("ambientlight");
        if (ambientLightManager == null) {
            Actions.INFO("ambient light not supported");
        } else {
            XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.action.impl.-$$Lambda$ActionAmbientLight$iMryus-fJH2g2UPy0e0KoBHjz1Y
                @Override // java.lang.Runnable
                public final void run() {
                    ActionAmbientLight.this.lambda$onStart$0$ActionAmbientLight(ambientLightManager);
                }
            });
        }
    }

    public /* synthetic */ void lambda$onStart$0$ActionAmbientLight(AmbientLightManager ambientLightManager) {
        try {
            this.ambientLightOpen = ambientLightManager.getAmbientLightOpen();
            Actions.INFO("getAmbientLightOpen = " + this.ambientLightOpen);
            if (!this.ambientLightOpen) {
                Actions.INFO("setAmbientLightOpen = true");
                ambientLightManager.setAmbientLightOpen(true);
            }
            ambientLightManager.registerListener(this);
            JSONObject effectData = new JSONObject();
            effectData.put("color", intArrToJson(this.color, "color"));
            effectData.put("bright", intArrToJson(this.brightness, "bright"));
            effectData.put("fade", intArrToJson(this.fade, "fade"));
            effectData.put("executeTime", this.executeTime);
            effectData.put(AmbientLightCapability.TIME_INTERVAL, this.timeInterval);
            String[] scriptEffect = {effectData.toString(), this.effect};
            ambientLightManager.startPlay(scriptEffect);
        } catch (Exception e) {
            Actions.ERROR("start ambient light fail", e);
        }
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    protected void onStop() {
        final AmbientLightManager ambientLightManager = (AmbientLightManager) XUIManager.getInstance().getService("ambientlight");
        if (ambientLightManager == null) {
            return;
        }
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.action.impl.-$$Lambda$ActionAmbientLight$Nief1_qVMSTNOd_rKl2ePlAG0YU
            @Override // java.lang.Runnable
            public final void run() {
                ActionAmbientLight.this.lambda$onStop$1$ActionAmbientLight(ambientLightManager);
            }
        });
    }

    public /* synthetic */ void lambda$onStop$1$ActionAmbientLight(AmbientLightManager ambientLightManager) {
        try {
            ambientLightManager.unregisterListener(this);
            ambientLightManager.stopPlay();
            if (!this.ambientLightOpen) {
                Actions.INFO("setAmbientLightOpen = false, restore AmbientLightOpen switch");
                ambientLightManager.setAmbientLightOpen(false);
            }
        } catch (Exception e) {
            Actions.ERROR("stop ambient light fail", e);
        }
    }

    public void onLightEffectTypeSetEvent(String effectType) {
        super.onLightEffectTypeSetEvent(effectType);
        Actions.INFO(this + " onLightEffectTypeSetEvent: effectType = " + effectType);
    }

    public void onLightDoubleColorEnableEvent(String effectType, boolean enable) {
        super.onLightDoubleColorEnableEvent(effectType, enable);
        Actions.INFO(this + " onLightDoubleColorEnableEvent: effectType = " + effectType + ", enable = " + enable);
    }

    public void onLightMonoColorSetEvent(String effectType, int color) {
        super.onLightMonoColorSetEvent(effectType, color);
        Actions.INFO(this + " onLightMonoColorSetEvent: effectType = " + effectType + ", color = " + color);
    }

    public void onLightDoubleColorSetEvent(String effectType, int first_color, int second_color) {
        super.onLightDoubleColorSetEvent(effectType, first_color, second_color);
        Actions.INFO(this + " onLightDoubleColorSetEvent: effectType = " + effectType + ", first_color = " + first_color + ", second_color = " + second_color);
    }

    public void onLightBrightSetEvent(String effectType, int bright) {
        super.onLightBrightSetEvent(effectType, bright);
        Actions.INFO(this + " onLightBrightSetEvent: effectType = " + effectType + ", bright = " + bright);
    }

    public void onLightSwitchSetEvent(String effectType, boolean enable) {
        super.onLightSwitchSetEvent(effectType, enable);
        Actions.INFO(this + " onLightSwitchSetEvent: effectType = " + effectType + ", enable = " + enable);
    }

    public void onErrorEvent(int errorCode, int operation) {
        Actions.INFO(this + " onErrorEvent: errorCode = " + errorCode + ", operation = " + operation);
    }

    private JSONObject intArrToJson(int[] arr, String infoType) {
        JSONObject output = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray();
            for (int i : arr) {
                jsonArray.put(i);
            }
            output.put(infoType, jsonArray);
        } catch (Exception e) {
            Log.e("egg", "intArrToJson() Exception " + e);
        }
        return output;
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    public String toString() {
        return super.toString() + "{effect='" + this.effect + "', color=" + Arrays.toString(this.color) + ", brightness=" + Arrays.toString(this.brightness) + ", fade=" + Arrays.toString(this.fade) + ", executeTime=" + this.executeTime + ", timeInterval=" + this.timeInterval + '}';
    }
}
