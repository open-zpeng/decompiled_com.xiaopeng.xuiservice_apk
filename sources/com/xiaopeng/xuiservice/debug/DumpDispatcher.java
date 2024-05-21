package com.xiaopeng.xuiservice.debug;

import android.os.SystemProperties;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes5.dex */
public class DumpDispatcher {
    private static HashMap<String, DebugDumper> dumperMap = new HashMap<>();

    /* loaded from: classes5.dex */
    public interface DebugDumper {
        void debugDump(PrintWriter printWriter, String[] strArr);
    }

    static {
        selfRegisterModules();
    }

    public static void registerDump(String module, DebugDumper dumper) {
        synchronized (dumperMap) {
            dumperMap.put(module, dumper);
        }
    }

    public static boolean hasModule(String module) {
        return dumperMap.containsKey(module);
    }

    public static void dumpModule(PrintWriter pw, String module, String[] args, int start) {
        DebugDumper dumper;
        pw.println("dump module:" + module + ",arg len=" + (args.length - start));
        String[] subArgs = null;
        if (start < args.length) {
            subArgs = new String[args.length - start];
            for (int i = start; i < args.length; i++) {
                subArgs[i - start] = args[i];
            }
        }
        synchronized (dumperMap) {
            dumper = dumperMap.get(module);
        }
        if (dumper != null) {
            pw.println("find dumper for:" + module);
            dumper.debugDump(pw, subArgs);
            return;
        }
        char c = 65535;
        int hashCode = module.hashCode();
        if (hashCode != 1437268) {
            if (hashCode == 3322014 && module.equals("list")) {
                c = 1;
            }
        } else if (module.equals("-all")) {
            c = 0;
        }
        if (c == 0 || c == 1) {
            printModules(pw);
        }
    }

    public static void dumpHelp(PrintWriter pw) {
        pw.println("iot service help command");
    }

    private static void selfRegisterModules() {
        registerDump("log", new DebugDumper() { // from class: com.xiaopeng.xuiservice.debug.-$$Lambda$DumpDispatcher$aH751Sy5__0X_w57Ixir9iSp2Dk
            @Override // com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
            public final void debugDump(PrintWriter printWriter, String[] strArr) {
                DumpDispatcher.handleDumpLog(printWriter, strArr);
            }
        });
    }

    private static void printModules(PrintWriter pw) {
        pw.println("available modules:");
        synchronized (dumperMap) {
            for (String m : dumperMap.keySet()) {
                pw.println("    " + m);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void handleDumpLog(PrintWriter pw, String[] args) {
        int opti = 0;
        Map<Integer, String> levelMap = new HashMap<Integer, String>() { // from class: com.xiaopeng.xuiservice.debug.DumpDispatcher.1
            {
                put(0, "verbose");
                put(1, "debug");
                put(2, "info");
                put(3, "warning");
                put(4, "error");
                put(10, "none");
                put(-1, "unset");
            }
        };
        if (args != null) {
            boolean persistSave = false;
            while (opti < args.length) {
                String param = args[opti];
                opti++;
                char c = 65535;
                int hashCode = param.hashCode();
                if (hashCode != -1633109644) {
                    if (hashCode != 1507) {
                        if (hashCode == 277447534 && param.equals("-output")) {
                            c = 1;
                        }
                    } else if (param.equals("-p")) {
                        c = 2;
                    }
                } else if (param.equals("-default")) {
                    c = 0;
                }
                if (c != 0) {
                    if (c != 1) {
                        if (c == 2) {
                            persistSave = true;
                        }
                    } else if (opti < args.length) {
                        int level = Integer.parseInt(args[opti]);
                        LogUtil.setOutputLevel(level);
                        pw.println("set output level:" + levelMap.get(Integer.valueOf(level)));
                        if (persistSave) {
                            pw.println("with persist config");
                            SystemProperties.set(XUIConfig.PROPERTY_TARGET_LOG_LEVEL, args[opti]);
                        }
                    } else {
                        pw.println("please input output log level:0-4/-1");
                    }
                } else if (opti < args.length) {
                    int level2 = Integer.parseInt(args[opti]);
                    LogUtil.setDefaultLevel(level2);
                    pw.println("set default level:" + levelMap.get(Integer.valueOf(level2)));
                } else {
                    pw.println("please input default log level:0-4/10");
                }
            }
            return;
        }
        int defaultLevel = LogUtil.getDefaultLevel();
        int targetLevel = LogUtil.getOutputLevel();
        pw.println("default log level:" + levelMap.get(Integer.valueOf(defaultLevel)));
        pw.println("output log level:" + levelMap.get(Integer.valueOf(targetLevel)));
    }
}
