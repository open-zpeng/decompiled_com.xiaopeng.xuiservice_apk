package com.xiaopeng.xuiservice.xapp.mode.octopus.db;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomMasterTable;
import androidx.room.RoomOpenHelper;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.xiaopeng.speech.speechwidget.SpeechWidget;
import java.util.HashMap;
import java.util.HashSet;
/* loaded from: classes5.dex */
public class ConfigDatabase_Impl extends ConfigDatabase {
    private volatile KeyConfigDao _keyConfigDao;

    @Override // androidx.room.RoomDatabase
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
        SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) { // from class: com.xiaopeng.xuiservice.xapp.mode.octopus.db.ConfigDatabase_Impl.1
            @Override // androidx.room.RoomOpenHelper.Delegate
            public void createAllTables(SupportSQLiteDatabase _db) {
                _db.execSQL("CREATE TABLE IF NOT EXISTS `KeyConfig` (`pkgName` TEXT NOT NULL, `configName` TEXT NOT NULL, `selected` INTEGER NOT NULL, `configInfo` TEXT, PRIMARY KEY(`pkgName`, `configName`))");
                _db.execSQL(RoomMasterTable.CREATE_QUERY);
                _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"faaa1c367f1f27bb58c7351f0288bfb3\")");
            }

            @Override // androidx.room.RoomOpenHelper.Delegate
            public void dropAllTables(SupportSQLiteDatabase _db) {
                _db.execSQL("DROP TABLE IF EXISTS `KeyConfig`");
            }

            @Override // androidx.room.RoomOpenHelper.Delegate
            protected void onCreate(SupportSQLiteDatabase _db) {
                if (ConfigDatabase_Impl.this.mCallbacks != null) {
                    int _size = ConfigDatabase_Impl.this.mCallbacks.size();
                    for (int _i = 0; _i < _size; _i++) {
                        ((RoomDatabase.Callback) ConfigDatabase_Impl.this.mCallbacks.get(_i)).onCreate(_db);
                    }
                }
            }

            @Override // androidx.room.RoomOpenHelper.Delegate
            public void onOpen(SupportSQLiteDatabase _db) {
                ConfigDatabase_Impl.this.mDatabase = _db;
                ConfigDatabase_Impl.this.internalInitInvalidationTracker(_db);
                if (ConfigDatabase_Impl.this.mCallbacks != null) {
                    int _size = ConfigDatabase_Impl.this.mCallbacks.size();
                    for (int _i = 0; _i < _size; _i++) {
                        ((RoomDatabase.Callback) ConfigDatabase_Impl.this.mCallbacks.get(_i)).onOpen(_db);
                    }
                }
            }

            @Override // androidx.room.RoomOpenHelper.Delegate
            protected void validateMigration(SupportSQLiteDatabase _db) {
                HashMap<String, TableInfo.Column> _columnsKeyConfig = new HashMap<>(4);
                _columnsKeyConfig.put("pkgName", new TableInfo.Column("pkgName", "TEXT", true, 1));
                _columnsKeyConfig.put("configName", new TableInfo.Column("configName", "TEXT", true, 2));
                _columnsKeyConfig.put(SpeechWidget.DATA_SOURCE_SELECT, new TableInfo.Column(SpeechWidget.DATA_SOURCE_SELECT, "INTEGER", true, 0));
                _columnsKeyConfig.put("configInfo", new TableInfo.Column("configInfo", "TEXT", false, 0));
                HashSet<TableInfo.ForeignKey> _foreignKeysKeyConfig = new HashSet<>(0);
                HashSet<TableInfo.Index> _indicesKeyConfig = new HashSet<>(0);
                TableInfo _infoKeyConfig = new TableInfo("KeyConfig", _columnsKeyConfig, _foreignKeysKeyConfig, _indicesKeyConfig);
                TableInfo _existingKeyConfig = TableInfo.read(_db, "KeyConfig");
                if (!_infoKeyConfig.equals(_existingKeyConfig)) {
                    throw new IllegalStateException("Migration didn't properly handle KeyConfig(com.xiaopeng.xuiservice.xapp.mode.octopus.db.KeyConfig).\n Expected:\n" + _infoKeyConfig + "\n Found:\n" + _existingKeyConfig);
                }
            }
        }, "faaa1c367f1f27bb58c7351f0288bfb3", "12ffec286e62b71047bd56072509e320");
        SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context).name(configuration.name).callback(_openCallback).build();
        SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
        return _helper;
    }

    @Override // androidx.room.RoomDatabase
    protected InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, "KeyConfig");
    }

    @Override // androidx.room.RoomDatabase
    public void clearAllTables() {
        super.assertNotMainThread();
        SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
        try {
            super.beginTransaction();
            _db.execSQL("DELETE FROM `KeyConfig`");
            super.setTransactionSuccessful();
        } finally {
            super.endTransaction();
            _db.query("PRAGMA wal_checkpoint(FULL)").close();
            if (!_db.inTransaction()) {
                _db.execSQL("VACUUM");
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.xapp.mode.octopus.db.ConfigDatabase
    public KeyConfigDao keyConfigDao() {
        KeyConfigDao keyConfigDao;
        if (this._keyConfigDao != null) {
            return this._keyConfigDao;
        }
        synchronized (this) {
            if (this._keyConfigDao == null) {
                this._keyConfigDao = new KeyConfigDao_Impl(this);
            }
            keyConfigDao = this._keyConfigDao;
        }
        return keyConfigDao;
    }
}
