package com.xiaopeng.xuiservice.xapp.mode.octopus.widget;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import com.xiaopeng.app.xpPackageInfo;
import com.xiaopeng.xui.app.XToast;
import com.xiaopeng.xui.widget.XExposedDropdownMenu;
import com.xiaopeng.xui.widget.XRelativeLayout;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.xapp.mode.octopus.Constants;
import com.xiaopeng.xuiservice.xapp.mode.octopus.EventProcessor;
import com.xiaopeng.xuiservice.xapp.mode.octopus.IConfigViewRoot;
import com.xiaopeng.xuiservice.xapp.mode.octopus.bean.KeyMapInfo;
import com.xiaopeng.xuiservice.xapp.mode.octopus.bean.KeyMapInfos;
import com.xiaopeng.xuiservice.xapp.mode.octopus.bean.KeyViewBasicInfo;
import com.xiaopeng.xuiservice.xapp.mode.octopus.bean.KeyViewInfo;
import com.xiaopeng.xuiservice.xapp.mode.octopus.db.KeyConfig;
import com.xiaopeng.xuiservice.xapp.util.GsonUtil;
import com.xiaopeng.xuiservice.xapp.util.ResourceUtil;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class KeyConfigViewGroup extends XRelativeLayout implements IConfigViewRoot {
    private static final String TAG = "KeyConfigViewGroup";
    public static final int VIEW_CONFIG_CLEAR = 2;
    public static final int VIEW_CONFIG_CLOSE = 0;
    public static final int VIEW_CONFIG_COLLAPSE = 3;
    public static final int VIEW_CONFIG_EXPAND = 4;
    public static final int VIEW_CONFIG_SAVE = 1;
    private View mClearBtn;
    private View mCollapseBtn;
    private EventCallback mEventCallback;
    private View mExitBtn;
    private boolean mIsConfigMode;
    private View mKeyBgView;
    private List<KeyViewInfo> mKeyViewInfos;
    private Rect mKeyViewRect;
    private List<KeyButtonView> mKeyViews;
    private View.OnClickListener mOnClickListener;
    private String mPackageName;
    private Rect mRemoveRect;
    private View mRemoveView;
    private View mSaveBtn;
    private XExposedDropdownMenu mSelectConfigDropMenu;
    private String mSelectConfigName;
    private xpPackageInfo mXpPackageInfo;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes5.dex */
    public @interface ConfigViewButton {
    }

    /* loaded from: classes5.dex */
    public interface EventCallback {
        void onButtonClick(int i);
    }

    public KeyConfigViewGroup(Context context) {
        this(context, null);
    }

    public KeyConfigViewGroup(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public KeyConfigViewGroup(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        this.mRemoveRect = new Rect();
        this.mKeyViewRect = new Rect();
        this.mOnClickListener = new View.OnClickListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.octopus.widget.KeyConfigViewGroup.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_clear_config /* 2131296362 */:
                        XToast.show((int) R.string.toast_clear_config);
                        return;
                    case R.id.btn_close_config /* 2131296364 */:
                        XToast.show((int) R.string.toast_close_config);
                        if (KeyConfigViewGroup.this.mEventCallback != null) {
                            KeyConfigViewGroup.this.mEventCallback.onButtonClick(0);
                            return;
                        }
                        return;
                    case R.id.btn_collapse_config /* 2131296365 */:
                        XToast.show((int) R.string.toast_collapse_config);
                        KeyConfigViewGroup.this.hideOrShowKeyButtonViews();
                        return;
                    case R.id.btn_save_config /* 2131296370 */:
                        XToast.show((int) R.string.toast_save_config);
                        KeyConfigViewGroup.this.saveKeyConfig();
                        if (KeyConfigViewGroup.this.mEventCallback != null) {
                            KeyConfigViewGroup.this.mEventCallback.onButtonClick(1);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        };
        init();
    }

    public void setEventCallback(EventCallback eventCallback) {
        this.mEventCallback = eventCallback;
    }

    private void init() {
        this.mKeyViews = new ArrayList();
    }

    public void setupWithPackageInfo(String pkgName, xpPackageInfo info) {
        this.mPackageName = pkgName;
        this.mXpPackageInfo = info;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        initViews();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xui.widget.XRelativeLayout, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        addKeyViews();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addKeyViews() {
        this.mSelectConfigName = this.mSelectConfigDropMenu.getSelectionTitle();
        LogUtil.d(TAG, "addKeyViews mSelectConfigName:" + this.mSelectConfigName);
        initSelectConfig(this.mSelectConfigName);
        removeKeyViews();
        List<KeyViewInfo> list = this.mKeyViewInfos;
        if (list == null || list.size() == 0) {
            return;
        }
        for (KeyViewInfo keyViewInfo : this.mKeyViewInfos) {
            KeyButtonView keyView = new KeyButtonView(getContext());
            keyView.setKeyViewInfo(keyViewInfo);
            keyView.setConfigViewRoot(this);
            keyView.setImageResource(keyViewInfo.mKeyImgResourceId);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(keyViewInfo.mWidth, keyViewInfo.mHeight);
            params.leftMargin = keyViewInfo.mPoint.x - (keyViewInfo.mWidth / 2);
            params.topMargin = keyViewInfo.mPoint.y - (keyViewInfo.mHeight / 2);
            this.mKeyViews.add(keyView);
            addView(keyView, params);
        }
    }

    private void initSelectConfig(String configName) {
        List<KeyConfig> currentKeyConfigs = EventProcessor.getInstance().getCurrentKeyConfigs();
        if (currentKeyConfigs != null && currentKeyConfigs.size() > 0) {
            for (KeyConfig keyConfig : currentKeyConfigs) {
                LogUtil.d(TAG, "key configs :" + keyConfig.toString());
                if (keyConfig.configName.equals(configName)) {
                    this.mKeyViewInfos = getKeyViewInfosFromConfig(keyConfig);
                    return;
                }
            }
        }
    }

    private List<KeyViewInfo> getKeyViewInfosFromConfig(KeyConfig keyConfig) {
        LogUtil.d(TAG, "getKeyViewInfosFromConfig:" + keyConfig.configInfo);
        KeyMapInfos keyMapInfos = convertKeyConfigToKeyMap(keyConfig);
        List<KeyViewInfo> keyViewInfoList = new ArrayList<>();
        KeyViewBasicInfo naviKeyBasicInfo = ResourceUtil.getViewInfoByKeyCode(Constants.SpecialKeyEvent.KEYCODE_LEFT_NAVI);
        KeyViewInfo navikeyViewInfo = new KeyViewInfo(Constants.SpecialKeyEvent.KEYCODE_LEFT_NAVI, "l", naviKeyBasicInfo.getResId(), new Point(keyMapInfos.getTouchRect().left, keyMapInfos.getTouchRect().top), naviKeyBasicInfo.getWidth(), naviKeyBasicInfo.getHeight());
        keyViewInfoList.add(navikeyViewInfo);
        for (KeyMapInfo keyMapInfo : keyMapInfos.getKeyMap()) {
            KeyViewBasicInfo tempBasicInfo = ResourceUtil.getViewInfoByKeyCode(keyMapInfo.keycode);
            if (tempBasicInfo != null) {
                keyViewInfoList.add(new KeyViewInfo(keyMapInfo.keycode, tempBasicInfo.getDesc(), tempBasicInfo.getResId(), new Point(keyMapInfo.x, keyMapInfo.y), tempBasicInfo.getWidth(), tempBasicInfo.getHeight()));
            }
        }
        return keyViewInfoList;
    }

    private KeyMapInfos convertKeyConfigToKeyMap(KeyConfig keyConfig) {
        KeyMapInfos keyMapInfos = new KeyMapInfos();
        keyMapInfos.setConfigName(keyConfig.configName);
        if (!TextUtils.isEmpty(keyConfig.configInfo)) {
            try {
                JSONObject jsonObject = new JSONObject(keyConfig.configInfo);
                JSONObject rectJsonObject = jsonObject.optJSONObject("touchRect");
                Rect naviRect = new Rect(rectJsonObject.optInt("left"), rectJsonObject.optInt("top"), rectJsonObject.optInt("right"), rectJsonObject.optInt("bottom"));
                keyMapInfos.setTouchRect(naviRect);
                JSONArray keyMapArray = jsonObject.getJSONArray("keyMap");
                List<KeyMapInfo> keyMapList = new ArrayList<>();
                for (int i = 0; i < keyMapArray.length(); i++) {
                    JSONObject keyValue = keyMapArray.getJSONObject(i);
                    int keycode = keyValue.optInt("keycode");
                    String desc = keyValue.optString("desc");
                    int x = keyValue.optInt("x");
                    int y = keyValue.optInt("y");
                    KeyMapInfo keyMapInfo = new KeyMapInfo();
                    keyMapInfo.keycode = keycode;
                    keyMapInfo.desc = desc;
                    keyMapInfo.x = x;
                    keyMapInfo.y = y;
                    keyMapList.add(keyMapInfo);
                }
                keyMapInfos.setKeyMap(keyMapList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return keyMapInfos;
    }

    private void removeKeyViews() {
        List<KeyButtonView> list = this.mKeyViews;
        if (list != null && list.size() > 0) {
            for (KeyButtonView keyButtonView : this.mKeyViews) {
                removeView(keyButtonView);
            }
        }
        this.mKeyViews.clear();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xui.widget.XRelativeLayout, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void initViews() {
        this.mExitBtn = findViewById(R.id.btn_close_config);
        this.mSaveBtn = findViewById(R.id.btn_save_config);
        this.mClearBtn = findViewById(R.id.btn_clear_config);
        this.mCollapseBtn = findViewById(R.id.btn_collapse_config);
        this.mKeyBgView = findViewById(R.id.img_key_map_bg);
        this.mSelectConfigDropMenu = (XExposedDropdownMenu) findViewById(R.id.dropdown_menu_config);
        this.mRemoveView = findViewById(R.id.btn_remove_config);
        this.mExitBtn.setOnClickListener(this.mOnClickListener);
        this.mSaveBtn.setOnClickListener(this.mOnClickListener);
        this.mClearBtn.setOnClickListener(this.mOnClickListener);
        this.mCollapseBtn.setOnClickListener(this.mOnClickListener);
        this.mSelectConfigDropMenu.setOnItemClickListener(new XExposedDropdownMenu.OnItemSelectedListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.octopus.widget.KeyConfigViewGroup.1
            @Override // com.xiaopeng.xui.widget.XExposedDropdownMenu.OnItemSelectedListener
            public void onItemSelected(int i, String s) {
                KeyConfigViewGroup.this.mSelectConfigName = s;
                XToast.show(s);
                KeyConfigViewGroup.this.addKeyViews();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideOrShowKeyButtonViews() {
        if (this.mKeyBgView.getVisibility() == 0) {
            hideKeyButtonViews();
        } else {
            showKeyButtonViews();
        }
    }

    private void hideKeyButtonViews() {
        for (View keyButton : this.mKeyViews) {
            keyButton.setVisibility(8);
        }
        this.mKeyBgView.setVisibility(8);
        EventCallback eventCallback = this.mEventCallback;
        if (eventCallback != null) {
            eventCallback.onButtonClick(3);
        }
    }

    private void showKeyButtonViews() {
        EventCallback eventCallback = this.mEventCallback;
        if (eventCallback != null) {
            eventCallback.onButtonClick(4);
        }
        this.mKeyBgView.setVisibility(0);
        for (View keyButton : this.mKeyViews) {
            keyButton.setVisibility(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveKeyConfig() {
        KeyMapInfos keyMapInfos = new KeyMapInfos();
        LogUtil.d(TAG, "current selection index :" + this.mSelectConfigDropMenu.getSelection());
        XExposedDropdownMenu xExposedDropdownMenu = this.mSelectConfigDropMenu;
        keyMapInfos.setConfigName(xExposedDropdownMenu.getTitleWithIndex(xExposedDropdownMenu.getSelection()));
        List<KeyMapInfo> keyMapArrays = new ArrayList<>();
        for (KeyButtonView keyButtonView : this.mKeyViews) {
            KeyViewInfo keyViewInfo = keyButtonView.getKeyViewInfo();
            if (keyViewInfo.mKeyCode == 8001) {
                Rect rect = new Rect();
                keyButtonView.getGlobalVisibleRect(rect);
                keyMapInfos.setTouchRect(rect);
            } else if (keyViewInfo.mKeyCode != 8002) {
                KeyMapInfo keyMapInfo = new KeyMapInfo();
                keyMapInfo.keycode = keyViewInfo.mKeyCode;
                keyMapInfo.desc = keyViewInfo.mKeyCodeIndexName;
                keyMapInfo.x = keyViewInfo.mPoint.x + (keyButtonView.getMeasuredWidth() / 2);
                keyMapInfo.y = keyViewInfo.mPoint.y + (keyButtonView.getMeasuredHeight() / 2);
                keyMapArrays.add(keyMapInfo);
            }
        }
        keyMapInfos.setKeyMap(keyMapArrays);
        KeyConfig keyConfig = new KeyConfig();
        keyConfig.configName = keyMapInfos.getConfigName();
        keyConfig.selected = 1;
        keyConfig.pkgName = this.mPackageName;
        keyConfig.configInfo = GsonUtil.toJson(keyMapInfos);
        LogUtil.d(TAG, "saveKeyConfig configInfo :" + keyConfig.configInfo);
        EventProcessor.getInstance().onUserUpdateConfig(keyConfig);
    }

    public synchronized void enterConfigMode(KeyButtonView moveView) {
        if (!this.mIsConfigMode) {
            this.mIsConfigMode = true;
            this.mRemoveView.setVisibility(0);
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (child != this.mRemoveView && child != moveView) {
                    child.setVisibility(4);
                }
            }
        }
    }

    public synchronized void exitConfigMode(KeyButtonView moveView) {
        if (this.mIsConfigMode) {
            this.mIsConfigMode = false;
            this.mRemoveView.setVisibility(4);
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (child != this.mRemoveView && child != moveView) {
                    child.setVisibility(0);
                }
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.xapp.mode.octopus.IConfigViewRoot
    public void onKeyViewTouchEvent(KeyButtonView moveView, MotionEvent event) {
        int action = event.getAction();
        if (action != 1) {
            if (action == 2) {
                enterConfigMode(moveView);
                refreshRemoveViewPressed(moveView);
                return;
            } else if (action != 3) {
                return;
            }
        }
        exitConfigMode(moveView);
        resetKeyView(moveView);
    }

    private void refreshRemoveViewPressed(View child) {
        this.mRemoveView.setPressed(checkRemoveHit(child));
    }

    private void resetKeyView(KeyButtonView keyChild) {
        boolean isHit = checkRemoveHit(keyChild);
        if (isHit) {
            keyChild.resetLayoutPosition();
        }
    }

    private boolean checkRemoveHit(View child) {
        child.getGlobalVisibleRect(this.mKeyViewRect);
        this.mRemoveView.getGlobalVisibleRect(this.mRemoveRect);
        return this.mKeyViewRect.intersect(this.mRemoveRect);
    }
}
