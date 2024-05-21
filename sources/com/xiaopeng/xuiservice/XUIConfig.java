package com.xiaopeng.xuiservice;

import android.car.hardware.mcu.CarMcuManager;
import android.os.SystemProperties;
import android.text.TextUtils;
import com.xiaopeng.util.FeatureOption;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
/* loaded from: classes5.dex */
public class XUIConfig {
    public static final int AMBIENT_LIGHT_CONTROLLER = 2;
    public static final int AMBIENT_LIGHT_LITE = 1;
    public static final int AMBIENT_LIGHT_NORMAL = 0;
    public static final int AMBIENT_LIGHT_NO_ATL = -1;
    public static final int BOOT_SOUND_SUPPORT_TYPE_ATLS = 0;
    public static final int BOOT_SOUND_SUPPORT_TYPE_IG_STATUS = 1;
    public static final int CFC_CODE_HIGH = 3;
    public static final int CFC_CODE_INVALID = 0;
    public static final int CFC_CODE_LOW = 1;
    public static final int CFC_CODE_MIDDLE = 2;
    public static final int CFC_CODE_TRIP = 4;
    private static final String HARDWARE_SOFTWARE_VERSION_PROPERTY = "ro.xiaopeng.software";
    public static final int HAS_CIU = 1;
    public static final int HAS_XPU = 0;
    public static final int ICM_TYPE_NEW = 1;
    public static final int ICM_TYPE_ORIGIN = 0;
    public static final int ICM_TYPE_SINGLEANDROID = 2;
    public static final int INVALID = 0;
    public static final int LLU_TYPE_DICTIONARY = 0;
    public static final int LLU_TYPE_INFINITE = 1;
    public static final String PROPERTY_AMBIENT_API = "persist.sys.xpeng.ambient.api";
    public static final String PROPERTY_AMBIENT_SERVICE = "persist.sys.xpeng.ambient.service";
    public static final String PROPERTY_AMP = "AMP";
    public static final String PROPERTY_AQS = "AQS";
    public static final String PROPERTY_ATLS = "ATLS";
    public static final String PROPERTY_AVAS = "AVAS";
    public static final String PROPERTY_AVM = "AVM";
    public static final String PROPERTY_AWARENESS = "awareness";
    public static final String PROPERTY_BOOT_COMPLETE = "sys.boot_completed";
    public static final String PROPERTY_CIU = "CIU";
    public static final String PROPERTY_CONTEXTINFO = "contextinfo";
    public static final String PROPERTY_CWC = "CWC";
    public static final String PROPERTY_DOLBY = "DOLBY";
    public static final String PROPERTY_FRIDGE_FEATURE = "refrigerator";
    public static final String PROPERTY_IMU = "IMU";
    public static final String PROPERTY_IPUF = "IPUF";
    public static final String PROPERTY_IPUR = "IPUR";
    public static final String PROPERTY_KARAOKE = "karaoke";
    public static final String PROPERTY_LLU = "LLU";
    public static final String PROPERTY_MAKEUP_MIRROR = "MAKEUP_MIRROR";
    public static final String PROPERTY_MEDIACENTER = "mediacenter";
    public static final String PROPERTY_MIRROR = "MIRROR";
    public static final String PROPERTY_MRR = "MRR";
    public static final String PROPERTY_MSB = "MSB";
    public static final String PROPERTY_MSMD = "MSMD";
    public static final String PROPERTY_MSMP = "MSMP";
    public static final String PROPERTY_MUSICRECOGNIZE = "musicrecognize";
    public static final String PROPERTY_NFC = "NFC";
    public static final String PROPERTY_PAS = "PAS";
    public static final String PROPERTY_RLS = "RLS";
    public static final String PROPERTY_SCISSORGATE = "scissorsGate";
    public static final String PROPERTY_SCU = "SCU";
    public static final String PROPERTY_SFS_FEATURE = "SFS";
    public static final String PROPERTY_SHC = "SHC";
    public static final String PROPERTY_SMART = "smart";
    public static final String PROPERTY_SRR_FL = "SRR_FL";
    public static final String PROPERTY_SRR_FR = "SRR_FR";
    public static final String PROPERTY_SRR_RL = "SRR_RL";
    public static final String PROPERTY_SRR_RR = "SRR_RR";
    public static final String PROPERTY_SVM = "FSEAT_RHYTHM";
    public static final String PROPERTY_TARGET_LOG_LEVEL = "persist.sys.xiaopeng.xuisvc.loglevel";
    public static final String PROPERTY_UVCCAMERA_PREVIEW_ENABLE = "persist.uvccamera.preview.enable";
    public static final String PROPERTY_VPM = "VPM";
    public static final String PROPERTY_XAPP = "xapp";
    public static final String PROPERTY_XPU = "XPU";
    private static final String TAG = "XUIConfig";
    public static final int UNKNOW = -1;
    public static final String XUI_SYS_MEDIA_BOOT_PATH = "/system/media/audio/xiaopeng/cdu/boot/";
    public static final String XUI_SYS_MEDIA_MP3_PATH = "/system/media/audio/xiaopeng/cdu/mp3/";
    public static final String XUI_SYS_MEDIA_PATH = "/system/media/audio/xiaopeng/cdu/";
    public static final String XUI_SYS_MEDIA_WAV_PATH = "/system/media/audio/xiaopeng/cdu/wav/";
    public static final String XUI_SYS_RSC_PATH = "/system/etc/xuiservice/";
    public static final String XUI_USER_RSC_PATH = "/data/xuiservice/";
    private static Integer sCfcCode = null;
    private static Boolean mHasCiuDevice = null;

    /* loaded from: classes5.dex */
    public static class BusinessModule {
        public static final String MODULE_IOT = "iot";
        public static final String MODULE_OPENCAR = "opencar";
        public static final String MODULE_UVCCAMERA = "uvccamera";
        public static final String MODULE_MAIN = "main";
        private static String mModule = MODULE_MAIN;

        public static String getBusinessModule() {
            return mModule;
        }

        public static void setBusinessModule(String module) {
            mModule = module;
        }

        public static boolean isMainProcess() {
            return MODULE_MAIN.equals(mModule);
        }
    }

    public static boolean hasFeature(String featureName) {
        return FeatureOption.hasFeature(featureName);
    }

    public static String getFeatureType(String featureName) {
        return FeatureOption.getFeatureType(featureName);
    }

    public static int getProductSeries() {
        return FeatureOption.FO_PRODUCT_SERIES;
    }

    public static boolean isInternationalEnable() {
        return FeatureOption.FO_DEVICE_INTERNATIONAL_ENABLED;
    }

    public static boolean isMiniProgramEnable() {
        return FeatureOption.FO_MINIRPOG_ENABLED;
    }

    public static int getAtlType() {
        return FeatureOption.FO_ATL_TYPE;
    }

    public static int getLluType() {
        return FeatureOption.FO_LLU_TYPE;
    }

    public static int getLluFrontNumType() {
        return FeatureOption.FO_LLU_FRONT_NUM;
    }

    public static int getLluRearNumType() {
        return FeatureOption.FO_LLU_REAR_NUM;
    }

    public static int getIcmType() {
        return FeatureOption.FO_ICM_TYPE;
    }

    public static int getAvasSupportType() {
        return FeatureOption.FO_AVAS_SUPPORT_TYPE;
    }

    public static boolean isAiLluEnable() {
        return FeatureOption.FO_AI_LLU_ENABLED;
    }

    public static boolean isA2DPAutoDisconnectEnable() {
        return FeatureOption.FO_A2DP_AUTODISCONNECT_ENABLED;
    }

    public static boolean isFlyGameModeEnabled() {
        return FeatureOption.FO_FLY_GAME_ENABLED;
    }

    public static int getBootSoundSupportType() {
        return FeatureOption.FO_BOOT_SOUND_SUPPORT_TYPE;
    }

    public static int getBacklightOvertempProtect() {
        return FeatureOption.FO_BACKLIGHT_OVERTEMP_PROTECT;
    }

    public static int getAutoBrightnessType() {
        return FeatureOption.FO_AUTO_BRIGHTNESS_TYPE;
    }

    public static boolean isAudioStereoEnable() {
        return FeatureOption.FO_AUDIO_STEREO_ENABLED;
    }

    public static boolean isXuiProcessHelpLaunch() {
        return FeatureOption.FO_XUI_PROCESS_HELP_LAUNCH;
    }

    public static boolean isPhoneMentionSupport() {
        return FeatureOption.FO_SUPPORT_PHONE_MENTION;
    }

    public static boolean isFragranceNewProtocol() {
        return 1 == FeatureOption.FO_FRAGRANCE_PROTOCAL;
    }

    public static boolean isAnimationSupported() {
        return FeatureOption.FO_ANIMA_SUPPORT && hasFeature("XSPORT");
    }

    public static boolean isSeatMassagerSupport() {
        return hasFeature("MSMD_MASSG") || hasFeature("MSMP_MASSG") || hasFeature("SECROW_LT_MASSG") || hasFeature("SECROW_RT_MASSG");
    }

    public static int getProjectUIType() {
        return FeatureOption.FO_PROJECT_UI_TYPE;
    }

    public static boolean isNapaUIType() {
        return FeatureOption.FO_PROJECT_UI_TYPE == 2;
    }

    public static int getEpsType() {
        return FeatureOption.FO_EPS_TYPE;
    }

    public static int getCfcCode() {
        if (sCfcCode == null) {
            int cfc = 0;
            String cfcInfo = SystemProperties.get(FeatureOption.FO_CFC_CODE_PROPERTY, (String) null);
            if (!TextUtils.isEmpty(cfcInfo)) {
                try {
                    cfc = Integer.parseInt(cfcInfo);
                } catch (Exception e) {
                }
            }
            if (cfc == 0) {
                cfc = 1;
            }
            sCfcCode = Integer.valueOf(cfc);
            LogUtil.i(TAG, "getCfcCode: " + sCfcCode);
        }
        return sCfcCode.intValue();
    }

    public static boolean isProductSeriesD() {
        return FeatureOption.FO_PRODUCT_SERIES == 0;
    }

    public static int autoBrightness() {
        if (hasFeature(PROPERTY_XPU)) {
            return 0;
        }
        if (hasCiu()) {
            return 1;
        }
        return -1;
    }

    public static boolean avasSayhiDefault() {
        return FeatureOption.FO_AVAS_SAYHI_DEFAULT;
    }

    public static boolean hasCiu() {
        if (mHasCiuDevice == null) {
            mHasCiuDevice = Boolean.valueOf(hasCiuDevice());
        }
        return mHasCiuDevice.booleanValue();
    }

    public static boolean hasXpu() {
        return hasFeature(PROPERTY_XPU);
    }

    public static boolean isNativeEcallEnable() {
        return FeatureOption.FO_NATIVE_E_CALL_ENABLE;
    }

    private static boolean hasCiuDevice() {
        int state = 2;
        CarMcuManager mcuManager = CarClientManager.getInstance().getCarManager("xp_mcu");
        if (mcuManager != null) {
            try {
                state = mcuManager.getCiuState();
                LogUtil.d(TAG, "carservice getCiuState:" + state);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return state == 1;
    }

    public static boolean isAmbientNewApi() {
        return SystemProperties.getInt(PROPERTY_AMBIENT_API, 1) == 1;
    }

    public static boolean isAmbientNewService() {
        return SystemProperties.getInt(PROPERTY_AMBIENT_SERVICE, 0) == 1;
    }

    public static boolean isUVCEnabled() {
        return SystemProperties.getBoolean(PROPERTY_UVCCAMERA_PREVIEW_ENABLE, false);
    }

    public static boolean hasPassengerBluetooth() {
        return FeatureOption.FO_PASSENGER_BT_AUDIO_EXIST > 0;
    }

    public static boolean isRemoteServiceEnable() {
        return FeatureOption.FO_REMOTE_SERVICE_ENABLE;
    }
}
