package OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO00o;

import android.hardware.usb.UsbDevice;
import com.loostone.libtuning.inf.extern.IDongleListener;
import com.loostone.libtuning.process.dongle.LocalDongleManager;
import java.util.Iterator;
import java.util.LinkedList;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class OooO0OO implements OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0 {

    /* renamed from: OooO00o  reason: collision with root package name */
    public final /* synthetic */ LocalDongleManager f583OooO00o;

    public OooO0OO(LocalDongleManager localDongleManager) {
        this.f583OooO00o = localDongleManager;
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0
    public void onDeviceAddedWithHid(@NotNull OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO00o cachedDevice) {
        LinkedList dongleListeners;
        Intrinsics.checkNotNullParameter(cachedDevice, "cachedDevice");
        dongleListeners = this.f583OooO00o.getDongleListeners();
        Iterator it = dongleListeners.iterator();
        while (it.hasNext()) {
            ((IDongleListener) it.next()).onDongleAttach();
        }
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0
    public void onDeviceDeniedByHid(@NotNull OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO00o cachedDevice) {
        Intrinsics.checkNotNullParameter(cachedDevice, "cachedDevice");
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0
    public void onDeviceRemoved(@NotNull OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO00o cachedDevice) {
        LinkedList dongleListeners;
        Intrinsics.checkNotNullParameter(cachedDevice, "cachedDevice");
        dongleListeners = this.f583OooO00o.getDongleListeners();
        Iterator it = dongleListeners.iterator();
        while (it.hasNext()) {
            ((IDongleListener) it.next()).onDongleDetach();
        }
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0
    public void onDonglePermissionDenied() {
    }

    @Override // OooO00o.OooO0O0.OooO0o0.OooO0Oo.OooO0O0
    public void onDonglePermissionGranted(@NotNull UsbDevice usbDevice) {
        Intrinsics.checkNotNullParameter(usbDevice, "usbDevice");
    }
}
