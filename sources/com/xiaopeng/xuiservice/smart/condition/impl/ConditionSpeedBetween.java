package com.xiaopeng.xuiservice.smart.condition.impl;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.vcu.CarVcuManager;
import android.util.Pair;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.smart.condition.ConditionBetween;
import com.xiaopeng.xuiservice.smart.condition.Conditions;
/* loaded from: classes5.dex */
public class ConditionSpeedBetween extends ConditionBetween<Double> implements CarVcuManager.CarVcuEventCallback {
    public ConditionSpeedBetween(Pair<Double, Double> targetValue) {
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
