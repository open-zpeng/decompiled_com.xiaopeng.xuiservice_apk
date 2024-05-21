package com.blankj.utilcode.util;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: classes4.dex */
public final class ApiUtils {
    private static final String TAG = "ApiUtils";
    private Map<Class, BaseApi> mApiMap;
    private Map<Class, Class> mInjectApiImplMap;

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.CLASS)
    /* loaded from: classes4.dex */
    public @interface Api {
        boolean isMock() default false;
    }

    /* loaded from: classes4.dex */
    public static class BaseApi {
    }

    private ApiUtils() {
        this.mApiMap = new ConcurrentHashMap();
        this.mInjectApiImplMap = new HashMap();
        init();
    }

    private void init() {
    }

    private void registerImpl(Class implClass) {
        this.mInjectApiImplMap.put(implClass.getSuperclass(), implClass);
    }

    @Nullable
    public static <T extends BaseApi> T getApi(@NonNull Class<T> apiClass) {
        if (apiClass == null) {
            throw new NullPointerException("Argument 'apiClass' of type Class<T> (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return (T) getInstance().getApiInner(apiClass);
    }

    public static void register(@Nullable Class<? extends BaseApi> implClass) {
        if (implClass == null) {
            return;
        }
        getInstance().registerImpl(implClass);
    }

    @NonNull
    public static String toString_() {
        String tmpTrauteVar2 = getInstance().toString();
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ApiUtils.toString_() marked by @androidx.annotation.NonNull");
    }

    public String toString() {
        return "ApiUtils: " + this.mInjectApiImplMap;
    }

    private static ApiUtils getInstance() {
        return LazyHolder.INSTANCE;
    }

    private <Result> Result getApiInner(Class apiClass) {
        Result result = (Result) this.mApiMap.get(apiClass);
        if (result != null) {
            return result;
        }
        synchronized (apiClass) {
            Result result2 = (Result) this.mApiMap.get(apiClass);
            if (result2 != null) {
                return result2;
            }
            Class implClass = this.mInjectApiImplMap.get(apiClass);
            if (implClass != null) {
                try {
                    Result result3 = (Result) ((BaseApi) implClass.newInstance());
                    this.mApiMap.put(apiClass, result3);
                    return result3;
                } catch (Exception e) {
                    Log.e(TAG, "The <" + implClass + "> has no parameterless constructor.");
                    return null;
                }
            }
            Log.e(TAG, "The <" + apiClass + "> doesn't implement.");
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class LazyHolder {
        private static final ApiUtils INSTANCE = new ApiUtils();

        private LazyHolder() {
        }
    }
}
