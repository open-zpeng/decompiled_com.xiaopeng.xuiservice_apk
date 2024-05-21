package com.xiaopeng.xuiservice.mediacenter.bluetooth;

import android.app.ActivityThread;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.mediacenter.Constants;
import com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes5.dex */
public class PassengerBtCommandListener implements BroadcastManager.BroadcastListener {
    private static final String TAG = "PassengerBtController";
    private List<String> mActions = new ArrayList();
    private AudioManager mAudioManager = (AudioManager) ActivityThread.currentActivityThread().getApplication().getSystemService("audio");
    private MediaCenterHalService mMediaCenterHalService;

    public PassengerBtCommandListener(MediaCenterHalService halService) {
        this.mMediaCenterHalService = halService;
        this.mActions.add(Constants.ACTION_PASSENGER_BT_PRESSED);
    }

    public void register() {
        LogUtil.d(TAG, "register receiver");
        BroadcastManager.getInstance().registerListenerDynamically(this, this.mActions);
    }

    @Override // com.xiaopeng.xuiservice.utils.BroadcastManager.BroadcastListener
    public void onReceive(Context context, Intent intent) {
        LogUtil.d(TAG, "onReceiver action:" + intent.getAction());
        if (Constants.ACTION_PASSENGER_BT_PRESSED.equals(intent.getAction())) {
            int keycode = intent.getIntExtra("keycode", -1);
            if (keycode == 24) {
                upVolume();
            } else if (keycode == 25) {
                downVolume();
            } else if (keycode == 85) {
                pauseOrPlay();
            } else if (keycode == 91) {
                mute();
            } else if (keycode == 87) {
                next();
            } else if (keycode == 88) {
                previous();
            }
        }
    }

    private void upVolume() {
        this.mAudioManager.adjustStreamVolume(13, 1, 0);
    }

    private void downVolume() {
        this.mAudioManager.adjustStreamVolume(13, -1, 0);
    }

    private void mute() {
        this.mAudioManager.adjustStreamVolume(13, 101, 0);
    }

    private void next() {
        this.mMediaCenterHalService.playbackControl(1, 6, 0);
    }

    private void previous() {
        this.mMediaCenterHalService.playbackControl(1, 7, 0);
    }

    private void pauseOrPlay() {
        this.mMediaCenterHalService.playbackControl(1, 2, 0);
    }
}
