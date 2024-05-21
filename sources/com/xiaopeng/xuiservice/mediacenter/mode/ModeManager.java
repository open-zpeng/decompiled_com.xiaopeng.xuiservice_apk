package com.xiaopeng.xuiservice.mediacenter.mode;

import android.app.ActivityThread;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes5.dex */
public class ModeManager {
    private static final String KEY_MODE_CHANGE_SETTINGS = "setting_switch";
    private static final String TAG = "ModeManager";
    private static ModeManager mInstance;
    private SystemModeSettingsObserver mModeSettingsObserver;
    private List<ModeChangedListener> mModeChangedListeners = new ArrayList();
    private int mCurrentMode = 0;
    private Context mContext = ActivityThread.currentActivityThread().getApplication();

    /* loaded from: classes5.dex */
    public interface ModeChangedListener {
        void onModeChanged(int i);
    }

    private ModeManager() {
    }

    public static ModeManager getInstance() {
        if (mInstance == null) {
            synchronized (ModeManager.class) {
                if (mInstance == null) {
                    mInstance = new ModeManager();
                }
            }
        }
        return mInstance;
    }

    public void init() {
        observerSystemMode();
    }

    public int getCurrentMode() {
        LogUtil.d(TAG, "getCurrentMode value--" + this.mCurrentMode);
        return this.mCurrentMode;
    }

    public synchronized void registerListener(ModeChangedListener listener) {
        synchronized (this.mModeChangedListeners) {
            if (!this.mModeChangedListeners.contains(listener)) {
                this.mModeChangedListeners.add(listener);
            }
        }
    }

    public synchronized void unregisterListener(ModeChangedListener listener) {
        synchronized (this.mModeChangedListeners) {
            if (this.mModeChangedListeners.contains(listener)) {
                this.mModeChangedListeners.remove(listener);
            }
        }
    }

    private void notifyListener(int mode) {
        for (ModeChangedListener listener : this.mModeChangedListeners) {
            listener.onModeChanged(mode);
        }
    }

    private void observerSystemMode() {
        this.mModeSettingsObserver = new SystemModeSettingsObserver(MediaCenterHalService.getInstance().getHandler());
        this.mModeSettingsObserver.register(this.mContext);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public final class SystemModeSettingsObserver extends ContentObserver {
        private final Uri mUri;

        public SystemModeSettingsObserver(Handler handler) {
            super(handler);
            this.mUri = Settings.System.getUriFor("setting_switch");
            ModeManager.this.getSystemMode();
        }

        public void register(Context context) {
            context.getContentResolver().registerContentObserver(this.mUri, false, this, -1);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, Uri uri, int userId) {
            if (this.mUri.equals(uri)) {
                ModeManager.this.notifyModeChange();
            }
        }
    }

    private void dealModeChage(int oldMode, int newMode) {
        LogUtil.i(TAG, "dealModeChange:" + oldMode + "|" + newMode);
        notifyListener(newMode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyModeChange() {
        int newMode = getSystemMode();
        LogUtil.d(TAG, "notifyModeChange - " + newMode);
        int i = this.mCurrentMode;
        if (newMode != i) {
            dealModeChage(i, newMode);
            this.mCurrentMode = newMode;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSystemMode() {
        int systemMode = Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "setting_switch", 0);
        return systemMode;
    }
}
