package com.xiaopeng.xuiservice.xapp.temperature;

import android.app.ActivityThread;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import com.xiaopeng.xuimanager.utils.LogUtil;
/* loaded from: classes5.dex */
public class TemperatureManager {
    private static final String KEY_VALUE = "psn_temperature_adjustment";
    private static final String TAG = "TemperatureManager";
    private static TemperatureManager mInstance;
    private Callback mCallback;
    protected String APPLICATION_SUFFIX = ":s";
    protected String mCurrentStateValue = "";
    private ContentObserver mCallbackObserver = new ContentObserver(new Handler()) { // from class: com.xiaopeng.xuiservice.xapp.temperature.TemperatureManager.1
        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, Uri uri) {
            String[] value;
            super.onChange(selfChange, uri);
            if (uri.equals(Settings.System.getUriFor(TemperatureManager.KEY_VALUE))) {
                String dbValue = Settings.System.getString(TemperatureManager.this.mContentResolver, TemperatureManager.KEY_VALUE);
                LogUtil.d(TemperatureManager.TAG, "xptile settingprovider onChange " + dbValue + " key: psn_temperature_adjustment");
                if (!TextUtils.isEmpty(dbValue) && (value = dbValue.split(":")) != null && value.length > 1 && value[0].equals(value[1])) {
                    TemperatureManager temperatureManager = TemperatureManager.this;
                    temperatureManager.refreshState(temperatureManager.getCurrentState());
                }
            }
        }
    };
    private CarSettingsManager mCarSettingsManager = CarSettingsManager.getInstance();
    private ContentResolver mContentResolver = ActivityThread.currentActivityThread().getApplication().getContentResolver();

    /* loaded from: classes5.dex */
    public interface Callback {
        void onStateChanged(int i);
    }

    private TemperatureManager() {
        this.mContentResolver.registerContentObserver(Settings.System.getUriFor(KEY_VALUE), true, this.mCallbackObserver);
    }

    public static TemperatureManager getInstance() {
        if (mInstance == null) {
            synchronized (TemperatureManager.class) {
                if (mInstance == null) {
                    mInstance = new TemperatureManager();
                }
            }
        }
        return mInstance;
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public int getConvertValue(int value) {
        if (value % 10 == 0 || value % 5 == 0) {
            return value;
        }
        if (value % 10 > 5) {
            int result = ((value / 10) * 10) + 5;
            return result;
        }
        int result2 = (value / 10) * 10;
        return result2;
    }

    public int getMaxTemperature() {
        return this.mCarSettingsManager.getMaxTemperature();
    }

    public int getMinTemperature() {
        return this.mCarSettingsManager.getMinTemperature();
    }

    public void setProgress(int value) {
        int currentState = getCurrentState();
        int result = getConvertValue(value);
        saveContentProvider(currentState, result);
    }

    public int getCurrentState() {
        this.mCurrentStateValue = Settings.System.getString(this.mContentResolver, KEY_VALUE);
        LogUtil.d(TAG, "xptile contentprovider getvalue  key:psn_temperature_adjustment value:" + this.mCurrentStateValue);
        if (!TextUtils.isEmpty(this.mCurrentStateValue)) {
            String[] value = this.mCurrentStateValue.split(":");
            return Integer.parseInt(value[0]);
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshState(int state) {
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onStateChanged(state);
        }
    }

    protected void saveContentProvider(int state, int nextState) {
        String value = state + ":" + nextState + this.APPLICATION_SUFFIX;
        LogUtil.d(TAG, "xptile saveContentProvider  key:psn_temperature_adjustment & value:" + value + " mCurrentStateValue:" + this.mCurrentStateValue);
        Settings.System.putString(this.mContentResolver, KEY_VALUE, value);
        this.mCurrentStateValue = value;
    }
}
