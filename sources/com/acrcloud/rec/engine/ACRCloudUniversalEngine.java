package com.acrcloud.rec.engine;

import com.acrcloud.rec.utils.ACRCloudLogger;
/* loaded from: classes4.dex */
public class ACRCloudUniversalEngine {
    private static final String TAG = "ACRCloudUniversalEngine";

    protected static native byte[] native_create_fingerprint(byte[] bArr, int i, int i2, String str, String str2, boolean z);

    protected static native byte[] native_create_humming_fingerprint(byte[] bArr, int i, boolean z);

    protected static native byte[] native_encrypt(byte[] bArr, int i, byte[] bArr2, int i2);

    protected static native byte[] native_resample(byte[] bArr, int i, int i2, int i3, int i4);

    protected static native void native_set_log(boolean z);

    /* JADX INFO: Access modifiers changed from: protected */
    public native int native_tinyalsa_get_buffer_size(long j);

    protected native int native_tinyalsa_get_recording_state(long j);

    /* JADX INFO: Access modifiers changed from: protected */
    public native long native_tinyalsa_init(int i, int i2, int i3, int i4, int i5, int i6, int i7);

    /* JADX INFO: Access modifiers changed from: protected */
    public native byte[] native_tinyalsa_read(long j, int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public native void native_tinyalsa_release(long j);

    static {
        try {
            System.loadLibrary(TAG);
        } catch (Exception e) {
            System.err.println("ACRCloudUniversalEngine loadLibrary error!");
        }
    }

    public static byte[] createFingerprint(byte[] buffer, int bufferLen, String ekey, String skey, int muteThreshold, boolean isFixOptimizing) {
        if (buffer == null || bufferLen <= 0) {
            return null;
        }
        if (bufferLen > buffer.length) {
            bufferLen = buffer.length;
        }
        return native_create_fingerprint(buffer, bufferLen, muteThreshold, ekey, skey, isFixOptimizing);
    }

    public static byte[] createFingerprint(byte[] buffer, int bufferLen, int muteThreshold, boolean isFixOptimizing) {
        if (buffer == null || bufferLen <= 0) {
            return null;
        }
        if (bufferLen > buffer.length) {
            bufferLen = buffer.length;
        }
        return native_create_fingerprint(buffer, bufferLen, muteThreshold, null, null, isFixOptimizing);
    }

    public static byte[] createFingerprint(byte[] buffer, int bufferLen, int rate, int channels, String ekey, String skey, int muteThreshold, int quality, boolean isFixOptimizing) {
        byte[] buffer2 = buffer;
        int bufferLen2 = bufferLen;
        ACRCloudLogger.e(TAG, "bufferLen=" + bufferLen + "; rate=" + rate + "; channels=" + channels + "; quality=" + quality);
        if (buffer2 == null || bufferLen2 <= 0) {
            return null;
        }
        if (bufferLen2 > buffer2.length) {
            bufferLen2 = buffer2.length;
        }
        if (rate != 8000 || channels != 1) {
            byte[] tBuffer = resample(buffer, bufferLen2, rate, channels, quality);
            if (tBuffer == null) {
                return null;
            }
            buffer2 = tBuffer;
            bufferLen2 = buffer2.length;
        }
        return native_create_fingerprint(buffer2, bufferLen2, muteThreshold, ekey, skey, isFixOptimizing);
    }

    public static byte[] createFingerprint(byte[] buffer, int bufferLen, int rate, int channels, int muteThreshold, int quality, boolean isFixOptimizing) {
        if (buffer == null || bufferLen <= 0) {
            return null;
        }
        if (bufferLen > buffer.length) {
            bufferLen = buffer.length;
        }
        if (rate != 8000 || channels != 1) {
            byte[] tBuffer = resample(buffer, bufferLen, rate, channels, quality);
            if (tBuffer == null) {
                return null;
            }
            buffer = tBuffer;
            bufferLen = buffer.length;
        }
        return native_create_fingerprint(buffer, bufferLen, muteThreshold, null, null, isFixOptimizing);
    }

    public static byte[] createHummingFingerprint(byte[] buffer, int bufferLen, int rate, int channels, int quality, boolean isFixOptimizing) {
        if (buffer == null || bufferLen <= 0) {
            return null;
        }
        if (bufferLen > buffer.length) {
            bufferLen = buffer.length;
        }
        if (rate != 8000 || channels != 1) {
            byte[] tBuffer = resample(buffer, bufferLen, rate, channels, quality);
            if (tBuffer == null) {
                return null;
            }
            buffer = tBuffer;
            bufferLen = buffer.length;
        }
        return native_create_humming_fingerprint(buffer, bufferLen, isFixOptimizing);
    }

    public static byte[] resample(byte[] buffer, int bufferLen, int sampleRate, int nChannels, int quality) {
        if (buffer == null || bufferLen <= 0) {
            return null;
        }
        if (bufferLen > buffer.length) {
            bufferLen = buffer.length;
        }
        if (sampleRate == 8000 && nChannels == 1) {
            return buffer;
        }
        return native_resample(buffer, bufferLen, nChannels, sampleRate, quality);
    }

    public static String encrypt(String value, String key) {
        if (value == null || "".equals(value)) {
            return null;
        }
        byte[] valueBytes = value.getBytes();
        byte[] keyBytes = key.getBytes();
        byte[] re = native_encrypt(valueBytes, valueBytes.length, keyBytes, keyBytes.length);
        if (re == null) {
            return null;
        }
        return new String(re);
    }

    public static void setLog(boolean isLog) {
        native_set_log(isLog);
    }
}
