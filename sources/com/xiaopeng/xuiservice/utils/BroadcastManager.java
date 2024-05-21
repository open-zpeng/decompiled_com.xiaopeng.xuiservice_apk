package com.xiaopeng.xuiservice.utils;

import android.app.ActivityThread;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;
/* loaded from: classes5.dex */
public class BroadcastManager {
    public static final String ACTION_SCREEN_STATUS_CHANGE = "com.xiaopeng.broadcast.ACTION_SCREEN_STATUS_CHANGE";
    public static final String ACTION_VOICE_ACTIVE = "com.xiaopeng.speech.action.wakeup";
    public static final String ACTION_XKEY = "com.xiaopeng.intent.action.xkey";
    private List<Runnable> mBootTaskList;
    private List<String> mDynamicActions;
    private Set<ListenerRecord> mListenerRecord;
    private Object mLock;
    private List<Runnable> mLockedBootTaskList;
    private static final String TAG = BroadcastManager.class.getSimpleName();
    private static Context mContext = ActivityThread.currentActivityThread().getApplication();
    private static boolean mBootCompleted = false;
    private static UserManager mUserManager = (UserManager) ActivityThread.currentActivityThread().getApplication().getSystemService(UserManager.class);
    private static boolean unlocked = false;
    private static final String PROP_LOCKED_BOOT_COMPLETE = "sys.xiaopeng.lockedbootcomplete";
    private static boolean lockedBootCompleteGot = SystemProperties.getBoolean(PROP_LOCKED_BOOT_COMPLETE, false);
    private static final List<String> broadcastActions = new ArrayList<String>() { // from class: com.xiaopeng.xuiservice.utils.BroadcastManager.1
        {
            add("android.intent.action.USER_UNLOCKED");
            add("com.xiaopeng.xui.businessevent");
            add(BroadcastManager.ACTION_VOICE_ACTIVE);
            add(BroadcastManager.ACTION_SCREEN_STATUS_CHANGE);
            add(BroadcastManager.ACTION_XKEY);
            add("android.net.conn.CONNECTIVITY_CHANGE");
        }
    };

    /* loaded from: classes5.dex */
    public interface BroadcastListener {
        void onReceive(Context context, Intent intent);
    }

    /* loaded from: classes5.dex */
    private static class BroadcastManagerHolder {
        private static final BroadcastManager sInstance = new BroadcastManager();

        private BroadcastManagerHolder() {
        }
    }

    private BroadcastManager() {
        this.mListenerRecord = new CopyOnWriteArraySet();
        this.mDynamicActions = new ArrayList();
        this.mLock = new Object();
    }

    public static BroadcastManager getInstance() {
        return BroadcastManagerHolder.sInstance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class ListenerRecord {
        public List<String> mActionFilter;
        public final BroadcastListener mBroadcastListener;

        public ListenerRecord(BroadcastListener listener, List<String> filter) {
            this.mBroadcastListener = listener;
            this.mActionFilter = filter;
        }
    }

    public void registerListener(BroadcastListener listener, List<String> filter) {
        ListenerRecord record = new ListenerRecord(listener, filter);
        this.mListenerRecord.add(record);
    }

    public void registerListenerDynamically(BroadcastListener listener, List<String> filter) {
        ListenerRecord record = new ListenerRecord(listener, filter);
        this.mListenerRecord.add(record);
        List<String> actions = null;
        synchronized (this.mDynamicActions) {
            for (String action : filter) {
                if (!this.mDynamicActions.contains(action)) {
                    if (actions == null) {
                        actions = new ArrayList<>();
                    }
                    this.mDynamicActions.add(action);
                    actions.add(action);
                }
            }
        }
        if (actions != null) {
            registBroadcasts(actions);
        }
    }

    public void unRegisterListener(BroadcastListener listener) {
        ListenerRecord record = null;
        if (this.mListenerRecord.isEmpty()) {
            return;
        }
        Iterator<ListenerRecord> it = this.mListenerRecord.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ListenerRecord listenerRecord = it.next();
            if (listenerRecord.mBroadcastListener == listener) {
                record = listenerRecord;
                break;
            }
        }
        if (record != null) {
            this.mListenerRecord.remove(record);
        }
    }

    public void registerPackageListener(BroadcastListener listener, List<String> filter) {
        ListenerRecord record = new ListenerRecord(listener, filter);
        this.mListenerRecord.add(record);
        List<String> actions = null;
        synchronized (this.mDynamicActions) {
            for (String action : filter) {
                if (!this.mDynamicActions.contains(action)) {
                    if (actions == null) {
                        actions = new ArrayList<>();
                    }
                    this.mDynamicActions.add(action);
                    actions.add(action);
                }
            }
        }
        if (actions != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addDataScheme("package");
            for (String action2 : actions) {
                intentFilter.addAction(action2);
            }
            BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.xiaopeng.xuiservice.utils.BroadcastManager.2
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    BroadcastManager.this.handleBroadcast(context, intent);
                }
            };
            mContext.registerReceiver(mBroadcastReceiver, intentFilter);
        }
    }

    public void handleBroadcast(final Context context, final Intent intent) {
        ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.xiaopeng.xuiservice.utils.BroadcastManager.3
            @Override // java.lang.Runnable
            public void run() {
                BroadcastManager.this.dispatchBroadcast(context, intent);
            }
        });
    }

    public void addBootCompleteTask(Runnable runnable) {
        synchronized (this.mLock) {
            if (this.mBootTaskList == null) {
                this.mBootTaskList = new ArrayList();
            }
            this.mBootTaskList.add(runnable);
        }
    }

    public void addLockedBootCompleteTask(Runnable runnable) {
        synchronized (this.mLock) {
            if (this.mLockedBootTaskList == null) {
                this.mLockedBootTaskList = new ArrayList();
            }
            this.mLockedBootTaskList.add(runnable);
        }
    }

    public static boolean isBootComplete() {
        if (!unlocked) {
            unlocked = mUserManager.isUserUnlocked();
        }
        return unlocked;
    }

    public static boolean isLockedBootComplete() {
        return lockedBootCompleteGot || unlocked;
    }

    public boolean isUserUnlocked() {
        if (!unlocked) {
            unlocked = mUserManager.isUserUnlocked();
        }
        return unlocked;
    }

    public void init() {
        registBroadcasts(broadcastActions);
    }

    public void initNonMain() {
        registBootCompleteBroadcast();
    }

    public boolean sendBroadcast(final Intent intent) {
        intent.addFlags(20971552);
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.utils.-$$Lambda$BroadcastManager$H_poE-BJJq-qp3VshwpuMpCjhoM
            @Override // java.lang.Runnable
            public final void run() {
                BroadcastManager.lambda$sendBroadcast$0(intent);
            }
        });
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$sendBroadcast$0(Intent intent) {
        try {
            mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
        } catch (Exception e) {
            String str = TAG;
            LogUtil.e(str, "sendBroadcast e=" + e);
        }
    }

    public boolean sendBroadcast(final Intent intent, boolean runtimeOnly) {
        if (runtimeOnly) {
            intent.addFlags(1073741824);
        } else {
            intent.addFlags(20971520);
        }
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.utils.-$$Lambda$BroadcastManager$5f_AWOqhyrwEwJnNPqSPHwzirjE
            @Override // java.lang.Runnable
            public final void run() {
                BroadcastManager.lambda$sendBroadcast$1(intent);
            }
        });
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$sendBroadcast$1(Intent intent) {
        try {
            mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
        } catch (Exception e) {
            String str = TAG;
            LogUtil.e(str, "sendBroadcast e=" + e);
        }
    }

    private void registBootCompleteBroadcast() {
        LogUtil.d(TAG, "no main registBootCompleteBroadcast");
        IntentFilter intentFilter = new IntentFilter("android.intent.action.BOOT_COMPLETED");
        intentFilter.addAction("android.intent.action.LOCKED_BOOT_COMPLETED");
        BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.xiaopeng.xuiservice.utils.BroadcastManager.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String str = BroadcastManager.TAG;
                LogUtil.d(str, "mBootTaskList=" + BroadcastManager.this.mBootTaskList + ",mLockedBootTaskList=" + BroadcastManager.this.mLockedBootTaskList);
                BroadcastManager.this.handleBroadcast(context, intent);
            }
        };
        mContext.registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private void registBroadcasts(List<String> actionList) {
        IntentFilter intentFilter = new IntentFilter();
        for (String action : actionList) {
            intentFilter.addAction(action);
        }
        BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.xiaopeng.xuiservice.utils.BroadcastManager.5
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                BroadcastManager.this.handleBroadcast(context, intent);
            }
        };
        mContext.registerReceiver(mBroadcastReceiver, intentFilter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchBroadcast(final Context context, final Intent intent) {
        LogUtil.d(TAG, "dispatchBroadcast, intent=" + intent);
        if (intent == null) {
            LogUtil.w(TAG, "intent invalid");
            return;
        }
        final String action = intent.getAction();
        char c = 65535;
        int hashCode = action.hashCode();
        if (hashCode != -905063602) {
            if (hashCode != 798292259) {
                if (hashCode == 833559602 && action.equals("android.intent.action.USER_UNLOCKED")) {
                    c = 2;
                }
            } else if (action.equals("android.intent.action.BOOT_COMPLETED")) {
                c = 1;
            }
        } else if (action.equals("android.intent.action.LOCKED_BOOT_COMPLETED")) {
            c = 0;
        }
        if (c == 0) {
            lockedBootCompleteGot = true;
            SystemProperties.set(PROP_LOCKED_BOOT_COMPLETE, "1");
            if (this.mLockedBootTaskList != null) {
                synchronized (this.mLock) {
                    for (Runnable runnable : this.mLockedBootTaskList) {
                        runnable.run();
                    }
                }
            }
        } else if (c != 1) {
            if (c == 2) {
                unlocked = true;
            }
        } else if (this.mBootTaskList != null) {
            synchronized (this.mLock) {
                for (Runnable runnable2 : this.mBootTaskList) {
                    runnable2.run();
                }
            }
        }
        if (!this.mListenerRecord.isEmpty()) {
            this.mListenerRecord.forEach(new Consumer() { // from class: com.xiaopeng.xuiservice.utils.-$$Lambda$BroadcastManager$vo7EaYJ-QZ2ahoO-WGffBRxqCDg
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    BroadcastManager.lambda$dispatchBroadcast$2(action, context, intent, (BroadcastManager.ListenerRecord) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$dispatchBroadcast$2(String action, Context context, Intent intent, ListenerRecord record) {
        for (String dest : record.mActionFilter) {
            if (dest.equals(action)) {
                record.mBroadcastListener.onReceive(context, intent);
                return;
            }
        }
    }
}
