package com.xiaopeng.xuiservice.operation.db;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.xiaopeng.xuimanager.utils.LogUtil;
@Database(entities = {ResourceInfo.class, SelectedInfo.class}, exportSchema = false, version = 4)
/* loaded from: classes5.dex */
public abstract class ResourceDatabase extends RoomDatabase {
    static final Migration MIGRATION_2_3 = new Migration(2, 3) { // from class: com.xiaopeng.xuiservice.operation.db.ResourceDatabase.1
        @Override // androidx.room.migration.Migration
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE ResourceInfo ADD COLUMN 'status' INTEGER NOT NULL DEFAULT 0");
        }
    };
    static final Migration MIGRATION_3_4 = new Migration(3, 4) { // from class: com.xiaopeng.xuiservice.operation.db.ResourceDatabase.2
        @Override // androidx.room.migration.Migration
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE ResourceInfo ADD COLUMN 'createTime' INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE ResourceInfo ADD COLUMN 'updateTime' INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE ResourceInfo ADD COLUMN 'resourceIcon' TEXT");
            database.execSQL("ALTER TABLE ResourceInfo ADD COLUMN 'description' TEXT");
            database.execSQL("ALTER TABLE ResourceInfo ADD COLUMN 'price' TEXT");
        }
    };
    private static final String TAG = "ResourceDatabase";
    private static volatile ResourceDatabase mInstance;

    public abstract ResourceDao resourceDao();

    public abstract SelectedDao selectedDao();

    public static ResourceDatabase getDatabase(Context context) {
        if (mInstance == null) {
            synchronized (ResourceDatabase.class) {
                if (mInstance == null) {
                    LogUtil.d(TAG, "room database build");
                    mInstance = (ResourceDatabase) Room.databaseBuilder(context, ResourceDatabase.class, "operation.db").fallbackToDestructiveMigration().addMigrations(MIGRATION_2_3).addMigrations(MIGRATION_3_4).build();
                }
            }
        }
        return mInstance;
    }
}
