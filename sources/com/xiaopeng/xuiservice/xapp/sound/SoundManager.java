package com.xiaopeng.xuiservice.xapp.sound;

import android.app.ActivityThread;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.provider.Settings;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.utils.PassengerBluetoothManager;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.xapp.Constants;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes5.dex */
public class SoundManager {
    private static final String EXTRA_PREV_VOLUME_STREAM_VALUE = "android.media.EXTRA_PREV_VOLUME_STREAM_VALUE";
    private static final String EXTRA_VOLUME_STREAM_TYPE = "android.media.EXTRA_VOLUME_STREAM_TYPE";
    private static final String EXTRA_VOLUME_STREAM_VALUE = "android.media.EXTRA_VOLUME_STREAM_VALUE";
    private static final String TAG = "SoundManager";
    private static final String VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION";
    private static volatile SoundManager sInstance;
    private AudioManager mAudioManager;
    private Context mContext;
    private final List<OnVolumeChangedListener> mListeners = new ArrayList();
    private SoundEventReceiver mReceiver;

    /* loaded from: classes5.dex */
    public interface OnVolumeChangedListener {
        void onVolumeChanged(int i, int i2, int i3);
    }

    public static SoundManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (SoundManager.class) {
                if (sInstance == null) {
                    sInstance = new SoundManager(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    private SoundManager(Context context) {
        this.mContext = context;
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
    }

    public int getStreamMaxVolume(int streamType) {
        return this.mAudioManager.getStreamMaxVolume(streamType);
    }

    public int getStreamVolume(int streamType) {
        return this.mAudioManager.getStreamVolume(streamType);
    }

    public void adjustStreamVolume(int streamType, int direction, int flag) {
        this.mAudioManager.adjustStreamVolume(streamType, direction, flag);
    }

    public void setStreamVolume(int streamType, int index, int flags) {
        int max = getStreamMaxVolume(streamType);
        if (index > max) {
            index = max;
        }
        if (index < 0) {
            index = 0;
        }
        this.mAudioManager.setStreamVolume(streamType, index, flags);
    }

    public void setCurrentVolume(int displayId, int value) {
        int streamType = getCurrentVolumeType(displayId);
        setStreamVolume(streamType, value, 0);
    }

    public void adjustCurrentVolume(int displayId, int direction) {
        int streamType = getCurrentVolumeType(displayId);
        adjustStreamVolume(streamType, direction, 0);
    }

    private boolean isAVASEnable() {
        return Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), Constants.KEY_AVAS_SPEAKER, 0) == 1;
    }

    public int getCurrentVolumeType(int displayId) {
        if (SharedDisplayManager.hasSharedDisplayFeature() && displayId == 1 && PassengerBluetoothManager.getInstance().isDeviceConnected()) {
            return 13;
        }
        if (isAVASEnable()) {
            return 11;
        }
        return 3;
    }

    public void addOnVolumeChangedListener(OnVolumeChangedListener l) {
        synchronized (this.mListeners) {
            if (this.mListeners.size() == 0 && this.mReceiver == null) {
                this.mReceiver = new SoundEventReceiver();
                this.mReceiver.register(this.mContext);
            }
            if (!this.mListeners.contains(l)) {
                this.mListeners.add(l);
            }
        }
    }

    public void removeOnVolumeChangedListener(OnVolumeChangedListener l) {
        synchronized (this.mListeners) {
            this.mListeners.remove(l);
            if (this.mListeners.size() == 0 && this.mReceiver != null) {
                this.mReceiver.unregister(this.mContext);
                this.mReceiver = null;
            }
        }
    }

    void notifyVolumeChanged(int streamType, int volume, int preVolume) {
        synchronized (this.mListeners) {
            for (OnVolumeChangedListener l : this.mListeners) {
                l.onVolumeChanged(streamType, volume, preVolume);
            }
        }
    }

    /* loaded from: classes5.dex */
    private class SoundEventReceiver extends BroadcastReceiver {
        private static final int STREAM_INVALID = -1;

        private SoundEventReceiver() {
        }

        public void register(Context context) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.media.VOLUME_CHANGED_ACTION");
            context.registerReceiver(this, filter);
        }

        public void unregister(Context context) {
            context.unregisterReceiver(this);
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String pkgName = intent.getStringExtra("android.media.vol_change.PACKAGE_NAME");
            LogUtil.v(SoundManager.TAG, "onReceive: action = " + action + " pkgName:" + pkgName);
            if ("android.media.VOLUME_CHANGED_ACTION".equals(action)) {
                int streamType = intent.getIntExtra(SoundManager.EXTRA_VOLUME_STREAM_TYPE, -1);
                if (streamType == 3 || streamType == 13 || streamType == 11) {
                    int volume = intent.getIntExtra(SoundManager.EXTRA_VOLUME_STREAM_VALUE, 0);
                    int preVolume = intent.getIntExtra(SoundManager.EXTRA_PREV_VOLUME_STREAM_VALUE, 0);
                    SoundManager.this.notifyVolumeChanged(streamType, volume, preVolume);
                }
            }
        }
    }
}
