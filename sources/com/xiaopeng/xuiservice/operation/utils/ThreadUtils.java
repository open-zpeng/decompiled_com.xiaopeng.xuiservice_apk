package com.xiaopeng.xuiservice.operation.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes5.dex */
public class ThreadUtils {
    private static volatile ExecutorService sSingleThreadExecutor;

    public static void postOnBackgroundThread(Runnable runnable) {
        if (sSingleThreadExecutor == null) {
            sSingleThreadExecutor = Executors.newSingleThreadExecutor(new OperationThreadFactory());
        }
        sSingleThreadExecutor.execute(runnable);
    }

    /* loaded from: classes5.dex */
    private static class OperationThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final String namePrefix;
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        OperationThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            this.group = s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.namePrefix = "operation-thread-pool-" + poolNumber.getAndIncrement() + "-thread-";
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable r) {
            ThreadGroup threadGroup = this.group;
            Thread t = new Thread(threadGroup, r, this.namePrefix + this.threadNumber.getAndIncrement(), 0L);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != 5) {
                t.setPriority(5);
            }
            return t;
        }
    }
}
