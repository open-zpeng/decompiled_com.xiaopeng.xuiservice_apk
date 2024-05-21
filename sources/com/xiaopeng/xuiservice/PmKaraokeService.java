package com.xiaopeng.xuiservice;

import android.app.ActivityThread;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Message;
import android.os.SystemProperties;
import com.loostone.puremic.Config;
import com.loostone.puremic.RSAUtils;
import com.loostone.puremic.controller.audio.PMAudioController;
import com.loostone.puremic.controller.audio.listener.IDongleListener;
import com.xiaopeng.speech.jarvisproto.DMEnd;
import com.xiaopeng.xuimanager.karaoke.IKaraokeEventListener;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import com.xiaopeng.xuiservice.uvccamera.usb.USBMonitor;
import java.util.HashMap;
/* loaded from: classes5.dex */
public class PmKaraokeService implements IXiaoPengMicVendor {
    private static final String ACTION_ASR_CONFIRMED = "com.xiaopeng.intent.action.ASR_CONFIRMED";
    private static final String ACTION_LOOSTONE_KEY = "com.loostone.puremic.dongle_key_changed";
    public static final boolean DBG = true;
    private static final String EXTRA_LOOSTONE_VOICEKEY = "xiaopeng.keyAction.loostone.extra";
    private static final int KARAOKE_EVENT = 1;
    private static final int REC_MODE_MIC = 1;
    private static final int REC_MODE_MIX = 2;
    public static final String TAG = "PmKaraokeService";
    private static PmKaraokeEventHandler pmHandler;
    private static volatile PmKaraokeService pmKaraokeService;
    private final Context mContext;
    private PMAudioController mController;
    private IDongleListener mDonglelistener;
    private IKaraokeEventListener mMicSDKCallBack;
    private static final boolean supportMicKey = SystemProperties.getBoolean("persist.audio.record.mickey.support", false);
    private static int RecMode = 1;
    private static boolean key_on = false;
    private static int mickey_use_type = 2;
    private static int RecOpenFlag = 0;
    private static boolean created = false;
    private final String LOOSTONE_VOICEKEY_ACTIOIN = "xiaopeng.keyAction.loostone_Voice";
    private final int trackAvailSize = 19200;
    private String deviceSerialNumber = "";
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.xiaopeng.xuiservice.PmKaraokeService.1
        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            char c;
            String action = intent.getAction();
            LogUtil.d(PmKaraokeService.TAG, "action: " + action);
            switch (action.hashCode()) {
                case -2114103349:
                    if (action.equals(USBMonitor.ACTION_USB_DEVICE_ATTACHED)) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case -1608292967:
                    if (action.equals("android.hardware.usb.action.USB_DEVICE_DETACHED")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case -708975591:
                    if (action.equals(PmKaraokeService.ACTION_LOOSTONE_KEY)) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 798292259:
                    if (action.equals("android.intent.action.BOOT_COMPLETED")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            if (c != 0) {
                if (c == 1) {
                    if (PmKaraokeService.pmHandler != null && PmKaraokeService.this.checkLoostoneUsbIn()) {
                        if (!PmKaraokeService.created) {
                            try {
                                PmKaraokeService.this.create("xuiservice", 0, "");
                            } catch (Exception e) {
                                LogUtil.e(PmKaraokeService.TAG, "create error");
                            }
                        }
                        Message m = PmKaraokeService.pmHandler.obtainMessage(1, 3, 0, 0);
                        PmKaraokeService.pmHandler.sendMessage(m);
                    }
                } else if (c != 2) {
                    if (c == 3 && PmKaraokeService.pmHandler != null && !PmKaraokeService.this.checkLoostoneUsbIn()) {
                        Message m2 = PmKaraokeService.pmHandler.obtainMessage(1, 4, 0, 0);
                        PmKaraokeService.pmHandler.sendMessage(m2);
                    }
                } else if (PmKaraokeService.pmHandler != null && PmKaraokeService.this.checkLoostoneUsbIn()) {
                    if (!PmKaraokeService.created) {
                        try {
                            PmKaraokeService.this.create("xuiservice", 0, "");
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                    Message m3 = PmKaraokeService.pmHandler.obtainMessage(1, 3, 0, 0);
                    PmKaraokeService.pmHandler.sendMessage(m3);
                }
            } else if (PmKaraokeService.supportMicKey) {
                String keyType = intent.getStringExtra("keycode_type");
                int keyAction = intent.getIntExtra("keycode_action", 0);
                String recordconfig = SystemProperties.get("persist.audio.record.path", "0");
                LogUtil.d(PmKaraokeService.TAG, "ACTION_LOOSTONE_KEY  type:" + keyType + "  action:" + keyAction + " recordconfig:" + recordconfig);
                if (DMEnd.REASON_VOICE.equals(keyType)) {
                    if (PmKaraokeService.mickey_use_type == 2) {
                        boolean startMicRecord = keyAction == 1;
                        if (startMicRecord) {
                            try {
                                PmKaraokeService.this.mMicSDKCallBack.MicDevChangeCallBack(7);
                            } catch (Exception e3) {
                                LogUtil.e(PmKaraokeService.TAG, "MIC_VOICE_ON exception " + e3);
                            }
                        }
                        PmKaraokeService.this.sendLoostoneKey(startMicRecord);
                    } else if (keyAction == 0) {
                        try {
                            PmKaraokeService.this.mMicSDKCallBack.MicDevChangeCallBack(7);
                        } catch (Exception e4) {
                            LogUtil.e(PmKaraokeService.TAG, "MIC_VOICE_ON exception " + e4);
                        }
                        PmKaraokeService.this.sendLoostoneKey(true);
                    }
                } else if ("power".equals(keyType) && keyAction == 0) {
                    PmKaraokeService.this.sendLoostoneKey(false);
                }
            }
        }
    };

    private PmKaraokeService() {
        LogUtil.i(TAG, "PmKaraokeService init");
        this.mContext = ActivityThread.currentActivityThread().getApplication();
        this.mController = new PMAudioController();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(USBMonitor.ACTION_USB_DEVICE_ATTACHED);
        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        if (supportMicKey) {
            intentFilter.addAction(ACTION_LOOSTONE_KEY);
            intentFilter.addAction(ACTION_ASR_CONFIRMED);
        }
        this.mContext.registerReceiver(this.mReceiver, intentFilter);
        sendLoostoneKey(false, true);
        pmHandler = new PmKaraokeEventHandler();
    }

    public static PmKaraokeService getInstance() {
        if (pmKaraokeService == null) {
            synchronized (PmKaraokeService.class) {
                if (pmKaraokeService == null) {
                    pmKaraokeService = new PmKaraokeService();
                }
            }
        }
        return pmKaraokeService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendLoostoneKey(boolean on) {
        sendLoostoneKey(on, false);
    }

    private void sendLoostoneKey(boolean on, boolean forceAction) {
        if (supportMicKey) {
            if (key_on != on || forceAction) {
                LogUtil.d(TAG, "sendLoostoneKey " + on);
                Intent LoostoneKey = new Intent("xiaopeng.keyAction.loostone_Voice");
                LoostoneKey.putExtra(EXTRA_LOOSTONE_VOICEKEY, on ? "start" : "stop");
                BroadcastManager.getInstance().sendBroadcast(LoostoneKey);
                key_on = on;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkLoostoneUsbIn() {
        try {
            UsbManager usbManager = (UsbManager) this.mContext.getSystemService("usb");
            HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
            for (UsbDevice device : deviceList.values()) {
                String name = device.getProductName();
                LogUtil.d(TAG, "getDeviceList: " + name);
                if (name != null && name.contains("Loostone")) {
                    this.deviceSerialNumber = device.getSerialNumber();
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            LogUtil.e(TAG, "checkLoostoneUsbIn: " + e);
            return false;
        }
    }

    /* JADX WARN: Type inference failed for: r0v4, types: [com.xiaopeng.xuiservice.PmKaraokeService$3] */
    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int create(String pkgName, int flag, String midware) {
        LogUtil.i(TAG, "create() pkgName=" + pkgName + "  flag=" + flag + " midware=" + midware);
        created = true;
        this.mDonglelistener = new IDongleListener() { // from class: com.xiaopeng.xuiservice.PmKaraokeService.2
            @Override // com.loostone.puremic.controller.audio.listener.IDongleListener
            public void onDongleChanged(boolean b) {
                LogUtil.i(PmKaraokeService.TAG, "onDongleChanged:" + b);
                if (PmKaraokeService.this.mMicSDKCallBack != null) {
                    int msg = b ? 3 : 4;
                    try {
                        PmKaraokeService.this.mMicSDKCallBack.MicDevChangeCallBack(msg);
                    } catch (Exception e) {
                        LogUtil.e(PmKaraokeService.TAG, "onDongleChanged " + e);
                    }
                }
            }

            @Override // com.loostone.puremic.controller.audio.listener.IDongleListener
            public void onMicPowerStatusChanged(boolean b) {
                LogUtil.i(PmKaraokeService.TAG, "onMicPowerStatusChanged:" + b);
                if (PmKaraokeService.this.mMicSDKCallBack != null) {
                    int msg = b ? 5 : 6;
                    try {
                        PmKaraokeService.this.mMicSDKCallBack.MicDevChangeCallBack(msg);
                    } catch (Exception e) {
                        LogUtil.e(PmKaraokeService.TAG, "onDongleChanged " + e);
                    }
                }
            }
        };
        new Thread() { // from class: com.xiaopeng.xuiservice.PmKaraokeService.3
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    LogUtil.d(PmKaraokeService.TAG, "mController:" + PmKaraokeService.this.mController);
                    PmKaraokeService.this.mController.init(PmKaraokeService.this.mContext);
                    LogUtil.d(PmKaraokeService.TAG, "init finished");
                    int mUnSignedToken = PmKaraokeService.this.getToken();
                    LogUtil.d(PmKaraokeService.TAG, "after GetToken : " + mUnSignedToken);
                    int ret = PmKaraokeService.this.setSignedToken(RSAUtils.SignData(String.valueOf(mUnSignedToken), Config.PRIVATE_KEY));
                    LogUtil.d(PmKaraokeService.TAG, "setSignToken:" + ret);
                    PmKaraokeService.this.mController.setDongleListener(PmKaraokeService.this.mDonglelistener);
                } catch (Exception e) {
                    LogUtil.e(PmKaraokeService.TAG, "WMM_Create " + e);
                }
            }
        }.start();
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int destroy() {
        LogUtil.i(TAG, "destroy() ");
        created = false;
        this.mController.deinit();
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int registCallback(IKaraokeEventListener callBackFunc) {
        LogUtil.i(TAG, "registCallback()");
        this.mMicSDKCallBack = callBackFunc;
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int unRegistCallback() {
        LogUtil.i(TAG, "unRegistCallback()");
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int getToken() {
        LogUtil.i(TAG, "getToken()");
        return this.mController.getToken();
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int setSignedToken(String sToken) {
        LogUtil.i(TAG, "setSignedToken()");
        return this.mController.setSignedToken(sToken);
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int getHandShakeStatus() {
        LogUtil.d(TAG, "getHandShakeStatus ");
        return this.mController.getHandShakeStatus();
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int getMicStatus() {
        LogUtil.i(TAG, "getMicStatus() ");
        return this.mController.getMicStatus();
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int getMicPowerStatus() {
        LogUtil.i(TAG, "getMicPowerStatus() ");
        return this.mController.getMicPowerStatus();
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int setVolume(int type, int vol) {
        LogUtil.i(TAG, "setVolume() type=" + type + " vol=" + vol);
        return this.mController.setVolume(type, vol);
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int getVolume(int type) {
        LogUtil.i(TAG, "getVolume() type=" + type);
        return this.mController.getVolume(type);
    }

    public int setEcho(int echo) {
        LogUtil.i(TAG, "setEcho() echo=" + echo);
        return setVolume(3, echo);
    }

    public int getEcho() {
        LogUtil.i(TAG, "getEcho()");
        return getVolume(3);
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int trackGetMinBuf(int sampleRate, int channel) {
        LogUtil.i(TAG, "trackGetMinBuf() sampleRate=" + sampleRate + " channel=" + channel);
        return this.mController.trackGetMinBuf(sampleRate, channel);
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int trackCreate(int sampleRate, int channel, int bufSize) {
        LogUtil.i(TAG, "trackCreate() sampleRate=" + sampleRate + " channel=" + channel + " bufSize=" + bufSize);
        return this.mController.trackCreate(sampleRate, channel, bufSize);
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int trackGetLatency() {
        return this.mController.trackGetLatency();
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int write(byte[] data, int off, int size) {
        return this.mController.write(data, off, size);
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int trackGetAvail() {
        return 19200;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int trackDestroy() {
        LogUtil.i(TAG, "trackDestroy()");
        return this.mController.trackDestroy();
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int pause() {
        LogUtil.i(TAG, "pause()");
        return this.mController.pause();
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int recStop() {
        LogUtil.i(TAG, "recStop()");
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int recStart() {
        LogUtil.i(TAG, "recStart()");
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int resume() {
        LogUtil.i(TAG, "resume()");
        return this.mController.resume();
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int resumePlay() {
        LogUtil.i(TAG, "resumePlay()");
        return this.mController.resumePlay();
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int pausePlay() {
        LogUtil.i(TAG, "pausePlay()");
        return this.mController.pausePlay();
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int saveRec(int mode, String micPath, String mixPath) {
        LogUtil.i(TAG, "saveRec() mode" + mode + " micPath" + micPath + " mixPath" + mixPath);
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int stopSaveRec() {
        LogUtil.i(TAG, "stopSaveRec()");
        RecOpenFlag = 0;
        return RecOpenFlag;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int recGetMinBuf(int sampleRate, int channel) {
        LogUtil.i(TAG, "recGetMinBuf() sampleRate=" + sampleRate + " channel=" + channel);
        return this.mController.recGetMinBuf(sampleRate, channel);
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int recCreate(int sampleRate, int channel, int bufSize) {
        LogUtil.i(TAG, "recCreate() sampleRate=" + sampleRate + " channel=" + channel + " bufSize" + bufSize);
        return this.mController.recCreate(sampleRate, channel, bufSize);
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int recGetAvail() {
        return this.mController.getRecDataAvail();
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int setRecMode(int mode) {
        LogUtil.i(TAG, "setRecMode() mode=" + mode);
        RecMode = mode;
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int read(byte[] data, int size) {
        return this.mController.readRec(data);
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int recDestroy() {
        LogUtil.i(TAG, "recDestroy()");
        this.mController.recDestroy();
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int micGetAvail() {
        return this.mController.getMicDataAvail();
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int micGetMinBuf(int sampleRate, int channel) {
        LogUtil.d(TAG, "micGetMinBuf " + sampleRate + " " + channel);
        return this.mController.recGetMinBuf(sampleRate, channel);
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int micCreate(int sampleRate, int channel, int bufSize) {
        LogUtil.i(TAG, "micCreate " + sampleRate + " " + channel + " " + bufSize);
        return this.mController.micCreate(sampleRate, channel, bufSize);
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int micRead(byte[] data, int size) {
        return this.mController.readMic(data);
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int micDestroy() {
        LogUtil.i(TAG, "micDestroy");
        return this.mController.micDestroy();
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public void setOutPutPath(boolean inCar, String productId) {
        LogUtil.d(TAG, "WMM_SetOutPutPath " + inCar + " " + productId);
        this.mController.switchPcmCardDevice(inCar, productId);
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int setCommonData(int type, String message) {
        LogUtil.i(TAG, "setCommonData() type: " + type + ",message:" + message);
        int ret = this.mController.setCommonData(type, message);
        return ret;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int setBypassFilter(int audioSatus, boolean isKaraokeApp, boolean isResumed, boolean isAudioStackEmpty) {
        LogUtil.d(TAG, "setBypassFilter() ");
        if (audioSatus == 1) {
            setBypassStatus(!isKaraokeApp);
        } else if (!isResumed) {
            setBypassStatus(true);
        } else if (isResumed && (isKaraokeApp || isAudioStackEmpty)) {
            setBypassStatus(false);
        }
        return 0;
    }

    private int setBypassStatus(boolean status) {
        LogUtil.i(TAG, "setBypassStatus() " + status);
        int ret = setVolume(5, status ? 1 : 0);
        return ret;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int requestKaraokeResource(String pkgName, boolean isAudioStackEmpty) {
        LogUtil.i(TAG, "requestKaraokeResource() ");
        if (!isAudioStackEmpty) {
            setBypassStatus(true);
        }
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.IXiaoPengMicVendor
    public int releaseKaraokeResource(String pkgName) {
        LogUtil.i(TAG, "releaseKaraokeResource() ");
        setBypassStatus(true);
        return 0;
    }

    /* loaded from: classes5.dex */
    private final class PmKaraokeEventHandler extends XuiWorkHandler {
        public PmKaraokeEventHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            int event = msg.arg1;
            if (msg.what == 1) {
                if (event == 4) {
                    PmKaraokeService.this.sendLoostoneKey(false);
                }
                LogUtil.i(PmKaraokeService.TAG, "KARAOKE_EVENT handleMessage  :" + event);
            }
        }
    }
}
