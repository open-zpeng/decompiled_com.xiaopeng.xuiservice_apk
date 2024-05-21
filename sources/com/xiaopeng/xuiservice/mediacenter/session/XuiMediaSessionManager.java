package com.xiaopeng.xuiservice.mediacenter.session;

import android.app.ActivityThread;
import android.content.ComponentName;
import android.media.AudioManager;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.support.v4.media.MediaMetadataCompat;
import android.view.KeyEvent;
import com.alipay.mobile.aromeservice.RequestParams;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.mediacenter.Constants;
import com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService;
import com.xiaopeng.xuiservice.mediacenter.bluetooth.XuiBtMediaManager;
import com.xiaopeng.xuiservice.utils.PassengerBluetoothManager;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.utils.ToastUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class XuiMediaSessionManager {
    private static final String TAG = "XuiMediaSessionManager";
    private static XuiMediaSessionManager mInstance;
    private MediaController mBluetoothMediaController;
    private List<MediaController> mGlobalActiveControllers;
    private MediaController mGlobalTargetController;
    private MediaController mPrimaryMediaController;
    private MediaController mSubMediaController;
    private MediaController mSystemTargetController;
    private MediaController[] mTargetControllers;
    private boolean mHeadsetDeviceConnected = false;
    private List<TargetChangedListener> mTargetChangedListeners = new ArrayList();
    private HashMap<String, Integer> mNapaMediaModuleSDInfo = new HashMap<>();
    private List<String> mSecondDisplayPackages = new ArrayList();
    private boolean enterBtMedia = false;
    private PassengerBluetoothManager.DeviceStatusListener mDeviceStatusListener = new PassengerBluetoothManager.DeviceStatusListener() { // from class: com.xiaopeng.xuiservice.mediacenter.session.XuiMediaSessionManager.2
        @Override // com.xiaopeng.xuiservice.utils.PassengerBluetoothManager.DeviceStatusListener
        public void onConnectStatusChanged(boolean connected) {
            if (XuiMediaSessionManager.this.mHeadsetDeviceConnected != connected) {
                XuiMediaSessionManager.this.mHeadsetDeviceConnected = connected;
                XuiMediaSessionManager.this.refreshTargetOnBtStatusChanged(connected);
            }
        }
    };
    private MediaSessionManager.OnActiveSessionsChangedListener mSessionsChangedListener = new MediaSessionManager.OnActiveSessionsChangedListener() { // from class: com.xiaopeng.xuiservice.mediacenter.session.XuiMediaSessionManager.3
        @Override // android.media.session.MediaSessionManager.OnActiveSessionsChangedListener
        public void onActiveSessionsChanged(List<MediaController> controllers) {
            LogUtil.d(XuiMediaSessionManager.TAG, "onActiveSessionsChanged");
            MediaCenterHalService.getInstance().getHandler().removeCallbacks(XuiMediaSessionManager.this.mRefreshTargetTask);
            MediaCenterHalService.getInstance().getHandler().postDelayed(XuiMediaSessionManager.this.mRefreshTargetTask, 250L);
        }
    };
    private Runnable mRefreshTargetTask = new Runnable() { // from class: com.xiaopeng.xuiservice.mediacenter.session.XuiMediaSessionManager.4
        @Override // java.lang.Runnable
        public void run() {
            XuiMediaSessionManager xuiMediaSessionManager = XuiMediaSessionManager.this;
            xuiMediaSessionManager.mGlobalActiveControllers = xuiMediaSessionManager.mMediaSessionManager.getActiveSessions(null);
        }
    };
    private XuiBtMediaManager.BTStateChangeListener mBTStateChangeListener = new XuiBtMediaManager.BTStateChangeListener() { // from class: com.xiaopeng.xuiservice.mediacenter.session.XuiMediaSessionManager.5
        @Override // com.xiaopeng.xuiservice.mediacenter.bluetooth.XuiBtMediaManager.BTStateChangeListener
        public void onBtStatusChanged(int state) {
            XuiMediaSessionManager.this.enterBtMedia = state == 6;
            String currentCallingPlayBt = XuiBtMediaManager.getInstance(ActivityThread.currentActivityThread().getApplication()).getCallingPlayBtPackage();
            LogUtil.d(XuiMediaSessionManager.TAG, "onBtStatusChanged:" + state + " &currentCallingPlayBt:" + currentCallingPlayBt);
            if (XuiMediaSessionManager.this.enterBtMedia && Constants.PACKAGE_XP_MUSIC.equals(currentCallingPlayBt)) {
                XuiMediaSessionManager.this.refreshTargetControllerByBtAudioSelected();
            }
        }
    };
    private final MediaSessionManager.Callback mButtonDispatchCallback = new MediaSessionManager.Callback() { // from class: com.xiaopeng.xuiservice.mediacenter.session.XuiMediaSessionManager.6
        public void onMediaKeyEventDispatched(KeyEvent event, MediaSession.Token token) {
        }

        public void onMediaKeyEventDispatched(KeyEvent event, ComponentName receiver) {
        }

        public void onAddressedPlayerChanged(MediaSession.Token token) {
            MediaController controller = new MediaController(ActivityThread.currentActivityThread().getApplication(), token);
            LogUtil.i(XuiMediaSessionManager.TAG, "onAddressedPlayerChanged target pkgName: " + controller.getPackageName());
            if (controller.getMetadata() != null) {
                LogUtil.i(XuiMediaSessionManager.TAG, "onAddressedPlayerChanged metaData:" + controller.getMetadata().getString(MediaMetadataCompat.METADATA_KEY_ARTIST));
            }
            XuiMediaSessionManager.this.onSystemTargetChanged(controller);
        }

        public void onAddressedPlayerChanged(ComponentName receiver) {
            if (receiver != null) {
                LogUtil.d(XuiMediaSessionManager.TAG, "onAddressedPlayerChanged: component=" + receiver.getPackageName());
            }
        }
    };
    private MediaSessionManager mMediaSessionManager = (MediaSessionManager) ActivityThread.currentActivityThread().getApplication().getSystemService("media_session");
    private AudioManager mAudioManager = (AudioManager) ActivityThread.currentActivityThread().getApplication().getSystemService("audio");

    /* loaded from: classes5.dex */
    public interface TargetChangedListener {
        void OnTargetChanged(MediaController[] mediaControllerArr);
    }

    private XuiMediaSessionManager() {
        int arraySize = SharedDisplayManager.hasSharedDisplayFeature() ? 2 : 1;
        this.mTargetControllers = new MediaController[arraySize];
    }

    public static XuiMediaSessionManager getInstance() {
        if (mInstance == null) {
            synchronized (XuiMediaSessionManager.class) {
                if (mInstance == null) {
                    mInstance = new XuiMediaSessionManager();
                }
            }
        }
        return mInstance;
    }

    public void init() {
        registerMediaSessionEvent();
        if (SharedDisplayManager.hasSharedDisplayFeature()) {
            registerSharedDisplayEvent();
            registerHeadsetConnectEvent();
        }
        registerBtStatusEvent();
    }

    public synchronized void registerTargetChangedListener(TargetChangedListener listener) {
        if (!this.mTargetChangedListeners.contains(listener)) {
            this.mTargetChangedListeners.add(listener);
        }
    }

    public synchronized void unregisterTargetChangedListener(TargetChangedListener listener) {
        if (this.mTargetChangedListeners.contains(listener)) {
            this.mTargetChangedListeners.remove(listener);
        }
    }

    public MediaController[] getTargetControllers() {
        return this.mTargetControllers;
    }

    public MediaController getTargetController(String pkgName) {
        List<MediaController> list = this.mGlobalActiveControllers;
        if (list != null && !list.isEmpty()) {
            for (MediaController controller : this.mGlobalActiveControllers) {
                if (pkgName.equals(controller.getPackageName())) {
                    return controller;
                }
            }
            return null;
        }
        return null;
    }

    public void setSDPosition(int displayId, String packageName) {
        int lastPosition = this.mNapaMediaModuleSDInfo.get(packageName).intValue();
        if (lastPosition != displayId) {
            this.mNapaMediaModuleSDInfo.put(packageName, Integer.valueOf(displayId));
        }
    }

    public void onAudioSessionStart(int usage, String pkgName) {
        MediaController mediaController;
        LogUtil.d(TAG, "onAudioSessionStart pkgName:" + pkgName + " &usage=" + usage);
        if (usage == 1 && this.enterBtMedia && (mediaController = this.mSystemTargetController) != null && mediaController.getPackageName().equals(pkgName)) {
            onGlobalTargetChanged(this.mSystemTargetController);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshTargetControllerByBtAudioSelected() {
        LogUtil.d(TAG, "refreshTargetControllerByBtAudioSelected");
        MediaController mediaController = this.mGlobalTargetController;
        if (mediaController == null || !mediaController.getPackageName().equals(Constants.PACKAGE_XP_MUSIC) || !this.mGlobalTargetController.getPackageName().equals(Constants.PACKAGE_BLUETOOTH)) {
            LogUtil.d(TAG, "refreshTargetControllerByBtAudioSelected begin");
            getBluetoothMediaController();
            if (this.mBluetoothMediaController != null) {
                LogUtil.d(TAG, "refreshTargetControllerByBtAudioSelected end");
                onGlobalTargetChanged(this.mBluetoothMediaController);
            }
        }
    }

    private boolean isCurrentTargetPackage(String pkgName) {
        if (this.mTargetControllers != null) {
            int i = 0;
            while (true) {
                MediaController[] mediaControllerArr = this.mTargetControllers;
                if (i < mediaControllerArr.length) {
                    if (!mediaControllerArr[i].getPackageName().equals(pkgName)) {
                        i++;
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    private void registerBtStatusEvent() {
        XuiBtMediaManager.getInstance(ActivityThread.currentActivityThread().getApplication()).addBTStateChangedListener(this.mBTStateChangeListener);
    }

    private void registerMediaSessionEvent() {
        if (this.mMediaSessionManager != null) {
            LogUtil.i(TAG, "registerMediaSessionCallback");
            this.mMediaSessionManager.setXuiCallback(this.mButtonDispatchCallback, MediaCenterHalService.getInstance().getHandler());
        }
    }

    private void registerSharedDisplayEvent() {
        this.mSecondDisplayPackages = getSecondMediaPackages();
        SharedDisplayManager.getInstance().registerListener(new SharedDisplayManager.ISharedDisplayEventListener() { // from class: com.xiaopeng.xuiservice.mediacenter.session.XuiMediaSessionManager.1
            @Override // com.xiaopeng.xuiservice.utils.SharedDisplayManager.ISharedDisplayEventListener
            public void onChanged(String pkgName, int sharedId) {
                boolean isInSecondDisplay;
                boolean isInSecondDisplayPackages;
                LogUtil.d(XuiMediaSessionManager.TAG, "onChanged pkgName:" + pkgName + " &sharedId:" + sharedId + " &secondList:{" + XuiMediaSessionManager.this.getSecondPackageListString());
                if (sharedId == 1) {
                    synchronized (XuiMediaSessionManager.this.mSecondDisplayPackages) {
                        isInSecondDisplayPackages = XuiMediaSessionManager.this.mSecondDisplayPackages.contains(pkgName);
                        if (!isInSecondDisplayPackages) {
                            XuiMediaSessionManager.this.mSecondDisplayPackages.add(pkgName);
                        }
                    }
                    if (!isInSecondDisplayPackages) {
                        LogUtil.d(XuiMediaSessionManager.TAG, "add Second List" + XuiMediaSessionManager.this.getSecondPackageListString());
                        if (XuiMediaSessionManager.this.mPrimaryMediaController == null) {
                            LogUtil.d(XuiMediaSessionManager.TAG, "mPrimaryMediaController == null");
                        } else {
                            LogUtil.d(XuiMediaSessionManager.TAG, "mPrimaryMediaController pkgName:" + XuiMediaSessionManager.this.mPrimaryMediaController.getPackageName());
                        }
                        if (XuiMediaSessionManager.this.mPrimaryMediaController != null && XuiMediaSessionManager.this.mPrimaryMediaController.getPackageName().equals(pkgName)) {
                            XuiMediaSessionManager xuiMediaSessionManager = XuiMediaSessionManager.this;
                            if (xuiMediaSessionManager.isShouldReplaceTarget(xuiMediaSessionManager.mPrimaryMediaController, XuiMediaSessionManager.this.mSubMediaController)) {
                                MediaController swapMediaController = new MediaController(ActivityThread.currentActivityThread().getApplication(), XuiMediaSessionManager.this.mPrimaryMediaController.getSessionToken());
                                XuiMediaSessionManager.this.mSubMediaController = swapMediaController;
                            }
                            XuiMediaSessionManager.this.mPrimaryMediaController = null;
                            LogUtil.i(XuiMediaSessionManager.TAG, "mPrimaryMediaController on changed with bt connected:" + PassengerBluetoothManager.getInstance().isDeviceConnected());
                            if (PassengerBluetoothManager.getInstance().isDeviceConnected()) {
                                XuiMediaSessionManager.this.showMediaChangedToast(0, R.string.media_changed_to_screen_secondary);
                                XuiMediaSessionManager.this.mTargetControllers[0] = XuiMediaSessionManager.this.mPrimaryMediaController;
                                XuiMediaSessionManager.this.mTargetControllers[1] = XuiMediaSessionManager.this.mSubMediaController;
                                XuiMediaSessionManager xuiMediaSessionManager2 = XuiMediaSessionManager.this;
                                xuiMediaSessionManager2.notifyTargetChanged(xuiMediaSessionManager2.mTargetControllers);
                            }
                        }
                    }
                } else if (sharedId == 0) {
                    synchronized (XuiMediaSessionManager.this.mSecondDisplayPackages) {
                        isInSecondDisplay = XuiMediaSessionManager.this.mSecondDisplayPackages.contains(pkgName);
                        if (isInSecondDisplay) {
                            XuiMediaSessionManager.this.mSecondDisplayPackages.remove(pkgName);
                        }
                    }
                    if (isInSecondDisplay && XuiMediaSessionManager.this.mSubMediaController != null && XuiMediaSessionManager.this.mSubMediaController.getPackageName().equals(pkgName)) {
                        XuiMediaSessionManager xuiMediaSessionManager3 = XuiMediaSessionManager.this;
                        if (xuiMediaSessionManager3.isShouldReplaceTarget(xuiMediaSessionManager3.mSubMediaController, XuiMediaSessionManager.this.mPrimaryMediaController)) {
                            MediaController swapMediaController2 = new MediaController(ActivityThread.currentActivityThread().getApplication(), XuiMediaSessionManager.this.mSubMediaController.getSessionToken());
                            XuiMediaSessionManager.this.mPrimaryMediaController = swapMediaController2;
                        }
                        XuiMediaSessionManager.this.mSubMediaController = null;
                        LogUtil.d(XuiMediaSessionManager.TAG, "mSubMediaController on changed with bt connected:" + PassengerBluetoothManager.getInstance().isDeviceConnected());
                        if (PassengerBluetoothManager.getInstance().isDeviceConnected()) {
                            XuiMediaSessionManager.this.showMediaChangedToast(1, R.string.media_changed_to_screen_primary);
                            XuiMediaSessionManager.this.mTargetControllers[0] = XuiMediaSessionManager.this.mPrimaryMediaController;
                            XuiMediaSessionManager.this.mTargetControllers[1] = XuiMediaSessionManager.this.mSubMediaController;
                            XuiMediaSessionManager xuiMediaSessionManager4 = XuiMediaSessionManager.this;
                            xuiMediaSessionManager4.notifyTargetChanged(xuiMediaSessionManager4.mTargetControllers);
                        }
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isShouldReplaceTarget(MediaController fromController, MediaController targetController) {
        List<String> focusList;
        AudioManager audioManager = this.mAudioManager;
        if (audioManager != null && targetController != null && (focusList = audioManager.getAudioFocusPackageNameListByPosition(2)) != null && !focusList.isEmpty() && !focusList.contains(fromController.getPackageName()) && focusList.contains(targetController.getPackageName())) {
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getSecondPackageListString() {
        List<String> list = this.mSecondDisplayPackages;
        if (list != null && !list.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (String pkgName : this.mSecondDisplayPackages) {
                builder.append(pkgName);
                builder.append("||");
            }
            return builder.toString();
        }
        return "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showMediaChangedToast(int displayId, int resId) {
        ToastUtil.showToast(ActivityThread.currentActivityThread().getApplication(), resId, 1, displayId);
    }

    private void registerHeadsetConnectEvent() {
        this.mHeadsetDeviceConnected = PassengerBluetoothManager.getInstance().isDeviceConnected();
        PassengerBluetoothManager.getInstance().registerListener(this.mDeviceStatusListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshTargetOnBtStatusChanged(boolean connected) {
        LogUtil.d(TAG, "on SubScreen Bluetooth Status Changed" + connected);
        if (connected) {
            MediaController mediaController = this.mGlobalTargetController;
            if (mediaController != null) {
                if (this.mSecondDisplayPackages.contains(mediaController.getPackageName())) {
                    LogUtil.d(TAG, "the global target on second");
                    this.mPrimaryMediaController = null;
                    MediaController mediaController2 = this.mGlobalTargetController;
                    this.mSubMediaController = mediaController2;
                    MediaController[] mediaControllerArr = this.mTargetControllers;
                    mediaControllerArr[0] = null;
                    mediaControllerArr[1] = mediaController2;
                    notifyTargetChanged(mediaControllerArr);
                    return;
                }
                LogUtil.d(TAG, "the global target on primary");
                MediaController mediaController3 = this.mGlobalTargetController;
                this.mPrimaryMediaController = mediaController3;
                this.mSubMediaController = null;
                MediaController[] mediaControllerArr2 = this.mTargetControllers;
                mediaControllerArr2[0] = mediaController3;
                mediaControllerArr2[1] = null;
                notifyTargetChanged(mediaControllerArr2);
                return;
            }
            return;
        }
        MediaController[] mediaControllerArr3 = this.mTargetControllers;
        MediaController mediaController4 = this.mGlobalTargetController;
        mediaControllerArr3[0] = mediaController4;
        mediaControllerArr3[1] = mediaController4;
        notifyTargetChanged(mediaControllerArr3);
    }

    private String getControllerListString(List<MediaController> controllers) {
        StringBuilder sb = new StringBuilder();
        for (MediaController controller : controllers) {
            sb.append("||");
            sb.append(controller.getPackageName());
        }
        return sb.toString();
    }

    private String getControllerArrayString(MediaController[] controllers) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < controllers.length; i++) {
            sb.append("||");
            if (controllers[i] != null) {
                sb.append(controllers[i].getPackageName());
            }
        }
        return sb.toString();
    }

    private boolean isMediaSessionPackage(String pkgName) {
        for (MediaController mediaController : this.mGlobalActiveControllers) {
            if (pkgName.equals(mediaController.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    private synchronized void refreshTargetController() {
        MediaController primaryTarget;
        MediaController secondTarget;
        if (this.mGlobalActiveControllers != null) {
            LogUtil.d(TAG, "refreshTargetController controllers:" + getControllerListString(this.mGlobalActiveControllers));
        }
        if (!SharedDisplayManager.hasSharedDisplayFeature()) {
            this.mTargetControllers[0] = getGlobalTarget();
        } else {
            if (this.mHeadsetDeviceConnected) {
                primaryTarget = getPrimaryScreenTarget();
                secondTarget = getSecondScreenTarget();
            } else {
                primaryTarget = getGlobalTarget();
                secondTarget = primaryTarget;
            }
            this.mTargetControllers[0] = primaryTarget;
            this.mTargetControllers[1] = secondTarget;
        }
        notifyTargetChanged(this.mTargetControllers);
    }

    private MediaController getPrimaryScreenTarget() {
        List<MediaController> list = this.mGlobalActiveControllers;
        if (list != null && !list.isEmpty()) {
            for (MediaController controller : this.mGlobalActiveControllers) {
                if (!this.mSecondDisplayPackages.contains(controller.getPackageName())) {
                    return controller;
                }
            }
            return null;
        }
        return null;
    }

    private List getFilterPackages(List<String> jsonStringList) {
        List<String> pkgList = new ArrayList<>();
        if (jsonStringList != null && !jsonStringList.isEmpty()) {
            for (String value : jsonStringList) {
                try {
                    JSONObject jsonObject = new JSONObject(value);
                    pkgList.add(jsonObject.optString(RequestParams.REQUEST_KEY_PACKAGE_NAME));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return pkgList;
    }

    private MediaController getSecondScreenTarget() {
        List<MediaController> list = this.mGlobalActiveControllers;
        if (list != null && !list.isEmpty()) {
            for (MediaController controller : this.mGlobalActiveControllers) {
                if (this.mSecondDisplayPackages.contains(controller.getPackageName())) {
                    return controller;
                }
            }
            return null;
        }
        return null;
    }

    private List<String> getSecondMediaPackages() {
        List<String> jsonList = SharedDisplayManager.getInstance().getSharedPackages();
        List<String> pkgList = new ArrayList<>();
        if (jsonList != null && !jsonList.isEmpty()) {
            for (String jsonValue : jsonList) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonValue);
                    if (jsonObject.optInt("sharedId") == 1) {
                        pkgList.add(jsonObject.optString(RequestParams.REQUEST_KEY_PACKAGE_NAME));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        StringBuffer buffer = new StringBuffer();
        for (String pkg : pkgList) {
            buffer.append("||");
            buffer.append(pkg);
        }
        LogUtil.d(TAG, "secondMediaPackages:" + buffer.toString());
        return pkgList;
    }

    private MediaController getGlobalTarget() {
        List<MediaController> list = this.mGlobalActiveControllers;
        if (list != null && !list.isEmpty()) {
            return this.mGlobalActiveControllers.get(0);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyTargetChanged(MediaController[] targetControllers) {
        LogUtil.i(TAG, "notifyTargetChanged:" + getControllerArrayString(targetControllers));
        List<TargetChangedListener> list = this.mTargetChangedListeners;
        if (list != null && !list.isEmpty()) {
            for (TargetChangedListener listener : this.mTargetChangedListeners) {
                listener.OnTargetChanged(targetControllers);
            }
        }
    }

    private MediaController[] getXpMusicControllers() {
        MediaController[] controllers = new MediaController[2];
        controllers[0] = getBluetoothMediaController();
        if (SharedDisplayManager.hasSharedDisplayFeature()) {
            controllers[1] = this.mTargetControllers[1];
        }
        return controllers;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0037  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private android.media.session.MediaController getBluetoothMediaController() {
        /*
            r5 = this;
            android.media.session.MediaController r0 = r5.mBluetoothMediaController
            if (r0 != 0) goto L59
            android.media.session.MediaSessionManager r0 = r5.mMediaSessionManager
            r1 = 0
            java.util.List r0 = r0.getActiveSessions(r1)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "getBluetoothMediaController list:"
            r1.append(r2)
            java.lang.String r2 = r5.getControllerListString(r0)
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            java.lang.String r2 = "XuiMediaSessionManager"
            com.xiaopeng.xuimanager.utils.LogUtil.d(r2, r1)
            if (r0 == 0) goto L59
            boolean r1 = r0.isEmpty()
            if (r1 != 0) goto L59
            java.util.Iterator r1 = r0.iterator()
        L31:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L59
            java.lang.Object r2 = r1.next()
            android.media.session.MediaController r2 = (android.media.session.MediaController) r2
            java.lang.String r3 = r2.getPackageName()
            java.lang.String r4 = "com.android.bluetooth"
            boolean r3 = r4.equals(r3)
            if (r3 != 0) goto L57
            java.lang.String r3 = r2.getPackageName()
            java.lang.String r4 = "com.xiaopeng.musicradio"
            boolean r3 = r4.equals(r3)
            if (r3 == 0) goto L56
            goto L57
        L56:
            goto L31
        L57:
            r5.mBluetoothMediaController = r2
        L59:
            android.media.session.MediaController r0 = r5.mBluetoothMediaController
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xuiservice.mediacenter.session.XuiMediaSessionManager.getBluetoothMediaController():android.media.session.MediaController");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSystemTargetChanged(MediaController target) {
        this.mSystemTargetController = target;
        onGlobalTargetChanged(target);
    }

    private void onGlobalTargetChanged(MediaController target) {
        this.mGlobalTargetController = target;
        if (!SharedDisplayManager.hasSharedDisplayFeature()) {
            this.mTargetControllers[0] = target;
        } else {
            this.mGlobalActiveControllers = this.mMediaSessionManager.getActiveSessions(null);
            if (!PassengerBluetoothManager.getInstance().isDeviceConnected()) {
                LogUtil.d(TAG, "PassengerBluetoothManager not connected");
                MediaController[] mediaControllerArr = this.mTargetControllers;
                mediaControllerArr[0] = target;
                mediaControllerArr[1] = target;
            } else if (this.mSecondDisplayPackages.contains(target.getPackageName())) {
                LogUtil.d(TAG, "the package on second display");
                this.mSubMediaController = target;
                this.mTargetControllers[1] = this.mSubMediaController;
            } else {
                this.mPrimaryMediaController = target;
                this.mTargetControllers[0] = this.mPrimaryMediaController;
            }
        }
        notifyTargetChanged(this.mTargetControllers);
    }
}
