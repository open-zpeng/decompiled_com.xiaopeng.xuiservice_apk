package com.xiaopeng.xuiservice.capabilities.makeup;

import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.bcm.CarBcmManager;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.alipay.mobile.aromeservice.RequestParams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lzy.okgo.model.HttpHeaders;
import com.xiaopeng.biutil.BiLog;
import com.xiaopeng.biutil.BiLogFactory;
import com.xiaopeng.xuimanager.makeuplight.MakeupLightManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.carcontrol.IServiceConn;
import com.xiaopeng.xuiservice.innerutils.BiLogTransmit;
import com.xiaopeng.xuiservice.innerutils.DataLogUtils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.opencv.videoio.Videoio;
/* loaded from: classes5.dex */
public class XpMakeupLightService {
    private static final int EAGAIN = 3;
    private static final int EINVAL = 2;
    private static final int EPERM = 1;
    private static final int ERROR = 2;
    private static final String MAKEUP_LUX = "MakeupLux";
    private static final String MAKEUP_RGB = "MakeupRgb";
    private static final String MAKEUP_WHITE = "MakeupWhite";
    private static final int MSG_BI_COLORTEMP = 5;
    private static final int MSG_BI_LUMINANCE = 6;
    private static final int MSG_COLORTEMP = 0;
    private static final int MSG_LUMINANCE = 1;
    private static final int MSG_MAKEUPSTATUS = 4;
    private static final int MSG_RUNEFFECT = 2;
    private static final int MSG_STOPEFFECT = 3;
    private static final String PATH = "/system/etc/xuiservice/makeup/alias.conf";
    private static final int START = 0;
    private static final int STOP = 1;
    private static final String TAG = "XpMakeupLightService";
    private static final int TICK_PERIOD = 100;
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    private static XpMakeupLightService sService = null;
    private static TaskInfo taskInfo = null;
    private static boolean mStatus = false;
    private static int mFadeTime = SystemProperties.getInt("persist.sys.xpeng.makeup.fadetime", (int) Videoio.CAP_PVAPI);
    private static List<MakeupLightListener> mListener = new ArrayList();
    private static ArrayMap<String, MakeupLightManager.ColorTemperature> mAliasMap = new ArrayMap<>();
    private static ArrayMap<String, Effect> mEffectMap = new ArrayMap<>();
    private static ScheduledThreadPoolExecutor mExecutor = new ScheduledThreadPoolExecutor(1);
    private static CarClientManager mCar = CarClientManager.getInstance();
    private static CarBcmManager.CarBcmEventCallback mBcmCallback = null;
    private static BiMakeup mBiMakeup = null;

    /* loaded from: classes5.dex */
    public interface MakeupLightListener {
        void onAvailable(boolean z);

        void onColorTemperature(MakeupLightManager.ColorTemperature colorTemperature);

        void onEffect(String str, int i);

        void onLuminance(int i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class Alias {
        public MakeupLightManager.ColorTemperature colorTemp;
        public String name;

        private Alias() {
        }
    }

    /* loaded from: classes5.dex */
    private class Packet {
        public Integer[] data;
        public int loop;
        public int period;

        private Packet() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class Effect {
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
                        LogUtil.e(XpMakeupLightService.TAG, "close " + file + " Exception: " + e);
                    }
                    return effect;
                } catch (Throwable th) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e2) {
                            LogUtil.e(XpMakeupLightService.TAG, "close " + file + " Exception: " + e2);
                        }
                    }
                    throw th;
                }
            } catch (Exception e3) {
                LogUtil.e(XpMakeupLightService.TAG, "readFromJson Exception: " + e3);
                if (reader != null) {
                    try {
                        reader.close();
                        return null;
                    } catch (Exception e4) {
                        LogUtil.e(XpMakeupLightService.TAG, "close " + file + " Exception: " + e4);
                        return null;
                    }
                }
                return null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class EffectTask implements Runnable {
        private Effect effect;
        private int tick = 0;

        public EffectTask(Effect effect) {
            this.effect = effect;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.tick++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class TaskInfo {
        public String effect;
        public ScheduledFuture<?> future;

        public TaskInfo(String effect, ScheduledFuture<?> future) {
            this.effect = effect;
            this.future = future;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class BiMakeup {
        private static final int TIMEOUT = 10;
        private static final int TOP_LIMIT = 20;
        private Set<Integer> colorTemps = new ArraySet();
        private Set<Integer> luminances = new ArraySet();
        private Set<String> effects = new ArraySet();
        private BiLog bilog = BiLogFactory.create(DataLogUtils.XUI_PID, DataLogUtils.MAKEUP_BID);

        public void sendColorTemperature(int temperature) {
            Message message = XpMakeupLightService.this.mHandler.obtainMessage();
            message.what = 5;
            message.arg1 = temperature;
            XpMakeupLightService.this.mHandler.removeMessages(5);
            XpMakeupLightService.this.mHandler.sendMessageDelayed(message, 10000L);
        }

        public void storeColorTemperature(int temperature) {
            if (this.colorTemps.size() < 20) {
                this.colorTemps.add(Integer.valueOf(temperature));
            }
        }

        public void sendLuminance(int lux) {
            Message message = XpMakeupLightService.this.mHandler.obtainMessage();
            message.what = 6;
            message.arg1 = lux;
            XpMakeupLightService.this.mHandler.removeMessages(6);
            XpMakeupLightService.this.mHandler.sendMessageDelayed(message, 10000L);
        }

        public void storeLuminance(int lux) {
            if (this.luminances.size() < 20) {
                this.luminances.add(Integer.valueOf(lux));
            }
        }

        public void addEffects(Effect effect) {
            if (this.effects.size() < 20) {
                this.effects.add(effect.name);
            }
        }

        public void submit() {
            XpMakeupLightService.this.mHandler.removeMessages(5);
            XpMakeupLightService.this.mHandler.removeMessages(6);
            this.bilog.push("endTime", String.valueOf(System.currentTimeMillis()));
            this.bilog.push("setColorTemps", this.colorTemps.toString());
            this.bilog.push("setLuminances", this.luminances.toString());
            this.bilog.push("setEffects", this.effects.toString());
            LogUtil.d(XpMakeupLightService.TAG, "BiMakeup: " + this.bilog.getString());
            BiLogTransmit.getInstance().submit(this.bilog);
        }

        public BiMakeup(int colorTemp, int luminance) {
            this.bilog.push("colorTemp", String.valueOf(colorTemp));
            this.bilog.push("luminance", String.valueOf(luminance));
            this.bilog.push(RequestParams.REQUEST_KEY_START_TIME, String.valueOf(System.currentTimeMillis()));
        }
    }

    private void setProviderColorTemperature(MakeupLightManager.ColorTemperature colorTemp) {
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), MAKEUP_RGB, colorTemp.rgb);
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), MAKEUP_WHITE, colorTemp.white);
    }

    private MakeupLightManager.ColorTemperature getProviderColorTemperature() {
        int rgb = Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), MAKEUP_RGB, 0);
        int white = Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), MAKEUP_WHITE, 42);
        return new MakeupLightManager.ColorTemperature(rgb, white);
    }

    private void setProviderLuminance(int lux) {
        Settings.System.putInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), MAKEUP_LUX, lux);
    }

    private int getProviderLuminance() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), MAKEUP_LUX, 40);
    }

    private void showAvailableEvent(boolean status) {
        if (mListener.isEmpty()) {
            return;
        }
        synchronized (mListener) {
            for (MakeupLightListener listener : mListener) {
                listener.onAvailable(status);
            }
        }
    }

    private void showColorTemperatureEvent(MakeupLightManager.ColorTemperature colorTemp) {
        if (mListener.isEmpty()) {
            return;
        }
        synchronized (mListener) {
            for (MakeupLightListener listener : mListener) {
                listener.onColorTemperature(colorTemp);
            }
        }
    }

    private void showLuminanceEvent(int lux) {
        if (mListener.isEmpty()) {
            return;
        }
        synchronized (mListener) {
            for (MakeupLightListener listener : mListener) {
                listener.onLuminance(lux);
            }
        }
    }

    private void showEffectEvent(String effect, int type) {
        if (mListener.isEmpty()) {
            return;
        }
        synchronized (mListener) {
            for (MakeupLightListener listener : mListener) {
                listener.onEffect(effect, type);
            }
        }
    }

    private boolean checkColorTemperature(int rgb, int white) {
        return rgb >= 0 && rgb <= 255 && white >= 0 && white <= 63;
    }

    private boolean checkLuminance(int lux) {
        return lux >= 0 && lux <= 63;
    }

    private void setDriverCtrl(boolean enable) throws Exception {
        CarBcmManager bcm = mCar.getCarManager("xp_bcm");
        bcm.setGroupLedControlStatus(enable ? 1 : 0, enable ? 1 : 0, enable ? 1 : 0, enable ? 1 : 0);
    }

    private void setDriverColorTemperature(MakeupLightManager.ColorTemperature colorTemp, int fadeTime) throws Exception {
        CarBcmManager bcm = mCar.getCarManager("xp_bcm");
        int temperature = colorTemp.white;
        int tick = (fadeTime / 50) & 63;
        bcm.setGroupLedFadeTime(tick, tick, tick, tick);
        bcm.setGroupLedTemperature(temperature, temperature, temperature, temperature);
    }

    private void setDriverLuminance(int lux) throws Exception {
        CarBcmManager bcm = mCar.getCarManager("xp_bcm");
        bcm.setGroupLedBrigntness(lux, lux, lux, lux);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleMakeupStatusMessage(boolean status) {
        StringBuilder sb = new StringBuilder();
        sb.append("handleMakeupStatusMessage, status=");
        sb.append(status ? "on" : "off");
        LogUtil.d(TAG, sb.toString());
        try {
            MakeupLightManager.ColorTemperature colorTemp = getProviderColorTemperature();
            int lux = getProviderLuminance();
            boolean hasTaskRunning = (taskInfo == null || taskInfo.future.isDone()) ? false : true;
            if (status && !hasTaskRunning) {
                setDriverColorTemperature(colorTemp, mFadeTime);
                setDriverLuminance(lux);
            } else if (!status && hasTaskRunning) {
                taskInfo.future.cancel(true);
            }
            setDriverCtrl(status);
        } catch (Exception e) {
            LogUtil.e(TAG, "handleMakeupStatusMessage failed, " + e);
        }
        showAvailableEvent(status);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleColorTempMessage(MakeupLightManager.ColorTemperature colorTemp, int fadeTime) {
        LogUtil.d(TAG, "handleColorTempMessage, colorTemp=(" + colorTemp.rgb + ", " + colorTemp.white + "), fadeTime=" + fadeTime);
        try {
            if (taskInfo != null && !taskInfo.future.isDone()) {
                taskInfo.future.cancel(true);
                showEffectEvent(taskInfo.effect, 1);
            }
            setDriverColorTemperature(colorTemp, fadeTime);
            setProviderColorTemperature(colorTemp);
            showColorTemperatureEvent(colorTemp);
        } catch (Exception e) {
            LogUtil.e(TAG, "handleColorTempMessage failed, " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLuminanceMessage(int lux) {
        LogUtil.d(TAG, "handleLuminanceMessage, lux=" + lux);
        try {
            setDriverLuminance(lux);
            showLuminanceEvent(lux);
            setProviderLuminance(lux);
        } catch (Exception e) {
            LogUtil.e(TAG, "handleLuminanceMessage failed, " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRunEffectMessage(Effect effect, int count) {
        LogUtil.d(TAG, "handleRunEffectMessage, effect=" + effect + ", count=" + count);
        try {
            if (taskInfo != null && !taskInfo.future.isDone()) {
                taskInfo.future.cancel(true);
                showEffectEvent(taskInfo.effect, 1);
            }
            ScheduledFuture<?> future = mExecutor.scheduleAtFixedRate(new EffectTask(effect), 100L, 100L, TimeUnit.MILLISECONDS);
            taskInfo = new TaskInfo(effect.name, future);
            showEffectEvent(effect.name, 0);
        } catch (Exception e) {
            LogUtil.e(TAG, "handleRunEffectMessage failed, " + e);
            showEffectEvent(effect.name, 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStopEffectMessage() {
        LogUtil.d(TAG, "handleStopEffectMessage");
        TaskInfo taskInfo2 = taskInfo;
        if (taskInfo2 != null && !taskInfo2.future.isDone()) {
            MakeupLightManager.ColorTemperature colorTemp = getProviderColorTemperature();
            LogUtil.i(TAG, "handleStopEffectMessage");
            try {
                taskInfo.future.cancel(true);
                setDriverColorTemperature(colorTemp, mFadeTime);
                showEffectEvent(taskInfo.effect, 1);
            } catch (Exception e) {
                LogUtil.e(TAG, "handleStopEffectMessage failed, " + e);
                showEffectEvent(taskInfo.effect, 2);
            }
        }
    }

    private int sendColorTempMessage(MakeupLightManager.ColorTemperature colorTemp, int fadeTime) {
        Message message = this.mHandler.obtainMessage();
        message.what = 0;
        message.obj = colorTemp;
        message.arg1 = fadeTime;
        this.mHandler.removeMessages(0);
        LogUtil.d(TAG, "sendColorTempMessage");
        return this.mHandler.sendMessage(message) ? 0 : -3;
    }

    private int sendLuminanceMessage(int lux) {
        Message message = this.mHandler.obtainMessage();
        message.what = 1;
        message.arg1 = lux;
        this.mHandler.removeMessages(1);
        LogUtil.d(TAG, "sendLuminanceMessage");
        return this.mHandler.sendMessage(message) ? 0 : -3;
    }

    private int sendRunEffectMessage(Effect effect, int count) {
        Message message = this.mHandler.obtainMessage();
        message.what = 2;
        message.obj = effect;
        message.arg1 = count;
        this.mHandler.removeMessages(2);
        LogUtil.d(TAG, "sendRunEffectMessage");
        return this.mHandler.sendMessage(message) ? 0 : -3;
    }

    private int sendStopEffectMassage() {
        Message message = this.mHandler.obtainMessage();
        message.what = 3;
        this.mHandler.removeMessages(3);
        LogUtil.d(TAG, "sendStopEffectMassage");
        return this.mHandler.sendMessage(message) ? 0 : -3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int sendMakeupStatusMessage(boolean status) {
        Message message = this.mHandler.obtainMessage();
        message.what = 4;
        message.obj = Boolean.valueOf(status);
        StringBuilder sb = new StringBuilder();
        sb.append("sendMakeupStatusMessage ");
        sb.append(status ? "on" : "off");
        LogUtil.d(TAG, sb.toString());
        return this.mHandler.sendMessage(message) ? 0 : -3;
    }

    public void setMakeupLightListener(MakeupLightListener listener) {
        StringBuilder sb = new StringBuilder();
        sb.append("setMakeupLightListener ");
        sb.append(listener == null ? "invalid" : "ok");
        LogUtil.i(TAG, sb.toString());
        if (listener != null) {
            synchronized (mListener) {
                mListener.add(listener);
            }
        }
    }

    public void unsetMakeupLightListener(MakeupLightListener listener) {
        StringBuilder sb = new StringBuilder();
        sb.append("unsetMakeupLightListener ");
        sb.append(listener == null ? "invalid" : "ok");
        LogUtil.i(TAG, sb.toString());
        if (listener != null) {
            synchronized (mListener) {
                mListener.remove(listener);
            }
        }
    }

    public void loadColorTemperature() {
        StringBuilder sb;
        BufferedReader reader = null;
        try {
            try {
                reader = new BufferedReader(new FileReader(PATH));
                Gson gson = new GsonBuilder().create();
                Alias[] array = (Alias[]) gson.fromJson((Reader) reader, (Class<Object>) Alias[].class);
                for (Alias alias : array) {
                    mAliasMap.put(alias.name, alias.colorTemp);
                }
                LogUtil.i(TAG, "loadColorTemperature /system/etc/xuiservice/makeup/alias.conf succeed");
                try {
                    reader.close();
                } catch (Exception e) {
                    e = e;
                    sb = new StringBuilder();
                    sb.append("close /system/etc/xuiservice/makeup/alias.conf Exception: ");
                    sb.append(e);
                    LogUtil.e(TAG, sb.toString());
                }
            } catch (Exception e2) {
                LogUtil.e(TAG, "loadColorTemperature Exception: " + e2);
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e3) {
                        e = e3;
                        sb = new StringBuilder();
                        sb.append("close /system/etc/xuiservice/makeup/alias.conf Exception: ");
                        sb.append(e);
                        LogUtil.e(TAG, sb.toString());
                    }
                }
            }
        } catch (Throwable th) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e4) {
                    LogUtil.e(TAG, "close /system/etc/xuiservice/makeup/alias.conf Exception: " + e4);
                }
            }
            throw th;
        }
    }

    public synchronized void loadMakeupEffect(String path) {
        Effect effect = new Effect().readFromJson(path);
        if (effect != null && !mEffectMap.containsKey(effect.name)) {
            LogUtil.i(TAG, "loadMakeupEffect " + path);
            mEffectMap.put(effect.name, effect);
        }
    }

    public synchronized void clearMakeupEffect() {
        mEffectMap.clear();
    }

    public synchronized List<String> showMakeupEffect() {
        return mEffectMap.isEmpty() ? new ArrayList() : new ArrayList(mEffectMap.keySet());
    }

    public synchronized List<String> showAliasColorTemperature() {
        return mAliasMap.isEmpty() ? new ArrayList() : new ArrayList(mAliasMap.keySet());
    }

    public synchronized int getMakeupAvailable() {
        return mStatus ? 1 : 0;
    }

    public synchronized int setMakeupLightStatus(boolean status) {
        return mStatus ? sendMakeupStatusMessage(status) : -1;
    }

    public synchronized int setColorTemperature(MakeupLightManager.ColorTemperature colorTemp) {
        if (mStatus) {
            return checkColorTemperature(colorTemp.rgb, colorTemp.white) ? sendColorTempMessage(colorTemp, 100) : -2;
        }
        return -1;
    }

    public synchronized int setAliasColorTemperature(String alias) {
        if (mStatus) {
            return mAliasMap.containsKey(alias) ? setColorTemperature(mAliasMap.get(alias)) : -2;
        }
        return -1;
    }

    public synchronized MakeupLightManager.ColorTemperature getColorTemperature() {
        return getProviderColorTemperature();
    }

    public synchronized int setLuminance(int lux) {
        if (mStatus) {
            return checkLuminance(lux) ? sendLuminanceMessage(lux) : -2;
        }
        return -1;
    }

    public synchronized int getLuminance() {
        return getProviderLuminance();
    }

    public synchronized int runEffect(String effect, int count) {
        if (mStatus) {
            return mEffectMap.containsKey(effect) ? sendRunEffectMessage(mEffectMap.get(effect), count) : -2;
        }
        return -1;
    }

    public synchronized int stopEffect() {
        if (mStatus) {
            return sendStopEffectMassage();
        }
        return -1;
    }

    private void addBcmManagerCallback() {
        LogUtil.i(TAG, "addBcmManagerCallback");
        mBcmCallback = new CarBcmManager.CarBcmEventCallback() { // from class: com.xiaopeng.xuiservice.capabilities.makeup.XpMakeupLightService.1
            public void onChangeEvent(CarPropertyValue value) {
                LogUtil.d(XpMakeupLightService.TAG, "onChangeEvent " + value.toString());
                if (value.getPropertyId() == 557849940) {
                    boolean unused = XpMakeupLightService.mStatus = ((Integer) value.getValue()).intValue() == 1;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onChangeEvent, makeupLight ");
                    sb.append(XpMakeupLightService.mStatus ? "open" : HttpHeaders.HEAD_VALUE_CONNECTION_CLOSE);
                    LogUtil.i(XpMakeupLightService.TAG, sb.toString());
                    XpMakeupLightService.this.sendMakeupStatusMessage(XpMakeupLightService.mStatus);
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        mCar.addBcmManagerListener(mBcmCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBiMakeup(Message msg) {
        if (msg.what == 4) {
            if (((Boolean) msg.obj).booleanValue()) {
                MakeupLightManager.ColorTemperature colorTemp = getProviderColorTemperature();
                mBiMakeup = new BiMakeup(colorTemp.white, getProviderLuminance());
                return;
            }
            BiMakeup biMakeup = mBiMakeup;
            if (biMakeup != null) {
                biMakeup.submit();
                mBiMakeup = null;
            }
        } else if (mBiMakeup != null) {
            int i = msg.what;
            if (i == 0) {
                MakeupLightManager.ColorTemperature colorTemp2 = (MakeupLightManager.ColorTemperature) msg.obj;
                mBiMakeup.sendColorTemperature(colorTemp2.white);
            } else if (i == 1) {
                mBiMakeup.sendLuminance(msg.arg1);
            } else if (i == 2) {
                mBiMakeup.addEffects((Effect) msg.obj);
            } else if (i == 5) {
                mBiMakeup.storeColorTemperature(msg.arg1);
            } else if (i == 6) {
                mBiMakeup.storeLuminance(msg.arg1);
            }
        }
    }

    private XpMakeupLightService(Context context) {
        this.mHandlerThread = null;
        this.mHandler = null;
        loadColorTemperature();
        addBcmManagerCallback();
        CarClientManager.getInstance().addConnectionListener(new IServiceConn() { // from class: com.xiaopeng.xuiservice.capabilities.makeup.XpMakeupLightService.2
            @Override // com.xiaopeng.xuiservice.carcontrol.IServiceConn
            public void onConnectedCar() {
                try {
                    CarBcmManager bcm = XpMakeupLightService.mCar.getCarManager("xp_bcm");
                    boolean z = true;
                    if (bcm.getCoverPlateStatus() != 1) {
                        z = false;
                    }
                    boolean unused = XpMakeupLightService.mStatus = z;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onConnectedCar, makeupLight ");
                    sb.append(XpMakeupLightService.mStatus ? "open" : HttpHeaders.HEAD_VALUE_CONNECTION_CLOSE);
                    LogUtil.i(XpMakeupLightService.TAG, sb.toString());
                    XpMakeupLightService.this.sendMakeupStatusMessage(XpMakeupLightService.mStatus);
                } catch (Exception e) {
                }
            }

            @Override // com.xiaopeng.xuiservice.carcontrol.IServiceConn
            public void onDisconnectCar() {
                boolean unused = XpMakeupLightService.mStatus = false;
            }
        });
        this.mHandlerThread = new HandlerThread("XpMakeupLightThread");
        this.mHandlerThread.start();
        this.mHandler = new Handler(this.mHandlerThread.getLooper()) { // from class: com.xiaopeng.xuiservice.capabilities.makeup.XpMakeupLightService.3
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                int i = msg.what;
                if (i == 0) {
                    XpMakeupLightService.this.handleColorTempMessage((MakeupLightManager.ColorTemperature) msg.obj, msg.arg1);
                } else if (i == 1) {
                    XpMakeupLightService.this.handleLuminanceMessage(msg.arg1);
                } else if (i == 2) {
                    XpMakeupLightService.this.handleRunEffectMessage((Effect) msg.obj, msg.arg1);
                } else if (i == 3) {
                    XpMakeupLightService.this.handleStopEffectMessage();
                } else if (i == 4) {
                    XpMakeupLightService.this.handleMakeupStatusMessage(((Boolean) msg.obj).booleanValue());
                }
                XpMakeupLightService.this.handleBiMakeup(msg);
            }
        };
    }

    public static XpMakeupLightService getInstance(Context context) {
        if (sService == null) {
            synchronized (XpMakeupLightService.class) {
                if (sService == null) {
                    sService = new XpMakeupLightService(context);
                }
            }
        }
        return sService;
    }
}
