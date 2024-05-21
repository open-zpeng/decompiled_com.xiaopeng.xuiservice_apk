package com.xiaopeng.xuiservice.download.util;

import android.net.Uri;
/* loaded from: classes5.dex */
public class DownloadUtils {
    public static String getNameFromUrl(String url) {
        return Uri.parse(url).getLastPathSegment();
    }

    public static String getNameFromUrl(Uri uri) {
        return uri.getLastPathSegment();
    }
}
