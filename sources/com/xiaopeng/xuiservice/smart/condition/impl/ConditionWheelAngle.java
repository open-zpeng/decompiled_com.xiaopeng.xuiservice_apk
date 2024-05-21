package com.xiaopeng.xuiservice.smart.condition.impl;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.eps.CarEpsManager;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.smart.condition.ConditionCompareBase;
import com.xiaopeng.xuiservice.smart.condition.Conditions;
/* loaded from: classes5.dex */
public class ConditionWheelAngle extends ConditionCompareBase<Float> implements CarEpsManager.CarEpsEventCallback {
    public static final String TYPE = "wheelAngle";

    public ConditionWheelAngle(Float targetValue, String compareOpName) {
        super(targetValue, compareOpName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    public Float fetchCurrentValue() {
        CarEpsManager carEpsManager = CarClientManager.getInstance().getCarManager("xp_eps");
        if (carEpsManager != null) {
            try {
                return Float.valueOf(carEpsManager.getSteeringAngle());
            } catch (Exception e) {
                Conditions.ERROR("CarEpsManager getSteeringAngle() error, " + e.getMessage());
                return null;
            }
        }
        return null;
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue, com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void onStartWatch() {
        CarClientManager.getInstance().addEpsManagerListener(this);
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue, com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void onStopWatch() {
        CarClientManager.getInstance().removeEpsManagerListener(this);
    }

    public void onChangeEvent(CarPropertyValue carPropertyValue) {
        if (carPropertyValue.getPropertyId() == 559948806) {
            updateCurrentValue((Float) carPropertyValue.getValue());
        }
    }

    public void onErrorEvent(int i, int i1) {
    }
}
