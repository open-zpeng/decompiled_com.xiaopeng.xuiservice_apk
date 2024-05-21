package com.xiaopeng.xuiservice.xapp;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.bcm.CarBcmManager;
import android.car.hardware.scu.CarScuManager;
import android.car.hardware.vcu.CarVcuManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.provider.Settings;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
/* loaded from: classes5.dex */
public class XAppCarService {
    private static final float CAMERA_DOWNRISE_VALUE = 80.0f;
    private static final String KEY_MIRROR_MODE = "key_xp_mirror_flag";
    private static final String KEY_PANEL_SPEED = "key_panel_car_speed";
    private static final String KEY_STEERING_MODE = "key_xp_steering_flag";
    private static final int MOTION = 1;
    private static final int MOTIONFAST = 2;
    private static final int MOTIONLESS = 0;
    private static final float PANEL_DISAPPEAR_VALUE = 3.0f;
    private static final String TAG = "XAppCarService";
    private static XAppCarService sXAppCarService = null;
    private CarBcmManager mCarBcmManager;
    private CarScuManager mCarScuManager;
    private CarVcuManager mCarVcuManager;
    private Context mContext;
    private int mRawCarSpeed = -1;
    private CarVcuManager.CarVcuEventCallback mVcuCallback = new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xuiservice.xapp.XAppCarService.1
        public void onChangeEvent(CarPropertyValue carPropertyValue) {
            XAppCarService.this.handleVcuEvent(carPropertyValue);
        }

        public void onErrorEvent(int i, int i1) {
        }
    };
    private CarScuManager.CarScuEventCallback mScuCallback = new CarScuManager.CarScuEventCallback() { // from class: com.xiaopeng.xuiservice.xapp.XAppCarService.2
        public void onChangeEvent(CarPropertyValue carPropertyValue) {
            XAppCarService.this.handleScuEvent(carPropertyValue);
        }

        public void onErrorEvent(int i, int i1) {
        }
    };
    private ContentObserver mContentObserver = new ContentObserver(XuiWorkHandler.getInstance()) { // from class: com.xiaopeng.xuiservice.xapp.XAppCarService.3
        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            LogUtil.d(XAppCarService.TAG, "onChange uri:" + uri);
            if (uri.equals(XAppCarService.this.getSystemUri(XAppCarService.KEY_MIRROR_MODE)) || uri.equals(XAppCarService.this.getSystemUri(XAppCarService.KEY_STEERING_MODE))) {
                XAppCarService.this.setSwsControlSceneStatus();
            }
        }
    };

    public static XAppCarService getInstance(Context context) {
        if (sXAppCarService == null) {
            synchronized (XAppCarService.class) {
                if (sXAppCarService == null) {
                    sXAppCarService = new XAppCarService(context);
                }
            }
        }
        return sXAppCarService;
    }

    private XAppCarService(Context context) {
        this.mContext = context;
        CarClientManager.getInstance().addVcuManagerListener(this.mVcuCallback);
        CarClientManager.getInstance().addScuManagerListener(this.mScuCallback);
    }

    public void start() {
        setSwsControlSceneStatus();
        registerSettingsObserver();
    }

    private void registerSettingsObserver() {
        this.mContext.getContentResolver().registerContentObserver(getSystemUri(KEY_MIRROR_MODE), false, this.mContentObserver);
        this.mContext.getContentResolver().registerContentObserver(getSystemUri(KEY_STEERING_MODE), false, this.mContentObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSwsControlSceneStatus() {
        LogUtil.d(TAG, "setSwsControlScene with featureEnable:" + Config.SET_SWS_CONTROL_SCENE_ENABLE);
        if (!Config.SET_SWS_CONTROL_SCENE_ENABLE) {
            return;
        }
        boolean isMirror = getSystemInt(KEY_MIRROR_MODE, 0) == 1;
        boolean isSteering = getSystemInt(KEY_STEERING_MODE, 0) == 1;
        if (isAccEnable()) {
            setSwsControlSceneStatusInternal(3);
        } else if (isCcEnable()) {
            setSwsControlSceneStatusInternal(2);
        } else if (isSteering) {
            setSwsControlSceneStatusInternal(4);
        } else if (isMirror) {
            setSwsControlSceneStatusInternal(1);
        } else {
            setSwsControlSceneStatusInternal(0);
        }
    }

    private void setSwsControlSceneStatusInternal(int status) {
        if (this.mCarBcmManager == null) {
            this.mCarBcmManager = CarClientManager.getInstance().getCarManager("xp_bcm");
        }
        if (this.mCarBcmManager != null) {
            try {
                LogUtil.d(TAG, "setSwsControlSceneStatusInternal status:" + status);
                this.mCarBcmManager.setSwsControlSceneStatus(status);
            } catch (Exception e) {
                LogUtil.w(TAG, "setSwsControlSceneStatusInternal exception:" + e.getMessage());
            }
        }
    }

    private boolean isAccEnable() {
        if (this.mCarScuManager == null) {
            this.mCarScuManager = CarClientManager.getInstance().getCarManager("xp_scu");
        }
        CarScuManager carScuManager = this.mCarScuManager;
        if (carScuManager != null) {
            try {
                int status = carScuManager.getAccStatus();
                return status == 3 || status == 4 || status == 5 || status == 6 || status == 7;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean isCcEnable() {
        if (this.mCarVcuManager == null) {
            this.mCarVcuManager = CarClientManager.getInstance().getCarManager("xp_vcu");
        }
        CarVcuManager carVcuManager = this.mCarVcuManager;
        if (carVcuManager != null) {
            try {
                int status = carVcuManager.getCruiseControlStatus();
                return status == 1 || status == 2;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private int getSystemInt(String key, int defaultValue) {
        try {
            ContentResolver resolver = this.mContext.getContentResolver();
            return Settings.System.getInt(resolver, key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Uri getSystemUri(String name) {
        return Settings.System.getUriFor(name);
    }

    private void saveRawCarSpeed(float speed) {
        int speedRange = speed > PANEL_DISAPPEAR_VALUE ? speed > CAMERA_DOWNRISE_VALUE ? 2 : 1 : 0;
        if (this.mRawCarSpeed != speedRange) {
            try {
                Settings.System.putInt(this.mContext.getContentResolver(), KEY_PANEL_SPEED, speedRange);
            } catch (Exception e) {
            }
            this.mRawCarSpeed = speedRange;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleVcuEvent(CarPropertyValue val) {
        if (val != null && val.getValue() != null) {
            int propertyId = val.getPropertyId();
            if (propertyId == 557847118) {
                setSwsControlSceneStatus();
            } else if (propertyId == 559944229) {
                try {
                    float value = ((Float) val.getValue()).floatValue();
                    saveRawCarSpeed(value);
                } catch (Exception e) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleScuEvent(CarPropertyValue val) {
        if (val != null && val.getValue() != null && val.getPropertyId() == 557852212) {
            setSwsControlSceneStatus();
        }
    }
}
