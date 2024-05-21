package com.loostone.libcore.jni;

import org.jetbrains.annotations.NotNull;
/* loaded from: classes4.dex */
public final class JniCall {
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public static final JniCall f602OooO00o = new JniCall();

    static {
        System.loadLibrary("UsbDongle");
    }

    public final native int exec(int i, int i2);

    public final native int setUsbDeviceParam(@NotNull String str, @NotNull String str2, @NotNull String str3, int i);
}
