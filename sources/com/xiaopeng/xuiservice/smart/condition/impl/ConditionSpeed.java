package com.xiaopeng.xuiservice.smart.condition.impl;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.vcu.CarVcuManager;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.smart.condition.ConditionCompareBase;
import com.xiaopeng.xuiservice.smart.condition.Conditions;
/* loaded from: classes5.dex */
public class ConditionSpeed extends ConditionCompareBase<Double> implements CarVcuManager.CarVcuEventCallback {
    public static final String TYPE = "speed";

    public ConditionSpeed(Double targetValue, String compareOpName) {
        super(targetValue, compareOpName);
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
        if (carPropertyValue.getPropertyId() == 559944229) {
            float speed = ((Float) carPropertyValue.getValue()).floatValue();
            updateCurrentValue(Double.valueOf(speed));
        }
    }

    public void onErrorEvent(int i, int i1) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    public Double fetchCurrentValue() {
        CarVcuManager carVcuManager = CarClientManager.getInstance().getCarManager("xp_vcu");
        if (carVcuManager != null) {
            try {
                return Double.valueOf(carVcuManager.getRawCarSpeed());
            } catch (Exception e) {
                Conditions.ERROR("getRawCarSpeed fail: " + e.getMessage());
                return null;
            }
        }
        return null;
    }
}
