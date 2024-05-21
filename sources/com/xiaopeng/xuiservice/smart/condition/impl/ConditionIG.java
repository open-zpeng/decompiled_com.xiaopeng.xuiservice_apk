package com.xiaopeng.xuiservice.smart.condition.impl;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.mcu.CarMcuManager;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.smart.condition.ConditionEq;
import com.xiaopeng.xuiservice.smart.condition.Conditions;
/* loaded from: classes5.dex */
public class ConditionIG extends ConditionEq<Integer> implements CarMcuManager.CarMcuEventCallback {
    public static final int IG_OFF = 0;
    public static final int IG_ON = 1;
    public static final int IG_REMOTE_ON = 2;
    public static final String TYPE = "ig";

    public ConditionIG(Integer targetValue) {
        super(targetValue);
        if (targetValue.intValue() < 0 || targetValue.intValue() > 2) {
            throw new IllegalArgumentException("Invalid target ig status = " + targetValue);
        }
    }

    public void onChangeEvent(CarPropertyValue carPropertyValue) {
        if (carPropertyValue.getPropertyId() == 557847561) {
            updateCurrentValue((Integer) carPropertyValue.getValue());
        }
    }

    public void onErrorEvent(int i, int i1) {
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue, com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void onStartWatch() {
        CarClientManager.getInstance().addMcuManagerListener(this);
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue, com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void onStopWatch() {
        CarClientManager.getInstance().removeMcuManagerListener(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    public Integer fetchCurrentValue() {
        CarMcuManager carMcuManager = CarClientManager.getInstance().getCarManager("xp_mcu");
        if (carMcuManager != null) {
            try {
                return Integer.valueOf(carMcuManager.getIgStatusFromMcu());
            } catch (Exception e) {
                Conditions.ERROR("getIgStatusFromMcu fail: " + e.getMessage());
                return null;
            }
        }
        return null;
    }
}
