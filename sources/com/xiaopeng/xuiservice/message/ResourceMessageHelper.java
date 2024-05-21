package com.xiaopeng.xuiservice.message;

import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.message.PushMessage;
import com.xiaopeng.xuiservice.message.ResourceMessageHelper;
import com.xiaopeng.xuiservice.utils.CommonUtils;
import java.util.function.Consumer;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class ResourceMessageHelper {
    private static final String FILTER_TYPE_ALL_VEHICLE = "0";
    private static final String FILTER_TYPE_SINGLE_VEHICLE = "2";
    private static final String FILTER_TYPE_VEHICLE_GROUP = "1";
    private static final String TAG = ResourceMessageHelper.class.getSimpleName();
    private final ArrayMap<String, ArraySet<ResourceUpdateListener>> mListenerMap;
    private final PushMessage.PushMessageListener pushMessageListener;

    /* loaded from: classes5.dex */
    public interface ResourceUpdateListener {
        void onResourceUpdated(String str);
    }

    public /* synthetic */ void lambda$new$0$ResourceMessageHelper(String messageType, String data) {
        try {
            JSONObject dataJson = new JSONObject(data);
            String category_code = dataJson.getString("category_code");
            if (!TextUtils.isEmpty(category_code) && filterMinRomVersion(dataJson) && filterPushTarget(dataJson)) {
                notifyResourceUpdate(category_code);
            } else {
                LogUtil.d(TAG, "drop resource message");
            }
        } catch (Exception e) {
            String str = TAG;
            LogUtil.i(str, "parse resource message error, " + e.getMessage());
            String str2 = TAG;
            LogUtil.i(str2, "error json: " + data);
        }
    }

    public static ResourceMessageHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* loaded from: classes5.dex */
    private static class SingletonHolder {
        private static final ResourceMessageHelper INSTANCE = new ResourceMessageHelper();

        private SingletonHolder() {
        }
    }

    private ResourceMessageHelper() {
        this.mListenerMap = new ArrayMap<>();
        this.pushMessageListener = new PushMessage.PushMessageListener() { // from class: com.xiaopeng.xuiservice.message.-$$Lambda$ResourceMessageHelper$5DVquQdBgKwH_lTV1afp-xGgJXc
            @Override // com.xiaopeng.xuiservice.message.PushMessage.PushMessageListener
            public final void onReceivePushMessage(String str, String str2) {
                ResourceMessageHelper.this.lambda$new$0$ResourceMessageHelper(str, str2);
            }
        };
    }

    public synchronized void registerResourceUpdateListener(String resourceType, ResourceUpdateListener listener) {
        synchronized (this.mListenerMap) {
            ArraySet<ResourceUpdateListener> listeners = this.mListenerMap.get(resourceType);
            if (listeners == null) {
                listeners = new ArraySet<>();
                this.mListenerMap.put(resourceType, listeners);
            }
            if (getListenerCount() == 0) {
                PushMessage.getInstance().registerMessage("001", this.pushMessageListener);
            }
            listeners.add(listener);
        }
    }

    public synchronized void unregisterResourceUpdateListener(String resourceType, ResourceUpdateListener listener) {
        synchronized (this.mListenerMap) {
            ArraySet<ResourceUpdateListener> listeners = this.mListenerMap.get(resourceType);
            if (listeners != null) {
                listeners.remove(listener);
                if (getListenerCount() == 0) {
                    PushMessage.getInstance().unregisterMessage("001", this.pushMessageListener);
                }
            }
        }
    }

    private int getListenerCount() {
        int count = 0;
        for (String key : this.mListenerMap.keySet()) {
            ArraySet<ResourceUpdateListener> listeners = this.mListenerMap.get(key);
            if (listeners != null) {
                count += listeners.size();
            }
        }
        return count;
    }

    private void notifyResourceUpdate(final String resourceType) {
        synchronized (this.mListenerMap) {
            ArraySet<ResourceUpdateListener> listeners = this.mListenerMap.get(resourceType);
            if (listeners != null) {
                listeners.forEach(new Consumer() { // from class: com.xiaopeng.xuiservice.message.-$$Lambda$ResourceMessageHelper$p83nh_zQDDCRRPYCcHv7Owo6YBc
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((ResourceMessageHelper.ResourceUpdateListener) obj).onResourceUpdated(resourceType);
                    }
                });
            }
        }
    }

    private boolean filterPushTarget(JSONObject data) throws JSONException {
        JSONObject push_target = data.getJSONObject("push_target");
        String push_to_vehicle = push_target.getString("push_to_vehicle");
        if ("0".equals(push_to_vehicle)) {
            return true;
        }
        if ("1".equals(push_to_vehicle) || "2".equals(push_to_vehicle)) {
            String vins = push_target.getString("vins");
            if (TextUtils.isEmpty(vins)) {
                return false;
            }
            String myVin = getVIN();
            if (TextUtils.isEmpty(myVin)) {
                return false;
            }
            String[] vinArr = vins.split(",");
            for (String str : vinArr) {
                if (myVin.equals(str)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private static String getVIN() {
        String vin = SystemProperties.get("persist.sys.xiaopeng.vin");
        if (TextUtils.isEmpty(vin)) {
            return SystemProperties.get("sys.xiaopeng.vin");
        }
        return vin;
    }

    private static boolean filterMinRomVersion(JSONObject data) throws JSONException {
        String min_rom_version = data.getString("min_rom_version");
        if (TextUtils.isEmpty(min_rom_version)) {
            return true;
        }
        int[] minRomVersion = parseRomVersion(min_rom_version);
        int[] romVersion = getCurrentRomVersion();
        if (minRomVersion == null || romVersion == null) {
            return false;
        }
        return checkRomVersion(romVersion, minRomVersion, 0);
    }

    private static int[] parseRomVersion(String serverRomVersion) {
        String[] split = serverRomVersion.split("\\.");
        if (split.length != 4) {
            return null;
        }
        int[] versions = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            try {
                versions[i] = Integer.parseInt(split[i]);
            } catch (Exception e) {
                String str = TAG;
                LogUtil.e(str, "parse server version error, version = " + serverRomVersion + ", error = " + e.getMessage());
                return null;
            }
        }
        return versions;
    }

    private static int[] getCurrentRomVersion() {
        int index1;
        int index2;
        String info = SystemProperties.get(CommonUtils.HARDWARE_SOFTWARE_VERSION_PROPERTY);
        if (TextUtils.isEmpty(info) || (index1 = info.indexOf("_V")) == -1 || (index2 = info.indexOf("_", index1 + 2)) == -1) {
            return null;
        }
        String versionStr = info.substring(index1 + 2, index2);
        return parseRomVersion(versionStr);
    }

    private static boolean checkRomVersion(int[] romVersion, int[] minRomVersion, int index) {
        if (index >= romVersion.length) {
            return true;
        }
        if (romVersion[index] < minRomVersion[index]) {
            return false;
        }
        if (romVersion[index] > minRomVersion[index]) {
            return true;
        }
        return checkRomVersion(romVersion, minRomVersion, index + 1);
    }
}
