package com.xiaopeng.xuiservice.carcontrol;

import android.app.ActivityThread;
import android.car.CarManagerBase;
import android.car.CarNotConnectedException;
import android.car.diagnostic.XpDiagnosticManager;
import android.car.hardware.CarEcuManager;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.amp.CarAmpManager;
import android.car.hardware.atl.CarAtlManager;
import android.car.hardware.avas.CarAvasManager;
import android.car.hardware.bcm.CarBcmManager;
import android.car.hardware.ciu.CarCiuManager;
import android.car.hardware.condition.CarConditionInfo;
import android.car.hardware.condition.CarConditionManager;
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
import android.content.Context;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import com.xiaopeng.xuiservice.utils.TimingLog;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Executor;
/* loaded from: classes5.dex */
public class CarClientManager extends BaseCarControl implements ICarControl {
    private static final long EVENT_DISPATCH_TIME_INTERVAL_MS = 1000;
    private final String CAR_VCU_SPECIAL;
    private boolean DEBUG_EVENT_LOG;
    private final int ECU_CALLBACK_NOT_REGISTER;
    private final int ECU_CALLBACK_REGISTED;
    private final int ECU_CALLBACK_REGISTING;
    private CarConditionManager.CarConditionCallback carConditionEventListener;
    private CarAmpManager.CarAmpEventCallback mCarAmpEventCallback;
    private CarAmpManager mCarAmpManager;
    private CarAtlManager.CarAtlEventCallback mCarAtlEventCallback;
    private CarAtlManager mCarAtlManager;
    private CarAvasManager.CarAvasEventCallback mCarAvasEventCallback;
    private CarAvasManager mCarAvasManager;
    private CarBcmManager.CarBcmEventCallback mCarBcmEventCallback;
    private CarBcmManager mCarBcmManager;
    private CarCiuManager.CarCiuEventCallback mCarCiuEventCallback;
    private CarCiuManager mCarCiuManager;
    private final Map<String, CarManagerBase> mCarClientMap;
    private CarConditionManager mCarConditionManager;
    private CarEpsManager.CarEpsEventCallback mCarEpsEventCallback;
    private CarEpsManager mCarEpsManager;
    private CarEspManager.CarEspEventCallback mCarEspEventCallback;
    private CarEspManager mCarEspManager;
    private CarHvacManager.CarHvacEventCallback mCarHvacEventCallback;
    private CarHvacManager mCarHvacManager;
    private CarIcmManager.CarIcmEventCallback mCarIcmEventCallback;
    private CarIcmManager mCarIcmManager;
    private CarInputManager.CarInputEventCallback mCarInputEventCallback;
    private CarInputManager mCarInputManager;
    private CarLluManager.CarLluEventCallback mCarLluEventCallback;
    private CarLluManager mCarLluManager;
    private CarMcuManager.CarMcuEventCallback mCarMcuEventCallback;
    private CarMcuManager mCarMcuManager;
    private CarMsmManager.CarMsmEventCallback mCarMsmEventCallback;
    private CarMsmManager mCarMsmManager;
    private CarPowerManager mCarPowerManager;
    private CarPowerManager.CarPowerStateListener mCarPowerStateListener;
    private CarScuManager.CarScuEventCallback mCarScuEventCallback;
    private CarScuManager mCarScuManager;
    private CarSrsManager.CarSrsEventCallback mCarSrsEventCallback;
    private CarSrsManager mCarSrsManager;
    private CarTboxManager.CarTboxEventCallback mCarTboxEventCallback;
    private CarTboxManager mCarTboxManager;
    private CarTpmsManager.CarTpmsEventCallback mCarTpmsEventCallback;
    private CarTpmsManager mCarTpmsManager;
    private CarVcuManager.CarVcuEventCallback mCarVcuEventCallback;
    private CarVcuManager mCarVcuManager;
    private CarVcuManager.CarVcuEventCallback mCarVcuSpecialEventCallback;
    private CarVpmManager.CarVpmEventCallback mCarVpmEventCallback;
    private CarVpmManager mCarVpmManager;
    private CarXpuManager.CarXpuEventCallback mCarXpuEventCallback;
    private CarXpuManager mCarXpuManager;
    private ArrayList<IServiceConn> mConnListener;
    private ArrayMap<String, Integer> mEcuCallbackRegister;
    private ArrayMap<String, ArrayList<CarEcuManager.CarEcuEventCallback>> mListener;
    private ArrayList<CarPowerManager.CarPowerStateListener> mPowerListener;
    private TimingLog mTimingTimeOutLog;
    private ArrayList<String> mXUICarEcuList;
    private XpDiagnosticManager.XpDiagnosticEventCallback mXpDiagnosticEventCallback;
    private XpDiagnosticManager mXpDiagnosticManager;
    private static boolean CarCallBackInited = false;
    private static LinkedList<EventRecord> mEventList = new LinkedList<>();
    private static boolean mRecordEvents = SystemProperties.getBoolean("persist.sys.xiaopeng.xui.car.debug", false);
    private static int mEventListMaxSize = SystemProperties.getInt("persist.sys.xiaopeng.xui.car.eventsize", 21600);
    private static final ArraySet<Integer> mUnInterestedIds = new ArraySet<>(Arrays.asList(557856777));

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarControl
    public /* bridge */ /* synthetic */ void init() {
        super.init();
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarControl
    public /* bridge */ /* synthetic */ void release() {
        super.release();
    }

    public CarClientManager(Context context) {
        super(context);
        this.DEBUG_EVENT_LOG = SystemProperties.getBoolean("persist.xuiservice.event.log.enable", false);
        this.CAR_VCU_SPECIAL = "VCU_SPECIAL";
        this.mListener = new ArrayMap<>();
        this.mPowerListener = new ArrayList<>();
        this.mEcuCallbackRegister = new ArrayMap<>();
        this.ECU_CALLBACK_NOT_REGISTER = 0;
        this.ECU_CALLBACK_REGISTING = 1;
        this.ECU_CALLBACK_REGISTED = 2;
        this.mXUICarEcuList = new ArrayList<>(Arrays.asList("xp_xpu", "xp_ciu", "xp_bcm", "xp_vcu", "xp_mcu", "xp_scu", "xp_vpm", "xp_tbox", "xp_msm", "xp_srs", "xp_avas", "xp_llu", "xp_icm", "xp_esp", "xp_input", "hvac", "xp_atl", "xp_eps", "xp_tpms", "xp_diagnostic"));
        this.mConnListener = new ArrayList<>();
        this.mTimingTimeOutLog = new TimingLog("CarClientManager:onChangeEvent");
        this.mCarClientMap = new HashMap();
        this.carConditionEventListener = new CarConditionManager.CarConditionCallback() { // from class: com.xiaopeng.xuiservice.carcontrol.CarClientManager.1
            public void onChangeEvent(CarPropertyValue value) {
                CarClientManager.this.dispatchEvent("xp_vcu", value);
            }
        };
        this.mListener.clear();
        this.mConnListener.clear();
        DumpDispatcher.registerDump("carapi", new DumpDispatcher.DebugDumper() { // from class: com.xiaopeng.xuiservice.carcontrol.-$$Lambda$CarClientManager$8LEMONbYq_viPuh8st96i90RCGg
            @Override // com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
            public final void debugDump(PrintWriter printWriter, String[] strArr) {
                CarClientManager.this.lambda$new$0$CarClientManager(printWriter, strArr);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class InstanceHolder {
        private static final CarClientManager sService = new CarClientManager(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    public static CarClientManager getInstance() {
        return InstanceHolder.sService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class EventRecord {
        private static SimpleDateFormat formatter;
        ArrayList<CarEcuManager.CarEcuEventCallback> callbacks;
        public long eventTick;
        public int propertyId;
        public String serviceId;

        private EventRecord() {
        }

        public String toString() {
            if (formatter == null) {
                formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
            }
            StringBuilder sb = new StringBuilder();
            sb.append(formatter.format(new Date(this.eventTick)));
            sb.append(",svc-");
            sb.append(this.serviceId);
            sb.append(",prop-");
            sb.append(this.propertyId);
            sb.append("@");
            sb.append(hashCode());
            sb.append("\n");
            Iterator<CarEcuManager.CarEcuEventCallback> it = this.callbacks.iterator();
            while (it.hasNext()) {
                CarEcuManager.CarEcuEventCallback callback = it.next();
                sb.append(callback);
                sb.append("\n");
            }
            return sb.toString();
        }
    }

    /* renamed from: dump */
    public void lambda$new$0$CarClientManager(PrintWriter pw, String[] args) {
        pw.println("dump-" + this.TAG);
        if (args == null) {
            pw.println("please input cmds");
            return;
        }
        for (String str : args) {
            char c = 65535;
            if (str.hashCode() == 547048436 && str.equals("-eventall")) {
                c = 0;
            }
            if (c == 0) {
                synchronized (mEventList) {
                    pw.println("event size:" + mEventList.size());
                    pw.println("capacity:" + mEventListMaxSize);
                    pw.println("event list:");
                    Iterator<EventRecord> it = mEventList.iterator();
                    while (it.hasNext()) {
                        EventRecord record = it.next();
                        pw.println(record);
                    }
                }
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchEvent(String serviceid, CarPropertyValue value) {
        ArrayList<CarEcuManager.CarEcuEventCallback> callbacks;
        long enterTick = SystemClock.elapsedRealtime();
        if (this.DEBUG_EVENT_LOG) {
            String str = this.TAG;
            LogUtil.i(str, "dispatchEvent value=" + value);
        }
        synchronized (this.mListener) {
            callbacks = this.mListener.get(serviceid);
        }
        if (callbacks != null) {
            if (mRecordEvents && !mUnInterestedIds.contains(Integer.valueOf(value.getPropertyId()))) {
                EventRecord record = new EventRecord();
                record.eventTick = System.currentTimeMillis();
                record.serviceId = serviceid;
                record.propertyId = value.getPropertyId();
                try {
                    record.callbacks = (ArrayList) callbacks.clone();
                    synchronized (mEventList) {
                        if (mEventList.size() >= mEventListMaxSize) {
                            EventRecord first = mEventList.removeFirst();
                            if (first.callbacks != null) {
                                first.callbacks.clear();
                                first.callbacks = null;
                            }
                        }
                        mEventList.add(record);
                    }
                } catch (Exception e) {
                    String str2 = this.TAG;
                    LogUtil.w(str2, "dispatchEvent e=" + e);
                }
            }
            synchronized (callbacks) {
                Iterator<CarEcuManager.CarEcuEventCallback> it = callbacks.iterator();
                while (it.hasNext()) {
                    CarEcuManager.CarEcuEventCallback callback = it.next();
                    callback.onChangeEvent(value);
                }
            }
        }
        long delta = SystemClock.elapsedRealtime() - enterTick;
        if (delta > 1000) {
            String str3 = this.TAG;
            LogUtil.w(str3, "dispatchEvent slow,time=" + delta + ",service=" + serviceid + ",prop id=" + value.getPropertyId());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchErrorEvent(String serviceid, int propertyId, int zone) {
        ArrayList<CarEcuManager.CarEcuEventCallback> callbacks;
        long enterTick = SystemClock.elapsedRealtime();
        if (this.DEBUG_EVENT_LOG) {
            String str = this.TAG;
            LogUtil.i(str, "dispatchErrorEvent serviceid=" + serviceid + " propertyId=" + propertyId);
        }
        synchronized (this.mListener) {
            callbacks = this.mListener.get(serviceid);
        }
        if (callbacks != null) {
            synchronized (callbacks) {
                Iterator<CarEcuManager.CarEcuEventCallback> it = callbacks.iterator();
                while (it.hasNext()) {
                    CarEcuManager.CarEcuEventCallback callback = it.next();
                    callback.onErrorEvent(propertyId, zone);
                }
            }
        }
        long delta = SystemClock.elapsedRealtime() - enterTick;
        if (delta > 1000) {
            String str2 = this.TAG;
            LogUtil.w(str2, "dispatchErrorEvent slow,time=" + delta + ",service=" + serviceid + ",prop id=" + propertyId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchPowerEvent(int state) {
        long enterTick = SystemClock.elapsedRealtime();
        if (this.DEBUG_EVENT_LOG) {
            String str = this.TAG;
            LogUtil.i(str, "dispatchPowerEvent state=" + state);
        }
        ArrayList<CarPowerManager.CarPowerStateListener> arrayList = this.mPowerListener;
        if (arrayList != null) {
            Iterator<CarPowerManager.CarPowerStateListener> it = arrayList.iterator();
            while (it.hasNext()) {
                CarPowerManager.CarPowerStateListener listener = it.next();
                TimingLog timingLog = this.mTimingTimeOutLog;
                timingLog.startLog(listener.toString() + "[ProertyId=" + state + "]");
                listener.onStateChanged(state);
                this.mTimingTimeOutLog.endLog();
            }
        }
        long delta = SystemClock.elapsedRealtime() - enterTick;
        if (delta > 1000) {
            String str2 = this.TAG;
            LogUtil.w(str2, "dispatchPowerEvent slow,time=" + delta + ",state=" + state);
        }
    }

    private void initCarCallback() {
        if (CarCallBackInited) {
            return;
        }
        LogUtil.d(this.TAG, "initCarCallback()");
        this.mCarXpuEventCallback = new CarXpuManager.CarXpuEventCallback() { // from class: com.xiaopeng.xuiservice.carcontrol.CarClientManager.2
            public void onChangeEvent(CarPropertyValue value) {
                CarClientManager.this.dispatchEvent("xp_xpu", value);
            }

            public void onErrorEvent(int propertyId, int zone) {
                CarClientManager.this.dispatchErrorEvent("xp_xpu", propertyId, zone);
            }
        };
        this.mCarCiuEventCallback = new CarCiuManager.CarCiuEventCallback() { // from class: com.xiaopeng.xuiservice.carcontrol.CarClientManager.3
            public void onChangeEvent(CarPropertyValue value) {
                CarClientManager.this.dispatchEvent("xp_ciu", value);
            }

            public void onErrorEvent(int propertyId, int zone) {
                CarClientManager.this.dispatchErrorEvent("xp_ciu", propertyId, zone);
            }
        };
        this.mCarVcuEventCallback = new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xuiservice.carcontrol.CarClientManager.4
            public void onChangeEvent(CarPropertyValue value) {
                CarClientManager.this.dispatchEvent("xp_vcu", value);
            }

            public void onErrorEvent(int propertyId, int zone) {
                CarClientManager.this.dispatchErrorEvent("xp_vcu", propertyId, zone);
            }
        };
        this.mCarVcuSpecialEventCallback = new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xuiservice.carcontrol.CarClientManager.5
            public void onChangeEvent(CarPropertyValue value) {
                CarClientManager.this.dispatchEvent("VCU_SPECIAL", value);
            }

            public void onErrorEvent(int propertyId, int zone) {
                CarClientManager.this.dispatchErrorEvent("xp_vcu", propertyId, zone);
            }
        };
        this.mCarBcmEventCallback = new CarBcmManager.CarBcmEventCallback() { // from class: com.xiaopeng.xuiservice.carcontrol.CarClientManager.6
            public void onChangeEvent(CarPropertyValue value) {
                CarClientManager.this.dispatchEvent("xp_bcm", value);
            }

            public void onErrorEvent(int propertyId, int zone) {
                CarClientManager.this.dispatchErrorEvent("xp_bcm", propertyId, zone);
            }
        };
        this.mCarMcuEventCallback = new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.carcontrol.CarClientManager.7
            public void onChangeEvent(CarPropertyValue value) {
                CarClientManager.this.dispatchEvent("xp_mcu", value);
            }

            public void onErrorEvent(int propertyId, int zone) {
                CarClientManager.this.dispatchErrorEvent("xp_mcu", propertyId, zone);
            }
        };
        this.mCarScuEventCallback = new CarScuManager.CarScuEventCallback() { // from class: com.xiaopeng.xuiservice.carcontrol.CarClientManager.8
            public void onChangeEvent(CarPropertyValue value) {
                CarClientManager.this.dispatchEvent("xp_scu", value);
            }

            public void onErrorEvent(int propertyId, int zone) {
                CarClientManager.this.dispatchErrorEvent("xp_scu", propertyId, zone);
            }
        };
        this.mCarVpmEventCallback = new CarVpmManager.CarVpmEventCallback() { // from class: com.xiaopeng.xuiservice.carcontrol.CarClientManager.9
            public void onChangeEvent(CarPropertyValue value) {
                CarClientManager.this.dispatchEvent("xp_vpm", value);
            }

            public void onErrorEvent(int propertyId, int errorCode) {
                CarClientManager.this.dispatchErrorEvent("xp_vpm", propertyId, errorCode);
            }
        };
        this.mCarTboxEventCallback = new CarTboxManager.CarTboxEventCallback() { // from class: com.xiaopeng.xuiservice.carcontrol.CarClientManager.10
            public void onChangeEvent(CarPropertyValue value) {
                CarClientManager.this.dispatchEvent("xp_tbox", value);
            }

            public void onErrorEvent(int propertyId, int zone) {
                CarClientManager.this.dispatchErrorEvent("xp_tbox", propertyId, zone);
            }
        };
        this.mCarMsmEventCallback = new CarMsmManager.CarMsmEventCallback() { // from class: com.xiaopeng.xuiservice.carcontrol.CarClientManager.11
            public void onChangeEvent(CarPropertyValue value) {
                CarClientManager.this.dispatchEvent("xp_msm", value);
            }

            public void onErrorEvent(int propertyId, int zone) {
                CarClientManager.this.dispatchErrorEvent("xp_msm", propertyId, zone);
            }
        };
        this.mCarSrsEventCallback = new CarSrsManager.CarSrsEventCallback() { // from class: com.xiaopeng.xuiservice.carcontrol.CarClientManager.12
            public void onChangeEvent(CarPropertyValue value) {
                CarClientManager.this.dispatchEvent("xp_srs", value);
            }

            public void onErrorEvent(int propertyId, int zone) {
                CarClientManager.this.dispatchErrorEvent("xp_srs", propertyId, zone);
            }
        };
        this.mCarLluEventCallback = new CarLluManager.CarLluEventCallback() { // from class: com.xiaopeng.xuiservice.carcontrol.CarClientManager.13
            public void onChangeEvent(CarPropertyValue value) {
                CarClientManager.this.dispatchEvent("xp_llu", value);
            }

            public void onErrorEvent(int propertyId, int zone) {
                CarClientManager.this.dispatchErrorEvent("xp_llu", propertyId, zone);
            }
        };
        this.mCarIcmEventCallback = new CarIcmManager.CarIcmEventCallback() { // from class: com.xiaopeng.xuiservice.carcontrol.CarClientManager.14
            public void onChangeEvent(CarPropertyValue value) {
                CarClientManager.this.dispatchEvent("xp_icm", value);
            }

            public void onErrorEvent(int propertyId, int zone) {
                CarClientManager.this.dispatchErrorEvent("xp_icm", propertyId, zone);
            }
        };
        this.mCarEspEventCallback = new CarEspManager.CarEspEventCallback() { // from class: com.xiaopeng.xuiservice.carcontrol.CarClientManager.15
            public void onChangeEvent(CarPropertyValue value) {
                CarClientManager.this.dispatchEvent("xp_esp", value);
            }

            public void onErrorEvent(int propertyId, int zone) {
                CarClientManager.this.dispatchErrorEvent("xp_esp", propertyId, zone);
            }
        };
        this.mCarInputEventCallback = new CarInputManager.CarInputEventCallback() { // from class: com.xiaopeng.xuiservice.carcontrol.CarClientManager.16
            public void onChangeEvent(CarPropertyValue value) {
                CarClientManager.this.dispatchEvent("xp_input", value);
            }

            public void onErrorEvent(int propertyId, int zone) {
                CarClientManager.this.dispatchErrorEvent("xp_input", propertyId, zone);
            }
        };
        this.mCarHvacEventCallback = new CarHvacManager.CarHvacEventCallback() { // from class: com.xiaopeng.xuiservice.carcontrol.CarClientManager.17
            public void onChangeEvent(CarPropertyValue value) {
                CarClientManager.this.dispatchEvent("hvac", value);
            }

            public void onErrorEvent(int propertyId, int zone) {
                CarClientManager.this.dispatchErrorEvent("hvac", propertyId, zone);
            }
        };
        this.mCarAtlEventCallback = new CarAtlManager.CarAtlEventCallback() { // from class: com.xiaopeng.xuiservice.carcontrol.CarClientManager.18
            public void onChangeEvent(CarPropertyValue value) {
                CarClientManager.this.dispatchEvent("xp_atl", value);
            }

            public void onErrorEvent(int propertyId, int zone) {
                CarClientManager.this.dispatchErrorEvent("xp_atl", propertyId, zone);
            }
        };
        this.mCarAmpEventCallback = new CarAmpManager.CarAmpEventCallback() { // from class: com.xiaopeng.xuiservice.carcontrol.CarClientManager.19
            public void onChangeEvent(CarPropertyValue value) {
                CarClientManager.this.dispatchEvent("xp_amp", value);
            }

            public void onErrorEvent(int propertyId, int zone) {
                CarClientManager.this.dispatchErrorEvent("xp_amp", propertyId, zone);
            }
        };
        this.mCarEpsEventCallback = new CarEpsManager.CarEpsEventCallback() { // from class: com.xiaopeng.xuiservice.carcontrol.CarClientManager.20
            public void onChangeEvent(CarPropertyValue value) {
                CarClientManager.this.dispatchEvent("xp_eps", value);
            }

            public void onErrorEvent(int propertyId, int zone) {
                CarClientManager.this.dispatchErrorEvent("xp_eps", propertyId, zone);
            }
        };
        this.mCarPowerStateListener = new CarPowerManager.CarPowerStateListener() { // from class: com.xiaopeng.xuiservice.carcontrol.CarClientManager.21
            public void onStateChanged(int state) {
                CarClientManager.this.dispatchPowerEvent(state);
            }
        };
        this.mCarTpmsEventCallback = new CarTpmsManager.CarTpmsEventCallback() { // from class: com.xiaopeng.xuiservice.carcontrol.CarClientManager.22
            public void onChangeEvent(CarPropertyValue carPropertyValue) {
                CarClientManager.this.dispatchEvent("xp_tpms", carPropertyValue);
            }

            public void onErrorEvent(int propertyId, int zone) {
                CarClientManager.this.dispatchErrorEvent("xp_tpms", propertyId, zone);
            }
        };
        this.mXpDiagnosticEventCallback = new XpDiagnosticManager.XpDiagnosticEventCallback() { // from class: com.xiaopeng.xuiservice.carcontrol.CarClientManager.23
            public void onChangeEvent(CarPropertyValue value) {
                CarClientManager.this.dispatchEvent("xp_diagnostic", value);
            }

            public void onErrorEvent(int propertyId, int zone) {
                CarClientManager.this.dispatchErrorEvent("xp_diagnostic", propertyId, zone);
            }
        };
        CarCallBackInited = true;
    }

    protected void reRegisterCarCallBack() {
        for (int i = 0; i < this.mXUICarEcuList.size(); i++) {
            if (checkEcuListenerExist(this.mXUICarEcuList.get(i))) {
                String str = this.TAG;
                LogUtil.d(str, "reRegisterCarCallBack " + this.mXUICarEcuList.get(i));
                registerCarListener(this.mXUICarEcuList.get(i));
            }
        }
    }

    private void resetCarApi() {
        LogUtil.d(this.TAG, "resetCarApi");
        this.mCarXpuManager = null;
        this.mCarCiuManager = null;
        this.mCarVcuManager = null;
        this.mCarBcmManager = null;
        this.mCarMcuManager = null;
        this.mCarScuManager = null;
        this.mCarVpmManager = null;
        this.mCarTboxManager = null;
        this.mCarMsmManager = null;
        this.mCarSrsManager = null;
        this.mCarAvasManager = null;
        this.mCarLluManager = null;
        this.mCarIcmManager = null;
        this.mCarEspManager = null;
        this.mCarInputManager = null;
        this.mCarHvacManager = null;
        this.mCarAtlManager = null;
        this.mCarEpsManager = null;
        this.mCarPowerManager = null;
        this.mCarConditionManager = null;
        this.mCarTpmsManager = null;
        this.mCarAmpManager = null;
        this.mXpDiagnosticManager = null;
        synchronized (this.mCarClientMap) {
            this.mCarClientMap.clear();
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarControl
    protected void initCarManager() throws CarNotConnectedException {
        LogUtil.d(this.TAG, "initCarManager");
        initCarCallback();
        reRegisterCarCallBack();
        doRegisterAfterCarInited();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private void registerCarListener(String serviceid) {
        char c;
        LogUtil.d(this.TAG, "registerCarListener " + serviceid);
        try {
            switch (serviceid.hashCode()) {
                case -1870955074:
                    if (serviceid.equals("xp_tbox")) {
                        c = '\b';
                        break;
                    }
                    c = 65535;
                    break;
                case -1870941687:
                    if (serviceid.equals("xp_tpms")) {
                        c = 20;
                        break;
                    }
                    c = 65535;
                    break;
                case -753107971:
                    if (serviceid.equals("xp_amp")) {
                        c = 14;
                        break;
                    }
                    c = 65535;
                    break;
                case -753107758:
                    if (serviceid.equals("xp_atl")) {
                        c = '\r';
                        break;
                    }
                    c = 65535;
                    break;
                case -753107323:
                    if (serviceid.equals("xp_bcm")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case -753106168:
                    if (serviceid.equals("xp_ciu")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case -753104031:
                    if (serviceid.equals("xp_eps")) {
                        c = 15;
                        break;
                    }
                    c = 65535;
                    break;
                case -753103941:
                    if (serviceid.equals("xp_esp")) {
                        c = 11;
                        break;
                    }
                    c = 65535;
                    break;
                case -753100596:
                    if (serviceid.equals("xp_icm")) {
                        c = '\n';
                        break;
                    }
                    c = 65535;
                    break;
                case -753097426:
                    if (serviceid.equals("xp_llu")) {
                        c = 16;
                        break;
                    }
                    c = 65535;
                    break;
                case -753096744:
                    if (serviceid.equals("xp_mcu")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case -753096256:
                    if (serviceid.equals("xp_msm")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case -753090978:
                    if (serviceid.equals("xp_scu")) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                case -753090515:
                    if (serviceid.equals("xp_srs")) {
                        c = '\t';
                        break;
                    }
                    c = 65535;
                    break;
                case -753088095:
                    if (serviceid.equals("xp_vcu")) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case -753087700:
                    if (serviceid.equals("xp_vpm")) {
                        c = 17;
                        break;
                    }
                    c = 65535;
                    break;
                case -753085770:
                    if (serviceid.equals("xp_xpu")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 3214768:
                    if (serviceid.equals("hvac")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 106858757:
                    if (serviceid.equals("power")) {
                        c = 18;
                        break;
                    }
                    c = 65535;
                    break;
                case 264148814:
                    if (serviceid.equals("xp_diagnostic")) {
                        c = 21;
                        break;
                    }
                    c = 65535;
                    break;
                case 1852438178:
                    if (serviceid.equals("VCU_SPECIAL")) {
                        c = 19;
                        break;
                    }
                    c = 65535;
                    break;
                case 2120134595:
                    if (serviceid.equals("xp_input")) {
                        c = '\f';
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                    if (this.mCarMcuManager == null) {
                        this.mCarMcuManager = (CarMcuManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarMcuManager.registerPropCallbackWithFlag(CarPropertyId.getRegisterPropertyIdsForMcu(), this.mCarMcuEventCallback, 1);
                    break;
                case 1:
                    if (this.mCarHvacManager == null) {
                        this.mCarHvacManager = (CarHvacManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarHvacManager.registerPropCallbackWithFlag(CarPropertyId.getRegisterPropertyIdsForHvac(), this.mCarHvacEventCallback, 1);
                    break;
                case 2:
                    if (this.mCarMsmManager == null) {
                        this.mCarMsmManager = (CarMsmManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarMsmManager.registerPropCallbackWithFlag(CarPropertyId.getRegisterPropertyIdsForMsm(), this.mCarMsmEventCallback, 1);
                    break;
                case 3:
                    if (this.mCarXpuManager == null) {
                        this.mCarXpuManager = (CarXpuManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarXpuManager.registerPropCallbackWithFlag(CarPropertyId.getRegisterPropertyIdsForXpu(), this.mCarXpuEventCallback, 1);
                    break;
                case 4:
                    if (this.mCarCiuManager == null) {
                        this.mCarCiuManager = (CarCiuManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarCiuManager.registerPropCallbackWithFlag(CarPropertyId.getRegisterPropertyIdsForCiu(), this.mCarCiuEventCallback, 1);
                    break;
                case 5:
                    if (this.mCarBcmManager == null) {
                        this.mCarBcmManager = (CarBcmManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarBcmManager.registerPropCallbackWithFlag(CarPropertyId.getRegisterPropertyIdsForBcm(), this.mCarBcmEventCallback, 1);
                    break;
                case 6:
                    if (this.mCarVcuManager == null) {
                        this.mCarVcuManager = (CarVcuManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarVcuManager.registerPropCallbackWithFlag(CarPropertyId.getRegisterPropertyIdsForVcu(), this.mCarVcuEventCallback, 1);
                    if (this.mCarConditionManager == null) {
                        this.mCarConditionManager = (CarConditionManager) this.mCarApiClient.getCarManager("xp_condition");
                    }
                    CarConditionInfo.Builder builder = new CarConditionInfo.Builder();
                    builder.setCarSpeedLimit(new Float[]{Float.valueOf(0.0f), Float.valueOf(3.0f), Float.valueOf(5.0f), Float.valueOf(10.0f), Float.valueOf(15.0f), Float.valueOf(20.0f), Float.valueOf(25.0f), Float.valueOf(30.0f), Float.valueOf(35.0f), Float.valueOf(40.0f), Float.valueOf(45.0f), Float.valueOf(50.0f), Float.valueOf(55.0f), Float.valueOf(60.0f), Float.valueOf(65.0f), Float.valueOf(70.0f), Float.valueOf(76.2f), Float.valueOf(80.96f), Float.valueOf(85.714f), Float.valueOf(115.238f)});
                    this.mCarConditionManager.registerCondition(Collections.singletonList(559944229), builder.build(), this.carConditionEventListener);
                    break;
                case 7:
                    if (this.mCarScuManager == null) {
                        this.mCarScuManager = (CarScuManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarScuManager.registerPropCallbackWithFlag(CarPropertyId.getRegisterPropertyIdsForScu(), this.mCarScuEventCallback, 1);
                    break;
                case '\b':
                    if (this.mCarTboxManager == null) {
                        this.mCarTboxManager = (CarTboxManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarTboxManager.registerPropCallbackWithFlag(CarPropertyId.getRegisterPropertyIdsForTbox(), this.mCarTboxEventCallback, 1);
                    break;
                case '\t':
                    if (this.mCarSrsManager == null) {
                        this.mCarSrsManager = (CarSrsManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarSrsManager.registerPropCallbackWithFlag(CarPropertyId.getRegisterPropertyIdsForSrs(), this.mCarSrsEventCallback, 1);
                    break;
                case '\n':
                    if (this.mCarIcmManager == null) {
                        this.mCarIcmManager = (CarIcmManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarIcmManager.registerPropCallbackWithFlag(CarPropertyId.getRegisterPropertyIdsForIcm(), this.mCarIcmEventCallback, 1);
                    break;
                case 11:
                    if (this.mCarEspManager == null) {
                        this.mCarEspManager = (CarEspManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarEspManager.registerPropCallbackWithFlag(CarPropertyId.getRegisterPropertyIdsForEsp(), this.mCarEspEventCallback, 1);
                    break;
                case '\f':
                    if (this.mCarInputManager == null) {
                        this.mCarInputManager = (CarInputManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarInputManager.registerPropCallbackWithFlag(CarPropertyId.getRegisterPropertyIdsForInput(), this.mCarInputEventCallback, 1);
                    break;
                case '\r':
                    if (this.mCarAtlManager == null) {
                        this.mCarAtlManager = (CarAtlManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarAtlManager.registerPropCallbackWithFlag(CarPropertyId.getRegisterPropertyIdsForAtl(), this.mCarAtlEventCallback, 1);
                    break;
                case 14:
                    if (this.mCarAmpManager == null) {
                        this.mCarAmpManager = (CarAmpManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarAmpManager.registerPropCallbackWithFlag(CarPropertyId.getRegisterPropertyIdsForAmp(), this.mCarAmpEventCallback, 1);
                    break;
                case 15:
                    if (this.mCarEpsManager == null) {
                        this.mCarEpsManager = (CarEpsManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarEpsManager.registerPropCallbackWithFlag(CarPropertyId.getRegisterPropertyIdsForEps(), this.mCarEpsEventCallback, 1);
                    break;
                case 16:
                    if (this.mCarLluManager == null) {
                        this.mCarLluManager = (CarLluManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarLluManager.registerPropCallbackWithFlag(CarPropertyId.getRegisterPropertyIdsForLlu(), this.mCarLluEventCallback, 1);
                    break;
                case 17:
                    if (this.mCarVpmManager == null) {
                        this.mCarVpmManager = (CarVpmManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarVpmManager.registerPropCallbackWithFlag(CarPropertyId.getRegisterPropertyIdsForVpm(), this.mCarVpmEventCallback, 1);
                    break;
                case 18:
                    if (this.mCarPowerManager == null) {
                        this.mCarPowerManager = (CarPowerManager) this.mCarApiClient.getCarManager("power");
                    }
                    this.mCarPowerManager.clearListener();
                    this.mCarPowerManager.setListener(this.mCarPowerStateListener, (Executor) null);
                    break;
                case 19:
                    if (this.mCarVcuManager == null) {
                        this.mCarVcuManager = (CarVcuManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarVcuManager.registerPropCallbackWithFlag(CarPropertyId.getRegisterPropertyIdsForVcuSpecialIds(), this.mCarVcuSpecialEventCallback, 1);
                    break;
                case 20:
                    if (this.mCarTpmsManager == null) {
                        this.mCarTpmsManager = (CarTpmsManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    ArraySet<Integer> propSet = CarPropertyId.getRegisterPropertyIdsForTpms();
                    if (propSet != null) {
                        this.mCarTpmsManager.registerPropCallbackWithFlag(propSet, this.mCarTpmsEventCallback, 1);
                        break;
                    } else {
                        LogUtil.w(this.TAG, "registerPropCallbackWithFlag, null prop set");
                        break;
                    }
                case 21:
                    if (this.mXpDiagnosticManager == null) {
                        this.mXpDiagnosticManager = (XpDiagnosticManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mXpDiagnosticManager.registerPropCallbackWithFlag(CarPropertyId.getRegisterPropertyIdsForXpDiagnostic(), this.mXpDiagnosticEventCallback, 1);
                    break;
                default:
                    LogUtil.w(this.TAG, "registerCarListener ID:" + serviceid + " not found");
                    break;
            }
            setCarRegisterStatus(serviceid, 2);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void unregisterCarListener(String serviceid) {
        char c = 65535;
        try {
            switch (serviceid.hashCode()) {
                case -1870955074:
                    if (serviceid.equals("xp_tbox")) {
                        c = '\b';
                        break;
                    }
                    break;
                case -1870941687:
                    if (serviceid.equals("xp_tpms")) {
                        c = 19;
                        break;
                    }
                    break;
                case -753107971:
                    if (serviceid.equals("xp_amp")) {
                        c = 14;
                        break;
                    }
                    break;
                case -753107758:
                    if (serviceid.equals("xp_atl")) {
                        c = '\r';
                        break;
                    }
                    break;
                case -753107323:
                    if (serviceid.equals("xp_bcm")) {
                        c = 5;
                        break;
                    }
                    break;
                case -753106168:
                    if (serviceid.equals("xp_ciu")) {
                        c = 4;
                        break;
                    }
                    break;
                case -753104031:
                    if (serviceid.equals("xp_eps")) {
                        c = 15;
                        break;
                    }
                    break;
                case -753103941:
                    if (serviceid.equals("xp_esp")) {
                        c = 11;
                        break;
                    }
                    break;
                case -753100596:
                    if (serviceid.equals("xp_icm")) {
                        c = '\n';
                        break;
                    }
                    break;
                case -753097426:
                    if (serviceid.equals("xp_llu")) {
                        c = 16;
                        break;
                    }
                    break;
                case -753096744:
                    if (serviceid.equals("xp_mcu")) {
                        c = 0;
                        break;
                    }
                    break;
                case -753096256:
                    if (serviceid.equals("xp_msm")) {
                        c = 2;
                        break;
                    }
                    break;
                case -753090978:
                    if (serviceid.equals("xp_scu")) {
                        c = 7;
                        break;
                    }
                    break;
                case -753090515:
                    if (serviceid.equals("xp_srs")) {
                        c = '\t';
                        break;
                    }
                    break;
                case -753088095:
                    if (serviceid.equals("xp_vcu")) {
                        c = 6;
                        break;
                    }
                    break;
                case -753087700:
                    if (serviceid.equals("xp_vpm")) {
                        c = 17;
                        break;
                    }
                    break;
                case -753085770:
                    if (serviceid.equals("xp_xpu")) {
                        c = 3;
                        break;
                    }
                    break;
                case 3214768:
                    if (serviceid.equals("hvac")) {
                        c = 1;
                        break;
                    }
                    break;
                case 264148814:
                    if (serviceid.equals("xp_diagnostic")) {
                        c = 20;
                        break;
                    }
                    break;
                case 1852438178:
                    if (serviceid.equals("VCU_SPECIAL")) {
                        c = 18;
                        break;
                    }
                    break;
                case 2120134595:
                    if (serviceid.equals("xp_input")) {
                        c = '\f';
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    if (this.mCarMcuManager == null) {
                        this.mCarMcuManager = (CarMcuManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarMcuManager.unregisterCallback(this.mCarMcuEventCallback);
                    break;
                case 1:
                    if (this.mCarHvacManager == null) {
                        this.mCarHvacManager = (CarHvacManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarHvacManager.unregisterCallback(this.mCarHvacEventCallback);
                    break;
                case 2:
                    if (this.mCarMsmManager == null) {
                        this.mCarMsmManager = (CarMsmManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarMsmManager.unregisterCallback(this.mCarMsmEventCallback);
                    break;
                case 3:
                    if (this.mCarXpuManager == null) {
                        this.mCarXpuManager = (CarXpuManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarXpuManager.unregisterCallback(this.mCarXpuEventCallback);
                    break;
                case 4:
                    if (this.mCarCiuManager == null) {
                        this.mCarCiuManager = (CarCiuManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarCiuManager.unregisterCallback(this.mCarCiuEventCallback);
                    break;
                case 5:
                    if (this.mCarBcmManager == null) {
                        this.mCarBcmManager = (CarBcmManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarBcmManager.unregisterCallback(this.mCarBcmEventCallback);
                    break;
                case 6:
                    if (this.mCarVcuManager == null) {
                        this.mCarVcuManager = (CarVcuManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarVcuManager.unregisterCallback(this.mCarVcuEventCallback);
                    if (this.mCarConditionManager != null) {
                        this.mCarConditionManager.unregisterCondition(this.carConditionEventListener);
                        break;
                    }
                    break;
                case 7:
                    if (this.mCarScuManager == null) {
                        this.mCarScuManager = (CarScuManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarScuManager.unregisterCallback(this.mCarScuEventCallback);
                    break;
                case '\b':
                    if (this.mCarTboxManager == null) {
                        this.mCarTboxManager = (CarTboxManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarTboxManager.unregisterCallback(this.mCarTboxEventCallback);
                    break;
                case '\t':
                    if (this.mCarSrsManager == null) {
                        this.mCarSrsManager = (CarSrsManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarSrsManager.unregisterCallback(this.mCarSrsEventCallback);
                    break;
                case '\n':
                    if (this.mCarIcmManager == null) {
                        this.mCarIcmManager = (CarIcmManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarIcmManager.unregisterCallback(this.mCarIcmEventCallback);
                    break;
                case 11:
                    if (this.mCarEspManager == null) {
                        this.mCarEspManager = (CarEspManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarEspManager.unregisterCallback(this.mCarEspEventCallback);
                    break;
                case '\f':
                    if (this.mCarInputManager == null) {
                        this.mCarInputManager = (CarInputManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarInputManager.unregisterCallback(this.mCarInputEventCallback);
                    break;
                case '\r':
                    if (this.mCarAtlManager == null) {
                        this.mCarAtlManager = (CarAtlManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarAtlManager.unregisterCallback(this.mCarAtlEventCallback);
                    break;
                case 14:
                    if (this.mCarAmpManager == null) {
                        this.mCarAmpManager = (CarAmpManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarAmpManager.unregisterCallback(this.mCarAmpEventCallback);
                    break;
                case 15:
                    if (this.mCarEpsManager == null) {
                        this.mCarEpsManager = (CarEpsManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarEpsManager.unregisterCallback(this.mCarEpsEventCallback);
                    break;
                case 16:
                    if (this.mCarLluManager == null) {
                        this.mCarLluManager = (CarLluManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarLluManager.unregisterCallback(this.mCarLluEventCallback);
                    break;
                case 17:
                    if (this.mCarVpmManager == null) {
                        this.mCarVpmManager = (CarVpmManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mCarVpmManager.unregisterCallback(this.mCarVpmEventCallback);
                    break;
                case 18:
                    if (this.mCarVcuManager == null) {
                        this.mCarVcuManager = (CarVcuManager) this.mCarApiClient.getCarManager("xp_scu");
                    }
                    this.mCarVcuManager.unregisterCallback(this.mCarVcuSpecialEventCallback);
                    break;
                case 19:
                    if (this.mCarTpmsManager == null) {
                        this.mCarTpmsManager = (CarTpmsManager) this.mCarApiClient.getCarManager("xp_tpms");
                    }
                    if (CarPropertyId.getRegisterPropertyIdsForTpms() != null) {
                        this.mCarTpmsManager.unregisterCallback(this.mCarTpmsEventCallback);
                        break;
                    } else {
                        LogUtil.w(this.TAG, "unregisterCallback, prop set null");
                        break;
                    }
                case 20:
                    if (this.mXpDiagnosticManager == null) {
                        this.mXpDiagnosticManager = (XpDiagnosticManager) this.mCarApiClient.getCarManager(serviceid);
                    }
                    this.mXpDiagnosticManager.unregisterCallback(this.mXpDiagnosticEventCallback);
                    break;
                default:
                    LogUtil.w(this.TAG, "unRegisterCarListener ID:" + serviceid + " not found");
                    break;
            }
            setCarRegisterStatus(serviceid, 2);
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarControl
    public void onInit() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarControl
    public void onRelease() {
        try {
            unregisterCarListener("xp_mcu");
            unregisterCarListener("hvac");
            unregisterCarListener("xp_xpu");
            unregisterCarListener("xp_ciu");
            unregisterCarListener("xp_bcm");
            unregisterCarListener("xp_vcu");
            unregisterCarListener("xp_scu");
            unregisterCarListener("xp_tbox");
            unregisterCarListener("xp_srs");
            unregisterCarListener("xp_icm");
            unregisterCarListener("xp_esp");
            unregisterCarListener("xp_input");
            unregisterCarListener("xp_eps");
            unregisterCarListener("xp_vpm");
            unregisterCarListener("xp_llu");
            unregisterCarListener("xp_vpm");
            unregisterCarListener("xp_tpms");
            unregisterCarListener("xp_diagnostic");
            synchronized (this.mListener) {
                this.mListener.clear();
            }
            this.mConnListener.clear();
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void addListener(String serviceid, CarEcuManager.CarEcuEventCallback callback) {
        ArrayList<CarEcuManager.CarEcuEventCallback> callbacks;
        if (callback == null) {
            LogUtil.w(this.TAG, "addListener error: callback is null");
            return;
        }
        synchronized (this.mListener) {
            callbacks = this.mListener.get(serviceid);
        }
        if (callbacks == null) {
            callbacks = new ArrayList<>();
        }
        if (!callbacks.contains(callback)) {
            if (!checkEcuListenerExist(serviceid)) {
                if (CarCallBackInited) {
                    registerCarListener(serviceid);
                } else {
                    setCarRegisterStatus(serviceid, 1);
                }
            }
            String str = this.TAG;
            LogUtil.v(str, "addListener " + serviceid + " " + callback);
            synchronized (callbacks) {
                callbacks.add(callback);
            }
            synchronized (this.mListener) {
                this.mListener.put(serviceid, callbacks);
            }
        }
    }

    private void doRegisterAfterCarInited() {
        for (int i = 0; i < this.mEcuCallbackRegister.size(); i++) {
            String serviceId = this.mEcuCallbackRegister.keyAt(i);
            int status = this.mEcuCallbackRegister.valueAt(i).intValue();
            if (status == 1) {
                registerCarListener(serviceId);
            }
        }
    }

    private void setCarRegisterStatus(String serviceID, int status) {
        synchronized (this) {
            this.mEcuCallbackRegister.put(serviceID, Integer.valueOf(status));
        }
    }

    private boolean checkEcuListenerExist(String serviceID) {
        ArrayList<CarEcuManager.CarEcuEventCallback> callbacks;
        synchronized (this.mListener) {
            callbacks = this.mListener.get(serviceID);
        }
        if (callbacks == null || callbacks.size() == 0) {
            return false;
        }
        return true;
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addXpuManagerListener(CarXpuManager.CarXpuEventCallback callback) {
        addListener("xp_xpu", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addCiuManagerListener(CarCiuManager.CarCiuEventCallback callback) {
        addListener("xp_ciu", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addBcmManagerListener(CarBcmManager.CarBcmEventCallback callback) {
        addListener("xp_bcm", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addVcuManagerListener(CarVcuManager.CarVcuEventCallback callback) {
        addListener("xp_vcu", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addMcuManagerListener(CarMcuManager.CarMcuEventCallback callback) {
        addListener("xp_mcu", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addScuManagerListener(CarScuManager.CarScuEventCallback callback) {
        addListener("xp_scu", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addVpmManagerListener(CarVpmManager.CarVpmEventCallback callback) {
        addListener("xp_vpm", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addTboxManagerListener(CarTboxManager.CarTboxEventCallback callback) {
        addListener("xp_tbox", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addMsmManagerListener(CarMsmManager.CarMsmEventCallback callback) {
        addListener("xp_msm", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addSrsManagerListener(CarSrsManager.CarSrsEventCallback callback) {
        addListener("xp_srs", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addAvasManagerListener(CarAvasManager.CarAvasEventCallback callback) {
        addListener("xp_avas", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addLluManagerListener(CarLluManager.CarLluEventCallback callback) {
        addListener("xp_llu", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addIcmManagerListener(CarIcmManager.CarIcmEventCallback callback) {
        addListener("xp_icm", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addEspManagerListener(CarEspManager.CarEspEventCallback callback) {
        addListener("xp_esp", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addInputManagerListener(CarInputManager.CarInputEventCallback callback) {
        addListener("xp_input", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addHvacManagerListener(CarHvacManager.CarHvacEventCallback callback) {
        addListener("hvac", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addAtlManagerListener(CarAtlManager.CarAtlEventCallback callback) {
        addListener("xp_atl", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addAmpManagerListener(CarAmpManager.CarAmpEventCallback callback) {
        addListener("xp_amp", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addEpsManagerListener(CarEpsManager.CarEpsEventCallback callback) {
        addListener("xp_eps", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addVcuSpecialManagerListener(CarVcuManager.CarVcuEventCallback callback) {
        addListener("VCU_SPECIAL", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addXpDiagnosticManagerListener(XpDiagnosticManager.XpDiagnosticEventCallback callback) {
        addListener("xp_diagnostic", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addPowerStateListener(CarPowerManager.CarPowerStateListener listener) {
        if (listener != null) {
            this.mPowerListener.add(listener);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addTpmsManagerListener(CarTpmsManager.CarTpmsEventCallback callback) {
        addListener("xp_tpms", callback);
    }

    private void removeListerner(String serviceid, CarEcuManager.CarEcuEventCallback callback) {
        ArrayList<CarEcuManager.CarEcuEventCallback> list;
        if (callback == null) {
            LogUtil.w(this.TAG, "removeListerner error: callback is null");
            return;
        }
        synchronized (this.mListener) {
            list = this.mListener.get(serviceid);
        }
        if (list != null) {
            synchronized (list) {
                if (list.contains(callback)) {
                    list.remove(callback);
                }
            }
        }
        if (!checkEcuListenerExist(serviceid)) {
            unregisterCarListener(serviceid);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeXpuManagerListener(CarXpuManager.CarXpuEventCallback callback) {
        removeListerner("xp_xpu", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeCiuManagerListener(CarCiuManager.CarCiuEventCallback callback) {
        removeListerner("xp_ciu", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeBcmManagerListener(CarBcmManager.CarBcmEventCallback callback) {
        removeListerner("xp_bcm", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeVcuManagerListener(CarVcuManager.CarVcuEventCallback callback) {
        removeListerner("xp_vcu", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeMcuManagerListener(CarMcuManager.CarMcuEventCallback callback) {
        removeListerner("xp_mcu", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeScuManagerListener(CarScuManager.CarScuEventCallback callback) {
        removeListerner("xp_scu", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeVpmManagerListener(CarVpmManager.CarVpmEventCallback callback) {
        removeListerner("xp_vpm", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeTboxManagerListener(CarTboxManager.CarTboxEventCallback callback) {
        removeListerner("xp_tbox", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeMsmManagerListener(CarMsmManager.CarMsmEventCallback callback) {
        removeListerner("xp_msm", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeSrsManagerListener(CarSrsManager.CarSrsEventCallback callback) {
        removeListerner("xp_srs", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeAvasManagerListener(CarAvasManager.CarAvasEventCallback callback) {
        removeListerner("xp_avas", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeLluManagerListener(CarLluManager.CarLluEventCallback callback) {
        removeListerner("xp_llu", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeIcmManagerListener(CarIcmManager.CarIcmEventCallback callback) {
        removeListerner("xp_icm", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeEspManagerListener(CarEspManager.CarEspEventCallback callback) {
        removeListerner("xp_esp", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeInputManagerListener(CarInputManager.CarInputEventCallback callback) {
        removeListerner("xp_input", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeHvacManagerListener(CarHvacManager.CarHvacEventCallback callback) {
        removeListerner("hvac", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeAtlManagerListener(CarAtlManager.CarAtlEventCallback callback) {
        removeListerner("xp_atl", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeAmpManagerListener(CarAmpManager.CarAmpEventCallback callback) {
        removeListerner("xp_amp", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeEpsManagerListener(CarEpsManager.CarEpsEventCallback callback) {
        removeListerner("xp_eps", callback);
        if (!checkEcuListenerExist("xp_eps")) {
            try {
                if (this.mCarEpsManager != null && this.mCarEpsEventCallback != null) {
                    this.mCarEpsManager.unregisterCallback(this.mCarEpsEventCallback);
                }
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removePowerStateListener(CarPowerManager.CarPowerStateListener listener) {
        if (listener != null && this.mPowerListener.contains(listener)) {
            this.mPowerListener.remove(listener);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeVcuSpecialManagerListener(CarVcuManager.CarVcuEventCallback callback) {
        removeListerner("VCU_SPECIAL", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeXpDiagnosticManagerListener(XpDiagnosticManager.XpDiagnosticEventCallback callback) {
        removeListerner("xp_diagnostic", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeTpmsManagerListener(CarTpmsManager.CarTpmsEventCallback callback) {
        removeListerner("xp_tpms", callback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public CarManagerBase getCarManager(String serviceName) {
        CarManagerBase manager = this.mCarClientMap.get(serviceName);
        if (manager != null) {
            return manager;
        }
        try {
            if (this.mCarApiClient != null) {
                CarManagerBase manager2 = (CarManagerBase) this.mCarApiClient.getCarManager(serviceName);
                synchronized (this.mCarClientMap) {
                    this.mCarClientMap.put(serviceName, manager2);
                }
                return manager2;
            }
            return null;
        } catch (Exception e) {
            handleException(e);
            return null;
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void addConnectionListener(IServiceConn con) {
        synchronized (this.mConnListener) {
            if (!this.mConnListener.contains(con)) {
                this.mConnListener.add(con);
            }
        }
        if (this.mIsCarConnect) {
            con.onConnectedCar();
        }
        if (this.mIsXuiConnect) {
            con.onConnectXUI();
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.ICarControl
    public void removeConnectionListener(IServiceConn con) {
        synchronized (this.mConnListener) {
            if (this.mConnListener.contains(con)) {
                this.mConnListener.remove(con);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarControl
    public void onConnectedCar() {
        super.onConnectedCar();
        String str = this.TAG;
        LogUtil.i(str, "CarClientManager onConnectedCar() mConnListener=" + this.mConnListener.size());
        synchronized (this.mConnListener) {
            Iterator<IServiceConn> it = this.mConnListener.iterator();
            while (it.hasNext()) {
                IServiceConn conn = it.next();
                conn.onConnectedCar();
            }
        }
        registerCarListener("power");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarControl
    public void onDisconnectCar() {
        super.onDisconnectCar();
        resetCarApi();
        synchronized (this.mConnListener) {
            Iterator<IServiceConn> it = this.mConnListener.iterator();
            while (it.hasNext()) {
                IServiceConn conn = it.next();
                conn.onDisconnectCar();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarControl
    public void onConnectXUI() {
        super.onConnectXUI();
        synchronized (this.mConnListener) {
            Iterator<IServiceConn> it = this.mConnListener.iterator();
            while (it.hasNext()) {
                IServiceConn conn = it.next();
                conn.onConnectXUI();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarControl
    public void onDisconnectXUI() {
        super.onDisconnectXUI();
        synchronized (this.mConnListener) {
            Iterator<IServiceConn> it = this.mConnListener.iterator();
            while (it.hasNext()) {
                IServiceConn conn = it.next();
                conn.onDisconnectXUI();
            }
        }
    }
}
