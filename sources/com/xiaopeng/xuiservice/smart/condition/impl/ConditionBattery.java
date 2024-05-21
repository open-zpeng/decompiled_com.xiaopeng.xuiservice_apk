package com.xiaopeng.xuiservice.smart.condition.impl;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.vcu.CarVcuManager;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.smart.condition.ConditionCompareBase;
import com.xiaopeng.xuiservice.smart.condition.Conditions;
/* loaded from: classes5.dex */
public class ConditionBattery extends ConditionCompareBase<Integer> implements CarVcuManager.CarVcuEventCallback {
    private static final int MAX_BATTERY = 100;
    private static final int MIN_BATTERY = 0;
    public static final String TYPE = "battery";

    public ConditionBattery(Integer targetValue, String compareOpName) {
        super(targetValue, compareOpName);
        if (targetValue.intValue() < 0 || targetValue.intValue() > 100) {
            throw new IllegalArgumentException("Invalid battery percent value = " + targetValue);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    public Integer fetchCurrentValue() {
        CarVcuManager carVcuManager = CarClientManager.getInstance().getCarManager("xp_vcu");
        if (carVcuManager != null) {
            try {
                return Integer.valueOf(carVcuManager.getElectricityPercent());
            } catch (Exception e) {
                Conditions.ERROR("CarVcuManager getElectricityPercent() error, " + e.getMessage());
                return null;
            }
        }
        return null;
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
        if (carPropertyValue.getPropertyId() == 557847042) {
            updateCurrentValue((Integer) carPropertyValue.getValue());
        }
    }

    public void onErrorEvent(int i, int i1) {
    }
}
