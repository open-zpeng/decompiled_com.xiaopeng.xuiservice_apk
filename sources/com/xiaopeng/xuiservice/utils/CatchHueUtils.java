package com.xiaopeng.xuiservice.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemProperties;
import android.util.SparseArray;
import android.view.WindowManagerGlobal;
import androidx.core.view.ViewCompat;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/* loaded from: classes5.dex */
public class CatchHueUtils {
    private static final String TAG = "CatchHueUtils";
    private static CatchHueUtils sService = null;
    private Map<Integer, List<HueDataListener>> mListenersMap = new ConcurrentHashMap();
    private ScheduledThreadPoolExecutor mExecutor = new ScheduledThreadPoolExecutor(1);
    private ScheduledFuture<?> mFuture = null;
    private int PERIODIC = SystemProperties.getInt("persist.sys.xpeng.catch_hue_periodic", 200);
    private int mColor = 0;

    /* loaded from: classes5.dex */
    public interface HueDataListener {
        void onHueData(int i, int i2);
    }

    public static int findDominantColorByHue(Bitmap bitmap, int samples) {
        int height;
        int height2 = bitmap.getHeight();
        int width = bitmap.getWidth();
        int sampleStride = (int) Math.sqrt((height2 * width) / samples);
        if (sampleStride < 1) {
            sampleStride = 1;
        }
        float[] hsv = new float[3];
        float[] hueScoreHistogram = new float[360];
        float highScore = -1.0f;
        int bestHue = -1;
        int[] pixels = new int[samples];
        int pixelCount = 0;
        int y = 0;
        while (true) {
            char c = 0;
            if (y >= height2) {
                break;
            }
            int x = 0;
            while (x < width) {
                int argb = bitmap.getPixel(x, y);
                int alpha = (argb >> 24) & 255;
                if (alpha < 128) {
                    height = height2;
                } else {
                    int rgb = argb | ViewCompat.MEASURED_STATE_MASK;
                    Color.colorToHSV(rgb, hsv);
                    height = height2;
                    int hue = (int) hsv[c];
                    if (hue >= 0 && hue < hueScoreHistogram.length) {
                        if (pixelCount < samples) {
                            pixels[pixelCount] = rgb;
                            pixelCount++;
                        }
                        hueScoreHistogram[hue] = hueScoreHistogram[hue] + (hsv[1] * hsv[2]);
                        if (hueScoreHistogram[hue] > highScore) {
                            highScore = hueScoreHistogram[hue];
                            bestHue = hue;
                        }
                    }
                }
                x += sampleStride;
                height2 = height;
                c = 0;
            }
            y += sampleStride;
        }
        SparseArray<Float> rgbScores = new SparseArray<>();
        int bestColor = ViewCompat.MEASURED_STATE_MASK;
        float highScore2 = -1.0f;
        for (int i = 0; i < pixelCount; i++) {
            int rgb2 = pixels[i];
            Color.colorToHSV(rgb2, hsv);
            if (((int) hsv[0]) == bestHue) {
                float s = hsv[1];
                float v = hsv[2];
                int bucket = ((int) (s * 100.0f)) + ((int) (v * 10000.0f));
                float score = s * v;
                Float oldTotal = rgbScores.get(bucket);
                float newTotal = oldTotal == null ? score : oldTotal.floatValue() + score;
                rgbScores.put(bucket, Float.valueOf(newTotal));
                if (newTotal > highScore2) {
                    float highScore3 = newTotal;
                    bestColor = rgb2;
                    highScore2 = highScore3;
                }
            }
        }
        return bestColor;
    }

    public synchronized void registerHueDataListener(int displayId, HueDataListener listener) {
        LogUtil.d(TAG, "registerHueDataListener " + displayId);
        if (!this.mListenersMap.containsKey(Integer.valueOf(displayId))) {
            this.mListenersMap.put(Integer.valueOf(displayId), (List) Stream.of(listener).collect(Collectors.toList()));
        } else if (!this.mListenersMap.get(Integer.valueOf(displayId)).contains(listener)) {
            this.mListenersMap.get(Integer.valueOf(displayId)).add(listener);
        }
        if (this.mFuture == null || this.mFuture.isDone()) {
            LogUtil.d(TAG, "start capture color");
            this.mFuture = this.mExecutor.scheduleAtFixedRate(new Runnable() { // from class: com.xiaopeng.xuiservice.utils.CatchHueUtils.1
                @Override // java.lang.Runnable
                public void run() {
                    for (Integer num : CatchHueUtils.this.mListenersMap.keySet()) {
                        int id = num.intValue();
                        try {
                            int color = CatchHueUtils.findDominantColorByHue(WindowManagerGlobal.getWindowManagerService().screenshot(id, (Bundle) null), 100);
                            if (color != CatchHueUtils.this.mColor) {
                                LogUtil.d(CatchHueUtils.TAG, "getHueData, displayId=" + id + ", color=" + Integer.toHexString(color));
                                for (HueDataListener l : (List) CatchHueUtils.this.mListenersMap.get(Integer.valueOf(id))) {
                                    l.onHueData(id, color);
                                }
                                CatchHueUtils.this.mColor = color;
                            }
                        } catch (Exception e) {
                            LogUtil.e(CatchHueUtils.TAG, "getHueData failed " + e);
                        }
                    }
                }
            }, 0L, this.PERIODIC, TimeUnit.MILLISECONDS);
        }
    }

    public synchronized void unregisterHueDataListener(int displayId, HueDataListener listener) {
        if (this.mListenersMap.containsKey(Integer.valueOf(displayId)) && this.mListenersMap.get(Integer.valueOf(displayId)).contains(listener)) {
            LogUtil.d(TAG, "unregisterHueDataListener " + displayId);
            this.mListenersMap.get(Integer.valueOf(displayId)).remove(listener);
            if (this.mListenersMap.get(Integer.valueOf(displayId)).isEmpty()) {
                this.mListenersMap.remove(Integer.valueOf(displayId));
            }
            if (this.mListenersMap.isEmpty() && this.mFuture != null && !this.mFuture.isDone()) {
                LogUtil.d(TAG, "stop capture color");
                this.mFuture.cancel(true);
            }
        }
    }

    public static CatchHueUtils getInstance() {
        if (sService == null) {
            synchronized (CatchHueUtils.class) {
                if (sService == null) {
                    sService = new CatchHueUtils();
                }
            }
        }
        return sService;
    }
}
