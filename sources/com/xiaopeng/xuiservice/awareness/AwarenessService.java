package com.xiaopeng.xuiservice.awareness;

import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import com.xiaopeng.xuimanager.awareness.IAwareness;
import com.xiaopeng.xuimanager.awareness.IAwarenessEventListener;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIServiceBase;
import com.xiaopeng.xuiservice.awareness.AwarenessHalService;
import com.xiaopeng.xuiservice.debug.WatchDog;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes5.dex */
public class AwarenessService extends IAwareness.Stub implements XUIServiceBase, AwarenessHalService.AwarenessHalListener {
    public static final boolean DBG = true;
    public static final String TAG = "AWARENESS.SERVICE .AwarenessService";
    private final Context mContext;
    private final Map<IBinder, IAwarenessEventListener> mListenersMap = new HashMap();
    private final Map<IBinder, AwarenessDeathRecipient> mDeathRecipientMap = new HashMap();
    private final AwarenessHalService mAwarenessHal = new AwarenessHalService();

    public AwarenessService(Context context) {
        this.mContext = context;
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        String info = "code=" + code + ",pid=" + Binder.getCallingPid() + "@" + SystemClock.elapsedRealtimeNanos();
        WatchDog.getInstance().binderEnter("AwarenessService", info);
        try {
            try {
                boolean ret = super.onTransact(code, data, reply, flags);
                return ret;
            } catch (RemoteException e) {
                LogUtil.w(TAG, "onTransact e=" + e + ",code=" + code);
                throw e;
            }
        } finally {
            WatchDog.getInstance().binderLeave("AwarenessService", info);
        }
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public synchronized void init() {
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public synchronized void release() {
        for (AwarenessDeathRecipient recipient : this.mDeathRecipientMap.values()) {
            recipient.release();
        }
        this.mDeathRecipientMap.clear();
        this.mListenersMap.clear();
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void dump(PrintWriter writer) {
    }

    public synchronized void registerListener(IAwarenessEventListener listener) {
        LogUtil.d(TAG, "registerListener");
        if (listener == null) {
            LogUtil.e(TAG, "registerListener: Listener is null.");
            throw new IllegalArgumentException("listener cannot be null.");
        }
        IBinder listenerBinder = listener.asBinder();
        if (this.mListenersMap.containsKey(listenerBinder)) {
            return;
        }
        AwarenessDeathRecipient deathRecipient = new AwarenessDeathRecipient(listenerBinder);
        try {
            listenerBinder.linkToDeath(deathRecipient, 0);
            this.mDeathRecipientMap.put(listenerBinder, deathRecipient);
            if (this.mListenersMap.isEmpty()) {
                this.mAwarenessHal.setListener(this);
            }
            this.mListenersMap.put(listenerBinder, listener);
        } catch (RemoteException e) {
            LogUtil.e(TAG, "Failed to link death for recipient. " + e);
            throw new IllegalStateException("XUIServiceNotConnected");
        }
    }

    public synchronized void unregisterListener(IAwarenessEventListener listener) {
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
            this.mAwarenessHal.setListener(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public class AwarenessDeathRecipient implements IBinder.DeathRecipient {
        private static final String TAG = "AWARENESS.SERVICE .AwarenessService.AwarenessDeathRecipient";
        private IBinder mListenerBinder;

        AwarenessDeathRecipient(IBinder listenerBinder) {
            this.mListenerBinder = listenerBinder;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            LogUtil.d(TAG, "binderDied " + this.mListenerBinder);
            AwarenessService.this.unregisterListenerLocked(this.mListenerBinder);
        }

        void release() {
            this.mListenerBinder.unlinkToDeath(this, 0);
        }
    }

    @Override // com.xiaopeng.xuiservice.awareness.AwarenessHalService.AwarenessHalListener
    public synchronized void onError(int errorCode, int operation) {
    }
}
