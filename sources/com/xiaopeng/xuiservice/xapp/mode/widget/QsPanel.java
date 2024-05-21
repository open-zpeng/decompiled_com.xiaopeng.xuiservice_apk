package com.xiaopeng.xuiservice.xapp.mode.widget;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.vcu.CarVcuManager;
import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.Settings;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.xiaopeng.app.xpPackageInfo;
import com.xiaopeng.xui.widget.XButton;
import com.xiaopeng.xui.widget.XLinearLayout;
import com.xiaopeng.xui.widget.XSwitch;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionWindowPos;
import com.xiaopeng.xuiservice.utils.PassengerBluetoothManager;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.utils.ToastUtil;
import com.xiaopeng.xuiservice.utils.UiHandler;
import com.xiaopeng.xuiservice.xapp.Config;
import com.xiaopeng.xuiservice.xapp.Constants;
import com.xiaopeng.xuiservice.xapp.Global;
import com.xiaopeng.xuiservice.xapp.brightness.DataRepository;
import com.xiaopeng.xuiservice.xapp.brightness.XpDisplayManager;
import com.xiaopeng.xuiservice.xapp.mode.XAppModeController;
import com.xiaopeng.xuiservice.xapp.mode.view.XAppModeViewManager;
import com.xiaopeng.xuiservice.xapp.mode.widget.QcSliderLayout;
import com.xiaopeng.xuiservice.xapp.sound.SoundManager;
import com.xiaopeng.xuiservice.xapp.temperature.TemperatureManager;
import com.xiaopeng.xuiservice.xapp.util.ResourceUtil;
/* loaded from: classes5.dex */
public class QsPanel extends XLinearLayout implements QcSliderLayout.OnSlideChangeListener, SoundManager.OnVolumeChangedListener, View.OnClickListener {
    private static final String TAG = "QsPanel";
    private String mAttachPkgName;
    private QcSliderLayout mBrightnessSlideLayout;
    CarVcuManager.CarVcuEventCallback mCarVcuEventCallback;
    private XButton mClearDataView;
    private ContentObserver mContentObserver;
    private TextView mCurrentTemperature;
    private DataRepository mDataRepository;
    private PassengerBluetoothManager.DeviceStatusListener mDeviceStatusListener;
    private int mDisplayId;
    private QcSliderLayout mEchoVolumeSlideLayout;
    private XSwitch mEnableGamePadSwitch;
    private View mEnableGamePadView;
    private ImageButton mGameDoubtBtn;
    private boolean mIsFullWindow;
    private View.OnTouchListener mNaviBarTouchListener;
    private SoundManager mSoundManager;
    private QcSliderLayout mSoundSlideLayout;
    private int mSoundStreamType;
    private int mTempSubTextSize;
    private int mTempSuffixSize;
    private TemperatureManager.Callback mTemperatureCallback;
    private TemperatureManager mTemperatureManager;
    private QcSliderLayout mTemperatureSlideLayout;
    private UserControlQsListener mUserControlQsListener;
    private WindowManager.LayoutParams mWindowLayoutParams;
    private WindowManager mWindowManager;
    private XpDisplayManager mXpDisplayManager;

    /* loaded from: classes5.dex */
    public interface UserControlQsListener {
        void onUserControlQS(int i);
    }

    public QsPanel(Context context) {
        this(context, null);
    }

    public QsPanel(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public QsPanel(Context context, AttributeSet attributeSet, int defStyle) {
        this(context, attributeSet, defStyle, 0);
    }

    public QsPanel(Context context, AttributeSet attributeSet, int defStyle, int defStyleRes) {
        super(context, attributeSet, defStyle, defStyleRes);
        this.mDataRepository = DataRepository.getInstance();
        this.mXpDisplayManager = XpDisplayManager.getInstance();
        this.mTemperatureManager = TemperatureManager.getInstance();
        this.mIsFullWindow = false;
        this.mNaviBarTouchListener = new View.OnTouchListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.widget.QsPanel.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View v, MotionEvent ev) {
                if (ev.getAction() == 4) {
                    String deviceName = ev.getDeviceName();
                    int touchDisplayId = QsPanel.this.getDisplayId(deviceName);
                    LogUtil.d(QsPanel.TAG, "deviceName: " + deviceName + " & touchDisplayId " + touchDisplayId + " mDisplayId " + QsPanel.this.mDisplayId);
                    if (QsPanel.this.mDisplayId == touchDisplayId) {
                        XAppModeViewManager.getInstance().addDelayDismissTask(QsPanel.this.mDisplayId);
                        return true;
                    }
                    return true;
                }
                return true;
            }
        };
        this.mContentObserver = new ContentObserver(new UiHandler()) { // from class: com.xiaopeng.xuiservice.xapp.mode.widget.QsPanel.3
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                if (Settings.System.getUriFor("screen_brightness").equals(uri)) {
                    QsPanel.this.mBrightnessSlideLayout.setProgress(QsPanel.this.getCurrentBrightnessState());
                } else if (Settings.System.getUriFor(Constants.KEY_AVAS_SPEAKER).equals(uri)) {
                    QsPanel.this.refreshSoundTypeAndUI();
                }
            }
        };
        this.mTemperatureCallback = new TemperatureManager.Callback() { // from class: com.xiaopeng.xuiservice.xapp.mode.widget.QsPanel.4
            @Override // com.xiaopeng.xuiservice.xapp.temperature.TemperatureManager.Callback
            public void onStateChanged(int state) {
                QsPanel.this.refreshTempertureUI(state);
            }
        };
        this.mDeviceStatusListener = new PassengerBluetoothManager.DeviceStatusListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.widget.QsPanel.6
            @Override // com.xiaopeng.xuiservice.utils.PassengerBluetoothManager.DeviceStatusListener
            public void onConnectStatusChanged(boolean connected) {
                QsPanel.this.mSoundSlideLayout.post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.widget.QsPanel.6.1
                    @Override // java.lang.Runnable
                    public void run() {
                        QsPanel.this.mSoundStreamType = QsPanel.this.getTargetStreamType();
                        int currentVolume = QsPanel.this.mSoundManager.getStreamVolume(QsPanel.this.mSoundStreamType);
                        LogUtil.d(QsPanel.TAG, "streamType:" + QsPanel.this.mSoundStreamType + " current volume:" + currentVolume);
                        QsPanel.this.mSoundSlideLayout.setProgress(currentVolume);
                    }
                });
            }
        };
        this.mCarVcuEventCallback = new CarVcuManager.CarVcuEventCallback() { // from class: com.xiaopeng.xuiservice.xapp.mode.widget.QsPanel.7
            public void onChangeEvent(CarPropertyValue value) {
                if (value.getPropertyId() == 557847056) {
                    int evState = ((Integer) value.getValue()).intValue();
                    QsPanel.this.refreshControlCheckBox(evState == 2);
                }
            }

            public void onErrorEvent(int propertyId, int zone) {
            }
        };
        init();
    }

    public void hideOrShow(xpPackageInfo packageInfo, int displayId, int x, int y) {
        if (isAttachedToWindow()) {
            this.mWindowManager.removeView(this);
            if (this.mDisplayId != displayId) {
                addToWindowAtPosition(packageInfo, displayId, x, y);
                return;
            }
            return;
        }
        addToWindowAtPosition(packageInfo, displayId, x, y);
    }

    public void addToWindowAtPosition(xpPackageInfo packageInfo, int displayId, int x, int y) {
        if (isAttachedToWindow()) {
            this.mWindowManager.removeViewImmediate(this);
        }
        this.mDisplayId = displayId;
        this.mAttachPkgName = packageInfo.packageName;
        WindowManager.LayoutParams layoutParams = this.mWindowLayoutParams;
        layoutParams.x = x;
        layoutParams.y = y;
        ensureLayoutParamsLegal(packageInfo);
        try {
            this.mWindowManager.addView(this, this.mWindowLayoutParams);
        } catch (Exception ex) {
            LogUtil.w(TAG, "addToWindowAtPosition error:" + ex.getMessage());
        }
    }

    private void ensureLayoutParamsLegal(xpPackageInfo packageInfo) {
        this.mIsFullWindow = xpPackageInfo.isFullscreen(packageInfo);
        Rect windowRect = this.mWindowManager.getActivityBounds(this.mAttachPkgName, this.mIsFullWindow);
        int naviBarHeight = ResourceUtil.getDimen(R.dimen.navi_bar_height);
        int naviWidthGap = ResourceUtil.getDimen(R.dimen.navi_bar_width_gap);
        int naviHeightGap = ResourceUtil.getDimen(R.dimen.navi_bar_height_gap);
        int panelWidth = ResourceUtil.getDimen(R.dimen.floating_dialog_width_small);
        this.mWindowLayoutParams.x -= naviWidthGap;
        if (getMeasuredHeight() == 0) {
            int h = View.MeasureSpec.makeMeasureSpec(-2, Integer.MIN_VALUE);
            int w = View.MeasureSpec.makeMeasureSpec(panelWidth, 1073741824);
            measure(w, h);
        }
        int h2 = windowRect.bottom;
        int bottomEdge = (h2 - getMeasuredHeight()) - naviHeightGap;
        if (this.mWindowLayoutParams.y > bottomEdge) {
            WindowManager.LayoutParams layoutParams = this.mWindowLayoutParams;
            layoutParams.y = ((layoutParams.y - naviBarHeight) - getMeasuredHeight()) - naviHeightGap;
        } else {
            this.mWindowLayoutParams.y += naviHeightGap;
        }
        if ((SharedDisplayManager.hasSharedDisplayFeature() && this.mDisplayId == 0) || !this.mIsFullWindow) {
            int viewWidth = ResourceUtil.getDimen(R.dimen.floating_dialog_width_small);
            int rightEdge = windowRect.right - viewWidth;
            if (this.mWindowLayoutParams.x > rightEdge) {
                this.mWindowLayoutParams.x = rightEdge;
            }
        }
    }

    public void removeFromWindow(int displayId) {
        if (isAttachedToWindow() && this.mDisplayId == displayId) {
            this.mWindowManager.removeView(this);
        }
    }

    private void init() {
        this.mWindowManager = (WindowManager) getContext().getSystemService(ConditionWindowPos.TYPE);
        initWindowLayoutParams();
        setOnTouchListener(this.mNaviBarTouchListener);
        this.mSoundManager = SoundManager.getInstance(getContext());
        this.mTempSubTextSize = getContext().getResources().getDimensionPixelSize(R.dimen.qc_temperature_text_size);
        this.mTempSuffixSize = getContext().getResources().getDimensionPixelSize(R.dimen.qc_temperature_text_size);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDisplayId(String deviceName) {
        if (deviceName.equals("xp_mt_ivi")) {
            return 0;
        }
        return deviceName.equals("xp_mt_psg") ? 1 : -1;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mEchoVolumeSlideLayout = (QcSliderLayout) findViewById(R.id.qs_panel_echo_volume);
        this.mEchoVolumeSlideLayout.setOnSlideChangeListener(this);
        this.mEchoVolumeSlideLayout.setMin(Config.ECHO_VOLUME_MIN);
        this.mEchoVolumeSlideLayout.setMax(Config.ECHO_VOLUME_MAX);
        this.mSoundSlideLayout = (QcSliderLayout) findViewById(R.id.qs_panel_sound);
        this.mSoundSlideLayout.setOnSlideChangeListener(this);
        this.mSoundSlideLayout.setMin(0);
        this.mSoundSlideLayout.setMax(this.mSoundManager.getStreamMaxVolume(3));
        this.mBrightnessSlideLayout = (QcSliderLayout) findViewById(R.id.qs_panel_brightness);
        this.mBrightnessSlideLayout.setOnSlideChangeListener(this);
        this.mBrightnessSlideLayout.setMin(1);
        this.mBrightnessSlideLayout.setMax(100);
        this.mTemperatureSlideLayout = (QcSliderLayout) findViewById(R.id.qs_panel_temperature);
        this.mTemperatureSlideLayout.setOnSlideChangeListener(this);
        int minTemperature = this.mTemperatureManager.getMinTemperature();
        int maxTemperature = this.mTemperatureManager.getMaxTemperature();
        this.mTemperatureSlideLayout.setMin(minTemperature * 10);
        this.mTemperatureSlideLayout.setMax(maxTemperature * 10);
        this.mCurrentTemperature = (TextView) findViewById(R.id.temperature_indicator1);
        this.mClearDataView = (XButton) findViewById(R.id.btn_clear_data);
        this.mClearDataView.setOnClickListener(this);
        this.mClearDataView.setVisibility(0);
        this.mEnableGamePadView = findViewById(R.id.ll_switch_str_wheel_control);
        this.mEnableGamePadView.setVisibility(Global.isGameModeEnable() ? 0 : 8);
        this.mEnableGamePadSwitch = (XSwitch) findViewById(R.id.switch_wheel_control);
        this.mEnableGamePadSwitch.setChecked(xpPackageInfo.isXpGameModeEnable());
        this.mEnableGamePadSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.widget.QsPanel.2
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LogUtil.d(QsPanel.TAG, "onCheckedChanged:" + isChecked);
                ToastUtil.showToast(QsPanel.this.getContext(), isChecked ? R.string.toast_wheel_control_open : R.string.toast_wheel_control_close, 0, QsPanel.this.mDisplayId);
                XAppModeController.getInstance().setWSControlStatus(isChecked);
            }
        });
        this.mGameDoubtBtn = (ImageButton) findViewById(R.id.btn_game_doubt);
        this.mGameDoubtBtn.setOnClickListener(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xui.widget.XLinearLayout, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        refreshSoundTypeAndUI();
        this.mBrightnessSlideLayout.setProgress(getCurrentBrightnessState());
        registerSoundCallback();
        registerScreenBrightness();
        registerAvasObserver();
        initTemperatureState();
        refreshControlCheckBox(isEvSysReady());
        addVCUEventListener();
        PassengerBluetoothManager.getInstance().registerListener(this.mDeviceStatusListener);
    }

    private void initWindowLayoutParams() {
        this.mWindowLayoutParams = new WindowManager.LayoutParams();
        WindowManager.LayoutParams layoutParams = this.mWindowLayoutParams;
        layoutParams.type = 2038;
        layoutParams.intentFlags = 32;
        layoutParams.format = 1;
        layoutParams.flags = 8;
        layoutParams.flags = 32 | layoutParams.flags;
        this.mWindowLayoutParams.flags |= 262144;
        WindowManager.LayoutParams layoutParams2 = this.mWindowLayoutParams;
        layoutParams2.gravity = 51;
        layoutParams2.width = -2;
        layoutParams2.height = -2;
    }

    private void initTemperatureState() {
        refreshTempertureUI(this.mTemperatureManager.getCurrentState());
        this.mTemperatureManager.setCallback(this.mTemperatureCallback);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xui.widget.XLinearLayout, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        XAppModeViewManager.getInstance().cancelDelayDismissTask(this.mDisplayId);
        unregisterSoundCallback();
        unRegisterScreenBrightness();
        removeVCUEventListener();
        PassengerBluetoothManager.getInstance().unregisterListener(this.mDeviceStatusListener);
    }

    @Override // com.xiaopeng.xuiservice.xapp.mode.widget.QcSliderLayout.OnSlideChangeListener
    public void onProgressChanged(QcSliderLayout qcSliderLayout, int progress) {
        if (qcSliderLayout == this.mSoundSlideLayout) {
            LogUtil.d(TAG, "mSoundSlideLayout");
            this.mSoundManager.setStreamVolume(this.mSoundStreamType, progress, 4);
        } else if (qcSliderLayout == this.mBrightnessSlideLayout) {
            LogUtil.d(TAG, "mBrightnessSlideLayout");
            int value = this.mXpDisplayManager.getRealBrightnessByUI(progress);
            this.mDataRepository.setScreenBrightness(getContext(), value, true, this.mDisplayId);
        } else if (qcSliderLayout == this.mTemperatureSlideLayout) {
            this.mCurrentTemperature.setText(getTemperature(progress));
        }
    }

    @Override // com.xiaopeng.xuiservice.xapp.mode.widget.QcSliderLayout.OnSlideChangeListener
    public void onStartTrackingTouch(QcSliderLayout qcSliderLayout) {
        if (qcSliderLayout == this.mSoundSlideLayout) {
            unregisterSoundCallback();
        } else if (qcSliderLayout == this.mBrightnessSlideLayout) {
            unRegisterScreenBrightness();
        }
    }

    @Override // com.xiaopeng.xuiservice.xapp.mode.widget.QcSliderLayout.OnSlideChangeListener
    public void onStopTrackingTouch(QcSliderLayout qcSliderLayout) {
        if (qcSliderLayout == this.mTemperatureSlideLayout) {
            this.mTemperatureManager.setProgress(qcSliderLayout.getProgress());
        } else if (qcSliderLayout == this.mSoundSlideLayout) {
            registerSoundCallback();
        } else if (qcSliderLayout == this.mBrightnessSlideLayout) {
            registerScreenBrightness();
        }
    }

    @Override // com.xiaopeng.xuiservice.xapp.sound.SoundManager.OnVolumeChangedListener
    public void onVolumeChanged(int streamType, int volume, int preVolume) {
        LogUtil.d(TAG, "onVolumeChanged");
        QcSliderLayout qcSliderLayout = this.mSoundSlideLayout;
        if (qcSliderLayout != null && streamType == this.mSoundStreamType) {
            qcSliderLayout.setProgress(volume);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent ev) {
        UserControlQsListener userControlQsListener;
        if (ev.getAction() != 4 && (userControlQsListener = this.mUserControlQsListener) != null) {
            userControlQsListener.onUserControlQS(this.mDisplayId);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setUserControlQSListener(UserControlQsListener listener) {
        this.mUserControlQsListener = listener;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        UserControlQsListener userControlQsListener;
        if (ev.getAction() != 4 && (userControlQsListener = this.mUserControlQsListener) != null) {
            userControlQsListener.onUserControlQS(this.mDisplayId);
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void registerScreenBrightness() {
        getContext().getContentResolver().registerContentObserver(Settings.System.getUriFor(this.mDataRepository.getBrightnessKey(this.mDisplayId)), true, this.mContentObserver);
    }

    private void registerAvasObserver() {
        getContext().getContentResolver().registerContentObserver(Settings.System.getUriFor(Constants.KEY_AVAS_SPEAKER), true, this.mContentObserver);
    }

    private void unRegisterScreenBrightness() {
        getContext().getContentResolver().unregisterContentObserver(this.mContentObserver);
    }

    private void registerSoundCallback() {
        this.mSoundManager.addOnVolumeChangedListener(this);
    }

    private void unregisterSoundCallback() {
        this.mSoundManager.removeOnVolumeChangedListener(this);
    }

    public int getCurrentBrightnessState() {
        int brightness = this.mXpDisplayManager.getUIProgressByReal(this.mDataRepository.getScreenBrightness(getContext(), this.mDisplayId));
        return brightness;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshSoundTypeAndUI() {
        this.mSoundStreamType = getTargetStreamType();
        ImageView soundImg = (ImageView) this.mSoundSlideLayout.findViewById(R.id.volume_indicator1);
        TextView soundText = (TextView) this.mSoundSlideLayout.findViewById(R.id.volume_indicator2);
        int i = this.mSoundStreamType;
        if (i == 11) {
            soundImg.setImageResource(R.drawable.ic_mid_externalvolume);
            soundText.setText(R.string.media_external_volume);
        } else if (i == 13) {
            soundImg.setImageResource(R.drawable.ic_mid_earphone);
            soundText.setText(R.string.media_earphone_volume);
        } else {
            soundImg.setImageResource(R.drawable.ic_mid_music);
            soundText.setText(R.string.media_volume);
        }
        int currentVolume = this.mSoundManager.getStreamVolume(this.mSoundStreamType);
        LogUtil.d(TAG, "streamType:" + this.mSoundStreamType + " current volume:" + currentVolume);
        this.mSoundSlideLayout.setProgress(currentVolume);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshTempertureUI(int state) {
        int realTemperature = this.mTemperatureManager.getConvertValue(this.mTemperatureSlideLayout.getProgress());
        LogUtil.d(TAG, "quickmenu refreshTempertureUI realTemperature:" + realTemperature + " " + state);
        if (realTemperature != state) {
            this.mTemperatureSlideLayout.setProgress(state);
        }
        this.mCurrentTemperature.setText(getTemperature(state));
    }

    private SpannableString getTemperature(int temperature) {
        if (temperature == -1) {
            return new SpannableString("");
        }
        String origin = (this.mTemperatureManager.getConvertValue(temperature) / 10.0f) + "";
        SpannableString ss = new SpannableString(origin);
        return ss;
    }

    private void addVCUEventListener() {
        CarClientManager.getInstance().addVcuManagerListener(this.mCarVcuEventCallback);
    }

    private void removeVCUEventListener() {
        CarClientManager.getInstance().removeVcuManagerListener(this.mCarVcuEventCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshControlCheckBox(final boolean isEvReady) {
        this.mEnableGamePadSwitch.post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.mode.widget.QsPanel.5
            @Override // java.lang.Runnable
            public void run() {
                QsPanel.this.mEnableGamePadSwitch.setChecked(XAppModeController.getInstance().isWSControlEnable());
                QsPanel.this.mEnableGamePadSwitch.setEnabled(isEvReady);
            }
        });
    }

    private boolean isEvSysReady() {
        boolean ready = false;
        try {
            CarVcuManager carVcuManager = CarClientManager.getInstance().getCarManager("xp_vcu");
            if (carVcuManager != null) {
                ready = carVcuManager.getEvSysReady() == 2;
            }
        } catch (Exception e) {
        }
        LogUtil.d(TAG, "isEvSysReady:" + ready);
        return ready;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_clear_data) {
            removeFromWindow(this.mDisplayId);
            XAppModeViewManager.getInstance().showClearAppDialog(this.mDisplayId, this.mAttachPkgName);
        } else if (id == R.id.btn_game_doubt) {
            removeFromWindow(this.mDisplayId);
            XAppModeViewManager.getInstance().showGameDetail();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getTargetStreamType() {
        if (this.mDisplayId == 1 && PassengerBluetoothManager.getInstance().isDeviceConnected()) {
            return 13;
        }
        if (!isAvasEnable()) {
            return 3;
        }
        return 11;
    }

    private boolean isAvasEnable() {
        return Settings.System.getInt(getContext().getContentResolver(), Constants.KEY_AVAS_SPEAKER, 0) == 1;
    }
}
