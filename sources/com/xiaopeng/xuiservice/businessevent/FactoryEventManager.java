package com.xiaopeng.xuiservice.businessevent;

import android.car.diagnostic.XpDiagnosticManager;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.mcu.CarMcuManager;
import android.os.SystemProperties;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import java.io.PrintWriter;
import java.util.HashMap;
import kotlin.UByte;
/* loaded from: classes5.dex */
public class FactoryEventManager {
    private static final int FACTORY_BINARY_VALUE = 6;
    private static final String SYSTEM_PROPERTY_SPECIAL = "ro.xiaopeng.special";
    private static final String notifyPackageName = "com.xiaopeng.factory";
    private static final String TAG = FactoryEventManager.class.getSimpleName();
    private static final byte[] targetSignalBytes = {16, 96};
    private static final byte[] MSG_IM_READY = {82, 69, 65, 68, 89};

    /* loaded from: classes5.dex */
    private static class FactoryEventManagerHolder {
        private static final FactoryEventManager sInstance = new FactoryEventManager();

        private FactoryEventManagerHolder() {
        }
    }

    private FactoryEventManager() {
        addCanSignalListener();
    }

    public static FactoryEventManager getInstance() {
        return FactoryEventManagerHolder.sInstance;
    }

    public void init() {
        BroadcastManager.getInstance().addBootCompleteTask(new Runnable() { // from class: com.xiaopeng.xuiservice.businessevent.-$$Lambda$FactoryEventManager$diiYr5hnBSTDWWKIyE-a2Lz9TA8
            @Override // java.lang.Runnable
            public final void run() {
                FactoryEventManager.this.lambda$init$0$FactoryEventManager();
            }
        });
    }

    public /* synthetic */ void lambda$init$0$FactoryEventManager() {
        sendReadyMsg();
        factoryTryLaunch();
    }

    public void dump(PrintWriter pw, String[] args) {
        String str = TAG;
        LogUtil.i(str, "dump " + TAG);
        HashMap<String, String> map = new HashMap<>();
        map.put("key1", TAG);
        map.put("key2", "val2");
        map.put("key3", "val3");
        BusinessEventManager.getInstance().notifyApp("com.example.test", map);
    }

    private void addCanSignalListener() {
        CarClientManager.getInstance().addXpDiagnosticManagerListener(new XpDiagnosticManager.XpDiagnosticEventCallback() { // from class: com.xiaopeng.xuiservice.businessevent.FactoryEventManager.1
            public void onChangeEvent(CarPropertyValue value) {
                FactoryEventManager.this.handlePropertyChange(value);
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePropertyChange(CarPropertyValue value) {
        if (value.getPropertyId() == 560993284) {
            Object data = value.getValue();
            if ((data instanceof byte[]) && checkSignal((byte[]) data)) {
                BusinessEventManager.getInstance().notifyApp(notifyPackageName, null);
            }
        }
    }

    private boolean checkSignal(byte[] bytes) {
        if (bytes != null) {
            String str = TAG;
            LogUtil.d(str, "ID_MCU_DIAG_REQUEST, value: " + byte2hex(bytes));
            int i = 0;
            while (true) {
                byte[] bArr = targetSignalBytes;
                if (i >= bArr.length) {
                    return true;
                }
                if (bytes[i] == bArr[i]) {
                    i++;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    private String byte2hex(byte[] b) {
        if (b == null) {
            return "**";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("");
        for (byte b2 : b) {
            String stmp = Integer.toHexString(b2 & UByte.MAX_VALUE);
            if (stmp.length() == 1) {
                sb.append("0");
                sb.append(stmp);
            } else {
                sb.append(stmp);
            }
        }
        return sb.toString();
    }

    public static boolean isFactoryBinary() {
        int special = SystemProperties.getInt(SYSTEM_PROPERTY_SPECIAL, 1);
        String str = TAG;
        LogUtil.d(str, "isFactoryBinary special = " + special);
        return special == 6;
    }

    private void sendReadyMsg() {
        if (!isFactoryBinary()) {
            LogUtil.i(TAG, "send ready msg");
            XpDiagnosticManager mXpDiagnosticManager = CarClientManager.getInstance().getCarManager("xp_diagnostic");
            if (mXpDiagnosticManager != null) {
                try {
                    mXpDiagnosticManager.sendMcuDiagCmdAck(convert64Byte(MSG_IM_READY));
                    return;
                } catch (Exception e) {
                    String str = TAG;
                    LogUtil.w(str, "sendMcuDiagCmdAck e=" + e);
                    return;
                }
            }
            LogUtil.w(TAG, "sendMcuDiagCmdAck fail,manager is null");
        }
    }

    private static byte[] convert64Byte(byte[] buf) {
        byte[] bytes64 = new byte[64];
        int index = 0;
        while (index < buf.length && index < 64) {
            bytes64[index] = buf[index];
            index++;
        }
        while (index < 64) {
            bytes64[index] = 0;
            index++;
        }
        return bytes64;
    }

    private void factoryTryLaunch() {
        CarMcuManager manager = CarClientManager.getInstance().getCarManager("xp_mcu");
        try {
            int status = manager.getFactoryModeSwitchStatus();
            String str = TAG;
            LogUtil.i(str, "factoryTryLaunch, factory status=" + status);
            if (1 == status) {
                BusinessEventManager.getInstance().notifyApp(notifyPackageName, null);
            }
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "factoryTryLaunch e=" + e);
        }
    }
}
