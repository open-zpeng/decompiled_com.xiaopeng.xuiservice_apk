package com.xiaopeng.xuiservice.xapp.speech;

import android.app.ActivityThread;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.alipay.mobile.aromeservice.RequestParams;
import com.xiaopeng.util.FeatureOption;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.PassengerBluetoothManager;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import com.xiaopeng.xuiservice.xapp.Config;
import com.xiaopeng.xuiservice.xapp.Constants;
import com.xiaopeng.xuiservice.xapp.XAppManagerService;
import com.xiaopeng.xuiservice.xapp.XAppStartManager;
import com.xiaopeng.xuiservice.xapp.entities.AppInfo;
import com.xiaopeng.xuiservice.xapp.karaoke.KaraokeManager;
import com.xiaopeng.xuiservice.xapp.sound.SoundManager;
import com.xiaopeng.xuiservice.xapp.speech.miniprog.LandScreenMiniprogHandler;
import com.xiaopeng.xuiservice.xapp.speech.miniprog.MiniprogHandler;
import com.xiaopeng.xuiservice.xapp.speech.miniprog.PortScreenMiniprogHandler;
import com.xiaopeng.xuiservice.xapp.speech.window.TopWindowHandler;
import com.xiaopeng.xuiservice.xapp.speech.window.WithNapaWindowHandler;
import com.xiaopeng.xuiservice.xapp.store.AppStoreStatusProvider;
import com.xiaopeng.xuiservice.xapp.util.InputEventUtil;
import com.xiaopeng.xuiservice.xapp.util.XAppBiLogUtil;
import com.xiaopeng.xuiservice.xapp.video.VideoManager;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.videoio.Videoio;
/* loaded from: classes5.dex */
public class SpeechController {
    private static final String TAG = "SpeechController";
    private MiniprogHandler mMiniprogHandler;
    private TopWindowHandler mTopWindowHandler;
    private XAppManagerService mXAppManagerService = XAppManagerService.getInstance();
    private XAppStartManager mXAppStartManager = XAppStartManager.getInstance();
    private Context mContext = ActivityThread.currentActivityThread().getApplication();
    private KaraokeManager mKaraokeManager = KaraokeManager.getInstance();

    public SpeechController() {
        initMiniprogHandler();
        initTopWindowHandler();
    }

    public int startXpAppStore(String data, String action) {
        int displayId = getTargetDisplayId(data);
        if (Config.CloseDialogBeforeStartApplication) {
            boolean notCancelableDialog = this.mTopWindowHandler.isHaveNotCancelableDialog(displayId);
            if (notCancelableDialog) {
                return 9;
            }
            this.mTopWindowHandler.closeDialog(0);
        }
        startXpAppByBroadcast(displayId, action);
        return 1;
    }

    private void initMiniprogHandler() {
        String miniProgramPkg = FeatureOption.FO_MINI_PROGRAM_PACKAGES;
        if (TextUtils.isEmpty(miniProgramPkg)) {
            this.mMiniprogHandler = new PortScreenMiniprogHandler(this.mXAppManagerService);
        } else {
            this.mMiniprogHandler = new LandScreenMiniprogHandler(this.mXAppManagerService);
        }
    }

    private void initTopWindowHandler() {
        if (XUIConfig.isNapaUIType()) {
            this.mTopWindowHandler = new WithNapaWindowHandler();
        } else {
            this.mTopWindowHandler = new TopWindowHandler();
        }
    }

    private void startXpAppByBroadcast(int displayId, String action) {
        LogUtil.i(TAG, "startXpAppByBroadcast displayId:" + displayId + " &action:" + action);
        Intent intent = new Intent(action);
        intent.setPackage(Constants.PACKAGE_APP_STORE);
        intent.putExtra(Constants.EXTRA_APP_STORE_SCREEN, displayId);
        BroadcastManager.getInstance().sendBroadcast(intent);
    }

    public int closeXpAppStore() {
        if (!this.mXAppManagerService.isOpened(Constants.PACKAGE_APP_STORE)) {
            return 2;
        }
        closeApp();
        return 1;
    }

    public int closeCurrentApp(String data) {
        int targetDisplayId = getTargetDisplayId(data);
        return closeTopView(targetDisplayId);
    }

    public int startApplication(String appData) {
        LogUtil.w(TAG, "startApplication appData:" + appData);
        try {
            JSONObject jsonObject = new JSONObject(appData);
            String packageNameInfo = jsonObject.optString(SpeechConstants.KEY_PACKAGE_NAME);
            String packageName = geTargetApplication(packageNameInfo);
            String pageUri = jsonObject.optString(SpeechConstants.KEY_PAGE_URL);
            int displayId = getTargetDisplayId(jsonObject);
            return startApplicationForResult(packageName, pageUri, displayId);
        } catch (JSONException e) {
            e.printStackTrace();
            return 3;
        }
    }

    private String geTargetApplication(String packageNameInfo) {
        if (!TextUtils.isEmpty(packageNameInfo) && packageNameInfo.contains("|")) {
            String[] pkgNameArray = packageNameInfo.split("\\|");
            for (String pkgName : pkgNameArray) {
                if (XAppManagerService.getInstance().checkAppInstalled(pkgName)) {
                    return pkgName;
                }
            }
            return pkgNameArray[0];
        }
        return packageNameInfo;
    }

    public int stopApplication(String appData) {
        try {
            JSONObject jsonObject = new JSONObject(appData);
            String appName = jsonObject.optString(SpeechConstants.KEY_PACKAGE_NAME);
            if (appName.equals(Constants.PACKAGE_USERMANUAL)) {
                closeUserManual();
                return 1;
            } else if (appName.contains("/")) {
                String[] infos = appName.split("/");
                return stopApplicationForResult(infos[0], infos[1]);
            } else {
                return stopApplicationForResult(appName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public int startApplet(String appletData) {
        try {
            JSONObject jsonObject = new JSONObject(appletData);
            String aliId = jsonObject.optString(SpeechConstants.KEY_ALI_ID);
            String page_url = jsonObject.optString(SpeechConstants.KEY_PAGE_URL);
            Map parmas = new HashMap();
            if (!TextUtils.isEmpty(page_url)) {
                parmas.put("page", page_url);
            }
            return startAppletForResult(aliId, "", parmas);
        } catch (JSONException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public int stopApplet(String appletData) {
        try {
            JSONObject jsonObject = new JSONObject(appletData);
            String aliId = jsonObject.optString(SpeechConstants.KEY_ALI_ID);
            return stopAppletForResult(aliId);
        } catch (JSONException e) {
            e.printStackTrace();
            return 1;
        }
    }

    private int startApplicationForResult(String packageName, String pageUri, int displayId) {
        int result;
        if (this.mXAppManagerService.checkAppInstalled(packageName)) {
            AppInfo appInfo = this.mXAppManagerService.getAppInfo(packageName);
            if (!TextUtils.isEmpty(pageUri)) {
                result = launchApp(packageName, appInfo, pageUri, displayId);
            } else if (this.mXAppManagerService.isOpened(packageName, displayId)) {
                result = 1;
            } else {
                result = launchApp(packageName, appInfo, pageUri, displayId);
            }
        } else {
            int status = AppStoreStatusProvider.getInstance().queryAppOnlineStatus(this.mContext, packageName);
            if (status == 1081) {
                result = 16;
            } else if (status == 1001) {
                result = 12;
            } else {
                showAppDetailPage(packageName, false, displayId);
                result = 4;
            }
        }
        LogUtil.i(TAG, "startApplicationForResult packageName:" + packageName + " &pageUri:" + pageUri + " &result:" + result);
        return result;
    }

    private int launchApp(String packageName, AppInfo appInfo, String pageUri, int displayId) {
        int appStartCode = this.mXAppStartManager.checkAppStartStatus(packageName, true, false, displayId);
        if (appStartCode != 0) {
            XAppBiLogUtil.sendAppUseDeniedDataLog(1, appStartCode, packageName);
        }
        int appOpenSpeechCode = convertStartCode(appStartCode, packageName, displayId);
        if (appOpenSpeechCode != 1) {
            return appOpenSpeechCode;
        }
        if (SharedDisplayManager.hasSharedDisplayFeature()) {
            SharedDisplayManager.getInstance().setSharedId(packageName, displayId);
        }
        if (!TextUtils.isEmpty(pageUri)) {
            startThirdAppPage(pageUri, displayId);
        } else if (Constants.PACKAGE_USERMANUAL.equals(packageName)) {
            startUserManual(displayId);
        } else if (Constants.PACKAGE_MONTECARLO.equals(packageName)) {
            startHome();
        } else if (appInfo != null) {
            if (Constants.PACKAGE_CAR_ACCOUNT.equals(packageName)) {
                appInfo.intent.putExtra(RequestParams.REQUEST_KEY_PACKAGE_NAME, this.mContext.getPackageName());
            }
            if (SharedDisplayManager.hasSharedDisplayFeature()) {
                appInfo.intent.setScreenId(displayId);
            }
            this.mContext.startActivity(appInfo.intent);
        }
        return 1;
    }

    private void startPackage(String pkgName, int displayId) {
        AppInfo appInfo = this.mXAppManagerService.getAppInfo(pkgName);
        if (appInfo != null) {
            if (SharedDisplayManager.hasSharedDisplayFeature()) {
                appInfo.intent.setScreenId(displayId);
            }
            this.mContext.startActivity(appInfo.intent);
        }
    }

    private int startAppletForResult(String aliId, String name, Map parmas) {
        return this.mMiniprogHandler.startMiniProgram(aliId, name, parmas);
    }

    private int stopAppletForResult(String aliId) {
        return this.mMiniprogHandler.exitApplet(aliId);
    }

    private int stopApplicationForResult(String packageName, String activityName) {
        LogUtil.i(TAG, "stopApplicationForResult packageName:" + packageName + " &activityName:" + activityName);
        if (this.mXAppManagerService.checkAppInstalled(packageName)) {
            XAppManagerService.ApplicationRecord curApp = this.mXAppManagerService.getCurApp();
            if (curApp.getName().getPackageName().equals(packageName) && curApp.getName().getClassName().equals(activityName)) {
                boolean haveCancelableDialog = isHaveCancelableDialog();
                LogUtil.i(TAG, "haveCancelableDialog:" + haveCancelableDialog);
                if (haveCancelableDialog) {
                    this.mXAppManagerService.dismissDialog();
                }
                int delayTime = haveCancelableDialog ? 1000 : 0;
                XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.speech.SpeechController.1
                    @Override // java.lang.Runnable
                    public void run() {
                        SpeechController.this.sendBackKeyEvent();
                    }
                }, delayTime);
                return 1;
            }
            return 2;
        }
        return 4;
    }

    private int stopApplicationForResult(String packageName) {
        if (this.mXAppManagerService.checkAppInstalled(packageName)) {
            if (checkAppOpenToClose(packageName)) {
                return 1;
            }
            return 2;
        }
        return 4;
    }

    private boolean checkAppOpenToClose(String pkgName) {
        int screenId = this.mXAppManagerService.checkTopApp(pkgName);
        if (screenId == -1) {
            return false;
        }
        SharedDisplayManager.getInstance().goSharedDesktop(screenId);
        return true;
    }

    private void closeApp() {
        startHome();
    }

    private void startHome() {
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            intent.addFlags(270532608);
            this.mContext.startActivity(intent);
        } catch (Exception e) {
            LogUtil.w(TAG, "startHomePackage e= " + e);
        }
    }

    private void startThirdAppPage(String pageUri, int displayId) {
        LogUtil.d(TAG, "startThirdAppPage with pageUri" + pageUri);
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            if (SharedDisplayManager.hasSharedDisplayFeature()) {
                intent.setScreenId(displayId);
            }
            intent.setData(Uri.parse(pageUri));
            intent.setFlags(Videoio.CAP_INTELPERC_IMAGE_GENERATOR);
            this.mContext.startActivity(intent);
        } catch (Exception e) {
            LogUtil.d(TAG, "startThirdAppPage failed with pageUri: " + pageUri);
        }
    }

    public void controlUserManual(boolean open) {
        LogUtil.i(TAG, "controlUserManual open: " + open);
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_LAUNCH_USER_MANUAL);
        intent.setPackage(Constants.PACKAGE_USERMANUAL);
        intent.addFlags(270532608);
        intent.putExtra("action", open ? "open_userManual" : "close_userManual");
        BroadcastManager.getInstance().sendBroadcast(intent);
    }

    private void startUserManual(int displayId) {
        LogUtil.d(TAG, "startUserManual displayId:" + displayId);
        Intent intent = new Intent(Constants.ACTION_LAUNCH_USER_MANUAL);
        intent.setPackage(Constants.PACKAGE_USERMANUAL);
        intent.putExtra("sharedId", displayId);
        BroadcastManager.getInstance().sendBroadcast(intent);
    }

    public void closeUserManual() {
        LogUtil.d(TAG, "closeUserManual");
        Intent intent = new Intent(Constants.ACTION_CLOSE_USER_MANUAL);
        intent.setPackage(Constants.PACKAGE_USERMANUAL);
        BroadcastManager.getInstance().sendBroadcast(intent);
    }

    private boolean isHaveCancelableDialog() {
        return !this.mTopWindowHandler.isHaveNotCancelableDialog(0);
    }

    private int closeTopView(int displayId) {
        return this.mTopWindowHandler.closeTopWindow(displayId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendBackKeyEvent() {
        LogUtil.i(TAG, "mock sendBackKeyEvent ");
        InputEventUtil.dispatchKey(4);
    }

    public boolean isHaveNotCancelableDialog() {
        return this.mTopWindowHandler.isHaveNotCancelableDialog(0);
    }

    public int downloadApplication(String appData) {
        try {
            JSONObject jsonObject = new JSONObject(appData);
            int displayId = getTargetDisplayId(jsonObject);
            String packageName = jsonObject.optString(SpeechConstants.KEY_PACKAGE_NAME);
            if (!TextUtils.isEmpty(packageName)) {
                boolean isRunning = AppStoreStatusProvider.getInstance().queryIsAssembling(this.mContext, packageName);
                if (isRunning) {
                    return 6;
                }
                boolean isInstalled = this.mXAppManagerService.checkAppInstalled(packageName);
                if (isInstalled) {
                    return 4;
                }
                int userStatus = AppStoreStatusProvider.getInstance().queryCanDownloadStatus(this.mContext);
                if (userStatus == 100) {
                    return 2;
                }
                int status = AppStoreStatusProvider.getInstance().queryAppOnlineStatus(this.mContext, packageName);
                if (status == 1001) {
                    return 7;
                }
                if (status == 1002) {
                    return 8;
                }
                showAppDetailPage(packageName, true, displayId);
                return 3;
            }
            return 5;
        } catch (JSONException e) {
            e.printStackTrace();
            return 5;
        }
    }

    public int currentBack(String data) {
        int targetDisplayId = getTargetDisplayId(data);
        InputEventUtil.dispatchKey(4, targetDisplayId);
        return 1;
    }

    public int getSingStatus(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String pkgName = jsonObject.optString(SpeechConstants.KEY_PACKAGE_NAME, "");
            int displayId = getTargetDisplayId(jsonObject);
            String artist = jsonObject.optString(SpeechConstants.KEY_ARTIST, "");
            String song = jsonObject.optString(SpeechConstants.KEY_SONG, "");
            String currentTopPackage = XAppManagerService.getInstance().getTopPackage(displayId);
            LogUtil.d(TAG, "currentTopPackage:" + currentTopPackage + " with displayId:" + displayId);
            if (TextUtils.isEmpty(pkgName)) {
                if (this.mKaraokeManager.isKaraokeApp(currentTopPackage)) {
                    pkgName = currentTopPackage;
                } else {
                    pkgName = this.mKaraokeManager.findTargetPackage(displayId);
                    LogUtil.d(TAG, "findTargetPackage:" + pkgName);
                }
            }
            LogUtil.d(TAG, "targetPkgName:" + pkgName);
            if (TextUtils.isEmpty(pkgName)) {
                showAppDetailPage("", false, displayId);
                return 4;
            } else if (pkgName.equals(currentTopPackage)) {
                if (this.mKaraokeManager.isSupportSpeech(pkgName)) {
                    this.mKaraokeManager.searchSong(displayId, pkgName, artist, song);
                    return 1;
                }
                return 16;
            } else {
                int startStatus = XAppStartManager.getInstance().checkAppStartStatus(pkgName, true, false, displayId);
                int appOpenSpeechCode = convertStartCode(startStatus, pkgName, displayId);
                if (appOpenSpeechCode == 1) {
                    if (!this.mKaraokeManager.isSupportSpeech(pkgName)) {
                        startPackage(pkgName, displayId);
                        return 16;
                    }
                    this.mKaraokeManager.searchSong(displayId, pkgName, artist, song);
                    return 1;
                }
                return appOpenSpeechCode;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return 3;
        }
    }

    private int getTargetDisplayId(String data) {
        if (SharedDisplayManager.hasSharedDisplayFeature() && !TextUtils.isEmpty(data)) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                return getTargetDisplayId(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    private int getTargetDisplayId(JSONObject object) {
        if (SharedDisplayManager.hasSharedDisplayFeature()) {
            try {
                if (object.has(SpeechConstants.KEY_DISPLAY_LOCATION)) {
                    return object.getInt(SpeechConstants.KEY_DISPLAY_LOCATION);
                }
                if (object.has(SpeechConstants.KEY_SOUND_LOCATION)) {
                    int soundLocation = object.getInt(SpeechConstants.KEY_SOUND_LOCATION);
                    int displayId = soundLocation == 2 ? 1 : 0;
                    return displayId;
                }
            } catch (JSONException e) {
                LogUtil.w(TAG, "getTargetDisplayId error:" + e.getMessage());
            }
        }
        return 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [android.content.Intent] */
    /* JADX WARN: Type inference failed for: r1v4, types: [int] */
    /* JADX WARN: Type inference failed for: r1v5 */
    /* JADX WARN: Type inference failed for: r1v8 */
    /* JADX WARN: Type inference failed for: r1v9 */
    /* JADX WARN: Type inference failed for: r2v0, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r2v4, types: [com.xiaopeng.xuiservice.utils.BroadcastManager] */
    private void showAppDetailPage(String packageName, boolean download, int displayId) {
        ?? intent = new Intent(Constants.ACTION_APP_STORE);
        if (!TextUtils.isEmpty(packageName)) {
            String uri = Constants.URI_SHOW_APP_DETAIL + packageName;
            intent.setData(Uri.parse(uri));
        }
        intent.setPackage(Constants.PACKAGE_APP_STORE);
        boolean z = download;
        ?? r1 = z;
        if (download) {
            r1 = z;
            if (this.mXAppStartManager.isInSpecialDeniedStatus(displayId)) {
                r1 = 2;
            }
        }
        LogUtil.d(TAG, "showAppDetailPage with action:" + r1);
        intent.putExtra("action", r1);
        intent.putExtra(Constants.EXTRA_APP_STORE_SCREEN, displayId);
        BroadcastManager.getInstance().sendBroadcast(intent);
    }

    private int convertStartCode(int startCheckCode, String packageName, int displayId) {
        if (startCheckCode == 4) {
            return 10;
        }
        if (startCheckCode == 2) {
            return 5;
        }
        if (startCheckCode == 5) {
            return 12;
        }
        if (startCheckCode == 6) {
            if (AppStoreStatusProvider.getInstance().queryIsAssembling(this.mContext, packageName)) {
                return 4;
            }
            return 8;
        } else if (startCheckCode == 7 || startCheckCode == 14) {
            return 13;
        } else {
            if (startCheckCode == 11) {
                return 14;
            }
            if (Config.CloseDialogBeforeStartApplication) {
                boolean notCancelableDialog = this.mTopWindowHandler.isHaveNotCancelableDialog(0);
                if (notCancelableDialog) {
                    return 9;
                }
                this.mTopWindowHandler.closeDialog(0);
                return 1;
            }
            return 1;
        }
    }

    public int adjustBluetoothVolume(int commandType) {
        if (!PassengerBluetoothManager.getInstance().isDeviceConnected()) {
            return 0;
        }
        return adjustVolume(13, commandType);
    }

    public int setBluetoothVolume(int value) {
        if (!PassengerBluetoothManager.getInstance().isDeviceConnected()) {
            return 0;
        }
        return setVolume(13, value);
    }

    public int getVideoDemandStatus(String data) {
        if (!TextUtils.isEmpty(data)) {
            try {
                JSONObject object = new JSONObject(data);
                int displayId = getTargetDisplayId(object);
                String pkgName = object.optString(SpeechConstants.KEY_PACKAGE_NAME, "");
                if (TextUtils.isEmpty(pkgName)) {
                    pkgName = VideoManager.getInstance().getTargetApp(displayId);
                }
                if (!TextUtils.isEmpty(pkgName) && this.mXAppManagerService.checkAppInstalled(pkgName)) {
                    int startStatus = XAppStartManager.getInstance().checkAppStartStatus(pkgName, true, false, displayId);
                    if (startStatus == 0) {
                        String command = object.optString("command", "");
                        if (!TextUtils.isEmpty(command)) {
                            VideoManager.getInstance().play(displayId, pkgName, command, null);
                            return 100;
                        }
                        return 101;
                    }
                    return convertStartCode(startStatus, pkgName, displayId);
                }
                return 4;
            } catch (JSONException e) {
                e.printStackTrace();
                return 101;
            }
        }
        return 101;
    }

    private int adjustVolume(int streamType, int commandType) {
        int currentVolume = SoundManager.getInstance(this.mContext).getStreamVolume(streamType);
        int maxVolume = SoundManager.getInstance(this.mContext).getStreamMaxVolume(streamType);
        switch (commandType) {
            case 1:
                if (currentVolume != maxVolume) {
                    SoundManager.getInstance(this.mContext).adjustStreamVolume(streamType, 1, 0);
                    break;
                } else {
                    return 2;
                }
            case 2:
                if (currentVolume == 0) {
                    return 3;
                }
                SoundManager.getInstance(this.mContext).adjustStreamVolume(streamType, -1, 0);
                break;
            case 3:
                if (currentVolume != maxVolume) {
                    SoundManager.getInstance(this.mContext).setStreamVolume(streamType, maxVolume, 0);
                    break;
                } else {
                    return 2;
                }
            case 4:
                if (currentVolume == 0) {
                    return 3;
                }
                SoundManager.getInstance(this.mContext).setStreamVolume(streamType, 0, 0);
                break;
            case 5:
                SoundManager.getInstance(this.mContext).adjustStreamVolume(streamType, -100, 0);
                break;
            case 6:
                SoundManager.getInstance(this.mContext).adjustStreamVolume(streamType, 100, 0);
                break;
        }
        return 1;
    }

    private int setVolume(int streamType, int value) {
        SoundManager.getInstance(this.mContext).setStreamVolume(streamType, value, 0);
        return 1;
    }
}
