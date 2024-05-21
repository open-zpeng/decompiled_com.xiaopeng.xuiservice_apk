package com.xiaopeng.xuiservice.xapp.mode.octopus;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import com.qiyi.video.qsrbase.QYSConst;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.xapp.mode.octopus.Constants;
import com.xiaopeng.xuiservice.xapp.mode.octopus.bean.KeyMapInfo;
import com.xiaopeng.xuiservice.xapp.mode.octopus.bean.KeyMapInfos;
import com.xiaopeng.xuiservice.xapp.mode.octopus.bean.KeyViewInfo;
import com.xiaopeng.xuiservice.xapp.mode.octopus.db.ConfigLoaderCallback;
import com.xiaopeng.xuiservice.xapp.mode.octopus.db.ConfigRepository;
import com.xiaopeng.xuiservice.xapp.mode.octopus.db.KeyConfig;
import com.xiaopeng.xuiservice.xapp.util.GsonUtil;
import com.xiaopeng.xuiservice.xapp.util.ResourceUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class KeyConfigManager {
    private static final int MSG_CONFIG_UPLOAD = 1;
    private static final int SIZE_CONFIG = 3;
    private static final String TAG = "KeyConfigManager";
    private static volatile KeyConfigManager mInstance;
    private ConfigRepository mConfigRepository;
    private List<KeyConfig> mDefaultKeyConfig;
    private HandlerThread mHandlerThread;
    private List<KeyViewInfo> mKeyViewInfos;
    private Handler mWorkHandler;

    private KeyConfigManager() {
        initDefaultKeyInfo();
        initDefaultKeyConfigs();
        this.mHandlerThread = new HandlerThread("ConfigManager");
        this.mHandlerThread.start();
        this.mWorkHandler = new Handler() { // from class: com.xiaopeng.xuiservice.xapp.mode.octopus.KeyConfigManager.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    KeyConfigManager.this.processConfigUpload((ConfigUpload) msg.obj);
                }
            }
        };
    }

    public static KeyConfigManager getInstance() {
        if (mInstance == null) {
            synchronized (KeyConfigManager.class) {
                if (mInstance == null) {
                    mInstance = new KeyConfigManager();
                }
            }
        }
        return mInstance;
    }

    public void setupWithConfigRepository(ConfigRepository configRepository) {
        this.mConfigRepository = configRepository;
    }

    public void saveConfig(KeyConfig keyConfig) {
        ConfigRepository configRepository;
        if (keyConfig != null && (configRepository = this.mConfigRepository) != null) {
            configRepository.saveConfig(keyConfig);
        }
    }

    public void getKeyConfigs(String pkgName, ConfigLoaderCallback callback) {
        this.mConfigRepository.getConfigs(pkgName, callback);
    }

    public void onAppConfigUpload(String pkg, String jsonString) {
        Message.obtain(this.mWorkHandler, 1, new ConfigUpload(pkg, jsonString)).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processConfigUpload(ConfigUpload configUpload) {
        ConfigRepository configRepository;
        List<KeyConfig> configs = getKeyConfigsFromAppConfig(configUpload);
        if (configs != null && configs.size() > 0 && (configRepository = this.mConfigRepository) != null) {
            configRepository.saveConfigs(configs);
        }
    }

    private List<KeyConfig> getKeyConfigsFromAppConfig(ConfigUpload configUpload) {
        String jsonString = configUpload.configJsonString;
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            LogUtil.d(TAG, "getKeyConfigsFromAppConfig:" + jsonString);
            List<KeyConfig> keyConfigList = new ArrayList<>();
            JSONObject appInfoJsonObject = new JSONObject(jsonString);
            JSONObject joystickConfig = appInfoJsonObject.optJSONObject("joystickConfig");
            if (joystickConfig == null) {
                return null;
            }
            String pkgName = joystickConfig.optString("pkgName");
            joystickConfig.optBoolean("needJoystickMotionEvent");
            JSONArray eventMapConfigs = joystickConfig.getJSONArray("eventMapConfigs");
            LogUtil.d(TAG, "eventMapConfigs length:" + eventMapConfigs.length());
            for (int i = 0; i < eventMapConfigs.length(); i++) {
                JSONObject configJsonItem = eventMapConfigs.getJSONObject(i);
                String configName = configJsonItem.optString("configName");
                KeyConfig keyConfig = new KeyConfig();
                keyConfig.pkgName = pkgName;
                keyConfig.configName = configName;
                keyConfig.configInfo = configJsonItem.toString();
                LogUtil.d(TAG, "pkgName:" + pkgName + " & configInfo:" + keyConfig.configInfo);
                keyConfigList.add(keyConfig);
            }
            return keyConfigList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* loaded from: classes5.dex */
    public class ConfigUpload {
        public String configJsonString;
        public String pkgName;

        public ConfigUpload(String pkgName, String jsonString) {
            this.pkgName = pkgName;
            this.configJsonString = jsonString;
        }
    }

    private void initDefaultKeyConfigs() {
        this.mDefaultKeyConfig = new ArrayList();
        int i = 0;
        while (i < 3) {
            KeyConfig keyConfig = new KeyConfig();
            keyConfig.pkgName = "";
            keyConfig.configName = ResourceUtil.getArrayString(R.array.XKeyConfigArray)[i];
            keyConfig.selected = i == 0 ? 1 : 0;
            keyConfig.configInfo = GsonUtil.toJson(getDefaultKeyMapInfos());
            this.mDefaultKeyConfig.add(keyConfig);
            i++;
        }
    }

    public KeyMapInfos getDefaultKeyMapInfos() {
        KeyMapInfos keyMapInfos = new KeyMapInfos();
        Rect rect = new Rect();
        rect.left = ResourceUtil.getDimen(R.dimen.key_button_l_margin_left);
        rect.top = ResourceUtil.getDimen(R.dimen.key_button_l_margin_top);
        rect.right = rect.left + ResourceUtil.getDimen(R.dimen.key_button_medium_size);
        rect.bottom = rect.top + ResourceUtil.getDimen(R.dimen.key_button_medium_size);
        keyMapInfos.setTouchRect(rect);
        List<KeyMapInfo> keyMapInfoList = new ArrayList<>();
        for (KeyViewInfo keyViewInfo : this.mKeyViewInfos) {
            if (keyViewInfo.mKeyCode != 8002 && keyViewInfo.mKeyCode != 8001) {
                KeyMapInfo keyMapInfo = new KeyMapInfo();
                keyMapInfo.keycode = keyViewInfo.mKeyCode;
                keyMapInfo.desc = keyViewInfo.mKeyCodeIndexName;
                keyMapInfo.x = keyViewInfo.mPoint.x + (keyViewInfo.mWidth / 2);
                keyMapInfo.y = keyViewInfo.mPoint.y + (keyViewInfo.mHeight / 2);
                keyMapInfoList.add(keyMapInfo);
            }
        }
        keyMapInfos.setKeyMap(keyMapInfoList);
        return keyMapInfos;
    }

    private void initDefaultKeyInfo() {
        this.mKeyViewInfos = new ArrayList();
        this.mKeyViewInfos.add(new KeyViewInfo(96, "a", R.drawable.selector_ic_game_a, new Point(ResourceUtil.getDimen(R.dimen.key_button_a_margin_left), ResourceUtil.getDimen(R.dimen.key_button_a_margin_top)), ResourceUtil.getDimen(R.dimen.key_button_small_size), ResourceUtil.getDimen(R.dimen.key_button_small_size)));
        this.mKeyViewInfos.add(new KeyViewInfo(97, "b", R.drawable.selector_ic_game_b, new Point(ResourceUtil.getDimen(R.dimen.key_button_b_margin_left), ResourceUtil.getDimen(R.dimen.key_button_b_margin_top)), ResourceUtil.getDimen(R.dimen.key_button_small_size), ResourceUtil.getDimen(R.dimen.key_button_small_size)));
        this.mKeyViewInfos.add(new KeyViewInfo(99, "x", R.drawable.selector_ic_game_x, new Point(ResourceUtil.getDimen(R.dimen.key_button_x_margin_left), ResourceUtil.getDimen(R.dimen.key_button_x_margin_top)), ResourceUtil.getDimen(R.dimen.key_button_small_size), ResourceUtil.getDimen(R.dimen.key_button_small_size)));
        this.mKeyViewInfos.add(new KeyViewInfo(100, "y", R.drawable.selector_ic_game_y, new Point(ResourceUtil.getDimen(R.dimen.key_button_y_margin_left), ResourceUtil.getDimen(R.dimen.key_button_y_margin_top)), ResourceUtil.getDimen(R.dimen.key_button_small_size), ResourceUtil.getDimen(R.dimen.key_button_small_size)));
        this.mKeyViewInfos.add(new KeyViewInfo(Constants.SpecialKeyEvent.KEYCODE_LEFT_NAVI, "l", R.drawable.selector_ic_game_l, new Point(ResourceUtil.getDimen(R.dimen.key_button_l_margin_left), ResourceUtil.getDimen(R.dimen.key_button_l_margin_top)), ResourceUtil.getDimen(R.dimen.key_button_medium_size), ResourceUtil.getDimen(R.dimen.key_button_medium_size)));
        this.mKeyViewInfos.add(new KeyViewInfo(Constants.SpecialKeyEvent.KEYCODE_RIGHT_NAVI, "r", R.drawable.selector_ic_game_r, new Point(ResourceUtil.getDimen(R.dimen.key_button_r_margin_left), ResourceUtil.getDimen(R.dimen.key_button_r_margin_top)), ResourceUtil.getDimen(R.dimen.key_button_medium_size), ResourceUtil.getDimen(R.dimen.key_button_medium_size)));
        this.mKeyViewInfos.add(new KeyViewInfo(102, "l1", R.drawable.selector_ic_game_lb, new Point(ResourceUtil.getDimen(R.dimen.key_button_lb_margin_left), ResourceUtil.getDimen(R.dimen.key_button_lb_margin_top)), ResourceUtil.getDimen(R.dimen.key_button_small_size), ResourceUtil.getDimen(R.dimen.key_button_small_size)));
        this.mKeyViewInfos.add(new KeyViewInfo(104, "l2", R.drawable.selector_ic_game_lt, new Point(ResourceUtil.getDimen(R.dimen.key_button_lt_margin_left), ResourceUtil.getDimen(R.dimen.key_button_lt_margin_top)), ResourceUtil.getDimen(R.dimen.key_button_small_size), ResourceUtil.getDimen(R.dimen.key_button_small_size)));
        this.mKeyViewInfos.add(new KeyViewInfo(103, "r1", R.drawable.selector_ic_game_rb, new Point(ResourceUtil.getDimen(R.dimen.key_button_rb_margin_left), ResourceUtil.getDimen(R.dimen.key_button_rb_margin_top)), ResourceUtil.getDimen(R.dimen.key_button_small_size), ResourceUtil.getDimen(R.dimen.key_button_small_size)));
        this.mKeyViewInfos.add(new KeyViewInfo(105, "r2", R.drawable.selector_ic_game_rt, new Point(ResourceUtil.getDimen(R.dimen.key_button_rt_margin_left), ResourceUtil.getDimen(R.dimen.key_button_rt_margin_top)), ResourceUtil.getDimen(R.dimen.key_button_small_size), ResourceUtil.getDimen(R.dimen.key_button_small_size)));
        this.mKeyViewInfos.add(new KeyViewInfo(109, QYSConst.DATA_TYPE_SELECT, R.drawable.selector_ic_game_select, new Point(ResourceUtil.getDimen(R.dimen.key_button_select_margin_left), ResourceUtil.getDimen(R.dimen.key_button_select_margin_top)), ResourceUtil.getDimen(R.dimen.key_button_small_size), ResourceUtil.getDimen(R.dimen.key_button_small_size)));
        this.mKeyViewInfos.add(new KeyViewInfo(108, "start", R.drawable.selector_ic_game_start, new Point(ResourceUtil.getDimen(R.dimen.key_button_start_margin_left), ResourceUtil.getDimen(R.dimen.key_button_start_margin_top)), ResourceUtil.getDimen(R.dimen.key_button_small_size), ResourceUtil.getDimen(R.dimen.key_button_small_size)));
        this.mKeyViewInfos.add(new KeyViewInfo(28, "clear", R.drawable.selector_ic_game_clear, new Point(ResourceUtil.getDimen(R.dimen.key_button_clear_margin_left), ResourceUtil.getDimen(R.dimen.key_button_clear_margin_top)), ResourceUtil.getDimen(R.dimen.key_button_small_size), ResourceUtil.getDimen(R.dimen.key_button_small_size)));
    }

    public List<KeyConfig> getDefaultKeyConfigList() {
        return this.mDefaultKeyConfig;
    }
}
