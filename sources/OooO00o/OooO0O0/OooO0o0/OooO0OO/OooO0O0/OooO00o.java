package OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.lzy.okgo.model.Progress;
import java.util.Arrays;
import java.util.Locale;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class OooO00o {
    @Nullable
    public String OooO;
    @Nullable

    /* renamed from: OooO00o  reason: collision with root package name */
    public final UsbDevice f228OooO00o;
    @Nullable
    public String OooO0O0;
    @Nullable
    public String OooO0OO;
    @Nullable
    public String OooO0Oo;
    @Nullable
    public String OooO0o;
    @Nullable
    public String OooO0o0;
    @Nullable
    public String OooO0oO;
    @Nullable
    public String OooO0oo;
    @Nullable
    public String OooOO0;
    public int OooOO0O;
    public boolean OooOO0o;
    @NotNull
    public EnumC0002OooO00o OooOOO0;

    /* renamed from: OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO00o$OooO00o  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public enum EnumC0002OooO00o {
        M,
        U,
        Q,
        X,
        S,
        NONE
    }

    public OooO00o(@NotNull Context mContext, @Nullable UsbDevice usbDevice) {
        Intrinsics.checkNotNullParameter(mContext, "mContext");
        this.f228OooO00o = usbDevice;
        this.OooO0Oo = "";
        this.OooO0o0 = "";
        this.OooOOO0 = EnumC0002OooO00o.NONE;
        OooO00o();
        OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o oooO00o = OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO00o.f237OooO00o;
        Intrinsics.checkNotNull(usbDevice);
        this.OooOO0o = oooO00o.OooO00o(usbDevice);
    }

    public final void OooO00o() {
        EnumC0002OooO00o enumC0002OooO00o;
        UsbDevice usbDevice = this.f228OooO00o;
        if (usbDevice == null) {
            return;
        }
        String productName = usbDevice.getProductName();
        if (productName != null && productName.length() >= 17) {
            Locale locale = Locale.getDefault();
            Intrinsics.checkNotNullExpressionValue(locale, "getDefault()");
            String upperCase = productName.toUpperCase(locale);
            Intrinsics.checkNotNullExpressionValue(upperCase, "(this as java.lang.String).toUpperCase(locale)");
            char charAt = upperCase.charAt(17);
            if (charAt == 'U') {
                enumC0002OooO00o = EnumC0002OooO00o.U;
            } else if (charAt == 'Q') {
                enumC0002OooO00o = EnumC0002OooO00o.Q;
            } else if (charAt == 'X') {
                enumC0002OooO00o = EnumC0002OooO00o.X;
            } else if (charAt == 'M') {
                enumC0002OooO00o = EnumC0002OooO00o.M;
            } else if (charAt == 'S') {
                enumC0002OooO00o = EnumC0002OooO00o.S;
            } else {
                enumC0002OooO00o = EnumC0002OooO00o.NONE;
            }
        } else {
            enumC0002OooO00o = EnumC0002OooO00o.NONE;
        }
        Intrinsics.checkNotNullParameter(enumC0002OooO00o, "<set-?>");
        this.OooOOO0 = enumC0002OooO00o;
        this.OooO0O0 = String.valueOf(this.f228OooO00o.getVendorId());
        this.OooO0OO = String.valueOf(this.f228OooO00o.getProductId());
        String format = String.format("%04x", Arrays.copyOf(new Object[]{Integer.valueOf(this.f228OooO00o.getVendorId())}, 1));
        Intrinsics.checkNotNullExpressionValue(format, "java.lang.String.format(format, *args)");
        Locale locale2 = Locale.getDefault();
        Intrinsics.checkNotNullExpressionValue(locale2, "getDefault()");
        if (format != null) {
            String lowerCase = format.toLowerCase(locale2);
            Intrinsics.checkNotNullExpressionValue(lowerCase, "(this as java.lang.String).toLowerCase(locale)");
            this.OooO0Oo = lowerCase;
            String format2 = String.format("%04x", Arrays.copyOf(new Object[]{Integer.valueOf(this.f228OooO00o.getProductId())}, 1));
            Intrinsics.checkNotNullExpressionValue(format2, "java.lang.String.format(format, *args)");
            Locale locale3 = Locale.getDefault();
            Intrinsics.checkNotNullExpressionValue(locale3, "getDefault()");
            if (format2 != null) {
                String lowerCase2 = format2.toLowerCase(locale3);
                Intrinsics.checkNotNullExpressionValue(lowerCase2, "(this as java.lang.String).toLowerCase(locale)");
                this.OooO0o0 = lowerCase2;
                try {
                    this.OooO0oo = OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO0o(this.f228OooO00o.getSerialNumber());
                    this.OooO0o = OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO0o(this.f228OooO00o.getManufacturerName());
                    this.OooO0oO = OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO0o(this.f228OooO00o.getProductName());
                    return;
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Intrinsics.checkNotNullParameter("CachedDongleDevice", Progress.TAG);
                    Intrinsics.checkNotNullParameter(" 拿不到设备信息", NotificationCompat.CATEGORY_MESSAGE);
                    if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 5) {
                        Log.e("LogTuning", "CachedDongleDevice ->  拿不到设备信息");
                        return;
                    }
                    return;
                }
            }
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    public boolean equals(@Nullable Object obj) {
        String str;
        String str2;
        String str3;
        String str4;
        if (obj == null || !(obj instanceof OooO00o)) {
            return false;
        }
        OooO00o oooO00o = (OooO00o) obj;
        String str5 = this.OooO0O0;
        return str5 != null && Intrinsics.areEqual(str5, oooO00o.OooO0O0) && (str = this.OooO0OO) != null && Intrinsics.areEqual(str, oooO00o.OooO0OO) && (str2 = this.OooO0o) != null && Intrinsics.areEqual(str2, oooO00o.OooO0o) && (str3 = this.OooO0oO) != null && Intrinsics.areEqual(str3, oooO00o.OooO0oO) && (str4 = this.OooO0oo) != null && Intrinsics.areEqual(str4, oooO00o.OooO0oo);
    }

    public int hashCode() {
        StringBuilder sb = new StringBuilder();
        sb.append((Object) this.OooO0O0);
        sb.append((Object) this.OooO0OO);
        sb.append((Object) this.OooO0o);
        sb.append((Object) this.OooO0oO);
        sb.append((Object) this.OooO0oo);
        return sb.toString().hashCode();
    }
}
