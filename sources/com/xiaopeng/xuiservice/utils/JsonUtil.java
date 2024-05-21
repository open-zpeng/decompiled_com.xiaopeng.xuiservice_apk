package com.xiaopeng.xuiservice.utils;

import android.util.Pair;
import org.json.JSONArray;
import org.json.JSONException;
/* loaded from: classes5.dex */
public class JsonUtil {
    public static String[] jsonArray2StringArray(JSONArray jsonArray) throws JSONException {
        return JsonArray2JavaArray.stringArray(jsonArray);
    }

    public static int[] jsonArray2intArray(JSONArray jsonArray) throws JSONException {
        return JsonArray2JavaArray.intArray(jsonArray);
    }

    public static Integer[] jsonArray2IntegerArray(JSONArray jsonArray) throws JSONException {
        return JsonArray2JavaArray.IntegerArray(jsonArray);
    }

    public static long[] jsonArray2longArray(JSONArray jsonArray) throws JSONException {
        return JsonArray2JavaArray.longArray(jsonArray);
    }

    public static Long[] jsonArray2LongArray(JSONArray jsonArray) throws JSONException {
        return JsonArray2JavaArray.LongArray(jsonArray);
    }

    public static double[] jsonArray2doubleArray(JSONArray jsonArray) throws JSONException {
        return JsonArray2JavaArray.doubleArray(jsonArray);
    }

    public static Double[] jsonArray2DoubleArray(JSONArray jsonArray) throws JSONException {
        return JsonArray2JavaArray.DoubleArray(jsonArray);
    }

    public static boolean[] jsonArray2booleanArray(JSONArray jsonArray) throws JSONException {
        return JsonArray2JavaArray.booleanArray(jsonArray);
    }

    public static Boolean[] jsonArray2BooleanArray(JSONArray jsonArray) throws JSONException {
        return JsonArray2JavaArray.BooleanArray(jsonArray);
    }

    public static Pair<String, String> jsonArray2StringPair(JSONArray jsonArray) throws JSONException {
        return JsonArray2Pair.stringPair(jsonArray);
    }

    public static Pair<Integer, Integer> jsonArray2IntegerPair(JSONArray jsonArray) throws JSONException {
        return JsonArray2Pair.integerPair(jsonArray);
    }

    public static Pair<Long, Long> jsonArray2LongPair(JSONArray jsonArray) throws JSONException {
        return JsonArray2Pair.longPair(jsonArray);
    }

    public static Pair<Double, Double> jsonArray2DoublePair(JSONArray jsonArray) throws JSONException {
        return JsonArray2Pair.doublePair(jsonArray);
    }

    public static Pair<Boolean, Boolean> jsonArray2BooleanPair(JSONArray jsonArray) throws JSONException {
        return JsonArray2Pair.booleanPair(jsonArray);
    }

    /* loaded from: classes5.dex */
    private static class JsonArray2JavaArray {
        private JsonArray2JavaArray() {
        }

        static String[] stringArray(JSONArray jsonArray) throws JSONException {
            int length = jsonArray.length();
            String[] array = new String[length];
            for (int i = 0; i < length; i++) {
                array[i] = jsonArray.getString(i);
            }
            return array;
        }

        static int[] intArray(JSONArray jsonArray) throws JSONException {
            int length = jsonArray.length();
            int[] array = new int[length];
            for (int i = 0; i < length; i++) {
                array[i] = jsonArray.getInt(i);
            }
            return array;
        }

        static Integer[] IntegerArray(JSONArray jsonArray) throws JSONException {
            int length = jsonArray.length();
            Integer[] array = new Integer[length];
            for (int i = 0; i < length; i++) {
                array[i] = Integer.valueOf(jsonArray.getInt(i));
            }
            return array;
        }

        static long[] longArray(JSONArray jsonArray) throws JSONException {
            int length = jsonArray.length();
            long[] array = new long[length];
            for (int i = 0; i < length; i++) {
                array[i] = jsonArray.getLong(i);
            }
            return array;
        }

        static Long[] LongArray(JSONArray jsonArray) throws JSONException {
            int length = jsonArray.length();
            Long[] array = new Long[length];
            for (int i = 0; i < length; i++) {
                array[i] = Long.valueOf(jsonArray.getLong(i));
            }
            return array;
        }

        static double[] doubleArray(JSONArray jsonArray) throws JSONException {
            int length = jsonArray.length();
            double[] array = new double[length];
            for (int i = 0; i < length; i++) {
                array[i] = jsonArray.getDouble(i);
            }
            return array;
        }

        static Double[] DoubleArray(JSONArray jsonArray) throws JSONException {
            int length = jsonArray.length();
            Double[] array = new Double[length];
            for (int i = 0; i < length; i++) {
                array[i] = Double.valueOf(jsonArray.getDouble(i));
            }
            return array;
        }

        static boolean[] booleanArray(JSONArray jsonArray) throws JSONException {
            int length = jsonArray.length();
            boolean[] array = new boolean[length];
            for (int i = 0; i < length; i++) {
                array[i] = jsonArray.getBoolean(i);
            }
            return array;
        }

        static Boolean[] BooleanArray(JSONArray jsonArray) throws JSONException {
            int length = jsonArray.length();
            Boolean[] array = new Boolean[length];
            for (int i = 0; i < length; i++) {
                array[i] = Boolean.valueOf(jsonArray.getBoolean(i));
            }
            return array;
        }
    }

    /* loaded from: classes5.dex */
    private static class JsonArray2Pair {
        private JsonArray2Pair() {
        }

        static Pair<String, String> stringPair(JSONArray jsonArray) throws JSONException {
            return Pair.create(jsonArray.getString(0), jsonArray.getString(1));
        }

        static Pair<Integer, Integer> integerPair(JSONArray jsonArray) throws JSONException {
            return Pair.create(Integer.valueOf(jsonArray.getInt(0)), Integer.valueOf(jsonArray.getInt(1)));
        }

        static Pair<Long, Long> longPair(JSONArray jsonArray) throws JSONException {
            return Pair.create(Long.valueOf(jsonArray.getLong(0)), Long.valueOf(jsonArray.getLong(1)));
        }

        static Pair<Double, Double> doublePair(JSONArray jsonArray) throws JSONException {
            return Pair.create(Double.valueOf(jsonArray.getDouble(0)), Double.valueOf(jsonArray.getDouble(1)));
        }

        static Pair<Boolean, Boolean> booleanPair(JSONArray jsonArray) throws JSONException {
            return Pair.create(Boolean.valueOf(jsonArray.getBoolean(0)), Boolean.valueOf(jsonArray.getBoolean(1)));
        }
    }
}
