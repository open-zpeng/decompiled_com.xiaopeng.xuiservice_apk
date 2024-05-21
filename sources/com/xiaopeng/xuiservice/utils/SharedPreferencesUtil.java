package com.xiaopeng.xuiservice.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import java.io.PrintWriter;
/* loaded from: classes5.dex */
public class SharedPreferencesUtil {
    private static final String PREFERENCES_NAME = "preferences_xui";
    private final String TAG;
    private boolean bootInited;
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private boolean moduleInited;

    /* loaded from: classes5.dex */
    public static class KeySet {
        public static final String KEY_FRIDGE_ADDRESS = "key_fridge_address";
        public static final String KEY_IOT_AIRBED_ADDRESS = "key_iot_airbed_address";
        public static final String KEY_MEDIA_NO_SEIZE_DIALOG = "key_media_no_seize_dialog";
        public static final String KEY_SAFETY_SEAT_ADDRESS = "key_safety_seat_address";
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class SharedPreferencesUtilHolder {
        private static SharedPreferencesUtil sInstance = new SharedPreferencesUtil();

        private SharedPreferencesUtilHolder() {
        }
    }

    public static SharedPreferencesUtil getInstance() {
        return SharedPreferencesUtilHolder.sInstance;
    }

    public void init(Context context) {
        if (!this.moduleInited) {
            this.moduleInited = true;
            this.mContext = context;
            initContextPreference();
            DumpDispatcher.registerDump("share", new DumpDispatcher.DebugDumper() { // from class: com.xiaopeng.xuiservice.utils.-$$Lambda$SharedPreferencesUtil$SoHYlA8xolMWnAEsyTTD8EUnFFE
                @Override // com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
                public final void debugDump(PrintWriter printWriter, String[] strArr) {
                    SharedPreferencesUtil.this.lambda$init$0$SharedPreferencesUtil(printWriter, strArr);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: handleDumpShare */
    public void lambda$init$0$SharedPreferencesUtil(PrintWriter pw, String[] args) {
        int opti = 0;
        if (args != null) {
            while (opti < args.length) {
                String cmd = args[opti];
                opti++;
                char c = 65535;
                int hashCode = cmd.hashCode();
                if (hashCode != 99339) {
                    if (hashCode != 3496342) {
                        if (hashCode == 113399775 && cmd.equals("write")) {
                            c = 1;
                        }
                    } else if (cmd.equals("read")) {
                        c = 0;
                    }
                } else if (cmd.equals("del")) {
                    c = 2;
                }
                if (c != 0) {
                    if (c != 1) {
                        if (c == 2) {
                            if (opti < args.length) {
                                String key = args[opti];
                                getInstance().remove(key);
                                pw.println("del " + key);
                            } else {
                                pw.println("del:less params");
                            }
                        }
                    } else if (opti < args.length) {
                        String key2 = args[opti];
                        opti++;
                        if (opti < args.length) {
                            String value = args[opti];
                            pw.println("write,key=" + key2 + ",value=" + value);
                            getInstance().put(key2, value);
                        } else {
                            pw.println("write:less params");
                        }
                    } else {
                        pw.println("write:less params");
                    }
                } else if (opti < args.length) {
                    String key3 = args[opti];
                    pw.println("read " + key3 + " =" + getInstance().get(key3, "null"));
                } else {
                    pw.println("read:less params");
                }
            }
            return;
        }
        pw.println("dump share,lack of args");
    }

    public void put(String key, String value) {
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences == null) {
            LogUtil.w(this.TAG, "db not inited");
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String get(String key, String defValue) {
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences == null) {
            LogUtil.w(this.TAG, "db not inited");
            return defValue;
        }
        return sharedPreferences.getString(key, defValue);
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences == null) {
            LogUtil.w(this.TAG, "db not inited");
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key, boolean defValue) {
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences == null) {
            LogUtil.w(this.TAG, "db not inited");
            return defValue;
        }
        return sharedPreferences.getBoolean(key, defValue);
    }

    public void putInt(String key, int value) {
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences == null) {
            LogUtil.w(this.TAG, "db not inited");
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key, int defValue) {
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences == null) {
            LogUtil.w(this.TAG, "db not inited");
            return defValue;
        }
        return sharedPreferences.getInt(key, defValue);
    }

    public void putLong(String key, long value) {
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences == null) {
            LogUtil.w(this.TAG, "db not inited");
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLong(String key, long defValue) {
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences == null) {
            LogUtil.w(this.TAG, "db not inited");
            return defValue;
        }
        return sharedPreferences.getLong(key, defValue);
    }

    public void remove(String key) {
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences == null) {
            LogUtil.w(this.TAG, "db not inited");
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    private SharedPreferencesUtil() {
        this.bootInited = false;
        this.moduleInited = false;
        this.TAG = SharedPreferencesUtil.class.getSimpleName();
    }

    private synchronized void initContextPreference() {
        if (this.bootInited) {
            LogUtil.w(this.TAG, "already init");
            return;
        }
        this.bootInited = true;
        Context deviceContext = this.mContext.createDeviceProtectedStorageContext();
        if (!deviceContext.moveSharedPreferencesFrom(this.mContext, PREFERENCES_NAME)) {
            LogUtil.w(this.TAG, "initContextPreference,Failed to migrate shared preferences.");
        }
        this.mContext = deviceContext;
        this.mSharedPreferences = this.mContext.getSharedPreferences(PREFERENCES_NAME, 0);
        if (this.mSharedPreferences == null) {
            LogUtil.w(this.TAG, "initContextPreference fail,sp=preferences_xui");
        }
        LogUtil.i(this.TAG, "initContextPreference done");
    }
}
