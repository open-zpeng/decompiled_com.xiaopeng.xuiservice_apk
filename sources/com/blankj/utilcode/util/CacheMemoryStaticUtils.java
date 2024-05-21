package com.blankj.utilcode.util;

import androidx.annotation.NonNull;
/* loaded from: classes4.dex */
public final class CacheMemoryStaticUtils {
    private static CacheMemoryUtils sDefaultCacheMemoryUtils;

    public static void setDefaultCacheMemoryUtils(CacheMemoryUtils cacheMemoryUtils) {
        sDefaultCacheMemoryUtils = cacheMemoryUtils;
    }

    public static void put(@NonNull String key, Object value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, getDefaultCacheMemoryUtils());
    }

    public static void put(@NonNull String key, Object value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, saveTime, getDefaultCacheMemoryUtils());
    }

    public static <T> T get(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return (T) get(key, getDefaultCacheMemoryUtils());
    }

    public static <T> T get(@NonNull String key, T defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return (T) get(key, defaultValue, getDefaultCacheMemoryUtils());
    }

    public static int getCacheCount() {
        return getCacheCount(getDefaultCacheMemoryUtils());
    }

    public static Object remove(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return remove(key, getDefaultCacheMemoryUtils());
    }

    public static void clear() {
        clear(getDefaultCacheMemoryUtils());
    }

    public static void put(@NonNull String key, Object value, @NonNull CacheMemoryUtils cacheMemoryUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheMemoryUtils == null) {
            throw new NullPointerException("Argument 'cacheMemoryUtils' of type CacheMemoryUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheMemoryUtils.put(key, value);
    }

    public static void put(@NonNull String key, Object value, int saveTime, @NonNull CacheMemoryUtils cacheMemoryUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheMemoryUtils == null) {
            throw new NullPointerException("Argument 'cacheMemoryUtils' of type CacheMemoryUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheMemoryUtils.put(key, value, saveTime);
    }

    public static <T> T get(@NonNull String key, @NonNull CacheMemoryUtils cacheMemoryUtils) {
        if (key != null) {
            if (cacheMemoryUtils == null) {
                throw new NullPointerException("Argument 'cacheMemoryUtils' of type CacheMemoryUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return (T) cacheMemoryUtils.get(key);
        }
        throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static <T> T get(@NonNull String key, T defaultValue, @NonNull CacheMemoryUtils cacheMemoryUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheMemoryUtils == null) {
            throw new NullPointerException("Argument 'cacheMemoryUtils' of type CacheMemoryUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return (T) cacheMemoryUtils.get(key, defaultValue);
    }

    public static int getCacheCount(@NonNull CacheMemoryUtils cacheMemoryUtils) {
        if (cacheMemoryUtils == null) {
            throw new NullPointerException("Argument 'cacheMemoryUtils' of type CacheMemoryUtils (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return cacheMemoryUtils.getCacheCount();
    }

    public static Object remove(@NonNull String key, @NonNull CacheMemoryUtils cacheMemoryUtils) {
        if (key != null) {
            if (cacheMemoryUtils == null) {
                throw new NullPointerException("Argument 'cacheMemoryUtils' of type CacheMemoryUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return cacheMemoryUtils.remove(key);
        }
        throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static void clear(@NonNull CacheMemoryUtils cacheMemoryUtils) {
        if (cacheMemoryUtils == null) {
            throw new NullPointerException("Argument 'cacheMemoryUtils' of type CacheMemoryUtils (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        cacheMemoryUtils.clear();
    }

    private static CacheMemoryUtils getDefaultCacheMemoryUtils() {
        CacheMemoryUtils cacheMemoryUtils = sDefaultCacheMemoryUtils;
        return cacheMemoryUtils != null ? cacheMemoryUtils : CacheMemoryUtils.getInstance();
    }
}
