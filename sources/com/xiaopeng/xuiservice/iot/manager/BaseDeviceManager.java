package com.xiaopeng.xuiservice.iot.manager;

import com.xiaopeng.xuimanager.iot.BaseDevice;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
/* loaded from: classes5.dex */
public abstract class BaseDeviceManager {
    public abstract void addDevice(BaseDevice baseDevice);

    public abstract void addDeviceListener(IEventListener iEventListener);

    public abstract void dump(PrintWriter printWriter, String[] strArr);

    public abstract List<BaseDevice> getDevice();

    public abstract Map<String, String> getPropertyMap(String str);

    public abstract void init();

    public abstract boolean isEnabled();

    public abstract void monitorDevice(String str, boolean z);

    public abstract void sendCommand(String str, String str2, String str3);

    public abstract void setPropertyMap(String str, Map<String, String> map);

    public void addInnerDeviceListener(IEventListener listener) {
    }
}
