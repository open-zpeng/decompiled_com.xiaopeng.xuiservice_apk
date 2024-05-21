package com.xiaopeng.xuiservice.iot.manager.safetyseat;

import android.app.ActivityThread;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.SoundPool;
import com.xiaopeng.xuimanager.iot.BaseDevice;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.iot.manager.IEventListener;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.ToastUtil;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/* loaded from: classes5.dex */
public class ChildSeatAlert {
    private static final String TAG = ChildSeatAlert.class.getSimpleName();
    private static final String seatAppPackage = "package:com.xp.childseat";
    private AudioManager mAudioManager;
    private IEventListener mEventListener;
    private int mSoundId;
    private SoundPool mSoundPool;
    private int mStreamID;
    private long soundDuration;
    private String soundFilePath;
    private Runnable soundPlayEndTask;

    public /* synthetic */ void lambda$new$0$ChildSeatAlert() {
        LogUtil.i(TAG, "playAlertSound end");
        synchronized (this.mSoundPool) {
            if (this.mStreamID > 0) {
                this.mAudioManager.abandonAudioFocus(null);
                this.mSoundPool.stop(this.mStreamID);
                this.mStreamID = 0;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class ChildSeatAlertHolder {
        private static final ChildSeatAlert instance = new ChildSeatAlert();

        private ChildSeatAlertHolder() {
        }
    }

    public /* synthetic */ void lambda$new$1$ChildSeatAlert(String deviceId, String key, String value) {
        String str = TAG;
        LogUtil.i(str, "prop update,device=" + deviceId + ",key=" + key + ",value=" + value);
        if ("isofix_stat".equals(key)) {
            doIsoFixAlert(value);
        }
    }

    private ChildSeatAlert() {
        this.mSoundPool = null;
        this.mSoundId = -1;
        this.mStreamID = 0;
        this.soundDuration = 0L;
        this.soundFilePath = "/system/media/audio/xiaopeng/cdu/wav/CDU_action_dialog.wav";
        this.soundPlayEndTask = new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.safetyseat.-$$Lambda$ChildSeatAlert$ZbZ5Ed-CY8GRpYt59kMGst_bD60
            @Override // java.lang.Runnable
            public final void run() {
                ChildSeatAlert.this.lambda$new$0$ChildSeatAlert();
            }
        };
        this.mEventListener = new IEventListener() { // from class: com.xiaopeng.xuiservice.iot.manager.safetyseat.-$$Lambda$ChildSeatAlert$uyR845PmyCMpoTIXBZXVQK3W1ak
            @Override // com.xiaopeng.xuiservice.iot.manager.IEventListener
            public final void onDeviceEvent(String str, String str2, String str3) {
                ChildSeatAlert.this.lambda$new$1$ChildSeatAlert(str, str2, str3);
            }
        };
    }

    public static ChildSeatAlert getInstance() {
        return ChildSeatAlertHolder.instance;
    }

    public void init() {
        soundPlayInit();
        BroadcastManager.getInstance().addBootCompleteTask(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.safetyseat.-$$Lambda$ChildSeatAlert$a8NdZ90wgoHDYIMlEYMI92uZgew
            @Override // java.lang.Runnable
            public final void run() {
                ChildSeatAlert.this.lambda$init$2$ChildSeatAlert();
            }
        });
        ChildSaftySeatManager.getInstance().addInnerDeviceListener(this.mEventListener);
        appUninstallMonitor();
    }

    public /* synthetic */ void lambda$init$2$ChildSeatAlert() {
        List<BaseDevice> deviceList = ChildSaftySeatManager.getInstance().getDevice();
        if (deviceList != null && deviceList.size() > 0) {
            Map<String, String> propMap = deviceList.get(0).getPropertyMap();
            String value = propMap.get("isofix_stat");
            if (!"-1".equals(value)) {
                this.mEventListener.onDeviceEvent(deviceList.get(0).getDeviceId(), "isofix_stat", value);
            }
        }
    }

    private void appUninstallMonitor() {
        List<String> actionList = new ArrayList<>();
        actionList.add("android.intent.action.PACKAGE_FULLY_REMOVED");
        String str = TAG;
        LogUtil.d(str, "appUninstallMonitor,action=" + actionList);
        BroadcastManager.getInstance().registerPackageListener(new BroadcastManager.BroadcastListener() { // from class: com.xiaopeng.xuiservice.iot.manager.safetyseat.-$$Lambda$ChildSeatAlert$PDEmicO2sLYqc6w22alwxblvwKQ
            @Override // com.xiaopeng.xuiservice.utils.BroadcastManager.BroadcastListener
            public final void onReceive(Context context, Intent intent) {
                ChildSeatAlert.lambda$appUninstallMonitor$4(context, intent);
            }
        }, actionList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$appUninstallMonitor$4(Context context, Intent intent) {
        String action = intent.getAction();
        if (((action.hashCode() == 1580442797 && action.equals("android.intent.action.PACKAGE_FULLY_REMOVED")) ? (char) 0 : (char) 65535) == 0) {
            String str = TAG;
            LogUtil.d(str, "pkg remove=" + intent.getDataString() + ",target=" + seatAppPackage);
            if (seatAppPackage.equals(intent.getDataString())) {
                String str2 = TAG;
                LogUtil.i(str2, "seat package removed,name=" + intent.getDataString());
                XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.iot.manager.safetyseat.-$$Lambda$ChildSeatAlert$W_HHP-vgVM2HiCwhJzzRpwG0pAs
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChildSaftySeatManager.getInstance().removeDeviceWhenPackageUninstall();
                    }
                });
            }
        }
    }

    private void doIsoFixAlert(String status) {
        int resId;
        if (!BroadcastManager.isBootComplete()) {
            LogUtil.w(TAG, "doIsoFixAlert, boot not complete...");
            return;
        }
        if ("0".equals(status)) {
            resId = R.string.child_seat_isofix_disconnected;
        } else if ("1".equals(status)) {
            resId = R.string.child_seat_isofix_connected;
        } else {
            LogUtil.w(TAG, "doIsoFixAlert, status error");
            return;
        }
        ToastUtil.showToast(ActivityThread.currentActivityThread().getApplication(), resId);
        if (R.string.child_seat_isofix_disconnected == resId) {
            playAlertSound();
        }
    }

    private void soundPlayInit() {
        this.mAudioManager = (AudioManager) ActivityThread.currentActivityThread().getApplication().getSystemService("audio");
        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(4).setContentType(4).build();
        this.mSoundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(1).build();
        this.mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() { // from class: com.xiaopeng.xuiservice.iot.manager.safetyseat.-$$Lambda$ChildSeatAlert$aUvaKskEc1-J_R8DGinE_P6EwIE
            @Override // android.media.SoundPool.OnLoadCompleteListener
            public final void onLoadComplete(SoundPool soundPool, int i, int i2) {
                ChildSeatAlert.lambda$soundPlayInit$5(soundPool, i, i2);
            }
        });
        this.mSoundId = this.mSoundPool.load(this.soundFilePath, 1);
        if (-1 != this.mSoundId) {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(this.soundFilePath);
            String durationstr = mmr.extractMetadata(9);
            this.soundDuration = Long.valueOf(durationstr).longValue();
            String str = TAG;
            LogUtil.d(str, "soundPlayInit,soundId=" + this.mSoundId + ",duration=" + this.soundDuration);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$soundPlayInit$5(SoundPool sound, int sampleId, int status) {
        String str = TAG;
        LogUtil.d(str, "sound load complete,id=" + sampleId + ",status=" + status);
    }

    private void playAlertSound() {
        SoundPool soundPool;
        if (-1 != this.mSoundId && (soundPool = this.mSoundPool) != null) {
            synchronized (soundPool) {
                if (this.mStreamID > 0) {
                    String str = TAG;
                    LogUtil.i(str, "stop former play,streamId=" + this.mStreamID);
                    XuiWorkHandler.getInstance().removeCallbacks(this.soundPlayEndTask, this.mSoundPool);
                    this.mAudioManager.abandonAudioFocus(null);
                    this.mSoundPool.stop(this.mStreamID);
                    this.mStreamID = 0;
                }
            }
            this.mAudioManager.requestAudioFocus(null, 4, 3);
            this.mStreamID = this.mSoundPool.play(this.mSoundId, 1.0f, 1.0f, 0, 3, 1.0f);
            if (this.mStreamID > 0) {
                XuiWorkHandler.getInstance().postDelayed(this.soundPlayEndTask, this.mSoundPool, this.soundDuration * 3);
            } else {
                this.mAudioManager.abandonAudioFocus(null);
            }
            String str2 = TAG;
            LogUtil.i(str2, "playAlertSound,streamId=" + this.mStreamID);
            return;
        }
        String str3 = TAG;
        LogUtil.w(str3, "playAlertSound,mSoundId=" + this.mSoundId + ",mSoundPool=" + this.mSoundPool);
    }
}
