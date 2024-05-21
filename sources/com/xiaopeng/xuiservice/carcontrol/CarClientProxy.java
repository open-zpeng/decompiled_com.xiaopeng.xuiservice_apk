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
public class CarClientProxy implements ICarControl {
    private ICarControl mCarControl = null;

    public void setCarControl(ICarControl control) {
        this.mCarControl = control;
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addConnectionListener(IServiceConn con) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null) {
            iCarControl.addConnectionListener(con);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeConnectionListener(IServiceConn con) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null) {
            iCarControl.removeConnectionListener(con);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addXpuManagerListener(CarXpuManager.CarXpuEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.addXpuManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addCiuManagerListener(CarCiuManager.CarCiuEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.addCiuManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addBcmManagerListener(CarBcmManager.CarBcmEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.addBcmManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addVcuManagerListener(CarVcuManager.CarVcuEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.addVcuManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addMcuManagerListener(CarMcuManager.CarMcuEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.addMcuManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addScuManagerListener(CarScuManager.CarScuEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.addScuManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addVpmManagerListener(CarVpmManager.CarVpmEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.addVpmManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addTboxManagerListener(CarTboxManager.CarTboxEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.addTboxManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addMsmManagerListener(CarMsmManager.CarMsmEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.addMsmManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addSrsManagerListener(CarSrsManager.CarSrsEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.addSrsManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addAvasManagerListener(CarAvasManager.CarAvasEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.addAvasManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addLluManagerListener(CarLluManager.CarLluEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.addLluManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addIcmManagerListener(CarIcmManager.CarIcmEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.addIcmManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addEspManagerListener(CarEspManager.CarEspEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.addEspManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addInputManagerListener(CarInputManager.CarInputEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.addInputManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addHvacManagerListener(CarHvacManager.CarHvacEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.addHvacManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addAtlManagerListener(CarAtlManager.CarAtlEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.addAtlManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addAmpManagerListener(CarAmpManager.CarAmpEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.addAmpManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addEpsManagerListener(CarEpsManager.CarEpsEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.addEpsManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addPowerStateListener(CarPowerManager.CarPowerStateListener listener) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && listener != null) {
            iCarControl.addPowerStateListener(listener);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addVcuSpecialManagerListener(CarVcuManager.CarVcuEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.addVcuSpecialManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addXpDiagnosticManagerListener(XpDiagnosticManager.XpDiagnosticEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.addXpDiagnosticManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addTpmsManagerListener(CarTpmsManager.CarTpmsEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.addTpmsManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeXpuManagerListener(CarXpuManager.CarXpuEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.removeXpuManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeCiuManagerListener(CarCiuManager.CarCiuEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.removeCiuManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeBcmManagerListener(CarBcmManager.CarBcmEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.removeBcmManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeVcuManagerListener(CarVcuManager.CarVcuEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.removeVcuManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeMcuManagerListener(CarMcuManager.CarMcuEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.removeMcuManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeScuManagerListener(CarScuManager.CarScuEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.removeScuManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeVpmManagerListener(CarVpmManager.CarVpmEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.removeVpmManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeTboxManagerListener(CarTboxManager.CarTboxEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.removeTboxManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeMsmManagerListener(CarMsmManager.CarMsmEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.removeMsmManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeSrsManagerListener(CarSrsManager.CarSrsEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.removeSrsManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeAvasManagerListener(CarAvasManager.CarAvasEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.removeAvasManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeLluManagerListener(CarLluManager.CarLluEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.removeLluManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeIcmManagerListener(CarIcmManager.CarIcmEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.removeIcmManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeEspManagerListener(CarEspManager.CarEspEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.removeEspManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeInputManagerListener(CarInputManager.CarInputEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.removeInputManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeHvacManagerListener(CarHvacManager.CarHvacEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.removeHvacManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeAtlManagerListener(CarAtlManager.CarAtlEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.removeAtlManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeAmpManagerListener(CarAmpManager.CarAmpEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.removeAmpManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeEpsManagerListener(CarEpsManager.CarEpsEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.removeEpsManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removePowerStateListener(CarPowerManager.CarPowerStateListener listener) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && listener != null) {
            iCarControl.removePowerStateListener(listener);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeVcuSpecialManagerListener(CarVcuManager.CarVcuEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.removeVcuSpecialManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeXpDiagnosticManagerListener(XpDiagnosticManager.XpDiagnosticEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.removeXpDiagnosticManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeTpmsManagerListener(CarTpmsManager.CarTpmsEventCallback callback) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null && callback != null) {
            iCarControl.removeTpmsManagerListener(callback);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public CarManagerBase getCarManager(String serviceName) {
        ICarControl iCarControl = this.mCarControl;
        if (iCarControl != null) {
            return iCarControl.getCarManager(serviceName);
        }
        return null;
    }
}
