package OooO00o.OooO00o.OooO00o.OooO00o;

import OooO00o.OooO00o.OooO00o.OooO00o.OooO0O0;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.os.Build;
import android.os.IBinder;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.irdeto.securesdk.core.SSUtils;
import com.irdeto.securesdk.upgrade.O00000Oo;
import com.loostone.libserver.version1.entity.PMConfig;
import com.lzy.okgo.model.Progress;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes.dex */
public final class OooO00o {
    public static void OooO00o(Context context, String str, String str2) {
        context.getApplicationContext().getSharedPreferences(SSUtils.O00000o0, 0).edit().putString(str, str2).commit();
    }

    public static String OooO0O0(String str) {
        String str2;
        if (str != null && str.length() > 10) {
            str2 = str.substring(str.length() - 6);
        } else {
            str2 = ".r1a5s";
        }
        return PMConfig.getIv() + str2;
    }

    public static String OooO0OO(String str) {
        String str2;
        String str3;
        if (str != null && str.length() > 10) {
            str2 = str.substring(0, 4);
            str3 = str.substring(str.length() - 4);
        } else {
            str2 = "s.1=";
            str3 = "4z;l";
        }
        return str2 + PMConfig.getKey() + str3;
    }

    /* JADX WARN: Removed duplicated region for block: B:56:0x0076 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:67:0x006c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:40:0x0060 -> B:64:0x0063). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String OooO0Oo(java.lang.String r5) {
        /*
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r1 = 0
            java.io.File r2 = new java.io.File     // Catch: java.lang.Throwable -> L47 java.io.IOException -> L4a
            r2.<init>(r5)     // Catch: java.lang.Throwable -> L47 java.io.IOException -> L4a
            boolean r2 = r2.exists()     // Catch: java.lang.Throwable -> L47 java.io.IOException -> L4a
            if (r2 == 0) goto L46
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L47 java.io.IOException -> L4a
            r2.<init>(r5)     // Catch: java.lang.Throwable -> L47 java.io.IOException -> L4a
            java.io.BufferedReader r5 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L42 java.io.IOException -> L44
            java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L42 java.io.IOException -> L44
            r3.<init>(r2)     // Catch: java.lang.Throwable -> L42 java.io.IOException -> L44
            r5.<init>(r3)     // Catch: java.lang.Throwable -> L42 java.io.IOException -> L44
        L20:
            java.lang.String r1 = r5.readLine()     // Catch: java.lang.Throwable -> L3a java.io.IOException -> L3d
            boolean r3 = android.text.TextUtils.isEmpty(r1)     // Catch: java.lang.Throwable -> L3a java.io.IOException -> L3d
            if (r3 != 0) goto L2e
            r0.append(r1)     // Catch: java.lang.Throwable -> L3a java.io.IOException -> L3d
            goto L20
        L2e:
            r5.close()     // Catch: java.io.IOException -> L32
            goto L36
        L32:
            r5 = move-exception
            r5.printStackTrace()
        L36:
            r2.close()     // Catch: java.io.IOException -> L5f
            goto L63
        L3a:
            r0 = move-exception
            r1 = r5
            goto L69
        L3d:
            r1 = move-exception
            r4 = r1
            r1 = r5
            r5 = r4
            goto L4c
        L42:
            r5 = move-exception
            goto L6a
        L44:
            r5 = move-exception
            goto L4c
        L46:
            return r1
        L47:
            r5 = move-exception
            r2 = r1
            goto L6a
        L4a:
            r5 = move-exception
            r2 = r1
        L4c:
            r5.printStackTrace()     // Catch: java.lang.Throwable -> L68
            if (r1 == 0) goto L59
            r1.close()     // Catch: java.io.IOException -> L55
            goto L59
        L55:
            r5 = move-exception
            r5.printStackTrace()
        L59:
            if (r2 == 0) goto L63
            r2.close()     // Catch: java.io.IOException -> L5f
            goto L63
        L5f:
            r5 = move-exception
            r5.printStackTrace()
        L63:
            java.lang.String r5 = r0.toString()
            return r5
        L68:
            r0 = move-exception
        L69:
            r5 = r0
        L6a:
            if (r1 == 0) goto L74
            r1.close()     // Catch: java.io.IOException -> L70
            goto L74
        L70:
            r0 = move-exception
            r0.printStackTrace()
        L74:
            if (r2 == 0) goto L7e
            r2.close()     // Catch: java.io.IOException -> L7a
            goto L7e
        L7a:
            r0 = move-exception
            r0.printStackTrace()
        L7e:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO0Oo(java.lang.String):java.lang.String");
    }

    public static String OooO0o(String str) {
        return str == null ? "" : str.trim();
    }

    public static String OooO0o0(String str) {
        FileInputStream fileInputStream;
        byte[] bArr = null;
        try {
            fileInputStream = new FileInputStream(str);
            try {
                bArr = new byte[fileInputStream.available()];
                fileInputStream.read(bArr);
            } catch (Exception e) {
            }
        } catch (Exception e2) {
            fileInputStream = null;
        }
        if (fileInputStream != null) {
            try {
                fileInputStream.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        if (bArr != null) {
            try {
                return new String(bArr, "UTF-8");
            } catch (UnsupportedEncodingException e4) {
                e4.printStackTrace();
            }
        }
        return "";
    }

    public static void OooO00o(UsbDevice usbDevice, Context context) {
        try {
            Class<?> cls = Class.forName("android.os.ServiceManager");
            IBinder iBinder = (IBinder) cls.getDeclaredMethod("getService", String.class).invoke(cls, "usb");
            Class<?>[] declaredClasses = Class.forName("android.hardware.usb.IUsbManager").getDeclaredClasses();
            Class<?> cls2 = null;
            int length = declaredClasses.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                Class<?> cls3 = declaredClasses[i];
                if ("Stub".equals(cls3.getSimpleName())) {
                    cls2 = cls3;
                    break;
                }
                i++;
            }
            Object invoke = cls2.getDeclaredMethod("asInterface", IBinder.class).invoke(cls2, iBinder);
            Class<?> cls4 = invoke.getClass();
            Class<?> cls5 = Integer.TYPE;
            cls4.getDeclaredMethod("grantDevicePermission", UsbDevice.class, cls5).invoke(invoke, usbDevice, Integer.valueOf(Process.myUid()));
            Class<?> cls6 = Class.forName("android.os.UserHandle");
            invoke.getClass().getDeclaredMethod("setDevicePackage", UsbDevice.class, String.class, cls5).invoke(invoke, context.getPackageName(), Integer.valueOf(((Integer) cls6.getDeclaredMethod("getUserId", cls5).invoke(cls6, Integer.valueOf(Process.myUid()))).intValue()));
        } catch (Exception e) {
            Intrinsics.checkNotNullParameter("OldCommonUtil - old", Progress.TAG);
            Intrinsics.checkNotNullParameter("ServiceManager Class except", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 5) {
                Log.e("LogTuning", "OldCommonUtil - old -> ServiceManager Class except");
            }
        }
    }

    public static String OooO00o(String str, String str2) {
        byte[] bArr;
        String OooO0O0 = OooO0O0(str2);
        String OooO0OO = OooO0OO(str2);
        if (str == null || str.length() == 0) {
            return "";
        }
        IvParameterSpec ivParameterSpec = new IvParameterSpec(OooO0O0.getBytes());
        SecretKeySpec secretKeySpec = new SecretKeySpec(OooO0OO.getBytes(), "AES");
        byte[] bArr2 = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(2, secretKeySpec, ivParameterSpec);
            if (str.length() < 2) {
                bArr = null;
            } else {
                int length = str.length() / 2;
                bArr = new byte[length];
                for (int i = 0; i < length; i++) {
                    int i2 = i * 2;
                    bArr[i] = (byte) Integer.parseInt(str.substring(i2, i2 + 2), 16);
                }
            }
            bArr2 = cipher.doFinal(bArr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bArr2 == null ? "" : new String(bArr2);
    }

    public static int OooO00o(String str) {
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        Pattern compile = Pattern.compile(" +(\\d) +(?=\\[\\w+\\s*]:)(?=.*" + str + ")");
        OooO0O0.OooO00o OooO00o2 = OooO0O0.OooO00o("cat /proc/asound/cards", false, true);
        if (OooO00o2.f213OooO00o != 0 || TextUtils.isEmpty(OooO00o2.OooO0O0)) {
            return -1;
        }
        for (String str2 : OooO00o2.OooO0O0.split("\r")) {
            Matcher matcher = compile.matcher(str2);
            if (matcher.find()) {
                try {
                    return Integer.parseInt(matcher.group().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(13:(4:6|7|(1:9)(1:150)|10)|(7:12|13|14|15|(3:17|(2:19|20)(2:22|23)|21)|24|25)|(18:27|28|29|31|32|34|35|(4:36|37|38|(1:40)(1:41))|(2:42|(1:44)(0))|46|(1:48)|(1:50)|52|53|(1:55)(1:60)|(1:57)|58|59)(1:133)|45|46|(0)|(0)|52|53|(0)(0)|(0)|58|59) */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x013b, code lost:
        if (r8 != null) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x00ce, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00cf, code lost:
        r0.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0114, code lost:
        if (r8 == null) goto L53;
     */
    /* JADX WARN: Removed duplicated region for block: B:109:0x012f A[Catch: IOException -> 0x012b, TryCatch #5 {IOException -> 0x012b, blocks: (B:105:0x0127, B:109:0x012f, B:111:0x0134), top: B:140:0x0127 }] */
    /* JADX WARN: Removed duplicated region for block: B:111:0x0134 A[Catch: IOException -> 0x012b, TRY_LEAVE, TryCatch #5 {IOException -> 0x012b, blocks: (B:105:0x0127, B:109:0x012f, B:111:0x0134), top: B:140:0x0127 }] */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0144  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0146  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x014d  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0161 A[Catch: IOException -> 0x015d, TryCatch #23 {IOException -> 0x015d, blocks: (B:128:0x0159, B:132:0x0161, B:134:0x0166), top: B:152:0x0159 }] */
    /* JADX WARN: Removed duplicated region for block: B:134:0x0166 A[Catch: IOException -> 0x015d, TRY_LEAVE, TryCatch #23 {IOException -> 0x015d, blocks: (B:128:0x0159, B:132:0x0161, B:134:0x0166), top: B:152:0x0159 }] */
    /* JADX WARN: Removed duplicated region for block: B:138:0x016f  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0127 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:142:0x0100 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:152:0x0159 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00c5 A[Catch: IOException -> 0x00ce, TryCatch #13 {IOException -> 0x00ce, blocks: (B:57:0x00c0, B:59:0x00c5, B:61:0x00ca), top: B:144:0x00c0 }] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x00ca A[Catch: IOException -> 0x00ce, TRY_LEAVE, TryCatch #13 {IOException -> 0x00ce, blocks: (B:57:0x00c0, B:59:0x00c5, B:61:0x00ca), top: B:144:0x00c0 }] */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0108 A[Catch: IOException -> 0x0104, TryCatch #7 {IOException -> 0x0104, blocks: (B:87:0x0100, B:91:0x0108, B:93:0x010d), top: B:142:0x0100 }] */
    /* JADX WARN: Removed duplicated region for block: B:93:0x010d A[Catch: IOException -> 0x0104, TRY_LEAVE, TryCatch #7 {IOException -> 0x0104, blocks: (B:87:0x0100, B:91:0x0108, B:93:0x010d), top: B:142:0x0100 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0o0.OooO0OO OooO00o(java.lang.String[] r7, boolean r8, boolean r9) {
        /*
            Method dump skipped, instructions count: 371
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO00o(java.lang.String[], boolean, boolean):OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0o0.OooO0OO");
    }

    public static byte[] OooO00o(byte[] bArr, byte[] bArr2, int i, String str) {
        KeyFactory keyFactory;
        if (bArr == null || bArr.length == 0 || bArr2 == null || bArr2.length == 0) {
            return null;
        }
        try {
            if (Build.VERSION.SDK_INT < 28) {
                keyFactory = KeyFactory.getInstance(O00000Oo.O000000o, "BC");
            } else {
                keyFactory = KeyFactory.getInstance(O00000Oo.O000000o);
            }
            PrivateKey generatePrivate = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(bArr2));
            if (generatePrivate == null) {
                return null;
            }
            Cipher cipher = Cipher.getInstance(str);
            cipher.init(2, generatePrivate);
            int length = bArr.length;
            int i2 = i / 8;
            int i3 = length / i2;
            if (i3 > 0) {
                byte[] bArr3 = new byte[i2];
                int i4 = 0;
                byte[] bArr4 = new byte[0];
                for (int i5 = 0; i5 < i3; i5++) {
                    System.arraycopy(bArr, i4, bArr3, 0, i2);
                    bArr4 = OooO00o(bArr4, cipher.doFinal(bArr3));
                    i4 += i2;
                }
                if (i4 != length) {
                    int i6 = length - i4;
                    byte[] bArr5 = new byte[i6];
                    System.arraycopy(bArr, i4, bArr5, 0, i6);
                    return OooO00o(bArr4, cipher.doFinal(bArr5));
                }
                return bArr4;
            }
            return cipher.doFinal(bArr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] OooO00o(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[bArr.length + bArr2.length];
        System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
        System.arraycopy(bArr2, 0, bArr3, bArr.length, bArr2.length);
        return bArr3;
    }
}
