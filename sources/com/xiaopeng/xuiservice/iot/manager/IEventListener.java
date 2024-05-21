package com.xiaopeng.xuiservice.iot.manager;

import com.xiaopeng.xuimanager.iot.BaseDevice;
import java.util.List;
/* loaded from: classes5.dex */
public interface IEventListener {
    void onDeviceEvent(String str, String str2, String str3);

    default void onDeviceAdd(List<BaseDevice> list) {
    }

    default void onDeviceEvent(String deviceId, String jsonString) {
    }
}
