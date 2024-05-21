package com.xiaopeng.xuiservice.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/* loaded from: classes5.dex */
public class ThreadUtils {
    private static volatile ExecutorService sSingleThreadExecutor;

    public static void postOnBackgroundThread(Runnable runnable) {
        if (sSingleThreadExecutor == null) {
            sSingleThreadExecutor = Executors.newSingleThreadExecutor();
        }
        sSingleThreadExecutor.execute(runnable);
    }
}
