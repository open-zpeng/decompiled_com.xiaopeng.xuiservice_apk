package com.acrcloud.rec.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import kotlin.UByte;
/* loaded from: classes4.dex */
public class ACRCloudUtils {
    public static double calculateVolume(byte[] buffer, int len) {
        int size = len >> 3;
        float fDB = 0.0f;
        float mu = 0.0f;
        for (int i = 0; i < size; i++) {
            short retVal = (short) ((buffer[i << 3] & UByte.MAX_VALUE) | ((short) (buffer[(i << 3) + 1] << 8)));
            int temp = ((retVal >> 15) ^ retVal) - (retVal >> 15);
            fDB += temp * temp;
            mu += temp;
        }
        float mu2 = mu / size;
        double db = Math.log10(((fDB / size) - (mu2 * mu2)) + 1.0f);
        return Math.min(db, 8.0d) / 8.0d;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
            return false;
        }
        return false;
    }

    public static byte[] pcm2Wav(byte[] pcmBuffer, int sampleRate, int nchannels) {
        if (pcmBuffer == null) {
            return null;
        }
        int pcmBufferLen = pcmBuffer.length;
        int totalAudiolen = pcmBufferLen + 36;
        int byteRate = ((sampleRate * 16) * nchannels) / 8;
        byte[] header = {82, 73, 70, 70, (byte) (totalAudiolen & 255), (byte) ((totalAudiolen >> 8) & 255), (byte) ((totalAudiolen >> 16) & 255), (byte) ((totalAudiolen >> 24) & 255), 87, 65, 86, 69, 102, 109, 116, 32, 16, 0, 0, 0, 1, 0, (byte) nchannels, 0, (byte) (sampleRate & 255), (byte) ((sampleRate >> 8) & 255), (byte) ((sampleRate >> 16) & 255), (byte) ((sampleRate >> 24) & 255), (byte) (byteRate & 255), (byte) ((byteRate >> 8) & 255), (byte) ((byteRate >> 16) & 255), (byte) ((byteRate >> 24) & 255), (byte) ((nchannels * 16) / 8), 0, 16, 0, 100, 97, 116, 97, (byte) (pcmBufferLen & 255), (byte) ((pcmBufferLen >> 8) & 255), (byte) ((pcmBufferLen >> 16) & 255), (byte) ((pcmBufferLen >> 24) & 255)};
        byte[] wavBuffer = new byte[header.length + pcmBufferLen];
        System.arraycopy(header, 0, wavBuffer, 0, header.length);
        System.arraycopy(pcmBuffer, 0, wavBuffer, header.length, pcmBuffer.length);
        return wavBuffer;
    }

    public static void createFileWithByte(byte[] bytes, String fileName) {
        File file = new File(fileName);
        FileOutputStream outputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            try {
                try {
                    if (file.exists()) {
                        file.delete();
                    }
                    file.createNewFile();
                    outputStream = new FileOutputStream(file);
                    bufferedOutputStream = new BufferedOutputStream(outputStream);
                    bufferedOutputStream.write(bytes);
                    bufferedOutputStream.flush();
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    bufferedOutputStream.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    if (bufferedOutputStream != null) {
                        bufferedOutputStream.close();
                    }
                }
            } catch (Throwable th) {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
                if (bufferedOutputStream != null) {
                    try {
                        bufferedOutputStream.close();
                    } catch (Exception e22) {
                        e22.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (Exception e23) {
            e23.printStackTrace();
        }
    }
}
