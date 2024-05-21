package com.xiaopeng.lib.framework.locationmodule;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;
import com.xiaopeng.lib.framework.locationmodule.common.Debug;
import com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation;
@Keep
/* loaded from: classes.dex */
public final class LocationImpl implements ILocation {
    public static final Parcelable.Creator<LocationImpl> CREATOR = new Parcelable.Creator<LocationImpl>() { // from class: com.xiaopeng.lib.framework.locationmodule.LocationImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LocationImpl createFromParcel(Parcel in) {
            return new LocationImpl(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LocationImpl[] newArray(int size) {
            return new LocationImpl[size];
        }
    };
    private static final boolean DEBUG = false;
    private int mAccuracy;
    private String mAdCode;
    private int mAltitude;
    private int mAngle;
    private ILocation.Category mCategory;
    private String mCity;
    private float mLatitude;
    private float mLongitude;
    private boolean mMutable;
    private float mRawLatitude;
    private float mRawLongitude;
    private int mSatellites;
    private int mSourceType;
    private float mSpeed;
    private long mTime;

    public LocationImpl() {
        this.mMutable = true;
    }

    private LocationImpl(Parcel in) {
        readFromParcel(in);
        this.mMutable = false;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public LocationImpl category(ILocation.Category value) {
        checkMutable();
        this.mCategory = value;
        return this;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public LocationImpl latitude(float value) {
        checkMutable();
        this.mLatitude = value;
        return this;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public LocationImpl longitude(float value) {
        checkMutable();
        this.mLongitude = value;
        return this;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public LocationImpl rawLatitude(float value) {
        checkMutable();
        this.mRawLatitude = value;
        return this;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public LocationImpl rawLongitude(float value) {
        checkMutable();
        this.mRawLongitude = value;
        return this;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public LocationImpl speed(float value) {
        checkMutable();
        this.mSpeed = value;
        return this;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public LocationImpl angle(int value) {
        checkMutable();
        this.mAngle = value;
        return this;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public ILocation accuracy(int value) {
        checkMutable();
        this.mAccuracy = value;
        return this;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public ILocation satellites(int value) {
        checkMutable();
        this.mSatellites = value;
        return this;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public ILocation altitude(int value) {
        checkMutable();
        this.mAltitude = value;
        return this;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public ILocation time(long value) {
        checkMutable();
        this.mTime = value;
        return this;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public ILocation sourceType(int value) {
        checkMutable();
        this.mSourceType = value;
        return this;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public ILocation city(String value) {
        checkMutable();
        this.mCity = value;
        return this;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public ILocation adCode(String value) {
        checkMutable();
        this.mAdCode = value;
        return this;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public ILocation.Category category() {
        return this.mCategory;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public float latitude() {
        return this.mLatitude;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public float longitude() {
        return this.mLongitude;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public float rawLatitude() {
        return this.mRawLatitude;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public float rawLongitude() {
        return this.mRawLongitude;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public float speed() {
        return this.mSpeed;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public int angle() {
        return this.mAngle;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public int accuracy() {
        return this.mAccuracy;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public int satellites() {
        return this.mSatellites;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public int altitude() {
        return this.mAltitude;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public long time() {
        return this.mTime;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public int sourceType() {
        return this.mSourceType;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public String city() {
        return this.mCity;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation
    public String adCode() {
        return this.mAdCode;
    }

    public void seal() {
        this.mMutable = false;
    }

    private void checkMutable() {
        if (!this.mMutable) {
            throw new RuntimeException("Not allow any modification if sealed!");
        }
    }

    public String toString() {
        return "<HIDDEN>";
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel arg0, int arg1) {
        arg0.writeInt(this.mCategory.ordinal());
        arg0.writeFloat(this.mLatitude);
        arg0.writeFloat(this.mLongitude);
        arg0.writeFloat(this.mRawLatitude);
        arg0.writeFloat(this.mRawLongitude);
        arg0.writeFloat(this.mSpeed);
        arg0.writeInt(this.mAngle);
        arg0.writeInt(this.mAccuracy);
        arg0.writeInt(this.mSatellites);
        arg0.writeInt(this.mAltitude);
        arg0.writeLong(this.mTime);
        arg0.writeInt(this.mSourceType);
        arg0.writeString(this.mCity);
        arg0.writeString(this.mAdCode);
    }

    private void readFromParcel(Parcel in) {
        int indexOfCategory = in.readInt();
        if (indexOfCategory < ILocation.Category.values().length) {
            this.mCategory = ILocation.Category.values()[indexOfCategory];
        } else {
            Debug.assertion(false, "Wrong Location Data!!!");
        }
        this.mLatitude = in.readFloat();
        this.mLongitude = in.readFloat();
        this.mRawLatitude = in.readFloat();
        this.mRawLongitude = in.readFloat();
        this.mSpeed = in.readFloat();
        this.mAngle = in.readInt();
        this.mAccuracy = in.readInt();
        this.mSatellites = in.readInt();
        this.mAltitude = in.readInt();
        this.mTime = in.readLong();
        this.mSourceType = in.readInt();
        this.mCity = in.readString();
        try {
            this.mAdCode = in.readString();
        } catch (Exception e) {
            this.mAdCode = "";
        }
    }
}
