package com.blankj.utilcode.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.Serializable;
import org.json.JSONArray;
import org.json.JSONObject;
/* loaded from: classes4.dex */
public final class CacheDiskStaticUtils {
    private static CacheDiskUtils sDefaultCacheDiskUtils;

    public static void setDefaultCacheDiskUtils(@Nullable CacheDiskUtils cacheDiskUtils) {
        sDefaultCacheDiskUtils = cacheDiskUtils;
    }

    public static void put(@NonNull String key, @Nullable byte[] value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, getDefaultCacheDiskUtils());
    }

    public static void put(@NonNull String key, @Nullable byte[] value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, saveTime, getDefaultCacheDiskUtils());
    }

    public static byte[] getBytes(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getBytes(key, getDefaultCacheDiskUtils());
    }

    public static byte[] getBytes(@NonNull String key, @Nullable byte[] defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getBytes(key, defaultValue, getDefaultCacheDiskUtils());
    }

    public static void put(@NonNull String key, @Nullable String value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, getDefaultCacheDiskUtils());
    }

    public static void put(@NonNull String key, @Nullable String value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, saveTime, getDefaultCacheDiskUtils());
    }

    public static String getString(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getString(key, getDefaultCacheDiskUtils());
    }

    public static String getString(@NonNull String key, @Nullable String defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getString(key, defaultValue, getDefaultCacheDiskUtils());
    }

    public static void put(@NonNull String key, @Nullable JSONObject value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, getDefaultCacheDiskUtils());
    }

    public static void put(@NonNull String key, @Nullable JSONObject value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, saveTime, getDefaultCacheDiskUtils());
    }

    public static JSONObject getJSONObject(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getJSONObject(key, getDefaultCacheDiskUtils());
    }

    public static JSONObject getJSONObject(@NonNull String key, @Nullable JSONObject defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getJSONObject(key, defaultValue, getDefaultCacheDiskUtils());
    }

    public static void put(@NonNull String key, @Nullable JSONArray value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, getDefaultCacheDiskUtils());
    }

    public static void put(@NonNull String key, @Nullable JSONArray value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, saveTime, getDefaultCacheDiskUtils());
    }

    public static JSONArray getJSONArray(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getJSONArray(key, getDefaultCacheDiskUtils());
    }

    public static JSONArray getJSONArray(@NonNull String key, @Nullable JSONArray defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getJSONArray(key, defaultValue, getDefaultCacheDiskUtils());
    }

    public static void put(@NonNull String key, @Nullable Bitmap value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, getDefaultCacheDiskUtils());
    }

    public static void put(@NonNull String key, @Nullable Bitmap value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, saveTime, getDefaultCacheDiskUtils());
    }

    public static Bitmap getBitmap(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getBitmap(key, getDefaultCacheDiskUtils());
    }

    public static Bitmap getBitmap(@NonNull String key, @Nullable Bitmap defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getBitmap(key, defaultValue, getDefaultCacheDiskUtils());
    }

    public static void put(@NonNull String key, @Nullable Drawable value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, getDefaultCacheDiskUtils());
    }

    public static void put(@NonNull String key, @Nullable Drawable value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, saveTime, getDefaultCacheDiskUtils());
    }

    public static Drawable getDrawable(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getDrawable(key, getDefaultCacheDiskUtils());
    }

    public static Drawable getDrawable(@NonNull String key, @Nullable Drawable defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getDrawable(key, defaultValue, getDefaultCacheDiskUtils());
    }

    public static void put(@NonNull String key, @Nullable Parcelable value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, getDefaultCacheDiskUtils());
    }

    public static void put(@NonNull String key, @Nullable Parcelable value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, saveTime, getDefaultCacheDiskUtils());
    }

    public static <T> T getParcelable(@NonNull String key, @NonNull Parcelable.Creator<T> creator) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (creator == null) {
            throw new NullPointerException("Argument 'creator' of type Parcelable.Creator<T> (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return (T) getParcelable(key, (Parcelable.Creator<Object>) creator, getDefaultCacheDiskUtils());
    }

    public static <T> T getParcelable(@NonNull String key, @NonNull Parcelable.Creator<T> creator, @Nullable T defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (creator == null) {
            throw new NullPointerException("Argument 'creator' of type Parcelable.Creator<T> (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return (T) getParcelable(key, creator, defaultValue, getDefaultCacheDiskUtils());
    }

    public static void put(@NonNull String key, @Nullable Serializable value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, getDefaultCacheDiskUtils());
    }

    public static void put(@NonNull String key, @Nullable Serializable value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, saveTime, getDefaultCacheDiskUtils());
    }

    public static Object getSerializable(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getSerializable(key, getDefaultCacheDiskUtils());
    }

    public static Object getSerializable(@NonNull String key, @Nullable Object defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getSerializable(key, defaultValue, getDefaultCacheDiskUtils());
    }

    public static long getCacheSize() {
        return getCacheSize(getDefaultCacheDiskUtils());
    }

    public static int getCacheCount() {
        return getCacheCount(getDefaultCacheDiskUtils());
    }

    public static boolean remove(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return remove(key, getDefaultCacheDiskUtils());
    }

    public static boolean clear() {
        return clear(getDefaultCacheDiskUtils());
    }

    public static void put(@NonNull String key, @Nullable byte[] value, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDiskUtils.put(key, value);
    }

    public static void put(@NonNull String key, @Nullable byte[] value, int saveTime, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDiskUtils.put(key, value, saveTime);
    }

    public static byte[] getBytes(@NonNull String key, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key != null) {
            if (cacheDiskUtils == null) {
                throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return cacheDiskUtils.getBytes(key);
        }
        throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static byte[] getBytes(@NonNull String key, @Nullable byte[] defaultValue, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return cacheDiskUtils.getBytes(key, defaultValue);
    }

    public static void put(@NonNull String key, @Nullable String value, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDiskUtils.put(key, value);
    }

    public static void put(@NonNull String key, @Nullable String value, int saveTime, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDiskUtils.put(key, value, saveTime);
    }

    public static String getString(@NonNull String key, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key != null) {
            if (cacheDiskUtils == null) {
                throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return cacheDiskUtils.getString(key);
        }
        throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static String getString(@NonNull String key, @Nullable String defaultValue, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return cacheDiskUtils.getString(key, defaultValue);
    }

    public static void put(@NonNull String key, @Nullable JSONObject value, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDiskUtils.put(key, value);
    }

    public static void put(@NonNull String key, @Nullable JSONObject value, int saveTime, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDiskUtils.put(key, value, saveTime);
    }

    public static JSONObject getJSONObject(@NonNull String key, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key != null) {
            if (cacheDiskUtils == null) {
                throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return cacheDiskUtils.getJSONObject(key);
        }
        throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static JSONObject getJSONObject(@NonNull String key, @Nullable JSONObject defaultValue, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return cacheDiskUtils.getJSONObject(key, defaultValue);
    }

    public static void put(@NonNull String key, @Nullable JSONArray value, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDiskUtils.put(key, value);
    }

    public static void put(@NonNull String key, @Nullable JSONArray value, int saveTime, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDiskUtils.put(key, value, saveTime);
    }

    public static JSONArray getJSONArray(@NonNull String key, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key != null) {
            if (cacheDiskUtils == null) {
                throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return cacheDiskUtils.getJSONArray(key);
        }
        throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static JSONArray getJSONArray(@NonNull String key, @Nullable JSONArray defaultValue, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return cacheDiskUtils.getJSONArray(key, defaultValue);
    }

    public static void put(@NonNull String key, @Nullable Bitmap value, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDiskUtils.put(key, value);
    }

    public static void put(@NonNull String key, @Nullable Bitmap value, int saveTime, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDiskUtils.put(key, value, saveTime);
    }

    public static Bitmap getBitmap(@NonNull String key, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key != null) {
            if (cacheDiskUtils == null) {
                throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return cacheDiskUtils.getBitmap(key);
        }
        throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static Bitmap getBitmap(@NonNull String key, @Nullable Bitmap defaultValue, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return cacheDiskUtils.getBitmap(key, defaultValue);
    }

    public static void put(@NonNull String key, @Nullable Drawable value, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDiskUtils.put(key, value);
    }

    public static void put(@NonNull String key, @Nullable Drawable value, int saveTime, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDiskUtils.put(key, value, saveTime);
    }

    public static Drawable getDrawable(@NonNull String key, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key != null) {
            if (cacheDiskUtils == null) {
                throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return cacheDiskUtils.getDrawable(key);
        }
        throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static Drawable getDrawable(@NonNull String key, @Nullable Drawable defaultValue, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return cacheDiskUtils.getDrawable(key, defaultValue);
    }

    public static void put(@NonNull String key, @Nullable Parcelable value, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDiskUtils.put(key, value);
    }

    public static void put(@NonNull String key, @Nullable Parcelable value, int saveTime, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDiskUtils.put(key, value, saveTime);
    }

    public static <T> T getParcelable(@NonNull String key, @NonNull Parcelable.Creator<T> creator, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (creator == null) {
            throw new NullPointerException("Argument 'creator' of type Parcelable.Creator<T> (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return (T) cacheDiskUtils.getParcelable(key, creator);
    }

    public static <T> T getParcelable(@NonNull String key, @NonNull Parcelable.Creator<T> creator, @Nullable T defaultValue, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (creator == null) {
            throw new NullPointerException("Argument 'creator' of type Parcelable.Creator<T> (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return (T) cacheDiskUtils.getParcelable(key, creator, defaultValue);
    }

    public static void put(@NonNull String key, @Nullable Serializable value, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDiskUtils.put(key, value);
    }

    public static void put(@NonNull String key, @Nullable Serializable value, int saveTime, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheDiskUtils.put(key, value, saveTime);
    }

    public static Object getSerializable(@NonNull String key, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key != null) {
            if (cacheDiskUtils == null) {
                throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return cacheDiskUtils.getSerializable(key);
        }
        throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static Object getSerializable(@NonNull String key, @Nullable Object defaultValue, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return cacheDiskUtils.getSerializable(key, defaultValue);
    }

    public static long getCacheSize(@NonNull CacheDiskUtils cacheDiskUtils) {
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return cacheDiskUtils.getCacheSize();
    }

    public static int getCacheCount(@NonNull CacheDiskUtils cacheDiskUtils) {
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return cacheDiskUtils.getCacheCount();
    }

    public static boolean remove(@NonNull String key, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (key != null) {
            if (cacheDiskUtils == null) {
                throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return cacheDiskUtils.remove(key);
        }
        throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static boolean clear(@NonNull CacheDiskUtils cacheDiskUtils) {
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return cacheDiskUtils.clear();
    }

    @NonNull
    private static CacheDiskUtils getDefaultCacheDiskUtils() {
        CacheDiskUtils tmpTrauteVar2 = sDefaultCacheDiskUtils;
        if (tmpTrauteVar2 == null) {
            tmpTrauteVar2 = CacheDiskUtils.getInstance();
        }
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.CacheDiskStaticUtils.getDefaultCacheDiskUtils() marked by @androidx.annotation.NonNull");
    }
}
