package com.xiaopeng.lib.framework.netchannelmodule.common;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import com.alipay.mobile.aromeservice.RequestParams;
import com.lzy.okgo.model.Progress;
import com.xiaopeng.xuiservice.xapp.Constants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class TrafficStatsEntry {
    public static final int FIRST_NETWORK_UID = 100000;
    public static final int LAST_NETWORK_UID = 110000;
    public static final String TAG = "TrafficStatsEntry";
    public static final int TAG_APP_AUTOPILOT = -16449537;
    public static final int TAG_APP_AUTO_SHOW = -14483457;
    public static final int TAG_APP_AVATAR_SERVICE = -15663105;
    public static final int TAG_APP_BLUETOOTH_PHONE = -16121857;
    public static final int TAG_APP_BUG_HUNTER = -14548993;
    public static final int TAG_APP_CAR_ACCOUNT = -15335425;
    public static final int TAG_APP_CAR_CAMERA = -15532033;
    public static final int TAG_APP_CAR_CONTROL = -15269889;
    public static final int TAG_APP_CAR_DIAGNOSIS = -14352385;
    public static final int TAG_APP_CAR_GALLERY = -15466497;
    public static final int TAG_APP_CAR_REMOTE_CONTROL = -15073281;
    public static final int TAG_APP_CAR_SERVICE = -15400961;
    public static final int TAG_APP_CAR_SETTINGS = -15204353;
    public static final int TAG_APP_CAR_SPEECH_SERVICE = -15138817;
    public static final int TAG_APP_DATA_COLLECTOR = -16252929;
    public static final int TAG_APP_DATA_UPLOADER = -16187393;
    public static final int TAG_APP_DEVICE_COMMUNICATION = -15597569;
    public static final int TAG_APP_DEV_TOOLS = -16384001;
    public static final int TAG_APP_ENGINE = -16515073;
    public static final int TAG_APP_FACTORY = -14417921;
    public static final int TAG_APP_IPC = -14614529;
    public static final int TAG_APP_NETWORK_MONITOR = -14286849;
    public static final int TAG_APP_OOBE = -16580609;
    public static final int TAG_APP_OTA = -16646145;
    public static final int TAG_APP_PSO_SERVICE = -14221313;
    public static final int TAG_APP_XUI_SERVICE = -16318465;
    private static final HashMap<String, Entry> sEntry = new HashMap<>();

    static {
        sEntry.clear();
        sEntry.put(Constants.PACKAGE_OTA, new Entry(100000, TAG_APP_OTA));
        sEntry.put("com.xiaopeng.oobe", new Entry(100001, TAG_APP_OOBE));
        sEntry.put("com.xiaopeng.appengine", new Entry(100002, TAG_APP_ENGINE));
        sEntry.put("com.xiaopeng.autopilot", new Entry(100003, TAG_APP_AUTOPILOT));
        sEntry.put("com.xiaopeng.devtools", new Entry(100004, TAG_APP_DEV_TOOLS));
        sEntry.put("com.xiaopeng.xuiservice", new Entry(100005, TAG_APP_XUI_SERVICE));
        sEntry.put("com.xiaopeng.data.collector", new Entry(100006, TAG_APP_DATA_COLLECTOR));
        sEntry.put("com.xiaopeng.data.uploader", new Entry(100007, TAG_APP_DATA_UPLOADER));
        sEntry.put("com.xiaopeng.btphone", new Entry(100008, TAG_APP_BLUETOOTH_PHONE));
        sEntry.put("com.xiaopeng.aiavatarservice", new Entry(100009, TAG_APP_AVATAR_SERVICE));
        sEntry.put("com.xiaopeng.device.communication", new Entry(100010, TAG_APP_DEVICE_COMMUNICATION));
        sEntry.put("com.xiaopeng.xmart.camera", new Entry(100011, TAG_APP_CAR_CAMERA));
        sEntry.put("com.xiaopeng.xmart.cargallery", new Entry(100012, TAG_APP_CAR_GALLERY));
        sEntry.put("com.android.car", new Entry(100013, TAG_APP_CAR_SERVICE));
        sEntry.put(Constants.PACKAGE_CAR_ACCOUNT, new Entry(100014, TAG_APP_CAR_ACCOUNT));
        sEntry.put(Constants.PACKAGE_CAR_CONTROL, new Entry(100015, TAG_APP_CAR_CONTROL));
        sEntry.put("com.xiaopeng.car.settings", new Entry(100016, TAG_APP_CAR_SETTINGS));
        sEntry.put("com.xiaopeng.xpspeechservice", new Entry(100017, TAG_APP_CAR_SPEECH_SERVICE));
        sEntry.put("com.xpeng.xpcarremotecontrol", new Entry(100018, TAG_APP_CAR_REMOTE_CONTROL));
        sEntry.put("com.xiaopeng.ipc", new Entry(100019, TAG_APP_IPC));
        sEntry.put("com.xiaopeng.bughunter", new Entry(100020, TAG_APP_BUG_HUNTER));
        sEntry.put("com.xiaopeng.autoshow", new Entry(100021, TAG_APP_AUTO_SHOW));
        sEntry.put("com.xiaopeng.factory", new Entry(100022, TAG_APP_FACTORY));
        sEntry.put("com.xiaopeng.cardiagnosis", new Entry(100023, TAG_APP_CAR_DIAGNOSIS));
        sEntry.put("com.xiaopeng.networkmonitor", new Entry(100024, TAG_APP_NETWORK_MONITOR));
        sEntry.put("android.E28psoService", new Entry(100025, TAG_APP_PSO_SERVICE));
    }

    public static int getTag(String packageName) {
        Entry entry = getEntry(packageName);
        if (entry != null) {
            return entry.tag;
        }
        return -1;
    }

    public static int getUid(String packageName) {
        Entry entry = getEntry(packageName);
        if (entry != null) {
            return entry.uid;
        }
        return -1;
    }

    public static String getPackageName(int tag, int uid) {
        HashMap<String, Entry> hashMap = sEntry;
        if (hashMap != null) {
            for (String key : hashMap.keySet()) {
                Entry entry = sEntry.get(key);
                if (entry != null && (entry.tag == tag || entry.uid == uid)) {
                    return key;
                }
            }
            return null;
        }
        return null;
    }

    public static boolean isEntryTag(int tag) {
        HashMap<String, Entry> hashMap = sEntry;
        if (hashMap != null) {
            for (String key : hashMap.keySet()) {
                Entry entry = sEntry.get(key);
                if (entry != null && entry.tag == tag) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static boolean isEntryUid(int uid) {
        HashMap<String, Entry> hashMap = sEntry;
        if (hashMap != null) {
            for (String key : hashMap.keySet()) {
                Entry entry = sEntry.get(key);
                if (entry != null && entry.uid == uid) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private static Entry getEntry(String packageName) {
        HashMap<String, Entry> hashMap;
        if (!TextUtils.isEmpty(packageName) && (hashMap = sEntry) != null && hashMap.containsKey(packageName)) {
            return sEntry.get(packageName);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class Entry {
        public int tag;
        public int uid;

        public Entry(int uid, int tag) {
            this.uid = uid;
            this.tag = tag;
        }
    }

    @RequiresApi(api = 28)
    private static void set(int uid, int tag) {
        TrafficStats.getAndSetThreadStatsTag(tag);
        TrafficStats.setThreadStatsUid(uid);
    }

    public static List<HashMap<String, Object>> getTrafficInfo() {
        String str;
        String str2 = RequestParams.REQUEST_KEY_PACKAGE_NAME;
        List<HashMap<String, Object>> list = new ArrayList<>();
        try {
            File file = new File("/system/etc/xp_traffic_stats_maps.json");
            if (file.exists()) {
                StringBuilder builder = new StringBuilder();
                InputStreamReader sr = new InputStreamReader(new FileInputStream(file));
                BufferedReader br = new BufferedReader(sr);
                while (true) {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    builder.append(line);
                }
                br.close();
                sr.close();
                JSONObject jObject = new JSONObject(builder.toString());
                JSONArray jArray = jObject.getJSONArray("data");
                if (jArray != null) {
                    int length = jArray.length();
                    int i = 0;
                    while (i < length) {
                        JSONObject object = jArray.getJSONObject(i);
                        HashMap<String, Object> map = new HashMap<>();
                        if (object.has(str2)) {
                            String packageName = object.getString(str2);
                            map.put(str2, packageName);
                        }
                        if (!object.has("uid")) {
                            str = str2;
                        } else {
                            int uid = object.getInt("uid");
                            str = str2;
                            map.put("uid", Integer.valueOf(uid));
                        }
                        if (object.has(Progress.TAG)) {
                            String tag = object.getString(Progress.TAG);
                            map.put(Progress.TAG, tag);
                        }
                        list.add(map);
                        i++;
                        str2 = str;
                    }
                }
            }
        } catch (Exception e) {
        }
        return list;
    }

    @RequiresApi(api = 28)
    public static void setTraficInfo() {
        Context context;
        int version = Build.VERSION.SDK_INT;
        if (version < 26 || (context = ContextNetStatusProvider.getApplicationContext()) == null) {
            return;
        }
        String packageName = context.getPackageName();
        int tag = getTag(packageName);
        int uid = getUid(packageName);
        Log.i(TAG, "setTraficInfo:\t" + packageName + "\ttag:" + tag + "\tuid:" + uid);
        set(uid, tag);
    }
}
