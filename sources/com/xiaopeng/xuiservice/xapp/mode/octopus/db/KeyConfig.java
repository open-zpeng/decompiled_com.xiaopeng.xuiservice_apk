package com.xiaopeng.xuiservice.xapp.mode.octopus.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import com.xiaopeng.speech.speechwidget.SpeechWidget;
@Entity(primaryKeys = {"pkgName", "configName"})
/* loaded from: classes5.dex */
public class KeyConfig {
    @ColumnInfo(name = "configInfo")
    public String configInfo;
    @NonNull
    @ColumnInfo(name = "configName")
    public String configName;
    @NonNull
    @ColumnInfo(name = "pkgName")
    public String pkgName;
    @ColumnInfo(name = SpeechWidget.DATA_SOURCE_SELECT)
    public int selected;

    public String toString() {
        return "KeyConfig{pkgName='" + this.pkgName + "', configName='" + this.configName + "', selected=" + this.selected + ", configInfo='" + this.configInfo + "'}";
    }
}
