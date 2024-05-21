package com.blankj.utilcode.util;

import androidx.annotation.NonNull;
import java.util.Map;
import java.util.Set;
/* loaded from: classes4.dex */
public final class SPStaticUtils {
    private static SPUtils sDefaultSPUtils;

    public static void setDefaultSPUtils(SPUtils spUtils) {
        sDefaultSPUtils = spUtils;
    }

    public static void put(@NonNull String key, String value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, getDefaultSPUtils());
    }

    public static void put(@NonNull String key, String value, boolean isCommit) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, isCommit, getDefaultSPUtils());
    }

    public static String getString(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getString(key, getDefaultSPUtils());
    }

    public static String getString(@NonNull String key, String defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getString(key, defaultValue, getDefaultSPUtils());
    }

    public static void put(@NonNull String key, int value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, getDefaultSPUtils());
    }

    public static void put(@NonNull String key, int value, boolean isCommit) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, isCommit, getDefaultSPUtils());
    }

    public static int getInt(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getInt(key, getDefaultSPUtils());
    }

    public static int getInt(@NonNull String key, int defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getInt(key, defaultValue, getDefaultSPUtils());
    }

    public static void put(@NonNull String key, long value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, getDefaultSPUtils());
    }

    public static void put(@NonNull String key, long value, boolean isCommit) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, isCommit, getDefaultSPUtils());
    }

    public static long getLong(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getLong(key, getDefaultSPUtils());
    }

    public static long getLong(@NonNull String key, long defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getLong(key, defaultValue, getDefaultSPUtils());
    }

    public static void put(@NonNull String key, float value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, getDefaultSPUtils());
    }

    public static void put(@NonNull String key, float value, boolean isCommit) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, isCommit, getDefaultSPUtils());
    }

    public static float getFloat(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getFloat(key, getDefaultSPUtils());
    }

    public static float getFloat(@NonNull String key, float defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getFloat(key, defaultValue, getDefaultSPUtils());
    }

    public static void put(@NonNull String key, boolean value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, getDefaultSPUtils());
    }

    public static void put(@NonNull String key, boolean value, boolean isCommit) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, isCommit, getDefaultSPUtils());
    }

    public static boolean getBoolean(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getBoolean(key, getDefaultSPUtils());
    }

    public static boolean getBoolean(@NonNull String key, boolean defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getBoolean(key, defaultValue, getDefaultSPUtils());
    }

    public static void put(@NonNull String key, Set<String> value) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, getDefaultSPUtils());
    }

    public static void put(@NonNull String key, Set<String> value, boolean isCommit) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        put(key, value, isCommit, getDefaultSPUtils());
    }

    public static Set<String> getStringSet(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getStringSet(key, getDefaultSPUtils());
    }

    public static Set<String> getStringSet(@NonNull String key, Set<String> defaultValue) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getStringSet(key, defaultValue, getDefaultSPUtils());
    }

    public static Map<String, ?> getAll() {
        return getAll(getDefaultSPUtils());
    }

    public static boolean contains(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return contains(key, getDefaultSPUtils());
    }

    public static void remove(@NonNull String key) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        remove(key, getDefaultSPUtils());
    }

    public static void remove(@NonNull String key, boolean isCommit) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        remove(key, isCommit, getDefaultSPUtils());
    }

    public static void clear() {
        clear(getDefaultSPUtils());
    }

    public static void clear(boolean isCommit) {
        clear(isCommit, getDefaultSPUtils());
    }

    public static void put(@NonNull String key, String value, @NonNull SPUtils spUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (spUtils == null) {
            throw new NullPointerException("Argument 'spUtils' of type SPUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        spUtils.put(key, value);
    }

    public static void put(@NonNull String key, String value, boolean isCommit, @NonNull SPUtils spUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (spUtils == null) {
            throw new NullPointerException("Argument 'spUtils' of type SPUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        spUtils.put(key, value, isCommit);
    }

    public static String getString(@NonNull String key, @NonNull SPUtils spUtils) {
        if (key != null) {
            if (spUtils == null) {
                throw new NullPointerException("Argument 'spUtils' of type SPUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return spUtils.getString(key);
        }
        throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static String getString(@NonNull String key, String defaultValue, @NonNull SPUtils spUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (spUtils == null) {
            throw new NullPointerException("Argument 'spUtils' of type SPUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return spUtils.getString(key, defaultValue);
    }

    public static void put(@NonNull String key, int value, @NonNull SPUtils spUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (spUtils == null) {
            throw new NullPointerException("Argument 'spUtils' of type SPUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        spUtils.put(key, value);
    }

    public static void put(@NonNull String key, int value, boolean isCommit, @NonNull SPUtils spUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (spUtils == null) {
            throw new NullPointerException("Argument 'spUtils' of type SPUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        spUtils.put(key, value, isCommit);
    }

    public static int getInt(@NonNull String key, @NonNull SPUtils spUtils) {
        if (key != null) {
            if (spUtils == null) {
                throw new NullPointerException("Argument 'spUtils' of type SPUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return spUtils.getInt(key);
        }
        throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static int getInt(@NonNull String key, int defaultValue, @NonNull SPUtils spUtils) {
        if (key != null) {
            if (spUtils == null) {
                throw new NullPointerException("Argument 'spUtils' of type SPUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return spUtils.getInt(key, defaultValue);
        }
        throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static void put(@NonNull String key, long value, @NonNull SPUtils spUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (spUtils == null) {
            throw new NullPointerException("Argument 'spUtils' of type SPUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        spUtils.put(key, value);
    }

    public static void put(@NonNull String key, long value, boolean isCommit, @NonNull SPUtils spUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (spUtils == null) {
            throw new NullPointerException("Argument 'spUtils' of type SPUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        spUtils.put(key, value, isCommit);
    }

    public static long getLong(@NonNull String key, @NonNull SPUtils spUtils) {
        if (key != null) {
            if (spUtils == null) {
                throw new NullPointerException("Argument 'spUtils' of type SPUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return spUtils.getLong(key);
        }
        throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static long getLong(@NonNull String key, long defaultValue, @NonNull SPUtils spUtils) {
        if (key != null) {
            if (spUtils == null) {
                throw new NullPointerException("Argument 'spUtils' of type SPUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return spUtils.getLong(key, defaultValue);
        }
        throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static void put(@NonNull String key, float value, @NonNull SPUtils spUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (spUtils == null) {
            throw new NullPointerException("Argument 'spUtils' of type SPUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        spUtils.put(key, value);
    }

    public static void put(@NonNull String key, float value, boolean isCommit, @NonNull SPUtils spUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (spUtils == null) {
            throw new NullPointerException("Argument 'spUtils' of type SPUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        spUtils.put(key, value, isCommit);
    }

    public static float getFloat(@NonNull String key, @NonNull SPUtils spUtils) {
        if (key != null) {
            if (spUtils == null) {
                throw new NullPointerException("Argument 'spUtils' of type SPUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return spUtils.getFloat(key);
        }
        throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static float getFloat(@NonNull String key, float defaultValue, @NonNull SPUtils spUtils) {
        if (key != null) {
            if (spUtils == null) {
                throw new NullPointerException("Argument 'spUtils' of type SPUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return spUtils.getFloat(key, defaultValue);
        }
        throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static void put(@NonNull String key, boolean value, @NonNull SPUtils spUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (spUtils == null) {
            throw new NullPointerException("Argument 'spUtils' of type SPUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        spUtils.put(key, value);
    }

    public static void put(@NonNull String key, boolean value, boolean isCommit, @NonNull SPUtils spUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (spUtils == null) {
            throw new NullPointerException("Argument 'spUtils' of type SPUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        spUtils.put(key, value, isCommit);
    }

    public static boolean getBoolean(@NonNull String key, @NonNull SPUtils spUtils) {
        if (key != null) {
            if (spUtils == null) {
                throw new NullPointerException("Argument 'spUtils' of type SPUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return spUtils.getBoolean(key);
        }
        throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static boolean getBoolean(@NonNull String key, boolean defaultValue, @NonNull SPUtils spUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (spUtils == null) {
            throw new NullPointerException("Argument 'spUtils' of type SPUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return spUtils.getBoolean(key, defaultValue);
    }

    public static void put(@NonNull String key, Set<String> value, @NonNull SPUtils spUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (spUtils == null) {
            throw new NullPointerException("Argument 'spUtils' of type SPUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        spUtils.put(key, value);
    }

    public static void put(@NonNull String key, Set<String> value, boolean isCommit, @NonNull SPUtils spUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (spUtils == null) {
            throw new NullPointerException("Argument 'spUtils' of type SPUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        spUtils.put(key, value, isCommit);
    }

    public static Set<String> getStringSet(@NonNull String key, @NonNull SPUtils spUtils) {
        if (key != null) {
            if (spUtils == null) {
                throw new NullPointerException("Argument 'spUtils' of type SPUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return spUtils.getStringSet(key);
        }
        throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static Set<String> getStringSet(@NonNull String key, Set<String> defaultValue, @NonNull SPUtils spUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (spUtils == null) {
            throw new NullPointerException("Argument 'spUtils' of type SPUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return spUtils.getStringSet(key, defaultValue);
    }

    public static Map<String, ?> getAll(@NonNull SPUtils spUtils) {
        if (spUtils == null) {
            throw new NullPointerException("Argument 'spUtils' of type SPUtils (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return spUtils.getAll();
    }

    public static boolean contains(@NonNull String key, @NonNull SPUtils spUtils) {
        if (key != null) {
            if (spUtils == null) {
                throw new NullPointerException("Argument 'spUtils' of type SPUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return spUtils.contains(key);
        }
        throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static void remove(@NonNull String key, @NonNull SPUtils spUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (spUtils == null) {
            throw new NullPointerException("Argument 'spUtils' of type SPUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        spUtils.remove(key);
    }

    public static void remove(@NonNull String key, boolean isCommit, @NonNull SPUtils spUtils) {
        if (key == null) {
            throw new NullPointerException("Argument 'key' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (spUtils == null) {
            throw new NullPointerException("Argument 'spUtils' of type SPUtils (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        spUtils.remove(key, isCommit);
    }

    public static void clear(@NonNull SPUtils spUtils) {
        if (spUtils == null) {
            throw new NullPointerException("Argument 'spUtils' of type SPUtils (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        spUtils.clear();
    }

    public static void clear(boolean isCommit, @NonNull SPUtils spUtils) {
        if (spUtils == null) {
            throw new NullPointerException("Argument 'spUtils' of type SPUtils (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        spUtils.clear(isCommit);
    }

    private static SPUtils getDefaultSPUtils() {
        SPUtils sPUtils = sDefaultSPUtils;
        return sPUtils != null ? sPUtils : SPUtils.getInstance();
    }
}
