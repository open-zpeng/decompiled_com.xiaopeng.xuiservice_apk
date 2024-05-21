package com.xiaopeng.xuiservice.innerutils;

import android.app.ActivityThread;
import android.os.Build;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import com.xiaopeng.xuiservice.innerutils.LocaleStrings;
import com.xiaopeng.xuiservice.xapp.XAppStartManager;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
/* loaded from: classes5.dex */
public class LocaleStrings {
    private static final String TAG = "LocaleStrings";
    private static LocaleStrings sService = null;
    private static StringMap mStringMap = null;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class StringMap {
        private Map<String, List<String>> array;
        private Map<String, String> string;

        private StringMap() {
        }

        public StringMap readFromJson(String file) {
            BufferedReader reader = null;
            LogUtil.i(LocaleStrings.TAG, "readFromJson " + file);
            try {
                try {
                    Gson gson = new GsonBuilder().create();
                    StringBuilder buffer = new StringBuilder();
                    List<String> fileContents = (List) Files.lines(Paths.get(file, new String[0])).filter(new Predicate() { // from class: com.xiaopeng.xuiservice.innerutils.-$$Lambda$LocaleStrings$StringMap$Bc136TDoXGwJW5w1aoLYaApLsu4
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj) {
                            return LocaleStrings.StringMap.lambda$readFromJson$0((String) obj);
                        }
                    }).collect(Collectors.toCollection(new Supplier() { // from class: com.xiaopeng.xuiservice.innerutils.-$$Lambda$OGSS2qx6njxlnp0dnKb4lA3jnw8
                        @Override // java.util.function.Supplier
                        public final Object get() {
                            return new ArrayList();
                        }
                    }));
                    for (String line : fileContents) {
                        buffer.append(line);
                    }
                    reader = new BufferedReader(new StringReader(buffer.toString()));
                    StringMap stringMap = (StringMap) gson.fromJson((Reader) reader, (Class<Object>) StringMap.class);
                    try {
                        reader.close();
                    } catch (Exception e) {
                        LogUtil.e(LocaleStrings.TAG, "colse " + file + " failed, " + e);
                    }
                    return stringMap;
                } catch (Throwable th) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e2) {
                            LogUtil.e(LocaleStrings.TAG, "colse " + file + " failed, " + e2);
                        }
                    }
                    throw th;
                }
            } catch (Exception e3) {
                LogUtil.e(LocaleStrings.TAG, "readFromJson failed, " + e3);
                if (reader != null) {
                    try {
                        reader.close();
                        return null;
                    } catch (Exception e4) {
                        LogUtil.e(LocaleStrings.TAG, "colse " + file + " failed, " + e4);
                        return null;
                    }
                }
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ boolean lambda$readFromJson$0(String line) {
            return !line.contains("//");
        }
    }

    private LocaleStrings() {
        DumpDispatcher.registerDump("locale", new DumpDispatcher.DebugDumper() { // from class: com.xiaopeng.xuiservice.innerutils.-$$Lambda$LocaleStrings$a7fKhY6s_ZHLnAsjHD_mAFrhp5M
            @Override // com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
            public final void debugDump(PrintWriter printWriter, String[] strArr) {
                LocaleStrings.this.lambda$new$0$LocaleStrings(printWriter, strArr);
            }
        });
        update();
    }

    public void update() {
        mStringMap = new StringMap().readFromJson(ActivityThread.currentActivityThread().getApplication().getResources().getString(R.string.locale_strings));
    }

    public String getString(String key) {
        StringMap stringMap = mStringMap;
        if (stringMap == null || !stringMap.string.containsKey(key)) {
            return null;
        }
        return (String) mStringMap.string.get(key);
    }

    public String[] getStringArray(String key) {
        StringMap stringMap = mStringMap;
        if (stringMap == null || !stringMap.array.containsKey(key)) {
            return null;
        }
        return (String[]) ((List) mStringMap.array.get(key)).toArray(new String[0]);
    }

    public static LocaleStrings getInstance() {
        if (sService == null) {
            synchronized (LocaleStrings.class) {
                if (sService == null) {
                    sService = new LocaleStrings();
                }
            }
        }
        return sService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: dump */
    public void lambda$new$0$LocaleStrings(PrintWriter pw, String[] args) {
        boolean z;
        if (args == null || args.length < 1) {
            pw.println("LocaleStrings:no valid params,please input like:\ndumpsys locale -get XXXkey\nordumpsys locale -set XXXkey XXXValue");
            return;
        }
        for (int i = 0; i < args.length; i++) {
            String str = args[i];
            int hashCode = str.hashCode();
            if (hashCode == 1503) {
                if (str.equals("-l")) {
                    z = false;
                }
                z = true;
            } else if (hashCode == 1442825) {
                if (str.equals("-get")) {
                    z = true;
                }
                z = true;
            } else if (hashCode != 1454357) {
                if (hashCode == 44880459 && str.equals("-list")) {
                    z = true;
                }
                z = true;
            } else {
                if (str.equals("-set")) {
                    z = true;
                }
                z = true;
            }
            if (!z || z) {
                pw.println("String map=" + mStringMap.string.toString());
                pw.println("array map=" + mStringMap.array.toString());
            } else if (!z) {
                if (z) {
                    if (Build.TYPE.equals(XAppStartManager.SHARED_FROM_USER)) {
                        pw.println("release version can't set...");
                    } else if (i + 2 < args.length) {
                        String key = args[i + 1];
                        String value = args[i + 2];
                        pw.println("set " + key + ",value=" + value);
                        mStringMap.string.put(key, value);
                    } else {
                        pw.println("lack key/value...");
                    }
                }
            } else if (i + 1 < args.length) {
                String key2 = args[i + 1];
                pw.println("get " + key2 + "=" + getString(key2));
            } else {
                pw.println("lack key...");
            }
        }
    }
}
