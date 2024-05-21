package com.xiaopeng.xuiservice.xapp.mode.octopus.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
@Dao
/* loaded from: classes5.dex */
public interface KeyConfigDao {
    @Query("SELECT * FROM keyconfig WHERE pkgName LIKE :pkgName AND configName LIKE :configName LIMIT 1")
    KeyConfig findByName(String str, String str2);

    @Query("SELECT * FROM keyconfig WHERE pkgName LIKE :pkgName")
    List<KeyConfig> findConfigsByPkg(String str);

    @Query("SELECT * FROM keyconfig WHERE pkgName LIKE :pkgName AND selected = 1 LIMIT 1")
    KeyConfig findSelected(String str);

    @Insert(onConflict = 1)
    void insertConfig(KeyConfig keyConfig);

    @Insert(onConflict = 1)
    void insertConfigs(List<KeyConfig> list);
}
