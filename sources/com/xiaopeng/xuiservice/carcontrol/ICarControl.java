package com.xiaopeng.xuiservice.carcontrol;

import android.car.CarManagerBase;
import android.car.diagnostic.XpDiagnosticManager;
import android.car.hardware.amp.CarAmpManager;
import android.car.hardware.atl.CarAtlManager;
import android.car.hardware.avas.CarAvasManager;
import android.car.hardware.bcm.CarBcmManager;
import android.car.hardware.ciu.CarCiuManager;
import android.car.hardware.eps.CarEpsManager;
import android.car.hardware.esp.CarEspManager;
import android.car.hardware.hvac.CarHvacManager;
import android.car.hardware.icm.CarIcmManager;
import android.car.hardware.input.CarInputManager;
import android.car.hardware.llu.CarLluManager;
import android.car.hardware.mcu.CarMcuManager;
import android.car.hardware.msm.CarMsmManager;
import android.car.hardware.power.CarPowerManager;
import android.car.hardware.scu.CarScuManager;
import android.car.hardware.srs.CarSrsManager;
import android.car.hardware.tbox.CarTboxManager;
import android.car.hardware.tpms.CarTpmsManager;
import android.car.hardware.vcu.CarVcuManager;
import android.car.hardware.vpm.CarVpmManager;
import android.car.hardware.xpu.CarXpuManager;
/* loaded from: classes5.dex */
public interface ICarControl {
    void addAmpManagerListener(CarAmpManager.CarAmpEventCallback carAmpEventCallback);

    void addAtlManagerListener(CarAtlManager.CarAtlEventCallback carAtlEventCallback);

    void addAvasManagerListener(CarAvasManager.CarAvasEventCallback carAvasEventCallback);

    void addBcmManagerListener(CarBcmManager.CarBcmEventCallback carBcmEventCallback);

    void addCiuManagerListener(CarCiuManager.CarCiuEventCallback carCiuEventCallback);

    void addEpsManagerListener(CarEpsManager.CarEpsEventCallback carEpsEventCallback);

    void addEspManagerListener(CarEspManager.CarEspEventCallback carEspEventCallback);

    void addHvacManagerListener(CarHvacManager.CarHvacEventCallback carHvacEventCallback);

    void addIcmManagerListener(CarIcmManager.CarIcmEventCallback carIcmEventCallback);

    void addInputManagerListener(CarInputManager.CarInputEventCallback carInputEventCallback);

    void addLluManagerListener(CarLluManager.CarLluEventCallback carLluEventCallback);

    void addMcuManagerListener(CarMcuManager.CarMcuEventCallback carMcuEventCallback);

    void addMsmManagerListener(CarMsmManager.CarMsmEventCallback carMsmEventCallback);

    void addPowerStateListener(CarPowerManager.CarPowerStateListener carPowerStateListener);

    void addScuManagerListener(CarScuManager.CarScuEventCallback carScuEventCallback);

    void addSrsManagerListener(CarSrsManager.CarSrsEventCallback carSrsEventCallback);

    void addTboxManagerListener(CarTboxManager.CarTboxEventCallback carTboxEventCallback);

    void addTpmsManagerListener(CarTpmsManager.CarTpmsEventCallback carTpmsEventCallback);

    void addVcuManagerListener(CarVcuManager.CarVcuEventCallback carVcuEventCallback);

    void addVcuSpecialManagerListener(CarVcuManager.CarVcuEventCallback carVcuEventCallback);

    void addVpmManagerListener(CarVpmManager.CarVpmEventCallback carVpmEventCallback);

    void addXpDiagnosticManagerListener(XpDiagnosticManager.XpDiagnosticEventCallback xpDiagnosticEventCallback);

    void addXpuManagerListener(CarXpuManager.CarXpuEventCallback carXpuEventCallback);

    CarManagerBase getCarManager(String str);

    void removeAmpManagerListener(CarAmpManager.CarAmpEventCallback carAmpEventCallback);

    void removeAtlManagerListener(CarAtlManager.CarAtlEventCallback carAtlEventCallback);

    void removeAvasManagerListener(CarAvasManager.CarAvasEventCallback carAvasEventCallback);

    void removeBcmManagerListener(CarBcmManager.CarBcmEventCallback carBcmEventCallback);

    void removeCiuManagerListener(CarCiuManager.CarCiuEventCallback carCiuEventCallback);

    void removeEpsManagerListener(CarEpsManager.CarEpsEventCallback carEpsEventCallback);

    void removeEspManagerListener(CarEspManager.CarEspEventCallback carEspEventCallback);

    void removeHvacManagerListener(CarHvacManager.CarHvacEventCallback carHvacEventCallback);

    void removeIcmManagerListener(CarIcmManager.CarIcmEventCallback carIcmEventCallback);

    void removeInputManagerListener(CarInputManager.CarInputEventCallback carInputEventCallback);

    void removeLluManagerListener(CarLluManager.CarLluEventCallback carLluEventCallback);

    void removeMcuManagerListener(CarMcuManager.CarMcuEventCallback carMcuEventCallback);

    void removeMsmManagerListener(CarMsmManager.CarMsmEventCallback carMsmEventCallback);

    void removePowerStateListener(CarPowerManager.CarPowerStateListener carPowerStateListener);

    void removeScuManagerListener(CarScuManager.CarScuEventCallback carScuEventCallback);

    void removeSrsManagerListener(CarSrsManager.CarSrsEventCallback carSrsEventCallback);

    void removeTboxManagerListener(CarTboxManager.CarTboxEventCallback carTboxEventCallback);

    void removeTpmsManagerListener(CarTpmsManager.CarTpmsEventCallback carTpmsEventCallback);

    void removeVcuManagerListener(CarVcuManager.CarVcuEventCallback carVcuEventCallback);

    void removeVcuSpecialManagerListener(CarVcuManager.CarVcuEventCallback carVcuEventCallback);

    void removeVpmManagerListener(CarVpmManager.CarVpmEventCallback carVpmEventCallback);

    void removeXpDiagnosticManagerListener(XpDiagnosticManager.XpDiagnosticEventCallback xpDiagnosticEventCallback);

    void removeXpuManagerListener(CarXpuManager.CarXpuEventCallback carXpuEventCallback);

    default void addConnectionListener(IServiceConn con) {
    }

    default void removeConnectionListener(IServiceConn con) {
    }
}
