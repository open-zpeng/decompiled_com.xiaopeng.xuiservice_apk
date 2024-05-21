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
import com.alipay.mobile.aromeservice.RequestParams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xiaopeng.biutil.BiLog;
import com.xiaopeng.biutil.BiLogFactory;
import com.xiaopeng.speech.protocol.bean.recommend.RecommendBean;
import com.xiaopeng.speech.protocol.bean.stats.SceneSwitchStatisticsBean;
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
import com.xiaopeng.xuiservice.innerutils.BiLogTransmit;
import com.xiaopeng.xuiservice.innerutils.DataLogUtils;
import com.xiaopeng.xuiservice.innerutils.LocaleStrings;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionWindowPos;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.FileUtils;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.UByte;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class SmartAlarmService extends BaseSmartService implements BroadcastManager.BroadcastListener {
    private static final int DEFAULT = 5;
    private static final int DONE = 1;
    private static final int ERROR = 2;
    private static final int FRONT = 7;
    private static final int IG_ON = 1;
    private static final String KEY_LDW_MODE = "lcc_ldw_type";
    private static final String KEY_LOCALE = "tts_locale";
    private static final String KEY_SIMPLE_MODE = "ngp_broadcast_type";
    private static final String KEY_XPILOT_STATUS = "curXPilotStatus";
    private static final int MESSAGE_BIND = 6;
    private static final int MESSAGE_ONPLAY = 8;
    private static final int MESSAGE_ONSPEECH = 9;
    private static final int MESSAGE_PLAY_OSD = 3;
    private static final int MESSAGE_PLAY_SOUND = 1;
    private static final int MESSAGE_SPEECH = 5;
    private static final int MESSAGE_STOP_OSD = 4;
    private static final int MESSAGE_STOP_SOUND = 2;
    private static final int MESSAGE_UNBIND = 7;
    private static final int NGP_SIGNAL = 561002513;
    private static final int REAR = 6;
    private static final int RESTART = 4;
    private static final String SIGNAL_PATH = "/system/etc/xuiservice/xpilot/signalmap/";
    private static final String SOUND_PATH = "/system/media/audio/xiaopeng/xpilot/wav/";
    private static final int START = 0;
    private static final int STOP = 3;
    private static final String TAG = "SmartAlarmService";
    private static final int TYPE_SOUND = 1;
    private static final int TYPE_TTS = 0;
    private Map<Long, BiAlarm> mBiMap;
    private CarBcmManager mCarBcmManager;
    private CarMcuManager mCarMcuManager;
    private CarScuManager mCarScuManager;
    private CarVcuManager mCarVcuManager;
    private CommonMsgController.InternalCommonEventListener mCommonEventListener;
    private CommonMsgController mCommonMsgController;
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    private Map<Integer, Integer> mLastSignal;
    private CarMcuManager.CarMcuEventCallback mMcuCallback;
    private NgpPackage mPackage;
    private CarScuManager.CarScuEventCallback mScuCallback;
    private Map<Integer, String> mSigMapName;
    private Map<Integer, Integer> mSigMapSound;
    private Map<Integer, Integer> mSigMapSpeech;
    private Set<Integer> mSigSetOsd;
    private Set<Integer> mSigSetSpeech;
    private ContentObserver mSimpleMode;
    private XpSoundPlayService mSoundPlay;
    private XpSoundPlayService.PlaySoundEventListener mSoundPlayListener;
    private XpSpeechTtsService mSpeechTts;
    private XpSpeechTtsService.SpeechTtsEventListener mSpeechTtsListener;
    private ToastOsd mToast;
    private CarVcuManager.CarVcuEventCallback mVcuCallback;
    private CarVpmManager.CarVpmEventCallback mVpmCallback;
    private CarXpuManager.CarXpuEventCallback mXpuCallback;
    private static BindXpuService mBindService = null;
    private static boolean hasBindService = SystemProperties.getBoolean("persist.sys.bindxpuservice", true);
    private static boolean hasBootCompleted = false;
    private static boolean hasInit = false;
    private static boolean hasIgOn = false;
    private static boolean hasSimpleMode = false;
    private static boolean lastNgpStatus = false;
    private static int mModeIndex = 0;
    private static Random mRand = new Random();

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService, com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
    public /* bridge */ /* synthetic */ void debugDump(PrintWriter printWriter, String[] strArr) {
        super.debugDump(printWriter, strArr);
    }

    private boolean checkFileExists(String path) {
        return new File(path).exists();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getSimpleMode() {
        return Settings.System.getInt(this.mContext.getContentResolver(), KEY_SIMPLE_MODE, 1) == 0;
    }

    private int getLdwMode() {
        return Settings.System.getInt(this.mContext.getContentResolver(), KEY_LDW_MODE, 3);
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
    }

    private void unregisterObservier() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mSimpleMode);
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
        public boolean hasSimpleMute;
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
        public boolean hasShutUp;
        public boolean hasSimpleContent;
        public int priority;
        public String resId;
        public String resSimpleId;
        public int strategy;
        public int value;

        private Tts() {
        }

        public String getTtsContent(String stringId) {
            String content = LocaleStrings.getInstance().getString(stringId);
            if (content == null) {
                String[] contents = LocaleStrings.getInstance().getStringArray(stringId);
                if (contents != null) {
                    return contents[SmartAlarmService.mRand.nextInt(contents.length)];
                }
                return null;
            }
            return content;
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
                    reader = new BufferedReader(new FileReader(SmartAlarmService.SIGNAL_PATH + file));
                    Gson gson = new GsonBuilder().create();
                    SignalMap signalMap = (SignalMap) gson.fromJson((Reader) reader, (Class<Object>) SignalMap.class);
                    try {
                        reader.close();
                    } catch (Exception e) {
                        LogUtil.e(SmartAlarmService.TAG, "close " + file + " Exception: " + e);
                    }
                    return signalMap;
                } catch (Throwable th) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e2) {
                            LogUtil.e(SmartAlarmService.TAG, "close " + file + " Exception: " + e2);
                        }
                    }
                    throw th;
                }
            } catch (Exception e3) {
                LogUtil.e(SmartAlarmService.TAG, "readFromJson Exception: " + e3);
                if (reader != null) {
                    try {
                        reader.close();
                        return null;
                    } catch (Exception e4) {
                        LogUtil.e(SmartAlarmService.TAG, "close " + file + " Exception: " + e4);
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
            LogUtil.e(SmartAlarmService.TAG, "ngp data checksum failed!");
            return false;
        }

        public int getRd11Data() {
            return this.mRdModuleCom11;
        }

        public int getRd13Data() {
            return this.mRdModuleCom13;
        }
    }

    /* loaded from: classes5.dex */
    private class BindXpuService {
        private static final int MESSAGE_HANDSHAKE = 0;
        private static final int MESSAGE_NGP_DATA = 1;
        private ServiceConnection connection;
        private Intent intent;
        private Messenger receiver;
        private HandlerThread thread;
        private ArrayMap<Integer, SignalMap> signals = new ArrayMap<>();
        private boolean hasConnected = false;

        /* JADX INFO: Access modifiers changed from: private */
        public void request(Messenger sender, Messenger receiver) {
            Bundle bundle = new Bundle();
            bundle.putString("clientid", "xuiclient");
            Message message = Message.obtain((Handler) null, 0);
            message.setData(bundle);
            message.replyTo = receiver;
            LogUtil.i(SmartAlarmService.TAG, "request xpuService...");
            try {
                sender.send(message);
            } catch (Exception e) {
                LogUtil.e(SmartAlarmService.TAG, "request xpuService failed " + e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void resetData() {
            SmartAlarmService.this.dispatch(this.signals.get(561002524), 561002524, 0);
            SmartAlarmService.this.dispatch(this.signals.get(561002526), 561002526, 0);
            SmartAlarmService.this.dispatch(this.signals.get(561002556), 561002556, 0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean bindService() {
            this.hasConnected = SmartAlarmService.this.mContext.bindService(this.intent, this.connection, 1);
            StringBuilder sb = new StringBuilder();
            sb.append("bindService ");
            sb.append(this.hasConnected ? "succeed" : "failed");
            LogUtil.i(SmartAlarmService.TAG, sb.toString());
            return this.hasConnected;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void unbindService() {
            if (this.hasConnected) {
                LogUtil.i(SmartAlarmService.TAG, "unbindService");
                SmartAlarmService.this.mContext.unbindService(this.connection);
                this.hasConnected = false;
            }
        }

        public void test(String cmd, int value) {
            char c;
            int hashCode = cmd.hashCode();
            if (hashCode == 1333364082) {
                if (cmd.equals("--rd11")) {
                    c = 0;
                }
                c = 65535;
            } else if (hashCode != 1333364084) {
                if (hashCode == 1333364177 && cmd.equals("--rd43")) {
                    c = 2;
                }
                c = 65535;
            } else {
                if (cmd.equals("--rd13")) {
                    c = 1;
                }
                c = 65535;
            }
            if (c == 0) {
                SmartAlarmService.this.dispatch(this.signals.get(561002524), 561002524, value);
            } else if (c == 1) {
                SmartAlarmService.this.dispatch(this.signals.get(561002526), 561002526, value);
            } else if (c == 2) {
                SmartAlarmService.this.dispatch(this.signals.get(561002556), 561002556, value);
            }
        }

        public BindXpuService() {
            ArrayMap<Integer, String> files = new ArrayMap<>();
            files.put(561002524, "rdmodulecom_11.json");
            files.put(561002526, "rdmodulecom_13.json");
            files.put(561002556, "rdmodulecom_43.json");
            SmartAlarmService.this.loadSignalMapArray(files, this.signals);
            this.intent = new Intent();
            this.intent.setClassName(SmartAlarmService.this.getXpuServicePackageName(), SmartAlarmService.this.getXpuServiceClassName());
            this.thread = new HandlerThread("bindXpuServiceThread");
            this.thread.start();
            this.receiver = new Messenger(new Handler(this.thread.getLooper()) { // from class: com.xiaopeng.xuiservice.smart.SmartAlarmService.BindXpuService.1
                @Override // android.os.Handler
                public void handleMessage(Message msg) {
                    LogUtil.d(SmartAlarmService.TAG, "receive bindMessage id=" + msg.what + ", message=" + msg.getData());
                    int i = msg.what;
                    if (i == 0) {
                        try {
                            String status = msg.getData().getString("bindStatus");
                            if (!TextUtils.isEmpty(status) && status.equals("succeed")) {
                                LogUtil.i(SmartAlarmService.TAG, "bindStatus ok");
                            }
                        } catch (Exception e) {
                            LogUtil.e(SmartAlarmService.TAG, "bindStatus failed" + e);
                        }
                    } else if (i == 1) {
                        try {
                            int now = (int) System.currentTimeMillis();
                            int last = msg.getData().getInt("ts");
                            List<Integer> data = msg.getData().getIntegerArrayList("data");
                            if (data != null && data.size() == 3) {
                                LogUtil.d(SmartAlarmService.TAG, "bindMessage rd11=" + data.get(0) + ", rd13=" + data.get(1) + ", rd43=" + data.get(2) + ", delayed=" + (now - last));
                                SmartAlarmService.this.dispatch((SignalMap) BindXpuService.this.signals.get(561002524), 561002524, data.get(0).intValue());
                                SmartAlarmService.this.dispatch((SignalMap) BindXpuService.this.signals.get(561002526), 561002526, data.get(1).intValue());
                                SmartAlarmService.this.dispatch((SignalMap) BindXpuService.this.signals.get(561002556), 561002556, data.get(2).intValue());
                            }
                        } catch (Exception e2) {
                            LogUtil.e(SmartAlarmService.TAG, "bindMessage failed" + e2);
                        }
                    }
                }
            });
            this.connection = new ServiceConnection() { // from class: com.xiaopeng.xuiservice.smart.SmartAlarmService.BindXpuService.2
                @Override // android.content.ServiceConnection
                public void onServiceConnected(ComponentName name, IBinder service) {
                    LogUtil.i(SmartAlarmService.TAG, name.getClassName() + " onServiceConnected");
                    BindXpuService.this.request(new Messenger(service), BindXpuService.this.receiver);
                }

                @Override // android.content.ServiceConnection
                public void onServiceDisconnected(ComponentName name) {
                    LogUtil.w(SmartAlarmService.TAG, name.getClassName() + " onServiceDisconnected");
                    BindXpuService.this.resetData();
                    SmartAlarmService.this.handleBindService(false, 500L);
                }

                @Override // android.content.ServiceConnection
                public void onBindingDied(ComponentName name) {
                    LogUtil.e(SmartAlarmService.TAG, name.getClassName() + " onBindingDied");
                    BindXpuService.this.resetData();
                    SmartAlarmService.this.handleBindService(false, 500L);
                }
            };
            SmartAlarmService.this.handleBindService(true, 500L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class PlaySound {
        public int signal;
        public Sound sound;
        public long ts;

        public PlaySound(Sound sound, int signal, long ts) {
            this.sound = sound;
            this.signal = signal;
            this.ts = ts;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class SpeechTts {
        private String content;
        private int delay;
        private boolean hasLoop;
        private boolean hasOnlyOnce;
        private boolean hasShutUp;
        private int nightDelay;
        private int priority;
        private int signal;
        private long ts;

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

        public void setShutUp(boolean hasShutUp) {
            this.hasShutUp = hasShutUp;
        }

        public boolean getShutUp() {
            return this.hasShutUp;
        }

        public void setOnlyOnce(boolean hasOnlyOnce) {
            this.hasOnlyOnce = hasOnlyOnce;
        }

        public boolean getOnlyOnce() {
            return this.hasOnlyOnce;
        }

        public SpeechTts(long ts) {
            this.ts = ts;
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
            LogUtil.i(SmartAlarmService.TAG, "ToastOsd.init");
            Context context = SmartAlarmService.this.mContext;
            Context context2 = SmartAlarmService.this.mContext;
            this.wm = (WindowManager) context.getSystemService(ConditionWindowPos.TYPE);
            WindowManager.LayoutParams layoutParams = this.params;
            layoutParams.type = 2049;
            layoutParams.flags = 8;
            layoutParams.format = -2;
            layoutParams.width = SmartAlarmService.this.mContext.getResources().getDimensionPixelSize(R.dimen.sr_warning_layout_width);
            this.params.height = SmartAlarmService.this.mContext.getResources().getDimensionPixelSize(R.dimen.sr_warning_layout_height);
            this.params.gravity = 17;
            this.inflater = LayoutInflater.from(SmartAlarmService.this.mContext);
            this.view = this.inflater.inflate(R.layout.sr_warning_bg, (ViewGroup) null);
            this.layout = (RelativeLayout) this.view.findViewById(R.id.background);
            this.textView = (XTextView) this.view.findViewById(R.id.text);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void notifySystemUiAction(boolean onoff) {
            LogUtil.d(SmartAlarmService.TAG, "notifySystemUiAction action=" + onoff);
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
                    LogUtil.i(SmartAlarmService.TAG, "osd on, type=" + type);
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
                LogUtil.i(SmartAlarmService.TAG, "osd off");
                notifySystemUiAction(false);
                this.wm.removeView(this.view);
                this.status = false;
            }
        }
    }

    /* loaded from: classes5.dex */
    private class BiAlarm {
        private int signal;
        private int streamId;
        private String ttsId;
        private boolean hasTts = false;
        private boolean hasSound = false;
        private JSONObject tts = new JSONObject();
        private JSONObject sound = new JSONObject();
        private BiLog bilog = BiLogFactory.create(DataLogUtils.XUI_PID, DataLogUtils.ALARM_BID);

        public boolean isCompleted() {
            return (this.hasTts || this.hasSound) ? false : true;
        }

        public void addTts(String content, int priority) {
            if (!this.hasTts) {
                try {
                    this.tts.put("content", content);
                    this.tts.put("priority", priority);
                } catch (Exception e) {
                }
                this.hasTts = true;
            }
        }

        public void addSound(String source, int priority, String type) {
            if (!this.hasSound) {
                try {
                    this.sound.put("source", source);
                    this.sound.put("priority", priority);
                    this.sound.put(SpeechConstants.KEY_COMMAND_TYPE, type);
                } catch (Exception e) {
                }
                this.hasSound = true;
            }
        }

        public void addTtsStartTime() {
            try {
                this.tts.put(RequestParams.REQUEST_KEY_START_TIME, System.currentTimeMillis());
            } catch (Exception e) {
            }
        }

        public synchronized void submit(int type, String result) {
            try {
                if (type == 0) {
                    this.tts.put(RecommendBean.SHOW_TIME_RESULT, result);
                    this.hasTts = false;
                } else {
                    this.sound.put("endTime", System.currentTimeMillis());
                    this.sound.put(RecommendBean.SHOW_TIME_RESULT, result);
                    this.hasSound = false;
                }
            } catch (Exception e) {
            }
            if (isCompleted()) {
                this.bilog.push("sound", this.sound.toString());
                this.bilog.push("tts", this.tts.toString());
                BiLogTransmit.getInstance().submit(this.bilog);
                LogUtil.d(SmartAlarmService.TAG, "send bilog " + this.bilog.getString());
            }
        }

        public BiAlarm(int signal, long ts) {
            int signalId = (signal >> 16) & 65535;
            int signalVal = 65535 & signal;
            String name = (String) SmartAlarmService.this.mSigMapName.get(Integer.valueOf(signalId));
            this.signal = signal;
            this.bilog.push("name", name);
            this.bilog.push("value", String.valueOf(signalVal));
            this.bilog.push("ts", String.valueOf(ts));
            LogUtil.d(SmartAlarmService.TAG, "create bilog " + ts);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String parseEvent(int event) {
        if (event != 0) {
            if (event != 1) {
                if (event != 2) {
                    if (event != 3) {
                        if (event == 4) {
                            return "restart";
                        }
                        return "unknown";
                    }
                    return "stop";
                }
                return "error";
            }
            return "done";
        }
        return "start";
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

    private void handlePlaySound(PlaySound playSound) {
        LogUtil.d(TAG, "handlePlaySound after " + playSound.sound.delay + "s, signal=" + playSound.signal);
        Message message = this.mHandler.obtainMessage();
        message.what = 1;
        message.obj = playSound;
        message.setAsynchronous(true);
        if (playSound.sound.delay > 0) {
            this.mHandler.sendMessageDelayed(message, playSound.sound.delay * 1000);
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
    public void handleSpeechTts(int messageId, SpeechTts speech) {
        String now = String.format("%tR", new Date());
        boolean hasNight = now.compareTo(getAlertBaseTime()) < 0;
        int delay = (speech.nightDelay <= 0 || !hasNight) ? speech.delay : speech.nightDelay;
        LogUtil.d(TAG, "handleSpeechTts after " + delay + "s, signal=" + speech.signal);
        Message message = this.mHandler.obtainMessage();
        message.what = messageId;
        message.obj = speech;
        message.setAsynchronous(true);
        if (delay > 0) {
            this.mHandler.sendMessageDelayed(message, delay * 1000);
        } else {
            this.mHandler.sendMessage(message);
        }
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

    private boolean hasConditionDoorsStatus(Dependence dependence) {
        return (dependence == null || TextUtils.isEmpty(dependence.condition) || !dependence.condition.equals("doors_status")) ? false : true;
    }

    private boolean hasConditionUseSimpleTtsExceptFirstTime(Dependence dependence) {
        return (dependence == null || TextUtils.isEmpty(dependence.condition) || !dependence.condition.equals("use_simple_tts_except_first_time")) ? false : true;
    }

    private boolean hasConditionVcuRawCarSpeed(Dependence dependence) {
        return (dependence == null || TextUtils.isEmpty(dependence.condition) || !dependence.condition.equals("vcu_car_speed_greater")) ? false : true;
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

    private void dispatchSound(Sound sound, int signal, long ts) {
        int signalId = (signal >> 16) & 65535;
        int signalVal = 65535 & signal;
        if (sound != null) {
            if (sound.hasSimpleMute && hasSimpleMode) {
                return;
            }
            if (hasConditionLdwType(sound.dependence)) {
                LogUtil.i(TAG, "ldwMode=" + getLdwMode() + ", param=" + sound.dependence.parameter);
                if (((1 << getLdwMode()) & sound.dependence.parameter) == 0) {
                    return;
                }
            }
            if (hasConditionDoorsStatus(sound.dependence)) {
                boolean leftSide = sound.dependence.parameter == 0;
                sound.position = getDoorsState(leftSide);
                StringBuilder sb = new StringBuilder();
                sb.append("get ");
                sb.append(leftSide ? "left" : "right");
                sb.append(" doorPosition=");
                sb.append(sound.position);
                LogUtil.i(TAG, sb.toString());
            }
            this.mSigMapSound.put(Integer.valueOf(signalId), Integer.valueOf(signalVal));
            handlePlaySound(new PlaySound(sound, signal, ts));
        } else if (this.mSigMapSound.containsKey(Integer.valueOf(signalId))) {
            int last = makeSignal(signalId, this.mSigMapSound.get(Integer.valueOf(signalId)).intValue());
            handleStopSound(last);
            this.mSigMapSound.remove(Integer.valueOf(signalId));
        }
    }

    private void dispatchSpeech(List<Tts> ttsArray, int signal, long ts) {
        String stringId;
        Iterator<Tts> it;
        int signalId;
        String name;
        int last;
        int i = signal;
        int signalId2 = 65535 & (i >> 16);
        String name2 = this.mSigMapName.get(Integer.valueOf(signalId2));
        if (this.mSigMapSpeech.containsKey(Integer.valueOf(signalId2)) && (last = this.mSigMapSpeech.get(Integer.valueOf(signalId2)).intValue()) != i) {
            LogUtil.d(TAG, "remove delay message " + last);
            this.mHandler.removeMessages(last);
            this.mSigMapSpeech.remove(Integer.valueOf(signalId2));
        }
        Iterator<Tts> it2 = ttsArray.iterator();
        while (it2.hasNext()) {
            Tts tts = it2.next();
            int i2 = tts.priority;
            boolean useSimpleContent = false;
            if (tts.dependence != null && tts.dependence.condition != null) {
                LogUtil.i(TAG, "tts condition=" + tts.dependence.condition + ", parameter=" + tts.dependence.parameter);
            }
            if (tts.hasOnlyOnce && this.mSigSetSpeech.contains(Integer.valueOf(signal))) {
                return;
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
                } catch (Exception e) {
                    LogUtil.e(TAG, "getRawCarSpeed or getPureDriveModeFeedback failed, " + e);
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
            boolean hasSimple = (hasSimpleMode && tts.hasSimpleContent) || useSimpleContent;
            if (hasSimple) {
                if (tts.resSimpleId == null) {
                    stringId = name2 + "_" + tts.value + "_simple";
                } else {
                    stringId = tts.resSimpleId;
                }
            } else if (tts.resId == null) {
                stringId = name2 + "_" + tts.value;
            } else {
                stringId = tts.resId;
            }
            String content = tts.getTtsContent(stringId);
            if (TextUtils.isEmpty(content)) {
                it = it2;
                signalId = signalId2;
                name = name2;
            } else {
                int messageId = tts.strategy != 0 ? i : 5;
                if (messageId != 5) {
                    this.mSigMapSpeech.put(Integer.valueOf(signalId2), Integer.valueOf(signal));
                }
                signalId = signalId2;
                name = name2;
                SpeechTts speech = new SpeechTts(ts);
                speech.setSignal(i);
                speech.setContent(content);
                it = it2;
                speech.setPriority(tts.priority);
                speech.setShutUp(tts.hasShutUp);
                speech.setDelay(tts.delay);
                speech.setNightDelay(getConditionNightDelay(tts.dependence));
                speech.setLoop(tts.strategy == 2);
                speech.setOnlyOnce(tts.hasOnlyOnce);
                handleSpeechTts(messageId, speech);
            }
            i = signal;
            signalId2 = signalId;
            name2 = name;
            it2 = it;
        }
    }

    private void dispatchOsd(Osd osd, int signalId) {
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
                int signal = makeSignal(signalId, signalVal);
                if (!duplicated) {
                    long ts = System.currentTimeMillis();
                    LogUtil.i(TAG, "dispatch " + signal + " [ " + signalMap.name + " = " + signalVal + " ], ts=" + ts);
                    this.mLastSignal.put(Integer.valueOf(signalId), Integer.valueOf(signalVal));
                    dispatchSpeech(signalMap.getTtsPolicy(signalVal), signal, ts);
                    dispatchSound(signalMap.getSoundPolicy(signalVal), signal, ts);
                    dispatchOsd(signalMap.getOsdPolicy(signalVal), signalId);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void loadSignalMapArray(ArrayMap<Integer, String> files, ArrayMap<Integer, SignalMap> signals) {
        for (Integer num : files.keySet()) {
            int signalId = num.intValue();
            SignalMap signalMap = new SignalMap().readFromJson(files.get(Integer.valueOf(signalId)));
            int key = (makeSignal(signalId, 0) >> 16) & 65535;
            if (signalMap != null) {
                signals.put(Integer.valueOf(signalId), signalMap);
                this.mSigMapName.put(Integer.valueOf(key), signalMap.getSignalName());
            } else {
                LogUtil.e(TAG, "read " + files.get(Integer.valueOf(signalId)) + " failed!");
            }
        }
    }

    private void addXpuManagerCallback() {
        ArrayMap<Integer, String> files = new ArrayMap<>();
        files.put(557856792, "xpu_ap_tips.json");
        if (!hasBindService) {
            files.put(561002524, "rdmodulecom_11.json");
            files.put(561002526, "rdmodulecom_13.json");
        }
        final ArrayMap<Integer, SignalMap> signals = new ArrayMap<>();
        loadSignalMapArray(files, signals);
        this.mXpuCallback = new CarXpuManager.CarXpuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartAlarmService.2
            public void onChangeEvent(CarPropertyValue value) {
                int signalId = value.getPropertyId();
                if (signals.containsKey(Integer.valueOf(signalId))) {
                    LogUtil.d(SmartAlarmService.TAG, "onXpuEvent: " + value.toString());
                    SmartAlarmService.this.dispatch((SignalMap) signals.get(Integer.valueOf(signalId)), signalId, ((Integer) value.getValue()).intValue());
                } else if (signalId == SmartAlarmService.NGP_SIGNAL && !SmartAlarmService.hasBindService) {
                    byte[] values = (byte[]) BaseCarListener.getValue(value);
                    LogUtil.d(SmartAlarmService.TAG, "onXpuEvent: " + value.toString());
                    if (SmartAlarmService.this.mPackage.hasValid(values)) {
                        SmartAlarmService.this.dispatch((SignalMap) signals.get(Integer.valueOf(signalId + 11)), signalId + 11, SmartAlarmService.this.mPackage.getRd11Data());
                        SmartAlarmService.this.dispatch((SignalMap) signals.get(Integer.valueOf(signalId + 13)), signalId + 13, SmartAlarmService.this.mPackage.getRd13Data());
                    }
                } else {
                    LogUtil.d(SmartAlarmService.TAG, "unhandle onXpuEvent " + signalId);
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        addXpuManagerListener(this.mXpuCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void stopSpeechAndSound() {
        LogUtil.i(TAG, "stopSpeechAndSound");
        for (Integer num : this.mSigMapSpeech.keySet()) {
            this.mHandler.removeMessages(this.mSigMapSpeech.get(Integer.valueOf(num.intValue())).intValue());
        }
        for (Integer num2 : this.mSigMapSound.keySet()) {
            int signalId = num2.intValue();
            int signal = makeSignal(signalId, this.mSigMapSound.get(Integer.valueOf(signalId)).intValue());
            handleStopSound(signal);
        }
        handleStopOsd();
        lastNgpStatus = false;
        restrictFullScreenApps(false);
        this.mSigSetSpeech.clear();
        this.mSigSetOsd.clear();
        this.mLastSignal.clear();
        this.mSigMapSpeech.clear();
        this.mSigMapSound.clear();
    }

    private void addMcuManagerCallback() {
        this.mMcuCallback = new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartAlarmService.3
            public void onChangeEvent(CarPropertyValue value) {
                int signalId = value.getPropertyId();
                LogUtil.d(SmartAlarmService.TAG, "onMcuEvent: " + value.toString());
                if (signalId == 557847561) {
                    int signalVal = ((Integer) value.getValue()).intValue();
                    boolean unused = SmartAlarmService.hasIgOn = signalVal == 1;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Mcu onChangeEvent: hasIgOn=");
                    sb.append(SmartAlarmService.hasIgOn ? OOBEEvent.STRING_TRUE : OOBEEvent.STRING_FALSE);
                    LogUtil.i(SmartAlarmService.TAG, sb.toString());
                    if (!SmartAlarmService.hasIgOn) {
                        SmartAlarmService.this.stopSpeechAndSound();
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        addMcuManagerListener(this.mMcuCallback);
    }

    private void addVpmManagerCallback() {
        ArrayMap<Integer, String> files = new ArrayMap<>();
        files.put(557852253, "vpm_ldw_left_warning_st.json");
        files.put(557852252, "vpm_ldw_right_warning_st.json");
        files.put(557852376, "vpm_rdp_left_warning_st.json");
        files.put(557852375, "vpm_rdp_right_warning_st.json");
        final ArrayMap<Integer, SignalMap> signals = new ArrayMap<>();
        loadSignalMapArray(files, signals);
        this.mVpmCallback = new CarVpmManager.CarVpmEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartAlarmService.4
            public void onChangeEvent(CarPropertyValue value) {
                int signalId = value.getPropertyId();
                if (signals.containsKey(Integer.valueOf(signalId))) {
                    LogUtil.d(SmartAlarmService.TAG, "onVpmEvent: " + value.toString());
                    SmartAlarmService.this.dispatch((SignalMap) signals.get(Integer.valueOf(signalId)), signalId, ((Integer) value.getValue()).intValue());
                    return;
                }
                LogUtil.d(SmartAlarmService.TAG, "unhandle onVpmEvent" + signalId);
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        addVpmManagerListener(this.mVpmCallback);
    }

    private void addScuManagerCallback() {
        ArrayMap<Integer, String> files = new ArrayMap<>();
        files.put(557852235, "scu_mode_index.json");
        files.put(557852187, "scu_operation_tips.json");
        files.put(557852300, "scu_acc_rd_voice_tips.json");
        files.put(557852197, "scu_rcta_left_warning.json");
        files.put(557852198, "scu_rcta_right_warning.json");
        files.put(557852286, "scu_dow_lwarning.json");
        files.put(557852287, "scu_dow_rwarning.json");
        files.put(557852326, "scu_sdc_ctrl_uradarvoice_tone.json");
        files.put(557852368, "scu_aeb_alarm_swst.json");
        files.put(557852370, "scu_dsm_remind.json");
        final ArrayMap<Integer, SignalMap> signals = new ArrayMap<>();
        loadSignalMapArray(files, signals);
        this.mScuCallback = new CarScuManager.CarScuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartAlarmService.5
            public void onChangeEvent(CarPropertyValue value) {
                int signalId = value.getPropertyId();
                if (signals.containsKey(Integer.valueOf(signalId))) {
                    LogUtil.d(SmartAlarmService.TAG, "onScuEvent: " + value.toString());
                    boolean z = true;
                    if (signalId == 557852235) {
                        int unused = SmartAlarmService.mModeIndex = ((Integer) value.getValue()).intValue();
                        if (SmartAlarmService.mModeIndex != 9 && SmartAlarmService.mModeIndex != 10) {
                            z = false;
                        }
                        boolean status = z;
                        if (status != SmartAlarmService.lastNgpStatus) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("mode_index=");
                            sb.append(SmartAlarmService.mModeIndex);
                            sb.append(status ? ", enter" : ", leave");
                            sb.append(" ngp/cngp");
                            LogUtil.i(SmartAlarmService.TAG, sb.toString());
                            SmartAlarmService.this.restrictFullScreenApps(status);
                            SmartAlarmService.this.dispatch((SignalMap) signals.get(Integer.valueOf(signalId)), signalId, status ? 10 : 0);
                            boolean unused2 = SmartAlarmService.lastNgpStatus = status;
                            return;
                        }
                        return;
                    } else if (signalId == 557917780) {
                        Integer[] signalVal = (Integer[]) value.getValue();
                        if (signalVal[0].intValue() == 3 && signalVal[1].intValue() == 2) {
                            r3 = 1;
                        }
                        int rcta_mapping = r3;
                        SmartAlarmService.this.dispatch((SignalMap) signals.get(Integer.valueOf(signalId)), signalId, rcta_mapping);
                        return;
                    } else if (signalId != 557917782) {
                        SmartAlarmService.this.dispatch((SignalMap) signals.get(Integer.valueOf(signalId)), signalId, ((Integer) value.getValue()).intValue());
                        return;
                    } else {
                        Integer[] signalVal2 = (Integer[]) value.getValue();
                        if (signalVal2[0].intValue() == 3 && (signalVal2[1].intValue() == 1 || signalVal2[1].intValue() == 2)) {
                            r3 = 1;
                        }
                        int ldw_mapping = r3;
                        SmartAlarmService.this.dispatch((SignalMap) signals.get(Integer.valueOf(signalId)), signalId, ldw_mapping);
                        return;
                    }
                }
                LogUtil.d(SmartAlarmService.TAG, "unhandle onScuEvent " + signalId);
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
        this.mCommonEventListener = new CommonMsgController.InternalCommonEventListener() { // from class: com.xiaopeng.xuiservice.smart.SmartAlarmService.6
            @Override // com.xiaopeng.xuiservice.carcontrol.CommonMsgController.InternalCommonEventListener
            public void onInternalCommonEvent(int eventType, int eventValue) {
                if (eventType == 3) {
                    SmartAlarmService.this.dispatch((SignalMap) signals.get(12345681), 12345681, eventValue);
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
        this.mCarScuManager = getCarManager("xp_scu");
        this.mCarBcmManager = getCarManager("xp_bcm");
        this.mCarVcuManager = getCarManager("xp_vcu");
        try {
            boolean z = true;
            if (this.mCarMcuManager.getIgStatusFromMcu() != 1) {
                z = false;
            }
            hasIgOn = z;
            mModeIndex = this.mCarScuManager.getScuModeIndex();
        } catch (Exception e) {
            LogUtil.e(TAG, "onCarManagerInited failed, " + e);
        }
        LogUtil.i(TAG, "onCarManagerInited, hasIgOn=" + hasIgOn + ", mModeIndex=" + mModeIndex);
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
        this.mSoundPlay = XpSoundPlayService.getInstance();
        if (this.mSoundPlay != null) {
            List<String> files = FileUtils.getAllFiles(SOUND_PATH, "wav");
            for (String file : files) {
                this.mSoundPlay.load(file);
            }
            this.mSoundPlayListener = new XpSoundPlayService.PlaySoundEventListener() { // from class: com.xiaopeng.xuiservice.smart.SmartAlarmService.7
                @Override // com.xiaopeng.xuiservice.capabilities.XpSoundPlayService.PlaySoundEventListener
                public void onPlaySoundEventCallBack(int event, int signal, int streamId) {
                    Message message = SmartAlarmService.this.mHandler.obtainMessage();
                    message.what = 8;
                    message.arg1 = event;
                    message.arg2 = signal;
                    message.obj = Integer.valueOf(streamId);
                    SmartAlarmService.this.mHandler.sendMessage(message);
                }
            };
            this.mSoundPlay.addPlaySoundEventListener(this.mSoundPlayListener);
        }
        this.mSpeechTts = XpSpeechTtsService.getInstance();
        if (this.mSpeechTts != null) {
            this.mSpeechTtsListener = new XpSpeechTtsService.SpeechTtsEventListener() { // from class: com.xiaopeng.xuiservice.smart.SmartAlarmService.8
                @Override // com.xiaopeng.xuiservice.capabilities.XpSpeechTtsService.SpeechTtsEventListener
                public void onSpeechTtsEventCallBack(int event, String ttsId) {
                    Message message = SmartAlarmService.this.mHandler.obtainMessage();
                    message.what = 9;
                    message.obj = ttsId;
                    message.arg1 = event;
                    SmartAlarmService.this.mHandler.sendMessage(message);
                }
            };
            this.mSpeechTts.addSpeechTtsEventListener(this.mSpeechTtsListener);
        }
        if (hasBindService) {
            mBindService = new BindXpuService();
        }
        if (this.mSoundPlay != null && this.mSpeechTts != null) {
            z = true;
        }
        hasInit = z;
        StringBuilder sb = new StringBuilder();
        sb.append("hasInit=");
        sb.append(hasInit);
        sb.append(", hasIgOn=");
        sb.append(hasIgOn);
        sb.append(", hasSimpleMode=");
        sb.append(hasSimpleMode);
        sb.append(", ngp protocol=");
        sb.append(hasBindService ? "bindmessage" : "someip");
        LogUtil.i(TAG, sb.toString());
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        LogUtil.i(TAG, "onRelease");
        unregisterObservier();
        removeCarListener();
        removeCommonEventListener();
        this.mHandlerThread.quit();
    }

    public SmartAlarmService(Context context) {
        super(context);
        this.mSoundPlay = null;
        this.mSoundPlayListener = null;
        this.mSpeechTts = null;
        this.mSpeechTtsListener = null;
        this.mXpuCallback = null;
        this.mMcuCallback = null;
        this.mVcuCallback = null;
        this.mVpmCallback = null;
        this.mScuCallback = null;
        this.mPackage = new NgpPackage();
        this.mToast = new ToastOsd();
        this.mLastSignal = new ArrayMap();
        this.mSigMapSound = new ArrayMap();
        this.mSigMapSpeech = new ArrayMap();
        this.mSigMapName = new ArrayMap();
        this.mBiMap = new ConcurrentHashMap();
        this.mSigSetSpeech = new HashSet();
        this.mSigSetOsd = new HashSet();
        this.mHandlerThread = null;
        this.mHandler = null;
        this.mSimpleMode = new ContentObserver(new XuiWorkHandler()) { // from class: com.xiaopeng.xuiservice.smart.SmartAlarmService.1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                boolean unused = SmartAlarmService.hasSimpleMode = SmartAlarmService.this.getSimpleMode();
                LogUtil.i(SmartAlarmService.TAG, "hasSimpleMode=" + SmartAlarmService.hasSimpleMode);
            }
        };
        this.mHandlerThread = new HandlerThread("SmartAlarmThread");
        this.mHandlerThread.start();
        this.mHandler = new Handler(this.mHandlerThread.getLooper()) { // from class: com.xiaopeng.xuiservice.smart.SmartAlarmService.9
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                BiAlarm uploader;
                BiAlarm uploader2;
                switch (msg.what) {
                    case 1:
                        PlaySound playSound = (PlaySound) msg.obj;
                        Sound sound = playSound.sound;
                        int position = sound.position != 0 ? sound.position : 5;
                        int priority = 7 - sound.priority;
                        if (SmartAlarmService.this.mBiMap.containsKey(Long.valueOf(playSound.ts))) {
                            uploader = (BiAlarm) SmartAlarmService.this.mBiMap.get(Long.valueOf(playSound.ts));
                        } else {
                            uploader = new BiAlarm(playSound.signal, playSound.ts);
                        }
                        uploader.addSound(sound.source, sound.priority, sound.loop == -1 ? "periodic" : SceneSwitchStatisticsBean.NAME_ONESHOT);
                        SmartAlarmService.this.mBiMap.put(Long.valueOf(playSound.ts), uploader);
                        LogUtil.i(SmartAlarmService.TAG, "play sound, signal=" + playSound.signal + " (" + sound.source + ", position=" + position + ", priority=" + sound.priority + ", loop=" + sound.loop + ")");
                        XpSoundPlayService xpSoundPlayService = SmartAlarmService.this.mSoundPlay;
                        int i = playSound.signal;
                        StringBuilder sb = new StringBuilder();
                        sb.append(SmartAlarmService.SOUND_PATH);
                        sb.append(sound.source);
                        xpSoundPlayService.play(i, sb.toString(), sound.leftVolume, sound.rightVolume, position, priority, sound.loop);
                        return;
                    case 2:
                        int signal = ((Integer) msg.obj).intValue();
                        LogUtil.i(SmartAlarmService.TAG, "stop sound, signal=" + signal);
                        SmartAlarmService.this.mSoundPlay.stop(signal);
                        return;
                    case 3:
                        int type = ((Integer) msg.obj).intValue();
                        SmartAlarmService.this.mToast.onVisibleView(type);
                        return;
                    case 4:
                        SmartAlarmService.this.mToast.offVisibleView();
                        return;
                    case 5:
                    default:
                        SpeechTts speech = (SpeechTts) msg.obj;
                        int priority2 = speech.getPriority();
                        String ttsId = null;
                        if (msg.what == speech.signal) {
                            LogUtil.d(SmartAlarmService.TAG, "handle delay message " + msg.what);
                        } else if (msg.what != 5) {
                            return;
                        }
                        if (speech.getOnlyOnce()) {
                            SmartAlarmService.this.mSigSetSpeech.add(Integer.valueOf(speech.signal));
                        }
                        if (priority2 == 0) {
                            ttsId = SmartAlarmService.this.mSpeechTts.speakByInstant(speech.getContent(), speech.getShutUp());
                        } else if (priority2 == 1) {
                            ttsId = SmartAlarmService.this.mSpeechTts.speakByUrgent(speech.getContent(), speech.getShutUp());
                        } else {
                            LogUtil.w(SmartAlarmService.TAG, "Invalid tts priority!");
                        }
                        long ts = msg.what == 5 ? speech.ts : System.currentTimeMillis();
                        if (SmartAlarmService.this.mBiMap.containsKey(Long.valueOf(ts))) {
                            uploader2 = (BiAlarm) SmartAlarmService.this.mBiMap.get(Long.valueOf(ts));
                        } else {
                            uploader2 = new BiAlarm(speech.signal, ts);
                        }
                        uploader2.addTts(speech.getContent(), speech.getPriority());
                        if (ttsId != null) {
                            uploader2.ttsId = ttsId;
                            SmartAlarmService.this.mBiMap.put(Long.valueOf(ts), uploader2);
                        } else {
                            uploader2.addTtsStartTime();
                            uploader2.submit(0, "failed");
                            if (SmartAlarmService.this.mBiMap.containsKey(Long.valueOf(ts)) && uploader2.isCompleted()) {
                                SmartAlarmService.this.mBiMap.remove(Long.valueOf(ts));
                            }
                        }
                        LogUtil.i(SmartAlarmService.TAG, "speech: " + speech.getContent() + ", ttsId=" + ttsId + ", priority=" + priority2 + ", hasShutUp=" + speech.getShutUp());
                        if (speech.getLoop()) {
                            SmartAlarmService.this.handleSpeechTts(msg.what, speech);
                            return;
                        }
                        return;
                    case 6:
                        if (SmartAlarmService.mBindService != null && !SmartAlarmService.mBindService.bindService()) {
                            SmartAlarmService.this.handleBindService(true, 5000L);
                            return;
                        }
                        return;
                    case 7:
                        if (SmartAlarmService.mBindService != null) {
                            SmartAlarmService.mBindService.unbindService();
                            SmartAlarmService.this.handleBindService(true, 800L);
                            return;
                        }
                        return;
                    case 8:
                        int event = msg.arg1;
                        int streamId = ((Integer) msg.obj).intValue();
                        int signal2 = msg.arg2;
                        LogUtil.i(SmartAlarmService.TAG, "onPlaySound " + SmartAlarmService.this.parseEvent(event) + ", signal=" + signal2 + ", streamId=" + streamId);
                        if (event != 4) {
                            for (Long l : SmartAlarmService.this.mBiMap.keySet()) {
                                long key = l.longValue();
                                BiAlarm uploader3 = (BiAlarm) SmartAlarmService.this.mBiMap.get(Long.valueOf(key));
                                if (uploader3 != null && uploader3.hasSound) {
                                    boolean hasStream = streamId == uploader3.streamId;
                                    boolean hasSignal = signal2 == uploader3.signal;
                                    if (event == 0) {
                                        if (hasSignal) {
                                            uploader3.streamId = streamId;
                                        } else if (uploader3.streamId != 0) {
                                            uploader3.submit(1, "stop");
                                        }
                                    } else if (hasStream || hasSignal) {
                                        uploader3.submit(1, SmartAlarmService.this.parseEvent(event));
                                    }
                                    if (uploader3.isCompleted()) {
                                        SmartAlarmService.this.mBiMap.remove(Long.valueOf(key));
                                    }
                                }
                            }
                            return;
                        }
                        long ts2 = System.currentTimeMillis();
                        BiAlarm uploader4 = new BiAlarm(signal2, ts2);
                        uploader4.addSound("", 0, "replay");
                        uploader4.streamId = streamId;
                        SmartAlarmService.this.mBiMap.put(Long.valueOf(ts2), uploader4);
                        return;
                    case 9:
                        String ttsId2 = (String) msg.obj;
                        int event2 = msg.arg1;
                        LogUtil.i(SmartAlarmService.TAG, "onSpeechTts " + SmartAlarmService.this.parseEvent(event2) + ", ttsId=" + ttsId2);
                        for (Long l2 : SmartAlarmService.this.mBiMap.keySet()) {
                            long key2 = l2.longValue();
                            BiAlarm uploader5 = (BiAlarm) SmartAlarmService.this.mBiMap.get(Long.valueOf(key2));
                            if (uploader5 != null && uploader5.hasTts && ttsId2.equals(uploader5.ttsId)) {
                                if (event2 != 0) {
                                    uploader5.submit(0, SmartAlarmService.this.parseEvent(event2));
                                    if (uploader5.isCompleted()) {
                                        SmartAlarmService.this.mBiMap.remove(Long.valueOf(key2));
                                    }
                                } else {
                                    uploader5.addTtsStartTime();
                                }
                            }
                        }
                        return;
                }
            }
        };
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final SmartAlarmService sService = new SmartAlarmService(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    public static SmartAlarmService getInstance() {
        return InstanceHolder.sService;
    }

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService
    public void dump(PrintWriter pw, String[] args) {
        pw.println("*dump-SmartAlarmService");
        if (args != null && args.length == 2) {
            try {
                int value = Integer.parseInt(args[1]);
                if (mBindService != null) {
                    pw.println("*test ngp_signal" + args[0] + "=" + value);
                    mBindService.test(args[0], value);
                }
            } catch (Exception e) {
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected boolean needConnectXUI() {
        return true;
    }
}
