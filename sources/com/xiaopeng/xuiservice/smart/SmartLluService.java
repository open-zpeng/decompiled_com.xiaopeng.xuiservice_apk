package com.xiaopeng.xuiservice.smart;

import android.annotation.SuppressLint;
import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.bcm.CarBcmManager;
import android.car.hardware.esp.CarEspManager;
import android.car.hardware.llu.CarLluManager;
import android.car.hardware.llu.LluScriptParameter;
import android.car.hardware.mcu.CarMcuManager;
import android.car.hardware.vcu.CarVcuManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioConfig.AudioConfig;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.SparseArray;
import androidx.core.view.MotionEventCompat;
import com.google.gson.Gson;
import com.xiaopeng.speech.protocol.event.OOBEEvent;
import com.xiaopeng.xuimanager.XUIServiceNotConnectedException;
import com.xiaopeng.xuimanager.mediacenter.MediaCenterManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.bluetooth.NfDef;
import com.xiaopeng.xuiservice.capabilities.XpPictureNotification;
import com.xiaopeng.xuiservice.carcontrol.BaseCarListener;
import com.xiaopeng.xuiservice.cv.SmartAvmManager;
import com.xiaopeng.xuiservice.utils.CarControlUtils;
import com.xiaopeng.xuiservice.utils.CommonUtils;
import com.xiaopeng.xuiservice.utils.FileUtils;
import com.xiaopeng.xuiservice.utils.SoundPlayHelper;
import com.xiaopeng.xuiservice.utils.ToastUtil;
import com.xiaopeng.xuiservice.utils.UiHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONObject;
import org.opencv.videoio.Videoio;
/* loaded from: classes5.dex */
public class SmartLluService extends BaseSmartService {
    private static final boolean DBG = true;
    private static final int ErrorCode_Common = -4;
    private static final int ErrorCode_DanceError = -4096;
    private static final int ErrorCode_Forbidden = -1;
    private static final int ErrorCode_NoFocus = -2;
    private static final int LIGHT_EFFECT_ACCHARGE = 6;
    private static final int LIGHT_EFFECT_AWAKE = 2;
    private static final int LIGHT_EFFECT_DCCHARGE = 7;
    private static final int LIGHT_EFFECT_FINDCAR = 1;
    private static final int LIGHT_EFFECT_SHOWOFF = 10;
    private static final int LIGHT_EFFECT_SLEEP = 5;
    private static final int LIGHT_EFFECT_TAKEPHOTO = 9;
    private static final int LLU_EFFECT_CLOSE = 0;
    private static final int LLU_EFFECT_MODE_A = 1;
    private static final int LLU_EFFECT_MODE_B = 2;
    private static final int LLU_EFFECT_MODE_C = 3;
    private static final int LLU_MODE1 = 1;
    private static final int LLU_MODE2 = 2;
    private static final int LLU_MODE3 = 3;
    static int LluPrivatePrintCount = 0;
    private static final int LluShowError = 4;
    private static final int LluShowFinish = 3;
    private static final int LluShowStart = 1;
    private static final int LluShowStop = 2;
    private static final int MSG_PLAY_LIGHT_DANCE = 2;
    private static final int MSG_PLAY_LLU_EFFECT_MODE = 1;
    private static final int MSG_START_LLU_EFFECT = 3;
    private static final int MSG_STOP_LLU_EFFECT = 5;
    private static final int MSG_STOP_LLU_EFFECT_NOREMOVE = 6;
    private static final int MSG_STOP_LLU_SPECIAL_MODE = 7;
    private static final int MSG_TURNOFF_AND_START_AIMODE = 4;
    private static final int MUSIC_SPECRHYTHM_INTERVAL_TIME = 1000;
    private static final String TAG = "SmartLluService";
    private static final String Type_Dance = "dance";
    private static final String Type_LightDance = "lightanddance";
    private static final String XP_XKEY_INTENT = "com.xiaopeng.intent.action.xkey";
    private static final String X_SAYHI_LLU = "android_xsayhi";
    private static LluAiListener mLluAiListener;
    static int mirrorCmdLast;
    private static MediaPlayer sPlayer;
    static int scissorsLgateCmdLast;
    static int scissorsRgateCmdLast;
    static int windowCmdLast;
    static boolean xpExpandControl;
    private final String DEFAULT_LDANCE_NAME;
    private int DownVolume;
    private int LIGHT_DANCE_INTERVAL;
    private boolean LluCduSelfCtlMode;
    private final String PERSONAL_LLU_PATH;
    private int RestoreVolume;
    private final List<String> SHOW_OFF_MODE_01;
    private final String SYSTEM_DANCE_MEIDA_PATH;
    private final String SYSTEM_LLU_PATH;
    private final String SYSTEM_LOCAL_DANCE_INFO_PATH;
    private final String USER_DANCE_MEIDA_PATH;
    private boolean brokenFftLlu;
    private int curAvhStatus;
    private String curDanceEffect;
    private int curGear;
    private String currentEffectName;
    private String currentEffectType;
    private int currentMcuEffectMode;
    private int currentMcuEffectName;
    private int danceLoop;
    private int[] fData;
    private int[] frontData;
    private boolean frontDataFinish;
    private int frontIndex;
    private int frontIndexNum;
    private boolean isDriving;
    private boolean isInSpecialMode;
    private boolean isLLUEnable;
    private boolean isLLUFftEnable;
    private volatile boolean isPGear;
    private boolean isRearFogOn;
    private boolean isSayHi;
    private boolean isWaitingForSayHi;
    private LangLightEffect langLightEffect;
    private int lightLoop;
    private Runnable llu_stop_timeout_thread;
    private Runnable llu_timeout_thread;
    private AiLluAudioTable mAiLluAudioTable;
    private AudioConfig mAudioConfig;
    AudioManager.OnAudioFocusChangeListener mAudioFocusListener;
    private AudioManager mAudioManager;
    private CarBcmManager.CarBcmEventCallback mCarBcmEventCallback;
    private CarBcmManager mCarBcmManager;
    private CarEspManager.CarEspEventCallback mCarEspEventCallback;
    private CarLluManager.CarLluEventCallback mCarLluEventCallback;
    private CarLluManager mCarLluManager;
    private CarMcuManager.CarMcuEventCallback mCarMcuEventCallback;
    private CarMcuManager mCarMcuManager;
    private CarVcuManager.CarVcuEventCallback mCarVcuEventCallback;
    private CarVcuManager mCarVcuManager;
    private boolean mDancePlaying;
    private ArrayList<String> mDownLangLightEffectNameList;
    private ArrayList<LangLightEffect> mLangLightEffectList;
    private ArrayList<String> mLangLightEffectNameList;
    private LluHandlerThread mLluHandlerThread;
    private MediaCenterManager mMediaCenterManager;
    private boolean mPreDance;
    private final BroadcastReceiver mReceiver;
    private String mSayHiEffect;
    private boolean mSayHiEnable;
    private int mSayHiMode;
    private SmartAvmManager mSmartAvmManager;
    private SmartLluServiceListener mSmartLluServiceListener;
    private SoundPlayHelper mSoundPlayHelper;
    private MediaCenterManager.VisualCaptureListener mVisualCaptureListener;
    private LangLightEffect mcuLightEffect;
    private boolean mpPrepareAsync;
    private LangLightEffect musicLightEffect;
    private long musicSpecrhythmTimeStamp;
    private int musicSpectrumCount;
    private int musicSpectrumDelayTime;
    private boolean musicSpectrumIntrupt;
    private boolean musicSpectrumLampIntrupt;
    private boolean musicSpectrumPause;
    private int musicStartTick;
    private int musicStopTick;
    private int musicTable;
    private int[] rData;
    private int[] rearData;
    private int rearIndex;
    private int rearIndexNum;
    private Timer timer;
    private TimerTask timerTask;
    private String triggerEffectName;
    private String triggerEffectType;
    private boolean trrigerXuiSayHi;
    private static String SendingLluEffectName = null;
    private static String SendingLluEffectType = null;
    private static Object timerLock = new Object();
    private static boolean ig_status_on = true;
    private static boolean llu_timeout_triggered_in_loop = false;
    private static boolean finishcallback_triggered = false;
    private static long last_startLanglight_time = 0;
    public static String mHardwareType = "";
    public static int INFINITE_FRONTLIGHT_COUNT = 0;
    public static int INFINITE_REARLIGHT_COUNT = 0;
    static long printLogTime = 0;
    private static int AiLluMode = 0;
    private static int PrevTick = 0;
    private static final int[] fTickTable = {3, 8, 13, 18, 24, 30, 17, 43, 49, 55, 60, 65, 70};
    private static Object lluCallbackLock = new Object();
    private static long last_turnLamp_time = 0;
    static int fLightLast = -1;
    static int rfLightLast = -1;

    /* loaded from: classes5.dex */
    public interface LluAiListener {
        void onAiLluEvent(int i, int i2);
    }

    /* loaded from: classes5.dex */
    public interface SmartLluServiceListener {
        void onLightEffectFinishEvent(int i, int i2);

        void onLightEffectShowError(String str, int i);

        void onLightEffectShowFinishEvent(String str, String str2);

        void onLightEffectShowStartEvent(String str, String str2);

        void onLightEffectShowStopEvent(String str, String str2);
    }

    static /* synthetic */ int access$1910(SmartLluService x0) {
        int i = x0.danceLoop;
        x0.danceLoop = i - 1;
        return i;
    }

    static /* synthetic */ int access$3810(SmartLluService x0) {
        int i = x0.lightLoop;
        x0.lightLoop = i - 1;
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

    static {
        boolean z = true;
        if (SystemProperties.getInt("persist.sys.xiaopeng.xui.ExpandControl", 0) != 1) {
            z = false;
        }
        xpExpandControl = z;
        windowCmdLast = -1;
        scissorsLgateCmdLast = -1;
        scissorsRgateCmdLast = -1;
        mirrorCmdLast = -1;
        LluPrivatePrintCount = 0;
    }

    private SmartLluService(Context context) {
        super(context);
        this.isLLUEnable = true;
        this.isLLUFftEnable = false;
        this.brokenFftLlu = false;
        this.isPGear = false;
        this.curGear = 0;
        this.curAvhStatus = 0;
        this.isDriving = false;
        this.isRearFogOn = false;
        this.currentEffectName = null;
        this.currentEffectType = null;
        this.triggerEffectName = null;
        this.triggerEffectType = null;
        this.frontDataFinish = false;
        this.currentMcuEffectName = -1;
        this.currentMcuEffectMode = -1;
        this.frontIndex = 0;
        this.frontIndexNum = 0;
        this.rearIndex = 0;
        this.rearIndexNum = 0;
        this.musicSpectrumPause = true;
        this.musicSpectrumIntrupt = false;
        this.musicSpectrumLampIntrupt = false;
        this.musicSpecrhythmTimeStamp = 0L;
        this.musicTable = 12;
        this.musicStartTick = 1;
        this.musicStopTick = 200;
        this.musicSpectrumDelayTime = 1;
        this.musicSpectrumCount = 0;
        this.danceLoop = 0;
        this.lightLoop = 0;
        this.curDanceEffect = null;
        this.mDancePlaying = false;
        this.mPreDance = false;
        this.mpPrepareAsync = true;
        this.LIGHT_DANCE_INTERVAL = 2;
        this.DEFAULT_LDANCE_NAME = "PianoConcerto";
        this.PERSONAL_LLU_PATH = "/data/xuiservice/llu";
        this.SYSTEM_LLU_PATH = "/system/etc/xuiservice/llu";
        this.USER_DANCE_MEIDA_PATH = "/data/xuiservice/dance/";
        this.SYSTEM_DANCE_MEIDA_PATH = "/system/etc/xuiservice/dance/";
        this.SYSTEM_LOCAL_DANCE_INFO_PATH = "/system/etc/xuiservice/dance/LocalDance_Info.json";
        this.LluCduSelfCtlMode = false;
        this.trrigerXuiSayHi = SystemProperties.getBoolean("persist.sys.support.sayhi", true);
        this.mSayHiEnable = false;
        this.isSayHi = false;
        this.isWaitingForSayHi = false;
        this.mSayHiMode = 1;
        this.SHOW_OFF_MODE_01 = Arrays.asList("notactive", "android_avh_sayhi_01", "android_avh_sayhi_02", "android_avh_sayhi_03");
        this.mSayHiEffect = this.SHOW_OFF_MODE_01.get(this.mSayHiMode);
        this.mAudioConfig = null;
        this.mLluHandlerThread = new LluHandlerThread();
        this.mReceiver = new BroadcastReceiver() { // from class: com.xiaopeng.xuiservice.smart.SmartLluService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                LogUtil.i(SmartLluService.TAG, "SmartLluService mReceiver " + intent.getAction() + "  keytype:" + intent.getStringExtra("keytype") + "  keyfunc:" + intent.getIntExtra("keyfunc", 0));
                if (action.equals("com.xiaopeng.intent.action.xkey") && intent.getStringExtra("keytype").equals("short") && intent.getIntExtra("keyfunc", 0) == 2) {
                    try {
                        SmartLluService.this.handleSayHiBroadcast();
                    } catch (Exception e) {
                        SmartLluService.this.handleException(e);
                    }
                }
            }
        };
        this.RestoreVolume = -1;
        this.DownVolume = -1;
        this.mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() { // from class: com.xiaopeng.xuiservice.smart.SmartLluService.10
            @Override // android.media.AudioManager.OnAudioFocusChangeListener
            public void onAudioFocusChange(int focusChange) {
                boolean volumeAdjustPolicy = SystemProperties.getBoolean("persist.sys.llu.focuspolicy", true);
                if (!volumeAdjustPolicy) {
                    SmartLluService.this.stopLightDancing();
                    return;
                }
                if (focusChange != -3) {
                    if (focusChange != -2 && focusChange != -1) {
                        if (focusChange == 1 || focusChange == 2 || focusChange != 3) {
                            return;
                        }
                    } else {
                        SmartLluService.this.stopLightDancing();
                        return;
                    }
                }
                SmartLluService.this.VolumeDownAndRestore(focusChange <= 0);
            }
        };
        this.mAiLluAudioTable = new AiLluAudioTable();
        this.mSoundPlayHelper = new SoundPlayHelper(this.mContext);
        this.llu_timeout_thread = new Runnable() { // from class: com.xiaopeng.xuiservice.smart.SmartLluService.13
            @Override // java.lang.Runnable
            public void run() {
                LogUtil.i(SmartLluService.TAG, "llu_timeout_thread");
                SmartLluService.this.musicSpectrumIntrupt = false;
                if (SmartLluService.this.brokenFftLlu) {
                    SmartLluService.this.brokenFftLlu = false;
                }
                SmartLluService.this.setLluToInitMode();
                SmartLluService.this.resetLastLightFlag();
                if (SmartLluService.this.lightLoop != 0 || SmartLluService.this.danceLoop != 0) {
                    boolean unused = SmartLluService.llu_timeout_triggered_in_loop = true;
                    LogUtil.i(SmartLluService.TAG, "llu_timeout_thread " + SmartLluService.this.lightLoop + " " + SmartLluService.this.danceLoop);
                    return;
                }
                SmartLluService.this.turnOffAndRestoreLight(3, false);
                if (SmartLluService.this.mSmartLluServiceListener != null) {
                    boolean unused2 = SmartLluService.finishcallback_triggered = true;
                    SmartLluService smartLluService = SmartLluService.this;
                    smartLluService.callBackLluShowStatusToClient(3, smartLluService.currentEffectName, SmartLluService.this.currentEffectType, 0);
                }
            }
        };
        this.llu_stop_timeout_thread = new Runnable() { // from class: com.xiaopeng.xuiservice.smart.SmartLluService.14
            @Override // java.lang.Runnable
            public void run() {
                LogUtil.i(SmartLluService.TAG, "llu_stop_timeout_thread");
                if (SmartLluService.this.currentEffectName == null) {
                    SmartLluService.this.setLluToInitMode();
                    SmartLluService.this.deactivateAndroidLluControl();
                }
            }
        };
        this.isInSpecialMode = false;
        this.fData = new int[60];
        this.rData = new int[60];
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
        synchronized (timerLock) {
            this.timer = new Timer();
        }
        this.mAudioConfig = new AudioConfig(context);
        initConfig();
        this.isLLUEnable = getLangLightEffectEnable();
        setLangLightMusicEffect(getLangLightMusicEffect());
        if (XUIConfig.isAiLluEnable() && XUIConfig.hasFeature(XUIConfig.PROPERTY_AVM)) {
            this.mSmartAvmManager = SmartAvmManager.getInstance();
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
        this.mLluHandlerThread.start();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    public void initXUIManager() throws XUIServiceNotConnectedException {
        this.mMediaCenterManager = (MediaCenterManager) getXuiManager().getXUIServiceManager(XUIConfig.PROPERTY_MEDIACENTER);
        this.mVisualCaptureListener = new MediaCenterManager.VisualCaptureListener() { // from class: com.xiaopeng.xuiservice.smart.SmartLluService.2
            public void OnFftDataCapture(byte[] bytes, int i) {
            }

            public void OnRatioData(float ratio, float minRatio) {
                SmartLluService.this.handleFftData(ratio, minRatio);
            }
        };
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void initCarListener() {
        this.mCarLluEventCallback = new CarLluManager.CarLluEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartLluService.3
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557854209) {
                    LogUtil.i(SmartLluService.TAG, "CarLluManager.CarLluEventCallback onChangeEvent : " + value.toString());
                    SmartLluService.this.handleLluSw(((Integer) BaseCarListener.getValue(value)).intValue());
                } else if (value.getPropertyId() == 557854229) {
                    LogUtil.i(SmartLluService.TAG, "CarLluManager.CarLluEventCallback onChangeEvent : " + value.toString());
                    SmartLluService.this.handleLluScriptSt(((Integer) BaseCarListener.getValue(value)).intValue());
                } else if (value.getPropertyId() == 557854217) {
                    LogUtil.i(SmartLluService.TAG, "CarLluManager.CarLluEventCallback onChangeEvent ID_LLU_FUNCTIONST: " + value.toString());
                    SmartLluService.this.handleLluFunction(((Integer) BaseCarListener.getValue(value)).intValue());
                } else if (value.getPropertyId() == 557847627) {
                    LogUtil.i(SmartLluService.TAG, "CarLluManager.CarLluEventCallback onChangeEvent MCU_LLU_SHOWOFF_SW: " + value.toString());
                    SmartLluService.this.handleMcuLluShowOffSw(((Integer) BaseCarListener.getValue(value)).intValue());
                } else if (value.getPropertyId() == 557847625) {
                    LogUtil.i(SmartLluService.TAG, "CarLluManager.CarLluEventCallback onChangeEvent ID_MCU_LLU_SW: " + value.toString());
                    SmartLluService.this.handleMcuLluSw(((Integer) BaseCarListener.getValue(value)).intValue());
                } else if (value.getPropertyId() == 557847631) {
                    LogUtil.d(SmartLluService.TAG, "Execute case ID_LLU_CHARGING_SW");
                    SmartLluService.this.handleMcuLluChargingSw(value.getPropertyId());
                } else if (value.getPropertyId() == 557847645) {
                    SmartLluService.this.handleMcuFbLluActive(((Integer) BaseCarListener.getValue(value)).intValue());
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
                LogUtil.e(SmartLluService.TAG, "CarLluManager.CarLluEventCallback onErrorEvent");
            }
        };
        this.mCarVcuEventCallback = new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartLluService.4
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557847045) {
                    LogUtil.i(SmartLluService.TAG, "CarVcuManager.CarVcuEventCallback onChangeEvent : " + value.toString());
                    SmartLluService.this.handleCarGearLevelChanged(((Integer) value.getValue()).intValue());
                } else if (value.getPropertyId() == 559944229) {
                    SmartLluService.this.handleCarSpeed(((Float) value.getValue()).floatValue());
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCarBcmEventCallback = new CarBcmManager.CarBcmEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartLluService.5
            public void onChangeEvent(CarPropertyValue value) {
                switch (value.getPropertyId()) {
                    case 557849602:
                        SmartLluService.this.handleRearFogLampChange(((Integer) BaseCarListener.getValue(value)).intValue());
                        return;
                    case 557849623:
                        SmartLluService.this.handleLRTurnlamp();
                        return;
                    case 557849624:
                        SmartLluService.this.handleLRTurnlamp();
                        return;
                    case 557849640:
                        LogUtil.i(SmartLluService.TAG, "CarBcmManager.CarBcmEventCallback onChangeEvent : " + value.toString());
                        SmartLluService.this.handleFrontLightGroupChange(((Integer) BaseCarListener.getValue(value)).intValue());
                        return;
                    default:
                        return;
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
                LogUtil.e(SmartLluService.TAG, "CarBcmManager.CarBcmEventCallback onErrorEvent");
            }
        };
        this.mCarMcuEventCallback = new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartLluService.6
            public void onChangeEvent(CarPropertyValue val) {
                if (val.getPropertyId() == 557847561) {
                    LogUtil.i(SmartLluService.TAG, "CarMcuManager.CarMcuEventCallback onChangeEvent : " + val.toString());
                    SmartLluService.this.handleIgStatusChange(((Integer) BaseCarListener.getValue(val)).intValue());
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
                LogUtil.e(SmartLluService.TAG, "CarMcuManager.CarMcuEventCallback onErrorEvent");
            }
        };
        this.mCarEspEventCallback = new CarEspManager.CarEspEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartLluService.7
            public void onChangeEvent(CarPropertyValue val) {
                if (val.getPropertyId() == 557851651) {
                    SmartLluService.this.handleCarAvhStatusChanged(((Integer) BaseCarListener.getValue(val)).intValue());
                }
            }

            public void onErrorEvent(int i, int i1) {
            }
        };
        addLluManagerListener(this.mCarLluEventCallback);
        addVcuManagerListener(this.mCarVcuEventCallback);
        addBcmManagerListener(this.mCarBcmEventCallback);
        addMcuManagerListener(this.mCarMcuEventCallback);
        addEspManagerListener(this.mCarEspEventCallback);
        setSayHiEnable(getSayHiEnable());
        registerReceiver();
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        LogUtil.d(TAG, "onCarManagerInited");
        this.mCarLluManager = getCarManager("xp_llu");
        this.mCarVcuManager = getCarManager("xp_vcu");
        this.mCarBcmManager = getCarManager("xp_bcm");
        this.mCarMcuManager = getCarManager("xp_mcu");
        try {
            ig_status_on = this.mCarMcuManager.getIgStatusFromMcu() == 1;
            this.curGear = this.mCarVcuManager.getStallState();
            this.isPGear = this.curGear == 4;
            this.mCarBcmManager.setDayLightMode(1);
            this.isRearFogOn = this.mCarBcmManager.getRearFogLamp() == 1;
        } catch (Exception e) {
            LogUtil.e(TAG, "initCarListener error");
        }
    }

    private void registerReceiver() {
        LogUtil.i(TAG, "registerReceiver");
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("com.xiaopeng.intent.action.xkey");
        this.mContext.registerReceiver(this.mReceiver, mIntentFilter);
    }

    public void unregisterReceiver() {
        LogUtil.i(TAG, "unregisterReceiver.");
        this.mContext.unregisterReceiver(this.mReceiver);
    }

    public void handleSayHiBroadcast() {
        try {
            if (this.mCarLluManager != null) {
                LogUtil.i(TAG, "handleSayHiBroadcast");
                if (!this.isLLUEnable) {
                    LogUtil.i(TAG, "do not start sayhi lluEnable off");
                    ToastUtil.showToast(this.mContext, (int) R.string.llu_toast_disabled, 1);
                    return;
                }
                if (Math.abs(System.currentTimeMillis() - last_turnLamp_time) >= 1500 && !this.isRearFogOn && !this.isDriving) {
                    if (!ig_status_on) {
                        LogUtil.i(TAG, "do not start sayhi " + this.isDriving + " " + this.mSayHiEnable + " " + ig_status_on + " " + this.isPGear + " " + this.isRearFogOn);
                        return;
                    }
                    int lluStatus = this.mCarLluManager.getMcuLluEnableStatus();
                    if (lluStatus == 1) {
                        LogUtil.d(TAG, "handleSayHiBroadcast mPreDance=" + this.mPreDance);
                        if (isLightDancing()) {
                            doStopLDance();
                            this.danceLoop = 0;
                        } else if (this.mPreDance && this.triggerEffectName != null) {
                            callBackLluShowStatusToClient(2, this.triggerEffectName, this.triggerEffectType, 0);
                        }
                        if (isSayHing()) {
                            this.isSayHi = false;
                        }
                        if (this.currentEffectName != null) {
                            callBackLluShowStatusToClient(2, this.currentEffectName, this.currentEffectType, 0);
                        }
                        if (this.mLluHandlerThread.getHandler().hasMessages(6)) {
                            this.mLluHandlerThread.getHandler().removeMessages(6);
                            sendEventMessage(6, 0, 0, "", Videoio.CAP_PVAPI);
                        }
                        turnOffAndRestoreLight(2, true);
                        Thread.sleep(150L);
                        startLangLightEffectShow(X_SAYHI_LLU, this.mSayHiMode, 1, false);
                        ToastUtil.showToast(this.mContext, (int) R.string.llu_toast_saihi_playing, 1);
                        return;
                    }
                    return;
                }
                ToastUtil.showToast(this.mContext, (int) R.string.llu_toast_gear_limit, 1);
                LogUtil.i(TAG, "turning lamp on do not start sayhi");
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    public static SmartLluService getInstance() {
        return InstanceHolder.sService;
    }

    public void startLangLightEffectShow(String effectName, int loop) {
        xpExpandControl = SystemProperties.getInt("persist.sys.xiaopeng.xui.ExpandControl", 0) == 1;
        if (isSayHing()) {
            this.isSayHi = false;
        }
        if (isDanceEffect(effectName)) {
            startLightDancing(effectName, -1);
        } else if (isSayhiEffect(effectName)) {
            startSayHi();
        } else if (!this.isLLUEnable || this.isRearFogOn) {
        } else {
            if (!xpExpandControl && !this.isPGear) {
                return;
            }
            turnOffLightAndStartEffect(effectName, loop, !xpExpandControl);
        }
    }

    public void startLangLightEffectShow(String effectName, int mode, int loop, boolean needPGearLimit) {
        StringBuilder sb;
        String str;
        if (mode == 0) {
            LogUtil.w(TAG, "startLangLightEffectShow LLU_EFFECT_CLOSE  NO NEED SHOW");
            return;
        }
        if (mode >= 10) {
            sb = new StringBuilder();
            sb.append(effectName);
            str = "_";
        } else {
            sb = new StringBuilder();
            sb.append(effectName);
            str = "_0";
        }
        sb.append(str);
        sb.append(mode);
        String realEffectName = sb.toString();
        LogUtil.d(TAG, "startLangLightEffectShow  realEffectName:" + realEffectName);
        doStartLangLightEffectShow(realEffectName, loop, needPGearLimit);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.xiaopeng.xuiservice.smart.SmartLluService$8] */
    private void turnOffLightAndStartEffect(final String effectName, final int loop, final boolean needPGearLimit) {
        new Thread() { // from class: com.xiaopeng.xuiservice.smart.SmartLluService.8
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    LangLightEffect LLEffect = SmartLluService.this.getLangLightEffectByName(effectName);
                    if (LLEffect.getNotResetFlag() != 1) {
                        SmartLluService.this.resetPirvateLluControl();
                        Thread.sleep(200L);
                    }
                    SmartLluService.this.turnOffAndRestoreLight(3, true);
                    Thread.sleep(250L);
                    SmartLluService.this.doStartLangLightEffectShow(effectName, loop, needPGearLimit);
                } catch (Exception e) {
                    LogUtil.e(SmartLluService.TAG, "turnOffLightAndSetEffect " + e);
                }
            }
        }.start();
    }

    private void printLogInterval(String log, int count, int num) {
        long curTime = System.currentTimeMillis();
        if (Math.abs(curTime - printLogTime) > CommonUtils.xuiLogInterval) {
            LogUtil.d(TAG, log);
            if (num >= count) {
                printLogTime = curTime;
            }
        }
    }

    public void setPause(boolean pause) {
        this.musicSpectrumPause = pause;
    }

    public boolean getFftLLUEnable() {
        return this.isLLUFftEnable;
    }

    public void setFftLLUEnable(boolean enable) {
        LogUtil.d(TAG, "setFftLLUEnable " + enable);
        try {
            if (this.mMediaCenterManager != null && this.mVisualCaptureListener != null) {
                if (enable) {
                    this.mMediaCenterManager.registerVisualizerListener(this.mVisualCaptureListener);
                } else {
                    this.mMediaCenterManager.unRegisterVisualizerListener(this.mVisualCaptureListener);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.isLLUFftEnable = enable;
    }

    public void updateEffectFiles() {
        parseAllLightEffectFromJsons();
    }

    public void updateEffectFiles(String llu_file) {
        loadLightEffectFromFile(llu_file);
    }

    public int getLluWakeWaitMode() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "lluWakeWaitMode", 0);
    }

    public void setLluWakeWaitMode(int type) {
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "lluWakeWaitMode", type);
    }

    public int getLluSleepMode() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "lluSleepMode", 0);
    }

    public void setLluSleepMode(int type) {
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "lluSleepMode", type);
    }

    private void saveLampOffstatus(boolean turnOff) {
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isLluTriggerLampOff", turnOff ? 1 : 0);
    }

    private boolean getLampOffstatus() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isLluTriggerLampOff", 0) == 1;
    }

    public void setLangLightEffectEnable(boolean enable) {
        LogUtil.i(TAG, "setLangLightEffectEnable " + enable);
        this.isLLUEnable = enable;
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "langLightEffectEnable", enable ? 1 : 0);
        if (!enable) {
            stopLightEffectShow();
            stopSayHi();
        }
    }

    public boolean getLangLightEffectEnable() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "langLightEffectEnable", 1) == 1;
    }

    public String getLangLightMusicEffect() {
        if (Settings.System.getString(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "LangLightMusicEffect") == null) {
            return "expomode_music1";
        }
        return Settings.System.getString(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "LangLightMusicEffect");
    }

    public void setLangLightMusicEffect(String effectName) {
        LangLightEffect lightEffect = getLangLightEffectByName(effectName);
        if (lightEffect != null) {
            LogUtil.i(TAG, "setLangLightMusicEffect effectName:" + effectName);
            this.musicLightEffect = lightEffect;
            Settings.System.putString(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "LangLightMusicEffect", effectName);
        }
    }

    public void setMusicSpectrumToLangLight(int tickNum) {
        if (this.musicSpecrhythmTimeStamp < System.currentTimeMillis() - 1000) {
            updateTimer(true);
            resetPirvateLluControl();
        }
        this.musicSpecrhythmTimeStamp = System.currentTimeMillis();
        if (!this.musicSpectrumIntrupt && !this.musicSpectrumPause) {
            tickLluStopTime();
            if (this.musicSpectrumCount % this.musicSpectrumDelayTime == 0) {
                int i = this.musicTable;
                setLluPrivateCtrl(true, i, tickNum, i, tickNum, -1, -1);
            }
            this.musicSpectrumCount++;
            if (this.musicSpectrumCount > 32767) {
                this.musicSpectrumCount = 0;
            }
        }
    }

    public void setMusicSpectrumToLangLight(float ratio) {
        if (!this.musicSpectrumIntrupt && !this.musicSpectrumPause && this.musicLightEffect != null) {
            doMusicSpectrumToLangLight(ratio);
        }
    }

    public void setMusicTableForDebug(int musicTable) {
        this.musicTable = musicTable;
    }

    public void setMusicStartTickNumForDebug(int tickNum) {
        this.musicStartTick = tickNum;
    }

    public void setMusicStopTickNumForDebug(int tickNum) {
        this.musicStopTick = tickNum;
    }

    public void setMusicDelayTimeForDebug(int delayTime) {
        this.musicSpectrumDelayTime = delayTime;
    }

    public String getLangLightEffectList(int effectType) {
        if (effectType == 1) {
            return getAllLightDanceEffect();
        }
        return getAllLightEffect();
    }

    public void stopLightEffectShow() {
        long currenttime = System.currentTimeMillis();
        if (Math.abs(currenttime - last_startLanglight_time) < 1500) {
            LogUtil.i(TAG, "stopLightEffectShow() timeGap:" + Math.abs(currenttime - last_startLanglight_time));
            sendEventMessage(6, 0, 0, "", 1500 - ((int) Math.abs(currenttime - last_startLanglight_time)));
            return;
        }
        doStopLightEffectShow();
    }

    public boolean getSayHiEnable() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isSayHiEnable", 0) == 1;
    }

    public void setSayHiEnable(boolean enable) {
        try {
            if (this.mCarLluManager != null) {
                int i = 0;
                boolean mcuEnable = this.trrigerXuiSayHi ? false : enable ? 1 : 0;
                CarLluManager carLluManager = this.mCarLluManager;
                if (mcuEnable) {
                    CarLluManager carLluManager2 = this.mCarLluManager;
                    i = 1;
                } else {
                    CarLluManager carLluManager3 = this.mCarLluManager;
                }
                carLluManager.setMcuLluShowOffSwitch(i);
            }
        } catch (Exception e) {
            handleException(e);
        }
        if (this.trrigerXuiSayHi) {
            this.mSayHiEnable = enable;
        }
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isSayHiEnable", enable ? 1 : 0);
        if (!enable && isSayHing()) {
            stopSayHi();
        }
    }

    public void sendLluScriptData(int index, int pos, int length, int[] data) {
        try {
            if (this.mCarLluManager != null) {
                this.mCarLluManager.sendLluScriptData(index, pos, length, data);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void setLluScriptStRequest(int request) {
        try {
            if (this.mCarLluManager != null) {
                this.mCarLluManager.setLluScriptStRequest(request);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCarAvhStatusChanged(int avhStatus) {
        LogUtil.i(TAG, "handleCarAvhStatusChanged " + avhStatus);
        if (this.curAvhStatus != avhStatus) {
            if (avhStatus == 1 && this.curGear == 1) {
                startSayHi();
            } else if (avhStatus == 2) {
                stopSayHi();
            } else if (avhStatus == 0) {
                stopSayHi();
            }
            this.curAvhStatus = avhStatus;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCarSpeed(float speed) {
        this.isDriving = speed > 3.0f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCarGearLevelChanged(int level) {
        xpExpandControl = SystemProperties.getInt("persist.sys.xiaopeng.xui.ExpandControl", 0) == 1;
        if (xpExpandControl) {
            LogUtil.i(TAG, "xpExpandControl MODE do not trigger LLU when gear change");
        } else if (this.curGear == level) {
        } else {
            this.isPGear = level == 4;
            if (this.isPGear) {
                startSayHi();
            } else {
                stopLightDancing();
                stopSayHi();
                stopLightEffectShow();
            }
            this.curGear = level;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFftData(float ratio, float minRatio) {
        AudioManager audioManager;
        if (!this.isLLUEnable || this.isRearFogOn || !this.isLLUFftEnable || this.brokenFftLlu || !this.isPGear || !ig_status_on || Math.abs(System.currentTimeMillis() - last_turnLamp_time) <= 1500 || this.musicSpectrumLampIntrupt || AiLluMode != 0 || (audioManager = this.mAudioManager) == null) {
            return;
        }
        if (audioManager.isMusicActive() || this.mAudioManager.isKaraokeOn()) {
            setMusicSpectrumToLangLight(ratio);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLluSw(int value) {
        if (value == 1) {
            setLluToInitMode();
            setLluSelfActiveMode(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLluFunction(int value) {
        if (value == 13) {
            this.LluCduSelfCtlMode = true;
        } else if (value == 0) {
            this.musicSpectrumIntrupt = false;
        } else if (this.LluCduSelfCtlMode && value != 13) {
            LogUtil.i(TAG, "LluCduSelfCtlMode to not SelfCtlMode!");
            this.LluCduSelfCtlMode = false;
            this.musicSpectrumIntrupt = true;
            updateTimer(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLluScriptSt(int value) {
        if (value == 1) {
            if (this.mcuLightEffect != null) {
                LluScriptParameter mLluScriptParameter = new LluScriptParameter.Builder().effectName(this.mcuLightEffect.getEffect_id()).frontEffectPeriod(this.mcuLightEffect.getFront_Effect().getPeriod()).frontEffectLoop(this.mcuLightEffect.getFront_Effect().getLoop()).frontEffectRetain(this.mcuLightEffect.getFront_Effect().getRetain()).frontEffectDataTotalLength(this.mcuLightEffect.getFront_Effect().getData().length).rearEffectPeriod(this.mcuLightEffect.getRear_Effect().getPeriod()).rearEffectLoop(this.mcuLightEffect.getRear_Effect().getLoop()).rearEffectRetain(this.mcuLightEffect.getRear_Effect().getRetain()).rearEffectDataTotalLength(this.mcuLightEffect.getRear_Effect().getData().length).build();
                try {
                    if (this.mCarLluManager != null) {
                        this.mCarLluManager.setLluScriptParameter(mLluScriptParameter);
                    }
                } catch (Exception e) {
                    handleException(e);
                }
            }
        } else if (value == 2) {
            SmartLluServiceListener smartLluServiceListener = this.mSmartLluServiceListener;
            if (smartLluServiceListener != null) {
                smartLluServiceListener.onLightEffectFinishEvent(this.currentMcuEffectName, this.currentMcuEffectMode);
            }
        } else if (value == 4) {
            sendPacket();
        } else if (value == 5) {
            sendPacket();
        } else if (value == 6) {
            this.frontDataFinish = true;
            sendPacket();
        } else if (value == 7) {
            setLluScriptStRequest(2);
        }
    }

    private void setLluSayHiEnable(boolean enable) {
        try {
            if (getSayHiEnable() != enable) {
                Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isSayHiEnable", enable ? 1 : 0);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "Exception " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleMcuLluShowOffSw(int value) {
        if (!this.trrigerXuiSayHi) {
            setLluSayHiEnable(value != 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleMcuLluSw(int value) {
        try {
            if (value == 1) {
                if (!getLangLightEffectEnable()) {
                    setLangLightEffectEnable(true);
                    return;
                }
                return;
            }
            if (getLangLightEffectEnable()) {
                setLangLightEffectEnable(false);
            }
            if (isLightDancing()) {
                doStopLDance();
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "Exception " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleMcuLluChargingSw(int propvalue) {
        XpPictureNotification.getInstance().generateBigPictureStyleNotification(propvalue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleMcuFbLluActive(int value) {
        if (value == 1) {
            this.musicSpectrumLampIntrupt = true;
            stopLightEffectShow();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFrontLightGroupChange(int value) {
        if (value == 0) {
            this.musicSpectrumLampIntrupt = false;
            if (this.isWaitingForSayHi) {
                doRealSayHiAfterWait();
                return;
            }
            return;
        }
        this.musicSpectrumLampIntrupt = true;
        CarControlUtils.getInstance().recoveryLampSaveMode(-1);
        stopLightEffectShow();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRearFogLampChange(int value) {
        this.isRearFogOn = value == 1;
        if (XUIConfig.getLluType() == 1) {
            if (this.isRearFogOn) {
                this.musicSpectrumLampIntrupt = true;
                stopSayHi();
                stopLightEffectShow();
                return;
            }
            this.musicSpectrumLampIntrupt = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLRTurnlamp() {
        stopLightEffectShow_turnLamp();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleIgStatusChange(int value) {
        String str;
        String str2;
        if (value == 0) {
            this.musicSpectrumIntrupt = true;
            resetLoopFlag();
            updateTimer(true);
            setLluToInitMode();
            CarControlUtils.getInstance().recoveryTurnOnoffSaveMode(-1);
            saveLampOffstatus(false);
            setLluSelfActiveMode(false);
            ig_status_on = false;
            if (llu_timeout_triggered_in_loop && this.mSmartLluServiceListener != null && (str = this.currentEffectName) != null && (str2 = this.currentEffectType) != null) {
                callBackLluShowStatusToClient(3, str, str2, 0);
                llu_timeout_triggered_in_loop = false;
            }
        } else if (value == 1) {
            this.musicSpectrumIntrupt = false;
            ig_status_on = true;
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        removeLluManagerListener(this.mCarLluEventCallback);
        removeVcuManagerListener(this.mCarVcuEventCallback);
        removeBcmManagerListener(this.mCarBcmEventCallback);
        removeMcuManagerListener(this.mCarMcuEventCallback);
        removeEspManagerListener(this.mCarEspEventCallback);
        this.mLluHandlerThread.quit();
        unregisterReceiver();
    }

    public void setMcuLangLightEffect(int effectName, int effectMode) {
        doMcuLangLightEffect(effectName, effectMode);
    }

    public int getLightEffect(int effectName) {
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        return Settings.System.getInt(contentResolver, "LIGHT_EFFECT" + effectName, 1);
    }

    public void setLightEffect(int effectName, int effectMode) {
        LogUtil.i(TAG, "setLightEffect effectName: " + effectName + " effectMode " + effectMode);
        doSetLightEffect(effectName, effectMode);
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        Settings.System.putInt(contentResolver, "LIGHT_EFFECT" + effectName, effectMode);
    }

    public void setLightEffectAndMusic(int messageID, int effectName, int effectMusic) {
    }

    public String getRunnningLluEffectName() {
        if (this.lightLoop == 0 && this.danceLoop == 0) {
            return null;
        }
        return this.currentEffectName;
    }

    public void setListener(SmartLluServiceListener carLluServiceListener) {
        LogUtil.i(TAG, "setListener");
        this.mSmartLluServiceListener = carLluServiceListener;
    }

    public int isLightDanceAvailable() {
        int ret = 0;
        if (!this.isPGear) {
            LogUtil.e(TAG, "Current is not P Gear, cannot display light dance");
            ret = 0 + 1;
        }
        if (CarControlUtils.getTurnLampStatus() || this.isRearFogOn) {
            LogUtil.e(TAG, "Current turn lamp is on, cannot display light dance");
            ret += 2;
        }
        AudioManager audioManager = this.mAudioManager;
        if (audioManager != null) {
            if (audioManager.getCurrentAudioFocusPackageName().equals("com.xiaopeng.btphone")) {
                LogUtil.e(TAG, "BT phone is on call, cannot display light dance");
                ret += 4;
            } else {
                AudioConfig audioConfig = this.mAudioConfig;
                if (audioConfig != null && audioConfig.getBtCallOnFlag() != 0) {
                    LogUtil.e(TAG, "BT phone is on call, cannot display light dance");
                    ret += 4;
                }
            }
        }
        return -ret;
    }

    public int startLightDancing(int loop) {
        return startLightDancing("PianoConcerto", loop);
    }

    public int startLightDancing(String file_name, int loop) {
        LogUtil.i(TAG, "startLightDacing: " + file_name + " " + loop);
        xpExpandControl = SystemProperties.getInt("persist.sys.xiaopeng.xui.ExpandControl", 0) == 1;
        if ((!this.isPGear && !xpExpandControl) || !this.isLLUEnable || this.isRearFogOn) {
            LogUtil.e(TAG, "startLightDacing :" + this.isPGear + "  " + this.isLLUEnable + " " + this.isRearFogOn);
            return -2;
        }
        if (this.mDancePlaying) {
            if (1 != 0) {
                stopLightDancing();
            } else {
                LogUtil.e(TAG, "startLightDacing is Playing");
                return -1;
            }
        }
        this.lightLoop = loop;
        this.danceLoop = loop;
        this.curDanceEffect = file_name;
        return preloadAndPlay(file_name);
    }

    public int stopLightDancing() {
        LogUtil.i(TAG, "stopLightDancing");
        if (isLightDancing()) {
            doStopLDance();
        } else if (this.mPreDance) {
            sendEventMessage(6, 0, 0, "", Videoio.CAP_PVAPI);
            this.mPreDance = false;
        }
        resetLoopFlag();
        this.curDanceEffect = null;
        return 0;
    }

    private void doTurnoffAndWait(String effectName, int reason) {
        try {
            LangLightEffect LLEffect = getLangLightEffectByName(effectName);
            if (LLEffect.getNotResetFlag() != 1) {
                resetPirvateLluControl();
                Thread.sleep(200L);
            }
            turnOffAndRestoreLight(reason, true);
            Thread.sleep(250L);
        } catch (Exception e) {
            LogUtil.e(TAG, "turnOffLightAndSetEffect " + e);
        }
    }

    private int preloadAndPlay(final String dance_name) {
        String music_file = getLDanceMediaFile(dance_name);
        if (music_file == null) {
            music_file = getDownLoadDanceMediaFile(dance_name);
        }
        if (music_file == null || getLangLightEffectByName(dance_name) == null) {
            LogUtil.e(TAG, "preloadAndPlay file not exist " + music_file);
            return -1;
        }
        if (isSayHing()) {
            this.isSayHi = false;
        }
        last_startLanglight_time = System.currentTimeMillis();
        this.triggerEffectName = dance_name;
        this.triggerEffectType = Type_Dance;
        if (sPlayer == null) {
            sPlayer = new MediaPlayer();
        }
        this.mPreDance = true;
        LogUtil.d(TAG, "mPreDance = TRUE");
        try {
            sPlayer.setAudioStreamType(3);
            sPlayer.reset();
            sPlayer.setDataSource(music_file);
            sPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.xiaopeng.xuiservice.smart.SmartLluService.9
                @Override // android.media.MediaPlayer.OnCompletionListener
                public void onCompletion(MediaPlayer mp) {
                    if (SmartLluService.this.danceLoop > 0) {
                        SmartLluService.access$1910(SmartLluService.this);
                    }
                    if (SmartLluService.this.danceLoop != 0) {
                        SmartLluService.this.sendReplayLightEffectEvent(2, null, dance_name);
                    } else {
                        SmartLluService.this.stopLightDancing();
                    }
                }
            });
            if (!handleVolumePolicy(true)) {
                callBackLluShowStatusToClient(4, dance_name, Type_Dance, -2);
                LogUtil.e(TAG, "Request AudioFocus Failed & do not start Light Dance");
                this.curDanceEffect = null;
                return -1;
            }
            if (this.mpPrepareAsync) {
                sPlayer.prepareAsync();
                sPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.xiaopeng.xuiservice.smart.-$$Lambda$SmartLluService$cCnPyTaU6TXJ4SF0yUe1o5ATRnI
                    @Override // android.media.MediaPlayer.OnPreparedListener
                    public final void onPrepared(MediaPlayer mediaPlayer) {
                        SmartLluService.this.lambda$preloadAndPlay$0$SmartLluService(dance_name, mediaPlayer);
                    }
                });
            } else {
                sPlayer.prepare();
                doTurnoffAndWait(dance_name, 1);
                sendLightEffectEventMessage(2, 0, null, dance_name, NfDef.STATE_STREAMING);
            }
            return 0;
        } catch (Exception e) {
            handleException(e);
            this.curDanceEffect = null;
            return -1;
        }
    }

    public /* synthetic */ void lambda$preloadAndPlay$0$SmartLluService(String dance_name, MediaPlayer mp) {
        doTurnoffAndWait(dance_name, 1);
        sendLightEffectEventMessage(2, 0, null, dance_name, NfDef.STATE_STREAMING);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void VolumeDownAndRestore(boolean down) {
        if (down) {
            int curVol = this.mAudioManager.getStreamVolume(3);
            if (curVol >= 5) {
                int downVol = (int) (curVol * 0.2d);
                this.RestoreVolume = curVol;
                this.DownVolume = downVol;
                this.mAudioManager.setStreamVolume(3, this.DownVolume, 0);
                return;
            }
            return;
        }
        if (this.RestoreVolume != -1 && this.mAudioManager.getStreamVolume(3) == this.DownVolume) {
            this.mAudioManager.setStreamVolume(3, this.RestoreVolume, 0);
        }
        this.RestoreVolume = -1;
        this.DownVolume = -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean handleVolumePolicy(boolean request) {
        if (requestAudioFocus(request)) {
            banVolumeChangeMode(request);
            return true;
        }
        return false;
    }

    private boolean requestAudioFocus(boolean request) {
        if (request) {
            return this.mAudioManager.requestAudioFocus(this.mAudioFocusListener, 3, 1) != 0;
        }
        this.mAudioManager.abandonAudioFocus(this.mAudioFocusListener);
        return true;
    }

    private void banVolumeChangeMode(boolean enable) {
        AudioConfig audioConfig = this.mAudioConfig;
        if (audioConfig != null) {
            audioConfig.setBanVolumeChangeMode(3, enable ? 1 : 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doStartLDance(String dance_name) {
        LogUtil.i(TAG, "doStartLDance()");
        MediaPlayer mediaPlayer = sPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
        setPause(false);
        this.mDancePlaying = true;
        this.mPreDance = false;
        doStartLangLightEffectShow(dance_name, 1, true);
        SystemProperties.set("persist.sys.light.dancing", OOBEEvent.STRING_TRUE);
    }

    private void doStopDanceMusic() {
        String str;
        if (sPlayer != null) {
            try {
                LogUtil.i(TAG, "doStopDanceMusic()");
                sPlayer.stop();
                sPlayer.release();
                sPlayer = null;
            } catch (Exception e) {
                handleException(e);
            }
        }
        handleVolumePolicy(false);
        this.mDancePlaying = false;
        if (this.mPreDance && (str = this.triggerEffectName) != null) {
            callBackLluShowStatusToClient(2, str, this.triggerEffectType, 0);
        }
        this.mPreDance = false;
        SystemProperties.set("persist.sys.light.dancing", OOBEEvent.STRING_FALSE);
    }

    private void doStopLDance() {
        LogUtil.d(TAG, "doStopLDance()");
        doStopDanceMusic();
        doStopLightEffectShow_l();
        turnOffAndRestoreLight(1, false);
    }

    public boolean isLightDancing() {
        return this.mDancePlaying;
    }

    private String getLDanceMediaFile(String dance_name) {
        String music_file = "/system/etc/xuiservice/dance/" + dance_name + ".mp3";
        if (!checkFileExist(music_file)) {
            music_file = "/system/etc/xuiservice/dance/" + dance_name + ".wav";
        }
        if (!checkFileExist(music_file)) {
            music_file = "/data/xuiservice/dance/" + dance_name + ".mp3";
        }
        if (!checkFileExist(music_file)) {
            music_file = "/data/xuiservice/dance/" + dance_name + ".wav";
        }
        if (!checkFileExist(music_file)) {
            LogUtil.e(TAG, "media file of " + dance_name + " has not found");
            return null;
        }
        return music_file;
    }

    private String getDownLoadDanceMediaFile(String dance_name) {
        LogUtil.d(TAG, "getDownLoadDanceMediaFile " + dance_name);
        if (dance_name == null) {
            return null;
        }
        List<String> mList = getDownloadAllMediaPath("/data/xuiservice/llu");
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i) != null) {
                String name = mList.get(i).substring(mList.get(i).lastIndexOf("/") + 1, mList.get(i).lastIndexOf("."));
                LogUtil.d(TAG, "path:" + mList.get(i) + " NAME:" + name);
                if (dance_name.equals(name)) {
                    LogUtil.d(TAG, "getDownLoadDanceMediaFile GOT!");
                    return mList.get(i);
                }
            }
        }
        return null;
    }

    private boolean checkFileExist(String filepath) {
        File file = new File(filepath);
        if (!file.exists()) {
            return false;
        }
        return true;
    }

    private boolean checkMediaNameExist(String mediaName) {
        if ("PianoConcerto".equals(mediaName)) {
            return true;
        }
        List<String> mList = getDownloadAllMediaName("/data/xuiservice/llu");
        for (int i = 0; i < mList.size(); i++) {
            LogUtil.d(TAG, "checkMediaNameExist  mediaName:" + mediaName + " listName:" + mList.get(i));
            if (mList.get(i).equals(mediaName)) {
                return true;
            }
        }
        return false;
    }

    private boolean isDanceEffect(String lightEffect) {
        if (getLDanceMediaFile(lightEffect) != null && getLangLightEffectByName(lightEffect) != null) {
            LogUtil.d(TAG, "**this is Dance Effect " + lightEffect);
            return true;
        }
        return false;
    }

    private void startSayHi() {
        LogUtil.i(TAG, "startSayHi " + this.mSayHiEnable + " " + isSayHing() + " " + this.isLLUEnable + " " + ig_status_on + " " + this.isRearFogOn + " " + this.mSayHiMode + " " + this.mSayHiEffect);
        if (!this.mSayHiEnable || isSayHing() || !this.isLLUEnable || this.isRearFogOn || !ig_status_on || this.mSayHiMode == 0) {
            return;
        }
        LogUtil.d(TAG, "startSayHi()");
        if (getLangLightEffectByName(this.mSayHiEffect) == null) {
            return;
        }
        removeDelayedEvent(5);
        turnOffAndRestoreLight(0, true);
        this.isSayHi = true;
        if (!this.isWaitingForSayHi) {
            doRealSayHiAfterWait();
        }
    }

    private void doRealSayHiAfterWait() {
        LogUtil.i(TAG, "doRealSayHiAfterWait()");
        this.isWaitingForSayHi = false;
        if (getLangLightEffectByName(this.mSayHiEffect) != null && isSayHing()) {
            doStartLangLightEffectShow(this.mSayHiEffect, 1, false);
        }
    }

    private void stopSayHi() {
        if (isSayHing()) {
            LogUtil.i(TAG, "stopSayHi()");
            turnOffAndRestoreLight(0, false);
            stopLightEffectShow();
            this.isSayHi = false;
        }
    }

    private boolean isSayHing() {
        return this.isSayHi;
    }

    private boolean isSayhiEffect(String lightEffect) {
        for (int i = 0; i < this.SHOW_OFF_MODE_01.size(); i++) {
            String autoSayhiName = this.SHOW_OFF_MODE_01.get(i);
            LogUtil.d(TAG, "autoSayhiName: " + autoSayhiName);
            if (autoSayhiName.equals(lightEffect)) {
                LogUtil.d(TAG, "**this is SayHi Effect " + lightEffect);
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getNumberByPos(int pos) {
        int i = 0;
        while (true) {
            int[] iArr = fTickTable;
            if (i < iArr.length - 1) {
                if (pos >= iArr[i] + ((iArr[i + 1] - iArr[i]) / 2)) {
                    i++;
                } else {
                    return i + 1;
                }
            } else {
                return iArr.length;
            }
        }
    }

    public void startAiLluMode(final int type) {
        if (this.mSmartAvmManager == null) {
            return;
        }
        stopLightEffectShow();
        sendEventMessage(4, type, 0, 0, 300);
        LogUtil.i(TAG, "startAiLluMode, type=" + type);
        try {
            this.mSmartAvmManager.initSmartAvmManager(type);
            this.mSmartAvmManager.registerListener(new SmartAvmManager.SmartAvmEventListener() { // from class: com.xiaopeng.xuiservice.smart.SmartLluService.11
                @Override // com.xiaopeng.xuiservice.cv.SmartAvmManager.SmartAvmEventListener
                public void onMotionTrackingCallBack(int position) {
                    int fTick = 0;
                    if (position < 0) {
                        int unused = SmartLluService.PrevTick = 0;
                        if (SmartLluService.mLluAiListener != null) {
                            LogUtil.d(SmartLluService.TAG, "onMotionTrackingCallBack, stop Llu");
                            SmartLluService.mLluAiListener.onAiLluEvent(type, 0);
                            return;
                        }
                        return;
                    }
                    int pos = ((position * 67) / 100) + 3;
                    int number = SmartLluService.this.getNumberByPos(pos);
                    if (number > 0 && number < 14) {
                        fTick = SmartLluService.fTickTable[number - 1];
                    }
                    int fType = fTick == 10 ? 17 : 40;
                    LogUtil.d(SmartLluService.TAG, "onMotionTrackingCallBack, pos=" + pos + ", fTick=" + fTick + ", mumber=" + number);
                    if (SmartLluService.PrevTick != fTick) {
                        int unused2 = SmartLluService.PrevTick = fTick;
                        if (SmartLluService.this.mCarLluManager != null) {
                            try {
                                SmartLluService.this.mCarLluManager.setLluPrivateCtrl(true, fType, fTick, 0, 0);
                                SmartLluService.this.mSoundPlayHelper.playNote(SmartLluService.this.mAiLluAudioTable.getNoteAudioId(number));
                            } catch (Exception e) {
                                SmartLluService.this.handleException(e);
                            }
                        }
                        if (SmartLluService.mLluAiListener != null) {
                            SmartLluService.mLluAiListener.onAiLluEvent(type, number);
                        }
                    }
                }
            });
        } catch (Exception e) {
            handleException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r0v0, types: [com.xiaopeng.xuiservice.smart.SmartLluService$12] */
    public void doStartAiLlu(int type) {
        new Thread() { // from class: com.xiaopeng.xuiservice.smart.SmartLluService.12
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    if (SmartLluService.this.handleVolumePolicy(true)) {
                        int unused = SmartLluService.AiLluMode = 1;
                        SmartLluService.this.resetPirvateLluControl();
                        Thread.sleep(200L);
                        SmartLluService.this.turnOffAndRestoreLight(3, true);
                        Thread.sleep(250L);
                        SmartLluService.this.mSmartAvmManager.startSmartAvm();
                        return;
                    }
                    LogUtil.e(SmartLluService.TAG, "Request AudioFocus Failed & do not start AI Light");
                } catch (Exception e) {
                    LogUtil.e(SmartLluService.TAG, "turnOffLightAndSetEffect " + e);
                }
            }
        }.start();
    }

    public void setAiLluListener(LluAiListener carAiLluListener) {
        LogUtil.i(TAG, "setListener");
        mLluAiListener = carAiLluListener;
    }

    public void stopAiLluMode() {
        if (this.mSmartAvmManager == null) {
            return;
        }
        LogUtil.i(TAG, "stopAiLluMode");
        if (AiLluMode != 0) {
            AiLluMode = 0;
            this.mSmartAvmManager.stopSmartAvm();
            turnOffAndRestoreLight(3, false);
            this.mSoundPlayHelper.releaseSoundPool();
            handleVolumePolicy(false);
        }
    }

    public int getAiLluMode() {
        return AiLluMode;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callBackLluShowStatusToClient(int funcType, String effectName, String effectType, int errorcode) {
        LogUtil.d(TAG, "callBackLluShowStatusToClient " + funcType + " " + effectName);
        if (effectName == null || effectName.equals("")) {
            LogUtil.d(TAG, "callBackLluShowStatusToClient type:" + funcType + " effectName not legal");
        } else if (this.mSmartLluServiceListener == null) {
        } else {
            synchronized (lluCallbackLock) {
                try {
                    if (funcType == 1) {
                        LogUtil.d(TAG, "onLightEffectShowStartEvent  " + effectName + " " + effectType);
                        this.mSmartLluServiceListener.onLightEffectShowStartEvent(effectName, effectType);
                    } else if (funcType == 2) {
                        LogUtil.d(TAG, "onLightEffectShowStopEvent  " + effectName + " " + effectType);
                        this.mSmartLluServiceListener.onLightEffectShowStopEvent(effectName, effectType);
                        this.currentEffectName = null;
                        this.triggerEffectName = null;
                    } else if (funcType == 3) {
                        LogUtil.d(TAG, "onLightEffectShowFinishEvent  " + effectName + " " + effectType);
                        this.mSmartLluServiceListener.onLightEffectShowFinishEvent(effectName, effectType);
                        this.currentEffectName = null;
                        this.triggerEffectName = null;
                    } else if (funcType == 4) {
                        LogUtil.d(TAG, "onLightEffectShowError  " + effectName + " " + errorcode);
                        if (Type_Dance.equals(effectType) || Type_LightDance.equals(effectType)) {
                            errorcode = ErrorCode_DanceError;
                        }
                        this.mSmartLluServiceListener.onLightEffectShowError(effectName, errorcode);
                        this.currentEffectName = null;
                        this.triggerEffectName = null;
                    } else {
                        LogUtil.w(TAG, "callBackLluShowStatusToClient NO such type:" + funcType);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final SmartLluService sService = new SmartLluService(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected boolean needConnectXUI() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class LluPlayClass {
        String danceName;
        int loop;
        LangLightEffect mLanglightEffect;

        private LluPlayClass() {
        }

        public LangLightEffect getLanglightEffect() {
            return this.mLanglightEffect;
        }

        public void setLanglightEffect(LangLightEffect lighteffect) {
            this.mLanglightEffect = lighteffect;
        }

        public int getLoop() {
            return this.loop;
        }

        public void setLoop(int loop) {
            this.loop = loop;
        }

        public void setDanceName(String danceName) {
            this.danceName = danceName;
        }

        public String getDanceName() {
            return this.danceName;
        }
    }

    /* loaded from: classes5.dex */
    private class AiLluAudioTable {
        private SparseArray<Integer> mNoteAudioTable = new SparseArray<>();

        /* JADX INFO: Access modifiers changed from: private */
        public int getNoteAudioId(int index) {
            return this.mNoteAudioTable.get(index).intValue();
        }

        AiLluAudioTable() {
            initNoteAudioTable();
        }

        private void initNoteAudioTable() {
            this.mNoteAudioTable.put(1, Integer.valueOf((int) R.raw.music_1));
            this.mNoteAudioTable.put(2, Integer.valueOf((int) R.raw.music_2));
            this.mNoteAudioTable.put(3, Integer.valueOf((int) R.raw.music_3));
            this.mNoteAudioTable.put(4, Integer.valueOf((int) R.raw.music_4));
            this.mNoteAudioTable.put(5, Integer.valueOf((int) R.raw.music_5));
            this.mNoteAudioTable.put(6, Integer.valueOf((int) R.raw.music_6));
            this.mNoteAudioTable.put(7, Integer.valueOf((int) R.raw.music_7));
            this.mNoteAudioTable.put(8, Integer.valueOf((int) R.raw.music_8));
            this.mNoteAudioTable.put(9, Integer.valueOf((int) R.raw.music_9));
            this.mNoteAudioTable.put(10, Integer.valueOf((int) R.raw.music_10));
            this.mNoteAudioTable.put(11, Integer.valueOf((int) R.raw.music_11));
            this.mNoteAudioTable.put(12, Integer.valueOf((int) R.raw.music_12));
            this.mNoteAudioTable.put(13, Integer.valueOf((int) R.raw.music_13));
        }
    }

    public void updateTimer(boolean stopMusic) {
        synchronized (timerLock) {
            if (this.timer != null) {
                this.timer.cancel();
            }
            this.timer = new Timer();
        }
        setLluSelfActiveMode(false);
        removePlayLoopEvent(true);
        resetLastLightFlag();
        if (stopMusic) {
            doStopDanceMusic();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tickLluTime() {
        finishcallback_triggered = false;
        llu_timeout_triggered_in_loop = false;
        UiHandler.getInstance().removeCallbacks(this.llu_timeout_thread);
        UiHandler.getInstance().postDelayed(this.llu_timeout_thread, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void untickLluTime() {
        UiHandler.getInstance().removeCallbacks(this.llu_timeout_thread);
    }

    private void tickLluStopTime() {
        UiHandler.getInstance().removeCallbacks(this.llu_stop_timeout_thread);
        UiHandler.getInstance().postDelayed(this.llu_stop_timeout_thread, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendReplayLightEffectEvent(int messageid, LangLightEffect lighteffect, String danceName) {
        LogUtil.i(TAG, "sendReplayLightEffectEvent " + this.lightLoop);
        Message message = this.mLluHandlerThread.getHandler().obtainMessage();
        message.what = messageid;
        LluPlayClass mLluPlayClass = new LluPlayClass();
        mLluPlayClass.setLoop(this.lightLoop);
        mLluPlayClass.setLanglightEffect(lighteffect);
        mLluPlayClass.setDanceName(danceName);
        if (lighteffect != null && messageid == 1) {
            SendingLluEffectName = lighteffect.getEffect_name();
            SendingLluEffectType = lighteffect.getEffect_type();
        } else if (messageid == 2 && danceName != null && !danceName.equals("")) {
            SendingLluEffectName = danceName;
            SendingLluEffectType = Type_Dance;
        }
        message.obj = mLluPlayClass;
        this.mLluHandlerThread.getHandler().sendMessageDelayed(message, this.LIGHT_DANCE_INTERVAL * 1000);
    }

    private void sendLightEffectEventMessage(int messageid, int loop, LangLightEffect lighteffect, String danceName, int delay) {
        LogUtil.i(TAG, "sendLightEffectEventMessage " + messageid + " " + loop);
        Message message = this.mLluHandlerThread.getHandler().obtainMessage();
        message.what = messageid;
        LluPlayClass mLluPlayClass = new LluPlayClass();
        mLluPlayClass.setLoop(loop);
        mLluPlayClass.setLanglightEffect(lighteffect);
        mLluPlayClass.setDanceName(danceName);
        if (lighteffect != null && messageid == 1) {
            SendingLluEffectName = lighteffect.getEffect_name();
            SendingLluEffectType = lighteffect.getEffect_type();
        } else if (messageid == 2 && danceName != null && !danceName.equals("")) {
            SendingLluEffectName = danceName;
            SendingLluEffectType = Type_Dance;
        }
        message.obj = mLluPlayClass;
        this.mLluHandlerThread.getHandler().sendMessageDelayed(message, delay);
    }

    private void sendEventMessage(int messageid, int arg1, int arg2, Object object, int delay) {
        LogUtil.i(TAG, "sendEventMessage " + messageid);
        Message message = this.mLluHandlerThread.getHandler().obtainMessage();
        message.what = messageid;
        message.arg1 = arg1;
        message.arg2 = arg2;
        message.obj = object;
        this.mLluHandlerThread.getHandler().sendMessageDelayed(message, delay);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removePlayLoopEvent(boolean needCallBack) {
        LogUtil.d(TAG, "removePlayLoopEvent " + needCallBack + " " + SendingLluEffectName);
        this.mLluHandlerThread.getHandler().obtainMessage();
        this.mLluHandlerThread.getHandler().removeMessages(1);
        this.mLluHandlerThread.getHandler().removeMessages(2);
        this.mLluHandlerThread.getHandler().removeMessages(3);
        this.mLluHandlerThread.getHandler().removeMessages(4);
        this.mLluHandlerThread.getHandler().removeMessages(5);
        String str = SendingLluEffectName;
        if (str != null && needCallBack) {
            callBackLluShowStatusToClient(4, str, SendingLluEffectType, -4);
            SendingLluEffectName = null;
        }
    }

    private void removeDelayedEvent(int messageId) {
        LogUtil.d(TAG, "removeDelayedEvent " + messageId);
        this.mLluHandlerThread.getHandler().removeMessages(messageId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class LluHandlerThread extends HandlerThread {
        private static final String TAG = "LluHandlerThread";
        private Handler handler;
        private LluPlayClass mLluPlayClass;

        public LluHandlerThread() {
            super(TAG, 10);
        }

        @Override // android.os.HandlerThread
        @SuppressLint({"HandlerLeak"})
        protected void onLooperPrepared() {
            this.handler = new Handler() { // from class: com.xiaopeng.xuiservice.smart.SmartLluService.LluHandlerThread.1
                @Override // android.os.Handler
                public void handleMessage(Message msg) {
                    LogUtil.i(LluHandlerThread.TAG, "handleMessage  msg:" + msg.what);
                    switch (msg.what) {
                        case 1:
                            String unused = SmartLluService.SendingLluEffectName = null;
                            LluHandlerThread.this.mLluPlayClass = (LluPlayClass) msg.obj;
                            SmartLluService.this.doLangLightEffectMode(LluHandlerThread.this.mLluPlayClass.getLanglightEffect());
                            return;
                        case 2:
                            String unused2 = SmartLluService.SendingLluEffectName = null;
                            LluHandlerThread.this.mLluPlayClass = (LluPlayClass) msg.obj;
                            SmartLluService.this.doStartLDance(LluHandlerThread.this.mLluPlayClass.getDanceName());
                            return;
                        case 3:
                            LluHandlerThread.this.mLluPlayClass = (LluPlayClass) msg.obj;
                            SmartLluService.this.resetLluModeAndPlayLightEffect(LluHandlerThread.this.mLluPlayClass.getLanglightEffect(), LluHandlerThread.this.mLluPlayClass.getLoop());
                            return;
                        case 4:
                            SmartLluService.this.doStartAiLlu(msg.arg1);
                            return;
                        case 5:
                        case 6:
                            SmartLluService.this.doStopLightEffectShow();
                            return;
                        case 7:
                            SmartLluService.this.turnOffAndRestoreLight(3, false);
                            SmartLluService.this.doSetSpecialMode(0, 0, false);
                            return;
                        default:
                            LogUtil.e(LluHandlerThread.TAG, "Event type not handled?" + msg);
                            return;
                    }
                }
            };
        }

        public Handler getHandler() {
            return this.handler;
        }
    }

    private String getAllLightEffect() {
        int effectNum = this.mLangLightEffectList.size();
        if (effectNum == 0) {
            return null;
        }
        try {
            JSONObject effectObject = new JSONObject();
            effectObject.put("effectNum", effectNum);
            for (int i = 0; i < effectNum; i++) {
                LangLightEffect LLEffect = this.mLangLightEffectList.get(i);
                LogUtil.i(TAG, "getAllLightEffect " + i + " effectName: " + LLEffect.getEffect_name());
                StringBuilder sb = new StringBuilder();
                sb.append("effectName");
                sb.append(i);
                effectObject.put(sb.toString(), LLEffect.getEffect_name());
            }
            String effectString = effectObject.toString();
            return effectString;
        } catch (Exception e) {
            handleException(e);
            return null;
        }
    }

    private String getAllLightDanceEffect() {
        int danceNum = 0;
        int effectNum = this.mLangLightEffectList.size();
        if (effectNum == 0) {
            return null;
        }
        try {
            JSONObject effectObject = new JSONObject();
            for (int i = 0; i < effectNum; i++) {
                LangLightEffect LLEffect = this.mLangLightEffectList.get(i);
                String effectName = LLEffect.getEffect_name();
                LogUtil.i(TAG, "getAllLightDanceEffect " + i + " effectName: " + effectName);
                if (getLDanceMediaFile(effectName) != null) {
                    danceNum++;
                    effectObject.put("effectName" + i, effectName);
                }
            }
            effectObject.put("effectNum", danceNum);
            String effectString = effectObject.toString();
            return effectString;
        } catch (Exception e) {
            handleException(e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public LangLightEffect getLangLightEffectByName(String effectName) {
        if (this.mLangLightEffectList.size() > 0) {
            for (int i = 0; i < this.mLangLightEffectList.size(); i++) {
                LangLightEffect LLEffect = this.mLangLightEffectList.get(i);
                LogUtil.i(TAG, "getLangLightEffectByName effectName: " + LLEffect.getEffect_name());
                if (LLEffect.getEffect_name().equals(effectName)) {
                    return LLEffect;
                }
            }
            return null;
        }
        return null;
    }

    private List<String> getFilesAllName(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) {
            LogUtil.e(TAG, "empty folder");
            return null;
        }
        List<String> s = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            LogUtil.i(TAG, "File path" + files[i].getAbsolutePath());
            s.add(files[i].getAbsolutePath());
        }
        return s;
    }

    private List<String> getDownloadAllId(int type, String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) {
            LogUtil.e(TAG, "empty folder");
            return null;
        }
        List<String> s = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            LogUtil.i(TAG, "File folder Name(id) :" + files[i].getName());
            if (type == 1) {
                s.add(files[i].getName());
            } else {
                s.add(files[i].getAbsolutePath());
            }
        }
        return s;
    }

    private List<String> getDownloadAllEffectPath(String path) {
        List<String> s = new ArrayList<>();
        FileUtils.parseAllFileInFolder(path, 0, s, "json");
        return s;
    }

    private List<String> getDownloadAllEffectName(String path) {
        List<String> s = new ArrayList<>();
        FileUtils.parseAllFileInFolder(path, 1, s, "json");
        return s;
    }

    private List<String> getDownloadAllMediaPath(String path) {
        List<String> s = new ArrayList<>();
        FileUtils.parseAllFileInFolder(path, 0, s, "mp3");
        return s;
    }

    private List<String> getDownloadAllMediaName(String path) {
        List<String> s = new ArrayList<>();
        FileUtils.parseAllFileInFolder(path, 1, s, "mp3");
        return s;
    }

    private void loadLightEffectFromFile(String llu_file) {
        File file = new File(llu_file);
        String effectFiles = file.getAbsolutePath();
        if (effectFiles != null) {
            try {
                LangLightEffect langLightEffect = (LangLightEffect) new Gson().fromJson((Reader) new FileReader(effectFiles), (Class<Object>) LangLightEffect.class);
                String llu_name = langLightEffect.getEffect_name();
                if (!this.mLangLightEffectNameList.contains(llu_name)) {
                    this.mLangLightEffectList.add(langLightEffect);
                    this.mLangLightEffectNameList.add(llu_name);
                    LogUtil.i(TAG, "loadLightEffectFromFile getEffect_name: " + llu_name);
                }
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    private void loadLightEffectFromPath(String llu_path) {
        List<String> effectFiles;
        if ("/data/xuiservice/llu".equals(llu_path)) {
            effectFiles = getDownloadAllEffectPath("/data/xuiservice/llu");
        } else {
            effectFiles = getFilesAllName(llu_path);
        }
        if (effectFiles != null) {
            for (int i = 0; i < effectFiles.size(); i++) {
                try {
                    this.langLightEffect = (LangLightEffect) new Gson().fromJson((Reader) new FileReader(effectFiles.get(i)), (Class<Object>) LangLightEffect.class);
                    if (!this.mLangLightEffectNameList.contains(this.langLightEffect.getEffect_name())) {
                        this.mLangLightEffectList.add(this.langLightEffect);
                        this.mLangLightEffectNameList.add(this.langLightEffect.getEffect_name());
                        LogUtil.i(TAG, "loadLightEffectFromPath getEffect_name: " + this.langLightEffect.getEffect_name());
                    }
                } catch (Exception e) {
                    handleException(e);
                }
            }
        }
    }

    private String getContentFromFile(File fileName) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(new FileInputStream(fileName));
            BufferedReader br = new BufferedReader(reader);
            while (true) {
                String line = br.readLine();
                if (line != null) {
                    stringBuilder.append(line);
                } else {
                    br.close();
                    reader.close();
                    return stringBuilder.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getLluLocalDanceNameList() {
        File mfile = new File("/system/etc/xuiservice/dance/LocalDance_Info.json");
        String LocalDanceNameJson = getContentFromFile(mfile);
        LogUtil.i(TAG, "getLluLocalDanceNameList PATH:/system/etc/xuiservice/dance/LocalDance_Info.json  " + LocalDanceNameJson);
        return LocalDanceNameJson;
    }

    private void parseAllLightEffectFromJsons() {
        this.mLangLightEffectList.clear();
        this.mLangLightEffectNameList.clear();
        loadLightEffectFromPath("/data/xuiservice/llu");
        loadLightEffectFromPath("/system/etc/xuiservice/llu");
    }

    public List<String> getLangLightEffectNameList(int effectType) {
        return this.mLangLightEffectNameList;
    }

    private JSONObject parseEffectInfoByID(String idPath) {
        JSONObject jsonObj = null;
        List<String> s = getDownloadAllEffectName(idPath);
        try {
            String id = idPath.substring(idPath.lastIndexOf("/") + 1);
            LogUtil.d(TAG, "parseEffectInfoByID id:" + id + " path:" + idPath + " Name:" + s);
            if (s == null || s.size() != 1) {
                return null;
            }
            jsonObj = new JSONObject();
            JSONObject content = new JSONObject();
            jsonObj.put("id", id);
            jsonObj.put("category", 1);
            content.put("effectName", s.get(0));
            String videoPath = idPath + "/" + s.get(0) + ".mp4";
            content.put("videoPath", videoPath);
            jsonObj.put("content", content);
            return jsonObj;
        } catch (Exception e) {
            LogUtil.e(TAG, "parseEffectInfoByID ERROR:" + e);
            return jsonObj;
        }
    }

    public String getLluDownLoadEffectNameById(String id) {
        JSONObject jsonObj = parseEffectInfoByID("/data/xuiservice/llu/" + id);
        if (jsonObj == null) {
            return null;
        }
        return jsonObj.toString();
    }

    public String getLluDownLoadEffectNameList() {
        JSONArray jsonArr = new JSONArray();
        new JSONObject();
        LogUtil.d(TAG, "getLluDownLoadEffectNameList()");
        List<String> idLists = getDownloadAllId(0, "/data/xuiservice/llu");
        LogUtil.d(TAG, "ID size:" + idLists.size());
        for (int i = 0; i < idLists.size(); i++) {
            JSONObject jsonObj = parseEffectInfoByID(idLists.get(i));
            if (jsonObj != null) {
                jsonArr.put(jsonObj);
            }
        }
        LogUtil.d(TAG, "getLluDownLoadEffectNameList : " + jsonArr.toString());
        return jsonArr.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public class LangLightTask extends TimerTask {
        int count = 0;
        int frontNum = 0;
        int rearNum = 0;
        int oldfType = -1;
        int oldfTick = -1;
        int oldrType = -1;
        int oldrTick = -1;
        int frontPeriod = -1;
        int frontRetain = -1;
        int frontLoop = -1;
        int[] frontData = null;
        String[] sfrontData = null;
        int rearPeriod = -1;
        int rearRetain = -1;
        int rearLoop = -1;
        int[] rearData = null;
        String[] srearData = null;
        String oldsfType = "";
        String oldsrType = "";

        LangLightTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
        }

        public void init(LangLightEffect LLEffect) {
            if (XUIConfig.getLluType() == 0) {
                this.frontPeriod = LLEffect.getFront_Effect().getPeriod();
                this.frontRetain = LLEffect.getFront_Effect().getRetain();
                this.frontLoop = LLEffect.getFront_Effect().getLoop();
                this.frontData = SmartLluService.this.copyArray(LLEffect.getFront_Effect().getData(), this.frontLoop);
                this.rearPeriod = LLEffect.getRear_Effect().getPeriod();
                this.rearRetain = LLEffect.getRear_Effect().getRetain();
                this.rearLoop = LLEffect.getRear_Effect().getLoop();
                this.rearData = SmartLluService.this.copyArray(LLEffect.getRear_Effect().getData(), this.frontLoop);
            } else if (XUIConfig.getLluType() == 1) {
                this.frontPeriod = LLEffect.getFront_Effect().getPeriod();
                this.frontRetain = LLEffect.getFront_Effect().getRetain();
                this.frontLoop = LLEffect.getFront_Effect().getLoop();
                this.sfrontData = SmartLluService.this.copyStrArray(LLEffect.getFront_Effect().getSdata(), this.frontLoop);
                this.rearPeriod = LLEffect.getRear_Effect().getPeriod();
                this.rearRetain = LLEffect.getRear_Effect().getRetain();
                this.rearLoop = LLEffect.getRear_Effect().getLoop();
                this.srearData = SmartLluService.this.copyStrArray(LLEffect.getRear_Effect().getSdata(), this.rearLoop);
            }
        }
    }

    private void initConfig() {
        this.mLangLightEffectList = new ArrayList<>();
        this.mLangLightEffectNameList = new ArrayList<>();
        this.mDownLangLightEffectNameList = new ArrayList<>();
        updateEffectFiles();
        this.mSayHiMode = getLightEffect(10);
        int i = this.mSayHiMode;
        if (i >= 0 && i <= 3) {
            this.mSayHiEffect = this.SHOW_OFF_MODE_01.get(i);
        }
        if (XUIConfig.getLluType() == 1) {
            INFINITE_FRONTLIGHT_COUNT = XUIConfig.getLluFrontNumType();
            INFINITE_REARLIGHT_COUNT = XUIConfig.getLluRearNumType();
        }
        if (getLampOffstatus()) {
            LogUtil.w(TAG, "Find lamp off by llu before reboot , need restore to AUTO");
            CarControlUtils.getInstance().recoveryLampSaveMode(3);
            turnOffAndRestoreLight(3, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deactivateAndroidLluControl() {
        try {
            LogUtil.i(TAG, "deactivateAndroidLluControl");
            this.mCarLluManager.deactivateAndroidLluControl();
            setLluSelfActiveMode(false);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void doStopLightEffectShow() {
        if (isLightDancing() || this.mPreDance) {
            stopLightDancing();
        } else {
            doStopLightEffectShow_l();
        }
    }

    public void doStopLightEffectShow_l() {
        LogUtil.d(TAG, "doStopLightEffectShow_l()");
        if (this.mAudioManager.isMusicActive() && this.timer != null && !this.musicSpectrumIntrupt && !this.musicSpectrumPause && this.isLLUFftEnable) {
            tickLluTime();
        }
        if (getAiLluMode() != 0) {
            stopAiLluMode();
        }
        if (this.isInSpecialMode) {
            doSetSpecialMode(0, 0, false);
        }
        this.musicSpectrumIntrupt = false;
        this.lightLoop = 0;
        removePlayLoopEvent(true);
        resetLastLightFlag();
        synchronized (timerLock) {
            if (this.timer != null) {
                this.timer.cancel();
            }
            this.timer = null;
        }
        turnOffAndRestoreLight(3, false);
        setLluToInitMode();
        callBackLluShowStatusToClient(2, this.currentEffectName, this.currentEffectType, 0);
        if (llu_timeout_triggered_in_loop) {
            callBackLluShowStatusToClient(3, this.currentEffectName, this.currentEffectType, 0);
            llu_timeout_triggered_in_loop = false;
        } else if (!finishcallback_triggered) {
            callBackLluShowStatusToClient(3, this.currentEffectName, this.currentEffectType, 0);
            finishcallback_triggered = true;
        }
        deactivateAndroidLluControl();
    }

    private void stopLightEffectShow_turnLamp() {
        last_turnLamp_time = System.currentTimeMillis();
        if (isLightDancing()) {
            doStopDanceMusic();
        }
        if (isSayHing()) {
            stopSayHi();
        }
        if (getAiLluMode() != 0) {
            stopAiLluMode();
        }
        if (this.isInSpecialMode) {
            doSetSpecialMode(0, 0, false);
        }
        this.musicSpectrumIntrupt = false;
        resetLoopFlag();
        removePlayLoopEvent(true);
        synchronized (timerLock) {
            if (this.timer != null) {
                this.timer.cancel();
            }
            this.timer = null;
        }
        turnOffAndRestoreLight(3, false);
        callBackLluShowStatusToClient(2, this.currentEffectName, this.currentEffectType, 0);
        if (llu_timeout_triggered_in_loop) {
            callBackLluShowStatusToClient(3, this.currentEffectName, this.currentEffectType, 0);
            llu_timeout_triggered_in_loop = false;
        } else if (!finishcallback_triggered) {
            callBackLluShowStatusToClient(3, this.currentEffectName, this.currentEffectType, 0);
            finishcallback_triggered = true;
        }
        deactivateAndroidLluControl();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void turnOffAndRestoreLight(int turnoffReason, boolean turnoff) {
        if (CarControlUtils.getInstance().turnOffAndRestoreLight(turnoffReason, turnoff) == 1) {
            this.isWaitingForSayHi = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[] copyArray(int[] srcArray, int num) {
        if (srcArray == null) {
            printLogInterval("copyArray src is null", 1, 1);
            return null;
        }
        int[] destArray = new int[srcArray.length * num];
        for (int i = 0; i < num; i++) {
            System.arraycopy(srcArray, 0, destArray, srcArray.length * i, srcArray.length);
        }
        return destArray;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String[] copyStrArray(String[] srcArray, int num) {
        if (srcArray == null) {
            printLogInterval("copyStrArray src is null", 1, 1);
            return null;
        }
        String[] destArray = new String[srcArray.length * num];
        for (int i = 0; i < num; i++) {
            System.arraycopy(srcArray, 0, destArray, srcArray.length * i, srcArray.length);
        }
        return destArray;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetLastLightFlag() {
        LogUtil.i(TAG, "resetLastLightFlag()");
        fLightLast = -1;
        rfLightLast = -1;
        windowCmdLast = -1;
        scissorsLgateCmdLast = -1;
        scissorsRgateCmdLast = -1;
        mirrorCmdLast = -1;
        LluPrivatePrintCount = 0;
    }

    private void resetLoopFlag() {
        this.danceLoop = 0;
        this.lightLoop = 0;
    }

    public void resetPirvateLluControl() {
        try {
            LogUtil.i(TAG, "resetPirvateLluControl()");
            setLluToInitMode();
            if (this.mCarLluManager != null) {
                this.mCarLluManager.activateAndroidLluControl();
                Thread.sleep(20L);
                if (XUIConfig.getLluType() == 0) {
                    setLluSelfActiveMode(false);
                    Thread.sleep(30L);
                }
                setLluSelfActiveMode(true);
                Thread.sleep(30L);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setInfiniteLluPrivateCtrl(boolean start, String fType, String rType, int flight, int rlight, String expandControl) {
        printLogInterval("setInfiniteLluPrivateCtrl : " + start + " " + flight + " " + rlight + " expand:" + expandControl, 4, 1);
        if (this.mCarLluManager == null || !ig_status_on) {
            printLogInterval("setInfiniteLluPrivateCtrl  not ready!!! " + ig_status_on, 1, 1);
            return;
        }
        printLogInterval("{" + fType + "}", 4, 2);
        printLogInterval("{" + rType + "}", 4, 3);
        byte[] data = convertInfiniteStringToLluData(fType, rType, flight, rlight);
        if (data == null) {
            return;
        }
        try {
            this.mCarLluManager.setMcuLLuSelfControlData(data);
        } catch (Exception e) {
            LogUtil.e(TAG, "setInfiniteLluPrivateCtrl " + e);
        }
    }

    private byte[] convertInfiniteStringToLluData(String fType, String rType, int flight, int rlight) {
        byte[] dataFront;
        byte[] dataRear;
        int i = INFINITE_FRONTLIGHT_COUNT;
        int length = ((INFINITE_REARLIGHT_COUNT + i) / 2) + (i == 0 ? 0 : 1) + (INFINITE_REARLIGHT_COUNT == 0 ? 0 : 1);
        byte[] data = new byte[length];
        data[0] = (byte) (flight == 1 ? 1 : 0);
        if (INFINITE_FRONTLIGHT_COUNT != 0 && fType.equals("")) {
            dataFront = new byte[INFINITE_FRONTLIGHT_COUNT / 2];
            Arrays.fill(dataFront, (byte) 0);
        } else {
            dataFront = CommonUtils.hexString2Bytes(fType);
        }
        if (dataFront.length == INFINITE_FRONTLIGHT_COUNT / 2) {
            System.arraycopy(dataFront, 0, data, 1, dataFront.length);
        }
        if (INFINITE_REARLIGHT_COUNT != 0) {
            if (rType.equals("")) {
                dataRear = new byte[INFINITE_REARLIGHT_COUNT / 2];
                Arrays.fill(dataRear, (byte) 0);
            } else {
                dataRear = CommonUtils.hexString2Bytes(rType);
            }
            printLogInterval("convertInfiniteStringToLluData  length:" + length + " fLength:" + dataFront.length + " rLength:" + dataRear.length, 4, 4);
            data[(INFINITE_FRONTLIGHT_COUNT / 2) + 1] = (byte) (rlight != 1 ? 0 : 1);
            if (dataRear.length == INFINITE_REARLIGHT_COUNT / 2) {
                System.arraycopy(dataRear, 0, data, (INFINITE_FRONTLIGHT_COUNT / 2) + 2, dataRear.length);
            }
        } else {
            printLogInterval("convertInfiniteStringToLluData  length:" + length + " fLength:" + dataFront.length, 4, 4);
        }
        return data;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLluPrivateCtrl(boolean start, int fType, int fTick, int rType, int rTick, int fLight, int rfLight, int expandControl) {
        StringBuilder sb;
        try {
            if (!ig_status_on) {
                if (LluPrivatePrintCount % 200 == 0) {
                    LogUtil.i(TAG, "setLluPrivateCtrl " + ig_status_on);
                }
                LluPrivatePrintCount++;
                return;
            }
            if (LluPrivatePrintCount % 200 == 0) {
                try {
                    sb = new StringBuilder();
                    sb.append("setLluPrivateCtrl start: ");
                    sb.append(start);
                    sb.append(" fType: ");
                } catch (Exception e) {
                    e = e;
                    handleException(e);
                    return;
                }
                try {
                    sb.append(fType);
                    sb.append(" fTick: ");
                } catch (Exception e2) {
                    e = e2;
                    handleException(e);
                    return;
                }
                try {
                    sb.append(fTick);
                    sb.append(" rType: ");
                    try {
                        sb.append(rType);
                        sb.append(" rTick: ");
                    } catch (Exception e3) {
                        e = e3;
                        handleException(e);
                        return;
                    }
                    try {
                        sb.append(rTick);
                        sb.append(" fLight: ");
                        sb.append(fLight);
                        sb.append(" rfLight: ");
                        sb.append(rfLight);
                        sb.append(" expandControl:");
                        sb.append(expandControl);
                        LogUtil.i(TAG, sb.toString());
                    } catch (Exception e4) {
                        e = e4;
                        handleException(e);
                        return;
                    }
                } catch (Exception e5) {
                    e = e5;
                    handleException(e);
                    return;
                }
            }
            LluPrivatePrintCount++;
            if (fLight == 0 && fLight != fLightLast) {
                setHighBeamOnOff(1);
            }
            if (fLight == 1 && fLight != fLightLast) {
                setHighBeamOnOff(2);
            }
            if (rfLight == 0 && rfLight != rfLightLast) {
                setBrakeLightOnOff(0);
            }
            if (rfLight == 1 && rfLight != rfLightLast) {
                setBrakeLightOnOff(1);
            }
            int ExpendControl = (expandControl & 64) >> 6;
            if (xpExpandControl && ExpendControl == 1) {
                int lgateCmd = expandControl & 3;
                int rgateCmd = (expandControl & 12) >> 2;
                int windowCmd = (expandControl & 16) >> 4;
                int mirrorCmd = (expandControl & 32) >> 5;
                LogUtil.d(TAG, "" + lgateCmd + " " + rgateCmd + " " + windowCmd + " " + mirrorCmd);
                if (lgateCmd == 1 && lgateCmd != scissorsLgateCmdLast) {
                    setScissorsLeftGateOnOff(1);
                }
                if (lgateCmd == 2 && lgateCmd != scissorsLgateCmdLast) {
                    setScissorsLeftGateOnOff(2);
                }
                if (lgateCmd == 3 && lgateCmd != scissorsLgateCmdLast) {
                    setScissorsLeftGateOnOff(3);
                }
                if (rgateCmd == 1 && rgateCmd != scissorsRgateCmdLast) {
                    setScissorsRightGateOnOff(1);
                }
                if (rgateCmd == 2 && rgateCmd != scissorsRgateCmdLast) {
                    setScissorsRightGateOnOff(2);
                }
                if (rgateCmd == 3 && rgateCmd != scissorsRgateCmdLast) {
                    setScissorsRightGateOnOff(3);
                }
                if (windowCmd == 0 && windowCmd != windowCmdLast) {
                    setWindowOnOff(2);
                }
                if (windowCmd == 1 && windowCmd != windowCmdLast) {
                    setWindowOnOff(1);
                }
                if (mirrorCmd == 0 && mirrorCmd != mirrorCmdLast) {
                    setMirrorOnOff(0);
                }
                if (mirrorCmd == 1 && mirrorCmd != mirrorCmdLast) {
                    setMirrorOnOff(1);
                }
                if ((lgateCmd == 0 || lgateCmd == 1 || lgateCmd == 2 || lgateCmd == 3) && scissorsLgateCmdLast != lgateCmd) {
                    scissorsLgateCmdLast = lgateCmd;
                }
                if ((rgateCmd == 0 || rgateCmd == 1 || rgateCmd == 2 || rgateCmd == 3) && scissorsRgateCmdLast != rgateCmd) {
                    scissorsRgateCmdLast = rgateCmd;
                }
                if ((windowCmd == 0 || windowCmd == 1) && windowCmdLast != windowCmd) {
                    windowCmdLast = windowCmd;
                }
                if ((mirrorCmd == 0 || mirrorCmd == 1) && mirrorCmdLast != mirrorCmd) {
                    mirrorCmdLast = mirrorCmd;
                }
            }
            if ((fLight == 0 || fLight == 1) && fLightLast != fLight) {
                fLightLast = fLight;
            }
            if ((rfLight == 0 || rfLight == 1) && rfLightLast != rfLight) {
                rfLightLast = rfLight;
            }
            setLluPrivateCtrl(start, fType, fTick, rType, rTick);
        } catch (Exception e6) {
            e = e6;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLluPrivateCtrl(boolean start, int fType, int fTick, int rType, int rTick, int fLight, int rfLight) {
        try {
            if (!ig_status_on) {
                if (LluPrivatePrintCount % 20 == 0) {
                    LogUtil.i(TAG, "setLluPrivateCtrl " + ig_status_on);
                }
                LluPrivatePrintCount++;
                return;
            }
            if (LluPrivatePrintCount % 20 == 0) {
                LogUtil.i(TAG, "setLluPrivateCtrl start: " + start + " fType: " + fType + " fTick: " + fTick + " rType: " + rType + " rTick: " + rTick + " fLight: " + fLight + " rfLight: " + rfLight + " fLightLast:" + fLightLast + " rfLightLast:" + rfLightLast);
            }
            LluPrivatePrintCount++;
            if (fLight == 0 && fLight != fLightLast) {
                setHighBeamOnOff(1);
            }
            if (fLight == 1 && fLight != fLightLast) {
                setHighBeamOnOff(2);
            }
            if (rfLight == 0 && rfLight != rfLightLast) {
                setBrakeLightOnOff(0);
            }
            if (rfLight == 1 && rfLight != rfLightLast) {
                setBrakeLightOnOff(1);
            }
            if ((fLight == 0 || fLight == 1) && fLightLast != fLight) {
                fLightLast = fLight;
            }
            if ((rfLight == 0 || rfLight == 1) && rfLightLast != rfLight) {
                rfLightLast = rfLight;
            }
            setLluPrivateCtrl(start, fType, fTick, rType, rTick);
        } catch (Exception e) {
            handleException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLluToInitMode() {
        if (XUIConfig.getLluType() == 0) {
            setLluPrivateCtrl(false, -1, -1, -1, -1, -1, -1);
        } else {
            setInfiniteLluPrivateCtrl(true, "", "", -1, -1, "");
        }
    }

    private void setLluSelfActiveMode(boolean active) {
        try {
            int i = 0;
            if (XUIConfig.getLluType() == 1) {
                int activeMode = active ? 1 : 0;
                this.mCarLluManager.setMcuLluModeCtrl(activeMode, activeMode, activeMode, activeMode, activeMode, activeMode);
                return;
            }
            CarLluManager carLluManager = this.mCarLluManager;
            if (active) {
                CarLluManager carLluManager2 = this.mCarLluManager;
                i = 1;
            } else {
                CarLluManager carLluManager3 = this.mCarLluManager;
            }
            carLluManager.setLluSelfActive(i);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void setLluPrivateCtrl(boolean start, int fType, int fTick, int rType, int rTick) throws Exception {
        CarLluManager carLluManager = this.mCarLluManager;
        if (carLluManager != null && ig_status_on) {
            carLluManager.setLluPrivateCtrl(start, fType, fTick, rType, rTick);
        }
    }

    public void setHighBeamOnOff(int type) {
        try {
            LogUtil.i(TAG, "setHighBeamOnOff " + type);
            if (this.mCarBcmManager != null) {
                this.mCarBcmManager.setHighBeamOnOff(type);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void setBrakeLightOnOff(int type) {
        try {
            LogUtil.i(TAG, "setBrakeLightOnOff " + type);
            if (this.mCarVcuManager != null) {
                this.mCarVcuManager.setBrakeLightOnOff(type);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void setWindowOnOff(int type) {
        try {
            if (this.mCarBcmManager != null) {
                int pos = 0;
                if (type == 1) {
                    pos = 100;
                }
                this.mCarBcmManager.setWindowsMovePositions(pos, pos, -1.0f, -1.0f);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void setScissorsLeftGateOnOff(int type) {
        try {
            if (this.mCarBcmManager != null) {
                this.mCarBcmManager.setLeftSdcAutoControl(type);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void setScissorsRightGateOnOff(int type) {
        try {
            if (this.mCarBcmManager != null) {
                this.mCarBcmManager.setRightSdcAutoControl(type);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void setMirrorOnOff(int type) {
        try {
            if (this.mCarBcmManager != null) {
                LogUtil.d(TAG, "setMirrorOnOff :" + type);
                this.mCarBcmManager.setRearViewMirror(type);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCustomLightEffectFinish() {
        LogUtil.d(TAG, "Custom Light Effect Show has finished ");
        int i = 0;
        while (true) {
            if (i >= this.SHOW_OFF_MODE_01.size()) {
                break;
            }
            String autoSayhiName = this.SHOW_OFF_MODE_01.get(i);
            String str = this.currentEffectName;
            if (str == null || !str.equals(autoSayhiName)) {
                i++;
            } else {
                stopSayHi();
                break;
            }
        }
        String str2 = this.currentEffectName;
        if (str2 != null && str2.equals(this.curDanceEffect) && this.danceLoop != 0) {
            LogUtil.d(TAG, "Light Dance need to loop, do not finish " + this.lightLoop);
            return;
        }
        turnOffAndRestoreLight(3, false);
        callBackLluShowStatusToClient(3, this.currentEffectName, this.currentEffectType, 0);
    }

    public void doMcuLangLightEffect(int effectName, int effectMode) {
        int effectId = (effectMode << 16) + effectName;
        this.frontDataFinish = false;
        this.frontIndex = 0;
        this.frontIndexNum = 0;
        this.rearIndex = 0;
        this.rearIndexNum = 0;
        LogUtil.i(TAG, "setMcuLangLightEffect effectName:" + effectName + " effectId:" + effectId + " effectMode:" + effectMode);
        this.mcuLightEffect = null;
        if (this.mLangLightEffectList.size() > 0) {
            int i = 0;
            while (true) {
                if (i >= this.mLangLightEffectList.size()) {
                    break;
                }
                LogUtil.i(TAG, "mcuLightEffect i:" + i + " effectId:" + this.mLangLightEffectList.get(i).getEffect_id());
                if (this.mLangLightEffectList.get(i).getEffect_id() != effectId) {
                    i++;
                } else {
                    this.mcuLightEffect = this.mLangLightEffectList.get(i);
                    break;
                }
            }
        }
        LangLightEffect langLightEffect = this.mcuLightEffect;
        if (langLightEffect != null) {
            this.frontData = new int[langLightEffect.getFront_Effect().getData().length];
            this.rearData = new int[this.mcuLightEffect.getRear_Effect().getData().length];
            System.arraycopy(this.mcuLightEffect.getFront_Effect().getData(), 0, this.frontData, 0, this.mcuLightEffect.getFront_Effect().getData().length);
            System.arraycopy(this.mcuLightEffect.getRear_Effect().getData(), 0, this.rearData, 0, this.mcuLightEffect.getRear_Effect().getData().length);
            setLluScriptStRequest(1);
        }
    }

    public void doSetLightEffect(int effectName, int effectMode) {
        LogUtil.i(TAG, "doSetLightEffect effectName: " + effectName + " effectMode " + effectMode);
        this.currentMcuEffectName = effectName;
        this.currentMcuEffectMode = effectMode;
        int i = 0;
        if (effectName == 1) {
            try {
                CarLluManager carLluManager = this.mCarLluManager;
                if (effectMode == 0) {
                    CarLluManager carLluManager2 = this.mCarLluManager;
                } else {
                    CarLluManager carLluManager3 = this.mCarLluManager;
                    i = 1;
                }
                carLluManager.setLluFindCarSwitch(i);
                if (effectMode == 1) {
                    CarLluManager carLluManager4 = this.mCarLluManager;
                    CarLluManager carLluManager5 = this.mCarLluManager;
                    carLluManager4.setLluFindCarSwitch(2);
                }
            } catch (Exception e) {
                handleException(e);
            }
        } else if (effectName == 2) {
            try {
                CarLluManager carLluManager6 = this.mCarLluManager;
                if (effectMode == 0) {
                    CarLluManager carLluManager7 = this.mCarLluManager;
                } else {
                    CarLluManager carLluManager8 = this.mCarLluManager;
                    i = 1;
                }
                carLluManager6.setMcuLluWakeWaitSwitch(i);
                if (effectMode == 1) {
                    CarLluManager carLluManager9 = this.mCarLluManager;
                    CarLluManager carLluManager10 = this.mCarLluManager;
                    carLluManager9.setMcuLluWakeWaitSwitch(2);
                }
            } catch (Exception e2) {
                handleException(e2);
            }
        } else if (effectName == 5) {
            try {
                CarLluManager carLluManager11 = this.mCarLluManager;
                if (effectMode == 0) {
                    CarLluManager carLluManager12 = this.mCarLluManager;
                } else {
                    CarLluManager carLluManager13 = this.mCarLluManager;
                    i = 1;
                }
                carLluManager11.setMcuLluSleepSwitch(i);
                if (effectMode == 1) {
                    CarLluManager carLluManager14 = this.mCarLluManager;
                    CarLluManager carLluManager15 = this.mCarLluManager;
                    carLluManager14.setMcuLluSleepSwitch(2);
                }
            } catch (Exception e3) {
                handleException(e3);
            }
        } else if (effectName != 6) {
            if (effectName != 7) {
                if (effectName != 9) {
                    if (effectName != 10) {
                        LogUtil.i(TAG, "setLightEffect unhandle effectName: " + effectName + " effectMode " + effectMode);
                    }
                } else {
                    try {
                        CarLluManager carLluManager16 = this.mCarLluManager;
                        if (effectMode == 0) {
                            CarLluManager carLluManager17 = this.mCarLluManager;
                        } else {
                            CarLluManager carLluManager18 = this.mCarLluManager;
                            i = 1;
                        }
                        carLluManager16.setMcuLluPhotoSwitch(i);
                        if (effectMode == 1) {
                            CarLluManager carLluManager19 = this.mCarLluManager;
                            CarLluManager carLluManager20 = this.mCarLluManager;
                            carLluManager19.setMcuLluPhotoSwitch(2);
                        }
                    } catch (Exception e4) {
                        handleException(e4);
                    }
                }
            } else if (effectMode == 1) {
                try {
                    CarLluManager carLluManager21 = this.mCarLluManager;
                    CarLluManager carLluManager22 = this.mCarLluManager;
                    carLluManager21.setMcuLluChargingSwitch(3);
                } catch (Exception e5) {
                    handleException(e5);
                }
            }
        } else if (effectMode == 1) {
            try {
                CarLluManager carLluManager23 = this.mCarLluManager;
                CarLluManager carLluManager24 = this.mCarLluManager;
                carLluManager23.setMcuLluChargingSwitch(2);
            } catch (Exception e6) {
                handleException(e6);
            }
        }
        if (effectMode != 0 && effectMode != 1) {
            setMcuLangLightEffect(effectName, effectMode);
        }
        if (effectName == 10 && effectMode >= 1) {
            this.mSayHiMode = effectMode;
            this.mSayHiEffect = this.SHOW_OFF_MODE_01.get(this.mSayHiMode);
        }
    }

    public void doMusicSpectrumToLangLight(float ratio) {
        String sfType;
        String srType;
        int fType;
        int fTick;
        int rType;
        int rTick;
        if (this.musicSpecrhythmTimeStamp < System.currentTimeMillis() - 1000) {
            updateTimer(true);
            resetPirvateLluControl();
        }
        this.musicSpecrhythmTimeStamp = System.currentTimeMillis();
        tickLluStopTime();
        if (XUIConfig.getLluType() == 0) {
            int[] frontData = this.musicLightEffect.getFront_Effect().getData();
            int[] rearData = this.musicLightEffect.getRear_Effect().getData();
            if (frontData.length != 0) {
                int fnum = Math.round((frontData.length - 1) * ratio);
                int fType2 = (frontData[fnum] & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                int fTick2 = frontData[fnum] & 255;
                fType = fType2;
                fTick = fTick2;
            } else {
                fType = -1;
                fTick = -1;
            }
            int fnum2 = rearData.length;
            if (fnum2 != 0) {
                int rnum = Math.round((rearData.length - 1) * ratio);
                int rType2 = (rearData[rnum] & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                rType = rType2;
                rTick = rearData[rnum] & 255;
            } else {
                rType = -1;
                rTick = -1;
            }
            setLluPrivateCtrl(true, fType, fTick, rType, rTick, -1, -1);
        } else if (XUIConfig.getLluType() == 1) {
            this.musicLightEffect.getFront_Effect().getPeriod();
            this.musicLightEffect.getFront_Effect().getRetain();
            int frontLoop = this.musicLightEffect.getFront_Effect().getLoop();
            String[] sfrontData = copyStrArray(this.musicLightEffect.getFront_Effect().getSdata(), frontLoop);
            this.musicLightEffect.getRear_Effect().getPeriod();
            this.musicLightEffect.getRear_Effect().getRetain();
            int rearLoop = this.musicLightEffect.getRear_Effect().getLoop();
            String[] srearData = copyStrArray(this.musicLightEffect.getRear_Effect().getSdata(), rearLoop);
            if (sfrontData.length != 0) {
                int fnum3 = Math.round((sfrontData.length - 1) * ratio);
                sfType = sfrontData[fnum3].substring(0, INFINITE_FRONTLIGHT_COUNT);
            } else {
                sfType = "";
            }
            if (srearData.length != 0) {
                srType = srearData[Math.round((srearData.length - 1) * ratio)].substring(0, INFINITE_REARLIGHT_COUNT);
            } else {
                srType = "";
            }
            setInfiniteLluPrivateCtrl(true, sfType, srType, -1, -1, "");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doStartLangLightEffectShow(String effectName, int loop, boolean needPGearLimit) {
        LogUtil.i(TAG, "doStartLangLightEffectShow effectName: " + effectName + ", isLLUEnable:" + this.isLLUEnable + ", isPGear:" + this.isPGear + " isRearFogOn:" + this.isRearFogOn + ", loop:" + loop + " " + this.isLLUFftEnable);
        removePlayLoopEvent(false);
        this.lightLoop = loop;
        if (!this.isLLUEnable || this.isRearFogOn || (needPGearLimit && !this.isPGear)) {
            LangLightEffect LLEffect = getLangLightEffectByName(effectName);
            String effectType = "";
            if (LLEffect != null) {
                effectType = LLEffect.getEffect_type();
            }
            callBackLluShowStatusToClient(4, effectName, effectType, -4);
            sendEventMessage(5, 0, 0, "", 200);
            return;
        }
        LogUtil.d(TAG, "doStartLangLightEffectShow  currentEffectName:" + this.currentEffectName + " " + effectName);
        String str = this.currentEffectName;
        if (str != null && !str.equals(effectName)) {
            callBackLluShowStatusToClient(2, this.currentEffectName, this.currentEffectType, 0);
        }
        last_startLanglight_time = System.currentTimeMillis();
        if (!checkMediaNameExist(effectName)) {
            doStopDanceMusic();
            this.danceLoop = 0;
        }
        LangLightEffect LLEffect2 = getLangLightEffectByName(effectName);
        if (LLEffect2 != null) {
            if (LLEffect2.getNotResetFlag() != 1) {
                updateTimer(false);
            }
            doLangLightEffectMode(LLEffect2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetLluModeAndPlayLightEffect(LangLightEffect LLEffect, int loop) {
        try {
            if (LLEffect.getNotResetFlag() != 1) {
                resetPirvateLluControl();
                Thread.sleep(200L);
                turnOffAndRestoreLight(3, true);
                sendLightEffectEventMessage(1, loop, LLEffect, "", 250);
            } else {
                sendLightEffectEventMessage(1, loop, LLEffect, "", 0);
            }
        } catch (Exception e) {
            LogUtil.w(TAG, "resetLluModeAndPlayLightEffect  " + e);
        }
    }

    private boolean checkIsSpecialMode(LangLightEffect LLEffect) {
        LogUtil.d(TAG, "checkIsSpecialMode :" + LLEffect.getSpecial_mode());
        return LLEffect.getSpecial_mode() != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doSetSpecialMode(int activeMode, int duration, boolean on) {
        LogUtil.d(TAG, "doSetSpecialMode  " + activeMode + " " + duration + " " + on);
        if (XUIConfig.getLluType() == 1) {
            removeDelayedEvent(7);
            this.isInSpecialMode = on;
            try {
                if (on) {
                    this.mCarLluManager.setMcuLluModeCtrl(activeMode, activeMode, activeMode, activeMode, activeMode, activeMode);
                    if (duration > 0) {
                        sendEventMessage(7, 0, 0, "", duration);
                    }
                } else {
                    this.mCarLluManager.setMcuLluModeCtrl(0, 0, 0, 0, 0, 0);
                    deactivateAndroidLluControl();
                    callBackLluShowStatusToClient(3, this.currentEffectName, this.currentEffectType, 0);
                }
            } catch (Exception e) {
                LogUtil.e(TAG, "doSetSpecialMode  e:" + e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doLangLightEffectMode(final LangLightEffect LLEffect) {
        if (this.isInSpecialMode) {
            doSetSpecialMode(0, 0, false);
        }
        this.currentEffectName = LLEffect.getEffect_name();
        this.currentEffectType = LLEffect.getEffect_type();
        callBackLluShowStatusToClient(1, this.currentEffectName, this.currentEffectType, 0);
        LogUtil.d(TAG, "doLangLightEffectMode  getNotResetFlag:" + LLEffect.getNotResetFlag());
        if (LLEffect.getNotResetFlag() != 1) {
            resetPirvateLluControl();
        } else {
            LogUtil.w(TAG, "doLangLightEffectMode  getNotResetFlag:" + LLEffect.getNotResetFlag());
        }
        String str = this.currentEffectName;
        if (str != null && !str.equals(this.mSayHiEffect) && !this.currentEffectName.contains("android_sayhi") && !this.currentEffectName.contains(X_SAYHI_LLU) && !this.isPGear && !xpExpandControl) {
            callBackLluShowStatusToClient(4, this.currentEffectName, this.currentEffectType, -4);
            sendEventMessage(5, 0, 0, "", 200);
            return;
        }
        if (!this.brokenFftLlu) {
            this.brokenFftLlu = true;
        }
        if (checkIsSpecialMode(LLEffect)) {
            doSetSpecialMode(LLEffect.getSpecial_mode(), LLEffect.getSpecial_duration(), true);
            return;
        }
        LangLightTask mtimerTask = new LangLightTask() { // from class: com.xiaopeng.xuiservice.smart.SmartLluService.15
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.xiaopeng.xuiservice.smart.SmartLluService.LangLightTask, java.util.TimerTask, java.lang.Runnable
            public void run() {
                if (XUIConfig.getLluType() == 1) {
                    infinite_llu_task();
                } else {
                    dictionary_llu_task();
                }
            }

            private void dictionary_llu_task() {
                int fType;
                int fTick;
                int rType;
                int rTick;
                SmartLluService.this.musicSpectrumIntrupt = true;
                SmartLluService.this.tickLluTime();
                int fLight = 0;
                int rLight = 0;
                int expandCmd = 0;
                this.count++;
                if (this.frontData != null && this.rearData != null) {
                    if (this.frontNum > this.frontData.length && this.rearNum > this.rearData.length) {
                        if (this.frontRetain == 1 || this.rearRetain == 1) {
                            SmartLluService.this.untickLluTime();
                        }
                        if (SmartLluService.this.mAudioManager.isMusicActive()) {
                            SmartLluService.this.tickLluTime();
                        }
                        SmartLluService.this.musicSpectrumIntrupt = false;
                        cancel();
                        if (SmartLluService.this.danceLoop == 0) {
                            LogUtil.d(SmartLluService.TAG, "dictionary_llu_task  LAST TICK");
                            SmartLluService.this.removePlayLoopEvent(true);
                            if (SmartLluService.this.lightLoop > 0) {
                                SmartLluService.access$3810(SmartLluService.this);
                            }
                            if (SmartLluService.this.lightLoop == 0) {
                                SmartLluService.this.handleCustomLightEffectFinish();
                            } else {
                                SmartLluService.this.sendReplayLightEffectEvent(1, LLEffect, "");
                            }
                        }
                    }
                    if ((this.count * 10) % this.frontPeriod != 0 || (this.count * 10) % this.rearPeriod != 0) {
                        if ((this.count * 10) % this.frontPeriod == 0) {
                            if (this.frontNum < this.frontData.length) {
                                int fType2 = (this.frontData[this.frontNum] & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                                int fTick2 = this.frontData[this.frontNum] & 255;
                                this.oldfType = fType2;
                                this.oldfTick = fTick2;
                                SmartLluService.this.setLluPrivateCtrl(true, fType2, fTick2, this.oldrType, this.oldrTick, (this.frontData[this.frontNum] & 65536) >> 16, -1, (this.frontData[this.frontNum] & (-131072)) >> 17);
                                this.frontNum++;
                                return;
                            }
                            return;
                        } else if ((this.count * 10) % this.rearPeriod == 0 && this.rearNum < this.rearData.length) {
                            int fType3 = this.oldfType;
                            int fTick3 = this.oldfTick;
                            int rType2 = (this.rearData[this.rearNum] & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                            int rTick2 = this.rearData[this.rearNum] & 255;
                            this.oldrType = rType2;
                            this.oldrTick = rTick2;
                            SmartLluService.this.setLluPrivateCtrl(true, fType3, fTick3, rType2, rTick2, -1, (this.rearData[this.rearNum] & 65536) >> 16);
                            this.rearNum++;
                            return;
                        } else {
                            return;
                        }
                    }
                    if (this.frontNum < this.frontData.length) {
                        fType = (this.frontData[this.frontNum] & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                        fTick = this.frontData[this.frontNum] & 255;
                        fLight = (this.frontData[this.frontNum] & 65536) >> 16;
                        expandCmd = (this.frontData[this.frontNum] & (-131072)) >> 17;
                        this.oldfType = fType;
                        this.oldfTick = fTick;
                    } else if (this.frontRetain == 1) {
                        fType = this.oldfType;
                        fTick = this.oldfTick;
                    } else {
                        this.oldfType = 0;
                        this.oldfTick = 0;
                        fType = -1;
                        fTick = -1;
                    }
                    if (this.rearNum < this.rearData.length) {
                        rType = (this.rearData[this.rearNum] & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                        rTick = this.rearData[this.rearNum] & 255;
                        rLight = (this.rearData[this.rearNum] & 65536) >> 16;
                        this.oldrType = rType;
                        this.oldrTick = rTick;
                    } else if (this.rearRetain == 1) {
                        rType = this.oldrType;
                        rTick = this.oldrTick;
                    } else {
                        this.oldrType = 0;
                        this.oldrTick = 0;
                        rType = -1;
                        rTick = -1;
                    }
                    this.frontNum++;
                    this.rearNum++;
                    if (fType != -1 || fTick != -1 || rType != -1 || rTick != -1) {
                        SmartLluService.this.setLluPrivateCtrl(true, fType, fTick, rType, rTick, fLight, rLight, expandCmd);
                        return;
                    }
                    SmartLluService.this.setLluPrivateCtrl(false, fType, fTick, rType, rTick, fLight, rLight);
                    SmartLluService.this.musicSpectrumIntrupt = false;
                }
            }

            private void infinite_llu_task() {
                boolean fLight;
                String sfType;
                int fLight2;
                String srType;
                SmartLluService.this.musicSpectrumIntrupt = true;
                SmartLluService.this.tickLluTime();
                int rLight = 0;
                this.count++;
                if (this.srearData != null) {
                    if (this.frontNum > this.sfrontData.length && this.rearNum > this.srearData.length) {
                        if (this.frontRetain == 1 || this.rearRetain == 1) {
                            SmartLluService.this.untickLluTime();
                        } else {
                            SmartLluService.this.setLluToInitMode();
                            SmartLluService.this.deactivateAndroidLluControl();
                        }
                        if (SmartLluService.this.mAudioManager.isMusicActive()) {
                            SmartLluService.this.tickLluTime();
                        }
                        SmartLluService.this.musicSpectrumIntrupt = false;
                        cancel();
                        if (SmartLluService.this.danceLoop == 0) {
                            SmartLluService.this.removePlayLoopEvent(true);
                            if (SmartLluService.this.lightLoop > 0) {
                                SmartLluService.access$3810(SmartLluService.this);
                            }
                            if (SmartLluService.this.lightLoop == 0) {
                                SmartLluService.this.handleCustomLightEffectFinish();
                            } else {
                                SmartLluService.this.sendReplayLightEffectEvent(1, LLEffect, "");
                            }
                        }
                    }
                    if ((this.count * 10) % this.frontPeriod != 0 || (this.count * 10) % this.rearPeriod != 0) {
                        if ((this.count * 10) % this.frontPeriod == 0) {
                            if (this.frontNum < this.sfrontData.length) {
                                String sfType2 = this.sfrontData[this.frontNum].substring(0, SmartLluService.INFINITE_FRONTLIGHT_COUNT);
                                if (this.sfrontData[this.frontNum].length() > SmartLluService.INFINITE_FRONTLIGHT_COUNT) {
                                    fLight = this.sfrontData[this.frontNum].substring(SmartLluService.INFINITE_FRONTLIGHT_COUNT, SmartLluService.INFINITE_FRONTLIGHT_COUNT + 1).equals("1");
                                } else {
                                    fLight = 0;
                                }
                                this.oldsfType = sfType2;
                                SmartLluService.this.setInfiniteLluPrivateCtrl(true, sfType2, this.oldsrType, fLight, -1, "");
                                this.frontNum++;
                                return;
                            }
                            return;
                        } else if ((this.count * 10) % this.rearPeriod == 0 && this.rearNum < this.srearData.length) {
                            String sfType3 = this.oldsfType;
                            boolean rLight2 = 0;
                            String srType2 = this.srearData[this.rearNum].substring(0, SmartLluService.INFINITE_REARLIGHT_COUNT);
                            if (this.srearData[this.rearNum].length() > SmartLluService.INFINITE_REARLIGHT_COUNT) {
                                rLight2 = this.srearData[this.rearNum].substring(SmartLluService.INFINITE_REARLIGHT_COUNT, SmartLluService.INFINITE_REARLIGHT_COUNT + 1).equals("1");
                            }
                            this.oldsrType = srType2;
                            SmartLluService.this.setInfiniteLluPrivateCtrl(true, sfType3, srType2, -1, rLight2, "");
                            this.rearNum++;
                            return;
                        } else {
                            return;
                        }
                    }
                    if (this.frontNum < this.sfrontData.length) {
                        sfType = this.sfrontData[this.frontNum].substring(0, SmartLluService.INFINITE_FRONTLIGHT_COUNT);
                        boolean fLight3 = 0;
                        if (this.sfrontData[this.frontNum].length() > SmartLluService.INFINITE_FRONTLIGHT_COUNT) {
                            fLight3 = this.sfrontData[this.frontNum].substring(SmartLluService.INFINITE_FRONTLIGHT_COUNT, SmartLluService.INFINITE_FRONTLIGHT_COUNT + 1).equals("1");
                        }
                        this.oldsfType = sfType;
                        fLight2 = fLight3;
                    } else if (this.frontRetain == 1) {
                        sfType = this.oldsfType;
                        fLight2 = 0;
                    } else {
                        this.oldsfType = "";
                        sfType = "";
                        fLight2 = 0;
                    }
                    int fLight4 = this.rearNum;
                    if (fLight4 < this.srearData.length) {
                        srType = this.srearData[this.rearNum].substring(0, SmartLluService.INFINITE_REARLIGHT_COUNT);
                        boolean rLight3 = 0;
                        if (this.srearData[this.rearNum].length() > SmartLluService.INFINITE_REARLIGHT_COUNT) {
                            rLight3 = this.srearData[this.rearNum].substring(SmartLluService.INFINITE_REARLIGHT_COUNT, SmartLluService.INFINITE_REARLIGHT_COUNT + 1).equals("1");
                        }
                        this.oldsrType = srType;
                        rLight = rLight3;
                    } else if (this.rearRetain == 1) {
                        srType = this.oldsrType;
                    } else {
                        this.oldsrType = "";
                        srType = "";
                    }
                    this.frontNum++;
                    this.rearNum++;
                    if (!sfType.equals("") || !srType.equals("")) {
                        SmartLluService.this.setInfiniteLluPrivateCtrl(true, sfType, srType, fLight2, rLight, "");
                    } else {
                        SmartLluService.this.setLluToInitMode();
                        SmartLluService.this.musicSpectrumIntrupt = false;
                    }
                }
            }
        };
        if (this.timer != null) {
            synchronized (timerLock) {
                try {
                    mtimerTask.init(LLEffect);
                    this.timer.scheduleAtFixedRate(mtimerTask, 0L, 10L);
                } catch (Exception e) {
                    handleException(e);
                    resetLoopFlag();
                    handleCustomLightEffectFinish();
                }
            }
            return;
        }
        LogUtil.w(TAG, "doLangLightEffectMode timer == null");
        resetLoopFlag();
        handleCustomLightEffectFinish();
    }

    private void sendPacket() {
        int[] iArr;
        int dataLength;
        int[] iArr2;
        int dataLength2;
        if (!this.frontDataFinish) {
            int[] iArr3 = this.frontData;
            if (iArr3.length > 0) {
                if (iArr3.length % 60 == 0) {
                    this.frontIndex = iArr3.length / 60;
                } else {
                    this.frontIndex = (iArr3.length / 60) + 1;
                }
                int i = this.frontIndexNum;
                int i2 = this.frontIndex;
                if (i < i2) {
                    if (i != i2 - 1) {
                        dataLength2 = 60;
                        System.arraycopy(this.frontData, i * 60, this.fData, 0, 60);
                    } else {
                        int[] iArr4 = this.frontData;
                        if (iArr4.length % 60 == 0) {
                            dataLength2 = 60;
                            System.arraycopy(iArr4, i * 60, this.fData, 0, 60);
                        } else {
                            int i3 = 0;
                            while (true) {
                                iArr2 = this.fData;
                                if (i3 >= iArr2.length) {
                                    break;
                                }
                                iArr2[i3] = 0;
                                i3++;
                            }
                            int[] iArr5 = this.frontData;
                            int dataLength3 = iArr5.length % 60;
                            int dataLength4 = this.frontIndexNum;
                            System.arraycopy(iArr5, dataLength4 * 60, iArr2, 0, iArr5.length % 60);
                            dataLength2 = dataLength3;
                        }
                    }
                    sendLluScriptData(this.frontIndexNum, 0, dataLength2, this.fData);
                    if (this.frontData.length <= 60) {
                        this.frontDataFinish = true;
                    } else {
                        this.frontIndexNum++;
                    }
                }
            } else {
                this.frontDataFinish = true;
            }
        }
        if (this.frontDataFinish) {
            int[] iArr6 = this.rearData;
            if (iArr6.length > 0) {
                if (iArr6.length % 60 == 0) {
                    this.rearIndex = iArr6.length / 60;
                } else {
                    this.rearIndex = (iArr6.length / 60) + 1;
                }
                int i4 = this.rearIndexNum;
                int i5 = this.rearIndex;
                if (i4 < i5) {
                    if (i4 != i5 - 1) {
                        dataLength = 60;
                        System.arraycopy(this.rearData, i4 * 60, this.rData, 0, 60);
                    } else {
                        int[] iArr7 = this.rearData;
                        if (iArr7.length % 60 == 0) {
                            dataLength = 60;
                            System.arraycopy(iArr7, i4 * 60, this.rData, 0, 60);
                        } else {
                            int i6 = 0;
                            while (true) {
                                iArr = this.rData;
                                if (i6 >= iArr.length) {
                                    break;
                                }
                                iArr[i6] = 0;
                                i6++;
                            }
                            int[] iArr8 = this.rearData;
                            int dataLength5 = iArr8.length % 60;
                            int dataLength6 = this.rearIndexNum;
                            System.arraycopy(iArr8, dataLength6 * 60, iArr, 0, iArr8.length % 60);
                            dataLength = dataLength5;
                        }
                    }
                    sendLluScriptData(this.rearIndexNum, 1, dataLength, this.rData);
                    if (this.rearData.length > 60) {
                        this.rearIndexNum++;
                        return;
                    }
                    return;
                }
                return;
            }
            setLluScriptStRequest(2);
        }
    }
}
