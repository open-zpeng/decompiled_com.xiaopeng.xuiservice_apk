package com.loostone.libtuning.process.dongle;

import OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o.OooO0O0;
import android.app.PendingIntent;
import android.content.Context;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.loostone.libtuning.inf.extern.IDongleListener;
import com.lzy.okgo.model.Progress;
import com.xiaopeng.xuiservice.uvccamera.usb.USBMonitor;
import java.util.HashMap;
import java.util.LinkedList;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u0000 #2\u00020\u0001:\u0001$B\u000f\u0012\u0006\u0010\u000f\u001a\u00020\u000e¢\u0006\u0004\b!\u0010\"J\r\u0010\u0003\u001a\u00020\u0002¢\u0006\u0004\b\u0003\u0010\u0004J\r\u0010\u0005\u001a\u00020\u0002¢\u0006\u0004\b\u0005\u0010\u0004J\r\u0010\u0007\u001a\u00020\u0006¢\u0006\u0004\b\u0007\u0010\bJ\u0015\u0010\u000b\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\t¢\u0006\u0004\b\u000b\u0010\fJ\u0015\u0010\r\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\t¢\u0006\u0004\b\r\u0010\fR\u0016\u0010\u000f\u001a\u00020\u000e8\u0002@\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u000f\u0010\u0010R\u001d\u0010\u0016\u001a\u00020\u00118F@\u0006X\u0086\u0084\u0002¢\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0014\u0010\u0015R#\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\t0\u00178B@\u0002X\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0018\u0010\u0013\u001a\u0004\b\u0019\u0010\u001aR\u001d\u0010 \u001a\u00020\u001c8F@\u0006X\u0086\u0084\u0002¢\u0006\f\n\u0004\b\u001d\u0010\u0013\u001a\u0004\b\u001e\u0010\u001f¨\u0006%"}, d2 = {"Lcom/loostone/libtuning/process/dongle/LocalDongleManager;", "", "", "init", "()V", "release", "", "dongleStatus", "()I", "Lcom/loostone/libtuning/inf/extern/IDongleListener;", "listener", "registerDongleListener", "(Lcom/loostone/libtuning/inf/extern/IDongleListener;)V", "unRegisterDongleListener", "Landroid/content/Context;", "mContext", "Landroid/content/Context;", "LOooO00o/OooO0O0/OooO0o0/OooO0o0/OooO00o/OooO00o;", "mCachedDongleDeviceManager$delegate", "Lkotlin/Lazy;", "getMCachedDongleDeviceManager", "()LOooO00o/OooO0O0/OooO0o0/OooO0o0/OooO00o/OooO00o;", "mCachedDongleDeviceManager", "Ljava/util/LinkedList;", "dongleListeners$delegate", "getDongleListeners", "()Ljava/util/LinkedList;", "dongleListeners", "LOooO00o/OooO0O0/OooO0o0/OooO0o0/OooO00o/OooO0O0;", "mEventManager$delegate", "getMEventManager", "()LOooO00o/OooO0O0/OooO0o0/OooO0o0/OooO00o/OooO0O0;", "mEventManager", "<init>", "(Landroid/content/Context;)V", "Companion", "OooO00o", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public final class LocalDongleManager {
    @NotNull
    public static final OooO00o Companion = new OooO00o();
    @NotNull
    private static final String TAG = "DongleController";
    @NotNull
    private final Lazy dongleListeners$delegate;
    @NotNull
    private final Lazy mCachedDongleDeviceManager$delegate;
    @NotNull
    private final Context mContext;
    @NotNull
    private final Lazy mEventManager$delegate;

    /* loaded from: classes4.dex */
    public static final class OooO00o {
    }

    /* loaded from: classes4.dex */
    public static final class OooO0O0 extends Lambda implements Function0<LinkedList<IDongleListener>> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final OooO0O0 f610OooO00o = new OooO0O0();

        public OooO0O0() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public LinkedList<IDongleListener> invoke() {
            return new LinkedList<>();
        }
    }

    /* loaded from: classes4.dex */
    public static final class OooO0OO extends Lambda implements Function0<OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o.OooO00o> {
        public OooO0OO() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o.OooO00o invoke() {
            return new OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o.OooO00o(LocalDongleManager.this.mContext, LocalDongleManager.this);
        }
    }

    /* loaded from: classes4.dex */
    public static final class OooO0o extends Lambda implements Function0<OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o.OooO0O0> {
        public OooO0o() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o.OooO0O0 invoke() {
            OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o.OooO0O0 oooO0O0 = new OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o.OooO0O0(LocalDongleManager.this.mContext, LocalDongleManager.this.getMCachedDongleDeviceManager());
            oooO0O0.OooO00o(new OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o.OooO0OO(LocalDongleManager.this));
            return oooO0O0;
        }
    }

    public LocalDongleManager(@NotNull Context mContext) {
        Intrinsics.checkNotNullParameter(mContext, "mContext");
        this.mContext = mContext;
        this.dongleListeners$delegate = LazyKt.lazy(OooO0O0.f610OooO00o);
        this.mCachedDongleDeviceManager$delegate = LazyKt.lazy(new OooO0OO());
        this.mEventManager$delegate = LazyKt.lazy(new OooO0o());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final LinkedList<IDongleListener> getDongleListeners() {
        return (LinkedList) this.dongleListeners$delegate.getValue();
    }

    public final int dongleStatus() {
        return getMCachedDongleDeviceManager().OooO0OO().size() > 0 ? 2 : 0;
    }

    @NotNull
    public final OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o.OooO00o getMCachedDongleDeviceManager() {
        return (OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o.OooO00o) this.mCachedDongleDeviceManager$delegate.getValue();
    }

    @NotNull
    public final OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o.OooO0O0 getMEventManager() {
        return (OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o.OooO0O0) this.mEventManager$delegate.getValue();
    }

    public final void init() {
        OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o.OooO0O0 mEventManager = getMEventManager();
        mEventManager.getClass();
        mEventManager.OooO00o(USBMonitor.ACTION_USB_DEVICE_ATTACHED, new OooO0O0.OooO00o(mEventManager));
        mEventManager.OooO00o("android.hardware.usb.action.USB_DEVICE_DETACHED", new OooO0O0.C0021OooO0O0(mEventManager));
        mEventManager.OooO00o("com.loostone.tuning.USB_PERMISSION", new OooO0O0.OooO0o(mEventManager));
        mEventManager.f573OooO00o.registerReceiver(mEventManager.OooO0oo, (IntentFilter) mEventManager.OooO0o0.getValue());
        HashMap<String, UsbDevice> deviceList = mEventManager.OooO0O0().getDeviceList();
        if (deviceList != null) {
            for (UsbDevice usbDevice : deviceList.values()) {
                OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o oooO00o = OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o.f237OooO00o;
                Intrinsics.checkNotNullExpressionValue(usbDevice, "usbDevice");
                if (oooO00o.OooO00o(usbDevice) && mEventManager.OooO0O0.OooO0o0 == null) {
                    OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO00o(usbDevice, mEventManager.f573OooO00o);
                    UsbManager OooO0O02 = mEventManager.OooO0O0();
                    Object value = mEventManager.OooO0oO.getValue();
                    Intrinsics.checkNotNullExpressionValue(value, "<get-mPermissionIntent>(...)");
                    OooO0O02.requestPermission(usbDevice, (PendingIntent) value);
                }
            }
        }
        Intrinsics.checkNotNullParameter("DongleEventManager", Progress.TAG);
        Intrinsics.checkNotNullParameter("init ------> 初始化成功", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "DongleEventManager -> init ------> 初始化成功");
        }
    }

    public final void registerDongleListener(@NotNull IDongleListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        getDongleListeners().add(listener);
    }

    public final void release() {
        OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o.OooO0O0 mEventManager = getMEventManager();
        mEventManager.getClass();
        Intrinsics.checkNotNullParameter("DongleEventManager", Progress.TAG);
        Intrinsics.checkNotNullParameter("  release", NotificationCompat.CATEGORY_MESSAGE);
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "DongleEventManager ->   release");
        }
        try {
            mEventManager.f573OooO00o.unregisterReceiver(mEventManager.OooO0oo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o.OooO00o mCachedDongleDeviceManager = getMCachedDongleDeviceManager();
        mCachedDongleDeviceManager.OooO0o0 = null;
        mCachedDongleDeviceManager.OooO0OO().clear();
    }

    public final void unRegisterDongleListener(@NotNull IDongleListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        getDongleListeners().remove(listener);
    }
}
