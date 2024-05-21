package com.xiaopeng.xuiservice.smart;

import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.amp.CarAmpManager;
import android.car.hardware.bcm.CarBcmManager;
import android.car.hardware.mcu.CarMcuManager;
import android.car.hardware.scu.CarScuManager;
import android.car.hardware.srs.CarSrsManager;
import android.car.hardware.tbox.CarTboxManager;
import android.car.hardware.vcu.CarVcuManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.media.AudioConfig.AudioConfig;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemProperties;
import android.provider.Settings;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.carcontrol.BaseCarListener;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.ToastUtil;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import com.xiaopeng.xuiservice.xapp.Constants;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes5.dex */
public class SmartVolumeService extends BaseSmartService implements BroadcastManager.BroadcastListener {
    private static final String ACTION_VOLUME_CHANGED = "android.media.VOLUME_CHANGED_ACTION";
    private static final String AVAS_STREAM_ENABLE = "avas_speaker";
    private static final int BCM_DOOR_CLOSE = 0;
    private static final int BCM_DOOR_OPEN = 1;
    private static final int CHECK_PASSENGER_DELIEVER_TIME = 5000;
    private static final boolean DBG = true;
    private static final String EXTRA_VOLCHANGE_PACKAGENAME = "android.media.vol_change.PACKAGE_NAME";
    private static final String EXTRA_VOLUME_STREAM_TYPE = "android.media.EXTRA_VOLUME_STREAM_TYPE";
    private static final String NET_ECALL_ENABLE = "XpNetECallEnable";
    private static final String PROP_VOLUME_MEDIA = "persist.audio.volume.media";
    private static final String PROP_VOLUME_NAVIGATION = "persist.audio.volume.navi";
    private static final int SPEED_VOLUME_SOFT = 3;
    private static final int SPEED_VOLUME_STANDARD = 1;
    private static final int SPEED_VOLUME_SURGE = 2;
    private static final int STREAM_VOLUME_MIN_LEVEL = 1;
    private static final String TAG = "SmartVolumeService";
    private static final int TEMPVOLCHANGE_FLAG_AEB = 128;
    private static final int TEMPVOLCHANGE_FLAG_DOOR = 2;
    private static final int TEMPVOLCHANGE_FLAG_GEAR = 1;
    private static final int VOLUME_DOWN = 2;
    private static final int VOLUME_MIN_LEVEL = 1;
    private static final int VOLUME_NONE = 0;
    private static final int VOLUME_UP = 1;
    private volatile boolean adjudgeOnlyDriverDoor;
    private int curAudioDriverMode;
    private volatile boolean isDoorOpen;
    private AudioConfig mAudioConfig;
    private AudioManager mAudioManager;
    private CarAmpManager.CarAmpEventCallback mCarAmpEventCallback;
    private CarBcmManager.CarBcmEventCallback mCarBcmEventCallback;
    private CarBcmManager mCarBcmManager;
    private CarMcuManager.CarMcuEventCallback mCarMcuEventCallback;
    private CarMcuManager mCarMcuManager;
    private CarScuManager.CarScuEventCallback mCarScuEventCallback;
    private CarSrsManager.CarSrsEventCallback mCarSrsEventCallback;
    private CarSrsManager mCarSrsManager;
    private CarTboxManager.CarTboxEventCallback mCarTboxEventCallback;
    private CarTboxManager mCarTboxManager;
    private CarVcuManager.CarVcuEventCallback mCarVcuEventCallback;
    private CarVcuManager mCarVcuManager;
    private CarVcuManager.CarVcuEventCallback mCarVcuSpeedEventCallback;
    private int mOriginalLevel;
    private int mOriginalNavLevel;
    private BroadcastReceiver mReceiver;
    private static boolean SpeedVolumeEnabled = false;
    private static int SpeedVolumeMode = 1;
    private static int SpeedMusicVolumeSetted = -1;
    private static final Map<Integer, Integer> mVolumeStandardMap = new HashMap<Integer, Integer>() { // from class: com.xiaopeng.xuiservice.smart.SmartVolumeService.1
        {
            put(1, 12);
            put(2, 14);
            put(3, 15);
            put(4, 16);
            put(5, 16);
            put(6, 17);
            put(7, 18);
            put(8, 18);
            put(9, 18);
        }
    };
    private static final Map<Integer, Integer> mVolumeSoftMap = new HashMap<Integer, Integer>() { // from class: com.xiaopeng.xuiservice.smart.SmartVolumeService.2
        {
            put(1, 11);
            put(2, 12);
            put(3, 13);
            put(4, 14);
            put(5, 14);
            put(6, 15);
            put(7, 16);
            put(8, 16);
            put(9, 16);
        }
    };
    private static final Map<Integer, Integer> mVolumeSurgeMap = new HashMap<Integer, Integer>() { // from class: com.xiaopeng.xuiservice.smart.SmartVolumeService.3
        {
            put(1, 15);
            put(2, 16);
            put(3, 17);
            put(4, 18);
            put(5, 18);
            put(6, 19);
            put(7, 20);
            put(8, 21);
            put(9, 22);
        }
    };
    private static final Map<Integer, Map> mVolumeMap = new HashMap<Integer, Map>() { // from class: com.xiaopeng.xuiservice.smart.SmartVolumeService.4
        {
            put(1, SmartVolumeService.mVolumeStandardMap);
            put(2, SmartVolumeService.mVolumeSurgeMap);
            put(3, SmartVolumeService.mVolumeSoftMap);
        }
    };
    private static boolean muteByScu = false;
    private static boolean dirverDoorOpened = false;
    private static boolean passengerDoorOpened = false;
    private static boolean passengerOccupancy = false;
    private static boolean AccessibilityVolDownInRGear = SystemProperties.getBoolean("persist.sys.rgear.accessibility.voldown", true);
    private static int DISABLE_REASON_GEAR = 0;
    private static int DISABLE_REASON_IG = 1;

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService, com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
    public /* bridge */ /* synthetic */ void debugDump(PrintWriter printWriter, String[] strArr) {
        super.debugDump(printWriter, strArr);
    }

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService
    public /* bridge */ /* synthetic */ void dump(PrintWriter printWriter, String[] strArr) {
        super.dump(printWriter, strArr);
    }

    private SmartVolumeService(Context context) {
        super(context);
        this.mOriginalLevel = -1;
        this.mOriginalNavLevel = -1;
        this.isDoorOpen = false;
        this.adjudgeOnlyDriverDoor = true;
        this.mReceiver = null;
        this.mAudioConfig = null;
        this.curAudioDriverMode = 0;
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
        this.mOriginalLevel = SystemProperties.getInt(PROP_VOLUME_MEDIA, -1);
        this.mOriginalNavLevel = SystemProperties.getInt(PROP_VOLUME_NAVIGATION, -1);
        LogUtil.i(TAG, "SmartVolumeService " + this.mOriginalLevel + " " + this.mOriginalNavLevel);
        this.mAudioConfig = new AudioConfig(context);
        SpeedVolumeEnabled = Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isSpeedVolumeEnabled", 0) == 1;
        SpeedVolumeMode = Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "SpeedVolumeMode", 1);
        registerReceiver();
        this.curAudioDriverMode = this.mAudioConfig.getMainDriverMode();
    }

    public static SmartVolumeService getInstance() {
        return InstanceHolder.sService;
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void initCarListener() {
        this.mCarVcuEventCallback = new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartVolumeService.5
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557847045) {
                    SmartVolumeService.this.handleCarGearLevelChanged(((Integer) value.getValue()).intValue(), false);
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarVcuSpeedEventCallback = new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartVolumeService.6
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 559944229) {
                    Float ovalue = (Float) value.getValue();
                    SmartVolumeService.this.handleSpeedVolumeChanged(ovalue.floatValue());
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarBcmEventCallback = new CarBcmManager.CarBcmEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartVolumeService.7
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557915161) {
                    LogUtil.i(SmartVolumeService.TAG, "CarBcmManager.CarBcmEventCallback onChangeEvent : " + value.toString());
                    Object[] values = (Object[]) BaseCarListener.getValue(value);
                    if (values != null) {
                        int[] doorState = new int[values.length];
                        for (int i = 0; i < values.length; i++) {
                            Object objValue = values[i];
                            if (objValue instanceof Integer) {
                                doorState[i] = ((Integer) objValue).intValue();
                            }
                        }
                        SmartVolumeService.this.handleCarDoorChanged(doorState);
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarMcuEventCallback = new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartVolumeService.8
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557847561) {
                    LogUtil.i(SmartVolumeService.TAG, "CarMcuManager.CarMcuEventCallback  :" + value.toString());
                    if (((Integer) value.getValue()).intValue() == 1) {
                        LogUtil.i(SmartVolumeService.TAG, "IG ON  set unmute!!");
                        if (!XUIConfig.isInternationalEnable() && !XUIConfig.isNativeEcallEnable() && Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), SmartVolumeService.NET_ECALL_ENABLE, 0) == 1) {
                            Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), SmartVolumeService.NET_ECALL_ENABLE, 0);
                        }
                        boolean isMuted = SmartVolumeService.this.mAudioManager.isStreamMute(3);
                        SmartVolumeService.this.mAudioManager.adjustStreamVolume(3, 100, 268599296);
                        if (!isMuted) {
                            try {
                                if (SmartVolumeService.this.mCarBcmManager != null) {
                                    SmartVolumeService.this.handleCarDoorChanged(SmartVolumeService.this.mCarBcmManager.getDoorsState());
                                }
                            } catch (Exception e) {
                                LogUtil.e(SmartVolumeService.TAG, "handleCarDoorChanged error");
                            }
                        } else {
                            XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.SmartVolumeService.8.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    try {
                                        if (SmartVolumeService.this.mCarBcmManager != null) {
                                            SmartVolumeService.this.handleCarDoorChanged(SmartVolumeService.this.mCarBcmManager.getDoorsState());
                                        }
                                    } catch (Exception e2) {
                                        LogUtil.e(SmartVolumeService.TAG, "handleCarDoorChanged error");
                                    }
                                }
                            }, 1000L);
                        }
                        SmartVolumeService.this.doDisableAvas(SmartVolumeService.DISABLE_REASON_IG, ((Integer) value.getValue()).intValue());
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarScuEventCallback = new CarScuManager.CarScuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartVolumeService.9
            public void onChangeEvent(CarPropertyValue value) {
                Object[] values;
                if (value.getPropertyId() == 557917818 && (values = (Object[]) BaseCarListener.getValue(value)) != null && values.length >= 4) {
                    int[] result = new int[values.length];
                    Object objValueEBA = values[2];
                    Object objValueAEB = values[3];
                    if (objValueEBA instanceof Integer) {
                        result[2] = ((Integer) objValueEBA).intValue();
                    }
                    if (objValueAEB instanceof Integer) {
                        result[3] = ((Integer) objValueAEB).intValue();
                    }
                    LogUtil.i(SmartVolumeService.TAG, "objValueEBA:" + result[2] + "  objValueAEB:" + result[3]);
                    if (result[3] == 1 || result[3] == 2 || result[2] == 1 || result[2] == 2) {
                        LogUtil.i(SmartVolumeService.TAG, "AEB_STATUS_ACTIVE_TO_CAR  MUTE MUSIC!!");
                        SmartVolumeService.this.mAudioManager.temporaryChangeVolumeDown(3, 1, 128, false);
                        boolean unused = SmartVolumeService.muteByScu = true;
                    } else if (result[3] == 0 && result[2] == 0 && SmartVolumeService.muteByScu) {
                        LogUtil.i(SmartVolumeService.TAG, "AEB_STATUS_INACTIVE  MUTE MUSIC!!");
                        SmartVolumeService.this.mAudioManager.temporaryChangeVolumeDown(3, 1, 128, true);
                        boolean unused2 = SmartVolumeService.muteByScu = false;
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarSrsEventCallback = new CarSrsManager.CarSrsEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartVolumeService.10
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557849679) {
                    SmartVolumeService.this.handlePassengerOccupancy(((Integer) value.getValue()).intValue());
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarTboxEventCallback = new CarTboxManager.CarTboxEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartVolumeService.11
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557846611) {
                    SmartVolumeService.this.handleECallMute(((Integer) BaseCarListener.getValue(value)).intValue());
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarAmpEventCallback = new CarAmpManager.CarAmpEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartVolumeService.12
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557850649 && SmartVolumeService.this.mAudioManager != null) {
                    if (((Integer) BaseCarListener.getValue(value)).intValue() == 2) {
                        LogUtil.i(SmartVolumeService.TAG, "A2B_CMD=reconnect!!");
                        SmartVolumeService.this.mAudioManager.setParameters("A2B_CMD=reconnect;");
                    } else if (((Integer) BaseCarListener.getValue(value)).intValue() == 1) {
                        LogUtil.i(SmartVolumeService.TAG, "A2B_CMD=connected!!");
                        SmartVolumeService.this.mAudioManager.setParameters("A2B_CMD=connected;");
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        addTboxManagerListener(this.mCarTboxEventCallback);
        addBcmManagerListener(this.mCarBcmEventCallback);
        addVcuManagerListener(this.mCarVcuEventCallback);
        addMcuManagerListener(this.mCarMcuEventCallback);
        addScuManagerListener(this.mCarScuEventCallback);
        addSrsManagerListener(this.mCarSrsEventCallback);
        addAmpManagerListener(this.mCarAmpEventCallback);
        if (SpeedVolumeEnabled) {
            addVcuSpecialManagerListener(this.mCarVcuSpeedEventCallback);
        }
        initCarProp();
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        LogUtil.d(TAG, "onCarManagerInited");
        this.mCarBcmManager = getCarManager("xp_bcm");
        this.mCarVcuManager = getCarManager("xp_vcu");
        this.mCarMcuManager = getCarManager("xp_mcu");
        this.mCarSrsManager = getCarManager("xp_srs");
        this.mCarTboxManager = getCarManager("xp_tbox");
    }

    private void initCarProp() {
        try {
            if (this.mCarSrsManager != null) {
                handlePassengerOccupancy(this.mCarSrsManager.getPsnOnSeat());
            }
            if (this.mCarBcmManager != null) {
                handleCarDoorChanged(this.mCarBcmManager.getDoorsState());
            }
            if (this.mCarVcuManager != null) {
                handleCarGearLevelChanged(this.mCarVcuManager.getDisplayGearLevel(), false);
            }
            if ((XUIConfig.isInternationalEnable() || XUIConfig.isNativeEcallEnable()) && this.mCarTboxManager != null) {
                handleECallMute(this.mCarTboxManager.getEcallMuteRequest());
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "initCarProp error");
        }
    }

    private boolean needSetDriverModeAutomatic() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "auto_switch_main_driver_mode", 0) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyPassengerStatus(boolean onSeat) {
        broadcastPassengerStatus(onSeat);
        if (needSetDriverModeAutomatic()) {
            AudioConfig audioConfig = this.mAudioConfig;
            if (AudioConfig.isHighConfig()) {
                if (!onSeat) {
                    if (this.mAudioConfig.getMainDriverMode() == 1) {
                        this.mAudioConfig.setMainDriverMode(this.curAudioDriverMode);
                        return;
                    }
                    return;
                }
                this.curAudioDriverMode = this.mAudioConfig.getMainDriverMode();
                this.mAudioConfig.setMainDriverMode(1);
            }
        }
    }

    private void broadcastPassengerStatus(boolean onSeat) {
        LogUtil.i(TAG, "broadcastPassengerStatus");
        Intent intent = new Intent("com.xiaopeng.xui.passenger.status");
        intent.addFlags(16777216);
        intent.putExtra("status", onSeat ? 1 : 0);
        BroadcastManager.getInstance().sendBroadcast(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePassengerOccupancy(int state) {
        LogUtil.i(TAG, "handlePassengerOccupancy  state:" + state + "|DoorOpened:" + passengerDoorOpened);
        boolean psngerStatus = state == 1;
        if (passengerOccupancy != psngerStatus) {
            passengerOccupancy = psngerStatus;
            if (!passengerOccupancy) {
                XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.SmartVolumeService.13
                    @Override // java.lang.Runnable
                    public void run() {
                        if (!SmartVolumeService.passengerOccupancy && SmartVolumeService.passengerDoorOpened) {
                            SmartVolumeService.this.notifyPassengerStatus(false);
                        }
                    }
                }, 5000L);
            } else {
                XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.SmartVolumeService.14
                    @Override // java.lang.Runnable
                    public void run() {
                        if (SmartVolumeService.passengerOccupancy) {
                            SmartVolumeService.this.notifyPassengerStatus(true);
                        }
                    }
                }, 5000L);
            }
        }
    }

    private void handlePassengerDoorState(int state) {
        LogUtil.i(TAG, "handlePassengerOccupancy  doorState:" + state + "|Occupancy:" + passengerOccupancy);
        boolean stateOpen = state == 1;
        if (passengerDoorOpened != stateOpen) {
            passengerDoorOpened = stateOpen;
            if (!passengerDoorOpened) {
                if (passengerOccupancy) {
                    notifyPassengerStatus(true);
                } else {
                    XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.SmartVolumeService.15
                        @Override // java.lang.Runnable
                        public void run() {
                            if (!SmartVolumeService.passengerOccupancy) {
                                SmartVolumeService.this.notifyPassengerStatus(false);
                            }
                        }
                    }, 5000L);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCarDoorChanged(int[] doorState) {
        try {
            if (this.mCarMcuManager.getIgStatusFromMcu() != 1) {
                LogUtil.w(TAG, "handleCarDoorChanged not in ig on status!!");
                return;
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "handleCarDoorChanged error");
        }
        handleVolDownByDoorStateChanged(getVolDownDoorState(doorState), false);
        if (doorState.length > 1) {
            handlePassengerDoorState(doorState[1]);
        }
    }

    private boolean getVolDownDoorState(int[] doorState) {
        boolean volDownDoorOpen = false;
        if (this.adjudgeOnlyDriverDoor) {
            boolean volDownDoorOpen2 = doorState[0] == 1;
            return volDownDoorOpen2;
        }
        for (int i : doorState) {
            if (i == 1) {
                volDownDoorOpen = true;
            }
        }
        return volDownDoorOpen;
    }

    private boolean isLightDancing() {
        return SystemProperties.getBoolean("persist.sys.light.dancing", false);
    }

    private void handleVolDownByDoorStateChanged(boolean isOpen, boolean forceChange) {
        LogUtil.i(TAG, "handleVolDownByDoorStateChanged() isOpen = " + isOpen + " " + forceChange + " " + getVolumeDownWithDoorOpen());
        if (isOpen && this.isDoorOpen == isOpen && !forceChange) {
            LogUtil.d(TAG, "handleVolDownByDoorStateChanged door is already opened");
            return;
        }
        this.isDoorOpen = isOpen;
        AudioConfig audioConfig = this.mAudioConfig;
        boolean z = false;
        if (audioConfig != null && isOpen) {
            audioConfig.doZenVolumeProcess(false);
        }
        if (getVolumeDownWithDoorOpen() || forceChange) {
            String log_str = this.isDoorOpen ? "down with door open" : "up with door close";
            LogUtil.i(TAG, "Volume " + log_str);
            boolean volDown = isOpen;
            if (forceChange) {
                if (getVolumeDownWithDoorOpen() && isOpen) {
                    z = true;
                }
                volDown = z;
            }
            doVolumeProcess(3, volDown ? 2 : 1, 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCarGearLevelChanged(int level, boolean forceChange) {
        LogUtil.i(TAG, "handleCarGearLevelChanged() gear = " + level + " " + forceChange);
        boolean z = false;
        boolean isRGear = level == 3;
        boolean isPGear = level == 4;
        AudioConfig audioConfig = this.mAudioConfig;
        if (audioConfig != null && isRGear) {
            audioConfig.doZenVolumeProcess(false);
        }
        if (getVolumeDownInReverseMode() || forceChange) {
            String log_str = isRGear ? "down with R gear" : "up with P/D/N gear";
            LogUtil.i(TAG, "Volume " + log_str);
            boolean volDown = isRGear;
            if (forceChange) {
                if (isRGear && getVolumeDownInReverseMode()) {
                    z = true;
                }
                volDown = z;
            }
            int direction = volDown ? 2 : 1;
            if (AccessibilityVolDownInRGear) {
                doVolumeProcess(10, direction, 1);
            }
            doVolumeProcess(9, direction, 1);
            doVolumeProcess(3, direction, 1);
        }
        if (!isPGear) {
            LogUtil.d(TAG, "**check avas speaker status while out of P Gear");
            doDisableAvas(DISABLE_REASON_GEAR, level);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doDisableAvas(int reason, int value) {
        boolean isAvasSpeakerEnable = Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "avas_speaker", 0) == 1;
        if (isAvasSpeakerEnable) {
            if (reason == DISABLE_REASON_GEAR) {
                LogUtil.i(TAG, "**Shutoff avas speaker, because entered gear " + value);
                ToastUtil.showToast(this.mContext, (int) R.string.avas_button_disabled_in_none_p_gear, 1);
            } else if (reason == DISABLE_REASON_IG) {
                LogUtil.i(TAG, "**Shutoff avas speaker, because ig change : " + value);
            }
            Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "avas_speaker", 0);
        }
    }

    private void doVolumeProcess(int streamType, int direction, int flag) {
        AudioConfig audioConfig;
        if (!this.mAudioManager.isStreamMute(streamType)) {
            if (direction != 1) {
                if (direction == 2 && this.mAudioManager.getStreamVolume(streamType) >= 1 && !this.mAudioManager.isMainDriverOn() && (audioConfig = this.mAudioConfig) != null && audioConfig.getBanVolumeChangeMode(streamType) == 0) {
                    this.mAudioManager.temporaryChangeVolumeDown(streamType, 1, flag, false);
                    return;
                }
                return;
            }
            this.mAudioManager.temporaryChangeVolumeDown(streamType, 1, flag, true);
            return;
        }
        LogUtil.w(TAG, "Don't adjust volume because the stream " + streamType + " is mute");
    }

    private void updateVolumeDownWithDoorOpen() {
        try {
            int[] doorsState = this.mCarBcmManager.getDoorsState();
            if (doorsState != null) {
                handleVolDownByDoorStateChanged(getVolDownDoorState(doorsState), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateVolumeDownInReverseMode() {
        try {
            handleCarGearLevelChanged(this.mCarVcuManager.getStallState(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getVolumeDownWithDoorOpen() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isVolumeDownWithDoorOpenSet", 1) == 1;
    }

    public void setVolumeDownWithDoorOpen(boolean enable) {
        LogUtil.i(TAG, "setVolumeDownWithDoorOpen enable " + enable);
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isVolumeDownWithDoorOpenSet", enable ? 1 : 0);
        updateVolumeDownWithDoorOpen();
    }

    public boolean getVolumeDownInReverseMode() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isVolumeDownInReverseModeSet", 1) == 1;
    }

    public void setVolumeDownInReverseMode(boolean enable) {
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isVolumeDownInReverseModeSet", enable ? 1 : 0);
        updateVolumeDownInReverseMode();
    }

    public void enableCarSpeedVolume(boolean enable) {
        LogUtil.i(TAG, "enableCarSpeedVolume() enable = " + enable);
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isSpeedVolumeEnabled", enable ? 1 : 0);
        SpeedVolumeEnabled = enable;
        if (SpeedVolumeEnabled) {
            addVcuSpecialManagerListener(this.mCarVcuSpeedEventCallback);
        } else {
            removeVcuSpecialManagerListener(this.mCarVcuSpeedEventCallback);
        }
        if (!enable) {
            SpeedMusicVolumeSetted = -1;
        }
    }

    public void setSpeedVolumeMode(int type) {
        LogUtil.i(TAG, "setSpeedVolumeMode() type = " + type);
        if (type > 3 || type < 1) {
            LogUtil.e(TAG, "setSpeedVolumeMode() type = " + type + " out of range!!!");
            return;
        }
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "SpeedVolumeMode", type);
        SpeedVolumeMode = type;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSpeedVolumeChanged(float speed) {
        if (!SpeedVolumeEnabled) {
            return;
        }
        int level = getSpeedLevel(speed);
        Map<Integer, Integer> vMap = mVolumeMap.get(Integer.valueOf(SpeedVolumeMode));
        int volume = vMap.get(Integer.valueOf(level)).intValue();
        if (volume != SpeedMusicVolumeSetted && this.mAudioManager != null) {
            LogUtil.i(TAG, "handleSpeedVolumeChanged " + speed + " volume:" + volume);
            this.mAudioManager.setStreamVolume(3, volume, 0);
            SpeedMusicVolumeSetted = volume;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleECallMute(int muteflag) {
        if (XUIConfig.isInternationalEnable() || XUIConfig.isNativeEcallEnable()) {
            LogUtil.i(TAG, "handleECallMute " + muteflag);
            boolean mute = muteflag == 1;
            AudioManager audioManager = this.mAudioManager;
            if (audioManager != null) {
                audioManager.setFixedVolume(mute, 0, 3);
                this.mAudioManager.setFixedVolume(mute, 0, 1);
                this.mAudioManager.setFixedVolume(mute, 0, 6);
                this.mAudioManager.setFixedVolume(mute, 0, 10);
                this.mAudioManager.setFixedVolume(mute, 0, 9);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNetECallMute(int muteflag) {
        if (!XUIConfig.isInternationalEnable() && !XUIConfig.isNativeEcallEnable()) {
            LogUtil.i(TAG, "handleNetECallMute " + muteflag);
            boolean mute = muteflag == 1;
            AudioManager audioManager = this.mAudioManager;
            if (audioManager != null) {
                audioManager.setNetEcallEnable(mute);
                this.mAudioManager.setFixedVolume(mute, 0, 3);
                this.mAudioManager.setFixedVolume(mute, 0, 1);
                this.mAudioManager.setFixedVolume(mute, 0, 9);
            }
        }
    }

    private int getSpeedLevel(float speed) {
        if (0.0f <= speed && speed < 50.0f) {
            return 1;
        }
        if (50.0f <= speed && speed < 60.0f) {
            return 2;
        }
        if (60.0f <= speed && speed < 70.0f) {
            return 3;
        }
        if (70.0f <= speed && speed < 80.0f) {
            return 4;
        }
        if (80.0f <= speed && speed < 90.0f) {
            return 5;
        }
        if (90.0f <= speed && speed < 100.0f) {
            return 6;
        }
        if (100.0f <= speed && speed < 110.0f) {
            return 7;
        }
        if (110.0f <= speed && speed < 120.0f) {
            return 8;
        }
        return 9;
    }

    @Override // com.xiaopeng.xuiservice.utils.BroadcastManager.BroadcastListener
    public void onReceive(Context context, Intent intent) {
        LogUtil.d(TAG, "##onReceive,intent=" + intent);
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            this.mOriginalLevel = this.mAudioManager.getStreamVolume(3);
            this.mOriginalNavLevel = this.mAudioManager.getStreamVolume(9);
        }
    }

    private void registerReceiver() {
        LogUtil.i(TAG, "registerReceiver");
        if (this.mReceiver == null) {
            this.mReceiver = new BroadcastReceiver() { // from class: com.xiaopeng.xuiservice.smart.SmartVolumeService.16
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    String vol_change_package = intent.getStringExtra(SmartVolumeService.EXTRA_VOLCHANGE_PACKAGENAME);
                    int vol_change_stream_type = intent.getIntExtra(SmartVolumeService.EXTRA_VOLUME_STREAM_TYPE, -1);
                    LogUtil.i(SmartVolumeService.TAG, "action: " + action + "  Volume_Change_PackageName: " + vol_change_package + "  Vol_Change_Stream_Type: " + vol_change_stream_type);
                    if (action.equals("android.media.VOLUME_CHANGED_ACTION")) {
                        if (!vol_change_package.equals("com.xiaopeng.xuiservice") && !vol_change_package.equals(Constants.PACKAGE_CAR_CONTROL) && !vol_change_package.equals("xpaudio_btcall") && vol_change_stream_type == 3) {
                            SmartVolumeService smartVolumeService = SmartVolumeService.this;
                            smartVolumeService.mOriginalLevel = smartVolumeService.mAudioManager.getStreamVolume(3);
                            LogUtil.i(SmartVolumeService.TAG, "mOriginalLevel: " + SmartVolumeService.this.mOriginalLevel);
                        }
                        if (!vol_change_package.equals("com.xiaopeng.xuiservice") && !vol_change_package.equals(Constants.PACKAGE_CAR_CONTROL) && vol_change_stream_type == 9) {
                            SmartVolumeService smartVolumeService2 = SmartVolumeService.this;
                            smartVolumeService2.mOriginalNavLevel = smartVolumeService2.mAudioManager.getStreamVolume(9);
                            LogUtil.i(SmartVolumeService.TAG, "mOriginalNavLevel: " + SmartVolumeService.this.mOriginalNavLevel);
                        }
                    }
                }
            };
        }
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.media.VOLUME_CHANGED_ACTION");
        this.mContext.registerReceiver(this.mReceiver, mIntentFilter);
        List<String> filter = new ArrayList<>();
        filter.add("android.intent.action.BOOT_COMPLETED");
        BroadcastManager.getInstance().registerListener(this, filter);
        if (!XUIConfig.isInternationalEnable() && !XUIConfig.isNativeEcallEnable()) {
            this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor(NET_ECALL_ENABLE), true, new ContentObserver(new Handler()) { // from class: com.xiaopeng.xuiservice.smart.SmartVolumeService.17
                @Override // android.database.ContentObserver
                public void onChange(boolean selfChange, Uri uri) {
                    super.onChange(selfChange, uri);
                    int xpNetEcallStatus = Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), SmartVolumeService.NET_ECALL_ENABLE, 0);
                    LogUtil.i(SmartVolumeService.TAG, "XpNetECallEnable  change to " + xpNetEcallStatus);
                    SmartVolumeService.this.handleNetECallMute(xpNetEcallStatus);
                }
            });
        }
    }

    public void unregisterReceiver() {
        LogUtil.i(TAG, "unregisterReceiver..");
        this.mContext.unregisterReceiver(this.mReceiver);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        removeBcmManagerListener(this.mCarBcmEventCallback);
        removeVcuManagerListener(this.mCarVcuEventCallback);
        removeMcuManagerListener(this.mCarMcuEventCallback);
        removeScuManagerListener(this.mCarScuEventCallback);
        removeVcuSpecialManagerListener(this.mCarVcuSpeedEventCallback);
        unregisterReceiver();
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final SmartVolumeService sService = new SmartVolumeService(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }
}
