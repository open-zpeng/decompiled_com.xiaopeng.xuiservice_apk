package com.alipay.arome.aromecli;

import android.os.Bundle;
import android.support.annotation.Keep;
@Keep
/* loaded from: classes4.dex */
public class AromeInitOptions {
    Bundle deviceConfig;
    String hardwareName;
    int hardwareType;
    int loginMode;
    Bundle themeConfig;

    public AromeInitOptions(int loginMode, int hardwareType, String hardwareName, Bundle themeConfig, Bundle deviceConfig) {
        this.loginMode = loginMode;
        this.hardwareType = hardwareType;
        this.hardwareName = hardwareName;
        this.themeConfig = themeConfig;
        this.deviceConfig = deviceConfig;
    }

    @Keep
    /* loaded from: classes4.dex */
    public static class Builder {
        int loginMode = 0;
        int hardwareType = 0;
        String hardwareName = "";
        Bundle themeConfig = null;
        Bundle deviceConfig = null;

        public Builder loginMode(int loginMode) {
            this.loginMode = loginMode;
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

        public AromeInitOptions build() {
            return new AromeInitOptions(this.loginMode, this.hardwareType, this.hardwareName, this.themeConfig, this.deviceConfig);
        }
    }
}
