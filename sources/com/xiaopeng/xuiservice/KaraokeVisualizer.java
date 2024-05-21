package com.xiaopeng.xuiservice;

import android.os.SystemClock;
import android.os.SystemProperties;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.karaoke.utils.ByteRingBuffer;
import com.xiaopeng.xuiservice.karaoke.utils.KaraokeAlgorithm;
import com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService;
/* loaded from: classes5.dex */
public class KaraokeVisualizer {
    public static final boolean DBG = true;
    public static final String TAG = "KaraokeVisualizer";
    private static int raptureRate = SystemProperties.getInt("persist.visualizer.rate", 1) * 50;
    private static int capturesize = SystemProperties.getInt("persist.fft.capture.size", 1024);
    private ByteRingBuffer mByteRingBuffer = null;
    private MediaCenterHalService mMediaCenterService = null;
    private int mSampleRate = 0;
    ResBufferingThread mBufferingthread = null;
    boolean bufRunning = true;

    public synchronized void init() {
    }

    public void VisualizerStart(int samplerate) {
        this.mMediaCenterService = MediaCenterHalService.getInstance();
        this.mSampleRate = samplerate * 1000;
        RingBufferCreate();
        ResBufferStart();
        MediaCenterHalService mediaCenterHalService = this.mMediaCenterService;
        if (mediaCenterHalService != null) {
            mediaCenterHalService.setKaraokeVisualizerOn(true);
        }
    }

    public void VisualizerStop() {
        ResBufferStop();
        RingBufferClear();
        MediaCenterHalService mediaCenterHalService = this.mMediaCenterService;
        if (mediaCenterHalService != null) {
            mediaCenterHalService.setKaraokeVisualizerOn(false);
        }
    }

    private void ResBufferStart() {
        LogUtil.i(TAG, "ResBufferStart");
        if (this.mBufferingthread == null) {
            this.bufRunning = true;
            this.mBufferingthread = new ResBufferingThread();
            Thread t1 = new Thread(this.mBufferingthread);
            t1.start();
        }
    }

    private void ResBufferStop() {
        LogUtil.i(TAG, "ResBufferStop");
        this.bufRunning = false;
        this.mBufferingthread = null;
    }

    public void ResBufferWrite(byte[] data, int size) {
        ByteRingBuffer byteRingBuffer = this.mByteRingBuffer;
        if (byteRingBuffer != null) {
            try {
                byteRingBuffer.write(data, 0, size);
            } catch (Exception e) {
                LogUtil.e(TAG, "ResBufferWrite ERROR:" + e);
            }
        }
    }

    private void RingBufferCreate() {
        LogUtil.i(TAG, "RingBufferCreate");
        if (this.mByteRingBuffer == null) {
            this.mByteRingBuffer = new ByteRingBuffer(2097152);
        }
    }

    public void RingBufferClear() {
        LogUtil.i(TAG, "RingBufferClear");
        ByteRingBuffer byteRingBuffer = this.mByteRingBuffer;
        if (byteRingBuffer != null) {
            byteRingBuffer.clear();
            this.mByteRingBuffer = null;
        }
    }

    /* loaded from: classes5.dex */
    public class ResBufferingThread extends Thread {
        public ResBufferingThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            int size = (((KaraokeVisualizer.this.mSampleRate / 1000) * 4) * KaraokeVisualizer.raptureRate) / 1000;
            LogUtil.i(KaraokeVisualizer.TAG, "ResBufferingThread  size:" + size + " mSampleRate:" + KaraokeVisualizer.this.mSampleRate + " raptureRate:" + KaraokeVisualizer.raptureRate + " capturesize:" + KaraokeVisualizer.capturesize);
            byte[] data = new byte[size];
            byte[] fft = new byte[KaraokeVisualizer.capturesize];
            while (KaraokeVisualizer.this.bufRunning) {
                try {
                    if (KaraokeVisualizer.this.mByteRingBuffer == null) {
                        try {
                            Thread.sleep(100L);
                        } catch (Exception e) {
                        }
                    }
                    SystemClock.currentThreadTimeMillis();
                    int used = KaraokeVisualizer.this.mByteRingBuffer.getUsed();
                    if (used >= size) {
                        int ret = KaraokeVisualizer.this.mByteRingBuffer.read(data, 0, size);
                        if (ret == -1) {
                            Thread.sleep(10L);
                        } else {
                            KaraokeAlgorithm.doFFt(data, fft);
                            if (KaraokeVisualizer.this.mMediaCenterService != null) {
                                KaraokeVisualizer.this.mMediaCenterService.karaokeFftCapture(fft, KaraokeVisualizer.this.mSampleRate);
                            }
                        }
                    } else {
                        Thread.sleep(10L);
                    }
                } catch (Exception e2) {
                    LogUtil.e(KaraokeVisualizer.TAG, "ResBufferingThread ERROR:" + e2);
                    return;
                }
            }
        }
    }
}
