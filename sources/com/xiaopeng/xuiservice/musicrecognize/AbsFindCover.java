package com.xiaopeng.xuiservice.musicrecognize;

import android.os.Looper;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.utils.UiHandler;
import java.io.IOException;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
/* loaded from: classes5.dex */
abstract class AbsFindCover implements IFindCover {
    private static final String TAG = "AbsFindCover";
    private static final OkHttpClient sClient = new OkHttpClient();

    /* loaded from: classes5.dex */
    protected interface HttpCallback {
        void onFail(String str);

        void onResponse(String str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void runOnUiThread(Runnable runnable) {
        Thread uiThread = Looper.getMainLooper().getThread();
        if (Thread.currentThread() != uiThread) {
            UiHandler.getInstance().post(runnable);
        } else {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void httpRequest(String url, Map<String, String> parameters, final HttpCallback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        sClient.newCall(new Request.Builder().url(url).post(builder.build()).build()).enqueue(new Callback() { // from class: com.xiaopeng.xuiservice.musicrecognize.AbsFindCover.1
            @Override // okhttp3.Callback
            public void onFailure(Call call, IOException e) {
                callback.onFail(e.getMessage());
            }

            @Override // okhttp3.Callback
            public void onResponse(Call call, Response response) {
                try {
                    ResponseBody body = response.body();
                    if (body != null) {
                        String json = body.string();
                        callback.onResponse(json);
                    }
                } catch (Exception e) {
                    LogUtil.e(AbsFindCover.TAG, "onResponse failed, " + e);
                }
            }
        });
    }
}
