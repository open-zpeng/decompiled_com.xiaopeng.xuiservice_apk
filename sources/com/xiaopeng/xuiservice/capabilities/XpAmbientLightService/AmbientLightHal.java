package com.xiaopeng.xuiservice.capabilities.XpAmbientLightService;

import android.car.hardware.atl.AtlConfiguration;
/* loaded from: classes5.dex */
public interface AmbientLightHal {
    void setAllLightData(byte[] bArr, byte[] bArr2, byte[] bArr3);

    void setAtlConfiguration(AtlConfiguration atlConfiguration);

    void setAtlOpen(int i);

    void setBrightnessLevel(int i);

    void setDoubleThemeColor(int i);

    void setGroutLightData(byte b, byte b2, int i, boolean z, byte b3, byte b4, byte b5);

    void setThemeFirstColor(int i);

    void setThemeSecondColor(int i);

    void setTwoLightData(byte b, byte[] bArr, boolean z, byte[] bArr2, byte[] bArr3, byte[] bArr4);
}
