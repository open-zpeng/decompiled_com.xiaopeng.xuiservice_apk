package com.alipay.arome.aromecli;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.Keep;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import com.alipay.mobile.aromeservice.ipc.IAromeServiceInterface;
import com.alipay.mobile.aromeservice.ipc.InitOptions;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.opencv.videoio.Videoio;
@Keep
/* loaded from: classes4.dex */
public class AromeInit {
    public static final int AROME_INIT_FAILED_BIND_SERVICE_FAILED = 3;
    public static final int AROME_INIT_FAILED_BIND_SERVICE_TIMEOUT = 4;
    public static final String PACKAGE_NAME = "com.alipay.arome.app";
    private static Context sApplicationContext;
    private static CountDownLatch sCountDownLatch;
    private static AromeInitOptions sInitOptions;
    private static IAromeServiceInterface sServiceInterface;
    private static AtomicBoolean sIsBound = new AtomicBoolean();
    private static AtomicBoolean sIsBinding = new AtomicBoolean();
    private static b sServiceBinder = null;
    private static final Object sConnectionLock = new Object();
    private static ServiceConnection sLastBindedConn = null;

    @Keep
    /* loaded from: classes4.dex */
    public interface Callback {
        void postInit(boolean z, int i, String str);

        void serverDied();
    }

    /* loaded from: classes4.dex */
    public interface b {
        void a(ServiceConnection serviceConnection);
    }

    public static void attachApplicationContext(Context context) {
        sApplicationContext = context;
    }

    public static Context getApplicationContext() {
        return sApplicationContext;
    }

    public static boolean isServiceOnline() {
        com.alipay.arome.aromecli.a.c("isServiceOnline :" + sIsBound.get());
        return sIsBound.get();
    }

    public static synchronized IAromeServiceInterface getRemoteService() {
        synchronized (AromeInit.class) {
            if (sIsBound.get()) {
                return sServiceInterface;
            } else if (isMainThread()) {
                throw new RuntimeException("Connect to RemoteService on main thread error.");
            } else {
                if (sIsBinding.get()) {
                    sCountDownLatch.await(10L, TimeUnit.SECONDS);
                    return sServiceInterface;
                }
                init(sInitOptions, null);
                return getRemoteService();
            }
        }
    }

    @VisibleForTesting
    protected static void setServiceBinder(b serviceBinder) {
        sServiceBinder = serviceBinder;
    }

    public static synchronized void init(Callback callback) {
        synchronized (AromeInit.class) {
            init(null, callback);
        }
    }

    public static synchronized void init(AromeInitOptions options, final Callback callback) {
        synchronized (AromeInit.class) {
            com.alipay.arome.aromecli.a.b("AromeInit with call stack: " + Log.getStackTraceString(new RuntimeException("Just Print!")));
            if (sIsBinding.get()) {
                com.alipay.arome.aromecli.a.b("AromeInit init skip because isBinding!");
            } else if (sIsBound.get()) {
                com.alipay.arome.aromecli.a.b("AromeInit init skip because isBound!");
            } else {
                if (sServiceBinder == null) {
                    sServiceBinder = new b() { // from class: com.alipay.arome.aromecli.AromeInit.1
                        @Override // com.alipay.arome.aromecli.AromeInit.b
                        public final void a(final ServiceConnection connection) {
                            synchronized (AromeInit.sConnectionLock) {
                                if (AromeInit.sLastBindedConn != null) {
                                    com.alipay.arome.aromecli.a.b("AromeInit unbind ServiceConnection: " + AromeInit.sLastBindedConn);
                                    AromeInit.sApplicationContext.unbindService(AromeInit.sLastBindedConn);
                                    ServiceConnection unused = AromeInit.sLastBindedConn = null;
                                }
                            }
                            com.alipay.arome.aromecli.a.b("AromeInit bindService " + AromeInit.sServiceBinder + " with callback: " + Callback.this);
                            Intent intent = new Intent().setClassName("com.alipay.arome.app", "com.alipay.mobile.aromeservice.AromeService");
                            intent.setAction(IAromeServiceInterface.class.getName());
                            intent.setPackage("com.alipay.arome.app");
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.alipay.arome.aromecli.AromeInit.1.1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    if (AromeInit.sIsBinding.get()) {
                                        com.alipay.arome.aromecli.a.c("bind service timeout with conn: " + connection);
                                        if (Callback.this != null) {
                                            Callback.this.postInit(false, 4, "bind service timeout");
                                        }
                                        ServiceConnection serviceConnection = connection;
                                        if (serviceConnection instanceof a) {
                                            ((a) serviceConnection).a();
                                        }
                                        try {
                                            AromeInit.reset();
                                            AromeInit.sApplicationContext.unbindService(connection);
                                        } catch (Throwable t) {
                                            com.alipay.arome.aromecli.a.a("unbindService exception!", t);
                                        }
                                    }
                                }
                            }, 30000L);
                            if (!AromeInit.sApplicationContext.bindService(intent, connection, 1)) {
                                com.alipay.arome.aromecli.a.c("bind service failed");
                                AromeInit.reset();
                                if (!c.a(AromeInit.sApplicationContext) || Build.VERSION.SDK_INT < 28) {
                                    try {
                                        AromeInit.startTransActivity();
                                    } catch (Throwable th) {
                                        com.alipay.arome.aromecli.a.c("startTransActivity failed");
                                    }
                                } else {
                                    com.alipay.arome.aromecli.a.c("start foreground service for debug!");
                                    AromeInit.sApplicationContext.startForegroundService(intent);
                                }
                                if (!AromeInit.sApplicationContext.bindService(intent, connection, 1)) {
                                    com.alipay.arome.aromecli.a.c("bind service failed again !");
                                    AromeInit.reset();
                                    Callback callback2 = Callback.this;
                                    if (callback2 != null) {
                                        callback2.postInit(false, 3, "bind service failed");
                                    }
                                }
                            }
                        }
                    };
                    com.alipay.arome.aromecli.a.b("AromeInit create service binder " + sServiceBinder);
                }
                sInitOptions = options;
                sCountDownLatch = new CountDownLatch(1);
                sIsBinding.set(true);
                sServiceBinder.a(new a(options, callback));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void startTransActivity() {
        Intent transApp = new Intent();
        transApp.setClassName("com.alipay.arome.app", "com.alipay.mobile.aromeservice.TransProcessPayActivity");
        transApp.setPackage("com.alipay.arome.app");
        try {
            transApp.addFlags(Videoio.CAP_INTELPERC_IMAGE_GENERATOR);
            sApplicationContext.startActivity(transApp);
        } catch (Throwable e) {
            com.alipay.arome.aromecli.a.c("startTransActivity failed: " + e.getMessage());
        }
        Thread.sleep(200L);
    }

    protected static void reset() {
        com.alipay.arome.aromecli.a.b("AromeInit reset status!");
        sIsBinding.set(false);
        sIsBound.set(false);
        sServiceInterface = null;
        synchronized (sConnectionLock) {
            if (sLastBindedConn != null) {
                sApplicationContext.unbindService(sLastBindedConn);
                sLastBindedConn = null;
            }
        }
    }

    private static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class a implements ServiceConnection {
        private final AromeInitOptions a;
        private final Callback b;
        private boolean c = false;

        public a(AromeInitOptions options, Callback callback) {
            this.a = options;
            this.b = callback;
        }

        public final void a() {
            com.alipay.arome.aromecli.a.b("AromeInit setDisabled with conn: " + this);
            this.c = true;
        }

        @Override // android.content.ServiceConnection
        public final void onServiceConnected(ComponentName className, final IBinder service) {
            if (this.c) {
                com.alipay.arome.aromecli.a.c("AromeInit onServiceConnected but disabled! " + AromeInit.sServiceBinder + " with conn: " + this);
                return;
            }
            com.alipay.arome.aromecli.a.b("AromeInit onServiceConnected " + AromeInit.sServiceBinder + " with conn: " + this);
            IAromeServiceInterface unused = AromeInit.sServiceInterface = IAromeServiceInterface.Stub.asInterface(service);
            ServiceConnection unused2 = AromeInit.sLastBindedConn = this;
            try {
                service.linkToDeath(new IBinder.DeathRecipient() { // from class: com.alipay.arome.aromecli.AromeInit.a.1
                    @Override // android.os.IBinder.DeathRecipient
                    public final void binderDied() {
                        com.alipay.arome.aromecli.a.c("binderDied");
                        if (a.this.b != null) {
                            a.this.b.serverDied();
                        }
                        AromeInit.reset();
                        service.unlinkToDeath(this, 0);
                    }
                }, 0);
            } catch (Throwable e) {
                com.alipay.arome.aromecli.a.a("linkToDeath", e);
            }
            AromeInit.sIsBinding.set(false);
            AromeInit.sIsBound.set(true);
            AromeInit.sCountDownLatch.countDown();
            if (this.a != null) {
                try {
                    AromeInit.sServiceInterface.setInitOptions(new InitOptions.Builder().loginMode(this.a.loginMode).hardwareType(this.a.hardwareType).hardwareName(this.a.hardwareName).themeConfig(this.a.themeConfig).deviceConfig(this.a.deviceConfig).build());
                } catch (RemoteException e2) {
                    e2.printStackTrace();
                }
            }
            Callback callback = this.b;
            if (callback != null) {
                callback.postInit(true, 0, null);
            }
            com.alipay.arome.aromecli.a.a("onServiceConnected");
        }

        @Override // android.content.ServiceConnection
        public final void onServiceDisconnected(ComponentName className) {
            if (this.c) {
                com.alipay.arome.aromecli.a.c("onServiceDisconnected " + AromeInit.sServiceBinder + " with coon: " + this);
                return;
            }
            com.alipay.arome.aromecli.a.c("onServiceDisconnected " + AromeInit.sServiceBinder + " with coon: " + this);
            ServiceConnection unused = AromeInit.sLastBindedConn = null;
            AromeInit.reset();
        }
    }
}
