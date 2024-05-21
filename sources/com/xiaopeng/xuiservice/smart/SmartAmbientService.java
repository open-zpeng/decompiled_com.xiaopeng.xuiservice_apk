package com.xiaopeng.xuiservice.smart;

import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.bcm.CarBcmManager;
import android.car.hardware.mcu.CarMcuManager;
import android.content.Context;
import com.xiaopeng.xuimanager.XUIServiceNotConnectedException;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.capabilities.ambientlight.XpAmbientLightProxy;
import com.xiaopeng.xuiservice.smart.action.Actions;
import java.io.PrintWriter;
/* loaded from: classes5.dex */
public class SmartAmbientService extends BaseSmartService {
    private static final String TAG = "SmartAmbientService";
    private boolean hasDoorOpen;
    private boolean hasIgOn;
    private boolean hasWelcomed;
    private CarBcmManager.CarBcmEventCallback mBcmCallback;
    private CarMcuManager.CarMcuEventCallback mMcuCallback;

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService, com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
    public /* bridge */ /* synthetic */ void debugDump(PrintWriter printWriter, String[] strArr) {
        super.debugDump(printWriter, strArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasPersonOnSeat() {
        CarBcmManager bcmManager = getCarManager("xp_bcm");
        try {
            return bcmManager.getDriverOnSeat() == 1;
        } catch (Exception e) {
            LogUtil.e(TAG, "hasPersonOnSeat failed, " + e);
            return false;
        }
    }

    private void addBcmManagerCallback() {
        this.mBcmCallback = new CarBcmManager.CarBcmEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartAmbientService.1
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557915161) {
                    Integer[] signalVal = (Integer[]) value.getValue();
                    boolean status = signalVal[0].intValue() == 1;
                    if (SmartAmbientService.this.hasDoorOpen != status) {
                        boolean hasPerson = SmartAmbientService.this.hasPersonOnSeat();
                        LogUtil.d(SmartAmbientService.TAG, "onChangeEvent, hasWelcomed=" + SmartAmbientService.this.hasWelcomed + ", hasDoorOpen=" + status + ", hasPerson=" + hasPerson);
                        if (!SmartAmbientService.this.hasWelcomed && hasPerson && !status) {
                            int ret = XpAmbientLightProxy.getInstance().playAmbientLightEffect(Actions.ACTION_ALL, "welcome", false);
                            StringBuilder sb = new StringBuilder();
                            sb.append("play welcome ambient ");
                            sb.append(ret == 0 ? "succeed" : "failed");
                            LogUtil.i(SmartAmbientService.TAG, sb.toString());
                            SmartAmbientService.this.hasWelcomed = true;
                        }
                        SmartAmbientService.this.hasDoorOpen = status;
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        addBcmManagerListener(this.mBcmCallback);
    }

    private void addMcuManagerCallback() {
        this.mMcuCallback = new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartAmbientService.2
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557847561) {
                    boolean status = ((Integer) value.getValue()).intValue() == 1;
                    if (SmartAmbientService.this.hasIgOn != status) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("onChangeEvent, hasIgOn=");
                        sb.append(status ? "ON" : "OFF");
                        LogUtil.i(SmartAmbientService.TAG, sb.toString());
                        if (status) {
                            SmartAmbientService.this.hasWelcomed = false;
                        }
                        SmartAmbientService.this.hasIgOn = status;
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        addMcuManagerListener(this.mMcuCallback);
    }

    private void removeCarListener() {
        removeMcuManagerListener(this.mMcuCallback);
        removeBcmManagerListener(this.mBcmCallback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        CarMcuManager mcuManager = getCarManager("xp_mcu");
        CarBcmManager bcmManager = getCarManager("xp_bcm");
        try {
            int[] status = bcmManager.getDoorsState();
            this.hasDoorOpen = status[0] == 1;
            this.hasIgOn = mcuManager.getIgStatusFromMcu() == 1;
        } catch (Exception e) {
            LogUtil.e(TAG, "onCarManagerInited failed, " + e);
        }
        LogUtil.i(TAG, "onCarManagerInited, hasIgOn=" + this.hasIgOn + ", hasDoorOpen=" + this.hasDoorOpen);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void initCarListener() {
        LogUtil.i(TAG, "initCarListener");
        addMcuManagerCallback();
        addBcmManagerCallback();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    public void initXUIManager() throws XUIServiceNotConnectedException {
        super.initXUIManager();
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
        LogUtil.i(TAG, "onInit");
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        LogUtil.i(TAG, "onRelease");
        removeCarListener();
    }

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService
    public void dump(PrintWriter pw, String[] args) {
    }

    public SmartAmbientService(Context context) {
        super(context);
        this.hasWelcomed = true;
        this.mMcuCallback = null;
        this.mBcmCallback = null;
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final SmartAmbientService sService = new SmartAmbientService(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    public static SmartAmbientService getInstance() {
        return InstanceHolder.sService;
    }
}
