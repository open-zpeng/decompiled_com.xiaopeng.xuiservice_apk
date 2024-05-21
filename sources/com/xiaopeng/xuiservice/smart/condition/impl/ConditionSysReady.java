package com.xiaopeng.xuiservice.smart.condition.impl;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.vcu.CarVcuManager;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.smart.condition.ConditionEq;
import com.xiaopeng.xuiservice.smart.condition.Conditions;
/* loaded from: classes5.dex */
public class ConditionSysReady extends ConditionEq<Integer> implements CarVcuManager.CarVcuEventCallback {
    public static final int HIGH_VOL_ON = 1;
    public static final int NOT_READY = 0;
    public static final int READY = 2;
    public static final String TYPE = "sysReady";

    public ConditionSysReady(Integer targetValue) {
        super(targetValue);
        if (targetValue.intValue() < 0 || targetValue.intValue() > 2) {
            throw new IllegalArgumentException("Invalid target sys ready state = " + targetValue);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    public Integer fetchCurrentValue() {
        CarVcuManager manager = CarClientManager.getInstance().getCarManager("xp_vcu");
        if (manager != null) {
            try {
                return Integer.valueOf(manager.getEvSysReady());
            } catch (Exception e) {
                Conditions.ERROR("CarVcuManager getEvSysReady() fail, " + e.getMessage());
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
        if (carPropertyValue.getPropertyId() == 557847056) {
            updateCurrentValue((Integer) carPropertyValue.getValue());
        }
    }

    public void onErrorEvent(int i, int i1) {
    }
}
