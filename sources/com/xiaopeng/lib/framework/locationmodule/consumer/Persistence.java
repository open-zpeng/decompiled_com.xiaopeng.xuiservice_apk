package com.xiaopeng.lib.framework.locationmodule.consumer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.xiaopeng.lib.framework.locationmodule.LocationImpl;
import com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class Persistence {
    private static final String KEY_ACCURACY = "LASTLOCATION_ACCURACY";
    private static final String KEY_ACCURATE_SPEED = "LASTLOCATION_ACCU_SPEED";
    private static final String KEY_ADCODE = "LASTLOCATION_ADCODE";
    private static final String KEY_ALTITUDE = "LASTLOCATION_ALTITUDE";
    private static final String KEY_ANGLE = "LASTLOCATION_ANGLE";
    private static final String KEY_CATEGORY = "LASTLOCATION_CATEGORY";
    private static final String KEY_CITY = "LASTLOCATION_CITY";
    private static final String KEY_LATITUDE = "LASTLOCATION_LATITUDE";
    private static final String KEY_LONGITUDE = "LASTLOCATION_LONGITUDE";
    private static final String KEY_RAW_LATITUDE = "LASTLOCATION_RAWLATITUDE";
    private static final String KEY_RAW_LONGITUDE = "LASTLOCATION_RAWLONGITUDE";
    private static final String KEY_SATELLITES = "LASTLOCATION_SATELLITES";
    private static final String KEY_SOURCETYPE = "LASTLOCATION_SOURCETYPE";
    private static final String KEY_TIME = "LASTLOCATION_TIME";
    private SharedPreferences.Editor mSharePreferenceEditor;
    private SharedPreferences mSharedPreference;

    public Persistence(@NonNull Context context) {
        this.mSharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
        this.mSharePreferenceEditor = this.mSharedPreference.edit();
    }

    public void write(ILocation location) {
        this.mSharePreferenceEditor.putInt(KEY_CATEGORY, location.category().ordinal());
        this.mSharePreferenceEditor.putFloat(KEY_LATITUDE, location.latitude());
        this.mSharePreferenceEditor.putFloat(KEY_LONGITUDE, location.longitude());
        this.mSharePreferenceEditor.putFloat(KEY_RAW_LATITUDE, location.rawLatitude());
        this.mSharePreferenceEditor.putFloat(KEY_RAW_LONGITUDE, location.rawLongitude());
        this.mSharePreferenceEditor.putFloat(KEY_ACCURATE_SPEED, location.speed());
        this.mSharePreferenceEditor.putInt(KEY_ANGLE, location.angle());
        this.mSharePreferenceEditor.putInt(KEY_ACCURACY, location.accuracy());
        this.mSharePreferenceEditor.putInt(KEY_SATELLITES, location.satellites());
        this.mSharePreferenceEditor.putInt(KEY_ALTITUDE, location.altitude());
        this.mSharePreferenceEditor.putLong(KEY_TIME, location.time());
        this.mSharePreferenceEditor.putInt(KEY_SOURCETYPE, location.sourceType());
        this.mSharePreferenceEditor.putString(KEY_CITY, location.city());
        this.mSharePreferenceEditor.putString(KEY_ADCODE, location.adCode());
        this.mSharePreferenceEditor.apply();
    }

    @Nullable
    public ILocation read() {
        int indexOfCategory = this.mSharedPreference.getInt(KEY_CATEGORY, ILocation.Category.values().length);
        if (indexOfCategory == ILocation.Category.values().length) {
            return null;
        }
        LocationImpl location = new LocationImpl();
        location.category(ILocation.Category.values()[indexOfCategory]).latitude(this.mSharedPreference.getFloat(KEY_LATITUDE, 0.0f)).longitude(this.mSharedPreference.getFloat(KEY_LONGITUDE, 0.0f)).rawLatitude(this.mSharedPreference.getFloat(KEY_RAW_LATITUDE, 0.0f)).rawLongitude(this.mSharedPreference.getFloat(KEY_RAW_LONGITUDE, 0.0f)).speed(this.mSharedPreference.getFloat(KEY_ACCURATE_SPEED, 0.0f)).angle(this.mSharedPreference.getInt(KEY_ANGLE, 0)).accuracy(this.mSharedPreference.getInt(KEY_ACCURACY, 0)).satellites(this.mSharedPreference.getInt(KEY_SATELLITES, 0)).altitude(this.mSharedPreference.getInt(KEY_ALTITUDE, 0)).time(this.mSharedPreference.getLong(KEY_TIME, 0L)).sourceType(this.mSharedPreference.getInt(KEY_SOURCETYPE, 0)).city(this.mSharedPreference.getString(KEY_CITY, "")).adCode(this.mSharedPreference.getString(KEY_ADCODE, ""));
        location.seal();
        return location;
    }
}
