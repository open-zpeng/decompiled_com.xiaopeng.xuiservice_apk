package com.xiaopeng.xuiservice.smart;

import android.app.ActivityThread;
import android.app.UiModeManager;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.mcu.CarMcuManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemProperties;
import android.provider.Settings;
import com.xiaopeng.xuimanager.XUIManager;
import com.xiaopeng.xuimanager.XUIServiceNotConnectedException;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.smart.condition.operator.Operators;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.PrintWriter;
/* loaded from: classes5.dex */
public class SmartThemeService extends BaseSmartService {
    private static final String CURRENT_THEME = "current_theme";
    private static final String KEY_NAPA_XSPORT = "key_napa_xsport";
    private static final String TAG = "SmartThemeService";
    private static final String THEME_BOOST = "boost";
    private static final String THEME_XSPORT = "xsport";
    private final Handler mHandler;
    private static String current_theme = "";
    private static boolean isSupportXPTheme = false;
    private static boolean isXsport = false;
    private static boolean isBoost = false;
    private static boolean isIgON = true;
    private static boolean isDefferTheme = false;

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService, com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
    public /* bridge */ /* synthetic */ void debugDump(PrintWriter printWriter, String[] strArr) {
        super.debugDump(printWriter, strArr);
    }

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService
    public /* bridge */ /* synthetic */ void dump(PrintWriter printWriter, String[] strArr) {
        super.dump(printWriter, strArr);
    }

    private SmartThemeService(Context context) {
        super(context);
        this.mHandler = new Handler() { // from class: com.xiaopeng.xuiservice.smart.SmartThemeService.1
        };
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
        try {
            boolean z = true;
            if (Settings.Global.getInt(this.mContext.getContentResolver(), KEY_NAPA_XSPORT, 0) != 1) {
                z = false;
            }
            isSupportXPTheme = z;
            LogUtil.d(TAG, "isSupportXPTheme=" + isSupportXPTheme);
        } catch (Exception e) {
            LogUtil.i(TAG, "isSupportXPTheme getInt e=" + e);
        }
        registerProvider();
        addCanSignalListener();
        initSportSettings();
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initSportSettings() {
        if ("1".equals(Settings.System.getString(this.mContext.getContentResolver(), THEME_XSPORT))) {
            return;
        }
        boolean bootCompleted = "1".equals(SystemProperties.get(XUIConfig.PROPERTY_BOOT_COMPLETE));
        if (bootCompleted) {
            LogUtil.d(TAG, "bootCompleted=" + bootCompleted);
            if (isSupportXPTheme) {
                Settings.System.putString(this.mContext.getContentResolver(), THEME_XSPORT, "1");
                return;
            }
            return;
        }
        this.mHandler.postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.SmartThemeService.2
            @Override // java.lang.Runnable
            public void run() {
                SmartThemeService.this.initSportSettings();
            }
        }, 500L);
    }

    private void registerProvider() {
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor(CURRENT_THEME), true, new ContentObserver(XuiWorkHandler.getInstance()) { // from class: com.xiaopeng.xuiservice.smart.SmartThemeService.3
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                if (SmartThemeService.isIgON) {
                    SmartThemeService.this.applyTheme();
                    boolean unused = SmartThemeService.isDefferTheme = false;
                    return;
                }
                boolean unused2 = SmartThemeService.isDefferTheme = true;
            }
        });
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor(KEY_NAPA_XSPORT), true, new ContentObserver(XuiWorkHandler.getInstance()) { // from class: com.xiaopeng.xuiservice.smart.SmartThemeService.4
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                SmartThemeService.this.resetFeature();
            }
        });
        if (isSupportXPTheme) {
            handleXsport();
            handleBoost();
            this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor(THEME_XSPORT), true, new ContentObserver(XuiWorkHandler.getInstance()) { // from class: com.xiaopeng.xuiservice.smart.SmartThemeService.5
                @Override // android.database.ContentObserver
                public void onChange(boolean selfChange, Uri uri) {
                    super.onChange(selfChange, uri);
                    SmartThemeService.this.handleXsport();
                }
            });
            this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor(THEME_BOOST), true, new ContentObserver(XuiWorkHandler.getInstance()) { // from class: com.xiaopeng.xuiservice.smart.SmartThemeService.6
                @Override // android.database.ContentObserver
                public void onChange(boolean selfChange, Uri uri) {
                    super.onChange(selfChange, uri);
                    SmartThemeService.this.handleBoost();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetFeature() {
        Settings.System.putString(this.mContext.getContentResolver(), CURRENT_THEME, "");
        Settings.System.putInt(this.mContext.getContentResolver(), THEME_BOOST, 0);
        Settings.System.putInt(this.mContext.getContentResolver(), THEME_XSPORT, 0);
        onInit();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleXsport() {
        String str = THEME_XSPORT;
        boolean xsportMode = checkMode(THEME_XSPORT);
        String res = xsportMode ? Operators.OP_IN : "out";
        LogUtil.i(TAG, "handleXsport " + res);
        if (!checkMode(THEME_BOOST)) {
            ContentResolver contentResolver = this.mContext.getContentResolver();
            if (!xsportMode) {
                str = "";
            }
            Settings.System.putString(contentResolver, CURRENT_THEME, str);
        }
        isXsport = xsportMode;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBoost() {
        String str = THEME_BOOST;
        boolean boostMode = checkMode(THEME_BOOST);
        String res = boostMode ? Operators.OP_IN : "out";
        LogUtil.i(TAG, "handleBoost " + res);
        ContentResolver contentResolver = this.mContext.getContentResolver();
        if (!boostMode) {
            str = checkMode(THEME_XSPORT) ? THEME_XSPORT : "";
        }
        Settings.System.putString(contentResolver, CURRENT_THEME, str);
        isBoost = boostMode;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyTheme() {
        String currentTheme = checkCurrentTheme();
        String str = current_theme;
        if (str != null && !str.equals(currentTheme)) {
            current_theme = currentTheme;
            LogUtil.i(TAG, "applyTheme " + currentTheme);
            UiModeManager modeManager = (UiModeManager) this.mContext.getSystemService("uimode");
            modeManager.applyThemeStyle(currentTheme);
        }
    }

    private boolean checkMode(String mode) {
        try {
            return Integer.parseInt(Settings.System.getString(this.mContext.getContentResolver(), mode)) == 1;
        } catch (Exception e) {
            LogUtil.e(TAG, "checkMode " + mode + "failed, " + e);
            return false;
        }
    }

    private String checkCurrentTheme() {
        String ret = null;
        try {
            ret = Settings.System.getString(this.mContext.getContentResolver(), CURRENT_THEME);
        } catch (Exception e) {
            LogUtil.e(TAG, "checkCurrentTheme failed, " + e);
        }
        return ret == null ? "" : ret;
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void initCarListener() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onCarManagerInited() {
        LogUtil.d(TAG, "onCarManagerInited");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    public void initXUIManager() throws XUIServiceNotConnectedException {
        super.initXUIManager();
        XUIManager xuiManager = getXuiManager();
        if (xuiManager == null) {
            LogUtil.w(TAG, "xuiManager is null");
        }
    }

    public static SmartThemeService getInstance() {
        return InstanceHolder.sService;
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final SmartThemeService sService = new SmartThemeService(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected boolean needConnectXUI() {
        return true;
    }

    private void addCanSignalListener() {
        CarClientManager.getInstance().addMcuManagerListener(new CarMcuManager.CarMcuEventCallback() { // from class: com.xiaopeng.xuiservice.smart.SmartThemeService.7
            public void onChangeEvent(CarPropertyValue value) {
                SmartThemeService.this.handlePropertyChange(value);
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePropertyChange(CarPropertyValue value) {
        int propertyId = value.getPropertyId();
        if (propertyId == 557847561) {
            int status = ((Integer) value.getValue()).intValue();
            isIgON = 1 == status;
            if (isIgON && isDefferTheme) {
                applyTheme();
                isDefferTheme = false;
            }
        }
    }
}
