package com.xiaopeng.xui.utils;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import org.opencv.videoio.Videoio;
@RestrictTo({RestrictTo.Scope.LIBRARY})
/* loaded from: classes5.dex */
public class XInputUtils {
    public static void ignoreHiddenInput(@NonNull View view) {
        view.setTag(Videoio.CAP_INTELPERC_IMAGE_GENERATOR, 1001);
    }
}
