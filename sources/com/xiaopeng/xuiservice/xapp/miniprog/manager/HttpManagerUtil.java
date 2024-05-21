package com.xiaopeng.xuiservice.xapp.miniprog.manager;
/* loaded from: classes5.dex */
public class HttpManagerUtil {
    public static final String REQUEST_XP_MO = "XP-Mo";
    private static final String TAG = "HttpManagerUtil";

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0074  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x008f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String getCarType() {
        /*
            java.lang.String r0 = "HttpManagerUtil"
            java.lang.String r1 = ""
            java.lang.String r2 = android.car.Car.getXpCduType()     // Catch: java.lang.Exception -> L94
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L94
            r3.<init>()     // Catch: java.lang.Exception -> L94
            java.lang.String r4 = "carType: "
            r3.append(r4)     // Catch: java.lang.Exception -> L94
            r3.append(r2)     // Catch: java.lang.Exception -> L94
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Exception -> L94
            com.xiaopeng.xuimanager.utils.LogUtil.d(r0, r3)     // Catch: java.lang.Exception -> L94
            r3 = -1
            int r4 = r2.hashCode()     // Catch: java.lang.Exception -> L94
            r5 = 2560(0xa00, float:3.587E-42)
            r6 = 5
            r7 = 4
            r8 = 3
            r9 = 2
            r10 = 1
            if (r4 == r5) goto L69
            r5 = 2562(0xa02, float:3.59E-42)
            if (r4 == r5) goto L5f
            r5 = 79487(0x1367f, float:1.11385E-40)
            if (r4 == r5) goto L55
            switch(r4) {
                case 2566: goto L4b;
                case 2567: goto L41;
                case 2568: goto L37;
                default: goto L36;
            }     // Catch: java.lang.Exception -> L94
        L36:
            goto L72
        L37:
            java.lang.String r4 = "Q9"
            boolean r4 = r2.equals(r4)     // Catch: java.lang.Exception -> L94
            if (r4 == 0) goto L36
            r3 = r8
            goto L72
        L41:
            java.lang.String r4 = "Q8"
            boolean r4 = r2.equals(r4)     // Catch: java.lang.Exception -> L94
            if (r4 == 0) goto L36
            r3 = r9
            goto L72
        L4b:
            java.lang.String r4 = "Q7"
            boolean r4 = r2.equals(r4)     // Catch: java.lang.Exception -> L94
            if (r4 == 0) goto L36
            r3 = 0
            goto L72
        L55:
            java.lang.String r4 = "Q3A"
            boolean r4 = r2.equals(r4)     // Catch: java.lang.Exception -> L94
            if (r4 == 0) goto L36
            r3 = r6
            goto L72
        L5f:
            java.lang.String r4 = "Q3"
            boolean r4 = r2.equals(r4)     // Catch: java.lang.Exception -> L94
            if (r4 == 0) goto L36
            r3 = r7
            goto L72
        L69:
            java.lang.String r4 = "Q1"
            boolean r4 = r2.equals(r4)     // Catch: java.lang.Exception -> L94
            if (r4 == 0) goto L36
            r3 = r10
        L72:
            if (r3 == 0) goto L8f
            if (r3 == r10) goto L8b
            if (r3 == r9) goto L87
            if (r3 == r8) goto L83
            if (r3 == r7) goto L7f
            if (r3 == r6) goto L7f
            goto L93
        L7f:
            java.lang.String r0 = "D55"
            r1 = r0
            goto L93
        L83:
            java.lang.String r0 = "F30"
            r1 = r0
            goto L93
        L87:
            java.lang.String r0 = "E28A"
            r1 = r0
            goto L93
        L8b:
            java.lang.String r0 = "E28"
            r1 = r0
            goto L93
        L8f:
            java.lang.String r0 = "E38"
            r1 = r0
        L93:
            goto La9
        L94:
            r2 = move-exception
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "can not get getCarType error = "
            r3.append(r4)
            r3.append(r2)
            java.lang.String r3 = r3.toString()
            com.xiaopeng.xuimanager.utils.LogUtil.e(r0, r3)
        La9:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xuiservice.xapp.miniprog.manager.HttpManagerUtil.getCarType():java.lang.String");
    }
}
