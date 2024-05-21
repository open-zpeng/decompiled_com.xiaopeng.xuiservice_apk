package com.xiaopeng.xuiservice.smart.condition.impl;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.vcu.CarVcuManager;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.smart.condition.ConditionCompareBase;
import com.xiaopeng.xuiservice.smart.condition.Conditions;
/* loaded from: classes5.dex */
public class ConditionAccPedal extends ConditionCompareBase<Integer> implements CarVcuManager.CarVcuEventCallback {
    public static final int ACC_PEDAL_MAX = 100;
    public static final int ACC_PEDAL_MIN = 0;
    public static final String TYPE = "accPedal";

    public ConditionAccPedal(Integer targetValue, String compareOpName) {
        super(targetValue, compareOpName);
        if (targetValue.intValue() < 0 || targetValue.intValue() > 100) {
            throw new IllegalArgumentException("Invalid target acc pedal value: " + targetValue);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    public Integer fetchCurrentValue() {
        CarVcuManager carVcuManager = CarClientManager.getInstance().getCarManager("xp_vcu");
        if (carVcuManager != null) {
            try {
                return Integer.valueOf(carVcuManager.getAccPedalStatus());
            } catch (Exception e) {
                Conditions.ERROR("CarVcuManager getAccPedalStatus() error, " + e.getMessage());
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
        if (carPropertyValue.getPropertyId() == 557847064) {
            updateCurrentValue((Integer) carPropertyValue.getValue());
        }
    }

    public void onErrorEvent(int i, int i1) {
    }
}
