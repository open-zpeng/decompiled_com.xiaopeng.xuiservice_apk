package com.xiaopeng.xuiservice.soundresource;

import android.app.ActivityThread;
import android.content.Context;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import com.xiaopeng.xuimanager.soundresource.ISoundResource;
import com.xiaopeng.xuimanager.soundresource.ISoundResourceListener;
import com.xiaopeng.xuimanager.soundresource.data.BootSoundResource;
import com.xiaopeng.xuimanager.soundresource.data.SoundEffectResource;
import com.xiaopeng.xuimanager.soundresource.data.SoundEffectTheme;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIServiceBase;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import com.xiaopeng.xuiservice.debug.WatchDog;
import com.xiaopeng.xuiservice.soundresource.SoundResourceLoader;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes5.dex */
public class SoundResourceService extends ISoundResource.Stub implements XUIServiceBase, SoundResourceLoader.ResourceListener {
    private static final int MSG_SOUND_RESOURCE_ACTIVE_CHANGE = 1;
    private static final String TAG = SoundResourceService.class.getSimpleName();
    private final Context mContext;
    private EventHandler mEventHandler;
    private final Map<IBinder, ListenerRecord> mListenerMap;
    private SoundResourceLoader mSoundResourceLoader;

    @Override // com.xiaopeng.xuiservice.soundresource.SoundResourceLoader.ResourceListener
    public void onResourceEvent(int resId, int event) {
        this.mEventHandler.obtainMessage(1, resId, event).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public final class ListenerRecord implements IBinder.DeathRecipient {
        private final String TAG = SoundResourceService.class.getSimpleName();
        public final ISoundResourceListener listener;
        public final int pid;
        public final int uid;

        ListenerRecord(ISoundResourceListener listener, int pid, int uid) {
            this.listener = listener;
            this.pid = pid;
            this.uid = uid;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            String str = this.TAG;
            LogUtil.w(str, "binder died,pid=" + this.pid);
            release();
            synchronized (SoundResourceService.this.mListenerMap) {
                SoundResourceService.this.mListenerMap.remove(this.listener.asBinder());
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

    /* loaded from: classes5.dex */
    private final class EventHandler extends Handler {
        public EventHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                SoundResourceService.this.dispatchResourceEvent(msg.arg1, msg.arg2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class SoundResourceServiceHolder {
        private static final SoundResourceService instance = new SoundResourceService();

        private SoundResourceServiceHolder() {
        }
    }

    private SoundResourceService() {
        this.mContext = ActivityThread.currentActivityThread().getApplication();
        this.mListenerMap = new HashMap();
        this.mSoundResourceLoader = SoundResourceLoader.getInstance();
        this.mEventHandler = null;
        this.mEventHandler = new EventHandler(XuiWorkHandler.getInstance().getLooper());
    }

    public static SoundResourceService getInstance() {
        return SoundResourceServiceHolder.instance;
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        String info = "code=" + code + ",pid=" + Binder.getCallingPid() + "@" + SystemClock.elapsedRealtimeNanos();
        WatchDog.getInstance().binderEnter("SndResourceSvc", info);
        try {
            try {
                boolean ret = super.onTransact(code, data, reply, flags);
                return ret;
            } catch (RemoteException e) {
                LogUtil.w(TAG, "onTransact e=" + e + ",code=" + code);
                throw e;
            }
        } finally {
            WatchDog.getInstance().binderLeave("SndResourceSvc", info);
        }
    }

    public SoundEffectTheme[] getAvailableThemes() throws RemoteException {
        String str = TAG;
        LogUtil.i(str, "getAvailableThemes,pid=" + Binder.getCallingPid());
        SoundEffectTheme[] themes = this.mSoundResourceLoader.getAvailableThemes();
        return themes;
    }

    public int getActiveSoundEffectTheme() throws RemoteException {
        String str = TAG;
        LogUtil.i(str, "getActiveSoundEffectTheme,pid=" + Binder.getCallingPid());
        int index = this.mSoundResourceLoader.getActiveSoundThemeIndex();
        return index;
    }

    public int selectSoundEffectTheme(int themeId) throws RemoteException {
        String str = TAG;
        LogUtil.i(str, "selectSoundEffectTheme,id=" + themeId + ",pid=" + Binder.getCallingPid());
        int ret = this.mSoundResourceLoader.setActiveSoundThemIndex(themeId);
        if (ret == 0) {
            this.mEventHandler.obtainMessage(1, themeId, 1).sendToTarget();
        }
        return ret;
    }

    public SoundEffectResource[] getSoundEffectPlayResource(int themeId) throws RemoteException {
        String str = TAG;
        LogUtil.i(str, "getSoundEffectPlayResource,id=" + themeId + ",pid=" + Binder.getCallingPid());
        SoundEffectResource[] resources = this.mSoundResourceLoader.getSoundEffectPlayResource(themeId);
        return resources;
    }

    public SoundEffectResource[] getSoundEffectPreviewResource(int themeId) throws RemoteException {
        String str = TAG;
        LogUtil.i(str, "getSoundEffectPreviewResource,id=" + themeId + ",pid=" + Binder.getCallingPid());
        SoundEffectResource[] resources = this.mSoundResourceLoader.getSoundEffectPreviewResource(themeId);
        return resources;
    }

    public SoundEffectResource getActiveSoundEffectResource(int effectType) throws RemoteException {
        String str = TAG;
        LogUtil.i(str, "getActiveSoundEffectResource,type=" + effectType + ",pid=" + Binder.getCallingPid());
        SoundEffectResource resource = this.mSoundResourceLoader.getActiveSoundEffectResource(effectType);
        return resource;
    }

    public int setBootSoundOnOff(boolean flag) throws RemoteException {
        String str = TAG;
        LogUtil.i(str, "setBootSoundOnOff,flag=" + flag + ",pid=" + Binder.getCallingPid());
        int ret = this.mSoundResourceLoader.setBootSoundOnOff(flag);
        return ret;
    }

    public int getBootSoundOnOff() throws RemoteException {
        String str = TAG;
        LogUtil.i(str, "getBootSoundOnOff,pid=" + Binder.getCallingPid());
        int ret = this.mSoundResourceLoader.getBootSoundOnOff();
        return ret;
    }

    public BootSoundResource[] getBootSoundResource() throws RemoteException {
        LogUtil.i(TAG, "getBootSoundResource");
        BootSoundResource[] resources = this.mSoundResourceLoader.getBootSoundResource();
        return resources;
    }

    public int setBootSoundResource(int resourceId) throws RemoteException {
        String str = TAG;
        LogUtil.i(str, "setBootSoundResource,id=" + resourceId + ",pid=" + Binder.getCallingPid());
        int ret = this.mSoundResourceLoader.setBootSoundResource(resourceId);
        return ret;
    }

    public BootSoundResource getActiveBootSoundResource() throws RemoteException {
        String str = TAG;
        LogUtil.i(str, "getActiveBootSoundResource,pid=" + Binder.getCallingPid());
        BootSoundResource resource = this.mSoundResourceLoader.getActiveBootSoundResource();
        return resource;
    }

    public void registerListener(ISoundResourceListener listener) throws RemoteException {
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
                this.mListenerMap.put(binder, record);
            } catch (RemoteException e) {
                String str3 = TAG;
                LogUtil.w(str3, "registerListener e=" + e);
            }
        }
    }

    public void unRegisterListener(ISoundResourceListener listener) throws RemoteException {
        ListenerRecord record;
        if (listener == null) {
            LogUtil.w(TAG, "unRegisterListener null!");
            return;
        }
        IBinder binder = listener.asBinder();
        String str = TAG;
        LogUtil.i(str, "unRegisterListener,binder-" + binder + ",listeners-" + this.mListenerMap.size());
        synchronized (this.mListenerMap) {
            record = this.mListenerMap.remove(binder);
        }
        if (record == null) {
            String str2 = TAG;
            LogUtil.w(str2, "no registed record for binder-" + binder);
            return;
        }
        record.release();
    }

    @Override // com.xiaopeng.xuiservice.XUIServiceBase
    public void init() {
        LogUtil.i(TAG, "init");
        DumpDispatcher.registerDump("soundresource", new DumpDispatcher.DebugDumper() { // from class: com.xiaopeng.xuiservice.soundresource.-$$Lambda$SoundResourceService$Ax6TjYYEvko9ial5s0kM_qL8MyA
            @Override // com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
            public final void debugDump(PrintWriter printWriter, String[] strArr) {
                SoundResourceService.this.lambda$init$0$SoundResourceService(printWriter, strArr);
            }
        });
        this.mSoundResourceLoader.setResourceListener(this);
        SoundResourceOperation.getInstance().init();
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.soundresource.-$$Lambda$SoundResourceService$wyII6UXmmhOWy3D-NLov2H9z-fc
            @Override // java.lang.Runnable
            public final void run() {
                SoundResourceService.this.lambda$init$1$SoundResourceService();
            }
        });
    }

    public /* synthetic */ void lambda$init$1$SoundResourceService() {
        this.mSoundResourceLoader.loadRsc();
        String str = TAG;
        LogUtil.d(str, "init done,load theme size:" + this.mSoundResourceLoader.getThemeMap().size());
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
    public void lambda$init$0$SoundResourceService(PrintWriter pw, String[] args) {
        boolean z;
        pw.println("dump:" + TAG);
        if (args == null || args.length < 1) {
            pw.println("please input params");
            return;
        }
        for (int i = 0; i < args.length; i++) {
            String str = args[i];
            int hashCode = str.hashCode();
            char c = 65535;
            if (hashCode == -2012366331) {
                if (str.equals("-bootload")) {
                    z = true;
                }
                z = true;
            } else if (hashCode == -1474189211) {
                if (str.equals("-bootactive")) {
                    z = true;
                }
                z = true;
            } else if (hashCode != 1492) {
                if (hashCode == 1511 && str.equals("-t")) {
                    z = false;
                }
                z = true;
            } else {
                if (str.equals("-a")) {
                    z = true;
                }
                z = true;
            }
            if (!z) {
                if (i + 1 < args.length) {
                    String str2 = args[i + 1];
                    if (str2.hashCode() == -172220347 && str2.equals("callback")) {
                        c = 0;
                    }
                    if (c == 0) {
                        pw.println("callback test");
                        handleCallbackTest();
                        return;
                    }
                    pw.println("unknown param " + args[i + 1]);
                    return;
                }
                pw.println("please add test params");
                return;
            } else if (!z) {
                if (!z) {
                    if (z) {
                        if (i + 1 < args.length) {
                            String activeIdx = args[i + 1];
                            this.mSoundResourceLoader.setBootSoundResource(Integer.parseInt(activeIdx));
                        } else {
                            pw.println("boot active but lack of flag!");
                        }
                    } else {
                        return;
                    }
                } else if (i + 1 < args.length) {
                    String path = args[i + 1];
                    List<BootSoundResource> resourceList = SoundResourceLoader.loadBootRscFromDisk(path);
                    int count = resourceList != null ? resourceList.size() : 0;
                    pw.println("load count:" + count + ",resources:");
                    if (count > 0) {
                        for (BootSoundResource res : resourceList) {
                            pw.println("  " + res);
                        }
                    }
                } else {
                    pw.println("load boot sound resource but lack of load path!");
                }
            } else {
                SoundEffectTheme[] themes = this.mSoundResourceLoader.getAvailableThemes();
                if (themes != null) {
                    SoundEffectTheme activeTheme = this.mSoundResourceLoader.getActiveSoundTheme();
                    pw.println("sound theme active=" + activeTheme);
                    pw.println("sound theme count=" + themes.length + ",data:");
                    int length = themes.length;
                    for (int i2 = 0; i2 < length; i2++) {
                        SoundEffectTheme theme = themes[i2];
                        pw.println("  " + theme);
                    }
                } else {
                    pw.println("no sound themes");
                }
                BootSoundResource[] resources = this.mSoundResourceLoader.getBootSoundResource();
                if (resources != null) {
                    BootSoundResource activeBoot = this.mSoundResourceLoader.getActiveBootSoundResource();
                    pw.println("boot sound active=" + activeBoot);
                    pw.println("boot sound last active id:" + this.mSoundResourceLoader.getLastBootSoundIdx());
                    pw.println("boot sound count=" + resources.length + ",data:");
                    int length2 = resources.length;
                    while (r8 < length2) {
                        BootSoundResource res2 = resources[r8];
                        pw.println("  " + res2);
                        r8++;
                    }
                    return;
                }
                pw.println("no boot sound resource");
                return;
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
        int i = 1;
        for (ListenerRecord record : this.mListenerMap.values()) {
            try {
                record.listener.onResourceEvent(i, i);
            } catch (Exception e) {
                String str2 = TAG;
                LogUtil.w(str2, "handleCallbackTest e=" + e);
            }
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchResourceEvent(int resId, int event) {
        if (this.mListenerMap.isEmpty()) {
            String str = TAG;
            LogUtil.w(str, "dispatchResourceEvent,no listeners, resId:" + resId + ",event:" + event);
            return;
        }
        synchronized (this.mListenerMap) {
            for (ListenerRecord record : this.mListenerMap.values()) {
                try {
                    record.listener.onResourceEvent(resId, event);
                } catch (Exception e) {
                    String str2 = TAG;
                    LogUtil.w(str2, "handleCallbackTest e=" + e);
                }
            }
        }
    }
}
