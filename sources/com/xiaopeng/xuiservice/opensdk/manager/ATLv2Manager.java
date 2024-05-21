package com.xiaopeng.xuiservice.opensdk.manager;

import android.app.ActivityThread;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Pair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xiaopeng.xuimanager.XUIManager;
import com.xiaopeng.xuimanager.ambient.AmbientEffect;
import com.xiaopeng.xuimanager.ambient.AmbientManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightCapability;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import com.xiaopeng.xuiservice.smart.action.Actions;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class ATLv2Manager extends BaseManager {
    private static final String AMBIENT = "{\"name\":\"run_ambient\",\"count\":-1,\"packets\":[{\"groups\":[2, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24],\"colors\":[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20],\"bright\":100,\"fade\":3000,\"period\":1000}]}";
    private static final String BREATH = "{\"name\":\"run_breath\",\"count\":-1,\"packets\":[{\"groups\":[3],\"colors\":[1],\"bright\":100,\"fade\":3000,\"period\":3000},{\"groups\":[3],\"colors\":[1],\"bright\":5,\"fade\":3000,\"period\":3000}]}";
    private static final String COLORS = "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]";
    private static final String FLASH = "{\"name\":\"run_flash\",\"count\":-1,\"packets\":[{\"groups\":[3],\"colors\":[1],\"bright\":100,\"fade\":3000,\"period\":500},{\"groups\":[3],\"colors\":[1],\"bright\":5,\"fade\":3000,\"period\":500}]}";
    private static final String FRONT = "{\"name\":\"run_front\",\"count\":1,\"packets\":[{\"groups\":[38],\"colors\":[1],\"bright\":100,\"fade\":3000,\"period\":1000}]}";
    private static final String GRADIENT = "{\"name\":\"run_gradient\",\"count\":1,\"packets\":[{\"groups\":[3],\"colors\":[1],\"bright\":5,\"fade\":3000,\"period\":1000},{\"groups\":[3],\"colors\":[1],\"bright\":100,\"fade\":3000,\"period\":1000}]}";
    private static final String LEFT = "{\"name\":\"run_left\",\"count\":1,\"packets\":[{\"groups\":[39],\"colors\":[1],\"bright\":100,\"fade\":3000,\"period\":1000}]}";
    private static final String LEFTRIGHT = "{\"name\":\"run_music2\",\"count\":-1,\"hasRhythm\":true,\"packets\":[{\"groups\":[43],\"colors\":[1],\"bright\":100,\"fade\":3000,\"period\":1000}]}";
    private static final String LEFTTOP = "{\"name\":\"run_lefttop\",\"count\":1,\"packets\":[{\"groups\":[41],\"colors\":[1],\"bright\":100,\"fade\":3000,\"period\":1000}]}";
    private static final String MAIN = "{\"name\":\"run_main\",\"count\":1,\"packets\":[{\"groups\":[37],\"colors\":[1],\"bright\":100,\"fade\":3000,\"period\":1000}]}";
    private static final String MODULE_NAME = "AmbientLightManager";
    private static final String MUSIC = "{\"name\":\"run_music\",\"count\":-1,\"hasRhythm\":true,\"packets\":[{\"groups\":[3],\"colors\":[1],\"bright\":100,\"fade\":3000,\"period\":1000}]}";
    private static final String MUTIMYSTERY = "{\"name\":\"run_mutimystery\",\"count\":-1,\"packets\":[{\"groups\":[3],\"colors\":[1],\"bright\":100,\"fade\":3000,\"period\":1000},{\"groups\":[111, 112, 113, 115, 116],\"colors\":[1],\"bright\":100,\"fade\":150,\"period\":1000}]}";
    private static final String MYSTERY = "{\"name\":\"run_mystery\",\"count\":-1,\"packets\":[{\"groups\":[101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119],\"colors\":[3],\"bright\":100,\"fade\":3000,\"period\":500}]}";
    private static final String RIGHT = "{\"name\":\"run_right\",\"count\":1,\"packets\":[{\"groups\":[40],\"colors\":[1],\"bright\":100,\"fade\":3000,\"period\":1000}]}";
    private static final String RIGHTTOP = "{\"name\":\"run_righttop\",\"count\":1,\"packets\":[{\"groups\":[42],\"colors\":[1],\"bright\":100,\"fade\":3000,\"period\":1000}]}";
    private static final String SPEED = "{\"name\":\"run_speed\",\"count\":-1,\"packets\":[{\"groups\":[3],\"colors\":[1],\"bright\":-1,\"fade\":3000,\"period\":1000}]}";
    private static final String STABLE = "{\"name\":\"run_stable\",\"count\":-1,\"packets\":[{\"groups\":[3],\"colors\":[1],\"bright\":100,\"fade\":3000,\"period\":3000}]}";
    private static final String TURNOFF = "{\"name\":\"run_turnoff\",\"count\":1,\"packets\":[{\"groups\":[0],\"colors\":[0],\"bright\":0,\"fade\":0,\"period\":1000}]}";
    private static XUIManager mXUIManager;
    private AmbientManager mManager;
    private static final String TAG = ATLv2Manager.class.getSimpleName();
    private static final Map<String, Pair<String, String>> mEffectMap = new HashMap<String, Pair<String, String>>() { // from class: com.xiaopeng.xuiservice.opensdk.manager.ATLv2Manager.1
        {
            put("effect_stable_effect_new", new Pair(ATLv2Manager.STABLE, "-1"));
            put("effect_gentle_breathing", new Pair(ATLv2Manager.BREATH, "2"));
            put("effect_follow_speed", new Pair(ATLv2Manager.SPEED, "1"));
            put("effect_music", new Pair(ATLv2Manager.MUSIC, "1"));
            put("effect_flashing_script_effect", new Pair(ATLv2Manager.FLASH, "-1"));
            put("effect_turn_off", new Pair(ATLv2Manager.TURNOFF, "0"));
            put("effect_gradient_light", new Pair(ATLv2Manager.GRADIENT, "2"));
            put("effect_mystery", new Pair(ATLv2Manager.MYSTERY, "1"));
            put("effect_multi_mystery", new Pair(ATLv2Manager.MUTIMYSTERY, "1"));
            put("effect_ambilight", new Pair(ATLv2Manager.AMBIENT, "0"));
            put("effect_main", new Pair(ATLv2Manager.MAIN, "1"));
            put("effect_front", new Pair(ATLv2Manager.FRONT, "1"));
            put("effect_left", new Pair(ATLv2Manager.LEFT, "1"));
            put("effect_right", new Pair(ATLv2Manager.RIGHT, "1"));
            put("effect_left_top", new Pair(ATLv2Manager.LEFTTOP, "1"));
            put("effect_right_top", new Pair(ATLv2Manager.RIGHTTOP, "1"));
            put("effect_music_rhythm_left_right", new Pair(ATLv2Manager.LEFTRIGHT, "1"));
        }
    };
    private static ATLv2Manager instance = null;
    private static IPipeListener mPipeListener = null;
    private static Map<Integer, Integer> mUidMap = new ConcurrentHashMap();
    private static boolean hasOld = !XUIConfig.isAmbientNewService();

    private ATLv2Manager() {
        if (mXUIManager == null) {
            mXUIManager = XUIManager.createXUIManager(ActivityThread.currentActivityThread().getApplication(), new ServiceConnection() { // from class: com.xiaopeng.xuiservice.opensdk.manager.ATLv2Manager.2
                @Override // android.content.ServiceConnection
                public void onServiceConnected(ComponentName name, IBinder service) {
                    LogUtil.i(ATLv2Manager.TAG, "onServiceConnected");
                    try {
                        ATLv2Manager.this.mManager = (AmbientManager) ATLv2Manager.mXUIManager.getXUIServiceManager(Actions.ACTION_AMBIENT);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override // android.content.ServiceConnection
                public void onServiceDisconnected(ComponentName name) {
                    ATLv2Manager.this.mManager = null;
                }
            });
            mXUIManager.connect();
            DumpDispatcher.registerDump(Actions.ACTION_AMBIENT, new DumpDispatcher.DebugDumper() { // from class: com.xiaopeng.xuiservice.opensdk.manager.-$$Lambda$ATLv2Manager$ujxg5Rhp1IfnjBcfdVt_TnikmeI
                @Override // com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
                public final void debugDump(PrintWriter printWriter, String[] strArr) {
                    ATLv2Manager.this.lambda$new$0$ATLv2Manager(printWriter, strArr);
                }
            });
        }
    }

    public static ATLv2Manager getInstance() {
        if (instance == null) {
            synchronized (ATLv2Manager.class) {
                if (instance == null) {
                    instance = new ATLv2Manager();
                }
            }
        }
        return instance;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void init() {
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public int ioControl(String cmd, String[] params, int pid, int uid) {
        String str = TAG;
        LogUtil.d(str, "ioControl, cmd=" + cmd + ", params=" + arrayToString(params));
        mUidMap.put(Integer.valueOf(uid), Integer.valueOf(pid));
        return handleIoControl(cmd, params);
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public int ioControlWithPocket(String cmd, String[] params, String[] results, int pid, int uid) {
        String str = TAG;
        LogUtil.d(str, "ioControlWithPocket, cmd=" + cmd + ", params=" + arrayToString(params));
        mUidMap.put(Integer.valueOf(uid), Integer.valueOf(pid));
        return handleIoControlWithPocket(cmd, params, results);
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public synchronized void addListener(IPipeListener listener) {
        String str = TAG;
        LogUtil.d(str, "addListener, listener=" + listener);
        mPipeListener = listener;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public synchronized void removeListener() {
        LogUtil.d(TAG, "removeListener");
        mPipeListener = null;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void onClientDied(int pid) {
        for (Integer num : mUidMap.keySet()) {
            int uid = num.intValue();
            if (pid == mUidMap.get(Integer.valueOf(uid)).intValue()) {
                String str = TAG;
                LogUtil.w(str, "onClientDied, pid=" + pid + ", releaseAmbientLightPermission");
                requestAmbientLightPermission(new String[]{"0"});
                return;
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    /* renamed from: dump */
    public void lambda$new$0$ATLv2Manager(PrintWriter pw, String[] args) {
        String str;
        pw.println("*dump-" + TAG);
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                String str2 = args[i];
                char c = 65535;
                int hashCode = str2.hashCode();
                if (hashCode != -1813292817) {
                    if (hashCode != -870484091) {
                        if (hashCode == 492151251 && str2.equals("--stopEffect")) {
                            c = 2;
                        }
                    } else if (str2.equals("--playEffect")) {
                        c = 1;
                    }
                } else if (str2.equals("--request")) {
                    c = 0;
                }
                str = "succeed";
                if (c != 0) {
                    if (c == 1) {
                        String[] params = {"{\"color\":{\"color\":[1,2]},\"bright\":{\"bright\":[80,5]},\"fade\":{\"fade\":[150]},\"executeTime\":10000,\"timeInterval\":1000}", "effect_gentle_breathing"};
                        int ret = playAmbientLightEffect(params);
                        StringBuilder sb = new StringBuilder();
                        sb.append("playEffect ");
                        sb.append(ret != 0 ? "failed" : "succeed");
                        pw.println(sb.toString());
                    } else if (c == 2) {
                        int ret2 = stopAmbientLightEffect();
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("stopEffect ");
                        sb2.append(ret2 != 0 ? "failed" : "succeed");
                        pw.println(sb2.toString());
                    }
                } else if (i < args.length - 1) {
                    try {
                        String apply = Integer.parseInt(args[i + 1]) == 1 ? "apply" : "release";
                        int ret3 = requestAmbientLightPermission(new String[]{args[i + 1]});
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("request ");
                        sb3.append(apply);
                        sb3.append(" ");
                        if (ret3 != 0) {
                            str = "failed";
                        }
                        sb3.append(str);
                        pw.println(sb3.toString());
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    private int handleIoControl(String cmd, String[] params) {
        if (((cmd.hashCode() == -208470825 && cmd.equals("ambientRequest")) ? (char) 0 : (char) 65535) != 0) {
            return 0;
        }
        return requestAmbientLightPermission(params);
    }

    private int handleIoControlWithPocket(String cmd, String[] params, String[] results) {
        char c;
        int hashCode = cmd.hashCode();
        if (hashCode == -2129411402) {
            if (cmd.equals("startPlay")) {
                c = 0;
            }
            c = 65535;
        } else if (hashCode != 829307466) {
            if (hashCode == 1714697814 && cmd.equals("stopPlay")) {
                c = 2;
            }
            c = 65535;
        } else {
            if (cmd.equals("pausePlay")) {
                c = 1;
            }
            c = 65535;
        }
        if (c == 0) {
            results[0] = String.valueOf(playAmbientLightEffect(params));
        } else if (c == 1 || c == 2) {
            results[0] = String.valueOf(stopAmbientLightEffect());
        }
        return 0;
    }

    private int requestAmbientLightPermission(String[] params) {
        if (params != null) {
            try {
                if (params.length > 0) {
                    return this.mManager.requestAmbientLightPermission("1".equals(params[0]));
                }
                return -1;
            } catch (Exception e) {
                String str = TAG;
                LogUtil.e(str, "requestAmbientLightPermission failed " + e);
                return -1;
            }
        }
        return -1;
    }

    private int playAmbientLightEffect(String[] params) {
        int[] color;
        JSONObject first;
        if (params != null) {
            try {
                if (params.length > 0) {
                    JSONObject param = new JSONObject(params[0]);
                    String name = params[1];
                    if (hasOld) {
                        String str = TAG;
                        LogUtil.d(str, "playAmbientLightEffect, effectInfo=" + params[0] + ", effect=" + name);
                        return this.mManager.playAmbientLightEffect(params[0], name);
                    } else if (mEffectMap.containsKey(name)) {
                        Gson gson = new GsonBuilder().create();
                        Pair<String, String> element = mEffectMap.get(name);
                        JSONObject obj = new JSONObject((String) element.first);
                        JSONArray packets = obj.getJSONArray("packets");
                        JSONObject first2 = (JSONObject) packets.get(0);
                        int limit = Integer.valueOf((String) element.second).intValue();
                        int[] color2 = (int[]) gson.fromJson(param.getJSONObject("color").getJSONArray("color").toString(), (Class<Object>) int[].class);
                        int[] bright = (int[]) gson.fromJson(param.getJSONObject("bright").getJSONArray("bright").toString(), (Class<Object>) int[].class);
                        int[] fade = (int[]) gson.fromJson(param.getJSONObject("fade").getJSONArray("fade").toString(), (Class<Object>) int[].class);
                        int period = param.getInt(AmbientLightCapability.TIME_INTERVAL);
                        int len = Math.max(Math.max(color2.length, bright.length), fade.length);
                        if (limit != 0) {
                            if (limit > 0) {
                                len = Math.min(limit, len);
                            }
                            int i = 0;
                            while (i < len) {
                                int limit2 = limit;
                                try {
                                    boolean append = i >= packets.length();
                                    JSONObject packet = append ? new JSONObject() : (JSONObject) packets.get(i);
                                    int len2 = len;
                                    Pair<String, String> element2 = element;
                                    if (packet.getJSONArray("colors").length() > 1) {
                                        color = color2;
                                    } else {
                                        int data = color2.length > i ? color2[i] : color2[0];
                                        if (data == -1) {
                                            color = color2;
                                            packet.put("colors", new JSONArray(COLORS));
                                        } else {
                                            color = color2;
                                            JSONArray array = new JSONArray();
                                            packet.put("colors", array.put(data));
                                        }
                                    }
                                    packet.put("bright", bright.length > i ? bright[i] : bright[0]);
                                    packet.put("fade", (fade.length > i ? fade[i] : fade[0]) * 20);
                                    packet.put("period", period);
                                    if (!append) {
                                        first = first2;
                                    } else {
                                        first = first2;
                                        packet.put("groups", first.getJSONArray("groups"));
                                        packets.put(packet);
                                    }
                                    i++;
                                    first2 = first;
                                    limit = limit2;
                                    len = len2;
                                    color2 = color;
                                    element = element2;
                                } catch (Exception e) {
                                    e = e;
                                    String str2 = TAG;
                                    LogUtil.e(str2, "playAmbientLightEffect failed " + e);
                                    return -1;
                                }
                            }
                        }
                        AmbientEffect effect = (AmbientEffect) gson.fromJson(obj.toString(), (Class<Object>) AmbientEffect.class);
                        String str3 = TAG;
                        LogUtil.d(str3, "playAmbientLightEffect, effect=" + effect.toJson());
                        return this.mManager.playAmbientLightEffect(Actions.ACTION_ALL, effect);
                    } else {
                        return -1;
                    }
                }
                return -1;
            } catch (Exception e2) {
                e = e2;
            }
        } else {
            return -1;
        }
    }

    private int stopAmbientLightEffect() {
        try {
            return this.mManager.stopAmbientLightEffect();
        } catch (Exception e) {
            String str = TAG;
            LogUtil.e(str, "stopAmbientLightEffect failed " + e);
            return -1;
        }
    }
}
