package com.xiaopeng.xuiservice.soundresource;

import android.app.ActivityThread;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseArray;
import com.xiaopeng.xuimanager.soundresource.data.BootSoundResource;
import com.xiaopeng.xuimanager.soundresource.data.SoundEffectResource;
import com.xiaopeng.xuimanager.soundresource.data.SoundEffectTheme;
import com.xiaopeng.xuimanager.themeoperation.AbilityEffect;
import com.xiaopeng.xuimanager.themeoperation.ThemeOperationData;
import com.xiaopeng.xuimanager.themeoperation.ThemeOperationListener;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.innerutils.LocaleStrings;
import com.xiaopeng.xuiservice.soundresource.SoundResourceLoader;
import com.xiaopeng.xuiservice.themeoperation.ThemeOperationService;
import com.xiaopeng.xuiservice.utils.FileUtils;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class SoundResourceLoader {
    private static final String GLOBAL_CONFIG_FILE = "config.txt";
    private static final String KEY_GROUP = "group";
    private static final String KEY_ID_MAP = "idMap";
    private static final String KEY_NAME_MAP = "nameMap";
    private static final String KEY_PREVIEW_NAME = "previewname";
    private static final String KEY_VERSION = "version";
    private static final String PROPERTY_SOUND_BOOT_INDEX = "persist.xiaopeng.soundboot.index";
    private static final String PROPERTY_SOUND_BOOT_SWITCH = "persist.xiaopeng.soundboot.switch";
    private static final String PROPERTY_SOUND_THEME_INDEX = "persist.xiaopeng.soundtheme.index";
    private static final String PROPERTY_SOUND_THEME_PATH = "persist.xiaopeng.soundtheme.path";
    private static final String RESOURCE_DESCRIPTION_FILE = "desc.txt";
    private static final String SOUND_THEME_DAY_PIC = "/day.png";
    private static final String SOUND_THEME_NIGHT_PIC = "/night.png";
    private final String KEY_SOUND_MODE;
    private boolean isPrivateSoundMode;
    private int mBootSoundGroupIndex;
    private int mBootSoundOnOff;
    private int mBootSoundResourceIdx;
    private Context mContext;
    private int mLastBootSoundResourceIdx;
    private ResourceListener mListener;
    private int mSoundThemeIndex;
    private ThemeOperationListener themeOperationListener;
    ThemeOperationService themeOperationSvc;
    private static final String TAG = SoundResourceLoader.class.getSimpleName();
    private static final ConcurrentHashMap<String, Integer> themeResTypeMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, String> nameMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Integer, SoundThemeWrapper> themeMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Integer> bootSoundOperationResMap = new ConcurrentHashMap<>();
    private static volatile String mPendingBootSoundOperationRedId = null;
    private static final HashMap<Integer, SparseArray<BootSoundResource>> mBootSoundResourceGroup = new HashMap<>();
    private static HashMap<String, Integer> bootSoundIdMap = new HashMap<>();
    private static HashMap<String, String> bootSoundNameMap = new HashMap<>();
    private static boolean hasAmp = XUIConfig.hasFeature(XUIConfig.PROPERTY_AMP);
    private static final String DEFAULT_THEME_RSC_PATH = "/system/media/audio/sndresource/theme";
    private static String mSoundThemeResourcePath = DEFAULT_THEME_RSC_PATH;
    private static final String DEFAULT_BOOT_RSC_PATH = "/system/media/audio/sndresource/boot";
    private static String mSoundBootResourcePath = DEFAULT_BOOT_RSC_PATH;

    /* loaded from: classes5.dex */
    public interface ResourceListener {
        void onResourceEvent(int i, int i2);
    }

    /* loaded from: classes5.dex */
    public static class SoundThemeWrapper {
        public Map<Integer, SoundEffectResource> playRscMap;
        public Map<Integer, SoundEffectResource> previewRscMap;
        public SoundEffectTheme theme = new SoundEffectTheme();
        public int themeId;
        public String themeRscPath;
    }

    /* synthetic */ SoundResourceLoader(AnonymousClass1 x0) {
        this();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xiaopeng.xuiservice.soundresource.SoundResourceLoader$1  reason: invalid class name */
    /* loaded from: classes5.dex */
    public class AnonymousClass1 implements ThemeOperationListener {
        AnonymousClass1() {
        }

        public void onThemeStatus(String event, String extra, List<ThemeOperationData> themeList) {
            String str = SoundResourceLoader.TAG;
            LogUtil.i(str, "onThemeStatus,event=" + event);
            if (((event.hashCode() == 1502945361 && event.equals("theme_selected")) ? (char) 0 : (char) 65535) == 0) {
                ThemeOperationData data = themeList.get(0);
                if ("default".equals(data.getInnerName())) {
                    String unused = SoundResourceLoader.mSoundThemeResourcePath = SoundResourceLoader.DEFAULT_THEME_RSC_PATH;
                    String unused2 = SoundResourceLoader.mSoundBootResourcePath = SoundResourceLoader.DEFAULT_BOOT_RSC_PATH;
                } else {
                    String unused3 = SoundResourceLoader.mSoundThemeResourcePath = data.getThemePath() + "/sndtheme";
                    String unused4 = SoundResourceLoader.mSoundBootResourcePath = data.getThemePath() + "/bootsound";
                }
                String str2 = SoundResourceLoader.TAG;
                LogUtil.i(str2, "onThemeStatus,data=" + data + ",sound theme path=" + SoundResourceLoader.mSoundThemeResourcePath + ",boot sound path=" + SoundResourceLoader.mSoundBootResourcePath);
                XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.soundresource.-$$Lambda$SoundResourceLoader$1$qZkSebykJlqu0J6o4wtrbg5LdP8
                    @Override // java.lang.Runnable
                    public final void run() {
                        SoundResourceLoader.AnonymousClass1.this.lambda$onThemeStatus$0$SoundResourceLoader$1();
                    }
                });
            }
        }

        public /* synthetic */ void lambda$onThemeStatus$0$SoundResourceLoader$1() {
            SoundResourceLoader.loadThemeRscFromDisk(SoundResourceLoader.mSoundThemeResourcePath);
            SoundResourceLoader.loadBootRscFromDisk(SoundResourceLoader.mSoundBootResourcePath);
            if (SoundResourceLoader.this.mListener != null) {
                LogUtil.i(SoundResourceLoader.TAG, "resource change...");
                SoundResourceLoader.this.mListener.onResourceEvent(0, 1000);
            }
        }
    }

    /* loaded from: classes5.dex */
    private static class SoundResourceLoaderHolder {
        private static final SoundResourceLoader instance = new SoundResourceLoader(null);

        private SoundResourceLoaderHolder() {
        }
    }

    private SoundResourceLoader() {
        this.mSoundThemeIndex = -1;
        this.mBootSoundGroupIndex = -1;
        this.mBootSoundResourceIdx = -1;
        this.mLastBootSoundResourceIdx = -1;
        this.mBootSoundOnOff = -1;
        this.isPrivateSoundMode = false;
        this.KEY_SOUND_MODE = "XpMainDriverMode";
        this.mListener = null;
        this.themeOperationListener = new AnonymousClass1();
        this.themeOperationSvc = null;
        this.mContext = ActivityThread.currentActivityThread().getApplication();
        if (-1 == this.mSoundThemeIndex) {
            this.mSoundThemeIndex = SystemProperties.getInt(PROPERTY_SOUND_THEME_INDEX, 1);
        }
        this.themeOperationSvc = ThemeOperationService.getInstance();
        this.themeOperationSvc.addListener(this.themeOperationListener);
        try {
            ThemeOperationData data = this.themeOperationSvc.getCurrentTheme();
            String str = TAG;
            LogUtil.i(str, "get current theme operation data=" + data + ",path=" + data.getThemePath());
            if (!"default".equals(data.getInnerName())) {
                mSoundThemeResourcePath = data.getThemePath() + "/sndtheme";
                mSoundBootResourcePath = data.getThemePath() + "/bootsound";
            }
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.w(str2, "get themeoperation data e=" + e);
        }
        registerObserver();
    }

    public static SoundResourceLoader getInstance() {
        return SoundResourceLoaderHolder.instance;
    }

    public Map<Integer, SoundThemeWrapper> getThemeMap() {
        return themeMap;
    }

    public SoundEffectTheme getActiveSoundTheme() {
        SoundThemeWrapper themeWrapper = themeMap.get(Integer.valueOf(this.mSoundThemeIndex));
        if (themeWrapper == null) {
            return null;
        }
        return themeWrapper.theme;
    }

    public void loadRsc() {
        loadThemeRscFromDisk(mSoundThemeResourcePath);
        loadBootRscFromDisk(mSoundBootResourcePath);
        if (this.mListener != null) {
            LogUtil.i(TAG, "loadRsc done");
            this.mListener.onResourceEvent(0, 1000);
        }
    }

    private static void loadThemeGlobalConfig(String path) {
        String content = FileUtils.readFromFile(path);
        if (content == null || "".equals(content)) {
            String str = TAG;
            LogUtil.w(str, "no config on:" + path);
            return;
        }
        try {
            JSONObject obj = new JSONObject(content);
            int version = obj.optInt(KEY_VERSION, 0);
            JSONObject previewObj = obj.optJSONObject(KEY_PREVIEW_NAME);
            Iterator<String> it = previewObj.keys();
            while (it.hasNext()) {
                String key = it.next();
                String[] values = previewObj.optString(key).split(",");
                nameMap.put(key, values[0]);
                themeResTypeMap.put(key, Integer.valueOf(Integer.parseInt(values[1])));
            }
            String str2 = TAG;
            LogUtil.d(str2, "loadThemeGlobalConfig,ver=" + version + ",nameMap=" + nameMap.toString() + ",themeResTypeMap=" + themeResTypeMap);
        } catch (Exception e) {
            String str3 = TAG;
            LogUtil.w(str3, "loadThemeGlobalConfig e=" + e);
        }
    }

    public static List<SoundEffectTheme> loadThemeRscFromDisk(String path) {
        SoundThemeWrapper wrapper;
        List<SoundEffectTheme> themeList = null;
        loadThemeGlobalConfig(path + "/" + GLOBAL_CONFIG_FILE);
        try {
            File mfile = new File(path);
            if (mfile.isDirectory()) {
                File[] files = mfile.listFiles();
                if (files == null) {
                    LogUtil.e(TAG, "empty folder");
                    return null;
                }
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory() && (wrapper = loadSoundTheme(files[i].getAbsolutePath())) != null) {
                        themeMap.put(Integer.valueOf(wrapper.themeId), wrapper);
                        if (themeList == null) {
                            themeList = new ArrayList<>();
                        }
                        themeList.add(wrapper.theme);
                    }
                }
                String str = TAG;
                LogUtil.d(str, "load theme size=" + themeMap.size());
            } else {
                LogUtil.w(TAG, "loadThemeRscFromDisk, not folder");
            }
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.e(str2, "loadThemeRscFromDisk fail e=" + Log.getStackTraceString(e).replaceAll("[\\t\\n\\r]", "#"));
        }
        return themeList;
    }

    private static String getResourceName(String nameId) {
        String name = LocaleStrings.getInstance().getString(nameId);
        return name == null ? "name" : name;
    }

    private static SoundThemeWrapper loadSoundTheme(String path) {
        String str;
        String content = FileUtils.readFromFile(path + "/" + RESOURCE_DESCRIPTION_FILE);
        SoundThemeWrapper themeWrapper = new SoundThemeWrapper();
        themeWrapper.themeRscPath = path;
        try {
            JSONObject obj = new JSONObject(content);
            themeWrapper.themeId = obj.optInt("id", -1);
            themeWrapper.theme.setThemeId(themeWrapper.themeId);
            String nameId = obj.optString("nameId", "name");
            themeWrapper.theme.setFriendlyName(getResourceName(nameId));
            themeWrapper.theme.setSubName(getResourceName(nameId + "_sub"));
        } catch (Exception e) {
            LogUtil.w(TAG, "loadSoundTheme e=" + e);
        }
        int i = 0;
        String[] picPath = {path + SOUND_THEME_DAY_PIC, path + SOUND_THEME_NIGHT_PIC};
        boolean validPicPath = true;
        int length = picPath.length;
        while (true) {
            if (i >= length) {
                break;
            }
            String s = picPath[i];
            File file = new File(s);
            if (file.exists()) {
                i++;
            } else {
                validPicPath = false;
                break;
            }
        }
        if (validPicPath) {
            themeWrapper.theme.setPlaybillPath(picPath);
        }
        themeWrapper.playRscMap = loadSoundEffectResource(path + "/play");
        themeWrapper.previewRscMap = loadSoundEffectResource(path + "/preview");
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("loadSoundTheme,id=");
        sb.append(themeWrapper.themeId);
        sb.append(",themeRscPath=");
        sb.append(themeWrapper.themeRscPath);
        sb.append(",theme=");
        sb.append(themeWrapper.theme);
        sb.append(",play rsc size=");
        sb.append(themeWrapper.playRscMap.size());
        sb.append(",play sample=");
        sb.append(themeWrapper.playRscMap.get(6));
        if (themeWrapper.previewRscMap != null) {
            str = ",preview size=" + themeWrapper.previewRscMap.size() + "preview sample=" + themeWrapper.previewRscMap.get(6);
        } else {
            str = "";
        }
        sb.append(str);
        LogUtil.i(str2, sb.toString());
        return themeWrapper;
    }

    private static Map<Integer, SoundEffectResource> loadSoundEffectResource(String path) {
        int resType;
        Map<Integer, SoundEffectResource> resMap = new HashMap<>();
        File file = new File(path);
        if (!file.exists()) {
            String str = TAG;
            LogUtil.i(str, "no resource on:" + path);
            return null;
        }
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                String folderName = files[i].getName();
                SoundEffectResource resource = new SoundEffectResource();
                resource.setResId(i + 1);
                Integer value = themeResTypeMap.get(folderName);
                if (value != null) {
                    resType = value.intValue();
                } else {
                    resType = calculateStringValue(folderName);
                    themeResTypeMap.put(folderName, Integer.valueOf(resType));
                }
                resource.setResourceType(resType);
                resource.setFriendlyName(nameMap.get(folderName));
                File[] resFile = files[i].listFiles();
                String[] resPath = new String[resFile.length];
                for (int k = 0; k < resPath.length; k++) {
                    resPath[k] = resFile[k].getAbsolutePath();
                }
                resource.setResPath(resPath);
                resMap.put(Integer.valueOf(resType), resource);
                String str2 = TAG;
                LogUtil.d(str2, "loadSoundEffectResource,find " + resource + ",file count=" + resPath.length);
            }
        }
        return resMap;
    }

    private static int calculateStringValue(String str) {
        byte[] bytes = str.getBytes();
        int sum = 0;
        for (byte b : bytes) {
            sum += b;
        }
        return sum + 10000;
    }

    public static List<BootSoundResource> loadBootRscFromDisk(String path) {
        loadBootGlobalConfig(path + "/" + GLOBAL_CONFIG_FILE);
        List<BootSoundResource> resList = null;
        try {
            File mfile = new File(path);
            if (mfile.isDirectory()) {
                File[] files = mfile.listFiles();
                if (files == null) {
                    LogUtil.e(TAG, "empty folder");
                    return null;
                }
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        resList = loadBootSoundFiles(files[i]);
                    }
                }
                String str = TAG;
                LogUtil.d(str, "loadBootRscFromDisk,group size=" + mBootSoundResourceGroup.size());
            } else {
                LogUtil.w(TAG, "loadBootRscFromDisk, not folder");
            }
        } catch (Exception e) {
            String str2 = TAG;
            LogUtil.e(str2, "loadBootRscFromDisk fail e=" + e);
        }
        return resList;
    }

    private static void loadBootGlobalConfig(String path) {
        String content = FileUtils.readFromFile(path);
        if (content == null) {
            String str = TAG;
            LogUtil.d(str, "loadBootGlobalConfig,no data on:" + path);
            return;
        }
        try {
            JSONObject obj = new JSONObject(content);
            int version = obj.optInt(KEY_VERSION, 0);
            JSONObject map = obj.optJSONObject(KEY_ID_MAP);
            Iterator<String> it = map.keys();
            while (it.hasNext()) {
                String key = it.next();
                bootSoundIdMap.put(key, Integer.valueOf(map.optInt(key, calculateStringValue(key))));
            }
            JSONObject map2 = obj.optJSONObject(KEY_NAME_MAP);
            Iterator<String> it2 = map2.keys();
            while (it2.hasNext()) {
                String key2 = it2.next();
                bootSoundNameMap.put(key2, map2.optString(key2, "XPeng Boot Sound"));
            }
            String str2 = TAG;
            LogUtil.d(str2, "loadBootGlobalConfig,ver=" + version + ",bootSoundIdMap=" + bootSoundIdMap + ",bootSoundNameMap=" + bootSoundNameMap);
        } catch (Exception e) {
            String str3 = TAG;
            LogUtil.w(str3, "loadBootGlobalConfig e=" + e);
        }
    }

    private static List<BootSoundResource> loadBootSoundFiles(File fileIn) {
        File[] files;
        int i;
        File[] files2 = fileIn.listFiles();
        if (files2 == null) {
            LogUtil.w(TAG, "loadBootSoundFiles, file is null");
            return null;
        }
        String absolutePath = fileIn.getAbsolutePath();
        LogUtil.d(TAG, "loadBootSoundFiles from " + absolutePath);
        String content = FileUtils.readFromFile(absolutePath + "/" + RESOURCE_DESCRIPTION_FILE);
        int groupId = -1;
        JSONObject selfNameMap = null;
        try {
            JSONObject obj = new JSONObject(content);
            groupId = obj.optInt(KEY_GROUP, -1);
            selfNameMap = obj.optJSONObject(KEY_NAME_MAP);
        } catch (Exception e) {
            LogUtil.w(TAG, "loadBootSoundFiles e=" + e);
        }
        if (groupId < 1) {
            LogUtil.w(TAG, "loadBootSoundFiles,invalid group id:" + groupId);
            return null;
        }
        SparseArray<BootSoundResource> group = mBootSoundResourceGroup.get(Integer.valueOf(groupId));
        if (group == null) {
            group = new SparseArray<>();
            mBootSoundResourceGroup.put(Integer.valueOf(groupId), group);
        }
        int length = files2.length;
        int i2 = 0;
        List<BootSoundResource> bootSoundResourceList = null;
        int i3 = 0;
        while (i3 < length) {
            File file = files2[i3];
            String fileName = file.getName();
            if (RESOURCE_DESCRIPTION_FILE.equals(fileName)) {
                files = files2;
                i = i2;
            } else {
                BootSoundResource resource = new BootSoundResource();
                String prefix = fileName.substring(i2, fileName.length() - 4);
                Integer obj2 = bootSoundIdMap.get(prefix);
                if (obj2 != null) {
                    resource.setResId(obj2.intValue());
                } else {
                    resource.setResId(-1);
                }
                if (selfNameMap != null) {
                    resource.setFriendlyName(selfNameMap.optString(prefix));
                } else {
                    resource.setFriendlyName(bootSoundNameMap.get(prefix));
                }
                i = 0;
                String[] path = {file.getAbsolutePath()};
                resource.setResPath(path);
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                files = files2;
                sb.append("loadBootSoundFiles,find :");
                sb.append(resource);
                LogUtil.i(str, sb.toString());
                group.put(resource.getResId(), resource);
                if (bootSoundResourceList == null) {
                    bootSoundResourceList = new ArrayList<>();
                }
                bootSoundResourceList.add(resource);
            }
            i3++;
            i2 = i;
            files2 = files;
        }
        LogUtil.i(TAG, "loadBootSoundFiles,id=" + groupId);
        return bootSoundResourceList;
    }

    public int getActiveSoundThemeIndex() {
        return this.mSoundThemeIndex;
    }

    public int setActiveSoundThemIndex(int index) {
        if (themeMap.get(Integer.valueOf(index)) == null) {
            String str = TAG;
            LogUtil.w(str, "setActiveSoundTheme,invalid theme id:" + index);
            return -1;
        }
        this.mSoundThemeIndex = index;
        SystemProperties.set(PROPERTY_SOUND_THEME_INDEX, String.valueOf(this.mSoundThemeIndex));
        SystemProperties.set(PROPERTY_SOUND_THEME_PATH, themeMap.get(Integer.valueOf(index)).themeRscPath);
        String str2 = TAG;
        LogUtil.i(str2, "setActiveSoundTheme:" + index);
        return 0;
    }

    public SoundEffectTheme[] getAvailableThemes() {
        int size = themeMap.size();
        if (size < 1) {
            LogUtil.w(TAG, "getAvailableThemes, no available theme");
            return null;
        }
        SoundEffectTheme[] themes = new SoundEffectTheme[size];
        int i = 0;
        for (Map.Entry<Integer, SoundThemeWrapper> entry : themeMap.entrySet()) {
            themes[i] = entry.getValue().theme;
            i++;
        }
        return themes;
    }

    public SoundEffectResource[] getSoundEffectPlayResource(int themeId) {
        SoundThemeWrapper themeWrapper = themeMap.get(Integer.valueOf(themeId));
        if (themeWrapper == null || themeWrapper.playRscMap == null) {
            String str = TAG;
            LogUtil.w(str, "getSoundEffectPlayResource,no rsc for theme:" + themeId);
            return null;
        }
        Set<Map.Entry<Integer, SoundEffectResource>> sets = themeWrapper.playRscMap.entrySet();
        SoundEffectResource[] resources = new SoundEffectResource[sets.size()];
        int i = 0;
        for (Map.Entry<Integer, SoundEffectResource> entry : sets) {
            resources[i] = entry.getValue();
            i++;
        }
        return resources;
    }

    public SoundEffectResource[] getSoundEffectPreviewResource(int themeId) {
        SoundThemeWrapper themeWrapper = themeMap.get(Integer.valueOf(themeId));
        if (themeWrapper == null || themeWrapper.previewRscMap == null) {
            String str = TAG;
            LogUtil.w(str, "getSoundEffectPreviewResource,no rsc for theme:" + themeId);
            return null;
        }
        Set<Map.Entry<Integer, SoundEffectResource>> sets = themeWrapper.previewRscMap.entrySet();
        SoundEffectResource[] resources = new SoundEffectResource[sets.size()];
        int i = 0;
        for (Map.Entry<Integer, SoundEffectResource> entry : sets) {
            resources[i] = entry.getValue();
            i++;
        }
        return resources;
    }

    public SoundEffectResource getActiveSoundEffectResource(int effectType) {
        SoundThemeWrapper themeWrapper = themeMap.get(Integer.valueOf(this.mSoundThemeIndex));
        if (themeWrapper == null || themeWrapper.playRscMap == null) {
            String str = TAG;
            LogUtil.w(str, "getActiveSoundEffectResource,no rsc for theme:" + this.mSoundThemeIndex);
            return null;
        }
        return themeWrapper.playRscMap.get(Integer.valueOf(effectType));
    }

    public int setBootSoundOnOff(boolean flag) {
        this.mBootSoundOnOff = flag ? 1 : 0;
        SystemProperties.set(PROPERTY_SOUND_BOOT_SWITCH, String.valueOf(this.mBootSoundOnOff));
        return 0;
    }

    public int getBootSoundOnOff() {
        if (-1 == this.mBootSoundOnOff) {
            this.mBootSoundOnOff = SystemProperties.getInt(PROPERTY_SOUND_BOOT_SWITCH, 0);
        }
        return this.mBootSoundOnOff;
    }

    public BootSoundResource[] getBootSoundResource() {
        SparseArray<BootSoundResource> group = getCurrentBootSoundGroup();
        if (group == null) {
            LogUtil.w(TAG, "getBootSoundResource,group data is null");
            return null;
        }
        BootSoundResource[] resources = new BootSoundResource[group.size()];
        for (int i = 0; i < resources.length; i++) {
            resources[i] = group.valueAt(i);
        }
        return resources;
    }

    public int setBootSoundResource(int resourceId) {
        LogUtil.i(TAG, "##setBootSoundResource,id=" + resourceId + ",current id=" + this.mBootSoundResourceIdx);
        if (resourceId == this.mBootSoundResourceIdx) {
            return 0;
        }
        SparseArray<BootSoundResource> group = getCurrentBootSoundGroup();
        if (group == null) {
            LogUtil.w(TAG, "setBootSoundResource, no group found");
            return -1;
        }
        if (group.get(resourceId) == null) {
            LogUtil.w(TAG, "setBootSoundResource,resourceId not valid:" + resourceId);
            resourceId = 1;
        }
        this.mLastBootSoundResourceIdx = this.mBootSoundResourceIdx;
        this.mBootSoundResourceIdx = resourceId;
        String saveValue = resourceId + "#" + this.mLastBootSoundResourceIdx;
        SystemProperties.set(PROPERTY_SOUND_BOOT_INDEX, saveValue);
        LogUtil.d(TAG, "setBootSoundResource, saveValue=" + saveValue);
        this.mListener.onResourceEvent(resourceId, 100);
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int setBootSoundResourceByOperationId(String operationId) {
        Integer resId = bootSoundOperationResMap.get(operationId);
        if (resId != null) {
            mPendingBootSoundOperationRedId = null;
            setBootSoundResource(resId.intValue());
            return 0;
        }
        mPendingBootSoundOperationRedId = operationId;
        String str = TAG;
        LogUtil.w(str, "setBootSoundResourceByOperationId,no valid res for operation id:" + operationId);
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int tryActiveBootSoundResourceFromOperation() {
        if (mPendingBootSoundOperationRedId != null) {
            return setBootSoundResourceByOperationId(mPendingBootSoundOperationRedId);
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int setBootSoundResourceOperationIdMap(String operationId, int resId) {
        bootSoundOperationResMap.put(operationId, Integer.valueOf(resId));
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int restoreToLastBootSound(String operationId) {
        String str = TAG;
        LogUtil.i(str, "restoreToLastBootSound,res id:" + this.mLastBootSoundResourceIdx);
        Integer resId = bootSoundOperationResMap.remove(operationId);
        if (resId == null) {
            String str2 = TAG;
            LogUtil.w(str2, "restoreToLastBootSound,null boot sound id for op id:" + operationId);
            return -1;
        } else if (resId.intValue() == this.mBootSoundResourceIdx) {
            setBootSoundResource(this.mLastBootSoundResourceIdx);
            return 0;
        } else if (resId.intValue() == this.mLastBootSoundResourceIdx) {
            this.mLastBootSoundResourceIdx = this.mBootSoundResourceIdx;
            return 0;
        } else {
            return 0;
        }
    }

    public BootSoundResource getActiveBootSoundResource() {
        SparseArray<BootSoundResource> group = getCurrentBootSoundGroup();
        if (group == null) {
            LogUtil.w(TAG, "getActiveBootSoundResource,group data is null");
            return null;
        }
        if (-1 == this.mBootSoundResourceIdx) {
            String val = SystemProperties.get(PROPERTY_SOUND_BOOT_INDEX);
            String str = TAG;
            LogUtil.d(str, "getActiveBootSoundResource,initial=" + val);
            if (val == null) {
                this.mBootSoundResourceIdx = 1;
            } else {
                String[] ids = val.split("#", 0);
                this.mBootSoundResourceIdx = Integer.parseInt(ids[0]);
                if (ids.length > 1) {
                    this.mLastBootSoundResourceIdx = Integer.parseInt(ids[1]);
                }
            }
            if (-1 == this.mBootSoundResourceIdx) {
                this.mBootSoundResourceIdx = 1;
            }
        }
        BootSoundResource resource = group.get(this.mBootSoundResourceIdx);
        return resource;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getLastBootSoundIdx() {
        return this.mLastBootSoundResourceIdx;
    }

    public void setResourceListener(ResourceListener listener) {
        this.mListener = listener;
    }

    public void unloadEffect(List<AbilityEffect> effectList) {
        if (effectList == null || effectList.isEmpty()) {
            return;
        }
        for (final AbilityEffect effect : effectList) {
            if ("sndtheme".equals(effect.getEffectAbilityType())) {
                themeMap.values().removeIf(new Predicate() { // from class: com.xiaopeng.xuiservice.soundresource.-$$Lambda$SoundResourceLoader$sqnqty671neHpnzRxhzqJrRyw-c
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return SoundResourceLoader.lambda$unloadEffect$0(effect, (SoundResourceLoader.SoundThemeWrapper) obj);
                    }
                });
            } else if ("bootsound".equals(effect.getEffectAbilityType())) {
                for (SparseArray<BootSoundResource> group : mBootSoundResourceGroup.values()) {
                    group.remove(Integer.parseInt(effect.getEffectId()));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$unloadEffect$0(AbilityEffect effect, SoundThemeWrapper soundThemeWrapper) {
        return soundThemeWrapper.themeId == Integer.parseInt(effect.getEffectId());
    }

    private void checkBootSoundGroupIndex() {
        int groupSize = mBootSoundResourceGroup.size();
        if (hasAmp && !this.isPrivateSoundMode) {
            this.mBootSoundGroupIndex = 2;
        } else {
            this.mBootSoundGroupIndex = 1;
        }
        String str = TAG;
        LogUtil.i(str, "checkBootSoundGroupIndex,group size=" + groupSize + ",hasAmp=" + hasAmp + ",boot sound group=" + this.mBootSoundGroupIndex);
    }

    private SparseArray<BootSoundResource> getCurrentBootSoundGroup() {
        checkBootSoundGroupIndex();
        SparseArray<BootSoundResource> curretGroup = mBootSoundResourceGroup.get(Integer.valueOf(this.mBootSoundGroupIndex));
        return curretGroup;
    }

    private void registerObserver() {
        this.isPrivateSoundMode = Settings.System.getInt(this.mContext.getContentResolver(), "XpMainDriverMode", 0) == 2;
        String str = TAG;
        LogUtil.i(str, "registerObserver isPrivateSoundMode: " + this.isPrivateSoundMode);
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("XpMainDriverMode"), true, new ContentObserver(new XuiWorkHandler()) { // from class: com.xiaopeng.xuiservice.soundresource.SoundResourceLoader.2
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                SoundResourceLoader soundResourceLoader = SoundResourceLoader.this;
                soundResourceLoader.isPrivateSoundMode = Settings.System.getInt(soundResourceLoader.mContext.getContentResolver(), "XpMainDriverMode", 0) == 2;
                String str2 = SoundResourceLoader.TAG;
                LogUtil.i(str2, "onChange isPrivateSoundMode: " + SoundResourceLoader.this.isPrivateSoundMode);
            }
        });
    }
}
