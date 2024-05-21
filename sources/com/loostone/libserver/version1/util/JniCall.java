package com.loostone.libserver.version1.util;

import org.jetbrains.annotations.NotNull;
/* loaded from: classes4.dex */
public final class JniCall {
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public static final JniCall f603OooO00o = new JniCall();

    static {
        System.loadLibrary("PureNet");
    }

    public final native void initNetConfig();
}
