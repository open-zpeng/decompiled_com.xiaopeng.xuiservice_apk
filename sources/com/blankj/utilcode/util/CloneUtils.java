package com.blankj.utilcode.util;

import java.lang.reflect.Type;
/* loaded from: classes4.dex */
public final class CloneUtils {
    private CloneUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static <T> T deepClone(T data, Type type) {
        try {
            return (T) UtilsBridge.fromJson(UtilsBridge.toJson(data), type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
