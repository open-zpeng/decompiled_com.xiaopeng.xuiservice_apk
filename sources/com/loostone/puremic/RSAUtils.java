package com.loostone.puremic;

import android.util.Base64;
import com.irdeto.securesdk.upgrade.O00000Oo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
/* loaded from: classes4.dex */
public final class RSAUtils {
    private static String RSA = O00000Oo.O000000o;
    public static String SIGALGORITHM = "SHA1WithRSA";

    public static KeyPair generateRSAKeyPair() {
        return generateRSAKeyPair(1024);
    }

    public static KeyPair generateRSAKeyPair(int keyLength) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
            kpg.initialize(keyLength);
            return kpg.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] encryptData(byte[] data, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(1, publicKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] decryptData(byte[] encryptedData, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(2, privateKey);
            return cipher.doFinal(encryptedData);
        } catch (Exception e) {
            return null;
        }
    }

    private static String signData(String message, PrivateKey privateKey) throws Exception {
        try {
            byte[] data = message.getBytes();
            Signature sig = Signature.getInstance(SIGALGORITHM);
            sig.initSign(privateKey);
            sig.update(data);
            byte[] signatureBytes = sig.sign();
            return Base64.encodeToString(signatureBytes, 0);
        } catch (InvalidKeyException e) {
            throw new Exception("Invalid key");
        } catch (NoSuchAlgorithmException e2) {
            throw new Exception("Unknown signature algorithm " + SIGALGORITHM);
        } catch (SignatureException e3) {
            throw new Exception("Signing failed: " + e3.toString());
        }
    }

    private static Boolean verifyData(String message, String signature, PublicKey publicKey) throws Exception {
        try {
            byte[] data = message.getBytes();
            byte[] signatureBytes = Base64.decode(signature, 0);
            Signature sig = Signature.getInstance(SIGALGORITHM);
            sig.initVerify(publicKey);
            sig.update(data);
            return Boolean.valueOf(sig.verify(signatureBytes));
        } catch (InvalidKeyException e) {
            throw new Exception("Invalid key");
        } catch (NoSuchAlgorithmException e2) {
            throw new Exception("Unknown signature algorithm " + SIGALGORITHM);
        } catch (SignatureException e3) {
            throw new Exception("Signing failed: " + e3.toString());
        }
    }

    public static String Encrypt(String data, String pkey) {
        if (data == null) {
            return null;
        }
        try {
            PublicKey publicKey = loadPublicKey(pkey);
            byte[] encryptByte = encryptData(data.getBytes(), publicKey);
            String afterencrypt = Base64.encodeToString(encryptByte, 0);
            return afterencrypt;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String Decrypt(String data, String skey) {
        if (data == null) {
            return null;
        }
        try {
            PrivateKey privateKey = loadPrivateKey(skey);
            byte[] decryptByte = decryptData(Base64.decode(data, 0), privateKey);
            String decryptStr = new String(decryptByte);
            return decryptStr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String SignData(String data, String skey) {
        try {
            PrivateKey privateKey = loadPrivateKey(skey);
            return signData(data, privateKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Boolean VerifyData(String data, String signature, String pkey) {
        try {
            PublicKey publicKey = loadPublicKey(pkey);
            return verifyData(data, signature, publicKey);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static PublicKey getPublicKey(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    public static PrivateKey getPrivateKey(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    public static PublicKey getPublicKey(String modulus, String publicExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        BigInteger bigIntModulus = new BigInteger(modulus);
        BigInteger bigIntPrivateExponent = new BigInteger(publicExponent);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    public static PrivateKey getPrivateKey(String modulus, String privateExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        BigInteger bigIntModulus = new BigInteger(modulus);
        BigInteger bigIntPrivateExponent = new BigInteger(privateExponent);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    public static PublicKey loadPublicKey(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decode(publicKeyStr, 0);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NullPointerException e) {
            throw new Exception("NullPointer");
        } catch (NoSuchAlgorithmException e2) {
            throw new Exception("no such Algorithm");
        } catch (InvalidKeySpecException e3) {
            throw new Exception("InvalidKey");
        }
    }

    public static PrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decode(privateKeyStr, 0);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NullPointerException e) {
            throw new Exception("NullPointer");
        } catch (NoSuchAlgorithmException e2) {
            throw new Exception("no such Algorithm");
        } catch (InvalidKeySpecException e3) {
            throw new Exception("InvalidKey");
        }
    }

    public static PublicKey loadPublicKey(InputStream in) throws Exception {
        try {
            return loadPublicKey(readKey(in));
        } catch (IOException e) {
            throw new Exception("IOException");
        } catch (NullPointerException e2) {
            throw new Exception("NullPointerException");
        }
    }

    public static PrivateKey loadPrivateKey(InputStream in) throws Exception {
        try {
            return loadPrivateKey(readKey(in));
        } catch (IOException e) {
            throw new Exception("IOException");
        } catch (NullPointerException e2) {
            throw new Exception("NullPointerException");
        }
    }

    private static String readKey(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        while (true) {
            String readLine = br.readLine();
            if (readLine != null) {
                if (readLine.charAt(0) != '-') {
                    sb.append(readLine);
                    sb.append('\r');
                }
            } else {
                return sb.toString();
            }
        }
    }

    public static void printPublicKeyInfo(PublicKey publicKey) {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
        System.out.println("----------RSAPublicKey----------");
        PrintStream printStream = System.out;
        printStream.println("Modulus.length=" + rsaPublicKey.getModulus().bitLength());
        PrintStream printStream2 = System.out;
        printStream2.println("Modulus=" + rsaPublicKey.getModulus().toString());
        PrintStream printStream3 = System.out;
        printStream3.println("PublicExponent.length=" + rsaPublicKey.getPublicExponent().bitLength());
        PrintStream printStream4 = System.out;
        printStream4.println("PublicExponent=" + rsaPublicKey.getPublicExponent().toString());
    }

    public static void printPrivateKeyInfo(PrivateKey privateKey) {
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;
        System.out.println("----------RSAPrivateKey ----------");
        PrintStream printStream = System.out;
        printStream.println("Modulus.length=" + rsaPrivateKey.getModulus().bitLength());
        PrintStream printStream2 = System.out;
        printStream2.println("Modulus=" + rsaPrivateKey.getModulus().toString());
        PrintStream printStream3 = System.out;
        printStream3.println("PrivateExponent.length=" + rsaPrivateKey.getPrivateExponent().bitLength());
        PrintStream printStream4 = System.out;
        printStream4.println("PrivatecExponent=" + rsaPrivateKey.getPrivateExponent().toString());
    }
}
