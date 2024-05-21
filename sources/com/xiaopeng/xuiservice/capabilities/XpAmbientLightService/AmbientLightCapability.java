package com.xiaopeng.xuiservice.capabilities.XpAmbientLightService;

import android.app.ActivityThread;
import android.car.hardware.atl.AtlConfiguration;
import android.car.hardware.atl.CarAtlManager;
import android.car.hardware.bcm.CarBcmManager;
import android.content.Context;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import androidx.core.app.NotificationCompat;
import com.car.opensdk.resourcemanager.CarResourceInfo;
import com.car.opensdk.resourcemanager.CarResourceManager;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.xiaopeng.biutil.BiLog;
import com.xiaopeng.biutil.BiLogFactory;
import com.xiaopeng.speech.protocol.bean.recommend.RecommendBean;
import com.xiaopeng.xuimanager.mediacenter.MediaCenterManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.carcontrol.HalServiceBaseCarListener;
import com.xiaopeng.xuiservice.innerutils.BiLogTransmit;
import com.xiaopeng.xuiservice.innerutils.DataLogUtils;
import com.xiaopeng.xuiservice.mediacenter.visualizer.VisualizerService;
import com.xiaopeng.xuiservice.utils.CommonUtils;
import com.xiaopeng.xuiservice.utils.PackageUtils;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.json.JSONArray;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class AmbientLightCapability extends HalServiceBaseCarListener {
    private static final int AMBIENTLIGHT_DEFAULT_FADE = SystemProperties.getInt("persist.atl.def.fadetime", 0);
    private static final int AMBIENTLIGHT_LIGHT_VAL = SystemProperties.getInt("persist.atl.light.percent", 20);
    private static final int AMBIENTLIGHT_RECOVERY_FADE = 100;
    private static final int AMBIENTLIGHT_SPEED_FADE = 2;
    private static final int AMBIENTLIGHT_SPEED_LONG_FADE = 100;
    private static final boolean DBG = true;
    public static final int EVENT_BRIGHT = 5;
    public static final int EVENT_COLORTYPE = 6;
    public static final int EVENT_DOUBLECOLOR = 4;
    public static final int EVENT_EFFECT = 2;
    public static final int EVENT_MODE = 1;
    public static final int EVENT_MONOCOLOR = 3;
    public static final int EVENT_POWER = 0;
    public static final int LIGHT_COLOR_1 = 1;
    public static final int LIGHT_COLOR_10 = 10;
    public static final int LIGHT_COLOR_11 = 11;
    public static final int LIGHT_COLOR_12 = 12;
    public static final int LIGHT_COLOR_13 = 13;
    public static final int LIGHT_COLOR_14 = 14;
    public static final int LIGHT_COLOR_15 = 15;
    public static final int LIGHT_COLOR_16 = 16;
    public static final int LIGHT_COLOR_17 = 17;
    public static final int LIGHT_COLOR_18 = 18;
    public static final int LIGHT_COLOR_19 = 19;
    public static final int LIGHT_COLOR_2 = 2;
    public static final int LIGHT_COLOR_20 = 20;
    public static final int LIGHT_COLOR_3 = 3;
    public static final int LIGHT_COLOR_4 = 4;
    public static final int LIGHT_COLOR_5 = 5;
    public static final int LIGHT_COLOR_6 = 6;
    public static final int LIGHT_COLOR_7 = 7;
    public static final int LIGHT_COLOR_8 = 8;
    public static final int LIGHT_COLOR_9 = 9;
    public static final int LIGHT_GROUP_0 = 0;
    public static final int LIGHT_GROUP_1 = 1;
    public static final int LIGHT_GROUP_10 = 10;
    public static final int LIGHT_GROUP_11 = 11;
    public static final int LIGHT_GROUP_12 = 12;
    public static final int LIGHT_GROUP_13 = 13;
    public static final int LIGHT_GROUP_14 = 14;
    public static final int LIGHT_GROUP_15 = 15;
    public static final int LIGHT_GROUP_16 = 16;
    public static final int LIGHT_GROUP_17 = 17;
    public static final int LIGHT_GROUP_18 = 18;
    public static final int LIGHT_GROUP_19 = 19;
    public static final int LIGHT_GROUP_2 = 2;
    public static final int LIGHT_GROUP_20 = 20;
    public static final int LIGHT_GROUP_21 = 21;
    public static final int LIGHT_GROUP_22 = 22;
    public static final int LIGHT_GROUP_23 = 23;
    public static final int LIGHT_GROUP_24 = 24;
    public static final int LIGHT_GROUP_25 = 25;
    public static final int LIGHT_GROUP_26 = 26;
    public static final int LIGHT_GROUP_27 = 27;
    public static final int LIGHT_GROUP_28 = 28;
    public static final int LIGHT_GROUP_29 = 29;
    public static final int LIGHT_GROUP_3 = 3;
    public static final int LIGHT_GROUP_30 = 30;
    public static final int LIGHT_GROUP_31 = 31;
    public static final int LIGHT_GROUP_4 = 4;
    public static final int LIGHT_GROUP_5 = 5;
    public static final int LIGHT_GROUP_6 = 6;
    public static final int LIGHT_GROUP_7 = 7;
    public static final int LIGHT_GROUP_8 = 8;
    public static final int LIGHT_GROUP_9 = 9;
    private static final String MODULE_NAME = "AmbientLight";
    public static final int MSG_DEFAULT_EFFECT = 1;
    public static final int NORMAL_EFFECT = 1;
    private static final String RESOURCE_TYPE = "Ambient Light";
    public static final int SCRIPT_EFFECT = 2;
    public static final int SPEECH_STATE_LISTENING = 2;
    public static final int SPEECH_STATE_NO_VOICE = 3;
    public static final int SPEECH_STATE_SPEAKERING = 0;
    public static final int SPEECH_STATE_UNDERSTANDING = 1;
    public static final int SPEECH_VOICE_DRIVER = 1;
    public static final int SPEECH_VOICE_PASSAGER = 2;
    public static final int SPEECH_VOICE_REAR_LEFT_PASSAGER = 3;
    public static final int SPEECH_VOICE_REAR_RIGHT_PASSAGER = 4;
    private static final String TAG = "AmbientLightCapability";
    public static final String TIME_INTERVAL = "timeInterval";
    private Runnable SHC_Change_thread;
    private long SHC_RAISE_DELAY;
    private HashMap<String, ATLEffect> ambientLightEffectMap;
    private float carSpeed;
    private int closeRandomGroup;
    private int currentBright;
    private int currentColor;
    private int currentDelayTime;
    private ATLEffect.EffectData currentEffect;
    private int currentFade;
    private int currentGroupNum;
    private int currentMaxEffectNum;
    private final GroupNum defaultOrMusicGroupNum;
    private int doubleFirstColor;
    private int doubleSecondColor;
    private boolean doubleThemeColorEnable;
    private String effectType;
    private int hvacColor;
    private int hvacDriverBright;
    private int hvacPsnBright;
    private Runnable hvac_timeout_thread;
    private boolean isAdjustHvac;
    private boolean isAmbientLightInit;
    private boolean isCarIgOn;
    private boolean isDriving;
    private boolean isEnableAtlController;
    private boolean isLite;
    private boolean isMusicRhythmMode;
    private volatile boolean isPNGear;
    private boolean isShcRised;
    private boolean isSpeechActive;
    private boolean isStartWelcomeEffect;
    private long lastAtlLogTime;
    private int loopingNum;
    private ATLEffect mATLEffect;
    private List<String> mAmbientLightEffectTypeList;
    private AmbientLightHal mAmbientLightHal;
    private AtlConfiguration mAtlConfiguration;
    private AudioManager mAudioManager;
    private CarAtlManager mCarAtlManager;
    private CarBcmManager mCarBcmManager;
    private CarResourceInfo mCarResourceInfo;
    private CarResourceManager mCarResourceManager;
    private Handler mHandler;
    private AmbientLightHalListener mListener;
    private MediaCenterManager mMediaCenterManager;
    private final List<String> mModeList;
    private final SpeechPara mSpeechPara;
    private MediaCenterManager.VisualCaptureListener mVisualCaptureListener;
    private VisualizerService mVisualizerService;
    private int monoColor;
    private Runnable music_spectrum_timeout_thread;
    private boolean originalOpenStatus;
    private int previousGroupNum;
    private Runnable recovery_color_thread;
    private ScheduledThreadPoolExecutor scriptEffectTimer;
    private boolean speechEnable;
    private int[] speechGroupNum;
    private int speechState;
    private ScheduledThreadPoolExecutor speechTimer;
    private int speechVoicer;

    /* loaded from: classes5.dex */
    public interface AmbientLightHalListener {
        void onAmbientLightSetEvent(int i, String str, int[] iArr);

        void onError(int i, int i2);
    }

    static /* synthetic */ int access$1512(AmbientLightCapability x0, int x1) {
        int i = x0.loopingNum + x1;
        x0.loopingNum = i;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recoveryColorThread() {
        if (!this.isLite) {
            String effectType = getAmbientLightEffectType();
            if (effectType.equals("follow_speed")) {
                setFollowCarSpeedEffect(this.carSpeed, 100);
            } else if (effectType.equals("music") && !isVisualizerEnable()) {
                setMusicSpectrumEffect((byte) this.monoColor, (byte) 10, (byte) 100);
            }
        }
    }

    public void recoveryColor() {
        XuiWorkHandler.getInstance().removeCallbacks(this.recovery_color_thread);
        XuiWorkHandler.getInstance().postDelayed(this.recovery_color_thread, 1000L);
    }

    public void resetColor() {
        XuiWorkHandler.getInstance().removeCallbacks(this.recovery_color_thread);
        XuiWorkHandler.getInstance().postDelayed(this.recovery_color_thread, 50L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void musicSpectrumTimeout() {
        if (getMusicSpectrumEnable() && this.speechState == 3) {
            LogUtil.i(TAG, "music_spectrum_timeout_thread");
            getAmbientLightEffectType();
            setMusicSpectrumEffect((byte) this.monoColor, (byte) 10, (byte) 100);
        }
    }

    public void tickMusicSpectrumTime() {
        XuiWorkHandler.getInstance().removeCallbacks(this.music_spectrum_timeout_thread);
        XuiWorkHandler.getInstance().postDelayed(this.music_spectrum_timeout_thread, 500L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hvacTimeout() {
        this.isAdjustHvac = false;
        setAmbientLightDataEffect(18, "hvacColor", (byte) 0, (byte) 100);
        setAmbientLightDataEffect(19, "hvacColor", (byte) 0, (byte) 100);
        updateAmbientLightEffect();
    }

    public void tickHvacTime() {
        XuiWorkHandler.getInstance().removeCallbacks(this.hvac_timeout_thread);
        XuiWorkHandler.getInstance().postDelayed(this.hvac_timeout_thread, 3000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void SHCChange() {
        boolean z = true;
        if (!this.isShcRised) {
            this.isShcRised = true;
            try {
                if (this.mCarBcmManager != null) {
                    LogUtil.d(TAG, "SHC Rised ");
                    CarBcmManager carBcmManager = this.mCarBcmManager;
                    CarBcmManager carBcmManager2 = this.mCarBcmManager;
                    carBcmManager.setShcReq(1);
                }
            } catch (Exception e) {
                LogUtil.e(TAG, "handleSHCWithCarDoorStateChanged error");
            }
        }
        this.monoColor = getAmbientLightMonoColor(getAmbientLightEffectType());
        sendAtlControl((this.isPNGear && this.isCarIgOn && this.isShcRised && getAmbientLightOpen()) ? false : false);
    }

    public void riseSHC() {
        if (!isWelcomeAutoShow()) {
            XuiWorkHandler.getInstance().postDelayed(this.SHC_Change_thread, this.SHC_RAISE_DELAY * 1000);
        }
    }

    public AmbientLightCapability(Context context) {
        super(context);
        boolean z;
        boolean z2;
        this.mHandler = null;
        this.mModeList = new ArrayList(Arrays.asList("stable_effect", "gentle_breathing", "follow_speed", "music"));
        this.speechVoicer = 1;
        this.speechState = 3;
        this.loopingNum = 0;
        this.lastAtlLogTime = 0L;
        this.SHC_RAISE_DELAY = SystemProperties.getInt("persist.sys.shcraise.delay", 0);
        this.carSpeed = 0.0f;
        this.currentGroupNum = 3;
        this.currentColor = 1;
        this.currentBright = 100;
        this.currentFade = 1000;
        this.currentDelayTime = 1000;
        this.currentMaxEffectNum = 1000;
        this.closeRandomGroup = 1;
        this.previousGroupNum = 3;
        this.speechEnable = true;
        this.isSpeechActive = false;
        this.isAdjustHvac = false;
        this.isDriving = false;
        this.isCarIgOn = false;
        this.isShcRised = false;
        this.isMusicRhythmMode = false;
        this.isAmbientLightInit = false;
        this.originalOpenStatus = false;
        if (XUIConfig.getAtlType() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.isLite = z;
        if (XUIConfig.getAtlType() == 2) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.isEnableAtlController = z2;
        this.isPNGear = false;
        this.isStartWelcomeEffect = false;
        this.ambientLightEffectMap = new HashMap<>();
        this.recovery_color_thread = new Runnable() { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightCapability.1
            @Override // java.lang.Runnable
            public void run() {
                AmbientLightCapability.this.recoveryColorThread();
            }
        };
        this.music_spectrum_timeout_thread = new Runnable() { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightCapability.2
            @Override // java.lang.Runnable
            public void run() {
                AmbientLightCapability.this.musicSpectrumTimeout();
            }
        };
        this.hvac_timeout_thread = new Runnable() { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightCapability.3
            @Override // java.lang.Runnable
            public void run() {
                AmbientLightCapability.this.hvacTimeout();
                if (!AmbientLightCapability.this.isVisualizerEnable()) {
                    AmbientLightCapability.this.tickMusicSpectrumTime();
                }
            }
        };
        this.SHC_Change_thread = new Runnable() { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightCapability.4
            @Override // java.lang.Runnable
            public void run() {
                AmbientLightCapability.this.SHCChange();
            }
        };
        LogUtil.d(TAG, "Start AmbientLightCapability(Context context)");
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        this.effectType = getAmbientLightEffectType();
        this.defaultOrMusicGroupNum = new GroupNum();
        this.mSpeechPara = new SpeechPara();
        this.defaultOrMusicGroupNum.setGroupNum(3);
        this.mATLEffect = new ATLEffect();
        this.scriptEffectTimer = new ScheduledThreadPoolExecutor(1);
        this.speechTimer = new ScheduledThreadPoolExecutor(1);
        this.mAmbientLightEffectTypeList = new ArrayList();
        this.currentEffect = new ATLEffect.EffectData();
        parseATLEffectFromJsons();
        if (!this.isEnableAtlController) {
            this.mAmbientLightHal = new CarAtlManagerProxy(context);
        } else {
            this.mAmbientLightHal = new AmbientLightController(context);
            this.isAmbientLightInit = true;
        }
        CarResourceManager carResourceManager = this.mCarResourceManager;
        CarResourceManager.getInstance();
        this.mHandler = new Handler(XuiWorkHandler.getInstance().getLooper()) { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightCapability.5
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        BiLog bilog = (BiLog) msg.obj;
                        LogUtil.d(AmbientLightCapability.TAG, "BiAmbient: " + bilog.getString());
                        BiLogTransmit.getInstance().submit(bilog);
                        return;
                    default:
                        return;
                }
            }
        };
        this.mVisualizerService = VisualizerService.getInstance();
    }

    public void setCarAtlManager(CarAtlManager manager) {
        this.mCarAtlManager = manager;
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        LogUtil.d(TAG, "onCarManagerInited()");
        this.mCarBcmManager = getCarManager("xp_bcm");
    }

    private void setAmbientLightEffectData(String effectName) {
        synchronized (this.ambientLightEffectMap) {
            if (this.ambientLightEffectMap.containsKey(effectName)) {
                int executeTime = 0;
                this.mATLEffect = this.ambientLightEffectMap.get(effectName);
                for (int i = 0; i < this.mATLEffect.getEffectData().size(); i++) {
                    executeTime += this.mATLEffect.getEffectData().get(i).getTimeInterval();
                }
                this.mATLEffect.setExecuteTime(executeTime);
                LogUtil.i(TAG, "setAmbientLightEffectData effectName " + effectName + " size " + this.mATLEffect.getEffectData().size() + " " + this.ambientLightEffectMap.size() + " executeTime " + executeTime);
            } else {
                LogUtil.i(TAG, "Effect name not found ");
            }
        }
    }

    public void setAmbientLightEffect(String effectType, int bright_low) {
        LogUtil.i(TAG, "setAmbientLightEffect effectType " + effectType + " bright_low " + bright_low);
        try {
            int firstThemeColor = this.doubleThemeColorEnable ? this.doubleFirstColor : this.monoColor;
            for (int i = 0; i < this.mATLEffect.getEffectData().size(); i++) {
                LogUtil.d(TAG, "setAmbientLightEffect doubleThemeColorEnable " + this.doubleThemeColorEnable);
                if (this.doubleThemeColorEnable) {
                    if (this.mATLEffect.getEffectData().get(i).getColorswitch()) {
                        this.mATLEffect.getEffectData().get(i).setColor(this.doubleSecondColor);
                    } else {
                        this.mATLEffect.getEffectData().get(i).setColor(firstThemeColor);
                    }
                } else {
                    this.mATLEffect.getEffectData().get(i).setColor(firstThemeColor);
                }
            }
            handleEffectData();
        } catch (Exception e) {
            LogUtil.e(TAG, "setAmbientLightEffect error" + e);
        }
    }

    public void updateLightEffectPara(String effectType) {
        int firstThemeColor = this.doubleThemeColorEnable ? this.doubleFirstColor : this.monoColor;
        LogUtil.i(TAG, "updateLightEffectPara effectType " + effectType + " doubleThemeColorEnable " + this.doubleThemeColorEnable + " firstThemeColor " + firstThemeColor + " doubleSecondColor " + this.doubleSecondColor);
        for (int i = 0; i < this.mATLEffect.getEffectData().size(); i++) {
            if (this.doubleThemeColorEnable) {
                if (this.mATLEffect.getEffectData().get(i).getColorswitch()) {
                    this.mATLEffect.getEffectData().get(i).setColor(this.doubleSecondColor);
                } else {
                    this.mATLEffect.getEffectData().get(i).setColor(firstThemeColor);
                }
                if (i >= 1) {
                    LogUtil.d(TAG, "updateLightEffectPara " + this.mATLEffect.getEffectData().get(i - 1).getColor() + " " + this.mATLEffect.getEffectData().get(i).getColor());
                }
            } else {
                this.mATLEffect.getEffectData().get(i).setColor(firstThemeColor);
            }
        }
        int i2 = setDrivingGroup();
        setGroupLightData(i2, (byte) firstThemeColor, (byte) this.mATLEffect.getEffectData().get(0).getBrightness(), (byte) this.mATLEffect.getEffectData().get(0).getFade());
    }

    public void setSpeechEffect(int[] groupNum, String effectType) {
        LogUtil.i(TAG, "setSpeechEffect effectType " + effectType);
        synchronized (this.ambientLightEffectMap) {
            if (this.ambientLightEffectMap.containsKey(effectType)) {
                ATLEffect currentEffect = this.ambientLightEffectMap.get(effectType);
                for (int i = 0; i < currentEffect.getEffectData().size(); i++) {
                    int num = currentEffect.getEffectData().size();
                    byte[] color = new byte[num];
                    byte[] bright = new byte[num];
                    int[] fade = new int[num];
                    for (int j = 0; j < num; j++) {
                        color[j] = (byte) currentEffect.getEffectData().get(i).getColor();
                        bright[j] = (byte) currentEffect.getEffectData().get(i).getBrightness();
                        fade[j] = currentEffect.getEffectData().get(i).getFade();
                    }
                    startSpeechEffect(groupNum, color, bright, fade);
                }
            }
        }
    }

    private void startSpeechEffect(final int[] groupNum, byte[] color, byte[] bright, int[] fade) {
        int i;
        LogUtil.i(TAG, "startSpeechEffect()");
        this.speechGroupNum = groupNum;
        long period_time = 0;
        for (int i2 : fade) {
            period_time += i2;
        }
        this.speechEnable = true;
        long delay_time = 0;
        for (int i3 = 0; i3 < color.length; i3++) {
            LogUtil.d(TAG, "started speechTimer length:" + color.length);
            final byte color_value = color[i3];
            final byte bright_value = bright[i3];
            final byte fade_value = (byte) Math.round((float) (fade[i3] / 20));
            Runnable timerTask = new Runnable() { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightCapability.6
                @Override // java.lang.Runnable
                public void run() {
                    if (!AmbientLightCapability.this.mSpeechPara.isEnable() || !AmbientLightCapability.this.speechEnable) {
                        if (!AmbientLightCapability.this.speechEnable) {
                            AmbientLightCapability.this.printDebugLog("speechTask", 0, 0);
                            return;
                        }
                        return;
                    }
                    int j = 0;
                    while (true) {
                        int[] iArr = groupNum;
                        if (j < iArr.length) {
                            AmbientLightCapability.this.setGroupLightData(iArr[j], color_value, bright_value, fade_value);
                            j++;
                        } else {
                            return;
                        }
                    }
                }
            };
            this.speechTimer.scheduleAtFixedRate(timerTask, delay_time, period_time, TimeUnit.MILLISECONDS);
            delay_time += fade[i];
        }
    }

    public void startWelcomeEffect() {
        if (!this.isStartWelcomeEffect) {
            LogUtil.i(TAG, "Ambient Light Welcome Effect Start.");
            this.isStartWelcomeEffect = true;
            if (getAmbientLightOpen()) {
                this.isAmbientLightInit = true;
                setAmbientLightEffectData("welcome_effect");
                setAmbientLightEffect("welcome_effect", 5);
            }
            XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightCapability.7
                @Override // java.lang.Runnable
                public void run() {
                    LogUtil.i(AmbientLightCapability.TAG, "Ambient Light Welcome Effect End.");
                    AmbientLightCapability.this.initAmbilentLightEffect();
                }
            }, this.mATLEffect.getExecuteTime());
        }
    }

    public void initAmbilentLightEffect() {
        if (getAmbientLightOpen() && this.isCarIgOn) {
            try {
                LogUtil.i(TAG, "initAmbientLightEffect()");
                if (!this.isEnableAtlController) {
                    byte[] lightPosition = {0, 0};
                    byte[] color = {0, 0};
                    byte[] bright = {0, 0};
                    byte[] fade = {0, 0};
                    setTwoLightData((byte) 0, lightPosition, false, color, bright, fade);
                    setAmbientLightDataEffect(0, "normalColor", (byte) 0, TarConstants.LF_SYMLINK);
                    setDoorAmbientLightOpen(true);
                }
            } catch (Exception e) {
                LogUtil.e(TAG, "initAmbilentLightEffect error");
            }
            this.isAmbientLightInit = true;
            setAmbientLightEffectType(getAmbientLightEffectType());
        }
    }

    public void updateAmbientLightStatus(String effectType) {
        this.doubleThemeColorEnable = getDoubleThemeColorEnable(effectType);
        this.monoColor = getAmbientLightMonoColor(effectType);
        this.doubleFirstColor = getAmbientLightDoubleFirstColor(effectType);
        this.doubleSecondColor = getAmbientLightDoubleSecondColor(effectType);
    }

    private void parseATLEffectFromJsons() {
        LogUtil.d(TAG, "parseATLEffectFromJsons()");
        List<String> effectFilesName = getFilesAllName("/system/etc/xuiservice/atl");
        if (effectFilesName == null) {
            return;
        }
        synchronized (this.ambientLightEffectMap) {
            this.ambientLightEffectMap.clear();
            this.mAmbientLightEffectTypeList.clear();
            for (int i = 0; i < effectFilesName.size(); i++) {
                try {
                    try {
                        ATLEffect effect = (ATLEffect) new Gson().fromJson((Reader) new FileReader(effectFilesName.get(i)), (Class<Object>) ATLEffect.class);
                        if (!this.mAmbientLightEffectTypeList.contains(effect.getEffectName())) {
                            this.mAmbientLightEffectTypeList.add(effect.getEffectName());
                            this.ambientLightEffectMap.put(effect.getEffectName(), effect);
                        }
                        LogUtil.d(TAG, "parseATLEffectFromJsons EffectName " + effect.getEffectName());
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                } catch (JsonIOException e2) {
                    e2.printStackTrace();
                } catch (FileNotFoundException e3) {
                    e3.printStackTrace();
                }
            }
        }
    }

    public void setAmbientLightMonoColor(String effectType, int color) {
        LogUtil.i(TAG, "setAmbientLightMonoColor() effectType: " + effectType + " color:" + color);
        this.monoColor = color;
        int privMonoColor = getAmbientLightMonoColor(effectType);
        if (privMonoColor != color) {
            int pid = Binder.getCallingPid();
            String pkg = PackageUtils.getPackageName(pid);
            BiAmbient uploader = new BiAmbient(3, String.valueOf(color), pkg);
            uploader.submit();
        }
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "MonoColor", color);
        AmbientLightHalListener ambientLightHalListener = this.mListener;
        if (ambientLightHalListener != null) {
            ambientLightHalListener.onAmbientLightSetEvent(3, effectType, new int[]{color});
        }
        if (getAmbientLightOpen()) {
            boolean z = this.isLite;
            if (!z) {
                if (!this.isAmbientLightInit && privMonoColor != color) {
                    initAmbilentLightEffect();
                    if (getAmbientLightEffectType().equals("music")) {
                        setMusicSpectrumEnable(true);
                    }
                }
                setDoorAmbientLightOpen(true);
                if (effectType.equals("stable_effect")) {
                    updateLightEffectPara("noeffect");
                } else if (effectType.equals("gentle_breathing")) {
                    updateLightEffectPara("breath");
                }
                if (effectType.equals("follow_speed")) {
                    recoveryColor();
                    setFollowCarSpeedEffect(80.0f, 2);
                } else if (effectType.equals("music")) {
                    recoveryColor();
                    if (!isVisualizerEnable() && !this.isSpeechActive) {
                        setMusicSpectrumEffect((byte) color, (byte) 100, (byte) AMBIENTLIGHT_DEFAULT_FADE);
                    }
                }
                setThemeFirstColor(color);
            } else if (z && color != privMonoColor && this.isPNGear) {
                riseSHC();
            }
        }
    }

    public void setDoubleThemeColorEnable(String effectType, boolean enable) {
        LogUtil.i(TAG, "setDoubleThemeColorEnable() effectType:" + effectType + " enable:" + enable);
        if (effectType.equals("stable_effect") || effectType.equals("gentle_breathing")) {
            if (getDoubleThemeColorEnable(effectType) != enable) {
                int pid = Binder.getCallingPid();
                String pkg = PackageUtils.getPackageName(pid);
                BiAmbient uploader = new BiAmbient(6, enable ? "double" : "mono", pkg);
                uploader.submit();
            }
            Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isDoubleColorEnable", enable ? 1 : 0);
        }
        AmbientLightHalListener ambientLightHalListener = this.mListener;
        if (ambientLightHalListener != null) {
            ambientLightHalListener.onAmbientLightSetEvent(2, effectType, new int[]{enable ? 1 : 0});
        }
        if (!this.isLite && (effectType.equals("stable_effect") || effectType.equals("gentle_breathing"))) {
            this.doubleThemeColorEnable = enable;
            if (!getAmbientLightOpen()) {
                return;
            }
            if (effectType.equals("stable_effect")) {
                updateLightEffectPara("noeffect");
            } else if (effectType.equals("gentle_breathing")) {
                updateLightEffectPara("breath");
            }
            setDoubleThemeColor(enable ? 1 : 0);
            setThemeFirstColor(this.doubleThemeColorEnable ? this.doubleFirstColor : this.monoColor);
            setThemeSecondColor(this.doubleSecondColor);
        }
        setDoorAmbientLightOpen(true);
    }

    public void setAmbientLightDoubleColor(String effectType, int first_color, int second_color) {
        LogUtil.i(TAG, "setAmbientLightDoubleColor() effectType:" + effectType + " first_color:" + first_color + " second_color:" + second_color);
        this.doubleFirstColor = first_color;
        this.doubleSecondColor = second_color;
        int prevDoublebFirstColor = getAmbientLightDoubleFirstColor(effectType);
        if (prevDoublebFirstColor != first_color || getAmbientLightDoubleSecondColor(effectType) != second_color) {
            int pid = Binder.getCallingPid();
            String pkg = PackageUtils.getPackageName(pid);
            List<Integer> colors = Arrays.asList(Integer.valueOf(first_color), Integer.valueOf(second_color));
            BiAmbient uploader = new BiAmbient(4, colors.toString(), pkg);
            uploader.submit();
        }
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "DoubleFirstColor", first_color);
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "DoubleSecondColor", second_color);
        AmbientLightHalListener ambientLightHalListener = this.mListener;
        if (ambientLightHalListener != null) {
            ambientLightHalListener.onAmbientLightSetEvent(4, effectType, new int[]{first_color, second_color});
        }
        if (getAmbientLightOpen() && !this.isLite) {
            if (!this.isAmbientLightInit && first_color != prevDoublebFirstColor) {
                initAmbilentLightEffect();
                if (getAmbientLightEffectType().equals("music")) {
                    setMusicSpectrumEnable(true);
                }
            }
            if (effectType.equals("stable_effect")) {
                updateLightEffectPara("noeffect");
            } else if (effectType.equals("gentle_breathing")) {
                updateLightEffectPara("breath");
            }
            setThemeFirstColor(this.doubleThemeColorEnable ? first_color : this.monoColor);
            setThemeSecondColor(second_color);
            setDoorAmbientLightOpen(true);
        }
    }

    public void setAmbientLightMemoryBright(int bright) {
        LogUtil.i(TAG, "setAmbientLightMemoryBright(): " + bright);
        String effectType = getAmbientLightEffectType();
        if (getAmbientLightBright() != bright) {
            int pid = Binder.getCallingPid();
            String pkg = PackageUtils.getPackageName(pid);
            BiAmbient uploader = new BiAmbient(5, String.valueOf(bright), pkg);
            uploader.submit();
        }
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "AmbientLightBright", bright);
        AmbientLightHalListener ambientLightHalListener = this.mListener;
        if (ambientLightHalListener != null) {
            ambientLightHalListener.onAmbientLightSetEvent(5, effectType, new int[]{bright});
        }
        if (getAmbientLightOpen()) {
            setBrightness(bright);
        }
    }

    public void setAmbientLightBright(int bright) {
        LogUtil.i(TAG, "setAmbientLightBright(): " + bright);
        String effectType = getAmbientLightEffectType();
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "AmbientLightBright", bright);
        if (getAmbientLightBright() != bright) {
            int pid = Binder.getCallingPid();
            String pkg = PackageUtils.getPackageName(pid);
            BiAmbient uploader = new BiAmbient(5, String.valueOf(bright), pkg);
            uploader.submit();
        }
        AmbientLightHalListener ambientLightHalListener = this.mListener;
        if (ambientLightHalListener != null) {
            ambientLightHalListener.onAmbientLightSetEvent(5, effectType, new int[]{bright});
        }
        if (getAmbientLightOpen()) {
            setBrightness(bright);
            if (!this.isLite) {
                if (!this.isAmbientLightInit) {
                    initAmbilentLightEffect();
                    if (getAmbientLightEffectType().equals("music")) {
                        setMusicSpectrumEnable(true);
                    }
                }
                if (effectType.equals("stable_effect")) {
                    updateLightEffectPara("noeffect");
                } else if (effectType.equals("gentle_breathing")) {
                    updateLightEffectPara("breath");
                }
                if (effectType.equals("follow_speed")) {
                    recoveryColor();
                    setFollowCarSpeedEffect(80.0f, 2);
                } else if (effectType.equals("music")) {
                    recoveryColor();
                    if (!isVisualizerEnable() && !this.isSpeechActive) {
                        setMusicSpectrumEffect((byte) getAmbientLightMonoColor(effectType), (byte) 100, (byte) AMBIENTLIGHT_DEFAULT_FADE);
                    }
                }
                setDoorAmbientLightOpen(true);
            }
        }
    }

    public void setAmbientLightEffectType(String effectType) {
        if (!this.mModeList.contains(effectType)) {
            LogUtil.w(TAG, "invalid effectType! " + effectType);
            return;
        }
        LogUtil.i(TAG, "setAmbientLightEffectType() " + effectType);
        if (!effectType.equals(getAmbientLightEffectType())) {
            int pid = Binder.getCallingPid();
            String pkg = PackageUtils.getPackageName(pid);
            BiAmbient uploader = new BiAmbient(1, effectType, pkg);
            uploader.submit();
        }
        AmbientLightHalListener ambientLightHalListener = this.mListener;
        if (ambientLightHalListener != null) {
            ambientLightHalListener.onAmbientLightSetEvent(1, effectType, new int[0]);
        }
        String privEffectType = getAmbientLightEffectType();
        this.effectType = effectType;
        Settings.System.putString(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "AmbientLightEffectType", effectType);
        if (getAmbientLightOpen() && !this.isLite) {
            if (!privEffectType.equals(effectType) && !this.isAmbientLightInit) {
                initAmbilentLightEffect();
            }
            setAmbientLightEffectData(effectType);
            updateAmbientLightStatus(effectType);
            setDoubleThemeColor(this.doubleThemeColorEnable ? 1 : 0);
            setThemeFirstColor(this.doubleThemeColorEnable ? this.doubleFirstColor : this.monoColor);
            setThemeSecondColor(this.doubleSecondColor);
            registerVisualizerListener();
            if (effectType.equals("music")) {
                stopEffect();
                updateAmbientLightEffect();
                resetColor();
            } else if (effectType.equals("follow_speed")) {
                stopEffect();
                for (int i = 0; i < 6; i++) {
                    setFollowCarSpeedEffect(this.carSpeed, 100);
                }
            } else {
                updateAmbientLightEffect();
                if (effectType.equals("stable_effect")) {
                    setAmbientLightEffect("noeffect", 5);
                } else if (effectType.equals("gentle_breathing")) {
                    setAmbientLightEffect("breath", 5);
                } else {
                    setAmbientLightEffect(effectType, 5);
                }
            }
            setDoorAmbientLightOpen(true);
        }
    }

    public void updateAmbientLightEffect() {
        this.effectType = getAmbientLightEffectType();
        updateAmbientLightStatus(this.effectType);
        int i = this.speechState;
        if (i == 0 || i == 1 || i == 2) {
            int i2 = this.speechVoicer;
            if (i2 == 1) {
                setDefaultOrMusicGroup(this.isDriving ? 15 : 17);
            } else if (i2 == 2) {
                setDefaultOrMusicGroup(this.isDriving ? 14 : 16);
            } else if (i2 == 3) {
                setDefaultOrMusicGroup(22);
            } else if (i2 == 4) {
                setDefaultOrMusicGroup(23);
            }
        } else if (i == 3) {
            setDefaultOrMusicGroup(this.isAdjustHvac ? this.isDriving ? 12 : 13 : this.isDriving ? 2 : 3);
        }
        int i3 = this.speechState;
        if (i3 == 0) {
            setSpeechEffectEnable(false);
        } else if (i3 == 2) {
            int[] speechGroup = new int[1];
            int i4 = this.speechVoicer;
            if (i4 == 1) {
                speechGroup[0] = 11;
            } else if (i4 == 2) {
                speechGroup[0] = 10;
            } else if (i4 == 3) {
                speechGroup[0] = 20;
            } else if (i4 == 4) {
                speechGroup[0] = 21;
            }
            if (speechGroup[0] != 0) {
                setSpeechEffectEnable(true);
                setSpeechEffect(speechGroup, "speecheffect1");
            }
        }
    }

    public void setTwoLightData(int group, byte color, byte bright, byte fade) {
        LogUtil.i(TAG, "setTwoLightData()");
        byte[] mlightPosition = {(byte) group, (byte) group};
        byte[] mcolor = {color, color};
        byte[] mbright = {bright, bright};
        byte[] mfade = {fade, fade};
        int protocol = SystemProperties.getInt("persist.atl.protocol", 1);
        try {
            if (this.isCarIgOn) {
                this.mAmbientLightHal.setTwoLightData((byte) protocol, mlightPosition, false, mcolor, mbright, mfade);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void setTwoLightData(byte protocol, byte[] lightPosition, boolean hold, byte[] color, byte[] bright, byte[] fade) {
        LogUtil.i(TAG, "setTwoLightData " + ((int) protocol) + " " + ((int) lightPosition[0]) + " " + hold + " " + ((int) color[0]) + " " + ((int) bright[0]) + " " + ((int) fade[0]) + " isIgOn:" + this.isCarIgOn);
        try {
            if (this.isCarIgOn) {
                this.mAmbientLightHal.setTwoLightData(protocol, lightPosition, hold, color, bright, fade);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void setGroupLightData(int groupNum, byte color, byte bright, byte fade) {
        LogUtil.d(TAG, "setGroupLightData groupNum:" + groupNum + ", color:" + ((int) color) + ", bright:" + ((int) bright) + ", fade:" + ((int) fade) + " isCarIgOn " + this.isCarIgOn);
        printAtlLog(groupNum, color, bright, fade);
        byte groupNumber = (byte) (groupNum & 255);
        try {
            if (!this.isCarIgOn && bright != 0) {
                printAtlLog("setGroutLightData", groupNum, bright);
            }
            this.mAmbientLightHal.setGroutLightData(groupNumber, (byte) 0, 0, false, color, bright, fade);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void setGroupLightData(byte groupNum, byte solution, int lightlist, boolean hold, byte color, byte bright, byte fade) {
        try {
        } catch (Exception e) {
            e = e;
        }
        try {
            if (!this.isCarIgOn && bright != 0) {
                printAtlLog("setGroutLightData", groupNum, bright);
            }
            this.mAmbientLightHal.setGroutLightData(groupNum, solution, lightlist, hold, color, bright, fade);
        } catch (Exception e2) {
            e = e2;
            handleException(e);
        }
    }

    public void setAllLightData(byte[] color, byte[] bright, byte[] fade) {
        LogUtil.d(TAG, "setAllLightData()");
        try {
            if (this.isCarIgOn) {
                this.mAmbientLightHal.setAllLightData(color, bright, fade);
            } else {
                printAtlLog("setAllLightData", 0, 0);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void setOtherLightOffWhenSpeech() {
        for (int i = 0; i < 6; i++) {
            setGroupLightData(this.defaultOrMusicGroupNum.getGroupNum(), (byte) 1, (byte) 0, (byte) 0);
        }
    }

    public void setAmbientLightDataEffect(int groupNum, String colorType, byte bright, byte fade) {
        byte color = 0;
        if (colorType == "normalColor") {
            color = (byte) (this.doubleThemeColorEnable ? this.doubleFirstColor : this.monoColor);
        } else if (colorType == "hvacColor") {
            color = (byte) this.hvacColor;
        }
        for (int i = 0; i < 6; i++) {
            setGroupLightData(groupNum, color, bright, fade);
        }
    }

    public void sendAtlControl(boolean on) {
        try {
            AtlConfiguration.Builder mBuilder = new AtlConfiguration.Builder();
            mBuilder.isLeftAtlControlEnabled(true);
            mBuilder.isRightAtlControlEnabled(true);
            mBuilder.colorType(0);
            mBuilder.isLedEnabled(on);
            mBuilder.isFadeEnabled(true);
            mBuilder.fadingTime(10);
            mBuilder.intensity(100);
            mBuilder.rgbRedOrPreDefinedColor(this.monoColor);
            this.mAtlConfiguration = mBuilder.build();
            setAtlConfiguration(this.mAtlConfiguration);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void stopSpeechEffect() {
        LogUtil.i(TAG, "stopSpeechEffect()");
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = this.speechTimer;
        if (scheduledThreadPoolExecutor != null) {
            scheduledThreadPoolExecutor.shutdown();
        }
        this.speechEnable = false;
        this.speechTimer = new ScheduledThreadPoolExecutor(1);
    }

    public void stopAllAmbilentEffect() {
        stopEffect();
        stopSpeechEffect();
    }

    public void registerVisualizerListener() {
        try {
            if (this.mVisualCaptureListener != null && getAmbientLightEffectType() != null) {
                if (this.mATLEffect.getEnableMusicSpectrum() != 1 && this.mATLEffect.getEnableMusicSpectrum() != 2) {
                    setMusicSpectrumEnable(false);
                    this.mMediaCenterManager.unRegisterVisualizerListener(this.mVisualCaptureListener);
                }
                setMusicSpectrumEnable(true);
                this.mMediaCenterManager.registerVisualizerListener(this.mVisualCaptureListener);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void resetGroutLightData() {
        String effectType = getAmbientLightEffectType();
        int color = getDoubleThemeColorEnable(effectType) ? getAmbientLightDoubleFirstColor(effectType) : getAmbientLightMonoColor(effectType);
        for (int i = 0; i < 6; i++) {
            setGroupLightData((byte) 0, (byte) 1, -1, false, (byte) -1, (byte) 0, (byte) 10);
            setGroupLightData((byte) 1, (byte) 0, -1, false, (byte) color, (byte) 100, (byte) 10);
        }
    }

    public boolean isWelcomeAutoShow() {
        StringBuilder sb = new StringBuilder();
        sb.append("Welcome mode status: ");
        sb.append(Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "welcome_disable", 0) == 1);
        LogUtil.i(TAG, sb.toString());
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "welcome_disable", 0) == 1;
    }

    public List<String> getFilesAllName(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) {
            LogUtil.e(TAG, "空目录");
            return null;
        }
        List<String> s = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            LogUtil.d(TAG, "File path: " + files[i].getAbsolutePath());
            s.add(files[i].getAbsolutePath());
        }
        return s;
    }

    public boolean getMusicSpectrumEnable() {
        return !this.isLite && Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isMusicSpectrumEnable", 1) == 1;
    }

    public boolean getAmbientLightOpen() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isAmbientLightOpen", 1) == 1;
    }

    public boolean isSupportDoubleThemeColor(String effectType) {
        return effectType.equals("stable_effect") || effectType.equals("gentle_breathing");
    }

    public String getAmbientLightEffectType() {
        String effectType = Settings.System.getString(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "AmbientLightEffectType");
        return effectType == null ? "stable_effect" : Settings.System.getString(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "AmbientLightEffectType");
    }

    public boolean getDoubleThemeColorEnable(String effectType) {
        return (effectType.equals("stable_effect") || effectType.equals("gentle_breathing")) && Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isDoubleColorEnable", 0) == 1;
    }

    public int getAmbientLightMonoColor(String effectType) {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "MonoColor", 14);
    }

    public int getAmbientLightDoubleFirstColor(String effectType) {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "DoubleFirstColor", 1);
    }

    public int getAmbientLightDoubleSecondColor(String effectType) {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "DoubleSecondColor", 6);
    }

    public int getAmbientLightBright() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "AmbientLightBright", 100);
    }

    public void setAmbientLightOpen(boolean enable) {
        try {
            LogUtil.i(TAG, "setAmbientLightOpen(): " + enable);
            boolean privOpenStatus = getAmbientLightOpen();
            boolean z = true;
            int i = 1;
            Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isAmbientLightOpen", enable ? 1 : 0);
            if (privOpenStatus != enable) {
                int pid = Binder.getCallingPid();
                String pkg = PackageUtils.getPackageName(pid);
                BiAmbient uploader = new BiAmbient(0, enable ? "on" : "off", pkg);
                uploader.submit();
            }
            if (this.mListener != null) {
                int data = enable ? 1 : 0;
                this.mListener.onAmbientLightSetEvent(6, this.effectType, new int[]{data});
            }
            if (!this.isLite) {
                AmbientLightHal ambientLightHal = this.mAmbientLightHal;
                if (!enable) {
                    i = 0;
                }
                ambientLightHal.setAtlOpen(i);
                if (enable && !this.isAmbientLightInit && privOpenStatus != enable) {
                    initAmbilentLightEffect();
                }
                setDoorAmbientLightOpen(enable);
            } else if (privOpenStatus != enable) {
                if (enable && this.isPNGear) {
                    riseSHC();
                    return;
                }
                if (!this.isPNGear || !this.isCarIgOn || !this.isShcRised || !getAmbientLightOpen() || !enable) {
                    z = false;
                }
                sendAtlControl(z);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void setThemeFirstColor(int color) {
        try {
            this.mAmbientLightHal.setThemeFirstColor(color);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void setThemeSecondColor(int color) {
        try {
            this.mAmbientLightHal.setThemeSecondColor(color);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void setDoubleThemeColor(int status) {
        try {
            this.mAmbientLightHal.setDoubleThemeColor(status);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void setBrightness(int bright) {
        try {
            this.mAmbientLightHal.setBrightnessLevel(bright);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void setAtlOpen(int status) {
        try {
            this.mAmbientLightHal.setAtlOpen(status);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void setAtlConfiguration(AtlConfiguration config) {
        try {
            this.mAmbientLightHal.setAtlConfiguration(config);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void setMusicSpectrumEffect(byte color, byte bright, byte fade) {
        if (getAmbientLightEffectType().equals("effect_music_rhythm_left_right")) {
            setGroupLightData(107, color, bright, fade);
        } else {
            setGroupLightData(this.defaultOrMusicGroupNum.getGroupNum(), color, bright, fade);
        }
    }

    public void setAmbientLightDefaultGroupEffect(byte color, byte bright, byte fade) {
        setGroupLightData(this.defaultOrMusicGroupNum.getGroupNum(), color, bright, fade);
    }

    public void setSpeechEffectEnable(boolean enable) {
        this.mSpeechPara.setEnable(enable);
    }

    public void setSpeechSpectrumEffect(byte color, byte bright, byte fade) {
        if (this.speechGroupNum != null) {
            int i = 0;
            while (true) {
                int[] iArr = this.speechGroupNum;
                if (i < iArr.length) {
                    setGroupLightData(iArr[i], color, bright, fade);
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public void setMusicSpectrum(byte bright, byte fade) {
        if (!this.isAmbientLightInit) {
            this.isAmbientLightInit = true;
        }
        byte color = (byte) (this.doubleThemeColorEnable ? this.doubleFirstColor : this.monoColor);
        if ((this.mATLEffect.getEnableMusicSpectrum() == 1 || this.mATLEffect.getEnableMusicSpectrum() == 2) && this.speechState != 0) {
            printAtlLog("setMusicSpectrum", " bright:" + ((int) bright) + ", fade:" + ((int) fade));
            if (this.mATLEffect.getEnableMusicSpectrum() == 2) {
                this.currentBright = bright;
                setMusicSpectrumEffect((byte) this.currentColor, (byte) this.currentBright, (byte) this.currentFade);
                return;
            }
            setMusicSpectrumEffect(color, bright, fade);
            return;
        }
        printAtlLog("setMusicSpectrum", getMusicSpectrumEnable(), this.speechState);
    }

    public void setFollowCarSpeedEffect(float speed, int fade) {
        LogUtil.d(TAG, "setFollowCarSpeedEffect speed:" + speed);
        byte bright = 1;
        if (speed >= 1.0f) {
            bright = speed < 80.0f ? (byte) Math.round(((speed * 99.0f) - 20.0f) / 79.0f) : (byte) 100;
        }
        if (this.mATLEffect.getEnableSpeedData() == 1) {
            setGroupLightData(this.defaultOrMusicGroupNum.getGroupNum(), (byte) this.monoColor, bright, (byte) fade);
        } else if (this.mATLEffect.getEnableSpeedData() == 2) {
            this.currentBright = Math.round(((99.0f * speed) - 20.0f) / 79.0f);
        }
    }

    public void setMusicRhythmMode(boolean enable) {
        LogUtil.i(TAG, "setMusicRhythmMode : " + enable);
        if (!this.isLite) {
            this.isMusicRhythmMode = enable;
            updateAmbientLightEffect();
        }
    }

    public void setDoorAmbientLightOpen(boolean open) {
        LogUtil.i(TAG, "setDoorAmbientLightOpen : " + open);
        byte b = 0;
        if (SystemProperties.getInt("persist.atl.group", 1) == 1) {
            String effectType = getAmbientLightEffectType();
            int color = getDoubleThemeColorEnable(effectType) ? getAmbientLightDoubleFirstColor(effectType) : getAmbientLightMonoColor(effectType);
            byte b2 = (byte) color;
            if (open && !this.isDriving) {
                b = 100;
            }
            setGroupLightData(1, b2, b, (byte) 100);
            return;
        }
        byte b3 = (byte) (this.doubleThemeColorEnable ? this.doubleFirstColor : this.monoColor);
        if (open && !this.isDriving) {
            b = 100;
        }
        setTwoLightData(1, b3, b, (byte) 100);
    }

    public void setMusicSpectrumEnable(boolean enable) {
        LogUtil.i(TAG, "setMusicSpectrumEnable : " + enable);
        if (!this.isLite) {
            Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "isMusicSpectrumEnable", enable ? 1 : 0);
        }
    }

    public List<String> getAmbientLightEffectTypeList() {
        return this.mAmbientLightEffectTypeList;
    }

    public boolean getDrivingStatus() {
        return this.isDriving;
    }

    public boolean getCarIgStatus() {
        return this.isCarIgOn;
    }

    public boolean getAdjustHvacStatus() {
        return this.isAdjustHvac;
    }

    public boolean getSHCStatus() {
        return this.isShcRised;
    }

    public boolean getPNGear() {
        return this.isPNGear;
    }

    public boolean getAmbientInitStatus() {
        return this.isAmbientLightInit;
    }

    public int getMusicEffectFade() {
        return AMBIENTLIGHT_DEFAULT_FADE;
    }

    public int getEnableMusicSpectrum() {
        return this.mATLEffect.getEnableMusicSpectrum();
    }

    public MediaCenterManager.VisualCaptureListener getVisualCaptureListener() {
        return this.mVisualCaptureListener;
    }

    public MediaCenterManager getMediaCenterManager() {
        return this.mMediaCenterManager;
    }

    public void setAdjustHvacStatus(boolean status) {
        this.isAdjustHvac = status;
    }

    public void setHvacColor(int color) {
        this.hvacColor = color;
    }

    public void setIgOnStatus(boolean enable) {
        LogUtil.d(TAG, "Car Ig Status: " + enable);
        this.isCarIgOn = enable;
    }

    public void setDrivingStatus(boolean status) {
        this.isDriving = status;
    }

    public void setSHCStatus(boolean status) {
        this.isShcRised = status;
    }

    public void setPNGear(boolean gearLevel) {
        this.isPNGear = gearLevel;
    }

    public void setMusicRhythmEnable(boolean enable) {
        this.isMusicRhythmMode = enable;
    }

    public void setAmbientInitStatus(boolean status) {
        this.isAmbientLightInit = status;
    }

    public void setWelcomeEffectStatus(boolean status) {
        this.isStartWelcomeEffect = status;
    }

    public void setRawCarSpeed(float speed) {
        this.carSpeed = speed;
    }

    public void setDefaultOrMusicGroup(int groupNum) {
        this.defaultOrMusicGroupNum.setGroupNum(groupNum);
    }

    public void setVisualCaptureListener(MediaCenterManager.VisualCaptureListener listener) {
        this.mVisualCaptureListener = listener;
    }

    public void setMediaCenterManager(MediaCenterManager manager) {
        this.mMediaCenterManager = manager;
    }

    public void setListener(AmbientLightHalListener listener) {
        synchronized (this) {
            this.mListener = listener;
        }
    }

    public void handleHalEvents() {
        AmbientLightHalListener listener;
        synchronized (this) {
            listener = this.mListener;
        }
        if (listener != null) {
            dispatchEventToListener(listener);
        }
    }

    private void dispatchEventToListener(AmbientLightHalListener listener) {
        LogUtil.i(TAG, "handleHalEvents event: ");
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.HalServiceBaseCarListener
    public void dump(PrintWriter writer) {
        writer.println("*AmbientLight HAL*");
    }

    public static AmbientLightCapability getInstance() {
        return InstanceHolder.sService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class InstanceHolder {
        private static final AmbientLightCapability sService = new AmbientLightCapability(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        this.mListener = null;
    }

    private void printAtlLog(int groupNum, byte color, byte bright, byte fade) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.lastAtlLogTime > CommonUtils.xuiLogInterval * 2) {
            LogUtil.i(TAG, "printAtlLog groupNum:" + groupNum + ", color:" + ((int) color) + ", bright:" + ((int) bright) + ", fade:" + ((int) fade));
            this.lastAtlLogTime = currentTime;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void printDebugLog(String func, int flag1, int flag2) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.lastAtlLogTime > CommonUtils.xuiLogInterval * 2) {
            LogUtil.i(TAG, "printDebugLog func:" + func + ", " + flag1 + ", " + flag2);
            this.lastAtlLogTime = currentTime;
        }
    }

    private void printAtlLog(String func, boolean flag1, int logs) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.lastAtlLogTime > CommonUtils.xuiLogInterval * 2) {
            LogUtil.i(TAG, "printAtlLog func:" + func + ", flag1:" + flag1 + ", logs:" + logs);
            this.lastAtlLogTime = currentTime;
        }
    }

    private void printAtlLog(String func, String logs) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.lastAtlLogTime > CommonUtils.xuiLogInterval * 2) {
            LogUtil.i(TAG, "printAtlLog func:" + func + ", logs:" + logs);
            this.lastAtlLogTime = currentTime;
        }
    }

    private void printAtlLog(String func, int pamram1, int pamram2) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.lastAtlLogTime > CommonUtils.xuiLogInterval) {
            LogUtil.i(TAG, "printAtlLog isIgOn:" + this.isCarIgOn + " func:" + func + ", " + pamram1 + ", " + pamram2);
            this.lastAtlLogTime = currentTime;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class GroupNum {
        int groupNum;

        private GroupNum() {
        }

        public int getGroupNum() {
            return this.groupNum;
        }

        public void setGroupNum(int groupNum) {
            this.groupNum = groupNum;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class SpeechPara {
        boolean enable;

        private SpeechPara() {
            this.enable = true;
        }

        public boolean isEnable() {
            return this.enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }
    }

    /* loaded from: classes5.dex */
    private class BrightPara {
        int bright = 100;

        private BrightPara() {
        }

        public int getBright() {
            return this.bright;
        }

        public void setBright(int bright) {
            this.bright = bright;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class BiAmbient {
        private BiLog bilog = BiLogFactory.create(DataLogUtils.XUI_PID, DataLogUtils.AMBIENT_BID);
        private int msg;

        private String parseEvent(int event) {
            switch (event) {
                case 0:
                    return "power";
                case 1:
                    return SpeechConstants.KEY_MODE;
                case 2:
                    return "effect";
                case 3:
                    return "monoColor";
                case 4:
                    return "doubleColor";
                case 5:
                    return "bright";
                case 6:
                    return "colorType";
                default:
                    return "unknown";
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void submit() {
            Message message = AmbientLightCapability.this.mHandler.obtainMessage();
            int i = this.msg;
            message.what = i;
            message.obj = this.bilog;
            if (i == 3 || i == 4 || i == 5) {
                AmbientLightCapability.this.mHandler.removeMessages(this.msg);
                AmbientLightCapability.this.mHandler.sendMessageDelayed(message, 10000L);
                return;
            }
            AmbientLightCapability.this.mHandler.sendMessage(message);
        }

        public BiAmbient(int event, String result, String source) {
            JSONObject params = new JSONObject();
            this.msg = event;
            if (event == 0 && result.equals("on")) {
                List<Integer> colors = Arrays.asList(Integer.valueOf(AmbientLightCapability.this.getAmbientLightDoubleFirstColor(AmbientLightCapability.this.effectType)), Integer.valueOf(AmbientLightCapability.this.getAmbientLightDoubleSecondColor(AmbientLightCapability.this.effectType)));
                try {
                    params.put(SpeechConstants.KEY_MODE, AmbientLightCapability.this.effectType);
                    params.put("colorType", AmbientLightCapability.this.getDoubleThemeColorEnable(AmbientLightCapability.this.effectType) ? "double" : "mono");
                    params.put("monoColor", AmbientLightCapability.this.getAmbientLightMonoColor(AmbientLightCapability.this.effectType));
                    params.put("doubleColor", colors.toString());
                    params.put("bright", AmbientLightCapability.this.getAmbientLightBright());
                } catch (Exception e) {
                }
            }
            this.bilog.push(NotificationCompat.CATEGORY_EVENT, parseEvent(event));
            this.bilog.push("ts", String.valueOf(System.currentTimeMillis()));
            this.bilog.push(RecommendBean.SHOW_TIME_RESULT, result);
            this.bilog.push("source", source);
            this.bilog.push("params", params.toString());
        }
    }

    public int startPlay(String[] effectData) {
        int res;
        int maxLength;
        int effectColor;
        int i;
        ATLEffect.EffectData effectInfo;
        ATLEffect.EffectData color;
        StringBuilder sb = new StringBuilder();
        sb.append("startPlay effect name ");
        int i2 = 1;
        sb.append(effectData[1]);
        LogUtil.d(TAG, sb.toString());
        int res2 = 0;
        if (!effectData[1].equals(getAmbientLightEffectType())) {
            int pid = Binder.getCallingPid();
            String pkg = PackageUtils.getPackageName(pid);
            BiAmbient uploader = new BiAmbient(2, effectData[1], pkg);
            uploader.submit();
        }
        try {
            setAmbientLightEffectData(effectData[1]);
            registerVisualizerListener();
            if (this.mATLEffect.getDefaultData() != 0) {
                res = 0;
            } else {
                char c = 0;
                JSONObject scriptEffect = new JSONObject(effectData[0]);
                int[] color2 = jsonToIntArr(scriptEffect.getJSONObject("color"), "color");
                int[] bright = jsonToIntArr(scriptEffect.getJSONObject("bright"), "bright");
                int[] fade = jsonToIntArr(scriptEffect.getJSONObject("fade"), "fade");
                int maxLength2 = Math.max(Math.max(color2.length, bright.length), fade.length);
                int periodTime = scriptEffect.getInt(TIME_INTERVAL) * maxLength2;
                ArrayList<ATLEffect.EffectData> effectList = new ArrayList<>();
                int i3 = 0;
                int num = 0;
                while (i3 < maxLength2) {
                    try {
                        if (color2.length <= i2) {
                            effectColor = color2[c];
                        } else {
                            try {
                                effectColor = color2[i3];
                            } catch (Exception e) {
                                e = e;
                                res = res2;
                                maxLength = maxLength2;
                                try {
                                    LogUtil.e(TAG, "startPlay2 exception " + e);
                                    i3++;
                                    maxLength2 = maxLength;
                                    res2 = res;
                                    i2 = 1;
                                    c = 0;
                                } catch (Exception e2) {
                                    e = e2;
                                    LogUtil.e(TAG, "startPlay exception " + e);
                                    return res;
                                }
                            }
                        }
                        int effectBright = bright.length > i2 ? bright[i3] : bright[0];
                        maxLength = maxLength2;
                        if (fade.length <= 1) {
                            i = fade[0];
                        } else {
                            try {
                                i = fade[i3];
                            } catch (Exception e3) {
                                e = e3;
                                res = res2;
                                LogUtil.e(TAG, "startPlay2 exception " + e);
                                i3++;
                                maxLength2 = maxLength;
                                res2 = res;
                                i2 = 1;
                                c = 0;
                            }
                        }
                        int effectFade = i;
                        if (i3 < this.mATLEffect.getEffectData().size()) {
                            effectList.add(this.mATLEffect.getEffectData().get(i3));
                            res = res2;
                        } else {
                            try {
                                if (num == effectList.size()) {
                                    num = 0;
                                }
                                effectInfo = new ATLEffect.EffectData();
                                color = effectInfo.setColor(effectList.get(num).getColor());
                                res = res2;
                            } catch (Exception e4) {
                                e = e4;
                                res = res2;
                                LogUtil.e(TAG, "startPlay2 exception " + e);
                                i3++;
                                maxLength2 = maxLength;
                                res2 = res;
                                i2 = 1;
                                c = 0;
                            }
                            try {
                                int res3 = effectList.get(num).getBrightness();
                                color.setBrightness(res3).setFade(effectList.get(num).getFade()).setTimeInterval(scriptEffect.getInt(TIME_INTERVAL)).setPeriodTime(periodTime).setEnableRandom(effectList.get(num).getEnableRandom()).setRandomMax(effectList.get(num).getRandomMax()).setRandomMin(effectList.get(num).getRandomMin()).setRandomArray(effectList.get(num).getRandomArray());
                                this.mATLEffect.getEffectData().add(effectInfo);
                                num++;
                            } catch (Exception e5) {
                                e = e5;
                                LogUtil.e(TAG, "startPlay2 exception " + e);
                                i3++;
                                maxLength2 = maxLength;
                                res2 = res;
                                i2 = 1;
                                c = 0;
                            }
                        }
                        LogUtil.d(TAG, "mATLEffect.getEffectData() size " + this.mATLEffect.getEffectData().size());
                        this.mATLEffect.getEffectData().get(i3).setColor(effectColor).setBrightness(effectBright).setFade(effectFade).setTimeInterval(scriptEffect.getInt(TIME_INTERVAL)).setPeriodTime(periodTime);
                        LogUtil.d(TAG, "startPlay i " + i3 + " effectColor " + this.mATLEffect.getEffectData().get(i3).getColor() + " effectBright " + this.mATLEffect.getEffectData().get(i3).getBrightness() + " effectFade " + this.mATLEffect.getEffectData().get(i3).getFade() + "startPlay color.length " + color2.length + " bright.length " + bright.length + " fade.length " + fade.length);
                    } catch (Exception e6) {
                        e = e6;
                        res = res2;
                        maxLength = maxLength2;
                    }
                    i3++;
                    maxLength2 = maxLength;
                    res2 = res;
                    i2 = 1;
                    c = 0;
                }
                res = res2;
            }
            handleEffectData();
        } catch (Exception e7) {
            e = e7;
            res = 0;
        }
        return res;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopEffect() {
        LogUtil.i(TAG, "stopEffect() ");
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = this.scriptEffectTimer;
        if (scheduledThreadPoolExecutor != null) {
            scheduledThreadPoolExecutor.shutdown();
        }
        this.scriptEffectTimer = new ScheduledThreadPoolExecutor(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int setDrivingGroup() {
        if (this.mATLEffect.getEnableDrivingGroup() == 1) {
            if (this.carSpeed >= 3.0f) {
                return 2;
            }
            return 3;
        }
        return this.mATLEffect.getEffectData().get(this.loopingNum).getGroupNum();
    }

    private void handleEffectData() {
        try {
            LogUtil.d(TAG, "handleEffectData() ");
            stopEffect();
            if (this.previousGroupNum != 3) {
                setGroupLightData(0, (byte) 1, (byte) 0, (byte) 0);
            }
            this.loopingNum = 0;
            this.currentMaxEffectNum = this.mATLEffect.getEffectData().size();
            Runnable timerTask = new Runnable() { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightCapability.8
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        AmbientLightCapability.this.currentEffect = AmbientLightCapability.this.mATLEffect.getEffectData().get(AmbientLightCapability.this.loopingNum);
                        List<Integer> randomArray = AmbientLightCapability.this.currentEffect.getRandomArray();
                        AmbientLightCapability.this.currentGroupNum = AmbientLightCapability.this.setDrivingGroup();
                        AmbientLightCapability.this.currentColor = AmbientLightCapability.this.mATLEffect.getEnableUserSelectedColor() == 1 ? AmbientLightCapability.this.doubleThemeColorEnable ? AmbientLightCapability.this.doubleFirstColor : AmbientLightCapability.this.monoColor : AmbientLightCapability.this.currentEffect.getColor();
                        if (AmbientLightCapability.this.currentEffect.getEnableRandom() == 1) {
                            AmbientLightCapability.this.currentGroupNum = AmbientLightCapability.this.currentEffect.getRandomMin() + ((int) (Math.random() * (AmbientLightCapability.this.currentEffect.getRandomMax() - AmbientLightCapability.this.currentEffect.getRandomMin())));
                        } else if (AmbientLightCapability.this.currentEffect.getEnableRandom() == 2) {
                            AmbientLightCapability.this.currentGroupNum = AmbientLightCapability.this.currentEffect.getRandomMin() + ((int) (Math.random() * (AmbientLightCapability.this.currentEffect.getRandomMax() - AmbientLightCapability.this.currentEffect.getRandomMin())));
                            AmbientLightCapability.this.currentColor = (int) (Math.random() * 20.0d);
                        } else if (AmbientLightCapability.this.currentEffect.getEnableRandom() == 3) {
                            AmbientLightCapability.this.currentGroupNum = randomArray.get(new Random().nextInt(randomArray.size())).intValue();
                        }
                        AmbientLightCapability.this.currentBright = AmbientLightCapability.this.currentEffect.getBrightness();
                        AmbientLightCapability.this.currentFade = AmbientLightCapability.this.currentEffect.getFade();
                        AmbientLightCapability.this.currentDelayTime = AmbientLightCapability.this.currentEffect.getTimeInterval();
                        if (AmbientLightCapability.this.mATLEffect.getEnableMusicSpectrum() != 1 && AmbientLightCapability.this.mATLEffect.getEnableMusicSpectrum() != 2 && AmbientLightCapability.this.mATLEffect.getEnableSpeedData() != 2) {
                            AmbientLightCapability.this.setGroupLightData(AmbientLightCapability.this.currentGroupNum, (byte) AmbientLightCapability.this.currentColor, (byte) AmbientLightCapability.this.currentBright, (byte) AmbientLightCapability.this.currentFade);
                        }
                        if (AmbientLightCapability.this.currentEffect.getEnableRandom() == 1 || AmbientLightCapability.this.currentEffect.getEnableRandom() == 3) {
                            AmbientLightCapability.this.setGroupLightData(AmbientLightCapability.this.closeRandomGroup, (byte) 1, (byte) 0, (byte) 0);
                            AmbientLightCapability.this.closeRandomGroup = AmbientLightCapability.this.currentGroupNum;
                        }
                        if (AmbientLightCapability.this.mATLEffect.getEffectType() == 0 && AmbientLightCapability.this.loopingNum == AmbientLightCapability.this.currentMaxEffectNum - 1) {
                            AmbientLightCapability.this.stopEffect();
                        }
                        if (AmbientLightCapability.this.currentMaxEffectNum - 1 != 0 && AmbientLightCapability.this.currentMaxEffectNum - 1 > AmbientLightCapability.this.loopingNum) {
                            AmbientLightCapability.access$1512(AmbientLightCapability.this, 1);
                            return;
                        }
                        AmbientLightCapability.this.loopingNum = 0;
                    } catch (Exception e) {
                        LogUtil.e(AmbientLightCapability.TAG, "handleEffectData exception " + e);
                    }
                }
            };
            this.scriptEffectTimer.scheduleAtFixedRate(timerTask, 0L, this.mATLEffect.getEffectData().get(this.loopingNum).getTimeInterval(), TimeUnit.MILLISECONDS);
            this.previousGroupNum = this.mATLEffect.getEffectData().get(this.currentMaxEffectNum - 1).getGroupNum();
        } catch (Exception e) {
            LogUtil.e(TAG, "handleEffectData run exception " + e);
        }
    }

    public int requestResource() {
        CarResourceInfo.Builder mBuilder = new CarResourceInfo.Builder();
        this.mCarResourceInfo = mBuilder.setModule(MODULE_NAME).setResourceType(RESOURCE_TYPE).setPriority(0).setDescription("Ambient Light Resource").createResourceInfo();
        return this.mCarResourceManager.acquireResource(this.mCarResourceInfo);
    }

    public int releaseResource() {
        Settings.System.putString(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "AmbientLightEffectType", "effect_stable_effect_new");
        if (getAmbientLightOpen()) {
            setAmbientLightEffect(getAmbientLightEffectType(), 5);
        }
        return this.mCarResourceManager.releaseResource(MODULE_NAME, RESOURCE_TYPE);
    }

    public int pausePlay() {
        stopEffect();
        return 0;
    }

    public int stopPlay() {
        stopEffect();
        setGroupLightData(3, (byte) 1, (byte) 0, (byte) 0);
        return 0;
    }

    private int[] jsonToIntArr(JSONObject jsonData, String infoType) {
        try {
            JSONArray jsonArray = jsonData.getJSONArray(infoType);
            int len = jsonArray.length();
            int[] infoAarr = new int[len];
            for (int i = 0; i < len; i++) {
                infoAarr[i] = jsonArray.getInt(i);
            }
            return infoAarr;
        } catch (Exception e) {
            LogUtil.e(TAG, "jsonToIntArr exception " + e);
            return new int[1];
        }
    }

    public int getMusicRhythmMode() {
        return this.mATLEffect.getMusicRhythmMode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isVisualizerEnable() {
        VisualizerService visualizerService = this.mVisualizerService;
        if (visualizerService != null) {
            return visualizerService.isVisualizerEnable(0);
        }
        return false;
    }

    public int requestPermission(boolean apply) {
        if (apply) {
            this.mAmbientLightHal.setAtlOpen(1);
            setBrightness(100);
        } else {
            setAmbientLightMemoryBright(getAmbientLightBright());
            setAmbientLightEffectType(getAmbientLightEffectType());
            setAmbientLightOpen(getAmbientLightOpen());
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class ATLEffect {
        List<EffectData> EffectData;
        String effectName;
        int effectType;
        int enableMusicSpectrum = 0;
        int enableSpeedData = 0;
        int executeTime = 0;
        int enableDefaultData = 0;
        int enableDrivingGroup = 0;
        int enableUserSelectedColor = 0;
        int musicRhythmMode = 1;

        public ATLEffect setEffectName(String effectName) {
            this.effectName = effectName;
            return this;
        }

        public String getEffectName() {
            return this.effectName;
        }

        public ATLEffect setEffectData(List<EffectData> effectData) {
            this.EffectData = effectData;
            return this;
        }

        public List<EffectData> getEffectData() {
            return this.EffectData;
        }

        public ATLEffect setEffectType(int effectType) {
            this.effectType = effectType;
            return this;
        }

        public int getEffectType() {
            return this.effectType;
        }

        public ATLEffect setEnableMusicSpectrum(int enableMusicSpectrum) {
            this.enableMusicSpectrum = enableMusicSpectrum;
            return this;
        }

        public int getEnableMusicSpectrum() {
            return this.enableMusicSpectrum;
        }

        public ATLEffect setEnableSpeedData(int enableSpeedData) {
            this.enableSpeedData = enableSpeedData;
            return this;
        }

        public int getEnableSpeedData() {
            return this.enableSpeedData;
        }

        public ATLEffect setExecuteTime(int executeTime) {
            this.executeTime = executeTime;
            return this;
        }

        public int getExecuteTime() {
            return this.executeTime;
        }

        public ATLEffect setDefaultData(int enableDefaultData) {
            this.enableDefaultData = enableDefaultData;
            return this;
        }

        public int getDefaultData() {
            return this.enableDefaultData;
        }

        public ATLEffect setEnableDrivingGroup(int enableDrivingGroup) {
            this.enableDrivingGroup = enableDrivingGroup;
            return this;
        }

        public int getEnableDrivingGroup() {
            return this.enableDrivingGroup;
        }

        public ATLEffect setEnableUserSelectedColor(int enableUserSelectedColor) {
            this.enableUserSelectedColor = enableUserSelectedColor;
            return this;
        }

        public int getEnableUserSelectedColor() {
            return this.enableUserSelectedColor;
        }

        public ATLEffect setMusicRhythmMode(int musicRhythmMode) {
            this.musicRhythmMode = musicRhythmMode;
            return this;
        }

        public int getMusicRhythmMode() {
            return this.musicRhythmMode;
        }

        /* loaded from: classes5.dex */
        public static class EffectData {
            int groupNum = 3;
            int color = 11;
            int bright = 100;
            int fade = 0;
            int timeInterval = 1000;
            int periodTime = 2000;
            long delayTime = 0;
            boolean colorSwitch = false;
            int enableRandom = 0;
            int randomMax = 0;
            int randomMin = 0;
            List<Integer> randomArray = new ArrayList();

            public EffectData setColor(int color) {
                this.color = color;
                return this;
            }

            public EffectData setBrightness(int bright) {
                this.bright = bright;
                return this;
            }

            public EffectData setFade(int fade) {
                this.fade = fade;
                return this;
            }

            public EffectData setTimeInterval(int timeInterval) {
                this.timeInterval = timeInterval;
                return this;
            }

            public EffectData setPeriodTime(int periodTime) {
                this.periodTime = periodTime;
                return this;
            }

            public EffectData setGroupNum(int groupNum) {
                this.groupNum = groupNum;
                return this;
            }

            public EffectData setDelayTime(long delayTime) {
                this.delayTime = delayTime;
                return this;
            }

            public EffectData setColorswitch(boolean colorSwitch) {
                this.colorSwitch = colorSwitch;
                return this;
            }

            public EffectData setEnableRandom(int enableRandom) {
                this.enableRandom = enableRandom;
                return this;
            }

            public int getEnableRandom() {
                return this.enableRandom;
            }

            public EffectData setRandomMax(int randomMax) {
                this.randomMax = randomMax;
                return this;
            }

            public int getRandomMax() {
                return this.randomMax;
            }

            public EffectData setRandomMin(int randomMin) {
                this.randomMin = randomMin;
                return this;
            }

            public int getRandomMin() {
                return this.randomMin;
            }

            public EffectData setRandomArray(List<Integer> randomArray) {
                this.randomArray = randomArray;
                return this;
            }

            public List<Integer> getRandomArray() {
                return this.randomArray;
            }

            public int getColor() {
                return this.color;
            }

            public int getBrightness() {
                return this.bright;
            }

            public int getFade() {
                return this.fade;
            }

            public int getTimeInterval() {
                return this.timeInterval;
            }

            public int getPeriodTime() {
                return this.periodTime;
            }

            public int getGroupNum() {
                return this.groupNum;
            }

            public long getDelayTime() {
                return this.delayTime;
            }

            public boolean getColorswitch() {
                return this.colorSwitch;
            }
        }
    }
}
