package com.xiaopeng.speech.protocol.query.speech.charge;

import com.xiaopeng.speech.IQueryCaller;
/* loaded from: classes2.dex */
public interface ISpeechChargeQueryCaller extends IQueryCaller {
    float getACChargingCurrent();

    float getACChargingVolt();

    boolean getACInputStatus();

    int getAddedElectricity();

    String getAverageVehConsume();

    String getAverageVehConsume100km();

    boolean getBatteryCoolStatus();

    boolean getBatteryWarmingStatus();

    boolean getChargeGunLockSt();

    int getChargeLimitSoc();

    int getChargeStatus();

    int getChargingError();

    int getChargingGunStatus();

    int[] getChargingMaxSoc();

    boolean getColdWarningTips();

    float getDCChargingCurrent();

    float getDCChargingVolt();

    float getElectricityPercent();

    int getEnduranceMileageMode();

    float getHvacConsume();

    int getMileageNumber();

    float getSOH();

    int getWltpMileageNumber();

    default int getCltcMieageNumber() {
        return -1;
    }

    default float getDynamicMileageNumber() {
        return -1.0f;
    }
}
