package com.xiaopeng.xuiservice.smart.condition.impl;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.bcm.CarBcmManager;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.smart.condition.ConditionEq;
import com.xiaopeng.xuiservice.smart.condition.Conditions;
/* loaded from: classes5.dex */
public class ConditionDoorState extends ConditionEq<Integer> implements CarBcmManager.CarBcmEventCallback {
    public static final int DOOR_DRIVER = 0;
    public static final int DOOR_FRONT_LEFT = 0;
    public static final int DOOR_FRONT_RIGHT = 1;
    public static final int DOOR_REAR_LEFT = 2;
    public static final int DOOR_REAR_RIGHT = 3;
    public static final int STATE_CLOSE = 0;
    public static final int STATE_OPEN = 1;
    public static final String TYPE = "door";
    private final int door;

    public ConditionDoorState(Integer targetValue, int door) {
        super(targetValue);
        this.door = door;
        if (door < 0 || door > 3) {
            throw new IllegalArgumentException("Invalid door: " + door);
        } else if (targetValue.intValue() != 1 && targetValue.intValue() != 0) {
            throw new IllegalArgumentException("Invalid target door state: " + targetValue);
        }
    }

    public int getDoor() {
        return this.door;
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
        Integer[] states;
        if (carPropertyValue.getPropertyId() == 557915161 && (states = (Integer[]) carPropertyValue.getValue()) != null) {
            updateCurrentValue(states[this.door]);
        }
    }

    public void onErrorEvent(int i, int i1) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    public Integer fetchCurrentValue() {
        CarBcmManager carBcmManager = CarClientManager.getInstance().getCarManager("xp_bcm");
        if (carBcmManager != null) {
            try {
                int[] states = carBcmManager.getDoorsState();
                return Integer.valueOf(states[this.door]);
            } catch (Exception e) {
                Conditions.ERROR("getDoorsState fail: " + e.getMessage());
                return null;
            }
        }
        return null;
    }
}
