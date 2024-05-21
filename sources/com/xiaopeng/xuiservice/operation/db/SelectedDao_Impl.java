package com.xiaopeng.xuiservice.operation.db;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteStatement;
/* loaded from: classes5.dex */
public class SelectedDao_Impl implements SelectedDao {
    private final RoomDatabase __db;
    private final EntityDeletionOrUpdateAdapter __deletionAdapterOfSelectedInfo;
    private final EntityInsertionAdapter __insertionAdapterOfSelectedInfo;

    public SelectedDao_Impl(RoomDatabase __db) {
        this.__db = __db;
        this.__insertionAdapterOfSelectedInfo = new EntityInsertionAdapter<SelectedInfo>(__db) { // from class: com.xiaopeng.xuiservice.operation.db.SelectedDao_Impl.1
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "INSERT OR REPLACE INTO `SelectedInfo`(`resourceType`,`resourceId`) VALUES (?,?)";
            }

            @Override // androidx.room.EntityInsertionAdapter
            public void bind(SupportSQLiteStatement stmt, SelectedInfo value) {
                stmt.bindLong(1, value.resourceType);
                if (value.resourceId == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.resourceId);
                }
            }
        };
        this.__deletionAdapterOfSelectedInfo = new EntityDeletionOrUpdateAdapter<SelectedInfo>(__db) { // from class: com.xiaopeng.xuiservice.operation.db.SelectedDao_Impl.2
            @Override // androidx.room.EntityDeletionOrUpdateAdapter, androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM `SelectedInfo` WHERE `resourceType` = ?";
            }

            @Override // androidx.room.EntityDeletionOrUpdateAdapter
            public void bind(SupportSQLiteStatement stmt, SelectedInfo value) {
                stmt.bindLong(1, value.resourceType);
            }
        };
    }

    @Override // com.xiaopeng.xuiservice.operation.db.SelectedDao
    public void insertSelectInfo(SelectedInfo info) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfSelectedInfo.insert((EntityInsertionAdapter) info);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.xiaopeng.xuiservice.operation.db.SelectedDao
    public void deleteSelectedInfo(SelectedInfo info) {
        this.__db.beginTransaction();
        try {
            this.__deletionAdapterOfSelectedInfo.handle(info);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.xiaopeng.xuiservice.operation.db.SelectedDao
    public SelectedInfo querySelectedByType(int resourceType) {
        SelectedInfo _result;
        RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire("SELECT * FROM selectedinfo WHERE resourceType = ?", 1);
        _statement.bindLong(1, resourceType);
        Cursor _cursor = this.__db.query(_statement);
        try {
            int _cursorIndexOfResourceType = _cursor.getColumnIndexOrThrow("resourceType");
            int _cursorIndexOfResourceId = _cursor.getColumnIndexOrThrow("resourceId");
            if (_cursor.moveToFirst()) {
                _result = new SelectedInfo();
                _result.resourceType = _cursor.getInt(_cursorIndexOfResourceType);
                _result.resourceId = _cursor.getString(_cursorIndexOfResourceId);
            } else {
                _result = null;
            }
            return _result;
        } finally {
            _cursor.close();
            _statement.release();
        }
    }
}
