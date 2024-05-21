package com.xiaopeng.xuiservice.mediacenter.icm;

import android.app.ActivityThread;
import android.graphics.Bitmap;
import com.xiaopeng.xuimanager.mediacenter.MediaInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.capabilities.XpIcmAgent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
/* loaded from: classes5.dex */
public abstract class ICMHandler {
    private static final String TAG = "ICMHandler";

    public abstract void sendIcmMediaInfoData(MediaInfo mediaInfo, int i, long[] jArr, boolean z);

    public void setMusicInfo(byte[] basic, byte[] image) {
        XpIcmAgent.getInstance(ActivityThread.currentActivityThread().getApplication()).setMusicInfo(basic, image);
    }

    public byte[] getBitmapByte(Bitmap bmp) {
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            if (bmp != null) {
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            }
            byte[] byteArray = out.toByteArray();
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return byteArray;
        } catch (Throwable th) {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            throw th;
        }
    }

    public void printMediaDataLog(String tag, String data, boolean withImage, Bitmap bitmap) {
        int imageWidth = 0;
        int imageHeight = 0;
        if (withImage && bitmap != null) {
            imageWidth = bitmap.getWidth();
            imageHeight = bitmap.getHeight();
        }
        if (withImage) {
            LogUtil.i(tag, "&image width:" + imageWidth + "&height:" + imageHeight + "&mediaData:" + data);
            return;
        }
        LogUtil.d(tag, "&image width:" + imageWidth + "&height:" + imageHeight + "&mediaData:" + data);
    }
}
