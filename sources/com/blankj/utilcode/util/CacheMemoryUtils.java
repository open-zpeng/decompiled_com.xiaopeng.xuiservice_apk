package com.blankj.utilcode.util;

import androidx.annotation.NonNull;
import androidx.collection.LruCache;
import com.blankj.utilcode.constant.CacheConstants;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes4.dex */
public final class CacheMemoryUtils implements CacheConstants {
    private static final Map<String, CacheMemoryUtils> CACHE_MAP = new HashMap();
    private static final int DEFAULT_MAX_COUNT = 256;
    private final String mCacheKey;
    private final LruCache<String, CacheValue> mMemoryCache;

    public static CacheMemoryUtils getInstance() {
        return getInstance(256);
    }

    public static CacheMemoryUtils getInstance(int maxCount) {
        return getInstance(String.valueOf(maxCount), maxCount);
    }

    public static CacheMemoryUtils getInstance(String cacheKey, int maxCount) {
        CacheMemoryUtils cache = CACHE_MAP.get(cacheKey);
        if (cache == null) {
            synchronized (CacheMemoryUtils.class) {
                cache = CACHE_MAP.get(cacheKey);
                if (cache == null) {
                    cache = new CacheMemoryUtils(cacheKey, new LruCache(maxCount));
                    CACHE_MAP.put(cacheKey, cache);
                }
            }
        }
        return cache;
    }

    private CacheMemoryUtils(String cacheKey, LruCache<String, CacheValue> memoryCache) {
        this.mCacheKey = cacheKey;
        this.mMemoryCache = memoryCache;
    }

    public String toString() {
        return this.mCacheKey + "@" + Integer.toHexString(hashCode());
    }

    public void put(@NonNull String key, Object value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, -1);
    }

    public void put(@NonNull String key, Object value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (value == null) {
            return;
        }
        long dueTime = saveTime < 0 ? -1L : System.currentTimeMillis() + (saveTime * 1000);
        this.mMemoryCache.put(key, new CacheValue(dueTime, value));
    }

    public <T> T get(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return (T) get(key, null);
    }

    public <T> T get(@NonNull String key, T defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        CacheValue val = this.mMemoryCache.get(key);
        if (val == null) {
            return defaultValue;
        }
        if (val.dueTime == -1 || val.dueTime >= System.currentTimeMillis()) {
            return (T) val.value;
        }
        this.mMemoryCache.remove(key);
        return defaultValue;
    }

    public int getCacheCount() {
        return this.mMemoryCache.size();
    }

    public Object remove(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        CacheValue remove = this.mMemoryCache.remove(key);
        if (remove == null) {
            return null;
        }
        return remove.value;
    }

    public void clear() {
        this.mMemoryCache.evictAll();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class CacheValue {
        long dueTime;
        Object value;

        CacheValue(long dueTime, Object value) {
            this.dueTime = dueTime;
            this.value = value;
        }
    }
}
