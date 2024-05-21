package com.xiaopeng.xuiservice.iot;

import android.app.ActivityThread;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.ServiceManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes5.dex */
public class IoTService extends Service {
    private static final String TAG = IoTService.class.getSimpleName() + "##";
    private IoTManagerService mIoTManagerService;

    @Override // android.app.Service
    public void onCreate() {
        LogUtil.d(TAG, "onCreate");
        this.mIoTManagerService = new IoTManagerService(ActivityThread.currentActivityThread().getApplication());
        try {
            ServiceManager.addService(IoTManagerService.class.getSimpleName(), this.mIoTManagerService);
        } catch (Exception e) {
            String str = TAG;
            LogUtil.e(str, "fail addService:" + IoTManagerService.class.getSimpleName());
        }
        super.onCreate();
    }

    @Override // android.app.Service
    public void onDestroy() {
        LogUtil.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        return 1;
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        String opt;
        writer.println("dump " + TAG);
        int opti = 0;
        while (opti < args.length && (opt = args[opti]) != null && opt.length() > 0) {
            opti++;
            if ("-h".equals(opt)) {
                DumpDispatcher.dumpHelp(writer);
                return;
            } else if ("-m".equals(opt) || "-module".equals(opt)) {
                if (opti < args.length) {
                    String opt2 = args[opti];
                    opti++;
                    if (opt2 != null) {
                        DumpDispatcher.dumpModule(writer, opt2, args, opti);
                        return;
                    }
                } else {
                    continue;
                }
            } else {
                writer.println("Unknown argument: " + opt + "; use -h for help");
            }
        }
    }
}
