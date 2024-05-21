package com.xiaopeng.xuiservice.smart.condition;

import android.util.Log;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes5.dex */
public class Conditions {
    private static final boolean LOG_ENABLE = true;
    public static final String TAG = "egg";

    public static void INFO(String msg) {
        Log.i("egg", msg);
    }

    public static void ERROR(String msg, Throwable t) {
        Log.e("egg", msg, t);
    }

    public static void ERROR(String msg) {
        Log.e("egg", msg);
    }

    public static Group AND(Condition... c) {
        return new AND(Arrays.asList(c));
    }

    public static Group AND(List<Condition> list) {
        return new AND(list);
    }

    public static Group OR(Condition... c) {
        return new OR(Arrays.asList(c));
    }

    public static Group OR(List<Condition> list) {
        return new OR(list);
    }

    public static boolean superEquals(Object a, Object b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null || !a.getClass().equals(b.getClass())) {
            return false;
        }
        if (a.getClass().isArray()) {
            Class componentType = a.getClass().getComponentType();
            if (componentType.isPrimitive()) {
                if (Integer.TYPE.equals(componentType)) {
                    return Arrays.equals((int[]) a, (int[]) b);
                }
                if (Long.TYPE.equals(componentType)) {
                    return Arrays.equals((long[]) a, (long[]) b);
                }
                if (Double.TYPE.equals(componentType)) {
                    return Arrays.equals((double[]) a, (double[]) b);
                }
                if (Float.TYPE.equals(componentType)) {
                    return Arrays.equals((float[]) a, (float[]) b);
                }
                if (Short.TYPE.equals(componentType)) {
                    return Arrays.equals((short[]) a, (short[]) b);
                }
                if (Byte.TYPE.equals(componentType)) {
                    return Arrays.equals((byte[]) a, (byte[]) b);
                }
                if (Boolean.TYPE.equals(componentType)) {
                    return Arrays.equals((boolean[]) a, (boolean[]) b);
                }
                if (Character.TYPE.equals(componentType)) {
                    return Arrays.equals((char[]) a, (char[]) b);
                }
            } else {
                Object[] aa = (Object[]) a;
                Object[] bb = (Object[]) b;
                if (aa.length != bb.length) {
                    return false;
                }
                for (int i = 0; i < aa.length; i++) {
                    if (!superEquals(aa[i], bb[i])) {
                        return false;
                    }
                }
                return true;
            }
        }
        return a.equals(b);
    }

    public static <T> String valueToString(T value) {
        if (value == null) {
            return null;
        }
        if (value.getClass().isArray()) {
            Class<?> componentType = value.getClass().getComponentType();
            if (componentType != null && componentType.isPrimitive()) {
                if (Integer.TYPE.equals(componentType)) {
                    return Arrays.toString((int[]) value);
                }
                if (Float.TYPE.equals(componentType)) {
                    return Arrays.toString((float[]) value);
                }
                if (Double.TYPE.equals(componentType)) {
                    return Arrays.toString((double[]) value);
                }
                if (Long.TYPE.equals(componentType)) {
                    return Arrays.toString((long[]) value);
                }
                if (Short.TYPE.equals(componentType)) {
                    return Arrays.toString((short[]) value);
                }
                if (Boolean.TYPE.equals(componentType)) {
                    return Arrays.toString((boolean[]) value);
                }
                if (Byte.TYPE.equals(componentType)) {
                    return Arrays.toString((byte[]) value);
                }
                if (Character.TYPE.equals(componentType)) {
                    return Arrays.toString((char[]) value);
                }
            } else {
                return Arrays.toString((Object[]) value);
            }
        }
        return value.toString();
    }
}
