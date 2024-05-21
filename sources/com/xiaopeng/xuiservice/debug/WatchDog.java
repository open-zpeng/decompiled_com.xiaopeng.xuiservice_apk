package com.xiaopeng.xuiservice.debug;

import android.os.Debug;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.os.Process;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.util.Printer;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import com.xiaopeng.xuiservice.utils.ThreadUtils;
import com.xiaopeng.xuiservice.utils.UiHandler;
import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
/* loaded from: classes5.dex */
public class WatchDog {
    private static final long BINDER_CALL_THRESHOLD = 1000;
    private static final int CHECK_BARRIER_MAX_COUNT = 3;
    private static final long DUMP_INTERVAL_MS = 10000;
    private static final String LOGGER_BEGIN = ">>>>> Dispatching to";
    private static final String LOGGER_END = "<<<<< Finished to";
    private static final long LOOPER_EXECUTE_THRESHOLD = 500;
    private static final int MAX_BINDER_RECORD = 32;
    private static final int MAX_TRACE_COUNT = 50;
    private static final int MSG_BINDER_ENTER = 1;
    private static final int MSG_BINDER_ENTER_CHECK = 3;
    private static final int MSG_BINDER_LEAVE = 2;
    private static final int MSG_IDLE_HANDLER_REPLACE = 3;
    private static final int MSG_LONG_DISPATCH_TEST = 4;
    private static final int MSG_SYNCBARRIER_CHECK = 4;
    private static final int MSG_TEST_ASYNC = 1;
    private static final int MSG_TEST_SYNC = 2;
    private static final String TAG = "XuiWatchDog";
    private static final String TRACE_COUNT_PROP = "sys.xiaopeng.xuitrace";
    private static final String TRACE_FILE_PATH = "/data/Log/log0/xui/xuitrace_";
    private static final String XUI_FILE_DIR = "/data/Log/log0/xui";
    private int barrierCount;
    private long looperLogTick;
    private HashMap<String, BinderRecord> mBinderRecordMap;
    private Field mMessagesfield;
    private Handler mWatchdogHandler;
    private HandlerThread mWatchdogTh;
    private Handler mainHandler;
    private Printer mainLooperPrinter;
    private MessageQueue mainQueue;
    private Runnable syncBarrierCheckRunner;
    private static long mLastDumpTick = 0;
    private static IdleHandlerArrayList<MessageQueue.IdleHandler> idleHandlerArrayList = new IdleHandlerArrayList<>();

    static /* synthetic */ int access$308(WatchDog x0) {
        int i = x0.barrierCount;
        x0.barrierCount = i + 1;
        return i;
    }

    /* loaded from: classes5.dex */
    private static class WatchDogHolder {
        private static final WatchDog instance = new WatchDog();

        private WatchDogHolder() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class BinderRecord {
        public long mEventTick;
        public String mInfo;
        public String mModule;

        public BinderRecord(String module, String info, long tick) {
            this.mModule = module;
            this.mInfo = info;
            this.mEventTick = tick;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void watchdogHandleMessage(Message msg) {
        int i = msg.what;
        if (i == 1) {
            BinderRecord record = (BinderRecord) msg.obj;
            if (this.mBinderRecordMap.size() < 32) {
                this.mBinderRecordMap.put(record.mModule + record.mInfo, record);
                Message checkMsg = this.mWatchdogHandler.obtainMessage(3, record);
                this.mWatchdogHandler.sendMessageDelayed(checkMsg, 2000L);
                return;
            }
            LogUtil.w(TAG, "binder enter, record map over 32");
            this.mBinderRecordMap.clear();
        } else if (i != 2) {
            if (i != 3) {
                if (i == 4) {
                    ThreadUtils.postOnBackgroundThread(this.syncBarrierCheckRunner);
                    return;
                }
                return;
            }
            BinderRecord start = (BinderRecord) msg.obj;
            long curTick = SystemClock.elapsedRealtime();
            long delta = curTick - start.mEventTick;
            LogUtil.w(TAG, "binder call check,module:" + start.mModule + ",info:" + start.mInfo + ",deltaMs=" + delta + ",total alive:" + this.mBinderRecordMap.size());
            long dumpDelta = curTick - mLastDumpTick;
            if (dumpDelta >= DUMP_INTERVAL_MS) {
                mLastDumpTick = curTick;
                ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.xiaopeng.xuiservice.debug.-$$Lambda$WatchDog$gsgpjKVysdrPcK2tqfmbnVfDJi8
                    @Override // java.lang.Runnable
                    public final void run() {
                        WatchDog.this.lambda$watchdogHandleMessage$0$WatchDog();
                    }
                });
            }
        } else {
            BinderRecord leave = (BinderRecord) msg.obj;
            String binderKey = leave.mModule + leave.mInfo;
            BinderRecord start2 = this.mBinderRecordMap.get(binderKey);
            if (start2 != null) {
                this.mWatchdogHandler.removeMessages(3, start2);
                long deltaMs = leave.mEventTick - start2.mEventTick;
                if (deltaMs > 1000) {
                    LogUtil.w(TAG, "binder leave,m:" + leave.mModule + ",info:" + leave.mInfo + ",deltaMs=" + deltaMs + ",total alive:" + this.mBinderRecordMap.size());
                }
                this.mBinderRecordMap.remove(binderKey);
                return;
            }
            LogUtil.w(TAG, "binder leave,record not found:" + leave.mModule + ",info:" + leave.mInfo);
        }
    }

    public /* synthetic */ void lambda$watchdogHandleMessage$0$WatchDog() {
        int traceCount = SystemProperties.getInt(TRACE_COUNT_PROP, 0);
        LogUtil.i(TAG, "try to dump stack,count=" + traceCount + ",max=50");
        if (traceCount < 50) {
            if (traceCount == 0) {
                dirMake();
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String curDate = format.format(Long.valueOf(System.currentTimeMillis()));
            int myPid = Process.myPid();
            Debug.dumpJavaBacktraceToFileTimeout(myPid, TRACE_FILE_PATH + curDate, 10);
            SystemProperties.set(TRACE_COUNT_PROP, String.valueOf(traceCount + 1));
        }
    }

    private WatchDog() {
        this.mWatchdogTh = null;
        this.mWatchdogHandler = null;
        this.mBinderRecordMap = new HashMap<>();
        this.mainQueue = Looper.getMainLooper().getQueue();
        this.barrierCount = 0;
        this.mainHandler = new Handler(Looper.getMainLooper()) { // from class: com.xiaopeng.xuiservice.debug.WatchDog.2
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                LogUtil.d(WatchDog.TAG, "mainHandler got msg:" + msg.what);
                int i = msg.what;
                if (i == 1) {
                    WatchDog.access$308(WatchDog.this);
                } else if (i == 2) {
                    WatchDog.this.barrierCount = 0;
                } else if (i == 3) {
                    WatchDog.this.idleHandlerListReplace();
                } else if (i == 4) {
                    try {
                        Thread.sleep(2000L);
                    } catch (Exception e) {
                    }
                }
            }
        };
        this.syncBarrierCheckRunner = new Runnable() { // from class: com.xiaopeng.xuiservice.debug.-$$Lambda$WatchDog$dPkwO4SJ39e8kXpnNKC_uSypikU
            @Override // java.lang.Runnable
            public final void run() {
                WatchDog.this.lambda$new$4$WatchDog();
            }
        };
        this.looperLogTick = 0L;
        this.mainLooperPrinter = new Printer() { // from class: com.xiaopeng.xuiservice.debug.-$$Lambda$WatchDog$83RkizuXYtAI1nV3-5-t1ysCdx0
            @Override // android.util.Printer
            public final void println(String str) {
                WatchDog.this.lambda$new$5$WatchDog(str);
            }
        };
        DumpDispatcher.registerDump("watchdog", new DumpDispatcher.DebugDumper() { // from class: com.xiaopeng.xuiservice.debug.-$$Lambda$WatchDog$rWtgH7B_2IddiVf2wVEVdR0WLfw
            @Override // com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
            public final void debugDump(PrintWriter printWriter, String[] strArr) {
                WatchDog.this.lambda$new$1$WatchDog(printWriter, strArr);
            }
        });
        this.mWatchdogTh = new HandlerThread(TAG);
        this.mWatchdogTh.start();
        this.mWatchdogHandler = new Handler(this.mWatchdogTh.getLooper()) { // from class: com.xiaopeng.xuiservice.debug.WatchDog.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                WatchDog.this.watchdogHandleMessage(msg);
            }
        };
        Looper.getMainLooper().setMessageLogging(this.mainLooperPrinter);
        try {
            this.mMessagesfield = this.mainQueue.getClass().getDeclaredField("mMessages");
            this.mMessagesfield.setAccessible(true);
            this.mainHandler.obtainMessage(3).sendToTarget();
        } catch (Exception e) {
            LogUtil.w(TAG, "init e=" + e);
        }
        Message checkMsg = this.mWatchdogHandler.obtainMessage(4);
        this.mWatchdogHandler.sendMessageDelayed(checkMsg, 3000L);
    }

    public static WatchDog getInstance() {
        return WatchDogHolder.instance;
    }

    public void binderEnter(String module, String info) {
        LogUtil.d(TAG, "binder enter,module=" + module + ",info:" + info);
        BinderRecord record = new BinderRecord(module, info, SystemClock.elapsedRealtime());
        this.mWatchdogHandler.obtainMessage(1, record).sendToTarget();
    }

    public void binderLeave(String module, String info) {
        LogUtil.d(TAG, "binder leave,module=" + module + ",info:" + info);
        BinderRecord record = new BinderRecord(module, info, SystemClock.elapsedRealtime());
        this.mWatchdogHandler.obtainMessage(2, record).sendToTarget();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* renamed from: dump */
    public void lambda$new$1$WatchDog(PrintWriter pw, String[] args) {
        boolean z;
        if (args == null || args.length < 1) {
            pw.println("please input args!");
            return;
        }
        for (String str : args) {
            switch (str.hashCode()) {
                case -1751344502:
                    if (str.equals("-barrierpost")) {
                        z = false;
                        break;
                    }
                    z = true;
                    break;
                case -407598262:
                    if (str.equals("-longmsgtest")) {
                        z = true;
                        break;
                    }
                    z = true;
                    break;
                case 348349169:
                    if (str.equals("-remove")) {
                        z = true;
                        break;
                    }
                    z = true;
                    break;
                case 395372552:
                    if (str.equals("-idlehandlerqueue")) {
                        z = true;
                        break;
                    }
                    z = true;
                    break;
                default:
                    z = true;
                    break;
            }
            if (!z) {
                postSyncBarrier();
                pw.println("postSyncBarrier done");
            } else if (z) {
                this.mainQueue.addIdleHandler(new MessageQueue.IdleHandler() { // from class: com.xiaopeng.xuiservice.debug.-$$Lambda$WatchDog$YzW_cyNl8g16KW96Ib0ULhv0qGo
                    @Override // android.os.MessageQueue.IdleHandler
                    public final boolean queueIdle() {
                        return WatchDog.lambda$dump$2();
                    }
                });
            } else if (z) {
                this.mainHandler.obtainMessage(4).sendToTarget();
            } else if (z) {
                UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.debug.-$$Lambda$WatchDog$Ov1cfZ72YEV5mHEcMEZqTP9_dVU
                    @Override // java.lang.Runnable
                    public final void run() {
                        WatchDog.this.lambda$dump$3$WatchDog();
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$dump$2() {
        try {
            Thread.sleep(1500L);
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public /* synthetic */ void lambda$dump$3$WatchDog() {
        removeSyncBarrier(9999);
    }

    private void dirMake() {
        File dir = new File(XUI_FILE_DIR);
        if (!dir.exists()) {
            try {
                dir.mkdirs();
            } catch (Exception e) {
                LogUtil.w(TAG, "mkdir fail");
            }
        }
    }

    public /* synthetic */ void lambda$new$5$WatchDog(String x) {
        if (x == null) {
            LogUtil.w(TAG, "mainLooperPrinter,x is null");
        } else if (x.startsWith(LOGGER_BEGIN)) {
            this.looperLogTick = SystemClock.elapsedRealtime();
        } else if (x.startsWith(LOGGER_END)) {
            if (0 != this.looperLogTick) {
                long deltaMs = SystemClock.elapsedRealtime() - this.looperLogTick;
                if (deltaMs > LOOPER_EXECUTE_THRESHOLD) {
                    LogUtil.w(TAG, "mainlooper done cost " + deltaMs + "ms,,msg:" + x);
                }
                this.looperLogTick = 0L;
            }
        } else {
            this.looperLogTick = 0L;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: syncBarrierCheck */
    public void lambda$new$4$WatchDog() {
        StringBuilder sb;
        Message mMessage = null;
        try {
            mMessage = (Message) this.mMessagesfield.get(this.mainQueue);
        } catch (Exception e) {
            LogUtil.w(TAG, "syncBarrierCheck e =" + e);
        }
        if (mMessage != null) {
            try {
                long deltaWhen = SystemClock.uptimeMillis() - mMessage.getWhen();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("cur msg what=");
                sb2.append(mMessage.what);
                sb2.append(",callback=");
                sb2.append(mMessage.getCallback());
                sb2.append(",target=");
                sb2.append(mMessage.getTarget());
                sb2.append(",when=");
                sb2.append(mMessage.getWhen());
                if (mMessage.getTarget() == null) {
                    sb = new StringBuilder();
                    sb.append(",barrier token=");
                    sb.append(mMessage.arg1);
                    sb.append(",delta=");
                    sb.append(deltaWhen);
                } else {
                    sb = new StringBuilder();
                    sb.append(",delta=");
                    sb.append(deltaWhen);
                }
                sb2.append(sb.toString());
                LogUtil.d(TAG, sb2.toString());
                if (0 != mMessage.getWhen() && deltaWhen > 3000 && mMessage.getTarget() == null) {
                    int token = mMessage.arg1;
                    startBarrierLeakCheck(token);
                }
            } catch (Exception e2) {
                LogUtil.w(TAG, "syncBarrierCheck e2=" + e2);
            }
        }
        Message checkMsg = this.mWatchdogHandler.obtainMessage(4);
        this.mWatchdogHandler.sendMessageDelayed(checkMsg, 1500L);
    }

    private int getSyncBarrierToken() {
        Message mMessage = null;
        try {
            mMessage = (Message) this.mMessagesfield.get(this.mainQueue);
        } catch (Exception e) {
            LogUtil.w(TAG, "syncBarrierCheck e =" + e);
        }
        if (mMessage != null && mMessage.getTarget() == null) {
            return mMessage.arg1;
        }
        return -1;
    }

    private void startBarrierLeakCheck(int token) {
        int checkCount = 0;
        this.barrierCount = 0;
        while (checkCount < 3) {
            checkCount++;
            int latestToken = getSyncBarrierToken();
            if (token == latestToken) {
                if (detectSyncBarrier()) {
                    LogUtil.w(TAG, "barrier leak,token=" + token);
                    removeSyncBarrier(token);
                    return;
                }
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                return;
            }
        }
    }

    private void removeSyncBarrier(int token) {
        this.mainQueue.removeSyncBarrier(token);
    }

    private boolean detectSyncBarrier() {
        Message asyncMessage = Message.obtain();
        asyncMessage.setAsynchronous(true);
        asyncMessage.what = 1;
        Message syncNormalMessage = Message.obtain();
        syncNormalMessage.what = 2;
        this.mainHandler.sendMessage(asyncMessage);
        this.mainHandler.sendMessage(syncNormalMessage);
        try {
            Thread.sleep(20L);
        } catch (Exception e) {
        }
        if (this.barrierCount >= 3) {
            return true;
        }
        return false;
    }

    private int postSyncBarrier() {
        try {
            Method method = this.mainQueue.getClass().getDeclaredMethod("postSyncBarrier", new Class[0]);
            method.setAccessible(true);
            int token = ((Integer) method.invoke(this.mainQueue, new Object[0])).intValue();
            return token;
        } catch (Exception e) {
            LogUtil.w(TAG, "postSyncBarrier e=" + e);
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void idleHandlerListReplace() {
        try {
            Field field = MessageQueue.class.getDeclaredField("mIdleHandlers");
            field.setAccessible(true);
            ArrayList<MessageQueue.IdleHandler> list = (ArrayList) field.get(this.mainQueue);
            if (list != null && !list.isEmpty()) {
                Message msg = this.mainHandler.obtainMessage(3);
                msg.setAsynchronous(true);
                this.mainHandler.sendMessageDelayed(msg, 1000L);
            }
            field.set(this.mainQueue, idleHandlerArrayList);
        } catch (Exception e) {
            LogUtil.w(TAG, "idleHandlerListReplace,e=" + e);
        }
    }

    /* loaded from: classes5.dex */
    private static class MyIdleHandler implements MessageQueue.IdleHandler {
        public MessageQueue.IdleHandler mIdleHandler;

        public MyIdleHandler(MessageQueue.IdleHandler handler) {
            this.mIdleHandler = handler;
        }

        @Override // android.os.MessageQueue.IdleHandler
        public boolean queueIdle() {
            LogUtil.d(WatchDog.TAG, "queueIdle,handler=" + this.mIdleHandler);
            if (this.mIdleHandler != null) {
                long tick = SystemClock.elapsedRealtime();
                boolean ret = this.mIdleHandler.queueIdle();
                long delta = SystemClock.elapsedRealtime() - tick;
                if (delta > WatchDog.LOOPER_EXECUTE_THRESHOLD) {
                    LogUtil.w(WatchDog.TAG, "queueIdle cost " + delta + "ms,handler=" + this.mIdleHandler);
                }
                return ret;
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class IdleHandlerArrayList<T> extends ArrayList {
        private IdleHandlerArrayList() {
        }

        @Override // java.util.ArrayList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean add(Object o) {
            LogUtil.d(WatchDog.TAG, "IdleHandlerArrayList, add:" + o + ",size=" + WatchDog.idleHandlerArrayList.size());
            if (o instanceof MessageQueue.IdleHandler) {
                MyIdleHandler myIdleHandler = new MyIdleHandler((MessageQueue.IdleHandler) o);
                return super.add(myIdleHandler);
            }
            return super.add(o);
        }

        @Override // java.util.ArrayList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean remove(Object o) {
            LogUtil.d(WatchDog.TAG, "IdleHandlerArrayList, remove:" + o + ",size=" + WatchDog.idleHandlerArrayList.size());
            if (o instanceof MyIdleHandler) {
                ((MyIdleHandler) o).mIdleHandler = null;
            }
            return super.remove(o);
        }
    }
}
