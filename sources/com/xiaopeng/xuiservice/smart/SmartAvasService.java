package com.xiaopeng.xuiservice.smart;

import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.avas.CarAvasManager;
import android.car.hardware.esp.CarEspManager;
import android.car.hardware.mcu.CarMcuManager;
import android.car.hardware.vcu.CarVcuManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.provider.Settings;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.carcontrol.BaseCarListener;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.PrintWriter;
import java.util.HashMap;
/* loaded from: classes5.dex */
public class SmartAvasService extends BaseSmartService {
    private static final String AVAS_FILE_PATH = "/system/media/audio/xiaopeng/cdu/wav/";
    private static final int AVAS_SOUND_EFFECT_A = 1;
    private static final int AVAS_SOUND_EFFECT_B = 2;
    private static final int AVAS_SOUND_EFFECT_C = 3;
    private static final int SAYHI_EFFECT_NUM = 3;
    private static final String STR_GEAR_AVH_ENABLE = "avasGearAvhSayhiEnable";
    private static final String STR_LLU_ENABLE = "LangLightEffectEnable";
    private static final String STR_SAYHI_ENABLE = "isSayHiEnable";
    private static final int XKEY_SHOW_OFF = 2;
    private static final String XP_XKEY_INTENT = "com.xiaopeng.intent.action.xkey";
    private static boolean avasSayhiEnable;
    private static boolean avasSayhiEnableDefault;
    private static boolean ig_status_on = true;
    private static boolean lluEnable;
    private static boolean sayhiEnable;
    private String SayHiName;
    private int curAvhStatus;
    private int curGear;
    private AudioManager mAudioManager;
    private CarAvasManager mCarAvasManager;
    private CarEspManager.CarEspEventCallback mCarEspEventCallback;
    private CarMcuManager.CarMcuEventCallback mCarMcuEventCallback;
    private CarMcuManager mCarMcuManager;
    private CarVcuManager.CarVcuEventCallback mCarVcuEventCallback;
    private CarVcuManager mCarVcuManager;
    private final BroadcastReceiver mReceiver;
    private SoundPool mSoundPool;
    private HashMap<String, Integer> soundData;

    static /* synthetic */ boolean access$700() {
        return getSayHiAvasEnable();
    }

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService, com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
    public /* bridge */ /* synthetic */ void debugDump(PrintWriter printWriter, String[] strArr) {
        super.debugDump(printWriter, strArr);
    }

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService
    public /* bridge */ /* synthetic */ void dump(PrintWriter printWriter, String[] strArr) {
        super.dump(printWriter, strArr);
    }

    static {
        lluEnable = Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), STR_LLU_ENABLE, 1) == 1;
        sayhiEnable = Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), STR_SAYHI_ENABLE, 0) == 1;
        avasSayhiEnableDefault = XUIConfig.avasSayhiDefault();
        avasSayhiEnable = getSayHiAvasEnable();
    }

    private SmartAvasService(Context context) {
        super(context);
        this.curGear = 0;
        this.curAvhStatus = 0;
        this.SayHiName = "CDU_avas_sayhi_mode_";
        this.mReceiver = new BroadcastReceiver() { // from class: com.xiaopeng.xuiservice.smart.SmartAvasService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String str = SmartAvasService.this.TAG;
                LogUtil.i(str, "SmartAvasService mReceiver" + intent.getAction() + "  keytype:" + intent.getStringExtra("keytype"));
                String action = intent.getAction();
                if (action.equals("com.xiaopeng.intent.action.xkey") && intent.getStringExtra("keytype").equals("short") && intent.getIntExtra("keyfunc", 0) == 2) {
                    if (SmartAvasService.avasSayhiEnableDefault || SmartAvasService.avasSayhiEnable) {
                        SmartAvasService.this.playAvasSayHiSound();
                    }
                }
            }
        };
        if (XUIConfig.getAvasSupportType() == 1) {
            this.mAudioManager = (AudioManager) context.getSystemService("audio");
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void initCarListener() {
        this.mCarMcuEventCallback = new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartAvasService.2
            public void onChangeEvent(CarPropertyValue val) {
                if (val.getPropertyId() == 557847561) {
                    if (((Integer) BaseCarListener.getValue(val)).intValue() == 0) {
                        boolean unused = SmartAvasService.ig_status_on = false;
                    } else if (((Integer) BaseCarListener.getValue(val)).intValue() == 1) {
                        boolean unused2 = SmartAvasService.ig_status_on = true;
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
                LogUtil.e(SmartAvasService.this.TAG, "CarMcuManager.CarMcuEventCallback onErrorEvent");
            }
        };
        this.mCarVcuEventCallback = new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartAvasService.3
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557847045) {
                    SmartAvasService.this.handleCarGearLevelChanged(((Integer) value.getValue()).intValue());
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
                LogUtil.e(SmartAvasService.this.TAG, "CarVcuManager.CarVcuEventCallback onErrorEvent");
            }
        };
        this.mCarEspEventCallback = new CarEspManager.CarEspEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartAvasService.4
            public void onChangeEvent(CarPropertyValue val) {
                if (val.getPropertyId() == 557851651) {
                    SmartAvasService.this.handleCarAvhStatusChanged(((Integer) BaseCarListener.getValue(val)).intValue());
                }
            }

            public void onErrorEvent(int i, int i1) {
            }
        };
        addEspManagerListener(this.mCarEspEventCallback);
        addVcuManagerListener(this.mCarVcuEventCallback);
        addMcuManagerListener(this.mCarMcuEventCallback);
        registerReceiver();
        registerContentReceiver();
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        LogUtil.d(this.TAG, "onCarManagerInited");
        this.mCarMcuManager = getCarManager("xp_mcu");
        this.mCarAvasManager = getCarManager("xp_avas");
        this.mCarVcuManager = getCarManager("xp_vcu");
        try {
            boolean z = true;
            if (this.mCarMcuManager.getIgStatusFromMcu() != 1) {
                z = false;
            }
            ig_status_on = z;
            this.curGear = this.mCarVcuManager.getStallState();
            String str = this.TAG;
            LogUtil.i(str, "curGear=" + this.curGear);
        } catch (Exception e) {
            LogUtil.e(this.TAG, "initCarListener error");
        }
    }

    private void registerContentReceiver() {
        LogUtil.d(this.TAG, "##registerContentReceiver");
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor(STR_LLU_ENABLE), true, new ContentObserver(XuiWorkHandler.getInstance()) { // from class: com.xiaopeng.xuiservice.smart.SmartAvasService.5
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                boolean unused = SmartAvasService.lluEnable = Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), SmartAvasService.STR_LLU_ENABLE, 1) == 1;
                String str = SmartAvasService.this.TAG;
                LogUtil.d(str, "langLightEffectEnable change to " + SmartAvasService.lluEnable);
            }
        });
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor(STR_SAYHI_ENABLE), true, new ContentObserver(XuiWorkHandler.getInstance()) { // from class: com.xiaopeng.xuiservice.smart.SmartAvasService.6
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                boolean unused = SmartAvasService.sayhiEnable = Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), SmartAvasService.STR_SAYHI_ENABLE, 0) == 1;
                String str = SmartAvasService.this.TAG;
                LogUtil.d(str, "isSayHiEnable change to " + SmartAvasService.sayhiEnable);
            }
        });
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor(STR_GEAR_AVH_ENABLE), true, new ContentObserver(XuiWorkHandler.getInstance()) { // from class: com.xiaopeng.xuiservice.smart.SmartAvasService.7
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                boolean unused = SmartAvasService.avasSayhiEnable = SmartAvasService.access$700();
                String str = SmartAvasService.this.TAG;
                LogUtil.d(str, "avasSayhiEnable change to " + SmartAvasService.avasSayhiEnable);
            }
        });
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        unregisterReceiver();
    }

    public static SmartAvasService getInstance() {
        return InstanceHolder.sService;
    }

    private static boolean getSayHiAvasEnable() {
        return avasSayhiEnableDefault && Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), STR_GEAR_AVH_ENABLE, avasSayhiEnableDefault ? 1 : 0) == 1;
    }

    public int getSayHiEffect() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "SayHiAvasType", 1);
    }

    public void setSayHiEffect(int type) {
        try {
            this.mCarAvasManager.setAvasFriendlySayHiSound(type);
        } catch (Exception e) {
            handleException(e);
        }
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "SayHiAvasType", type);
    }

    private void registerReceiver() {
        LogUtil.i(this.TAG, "registerReceiver");
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("com.xiaopeng.intent.action.xkey");
        this.mContext.registerReceiver(this.mReceiver, mIntentFilter);
    }

    public void unregisterReceiver() {
        LogUtil.i(this.TAG, "unregisterReceiver.");
        this.mContext.unregisterReceiver(this.mReceiver);
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final SmartAvasService sService = new SmartAvasService(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCarAvhStatusChanged(int avhStatus) {
        String str = this.TAG;
        LogUtil.i(str, "handleCarAvhStatusChanged " + avhStatus);
        if (this.curAvhStatus != avhStatus) {
            if (avhStatus == 1 && this.curGear == 1 && checkAvasAutoPlayStatus()) {
                playAvasSayHiSound();
            } else if (avhStatus == 2) {
            }
            this.curAvhStatus = avhStatus;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCarGearLevelChanged(int level) {
        if (this.curGear != level) {
            this.curGear = level;
            if (level == 4 && checkAvasAutoPlayStatus()) {
                playAvasSayHiSound();
            }
        }
    }

    public void playAvasSayHiSound() {
        try {
            LogUtil.i(this.TAG, "playAvasSayHiSound " + getSayHiEffect());
            if (XUIConfig.getAvasSupportType() == 0) {
                this.mCarAvasManager.setAvasExternalSound(getSayHiEffect() + 4);
                CarAvasManager carAvasManager = this.mCarAvasManager;
                CarAvasManager carAvasManager2 = this.mCarAvasManager;
                carAvasManager.setExternalSoundModeCmd(1);
            } else if (this.mAudioManager != null) {
                String name = this.SayHiName + getSayHiEffect();
                LogUtil.log(1, this.TAG, "playSound /system/media/audio/xiaopeng/cdu/wav/" + name + ".wav");
                this.mAudioManager.playAvasSound(16, "/system/media/audio/xiaopeng/cdu/wav/" + name + ".wav");
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    private boolean checkAvasAutoPlayStatus() {
        String str = this.TAG;
        LogUtil.d(str, "checkAvasAutoPlayStatus " + lluEnable + " " + sayhiEnable + " " + ig_status_on);
        return XUIConfig.hasFeature(XUIConfig.PROPERTY_LLU) && lluEnable && sayhiEnable && ig_status_on && avasSayhiEnable;
    }
}
