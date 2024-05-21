package com.xiaopeng.xuiservice.mediacenter.controller;

import android.app.ActivityManager;
import android.app.ActivityThread;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.session.MediaController;
import android.text.TextUtils;
import com.xiaopeng.xuimanager.mediacenter.MediaInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.IXUIServiceImpl;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.mediacenter.Constants;
import com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.PassengerBluetoothManager;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.xapp.XAppService;
import java.util.List;
/* loaded from: classes5.dex */
public abstract class BaseMediaController implements IMediaController {
    private static final String TAG = "BaseMediaController";
    protected MediaController mMediaController;
    private XAppService mXAppService;
    private ActivityManager mActivityManager = (ActivityManager) ActivityThread.currentActivityThread().getApplication().getSystemService("activity");
    private PackageManager mPackageManager = ActivityThread.currentActivityThread().getApplication().getPackageManager();

    public abstract String getTargetPackage();

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public void updateMediaController(MediaController mediaController) {
        this.mMediaController = mediaController;
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public boolean playbackControl(int displayId, int cmd, int param) {
        if (cmd == 11) {
            enterApp(displayId);
            return true;
        } else if (SharedDisplayManager.hasSharedDisplayFeature() && PassengerBluetoothManager.getInstance().isDeviceConnected() && !isTargetOnCurrentDisplay(displayId)) {
            enterApp(displayId);
            return true;
        } else {
            return false;
        }
    }

    private boolean isTargetOnCurrentDisplay(int displayId) {
        String targetPkg = getTargetPackage();
        return TextUtils.isEmpty(targetPkg) || SharedDisplayManager.getInstance().getSharedId(targetPkg) == displayId;
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public void switchSource(int source) {
    }

    @Override // com.xiaopeng.xuiservice.mediacenter.controller.IMediaController
    public void setFavorite(boolean favorite, String id) {
    }

    public void enterApp(int displayId) {
        String currentMediaPackage = getTargetPackage();
        if (XUIConfig.isNapaUIType() && !TextUtils.isEmpty(currentMediaPackage) && currentMediaPackage.equals(Constants.PACKAGE_XP_MUSIC)) {
            startNapaXpMusic(displayId);
        } else if (SharedDisplayManager.hasSharedDisplayFeature()) {
            startPackageByDisplay(currentMediaPackage, displayId);
        } else {
            ActivityManager.RunningTaskInfo currentMediaTask = queryApplicationTask(currentMediaPackage);
            if (currentMediaTask == null) {
                LogUtil.d(TAG, "app may killed, so start by package");
                if (!TextUtils.isEmpty(currentMediaPackage)) {
                    startPackage(currentMediaPackage);
                    return;
                } else {
                    startXpMusic();
                    return;
                }
            }
            moveTaskToFront(currentMediaTask);
        }
    }

    private void moveTaskToFront(ActivityManager.RunningTaskInfo taskInfo) {
        if (taskInfo.id >= 0) {
            try {
                LogUtil.i(TAG, "am moveTaskToFront package:" + taskInfo.topActivity.getPackageName());
                this.mActivityManager.moveTaskToFront(taskInfo.id, 1);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        Intent it = new Intent();
        it.setComponent(taskInfo.topActivity);
        it.addFlags(1064960);
        try {
            ActivityThread.currentActivityThread().getApplication().startActivity(it);
        } catch (ActivityNotFoundException e2) {
            LogUtil.w(TAG, "Unable to launch running task " + e2);
        }
    }

    private ActivityManager.RunningTaskInfo queryApplicationTask(String pkgName) {
        List<ActivityManager.RunningTaskInfo> runningTasks = this.mActivityManager.getRunningTasks(64);
        int numTasks = runningTasks.size();
        for (int i = 0; i < numTasks; i++) {
            ActivityManager.RunningTaskInfo info = runningTasks.get(i);
            if (info.topActivity != null && info.topActivity.getPackageName().equals(pkgName)) {
                return info;
            }
        }
        return null;
    }

    private int startPackage(String pkgName) {
        try {
            LogUtil.i(TAG, "startPackage pkgName:" + pkgName);
            if (this.mPackageManager != null) {
                Intent intent = this.mPackageManager.getLaunchIntentForPackage(pkgName);
                if (intent != null) {
                    ActivityThread.currentActivityThread().getApplication().startActivity(intent);
                } else {
                    LogUtil.i(TAG, "send start broadcast for pkgName:" + pkgName);
                    sendStartPackageBroadcast(pkgName, 0);
                }
            }
        } catch (ActivityNotFoundException e) {
            LogUtil.w(TAG, "startPackage not found activity for pckage:" + pkgName);
        }
        return 0;
    }

    private void startPackageByDisplay(String pkgName, int displayId) {
        PackageManager packageManager;
        LogUtil.d(TAG, "startPackageByDisplay pkgName:" + pkgName + " &displayId:" + displayId);
        if (!TextUtils.isEmpty(pkgName) && (packageManager = this.mPackageManager) != null) {
            Intent intent = packageManager.getLaunchIntentForPackage(pkgName);
            if (intent != null) {
                intent.setSharedId(displayId);
                try {
                    ActivityThread.currentActivityThread().getApplication().startActivity(intent);
                    return;
                } catch (Exception e) {
                    LogUtil.w(TAG, "startPackageByDisplay failed:" + e.getMessage());
                    return;
                }
            }
            sendStartPackageBroadcast(pkgName, displayId);
        }
    }

    private void sendStartPackageBroadcast(String pkgName, int displayId) {
        LogUtil.d(TAG, "sendStartPackageBroadcast pkgName:" + pkgName + " on Display:" + displayId);
        Intent intent = new Intent(Constants.ACTION_START_MEDIA_PACKAGE);
        intent.putExtra("pkgName", pkgName);
        intent.putExtra("displayId", displayId);
        BroadcastManager.getInstance().sendBroadcast(intent);
    }

    private void startXpMusic() {
        Intent intent;
        if (!XUIConfig.isNapaUIType()) {
            try {
                LogUtil.i(TAG, "startXpMusic by music radio main action");
                if (this.mPackageManager != null && (intent = this.mPackageManager.getLaunchIntentForPackage(Constants.PACKAGE_XP_MUSIC)) != null) {
                    ActivityThread.currentActivityThread().getApplication().startActivity(intent);
                    return;
                }
                return;
            } catch (ActivityNotFoundException e) {
                LogUtil.w(TAG, "startXpMusic not found activity");
                return;
            }
        }
        startNapaXpMusic(0);
    }

    private void startNapaXpMusic(int displayId) {
        LogUtil.i(TAG, "startNapaXpMusic displayId:" + displayId);
        if (this.mXAppService == null) {
            this.mXAppService = (XAppService) IXUIServiceImpl.getInstance().getService(XUIConfig.PROPERTY_XAPP);
        }
        MediaInfo currentMediaInfo = MediaCenterHalService.getInstance().getCurrentMediaInfo(displayId);
        if (currentMediaInfo != null && currentMediaInfo.getSource() == 6) {
            this.mXAppService.startXpApp(Constants.NAPA_DOLBY_MUSIC_START_PARAMS, displayId);
        } else {
            this.mXAppService.startXpApp(Constants.NAPA_MUSIC_START_PARAMS, displayId);
        }
    }

    public void seekTo(long position) {
        MediaController.TransportControls transportControls;
        MediaController mediaController = this.mMediaController;
        if (mediaController != null && (transportControls = mediaController.getTransportControls()) != null) {
            transportControls.seekTo(position);
        }
    }
}
