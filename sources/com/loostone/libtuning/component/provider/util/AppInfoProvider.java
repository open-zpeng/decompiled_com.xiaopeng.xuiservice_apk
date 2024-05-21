package com.loostone.libtuning.component.provider.util;

import OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO0O0;
import OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO;
import OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO0O0;
import OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0o.OooO0OO;
import OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.o0O0o000;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.util.Log;
import com.loostone.libtuning.BuildConfig;
import com.lzy.okgo.model.Progress;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000e\n\u0002\b\n\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0015\u0010\u0016J\u0015\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002¢\u0006\u0004\b\u0005\u0010\u0006J\u001d\u0010\b\u001a\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002¢\u0006\u0004\b\b\u0010\tR\u0016\u0010\n\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\n\u0010\u000bR\u0016\u0010\f\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\f\u0010\u000bR\u0016\u0010\u000e\u001a\u00020\r8\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u000e\u0010\u000fR\u0016\u0010\u0010\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0010\u0010\u000bR\u0016\u0010\u0011\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0011\u0010\u000bR\u0016\u0010\u0012\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0012\u0010\u000bR\u0016\u0010\u0013\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0013\u0010\u000bR\u0016\u0010\u0014\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0014\u0010\u000b¨\u0006\u0017"}, d2 = {"Lcom/loostone/libtuning/component/provider/util/AppInfoProvider;", "", "", "keyIndex", "Landroid/database/Cursor;", "getValue", "(I)Landroid/database/Cursor;", "value", "setValue", "(II)I", "GET_ENVIRONMENTAL_SOUND", "I", "GET_LIB_TUNING_BUILD_TIME", "", "TAG", "Ljava/lang/String;", "GET_LIB_TUNING_VERSION", "GET_CHANNEL_ID", "GET_CONFIG_CONTENT", "GET_SO_VERSION", "GET_MODEL_ID", "<init>", "()V", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public final class AppInfoProvider {
    private static final int GET_CHANNEL_ID = 106;
    private static final int GET_CONFIG_CONTENT = 110;
    private static final int GET_ENVIRONMENTAL_SOUND = 112;
    private static final int GET_LIB_TUNING_BUILD_TIME = 102;
    private static final int GET_LIB_TUNING_VERSION = 100;
    private static final int GET_MODEL_ID = 108;
    private static final int GET_SO_VERSION = 104;
    @NotNull
    public static final AppInfoProvider INSTANCE = new AppInfoProvider();
    @NotNull
    private static final String TAG = "AppInfoProvider";

    private AppInfoProvider() {
    }

    @NotNull
    public final Cursor getValue(int i) {
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"key", "value"});
        MatrixCursor.RowBuilder newRow = matrixCursor.newRow();
        String str = "";
        if (i == 100) {
            str = BuildConfig.VERSION_NAME;
        } else if (i == 102) {
            str = BuildConfig.BUILD_TIME;
        } else if (i != 104) {
            if (i != 106) {
                if (i != 108) {
                    if (i != 110) {
                        if (i != 112) {
                            str = "null";
                        } else {
                            OooO0OO oooO0OO = OooO0OO.f553OooO00o;
                            OooO oooO = OooO.f279OooO00o;
                            Long OooO00o2 = oooO.OooO0O0().OooO00o();
                            Intrinsics.checkNotNull(OooO00o2);
                            long longValue = OooO00o2.longValue();
                            Integer OooO00o3 = oooO.OooO0oO().OooO00o();
                            Intrinsics.checkNotNull(OooO00o3);
                            str = oooO0OO.OooO00o(longValue, OooO00o3.intValue());
                        }
                    } else {
                        OooO0O0 oooO0O0 = OooO0O0.f311OooO00o;
                        OooO oooO2 = OooO.f279OooO00o;
                        Long OooO00o4 = oooO2.OooO0O0().OooO00o();
                        Intrinsics.checkNotNull(OooO00o4);
                        long longValue2 = OooO00o4.longValue();
                        Integer OooO00o5 = oooO2.OooO0oO().OooO00o();
                        Intrinsics.checkNotNull(OooO00o5);
                        str = oooO0O0.OooO00o(longValue2, OooO00o5.intValue());
                    }
                } else {
                    str = String.valueOf(OooO.f279OooO00o.OooO0oO().OooO00o());
                }
            } else {
                str = String.valueOf(OooO.f279OooO00o.OooO0O0().OooO00o());
            }
        } else {
            o0O0o000 o0o0o000 = o0O0o000.f584OooO00o;
            OooO0O0.OooO0o version = OooO0O0.OooO0o.DEFAULT;
            Intrinsics.checkNotNullParameter(version, "version");
            String format = String.format("%03d", Arrays.copyOf(new Object[]{60}, 1));
            Intrinsics.checkNotNullExpressionValue(format, "java.lang.String.format(format, *args)");
            String format2 = String.format("%03d", Arrays.copyOf(new Object[]{1}, 1));
            Intrinsics.checkNotNullExpressionValue(format2, "java.lang.String.format(format, *args)");
            String format3 = String.format("%02d", Arrays.copyOf(new Object[]{0}, 1));
            Intrinsics.checkNotNullExpressionValue(format3, "java.lang.String.format(format, *args)");
            OooO0O0.OooO0OO sign = OooO0O0.OooO0OO.READ;
            Intrinsics.checkNotNullParameter(sign, "sign");
            OooO0O0.EnumC0003OooO0O0 type = OooO0O0.EnumC0003OooO0O0.NONE;
            Intrinsics.checkNotNullParameter(type, "type");
            OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO0O0 oooO0O02 = new OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO0O0();
            oooO0O02.f230OooO00o = format;
            oooO0O02.OooO0O0 = format2;
            oooO0O02.OooO0OO = format3;
            oooO0O02.OooO0Oo = 1;
            oooO0O02.OooO0o0 = 0;
            oooO0O02.OooO0o = "000";
            oooO0O02.OooO0oO = "";
            String OooO00o6 = o0o0o000.OooO00o().OooO00o(oooO0O02);
            if (OooO00o6.length() < 12) {
                str = "Format Error";
            } else {
                try {
                    StringBuilder sb = new StringBuilder();
                    String substring = OooO00o6.substring(0, 3);
                    Intrinsics.checkNotNullExpressionValue(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    sb.append(Integer.parseInt(substring));
                    sb.append('.');
                    String substring2 = OooO00o6.substring(3, 6);
                    Intrinsics.checkNotNullExpressionValue(substring2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    sb.append(Integer.parseInt(substring2));
                    sb.append('.');
                    String substring3 = OooO00o6.substring(6, 9);
                    Intrinsics.checkNotNullExpressionValue(substring3, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    sb.append(Integer.parseInt(substring3));
                    sb.append('.');
                    String substring4 = OooO00o6.substring(9, 12);
                    Intrinsics.checkNotNullExpressionValue(substring4, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    sb.append(Integer.parseInt(substring4));
                    sb.append('_');
                    String substring5 = OooO00o6.substring(12);
                    Intrinsics.checkNotNullExpressionValue(substring5, "(this as java.lang.String).substring(startIndex)");
                    sb.append(substring5);
                    str = sb.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        newRow.add("key", Integer.valueOf(i));
        newRow.add("value", str);
        String msg = "get  keyIndex: " + i + " value: " + str;
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> " + msg);
        }
        return matrixCursor;
    }

    public final int setValue(int i, int i2) {
        String msg = "set  keyIndex: " + i + " value: " + i2;
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> " + msg);
            return 1;
        }
        return 1;
    }
}
