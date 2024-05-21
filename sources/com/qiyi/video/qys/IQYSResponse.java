package com.qiyi.video.qys;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes4.dex */
public final class IQYSResponse implements Parcelable {
    public static final Parcelable.Creator<IQYSResponse> CREATOR = new Parcelable.Creator<IQYSResponse>() { // from class: com.qiyi.video.qys.IQYSResponse.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IQYSResponse createFromParcel(Parcel source) {
            return new IQYSResponse(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IQYSResponse[] newArray(int size) {
            return new IQYSResponse[size];
        }
    };
    public int mCode;
    public IQYSStringWrapper mMsg;
    public IQYSRequest mRealRequest;
    public IQYSRequest mRequest;
    public IQYSStringWrapper mResult;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mCode);
        dest.writeParcelable(this.mMsg, flags);
        dest.writeParcelable(this.mResult, flags);
        dest.writeParcelable(this.mRealRequest, flags);
    }

    public void readFromParcel(Parcel source) {
        this.mCode = source.readInt();
        this.mMsg = (IQYSStringWrapper) source.readParcelable(IQYSStringWrapper.class.getClassLoader());
        this.mResult = (IQYSStringWrapper) source.readParcelable(IQYSStringWrapper.class.getClassLoader());
        this.mRealRequest = (IQYSRequest) source.readParcelable(IQYSRequest.class.getClassLoader());
    }

    public IQYSResponse() {
        this.mMsg = IQYSString.EMPTY;
        this.mResult = IQYSString.EMPTY;
    }

    protected IQYSResponse(Parcel in) {
        this.mMsg = IQYSString.EMPTY;
        this.mResult = IQYSString.EMPTY;
        this.mCode = in.readInt();
        this.mMsg = (IQYSStringWrapper) in.readParcelable(IQYSStringWrapper.class.getClassLoader());
        this.mResult = (IQYSStringWrapper) in.readParcelable(IQYSStringWrapper.class.getClassLoader());
        this.mRealRequest = (IQYSRequest) in.readParcelable(IQYSRequest.class.getClassLoader());
    }
}
