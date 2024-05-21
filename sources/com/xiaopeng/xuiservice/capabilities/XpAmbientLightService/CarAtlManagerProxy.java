package com.xiaopeng.xuiservice.capabilities.XpAmbientLightService;

import android.app.ActivityThread;
import android.car.hardware.atl.AtlConfiguration;
import android.car.hardware.atl.CarAtlManager;
import android.content.Context;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.carcontrol.BaseCarListener;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
/* loaded from: classes5.dex */
public class CarAtlManagerProxy extends BaseCarListener implements AmbientLightHal {
    private static final boolean DBG = true;
    private static final String TAG = "CarAtlManagerProxy";

    public CarAtlManagerProxy(Context context) {
        super(context);
    }

    public static CarAtlManagerProxy getInstance() {
        return InstanceHolder.sService;
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        LogUtil.d(TAG, "onCarManagerInited()");
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHal
    public void setThemeFirstColor(int color) {
        LogUtil.d(TAG, " setThemeFirstColor " + color);
        try {
            getCarAtlManager().setThemeFirstColor(color);
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHal
    public void setThemeSecondColor(int color) {
        LogUtil.d(TAG, " setThemeSecondColor " + color);
        try {
            getCarAtlManager().setThemeSecondColor(color);
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHal
    public void setDoubleThemeColor(int status) {
        LogUtil.d(TAG, " setDoubleThemeColor " + status);
        try {
            getCarAtlManager().setDoubleThemeColor(status);
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHal
    public void setBrightnessLevel(int bright) {
        LogUtil.d(TAG, " setBrightnessLevel " + bright);
        try {
            getCarAtlManager().setBrightnessLevel(bright);
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHal
    public void setAtlOpen(int status) {
        LogUtil.d(TAG, " setAtlOpen " + status);
        try {
            getCarAtlManager().setAtlOpen(status);
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHal
    public void setAtlConfiguration(AtlConfiguration config) {
        try {
            getCarAtlManager().setAtlConfiguration(config);
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHal
    public void setTwoLightData(byte protocol, byte[] lightPosition, boolean hold, byte[] color, byte[] bright, byte[] fade) {
        LogUtil.i(TAG, "setTwoLightData " + ((int) protocol) + " " + ((int) lightPosition[0]) + " " + hold + " " + ((int) color[0]) + " " + ((int) bright[0]) + " " + ((int) fade[0]));
        try {
            getCarAtlManager().setTwoLightData(protocol, lightPosition, hold, color, bright, fade);
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHal
    public void setGroutLightData(byte groupNum, byte solution, int lightlist, boolean hold, byte color, byte bright, byte fade) {
        try {
            getCarAtlManager().setGroutLightData(groupNum, solution, lightlist, hold, color, bright, fade);
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHal
    public void setAllLightData(byte[] color, byte[] bright, byte[] fade) {
        try {
            getCarAtlManager().setAllLightData(false, color, bright, fade);
        } catch (Exception e) {
            handleException(e);
        }
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final CarAtlManagerProxy sService = new CarAtlManagerProxy(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    private CarAtlManager getCarAtlManager() {
        return CarClientManager.getInstance().getCarManager("xp_atl");
    }
}
