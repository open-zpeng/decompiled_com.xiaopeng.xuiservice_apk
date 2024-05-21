package com.xiaopeng.xuiservice.uvccamera.utils;

import android.content.res.AssetManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/* loaded from: classes5.dex */
public class AssetsHelper {
    public static String loadString(AssetManager assets, String name) throws IOException {
        StringBuffer sb = new StringBuffer();
        char[] buf = new char[1024];
        BufferedReader reader = new BufferedReader(new InputStreamReader(assets.open(name)));
        for (int r = reader.read(buf); r > 0; r = reader.read(buf)) {
            sb.append(buf, 0, r);
        }
        return sb.toString();
    }
}
