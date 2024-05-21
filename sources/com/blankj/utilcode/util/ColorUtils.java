package com.blankj.utilcode.util;

import android.graphics.Color;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.InputDeviceCompat;
import androidx.core.view.ViewCompat;
/* loaded from: classes4.dex */
public final class ColorUtils {
    private ColorUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static int getColor(@ColorRes int id) {
        return ContextCompat.getColor(Utils.getApp(), id);
    }

    public static int setAlphaComponent(@ColorInt int color, @IntRange(from = 0, to = 255) int alpha) {
        return (16777215 & color) | (alpha << 24);
    }

    public static int setAlphaComponent(@ColorInt int color, @FloatRange(from = 0.0d, to = 1.0d) float alpha) {
        return (16777215 & color) | (((int) ((255.0f * alpha) + 0.5f)) << 24);
    }

    public static int setRedComponent(@ColorInt int color, @IntRange(from = 0, to = 255) int red) {
        return ((-16711681) & color) | (red << 16);
    }

    public static int setRedComponent(@ColorInt int color, @FloatRange(from = 0.0d, to = 1.0d) float red) {
        return ((-16711681) & color) | (((int) ((255.0f * red) + 0.5f)) << 16);
    }

    public static int setGreenComponent(@ColorInt int color, @IntRange(from = 0, to = 255) int green) {
        return ((-65281) & color) | (green << 8);
    }

    public static int setGreenComponent(@ColorInt int color, @FloatRange(from = 0.0d, to = 1.0d) float green) {
        return ((-65281) & color) | (((int) ((255.0f * green) + 0.5f)) << 8);
    }

    public static int setBlueComponent(@ColorInt int color, @IntRange(from = 0, to = 255) int blue) {
        return (color & InputDeviceCompat.SOURCE_ANY) | blue;
    }

    public static int setBlueComponent(@ColorInt int color, @FloatRange(from = 0.0d, to = 1.0d) float blue) {
        return (color & InputDeviceCompat.SOURCE_ANY) | ((int) ((255.0f * blue) + 0.5f));
    }

    public static int string2Int(@NonNull String colorString) {
        if (colorString == null) {
            throw new NullPointerException("Argument 'colorString' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return Color.parseColor(colorString);
    }

    public static String int2RgbString(@ColorInt int colorInt) {
        String color = Integer.toHexString(colorInt & ViewCompat.MEASURED_SIZE_MASK);
        while (color.length() < 6) {
            color = "0" + color;
        }
        return "#" + color;
    }

    public static String int2ArgbString(@ColorInt int colorInt) {
        String color = Integer.toHexString(colorInt);
        while (color.length() < 6) {
            color = "0" + color;
        }
        while (color.length() < 8) {
            color = "f" + color;
        }
        return "#" + color;
    }

    public static int getRandomColor() {
        return getRandomColor(true);
    }

    public static int getRandomColor(boolean supportAlpha) {
        int high = supportAlpha ? ((int) (Math.random() * 256.0d)) << 24 : ViewCompat.MEASURED_STATE_MASK;
        return ((int) (Math.random() * 1.6777216E7d)) | high;
    }

    public static boolean isLightColor(@ColorInt int color) {
        return ((((double) Color.red(color)) * 0.299d) + (((double) Color.green(color)) * 0.587d)) + (((double) Color.blue(color)) * 0.114d) >= 127.5d;
    }
}
