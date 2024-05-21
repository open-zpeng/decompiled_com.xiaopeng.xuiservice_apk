package com.xiaopeng.lib.apirouter.server;

import android.os.IBinder;
import android.util.Pair;
import java.util.HashMap;
/* loaded from: classes.dex */
public class ManifestHelper {
    public static HashMap<String, Pair<IBinder, String>> mapping = new HashMap<>();

    static {
        Pair stub0 = new Pair(new IpcRouterService_Stub(), IpcRouterService_Manifest.toJsonManifest());
        for (String key : IpcRouterService_Manifest.getKey()) {
            mapping.put(key, stub0);
        }
    }
}
