package com.xiaopeng.xuiservice.smart;

import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.bcm.CarBcmManager;
import android.car.hardware.esp.CarEspManager;
import android.car.hardware.llu.CarLluManager;
import android.car.hardware.mcu.CarMcuManager;
import android.car.hardware.vcu.CarVcuManager;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.SystemProperties;
import android.provider.Settings;
import com.xiaopeng.xuimanager.XUIServiceNotConnectedException;
import com.xiaopeng.xuimanager.mediacenter.MediaCenterManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.capabilities.XpPictureNotification;
import com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageProxy;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.ToastUtil;
import com.xiaopeng.xuiservice.utils.UiHandler;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes5.dex */
public class SmartLampService extends BaseSmartService implements BroadcastManager.BroadcastListener {
    private static final String ACTION_XKEY = "com.xiaopeng.intent.action.xkey";
    private static final String TAG = "SmartLampService";
    private static int mGearLevelStatus;
    private CarBcmManager.CarBcmEventCallback mBcmCallback;
    private CarEspManager.CarEspEventCallback mEspCallback;
    private CarLluManager.CarLluEventCallback mLluCallback;
    private CarMcuManager.CarMcuEventCallback mMcuCallback;
    private MediaCenterManager mMediaCenterManager;
    private XpLightlanuageProxy mProxy;
    private CarVcuManager.CarVcuEventCallback mVcuCallback;
    private MediaCenterManager.VisualCaptureListener mVisualCaptureListener;
    private static final boolean hasSeriesD = XUIConfig.isProductSeriesD();
    private static boolean hasBootCompleted = false;
    private static boolean hasIgOn = false;
    private static boolean hasRearFogLampOff = false;
    private static boolean hasTurnLampOff = false;
    private static boolean hasGearLevelP = true;
    private static boolean hasAvhActive = false;
    private static boolean hasHeadLightOff = false;
    private static boolean hasTemporaryLock = false;
    private static long mLastRatioTs = 0;

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService, com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
    public /* bridge */ /* synthetic */ void debugDump(PrintWriter printWriter, String[] strArr) {
        super.debugDump(printWriter, strArr);
    }

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService
    public /* bridge */ /* synthetic */ void dump(PrintWriter printWriter, String[] strArr) {
        super.dump(printWriter, strArr);
    }

    private void registerReceiver() {
        List<String> filter = new ArrayList<>();
        filter.add("android.intent.action.BOOT_COMPLETED");
        filter.add("com.xiaopeng.intent.action.xkey");
        BroadcastManager.getInstance().registerListener(this, filter);
        if ("1".equals(SystemProperties.get(XUIConfig.PROPERTY_BOOT_COMPLETE)) && !hasBootCompleted) {
            hasBootCompleted = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkHeadLampModeOff() {
        try {
            return Integer.parseInt(Settings.System.getString(this.mContext.getContentResolver(), "c_head_lamp_mode")) == 0;
        } catch (Exception e) {
            LogUtil.e(TAG, "checkHeadLampModeOff failed, " + e);
            return false;
        }
    }

    private void registerProvider() {
        if (hasSeriesD) {
            Uri uri = Settings.System.getUriFor("c_head_lamp_mode");
            hasHeadLightOff = checkHeadLampModeOff();
            this.mContext.getContentResolver().registerContentObserver(uri, true, new ContentObserver(new UiHandler()) { // from class: com.xiaopeng.xuiservice.smart.SmartLampService.1
                @Override // android.database.ContentObserver
                public void onChange(boolean selfChange, Uri uri2) {
                    super.onChange(selfChange, uri2);
                    boolean unused = SmartLampService.hasHeadLightOff = SmartLampService.this.checkHeadLampModeOff();
                    if (SmartLampService.hasIgOn && !SmartLampService.hasHeadLightOff && SmartLampService.this.mProxy.getRunningEffect() != null) {
                        LogUtil.i(SmartLampService.TAG, "headLight ON, suspendEffect");
                        SmartLampService.this.mProxy.suspendEffect();
                    }
                }
            });
        }
    }

    private void addMcuManagerCallback() {
        this.mMcuCallback = new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartLampService.2
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557847561) {
                    boolean unused = SmartLampService.hasIgOn = ((Integer) value.getValue()).intValue() == 1;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onChangeEvent, hasIgOn=");
                    sb.append(SmartLampService.hasIgOn ? "ON" : "OFF, suspendEffect");
                    LogUtil.i(SmartLampService.TAG, sb.toString());
                    if (SmartLampService.hasIgOn) {
                        SmartLampService.this.initCarStatus();
                    } else {
                        SmartLampService.this.mProxy.suspendEffect();
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        addMcuManagerListener(this.mMcuCallback);
    }

    private void addBcmManagerCallback() {
        this.mBcmCallback = new CarBcmManager.CarBcmEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartLampService.3
            public void onChangeEvent(CarPropertyValue value) {
                if (SmartLampService.hasIgOn) {
                    switch (value.getPropertyId()) {
                        case 557849602:
                            boolean disable = ((Integer) value.getValue()).intValue() == 0;
                            if (disable != SmartLampService.hasRearFogLampOff) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("rearFogLamp ");
                                sb.append(disable ? "OFF" : "ON, stopEffect");
                                LogUtil.i(SmartLampService.TAG, sb.toString());
                                if (!disable) {
                                    SmartLampService.this.mProxy.stopEffect();
                                }
                                boolean unused = SmartLampService.hasRearFogLampOff = disable;
                                return;
                            }
                            return;
                        case 557849640:
                            if (!SmartLampService.hasSeriesD) {
                                boolean unused2 = SmartLampService.hasHeadLightOff = ((Integer) value.getValue()).intValue() == 0;
                                if (!SmartLampService.hasHeadLightOff && SmartLampService.this.mProxy.getRunningEffect() != null) {
                                    LogUtil.i(SmartLampService.TAG, "headLight ON, suspendEffect");
                                    SmartLampService.this.mProxy.suspendEffect();
                                    return;
                                }
                                return;
                            }
                            return;
                        case 557849889:
                            boolean disable2 = ((Integer) value.getValue()).intValue() == 0;
                            if (disable2 != SmartLampService.hasTemporaryLock) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("temporaryLock ");
                                sb2.append(disable2 ? "OFF" : "ON, stopEffect");
                                LogUtil.i(SmartLampService.TAG, sb2.toString());
                                if (!disable2) {
                                    SmartLampService.this.mProxy.stopEffect();
                                }
                                boolean unused3 = SmartLampService.hasTemporaryLock = disable2;
                                return;
                            }
                            return;
                        case 557915328:
                            Integer[] data = (Integer[]) value.getValue();
                            if (data[0].intValue() != 1 && data[1].intValue() != 1) {
                                disable = true;
                            }
                            if (disable != SmartLampService.hasTurnLampOff) {
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("turnLamp ");
                                sb3.append(disable ? "OFF" : "ON, stopEffect");
                                LogUtil.i(SmartLampService.TAG, sb3.toString());
                                if (!disable) {
                                    SmartLampService.this.mProxy.stopEffect();
                                }
                                boolean unused4 = SmartLampService.hasTurnLampOff = disable;
                                return;
                            }
                            return;
                        default:
                            return;
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        addBcmManagerListener(this.mBcmCallback);
    }

    private void addEspManagerCallback() {
        this.mEspCallback = new CarEspManager.CarEspEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartLampService.4
            public void onChangeEvent(CarPropertyValue value) {
                if (SmartLampService.hasIgOn && value.getPropertyId() == 557851651) {
                    boolean enable = ((Integer) value.getValue()).intValue() == 1;
                    LogUtil.d(SmartLampService.TAG, "onAvhStatus event, value=" + ((Integer) value.getValue()).intValue());
                    if (enable != SmartLampService.hasAvhActive) {
                        if (enable && SmartLampService.mGearLevelStatus == 1) {
                            LogUtil.i(SmartLampService.TAG, "onAvhStatus active, play SayhiEffect");
                            SmartLampService.this.mProxy.playSayhiEffect();
                        } else if (!enable && !SmartLampService.hasGearLevelP) {
                            LogUtil.i(SmartLampService.TAG, "onAvhStatus inactive, stop SayhiEffect");
                            SmartLampService.this.mProxy.stopSayhiEffect();
                        }
                        boolean unused = SmartLampService.hasAvhActive = enable;
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        addEspManagerListener(this.mEspCallback);
    }

    private void addVcuManagerCallback() {
        this.mVcuCallback = new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartLampService.5
            public void onChangeEvent(CarPropertyValue value) {
                if (SmartLampService.hasIgOn && value.getPropertyId() == 557847045) {
                    int unused = SmartLampService.mGearLevelStatus = ((Integer) value.getValue()).intValue();
                    boolean enable = SmartLampService.mGearLevelStatus == 4;
                    LogUtil.d(SmartLampService.TAG, "onVcuGearLevel event, value=" + SmartLampService.mGearLevelStatus);
                    if (enable != SmartLampService.hasGearLevelP) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("onVcuGearLevel ");
                        sb.append(enable ? "enter P, play SayhiEffect" : "leave P, stop Effect");
                        LogUtil.i(SmartLampService.TAG, sb.toString());
                        if (enable) {
                            SmartLampService.this.mProxy.playSayhiEffect();
                        } else {
                            SmartLampService.this.mProxy.stopEffect();
                        }
                        boolean unused2 = SmartLampService.hasGearLevelP = enable;
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        addVcuManagerListener(this.mVcuCallback);
    }

    private void addLluManagerCallback() {
        this.mLluCallback = new CarLluManager.CarLluEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartLampService.6
            public void onChangeEvent(CarPropertyValue value) {
                LogUtil.d(SmartLampService.TAG, "onLluChangeEvent, " + value.toString());
                if (SmartLampService.hasIgOn) {
                    int propertyId = value.getPropertyId();
                    if (propertyId == 557847625) {
                        boolean enable = ((Integer) value.getValue()).intValue() == 1;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onLightEffect ");
                        sb.append(enable ? "enable" : "disable");
                        LogUtil.i(SmartLampService.TAG, sb.toString());
                        SmartLampService.this.mProxy.setLightEffectEnable(enable);
                    } else if (propertyId == 557847631) {
                        LogUtil.i(SmartLampService.TAG, "onChargingSwitch");
                        XpPictureNotification.getInstance().generateBigPictureStyleNotification(557847631);
                    } else if (propertyId == 557847645) {
                        boolean active = ((Integer) value.getValue()).intValue() == 1;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("onMcuEffect ");
                        sb2.append(active ? "active, stopEffect" : "inactive");
                        LogUtil.i(SmartLampService.TAG, sb2.toString());
                        if (active) {
                            SmartLampService.this.mProxy.stopEffect();
                        }
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        addLluManagerListener(this.mLluCallback);
    }

    private void removeCarListener() {
        removeMcuManagerListener(this.mMcuCallback);
        removeBcmManagerListener(this.mBcmCallback);
        removeEspManagerListener(this.mEspCallback);
        removeVcuManagerListener(this.mVcuCallback);
        removeLluManagerListener(this.mLluCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void initCarStatus() {
        boolean z;
        try {
            CarVcuManager vcuMgr = getCarManager("xp_vcu");
            CarEspManager espMgr = getCarManager("xp_esp");
            CarBcmManager bcmMgr = getCarManager("xp_bcm");
            mGearLevelStatus = vcuMgr.getDisplayGearLevel();
            hasGearLevelP = mGearLevelStatus == 4;
            hasAvhActive = espMgr.getAvh() == 1;
            hasRearFogLampOff = bcmMgr.getRearFogLamp() != 1;
            int[] data = bcmMgr.getLeftAndRightTurnLampsActiveStatus();
            hasTurnLampOff = (data[0] == 1 || data[1] == 1) ? false : true;
            if (hasSeriesD) {
                z = checkHeadLampModeOff();
            } else {
                z = bcmMgr.getHeadLampGroup() == 0;
            }
            hasHeadLightOff = z;
            hasTemporaryLock = bcmMgr.getTemporaryStopLockActivateStatus() == 1;
        } catch (Exception e) {
            LogUtil.e(TAG, "initCarStatus failed, " + e);
        }
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
        initCarStatus();
        LogUtil.i(TAG, "onCarManagerInited, hasIgOn=" + hasIgOn + ", hasRearFogLampOff=" + hasRearFogLampOff + ", hasTurnLampOff=" + hasTurnLampOff + ", hasGearLevelP=" + hasGearLevelP + ", hasTemporaryLock=" + hasTemporaryLock);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void initCarListener() {
        addMcuManagerCallback();
        addBcmManagerCallback();
        addEspManagerCallback();
        addVcuManagerCallback();
        addLluManagerCallback();
    }

    @Override // com.xiaopeng.xuiservice.utils.BroadcastManager.BroadcastListener
    public void onReceive(Context context, Intent intent) {
        LogUtil.i(TAG, "onReceive " + intent);
        String action = intent.getAction();
        if (action.equals("android.intent.action.BOOT_COMPLETED") && !hasBootCompleted) {
            hasBootCompleted = true;
        }
        if (hasIgOn && action.equals("com.xiaopeng.intent.action.xkey") && intent.getIntExtra("keyfunc", 0) == 2 && intent.getStringExtra("keytype").equals("short")) {
            int resId = this.mProxy.playXSayhiEffect();
            LogUtil.i(TAG, "play XSayhiEffect, resId=" + resId);
            ToastUtil.showToast(this.mContext, resId, 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    public void initXUIManager() throws XUIServiceNotConnectedException {
        LogUtil.i(TAG, "initXUIManager, registerVisualizerListener");
        this.mMediaCenterManager = (MediaCenterManager) getXuiManager().getXUIServiceManager(XUIConfig.PROPERTY_MEDIACENTER);
        this.mVisualCaptureListener = new MediaCenterManager.VisualCaptureListener() { // from class: com.xiaopeng.xuiservice.smart.SmartLampService.7
            public void OnFftDataCapture(byte[] bytes, int i) {
            }

            public void OnRatioData(float ratio, float minRatio) {
                if (SmartLampService.hasIgOn && SmartLampService.hasHeadLightOff && SmartLampService.hasTurnLampOff && SmartLampService.hasRearFogLampOff && SmartLampService.hasGearLevelP) {
                    LogUtil.d(SmartLampService.TAG, "onRatio " + ratio);
                    SmartLampService.this.mProxy.playRhythmEffect(ratio);
                }
            }
        };
        this.mMediaCenterManager.registerVisualizerListener(this.mVisualCaptureListener);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
        LogUtil.i(TAG, "onInit");
        registerReceiver();
        registerProvider();
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        LogUtil.i(TAG, "onRelease");
        removeCarListener();
    }

    public SmartLampService(Context context) {
        super(context);
        this.mMcuCallback = null;
        this.mBcmCallback = null;
        this.mEspCallback = null;
        this.mVcuCallback = null;
        this.mLluCallback = null;
        this.mProxy = XpLightlanuageProxy.getInstance();
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final SmartLampService sService = new SmartLampService(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    public static SmartLampService getInstance() {
        return InstanceHolder.sService;
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected boolean needConnectXUI() {
        return true;
    }
}
