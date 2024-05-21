package com.xiaopeng.xuiservice.uvccamera.opengl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import com.xiaopeng.xuiservice.uvccamera.utils.AssetsHelper;
import java.io.IOException;
import org.apache.commons.compress.archivers.tar.TarConstants;
/* loaded from: classes5.dex */
public final class GLHelper {
    private static final String TAG = "GLHelper";

    public static void checkGlError(String op) {
        int error = GLES20.glGetError();
        if (error != 0) {
            String msg = op + ": glError 0x" + Integer.toHexString(error);
            Log.e(TAG, msg);
            new Throwable(msg).printStackTrace();
        }
    }

    public static int initTex(int texTarget, int filter_param) {
        return initTex(texTarget, 33984, filter_param, filter_param, 33071);
    }

    public static int initTex(int texTarget, int texUnit, int min_filter, int mag_filter, int wrap) {
        int[] tex = new int[1];
        GLES20.glActiveTexture(texUnit);
        GLES20.glGenTextures(1, tex, 0);
        GLES20.glBindTexture(texTarget, tex[0]);
        GLES20.glTexParameteri(texTarget, 10242, wrap);
        GLES20.glTexParameteri(texTarget, 10243, wrap);
        GLES20.glTexParameteri(texTarget, 10241, min_filter);
        GLES20.glTexParameteri(texTarget, TarConstants.DEFAULT_BLKSIZE, mag_filter);
        return tex[0];
    }

    public static int[] initTexes(int n, int texTarget, int filter_param) {
        return initTexes(new int[n], texTarget, filter_param, filter_param, 33071);
    }

    public static int[] initTexes(@NonNull int[] texIds, int texTarget, int filter_param) {
        return initTexes(texIds, texTarget, filter_param, filter_param, 33071);
    }

    public static int[] initTexes(int n, int texTarget, int min_filter, int mag_filter, int wrap) {
        return initTexes(new int[n], texTarget, min_filter, mag_filter, wrap);
    }

    public static int[] initTexes(@NonNull int[] texIds, int texTarget, int min_filter, int mag_filter, int wrap) {
        int[] textureUnits = new int[1];
        GLES20.glGetIntegerv(34930, textureUnits, 0);
        Log.v(TAG, "GL_MAX_TEXTURE_IMAGE_UNITS=" + textureUnits[0]);
        int n = texIds.length > textureUnits[0] ? textureUnits[0] : texIds.length;
        for (int i = 0; i < n; i++) {
            texIds[i] = initTex(texTarget, ShaderConst.TEX_NUMBERS[i], min_filter, mag_filter, wrap);
        }
        return texIds;
    }

    public static int[] initTexes(int n, int texTarget, int texUnit, int min_filter, int mag_filter, int wrap) {
        return initTexes(new int[n], texTarget, texUnit, min_filter, mag_filter, wrap);
    }

    public static int[] initTexes(@NonNull int[] texIds, int texTarget, int texUnit, int filter_param) {
        return initTexes(texIds, texTarget, texUnit, filter_param, filter_param, 33071);
    }

    public static int[] initTexes(@NonNull int[] texIds, int texTarget, int texUnit, int min_filter, int mag_filter, int wrap) {
        int[] textureUnits = new int[1];
        GLES20.glGetIntegerv(34930, textureUnits, 0);
        int n = texIds.length > textureUnits[0] ? textureUnits[0] : texIds.length;
        for (int i = 0; i < n; i++) {
            texIds[i] = initTex(texTarget, texUnit, min_filter, mag_filter, wrap);
        }
        return texIds;
    }

    public static void deleteTex(int hTex) {
        int[] tex = {hTex};
        GLES20.glDeleteTextures(1, tex, 0);
    }

    public static void deleteTex(@NonNull int[] tex) {
        GLES20.glDeleteTextures(tex.length, tex, 0);
    }

    public static int loadTextureFromResource(Context context, int resId) {
        return loadTextureFromResource(context, resId, null);
    }

    @SuppressLint({"NewApi"})
    public static int loadTextureFromResource(Context context, int resId, Resources.Theme theme) {
        Drawable background;
        Bitmap bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawARGB(0, 0, 255, 0);
        if (Build.VERSION.SDK_INT >= 21) {
            background = context.getResources().getDrawable(resId, theme);
        } else {
            background = context.getResources().getDrawable(resId);
        }
        background.setBounds(0, 0, 256, 256);
        background.draw(canvas);
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        GLES20.glBindTexture(ShaderConst.GL_TEXTURE_2D, textures[0]);
        GLES20.glTexParameterf(ShaderConst.GL_TEXTURE_2D, 10241, 9728.0f);
        GLES20.glTexParameterf(ShaderConst.GL_TEXTURE_2D, TarConstants.DEFAULT_BLKSIZE, 9729.0f);
        GLES20.glTexParameterf(ShaderConst.GL_TEXTURE_2D, 10242, 10497.0f);
        GLES20.glTexParameterf(ShaderConst.GL_TEXTURE_2D, 10243, 10497.0f);
        GLUtils.texImage2D(ShaderConst.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        return textures[0];
    }

    public static int createTextureWithTextContent(String text) {
        Bitmap bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawARGB(0, 0, 255, 0);
        Paint textPaint = new Paint();
        textPaint.setTextSize(32.0f);
        textPaint.setAntiAlias(true);
        textPaint.setARGB(255, 255, 255, 255);
        canvas.drawText(text, 16.0f, 112.0f, textPaint);
        int texture = initTex(ShaderConst.GL_TEXTURE_2D, 33984, 9728, 9729, 10497);
        GLUtils.texImage2D(ShaderConst.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        return texture;
    }

    public static int loadShader(@NonNull Context context, String vss_asset, String fss_asset) {
        try {
            String vss = AssetsHelper.loadString(context.getAssets(), vss_asset);
            String fss = AssetsHelper.loadString(context.getAssets(), vss_asset);
            int program = loadShader(vss, fss);
            return program;
        } catch (IOException e) {
            return 0;
        }
    }

    public static int loadShader(String vss, String fss) {
        int fs;
        int[] iArr = new int[1];
        int vs = loadShader(35633, vss);
        if (vs == 0 || (fs = loadShader(35632, fss)) == 0) {
            return 0;
        }
        int program = GLES20.glCreateProgram();
        checkGlError("glCreateProgram");
        if (program == 0) {
            Log.e(TAG, "Could not create program");
        }
        GLES20.glAttachShader(program, vs);
        checkGlError("glAttachShader");
        GLES20.glAttachShader(program, fs);
        checkGlError("glAttachShader");
        GLES20.glLinkProgram(program);
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(program, 35714, linkStatus, 0);
        if (linkStatus[0] != 1) {
            Log.e(TAG, "Could not link program: ");
            Log.e(TAG, GLES20.glGetProgramInfoLog(program));
            GLES20.glDeleteProgram(program);
            return 0;
        }
        return program;
    }

    public static int loadShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType);
        checkGlError("glCreateShader type=" + shaderType);
        GLES20.glShaderSource(shader, source);
        GLES20.glCompileShader(shader);
        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, 35713, compiled, 0);
        if (compiled[0] == 0) {
            Log.e(TAG, "Could not compile shader " + shaderType + ":");
            StringBuilder sb = new StringBuilder();
            sb.append(" ");
            sb.append(GLES20.glGetShaderInfoLog(shader));
            Log.e(TAG, sb.toString());
            GLES20.glDeleteShader(shader);
            return 0;
        }
        return shader;
    }

    public static void checkLocation(int location, String label) {
        if (location < 0) {
            throw new RuntimeException("Unable to locate '" + label + "' in program");
        }
    }

    @SuppressLint({"InlinedApi"})
    public static void logVersionInfo() {
        Log.i(TAG, "vendor  : " + GLES20.glGetString(7936));
        Log.i(TAG, "renderer: " + GLES20.glGetString(7937));
        Log.i(TAG, "version : " + GLES20.glGetString(7938));
        if (Build.VERSION.SDK_INT >= 18) {
            int[] values = new int[1];
            GLES30.glGetIntegerv(33307, values, 0);
            int majorVersion = values[0];
            GLES30.glGetIntegerv(33308, values, 0);
            int minorVersion = values[0];
            if (GLES30.glGetError() == 0) {
                Log.i(TAG, "version: " + majorVersion + "." + minorVersion);
            }
        }
    }
}
