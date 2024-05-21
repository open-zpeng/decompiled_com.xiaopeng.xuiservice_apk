package com.xiaopeng.xuiservice.themeoperation;

import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.ArrayMap;
import androidx.annotation.NonNull;
import com.xiaopeng.xuimanager.XUIManager;
import com.xiaopeng.xuimanager.operation.IOperationListener;
import com.xiaopeng.xuimanager.operation.OperationManager;
import com.xiaopeng.xuimanager.operation.OperationResource;
import com.xiaopeng.xuimanager.themeoperation.AbilityEffect;
import com.xiaopeng.xuimanager.themeoperation.ThemeOperationData;
import com.xiaopeng.xuimanager.themeoperation.ThemeOperationListener;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.themeoperation.ThemeResManager;
import com.xiaopeng.xuiservice.themeoperation.effect.BaseEffect;
import com.xiaopeng.xuiservice.themeoperation.effect.BootAnimationEffect;
import com.xiaopeng.xuiservice.themeoperation.effect.SkinEffect;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.FileUtils;
import com.xiaopeng.xuiservice.utils.SharedPreferencesUtil;
import com.xiaopeng.xuiservice.utils.UiHandler;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class ThemeResManager {
    private static final String GLOBAL_CONFIG_FILE = "themeconfig.txt";
    private static final String KEY_ACTIVE_THEME_SP = "theme_active";
    private static final String KEY_THEME_NAME = "name";
    private static final String KEY_VERSION = "version";
    private static final int MSG_REMOTE_QUERY_DONE = 1;
    private static final String THEME_CONFIG_FILE = "config.txt";
    private static final String THEME_EFFECT_DESCRIPTION_FILE = "themedesc.txt";
    private static final String THEME_OPERATION_PATH = "/data/operation/resource/THEME_OPERATION";
    private static final String THEME_SYSTEM_PATH = "/system/resource/themeoperation";
    public static final String THEME_SYSTEM_PATH_PREFIX = "/system";
    private static String mActiveThemeId;
    private ThemeOperationData mDefaultTheme;
    private Handler mHander;
    private ThemeOperationQuery mOperationQuery;
    private final IOperationListener operationListener;
    private static final String TAG = ThemeResManager.class.getSimpleName();
    private static final Map<String, BaseEffect> effectMap = new ConcurrentHashMap();
    private static final Map<String, ThemeOperationData> themeMap = new ConcurrentHashMap();
    private static ThemeOperationListener themeOperationListener = null;

    /* synthetic */ ThemeResManager(AnonymousClass1 x0) {
        this();
    }

    /* renamed from: com.xiaopeng.xuiservice.themeoperation.ThemeResManager$1  reason: invalid class name */
    /* loaded from: classes5.dex */
    class AnonymousClass1 implements IOperationListener {
        AnonymousClass1() {
        }

        public void onOperationSourceAdd(int type, final OperationResource resource) {
            String str = ThemeResManager.TAG;
            LogUtil.i(str, "onOperationSourceAdd,type=" + type + ",res=" + resource);
            if (type == 1080100) {
                String str2 = ThemeResManager.TAG;
                LogUtil.d(str2, "onOperationSourceAdd,try to load from:" + resource.getTargetPath());
                XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.themeoperation.-$$Lambda$ThemeResManager$1$oPdTtyNrRsSw5o3DcUBXwr4_AG4
                    @Override // java.lang.Runnable
                    public final void run() {
                        ThemeResManager.AnonymousClass1.lambda$onOperationSourceAdd$0(resource);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$onOperationSourceAdd$0(OperationResource resource) {
            List<ThemeOperationData> list = new ArrayList<>();
            ThemeOperationData data = (ThemeOperationData) ThemeResManager.themeMap.get(resource.getId());
            File file = new File(resource.getTargetPath());
            if (!file.exists()) {
                LogUtil.w(ThemeResManager.TAG, "update theme fail,path not exist:" + resource.getTargetPath());
                return;
            }
            File[] folders = file.listFiles();
            if (folders == null || folders.length <= 0) {
                LogUtil.w(ThemeResManager.TAG, "strange, folder is null on:" + resource.getTargetPath());
                return;
            }
            data.setTargetPath(resource.getTargetPath());
            String innerName = folders[0].getName();
            data.setInnerName(innerName);
            String targetPath = resource.getTargetPath() + innerName;
            data.setThemePath(targetPath);
            ThemeResManager.updateThemeData(targetPath, data);
            list.add(data);
            ThemeResManager.themeOperationListener.onThemeStatus("theme_ready", (String) null, list);
        }

        public void onOperationSourceExpire(int type, OperationResource resource) {
            String str = ThemeResManager.TAG;
            LogUtil.i(str, "onOperationSourceExpire,type=" + type + ",res=" + resource);
            if (type != 1080100) {
            }
        }

        public void onOperationSourceDelete(int type, OperationResource resource) {
            String str = ThemeResManager.TAG;
            LogUtil.i(str, "onOperationSourceDelete,type=" + type + ",res=" + resource);
            if (type != 1080100) {
            }
        }

        public void onRemoteSourceQuerySuccess(int type, List<OperationResource> resources) {
            String str = ThemeResManager.TAG;
            LogUtil.i(str, "onRemoteSourceQuerySuccess,type=" + type + ",list=" + resources);
            if (type == 1080100) {
                ThemeResManager.this.mHander.obtainMessage(1, resources).sendToTarget();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class ThemeResManagerHolder {
        private static final ThemeResManager instance = new ThemeResManager(null);

        private ThemeResManagerHolder() {
        }
    }

    private ThemeResManager() {
        this.mDefaultTheme = new ThemeOperationData();
        this.mOperationQuery = null;
        this.operationListener = new AnonymousClass1();
        this.mHander = new Handler(XuiWorkHandler.getInstance().getLooper()) { // from class: com.xiaopeng.xuiservice.themeoperation.ThemeResManager.2
            @Override // android.os.Handler
            public void handleMessage(@NonNull Message msg) {
                ThemeResManager.this.handleOperationMessage(msg);
            }
        };
        initInner();
    }

    public static ThemeResManager getInstance() {
        return ThemeResManagerHolder.instance;
    }

    public void setThemeListener(ThemeOperationListener listener) {
        themeOperationListener = listener;
    }

    public void initInner() {
        this.mOperationQuery = ThemeOperationQuery.getInstance();
        this.mOperationQuery.setListener(this.operationListener);
        this.mOperationQuery.init();
        if (TextUtils.isEmpty(mActiveThemeId)) {
            mActiveThemeId = SharedPreferencesUtil.getInstance().get(KEY_ACTIVE_THEME_SP, null);
            if (mActiveThemeId == null) {
                mActiveThemeId = "default";
            }
        }
        addEffectMap();
        initEffectMap();
        loadThemeRscFromDisk(THEME_SYSTEM_PATH, false);
        loadThemeRscFromDisk(THEME_OPERATION_PATH, true);
        UiHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.themeoperation.-$$Lambda$ThemeResManager$Ijyg6f2DbdWPCgSndyXpoicEFtA
            @Override // java.lang.Runnable
            public final void run() {
                ThemeResManager.this.lambda$initInner$2$ThemeResManager();
            }
        }, 1000L);
        defaultThemeInit();
        ThemeOperationData activeData = themeMap.get(mActiveThemeId);
        if (activeData != null) {
            activeData.setActive(true);
            return;
        }
        String str = TAG;
        LogUtil.w(str, "init,no record for:" + mActiveThemeId);
    }

    public /* synthetic */ void lambda$initInner$2$ThemeResManager() {
        if (BroadcastManager.getInstance().isUserUnlocked()) {
            XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.themeoperation.-$$Lambda$ThemeResManager$mMoU_fawG7pVjcmOMkwRle1Xa4k
                @Override // java.lang.Runnable
                public final void run() {
                    ThemeResManager.this.lambda$initInner$0$ThemeResManager();
                }
            });
        } else {
            BroadcastManager.getInstance().addBootCompleteTask(new Runnable() { // from class: com.xiaopeng.xuiservice.themeoperation.-$$Lambda$ThemeResManager$cQ0JKwEOXXWe1Tiwxk57rQBniJA
                @Override // java.lang.Runnable
                public final void run() {
                    ThemeResManager.this.lambda$initInner$1$ThemeResManager();
                }
            });
        }
    }

    public void init() {
    }

    public ThemeOperationData[] getThemes() {
        int length = themeMap.size();
        if (length < 1) {
            LogUtil.d(TAG, "no themes");
            return null;
        }
        int i = 0;
        ThemeOperationData[] data = new ThemeOperationData[length];
        for (ThemeOperationData d : themeMap.values()) {
            data[i] = d;
            i++;
        }
        String str = TAG;
        LogUtil.i(str, "getThemes,len=" + Integer.valueOf(data.length));
        return data;
    }

    public ThemeOperationData getCurrentTheme() {
        ThemeOperationData data = themeMap.get(mActiveThemeId);
        String str = TAG;
        LogUtil.i(str, "getCurrentTheme,active=" + mActiveThemeId + ",data=" + data);
        return data;
    }

    public synchronized boolean selectTheme(ThemeOperationData themeData) {
        String str = TAG;
        LogUtil.i(str, "selectTheme:" + themeData + ",current=" + mActiveThemeId);
        boolean ret = themeDataVerify(themeData);
        if (!ret) {
            LogUtil.w(TAG, "selectTheme,data verify fail");
            return false;
        } else if (mActiveThemeId.equals(themeData.getId())) {
            LogUtil.w(TAG, "selectTheme,apply the same theme as last");
            return true;
        } else {
            ThemeOperationData oldData = getAcitiveThemeData();
            ArrayList<ThemeOperationData> dataList = new ArrayList<>();
            dataList.add(themeData);
            themeOperationListener.onThemeStatus("theme_selected", (String) null, dataList);
            applyEffects(themeData);
            mActiveThemeId = themeData.getId();
            SharedPreferencesUtil.getInstance().put(KEY_ACTIVE_THEME_SP, mActiveThemeId);
            if (oldData == null) {
                LogUtil.w(TAG, "selectTheme,no old data!");
            } else {
                oldData.setActive(false);
            }
            ThemeOperationData activeData = themeMap.get(mActiveThemeId);
            if (activeData != null) {
                activeData.setActive(true);
                return true;
            }
            String str2 = TAG;
            LogUtil.w(str2, "selectTheme,no record for:" + mActiveThemeId);
            return false;
        }
    }

    public boolean resetTheme(ThemeOperationData themeData) {
        boolean ret = themeDataVerify(themeData);
        if (!ret) {
            LogUtil.w(TAG, "resetTheme,data verify fail");
            return false;
        }
        String str = TAG;
        LogUtil.i(str, "resetTheme:" + themeData);
        return true;
    }

    public boolean updateTheme(ThemeOperationData themeData) {
        boolean ret = themeDataVerify(themeData);
        if (!ret) {
            LogUtil.w(TAG, "updateTheme,data verify fail");
            return false;
        }
        String str = TAG;
        LogUtil.i(str, "updateTheme:" + themeData + ",url:" + themeData.getDownloadUrl());
        this.mOperationQuery.downloadResource(themeData);
        return true;
    }

    public boolean deleteTheme(ThemeOperationData themeData) {
        if (1 == themeData.getResStorageType()) {
            String str = TAG;
            LogUtil.w(str, "try to delete preset Theme:" + themeData);
            return false;
        }
        boolean ret = themeDataVerify(themeData);
        if (!ret) {
            LogUtil.w(TAG, "deleteTheme,data verify fail");
            return false;
        } else if (!TextUtils.isEmpty(mActiveThemeId) && mActiveThemeId.equals(themeData.getId())) {
            String str2 = TAG;
            LogUtil.w(str2, "deleteTheme, theme is active:" + themeData);
            return false;
        } else {
            String str3 = TAG;
            LogUtil.i(str3, "deleteTheme:" + themeData);
            unloadEffects(themeData);
            synchronized (themeMap) {
                themeMap.remove(themeData.getId());
            }
            this.mOperationQuery.deleteResource(themeData);
            ArrayList<ThemeOperationData> dataList = new ArrayList<>();
            dataList.add(themeData);
            themeOperationListener.onThemeStatus("theme_deleted", (String) null, dataList);
            return true;
        }
    }

    public boolean selectEffect(AbilityEffect effect) {
        String abilityType = effect.getEffectAbilityType();
        BaseEffect effectImpl = effectMap.get(abilityType);
        if (effectImpl != null) {
            String str = TAG;
            LogUtil.i(str, "applyEffects for:" + abilityType);
            effectImpl.selectEffect(effect);
            return true;
        }
        String str2 = TAG;
        LogUtil.w(str2, "applyEffects error for:" + abilityType + ",effect=" + effect);
        return false;
    }

    public ThemeOperationData getAcitiveThemeData() {
        return themeMap.get(mActiveThemeId);
    }

    public boolean isActiveThemeDefault() {
        return "default".equals(mActiveThemeId);
    }

    private void addEffectMap() {
        effectMap.put("skin", SkinEffect.getInstance());
        effectMap.put("bootani", BootAnimationEffect.getInstance());
    }

    private void initEffectMap() {
        for (BaseEffect effect : effectMap.values()) {
            effect.init();
        }
    }

    private boolean themeDataVerify(ThemeOperationData themeData) {
        ThemeOperationData data;
        if (themeMap.isEmpty()) {
            LogUtil.d(TAG, "themeDataVerify, no data");
            return false;
        }
        synchronized (themeMap) {
            data = themeMap.get(themeData.getId());
        }
        return data != null;
    }

    private void applyEffects(ThemeOperationData themeData) {
        LogUtil.d(TAG, "applyEffects...");
        for (AbilityEffect effect : themeData.getActiveEffectMap().values()) {
            String abilityType = effect.getEffectAbilityType();
            BaseEffect effectImpl = effectMap.get(abilityType);
            if (effectImpl != null) {
                String str = TAG;
                LogUtil.i(str, "applyEffects for:" + abilityType);
                effectImpl.selectEffect(effect);
            } else {
                String str2 = TAG;
                LogUtil.w(str2, "applyEffects error for:" + abilityType);
            }
        }
    }

    private void unloadEffects(ThemeOperationData themeData) {
        LogUtil.d(TAG, "unloadEffects in");
        ArrayMap<String, AbilityEffect[]> map = themeData.getEffectMap();
        if (map == null) {
            LogUtil.w(TAG, "unloadEffects,no effect map");
            return;
        }
        for (Map.Entry<String, AbilityEffect[]> entry : map.entrySet()) {
            AbilityEffect[] abilityEffectArr = (Parcelable[]) entry.getValue();
            if (abilityEffectArr != null && abilityEffectArr.length != 0) {
                AbilityEffect[] effects = new AbilityEffect[abilityEffectArr.length];
                for (int i = 0; i < abilityEffectArr.length; i++) {
                    effects[i] = abilityEffectArr[i];
                }
                String abilityType = effects[0].getEffectAbilityType();
                BaseEffect effectImpl = effectMap.get(abilityType);
                if (effectImpl != null) {
                    String str = TAG;
                    LogUtil.i(str, "unloadEffects for:" + abilityType);
                    effectImpl.unloadEffect(Arrays.asList(effects));
                } else {
                    String str2 = TAG;
                    LogUtil.w(str2, "unloadEffects error for:" + abilityType);
                }
            }
        }
        LogUtil.d(TAG, "unloadEffects out");
    }

    private static void loadThemeRscFromDisk(String path, boolean maybeSkipResIdPath) {
        int storageType;
        boolean preSet = false;
        if (path.startsWith(THEME_SYSTEM_PATH_PREFIX)) {
            storageType = 1;
            preSet = true;
        } else {
            storageType = 2;
        }
        try {
            File mfile = new File(path);
            if (!mfile.exists()) {
                LogUtil.e(TAG, "loadThemeRscFromDisk path:" + path + " not exist!");
            } else if (mfile.isDirectory()) {
                File[] files = mfile.listFiles();
                if (files == null) {
                    LogUtil.e(TAG, "empty folder:" + path);
                    return;
                }
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        File themFile = files[i];
                        String resIdName = null;
                        if (!preSet) {
                            String realPath = files[i].getAbsolutePath();
                            if (maybeSkipResIdPath) {
                                if (files[i].isDirectory()) {
                                    File[] themeFiles = files[i].listFiles();
                                    if (themeFiles == null) {
                                        LogUtil.e(TAG, "empty folder:" + path);
                                    } else {
                                        resIdName = files[i].getName();
                                        String realPath2 = realPath + "/" + themeFiles[0].getName();
                                        themFile = new File(realPath2);
                                        LogUtil.d(TAG, "loadThemeRscFromDisk,operation path:" + realPath2);
                                    }
                                } else {
                                    LogUtil.w(TAG, "loadThemeRscFromDisk,no theme folder on:" + realPath);
                                }
                            }
                        }
                        ThemeOperationData data = loadThemeData(themFile, preSet, resIdName);
                        if (data != null) {
                            if (!TextUtils.isEmpty(mActiveThemeId) && mActiveThemeId.equals(data.getId())) {
                                data.setActive(true);
                            }
                            data.setResStorageType(storageType);
                            themeMap.put(data.getId(), data);
                        } else {
                            LogUtil.w(TAG, "loadThemeRscFromDisk,theme not found on:" + themFile.getAbsolutePath());
                        }
                    } else {
                        LogUtil.w(TAG, "go through theme folder but not:" + files[i].getAbsolutePath());
                    }
                }
                LogUtil.d(TAG, "load theme size=" + themeMap.size() + ",data=" + themeMap);
            } else {
                LogUtil.w(TAG, "loadThemeRscFromDisk, not folder:" + path);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "loadThemeRscFromDisk fail e=" + e);
        }
    }

    private static ThemeOperationData loadThemeData(File file, boolean preset, String resIdName) {
        File[] files;
        ArrayMap<String, AbilityEffect> map;
        String path = file.getAbsolutePath();
        ThemeOperationData data = new ThemeOperationData();
        String str = TAG;
        LogUtil.d(str, "loadThemeData on:" + path);
        String themeName = file.getName();
        data.setThemePath(path);
        data.setInnerName(file.getName());
        if (preset) {
            data.setId(themeName);
        } else {
            if (resIdName != null) {
                data.setId(resIdName);
            }
            data.setResourceName(themeName);
        }
        String content = FileUtils.readFromFile(path + "/" + THEME_CONFIG_FILE);
        try {
            JSONObject obj = new JSONObject(content);
            int version = obj.optInt(KEY_VERSION, 0);
            String[] values = obj.optString("name").split(",");
            data.setFriendlyName(values[0]);
            String str2 = TAG;
            LogUtil.d(str2, "loadThemeData,ver=" + version + ",name=" + values[0]);
        } catch (Exception e) {
            String str3 = TAG;
            LogUtil.w(str3, "loadThemeData e=" + e);
        }
        File[] files2 = file.listFiles();
        if (files2 == null) {
            LogUtil.e(TAG, "loadThemeData,empty effect res folder");
            return null;
        }
        int i = 0;
        while (i < files2.length) {
            if (!files2[i].isDirectory()) {
                files = files2;
            } else {
                String effectType = files2[i].getName();
                BaseEffect effectImpl = effectMap.get(effectType);
                if (effectImpl == null) {
                    files = files2;
                } else {
                    String effectPath = files2[i].getAbsolutePath();
                    List<AbilityEffect> effectList = effectImpl.loadEffect(effectPath);
                    if (effectList == null || effectList.isEmpty()) {
                        files = files2;
                        String str4 = TAG;
                        LogUtil.w(str4, "loadThemeData,fail for load:" + effectPath);
                    } else {
                        String str5 = TAG;
                        LogUtil.d(str5, "loadThemeData,find effect:" + effectList);
                        ArrayMap<String, AbilityEffect[]> effectMap2 = data.getEffectMap();
                        if (effectMap2 == null) {
                            effectMap2 = new ArrayMap<>();
                            data.setEffectMap(effectMap2);
                        }
                        int length = effectList.size();
                        AbilityEffect[] effects = new AbilityEffect[length];
                        int k = 0;
                        for (AbilityEffect effect : effectList) {
                            effects[k] = effect;
                            k++;
                        }
                        effectMap2.put(effectType, effects);
                        ArrayMap<String, AbilityEffect> map2 = data.getActiveEffectMap();
                        if (map2 != null) {
                            map = map2;
                        } else {
                            map = new ArrayMap<>();
                            data.setActiveEffectMap(map);
                        }
                        AbilityEffect activeEffect = effectList.get(0);
                        files = files2;
                        map.put(activeEffect.getEffectAbilityType(), activeEffect);
                    }
                }
            }
            i++;
            files2 = files;
        }
        String str6 = TAG;
        LogUtil.i(str6, "loadThemeData=" + data + ",actives=" + data.getActiveEffectMap());
        return data;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void updateThemeData(String themePath, ThemeOperationData data) {
        File file;
        String str = TAG;
        LogUtil.d(str, "updateThemeData on:" + themePath);
        String content = FileUtils.readFromFile(themePath + "/" + THEME_CONFIG_FILE);
        int i = 0;
        try {
            JSONObject obj = new JSONObject(content);
            int version = obj.optInt(KEY_VERSION, 0);
            String[] values = obj.optString("name").split(",");
            data.setFriendlyName(values[0]);
            String str2 = TAG;
            LogUtil.d(str2, "updateThemeData,ver=" + version + ",name=" + values[0]);
        } catch (Exception e) {
            String str3 = TAG;
            LogUtil.w(str3, "updateThemeData e=" + e);
        }
        File file2 = new File(themePath);
        File[] files = file2.listFiles();
        if (files == null) {
            LogUtil.e(TAG, "updateThemeData,empty effect res folder");
            return;
        }
        int i2 = 0;
        while (i2 < files.length) {
            if (!files[i2].isDirectory()) {
                file = file2;
            } else {
                String effectType = files[i2].getName();
                BaseEffect effectImpl = effectMap.get(effectType);
                if (effectImpl == null) {
                    file = file2;
                } else {
                    String effectPath = files[i2].getAbsolutePath();
                    List<AbilityEffect> effectList = effectImpl.loadEffect(effectPath);
                    if (effectList == null || effectList.isEmpty()) {
                        file = file2;
                        String str4 = TAG;
                        LogUtil.w(str4, "updateThemeData,fail for load:" + effectPath);
                    } else {
                        String str5 = TAG;
                        LogUtil.d(str5, "updateThemeData,find effect:" + effectList);
                        ArrayMap<String, AbilityEffect[]> effectMap2 = data.getEffectMap();
                        if (effectMap2 == null) {
                            effectMap2 = new ArrayMap<>();
                            data.setEffectMap(effectMap2);
                        }
                        int length = effectList.size();
                        AbilityEffect[] effects = new AbilityEffect[length];
                        int k = 0;
                        for (AbilityEffect effect : effectList) {
                            effects[k] = effect;
                            k++;
                        }
                        effectMap2.put(effectType, effects);
                        ArrayMap<String, AbilityEffect> map = data.getActiveEffectMap();
                        if (map == null) {
                            map = new ArrayMap<>();
                            data.setActiveEffectMap(map);
                        }
                        AbilityEffect activeEffect = effectList.get(i);
                        file = file2;
                        map.put(activeEffect.getEffectAbilityType(), activeEffect);
                    }
                }
            }
            i2++;
            file2 = file;
            i = 0;
        }
        data.setUpdateFlag(false);
        String str6 = TAG;
        LogUtil.i(str6, "updateThemeData=" + data + ",actives=" + data.getActiveEffectMap());
    }

    private void invalidateDefaultThemeData() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOperationMessage(Message msg) {
        String str = TAG;
        LogUtil.d(str, "handleOperationMessage,what=" + msg.what);
        if (msg.what == 1) {
            handleRemoteQuery((List) msg.obj);
        }
    }

    private void handleRemoteQuery(List<OperationResource> resources) {
        if (resources == null || resources.isEmpty()) {
            LogUtil.i(TAG, "handleRemoteQuery,no resource list");
            return;
        }
        List<ThemeOperationData> updatedList = new ArrayList<>();
        for (OperationResource res : resources) {
            ThemeOperationData data = getUpdateResource(res);
            if (data != null) {
                updatedList.add(data);
            }
        }
        if (!updatedList.isEmpty()) {
            String str = TAG;
            LogUtil.i(str, "handleRemoteQuery, notify list size:" + updatedList.size());
            themeOperationListener.onThemeStatus("theme_available", (String) null, updatedList);
        }
    }

    private ThemeOperationData getUpdateResource(OperationResource res) {
        if (themeMap.size() > 1) {
            for (ThemeOperationData data : themeMap.values()) {
                if (res.getResourceName().equalsIgnoreCase(data.getResourceName())) {
                    String str = TAG;
                    LogUtil.d(str, "getUpdateResource,res name:" + data.getResourceName() + ",local id:" + data.getId() + ",remote id" + res.getId() + ",local extra:" + data.getExtraData() + ",remote extra:" + res.getExtraData());
                    if (res.getId().equals(data.getId())) {
                        return null;
                    }
                    data.setUpdateFlag(true);
                    return data;
                }
            }
        }
        ThemeOperationData data2 = getThemeDataFromResource(res);
        data2.setResStorageType(0);
        data2.setUpdateFlag(true);
        themeMap.put(data2.getId(), data2);
        return data2;
    }

    private ThemeOperationData getThemeDataFromResource(OperationResource res) {
        ThemeOperationData data = new ThemeOperationData();
        data.setId(res.getId());
        data.setResourceType(res.getResourceType());
        data.setResourceName(res.getResourceName());
        data.setFriendlyName(res.getResourceName());
        data.setDownloadUrl(res.getDownloadUrl());
        data.setTargetPath(res.getTargetPath());
        String innerName = getFirstFolderInPath(res.getTargetPath());
        data.setInnerName(innerName);
        data.setThemePath(res.getTargetPath() + "/" + innerName);
        data.setResourceFrom(res.getResourceFrom());
        data.setExtraData(res.getExtraData());
        data.setResStorageType(2);
        data.setActive(false);
        return data;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: updateThemeDataFromDb */
    public void lambda$initInner$1$ThemeResManager() {
        OperationManager opManager = (OperationManager) XUIManager.getInstance().getService("operation");
        try {
            List<OperationResource> list = opManager.getResourceList(1080100);
            if (list != null && !list.isEmpty()) {
                for (ThemeOperationData data : themeMap.values()) {
                    if (2 == data.getResStorageType()) {
                        String str = TAG;
                        LogUtil.d(str, "updateThemeDataFromDb,data before:" + data);
                        Iterator<OperationResource> it = list.iterator();
                        while (true) {
                            if (it.hasNext()) {
                                OperationResource resource = it.next();
                                if (resource.getId().equals(data.getId())) {
                                    String str2 = TAG;
                                    LogUtil.i(str2, "updateThemeDataFromDb,id:" + resource.getId());
                                    data.setEffectTime(resource.getEffectTime());
                                    data.setExpireTime(resource.getExpireTime());
                                    data.setExtraData(resource.getExtraData());
                                    data.setTargetPath(resource.getTargetPath());
                                    break;
                                }
                            }
                        }
                    }
                }
                return;
            }
            LogUtil.w(TAG, "updateThemeDataFromDb,null res list");
        } catch (Exception e) {
            String str3 = TAG;
            LogUtil.w(str3, "updateThemeDataFromDb e=" + e);
        }
    }

    private void defaultThemeInit() {
        this.mDefaultTheme.setId("default");
        this.mDefaultTheme.setFriendlyName("default");
        this.mDefaultTheme.setInnerName("default");
        this.mDefaultTheme.setResStorageType(1);
        defaultEffectsInit();
        themeMap.put(this.mDefaultTheme.getId(), this.mDefaultTheme);
        if (this.mDefaultTheme.getId().equals(mActiveThemeId)) {
            this.mDefaultTheme.setActive(true);
        }
    }

    private void defaultEffectsInit() {
        for (String effectType : effectMap.keySet()) {
            BaseEffect effectImpl = effectMap.get(effectType);
            List<AbilityEffect> effectList = effectImpl.loadEffect("default");
            if (effectList != null && !effectList.isEmpty()) {
                String str = TAG;
                LogUtil.d(str, "defaultEffectsInit,find effect:" + effectList);
                ArrayMap<String, AbilityEffect> map = this.mDefaultTheme.getActiveEffectMap();
                if (map == null) {
                    map = new ArrayMap<>();
                    this.mDefaultTheme.setActiveEffectMap(map);
                }
                AbilityEffect activeEffect = effectList.get(0);
                map.put(activeEffect.getEffectAbilityType(), activeEffect);
            } else {
                String str2 = TAG;
                LogUtil.w(str2, "defaultEffectsInit,fail for load:" + effectType);
            }
        }
    }

    private String getFirstFolderInPath(String path) {
        if (path == null) {
            return null;
        }
        try {
            File file = new File(path);
            if (file.isDirectory()) {
                File[] themeFiles = file.listFiles();
                if (themeFiles == null) {
                    String str = TAG;
                    LogUtil.e(str, "getFirstFolderInPath,empty folder:" + path);
                    return null;
                }
                String folderName = themeFiles[0].getName();
                String str2 = TAG;
                LogUtil.d(str2, "getFirstFolderInPath=" + folderName);
                return folderName;
            }
            String str3 = TAG;
            LogUtil.w(str3, "getFirstFolderInPath,path is not folder:" + path);
            return null;
        } catch (Exception e) {
            String str4 = TAG;
            LogUtil.w(str4, "getFirstFolderInPath e=" + e);
            return null;
        }
    }
}
