package com.xiaopeng.xuiservice.smart.condition.impl;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.bcm.CarBcmManager;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.smart.condition.ConditionEq;
import com.xiaopeng.xuiservice.smart.condition.Conditions;
/* loaded from: classes5.dex */
public class ConditionBonnet extends ConditionEq<Integer> implements CarBcmManager.CarBcmEventCallback {
    public static final int BONNECT_CLOSED = 0;
    public static final int BONNECT_OPENED = 1;
    public static final String TYPE = "bonnet";

    public ConditionBonnet(Integer targetValue) {
        super(targetValue);
        if (targetValue.intValue() != 0 && targetValue.intValue() != 1) {
            throw new IllegalArgumentException("Invalid target bonnet status = " + targetValue);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    public Integer fetchCurrentValue() {
        CarBcmManager carBcmManager = CarClientManager.getInstance().getCarManager("xp_bcm");
        if (carBcmManager != null) {
            try {
                return Integer.valueOf(carBcmManager.isBcmBonnetOpened());
            } catch (Exception e) {
                Conditions.ERROR("CarBcmManager isBcmBonnetOpened() error, " + e.getMessage());
                return null;
            }
        }
        return null;
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue, com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void onStartWatch() {
        CarClientManager.getInstance().addBcmManagerListener(this);
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue, com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void onStopWatch() {
        CarClientManager.getInstance().removeBcmManagerListener(this);
    }

    public void onChangeEvent(CarPropertyValue carPropertyValue) {
        if (carPropertyValue.getPropertyId() == 557849641) {
            updateCurrentValue((Integer) carPropertyValue.getValue());
        }
    }

    public void onErrorEvent(int i, int i1) {
    }
}
