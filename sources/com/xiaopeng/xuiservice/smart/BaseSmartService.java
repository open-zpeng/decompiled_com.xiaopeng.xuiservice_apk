package com.xiaopeng.xuiservice.smart;

import android.content.Context;
import com.xiaopeng.xuiservice.carcontrol.BaseCarListener;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import java.io.PrintWriter;
import java.util.Arrays;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes5.dex */
public abstract class BaseSmartService extends BaseCarListener implements DumpDispatcher.DebugDumper {
    public BaseSmartService(Context context) {
        super(context);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected boolean needConnectCar() {
        return false;
    }

    public void dump(PrintWriter pw, String[] args) {
        pw.println("dump-" + getClass().getSimpleName() + ",args=" + Arrays.toString(args));
    }

    @Override // com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
    public void debugDump(PrintWriter pw, String[] args) {
        dump(pw, args);
    }
}
