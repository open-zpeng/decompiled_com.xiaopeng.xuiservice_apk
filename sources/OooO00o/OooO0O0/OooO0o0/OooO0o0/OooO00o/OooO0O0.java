package OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.lzy.okgo.model.Progress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class OooO0O0 {
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public final Context f573OooO00o;
    @NotNull
    public final OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o.OooO00o OooO0O0;
    @NotNull
    public final Lazy OooO0OO;
    @NotNull
    public final Lazy OooO0Oo;
    @NotNull
    public final Lazy OooO0o;
    @NotNull
    public final Lazy OooO0o0;
    @NotNull
    public final Lazy OooO0oO;
    @NotNull
    public final OooOOO OooO0oo;

    /* loaded from: classes.dex */
    public static final class OooO extends Lambda implements Function0<IntentFilter> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final OooO f574OooO00o = new OooO();

        public OooO() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public IntentFilter invoke() {
            return new IntentFilter();
        }
    }

    /* loaded from: classes.dex */
    public final class OooO00o implements OooO0OO {

        /* renamed from: OooO00o  reason: collision with root package name */
        public final /* synthetic */ OooO0O0 f575OooO00o;

        public OooO00o(OooO0O0 this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this.f575OooO00o = this$0;
        }

        @Override // OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o.OooO0O0.OooO0OO
        public void OooO00o(@Nullable Context context, @NotNull Intent intent, @Nullable UsbDevice usbDevice) {
            Intrinsics.checkNotNullParameter(intent, "intent");
            if (context == null || usbDevice == null) {
                return;
            }
            String msg = Intrinsics.stringPlus("USB设备接入 ", usbDevice);
            Intrinsics.checkNotNullParameter("DongleEventManager", Progress.TAG);
            Intrinsics.checkNotNullParameter(msg, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "DongleEventManager -> " + msg);
            }
            if (OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o.f237OooO00o.OooO00o(usbDevice)) {
                OooO0O0 oooO0O0 = this.f575OooO00o;
                if (oooO0O0.OooO0O0.OooO0o0 == null) {
                    OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO00o(usbDevice, oooO0O0.f573OooO00o);
                    UsbManager OooO0O0 = this.f575OooO00o.OooO0O0();
                    Object value = this.f575OooO00o.OooO0oO.getValue();
                    Intrinsics.checkNotNullExpressionValue(value, "<get-mPermissionIntent>(...)");
                    OooO0O0.requestPermission(usbDevice, (PendingIntent) value);
                }
            }
        }
    }

    /* renamed from: OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o.OooO0O0$OooO0O0  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public final class C0021OooO0O0 implements OooO0OO {

        /* renamed from: OooO00o  reason: collision with root package name */
        public final /* synthetic */ OooO0O0 f576OooO00o;

        public C0021OooO0O0(OooO0O0 this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this.f576OooO00o = this$0;
        }

        @Override // OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o.OooO0O0.OooO0OO
        public void OooO00o(@Nullable Context context, @NotNull Intent intent, @Nullable UsbDevice usbDevice) {
            Intrinsics.checkNotNullParameter(intent, "intent");
            String msg = Intrinsics.stringPlus("  USB设备拔出 ", usbDevice);
            Intrinsics.checkNotNullParameter("DongleEventManager", Progress.TAG);
            Intrinsics.checkNotNullParameter(msg, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", "DongleEventManager -> " + msg);
            }
            OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o.OooO00o oooO00o = this.f576OooO00o.OooO0O0;
            oooO00o.getClass();
            if (usbDevice != null && OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o.f237OooO00o.OooO00o(usbDevice)) {
                OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO00o cachedDongleDevice = null;
                for (OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO00o oooO00o2 : oooO00o.OooO0OO()) {
                    UsbDevice usbDevice2 = oooO00o2.f228OooO00o;
                    if (usbDevice2 != null && Intrinsics.areEqual(usbDevice2, usbDevice)) {
                        cachedDongleDevice = oooO00o2;
                    }
                }
                if (cachedDongleDevice == null) {
                    return;
                }
                oooO00o.OooO0OO().remove(cachedDongleDevice);
                OooO0O0 mEventManager = oooO00o.OooO0O0.getMEventManager();
                synchronized (mEventManager) {
                    Intrinsics.checkNotNullParameter(cachedDongleDevice, "cachedDongleDevice");
                    int i = 0;
                    int size = mEventManager.OooO00o().size();
                    if (size > 0) {
                        while (true) {
                            int i2 = i + 1;
                            mEventManager.OooO00o().get(i).onDeviceRemoved(cachedDongleDevice);
                            if (i2 >= size) {
                                break;
                            }
                            i = i2;
                        }
                    }
                }
                oooO00o.OooO0o0 = null;
            }
        }
    }

    /* loaded from: classes.dex */
    public interface OooO0OO {
        void OooO00o(@Nullable Context context, @NotNull Intent intent, @Nullable UsbDevice usbDevice);
    }

    /* loaded from: classes.dex */
    public final class OooO0o implements OooO0OO {

        /* renamed from: OooO00o  reason: collision with root package name */
        public final /* synthetic */ OooO0O0 f577OooO00o;

        public OooO0o(OooO0O0 this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this.f577OooO00o = this$0;
        }

        /* JADX WARN: Removed duplicated region for block: B:101:0x02cb  */
        /* JADX WARN: Removed duplicated region for block: B:88:0x0223  */
        @Override // OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o.OooO0O0.OooO0OO
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void OooO00o(@org.jetbrains.annotations.Nullable android.content.Context r17, @org.jetbrains.annotations.NotNull android.content.Intent r18, @org.jetbrains.annotations.Nullable android.hardware.usb.UsbDevice r19) {
            /*
                Method dump skipped, instructions count: 1049
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o.OooO0O0.OooO0o.OooO00o(android.content.Context, android.content.Intent, android.hardware.usb.UsbDevice):void");
        }
    }

    /* loaded from: classes.dex */
    public static final class OooOO0 extends Lambda implements Function0<ArrayList<OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0>> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final OooOO0 f578OooO00o = new OooOO0();

        public OooOO0() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public ArrayList<OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0> invoke() {
            return new ArrayList<>();
        }
    }

    /* loaded from: classes.dex */
    public static final class OooOO0O extends Lambda implements Function0<HashMap<String, OooO0OO>> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final OooOO0O f579OooO00o = new OooOO0O();

        public OooOO0O() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public HashMap<String, OooO0OO> invoke() {
            return new HashMap<>();
        }
    }

    /* loaded from: classes.dex */
    public static final class OooOOO extends BroadcastReceiver {
        public OooOOO() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(@Nullable Context context, @Nullable Intent intent) {
            if (intent == null) {
                return;
            }
            UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra("device");
            OooO0OO oooO0OO = (OooO0OO) ((HashMap) OooO0O0.this.OooO0OO.getValue()).get(intent.getAction());
            if (oooO0OO == null) {
                return;
            }
            oooO0OO.OooO00o(context, intent, usbDevice);
        }
    }

    /* loaded from: classes.dex */
    public static final class OooOOO0 extends Lambda implements Function0<PendingIntent> {
        public OooOOO0() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public PendingIntent invoke() {
            Intent intent = new Intent();
            intent.setAction("com.loostone.tuning.USB_PERMISSION");
            return PendingIntent.getBroadcast(OooO0O0.this.f573OooO00o, 0, intent, 0);
        }
    }

    /* loaded from: classes.dex */
    public static final class OooOOOO extends Lambda implements Function0<UsbManager> {
        public OooOOOO() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public UsbManager invoke() {
            Object systemService = OooO0O0.this.f573OooO00o.getSystemService("usb");
            if (systemService != null) {
                return (UsbManager) systemService;
            }
            throw new NullPointerException("null cannot be cast to non-null type android.hardware.usb.UsbManager");
        }
    }

    public OooO0O0(@NotNull Context mContext, @NotNull OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o.OooO00o mDeviceManager) {
        Intrinsics.checkNotNullParameter(mContext, "mContext");
        Intrinsics.checkNotNullParameter(mDeviceManager, "mDeviceManager");
        this.f573OooO00o = mContext;
        this.OooO0O0 = mDeviceManager;
        this.OooO0OO = LazyKt.lazy(OooOO0O.f579OooO00o);
        this.OooO0Oo = LazyKt.lazy(OooOO0.f578OooO00o);
        this.OooO0o0 = LazyKt.lazy(OooO.f574OooO00o);
        this.OooO0o = LazyKt.lazy(new OooOOOO());
        this.OooO0oO = LazyKt.lazy(new OooOOO0());
        this.OooO0oo = new OooOOO();
    }

    public final void OooO00o(String str, OooO0OO oooO0OO) {
        ((HashMap) this.OooO0OO.getValue()).put(str, oooO0OO);
        ((IntentFilter) this.OooO0o0.getValue()).addAction(str);
    }

    public final UsbManager OooO0O0() {
        return (UsbManager) this.OooO0o.getValue();
    }

    public final synchronized void OooO0O0(@NotNull OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0 callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        OooO00o().remove(callback);
        Intrinsics.checkNotNullParameter("DongleEventManager", Progress.TAG);
        Intrinsics.checkNotNullParameter("  取消注册Callback成功", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "DongleEventManager ->   取消注册Callback成功");
        }
    }

    public final List<OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0> OooO00o() {
        return (List) this.OooO0Oo.getValue();
    }

    public final synchronized void OooO00o(@NotNull OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0 callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        OooO00o().add(callback);
        Intrinsics.checkNotNullParameter("DongleEventManager", Progress.TAG);
        Intrinsics.checkNotNullParameter("  注册Callback成功", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "DongleEventManager ->   注册Callback成功");
        }
    }
}
