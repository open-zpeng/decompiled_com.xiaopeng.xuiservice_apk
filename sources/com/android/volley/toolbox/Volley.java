package com.android.volley.toolbox;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import java.io.File;
/* loaded from: classes4.dex */
public class Volley {
    private static final String DEFAULT_CACHE_DIR = "volley";

    public static RequestQueue newRequestQueue(Context context, BaseHttpStack baseHttpStack) {
        BasicNetwork basicNetwork;
        String str;
        if (baseHttpStack == null) {
            if (Build.VERSION.SDK_INT >= 9) {
                basicNetwork = new BasicNetwork((BaseHttpStack) new HurlStack());
            } else {
                try {
                    String packageName = context.getPackageName();
                    PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
                    str = packageName + "/" + packageInfo.versionCode;
                } catch (PackageManager.NameNotFoundException e) {
                    str = "volley/0";
                }
                basicNetwork = new BasicNetwork(new HttpClientStack(AndroidHttpClient.newInstance(str)));
            }
        } else {
            basicNetwork = new BasicNetwork(baseHttpStack);
        }
        return newRequestQueue(context, basicNetwork);
    }

    @Deprecated
    public static RequestQueue newRequestQueue(Context context, HttpStack httpStack) {
        if (httpStack == null) {
            return newRequestQueue(context, (BaseHttpStack) null);
        }
        return newRequestQueue(context, new BasicNetwork(httpStack));
    }

    private static RequestQueue newRequestQueue(Context context, Network network) {
        RequestQueue requestQueue = new RequestQueue(new DiskBasedCache(new File(context.getCacheDir(), DEFAULT_CACHE_DIR)), network);
        requestQueue.start();
        return requestQueue;
    }

    public static RequestQueue newRequestQueue(Context context) {
        return newRequestQueue(context, (BaseHttpStack) null);
    }
}
