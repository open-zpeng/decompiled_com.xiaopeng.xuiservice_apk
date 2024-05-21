package com.xiaopeng.xuiservice.xapp.store;

import android.app.ActivityThread;
import android.content.ContentUris;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.Nullable;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.util.HashMap;
/* loaded from: classes5.dex */
public class AppStoreStatusProvider {
    private static final String AUTHORITY = "appstore";
    private static final String COLUMN_KEY = "key";
    public static final int DOWNLOAD_START_NOT_DONE_AGREEMENT = 105;
    public static final int DOWNLOAD_START_NOT_LOGIN = 100;
    public static final int DOWNLOAD_START_SATISFY = 0;
    private static final int INDEX_ASSEMBLE_STATE = 5;
    public static final int INDEX_CAN_START_DOWNLOAD = 0;
    private static final int INDEX_PROMPT_TEXT = 4;
    private static final int INDEX_PROMPT_TITLE = 3;
    private static final String PACKAGE_NAME_COLUMN = "packageName";
    private static final int PACKAGE_NAME_INDEX = 0;
    private static final int RES_TYPE_APP = 1000;
    public static final int STATE_FORCE_UPDATE = 1051;
    private static final int STATE_INDEX = 1;
    public static final int STATE_NORMAL_UPDATE = 100;
    public static final int STATE_OFF_SHELF = 1081;
    public static final int STATE_SUSPENDED = 1001;
    public static final int STATE_TIPS = 1002;
    private static final String TAG = "AppStoreStatusProvider";
    private static AppStoreStatusProvider mInstance;
    ContentObserver mContentObserver = new ContentObserver(XuiWorkHandler.getInstance()) { // from class: com.xiaopeng.xuiservice.xapp.store.AppStoreStatusProvider.2
        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, @Nullable Uri uri) {
            super.onChange(selfChange, uri);
            if (uri.equals(AppStoreStatusProvider.REMOTE_STATE_PROVIDER_URI)) {
                AppStoreStatusProvider.this.syncAppOnlineStatus(ActivityThread.currentActivityThread().getApplication());
            }
        }
    };
    private static final Uri APP_STORE_AUTHORITY_URI = Uri.parse("content://appstore");
    public static final String REMOTE_STATE_PATH = "remote_states";
    private static final Uri REMOTE_STATE_PROVIDER_URI = Uri.withAppendedPath(APP_STORE_AUTHORITY_URI, REMOTE_STATE_PATH);
    private static final Uri ASSEMBLE_URI = Uri.parse("content://resource_service/assemble");
    private static final Uri ASSEMBLE_URI_APP = ContentUris.withAppendedId(ASSEMBLE_URI, 1000);
    public static final String LOCAL_STATE_PATH = "app_local_states";
    public static final Uri LOCAL_STATE_PROVIDER_URI = Uri.withAppendedPath(APP_STORE_AUTHORITY_URI, LOCAL_STATE_PATH);
    private static HashMap<String, Integer> mAppOnlineStatus = new HashMap<>();

    private AppStoreStatusProvider() {
        init();
    }

    public static AppStoreStatusProvider getInstance() {
        if (mInstance == null) {
            synchronized (AppStoreStatusProvider.class) {
                if (mInstance == null) {
                    mInstance = new AppStoreStatusProvider();
                }
            }
        }
        return mInstance;
    }

    private void init() {
        registerAppOnlineStatusChanged();
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.store.AppStoreStatusProvider.1
            @Override // java.lang.Runnable
            public void run() {
                AppStoreStatusProvider.this.syncAppOnlineStatus(ActivityThread.currentActivityThread().getApplication().getApplicationContext());
            }
        });
    }

    public String[] getAppTipsContent(String pkgName) {
        Cursor cursor;
        try {
            cursor = ActivityThread.currentActivityThread().getApplication().getContentResolver().query(REMOTE_STATE_PROVIDER_URI, null, "packageName LIKE ?", new String[]{pkgName}, null);
        } catch (Exception e) {
            LogUtil.w(TAG, "syncAppOnlineStatus e= " + e);
        }
        if (cursor == null || cursor.getCount() == 0 || !cursor.moveToFirst()) {
            if (cursor != null) {
                $closeResource(null, cursor);
            }
            return null;
        }
        String tipsTitle = cursor.getString(3);
        String tipsText = cursor.getString(4);
        LogUtil.d(TAG, "getAppTipsText pkgName:=" + pkgName + " tipsTitle" + tipsTitle + " & tipsText=" + tipsText);
        String[] strArr = {tipsTitle, tipsText};
        $closeResource(null, cursor);
        return strArr;
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 == null) {
            x1.close();
            return;
        }
        try {
            x1.close();
        } catch (Throwable th) {
            x0.addSuppressed(th);
        }
    }

    private void registerAppOnlineStatusChanged() {
        try {
            ActivityThread.currentActivityThread().getApplication().getContentResolver().registerContentObserver(REMOTE_STATE_PROVIDER_URI, true, this.mContentObserver);
        } catch (Exception e) {
            LogUtil.w(TAG, "registerAppOnlineStatusChanged failed:" + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncAppOnlineStatus(Context context) {
        LogUtil.d(TAG, "syncAppOnlineStatus start");
        mAppOnlineStatus.clear();
        try {
            Cursor cursor = context.getContentResolver().query(REMOTE_STATE_PROVIDER_URI, null, null, null);
            if (cursor != null && cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    String pkgName = cursor.getString(0);
                    int state = cursor.getInt(1);
                    mAppOnlineStatus.put(pkgName, Integer.valueOf(state));
                    LogUtil.d(TAG, "syncAppOnlineStatus pkgName:=" + pkgName + " state=" + state);
                }
            }
            if (cursor != null) {
                $closeResource(null, cursor);
            }
        } catch (Exception e) {
            LogUtil.w(TAG, "syncAppOnlineStatus e= " + e);
        }
    }

    public int queryAppOnlineStatus(Context context, String packageName) {
        int status = 0;
        if (mAppOnlineStatus.containsKey(packageName)) {
            status = mAppOnlineStatus.get(packageName).intValue();
        }
        LogUtil.i(TAG, "queryAppOnlineStatus: packageName=" + packageName + " &currentStatus:" + status);
        return status;
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0070  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean queryIsAssembling(android.content.Context r9, java.lang.String r10) {
        /*
            r8 = this;
            android.content.ContentResolver r0 = r9.getContentResolver()
            android.net.Uri r1 = com.xiaopeng.xuiservice.xapp.store.AppStoreStatusProvider.ASSEMBLE_URI_APP
            r6 = 1
            java.lang.String[] r4 = new java.lang.String[r6]
            r7 = 0
            r4[r7] = r10
            r2 = 0
            java.lang.String r3 = "key LIKE ?"
            r5 = 0
            android.database.Cursor r0 = r0.query(r1, r2, r3, r4, r5)
            java.lang.String r1 = "AppStoreStatusProvider"
            r2 = 0
            if (r0 == 0) goto L5a
            int r3 = r0.getCount()     // Catch: java.lang.Throwable -> L58
            if (r3 == 0) goto L5a
            boolean r3 = r0.moveToFirst()     // Catch: java.lang.Throwable -> L58
            if (r3 == 0) goto L6e
            r3 = 5
            int r3 = r0.getInt(r3)     // Catch: java.lang.Throwable -> L58
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L58
            r4.<init>()     // Catch: java.lang.Throwable -> L58
            java.lang.String r5 = "queryIsAssembling finish: pn="
            r4.append(r5)     // Catch: java.lang.Throwable -> L58
            r4.append(r10)     // Catch: java.lang.Throwable -> L58
            java.lang.String r5 = " state="
            r4.append(r5)     // Catch: java.lang.Throwable -> L58
            r4.append(r3)     // Catch: java.lang.Throwable -> L58
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Throwable -> L58
            com.xiaopeng.xuimanager.utils.LogUtil.d(r1, r4)     // Catch: java.lang.Throwable -> L58
            boolean r1 = isRunning(r3)     // Catch: java.lang.Throwable -> L58
            if (r1 != 0) goto L54
            boolean r1 = isPreparing(r3)     // Catch: java.lang.Throwable -> L58
            if (r1 == 0) goto L53
            goto L54
        L53:
            r6 = r7
        L54:
            $closeResource(r2, r0)
            return r6
        L58:
            r1 = move-exception
            goto L74
        L5a:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L58
            r3.<init>()     // Catch: java.lang.Throwable -> L58
            java.lang.String r4 = "queryIsAssembling finish: no data relative to "
            r3.append(r4)     // Catch: java.lang.Throwable -> L58
            r3.append(r10)     // Catch: java.lang.Throwable -> L58
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Throwable -> L58
            com.xiaopeng.xuimanager.utils.LogUtil.d(r1, r3)     // Catch: java.lang.Throwable -> L58
        L6e:
            if (r0 == 0) goto L73
            $closeResource(r2, r0)
        L73:
            return r7
        L74:
            throw r1     // Catch: java.lang.Throwable -> L75
        L75:
            r2 = move-exception
            if (r0 == 0) goto L7b
            $closeResource(r1, r0)
        L7b:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xuiservice.xapp.store.AppStoreStatusProvider.queryIsAssembling(android.content.Context, java.lang.String):boolean");
    }

    public static boolean isPreparing(int state) {
        return state >= 1 && state < 100;
    }

    public static boolean isRunning(int state) {
        return state >= 100 && state < 200;
    }

    public int queryCanDownloadStatus(Context context) {
        Cursor cursor = context.getContentResolver().query(LOCAL_STATE_PROVIDER_URI, null, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.getCount() != 0 && cursor.moveToFirst()) {
                    int i = cursor.getInt(0);
                    $closeResource(null, cursor);
                    return i;
                }
            } finally {
            }
        }
        if (cursor != null) {
            $closeResource(null, cursor);
        }
        return 0;
    }
}
