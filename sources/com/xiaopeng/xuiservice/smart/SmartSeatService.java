package com.xiaopeng.xuiservice.smart;

import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.mcu.CarMcuManager;
import android.content.Context;
import com.xiaopeng.xuimanager.XUIServiceNotConnectedException;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.capabilities.smm.XpSeatMassagerProxy;
import java.io.PrintWriter;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/* loaded from: classes5.dex */
public class SmartSeatService extends BaseSmartService {
    private static final String TAG = "SmartSeatService";
    private static boolean hasIgOn = false;
    private CarMcuManager.CarMcuEventCallback mMcuCallback;
    private XpSeatMassagerProxy mProxy;

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService, com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
    public /* bridge */ /* synthetic */ void debugDump(PrintWriter printWriter, String[] strArr) {
        super.debugDump(printWriter, strArr);
    }

    private void addMcuManagerCallback() {
        int nr = this.mProxy.getSeatMassagerNumbers();
        List<Integer> ids = (List) Stream.iterate(0, new UnaryOperator() { // from class: com.xiaopeng.xuiservice.smart.-$$Lambda$SmartSeatService$SimUVDM0kEt4Y8t0nO442JEPwJs
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Integer valueOf;
                valueOf = Integer.valueOf(((Integer) obj).intValue() + 1);
                return valueOf;
            }
        }).limit(nr).collect(Collectors.toList());
        final List<String> strIds = (List) ids.stream().map(new Function() { // from class: com.xiaopeng.xuiservice.smart.-$$Lambda$znfQj8LqOvyui6ncUHU4komPIHY
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return String.valueOf((Integer) obj);
            }
        }).collect(Collectors.toList());
        this.mMcuCallback = new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartSeatService.1
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557847561) {
                    boolean unused = SmartSeatService.hasIgOn = ((Integer) value.getValue()).intValue() == 1;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onChangeEvent, hasIgOn=");
                    sb.append(SmartSeatService.hasIgOn ? "ON" : "OFF, stop massager and vibrate");
                    LogUtil.i(SmartSeatService.TAG, sb.toString());
                    if (!SmartSeatService.hasIgOn) {
                        SmartSeatService.this.mProxy.stopMassager(strIds);
                        SmartSeatService.this.mProxy.stopVibrate();
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

    /* JADX WARN: Incorrect condition in loop: B:6:0x0017 */
    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void dump(java.io.PrintWriter r19, java.lang.String[] r20) {
        /*
            Method dump skipped, instructions count: 486
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xuiservice.smart.SmartSeatService.dump(java.io.PrintWriter, java.lang.String[]):void");
    }

    public SmartSeatService(Context context) {
        super(context);
        this.mMcuCallback = null;
        this.mProxy = XpSeatMassagerProxy.getInstance();
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final SmartSeatService sService = new SmartSeatService(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    public static SmartSeatService getInstance() {
        return InstanceHolder.sService;
    }
}
