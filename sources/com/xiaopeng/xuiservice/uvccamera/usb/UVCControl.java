package com.xiaopeng.xuiservice.uvccamera.usb;

import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
/* loaded from: classes5.dex */
public class UVCControl {
    public static final int CT_AE_MODE_CONTROL = 2;
    public static final int CT_AE_PRIORITY_CONTROL = 4;
    public static final int CT_EXPOSURE_TIME_ABSOLUTE_CONTROL = 8;
    public static final int CT_EXPOSURE_TIME_RELATIVE_CONTROL = 16;
    public static final int CT_FOCUS_ABSOLUTE_CONTROL = 32;
    public static final int CT_FOCUS_AUTO_CONTROL = 131072;
    public static final int CT_FOCUS_RELATIVE_CONTROL = 64;
    public static final int CT_FOCUS_SIMPLE_CONTROL = 524288;
    public static final int CT_IRIS_ABSOLUTE_CONTROL = 128;
    public static final int CT_IRIS_RELATIVE_CONTROL = 256;
    public static final int CT_PANTILT_ABSOLUTE_CONTROL = 2048;
    public static final int CT_PANTILT_RELATIVE_CONTROL = 4096;
    public static final int CT_PRIVACY_CONTROL = 262144;
    public static final int CT_ROLL_ABSOLUTE_CONTROL = 8192;
    public static final int CT_ROLL_RELATIVE_CONTROL = 16384;
    public static final int CT_SCANNING_MODE_CONTROL = 1;
    public static final int CT_WINDOW_CONTROL = 1048576;
    public static final int CT_ZOOM_ABSOLUTE_CONTROL = 512;
    public static final int CT_ZOOM_RELATIVE_CONTROL = 1024;
    public static final int PU_ANALOG_LOCK_STATUS_CONTROL = -2147352576;
    public static final int PU_ANALOG_VIDEO_STANDARD_CONTROL = -2147418112;
    public static final int PU_BACKLIGHT_COMPENSATION_CONTROL = -2147483392;
    public static final int PU_BRIGHTNESS_CONTROL = -2147483647;
    public static final int PU_CONTRAST_AUTO_CONTROL = -2147221504;
    public static final int PU_CONTRAST_CONTROL = -2147483646;
    public static final int PU_DIGITAL_MULTIPLIER_CONTROL = -2147467264;
    public static final int PU_DIGITAL_MULTIPLIER_LIMIT_CONTROL = -2147450880;
    public static final int PU_GAIN_CONTROL = -2147483136;
    public static final int PU_GAMMA_CONTROL = -2147483616;
    public static final int PU_HUE_AUTO_CONTROL = -2147481600;
    public static final int PU_HUE_CONTROL = -2147483644;
    public static final int PU_POWER_LINE_FREQUENCY_CONTROL = -2147482624;
    public static final int PU_SATURATION_CONTROL = -2147483640;
    public static final int PU_SHARPNESS_CONTROL = -2147483632;
    public static final int PU_WHITE_BALANCE_COMPONENT_AUTO_CONTROL = -2147475456;
    public static final int PU_WHITE_BALANCE_COMPONENT_CONTROL = -2147483520;
    public static final int PU_WHITE_BALANCE_TEMPERATURE_AUTO_CONTROL = -2147479552;
    public static final int PU_WHITE_BALANCE_TEMPERATURE_CONTROL = -2147483584;
    public static final int UVC_AUTO_EXPOSURE_MODE_APERTURE_PRIORITY = 8;
    public static final int UVC_AUTO_EXPOSURE_MODE_AUTO = 2;
    public static final int UVC_AUTO_EXPOSURE_MODE_MANUAL = 1;
    public static final int UVC_AUTO_EXPOSURE_MODE_SHUTTER_PRIORITY = 4;
    protected int mAnalogVideoLockStateDef;
    protected int mAnalogVideoLockStateMax;
    protected int mAnalogVideoLockStateMin;
    protected int mAnalogVideoStandardDef;
    protected int mAnalogVideoStandardMax;
    protected int mAnalogVideoStandardMin;
    protected int mAutoExposureModeDef;
    protected int mAutoExposureModeMax;
    protected int mAutoExposureModeMin;
    protected int mAutoExposurePriorityDef;
    protected int mAutoExposurePriorityMax;
    protected int mAutoExposurePriorityMin;
    protected int mBacklightCompDef;
    protected int mBacklightCompMax;
    protected int mBacklightCompMin;
    protected int mBrightnessDef;
    protected int mBrightnessMax;
    protected int mBrightnessMin;
    protected long mCameraTerminalControls;
    protected int mContrastAutoDef;
    protected int mContrastAutoMax;
    protected int mContrastAutoMin;
    protected int mContrastDef;
    protected int mContrastMax;
    protected int mContrastMin;
    protected int mDigitalMultiplierDef;
    protected int mDigitalMultiplierLimitDef;
    protected int mDigitalMultiplierLimitMax;
    protected int mDigitalMultiplierLimitMin;
    protected int mDigitalMultiplierMax;
    protected int mDigitalMultiplierMin;
    protected int mExposureTimeDef;
    protected int mExposureTimeMax;
    protected int mExposureTimeMin;
    protected int mExposureTimeRelativeDef;
    protected int mExposureTimeRelativeMax;
    protected int mExposureTimeRelativeMin;
    protected int mFocusAbsoluteDef;
    protected int mFocusAbsoluteMax;
    protected int mFocusAbsoluteMin;
    protected int mFocusAutoDef;
    protected int mFocusAutoMax;
    protected int mFocusAutoMin;
    protected int mFocusRelativeDef;
    protected int mFocusRelativeMax;
    protected int mFocusRelativeMin;
    protected int mFocusSimpleDef;
    protected int mFocusSimpleMax;
    protected int mFocusSimpleMin;
    protected int mGainDef;
    protected int mGainMax;
    protected int mGainMin;
    protected int mGammaDef;
    protected int mGammaMax;
    protected int mGammaMin;
    protected int mHueAutoDef;
    protected int mHueAutoMax;
    protected int mHueAutoMin;
    protected int mHueDef;
    protected int mHueMax;
    protected int mHueMin;
    protected int mIrisAbsoluteDef;
    protected int mIrisAbsoluteMax;
    protected int mIrisAbsoluteMin;
    protected int mIrisRelativeDef;
    protected int mIrisRelativeMax;
    protected int mIrisRelativeMin;
    protected long mNativePtr;
    protected int mPanAbsoluteDef;
    protected int mPanAbsoluteMax;
    protected int mPanAbsoluteMin;
    protected int mPanRelativeDef;
    protected int mPanRelativeMax;
    protected int mPanRelativeMin;
    protected int mPowerlineFrequencyDef;
    protected int mPowerlineFrequencyMax;
    protected int mPowerlineFrequencyMin;
    protected int mPrivacyDef;
    protected int mPrivacyMax;
    protected int mPrivacyMin;
    protected long mProcessingUnitControls;
    protected int mRollDef;
    protected int mRollMax;
    protected int mRollMin;
    protected int mRollRelativeDef;
    protected int mRollRelativeMax;
    protected int mRollRelativeMin;
    protected int mSaturationDef;
    protected int mSaturationMax;
    protected int mSaturationMin;
    protected int mScanningModeDef;
    protected int mScanningModeMax;
    protected int mScanningModeMin;
    protected int mSharpnessDef;
    protected int mSharpnessMax;
    protected int mSharpnessMin;
    protected int mTiltAbsoluteDef;
    protected int mTiltAbsoluteMax;
    protected int mTiltAbsoluteMin;
    protected int mTiltRelativeDef;
    protected int mTiltRelativeMax;
    protected int mTiltRelativeMin;
    protected int mWhiteBalanceAutoDef;
    protected int mWhiteBalanceAutoMax;
    protected int mWhiteBalanceAutoMin;
    protected int mWhiteBalanceCompoAutoDef;
    protected int mWhiteBalanceCompoAutoMax;
    protected int mWhiteBalanceCompoAutoMin;
    protected int mWhiteBalanceCompoDef;
    protected int mWhiteBalanceCompoMax;
    protected int mWhiteBalanceCompoMin;
    protected int mWhiteBalanceDef;
    protected int mWhiteBalanceMax;
    protected int mWhiteBalanceMin;
    protected int mZoomAbsoluteDef;
    protected int mZoomAbsoluteMax;
    protected int mZoomAbsoluteMin;
    protected int mZoomRelativeDef;
    protected int mZoomRelativeMax;
    protected int mZoomRelativeMin;
    private static final String TAG = UVCControl.class.getSimpleName();
    private static final String[] CAMERA_TERMINAL_DESCS = {"D0:  Scanning Mode", "D1:  Auto-Exposure Mode", "D2:  Auto-Exposure Priority", "D3:  Exposure Time (Absolute)", "D4:  Exposure Time (Relative)", "D5:  Focus (Absolute)", "D6:  Focus (Relative)", "D7:  Iris (Absolute)", "D8:  Iris (Relative)", "D9:  Zoom (Absolute)", "D10: Zoom (Relative)", "D11: PanTilt (Absolute)", "D12: PanTilt (Relative)", "D13: Roll (Absolute)", "D14: Roll (Relative)", "D15: Reserved", "D16: Reserved", "D17: Focus, Auto", "D18: Privacy", "D19: Focus, Simple", "D20: Window", "D21: Region of Interest", "D22: Reserved, set to zero", "D23: Reserved, set to zero"};
    private static final String[] PROCESSING_UNIT_DESCS = {"D0: Brightness", "D1: Contrast", "D2: Hue", "D3: Saturation", "D4: Sharpness", "D5: Gamma", "D6: White Balance Temperature", "D7: White Balance Component", "D8: Backlight Compensation", "D9: Gain", "D10: Power Line Frequency", "D11: Hue, Auto", "D12: White Balance Temperature, Auto", "D13: White Balance Component, Auto", "D14: Digital Multiplier", "D15: Digital Multiplier Limit", "D16: Analog Video Standard", "D17: Analog Video Lock Status", "D18: Contrast, Auto", "D19: Reserved. Set to zero", "D20: Reserved. Set to zero", "D21: Reserved. Set to zero", "D22: Reserved. Set to zero", "D23: Reserved. Set to zero"};

    private native int nativeGetAnalogVideoLockState(long j);

    private native int nativeGetAnalogVideoStandard(long j);

    private native int nativeGetAutoExposureMode(long j);

    private native int nativeGetAutoExposurePriority(long j);

    private native int nativeGetBacklightComp(long j);

    private native int nativeGetBrightness(long j);

    private native long nativeGetCameraTerminalControls(long j);

    private native int nativeGetContrast(long j);

    private native int nativeGetContrastAuto(long j);

    private native int nativeGetDigitalMultiplier(long j);

    private native int nativeGetDigitalMultiplierLimit(long j);

    private native int nativeGetExposureTimeAbsolute(long j);

    private native int nativeGetExposureTimeRelative(long j);

    private native int nativeGetFocusAbsolute(long j);

    private native int nativeGetFocusAuto(long j);

    private native int nativeGetFocusRelative(long j);

    private native int nativeGetGain(long j);

    private native int nativeGetGamma(long j);

    private native int nativeGetHue(long j);

    private native int nativeGetHueAuto(long j);

    private native int nativeGetIrisAbsolute(long j);

    private native int nativeGetIrisRelative(long j);

    private native int nativeGetPanAbsolute(long j);

    private native int nativeGetPanRelative(long j);

    private native int nativeGetPowerlineFrequency(long j);

    private native int nativeGetPrivacy(long j);

    private native long nativeGetProcessingUnitControls(long j);

    private native int nativeGetRollAbsolute(long j);

    private native int nativeGetRollRelative(long j);

    private native int nativeGetSaturation(long j);

    private native int nativeGetScanningMode(long j);

    private native int nativeGetSharpness(long j);

    private native int nativeGetTiltAbsolute(long j);

    private native int nativeGetTiltRelative(long j);

    private native int nativeGetWhiteBalance(long j);

    private native int nativeGetWhiteBalanceAuto(long j);

    private native int nativeGetWhiteBalanceCompo(long j);

    private native int nativeGetWhiteBalanceCompoAuto(long j);

    private native int nativeGetZoomAbsolute(long j);

    private native int nativeGetZoomRelative(long j);

    private native int[] nativeObtainAnalogVideoLockStateLimit(long j);

    private native int[] nativeObtainAnalogVideoStandardLimit(long j);

    private native int[] nativeObtainAutoExposureModeLimit(long j);

    private native int[] nativeObtainAutoExposurePriorityLimit(long j);

    private native int[] nativeObtainBacklightCompLimit(long j);

    private native int[] nativeObtainBrightnessLimit(long j);

    private native int[] nativeObtainContrastAutoLimit(long j);

    private native int[] nativeObtainContrastLimit(long j);

    private native int[] nativeObtainDigitalMultiplierLimit(long j);

    private native int[] nativeObtainDigitalMultiplierLimitLimit(long j);

    private native int[] nativeObtainExposureTimeAbsoluteLimit(long j);

    private native int[] nativeObtainExposureTimeRelativeLimit(long j);

    private native int[] nativeObtainFocusAbsoluteLimit(long j);

    private native int[] nativeObtainFocusAutoLimit(long j);

    private native int[] nativeObtainFocusRelativeLimit(long j);

    private native int[] nativeObtainGainLimit(long j);

    private native int[] nativeObtainGammaLimit(long j);

    private native int[] nativeObtainHueAutoLimit(long j);

    private native int[] nativeObtainHueLimit(long j);

    private native int[] nativeObtainIrisAbsoluteLimit(long j);

    private native int[] nativeObtainIrisRelativeLimit(long j);

    private native int[] nativeObtainPanAbsoluteLimit(long j);

    private native int[] nativeObtainPanRelativeLimit(long j);

    private native int[] nativeObtainPowerlineFrequencyLimit(long j);

    private native int[] nativeObtainPrivacyLimit(long j);

    private native int[] nativeObtainRollAbsoluteLimit(long j);

    private native int[] nativeObtainRollRelativeLimit(long j);

    private native int[] nativeObtainSaturationLimit(long j);

    private native int[] nativeObtainScanningModeLimit(long j);

    private native int[] nativeObtainSharpnessLimit(long j);

    private native int[] nativeObtainTiltAbsoluteLimit(long j);

    private native int[] nativeObtainTiltRelativeLimit(long j);

    private native int[] nativeObtainWhiteBalanceAutoLimit(long j);

    private native int[] nativeObtainWhiteBalanceCompoAutoLimit(long j);

    private native int[] nativeObtainWhiteBalanceCompoLimit(long j);

    private native int[] nativeObtainWhiteBalanceLimit(long j);

    private native int[] nativeObtainZoomAbsoluteLimit(long j);

    private native int[] nativeObtainZoomRelativeLimit(long j);

    private native int nativeSetAnalogVideoLockState(long j, int i);

    private native int nativeSetAnalogVideoStandard(long j, int i);

    private native int nativeSetAutoExposureMode(long j, int i);

    private native int nativeSetAutoExposurePriority(long j, int i);

    private native int nativeSetBacklightComp(long j, int i);

    private native int nativeSetBrightness(long j, int i);

    private native int nativeSetContrast(long j, int i);

    private native int nativeSetContrastAuto(long j, boolean z);

    private native int nativeSetDigitalMultiplier(long j, int i);

    private native int nativeSetDigitalMultiplierLimit(long j, int i);

    private native int nativeSetExposureTimeAbsolute(long j, int i);

    private native int nativeSetExposureTimeRelative(long j, int i);

    private native int nativeSetFocusAbsolute(long j, int i);

    private native int nativeSetFocusAuto(long j, boolean z);

    private native int nativeSetFocusRelative(long j, int i);

    private native int nativeSetGain(long j, int i);

    private native int nativeSetGamma(long j, int i);

    private native int nativeSetHue(long j, int i);

    private native int nativeSetHueAuto(long j, boolean z);

    private native int nativeSetIrisAbsolute(long j, int i);

    private native int nativeSetIrisRelative(long j, int i);

    private native int nativeSetPanAbsolute(long j, int i);

    private native int nativeSetPanRelative(long j, int i);

    private native int nativeSetPowerlineFrequency(long j, int i);

    private native int nativeSetPrivacy(long j, boolean z);

    private native int nativeSetRollAbsolute(long j, int i);

    private native int nativeSetRollRelative(long j, int i);

    private native int nativeSetSaturation(long j, int i);

    private native int nativeSetScanningMode(long j, int i);

    private native int nativeSetSharpness(long j, int i);

    private native int nativeSetTiltAbsolute(long j, int i);

    private native int nativeSetTiltRelative(long j, int i);

    private native int nativeSetWhiteBalance(long j, int i);

    private native int nativeSetWhiteBalanceAuto(long j, boolean z);

    private native int nativeSetWhiteBalanceCompo(long j, int i);

    private native int nativeSetWhiteBalanceCompoAuto(long j, boolean z);

    private native int nativeSetZoomAbsolute(long j, int i);

    private native int nativeSetZoomRelative(long j, int i);

    static {
        System.loadLibrary("jpeg-turbo212");
        System.loadLibrary("usb1.0");
        System.loadLibrary("uvc");
        System.loadLibrary("UVCCamera");
    }

    public UVCControl(long ptr) {
        this.mNativePtr = ptr;
        updateCameraParams();
    }

    public boolean checkSupportFlag(long flag) {
        updateCameraParams();
        return (flag & (-2147483648L)) == -2147483648L ? (this.mProcessingUnitControls & flag) == (2147483647L & flag) : (this.mCameraTerminalControls & flag) == flag;
    }

    public synchronized int[] updateScanningModeLimit() {
        int[] ints;
        ints = nativeObtainScanningModeLimit(this.mNativePtr);
        if (ints != null) {
            this.mScanningModeMin = ints[0];
            this.mScanningModeMax = ints[1];
            this.mScanningModeDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isScanningModeEnable() {
        return checkSupportFlag(1L);
    }

    public synchronized void setScanningMode(int mode) {
        nativeSetScanningMode(this.mNativePtr, mode);
    }

    public synchronized int getScanningMode() {
        return nativeGetScanningMode(this.mNativePtr);
    }

    public synchronized void resetScanningMode() {
        nativeSetScanningMode(this.mNativePtr, this.mScanningModeDef);
    }

    public synchronized int[] updateAutoExposureModeLimit() {
        int[] ints;
        ints = nativeObtainAutoExposureModeLimit(this.mNativePtr);
        if (ints != null) {
            this.mAutoExposureModeMin = ints[0];
            this.mAutoExposureModeMax = ints[1];
            this.mAutoExposureModeDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isAutoExposureModeEnable() {
        return checkSupportFlag(2L);
    }

    public synchronized void setAutoExposureMode(int mode) {
        nativeSetAutoExposureMode(this.mNativePtr, mode);
    }

    public synchronized int getAutoExposureMode() {
        return nativeGetAutoExposureMode(this.mNativePtr);
    }

    public synchronized void resetAutoExposureMode() {
        nativeSetAutoExposureMode(this.mNativePtr, this.mAutoExposureModeDef);
    }

    public synchronized void setExposureTimeAuto(boolean auto) {
        int mode = getAutoExposureMode();
        if (auto) {
            if (mode == 1) {
                mode = 8;
            } else if (mode == 4) {
                mode = 2;
            }
        } else if (mode == 2) {
            mode = 4;
        } else if (mode == 8) {
            mode = 1;
        }
        nativeSetAutoExposureMode(this.mNativePtr, mode);
    }

    public synchronized boolean isExposureTimeAuto() {
        int mode;
        mode = getAutoExposureMode();
        return mode == 2 || mode == 8;
    }

    public synchronized int[] updateAutoExposurePriorityLimit() {
        int[] ints;
        ints = nativeObtainAutoExposurePriorityLimit(this.mNativePtr);
        if (ints != null) {
            this.mAutoExposurePriorityMin = ints[0];
            this.mAutoExposurePriorityMax = ints[1];
            this.mAutoExposurePriorityDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isAutoExposurePriorityEnable() {
        return checkSupportFlag(4L);
    }

    public synchronized void setAutoExposurePriority(int priority) {
        nativeSetAutoExposurePriority(this.mNativePtr, priority);
    }

    public synchronized int getAutoExposurePriority() {
        return nativeGetAutoExposurePriority(this.mNativePtr);
    }

    public synchronized void resetAutoExposurePriority() {
        nativeSetAutoExposurePriority(this.mNativePtr, this.mAutoExposurePriorityDef);
    }

    public synchronized int[] updateExposureTimeAbsoluteLimit() {
        int[] ints;
        ints = nativeObtainExposureTimeAbsoluteLimit(this.mNativePtr);
        if (ints != null) {
            this.mExposureTimeMin = ints[0];
            this.mExposureTimeMax = ints[1];
            this.mExposureTimeDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isExposureTimeAbsoluteEnable() {
        return checkSupportFlag(8L);
    }

    public synchronized void setExposureTimeAbsolute(int time) {
        nativeSetExposureTimeAbsolute(this.mNativePtr, time);
    }

    public synchronized int getExposureTimeAbsolute() {
        return nativeGetExposureTimeAbsolute(this.mNativePtr);
    }

    public synchronized void resetExposureTimeAbsolute() {
        nativeSetExposureTimeAbsolute(this.mNativePtr, this.mExposureTimeDef);
    }

    public synchronized int[] updateExposureTimeRelativeLimit() {
        int[] ints;
        ints = nativeObtainExposureTimeRelativeLimit(this.mNativePtr);
        if (ints != null) {
            this.mExposureTimeRelativeMin = ints[0];
            this.mExposureTimeRelativeMax = ints[1];
            this.mExposureTimeRelativeDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isExposureTimeRelativeEnable() {
        return checkSupportFlag(16L);
    }

    public synchronized void setExposureTimeRelative(int step) {
        nativeSetExposureTimeRelative(this.mNativePtr, step);
    }

    public synchronized int getExposureTimeRelative() {
        return nativeGetExposureTimeRelative(this.mNativePtr);
    }

    public synchronized void resetExposureTimeRelative() {
        nativeSetExposureTimeRelative(this.mNativePtr, this.mExposureTimeRelativeDef);
    }

    public synchronized int[] updateFocusAbsoluteLimit() {
        int[] ints;
        ints = nativeObtainFocusAbsoluteLimit(this.mNativePtr);
        if (ints != null) {
            this.mFocusAbsoluteMin = ints[0];
            this.mFocusAbsoluteMax = ints[1];
            this.mFocusAbsoluteDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isFocusAbsoluteEnable() {
        return checkSupportFlag(32L);
    }

    public synchronized void setFocusAbsolute(int focus) {
        nativeSetFocusAbsolute(this.mNativePtr, focus);
    }

    public synchronized int getFocusAbsolute() {
        return nativeGetFocusAbsolute(this.mNativePtr);
    }

    public synchronized void resetFocusAbsolute() {
        nativeSetFocusAbsolute(this.mNativePtr, this.mFocusAbsoluteDef);
    }

    public synchronized void setFocusAbsolutePercent(int percent) {
        float range = Math.abs(this.mFocusAbsoluteMax - this.mFocusAbsoluteMin);
        if (range > 0.0f) {
            nativeSetFocusAbsolute(this.mNativePtr, ((int) ((percent / 100.0f) * range)) + this.mFocusAbsoluteMin);
        }
    }

    public synchronized int getFocusAbsolutePercent() {
        int result;
        result = 0;
        updateFocusAbsoluteLimit();
        float range = Math.abs(this.mFocusAbsoluteMax - this.mFocusAbsoluteMin);
        if (range > 0.0f) {
            result = (int) (((nativeGetFocusAbsolute(this.mNativePtr) - this.mFocusAbsoluteMin) * 100.0f) / range);
        }
        return result;
    }

    public synchronized int[] updateFocusRelativeLimit() {
        int[] ints;
        ints = nativeObtainFocusRelativeLimit(this.mNativePtr);
        if (ints != null) {
            this.mFocusRelativeMin = ints[0];
            this.mFocusRelativeMax = ints[1];
            this.mFocusRelativeDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isFocusRelativeEnable() {
        return checkSupportFlag(64L);
    }

    public synchronized void setFocusRelative(int focus) {
        nativeSetFocusRelative(this.mNativePtr, focus);
    }

    public synchronized int getFocusRelative() {
        return nativeGetFocusRelative(this.mNativePtr);
    }

    public synchronized void resetFocusRelative() {
        nativeSetFocusRelative(this.mNativePtr, this.mFocusAbsoluteDef);
    }

    public synchronized int[] updateIrisAbsoluteLimit() {
        int[] ints;
        ints = nativeObtainIrisAbsoluteLimit(this.mNativePtr);
        if (ints != null) {
            this.mIrisAbsoluteMin = ints[0];
            this.mIrisAbsoluteMax = ints[1];
            this.mIrisAbsoluteDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isIrisAbsoluteEnable() {
        return checkSupportFlag(128L);
    }

    public synchronized void setIrisAbsolute(int iris) {
        nativeSetIrisAbsolute(this.mNativePtr, iris);
    }

    public synchronized int getIrisAbsolute() {
        return nativeGetIrisAbsolute(this.mNativePtr);
    }

    public synchronized void resetIrisAbsolute() {
        nativeSetIrisAbsolute(this.mNativePtr, this.mIrisAbsoluteDef);
    }

    public synchronized int[] updateIrisRelativeLimit() {
        int[] ints;
        ints = nativeObtainIrisRelativeLimit(this.mNativePtr);
        if (ints != null) {
            this.mIrisRelativeMin = ints[0];
            this.mIrisRelativeMax = ints[1];
            this.mIrisRelativeDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isIrisRelativeEnable() {
        return checkSupportFlag(256L);
    }

    public synchronized void setIrisRelative(int iris) {
        nativeSetIrisRelative(this.mNativePtr, iris);
    }

    public synchronized int getIrisRelative() {
        return nativeGetIrisRelative(this.mNativePtr);
    }

    public synchronized void resetIrisRelative() {
        nativeSetIrisRelative(this.mNativePtr, this.mIrisRelativeDef);
    }

    public synchronized int[] updateZoomAbsoluteLimit() {
        int[] ints;
        ints = nativeObtainZoomAbsoluteLimit(this.mNativePtr);
        if (ints != null) {
            this.mZoomAbsoluteMin = ints[0];
            this.mZoomAbsoluteMax = ints[1];
            this.mZoomAbsoluteDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isZoomAbsoluteEnable() {
        return checkSupportFlag(512L);
    }

    public synchronized void setZoomAbsolute(int zoom) {
        nativeSetZoomAbsolute(this.mNativePtr, zoom);
    }

    public synchronized int getZoomAbsolute() {
        return nativeGetZoomAbsolute(this.mNativePtr);
    }

    public synchronized void resetZoomAbsolute() {
        nativeSetZoomAbsolute(this.mNativePtr, this.mZoomAbsoluteDef);
    }

    public synchronized void setZoomAbsolutePercent(int percent) {
        float range = Math.abs(this.mZoomAbsoluteMax - this.mZoomAbsoluteMin);
        if (range > 0.0f) {
            int z = ((int) ((percent / 100.0f) * range)) + this.mZoomAbsoluteMin;
            nativeSetZoomAbsolute(this.mNativePtr, z);
        }
    }

    public synchronized int getZoomAbsolutePercent() {
        int result;
        result = 0;
        updateZoomAbsoluteLimit();
        float range = Math.abs(this.mZoomAbsoluteMax - this.mZoomAbsoluteMin);
        if (range > 0.0f) {
            result = (int) (((nativeGetZoomAbsolute(this.mNativePtr) - this.mZoomAbsoluteMin) * 100.0f) / range);
        }
        return result;
    }

    public synchronized int[] updateZoomRelativeLimit() {
        int[] ints;
        ints = nativeObtainZoomRelativeLimit(this.mNativePtr);
        if (ints != null) {
            this.mZoomRelativeMin = ints[0];
            this.mZoomRelativeMax = ints[1];
            this.mZoomRelativeDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isZoomRelativeEnable() {
        return checkSupportFlag(1024L);
    }

    public synchronized void setZoomRelative(int zoom) {
        nativeSetZoomRelative(this.mNativePtr, zoom);
    }

    public synchronized int getZoomRelative() {
        return nativeGetZoomRelative(this.mNativePtr);
    }

    public synchronized void resetZoomRelative() {
        nativeSetZoomRelative(this.mNativePtr, this.mZoomRelativeDef);
    }

    public synchronized int[] updatePanAbsoluteLimit() {
        int[] ints;
        ints = nativeObtainPanAbsoluteLimit(this.mNativePtr);
        if (ints != null) {
            this.mPanAbsoluteMin = ints[0];
            this.mPanAbsoluteMax = ints[1];
            this.mPanAbsoluteDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isPanAbsoluteEnable() {
        return checkSupportFlag(PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH);
    }

    public synchronized void setPanAbsolute(int pan) {
        nativeSetPanAbsolute(this.mNativePtr, pan);
    }

    public synchronized int getPanAbsolute() {
        return nativeGetPanAbsolute(this.mNativePtr);
    }

    public synchronized void resetPanAbsolute() {
        nativeSetPanAbsolute(this.mNativePtr, this.mPanAbsoluteDef);
    }

    public synchronized int[] updateTiltAbsoluteLimit() {
        int[] ints;
        ints = nativeObtainTiltAbsoluteLimit(this.mNativePtr);
        if (ints != null) {
            this.mTiltAbsoluteMin = ints[0];
            this.mTiltAbsoluteMax = ints[1];
            this.mTiltAbsoluteDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isTiltAbsoluteEnable() {
        return checkSupportFlag(PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH);
    }

    public synchronized void setTiltAbsolute(int pan) {
        nativeSetTiltAbsolute(this.mNativePtr, pan);
    }

    public synchronized int getTiltAbsolute() {
        return nativeGetTiltAbsolute(this.mNativePtr);
    }

    public synchronized void resetTiltAbsolute() {
        nativeSetTiltAbsolute(this.mNativePtr, this.mTiltAbsoluteDef);
    }

    public synchronized int[] updatePanRelativeLimit() {
        int[] ints;
        ints = nativeObtainPanRelativeLimit(this.mNativePtr);
        if (ints != null) {
            this.mPanRelativeMin = ints[0];
            this.mPanRelativeMax = ints[1];
            this.mPanRelativeDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isPanRelativeEnable() {
        return checkSupportFlag(PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM);
    }

    public synchronized void setPanRelative(int PanRelative) {
        nativeSetPanRelative(this.mNativePtr, PanRelative);
    }

    public synchronized int getPanRelative() {
        return nativeGetPanRelative(this.mNativePtr);
    }

    public synchronized void resetPanRelative() {
        nativeSetPanRelative(this.mNativePtr, this.mPanRelativeDef);
    }

    public synchronized int[] updateTiltRelativeLimit() {
        int[] ints;
        ints = nativeObtainTiltRelativeLimit(this.mNativePtr);
        if (ints != null) {
            this.mTiltRelativeMin = ints[0];
            this.mTiltRelativeMax = ints[1];
            this.mTiltRelativeDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isTiltRelativeEnable() {
        return checkSupportFlag(PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM);
    }

    public synchronized void setTiltRelative(int TiltRelative) {
        nativeSetTiltRelative(this.mNativePtr, TiltRelative);
    }

    public synchronized int getTiltRelative() {
        return nativeGetTiltRelative(this.mNativePtr);
    }

    public synchronized void resetTiltRelative() {
        nativeSetTiltRelative(this.mNativePtr, this.mTiltRelativeDef);
    }

    public synchronized int[] updateRollAbsoluteLimit() {
        int[] ints;
        ints = nativeObtainRollAbsoluteLimit(this.mNativePtr);
        if (ints != null) {
            this.mRollMin = ints[0];
            this.mRollMax = ints[1];
            this.mRollDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isRollAbsoluteEnable() {
        return checkSupportFlag(PlaybackStateCompat.ACTION_PLAY_FROM_URI);
    }

    public synchronized void setRollAbsolute(int roll) {
        nativeSetRollAbsolute(this.mNativePtr, roll);
    }

    public synchronized int getRollAbsolute() {
        return nativeGetRollAbsolute(this.mNativePtr);
    }

    public synchronized void resetRollAbsolute() {
        nativeSetRollAbsolute(this.mNativePtr, this.mRollDef);
    }

    public synchronized int[] updateRollRelativeLimit() {
        int[] ints;
        ints = nativeObtainRollRelativeLimit(this.mNativePtr);
        if (ints != null) {
            this.mRollRelativeMin = ints[0];
            this.mRollRelativeMax = ints[1];
            this.mRollRelativeDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isRollRelativeEnable() {
        return checkSupportFlag(PlaybackStateCompat.ACTION_PREPARE);
    }

    public synchronized void setRollRelative(int roll) {
        nativeSetRollRelative(this.mNativePtr, roll);
    }

    public synchronized int getRollRelative() {
        return nativeGetRollRelative(this.mNativePtr);
    }

    public synchronized void resetRollRelative() {
        nativeSetRollRelative(this.mNativePtr, this.mRollRelativeDef);
    }

    public synchronized int[] updateFocusAutoLimit() {
        int[] ints;
        ints = nativeObtainFocusAutoLimit(this.mNativePtr);
        if (ints != null) {
            this.mFocusAutoMin = ints[0];
            this.mFocusAutoMax = ints[1];
            this.mFocusAutoDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isFocusAutoEnable() {
        return checkSupportFlag(PlaybackStateCompat.ACTION_PREPARE_FROM_URI);
    }

    public synchronized void setFocusAuto(boolean state) {
        nativeSetFocusAuto(this.mNativePtr, state);
    }

    public synchronized boolean getFocusAuto() {
        return nativeGetFocusAuto(this.mNativePtr) > 0;
    }

    public synchronized void resetFocusAuto() {
        nativeSetFocusAuto(this.mNativePtr, this.mFocusAutoDef > 0);
    }

    public synchronized int[] updatePrivacyLimit() {
        int[] ints;
        ints = nativeObtainPrivacyLimit(this.mNativePtr);
        if (ints != null) {
            this.mPrivacyMin = ints[0];
            this.mPrivacyMax = ints[1];
            this.mPrivacyDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isPrivacyEnable() {
        return checkSupportFlag(PlaybackStateCompat.ACTION_SET_REPEAT_MODE);
    }

    public synchronized void setPrivacy(boolean state) {
        nativeSetPrivacy(this.mNativePtr, state);
    }

    public synchronized boolean getPrivacy() {
        return nativeGetPrivacy(this.mNativePtr) > 0;
    }

    public synchronized void resetPrivacy() {
        nativeSetPrivacy(this.mNativePtr, this.mPrivacyDef > 0);
    }

    public synchronized int[] updateBrightnessLimit() {
        int[] ints;
        ints = nativeObtainBrightnessLimit(this.mNativePtr);
        if (ints != null) {
            this.mBrightnessMin = ints[0];
            this.mBrightnessMax = ints[1];
            this.mBrightnessDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isBrightnessEnable() {
        return checkSupportFlag(-2147483647L);
    }

    public synchronized void setBrightness(int brightness) {
        nativeSetBrightness(this.mNativePtr, brightness);
    }

    public synchronized int getBrightness() {
        return nativeGetBrightness(this.mNativePtr);
    }

    public synchronized void resetBrightness() {
        nativeSetBrightness(this.mNativePtr, this.mBrightnessDef);
    }

    public synchronized void setBrightnessPercent(int percent) {
        float range = Math.abs(this.mBrightnessMax - this.mBrightnessMin);
        if (range > 0.0f) {
            nativeSetBrightness(this.mNativePtr, ((int) ((percent / 100.0f) * range)) + this.mBrightnessMin);
        }
    }

    public synchronized int getBrightnessPercent() {
        int result;
        result = 0;
        updateBrightnessLimit();
        float range = Math.abs(this.mBrightnessMax - this.mBrightnessMin);
        if (range > 0.0f) {
            result = (int) (((nativeGetBrightness(this.mNativePtr) - this.mBrightnessMin) * 100.0f) / range);
        }
        return result;
    }

    public synchronized int[] updateContrastLimit() {
        int[] ints;
        ints = nativeObtainContrastLimit(this.mNativePtr);
        if (ints != null) {
            this.mContrastMin = ints[0];
            this.mContrastMax = ints[1];
            this.mContrastDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isContrastEnable() {
        return checkSupportFlag(-2147483646L);
    }

    public synchronized void setContrast(int contrast) {
        nativeSetContrast(this.mNativePtr, contrast);
    }

    public synchronized int getContrast() {
        return nativeGetContrast(this.mNativePtr);
    }

    public synchronized void resetContrast() {
        nativeSetContrast(this.mNativePtr, this.mContrastDef);
    }

    public synchronized void setContrastPercent(int percent) {
        updateContrastLimit();
        float range = Math.abs(this.mContrastMax - this.mContrastMin);
        if (range > 0.0f) {
            nativeSetContrast(this.mNativePtr, ((int) ((percent / 100.0f) * range)) + this.mContrastMin);
        }
    }

    public synchronized int getContrastPercent() {
        int result;
        result = 0;
        float range = Math.abs(this.mContrastMax - this.mContrastMin);
        if (range > 0.0f) {
            result = (int) (((nativeGetContrast(this.mNativePtr) - this.mContrastMin) * 100.0f) / range);
        }
        return result;
    }

    public synchronized int[] updateHueLimit() {
        int[] ints;
        ints = nativeObtainHueLimit(this.mNativePtr);
        if (ints != null) {
            this.mHueMin = ints[0];
            this.mHueMax = ints[1];
            this.mHueDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isHueEnable() {
        return checkSupportFlag(-2147483644L);
    }

    public synchronized void setHue(int hue) {
        nativeSetHue(this.mNativePtr, hue);
    }

    public synchronized int getHue() {
        return nativeGetHue(this.mNativePtr);
    }

    public synchronized void resetHue() {
        nativeSetHue(this.mNativePtr, this.mHueDef);
    }

    public synchronized void setHuePercent(int percent) {
        float range = Math.abs(this.mHueMax - this.mHueMin);
        if (range > 0.0f) {
            nativeSetHue(this.mNativePtr, ((int) ((percent / 100.0f) * range)) + this.mHueMin);
        }
    }

    public synchronized int getHuePercent() {
        int result;
        result = 0;
        nativeObtainHueLimit(this.mNativePtr);
        float range = Math.abs(this.mHueMax - this.mHueMin);
        if (range > 0.0f) {
            result = (int) (((nativeGetHue(this.mNativePtr) - this.mHueMin) * 100.0f) / range);
        }
        return result;
    }

    public synchronized int[] updateSaturationLimit() {
        int[] ints;
        ints = nativeObtainSaturationLimit(this.mNativePtr);
        if (ints != null) {
            this.mSaturationMin = ints[0];
            this.mSaturationMax = ints[1];
            this.mSaturationDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isSaturationEnable() {
        return checkSupportFlag(-2147483640L);
    }

    public synchronized void setSaturation(int saturation) {
        nativeSetSaturation(this.mNativePtr, saturation);
    }

    public synchronized int getSaturation() {
        return nativeGetSaturation(this.mNativePtr);
    }

    public synchronized void resetSaturation() {
        nativeSetSaturation(this.mNativePtr, this.mSaturationDef);
    }

    public synchronized void setSaturationPercent(int percent) {
        float range = Math.abs(this.mSaturationMax - this.mSaturationMin);
        if (range > 0.0f) {
            nativeSetSaturation(this.mNativePtr, ((int) ((percent / 100.0f) * range)) + this.mSaturationMin);
        }
    }

    public synchronized int getSaturationPercent() {
        int result;
        result = 0;
        updateSaturationLimit();
        float range = Math.abs(this.mSaturationMax - this.mSaturationMin);
        if (range > 0.0f) {
            result = (int) (((nativeGetSaturation(this.mNativePtr) - this.mSaturationMin) * 100.0f) / range);
        }
        return result;
    }

    public synchronized int[] updateSharpnessLimit() {
        int[] ints;
        ints = nativeObtainSharpnessLimit(this.mNativePtr);
        if (ints != null) {
            this.mSharpnessMin = ints[0];
            this.mSharpnessMax = ints[1];
            this.mSharpnessDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isSharpnessEnable() {
        return checkSupportFlag(-2147483632L);
    }

    public synchronized void setSharpness(int sharpness) {
        nativeSetSharpness(this.mNativePtr, sharpness);
    }

    public synchronized int getSharpness() {
        return nativeGetSharpness(this.mNativePtr);
    }

    public synchronized void resetSharpness() {
        nativeSetSharpness(this.mNativePtr, this.mSharpnessDef);
    }

    public synchronized void setSharpnessPercent(int percent) {
        float range = Math.abs(this.mSharpnessMax - this.mSharpnessMin);
        if (range > 0.0f) {
            nativeSetSharpness(this.mNativePtr, ((int) ((percent / 100.0f) * range)) + this.mSharpnessMin);
        }
    }

    public synchronized int getSharpnessPercent() {
        int result;
        result = 0;
        updateSharpnessLimit();
        float range = Math.abs(this.mSharpnessMax - this.mSharpnessMin);
        if (range > 0.0f) {
            result = (int) (((nativeGetSharpness(this.mNativePtr) - this.mSharpnessMin) * 100.0f) / range);
        }
        return result;
    }

    public synchronized int[] updateGammaLimit() {
        int[] ints;
        ints = nativeObtainGammaLimit(this.mNativePtr);
        if (ints != null) {
            this.mGammaMin = ints[0];
            this.mGammaMax = ints[1];
            this.mGammaDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isGammaEnable() {
        return checkSupportFlag(-2147483616L);
    }

    public synchronized void setGamma(int gamma) {
        nativeSetGamma(this.mNativePtr, gamma);
    }

    public synchronized int getGamma() {
        return nativeGetGamma(this.mNativePtr);
    }

    public synchronized void resetGamma() {
        nativeSetGamma(this.mNativePtr, this.mGammaDef);
    }

    public synchronized void setGammaPercent(int percent) {
        float range = Math.abs(this.mGammaMax - this.mGammaMin);
        if (range > 0.0f) {
            nativeSetGamma(this.mNativePtr, ((int) ((percent / 100.0f) * range)) + this.mGammaMin);
        }
    }

    public synchronized int getGammaPercent() {
        int result;
        result = 0;
        updateGammaLimit();
        float range = Math.abs(this.mGammaMax - this.mGammaMin);
        if (range > 0.0f) {
            result = (int) (((nativeGetGamma(this.mNativePtr) - this.mGammaMin) * 100.0f) / range);
        }
        return result;
    }

    public synchronized int[] updateWhiteBalanceLimit() {
        int[] ints;
        ints = nativeObtainWhiteBalanceLimit(this.mNativePtr);
        if (ints != null) {
            this.mWhiteBalanceMin = ints[0];
            this.mWhiteBalanceMax = ints[1];
            this.mWhiteBalanceDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isWhiteBalanceEnable() {
        return checkSupportFlag(-2147483584L);
    }

    public synchronized void setWhiteBalance(int whiteBalance) {
        nativeSetWhiteBalance(this.mNativePtr, whiteBalance);
    }

    public synchronized int getWhiteBalance() {
        return nativeGetWhiteBalance(this.mNativePtr);
    }

    public synchronized void resetWhiteBalance() {
        nativeSetWhiteBalance(this.mNativePtr, this.mWhiteBalanceDef);
    }

    public synchronized void setWhiteBalancePercent(int percent) {
        float range = Math.abs(this.mWhiteBalanceMax - this.mWhiteBalanceMin);
        if (range > 0.0f) {
            nativeSetWhiteBalance(this.mNativePtr, ((int) ((percent / 100.0f) * range)) + this.mWhiteBalanceMin);
        }
    }

    public synchronized int getWhiteBalancePercent() {
        int result;
        result = 0;
        updateWhiteBalanceLimit();
        float range = Math.abs(this.mWhiteBalanceMax - this.mWhiteBalanceMin);
        if (range > 0.0f) {
            result = (int) (((nativeGetWhiteBalance(this.mNativePtr) - this.mWhiteBalanceMin) * 100.0f) / range);
        }
        return result;
    }

    public synchronized int[] updateWhiteBalanceCompoLimit() {
        int[] ints;
        ints = nativeObtainWhiteBalanceCompoLimit(this.mNativePtr);
        if (ints != null) {
            this.mWhiteBalanceCompoMin = ints[0];
            this.mWhiteBalanceCompoMax = ints[1];
            this.mWhiteBalanceCompoDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isWhiteBalanceCompoEnable() {
        return checkSupportFlag(-2147483520L);
    }

    public synchronized void setWhiteBalanceCompo(int component) {
        nativeSetWhiteBalanceCompo(this.mNativePtr, component);
    }

    public synchronized int getWhiteBalanceCompo() {
        return nativeGetWhiteBalanceCompo(this.mNativePtr);
    }

    public synchronized void resetWhiteBalanceCompo() {
        nativeSetZoomRelative(this.mNativePtr, this.mWhiteBalanceCompoDef);
    }

    public synchronized int[] updateBacklightCompLimit() {
        int[] ints;
        ints = nativeObtainBacklightCompLimit(this.mNativePtr);
        if (ints != null) {
            this.mBacklightCompMin = ints[0];
            this.mBacklightCompMax = ints[1];
            this.mBacklightCompDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isBacklightCompEnable() {
        return checkSupportFlag(-2147483392L);
    }

    public synchronized void setBacklightComp(int backlight_compensation) {
        nativeSetBacklightComp(this.mNativePtr, backlight_compensation);
    }

    public synchronized int getBacklightComp() {
        return nativeGetBacklightComp(this.mNativePtr);
    }

    public synchronized void resetBacklightComp() {
        nativeSetBacklightComp(this.mNativePtr, this.mBacklightCompDef);
    }

    public synchronized int[] updateGainLimit() {
        int[] ints;
        ints = nativeObtainGainLimit(this.mNativePtr);
        if (ints != null) {
            this.mGainMin = ints[0];
            this.mGainMax = ints[1];
            this.mGainDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isGainEnable() {
        return checkSupportFlag(-2147483136L);
    }

    public synchronized void setGain(int gain) {
        nativeSetGain(this.mNativePtr, gain);
    }

    public synchronized int getGain() {
        return nativeGetGain(this.mNativePtr);
    }

    public synchronized void resetGain() {
        nativeSetGain(this.mNativePtr, this.mGainDef);
    }

    public synchronized void setGainPercent(int percent) {
        float range = Math.abs(this.mGainMax - this.mGainMin);
        if (range > 0.0f) {
            nativeSetGain(this.mNativePtr, ((int) ((percent / 100.0f) * range)) + this.mGainMin);
        }
    }

    public synchronized int getGainPercent() {
        int result;
        result = 0;
        updateGainLimit();
        float range = Math.abs(this.mGainMax - this.mGainMin);
        if (range > 0.0f) {
            result = (int) (((nativeGetGain(this.mNativePtr) - this.mGainMin) * 100.0f) / range);
        }
        return result;
    }

    public synchronized int[] updatePowerlineFrequencyLimit() {
        int[] ints;
        ints = nativeObtainPowerlineFrequencyLimit(this.mNativePtr);
        if (ints != null) {
            this.mPowerlineFrequencyMin = ints[0];
            this.mPowerlineFrequencyMax = ints[1];
            this.mPowerlineFrequencyDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isPowerlineFrequencyEnable() {
        return checkSupportFlag(-2147482624L);
    }

    public void setPowerlineFrequency(int frequency) {
        nativeSetPowerlineFrequency(this.mNativePtr, frequency);
    }

    public int getPowerlineFrequency() {
        return nativeGetPowerlineFrequency(this.mNativePtr);
    }

    public synchronized void resetPowerlineFrequency() {
        nativeSetPowerlineFrequency(this.mNativePtr, this.mPowerlineFrequencyDef);
    }

    public synchronized int[] updateHueAutoLimit() {
        int[] ints;
        ints = nativeObtainHueAutoLimit(this.mNativePtr);
        if (ints != null) {
            this.mHueAutoMin = ints[0];
            this.mHueAutoMax = ints[1];
            this.mHueAutoDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isHueAutoEnable() {
        return checkSupportFlag(-2147481600L);
    }

    public synchronized void setHueAuto(boolean state) {
        nativeSetHueAuto(this.mNativePtr, state);
    }

    public synchronized boolean getHueAuto() {
        return nativeGetHueAuto(this.mNativePtr) > 0;
    }

    public synchronized void resetHueAuto() {
        nativeSetHueAuto(this.mNativePtr, this.mHueAutoDef > 0);
    }

    public synchronized int[] updateWhiteBalanceAutoLimit() {
        int[] ints;
        ints = nativeObtainWhiteBalanceAutoLimit(this.mNativePtr);
        if (ints != null) {
            this.mWhiteBalanceAutoMin = ints[0];
            this.mWhiteBalanceAutoMax = ints[1];
            this.mWhiteBalanceAutoDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isWhiteBalanceAutoEnable() {
        return checkSupportFlag(-2147479552L);
    }

    public synchronized void setWhiteBalanceAuto(boolean whiteBalanceAuto) {
        nativeSetWhiteBalanceAuto(this.mNativePtr, whiteBalanceAuto);
    }

    public synchronized boolean getWhiteBalanceAuto() {
        return nativeGetWhiteBalanceAuto(this.mNativePtr) > 0;
    }

    public synchronized void resetWhiteBalanceAuto() {
        nativeSetWhiteBalanceAuto(this.mNativePtr, this.mWhiteBalanceAutoDef > 0);
    }

    public synchronized int[] updateWhiteBalanceCompoAutoLimit() {
        int[] ints;
        ints = nativeObtainWhiteBalanceCompoAutoLimit(this.mNativePtr);
        if (ints != null) {
            this.mWhiteBalanceCompoAutoMin = ints[0];
            this.mWhiteBalanceCompoAutoMax = ints[1];
            this.mWhiteBalanceCompoAutoDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isWhiteBalanceCompoAutoEnable() {
        return checkSupportFlag(-2147475456L);
    }

    public synchronized void setWhiteBalanceCompoAuto(boolean whiteBalanceCompoAuto) {
        nativeSetWhiteBalanceCompoAuto(this.mNativePtr, whiteBalanceCompoAuto);
    }

    public synchronized boolean getWhiteBalanceCompoAuto() {
        return nativeGetWhiteBalanceCompoAuto(this.mNativePtr) > 0;
    }

    public synchronized void resetWhiteBalanceCompoAuto() {
        nativeSetWhiteBalanceCompoAuto(this.mNativePtr, this.mWhiteBalanceCompoAutoDef > 0);
    }

    public synchronized int[] updateDigitalMultiplierLimit() {
        int[] ints;
        ints = nativeObtainDigitalMultiplierLimit(this.mNativePtr);
        if (ints != null) {
            this.mDigitalMultiplierMin = ints[0];
            this.mDigitalMultiplierMax = ints[1];
            this.mDigitalMultiplierDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isDigitalMultiplierEnable() {
        return checkSupportFlag(-2147467264L);
    }

    public void setDigitalMultiplier(int digitalMultiplier) {
        nativeSetDigitalMultiplier(this.mNativePtr, digitalMultiplier);
    }

    public int getDigitalMultiplier() {
        return nativeGetDigitalMultiplier(this.mNativePtr);
    }

    public synchronized void resetDigitalMultiplier() {
        nativeSetDigitalMultiplier(this.mNativePtr, this.mDigitalMultiplierDef);
    }

    public synchronized int[] updateDigitalMultiplierLimitLimit() {
        int[] ints;
        ints = nativeObtainDigitalMultiplierLimitLimit(this.mNativePtr);
        if (ints != null) {
            this.mDigitalMultiplierLimitMin = ints[0];
            this.mDigitalMultiplierLimitMax = ints[1];
            this.mDigitalMultiplierLimitDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isDigitalMultiplierLimitEnable() {
        return checkSupportFlag(-2147450880L);
    }

    public void setDigitalMultiplierLimit(int digitalMultiplierLimit) {
        nativeSetDigitalMultiplierLimit(this.mNativePtr, digitalMultiplierLimit);
    }

    public int getDigitalMultiplierLimit() {
        return nativeGetDigitalMultiplierLimit(this.mNativePtr);
    }

    public synchronized void resetDigitalMultiplierLimit() {
        nativeSetDigitalMultiplierLimit(this.mNativePtr, this.mDigitalMultiplierLimitDef);
    }

    public synchronized int[] updateAnalogVideoStandardLimit() {
        int[] ints;
        ints = nativeObtainAnalogVideoStandardLimit(this.mNativePtr);
        if (ints != null) {
            this.mAnalogVideoStandardMin = ints[0];
            this.mAnalogVideoStandardMax = ints[1];
            this.mAnalogVideoStandardDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isAnalogVideoStandardEnable() {
        return checkSupportFlag(-2147418112L);
    }

    public void setAnalogVideoStandard(int analogVideoStandard) {
        nativeSetAnalogVideoStandard(this.mNativePtr, analogVideoStandard);
    }

    public int getAnalogVideoStandard() {
        return nativeGetAnalogVideoStandard(this.mNativePtr);
    }

    public synchronized void resetAnalogVideoStandard() {
        nativeSetAnalogVideoStandard(this.mNativePtr, this.mAnalogVideoStandardDef);
    }

    public synchronized int[] updateAnalogVideoLockStateLimit() {
        int[] ints;
        ints = nativeObtainAnalogVideoLockStateLimit(this.mNativePtr);
        if (ints != null) {
            this.mAnalogVideoLockStateMin = ints[0];
            this.mAnalogVideoLockStateMax = ints[1];
            this.mAnalogVideoLockStateDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isAnalogVideoLockStateEnable() {
        return checkSupportFlag(-2147352576L);
    }

    public void setAnalogVideoLockState(int analogVideoLockState) {
        nativeSetAnalogVideoLockState(this.mNativePtr, analogVideoLockState);
    }

    public int getAnalogVideoLockState() {
        return nativeGetAnalogVideoLockState(this.mNativePtr);
    }

    public synchronized void resetAnalogVideoLockState() {
        nativeSetAnalogVideoLockState(this.mNativePtr, this.mAnalogVideoLockStateDef);
    }

    public synchronized int[] updateContrastAutoLimit() {
        int[] ints;
        ints = nativeObtainContrastAutoLimit(this.mNativePtr);
        if (ints != null) {
            this.mContrastAutoMin = ints[0];
            this.mContrastAutoMax = ints[1];
            this.mContrastAutoDef = ints[2];
        }
        return ints;
    }

    public synchronized boolean isContrastAutoEnable() {
        return checkSupportFlag(-2147221504L);
    }

    public synchronized void setContrastAuto(boolean state) {
        nativeSetContrastAuto(this.mNativePtr, state);
    }

    public synchronized boolean getContrastAuto() {
        return nativeGetContrastAuto(this.mNativePtr) > 0;
    }

    public synchronized void resetContrastAuto() {
        nativeSetContrastAuto(this.mNativePtr, this.mContrastAutoDef > 0);
    }

    public synchronized void updateCameraParams() {
        if (this.mNativePtr != 0) {
            if (this.mCameraTerminalControls == 0 || this.mProcessingUnitControls == 0) {
                if (this.mCameraTerminalControls == 0) {
                    this.mCameraTerminalControls = nativeGetCameraTerminalControls(this.mNativePtr);
                }
                if (this.mProcessingUnitControls == 0) {
                    this.mProcessingUnitControls = nativeGetProcessingUnitControls(this.mNativePtr);
                }
                if (this.mCameraTerminalControls != 0 && this.mProcessingUnitControls != 0) {
                    updateScanningModeLimit();
                    updateAutoExposureModeLimit();
                    updateAutoExposurePriorityLimit();
                    updateExposureTimeAbsoluteLimit();
                    updateExposureTimeRelativeLimit();
                    updateFocusAbsoluteLimit();
                    updateFocusRelativeLimit();
                    updateIrisAbsoluteLimit();
                    updateIrisRelativeLimit();
                    updateZoomAbsoluteLimit();
                    updateZoomRelativeLimit();
                    updatePanAbsoluteLimit();
                    updateTiltAbsoluteLimit();
                    updatePanRelativeLimit();
                    updateTiltRelativeLimit();
                    updateRollAbsoluteLimit();
                    updateRollRelativeLimit();
                    updateFocusAutoLimit();
                    updatePrivacyLimit();
                    updateBrightnessLimit();
                    updateContrastLimit();
                    updateHueLimit();
                    updateSaturationLimit();
                    updateSharpnessLimit();
                    updateGammaLimit();
                    updateWhiteBalanceLimit();
                    updateWhiteBalanceCompoLimit();
                    updateBacklightCompLimit();
                    updateGainLimit();
                    updatePowerlineFrequencyLimit();
                    updateHueAutoLimit();
                    updateWhiteBalanceAutoLimit();
                    updateWhiteBalanceCompoAutoLimit();
                    updateDigitalMultiplierLimit();
                    updateDigitalMultiplierLimitLimit();
                    updateAnalogVideoStandardLimit();
                    updateAnalogVideoLockStateLimit();
                    updateContrastAutoLimit();
                }
            }
        } else {
            this.mProcessingUnitControls = 0L;
            this.mCameraTerminalControls = 0L;
        }
    }

    public void release() {
        this.mNativePtr = 0L;
        this.mProcessingUnitControls = 0L;
        this.mCameraTerminalControls = 0L;
    }

    private void dumpCameraTerminal(long CameraTerminalControls) {
        Log.i(TAG, String.format("CameraTerminalControls=%x", Long.valueOf(CameraTerminalControls)));
        for (int i = 0; i < CAMERA_TERMINAL_DESCS.length; i++) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(CAMERA_TERMINAL_DESCS[i]);
            sb.append((((long) (1 << i)) & CameraTerminalControls) != 0 ? "=enabled" : "=disabled");
            Log.i(str, sb.toString());
        }
    }

    private void dumpProcessingUnit(long ProcessingUnitControls) {
        Log.i(TAG, String.format("ProcessingUnitControls=%x", Long.valueOf(ProcessingUnitControls)));
        for (int i = 0; i < PROCESSING_UNIT_DESCS.length; i++) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(PROCESSING_UNIT_DESCS[i]);
            sb.append((((long) (1 << i)) & ProcessingUnitControls) != 0 ? "=enabled" : "=disabled");
            Log.i(str, sb.toString());
        }
    }
}
