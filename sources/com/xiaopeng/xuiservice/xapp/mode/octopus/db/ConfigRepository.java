package com.xiaopeng.xuiservice.xapp.mode.octopus.db;

import android.content.Context;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.util.List;
/* loaded from: classes5.dex */
public class ConfigRepository {
    private static final String TAG = "ConfigRepository";
    private static volatile ConfigRepository mInstance;
    private Context mContext;
    private KeyConfigDao mKeyConfigDao;

    private ConfigRepository(Context context) {
        LogUtil.d(TAG, "create database begin");
        this.mContext = context;
        ConfigDatabase configDatabase = ConfigDatabase.getDatabase(context);
        this.mKeyConfigDao = configDatabase.keyConfigDao();
        LogUtil.d(TAG, "create database end");
    }

    public static ConfigRepository getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ConfigRepository.class) {
                if (mInstance == null) {
                    mInstance = new ConfigRepository(context);
                }
            }
        }
        return mInstance;
    }

    public List<KeyConfig> getPkgConfigs() {
        return null;
    }

    public void saveConfigs(final List<KeyConfig> configs) {
        ConfigDatabase.databaseWriteExecutor.execute(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.octopus.db.ConfigRepository.1
            @Override // java.lang.Runnable
            public void run() {
                ConfigRepository.this.mKeyConfigDao.insertConfigs(configs);
            }
        });
    }

    public void saveConfig(final KeyConfig config) {
        ConfigDatabase.databaseWriteExecutor.execute(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.octopus.db.ConfigRepository.2
            @Override // java.lang.Runnable
            public void run() {
                ConfigRepository.this.mKeyConfigDao.insertConfig(config);
            }
        });
    }

    public void getConfigs(final String pkgName, final ConfigLoaderCallback callback) {
        ConfigDatabase.databaseWriteExecutor.execute(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.octopus.db.ConfigRepository.3
            @Override // java.lang.Runnable
            public void run() {
                List<KeyConfig> configs = ConfigRepository.this.mKeyConfigDao.findConfigsByPkg(pkgName);
                callback.onConfigLoaded(configs);
            }
        });
    }
}
