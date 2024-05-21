package com.xiaopeng.xuiservice.mediacenter.helper;

import android.app.ActivityManager;
import android.app.ActivityThread;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.SystemProperties;
import android.text.TextUtils;
import com.xiaopeng.xuimanager.mediacenter.MediaInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.mediacenter.Config;
import com.xiaopeng.xuiservice.mediacenter.Constants;
import com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.xapp.util.InputEventUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes5.dex */
public class MediaCenterHelper {
    private static final String TAG = "MediaCenterHelper";
    private static MediaCenterHelper mInstance;
    private String mLastStoreMediaPackageName;
    private int mThirdAppLastIgOffStatus;
    private boolean needCheckMediaProcess;
    private final Map<Integer, Integer> mCmdKeyMap = new HashMap<Integer, Integer>() { // from class: com.xiaopeng.xuiservice.mediacenter.helper.MediaCenterHelper.1
        {
            put(0, 126);
            put(1, 86);
            put(2, 85);
            put(3, 85);
            put(5, 90);
            put(13, 89);
            put(6, 87);
            put(7, 88);
        }
    };
    private PackageManager mPackageManager = ActivityThread.currentActivityThread().getApplication().getPackageManager();
    private ActivityManager mActivityManager = (ActivityManager) ActivityThread.currentActivityThread().getApplication().getSystemService("activity");

    private MediaCenterHelper() {
    }

    public static MediaCenterHelper getInstance() {
        if (mInstance == null) {
            synchronized (MediaCenterHelper.class) {
                if (mInstance == null) {
                    mInstance = new MediaCenterHelper();
                }
            }
        }
        return mInstance;
    }

    public void controlSessionByXUIBroadcast(int cmd) {
        LogUtil.d(TAG, "controlSessionByXUIBroadcast cmd:" + cmd);
        Intent intent = new Intent(Constants.MediaLaunchPolicy.ACTION_LAUNCH_MUSIC);
        intent.putExtra("command", cmd);
        BroadcastManager.getInstance().sendBroadcast(intent);
    }

    public void notifyIgStatus(boolean igOff) {
        int i = Config.MediaLaunchPolicy;
        if (i == 1) {
            if (isCurrentThirdAppPlay() && igOff) {
                setCheckMediaProcess(true);
                this.mThirdAppLastIgOffStatus = MediaCenterHalService.getInstance().getCurrentPlayStatus(0);
            }
        } else if (i == 2) {
            if (igOff) {
                setCheckMediaProcess(true);
            }
        } else if (i == 3 && isCurrentThirdAppPlay() && igOff) {
            this.mThirdAppLastIgOffStatus = MediaCenterHalService.getInstance().getCurrentPlayStatus(0);
            LogUtil.i(TAG, "resume launch policy current status:" + this.mThirdAppLastIgOffStatus);
            if (this.mThirdAppLastIgOffStatus == 0) {
                MediaCenterHalService.getInstance().playbackControl(0, 18, 0);
            }
        }
    }

    public void notifySoundComplete() {
        LogUtil.i(TAG, "notifySoundComplete");
        int i = Config.MediaLaunchPolicy;
        if (i == 1) {
            if (isCurrentThirdAppPlay() && this.mThirdAppLastIgOffStatus == 0) {
                controlSessionByXUIBroadcast(0);
            }
        } else if (i == 3 && isCurrentThirdAppPlay() && this.mThirdAppLastIgOffStatus == 0) {
            MediaCenterHalService.getInstance().playbackControl(0, 3, 0);
        }
    }

    public boolean isCurrentThirdAppPlay() {
        MediaInfo mediaInfo = MediaCenterHalService.getInstance().getCurrentMediaInfo(0);
        return (mediaInfo == null || mediaInfo.isXpMusic()) ? false : true;
    }

    public void preLaunchThirdAppService(String pkgName) {
        PackageManager packageManager;
        if (!TextUtils.isEmpty(pkgName) && (packageManager = this.mPackageManager) != null) {
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(pkgName, 4);
                ServiceInfo[] serviceInfo = packageInfo.services;
                for (ServiceInfo service : serviceInfo) {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(service.packageName, service.name));
                    LogUtil.i(TAG, "preLaunchThirdAppService pkgName:" + service.packageName + " &serviceName:" + service.name);
                    ActivityThread.currentActivityThread().getApplication().startForegroundService(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void setCheckMediaProcess(boolean needCheck) {
        LogUtil.d(TAG, "setCheckMediaProcess:" + needCheck);
        this.needCheckMediaProcess = needCheck;
    }

    public synchronized boolean isNeedCheckMediaProcess() {
        return this.needCheckMediaProcess;
    }

    public boolean isProcessExist(String pkgName) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList = this.mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : runningAppProcessInfoList) {
            if (info.processName.contains(pkgName)) {
                return true;
            }
        }
        return false;
    }

    public void saveMediaPackageName(String packageName) {
        if (!TextUtils.equals(this.mLastStoreMediaPackageName, packageName)) {
            this.mLastStoreMediaPackageName = packageName;
            LogUtil.i(TAG, "saveMediaPackageName:" + packageName);
            SystemProperties.set(Constants.KEY_PROP_MEDIA_PACKAGE, packageName);
        }
    }

    public void dispatchMediaButton(int cmd) {
        if (this.mCmdKeyMap.containsKey(Integer.valueOf(cmd))) {
            InputEventUtil.dispatchKey(this.mCmdKeyMap.get(Integer.valueOf(cmd)).intValue());
        }
    }
}
