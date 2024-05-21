package com.xiaopeng.xuiservice.smart.condition.impl;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.tpms.CarTpmsManager;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.smart.condition.ConditionCompareBase;
import com.xiaopeng.xuiservice.smart.condition.Conditions;
/* loaded from: classes5.dex */
public class ConditionTirePressure extends ConditionCompareBase<Float> implements CarTpmsManager.CarTpmsEventCallback {
    public static final int TIRE_LEFT_FRONT = 1;
    public static final int TIRE_LEFT_REAR = 3;
    public static final int TIRE_RIGHT_FRONT = 2;
    public static final int TIRE_RIGHT_REAR = 4;
    public static final String TYPE = "tirePressure";
    private final int tire;

    public ConditionTirePressure(Float targetValue, String compareOpName, int tire) {
        super(targetValue, compareOpName);
        this.tire = tire;
        if (tire < 1 || tire > 4) {
            throw new IllegalArgumentException("Invalid tire = " + tire);
        }
    }

    public int getTire() {
        return this.tire;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    public Float fetchCurrentValue() {
        CarTpmsManager carTpmsManager = CarClientManager.getInstance().getCarManager("xp_tpms");
        if (carTpmsManager != null) {
            try {
                return Float.valueOf(carTpmsManager.getTirePressureValue(this.tire));
            } catch (Exception e) {
                Conditions.ERROR("CarTpmsManager getTirePressureValue tire = " + this.tire + " error, " + e.getMessage());
                return null;
            }
        }
        return null;
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue, com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void onStartWatch() {
        CarClientManager.getInstance().addTpmsManagerListener(this);
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue, com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void onStopWatch() {
        CarClientManager.getInstance().removeTpmsManagerListener(this);
    }

    public void onChangeEvent(CarPropertyValue carPropertyValue) {
        if ((this.tire == 1 && carPropertyValue.getPropertyId() == 559947266) || ((this.tire == 2 && carPropertyValue.getPropertyId() == 559947267) || ((this.tire == 3 && carPropertyValue.getPropertyId() == 559947268) || (this.tire == 4 && carPropertyValue.getPropertyId() == 559947269)))) {
            updateCurrentValue((Float) carPropertyValue.getValue());
        }
    }

    public void onErrorEvent(int i, int i1) {
    }
}
