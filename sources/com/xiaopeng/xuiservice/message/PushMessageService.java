package com.xiaopeng.xuiservice.message;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import com.alipay.mobile.aromeservice.ResponseParams;
import com.xiaopeng.xuimanager.message.IMessage;
import com.xiaopeng.xuimanager.message.IMessageListener;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIServiceBase;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import com.xiaopeng.xuiservice.debug.WatchDog;
import com.xiaopeng.xuiservice.message.PushMessage;
import com.xiaopeng.xuiservice.message.PushMessageService;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
/* loaded from: classes5.dex */
public class PushMessageService extends IMessage.Stub implements XUIServiceBase, PushMessage.PushMessageListener {
    private static final String TAG = "MESSAGE.SERVICE." + PushMessageService.class.getSimpleName();
    private final Map<IBinder, ListenerRecord> mListenerMap;

    /* loaded from: classes5.dex */
    private static final class MessageServiceHolder {
        static final PushMessageService INSTANCE = new PushMessageService();

        private MessageServiceHolder() {
        }
    }

    public static PushMessageService getInstance() {
        return MessageServiceHolder.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public final class ListenerRecord implements IBinder.DeathRecipient {
        final IMessageListener listener;
        final String messageType;
        final int pid;
        final int uid;

        public ListenerRecord(String messageType, IMessageListener listener, int pid, int uid) {
            this.messageType = messageType;
            this.listener = listener;
            this.pid = pid;
            this.uid = uid;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            String str = PushMessageService.TAG;
            LogUtil.w(str, "binder died,pid=" + this.pid + ",uid=" + this.uid);
            release();
            synchronized (PushMessageService.this.mListenerMap) {
                PushMessageService.this.mListenerMap.remove(this.listener.asBinder());
            }
        }

        public void release() {
            try {
                this.listener.asBinder().unlinkToDeath(this, 0);
            } catch (Exception e) {
                String str = PushMessageService.TAG;
                LogUtil.w(str, "ListenerRecord release e=" + e);
            }
        }

        public String toString() {
            return "ListenerRecord{messageType='" + this.messageType + "', listener=" + this.listener + ", pid=" + this.pid + ", uid=" + this.uid + '}';
        }
    }

    private PushMessageService() {
        this.mListenerMap = new HashMap();
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void init() {
        LogUtil.i(TAG, "init");
        DumpDispatcher.registerDump(ResponseParams.RESPONSE_KEY_MESSAGE, new DumpDispatcher.DebugDumper() { // from class: com.xiaopeng.xuiservice.message.-$$Lambda$PushMessageService$KwujArOpsqFV9_eArj8bh3zXiRM
            @Override // com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
            public final void debugDump(PrintWriter printWriter, String[] strArr) {
                PushMessageService.this.handleDump(printWriter, strArr);
            }
        });
        PushMessage.getInstance().setAllMessageListener(this);
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void release() {
        LogUtil.i(TAG, "release");
        synchronized (this.mListenerMap) {
            for (ListenerRecord record : this.mListenerMap.values()) {
                record.release();
            }
            this.mListenerMap.clear();
        }
        PushMessage.getInstance().setAllMessageListener(null);
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void dump(PrintWriter writer) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDump(PrintWriter pw, String[] args) {
        pw.println("dump:" + TAG);
        pw.println("ListenerRecord: size=" + this.mListenerMap.size());
        for (ListenerRecord record : this.mListenerMap.values()) {
            pw.println(record);
        }
    }

    public void registerMessage(String messageType, IMessageListener listener) throws RemoteException {
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        String str = TAG;
        LogUtil.i(str, "registerMessage messageType=" + messageType + ", listener=" + listener + ", pid=" + pid + ",uid=" + uid);
        if (TextUtils.isEmpty(messageType)) {
            LogUtil.w(TAG, "registerMessage messageType is empty");
        } else if (listener == null) {
            LogUtil.w(TAG, "registerMessage listener=null");
        } else {
            IBinder binder = listener.asBinder();
            synchronized (this.mListenerMap) {
                if (this.mListenerMap.containsKey(binder)) {
                    String str2 = TAG;
                    LogUtil.w(str2, "repeat listener-" + binder);
                    return;
                }
                ListenerRecord record = new ListenerRecord(messageType, listener, pid, uid);
                try {
                    binder.linkToDeath(record, 0);
                    this.mListenerMap.put(binder, record);
                } catch (RemoteException e) {
                    String str3 = TAG;
                    LogUtil.w(str3, "registerMessage e=" + e);
                }
            }
        }
    }

    public void unregisterMessage(String messageType, IMessageListener listener) throws RemoteException {
        ListenerRecord record;
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        String str = TAG;
        LogUtil.i(str, "unregisterMessage messageType=" + messageType + ", listener=" + listener + ", pid=" + pid + ", uid=" + uid);
        if (TextUtils.isEmpty(messageType)) {
            LogUtil.w(TAG, "unregisterMessage messageType is empty");
        } else if (listener == null) {
            LogUtil.w(TAG, "unregisterMessage listener=null");
        } else {
            IBinder binder = listener.asBinder();
            String str2 = TAG;
            LogUtil.i(str2, "unregisterMessage,binder-" + binder + ",listeners-" + this.mListenerMap.size());
            synchronized (this.mListenerMap) {
                record = this.mListenerMap.remove(binder);
            }
            if (record == null) {
                String str3 = TAG;
                LogUtil.w(str3, "no registered record for binder-" + binder);
                return;
            }
            record.release();
        }
    }

    public void publishMessage(String messageType, String data) throws RemoteException {
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        String str = TAG;
        LogUtil.i(str, "publishMessage messageType=" + messageType + ", data=" + data + ", pid=" + pid + ", uid=" + uid);
        if (TextUtils.isEmpty(messageType)) {
            LogUtil.w(TAG, "publishMessage messageType is empty");
        } else {
            PushMessage.getInstance().dispatchInternalMessage(messageType, data);
        }
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        String info = "code=" + code + ",pid=" + Binder.getCallingPid() + "@" + SystemClock.elapsedRealtimeNanos();
        WatchDog.getInstance().binderEnter("PushMessageService", info);
        try {
            try {
                boolean ret = super.onTransact(code, data, reply, flags);
                return ret;
            } catch (RemoteException e) {
                LogUtil.w(TAG, "onTransact e=" + e + ",code=" + code);
                throw e;
            }
        } finally {
            WatchDog.getInstance().binderLeave("PushMessageService", info);
        }
    }

    @Override // com.xiaopeng.xuiservice.message.PushMessage.PushMessageListener
    public void onReceivePushMessage(final String messageType, final String data) {
        synchronized (this.mListenerMap) {
            this.mListenerMap.values().stream().filter(new Predicate() { // from class: com.xiaopeng.xuiservice.message.-$$Lambda$PushMessageService$NA3pV7BNXoUggdp7UqUYw0-cSSw
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean equals;
                    equals = ((PushMessageService.ListenerRecord) obj).messageType.equals(messageType);
                    return equals;
                }
            }).forEach(new Consumer() { // from class: com.xiaopeng.xuiservice.message.-$$Lambda$PushMessageService$B_2aWQ8YTGB3UIVs-ppJpUzcdw8
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    PushMessageService.lambda$onReceivePushMessage$1(messageType, data, (PushMessageService.ListenerRecord) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$onReceivePushMessage$1(String messageType, String data, ListenerRecord r) {
        try {
            r.listener.onReceiveMessage(messageType, data);
        } catch (RemoteException e) {
            String str = TAG;
            LogUtil.e(str, "onReceiveMessage, " + e.getMessage());
            e.printStackTrace();
        }
    }
}
