package com.ta.utdid2.c.a;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import com.ta.utdid2.b.a.i;
import com.ta.utdid2.c.a.b;
import java.io.File;
import java.util.Map;
/* compiled from: PersistentConfiguration.java */
/* loaded from: classes4.dex */
public class c {

    /* renamed from: a  reason: collision with other field name */
    private SharedPreferences f189a;

    /* renamed from: a  reason: collision with other field name */
    private b f191a;

    /* renamed from: a  reason: collision with other field name */
    private d f192a;
    private String e;
    private String f;
    private boolean g;
    private boolean h;
    private boolean i;
    private boolean j;
    private Context mContext;
    private SharedPreferences.Editor a = null;

    /* renamed from: a  reason: collision with other field name */
    private b.a f190a = null;

    /* JADX WARN: Removed duplicated region for block: B:109:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:87:0x018e  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x019c A[Catch: Exception -> 0x01a9, TRY_LEAVE, TryCatch #0 {Exception -> 0x01a9, blocks: (B:88:0x0198, B:90:0x019c), top: B:95:0x0198 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public c(android.content.Context r10, java.lang.String r11, java.lang.String r12, boolean r13, boolean r14) {
        /*
            Method dump skipped, instructions count: 428
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.utdid2.c.a.c.<init>(android.content.Context, java.lang.String, java.lang.String, boolean, boolean):void");
    }

    private d a(String str) {
        File m104a = m104a(str);
        if (m104a != null) {
            this.f192a = new d(m104a.getAbsolutePath());
            return this.f192a;
        }
        return null;
    }

    /* renamed from: a  reason: collision with other method in class */
    private File m104a(String str) {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        if (externalStorageDirectory != null) {
            File file = new File(String.format("%s%s%s", externalStorageDirectory.getAbsolutePath(), File.separator, str));
            if (!file.exists()) {
                file.mkdirs();
            }
            return file;
        }
        return null;
    }

    private void a(SharedPreferences sharedPreferences, b bVar) {
        b.a a;
        if (sharedPreferences != null && bVar != null && (a = bVar.a()) != null) {
            a.b();
            for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof String) {
                    a.a(key, (String) value);
                } else if (value instanceof Integer) {
                    a.a(key, ((Integer) value).intValue());
                } else if (value instanceof Long) {
                    a.a(key, ((Long) value).longValue());
                } else if (value instanceof Float) {
                    a.a(key, ((Float) value).floatValue());
                } else if (value instanceof Boolean) {
                    a.a(key, ((Boolean) value).booleanValue());
                }
            }
            a.commit();
        }
    }

    private void a(b bVar, SharedPreferences sharedPreferences) {
        SharedPreferences.Editor edit;
        if (bVar != null && sharedPreferences != null && (edit = sharedPreferences.edit()) != null) {
            edit.clear();
            for (Map.Entry<String, ?> entry : bVar.getAll().entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof String) {
                    edit.putString(key, (String) value);
                } else if (value instanceof Integer) {
                    edit.putInt(key, ((Integer) value).intValue());
                } else if (value instanceof Long) {
                    edit.putLong(key, ((Long) value).longValue());
                } else if (value instanceof Float) {
                    edit.putFloat(key, ((Float) value).floatValue());
                } else if (value instanceof Boolean) {
                    edit.putBoolean(key, ((Boolean) value).booleanValue());
                }
            }
            edit.commit();
        }
    }

    private boolean b() {
        b bVar = this.f191a;
        if (bVar != null) {
            boolean mo103a = bVar.mo103a();
            if (!mo103a) {
                commit();
            }
            return mo103a;
        }
        return false;
    }

    private void c() {
        b bVar;
        SharedPreferences sharedPreferences;
        if (this.a == null && (sharedPreferences = this.f189a) != null) {
            this.a = sharedPreferences.edit();
        }
        if (this.i && this.f190a == null && (bVar = this.f191a) != null) {
            this.f190a = bVar.a();
        }
        b();
    }

    public void putString(String key, String value) {
        if (!i.m102a(key) && !key.equals("t")) {
            c();
            SharedPreferences.Editor editor = this.a;
            if (editor != null) {
                editor.putString(key, value);
            }
            b.a aVar = this.f190a;
            if (aVar != null) {
                aVar.a(key, value);
            }
        }
    }

    public void remove(String key) {
        if (!i.m102a(key) && !key.equals("t")) {
            c();
            SharedPreferences.Editor editor = this.a;
            if (editor != null) {
                editor.remove(key);
            }
            b.a aVar = this.f190a;
            if (aVar != null) {
                aVar.a(key);
            }
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(11:1|(4:3|(1:7)|8|(9:10|11|(1:15)|16|17|18|19|(4:21|(2:23|(2:25|(3:27|(1:29)(1:31)|30))(2:32|(1:36)))|37|(3:43|44|(1:46)))|49))|54|11|(2:13|15)|16|17|18|19|(0)|49) */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0039, code lost:
        r2 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x003a, code lost:
        r2.printStackTrace();
     */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0043  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean commit() {
        /*
            r6 = this;
            long r0 = java.lang.System.currentTimeMillis()
            android.content.SharedPreferences$Editor r2 = r6.a
            r3 = 0
            if (r2 == 0) goto L21
            boolean r4 = r6.j
            if (r4 != 0) goto L17
            android.content.SharedPreferences r4 = r6.f189a
            if (r4 == 0) goto L17
            java.lang.String r4 = "t"
            r2.putLong(r4, r0)
        L17:
            android.content.SharedPreferences$Editor r0 = r6.a
            boolean r0 = r0.commit()
            if (r0 != 0) goto L21
            r0 = r3
            goto L22
        L21:
            r0 = 1
        L22:
            android.content.SharedPreferences r1 = r6.f189a
            if (r1 == 0) goto L33
            android.content.Context r1 = r6.mContext
            if (r1 == 0) goto L33
            java.lang.String r2 = r6.e
            android.content.SharedPreferences r1 = r1.getSharedPreferences(r2, r3)
            r6.f189a = r1
        L33:
            r1 = 0
            java.lang.String r1 = android.os.Environment.getExternalStorageState()     // Catch: java.lang.Exception -> L39
            goto L3d
        L39:
            r2 = move-exception
            r2.printStackTrace()
        L3d:
            boolean r2 = com.ta.utdid2.b.a.i.m102a(r1)
            if (r2 != 0) goto Lad
            java.lang.String r2 = "mounted"
            boolean r4 = r1.equals(r2)
            if (r4 == 0) goto L89
            com.ta.utdid2.c.a.b r4 = r6.f191a
            if (r4 != 0) goto L7e
        L50:
            java.lang.String r4 = r6.f
            com.ta.utdid2.c.a.d r4 = r6.a(r4)
            if (r4 == 0) goto L89
            java.lang.String r5 = r6.e
            com.ta.utdid2.c.a.b r4 = r4.a(r5, r3)
            r6.f191a = r4
            boolean r4 = r6.j
            if (r4 != 0) goto L6e
            android.content.SharedPreferences r4 = r6.f189a
            com.ta.utdid2.c.a.b r5 = r6.f191a
            r6.a(r4, r5)
            goto L75
        L6e:
            com.ta.utdid2.c.a.b r4 = r6.f191a
            android.content.SharedPreferences r5 = r6.f189a
            r6.a(r4, r5)
        L75:
            com.ta.utdid2.c.a.b r4 = r6.f191a
            com.ta.utdid2.c.a.b$a r4 = r4.a()
            r6.f190a = r4
            goto L89
        L7e:
            com.ta.utdid2.c.a.b$a r4 = r6.f190a
            if (r4 == 0) goto L89
            boolean r4 = r4.commit()
            if (r4 != 0) goto L89
            r0 = r3
        L89:
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L9c
        L90:
            java.lang.String r2 = "mounted_ro"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto Lad
            com.ta.utdid2.c.a.b r1 = r6.f191a
            if (r1 == 0) goto Lad
        L9c:
            com.ta.utdid2.c.a.d r1 = r6.f192a     // Catch: java.lang.Exception -> Lac
            if (r1 == 0) goto Lad
            com.ta.utdid2.c.a.d r1 = r6.f192a     // Catch: java.lang.Exception -> Lac
            java.lang.String r2 = r6.e     // Catch: java.lang.Exception -> Lac
            com.ta.utdid2.c.a.b r1 = r1.a(r2, r3)     // Catch: java.lang.Exception -> Lac
            r6.f191a = r1     // Catch: java.lang.Exception -> Lac
            goto Lad
        Lac:
            r1 = move-exception
        Lad:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.utdid2.c.a.c.commit():boolean");
    }

    public String getString(String key) {
        b();
        SharedPreferences sharedPreferences = this.f189a;
        if (sharedPreferences != null) {
            String string = sharedPreferences.getString(key, "");
            if (!i.m102a(string)) {
                return string;
            }
        }
        b bVar = this.f191a;
        return bVar != null ? bVar.getString(key, "") : "";
    }
}
