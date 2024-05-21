package com.xiaopeng.xuiservice.xapp.media;

import com.xiaopeng.xuimanager.mediacenter.MediaInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.mediacenter.Config;
import com.xiaopeng.xuiservice.mediacenter.Constants;
import com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService;
import com.xiaopeng.xuiservice.mediacenter.controller.info.InfoUtil;
import com.xiaopeng.xuiservice.utils.PassengerBluetoothManager;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.utils.SharedPreferencesUtil;
import com.xiaopeng.xuiservice.xapp.media.handler.IMediaHandler;
import com.xiaopeng.xuiservice.xapp.media.handler.XpMediaHandler;
import java.util.HashMap;
/* loaded from: classes5.dex */
public class MediaManager {
    private static final String TAG = "MediaManager";
    private static MediaManager mInstance;
    private HashMap<String, IMediaHandler> mHandlerHashMap = new HashMap<>();

    private MediaManager() {
        initHandlers();
    }

    private void initHandlers() {
        this.mHandlerHashMap.put(Constants.PACKAGE_XP_MUSIC, new XpMediaHandler());
    }

    public static MediaManager getInstance() {
        if (mInstance == null) {
            synchronized (MediaManager.class) {
                if (mInstance == null) {
                    mInstance = new MediaManager();
                }
            }
        }
        return mInstance;
    }

    public boolean isShowSeizeDialog(int displayId) {
        boolean noShow = SharedPreferencesUtil.getInstance().getBoolean(SharedPreferencesUtil.KeySet.KEY_MEDIA_NO_SEIZE_DIALOG, false);
        if (SharedDisplayManager.hasSharedDisplayFeature() && Config.Show_Seize_Dialog && !noShow) {
            MediaInfo current = MediaCenterHalService.getInstance().getCurrentMediaInfo(displayId);
            MediaInfo other = MediaCenterHalService.getInstance().getCurrentMediaInfo(1 - displayId);
            return current != null && current.isXpMusic() && InfoUtil.isDefaultMedia(current) && other != null && other.isXpMusic() && !InfoUtil.isDefaultMedia(other);
        }
        return false;
    }

    public boolean isShowSeizeDialog(int displayId, String pkgName) {
        MediaInfo other;
        boolean noShow = SharedPreferencesUtil.getInstance().getBoolean(SharedPreferencesUtil.KeySet.KEY_MEDIA_NO_SEIZE_DIALOG, false);
        LogUtil.d(TAG, "isShowSeizeDialog noShow:" + noShow);
        if (!SharedDisplayManager.hasSharedDisplayFeature() || !Config.Show_Seize_Dialog || noShow || !PassengerBluetoothManager.getInstance().isDeviceConnected() || (other = MediaCenterHalService.getInstance().getCurrentMediaInfo(1 - displayId)) == null || InfoUtil.isDefaultMedia(other)) {
            return false;
        }
        String otherPkgName = other.isXpMusic() ? Constants.PACKAGE_XP_MUSIC : other.getPackageName();
        LogUtil.d(TAG, "currentPkgName:" + pkgName + " &otherPkgName:" + otherPkgName);
        return otherPkgName.equals(pkgName);
    }

    public void enterCurrent(int displayId) {
        MediaCenterHalService.getInstance().playbackControlInner(displayId, 11, 0);
    }

    public void enterTargetPackage(int displayId, String pkgName) {
        IMediaHandler handler = this.mHandlerHashMap.get(pkgName);
        if (handler != null) {
            handler.enterApp(displayId);
        }
    }

    public void mediaPlay(int displayId, String pkgName, String type, String command) {
        IMediaHandler handler = this.mHandlerHashMap.get(pkgName);
        if (handler != null) {
            if (SharedDisplayManager.hasSharedDisplayFeature()) {
                SharedDisplayManager.getInstance().setSharedId(handler.getPkgName(), displayId);
            }
            handler.mediaPlay(displayId, type, command);
        }
    }
}
