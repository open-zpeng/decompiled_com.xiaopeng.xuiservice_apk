package com.xiaopeng.xuiservice.xapp.media.handler;

import android.app.ActivityThread;
import android.content.Intent;
import android.content.pm.PackageManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.IXUIServiceImpl;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.mediacenter.Constants;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.xapp.XAppService;
/* loaded from: classes5.dex */
public class XpMediaHandler implements IMediaHandler {
    private static final String ACTION_MEDIA_PLAY_LIST = "com.xiaopeng.music_MEDIA_PLAY_LIST";
    private static final String EXTRA_COMMAND = "command";
    private static final String EXTRA_DISPLAY_ID = "displayId";
    private static final String EXTRA_TYPE = "type";
    private static final String TAG = "XpMediaHandler";
    private PackageManager mPackageManager = ActivityThread.currentActivityThread().getApplication().getPackageManager();

    @Override // com.xiaopeng.xuiservice.xapp.media.handler.IMediaHandler
    public void mediaPlay(int displayId, String commandType, String command) {
        LogUtil.d(TAG, "mediaPlay displayId:" + displayId + " &commandType:" + commandType + " &command:" + command);
        Intent intent = new Intent(ACTION_MEDIA_PLAY_LIST);
        intent.putExtra(EXTRA_DISPLAY_ID, displayId);
        intent.putExtra("type", commandType);
        intent.putExtra("command", command);
        BroadcastManager.getInstance().sendBroadcast(intent);
    }

    @Override // com.xiaopeng.xuiservice.xapp.media.handler.IMediaHandler
    public void enterApp(int displayId) {
        if (XUIConfig.isNapaUIType()) {
            startNapaXpMusic(displayId);
        } else if (SharedDisplayManager.hasSharedDisplayFeature()) {
            startPackageByDisplay(displayId);
        }
    }

    @Override // com.xiaopeng.xuiservice.xapp.media.handler.IMediaHandler
    public String getPkgName() {
        return Constants.PACKAGE_XP_MUSIC;
    }

    private void startNapaXpMusic(int displayId) {
        LogUtil.i(TAG, "startNapaXpMusic displayId:" + displayId);
        XAppService mXAppService = (XAppService) IXUIServiceImpl.getInstance().getService(XUIConfig.PROPERTY_XAPP);
        mXAppService.startXpApp(Constants.NAPA_MUSIC_START_PARAMS, displayId);
    }

    private void startPackageByDisplay(int displayId) {
        Intent intent;
        LogUtil.d(TAG, "startPackageByDisplay displayId:" + displayId);
        PackageManager packageManager = this.mPackageManager;
        if (packageManager != null && (intent = packageManager.getLaunchIntentForPackage(getPkgName())) != null) {
            intent.setScreenId(displayId);
            try {
                ActivityThread.currentActivityThread().getApplication().startActivity(intent);
            } catch (Exception e) {
                LogUtil.w(TAG, "startPackageByDisplay failed:" + e.getMessage());
            }
        }
    }
}
