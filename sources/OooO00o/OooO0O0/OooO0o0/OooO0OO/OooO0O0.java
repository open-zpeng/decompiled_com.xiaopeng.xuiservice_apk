package OooO00o.OooO0O0.OooO0o0.OooO0Oo;

import OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO00o;
import android.hardware.usb.UsbDevice;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public interface OooO0O0 {
    void onDeviceAddedWithHid(@NotNull OooO00o oooO00o);

    void onDeviceDeniedByHid(@NotNull OooO00o oooO00o);

    void onDeviceRemoved(@NotNull OooO00o oooO00o);

    void onDonglePermissionDenied();

    void onDonglePermissionGranted(@NotNull UsbDevice usbDevice);
}
