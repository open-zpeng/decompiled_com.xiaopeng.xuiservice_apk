package com.blankj.utilcode.util;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: classes4.dex */
public final class GsonUtils {
    private static final Map<String, Gson> GSONS = new ConcurrentHashMap();
    private static final String KEY_DEFAULT = "defaultGson";
    private static final String KEY_DELEGATE = "delegateGson";
    private static final String KEY_LOG_UTILS = "logUtilsGson";

    private GsonUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void setGsonDelegate(Gson delegate) {
        if (delegate == null) {
            return;
        }
        GSONS.put(KEY_DELEGATE, delegate);
    }

    public static void setGson(String key, Gson gson) {
        if (TextUtils.isEmpty(key) || gson == null) {
            return;
        }
        GSONS.put(key, gson);
    }

    public static Gson getGson(String key) {
        return GSONS.get(key);
    }

    public static Gson getGson() {
        Gson gsonDelegate = GSONS.get(KEY_DELEGATE);
        if (gsonDelegate != null) {
            return gsonDelegate;
        }
        Gson gsonDefault = GSONS.get(KEY_DEFAULT);
        if (gsonDefault == null) {
            Gson gsonDefault2 = createGson();
            GSONS.put(KEY_DEFAULT, gsonDefault2);
            return gsonDefault2;
        }
        return gsonDefault;
    }

    public static String toJson(Object object) {
        return toJson(getGson(), object);
    }

    public static String toJson(Object src, @NonNull Type typeOfSrc) {
        if (typeOfSrc == null) {
            throw new NullPointerException("Argument 'typeOfSrc' of type Type (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return toJson(getGson(), src, typeOfSrc);
    }

    public static String toJson(@NonNull Gson gson, Object object) {
        if (gson == null) {
            throw new NullPointerException("Argument 'gson' of type Gson (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return gson.toJson(object);
    }

    public static String toJson(@NonNull Gson gson, Object src, @NonNull Type typeOfSrc) {
        if (gson != null) {
            if (typeOfSrc == null) {
                throw new NullPointerException("Argument 'typeOfSrc' of type Type (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return gson.toJson(src, typeOfSrc);
        }
        throw new NullPointerException("Argument 'gson' of type Gson (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static <T> T fromJson(String json, @NonNull Class<T> type) {
        if (type == null) {
            throw new NullPointerException("Argument 'type' of type Class<T> (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return (T) fromJson(getGson(), json, (Class<Object>) type);
    }

    public static <T> T fromJson(String json, @NonNull Type type) {
        if (type == null) {
            throw new NullPointerException("Argument 'type' of type Type (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return (T) fromJson(getGson(), json, type);
    }

    public static <T> T fromJson(@NonNull Reader reader, @NonNull Class<T> type) {
        if (reader != null) {
            if (type == null) {
                throw new NullPointerException("Argument 'type' of type Class<T> (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return (T) fromJson(getGson(), reader, (Class<Object>) type);
        }
        throw new NullPointerException("Argument 'reader' of type Reader (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static <T> T fromJson(@NonNull Reader reader, @NonNull Type type) {
        if (reader != null) {
            if (type == null) {
                throw new NullPointerException("Argument 'type' of type Type (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return (T) fromJson(getGson(), reader, type);
        }
        throw new NullPointerException("Argument 'reader' of type Reader (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static <T> T fromJson(@NonNull Gson gson, String json, @NonNull Class<T> type) {
        if (gson != null) {
            if (type == null) {
                throw new NullPointerException("Argument 'type' of type Class<T> (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return (T) gson.fromJson(json, (Class<Object>) type);
        }
        throw new NullPointerException("Argument 'gson' of type Gson (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static <T> T fromJson(@NonNull Gson gson, String json, @NonNull Type type) {
        if (gson != null) {
            if (type == null) {
                throw new NullPointerException("Argument 'type' of type Type (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return (T) gson.fromJson(json, type);
        }
        throw new NullPointerException("Argument 'gson' of type Gson (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static <T> T fromJson(@NonNull Gson gson, Reader reader, @NonNull Class<T> type) {
        if (gson != null) {
            if (type == null) {
                throw new NullPointerException("Argument 'type' of type Class<T> (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return (T) gson.fromJson(reader, (Class<Object>) type);
        }
        throw new NullPointerException("Argument 'gson' of type Gson (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static <T> T fromJson(@NonNull Gson gson, Reader reader, @NonNull Type type) {
        if (gson != null) {
            if (type == null) {
                throw new NullPointerException("Argument 'type' of type Type (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return (T) gson.fromJson(reader, type);
        }
        throw new NullPointerException("Argument 'gson' of type Gson (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static Type getListType(@NonNull Type type) {
        if (type == null) {
            throw new NullPointerException("Argument 'type' of type Type (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return TypeToken.getParameterized(List.class, type).getType();
    }

    public static Type getSetType(@NonNull Type type) {
        if (type == null) {
            throw new NullPointerException("Argument 'type' of type Type (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return TypeToken.getParameterized(Set.class, type).getType();
    }

    public static Type getMapType(@NonNull Type keyType, @NonNull Type valueType) {
        if (keyType != null) {
            if (valueType == null) {
                throw new NullPointerException("Argument 'valueType' of type Type (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return TypeToken.getParameterized(Map.class, keyType, valueType).getType();
        }
        throw new NullPointerException("Argument 'keyType' of type Type (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static Type getArrayType(@NonNull Type type) {
        if (type == null) {
            throw new NullPointerException("Argument 'type' of type Type (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return TypeToken.getArray(type).getType();
    }

    public static Type getType(@NonNull Type rawType, @NonNull Type... typeArguments) {
        if (rawType != null) {
            if (typeArguments == null) {
                throw new NullPointerException("Argument 'typeArguments' of type Type[] (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return TypeToken.getParameterized(rawType, typeArguments).getType();
        }
        throw new NullPointerException("Argument 'rawType' of type Type (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Gson getGson4LogUtils() {
        Gson gson4LogUtils = GSONS.get(KEY_LOG_UTILS);
        if (gson4LogUtils == null) {
            Gson gson4LogUtils2 = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            GSONS.put(KEY_LOG_UTILS, gson4LogUtils2);
            return gson4LogUtils2;
        }
        return gson4LogUtils;
    }

    private static Gson createGson() {
        return new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
    }
}
