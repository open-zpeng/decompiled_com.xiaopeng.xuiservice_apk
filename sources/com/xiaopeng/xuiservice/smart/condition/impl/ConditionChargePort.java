package com.xiaopeng.xuiservice.smart.condition.impl;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.bcm.CarBcmManager;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.smart.condition.ConditionEq;
import com.xiaopeng.xuiservice.smart.condition.Conditions;
/* loaded from: classes5.dex */
public class ConditionChargePort extends ConditionEq<Integer> implements CarBcmManager.CarBcmEventCallback {
    public static final int PORT_LEFT = 1;
    public static final int PORT_RIGHT = 2;
    public static final int STATUS_CLOSED = 2;
    public static final int STATUS_FAULT = 3;
    public static final int STATUS_MIDDLE = 1;
    public static final int STATUS_OPEN = 0;
    public static final String TYPE = "chargePort";
    private final int port;

    public ConditionChargePort(Integer targetValue, int port) {
        super(targetValue);
        this.port = port;
        if (port != 1 && port != 2) {
            throw new IllegalArgumentException("Invalid port = " + port);
        } else if (targetValue.intValue() < 0 || targetValue.intValue() > 3) {
            throw new IllegalArgumentException("Invalid target charge port status: " + targetValue);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    public Integer fetchCurrentValue() {
        CarBcmManager carBcmManager = CarClientManager.getInstance().getCarManager("xp_bcm");
        if (carBcmManager != null) {
            try {
                return Integer.valueOf(carBcmManager.getChargePortStatus(this.port));
            } catch (Exception e) {
                Conditions.ERROR("CarBcmManager getChargePortStatus port = " + this.port + " error, " + e.getMessage());
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
        if ((this.port == 1 && carPropertyValue.getPropertyId() == 557849642) || (this.port == 2 && carPropertyValue.getPropertyId() == 557849643)) {
            updateCurrentValue((Integer) carPropertyValue.getValue());
        }
    }

    public void onErrorEvent(int i, int i1) {
    }
}
