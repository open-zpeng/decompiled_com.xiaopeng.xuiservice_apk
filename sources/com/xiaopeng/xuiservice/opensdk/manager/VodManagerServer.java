package com.xiaopeng.xuiservice.opensdk.manager;

import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import com.car.opensdk.pipebus.IPipeBus;
import com.car.opensdk.pipebus.IPipeBusListener;
import com.car.opensdk.pipebus.ParcelableData;
import com.xiaopeng.speech.speechwidget.SpeechWidget;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: classes5.dex */
public class VodManagerServer extends IPipeBus.Stub {
    private static final String MODULE_NAME = "vodManager";
    private static final String TAG = VodManagerServer.class.getSimpleName();
    private static HashMap<String, Method> mFuncMap = new HashMap<>();
    private final ArrayList<PipeListenerRecord> mPipeListeners;
    private String mTestID;
    private IVodCommandCallback mVodCommandCallback;
    private final ConcurrentHashMap<String, PipeListenerRecord> mVodControlMap;
    private final ConcurrentHashMap<Integer, String> mVodIdMap;

    /* loaded from: classes5.dex */
    public interface IVodCommandCallback {
        int onVodCommand(String str, String str2, String[] strArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public final class PipeListenerRecord implements IBinder.DeathRecipient {
        public String id;
        public IPipeBusListener listener;
        public final int pid;
        public final int uid;

        public PipeListenerRecord(IPipeBusListener listener, int pid, int uid) {
            this.listener = listener;
            this.pid = pid;
            this.uid = uid;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            String str = VodManagerServer.TAG;
            LogUtil.i(str, "binderDied, pid=" + this.pid);
            synchronized (VodManagerServer.this.mPipeListeners) {
                VodManagerServer.this.mPipeListeners.remove(this);
            }
            if (this.id != null) {
                VodManagerServer.this.mVodControlMap.remove(this.id);
            }
        }
    }

    /* loaded from: classes5.dex */
    private static class VodManageServerHolder {
        private static final VodManagerServer instance = new VodManagerServer();

        private VodManageServerHolder() {
        }
    }

    private VodManagerServer() {
        this.mPipeListeners = new ArrayList<>();
        this.mVodIdMap = new ConcurrentHashMap<>();
        this.mVodControlMap = new ConcurrentHashMap<>();
        this.mTestID = null;
        DumpDispatcher.registerDump("vod", new DumpDispatcher.DebugDumper() { // from class: com.xiaopeng.xuiservice.opensdk.manager.-$$Lambda$VodManagerServer$SoOSTNf7iJYwrBTMbGMgUz_-k98
            @Override // com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
            public final void debugDump(PrintWriter printWriter, String[] strArr) {
                VodManagerServer.this.lambda$new$0$VodManagerServer(printWriter, strArr);
            }
        });
    }

    public static VodManagerServer getInstance() {
        return VodManageServerHolder.instance;
    }

    public static String getModuleName() {
        return MODULE_NAME;
    }

    public void init() {
        initFuncMap();
    }

    public void setVodCommandCallback(IVodCommandCallback callback) {
        this.mVodCommandCallback = callback;
    }

    public int ioControl(String module, String cmd, String[] params) throws RemoteException {
        LogUtil.i(TAG, "ioControl, not supported yet");
        return 0;
    }

    public int ioControlWithParcelable(String moduleName, String cmd, ParcelableData dataIn, ParcelableData dataOut) throws RemoteException {
        LogUtil.i(TAG, "ioControlWithParcelable, not supported yet");
        return 0;
    }

    public int ioControlWithBytes(String moduleName, String cmd, String[] params, byte[] bytesIn, byte[] bytesOut) throws RemoteException {
        LogUtil.i(TAG, "ioControlWithBytes, not supported yet");
        return 0;
    }

    public int ioControlWithPocket(String moduleName, String cmd, String[] params, String[] results) throws RemoteException {
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        String str = TAG;
        LogUtil.d(str, "ioControlWithPocket,cmd=" + cmd + ",call pid=" + pid + ",uid=" + uid);
        return handleIoControlWithPocket(cmd, params, results, pid, uid);
    }

    public void registerListener(String moduleName, IPipeBusListener listener) throws RemoteException {
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        String str = TAG;
        LogUtil.d(str, "registerListener,call pid=" + pid + ",uid=" + uid);
        PipeListenerRecord record = new PipeListenerRecord(listener, pid, uid);
        try {
            listener.asBinder().linkToDeath(record, 0);
            synchronized (this.mPipeListeners) {
                this.mPipeListeners.add(record);
            }
            String id = this.mVodIdMap.get(Integer.valueOf(uid));
            PipeListenerRecord record2 = this.mVodControlMap.get(id);
            if (record2 != null) {
                PipeListenerRecord record3 = getRecordFromPid(record2.pid);
                if (record3 != null) {
                    record3.id = id;
                    this.mVodControlMap.put(id, record3);
                    return;
                }
                String str2 = TAG;
                LogUtil.w(str2, "no valid record for id:" + id);
            }
        } catch (RemoteException e) {
            LogUtil.e(TAG, "PipeListener is dead, ignoring it");
        }
    }

    public void unRegisterListener(String moduleName) throws RemoteException {
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        String str = TAG;
        LogUtil.d(str, "unRegisterListener,call pid=" + pid + ",uid=" + uid);
        PipeListenerRecord record = null;
        synchronized (this.mPipeListeners) {
            Iterator<PipeListenerRecord> it = this.mPipeListeners.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                PipeListenerRecord r = it.next();
                if (r.pid == pid) {
                    record = r;
                    break;
                }
            }
        }
        if (record != null) {
            try {
                record.listener.asBinder().unlinkToDeath(record, 0);
            } catch (Exception e) {
            }
            synchronized (this.mPipeListeners) {
                this.mPipeListeners.remove(record);
            }
            if (record.id != null) {
                record.listener = null;
                return;
            }
            return;
        }
        String str2 = TAG;
        LogUtil.w(str2, "unRegisterListener, no record for pid:" + pid);
    }

    public void registerModuleImplementor(String moduleName, IPipeBus impl) throws RemoteException {
    }

    public void unRegisterModuleImplementor(String moduleName, IPipeBus impl) throws RemoteException {
    }

    public IPipeBus getModuleImplementor(String module) throws RemoteException {
        return null;
    }

    public int onSearch(String id, String param) {
        PipeListenerRecord record = this.mVodControlMap.get(id);
        if (record == null || record.listener == null) {
            String str = TAG;
            LogUtil.w(str, "onSearch,no valid vod control for:" + id);
            return -300;
        }
        try {
            record.listener.onPipeBusNotify(MODULE_NAME, "vodSearch", new String[]{param});
            return 0;
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "onSearch,id=" + id + ",e=" + e);
            return -1;
        }
    }

    public int onPlayContent(String id, String param) {
        PipeListenerRecord record = this.mVodControlMap.get(id);
        if (record == null || record.listener == null) {
            String str = TAG;
            LogUtil.w(str, "onPlayContent,no valid vod control for:" + id);
            return -300;
        }
        try {
            record.listener.onPipeBusNotify(MODULE_NAME, "vodPlayContent", new String[]{param});
            return 0;
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "onPlayContent,id=" + id + ",e=" + e);
            return -1;
        }
    }

    public int onSearchAndPlay(String id, String param) {
        PipeListenerRecord record = this.mVodControlMap.get(id);
        if (record == null || record.listener == null) {
            String str = TAG;
            LogUtil.w(str, "onSearchAndPlay,no valid vod control for:" + id);
            return -300;
        }
        try {
            record.listener.onPipeBusNotify(MODULE_NAME, "vodSearchAndPlay", new String[]{param});
            return 0;
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "onSearchAndPlay,id=" + id + ",e=" + e);
            return -1;
        }
    }

    public int onPageBack(String id) {
        PipeListenerRecord record = this.mVodControlMap.get(id);
        if (record == null || record.listener == null) {
            String str = TAG;
            LogUtil.w(str, "onPageBack,no valid vod control for:" + id);
            return -300;
        }
        try {
            record.listener.onPipeBusNotify(MODULE_NAME, "vodPageBack", (String[]) null);
            return 0;
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "onPageBack,id=" + id + ",e=" + e);
            return -1;
        }
    }

    public int onPageHistory(String id) {
        PipeListenerRecord record = this.mVodControlMap.get(id);
        if (record == null || record.listener == null) {
            String str = TAG;
            LogUtil.w(str, "onPageHistory,no valid vod control for:" + id);
            return -300;
        }
        try {
            record.listener.onPipeBusNotify(MODULE_NAME, "vodPageHistory", (String[]) null);
            return 0;
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "onPageHistory,id=" + id + ",e=" + e);
            return -1;
        }
    }

    public int onPageRankList(String id, String type) {
        PipeListenerRecord record = this.mVodControlMap.get(id);
        if (record == null || record.listener == null) {
            String str = TAG;
            LogUtil.w(str, "onPageRankList,no valid vod control for:" + id);
            return -300;
        }
        try {
            record.listener.onPipeBusNotify(MODULE_NAME, "vodPageRankList", new String[]{type});
            return 0;
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "onPageRankList,id=" + id + ",e=" + e);
            return -1;
        }
    }

    public int onPageHome(String id, String type) {
        PipeListenerRecord record = this.mVodControlMap.get(id);
        if (record == null || record.listener == null) {
            String str = TAG;
            LogUtil.w(str, "onPageHome,no valid vod control for:" + id);
            return -300;
        }
        try {
            record.listener.onPipeBusNotify(MODULE_NAME, "vodPageHome", new String[]{type});
            return 0;
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "onPageHome,id=" + id + ",e=" + e);
            return -1;
        }
    }

    public int onPageLogin(String id) {
        PipeListenerRecord record = this.mVodControlMap.get(id);
        if (record == null || record.listener == null) {
            String str = TAG;
            LogUtil.w(str, "onPageLogin,no valid vod control for:" + id);
            return -300;
        }
        try {
            record.listener.onPipeBusNotify(MODULE_NAME, "vodPageLogIn", (String[]) null);
            return 0;
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "onPageLogin,id=" + id + ",e=" + e);
            return -1;
        }
    }

    public int onPageBuyVip(String id, String param) {
        PipeListenerRecord record = this.mVodControlMap.get(id);
        if (record == null || record.listener == null) {
            String str = TAG;
            LogUtil.w(str, "onPageBuyVip,no valid vod control for:" + id);
            return -300;
        }
        try {
            record.listener.onPipeBusNotify(MODULE_NAME, "vodPageBuyVip", new String[]{param});
            return 0;
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "onPageBuyVip,id=" + id + ",e=" + e);
            return -1;
        }
    }

    public int onPlayPlay(String id) {
        PipeListenerRecord record = this.mVodControlMap.get(id);
        if (record == null || record.listener == null) {
            String str = TAG;
            LogUtil.w(str, "onPlayPlay,no valid vod control for:" + id);
            return -300;
        }
        try {
            record.listener.onPipeBusNotify(MODULE_NAME, "vodPlayPlay", (String[]) null);
            return 0;
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "onPlayPlay,id=" + id + ",e=" + e);
            return -1;
        }
    }

    public int onPlayPause(String id) {
        PipeListenerRecord record = this.mVodControlMap.get(id);
        if (record == null || record.listener == null) {
            String str = TAG;
            LogUtil.w(str, "onPlayPause,no valid vod control for:" + id);
            return -300;
        }
        try {
            record.listener.onPipeBusNotify(MODULE_NAME, "vodPlayPause", (String[]) null);
            return 0;
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "onPlayPause,id=" + id + ",e=" + e);
            return -1;
        }
    }

    public int onPlayPrevious(String id) {
        PipeListenerRecord record = this.mVodControlMap.get(id);
        if (record == null || record.listener == null) {
            String str = TAG;
            LogUtil.w(str, "onPlayPrevious,no valid vod control for:" + id);
            return -300;
        }
        try {
            record.listener.onPipeBusNotify(MODULE_NAME, "vodPlayPrevious", (String[]) null);
            return 0;
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "onPlayPrevious,id=" + id + ",e=" + e);
            return -1;
        }
    }

    public int onPlayNext(String id) {
        PipeListenerRecord record = this.mVodControlMap.get(id);
        if (record == null || record.listener == null) {
            String str = TAG;
            LogUtil.w(str, "onPlayNext,no valid vod control for:" + id);
            return -300;
        }
        try {
            record.listener.onPipeBusNotify(MODULE_NAME, "vodPlayNext", (String[]) null);
            return 0;
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "onPlayNext,id=" + id + ",e=" + e);
            return -1;
        }
    }

    public int onPlayEpisode(String id, String episode) {
        PipeListenerRecord record = this.mVodControlMap.get(id);
        if (record == null || record.listener == null) {
            String str = TAG;
            LogUtil.w(str, "onPlayEpisode,no valid vod control for:" + id);
            return -300;
        }
        try {
            record.listener.onPipeBusNotify(MODULE_NAME, "vodPlayEpisode", new String[]{episode});
            return 0;
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "onPlayEpisode,id=" + id + ",e=" + e);
            return -1;
        }
    }

    public int onPlayFastForward(String id, String timeMs) {
        PipeListenerRecord record = this.mVodControlMap.get(id);
        if (record == null || record.listener == null) {
            String str = TAG;
            LogUtil.w(str, "onPlayFastForward,no valid vod control for:" + id);
            return -300;
        }
        try {
            record.listener.onPipeBusNotify(MODULE_NAME, "vodPlayFF", new String[]{timeMs});
            return 0;
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "onPlayFastForward,id=" + id + ",e=" + e);
            return -1;
        }
    }

    public int onPlayFastBackward(String id, String timeMs) {
        PipeListenerRecord record = this.mVodControlMap.get(id);
        if (record == null || record.listener == null) {
            String str = TAG;
            LogUtil.w(str, "onPlayFastBackward,no valid vod control for:" + id);
            return -300;
        }
        try {
            record.listener.onPipeBusNotify(MODULE_NAME, "vodPlayFB", new String[]{timeMs});
            return 0;
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "onPlayFastBackward,id=" + id + ",e=" + e);
            return -1;
        }
    }

    public int onPlaySeekTo(String id, String timeMs) {
        PipeListenerRecord record = this.mVodControlMap.get(id);
        if (record == null || record.listener == null) {
            String str = TAG;
            LogUtil.w(str, "onPlaySeekTo,no valid vod control for:" + id);
            return -300;
        }
        try {
            record.listener.onPipeBusNotify(MODULE_NAME, "vodPlaySeek", new String[]{timeMs});
            return 0;
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "onPlaySeekTo,id=" + id + ",e=" + e);
            return -1;
        }
    }

    public int onPlaySetSpeed(String id, String speed) {
        PipeListenerRecord record = this.mVodControlMap.get(id);
        if (record == null || record.listener == null) {
            String str = TAG;
            LogUtil.w(str, "onPlaySetSpeed,no valid vod control for:" + id);
            return -300;
        }
        try {
            record.listener.onPipeBusNotify(MODULE_NAME, "vodPlaySpeed", new String[]{speed});
            return 0;
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "onPlaySetSpeed,id=" + id + ",e=" + e);
            return -1;
        }
    }

    public int onPlaySetResolution(String id, String resolution) {
        PipeListenerRecord record = this.mVodControlMap.get(id);
        if (record == null || record.listener == null) {
            String str = TAG;
            LogUtil.w(str, "onPlaySetResolution,no valid vod control for:" + id);
            return -300;
        }
        try {
            record.listener.onPipeBusNotify(MODULE_NAME, "vodPlayResolution", new String[]{resolution});
            return 0;
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "onPlaySetResolution,id=" + id + ",e=" + e);
            return -1;
        }
    }

    public int onPlaySkipVideoHead(String id) {
        PipeListenerRecord record = this.mVodControlMap.get(id);
        if (record == null || record.listener == null) {
            String str = TAG;
            LogUtil.w(str, "onPlaySkipVideoHead,no valid vod control for:" + id);
            return -300;
        }
        try {
            record.listener.onPipeBusNotify(MODULE_NAME, "vodPlaySkipHead", (String[]) null);
            return 0;
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "onPlaySkipVideoHead,id=" + id + ",e=" + e);
            return -1;
        }
    }

    public int onPlaySkipVideoTail(String id) {
        PipeListenerRecord record = this.mVodControlMap.get(id);
        if (record == null || record.listener == null) {
            String str = TAG;
            LogUtil.w(str, "onPlaySkipVideoTail,no valid vod control for:" + id);
            return -300;
        }
        try {
            record.listener.onPipeBusNotify(MODULE_NAME, "vodPlaySkipTail", (String[]) null);
            return 0;
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "onPlaySkipVideoTail,id=" + id + ",e=" + e);
            return -1;
        }
    }

    public int onPlayExit(String id) {
        PipeListenerRecord record = this.mVodControlMap.get(id);
        if (record == null || record.listener == null) {
            String str = TAG;
            LogUtil.w(str, "onPlayExit,no valid vod control for:" + id);
            return -300;
        }
        try {
            record.listener.onPipeBusNotify(MODULE_NAME, "vodPlayExit", (String[]) null);
            return 0;
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "onPlayExit,id=" + id + ",e=" + e);
            return -1;
        }
    }

    public int onPlayAddFavor(String id, boolean favor) {
        PipeListenerRecord record = this.mVodControlMap.get(id);
        if (record == null || record.listener == null) {
            String str = TAG;
            LogUtil.w(str, "onPlayAddFavor,no valid vod control for:" + id);
            return -300;
        }
        try {
            IPipeBusListener iPipeBusListener = record.listener;
            String[] strArr = new String[1];
            strArr[0] = favor ? "1" : "0";
            iPipeBusListener.onPipeBusNotify(MODULE_NAME, "vodPlayAddFavor", strArr);
            return 0;
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "onPlayAddFavor,id=" + id + ",e=" + e);
            return -1;
        }
    }

    public int onPlayAudioMode(String id) {
        PipeListenerRecord record = this.mVodControlMap.get(id);
        if (record == null || record.listener == null) {
            String str = TAG;
            LogUtil.w(str, "onPlayAudioMode,no valid vod control for:" + id);
            return -300;
        }
        try {
            record.listener.onPipeBusNotify(MODULE_NAME, "vodAudioMode", (String[]) null);
            return 0;
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "onPlayAudioMode,id=" + id + ",e=" + e);
            return -1;
        }
    }

    public int onPlayVideoMode(String id) {
        PipeListenerRecord record = this.mVodControlMap.get(id);
        if (record == null || record.listener == null) {
            String str = TAG;
            LogUtil.w(str, "onPlayVideoMode,no valid vod control for:" + id);
            return -300;
        }
        try {
            record.listener.onPipeBusNotify(MODULE_NAME, "vodVideoMode", (String[]) null);
            return 0;
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "onPlayVideoMode,id=" + id + ",e=" + e);
            return -1;
        }
    }

    private int handleIoControlWithPocket(String cmd, String[] params, String[] results, int pid, int uid) {
        int ret = 0;
        String str = null;
        String param = params != null ? params[0] : null;
        char c = 65535;
        int hashCode = cmd.hashCode();
        if (hashCode != 1354174791) {
            if (hashCode == 1515458933 && cmd.equals("vodSetIdentity")) {
                c = 1;
            }
        } else if (cmd.equals("vodSendCmd")) {
            c = 0;
        }
        if (c != 0) {
            if (c == 1) {
                this.mVodIdMap.put(Integer.valueOf(uid), param);
                PipeListenerRecord record = getRecordFromPid(pid);
                if (record == null) {
                    record = new PipeListenerRecord(null, pid, uid);
                }
                this.mVodControlMap.put(param, record);
            } else {
                ret = -1;
            }
        } else if (this.mVodCommandCallback == null) {
            LogUtil.w(TAG, "null VodCommandCallback,param=" + param);
            ret = -20;
        } else {
            String id = this.mVodIdMap.get(Integer.valueOf(uid));
            if (id == null) {
                LogUtil.w(TAG, "unknown send cmd,no id found");
            } else {
                this.mVodCommandCallback.onVodCommand(id, param, results);
            }
        }
        String id2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("handleIoControlWithPocket, cmd=");
        sb.append(cmd);
        sb.append(",param=");
        sb.append(param);
        sb.append(",result=");
        if (results != null && results.length > 0) {
            str = results[0];
        }
        sb.append(str);
        sb.append(",ret=");
        sb.append(ret);
        LogUtil.i(id2, sb.toString());
        return ret;
    }

    private PipeListenerRecord getRecordFromPid(int pid) {
        synchronized (this.mPipeListeners) {
            Iterator<PipeListenerRecord> it = this.mPipeListeners.iterator();
            while (it.hasNext()) {
                PipeListenerRecord record = it.next();
                if (pid == record.pid) {
                    return record;
                }
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: dump */
    public void lambda$new$0$VodManagerServer(PrintWriter pw, String[] args) {
        boolean z;
        pw.println("dump " + TAG);
        if (args == null || args.length < 1) {
            pw.println("lack of params");
            return;
        }
        for (int i = 0; i < args.length; i++) {
            String str = args[i];
            int hashCode = str.hashCode();
            if (hashCode == 1492) {
                if (str.equals("-a")) {
                    z = false;
                }
                z = true;
            } else if (hashCode == 1511) {
                if (str.equals("-t")) {
                    z = true;
                }
                z = true;
            } else if (hashCode != 46600) {
                if (hashCode == 44713073 && str.equals("-func")) {
                    z = true;
                }
                z = true;
            } else {
                if (str.equals("-id")) {
                    z = true;
                }
                z = true;
            }
            if (!z) {
                dumpAll(pw);
                return;
            } else if (!z) {
                if (z) {
                    this.mTestID = args[i + 1];
                } else if (z) {
                    try {
                        String func = args[i + 1];
                        String param = null;
                        if (i + 2 < args.length) {
                            param = args[i + 2];
                        }
                        handleTestFunc(pw, this.mTestID, func, param);
                    } catch (Exception e) {
                        pw.println("func err,e=" + e);
                    }
                }
            } else {
                functionTest();
                return;
            }
        }
    }

    private void dumpAll(PrintWriter pw) {
        pw.println("registered vod controls:" + this.mPipeListeners.size());
        for (int i = 0; i < this.mPipeListeners.size(); i++) {
            PipeListenerRecord record = this.mPipeListeners.get(i);
            try {
                pw.println("control " + i + ",pid=" + record.pid + ",binder=" + record.listener.asBinder().getInterfaceDescriptor());
            } catch (Exception e) {
            }
        }
        pw.println("registered vod ids:" + this.mVodControlMap.size());
        for (Map.Entry<String, PipeListenerRecord> entry : this.mVodControlMap.entrySet()) {
            StringBuilder sb = new StringBuilder();
            sb.append("id ");
            sb.append(entry.getKey());
            sb.append(",pid=");
            sb.append(entry.getValue().pid);
            sb.append(",uid=");
            sb.append(entry.getValue().uid);
            sb.append(",binder=");
            sb.append(entry.getValue().listener != null ? entry.getValue().listener.asBinder() : null);
            pw.println(sb.toString());
        }
        pw.println("supported control func:");
        for (Map.Entry<String, Method> entry2 : mFuncMap.entrySet()) {
            pw.println("func:" + entry2.getKey() + ",api:" + entry2.getValue().getName() + ",param count:" + entry2.getValue().getParameterCount());
        }
    }

    private void functionTest() {
        onSearch("IdYouKu", "小鹏汽车");
        onPageBack("IdYouKu");
        onPlayAudioMode("IdYouKu");
        onPlayVideoMode("IdYouKu");
    }

    private void initFuncMap() {
        try {
            mFuncMap.put(SpeechWidget.TYPE_SEARCH, getClass().getDeclaredMethod("onSearch", String.class, String.class));
            mFuncMap.put("playContent", getClass().getDeclaredMethod("onPlayContent", String.class, String.class));
            mFuncMap.put("searchAndPlay", getClass().getDeclaredMethod("onSearchAndPlay", String.class, String.class));
            mFuncMap.put("pageBack", getClass().getDeclaredMethod("onPageBack", String.class));
            mFuncMap.put("pageHistory", getClass().getDeclaredMethod("onPageHistory", String.class));
            mFuncMap.put("pageRankList", getClass().getDeclaredMethod("onPageRankList", String.class, String.class));
            mFuncMap.put("pageHome", getClass().getDeclaredMethod("onPageHome", String.class, String.class));
            mFuncMap.put("pageLogin", getClass().getDeclaredMethod("onPageLogin", String.class));
            mFuncMap.put("pageBuyVip", getClass().getDeclaredMethod("onPageBuyVip", String.class, String.class));
            mFuncMap.put("play", getClass().getDeclaredMethod("onPlayPlay", String.class));
            mFuncMap.put("pause", getClass().getDeclaredMethod("onPlayPause", String.class));
            mFuncMap.put("previous", getClass().getDeclaredMethod("onPlayPrevious", String.class));
            mFuncMap.put("next", getClass().getDeclaredMethod("onPlayNext", String.class));
            mFuncMap.put("episode", getClass().getDeclaredMethod("onPlayEpisode", String.class, String.class));
            mFuncMap.put("ff", getClass().getDeclaredMethod("onPlayFastForward", String.class, String.class));
            mFuncMap.put("fb", getClass().getDeclaredMethod("onPlayFastBackward", String.class, String.class));
            mFuncMap.put("seekto", getClass().getDeclaredMethod("onPlaySeekTo", String.class, String.class));
            mFuncMap.put("speed", getClass().getDeclaredMethod("onPlaySetSpeed", String.class, String.class));
            mFuncMap.put("resolution", getClass().getDeclaredMethod("onPlaySetResolution", String.class, String.class));
            mFuncMap.put("skipHead", getClass().getDeclaredMethod("onPlaySkipVideoHead", String.class));
            mFuncMap.put("skipTail", getClass().getDeclaredMethod("onPlaySkipVideoTail", String.class));
            mFuncMap.put("exit", getClass().getDeclaredMethod("onPlayExit", String.class));
            mFuncMap.put("addFavor", getClass().getDeclaredMethod("onPlayAddFavor", String.class, Boolean.TYPE));
            mFuncMap.put("audioMode", getClass().getDeclaredMethod("onPlayAudioMode", String.class));
            mFuncMap.put("videoMode", getClass().getDeclaredMethod("onPlayVideoMode", String.class));
        } catch (NoSuchMethodException e) {
            String str = TAG;
            LogUtil.w(str, "initFuncMap,e=" + e);
        }
    }

    private void handleTestFunc(PrintWriter pw, String id, String api, String param) {
        Method method = mFuncMap.get(api);
        if (method == null) {
            pw.println("no method for:" + api);
            return;
        }
        if (id == null) {
            id = "IdYouKu";
        }
        pw.println("test id:" + id + ",func:" + method.getName() + ",param:" + param);
        try {
            int paramCount = method.getParameterCount();
            if (paramCount == 1) {
                method.invoke(this, id);
            } else if (paramCount == 2) {
                if ("addFavor".equals(api)) {
                    onPlayAddFavor(id, "1".equals(param));
                } else {
                    method.invoke(this, id, param);
                }
            } else {
                pw.println("no suitable param for:" + api);
            }
        } catch (Exception e) {
            pw.println("test " + api + ",e=" + e);
        }
    }
}
