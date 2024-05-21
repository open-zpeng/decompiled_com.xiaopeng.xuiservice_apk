package com.loostone.libtuning.channel.byd.light;

import androidx.core.view.ViewCompat;
/* loaded from: classes4.dex */
public class ColorUtil {
    public static int getColor(int[] iArr) {
        return iArr[2] | (iArr[0] << 16) | ViewCompat.MEASURED_STATE_MASK | (iArr[1] << 8);
    }

    public static int getColor(byte[] bArr) {
        return bArr[2] | (bArr[0] << 16) | ViewCompat.MEASURED_STATE_MASK | (bArr[1] << 8);
    }
}
