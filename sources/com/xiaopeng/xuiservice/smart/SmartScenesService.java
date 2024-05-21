package com.xiaopeng.xuiservice.smart;

import android.app.ActivityThread;
import android.content.Context;
import com.xiaopeng.xuimanager.XUIManager;
import com.xiaopeng.xuimanager.XUIServiceNotConnectedException;
import com.xiaopeng.xuimanager.ambientlight.AmbientLightManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.carcontrol.CommonMsgController;
import java.io.PrintWriter;
/* loaded from: classes5.dex */
public class SmartScenesService extends BaseSmartService {
    private static final String PROP_IS_ATL_EXIST = "persist.sys.xiaopeng.ATLS";
    private static final String TAG = "SmartScenesService";
    private AmbientLightManager mAmbientLightManager;
    private CommonMsgController mCommonMsgController;
    private CommonMsgController.InternalCommonEventListener mInternalCommonEventListener;
    private static boolean savedAtlOpenStatus = false;
    private static String StoredEffectType = "stable_effect";
    private static int StoredEffectColor = -1;

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService, com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
    public /* bridge */ /* synthetic */ void debugDump(PrintWriter printWriter, String[] strArr) {
        super.debugDump(printWriter, strArr);
    }

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService
    public /* bridge */ /* synthetic */ void dump(PrintWriter printWriter, String[] strArr) {
        super.dump(printWriter, strArr);
    }

    private SmartScenesService(Context context) {
        super(context);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        try {
            if (this.mCommonMsgController != null) {
                this.mCommonMsgController.unregisterListener(this.mInternalCommonEventListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            return;
        }
        this.mCommonMsgController = CommonMsgController.getInstance(this.mContext);
        this.mInternalCommonEventListener = new CommonMsgController.InternalCommonEventListener() { // from class: com.xiaopeng.xuiservice.smart.SmartScenesService.1
            @Override // com.xiaopeng.xuiservice.carcontrol.CommonMsgController.InternalCommonEventListener
            public void onInternalCommonEvent(int eventType, int eventValue) {
                if (eventType == 10) {
                    if (eventValue == 0) {
                        SmartScenesService.this.restoreAmbientLight();
                    } else {
                        SmartScenesService.this.setAmbientLightStatus("follow_speed", -1);
                    }
                } else if (eventType == 11) {
                    if (eventValue == 0) {
                        SmartScenesService.this.restoreAmbientLight();
                    } else {
                        SmartScenesService.this.setAmbientLightStatus("music", eventValue);
                    }
                }
            }
        };
        try {
            this.mCommonMsgController.registerListener(this.mInternalCommonEventListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean hasAtl() {
        return XUIConfig.hasFeature(XUIConfig.PROPERTY_ATLS) && this.mAmbientLightManager != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int setAmbientLightStatus(String effect, int color) {
        if (hasAtl()) {
            LogUtil.i(TAG, "setAmbientLightEffect " + effect + "|color " + color);
            try {
                savedAtlOpenStatus = this.mAmbientLightManager.getAmbientLightOpen();
                StoredEffectType = this.mAmbientLightManager.getAmbientLightEffectType();
                this.mAmbientLightManager.setAmbientLightOpen(true);
                this.mAmbientLightManager.setAmbientLightEffectType(effect);
                if (color != -1) {
                    StoredEffectColor = this.mAmbientLightManager.getAmbientLightMonoColor(effect);
                    this.mAmbientLightManager.setAmbientLightMonoColor(effect, color);
                    return 0;
                }
                return 0;
            } catch (Exception e) {
                return 0;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int restoreAmbientLight() {
        if (hasAtl()) {
            LogUtil.i(TAG, "restoreAmbientLight " + savedAtlOpenStatus + " " + StoredEffectType);
            try {
                if (savedAtlOpenStatus != this.mAmbientLightManager.getAmbientLightOpen()) {
                    this.mAmbientLightManager.setAmbientLightOpen(savedAtlOpenStatus);
                }
                String curEffect = this.mAmbientLightManager.getAmbientLightEffectType();
                if (this.mAmbientLightManager.getAmbientLightMonoColor(curEffect) != StoredEffectColor) {
                    this.mAmbientLightManager.setAmbientLightMonoColor(curEffect, StoredEffectColor);
                }
                if (!curEffect.equals(StoredEffectType)) {
                    this.mAmbientLightManager.setAmbientLightEffectType(StoredEffectType);
                    return 0;
                }
                return 0;
            } catch (Exception e) {
                return 0;
            }
        }
        return -1;
    }

    public static SmartScenesService getInstance() {
        return InstanceHolder.sService;
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final SmartScenesService sService = new SmartScenesService(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected boolean needConnectXUI() {
        return true;
    }
}
