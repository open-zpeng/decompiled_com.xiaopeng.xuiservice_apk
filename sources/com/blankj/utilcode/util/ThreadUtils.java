package com.blankj.utilcode.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.CallSuper;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import java.lang.Thread;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
/* loaded from: classes4.dex */
public final class ThreadUtils {
    private static final byte TYPE_CACHED = -2;
    private static final byte TYPE_CPU = -8;
    private static final byte TYPE_IO = -4;
    private static final byte TYPE_SINGLE = -1;
    private static Executor sDeliver;
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());
    private static final Map<Integer, Map<Integer, ExecutorService>> TYPE_PRIORITY_POOLS = new HashMap();
    private static final Map<Task, ExecutorService> TASK_POOL_MAP = new ConcurrentHashMap();
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final Timer TIMER = new Timer();

    static /* synthetic */ Executor access$600() {
        return getGlobalDeliver();
    }

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static Handler getMainHandler() {
        return HANDLER;
    }

    public static void runOnUiThread(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            HANDLER.post(runnable);
        }
    }

    public static void runOnUiThreadDelayed(Runnable runnable, long delayMillis) {
        HANDLER.postDelayed(runnable, delayMillis);
    }

    public static ExecutorService getFixedPool(@IntRange(from = 1) int size) {
        return getPoolByTypeAndPriority(size);
    }

    public static ExecutorService getFixedPool(@IntRange(from = 1) int size, @IntRange(from = 1, to = 10) int priority) {
        return getPoolByTypeAndPriority(size, priority);
    }

    public static ExecutorService getSinglePool() {
        return getPoolByTypeAndPriority(-1);
    }

    public static ExecutorService getSinglePool(@IntRange(from = 1, to = 10) int priority) {
        return getPoolByTypeAndPriority(-1, priority);
    }

    public static ExecutorService getCachedPool() {
        return getPoolByTypeAndPriority(-2);
    }

    public static ExecutorService getCachedPool(@IntRange(from = 1, to = 10) int priority) {
        return getPoolByTypeAndPriority(-2, priority);
    }

    public static ExecutorService getIoPool() {
        return getPoolByTypeAndPriority(-4);
    }

    public static ExecutorService getIoPool(@IntRange(from = 1, to = 10) int priority) {
        return getPoolByTypeAndPriority(-4, priority);
    }

    public static ExecutorService getCpuPool() {
        return getPoolByTypeAndPriority(-8);
    }

    public static ExecutorService getCpuPool(@IntRange(from = 1, to = 10) int priority) {
        return getPoolByTypeAndPriority(-8, priority);
    }

    public static <T> void executeByFixed(@IntRange(from = 1) int size, Task<T> task) {
        execute(getPoolByTypeAndPriority(size), task);
    }

    public static <T> void executeByFixed(@IntRange(from = 1) int size, Task<T> task, @IntRange(from = 1, to = 10) int priority) {
        execute(getPoolByTypeAndPriority(size, priority), task);
    }

    public static <T> void executeByFixedWithDelay(@IntRange(from = 1) int size, Task<T> task, long delay, TimeUnit unit) {
        executeWithDelay(getPoolByTypeAndPriority(size), task, delay, unit);
    }

    public static <T> void executeByFixedWithDelay(@IntRange(from = 1) int size, Task<T> task, long delay, TimeUnit unit, @IntRange(from = 1, to = 10) int priority) {
        executeWithDelay(getPoolByTypeAndPriority(size, priority), task, delay, unit);
    }

    public static <T> void executeByFixedAtFixRate(@IntRange(from = 1) int size, Task<T> task, long period, TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(size), task, 0L, period, unit);
    }

    public static <T> void executeByFixedAtFixRate(@IntRange(from = 1) int size, Task<T> task, long period, TimeUnit unit, @IntRange(from = 1, to = 10) int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(size, priority), task, 0L, period, unit);
    }

    public static <T> void executeByFixedAtFixRate(@IntRange(from = 1) int size, Task<T> task, long initialDelay, long period, TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(size), task, initialDelay, period, unit);
    }

    public static <T> void executeByFixedAtFixRate(@IntRange(from = 1) int size, Task<T> task, long initialDelay, long period, TimeUnit unit, @IntRange(from = 1, to = 10) int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(size, priority), task, initialDelay, period, unit);
    }

    public static <T> void executeBySingle(Task<T> task) {
        execute(getPoolByTypeAndPriority(-1), task);
    }

    public static <T> void executeBySingle(Task<T> task, @IntRange(from = 1, to = 10) int priority) {
        execute(getPoolByTypeAndPriority(-1, priority), task);
    }

    public static <T> void executeBySingleWithDelay(Task<T> task, long delay, TimeUnit unit) {
        executeWithDelay(getPoolByTypeAndPriority(-1), task, delay, unit);
    }

    public static <T> void executeBySingleWithDelay(Task<T> task, long delay, TimeUnit unit, @IntRange(from = 1, to = 10) int priority) {
        executeWithDelay(getPoolByTypeAndPriority(-1, priority), task, delay, unit);
    }

    public static <T> void executeBySingleAtFixRate(Task<T> task, long period, TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(-1), task, 0L, period, unit);
    }

    public static <T> void executeBySingleAtFixRate(Task<T> task, long period, TimeUnit unit, @IntRange(from = 1, to = 10) int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(-1, priority), task, 0L, period, unit);
    }

    public static <T> void executeBySingleAtFixRate(Task<T> task, long initialDelay, long period, TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(-1), task, initialDelay, period, unit);
    }

    public static <T> void executeBySingleAtFixRate(Task<T> task, long initialDelay, long period, TimeUnit unit, @IntRange(from = 1, to = 10) int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(-1, priority), task, initialDelay, period, unit);
    }

    public static <T> void executeByCached(Task<T> task) {
        execute(getPoolByTypeAndPriority(-2), task);
    }

    public static <T> void executeByCached(Task<T> task, @IntRange(from = 1, to = 10) int priority) {
        execute(getPoolByTypeAndPriority(-2, priority), task);
    }

    public static <T> void executeByCachedWithDelay(Task<T> task, long delay, TimeUnit unit) {
        executeWithDelay(getPoolByTypeAndPriority(-2), task, delay, unit);
    }

    public static <T> void executeByCachedWithDelay(Task<T> task, long delay, TimeUnit unit, @IntRange(from = 1, to = 10) int priority) {
        executeWithDelay(getPoolByTypeAndPriority(-2, priority), task, delay, unit);
    }

    public static <T> void executeByCachedAtFixRate(Task<T> task, long period, TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(-2), task, 0L, period, unit);
    }

    public static <T> void executeByCachedAtFixRate(Task<T> task, long period, TimeUnit unit, @IntRange(from = 1, to = 10) int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(-2, priority), task, 0L, period, unit);
    }

    public static <T> void executeByCachedAtFixRate(Task<T> task, long initialDelay, long period, TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(-2), task, initialDelay, period, unit);
    }

    public static <T> void executeByCachedAtFixRate(Task<T> task, long initialDelay, long period, TimeUnit unit, @IntRange(from = 1, to = 10) int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(-2, priority), task, initialDelay, period, unit);
    }

    public static <T> void executeByIo(Task<T> task) {
        execute(getPoolByTypeAndPriority(-4), task);
    }

    public static <T> void executeByIo(Task<T> task, @IntRange(from = 1, to = 10) int priority) {
        execute(getPoolByTypeAndPriority(-4, priority), task);
    }

    public static <T> void executeByIoWithDelay(Task<T> task, long delay, TimeUnit unit) {
        executeWithDelay(getPoolByTypeAndPriority(-4), task, delay, unit);
    }

    public static <T> void executeByIoWithDelay(Task<T> task, long delay, TimeUnit unit, @IntRange(from = 1, to = 10) int priority) {
        executeWithDelay(getPoolByTypeAndPriority(-4, priority), task, delay, unit);
    }

    public static <T> void executeByIoAtFixRate(Task<T> task, long period, TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(-4), task, 0L, period, unit);
    }

    public static <T> void executeByIoAtFixRate(Task<T> task, long period, TimeUnit unit, @IntRange(from = 1, to = 10) int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(-4, priority), task, 0L, period, unit);
    }

    public static <T> void executeByIoAtFixRate(Task<T> task, long initialDelay, long period, TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(-4), task, initialDelay, period, unit);
    }

    public static <T> void executeByIoAtFixRate(Task<T> task, long initialDelay, long period, TimeUnit unit, @IntRange(from = 1, to = 10) int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(-4, priority), task, initialDelay, period, unit);
    }

    public static <T> void executeByCpu(Task<T> task) {
        execute(getPoolByTypeAndPriority(-8), task);
    }

    public static <T> void executeByCpu(Task<T> task, @IntRange(from = 1, to = 10) int priority) {
        execute(getPoolByTypeAndPriority(-8, priority), task);
    }

    public static <T> void executeByCpuWithDelay(Task<T> task, long delay, TimeUnit unit) {
        executeWithDelay(getPoolByTypeAndPriority(-8), task, delay, unit);
    }

    public static <T> void executeByCpuWithDelay(Task<T> task, long delay, TimeUnit unit, @IntRange(from = 1, to = 10) int priority) {
        executeWithDelay(getPoolByTypeAndPriority(-8, priority), task, delay, unit);
    }

    public static <T> void executeByCpuAtFixRate(Task<T> task, long period, TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(-8), task, 0L, period, unit);
    }

    public static <T> void executeByCpuAtFixRate(Task<T> task, long period, TimeUnit unit, @IntRange(from = 1, to = 10) int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(-8, priority), task, 0L, period, unit);
    }

    public static <T> void executeByCpuAtFixRate(Task<T> task, long initialDelay, long period, TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(-8), task, initialDelay, period, unit);
    }

    public static <T> void executeByCpuAtFixRate(Task<T> task, long initialDelay, long period, TimeUnit unit, @IntRange(from = 1, to = 10) int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(-8, priority), task, initialDelay, period, unit);
    }

    public static <T> void executeByCustom(ExecutorService pool, Task<T> task) {
        execute(pool, task);
    }

    public static <T> void executeByCustomWithDelay(ExecutorService pool, Task<T> task, long delay, TimeUnit unit) {
        executeWithDelay(pool, task, delay, unit);
    }

    public static <T> void executeByCustomAtFixRate(ExecutorService pool, Task<T> task, long period, TimeUnit unit) {
        executeAtFixedRate(pool, task, 0L, period, unit);
    }

    public static <T> void executeByCustomAtFixRate(ExecutorService pool, Task<T> task, long initialDelay, long period, TimeUnit unit) {
        executeAtFixedRate(pool, task, initialDelay, period, unit);
    }

    public static void cancel(Task task) {
        if (task == null) {
            return;
        }
        task.cancel();
    }

    public static void cancel(Task... tasks) {
        if (tasks == null || tasks.length == 0) {
            return;
        }
        for (Task task : tasks) {
            if (task != null) {
                task.cancel();
            }
        }
    }

    public static void cancel(List<Task> tasks) {
        if (tasks == null || tasks.size() == 0) {
            return;
        }
        for (Task task : tasks) {
            if (task != null) {
                task.cancel();
            }
        }
    }

    public static void cancel(ExecutorService executorService) {
        if (executorService instanceof ThreadPoolExecutor4Util) {
            for (Map.Entry<Task, ExecutorService> taskTaskInfoEntry : TASK_POOL_MAP.entrySet()) {
                if (taskTaskInfoEntry.getValue() == executorService) {
                    cancel(taskTaskInfoEntry.getKey());
                }
            }
            return;
        }
        Log.e("ThreadUtils", "The executorService is not ThreadUtils's pool.");
    }

    public static void setDeliver(Executor deliver) {
        sDeliver = deliver;
    }

    private static <T> void execute(ExecutorService pool, Task<T> task) {
        execute(pool, task, 0L, 0L, null);
    }

    private static <T> void executeWithDelay(ExecutorService pool, Task<T> task, long delay, TimeUnit unit) {
        execute(pool, task, delay, 0L, unit);
    }

    private static <T> void executeAtFixedRate(ExecutorService pool, Task<T> task, long delay, long period, TimeUnit unit) {
        execute(pool, task, delay, period, unit);
    }

    private static <T> void execute(final ExecutorService pool, final Task<T> task, long delay, long period, TimeUnit unit) {
        synchronized (TASK_POOL_MAP) {
            if (TASK_POOL_MAP.get(task) != null) {
                Log.e("ThreadUtils", "Task can only be executed once.");
                return;
            }
            TASK_POOL_MAP.put(task, pool);
            if (period == 0) {
                if (delay == 0) {
                    pool.execute(task);
                    return;
                }
                TimerTask timerTask = new TimerTask() { // from class: com.blankj.utilcode.util.ThreadUtils.1
                    @Override // java.util.TimerTask, java.lang.Runnable
                    public void run() {
                        pool.execute(task);
                    }
                };
                TIMER.schedule(timerTask, unit.toMillis(delay));
                return;
            }
            task.setSchedule(true);
            TimerTask timerTask2 = new TimerTask() { // from class: com.blankj.utilcode.util.ThreadUtils.2
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    pool.execute(task);
                }
            };
            TIMER.scheduleAtFixedRate(timerTask2, unit.toMillis(delay), unit.toMillis(period));
        }
    }

    private static ExecutorService getPoolByTypeAndPriority(int type) {
        return getPoolByTypeAndPriority(type, 5);
    }

    private static ExecutorService getPoolByTypeAndPriority(int type, int priority) {
        ExecutorService pool;
        synchronized (TYPE_PRIORITY_POOLS) {
            Map<Integer, ExecutorService> priorityPools = TYPE_PRIORITY_POOLS.get(Integer.valueOf(type));
            if (priorityPools == null) {
                Map<Integer, ExecutorService> priorityPools2 = new ConcurrentHashMap<>();
                pool = ThreadPoolExecutor4Util.createPool(type, priority);
                priorityPools2.put(Integer.valueOf(priority), pool);
                TYPE_PRIORITY_POOLS.put(Integer.valueOf(type), priorityPools2);
            } else {
                pool = priorityPools.get(Integer.valueOf(priority));
                if (pool == null) {
                    pool = ThreadPoolExecutor4Util.createPool(type, priority);
                    priorityPools.put(Integer.valueOf(priority), pool);
                }
            }
        }
        return pool;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static final class ThreadPoolExecutor4Util extends ThreadPoolExecutor {
        private final AtomicInteger mSubmittedCount;
        private LinkedBlockingQueue4Util mWorkQueue;

        /* JADX INFO: Access modifiers changed from: private */
        public static ExecutorService createPool(int type, int priority) {
            if (type != -8) {
                if (type != -4) {
                    if (type != -2) {
                        if (type != -1) {
                            TimeUnit timeUnit = TimeUnit.MILLISECONDS;
                            LinkedBlockingQueue4Util linkedBlockingQueue4Util = new LinkedBlockingQueue4Util();
                            return new ThreadPoolExecutor4Util(type, type, 0L, timeUnit, linkedBlockingQueue4Util, new UtilsThreadFactory("fixed(" + type + ")", priority));
                        }
                        return new ThreadPoolExecutor4Util(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue4Util(), new UtilsThreadFactory("single", priority));
                    }
                    return new ThreadPoolExecutor4Util(0, 128, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue4Util(true), new UtilsThreadFactory("cached", priority));
                }
                return new ThreadPoolExecutor4Util((ThreadUtils.CPU_COUNT * 2) + 1, (ThreadUtils.CPU_COUNT * 2) + 1, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue4Util(), new UtilsThreadFactory("io", priority));
            }
            return new ThreadPoolExecutor4Util(ThreadUtils.CPU_COUNT + 1, (ThreadUtils.CPU_COUNT * 2) + 1, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue4Util(true), new UtilsThreadFactory("cpu", priority));
        }

        ThreadPoolExecutor4Util(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, LinkedBlockingQueue4Util workQueue, ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
            this.mSubmittedCount = new AtomicInteger();
            workQueue.mPool = this;
            this.mWorkQueue = workQueue;
        }

        private int getSubmittedCount() {
            return this.mSubmittedCount.get();
        }

        @Override // java.util.concurrent.ThreadPoolExecutor
        protected void afterExecute(Runnable r, Throwable t) {
            this.mSubmittedCount.decrementAndGet();
            super.afterExecute(r, t);
        }

        @Override // java.util.concurrent.ThreadPoolExecutor, java.util.concurrent.Executor
        public void execute(@NonNull Runnable command) {
            if (command == null) {
                throw new NullPointerException("Argument 'command' of type Runnable (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            if (isShutdown()) {
                return;
            }
            this.mSubmittedCount.incrementAndGet();
            try {
                super.execute(command);
            } catch (RejectedExecutionException e) {
                Log.e("ThreadUtils", "This will not happen!");
                this.mWorkQueue.offer(command);
            } catch (Throwable th) {
                this.mSubmittedCount.decrementAndGet();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class LinkedBlockingQueue4Util extends LinkedBlockingQueue<Runnable> {
        private int mCapacity;
        private volatile ThreadPoolExecutor4Util mPool;

        LinkedBlockingQueue4Util() {
            this.mCapacity = Integer.MAX_VALUE;
        }

        LinkedBlockingQueue4Util(boolean isAddSubThreadFirstThenAddQueue) {
            this.mCapacity = Integer.MAX_VALUE;
            if (isAddSubThreadFirstThenAddQueue) {
                this.mCapacity = 0;
            }
        }

        LinkedBlockingQueue4Util(int capacity) {
            this.mCapacity = Integer.MAX_VALUE;
            this.mCapacity = capacity;
        }

        @Override // java.util.concurrent.LinkedBlockingQueue, java.util.Queue, java.util.concurrent.BlockingQueue
        public boolean offer(@NonNull Runnable runnable) {
            if (runnable == null) {
                throw new NullPointerException("Argument 'runnable' of type Runnable (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            if (this.mCapacity <= size() && this.mPool != null && this.mPool.getPoolSize() < this.mPool.getMaximumPoolSize()) {
                return false;
            }
            return super.offer((LinkedBlockingQueue4Util) runnable);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static final class UtilsThreadFactory extends AtomicLong implements ThreadFactory {
        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        private static final long serialVersionUID = -9209200509960368598L;
        private final boolean isDaemon;
        private final String namePrefix;
        private final int priority;

        UtilsThreadFactory(String prefix, int priority) {
            this(prefix, priority, false);
        }

        UtilsThreadFactory(String prefix, int priority, boolean isDaemon) {
            this.namePrefix = prefix + "-pool-" + POOL_NUMBER.getAndIncrement() + "-thread-";
            this.priority = priority;
            this.isDaemon = isDaemon;
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(@NonNull Runnable r) {
            if (r == null) {
                throw new NullPointerException("Argument 'r' of type Runnable (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            Thread t = new Thread(r, this.namePrefix + getAndIncrement()) { // from class: com.blankj.utilcode.util.ThreadUtils.UtilsThreadFactory.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    try {
                        super.run();
                    } catch (Throwable t2) {
                        Log.e("ThreadUtils", "Request threw uncaught throwable", t2);
                    }
                }
            };
            t.setDaemon(this.isDaemon);
            t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() { // from class: com.blankj.utilcode.util.ThreadUtils.UtilsThreadFactory.2
                @Override // java.lang.Thread.UncaughtExceptionHandler
                public void uncaughtException(Thread t2, Throwable e) {
                    System.out.println(e);
                }
            });
            t.setPriority(this.priority);
            return t;
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class SimpleTask<T> extends Task<T> {
        @Override // com.blankj.utilcode.util.ThreadUtils.Task
        public void onCancel() {
            Log.e("ThreadUtils", "onCancel: " + Thread.currentThread());
        }

        @Override // com.blankj.utilcode.util.ThreadUtils.Task
        public void onFail(Throwable t) {
            Log.e("ThreadUtils", "onFail: ", t);
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class Task<T> implements Runnable {
        private static final int CANCELLED = 4;
        private static final int COMPLETING = 3;
        private static final int EXCEPTIONAL = 2;
        private static final int INTERRUPTED = 5;
        private static final int NEW = 0;
        private static final int RUNNING = 1;
        private static final int TIMEOUT = 6;
        private Executor deliver;
        private volatile boolean isSchedule;
        private OnTimeoutListener mTimeoutListener;
        private long mTimeoutMillis;
        private Timer mTimer;
        private volatile Thread runner;
        private final AtomicInteger state = new AtomicInteger(0);

        /* loaded from: classes4.dex */
        public interface OnTimeoutListener {
            void onTimeout();
        }

        public abstract T doInBackground() throws Throwable;

        public abstract void onCancel();

        public abstract void onFail(Throwable th);

        public abstract void onSuccess(T t);

        @Override // java.lang.Runnable
        public void run() {
            if (this.isSchedule) {
                if (this.runner == null) {
                    if (!this.state.compareAndSet(0, 1)) {
                        return;
                    }
                    this.runner = Thread.currentThread();
                    if (this.mTimeoutListener != null) {
                        Log.w("ThreadUtils", "Scheduled task doesn't support timeout.");
                    }
                } else if (this.state.get() != 1) {
                    return;
                }
            } else if (!this.state.compareAndSet(0, 1)) {
                return;
            } else {
                this.runner = Thread.currentThread();
                if (this.mTimeoutListener != null) {
                    this.mTimer = new Timer();
                    this.mTimer.schedule(new TimerTask() { // from class: com.blankj.utilcode.util.ThreadUtils.Task.1
                        @Override // java.util.TimerTask, java.lang.Runnable
                        public void run() {
                            if (!Task.this.isDone() && Task.this.mTimeoutListener != null) {
                                Task.this.timeout();
                                Task.this.mTimeoutListener.onTimeout();
                                Task.this.onDone();
                            }
                        }
                    }, this.mTimeoutMillis);
                }
            }
            try {
                final T result = doInBackground();
                if (this.isSchedule) {
                    if (this.state.get() != 1) {
                        return;
                    }
                    getDeliver().execute(new Runnable() { // from class: com.blankj.utilcode.util.ThreadUtils.Task.2
                        /* JADX WARN: Multi-variable type inference failed */
                        @Override // java.lang.Runnable
                        public void run() {
                            Task.this.onSuccess(result);
                        }
                    });
                } else if (this.state.compareAndSet(1, 3)) {
                    getDeliver().execute(new Runnable() { // from class: com.blankj.utilcode.util.ThreadUtils.Task.3
                        /* JADX WARN: Multi-variable type inference failed */
                        @Override // java.lang.Runnable
                        public void run() {
                            Task.this.onSuccess(result);
                            Task.this.onDone();
                        }
                    });
                }
            } catch (InterruptedException e) {
                this.state.compareAndSet(4, 5);
            } catch (Throwable throwable) {
                if (this.state.compareAndSet(1, 2)) {
                    getDeliver().execute(new Runnable() { // from class: com.blankj.utilcode.util.ThreadUtils.Task.4
                        @Override // java.lang.Runnable
                        public void run() {
                            Task.this.onFail(throwable);
                            Task.this.onDone();
                        }
                    });
                }
            }
        }

        public void cancel() {
            cancel(true);
        }

        public void cancel(boolean mayInterruptIfRunning) {
            synchronized (this.state) {
                if (this.state.get() > 1) {
                    return;
                }
                this.state.set(4);
                if (mayInterruptIfRunning && this.runner != null) {
                    this.runner.interrupt();
                }
                getDeliver().execute(new Runnable() { // from class: com.blankj.utilcode.util.ThreadUtils.Task.5
                    @Override // java.lang.Runnable
                    public void run() {
                        Task.this.onCancel();
                        Task.this.onDone();
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void timeout() {
            synchronized (this.state) {
                if (this.state.get() > 1) {
                    return;
                }
                this.state.set(6);
                if (this.runner != null) {
                    this.runner.interrupt();
                }
            }
        }

        public boolean isCanceled() {
            return this.state.get() >= 4;
        }

        public boolean isDone() {
            return this.state.get() > 1;
        }

        public Task<T> setDeliver(Executor deliver) {
            this.deliver = deliver;
            return this;
        }

        public Task<T> setTimeout(long timeoutMillis, OnTimeoutListener listener) {
            this.mTimeoutMillis = timeoutMillis;
            this.mTimeoutListener = listener;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setSchedule(boolean isSchedule) {
            this.isSchedule = isSchedule;
        }

        private Executor getDeliver() {
            Executor executor = this.deliver;
            if (executor == null) {
                return ThreadUtils.access$600();
            }
            return executor;
        }

        @CallSuper
        protected void onDone() {
            ThreadUtils.TASK_POOL_MAP.remove(this);
            Timer timer = this.mTimer;
            if (timer != null) {
                timer.cancel();
                this.mTimer = null;
                this.mTimeoutListener = null;
            }
        }
    }

    /* loaded from: classes4.dex */
    public static class SyncValue<T> {
        private T mValue;
        private CountDownLatch mLatch = new CountDownLatch(1);
        private AtomicBoolean mFlag = new AtomicBoolean();

        public void setValue(T value) {
            if (this.mFlag.compareAndSet(false, true)) {
                this.mValue = value;
                this.mLatch.countDown();
            }
        }

        public T getValue() {
            if (!this.mFlag.get()) {
                try {
                    this.mLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return this.mValue;
        }

        public T getValue(long timeout, TimeUnit unit, T defaultValue) {
            if (!this.mFlag.get()) {
                try {
                    this.mLatch.await(timeout, unit);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return defaultValue;
                }
            }
            return this.mValue;
        }
    }

    private static Executor getGlobalDeliver() {
        if (sDeliver == null) {
            sDeliver = new Executor() { // from class: com.blankj.utilcode.util.ThreadUtils.3
                @Override // java.util.concurrent.Executor
                public void execute(@NonNull Runnable command) {
                    if (command == null) {
                        throw new NullPointerException("Argument 'command' of type Runnable (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
                    }
                    ThreadUtils.runOnUiThread(command);
                }
            };
        }
        return sDeliver;
    }
}
