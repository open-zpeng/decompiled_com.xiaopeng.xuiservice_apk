package com.xiaopeng.xuiservice.capabilities.ambientlight;

import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.atl.AtlConfiguration;
import android.car.hardware.atl.CarAtlManager;
import android.car.hardware.bcm.CarBcmManager;
import android.car.hardware.hvac.CarHvacManager;
import android.car.hardware.mcu.CarMcuManager;
import android.car.hardware.vcu.CarVcuManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import androidx.core.app.NotificationCompat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lzy.okgo.model.HttpHeaders;
import com.xiaopeng.biutil.BiLog;
import com.xiaopeng.biutil.BiLogFactory;
import com.xiaopeng.libtheme.ThemeManager;
import com.xiaopeng.speech.protocol.bean.recommend.RecommendBean;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.bluetooth.NfDef;
import com.xiaopeng.xuiservice.capabilities.ambientlight.XpAmbientLightService;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.carcontrol.IServiceConn;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import com.xiaopeng.xuiservice.innerutils.BiLogTransmit;
import com.xiaopeng.xuiservice.innerutils.DataLogUtils;
import com.xiaopeng.xuiservice.mediacenter.Constants;
import com.xiaopeng.xuiservice.mediacenter.visualizer.SDFftDataListener;
import com.xiaopeng.xuiservice.mediacenter.visualizer.VisualizerService;
import com.xiaopeng.xuiservice.smart.action.Actions;
import com.xiaopeng.xuiservice.utils.CatchHueUtils;
import com.xiaopeng.xuiservice.utils.FileUtils;
import com.xiaopeng.xuiservice.utils.PackageUtils;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class XpAmbientLightService {
    private static final int APPEND = 1;
    private static final int CHANGE = 0;
    private static final String KEY_ATL_SW = "atl_speaker";
    private static final String MODEPATH = "/system/etc/xuiservice/ambient/modes";
    public static final int MSG_BILOG = 100;
    public static final int MSG_BRIGHT = 6;
    public static final int MSG_COLORTYPE = 7;
    public static final int MSG_DOUBLECOLOR = 9;
    public static final int MSG_ENABLE = 1;
    public static final int MSG_ERR_EFFECT = 13;
    public static final int MSG_ERR_MODE = 14;
    public static final int MSG_MONOCOLOR = 8;
    public static final int MSG_PLAY = 2;
    public static final int MSG_REGIONBRIGHT = 12;
    public static final int MSG_REGIONCOLOR = 11;
    public static final int MSG_REQUEST = 0;
    public static final int MSG_SETAMBIENT = 15;
    public static final int MSG_SETMODE = 4;
    public static final int MSG_STOP = 3;
    public static final int MSG_SUBMODE = 5;
    public static final int MSG_THEMECOLOR = 10;
    private static final String PATH = "/system/etc/xuiservice/ambient/controller.conf";
    private static final String TAG = "XpAmbientLightService";
    private static final String THEMEPATH = "/system/etc/xuiservice/ambient/themes";
    private static final int TICK_PERIOD = 50;
    private static XpAmbientLightService sService = null;
    private int TICK_HVAC;
    private boolean hasAirConditioner;
    private boolean hasApply;
    private boolean hasCarSpeed;
    private boolean hasCatchHue;
    private volatile boolean hasChange;
    private boolean hasController;
    private boolean hasDoorLight;
    private boolean hasEnable;
    private boolean hasIgOn;
    private boolean hasInit;
    private boolean hasLite;
    private boolean hasMcuOn;
    private volatile boolean hasRhythmBrightChange;
    private volatile boolean hasSpeedBrightChange;
    private boolean hasSwitchOn;
    private boolean hasVisualizer;
    private AtlConfiguration.Builder mBuilder;
    private CarClientManager mCar;
    private CatchHueUtils mCatchHueUtils;
    private Map<String, String> mColorTypeMap;
    private Set<String> mColorTypes;
    private Context mContext;
    private Controller mController;
    private int mDisplayId;
    private Map<String, Pair<Integer, Integer>> mDoubleColorMap;
    private Map<String, Integer> mDutyCycleMap;
    private Map<String, Effect> mEffectMap;
    private Set<String> mEffectPartitions;
    private ScheduledThreadPoolExecutor mExecutor;
    private int mFactor;
    private int mFadeIn;
    private int mFadeOut;
    private ScheduledFuture<?> mFuture;
    private int mGroup;
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    private Map<String, Integer> mHashCodeMap;
    private int mHueColor;
    private CatchHueUtils.HueDataListener mHueDataListener;
    private int mHvacBright;
    private CarHvacManager.CarHvacEventCallback mHvacCallback;
    private int mHvacColor;
    private int mHvacPsnBright;
    private List<AmbientLightListener> mListener;
    private CarMcuManager.CarMcuEventCallback mMcuCallback;
    private Map<String, Effect> mModeMap;
    private Set<String> mModePartitions;
    private Set<String> mModes;
    private Map<String, Integer> mMonoColorMap;
    private Pair<String, String> mPlayEffect;
    private Random mRandom;
    private float mRatio;
    private Map<String, Map<String, Integer>> mRegionBrightMap;
    private Map<String, Map<String, Integer>> mRegionColorMap;
    private Map<String, String> mRegionMap;
    private int mRhythmBright;
    private SDFftDataListener mSDFftDataListener;
    private Map<String, String> mSnapshotStatusMap;
    private int mSpeedBright;
    private Map<String, String> mStatusMap;
    private Tasklet mTask;
    private Map<String, String> mThemeColorMap;
    private Map<String, ThemeColor> mThemeMap;
    private int mTickRatio;
    private long mTime;
    private VisualizerService mVisualizerService;
    private final int MIN = 10;
    private boolean hasDebug = SystemProperties.getBoolean("persist.sys.xpeng.ambient_debug", false);

    /* loaded from: classes5.dex */
    public interface AmbientLightListener {
        void onAmbientLightEvent(int i, String str, String str2, int i2);
    }

    static /* synthetic */ int access$3920(XpAmbientLightService x0, int x1) {
        int i = x0.TICK_HVAC - x1;
        x0.TICK_HVAC = i;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class AmbientMessage {
        public Pair<Integer, Integer> color;
        public int data;
        public Effect effect;
        public String owner;
        public String sdata;
        public String sdata2;
        public boolean status;

        public AmbientMessage(String owner) {
            this.owner = owner;
        }

        public AmbientMessage(Effect effect, String owner) {
            this.effect = effect;
            this.owner = owner;
        }

        public AmbientMessage(String sdata, int data, String owner) {
            this.sdata = sdata;
            this.data = data;
            this.owner = owner;
        }

        public AmbientMessage(String sdata, Effect effect, String owner) {
            this.sdata = sdata;
            this.effect = effect;
            this.owner = owner;
        }

        public AmbientMessage(String sdata, String sdata2, String owner) {
            this.sdata = sdata;
            this.sdata2 = sdata2;
            this.owner = owner;
        }

        public AmbientMessage(String sdata, String sdata2, int data, String owner) {
            this.sdata = sdata;
            this.sdata2 = sdata2;
            this.data = data;
            this.owner = owner;
        }

        public AmbientMessage(String sdata, Pair<Integer, Integer> color, String owner) {
            this.sdata = sdata;
            this.color = color;
            this.owner = owner;
        }

        public AmbientMessage(boolean status, String owner) {
            this.status = status;
            this.owner = owner;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class BiAmbient {
        private BiLog bilog = BiLogFactory.create(DataLogUtils.XUI_PID, DataLogUtils.AMBIENT_BID);

        public void submit() {
            LogUtil.d(XpAmbientLightService.TAG, "BiAmbient: " + this.bilog.getString());
            BiLogTransmit.getInstance().submit(this.bilog);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        public BiAmbient(String event, String result, String source) {
            char c;
            this.bilog.push(NotificationCompat.CATEGORY_EVENT, event);
            this.bilog.push("ts", String.valueOf(System.currentTimeMillis()));
            this.bilog.push(RecommendBean.SHOW_TIME_RESULT, result);
            this.bilog.push("source", source);
            if ("power".equals(event) && result.equals("on")) {
                JSONArray params = new JSONArray();
                try {
                    for (String partition : XpAmbientLightService.this.getProviderPartitions()) {
                        String mode = XpAmbientLightService.this.getProviderMode(partition);
                        String colorType = XpAmbientLightService.this.getProviderColorType(partition, mode);
                        JSONObject object = new JSONObject();
                        object.put("partition", partition);
                        object.put(SpeechConstants.KEY_MODE, mode);
                        object.put("bright", String.valueOf(XpAmbientLightService.this.getProviderBright(partition)));
                        object.put("colorType", colorType);
                        switch (colorType.hashCode()) {
                            case -1349088399:
                                if (colorType.equals("custom")) {
                                    c = 3;
                                    break;
                                }
                                c = 65535;
                                break;
                            case -1325958191:
                                if (colorType.equals("double")) {
                                    c = 1;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 3357411:
                                if (colorType.equals("mono")) {
                                    c = 0;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 110327241:
                                if (colorType.equals(ThemeManager.AttributeSet.THEME)) {
                                    c = 2;
                                    break;
                                }
                                c = 65535;
                                break;
                            default:
                                c = 65535;
                                break;
                        }
                        if (c == 0) {
                            object.put("monoColor", XpAmbientLightService.this.parseColor(XpAmbientLightService.this.getProviderMonoColor(partition)));
                        } else if (c == 1) {
                            Pair<Integer, Integer> colors = XpAmbientLightService.this.getProviderDoubleColor(partition);
                            List<String> list = Arrays.asList(XpAmbientLightService.this.parseColor(((Integer) colors.first).intValue()), XpAmbientLightService.this.parseColor(((Integer) colors.second).intValue()));
                            object.put("doubleColor", list.toString());
                        } else if (c == 2) {
                            object.put("themeColor", XpAmbientLightService.this.getProviderThemeColor(partition));
                        } else if (c == 3) {
                            Map<String, Integer> map = (Map) XpAmbientLightService.this.mRegionColorMap.get(partition);
                            map.entrySet().stream().collect(Collectors.toMap(new Function() { // from class: com.xiaopeng.xuiservice.capabilities.ambientlight.-$$Lambda$XpAmbientLightService$BiAmbient$9rYavh4rGTCRLjKsWYib__RP7ak
                                @Override // java.util.function.Function
                                public final Object apply(Object obj) {
                                    return XpAmbientLightService.BiAmbient.lambda$new$0((Map.Entry) obj);
                                }
                            }, new Function() { // from class: com.xiaopeng.xuiservice.capabilities.ambientlight.-$$Lambda$XpAmbientLightService$BiAmbient$kO2fqm01b62oZpugACxkUvYvfQ8
                                @Override // java.util.function.Function
                                public final Object apply(Object obj) {
                                    return XpAmbientLightService.BiAmbient.this.lambda$new$1$XpAmbientLightService$BiAmbient((Map.Entry) obj);
                                }
                            }));
                            object.put("customColor", map.toString());
                        }
                        params.put(object);
                    }
                } catch (Exception e) {
                }
                this.bilog.push("params", params.toString());
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ String lambda$new$0(Map.Entry entry) {
            return (String) entry.getKey();
        }

        public /* synthetic */ String lambda$new$1$XpAmbientLightService$BiAmbient(Map.Entry entry) {
            return XpAmbientLightService.this.parseColor(((Integer) entry.getValue()).intValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class Lin {
        public String lin1;
        public String lin2;
        public String lin3;

        private Lin() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class Controller {
        public Set<String> colorTypes;
        public Map<String, String> effectPartitionsMap;
        public Map<String, Integer> groupsMap;
        public Map<Integer, Lin> lightsMap;
        public Map<String, String> modePartitionsMap;
        public Map<String, Integer> paramsMap;
        public Map<String, String> regionsMap;
        public Map<String, Boolean> strategyMap;

        private Controller() {
        }

        public Controller readFromJson(String file) {
            BufferedReader reader = null;
            try {
                try {
                    reader = new BufferedReader(new FileReader(file));
                    Gson gson = new GsonBuilder().create();
                    Controller controller = (Controller) gson.fromJson((Reader) reader, (Class<Object>) Controller.class);
                    try {
                        reader.close();
                    } catch (Exception e) {
                        LogUtil.e(XpAmbientLightService.TAG, "close " + file + " Exception: " + e);
                    }
                    return controller;
                } catch (Throwable th) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e2) {
                            LogUtil.e(XpAmbientLightService.TAG, "close " + file + " Exception: " + e2);
                        }
                    }
                    throw th;
                }
            } catch (Exception e3) {
                LogUtil.e(XpAmbientLightService.TAG, "readFromJson Exception: " + e3);
                if (reader != null) {
                    try {
                        reader.close();
                        return null;
                    } catch (Exception e4) {
                        LogUtil.e(XpAmbientLightService.TAG, "close " + file + " Exception: " + e4);
                        return null;
                    }
                }
                return null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class ThemeColor {
        public Map<String, Integer> colors;
        public String name;

        private ThemeColor() {
        }

        public ThemeColor readFromJson(String file) {
            BufferedReader reader = null;
            try {
                try {
                    reader = new BufferedReader(new FileReader(file));
                    Gson gson = new GsonBuilder().create();
                    ThemeColor themeColor = (ThemeColor) gson.fromJson((Reader) reader, (Class<Object>) ThemeColor.class);
                    try {
                        reader.close();
                    } catch (Exception e) {
                        LogUtil.e(XpAmbientLightService.TAG, "close " + file + " Exception: " + e);
                    }
                    return themeColor;
                } catch (Throwable th) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e2) {
                            LogUtil.e(XpAmbientLightService.TAG, "close " + file + " Exception: " + e2);
                        }
                    }
                    throw th;
                }
            } catch (Exception e3) {
                LogUtil.e(XpAmbientLightService.TAG, "readFromJson Exception: " + e3);
                if (reader != null) {
                    try {
                        reader.close();
                        return null;
                    } catch (Exception e4) {
                        LogUtil.e(XpAmbientLightService.TAG, "close " + file + " Exception: " + e4);
                        return null;
                    }
                }
                return null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class Packet {
        public int bright;
        public List<Integer> colors;
        public int fade;
        public List<Integer> groups;
        public boolean has2ndColor;
        public int period;

        private Packet() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class Effect {
        public Set<String> colorTypes;
        public int count;
        public int displayId;
        public boolean hasHue;
        public boolean hasMemColorType;
        public boolean hasRhythm;
        public String name;
        public List<Packet> packets;
        public int priority;

        private Effect() {
        }

        public Effect readFromJson(String file) {
            BufferedReader reader = null;
            try {
                try {
                    reader = new BufferedReader(new FileReader(file));
                    Gson gson = new GsonBuilder().create();
                    Effect effect = (Effect) gson.fromJson((Reader) reader, (Class<Object>) Effect.class);
                    try {
                        reader.close();
                    } catch (Exception e) {
                        LogUtil.e(XpAmbientLightService.TAG, "close " + file + " Exception: " + e);
                    }
                    return effect;
                } catch (Throwable th) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e2) {
                            LogUtil.e(XpAmbientLightService.TAG, "close " + file + " Exception: " + e2);
                        }
                    }
                    throw th;
                }
            } catch (Exception e3) {
                LogUtil.e(XpAmbientLightService.TAG, "readFromJson Exception: " + e3);
                if (reader != null) {
                    try {
                        reader.close();
                        return null;
                    } catch (Exception e4) {
                        LogUtil.e(XpAmbientLightService.TAG, "close " + file + " Exception: " + e4);
                        return null;
                    }
                }
                return null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class EffectTask {
        private Map<String, Integer> brights;
        private Map<String, Integer> colors;
        private String current;
        private Map<String, Integer> dutyBrights;
        private Effect effect;
        private int fade;
        private int group;
        private boolean hasBright;
        private boolean hasColor;
        private boolean hasGroup;
        private boolean hasMode;
        private boolean hasSpeed;
        private int loop;
        private int mask;
        private String overlap;
        private String owner;
        private String part;
        private String partition;
        private int prevBright;
        private int prevColor;
        private int prevGroup;
        private String previous;
        private List<Integer> status;
        private int index = 0;
        private int elapsed = 0;
        private boolean hasDone = false;
        private Map<String, Integer> regionBrights = new ArrayMap();
        private Packet packet = null;

        public EffectTask(String partition, Effect effect, String owner) {
            this.partition = partition;
            this.part = XpAmbientLightService.this.mModePartitions.contains(partition) ? partition : Actions.ACTION_ALL;
            this.effect = effect;
            this.owner = owner;
            this.loop = effect.count < 0 ? -1 : effect.packets.size() * effect.count;
            this.hasMode = XpAmbientLightService.this.checkMode(effect.name);
            this.hasSpeed = "speed".equals(effect.name);
            this.prevGroup = XpAmbientLightService.this.mGroup;
            this.prevColor = XpAmbientLightService.this.mHueColor;
            this.dutyBrights = getDutyBrights();
            this.prevBright = XpAmbientLightService.this.mRhythmBright;
            XpAmbientLightService.this.mSpeedBright = this.hasSpeed ? 100 : XpAmbientLightService.this.mSpeedBright;
            LogUtil.i(XpAmbientLightService.TAG, "EffectTask " + partition + "=" + effect.name);
        }

        public void cleanStatus() {
            this.previous = "";
        }

        public boolean hasSpeedMode() {
            return this.hasSpeed;
        }

        public boolean hasStop() {
            return this.hasDone;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void stop() {
            this.hasDone = true;
        }

        private Packet getPacket(int index) {
            return this.effect.packets.get(index % this.effect.packets.size());
        }

        private boolean hasPeriod(int tickPeriod, int period) {
            this.elapsed += tickPeriod;
            return this.elapsed >= period;
        }

        private boolean hasFinish(int index) {
            int i = this.loop;
            return i >= 0 && index >= i;
        }

        private int getMask(String partition, String overlap) {
            if (XpAmbientLightService.this.mController == null) {
                return XpAmbientLightService.this.hasController ? -1 : 0;
            }
            String hexPartition = XpAmbientLightService.this.mEffectPartitions.contains(partition) ? XpAmbientLightService.this.mController.effectPartitionsMap.get(partition) : XpAmbientLightService.this.mController.modePartitionsMap.get(partition);
            if (TextUtils.isEmpty(overlap)) {
                if (XpAmbientLightService.this.mController.groupsMap.containsKey(hexPartition)) {
                    return XpAmbientLightService.this.mController.groupsMap.get(hexPartition).intValue();
                }
                return -1;
            }
            String hexOverlap = XpAmbientLightService.this.mController.effectPartitionsMap.get(overlap);
            int bitmap = Integer.valueOf(hexPartition, 16).intValue() - (Integer.valueOf(hexOverlap, 16).intValue() & Integer.valueOf(hexPartition, 16).intValue());
            if (bitmap == 0 || !XpAmbientLightService.this.mController.groupsMap.containsKey(Integer.toHexString(bitmap))) {
                return -1;
            }
            return XpAmbientLightService.this.mController.groupsMap.get(Integer.toHexString(bitmap)).intValue();
        }

        private Map<String, Integer> getColors(boolean hasSecond) {
            if (this.effect.hasHue) {
                if (!this.effect.hasRhythm || this.prevBright != XpAmbientLightService.this.mRhythmBright) {
                    this.prevColor = XpAmbientLightService.this.mHueColor;
                    XpAmbientLightService xpAmbientLightService = XpAmbientLightService.this;
                    return xpAmbientLightService.toMaps(xpAmbientLightService.mHueColor);
                }
                return XpAmbientLightService.this.toMaps(this.prevColor);
            } else if (!this.hasColor) {
                String str = (String) XpAmbientLightService.this.mColorTypeMap.get(this.part);
                char c = 65535;
                switch (str.hashCode()) {
                    case -1349088399:
                        if (str.equals("custom")) {
                            c = 3;
                            break;
                        }
                        break;
                    case -1325958191:
                        if (str.equals("double")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 3357411:
                        if (str.equals("mono")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 110327241:
                        if (str.equals(ThemeManager.AttributeSet.THEME)) {
                            c = 2;
                            break;
                        }
                        break;
                }
                if (c == 0) {
                    XpAmbientLightService xpAmbientLightService2 = XpAmbientLightService.this;
                    return xpAmbientLightService2.toMaps(((Integer) xpAmbientLightService2.mMonoColorMap.get(this.part)).intValue());
                } else if (c == 1) {
                    Pair<Integer, Integer> color = (Pair) XpAmbientLightService.this.mDoubleColorMap.get(this.part);
                    return XpAmbientLightService.this.toMaps(((Integer) (hasSecond ? color.second : color.first)).intValue());
                } else if (c != 2) {
                    if (c == 3) {
                        return (Map) XpAmbientLightService.this.mRegionColorMap.get(this.part);
                    }
                    XpAmbientLightService xpAmbientLightService3 = XpAmbientLightService.this;
                    return xpAmbientLightService3.toMaps(((Integer) xpAmbientLightService3.mMonoColorMap.get(this.part)).intValue());
                } else {
                    return ((ThemeColor) XpAmbientLightService.this.mThemeMap.get(XpAmbientLightService.this.mThemeColorMap.get(this.part))).colors;
                }
            } else {
                return this.colors;
            }
        }

        private Map<String, Integer> getDutyBrights() {
            if ("custom".equals(XpAmbientLightService.this.mColorTypeMap.get(this.part))) {
                return (Map) XpAmbientLightService.this.mRegionBrightMap.get(this.part);
            }
            XpAmbientLightService xpAmbientLightService = XpAmbientLightService.this;
            return xpAmbientLightService.toMaps(((Integer) xpAmbientLightService.mDutyCycleMap.get(this.part)).intValue());
        }

        private Map<String, Integer> getBrights(int bright) {
            if (this.hasMode && "custom".equals(XpAmbientLightService.this.mColorTypeMap.get(this.part))) {
                for (Map.Entry<String, Integer> entry : ((Map) XpAmbientLightService.this.mRegionBrightMap.get(this.part)).entrySet()) {
                    this.regionBrights.put(entry.getKey(), Integer.valueOf(Math.max(5, (entry.getValue().intValue() * bright) / 100)));
                }
                return this.regionBrights;
            }
            XpAmbientLightService xpAmbientLightService = XpAmbientLightService.this;
            return xpAmbientLightService.toMaps(Math.max(5, ((this.hasMode ? ((Integer) xpAmbientLightService.mDutyCycleMap.get(this.part)).intValue() : 100) * bright) / 100));
        }

        private int parseGroup(int data) {
            if (this.hasMode && XpAmbientLightService.this.hasDoorLight && data == 0) {
                return 3;
            }
            return data;
        }

        public void run() throws Exception {
            Map<String, Integer> map;
            boolean hasBrightManual;
            int i;
            int i2;
            int i3;
            int i4;
            if (this.packet == null || hasPeriod(XpAmbientLightService.this.mTickRatio * 50, this.packet.period)) {
                if (hasFinish(this.index)) {
                    if (!this.hasMode) {
                        XpAmbientLightService.this.mTask.priority = 0;
                        XpAmbientLightService.this.mPlayEffect = null;
                        XpAmbientLightService.this.showAmbientLightEvent(3, this.partition, this.effect.name, 0, this.owner);
                    }
                    XpAmbientLightService.this.mTask.stopEffect(this);
                    return;
                }
                int i5 = this.index;
                this.index = i5 + 1;
                this.packet = getPacket(i5);
                this.elapsed = 0;
                if (this.index < 0) {
                    this.index = 0;
                }
                this.hasGroup = this.packet.groups != null && this.packet.groups.size() > 0;
                this.hasColor = this.packet.colors != null && this.packet.colors.size() > 0;
                this.hasBright = this.packet.bright >= 0;
                this.group = this.hasGroup ? this.packet.groups.get(XpAmbientLightService.this.mRandom.nextInt(this.packet.groups.size())).intValue() : this.group;
                if (this.hasColor) {
                    map = XpAmbientLightService.this.toMaps(this.packet.colors.get(XpAmbientLightService.this.mRandom.nextInt(this.packet.colors.size())).intValue());
                } else {
                    map = this.colors;
                }
                this.colors = map;
            }
            this.mask = getMask(this.partition, this.overlap);
            if (this.mask >= 0) {
                boolean hasManual = this.hasMode && XpAmbientLightService.this.hasChange;
                if (this.hasMode) {
                    hasBrightManual = this.effect.hasRhythm ? XpAmbientLightService.this.hasRhythmBrightChange : XpAmbientLightService.this.hasSpeedBrightChange;
                } else {
                    hasBrightManual = false;
                }
                if (!this.hasGroup) {
                    i = XpAmbientLightService.this.mGroup;
                } else {
                    i = this.group;
                }
                this.group = i;
                this.colors = getColors(this.packet.has2ndColor);
                if (hasManual) {
                    i2 = 100;
                } else if (this.effect.hasRhythm) {
                    i2 = XpAmbientLightService.this.mRhythmBright;
                } else {
                    i2 = this.hasBright ? this.packet.bright : XpAmbientLightService.this.mSpeedBright;
                }
                this.brights = getBrights(i2);
                this.fade = hasManual ? 100 : this.effect.hasRhythm ? (XpAmbientLightService.this.mRhythmBright * XpAmbientLightService.this.fadeOut()) / 100 : this.packet.fade;
                this.status = new ArrayList(Arrays.asList(Integer.valueOf(this.mask), Integer.valueOf(this.group)));
                this.status.addAll(this.colors.values());
                this.status.addAll(this.brights.values());
                this.current = (String) this.status.stream().map(new Function() { // from class: com.xiaopeng.xuiservice.capabilities.ambientlight.-$$Lambda$znfQj8LqOvyui6ncUHU4komPIHY
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return String.valueOf((Integer) obj);
                    }
                }).collect(Collectors.joining());
                if (!this.current.equals(this.previous)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("run ");
                    sb.append(this.partition);
                    sb.append("=");
                    sb.append(this.effect.name);
                    sb.append(this.hasMode ? " mode" : " effect");
                    LogUtil.d(XpAmbientLightService.TAG, sb.toString());
                    if (this.hasMode && (i3 = this.group) != (i4 = this.prevGroup)) {
                        if (i3 == 0) {
                            if (XpAmbientLightService.this.hasDoorLight) {
                                LogUtil.i(XpAmbientLightService.TAG, "turn on doorHandle lights");
                                XpAmbientLightService xpAmbientLightService = XpAmbientLightService.this;
                                xpAmbientLightService.setGroupLightData(0, 1, xpAmbientLightService.toMaps(6), XpAmbientLightService.this.toMaps(100), this.fade);
                            }
                        } else {
                            if (i4 == 0 && XpAmbientLightService.this.hasDoorLight) {
                                LogUtil.i(XpAmbientLightService.TAG, "turn off doorHandle lights");
                                XpAmbientLightService xpAmbientLightService2 = XpAmbientLightService.this;
                                xpAmbientLightService2.setGroupLightData(0, 1, xpAmbientLightService2.toMaps(0), XpAmbientLightService.this.toMaps(0), this.fade);
                            }
                            if (this.group == 2) {
                                LogUtil.i(XpAmbientLightService.TAG, "turn off trebleSpeaker lights");
                                XpAmbientLightService xpAmbientLightService3 = XpAmbientLightService.this;
                                xpAmbientLightService3.setGroupLightData(0, 1, xpAmbientLightService3.toMaps(0), XpAmbientLightService.this.toMaps(0), this.fade);
                                XpAmbientLightService xpAmbientLightService4 = XpAmbientLightService.this;
                                xpAmbientLightService4.setGroupLightData(0, 7, xpAmbientLightService4.toMaps(0), XpAmbientLightService.this.toMaps(0), this.fade);
                            }
                        }
                        this.prevGroup = this.group;
                    }
                    if (hasManual) {
                        Map<String, Integer> dutyMaps = getDutyBrights();
                        if (!hasBrightManual && !this.dutyBrights.equals(dutyMaps)) {
                            LogUtil.d(XpAmbientLightService.TAG, "hasBrightChange " + this.dutyBrights + " -> " + dutyMaps);
                            hasBrightManual = true;
                            if (this.effect.hasRhythm) {
                                XpAmbientLightService.this.hasRhythmBrightChange = true;
                                XpAmbientLightService xpAmbientLightService5 = XpAmbientLightService.this;
                                this.prevBright = 100;
                                xpAmbientLightService5.mRhythmBright = 100;
                            } else if (this.hasSpeed) {
                                XpAmbientLightService.this.hasSpeedBrightChange = true;
                                XpAmbientLightService.this.mSpeedBright = 100;
                            }
                        }
                        XpAmbientLightService.this.hasChange = false;
                        this.dutyBrights = dutyMaps;
                    }
                    if (!this.effect.hasRhythm) {
                        XpAmbientLightService.this.setGroupLightData(this.mask, parseGroup(this.group), this.colors, this.brights, this.fade);
                    } else {
                        int i6 = 10;
                        if (hasManual) {
                            XpAmbientLightService.this.setGroupLightData(this.mask, parseGroup(this.group), this.colors, this.brights, this.fade);
                            if (!hasBrightManual) {
                                XpAmbientLightService.this.setGroupLightData(this.mask, parseGroup(this.group), this.colors, getBrights(10), this.packet.fade);
                            }
                        } else if (this.prevBright != XpAmbientLightService.this.mRhythmBright) {
                            XpAmbientLightService.this.setGroupLightData(this.mask, parseGroup(this.group), this.colors, this.brights, XpAmbientLightService.this.fadeIn());
                            XpAmbientLightService.this.setGroupLightData(this.mask, parseGroup(this.group), this.colors, getBrights(10), this.fade);
                            this.prevBright = XpAmbientLightService.this.mRhythmBright;
                        } else {
                            XpAmbientLightService xpAmbientLightService6 = XpAmbientLightService.this;
                            int i7 = this.mask;
                            int parseGroup = parseGroup(this.group);
                            Map<String, Integer> map2 = this.colors;
                            if (hasBrightManual) {
                                i6 = 100;
                            }
                            xpAmbientLightService6.setGroupLightData(i7, parseGroup, map2, getBrights(i6), this.packet.fade);
                        }
                    }
                    this.previous = this.current;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class Tasklet implements Runnable {
        private EffectTask current;
        private List<EffectTask> taskList = Collections.synchronizedList(new ArrayList());
        private int tick = 0;
        private int refCountRhythm = 0;
        private int refCountHue = 0;
        private int refCountSpeed = 0;
        private int lastHvacBright = 0;
        private int lastHvacPsnBright = 0;
        private int priority = 0;

        public Tasklet(EffectTask effectTask) {
            XpAmbientLightService.this.TICK_HVAC = 0;
            this.taskList.add(effectTask);
            this.refCountRhythm += effectTask.effect.hasRhythm ? 1 : 0;
            this.refCountHue += effectTask.effect.hasHue ? 1 : 0;
            this.refCountSpeed += effectTask.hasSpeedMode() ? 1 : 0;
            XpAmbientLightService.this.hasChange = false;
            if (this.refCountRhythm > 0) {
                XpAmbientLightService.this.startCaptureRatio();
            }
            if (this.refCountHue > 0) {
                XpAmbientLightService.this.startCaptureHue(effectTask.effect.displayId);
            }
            if (this.refCountSpeed > 0) {
                XpAmbientLightService.this.hasCarSpeed = true;
            }
        }

        public Iterator<EffectTask> iteratorTasks() {
            return this.taskList.iterator();
        }

        public boolean addEffect(EffectTask task) {
            boolean res = this.taskList.add(task);
            if (res) {
                this.refCountRhythm += task.effect.hasRhythm ? 1 : 0;
                this.refCountHue += task.effect.hasHue ? 1 : 0;
                this.refCountSpeed += task.hasSpeedMode() ? 1 : 0;
                if (this.refCountRhythm > 0) {
                    XpAmbientLightService.this.startCaptureRatio();
                }
                if (this.refCountHue > 0) {
                    XpAmbientLightService.this.startCaptureHue(task.effect.displayId);
                }
                if (this.refCountSpeed > 0) {
                    XpAmbientLightService.this.hasCarSpeed = true;
                }
            }
            return res;
        }

        public void stopEffect(EffectTask task) {
            this.refCountRhythm -= task.effect.hasRhythm ? 1 : 0;
            this.refCountHue -= task.effect.hasHue ? 1 : 0;
            this.refCountSpeed -= task.hasSpeedMode() ? 1 : 0;
            if (this.refCountRhythm == 0) {
                XpAmbientLightService.this.stopCaptureRatio();
            }
            if (this.refCountHue == 0) {
                XpAmbientLightService xpAmbientLightService = XpAmbientLightService.this;
                xpAmbientLightService.stopCaptureHue(xpAmbientLightService.mDisplayId);
            }
            if (this.refCountSpeed == 0) {
                XpAmbientLightService.this.hasCarSpeed = false;
            }
            task.stop();
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                if (this.taskList.isEmpty()) {
                    XpAmbientLightService.this.mFuture.cancel(false);
                    XpAmbientLightService.this.setGroupLightData(0, 0, XpAmbientLightService.this.toMaps(0), XpAmbientLightService.this.toMaps(0), 0);
                } else if (XpAmbientLightService.this.hasAirConditioner && XpAmbientLightService.this.mPlayEffect == null && XpAmbientLightService.this.TICK_HVAC > 0) {
                    int bright = ((Integer) XpAmbientLightService.this.mDutyCycleMap.get(Actions.ACTION_ALL)).intValue();
                    if (this.lastHvacBright == XpAmbientLightService.this.mHvacBright || XpAmbientLightService.this.TICK_HVAC % 2 != 0) {
                        if (this.lastHvacPsnBright != XpAmbientLightService.this.mHvacPsnBright && XpAmbientLightService.this.TICK_HVAC % 2 == 1) {
                            this.lastHvacPsnBright = XpAmbientLightService.this.mHvacPsnBright;
                            XpAmbientLightService.this.setGroupLightData(0, 19, XpAmbientLightService.this.toMaps(XpAmbientLightService.this.mHvacColor), XpAmbientLightService.this.toMaps((XpAmbientLightService.this.mHvacPsnBright * Math.max(5, bright)) / 100), NfDef.STATE_STREAMING);
                        }
                    } else {
                        this.lastHvacBright = XpAmbientLightService.this.mHvacBright;
                        XpAmbientLightService.this.setGroupLightData(0, 18, XpAmbientLightService.this.toMaps(XpAmbientLightService.this.mHvacColor), XpAmbientLightService.this.toMaps((XpAmbientLightService.this.mHvacBright * Math.max(5, bright)) / 100), NfDef.STATE_STREAMING);
                    }
                    XpAmbientLightService.access$3920(XpAmbientLightService.this, 1);
                    if (XpAmbientLightService.this.TICK_HVAC == 0) {
                        Iterator<EffectTask> iter = iteratorTasks();
                        while (iter.hasNext()) {
                            EffectTask task = iter.next();
                            task.cleanStatus();
                        }
                    }
                } else {
                    if (this.tick < 0) {
                        this.tick = 0;
                    }
                    List<EffectTask> list = this.taskList;
                    int i = this.tick;
                    this.tick = i + 1;
                    this.current = list.get(i % this.taskList.size());
                    if (this.current.hasStop()) {
                        this.taskList.remove(this.current);
                        return;
                    }
                    XpAmbientLightService.this.mTickRatio = this.taskList.size();
                    if (XpAmbientLightService.this.mPlayEffect == null && !TextUtils.isEmpty(this.current.overlap)) {
                        this.current.overlap = new String();
                    }
                    this.current.run();
                }
            } catch (Exception e) {
                LogUtil.e(XpAmbientLightService.TAG, "EffectTask exception " + Log.getStackTraceString(e));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int fadeIn() {
        return this.hasDebug ? SystemProperties.getInt("persist.sys.xpeng.ambient_fadein", 0) : this.mFadeIn;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int fadeOut() {
        return this.hasDebug ? SystemProperties.getInt("persist.sys.xpeng.ambient_fadeout", 1000) : this.mFadeOut;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int factor() {
        return this.hasDebug ? SystemProperties.getInt("persist.sys.xpeng.ambient_factor", 10) : this.mFactor;
    }

    private boolean hasRgbColor(int color) {
        return ((color >>> 24) & 1) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String parseColor(int color) {
        if (hasRgbColor(color)) {
            return ((color >> 16) & 255) + "-" + ((color >> 8) & 255) + "-" + (color & 255);
        }
        return String.valueOf(color);
    }

    private String parseColorMap(Map<String, Integer> colors) {
        if (colors.containsKey(Actions.ACTION_ALL)) {
            return parseColor(colors.get(Actions.ACTION_ALL).intValue());
        }
        StringBuffer buffer = new StringBuffer(" ");
        for (Map.Entry<String, Integer> entry : colors.entrySet()) {
            buffer.append(entry.getKey());
            buffer.append("=");
            buffer.append(parseColor(entry.getValue().intValue()));
            buffer.append(" ");
        }
        return buffer.toString();
    }

    private String parseBrightMap(Map<String, Integer> brights) {
        if (brights.containsKey(Actions.ACTION_ALL)) {
            return String.valueOf(brights.get(Actions.ACTION_ALL));
        }
        StringBuffer buffer = new StringBuffer(" ");
        for (Map.Entry<String, Integer> entry : brights.entrySet()) {
            buffer.append(entry.getKey());
            buffer.append("=");
            buffer.append(entry.getValue());
            buffer.append(" ");
        }
        return buffer.toString();
    }

    private boolean checkColor(int color) {
        return (hasRgbColor(color) && this.hasController) || (color > 0 && color <= 20);
    }

    private boolean checkBright(int bright) {
        return bright >= 0 && bright <= 100;
    }

    private boolean checkPolicy(int priority) {
        return this.hasApply || (priority > 0 && this.hasEnable);
    }

    private boolean checkEffectPartition(String partition) {
        return this.mEffectPartitions.contains(partition);
    }

    private boolean checkModePartition(String partition) {
        return this.mModePartitions.contains(partition);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkMode(String mode) {
        return this.mModes.contains(mode);
    }

    private boolean checkColorType(String colorType) {
        return this.mColorTypes.contains(colorType);
    }

    private boolean checkThemeColor(String theme) {
        return this.mThemeMap.containsKey(theme);
    }

    private boolean checkRegion(String region) {
        return !this.mRegionMap.isEmpty() && this.mRegionMap.values().contains(region);
    }

    private boolean hasSupportColorType(String mode, String colorType) {
        return this.mModeMap.get(mode).colorTypes.contains(colorType);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Map<String, Integer> toMaps(final int data) {
        return new HashMap<String, Integer>() { // from class: com.xiaopeng.xuiservice.capabilities.ambientlight.XpAmbientLightService.1
            {
                put(Actions.ACTION_ALL, Integer.valueOf(data));
            }
        };
    }

    private void sendBiAmbient(int type, String sdata, String sdata2, int data, String owner) {
        boolean hasDelay = false;
        String event = null;
        String result = null;
        switch (type) {
            case 1:
                event = "power";
                result = data == 1 ? "on" : "off";
                break;
            case 2:
                event = "effect";
                result = sdata + "=" + sdata2;
                break;
            case 4:
                event = data == 0 ? Constants.CONTROL_PLAY_MODE : "addMode";
                result = sdata + "=" + sdata2;
                break;
            case 5:
                event = "subMode";
                result = sdata + "=" + sdata2;
                break;
            case 6:
                event = "bright";
                result = sdata + "=" + data;
                hasDelay = true;
                break;
            case 7:
                event = "colorType";
                result = sdata + "=" + sdata2;
                break;
            case 8:
                event = "monoColor";
                result = sdata + "=" + parseColor(data);
                hasDelay = true;
                break;
            case 9:
                List<String> colors = Arrays.asList(parseColor(Integer.valueOf(sdata2).intValue()), parseColor(data));
                event = "doubleColor";
                result = sdata + "=" + colors.toString();
                hasDelay = true;
                break;
            case 10:
                event = "themeColor";
                result = sdata + "=" + sdata2;
                hasDelay = true;
                break;
            case 11:
                event = "customColor";
                result = sdata + "-" + sdata2 + "=" + parseColor(data);
                hasDelay = true;
                break;
        }
        if (event != null && result != null) {
            BiAmbient biAmbient = new BiAmbient(event, result, owner);
            Message message = this.mHandler.obtainMessage();
            message.what = type + 100;
            message.obj = biAmbient;
            if (hasDelay) {
                this.mHandler.removeMessages(type + 100);
                this.mHandler.sendMessageDelayed(message, 10000L);
                return;
            }
            this.mHandler.sendMessage(message);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showAmbientLightEvent(int type, String sdata, String sdata2, int data, String owner) {
        if (this.hasEnable || type == 1) {
            sendBiAmbient(type, sdata, sdata2, data, owner);
        }
        switch (type) {
            case 0:
                StringBuilder sb = new StringBuilder();
                sb.append("onRequest: ");
                sb.append(data == 1 ? "apply" : "release");
                LogUtil.d(TAG, sb.toString());
                break;
            case 1:
                StringBuilder sb2 = new StringBuilder();
                sb2.append("onChangeSwitch: ");
                sb2.append(data == 1 ? "open" : HttpHeaders.HEAD_VALUE_CONNECTION_CLOSE);
                LogUtil.d(TAG, sb2.toString());
                break;
            case 2:
                LogUtil.d(TAG, "onPlayEffect: " + sdata + "=" + sdata2);
                break;
            case 3:
                LogUtil.d(TAG, "onStopEffect: " + sdata + "=" + sdata2);
                break;
            case 4:
                StringBuilder sb3 = new StringBuilder();
                sb3.append(data == 0 ? "onChangeMode: " : "onAddMode: ");
                sb3.append(sdata);
                sb3.append("=");
                sb3.append(sdata2);
                LogUtil.d(TAG, sb3.toString());
                break;
            case 5:
                LogUtil.d(TAG, "onSubMode: " + sdata + "=" + sdata2);
                break;
            case 6:
                LogUtil.d(TAG, "onChangeBright: " + sdata + "=" + data);
                break;
            case 7:
                LogUtil.d(TAG, "onChangeColorType: " + sdata + "=" + sdata2);
                break;
            case 8:
                LogUtil.d(TAG, "onChangeMonoColor: " + sdata + "=" + parseColor(data));
                break;
            case 9:
                LogUtil.d(TAG, "onChangeDoubleColor: " + sdata + "=" + parseColor(Integer.valueOf(sdata2).intValue()) + ", " + parseColor(data));
                break;
            case 10:
                LogUtil.d(TAG, "onChangeThemeColor: " + sdata + "=" + sdata2);
                break;
            case 11:
                LogUtil.d(TAG, "onChangeRegionColor: " + sdata + "-" + sdata2 + "=" + parseColor(data));
                break;
            case 12:
                LogUtil.d(TAG, "onChangeRegionBright: " + sdata + "-" + sdata2 + "=" + parseColor(data));
                break;
            case 13:
                StringBuilder sb4 = new StringBuilder();
                sb4.append(data == 2 ? "onErrorPlay: " : "onErrorStop: ");
                sb4.append(sdata);
                sb4.append("=");
                sb4.append(sdata2);
                LogUtil.d(TAG, sb4.toString());
                break;
            case 14:
                StringBuilder sb5 = new StringBuilder();
                sb5.append(data == 4 ? "onErrorSet: " : "onErrorSub: ");
                sb5.append(sdata);
                sb5.append("=");
                sb5.append(sdata2);
                LogUtil.d(TAG, sb5.toString());
                break;
        }
        if (!this.mListener.isEmpty()) {
            synchronized (this.mListener) {
                for (AmbientLightListener listener : this.mListener) {
                    listener.onAmbientLightEvent(type, sdata, sdata2, data);
                }
            }
        }
    }

    private void registerObserver() {
        this.hasSwitchOn = Settings.System.getInt(this.mContext.getContentResolver(), KEY_ATL_SW, 0) == 1;
        LogUtil.i(TAG, "registerObserver, hasSwitchOn=" + this.hasSwitchOn);
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor(KEY_ATL_SW), true, new ContentObserver(new XuiWorkHandler()) { // from class: com.xiaopeng.xuiservice.capabilities.ambientlight.XpAmbientLightService.2
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                XpAmbientLightService xpAmbientLightService = XpAmbientLightService.this;
                int i = 0;
                xpAmbientLightService.hasSwitchOn = Settings.System.getInt(xpAmbientLightService.mContext.getContentResolver(), XpAmbientLightService.KEY_ATL_SW, 0) == 1;
                XpAmbientLightService xpAmbientLightService2 = XpAmbientLightService.this;
                if (xpAmbientLightService2.getRawCarSpeed() > 3.0f) {
                    if (!XpAmbientLightService.this.hasSwitchOn) {
                        i = 2;
                    } else if (XpAmbientLightService.this.hasDoorLight) {
                        i = 3;
                    }
                }
                xpAmbientLightService2.mGroup = i;
                LogUtil.i(XpAmbientLightService.TAG, "onChange, hasSwitchOn=" + XpAmbientLightService.this.hasSwitchOn + ", mGroup=" + XpAmbientLightService.this.mGroup);
            }
        });
    }

    private void loadAmbientModes(String path) {
        List<String> files = FileUtils.getAllFiles(path, "json");
        for (String file : files) {
            Effect effect = new Effect().readFromJson(file);
            if (effect != null && checkMode(effect.name)) {
                this.mModeMap.put(effect.name, effect);
            }
        }
        LogUtil.i(TAG, "modes: " + new ArrayList(this.mModeMap.keySet()));
    }

    private void loadThemesColor(String path) {
        List<String> files = FileUtils.getAllFiles(path, "json");
        for (String file : files) {
            ThemeColor themeColor = new ThemeColor().readFromJson(file);
            if (themeColor != null && !this.mThemeMap.containsKey(themeColor.name)) {
                this.mThemeMap.put(themeColor.name, themeColor);
            }
        }
        LogUtil.i(TAG, "themesColor: " + new ArrayList(this.mThemeMap.keySet()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int parseSpeedBright(float speed) {
        if (speed >= 80.0f) {
            return 100;
        }
        if (speed >= 50.0f) {
            return (int) ((((speed - 50.0f) / 30.0f) * 50.0f) + 50.0f);
        }
        if (speed >= 10.0f) {
            return (int) speed;
        }
        return 10;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getRawCarSpeed() {
        CarVcuManager vcu = this.mCar.getCarManager("xp_vcu");
        try {
            return vcu.getRawCarSpeed();
        } catch (Exception e) {
            return 0.0f;
        }
    }

    private int getIgStatusFromMcu() {
        CarMcuManager mcu = this.mCar.getCarManager("xp_mcu");
        try {
            return mcu.getIgStatusFromMcu();
        } catch (Exception e) {
            return 0;
        }
    }

    private int getMcuAtlsStatus() {
        CarMcuManager mcu = this.mCar.getCarManager("xp_mcu");
        try {
            return mcu.getMcuAtlsStatus();
        } catch (Exception e) {
            return 3;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addMcuManagerCallback() {
        boolean z = false;
        this.hasIgOn = getIgStatusFromMcu() == 1;
        if (!this.hasController && !this.hasLite && getMcuAtlsStatus() != 3) {
            z = true;
        }
        this.hasMcuOn = z;
        LogUtil.i(TAG, "addMcuManagerCallback, hasIgOn=" + this.hasIgOn + ", hasMcuOn=" + this.hasMcuOn);
        this.mMcuCallback = new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.capabilities.ambientlight.XpAmbientLightService.3
            public void onChangeEvent(CarPropertyValue value) {
                LogUtil.d(XpAmbientLightService.TAG, "onChangeEvent(mcu) " + value.toString() + ", hasEnable=" + XpAmbientLightService.this.hasEnable);
                int propertyId = value.getPropertyId();
                if (propertyId == 557847561) {
                    boolean status = ((Integer) value.getValue()).intValue() == 1;
                    if (XpAmbientLightService.this.hasIgOn != status) {
                        String owner = PackageUtils.getPackageName(Binder.getCallingPid());
                        if (!XpAmbientLightService.this.hasMcuOn) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("onChangeEvent, hasIgOn=");
                            sb.append(status);
                            sb.append(", ");
                            sb.append(status ? "recovery" : "interrupt");
                            sb.append(" Ambient");
                            LogUtil.i(XpAmbientLightService.TAG, sb.toString());
                            XpAmbientLightService xpAmbientLightService = XpAmbientLightService.this;
                            xpAmbientLightService.sendAmbientMessage(15, new AmbientMessage(status, owner));
                        }
                        XpAmbientLightService.this.hasIgOn = status;
                    }
                } else if (propertyId == 557847614) {
                    int val = ((Integer) value.getValue()).intValue();
                    if (XpAmbientLightService.this.hasController || XpAmbientLightService.this.hasLite) {
                        return;
                    }
                    if (val == 1 || val == 3) {
                        boolean status2 = val != 3;
                        if (XpAmbientLightService.this.hasMcuOn != status2) {
                            String owner2 = PackageUtils.getPackageName(Binder.getCallingPid());
                            if (XpAmbientLightService.this.hasIgOn) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("onChangeEvent, hasMcuOn=");
                                sb2.append(status2);
                                sb2.append(", ");
                                sb2.append(status2 ? "interrupt" : "recovery");
                                sb2.append(" Ambient");
                                LogUtil.i(XpAmbientLightService.TAG, sb2.toString());
                                XpAmbientLightService xpAmbientLightService2 = XpAmbientLightService.this;
                                xpAmbientLightService2.sendAmbientMessage(15, new AmbientMessage(status2 ? false : true, owner2));
                            }
                            XpAmbientLightService.this.hasMcuOn = status2;
                        }
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        this.mCar.addMcuManagerListener(this.mMcuCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getHvacColor(int type) {
        if (type != 2) {
            return type != 3 ? 6 : 3;
        }
        return 14;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getHvacBright(int color, float temp) {
        return Math.round(((color == 3 ? temp - 17.0f : 33.0f - temp) * 100.0f) / 15.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getHvacPsnBright(int color, float temp) {
        return Math.round(((color == 3 ? temp - 17.0f : 33.0f - temp) * 100.0f) / 15.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addHvacManagerCallback() {
        if (this.hasAirConditioner) {
            initHvacColorAndBright();
            this.mHvacCallback = new CarHvacManager.CarHvacEventCallback() { // from class: com.xiaopeng.xuiservice.capabilities.ambientlight.XpAmbientLightService.4
                public void onChangeEvent(CarPropertyValue value) {
                    int propertyId = value.getPropertyId();
                    if (propertyId == 358614275) {
                        XpAmbientLightService xpAmbientLightService = XpAmbientLightService.this;
                        xpAmbientLightService.mHvacBright = xpAmbientLightService.getHvacBright(xpAmbientLightService.mHvacColor, ((Float) value.getValue()).floatValue());
                        XpAmbientLightService.this.TICK_HVAC = 60;
                        LogUtil.d(XpAmbientLightService.TAG, "onChangeHvacBright, mHvacBright=" + XpAmbientLightService.this.mHvacBright);
                    } else if (propertyId == 557849120) {
                        XpAmbientLightService xpAmbientLightService2 = XpAmbientLightService.this;
                        xpAmbientLightService2.mHvacColor = xpAmbientLightService2.getHvacColor(((Integer) value.getValue()).intValue());
                    } else if (propertyId == 559946242) {
                        XpAmbientLightService xpAmbientLightService3 = XpAmbientLightService.this;
                        xpAmbientLightService3.mHvacPsnBright = xpAmbientLightService3.getHvacPsnBright(xpAmbientLightService3.mHvacColor, ((Float) value.getValue()).floatValue());
                        XpAmbientLightService.this.TICK_HVAC = 60;
                        LogUtil.d(XpAmbientLightService.TAG, "onChangePsnHvacBright, mHvacPsnBright=" + XpAmbientLightService.this.mHvacPsnBright);
                    }
                }

                public void onErrorEvent(int propertyId, int zone) {
                }
            };
            this.mCar.addHvacManagerListener(this.mHvacCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startCaptureHue(int displayId) {
        if (this.mCatchHueUtils != null && !this.hasCatchHue) {
            LogUtil.i(TAG, "startCaptureHue, displayId=" + displayId);
            this.mCatchHueUtils.registerHueDataListener(displayId, this.mHueDataListener);
            this.mDisplayId = displayId;
            this.hasCatchHue = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopCaptureHue(int displayId) {
        if (this.mCatchHueUtils != null && this.hasCatchHue) {
            LogUtil.i(TAG, "stopCaptureHue, displayId=" + displayId);
            this.mCatchHueUtils.unregisterHueDataListener(displayId, this.mHueDataListener);
            this.hasCatchHue = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startCaptureRatio() {
        if (this.mVisualizerService != null && !this.hasVisualizer) {
            LogUtil.i(TAG, "startCaptureRatio");
            this.mVisualizerService.registerFftDataListener(this.mSDFftDataListener);
            this.mRhythmBright = 100;
            this.hasVisualizer = true;
            this.hasRhythmBrightChange = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopCaptureRatio() {
        if (this.mVisualizerService != null && this.hasVisualizer) {
            LogUtil.i(TAG, "stopCaptureRatio");
            this.mVisualizerService.unregisterFftDataListener(this.mSDFftDataListener);
            this.hasVisualizer = false;
        }
    }

    private int getIntProvider(String key, int val) {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), key, val);
    }

    private String getStringProvider(String key, String val) {
        String res = Settings.System.getString(ActivityThread.currentActivityThread().getApplication().getContentResolver(), key);
        return !TextUtils.isEmpty(res) ? res : val;
    }

    private void setProviderPartitions(Set<String> partitions) {
        Settings.System.putString(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "ambientPartitions", String.join(",", partitions));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Set<String> getProviderPartitions() {
        String partitions = Settings.System.getString(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "ambientPartitions");
        return TextUtils.isEmpty(partitions) ? new ArraySet(Arrays.asList(Actions.ACTION_ALL)) : (Set) Arrays.asList(partitions.split(",")).stream().collect(Collectors.toSet());
    }

    private void setProviderMode(String partition, String mode) {
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        Settings.System.putString(contentResolver, "ambientMode_" + partition, mode);
    }

    private String getDefaultMode(final String mode) {
        Optional<String> result = this.mModes.stream().filter(new Predicate() { // from class: com.xiaopeng.xuiservice.capabilities.ambientlight.-$$Lambda$XpAmbientLightService$6xhf8TTZ-m8On3FSTSesbioTZ74
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean contains;
                contains = mode.contains((String) obj);
                return contains;
            }
        }).findFirst();
        return result.isPresent() ? result.get() : "stable";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getProviderMode(String partition) {
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        String mode = Settings.System.getString(contentResolver, "ambientMode_" + partition);
        return checkMode(mode) ? mode : getDefaultMode(getStringProvider("AmbientLightEffectType", "stable"));
    }

    private void setProviderColorType(String partition, String mode, String colorType) {
        String str;
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        StringBuilder sb = new StringBuilder();
        sb.append("ambientColorType_");
        sb.append(partition);
        if (this.mModeMap.get(mode).hasMemColorType) {
            str = "_" + mode;
        } else {
            str = "";
        }
        sb.append(str);
        Settings.System.putString(contentResolver, sb.toString(), colorType);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getProviderColorType(String partition, String mode) {
        String str;
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        StringBuilder sb = new StringBuilder();
        sb.append("ambientColorType_");
        sb.append(partition);
        if (this.mModeMap.get(mode).hasMemColorType) {
            str = "_" + mode;
        } else {
            str = "";
        }
        sb.append(str);
        String colorType = Settings.System.getString(contentResolver, sb.toString());
        return this.mColorTypes.contains(colorType) ? colorType : (!hasSupportColorType(mode, "double") || getIntProvider("isDoubleColorEnable", 0) == 0) ? "mono" : "double";
    }

    private void setProviderMonoColor(String partition, int color) {
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        Settings.System.putInt(contentResolver, "ambientMonoColor_" + partition, color);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getProviderMonoColor(String partition) {
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        return Settings.System.getInt(contentResolver, "ambientMonoColor_" + partition, getIntProvider("MonoColor", 1));
    }

    private void setProviderDoubleColor(String partition, Pair<Integer, Integer> color) {
        long colors = ((Integer) color.first).intValue() | (((Integer) color.second).intValue() << 32);
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        Settings.System.putLong(contentResolver, "ambientDoubleColor_" + partition, colors);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Pair<Integer, Integer> getProviderDoubleColor(String partition) {
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        long colors = Settings.System.getLong(contentResolver, "ambientDoubleColor_" + partition, getIntProvider("DoubleFirstColor", 1) | (getIntProvider("DoubleSecondColor", 6) << 32));
        return new Pair<>(Integer.valueOf((int) ((-1) & colors)), Integer.valueOf((int) (colors >>> 32)));
    }

    private void setProviderThemeColor(String partition, String themeColor) {
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        Settings.System.putString(contentResolver, "ambientThemeColor_" + partition, themeColor);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getProviderThemeColor(String partition) {
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        String themeColor = Settings.System.getString(contentResolver, "ambientThemeColor_" + partition);
        return this.mThemeMap.containsKey(themeColor) ? themeColor : "sea";
    }

    private void setProviderRegionColor(String partition, String region, int color) {
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        Settings.System.putInt(contentResolver, "ambientColor_" + partition + "_" + region, color);
    }

    private int getProviderRegionColor(String partition, String region) {
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        return Settings.System.getInt(contentResolver, "ambientColor_" + partition + "_" + region, 1);
    }

    private void setProviderRegionBright(String partition, String region, int bright) {
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        Settings.System.putInt(contentResolver, "ambientBright_" + partition + "_" + region, bright);
    }

    private int getProviderRegionBright(String partition, String region) {
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        return Settings.System.getInt(contentResolver, "ambientBright_" + partition + "_" + region, 100);
    }

    private void setProviderBright(String partition, int bright) {
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        Settings.System.putInt(contentResolver, "ambientBright_" + partition, bright);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getProviderBright(String partition) {
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        return Settings.System.getInt(contentResolver, "ambientBright_" + partition, getIntProvider("AmbientLightBright", 100));
    }

    private void setProviderEnable(boolean status) {
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "ambientEnable", status ? 1 : 0);
    }

    private boolean getProviderEnable() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "ambientEnable", getIntProvider("isAmbientLightOpen", 0)) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSHCReq(int status) {
        CarBcmManager bcm = this.mCar.getCarManager("xp_bcm");
        LogUtil.d(TAG, "setSHCReq" + status);
        try {
            bcm.setShcReq(status);
        } catch (Exception e) {
            LogUtil.e(TAG, "setSHCReq " + status + " failed, " + e);
        }
    }

    private void setAtlConfiguration(AtlConfiguration config) {
        CarAtlManager atl = this.mCar.getCarManager("xp_atl");
        try {
            atl.setAtlConfiguration(config);
        } catch (Exception e) {
            LogUtil.e(TAG, "setAtlConfiguration failed, " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAtlOpen(int status) {
        CarAtlManager atl = this.mCar.getCarManager("xp_atl");
        LogUtil.d(TAG, "setAtlOpen " + status);
        if (status == 1) {
            try {
                if (!this.hasController && !this.hasLite) {
                    byte[] data = {0, 0};
                    atl.setTwoLightData((byte) 0, data, false, data, data, data);
                }
            } catch (Exception e) {
                LogUtil.e(TAG, "setAtlOpen " + status + " failed, " + e);
                return;
            }
        }
        atl.setAtlOpen(status);
    }

    private void initHvacColorAndBright() {
        CarHvacManager hvac = this.mCar.getCarManager("hvac");
        try {
            this.mHvacColor = getHvacColor(hvac.getWindModColor());
            this.mHvacBright = getHvacBright(this.mHvacColor, hvac.getHvacTempDriverValue());
            this.mHvacPsnBright = getHvacPsnBright(this.mHvacColor, hvac.getHvacTempPsnValue());
        } catch (Exception e) {
            LogUtil.e(TAG, "initHvacColorAndBright failed, " + e);
        }
        LogUtil.d(TAG, "initHvacColorAndBright, color=" + this.mHvacColor + ", bright=(" + this.mHvacBright + ", " + this.mHvacPsnBright + ")");
    }

    private int writeBytesFade(boolean enable, boolean hold, int fade, byte[] bytes) {
        if (bytes.length >= 4) {
            bytes[2] = (byte) (((enable ? 1 : 0) << 1) | ((!hold ? 1 : 0) << 2));
            bytes[3] = (byte) ((fade / 20) & 250);
        }
        return bytes.length >= 4 ? 0 : 1;
    }

    private int writeBytesAddr(int addr, byte[] bytes) {
        if (bytes.length >= 2) {
            bytes[0] = (byte) (addr & 255);
            bytes[1] = (byte) (addr >>> 8);
        }
        return bytes.length >= 2 ? 0 : 1;
    }

    private int writeBytesBright(int bright, byte[] bytes) {
        if (bytes.length >= 5) {
            bytes[4] = (byte) (bright & 127);
        }
        return bytes.length >= 5 ? 0 : 1;
    }

    private int writeBytesColor(int color, byte[] bytes) {
        if (bytes.length >= 8) {
            boolean rgb = hasRgbColor(color);
            bytes[2] = (byte) (rgb ? bytes[2] | 1 : bytes[2] & (-2));
            if (rgb) {
                bytes[5] = (byte) ((color >> 16) & 255);
                bytes[6] = (byte) ((color >> 8) & 255);
                bytes[7] = (byte) (color & 255);
            } else {
                bytes[5] = (byte) ((color > 0 ? color - 1 : 0) & 31);
            }
        }
        return bytes.length >= 8 ? 0 : 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setGroupLightData(int mask, int group, Map<String, Integer> colors, Map<String, Integer> brights, int fade) {
        String region;
        String region2;
        int color = colors.containsKey(Actions.ACTION_ALL) ? colors.get(Actions.ACTION_ALL).intValue() : 0;
        int bright = brights.containsKey(Actions.ACTION_ALL) ? brights.get(Actions.ACTION_ALL).intValue() : 0;
        boolean hold = true;
        boolean enable = (((mask + group) + color) + bright) + fade != 0;
        if (fade != 0) {
            hold = false;
        }
        CarAtlManager atl = this.mCar.getCarManager("xp_atl");
        LogUtil.d(TAG, "setGroupLightData, mask=" + mask + ", group=" + group + ", colors=[" + parseColorMap(colors) + "], brights=[" + parseBrightMap(brights) + "], fade=" + fade);
        try {
            try {
                if (this.hasController) {
                    try {
                        if (this.mController != null) {
                            try {
                                if (!this.mController.lightsMap.containsKey(Integer.valueOf(group)) && !this.mController.lightsMap.containsKey(Integer.valueOf(mask))) {
                                    throw new Exception("Invalid group " + group);
                                }
                            } catch (Exception e) {
                                e = e;
                                LogUtil.e(TAG, "setGroupLightData failed, " + e);
                            }
                        }
                        byte[] bytes = new byte[8];
                        Lin lin = this.mController.lightsMap.get(Integer.valueOf(group));
                        Lin mLin = this.mController.lightsMap.get(Integer.valueOf(mask));
                        int ret = (brights.containsKey(Actions.ACTION_ALL) ? writeBytesBright(brights.get(Actions.ACTION_ALL).intValue(), bytes) : 0) | writeBytesFade(enable, hold, fade, bytes) | (colors.containsKey(Actions.ACTION_ALL) ? writeBytesColor(colors.get(Actions.ACTION_ALL).intValue(), bytes) : 0);
                        if (!TextUtils.isEmpty(lin.lin1) && !TextUtils.isEmpty(mLin.lin1)) {
                            int address = Integer.valueOf(mLin.lin1, 16).intValue() & Integer.valueOf(lin.lin1, 16).intValue();
                            String region3 = this.mRegionMap.containsKey("lin1") ? this.mRegionMap.get("lin1") : null;
                            int ret2 = ret | writeBytesAddr(address, bytes) | (brights.containsKey(region3) ? writeBytesBright(brights.get(region3).intValue(), bytes) : 0) | (colors.containsKey(region3) ? writeBytesColor(colors.get(region3).intValue(), bytes) : 0);
                            int hash = Arrays.hashCode(bytes);
                            if (this.mHashCodeMap.containsKey("lin1") && this.mHashCodeMap.get("lin1").intValue() == hash) {
                                region2 = region3;
                                ret = ret2;
                            }
                            StringBuilder sb = new StringBuilder();
                            region2 = region3;
                            sb.append("SetLin1Data ");
                            sb.append(Arrays.toString(bytes));
                            LogUtil.d(TAG, sb.toString());
                            atl.setAtlLin1Data(bytes);
                            this.mHashCodeMap.put("lin1", Integer.valueOf(hash));
                            ret = ret2;
                        }
                        if (!TextUtils.isEmpty(lin.lin2) && !TextUtils.isEmpty(mLin.lin2)) {
                            int address2 = Integer.valueOf(lin.lin2, 16).intValue() & Integer.valueOf(mLin.lin2, 16).intValue();
                            String region4 = this.mRegionMap.containsKey("lin2") ? this.mRegionMap.get("lin2") : null;
                            int ret3 = ret | writeBytesAddr(address2, bytes) | (brights.containsKey(region4) ? writeBytesBright(brights.get(region4).intValue(), bytes) : 0) | (colors.containsKey(region4) ? writeBytesColor(colors.get(region4).intValue(), bytes) : 0);
                            int hash2 = Arrays.hashCode(bytes);
                            if (this.mHashCodeMap.containsKey("lin2") && this.mHashCodeMap.get("lin2").intValue() == hash2) {
                                region = region4;
                                ret = ret3;
                            }
                            StringBuilder sb2 = new StringBuilder();
                            region = region4;
                            sb2.append("SetLin2Data ");
                            sb2.append(Arrays.toString(bytes));
                            LogUtil.d(TAG, sb2.toString());
                            atl.setAtlLin2Data(bytes);
                            this.mHashCodeMap.put("lin2", Integer.valueOf(hash2));
                            ret = ret3;
                        }
                        if (!TextUtils.isEmpty(lin.lin3) && !TextUtils.isEmpty(mLin.lin3)) {
                            int address3 = Integer.valueOf(lin.lin3, 16).intValue() & Integer.valueOf(mLin.lin3, 16).intValue();
                            String region5 = this.mRegionMap.containsKey("lin3") ? this.mRegionMap.get("lin3") : null;
                            int writeBytesAddr = ret | writeBytesAddr(address3, bytes) | (brights.containsKey(region5) ? writeBytesBright(brights.get(region5).intValue(), bytes) : 0) | (colors.containsKey(region5) ? writeBytesColor(colors.get(region5).intValue(), bytes) : 0);
                            int hash3 = Arrays.hashCode(bytes);
                            if (!this.mHashCodeMap.containsKey("lin3") || this.mHashCodeMap.get("lin3").intValue() != hash3) {
                                LogUtil.d(TAG, "SetLin3Data " + Arrays.toString(bytes));
                                atl.setAtlLin3Data(bytes);
                                this.mHashCodeMap.put("lin3", Integer.valueOf(hash3));
                            }
                        }
                    } catch (Exception e2) {
                        e = e2;
                    }
                } else {
                    boolean hold2 = hold;
                    try {
                        if (this.hasLite) {
                            this.mBuilder.isLedEnabled(enable);
                            this.mBuilder.rgbRedOrPreDefinedColor(color);
                            atl.setAtlConfiguration(this.mBuilder.build());
                        } else {
                            try {
                                atl.setGroutLightData((byte) group, (byte) 0, 0, hold2, (byte) (enable ? color : 255), (byte) bright, (byte) ((fade / 20) & 250));
                            } catch (Exception e3) {
                                e = e3;
                                LogUtil.e(TAG, "setGroupLightData failed, " + e);
                            }
                        }
                    } catch (Exception e4) {
                        e = e4;
                    }
                }
            } catch (Exception e5) {
                e = e5;
            }
        } catch (Exception e6) {
            e = e6;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int sendAmbientMessage(int type, AmbientMessage ambientMessage) {
        Message message = this.mHandler.obtainMessage();
        message.what = type;
        message.obj = ambientMessage;
        return (this.hasInit && this.mHandler.sendMessage(message)) ? 0 : -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isRunning() {
        ScheduledFuture<?> scheduledFuture = this.mFuture;
        return (scheduledFuture == null || scheduledFuture.isDone()) ? false : true;
    }

    private ScheduledFuture<?> runTasklet(Tasklet task) {
        try {
            return this.mExecutor.scheduleAtFixedRate(task, 100L, 50L, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            LogUtil.e(TAG, "runTasklet failed, " + e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean scheduledTasklet(String partition, Effect effect, String owner) {
        this.mTask = new Tasklet(new EffectTask(partition, effect, owner));
        this.mFuture = runTasklet(this.mTask);
        return this.mFuture != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePlayEffect(String partition, Effect effect, String owner) {
        EffectTask drop;
        boolean res;
        Pair<String, String> playEffect = new Pair<>(partition, effect.name);
        EffectTask drop2 = null;
        LogUtil.i(TAG, "playEffect: " + partition + "=" + effect.name);
        if (isRunning()) {
            if (effect.priority < this.mTask.priority) {
                drop = null;
                res = false;
            } else {
                Iterator<EffectTask> iter = this.mTask.iteratorTasks();
                while (iter.hasNext()) {
                    EffectTask task = iter.next();
                    if (task.hasMode) {
                        task.overlap = partition;
                    } else if (!task.hasStop()) {
                        drop2 = task;
                    }
                }
                boolean res2 = this.mTask.addEffect(new EffectTask(partition, effect, owner));
                drop = drop2;
                res = res2;
            }
        } else {
            boolean res3 = scheduledTasklet(partition, effect, owner);
            drop = null;
            res = res3;
        }
        if (res) {
            if (drop != null) {
                this.mTask.stopEffect(drop);
                showAmbientLightEvent(3, drop.partition, drop.effect.name, 0, owner);
            }
            this.mTask.priority = effect.priority;
            this.mPlayEffect = playEffect;
            showAmbientLightEvent(2, partition, effect.name, 0, owner);
            return;
        }
        showAmbientLightEvent(13, partition, effect.name, 2, owner);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStopEffect(String owner) {
        if (isRunning() && this.mPlayEffect != null) {
            LogUtil.i(TAG, "stopEffect: " + ((String) this.mPlayEffect.first) + "=" + ((String) this.mPlayEffect.second));
            Iterator<EffectTask> iter = this.mTask.iteratorTasks();
            while (iter.hasNext()) {
                EffectTask task = iter.next();
                String name = task.effect.name;
                if (task.hasMode) {
                    task.overlap = new String();
                } else if (!task.hasStop()) {
                    this.mTask.stopEffect(task);
                    this.mTask.priority = 0;
                    showAmbientLightEvent(3, task.partition, name, 0, owner);
                }
            }
            this.mPlayEffect = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSetAmbientLightMode(String partition, String mode, String owner) {
        String destColorType;
        String srcColorType;
        String str;
        Iterator<EffectTask> iter;
        boolean hasAppend = !Actions.ACTION_ALL.equals(partition) && this.mStatusMap.containsKey(Actions.ACTION_ALL);
        String prevMode = getProviderMode(partition);
        if (Actions.ACTION_ALL.equals(partition) && !this.mStatusMap.containsKey(Actions.ACTION_ALL)) {
            LogUtil.e(TAG, "setMode: " + partition + "=" + mode + " failed!");
            showAmbientLightEvent(14, partition, mode, 4, owner);
        } else if (hasAppend || !mode.equals(prevMode)) {
            String split = "front".equals(partition) ? "rear" : "front";
            String srcColorType2 = getProviderColorType(partition, prevMode);
            String destColorType2 = getProviderColorType(partition, mode);
            String destColorType3 = hasSupportColorType(mode, destColorType2) ? destColorType2 : "mono";
            if (this.hasEnable && !this.hasApply && isRunning()) {
                EffectTask drop = null;
                EffectTask append = new EffectTask(partition, this.mModeMap.get(mode), owner);
                Iterator<EffectTask> iter2 = this.mTask.iteratorTasks();
                while (iter2.hasNext()) {
                    EffectTask task = iter2.next();
                    if (task.hasMode) {
                        iter = iter2;
                        if (!partition.equals(task.partition)) {
                            if (Actions.ACTION_ALL.equals(task.partition)) {
                                task.partition = split;
                                task.part = split;
                            }
                        } else {
                            drop = task;
                        }
                    } else {
                        iter = iter2;
                        if (!task.hasStop()) {
                            append.overlap = task.partition;
                        }
                    }
                    iter2 = iter;
                }
                this.mTask.addEffect(append);
                if (drop != null) {
                    this.mTask.stopEffect(drop);
                }
            }
            if (!hasAppend) {
                destColorType = destColorType3;
                srcColorType = srcColorType2;
            } else {
                String name = this.mStatusMap.get(Actions.ACTION_ALL);
                this.mStatusMap.remove(Actions.ACTION_ALL);
                this.mStatusMap.put(split, name);
                setProviderMode(split, name);
                destColorType = destColorType3;
                srcColorType = srcColorType2;
                showAmbientLightEvent(4, split, name, 0, owner);
            }
            this.mStatusMap.put(partition, mode);
            setProviderMode(partition, mode);
            setProviderPartitions(this.mStatusMap.keySet());
            String destColorType4 = destColorType;
            if (!destColorType4.equals(srcColorType)) {
                setProviderColorType(partition, mode, destColorType4);
                this.mColorTypeMap.put(partition, destColorType4);
                showAmbientLightEvent(7, partition, destColorType4, 0, owner);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("SetMode: ");
            sb.append(partition);
            sb.append("=");
            sb.append(prevMode);
            sb.append(" -> ");
            sb.append(mode);
            sb.append(" (");
            sb.append(this.mSnapshotStatusMap);
            sb.append(" -> ");
            sb.append(this.mStatusMap);
            sb.append(")");
            if (destColorType4.equals(srcColorType)) {
                str = "";
            } else {
                str = ", " + srcColorType + " -> " + destColorType4;
            }
            sb.append(str);
            LogUtil.i(TAG, sb.toString());
            this.mSnapshotStatusMap = (Map) this.mStatusMap.entrySet().stream().collect(Collectors.toMap($$Lambda$CSz_ibwXhtkKNl72Q8tR5oBgkWk.INSTANCE, $$Lambda$etDQhIA8H5hI6BDqsFIFQkLL9Nc.INSTANCE));
            showAmbientLightEvent(4, partition, mode, hasAppend ? 1 : 0, owner);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSubAmbientLightMode(String partition, String mode, String owner) {
        if (Actions.ACTION_ALL.equals(partition) || !mode.equals(this.mStatusMap.get(partition))) {
            LogUtil.e(TAG, "subMode: " + partition + "=" + mode + " failed!");
            showAmbientLightEvent(14, partition, mode, 5, owner);
            return;
        }
        if (this.hasEnable && !this.hasApply && isRunning()) {
            Iterator<EffectTask> iter = this.mTask.iteratorTasks();
            while (iter.hasNext()) {
                EffectTask task = iter.next();
                if (task.hasMode) {
                    if (!partition.equals(task.partition)) {
                        task.partition = Actions.ACTION_ALL;
                        task.part = Actions.ACTION_ALL;
                    } else if (mode.equals(task.effect.name)) {
                        this.mTask.stopEffect(task);
                    }
                }
            }
        }
        String rest = this.mStatusMap.get("front".equals(partition) ? "rear" : "front");
        this.mStatusMap.clear();
        showAmbientLightEvent(5, partition, mode, 0, owner);
        this.mStatusMap.put(Actions.ACTION_ALL, rest);
        setProviderMode(Actions.ACTION_ALL, rest);
        setProviderPartitions(this.mStatusMap.keySet());
        LogUtil.i(TAG, "subMode: " + partition + "=" + mode + " (" + this.mSnapshotStatusMap + " -> " + this.mStatusMap + ")");
        this.mSnapshotStatusMap = (Map) this.mStatusMap.entrySet().stream().collect(Collectors.toMap($$Lambda$CSz_ibwXhtkKNl72Q8tR5oBgkWk.INSTANCE, $$Lambda$etDQhIA8H5hI6BDqsFIFQkLL9Nc.INSTANCE));
        showAmbientLightEvent(4, Actions.ACTION_ALL, rest, 0, owner);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSetAmbientLightMonoColor(String partition, int color, String owner) {
        int last = this.mMonoColorMap.get(partition).intValue();
        LogUtil.i(TAG, "SetMonoColor: " + partition + "=" + parseColor(last) + " -> " + parseColor(color));
        if (color != last) {
            this.hasChange = true;
            this.mMonoColorMap.put(partition, Integer.valueOf(color));
            setProviderMonoColor(partition, color);
            showAmbientLightEvent(8, partition, new String(), color, owner);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSetAmbientLightDoubleColor(String partition, Pair<Integer, Integer> color, String owner) {
        Pair<Integer, Integer> last = this.mDoubleColorMap.get(partition);
        LogUtil.i(TAG, "SetDoubleColor: " + partition + " = (" + parseColor(((Integer) last.first).intValue()) + ", " + parseColor(((Integer) last.second).intValue()) + ") -> (" + parseColor(((Integer) color.first).intValue()) + ", " + parseColor(((Integer) color.second).intValue()) + ")");
        if (!color.equals(last)) {
            this.hasChange = true;
            this.mDoubleColorMap.put(partition, color);
            setProviderDoubleColor(partition, color);
            showAmbientLightEvent(9, partition, String.valueOf(color.first), ((Integer) color.second).intValue(), owner);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSetAmbientLightThemeColor(String partition, String themeColor, String owner) {
        String last = this.mThemeColorMap.get(partition);
        LogUtil.i(TAG, "SetThemeColor: " + partition + "=" + last + " -> " + themeColor);
        if (!themeColor.equals(last)) {
            this.hasChange = true;
            this.mThemeColorMap.put(partition, themeColor);
            setProviderThemeColor(partition, themeColor);
            showAmbientLightEvent(10, partition, themeColor, 0, owner);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSetAmbientLightRegionColor(String partition, String region, int color, String owner) {
        int last = this.mRegionColorMap.get(partition).get(region).intValue();
        LogUtil.i(TAG, "SetCustomColor: " + partition + "_" + region + "=" + parseColor(last) + " -> " + parseColor(color));
        if (color != last) {
            this.hasChange = true;
            this.mRegionColorMap.get(partition).put(region, Integer.valueOf(color));
            setProviderRegionColor(partition, region, color);
            showAmbientLightEvent(11, partition, region, color, owner);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSetAmbientLightRegionBright(String partition, String region, int bright, String owner) {
        int last = this.mRegionBrightMap.get(partition).get(region).intValue();
        LogUtil.i(TAG, "SetCustomBright: " + partition + "_" + region + "=" + last + " -> " + bright);
        if (bright != last) {
            this.hasChange = true;
            this.mRegionBrightMap.get(partition).put(region, Integer.valueOf(bright));
            setProviderRegionBright(partition, region, bright);
            showAmbientLightEvent(12, partition, region, bright, owner);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSetAmbientLightBright(String partition, int bright, String owner) {
        int last = this.mDutyCycleMap.get(partition).intValue();
        LogUtil.i(TAG, "SetBright: " + partition + "=" + last + " -> " + bright);
        if (bright != last) {
            this.hasChange = true;
            this.mDutyCycleMap.put(partition, Integer.valueOf(bright));
            setProviderBright(partition, bright);
            showAmbientLightEvent(6, partition, new String(), bright, owner);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSetAmbientLightColorType(String partition, String colorType, String owner) {
        String last = this.mColorTypeMap.get(partition);
        String mode = getProviderMode(partition);
        LogUtil.i(TAG, "SetColorType: " + partition + "=" + last + " -> " + colorType);
        if (!colorType.equals(last) && hasSupportColorType(mode, colorType)) {
            this.hasChange = true;
            this.mColorTypeMap.put(partition, colorType);
            setProviderColorType(partition, mode, colorType);
            showAmbientLightEvent(7, partition, colorType, 0, owner);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSetAmbientLightEnable(boolean enable, String owner) {
        LogUtil.i(TAG, "SetAmbientLightEnable: " + this.hasEnable + " -> " + enable);
        if (enable != this.hasEnable) {
            boolean hasActive = enable && this.hasIgOn && !this.hasMcuOn;
            if (isRunning()) {
                stopCaptureRatio();
                stopCaptureHue(this.mDisplayId);
                this.mFuture.cancel(true);
                setGroupLightData(0, 0, toMaps(0), toMaps(0), 0);
                Pair<String, String> pair = this.mPlayEffect;
                if (pair != null) {
                    showAmbientLightEvent(3, (String) pair.first, (String) this.mPlayEffect.second, 0, owner);
                    this.mPlayEffect = null;
                }
            }
            if (this.hasApply) {
                this.hasApply = false;
                showAmbientLightEvent(0, new String(), new String(), 0, owner);
            }
            if (!this.hasLite) {
                setAtlOpen(hasActive ? 1 : 0);
            } else if (hasActive) {
                setSHCReq(1);
            }
            if (hasActive && !this.mStatusMap.isEmpty()) {
                for (Map.Entry<String, String> entry : this.mStatusMap.entrySet()) {
                    String partition = entry.getKey();
                    Effect effect = this.mModeMap.get(entry.getValue());
                    if (!isRunning()) {
                        scheduledTasklet(partition, effect, owner);
                    } else {
                        this.mTask.addEffect(new EffectTask(partition, effect, owner));
                    }
                }
            }
            this.hasEnable = enable;
            setProviderEnable(enable);
            showAmbientLightEvent(1, new String(), new String(), enable ? 1 : 0, owner);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSetAmbientLight(boolean hasStart, String owner) {
        if (this.hasEnable || this.hasApply) {
            StringBuilder sb = new StringBuilder();
            sb.append(hasStart ? "start" : "stop");
            sb.append(" AmbientLight");
            LogUtil.i(TAG, sb.toString());
            int i = 1;
            if (isRunning()) {
                stopCaptureRatio();
                stopCaptureHue(this.mDisplayId);
                this.mFuture.cancel(true);
                setGroupLightData(0, 0, toMaps(0), toMaps(0), 0);
                Pair<String, String> pair = this.mPlayEffect;
                if (pair != null) {
                    showAmbientLightEvent(3, (String) pair.first, (String) this.mPlayEffect.second, 0, owner);
                    this.mPlayEffect = null;
                }
            }
            if (this.hasApply) {
                showAmbientLightEvent(0, new String(), new String(), 0, owner);
                this.hasApply = false;
            }
            if (!this.hasLite) {
                setAtlOpen((this.hasEnable && hasStart) ? 0 : 0);
            } else if (!hasStart) {
                setSHCReq(2);
            } else if (this.hasEnable) {
                setSHCReq(1);
            }
            if (this.hasEnable && hasStart && !this.mStatusMap.isEmpty()) {
                for (Map.Entry<String, String> entry : this.mStatusMap.entrySet()) {
                    String partition = entry.getKey();
                    Effect effect = this.mModeMap.get(entry.getValue());
                    if (!isRunning()) {
                        scheduledTasklet(partition, effect, "init");
                    } else {
                        this.mTask.addEffect(new EffectTask(partition, effect, "init"));
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRequestAmbientLight(boolean apply, String owner) {
        LogUtil.i(TAG, "RequestPermission: " + this.hasApply + " -> " + apply);
        if (apply != this.hasApply) {
            int i = 1;
            if (isRunning()) {
                stopCaptureRatio();
                stopCaptureHue(this.mDisplayId);
                this.mFuture.cancel(true);
                setGroupLightData(0, 0, toMaps(0), toMaps(0), 0);
                Pair<String, String> pair = this.mPlayEffect;
                if (pair != null) {
                    showAmbientLightEvent(3, (String) pair.first, (String) this.mPlayEffect.second, 0, owner);
                    this.mPlayEffect = null;
                }
            }
            if (!apply && !this.hasEnable) {
                i = 0;
            }
            setAtlOpen(i);
            if (!apply && this.hasEnable && !this.mStatusMap.isEmpty()) {
                for (Map.Entry<String, String> entry : this.mStatusMap.entrySet()) {
                    String partition = entry.getKey();
                    Effect effect = this.mModeMap.get(entry.getValue());
                    if (!isRunning()) {
                        scheduledTasklet(partition, effect, "init");
                    } else {
                        this.mTask.addEffect(new EffectTask(partition, effect, "init"));
                    }
                }
            }
            this.hasApply = apply;
            showAmbientLightEvent(0, new String(), new String(), apply ? 1 : 0, owner);
        }
    }

    public void setAmbientLightListener(AmbientLightListener listener) {
        StringBuilder sb = new StringBuilder();
        sb.append("setAmbientLightListener ");
        sb.append(listener == null ? "invalid" : "ok");
        LogUtil.i(TAG, sb.toString());
        if (listener != null) {
            synchronized (this.mListener) {
                this.mListener.add(listener);
            }
        }
    }

    public void unsetAmbientLightListener(AmbientLightListener listener) {
        StringBuilder sb = new StringBuilder();
        sb.append("unsetAmbientLightListener ");
        sb.append(listener == null ? "invalid" : "ok");
        LogUtil.i(TAG, sb.toString());
        if (listener != null) {
            synchronized (this.mListener) {
                this.mListener.remove(listener);
            }
        }
    }

    public synchronized void loadAmbientLightEffect(String path) {
        Effect effect = new Effect().readFromJson(path);
        if (effect != null && !checkMode(effect.name)) {
            LogUtil.i(TAG, "loadAmbientLightEffect " + path);
            this.mEffectMap.put(effect.name, effect);
        }
    }

    public synchronized void clearAmbientLightEffect() {
        this.mEffectMap.clear();
    }

    public synchronized int requestAmbientLightPermission(boolean apply) {
        int i;
        String owner = PackageUtils.getPackageName(Binder.getCallingPid());
        if (!this.hasLite && this.hasIgOn && !this.hasMcuOn) {
            i = sendAmbientMessage(0, new AmbientMessage(apply, owner));
        }
        i = -1;
        return i;
    }

    public synchronized int setAmbientLightEnable(boolean status) {
        String owner;
        owner = PackageUtils.getPackageName(Binder.getCallingPid());
        return sendAmbientMessage(1, new AmbientMessage(status, owner));
    }

    public boolean getAmbientLightEnable() {
        return getProviderEnable();
    }

    public Set<String> showAmbientLightEffectPartitions() {
        return this.mEffectPartitions;
    }

    public Set<String> showAmbientLightEffects() {
        return this.mEffectMap.keySet();
    }

    public synchronized int playAmbientLightEffect(String partition, String effect, boolean hasJson) {
        int i;
        String owner = PackageUtils.getPackageName(Binder.getCallingPid());
        Effect ambient = null;
        if (hasJson) {
            Gson gson = new GsonBuilder().create();
            try {
                ambient = (Effect) gson.fromJson(effect, (Class<Object>) Effect.class);
            } catch (Exception e) {
                LogUtil.e(TAG, "playEffect deserialize exception " + e);
            }
        } else if (this.mEffectMap.containsKey(effect)) {
            ambient = this.mEffectMap.get(effect);
        }
        if (!this.hasLite && this.hasIgOn && !this.hasMcuOn && ambient != null && checkPolicy(ambient.priority) && !checkMode(ambient.name) && checkEffectPartition(partition)) {
            i = sendAmbientMessage(2, new AmbientMessage(partition, ambient, owner));
        }
        i = -1;
        return i;
    }

    public synchronized int stopAmbientLightEffect() {
        String owner;
        owner = PackageUtils.getPackageName(Binder.getCallingPid());
        return (!this.hasLite && this.hasIgOn) ? sendAmbientMessage(3, new AmbientMessage(owner)) : -1;
    }

    public Set<String> showAmbientLightModePartitions() {
        return this.mModePartitions;
    }

    public Set<String> showAmbientLightModes() {
        return this.mModes;
    }

    public synchronized int setAmbientLightMode(String partition, String mode) {
        int i;
        String owner = PackageUtils.getPackageName(Binder.getCallingPid());
        if (checkModePartition(partition) && checkMode(mode)) {
            i = sendAmbientMessage(4, new AmbientMessage(partition, mode, owner));
        }
        i = -1;
        return i;
    }

    public synchronized int subAmbientLightMode(String partition, String mode) {
        int i;
        String owner = PackageUtils.getPackageName(Binder.getCallingPid());
        if (checkModePartition(partition) && checkMode(mode)) {
            i = sendAmbientMessage(5, new AmbientMessage(partition, mode, owner));
        }
        i = -1;
        return i;
    }

    public String getAmbientLightMode(String partition) {
        return !checkModePartition(partition) ? new String() : getProviderMode(partition);
    }

    public Map<String, String> getAmbientLightPartitionModes() {
        return this.mSnapshotStatusMap;
    }

    public synchronized int setAmbientLightBright(String partition, int bright) {
        int i;
        String owner = PackageUtils.getPackageName(Binder.getCallingPid());
        if (!this.hasLite && checkModePartition(partition) && checkBright(bright)) {
            i = sendAmbientMessage(6, new AmbientMessage(partition, bright, owner));
        }
        i = -1;
        return i;
    }

    public int getAmbientLightBright(String partition) {
        if (this.hasLite || !checkModePartition(partition)) {
            return -1;
        }
        return getProviderBright(partition);
    }

    public Set<String> showAmbientLightColorTypes() {
        return this.mColorTypes;
    }

    public synchronized int setAmbientLightColorType(String partition, String colorType) {
        int i;
        String owner = PackageUtils.getPackageName(Binder.getCallingPid());
        if (!this.hasLite && checkModePartition(partition) && checkColorType(colorType) && hasSupportColorType(getAmbientLightMode(partition), colorType)) {
            i = sendAmbientMessage(7, new AmbientMessage(partition, colorType, owner));
        }
        i = -1;
        return i;
    }

    public String getAmbientLightColorType(String partition) {
        return !checkModePartition(partition) ? new String() : getProviderColorType(partition, getProviderMode(partition));
    }

    public synchronized int setAmbientLightMonoColor(String partition, int color) {
        int i;
        String owner = PackageUtils.getPackageName(Binder.getCallingPid());
        if (checkModePartition(partition) && checkColor(color)) {
            i = sendAmbientMessage(8, new AmbientMessage(partition, color, owner));
        }
        i = -1;
        return i;
    }

    public int getAmbientLightMonoColor(String partition) {
        if (checkModePartition(partition)) {
            return getProviderMonoColor(partition);
        }
        return -1;
    }

    public synchronized int setAmbientLightDoubleColor(String partition, Pair<Integer, Integer> color) {
        int i;
        String owner = PackageUtils.getPackageName(Binder.getCallingPid());
        if (!this.hasLite && checkModePartition(partition) && checkColor(((Integer) color.first).intValue()) && checkColor(((Integer) color.second).intValue())) {
            i = sendAmbientMessage(9, new AmbientMessage(partition, color, owner));
        }
        i = -1;
        return i;
    }

    public Pair<Integer, Integer> getAmbientLightDoubleColor(String partition) {
        if (this.hasLite || !checkModePartition(partition)) {
            return new Pair<>(-1, -1);
        }
        return getProviderDoubleColor(partition);
    }

    public Set<String> showAmbientLightThemesColor() {
        return this.mThemeMap.keySet();
    }

    public synchronized int setAmbientLightThemeColor(String partition, String themeColor) {
        int i;
        String owner = PackageUtils.getPackageName(Binder.getCallingPid());
        if (checkModePartition(partition) && checkThemeColor(themeColor)) {
            i = sendAmbientMessage(10, new AmbientMessage(partition, themeColor, owner));
        }
        i = -1;
        return i;
    }

    public String getAmbientLightThemeColor(String partition) {
        return !checkModePartition(partition) ? new String() : getProviderThemeColor(partition);
    }

    public Set<String> showAmbientLightRegions() {
        return new ArraySet(this.mRegionMap.values());
    }

    public synchronized int setAmbientLightRegionColor(String partition, String region, int color) {
        int i;
        String owner = PackageUtils.getPackageName(Binder.getCallingPid());
        if (checkModePartition(partition) && checkRegion(region)) {
            i = sendAmbientMessage(11, new AmbientMessage(partition, region, color, owner));
        }
        i = -1;
        return i;
    }

    public int getAmbientLightRegionColor(String partition, String region) {
        if (!checkModePartition(partition) || !checkRegion(region)) {
            return -1;
        }
        return getProviderRegionColor(partition, region);
    }

    public synchronized int setAmbientLightRegionBright(String partition, String region, int bright) {
        int i;
        String owner = PackageUtils.getPackageName(Binder.getCallingPid());
        if (checkModePartition(partition) && checkRegion(region)) {
            i = sendAmbientMessage(12, new AmbientMessage(partition, region, bright, owner));
        }
        i = -1;
        return i;
    }

    public int getAmbientLightRegionBright(String partition, String region) {
        if (!checkModePartition(partition) || !checkRegion(region)) {
            return -1;
        }
        return getProviderRegionBright(partition, region);
    }

    private XpAmbientLightService(Context context) {
        boolean z;
        this.mModes = new ArraySet(Arrays.asList("stable", "breath", "speed", "music"));
        this.mColorTypes = new ArraySet(Arrays.asList("mono", "double"));
        this.mEffectPartitions = new ArraySet(Arrays.asList(Actions.ACTION_ALL));
        this.mModePartitions = new ArraySet(Arrays.asList(Actions.ACTION_ALL));
        this.hasController = XUIConfig.getAtlType() == 2;
        this.hasLite = XUIConfig.getAtlType() == 1;
        this.hasDoorLight = false;
        this.hasAirConditioner = false;
        this.mListener = new ArrayList();
        this.mHvacCallback = null;
        this.mMcuCallback = null;
        this.mController = null;
        this.mCar = CarClientManager.getInstance();
        this.mCatchHueUtils = CatchHueUtils.getInstance();
        this.mVisualizerService = VisualizerService.getInstance();
        this.mExecutor = new ScheduledThreadPoolExecutor(1);
        this.mFuture = null;
        this.mTask = null;
        this.mEffectMap = new ArrayMap();
        this.mModeMap = new ArrayMap();
        this.mThemeMap = new ArrayMap();
        this.mRegionMap = new ArrayMap();
        this.mTime = 0L;
        this.mFadeIn = 0;
        this.mFadeOut = 1000;
        this.mFactor = 10;
        this.TICK_HVAC = 0;
        this.mTickRatio = 1;
        this.mDisplayId = 0;
        this.mGroup = 0;
        this.mRatio = 0.0f;
        this.hasInit = false;
        this.hasApply = false;
        this.hasVisualizer = false;
        this.hasCatchHue = false;
        this.hasCarSpeed = false;
        this.hasSpeedBrightChange = false;
        this.hasChange = false;
        this.hasSwitchOn = true;
        this.hasEnable = getProviderEnable();
        this.mStatusMap = new ArrayMap();
        this.mSnapshotStatusMap = new ArrayMap();
        this.mColorTypeMap = new ArrayMap();
        this.mDutyCycleMap = new ArrayMap();
        this.mMonoColorMap = new ArrayMap();
        this.mDoubleColorMap = new ArrayMap();
        this.mThemeColorMap = new ArrayMap();
        this.mRegionColorMap = new ArrayMap();
        this.mRegionBrightMap = new ArrayMap();
        this.mPlayEffect = null;
        this.mRandom = new Random();
        this.mHashCodeMap = new ArrayMap();
        this.mHandlerThread = null;
        this.mHandler = null;
        this.mContext = context;
        loadAmbientModes(MODEPATH);
        if (this.hasController) {
            this.mController = new Controller().readFromJson(PATH);
            Controller controller = this.mController;
            if (controller != null && controller.paramsMap != null) {
                this.mFadeIn = this.mController.paramsMap.containsKey("fade_in") ? this.mController.paramsMap.get("fade_in").intValue() : this.mFadeIn;
                this.mFadeOut = this.mController.paramsMap.containsKey("fade_out") ? this.mController.paramsMap.get("fade_out").intValue() : this.mFadeOut;
                this.mFactor = this.mController.paramsMap.containsKey("factor") ? this.mController.paramsMap.get("factor").intValue() : this.mFactor;
                LogUtil.i(TAG, "paramsMap: " + this.mController.paramsMap);
            }
            Controller controller2 = this.mController;
            if (controller2 != null && controller2.strategyMap != null) {
                if (!this.mController.strategyMap.containsKey("doorlight")) {
                    z = false;
                } else {
                    z = this.mController.strategyMap.get("doorlight").booleanValue();
                }
                this.hasDoorLight = z;
                this.hasAirConditioner = this.mController.strategyMap.containsKey("aircond") ? this.mController.strategyMap.get("aircond").booleanValue() : false;
                LogUtil.i(TAG, "strategyMap: " + this.mController.strategyMap);
            }
            Controller controller3 = this.mController;
            if (controller3 != null && controller3.colorTypes != null) {
                this.mColorTypes = new ArraySet(this.mController.colorTypes);
                LogUtil.i(TAG, "colorTypes: " + new ArraySet(this.mColorTypes));
            }
            if (this.mColorTypes.contains(ThemeManager.AttributeSet.THEME)) {
                loadThemesColor(THEMEPATH);
            }
            Controller controller4 = this.mController;
            if (controller4 != null && controller4.modePartitionsMap != null) {
                this.mModePartitions = this.mController.modePartitionsMap.keySet();
                LogUtil.i(TAG, "partition(mode): " + new ArrayList(this.mModePartitions));
            }
            Controller controller5 = this.mController;
            if (controller5 != null && controller5.effectPartitionsMap != null) {
                this.mEffectPartitions = this.mController.effectPartitionsMap.keySet();
                LogUtil.i(TAG, "partition(effect): " + new ArrayList(this.mEffectPartitions));
            }
            Controller controller6 = this.mController;
            if (controller6 != null && controller6.regionsMap != null) {
                this.mRegionMap = this.mController.regionsMap;
                LogUtil.i(TAG, "region: " + new ArrayList(this.mRegionMap.values()));
            }
        } else if (this.hasLite) {
            this.mModes = new ArraySet(Arrays.asList("stable"));
            this.mColorTypes = new ArraySet(Arrays.asList("mono"));
            LogUtil.i(TAG, "init AtlConfiguration");
            this.mBuilder = new AtlConfiguration.Builder().isLedEnabled(this.hasEnable).isLeftAtlControlEnabled(true).isRightAtlControlEnabled(true).colorType(0).rgbRedOrPreDefinedColor(getProviderMonoColor(Actions.ACTION_ALL)).isFadeEnabled(true).fadingTime(10).intensity(100);
        } else {
            this.mFadeIn = NfDef.STATE_STREAMING;
            this.mFadeOut = 1500;
            this.mFactor = 15;
            registerObserver();
        }
        for (String partition : this.mModePartitions) {
            String mode = getProviderMode(partition);
            Map<String, Integer> colorMap = new ArrayMap<>();
            Map<String, Integer> brightMap = new ArrayMap<>();
            for (String region : this.mRegionMap.values()) {
                colorMap.put(region, Integer.valueOf(getProviderRegionColor(partition, region)));
                brightMap.put(region, Integer.valueOf(getProviderRegionBright(partition, region)));
            }
            if (getProviderPartitions().contains(partition)) {
                this.mStatusMap.put(partition, mode);
            }
            this.mColorTypeMap.put(partition, getProviderColorType(partition, mode));
            this.mDutyCycleMap.put(partition, Integer.valueOf(getProviderBright(partition)));
            this.mMonoColorMap.put(partition, Integer.valueOf(getProviderMonoColor(partition)));
            this.mDoubleColorMap.put(partition, getProviderDoubleColor(partition));
            this.mThemeColorMap.put(partition, getProviderThemeColor(partition));
            this.mRegionColorMap.put(partition, colorMap);
            this.mRegionBrightMap.put(partition, brightMap);
        }
        this.mSnapshotStatusMap = (Map) this.mStatusMap.entrySet().stream().collect(Collectors.toMap($$Lambda$CSz_ibwXhtkKNl72Q8tR5oBgkWk.INSTANCE, $$Lambda$etDQhIA8H5hI6BDqsFIFQkLL9Nc.INSTANCE));
        LogUtil.i(TAG, "partition modes: " + this.mSnapshotStatusMap);
        this.mHueDataListener = new CatchHueUtils.HueDataListener() { // from class: com.xiaopeng.xuiservice.capabilities.ambientlight.XpAmbientLightService.5
            @Override // com.xiaopeng.xuiservice.utils.CatchHueUtils.HueDataListener
            public void onHueData(int displayId, int rgbColor) {
                XpAmbientLightService.this.mHueColor = (16777215 & rgbColor) | 16777216;
                StringBuilder sb = new StringBuilder();
                sb.append("onHueData, displayId=");
                sb.append(displayId);
                sb.append(", color=");
                sb.append(Integer.toHexString(rgbColor));
                sb.append(" (");
                XpAmbientLightService xpAmbientLightService = XpAmbientLightService.this;
                sb.append(xpAmbientLightService.parseColor(xpAmbientLightService.mHueColor));
                sb.append(")");
                LogUtil.d(XpAmbientLightService.TAG, sb.toString());
            }
        };
        this.mSDFftDataListener = new SDFftDataListener() { // from class: com.xiaopeng.xuiservice.capabilities.ambientlight.XpAmbientLightService.6
            @Override // com.xiaopeng.xuiservice.mediacenter.visualizer.SDFftDataListener
            public void onFftDataCapture(int displayId, String pgkName, byte[] fft, int samplingRate) {
            }

            @Override // com.xiaopeng.xuiservice.mediacenter.visualizer.SDFftDataListener
            public void onRatioData(int displayId, String pkgName, float ratio, float minRatio) {
                long time = SystemClock.elapsedRealtime();
                int elapsed = (int) (time - XpAmbientLightService.this.mTime);
                int fade = (XpAmbientLightService.this.mRhythmBright * XpAmbientLightService.this.fadeOut()) / 100;
                float threshold = XpAmbientLightService.this.factor() * 0.01f;
                if ((ratio > threshold && ratio - threshold > XpAmbientLightService.this.mRatio) || elapsed > fade) {
                    int bright = Math.max(10, (int) (100.0f * ratio));
                    XpAmbientLightService xpAmbientLightService = XpAmbientLightService.this;
                    if (xpAmbientLightService.mRhythmBright == bright) {
                        bright--;
                    }
                    xpAmbientLightService.mRhythmBright = bright;
                    XpAmbientLightService.this.hasRhythmBrightChange = false;
                    XpAmbientLightService.this.mTime = time;
                }
                LogUtil.d(XpAmbientLightService.TAG, "onRatioData, ratio=" + ratio + ", prev ratio=" + XpAmbientLightService.this.mRatio + ", mRhythmBright=" + XpAmbientLightService.this.mRhythmBright);
                XpAmbientLightService.this.mRatio = ratio;
            }
        };
        this.mSpeedBright = parseSpeedBright(getRawCarSpeed());
        this.mCar.addVcuManagerListener(new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xuiservice.capabilities.ambientlight.XpAmbientLightService.7
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 559944229) {
                    if (XpAmbientLightService.this.hasCarSpeed || !XpAmbientLightService.this.hasSwitchOn || XpAmbientLightService.this.hasDoorLight) {
                        float speed = ((Float) value.getValue()).floatValue();
                        int current = XpAmbientLightService.this.parseSpeedBright(speed);
                        int i = 0;
                        if (XpAmbientLightService.this.mSpeedBright != current) {
                            XpAmbientLightService.this.mSpeedBright = current;
                            XpAmbientLightService.this.hasSpeedBrightChange = false;
                        }
                        XpAmbientLightService xpAmbientLightService = XpAmbientLightService.this;
                        if (speed > 3.0f) {
                            if (!xpAmbientLightService.hasSwitchOn) {
                                i = 2;
                            } else if (XpAmbientLightService.this.hasDoorLight) {
                                i = 3;
                            }
                        }
                        xpAmbientLightService.mGroup = i;
                        LogUtil.d(XpAmbientLightService.TAG, "onChangeSpeed, speed=" + speed + ", mGroup=" + XpAmbientLightService.this.mGroup + ", bright=" + XpAmbientLightService.this.mSpeedBright);
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        });
        this.mCar.addConnectionListener(new IServiceConn() { // from class: com.xiaopeng.xuiservice.capabilities.ambientlight.XpAmbientLightService.8
            @Override // com.xiaopeng.xuiservice.carcontrol.IServiceConn
            public void onConnectedCar() {
                LogUtil.i(XpAmbientLightService.TAG, "onConnectedCar, hasEnable=" + XpAmbientLightService.this.hasEnable);
                if (XpAmbientLightService.this.hasInit) {
                    return;
                }
                XpAmbientLightService.this.addMcuManagerCallback();
                if (!XpAmbientLightService.this.hasLite) {
                    XpAmbientLightService.this.addHvacManagerCallback();
                    XpAmbientLightService xpAmbientLightService = XpAmbientLightService.this;
                    xpAmbientLightService.setAtlOpen(xpAmbientLightService.hasEnable ? 1 : 0);
                } else if (XpAmbientLightService.this.hasIgOn && XpAmbientLightService.this.hasEnable) {
                    XpAmbientLightService.this.setSHCReq(1);
                }
                if (XpAmbientLightService.this.hasIgOn && XpAmbientLightService.this.hasEnable && !XpAmbientLightService.this.mStatusMap.isEmpty()) {
                    LogUtil.i(XpAmbientLightService.TAG, "start AmbientLight (" + XpAmbientLightService.this.mStatusMap + ")");
                    for (Map.Entry<String, String> entry : XpAmbientLightService.this.mStatusMap.entrySet()) {
                        String partition2 = entry.getKey();
                        Effect effect = (Effect) XpAmbientLightService.this.mModeMap.get(entry.getValue());
                        if (!XpAmbientLightService.this.isRunning()) {
                            XpAmbientLightService.this.scheduledTasklet(partition2, effect, "init");
                        } else {
                            XpAmbientLightService.this.mTask.addEffect(new EffectTask(partition2, effect, "init"));
                        }
                    }
                } else {
                    XpAmbientLightService xpAmbientLightService2 = XpAmbientLightService.this;
                    xpAmbientLightService2.setGroupLightData(0, 0, xpAmbientLightService2.toMaps(0), XpAmbientLightService.this.toMaps(0), 0);
                }
                XpAmbientLightService.this.hasInit = true;
            }

            @Override // com.xiaopeng.xuiservice.carcontrol.IServiceConn
            public void onDisconnectCar() {
                LogUtil.i(XpAmbientLightService.TAG, "onDisconnectCar");
            }
        });
        this.mHandlerThread = new HandlerThread("XpAmbientLightThread");
        this.mHandlerThread.start();
        this.mHandler = new Handler(this.mHandlerThread.getLooper()) { // from class: com.xiaopeng.xuiservice.capabilities.ambientlight.XpAmbientLightService.9
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                int i = msg.what;
                if (i != 15) {
                    switch (i) {
                        case 0:
                            AmbientMessage message = (AmbientMessage) msg.obj;
                            XpAmbientLightService.this.handleRequestAmbientLight(message.status, message.owner);
                            return;
                        case 1:
                            AmbientMessage message2 = (AmbientMessage) msg.obj;
                            XpAmbientLightService.this.handleSetAmbientLightEnable(message2.status, message2.owner);
                            return;
                        case 2:
                            AmbientMessage message3 = (AmbientMessage) msg.obj;
                            XpAmbientLightService.this.handlePlayEffect(message3.sdata, message3.effect, message3.owner);
                            return;
                        case 3:
                            XpAmbientLightService.this.handleStopEffect(((AmbientMessage) msg.obj).owner);
                            return;
                        case 4:
                            AmbientMessage message4 = (AmbientMessage) msg.obj;
                            XpAmbientLightService.this.handleSetAmbientLightMode(message4.sdata, message4.sdata2, message4.owner);
                            return;
                        case 5:
                            AmbientMessage message5 = (AmbientMessage) msg.obj;
                            XpAmbientLightService.this.handleSubAmbientLightMode(message5.sdata, message5.sdata2, message5.owner);
                            return;
                        case 6:
                            AmbientMessage message6 = (AmbientMessage) msg.obj;
                            XpAmbientLightService.this.handleSetAmbientLightBright(message6.sdata, message6.data, message6.owner);
                            return;
                        case 7:
                            AmbientMessage message7 = (AmbientMessage) msg.obj;
                            XpAmbientLightService.this.handleSetAmbientLightColorType(message7.sdata, message7.sdata2, message7.owner);
                            return;
                        case 8:
                            AmbientMessage message8 = (AmbientMessage) msg.obj;
                            XpAmbientLightService.this.handleSetAmbientLightMonoColor(message8.sdata, message8.data, message8.owner);
                            return;
                        case 9:
                            AmbientMessage message9 = (AmbientMessage) msg.obj;
                            XpAmbientLightService.this.handleSetAmbientLightDoubleColor(message9.sdata, message9.color, message9.owner);
                            return;
                        case 10:
                            AmbientMessage message10 = (AmbientMessage) msg.obj;
                            XpAmbientLightService.this.handleSetAmbientLightThemeColor(message10.sdata, message10.sdata2, message10.owner);
                            return;
                        case 11:
                            AmbientMessage message11 = (AmbientMessage) msg.obj;
                            XpAmbientLightService.this.handleSetAmbientLightRegionColor(message11.sdata, message11.sdata2, message11.data, message11.owner);
                            return;
                        case 12:
                            AmbientMessage message12 = (AmbientMessage) msg.obj;
                            XpAmbientLightService.this.handleSetAmbientLightRegionBright(message12.sdata, message12.sdata2, message12.data, message12.owner);
                            return;
                        default:
                            BiAmbient bilog = (BiAmbient) msg.obj;
                            bilog.submit();
                            return;
                    }
                }
                AmbientMessage message13 = (AmbientMessage) msg.obj;
                XpAmbientLightService.this.handleSetAmbientLight(message13.status, message13.owner);
            }
        };
        DumpDispatcher.registerDump(Actions.ACTION_AMBIENT, new DumpDispatcher.DebugDumper() { // from class: com.xiaopeng.xuiservice.capabilities.ambientlight.-$$Lambda$XpAmbientLightService$1GIVkSrkksj5D83YdH1zHouG7ns
            @Override // com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
            public final void debugDump(PrintWriter printWriter, String[] strArr) {
                XpAmbientLightService.this.lambda$new$1$XpAmbientLightService(printWriter, strArr);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* renamed from: dump */
    public void lambda$new$1$XpAmbientLightService(PrintWriter pw, String[] args) {
        char c;
        String str;
        pw.println("*dump-XpAmbientLightService");
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                String str2 = args[i];
                switch (str2.hashCode()) {
                    case -2139626000:
                        if (str2.equals("--setDoubleColor")) {
                            c = 5;
                            break;
                        }
                        c = 65535;
                        break;
                    case -2087470931:
                        if (str2.equals("--setRegionColor")) {
                            c = 7;
                            break;
                        }
                        c = 65535;
                        break;
                    case -1670640498:
                        if (str2.equals("--showEffect")) {
                            c = '\n';
                            break;
                        }
                        c = 65535;
                        break;
                    case -1530453602:
                        if (str2.equals("--setMonoColor")) {
                            c = 4;
                            break;
                        }
                        c = 65535;
                        break;
                    case -1155325060:
                        if (str2.equals("--setThemeColor")) {
                            c = 6;
                            break;
                        }
                        c = 65535;
                        break;
                    case -924201083:
                        if (str2.equals("--setMode")) {
                            c = 1;
                            break;
                        }
                        c = 65535;
                        break;
                    case -870484091:
                        if (str2.equals("--playEffect")) {
                            c = '\f';
                            break;
                        }
                        c = 65535;
                        break;
                    case -518530018:
                        if (str2.equals("--requestPermission")) {
                            c = 11;
                            break;
                        }
                        c = 65535;
                        break;
                    case -313045264:
                        if (str2.equals("--setRegionBright")) {
                            c = '\b';
                            break;
                        }
                        c = 65535;
                        break;
                    case 492151251:
                        if (str2.equals("--stopEffect")) {
                            c = '\r';
                            break;
                        }
                        c = 65535;
                        break;
                    case 588993628:
                        if (str2.equals("--setBright")) {
                            c = 2;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1417326435:
                        if (str2.equals("--setPower")) {
                            c = 0;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1689405827:
                        if (str2.equals("--setGroupLightData")) {
                            c = '\t';
                            break;
                        }
                        c = 65535;
                        break;
                    case 1926795419:
                        if (str2.equals("--setColorType")) {
                            c = 3;
                            break;
                        }
                        c = 65535;
                        break;
                    default:
                        c = 65535;
                        break;
                }
                str = " succeed";
                switch (c) {
                    case 0:
                        if (i < args.length - 1) {
                            try {
                                boolean status = Integer.parseInt(args[i + 1]) != 0;
                                int ret = setAmbientLightEnable(status);
                                StringBuilder sb = new StringBuilder();
                                sb.append("setPower ");
                                sb.append(status ? "on" : "off");
                                if (ret != 0) {
                                    str = " failed";
                                }
                                sb.append(str);
                                pw.println(sb.toString());
                                break;
                            } catch (Exception e) {
                                break;
                            }
                        } else {
                            break;
                        }
                    case 1:
                        if (i < args.length - 1) {
                            String mode = args[i + 1];
                            int ret2 = setAmbientLightMode(Actions.ACTION_ALL, mode);
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("setMode ");
                            sb2.append(mode);
                            sb2.append(ret2 != 0 ? " failed" : " succeed");
                            pw.println(sb2.toString());
                            break;
                        } else {
                            break;
                        }
                    case 2:
                        if (i < args.length - 1) {
                            try {
                                int bright = Integer.parseInt(args[i + 1]);
                                int ret3 = setAmbientLightBright(Actions.ACTION_ALL, bright);
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("setBright ");
                                sb3.append(bright);
                                if (ret3 != 0) {
                                    str = " failed";
                                }
                                sb3.append(str);
                                pw.println(sb3.toString());
                                break;
                            } catch (Exception e2) {
                                break;
                            }
                        } else {
                            break;
                        }
                    case 3:
                        if (i < args.length - 1) {
                            String type = args[i + 1];
                            int ret4 = setAmbientLightColorType(Actions.ACTION_ALL, type);
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("setColorType ");
                            sb4.append(type);
                            sb4.append(ret4 != 0 ? " failed" : " succeed");
                            pw.println(sb4.toString());
                            break;
                        } else {
                            break;
                        }
                    case 4:
                        if (i < args.length - 1) {
                            try {
                                int mono = Integer.parseInt(args[i + 1]);
                                int ret5 = "mono".equals(getAmbientLightColorType(Actions.ACTION_ALL)) ? setAmbientLightMonoColor(Actions.ACTION_ALL, mono) : -1;
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append("setMonoColor");
                                sb5.append(mono);
                                if (ret5 != 0) {
                                    str = " failed";
                                }
                                sb5.append(str);
                                pw.println(sb5.toString());
                                break;
                            } catch (Exception e3) {
                                break;
                            }
                        } else {
                            break;
                        }
                    case 5:
                        if (i < args.length - 1) {
                            try {
                                Pair<Integer, Integer> colors = new Pair<>(Integer.valueOf(Integer.parseInt(args[i + 1])), Integer.valueOf(Integer.parseInt(args[i + 2])));
                                int ret6 = "double".equals(getAmbientLightColorType(Actions.ACTION_ALL)) ? setAmbientLightDoubleColor(Actions.ACTION_ALL, colors) : -1;
                                StringBuilder sb6 = new StringBuilder();
                                sb6.append("setDoubleColor (");
                                sb6.append(colors.first);
                                sb6.append(", ");
                                sb6.append(colors.second);
                                sb6.append(ret6 == 0 ? ") succeed" : ") failed");
                                pw.println(sb6.toString());
                                break;
                            } catch (Exception e4) {
                                break;
                            }
                        } else {
                            break;
                        }
                    case 6:
                        if (i < args.length - 1) {
                            String theme = args[i + 1];
                            int ret7 = ThemeManager.AttributeSet.THEME.equals(getAmbientLightColorType(Actions.ACTION_ALL)) ? setAmbientLightThemeColor(Actions.ACTION_ALL, theme) : -1;
                            StringBuilder sb7 = new StringBuilder();
                            sb7.append("setThemeColor ");
                            sb7.append(theme);
                            sb7.append(ret7 != 0 ? " failed" : " succeed");
                            pw.println(sb7.toString());
                            break;
                        } else {
                            break;
                        }
                    case 7:
                        if (i < args.length - 1) {
                            try {
                                String region = args[i + 1];
                                int color = Integer.parseInt(args[i + 2]);
                                int ret8 = "custom".equals(getAmbientLightColorType(Actions.ACTION_ALL)) ? setAmbientLightRegionColor(Actions.ACTION_ALL, region, color) : -1;
                                StringBuilder sb8 = new StringBuilder();
                                sb8.append("setRegionColor ");
                                sb8.append(region);
                                sb8.append(" = ");
                                sb8.append(color);
                                if (ret8 != 0) {
                                    str = " failed";
                                }
                                sb8.append(str);
                                pw.println(sb8.toString());
                                break;
                            } catch (Exception e5) {
                                break;
                            }
                        } else {
                            break;
                        }
                    case '\b':
                        if (i < args.length - 1) {
                            try {
                                String region2 = args[i + 1];
                                int bright2 = Integer.parseInt(args[i + 2]);
                                int ret9 = "custom".equals(getAmbientLightColorType(Actions.ACTION_ALL)) ? setAmbientLightRegionBright(Actions.ACTION_ALL, region2, bright2) : -1;
                                StringBuilder sb9 = new StringBuilder();
                                sb9.append("setRegionBright ");
                                sb9.append(region2);
                                sb9.append(" = ");
                                sb9.append(bright2);
                                if (ret9 != 0) {
                                    str = " failed";
                                }
                                sb9.append(str);
                                pw.println(sb9.toString());
                                break;
                            } catch (Exception e6) {
                                break;
                            }
                        } else {
                            break;
                        }
                    case '\t':
                        if (i < args.length - 1) {
                            try {
                                int group = Integer.parseInt(args[i + 1]);
                                int color2 = Integer.parseInt(args[i + 2]);
                                int bright3 = Integer.parseInt(args[i + 3]);
                                int fade = Integer.parseInt(args[i + 4]);
                                handleRequestAmbientLight(true, "test");
                                setGroupLightData(0, group, toMaps(color2), toMaps(bright3), fade);
                                pw.println("setGroupLightData, group=" + group + ", color=" + color2 + ", bright=" + bright3 + ", fade=" + fade);
                                break;
                            } catch (Exception e7) {
                                break;
                            }
                        } else {
                            break;
                        }
                    case '\n':
                        pw.println("showEffect: " + String.join(", ", showAmbientLightEffects()));
                        break;
                    case 11:
                        if (i < args.length - 1) {
                            try {
                                boolean apply = Integer.parseInt(args[i + 1]) != 0;
                                int ret10 = requestAmbientLightPermission(apply);
                                StringBuilder sb10 = new StringBuilder();
                                sb10.append("requestPermission ");
                                sb10.append(apply ? "apply" : "release");
                                if (ret10 != 0) {
                                    str = " failed";
                                }
                                sb10.append(str);
                                pw.println(sb10.toString());
                                break;
                            } catch (Exception e8) {
                                break;
                            }
                        } else {
                            break;
                        }
                    case '\f':
                        if (i < args.length - 1) {
                            String effect = args[i + 1];
                            int ret11 = playAmbientLightEffect(Actions.ACTION_ALL, effect, false);
                            StringBuilder sb11 = new StringBuilder();
                            sb11.append("playEffect ");
                            sb11.append(effect);
                            sb11.append(ret11 != 0 ? " failed" : " succeed");
                            pw.println(sb11.toString());
                            break;
                        } else {
                            break;
                        }
                    case '\r':
                        int ret12 = stopAmbientLightEffect();
                        StringBuilder sb12 = new StringBuilder();
                        sb12.append("stopEffect ");
                        sb12.append(ret12 != 0 ? " failed" : " succeed");
                        pw.println(sb12.toString());
                        break;
                }
            }
        }
    }

    public static XpAmbientLightService getInstance(Context context) {
        if (sService == null) {
            synchronized (XpAmbientLightService.class) {
                if (sService == null) {
                    sService = new XpAmbientLightService(context);
                }
            }
        }
        return sService;
    }
}
