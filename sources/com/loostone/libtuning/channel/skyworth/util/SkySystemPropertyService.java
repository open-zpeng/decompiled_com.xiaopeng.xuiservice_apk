package com.loostone.libtuning.channel.skyworth.util;

import com.loostone.libtuning.channel.skyworth.bean.SkySystemProperty;
import com.loostone.libtuning.channel.skyworth.inf.ISkySystemProperty;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 !2\u00020\u0001:\u0002!\"B\u0007¢\u0006\u0004\b \u0010\u0004J\u000f\u0010\u0003\u001a\u00020\u0002H\u0002¢\u0006\u0004\b\u0003\u0010\u0004J\r\u0010\u0005\u001a\u00020\u0002¢\u0006\u0004\b\u0005\u0010\u0004J\r\u0010\u0006\u001a\u00020\u0002¢\u0006\u0004\b\u0006\u0010\u0004J\r\u0010\u0007\u001a\u00020\u0002¢\u0006\u0004\b\u0007\u0010\u0004J\r\u0010\b\u001a\u00020\u0002¢\u0006\u0004\b\b\u0010\u0004J\u0015\u0010\u000b\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\t¢\u0006\u0004\b\u000b\u0010\fJ\u0015\u0010\r\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\t¢\u0006\u0004\b\r\u0010\fR\u0016\u0010\u000f\u001a\u00020\u000e8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u000f\u0010\u0010R\u001c\u0010\u0012\u001a\b\u0018\u00010\u0011R\u00020\u00008\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0012\u0010\u0013R#\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00150\u00148B@\u0002X\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0016\u0010\u0017\u001a\u0004\b\u0018\u0010\u0019R#\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\t0\u001b8B@\u0002X\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u001c\u0010\u0017\u001a\u0004\b\u001d\u0010\u001e¨\u0006#"}, d2 = {"Lcom/loostone/libtuning/channel/skyworth/util/SkySystemPropertyService;", "", "", "dealWithMute", "()V", "start", "resume", "pause", "stop", "Lcom/loostone/libtuning/channel/skyworth/inf/ISkySystemProperty;", "listener", "registerCallback", "(Lcom/loostone/libtuning/channel/skyworth/inf/ISkySystemProperty;)V", "unRegisterCallback", "", "startListen", "Z", "Lcom/loostone/libtuning/channel/skyworth/util/SkySystemPropertyService$SystemPropertyThread;", "listenThread", "Lcom/loostone/libtuning/channel/skyworth/util/SkySystemPropertyService$SystemPropertyThread;", "Ljava/util/ArrayList;", "Lcom/loostone/libtuning/channel/skyworth/bean/SkySystemProperty;", "systemProperties$delegate", "Lkotlin/Lazy;", "getSystemProperties", "()Ljava/util/ArrayList;", "systemProperties", "Ljava/util/LinkedList;", "callBackList$delegate", "getCallBackList", "()Ljava/util/LinkedList;", "callBackList", "<init>", "Companion", "SystemPropertyThread", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public final class SkySystemPropertyService {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private static final String TAG = "MicMuteService";
    @Nullable
    private SystemPropertyThread listenThread;
    private boolean startListen = true;
    @NotNull
    private final Lazy callBackList$delegate = LazyKt.lazy(new Function0<LinkedList<ISkySystemProperty>>() { // from class: com.loostone.libtuning.channel.skyworth.util.SkySystemPropertyService$callBackList$2
        @Override // kotlin.jvm.functions.Function0
        @NotNull
        public final LinkedList<ISkySystemProperty> invoke() {
            return new LinkedList<>();
        }
    });
    @NotNull
    private final Lazy systemProperties$delegate = LazyKt.lazy(new Function0<ArrayList<SkySystemProperty>>() { // from class: com.loostone.libtuning.channel.skyworth.util.SkySystemPropertyService$systemProperties$2
        @Override // kotlin.jvm.functions.Function0
        @NotNull
        public final ArrayList<SkySystemProperty> invoke() {
            ArrayList<SkySystemProperty> arrayList = new ArrayList<>();
            arrayList.add(new SkySystemProperty());
            return arrayList;
        }
    });

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0005\u0010\u0006R\u0016\u0010\u0003\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004¨\u0006\u0007"}, d2 = {"Lcom/loostone/libtuning/channel/skyworth/util/SkySystemPropertyService$Companion;", "", "", "TAG", "Ljava/lang/String;", "<init>", "()V", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes4.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u000e\u0010\u000fJ\u000f\u0010\u0003\u001a\u00020\u0002H\u0002¢\u0006\u0004\b\u0003\u0010\u0004J\r\u0010\u0005\u001a\u00020\u0002¢\u0006\u0004\b\u0005\u0010\u0004J\r\u0010\u0006\u001a\u00020\u0002¢\u0006\u0004\b\u0006\u0010\u0004J\u000f\u0010\u0007\u001a\u00020\u0002H\u0016¢\u0006\u0004\b\u0007\u0010\u0004R\u0016\u0010\t\u001a\u00020\b8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\t\u0010\nR\u0016\u0010\f\u001a\u00020\u000b8\u0002@\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\f\u0010\r¨\u0006\u0010"}, d2 = {"Lcom/loostone/libtuning/channel/skyworth/util/SkySystemPropertyService$SystemPropertyThread;", "Ljava/lang/Thread;", "", "onPause", "()V", "pauseThread", "resumeThread", "run", "", "pause", "Z", "Ljava/lang/Object;", "lock", "Ljava/lang/Object;", "<init>", "(Lcom/loostone/libtuning/channel/skyworth/util/SkySystemPropertyService;)V", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes4.dex */
    public final class SystemPropertyThread extends Thread {
        @NotNull
        private final Object lock;
        private boolean pause;
        public final /* synthetic */ SkySystemPropertyService this$0;

        public SystemPropertyThread(SkySystemPropertyService this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this.this$0 = this$0;
            this.lock = new Object();
        }

        private final void onPause() {
            synchronized (this.lock) {
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public final void pauseThread() {
            this.pause = true;
        }

        public final void resumeThread() {
            this.pause = false;
            synchronized (this.lock) {
                this.lock.notifyAll();
            }
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (this.this$0.startListen) {
                try {
                    while (this.pause) {
                        onPause();
                    }
                    this.this$0.dealWithMute();
                    Thread.sleep(1000L);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void dealWithMute() {
        int i;
        Object invoke;
        try {
            invoke = Class.forName("android.os.SystemProperties").getMethod("getInt", String.class, Integer.TYPE).invoke(null, "third.get.aistandbymode", 0);
        } catch (Exception e) {
            e.printStackTrace();
            i = 0;
        }
        if (invoke == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
        }
        i = ((Integer) invoke).intValue();
        boolean z = i != 0;
        if (getSystemProperties().get(0).getLastState() != i) {
            try {
                Iterator<ISkySystemProperty> it = getCallBackList().iterator();
                while (it.hasNext()) {
                    it.next().onDormancy(z);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        getSystemProperties().get(0).setLastState(i);
    }

    private final LinkedList<ISkySystemProperty> getCallBackList() {
        return (LinkedList) this.callBackList$delegate.getValue();
    }

    private final ArrayList<SkySystemProperty> getSystemProperties() {
        return (ArrayList) this.systemProperties$delegate.getValue();
    }

    public final void pause() {
        SystemPropertyThread systemPropertyThread = this.listenThread;
        if (systemPropertyThread == null) {
            return;
        }
        systemPropertyThread.pauseThread();
    }

    public final void registerCallback(@NotNull ISkySystemProperty listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        getCallBackList().add(listener);
    }

    public final void resume() {
        SystemPropertyThread systemPropertyThread = this.listenThread;
        if (systemPropertyThread == null) {
            return;
        }
        systemPropertyThread.resumeThread();
    }

    public final void start() {
        this.startListen = true;
        SystemPropertyThread systemPropertyThread = new SystemPropertyThread(this);
        this.listenThread = systemPropertyThread;
        systemPropertyThread.start();
    }

    public final void stop() {
        this.startListen = false;
        this.listenThread = null;
    }

    public final void unRegisterCallback(@NotNull ISkySystemProperty listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        getCallBackList().remove(listener);
    }
}
