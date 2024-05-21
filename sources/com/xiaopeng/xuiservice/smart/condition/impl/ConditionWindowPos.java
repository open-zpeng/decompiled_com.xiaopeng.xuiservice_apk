package com.xiaopeng.xuiservice.smart.condition.impl;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.bcm.CarBcmManager;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.smart.condition.ConditionCompareBase;
import com.xiaopeng.xuiservice.smart.condition.Conditions;
/* loaded from: classes5.dex */
public class ConditionWindowPos extends ConditionCompareBase<Float> implements CarBcmManager.CarBcmEventCallback {
    public static final float MAX_POS = 100.0f;
    public static final float MIN_POS = 0.0f;
    public static final String TYPE = "window";
    public static final int WINDOW_DRIVER = 0;
    public static final int WINDOW_FRONT_LEFT = 0;
    public static final int WINDOW_FRONT_RIGHT = 1;
    public static final int WINDOW_REAR_LEFT = 2;
    public static final int WINDOW_REAR_RIGHT = 3;
    private final int window;

    public ConditionWindowPos(Float targetValue, String compareOpName, int window) {
        super(targetValue, compareOpName);
        this.window = window;
        if (window < 0 || window > 3) {
            throw new IllegalArgumentException("Invalid window: " + window);
        } else if (targetValue.floatValue() < 0.0f || targetValue.floatValue() > 100.0f) {
            throw new IllegalArgumentException("Invalid target window pos: " + targetValue);
        }
    }

    public int getWindow() {
        return this.window;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    public Float fetchCurrentValue() {
        CarBcmManager carBcmManager = CarClientManager.getInstance().getCarManager("xp_bcm");
        if (carBcmManager != null) {
            try {
                if (XUIConfig.isProductSeriesD()) {
                    int i = this.window;
                    if (i != 0) {
                        if (i != 1) {
                            if (i != 2) {
                                if (i == 3) {
                                    return Float.valueOf(carBcmManager.getRrdmWinPstState());
                                }
                                return null;
                            }
                            return Float.valueOf(carBcmManager.getRldmWinPstState());
                        }
                        return Float.valueOf(carBcmManager.getFrdmWinPstState());
                    }
                    return Float.valueOf(carBcmManager.getFldmWinPstState());
                }
                return Float.valueOf(carBcmManager.getWindowMovePosition(this.window));
            } catch (Exception e) {
                Conditions.ERROR("get window pos error, window = " + this.window + ", isSeriesD = " + XUIConfig.isProductSeriesD() + ", error = " + e.getMessage());
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
        if ((this.window == 0 && carPropertyValue.getPropertyId() == 559946855) || ((this.window == 1 && carPropertyValue.getPropertyId() == 559946856) || ((this.window == 2 && carPropertyValue.getPropertyId() == 559946854) || (this.window == 3 && carPropertyValue.getPropertyId() == 559946857)))) {
            updateCurrentValue((Float) carPropertyValue.getValue());
        }
    }

    public void onErrorEvent(int i, int i1) {
    }
}
