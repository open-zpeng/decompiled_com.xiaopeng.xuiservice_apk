package com.xiaopeng.xuiservice.opensdk;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.EllipticCurve;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
/* loaded from: classes5.dex */
public class SecurityUtil {
    public static boolean verifyAppKey(String packageName, String appKey) {
        if (packageName == null || appKey == null) {
            return false;
        }
        try {
            Signature signature = Signature.getInstance("SHA256withECDSA");
            PublicKey publicKey = ecPublicKey();
            signature.initVerify(publicKey);
            signature.update(packageName.getBytes());
            return signature.verify(base64Decode(appKey));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static byte[] base64Decode(String base64) {
        return Base64.getDecoder().decode(base64);
    }

    private static PublicKey ecPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        ECPoint w = new ECPoint(new BigInteger("98536239937570091381349016063140841027202266507983536246632853046696126459053"), new BigInteger("77751423219411590423181027840614453877034068748914689313600065475378352029712"));
        EllipticCurve curve = new EllipticCurve(new ECFieldFp(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951")), new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853948"), new BigInteger("41058363725152142129326129780047268409114441015993725554835256314039467401291"), null);
        ECPoint g = new ECPoint(new BigInteger("48439561293906451759052585252797914202762949526041747995844080717082404635286"), new BigInteger("36134250956749795798585127919587881956611106672985015071877198253568414405109"));
        BigInteger n = new BigInteger("115792089210356248762697446949407573529996955224135760342422259061068512044369");
        ECParameterSpec ecParameterSpec = new ECParameterSpec(curve, g, n, 1);
        ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(w, ecParameterSpec);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePublic(ecPublicKeySpec);
    }
}
