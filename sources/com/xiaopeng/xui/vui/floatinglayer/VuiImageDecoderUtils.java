package com.xiaopeng.xui.vui.floatinglayer;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.xiaopeng.xpui.R;
import com.xiaopeng.xui.utils.XLogUtils;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes5.dex */
public class VuiImageDecoderUtils {
    private static final String TOUCH_DEFAULT_WEBP = "anim/floating_touch.webp";
    private static final String TOUCH_DEFAULT_WEBP_NIGHT = "anim/floating_touch_night.webp";
    private static final String TAG = VuiImageDecoderUtils.class.getSimpleName();
    private static List<String> sAssetsFileList = null;

    public static boolean isSupportNight(int type) {
        if (type == 1) {
            return true;
        }
        return false;
    }

    public static boolean isSupportAlpha(int type) {
        return false;
    }

    public static int getAnimateTimeOut(int type) {
        if (type != 1) {
            return 1500;
        }
        return 5500;
    }

    @TargetApi(28)
    public static Drawable decoderImage(@NonNull Context context, int type, boolean isNight) {
        String str = TAG;
        XLogUtils.d(str, "decoderImage type : " + type + ", isNight : " + isNight);
        if (type != 1) {
            boolean nightExists = false;
            boolean dayExists = false;
            String resourceName = TOUCH_DEFAULT_WEBP_NIGHT;
            if (isNight) {
                nightExists = animAssetsContains(context, TOUCH_DEFAULT_WEBP_NIGHT);
            } else {
                dayExists = animAssetsContains(context, TOUCH_DEFAULT_WEBP);
            }
            if (!isNight || !nightExists) {
                resourceName = dayExists ? TOUCH_DEFAULT_WEBP : null;
            }
            if (!TextUtils.isEmpty(resourceName)) {
                ImageDecoder.Source source = ImageDecoder.createSource(context.getAssets(), resourceName);
                try {
                    Drawable animatedImageDrawable = ImageDecoder.decodeDrawable(source);
                    return animatedImageDrawable;
                } catch (IOException e) {
                    XLogUtils.w(TAG, "decodeException:", e);
                    return null;
                }
            }
            XLogUtils.e(TAG, "decode error, file not found");
            return null;
        }
        return new VuiFloatingDrawable(BitmapFactory.decodeResource(context.getResources(), R.drawable.floating_element));
    }

    @TargetApi(28)
    public static Drawable decoderImage(@NonNull Context context, int type) {
        return decoderImage(context, type, false);
    }

    private static boolean animAssetsContains(Context context, String filePath) {
        if (filePath.startsWith("anim/")) {
            filePath = filePath.replace("anim/", "");
        }
        try {
            if (sAssetsFileList == null) {
                sAssetsFileList = Arrays.asList(context.getResources().getAssets().list("anim"));
            }
            String str = TAG;
            XLogUtils.d(str, "assetsContains, assets:" + sAssetsFileList);
            return sAssetsFileList.contains(filePath);
        } catch (Exception e) {
            String str2 = TAG;
            XLogUtils.w(str2, "assetsContains call ex:" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
