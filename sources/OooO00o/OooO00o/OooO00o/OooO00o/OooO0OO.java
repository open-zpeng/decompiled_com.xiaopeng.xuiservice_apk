package OooO00o.OooO00o.OooO00o.OooO00o;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import java.lang.Thread;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
/* loaded from: classes.dex */
public final class OooO0OO {
    public static Executor OooO0o;

    /* renamed from: OooO00o  reason: collision with root package name */
    public static final Handler f214OooO00o = new Handler(Looper.getMainLooper());
    public static final Map<Integer, Map<Integer, ExecutorService>> OooO0O0 = new HashMap();
    public static final Map<OooO, ExecutorService> OooO0OO = new ConcurrentHashMap();
    public static final int OooO0Oo = Runtime.getRuntime().availableProcessors();
    public static final Timer OooO0o0 = new Timer();

    /* loaded from: classes.dex */
    public static abstract class OooO<T> implements Runnable {

        /* renamed from: OooO00o  reason: collision with root package name */
        public final AtomicInteger f215OooO00o = new AtomicInteger(0);
        public volatile boolean OooO0O0;
        public volatile Thread OooO0OO;

        /* loaded from: classes.dex */
        public class OooO00o implements Runnable {

            /* renamed from: OooO00o  reason: collision with root package name */
            public final /* synthetic */ Object f216OooO00o;

            public OooO00o(Object obj) {
                this.f216OooO00o = obj;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.lang.Runnable
            public void run() {
                OooO.this.OooO00o((OooO) this.f216OooO00o);
            }
        }

        /* loaded from: classes.dex */
        public class OooO0O0 implements Runnable {

            /* renamed from: OooO00o  reason: collision with root package name */
            public final /* synthetic */ Object f217OooO00o;

            public OooO0O0(Object obj) {
                this.f217OooO00o = obj;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.lang.Runnable
            public void run() {
                OooO.this.OooO00o((OooO) this.f217OooO00o);
                OooO.this.OooO0OO();
            }
        }

        /* renamed from: OooO00o.OooO00o.OooO00o.OooO00o.OooO0OO$OooO$OooO0OO  reason: collision with other inner class name */
        /* loaded from: classes.dex */
        public class RunnableC0000OooO0OO implements Runnable {

            /* renamed from: OooO00o  reason: collision with root package name */
            public final /* synthetic */ Throwable f218OooO00o;

            public RunnableC0000OooO0OO(Throwable th) {
                this.f218OooO00o = th;
            }

            @Override // java.lang.Runnable
            public void run() {
                OooO.this.OooO00o(this.f218OooO00o);
                OooO.this.OooO0OO();
            }
        }

        public abstract T OooO00o();

        public abstract void OooO00o(T t);

        public abstract void OooO00o(Throwable th);

        public final Executor OooO0O0() {
            if (OooO0OO.OooO0o == null) {
                OooO0OO.OooO0o = new OooO00o.OooO00o.OooO00o.OooO00o.OooO0o();
            }
            return OooO0OO.OooO0o;
        }

        @CallSuper
        public void OooO0OO() {
            OooO0OO.OooO0OO.remove(this);
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.OooO0O0) {
                if (this.OooO0OO == null) {
                    if (!this.f215OooO00o.compareAndSet(0, 1)) {
                        return;
                    }
                    this.OooO0OO = Thread.currentThread();
                } else if (this.f215OooO00o.get() != 1) {
                    return;
                }
            } else if (!this.f215OooO00o.compareAndSet(0, 1)) {
                return;
            } else {
                this.OooO0OO = Thread.currentThread();
            }
            try {
                T OooO00o2 = OooO00o();
                if (this.OooO0O0) {
                    if (this.f215OooO00o.get() != 1) {
                        return;
                    }
                    OooO0O0().execute(new OooO00o(OooO00o2));
                } else if (this.f215OooO00o.compareAndSet(1, 3)) {
                    OooO0O0().execute(new OooO0O0(OooO00o2));
                }
            } catch (InterruptedException e) {
                this.f215OooO00o.compareAndSet(4, 5);
            } catch (Throwable th) {
                if (this.f215OooO00o.compareAndSet(1, 2)) {
                    OooO0O0().execute(new RunnableC0000OooO0OO(th));
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static class OooO00o extends TimerTask {

        /* renamed from: OooO00o  reason: collision with root package name */
        public final /* synthetic */ ExecutorService f219OooO00o;
        public final /* synthetic */ OooO OooO0O0;

        public OooO00o(ExecutorService executorService, OooO oooO) {
            this.f219OooO00o = executorService;
            this.OooO0O0 = oooO;
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            this.f219OooO00o.execute(this.OooO0O0);
        }
    }

    /* loaded from: classes.dex */
    public static class OooO0O0 extends TimerTask {

        /* renamed from: OooO00o  reason: collision with root package name */
        public final /* synthetic */ ExecutorService f220OooO00o;
        public final /* synthetic */ OooO OooO0O0;

        public OooO0O0(ExecutorService executorService, OooO oooO) {
            this.f220OooO00o = executorService;
            this.OooO0O0 = oooO;
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            this.f220OooO00o.execute(this.OooO0O0);
        }
    }

    /* renamed from: OooO00o.OooO00o.OooO00o.OooO00o.OooO0OO$OooO0OO  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static final class C0001OooO0OO extends LinkedBlockingQueue<Runnable> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public volatile OooOO0 f221OooO00o;
        public int OooO0O0;

        public C0001OooO0OO() {
            this.OooO0O0 = Integer.MAX_VALUE;
        }

        @Override // java.util.concurrent.LinkedBlockingQueue, java.util.Queue, java.util.concurrent.BlockingQueue
        /* renamed from: OooO00o */
        public boolean offer(@NonNull Runnable runnable) {
            if (runnable != null) {
                if (this.OooO0O0 > size() || this.f221OooO00o == null || this.f221OooO00o.getPoolSize() >= this.f221OooO00o.getMaximumPoolSize()) {
                    return super.offer(runnable);
                }
                return false;
            }
            throw new NullPointerException("Argument 'runnable' of type Runnable (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }

        public C0001OooO0OO(boolean z) {
            this.OooO0O0 = Integer.MAX_VALUE;
            if (z) {
                this.OooO0O0 = 0;
            }
        }
    }

    /* loaded from: classes.dex */
    public static abstract class OooO0o<T> extends OooO<T> {
        @Override // OooO00o.OooO00o.OooO00o.OooO00o.OooO0OO.OooO
        public void OooO00o(Throwable th) {
            Log.e("ThreadUtils", "onFail: ", th);
        }
    }

    /* loaded from: classes.dex */
    public static final class OooOO0 extends ThreadPoolExecutor {

        /* renamed from: OooO00o  reason: collision with root package name */
        public final AtomicInteger f222OooO00o;
        public C0001OooO0OO OooO0O0;

        public OooOO0(int i, int i2, long j, TimeUnit timeUnit, C0001OooO0OO c0001OooO0OO, ThreadFactory threadFactory) {
            super(i, i2, j, timeUnit, c0001OooO0OO, threadFactory);
            this.f222OooO00o = new AtomicInteger();
            c0001OooO0OO.f221OooO00o = this;
            this.OooO0O0 = c0001OooO0OO;
        }

        public static ExecutorService OooO00o(int i, int i2) {
            if (i == -8) {
                int i3 = OooO0OO.OooO0Oo;
                return new OooOO0(i3 + 1, (i3 * 2) + 1, 30L, TimeUnit.SECONDS, new C0001OooO0OO(true), new OooOO0O("cpu", i2, false));
            } else if (i == -4) {
                int i4 = (OooO0OO.OooO0Oo * 2) + 1;
                return new OooOO0(i4, i4, 30L, TimeUnit.SECONDS, new C0001OooO0OO(), new OooOO0O("io", i2, false));
            } else if (i != -2) {
                if (i != -1) {
                    TimeUnit timeUnit = TimeUnit.MILLISECONDS;
                    C0001OooO0OO c0001OooO0OO = new C0001OooO0OO();
                    return new OooOO0(i, i, 0L, timeUnit, c0001OooO0OO, new OooOO0O("fixed(" + i + ")", i2, false));
                }
                return new OooOO0(1, 1, 0L, TimeUnit.MILLISECONDS, new C0001OooO0OO(), new OooOO0O("single", i2, false));
            } else {
                return new OooOO0(0, 128, 60L, TimeUnit.SECONDS, new C0001OooO0OO(true), new OooOO0O("cached", i2, false));
            }
        }

        @Override // java.util.concurrent.ThreadPoolExecutor
        public void afterExecute(Runnable runnable, Throwable th) {
            this.f222OooO00o.decrementAndGet();
            super.afterExecute(runnable, th);
        }

        @Override // java.util.concurrent.ThreadPoolExecutor, java.util.concurrent.Executor
        public void execute(@NonNull Runnable runnable) {
            if (runnable != null) {
                if (isShutdown()) {
                    return;
                }
                this.f222OooO00o.incrementAndGet();
                try {
                    super.execute(runnable);
                    return;
                } catch (RejectedExecutionException e) {
                    Log.e("ThreadUtils", "This will not happen!");
                    this.OooO0O0.offer(runnable);
                    return;
                } catch (Throwable th) {
                    this.f222OooO00o.decrementAndGet();
                    return;
                }
            }
            throw new NullPointerException("Argument 'command' of type Runnable (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
    }

    /* loaded from: classes.dex */
    public static final class OooOO0O extends AtomicLong implements ThreadFactory {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final AtomicInteger f223OooO00o = new AtomicInteger(1);
        private static final long serialVersionUID = -9209200509960368598L;
        public final String OooO0O0;
        public final int OooO0OO;
        public final boolean OooO0Oo;

        /* loaded from: classes.dex */
        public class OooO00o extends Thread {
            public OooO00o(OooOO0O oooOO0O, Runnable runnable, String str) {
                super(runnable, str);
            }

            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    super.run();
                } catch (Throwable th) {
                    Log.e("ThreadUtils", "Request threw uncaught throwable", th);
                }
            }
        }

        /* loaded from: classes.dex */
        public class OooO0O0 implements Thread.UncaughtExceptionHandler {
            public OooO0O0(OooOO0O oooOO0O) {
            }

            @Override // java.lang.Thread.UncaughtExceptionHandler
            public void uncaughtException(Thread thread, Throwable th) {
                System.out.println(th);
            }
        }

        public OooOO0O(String str, int i, boolean z) {
            this.OooO0O0 = str + "-pool-" + f223OooO00o.getAndIncrement() + "-thread-";
            this.OooO0OO = i;
            this.OooO0Oo = z;
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(@NonNull Runnable runnable) {
            if (runnable != null) {
                OooO00o oooO00o = new OooO00o(this, runnable, this.OooO0O0 + getAndIncrement());
                oooO00o.setDaemon(this.OooO0Oo);
                oooO00o.setUncaughtExceptionHandler(new OooO0O0(this));
                oooO00o.setPriority(this.OooO0OO);
                return oooO00o;
            }
            throw new NullPointerException("Argument 'r' of type Runnable (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
    }

    public static <T> void OooO00o(ExecutorService executorService, OooO<T> oooO, long j, long j2, TimeUnit timeUnit) {
        Map<OooO, ExecutorService> map = OooO0OO;
        synchronized (map) {
            if (map.get(oooO) != null) {
                Log.e("ThreadUtils", "Task can only be executed once.");
                return;
            }
            map.put(oooO, executorService);
            if (j2 != 0) {
                oooO.OooO0O0 = true;
                OooO0o0.scheduleAtFixedRate(new OooO0O0(executorService, oooO), timeUnit.toMillis(j), timeUnit.toMillis(j2));
            } else if (j == 0) {
                executorService.execute(oooO);
            } else {
                OooO0o0.schedule(new OooO00o(executorService, oooO), timeUnit.toMillis(j));
            }
        }
    }

    public static ExecutorService OooO00o(int i) {
        ExecutorService executorService;
        Map<Integer, Map<Integer, ExecutorService>> map = OooO0O0;
        synchronized (map) {
            Map<Integer, ExecutorService> map2 = map.get(Integer.valueOf(i));
            if (map2 == null) {
                ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                executorService = OooOO0.OooO00o(i, 5);
                concurrentHashMap.put(5, executorService);
                map.put(Integer.valueOf(i), concurrentHashMap);
            } else {
                ExecutorService executorService2 = map2.get(5);
                if (executorService2 != null) {
                    executorService = executorService2;
                } else {
                    executorService = OooOO0.OooO00o(i, 5);
                    map2.put(5, executorService);
                }
            }
        }
        return executorService;
    }
}
