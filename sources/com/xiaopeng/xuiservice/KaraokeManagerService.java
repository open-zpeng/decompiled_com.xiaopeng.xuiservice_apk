package com.xiaopeng.xuiservice;

import android.app.ActivityThread;
import android.app.AppGlobals;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.mcu.CarMcuManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.media.AudioManager;
import android.media.AudioSystem;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import com.alipay.mobile.aromeservice.RequestParams;
import com.xiaopeng.app.xpPackageInfo;
import com.xiaopeng.biutil.BiLog;
import com.xiaopeng.biutil.BiLogFactory;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.speech.protocol.bean.recommend.RecommendBean;
import com.xiaopeng.speech.speechwidget.SpeechWidget;
import com.xiaopeng.util.FeatureOption;
import com.xiaopeng.xuimanager.XUIManager;
import com.xiaopeng.xuimanager.ambientlight.AmbientLightManager;
import com.xiaopeng.xuimanager.karaoke.IKaraoke;
import com.xiaopeng.xuimanager.karaoke.IKaraokeEventListener;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.base.event.EventCenter;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.debug.WatchDog;
import com.xiaopeng.xuiservice.innerutils.BiLogTransmit;
import com.xiaopeng.xuiservice.innerutils.DataLogUtils;
import com.xiaopeng.xuiservice.innerutils.SpeechUtils;
import com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.ToastUtil;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import com.xiaopeng.xuiservice.uvccamera.usb.USBMonitor;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class KaraokeManagerService extends IKaraoke.Stub implements XUIServiceBase {
    private static final String ACTION_ASR_CONFIRMED = "com.xiaopeng.intent.action.ASR_CONFIRMED";
    public static final int AUDIO_PAUSE_STOP = 0;
    private static final String AVAS_STREAM_ENABLE = "avas_speaker";
    private static final String BUTTON_ID = "B002";
    public static final boolean DBG = true;
    private static final String EXTRA_ASR_CONTENT = "com.xiaopeng.intent.extra.ASR_CONTENT";
    private static final int KARAOKE_EVENT = 1;
    private static final int KARAOKE_MIC_RESTRICTION;
    private static final boolean KARAOKE_SPEECH_RESTRICTION;
    public static final int MIC_ON_BY_DONGLE = 2;
    public static final int MIC_ON_BY_KARAOKE_APP = 1;
    public static final int MIC_ON_BY_START_KARAOKE = 0;
    public static final int MIC_POWER_OFF = 6;
    public static final int MIC_POWER_ON = 5;
    public static final int MIC_VOICE_ON = 7;
    public static final int MIC_VOLUME = 1;
    public static final int MUSIC_VOLUME = 0;
    public static final int NEW_AUDIO_START = 1;
    private static final String PROP_ATL_ENABLE = "persist.karaoke.atl.enable";
    private static final String PROP_DEBUG_MODE = "persist.sys.xiaopeng.debugon";
    private static final String PROP_IS_ATL_EXIST = "persist.sys.xiaopeng.ATLS";
    private static String StoredEffectType = null;
    public static final String TAG = "KaraokeManagerService";
    public static final int UDB_DONGLE_OFF = 4;
    public static final int UDB_DONGLE_ON = 3;
    private static final boolean hasAvas;
    private static volatile KaraokeManagerService karaokeManagerService;
    private static boolean key_on;
    private static AmbientLightManager mAmbilentManager;
    private static MediaCenterHalService mMediaCenterHalService;
    private static XUIManager mXUIManager;
    private static int mickey_use_type;
    private static KaraokeEventHandler mkHandler;
    private static final String productId;
    private static boolean savedAtlOpenStatus;
    private static final boolean supportMicKey = SystemProperties.getBoolean("persist.audio.record.mickey.support", false);
    private Stack<String> audioStack;
    private String currentVendor;
    private FileOutputStream fRec;
    private boolean hasAMP;
    IKaraokeEventListener iKaraokeListener;
    IXiaoPengMicVendor karaokeService;
    private AudioManager mAudioManager;
    MediaCenterHalService.AudioStatusListener mAudioStatusListener;
    private CarMcuManager.CarMcuEventCallback mCarMcuEventCallback;
    private CarMcuManager mCarMcuManager;
    private final Context mContext;
    private KaraokeVisualizer mKaraokeVisualizer;
    private final BroadcastReceiver mReceiver;
    private Queue<MethodContainer> methodQueue;
    private final String recFilePath;
    private boolean resourcesOccupied;
    private List<String> resumeList;
    private final Object mForceControlLock = new Object();
    private final Object mMicSDKServiceLock = new Object();
    private final Object mMicSDKWriteLock = new Object();
    private final Object mMicSDKReadLock = new Object();
    private final Map<IBinder, IKaraokeEventListener> mListenersMap = new HashMap();
    private final Map<IBinder, KaraokeDeathRecipient> mDeathRecipientMap = new HashMap();
    private final String EFFECT_FILE_NAME = "/system/etc/xuiservice/karaoke/MicEffectData.json";
    private final int DOLBY_ATOMS_OFF = 0;
    private final int DOLBY_ATOMS_ON = 1;
    private final String LOOSTONE_KARAOKE = "Loostone";
    private final String XP_KARAOKE = "xp";
    private boolean dongleStatus = false;
    private int VISUALIZER_REC_INIT = 0;
    private int VISUALIZER_REC_OFF = 1;
    private int VISUALIZER_REC_ON = 2;
    private int visualizer_rec_status = this.VISUALIZER_REC_INIT;
    private long resumeTime = 0;
    private String deviceSerialNumber = "";
    private int TYPE_LOCAL_IG_ON = 0;
    private String VALUE_LOCAL_IG_ON = "local_ig_on";
    private HashMap<String, Integer> micSoundEffectMap = new HashMap<>();

    static {
        hasAvas = FeatureOption.FO_AVAS_SUPPORT_TYPE != 0;
        productId = SystemProperties.get("ro.product.product.model", "");
        savedAtlOpenStatus = false;
        StoredEffectType = "stable_effect";
        key_on = false;
        mickey_use_type = 2;
        KARAOKE_SPEECH_RESTRICTION = SystemProperties.getBoolean("persist.karaoke.speech.restriction", true);
        KARAOKE_MIC_RESTRICTION = SystemProperties.getInt("persist.karaoke.mic.restriction", 1);
    }

    private KaraokeManagerService() {
        boolean z;
        boolean avasStreamEnable;
        this.mKaraokeVisualizer = null;
        if (SystemProperties.getInt("persist.sys.xiaopeng.AMP", 0) != 1) {
            z = false;
        } else {
            z = true;
        }
        this.hasAMP = z;
        this.methodQueue = new LinkedList();
        this.resumeList = new ArrayList();
        this.currentVendor = "Unknow";
        this.audioStack = new Stack<>();
        this.resourcesOccupied = false;
        this.mCarMcuEventCallback = new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.KaraokeManagerService.3
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557847561) {
                    KaraokeManagerService.this.handleIgStatus(((Integer) value.getValue()).intValue());
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mReceiver = new BroadcastReceiver() { // from class: com.xiaopeng.xuiservice.KaraokeManagerService.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                char c;
                String asr_content;
                String action = intent.getAction();
                LogUtil.d(KaraokeManagerService.TAG, "action: " + action);
                int hashCode = action.hashCode();
                if (hashCode == -2114103349) {
                    if (action.equals(USBMonitor.ACTION_USB_DEVICE_ATTACHED)) {
                        c = 0;
                    }
                    c = 65535;
                } else if (hashCode != -1608292967) {
                    if (hashCode == 1169669670 && action.equals(KaraokeManagerService.ACTION_ASR_CONFIRMED)) {
                        c = 2;
                    }
                    c = 65535;
                } else {
                    if (action.equals("android.hardware.usb.action.USB_DEVICE_DETACHED")) {
                        c = 1;
                    }
                    c = 65535;
                }
                if (c == 0) {
                    KaraokeManagerService karaokeManagerService2 = KaraokeManagerService.this;
                    karaokeManagerService2.currentVendor = karaokeManagerService2.checkUsbName();
                    KaraokeManagerService karaokeManagerService3 = KaraokeManagerService.this;
                    if (karaokeManagerService3.isKaraokeVendor(karaokeManagerService3.currentVendor)) {
                        Message m1 = KaraokeManagerService.mkHandler.obtainMessage(1, 3, 0, 0);
                        KaraokeManagerService.mkHandler.sendMessage(m1);
                        KaraokeManagerService.this.reExecuteMethod();
                    }
                    LogUtil.d(KaraokeManagerService.TAG, "Usb device attached, vendor: " + KaraokeManagerService.this.currentVendor);
                } else if (c != 1) {
                    if (c == 2 && KaraokeManagerService.supportMicKey && KaraokeManagerService.key_on && (asr_content = intent.getStringExtra(KaraokeManagerService.EXTRA_ASR_CONTENT)) != null) {
                        LogUtil.d(KaraokeManagerService.TAG, "ACTION_ASR_CONTENT:" + asr_content);
                        SpeechUtils.getInstance().handleASR(asr_content);
                    }
                } else {
                    KaraokeManagerService karaokeManagerService4 = KaraokeManagerService.this;
                    if (karaokeManagerService4.isKaraokeVendor(karaokeManagerService4.currentVendor)) {
                        Message m2 = KaraokeManagerService.mkHandler.obtainMessage(1, 4, 0, 0);
                        KaraokeManagerService.mkHandler.sendMessage(m2);
                    }
                    LogUtil.d(KaraokeManagerService.TAG, "Usb device detached, vendor: " + KaraokeManagerService.this.currentVendor);
                }
            }
        };
        this.iKaraokeListener = new IKaraokeEventListener.Stub() { // from class: com.xiaopeng.xuiservice.KaraokeManagerService.5
            public void MicDevChangeCallBack(int event) {
                LogUtil.i(KaraokeManagerService.TAG, "IKaraokeEventListener MicDevChangeCallBack: " + event);
                if (KaraokeManagerService.mkHandler != null) {
                    Message m = KaraokeManagerService.mkHandler.obtainMessage(1, event, 0, 0);
                    KaraokeManagerService.mkHandler.sendMessage(m);
                }
            }

            public void volumeEffectCallBack(int event, int value) {
                LogUtil.i(KaraokeManagerService.TAG, "IKaraokeEventListener MicDevChangeCallBack: " + event + ",value: " + value);
                if (KaraokeManagerService.mkHandler != null) {
                    Message m = KaraokeManagerService.mkHandler.obtainMessage(1, event, value, 0);
                    KaraokeManagerService.mkHandler.sendMessage(m);
                }
            }

            public void onError(int errorCode, int operation) {
                LogUtil.d(KaraokeManagerService.TAG, "IKaraokeEventListener onError: " + errorCode + ",operation: " + operation);
            }
        };
        this.mAudioStatusListener = new MediaCenterHalService.AudioStatusListener() { // from class: com.xiaopeng.xuiservice.KaraokeManagerService.6
            @Override // com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService.AudioStatusListener
            public void onNewAudioStart(int audioSession, int usage, String pkgName) {
                try {
                    KaraokeManagerService.this.audioStack.push(pkgName);
                    if (KaraokeManagerService.this.resumeList.size() > 0) {
                        if (usage == 14 || usage == 1) {
                            LogUtil.d(KaraokeManagerService.TAG, "onNewAudioStart audioSession " + audioSession + " usage " + usage + " pkgName " + pkgName);
                            KaraokeManagerService.this.XMS_setBypassFilter(1, KaraokeManagerService.this.XMS_isKaraokeApp(pkgName), KaraokeManagerService.this.resumeList.size() > 0, KaraokeManagerService.this.audioStack.size() == 0);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override // com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService.AudioStatusListener
            public void onAudioStop(int usage, String pkgName) {
                try {
                    KaraokeManagerService.this.audioStack.remove(pkgName);
                    if (KaraokeManagerService.this.resumeList.size() > 0) {
                        boolean z2 = true;
                        if (usage == 14 || usage == 1) {
                            LogUtil.d(KaraokeManagerService.TAG, "onAudioStop  usage " + usage + " pkgName " + pkgName);
                            KaraokeManagerService karaokeManagerService2 = KaraokeManagerService.this;
                            boolean XMS_isKaraokeApp = KaraokeManagerService.this.XMS_isKaraokeApp((String) KaraokeManagerService.this.audioStack.peek());
                            boolean z3 = KaraokeManagerService.this.resumeList.size() > 0;
                            if (KaraokeManagerService.this.audioStack.size() != 0) {
                                z2 = false;
                            }
                            karaokeManagerService2.XMS_setBypassFilter(0, XMS_isKaraokeApp, z3, z2);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        this.recFilePath = "/data/local/tmp/rec.pcm";
        this.mContext = ActivityThread.currentActivityThread().getApplication();
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        mkHandler = new KaraokeEventHandler();
        this.mKaraokeVisualizer = new KaraokeVisualizer();
        this.karaokeService = PmKaraokeService.getInstance();
        if (mXUIManager == null) {
            mXUIManager = XUIManager.createXUIManager(this.mContext, new ServiceConnection() { // from class: com.xiaopeng.xuiservice.KaraokeManagerService.1
                @Override // android.content.ServiceConnection
                public void onServiceConnected(ComponentName name, IBinder service) {
                    LogUtil.i(KaraokeManagerService.TAG, "onServiceConnected");
                    try {
                        AmbientLightManager unused = KaraokeManagerService.mAmbilentManager = (AmbientLightManager) KaraokeManagerService.mXUIManager.getXUIServiceManager("ambientlight");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override // android.content.ServiceConnection
                public void onServiceDisconnected(ComponentName name) {
                    AmbientLightManager unused = KaraokeManagerService.mAmbilentManager = null;
                }
            });
            mXUIManager.connect();
        }
        AudioManager audioManager = this.mAudioManager;
        if (audioManager != null) {
            audioManager.setKaraokeOn(false);
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.hardware.usb.action.USB_ACCESSORY_ATTACHED");
        intentFilter.addAction("android.hardware.usb.action.USB_ACCESSORY_DETACHED");
        intentFilter.addAction(USBMonitor.ACTION_USB_DEVICE_ATTACHED);
        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        this.mContext.registerReceiver(this.mReceiver, intentFilter);
        if (hasAvas) {
            if (Settings.System.getInt(this.mContext.getContentResolver(), "avas_speaker", 0) != 1) {
                avasStreamEnable = false;
            } else {
                avasStreamEnable = true;
            }
            XMS_SetOutPutPath(avasStreamEnable ? false : true, productId);
            this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("avas_speaker"), true, new ContentObserver(new Handler()) { // from class: com.xiaopeng.xuiservice.KaraokeManagerService.2
                @Override // android.database.ContentObserver
                public void onChange(boolean selfChange, Uri uri) {
                    super.onChange(selfChange, uri);
                    boolean avasEnable = Settings.System.getInt(KaraokeManagerService.this.mContext.getContentResolver(), "avas_speaker", 0) == 1;
                    LogUtil.d(KaraokeManagerService.TAG, "avasStreamEnable change to " + avasEnable);
                    KaraokeManagerService.this.XMS_SetOutPutPath(avasEnable ? false : true, KaraokeManagerService.productId);
                }
            });
        }
        parseMicEffectFromJson();
        mMediaCenterHalService = MediaCenterHalService.getInstance();
    }

    public static KaraokeManagerService getInstance() {
        if (karaokeManagerService == null) {
            synchronized (KaraokeManagerService.class) {
                if (karaokeManagerService == null) {
                    karaokeManagerService = new KaraokeManagerService();
                }
            }
        }
        return karaokeManagerService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleIgStatus(int value) {
        if (value == 1) {
            this.karaokeService.setCommonData(this.TYPE_LOCAL_IG_ON, this.VALUE_LOCAL_IG_ON);
            clearMethodData();
        } else if (value == 0) {
            this.audioStack.clear();
            this.resumeList.clear();
            XMS_releaseKaraokeResource(ActivityThread.currentActivityThread().getApplication().getPackageName());
        }
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        String info = "code=" + code + ",pid=" + Binder.getCallingPid() + "@" + SystemClock.elapsedRealtimeNanos();
        WatchDog.getInstance().binderEnter("KaraokeService", info);
        try {
            try {
                boolean ret = super.onTransact(code, data, reply, flags);
                return ret;
            } catch (RemoteException e) {
                LogUtil.w(TAG, "onTransact e=" + e + ",code=" + code);
                throw e;
            }
        } finally {
            WatchDog.getInstance().binderLeave("KaraokeService", info);
        }
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public synchronized void init() {
        CarClientManager.getInstance().addMcuManagerListener(this.mCarMcuEventCallback);
        mMediaCenterHalService.addAudioStatusListener(this.mAudioStatusListener);
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public synchronized void release() {
        for (KaraokeDeathRecipient recipient : this.mDeathRecipientMap.values()) {
            recipient.release();
        }
        this.mDeathRecipientMap.clear();
        this.mListenersMap.clear();
        mMediaCenterHalService.removeAudioStatusListener(this.mAudioStatusListener);
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void dump(PrintWriter writer) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterListenerLocked(IBinder listenerBinder) {
        Object status = this.mListenersMap.remove(listenerBinder);
        if (status != null) {
            this.mDeathRecipientMap.get(listenerBinder).release();
            this.mDeathRecipientMap.remove(listenerBinder);
        }
    }

    public void XMS_RegisterCallback(String pkgName, IKaraokeEventListener callBackFunc) {
        LogUtil.i(TAG, "XMS_RegisterCallback " + pkgName);
        if (callBackFunc == null) {
            LogUtil.e(TAG, "XMS_RegisterCallback: Listener is null.");
            throw new IllegalArgumentException("listener cannot be null.");
        }
        IBinder listenerBinder = callBackFunc.asBinder();
        if (this.mListenersMap.containsKey(listenerBinder)) {
            return;
        }
        KaraokeDeathRecipient deathRecipient = new KaraokeDeathRecipient(listenerBinder);
        try {
            listenerBinder.linkToDeath(deathRecipient, 0);
            this.mDeathRecipientMap.put(listenerBinder, deathRecipient);
            if (this.mListenersMap.isEmpty()) {
                synchronized (this.mMicSDKServiceLock) {
                    this.karaokeService.registCallback(this.iKaraokeListener);
                }
            }
            this.mListenersMap.put(listenerBinder, callBackFunc);
        } catch (RemoteException e) {
            LogUtil.e(TAG, "Failed to link death for recipient. " + e);
            throw new IllegalStateException("XUIServiceNotConnected");
        }
    }

    public void XMS_UnRegisterCallback(String pkgName, IKaraokeEventListener callBackFunc) {
        if (callBackFunc == null) {
            LogUtil.e(TAG, "unregisterListener: listener was not registered");
            return;
        }
        IBinder listenerBinder = callBackFunc.asBinder();
        if (!this.mListenersMap.containsKey(listenerBinder)) {
            LogUtil.e(TAG, "unregisterListener: Listener was not previously registered.");
        }
        unregisterListenerLocked(listenerBinder);
    }

    public void dispatchKaraokeCallback(int event, int value) {
        if (this.mListenersMap.isEmpty()) {
            LogUtil.e(TAG, "dispatchKaraokeCallback  ERROR:NO LISTENER");
            return;
        }
        try {
            for (IKaraokeEventListener listener : this.mListenersMap.values()) {
                listener.MicDevChangeCallBack(event);
                listener.volumeEffectCallBack(event, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int XMS_Create(String pkgName, int flag, String midware, IBinder cb) throws RemoteException {
        LogUtil.i(TAG, "XMS_Create() pkgName=" + pkgName + "  flag=" + flag + " midware=" + midware);
        storeMethodData("XMS_Create", new Object[]{pkgName, Integer.valueOf(flag), midware, cb}, new Class[]{String.class, Integer.TYPE, String.class, IBinder.class});
        long start_time = System.currentTimeMillis();
        synchronized (this.mMicSDKServiceLock) {
            try {
                this.karaokeService.create(pkgName, flag, midware);
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
        long end_time = System.currentTimeMillis();
        LogUtil.i(TAG, "XMS_Create package=" + pkgName + "  duration:" + (end_time - start_time));
        return 0;
    }

    public int XMS_Distroy(String pkgName) throws RemoteException {
        int ret;
        LogUtil.i(TAG, "XMS_Distroy() " + pkgName);
        clearMethodData();
        long start_time = System.currentTimeMillis();
        synchronized (this.mMicSDKServiceLock) {
            try {
                ret = this.karaokeService.destroy();
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
        long end_time = System.currentTimeMillis();
        LogUtil.i(TAG, "XMS_Distroy package=" + pkgName + "  duration:" + (end_time - start_time));
        return ret;
    }

    public int XMS_GetToken(String pkgName) throws RemoteException {
        LogUtil.i(TAG, "XMS_GetToken()");
        return this.karaokeService.getToken();
    }

    public int XMS_SetSignedToken(String pkgName, String sToken) throws RemoteException {
        LogUtil.i(TAG, "XMS_SetSignedToken() token " + sToken);
        storeMethodData("XMS_SetSignedToken", new Object[]{pkgName, sToken}, new Class[]{String.class, String.class});
        return this.karaokeService.setSignedToken(sToken);
    }

    public int XMS_GetHandShakeStatus(String pkgName) throws RemoteException {
        LogUtil.i(TAG, "XMS_GetHandShakeStatus " + pkgName);
        return this.karaokeService.getHandShakeStatus();
    }

    public int XMS_GetMicStatus(String pkgName) throws RemoteException {
        LogUtil.i(TAG, "XMS_GetMicStatus() " + pkgName);
        return this.karaokeService.getMicStatus();
    }

    public int XMS_GetMicPowerStatus(String pkgName) throws RemoteException {
        LogUtil.i(TAG, "XMS_GetMicPowerStatus() " + pkgName);
        return this.karaokeService.getMicPowerStatus();
    }

    public int XMS_SetVolume(String pkgName, int type, int vol) throws RemoteException {
        LogUtil.i(TAG, "XMS_SetVolume() pkgName=" + pkgName + "type=" + type + " vol=" + vol);
        if (type != 1 && type != 0 && AppGlobals.getPackageManager().getXpPackageInfo(pkgName) != null) {
            return 0;
        }
        storeMethodData("XMS_SetVolume", new Object[]{pkgName, Integer.valueOf(type), Integer.valueOf(vol)}, new Class[]{String.class, Integer.TYPE, Integer.TYPE});
        dispatchKaraokeCallback(type, vol);
        int ret = this.karaokeService.setVolume(type, vol);
        return ret;
    }

    public int XMS_GetVolume(String pkgName, int type) throws RemoteException {
        LogUtil.i(TAG, "XMS_GetVolume() pkgName=" + pkgName + "type=" + type);
        return this.karaokeService.getVolume(type);
    }

    public int XMS_SetEcho(String pkgName, int echo) throws RemoteException {
        LogUtil.i(TAG, "XMS_SetEcho() pkgName= echo=" + echo);
        storeMethodData("XMS_SetEcho", new Object[]{pkgName, Integer.valueOf(echo)}, new Class[]{String.class, Integer.TYPE});
        return this.karaokeService.setVolume(3, echo);
    }

    public int XMS_GetEcho(String pkgName) throws RemoteException {
        LogUtil.i(TAG, "XMS_GetEcho() pkgName=" + pkgName);
        return this.karaokeService.getVolume(3);
    }

    public int XMS_TrackGetMinBuf(String pkgName, int sampleRate, int channel) throws RemoteException {
        LogUtil.i(TAG, "XMS_TrackGetMinBuf() pkgName=" + pkgName + " sampleRate=" + sampleRate + " channel=" + channel);
        return this.karaokeService.trackGetMinBuf(sampleRate, channel);
    }

    public int XMS_TrackCreate(String pkgName, int sampleRate, int channel, int bufSize) throws RemoteException {
        int ret;
        LogUtil.i(TAG, "XMS_TrackCreate() pkgName=" + pkgName + " sampleRate=" + sampleRate + " channel=" + channel + " bufSize=" + bufSize);
        storeMethodData("XMS_TrackCreate", new Object[]{pkgName, Integer.valueOf(sampleRate), Integer.valueOf(channel), Integer.valueOf(bufSize)}, new Class[]{String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE});
        this.dongleStatus = this.karaokeService.getMicStatus() == 0;
        synchronized (this.mMicSDKServiceLock) {
            if (!this.dongleStatus) {
                this.mKaraokeVisualizer.VisualizerStart(sampleRate);
            }
            ret = this.karaokeService.trackCreate(sampleRate, channel, bufSize);
        }
        return ret;
    }

    public int XMS_TrackGetLatency(String pkgName) throws RemoteException {
        return this.karaokeService.trackGetLatency();
    }

    public int XMS_Write(String pkgName, byte[] data, int off, int size) throws RemoteException {
        int ret;
        synchronized (this.mMicSDKWriteLock) {
            ret = this.karaokeService.write(data, off, size);
            if (this.visualizer_rec_status != this.VISUALIZER_REC_ON) {
                this.mKaraokeVisualizer.ResBufferWrite(data, ret);
            }
        }
        return ret;
    }

    public int XMS_TrackGetAvail(String pkgName) throws RemoteException {
        int ret = this.karaokeService.trackGetAvail();
        return ret;
    }

    public int XMS_TrackDestroy(String pkgName) throws RemoteException {
        int ret;
        LogUtil.i(TAG, "XMS_TrackDestroy() " + pkgName);
        synchronized (this.mMicSDKServiceLock) {
            if (!this.dongleStatus) {
                this.mKaraokeVisualizer.VisualizerStop();
            }
            ret = this.karaokeService.trackDestroy();
        }
        XMS_setDolbyAtomsSwitchStatus(0);
        return ret;
    }

    public int XMS_Pause(String pkgName) throws RemoteException {
        LogUtil.i(TAG, "XMS_Pause() " + pkgName);
        int ret = 0;
        this.resumeList.remove(pkgName);
        setSpeechStatus(pkgName, true);
        if (this.resumeList.size() == 0) {
            storeMethodData("XMS_Pause", new Object[]{pkgName}, new Class[]{String.class});
            synchronized (this.mMicSDKServiceLock) {
                ret = this.karaokeService.pause();
                if (this.mAudioManager != null) {
                    this.mAudioManager.setKaraokeOn(false);
                }
            }
            XMS_setDolbyAtomsSwitchStatus(0);
            restoreAtlStatus();
            sendBIData(pkgName);
        } else {
            LogUtil.i(TAG, "XMS_Pause() failed, resumeList size " + this.resumeList.size());
        }
        return ret;
    }

    public int XMS_RecStop(String pkgName) throws RemoteException {
        int ret;
        LogUtil.i(TAG, "XMS_RecStop() " + pkgName);
        storeMethodData("XMS_RecStop", new Object[]{pkgName}, new Class[]{String.class});
        synchronized (this.mMicSDKServiceLock) {
            ret = this.karaokeService.recStop();
        }
        return ret;
    }

    public int XMS_RecStart(String pkgName) throws RemoteException {
        int ret;
        LogUtil.i(TAG, "XMS_RecStart() " + pkgName);
        storeMethodData("XMS_RecStart", new Object[]{pkgName}, new Class[]{String.class});
        synchronized (this.mMicSDKServiceLock) {
            ret = this.karaokeService.recStart();
        }
        return ret;
    }

    public int XMS_Resume(String pkgName) throws RemoteException {
        LogUtil.i(TAG, "XMS_Resume() pkgName=" + pkgName);
        int ret = 0;
        this.resumeList.add(pkgName);
        setSpeechStatus(pkgName, false);
        if (this.resumeList.size() == 1) {
            storeMethodData("XMS_Resume", new Object[]{pkgName}, new Class[]{String.class});
            synchronized (this.mMicSDKServiceLock) {
                if (this.mAudioManager != null) {
                    this.mAudioManager.setKaraokeOn(true);
                }
                ret = this.karaokeService.resume();
                EventCenter.getInstance().notifyNewMediaStream();
            }
            XMS_setDolbyAtomsSwitchStatus(1);
            saveAtlStatus();
            enableKaraokeAtl(SystemProperties.getInt(PROP_ATL_ENABLE, 0) != 0);
            this.resumeTime = System.currentTimeMillis();
        } else {
            LogUtil.i(TAG, "XMS_Resume() failed pkgName=" + pkgName + " resumeList size " + this.resumeList.size());
        }
        return ret;
    }

    public int XMS_ResumePlay(String pkgName) throws RemoteException {
        LogUtil.i(TAG, "XMS_ResumePlay() pkgName=" + pkgName);
        int ret = 0;
        this.resumeList.add(pkgName);
        setSpeechStatus(pkgName, false);
        if (this.resumeList.size() == 1) {
            storeMethodData("XMS_ResumePlay", new Object[]{pkgName}, new Class[]{String.class});
            synchronized (this.mMicSDKServiceLock) {
                ret = this.karaokeService.resumePlay();
            }
            XMS_setDolbyAtomsSwitchStatus(1);
        } else {
            LogUtil.i(TAG, "XMS_ResumePlay() failed, resumeList size " + this.resumeList.size());
        }
        return ret;
    }

    public int XMS_PausePlay(String pkgName) throws RemoteException {
        LogUtil.i(TAG, "XMS_PausePlay() pkgName=" + pkgName);
        int ret = 0;
        this.resumeList.remove(pkgName);
        setSpeechStatus(pkgName, true);
        if (this.resumeList.size() == 0) {
            storeMethodData("XMS_PausePlay", new Object[]{pkgName}, new Class[]{String.class});
            synchronized (this.mMicSDKServiceLock) {
                ret = this.karaokeService.pausePlay();
            }
            XMS_setDolbyAtomsSwitchStatus(0);
        } else {
            LogUtil.i(TAG, "XMS_PausePlay() failed, resumeList size " + this.resumeList.size());
        }
        return ret;
    }

    public int XMS_SaveRec(String pkgName, int mode, String micPath, String mixPath) throws RemoteException {
        LogUtil.i(TAG, "XMS_SaveRec() pkgName=" + pkgName + "  mode" + mode + " micPath" + micPath + " mixPath" + mixPath);
        storeMethodData("XMS_SaveRec", new Object[]{pkgName, Integer.valueOf(mode), micPath, mixPath}, new Class[]{String.class, Integer.TYPE, String.class, String.class});
        return this.karaokeService.saveRec(mode, micPath, mixPath);
    }

    public int XMS_StopSaveRec(String pkgName) throws RemoteException {
        LogUtil.i(TAG, "XMS_StopSaveRec() " + pkgName);
        storeMethodData("XMS_StopSaveRec", new Object[]{pkgName}, new Class[]{String.class});
        return this.karaokeService.stopSaveRec();
    }

    public int XMS_RecGetMinBuf(String pkgName, int sampleRate, int channel) throws RemoteException {
        LogUtil.i(TAG, "XMS_RecGetMinBuf() pkgName=" + pkgName + "  sampleRate=" + sampleRate + " channel=" + channel);
        return this.karaokeService.recGetMinBuf(sampleRate, channel);
    }

    public int XMS_RecCreate(String pkgName, int sampleRate, int channel, int bufSize) throws RemoteException {
        int ret;
        LogUtil.i(TAG, "XMS_RecCreate() pkgName=" + pkgName + "  sampleRate=" + sampleRate + " channel=" + channel + " bufSize" + bufSize);
        storeMethodData("XMS_RecCreate", new Object[]{pkgName, Integer.valueOf(sampleRate), Integer.valueOf(channel), Integer.valueOf(bufSize)}, new Class[]{String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE});
        this.dongleStatus = this.karaokeService.getMicStatus() == 0;
        synchronized (this.mMicSDKServiceLock) {
            ret = this.karaokeService.recCreate(sampleRate, channel, bufSize);
            if (this.dongleStatus) {
                this.mKaraokeVisualizer.VisualizerStart(sampleRate);
            }
        }
        return ret;
    }

    public int XMS_RecGetAvail(String pkgName) throws RemoteException {
        return this.karaokeService.recGetAvail();
    }

    public int XMS_SetRecMode(String pkgName, int mode) throws RemoteException {
        LogUtil.i(TAG, "XMS_SetRecMode() pkgName=" + pkgName + " mode=" + mode);
        storeMethodData("XMS_SetRecMode", new Object[]{pkgName, Integer.valueOf(mode)}, new Class[]{String.class, Integer.TYPE});
        return this.karaokeService.setRecMode(mode);
    }

    public int XMS_Read(String pkgName, byte[] data, int size) throws RemoteException {
        int ret;
        synchronized (this.mMicSDKReadLock) {
            ret = this.karaokeService.read(data, size);
            if (this.dongleStatus) {
                this.mKaraokeVisualizer.ResBufferWrite(data, ret);
            }
            if (this.visualizer_rec_status != this.VISUALIZER_REC_OFF) {
                this.visualizer_rec_status = this.VISUALIZER_REC_ON;
            }
        }
        return ret;
    }

    public int XMS_RecDestroy(String pkgName) throws RemoteException {
        int ret;
        LogUtil.i(TAG, "XMS_RecDestroy() " + pkgName);
        synchronized (this.mMicSDKServiceLock) {
            if (this.dongleStatus) {
                this.mKaraokeVisualizer.VisualizerStop();
            }
            this.visualizer_rec_status = this.VISUALIZER_REC_OFF;
            ret = this.karaokeService.recDestroy();
        }
        XMS_setDolbyAtomsSwitchStatus(0);
        return ret;
    }

    public int XMS_MicGetAvail(String pkgName) throws RemoteException {
        return this.karaokeService.micGetAvail();
    }

    public int XMS_MicGetMinBuf(String pkgName, int sampleRate, int channel) throws RemoteException {
        LogUtil.d(TAG, "XMS_MicGetMinBuf " + pkgName + " " + sampleRate + " " + channel);
        return this.karaokeService.micGetMinBuf(sampleRate, channel);
    }

    public int XMS_MicCreate(String pkgName, int sampleRate, int channel, int bufSize) throws RemoteException {
        LogUtil.i(TAG, "XMS_MicCreate " + pkgName + " " + sampleRate + " " + channel + " " + bufSize);
        storeMethodData("XMS_MicCreate", new Object[]{pkgName, Integer.valueOf(sampleRate), Integer.valueOf(channel), Integer.valueOf(bufSize)}, new Class[]{String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE});
        int ret = this.karaokeService.micCreate(sampleRate, channel, bufSize);
        checkDebugMode();
        return ret;
    }

    public int XMS_MicRead(String pkgName, byte[] data, int size) throws RemoteException {
        return this.karaokeService.micRead(data, size);
    }

    public int XMS_MicDestroy(String pkgName) throws RemoteException {
        LogUtil.i(TAG, "XMS_MicDestroy " + pkgName);
        this.resumeList.remove(pkgName);
        if (this.resumeList.size() == 0) {
            this.karaokeService.micDestroy();
            return 0;
        }
        LogUtil.i(TAG, "XMS_MicDestroy failed, resumeList size " + this.resumeList.size());
        return 0;
    }

    public int XMS_atlSwitch(boolean enable) {
        LogUtil.i(TAG, "XMS_atlSwitch " + enable);
        storeMethodData("XMS_atlSwitch", new Object[]{Boolean.valueOf(enable)}, new Class[]{Boolean.TYPE});
        return 0;
    }

    public int XMS_aiWakeUp() {
        LogUtil.i(TAG, "XMS_aiWakeUp");
        storeMethodData("XMS_aiWakeUp", new Object[0], new Class[0]);
        SpeechUtils.getInstance().wakeupSpeech("changba");
        return 0;
    }

    public boolean XMS_hasAtl() {
        LogUtil.i(TAG, "XMS_hasAtl");
        return hasAtl();
    }

    public String XMS_getMicName() {
        LogUtil.i(TAG, "XMS_getMicName");
        return "小鹏无线麦克风";
    }

    public void XMS_setAtlEnable(boolean enable) {
        LogUtil.i(TAG, "XMS_setAtlEnable enable:" + enable);
        String value = enable ? "1" : "0";
        enableKaraokeAtl(enable);
        SystemProperties.set(PROP_ATL_ENABLE, value);
    }

    private void saveAtlStatus() {
        LogUtil.i(TAG, "saveAtlStatus");
        if (hasAtl()) {
            try {
                savedAtlOpenStatus = mAmbilentManager.getAmbientLightOpen();
                StoredEffectType = mAmbilentManager.getAmbientLightEffectType();
            } catch (Exception e) {
            }
            LogUtil.i(TAG, "saveAtlStatus " + savedAtlOpenStatus + " " + StoredEffectType);
        }
    }

    private void restoreAtlStatus() {
        if (hasAtl()) {
            LogUtil.i(TAG, "restoreAtlStatus " + savedAtlOpenStatus + " " + StoredEffectType);
            try {
                if (savedAtlOpenStatus != mAmbilentManager.getAmbientLightOpen()) {
                    mAmbilentManager.setAmbientLightOpen(savedAtlOpenStatus);
                }
                if (!mAmbilentManager.getAmbientLightEffectType().equals(StoredEffectType)) {
                    mAmbilentManager.setAmbientLightEffectType(StoredEffectType);
                }
            } catch (Exception e) {
            }
        }
    }

    private void enableKaraokeAtl(boolean enable) {
        if (hasAtl()) {
            LogUtil.i(TAG, "enableKaraokeAtl " + enable);
            storeMethodData("enableKaraokeAtl", new Object[]{Boolean.valueOf(enable)}, new Class[]{Boolean.TYPE});
            try {
                mAmbilentManager.setAmbientLightOpen(enable);
                if (enable) {
                    mAmbilentManager.setAmbientLightEffectType("music");
                }
            } catch (Exception e) {
            }
        }
    }

    private boolean hasAtl() {
        return XUIConfig.hasFeature(XUIConfig.PROPERTY_ATLS) && (XUIConfig.getAtlType() == 0 || XUIConfig.getAtlType() == 2) && mAmbilentManager != null;
    }

    public boolean XMS_isAtlEnabled() {
        LogUtil.i(TAG, "XMS_isAtlEnabled");
        return SystemProperties.getInt(PROP_ATL_ENABLE, 0) != 0;
    }

    public String XMS_getBuyMicUrl() {
        return "https://mall.xiaopeng.com/product/1352927229410332673";
    }

    public void XMS_connectMicFlow() {
        if (this.mContext != null) {
            Intent intent = new Intent("android.karaoke.recommend.CONNECT_ACTION");
            intent.addFlags(16777216);
            BroadcastManager.getInstance().sendBroadcast(intent);
        }
    }

    public void XMS_ShowToast(String text) {
        LogUtil.i(TAG, "XMS_ShowToast " + text);
        ToastUtil.showToast(this.mContext, text, 1);
    }

    public void XMS_SetOutPutPath(boolean inCar, String productId2) {
        LogUtil.i(TAG, "XMS_SetOutPutPath " + inCar + ",productId:" + productId2);
        storeMethodData("XMS_SetOutPutPath", new Object[]{Boolean.valueOf(inCar), productId2}, new Class[]{Boolean.TYPE, String.class});
        this.karaokeService.setOutPutPath(inCar, productId2);
    }

    public void XMS_SetCommonData(int type, String message) {
        LogUtil.i(TAG, "XMS_SetCommonData " + type + ",message: " + message);
        storeMethodData("XMS_SetCommonData", new Object[]{Integer.valueOf(type), message}, new Class[]{String.class, Integer.TYPE});
        this.karaokeService.setCommonData(type, message);
    }

    private void sendBIData(String pkgName) {
        BiLog biLog = BiLogFactory.create(DataLogUtils.XUI_PID, "B002");
        biLog.push(RecommendBean.SHOW_TIME_RESULT, pkgName);
        biLog.push("serial_number", this.deviceSerialNumber);
        biLog.push(RequestParams.REQUEST_KEY_START_TIME, String.valueOf(this.resumeTime));
        biLog.push("endTime", String.valueOf(System.currentTimeMillis()));
        BiLogTransmit.getInstance().submit(biLog);
    }

    public int XMS_setMusicLyric(String[] lyric) {
        return 0;
    }

    public int XMS_setMusicInfo(String[] info) {
        LogUtil.i(TAG, "XMS_setMusicInfo");
        try {
            MusicInfo musicInfo = new MusicInfo();
            JSONObject musicInfoObj = new JSONObject(info[0]);
            musicInfo.setTitle(musicInfoObj.getString(SpeechWidget.WIDGET_TITLE)).setSinger(musicInfoObj.getString("singer")).setLyricist(musicInfoObj.getString("lyricist")).setProducer(musicInfoObj.getString("producer")).setSongwriter(musicInfoObj.getString("songwriter")).setSongDuration(musicInfoObj.getString("songDuration")).setPremiumStatus(musicInfoObj.getString("premiumStatus"));
            LogUtil.i(TAG, "XMS_setMusicInfo title " + musicInfo.getTitle() + " singer " + musicInfo.getSinger() + " lyricist " + musicInfo.getLyricist() + " producer " + musicInfo.getProducer() + " songwriter " + musicInfo.getSongwriter() + " songDuration " + musicInfo.getSongDuration() + " premiumStatus " + musicInfo.getPremiumStatus());
        } catch (Exception e) {
            LogUtil.e(TAG, "XMS_setMusicInfo exception " + e);
        }
        return 0;
    }

    public int XMS_clientDied() {
        AudioManager audioManager = this.mAudioManager;
        if (audioManager != null) {
            audioManager.setKaraokeOn(false);
        }
        SpeechUtils.getInstance().enableSpeechByKaraoke();
        try {
            XMS_PausePlay("dead");
            XMS_Pause("dead");
            XMS_TrackDestroy("dead");
            XMS_RecDestroy("dead");
        } catch (Exception e) {
            LogUtil.e(TAG, "binderDied:" + e);
        }
        return 0;
    }

    private void parseMicEffectFromJson() {
        LogUtil.i(TAG, "parseMicEffectFromJson()");
        try {
            InputStream inputStream = new FileInputStream("/system/etc/xuiservice/karaoke/MicEffectData.json");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                stringBuilder.append(line);
            }
            bufferedReader.close();
            inputStream.close();
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("effect");
            this.micSoundEffectMap.clear();
            synchronized (this.micSoundEffectMap) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    if (!this.hasAMP && object.getBoolean("needAMP")) {
                        LogUtil.d(TAG, "Not support: " + object.getString("name"));
                    }
                    this.micSoundEffectMap.put(object.getString("name"), Integer.valueOf(object.getInt("value")));
                    LogUtil.d(TAG, "Mic effect: " + object.getString("name"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
    }

    public Map XMS_getMicSoundEffectMap() {
        Map<String, Integer> result = new HashMap<>(this.micSoundEffectMap);
        LogUtil.i(TAG, "XMS_getMicSoundEffectMap() size" + this.micSoundEffectMap.size());
        if (this.micSoundEffectMap.size() == 0) {
            LogUtil.w(TAG, "XMS_getMicSoundEffectMap micSoundEffectMap size is 0");
            parseMicEffectFromJson();
        }
        return result;
    }

    private void XMS_setDolbyAtomsSwitchStatus(int status) {
        LogUtil.i(TAG, "XMS_setDolbyAtomsSwitchStatus " + status);
        int cmd = status != 1 ? 0 : 1;
        try {
            String parameters = "AfMethod=triggerPlayMusic;uid=999;channelConfig=185919;cmd=" + String.valueOf(cmd);
            AudioSystem.setParameters(parameters);
        } catch (Exception e) {
            LogUtil.e(TAG, "XMS_setDolbyAtomsSwitchStatus exception " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String checkUsbName() {
        try {
            UsbManager usbManager = (UsbManager) this.mContext.getSystemService("usb");
            HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
            for (UsbDevice device : deviceList.values()) {
                String name = device.getProductName();
                LogUtil.d(TAG, "getDeviceList: " + name);
                if (name != null && name.contains("Loostone")) {
                    this.deviceSerialNumber = device.getSerialNumber();
                    this.currentVendor = "Loostone";
                    return "Loostone";
                }
                this.currentVendor = "Unknow";
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "checkUsbName: " + e);
        }
        return this.currentVendor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int aiWakeUp() {
        LogUtil.i(TAG, "aiWakeUp");
        SpeechUtils.getInstance().wakeupSpeech("changba");
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public class KaraokeDeathRecipient implements IBinder.DeathRecipient {
        private static final String TAG = "KaraokeManagerService.KaraokeDeathRecipient";
        private IBinder mListenerBinder;

        KaraokeDeathRecipient(IBinder listenerBinder) {
            this.mListenerBinder = listenerBinder;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            LogUtil.w(TAG, "binderDied " + this.mListenerBinder);
            KaraokeManagerService.this.unregisterListenerLocked(this.mListenerBinder);
            KaraokeManagerService.this.XMS_clientDied();
        }

        void release() {
            this.mListenerBinder.unlinkToDeath(this, 0);
        }
    }

    /* loaded from: classes5.dex */
    private final class KaraokeEventHandler extends XuiWorkHandler {
        public KaraokeEventHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            try {
                int event = msg.arg1;
                String callingPackage = ActivityThread.currentActivityThread().getApplication().getPackageName();
                if (msg.what == 1) {
                    if (event == 4) {
                        KaraokeManagerService.this.dongleStatus = false;
                        KaraokeManagerService.this.visualizer_rec_status = KaraokeManagerService.this.VISUALIZER_REC_OFF;
                        if (KaraokeManagerService.KARAOKE_MIC_RESTRICTION == 2) {
                            KaraokeManagerService.this.releaseKaraokeResource(callingPackage);
                        }
                    } else if (event == 7) {
                        KaraokeManagerService.this.aiWakeUp();
                    } else {
                        KaraokeManagerService.this.dongleStatus = true;
                        KaraokeManagerService.this.visualizer_rec_status = KaraokeManagerService.this.VISUALIZER_REC_INIT;
                        if (KaraokeManagerService.KARAOKE_MIC_RESTRICTION == 2) {
                            KaraokeManagerService.this.XMS_requestKaraokeResource(callingPackage, 48000, 2, KaraokeManagerService.this.XMS_MicGetMinBuf(callingPackage, 48000, 2));
                        }
                    }
                    LogUtil.i(KaraokeManagerService.TAG, "KARAOKE_EVENT  handleMessage  :" + event + " dongleStatus:" + KaraokeManagerService.this.dongleStatus);
                    KaraokeManagerService.this.dispatchKaraokeCallback(event, -1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* loaded from: classes5.dex */
    public class MusicInfo {
        String title = "";
        String singer = "";
        String producer = "";
        String lyricist = "";
        String songwriter = "";
        String songDuration = "";
        String premiumStatus = "";

        public MusicInfo() {
        }

        public String getTitle() {
            return this.title;
        }

        public MusicInfo setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getSinger() {
            return this.singer;
        }

        public MusicInfo setSinger(String singer) {
            this.singer = singer;
            return this;
        }

        public String getLyricist() {
            return this.lyricist;
        }

        public MusicInfo setLyricist(String lyricist) {
            this.lyricist = lyricist;
            return this;
        }

        public String getProducer() {
            return this.producer;
        }

        public MusicInfo setProducer(String producer) {
            this.producer = producer;
            return this;
        }

        public String getSongwriter() {
            return this.songwriter;
        }

        public MusicInfo setSongwriter(String songwriter) {
            this.songwriter = songwriter;
            return this;
        }

        public String getSongDuration() {
            return this.songDuration;
        }

        public MusicInfo setSongDuration(String songDuration) {
            this.songDuration = songDuration;
            return this;
        }

        public String getPremiumStatus() {
            return this.premiumStatus;
        }

        public MusicInfo setPremiumStatus(String premiumStatus) {
            this.premiumStatus = premiumStatus;
            return this;
        }
    }

    private void storeMethodData(String methodName, Object[] parameter, Class[] parameterType) {
        LogUtil.d(TAG, "storeMethodData method " + methodName);
        MethodContainer container = new MethodContainer();
        container.setMethodName(methodName).setParameter(parameter).setParameterType(parameterType);
        this.methodQueue.offer(container);
    }

    private void clearMethodData() {
        LogUtil.d(TAG, "clearMethodData");
        this.methodQueue.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reExecuteMethod() {
        String str;
        String str2 = TAG;
        LogUtil.d(TAG, "reExecuteMethod method size " + this.methodQueue.size());
        try {
            KaraokeManagerService instance = getInstance();
            Class c = instance.getClass();
            Queue<MethodContainer> methods = new LinkedList<>();
            for (MethodContainer methodContainer : this.methodQueue) {
                methods.offer(methodContainer);
            }
            Iterator<MethodContainer> iter = methods.iterator();
            while (iter.hasNext()) {
                MethodContainer container = iter.next();
                String methodName = container.getMethodName();
                LogUtil.d(str2, "reExecuteMethod methodName " + methodName);
                Object[] param = container.getParameter();
                Class[] paramType = container.getParameterType();
                iter.remove();
                int length = param.length;
                if (length == 0) {
                    str = str2;
                    Method method = c.getDeclaredMethod(methodName, new Class[0]);
                    method.setAccessible(true);
                    method.invoke(instance, new Object[0]);
                } else if (length == 1) {
                    str = str2;
                    Method method2 = c.getDeclaredMethod(methodName, paramType[0]);
                    method2.setAccessible(true);
                    method2.invoke(instance, getParameter(paramType[0], param[0]));
                } else if (length == 2) {
                    str = str2;
                    Method method3 = c.getDeclaredMethod(methodName, paramType[0], paramType[1]);
                    method3.setAccessible(true);
                    method3.invoke(instance, getParameter(paramType[0], param[0]), getParameter(paramType[1], param[1]));
                } else if (length == 3) {
                    str = str2;
                    Method method4 = c.getDeclaredMethod(methodName, paramType[0], paramType[1], paramType[2]);
                    method4.setAccessible(true);
                    method4.invoke(instance, getParameter(paramType[0], param[0]), getParameter(paramType[1], param[1]), getParameter(paramType[2], param[2]));
                } else if (length != 4) {
                    str = str2;
                } else {
                    Method method5 = c.getDeclaredMethod(methodName, paramType[0], paramType[1], paramType[2], paramType[3]);
                    method5.setAccessible(true);
                    str = str2;
                    method5.invoke(instance, getParameter(paramType[0], param[0]), getParameter(paramType[1], param[1]), getParameter(paramType[2], param[2]), getParameter(paramType[3], param[3]));
                }
                str2 = str;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object getParameter(Class c, Object para) {
        if (c == Integer.TYPE) {
            return Integer.valueOf(para.toString());
        }
        if (c == Boolean.TYPE) {
            return Boolean.valueOf(para.toString());
        }
        if (c == Byte[].class) {
            return objectToByteArray(para);
        }
        if (c == String.class) {
            return para.toString();
        }
        return para;
    }

    public byte[] objectToByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
            return bytes;
        } catch (IOException ex) {
            ex.printStackTrace();
            return bytes;
        }
    }

    public Object byteArrayToObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
            return obj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return obj;
        } catch (ClassNotFoundException ex2) {
            ex2.printStackTrace();
            return obj;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class MethodContainer {
        String methodName;
        Object[] parameter;
        Class[] parameterType;

        public MethodContainer() {
        }

        public String getMethodName() {
            return this.methodName;
        }

        public MethodContainer setMethodName(String methodName) {
            this.methodName = methodName;
            return this;
        }

        public Object[] getParameter() {
            return this.parameter;
        }

        public MethodContainer setParameter(Object[] parameter) {
            this.parameter = parameter;
            return this;
        }

        public Class[] getParameterType() {
            return this.parameterType;
        }

        public MethodContainer setParameterType(Class[] parameterType) {
            this.parameterType = parameterType;
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int XMS_setBypassFilter(int audioSatus, boolean isKaraokeApp, boolean isResumed, boolean isAudioStackEmpty) {
        LogUtils.d(TAG, "XMS_setBypassFilter " + audioSatus + " " + isKaraokeApp + " " + isResumed + " " + isAudioStackEmpty);
        return this.karaokeService.setBypassFilter(audioSatus, isKaraokeApp, isResumed, isAudioStackEmpty);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean XMS_isKaraokeApp(String pkgName) {
        try {
            xpPackageInfo karaokePackageInfo = AppGlobals.getPackageManager().getXpPackageInfo(pkgName);
            if (karaokePackageInfo != null) {
                return karaokePackageInfo.appType == 7;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int XMS_requestKaraokeResource(String pkgName, int sampleRate, int channel, int bufferSize) {
        LogUtil.i(TAG, "XMS_requestKaraokeResource pkgName " + pkgName + " mic restriction " + KARAOKE_MIC_RESTRICTION);
        try {
            if (KARAOKE_MIC_RESTRICTION != 0 && !this.resourcesOccupied) {
                XMS_Create(pkgName, 0, "", null);
                XMS_MicCreate(pkgName, sampleRate, channel, bufferSize);
                XMS_RecCreate(pkgName, sampleRate, channel, bufferSize);
                XMS_Resume(pkgName);
                this.karaokeService.requestKaraokeResource(pkgName, this.audioStack.size() == 0);
                this.resourcesOccupied = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int XMS_releaseKaraokeResource(String pkgName) {
        if (KARAOKE_MIC_RESTRICTION != 1) {
            return 0;
        }
        int ret = releaseKaraokeResource(pkgName);
        return ret;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int releaseKaraokeResource(String pkgName) {
        LogUtil.i(TAG, "releaseKaraokeResource " + pkgName + " resource status " + this.resourcesOccupied + " mic restriction " + KARAOKE_MIC_RESTRICTION);
        try {
            if (!this.resourcesOccupied) {
                return 0;
            }
            this.resourcesOccupied = false;
            this.resumeList.clear();
            clearMethodData();
            XMS_Pause(pkgName);
            XMS_MicDestroy(pkgName);
            XMS_RecDestroy(pkgName);
            XMS_TrackDestroy(pkgName);
            int ret = this.karaokeService.releaseKaraokeResource(pkgName);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void setSpeechStatus(String pkgName, boolean enable) {
        LogUtil.d(TAG, "setSpeechStatus " + pkgName + " enable " + enable + " speech restriction " + KARAOKE_SPEECH_RESTRICTION);
        if (KARAOKE_SPEECH_RESTRICTION) {
            try {
                if (AppGlobals.getPackageManager().getXpPackageInfo(pkgName) != null) {
                    if (enable) {
                        SpeechUtils.getInstance().enableSpeechByKaraoke();
                    } else {
                        SpeechUtils.getInstance().disableSpeechByKaraoke();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isKaraokeVendor(String dongleName) {
        return dongleName.equals("Loostone");
    }

    private void openFileWrite() {
        try {
            this.fRec = new FileOutputStream("/data/local/tmp/rec.pcm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filewrite(byte[] data, int size) {
        FileOutputStream fileOutputStream;
        if (size > 0 && (fileOutputStream = this.fRec) != null) {
            try {
                fileOutputStream.write(data, 0, size);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkDebugMode() {
        boolean isDbgMode = SystemProperties.getBoolean(PROP_DEBUG_MODE, false);
        if (isDbgMode) {
            LogUtil.e(TAG, "Can not use Mic because of DebugMode");
            ToastUtil.showToast(this.mContext, (int) R.string.karaoke_debug_mode_on, 1);
        }
    }
}
