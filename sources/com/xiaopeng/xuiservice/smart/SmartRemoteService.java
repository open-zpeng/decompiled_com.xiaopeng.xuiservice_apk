package com.xiaopeng.xuiservice.smart;

import android.app.ActivityThread;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.avas.CarAvasManager;
import android.car.hardware.tbox.CarTboxManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import com.xiaopeng.xuimanager.XUIServiceNotConnectedException;
import com.xiaopeng.xuimanager.smart.SmartManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.carcontrol.BaseCarListener;
import java.io.File;
import java.io.PrintWriter;
/* loaded from: classes5.dex */
public class SmartRemoteService extends BaseSmartService {
    private static final boolean DBG = true;
    private static final String[] PATH_SURPRISE_SOUND = {"/system/media/audio/xiaopeng/cdu/mp3/surprise_normal.mp3"};
    private static final String XPAPP_EFFECT_ACCHARGE = "xpapp_accharge";
    private static final String XPAPP_EFFECT_AWAKE = "xpapp_awake";
    private static final String XPAPP_EFFECT_DCCHARGE = "xpapp_dccharge";
    private static final String XPAPP_EFFECT_FULLCHARGED = "xpapp_fullcharged";
    private static final String XPAPP_EFFECT_SHOWOFF = "xpapp_showoff";
    private static final String XPAPP_EFFECT_SLEEP = "xpapp_sleep";
    private static final String XPAPP_EFFECT_SURPRISE_NORMAL = "xpapp_surprise_normal";
    private static final int XPAPP_REMOTE_COMMAND_0 = 1;
    private static final int XPAPP_REMOTE_COMMAND_1 = 2;
    private static final int XPAPP_REMOTE_COMMAND_2 = 3;
    private static final int XPAPP_REMOTE_COMMAND_3 = 4;
    private static final int XPAPP_REMOTE_COMMAND_4 = 5;
    private static final int XPAPP_REMOTE_COMMAND_5 = 6;
    private static final int XPAPP_REMOTE_COMMAND_6 = 7;
    private AudioManager mAudioManager;
    private CarAvasManager mCarAvasManager;
    private CarTboxManager.CarTboxEventCallback mCarTboxEventCallback;
    private SmartManager.SmartEventListener mSmartEventListener;
    private SmartManager mSmartManager;
    private MediaPlayer mediaPlayer;
    private int soundEffectStreamType;

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService, com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
    public /* bridge */ /* synthetic */ void debugDump(PrintWriter printWriter, String[] strArr) {
        super.debugDump(printWriter, strArr);
    }

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService
    public /* bridge */ /* synthetic */ void dump(PrintWriter printWriter, String[] strArr) {
        super.dump(printWriter, strArr);
    }

    private SmartRemoteService(Context context) {
        super(context);
        this.mediaPlayer = new MediaPlayer();
        this.soundEffectStreamType = 3;
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
    }

    public static SmartRemoteService getInstance() {
        return InstanceHolder.sService;
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void initCarListener() {
        this.mCarTboxEventCallback = new CarTboxManager.CarTboxEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartRemoteService.1
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557846597) {
                    SmartRemoteService.this.handleRemoteCommand(((Integer) BaseCarListener.getValue(value)).intValue());
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        addTboxManagerListener(this.mCarTboxEventCallback);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        LogUtil.d(this.TAG, "onCarManagerInited");
        this.mCarAvasManager = getCarManager("xp_avas");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    public void initXUIManager() throws XUIServiceNotConnectedException {
        try {
            this.mSmartManager = (SmartManager) getXuiManager().getXUIServiceManager(XUIConfig.PROPERTY_SMART);
            this.mSmartEventListener = new SmartManager.SmartEventListener() { // from class: com.xiaopeng.xuiservice.smart.SmartRemoteService.2
                public void onErrorEvent(int i, int i1) {
                }

                public void onLightEffectFinishEvent(int effectName, int effectMode) {
                }
            };
            this.mSmartManager.registerListener(this.mSmartEventListener);
        } catch (XUIServiceNotConnectedException e) {
            LogUtil.e(this.TAG, "initXUIManager error");
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        removeTboxManagerListener(this.mCarTboxEventCallback);
        try {
            if (this.mSmartManager != null) {
                this.mSmartManager.unregisterListener(this.mSmartEventListener);
            }
        } catch (XUIServiceNotConnectedException e) {
            LogUtil.e(this.TAG, "onRelease error");
        }
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRemoteCommand(int cmd) {
        int avasType = -1;
        String lluEffect = null;
        boolean needPlaySnd = false;
        switch (cmd) {
            case 1:
                avasType = 8;
                lluEffect = XPAPP_EFFECT_AWAKE;
                break;
            case 2:
                avasType = 9;
                lluEffect = XPAPP_EFFECT_FULLCHARGED;
                break;
            case 3:
                avasType = 10;
                lluEffect = XPAPP_EFFECT_SLEEP;
                break;
            case 4:
                avasType = 11;
                lluEffect = XPAPP_EFFECT_ACCHARGE;
                break;
            case 5:
                avasType = 12;
                lluEffect = XPAPP_EFFECT_DCCHARGE;
                break;
            case 6:
                avasType = 6;
                lluEffect = XPAPP_EFFECT_SHOWOFF;
                break;
            case 7:
                lluEffect = XPAPP_EFFECT_SURPRISE_NORMAL;
                needPlaySnd = true;
                break;
        }
        try {
            if (XUIConfig.getAvasSupportType() != 0 && avasType > 0) {
                this.mCarAvasManager.setAvasExternalSound(avasType);
                this.mCarAvasManager.setExternalSoundModeCmd(1);
            }
            if (XUIConfig.hasFeature(XUIConfig.PROPERTY_LLU) && lluEffect != null) {
                this.mSmartManager.setLangLightEffectMode(lluEffect);
            }
            if (needPlaySnd) {
                playSurpriseSound();
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void playSurpriseSound() {
        MediaPlayer mediaPlayer;
        File file = new File(PATH_SURPRISE_SOUND[0]);
        if (file.exists() && (mediaPlayer = this.mediaPlayer) != null) {
            try {
                mediaPlayer.setDataSource(file.getPath());
                this.mediaPlayer.setLooping(false);
                this.mediaPlayer.prepare();
                this.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.xiaopeng.xuiservice.smart.SmartRemoteService.3
                    @Override // android.media.MediaPlayer.OnPreparedListener
                    public void onPrepared(MediaPlayer mp) {
                        SmartRemoteService.this.startfinishPlayer(true);
                    }
                });
                this.mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() { // from class: com.xiaopeng.xuiservice.smart.SmartRemoteService.4
                    @Override // android.media.MediaPlayer.OnErrorListener
                    public boolean onError(MediaPlayer paramMediaPlayer, int paramInt1, int paramInt2) {
                        SmartRemoteService.this.startfinishPlayer(false);
                        return false;
                    }
                });
                this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.xiaopeng.xuiservice.smart.SmartRemoteService.5
                    @Override // android.media.MediaPlayer.OnCompletionListener
                    public void onCompletion(MediaPlayer mp) {
                        SmartRemoteService.this.startfinishPlayer(false);
                    }
                });
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        LogUtil.i(this.TAG, "The sound file isn't exist");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startfinishPlayer(boolean start) {
        AudioManager audioManager = this.mAudioManager;
        if (audioManager != null) {
            if (start) {
                audioManager.requestAudioFocus(null, this.soundEffectStreamType, 2);
            } else {
                audioManager.abandonAudioFocus(null);
            }
        }
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer != null) {
            if (start) {
                mediaPlayer.start();
            } else {
                mediaPlayer.stop();
                this.mediaPlayer.reset();
            }
        }
        try {
            if (this.mSmartManager != null) {
                this.mSmartManager.setFftLLUEnable(!start);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final SmartRemoteService sService = new SmartRemoteService(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected boolean needConnectXUI() {
        return true;
    }
}
