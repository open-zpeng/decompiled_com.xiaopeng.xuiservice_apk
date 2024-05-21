package com.xiaopeng.xuiservice.capabilities.llu;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.bcm.CarBcmManager;
import android.car.hardware.llu.CarLluManager;
import android.car.hardware.llu.LluScriptParameter;
import android.car.hardware.vcu.CarVcuManager;
import android.content.Context;
import android.media.AudioConfig.AudioConfig;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Pair;
import com.alipay.mobile.aromeservice.RequestParams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.xiaopeng.biutil.BiLog;
import com.xiaopeng.biutil.BiLogFactory;
import com.xiaopeng.speech.protocol.bean.recommend.RecommendBean;
import com.xiaopeng.speech.protocol.event.OOBEEvent;
import com.xiaopeng.xuimanager.themeoperation.ThemeOperationData;
import com.xiaopeng.xuimanager.themeoperation.ThemeOperationListener;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.carcontrol.IServiceConn;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import com.xiaopeng.xuiservice.innerutils.BiLogTransmit;
import com.xiaopeng.xuiservice.innerutils.DataLogUtils;
import com.xiaopeng.xuiservice.themeoperation.ThemeOperationService;
import com.xiaopeng.xuiservice.utils.CarControlUtils;
import com.xiaopeng.xuiservice.utils.CommonUtils;
import com.xiaopeng.xuiservice.utils.FileUtils;
import com.xiaopeng.xuiservice.utils.PackageUtils;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/* loaded from: classes5.dex */
public class XpLightlanuageService {
    private static final String DEFAULT_THEME = "default";
    private static final int EVENT_ERROR = 4;
    private static final int EVENT_FINISH = 3;
    private static final int EVENT_REPEAT = 5;
    private static final int EVENT_START = 1;
    private static final int EVENT_STOP = 2;
    private static final int MCU_AC_CHARGE = 6;
    private static final int MCU_AWAKE = 2;
    private static final int MCU_CLOSE = 0;
    private static final int MCU_DC_CHARGE = 7;
    private static final int MCU_FINDCAR = 1;
    private static final int MCU_MODE_A = 1;
    private static final int MCU_MODE_B = 2;
    private static final int MCU_MODE_C = 3;
    private static final int MCU_SHOWOFF = 10;
    private static final int MCU_SLEEP = 5;
    private static final int MCU_TAKEPHOTO = 9;
    private static final int MSG_FINISH = 3;
    private static final int MSG_PLAY = 1;
    private static final int MSG_STOP = 2;
    private static final String PROP_DANCING = "persist.sys.light.dancing";
    private static final String PROP_EXPAND_CTRL = "persist.sys.xiaopeng.xui.ExpandControl";
    private static final String TAG = "XpLightlanuageService";
    private static final int TICK_PERIOD = 10;
    private static final boolean hasDictType;
    private static AudioConfig mAudioConfig;
    private static boolean mAudioFocus;
    private static AudioManager.OnAudioFocusChangeListener mAudioFocusListener;
    private static AudioManager mAudioManager;
    private static BiLog mBilog;
    private static int mBrakeLight;
    private static CarClientManager mCar;
    private static ScheduledThreadPoolExecutor mExecutor;
    private static ScheduledFuture<?> mFuture;
    private static int mHighBeamLight;
    private static int mLeftSdc;
    private static int mLength;
    private static Stack<LightEffect> mLight;
    private static Map<String, LightEffect> mLightEffectMap;
    private static List<LightEffectListener> mListener;
    private static int mMirrorView;
    private static MediaPlayer mPlayer;
    private static RhythmEffect mRhythmEffect;
    private static PlayEffect mRhythmPlay;
    private static int mRightSdc;
    private static String mTheme;
    private static ThemeOperationListener mThemeListener;
    private static Map<String, Theme> mThemeMap;
    private static ThemeOperationService mThemeService;
    private static Uploader mUploader;
    private static int mWindowPos;
    private static XpLightlanuageService sService;
    private AvmListener mAvmListener;
    private Handler mHandler;
    private HandlerThread mHandlerThread;

    /* loaded from: classes5.dex */
    public interface AvmListener {
        void onAvmEvent(int i, int i2);
    }

    /* loaded from: classes5.dex */
    public interface LightEffectListener {
        void onErrorEffect(String str, int i);

        void onFinishEffect(String str, String str2);

        void onStartEffect(String str, String str2);

        void onStopEffect(String str, String str2);

        void onUpgradeEffect(int i, int i2);
    }

    static {
        hasDictType = XUIConfig.getLluType() == 0;
        mHighBeamLight = -1;
        mBrakeLight = -1;
        mLeftSdc = -1;
        mRightSdc = -1;
        mWindowPos = -1;
        mMirrorView = -1;
        mLength = 0;
        mExecutor = new ScheduledThreadPoolExecutor(1);
        mFuture = null;
        mCar = CarClientManager.getInstance();
        mThemeService = ThemeOperationService.getInstance();
        mThemeMap = new ArrayMap();
        mTheme = DEFAULT_THEME;
        mAudioFocus = false;
        mLightEffectMap = new ArrayMap();
        mLight = new Stack<>();
        mRhythmPlay = null;
        mRhythmEffect = null;
        mUploader = null;
        mPlayer = null;
        mListener = new ArrayList();
        sService = null;
        mBilog = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class Packet {
        public Integer[] data;
        public int loop;
        public int period;
        public int retain;
        public String[] sdata;

        private Packet() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class LightEffect {
        @SerializedName(alternate = {"special_duration"}, value = "duration")
        public int duration;
        @SerializedName(alternate = {"front_effect"}, value = "front")
        public Packet front;
        @SerializedName(alternate = {"effect_id"}, value = "id")
        public int id;
        @SerializedName(alternate = {"effect_name"}, value = "name")
        public String name;
        @SerializedName(alternate = {"rear_effect"}, value = "rear")
        public Packet rear;
        @SerializedName(alternate = {"notResetFlag"}, value = "reset")
        public int reset;
        @SerializedName(alternate = {"special_mode"}, value = "special")
        public int special;
        @SerializedName(alternate = {"effect_type"}, value = SpeechConstants.KEY_COMMAND_TYPE)
        public String type;

        private LightEffect() {
        }

        public LightEffect readFromJson(String file) {
            BufferedReader reader = null;
            try {
                try {
                    reader = new BufferedReader(new FileReader(file));
                    Gson gson = new GsonBuilder().create();
                    LightEffect lightEffect = (LightEffect) gson.fromJson((Reader) reader, (Class<Object>) LightEffect.class);
                    try {
                        reader.close();
                    } catch (Exception e) {
                        LogUtil.e(XpLightlanuageService.TAG, "close " + file + " Exception: " + e);
                    }
                    return lightEffect;
                } catch (Throwable th) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e2) {
                            LogUtil.e(XpLightlanuageService.TAG, "close " + file + " Exception: " + e2);
                        }
                    }
                    throw th;
                }
            } catch (Exception e3) {
                LogUtil.e(XpLightlanuageService.TAG, "readFromJson Exception: " + e3);
                if (reader != null) {
                    try {
                        reader.close();
                        return null;
                    } catch (Exception e4) {
                        LogUtil.e(XpLightlanuageService.TAG, "close " + file + " Exception: " + e4);
                        return null;
                    }
                }
                return null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class PlayEffect {
        public int count;
        public LightEffect light;
        public String owner;
        public float ratio;
        public boolean repeat;
        public boolean rhythm;
        public String source;

        public PlayEffect(LightEffect light, String source, int count, boolean repeat, String owner) {
            this.source = null;
            this.count = 1;
            this.ratio = 0.0f;
            this.rhythm = false;
            this.repeat = false;
            this.owner = null;
            this.light = light;
            this.source = source;
            this.count = count;
            this.repeat = repeat;
            this.owner = owner;
        }

        public PlayEffect(LightEffect light, String owner) {
            this.source = null;
            this.count = 1;
            this.ratio = 0.0f;
            this.rhythm = false;
            this.repeat = false;
            this.owner = null;
            this.light = light;
            this.rhythm = true;
            this.repeat = true;
            this.owner = owner;
        }

        public PlayEffect setRatio(float ratio) {
            this.ratio = ratio;
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class Effect<T> {
        private int count;
        public List<T> list;
        public int period;
        public boolean retain;

        public Effect(Packet pack, T[] array) {
            this.retain = pack.retain == 1;
            this.period = pack.period >= 10 ? pack.period / 10 : 1;
            this.list = Arrays.asList(array);
            this.count = pack.loop;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean hasPeriod(int tick) {
            return tick % this.period == 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public T get(int index) {
            List<T> list = this.list;
            return list.get(index % list.size());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int parse(float ratio) {
            return Math.round((this.list.size() - 1) * ratio) % this.list.size();
        }

        public boolean hasFinish(int tick) {
            return this.count >= 0 && tick >= (this.list.size() * this.count) * this.period;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class RhythmEffect {
        private Effect front;
        private String name;
        private Effect rear;
        private volatile int frontIndex = 0;
        private volatile int rearIndex = 0;

        public RhythmEffect(LightEffect light) {
            this.name = light.name;
            Effect effect = null;
            if (XpLightlanuageService.hasDictType) {
                this.front = (light.front == null || light.front.data == null) ? null : new Effect(light.front, light.front.data);
                if (light.rear != null && light.rear.data != null) {
                    effect = new Effect(light.rear, light.rear.data);
                }
                this.rear = effect;
                return;
            }
            this.front = (light.front == null || light.front.sdata == null) ? null : new Effect(light.front, light.front.sdata);
            if (light.rear != null && light.rear.sdata != null) {
                effect = new Effect(light.rear, light.rear.sdata);
            }
            this.rear = effect;
        }

        public void seek(float ratio) {
            Effect effect = this.front;
            int i = 0;
            this.frontIndex = (effect == null || effect.list.size() <= 0) ? 0 : this.front.parse(ratio);
            Effect effect2 = this.rear;
            if (effect2 != null && effect2.list.size() > 0) {
                i = this.rear.parse(ratio);
            }
            this.rearIndex = i;
        }

        public Pair get() {
            String str;
            int i;
            Effect effect = this.front;
            Object obj = "";
            int i2 = 0;
            if (effect == null || effect.list.size() <= 0) {
                str = XpLightlanuageService.hasDictType ? 0 : "";
            } else {
                Effect effect2 = this.front;
                if (this.frontIndex > 0) {
                    i = this.frontIndex;
                    this.frontIndex = i - 1;
                } else {
                    i = 0;
                }
                str = effect2.get(i);
            }
            Effect effect3 = this.rear;
            if (effect3 == null || effect3.list.size() <= 0) {
                if (XpLightlanuageService.hasDictType) {
                    obj = 0;
                }
            } else {
                Effect effect4 = this.rear;
                if (this.rearIndex > 0) {
                    i2 = this.rearIndex;
                    this.rearIndex = i2 - 1;
                }
                obj = effect4.get(i2);
            }
            return new Pair(str, obj);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class EffectTask implements Runnable {
        private int count;
        private Effect front;
        private LightEffect light;
        private Pair prev;
        private Effect rear;
        private String source;
        private int tick = 0;

        public EffectTask(LightEffect light, String source, int count) {
            this.prev = XpLightlanuageService.hasDictType ? new Pair(0, 0) : new Pair("", "");
            this.light = light;
            this.source = source;
            this.count = count;
            Effect effect = null;
            if (XpLightlanuageService.hasDictType) {
                this.front = (light.front == null || light.front.data == null) ? null : new Effect(light.front, light.front.data);
                if (light.rear != null && light.rear.data != null) {
                    effect = new Effect(light.rear, light.rear.data);
                }
                this.rear = effect;
            } else {
                this.front = (light.front == null || light.front.sdata == null) ? null : new Effect(light.front, light.front.sdata);
                if (light.rear != null && light.rear.sdata != null) {
                    effect = new Effect(light.rear, light.rear.sdata);
                }
                this.rear = effect;
                Effect effect2 = this.front;
                String fdata = effect2 != null ? (String) effect2.get(0) : new String();
                Effect effect3 = this.rear;
                String rdata = effect3 != null ? (String) effect3.get(0) : new String();
                int unused = XpLightlanuageService.mLength = fdata.length() + rdata.length();
            }
            StringBuilder sb = new StringBuilder();
            sb.append("EffectTask ");
            sb.append(light.name);
            sb.append(", front size=");
            Effect effect4 = this.front;
            sb.append(effect4 != null ? effect4.list.size() : 0);
            sb.append(", rear size=");
            Effect effect5 = this.rear;
            sb.append(effect5 != null ? effect5.list.size() : 0);
            LogUtil.d(XpLightlanuageService.TAG, sb.toString());
        }

        private boolean hasFinish(int tick) {
            Effect effect;
            Effect effect2 = this.front;
            return (effect2 == null || effect2.hasFinish(tick)) && ((effect = this.rear) == null || effect.hasFinish(tick));
        }

        private Pair parseEffect(int tick) {
            Object obj;
            Object obj2;
            Effect effect = this.front;
            if (effect == null || effect.list.size() <= 0 || !this.front.hasPeriod(tick)) {
                obj = this.prev.first;
            } else {
                Effect effect2 = this.front;
                obj = effect2.get(tick / effect2.period);
            }
            Effect effect3 = this.rear;
            if (effect3 == null || effect3.list.size() <= 0 || !this.rear.hasPeriod(tick)) {
                obj2 = this.prev.second;
            } else {
                Effect effect4 = this.rear;
                obj2 = effect4.get(tick / effect4.period);
            }
            return new Pair(obj, obj2);
        }

        private boolean hasPeriod(int tick) {
            Effect effect;
            Effect effect2 = this.front;
            return (effect2 == null || effect2.hasPeriod(tick)) && ((effect = this.rear) == null || effect.hasPeriod(tick));
        }

        private boolean needRetain() {
            Effect effect;
            Effect effect2 = this.front;
            return (effect2 != null && effect2.retain) || ((effect = this.rear) != null && effect.retain);
        }

        private void showEffect(Pair cur) {
            if (!this.prev.equals(cur)) {
                LogUtil.v(XpLightlanuageService.TAG, "showEffect: " + cur.first + ", " + cur.second);
                XpLightlanuageService.this.setPrivateCtrl(true, cur);
                XpLightlanuageService.this.setLightCtrl(cur);
                XpLightlanuageService.this.setExpandCtrl(cur);
                this.prev = cur;
            }
        }

        private void doneEffect(boolean retain) {
            if (!retain) {
                XpLightlanuageService.this.setPrivateCtrl(false, XpLightlanuageService.hasDictType ? new Pair(0, 0) : new Pair("", ""));
            }
            XpLightlanuageService.mFuture.cancel(false);
            int i = this.count;
            if (i == 0) {
                XpLightlanuageService.this.handleFinishEffect(0L);
                return;
            }
            XpLightlanuageService xpLightlanuageService = XpLightlanuageService.this;
            xpLightlanuageService.handlePlayEffect(new PlayEffect(this.light, this.source, i, true, null), 2000L);
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                if (!hasFinish(this.tick)) {
                    showEffect(parseEffect(this.tick));
                } else if (hasPeriod(this.tick)) {
                    doneEffect(needRetain());
                }
            } catch (Exception e) {
                LogUtil.e(XpLightlanuageService.TAG, "run exception " + e);
            }
            int i = this.tick + 1;
            this.tick = i;
            if (i < 0) {
                this.tick = 0;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class Uploader {
        private Packet front;
        public int frontIndex;
        public int frontLeft;
        public int frontSize;
        public int id;
        public int mode;
        public int name;
        private Packet rear;
        public int rearIndex;
        public int rearLeft;
        public int rearSize;

        public Uploader(LightEffect light) {
            this.name = light.id & 15;
            this.mode = light.id >> 16;
            this.id = light.id;
            this.front = light.front;
            this.rear = light.rear;
            Packet packet = this.front;
            int length = (packet == null || packet.data == null) ? 0 : this.front.data.length;
            this.frontLeft = length;
            this.frontSize = length;
            Packet packet2 = this.rear;
            int length2 = (packet2 == null || packet2.data == null) ? 0 : this.rear.data.length;
            this.rearLeft = length2;
            this.rearSize = length2;
            this.rearIndex = 0;
            this.frontIndex = 0;
        }

        public LluScriptParameter build() {
            LluScriptParameter.Builder effectName = new LluScriptParameter.Builder().effectName(this.id);
            Packet packet = this.front;
            LluScriptParameter.Builder frontEffectPeriod = effectName.frontEffectPeriod(packet != null ? packet.period : 0);
            Packet packet2 = this.front;
            LluScriptParameter.Builder frontEffectLoop = frontEffectPeriod.frontEffectLoop(packet2 != null ? packet2.loop : 0);
            Packet packet3 = this.front;
            LluScriptParameter.Builder frontEffectDataTotalLength = frontEffectLoop.frontEffectRetain(packet3 != null ? packet3.retain : 0).frontEffectDataTotalLength(this.frontSize);
            Packet packet4 = this.rear;
            LluScriptParameter.Builder rearEffectPeriod = frontEffectDataTotalLength.rearEffectPeriod(packet4 != null ? packet4.period : 0);
            Packet packet5 = this.rear;
            LluScriptParameter.Builder rearEffectLoop = rearEffectPeriod.rearEffectLoop(packet5 != null ? packet5.loop : 0);
            Packet packet6 = this.rear;
            return rearEffectLoop.rearEffectRetain(packet6 != null ? packet6.retain : 0).rearEffectDataTotalLength(this.rearSize).build();
        }

        public Integer[] get() {
            int size = this.frontLeft;
            if (size > 0) {
                int start = this.frontSize - size;
                if (size >= 60) {
                    size = 60;
                }
                this.frontLeft -= size;
                this.frontIndex++;
                StringBuilder sb = new StringBuilder();
                sb.append("get frontData[");
                sb.append(start);
                sb.append(", ");
                sb.append((start + size) - 1);
                sb.append("]");
                LogUtil.v(XpLightlanuageService.TAG, sb.toString());
                Integer[] data = new Integer[size];
                System.arraycopy(this.front.data, start, data, 0, size);
                return data;
            }
            int size2 = this.rearLeft;
            if (size2 <= 0) {
                return null;
            }
            int start2 = this.rearSize - size2;
            if (size2 >= 60) {
                size2 = 60;
            }
            this.rearLeft -= size2;
            this.rearIndex++;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("get rearData[");
            sb2.append(start2);
            sb2.append(", ");
            sb2.append((start2 + size2) - 1);
            sb2.append("]");
            LogUtil.v(XpLightlanuageService.TAG, sb2.toString());
            Integer[] data2 = new Integer[size2];
            System.arraycopy(this.rear.data, start2, data2, 0, size2);
            return data2;
        }

        public boolean available() {
            return this.frontLeft + this.rearLeft > 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class Theme {
        public String path;
        public String theme;
        public long ttl = SystemClock.elapsedRealtime();
        public Map<String, LightEffect> effectMap = new ArrayMap();

        public void updateTtl() {
            this.ttl = SystemClock.elapsedRealtime();
        }

        public boolean containsEffect(String name) {
            return this.effectMap.containsKey(name);
        }

        public LightEffect getEffect(String name) {
            return this.effectMap.get(name);
        }

        public Theme(String theme, String path) {
            List<String> files = FileUtils.getAllFiles(path, "json");
            for (String file : files) {
                LightEffect effect = new LightEffect().readFromJson(file);
                if (effect != null && !this.effectMap.containsKey(effect.name)) {
                    LogUtil.i(XpLightlanuageService.TAG, "loadThemeEffect" + file);
                    this.effectMap.put(effect.name, effect);
                }
            }
            this.theme = theme;
            this.path = path;
        }
    }

    private int parseTick(int data) {
        if (data != 0) {
            return data & 255;
        }
        return 65535;
    }

    private int parseType(int data) {
        if (data != 0) {
            return 255 & (data >> 8);
        }
        return 255;
    }

    private byte[] parseData(String fdata, String rdata) {
        String sdata;
        if (fdata.length() > 0 || rdata.length() > 0) {
            sdata = fdata + rdata;
        } else {
            sdata = (String) Stream.generate(new Supplier() { // from class: com.xiaopeng.xuiservice.capabilities.llu.-$$Lambda$XpLightlanuageService$xaXESdIZ_ZWONOBoOkGTY8mZk3A
                @Override // java.util.function.Supplier
                public final Object get() {
                    String valueOf;
                    valueOf = String.valueOf(0);
                    return valueOf;
                }
            }).limit(mLength).collect(Collectors.joining());
        }
        LogUtil.v(TAG, "parseData: " + sdata);
        return CommonUtils.hexString2Bytes(sdata);
    }

    private int parseLight(int data) {
        if (data != 0) {
            return (data >> 16) & 1;
        }
        return 0;
    }

    private int parseExpand(int data) {
        if (data != 0) {
            return (data >> 17) & 32767;
        }
        return 0;
    }

    private boolean hasSdcData(int data) {
        if (data == 1 || data == 2 || data == 3) {
            return true;
        }
        return false;
    }

    private void setLeftSdcAutoControl(int data) {
        try {
            if (hasSdcData(data) && data != mLeftSdc) {
                CarBcmManager bcm = mCar.getCarManager("xp_bcm");
                bcm.setLeftSdcAutoControl(data);
                mLeftSdc = data;
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "setLeftSdcAutoControl failed " + e);
        }
    }

    private void setRightSdcAutoControl(int data) {
        try {
            if (hasSdcData(data) && data != mRightSdc) {
                CarBcmManager bcm = mCar.getCarManager("xp_bcm");
                bcm.setRightSdcAutoControl(data);
                mRightSdc = data;
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "setRightSdcAutoControl failed " + e);
        }
    }

    private void setWindowsMovePositions(int data) {
        int pos = data == 1 ? 100 : 0;
        try {
            if (pos != mWindowPos) {
                CarBcmManager bcm = mCar.getCarManager("xp_bcm");
                bcm.setWindowsMovePositions(pos, pos, -1.0f, -1.0f);
                mWindowPos = pos;
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "setWindowsMovePositions failed " + e);
        }
    }

    private void setRearViewMirror(int data) {
        int type = data == 0 ? 0 : 1;
        try {
            if (data != mMirrorView) {
                CarBcmManager bcm = mCar.getCarManager("xp_bcm");
                bcm.setRearViewMirror(type);
                mMirrorView = data;
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "setRearViewMirror failed " + e);
        }
    }

    private void setDictExpandCtrl(int data) {
        setLeftSdcAutoControl(data & 3);
        setRightSdcAutoControl((data >> 2) & 3);
        setWindowsMovePositions((data >> 4) & 1);
        setRearViewMirror((data >> 5) & 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setExpandCtrl(Pair data) {
        if (hasDictType && hasExpand()) {
            int expand = parseExpand(((Integer) data.first).intValue());
            LogUtil.v(TAG, "setExpandCtrl, expand=" + expand);
            setDictExpandCtrl(expand);
        }
    }

    private void setHighBeamOnOff(int data) {
        int type = data == 0 ? 1 : 2;
        try {
            if (data != mHighBeamLight) {
                CarBcmManager bcm = mCar.getCarManager("xp_bcm");
                bcm.setHighBeamOnOff(type);
                mHighBeamLight = data;
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "setHighBeamOnOff failed " + e);
        }
    }

    private void setBrakeLightOnOff(int data) {
        if (data == 0) {
        }
        try {
            if (data != mBrakeLight) {
                CarVcuManager vcu = mCar.getCarManager("xp_vcu");
                vcu.setBrakeLightOnOff(data);
                mBrakeLight = data;
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "setBrakeLightOnOff failed " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLightCtrl(Pair data) {
        if (hasDictType) {
            int fLight = parseLight(((Integer) data.first).intValue());
            int rLight = parseLight(((Integer) data.second).intValue());
            LogUtil.v(TAG, "setLightCtrl, fLight=" + fLight + ", rLight=" + rLight);
            setHighBeamOnOff(fLight);
            setBrakeLightOnOff(rLight);
        }
    }

    private void setDictPrivateCtrl(boolean start, int fType, int fTick, int rType, int rTick) {
        try {
            LogUtil.v(TAG, "setDictPrivateCtrl, start=" + start + ", fType=" + fType + ", fTick=" + fTick + ", rType=" + rType + ", rTick=" + rTick);
            CarLluManager llu = mCar.getCarManager("xp_llu");
            llu.setLluPrivateCtrl(start, fType, fTick, rType, rTick);
        } catch (Exception e) {
            LogUtil.e(TAG, "setDictPrivateCtrl Exception: " + e);
        }
    }

    private void setInfPrivateCtrl(byte[] data) {
        try {
            LogUtil.v(TAG, "setInfPrivateCtrl, data=" + Arrays.toString(data));
            CarLluManager llu = mCar.getCarManager("xp_llu");
            llu.setMcuLLuSelfControlData(data);
        } catch (Exception e) {
            LogUtil.e(TAG, "setInfPrivateCtrl Exception: " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPrivateCtrl(boolean start, Pair data) {
        if (hasDictType) {
            setDictPrivateCtrl(start, parseType(((Integer) data.first).intValue()), parseTick(((Integer) data.first).intValue()), parseType(((Integer) data.second).intValue()), parseTick(((Integer) data.second).intValue()));
        } else {
            setInfPrivateCtrl(parseData((String) data.first, (String) data.second));
        }
    }

    private ThemeOperationData getCurrentTheme() {
        try {
            return mThemeService.getCurrentTheme();
        } catch (Exception e) {
            LogUtil.e(TAG, "getCurrentTheme failed, " + e);
            return null;
        }
    }

    private void handleBiLog(int event, String name, String owner) {
        if (event == 1) {
            mBilog = BiLogFactory.create(DataLogUtils.XUI_PID, DataLogUtils.LIGHTLANG_BID);
            mBilog.push("name", name);
            mBilog.push("source", !TextUtils.isEmpty(owner) ? owner : "unknown");
            mBilog.push(RequestParams.REQUEST_KEY_START_TIME, String.valueOf(System.currentTimeMillis()));
            return;
        }
        BiLog biLog = mBilog;
        if (biLog != null && event != 5) {
            biLog.push("endTime", String.valueOf(System.currentTimeMillis()));
            mBilog.push(RecommendBean.SHOW_TIME_RESULT, event == 4 ? "-1" : "0");
            LogUtil.d(TAG, "handleBiLog: " + mBilog.getString());
            BiLogTransmit.getInstance().submit(mBilog);
            mBilog = null;
        }
    }

    private void showStatusCallback(int event, String name, String type, int errCode, String owner) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(type)) {
            return;
        }
        handleBiLog(event, name, owner);
        if (!mListener.isEmpty()) {
            synchronized (mListener) {
                String baseName = parseBaseEffect(name);
                for (LightEffectListener listener : mListener) {
                    if (event != 1) {
                        if (event == 2) {
                            listener.onStopEffect(baseName, type);
                        } else if (event == 3) {
                            listener.onFinishEffect(baseName, type);
                        } else if (event == 4) {
                            listener.onErrorEffect(baseName, errCode);
                        } else if (event != 5) {
                            LogUtil.w(TAG, "showStatusCallback, invalid event:" + event);
                        }
                    }
                    listener.onStartEffect(baseName, type);
                }
            }
        }
    }

    private void removeAllPendingMessages(Handler handler) {
        handler.removeMessages(1);
        handler.removeMessages(2);
        handler.removeMessages(3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean handlePlayEffect(PlayEffect play, long delay) {
        if (play != null && play.light != null && play.count != 0) {
            Message message = this.mHandler.obtainMessage();
            message.what = 1;
            message.obj = play;
            LogUtil.d(TAG, "handlePlayEffect after " + delay + "ms");
            removeAllPendingMessages(this.mHandler);
            this.mHandler.sendMessageDelayed(message, delay);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStopEffect(String name, boolean restore, long delay) {
        Message message = this.mHandler.obtainMessage();
        message.what = 2;
        message.obj = new Pair(name, Boolean.valueOf(restore));
        LogUtil.d(TAG, "handleStopEffect after " + delay + "ms, restore=" + restore);
        removeAllPendingMessages(this.mHandler);
        this.mHandler.sendMessageDelayed(message, delay);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFinishEffect(long delay) {
        Message message = this.mHandler.obtainMessage();
        message.what = 3;
        LogUtil.d(TAG, "handleFinishEffect after " + delay + "ms");
        removeAllPendingMessages(this.mHandler);
        this.mHandler.sendMessageDelayed(message, delay);
    }

    private String parseThemeEffect(String name) {
        return mTheme + "-" + name;
    }

    private String parseBaseEffect(String name) {
        return name.substring(name.lastIndexOf("-") + 1);
    }

    private synchronized LightEffect getLightEffectByName(String name) {
        if (mThemeMap.containsKey(mTheme) && mThemeMap.get(mTheme).containsEffect(parseThemeEffect(name))) {
            return mThemeMap.get(mTheme).getEffect(parseThemeEffect(name));
        } else if (mLightEffectMap.containsKey(name)) {
            return mLightEffectMap.get(name);
        } else {
            LogUtil.e(TAG, "getLightEffectByName failed, name=" + name);
            return null;
        }
    }

    private synchronized LightEffect getLightEffectById(int id) {
        for (Map.Entry<String, LightEffect> entry : mLightEffectMap.entrySet()) {
            LightEffect light = entry.getValue();
            if (light.id == id) {
                return light;
            }
        }
        return null;
    }

    private void setLluFindCarSwitch(boolean active) {
        try {
            CarLluManager llu = mCar.getCarManager("xp_llu");
            if (active) {
                llu.setLluFindCarSwitch(1);
                llu.setLluFindCarSwitch(2);
            } else {
                llu.setLluFindCarSwitch(0);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "setLluFindCarSwitch Exception: " + e);
        }
    }

    private void setMcuLluWakeWaitSwitch(boolean active) {
        try {
            CarLluManager llu = mCar.getCarManager("xp_llu");
            if (active) {
                llu.setMcuLluWakeWaitSwitch(1);
                llu.setMcuLluWakeWaitSwitch(2);
            } else {
                llu.setMcuLluWakeWaitSwitch(0);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "setMcuLluWakeWaitSwitch Exception: " + e);
        }
    }

    private void setMcuLluChargingSwitch(boolean active, int type) {
        try {
            CarLluManager llu = mCar.getCarManager("xp_llu");
            if (active) {
                llu.setMcuLluChargingSwitch(1);
                llu.setMcuLluChargingSwitch(type);
            } else {
                llu.setMcuLluChargingSwitch(0);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "setMcuLluWakeWaitSwitch " + type + " Exception: " + e);
        }
    }

    private void setMcuLluAcChargingSwitch(boolean active) {
        setMcuLluChargingSwitch(active, 2);
    }

    private void setMcuLluDcChargingSwitch(boolean active) {
        setMcuLluChargingSwitch(active, 3);
    }

    private void setMcuLluSleepSwitch(boolean active) {
        try {
            CarLluManager llu = mCar.getCarManager("xp_llu");
            if (active) {
                llu.setMcuLluSleepSwitch(1);
                llu.setMcuLluSleepSwitch(2);
            } else {
                llu.setMcuLluSleepSwitch(0);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "setMcuLluSleepSwitch Exception: " + e);
        }
    }

    private void setMcuLluPhotoSwitch(boolean active) {
        try {
            CarLluManager llu = mCar.getCarManager("xp_llu");
            if (active) {
                llu.setMcuLluPhotoSwitch(1);
                llu.setMcuLluPhotoSwitch(2);
            } else {
                llu.setMcuLluPhotoSwitch(0);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "setMcuLluPhotoSwitch Exception: " + e);
        }
    }

    private void setLluScriptStRequest(int name, int mode) {
        if (mUploader == null) {
            int id = (mode << 16) | (name & 15);
            LightEffect light = getLightEffectById(id);
            if (light != null) {
                mUploader = new Uploader(light);
                try {
                    CarLluManager llu = mCar.getCarManager("xp_llu");
                    llu.setLluScriptStRequest(1);
                } catch (Exception e) {
                    LogUtil.e(TAG, "setLluScriptStRequest failed " + e);
                }
            }
        }
    }

    private void setActiveMode(boolean active) {
        try {
            CarLluManager llu = mCar.getCarManager("xp_llu");
            int mode = 1;
            if (hasDictType) {
                if (!active) {
                    mode = 0;
                }
                llu.setLluSelfActive(mode);
            } else {
                if (!active) {
                    mode = 0;
                }
                llu.setMcuLluModeCtrl(mode, mode, mode, mode, mode, mode);
            }
            Thread.sleep(30L);
        } catch (Exception e) {
            LogUtil.e(TAG, "setActiveMode Exception: " + e);
        }
    }

    private void activePrivateCtrl() {
        try {
            CarLluManager llu = mCar.getCarManager("xp_llu");
            llu.activateAndroidLluControl();
            Thread.sleep(20L);
            if (hasDictType) {
                setActiveMode(false);
            }
            setActiveMode(true);
        } catch (Exception e) {
            LogUtil.e(TAG, "activePrivateCtrl Exception: " + e);
        }
    }

    private void deactivePrivateCtrl() {
        try {
            CarLluManager llu = mCar.getCarManager("xp_llu");
            llu.deactivateAndroidLluControl();
            Thread.sleep(20L);
            setActiveMode(false);
        } catch (Exception e) {
            LogUtil.e(TAG, "deactivePrivateCtrl Exception: " + e);
        }
    }

    private void setDayLightMode(int mode) {
        try {
            LogUtil.d(TAG, "setDayLightMode " + mode);
            CarBcmManager bcm = mCar.getCarManager("xp_bcm");
            bcm.setDayLightMode(mode);
        } catch (Exception e) {
            LogUtil.e(TAG, "setDayLightMode Exception: " + e);
        }
    }

    private void turnoffHeadLight() {
        CarControlUtils.getInstance().setHeadLight(false);
    }

    private void restoreHeadLight() {
        CarControlUtils.getInstance().setHeadLight(true);
    }

    private void resetPrivateCtrl() {
        setPrivateCtrl(false, hasDictType ? new Pair(0, 0) : new Pair("", ""));
        activePrivateCtrl();
    }

    private void stopPrivateCtrl() {
        setPrivateCtrl(false, hasDictType ? new Pair(0, 0) : new Pair("", ""));
        deactivePrivateCtrl();
    }

    private boolean needResetPrivateCtrl(int value) {
        return value == 0;
    }

    private boolean needPlaySpecialMode(int value) {
        return value != 0;
    }

    private void runEffectTask(EffectTask task) {
        if (task == null) {
            return;
        }
        LogUtil.i(TAG, "runEffectTask");
        ScheduledFuture<?> scheduledFuture = mFuture;
        if (scheduledFuture != null && !scheduledFuture.isDone()) {
            mFuture.cancel(true);
        }
        mFuture = mExecutor.scheduleAtFixedRate(task, 0L, 10L, TimeUnit.MILLISECONDS);
    }

    private void scheduleEffectTask(final EffectTask task) {
        if (task.source == null) {
            runEffectTask(task);
            return;
        }
        try {
            if (mPlayer != null) {
                LogUtil.i(TAG, "replayDanceMusic ...");
                if (mPlayer.isPlaying()) {
                    mPlayer.pause();
                    mPlayer.seekTo(0);
                }
                mPlayer.start();
                runEffectTask(task);
                return;
            }
            MediaPlayer player = new MediaPlayer();
            player.setAudioStreamType(3);
            player.setDataSource(task.source);
            player.setOnErrorListener(new MediaPlayer.OnErrorListener() { // from class: com.xiaopeng.xuiservice.capabilities.llu.-$$Lambda$XpLightlanuageService$DGzUBVZJZum9r8u7KE2oR6Zw1lI
                @Override // android.media.MediaPlayer.OnErrorListener
                public final boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                    return XpLightlanuageService.this.lambda$scheduleEffectTask$1$XpLightlanuageService(task, mediaPlayer, i, i2);
                }
            });
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageService.1
                @Override // android.media.MediaPlayer.OnCompletionListener
                public void onCompletion(MediaPlayer mp) {
                    LogUtil.i(XpLightlanuageService.TAG, "playDanceMusic completion");
                }
            });
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.xiaopeng.xuiservice.capabilities.llu.-$$Lambda$XpLightlanuageService$keTInT2YORn5wN_DHDlyhlGprwo
                @Override // android.media.MediaPlayer.OnPreparedListener
                public final void onPrepared(MediaPlayer mediaPlayer) {
                    XpLightlanuageService.this.lambda$scheduleEffectTask$2$XpLightlanuageService(task, mediaPlayer);
                }
            });
            player.prepareAsync();
        } catch (Exception e) {
            LogUtil.e(TAG, "playDanceMusic failed, " + e);
        }
    }

    public /* synthetic */ boolean lambda$scheduleEffectTask$1$XpLightlanuageService(EffectTask task, MediaPlayer mp, int what, int extra) {
        LogUtil.e(TAG, "mPlayer encountered error " + what + ", " + extra);
        mp.reset();
        showStatusCallback(4, task.light.name, task.light.type, -4096, null);
        return true;
    }

    public /* synthetic */ void lambda$scheduleEffectTask$2$XpLightlanuageService(EffectTask task, MediaPlayer mp) {
        if (task.light.name.equals(getRunningEffect())) {
            LogUtil.i(TAG, "playDanceMusic " + task.source);
            mp.start();
            runEffectTask(task);
            mPlayer = mp;
            SystemProperties.set(PROP_DANCING, OOBEEvent.STRING_TRUE);
        }
    }

    private void releaseAudioFocus() {
        if (mAudioFocus) {
            LogUtil.d(TAG, "releaseAudioFocus");
            mAudioManager.abandonAudioFocus(mAudioFocusListener);
            mAudioConfig.setBanVolumeChangeMode(3, 0);
            mAudioFocus = false;
        }
    }

    private void stopDanceMusic() {
        if (mPlayer == null) {
            return;
        }
        try {
            LogUtil.i(TAG, "stopDanceMusic");
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.release();
            mPlayer = null;
        } catch (Exception e) {
            LogUtil.e(TAG, "stopDanceMusic failed, " + e);
        }
        SystemProperties.set(PROP_DANCING, OOBEEvent.STRING_FALSE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setVolume(float leftVol, float rightVol) {
        if (mPlayer != null) {
            try {
                LogUtil.d(TAG, "setVolume (" + leftVol + ", " + rightVol + ")");
                mPlayer.setVolume(leftVol, rightVol);
            } catch (Exception e) {
                LogUtil.e(TAG, "setVolume failed, " + e);
            }
        }
    }

    private void playCustomEffect(LightEffect light, String source, int count) {
        LogUtil.d(TAG, "playCustomEffect name=" + light.name + ", count=" + count + "...");
        EffectTask task = new EffectTask(light, source, count);
        scheduleEffectTask(task);
    }

    private void playSpecialEffect(LightEffect light, int count) {
        try {
            int mode = light.special;
            int duration = light.duration;
            if (!hasDictType) {
                LogUtil.d(TAG, "playSpecialEffect, mode=" + mode + ", duration=" + duration + ", count=" + count);
                CarLluManager llu = mCar.getCarManager("xp_llu");
                llu.setMcuLluModeCtrl(mode, mode, mode, mode, mode, mode);
            }
            if (count == 0) {
                handleFinishEffect(duration);
            } else {
                handlePlayEffect(new PlayEffect(light, null, count, true, null), duration + 2000);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "playSpecialEffect failed, " + e);
        }
    }

    private void playRhythmEffect(final float ratio) {
        mRhythmEffect.seek(ratio);
        ScheduledFuture<?> scheduledFuture = mFuture;
        if (scheduledFuture == null || scheduledFuture.isDone()) {
            mFuture = mExecutor.scheduleAtFixedRate(new Runnable() { // from class: com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageService.2
                @Override // java.lang.Runnable
                public void run() {
                    Pair data = XpLightlanuageService.mRhythmEffect.get();
                    LogUtil.d(XpLightlanuageService.TAG, "rhythmEffect name=" + XpLightlanuageService.mRhythmEffect.name + ", ratio=" + ratio + ", fdata=" + data.first + ", rdata=" + data.second);
                    XpLightlanuageService.this.setPrivateCtrl(true, data);
                }
            }, 0L, 20L, TimeUnit.MILLISECONDS);
        }
        handleStopEffect(mRhythmEffect.name, false, 2000L);
    }

    private void repeatLightEffect(LightEffect light, boolean notify) {
        LogUtil.i(TAG, "REPEAT effect: " + light.name);
        if (needResetPrivateCtrl(light.reset)) {
            resetPrivateCtrl();
        }
        if (notify) {
            showStatusCallback(5, light.name, light.type, 0, null);
        }
    }

    private void startLightEffect(LightEffect light, boolean turnOff, String owner) {
        LogUtil.i(TAG, "START effect: " + light.name + ", turnOff=" + turnOff);
        if (turnOff) {
            setDayLightMode(0);
            turnoffHeadLight();
        }
        resetPrivateCtrl();
        mLight.push(light);
        showStatusCallback(1, light.name, light.type, 0, owner);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishLightEffect() {
        if (!mLight.empty()) {
            LightEffect light = mLight.pop();
            LogUtil.i(TAG, "FINISH effect: " + light.name);
            stopDanceMusic();
            releaseAudioFocus();
            stopPrivateCtrl();
            restoreHeadLight();
            setDayLightMode(1);
            showStatusCallback(3, light.name, light.type, 0, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopLightEffect(String name, boolean restore, boolean turnOn, boolean release) {
        if (mLight.empty()) {
            return;
        }
        if ("any".equals(name) || mLight.peek().name.equals(name)) {
            LightEffect light = mLight.pop();
            StringBuilder sb = new StringBuilder();
            sb.append(restore ? "STOP" : "INTERRUPT");
            sb.append(" effect: ");
            sb.append(light.name);
            LogUtil.i(TAG, sb.toString());
            ScheduledFuture<?> scheduledFuture = mFuture;
            if (scheduledFuture != null && !scheduledFuture.isDone()) {
                mFuture.cancel(true);
            }
            stopDanceMusic();
            if (release) {
                releaseAudioFocus();
            }
            stopPrivateCtrl();
            if (restore) {
                restoreHeadLight();
            }
            if (turnOn) {
                setDayLightMode(1);
            }
            showStatusCallback(2, light.name, light.type, 0, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playLightEffect(PlayEffect play) {
        LightEffect light = play.light;
        boolean turnOff = mLight.empty();
        int count = play.count > 0 ? play.count - 1 : -1;
        LogUtil.d(TAG, "playLightEffect");
        if (!play.repeat) {
            stopLightEffect("any", false, false, play.source == null);
        }
        if (mLight.empty()) {
            startLightEffect(light, turnOff, play.owner);
        } else if (!play.rhythm) {
            repeatLightEffect(light, play.source != null);
        }
        if (play.rhythm) {
            playRhythmEffect(play.ratio);
        } else if (needPlaySpecialMode(light.special)) {
            playSpecialEffect(light, count);
        } else {
            playCustomEffect(light, play.source, count);
        }
    }

    public synchronized void loadLightEffect(String path) {
        LightEffect effect = new LightEffect().readFromJson(path);
        if (effect != null && !mLightEffectMap.containsKey(effect.name)) {
            LogUtil.i(TAG, "loadLightEffect " + path);
            mLightEffectMap.put(effect.name, effect);
        }
    }

    public synchronized void clearLightEffect() {
        mLightEffectMap.clear();
    }

    public synchronized List<String> getLightEffect() {
        Set<String> set;
        set = mLightEffectMap.keySet();
        return new ArrayList(set);
    }

    public synchronized boolean isLightEffectLoaded(String name) {
        return mLightEffectMap.containsKey(name);
    }

    public boolean playEffect(String name, String source, int count) {
        LightEffect light = getLightEffectByName(name);
        int pid = Binder.getCallingPid();
        String pkg = PackageUtils.getPackageName(pid);
        return light != null && handlePlayEffect(new PlayEffect(light, source, count, false, pkg), 0L);
    }

    public boolean playEffect(float ratio) {
        String effect = getRunningEffect();
        return mRhythmPlay != null && (effect.isEmpty() || mRhythmPlay.light.name.equals(effect)) && handlePlayEffect(mRhythmPlay.setRatio(ratio), 0L);
    }

    public void stopEffect(boolean restore) {
        handleStopEffect("any", restore, 0L);
    }

    public void stopEffect(String name) {
        handleStopEffect(name, true, 0L);
    }

    public String getRunningEffect() {
        if (!mLight.empty()) {
            return mLight.peek().name;
        }
        return new String();
    }

    public boolean isDanceEffectRunning() {
        return (mLight.empty() || mPlayer == null) ? false : true;
    }

    public boolean requestAudioFocus() {
        if (!mAudioFocus && mAudioManager.requestAudioFocus(mAudioFocusListener, 3, 1) != 0) {
            mAudioConfig.setBanVolumeChangeMode(3, 1);
            mAudioFocus = true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("requestAudioFocus ");
        sb.append(mAudioFocus ? "succeed" : "failed");
        LogUtil.d(TAG, sb.toString());
        return mAudioFocus;
    }

    public boolean hasExpand() {
        return SystemProperties.getInt(PROP_EXPAND_CTRL, 0) == 1;
    }

    public void setRhythmEffect(String name) {
        LightEffect light = getLightEffectByName(name);
        int pid = Binder.getCallingPid();
        String pkg = PackageUtils.getPackageName(pid);
        if (light != null) {
            RhythmEffect rhythmEffect = mRhythmEffect;
            if (rhythmEffect == null || !rhythmEffect.name.equals(name)) {
                LogUtil.i(TAG, "setRhythmEffect, name=" + name);
                mRhythmPlay = new PlayEffect(light, pkg);
                mRhythmEffect = new RhythmEffect(light);
            }
        }
    }

    public void setMcuEffect(int name, int mode) {
        boolean normal = mode == 1 || mode == 0;
        if (normal) {
            boolean active = mode != 0;
            if (name == 1) {
                setLluFindCarSwitch(active);
                return;
            } else if (name == 2) {
                setMcuLluWakeWaitSwitch(active);
                return;
            } else if (name == 5) {
                setMcuLluSleepSwitch(active);
                return;
            } else if (name == 6) {
                setMcuLluAcChargingSwitch(active);
                return;
            } else if (name == 7) {
                setMcuLluDcChargingSwitch(active);
                return;
            } else if (name == 9) {
                setMcuLluPhotoSwitch(active);
                return;
            } else {
                return;
            }
        }
        setLluScriptStRequest(name, mode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doMcuEffectUpgrade(int value) {
        if (mUploader == null) {
            return;
        }
        try {
            CarLluManager llu = mCar.getCarManager("xp_llu");
            int pos = 1;
            if (value == 1) {
                LogUtil.d(TAG, "doMcuEffectUpgrade Start");
                llu.setLluScriptParameter(mUploader.build());
            } else if (value == 2) {
                synchronized (mListener) {
                    for (LightEffectListener listener : mListener) {
                        listener.onUpgradeEffect(mUploader.name, mUploader.mode);
                    }
                }
                mUploader = null;
            } else if (value != 4 && value != 5 && value != 6) {
                if (value == 7) {
                    LogUtil.d(TAG, "doMcuEffectUpgrade Finish");
                    llu.setLluScriptStRequest(2);
                }
            } else if (mUploader.available()) {
                int index = mUploader.frontLeft > 0 ? mUploader.frontIndex : mUploader.rearIndex;
                if (mUploader.frontLeft > 0) {
                    pos = 0;
                }
                int[] data = Arrays.stream(mUploader.get()).mapToInt(new ToIntFunction() { // from class: com.xiaopeng.xuiservice.capabilities.llu.-$$Lambda$gfCssnBJI7TKfXb_Jmv7raVYNkY
                    @Override // java.util.function.ToIntFunction
                    public final int applyAsInt(Object obj) {
                        return Integer.valueOf(((Integer) obj).intValue()).intValue();
                    }
                }).toArray();
                LogUtil.d(TAG, "doMcuEffectUpgrade Uploading...");
                llu.sendLluScriptData(index, pos, data.length, data);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "doMcuEffectUpgrade failed, " + e);
        }
    }

    public void unsetLightEffectListener(LightEffectListener listener) {
        StringBuilder sb = new StringBuilder();
        sb.append("unsetLightEffectListener ");
        sb.append(listener == null ? "invalid" : "ok");
        LogUtil.i(TAG, sb.toString());
        if (listener == null) {
            return;
        }
        synchronized (mListener) {
            mListener.remove(listener);
        }
    }

    public void setLightEffectListener(LightEffectListener listener) {
        StringBuilder sb = new StringBuilder();
        sb.append("setLightEffectListener ");
        sb.append(listener == null ? "invalid" : "ok");
        LogUtil.i(TAG, sb.toString());
        if (listener == null) {
            return;
        }
        synchronized (mListener) {
            mListener.add(listener);
        }
    }

    public void setAvmListener(AvmListener listener) {
        StringBuilder sb = new StringBuilder();
        sb.append("setAvmListener");
        sb.append(listener == null ? "invalid" : "ok");
        LogUtil.i(TAG, sb.toString());
        this.mAvmListener = listener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetLightlanuageStatus() {
        LogUtil.i(TAG, "resetLightlanuageStatus");
        stopPrivateCtrl();
        restoreHeadLight();
    }

    private XpLightlanuageService(Context context) {
        this.mHandlerThread = null;
        this.mHandler = null;
        mAudioConfig = new AudioConfig(context);
        mAudioManager = (AudioManager) context.getSystemService("audio");
        mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() { // from class: com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageService.3
            @Override // android.media.AudioManager.OnAudioFocusChangeListener
            public void onAudioFocusChange(int onChange) {
                if (XpLightlanuageService.this.isDanceEffectRunning()) {
                    if (onChange == -3) {
                        XpLightlanuageService.this.setVolume(0.2f, 0.2f);
                    } else if (onChange == -2 || onChange == -1) {
                        LogUtil.w(XpLightlanuageService.TAG, "AF loss, stopDanceEffect");
                        XpLightlanuageService.this.handleStopEffect("any", true, 0L);
                    } else if (onChange == 3) {
                        XpLightlanuageService.this.setVolume(1.0f, 1.0f);
                    }
                }
            }
        };
        ThemeOperationData themeData = getCurrentTheme();
        if (themeData != null) {
            String theme = themeData.getInnerName();
            String path = themeData.getThemePath() + "/lightlang";
            if (!theme.equals(DEFAULT_THEME)) {
                LogUtil.i(TAG, "load " + theme + " theme effects");
                mThemeMap.put(theme, new Theme(theme, path));
            }
            mTheme = theme;
        }
        mThemeListener = new ThemeOperationListener() { // from class: com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageService.4
            /* JADX WARN: Code restructure failed: missing block: B:17:0x0051, code lost:
                if (r13.equals("theme_selected") != false) goto L14;
             */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onThemeStatus(java.lang.String r13, java.lang.String r14, java.util.List<com.xiaopeng.xuimanager.themeoperation.ThemeOperationData> r15) {
                /*
                    Method dump skipped, instructions count: 310
                    To view this dump add '--comments-level debug' option
                */
                throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageService.AnonymousClass4.onThemeStatus(java.lang.String, java.lang.String, java.util.List):void");
            }
        };
        mThemeService.addListener(mThemeListener);
        CarClientManager.getInstance().addLluManagerListener(new CarLluManager.CarLluEventCallback() { // from class: com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageService.5
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557854229) {
                    XpLightlanuageService.this.doMcuEffectUpgrade(((Integer) value.getValue()).intValue());
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        });
        CarClientManager.getInstance().addConnectionListener(new IServiceConn() { // from class: com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageService.6
            @Override // com.xiaopeng.xuiservice.carcontrol.IServiceConn
            public void onConnectedCar() {
                XpLightlanuageService.this.resetLightlanuageStatus();
            }

            @Override // com.xiaopeng.xuiservice.carcontrol.IServiceConn
            public void onDisconnectCar() {
            }
        });
        this.mHandlerThread = new HandlerThread("XpLightlanuageThread");
        this.mHandlerThread.start();
        this.mHandler = new Handler(this.mHandlerThread.getLooper()) { // from class: com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageService.7
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                int i = msg.what;
                if (i == 1) {
                    XpLightlanuageService.this.playLightEffect((PlayEffect) msg.obj);
                } else if (i == 2) {
                    Pair<String, Boolean> pair = (Pair) msg.obj;
                    XpLightlanuageService.this.stopLightEffect((String) pair.first, ((Boolean) pair.second).booleanValue(), true, true);
                } else if (i == 3) {
                    XpLightlanuageService.this.finishLightEffect();
                }
            }
        };
        DumpDispatcher.registerDump(TAG, new DumpDispatcher.DebugDumper() { // from class: com.xiaopeng.xuiservice.capabilities.llu.-$$Lambda$XpLightlanuageService$D6wzmNWNpHEEkGuakZu3rFTyg-g
            @Override // com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
            public final void debugDump(PrintWriter printWriter, String[] strArr) {
                XpLightlanuageService.this.lambda$new$3$XpLightlanuageService(printWriter, strArr);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: handleDump */
    public void lambda$new$3$XpLightlanuageService(PrintWriter pw, String[] args) {
        pw.println("*dump-XpLightlanuageService");
        if (args != null) {
            int i = 0;
            while (i < args.length) {
                String str = args[i];
                char c = 65535;
                switch (str.hashCode()) {
                    case -1239378233:
                        if (str.equals("--showThemes")) {
                            c = 4;
                            break;
                        }
                        break;
                    case -870484091:
                        if (str.equals("--playEffect")) {
                            c = 1;
                            break;
                        }
                        break;
                    case -250247771:
                        if (str.equals("--showEffects")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 63179070:
                        if (str.equals("--deleteTheme")) {
                            c = 6;
                            break;
                        }
                        break;
                    case 492151251:
                        if (str.equals("--stopEffect")) {
                            c = 2;
                            break;
                        }
                        break;
                    case 1400456333:
                        if (str.equals("--selectTheme")) {
                            c = 5;
                            break;
                        }
                        break;
                    case 1981907379:
                        if (str.equals("--getTheme")) {
                            c = 3;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        List<String> effects = getLightEffect();
                        pw.println("showEffects: " + effects.toString());
                        break;
                    case 1:
                        break;
                    case 2:
                        if (i < args.length - 1) {
                            pw.println("stopEffect " + args[i + 1]);
                            stopEffect(args[i + 1]);
                            continue;
                        } else {
                            pw.println("stopEffect");
                            stopEffect(true);
                        }
                        i++;
                    case 3:
                        pw.println("getTheme: " + mTheme);
                        continue;
                        i++;
                    case 4:
                        List<String> themes = new ArrayList<>(mThemeMap.keySet());
                        pw.println("showThemes: " + themes.toString());
                        continue;
                        i++;
                    case 5:
                        if (i < args.length - 1) {
                            String theme = args[i + 1];
                            ThemeOperationData themeData = getCurrentTheme();
                            pw.println("selectTheme " + theme);
                            themeData.setInnerName(theme);
                            themeData.setThemePath("/data/theme/" + theme);
                            mThemeListener.onThemeStatus("theme_selected", (String) null, Collections.singletonList(themeData));
                        } else {
                            continue;
                        }
                        i++;
                    case 6:
                        if (i < args.length - 1) {
                            String theme2 = args[i + 1];
                            ThemeOperationData themeData2 = getCurrentTheme();
                            pw.println("deleteTheme " + theme2);
                            themeData2.setInnerName(theme2);
                            themeData2.setThemePath("/data/theme/" + theme2);
                            mThemeListener.onThemeStatus("theme_deleted", (String) null, Collections.singletonList(themeData2));
                        } else {
                            continue;
                        }
                        i++;
                    default:
                        i++;
                }
                if (i < args.length - 1) {
                    try {
                        int count = i < args.length - 2 ? Integer.parseInt(args[i + 2]) : 1;
                        boolean ret = playEffect(args[i + 1], null, count);
                        StringBuilder sb = new StringBuilder();
                        sb.append("playEffect ");
                        sb.append(args[i + 1]);
                        sb.append(ret ? " succeed" : " failed");
                        pw.println(sb.toString());
                    } catch (Exception e) {
                    }
                }
                i++;
            }
        }
    }

    public static XpLightlanuageService getInstance(Context context) {
        if (sService == null) {
            synchronized (XpLightlanuageService.class) {
                if (sService == null) {
                    sService = new XpLightlanuageService(context);
                }
            }
        }
        return sService;
    }
}
