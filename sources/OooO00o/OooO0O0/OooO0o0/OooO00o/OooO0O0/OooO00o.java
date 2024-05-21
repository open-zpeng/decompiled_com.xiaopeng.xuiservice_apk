package OooO00o.OooO0O0.OooO0o0.OooO00o.OooO0O0;

import android.hardware.usb.UsbDevice;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.loostone.libtuning.data.bean.HidItem;
import com.loostone.libtuning.inf.extern.IBaseKeyService;
import com.loostone.libtuning.inf.extern.IKeyListener;
import com.loostone.libtuning.inf.extern.IMicControl;
import com.loostone.libtuning.inf.extern.IMicInfoListener;
import com.lzy.okgo.model.Progress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class OooO00o implements IBaseKeyService {
    @Nullable
    public C0010OooO00o OooO0Oo;
    @Nullable

    /* renamed from: OooO00o  reason: collision with root package name */
    public UsbDevice f264OooO00o = null;
    public boolean OooO0O0 = true;
    @NotNull
    public final Lazy OooO0OO = LazyKt.lazy(new OooOO0());
    @NotNull
    public final Lazy OooO0o0 = LazyKt.lazy(OooO0o.f269OooO00o);
    @NotNull
    public final Lazy OooO0o = LazyKt.lazy(OooOOO0.f272OooO00o);
    @NotNull
    public final Lazy OooO0oO = LazyKt.lazy(OooO.f265OooO00o);
    @NotNull
    public final Lazy OooO0oo = LazyKt.lazy(OooO0OO.f268OooO00o);
    @NotNull
    public final Lazy OooO = LazyKt.lazy(OooOO0O.f271OooO00o);
    @NotNull
    public final Lazy OooOO0 = LazyKt.lazy(OooO0O0.f267OooO00o);

    /* loaded from: classes.dex */
    public static final class OooO extends Lambda implements Function0<Boolean> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final OooO f265OooO00o = new OooO();

        public OooO() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public Boolean invoke() {
            OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO.f279OooO00o.getClass();
            Integer num = (Integer) ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO.OooOOOO.getValue()).OooO00o();
            return Boolean.valueOf((num == null ? 0 : num.intValue()) == 1);
        }
    }

    /* renamed from: OooO00o.OooO0O0.OooO0o0.OooO00o.OooO0O0.OooO00o$OooO00o  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public final class C0010OooO00o extends Thread {
        @NotNull

        /* renamed from: OooO00o  reason: collision with root package name */
        public final Object f266OooO00o;
        public boolean OooO0O0;
        public final /* synthetic */ OooO00o OooO0OO;

        public C0010OooO00o(OooO00o this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this.OooO0OO = this$0;
            this.f266OooO00o = new Object();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (this.OooO0OO.OooO0O0) {
                try {
                    while (this.OooO0O0) {
                        synchronized (this.f266OooO00o) {
                            try {
                                this.f266OooO00o.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    byte[] OooO0O0 = this.OooO0OO.OooO0O0().OooO0O0();
                    OooO00o oooO00o = this.OooO0OO;
                    oooO00o.OooO00o(0, (byte) (OooO0O0[0] | OooO0O0[1]));
                    oooO00o.OooO00o(1, OooO0O0[0]);
                    oooO00o.OooO00o(2, OooO0O0[1]);
                    Thread.sleep(50L);
                    if (((Boolean) this.OooO0OO.OooO0oO.getValue()).booleanValue()) {
                        OooO00o.OooO0O0.OooO0o0.OooO0O0.OooO0OO.OooO0O0 OooO0O02 = this.OooO0OO.OooO0O0();
                        OooO0O02.getClass();
                        byte[] OooO00o2 = OooO00o.OooO0O0.OooO0o0.OooO0O0.OooO0OO.OooO0O0.OooO00o(OooO0O02, new byte[]{-16, 2, 0}, (byte[]) null, 2);
                        OooO00o oooO00o2 = this.OooO0OO;
                        OooO00o.OooO00o(oooO00o2, 1, OooO00o2[0]);
                        OooO00o.OooO00o(oooO00o2, 2, OooO00o2[1]);
                        Thread.sleep(50L);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                    this.OooO0OO.OooO00o(0, 0);
                    return;
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class OooO0O0 extends Lambda implements Function0<HashSet<Integer>> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final OooO0O0 f267OooO00o = new OooO0O0();

        public OooO0O0() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public HashSet<Integer> invoke() {
            HashSet<Integer> hashSet = new HashSet<>();
            hashSet.add(0);
            hashSet.add(32);
            hashSet.add(64);
            hashSet.add(66);
            hashSet.add(68);
            hashSet.add(72);
            hashSet.add(96);
            return hashSet;
        }
    }

    /* loaded from: classes.dex */
    public static final class OooO0OO extends Lambda implements Function0<ArrayList<HidItem>> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final OooO0OO f268OooO00o = new OooO0OO();

        public OooO0OO() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public ArrayList<HidItem> invoke() {
            ArrayList<HidItem> arrayList = new ArrayList<>();
            arrayList.add(new HidItem(0, 2, -3));
            arrayList.add(new HidItem(0, 4, -4));
            arrayList.add(new HidItem(0, 8, -5));
            arrayList.add(new HidItem(0, 32, -2));
            arrayList.add(new HidItem(0, 64, -1));
            arrayList.add(new HidItem(1, 64, -1));
            arrayList.add(new HidItem(2, 64, -1));
            return arrayList;
        }
    }

    /* loaded from: classes.dex */
    public static final class OooO0o extends Lambda implements Function0<LinkedList<IKeyListener>> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final OooO0o f269OooO00o = new OooO0o();

        public OooO0o() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public LinkedList<IKeyListener> invoke() {
            return new LinkedList<>();
        }
    }

    /* loaded from: classes.dex */
    public static final class OooOO0 extends Lambda implements Function0<OooO00o.OooO0O0.OooO0o0.OooO0O0.OooO0OO.OooO0O0> {
        public OooOO0() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public OooO00o.OooO0O0.OooO0o0.OooO0O0.OooO0OO.OooO0O0 invoke() {
            return new OooO00o.OooO0O0.OooO0o0.OooO0O0.OooO0OO.OooO0O0(OooO00o.this.f264OooO00o);
        }
    }

    /* loaded from: classes.dex */
    public static final class OooOO0O extends Lambda implements Function0<ArrayList<HidItem>> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final OooOO0O f271OooO00o = new OooOO0O();

        public OooOO0O() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public ArrayList<HidItem> invoke() {
            ArrayList<HidItem> arrayList = new ArrayList<>();
            arrayList.add(new HidItem(1, 0, -100));
            arrayList.add(new HidItem(2, 0, -100));
            return arrayList;
        }
    }

    /* loaded from: classes.dex */
    public static final class OooOOO0 extends Lambda implements Function0<LinkedList<IMicInfoListener>> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final OooOOO0 f272OooO00o = new OooOOO0();

        public OooOOO0() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public LinkedList<IMicInfoListener> invoke() {
            return new LinkedList<>();
        }
    }

    public OooO00o(@Nullable UsbDevice usbDevice) {
    }

    public final LinkedList<IKeyListener> OooO00o() {
        return (LinkedList) this.OooO0o0.getValue();
    }

    public final OooO00o.OooO0O0.OooO0o0.OooO0O0.OooO0OO.OooO0O0 OooO0O0() {
        return (OooO00o.OooO0O0.OooO0o0.OooO0O0.OooO0OO.OooO0O0) this.OooO0OO.getValue();
    }

    public final LinkedList<IMicInfoListener> OooO0OO() {
        return (LinkedList) this.OooO0o.getValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IBaseKeyService
    @NotNull
    public IMicControl getMicControl() {
        return OooO0O0();
    }

    @Override // com.loostone.libtuning.inf.extern.IBaseKeyService
    public void pause() {
        C0010OooO00o c0010OooO00o = this.OooO0Oo;
        if (c0010OooO00o == null) {
            return;
        }
        c0010OooO00o.OooO0O0 = true;
    }

    @Override // com.loostone.libtuning.inf.extern.IBaseKeyService
    public void registerKeyListener(@NotNull IKeyListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        OooO00o().add(listener);
    }

    @Override // com.loostone.libtuning.inf.extern.IBaseKeyService
    public void registerMicInfoListener(@NotNull IMicInfoListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        OooO0OO().add(listener);
    }

    @Override // com.loostone.libtuning.inf.extern.IBaseKeyService
    public void resume() {
        C0010OooO00o c0010OooO00o = this.OooO0Oo;
        if (c0010OooO00o == null) {
            return;
        }
        c0010OooO00o.OooO0O0 = false;
        synchronized (c0010OooO00o.f266OooO00o) {
            c0010OooO00o.f266OooO00o.notifyAll();
        }
    }

    @Override // com.loostone.libtuning.inf.extern.IBaseKeyService
    public void setDevice(@Nullable UsbDevice usbDevice) {
        this.f264OooO00o = usbDevice;
        OooO0O0().OooO0O0 = usbDevice;
    }

    @Override // com.loostone.libtuning.inf.extern.IBaseKeyService
    public void start() {
        Intrinsics.checkNotNullParameter("HidService", Progress.TAG);
        Intrinsics.checkNotNullParameter("start()", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "HidService -> start()");
        }
        OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO.f279OooO00o.getClass();
        Integer num = (Integer) ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO.OooOooO.getValue()).OooO00o();
        if (num != null && num.intValue() == 0) {
            OooO00o.OooO0O0.OooO0o0.OooO0O0.OooO0OO.OooO0O0 OooO0O02 = OooO0O0();
            OooO0O02.getClass();
            OooO0O02.OooO00o(new byte[]{-18, 8, 1}, true);
        }
        this.OooO0O0 = true;
        C0010OooO00o c0010OooO00o = new C0010OooO00o(this);
        c0010OooO00o.start();
        this.OooO0Oo = c0010OooO00o;
    }

    @Override // com.loostone.libtuning.inf.extern.IBaseKeyService
    public void stop() {
        Intrinsics.checkNotNullParameter("HidService", Progress.TAG);
        Intrinsics.checkNotNullParameter("stop()", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "HidService -> stop()");
        }
        this.OooO0O0 = false;
        this.OooO0Oo = null;
        OooO00o(0, 0);
    }

    @Override // com.loostone.libtuning.inf.extern.IBaseKeyService
    public void unRegisterKeyListener(@NotNull IKeyListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        OooO00o().remove(listener);
    }

    @Override // com.loostone.libtuning.inf.extern.IBaseKeyService
    public void unRegisterMicInfoListener(@NotNull IMicInfoListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        OooO0OO().remove(listener);
    }

    public static final void OooO00o(OooO00o oooO00o, int i, int i2) {
        Iterator it = ((ArrayList) oooO00o.OooO.getValue()).iterator();
        while (it.hasNext()) {
            HidItem hidItem = (HidItem) it.next();
            if (hidItem.getMicIndex() == i) {
                if (hidItem.getLastState() != i2) {
                    try {
                        Iterator<IMicInfoListener> it2 = oooO00o.OooO0OO().iterator();
                        while (it2.hasNext()) {
                            it2.next();
                            hidItem.getKeyCode();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                hidItem.setLastState(i2);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void OooO00o(int i, int i2) {
        if (((HashSet) this.OooOO0.getValue()).contains(Integer.valueOf(i2))) {
            Iterator it = ((ArrayList) this.OooO0oo.getValue()).iterator();
            while (it.hasNext()) {
                HidItem hidItem = (HidItem) it.next();
                if (hidItem.getMicIndex() == i) {
                    boolean z = (hidItem.getHidCode() & i2) > 0 ? 1 : 0;
                    int i3 = !z;
                    if (hidItem.getLastState() != i3) {
                        try {
                            Iterator<IKeyListener> it2 = OooO00o().iterator();
                            while (it2.hasNext()) {
                                IKeyListener next = it2.next();
                                int keyCode = hidItem.getKeyCode();
                                if (keyCode == -5) {
                                    next.onVolumeDown(i, z);
                                } else if (keyCode == -4) {
                                    next.onVolumeUp(i, z);
                                } else if (keyCode == -3) {
                                    next.onTVPhoneSwitch(i, z);
                                } else if (keyCode == -2) {
                                    next.onVoice(i, z);
                                } else if (keyCode == -1) {
                                    next.onPower(i, z);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    hidItem.setLastState(i3);
                }
            }
        }
    }
}
