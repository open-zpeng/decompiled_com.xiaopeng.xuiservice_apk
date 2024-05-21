package com.xiaopeng.xuiservice.musicrecognize;

import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import com.xiaopeng.xuimanager.musicrecognize.IMusicRecognize;
import com.xiaopeng.xuimanager.musicrecognize.IMusicRecognizeEventListener;
import com.xiaopeng.xuimanager.musicrecognize.MusicRecognizeEvent;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIServiceBase;
import com.xiaopeng.xuiservice.debug.WatchDog;
import com.xiaopeng.xuiservice.musicrecognize.MusicRecognizeHalService;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes5.dex */
public class MusicRecognizeService extends IMusicRecognize.Stub implements XUIServiceBase, MusicRecognizeHalService.MusicRecognizeHalListener {
    public static final boolean DBG = true;
    public static final String TAG = "MUSICRECOGNIZE.SERVICE.MusicRecognizeService";
    private final Context mContext;
    private final Map<IBinder, IMusicRecognizeEventListener> mListenersMap = new HashMap();
    private final Map<IBinder, MusicRecognizeDeathRecipient> mDeathRecipientMap = new HashMap();
    private final MusicRecognizeHalService mMusicRecognizeHal = new MusicRecognizeHalService();

    public MusicRecognizeService(Context context) {
        this.mContext = context;
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        String info = "code=" + code + ",pid=" + Binder.getCallingPid() + "@" + SystemClock.elapsedRealtimeNanos();
        WatchDog.getInstance().binderEnter("MusicRecognizeService", info);
        try {
            try {
                boolean ret = super.onTransact(code, data, reply, flags);
                return ret;
            } catch (RemoteException e) {
                LogUtil.w(TAG, "onTransact e=" + e + ",code=" + code);
                throw e;
            }
        } finally {
            WatchDog.getInstance().binderLeave("MusicRecognizeService", info);
        }
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void init() {
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void release() {
        for (MusicRecognizeDeathRecipient recipient : this.mDeathRecipientMap.values()) {
            recipient.release();
        }
        this.mDeathRecipientMap.clear();
        this.mListenersMap.clear();
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void dump(PrintWriter writer) {
    }

    public synchronized void registerListener(IMusicRecognizeEventListener listener) {
        LogUtil.d(TAG, "registerListener");
        if (listener == null) {
            LogUtil.e(TAG, "registerListener: Listener is null.");
            throw new IllegalArgumentException("listener cannot be null.");
        }
        IBinder listenerBinder = listener.asBinder();
        if (this.mListenersMap.containsKey(listenerBinder)) {
            return;
        }
        MusicRecognizeDeathRecipient deathRecipient = new MusicRecognizeDeathRecipient(listenerBinder);
        try {
            listenerBinder.linkToDeath(deathRecipient, 0);
            this.mDeathRecipientMap.put(listenerBinder, deathRecipient);
            if (this.mListenersMap.isEmpty()) {
                this.mMusicRecognizeHal.setListener(this);
            }
            this.mListenersMap.put(listenerBinder, listener);
        } catch (RemoteException e) {
            LogUtil.e(TAG, "Failed to link death for recipient. " + e);
            throw new IllegalStateException("XUIServiceNotConnected");
        }
    }

    public synchronized void unregisterListener(IMusicRecognizeEventListener listener) {
        LogUtil.d(TAG, "unregisterListener");
        if (listener == null) {
            LogUtil.e(TAG, "unregisterListener: listener was not registered");
            return;
        }
        IBinder listenerBinder = listener.asBinder();
        if (!this.mListenersMap.containsKey(listenerBinder)) {
            LogUtil.e(TAG, "unregisterListener: Listener was not previously registered.");
        }
        unregisterListenerLocked(listenerBinder);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterListenerLocked(IBinder listenerBinder) {
        Object status = this.mListenersMap.remove(listenerBinder);
        if (status != null) {
            this.mDeathRecipientMap.get(listenerBinder).release();
            this.mDeathRecipientMap.remove(listenerBinder);
        }
        if (this.mListenersMap.isEmpty()) {
            this.mMusicRecognizeHal.setListener(null);
        }
    }

    public void start() {
        LogUtil.d(TAG, "start()");
        this.mMusicRecognizeHal.start();
    }

    public void stop() {
        this.mMusicRecognizeHal.stop();
    }

    public void stopAndRecognize() {
        this.mMusicRecognizeHal.stopAndRecognize();
    }

    public int getMode() {
        return this.mMusicRecognizeHal.getMode();
    }

    public void setMode(int mode) {
        this.mMusicRecognizeHal.setMode(mode);
    }

    public float getMinScore() {
        return this.mMusicRecognizeHal.getMinScore();
    }

    public void setMinScore(float score) {
        this.mMusicRecognizeHal.setMinScore(score);
    }

    public void findSongCover(String songName) {
        this.mMusicRecognizeHal.findSongCover(songName);
    }

    @Override // com.xiaopeng.xuiservice.musicrecognize.MusicRecognizeHalService.MusicRecognizeHalListener
    public void onFindCoverEvent(String coverUrl) {
        for (IMusicRecognizeEventListener l : this.mListenersMap.values()) {
            try {
                l.onFindCoverEvent(coverUrl);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onFindCoverEvent calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.musicrecognize.MusicRecognizeHalService.MusicRecognizeHalListener
    public void onRecognizeEvent(MusicRecognizeEvent event) {
        for (IMusicRecognizeEventListener l : this.mListenersMap.values()) {
            try {
                l.onRecognizeEvent(event);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onRecognizeEvent calling failed: " + ex);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.musicrecognize.MusicRecognizeHalService.MusicRecognizeHalListener
    public void onError(int errorCode, int operation) {
        for (IMusicRecognizeEventListener l : this.mListenersMap.values()) {
            try {
                l.onError(errorCode, operation);
            } catch (RemoteException ex) {
                LogUtil.e(TAG, "onError calling failed: " + ex);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public class MusicRecognizeDeathRecipient implements IBinder.DeathRecipient {
        private static final String TAG = "MUSICRECOGNIZE.SERVICE.MusicRecognizeService.MusicRecognizeDeathRecipient";
        private IBinder mListenerBinder;

        MusicRecognizeDeathRecipient(IBinder listenerBinder) {
            this.mListenerBinder = listenerBinder;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            LogUtil.d(TAG, "binderDied " + this.mListenerBinder);
            MusicRecognizeService.this.unregisterListenerLocked(this.mListenerBinder);
        }

        void release() {
            this.mListenerBinder.unlinkToDeath(this, 0);
        }
    }
}
