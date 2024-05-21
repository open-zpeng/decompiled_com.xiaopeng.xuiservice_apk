package com.xiaopeng.xuiservice.smart;

import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.avas.CarAvasManager;
import android.car.hardware.bcm.CarBcmManager;
import android.content.Context;
import android.os.Message;
import android.os.SystemProperties;
import com.xiaopeng.xuimanager.XUIServiceNotConnectedException;
import com.xiaopeng.xuimanager.smart.SmartManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.PrintWriter;
/* loaded from: classes5.dex */
public class SmartShowService extends BaseSmartService {
    private static final boolean DBG = true;
    private static final int DELAY_TIME_EFFECT_AC_CHARGE = 6200;
    private static final int DELAY_TIME_EFFECT_AWAKE = 27000;
    private static final int DELAY_TIME_EFFECT_DC_CHARGE = 3200;
    private static final int DELAY_TIME_EFFECT_FIND_CAR = 5000;
    private static final int DELAY_TIME_EFFECT_FULL_CHARGED = 44400;
    private static final int DELAY_TIME_EFFECT_SHOW_OFF = 4300;
    private static final int DELAY_TIME_EFFECT_SLEEP = 22300;
    private static final int DELAY_TIME_INTERVAL = 3000;
    private static final int MESSAGE_EFFECT_AC_CHARGE = 3;
    private static final int MESSAGE_EFFECT_AWAKE = 1;
    private static final int MESSAGE_EFFECT_DC_CHARGE = 4;
    private static final int MESSAGE_EFFECT_FIND_CAR = 0;
    private static final int MESSAGE_EFFECT_FULL_CHARGED = 2;
    private static final int MESSAGE_EFFECT_SHOW_OFF = 6;
    private static final int MESSAGE_EFFECT_SLEEP = 5;
    static boolean xpExpandControl;
    private CarAvasManager mCarAvasManager;
    private CarBcmManager.CarBcmEventCallback mCarBcmEventCallback;
    private CarBcmManager mCarBcmManager;
    private CarShowHandler mCarShowHandler;
    private SmartManager.SmartEventListener mSmartEventListener;
    private SmartManager mSmartManager;

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService, com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
    public /* bridge */ /* synthetic */ void debugDump(PrintWriter printWriter, String[] strArr) {
        super.debugDump(printWriter, strArr);
    }

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService
    public /* bridge */ /* synthetic */ void dump(PrintWriter printWriter, String[] strArr) {
        super.dump(printWriter, strArr);
    }

    static {
        xpExpandControl = SystemProperties.getInt("persist.sys.xiaopeng.xui.ExpandControl", 0) == 1;
    }

    private SmartShowService(Context context) {
        super(context);
        this.mCarShowHandler = new CarShowHandler(context);
    }

    public static SmartShowService getInstance() {
        return InstanceHolder.sService;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    public void initXUIManager() throws XUIServiceNotConnectedException {
        try {
            this.mSmartManager = (SmartManager) getXuiManager().getXUIServiceManager(XUIConfig.PROPERTY_SMART);
            this.mSmartEventListener = new SmartManager.SmartEventListener() { // from class: com.xiaopeng.xuiservice.smart.SmartShowService.1
                public void onErrorEvent(int i, int i1) {
                }

                public void onLightEffectFinishEvent(int effectName, int effectMode) {
                }
            };
            this.mSmartManager.registerListener(this.mSmartEventListener);
        } catch (XUIServiceNotConnectedException e) {
            LogUtil.e(this.TAG, "initXUIManager error");
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void initCarListener() {
        this.mCarBcmEventCallback = new CarBcmManager.CarBcmEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartShowService.2
            public void onChangeEvent(CarPropertyValue value) {
                switch (value.getPropertyId()) {
                    case 557849623:
                        String str = SmartShowService.this.TAG;
                        LogUtil.i(str, "CarBcmManager.CarBcmEventCallback onChangeEvent : " + value.toString());
                        return;
                    case 557849624:
                        String str2 = SmartShowService.this.TAG;
                        LogUtil.i(str2, "CarBcmManager.CarBcmEventCallback onChangeEvent : " + value.toString());
                        return;
                    case 557849640:
                        String str3 = SmartShowService.this.TAG;
                        LogUtil.i(str3, "CarBcmManager.CarBcmEventCallback onChangeEvent : " + value.toString());
                        return;
                    case 557849760:
                    default:
                        return;
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        addBcmManagerListener(this.mCarBcmEventCallback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        LogUtil.d(this.TAG, "onCarManagerInited");
        this.mCarBcmManager = getCarManager("xp_bcm");
        this.mCarAvasManager = getCarManager("xp_avas");
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        removeBcmManagerListener(this.mCarBcmEventCallback);
        try {
            if (this.mSmartManager != null) {
                this.mSmartManager.unregisterListener(this.mSmartEventListener);
            }
        } catch (XUIServiceNotConnectedException e) {
            LogUtil.e(this.TAG, "onRelease error");
        }
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final SmartShowService sService = new SmartShowService(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    /* loaded from: classes5.dex */
    public class CarShowHandler extends XuiWorkHandler {
        private Context context;

        public CarShowHandler(Context context) {
            this.context = context;
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String str = SmartShowService.this.TAG;
            LogUtil.d(str, "handleMessage: msg.what=" + msg.what);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected boolean needConnectXUI() {
        return true;
    }
}
