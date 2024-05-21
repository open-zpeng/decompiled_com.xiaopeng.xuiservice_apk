package com.blankj.utilcode.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.Log;
import androidx.annotation.NonNull;
import com.blankj.utilcode.constant.CacheConstants;
import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.json.JSONArray;
import org.json.JSONObject;
/* loaded from: classes4.dex */
public final class CacheDiskUtils implements CacheConstants {
    private static final Map<String, CacheDiskUtils> CACHE_MAP = new HashMap();
    private static final String CACHE_PREFIX = "cdu_";
    private static final int DEFAULT_MAX_COUNT = Integer.MAX_VALUE;
    private static final long DEFAULT_MAX_SIZE = Long.MAX_VALUE;
    private static final String TYPE_BITMAP = "bi_";
    private static final String TYPE_BYTE = "by_";
    private static final String TYPE_DRAWABLE = "dr_";
    private static final String TYPE_JSON_ARRAY = "ja_";
    private static final String TYPE_JSON_OBJECT = "jo_";
    private static final String TYPE_PARCELABLE = "pa_";
    private static final String TYPE_SERIALIZABLE = "se_";
    private static final String TYPE_STRING = "st_";
    private final File mCacheDir;
    private final String mCacheKey;
    private DiskCacheManager mDiskCacheManager;
    private final int mMaxCount;
    private final long mMaxSize;

    public static CacheDiskUtils getInstance() {
        return getInstance("", Long.MAX_VALUE, Integer.MAX_VALUE);
    }

    public static CacheDiskUtils getInstance(String cacheName) {
        return getInstance(cacheName, Long.MAX_VALUE, Integer.MAX_VALUE);
    }

    public static CacheDiskUtils getInstance(long maxSize, int maxCount) {
        return getInstance("", maxSize, maxCount);
    }

    public static CacheDiskUtils getInstance(String cacheName, long maxSize, int maxCount) {
        if (UtilsBridge.isSpace(cacheName)) {
            cacheName = "cacheUtils";
        }
        File file = new File(Utils.getApp().getCacheDir(), cacheName);
        return getInstance(file, maxSize, maxCount);
    }

    public static CacheDiskUtils getInstance(@NonNull File cacheDir) {
        if (cacheDir == null) {
            throw new NullPointerException("Argument 'cacheDir' of type File (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getInstance(cacheDir, Long.MAX_VALUE, Integer.MAX_VALUE);
    }

    public static CacheDiskUtils getInstance(@NonNull File cacheDir, long maxSize, int maxCount) {
        if (cacheDir == null) {
            throw new NullPointerException("Argument 'cacheDir' of type File (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        String cacheKey = cacheDir.getAbsoluteFile() + "_" + maxSize + "_" + maxCount;
        CacheDiskUtils cache = CACHE_MAP.get(cacheKey);
        if (cache == null) {
            synchronized (CacheDiskUtils.class) {
                cache = CACHE_MAP.get(cacheKey);
                if (cache == null) {
                    cache = new CacheDiskUtils(cacheKey, cacheDir, maxSize, maxCount);
                    CACHE_MAP.put(cacheKey, cache);
                }
            }
        }
        return cache;
    }

    private CacheDiskUtils(String cacheKey, File cacheDir, long maxSize, int maxCount) {
        this.mCacheKey = cacheKey;
        this.mCacheDir = cacheDir;
        this.mMaxSize = maxSize;
        this.mMaxCount = maxCount;
    }

    private DiskCacheManager getDiskCacheManager() {
        if (this.mCacheDir.exists()) {
            if (this.mDiskCacheManager == null) {
                this.mDiskCacheManager = new DiskCacheManager(this.mCacheDir, this.mMaxSize, this.mMaxCount);
            }
        } else if (this.mCacheDir.mkdirs()) {
            this.mDiskCacheManager = new DiskCacheManager(this.mCacheDir, this.mMaxSize, this.mMaxCount);
        } else {
            Log.e("CacheDiskUtils", "can't make dirs in " + this.mCacheDir.getAbsolutePath());
        }
        return this.mDiskCacheManager;
    }

    public String toString() {
        return this.mCacheKey + "@" + Integer.toHexString(hashCode());
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
        realPutBytes(TYPE_BYTE + key, value, saveTime);
    }

    private void realPutBytes(String key, byte[] value, int saveTime) {
        DiskCacheManager diskCacheManager;
        if (value == null || (diskCacheManager = getDiskCacheManager()) == null) {
            return;
        }
        if (saveTime >= 0) {
            value = DiskCacheHelper.newByteArrayWithTime(saveTime, value);
        }
        File file = diskCacheManager.getFileBeforePut(key);
        UtilsBridge.writeFileFromBytes(file, value);
        diskCacheManager.updateModify(file);
        diskCacheManager.put(file);
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
        return realGetBytes(TYPE_BYTE + key, defaultValue);
    }

    private byte[] realGetBytes(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return realGetBytes(key, null);
    }

    private byte[] realGetBytes(@NonNull String key, byte[] defaultValue) {
        File file;
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        DiskCacheManager diskCacheManager = getDiskCacheManager();
        if (diskCacheManager == null || (file = diskCacheManager.getFileIfExists(key)) == null) {
            return defaultValue;
        }
        byte[] data = UtilsBridge.readFile2Bytes(file);
        if (DiskCacheHelper.isDue(data)) {
            diskCacheManager.removeByKey(key);
            return defaultValue;
        }
        diskCacheManager.updateModify(file);
        return DiskCacheHelper.getDataWithoutDueTime(data);
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
        realPutBytes(TYPE_STRING + key, UtilsBridge.string2Bytes(value), saveTime);
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
        byte[] bytes = realGetBytes(TYPE_STRING + key);
        return bytes == null ? defaultValue : UtilsBridge.bytes2String(bytes);
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
        realPutBytes(TYPE_JSON_OBJECT + key, UtilsBridge.jsonObject2Bytes(value), saveTime);
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
        byte[] bytes = realGetBytes(TYPE_JSON_OBJECT + key);
        return bytes == null ? defaultValue : UtilsBridge.bytes2JSONObject(bytes);
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
        realPutBytes(TYPE_JSON_ARRAY + key, UtilsBridge.jsonArray2Bytes(value), saveTime);
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
        byte[] bytes = realGetBytes(TYPE_JSON_ARRAY + key);
        return bytes == null ? defaultValue : UtilsBridge.bytes2JSONArray(bytes);
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
        realPutBytes(TYPE_BITMAP + key, UtilsBridge.bitmap2Bytes(value), saveTime);
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
        byte[] bytes = realGetBytes(TYPE_BITMAP + key);
        return bytes == null ? defaultValue : UtilsBridge.bytes2Bitmap(bytes);
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
        realPutBytes(TYPE_DRAWABLE + key, UtilsBridge.drawable2Bytes(value), saveTime);
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
        byte[] bytes = realGetBytes(TYPE_DRAWABLE + key);
        return bytes == null ? defaultValue : UtilsBridge.bytes2Drawable(bytes);
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
        realPutBytes(TYPE_PARCELABLE + key, UtilsBridge.parcelable2Bytes(value), saveTime);
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
        byte[] bytes = realGetBytes(TYPE_PARCELABLE + key);
        return bytes == null ? defaultValue : (T) UtilsBridge.bytes2Parcelable(bytes, creator);
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
        realPutBytes(TYPE_SERIALIZABLE + key, UtilsBridge.serializable2Bytes(value), saveTime);
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
        byte[] bytes = realGetBytes(TYPE_SERIALIZABLE + key);
        return bytes == null ? defaultValue : UtilsBridge.bytes2Object(bytes);
    }

    public long getCacheSize() {
        DiskCacheManager diskCacheManager = getDiskCacheManager();
        if (diskCacheManager == null) {
            return 0L;
        }
        return diskCacheManager.getCacheSize();
    }

    public int getCacheCount() {
        DiskCacheManager diskCacheManager = getDiskCacheManager();
        if (diskCacheManager == null) {
            return 0;
        }
        return diskCacheManager.getCacheCount();
    }

    public boolean remove(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        DiskCacheManager diskCacheManager = getDiskCacheManager();
        if (diskCacheManager == null) {
            return true;
        }
        if (diskCacheManager.removeByKey(TYPE_BYTE + key)) {
            if (diskCacheManager.removeByKey(TYPE_STRING + key)) {
                if (diskCacheManager.removeByKey(TYPE_JSON_OBJECT + key)) {
                    if (diskCacheManager.removeByKey(TYPE_JSON_ARRAY + key)) {
                        if (diskCacheManager.removeByKey(TYPE_BITMAP + key)) {
                            if (diskCacheManager.removeByKey(TYPE_DRAWABLE + key)) {
                                if (diskCacheManager.removeByKey(TYPE_PARCELABLE + key)) {
                                    if (diskCacheManager.removeByKey(TYPE_SERIALIZABLE + key)) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean clear() {
        DiskCacheManager diskCacheManager = getDiskCacheManager();
        if (diskCacheManager == null) {
            return true;
        }
        return diskCacheManager.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class DiskCacheManager {
        private final AtomicInteger cacheCount;
        private final File cacheDir;
        private final AtomicLong cacheSize;
        private final int countLimit;
        private final Map<File, Long> lastUsageDates;
        private final Thread mThread;
        private final long sizeLimit;

        private DiskCacheManager(final File cacheDir, long sizeLimit, int countLimit) {
            this.lastUsageDates = Collections.synchronizedMap(new HashMap());
            this.cacheDir = cacheDir;
            this.sizeLimit = sizeLimit;
            this.countLimit = countLimit;
            this.cacheSize = new AtomicLong();
            this.cacheCount = new AtomicInteger();
            this.mThread = new Thread(new Runnable() { // from class: com.blankj.utilcode.util.CacheDiskUtils.DiskCacheManager.1
                @Override // java.lang.Runnable
                public void run() {
                    int size = 0;
                    int count = 0;
                    File[] cachedFiles = cacheDir.listFiles(new FilenameFilter() { // from class: com.blankj.utilcode.util.CacheDiskUtils.DiskCacheManager.1.1
                        @Override // java.io.FilenameFilter
                        public boolean accept(File dir, String name) {
                            return name.startsWith(CacheDiskUtils.CACHE_PREFIX);
                        }
                    });
                    if (cachedFiles != null) {
                        for (File cachedFile : cachedFiles) {
                            size = (int) (size + cachedFile.length());
                            count++;
                            DiskCacheManager.this.lastUsageDates.put(cachedFile, Long.valueOf(cachedFile.lastModified()));
                        }
                        DiskCacheManager.this.cacheSize.getAndAdd(size);
                        DiskCacheManager.this.cacheCount.getAndAdd(count);
                    }
                }
            });
            this.mThread.start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public long getCacheSize() {
            wait2InitOk();
            return this.cacheSize.get();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getCacheCount() {
            wait2InitOk();
            return this.cacheCount.get();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public File getFileBeforePut(String key) {
            wait2InitOk();
            File file = new File(this.cacheDir, getCacheNameByKey(key));
            if (file.exists()) {
                this.cacheCount.addAndGet(-1);
                this.cacheSize.addAndGet(-file.length());
            }
            return file;
        }

        private void wait2InitOk() {
            try {
                this.mThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public File getFileIfExists(String key) {
            File file = new File(this.cacheDir, getCacheNameByKey(key));
            if (file.exists()) {
                return file;
            }
            return null;
        }

        private String getCacheNameByKey(String key) {
            return CacheDiskUtils.CACHE_PREFIX + key.substring(0, 3) + key.substring(3).hashCode();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void put(File file) {
            this.cacheCount.addAndGet(1);
            this.cacheSize.addAndGet(file.length());
            while (true) {
                if (this.cacheCount.get() > this.countLimit || this.cacheSize.get() > this.sizeLimit) {
                    this.cacheSize.addAndGet(-removeOldest());
                    this.cacheCount.addAndGet(-1);
                } else {
                    return;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateModify(File file) {
            Long millis = Long.valueOf(System.currentTimeMillis());
            file.setLastModified(millis.longValue());
            this.lastUsageDates.put(file, millis);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean removeByKey(String key) {
            File file = getFileIfExists(key);
            if (file == null) {
                return true;
            }
            if (file.delete()) {
                this.cacheSize.addAndGet(-file.length());
                this.cacheCount.addAndGet(-1);
                this.lastUsageDates.remove(file);
                return true;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean clear() {
            File[] files = this.cacheDir.listFiles(new FilenameFilter() { // from class: com.blankj.utilcode.util.CacheDiskUtils.DiskCacheManager.2
                @Override // java.io.FilenameFilter
                public boolean accept(File dir, String name) {
                    return name.startsWith(CacheDiskUtils.CACHE_PREFIX);
                }
            });
            if (files == null || files.length <= 0) {
                return true;
            }
            boolean flag = true;
            for (File file : files) {
                if (!file.delete()) {
                    flag = false;
                } else {
                    this.cacheSize.addAndGet(-file.length());
                    this.cacheCount.addAndGet(-1);
                    this.lastUsageDates.remove(file);
                }
            }
            if (flag) {
                this.lastUsageDates.clear();
                this.cacheSize.set(0L);
                this.cacheCount.set(0);
            }
            return flag;
        }

        private long removeOldest() {
            if (this.lastUsageDates.isEmpty()) {
                return 0L;
            }
            Long oldestUsage = Long.MAX_VALUE;
            File oldestFile = null;
            Set<Map.Entry<File, Long>> entries = this.lastUsageDates.entrySet();
            synchronized (this.lastUsageDates) {
                for (Map.Entry<File, Long> entry : entries) {
                    Long lastValueUsage = entry.getValue();
                    if (lastValueUsage.longValue() < oldestUsage.longValue()) {
                        oldestUsage = lastValueUsage;
                        oldestFile = entry.getKey();
                    }
                }
            }
            if (oldestFile == null) {
                return 0L;
            }
            long fileSize = oldestFile.length();
            if (oldestFile.delete()) {
                this.lastUsageDates.remove(oldestFile);
                return fileSize;
            }
            return 0L;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class DiskCacheHelper {
        static final int TIME_INFO_LEN = 14;

        private DiskCacheHelper() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static byte[] newByteArrayWithTime(int second, byte[] data) {
            byte[] time = createDueTime(second).getBytes();
            byte[] content = new byte[time.length + data.length];
            System.arraycopy(time, 0, content, 0, time.length);
            System.arraycopy(data, 0, content, time.length, data.length);
            return content;
        }

        private static String createDueTime(int seconds) {
            return String.format(Locale.getDefault(), "_$%010d$_", Long.valueOf((System.currentTimeMillis() / 1000) + seconds));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean isDue(byte[] data) {
            long millis = getDueTime(data);
            return millis != -1 && System.currentTimeMillis() > millis;
        }

        private static long getDueTime(byte[] data) {
            if (hasTimeInfo(data)) {
                String millis = new String(copyOfRange(data, 2, 12));
                try {
                    return Long.parseLong(millis) * 1000;
                } catch (NumberFormatException e) {
                    return -1L;
                }
            }
            return -1L;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static byte[] getDataWithoutDueTime(byte[] data) {
            if (hasTimeInfo(data)) {
                return copyOfRange(data, 14, data.length);
            }
            return data;
        }

        private static byte[] copyOfRange(byte[] original, int from, int to) {
            int newLength = to - from;
            if (newLength < 0) {
                throw new IllegalArgumentException(from + " > " + to);
            }
            byte[] copy = new byte[newLength];
            System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
            return copy;
        }

        private static boolean hasTimeInfo(byte[] data) {
            return data != null && data.length >= 14 && data[0] == 95 && data[1] == 36 && data[12] == 36 && data[13] == 95;
        }
    }
}
