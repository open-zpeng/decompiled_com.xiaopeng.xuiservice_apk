package com.xiaopeng.xuiservice.smart.condition.impl;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.srs.CarSrsManager;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.smart.condition.ConditionEq;
import com.xiaopeng.xuiservice.smart.condition.Conditions;
/* loaded from: classes5.dex */
public class ConditionBeltStatus extends ConditionEq<Integer> implements CarSrsManager.CarSrsEventCallback {
    public static final int BELT_6 = 6;
    public static final int BELT_7 = 7;
    public static final int BELT_DRIVER = 1;
    public static final int BELT_FRONT_RIGHT = 2;
    public static final int BELT_REAR_LEFT = 3;
    public static final int BELT_REAR_MIDDLE = 4;
    public static final int BELT_REAR_RIGHT = 5;
    public static final int NO_WARNING = 0;
    public static final String TYPE = "belt";
    public static final int WARNING = 1;
    private final int belt;

    public ConditionBeltStatus(Integer targetValue, int belt) {
        super(targetValue);
        this.belt = belt;
        if (belt < 1 || belt > 7) {
            throw new IllegalArgumentException("Invalid belt = " + belt);
        } else if (targetValue.intValue() != 0 && targetValue.intValue() != 1) {
            throw new IllegalArgumentException("Invalid target belt status = " + targetValue);
        }
    }

    public int getBelt() {
        return this.belt;
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue, com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void onStartWatch() {
        CarClientManager.getInstance().addSrsManagerListener(this);
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue, com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void onStopWatch() {
        CarClientManager.getInstance().removeSrsManagerListener(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    public Integer fetchCurrentValue() {
        CarSrsManager carSrsManager = CarClientManager.getInstance().getCarManager("xp_srs");
        if (carSrsManager != null) {
            switch (this.belt) {
                case 1:
                    try {
                        return Integer.valueOf(carSrsManager.getDriverBeltStatus());
                    } catch (Exception e) {
                        Conditions.ERROR("CarSrsManager getDriverBeltStatus() error, " + e.getMessage());
                        break;
                    }
                case 2:
                    try {
                        return Integer.valueOf(carSrsManager.getPsnBeltStatus());
                    } catch (Exception e2) {
                        Conditions.ERROR("CarSrsManager getPsnBeltStatus() error, " + e2.getMessage());
                        break;
                    }
                case 3:
                    try {
                        return Integer.valueOf(carSrsManager.getBackLeftBeltStatus());
                    } catch (Exception e3) {
                        Conditions.ERROR("CarSrsManager getBackLeftBeltStatus() error, " + e3.getMessage());
                        break;
                    }
                case 4:
                    try {
                        return Integer.valueOf(carSrsManager.getBackMiddleBeltStatus());
                    } catch (Exception e4) {
                        Conditions.ERROR("CarSrsManager getBackMiddleBeltStatus() error, " + e4.getMessage());
                        break;
                    }
                case 5:
                    try {
                        return Integer.valueOf(carSrsManager.getBackRightBeltStatus());
                    } catch (Exception e5) {
                        Conditions.ERROR("CarSrsManager getBackRightBeltStatus() error, " + e5.getMessage());
                        break;
                    }
                case 6:
                case 7:
                    return null;
            }
        }
        return null;
    }

    public void onChangeEvent(CarPropertyValue carPropertyValue) {
        if ((this.belt == 1 && carPropertyValue.getPropertyId() == 557849612) || ((this.belt == 2 && carPropertyValue.getPropertyId() == 557849613) || ((this.belt == 3 && carPropertyValue.getPropertyId() == 557849614) || ((this.belt == 4 && carPropertyValue.getPropertyId() == 557849615) || (this.belt == 5 && carPropertyValue.getPropertyId() == 557849616))))) {
            updateCurrentValue((Integer) carPropertyValue.getValue());
        }
    }

    public void onErrorEvent(int i, int i1) {
    }
}
