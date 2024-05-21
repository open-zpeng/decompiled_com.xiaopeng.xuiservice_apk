package com.xiaopeng.xuiservice.smart;

import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.mcu.CarMcuManager;
import android.content.Context;
import com.xiaopeng.xuimanager.XUIServiceNotConnectedException;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.capabilities.makeup.XpMakeupLightProxy;
import java.io.PrintWriter;
/* loaded from: classes5.dex */
public class SmartMakeupService extends BaseSmartService {
    private static final String TAG = "SmartMakeupService";
    private static boolean hasIgOn = false;
    private CarMcuManager.CarMcuEventCallback mMcuCallback;
    private XpMakeupLightProxy mProxy;

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService, com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
    public /* bridge */ /* synthetic */ void debugDump(PrintWriter printWriter, String[] strArr) {
        super.debugDump(printWriter, strArr);
    }

    private void addMcuManagerCallback() {
        this.mMcuCallback = new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartMakeupService.1
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557847561) {
                    boolean unused = SmartMakeupService.hasIgOn = ((Integer) value.getValue()).intValue() == 1;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onChangeEvent, hasIgOn=");
                    sb.append(SmartMakeupService.hasIgOn ? "ON" : "OFF");
                    LogUtil.i(SmartMakeupService.TAG, sb.toString());
                    if (SmartMakeupService.this.mProxy.getMakeupAvailable() != 0) {
                        SmartMakeupService.this.mProxy.setMakeupLightStatus(SmartMakeupService.hasIgOn);
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
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        try {
            CarMcuManager mcuManager = getCarManager("xp_mcu");
            boolean z = true;
            if (mcuManager.getIgStatusFromMcu() != 1) {
                z = false;
            }
            hasIgOn = z;
        } catch (Exception e) {
            LogUtil.e(TAG, "getIgStatusFromMcu failed, " + e);
        }
        LogUtil.i(TAG, "onCarManagerInited, hasIgOn=" + hasIgOn);
        if (this.mProxy.getMakeupAvailable() != 0) {
            this.mProxy.setMakeupLightStatus(hasIgOn);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void initCarListener() {
        addMcuManagerCallback();
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

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Incorrect condition in loop: B:6:0x0028 */
    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void dump(java.io.PrintWriter r17, java.lang.String[] r18) {
        /*
            Method dump skipped, instructions count: 478
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xuiservice.smart.SmartMakeupService.dump(java.io.PrintWriter, java.lang.String[]):void");
    }

    public SmartMakeupService(Context context) {
        super(context);
        this.mMcuCallback = null;
        this.mProxy = XpMakeupLightProxy.getInstance();
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final SmartMakeupService sService = new SmartMakeupService(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    public static SmartMakeupService getInstance() {
        return InstanceHolder.sService;
    }
}
