package com.xiaopeng.xuiservice.capabilities;

import android.app.ActivityThread;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.ArrayMap;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.xapp.Constants;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes5.dex */
public class XpSoundPlayService {
    public static final int EVENT_DONE = 1;
    public static final int EVENT_ERROR = 2;
    public static final int EVENT_RESTART = 4;
    public static final int EVENT_START = 0;
    public static final int EVENT_STOP = 3;
    private static final int FLAG_ALARM = 16;
    private static final int MESSAGE_FINISH = 2;
    private static final int MESSAGE_PLAY = 0;
    private static final int MESSAGE_STOP = 1;
    private static final String PACKAGE_NAME = "android.media.vol_change.PACKAGE_NAME";
    private static final String STREAM_TYPE = "android.media.EXTRA_VOLUME_STREAM_TYPE";
    private static final String TAG = "XpSoundPlayService";
    private static final String VOLUME_CHANGED = "android.media.VOLUME_CHANGED_ACTION";
    private static final boolean hasStereoSound = XUIConfig.isAudioStereoEnable();
    private boolean hasTurndownVolume;
    private AudioManager mAudioManager;
    private Context mContext;
    private Map<Integer, Integer> mDurationMap;
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    private CopyOnWriteArrayList<PlaySoundEventListener> mListeners;
    private SoundPool.OnLoadCompleteListener mLoadCompleteListener;
    private Map<Integer, PlaySound> mLoopSoundMap;
    private BroadcastReceiver mReceiver;
    private Map<String, Integer> mSampleIdMap;
    private int mSignal;
    private SoundPool mSoundPool;
    private long mStartTime;
    private int mStreamId;
    private int mStreamVolume;

    /* loaded from: classes5.dex */
    public interface PlaySoundEventListener {
        void onPlaySoundEventCallBack(int i, int i2, int i3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class PlaySound {
        float leftVolume;
        int loop;
        int position;
        int priority;
        float rightVolume;
        int sampleId;
        int signal;

        public PlaySound(int sampleId, int signal, float leftVolume, float rightVolume, int position, int priority, int loop) {
            this.sampleId = sampleId;
            this.signal = signal;
            this.leftVolume = leftVolume;
            this.rightVolume = rightVolume;
            this.position = position;
            this.priority = priority;
            this.loop = loop;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class StopSound {
        boolean shutUp;
        int signal;
        int streamId;

        public StopSound(int signal, int streamId, boolean shutUp) {
            this.signal = signal;
            this.streamId = streamId;
            this.shutUp = shutUp;
        }
    }

    protected void release() {
        LogUtil.d(TAG, "release");
        unregisterReceiver();
        SoundPool soundPool = this.mSoundPool;
        if (soundPool != null) {
            soundPool.autoPause();
            for (String path : this.mSampleIdMap.keySet()) {
                LogUtil.d(TAG, "unload sampleId=" + this.mSampleIdMap.get(path) + ", path=" + path);
                this.mSoundPool.unload(this.mSampleIdMap.get(path).intValue());
            }
            this.mSampleIdMap.clear();
            this.mDurationMap.clear();
            this.mLoopSoundMap.clear();
            this.mSoundPool.release();
            this.mSoundPool = null;
        }
    }

    private void turndownVolume() {
        if (!this.hasTurndownVolume && this.mStreamVolume > 0) {
            LogUtil.d(TAG, "turndownVolume");
            int i = this.mStreamVolume;
            int volume = i < 5 ? 1 : i / 5;
            this.mAudioManager.temporaryChangeVolumeDown(3, Math.round(volume), 16, false);
            this.hasTurndownVolume = true;
        }
    }

    private void turnupVolume() {
        if (this.hasTurndownVolume) {
            LogUtil.d(TAG, "turnupVolume");
            this.mAudioManager.temporaryChangeVolumeDown(3, this.mStreamVolume, 16, true);
            this.hasTurndownVolume = false;
        }
    }

    public synchronized void addPlaySoundEventListener(PlaySoundEventListener listener) {
        if (!this.mListeners.contains(listener)) {
            this.mListeners.add(listener);
        }
    }

    public synchronized void removePlaySoundEventListener(PlaySoundEventListener listener) {
        if (!this.mListeners.contains(listener)) {
            this.mListeners.remove(listener);
        }
    }

    public int getSignalId(int signal) {
        return (signal >> 16) & 65535;
    }

    public int getSignalVal(int signal) {
        return 65535 & signal;
    }

    private void sendFinshSound(StopSound stopSound, int mdelay) {
        Message message = this.mHandler.obtainMessage();
        message.what = 2;
        message.obj = stopSound;
        this.mHandler.removeMessages(2);
        this.mHandler.sendMessageDelayed(message, mdelay);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasVolumePackage(String obj) {
        return obj.equals("com.xiaopeng.xuiservice") || obj.equals(Constants.PACKAGE_CAR_CONTROL) || obj.equals("xpaudio_btcall");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasStreamMusic(int type) {
        return type == 3;
    }

    private void registerReceiver() {
        LogUtil.i(TAG, "registerReceiver");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        this.mContext.registerReceiver(this.mReceiver, filter);
    }

    private void unregisterReceiver() {
        LogUtil.i(TAG, "unregisterReceiver");
        this.mContext.unregisterReceiver(this.mReceiver);
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final XpSoundPlayService sService = new XpSoundPlayService();

        private InstanceHolder() {
        }
    }

    public static XpSoundPlayService getInstance() {
        return InstanceHolder.sService;
    }

    private XpSoundPlayService() {
        this.mSoundPool = null;
        this.hasTurndownVolume = false;
        this.mStreamVolume = 0;
        this.mStreamId = 0;
        this.mSignal = 0;
        this.mStartTime = 0L;
        this.mSampleIdMap = new ArrayMap();
        this.mDurationMap = new ArrayMap();
        this.mLoopSoundMap = new ArrayMap();
        this.mHandlerThread = null;
        this.mHandler = null;
        this.mListeners = new CopyOnWriteArrayList<>();
        this.mReceiver = new BroadcastReceiver() { // from class: com.xiaopeng.xuiservice.capabilities.XpSoundPlayService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals("android.media.VOLUME_CHANGED_ACTION")) {
                    String name = intent.getStringExtra(XpSoundPlayService.PACKAGE_NAME);
                    int type = intent.getIntExtra(XpSoundPlayService.STREAM_TYPE, -1);
                    if (!XpSoundPlayService.this.hasVolumePackage(name) && XpSoundPlayService.this.hasStreamMusic(type)) {
                        XpSoundPlayService xpSoundPlayService = XpSoundPlayService.this;
                        xpSoundPlayService.mStreamVolume = xpSoundPlayService.mAudioManager.getStreamVolume(type);
                    }
                }
            }
        };
        this.mLoadCompleteListener = new SoundPool.OnLoadCompleteListener() { // from class: com.xiaopeng.xuiservice.capabilities.XpSoundPlayService.3
            @Override // android.media.SoundPool.OnLoadCompleteListener
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                LogUtil.i(XpSoundPlayService.TAG, "onLoadComplete, simpleId=" + sampleId + ", status=" + status);
            }
        };
        LogUtil.d(TAG, "init, hasStereoSound is " + hasStereoSound);
        this.mContext = ActivityThread.currentActivityThread().getApplication();
        this.mHandlerThread = new HandlerThread("XpSoundPlayThread");
        this.mHandlerThread.start();
        this.mHandler = new Handler(this.mHandlerThread.getLooper()) { // from class: com.xiaopeng.xuiservice.capabilities.XpSoundPlayService.2
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int i = msg.what;
                if (i == 0) {
                    XpSoundPlayService.this.handlePlay((PlaySound) msg.obj);
                } else if (i == 1) {
                    XpSoundPlayService.this.handleStop(msg.arg1);
                } else if (i == 2) {
                    XpSoundPlayService.this.handleFinshAndRecovery((StopSound) msg.obj);
                }
            }
        };
        registerReceiver();
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        this.mStreamVolume = this.mAudioManager.getStreamVolume(3);
        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(1).setContentType(2).setLegacyStreamType(4).build();
        this.mSoundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(1).build();
        this.mSoundPool.setOnLoadCompleteListener(this.mLoadCompleteListener);
    }

    private void showPlaySoundStatus(int event, int signal, int streamId) {
        if (!this.mListeners.isEmpty()) {
            Iterator<PlaySoundEventListener> it = this.mListeners.iterator();
            while (it.hasNext()) {
                PlaySoundEventListener listener = it.next();
                listener.onPlaySoundEventCallBack(event, signal, streamId);
            }
        }
    }

    private int getSoundDuration(String path) {
        int ret = -1;
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            try {
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                ret = mediaPlayer.getDuration();
            } catch (Exception e) {
                LogUtil.e(TAG, "get " + path + " duration failed!");
            }
            return ret;
        } finally {
            mediaPlayer.release();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePlay(PlaySound playSound) {
        int i;
        if (this.mStreamId != 0 && getSignalId(playSound.signal) == getSignalId(this.mSignal)) {
            LogUtil.i(TAG, "stop streamId=" + this.mStreamId);
            this.mSoundPool.stop(this.mStreamId);
        }
        int streamId = this.mSoundPool.play(playSound.sampleId, hasStereoSound ? playSound.leftVolume : 1.0f, hasStereoSound ? playSound.rightVolume : 1.0f, playSound.priority, playSound.loop, 1.0f, playSound.position, 0);
        if (streamId == 0) {
            LogUtil.e(TAG, "play sound(signal=" + playSound.signal + ") failed!");
            showPlaySoundStatus(2, playSound.signal, -1);
            return;
        }
        int signalId = getSignalId(playSound.signal);
        int i2 = this.mStreamId;
        if (i2 != 0 && (i = this.mSignal) != 0) {
            showPlaySoundStatus(3, i, i2);
        }
        this.mStreamId = streamId;
        this.mSignal = playSound.signal;
        LogUtil.i(TAG, "play sound(signal=" + playSound.signal + "), sampleId=" + playSound.sampleId + ", streamId=" + streamId + ", position=" + playSound.position + ", priority=" + playSound.priority + ", loop=" + playSound.loop);
        if (playSound.loop == -1) {
            this.mStartTime = System.currentTimeMillis();
            this.mLoopSoundMap.put(Integer.valueOf(signalId), playSound);
        } else if (this.mLoopSoundMap.containsKey(Integer.valueOf(signalId))) {
            this.mLoopSoundMap.remove(Integer.valueOf(signalId));
        }
        showPlaySoundStatus(0, playSound.signal, streamId);
        if (playSound.loop != -1) {
            int delay = (this.mDurationMap.get(Integer.valueOf(playSound.sampleId)).intValue() * (playSound.loop + 1)) + 200;
            sendFinshSound(new StopSound(playSound.signal, streamId, false), delay);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStop(int signal) {
        int signalId = getSignalId(signal);
        if (this.mLoopSoundMap.containsKey(Integer.valueOf(signalId))) {
            PlaySound sound = this.mLoopSoundMap.get(Integer.valueOf(signalId));
            int duration = this.mDurationMap.get(Integer.valueOf(sound.sampleId)).intValue();
            this.mLoopSoundMap.remove(Integer.valueOf(signalId));
            if (this.mStreamId != 0 && signalId == getSignalId(this.mSignal)) {
                int elapse = (int) (System.currentTimeMillis() - this.mStartTime);
                int delay = duration - (elapse % duration);
                LogUtil.i(TAG, "stop sound(signal=" + signal + ", streamId=" + this.mStreamId + ") after " + delay + "ms");
                sendFinshSound(new StopSound(signal, this.mStreamId, true), delay);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFinshAndRecovery(StopSound stopSound) {
        if (stopSound.streamId == this.mStreamId) {
            StringBuilder sb = new StringBuilder();
            sb.append(stopSound.shutUp ? "stop" : "finish");
            sb.append(" sound, streamId= ");
            sb.append(this.mStreamId);
            LogUtil.i(TAG, sb.toString());
            if (stopSound.shutUp) {
                this.mSoundPool.stop(stopSound.streamId);
                showPlaySoundStatus(3, stopSound.signal, stopSound.streamId);
            } else if (stopSound.signal != 0) {
                showPlaySoundStatus(1, stopSound.signal, stopSound.streamId);
            }
            if (this.mLoopSoundMap.isEmpty()) {
                this.mStreamId = 0;
                this.mSignal = 0;
                return;
            }
            PlaySound sound = null;
            for (Integer signalId : this.mLoopSoundMap.keySet()) {
                PlaySound it = this.mLoopSoundMap.get(signalId);
                if (sound == null || it.priority > sound.priority) {
                    sound = it;
                }
            }
            int streamId = this.mSoundPool.play(sound.sampleId, hasStereoSound ? sound.leftVolume : 1.0f, hasStereoSound ? sound.rightVolume : 1.0f, sound.priority, -1, 1.0f, sound.position, 0);
            if (streamId == 0) {
                LogUtil.e(TAG, "replay sound failed, retry...");
                sendFinshSound(new StopSound(0, this.mStreamId, false), 200);
                return;
            }
            this.mStreamId = streamId;
            this.mSignal = sound.signal;
            LogUtil.i(TAG, "replay sound(signal=" + sound.signal + "), sampleId=" + sound.sampleId + ", streamId=" + streamId + ", position=" + sound.position + ", priority=" + sound.priority);
            showPlaySoundStatus(4, sound.signal, streamId);
        }
    }

    public synchronized void load(String path) {
        boolean hasVaild = true;
        String name = path.substring(path.lastIndexOf("/") + 1);
        if (!this.mSampleIdMap.containsKey(name)) {
            int sampleId = this.mSoundPool.load(path, 1);
            int duration = getSoundDuration(path);
            if (sampleId <= 0 || duration <= 0) {
                hasVaild = false;
            }
            if (hasVaild) {
                this.mSampleIdMap.put(name, Integer.valueOf(sampleId));
                this.mDurationMap.put(Integer.valueOf(sampleId), Integer.valueOf(duration));
            }
            StringBuilder sb = new StringBuilder();
            sb.append("load ");
            sb.append(name);
            sb.append(hasVaild ? " succeed!" : " failed!");
            LogUtil.i(TAG, sb.toString());
        }
    }

    public synchronized void play(int signal, String path, float leftVolume, float rightVolume, int position, int priority, int loop) {
        String name = path.substring(path.lastIndexOf("/") + 1);
        if (!this.mSampleIdMap.containsKey(name)) {
            LogUtil.e(TAG, "play sound(signal=" + signal + ") failed! " + name + " not found");
            showPlaySoundStatus(2, signal, -1);
        } else {
            Message message = this.mHandler.obtainMessage();
            PlaySound playSound = new PlaySound(this.mSampleIdMap.get(name).intValue(), signal, leftVolume, rightVolume, position, priority, loop);
            LogUtil.d(TAG, "send play, signal= " + signal);
            message.what = 0;
            message.obj = playSound;
            this.mHandler.sendMessage(message);
        }
    }

    public synchronized void stop(int signal) {
        Message message = this.mHandler.obtainMessage();
        LogUtil.d(TAG, "send stop, signal= " + signal);
        message.what = 1;
        message.arg1 = signal;
        this.mHandler.sendMessage(message);
    }
}
