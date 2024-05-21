package com.blankj.utilcode.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import java.io.Serializable;
import org.json.JSONArray;
import org.json.JSONObject;
/* loaded from: classes4.dex */
public final class CacheDoubleStaticUtils {
    private static CacheDoubleUtils sDefaultCacheDoubleUtils;

    public static void setDefaultCacheDoubleUtils(CacheDoubleUtils cacheDoubleUtils) {
        sDefaultCacheDoubleUtils = cacheDoubleUtils;
    }

    public static void put(@NonNull String key, byte[] value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, getDefaultCacheDoubleUtils());
    }

    public static void put(@NonNull String key, byte[] value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, saveTime, getDefaultCacheDoubleUtils());
    }

    public static byte[] getBytes(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getBytes(key, getDefaultCacheDoubleUtils());
    }

    public static byte[] getBytes(@NonNull String key, byte[] defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getBytes(key, defaultValue, getDefaultCacheDoubleUtils());
    }

    public static void put(@NonNull String key, String value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, getDefaultCacheDoubleUtils());
    }

    public static void put(@NonNull String key, String value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, saveTime, getDefaultCacheDoubleUtils());
    }

    public static String getString(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getString(key, getDefaultCacheDoubleUtils());
    }

    public static String getString(@NonNull String key, String defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getString(key, defaultValue, getDefaultCacheDoubleUtils());
    }

    public static void put(@NonNull String key, JSONObject value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, getDefaultCacheDoubleUtils());
    }

    public static void put(@NonNull String key, JSONObject value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, saveTime, getDefaultCacheDoubleUtils());
    }

    public static JSONObject getJSONObject(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getJSONObject(key, getDefaultCacheDoubleUtils());
    }

    public static JSONObject getJSONObject(@NonNull String key, JSONObject defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getJSONObject(key, defaultValue, getDefaultCacheDoubleUtils());
    }

    public static void put(@NonNull String key, JSONArray value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, getDefaultCacheDoubleUtils());
    }

    public static void put(@NonNull String key, JSONArray value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, saveTime, getDefaultCacheDoubleUtils());
    }

    public static JSONArray getJSONArray(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getJSONArray(key, getDefaultCacheDoubleUtils());
    }

    public static JSONArray getJSONArray(@NonNull String key, JSONArray defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getJSONArray(key, defaultValue, getDefaultCacheDoubleUtils());
    }

    public static void put(@NonNull String key, Bitmap value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, getDefaultCacheDoubleUtils());
    }

    public static void put(@NonNull String key, Bitmap value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, saveTime, getDefaultCacheDoubleUtils());
    }

    public static Bitmap getBitmap(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getBitmap(key, getDefaultCacheDoubleUtils());
    }

    public static Bitmap getBitmap(@NonNull String key, Bitmap defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getBitmap(key, defaultValue, getDefaultCacheDoubleUtils());
    }

    public static void put(@NonNull String key, Drawable value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, getDefaultCacheDoubleUtils());
    }

    public static void put(@NonNull String key, Drawable value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, saveTime, getDefaultCacheDoubleUtils());
    }

    public static Drawable getDrawable(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getDrawable(key, getDefaultCacheDoubleUtils());
    }

    public static Drawable getDrawable(@NonNull String key, Drawable defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getDrawable(key, defaultValue, getDefaultCacheDoubleUtils());
    }

    public static void put(@NonNull String key, Parcelable value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, getDefaultCacheDoubleUtils());
    }

    public static void put(@NonNull String key, Parcelable value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, saveTime, getDefaultCacheDoubleUtils());
    }

    public static <T> T getParcelable(@NonNull String key, @NonNull Parcelable.Creator<T> creator) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (creator == null) {
            throw new NullPointerException("Argument 'creator' of type Parcelable.Creator<T> (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return (T) getParcelable(key, (Parcelable.Creator<Object>) creator, getDefaultCacheDoubleUtils());
    }

    public static <T> T getParcelable(@NonNull String key, @NonNull Parcelable.Creator<T> creator, T defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (creator == null) {
            throw new NullPointerException("Argument 'creator' of type Parcelable.Creator<T> (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return (T) getParcelable(key, creator, defaultValue, getDefaultCacheDoubleUtils());
    }

    public static void put(@NonNull String key, Serializable value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, getDefaultCacheDoubleUtils());
    }

    public static void put(@NonNull String key, Serializable value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, saveTime, getDefaultCacheDoubleUtils());
    }

    public static Object getSerializable(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getSerializable(key, getDefaultCacheDoubleUtils());
    }

    public static Object getSerializable(@NonNull String key, Object defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getSerializable(key, defaultValue, getDefaultCacheDoubleUtils());
    }

    public static long getCacheDiskSize() {
        return getCacheDiskSize(getDefaultCacheDoubleUtils());
    }

    public static int getCacheDiskCount() {
        return getCacheDiskCount(getDefaultCacheDoubleUtils());
    }

    public static int getCacheMemoryCount() {
        return getCacheMemoryCount(getDefaultCacheDoubleUtils());
    }

    public static void remove(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        remove(key, getDefaultCacheDoubleUtils());
    }

    public static void clear() {
        clear(getDefaultCacheDoubleUtils());
    }

    public static void put(@NonNull String key, byte[] value, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDoubleUtils.put(key, value);
    }

    public static void put(@NonNull String key, byte[] value, int saveTime, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDoubleUtils.put(key, value, saveTime);
    }

    public static byte[] getBytes(@NonNull String key, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key != null) {
            if (cacheDoubleUtils == null) {
                throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return cacheDoubleUtils.getBytes(key);
        }
        throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static byte[] getBytes(@NonNull String key, byte[] defaultValue, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return cacheDoubleUtils.getBytes(key, defaultValue);
    }

    public static void put(@NonNull String key, String value, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDoubleUtils.put(key, value);
    }

    public static void put(@NonNull String key, String value, int saveTime, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDoubleUtils.put(key, value, saveTime);
    }

    public static String getString(@NonNull String key, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key != null) {
            if (cacheDoubleUtils == null) {
                throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return cacheDoubleUtils.getString(key);
        }
        throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static String getString(@NonNull String key, String defaultValue, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return cacheDoubleUtils.getString(key, defaultValue);
    }

    public static void put(@NonNull String key, JSONObject value, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDoubleUtils.put(key, value);
    }

    public static void put(@NonNull String key, JSONObject value, int saveTime, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDoubleUtils.put(key, value, saveTime);
    }

    public static JSONObject getJSONObject(@NonNull String key, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return cacheDoubleUtils.getJSONObject(key);
    }

    public static JSONObject getJSONObject(@NonNull String key, JSONObject defaultValue, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return cacheDoubleUtils.getJSONObject(key, defaultValue);
    }

    public static void put(@NonNull String key, JSONArray value, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDoubleUtils.put(key, value);
    }

    public static void put(@NonNull String key, JSONArray value, int saveTime, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDoubleUtils.put(key, value, saveTime);
    }

    public static JSONArray getJSONArray(@NonNull String key, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key != null) {
            if (cacheDoubleUtils == null) {
                throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return cacheDoubleUtils.getJSONArray(key);
        }
        throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static JSONArray getJSONArray(@NonNull String key, JSONArray defaultValue, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return cacheDoubleUtils.getJSONArray(key, defaultValue);
    }

    public static void put(@NonNull String key, Bitmap value, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDoubleUtils.put(key, value);
    }

    public static void put(@NonNull String key, Bitmap value, int saveTime, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDoubleUtils.put(key, value, saveTime);
    }

    public static Bitmap getBitmap(@NonNull String key, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key != null) {
            if (cacheDoubleUtils == null) {
                throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return cacheDoubleUtils.getBitmap(key);
        }
        throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static Bitmap getBitmap(@NonNull String key, Bitmap defaultValue, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return cacheDoubleUtils.getBitmap(key, defaultValue);
    }

    public static void put(@NonNull String key, Drawable value, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDoubleUtils.put(key, value);
    }

    public static void put(@NonNull String key, Drawable value, int saveTime, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDoubleUtils.put(key, value, saveTime);
    }

    public static Drawable getDrawable(@NonNull String key, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key != null) {
            if (cacheDoubleUtils == null) {
                throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return cacheDoubleUtils.getDrawable(key);
        }
        throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static Drawable getDrawable(@NonNull String key, Drawable defaultValue, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return cacheDoubleUtils.getDrawable(key, defaultValue);
    }

    public static void put(@NonNull String key, Parcelable value, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDoubleUtils.put(key, value);
    }

    public static void put(@NonNull String key, Parcelable value, int saveTime, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDoubleUtils.put(key, value, saveTime);
    }

    public static <T> T getParcelable(@NonNull String key, @NonNull Parcelable.Creator<T> creator, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (creator == null) {
            throw new NullPointerException("Argument 'creator' of type Parcelable.Creator<T> (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return (T) cacheDoubleUtils.getParcelable(key, creator);
    }

    public static <T> T getParcelable(@NonNull String key, @NonNull Parcelable.Creator<T> creator, T defaultValue, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (creator == null) {
            throw new NullPointerException("Argument 'creator' of type Parcelable.Creator<T> (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return (T) cacheDoubleUtils.getParcelable(key, creator, defaultValue);
    }

    public static void put(@NonNull String key, Serializable value, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDoubleUtils.put(key, value);
    }

    public static void put(@NonNull String key, Serializable value, int saveTime, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDoubleUtils.put(key, value, saveTime);
    }

    public static Object getSerializable(@NonNull String key, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key != null) {
            if (cacheDoubleUtils == null) {
                throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return cacheDoubleUtils.getSerializable(key);
        }
        throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static Object getSerializable(@NonNull String key, Object defaultValue, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return cacheDoubleUtils.getSerializable(key, defaultValue);
    }

    public static long getCacheDiskSize(@NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return cacheDoubleUtils.getCacheDiskSize();
    }

    public static int getCacheDiskCount(@NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return cacheDoubleUtils.getCacheDiskCount();
    }

    public static int getCacheMemoryCount(@NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return cacheDoubleUtils.getCacheMemoryCount();
    }

    public static void remove(@NonNull String key, @NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDoubleUtils.remove(key);
    }

    public static void clear(@NonNull CacheDoubleUtils cacheDoubleUtils) {
        if (cacheDoubleUtils == null) {
            throw new NullPointerException("Argument 'cacheDoubleUtils' of type CacheDoubleUtils (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDoubleUtils.clear();
    }

    private static CacheDoubleUtils getDefaultCacheDoubleUtils() {
        CacheDoubleUtils cacheDoubleUtils = sDefaultCacheDoubleUtils;
        return cacheDoubleUtils != null ? cacheDoubleUtils : CacheDoubleUtils.getInstance();
    }
}
