package com.acrcloud.rec.utils;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import com.acrcloud.rec.ACRCloudConfig;
import com.acrcloud.rec.network.ACRCloudHttpWrapper;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes4.dex */
public class ACRCloudDeviceLoginAsyncTask extends AsyncTask<String, Void, Void> {
    private static final String TAG = "ACRCloudDeviceLoginAsyncTask";
    private ACRCloudConfig mConfig;
    private String mLoginHost;
    private String mLoginUrl;
    private String mLoginAction = "/v1/device";
    private int mRetryNum = 3;

    public ACRCloudDeviceLoginAsyncTask(ACRCloudConfig config) {
        this.mLoginHost = "https://api.acrcloud.com";
        this.mLoginUrl = "";
        this.mConfig = config;
        if (this.mConfig.host.indexOf("identify-cn") != -1) {
            this.mLoginHost = "https://cn-api.acrcloud.com";
        }
        this.mLoginUrl = this.mLoginHost + this.mLoginAction;
        ACRCloudLogger.d(TAG, this.mLoginUrl);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Void doInBackground(String... params) {
        for (int i = 0; i < this.mRetryNum; i++) {
            try {
                Map<String, Object> initParams = new HashMap<>();
                initParams.put("access_key", this.mConfig.accessKey);
                initParams.put("dkey", params[0]);
                initParams.put(SpeechConstants.KEY_COMMAND_TYPE, "device");
                ACRCloudHttpWrapper.doPost(this.mLoginUrl, initParams, 5000);
                ACRCloudLogger.d(TAG, "login done");
                try {
                    if (this.mConfig.context != null) {
                        SharedPreferences mySharedPreferences = this.mConfig.context.getSharedPreferences("acrcloud", 0);
                        int loginNum = mySharedPreferences.getInt("login_num", 0);
                        SharedPreferences.Editor editor = mySharedPreferences.edit();
                        editor.putInt("login_num", loginNum + 1);
                        editor.commit();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(Void result) {
        super.onPreExecute();
    }
}
