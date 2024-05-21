package com.ta.utdid2.device;

import android.content.Context;
import android.provider.Settings;
import com.ta.utdid2.b.a.g;
import com.ta.utdid2.b.a.i;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Random;
import java.util.regex.Pattern;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
/* compiled from: UTUtdid.java */
/* loaded from: classes4.dex */
public class c {

    /* renamed from: a  reason: collision with other field name */
    private com.ta.utdid2.c.a.c f196a;

    /* renamed from: a  reason: collision with other field name */
    private d f197a;
    private com.ta.utdid2.c.a.c b;
    private String m;
    private Context mContext;
    private String n;
    private static final Object f = new Object();
    private static c a = null;
    private static final String o = ".UTSystemConfig" + File.separator + "Global";
    private String l = null;

    /* renamed from: a  reason: collision with other field name */
    private Pattern f198a = Pattern.compile("[^0-9a-zA-Z=/+]+");

    public c(Context context) {
        this.mContext = null;
        this.f197a = null;
        this.m = "xx_utdid_key";
        this.n = "xx_utdid_domain";
        this.f196a = null;
        this.b = null;
        this.mContext = context;
        this.b = new com.ta.utdid2.c.a.c(context, o, "Alvin2", false, true);
        this.f196a = new com.ta.utdid2.c.a.c(context, ".DataStorage", "ContextData", false, true);
        this.f197a = new d();
        this.m = String.format("K_%d", Integer.valueOf(i.a(this.m)));
        this.n = String.format("D_%d", Integer.valueOf(i.a(this.n)));
    }

    private void d() {
        com.ta.utdid2.c.a.c cVar = this.b;
        if (cVar != null) {
            if (i.m102a(cVar.getString("UTDID2"))) {
                String string = this.b.getString("UTDID");
                if (!i.m102a(string)) {
                    f(string);
                }
            }
            boolean z = false;
            if (!i.m102a(this.b.getString("DID"))) {
                this.b.remove("DID");
                z = true;
            }
            if (!i.m102a(this.b.getString("EI"))) {
                this.b.remove("EI");
                z = true;
            }
            if (!i.m102a(this.b.getString("SI"))) {
                this.b.remove("SI");
                z = true;
            }
            if (z) {
                this.b.commit();
            }
        }
    }

    public static c a(Context context) {
        if (context != null && a == null) {
            synchronized (f) {
                if (a == null) {
                    a = new c(context);
                    a.d();
                }
            }
        }
        return a;
    }

    private void f(String str) {
        com.ta.utdid2.c.a.c cVar;
        if (b(str)) {
            if (str.endsWith("\n")) {
                str = str.substring(0, str.length() - 1);
            }
            if (str.length() == 24 && (cVar = this.b) != null) {
                cVar.putString("UTDID2", str);
                this.b.commit();
            }
        }
    }

    private void g(String str) {
        com.ta.utdid2.c.a.c cVar;
        if (str != null && (cVar = this.f196a) != null && !str.equals(cVar.getString(this.m))) {
            this.f196a.putString(this.m, str);
            this.f196a.commit();
        }
    }

    private void h(String str) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.WRITE_SETTINGS") == 0 && b(str)) {
            if (str.endsWith("\n")) {
                str = str.substring(0, str.length() - 1);
            }
            if (24 == str.length()) {
                String str2 = null;
                try {
                    str2 = Settings.System.getString(this.mContext.getContentResolver(), "mqBRboGZkQPcAkyk");
                } catch (Exception e) {
                }
                if (!b(str2)) {
                    try {
                        Settings.System.putString(this.mContext.getContentResolver(), "mqBRboGZkQPcAkyk", str);
                    } catch (Exception e2) {
                    }
                }
            }
        }
    }

    private void i(String str) {
        String str2;
        try {
            str2 = Settings.System.getString(this.mContext.getContentResolver(), "dxCRMxhQkdGePGnp");
        } catch (Exception e) {
            str2 = null;
        }
        if (!str.equals(str2)) {
            try {
                Settings.System.putString(this.mContext.getContentResolver(), "dxCRMxhQkdGePGnp", str);
            } catch (Exception e2) {
            }
        }
    }

    private void j(String str) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.WRITE_SETTINGS") == 0 && str != null) {
            i(str);
        }
    }

    private String g() {
        com.ta.utdid2.c.a.c cVar = this.b;
        if (cVar != null) {
            String string = cVar.getString("UTDID2");
            if (!i.m102a(string) && this.f197a.a(string) != null) {
                return string;
            }
            return null;
        }
        return null;
    }

    private boolean b(String str) {
        if (str != null) {
            if (str.endsWith("\n")) {
                str = str.substring(0, str.length() - 1);
            }
            if (24 == str.length() && !this.f198a.matcher(str).find()) {
                return true;
            }
        }
        return false;
    }

    public synchronized String getValue() {
        if (this.l != null) {
            return this.l;
        }
        return h();
    }

    public synchronized String h() {
        String str;
        String string;
        try {
            string = Settings.System.getString(this.mContext.getContentResolver(), "mqBRboGZkQPcAkyk");
        } catch (Exception e) {
        }
        if (b(string)) {
            return string;
        }
        e eVar = new e();
        boolean z = false;
        try {
            str = Settings.System.getString(this.mContext.getContentResolver(), "dxCRMxhQkdGePGnp");
        } catch (Exception e2) {
            str = null;
        }
        if (!i.m102a(str)) {
            String c = eVar.c(str);
            if (b(c)) {
                h(c);
                return c;
            }
            String b = eVar.b(str);
            if (b(b)) {
                String a2 = this.f197a.a(b);
                if (!i.m102a(a2)) {
                    j(a2);
                    try {
                        str = Settings.System.getString(this.mContext.getContentResolver(), "dxCRMxhQkdGePGnp");
                    } catch (Exception e3) {
                    }
                }
            }
            String b2 = this.f197a.b(str);
            if (b(b2)) {
                this.l = b2;
                f(b2);
                g(str);
                h(this.l);
                return this.l;
            }
        } else {
            z = true;
        }
        String g = g();
        if (b(g)) {
            String a3 = this.f197a.a(g);
            if (z) {
                j(a3);
            }
            h(g);
            g(a3);
            this.l = g;
            return g;
        }
        String string2 = this.f196a.getString(this.m);
        if (!i.m102a(string2)) {
            String b3 = eVar.b(string2);
            if (!b(b3)) {
                b3 = this.f197a.b(string2);
            }
            if (b(b3)) {
                String a4 = this.f197a.a(b3);
                if (!i.m102a(b3)) {
                    this.l = b3;
                    if (z) {
                        j(a4);
                    }
                    f(this.l);
                    return this.l;
                }
            }
        }
        try {
            byte[] a5 = a();
            if (a5 != null) {
                this.l = com.ta.utdid2.b.a.b.encodeToString(a5, 2);
                f(this.l);
                String c2 = this.f197a.c(a5);
                if (c2 != null) {
                    if (z) {
                        j(c2);
                    }
                    g(c2);
                }
                return this.l;
            }
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        return null;
    }

    private final byte[] a() throws Exception {
        String sb;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int nextInt = new Random().nextInt();
        byte[] bytes = com.ta.utdid2.b.a.e.getBytes((int) (System.currentTimeMillis() / 1000));
        byte[] bytes2 = com.ta.utdid2.b.a.e.getBytes(nextInt);
        byteArrayOutputStream.write(bytes, 0, 4);
        byteArrayOutputStream.write(bytes2, 0, 4);
        byteArrayOutputStream.write(3);
        byteArrayOutputStream.write(0);
        try {
            sb = g.a(this.mContext);
        } catch (Exception e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(new Random().nextInt());
            sb = sb2.toString();
        }
        byteArrayOutputStream.write(com.ta.utdid2.b.a.e.getBytes(i.a(sb)), 0, 4);
        byteArrayOutputStream.write(com.ta.utdid2.b.a.e.getBytes(i.a(b(byteArrayOutputStream.toByteArray()))));
        return byteArrayOutputStream.toByteArray();
    }

    private static String b(byte[] bArr) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec("d6fc3a4a06adbde89223bvefedc24fecde188aaa9161".getBytes(), mac.getAlgorithm()));
        return com.ta.utdid2.b.a.b.encodeToString(mac.doFinal(bArr), 2);
    }
}
