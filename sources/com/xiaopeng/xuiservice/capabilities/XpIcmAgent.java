package com.xiaopeng.xuiservice.capabilities;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.bcm.CarBcmManager;
import android.car.hardware.hvac.CarHvacManager;
import android.car.hardware.icm.CarIcmManager;
import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Message;
import android.provider.Settings;
import com.google.gson.Gson;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.carcontrol.HalServiceBaseCarListener;
import com.xiaopeng.xuiservice.utils.UiHandler;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.Objects;
/* loaded from: classes5.dex */
public class XpIcmAgent extends HalServiceBaseCarListener {
    private static final boolean DBG = true;
    private static final int MSG_ICMAGENT_EVENT = 0;
    private static final int MSG_ICMAGENT_KEYEVENT = 1;
    public static final String SYNCMODE_ALARMMODE = "AlarmMode";
    public static final String TAG = "XpIcmAgent";
    private static String time12_24;
    private AudioManager mAudioManager;
    private CarBcmManager mBcmManager;
    private final XuiWorkHandler mHandler;
    private CarHvacManager mHvacManager;
    private CarIcmManager mIcmManager;
    private static int mIcmReady = 0;
    private static int mCduReady = 0;
    private static volatile XpIcmAgent mAgent = null;

    public XpIcmAgent(Context context) {
        super(context);
        LogUtil.d(TAG, TAG);
        this.mHandler = new EventCallbackHandler(this);
        time12_24 = Settings.System.getString(this.mContext.getContentResolver(), "time_12_24");
        init();
    }

    public static XpIcmAgent getInstance(Context context) {
        if (mAgent == null) {
            synchronized (XpIcmAgent.class) {
                if (mAgent == null) {
                    mAgent = new XpIcmAgent(context);
                }
            }
        }
        return mAgent;
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void initCarListener() {
        LogUtil.i(TAG, "initCarListener()");
        if (this.mContext != null) {
            this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        }
        Uri uri = Settings.System.getUriFor("time_12_24");
        this.mContext.getContentResolver().registerContentObserver(uri, true, new ContentObserver(new UiHandler()) { // from class: com.xiaopeng.xuiservice.capabilities.XpIcmAgent.1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, Uri uri2) {
                super.onChange(selfChange, uri2);
                String unused = XpIcmAgent.time12_24 = Settings.System.getString(XpIcmAgent.this.mContext.getContentResolver(), "time_12_24");
                XpIcmAgent.this.sendTimeFormatSync();
            }
        });
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        LogUtil.d(TAG, "onCarManagerInited");
        this.mIcmManager = getCarManager("xp_icm");
        this.mHvacManager = getCarManager("hvac");
        this.mBcmManager = getCarManager("xp_bcm");
        sendSysReadySyncSignal();
        sendTimeFormatSync();
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    public void onInit() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    public void onRelease() {
    }

    public void handleIGON() {
        XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.capabilities.XpIcmAgent.2
            @Override // java.lang.Runnable
            public void run() {
                XpIcmAgent.this.sendTimeFormatSync();
            }
        }, 2000L);
    }

    public void setCduReady(int ready) {
        LogUtil.i(TAG, "setCduReady " + ready);
        mCduReady = ready;
    }

    public void setMusicInfo(byte[] basic, byte[] image) {
        CarIcmManager carIcmManager = this.mIcmManager;
        if (carIcmManager == null) {
            return;
        }
        try {
            carIcmManager.setMusicInfo(basic, image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNetRadioInfo(byte[] basic, byte[] image) {
        CarIcmManager carIcmManager = this.mIcmManager;
        if (carIcmManager == null) {
            return;
        }
        try {
            carIcmManager.setNetRadioInfo(basic, image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setRadioInfo(byte[] basic, byte[] image) {
        CarIcmManager carIcmManager = this.mIcmManager;
        if (carIcmManager == null) {
            return;
        }
        try {
            carIcmManager.setRadioInfo(basic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEventMsg(CarPropertyValue value) {
        Message message = this.mHandler.obtainMessage();
        message.what = 0;
        message.obj = value;
        this.mHandler.sendMessage(message);
    }

    private void sendKeyEventMsg(CarPropertyValue value) {
        Message message = this.mHandler.obtainMessage();
        message.what = 1;
        message.obj = value;
        this.mHandler.sendMessage(message);
    }

    /* loaded from: classes5.dex */
    private static final class EventCallbackHandler extends XuiWorkHandler {
        WeakReference<XpIcmAgent> mIcmService;

        EventCallbackHandler(XpIcmAgent icmServcie) {
            this.mIcmService = new WeakReference<>(icmServcie);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            XpIcmAgent mIcm = this.mIcmService.get();
            int i = msg.what;
            if (i == 0) {
                if (mIcm != null) {
                    mIcm.handleIcmEvents((CarPropertyValue) msg.obj);
                }
            } else if (i == 1) {
                if (mIcm != null) {
                    mIcm.handleKeyEvents((CarPropertyValue) msg.obj);
                }
            } else {
                LogUtil.e(XpIcmAgent.TAG, "Event type not handled?" + msg);
            }
        }
    }

    private void parseSyncSignal(String json) {
        Gson mGson = new Gson();
        try {
            CarIcmManager.SyncSinal mSyncSinal = (CarIcmManager.SyncSinal) mGson.fromJson(json, (Class<Object>) CarIcmManager.SyncSinal.class);
            String str = mSyncSinal.SyncMode;
            char c = 65535;
            int hashCode = str.hashCode();
            if (hashCode != 1279833972) {
                if (hashCode == 2009319158 && str.equals("SysReady")) {
                    c = 0;
                }
            } else if (str.equals(SYNCMODE_ALARMMODE)) {
                c = 1;
            }
            if (c == 0) {
                mIcmReady = mSyncSinal.SyncProgress;
                sendSysReadySyncSignal();
                sendTimeFormatSync();
            } else if (c == 1) {
                if (mSyncSinal.SyncProgress == 2 && this.mAudioManager != null) {
                    LogUtil.i(TAG, "GOT Icm message: try to mute music vol");
                    this.mAudioManager.adjustStreamVolume(3, -100, 0);
                } else if (mSyncSinal.SyncProgress == 0 && this.mAudioManager != null) {
                    LogUtil.i(TAG, "GOT Icm message: unmute music vol");
                    this.mAudioManager.adjustStreamVolume(3, 100, 0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "parseSyncSignal: json error!!! " + json);
        }
    }

    private void sendSysReadySyncSignal() {
        CarIcmManager carIcmManager = this.mIcmManager;
        if (carIcmManager == null) {
            return;
        }
        Objects.requireNonNull(carIcmManager);
        CarIcmManager.SyncSinal mSync = new CarIcmManager.SyncSinal(carIcmManager);
        mSync.SyncMode = "SysReady";
        mSync.SyncProgress = 1;
        Gson mGson = new Gson();
        String syncSignal = mGson.toJson(mSync);
        try {
            this.mIcmManager.setIcmSyncSignal(syncSignal);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendTimeFormatSync() {
        LogUtil.i(TAG, "sendTimeFormatSync()");
        int timeFormat = 0;
        if ("12".equals(time12_24)) {
            timeFormat = 1;
        }
        try {
            this.mIcmManager.setIcmTimeFormat(timeFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleKeyEvents(CarPropertyValue value) {
        int windSpeed;
        float level;
        int propertyId = value.getPropertyId();
        if (propertyId == 557851163) {
            int mode = ((Integer) value.getValue()).intValue();
            CarHvacManager carHvacManager = this.mHvacManager;
            if (carHvacManager != null && mode >= 1 && mode <= 4) {
                try {
                    carHvacManager.setHvacWindBlowMode(mode);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            return;
        }
        if (propertyId == 557916703) {
            Integer[] mValue = (Integer[]) value.getValue();
            LogUtil.i(TAG, "ID_WIND_SPD_ADJ_REQUEST  [0]:" + mValue[0] + " [1]:" + mValue[1]);
            int direction = mValue[0].intValue();
            int val = mValue[1].intValue();
            CarHvacManager carHvacManager2 = this.mHvacManager;
            if (carHvacManager2 != null) {
                try {
                    int temp = carHvacManager2.getHvacWindSpeedLevel();
                    if (direction == 0) {
                        int temp2 = temp + val;
                        windSpeed = temp2 > 7 ? 7 : temp2;
                    } else {
                        windSpeed = temp > val ? temp - val : 0;
                    }
                    LogUtil.i(TAG, "try to setHvacWindSpeedLevel to :" + windSpeed);
                    if (windSpeed >= 0 && windSpeed <= 7) {
                        this.mHvacManager.setHvacWindSpeedLevel(windSpeed);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        } else if (propertyId == 557916705) {
            Integer[] mValue2 = (Integer[]) value.getValue();
            LogUtil.i(TAG, "ID_TEMP_ADJ_REQUEST  [0]:" + mValue2[0] + " [1]:" + mValue2[1]);
            int direction2 = mValue2[0].intValue();
            int val2 = mValue2[1].intValue();
            CarHvacManager carHvacManager3 = this.mHvacManager;
            if (carHvacManager3 != null) {
                try {
                    float temperature = carHvacManager3.getHvacTempDriverValue();
                    if (direction2 == 0) {
                        float temperature2 = temperature + val2;
                        level = temperature2 > 32.0f ? 32.0f : temperature2;
                    } else {
                        level = temperature - ((float) val2) > 18.0f ? temperature - val2 : 18.0f;
                    }
                    LogUtil.i(TAG, "try to setHvacWindSpeedLevel to :" + level);
                    if (level >= 18.0f && level <= 32.0f) {
                        this.mHvacManager.setHvacTempDriverValue(level);
                    }
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }
    }

    public void handleIcmEvents(CarPropertyValue value) {
        int propertyId = value.getPropertyId();
        if (propertyId == 554702353) {
            parseSyncSignal((String) value.getValue());
        } else if (propertyId == 554702360) {
            parseCarSettingAndCallBack((String) value.getValue());
        } else if (propertyId == 557848078 && ((Integer) value.getValue()).intValue() == 1) {
            sendSysReadySyncSignal();
            sendTimeFormatSync();
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private void doCarSettingCommand(CarIcmManager.CarCtl_msgData msgData) {
        try {
            String str = msgData.msgCmd;
            char c = 65535;
            switch (str.hashCode()) {
                case -1836706340:
                    if (str.equals("setHvacFanSpeedDec")) {
                        c = '\t';
                        break;
                    }
                    break;
                case -1836701256:
                    if (str.equals("setHvacFanSpeedInc")) {
                        c = '\b';
                        break;
                    }
                    break;
                case -1787892873:
                    if (str.equals("setHvacWindSpeedLevel")) {
                        c = 4;
                        break;
                    }
                    break;
                case -1699768593:
                    if (str.equals("setHvacWindBlowMode")) {
                        c = 1;
                        break;
                    }
                    break;
                case -1113808913:
                    if (str.equals("setAirDistributionMode")) {
                        c = 7;
                        break;
                    }
                    break;
                case -867595741:
                    if (str.equals("setHvacTempDriverValue")) {
                        c = 0;
                        break;
                    }
                    break;
                case 401730181:
                    if (str.equals("setBCMSeatHeatLevel")) {
                        c = 2;
                        break;
                    }
                    break;
                case 588107444:
                    if (str.equals("triggerVolumeDown")) {
                        c = '\f';
                        break;
                    }
                    break;
                case 1275071258:
                    if (str.equals("setIcmPosition")) {
                        c = 11;
                        break;
                    }
                    break;
                case 1311819574:
                    if (str.equals("setHvacPowerMode")) {
                        c = '\n';
                        break;
                    }
                    break;
                case 1388864143:
                    if (str.equals("setHvacFrontDefrostMode")) {
                        c = 6;
                        break;
                    }
                    break;
                case 1597329923:
                    if (str.equals("setBCMSeatBlowLevel")) {
                        c = 3;
                        break;
                    }
                    break;
                case 1939998052:
                    if (str.equals("setSweepWindStatus")) {
                        c = 5;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    if (this.mHvacManager != null) {
                        float value = msgData.param;
                        LogUtil.i(TAG, "doCarSettingCommand try to setHvacTempDriverValue to " + value);
                        this.mHvacManager.setHvacTempDriverValue(value);
                        break;
                    }
                    break;
                case 1:
                    if (this.mHvacManager != null) {
                        int mode = (int) msgData.param;
                        LogUtil.i(TAG, "doCarSettingCommand try to setAirDistributionMode to " + mode);
                        this.mHvacManager.setAirDistributionMode(mode);
                        break;
                    }
                    break;
                case 2:
                    if (this.mBcmManager != null) {
                        int mode2 = (int) msgData.param;
                        LogUtil.i(TAG, "doCarSettingCommand try to setBcmSeatHeatLevel to " + mode2);
                        this.mBcmManager.setBcmSeatHeatLevel(mode2);
                        break;
                    }
                    break;
                case 3:
                    if (this.mBcmManager != null) {
                        int mode3 = (int) msgData.param;
                        LogUtil.i(TAG, "doCarSettingCommand try to setBcmSeatBlowLevel to " + mode3);
                        this.mBcmManager.setBcmSeatBlowLevel(mode3);
                        break;
                    }
                    break;
                case 4:
                    if (this.mHvacManager != null) {
                        int value2 = (int) msgData.param;
                        LogUtil.i(TAG, "doCarSettingCommand try to setHvacWindSpeedLevel to " + value2);
                        this.mHvacManager.setHvacWindSpeedLevel(value2);
                        break;
                    }
                    break;
                case 5:
                    if (this.mHvacManager != null) {
                        int value3 = (int) msgData.param;
                        LogUtil.i(TAG, "doCarSettingCommand try to setSweepWindStatus to " + value3);
                        if (value3 == 1) {
                            this.mHvacManager.setSweepWindStatus(value3);
                        } else if (value3 == 0) {
                            this.mHvacManager.setEavDriverWindMode(2);
                            this.mHvacManager.setEavPsnWindMode(2);
                        }
                        break;
                    }
                    break;
                case 6:
                    if (this.mHvacManager != null) {
                        int value4 = (int) msgData.param;
                        LogUtil.i(TAG, "doCarSettingCommand try to setHvacFrontDefrostMode to " + value4);
                        this.mHvacManager.setHvacFrontDefrostMode(value4);
                        break;
                    }
                    break;
                case 7:
                    int mode4 = (int) msgData.param;
                    if (this.mHvacManager != null) {
                        LogUtil.i(TAG, "doCarSettingCommand try to setAirDistributionMode " + mode4);
                        this.mHvacManager.setAirDistributionMode(mode4);
                        break;
                    }
                    break;
                case '\b':
                    if (this.mHvacManager != null) {
                        LogUtil.i(TAG, "doCarSettingCommand try to setHvacFanSpeedInc ");
                        this.mHvacManager.setHvacFanSpeedInc();
                        break;
                    }
                    break;
                case '\t':
                    if (this.mHvacManager != null) {
                        LogUtil.i(TAG, "doCarSettingCommand try to setHvacFanSpeedDec ");
                        this.mHvacManager.setHvacFanSpeedDec();
                        break;
                    }
                    break;
                case '\n':
                    if (this.mHvacManager != null) {
                        int enable_mode = (int) msgData.param;
                        LogUtil.i(TAG, "doCarSettingCommand try to setHvacPowerMode " + enable_mode);
                        if (enable_mode == 0 || enable_mode == 1) {
                            this.mHvacManager.setHvacPowerMode(enable_mode);
                        }
                        break;
                    }
                    break;
                case 11:
                    if (this.mAudioManager != null) {
                        int position = (int) msgData.param;
                        LogUtil.i(TAG, "setIcmPosition try to setHvacPowerMode " + position);
                        this.mAudioManager.setStreamPosition(99, "", position, 0);
                        break;
                    }
                    break;
                case '\f':
                    if (this.mAudioManager != null) {
                        int down = (int) msgData.param;
                        LogUtil.i(TAG, "triggerVolumeDown : " + down);
                        this.mAudioManager.temporaryChangeVolumeDown(3, 1, 512, down != 1);
                        break;
                    }
                    break;
                default:
                    LogUtil.d(TAG, "doCarSettingCommand NO SUCH msgCmd:" + msgData.msgCmd);
                    return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseCarSettingAndCallBack(String json) {
        CarIcmManager.CarCtl_msgData mCarMsgData;
        Gson mGson = new Gson();
        try {
            CarIcmManager.CarControl_20 mCarControl = (CarIcmManager.CarControl_20) mGson.fromJson(json, (Class<Object>) CarIcmManager.CarControl_20.class);
            if (mCarControl == null || (mCarMsgData = mCarControl.msgData) == null) {
                return;
            }
            mCarMsgData.msgCmd.hashCode();
            doCarSettingCommand(mCarMsgData);
        } catch (Exception e) {
            LogUtil.e(TAG, "parseCarSettingAndCallBack: json error!!! " + json);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.HalServiceBaseCarListener
    public void dump(PrintWriter writer) {
        writer.println("*AmbientLight HAL*");
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.HalServiceBaseCarListener, com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected boolean needConnectCar() {
        return false;
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected boolean needConnectXUI() {
        return true;
    }
}
