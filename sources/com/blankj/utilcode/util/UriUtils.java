package com.blankj.utilcode.util;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes4.dex */
public final class UriUtils {
    private UriUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static Uri res2Uri(String resPath) {
        return Uri.parse("android.resource://" + Utils.getApp().getPackageName() + "/" + resPath);
    }

    public static Uri file2Uri(File file) {
        if (UtilsBridge.isFileExists(file)) {
            if (Build.VERSION.SDK_INT >= 24) {
                String authority = Utils.getApp().getPackageName() + ".utilcode.fileprovider";
                return FileProvider.getUriForFile(Utils.getApp(), authority, file);
            }
            return Uri.fromFile(file);
        }
        return null;
    }

    public static File uri2File(Uri uri) {
        if (uri == null) {
            return null;
        }
        File file = uri2FileReal(uri);
        return file != null ? file : copyUri2Cache(uri);
    }

    /* JADX WARN: Removed duplicated region for block: B:68:0x0267  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x026a A[Catch: Exception -> 0x02d4, TryCatch #4 {Exception -> 0x02d4, blocks: (B:59:0x0240, B:61:0x024e, B:69:0x026a, B:71:0x0279, B:74:0x028b, B:76:0x0296, B:78:0x029c), top: B:161:0x0240 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.io.File uri2FileReal(android.net.Uri r26) {
        /*
            Method dump skipped, instructions count: 1161
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blankj.utilcode.util.UriUtils.uri2FileReal(android.net.Uri):java.io.File");
    }

    private static File getFileFromUri(Uri uri, String code) {
        return getFileFromUri(uri, null, null, code);
    }

    private static File getFileFromUri(Uri uri, String selection, String[] selectionArgs, String code) {
        if ("com.google.android.apps.photos.content".equals(uri.getAuthority())) {
            if (!TextUtils.isEmpty(uri.getLastPathSegment())) {
                return new File(uri.getLastPathSegment());
            }
        } else if ("com.tencent.mtt.fileprovider".equals(uri.getAuthority())) {
            String path = uri.getPath();
            if (!TextUtils.isEmpty(path)) {
                File fileDir = Environment.getExternalStorageDirectory();
                return new File(fileDir, path.substring("/QQBrowser".length(), path.length()));
            }
        } else if ("com.huawei.hidisk.fileprovider".equals(uri.getAuthority())) {
            String path2 = uri.getPath();
            if (!TextUtils.isEmpty(path2)) {
                return new File(path2.replace("/root", ""));
            }
        }
        Cursor cursor = Utils.getApp().getContentResolver().query(uri, new String[]{"_data"}, selection, selectionArgs, null);
        try {
            if (cursor == null) {
                Log.d("UriUtils", uri.toString() + " parse failed(cursor is null). -> " + code);
                return null;
            } else if (!cursor.moveToFirst()) {
                Log.d("UriUtils", uri.toString() + " parse failed(moveToFirst return false). -> " + code);
                return null;
            } else {
                int columnIndex = cursor.getColumnIndex("_data");
                if (columnIndex > -1) {
                    return new File(cursor.getString(columnIndex));
                }
                Log.d("UriUtils", uri.toString() + " parse failed(columnIndex: " + columnIndex + " is wrong). -> " + code);
                return null;
            }
        } catch (Exception e) {
            Log.d("UriUtils", uri.toString() + " parse failed. -> " + code);
            return null;
        } finally {
            cursor.close();
        }
    }

    private static File copyUri2Cache(Uri uri) {
        Log.d("UriUtils", "copyUri2Cache() called");
        InputStream is = null;
        try {
            try {
                is = Utils.getApp().getContentResolver().openInputStream(uri);
                File cacheDir = Utils.getApp().getCacheDir();
                File file = new File(cacheDir, "" + System.currentTimeMillis());
                UtilsBridge.writeFileFromIS(file.getAbsolutePath(), is);
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return file;
            } catch (FileNotFoundException e2) {
                e2.printStackTrace();
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
                return null;
            }
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
            throw th;
        }
    }

    public static byte[] uri2Bytes(Uri uri) {
        if (uri == null) {
            return null;
        }
        InputStream is = null;
        try {
            try {
                is = Utils.getApp().getContentResolver().openInputStream(uri);
                byte[] inputStream2Bytes = UtilsBridge.inputStream2Bytes(is);
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return inputStream2Bytes;
            } catch (Throwable th) {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (FileNotFoundException e3) {
            e3.printStackTrace();
            Log.d("UriUtils", "uri to bytes failed.");
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
            return null;
        }
    }
}
