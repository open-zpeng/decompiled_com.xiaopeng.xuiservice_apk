package com.xiaopeng.xuiservice.cv.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
/* loaded from: classes5.dex */
public class ImageUtils {
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:8:0x0039 -> B:26:0x004c). Please submit an issue!!! */
    public static Bitmap ByteToBitmap(byte[] data, Camera.Size previewSize) {
        Bitmap bm = null;
        ByteArrayOutputStream baos = null;
        try {
            try {
                try {
                    YuvImage yuvimage = new YuvImage(data, 20, previewSize.width, previewSize.height, null);
                    baos = new ByteArrayOutputStream();
                    yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 80, baos);
                    byte[] jdata = baos.toByteArray();
                    bm = BitmapFactory.decodeByteArray(jdata, 0, jdata.length);
                    baos.flush();
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (baos != null) {
                        baos.flush();
                        baos.close();
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return bm;
        } catch (Throwable th) {
            if (baos != null) {
                try {
                    baos.flush();
                    baos.close();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
            throw th;
        }
    }

    public static Bitmap Base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, 0);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static Bitmap resizeBitmap(Bitmap bm, int ivbWidth, int ivbHeight) {
        try {
            int width = bm.getWidth();
            int height = bm.getHeight();
            Matrix matrix = new Matrix();
            float scaleWidth = ivbWidth / width;
            float scaleHeight = ivbHeight / height;
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap resizeBmp = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
            return resizeBmp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap cropBitmap(Bitmap bitmap, int x1, int y1, int x2, int y2) {
        if (x1 + x2 + y2 + y1 == 0) {
            return null;
        }
        int cropWidth = x2 - x1;
        int cropHeight = y2 - y1;
        return Bitmap.createBitmap(bitmap, x1, y1, cropWidth, cropHeight, (Matrix) null, false);
    }

    public static boolean saveImage2Gallery(Bitmap bmp) {
        boolean isSuccess = false;
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Avm";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();
            return isSuccess;
        } catch (IOException e) {
            e.printStackTrace();
            return isSuccess;
        }
    }
}
