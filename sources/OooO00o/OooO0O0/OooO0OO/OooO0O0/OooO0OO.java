package OooO00o.OooO0O0.OooO0OO.OooO0O0;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.view.MotionEventCompat;
import com.loostone.libcore.jni.JniCall;
import com.lzy.okgo.model.Progress;
import java.util.Locale;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.LazyThreadSafetyMode;
import kotlin.UByte;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class OooO0OO implements OooO00o.OooO0O0.OooO0OO.OooO0O0.OooO00o {
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public static final C0005OooO0OO f242OooO00o = new C0005OooO0OO();
    @NotNull
    public static final Lazy<OooO0OO> OooO0O0 = LazyKt.lazy(LazyThreadSafetyMode.SYNCHRONIZED, (Function0) OooO0O0.f244OooO00o);
    public boolean OooO0OO;
    @Nullable
    public UsbInterface OooO0Oo;
    @Nullable
    public OooO00o.OooO0O0.OooO0OO.OooO0O0.OooO00o OooO0o0;

    /* loaded from: classes.dex */
    public enum OooO00o {
        SO(1),
        KO(2);
        
        public final int OooO0Oo;

        OooO00o(int i) {
            this.OooO0Oo = i;
        }
    }

    /* loaded from: classes.dex */
    public static final class OooO0O0 extends Lambda implements Function0<OooO0OO> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final OooO0O0 f244OooO00o = new OooO0O0();

        public OooO0O0() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public OooO0OO invoke() {
            return new OooO0OO();
        }
    }

    /* renamed from: OooO00o.OooO0O0.OooO0OO.OooO0O0.OooO0OO$OooO0OO  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static final class C0005OooO0OO {
        static {
            Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(C0005OooO0OO.class), "instance", "getInstance()Lcom/loostone/libcore/hid/HidProxy;"));
        }

        @NotNull
        public final OooO0OO OooO00o() {
            return OooO0OO.OooO0O0.getValue();
        }
    }

    /* loaded from: classes.dex */
    public enum OooO0o {
        K(1),
        C(2);
        
        public final int OooO0Oo;

        OooO0o(int i) {
            this.OooO0Oo = i;
        }
    }

    @Override // OooO00o.OooO0O0.OooO0OO.OooO0O0.OooO00o
    public boolean OooO00o(@Nullable UsbDevice usbDevice) {
        if (this.OooO0OO) {
            return true;
        }
        if (this.OooO0o0 == null) {
            UsbInterface usbInterface = this.OooO0Oo;
            Intrinsics.checkNotNull(usbInterface);
            this.OooO0o0 = new OooO00o.OooO0O0.OooO0OO.OooO0O0.OooO0O0(usbInterface);
        }
        OooO00o.OooO0O0.OooO0OO.OooO0O0.OooO00o oooO00o = this.OooO0o0;
        boolean OooO00o2 = oooO00o == null ? false : oooO00o.OooO00o(usbDevice);
        this.OooO0OO = OooO00o2;
        return OooO00o2;
    }

    @NotNull
    public final String OooO0O0(byte b, byte b2, byte b3) {
        OooO00o.OooO0O0.OooO0OO.OooO0O0.OooO00o oooO00o = this.OooO0o0;
        Intrinsics.checkNotNull(oooO00o);
        byte[] OooO00o2 = oooO00o.OooO00o(b, b2, b3);
        Intrinsics.checkNotNullExpressionValue(OooO00o2, "hidCommunication!!.getBytes(inputByte0, inputByte1, inputByte2)");
        String hexString = Integer.toHexString((((OooO00o2[1] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (OooO00o2[0] & UByte.MAX_VALUE)) & 65535);
        String stringPlus = Intrinsics.stringPlus("0000", hexString);
        int length = hexString.length();
        int length2 = hexString.length() + 4;
        if (stringPlus != null) {
            String substring = stringPlus.substring(length, length2);
            Intrinsics.checkNotNullExpressionValue(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
            return substring;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    public final boolean OooO0OO(@NotNull UsbDevice usbDevice) {
        Intrinsics.checkNotNullParameter(usbDevice, "usbDevice");
        if (this.OooO0Oo == null && !OooO0O0(usbDevice)) {
            Intrinsics.checkNotNullParameter("HidProxy", Progress.TAG);
            Intrinsics.checkNotNullParameter("start hid, checkInterface false", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 5) {
                Log.e("LogTuning", "HidProxy -> start hid, checkInterface false");
                return false;
            }
            return false;
        }
        return OooO00o(usbDevice);
    }

    public final boolean OooO0O0(UsbDevice usbDevice) {
        int interfaceCount = usbDevice.getInterfaceCount();
        if (interfaceCount > 0) {
            if (interfaceCount >= 0) {
                int i = 0;
                while (true) {
                    int i2 = i + 1;
                    if (usbDevice.getInterface(i).getInterfaceClass() == 3) {
                        this.OooO0Oo = usbDevice.getInterface(i);
                        return true;
                    } else if (i == interfaceCount) {
                        break;
                    } else {
                        i = i2;
                    }
                }
            }
            Intrinsics.checkNotNullParameter("HidProxy", Progress.TAG);
            Intrinsics.checkNotNullParameter("UsbInterface not found", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 5) {
                Log.e("LogTuning", "HidProxy -> UsbInterface not found");
            }
        } else {
            Intrinsics.checkNotNullParameter("HidProxy", Progress.TAG);
            Intrinsics.checkNotNullParameter("UsbInterface is null", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 5) {
                Log.e("LogTuning", "HidProxy -> UsbInterface is null");
            }
        }
        return false;
    }

    @Override // OooO00o.OooO0O0.OooO0OO.OooO0O0.OooO00o
    public void OooO00o() {
        this.OooO0OO = false;
        OooO00o.OooO0O0.OooO0OO.OooO0O0.OooO00o oooO00o = this.OooO0o0;
        if (oooO00o == null) {
            return;
        }
        oooO00o.OooO00o();
    }

    @Override // OooO00o.OooO0O0.OooO0OO.OooO0O0.OooO00o
    @NotNull
    public byte[] OooO00o(byte b, byte b2, byte b3) {
        OooO00o.OooO0O0.OooO0OO.OooO0O0.OooO00o oooO00o = this.OooO0o0;
        Intrinsics.checkNotNull(oooO00o);
        byte[] OooO00o2 = oooO00o.OooO00o(b, b2, b3);
        Intrinsics.checkNotNullExpressionValue(OooO00o2, "hidCommunication!!.getBytes(inputByte0, inputByte1, inputByte2)");
        return OooO00o2;
    }

    public final boolean OooO00o(@NotNull UsbDevice usbDevice, int i) {
        Intrinsics.checkNotNullParameter(usbDevice, "usbDevice");
        if (!OooO00o.OooO0O0.OooO0O0.OooO0OO.INTANCE.OooO00o().hasPermission(usbDevice)) {
            Intrinsics.checkNotNullParameter("HidProxy", Progress.TAG);
            Intrinsics.checkNotNullParameter("check no permission", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 5) {
                Log.e("LogTuning", "HidProxy -> check no permission");
            }
            return false;
        }
        if (OooO0O0(usbDevice)) {
            if (OooO00o(usbDevice)) {
                int i2 = 1;
                if (i == 1) {
                    OooO00o oooO00o = OooO00o.SO;
                } else {
                    OooO00o oooO00o2 = OooO00o.KO;
                    i2 = 2;
                }
                String OooO0O02 = OooO0O0((byte) -1, (byte) i2, (byte) 0);
                String msg = Intrinsics.stringPlus("check ------> ", OooO0O02);
                Intrinsics.checkNotNullParameter("HidProxy", Progress.TAG);
                Intrinsics.checkNotNullParameter(msg, "msg");
                if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                    Log.i("LogTuning", "HidProxy -> " + msg);
                }
                OooO00o();
                return Intrinsics.areEqual("0001", OooO0O02);
            }
            OooO00o();
            Intrinsics.checkNotNullParameter("HidProxy", Progress.TAG);
            Intrinsics.checkNotNullParameter("check ------> start hid failure", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 5) {
                Log.e("LogTuning", "HidProxy -> check ------> start hid failure");
            }
        } else {
            Intrinsics.checkNotNullParameter("HidProxy", Progress.TAG);
            Intrinsics.checkNotNullParameter("check ------> check interface failure", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 5) {
                Log.e("LogTuning", "HidProxy -> check ------> check interface failure");
            }
        }
        return false;
    }

    public final int OooO00o(@NotNull String vendorName, @NotNull String productName, @NotNull String serialNumber) {
        OooO0o oooO0o;
        Intrinsics.checkNotNullParameter(vendorName, "vendorName");
        Intrinsics.checkNotNullParameter(productName, "productName");
        Intrinsics.checkNotNullParameter(serialNumber, "serialNumber");
        int indexOf$default = StringsKt.indexOf$default((CharSequence) productName, "-", 0, false, 6, (Object) null);
        if (indexOf$default > 0) {
            String substring = productName.substring(indexOf$default + 1);
            Intrinsics.checkNotNullExpressionValue(substring, "(this as java.lang.String).substring(startIndex)");
            Locale ROOT = Locale.ROOT;
            Intrinsics.checkNotNullExpressionValue(ROOT, "ROOT");
            if (substring == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            String lowerCase = substring.toLowerCase(ROOT);
            Intrinsics.checkNotNullExpressionValue(lowerCase, "(this as java.lang.String).toLowerCase(locale)");
            if (StringsKt.contains$default((CharSequence) lowerCase, (CharSequence) "k", false, 2, (Object) null)) {
                oooO0o = OooO0o.K;
            } else {
                oooO0o = OooO0o.C;
            }
        } else {
            oooO0o = OooO0o.C;
        }
        return JniCall.f602OooO00o.setUsbDeviceParam(vendorName, productName, serialNumber, oooO0o.OooO0Oo);
    }
}
