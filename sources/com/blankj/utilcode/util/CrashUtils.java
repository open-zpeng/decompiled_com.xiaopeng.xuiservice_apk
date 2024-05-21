package com.blankj.utilcode.util;

import androidx.annotation.NonNull;
import com.blankj.utilcode.util.UtilsBridge;
import java.io.File;
import java.lang.Thread;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
/* loaded from: classes4.dex */
public final class CrashUtils {
    private static final String FILE_SEP = System.getProperty("file.separator");
    private static final Thread.UncaughtExceptionHandler DEFAULT_UNCAUGHT_EXCEPTION_HANDLER = Thread.getDefaultUncaughtExceptionHandler();

    /* loaded from: classes4.dex */
    public interface OnCrashListener {
        void onCrash(CrashInfo crashInfo);
    }

    private CrashUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init() {
        init("");
    }

    public static void init(@NonNull File crashDir) {
        if (crashDir == null) {
            throw new NullPointerException("Argument 'crashDir' of type File (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        init(crashDir.getAbsolutePath(), (OnCrashListener) null);
    }

    public static void init(String crashDirPath) {
        init(crashDirPath, (OnCrashListener) null);
    }

    public static void init(OnCrashListener onCrashListener) {
        init("", onCrashListener);
    }

    public static void init(@NonNull File crashDir, OnCrashListener onCrashListener) {
        if (crashDir == null) {
            throw new NullPointerException("Argument 'crashDir' of type File (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        init(crashDir.getAbsolutePath(), onCrashListener);
    }

    public static void init(String crashDirPath, OnCrashListener onCrashListener) {
        String dirPath;
        if (UtilsBridge.isSpace(crashDirPath)) {
            if (UtilsBridge.isSDCardEnableByEnvironment() && Utils.getApp().getExternalFilesDir(null) != null) {
                dirPath = Utils.getApp().getExternalFilesDir(null) + FILE_SEP + "crash" + FILE_SEP;
            } else {
                dirPath = Utils.getApp().getFilesDir() + FILE_SEP + "crash" + FILE_SEP;
            }
        } else {
            String dirPath2 = FILE_SEP;
            if (crashDirPath.endsWith(dirPath2)) {
                dirPath = crashDirPath;
            } else {
                dirPath = crashDirPath + FILE_SEP;
            }
        }
        Thread.setDefaultUncaughtExceptionHandler(getUncaughtExceptionHandler(dirPath, onCrashListener));
    }

    private static Thread.UncaughtExceptionHandler getUncaughtExceptionHandler(final String dirPath, final OnCrashListener onCrashListener) {
        return new Thread.UncaughtExceptionHandler() { // from class: com.blankj.utilcode.util.CrashUtils.1
            @Override // java.lang.Thread.UncaughtExceptionHandler
            public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
                if (t == null) {
                    throw new NullPointerException("Argument 't' of type Thread (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
                }
                if (e == null) {
                    throw new NullPointerException("Argument 'e' of type Throwable (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
                }
                String time = new SimpleDateFormat("yyyy_MM_dd-HH_mm_ss").format(new Date());
                CrashInfo info = new CrashInfo(time, e);
                OnCrashListener onCrashListener2 = OnCrashListener.this;
                if (onCrashListener2 != null) {
                    onCrashListener2.onCrash(info);
                }
                String crashFile = dirPath + time + ".txt";
                UtilsBridge.writeFileFromString(crashFile, info.toString(), true);
                if (CrashUtils.DEFAULT_UNCAUGHT_EXCEPTION_HANDLER != null) {
                    CrashUtils.DEFAULT_UNCAUGHT_EXCEPTION_HANDLER.uncaughtException(t, e);
                }
            }
        };
    }

    /* loaded from: classes4.dex */
    public static final class CrashInfo {
        private UtilsBridge.FileHead mFileHeadProvider;
        private Throwable mThrowable;

        private CrashInfo(String time, Throwable throwable) {
            this.mThrowable = throwable;
            this.mFileHeadProvider = new UtilsBridge.FileHead("Crash");
            this.mFileHeadProvider.addFirst("Time Of Crash", time);
        }

        public final void addExtraHead(Map<String, String> extraHead) {
            this.mFileHeadProvider.append(extraHead);
        }

        public final void addExtraHead(String key, String value) {
            this.mFileHeadProvider.append(key, value);
        }

        public final Throwable getThrowable() {
            return this.mThrowable;
        }

        public String toString() {
            return this.mFileHeadProvider.toString() + UtilsBridge.getFullStackTrace(this.mThrowable);
        }
    }
}
