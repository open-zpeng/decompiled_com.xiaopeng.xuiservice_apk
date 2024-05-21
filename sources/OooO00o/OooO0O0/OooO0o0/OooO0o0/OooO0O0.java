package OooO00o.OooO0O0.OooO0O0.OooO0o0;
/* loaded from: classes.dex */
public class OooO0O0 {
    /* JADX WARN: Removed duplicated region for block: B:26:0x0053  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0054 A[Catch: IOException -> 0x0064, TRY_LEAVE, TryCatch #1 {IOException -> 0x0064, blocks: (B:3:0x0013, B:24:0x004d, B:23:0x004a, B:27:0x0054, B:7:0x0021, B:8:0x002a, B:10:0x0033, B:19:0x0042), top: B:37:0x0013, inners: #3 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String OooO00o(java.lang.String r8) {
        /*
            java.lang.String r0 = ""
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "loostone/"
            r1.append(r2)
            r1.append(r8)
            java.lang.String r8 = r1.toString()
            android.content.Context r1 = OooO00o.OooO0O0.OooO0O0.OooO00o.OooO0O0     // Catch: java.io.IOException -> L64
            android.content.res.AssetManager r1 = r1.getAssets()     // Catch: java.io.IOException -> L64
            java.io.InputStream r8 = r1.open(r8)     // Catch: java.io.IOException -> L64
            r1 = 0
            if (r8 != 0) goto L21
            goto L51
        L21:
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream     // Catch: java.lang.Throwable -> L3f java.io.IOException -> L41
            r2.<init>()     // Catch: java.lang.Throwable -> L3f java.io.IOException -> L41
            r3 = 8192(0x2000, float:1.14794E-41)
            byte[] r4 = new byte[r3]     // Catch: java.lang.Throwable -> L3f java.io.IOException -> L41
        L2a:
            r5 = 0
            int r6 = r8.read(r4, r5, r3)     // Catch: java.lang.Throwable -> L3f java.io.IOException -> L41
            r7 = -1
            if (r6 == r7) goto L37
            r2.write(r4, r5, r6)     // Catch: java.lang.Throwable -> L3f java.io.IOException -> L41
            goto L2a
        L37:
            r8.close()     // Catch: java.io.IOException -> L3c
            r1 = r2
            goto L4d
        L3c:
            r8 = move-exception
            r1 = r2
            goto L4a
        L3f:
            r1 = move-exception
            goto L5b
        L41:
            r2 = move-exception
            r2.printStackTrace()     // Catch: java.lang.Throwable -> L3f
            r8.close()     // Catch: java.io.IOException -> L49
            goto L4d
        L49:
            r8 = move-exception
        L4a:
            r8.printStackTrace()     // Catch: java.io.IOException -> L64
        L4d:
            byte[] r1 = r1.toByteArray()     // Catch: java.io.IOException -> L64
        L51:
            if (r1 != 0) goto L54
            goto L68
        L54:
            java.lang.String r8 = new java.lang.String     // Catch: java.io.IOException -> L64
            r8.<init>(r1)     // Catch: java.io.IOException -> L64
            r0 = r8
            goto L68
        L5b:
            r8.close()     // Catch: java.io.IOException -> L5f
            goto L63
        L5f:
            r8 = move-exception
            r8.printStackTrace()     // Catch: java.io.IOException -> L64
        L63:
            throw r1     // Catch: java.io.IOException -> L64
        L64:
            r8 = move-exception
            r8.printStackTrace()
        L68:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0O0.OooO00o(java.lang.String):java.lang.String");
    }
}
