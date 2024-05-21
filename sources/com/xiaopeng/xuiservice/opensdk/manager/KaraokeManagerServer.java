package com.xiaopeng.xuiservice.opensdk.manager;

import android.app.ActivityThread;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import com.alipay.mobile.aromeservice.ResponseParams;
import com.car.opensdk.pipebus.IPipeBus;
import com.car.opensdk.pipebus.IPipeBusListener;
import com.car.opensdk.pipebus.ParcelableData;
import com.xiaopeng.xuimanager.XUIManager;
import com.xiaopeng.xuimanager.karaoke.KaraokeManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import com.xiaopeng.xuiservice.utils.PackageUtils;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes5.dex */
public class KaraokeManagerServer extends IPipeBus.Stub {
    private static final String MODULE_NAME = "karaoke";
    private static final String TAG = "KaraokeServer";
    private KaraokeManager mKaraokeManager;
    private final ArrayList<PipeListenerRecord> mPipeListeners;
    private final KaraokeManager.MicCallBack micCallBack;

    /* loaded from: classes5.dex */
    final class PipeListenerRecord implements IBinder.DeathRecipient {
        public final IPipeBusListener listener;
        public final int pid;
        public final int uid;

        public PipeListenerRecord(IPipeBusListener listener, int pid, int uid) {
            this.listener = listener;
            this.pid = pid;
            this.uid = uid;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            LogUtil.d(KaraokeManagerServer.TAG, "binderDied, pid=" + this.pid);
            KaraokeManagerServer.this.mKaraokeManager.XMA_clientDied();
            synchronized (KaraokeManagerServer.this.mPipeListeners) {
                KaraokeManagerServer.this.mPipeListeners.remove(this);
            }
        }
    }

    /* loaded from: classes5.dex */
    private static class KaraokeManagerServerHolder {
        private static final KaraokeManagerServer instance = new KaraokeManagerServer();

        private KaraokeManagerServerHolder() {
        }
    }

    private KaraokeManagerServer() {
        this.mKaraokeManager = null;
        this.mPipeListeners = new ArrayList<>();
        this.micCallBack = new KaraokeManager.MicCallBack() { // from class: com.xiaopeng.xuiservice.opensdk.manager.KaraokeManagerServer.1
            public void micServiceCallBack(int msg) {
                if (!KaraokeManagerServer.this.mPipeListeners.isEmpty()) {
                    synchronized (KaraokeManagerServer.this.mPipeListeners) {
                        Iterator it = KaraokeManagerServer.this.mPipeListeners.iterator();
                        while (it.hasNext()) {
                            PipeListenerRecord record = (PipeListenerRecord) it.next();
                            try {
                                record.listener.onPipeBusNotify("karaoke", ResponseParams.RESPONSE_KEY_MESSAGE, new String[]{String.valueOf(msg)});
                            } catch (Exception e) {
                                LogUtil.w(KaraokeManagerServer.TAG, "micServiceCallBack e=" + e);
                            }
                        }
                    }
                }
            }

            public void onErrorEvent(int errorCode, int operation) {
                if (!KaraokeManagerServer.this.mPipeListeners.isEmpty()) {
                    synchronized (KaraokeManagerServer.this.mPipeListeners) {
                        Iterator it = KaraokeManagerServer.this.mPipeListeners.iterator();
                        while (it.hasNext()) {
                            PipeListenerRecord record = (PipeListenerRecord) it.next();
                            try {
                                record.listener.onPipeBusNotify("karaoke", "error", new String[]{String.valueOf(errorCode), String.valueOf(operation)});
                            } catch (Exception e) {
                                LogUtil.w(KaraokeManagerServer.TAG, "micServiceCallBack e=" + e);
                            }
                        }
                    }
                }
            }
        };
    }

    public static KaraokeManagerServer getInstance() {
        return KaraokeManagerServerHolder.instance;
    }

    public void init() {
        getKaraokeManager();
        DumpDispatcher.registerDump("karaokeservice2", new DumpDispatcher.DebugDumper() { // from class: com.xiaopeng.xuiservice.opensdk.manager.KaraokeManagerServer.2
            @Override // com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
            public void debugDump(PrintWriter pw, String[] args) {
                if (args == null) {
                    pw.println("please input params...");
                    return;
                }
                boolean z = false;
                String str = args[0];
                if (str.hashCode() != -172220347 || !str.equals("callback")) {
                    z = true;
                }
                if (!z) {
                    KaraokeManagerServer.this.callbackTest();
                }
            }
        });
    }

    public int ioControl(String module, String cmd, String[] params) throws RemoteException {
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        LogUtil.d(TAG, "ioControl,cmd=" + cmd + ",call pid=" + pid + ",uid=" + uid);
        return 0;
    }

    public int ioControlWithParcelable(String moduleName, String cmd, ParcelableData dataIn, ParcelableData dataOut) throws RemoteException {
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        LogUtil.d(TAG, "ioControlWithParcelable,cmd=" + cmd + ",call pid=" + pid + ",uid=" + uid);
        return 0;
    }

    public int ioControlWithBytes(String moduleName, String cmd, String[] params, byte[] bytesIn, byte[] bytesOut) throws RemoteException {
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        LogUtil.d(TAG, "ioControlWithBytes,cmd=" + cmd + ",call pid=" + pid + ",uid=" + uid);
        return handleCmdWithBytes(cmd, params, bytesIn, bytesOut, pid, uid);
    }

    public int ioControlWithPocket(String moduleName, String cmd, String[] params, String[] results) throws RemoteException {
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        LogUtil.d(TAG, "ioControlWithPocket,cmd=" + cmd + ",call pid=" + pid + ",uid=" + uid);
        return handleIoControlWithPocket(cmd, params, results, pid, uid);
    }

    public void registerListener(String moduleName, IPipeBusListener listener) throws RemoteException {
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        LogUtil.d(TAG, "registerListener,call pid=" + pid + ",uid=" + uid);
        getKaraokeManager();
        PipeListenerRecord record = new PipeListenerRecord(listener, pid, uid);
        try {
            listener.asBinder().linkToDeath(record, 0);
            if (this.mPipeListeners.isEmpty()) {
                try {
                    this.mKaraokeManager.XMA_registerCallback(this.micCallBack);
                } catch (Exception e2) {
                    LogUtil.w(TAG, "registerListener e2=" + e2);
                }
            }
            synchronized (this.mPipeListeners) {
                this.mPipeListeners.add(record);
            }
        } catch (RemoteException e) {
            LogUtil.e(TAG, "PipeListener is dead, ignoring it");
        }
    }

    public void unRegisterListener(String moduleName) throws RemoteException {
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        LogUtil.d(TAG, "unRegisterListener,call pid=" + pid + ",uid=" + uid);
        getKaraokeManager();
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
        } else {
            LogUtil.w(TAG, "unRegisterListener, no record for pid:" + pid);
        }
        if (this.mPipeListeners.isEmpty()) {
            try {
                this.mKaraokeManager.XMA_unRegisterCallback();
            } catch (Exception e2) {
                LogUtil.w(TAG, "unRegisterListener e=" + e2);
            }
        }
    }

    public void registerModuleImplementor(String moduleName, IPipeBus impl) throws RemoteException {
    }

    public void unRegisterModuleImplementor(String moduleName, IPipeBus impl) throws RemoteException {
    }

    public IPipeBus getModuleImplementor(String module) throws RemoteException {
        return null;
    }

    public static String getModuleName() {
        return "karaoke";
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private int handleIoControlWithPocket(String cmd, String[] params, String[] results, int pid, int uid) {
        char c;
        int ret = -4;
        getKaraokeManager();
        switch (cmd.hashCode()) {
            case -1829276557:
                if (cmd.equals("trackGetLatency")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case -1437457885:
                if (cmd.equals("micCreate")) {
                    c = 19;
                    break;
                }
                c = 65535;
                break;
            case -1335539439:
                if (cmd.equals("deinit")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -1082702925:
                if (cmd.equals("micDestroy")) {
                    c = 21;
                    break;
                }
                c = 65535;
                break;
            case -993035092:
                if (cmd.equals("trackGetMinBuf")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case -987250077:
                if (cmd.equals("getMicStatus")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -934426579:
                if (cmd.equals("resume")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case -607433681:
                if (cmd.equals("trackDestroy")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case -557192959:
                if (cmd.equals("resumePlay")) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            case -321004740:
                if (cmd.equals("setMusicLyric")) {
                    c = 23;
                    break;
                }
                c = 65535;
                break;
            case -266102356:
                if (cmd.equals("recCreate")) {
                    c = 15;
                    break;
                }
                c = 65535;
                break;
            case 3237136:
                if (cmd.equals("init")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 106440182:
                if (cmd.equals("pause")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case 670514716:
                if (cmd.equals("setVolume")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 794630695:
                if (cmd.equals("trackCreate")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 829307466:
                if (cmd.equals("pausePlay")) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case 869580106:
                if (cmd.equals("recDestroy")) {
                    c = 17;
                    break;
                }
                c = 65535;
                break;
            case 885131792:
                if (cmd.equals("getVolume")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 1078006535:
                if (cmd.equals("recGetMinBuf")) {
                    c = 14;
                    break;
                }
                c = 65535;
                break;
            case 1697028486:
                if (cmd.equals("getMicPowerStatus")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1711046304:
                if (cmd.equals("micGetAvail")) {
                    c = 20;
                    break;
                }
                c = 65535;
                break;
            case 1834722096:
                if (cmd.equals("micGetMinBuf")) {
                    c = 18;
                    break;
                }
                c = 65535;
                break;
            case 1929207345:
                if (cmd.equals("setMusicInfo")) {
                    c = 22;
                    break;
                }
                c = 65535;
                break;
            case 2102278121:
                if (cmd.equals("recGetAvail")) {
                    c = 16;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                String pkgName = params[0];
                KaraokeManager karaokeManager = this.mKaraokeManager;
                if (karaokeManager != null) {
                    ret = karaokeManager.XMA_init(pkgName);
                }
                LogUtil.d(TAG, "init,pkg=" + pkgName + ",ret=" + ret);
                if (this.mKaraokeManager != null) {
                    String pkg = PackageUtils.getPackageName(pid);
                    if (pkg == null) {
                        pkg = "com.xiaopeng.xuiservice";
                    }
                    this.mKaraokeManager.setCallingAppPkg(pkg);
                    break;
                }
                break;
            case 1:
                KaraokeManager karaokeManager2 = this.mKaraokeManager;
                if (karaokeManager2 != null) {
                    ret = karaokeManager2.XMA_deinit();
                    break;
                }
                break;
            case 2:
                KaraokeManager karaokeManager3 = this.mKaraokeManager;
                if (karaokeManager3 != null) {
                    ret = karaokeManager3.XMA_getMicStatus();
                    break;
                }
                break;
            case 3:
                KaraokeManager karaokeManager4 = this.mKaraokeManager;
                if (karaokeManager4 != null) {
                    ret = karaokeManager4.XMA_getMicPowerStatus();
                    break;
                }
                break;
            case 4:
                KaraokeManager karaokeManager5 = this.mKaraokeManager;
                if (karaokeManager5 != null) {
                    ret = karaokeManager5.XMA_setVolume(Integer.parseInt(params[0]), Integer.parseInt(params[1]));
                    break;
                }
                break;
            case 5:
                KaraokeManager karaokeManager6 = this.mKaraokeManager;
                if (karaokeManager6 != null) {
                    ret = karaokeManager6.XMA_getVolume(Integer.parseInt(params[0]));
                    break;
                }
                break;
            case 6:
                KaraokeManager karaokeManager7 = this.mKaraokeManager;
                if (karaokeManager7 != null) {
                    ret = karaokeManager7.XMA_trackGetMinBuf(Integer.parseInt(params[0]), Integer.parseInt(params[1]));
                    break;
                }
                break;
            case 7:
                KaraokeManager karaokeManager8 = this.mKaraokeManager;
                if (karaokeManager8 != null) {
                    ret = karaokeManager8.XMA_trackCreate(Integer.parseInt(params[0]), Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                    break;
                }
                break;
            case '\b':
                KaraokeManager karaokeManager9 = this.mKaraokeManager;
                if (karaokeManager9 != null) {
                    ret = karaokeManager9.XMA_trackGetLatency();
                    break;
                }
                break;
            case '\t':
                KaraokeManager karaokeManager10 = this.mKaraokeManager;
                if (karaokeManager10 != null) {
                    ret = karaokeManager10.XMA_trackDestroy();
                    break;
                }
                break;
            case '\n':
                KaraokeManager karaokeManager11 = this.mKaraokeManager;
                if (karaokeManager11 != null) {
                    ret = karaokeManager11.XMA_pause();
                    break;
                }
                break;
            case 11:
                KaraokeManager karaokeManager12 = this.mKaraokeManager;
                if (karaokeManager12 != null) {
                    ret = karaokeManager12.XMA_resume();
                    break;
                }
                break;
            case '\f':
                KaraokeManager karaokeManager13 = this.mKaraokeManager;
                if (karaokeManager13 != null) {
                    ret = karaokeManager13.XMA_pausePlay();
                    break;
                }
                break;
            case '\r':
                KaraokeManager karaokeManager14 = this.mKaraokeManager;
                if (karaokeManager14 != null) {
                    ret = karaokeManager14.XMA_resumePlay();
                    break;
                }
                break;
            case 14:
                KaraokeManager karaokeManager15 = this.mKaraokeManager;
                if (karaokeManager15 != null) {
                    ret = karaokeManager15.XMA_recGetMinBuf(Integer.parseInt(params[0]), Integer.parseInt(params[1]));
                    break;
                }
                break;
            case 15:
                KaraokeManager karaokeManager16 = this.mKaraokeManager;
                if (karaokeManager16 != null) {
                    ret = karaokeManager16.XMA_recCreate(Integer.parseInt(params[0]), Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                    break;
                }
                break;
            case 16:
                KaraokeManager karaokeManager17 = this.mKaraokeManager;
                if (karaokeManager17 != null) {
                    ret = karaokeManager17.XMA_recGetAvail();
                    break;
                }
                break;
            case 17:
                KaraokeManager karaokeManager18 = this.mKaraokeManager;
                if (karaokeManager18 != null) {
                    ret = karaokeManager18.XMA_recDestroy();
                    break;
                }
                break;
            case 18:
                KaraokeManager karaokeManager19 = this.mKaraokeManager;
                if (karaokeManager19 != null) {
                    ret = karaokeManager19.XMA_micGetMinBuf(Integer.parseInt(params[0]), Integer.parseInt(params[1]));
                    break;
                }
                break;
            case 19:
                KaraokeManager karaokeManager20 = this.mKaraokeManager;
                if (karaokeManager20 != null) {
                    ret = karaokeManager20.XMA_micCreate(Integer.parseInt(params[0]), Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                    break;
                }
                break;
            case 20:
                KaraokeManager karaokeManager21 = this.mKaraokeManager;
                if (karaokeManager21 != null) {
                    ret = karaokeManager21.XMA_micGetAvail();
                    break;
                }
                break;
            case 21:
                KaraokeManager karaokeManager22 = this.mKaraokeManager;
                if (karaokeManager22 != null) {
                    ret = karaokeManager22.XMA_micDestroy();
                    break;
                }
                break;
            case 22:
                KaraokeManager karaokeManager23 = this.mKaraokeManager;
                if (karaokeManager23 != null) {
                    ret = karaokeManager23.XMA_setMusicInfo(params);
                    break;
                }
                break;
            case 23:
                KaraokeManager karaokeManager24 = this.mKaraokeManager;
                if (karaokeManager24 != null) {
                    ret = karaokeManager24.XMA_setMusicLyric(params);
                    break;
                }
                break;
        }
        LogUtil.i(TAG, "handleIoControlWithPocket, cmd=" + cmd + ",ret=" + ret + ",mKaraokeManager=" + this.mKaraokeManager);
        return ret;
    }

    private int handleCmdWithBytes(String cmd, String[] params, byte[] bytesIn, byte[] bytesOut, int pid, int uid) {
        KaraokeManager karaokeManager;
        StringBuilder sb = new StringBuilder();
        sb.append("handleCmdWithBytes, cmd=");
        sb.append(cmd);
        sb.append(",bytes in len=");
        sb.append(bytesIn != null ? bytesIn.length : 0);
        sb.append(",out len=");
        sb.append(bytesOut != null ? bytesOut.length : 0);
        LogUtil.d(TAG, sb.toString());
        getKaraokeManager();
        char c = 65535;
        int hashCode = cmd.hashCode();
        if (hashCode != 113399775) {
            if (hashCode != 1080386769) {
                if (hashCode == 1080391450 && cmd.equals("readRec")) {
                    c = 1;
                }
            } else if (cmd.equals("readMic")) {
                c = 2;
            }
        } else if (cmd.equals("write")) {
            c = 0;
        }
        if (c == 0) {
            KaraokeManager karaokeManager2 = this.mKaraokeManager;
            if (karaokeManager2 == null) {
                return -4;
            }
            int ret = karaokeManager2.XMA_write(bytesIn, Integer.parseInt(params[0]), Integer.parseInt(params[1]));
            return ret;
        } else if (c != 1) {
            if (c != 2 || (karaokeManager = this.mKaraokeManager) == null) {
                return -4;
            }
            int ret2 = karaokeManager.XMA_readMic(bytesOut);
            return ret2;
        } else {
            KaraokeManager karaokeManager3 = this.mKaraokeManager;
            if (karaokeManager3 == null) {
                return -4;
            }
            int ret3 = karaokeManager3.XMA_readRec(bytesOut);
            return ret3;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callbackTest() {
        for (int i = 1; i < 20; i += 2) {
            this.micCallBack.micServiceCallBack(i);
            try {
                Thread.sleep(2L);
            } catch (Exception e) {
            }
            this.micCallBack.onErrorEvent(i - 1, i);
            try {
                Thread.sleep(2L);
            } catch (Exception e2) {
            }
        }
    }

    private void getKaraokeManager() {
        if (this.mKaraokeManager == null) {
            XUIManager xuiManager = XUIManager.createXUIManager(ActivityThread.currentActivityThread().getApplication(), (ServiceConnection) null);
            try {
                this.mKaraokeManager = (KaraokeManager) xuiManager.getXUIServiceManager("karaoke");
                LogUtil.d(TAG, "init,mKaraokeManager=" + this.mKaraokeManager);
            } catch (Exception e) {
                LogUtil.w(TAG, "init e=" + e);
            }
        }
    }
}
