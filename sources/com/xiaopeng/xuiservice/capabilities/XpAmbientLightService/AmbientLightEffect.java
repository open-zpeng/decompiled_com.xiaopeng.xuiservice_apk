package com.xiaopeng.xuiservice.capabilities.XpAmbientLightService;

import android.content.Context;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.utils.CommonUtils;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/* loaded from: classes5.dex */
public class AmbientLightEffect {
    private static final boolean DBG = false;
    public static final int DEFAULT_DOUBLE_1ST_COLOR = 1;
    public static final int DEFAULT_DOUBLE_2ND_COLOR = 6;
    public static final int DEFAULT_MONO_COLOR = 14;
    private static final int DEFAULT_MUSIC_LIGHT_GROUP = 3;
    public static final int LIGHT_COLOR_1 = 1;
    public static final int LIGHT_COLOR_10 = 10;
    public static final int LIGHT_COLOR_11 = 11;
    public static final int LIGHT_COLOR_12 = 12;
    public static final int LIGHT_COLOR_13 = 13;
    public static final int LIGHT_COLOR_14 = 14;
    public static final int LIGHT_COLOR_15 = 15;
    public static final int LIGHT_COLOR_16 = 16;
    public static final int LIGHT_COLOR_17 = 17;
    public static final int LIGHT_COLOR_18 = 18;
    public static final int LIGHT_COLOR_19 = 19;
    public static final int LIGHT_COLOR_2 = 2;
    public static final int LIGHT_COLOR_20 = 20;
    public static final int LIGHT_COLOR_3 = 3;
    public static final int LIGHT_COLOR_4 = 4;
    public static final int LIGHT_COLOR_5 = 5;
    public static final int LIGHT_COLOR_6 = 6;
    public static final int LIGHT_COLOR_7 = 7;
    public static final int LIGHT_COLOR_8 = 8;
    public static final int LIGHT_COLOR_9 = 9;
    private static final String TAG = "AmbientLightEffect";
    private final GroupNum defaultOrMusicGroupNum;
    private ScheduledThreadPoolExecutor defaultTimer;
    private final ArrayList<AmbilentPara> mAmbilentParaList;
    private final BrightPara mBrightPara;
    private CarAtlManagerAgent mCarAtlManagerAgent;
    private LightEffectManager mLightEffectManager;
    private final SpeechPara mSpeechPara;
    private int[] speechGroupNum;
    private ScheduledThreadPoolExecutor speechTimer;
    private long lastAtlLogTime = 0;
    private boolean speechEnable = true;

    public AmbientLightEffect(Context context) {
        getCarAtlManagerAgent();
        getLightEffectManager();
        this.defaultOrMusicGroupNum = new GroupNum();
        this.mSpeechPara = new SpeechPara();
        this.mBrightPara = new BrightPara();
        this.defaultOrMusicGroupNum.setGroupNum(3);
        this.mAmbilentParaList = new ArrayList<>();
        this.defaultTimer = new ScheduledThreadPoolExecutor(1);
        this.speechTimer = new ScheduledThreadPoolExecutor(1);
    }

    public void setAmbientLightEffect(String effectType, boolean doubleThemeColorEnable, int firstThemeColor, int secondThemeColor, int bright_low) {
        int i = firstThemeColor;
        ArrayList<LightEffect> mAmbientLightEffectList = getLightEffectManager().getAmbientLightEffectList();
        if (mAmbientLightEffectList.size() > 0) {
            int i2 = 0;
            while (i2 < mAmbientLightEffectList.size()) {
                long delay_time = 0;
                if (mAmbientLightEffectList.get(i2).getEffect_type().equals(effectType)) {
                    stopAllAmbilentEffect();
                    this.mAmbilentParaList.clear();
                    if (mAmbientLightEffectList.get(i2).getEffect_init().size() > 0) {
                        int num = mAmbientLightEffectList.get(i2).getEffect_init().size();
                        int j = 0;
                        while (j < num) {
                            int fade = mAmbientLightEffectList.get(i2).getEffect_init().get(j).getFade();
                            setGroupLightData(this.defaultOrMusicGroupNum.getGroupNum(), (byte) i, (byte) Math.round((this.mBrightPara.getBright() * ((byte) mAmbientLightEffectList.get(i2).getEffect_init().get(j).getBright())) / 100), (byte) Math.round(fade / 20));
                            j++;
                            delay_time += fade;
                        }
                    }
                    if (mAmbientLightEffectList.get(i2).getEffect_period().size() > 0) {
                        int num2 = mAmbientLightEffectList.get(i2).getEffect_period().size();
                        byte[] color = new byte[num2];
                        byte[] bright = new byte[num2];
                        int[] fade2 = new int[num2];
                        int color_state = firstThemeColor;
                        int j2 = 0;
                        while (j2 < num2) {
                            if (doubleThemeColorEnable) {
                                if (mAmbientLightEffectList.get(i2).getEffect_period().get(j2).getColorswitch()) {
                                    if (color_state == i) {
                                        color[j2] = (byte) secondThemeColor;
                                        color_state = secondThemeColor;
                                    } else if (color_state == secondThemeColor) {
                                        color[j2] = (byte) i;
                                        color_state = firstThemeColor;
                                    }
                                } else {
                                    color[j2] = (byte) color_state;
                                }
                            } else {
                                color[j2] = (byte) i;
                            }
                            bright[j2] = (byte) mAmbientLightEffectList.get(i2).getEffect_period().get(j2).getBright();
                            if (bright[j2] < ((byte) bright_low)) {
                                bright[j2] = (byte) bright_low;
                            }
                            fade2[j2] = mAmbientLightEffectList.get(i2).getEffect_period().get(j2).getFade();
                            AmbilentPara mAmbilentPara = new AmbilentPara();
                            mAmbilentPara.setColor(color[j2]);
                            mAmbilentPara.setBright(bright[j2]);
                            mAmbilentPara.setFade(fade2[j2]);
                            this.mAmbilentParaList.add(mAmbilentPara);
                            j2++;
                            i = firstThemeColor;
                        }
                        startAmbientLightEffect(delay_time);
                    }
                }
                i2++;
                i = firstThemeColor;
            }
        }
    }

    public void setDefaultOrMusicGroup(int groupNum) {
        this.defaultOrMusicGroupNum.setGroupNum(groupNum);
    }

    public void setAmbientLightBrightPara(int bright) {
        this.mBrightPara.setBright(bright);
    }

    public void updateLightEffectPara(String effectType, boolean doubleThemeColorEnable, int firstThemeColor, int secondThemeColor, int bright_low) {
        int i = firstThemeColor;
        ArrayList<LightEffect> mAmbientLightEffectList = getLightEffectManager().getAmbientLightEffectList();
        setGroupLightData(this.defaultOrMusicGroupNum.getGroupNum(), (byte) i, (byte) Math.round((this.mBrightPara.getBright() * 100) / 100), (byte) 20);
        if (mAmbientLightEffectList.size() > 0) {
            int i2 = 0;
            while (i2 < mAmbientLightEffectList.size()) {
                if (mAmbientLightEffectList.get(i2).getEffect_type().equals(effectType) && mAmbientLightEffectList.get(i2).getEffect_period().size() > 0) {
                    int num = mAmbientLightEffectList.get(i2).getEffect_period().size();
                    byte[] color = new byte[num];
                    byte[] bright = new byte[num];
                    int[] fade = new int[num];
                    int color_state = firstThemeColor;
                    int j = 0;
                    while (j < num) {
                        if (doubleThemeColorEnable) {
                            if (mAmbientLightEffectList.get(i2).getEffect_period().get(j).getColorswitch()) {
                                if (color_state == i) {
                                    color[j] = (byte) secondThemeColor;
                                    color_state = secondThemeColor;
                                } else if (color_state == secondThemeColor) {
                                    color[j] = (byte) i;
                                    color_state = firstThemeColor;
                                }
                            } else {
                                color[j] = (byte) color_state;
                            }
                        } else {
                            color[j] = (byte) i;
                        }
                        bright[j] = (byte) mAmbientLightEffectList.get(i2).getEffect_period().get(j).getBright();
                        if (bright[j] < ((byte) bright_low)) {
                            bright[j] = (byte) bright_low;
                        }
                        fade[j] = mAmbientLightEffectList.get(i2).getEffect_period().get(j).getFade();
                        this.mAmbilentParaList.get(j).setColor(color[j]);
                        this.mAmbilentParaList.get(j).setBright(bright[j]);
                        this.mAmbilentParaList.get(j).setFade(fade[j]);
                        j++;
                        i = firstThemeColor;
                    }
                }
                i2++;
                i = firstThemeColor;
            }
        }
    }

    private void startAmbientLightEffect(long delaytime) {
        long period_time = 0;
        for (int i = 0; i < this.mAmbilentParaList.size(); i++) {
            period_time += this.mAmbilentParaList.get(i).getFade();
        }
        long delay_time = delaytime;
        for (int i2 = 0; i2 < this.mAmbilentParaList.size(); i2++) {
            LogUtil.d(TAG, "started timerTask mAmbilentParaList.size():" + this.mAmbilentParaList.size());
            final int num = i2;
            Runnable timerTask = new Runnable() { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightEffect.1
                @Override // java.lang.Runnable
                public void run() {
                    AmbientLightEffect ambientLightEffect = AmbientLightEffect.this;
                    ambientLightEffect.setGroupLightData(ambientLightEffect.defaultOrMusicGroupNum.getGroupNum(), ((AmbilentPara) AmbientLightEffect.this.mAmbilentParaList.get(num)).getColor(), (byte) Math.round((((AmbilentPara) AmbientLightEffect.this.mAmbilentParaList.get(num)).getBright() * AmbientLightEffect.this.mBrightPara.getBright()) / 100), (byte) Math.round(((AmbilentPara) AmbientLightEffect.this.mAmbilentParaList.get(num)).getFade() / 20));
                }
            };
            this.defaultTimer.scheduleAtFixedRate(timerTask, delay_time, period_time, TimeUnit.MILLISECONDS);
            delay_time += this.mAmbilentParaList.get(i2).getFade();
        }
    }

    public void setSpeechEffect(int[] groupNum, String effectType) {
        ArrayList<LightEffect> mAmbientLightEffectList = getLightEffectManager().getAmbientLightEffectList();
        if (mAmbientLightEffectList.size() > 0) {
            for (int i = 0; i < mAmbientLightEffectList.size(); i++) {
                if (mAmbientLightEffectList.get(i).getEffect_type().equals(effectType) && mAmbientLightEffectList.get(i).getEffect_period().size() > 0) {
                    int num = mAmbientLightEffectList.get(i).getEffect_period().size();
                    byte[] color = new byte[num];
                    byte[] bright = new byte[num];
                    int[] fade = new int[num];
                    for (int j = 0; j < num; j++) {
                        color[j] = (byte) mAmbientLightEffectList.get(i).getEffect_period().get(j).getColor();
                        bright[j] = (byte) mAmbientLightEffectList.get(i).getEffect_period().get(j).getBright();
                        fade[j] = mAmbientLightEffectList.get(i).getEffect_period().get(j).getFade();
                    }
                    startSpeechEffect(groupNum, color, bright, fade);
                }
            }
        }
    }

    private void startSpeechEffect(final int[] groupNum, byte[] color, byte[] bright, int[] fade) {
        int i;
        LogUtil.i(TAG, "startSpeechEffect()");
        this.speechGroupNum = groupNum;
        long period_time = 0;
        for (int i2 : fade) {
            period_time += i2;
        }
        this.speechEnable = true;
        long delay_time = 0;
        for (int i3 = 0; i3 < color.length; i3++) {
            LogUtil.d(TAG, "started speechTimer length:" + color.length);
            final byte color_value = color[i3];
            final byte bright_value = bright[i3];
            final byte fade_value = (byte) Math.round((float) (fade[i3] / 20));
            Runnable timerTask = new Runnable() { // from class: com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightEffect.2
                @Override // java.lang.Runnable
                public void run() {
                    if (!AmbientLightEffect.this.mSpeechPara.isEnable() || !AmbientLightEffect.this.speechEnable) {
                        if (!AmbientLightEffect.this.speechEnable) {
                            AmbientLightEffect.this.printDebugLog("speechTask", 0, 0);
                            return;
                        }
                        return;
                    }
                    int j = 0;
                    while (true) {
                        int[] iArr = groupNum;
                        if (j < iArr.length) {
                            AmbientLightEffect.this.setGroupLightData(iArr[j], color_value, bright_value, fade_value);
                            j++;
                        } else {
                            return;
                        }
                    }
                }
            };
            this.speechTimer.scheduleAtFixedRate(timerTask, delay_time, period_time, TimeUnit.MILLISECONDS);
            delay_time += fade[i];
        }
    }

    public void setMusicSpectrumEffect(byte color, byte bright, byte fade) {
        setGroupLightData(this.defaultOrMusicGroupNum.getGroupNum(), color, bright, fade);
    }

    public void setAmbientLightDefaultGroupEffect(byte color, byte bright, byte fade) {
        setGroupLightData(this.defaultOrMusicGroupNum.getGroupNum(), color, bright, fade);
    }

    public void setSpeechEffectEnable(boolean enable) {
        this.mSpeechPara.setEnable(enable);
    }

    public void setSpeechSpectrumEffect(byte color, byte bright, byte fade) {
        if (this.speechGroupNum != null) {
            int i = 0;
            while (true) {
                int[] iArr = this.speechGroupNum;
                if (i < iArr.length) {
                    setGroupLightData(iArr[i], color, bright, fade);
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public void setFollowCarSpeedEffect(int color, float speed, int fade) {
        byte bright = 1;
        if (speed >= 1.0f) {
            bright = speed < 80.0f ? (byte) Math.round(((99.0f * speed) - 20.0f) / 79.0f) : (byte) 100;
        }
        setGroupLightData(this.defaultOrMusicGroupNum.getGroupNum(), (byte) color, bright, (byte) fade);
    }

    public void stopDefaultEffect() {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = this.defaultTimer;
        if (scheduledThreadPoolExecutor != null) {
            scheduledThreadPoolExecutor.shutdown();
        }
        this.defaultTimer = new ScheduledThreadPoolExecutor(1);
    }

    public void stopSpeechEffect() {
        LogUtil.i(TAG, "stopSpeechEffect()");
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = this.speechTimer;
        if (scheduledThreadPoolExecutor != null) {
            scheduledThreadPoolExecutor.shutdown();
        }
        this.speechEnable = false;
        this.speechTimer = new ScheduledThreadPoolExecutor(1);
    }

    public void stopAllAmbilentEffect() {
        stopDefaultEffect();
        stopSpeechEffect();
    }

    public void setTwoLightData(byte protocol, byte[] lightPosition, byte[] color, byte[] bright, byte[] fade) {
        LogUtil.i(TAG, "setTwoLightData()");
        getCarAtlManagerAgent().setTwoLightData(protocol, lightPosition, false, color, bright, fade);
    }

    public void setGroupLightData(int groupNum, byte color, byte bright, byte fade) {
        printAtlLog(groupNum, color, bright, fade);
        byte groupNumber = (byte) (groupNum & 255);
        getCarAtlManagerAgent().setGroutLightData(groupNumber, (byte) 0, 0, false, color, bright, fade);
    }

    public void setAllLightData(byte[] color, byte[] bright, byte[] fade) {
        getCarAtlManagerAgent().setAllLightData(false, color, bright, fade);
    }

    public void setIgOnStatus(boolean enable) {
        getCarAtlManagerAgent().setIgOnStatus(enable);
    }

    private CarAtlManagerAgent getCarAtlManagerAgent() {
        if (this.mCarAtlManagerAgent == null) {
            LogUtil.d(TAG, "getCarAtlManagerAgent()");
            this.mCarAtlManagerAgent = CarAtlManagerAgent.getInstance();
            this.mCarAtlManagerAgent.init();
        }
        return this.mCarAtlManagerAgent;
    }

    private LightEffectManager getLightEffectManager() {
        if (this.mLightEffectManager == null) {
            this.mLightEffectManager = LightEffectManager.getInstance();
        }
        return this.mLightEffectManager;
    }

    private void printAtlLog(int groupNum, byte color, byte bright, byte fade) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.lastAtlLogTime > CommonUtils.xuiLogInterval * 2) {
            LogUtil.i(TAG, "printAtlLog groupNum:" + groupNum + ", color:" + ((int) color) + ", bright:" + ((int) bright) + ", fade:" + ((int) fade));
            this.lastAtlLogTime = currentTime;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void printDebugLog(String func, int flag1, int flag2) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.lastAtlLogTime > CommonUtils.xuiLogInterval * 2) {
            LogUtil.i(TAG, "printDebugLog func:" + func + ", " + flag1 + ", " + flag2);
            this.lastAtlLogTime = currentTime;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class AmbilentPara {
        byte bright;
        byte color;
        int fade;

        private AmbilentPara() {
        }

        public byte getColor() {
            return this.color;
        }

        public void setColor(byte color) {
            this.color = color;
        }

        public byte getBright() {
            return this.bright;
        }

        public void setBright(byte bright) {
            this.bright = bright;
        }

        public int getFade() {
            return this.fade;
        }

        public void setFade(int fade) {
            this.fade = fade;
        }
    }

    /* loaded from: classes5.dex */
    private class GroupNum {
        int groupNum;

        private GroupNum() {
        }

        public int getGroupNum() {
            return this.groupNum;
        }

        public void setGroupNum(int groupNum) {
            this.groupNum = groupNum;
        }
    }

    /* loaded from: classes5.dex */
    private class SpeechPara {
        boolean enable;

        private SpeechPara() {
            this.enable = true;
        }

        public boolean isEnable() {
            return this.enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }
    }

    /* loaded from: classes5.dex */
    private class BrightPara {
        int bright;

        private BrightPara() {
            this.bright = 100;
        }

        public int getBright() {
            return this.bright;
        }

        public void setBright(int bright) {
            this.bright = bright;
        }
    }
}
