package com.qiyi.video.qys;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes4.dex */
public final class IQYSString implements IQYSStringWrapper {
    private String mValue;
    public static final IQYSString EMPTY = new IQYSString("");
    public static final Parcelable.Creator<IQYSString> CREATOR = new Parcelable.Creator<IQYSString>() { // from class: com.qiyi.video.qys.IQYSString.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IQYSString createFromParcel(Parcel source) {
            return new IQYSString(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IQYSString[] newArray(int size) {
            return new IQYSString[size];
        }
    };

    public IQYSString(String value) {
        this.mValue = value;
    }

    protected IQYSString(Parcel in) {
        this.mValue = in.readString();
    }

    @Override // com.qiyi.video.qys.IQYSStringWrapper
    public String get() {
        return this.mValue;
    }

    public String toString() {
        String str = this.mValue;
        return str == null ? "" : str;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mValue);
    }

    public void readFromParcel(Parcel source) {
        this.mValue = source.readString();
    }
}
