package com.xiaopeng.xuiservice.operation.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
@Dao
/* loaded from: classes5.dex */
public interface SelectedDao {
    @Delete
    void deleteSelectedInfo(SelectedInfo selectedInfo);

    @Insert(onConflict = 1)
    void insertSelectInfo(SelectedInfo selectedInfo);

    @Query("SELECT * FROM selectedinfo WHERE resourceType = :resourceType")
    SelectedInfo querySelectedByType(int i);
}
