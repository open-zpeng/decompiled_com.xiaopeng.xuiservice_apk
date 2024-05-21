package com.xiaopeng.xuiservice.innerutils;

import android.util.Log;
import com.xiaopeng.biutil.BiLog;
import com.xiaopeng.biutil.BiLogUploader;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.smart.SmartBiService;
/* loaded from: classes5.dex */
public class BiLogTransmit extends BiLogUploader {
    private static final String TAG = "BiLogTransmit";

    private BiLogTransmit() {
    }

    /* loaded from: classes5.dex */
    private static class Holder {
        private static final BiLogTransmit INSTANCE = new BiLogTransmit();

        private Holder() {
        }
    }

    public static BiLogTransmit getInstance() {
        return Holder.INSTANCE;
    }

    public synchronized void submit(BiLog biLog) {
        if (!XUIConfig.isInternationalEnable()) {
            if (!XUIConfig.BusinessModule.isMainProcess()) {
                super.submit(biLog);
            } else {
                Log.d(TAG, "submit " + biLog.getString());
                SmartBiService.getInstance().handleBiUploadEvent(biLog.getString());
            }
        }
    }
}
