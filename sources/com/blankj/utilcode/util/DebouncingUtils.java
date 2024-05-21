package com.blankj.utilcode.util;

import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import androidx.annotation.NonNull;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: classes4.dex */
public class DebouncingUtils {
    private static final int CACHE_SIZE = 64;
    private static final long DEBOUNCING_DEFAULT_VALUE = 1000;
    private static final Map<String, Long> KEY_MILLIS_MAP = new ConcurrentHashMap(64);

    private DebouncingUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static boolean isValid(@NonNull View view) {
        if (view == null) {
            throw new NullPointerException("Argument 'view' of type View (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return isValid(view, 1000L);
    }

    public static boolean isValid(@NonNull View view, long duration) {
        if (view == null) {
            throw new NullPointerException("Argument 'view' of type View (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return isValid(String.valueOf(view.hashCode()), duration);
    }

    public static boolean isValid(@NonNull String key, long duration) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (TextUtils.isEmpty(key)) {
            throw new IllegalArgumentException("The key is null.");
        }
        if (duration < 0) {
            throw new IllegalArgumentException("The duration is less than 0.");
        }
        long curTime = SystemClock.elapsedRealtime();
        clearIfNecessary(curTime);
        Long validTime = KEY_MILLIS_MAP.get(key);
        if (validTime == null || curTime >= validTime.longValue()) {
            KEY_MILLIS_MAP.put(key, Long.valueOf(curTime + duration));
            return true;
        }
        return false;
    }

    private static void clearIfNecessary(long curTime) {
        if (KEY_MILLIS_MAP.size() < 64) {
            return;
        }
        Iterator<Map.Entry<String, Long>> it = KEY_MILLIS_MAP.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Long> entry = it.next();
            Long validTime = entry.getValue();
            if (curTime >= validTime.longValue()) {
                it.remove();
            }
        }
    }
}
