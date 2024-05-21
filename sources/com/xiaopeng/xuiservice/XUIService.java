package com.xiaopeng.xuiservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.ServiceManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.businessevent.BusinessEventManager;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes5.dex */
public class XUIService extends Service {
    private final BusinessEventManager mBusinessEventManager = BusinessEventManager.getInstance();
    private IXUIServiceImpl mIXUIServiceImpl;

    @Override // android.app.Service
    public void onCreate() {
        LogUtil.i(XUILog.TAG_SERVICE, "##XUIService onCreate enter");
        BroadcastManager.getInstance().init();
        this.mIXUIServiceImpl = IXUIServiceImpl.getInstance(this);
        ServiceManager.addService("XuiServiceManager", this.mIXUIServiceImpl);
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.-$$Lambda$XUIService$6vWhOCmoEeLbSnLapjuZpJ1tm_0
            @Override // java.lang.Runnable
            public final void run() {
                XUIService.this.lambda$onCreate$0$XUIService();
            }
        });
        super.onCreate();
        LogUtil.i(XUILog.TAG_SERVICE, "##XUIService onCreate leave");
    }

    public /* synthetic */ void lambda$onCreate$0$XUIService() {
        this.mBusinessEventManager.init();
    }

    @Override // android.app.Service
    public void onDestroy() {
        LogUtil.i(XUILog.TAG_SERVICE, "XUIService onDestroy");
        IXUIServiceImpl.releaseInstance();
        super.onDestroy();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        return 1;
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.mIXUIServiceImpl;
    }

    @Override // android.app.Service
    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        if (args != null) {
            while (0 < args.length) {
                String str = args[0];
                char c = 65535;
                int hashCode = str.hashCode();
                if (hashCode != 1504) {
                    if (hashCode == 214175961 && str.equals("-module")) {
                        c = 1;
                    }
                } else if (str.equals("-m")) {
                    c = 0;
                }
                if (c != 0) {
                    if (c == 1) {
                    }
                }
                if (0 < args.length - 1) {
                    int opti = 0 + 1;
                    String module = args[opti];
                    DumpDispatcher.dumpModule(writer, module, args, opti + 1);
                    return;
                }
                writer.println("lack module name...");
                return;
            }
        }
        writer.println("*dump XUIService*");
        writer.println("*dump HAL*");
        writer.println("*dump services*");
        IXUIServiceImpl.getInstance(this).dump(writer);
    }
}
