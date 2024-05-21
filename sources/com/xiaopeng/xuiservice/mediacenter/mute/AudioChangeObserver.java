package com.xiaopeng.xuiservice.mediacenter.mute;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.text.TextUtils;
import com.xiaopeng.xuimanager.utils.LogUtil;
/* loaded from: classes5.dex */
public class AudioChangeObserver extends BroadcastReceiver {
    private static final String TAG = "MSAudioChangeObserver";
    public static final String VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION";
    private AudioManager mAudioManager;
    private Context mContext;
    private boolean mIsXUIMuted;
    private AudioStatusChangedListener mListener;

    /* loaded from: classes5.dex */
    public interface AudioStatusChangedListener {
        void onMuteStatusChanged(boolean z, String str);
    }

    public AudioChangeObserver(Context context, AudioManager audioManager, AudioStatusChangedListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.mAudioManager = audioManager;
    }

    public void register() {
        LogUtil.i(TAG, "register RINGER_MODE_CHANGED_ACTION");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.media.RINGER_MODE_CHANGED");
        intentFilter.addAction("android.media.VOLUME_CHANGED_ACTION");
        this.mContext.registerReceiver(this, intentFilter);
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        boolean isAuto;
        String action = intent.getAction();
        boolean isXUIMuted = false;
        int hasMusicActiveFlag = intent.getIntExtra("android.media.EXTRA_HAS_MUSIC_RUNNING_FLAG", 0);
        String otherPlayPackages = intent.getStringExtra("android.media.other_musicplaying.PACKAGE_NAME");
        int streamType = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1);
        int xuiFlag = intent.getIntExtra("android.media.EXTRA_AUTOVOLUME_FLAG", 0);
        LogUtil.i(TAG, "onReceive: " + action + ", hasMusicActiveFlag:" + hasMusicActiveFlag + ", streemType:" + streamType + " &xuiFlag:" + xuiFlag);
        if (xuiFlag != 1) {
            isAuto = false;
        } else {
            isAuto = true;
        }
        if (isAuto) {
            return;
        }
        if ((TextUtils.equals(action, "android.media.RINGER_MODE_CHANGED") || TextUtils.equals(action, "android.media.VOLUME_CHANGED_ACTION")) && streamType == 3) {
            boolean isMute = this.mAudioManager.isStreamMute(3);
            int currentVolume = this.mAudioManager.getStreamVolume(streamType);
            LogUtil.i(TAG, "isMute:" + isMute + " &currentVolume:" + currentVolume);
            if (isMute || currentVolume == 0) {
                isXUIMuted = true;
            }
            if (isXUIMuted != this.mIsXUIMuted) {
                LogUtil.d(TAG, "onMuteStatusChanged:" + isXUIMuted);
                this.mListener.onMuteStatusChanged(isXUIMuted, otherPlayPackages);
            }
            this.mIsXUIMuted = isXUIMuted;
        }
    }
}
