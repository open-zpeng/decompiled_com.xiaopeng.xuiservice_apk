package com.xiaopeng.xuiservice.smart.condition.impl;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.vcu.CarVcuManager;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.smart.action.Actions;
import com.xiaopeng.xuiservice.smart.condition.ConditionCompareBase;
/* loaded from: classes5.dex */
public class ConditionDrivingDistance extends ConditionCompareBase<Float> implements CarVcuManager.CarVcuEventCallback {
    public static final String MODE_CLTC = "CLTC";
    public static final String MODE_DYNAMIC = "DYNAMIC";
    public static final String MODE_NEDC = "NEDC";
    public static final String MODE_WLTP = "WLTP";
    public static final String TYPE = "drivingDistance";
    private final String mode;

    public ConditionDrivingDistance(Float targetValue, String compareOpName, String mode) {
        super(targetValue, compareOpName);
        this.mode = mode;
        if (!"WLTP".equalsIgnoreCase(mode) && !"CLTC".equalsIgnoreCase(mode) && !"NEDC".equalsIgnoreCase(mode) && !"DYNAMIC".equalsIgnoreCase(mode)) {
            throw new IllegalArgumentException("Invalid driving distance mode = " + mode);
        }
    }

    public String getMode() {
        return this.mode;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    public Float fetchCurrentValue() {
        CarVcuManager carVcuManager = CarClientManager.getInstance().getCarManager("xp_vcu");
        Float distance = null;
        if (carVcuManager != null) {
            try {
                if ("CLTC".equalsIgnoreCase(this.mode)) {
                    distance = Float.valueOf(carVcuManager.getCltcAvailableDrivingDistanceFloat());
                } else if ("WLTP".equalsIgnoreCase(this.mode)) {
                    distance = Float.valueOf(carVcuManager.getWltpAvailableDrivingDistanceFloat());
                } else if ("NEDC".equalsIgnoreCase(this.mode)) {
                    distance = Float.valueOf(carVcuManager.getNedcAvalibleDrivingDistanceFloat());
                } else if ("DYNAMIC".equalsIgnoreCase(this.mode)) {
                    distance = Float.valueOf(carVcuManager.getDynamicAvailableDrivingDistance());
                }
            } catch (Exception e) {
                Actions.ERROR("get driving distance error, mode = " + this.mode + ", e = " + e.getMessage());
            }
        }
        return distance;
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
        if (("WLTP".equalsIgnoreCase(this.mode) && carPropertyValue.getPropertyId() == 559944315) || (("CLTC".equalsIgnoreCase(this.mode) && carPropertyValue.getPropertyId() == 559944314) || (("NEDC".equalsIgnoreCase(this.mode) && carPropertyValue.getPropertyId() == 559944326) || ("DYNAMIC".equalsIgnoreCase(this.mode) && carPropertyValue.getPropertyId() == 559944335)))) {
            updateCurrentValue((Float) carPropertyValue.getValue());
        }
    }

    public void onErrorEvent(int i, int i1) {
    }
}
