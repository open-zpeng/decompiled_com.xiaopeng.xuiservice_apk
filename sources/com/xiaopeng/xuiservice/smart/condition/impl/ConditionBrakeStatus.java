package com.xiaopeng.xuiservice.smart.condition.impl;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.vcu.CarVcuManager;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.smart.condition.ConditionEq;
import com.xiaopeng.xuiservice.smart.condition.Conditions;
/* loaded from: classes5.dex */
public class ConditionBrakeStatus extends ConditionEq<Integer> implements CarVcuManager.CarVcuEventCallback {
    public static final int BRAKE_OFF = 0;
    public static final int BRAKE_ON = 1;
    public static final String TYPE = "brake";

    public ConditionBrakeStatus(Integer targetValue) {
        super(targetValue);
        if (targetValue.intValue() != 0 && targetValue.intValue() != 1) {
            throw new IllegalArgumentException("Invalid target brake status = " + targetValue);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    public Integer fetchCurrentValue() {
        CarVcuManager carVcuManager = CarClientManager.getInstance().getCarManager("xp_vcu");
        if (carVcuManager != null) {
            try {
                return Integer.valueOf(carVcuManager.getBreakPedalStatus());
            } catch (Exception e) {
                Conditions.ERROR("CarVcuManager getBreakPedalStatus() error, " + e.getMessage());
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
        if (carPropertyValue.getPropertyId() == 557847063) {
            updateCurrentValue((Integer) carPropertyValue.getValue());
        }
    }

    public void onErrorEvent(int i, int i1) {
    }
}
