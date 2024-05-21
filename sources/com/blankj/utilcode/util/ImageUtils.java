package com.blankj.utilcode.util;

import android.content.ContentValues;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import com.alibaba.fastjson.asm.Opcodes;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
/* loaded from: classes4.dex */
public final class ImageUtils {
    private ImageUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static byte[] bitmap2Bytes(Bitmap bitmap) {
        return bitmap2Bytes(bitmap, Bitmap.CompressFormat.PNG, 100);
    }

    public static byte[] bitmap2Bytes(@Nullable Bitmap bitmap, @NonNull Bitmap.CompressFormat format, int quality) {
        if (format == null) {
            throw new NullPointerException("Argument 'format' of type CompressFormat (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, quality, baos);
        return baos.toByteArray();
    }

    public static Bitmap bytes2Bitmap(@Nullable byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static Bitmap drawable2Bitmap(@Nullable Drawable drawable) {
        Bitmap bitmap;
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Drawable bitmap2Drawable(@Nullable Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        return new BitmapDrawable(Utils.getApp().getResources(), bitmap);
    }

    public static byte[] drawable2Bytes(@Nullable Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        return bitmap2Bytes(drawable2Bitmap(drawable));
    }

    public static byte[] drawable2Bytes(Drawable drawable, Bitmap.CompressFormat format, int quality) {
        if (drawable == null) {
            return null;
        }
        return bitmap2Bytes(drawable2Bitmap(drawable), format, quality);
    }

    public static Drawable bytes2Drawable(byte[] bytes) {
        return bitmap2Drawable(bytes2Bitmap(bytes));
    }

    public static Bitmap view2Bitmap(View view) {
        Bitmap bitmap;
        if (view == null) {
            return null;
        }
        boolean drawingCacheEnabled = view.isDrawingCacheEnabled();
        boolean willNotCacheDrawing = view.willNotCacheDrawing();
        view.setDrawingCacheEnabled(true);
        view.setWillNotCacheDrawing(false);
        Bitmap drawingCache = view.getDrawingCache();
        if (drawingCache == null || drawingCache.isRecycled()) {
            view.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            view.buildDrawingCache();
            Bitmap drawingCache2 = view.getDrawingCache();
            if (drawingCache2 == null || drawingCache2.isRecycled()) {
                bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(bitmap);
                view.draw(canvas);
            } else {
                bitmap = Bitmap.createBitmap(drawingCache2);
            }
        } else {
            bitmap = Bitmap.createBitmap(drawingCache);
        }
        view.setWillNotCacheDrawing(willNotCacheDrawing);
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        return bitmap;
    }

    public static Bitmap getBitmap(File file) {
        if (file == null) {
            return null;
        }
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    public static Bitmap getBitmap(File file, int maxWidth, int maxHeight) {
        if (file == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    public static Bitmap getBitmap(String filePath) {
        if (UtilsBridge.isSpace(filePath)) {
            return null;
        }
        return BitmapFactory.decodeFile(filePath);
    }

    public static Bitmap getBitmap(String filePath, int maxWidth, int maxHeight) {
        if (UtilsBridge.isSpace(filePath)) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap getBitmap(InputStream is) {
        if (is == null) {
            return null;
        }
        return BitmapFactory.decodeStream(is);
    }

    public static Bitmap getBitmap(InputStream is, int maxWidth, int maxHeight) {
        if (is == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(is, null, options);
    }

    public static Bitmap getBitmap(byte[] data, int offset) {
        if (data.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(data, offset, data.length);
    }

    public static Bitmap getBitmap(byte[] data, int offset, int maxWidth, int maxHeight) {
        if (data.length == 0) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, offset, data.length, options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, offset, data.length, options);
    }

    public static Bitmap getBitmap(@DrawableRes int resId) {
        Drawable drawable = ContextCompat.getDrawable(Utils.getApp(), resId);
        if (drawable == null) {
            return null;
        }
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap getBitmap(@DrawableRes int resId, int maxWidth, int maxHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Resources resources = Utils.getApp().getResources();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resId, options);
    }

    public static Bitmap getBitmap(FileDescriptor fd) {
        if (fd == null) {
            return null;
        }
        return BitmapFactory.decodeFileDescriptor(fd);
    }

    public static Bitmap getBitmap(FileDescriptor fd, int maxWidth, int maxHeight) {
        if (fd == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }

    public static Bitmap drawColor(@NonNull Bitmap src, @ColorInt int color) {
        if (src == null) {
            throw new NullPointerException("Argument 'src' of type Bitmap (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return drawColor(src, color, false);
    }

    public static Bitmap drawColor(@NonNull Bitmap src, @ColorInt int color, boolean recycle) {
        if (src == null) {
            throw new NullPointerException("Argument 'src' of type Bitmap (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (isEmptyBitmap(src)) {
            return null;
        }
        Bitmap ret = recycle ? src : src.copy(src.getConfig(), true);
        Canvas canvas = new Canvas(ret);
        canvas.drawColor(color, PorterDuff.Mode.DARKEN);
        return ret;
    }

    public static Bitmap scale(Bitmap src, int newWidth, int newHeight) {
        return scale(src, newWidth, newHeight, false);
    }

    public static Bitmap scale(Bitmap src, int newWidth, int newHeight, boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        Bitmap ret = Bitmap.createScaledBitmap(src, newWidth, newHeight, true);
        if (recycle && !src.isRecycled() && ret != src) {
            src.recycle();
        }
        return ret;
    }

    public static Bitmap scale(Bitmap src, float scaleWidth, float scaleHeight) {
        return scale(src, scaleWidth, scaleHeight, false);
    }

    public static Bitmap scale(Bitmap src, float scaleWidth, float scaleHeight, boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.setScale(scaleWidth, scaleHeight);
        Bitmap ret = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        if (recycle && !src.isRecycled() && ret != src) {
            src.recycle();
        }
        return ret;
    }

    public static Bitmap clip(Bitmap src, int x, int y, int width, int height) {
        return clip(src, x, y, width, height, false);
    }

    public static Bitmap clip(Bitmap src, int x, int y, int width, int height, boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        Bitmap ret = Bitmap.createBitmap(src, x, y, width, height);
        if (recycle && !src.isRecycled() && ret != src) {
            src.recycle();
        }
        return ret;
    }

    public static Bitmap skew(Bitmap src, float kx, float ky) {
        return skew(src, kx, ky, 0.0f, 0.0f, false);
    }

    public static Bitmap skew(Bitmap src, float kx, float ky, boolean recycle) {
        return skew(src, kx, ky, 0.0f, 0.0f, recycle);
    }

    public static Bitmap skew(Bitmap src, float kx, float ky, float px, float py) {
        return skew(src, kx, ky, px, py, false);
    }

    public static Bitmap skew(Bitmap src, float kx, float ky, float px, float py, boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.setSkew(kx, ky, px, py);
        Bitmap ret = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        if (recycle && !src.isRecycled() && ret != src) {
            src.recycle();
        }
        return ret;
    }

    public static Bitmap rotate(Bitmap src, int degrees, float px, float py) {
        return rotate(src, degrees, px, py, false);
    }

    public static Bitmap rotate(Bitmap src, int degrees, float px, float py, boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        if (degrees == 0) {
            return src;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, px, py);
        Bitmap ret = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        if (recycle && !src.isRecycled() && ret != src) {
            src.recycle();
        }
        return ret;
    }

    public static int getRotateDegree(String filePath) {
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION, 1);
            if (orientation != 3) {
                if (orientation != 6) {
                    if (orientation == 8) {
                        return 270;
                    }
                    return 0;
                }
                return 90;
            }
            return Opcodes.GETFIELD;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static Bitmap toRound(Bitmap src) {
        return toRound(src, 0, 0, false);
    }

    public static Bitmap toRound(Bitmap src, boolean recycle) {
        return toRound(src, 0, 0, recycle);
    }

    public static Bitmap toRound(Bitmap src, @IntRange(from = 0) int borderSize, @ColorInt int borderColor) {
        return toRound(src, borderSize, borderColor, false);
    }

    public static Bitmap toRound(Bitmap src, @IntRange(from = 0) int borderSize, @ColorInt int borderColor, boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        int width = src.getWidth();
        int height = src.getHeight();
        int size = Math.min(width, height);
        Paint paint = new Paint(1);
        Bitmap ret = Bitmap.createBitmap(width, height, src.getConfig());
        float center = size / 2.0f;
        RectF rectF = new RectF(0.0f, 0.0f, width, height);
        rectF.inset((width - size) / 2.0f, (height - size) / 2.0f);
        Matrix matrix = new Matrix();
        matrix.setTranslate(rectF.left, rectF.top);
        if (width != height) {
            matrix.preScale(size / width, size / height);
        }
        BitmapShader shader = new BitmapShader(src, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        shader.setLocalMatrix(matrix);
        paint.setShader(shader);
        Canvas canvas = new Canvas(ret);
        canvas.drawRoundRect(rectF, center, center, paint);
        if (borderSize > 0) {
            paint.setShader(null);
            paint.setColor(borderColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(borderSize);
            float radius = center - (borderSize / 2.0f);
            canvas.drawCircle(width / 2.0f, height / 2.0f, radius, paint);
        }
        if (recycle && !src.isRecycled() && ret != src) {
            src.recycle();
        }
        return ret;
    }

    public static Bitmap toRoundCorner(Bitmap src, float radius) {
        return toRoundCorner(src, radius, 0.0f, 0, false);
    }

    public static Bitmap toRoundCorner(Bitmap src, float radius, boolean recycle) {
        return toRoundCorner(src, radius, 0.0f, 0, recycle);
    }

    public static Bitmap toRoundCorner(Bitmap src, float radius, @FloatRange(from = 0.0d) float borderSize, @ColorInt int borderColor) {
        return toRoundCorner(src, radius, borderSize, borderColor, false);
    }

    public static Bitmap toRoundCorner(Bitmap src, float[] radii, @FloatRange(from = 0.0d) float borderSize, @ColorInt int borderColor) {
        return toRoundCorner(src, radii, borderSize, borderColor, false);
    }

    public static Bitmap toRoundCorner(Bitmap src, float radius, @FloatRange(from = 0.0d) float borderSize, @ColorInt int borderColor, boolean recycle) {
        float[] radii = {radius, radius, radius, radius, radius, radius, radius, radius};
        return toRoundCorner(src, radii, borderSize, borderColor, recycle);
    }

    public static Bitmap toRoundCorner(Bitmap src, float[] radii, @FloatRange(from = 0.0d) float borderSize, @ColorInt int borderColor, boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        int width = src.getWidth();
        int height = src.getHeight();
        Paint paint = new Paint(1);
        Bitmap ret = Bitmap.createBitmap(width, height, src.getConfig());
        BitmapShader shader = new BitmapShader(src, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        Canvas canvas = new Canvas(ret);
        RectF rectF = new RectF(0.0f, 0.0f, width, height);
        float halfBorderSize = borderSize / 2.0f;
        rectF.inset(halfBorderSize, halfBorderSize);
        Path path = new Path();
        path.addRoundRect(rectF, radii, Path.Direction.CW);
        canvas.drawPath(path, paint);
        if (borderSize > 0.0f) {
            paint.setShader(null);
            paint.setColor(borderColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(borderSize);
            paint.setStrokeCap(Paint.Cap.ROUND);
            canvas.drawPath(path, paint);
        }
        if (recycle && !src.isRecycled() && ret != src) {
            src.recycle();
        }
        return ret;
    }

    public static Bitmap addCornerBorder(Bitmap src, @FloatRange(from = 1.0d) float borderSize, @ColorInt int color, @FloatRange(from = 0.0d) float cornerRadius) {
        return addBorder(src, borderSize, color, false, cornerRadius, false);
    }

    public static Bitmap addCornerBorder(Bitmap src, @FloatRange(from = 1.0d) float borderSize, @ColorInt int color, float[] radii) {
        return addBorder(src, borderSize, color, false, radii, false);
    }

    public static Bitmap addCornerBorder(Bitmap src, @FloatRange(from = 1.0d) float borderSize, @ColorInt int color, float[] radii, boolean recycle) {
        return addBorder(src, borderSize, color, false, radii, recycle);
    }

    public static Bitmap addCornerBorder(Bitmap src, @FloatRange(from = 1.0d) float borderSize, @ColorInt int color, @FloatRange(from = 0.0d) float cornerRadius, boolean recycle) {
        return addBorder(src, borderSize, color, false, cornerRadius, recycle);
    }

    public static Bitmap addCircleBorder(Bitmap src, @FloatRange(from = 1.0d) float borderSize, @ColorInt int color) {
        return addBorder(src, borderSize, color, true, 0.0f, false);
    }

    public static Bitmap addCircleBorder(Bitmap src, @FloatRange(from = 1.0d) float borderSize, @ColorInt int color, boolean recycle) {
        return addBorder(src, borderSize, color, true, 0.0f, recycle);
    }

    private static Bitmap addBorder(Bitmap src, @FloatRange(from = 1.0d) float borderSize, @ColorInt int color, boolean isCircle, float cornerRadius, boolean recycle) {
        float[] radii = {cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius};
        return addBorder(src, borderSize, color, isCircle, radii, recycle);
    }

    private static Bitmap addBorder(Bitmap src, @FloatRange(from = 1.0d) float borderSize, @ColorInt int color, boolean isCircle, float[] radii, boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        Bitmap ret = recycle ? src : src.copy(src.getConfig(), true);
        int width = ret.getWidth();
        int height = ret.getHeight();
        Canvas canvas = new Canvas(ret);
        Paint paint = new Paint(1);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderSize);
        if (isCircle) {
            float radius = (Math.min(width, height) / 2.0f) - (borderSize / 2.0f);
            canvas.drawCircle(width / 2.0f, height / 2.0f, radius, paint);
        } else {
            RectF rectF = new RectF(0.0f, 0.0f, width, height);
            float halfBorderSize = borderSize / 2.0f;
            rectF.inset(halfBorderSize, halfBorderSize);
            Path path = new Path();
            path.addRoundRect(rectF, radii, Path.Direction.CW);
            canvas.drawPath(path, paint);
        }
        return ret;
    }

    public static Bitmap addReflection(Bitmap src, int reflectionHeight) {
        return addReflection(src, reflectionHeight, false);
    }

    public static Bitmap addReflection(Bitmap src, int reflectionHeight, boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        Bitmap reflectionBitmap = Bitmap.createBitmap(src, 0, srcHeight - reflectionHeight, srcWidth, reflectionHeight, matrix, false);
        Bitmap ret = Bitmap.createBitmap(srcWidth, srcHeight + reflectionHeight, src.getConfig());
        Canvas canvas = new Canvas(ret);
        canvas.drawBitmap(src, 0.0f, 0.0f, (Paint) null);
        canvas.drawBitmap(reflectionBitmap, 0.0f, srcHeight + 0, (Paint) null);
        Paint paint = new Paint(1);
        LinearGradient shader = new LinearGradient(0.0f, srcHeight, 0.0f, ret.getHeight() + 0, 1895825407, (int) ViewCompat.MEASURED_SIZE_MASK, Shader.TileMode.MIRROR);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0.0f, srcHeight + 0, srcWidth, ret.getHeight(), paint);
        if (!reflectionBitmap.isRecycled()) {
            reflectionBitmap.recycle();
        }
        if (recycle && !src.isRecycled() && ret != src) {
            src.recycle();
        }
        return ret;
    }

    public static Bitmap addTextWatermark(Bitmap src, String content, int textSize, @ColorInt int color, float x, float y) {
        return addTextWatermark(src, content, textSize, color, x, y, false);
    }

    public static Bitmap addTextWatermark(Bitmap src, String content, float textSize, @ColorInt int color, float x, float y, boolean recycle) {
        if (isEmptyBitmap(src) || content == null) {
            return null;
        }
        Bitmap ret = src.copy(src.getConfig(), true);
        Paint paint = new Paint(1);
        Canvas canvas = new Canvas(ret);
        paint.setColor(color);
        paint.setTextSize(textSize);
        Rect bounds = new Rect();
        paint.getTextBounds(content, 0, content.length(), bounds);
        canvas.drawText(content, x, y + textSize, paint);
        if (recycle && !src.isRecycled() && ret != src) {
            src.recycle();
        }
        return ret;
    }

    public static Bitmap addImageWatermark(Bitmap src, Bitmap watermark, int x, int y, int alpha) {
        return addImageWatermark(src, watermark, x, y, alpha, false);
    }

    public static Bitmap addImageWatermark(Bitmap src, Bitmap watermark, int x, int y, int alpha, boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        Bitmap ret = src.copy(src.getConfig(), true);
        if (!isEmptyBitmap(watermark)) {
            Paint paint = new Paint(1);
            Canvas canvas = new Canvas(ret);
            paint.setAlpha(alpha);
            canvas.drawBitmap(watermark, x, y, paint);
        }
        if (recycle && !src.isRecycled() && ret != src) {
            src.recycle();
        }
        return ret;
    }

    public static Bitmap toAlpha(Bitmap src) {
        return toAlpha(src, false);
    }

    public static Bitmap toAlpha(Bitmap src, Boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        Bitmap ret = src.extractAlpha();
        if (recycle.booleanValue() && !src.isRecycled() && ret != src) {
            src.recycle();
        }
        return ret;
    }

    public static Bitmap toGray(Bitmap src) {
        return toGray(src, false);
    }

    public static Bitmap toGray(Bitmap src, boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        Bitmap ret = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        Canvas canvas = new Canvas(ret);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixColorFilter);
        canvas.drawBitmap(src, 0.0f, 0.0f, paint);
        if (recycle && !src.isRecycled() && ret != src) {
            src.recycle();
        }
        return ret;
    }

    public static Bitmap fastBlur(Bitmap src, @FloatRange(from = 0.0d, fromInclusive = false, to = 1.0d) float scale, @FloatRange(from = 0.0d, fromInclusive = false, to = 25.0d) float radius) {
        return fastBlur(src, scale, radius, false, false);
    }

    public static Bitmap fastBlur(Bitmap src, @FloatRange(from = 0.0d, fromInclusive = false, to = 1.0d) float scale, @FloatRange(from = 0.0d, fromInclusive = false, to = 25.0d) float radius, boolean recycle) {
        return fastBlur(src, scale, radius, recycle, false);
    }

    public static Bitmap fastBlur(Bitmap src, @FloatRange(from = 0.0d, fromInclusive = false, to = 1.0d) float scale, @FloatRange(from = 0.0d, fromInclusive = false, to = 25.0d) float radius, boolean recycle, boolean isReturnScale) {
        Bitmap scaleBitmap;
        if (isEmptyBitmap(src)) {
            return null;
        }
        int width = src.getWidth();
        int height = src.getHeight();
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        Bitmap scaleBitmap2 = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        Paint paint = new Paint(3);
        Canvas canvas = new Canvas();
        PorterDuffColorFilter filter = new PorterDuffColorFilter(0, PorterDuff.Mode.SRC_ATOP);
        paint.setColorFilter(filter);
        canvas.scale(scale, scale);
        canvas.drawBitmap(scaleBitmap2, 0.0f, 0.0f, paint);
        if (Build.VERSION.SDK_INT >= 17) {
            scaleBitmap = renderScriptBlur(scaleBitmap2, radius, recycle);
        } else {
            scaleBitmap = stackBlur(scaleBitmap2, (int) radius, recycle);
        }
        if (scale == 1.0f || isReturnScale) {
            if (recycle && !src.isRecycled() && scaleBitmap != src) {
                src.recycle();
            }
            return scaleBitmap;
        }
        Bitmap ret = Bitmap.createScaledBitmap(scaleBitmap, width, height, true);
        if (!scaleBitmap.isRecycled()) {
            scaleBitmap.recycle();
        }
        if (recycle && !src.isRecycled() && ret != src) {
            src.recycle();
        }
        return ret;
    }

    @RequiresApi(17)
    public static Bitmap renderScriptBlur(Bitmap src, @FloatRange(from = 0.0d, fromInclusive = false, to = 25.0d) float radius) {
        return renderScriptBlur(src, radius, false);
    }

    @RequiresApi(17)
    public static Bitmap renderScriptBlur(Bitmap src, @FloatRange(from = 0.0d, fromInclusive = false, to = 25.0d) float radius, boolean recycle) {
        RenderScript rs = null;
        Bitmap ret = recycle ? src : src.copy(src.getConfig(), true);
        try {
            rs = RenderScript.create(Utils.getApp());
            rs.setMessageHandler(new RenderScript.RSMessageHandler());
            Allocation input = Allocation.createFromBitmap(rs, ret, Allocation.MipmapControl.MIPMAP_NONE, 1);
            Allocation output = Allocation.createTyped(rs, input.getType());
            ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            blurScript.setInput(input);
            blurScript.setRadius(radius);
            blurScript.forEach(output);
            output.copyTo(ret);
            rs.destroy();
            return ret;
        } catch (Throwable th) {
            if (rs != null) {
                rs.destroy();
            }
            throw th;
        }
    }

    public static Bitmap stackBlur(Bitmap src, int radius) {
        return stackBlur(src, radius, false);
    }

    public static Bitmap stackBlur(Bitmap src, int radius, boolean recycle) {
        int radius2;
        int yp;
        int[] dv;
        int i;
        Bitmap ret = recycle ? src : src.copy(src.getConfig(), true);
        if (radius >= 1) {
            radius2 = radius;
        } else {
            radius2 = 1;
        }
        int w = ret.getWidth();
        int p = ret.getHeight();
        int[] pix = new int[w * p];
        ret.getPixels(pix, 0, w, 0, 0, w, p);
        int wm = w - 1;
        int hm = p - 1;
        int wh = w * p;
        int div = radius2 + radius2 + 1;
        int[] r = new int[wh];
        int[] g = new int[wh];
        int[] b = new int[wh];
        int[] vmin = new int[Math.max(w, p)];
        int divsum = (div + 1) >> 1;
        int divsum2 = divsum * divsum;
        int[] dv2 = new int[divsum2 * 256];
        int i2 = 0;
        while (true) {
            int wh2 = wh;
            if (i2 >= divsum2 * 256) {
                break;
            }
            dv2[i2] = i2 / divsum2;
            i2++;
            wh = wh2;
        }
        int yi = 0;
        int yw = 0;
        int[][] stack = (int[][]) Array.newInstance(int.class, div, 3);
        int r1 = radius2 + 1;
        int y = 0;
        while (y < p) {
            int h = 0;
            int gsum = 0;
            int rsum = 0;
            int boutsum = 0;
            int goutsum = 0;
            int routsum = 0;
            int binsum = 0;
            int ginsum = 0;
            int rinsum = 0;
            int divsum3 = divsum2;
            int i3 = -radius2;
            int i4 = 0;
            while (i3 <= radius2) {
                Bitmap ret2 = ret;
                int i5 = h;
                int h2 = p;
                int h3 = Math.max(i3, i5);
                int p2 = pix[yi + Math.min(wm, h3)];
                int[] sir = stack[i3 + radius2];
                sir[i5] = (p2 & 16711680) >> 16;
                sir[1] = (p2 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                sir[2] = p2 & 255;
                int rbs = r1 - Math.abs(i3);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                i4 += sir[2] * rbs;
                if (i3 > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
                i3++;
                p = h2;
                ret = ret2;
                h = 0;
            }
            Bitmap ret3 = ret;
            int h4 = p;
            int stackpointer = radius2;
            int x = 0;
            while (x < w) {
                r[yi] = dv2[rsum];
                g[yi] = dv2[gsum];
                b[yi] = dv2[i4];
                int rsum2 = rsum - routsum;
                int gsum2 = gsum - goutsum;
                int bsum = i4 - boutsum;
                int stackstart = (stackpointer - radius2) + div;
                int[] sir2 = stack[stackstart % div];
                int routsum2 = routsum - sir2[0];
                int goutsum2 = goutsum - sir2[1];
                int boutsum2 = boutsum - sir2[2];
                if (y != 0) {
                    i = i3;
                } else {
                    i = i3;
                    int i6 = x + radius2 + 1;
                    vmin[x] = Math.min(i6, wm);
                }
                int i7 = vmin[x];
                int p3 = pix[yw + i7];
                sir2[0] = (p3 & 16711680) >> 16;
                sir2[1] = (p3 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                int wm2 = wm;
                int wm3 = p3 & 255;
                sir2[2] = wm3;
                int rinsum2 = rinsum + sir2[0];
                int ginsum2 = ginsum + sir2[1];
                int binsum2 = binsum + sir2[2];
                rsum = rsum2 + rinsum2;
                gsum = gsum2 + ginsum2;
                i4 = bsum + binsum2;
                stackpointer = (stackpointer + 1) % div;
                int[] sir3 = stack[stackpointer % div];
                routsum = routsum2 + sir3[0];
                goutsum = goutsum2 + sir3[1];
                boutsum = boutsum2 + sir3[2];
                rinsum = rinsum2 - sir3[0];
                ginsum = ginsum2 - sir3[1];
                binsum = binsum2 - sir3[2];
                yi++;
                x++;
                wm = wm2;
                i3 = i;
            }
            yw += w;
            y++;
            p = h4;
            divsum2 = divsum3;
            ret = ret3;
        }
        Bitmap ret4 = ret;
        int stackstart2 = p;
        int x2 = 0;
        int rbs2 = y;
        while (x2 < w) {
            int goutsum3 = 0;
            int routsum3 = 0;
            int ginsum3 = 0;
            int rinsum3 = 0;
            int yp2 = (-radius2) * w;
            int i8 = -radius2;
            int i9 = 0;
            int gsum3 = 0;
            int boutsum3 = 0;
            int yp3 = yp2;
            int binsum3 = 0;
            int binsum4 = 0;
            while (i8 <= radius2) {
                int y2 = rbs2;
                int yi2 = Math.max(0, yp3) + x2;
                int[] sir4 = stack[i8 + radius2];
                sir4[0] = r[yi2];
                sir4[1] = g[yi2];
                sir4[2] = b[yi2];
                int rbs3 = r1 - Math.abs(i8);
                gsum3 += r[yi2] * rbs3;
                i9 += g[yi2] * rbs3;
                boutsum3 += b[yi2] * rbs3;
                if (i8 > 0) {
                    rinsum3 += sir4[0];
                    ginsum3 += sir4[1];
                    binsum3 += sir4[2];
                } else {
                    routsum3 += sir4[0];
                    goutsum3 += sir4[1];
                    binsum4 += sir4[2];
                }
                if (i8 < hm) {
                    yp3 += w;
                }
                i8++;
                rbs2 = y2;
            }
            int y3 = x2;
            int stackpointer2 = radius2;
            int yi3 = y3;
            rbs2 = 0;
            while (true) {
                int yp4 = yp3;
                yp = stackstart2;
                if (rbs2 < yp) {
                    pix[yi3] = (pix[yi3] & ViewCompat.MEASURED_STATE_MASK) | (dv2[gsum3] << 16) | (dv2[i9] << 8) | dv2[boutsum3];
                    int rsum3 = gsum3 - routsum3;
                    int gsum4 = i9 - goutsum3;
                    int bsum2 = boutsum3 - binsum4;
                    int stackstart3 = (stackpointer2 - radius2) + div;
                    int[] sir5 = stack[stackstart3 % div];
                    int routsum4 = routsum3 - sir5[0];
                    int goutsum4 = goutsum3 - sir5[1];
                    int boutsum4 = binsum4 - sir5[2];
                    if (x2 != 0) {
                        dv = dv2;
                    } else {
                        dv = dv2;
                        vmin[rbs2] = Math.min(rbs2 + r1, hm) * w;
                    }
                    int p4 = vmin[rbs2] + x2;
                    sir5[0] = r[p4];
                    sir5[1] = g[p4];
                    sir5[2] = b[p4];
                    int rinsum4 = rinsum3 + sir5[0];
                    int ginsum4 = ginsum3 + sir5[1];
                    int binsum5 = binsum3 + sir5[2];
                    gsum3 = rsum3 + rinsum4;
                    i9 = gsum4 + ginsum4;
                    boutsum3 = bsum2 + binsum5;
                    stackpointer2 = (stackpointer2 + 1) % div;
                    int[] sir6 = stack[stackpointer2];
                    routsum3 = routsum4 + sir6[0];
                    goutsum3 = goutsum4 + sir6[1];
                    binsum4 = boutsum4 + sir6[2];
                    rinsum3 = rinsum4 - sir6[0];
                    ginsum3 = ginsum4 - sir6[1];
                    binsum3 = binsum5 - sir6[2];
                    yi3 += w;
                    rbs2++;
                    stackstart2 = yp;
                    yp3 = yp4;
                    dv2 = dv;
                }
            }
            x2++;
            stackstart2 = yp;
        }
        int h5 = stackstart2;
        ret4.setPixels(pix, 0, w, 0, 0, w, h5);
        return ret4;
    }

    public static boolean save(Bitmap src, String filePath, Bitmap.CompressFormat format) {
        return save(src, filePath, format, 100, false);
    }

    public static boolean save(Bitmap src, File file, Bitmap.CompressFormat format) {
        return save(src, file, format, 100, false);
    }

    public static boolean save(Bitmap src, String filePath, Bitmap.CompressFormat format, boolean recycle) {
        return save(src, filePath, format, 100, recycle);
    }

    public static boolean save(Bitmap src, File file, Bitmap.CompressFormat format, boolean recycle) {
        return save(src, file, format, 100, recycle);
    }

    public static boolean save(Bitmap src, String filePath, Bitmap.CompressFormat format, int quality) {
        return save(src, UtilsBridge.getFileByPath(filePath), format, quality, false);
    }

    public static boolean save(Bitmap src, File file, Bitmap.CompressFormat format, int quality) {
        return save(src, file, format, quality, false);
    }

    public static boolean save(Bitmap src, String filePath, Bitmap.CompressFormat format, int quality, boolean recycle) {
        return save(src, UtilsBridge.getFileByPath(filePath), format, quality, recycle);
    }

    public static boolean save(Bitmap src, File file, Bitmap.CompressFormat format, int quality, boolean recycle) {
        if (isEmptyBitmap(src)) {
            Log.e("ImageUtils", "bitmap is empty.");
            return false;
        } else if (src.isRecycled()) {
            Log.e("ImageUtils", "bitmap is recycled.");
            return false;
        } else if (!UtilsBridge.createFileByDeleteOldFile(file)) {
            Log.e("ImageUtils", "create or delete file <" + file + "> failed.");
            return false;
        } else {
            OutputStream os = null;
            boolean ret = false;
            try {
                try {
                    try {
                        os = new BufferedOutputStream(new FileOutputStream(file));
                        ret = src.compress(format, quality, os);
                        if (recycle && !src.isRecycled()) {
                            src.recycle();
                        }
                        os.close();
                    } catch (Throwable th) {
                        if (os != null) {
                            try {
                                os.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                    if (os != null) {
                        os.close();
                    }
                }
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            return ret;
        }
    }

    @Nullable
    public static File save2Album(Bitmap src, Bitmap.CompressFormat format) {
        return save2Album(src, "", format, 100, false);
    }

    @Nullable
    public static File save2Album(Bitmap src, Bitmap.CompressFormat format, boolean recycle) {
        return save2Album(src, "", format, 100, recycle);
    }

    @Nullable
    public static File save2Album(Bitmap src, Bitmap.CompressFormat format, int quality) {
        return save2Album(src, "", format, quality, false);
    }

    @Nullable
    public static File save2Album(Bitmap src, Bitmap.CompressFormat format, int quality, boolean recycle) {
        return save2Album(src, "", format, quality, recycle);
    }

    @Nullable
    public static File save2Album(Bitmap src, String dirName, Bitmap.CompressFormat format) {
        return save2Album(src, dirName, format, 100, false);
    }

    @Nullable
    public static File save2Album(Bitmap src, String dirName, Bitmap.CompressFormat format, boolean recycle) {
        return save2Album(src, dirName, format, 100, recycle);
    }

    @Nullable
    public static File save2Album(Bitmap src, String dirName, Bitmap.CompressFormat format, int quality) {
        return save2Album(src, dirName, format, quality, false);
    }

    @Nullable
    public static File save2Album(Bitmap src, String dirName, Bitmap.CompressFormat format, int quality, boolean recycle) {
        Uri contentUri;
        String safeDirName = TextUtils.isEmpty(dirName) ? Utils.getApp().getPackageName() : dirName;
        String suffix = Bitmap.CompressFormat.JPEG.equals(format) ? "JPG" : format.name();
        String fileName = System.currentTimeMillis() + "_" + quality + "." + suffix;
        if (Build.VERSION.SDK_INT < 29) {
            if (!UtilsBridge.isGranted("android.permission.WRITE_EXTERNAL_STORAGE")) {
                Log.e("ImageUtils", "save to album need storage permission");
                return null;
            }
            File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File destFile = new File(picDir, safeDirName + "/" + fileName);
            if (save(src, destFile, format, quality, recycle)) {
                UtilsBridge.notifySystemToScan(destFile);
                return destFile;
            }
            return null;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("_display_name", fileName);
        contentValues.put("mime_type", "image/*");
        if (Environment.getExternalStorageState().equals("mounted")) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else {
            contentUri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        }
        contentValues.put("relative_path", Environment.DIRECTORY_DCIM + "/" + safeDirName);
        contentValues.put("is_pending", (Integer) 1);
        Uri uri = Utils.getApp().getContentResolver().insert(contentUri, contentValues);
        if (uri == null) {
            return null;
        }
        OutputStream os = null;
        try {
            try {
                os = Utils.getApp().getContentResolver().openOutputStream(uri);
                src.compress(format, quality, os);
                contentValues.clear();
                contentValues.put("is_pending", (Integer) 0);
                Utils.getApp().getContentResolver().update(uri, contentValues, null, null);
                File uri2File = UtilsBridge.uri2File(uri);
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return uri2File;
            } catch (Throwable th) {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (Exception e3) {
            Utils.getApp().getContentResolver().delete(uri, null, null);
            e3.printStackTrace();
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
            return null;
        }
    }

    public static boolean isImage(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        return isImage(file.getPath());
    }

    public static boolean isImage(String filePath) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            if (options.outWidth > 0) {
                if (options.outHeight <= 0) {
                    return false;
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static ImageType getImageType(String filePath) {
        return getImageType(UtilsBridge.getFileByPath(filePath));
    }

    public static ImageType getImageType(File file) {
        ImageType type;
        if (file == null) {
            return null;
        }
        InputStream is = null;
        try {
            try {
                try {
                    is = new FileInputStream(file);
                    type = getImageType(is);
                } catch (IOException e) {
                    e.printStackTrace();
                    if (is != null) {
                        is.close();
                    }
                }
            } catch (Throwable th) {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        if (type == null) {
            is.close();
            return null;
        }
        try {
            is.close();
        } catch (IOException e4) {
            e4.printStackTrace();
        }
        return type;
    }

    private static ImageType getImageType(InputStream is) {
        if (is == null) {
            return null;
        }
        try {
            byte[] bytes = new byte[12];
            if (is.read(bytes) != -1) {
                return getImageType(bytes);
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ImageType getImageType(byte[] bytes) {
        String type = UtilsBridge.bytes2HexString(bytes).toUpperCase();
        if (type.contains("FFD8FF")) {
            return ImageType.TYPE_JPG;
        }
        if (type.contains("89504E47")) {
            return ImageType.TYPE_PNG;
        }
        if (type.contains("47494638")) {
            return ImageType.TYPE_GIF;
        }
        if (type.contains("49492A00") || type.contains("4D4D002A")) {
            return ImageType.TYPE_TIFF;
        }
        if (type.contains("424D")) {
            return ImageType.TYPE_BMP;
        }
        if (type.startsWith("52494646") && type.endsWith("57454250")) {
            return ImageType.TYPE_WEBP;
        }
        if (type.contains("00000100") || type.contains("00000200")) {
            return ImageType.TYPE_ICO;
        }
        return ImageType.TYPE_UNKNOWN;
    }

    private static boolean isJPEG(byte[] b) {
        return b.length >= 2 && b[0] == -1 && b[1] == -40;
    }

    private static boolean isGIF(byte[] b) {
        return b.length >= 6 && b[0] == 71 && b[1] == 73 && b[2] == 70 && b[3] == 56 && (b[4] == 55 || b[4] == 57) && b[5] == 97;
    }

    private static boolean isPNG(byte[] b) {
        return b.length >= 8 && b[0] == -119 && b[1] == 80 && b[2] == 78 && b[3] == 71 && b[4] == 13 && b[5] == 10 && b[6] == 26 && b[7] == 10;
    }

    private static boolean isBMP(byte[] b) {
        return b.length >= 2 && b[0] == 66 && b[1] == 77;
    }

    private static boolean isEmptyBitmap(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    public static Bitmap compressByScale(Bitmap src, int newWidth, int newHeight) {
        return scale(src, newWidth, newHeight, false);
    }

    public static Bitmap compressByScale(Bitmap src, int newWidth, int newHeight, boolean recycle) {
        return scale(src, newWidth, newHeight, recycle);
    }

    public static Bitmap compressByScale(Bitmap src, float scaleWidth, float scaleHeight) {
        return scale(src, scaleWidth, scaleHeight, false);
    }

    public static Bitmap compressByScale(Bitmap src, float scaleWidth, float scaleHeight, boolean recycle) {
        return scale(src, scaleWidth, scaleHeight, recycle);
    }

    public static byte[] compressByQuality(Bitmap src, @IntRange(from = 0, to = 100) int quality) {
        return compressByQuality(src, quality, false);
    }

    public static byte[] compressByQuality(Bitmap src, @IntRange(from = 0, to = 100) int quality, boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        src.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] bytes = baos.toByteArray();
        if (recycle && !src.isRecycled()) {
            src.recycle();
        }
        return bytes;
    }

    public static byte[] compressByQuality(Bitmap src, long maxByteSize) {
        return compressByQuality(src, maxByteSize, false);
    }

    public static byte[] compressByQuality(Bitmap src, long maxByteSize, boolean recycle) {
        byte[] bytes;
        if (isEmptyBitmap(src) || maxByteSize <= 0) {
            return new byte[0];
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        src.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.size() <= maxByteSize) {
            bytes = baos.toByteArray();
        } else {
            baos.reset();
            src.compress(Bitmap.CompressFormat.JPEG, 0, baos);
            if (baos.size() >= maxByteSize) {
                bytes = baos.toByteArray();
            } else {
                int st = 0;
                int end = 100;
                int mid = 0;
                while (st < end) {
                    mid = (st + end) / 2;
                    baos.reset();
                    src.compress(Bitmap.CompressFormat.JPEG, mid, baos);
                    int len = baos.size();
                    if (len == maxByteSize) {
                        break;
                    } else if (len > maxByteSize) {
                        end = mid - 1;
                    } else {
                        st = mid + 1;
                    }
                }
                if (end == mid - 1) {
                    baos.reset();
                    src.compress(Bitmap.CompressFormat.JPEG, st, baos);
                }
                bytes = baos.toByteArray();
            }
        }
        if (recycle && !src.isRecycled()) {
            src.recycle();
        }
        return bytes;
    }

    public static Bitmap compressBySampleSize(Bitmap src, int sampleSize) {
        return compressBySampleSize(src, sampleSize, false);
    }

    public static Bitmap compressBySampleSize(Bitmap src, int sampleSize, boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        src.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        if (recycle && !src.isRecycled()) {
            src.recycle();
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    public static Bitmap compressBySampleSize(Bitmap src, int maxWidth, int maxHeight) {
        return compressBySampleSize(src, maxWidth, maxHeight, false);
    }

    public static Bitmap compressBySampleSize(Bitmap src, int maxWidth, int maxHeight, boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        src.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        if (recycle && !src.isRecycled()) {
            src.recycle();
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    public static int[] getSize(String filePath) {
        return getSize(UtilsBridge.getFileByPath(filePath));
    }

    public static int[] getSize(File file) {
        if (file == null) {
            return new int[]{0, 0};
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
        return new int[]{opts.outWidth, opts.outHeight};
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int maxWidth, int maxHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        while (true) {
            if (height > maxHeight || width > maxWidth) {
                height >>= 1;
                width >>= 1;
                inSampleSize <<= 1;
            } else {
                return inSampleSize;
            }
        }
    }

    /* loaded from: classes4.dex */
    public enum ImageType {
        TYPE_JPG("jpg"),
        TYPE_PNG("png"),
        TYPE_GIF("gif"),
        TYPE_TIFF("tiff"),
        TYPE_BMP("bmp"),
        TYPE_WEBP("webp"),
        TYPE_ICO("ico"),
        TYPE_UNKNOWN("unknown");
        
        String value;

        ImageType(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }
}
