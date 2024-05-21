package com.alipay.mobile.aromeservice.ipc;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes4.dex */
public final class App implements Parcelable {
    public static final Parcelable.Creator<App> CREATOR = new Parcelable.Creator<App>() { // from class: com.alipay.mobile.aromeservice.ipc.App.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public final App createFromParcel(Parcel in) {
            return new App(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public final App[] newArray(int size) {
            return new App[size];
        }
    };
    public String hostAppId;

    private App() {
    }

    private App(Parcel in) {
        readFromParcel(in);
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel out, int flags) {
        out.writeString(this.hostAppId);
    }

    public final void readFromParcel(Parcel in) {
        this.hostAppId = in.readString();
    }

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    /* loaded from: classes4.dex */
    public static class Builder {
        private String hostAppId;

        public Builder hostAppId(String appId) {
            this.hostAppId = appId;
            return this;
        }

        public App build() {
            App app = new App();
            app.hostAppId = this.hostAppId;
            return app;
        }
    }
}
