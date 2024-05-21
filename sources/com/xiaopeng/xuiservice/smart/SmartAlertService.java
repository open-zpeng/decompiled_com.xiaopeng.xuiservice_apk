package com.xiaopeng.xuiservice.smart;

import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.bcm.CarBcmManager;
import android.car.hardware.mcu.CarMcuManager;
import android.car.hardware.scu.CarScuManager;
import android.car.hardware.vcu.CarVcuManager;
import android.car.hardware.vpm.CarVpmManager;
import android.car.hardware.xpu.CarXpuManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.SystemProperties;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loostone.libtuning.channel.skyworth.old.ai.config.AiCmd;
import com.xiaopeng.speech.protocol.event.OOBEEvent;
import com.xiaopeng.xui.widget.XTextView;
import com.xiaopeng.xuimanager.XUIServiceNotConnectedException;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.capabilities.XpSoundPlayService;
import com.xiaopeng.xuiservice.capabilities.XpSpeechTtsService;
import com.xiaopeng.xuiservice.carcontrol.BaseCarListener;
import com.xiaopeng.xuiservice.carcontrol.CommonMsgController;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionWindowPos;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.FileUtils;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import kotlin.UByte;
/* loaded from: classes5.dex */
public class SmartAlertService extends BaseSmartService implements BroadcastManager.BroadcastListener {
    private static final int DEFAULT = 5;
    private static final int FRONT = 7;
    private static final int IG_ON = 1;
    private static final String KEY_LDW_MODE = "lcc_ldw_type";
    private static final String KEY_LOCALE = "tts_locale";
    private static final String KEY_LOST_CFG = "followed_vehicle_lost_config";
    private static final String KEY_SIMPLE_MODE = "ngp_broadcast_type";
    private static final String KEY_XPILOT_STATUS = "curXPilotStatus";
    private static final int MESSAGE_BIND = 6;
    private static final int MESSAGE_MODE_INDEX = 0;
    private static final int MESSAGE_PLAY_OSD = 3;
    private static final int MESSAGE_PLAY_SOUND = 1;
    private static final int MESSAGE_SPEECH = 5;
    private static final int MESSAGE_STOP_OSD = 4;
    private static final int MESSAGE_STOP_SOUND = 2;
    private static final int MESSAGE_UNBIND = 7;
    private static final int REAR = 6;
    private static final String SIGNAL_PATH = "/system/etc/xuiservice/xpilot/signalmap/";
    private static final String SOUND_PATH = "/system/media/audio/xiaopeng/xpilot/wav/";
    private static final String TAG = "SmartAlertService";
    private static SignalMap mSigMapModeIndex;
    private static SignalMap mSigMapRd11;
    private static SignalMap mSigMapRd13;
    private static SignalMap mSigMapRd43;
    private CarBcmManager mCarBcmManager;
    private CarMcuManager mCarMcuManager;
    private CarVcuManager mCarVcuManager;
    private CommonMsgController.InternalCommonEventListener mCommonEventListener;
    private CommonMsgController mCommonMsgController;
    private ContentMapCollection mContentMapCollection;
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    private ArrayMap<Integer, Integer> mLastSignal;
    private CarMcuManager.CarMcuEventCallback mMcuCallback;
    private ModeIndex mModeIndex;
    private ContentObserver mOtaCfg;
    private NgpPackage mPackage;
    private CarScuManager.CarScuEventCallback mScuCallback;
    private ArrayMap<Integer, Integer> mSigMapSound;
    private ArrayMap<Integer, Integer> mSigMapSpeech;
    private Set<Integer> mSigSetOsd;
    private Set<Integer> mSigSetSpeech;
    private ContentObserver mSimpleMode;
    private XpSoundPlayService mSoundPlay;
    private XpSpeechTtsService mSpeechTts;
    private XpSpeechTtsService.SpeechTtsEventListener mSpeechTtsListener;
    private ToastOsd mToast;
    private CarVcuManager.CarVcuEventCallback mVcuCallback;
    private CarVpmManager.CarVpmEventCallback mVpmCallback;
    private CarXpuManager.CarXpuEventCallback mXpuCallback;
    private static BindXpuService mBindService = null;
    private static boolean hasBootCompleted = false;
    private static boolean hasInit = false;
    private static boolean hasIgOn = false;
    private static boolean hasSimpleMode = false;
    private static boolean hasOtaStatus = true;
    private static boolean mAccExitPassively = false;
    private static boolean mLccExitPassively = false;
    private static boolean mAccLccExitPassively = false;
    private static Random mRand = new Random();

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService, com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
    public /* bridge */ /* synthetic */ void debugDump(PrintWriter printWriter, String[] strArr) {
        super.debugDump(printWriter, strArr);
    }

    private boolean checkFileExists(String path) {
        return new File(path).exists();
    }

    private String getLocaleSpeechContent() {
        String file = "tts_string_" + Settings.System.getString(this.mContext.getContentResolver(), KEY_LOCALE) + ".json";
        if (!checkFileExists(SIGNAL_PATH + file)) {
            String locale = SystemProperties.get("ro.product.locale", "");
            StringBuilder sb = new StringBuilder();
            sb.append("tts_string_");
            sb.append(locale.contains("-") ? locale.substring(0, locale.indexOf("-")) : locale);
            sb.append(".json");
            file = sb.toString();
            if (!checkFileExists(SIGNAL_PATH + file)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("tts_string_");
                sb2.append(XUIConfig.isInternationalEnable() ? "en" : "zh");
                sb2.append(".json");
                file = sb2.toString();
            }
        }
        LogUtil.i(TAG, "getLocaleSpeechContent: " + file);
        return file;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getSimpleMode() {
        return Settings.System.getInt(this.mContext.getContentResolver(), KEY_SIMPLE_MODE, 1) == 0;
    }

    private int getLdwMode() {
        return Settings.System.getInt(this.mContext.getContentResolver(), KEY_LDW_MODE, 3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getOtaStatus() {
        return Settings.System.getInt(this.mContext.getContentResolver(), KEY_LOST_CFG, 2) == 2;
    }

    private boolean hasFullScreenAppsAvailable() {
        return Settings.System.getInt(this.mContext.getContentResolver(), KEY_XPILOT_STATUS, 0) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restrictFullScreenApps(boolean status) {
        try {
            if (hasFullScreenAppsAvailable() != status) {
                StringBuilder sb = new StringBuilder();
                sb.append("restrictFullScreenApps ");
                sb.append(status ? OOBEEvent.STRING_TRUE : OOBEEvent.STRING_FALSE);
                LogUtil.i(TAG, sb.toString());
                Settings.System.putInt(this.mContext.getContentResolver(), KEY_XPILOT_STATUS, status ? 1 : 0);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "restrictFullScreenApps failed, " + e);
        }
    }

    private void registerObserver() {
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor(KEY_SIMPLE_MODE), true, this.mSimpleMode);
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor(KEY_LOST_CFG), true, this.mOtaCfg);
    }

    private void unregisterObservier() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mSimpleMode);
        this.mContext.getContentResolver().unregisterContentObserver(this.mOtaCfg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getXpuServicePackageName() {
        return SystemProperties.get("persist.sys.xpuservice.package", "com.xiaopeng.xpuservice");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getXpuServiceClassName() {
        return SystemProperties.get("persist.sys.xpuservice.class", "com.xiaopeng.xpuservicelibrary.XpuSRDataService");
    }

    private String getAlertBaseTime() {
        return SystemProperties.get("persist.alert.basetime", "06:00");
    }

    private void registerReceiver() {
        List<String> filter = new ArrayList<>();
        filter.add("android.intent.action.BOOT_COMPLETED");
        BroadcastManager.getInstance().registerListener(this, filter);
        if (!"1".equals(SystemProperties.get(XUIConfig.PROPERTY_BOOT_COMPLETE)) || hasBootCompleted) {
            return;
        }
        this.mToast.notifySystemUiAction(false);
        hasBootCompleted = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class Content {
        public List<String> content;
        public List<String> simpleContent;
        public int value;

        private Content() {
        }

        private String getRandomContent(List<String> contents) {
            if (contents.isEmpty()) {
                return null;
            }
            return contents.get(SmartAlertService.mRand.nextInt(contents.size()));
        }

        public String getTtsContent() {
            return getRandomContent(this.content);
        }

        public String getTtsSimpleContent() {
            return getRandomContent(this.simpleContent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class ContentMap {
        public List<Content> contentMap;
        public String name;

        private ContentMap() {
        }

        public Content getContent(int value) {
            for (Content it : this.contentMap) {
                if (it.value == value) {
                    return it;
                }
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class ContentMapCollection {
        public List<ContentMap> collection;

        private ContentMapCollection() {
        }

        public ContentMapCollection readFromJson(String file) {
            BufferedReader reader = null;
            try {
                try {
                    reader = new BufferedReader(new FileReader(SmartAlertService.SIGNAL_PATH + file));
                    Gson gson = new GsonBuilder().create();
                    ContentMapCollection contentMapCollection = (ContentMapCollection) gson.fromJson((Reader) reader, (Class<Object>) ContentMapCollection.class);
                    try {
                        reader.close();
                    } catch (Exception e) {
                        LogUtil.e(SmartAlertService.TAG, "close " + file + " Exception: " + e);
                    }
                    return contentMapCollection;
                } catch (Throwable th) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e2) {
                            LogUtil.e(SmartAlertService.TAG, "close " + file + " Exception: " + e2);
                        }
                    }
                    throw th;
                }
            } catch (Exception e3) {
                LogUtil.e(SmartAlertService.TAG, "readFromJson Exception: " + e3);
                if (reader != null) {
                    try {
                        reader.close();
                        return null;
                    } catch (Exception e4) {
                        LogUtil.e(SmartAlertService.TAG, "close " + file + " Exception: " + e4);
                        return null;
                    }
                }
                return null;
            }
        }

        public Content getContent(String name, int value) {
            for (ContentMap it : this.collection) {
                if (it.name.equals(name)) {
                    return it.getContent(value);
                }
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class Dependence {
        public String condition;
        public int parameter;

        private Dependence() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class Sound {
        public static final int MAX_PRIORITY = 7;
        public int delay;
        public Dependence dependence;
        public float leftVolume;
        public int loop;
        public int position;
        public int priority;
        public float rightVolume;
        public String source;
        public int value;

        private Sound() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class Tts {
        public static final int STRATEGY_DEFAULT = 0;
        public static final int STRATEGY_DELAY = 1;
        public static final int STRATEGY_PERIODIC = 2;
        public int delay;
        public Dependence dependence;
        public boolean hasOnlyOnce;
        public boolean hasSimpleContent;
        public int priority;
        public int strategy;
        public int value;

        private Tts() {
        }

        public String getTtsContent(Content content) {
            if (content != null) {
                return content.getTtsContent();
            }
            return null;
        }

        public String getTtsSimpleContent(Content content) {
            if (content != null) {
                return content.getTtsSimpleContent();
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class Osd {
        public int type;
        public int value;

        private Osd() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class SignalMap {
        private String name;
        private List<Osd> osd;
        private List<Sound> sound;
        private List<Tts> tts;

        private SignalMap() {
        }

        public SignalMap readFromJson(String file) {
            BufferedReader reader = null;
            try {
                try {
                    reader = new BufferedReader(new FileReader(SmartAlertService.SIGNAL_PATH + file));
                    Gson gson = new GsonBuilder().create();
                    SignalMap signalMap = (SignalMap) gson.fromJson((Reader) reader, (Class<Object>) SignalMap.class);
                    try {
                        reader.close();
                    } catch (Exception e) {
                        LogUtil.e(SmartAlertService.TAG, "close " + file + " Exception: " + e);
                    }
                    return signalMap;
                } catch (Throwable th) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e2) {
                            LogUtil.e(SmartAlertService.TAG, "close " + file + " Exception: " + e2);
                        }
                    }
                    throw th;
                }
            } catch (Exception e3) {
                LogUtil.e(SmartAlertService.TAG, "readFromJson Exception: " + e3);
                if (reader != null) {
                    try {
                        reader.close();
                        return null;
                    } catch (Exception e4) {
                        LogUtil.e(SmartAlertService.TAG, "close " + file + " Exception: " + e4);
                        return null;
                    }
                }
                return null;
            }
        }

        public String getSignalName() {
            return this.name;
        }

        public List<Tts> getTtsPolicy(int value) {
            List<Tts> list = new ArrayList<>();
            for (Tts it : this.tts) {
                if (it.value == value) {
                    list.add(it);
                }
            }
            return list;
        }

        public Sound getSoundPolicy(int value) {
            for (Sound it : this.sound) {
                if (it.value == value) {
                    return it;
                }
            }
            return null;
        }

        public Osd getOsdPolicy(int value) {
            for (Osd it : this.osd) {
                if (it.value == value) {
                    return it;
                }
            }
            return null;
        }
    }

    /* loaded from: classes5.dex */
    private class NgpPackage {
        private static final int NGP_DATA_IDX_RD11 = 1;
        private static final int NGP_DATA_IDX_RD13 = 2;
        private static final int NGP_DATA_LENGTH = 10;
        private int mRdModuleCom11;
        private int mRdModuleCom13;

        private NgpPackage() {
        }

        private boolean hasNgpDataChecksum(Integer[] data) {
            int sum = 0;
            for (int i = 0; i < data.length - 1; i++) {
                sum += data[i].intValue();
            }
            return sum == data[data.length - 1].intValue();
        }

        private int byteArrayToInt(byte[] b, int pos) {
            return (b[pos] & UByte.MAX_VALUE) | ((b[pos + 1] & UByte.MAX_VALUE) << 8) | ((b[pos + 2] & UByte.MAX_VALUE) << 16) | ((b[pos + 3] & UByte.MAX_VALUE) << 24);
        }

        public boolean hasValid(byte[] data) {
            if (data != null && data.length % 4 == 0) {
                List<Integer> al = new ArrayList<>();
                for (int i = 0; i < data.length; i += 4) {
                    al.add(Integer.valueOf(byteArrayToInt(data, i)));
                }
                int i2 = al.size();
                Integer[] arr = (Integer[]) al.toArray(new Integer[i2]);
                if (arr.length == 10 && hasNgpDataChecksum(arr)) {
                    this.mRdModuleCom11 = arr[1].intValue();
                    this.mRdModuleCom13 = arr[2].intValue();
                    return true;
                }
            }
            LogUtil.e(SmartAlertService.TAG, "ngp data checksum failed!");
            return false;
        }

        public int getRd11Data() {
            return this.mRdModuleCom11;
        }

        public int getRd13Data() {
            return this.mRdModuleCom13;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class BindXpuService {
        private static final int MESSAGE_HANDSHAKE = 0;
        private static final int MESSAGE_NGP_DATA = 1;
        private ServiceConnection connection;
        private boolean hasConnected = false;
        private Intent intent = new Intent();
        private Messenger receiver;
        private int signalId;
        private HandlerThread thread;

        /* JADX INFO: Access modifiers changed from: private */
        public void request(Messenger sender, Messenger receiver) {
            Bundle bundle = new Bundle();
            bundle.putString("clientid", "xuiclient");
            Message message = Message.obtain((Handler) null, 0);
            message.setData(bundle);
            message.replyTo = receiver;
            LogUtil.i(SmartAlertService.TAG, "request xpuService...");
            try {
                sender.send(message);
            } catch (Exception e) {
                LogUtil.e(SmartAlertService.TAG, "request xpuService failed " + e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void resetData() {
            SmartAlertService.this.dispatch(SmartAlertService.mSigMapRd11, this.signalId + 11, 0);
            SmartAlertService.this.dispatch(SmartAlertService.mSigMapRd13, this.signalId + 13, 0);
            SmartAlertService.this.dispatch(SmartAlertService.mSigMapRd43, this.signalId + 43, 0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean bindService() {
            this.hasConnected = SmartAlertService.this.mContext.bindService(this.intent, this.connection, 1);
            StringBuilder sb = new StringBuilder();
            sb.append("bindService ");
            sb.append(this.hasConnected ? "succeed" : "failed");
            LogUtil.i(SmartAlertService.TAG, sb.toString());
            return this.hasConnected;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void unbindService() {
            if (this.hasConnected) {
                LogUtil.i(SmartAlertService.TAG, "unbindService");
                SmartAlertService.this.mContext.unbindService(this.connection);
                this.hasConnected = false;
            }
        }

        public BindXpuService(int signal) {
            this.signalId = signal;
            this.intent.setClassName(SmartAlertService.this.getXpuServicePackageName(), SmartAlertService.this.getXpuServiceClassName());
            this.thread = new HandlerThread("bindXpuServiceThread");
            this.thread.start();
            this.receiver = new Messenger(new Handler(this.thread.getLooper()) { // from class: com.xiaopeng.xuiservice.smart.SmartAlertService.BindXpuService.1
                @Override // android.os.Handler
                public void handleMessage(Message msg) {
                    LogUtil.d(SmartAlertService.TAG, "receive bindMessage id=" + msg.what + ", message=" + msg.getData());
                    int i = msg.what;
                    if (i == 0) {
                        try {
                            String status = msg.getData().getString("bindStatus");
                            if (!TextUtils.isEmpty(status) && status.equals("succeed")) {
                                LogUtil.i(SmartAlertService.TAG, "bindStatus ok");
                            }
                        } catch (Exception e) {
                            LogUtil.e(SmartAlertService.TAG, "bindStatus failed" + e);
                        }
                    } else if (i == 1) {
                        try {
                            int now = (int) System.currentTimeMillis();
                            int last = msg.getData().getInt("ts");
                            List<Integer> data = msg.getData().getIntegerArrayList("data");
                            if (data != null && data.size() == 3) {
                                LogUtil.d(SmartAlertService.TAG, "bindMessage rd11=" + data.get(0) + ", rd13=" + data.get(1) + ", rd43=" + data.get(2) + ", delayed=" + (now - last));
                                SmartAlertService.this.dispatch(SmartAlertService.mSigMapRd11, BindXpuService.this.signalId + 11, data.get(0).intValue());
                                SmartAlertService.this.dispatch(SmartAlertService.mSigMapRd13, BindXpuService.this.signalId + 13, data.get(1).intValue());
                                SmartAlertService.this.dispatch(SmartAlertService.mSigMapRd43, BindXpuService.this.signalId + 43, data.get(2).intValue());
                            }
                        } catch (Exception e2) {
                            LogUtil.e(SmartAlertService.TAG, "bindMessage failed" + e2);
                        }
                    }
                }
            });
            this.connection = new ServiceConnection() { // from class: com.xiaopeng.xuiservice.smart.SmartAlertService.BindXpuService.2
                @Override // android.content.ServiceConnection
                public void onServiceConnected(ComponentName name, IBinder service) {
                    LogUtil.i(SmartAlertService.TAG, name.getClassName() + " onServiceConnected");
                    BindXpuService.this.request(new Messenger(service), BindXpuService.this.receiver);
                }

                @Override // android.content.ServiceConnection
                public void onServiceDisconnected(ComponentName name) {
                    LogUtil.w(SmartAlertService.TAG, name.getClassName() + " onServiceDisconnected");
                    BindXpuService.this.resetData();
                    SmartAlertService.this.handleBindService(false, 500L);
                }

                @Override // android.content.ServiceConnection
                public void onBindingDied(ComponentName name) {
                    LogUtil.e(SmartAlertService.TAG, name.getClassName() + " onBindingDied");
                    BindXpuService.this.resetData();
                    SmartAlertService.this.handleBindService(false, 500L);
                }
            };
            SmartAlertService.this.handleBindService(true, 500L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class ModeIndex {
        public static final int ACC = 1;
        public static final int CNGP = 4;
        public static final int LCC = 2;
        public static final int NGP = 3;
        public static final int NONE = 0;
        private int mLastStatus;
        private boolean mNgpTriggered;

        private ModeIndex() {
            this.mLastStatus = 0;
            this.mNgpTriggered = false;
        }

        public int getStatus() {
            return this.mLastStatus;
        }

        public boolean hasNgpTriggered() {
            return this.mNgpTriggered;
        }

        public void clearStatus() {
            this.mLastStatus = 0;
            this.mNgpTriggered = false;
            SmartAlertService.this.restrictFullScreenApps(false);
        }

        public List<Integer> getMapValue(int value) {
            List<Integer> values = new ArrayList<>();
            int status = 0;
            if (value == 4) {
                status = 1;
            } else if (value > 4 && value < 9) {
                status = 2;
            } else if (value == 9) {
                status = 3;
            } else if (value == 10) {
                status = 4;
            }
            if (status == this.mLastStatus) {
                return values;
            }
            LogUtil.i(SmartAlertService.TAG, "mode_index last status=" + this.mLastStatus + ", status=" + status + " (none=0, acc=1, lcc=2, ngp=3, cngp=4)");
            if (this.mLastStatus == 0 && (status == 1 || status == 2)) {
                values.add(1);
            }
            if (this.mLastStatus == 1 && status == 0) {
                values.add(SmartAlertService.mAccExitPassively ? 4 : 3);
            }
            if (this.mLastStatus == 2 && status == 1) {
                values.add(Integer.valueOf(SmartAlertService.mLccExitPassively ? 6 : 5));
            }
            if (this.mLastStatus == 2 && status == 0) {
                values.add(Integer.valueOf(SmartAlertService.mAccLccExitPassively ? 8 : 7));
            }
            if (this.mLastStatus != 2 && status == 2) {
                values.add(9);
            }
            if (status == 3 || status == 4) {
                values.add(10);
                this.mNgpTriggered = true;
                SmartAlertService.this.restrictFullScreenApps(true);
            } else {
                int i = this.mLastStatus;
                if (i == 3 || i == 4) {
                    SmartAlertService.this.restrictFullScreenApps(false);
                }
            }
            if (values.isEmpty()) {
                values.add(0);
            }
            this.mLastStatus = status;
            return values;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class PlaySound {
        private int signal;
        private Sound sound;

        private PlaySound() {
        }

        public void setSignal(int signal) {
            this.signal = signal;
        }

        public int getSignal() {
            return this.signal;
        }

        public void setSound(Sound sound) {
            this.sound = sound;
        }

        public Sound getSound() {
            return this.sound;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class SpeechTts {
        private String content;
        private int delay;
        private boolean hasLoop;
        private boolean hasOnlyOnce;
        private int nightDelay;
        private int priority;
        private int signal;

        private SpeechTts() {
        }

        public void setSignal(int signal) {
            this.signal = signal;
        }

        public int getSignal() {
            return this.signal;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContent() {
            return this.content;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public int getPriority() {
            return this.priority;
        }

        public void setDelay(int delay) {
            this.delay = delay;
        }

        public int getDelay() {
            return this.delay;
        }

        public void setNightDelay(int nightDelay) {
            this.nightDelay = nightDelay;
        }

        public int getNightDelay() {
            return this.nightDelay;
        }

        public void setLoop(boolean hasLoop) {
            this.hasLoop = hasLoop;
        }

        public boolean getLoop() {
            return this.hasLoop;
        }

        public void setOnlyOnce(boolean hasOnlyOnce) {
            this.hasOnlyOnce = hasOnlyOnce;
        }

        public boolean getOnlyOnce() {
            return this.hasOnlyOnce;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class ToastOsd {
        private static final String ACTION_OFF = "com.xiaopeng.systemui.ACTION_STOP_NGP_WARNING";
        private static final String ACTION_ON = "com.xiaopeng.systemui.ACTION_START_NGP_WARNING";
        private static final int TAKEOVER_BRAKE = 1;
        private static final int TAKEOVER_STEEL = 0;
        private LayoutInflater inflater;
        private RelativeLayout layout;
        private WindowManager.LayoutParams params;
        private boolean status;
        private XTextView textView;
        private View view;
        private WindowManager wm;

        private ToastOsd() {
            this.status = false;
            this.params = new WindowManager.LayoutParams();
        }

        public void init() {
            LogUtil.i(SmartAlertService.TAG, "ToastOsd.init");
            Context context = SmartAlertService.this.mContext;
            Context context2 = SmartAlertService.this.mContext;
            this.wm = (WindowManager) context.getSystemService(ConditionWindowPos.TYPE);
            WindowManager.LayoutParams layoutParams = this.params;
            layoutParams.type = 2049;
            layoutParams.flags = 8;
            layoutParams.format = -2;
            layoutParams.width = SmartAlertService.this.mContext.getResources().getDimensionPixelSize(R.dimen.sr_warning_layout_width);
            this.params.height = SmartAlertService.this.mContext.getResources().getDimensionPixelSize(R.dimen.sr_warning_layout_height);
            this.params.gravity = 17;
            this.inflater = LayoutInflater.from(SmartAlertService.this.mContext);
            this.view = this.inflater.inflate(R.layout.sr_warning_bg, (ViewGroup) null);
            this.layout = (RelativeLayout) this.view.findViewById(R.id.background);
            this.textView = (XTextView) this.view.findViewById(R.id.text);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void notifySystemUiAction(boolean onoff) {
            LogUtil.d(SmartAlertService.TAG, "notifySystemUiAction action=" + onoff);
            String action = onoff ? ACTION_ON : ACTION_OFF;
            Intent res = new Intent();
            res.setAction(action);
            BroadcastManager.getInstance().sendBroadcast(res, true);
        }

        public synchronized void onVisibleView(int type) {
            try {
                if (type == 0) {
                    this.layout.setBackgroundResource(R.drawable.img_sr_warning_steeling);
                    this.textView.setText(R.string.sr_warning_steeling);
                } else if (type == 1) {
                    this.layout.setBackgroundResource(R.drawable.img_sr_warning_brake);
                    this.textView.setText(R.string.sr_warning_brake);
                }
                if (!this.status) {
                    LogUtil.i(SmartAlertService.TAG, "osd on, type=" + type);
                    notifySystemUiAction(true);
                    this.wm.addView(this.view, this.params);
                    this.status = true;
                } else {
                    this.wm.updateViewLayout(this.view, this.params);
                }
            } catch (Throwable th) {
                throw th;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void offVisibleView() {
            if (this.status) {
                LogUtil.i(SmartAlertService.TAG, "osd off");
                notifySystemUiAction(false);
                this.wm.removeView(this.view);
                this.status = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String parseSpeechEvent(int event) {
        if (event != 0) {
            if (event != 1) {
                if (event != 2) {
                    if (event == 3) {
                        return AiCmd.STOP;
                    }
                    return "Unknown";
                }
                return "Error";
            }
            return "Done";
        }
        return "Start";
    }

    public int makeSignal(int signalId, int signalVal) {
        return (signalId << 16) | (65535 & signalVal);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBindService(boolean status, long delay) {
        Message message = this.mHandler.obtainMessage();
        message.what = status ? 6 : 7;
        this.mHandler.sendMessageDelayed(message, delay);
    }

    private void handlePlaySound(int signal, Sound sound) {
        LogUtil.d(TAG, "handlePlaySound after " + sound.delay + "s, signal=" + signal);
        PlaySound playSound = new PlaySound();
        playSound.setSignal(signal);
        playSound.setSound(sound);
        Message message = this.mHandler.obtainMessage();
        message.what = 1;
        message.obj = playSound;
        message.setAsynchronous(true);
        if (sound.delay > 0) {
            this.mHandler.sendMessageDelayed(message, sound.delay * 1000);
        } else {
            this.mHandler.sendMessage(message);
        }
    }

    private void handleStopSound(int signal) {
        LogUtil.d(TAG, "handleStopSound, signal=" + signal);
        Message message = this.mHandler.obtainMessage();
        message.what = 2;
        message.obj = Integer.valueOf(signal);
        message.setAsynchronous(true);
        this.mHandler.sendMessage(message);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDelayedModeIndex(int signalVal) {
        Message message = this.mHandler.obtainMessage();
        message.what = 0;
        message.obj = Integer.valueOf(signalVal);
        message.setAsynchronous(true);
        this.mHandler.sendMessageDelayed(message, 800L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAccLccExitReason(int signalId, int signalVal) {
        if (signalId == 557852332) {
            if (signalVal == 14 || signalVal == 15) {
                r3 = false;
            }
            mAccExitPassively = r3;
            mAccLccExitPassively = mAccExitPassively;
            LogUtil.d(TAG, "AccExitPassively " + mAccExitPassively);
            LogUtil.d(TAG, "AccLccExitPassively " + mAccLccExitPassively);
        } else if (signalId == 557852331) {
            mLccExitPassively = signalVal != 20;
            LogUtil.d(TAG, "LccExitPassively " + mLccExitPassively);
        } else if (signalId == 557852199 && (signalVal == 2 || signalVal == 8)) {
            mAccExitPassively = signalVal != 2;
            LogUtil.d(TAG, "AccExitPassively " + mAccExitPassively);
        } else if (signalId == 557852201) {
            if (signalVal == 6 || signalVal == 10 || signalVal == 14) {
                mLccExitPassively = signalVal != 6;
                mAccLccExitPassively = mLccExitPassively;
                LogUtil.d(TAG, "LccExitPassively " + mLccExitPassively);
                LogUtil.d(TAG, "AccLccExitPassively " + mAccLccExitPassively);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSpeechTts(int messageId, SpeechTts speech) {
        Message message = this.mHandler.obtainMessage();
        message.what = messageId;
        message.obj = speech;
        message.setAsynchronous(true);
        String now = String.format("%tR", new Date());
        boolean hasNight = now.compareTo(getAlertBaseTime()) < 0;
        int delay = (speech.nightDelay <= 0 || !hasNight) ? speech.delay : speech.nightDelay;
        if (delay > 0) {
            if (messageId != 5) {
                LogUtil.d(TAG, "send message " + messageId + " after " + delay + "s");
            }
            this.mHandler.sendMessageDelayed(message, delay * 1000);
            return;
        }
        this.mHandler.sendMessage(message);
    }

    private void handleStartOsd(int type) {
        Message message = this.mHandler.obtainMessage();
        message.what = 3;
        message.obj = Integer.valueOf(type);
        message.setAsynchronous(true);
        this.mHandler.sendMessage(message);
    }

    private void handleStopOsd() {
        Message message = this.mHandler.obtainMessage();
        message.what = 4;
        message.setAsynchronous(true);
        this.mHandler.sendMessage(message);
    }

    private boolean hasConditionOtaStatus(Dependence dependence) {
        return (dependence == null || TextUtils.isEmpty(dependence.condition) || !dependence.condition.equals("check_ota_status")) ? false : true;
    }

    private boolean hasConditionDoorsStatus(Dependence dependence) {
        return (dependence == null || TextUtils.isEmpty(dependence.condition) || !dependence.condition.equals("doors_status")) ? false : true;
    }

    private boolean hasConditionUseSimpleTtsExceptFirstTime(Dependence dependence) {
        return (dependence == null || TextUtils.isEmpty(dependence.condition) || !dependence.condition.equals("use_simple_tts_except_first_time")) ? false : true;
    }

    private boolean hasConditionBcmWiperSpeed(Dependence dependence) {
        return (dependence == null || TextUtils.isEmpty(dependence.condition) || !dependence.condition.equals("bcm_wiper_speed_equal")) ? false : true;
    }

    private boolean hasConditionVcuRawCarSpeed(Dependence dependence) {
        return (dependence == null || TextUtils.isEmpty(dependence.condition) || !dependence.condition.equals("vcu_car_speed_greater")) ? false : true;
    }

    private boolean hasConditionNgpTriggered(Dependence dependence) {
        return (dependence == null || TextUtils.isEmpty(dependence.condition) || !dependence.condition.equals("check_ngp_triggered")) ? false : true;
    }

    private boolean hasConditionLdwType(Dependence dependence) {
        return (dependence == null || TextUtils.isEmpty(dependence.condition) || !dependence.condition.equals("ldw_switch_mask")) ? false : true;
    }

    private int getConditionNightDelay(Dependence dependence) {
        if (dependence != null && !TextUtils.isEmpty(dependence.condition) && dependence.condition.equals("night_delay")) {
            return dependence.parameter;
        }
        return 0;
    }

    private boolean checkSoundCondition(Sound sound) {
        if (sound.dependence != null && sound.dependence.condition != null) {
            LogUtil.i(TAG, "sound condition=" + sound.dependence.condition + ", parameter=" + sound.dependence.parameter);
            if (hasConditionOtaStatus(sound.dependence) && !hasOtaStatus) {
                return false;
            }
            return true;
        }
        return true;
    }

    private int getDoorsState(boolean leftSide) {
        int front = !leftSide ? 1 : 0;
        int rear = leftSide ? 2 : 3;
        try {
            int[] states = this.mCarBcmManager.getDoorsState();
            if (states[front] == states[rear]) {
                return 5;
            }
            return states[front] == 1 ? 7 : 6;
        } catch (Exception e) {
            LogUtil.e(TAG, "getDoorsState failed, " + e);
            return 5;
        }
    }

    private synchronized void dispatchSound(Sound sound, int signalId, int signalVal) {
        int signal = makeSignal(signalId, signalVal);
        if (sound != null && checkSoundCondition(sound)) {
            boolean z = true;
            if (hasConditionLdwType(sound.dependence) && ((1 << getLdwMode()) & sound.dependence.parameter) == 0) {
                LogUtil.d(TAG, "vpm_rdp_warning(" + signalVal + ") discard! ldwType=" + getLdwMode());
                return;
            }
            if (hasConditionDoorsStatus(sound.dependence)) {
                if (sound.dependence.parameter != 0) {
                    z = false;
                }
                boolean leftSide = z;
                sound.position = getDoorsState(leftSide);
                StringBuilder sb = new StringBuilder();
                sb.append("get ");
                sb.append(leftSide ? "left" : "right");
                sb.append(" doorPosition=");
                sb.append(sound.position);
                LogUtil.i(TAG, sb.toString());
            }
            this.mSigMapSound.put(Integer.valueOf(signalId), Integer.valueOf(signalVal));
            handlePlaySound(signal, sound);
        } else if (this.mSigMapSound.containsKey(Integer.valueOf(signalId))) {
            int last = makeSignal(signalId, this.mSigMapSound.get(Integer.valueOf(signalId)).intValue());
            handleStopSound(last);
            this.mSigMapSound.remove(Integer.valueOf(signalId));
        }
    }

    private synchronized void dispatchSpeech(List<Tts> ttsArray, String name, int signalId, int signalVal) {
        String content;
        int last;
        int signal = makeSignal(signalId, signalVal);
        if (this.mContentMapCollection == null) {
            return;
        }
        if (this.mSigMapSpeech.containsKey(Integer.valueOf(signalId)) && (last = this.mSigMapSpeech.get(Integer.valueOf(signalId)).intValue()) != signal) {
            LogUtil.d(TAG, "remove delay message " + last);
            this.mHandler.removeMessages(last);
            this.mSigMapSpeech.remove(Integer.valueOf(signalId));
        }
        for (Tts tts : ttsArray) {
            int i = tts.priority;
            boolean useSimpleContent = false;
            if (tts.dependence != null && tts.dependence.condition != null) {
                LogUtil.i(TAG, "tts condition=" + tts.dependence.condition + ", parameter=" + tts.dependence.parameter);
            }
            if (tts.hasOnlyOnce && this.mSigSetSpeech.contains(Integer.valueOf(signal))) {
                return;
            }
            if (hasConditionNgpTriggered(tts.dependence) && this.mModeIndex.hasNgpTriggered()) {
                return;
            }
            if (hasConditionBcmWiperSpeed(tts.dependence)) {
                try {
                    int value = tts.dependence.parameter;
                    int state = this.mCarBcmManager.getWiperSpeedSwitchState();
                    LogUtil.d(TAG, "getWiperSpeedSwitchState = " + state);
                    if (state != value) {
                        return;
                    }
                } catch (Exception e) {
                    LogUtil.e(TAG, "getWiperSpeedSwitchState failed, " + e);
                    return;
                }
            }
            if (hasConditionVcuRawCarSpeed(tts.dependence)) {
                try {
                    int limit = tts.dependence.parameter;
                    float speed = this.mCarVcuManager.getRawCarSpeed();
                    int driveMode = this.mCarVcuManager.getPureDriveModeFeedback();
                    LogUtil.d(TAG, "getRawCarSpeed=" + speed + ", limitSpeed=" + limit + ", driveMode=" + driveMode);
                    if (speed <= limit || driveMode != 0) {
                        return;
                    }
                } catch (Exception e2) {
                    LogUtil.e(TAG, "getRawCarSpeed or getPureDriveModeFeedback failed, " + e2);
                    return;
                }
            }
            if (hasConditionUseSimpleTtsExceptFirstTime(tts.dependence)) {
                if (this.mSigSetSpeech.contains(Integer.valueOf(signal))) {
                    useSimpleContent = true;
                } else {
                    this.mSigSetSpeech.add(Integer.valueOf(signal));
                }
            }
            if ((hasSimpleMode && tts.hasSimpleContent) || useSimpleContent) {
                content = tts.getTtsSimpleContent(this.mContentMapCollection.getContent(name, tts.value));
            } else {
                content = tts.getTtsContent(this.mContentMapCollection.getContent(name, tts.value));
            }
            if (!TextUtils.isEmpty(content)) {
                int messageId = tts.strategy != 0 ? signal : 5;
                if (messageId != 5) {
                    this.mSigMapSpeech.put(Integer.valueOf(signalId), Integer.valueOf(signal));
                }
                SpeechTts speech = new SpeechTts();
                speech.setSignal(signal);
                speech.setContent(content);
                speech.setPriority(tts.priority);
                speech.setDelay(tts.delay);
                speech.setNightDelay(getConditionNightDelay(tts.dependence));
                speech.setLoop(tts.strategy == 2);
                speech.setOnlyOnce(tts.hasOnlyOnce);
                handleSpeechTts(messageId, speech);
            }
        }
    }

    private synchronized void dispatchOsd(Osd osd, int signalId) {
        if (osd != null) {
            handleStartOsd(osd.type);
            this.mSigSetOsd.add(Integer.valueOf(signalId));
        } else if (this.mSigSetOsd.contains(Integer.valueOf(signalId))) {
            this.mSigSetOsd.remove(Integer.valueOf(signalId));
            if (this.mSigSetOsd.isEmpty()) {
                handleStopOsd();
            }
        }
    }

    private boolean hasDuplicated(int signalId, int signalVal) {
        return this.mLastSignal.containsKey(Integer.valueOf(signalId)) && signalVal == this.mLastSignal.get(Integer.valueOf(signalId)).intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatch(SignalMap signalMap, int signalId, int signalVal) {
        if (signalMap != null) {
            if (hasInit && hasIgOn) {
                boolean duplicated = hasDuplicated(signalId, signalVal);
                String name = signalMap.name;
                StringBuilder sb = new StringBuilder();
                sb.append("dispatch ");
                sb.append(makeSignal(signalId, signalVal));
                sb.append(" [ ");
                sb.append(name);
                sb.append(" = ");
                sb.append(signalVal);
                sb.append(" ]");
                sb.append(duplicated ? ", duplicated signal!" : "");
                LogUtil.i(TAG, sb.toString());
                if (!duplicated) {
                    this.mLastSignal.put(Integer.valueOf(signalId), Integer.valueOf(signalVal));
                    dispatchSpeech(signalMap.getTtsPolicy(signalVal), name, signalId, signalVal);
                    dispatchSound(signalMap.getSoundPolicy(signalVal), signalId, signalVal);
                    dispatchOsd(signalMap.getOsdPolicy(signalVal), signalId);
                }
            }
        }
    }

    private synchronized void loadSignalMapArray(ArrayMap<Integer, String> files, ArrayMap<Integer, SignalMap> signals) {
        for (Integer num : files.keySet()) {
            int signalId = num.intValue();
            SignalMap signalMap = new SignalMap().readFromJson(files.get(Integer.valueOf(signalId)));
            if (signalMap == null) {
                LogUtil.i(TAG, "read " + files.get(Integer.valueOf(signalId)) + " failed!");
            }
            signals.put(Integer.valueOf(signalId), signalMap);
        }
    }

    private void addXpuManagerCallback() {
        mSigMapRd11 = new SignalMap().readFromJson("rdmodulecom_11.json");
        if (mSigMapRd11 == null) {
            LogUtil.e(TAG, "load rdmodulecom_11.json failed");
        }
        mSigMapRd13 = new SignalMap().readFromJson("rdmodulecom_13.json");
        if (mSigMapRd13 == null) {
            LogUtil.e(TAG, "load rdmodulecom_13.json failed");
        }
        mSigMapRd43 = new SignalMap().readFromJson("rdmodulecom_43.json");
        if (mSigMapRd43 == null) {
            LogUtil.e(TAG, "load rdmodulecom_43.json failed");
        }
        if (SystemProperties.getBoolean("persist.sys.bindxpuservice", true)) {
            LogUtil.i(TAG, "use bindMessage protocol, pkgName=" + getXpuServicePackageName() + ", className=" + getXpuServiceClassName());
            mBindService = new BindXpuService(561002513);
            return;
        }
        LogUtil.i(TAG, "use someip protocol");
        this.mXpuCallback = new CarXpuManager.CarXpuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartAlertService.3
            public void onChangeEvent(CarPropertyValue value) {
                LogUtil.d(SmartAlertService.TAG, "onXpuEvent: " + value.toString());
                if (value.getPropertyId() == r2) {
                    byte[] values = (byte[]) BaseCarListener.getValue(value);
                    if (SmartAlertService.this.mPackage.hasValid(values)) {
                        SmartAlertService.this.dispatch(SmartAlertService.mSigMapRd11, r2 + 11, SmartAlertService.this.mPackage.getRd11Data());
                        SmartAlertService.this.dispatch(SmartAlertService.mSigMapRd13, r2 + 13, SmartAlertService.this.mPackage.getRd13Data());
                        return;
                    }
                    return;
                }
                LogUtil.d(SmartAlertService.TAG, "unhandle onXpuEvent " + r2);
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        addXpuManagerListener(this.mXpuCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void stopSpeechAndSound() {
        LogUtil.i(TAG, "stopSpeechAndSound");
        this.mHandler.removeMessages(0);
        for (Integer num : this.mSigMapSpeech.keySet()) {
            this.mHandler.removeMessages(this.mSigMapSpeech.get(Integer.valueOf(num.intValue())).intValue());
        }
        for (Integer num2 : this.mSigMapSound.keySet()) {
            int signalId = num2.intValue();
            int signal = makeSignal(signalId, this.mSigMapSound.get(Integer.valueOf(signalId)).intValue());
            handleStopSound(signal);
        }
        handleStopOsd();
        this.mModeIndex.clearStatus();
        this.mSigSetSpeech.clear();
        this.mSigSetOsd.clear();
        this.mLastSignal.clear();
        this.mSigMapSpeech.clear();
        this.mSigMapSound.clear();
    }

    private void addMcuManagerCallback() {
        this.mMcuCallback = new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartAlertService.4
            public void onChangeEvent(CarPropertyValue value) {
                int signalId = value.getPropertyId();
                LogUtil.d(SmartAlertService.TAG, "onMcuEvent: " + value.toString());
                if (signalId == 557847561) {
                    int signalVal = ((Integer) value.getValue()).intValue();
                    boolean unused = SmartAlertService.hasIgOn = signalVal == 1;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Mcu onChangeEvent: hasIgOn=");
                    sb.append(SmartAlertService.hasIgOn ? OOBEEvent.STRING_TRUE : OOBEEvent.STRING_FALSE);
                    LogUtil.i(SmartAlertService.TAG, sb.toString());
                    if (!SmartAlertService.hasIgOn) {
                        SmartAlertService.this.stopSpeechAndSound();
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        addMcuManagerListener(this.mMcuCallback);
    }

    private void addVcuManagerCallback() {
        ArrayMap<Integer, String> files = new ArrayMap<>();
        files.put(559944229, "vcu_raw_car_speed.json");
        final ArrayMap<Integer, SignalMap> signals = new ArrayMap<>();
        loadSignalMapArray(files, signals);
        this.mVcuCallback = new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartAlertService.5
            public void onChangeEvent(CarPropertyValue value) {
                int signalId = value.getPropertyId();
                LogUtil.d(SmartAlertService.TAG, "onVcuEvent: " + value.toString());
                if (signals.containsKey(Integer.valueOf(signalId))) {
                    if (signalId == 559944229) {
                        int status = SmartAlertService.this.mModeIndex.getStatus();
                        int mapVal = 0;
                        float carSpeed = ((Float) value.getValue()).floatValue() * 1.05f;
                        ModeIndex unused = SmartAlertService.this.mModeIndex;
                        if (status == 0) {
                            return;
                        }
                        ModeIndex unused2 = SmartAlertService.this.mModeIndex;
                        if (status == 4 || carSpeed <= 121.0f || carSpeed >= 125.0f) {
                            ModeIndex unused3 = SmartAlertService.this.mModeIndex;
                            if (status == 4 && carSpeed > 80.0f && carSpeed < 85.0f) {
                                mapVal = 2;
                            }
                        } else {
                            mapVal = 1;
                        }
                        if (mapVal != 0) {
                            LogUtil.d(SmartAlertService.TAG, "current carSpeed = " + carSpeed + " km/h");
                            SmartAlertService.this.dispatch((SignalMap) signals.get(Integer.valueOf(signalId)), signalId, mapVal);
                            return;
                        }
                        return;
                    }
                    return;
                }
                LogUtil.d(SmartAlertService.TAG, "unhandle onVcuEvent " + signalId);
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        addVcuManagerListener(this.mVcuCallback);
    }

    private void addVpmManagerCallback() {
        ArrayMap<Integer, String> files = new ArrayMap<>();
        files.put(557852253, "vpm_ldw_left_warning_st.json");
        files.put(557852252, "vpm_ldw_right_warning_st.json");
        files.put(557852376, "vpm_rdp_left_warning_st.json");
        files.put(557852375, "vpm_rdp_right_warning_st.json");
        final ArrayMap<Integer, SignalMap> signals = new ArrayMap<>();
        loadSignalMapArray(files, signals);
        this.mVpmCallback = new CarVpmManager.CarVpmEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartAlertService.6
            public void onChangeEvent(CarPropertyValue value) {
                int signalId = value.getPropertyId();
                LogUtil.d(SmartAlertService.TAG, "onVpmEvent: " + value.toString());
                if (signals.containsKey(Integer.valueOf(signalId))) {
                    int signalVal = ((Integer) value.getValue()).intValue();
                    SmartAlertService.this.dispatch((SignalMap) signals.get(Integer.valueOf(signalId)), signalId, signalVal);
                    return;
                }
                LogUtil.d(SmartAlertService.TAG, "unhandle onVpmEvent" + signalId);
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        addVpmManagerListener(this.mVpmCallback);
    }

    private void addScuManagerCallback() {
        ArrayMap<Integer, String> files = new ArrayMap<>();
        files.put(557852235, "scu_mode_index.json");
        files.put(557852212, "scu_acc_st.json");
        files.put(557852187, "scu_operation_tips.json");
        files.put(557852300, "scu_acc_rd_voice_tips.json");
        files.put(557852197, "scu_rcta_left_warning.json");
        files.put(557852198, "scu_rcta_right_warning.json");
        files.put(557852286, "scu_dow_lwarning.json");
        files.put(557852287, "scu_dow_rwarning.json");
        files.put(557852326, "scu_sdc_ctrl_uradarvoice_tone.json");
        files.put(557852199, "scu_longctrl_remind.json");
        files.put(557852201, "scu_latctrl_remind.json");
        files.put(557852295, "scu_latctrl_remind_2.json");
        files.put(557852368, "scu_aeb_alarm_swst.json");
        files.put(557852370, "scu_dsm_remind.json");
        files.put(557852274, "scu_alcctrl_remind.json");
        files.put(557852302, "scu_lka_state.json");
        files.put(557852288, "scu_acc_lka_warning.json");
        files.put(557917782, "scu_ldw_warning.json");
        files.put(557917780, "scu_rcta_warning.json");
        files.put(557852394, "scu_spdlit_vocremind.json");
        files.put(557852409, "scu_hmi_dop_remind.json");
        final ArrayMap<Integer, SignalMap> signals = new ArrayMap<>();
        loadSignalMapArray(files, signals);
        this.mScuCallback = new CarScuManager.CarScuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartAlertService.7
            public void onChangeEvent(CarPropertyValue value) {
                int signalId = value.getPropertyId();
                LogUtil.d(SmartAlertService.TAG, "onScuEvent: " + value.toString());
                if (value.getValue() instanceof Integer) {
                    SmartAlertService.this.handleAccLccExitReason(signalId, ((Integer) value.getValue()).intValue());
                }
                if (signals.containsKey(Integer.valueOf(signalId))) {
                    if (signalId == 557852235) {
                        SignalMap unused = SmartAlertService.mSigMapModeIndex = (SignalMap) signals.get(Integer.valueOf(signalId));
                        SmartAlertService.this.handleDelayedModeIndex(((Integer) value.getValue()).intValue());
                        return;
                    }
                    int i = 0;
                    if (signalId == 557917780) {
                        Integer[] signalVal = (Integer[]) value.getValue();
                        if (signalVal[0].intValue() == 3 && signalVal[1].intValue() == 2) {
                            i = 1;
                        }
                        int rcta_mapping = i;
                        SmartAlertService.this.dispatch((SignalMap) signals.get(Integer.valueOf(signalId)), signalId, rcta_mapping);
                        return;
                    } else if (signalId != 557917782) {
                        SmartAlertService.this.dispatch((SignalMap) signals.get(Integer.valueOf(signalId)), signalId, ((Integer) value.getValue()).intValue());
                        return;
                    } else {
                        Integer[] signalVal2 = (Integer[]) value.getValue();
                        if (signalVal2[0].intValue() == 3 && (signalVal2[1].intValue() == 1 || signalVal2[1].intValue() == 2)) {
                            i = 1;
                        }
                        int ldw_mapping = i;
                        SmartAlertService.this.dispatch((SignalMap) signals.get(Integer.valueOf(signalId)), signalId, ldw_mapping);
                        return;
                    }
                }
                LogUtil.d(SmartAlertService.TAG, "unhandle onScuEvent " + signalId);
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        addScuManagerListener(this.mScuCallback);
    }

    private void removeCarListener() {
        CarXpuManager.CarXpuEventCallback carXpuEventCallback = this.mXpuCallback;
        if (carXpuEventCallback != null) {
            removeXpuManagerListener(carXpuEventCallback);
        }
        removeMcuManagerListener(this.mMcuCallback);
        removeVcuManagerListener(this.mVcuCallback);
        removeVpmManagerListener(this.mVpmCallback);
        removeScuManagerListener(this.mScuCallback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void initCarListener() {
        addXpuManagerCallback();
        addMcuManagerCallback();
        addVcuManagerCallback();
        addVpmManagerCallback();
        addScuManagerCallback();
    }

    private void removeCommonEventListener() {
        try {
            if (this.mCommonMsgController != null) {
                this.mCommonMsgController.unregisterListener(this.mCommonEventListener);
            }
        } catch (Exception e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    public void initXUIManager() throws XUIServiceNotConnectedException {
        super.initXUIManager();
        if (getXuiManager() == null) {
            return;
        }
        ArrayMap<Integer, String> files = new ArrayMap<>();
        files.put(12345681, "xui_context_common_navi.json");
        final ArrayMap<Integer, SignalMap> signals = new ArrayMap<>();
        loadSignalMapArray(files, signals);
        this.mCommonMsgController = CommonMsgController.getInstance(this.mContext);
        this.mCommonEventListener = new CommonMsgController.InternalCommonEventListener() { // from class: com.xiaopeng.xuiservice.smart.SmartAlertService.8
            @Override // com.xiaopeng.xuiservice.carcontrol.CommonMsgController.InternalCommonEventListener
            public void onInternalCommonEvent(int eventType, int eventValue) {
                if (eventType == 3) {
                    SmartAlertService.this.dispatch((SignalMap) signals.get(12345681), 12345681, eventValue);
                }
            }
        };
        try {
            this.mCommonMsgController.registerListener(this.mCommonEventListener);
        } catch (Exception e) {
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        this.mCarMcuManager = getCarManager("xp_mcu");
        try {
            boolean z = true;
            if (this.mCarMcuManager.getIgStatusFromMcu() != 1) {
                z = false;
            }
            hasIgOn = z;
        } catch (Exception e) {
            LogUtil.e(TAG, "getIgStatusFromMcu failed, " + e);
        }
        this.mCarBcmManager = getCarManager("xp_bcm");
        this.mCarVcuManager = getCarManager("xp_vcu");
        LogUtil.i(TAG, "onCarManagerInited, hasIgOn=" + hasIgOn);
    }

    @Override // com.xiaopeng.xuiservice.utils.BroadcastManager.BroadcastListener
    public void onReceive(Context context, Intent intent) {
        LogUtil.i(TAG, "onReceive " + intent);
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") && !hasBootCompleted) {
            this.mToast.notifySystemUiAction(false);
            hasBootCompleted = true;
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
        LogUtil.i(TAG, "onInit");
        this.mToast.init();
        registerReceiver();
        registerObserver();
        boolean z = false;
        restrictFullScreenApps(false);
        hasSimpleMode = getSimpleMode();
        hasOtaStatus = getOtaStatus();
        this.mContentMapCollection = new ContentMapCollection().readFromJson(getLocaleSpeechContent());
        if (this.mContentMapCollection == null) {
            LogUtil.e(TAG, "load localeContent failed!");
        }
        this.mSoundPlay = XpSoundPlayService.getInstance();
        if (this.mSoundPlay != null) {
            List<String> files = FileUtils.getAllFiles(SOUND_PATH, "wav");
            for (String file : files) {
                this.mSoundPlay.load(file);
            }
        }
        this.mSpeechTts = XpSpeechTtsService.getInstance();
        if (this.mSpeechTts != null) {
            this.mSpeechTtsListener = new XpSpeechTtsService.SpeechTtsEventListener() { // from class: com.xiaopeng.xuiservice.smart.SmartAlertService.9
                @Override // com.xiaopeng.xuiservice.capabilities.XpSpeechTtsService.SpeechTtsEventListener
                public void onSpeechTtsEventCallBack(int event, String ttsId) {
                    LogUtil.d(SmartAlertService.TAG, "onSpeechTts " + SmartAlertService.this.parseSpeechEvent(event) + ", ttsId=" + ttsId);
                }
            };
            this.mSpeechTts.addSpeechTtsEventListener(this.mSpeechTtsListener);
        }
        if (this.mSoundPlay != null && this.mSpeechTts != null) {
            z = true;
        }
        hasInit = z;
        LogUtil.i(TAG, "hasInit=" + hasInit + ", hasIgOn=" + hasIgOn + ", hasSimpleMode=" + hasSimpleMode + ", hasOtaStatus=" + hasOtaStatus);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        LogUtil.i(TAG, "onRelease");
        unregisterObservier();
        removeCarListener();
        removeCommonEventListener();
        this.mHandlerThread.quit();
    }

    public SmartAlertService(Context context) {
        super(context);
        this.mSoundPlay = null;
        this.mSpeechTts = null;
        this.mSpeechTtsListener = null;
        this.mXpuCallback = null;
        this.mMcuCallback = null;
        this.mVcuCallback = null;
        this.mVpmCallback = null;
        this.mScuCallback = null;
        this.mPackage = new NgpPackage();
        this.mToast = new ToastOsd();
        this.mModeIndex = new ModeIndex();
        this.mLastSignal = new ArrayMap<>();
        this.mSigMapSound = new ArrayMap<>();
        this.mSigMapSpeech = new ArrayMap<>();
        this.mSigSetSpeech = new HashSet();
        this.mSigSetOsd = new HashSet();
        this.mContentMapCollection = null;
        this.mHandlerThread = null;
        this.mHandler = null;
        this.mSimpleMode = new ContentObserver(new XuiWorkHandler()) { // from class: com.xiaopeng.xuiservice.smart.SmartAlertService.1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                boolean unused = SmartAlertService.hasSimpleMode = SmartAlertService.this.getSimpleMode();
                LogUtil.i(SmartAlertService.TAG, "hasSimpleMode=" + SmartAlertService.hasSimpleMode);
            }
        };
        this.mOtaCfg = new ContentObserver(new XuiWorkHandler()) { // from class: com.xiaopeng.xuiservice.smart.SmartAlertService.2
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                boolean unused = SmartAlertService.hasOtaStatus = SmartAlertService.this.getOtaStatus();
                LogUtil.i(SmartAlertService.TAG, "hasOtaStatus=" + SmartAlertService.hasOtaStatus);
            }
        };
        this.mHandlerThread = new HandlerThread("SmartAlertThread");
        this.mHandlerThread.start();
        this.mHandler = new Handler(this.mHandlerThread.getLooper()) { // from class: com.xiaopeng.xuiservice.smart.SmartAlertService.10
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                int i = msg.what;
                if (i == 0) {
                    int signalVal = ((Integer) msg.obj).intValue();
                    List<Integer> maps = SmartAlertService.this.mModeIndex.getMapValue(signalVal);
                    for (Integer num : maps) {
                        int val = num.intValue();
                        LogUtil.i(SmartAlertService.TAG, "mode_index " + signalVal + " -> " + val);
                        SmartAlertService.this.dispatch(SmartAlertService.mSigMapModeIndex, 557852235, val);
                    }
                    return;
                }
                if (i == 1) {
                    PlaySound playSound = (PlaySound) msg.obj;
                    Sound sound = playSound.getSound();
                    int signal = playSound.getSignal();
                    int position = sound.position != 0 ? sound.position : 5;
                    int priority = 7 - sound.priority;
                    LogUtil.i(SmartAlertService.TAG, "play sound, signal=" + signal + " (" + sound.source + ", position=" + position + ", priority=" + sound.priority + ", loop=" + sound.loop + ")");
                    XpSoundPlayService xpSoundPlayService = SmartAlertService.this.mSoundPlay;
                    StringBuilder sb = new StringBuilder();
                    sb.append(SmartAlertService.SOUND_PATH);
                    sb.append(sound.source);
                    xpSoundPlayService.play(signal, sb.toString(), sound.leftVolume, sound.rightVolume, position, priority, sound.loop);
                } else if (i == 2) {
                    int signal2 = ((Integer) msg.obj).intValue();
                    LogUtil.i(SmartAlertService.TAG, "stop sound, signal=" + signal2);
                    SmartAlertService.this.mSoundPlay.stop(signal2);
                } else if (i == 3) {
                    int type = ((Integer) msg.obj).intValue();
                    SmartAlertService.this.mToast.onVisibleView(type);
                } else if (i == 4) {
                    SmartAlertService.this.mToast.offVisibleView();
                } else if (i == 6) {
                    if (SmartAlertService.mBindService != null && !SmartAlertService.mBindService.bindService()) {
                        SmartAlertService.this.handleBindService(true, 5000L);
                    }
                } else if (i == 7) {
                    if (SmartAlertService.mBindService != null) {
                        SmartAlertService.mBindService.unbindService();
                        SmartAlertService.this.handleBindService(true, 800L);
                    }
                } else {
                    SpeechTts speech = (SpeechTts) msg.obj;
                    String ttsId = null;
                    int priority2 = speech.getPriority();
                    boolean hasShutUp = priority2 == 0 || priority2 == 3;
                    if (msg.what == speech.signal) {
                        LogUtil.d(SmartAlertService.TAG, "handle delay message " + msg.what);
                    } else if (msg.what != 5) {
                        return;
                    }
                    if (speech.getOnlyOnce()) {
                        SmartAlertService.this.mSigSetSpeech.add(Integer.valueOf(speech.signal));
                    }
                    if (priority2 == 0 || priority2 == 2) {
                        ttsId = SmartAlertService.this.mSpeechTts.speakByInstant(speech.getContent(), hasShutUp);
                    } else if (priority2 == 1 || priority2 == 3) {
                        ttsId = SmartAlertService.this.mSpeechTts.speakByUrgent(speech.getContent(), hasShutUp);
                    } else {
                        LogUtil.w(SmartAlertService.TAG, "Invalid tts priority!");
                    }
                    LogUtil.i(SmartAlertService.TAG, "speech: " + speech.getContent() + ", ttsId=" + ttsId + ", priority=" + priority2 + ", hasShutUp=" + hasShutUp);
                    if (speech.getLoop()) {
                        SmartAlertService.this.handleSpeechTts(msg.what, speech);
                    }
                }
            }
        };
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final SmartAlertService sService = new SmartAlertService(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    public static SmartAlertService getInstance() {
        return InstanceHolder.sService;
    }

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService
    public void dump(PrintWriter pw, String[] args) {
        int val = 0;
        pw.println("*dump-SmartAlertService");
        if (args != null && args.length == 2) {
            try {
                val = Integer.parseInt(args[1]);
            } catch (Exception e) {
            }
            if ("--rd11".equals(args[0])) {
                pw.println("*test rd11 = " + val);
                dispatch(mSigMapRd11, 561002513 + 11, val);
            } else if ("--rd13".equals(args[0])) {
                pw.println("*test rd13 = " + val);
                dispatch(mSigMapRd13, 561002513 + 13, val);
            } else if ("--rd43".equals(args[0])) {
                pw.println("*test rd43 = " + val);
                dispatch(mSigMapRd43, 561002513 + 43, val);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected boolean needConnectXUI() {
        return true;
    }
}
