package com.xiaopeng.xuiservice.smart.condition.impl;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.vcu.CarVcuManager;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.smart.condition.ConditionIn;
import com.xiaopeng.xuiservice.smart.condition.Conditions;
/* loaded from: classes5.dex */
public class ConditionGear extends ConditionIn<String> implements CarVcuManager.CarVcuEventCallback {
    public static final String TYPE = "gear";

    public ConditionGear(String[] targetValue) {
        super(targetValue);
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue, com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void onStartWatch() {
        CarClientManager.getInstance().addVcuManagerListener(this);
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue, com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void onStopWatch() {
        CarClientManager.getInstance().removeVcuManagerListener(this);
    }

    public void onChangeEvent(CarPropertyValue carPropertyValue) {
        if (carPropertyValue.getPropertyId() == 557847045) {
            updateCurrentValue(gearToString(((Integer) carPropertyValue.getValue()).intValue()));
        }
    }

    public void onErrorEvent(int i, int i1) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    public String fetchCurrentValue() {
        return gearToString(fetchCurrentValueInt());
    }

    protected int fetchCurrentValueInt() {
        CarVcuManager carVcuManager = CarClientManager.getInstance().getCarManager("xp_vcu");
        if (carVcuManager != null) {
            try {
                return carVcuManager.getDisplayGearLevel();
            } catch (Exception e) {
                Conditions.ERROR("getDisplayGearLevel fail: " + e.getMessage());
                return 0;
            }
        }
        return 0;
    }

    private String gearToString(int gear) {
        if (gear != 1) {
            if (gear != 2) {
                if (gear != 3) {
                    if (gear == 4) {
                        return "p";
                    }
                    return null;
                }
                return "r";
            }
            return "n";
        }
        return "d";
    }
}
