package com.xiaopeng.xuiservice.capabilities.smm;

import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.msm.CarMsmManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Pair;
import com.alipay.mobile.aromeservice.RequestParams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loostone.libtuning.channel.skyworth.old.ai.config.AiCmd;
import com.xiaopeng.biutil.BiLog;
import com.xiaopeng.biutil.BiLogFactory;
import com.xiaopeng.libtheme.ThemeManager;
import com.xiaopeng.xuimanager.seatmassager.Seat;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.carcontrol.IServiceConn;
import com.xiaopeng.xuiservice.innerutils.BiLogTransmit;
import com.xiaopeng.xuiservice.innerutils.DataLogUtils;
import com.xiaopeng.xuiservice.utils.PackageUtils;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class XpSeatMassagerService {
    private static final String CFG_INTENSITY = "/system/etc/xuiservice/svm/intensity.json";
    private static final int EAGAIN = 5;
    private static final int EBUSY = 4;
    private static final int EEXIST = 6;
    private static final int EFBIG = 7;
    private static final int EFFECT = 2;
    private static final int EINVAL = 2;
    private static final int EIO = 3;
    private static final int EPERM = 1;
    private static final int ERROR = 8;
    private static final int INTENSITY = 3;
    private static final int INTENSITY_SMM = 3;
    private static final int INTENSITY_SVM = 5;
    private static final int LOAD = 7;
    private static final int MAX_PATTERN = 1;
    private static final int MAX_POSITION = 2;
    private static final int MSG_RHYTHM_ENABLE = 7;
    private static final int MSG_RHYTHM_SET_INTENSITY = 8;
    private static final int MSG_RHYTHM_SET_PATTERN = 9;
    private static final int MSG_SMM_SET_EFFECT = 2;
    private static final int MSG_SMM_SET_INTENSITY = 3;
    private static final int MSG_SMM_START = 0;
    private static final int MSG_SMM_STOP = 1;
    private static final int MSG_SVM_FINISH = 5;
    private static final int MSG_SVM_SET_INTENSITY = 6;
    private static final int MSG_SVM_START = 4;
    private static final int NR_SMM = 4;
    private static final int NR_SVM = 2;
    private static final int NR_UNIT = 2;
    private static final int RHYTHM_INTENSITY = 5;
    private static final int RHYTHM_PATTERN = 6;
    private static final int RHYTHM_STATUS = 4;
    private static final String SMM_EFFECT = "MassagerEffect";
    private static final String SMM_INTENSITY = "SeatMassagerIntensity";
    private static final String SR_ENABLE = "SeatRhythmEnable";
    private static final String SR_INTENSITY = "SeatRhythmIntensity";
    private static final String SR_PATTERN = "SeatRhythmPattern";
    private static final int START = 0;
    private static final int STOP = 1;
    private static final String SVM_INTENSITY = "SeatVibrateIntensity";
    private static final String TAG = "XpSeatMassagerService";
    private static final int TICK_PERIOD = 10;
    private static String mRhythmPattern;
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    private static XpSeatMassagerService sService = null;
    private static List<SeatMassagerListener> mListener = new ArrayList();
    private static Map<String, Pair<Integer, Integer>> mSoundMap = new ArrayMap();
    private static Map<Integer, String> mSoundLoadMap = new ArrayMap();
    private static Map<String, Effect> mEffectMap = new ArrayMap();
    private static Map<Integer, String> mModeMap = new ArrayMap();
    private static Map<Integer, ScheduledFuture<?>> mTaskMap = new ArrayMap();
    private static Map<Integer, Integer> mStatusMap = Collections.synchronizedMap(new HashMap());
    private static Map<String, Map<Integer, List<Integer>>> mMapIntensity = new ArrayMap();
    private static Map<Integer, List<Integer>> mRhythmIntensityMap = new ArrayMap();
    private static Map<Integer, Object> mLockMap = new ArrayMap();
    private static Map<Integer, BiMassager> mBiMassagerMap = new ArrayMap();
    private static Map<Integer, BiMassager> mBiRhythmMap = new ArrayMap();
    private static Map<Integer, Integer> mIntensitysMap = new ArrayMap();
    private static Map<Integer, List<Integer>> mSignalMap = new ArrayMap();
    private static List<Integer> mIntensity = new ArrayList();
    private static ScheduledThreadPoolExecutor mExecutor = new ScheduledThreadPoolExecutor(4);
    private static SoundPool mSoundPool = null;
    private static AudioManager mAudioManager = null;
    private static boolean hasRhythm = false;
    private static boolean hasInit = false;
    private static Vibrate mVibrate = null;
    private static BiVibrate mBiVibrate = null;
    private static CarClientManager mCar = CarClientManager.getInstance();
    private static CarMsmManager.CarMsmEventCallback mMsmCallback = null;

    /* loaded from: classes5.dex */
    public interface SeatMassagerListener {
        void onMassagerEvent(int i, int i2, String str, int i3);

        void onVibrateEvent(List<Integer> list, int i, String str, int i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class MapIntensity {
        public int key;
        public List<Integer> value;

        private MapIntensity() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class Intensity {
        public List<MapIntensity> intensity;
        public String pattern;

        private Intensity() {
        }
    }

    /* loaded from: classes5.dex */
    private class Packet {
        public String[] data;
        public int loop;
        public int period;

        private Packet() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class Effect {
        public boolean hasPreset;
        public int mode;
        public String name;
        public Packet packet;

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
                        LogUtil.e(XpSeatMassagerService.TAG, "close " + file + " Exception: " + e);
                    }
                    return effect;
                } catch (Throwable th) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e2) {
                            LogUtil.e(XpSeatMassagerService.TAG, "close " + file + " Exception: " + e2);
                        }
                    }
                    throw th;
                }
            } catch (Exception e3) {
                LogUtil.e(XpSeatMassagerService.TAG, "readFromJson Exception: " + e3);
                if (reader != null) {
                    try {
                        reader.close();
                        return null;
                    } catch (Exception e4) {
                        LogUtil.e(XpSeatMassagerService.TAG, "close " + file + " Exception: " + e4);
                        return null;
                    }
                }
                return null;
            }
        }
    }

    /* loaded from: classes5.dex */
    private class Massager {
        public String effect;
        public List<Integer> ids;
        public String owner;
        public List<Seat> seats;

        public Massager(List<Integer> ids, List<Seat> seats, String effect, String owner) {
            this.ids = ids;
            this.seats = seats;
            this.effect = effect;
            this.owner = owner;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class Vibrate {
        public String effect;
        public boolean enable;
        public List<Integer> ids;
        public int loop;
        public String owner;
        public int position;
        public List<Seat> seats;
        public int streamId;

        public Vibrate(List<Seat> seats, String effect, int loop, int position, String owner) {
            this.seats = seats;
            this.effect = effect;
            this.loop = loop < 0 ? -1 : loop;
            this.position = position;
            this.owner = owner;
        }

        public Vibrate(int streamId, List<Integer> ids, String effect, int position) {
            this.streamId = streamId;
            this.ids = ids;
            this.effect = effect;
            this.position = position;
        }

        public Vibrate(List<Integer> ids, boolean enable, String owner) {
            this.ids = ids;
            this.enable = enable;
            this.owner = owner;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class EffectTask implements Runnable {
        private Effect effect;
        private int id;
        private int tick = 0;

        public EffectTask(int id, Effect effect) {
            this.id = id;
            this.effect = effect;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.tick++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class BiMassager {
        private static final int TOP_LIMIT = 10;
        private JSONArray patterns = new JSONArray();
        private JSONArray intensitys = new JSONArray();
        private BiLog bilog = BiLogFactory.create(DataLogUtils.XUI_PID, "B007");

        public void addPattern(String pattern) {
            if (this.patterns.length() < 10) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("ts", System.currentTimeMillis());
                    obj.put("pattern", pattern);
                } catch (Exception e) {
                }
                this.patterns.put(obj);
            }
        }

        public void addIntensity(int intensity) {
            if (this.intensitys.length() < 10) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("ts", System.currentTimeMillis());
                    obj.put("intensity", intensity);
                } catch (Exception e) {
                }
                this.intensitys.put(obj);
            }
        }

        public void submit() {
            this.bilog.push("endTime", String.valueOf(System.currentTimeMillis()));
            this.bilog.push("changePatterns", this.patterns.toString());
            this.bilog.push("changeIntensitys", this.intensitys.toString());
            LogUtil.d(XpSeatMassagerService.TAG, "BiMassager: " + this.bilog.getString());
            BiLogTransmit.getInstance().submit(this.bilog);
        }

        public BiMassager(int id, String type, String source, String pattern, int intensity) {
            this.bilog.push("id", String.valueOf(id));
            this.bilog.push(SpeechConstants.KEY_COMMAND_TYPE, type);
            this.bilog.push("pattern", pattern);
            this.bilog.push("intensity", String.valueOf(intensity));
            this.bilog.push("source", !TextUtils.isEmpty(source) ? source : "unknown");
            this.bilog.push(RequestParams.REQUEST_KEY_START_TIME, String.valueOf(System.currentTimeMillis()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class BiVibrate {
        private BiLog bilog = BiLogFactory.create(DataLogUtils.XUI_PID, DataLogUtils.SEATVIBRATE_BID);

        public void submit() {
            this.bilog.push("endTime", String.valueOf(System.currentTimeMillis()));
            LogUtil.d(XpSeatMassagerService.TAG, "BiVibrate: " + this.bilog.getString());
            BiLogTransmit.getInstance().submit(this.bilog);
        }

        public BiVibrate(List<Integer> ids, String source, String effect, int position) {
            JSONArray intensitys = new JSONArray();
            for (Integer id : ids) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("id", id);
                    obj.put("intensity", XpSeatMassagerService.mIntensitysMap.get(id));
                } catch (Exception e) {
                }
                intensitys.put(obj);
            }
            this.bilog.push("effect", effect);
            this.bilog.push("seatsIntensity", intensitys.toString());
            this.bilog.push("position", String.valueOf(position));
            this.bilog.push("source", !TextUtils.isEmpty(source) ? source : "unknown");
            this.bilog.push(RequestParams.REQUEST_KEY_START_TIME, String.valueOf(System.currentTimeMillis()));
        }
    }

    private void loadVibrateIntensity(String path) {
        BufferedReader reader = null;
        try {
            try {
                try {
                    reader = new BufferedReader(new FileReader(path));
                    Gson gson = new GsonBuilder().create();
                    Intensity[] array = (Intensity[]) gson.fromJson((Reader) reader, (Class<Object>) Intensity[].class);
                    for (Intensity it : array) {
                        Map<Integer, List<Integer>> mapping = new ArrayMap<>();
                        for (MapIntensity item : it.intensity) {
                            mapping.put(Integer.valueOf(item.key), item.value);
                        }
                        mMapIntensity.put(it.pattern, mapping);
                    }
                    LogUtil.i(TAG, "loadVibrateIntensity " + path + " succeed");
                    reader.close();
                } catch (Throwable th) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e) {
                        }
                    }
                    throw th;
                }
            } catch (Exception e2) {
                LogUtil.e(TAG, "loadVibrateIntensity failed " + e2);
                if (reader != null) {
                    reader.close();
                }
            }
        } catch (Exception e3) {
        }
    }

    private void mappingSignalSeatId(int id) {
        if (id == 0) {
            mSignalMap.put(Integer.valueOf(id), Arrays.asList(557849914, 557849915, 557849953));
            mLockMap.put(557849914, new Object());
            mLockMap.put(557849915, new Object());
        } else if (id == 1) {
            mSignalMap.put(Integer.valueOf(id), Arrays.asList(557849917, 557849916, 557849954));
            mLockMap.put(557849917, new Object());
            mLockMap.put(557849916, new Object());
        } else if (id == 2) {
            mSignalMap.put(Integer.valueOf(id), Arrays.asList(557849918, 557849919, 557849955));
            mLockMap.put(557849918, new Object());
            mLockMap.put(557849919, new Object());
        } else if (id == 3) {
            mSignalMap.put(Integer.valueOf(id), Arrays.asList(557849920, 557849921, 557849956));
            mLockMap.put(557849920, new Object());
            mLockMap.put(557849921, new Object());
        } else {
            LogUtil.w(TAG, "invalid mappingSignalSeatId " + id);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasSupportMassager(int id) {
        if (id != 0) {
            if (id != 1) {
                if (id != 2) {
                    if (id == 3) {
                        return XUIConfig.hasFeature("SECROW_RT_MASSG");
                    }
                    return false;
                }
                return XUIConfig.hasFeature("SECROW_LT_MASSG");
            }
            return XUIConfig.hasFeature("MSMP_MASSG");
        }
        return XUIConfig.hasFeature("MSMD_MASSG");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSignalMode(int id) {
        return mSignalMap.get(Integer.valueOf(id)).get(0).intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSignalIntensity(int id) {
        return mSignalMap.get(Integer.valueOf(id)).get(1).intValue();
    }

    private int getSignalStatus(int id) {
        return mSignalMap.get(Integer.valueOf(id)).get(2).intValue();
    }

    private void registerSignalMapSeatIds() {
        for (int i = 0; i < 4; i++) {
            mappingSignalSeatId(i);
        }
    }

    private boolean checkId(int id, int range) {
        return id >= 0 && id < range;
    }

    private boolean checkIntensity(int intensity, int range) {
        return intensity > 0 && intensity <= range;
    }

    private boolean checkRhythmPattern(int pattern) {
        return pattern >= 0 && pattern <= 1;
    }

    private boolean checkPosition(int position) {
        return position >= 0 && position <= 2;
    }

    private boolean checkIds(List<Integer> ids, int id) {
        return !ids.isEmpty() && checkId(((Integer) Collections.max(ids)).intValue(), id) && checkId(((Integer) Collections.min(ids)).intValue(), id);
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x000a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean checkSeats(java.util.List<com.xiaopeng.xuimanager.seatmassager.Seat> r4, int r5, int r6) {
        /*
            r3 = this;
            java.util.Iterator r0 = r4.iterator()
        L4:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L28
            java.lang.Object r1 = r0.next()
            com.xiaopeng.xuimanager.seatmassager.Seat r1 = (com.xiaopeng.xuimanager.seatmassager.Seat) r1
            int r2 = r1.getId()
            boolean r2 = r3.checkId(r2, r5)
            if (r2 == 0) goto L26
            int r2 = r1.getIntensity()
            boolean r2 = r3.checkIntensity(r2, r6)
            if (r2 != 0) goto L25
            goto L26
        L25:
            goto L4
        L26:
            r2 = 0
            return r2
        L28:
            boolean r1 = r4.isEmpty()
            r1 = r1 ^ 1
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xuiservice.capabilities.smm.XpSeatMassagerService.checkSeats(java.util.List, int, int):boolean");
    }

    private boolean checkMassagerId(int id) {
        return checkId(id, 4);
    }

    private boolean checkVibrateId(int id) {
        return checkId(id, 2);
    }

    private boolean checkMassagerIntensity(int intensity) {
        return checkIntensity(intensity, 3);
    }

    private boolean checkVibrateIntensity(int intensity) {
        return checkIntensity(intensity, 5);
    }

    private boolean checkMassagerEffect(String name) {
        return name != null && mEffectMap.containsKey(name);
    }

    private boolean checkVibrateEffect(String name) {
        return mSoundMap.containsKey(name) || ThemeManager.AttributeSet.BACKGROUND.equals(name);
    }

    private boolean checkMassagerIds(List<Integer> ids) {
        return checkIds(ids, 4);
    }

    private boolean checkVibrateIds(List<Integer> ids) {
        return checkIds(ids, 2);
    }

    private boolean checkMassagerSeats(List<Seat> seats) {
        return checkSeats(seats, 4, 3);
    }

    private boolean checkVibrateSeats(List<Seat> seats) {
        return checkSeats(seats, 2, 5);
    }

    private void setProviderMassagerEffect(int id, String effect) {
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        Settings.System.putString(contentResolver, SMM_EFFECT + id, effect);
    }

    private String getProviderMassagerEffect(int id) {
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        String effect = Settings.System.getString(contentResolver, SMM_EFFECT + id);
        return checkMassagerEffect(effect) ? effect : "wave";
    }

    private void setProviderMassagerIntensity(int id, int intensity) {
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        Settings.System.putInt(contentResolver, SMM_INTENSITY + id, intensity);
    }

    private int getProviderMassagerIntensity(int id) {
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        int intensity = Settings.System.getInt(contentResolver, SMM_INTENSITY + id, 2);
        if (checkMassagerIntensity(intensity)) {
            return intensity;
        }
        return 2;
    }

    private void setProviderVibrateIntensity(int id, int intensity) {
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        Settings.System.putInt(contentResolver, SVM_INTENSITY + id, intensity);
    }

    private int getProviderVibrateIntensity(int id) {
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        int intensity = Settings.System.getInt(contentResolver, SVM_INTENSITY + id, 3);
        if (checkVibrateIntensity(intensity)) {
            return intensity;
        }
        return 3;
    }

    private void setProviderRhythmEnable(int id, boolean enable) {
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        Settings.System.putInt(contentResolver, SR_ENABLE + id, enable ? 1 : 0);
    }

    private boolean getProviderRhythmEnable(int id) {
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        StringBuilder sb = new StringBuilder();
        sb.append(SR_ENABLE);
        sb.append(id);
        return Settings.System.getInt(contentResolver, sb.toString(), 0) != 0;
    }

    private void setProviderRhythmIntensity(int id, int intensity) {
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        Settings.System.putInt(contentResolver, SR_INTENSITY + id, intensity);
    }

    private int getProviderRhythmIntensity(int id) {
        ContentResolver contentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();
        int intensity = Settings.System.getInt(contentResolver, SR_INTENSITY + id, 3);
        if (checkVibrateIntensity(intensity)) {
            return intensity;
        }
        return 3;
    }

    private void setProviderRhythmPattern(int pattern) {
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), SR_PATTERN, pattern);
    }

    private int getProviderRhythmPattern() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), SR_PATTERN, 0);
    }

    private String parseOpCode(int opCode) {
        if (opCode != 0) {
            if (opCode != 1) {
                if (opCode != 2) {
                    if (opCode == 3) {
                        return "setIntensity";
                    }
                    return "unKnown";
                }
                return "setEffect";
            }
            return AiCmd.STOP;
        }
        return "Start";
    }

    private void handleBiMassager(int id, int type, String effect, int intensity, String owner) {
        if (!mBiMassagerMap.containsKey(Integer.valueOf(id)) && type == 0) {
            mBiMassagerMap.put(Integer.valueOf(id), new BiMassager(id, "massager", owner, effect, intensity));
        } else if (mBiMassagerMap.containsKey(Integer.valueOf(id))) {
            BiMassager uploader = mBiMassagerMap.get(Integer.valueOf(id));
            if (type == 2) {
                uploader.addPattern(effect);
            } else if (type == 3) {
                uploader.addIntensity(intensity);
            } else if (type == 1) {
                uploader.submit();
                mBiMassagerMap.remove(Integer.valueOf(id));
            }
        }
    }

    private void showMassagerEvent(int id, int type, String effect, int data, String owner) {
        handleBiMassager(id, type, effect, data, owner);
        if (mListener.isEmpty()) {
            return;
        }
        if (type == 0 || type == 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("on");
            sb.append(type == 0 ? "Start" : AiCmd.STOP);
            sb.append("Massager, id=");
            sb.append(id);
            sb.append(", effect=");
            sb.append(effect);
            sb.append(", intensity=");
            sb.append(data);
            LogUtil.d(TAG, sb.toString());
        } else if (type == 2) {
            LogUtil.d(TAG, "onChangeMassagerEffect, id=" + id + ", effect=" + effect);
        } else if (type == 3) {
            LogUtil.d(TAG, "onChangeMassagerIntensity, id=" + id + ", intensity=" + data);
        } else if (type == 8) {
            LogUtil.d(TAG, "onErrorMassager, id=" + id + ", effect=" + effect + ", operation=" + parseOpCode(data >> 4) + ", errCode=" + (data & 15));
        }
        synchronized (mListener) {
            for (SeatMassagerListener listener : mListener) {
                listener.onMassagerEvent(id, type, effect, data);
            }
        }
    }

    private void handleBiVibrate(List<Integer> ids, int type, String effect, int data, String owner) {
        BiVibrate biVibrate;
        if (type != 0) {
            if (type != 1 || (biVibrate = mBiVibrate) == null) {
                if (type != 8 && type != 3) {
                    for (Integer id : ids) {
                        if (!mBiRhythmMap.containsKey(id) && type == 4 && data != 0) {
                            int intensity = getProviderRhythmIntensity(id.intValue());
                            String pattern = String.valueOf(getProviderRhythmPattern());
                            mBiRhythmMap.put(id, new BiMassager(id.intValue(), "rhythm", owner, pattern, intensity));
                        } else if (mBiRhythmMap.containsKey(id)) {
                            BiMassager uploader = mBiRhythmMap.get(id);
                            if (type != 6) {
                                if (type == 5) {
                                    uploader.addIntensity(data);
                                } else if (type == 4 && data == 0) {
                                    uploader.submit();
                                    mBiRhythmMap.remove(id);
                                }
                            } else {
                                uploader.addPattern(String.valueOf(data));
                            }
                        }
                    }
                    return;
                }
                return;
            }
            biVibrate.submit();
            mBiVibrate = null;
            return;
        }
        mBiVibrate = new BiVibrate(ids, owner, effect, data);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showVibrateEvent(List<Integer> ids, int type, String effect, int data, String owner) {
        handleBiVibrate(ids, type, effect, data, owner);
        if (mListener.isEmpty()) {
            return;
        }
        switch (type) {
            case 0:
            case 1:
                StringBuilder sb = new StringBuilder();
                sb.append("on");
                sb.append(type == 0 ? "Start" : AiCmd.STOP);
                sb.append("Vibrate, ids=");
                sb.append(ids.toString());
                sb.append(", effect=");
                sb.append(effect);
                sb.append(", position=");
                sb.append(data);
                LogUtil.d(TAG, sb.toString());
                break;
            case 3:
            case 5:
                StringBuilder sb2 = new StringBuilder();
                sb2.append("onChange");
                sb2.append(type == 3 ? "Vibrate" : "Rhythm");
                sb2.append("Intensity, ids=");
                sb2.append(ids.toString());
                sb2.append(", intensity=");
                sb2.append(data);
                LogUtil.d(TAG, sb2.toString());
                break;
            case 4:
                StringBuilder sb3 = new StringBuilder();
                sb3.append("onChangeRhythmEnable: ");
                sb3.append(data != 0 ? " enable" : " disable");
                sb3.append(", ids=");
                sb3.append(ids.toString());
                LogUtil.d(TAG, sb3.toString());
                break;
            case 6:
                LogUtil.d(TAG, "onChangeRhythmPattern, ids=" + ids.toString() + ", pattern=" + data);
                break;
            case 7:
                StringBuilder sb4 = new StringBuilder();
                sb4.append("onLoadVibrate, effect=");
                sb4.append(effect);
                sb4.append(", result: ");
                sb4.append(data != 0 ? "succeed" : "failed!");
                LogUtil.d(TAG, sb4.toString());
                break;
            case 8:
                LogUtil.d(TAG, "onErrorVibrate, ids=" + ids.toString() + ", effect=" + effect + ", operation=" + parseOpCode(data >> 4) + ", errCode=" + (data & 15));
                break;
        }
        synchronized (mListener) {
            for (SeatMassagerListener listener : mListener) {
                listener.onVibrateEvent(ids, type, effect, data);
            }
        }
    }

    private void setDriverSeatMassagerMode(int id, int mode) throws Exception {
        int signalId = getSignalMode(id);
        CarMsmManager msm = mCar.getCarManager("xp_msm");
        mStatusMap.put(Integer.valueOf(signalId), Integer.valueOf(mode));
        if (getDriverSeatMassagerMode(id) != mode) {
            LogUtil.i(TAG, "setDriverSeatMassagerMode, id=" + id + ", mode=" + mode);
            synchronized (mLockMap.get(Integer.valueOf(signalId))) {
                try {
                    try {
                        switch (signalId) {
                            case 557849914:
                                msm.setDriverSeatMassgProgMode(mode);
                                break;
                            case 557849917:
                                msm.setPassengerSeatMassgProgMode(mode);
                                break;
                            case 557849918:
                                msm.setSecRowLeftSeatMassgProgMode(mode);
                                break;
                            case 557849920:
                                msm.setSecRowRightSeatMassgProgMode(mode);
                                break;
                        }
                        mLockMap.get(Integer.valueOf(signalId)).wait(500L);
                    } catch (Exception e) {
                        throw new Exception("setMassagerMode " + id + " failed, " + e);
                    }
                } finally {
                }
            }
        }
    }

    private int getDriverSeatMassagerMode(int id) throws Exception {
        CarMsmManager msm = mCar.getCarManager("xp_msm");
        LogUtil.v(TAG, "getDriverSeatMassagerMode, id=" + id);
        switch (getSignalMode(id)) {
            case 557849914:
                return msm.getDriverSeatMassgProgMode();
            case 557849915:
            case 557849916:
            case 557849919:
            default:
                throw new Exception("Invalid Arguments: id=" + id);
            case 557849917:
                return msm.getPassengerSeatMassgProgMode();
            case 557849918:
                return msm.getSecRowLeftSeatMassgProgMode();
            case 557849920:
                return msm.getSecRowRightSeatMassgProgMode();
        }
    }

    private boolean getDriverSeatMassagerErrorStatus(int id) throws Exception {
        CarMsmManager msm = mCar.getCarManager("xp_msm");
        LogUtil.v(TAG, "getDriverSeatMassagerErrorStatus, id=" + id);
        switch (getSignalStatus(id)) {
            case 557849953:
                return msm.getDriverSeatMassgErrorStatus() == 0;
            case 557849954:
                return msm.getPassengerSeatMassgErrorStatus() == 0;
            case 557849955:
                return msm.getSecRowLeftSeatMassgErrorStatus() == 0;
            case 557849956:
                return msm.getSecRowRightSeatMassgErrorStatus() == 0;
            default:
                throw new Exception("Invalid Arguments: id=" + id);
        }
    }

    private void setDriverSeatMassagerIntensity(int id, int intensity) throws Exception {
        int signalId = getSignalIntensity(id);
        CarMsmManager msm = mCar.getCarManager("xp_msm");
        mStatusMap.put(Integer.valueOf(signalId), Integer.valueOf(intensity));
        if (getDriverSeatMassagerIntensity(id) != intensity) {
            LogUtil.i(TAG, "setDriverSeatMassagerIntensity, id=" + id + ", intensity=" + intensity);
            synchronized (mLockMap.get(Integer.valueOf(signalId))) {
                try {
                    try {
                        switch (signalId) {
                            case 557849915:
                                msm.setDriverSeatMassgIntensity(intensity);
                                break;
                            case 557849916:
                                msm.setPassengerSeatMassgIntensity(intensity);
                                break;
                            case 557849919:
                                msm.setSecRowLeftSeatMassgIntensity(intensity);
                                break;
                            case 557849921:
                                msm.setSecRowRightSeatMassgIntensity(intensity);
                                break;
                        }
                        mLockMap.get(Integer.valueOf(signalId)).wait(500L);
                    } catch (Exception e) {
                        throw new Exception("setMassagerIntensity " + id + " failed, " + e);
                    }
                } finally {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDriverSeatMassagerIntensity(int id) throws Exception {
        CarMsmManager msm = mCar.getCarManager("xp_msm");
        LogUtil.v(TAG, "getDriverSeatMassagerIntensity, id=" + id);
        switch (getSignalIntensity(id)) {
            case 557849915:
                return msm.getDriverSeatMassgIntensity();
            case 557849916:
                return msm.getPassengerSeatMassgIntensity();
            case 557849917:
            case 557849918:
            case 557849920:
            default:
                throw new Exception("Invalid Arguments: id=" + id);
            case 557849919:
                return msm.getSecRowLeftSeatMassgIntensity();
            case 557849921:
                return msm.getSecRowRightSeatMassgIntensity();
        }
    }

    private void startDriverSeatMassager(int id, int mode, int intensity) throws Exception {
        LogUtil.d(TAG, "startDriverSeatMassager, id=" + id + ", mode=" + mode + ", intensity=" + intensity);
        setDriverSeatMassagerMode(id, mode);
        setDriverSeatMassagerIntensity(id, intensity);
    }

    private void stopDriverSeatMassager(int id) throws Exception {
        LogUtil.d(TAG, "stopDriverSeatMassager, id=" + id);
        setDriverSeatMassagerMode(id, 0);
    }

    private boolean isDriverSeatMassagerRunning(int id) throws Exception {
        return getDriverSeatMassagerMode(id) != 0;
    }

    private void setDriverSeatVibrateRhythmEnable(boolean enable) {
        StringBuilder sb = new StringBuilder();
        sb.append("setDriverSeatVibrateRhythmEnable ");
        sb.append(enable ? "enable" : "disable");
        LogUtil.d(TAG, sb.toString());
        try {
            mAudioManager.setMusicSeatEnable(enable);
        } catch (Exception e) {
            LogUtil.e(TAG, "setDriverSeatVibrateRhythmEnable failed " + e);
        }
    }

    private void setDriverSeatVibrateRhythmPattern(int pattern) {
        LogUtil.d(TAG, "setDriverSeatVibrateRhythmPattern " + pattern);
        try {
            mAudioManager.setMusicSeatEffect(pattern);
        } catch (Exception e) {
            LogUtil.e(TAG, "setDriverSeatVibrateRhythmPattern failed " + e);
        }
    }

    private void setDriverSeatVibrateRythmPause(boolean pause) {
        LogUtil.v(TAG, "setDriverSeatVibrateRythmPause " + pause);
        try {
            mAudioManager.setMusicSeatRythmPause(pause);
        } catch (Exception e) {
            LogUtil.e(TAG, "setDriverSeatVibrateRythmPause failed " + e);
        }
    }

    private void setDriverSeatVibrateIntensity(List<Integer> intensity) {
        if (!((String) mIntensity.stream().map($$Lambda$znfQj8LqOvyui6ncUHU4komPIHY.INSTANCE).collect(Collectors.joining())).equals(intensity.stream().map($$Lambda$znfQj8LqOvyui6ncUHU4komPIHY.INSTANCE).collect(Collectors.joining()))) {
            LogUtil.d(TAG, "setDriverSeatVibrateIntensity, intensity=" + intensity.toString());
            mAudioManager.setMassageSeatLevel(new ArrayList(intensity));
            mIntensity = intensity;
        }
    }

    private void initRhythmEnableAndIntensity() {
        mRhythmPattern = "rhythm" + getProviderRhythmPattern();
        int i = 0;
        while (true) {
            int intensity = 0;
            if (i < 2) {
                if (getProviderRhythmEnable(i)) {
                    intensity = getProviderRhythmIntensity(i);
                }
                mRhythmIntensityMap.put(Integer.valueOf(i), parseVibrateIntensity(mRhythmPattern, intensity, 2));
                i++;
            } else {
                List<Integer> dist = makeVibrateIntensity(mRhythmIntensityMap);
                hasRhythm = !hasMuteIntensity(dist);
                setDriverSeatVibrateIntensity(dist);
                setDriverSeatVibrateRhythmPattern(getProviderRhythmPattern());
                setDriverSeatVibrateRhythmEnable(hasRhythm);
                setDriverSeatVibrateRythmPause(false);
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initMassagerStatus() {
        LogUtil.i(TAG, "initMassagerStatus");
        for (int id = 0; id < 4; id++) {
            try {
                mStatusMap.put(Integer.valueOf(getSignalMode(id)), Integer.valueOf(getDriverSeatMassagerMode(id)));
            } catch (Exception e) {
                mStatusMap.put(Integer.valueOf(getSignalMode(id)), 0);
            }
            try {
                mStatusMap.put(Integer.valueOf(getSignalIntensity(id)), Integer.valueOf(getDriverSeatMassagerIntensity(id)));
            } catch (Exception e2) {
                mStatusMap.put(Integer.valueOf(getSignalMode(id)), 0);
            }
        }
    }

    private int getSoundDuration(String path) {
        int duration = -1;
        MediaPlayer mp = new MediaPlayer();
        try {
            try {
                mp.setDataSource(path);
                mp.prepare();
                duration = mp.getDuration();
            } catch (Exception e) {
                LogUtil.e(TAG, "get " + path + " duration failed, " + e);
            }
            return duration;
        } finally {
            mp.release();
        }
    }

    private int getSoundDuration(AssetFileDescriptor fd) {
        int duration = -1;
        MediaPlayer mp = new MediaPlayer();
        try {
            try {
                mp.setDataSource(fd);
                mp.prepare();
                duration = mp.getDuration();
            } catch (Exception e) {
                LogUtil.e(TAG, "getSoundDuration failed, " + e);
            }
            return duration;
        } finally {
            mp.release();
        }
    }

    private synchronized Effect getEffect(String name) {
        return mEffectMap.containsKey(name) ? mEffectMap.get(name) : null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStartMassager(List<Seat> seats, String name, String owner) {
        Effect effect = getEffect(name);
        for (Seat seat : seats) {
            int id = seat.getId();
            int intensity = seat.getIntensity();
            LogUtil.i(TAG, "handleStartMassager, id=" + id + ", effect=" + name + "(" + effect.mode + "), intensity=" + intensity);
            try {
            } catch (Exception e) {
                LogUtil.e(TAG, "handleStartMassager " + id + " failed (-3), " + e);
                showMassagerEvent(id, 8, name, 3 | 0, null);
            }
            if (!hasSupportMassager(id)) {
                throw new Exception("No such device! seatId=" + id);
            } else if (!getDriverSeatMassagerErrorStatus(id)) {
                throw new Exception("Bad seatMassager status!");
            } else {
                if (effect.hasPreset) {
                    startDriverSeatMassager(id, effect.mode, intensity);
                } else {
                    setDriverSeatMassagerMode(id, effect.mode);
                    ScheduledFuture<?> future = mExecutor.scheduleAtFixedRate(new EffectTask(id, effect), 200L, 10L, TimeUnit.MILLISECONDS);
                    mTaskMap.put(Integer.valueOf(id), future);
                }
                setProviderMassagerEffect(id, name);
                setProviderMassagerIntensity(id, intensity);
                showMassagerEvent(id, 0, name, intensity, owner);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStopMassager(List<Integer> ids) {
        LogUtil.i(TAG, "handleStopMassager, ids=" + ids.toString());
        for (Integer id : ids) {
            String name = getProviderMassagerEffect(id.intValue());
            int intensity = getProviderMassagerIntensity(id.intValue());
            int errCode = 3;
            try {
            } catch (Exception e) {
                LogUtil.e(TAG, "handleStopMassager " + id + " failed, " + e);
                showMassagerEvent(id.intValue(), 8, name, errCode | 16, null);
            }
            if (!hasSupportMassager(id.intValue())) {
                errCode = 1;
                throw new Exception("No such device! seatId=" + id);
                break;
            }
            if (mTaskMap.containsKey(id) && !mTaskMap.get(id).isDone()) {
                mTaskMap.get(id).cancel(true);
            }
            stopDriverSeatMassager(id.intValue());
            showMassagerEvent(id.intValue(), 1, name, intensity, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSetMassagerEffect(List<Integer> ids, String dist) {
        Effect effect = getEffect(dist);
        LogUtil.i(TAG, "handleSetMassagerEffect, ids=" + ids.toString() + ", effect=" + dist);
        for (Integer id : ids) {
            String source = getProviderMassagerEffect(id.intValue());
            int errCode = 3;
            if (!dist.equals(source)) {
                try {
                    if (hasSupportMassager(id.intValue())) {
                        if (isDriverSeatMassagerRunning(id.intValue())) {
                            LogUtil.d(TAG, "set MassagerEffect, id=" + id + ", effect=" + source + " -> " + dist);
                            if (mTaskMap.containsKey(id) && !mTaskMap.get(id).isDone()) {
                                mTaskMap.get(id).cancel(true);
                            }
                            setDriverSeatMassagerMode(id.intValue(), effect.mode);
                            if (!effect.hasPreset) {
                                ScheduledFuture<?> future = mExecutor.scheduleAtFixedRate(new EffectTask(id.intValue(), effect), 2000L, 10L, TimeUnit.MILLISECONDS);
                                mTaskMap.put(id, future);
                            }
                        }
                        setProviderMassagerEffect(id.intValue(), dist);
                        showMassagerEvent(id.intValue(), 2, dist, getProviderMassagerIntensity(id.intValue()), null);
                    } else {
                        errCode = 1;
                        throw new Exception("No such device! seatId=" + id);
                        break;
                    }
                } catch (Exception e) {
                    LogUtil.e(TAG, "handleSetMassagerEffect " + id + " failed, " + e);
                    showMassagerEvent(id.intValue(), 8, dist, errCode | 32, null);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSetMassagerIntensity(List<Integer> ids, int dist) {
        LogUtil.i(TAG, "handleSetMassagerIntensity, ids=" + ids.toString() + ", intensity=" + dist);
        for (Integer id : ids) {
            Effect effect = getEffect(getProviderMassagerEffect(id.intValue()));
            int source = getProviderMassagerIntensity(id.intValue());
            int errCode = 3;
            if (dist != source) {
                try {
                    if (hasSupportMassager(id.intValue())) {
                        if (isDriverSeatMassagerRunning(id.intValue()) && effect.hasPreset) {
                            LogUtil.d(TAG, "set MassagerIntensity, id=" + id + ", intensity=" + source + " -> " + dist);
                            setDriverSeatMassagerIntensity(id.intValue(), dist);
                        }
                        setProviderMassagerIntensity(id.intValue(), dist);
                        showMassagerEvent(id.intValue(), 3, effect.name, dist, null);
                    } else {
                        errCode = 1;
                        throw new Exception("No such device! seatId=" + id);
                        break;
                    }
                } catch (Exception e) {
                    LogUtil.e(TAG, "handleSetMassagerIntensity " + id + " failed, " + e);
                    showMassagerEvent(id.intValue(), 8, effect.name, errCode | 48, null);
                }
            }
        }
    }

    private List<Integer> makeVibrateIntensity(Map<Integer, List<Integer>> map) {
        List<Integer> intensity = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            intensity.addAll(map.containsKey(Integer.valueOf(i)) ? map.get(Integer.valueOf(i)) : new ArrayList<>(Collections.nCopies(2, 0)));
        }
        LogUtil.v(TAG, "makeVibrateIntensity " + intensity.toString());
        return intensity;
    }

    private boolean hasMuteIntensity(List<Integer> intensity) {
        return intensity.stream().mapToInt(new ToIntFunction() { // from class: com.xiaopeng.xuiservice.capabilities.smm.-$$Lambda$UV1wDVoVlbcxpr8zevj_aMFtUGw
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(Object obj) {
                return ((Integer) obj).intValue();
            }
        }).sum() == 0;
    }

    private List<Integer> parseVibrateIntensity(String pattern, int intensity, int position) {
        List<Integer> dist = new ArrayList<>(Collections.nCopies(2, 0));
        if (mMapIntensity.containsKey(pattern)) {
            Map<Integer, List<Integer>> mapping = mMapIntensity.get(pattern);
            if (mapping.containsKey(Integer.valueOf(intensity))) {
                dist = new ArrayList<>(mapping.get(Integer.valueOf(intensity)));
            }
            Map<Integer, List<Integer>> mapping2 = mMapIntensity.get("position");
            if (mapping2.containsKey(Integer.valueOf(position))) {
                List<Integer> mask = mapping2.get(Integer.valueOf(position));
                for (int i = 0; i < dist.size(); i++) {
                    dist.set(i, Integer.valueOf(dist.get(i).intValue() * mask.get(i).intValue()));
                }
            }
        }
        return dist;
    }

    private void doVibrateEffect(List<Integer> ids, String effect, int loop, int position, Map<Integer, List<Integer>> intensityMap, String owner) {
        Pair<Integer, Integer> sound;
        Pair<Integer, Integer> sound2 = mSoundMap.get(effect);
        setDriverSeatVibrateRythmPause(true);
        setDriverSeatVibrateIntensity(makeVibrateIntensity(intensityMap));
        int streamId = mSoundPool.play(((Integer) sound2.first).intValue(), 1.0f, 1.0f, 0, loop, 1.0f);
        StringBuilder sb = new StringBuilder();
        sb.append("handleDoVibrate ");
        sb.append(streamId != 0 ? "succeed" : "failed");
        sb.append(", ids=");
        sb.append(ids.toString());
        sb.append(", effect=");
        sb.append(effect);
        sb.append(", loop=");
        sb.append(loop);
        sb.append(", position=");
        sb.append(position);
        sb.append(", streamId=");
        sb.append(streamId);
        LogUtil.i(TAG, sb.toString());
        if (streamId != 0) {
            Vibrate vibrate = mVibrate;
            if (vibrate == null) {
                sound = sound2;
            } else {
                sound = sound2;
                showVibrateEvent(vibrate.ids, 1, mVibrate.effect, mVibrate.position, null);
            }
            mVibrate = new Vibrate(streamId, ids, effect, position);
            if (loop >= 0) {
                sendDoneVibrateDelayed((((Integer) sound.second).intValue() * (loop + 1)) + 500);
            }
            showVibrateEvent(ids, 0, effect, position, owner);
            return;
        }
        showVibrateEvent(ids, 8, effect, 5, null);
        if (hasRhythm && mVibrate == null) {
            setDriverSeatVibrateIntensity(makeVibrateIntensity(mRhythmIntensityMap));
        }
        setDriverSeatVibrateRythmPause(false);
    }

    private void doRhythmEffect(List<Integer> ids, String effect, int duration, int position, Map<Integer, List<Integer>> intensityMap, String owner) {
        StringBuilder sb = new StringBuilder();
        sb.append("handleDoVibrate ");
        sb.append(!hasRhythm ? "succeed" : "failed");
        sb.append(", ids=");
        sb.append(ids.toString());
        sb.append(", effect=");
        sb.append(effect);
        sb.append(", duration=");
        sb.append(duration);
        sb.append(", position=");
        sb.append(position);
        LogUtil.i(TAG, sb.toString());
        if (!hasRhythm) {
            Vibrate vibrate = mVibrate;
            if (vibrate != null) {
                if (vibrate.streamId != 0) {
                    mSoundPool.stop(mVibrate.streamId);
                    setDriverSeatVibrateRythmPause(false);
                }
                showVibrateEvent(mVibrate.ids, 1, mVibrate.effect, mVibrate.position, null);
            }
            mVibrate = new Vibrate(0, ids, effect, position);
            setDriverSeatVibrateIntensity(makeVibrateIntensity(intensityMap));
            setDriverSeatVibrateRhythmEnable(true);
            if (duration > 0) {
                sendDoneVibrateDelayed(duration);
            }
            showVibrateEvent(ids, 0, effect, position, owner);
            return;
        }
        showVibrateEvent(ids, 8, effect, 4, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDoVibrate(List<Seat> seats, String effect, int loop, int position, String owner) {
        Map<Integer, List<Integer>> intensityMap = new ArrayMap<>();
        List<Integer> ids = new ArrayList<>();
        String str = ThemeManager.AttributeSet.BACKGROUND;
        boolean hasVibrate = !effect.equals(ThemeManager.AttributeSet.BACKGROUND);
        if (hasVibrate) {
            str = "vibrate";
        }
        String pattern = str;
        for (Seat seat : seats) {
            int id = seat.getId();
            int intensity = seat.getIntensity();
            ids.add(Integer.valueOf(id));
            mIntensitysMap.put(Integer.valueOf(id), Integer.valueOf(intensity));
            intensityMap.put(Integer.valueOf(id), parseVibrateIntensity(pattern, intensity, position));
        }
        if (hasVibrate) {
            doVibrateEffect(ids, effect, loop, position, intensityMap, owner);
        } else {
            doRhythmEffect(ids, effect, loop, position, intensityMap, owner);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDoneVibrate() {
        if (mVibrate == null) {
            return;
        }
        LogUtil.i(TAG, "handleDoneVibrate, ids=" + mVibrate.ids.toString() + ", effect=" + mVibrate.effect + ", streamId=" + mVibrate.streamId);
        if (mVibrate.streamId != 0) {
            mSoundPool.stop(mVibrate.streamId);
            setDriverSeatVibrateRythmPause(false);
        }
        showVibrateEvent(mVibrate.ids, 1, mVibrate.effect, mVibrate.position, null);
        if (hasRhythm) {
            setDriverSeatVibrateIntensity(makeVibrateIntensity(mRhythmIntensityMap));
        } else {
            setDriverSeatVibrateRhythmEnable(false);
        }
        mVibrate = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSetRhythmEnable(List<Integer> ids, boolean enable, String owner) {
        LogUtil.i(TAG, "handleSetRhythmEnable, ids=" + ids.toString() + ", enable=" + enable);
        for (Integer id : ids) {
            if (enable != getProviderRhythmEnable(id.intValue())) {
                int intensity = enable ? getProviderRhythmIntensity(id.intValue()) : 0;
                mRhythmIntensityMap.put(id, parseVibrateIntensity(mRhythmPattern, intensity, 2));
                setProviderRhythmEnable(id.intValue(), enable);
            }
        }
        List<Integer> dist = makeVibrateIntensity(mRhythmIntensityMap);
        hasRhythm = !hasMuteIntensity(dist);
        if (hasRhythm && mVibrate == null) {
            setDriverSeatVibrateIntensity(dist);
        }
        setDriverSeatVibrateRhythmEnable(hasRhythm);
        showVibrateEvent(ids, 4, new String(), enable ? 1 : 0, owner);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSetVibrateIntensity(List<Integer> ids, int intensity) {
        LogUtil.i(TAG, "handleSetVibrateIntensity, ids=" + ids.toString() + ", intensity=" + intensity);
        for (Integer id : ids) {
            setProviderVibrateIntensity(id.intValue(), intensity);
        }
        showVibrateEvent(ids, 3, new String(), intensity, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSetRhythmIntensity(List<Integer> ids, int intensity) {
        LogUtil.i(TAG, "handleSetRhythmIntensity, ids=" + ids.toString() + ", intensity=" + intensity);
        for (Integer id : ids) {
            if (intensity != getProviderRhythmIntensity(id.intValue())) {
                if (getProviderRhythmEnable(id.intValue())) {
                    mRhythmIntensityMap.put(id, parseVibrateIntensity(mRhythmPattern, intensity, 2));
                }
                setProviderRhythmIntensity(id.intValue(), intensity);
            }
        }
        if (mVibrate == null && hasRhythm) {
            setDriverSeatVibrateIntensity(makeVibrateIntensity(mRhythmIntensityMap));
        }
        showVibrateEvent(ids, 5, new String(), intensity, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSetRhythmPattern(int pattern) {
        mRhythmPattern = "rhythm" + pattern;
        LogUtil.i(TAG, "handleSetRhythmPattern " + pattern);
        if (pattern != getProviderRhythmPattern()) {
            setDriverSeatVibrateRhythmPattern(pattern);
            for (int i = 0; i < 2; i++) {
                int intensity = getProviderRhythmEnable(i) ? getProviderRhythmIntensity(i) : 0;
                mRhythmIntensityMap.put(Integer.valueOf(i), parseVibrateIntensity(mRhythmPattern, intensity, 2));
            }
            setProviderRhythmPattern(pattern);
        }
        if (hasRhythm) {
            setDriverSeatVibrateIntensity(makeVibrateIntensity(mRhythmIntensityMap));
        }
        showVibrateEvent(new ArrayList(), 6, new String(), pattern, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int sendStartMassager(Massager obj) {
        Message message = this.mHandler.obtainMessage();
        message.what = 0;
        message.obj = obj;
        LogUtil.v(TAG, "sendStartMassager");
        return (hasInit && this.mHandler.sendMessage(message)) ? 0 : -5;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized int sendStopMassager(List<Integer> ids) {
        int i;
        Message message = this.mHandler.obtainMessage();
        message.what = 1;
        message.obj = ids;
        LogUtil.v(TAG, "sendStopMassager");
        if (hasInit) {
            i = this.mHandler.sendMessage(message) ? 0 : -5;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int sendSetMassagerEffect(Massager obj) {
        Message message = this.mHandler.obtainMessage();
        message.what = 2;
        message.obj = obj;
        LogUtil.v(TAG, "sendSetMassagerEffect");
        return (hasInit && this.mHandler.sendMessage(message)) ? 0 : -5;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int sendSetMassagerIntensity(List<Integer> ids, int intensity) {
        Message message = this.mHandler.obtainMessage();
        message.what = 3;
        message.obj = ids;
        message.arg1 = intensity;
        LogUtil.v(TAG, "sendSetMassagerIntensity");
        return (hasInit && this.mHandler.sendMessage(message)) ? 0 : -5;
    }

    private int sendDoVibrate(Vibrate obj) {
        Message message = this.mHandler.obtainMessage();
        message.what = 4;
        message.obj = obj;
        this.mHandler.removeMessages(5);
        this.mHandler.removeMessages(4);
        LogUtil.v(TAG, "sendDoVibrate");
        return this.mHandler.sendMessage(message) ? 0 : -5;
    }

    private int sendDoneVibrateDelayed(int delay) {
        Message message = this.mHandler.obtainMessage();
        message.what = 5;
        this.mHandler.removeMessages(5);
        LogUtil.d(TAG, "sendDoneVibrateDelayed after " + delay + "ms");
        return this.mHandler.sendMessageDelayed(message, (long) delay) ? 0 : -5;
    }

    private int sendSetRhythmEnable(Vibrate obj) {
        Message message = this.mHandler.obtainMessage();
        message.what = 7;
        message.obj = obj;
        LogUtil.v(TAG, "sendSetRhythmEnable");
        return this.mHandler.sendMessage(message) ? 0 : -5;
    }

    private int sendSetVibrateIntensity(List<Integer> ids, int intensity) {
        Message message = this.mHandler.obtainMessage();
        message.what = 6;
        message.obj = ids;
        message.arg1 = intensity;
        LogUtil.v(TAG, "sendSetVibrateIntensity");
        return this.mHandler.sendMessage(message) ? 0 : -5;
    }

    private int sendSetRhythmIntensity(List<Integer> ids, int intensity) {
        Message message = this.mHandler.obtainMessage();
        message.what = 8;
        message.obj = ids;
        message.arg1 = intensity;
        LogUtil.v(TAG, "sendSetRhythmIntensity");
        return this.mHandler.sendMessage(message) ? 0 : -5;
    }

    private int sendSetRhythmPattern(int pattern) {
        Message message = this.mHandler.obtainMessage();
        message.what = 9;
        message.arg1 = pattern;
        LogUtil.v(TAG, "sendSetRhythmPattern");
        return this.mHandler.sendMessage(message) ? 0 : -5;
    }

    public int getSeatMassagerNumbers() {
        return 4;
    }

    public int getSeatVibrateNumbers() {
        return 2;
    }

    public void setSeatMassagerListener(SeatMassagerListener listener) {
        StringBuilder sb = new StringBuilder();
        sb.append("setSeatMassagerListener ");
        sb.append(listener == null ? "invalid" : "ok");
        LogUtil.i(TAG, sb.toString());
        if (listener != null) {
            synchronized (mListener) {
                mListener.add(listener);
            }
        }
    }

    public void unsetSeatMassagerListener(SeatMassagerListener listener) {
        StringBuilder sb = new StringBuilder();
        sb.append("unsetSeatMassagerListener ");
        sb.append(listener == null ? "invalid" : "ok");
        LogUtil.i(TAG, sb.toString());
        if (listener != null) {
            synchronized (mListener) {
                mListener.remove(listener);
            }
        }
    }

    public synchronized void loadMassagerEffect(String path) {
        Effect effect = new Effect().readFromJson(path);
        if (effect != null && !mEffectMap.containsKey(effect.name)) {
            LogUtil.i(TAG, "loadMassagerEffect " + path);
            mEffectMap.put(effect.name, effect);
            if (effect.hasPreset) {
                mModeMap.put(Integer.valueOf(effect.mode), effect.name);
            }
        }
    }

    public synchronized void clearMassagerEffect() {
        mEffectMap.clear();
        mModeMap.clear();
    }

    public synchronized List<String> showMassagerEffect() {
        return mEffectMap.isEmpty() ? new ArrayList() : new ArrayList(mEffectMap.keySet());
    }

    public synchronized int startMassager(List<Seat> seats, String effect) {
        String pkg;
        int pid = Binder.getCallingPid();
        pkg = PackageUtils.getPackageName(pid);
        return (checkMassagerSeats(seats) && checkMassagerEffect(effect)) ? sendStartMassager(new Massager(null, seats, effect, pkg)) : -2;
    }

    public synchronized int stopMassager(List<Integer> ids) {
        return checkMassagerIds(ids) ? sendStopMassager(ids) : -2;
    }

    public synchronized int setMassagerEffect(List<Integer> ids, String effect) {
        return (checkMassagerIds(ids) && checkMassagerEffect(effect)) ? sendSetMassagerEffect(new Massager(ids, null, effect, null)) : -2;
    }

    public synchronized String getMassagerEffect(int id) {
        return checkMassagerId(id) ? getProviderMassagerEffect(id) : new String();
    }

    public synchronized int setMassagerIntensity(List<Integer> ids, int intensity) {
        return (checkMassagerIds(ids) && checkMassagerIntensity(intensity)) ? sendSetMassagerIntensity(ids, intensity) : -2;
    }

    public synchronized int getMassagerIntensity(int id) {
        return checkMassagerId(id) ? getProviderMassagerIntensity(id) : -2;
    }

    public synchronized int getMassagerStatus(int id) {
        int ret = -2;
        if (checkMassagerId(id)) {
            try {
                return isDriverSeatMassagerRunning(id) ? 1 : 0;
            } catch (Exception e) {
                LogUtil.e(TAG, "getMassagerStatus " + id + " failed, " + e);
                ret = -3;
            }
        }
        return ret;
    }

    public synchronized void clearVibrateEffect() {
        mSoundMap.clear();
        initSoundPool();
    }

    public synchronized void loadVibrateEffect(String path) {
        String name = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf(".wav"));
        int duration = getSoundDuration(path);
        if (!mSoundMap.containsKey(name)) {
            try {
                int sampleId = mSoundPool.load(path, 1);
                if (sampleId > 0 && duration > 0) {
                    mSoundMap.put(name, new Pair<>(Integer.valueOf(sampleId), Integer.valueOf(duration)));
                }
                StringBuilder sb = new StringBuilder();
                sb.append("loadVibrateEffect ");
                sb.append(name);
                sb.append(", path=");
                sb.append(path);
                sb.append((sampleId <= 0 || duration <= 0) ? " failed!" : " succeed");
                LogUtil.i(TAG, sb.toString());
            } catch (Exception e) {
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x00b7  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00ba A[Catch: all -> 0x00e0, TryCatch #0 {, blocks: (B:23:0x0058, B:26:0x005d, B:27:0x0070, B:37:0x00a6, B:41:0x00d0, B:40:0x00ba, B:44:0x00dc, B:49:0x00e3, B:50:0x00f9, B:33:0x008d, B:36:0x0092), top: B:53:0x0002, inners: #2, #6, #7 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized int loadVibrate(android.content.res.AssetFileDescriptor r8, java.lang.String r9) {
        /*
            Method dump skipped, instructions count: 252
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xuiservice.capabilities.smm.XpSeatMassagerService.loadVibrate(android.content.res.AssetFileDescriptor, java.lang.String):int");
    }

    public synchronized List<String> showVibrateEffect() {
        return mSoundMap.isEmpty() ? new ArrayList() : new ArrayList(mSoundMap.keySet());
    }

    public synchronized int setRhythmEnable(List<Integer> ids, boolean enable) {
        String pkg;
        int pid = Binder.getCallingPid();
        pkg = PackageUtils.getPackageName(pid);
        return checkVibrateIds(ids) ? sendSetRhythmEnable(new Vibrate(ids, enable, pkg)) : -2;
    }

    public synchronized int getRhythmEnable(int id) {
        return checkVibrateId(id) ? getProviderRhythmEnable(id) ? 1 : 0 : -2;
    }

    public synchronized int doVibrate(List<Seat> seats, String effect, int loop, int position) {
        String pkg;
        int pid = Binder.getCallingPid();
        pkg = PackageUtils.getPackageName(pid);
        return (checkVibrateSeats(seats) && checkVibrateEffect(effect) && checkPosition(position)) ? sendDoVibrate(new Vibrate(seats, effect, loop, position, pkg)) : -2;
    }

    public synchronized int stopVibrate() {
        return sendDoneVibrateDelayed(0);
    }

    public synchronized int setVibrateIntensity(List<Integer> ids, int intensity) {
        return (checkVibrateIds(ids) && checkVibrateIntensity(intensity)) ? sendSetVibrateIntensity(ids, intensity) : -2;
    }

    public synchronized int getVibrateIntensity(int id) {
        return checkVibrateId(id) ? getProviderVibrateIntensity(id) : -2;
    }

    public synchronized int setRhythmIntensity(List<Integer> ids, int intensity) {
        return (checkVibrateIds(ids) && checkVibrateIntensity(intensity)) ? sendSetRhythmIntensity(ids, intensity) : -2;
    }

    public synchronized int getRhythmIntensity(int id) {
        return checkVibrateId(id) ? getProviderRhythmIntensity(id) : -2;
    }

    public synchronized int setRhythmPattern(int pattern) {
        return checkRhythmPattern(pattern) ? sendSetRhythmPattern(pattern) : -2;
    }

    public synchronized int getRhythmPattern() {
        return getProviderRhythmPattern();
    }

    private synchronized void initSoundPool() {
        LogUtil.i(TAG, "initSoundPool");
        AudioAttributes audioAttributes = new AudioAttributes.Builder().setLegacyStreamType(12).build();
        SoundPool.OnLoadCompleteListener listener = new SoundPool.OnLoadCompleteListener() { // from class: com.xiaopeng.xuiservice.capabilities.smm.XpSeatMassagerService.1
            @Override // android.media.SoundPool.OnLoadCompleteListener
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                LogUtil.i(XpSeatMassagerService.TAG, "OnLoadComplete sampleId=" + sampleId + ", status=" + status);
                if (XpSeatMassagerService.mSoundLoadMap.containsKey(Integer.valueOf(sampleId))) {
                    String effect = (String) XpSeatMassagerService.mSoundLoadMap.get(Integer.valueOf(sampleId));
                    XpSeatMassagerService.this.showVibrateEvent(new ArrayList(), 7, effect, status == 0 ? 1 : 0, null);
                    XpSeatMassagerService.mSoundLoadMap.remove(Integer.valueOf(sampleId));
                }
            }
        };
        if (mSoundPool != null) {
            mSoundPool.autoPause();
            mSoundPool.release();
        }
        mSoundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(1).build();
        mSoundPool.setOnLoadCompleteListener(listener);
    }

    private void addMsmManagerCallback() {
        LogUtil.i(TAG, "addMsmManagerCallback");
        mMsmCallback = new CarMsmManager.CarMsmEventCallback() { // from class: com.xiaopeng.xuiservice.capabilities.smm.XpSeatMassagerService.2
            public void onChangeEvent(CarPropertyValue value) {
                int signalId = value.getPropertyId();
                int signalVal = value.getValue() instanceof Integer ? ((Integer) value.getValue()).intValue() : 0;
                int lastStatus = XpSeatMassagerService.mStatusMap.containsKey(Integer.valueOf(signalId)) ? ((Integer) XpSeatMassagerService.mStatusMap.get(Integer.valueOf(signalId))).intValue() : 0;
                int intensity = 3;
                LogUtil.i(XpSeatMassagerService.TAG, "onMsmEvent: " + value.toString() + ", lastVal=" + lastStatus);
                if (XpSeatMassagerService.mLockMap.containsKey(Integer.valueOf(signalId))) {
                    synchronized (XpSeatMassagerService.mLockMap.get(Integer.valueOf(signalId))) {
                        XpSeatMassagerService.mLockMap.get(Integer.valueOf(signalId)).notify();
                    }
                }
                for (int id = 0; id < 4; id++) {
                    if (XpSeatMassagerService.this.hasSupportMassager(id)) {
                        if (XpSeatMassagerService.this.getSignalMode(id) != signalId || lastStatus == signalVal) {
                            if (XpSeatMassagerService.this.getSignalIntensity(id) == signalId && signalVal != 0 && lastStatus != signalVal) {
                                LogUtil.i(XpSeatMassagerService.TAG, "onChangeEvent, changeMassagerIntensity [id=" + id + ", intensity=" + signalVal + "]");
                                XpSeatMassagerService.this.sendSetMassagerIntensity(Collections.singletonList(Integer.valueOf(id)), signalVal);
                                return;
                            }
                        } else if (signalVal != 0) {
                            if (XpSeatMassagerService.mModeMap.containsKey(Integer.valueOf(signalVal))) {
                                String effect = (String) XpSeatMassagerService.mModeMap.get(Integer.valueOf(signalVal));
                                if (lastStatus == 0) {
                                    try {
                                        intensity = XpSeatMassagerService.this.getDriverSeatMassagerIntensity(id);
                                    } catch (Exception e) {
                                    }
                                    List<Seat> seats = Collections.singletonList(new Seat(id, intensity));
                                    LogUtil.i(XpSeatMassagerService.TAG, "onChangeEvent, startMassager [id=" + id + ", effect=" + effect + ", intensity=" + intensity + "]");
                                    XpSeatMassagerService xpSeatMassagerService = XpSeatMassagerService.this;
                                    xpSeatMassagerService.sendStartMassager(new Massager(null, seats, effect, "physicalKey"));
                                    return;
                                }
                                LogUtil.i(XpSeatMassagerService.TAG, "onChangeEvent, changeMassagerEffect [id=" + id + ", effect=" + effect + "]");
                                XpSeatMassagerService xpSeatMassagerService2 = XpSeatMassagerService.this;
                                xpSeatMassagerService2.sendSetMassagerEffect(new Massager(Collections.singletonList(Integer.valueOf(id)), null, effect, null));
                                return;
                            }
                            LogUtil.w(XpSeatMassagerService.TAG, "onChangeEvent, massager mode=" + signalVal + " unsupported!");
                            return;
                        } else {
                            LogUtil.i(XpSeatMassagerService.TAG, "onChangeEvent, stopMassager [id=" + id + "]");
                            XpSeatMassagerService.this.sendStopMassager(Collections.singletonList(Integer.valueOf(id)));
                            return;
                        }
                    }
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        mCar.addMsmManagerListener(mMsmCallback);
    }

    private XpSeatMassagerService(Context context) {
        this.mHandlerThread = null;
        this.mHandler = null;
        registerSignalMapSeatIds();
        loadVibrateIntensity(CFG_INTENSITY);
        addMsmManagerCallback();
        mCar.addConnectionListener(new IServiceConn() { // from class: com.xiaopeng.xuiservice.capabilities.smm.XpSeatMassagerService.3
            @Override // com.xiaopeng.xuiservice.carcontrol.IServiceConn
            public void onConnectedCar() {
                XpSeatMassagerService.this.initMassagerStatus();
                boolean unused = XpSeatMassagerService.hasInit = true;
            }

            @Override // com.xiaopeng.xuiservice.carcontrol.IServiceConn
            public void onDisconnectCar() {
                boolean unused = XpSeatMassagerService.hasInit = false;
            }
        });
        mAudioManager = (AudioManager) context.getSystemService("audio");
        initRhythmEnableAndIntensity();
        initSoundPool();
        this.mHandlerThread = new HandlerThread("XpSeatMassagerThread");
        this.mHandlerThread.start();
        this.mHandler = new Handler(this.mHandlerThread.getLooper()) { // from class: com.xiaopeng.xuiservice.capabilities.smm.XpSeatMassagerService.4
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        Massager massager = (Massager) msg.obj;
                        XpSeatMassagerService.this.handleStartMassager(massager.seats, massager.effect, massager.owner);
                        return;
                    case 1:
                        XpSeatMassagerService.this.handleStopMassager((List) msg.obj);
                        return;
                    case 2:
                        Massager massager2 = (Massager) msg.obj;
                        XpSeatMassagerService.this.handleSetMassagerEffect(massager2.ids, massager2.effect);
                        return;
                    case 3:
                        XpSeatMassagerService.this.handleSetMassagerIntensity((List) msg.obj, msg.arg1);
                        return;
                    case 4:
                        Vibrate vibrate = (Vibrate) msg.obj;
                        XpSeatMassagerService.this.handleDoVibrate(vibrate.seats, vibrate.effect, vibrate.loop, vibrate.position, vibrate.owner);
                        return;
                    case 5:
                        XpSeatMassagerService.this.handleDoneVibrate();
                        return;
                    case 6:
                        XpSeatMassagerService.this.handleSetVibrateIntensity((List) msg.obj, msg.arg1);
                        return;
                    case 7:
                        Vibrate vibrate2 = (Vibrate) msg.obj;
                        XpSeatMassagerService.this.handleSetRhythmEnable(vibrate2.ids, vibrate2.enable, vibrate2.owner);
                        return;
                    case 8:
                        XpSeatMassagerService.this.handleSetRhythmIntensity((List) msg.obj, msg.arg1);
                        return;
                    case 9:
                        XpSeatMassagerService.this.handleSetRhythmPattern(msg.arg1);
                        return;
                    default:
                        LogUtil.w(XpSeatMassagerService.TAG, "invalid handleMessage " + msg.what);
                        return;
                }
            }
        };
    }

    public static XpSeatMassagerService getInstance(Context context) {
        if (sService == null) {
            synchronized (XpSeatMassagerService.class) {
                if (sService == null) {
                    sService = new XpSeatMassagerService(context);
                }
            }
        }
        return sService;
    }
}
