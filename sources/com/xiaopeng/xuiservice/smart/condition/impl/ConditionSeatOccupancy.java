package com.xiaopeng.xuiservice.smart.condition.impl;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.bcm.CarBcmManager;
import android.car.hardware.srs.CarSrsManager;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.smart.condition.ConditionEq;
import com.xiaopeng.xuiservice.smart.condition.Conditions;
/* loaded from: classes5.dex */
public class ConditionSeatOccupancy extends ConditionEq<Integer> implements CarBcmManager.CarBcmEventCallback, CarSrsManager.CarSrsEventCallback {
    public static final int SEAT_6 = 6;
    public static final int SEAT_7 = 7;
    public static final int SEAT_DRIVER = 1;
    public static final int SEAT_FRONT_RIGHT = 2;
    public static final int SEAT_NOT_OCCUPIED = 0;
    public static final int SEAT_OCCUPIED = 1;
    public static final int SEAT_REAR_LEFT = 3;
    public static final int SEAT_REAR_MIDDLE = 4;
    public static final int SEAT_REAR_RIGHT = 5;
    public static final String TYPE = "seat";
    private final int seat;

    public ConditionSeatOccupancy(Integer targetValue, int seat) {
        super(targetValue);
        this.seat = seat;
        if (seat < 1 || seat > 7) {
            throw new IllegalArgumentException("Invalid seat = " + seat);
        } else if (targetValue.intValue() != 0 && targetValue.intValue() != 1) {
            throw new IllegalArgumentException("Invalid target seat occupancy = " + targetValue);
        }
    }

    public int getSeat() {
        return this.seat;
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue, com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void onStartWatch() {
        if (this.seat == 1) {
            CarClientManager.getInstance().addBcmManagerListener(this);
        } else {
            CarClientManager.getInstance().addSrsManagerListener(this);
        }
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue, com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void onStopWatch() {
        if (this.seat == 1) {
            CarClientManager.getInstance().removeBcmManagerListener(this);
        } else {
            CarClientManager.getInstance().removeSrsManagerListener(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    public Integer fetchCurrentValue() {
        if (this.seat == 1) {
            CarBcmManager carBcmManager = CarClientManager.getInstance().getCarManager("xp_bcm");
            if (carBcmManager != null) {
                try {
                    return Integer.valueOf(carBcmManager.getDriverOnSeat());
                } catch (Exception e) {
                    Conditions.ERROR("CarBcmManager getDriverOnSeat error, " + e.getMessage());
                }
            }
        } else {
            CarSrsManager carSrsManager = CarClientManager.getInstance().getCarManager("xp_srs");
            if (carSrsManager != null) {
                switch (this.seat) {
                    case 2:
                        try {
                            return Integer.valueOf(carSrsManager.getPsnOnSeat());
                        } catch (Exception e2) {
                            Conditions.ERROR("CarSrsManager getPsnOnSeat() error, " + e2.getMessage());
                            break;
                        }
                    case 3:
                        try {
                            return Integer.valueOf(carSrsManager.getRearLeftSeatOccupancyStatus());
                        } catch (Exception e3) {
                            Conditions.ERROR("CarSrsManager getRearLeftSeatOccupancyStatus() error, " + e3.getMessage());
                            break;
                        }
                    case 4:
                        try {
                            return Integer.valueOf(carSrsManager.getRearMiddleSeatOccupancyStatus());
                        } catch (Exception e4) {
                            Conditions.ERROR("CarSrsManager getRearMiddleSeatOccupancyStatus() error, " + e4.getMessage());
                            break;
                        }
                    case 5:
                        try {
                            return Integer.valueOf(carSrsManager.getRearRightSeatOccupancyStatus());
                        } catch (Exception e5) {
                            Conditions.ERROR("CarSrsManager getRearRightSeatOccupancyStatus() error, " + e5.getMessage());
                            break;
                        }
                    case 6:
                    case 7:
                        return null;
                }
            }
        }
        return null;
    }

    public void onChangeEvent(CarPropertyValue carPropertyValue) {
        if ((this.seat == 1 && carPropertyValue.getPropertyId() == 557849607) || ((this.seat == 2 && carPropertyValue.getPropertyId() == 557849679) || ((this.seat == 3 && carPropertyValue.getPropertyId() == 557849800) || ((this.seat == 4 && carPropertyValue.getPropertyId() == 557849801) || (this.seat == 5 && carPropertyValue.getPropertyId() == 557849802))))) {
            updateCurrentValue((Integer) carPropertyValue.getValue());
        }
    }

    public void onErrorEvent(int i, int i1) {
    }
}
