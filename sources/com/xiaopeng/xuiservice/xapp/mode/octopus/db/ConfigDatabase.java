package com.xiaopeng.xuiservice.xapp.mode.octopus.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Database(entities = {KeyConfig.class}, exportSchema = false, version = 1)
/* loaded from: classes5.dex */
public abstract class ConfigDatabase extends RoomDatabase {
    private static final int NUMBER_OF_THREADS = 1;
    private static final String TAG = "ConfigDatabase";
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(1);
    private static volatile ConfigDatabase mInstance;

    public abstract KeyConfigDao keyConfigDao();

    public static ConfigDatabase getDatabase(Context context) {
        if (mInstance == null) {
            synchronized (ConfigDatabase.class) {
                if (mInstance == null) {
                    LogUtil.d(TAG, "room database build");
                    mInstance = (ConfigDatabase) Room.databaseBuilder(context, ConfigDatabase.class, "keyconfig.db").build();
                }
            }
        }
        return mInstance;
    }
}
