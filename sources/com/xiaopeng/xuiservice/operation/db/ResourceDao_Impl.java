package com.xiaopeng.xuiservice.operation.db;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes5.dex */
public class ResourceDao_Impl implements ResourceDao {
    private final RoomDatabase __db;
    private final EntityDeletionOrUpdateAdapter __deletionAdapterOfResourceInfo;
    private final EntityInsertionAdapter __insertionAdapterOfResourceInfo;
    private final SharedSQLiteStatement __preparedStmtOfUpdateResourceStatus;

    public ResourceDao_Impl(RoomDatabase __db) {
        this.__db = __db;
        this.__insertionAdapterOfResourceInfo = new EntityInsertionAdapter<ResourceInfo>(__db) { // from class: com.xiaopeng.xuiservice.operation.db.ResourceDao_Impl.1
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "INSERT OR REPLACE INTO `ResourceInfo`(`resourceId`,`resourceType`,`resourceName`,`targetPath`,`md5Hash`,`effectTime`,`expireTime`,`resourceFrom`,`extraInfo`,`status`,`createTime`,`updateTime`,`resourceIcon`,`description`,`price`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }

            @Override // androidx.room.EntityInsertionAdapter
            public void bind(SupportSQLiteStatement stmt, ResourceInfo value) {
                if (value.resourceId == null) {
                    stmt.bindNull(1);
                } else {
                    stmt.bindString(1, value.resourceId);
                }
                stmt.bindLong(2, value.resourceType);
                if (value.resourceName == null) {
                    stmt.bindNull(3);
                } else {
                    stmt.bindString(3, value.resourceName);
                }
                if (value.targetPath == null) {
                    stmt.bindNull(4);
                } else {
                    stmt.bindString(4, value.targetPath);
                }
                if (value.md5Hash == null) {
                    stmt.bindNull(5);
                } else {
                    stmt.bindString(5, value.md5Hash);
                }
                stmt.bindLong(6, value.effectTime);
                stmt.bindLong(7, value.expireTime);
                if (value.resourceFrom == null) {
                    stmt.bindNull(8);
                } else {
                    stmt.bindString(8, value.resourceFrom);
                }
                if (value.extraInfo == null) {
                    stmt.bindNull(9);
                } else {
                    stmt.bindString(9, value.extraInfo);
                }
                stmt.bindLong(10, value.status);
                stmt.bindLong(11, value.createTime);
                stmt.bindLong(12, value.updateTime);
                if (value.resourceIcon == null) {
                    stmt.bindNull(13);
                } else {
                    stmt.bindString(13, value.resourceIcon);
                }
                if (value.description == null) {
                    stmt.bindNull(14);
                } else {
                    stmt.bindString(14, value.description);
                }
                if (value.price == null) {
                    stmt.bindNull(15);
                } else {
                    stmt.bindString(15, value.price);
                }
            }
        };
        this.__deletionAdapterOfResourceInfo = new EntityDeletionOrUpdateAdapter<ResourceInfo>(__db) { // from class: com.xiaopeng.xuiservice.operation.db.ResourceDao_Impl.2
            @Override // androidx.room.EntityDeletionOrUpdateAdapter, androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM `ResourceInfo` WHERE `resourceId` = ?";
            }

            @Override // androidx.room.EntityDeletionOrUpdateAdapter
            public void bind(SupportSQLiteStatement stmt, ResourceInfo value) {
                if (value.resourceId == null) {
                    stmt.bindNull(1);
                } else {
                    stmt.bindString(1, value.resourceId);
                }
            }
        };
        this.__preparedStmtOfUpdateResourceStatus = new SharedSQLiteStatement(__db) { // from class: com.xiaopeng.xuiservice.operation.db.ResourceDao_Impl.3
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE resourceinfo SET status = ? WHERE resourceId = ?";
            }
        };
    }

    @Override // com.xiaopeng.xuiservice.operation.db.ResourceDao
    public void insertResources(ResourceInfo... info) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfResourceInfo.insert((Object[]) info);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.xiaopeng.xuiservice.operation.db.ResourceDao
    public void insertResource(ResourceInfo info) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfResourceInfo.insert((EntityInsertionAdapter) info);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.xiaopeng.xuiservice.operation.db.ResourceDao
    public void deleteResources(ResourceInfo... info) {
        this.__db.beginTransaction();
        try {
            this.__deletionAdapterOfResourceInfo.handleMultiple(info);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.xiaopeng.xuiservice.operation.db.ResourceDao
    public void deleteResource(ResourceInfo info) {
        this.__db.beginTransaction();
        try {
            this.__deletionAdapterOfResourceInfo.handle(info);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.xiaopeng.xuiservice.operation.db.ResourceDao
    public void updateResourceStatus(String id, int status) {
        SupportSQLiteStatement _stmt = this.__preparedStmtOfUpdateResourceStatus.acquire();
        this.__db.beginTransaction();
        try {
            _stmt.bindLong(1, status);
            if (id == null) {
                _stmt.bindNull(2);
            } else {
                _stmt.bindString(2, id);
            }
            _stmt.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfUpdateResourceStatus.release(_stmt);
        }
    }

    @Override // com.xiaopeng.xuiservice.operation.db.ResourceDao
    public List<ResourceInfo> queryAllResource() {
        RoomSQLiteQuery _statement;
        RoomSQLiteQuery _statement2 = RoomSQLiteQuery.acquire("SELECT * FROM resourceinfo", 0);
        Cursor _cursor = this.__db.query(_statement2);
        try {
            int _cursorIndexOfResourceId = _cursor.getColumnIndexOrThrow("resourceId");
            int _cursorIndexOfDescription = _cursor.getColumnIndexOrThrow("resourceType");
            int _cursorIndexOfPrice = _cursor.getColumnIndexOrThrow("resourceName");
            int _cursorIndexOfTargetPath = _cursor.getColumnIndexOrThrow("targetPath");
            int _cursorIndexOfMd5Hash = _cursor.getColumnIndexOrThrow("md5Hash");
            int _cursorIndexOfEffectTime = _cursor.getColumnIndexOrThrow("effectTime");
            int _cursorIndexOfExpireTime = _cursor.getColumnIndexOrThrow("expireTime");
            int _cursorIndexOfResourceFrom = _cursor.getColumnIndexOrThrow("resourceFrom");
            int _cursorIndexOfExtraInfo = _cursor.getColumnIndexOrThrow("extraInfo");
            int _cursorIndexOfStatus = _cursor.getColumnIndexOrThrow("status");
            int _cursorIndexOfCreateTime = _cursor.getColumnIndexOrThrow("createTime");
            int _cursorIndexOfUpdateTime = _cursor.getColumnIndexOrThrow("updateTime");
            try {
                int _cursorIndexOfResourceIcon = _cursor.getColumnIndexOrThrow("resourceIcon");
                int _cursorIndexOfDescription2 = _cursor.getColumnIndexOrThrow("description");
                _statement = _statement2;
                try {
                    int _cursorIndexOfPrice2 = _cursor.getColumnIndexOrThrow("price");
                    int _cursorIndexOfResourceId2 = _cursorIndexOfPrice2;
                    int _cursorIndexOfDescription3 = _cursorIndexOfDescription2;
                    int _cursorIndexOfDescription4 = _cursor.getCount();
                    List<ResourceInfo> _result = new ArrayList<>(_cursorIndexOfDescription4);
                    while (_cursor.moveToNext()) {
                        ResourceInfo _item = new ResourceInfo();
                        List<ResourceInfo> _result2 = _result;
                        _item.resourceId = _cursor.getString(_cursorIndexOfResourceId);
                        _item.resourceType = _cursor.getInt(_cursorIndexOfDescription);
                        _item.resourceName = _cursor.getString(_cursorIndexOfPrice);
                        _item.targetPath = _cursor.getString(_cursorIndexOfTargetPath);
                        _item.md5Hash = _cursor.getString(_cursorIndexOfMd5Hash);
                        int _cursorIndexOfResourceType = _cursorIndexOfDescription;
                        int _cursorIndexOfResourceName = _cursorIndexOfPrice;
                        _item.effectTime = _cursor.getLong(_cursorIndexOfEffectTime);
                        _item.expireTime = _cursor.getLong(_cursorIndexOfExpireTime);
                        _item.resourceFrom = _cursor.getString(_cursorIndexOfResourceFrom);
                        _item.extraInfo = _cursor.getString(_cursorIndexOfExtraInfo);
                        _item.status = _cursor.getInt(_cursorIndexOfStatus);
                        _item.createTime = _cursor.getLong(_cursorIndexOfCreateTime);
                        _item.updateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
                        _item.resourceIcon = _cursor.getString(_cursorIndexOfResourceIcon);
                        int _cursorIndexOfDescription5 = _cursorIndexOfDescription3;
                        _item.description = _cursor.getString(_cursorIndexOfDescription5);
                        int _cursorIndexOfPrice3 = _cursorIndexOfResourceId2;
                        int _cursorIndexOfPrice4 = _cursorIndexOfResourceId;
                        _item.price = _cursor.getString(_cursorIndexOfPrice3);
                        _result2.add(_item);
                        _cursorIndexOfDescription3 = _cursorIndexOfDescription5;
                        _cursorIndexOfDescription = _cursorIndexOfResourceType;
                        _result = _result2;
                        _cursorIndexOfResourceId = _cursorIndexOfPrice4;
                        _cursorIndexOfResourceId2 = _cursorIndexOfPrice3;
                        _cursorIndexOfPrice = _cursorIndexOfResourceName;
                    }
                    List<ResourceInfo> _result3 = _result;
                    _cursor.close();
                    _statement.release();
                    return _result3;
                } catch (Throwable th) {
                    th = th;
                    _cursor.close();
                    _statement.release();
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                _statement = _statement2;
            }
        } catch (Throwable th3) {
            th = th3;
            _statement = _statement2;
        }
    }

    @Override // com.xiaopeng.xuiservice.operation.db.ResourceDao
    public List<ResourceInfo> queryResourceByType(int resourceType) {
        RoomSQLiteQuery _statement;
        RoomSQLiteQuery _statement2 = RoomSQLiteQuery.acquire("SELECT * FROM resourceinfo WHERE resourceType = ?", 1);
        _statement2.bindLong(1, resourceType);
        Cursor _cursor = this.__db.query(_statement2);
        try {
            int _cursorIndexOfResourceId = _cursor.getColumnIndexOrThrow("resourceId");
            int _cursorIndexOfDescription = _cursor.getColumnIndexOrThrow("resourceType");
            int _cursorIndexOfPrice = _cursor.getColumnIndexOrThrow("resourceName");
            int _cursorIndexOfTargetPath = _cursor.getColumnIndexOrThrow("targetPath");
            int _cursorIndexOfMd5Hash = _cursor.getColumnIndexOrThrow("md5Hash");
            int _cursorIndexOfEffectTime = _cursor.getColumnIndexOrThrow("effectTime");
            int _cursorIndexOfExpireTime = _cursor.getColumnIndexOrThrow("expireTime");
            int _cursorIndexOfResourceFrom = _cursor.getColumnIndexOrThrow("resourceFrom");
            int _cursorIndexOfExtraInfo = _cursor.getColumnIndexOrThrow("extraInfo");
            int _cursorIndexOfStatus = _cursor.getColumnIndexOrThrow("status");
            try {
                int _cursorIndexOfCreateTime = _cursor.getColumnIndexOrThrow("createTime");
                try {
                    int _cursorIndexOfUpdateTime = _cursor.getColumnIndexOrThrow("updateTime");
                    int _cursorIndexOfResourceIcon = _cursor.getColumnIndexOrThrow("resourceIcon");
                    int _cursorIndexOfDescription2 = _cursor.getColumnIndexOrThrow("description");
                    _statement = _statement2;
                    try {
                        int _cursorIndexOfPrice2 = _cursor.getColumnIndexOrThrow("price");
                        int _cursorIndexOfResourceId2 = _cursorIndexOfPrice2;
                        int _cursorIndexOfDescription3 = _cursorIndexOfDescription2;
                        int _cursorIndexOfDescription4 = _cursor.getCount();
                        List<ResourceInfo> _result = new ArrayList<>(_cursorIndexOfDescription4);
                        while (_cursor.moveToNext()) {
                            ResourceInfo _item = new ResourceInfo();
                            List<ResourceInfo> _result2 = _result;
                            _item.resourceId = _cursor.getString(_cursorIndexOfResourceId);
                            _item.resourceType = _cursor.getInt(_cursorIndexOfDescription);
                            _item.resourceName = _cursor.getString(_cursorIndexOfPrice);
                            _item.targetPath = _cursor.getString(_cursorIndexOfTargetPath);
                            _item.md5Hash = _cursor.getString(_cursorIndexOfMd5Hash);
                            int _cursorIndexOfResourceType = _cursorIndexOfDescription;
                            int _cursorIndexOfResourceName = _cursorIndexOfPrice;
                            _item.effectTime = _cursor.getLong(_cursorIndexOfEffectTime);
                            _item.expireTime = _cursor.getLong(_cursorIndexOfExpireTime);
                            _item.resourceFrom = _cursor.getString(_cursorIndexOfResourceFrom);
                            _item.extraInfo = _cursor.getString(_cursorIndexOfExtraInfo);
                            _item.status = _cursor.getInt(_cursorIndexOfStatus);
                            _item.createTime = _cursor.getLong(_cursorIndexOfCreateTime);
                            _item.updateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
                            _item.resourceIcon = _cursor.getString(_cursorIndexOfResourceIcon);
                            int _cursorIndexOfDescription5 = _cursorIndexOfDescription3;
                            _item.description = _cursor.getString(_cursorIndexOfDescription5);
                            int _cursorIndexOfPrice3 = _cursorIndexOfResourceId2;
                            int _cursorIndexOfPrice4 = _cursorIndexOfResourceId;
                            _item.price = _cursor.getString(_cursorIndexOfPrice3);
                            _result2.add(_item);
                            _cursorIndexOfDescription3 = _cursorIndexOfDescription5;
                            _cursorIndexOfDescription = _cursorIndexOfResourceType;
                            _result = _result2;
                            _cursorIndexOfResourceId = _cursorIndexOfPrice4;
                            _cursorIndexOfResourceId2 = _cursorIndexOfPrice3;
                            _cursorIndexOfPrice = _cursorIndexOfResourceName;
                        }
                        List<ResourceInfo> _result3 = _result;
                        _cursor.close();
                        _statement.release();
                        return _result3;
                    } catch (Throwable th) {
                        th = th;
                        _cursor.close();
                        _statement.release();
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    _statement = _statement2;
                }
            } catch (Throwable th3) {
                th = th3;
                _statement = _statement2;
            }
        } catch (Throwable th4) {
            th = th4;
            _statement = _statement2;
        }
    }

    @Override // com.xiaopeng.xuiservice.operation.db.ResourceDao
    public List<ResourceInfo> queryResourceByTypeAndFrom(int resourceType, String resourceFrom) {
        RoomSQLiteQuery _statement;
        int _cursorIndexOfResourceId;
        int _cursorIndexOfDescription;
        int _cursorIndexOfPrice;
        int _cursorIndexOfTargetPath;
        int _cursorIndexOfMd5Hash;
        int _cursorIndexOfEffectTime;
        int _cursorIndexOfExpireTime;
        int _cursorIndexOfResourceFrom;
        int _cursorIndexOfExtraInfo;
        int _cursorIndexOfStatus;
        int _cursorIndexOfCreateTime;
        int _cursorIndexOfUpdateTime;
        int _cursorIndexOfResourceIcon;
        int _cursorIndexOfDescription2;
        RoomSQLiteQuery _statement2 = RoomSQLiteQuery.acquire("SELECT * FROM resourceinfo WHERE resourceType = ? AND resourceFrom = ? ", 2);
        _statement2.bindLong(1, resourceType);
        if (resourceFrom == null) {
            _statement2.bindNull(2);
        } else {
            _statement2.bindString(2, resourceFrom);
        }
        Cursor _cursor = this.__db.query(_statement2);
        try {
            _cursorIndexOfResourceId = _cursor.getColumnIndexOrThrow("resourceId");
            _cursorIndexOfDescription = _cursor.getColumnIndexOrThrow("resourceType");
            _cursorIndexOfPrice = _cursor.getColumnIndexOrThrow("resourceName");
            _cursorIndexOfTargetPath = _cursor.getColumnIndexOrThrow("targetPath");
            _cursorIndexOfMd5Hash = _cursor.getColumnIndexOrThrow("md5Hash");
            _cursorIndexOfEffectTime = _cursor.getColumnIndexOrThrow("effectTime");
            _cursorIndexOfExpireTime = _cursor.getColumnIndexOrThrow("expireTime");
            _cursorIndexOfResourceFrom = _cursor.getColumnIndexOrThrow("resourceFrom");
            _cursorIndexOfExtraInfo = _cursor.getColumnIndexOrThrow("extraInfo");
            _cursorIndexOfStatus = _cursor.getColumnIndexOrThrow("status");
            try {
                _cursorIndexOfCreateTime = _cursor.getColumnIndexOrThrow("createTime");
                _cursorIndexOfUpdateTime = _cursor.getColumnIndexOrThrow("updateTime");
                try {
                    _cursorIndexOfResourceIcon = _cursor.getColumnIndexOrThrow("resourceIcon");
                    _cursorIndexOfDescription2 = _cursor.getColumnIndexOrThrow("description");
                    _statement = _statement2;
                } catch (Throwable th) {
                    th = th;
                    _statement = _statement2;
                }
            } catch (Throwable th2) {
                th = th2;
                _statement = _statement2;
            }
        } catch (Throwable th3) {
            th = th3;
            _statement = _statement2;
        }
        try {
            int _cursorIndexOfPrice2 = _cursor.getColumnIndexOrThrow("price");
            int _cursorIndexOfResourceId2 = _cursorIndexOfPrice2;
            int _cursorIndexOfDescription3 = _cursorIndexOfDescription2;
            int _cursorIndexOfDescription4 = _cursor.getCount();
            List<ResourceInfo> _result = new ArrayList<>(_cursorIndexOfDescription4);
            while (_cursor.moveToNext()) {
                ResourceInfo _item = new ResourceInfo();
                List<ResourceInfo> _result2 = _result;
                _item.resourceId = _cursor.getString(_cursorIndexOfResourceId);
                _item.resourceType = _cursor.getInt(_cursorIndexOfDescription);
                _item.resourceName = _cursor.getString(_cursorIndexOfPrice);
                _item.targetPath = _cursor.getString(_cursorIndexOfTargetPath);
                _item.md5Hash = _cursor.getString(_cursorIndexOfMd5Hash);
                int _cursorIndexOfResourceType = _cursorIndexOfDescription;
                int _cursorIndexOfResourceName = _cursorIndexOfPrice;
                _item.effectTime = _cursor.getLong(_cursorIndexOfEffectTime);
                _item.expireTime = _cursor.getLong(_cursorIndexOfExpireTime);
                _item.resourceFrom = _cursor.getString(_cursorIndexOfResourceFrom);
                _item.extraInfo = _cursor.getString(_cursorIndexOfExtraInfo);
                _item.status = _cursor.getInt(_cursorIndexOfStatus);
                _item.createTime = _cursor.getLong(_cursorIndexOfCreateTime);
                _item.updateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
                _item.resourceIcon = _cursor.getString(_cursorIndexOfResourceIcon);
                int _cursorIndexOfDescription5 = _cursorIndexOfDescription3;
                _item.description = _cursor.getString(_cursorIndexOfDescription5);
                int _cursorIndexOfPrice3 = _cursorIndexOfResourceId2;
                int _cursorIndexOfPrice4 = _cursorIndexOfResourceId;
                _item.price = _cursor.getString(_cursorIndexOfPrice3);
                _result2.add(_item);
                _cursorIndexOfDescription3 = _cursorIndexOfDescription5;
                _cursorIndexOfDescription = _cursorIndexOfResourceType;
                _result = _result2;
                _cursorIndexOfResourceId = _cursorIndexOfPrice4;
                _cursorIndexOfResourceId2 = _cursorIndexOfPrice3;
                _cursorIndexOfPrice = _cursorIndexOfResourceName;
            }
            List<ResourceInfo> _result3 = _result;
            _cursor.close();
            _statement.release();
            return _result3;
        } catch (Throwable th4) {
            th = th4;
            _cursor.close();
            _statement.release();
            throw th;
        }
    }

    @Override // com.xiaopeng.xuiservice.operation.db.ResourceDao
    public ResourceInfo queryResourceById(String id) {
        RoomSQLiteQuery _statement;
        ResourceInfo _result;
        RoomSQLiteQuery _statement2 = RoomSQLiteQuery.acquire("SELECT * FROM resourceinfo WHERE resourceId = ?", 1);
        if (id == null) {
            _statement2.bindNull(1);
        } else {
            _statement2.bindString(1, id);
        }
        Cursor _cursor = this.__db.query(_statement2);
        try {
            int _cursorIndexOfResourceId = _cursor.getColumnIndexOrThrow("resourceId");
            int _cursorIndexOfResourceType = _cursor.getColumnIndexOrThrow("resourceType");
            int _cursorIndexOfResourceName = _cursor.getColumnIndexOrThrow("resourceName");
            int _cursorIndexOfTargetPath = _cursor.getColumnIndexOrThrow("targetPath");
            int _cursorIndexOfMd5Hash = _cursor.getColumnIndexOrThrow("md5Hash");
            int _cursorIndexOfEffectTime = _cursor.getColumnIndexOrThrow("effectTime");
            int _cursorIndexOfExpireTime = _cursor.getColumnIndexOrThrow("expireTime");
            int _cursorIndexOfResourceFrom = _cursor.getColumnIndexOrThrow("resourceFrom");
            int _cursorIndexOfExtraInfo = _cursor.getColumnIndexOrThrow("extraInfo");
            int _cursorIndexOfStatus = _cursor.getColumnIndexOrThrow("status");
            int _cursorIndexOfCreateTime = _cursor.getColumnIndexOrThrow("createTime");
            try {
                int _cursorIndexOfUpdateTime = _cursor.getColumnIndexOrThrow("updateTime");
                try {
                    int _cursorIndexOfResourceIcon = _cursor.getColumnIndexOrThrow("resourceIcon");
                    int _cursorIndexOfDescription = _cursor.getColumnIndexOrThrow("description");
                    _statement = _statement2;
                    try {
                        int _cursorIndexOfPrice = _cursor.getColumnIndexOrThrow("price");
                        if (_cursor.moveToFirst()) {
                            ResourceInfo _result2 = new ResourceInfo();
                            String string = _cursor.getString(_cursorIndexOfResourceId);
                            _result = _result2;
                            _result.resourceId = string;
                            _result.resourceType = _cursor.getInt(_cursorIndexOfResourceType);
                            _result.resourceName = _cursor.getString(_cursorIndexOfResourceName);
                            _result.targetPath = _cursor.getString(_cursorIndexOfTargetPath);
                            _result.md5Hash = _cursor.getString(_cursorIndexOfMd5Hash);
                            _result.effectTime = _cursor.getLong(_cursorIndexOfEffectTime);
                            _result.expireTime = _cursor.getLong(_cursorIndexOfExpireTime);
                            _result.resourceFrom = _cursor.getString(_cursorIndexOfResourceFrom);
                            _result.extraInfo = _cursor.getString(_cursorIndexOfExtraInfo);
                            _result.status = _cursor.getInt(_cursorIndexOfStatus);
                            _result.createTime = _cursor.getLong(_cursorIndexOfCreateTime);
                            _result.updateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
                            _result.resourceIcon = _cursor.getString(_cursorIndexOfResourceIcon);
                            _result.description = _cursor.getString(_cursorIndexOfDescription);
                            _result.price = _cursor.getString(_cursorIndexOfPrice);
                        } else {
                            _result = null;
                        }
                        _cursor.close();
                        _statement.release();
                        return _result;
                    } catch (Throwable th) {
                        th = th;
                        _cursor.close();
                        _statement.release();
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    _statement = _statement2;
                }
            } catch (Throwable th3) {
                th = th3;
                _statement = _statement2;
            }
        } catch (Throwable th4) {
            th = th4;
            _statement = _statement2;
        }
    }
}
