package com.xiaopeng.xuiservice.pipebus;

import android.content.Context;
import android.os.Process;
import com.xiaopeng.xuimanager.pipebus.IPipeBus;
import com.xiaopeng.xuimanager.pipebus.IPipeBusListener;
import com.xiaopeng.xuimanager.pipebus.ParcelableContainer;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes5.dex */
public class PipeBusManagerService extends IPipeBus.Stub {
    private static final String TAG = PipeBusManagerService.class.getSimpleName() + "##";
    private Context mContext;

    public PipeBusManagerService(Context context) {
        this.mContext = context;
        LogUtil.setModuleLogLevel(TAG, 0);
        LogUtil.d(TAG, "create PipeBusManagerService");
    }

    public int ioControl(String module, String cmd, String[] params) {
        String str = TAG;
        LogUtil.d(str, "ioControl,module=" + module + ",cmd=" + cmd + ",params=" + params);
        return 0;
    }

    public int ioControlWithPocket(String module, String cmd, String[] params, String[] results) {
        String str = TAG;
        LogUtil.d(str, "ioControlWithPocket,module=" + module + ",cmd=" + cmd + ",params=" + params);
        return 0;
    }

    public int ioControlWithParcelable(String moduleName, String cmd, ParcelableContainer dataIn, ParcelableContainer dataOut) {
        String str = TAG;
        LogUtil.i(str, "ioControlWithParcelable not implemented yet,mod:" + moduleName + ",cmd:" + cmd);
        return -1;
    }

    public void registerListenerEx(String moduleName, IPipeBusListener listener) {
        String str = TAG;
        LogUtil.i(str, "not implement registerListenerEx,mod:" + moduleName);
    }

    public void unRegisterListenerEx(String moduleName) {
        String str = TAG;
        LogUtil.i(str, "not implement unRegisterListenerEx,mod:" + moduleName);
    }

    public void registerListener(IPipeBusListener listener) {
        String str = TAG;
        LogUtil.d(str, "registerListener=" + listener);
    }

    public void unRegisterListener(IPipeBusListener listener) {
        String str = TAG;
        LogUtil.d(str, "unRegisterListener=" + listener);
    }

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println("dump " + getClass().getName());
        pw.println("thread-" + Process.myPid());
    }
}
