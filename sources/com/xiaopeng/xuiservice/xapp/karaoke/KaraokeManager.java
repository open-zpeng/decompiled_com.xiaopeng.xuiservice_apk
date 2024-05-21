package com.xiaopeng.xuiservice.xapp.karaoke;

import android.text.TextUtils;
import com.xiaopeng.app.xpPackageInfo;
import com.xiaopeng.xuimanager.mediacenter.MediaInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService;
import com.xiaopeng.xuiservice.xapp.Constants;
import com.xiaopeng.xuiservice.xapp.XAppManagerService;
import com.xiaopeng.xuiservice.xapp.karaoke.handler.CBKaraokeHandler;
import com.xiaopeng.xuiservice.xapp.karaoke.handler.IKaraokeHandler;
import com.xiaopeng.xuiservice.xapp.karaoke.handler.QMKaraokeHandler;
import com.xiaopeng.xuiservice.xapp.karaoke.handler.ThunderKaraokeHandler;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
/* loaded from: classes5.dex */
public class KaraokeManager {
    private static final String TAG = "KaraokeController";
    private static KaraokeManager mInstance;
    private HashMap<String, IKaraokeHandler> mHandlerHashMap;
    private String mLastKaraokePackage = "";
    private XAppManagerService.ActivityChangedListener mActivityChangedListener = new XAppManagerService.ActivityChangedListener() { // from class: com.xiaopeng.xuiservice.xapp.karaoke.KaraokeManager.1
        @Override // com.xiaopeng.xuiservice.xapp.XAppManagerService.ActivityChangedListener
        public void onTopActivityChanged(int displayId, xpPackageInfo info) {
            if (info != null && info.appType == 7 && KaraokeManager.this.mHandlerHashMap.containsKey(info.packageName)) {
                KaraokeManager.this.mLastKaraokePackage = info.packageName;
            }
        }
    };

    private KaraokeManager() {
        initHandlerMap();
        registerActivityChangedListener();
    }

    public static KaraokeManager getInstance() {
        if (mInstance == null) {
            synchronized (KaraokeManager.class) {
                if (mInstance == null) {
                    mInstance = new KaraokeManager();
                }
            }
        }
        return mInstance;
    }

    private void initHandlerMap() {
        this.mHandlerHashMap = new LinkedHashMap();
        this.mHandlerHashMap.put("com.tencent.wecar.karaoke", new QMKaraokeHandler());
        this.mHandlerHashMap.put(Constants.PACKAGE_CHANGBA, new CBKaraokeHandler());
        this.mHandlerHashMap.put(Constants.PACKAGE_THUNDER, new ThunderKaraokeHandler());
    }

    private void registerActivityChangedListener() {
        XAppManagerService.getInstance().registerActivityChangedListener(this.mActivityChangedListener);
    }

    public int startKaraokeCurrentMedia(int displayId) {
        String targetPackage = findTargetPackage(displayId);
        if (TextUtils.isEmpty(targetPackage)) {
            return 4;
        }
        searchCurrentSong(displayId, targetPackage);
        return 1;
    }

    private void searchCurrentSong(int displayId, String pkgName) {
        MediaInfo info = MediaCenterHalService.getInstance().getCurrentMediaInfo(displayId);
        if (info != null) {
            searchSong(displayId, pkgName, info.getArtist(), info.getTitle());
        }
    }

    public String findTargetPackage(int displayId) {
        List<String> installPackages = XAppManagerService.getInstance().getInstalledAppList(7);
        if (!TextUtils.isEmpty(this.mLastKaraokePackage) && installPackages.contains(this.mLastKaraokePackage)) {
            return this.mLastKaraokePackage;
        }
        for (String pkgName : this.mHandlerHashMap.keySet()) {
            if (installPackages.contains(pkgName)) {
                return pkgName;
            }
        }
        if (installPackages != null && !installPackages.isEmpty()) {
            return installPackages.get(0);
        }
        return "";
    }

    public boolean isSupportSpeech(String pkgName) {
        return this.mHandlerHashMap.containsKey(pkgName);
    }

    public boolean isKaraokeApp(String pkgName) {
        List<String> installPackages = XAppManagerService.getInstance().getInstalledAppList(7);
        return installPackages.contains(pkgName);
    }

    public void searchSong(int displayId, String pkgName, String artist, String song) {
        LogUtil.d(TAG, "searchSong pkgName:" + pkgName + " &artist:" + artist + " &song:" + song);
        IKaraokeHandler handler = this.mHandlerHashMap.get(pkgName);
        if (handler != null) {
            handler.searchSong(displayId, artist, song);
        }
    }
}
