package com.ta.utdid2.b.a;
/* compiled from: StringUtils.java */
/* loaded from: classes4.dex */
public class i {
    /* renamed from: a  reason: collision with other method in class */
    public static boolean m102a(String str) {
        if (str != null && str.length() > 0) {
            return false;
        }
        return true;
    }

    public static int a(String str) {
        if (str.length() > 0) {
            int i = 0;
            for (char c : str.toCharArray()) {
                i = (i * 31) + c;
            }
            return i;
        }
        return 0;
    }
}
