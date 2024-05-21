package com.blankj.utilcode.util;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
/* loaded from: classes4.dex */
public final class BusUtils {
    private static final Object NULL = "nULl";
    private static final String TAG = "BusUtils";
    private final Map<String, Set<Object>> mClassName_BusesMap;
    private final Map<String, Map<String, Object>> mClassName_Tag_Arg4StickyMap;
    private final Map<String, List<String>> mClassName_TagsMap;
    private final Map<String, List<BusInfo>> mTag_BusInfoListMap;

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.CLASS)
    /* loaded from: classes4.dex */
    public @interface Bus {
        int priority() default 0;

        boolean sticky() default false;

        String tag();

        ThreadMode threadMode() default ThreadMode.POSTING;
    }

    /* loaded from: classes4.dex */
    public enum ThreadMode {
        MAIN,
        IO,
        CPU,
        CACHED,
        SINGLE,
        POSTING
    }

    private BusUtils() {
        this.mTag_BusInfoListMap = new ConcurrentHashMap();
        this.mClassName_BusesMap = new ConcurrentHashMap();
        this.mClassName_TagsMap = new ConcurrentHashMap();
        this.mClassName_Tag_Arg4StickyMap = new ConcurrentHashMap();
        init();
    }

    private void init() {
    }

    private void registerBus(String tag, String className, String funName, String paramType, String paramName, boolean sticky, String threadMode) {
        registerBus(tag, className, funName, paramType, paramName, sticky, threadMode, 0);
    }

    private void registerBus(String tag, String className, String funName, String paramType, String paramName, boolean sticky, String threadMode, int priority) {
        List<BusInfo> busInfoList;
        List<BusInfo> busInfoList2 = this.mTag_BusInfoListMap.get(tag);
        if (busInfoList2 != null) {
            busInfoList = busInfoList2;
        } else {
            List<BusInfo> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
            this.mTag_BusInfoListMap.put(tag, copyOnWriteArrayList);
            busInfoList = copyOnWriteArrayList;
        }
        busInfoList.add(new BusInfo(tag, className, funName, paramType, paramName, sticky, threadMode, priority));
    }

    public static void register(@Nullable Object bus) {
        getInstance().registerInner(bus);
    }

    public static void unregister(@Nullable Object bus) {
        getInstance().unregisterInner(bus);
    }

    public static void post(@NonNull String tag) {
        if (tag == null) {
            throw new NullPointerException("Argument 'tag' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        post(tag, NULL);
    }

    public static void post(@NonNull String tag, @NonNull Object arg) {
        if (tag == null) {
            throw new NullPointerException("Argument 'tag' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (arg == null) {
            throw new NullPointerException("Argument 'arg' of type Object (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        getInstance().postInner(tag, arg);
    }

    public static void postSticky(@NonNull String tag) {
        if (tag == null) {
            throw new NullPointerException("Argument 'tag' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        postSticky(tag, NULL);
    }

    public static void postSticky(@NonNull String tag, Object arg) {
        if (tag == null) {
            throw new NullPointerException("Argument 'tag' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        getInstance().postStickyInner(tag, arg);
    }

    public static void removeSticky(String tag) {
        getInstance().removeStickyInner(tag);
    }

    public static String toString_() {
        return getInstance().toString();
    }

    public String toString() {
        return "BusUtils: " + this.mTag_BusInfoListMap;
    }

    private static BusUtils getInstance() {
        return LazyHolder.INSTANCE;
    }

    private void registerInner(@Nullable Object bus) {
        if (bus == null) {
            return;
        }
        Class<?> aClass = bus.getClass();
        String className = aClass.getName();
        boolean isNeedRecordTags = false;
        synchronized (this.mClassName_BusesMap) {
            Set<Object> buses = this.mClassName_BusesMap.get(className);
            if (buses == null) {
                buses = new CopyOnWriteArraySet();
                this.mClassName_BusesMap.put(className, buses);
                isNeedRecordTags = true;
            }
            if (buses.contains(bus)) {
                Log.w(TAG, "The bus of <" + bus + "> already registered.");
                return;
            }
            buses.add(bus);
            if (isNeedRecordTags) {
                recordTags(aClass, className);
            }
            consumeStickyIfExist(bus);
        }
    }

    private void recordTags(Class<?> aClass, String className) {
        List<String> tags = this.mClassName_TagsMap.get(className);
        if (tags == null) {
            synchronized (this.mClassName_TagsMap) {
                List<String> tags2 = this.mClassName_TagsMap.get(className);
                if (tags2 == null) {
                    List<String> tags3 = new CopyOnWriteArrayList<>();
                    for (Map.Entry<String, List<BusInfo>> entry : this.mTag_BusInfoListMap.entrySet()) {
                        for (BusInfo busInfo : entry.getValue()) {
                            try {
                                if (Class.forName(busInfo.className).isAssignableFrom(aClass)) {
                                    tags3.add(entry.getKey());
                                    busInfo.subClassNames.add(className);
                                }
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    this.mClassName_TagsMap.put(className, tags3);
                }
            }
        }
    }

    private void consumeStickyIfExist(Object bus) {
        Map<String, Object> tagArgMap = this.mClassName_Tag_Arg4StickyMap.get(bus.getClass().getName());
        if (tagArgMap == null) {
            return;
        }
        synchronized (this.mClassName_Tag_Arg4StickyMap) {
            for (Map.Entry<String, Object> tagArgEntry : tagArgMap.entrySet()) {
                consumeSticky(bus, tagArgEntry.getKey(), tagArgEntry.getValue());
            }
        }
    }

    private void consumeSticky(Object bus, String tag, Object arg) {
        List<BusInfo> busInfoList = this.mTag_BusInfoListMap.get(tag);
        if (busInfoList == null) {
            Log.e(TAG, "The bus of tag <" + tag + "> is not exists.");
            return;
        }
        for (BusInfo busInfo : busInfoList) {
            if (busInfo.subClassNames.contains(bus.getClass().getName()) && busInfo.sticky) {
                synchronized (this.mClassName_Tag_Arg4StickyMap) {
                    Map<String, Object> tagArgMap = this.mClassName_Tag_Arg4StickyMap.get(busInfo.className);
                    if (tagArgMap != null && tagArgMap.containsKey(tag)) {
                        invokeBus(bus, arg, busInfo, true);
                    }
                }
            }
        }
    }

    private void unregisterInner(Object bus) {
        if (bus == null) {
            return;
        }
        String className = bus.getClass().getName();
        synchronized (this.mClassName_BusesMap) {
            Set<Object> buses = this.mClassName_BusesMap.get(className);
            if (buses != null && buses.contains(bus)) {
                buses.remove(bus);
                return;
            }
            Log.e(TAG, "The bus of <" + bus + "> was not registered before.");
        }
    }

    private void postInner(String tag, Object arg) {
        postInner(tag, arg, false);
    }

    private void postInner(String tag, Object arg, boolean sticky) {
        List<BusInfo> busInfoList = this.mTag_BusInfoListMap.get(tag);
        if (busInfoList == null) {
            Log.e(TAG, "The bus of tag <" + tag + "> is not exists.");
            if (this.mTag_BusInfoListMap.isEmpty()) {
                Log.e(TAG, "Please check whether the bus plugin is applied.");
                return;
            }
            return;
        }
        for (BusInfo busInfo : busInfoList) {
            invokeBus(arg, busInfo, sticky);
        }
    }

    private void invokeBus(Object arg, BusInfo busInfo, boolean sticky) {
        invokeBus(null, arg, busInfo, sticky);
    }

    private void invokeBus(Object bus, Object arg, BusInfo busInfo, boolean sticky) {
        if (busInfo.method == null) {
            Method method = getMethodByBusInfo(busInfo);
            if (method == null) {
                return;
            }
            busInfo.method = method;
        }
        invokeMethod(bus, arg, busInfo, sticky);
    }

    private Method getMethodByBusInfo(BusInfo busInfo) {
        try {
            return "".equals(busInfo.paramType) ? Class.forName(busInfo.className).getDeclaredMethod(busInfo.funName, new Class[0]) : Class.forName(busInfo.className).getDeclaredMethod(busInfo.funName, getClassName(busInfo.paramType));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private Class getClassName(String paramType) throws ClassNotFoundException {
        char c;
        switch (paramType.hashCode()) {
            case -1325958191:
                if (paramType.equals("double")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 104431:
                if (paramType.equals("int")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 3039496:
                if (paramType.equals("byte")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 3052374:
                if (paramType.equals("char")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 3327612:
                if (paramType.equals("long")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 64711720:
                if (paramType.equals("boolean")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 97526364:
                if (paramType.equals("float")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 109413500:
                if (paramType.equals("short")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return Boolean.TYPE;
            case 1:
                return Integer.TYPE;
            case 2:
                return Long.TYPE;
            case 3:
                return Short.TYPE;
            case 4:
                return Byte.TYPE;
            case 5:
                return Double.TYPE;
            case 6:
                return Float.TYPE;
            case 7:
                return Character.TYPE;
            default:
                return Class.forName(paramType);
        }
    }

    private void invokeMethod(Object arg, BusInfo busInfo, boolean sticky) {
        invokeMethod(null, arg, busInfo, sticky);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private void invokeMethod(final Object bus, final Object arg, final BusInfo busInfo, final boolean sticky) {
        char c;
        Runnable runnable = new Runnable() { // from class: com.blankj.utilcode.util.BusUtils.1
            @Override // java.lang.Runnable
            public void run() {
                BusUtils.this.realInvokeMethod(bus, arg, busInfo, sticky);
            }
        };
        String str = busInfo.threadMode;
        switch (str.hashCode()) {
            case -1848936376:
                if (str.equals("SINGLE")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 2342:
                if (str.equals("IO")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 66952:
                if (str.equals("CPU")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 2358713:
                if (str.equals("MAIN")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 1980249378:
                if (str.equals("CACHED")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        if (c == 0) {
            ThreadUtils.runOnUiThread(runnable);
        } else if (c == 1) {
            ThreadUtils.getIoPool().execute(runnable);
        } else if (c == 2) {
            ThreadUtils.getCpuPool().execute(runnable);
        } else if (c == 3) {
            ThreadUtils.getCachedPool().execute(runnable);
        } else if (c == 4) {
            ThreadUtils.getSinglePool().execute(runnable);
        } else {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void realInvokeMethod(Object bus, Object arg, BusInfo busInfo, boolean sticky) {
        Set<Object> buses = new HashSet<>();
        if (bus == null) {
            for (String subClassName : busInfo.subClassNames) {
                Set<Object> subBuses = this.mClassName_BusesMap.get(subClassName);
                if (subBuses != null && !subBuses.isEmpty()) {
                    buses.addAll(subBuses);
                }
            }
            if (buses.size() == 0) {
                if (!sticky) {
                    Log.e(TAG, "The " + busInfo + " was not registered before.");
                    return;
                }
                return;
            }
        } else {
            buses.add(bus);
        }
        invokeBuses(arg, busInfo, buses);
    }

    private void invokeBuses(Object arg, BusInfo busInfo, Set<Object> buses) {
        try {
            if (arg == NULL) {
                for (Object bus : buses) {
                    busInfo.method.invoke(bus, new Object[0]);
                }
                return;
            }
            for (Object bus2 : buses) {
                busInfo.method.invoke(bus2, arg);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
        }
    }

    private void postStickyInner(String tag, Object arg) {
        List<BusInfo> busInfoList = this.mTag_BusInfoListMap.get(tag);
        if (busInfoList == null) {
            Log.e(TAG, "The bus of tag <" + tag + "> is not exists.");
            return;
        }
        for (BusInfo busInfo : busInfoList) {
            if (!busInfo.sticky) {
                invokeBus(arg, busInfo, false);
            } else {
                synchronized (this.mClassName_Tag_Arg4StickyMap) {
                    Map<String, Object> tagArgMap = this.mClassName_Tag_Arg4StickyMap.get(busInfo.className);
                    if (tagArgMap == null) {
                        tagArgMap = new ConcurrentHashMap();
                        this.mClassName_Tag_Arg4StickyMap.put(busInfo.className, tagArgMap);
                    }
                    tagArgMap.put(tag, arg);
                }
                invokeBus(arg, busInfo, true);
            }
        }
    }

    private void removeStickyInner(String tag) {
        List<BusInfo> busInfoList = this.mTag_BusInfoListMap.get(tag);
        if (busInfoList == null) {
            Log.e(TAG, "The bus of tag <" + tag + "> is not exists.");
            return;
        }
        for (BusInfo busInfo : busInfoList) {
            if (busInfo.sticky) {
                synchronized (this.mClassName_Tag_Arg4StickyMap) {
                    Map<String, Object> tagArgMap = this.mClassName_Tag_Arg4StickyMap.get(busInfo.className);
                    if (tagArgMap != null && tagArgMap.containsKey(tag)) {
                        tagArgMap.remove(tag);
                    }
                    return;
                }
            }
        }
    }

    static void registerBus4Test(String tag, String className, String funName, String paramType, String paramName, boolean sticky, String threadMode, int priority) {
        getInstance().registerBus(tag, className, funName, paramType, paramName, sticky, threadMode, priority);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class BusInfo {
        String className;
        String funName;
        Method method;
        String paramName;
        String paramType;
        int priority;
        boolean sticky;
        List<String> subClassNames = new CopyOnWriteArrayList();
        String tag;
        String threadMode;

        BusInfo(String tag, String className, String funName, String paramType, String paramName, boolean sticky, String threadMode, int priority) {
            this.tag = tag;
            this.className = className;
            this.funName = funName;
            this.paramType = paramType;
            this.paramName = paramName;
            this.sticky = sticky;
            this.threadMode = threadMode;
            this.priority = priority;
        }

        public String toString() {
            return "BusInfo { tag : " + this.tag + ", desc: " + getDesc() + ", sticky: " + this.sticky + ", threadMode: " + this.threadMode + ", method: " + this.method + ", priority: " + this.priority + " }";
        }

        private String getDesc() {
            String str;
            StringBuilder sb = new StringBuilder();
            sb.append(this.className);
            sb.append("#");
            sb.append(this.funName);
            if ("".equals(this.paramType)) {
                str = "()";
            } else {
                str = "(" + this.paramType + " " + this.paramName + ")";
            }
            sb.append(str);
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class LazyHolder {
        private static final BusUtils INSTANCE = new BusUtils();

        private LazyHolder() {
        }
    }
}
