package com.qiyi.video.qys;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes4.dex */
public final class IQYSRequest implements Parcelable {
    public static final Parcelable.Creator<IQYSRequest> CREATOR = new Parcelable.Creator<IQYSRequest>() { // from class: com.qiyi.video.qys.IQYSRequest.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IQYSRequest createFromParcel(Parcel source) {
            return new IQYSRequest(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IQYSRequest[] newArray(int size) {
            return new IQYSRequest[size];
        }
    };
    public IQYSStringWrapper mCmd;

    public IQYSRequest(String cmd) {
        this.mCmd = new IQYSString(cmd);
    }

    public IQYSRequest(IQYSStringWrapper cmd) {
        this.mCmd = cmd;
    }

    public void readFromParcel(Parcel source) {
        this.mCmd = (IQYSStringWrapper) source.readParcelable(IQYSStringWrapper.class.getClassLoader());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mCmd, flags);
    }

    protected IQYSRequest(Parcel in) {
        this.mCmd = (IQYSStringWrapper) in.readParcelable(IQYSStringWrapper.class.getClassLoader());
    }
}
