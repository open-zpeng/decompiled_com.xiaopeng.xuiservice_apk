package OooO00o.OooO0O0.OooO0O0.OooO0o0;

import OooO00o.OooO00o.OooO00o.OooO00o.OooO0O0;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import java.util.Locale;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class OooO00o {
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public static final OooO00o f237OooO00o = new OooO00o();
    @NotNull
    public static final Object OooO0O0 = new Object();

    public final int OooO00o(int i, int i2, int i3, int i4, int i5) {
        int i6 = ((int) ((((i - i2) * 1.0f) * (i5 - i4)) / (i3 - i2))) + i4;
        return i6 > i5 ? i5 : i6 < i4 ? i4 : i6;
    }

    public final boolean OooO00o(@NotNull UsbDevice usbDevice) {
        Intrinsics.checkNotNullParameter(usbDevice, "usbDevice");
        String manufacturerName = usbDevice.getManufacturerName();
        String productName = usbDevice.getProductName();
        if (manufacturerName == null || productName == null) {
            return false;
        }
        Locale locale = Locale.getDefault();
        Intrinsics.checkNotNullExpressionValue(locale, "getDefault()");
        String lowerCase = manufacturerName.toLowerCase(locale);
        Intrinsics.checkNotNullExpressionValue(lowerCase, "(this as java.lang.String).toLowerCase(locale)");
        String OooO0o = OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO0o(lowerCase);
        Locale locale2 = Locale.getDefault();
        Intrinsics.checkNotNullExpressionValue(locale2, "getDefault()");
        String lowerCase2 = productName.toLowerCase(locale2);
        Intrinsics.checkNotNullExpressionValue(lowerCase2, "(this as java.lang.String).toLowerCase(locale)");
        String productLowerCase = OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO0o(lowerCase2);
        if (Intrinsics.areEqual(OooO0o, "www.loostone.com")) {
            Intrinsics.checkNotNullExpressionValue(productLowerCase, "productLowerCase");
            return StringsKt.contains$default((CharSequence) productLowerCase, (CharSequence) "puremic", false, 2, (Object) null);
        }
        return false;
    }

    @NotNull
    public final String OooO0O0(@NotNull String fileName) {
        Intrinsics.checkNotNullParameter(fileName, "fileName");
        Context context = OooO00o.OooO0O0.OooO0O0.OooO00o.OooO0O0;
        if (context != null) {
            String path = context.getPackageCodePath();
            Intrinsics.checkNotNullExpressionValue(path, "path");
            String substring = path.substring(0, StringsKt.lastIndexOf$default((CharSequence) path, "/", 0, false, 6, (Object) null) + 1);
            Intrinsics.checkNotNullExpressionValue(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
            return Intrinsics.stringPlus(substring, fileName);
        }
        Intrinsics.throwUninitializedPropertyAccessException("instance");
        throw null;
    }

    @NotNull
    public final String OooO00o(@NotNull Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            Intrinsics.checkNotNullExpressionValue(applicationInfo, "context.packageManager\n                .getApplicationInfo(\n                    context.packageName,\n                    PackageManager.GET_META_DATA\n                )");
            Bundle bundle = applicationInfo.metaData;
            if (bundle == null) {
                return "";
            }
            String string = bundle.getString("APP_CHANNEL");
            return string == null ? "" : string;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @NotNull
    public final String OooO00o(@NotNull String path) {
        Intrinsics.checkNotNullParameter(path, "path");
        OooO0O0.OooO00o OooO00o2 = OooO00o.OooO00o.OooO00o.OooO00o.OooO0O0.OooO00o(Intrinsics.stringPlus("md5sum ", path), false, true);
        Intrinsics.checkNotNullExpressionValue(OooO00o2, "execCmd(\n                \"md5sum $path\",\n                false, true\n            )");
        if (OooO00o2.f213OooO00o != 0 || Intrinsics.areEqual(OooO00o2.OooO0O0, "")) {
            return "";
        }
        String str = OooO00o2.OooO0O0;
        Intrinsics.checkNotNullExpressionValue(str, "{\n            value.successMsg\n        }");
        return str;
    }
}
