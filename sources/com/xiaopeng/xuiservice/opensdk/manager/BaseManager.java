package com.xiaopeng.xuiservice.opensdk.manager;

import com.car.opensdk.pipebus.ParcelableData;
import java.io.PrintWriter;
/* loaded from: classes5.dex */
public abstract class BaseManager {
    public abstract void addListener(IPipeListener iPipeListener);

    public abstract void dump(PrintWriter printWriter, String[] strArr);

    public abstract String getModuleName();

    public abstract void init();

    public abstract int ioControl(String str, String[] strArr, int i, int i2);

    public abstract int ioControlWithPocket(String str, String[] strArr, String[] strArr2, int i, int i2);

    public abstract void removeListener();

    public boolean isEnabled() {
        return true;
    }

    public String arrayToString(Object[] array) {
        if (array == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Object o : array) {
            sb.append(o);
            sb.append("#");
        }
        return sb.toString();
    }

    public void onClientDied(int pid) {
    }

    public int ioControlWithParcelable(String cmd, ParcelableData dataIn, ParcelableData dataOut, int pid, int uid) {
        return 0;
    }

    public int ioControlWithBytes(String cmd, String[] params, byte[] bytesIn, byte[] bytesOut, int pid, int uid) {
        return 0;
    }
}
