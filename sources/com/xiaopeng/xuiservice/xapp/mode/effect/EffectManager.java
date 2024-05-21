package com.xiaopeng.xuiservice.xapp.mode.effect;

import android.app.ActivityThread;
import android.content.ServiceConnection;
import com.xiaopeng.xuimanager.XUIManager;
import com.xiaopeng.xuimanager.karaoke.KaraokeManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.xapp.util.XAppBiLogUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/* loaded from: classes5.dex */
public class EffectManager implements KaraokeManager.MicCallBack {
    private static final String TAG = "EffectManager";
    private static EffectManager mEffectManager;
    private List<Callback> mCallbackList = new ArrayList();
    private List<EffectItem> mEffectItemList;
    private KaraokeManager mKaraokeManager;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public interface Callback {
        void onEffectSet(int i);
    }

    private EffectManager() {
        initKaraokeManager();
    }

    public static EffectManager getInstance() {
        if (mEffectManager == null) {
            synchronized (EffectManager.class) {
                if (mEffectManager == null) {
                    mEffectManager = new EffectManager();
                }
            }
        }
        return mEffectManager;
    }

    public void setMicEnable(boolean enable) {
        LogUtil.d(TAG, "setMicEnable enable:" + enable);
        if (this.mKaraokeManager != null) {
            String callingPackage = ActivityThread.currentActivityThread().getApplication().getPackageName();
            if (enable) {
                this.mKaraokeManager.XMA_requestKaraokeResource(callingPackage);
            } else {
                this.mKaraokeManager.XMA_releaseKaraokeResource(callingPackage);
            }
        }
    }

    public void volumeEffectCallBack(int type, int value) {
        LogUtil.i(TAG, "volumeEffectCallBack type:" + type + " &value:" + value);
        if (type == 4) {
            notifyEffectSet(value);
        }
    }

    public void onErrorEvent(int errorCode, int operation) {
    }

    public void micServiceCallBack(int msg) {
    }

    public void registerCallback(Callback callback) {
        if (!this.mCallbackList.contains(callback)) {
            this.mCallbackList.add(callback);
        }
    }

    public void unregisterCallback(Callback callback) {
        if (this.mCallbackList.contains(callback)) {
            this.mCallbackList.remove(callback);
        }
    }

    public List<EffectItem> getEffectList() {
        List<EffectItem> list = this.mEffectItemList;
        if (list != null) {
            return list;
        }
        KaraokeManager karaokeManager = this.mKaraokeManager;
        if (karaokeManager != null) {
            HashMap<String, Integer> result = karaokeManager.XMA_getMicSoundEffectMap();
            LogUtil.i(TAG, "getEffectList:" + EffectConfig.getEffectInfo(result));
            this.mEffectItemList = EffectConfig.getEffectList(result);
            return this.mEffectItemList;
        }
        return null;
    }

    public int getEffect() {
        KaraokeManager karaokeManager = this.mKaraokeManager;
        if (karaokeManager != null) {
            return karaokeManager.XMA_getVolume(4);
        }
        return 1;
    }

    public void setEffect(int index) {
        KaraokeManager karaokeManager = this.mKaraokeManager;
        if (karaokeManager != null) {
            karaokeManager.XMA_setVolume(4, index);
        }
    }

    public void setBIData(int index) {
        XAppBiLogUtil.sendEffectSetDataLog(index, getEffectName(index));
    }

    public void setEchoValue(int value) {
        LogUtil.d(TAG, "setEchoValue value:" + value);
        KaraokeManager karaokeManager = this.mKaraokeManager;
        if (karaokeManager != null) {
            karaokeManager.XMA_setEcho(value);
        }
    }

    public int getEchoValue() {
        KaraokeManager karaokeManager = this.mKaraokeManager;
        if (karaokeManager != null) {
            return karaokeManager.XMA_getEcho();
        }
        return 0;
    }

    public boolean isMusicEffectEnable() {
        KaraokeManager karaokeManager = this.mKaraokeManager;
        if (karaokeManager != null) {
            return karaokeManager.XMA_isAtlEnabled();
        }
        return false;
    }

    public boolean isMusicEffectSupport() {
        KaraokeManager karaokeManager = this.mKaraokeManager;
        if (karaokeManager != null) {
            return karaokeManager.XMA_hasAtl();
        }
        return false;
    }

    public void setMusicEffect(boolean enable) {
        LogUtil.d(TAG, "setMusicEffect enable:" + enable);
        KaraokeManager karaokeManager = this.mKaraokeManager;
        if (karaokeManager != null) {
            karaokeManager.XMA_setAtlEnable(enable);
        }
    }

    private String getEffectName(int index) {
        List<EffectItem> list = this.mEffectItemList;
        if (list != null) {
            for (EffectItem item : list) {
                if (item.getIndex() == index) {
                    return item.getName();
                }
            }
            return "";
        }
        return "";
    }

    private void notifyEffectSet(int index) {
        for (Callback callback : this.mCallbackList) {
            callback.onEffectSet(index);
        }
    }

    private void initKaraokeManager() {
        if (this.mKaraokeManager == null) {
            XUIManager xuiManager = XUIManager.createXUIManager(ActivityThread.currentActivityThread().getApplication(), (ServiceConnection) null);
            try {
                this.mKaraokeManager = (KaraokeManager) xuiManager.getXUIServiceManager(XUIConfig.PROPERTY_KARAOKE);
                if (this.mKaraokeManager != null) {
                    this.mKaraokeManager.XMA_init(ActivityThread.currentActivityThread().getApplication().getPackageName());
                    this.mKaraokeManager.XMA_registerCallback(this);
                }
                LogUtil.d(TAG, "init,mKaraokeManager=" + this.mKaraokeManager);
            } catch (Exception e) {
                LogUtil.w(TAG, "init e=" + e);
            }
        }
    }
}
