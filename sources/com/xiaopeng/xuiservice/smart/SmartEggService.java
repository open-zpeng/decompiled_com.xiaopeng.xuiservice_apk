package com.xiaopeng.xuiservice.smart;

import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.bcm.CarBcmManager;
import android.car.hardware.srs.CarSrsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import com.xiaopeng.xuimanager.XUIServiceNotConnectedException;
import com.xiaopeng.xuimanager.contextinfo.ContextInfoManager;
import com.xiaopeng.xuimanager.smart.SmartManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.carcontrol.BaseCarListener;
import com.xiaopeng.xuiservice.utils.ToastUtil;
import java.io.File;
import java.io.PrintWriter;
import java.util.Calendar;
/* loaded from: classes5.dex */
public class SmartEggService extends BaseSmartService {
    private static final boolean DBG = true;
    private static final int EGG_SURPRISE_IGON_COUNT = 2;
    private static final int EGG_SURPRISE_PGEAR_COUNT = 3;
    private static final int EGG_SURPRISE_TIME_DAY = 1;
    private static final int EGG_SURPRISE_TIME_MONTH_12 = 12;
    private static final int EGG_SURPRISE_TIME_MONTH_3 = 3;
    private static final int EGG_SURPRISE_TIME_MONTH_6 = 6;
    private static final int EGG_SURPRISE_TIME_MONTH_9 = 9;
    private static final String LLU_EFFECT_SURPRISE_NORMAL = "CDU_AI_surprise_normal";
    private static final String[] PATH_SURPRISE_SOUND = {"/system/media/audio/xiaopeng/cdu/mp3/surprise_normal.mp3"};
    private static final String XP_SMART_EGG_LLU_INTENT = "com.xiaopeng.intent.action.smartegg.llu";
    private int carIgOnCount;
    private int carPGearCount;
    private boolean isCarIgOn;
    private boolean isDriverBeltBuck;
    private boolean isDriverOnSeat;
    private boolean isPGear;
    private int lastDay;
    private AudioManager mAudioManager;
    private CarBcmManager.CarBcmEventCallback mCarBcmEventCallback;
    private CarSrsManager.CarSrsEventCallback mCarSrsEventCallback;
    private ContextInfoManager.ContextCarStatusEventListener mContextCarStatusEventListener;
    private ContextInfoManager mContextInfoManager;
    private NotificationHelper mNotificationHelper;
    private final BroadcastReceiver mReceiver;
    private SmartManager.SmartEventListener mSmartEventListener;
    private SmartManager mSmartManager;
    private XUIConfig mXUIConfig;
    private MediaPlayer mediaPlayer;
    private int soundEffectStreamType;

    static /* synthetic */ int access$408(SmartEggService x0) {
        int i = x0.carIgOnCount;
        x0.carIgOnCount = i + 1;
        return i;
    }

    static /* synthetic */ int access$508(SmartEggService x0) {
        int i = x0.carPGearCount;
        x0.carPGearCount = i + 1;
        return i;
    }

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService, com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
    public /* bridge */ /* synthetic */ void debugDump(PrintWriter printWriter, String[] strArr) {
        super.debugDump(printWriter, strArr);
    }

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService
    public /* bridge */ /* synthetic */ void dump(PrintWriter printWriter, String[] strArr) {
        super.dump(printWriter, strArr);
    }

    private SmartEggService(Context context) {
        super(context);
        this.mediaPlayer = new MediaPlayer();
        this.mNotificationHelper = null;
        this.soundEffectStreamType = 3;
        this.isCarIgOn = false;
        this.isPGear = false;
        this.isDriverOnSeat = false;
        this.isDriverBeltBuck = false;
        this.carIgOnCount = 0;
        this.carPGearCount = 0;
        this.lastDay = 0;
        this.mReceiver = new BroadcastReceiver() { // from class: com.xiaopeng.xuiservice.smart.SmartEggService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                String str = SmartEggService.this.TAG;
                LogUtil.d(str, "action: " + action);
                if (action.equals(SmartEggService.XP_SMART_EGG_LLU_INTENT)) {
                    if (SmartEggService.this.isPGear) {
                        SmartEggService.this.handleSurprise();
                    } else {
                        ToastUtil.showToast(SmartEggService.this.mContext, "请停车后再查看惊喜");
                    }
                }
            }
        };
        this.mXUIConfig = new XUIConfig();
        this.mNotificationHelper = NotificationHelper.getInstance();
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
        registerReceiver();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    public void initXUIManager() throws XUIServiceNotConnectedException {
        try {
            this.mSmartManager = (SmartManager) getXuiManager().getXUIServiceManager(XUIConfig.PROPERTY_SMART);
            this.mContextInfoManager = (ContextInfoManager) getXuiManager().getXUIServiceManager(XUIConfig.PROPERTY_CONTEXTINFO);
            this.mContextCarStatusEventListener = new ContextInfoManager.ContextCarStatusEventListener() { // from class: com.xiaopeng.xuiservice.smart.SmartEggService.2
                public void onIGStatus(int status) {
                    if (status == 0) {
                        SmartEggService.this.isCarIgOn = false;
                    } else if (status == 1) {
                        SmartEggService.this.isCarIgOn = true;
                        SmartEggService.this.checkSameDay();
                        SmartEggService.access$408(SmartEggService.this);
                    }
                }

                public void onGearChanged(int gear) {
                    SmartEggService.this.isPGear = gear == 4;
                    if (SmartEggService.this.isPGear) {
                        SmartEggService.this.checkSameDay();
                        SmartEggService.access$508(SmartEggService.this);
                        SmartEggService.this.showInfoFlowCard();
                    }
                }

                public void onErrorEvent(int i, int i1) {
                }
            };
            try {
                this.mContextInfoManager.registerListener(this.mContextCarStatusEventListener);
            } catch (XUIServiceNotConnectedException e) {
                handleException(e);
            }
        } catch (XUIServiceNotConnectedException e2) {
            e2.printStackTrace();
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void initCarListener() {
        this.mCarSrsEventCallback = new CarSrsManager.CarSrsEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartEggService.3
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557849612) {
                    String str = SmartEggService.this.TAG;
                    LogUtil.i(str, "CarSrsManager.CarSrsEventCallback onChangeEvent : " + value.toString());
                    if (((Integer) BaseCarListener.getValue(value)).intValue() == 0) {
                        SmartEggService.this.isDriverBeltBuck = true;
                    } else if (((Integer) BaseCarListener.getValue(value)).intValue() == 1) {
                        SmartEggService.this.isDriverBeltBuck = false;
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarBcmEventCallback = new CarBcmManager.CarBcmEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartEggService.4
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557849607) {
                    if (((Integer) BaseCarListener.getValue(value)).intValue() == 0) {
                        SmartEggService.this.isDriverOnSeat = false;
                    } else if (((Integer) BaseCarListener.getValue(value)).intValue() == 1) {
                        SmartEggService.this.isDriverOnSeat = true;
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        addSrsManagerListener(this.mCarSrsEventCallback);
        addBcmManagerListener(this.mCarBcmEventCallback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        LogUtil.d(this.TAG, "onCarManagerInited");
    }

    public static SmartEggService getInstance() {
        return InstanceHolder.sService;
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        removeSrsManagerListener(this.mCarSrsEventCallback);
        removeBcmManagerListener(this.mCarBcmEventCallback);
        try {
            if (this.mSmartManager != null && this.mSmartEventListener != null) {
                this.mSmartManager.unregisterListener(this.mSmartEventListener);
            }
            if (this.mContextInfoManager != null) {
                this.mContextInfoManager.unregisterListener(this.mContextCarStatusEventListener);
            }
        } catch (XUIServiceNotConnectedException e) {
            e.printStackTrace();
        }
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
        unregisterReceiver();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSurprise() {
        try {
            this.mNotificationHelper.dismissEggInfoCard();
            this.mSmartManager.setLangLightEffectMode(LLU_EFFECT_SURPRISE_NORMAL);
            playSurpriseSound();
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void playSurpriseSound() {
        File file = new File(PATH_SURPRISE_SOUND[0]);
        if (file.exists()) {
            try {
                this.mediaPlayer.setDataSource(file.getPath());
                this.mediaPlayer.setLooping(false);
                this.mediaPlayer.prepare();
                this.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.xiaopeng.xuiservice.smart.SmartEggService.5
                    @Override // android.media.MediaPlayer.OnPreparedListener
                    public void onPrepared(MediaPlayer mp) {
                        SmartEggService.this.mAudioManager.requestAudioFocus(null, SmartEggService.this.soundEffectStreamType, 2);
                        SmartEggService.this.mediaPlayer.start();
                        try {
                            SmartEggService.this.mSmartManager.setFftLLUEnable(false);
                        } catch (Exception e) {
                            SmartEggService.this.handleException(e);
                        }
                    }
                });
                this.mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() { // from class: com.xiaopeng.xuiservice.smart.SmartEggService.6
                    @Override // android.media.MediaPlayer.OnErrorListener
                    public boolean onError(MediaPlayer paramMediaPlayer, int paramInt1, int paramInt2) {
                        SmartEggService.this.mediaPlayer.stop();
                        SmartEggService.this.mediaPlayer.reset();
                        SmartEggService.this.mAudioManager.abandonAudioFocus(null);
                        return false;
                    }
                });
                this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.xiaopeng.xuiservice.smart.SmartEggService.7
                    @Override // android.media.MediaPlayer.OnCompletionListener
                    public void onCompletion(MediaPlayer mp) {
                        SmartEggService.this.mediaPlayer.stop();
                        SmartEggService.this.mediaPlayer.reset();
                        SmartEggService.this.mAudioManager.abandonAudioFocus(null);
                        try {
                            SmartEggService.this.mSmartManager.setFftLLUEnable(true);
                        } catch (Exception e) {
                            SmartEggService.this.handleException(e);
                        }
                    }
                });
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        LogUtil.i(this.TAG, "The sound file isn't exist");
    }

    private boolean isSurpriseTime() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(2) + 1;
        int day = calendar.get(5);
        return (month == 3 || month == 6 || month == 9 || month == 12) && day == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkSameDay() {
        Calendar calendar = Calendar.getInstance();
        if (this.lastDay != calendar.get(5)) {
            this.carIgOnCount = 0;
            this.carPGearCount = 0;
            this.lastDay = calendar.get(5);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showInfoFlowCard() {
        if (this.isCarIgOn && this.isPGear && this.isDriverOnSeat && this.isDriverBeltBuck && this.carIgOnCount == 2 && this.carPGearCount == 3 && isSurpriseTime()) {
            XUIConfig xUIConfig = this.mXUIConfig;
            if (XUIConfig.hasFeature(XUIConfig.PROPERTY_LLU)) {
                this.mNotificationHelper.showEggInfoCard();
            }
        }
    }

    private void registerReceiver() {
        LogUtil.d(this.TAG, "registerReceiver");
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(XP_SMART_EGG_LLU_INTENT);
        this.mContext.registerReceiver(this.mReceiver, mIntentFilter);
    }

    public void unregisterReceiver() {
        LogUtil.d(this.TAG, "unregisterReceiver.");
        this.mContext.unregisterReceiver(this.mReceiver);
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final SmartEggService sService = new SmartEggService(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected boolean needConnectXUI() {
        return true;
    }
}
