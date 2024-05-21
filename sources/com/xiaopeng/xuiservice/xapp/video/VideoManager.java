package com.xiaopeng.xuiservice.xapp.video;

import android.text.TextUtils;
import com.xiaopeng.app.xpPackageInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.xapp.Constants;
import com.xiaopeng.xuiservice.xapp.XAppManagerService;
import com.xiaopeng.xuiservice.xapp.video.handler.IVideoHandler;
import com.xiaopeng.xuiservice.xapp.video.handler.QIYIVideoHandler;
import com.xiaopeng.xuiservice.xapp.video.handler.VODVideoHandler;
import java.util.HashMap;
import java.util.LinkedHashMap;
/* loaded from: classes5.dex */
public class VideoManager {
    public static final int CODE_FAILED = 1;
    public static final int CODE_NO_MATCH = 3;
    public static final int CODE_NO_X_MEMBER = 4;
    public static final int CODE_PLAYER_ERROR = 2;
    public static final int CODE_SUCCESS = 0;
    public static final int CODE_UN_ANTICIPATE = 5;
    public static final int CODE_VERSION_ERROR = 6;
    private static final String TAG = "VideoManager";
    private static VideoManager mInstance;
    private HashMap<String, IVideoHandler> mHandlerHashMap;
    private String mLatestTargetApp = "";
    private XAppManagerService.ActivityChangedListener mActivityChangedListener = new XAppManagerService.ActivityChangedListener() { // from class: com.xiaopeng.xuiservice.xapp.video.VideoManager.1
        @Override // com.xiaopeng.xuiservice.xapp.XAppManagerService.ActivityChangedListener
        public void onTopActivityChanged(int displayId, xpPackageInfo info) {
            if (info != null && info.appType == 2 && VideoManager.this.mHandlerHashMap.containsKey(info.packageName)) {
                VideoManager.this.mLatestTargetApp = info.packageName;
            }
        }
    };

    private VideoManager() {
        initHandlers();
        registerActivityChangedListener();
    }

    private void initHandlers() {
        this.mHandlerHashMap = new LinkedHashMap();
        this.mHandlerHashMap.put(Constants.PACKAGE_QIYI_IV, new QIYIVideoHandler());
        this.mHandlerHashMap.put(Constants.PACKAGE_YOUKU_IOT, new VODVideoHandler("IdYouKu", Constants.PACKAGE_YOUKU_IOT));
    }

    private void registerActivityChangedListener() {
        XAppManagerService.getInstance().registerActivityChangedListener(this.mActivityChangedListener);
    }

    public static VideoManager getInstance() {
        if (mInstance == null) {
            synchronized (VideoManager.class) {
                if (mInstance == null) {
                    mInstance = new VideoManager();
                }
            }
        }
        return mInstance;
    }

    public boolean isSupportVideoControl(String pkgName) {
        IVideoHandler handler = this.mHandlerHashMap.get(pkgName);
        return handler != null;
    }

    public void play(int displayId, String pkgName, String command, Callback callback) {
        IVideoHandler handler = this.mHandlerHashMap.get(pkgName);
        if (handler != null) {
            if (SharedDisplayManager.hasSharedDisplayFeature()) {
                SharedDisplayManager.getInstance().setSharedId(pkgName, displayId);
            }
            handler.play(displayId, command, callback);
        }
    }

    public void play(int displayId, String pkgName, String programName, String artistName, Callback callback) {
        IVideoHandler handler = this.mHandlerHashMap.get(pkgName);
        if (handler != null) {
            handler.play(displayId, programName, artistName, callback);
        }
    }

    public int play(String pkgName, String artistName, String programName) {
        return 0;
    }

    public void prev(String packageName, Callback callback) {
        IVideoHandler handler = this.mHandlerHashMap.get(packageName);
        if (handler != null) {
            handler.prev(callback);
        }
    }

    public void next(String pkgName, Callback callback) {
        IVideoHandler handler = this.mHandlerHashMap.get(pkgName);
        if (handler != null) {
            handler.next(callback);
        }
    }

    public void set(String pkgName, int value, Callback callback) {
        IVideoHandler handler = this.mHandlerHashMap.get(pkgName);
        if (handler != null) {
            handler.set(value, callback);
        }
    }

    public void forward(String pkgName, Callback callback) {
        IVideoHandler handler = this.mHandlerHashMap.get(pkgName);
        if (handler != null) {
            handler.forward(callback);
        }
    }

    public void backward(String pkgName, Callback callback) {
        IVideoHandler handler = this.mHandlerHashMap.get(pkgName);
        if (handler != null) {
            handler.backward(callback);
        }
    }

    public void setTime(String pkgName, double time, Callback callback) {
        IVideoHandler handler = this.mHandlerHashMap.get(pkgName);
        if (handler != null) {
            handler.setTime(time, callback);
        }
    }

    public void pause(String pkgName, Callback callback) {
        IVideoHandler handler = this.mHandlerHashMap.get(pkgName);
        if (handler != null) {
            handler.pause(callback);
        }
    }

    public void resume(String pkgName, Callback callback) {
        IVideoHandler handler = this.mHandlerHashMap.get(pkgName);
        if (handler != null) {
            handler.resume(callback);
        }
    }

    public void speedUp(String pkgName, Callback callback) {
        IVideoHandler handler = this.mHandlerHashMap.get(pkgName);
        if (handler != null) {
            handler.speedUp(callback);
        }
    }

    public void speedDown(String pkgName, Callback callback) {
        IVideoHandler handler = this.mHandlerHashMap.get(pkgName);
        if (handler != null) {
            handler.speedDown(callback);
        }
    }

    public void speedSet(String pkgName, double speed, Callback callback) {
        IVideoHandler handler = this.mHandlerHashMap.get(pkgName);
        if (handler != null) {
            handler.speedSet(speed, callback);
        }
    }

    public void end(String pkgName, Callback callback) {
        IVideoHandler handler = this.mHandlerHashMap.get(pkgName);
        if (handler != null) {
            handler.end(callback);
        }
    }

    public void clarityUP(String pkgName, Callback callback) {
        IVideoHandler handler = this.mHandlerHashMap.get(pkgName);
        if (handler != null) {
            handler.clarityUP(callback);
        }
    }

    public void clarityDown(String pkgName, Callback callback) {
        IVideoHandler handler = this.mHandlerHashMap.get(pkgName);
        if (handler != null) {
            handler.clarityDown(callback);
        }
    }

    public void claritySet(String pkgName, String value, Callback callback) {
        IVideoHandler handler = this.mHandlerHashMap.get(pkgName);
        if (handler != null) {
            handler.claritySet(value, callback);
        }
    }

    public void setVideoPlayMode(String pkgName, Callback callback) {
        IVideoHandler handler = this.mHandlerHashMap.get(pkgName);
        if (handler != null) {
            handler.setVideoPlayMode(callback);
        }
    }

    public void setMediaPlayMode(String pkgName, Callback callback) {
        IVideoHandler handler = this.mHandlerHashMap.get(pkgName);
        if (handler != null) {
            handler.setMediaPlayMode(callback);
        }
    }

    public void collect(String pkgName, Callback callback) {
        IVideoHandler handler = this.mHandlerHashMap.get(pkgName);
        if (handler != null) {
            handler.collect(callback);
        }
    }

    public void collectCancel(String pkgName, Callback callback) {
        IVideoHandler handler = this.mHandlerHashMap.get(pkgName);
        if (handler != null) {
            handler.collectCancel(callback);
        }
    }

    public void skipBegin(String pkgName, Callback callback) {
        IVideoHandler handler = this.mHandlerHashMap.get(pkgName);
        if (handler != null) {
            handler.skipBegin(callback);
        }
    }

    public void skipEnd(String pkgName, Callback callback) {
        IVideoHandler handler = this.mHandlerHashMap.get(pkgName);
        if (handler != null) {
            handler.skipEnd(callback);
        }
    }

    public String getTargetApp(int displayId) {
        xpPackageInfo curVideoApp = XAppManagerService.getInstance().getXpPackageInfo(displayId);
        if (curVideoApp != null && curVideoApp.appType == 2) {
            String curVideoAppName = curVideoApp.packageName;
            if (this.mHandlerHashMap.containsKey(curVideoAppName)) {
                LogUtil.d(TAG, "getTargetApp displayId:" + displayId + " &curVideoAppName:" + curVideoAppName);
                return curVideoAppName;
            }
        }
        if (!TextUtils.isEmpty(this.mLatestTargetApp) && XAppManagerService.getInstance().checkAppInstalled(this.mLatestTargetApp)) {
            LogUtil.d(TAG, "getTargetApp displayId:" + displayId + " &mLatestTargetApp:" + this.mLatestTargetApp);
            return this.mLatestTargetApp;
        }
        for (String pkgName : this.mHandlerHashMap.keySet()) {
            if (XAppManagerService.getInstance().checkAppInstalled(pkgName)) {
                LogUtil.d(TAG, "getTargetApp displayId:" + displayId + " &pkgName:" + pkgName);
                return pkgName;
            }
        }
        return "";
    }
}
