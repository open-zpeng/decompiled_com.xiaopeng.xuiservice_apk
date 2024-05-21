package com.xiaopeng.xuiservice.operation.db;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomMasterTable;
import androidx.room.RoomOpenHelper;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.util.HashMap;
import java.util.HashSet;
/* loaded from: classes5.dex */
public class ResourceDatabase_Impl extends ResourceDatabase {
    private volatile ResourceDao _resourceDao;
    private volatile SelectedDao _selectedDao;

    @Override // androidx.room.RoomDatabase
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
        SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(4) { // from class: com.xiaopeng.xuiservice.operation.db.ResourceDatabase_Impl.1
            @Override // androidx.room.RoomOpenHelper.Delegate
            public void createAllTables(SupportSQLiteDatabase _db) {
                _db.execSQL("CREATE TABLE IF NOT EXISTS `ResourceInfo` (`resourceId` TEXT NOT NULL, `resourceType` INTEGER NOT NULL, `resourceName` TEXT NOT NULL, `targetPath` TEXT, `md5Hash` TEXT, `effectTime` INTEGER NOT NULL, `expireTime` INTEGER NOT NULL, `resourceFrom` TEXT, `extraInfo` TEXT, `status` INTEGER NOT NULL, `createTime` INTEGER NOT NULL, `updateTime` INTEGER NOT NULL, `resourceIcon` TEXT, `description` TEXT, `price` TEXT, PRIMARY KEY(`resourceId`))");
                _db.execSQL("CREATE TABLE IF NOT EXISTS `SelectedInfo` (`resourceType` INTEGER NOT NULL, `resourceId` TEXT NOT NULL, PRIMARY KEY(`resourceType`))");
                _db.execSQL(RoomMasterTable.CREATE_QUERY);
                _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"a4e40736b0d628e5caee1230e183017c\")");
            }

            @Override // androidx.room.RoomOpenHelper.Delegate
            public void dropAllTables(SupportSQLiteDatabase _db) {
                _db.execSQL("DROP TABLE IF EXISTS `ResourceInfo`");
                _db.execSQL("DROP TABLE IF EXISTS `SelectedInfo`");
            }

            @Override // androidx.room.RoomOpenHelper.Delegate
            protected void onCreate(SupportSQLiteDatabase _db) {
                if (ResourceDatabase_Impl.this.mCallbacks != null) {
                    int _size = ResourceDatabase_Impl.this.mCallbacks.size();
                    for (int _i = 0; _i < _size; _i++) {
                        ((RoomDatabase.Callback) ResourceDatabase_Impl.this.mCallbacks.get(_i)).onCreate(_db);
                    }
                }
            }

            @Override // androidx.room.RoomOpenHelper.Delegate
            public void onOpen(SupportSQLiteDatabase _db) {
                ResourceDatabase_Impl.this.mDatabase = _db;
                ResourceDatabase_Impl.this.internalInitInvalidationTracker(_db);
                if (ResourceDatabase_Impl.this.mCallbacks != null) {
                    int _size = ResourceDatabase_Impl.this.mCallbacks.size();
                    for (int _i = 0; _i < _size; _i++) {
                        ((RoomDatabase.Callback) ResourceDatabase_Impl.this.mCallbacks.get(_i)).onOpen(_db);
                    }
                }
            }

            @Override // androidx.room.RoomOpenHelper.Delegate
            protected void validateMigration(SupportSQLiteDatabase _db) {
                HashMap<String, TableInfo.Column> _columnsResourceInfo = new HashMap<>(15);
                _columnsResourceInfo.put("resourceId", new TableInfo.Column("resourceId", "TEXT", true, 1));
                _columnsResourceInfo.put("resourceType", new TableInfo.Column("resourceType", "INTEGER", true, 0));
                _columnsResourceInfo.put("resourceName", new TableInfo.Column("resourceName", "TEXT", true, 0));
                _columnsResourceInfo.put("targetPath", new TableInfo.Column("targetPath", "TEXT", false, 0));
                _columnsResourceInfo.put("md5Hash", new TableInfo.Column("md5Hash", "TEXT", false, 0));
                _columnsResourceInfo.put("effectTime", new TableInfo.Column("effectTime", "INTEGER", true, 0));
                _columnsResourceInfo.put("expireTime", new TableInfo.Column("expireTime", "INTEGER", true, 0));
                _columnsResourceInfo.put("resourceFrom", new TableInfo.Column("resourceFrom", "TEXT", false, 0));
                _columnsResourceInfo.put("extraInfo", new TableInfo.Column("extraInfo", "TEXT", false, 0));
                _columnsResourceInfo.put("status", new TableInfo.Column("status", "INTEGER", true, 0));
                _columnsResourceInfo.put("createTime", new TableInfo.Column("createTime", "INTEGER", true, 0));
                _columnsResourceInfo.put("updateTime", new TableInfo.Column("updateTime", "INTEGER", true, 0));
                _columnsResourceInfo.put("resourceIcon", new TableInfo.Column("resourceIcon", "TEXT", false, 0));
                _columnsResourceInfo.put("description", new TableInfo.Column("description", "TEXT", false, 0));
                _columnsResourceInfo.put("price", new TableInfo.Column("price", "TEXT", false, 0));
                HashSet<TableInfo.ForeignKey> _foreignKeysResourceInfo = new HashSet<>(0);
                HashSet<TableInfo.Index> _indicesResourceInfo = new HashSet<>(0);
                TableInfo _infoResourceInfo = new TableInfo("ResourceInfo", _columnsResourceInfo, _foreignKeysResourceInfo, _indicesResourceInfo);
                TableInfo _existingResourceInfo = TableInfo.read(_db, "ResourceInfo");
                if (!_infoResourceInfo.equals(_existingResourceInfo)) {
                    throw new IllegalStateException("Migration didn't properly handle ResourceInfo(com.xiaopeng.xuiservice.operation.db.ResourceInfo).\n Expected:\n" + _infoResourceInfo + "\n Found:\n" + _existingResourceInfo);
                }
                HashMap<String, TableInfo.Column> _columnsSelectedInfo = new HashMap<>(2);
                _columnsSelectedInfo.put("resourceType", new TableInfo.Column("resourceType", "INTEGER", true, 1));
                _columnsSelectedInfo.put("resourceId", new TableInfo.Column("resourceId", "TEXT", true, 0));
                HashSet<TableInfo.ForeignKey> _foreignKeysSelectedInfo = new HashSet<>(0);
                HashSet<TableInfo.Index> _indicesSelectedInfo = new HashSet<>(0);
                TableInfo _infoSelectedInfo = new TableInfo("SelectedInfo", _columnsSelectedInfo, _foreignKeysSelectedInfo, _indicesSelectedInfo);
                TableInfo _existingSelectedInfo = TableInfo.read(_db, "SelectedInfo");
                if (!_infoSelectedInfo.equals(_existingSelectedInfo)) {
                    throw new IllegalStateException("Migration didn't properly handle SelectedInfo(com.xiaopeng.xuiservice.operation.db.SelectedInfo).\n Expected:\n" + _infoSelectedInfo + "\n Found:\n" + _existingSelectedInfo);
                }
            }
        }, "a4e40736b0d628e5caee1230e183017c", "00679fe1fb3b11ac7c7d3a673c53db1b");
        SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context).name(configuration.name).callback(_openCallback).build();
        SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
        return _helper;
    }

    @Override // androidx.room.RoomDatabase
    protected InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, "ResourceInfo", "SelectedInfo");
    }

    @Override // androidx.room.RoomDatabase
    public void clearAllTables() {
        super.assertNotMainThread();
        SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
        try {
            super.beginTransaction();
            _db.execSQL("DELETE FROM `ResourceInfo`");
            _db.execSQL("DELETE FROM `SelectedInfo`");
            super.setTransactionSuccessful();
        } finally {
            super.endTransaction();
            _db.query("PRAGMA wal_checkpoint(FULL)").close();
            if (!_db.inTransaction()) {
                _db.execSQL("VACUUM");
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.operation.db.ResourceDatabase
    public ResourceDao resourceDao() {
        ResourceDao resourceDao;
        if (this._resourceDao != null) {
            return this._resourceDao;
        }
        synchronized (this) {
            if (this._resourceDao == null) {
                this._resourceDao = new ResourceDao_Impl(this);
            }
            resourceDao = this._resourceDao;
        }
        return resourceDao;
    }

    @Override // com.xiaopeng.xuiservice.operation.db.ResourceDatabase
    public SelectedDao selectedDao() {
        SelectedDao selectedDao;
        if (this._selectedDao != null) {
            return this._selectedDao;
        }
        synchronized (this) {
            if (this._selectedDao == null) {
                this._selectedDao = new SelectedDao_Impl(this);
            }
            selectedDao = this._selectedDao;
        }
        return selectedDao;
    }
}
