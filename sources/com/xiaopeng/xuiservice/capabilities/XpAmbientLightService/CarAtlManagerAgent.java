package com.xiaopeng.xuiservice.capabilities.XpAmbientLightService;

import android.app.ActivityThread;
import android.car.hardware.atl.AtlConfiguration;
import android.car.hardware.atl.CarAtlManager;
import android.content.Context;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.carcontrol.BaseCarListener;
import com.xiaopeng.xuiservice.utils.CommonUtils;
/* loaded from: classes5.dex */
public class CarAtlManagerAgent extends BaseCarListener {
    private static final boolean DBG = true;
    private static final String TAG = "CarAtlManagerAgent";
    private boolean isIgOn;
    private long lastAtlLogTime;
    private CarAtlManager mCarAtlManager;

    private CarAtlManagerAgent(Context context) {
        super(context);
        this.isIgOn = true;
        this.lastAtlLogTime = 0L;
    }

    public static CarAtlManagerAgent getInstance() {
        return InstanceHolder.sService;
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        LogUtil.d(TAG, "onCarManagerInited()");
        this.mCarAtlManager = getCarManager("xp_atl");
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
    }

    public void setThemeFirstColor(int color) {
        try {
            this.mCarAtlManager.setThemeFirstColor(color);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void setThemeSecondColor(int color) {
        try {
            this.mCarAtlManager.setThemeSecondColor(color);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void setDoubleThemeColor(int status) {
        try {
            this.mCarAtlManager.setDoubleThemeColor(status);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void setAtlOpen(int status) {
        try {
            this.mCarAtlManager.setAtlOpen(status);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void setAtlConfiguration(AtlConfiguration config) {
        try {
            this.mCarAtlManager.setAtlConfiguration(config);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void setTwoLightData(byte protocol, byte[] lightPosition, boolean hold, byte[] color, byte[] bright, byte[] fade) {
        LogUtil.i(TAG, "setTwoLightData " + ((int) protocol) + " " + ((int) lightPosition[0]) + " " + hold + " " + ((int) color[0]) + " " + ((int) bright[0]) + " " + ((int) fade[0]) + " isIgOn:" + this.isIgOn);
        try {
            if (this.isIgOn) {
                this.mCarAtlManager.setTwoLightData(protocol, lightPosition, hold, color, bright, fade);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void setGroutLightData(byte groupNum, byte solution, int lightlist, boolean hold, byte color, byte bright, byte fade) {
        try {
        } catch (Exception e) {
            e = e;
        }
        try {
            if (!this.isIgOn && bright != 0) {
                printAtlLog("setGroutLightData", groupNum, bright);
            }
            this.mCarAtlManager.setGroutLightData(groupNum, solution, lightlist, hold, color, bright, fade);
        } catch (Exception e2) {
            e = e2;
            handleException(e);
        }
    }

    public void setAllLightData(boolean hold, byte[] color, byte[] bright, byte[] fade) {
        try {
            if (this.isIgOn) {
                this.mCarAtlManager.setAllLightData(hold, color, bright, fade);
            } else {
                printAtlLog("setAllLightData", 0, 0);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final CarAtlManagerAgent sService = new CarAtlManagerAgent(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    public void setIgOnStatus(boolean enable) {
        LogUtil.i(TAG, "setIgOnStatus:" + enable);
        this.isIgOn = enable;
    }

    private void printAtlLog(String func, int pamram1, int pamram2) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.lastAtlLogTime > CommonUtils.xuiLogInterval) {
            LogUtil.i(TAG, "printAtlLog isIgOn:" + this.isIgOn + " func:" + func + ", " + pamram1 + ", " + pamram2);
            this.lastAtlLogTime = currentTime;
        }
    }
}
