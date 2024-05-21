package com.loostone.libbottom.util;

import OooO00o.OooO0O0.OooO0O0.OooO0O0;
import android.util.Log;
import com.lzy.okgo.model.Progress;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes4.dex */
public final class JniUtil {
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public static final JniUtil f600OooO00o;

    static {
        JniUtil jniUtil = new JniUtil();
        f600OooO00o = jniUtil;
        System.loadLibrary("UsbMic");
        String msg = Intrinsics.stringPlus("native init ", Integer.valueOf(jniUtil.nativeInit()));
        Intrinsics.checkNotNullParameter("JniUtil", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "JniUtil -> " + msg);
        }
    }

    @NotNull
    public final native String decryption(@NotNull String str, int i);

    @NotNull
    public final native String encryption(@NotNull String str, int i);

    public final native int nativeInit();

    public final native int recordCreate(int i, int i2, int i3);

    public final native int recordRead(@NotNull byte[] bArr, int i, int i2);

    public final native int recordRead2Cache();

    public final native int recordRelease();

    public final native int resample(@NotNull byte[] bArr, @NotNull byte[] bArr2, int i, int i2, int i3, int i4);

    public final native int resampleInit();

    public final native int resampleRelease();
}
