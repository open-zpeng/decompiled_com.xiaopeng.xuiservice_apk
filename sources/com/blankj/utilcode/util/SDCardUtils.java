package com.blankj.utilcode.util;

import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.text.format.Formatter;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes4.dex */
public final class SDCardUtils {
    private SDCardUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static boolean isSDCardEnableByEnvironment() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public static String getSDCardPathByEnvironment() {
        if (isSDCardEnableByEnvironment()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return "";
    }

    public static List<SDCardInfo> getSDCardInfo() {
        List<SDCardInfo> paths = new ArrayList<>();
        StorageManager sm = (StorageManager) Utils.getApp().getSystemService("storage");
        if (sm == null) {
            return paths;
        }
        int i = 0;
        if (Build.VERSION.SDK_INT >= 24) {
            List<StorageVolume> storageVolumes = sm.getStorageVolumes();
            try {
                Method getPathMethod = StorageVolume.class.getMethod("getPath", new Class[0]);
                for (StorageVolume storageVolume : storageVolumes) {
                    boolean isRemovable = storageVolume.isRemovable();
                    String state = storageVolume.getState();
                    paths.add(new SDCardInfo((String) getPathMethod.invoke(storageVolume, new Object[0]), state, isRemovable));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e2) {
                e2.printStackTrace();
            } catch (InvocationTargetException e3) {
                e3.printStackTrace();
            }
        } else {
            try {
                Class<?> storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
                Method getPathMethod2 = storageVolumeClazz.getMethod("getPath", new Class[0]);
                Method isRemovableMethod = storageVolumeClazz.getMethod("isRemovable", new Class[0]);
                Method getVolumeStateMethod = StorageManager.class.getMethod("getVolumeState", String.class);
                Method getVolumeListMethod = StorageManager.class.getMethod("getVolumeList", new Class[0]);
                Object result = getVolumeListMethod.invoke(sm, new Object[0]);
                int length = Array.getLength(result);
                int i2 = 0;
                while (i2 < length) {
                    Object storageVolumeElement = Array.get(result, i2);
                    String path = (String) getPathMethod2.invoke(storageVolumeElement, new Object[i]);
                    boolean isRemovable2 = ((Boolean) isRemovableMethod.invoke(storageVolumeElement, new Object[i])).booleanValue();
                    Object[] objArr = new Object[1];
                    objArr[i] = path;
                    String state2 = (String) getVolumeStateMethod.invoke(sm, objArr);
                    paths.add(new SDCardInfo(path, state2, isRemovable2));
                    i2++;
                    i = 0;
                }
            } catch (ClassNotFoundException e4) {
                e4.printStackTrace();
            } catch (IllegalAccessException e5) {
                e5.printStackTrace();
            } catch (NoSuchMethodException e6) {
                e6.printStackTrace();
            } catch (InvocationTargetException e7) {
                e7.printStackTrace();
            }
        }
        return paths;
    }

    public static List<String> getMountedSDCardPath() {
        List<String> path = new ArrayList<>();
        List<SDCardInfo> sdCardInfo = getSDCardInfo();
        if (sdCardInfo == null || sdCardInfo.isEmpty()) {
            return path;
        }
        for (SDCardInfo cardInfo : sdCardInfo) {
            String state = cardInfo.state;
            if (state != null && "mounted".equals(state.toLowerCase())) {
                path.add(cardInfo.path);
            }
        }
        return path;
    }

    public static long getExternalTotalSize() {
        return UtilsBridge.getFsTotalSize(getSDCardPathByEnvironment());
    }

    public static long getExternalAvailableSize() {
        return UtilsBridge.getFsAvailableSize(getSDCardPathByEnvironment());
    }

    public static long getInternalTotalSize() {
        return UtilsBridge.getFsTotalSize(Environment.getDataDirectory().getAbsolutePath());
    }

    public static long getInternalAvailableSize() {
        return UtilsBridge.getFsAvailableSize(Environment.getDataDirectory().getAbsolutePath());
    }

    /* loaded from: classes4.dex */
    public static class SDCardInfo {
        private long availableSize;
        private boolean isRemovable;
        private String path;
        private String state;
        private long totalSize;

        SDCardInfo(String path, String state, boolean isRemovable) {
            this.path = path;
            this.state = state;
            this.isRemovable = isRemovable;
            this.totalSize = UtilsBridge.getFsTotalSize(path);
            this.availableSize = UtilsBridge.getFsAvailableSize(path);
        }

        public String getPath() {
            return this.path;
        }

        public String getState() {
            return this.state;
        }

        public boolean isRemovable() {
            return this.isRemovable;
        }

        public long getTotalSize() {
            return this.totalSize;
        }

        public long getAvailableSize() {
            return this.availableSize;
        }

        public String toString() {
            return "SDCardInfo {path = " + this.path + ", state = " + this.state + ", isRemovable = " + this.isRemovable + ", totalSize = " + Formatter.formatFileSize(Utils.getApp(), this.totalSize) + ", availableSize = " + Formatter.formatFileSize(Utils.getApp(), this.availableSize) + '}';
        }
    }
}
