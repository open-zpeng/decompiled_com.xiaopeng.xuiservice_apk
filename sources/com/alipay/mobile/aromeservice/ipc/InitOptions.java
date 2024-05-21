package com.alipay.mobile.aromeservice.ipc;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes4.dex */
public class InitOptions implements Parcelable {
    public static final Parcelable.Creator<InitOptions> CREATOR = new Parcelable.Creator<InitOptions>() { // from class: com.alipay.mobile.aromeservice.ipc.InitOptions.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public final InitOptions createFromParcel(Parcel in) {
            return new InitOptions(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public final InitOptions[] newArray(int size) {
            return new InitOptions[size];
        }
    };
    public Bundle deviceConfig;
    public String hardwareName;
    public int hardwareType;
    public int loginMode;
    public Bundle themeConfig;

    private InitOptions() {
    }

    private InitOptions(Parcel in) {
        readFromParcel(in);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.loginMode);
        out.writeInt(this.hardwareType);
        out.writeString(this.hardwareName);
        out.writeBundle(this.themeConfig);
        out.writeBundle(this.deviceConfig);
    }

    public void readFromParcel(Parcel in) {
        this.loginMode = in.readInt();
        this.hardwareType = in.readInt();
        this.hardwareName = in.readString();
        this.themeConfig = in.readBundle();
        this.deviceConfig = in.readBundle();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* loaded from: classes4.dex */
    public static class Builder {
        private Bundle deviceConfig;
        private String hardwareName;
        private int hardwareType;
        private int loginMode;
        private Bundle themeConfig;

        public Builder loginMode(int initMode) {
            this.loginMode = initMode;
            return this;
        }

        public Builder hardwareType(int hardwareType) {
            this.hardwareType = hardwareType;
            return this;
        }

        public Builder hardwareName(String hardwareName) {
            this.hardwareName = hardwareName;
            return this;
        }

        public Builder themeConfig(Bundle themeConfig) {
            this.themeConfig = themeConfig;
            return this;
        }

        public Builder deviceConfig(Bundle deviceConfig) {
            this.deviceConfig = deviceConfig;
            return this;
        }

        public InitOptions build() {
            InitOptions options = new InitOptions();
            options.loginMode = this.loginMode;
            options.hardwareType = this.hardwareType;
            options.hardwareName = this.hardwareName;
            options.themeConfig = this.themeConfig;
            options.deviceConfig = this.deviceConfig;
            return options;
        }
    }
}
