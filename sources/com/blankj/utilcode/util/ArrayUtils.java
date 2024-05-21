package com.blankj.utilcode.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
/* loaded from: classes4.dex */
public class ArrayUtils {
    public static final int INDEX_NOT_FOUND = -1;

    /* loaded from: classes4.dex */
    public interface Closure<E> {
        void execute(int i, E e);
    }

    private ArrayUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    @NonNull
    public static <T> T[] newArray(T... array) {
        if (array != null) {
            return array;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.newArray() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static long[] newLongArray(long... array) {
        if (array != null) {
            return array;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.newLongArray() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static int[] newIntArray(int... array) {
        if (array != null) {
            return array;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.newIntArray() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static short[] newShortArray(short... array) {
        if (array != null) {
            return array;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.newShortArray() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static char[] newCharArray(char... array) {
        if (array != null) {
            return array;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.newCharArray() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static byte[] newByteArray(byte... array) {
        if (array != null) {
            return array;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.newByteArray() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static double[] newDoubleArray(double... array) {
        if (array != null) {
            return array;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.newDoubleArray() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static float[] newFloatArray(float... array) {
        if (array != null) {
            return array;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.newFloatArray() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static boolean[] newBooleanArray(boolean... array) {
        if (array != null) {
            return array;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.newBooleanArray() marked by @androidx.annotation.NonNull");
    }

    public static boolean isEmpty(@Nullable Object array) {
        return getLength(array) == 0;
    }

    public static int getLength(@Nullable Object array) {
        if (array == null) {
            return 0;
        }
        return Array.getLength(array);
    }

    public static boolean isSameLength(@Nullable Object array1, @Nullable Object array2) {
        return getLength(array1) == getLength(array2);
    }

    @Nullable
    public static Object get(@Nullable Object array, int index) {
        return get(array, index, null);
    }

    @Nullable
    public static Object get(@Nullable Object array, int index, @Nullable Object defaultValue) {
        if (array == null) {
            return defaultValue;
        }
        try {
            return Array.get(array, index);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static void set(@Nullable Object array, int index, @Nullable Object value) {
        if (array == null) {
            return;
        }
        Array.set(array, index, value);
    }

    public static boolean equals(@Nullable Object[] a, @Nullable Object[] a2) {
        return Arrays.deepEquals(a, a2);
    }

    public static boolean equals(boolean[] a, boolean[] a2) {
        return Arrays.equals(a, a2);
    }

    public static boolean equals(byte[] a, byte[] a2) {
        return Arrays.equals(a, a2);
    }

    public static boolean equals(char[] a, char[] a2) {
        return Arrays.equals(a, a2);
    }

    public static boolean equals(double[] a, double[] a2) {
        return Arrays.equals(a, a2);
    }

    public static boolean equals(float[] a, float[] a2) {
        return Arrays.equals(a, a2);
    }

    public static boolean equals(int[] a, int[] a2) {
        return Arrays.equals(a, a2);
    }

    public static boolean equals(short[] a, short[] a2) {
        return Arrays.equals(a, a2);
    }

    public static <T> void reverse(T[] array) {
        if (array == null) {
            return;
        }
        int j = array.length - 1;
        for (int i = 0; j > i; i++) {
            T tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
        }
    }

    public static void reverse(long[] array) {
        if (array == null) {
            return;
        }
        int j = array.length - 1;
        for (int i = 0; j > i; i++) {
            long tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
        }
    }

    public static void reverse(int[] array) {
        if (array == null) {
            return;
        }
        int j = array.length - 1;
        for (int i = 0; j > i; i++) {
            int tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
        }
    }

    public static void reverse(short[] array) {
        if (array == null) {
            return;
        }
        int j = array.length - 1;
        for (int i = 0; j > i; i++) {
            short tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
        }
    }

    public static void reverse(char[] array) {
        if (array == null) {
            return;
        }
        int j = array.length - 1;
        for (int i = 0; j > i; i++) {
            char tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
        }
    }

    public static void reverse(byte[] array) {
        if (array == null) {
            return;
        }
        int j = array.length - 1;
        for (int i = 0; j > i; i++) {
            byte tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
        }
    }

    public static void reverse(double[] array) {
        if (array == null) {
            return;
        }
        int j = array.length - 1;
        for (int i = 0; j > i; i++) {
            double tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
        }
    }

    public static void reverse(float[] array) {
        if (array == null) {
            return;
        }
        int j = array.length - 1;
        for (int i = 0; j > i; i++) {
            float tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
        }
    }

    public static void reverse(boolean[] array) {
        if (array == null) {
            return;
        }
        int j = array.length - 1;
        for (int i = 0; j > i; i++) {
            boolean tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
        }
    }

    @Nullable
    public static <T> T[] copy(@Nullable T[] array) {
        if (array == null) {
            return null;
        }
        return (T[]) subArray(array, 0, array.length);
    }

    @Nullable
    public static long[] copy(@Nullable long[] array) {
        if (array == null) {
            return null;
        }
        return subArray(array, 0, array.length);
    }

    @Nullable
    public static int[] copy(@Nullable int[] array) {
        if (array == null) {
            return null;
        }
        return subArray(array, 0, array.length);
    }

    @Nullable
    public static short[] copy(@Nullable short[] array) {
        if (array == null) {
            return null;
        }
        return subArray(array, 0, array.length);
    }

    @Nullable
    public static char[] copy(@Nullable char[] array) {
        if (array == null) {
            return null;
        }
        return subArray(array, 0, array.length);
    }

    @Nullable
    public static byte[] copy(@Nullable byte[] array) {
        if (array == null) {
            return null;
        }
        return subArray(array, 0, array.length);
    }

    @Nullable
    public static double[] copy(@Nullable double[] array) {
        if (array == null) {
            return null;
        }
        return subArray(array, 0, array.length);
    }

    @Nullable
    public static float[] copy(@Nullable float[] array) {
        if (array == null) {
            return null;
        }
        return subArray(array, 0, array.length);
    }

    @Nullable
    public static boolean[] copy(@Nullable boolean[] array) {
        if (array == null) {
            return null;
        }
        return subArray(array, 0, array.length);
    }

    @Nullable
    private static Object realCopy(@Nullable Object array) {
        if (array == null) {
            return null;
        }
        return realSubArray(array, 0, getLength(array));
    }

    @Nullable
    public static <T> T[] subArray(@Nullable T[] array, int startIndexInclusive, int endIndexExclusive) {
        return (T[]) ((Object[]) realSubArray(array, startIndexInclusive, endIndexExclusive));
    }

    @Nullable
    public static long[] subArray(@Nullable long[] array, int startIndexInclusive, int endIndexExclusive) {
        return (long[]) realSubArray(array, startIndexInclusive, endIndexExclusive);
    }

    @Nullable
    public static int[] subArray(@Nullable int[] array, int startIndexInclusive, int endIndexExclusive) {
        return (int[]) realSubArray(array, startIndexInclusive, endIndexExclusive);
    }

    @Nullable
    public static short[] subArray(@Nullable short[] array, int startIndexInclusive, int endIndexExclusive) {
        return (short[]) realSubArray(array, startIndexInclusive, endIndexExclusive);
    }

    @Nullable
    public static char[] subArray(@Nullable char[] array, int startIndexInclusive, int endIndexExclusive) {
        return (char[]) realSubArray(array, startIndexInclusive, endIndexExclusive);
    }

    @Nullable
    public static byte[] subArray(@Nullable byte[] array, int startIndexInclusive, int endIndexExclusive) {
        return (byte[]) realSubArray(array, startIndexInclusive, endIndexExclusive);
    }

    @Nullable
    public static double[] subArray(@Nullable double[] array, int startIndexInclusive, int endIndexExclusive) {
        return (double[]) realSubArray(array, startIndexInclusive, endIndexExclusive);
    }

    @Nullable
    public static float[] subArray(@Nullable float[] array, int startIndexInclusive, int endIndexExclusive) {
        return (float[]) realSubArray(array, startIndexInclusive, endIndexExclusive);
    }

    @Nullable
    public static boolean[] subArray(@Nullable boolean[] array, int startIndexInclusive, int endIndexExclusive) {
        return (boolean[]) realSubArray(array, startIndexInclusive, endIndexExclusive);
    }

    @Nullable
    private static Object realSubArray(@Nullable Object array, int startIndexInclusive, int endIndexExclusive) {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        int length = getLength(array);
        if (endIndexExclusive > length) {
            endIndexExclusive = length;
        }
        int newSize = endIndexExclusive - startIndexInclusive;
        Class type = array.getClass().getComponentType();
        if (newSize <= 0) {
            return Array.newInstance(type, 0);
        }
        Object subArray = Array.newInstance(type, newSize);
        System.arraycopy(array, startIndexInclusive, subArray, 0, newSize);
        return subArray;
    }

    @NonNull
    public static <T> T[] add(@Nullable T[] array, @Nullable T element) {
        Class type = array != null ? array.getClass() : element != null ? element.getClass() : Object.class;
        T[] tmpTrauteVar2 = (T[]) ((Object[]) realAddOne(array, element, type));
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.add() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static boolean[] add(@Nullable boolean[] array, boolean element) {
        boolean[] tmpTrauteVar2 = (boolean[]) realAddOne(array, Boolean.valueOf(element), Boolean.TYPE);
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.add() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static byte[] add(@Nullable byte[] array, byte element) {
        byte[] tmpTrauteVar2 = (byte[]) realAddOne(array, Byte.valueOf(element), Byte.TYPE);
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.add() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static char[] add(@Nullable char[] array, char element) {
        char[] tmpTrauteVar2 = (char[]) realAddOne(array, Character.valueOf(element), Character.TYPE);
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.add() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static double[] add(@Nullable double[] array, double element) {
        double[] tmpTrauteVar2 = (double[]) realAddOne(array, Double.valueOf(element), Double.TYPE);
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.add() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static float[] add(@Nullable float[] array, float element) {
        float[] tmpTrauteVar2 = (float[]) realAddOne(array, Float.valueOf(element), Float.TYPE);
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.add() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static int[] add(@Nullable int[] array, int element) {
        int[] tmpTrauteVar2 = (int[]) realAddOne(array, Integer.valueOf(element), Integer.TYPE);
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.add() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static long[] add(@Nullable long[] array, long element) {
        long[] tmpTrauteVar2 = (long[]) realAddOne(array, Long.valueOf(element), Long.TYPE);
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.add() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static short[] add(@Nullable short[] array, short element) {
        short[] tmpTrauteVar2 = (short[]) realAddOne(array, Short.valueOf(element), Short.TYPE);
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.add() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    private static Object realAddOne(@Nullable Object array, @Nullable Object element, Class newArrayComponentType) {
        Object newArray;
        int arrayLength = 0;
        if (array != null) {
            arrayLength = getLength(array);
            newArray = Array.newInstance(array.getClass().getComponentType(), arrayLength + 1);
            System.arraycopy(array, 0, newArray, 0, arrayLength);
        } else {
            newArray = Array.newInstance(newArrayComponentType, 1);
        }
        Array.set(newArray, arrayLength, element);
        Object tmpTrauteVar2 = newArray;
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.realAddOne() marked by @androidx.annotation.NonNull");
    }

    @Nullable
    public static <T> T[] add(@Nullable T[] array1, @Nullable T[] array2) {
        return (T[]) ((Object[]) realAddArr(array1, array2));
    }

    @Nullable
    public static boolean[] add(@Nullable boolean[] array1, @Nullable boolean[] array2) {
        return (boolean[]) realAddArr(array1, array2);
    }

    @Nullable
    public static char[] add(@Nullable char[] array1, @Nullable char[] array2) {
        return (char[]) realAddArr(array1, array2);
    }

    @Nullable
    public static byte[] add(@Nullable byte[] array1, @Nullable byte[] array2) {
        return (byte[]) realAddArr(array1, array2);
    }

    @Nullable
    public static short[] add(@Nullable short[] array1, @Nullable short[] array2) {
        return (short[]) realAddArr(array1, array2);
    }

    @Nullable
    public static int[] add(@Nullable int[] array1, @Nullable int[] array2) {
        return (int[]) realAddArr(array1, array2);
    }

    @Nullable
    public static long[] add(@Nullable long[] array1, @Nullable long[] array2) {
        return (long[]) realAddArr(array1, array2);
    }

    @Nullable
    public static float[] add(@Nullable float[] array1, @Nullable float[] array2) {
        return (float[]) realAddArr(array1, array2);
    }

    @Nullable
    public static double[] add(@Nullable double[] array1, @Nullable double[] array2) {
        return (double[]) realAddArr(array1, array2);
    }

    private static Object realAddArr(@Nullable Object array1, @Nullable Object array2) {
        if (array1 == null && array2 == null) {
            return null;
        }
        if (array1 == null) {
            return realCopy(array2);
        }
        if (array2 == null) {
            return realCopy(array1);
        }
        int len1 = getLength(array1);
        int len2 = getLength(array2);
        Object joinedArray = Array.newInstance(array1.getClass().getComponentType(), len1 + len2);
        System.arraycopy(array1, 0, joinedArray, 0, len1);
        System.arraycopy(array2, 0, joinedArray, len1, len2);
        return joinedArray;
    }

    @Nullable
    public static <T> T[] add(@Nullable T[] array1, int index, @Nullable T[] array2) {
        Class clss;
        if (array1 != null) {
            clss = array1.getClass().getComponentType();
        } else if (array2 != null) {
            clss = array2.getClass().getComponentType();
        } else {
            return null;
        }
        return (T[]) ((Object[]) realAddArr(array1, index, array2, clss));
    }

    @Nullable
    public static boolean[] add(@Nullable boolean[] array1, int index, @Nullable boolean[] array2) {
        Object result = realAddArr(array1, index, array2, Boolean.TYPE);
        if (result == null) {
            return null;
        }
        return (boolean[]) result;
    }

    public static char[] add(@Nullable char[] array1, int index, @Nullable char[] array2) {
        Object result = realAddArr(array1, index, array2, Character.TYPE);
        if (result == null) {
            return null;
        }
        return (char[]) result;
    }

    @Nullable
    public static byte[] add(@Nullable byte[] array1, int index, @Nullable byte[] array2) {
        Object result = realAddArr(array1, index, array2, Byte.TYPE);
        if (result == null) {
            return null;
        }
        return (byte[]) result;
    }

    @Nullable
    public static short[] add(@Nullable short[] array1, int index, @Nullable short[] array2) {
        Object result = realAddArr(array1, index, array2, Short.TYPE);
        if (result == null) {
            return null;
        }
        return (short[]) result;
    }

    @Nullable
    public static int[] add(@Nullable int[] array1, int index, @Nullable int[] array2) {
        Object result = realAddArr(array1, index, array2, Integer.TYPE);
        if (result == null) {
            return null;
        }
        return (int[]) result;
    }

    @Nullable
    public static long[] add(@Nullable long[] array1, int index, @Nullable long[] array2) {
        Object result = realAddArr(array1, index, array2, Long.TYPE);
        if (result == null) {
            return null;
        }
        return (long[]) result;
    }

    @Nullable
    public static float[] add(@Nullable float[] array1, int index, @Nullable float[] array2) {
        Object result = realAddArr(array1, index, array2, Float.TYPE);
        if (result == null) {
            return null;
        }
        return (float[]) result;
    }

    @Nullable
    public static double[] add(@Nullable double[] array1, int index, @Nullable double[] array2) {
        Object result = realAddArr(array1, index, array2, Double.TYPE);
        if (result == null) {
            return null;
        }
        return (double[]) result;
    }

    @Nullable
    private static Object realAddArr(@Nullable Object array1, int index, @Nullable Object array2, Class clss) {
        if (array1 == null && array2 == null) {
            return null;
        }
        int len1 = getLength(array1);
        int len2 = getLength(array2);
        if (len1 == 0) {
            if (index != 0) {
                throw new IndexOutOfBoundsException("Index: " + index + ", array1 Length: 0");
            }
            return realCopy(array2);
        } else if (len2 == 0) {
            return realCopy(array1);
        } else {
            if (index > len1 || index < 0) {
                throw new IndexOutOfBoundsException("Index: " + index + ", array1 Length: " + len1);
            }
            Object joinedArray = Array.newInstance(array1.getClass().getComponentType(), len1 + len2);
            if (index == len1) {
                System.arraycopy(array1, 0, joinedArray, 0, len1);
                System.arraycopy(array2, 0, joinedArray, len1, len2);
            } else if (index == 0) {
                System.arraycopy(array2, 0, joinedArray, 0, len2);
                System.arraycopy(array1, 0, joinedArray, len2, len1);
            } else {
                System.arraycopy(array1, 0, joinedArray, 0, index);
                System.arraycopy(array2, 0, joinedArray, index, len2);
                System.arraycopy(array1, index, joinedArray, index + len2, len1 - index);
            }
            return joinedArray;
        }
    }

    @NonNull
    public static <T> T[] add(@Nullable T[] array, int index, @Nullable T element) {
        Class clss;
        if (array != null) {
            clss = array.getClass().getComponentType();
        } else if (element == null) {
            T[] tmpTrauteVar2 = (T[]) new Object[]{null};
            return tmpTrauteVar2;
        } else {
            clss = element.getClass();
        }
        T[] tmpTrauteVar3 = (T[]) ((Object[]) realAdd(array, index, element, clss));
        if (tmpTrauteVar3 != null) {
            return tmpTrauteVar3;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.add() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static boolean[] add(@Nullable boolean[] array, int index, boolean element) {
        boolean[] tmpTrauteVar2 = (boolean[]) realAdd(array, index, Boolean.valueOf(element), Boolean.TYPE);
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.add() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static char[] add(@Nullable char[] array, int index, char element) {
        char[] tmpTrauteVar2 = (char[]) realAdd(array, index, Character.valueOf(element), Character.TYPE);
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.add() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static byte[] add(@Nullable byte[] array, int index, byte element) {
        byte[] tmpTrauteVar2 = (byte[]) realAdd(array, index, Byte.valueOf(element), Byte.TYPE);
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.add() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static short[] add(@Nullable short[] array, int index, short element) {
        short[] tmpTrauteVar2 = (short[]) realAdd(array, index, Short.valueOf(element), Short.TYPE);
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.add() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static int[] add(@Nullable int[] array, int index, int element) {
        int[] tmpTrauteVar2 = (int[]) realAdd(array, index, Integer.valueOf(element), Integer.TYPE);
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.add() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static long[] add(@Nullable long[] array, int index, long element) {
        long[] tmpTrauteVar2 = (long[]) realAdd(array, index, Long.valueOf(element), Long.TYPE);
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.add() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static float[] add(@Nullable float[] array, int index, float element) {
        float[] tmpTrauteVar2 = (float[]) realAdd(array, index, Float.valueOf(element), Float.TYPE);
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.add() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static double[] add(@Nullable double[] array, int index, double element) {
        double[] tmpTrauteVar2 = (double[]) realAdd(array, index, Double.valueOf(element), Double.TYPE);
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.add() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    private static Object realAdd(@Nullable Object array, int index, @Nullable Object element, Class clss) {
        if (array == null) {
            if (index != 0) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Length: 0");
            }
            Object joinedArray = Array.newInstance(clss, 1);
            Array.set(joinedArray, 0, element);
            if (joinedArray == null) {
                throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.realAdd() marked by @androidx.annotation.NonNull");
            }
            return joinedArray;
        }
        int length = Array.getLength(array);
        if (index > length || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
        }
        Object result = Array.newInstance(clss, length + 1);
        System.arraycopy(array, 0, result, 0, index);
        Array.set(result, index, element);
        if (index < length) {
            System.arraycopy(array, index, result, index + 1, length - index);
        }
        if (result == null) {
            throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.realAdd() marked by @androidx.annotation.NonNull");
        }
        return result;
    }

    @Nullable
    public static Object[] remove(@Nullable Object[] array, int index) {
        if (array == null) {
            return null;
        }
        return (Object[]) remove((Object) array, index);
    }

    @Nullable
    public static Object[] removeElement(@Nullable Object[] array, @Nullable Object element) {
        int index = indexOf(array, element);
        if (index == -1) {
            return copy(array);
        }
        return remove(array, index);
    }

    @Nullable
    public static boolean[] remove(@Nullable boolean[] array, int index) {
        if (array == null) {
            return null;
        }
        return (boolean[]) remove((Object) array, index);
    }

    @Nullable
    public static boolean[] removeElement(@Nullable boolean[] array, boolean element) {
        int index = indexOf(array, element);
        if (index == -1) {
            return copy(array);
        }
        return remove(array, index);
    }

    @Nullable
    public static byte[] remove(@Nullable byte[] array, int index) {
        if (array == null) {
            return null;
        }
        return (byte[]) remove((Object) array, index);
    }

    @Nullable
    public static byte[] removeElement(@Nullable byte[] array, byte element) {
        int index = indexOf(array, element);
        if (index == -1) {
            return copy(array);
        }
        return remove(array, index);
    }

    @Nullable
    public static char[] remove(@Nullable char[] array, int index) {
        if (array == null) {
            return null;
        }
        return (char[]) remove((Object) array, index);
    }

    @Nullable
    public static char[] removeElement(@Nullable char[] array, char element) {
        int index = indexOf(array, element);
        if (index == -1) {
            return copy(array);
        }
        return remove(array, index);
    }

    @Nullable
    public static double[] remove(@Nullable double[] array, int index) {
        if (array == null) {
            return null;
        }
        return (double[]) remove((Object) array, index);
    }

    @Nullable
    public static double[] removeElement(@Nullable double[] array, double element) {
        int index = indexOf(array, element);
        if (index == -1) {
            return copy(array);
        }
        return remove(array, index);
    }

    @Nullable
    public static float[] remove(@Nullable float[] array, int index) {
        if (array == null) {
            return null;
        }
        return (float[]) remove((Object) array, index);
    }

    @Nullable
    public static float[] removeElement(@Nullable float[] array, float element) {
        int index = indexOf(array, element);
        if (index == -1) {
            return copy(array);
        }
        return remove(array, index);
    }

    @Nullable
    public static int[] remove(@Nullable int[] array, int index) {
        if (array == null) {
            return null;
        }
        return (int[]) remove((Object) array, index);
    }

    @Nullable
    public static int[] removeElement(@Nullable int[] array, int element) {
        int index = indexOf(array, element);
        if (index == -1) {
            return copy(array);
        }
        return remove(array, index);
    }

    @Nullable
    public static long[] remove(@Nullable long[] array, int index) {
        if (array == null) {
            return null;
        }
        return (long[]) remove((Object) array, index);
    }

    @Nullable
    public static long[] removeElement(@Nullable long[] array, long element) {
        int index = indexOf(array, element);
        if (index == -1) {
            return copy(array);
        }
        return remove(array, index);
    }

    @Nullable
    public static short[] remove(@Nullable short[] array, int index) {
        if (array == null) {
            return null;
        }
        return (short[]) remove((Object) array, index);
    }

    @Nullable
    public static short[] removeElement(@Nullable short[] array, short element) {
        int index = indexOf(array, element);
        if (index == -1) {
            return copy(array);
        }
        return remove(array, index);
    }

    @NonNull
    private static Object remove(@NonNull Object array, int index) {
        if (array == null) {
            throw new NullPointerException("Argument 'array' of type Object (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        int length = getLength(array);
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
        }
        Object result = Array.newInstance(array.getClass().getComponentType(), length - 1);
        System.arraycopy(array, 0, result, 0, index);
        if (index < length - 1) {
            System.arraycopy(array, index + 1, result, index, (length - index) - 1);
        }
        if (result != null) {
            return result;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.remove() marked by @androidx.annotation.NonNull");
    }

    public static int indexOf(@Nullable Object[] array, @Nullable Object objectToFind) {
        return indexOf(array, objectToFind, 0);
    }

    public static int indexOf(@Nullable Object[] array, @Nullable Object objectToFind, int startIndex) {
        if (array == null) {
            return -1;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        if (objectToFind == null) {
            for (int i = startIndex; i < array.length; i++) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i2 = startIndex; i2 < array.length; i2++) {
                if (objectToFind.equals(array[i2])) {
                    return i2;
                }
            }
        }
        return -1;
    }

    public static int lastIndexOf(@Nullable Object[] array, @Nullable Object objectToFind) {
        return lastIndexOf(array, objectToFind, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(@Nullable Object[] array, @Nullable Object objectToFind, int startIndex) {
        if (array == null || startIndex < 0) {
            return -1;
        }
        if (startIndex >= array.length) {
            startIndex = array.length - 1;
        }
        if (objectToFind == null) {
            for (int i = startIndex; i >= 0; i--) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i2 = startIndex; i2 >= 0; i2--) {
                if (objectToFind.equals(array[i2])) {
                    return i2;
                }
            }
        }
        return -1;
    }

    public static boolean contains(@Nullable Object[] array, @Nullable Object objectToFind) {
        return indexOf(array, objectToFind) != -1;
    }

    public static int indexOf(@Nullable long[] array, long valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    public static int indexOf(@Nullable long[] array, long valueToFind, int startIndex) {
        if (array == null) {
            return -1;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(@Nullable long[] array, long valueToFind) {
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(@Nullable long[] array, long valueToFind, int startIndex) {
        if (array == null || startIndex < 0) {
            return -1;
        }
        if (startIndex >= array.length) {
            startIndex = array.length - 1;
        }
        for (int i = startIndex; i >= 0; i--) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(@Nullable long[] array, long valueToFind) {
        return indexOf(array, valueToFind) != -1;
    }

    public static int indexOf(@Nullable int[] array, int valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    public static int indexOf(@Nullable int[] array, int valueToFind, int startIndex) {
        if (array == null) {
            return -1;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(@Nullable int[] array, int valueToFind) {
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(@Nullable int[] array, int valueToFind, int startIndex) {
        if (array == null || startIndex < 0) {
            return -1;
        }
        if (startIndex >= array.length) {
            startIndex = array.length - 1;
        }
        for (int i = startIndex; i >= 0; i--) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(@Nullable int[] array, int valueToFind) {
        return indexOf(array, valueToFind) != -1;
    }

    public static int indexOf(@Nullable short[] array, short valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    public static int indexOf(@Nullable short[] array, short valueToFind, int startIndex) {
        if (array == null) {
            return -1;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(@Nullable short[] array, short valueToFind) {
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(@Nullable short[] array, short valueToFind, int startIndex) {
        if (array == null || startIndex < 0) {
            return -1;
        }
        if (startIndex >= array.length) {
            startIndex = array.length - 1;
        }
        for (int i = startIndex; i >= 0; i--) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(@Nullable short[] array, short valueToFind) {
        return indexOf(array, valueToFind) != -1;
    }

    public static int indexOf(@Nullable char[] array, char valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    public static int indexOf(@Nullable char[] array, char valueToFind, int startIndex) {
        if (array == null) {
            return -1;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(@Nullable char[] array, char valueToFind) {
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(@Nullable char[] array, char valueToFind, int startIndex) {
        if (array == null || startIndex < 0) {
            return -1;
        }
        if (startIndex >= array.length) {
            startIndex = array.length - 1;
        }
        for (int i = startIndex; i >= 0; i--) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(@Nullable char[] array, char valueToFind) {
        return indexOf(array, valueToFind) != -1;
    }

    public static int indexOf(@Nullable byte[] array, byte valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    public static int indexOf(@Nullable byte[] array, byte valueToFind, int startIndex) {
        if (array == null) {
            return -1;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(@Nullable byte[] array, byte valueToFind) {
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(@Nullable byte[] array, byte valueToFind, int startIndex) {
        if (array == null || startIndex < 0) {
            return -1;
        }
        if (startIndex >= array.length) {
            startIndex = array.length - 1;
        }
        for (int i = startIndex; i >= 0; i--) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(@Nullable byte[] array, byte valueToFind) {
        return indexOf(array, valueToFind) != -1;
    }

    public static int indexOf(@Nullable double[] array, double valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    public static int indexOf(@Nullable double[] array, double valueToFind, double tolerance) {
        return indexOf(array, valueToFind, 0, tolerance);
    }

    public static int indexOf(@Nullable double[] array, double valueToFind, int startIndex) {
        if (isEmpty(array)) {
            return -1;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(@Nullable double[] array, double valueToFind, int startIndex, double tolerance) {
        if (isEmpty(array)) {
            return -1;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        double min = valueToFind - tolerance;
        double max = valueToFind + tolerance;
        for (int i = startIndex; i < array.length; i++) {
            if (array[i] >= min && array[i] <= max) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(@Nullable double[] array, double valueToFind) {
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(@Nullable double[] array, double valueToFind, double tolerance) {
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE, tolerance);
    }

    public static int lastIndexOf(@Nullable double[] array, double valueToFind, int startIndex) {
        if (!isEmpty(array) && startIndex >= 0) {
            if (startIndex >= array.length) {
                startIndex = array.length - 1;
            }
            for (int i = startIndex; i >= 0; i--) {
                if (valueToFind == array[i]) {
                    return i;
                }
            }
            return -1;
        }
        return -1;
    }

    public static int lastIndexOf(@Nullable double[] array, double valueToFind, int startIndex, double tolerance) {
        if (!isEmpty(array) && startIndex >= 0) {
            if (startIndex >= array.length) {
                startIndex = array.length - 1;
            }
            double min = valueToFind - tolerance;
            double max = valueToFind + tolerance;
            for (int i = startIndex; i >= 0; i--) {
                if (array[i] >= min && array[i] <= max) {
                    return i;
                }
            }
            return -1;
        }
        return -1;
    }

    public static boolean contains(@Nullable double[] array, double valueToFind) {
        return indexOf(array, valueToFind) != -1;
    }

    public static boolean contains(@Nullable double[] array, double valueToFind, double tolerance) {
        return indexOf(array, valueToFind, 0, tolerance) != -1;
    }

    public static int indexOf(@Nullable float[] array, float valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    public static int indexOf(@Nullable float[] array, float valueToFind, int startIndex) {
        if (isEmpty(array)) {
            return -1;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(@Nullable float[] array, float valueToFind) {
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(@Nullable float[] array, float valueToFind, int startIndex) {
        if (!isEmpty(array) && startIndex >= 0) {
            if (startIndex >= array.length) {
                startIndex = array.length - 1;
            }
            for (int i = startIndex; i >= 0; i--) {
                if (valueToFind == array[i]) {
                    return i;
                }
            }
            return -1;
        }
        return -1;
    }

    public static boolean contains(@Nullable float[] array, float valueToFind) {
        return indexOf(array, valueToFind) != -1;
    }

    public static int indexOf(@Nullable boolean[] array, boolean valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    public static int indexOf(@Nullable boolean[] array, boolean valueToFind, int startIndex) {
        if (isEmpty(array)) {
            return -1;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(@Nullable boolean[] array, boolean valueToFind) {
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(@Nullable boolean[] array, boolean valueToFind, int startIndex) {
        if (!isEmpty(array) && startIndex >= 0) {
            if (startIndex >= array.length) {
                startIndex = array.length - 1;
            }
            for (int i = startIndex; i >= 0; i--) {
                if (valueToFind == array[i]) {
                    return i;
                }
            }
            return -1;
        }
        return -1;
    }

    public static boolean contains(@Nullable boolean[] array, boolean valueToFind) {
        return indexOf(array, valueToFind) != -1;
    }

    @Nullable
    public static char[] toPrimitive(@Nullable Character[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return new char[0];
        }
        char[] result = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].charValue();
        }
        return result;
    }

    @Nullable
    public static char[] toPrimitive(@Nullable Character[] array, char valueForNull) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return new char[0];
        }
        char[] result = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            Character b = array[i];
            result[i] = b == null ? valueForNull : b.charValue();
        }
        return result;
    }

    @Nullable
    public static Character[] toObject(@Nullable char[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return new Character[0];
        }
        Character[] result = new Character[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = new Character(array[i]);
        }
        return result;
    }

    @Nullable
    public static long[] toPrimitive(@Nullable Long[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return new long[0];
        }
        long[] result = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].longValue();
        }
        return result;
    }

    @Nullable
    public static long[] toPrimitive(@Nullable Long[] array, long valueForNull) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return new long[0];
        }
        long[] result = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            Long b = array[i];
            result[i] = b == null ? valueForNull : b.longValue();
        }
        return result;
    }

    @Nullable
    public static Long[] toObject(@Nullable long[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return new Long[0];
        }
        Long[] result = new Long[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = new Long(array[i]);
        }
        return result;
    }

    @Nullable
    public static int[] toPrimitive(@Nullable Integer[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return new int[0];
        }
        int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].intValue();
        }
        return result;
    }

    @Nullable
    public static int[] toPrimitive(@Nullable Integer[] array, int valueForNull) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return new int[0];
        }
        int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            Integer b = array[i];
            result[i] = b == null ? valueForNull : b.intValue();
        }
        return result;
    }

    @Nullable
    public static Integer[] toObject(@Nullable int[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return new Integer[0];
        }
        Integer[] result = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = new Integer(array[i]);
        }
        return result;
    }

    @Nullable
    public static short[] toPrimitive(@Nullable Short[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return new short[0];
        }
        short[] result = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].shortValue();
        }
        return result;
    }

    @Nullable
    public static short[] toPrimitive(@Nullable Short[] array, short valueForNull) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return new short[0];
        }
        short[] result = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            Short b = array[i];
            result[i] = b == null ? valueForNull : b.shortValue();
        }
        return result;
    }

    @Nullable
    public static Short[] toObject(@Nullable short[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return new Short[0];
        }
        Short[] result = new Short[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = new Short(array[i]);
        }
        return result;
    }

    @Nullable
    public static byte[] toPrimitive(@Nullable Byte[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return new byte[0];
        }
        byte[] result = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].byteValue();
        }
        return result;
    }

    @Nullable
    public static byte[] toPrimitive(@Nullable Byte[] array, byte valueForNull) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return new byte[0];
        }
        byte[] result = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            Byte b = array[i];
            result[i] = b == null ? valueForNull : b.byteValue();
        }
        return result;
    }

    @Nullable
    public static Byte[] toObject(@Nullable byte[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return new Byte[0];
        }
        Byte[] result = new Byte[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = new Byte(array[i]);
        }
        return result;
    }

    @Nullable
    public static double[] toPrimitive(@Nullable Double[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return new double[0];
        }
        double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].doubleValue();
        }
        return result;
    }

    @Nullable
    public static double[] toPrimitive(@Nullable Double[] array, double valueForNull) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return new double[0];
        }
        double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            Double b = array[i];
            result[i] = b == null ? valueForNull : b.doubleValue();
        }
        return result;
    }

    @Nullable
    public static Double[] toObject(@Nullable double[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return new Double[0];
        }
        Double[] result = new Double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = new Double(array[i]);
        }
        return result;
    }

    @Nullable
    public static float[] toPrimitive(@Nullable Float[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return new float[0];
        }
        float[] result = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].floatValue();
        }
        return result;
    }

    @Nullable
    public static float[] toPrimitive(@Nullable Float[] array, float valueForNull) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return new float[0];
        }
        float[] result = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            Float b = array[i];
            result[i] = b == null ? valueForNull : b.floatValue();
        }
        return result;
    }

    @Nullable
    public static Float[] toObject(@Nullable float[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return new Float[0];
        }
        Float[] result = new Float[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = new Float(array[i]);
        }
        return result;
    }

    @Nullable
    public static boolean[] toPrimitive(@Nullable Boolean[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return new boolean[0];
        }
        boolean[] result = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].booleanValue();
        }
        return result;
    }

    @Nullable
    public static boolean[] toPrimitive(@Nullable Boolean[] array, boolean valueForNull) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return new boolean[0];
        }
        boolean[] result = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            Boolean b = array[i];
            result[i] = b == null ? valueForNull : b.booleanValue();
        }
        return result;
    }

    @Nullable
    public static Boolean[] toObject(@Nullable boolean[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return new Boolean[0];
        }
        Boolean[] result = new Boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i] ? Boolean.TRUE : Boolean.FALSE;
        }
        return result;
    }

    @NonNull
    public static <T> List<T> asList(@Nullable T... array) {
        if (array == null || array.length == 0) {
            List<T> tmpTrauteVar2 = Collections.emptyList();
            if (tmpTrauteVar2 != null) {
                return tmpTrauteVar2;
            }
            throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.asList() marked by @androidx.annotation.NonNull");
        }
        List<T> tmpTrauteVar3 = Arrays.asList(array);
        if (tmpTrauteVar3 != null) {
            return tmpTrauteVar3;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.asList() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static <T> List<T> asUnmodifiableList(@Nullable T... array) {
        List<T> tmpTrauteVar2 = Collections.unmodifiableList(asList(array));
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.asUnmodifiableList() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public static <T> List<T> asArrayList(@Nullable T... array) {
        List<T> list = new ArrayList<>();
        if (array == null || array.length == 0) {
            return list;
        }
        list.addAll(Arrays.asList(array));
        return list;
    }

    @NonNull
    public static <T> List<T> asLinkedList(@Nullable T... array) {
        List<T> list = new LinkedList<>();
        if (array == null || array.length == 0) {
            return list;
        }
        list.addAll(Arrays.asList(array));
        return list;
    }

    public static <T> void sort(@Nullable T[] array, Comparator<? super T> c) {
        if (array == null || array.length < 2) {
            return;
        }
        Arrays.sort(array, c);
    }

    public static void sort(@Nullable byte[] array) {
        if (array == null || array.length < 2) {
            return;
        }
        Arrays.sort(array);
    }

    public static void sort(@Nullable char[] array) {
        if (array == null || array.length < 2) {
            return;
        }
        Arrays.sort(array);
    }

    public static void sort(@Nullable double[] array) {
        if (array == null || array.length < 2) {
            return;
        }
        Arrays.sort(array);
    }

    public static void sort(@Nullable float[] array) {
        if (array == null || array.length < 2) {
            return;
        }
        Arrays.sort(array);
    }

    public static void sort(@Nullable int[] array) {
        if (array == null || array.length < 2) {
            return;
        }
        Arrays.sort(array);
    }

    public static void sort(@Nullable long[] array) {
        if (array == null || array.length < 2) {
            return;
        }
        Arrays.sort(array);
    }

    public static void sort(@Nullable short[] array) {
        if (array == null || array.length < 2) {
            return;
        }
        Arrays.sort(array);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <E> void forAllDo(@Nullable Object array, @Nullable Closure<E> closure) {
        if (array == null || closure == 0) {
            return;
        }
        if (array instanceof Object[]) {
            Object[] objects = (Object[]) array;
            int length = objects.length;
            for (int i = 0; i < length; i++) {
                Object ele = objects[i];
                closure.execute(i, ele);
            }
        } else if (array instanceof boolean[]) {
            boolean[] booleans = (boolean[]) array;
            int length2 = booleans.length;
            for (int i2 = 0; i2 < length2; i2++) {
                boolean ele2 = booleans[i2];
                closure.execute(i2, ele2 ? Boolean.TRUE : Boolean.FALSE);
            }
        } else if (array instanceof byte[]) {
            byte[] bytes = (byte[]) array;
            int length3 = bytes.length;
            for (int i3 = 0; i3 < length3; i3++) {
                byte ele3 = bytes[i3];
                closure.execute(i3, Byte.valueOf(ele3));
            }
        } else if (array instanceof char[]) {
            char[] chars = (char[]) array;
            int length4 = chars.length;
            for (int i4 = 0; i4 < length4; i4++) {
                char ele4 = chars[i4];
                closure.execute(i4, Character.valueOf(ele4));
            }
        } else if (array instanceof short[]) {
            short[] shorts = (short[]) array;
            int length5 = shorts.length;
            for (int i5 = 0; i5 < length5; i5++) {
                short ele5 = shorts[i5];
                closure.execute(i5, Short.valueOf(ele5));
            }
        } else if (array instanceof int[]) {
            int[] ints = (int[]) array;
            int length6 = ints.length;
            for (int i6 = 0; i6 < length6; i6++) {
                int ele6 = ints[i6];
                closure.execute(i6, Integer.valueOf(ele6));
            }
        } else if (array instanceof long[]) {
            long[] longs = (long[]) array;
            int length7 = longs.length;
            for (int i7 = 0; i7 < length7; i7++) {
                long ele7 = longs[i7];
                closure.execute(i7, Long.valueOf(ele7));
            }
        } else if (array instanceof float[]) {
            float[] floats = (float[]) array;
            int length8 = floats.length;
            for (int i8 = 0; i8 < length8; i8++) {
                float ele8 = floats[i8];
                closure.execute(i8, Float.valueOf(ele8));
            }
        } else if (array instanceof double[]) {
            double[] doubles = (double[]) array;
            int length9 = doubles.length;
            for (int i9 = 0; i9 < length9; i9++) {
                double ele9 = doubles[i9];
                closure.execute(i9, Double.valueOf(ele9));
            }
        } else {
            throw new IllegalArgumentException("Not an array: " + array.getClass());
        }
    }

    @NonNull
    public static String toString(@Nullable Object array) {
        if (array == null) {
            return "null";
        }
        if (array instanceof Object[]) {
            String tmpTrauteVar3 = Arrays.deepToString((Object[]) array);
            if (tmpTrauteVar3 != null) {
                return tmpTrauteVar3;
            }
            throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.toString() marked by @androidx.annotation.NonNull");
        } else if (array instanceof boolean[]) {
            String tmpTrauteVar4 = Arrays.toString((boolean[]) array);
            if (tmpTrauteVar4 != null) {
                return tmpTrauteVar4;
            }
            throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.toString() marked by @androidx.annotation.NonNull");
        } else if (array instanceof byte[]) {
            String tmpTrauteVar5 = Arrays.toString((byte[]) array);
            if (tmpTrauteVar5 != null) {
                return tmpTrauteVar5;
            }
            throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.toString() marked by @androidx.annotation.NonNull");
        } else if (array instanceof char[]) {
            String tmpTrauteVar6 = Arrays.toString((char[]) array);
            if (tmpTrauteVar6 != null) {
                return tmpTrauteVar6;
            }
            throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.toString() marked by @androidx.annotation.NonNull");
        } else if (array instanceof double[]) {
            String tmpTrauteVar7 = Arrays.toString((double[]) array);
            if (tmpTrauteVar7 != null) {
                return tmpTrauteVar7;
            }
            throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.toString() marked by @androidx.annotation.NonNull");
        } else if (array instanceof float[]) {
            String tmpTrauteVar8 = Arrays.toString((float[]) array);
            if (tmpTrauteVar8 != null) {
                return tmpTrauteVar8;
            }
            throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.toString() marked by @androidx.annotation.NonNull");
        } else if (array instanceof int[]) {
            String tmpTrauteVar9 = Arrays.toString((int[]) array);
            if (tmpTrauteVar9 != null) {
                return tmpTrauteVar9;
            }
            throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.toString() marked by @androidx.annotation.NonNull");
        } else if (array instanceof long[]) {
            String tmpTrauteVar10 = Arrays.toString((long[]) array);
            if (tmpTrauteVar10 != null) {
                return tmpTrauteVar10;
            }
            throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.toString() marked by @androidx.annotation.NonNull");
        } else if (array instanceof short[]) {
            String tmpTrauteVar11 = Arrays.toString((short[]) array);
            if (tmpTrauteVar11 != null) {
                return tmpTrauteVar11;
            }
            throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ArrayUtils.toString() marked by @androidx.annotation.NonNull");
        } else {
            throw new IllegalArgumentException("Array has incompatible type: " + array.getClass());
        }
    }
}
