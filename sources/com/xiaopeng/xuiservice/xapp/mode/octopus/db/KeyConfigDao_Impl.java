package com.xiaopeng.xuiservice.xapp.mode.octopus.db;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.xiaopeng.speech.speechwidget.SpeechWidget;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes5.dex */
public class KeyConfigDao_Impl implements KeyConfigDao {
    private final RoomDatabase __db;
    private final EntityInsertionAdapter __insertionAdapterOfKeyConfig;

    public KeyConfigDao_Impl(RoomDatabase __db) {
        this.__db = __db;
        this.__insertionAdapterOfKeyConfig = new EntityInsertionAdapter<KeyConfig>(__db) { // from class: com.xiaopeng.xuiservice.xapp.mode.octopus.db.KeyConfigDao_Impl.1
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "INSERT OR REPLACE INTO `KeyConfig`(`pkgName`,`configName`,`selected`,`configInfo`) VALUES (?,?,?,?)";
            }

            @Override // androidx.room.EntityInsertionAdapter
            public void bind(SupportSQLiteStatement stmt, KeyConfig value) {
                if (value.pkgName == null) {
                    stmt.bindNull(1);
                } else {
                    stmt.bindString(1, value.pkgName);
                }
                if (value.configName == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.configName);
                }
                stmt.bindLong(3, value.selected);
                if (value.configInfo == null) {
                    stmt.bindNull(4);
                } else {
                    stmt.bindString(4, value.configInfo);
                }
            }
        };
    }

    @Override // com.xiaopeng.xuiservice.xapp.mode.octopus.db.KeyConfigDao
    public void insertConfigs(List<KeyConfig> configs) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfKeyConfig.insert((Iterable) configs);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.xiaopeng.xuiservice.xapp.mode.octopus.db.KeyConfigDao
    public void insertConfig(KeyConfig config) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfKeyConfig.insert((EntityInsertionAdapter) config);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.xiaopeng.xuiservice.xapp.mode.octopus.db.KeyConfigDao
    public List<KeyConfig> findConfigsByPkg(String pkgName) {
        RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire("SELECT * FROM keyconfig WHERE pkgName LIKE ?", 1);
        if (pkgName == null) {
            _statement.bindNull(1);
        } else {
            _statement.bindString(1, pkgName);
        }
        Cursor _cursor = this.__db.query(_statement);
        try {
            int _cursorIndexOfPkgName = _cursor.getColumnIndexOrThrow("pkgName");
            int _cursorIndexOfConfigName = _cursor.getColumnIndexOrThrow("configName");
            int _cursorIndexOfSelected = _cursor.getColumnIndexOrThrow(SpeechWidget.DATA_SOURCE_SELECT);
            int _cursorIndexOfConfigInfo = _cursor.getColumnIndexOrThrow("configInfo");
            List<KeyConfig> _result = new ArrayList<>(_cursor.getCount());
            while (_cursor.moveToNext()) {
                KeyConfig _item = new KeyConfig();
                _item.pkgName = _cursor.getString(_cursorIndexOfPkgName);
                _item.configName = _cursor.getString(_cursorIndexOfConfigName);
                _item.selected = _cursor.getInt(_cursorIndexOfSelected);
                _item.configInfo = _cursor.getString(_cursorIndexOfConfigInfo);
                _result.add(_item);
            }
            return _result;
        } finally {
            _cursor.close();
            _statement.release();
        }
    }

    @Override // com.xiaopeng.xuiservice.xapp.mode.octopus.db.KeyConfigDao
    public KeyConfig findByName(String pkgName, String configName) {
        KeyConfig _result;
        RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire("SELECT * FROM keyconfig WHERE pkgName LIKE ? AND configName LIKE ? LIMIT 1", 2);
        if (pkgName == null) {
            _statement.bindNull(1);
        } else {
            _statement.bindString(1, pkgName);
        }
        if (configName == null) {
            _statement.bindNull(2);
        } else {
            _statement.bindString(2, configName);
        }
        Cursor _cursor = this.__db.query(_statement);
        try {
            int _cursorIndexOfPkgName = _cursor.getColumnIndexOrThrow("pkgName");
            int _cursorIndexOfConfigName = _cursor.getColumnIndexOrThrow("configName");
            int _cursorIndexOfSelected = _cursor.getColumnIndexOrThrow(SpeechWidget.DATA_SOURCE_SELECT);
            int _cursorIndexOfConfigInfo = _cursor.getColumnIndexOrThrow("configInfo");
            if (_cursor.moveToFirst()) {
                _result = new KeyConfig();
                _result.pkgName = _cursor.getString(_cursorIndexOfPkgName);
                _result.configName = _cursor.getString(_cursorIndexOfConfigName);
                _result.selected = _cursor.getInt(_cursorIndexOfSelected);
                _result.configInfo = _cursor.getString(_cursorIndexOfConfigInfo);
            } else {
                _result = null;
            }
            return _result;
        } finally {
            _cursor.close();
            _statement.release();
        }
    }

    @Override // com.xiaopeng.xuiservice.xapp.mode.octopus.db.KeyConfigDao
    public KeyConfig findSelected(String pkgName) {
        KeyConfig _result;
        RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire("SELECT * FROM keyconfig WHERE pkgName LIKE ? AND selected = 1 LIMIT 1", 1);
        if (pkgName == null) {
            _statement.bindNull(1);
        } else {
            _statement.bindString(1, pkgName);
        }
        Cursor _cursor = this.__db.query(_statement);
        try {
            int _cursorIndexOfPkgName = _cursor.getColumnIndexOrThrow("pkgName");
            int _cursorIndexOfConfigName = _cursor.getColumnIndexOrThrow("configName");
            int _cursorIndexOfSelected = _cursor.getColumnIndexOrThrow(SpeechWidget.DATA_SOURCE_SELECT);
            int _cursorIndexOfConfigInfo = _cursor.getColumnIndexOrThrow("configInfo");
            if (_cursor.moveToFirst()) {
                _result = new KeyConfig();
                _result.pkgName = _cursor.getString(_cursorIndexOfPkgName);
                _result.configName = _cursor.getString(_cursorIndexOfConfigName);
                _result.selected = _cursor.getInt(_cursorIndexOfSelected);
                _result.configInfo = _cursor.getString(_cursorIndexOfConfigInfo);
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
