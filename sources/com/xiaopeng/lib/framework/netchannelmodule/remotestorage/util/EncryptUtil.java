package com.xiaopeng.lib.framework.netchannelmodule.remotestorage.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import kotlin.UByte;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
/* loaded from: classes.dex */
public class EncryptUtil {
    public static String SHA1(String decript) {
        try {
            MessageDigest digest = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_1);
            digest.update(decript.getBytes());
            byte[] messageDigest = digest.digest();
            StringBuffer hexString = new StringBuffer();
            for (byte b : messageDigest) {
                String shaHex = Integer.toHexString(b & UByte.MAX_VALUE);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String MD5(String input) {
        try {
            MessageDigest mdInst = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
            mdInst.update(input.getBytes());
            byte[] md = mdInst.digest();
            StringBuffer hexString = new StringBuffer();
            for (byte b : md) {
                String shaHex = Integer.toHexString(b & UByte.MAX_VALUE);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
