package com.blankj.utilcode.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.blankj.utilcode.constant.CacheConstants;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
/* loaded from: classes4.dex */
public final class CacheDoubleUtils implements CacheConstants {
    private static final Map<String, CacheDoubleUtils> CACHE_MAP = new HashMap();
    private final CacheDiskUtils mCacheDiskUtils;
    private final CacheMemoryUtils mCacheMemoryUtils;

    public static CacheDoubleUtils getInstance() {
        return getInstance(CacheMemoryUtils.getInstance(), CacheDiskUtils.getInstance());
    }

    public static CacheDoubleUtils getInstance(@NonNull CacheMemoryUtils cacheMemoryUtils, @NonNull CacheDiskUtils cacheDiskUtils) {
        if (cacheMemoryUtils == null) {
            throw new NullPointerException("Argument 'cacheMemoryUtils' of type CacheMemoryUtils (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (cacheDiskUtils == null) {
            throw new NullPointerException("Argument 'cacheDiskUtils' of type CacheDiskUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        String cacheKey = cacheDiskUtils.toString() + "_" + cacheMemoryUtils.toString();
        CacheDoubleUtils cache = CACHE_MAP.get(cacheKey);
        if (cache == null) {
            synchronized (CacheDoubleUtils.class) {
                cache = CACHE_MAP.get(cacheKey);
                if (cache == null) {
                    cache = new CacheDoubleUtils(cacheMemoryUtils, cacheDiskUtils);
                    CACHE_MAP.put(cacheKey, cache);
                }
            }
        }
        return cache;
    }

    private CacheDoubleUtils(CacheMemoryUtils cacheMemoryUtils, CacheDiskUtils cacheUtils) {
        this.mCacheMemoryUtils = cacheMemoryUtils;
        this.mCacheDiskUtils = cacheUtils;
    }

    public void put(@NonNull String key, byte[] value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, -1);
    }

    public void put(@NonNull String key, byte[] value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        this.mCacheMemoryUtils.put(key, value, saveTime);
        this.mCacheDiskUtils.put(key, value, saveTime);
    }

    public byte[] getBytes(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getBytes(key, null);
    }

    public byte[] getBytes(@NonNull String key, byte[] defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        byte[] obj = (byte[]) this.mCacheMemoryUtils.get(key);
        if (obj != null) {
            return obj;
        }
        byte[] bytes = this.mCacheDiskUtils.getBytes(key);
        if (bytes != null) {
            this.mCacheMemoryUtils.put(key, bytes);
            return bytes;
        }
        return defaultValue;
    }

    public void put(@NonNull String key, String value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, -1);
    }

    public void put(@NonNull String key, String value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        this.mCacheMemoryUtils.put(key, value, saveTime);
        this.mCacheDiskUtils.put(key, value, saveTime);
    }

    public String getString(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getString(key, null);
    }

    public String getString(@NonNull String key, String defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        String obj = (String) this.mCacheMemoryUtils.get(key);
        if (obj != null) {
            return obj;
        }
        String string = this.mCacheDiskUtils.getString(key);
        if (string != null) {
            this.mCacheMemoryUtils.put(key, string);
            return string;
        }
        return defaultValue;
    }

    public void put(@NonNull String key, JSONObject value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, -1);
    }

    public void put(@NonNull String key, JSONObject value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        this.mCacheMemoryUtils.put(key, value, saveTime);
        this.mCacheDiskUtils.put(key, value, saveTime);
    }

    public JSONObject getJSONObject(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getJSONObject(key, null);
    }

    public JSONObject getJSONObject(@NonNull String key, JSONObject defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        JSONObject obj = (JSONObject) this.mCacheMemoryUtils.get(key);
        if (obj != null) {
            return obj;
        }
        JSONObject jsonObject = this.mCacheDiskUtils.getJSONObject(key);
        if (jsonObject != null) {
            this.mCacheMemoryUtils.put(key, jsonObject);
            return jsonObject;
        }
        return defaultValue;
    }

    public void put(@NonNull String key, JSONArray value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, -1);
    }

    public void put(@NonNull String key, JSONArray value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        this.mCacheMemoryUtils.put(key, value, saveTime);
        this.mCacheDiskUtils.put(key, value, saveTime);
    }

    public JSONArray getJSONArray(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getJSONArray(key, null);
    }

    public JSONArray getJSONArray(@NonNull String key, JSONArray defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        JSONArray obj = (JSONArray) this.mCacheMemoryUtils.get(key);
        if (obj != null) {
            return obj;
        }
        JSONArray jsonArray = this.mCacheDiskUtils.getJSONArray(key);
        if (jsonArray != null) {
            this.mCacheMemoryUtils.put(key, jsonArray);
            return jsonArray;
        }
        return defaultValue;
    }

    public void put(@NonNull String key, Bitmap value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, -1);
    }

    public void put(@NonNull String key, Bitmap value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        this.mCacheMemoryUtils.put(key, value, saveTime);
        this.mCacheDiskUtils.put(key, value, saveTime);
    }

    public Bitmap getBitmap(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getBitmap(key, null);
    }

    public Bitmap getBitmap(@NonNull String key, Bitmap defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        Bitmap obj = (Bitmap) this.mCacheMemoryUtils.get(key);
        if (obj != null) {
            return obj;
        }
        Bitmap bitmap = this.mCacheDiskUtils.getBitmap(key);
        if (bitmap != null) {
            this.mCacheMemoryUtils.put(key, bitmap);
            return bitmap;
        }
        return defaultValue;
    }

    public void put(@NonNull String key, Drawable value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, -1);
    }

    public void put(@NonNull String key, Drawable value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        this.mCacheMemoryUtils.put(key, value, saveTime);
        this.mCacheDiskUtils.put(key, value, saveTime);
    }

    public Drawable getDrawable(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getDrawable(key, null);
    }

    public Drawable getDrawable(@NonNull String key, Drawable defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        Drawable obj = (Drawable) this.mCacheMemoryUtils.get(key);
        if (obj != null) {
            return obj;
        }
        Drawable drawable = this.mCacheDiskUtils.getDrawable(key);
        if (drawable != null) {
            this.mCacheMemoryUtils.put(key, drawable);
            return drawable;
        }
        return defaultValue;
    }

    public void put(@NonNull String key, Parcelable value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, -1);
    }

    public void put(@NonNull String key, Parcelable value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        this.mCacheMemoryUtils.put(key, value, saveTime);
        this.mCacheDiskUtils.put(key, value, saveTime);
    }

    public <T> T getParcelable(@NonNull String key, @NonNull Parcelable.Creator<T> creator) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (creator == null) {
            throw new NullPointerException("Argument 'creator' of type Parcelable.Creator<T> (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return (T) getParcelable(key, creator, null);
    }

    public <T> T getParcelable(@NonNull String key, @NonNull Parcelable.Creator<T> creator, T defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (creator == null) {
            throw new NullPointerException("Argument 'creator' of type Parcelable.Creator<T> (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        T value = (T) this.mCacheMemoryUtils.get(key);
        if (value != null) {
            return value;
        }
        T val = (T) this.mCacheDiskUtils.getParcelable(key, creator);
        if (val != null) {
            this.mCacheMemoryUtils.put(key, val);
            return val;
        }
        return defaultValue;
    }

    public void put(@NonNull String key, Serializable value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, -1);
    }

    public void put(@NonNull String key, Serializable value, int saveTime) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        this.mCacheMemoryUtils.put(key, value, saveTime);
        this.mCacheDiskUtils.put(key, value, saveTime);
    }

    public Object getSerializable(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getSerializable(key, null);
    }

    public Object getSerializable(@NonNull String key, Object defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        Object obj = this.mCacheMemoryUtils.get(key);
        if (obj != null) {
            return obj;
        }
        Object serializable = this.mCacheDiskUtils.getSerializable(key);
        if (serializable != null) {
            this.mCacheMemoryUtils.put(key, serializable);
            return serializable;
        }
        return defaultValue;
    }

    public long getCacheDiskSize() {
        return this.mCacheDiskUtils.getCacheSize();
    }

    public int getCacheDiskCount() {
        return this.mCacheDiskUtils.getCacheCount();
    }

    public int getCacheMemoryCount() {
        return this.mCacheMemoryUtils.getCacheCount();
    }

    public void remove(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        this.mCacheMemoryUtils.remove(key);
        this.mCacheDiskUtils.remove(key);
    }

    public void clear() {
        this.mCacheMemoryUtils.clear();
        this.mCacheDiskUtils.clear();
    }
}
