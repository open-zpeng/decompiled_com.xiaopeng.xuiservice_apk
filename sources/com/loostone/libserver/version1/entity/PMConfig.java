package com.loostone.libserver.version1.entity;
/* loaded from: classes4.dex */
public class PMConfig {
    public static String PRIVATE_KEY = "ASDFGHJKL";
    public static String PUBLIC_KEY = "ZXCVBNM";
    public static String a = "0123456789";
    public static String b = "12345678";
    public static String c = "1234567890987654321234567";
    public static int d = 161732679;
    public static int e = 1516846215;

    public static String getIv() {
        return a;
    }

    public static String getKey() {
        return b;
    }

    public static int getMixerMask() {
        return d;
    }

    public static int getPowerMask() {
        return e;
    }

    public static String getToken() {
        return c;
    }
}
