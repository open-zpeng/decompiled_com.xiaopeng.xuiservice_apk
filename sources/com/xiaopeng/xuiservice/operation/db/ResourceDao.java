package com.xiaopeng.xuiservice.operation.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
@Dao
/* loaded from: classes5.dex */
public interface ResourceDao {
    @Delete
    void deleteResource(ResourceInfo resourceInfo);

    @Delete
    void deleteResources(ResourceInfo... resourceInfoArr);

    @Insert(onConflict = 1)
    void insertResource(ResourceInfo resourceInfo);

    @Insert(onConflict = 1)
    void insertResources(ResourceInfo... resourceInfoArr);

    @Query("SELECT * FROM resourceinfo")
    List<ResourceInfo> queryAllResource();

    @Query("SELECT * FROM resourceinfo WHERE resourceId = :id")
    ResourceInfo queryResourceById(String str);

    @Query("SELECT * FROM resourceinfo WHERE resourceType = :resourceType")
    List<ResourceInfo> queryResourceByType(int i);

    @Query("SELECT * FROM resourceinfo WHERE resourceType = :resourceType AND resourceFrom = :resourceFrom ")
    List<ResourceInfo> queryResourceByTypeAndFrom(int i, String str);

    @Query("UPDATE resourceinfo SET status = :status WHERE resourceId = :id")
    void updateResourceStatus(String str, int i);
}
