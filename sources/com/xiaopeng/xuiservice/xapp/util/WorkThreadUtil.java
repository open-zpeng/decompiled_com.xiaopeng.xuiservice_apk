package com.xiaopeng.xuiservice.xapp.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
/* loaded from: classes5.dex */
public class WorkThreadUtil {
    private static final WorkThreadUtil sInstance = new WorkThreadUtil();
    private final ExecutorService mNetworkThreadPool = Executors.newCachedThreadPool();
    private final ExecutorService mInitBaseThreadPool = Executors.newSingleThreadExecutor();
    private final ExecutorService mIntentTaskThreadPool = Executors.newSingleThreadExecutor();
    private final ScheduledExecutorService mIOThreadPool = Executors.newScheduledThreadPool(0);
    private final ExecutorService mFileOperationThreadPool = Executors.newSingleThreadExecutor();

    private WorkThreadUtil() {
    }

    public static WorkThreadUtil getInstance() {
        return sInstance;
    }

    public void release() {
        if (!this.mIOThreadPool.isShutdown()) {
            this.mIOThreadPool.shutdownNow();
        }
        if (!this.mInitBaseThreadPool.isShutdown()) {
            this.mInitBaseThreadPool.shutdownNow();
        }
        if (!this.mNetworkThreadPool.isShutdown()) {
            this.mNetworkThreadPool.shutdownNow();
        }
        if (!this.mIntentTaskThreadPool.isShutdown()) {
            this.mIntentTaskThreadPool.shutdownNow();
        }
        if (!this.mFileOperationThreadPool.isShutdown()) {
            this.mFileOperationThreadPool.shutdownNow();
        }
    }

    public void executeIOTask(Runnable task) {
        this.mIOThreadPool.execute(task);
    }

    public void executeDelayedIOTask(Runnable task, long delayTime, TimeUnit timeUnit) {
        this.mIOThreadPool.schedule(task, delayTime, timeUnit);
    }

    public void executeNetworkTask(Runnable task) {
        this.mNetworkThreadPool.execute(task);
    }

    public void executeIntentTask(Runnable task) {
        this.mIntentTaskThreadPool.execute(task);
    }

    public void executeAppInitTask(Runnable task) {
        this.mInitBaseThreadPool.execute(task);
    }

    public void executeLoadDatabaseTask(Runnable task) {
        this.mIOThreadPool.execute(task);
    }

    public void executeFileOperation(Runnable task) {
        this.mFileOperationThreadPool.execute(task);
    }
}
