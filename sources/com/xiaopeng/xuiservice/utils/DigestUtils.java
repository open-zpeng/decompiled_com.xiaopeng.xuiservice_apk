package com.xiaopeng.xuiservice.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
/* loaded from: classes5.dex */
public class DigestUtils {
    private static final int FILE_READ_BUFFER_SIZE = 16384;

    private DigestUtils() {
    }

    public static byte[] getSha256Hash(File file) throws IOException, NoSuchAlgorithmException {
        InputStream stream = new FileInputStream(file);
        try {
            byte[] sha256Hash = getSha256Hash(stream);
            $closeResource(null, stream);
            return sha256Hash;
        } finally {
        }
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 == null) {
            x1.close();
            return;
        }
        try {
            x1.close();
        } catch (Throwable th) {
            x0.addSuppressed(th);
        }
    }

    public static byte[] getSha256Hash(InputStream stream) throws IOException, NoSuchAlgorithmException {
        MessageDigest digester = MessageDigest.getInstance("SHA256");
        byte[] buf = new byte[16384];
        while (true) {
            int bytesRead = stream.read(buf);
            if (bytesRead >= 0) {
                digester.update(buf, 0, bytesRead);
            } else {
                return digester.digest();
            }
        }
    }

    public static String getMD5HashString(File file) throws IOException, NoSuchAlgorithmException {
        InputStream stream = new FileInputStream(file);
        try {
            String byteArrayToHex = byteArrayToHex(getMD5Hash(stream));
            $closeResource(null, stream);
            return byteArrayToHex;
        } finally {
        }
    }

    public static byte[] getMD5Hash(InputStream stream) throws IOException, NoSuchAlgorithmException {
        MessageDigest digester = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
        byte[] buf = new byte[16384];
        while (true) {
            int bytesRead = stream.read(buf);
            if (bytesRead >= 0) {
                digester.update(buf, 0, bytesRead);
            } else {
                return digester.digest();
            }
        }
    }

    public static String byteArrayToHex(byte[] byteArray) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] resultCharArray = new char[byteArray.length * 2];
        int index = 0;
        for (byte b : byteArray) {
            int index2 = index + 1;
            resultCharArray[index] = hexDigits[(b >>> 4) & 15];
            index = index2 + 1;
            resultCharArray[index2] = hexDigits[b & 15];
        }
        return new String(resultCharArray);
    }
}
