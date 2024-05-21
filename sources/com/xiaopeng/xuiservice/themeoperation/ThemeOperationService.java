package com.xiaopeng.xuiservice.themeoperation;

import android.app.ActivityThread;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.ArrayMap;
import com.xiaopeng.libtheme.ThemeManager;
import com.xiaopeng.xuimanager.themeoperation.AbilityEffect;
import com.xiaopeng.xuimanager.themeoperation.IThemeOperation;
import com.xiaopeng.xuimanager.themeoperation.IThemeOperationListener;
import com.xiaopeng.xuimanager.themeoperation.ThemeOperationData;
import com.xiaopeng.xuimanager.themeoperation.ThemeOperationListener;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIServiceBase;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import com.xiaopeng.xuiservice.debug.WatchDog;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
/* loaded from: classes5.dex */
public class ThemeOperationService extends IThemeOperation.Stub implements XUIServiceBase, ThemeOperationListener {
    private static final String TAG = ThemeOperationService.class.getSimpleName();
    private final Set<ThemeOperationListener> innerListeners;
    private final Context mContext;
    private final Map<IBinder, ListenerRecord> mListenerMap;
    private final ThemeResManager mThemeResManager;

    /* loaded from: classes5.dex */
    private static class ThemeOperationServiceHolder {
        private static final ThemeOperationService instacne = new ThemeOperationService();

        private ThemeOperationServiceHolder() {
        }
    }

    public static ThemeOperationService getInstance() {
        return ThemeOperationServiceHolder.instacne;
    }

    public void addListener(ThemeOperationListener listener) {
        String str = TAG;
        LogUtil.i(str, "addListener:" + listener);
        this.innerListeners.add(listener);
    }

    public void removeListener(ThemeOperationListener listener) {
        String str = TAG;
        LogUtil.i(str, "removeListener:" + listener);
        this.innerListeners.remove(listener);
    }

    public void onThemeStatus(final String event, final String extra, final List<ThemeOperationData> themeList) {
        if (!this.innerListeners.isEmpty()) {
            XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.themeoperation.-$$Lambda$ThemeOperationService$wS-yYD_RBpbwGx9pGvvGsPzNMRk
                @Override // java.lang.Runnable
                public final void run() {
                    ThemeOperationService.this.lambda$onThemeStatus$0$ThemeOperationService(event, extra, themeList);
                }
            });
        } else {
            LogUtil.i(TAG, "onThemeStatus,no inner listeners");
        }
        if (this.mListenerMap.isEmpty()) {
            LogUtil.d(TAG, "onThemeStatus, no listeners");
        } else {
            XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.themeoperation.-$$Lambda$ThemeOperationService$3V0F05wCfDEuCDJTmEPbYl5MNgQ
                @Override // java.lang.Runnable
                public final void run() {
                    ThemeOperationService.this.lambda$onThemeStatus$1$ThemeOperationService(event, extra, themeList);
                }
            });
        }
    }

    public /* synthetic */ void lambda$onThemeStatus$0$ThemeOperationService(String event, String extra, List themeList) {
        for (ThemeOperationListener listener : this.innerListeners) {
            listener.onThemeStatus(event, extra, themeList);
        }
    }

    public /* synthetic */ void lambda$onThemeStatus$1$ThemeOperationService(String event, String extra, List themeList) {
        for (ListenerRecord record : this.mListenerMap.values()) {
            try {
                record.listener.onThemeStatus(event, extra, themeList);
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "onThemeStatus e=" + e);
            }
        }
    }

    public void onEffectStatus(final String event, final String extra, final List<AbilityEffect> effectList) {
        if (!this.innerListeners.isEmpty()) {
            XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.themeoperation.-$$Lambda$ThemeOperationService$59a5kCUpR2fsRAjjh0j_rOoyS_o
                @Override // java.lang.Runnable
                public final void run() {
                    ThemeOperationService.this.lambda$onEffectStatus$2$ThemeOperationService(event, extra, effectList);
                }
            });
        } else {
            LogUtil.i(TAG, "onEffectStatus,no inner listeners");
        }
        if (this.mListenerMap.isEmpty()) {
            LogUtil.d(TAG, "onEffectStatus, no listeners");
        } else {
            XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.themeoperation.-$$Lambda$ThemeOperationService$FSqis4eREPL-3B-ORp66Wj1cw5k
                @Override // java.lang.Runnable
                public final void run() {
                    ThemeOperationService.this.lambda$onEffectStatus$3$ThemeOperationService(event, extra, effectList);
                }
            });
        }
    }

    public /* synthetic */ void lambda$onEffectStatus$2$ThemeOperationService(String event, String extra, List effectList) {
        for (ThemeOperationListener listener : this.innerListeners) {
            listener.onEffectStatus(event, extra, effectList);
        }
    }

    public /* synthetic */ void lambda$onEffectStatus$3$ThemeOperationService(String event, String extra, List effectList) {
        for (ListenerRecord record : this.mListenerMap.values()) {
            try {
                record.listener.onEffectStatus(event, extra, effectList);
            } catch (Exception e) {
                String str = TAG;
                LogUtil.w(str, "handleCallbackTest e=" + e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public final class ListenerRecord implements IBinder.DeathRecipient {
        private final String TAG = ThemeOperationService.class.getSimpleName();
        public final IThemeOperationListener listener;
        public final int pid;
        public final int uid;

        ListenerRecord(IThemeOperationListener listener, int pid, int uid) {
            this.listener = listener;
            this.pid = pid;
            this.uid = uid;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            String str = this.TAG;
            LogUtil.w(str, "binder died,pid=" + this.pid);
            release();
            synchronized (ThemeOperationService.this.mListenerMap) {
                ThemeOperationService.this.mListenerMap.remove(this.listener.asBinder());
            }
        }

        public void release() {
            try {
                this.listener.asBinder().unlinkToDeath(this, 0);
            } catch (Exception e) {
                String str = this.TAG;
                LogUtil.w(str, "ListenerRecord release e=" + e);
            }
        }
    }

    private ThemeOperationService() {
        this.mThemeResManager = ThemeResManager.getInstance();
        this.mListenerMap = new ConcurrentHashMap();
        this.innerListeners = new CopyOnWriteArraySet();
        this.mContext = ActivityThread.currentActivityThread().getApplication();
        DumpDispatcher.registerDump(ThemeManager.AttributeSet.THEME, new DumpDispatcher.DebugDumper() { // from class: com.xiaopeng.xuiservice.themeoperation.-$$Lambda$ThemeOperationService$7R9Xzi32ZsTc2eSo9QvpG6gj3rk
            @Override // com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
            public final void debugDump(PrintWriter printWriter, String[] strArr) {
                ThemeOperationService.this.lambda$new$4$ThemeOperationService(printWriter, strArr);
            }
        });
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        String info = "code=" + code + ",pid=" + Binder.getCallingPid() + "@" + SystemClock.elapsedRealtimeNanos();
        WatchDog.getInstance().binderEnter("ThemeOperationSvc", info);
        try {
            try {
                boolean ret = super.onTransact(code, data, reply, flags);
                return ret;
            } catch (RemoteException e) {
                LogUtil.w(TAG, "onTransact e=" + e + ",code=" + code);
                throw e;
            }
        } finally {
            WatchDog.getInstance().binderLeave("ThemeOperationSvc", info);
        }
    }

    public ThemeOperationData[] getThemes() throws RemoteException {
        String str = TAG;
        LogUtil.i(str, "getThemes enter,caller=" + Binder.getCallingPid());
        ThemeOperationData[] data = this.mThemeResManager.getThemes();
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("get theme size=");
        sb.append(data != null ? data.length : 0);
        LogUtil.d(str2, sb.toString());
        return data;
    }

    public ThemeOperationData getCurrentTheme() throws RemoteException {
        String str = TAG;
        LogUtil.i(str, "getCurrentTheme enter,caller=" + Binder.getCallingPid());
        ThemeOperationData data = this.mThemeResManager.getCurrentTheme();
        return data;
    }

    public boolean selectTheme(ThemeOperationData themeData) throws RemoteException {
        String str = TAG;
        LogUtil.i(str, "selectTheme:" + themeData);
        boolean ret = this.mThemeResManager.selectTheme(themeData);
        return ret;
    }

    public boolean resetTheme(ThemeOperationData themeData) throws RemoteException {
        String str = TAG;
        LogUtil.i(str, "resetTheme:" + themeData);
        boolean ret = this.mThemeResManager.resetTheme(themeData);
        return ret;
    }

    public boolean updateTheme(ThemeOperationData themeData) throws RemoteException {
        String str = TAG;
        LogUtil.i(str, "updateTheme:" + themeData);
        boolean ret = this.mThemeResManager.updateTheme(themeData);
        return ret;
    }

    public boolean deleteTheme(ThemeOperationData themeData) throws RemoteException {
        String str = TAG;
        LogUtil.i(str, "deleteTheme:" + themeData);
        boolean ret = this.mThemeResManager.deleteTheme(themeData);
        return ret;
    }

    public boolean selectEffect(AbilityEffect effect) throws RemoteException {
        String str = TAG;
        LogUtil.i(str, "selectEffect:" + effect);
        boolean ret = this.mThemeResManager.selectEffect(effect);
        return ret;
    }

    public void registerListener(IThemeOperationListener listener) throws RemoteException {
        if (listener == null) {
            LogUtil.w(TAG, "registerListener null!");
            return;
        }
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        IBinder binder = listener.asBinder();
        String str = TAG;
        LogUtil.i(str, "registerListener,binder-" + binder + ",pid-" + pid + ",uid" + uid + ",listeners-" + this.mListenerMap.size());
        synchronized (this.mListenerMap) {
            if (this.mListenerMap.containsKey(listener.asBinder())) {
                String str2 = TAG;
                LogUtil.w(str2, "repeat listener-" + binder);
                return;
            }
            ListenerRecord record = new ListenerRecord(listener, pid, uid);
            try {
                binder.linkToDeath(record, 0);
                synchronized (this.mListenerMap) {
                    this.mListenerMap.put(binder, record);
                }
            } catch (RemoteException e) {
                String str3 = TAG;
                LogUtil.w(str3, "registerListener e=" + e);
            }
        }
    }

    public void unRegisterListener(IThemeOperationListener listener) throws RemoteException {
        if (listener == null) {
            LogUtil.w(TAG, "unregisterListener null!");
            return;
        }
        IBinder binder = listener.asBinder();
        String str = TAG;
        LogUtil.i(str, "unregisterListener,binder-" + binder + ",listeners-" + this.mListenerMap.size());
        ListenerRecord record = this.mListenerMap.get(binder);
        if (record == null) {
            String str2 = TAG;
            LogUtil.w(str2, "no registed record for binder-" + binder);
            return;
        }
        record.release();
        synchronized (this.mListenerMap) {
            this.mListenerMap.remove(binder);
        }
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void init() {
        LogUtil.i(TAG, "init");
        this.mThemeResManager.setThemeListener(this);
        this.mThemeResManager.init();
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
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void dump(PrintWriter writer) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: handleDump */
    public void lambda$new$4$ThemeOperationService(PrintWriter pw, String[] args) {
        boolean z;
        pw.println("dump:" + TAG);
        if (args == null || args.length < 1) {
            pw.println("please input params");
        } else if (0 < args.length) {
            String str = args[0];
            int hashCode = str.hashCode();
            boolean z2 = false;
            if (hashCode != 1511) {
                if (hashCode == 45099009 && str.equals("-stat")) {
                    z = true;
                }
                z = true;
            } else {
                if (str.equals("-t")) {
                    z = false;
                }
                z = true;
            }
            if (z) {
                if (z) {
                    dumpStatus(pw);
                }
            } else if (0 + 1 < args.length) {
                String str2 = args[0 + 1];
                if (str2.hashCode() != -172220347 || !str2.equals("callback")) {
                    z2 = true;
                }
                if (!z2) {
                    pw.println("callback test");
                    handleCallbackTest();
                    return;
                }
                pw.println("unknown param " + args[0 + 1]);
            } else {
                pw.println("please add test params: -stat");
            }
        }
    }

    private void handleCallbackTest() {
        String str = TAG;
        LogUtil.i(str, "handleCallbackTest,registered counts=" + this.mListenerMap.size());
        if (this.mListenerMap.isEmpty()) {
            LogUtil.w(TAG, "handleCallbackTest,no listeners");
            return;
        }
        ThemeOperationData[] data = {new ThemeOperationData()};
        data[0].setActive(false);
        data[0].setFriendlyName("X战警");
        ArrayMap<String, AbilityEffect> map = new ArrayMap<>();
        AbilityEffect effect = new AbilityEffect();
        effect.setPropertyType(1);
        effect.setFriendlyName("绿野仙踪");
        effect.setEffectAbilityType("soundEffect");
        map.put(effect.getEffectAbilityType(), effect);
        data[0].setActiveEffectMap(map);
        ArrayList<ThemeOperationData> dataList = new ArrayList<>();
        dataList.add(data[0]);
        for (ListenerRecord record : this.mListenerMap.values()) {
            try {
                record.listener.onThemeStatus("theme_available", "test for extra", dataList);
                record.listener.onEffectStatus("effect_focus_change", "test for extra", (List) null);
            } catch (Exception e) {
                String str2 = TAG;
                LogUtil.w(str2, "handleCallbackTest e=" + e);
            }
        }
    }

    private void dumpStatus(PrintWriter pw) {
        ThemeOperationData[] dataArray = this.mThemeResManager.getThemes();
        int length = dataArray != null ? dataArray.length : 0;
        pw.println("theme data size:" + length + ",active theme:" + this.mThemeResManager.getAcitiveThemeData() + "\nAll data:");
        if (length < 1) {
            pw.println("  empty");
            return;
        }
        for (ThemeOperationData data : dataArray) {
            pw.println("  " + data);
        }
    }
}
