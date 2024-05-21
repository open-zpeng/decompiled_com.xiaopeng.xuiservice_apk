package com.xiaopeng.xuiservice.xapp.mode.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.xiaopeng.app.xpPackageInfo;
import com.xiaopeng.xui.widget.XRelativeLayout;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionWindowPos;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.utils.SharedPreferencesUtil;
import com.xiaopeng.xuiservice.xapp.Config;
import com.xiaopeng.xuiservice.xapp.Global;
import com.xiaopeng.xuiservice.xapp.mode.XAppModeController;
import com.xiaopeng.xuiservice.xapp.mode.view.XAppModeViewManager;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes5.dex */
public class NavigationBar extends XRelativeLayout implements View.OnClickListener {
    public static final int COLLAPSE_STATE_BOTTOM = 4;
    public static final int COLLAPSE_STATE_INVALID = -1;
    public static final int COLLAPSE_STATE_LEFT = 2;
    public static final int COLLAPSE_STATE_RIGHT = 3;
    public static final int COLLAPSE_STATE_TOP = 1;
    private static final int MSG_FADE_OUT = 1;
    private static final String NAVI_GUIDANCE_MODE = "com.xiaopeng.xuiservice";
    public static final int STATE_COLLAPSE = 1;
    public static final int STATE_EXPAND = 2;
    private static final int SUB_MODE_BACK = 1;
    private static final int SUB_MODE_MENU = 2;
    private static final int SUB_MODE_NONE = 0;
    private static final String TAG = "NavigationBar";
    private int EXPAND_VIEW_HEIGHT;
    private int EXPAND_VIEW_WIDTH;
    private int NAVI_HEIGHT;
    private int VERTICAL_EXPAND_VIEW_HEIGHT;
    private int VERTICAL_EXPAND_VIEW_WIDTH;
    private ImageButton mBack;
    private int mCollapseState;
    private View mCollapseView;
    private View mControlView;
    private int mDisplayId;
    private int mDownX;
    private int mDownY;
    private ImageButton mExit;
    private int mExpandTouchArea;
    private ImageView mExpandView;
    private Handler mHandler;
    private boolean mIsFullWindow;
    private boolean mIsGuidanceMode;
    private View mJoystickBtn;
    private int mLastPosX;
    private int mLastPosY;
    private float mLastTouchX;
    private float mLastTouchY;
    private ImageButton mMore;
    private View.OnTouchListener mNaviBarTouchListener;
    private OnControlNaviBarListener mOnControlListener;
    private ViewTreeObserver.OnPreDrawListener mPreDrawListener;
    private ImageButton mSharedDisplay;
    private ImageButton mSoundEffect;
    private int mState;
    private float mTouchSlop;
    private WindowManager.LayoutParams mWindowLayoutParams;
    private WindowManager mWindowManager;
    private Rect mWindowRect;
    private XAppModeController mXAppModeController;
    private XAppModeViewManager mXAppModeViewManager;
    private xpPackageInfo mXpPackageInfo;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes5.dex */
    public @interface COLLAPSE_STATE {
    }

    /* loaded from: classes5.dex */
    public interface OnControlNaviBarListener {
        void onUserTouchedNaviBar();
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes5.dex */
    public @interface STATE {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void autoCollapseView() {
        int currentState = getCollapseState();
        if (currentState != -1) {
            setState(1);
            addTouchArea();
        }
    }

    public NavigationBar(Context context) {
        this(context, null);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mIsFullWindow = false;
        this.mDownX = -1;
        this.mDownY = -1;
        this.mState = 1;
        this.mCollapseState = -1;
        this.mHandler = new Handler(Looper.myLooper()) { // from class: com.xiaopeng.xuiservice.xapp.mode.widget.NavigationBar.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (msg.what == 1 && !NavigationBar.this.mIsGuidanceMode) {
                    NavigationBar.this.setAlpha(0.5f);
                    XAppModeViewManager.getInstance().hideQsPanel(NavigationBar.this.mDisplayId);
                    NavigationBar.this.autoCollapseView();
                }
            }
        };
        this.mNaviBarTouchListener = new View.OnTouchListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.widget.NavigationBar.2
            /* JADX WARN: Code restructure failed: missing block: B:9:0x0036, code lost:
                if (r0 != 3) goto L9;
             */
            @Override // android.view.View.OnTouchListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public boolean onTouch(android.view.View r8, android.view.MotionEvent r9) {
                /*
                    Method dump skipped, instructions count: 301
                    To view this dump add '--comments-level debug' option
                */
                throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xuiservice.xapp.mode.widget.NavigationBar.AnonymousClass2.onTouch(android.view.View, android.view.MotionEvent):boolean");
            }
        };
        this.mPreDrawListener = new ViewTreeObserver.OnPreDrawListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.widget.NavigationBar.3
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                NavigationBar.this.mWindowLayoutParams.y = NavigationBar.this.mWindowRect.top;
                NavigationBar.this.mWindowLayoutParams.x = NavigationBar.this.mWindowRect.centerX() - (NavigationBar.this.getWidth() / 2);
                NavigationBar.this.updatePosition();
                NavigationBar.this.removePreDrawListener();
                return true;
            }
        };
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.NAVI_HEIGHT = context.getResources().getDimensionPixelSize(R.dimen.navi_bar_height);
        this.mExpandTouchArea = context.getResources().getDimensionPixelSize(R.dimen.navi_bar_expand_touch);
        this.EXPAND_VIEW_WIDTH = context.getResources().getDimensionPixelSize(R.dimen.navi_bar_hor_expand_width);
        this.EXPAND_VIEW_HEIGHT = context.getResources().getDimensionPixelSize(R.dimen.navi_bar_hor_expand_height);
        this.VERTICAL_EXPAND_VIEW_WIDTH = context.getResources().getDimensionPixelSize(R.dimen.navi_bar_vertical_expand_width);
        this.VERTICAL_EXPAND_VIEW_HEIGHT = context.getResources().getDimensionPixelSize(R.dimen.navi_bar_vertical_expand_height);
        this.mIsGuidanceMode = SharedPreferencesUtil.getInstance().getBoolean(NAVI_GUIDANCE_MODE, true);
        LogUtil.d(TAG, "mTouchSlop value:" + this.mTouchSlop);
        init();
    }

    public void setDisplayId(int displayId) {
        this.mDisplayId = displayId;
    }

    public void updatePackageInfo(xpPackageInfo packageInfo) {
        this.mXpPackageInfo = packageInfo;
        updateWindowRect();
        updateWindowParams();
        boolean isMenuShow = this.mXpPackageInfo.navigationKey == 2;
        this.mMore.setVisibility(isMenuShow ? 0 : 8);
        this.mJoystickBtn.setVisibility(shouldShowJoystickIcon() ? 0 : 8);
        refreshSharedDisplayButton();
        refreshSoundEffectIcon();
    }

    public void addToWindow() {
        LogUtil.d(TAG, "addToWindow displayId:" + this.mDisplayId);
        if (getVisibility() != 0) {
            setVisibility(0);
            if (isAttachedToWindow()) {
                WindowManager.LayoutParams layoutParams = this.mWindowLayoutParams;
                layoutParams.x = this.mLastPosX;
                layoutParams.y = this.mLastPosY;
                updatePosition();
            }
        }
        if (!isAttachedToWindow()) {
            try {
                this.mWindowLayoutParams.x = this.mLastPosX;
                this.mWindowLayoutParams.y = this.mLastPosY;
                this.mWindowManager.addView(this, this.mWindowLayoutParams);
            } catch (IllegalStateException e) {
                LogUtil.w(TAG, "addToWindow exception:" + e.getMessage());
            }
        }
        isOutOfBounds();
    }

    public void removeFromWindow() {
        LogUtil.d(TAG, "removeFromWindow displayId:" + this.mDisplayId);
        if (isAttachedToWindow()) {
            cachePosition();
            saveGuidanceMode();
            this.mWindowManager.removeViewImmediate(this);
        }
    }

    public void setOnControlListener(OnControlNaviBarListener listener) {
        this.mOnControlListener = listener;
    }

    public void setState(int state) {
        this.mState = state;
        int i = this.mState;
        if (i == 1) {
            this.mControlView.setVisibility(4);
            this.mControlView.setEnabled(false);
            refreshExpandViewStyle();
            this.mControlView.setVisibility(8);
            this.mExpandView.setVisibility(0);
            this.mExpandView.setEnabled(true);
        } else if (i == 2) {
            this.mExpandView.setVisibility(8);
            this.mExpandView.setEnabled(false);
            if (this.mWindowLayoutParams.x >= this.mWindowRect.right - this.mControlView.getWidth()) {
                this.mWindowLayoutParams.x = this.mWindowRect.right - this.mControlView.getWidth();
                this.mWindowManager.updateViewLayout(this, this.mWindowLayoutParams);
            }
            if (this.mWindowLayoutParams.y >= this.mWindowRect.bottom - this.mControlView.getHeight()) {
                this.mWindowLayoutParams.y = this.mWindowRect.bottom - this.mControlView.getHeight();
                this.mWindowManager.updateViewLayout(this, this.mWindowLayoutParams);
            }
            this.mControlView.setVisibility(0);
            this.mControlView.setEnabled(true);
        }
    }

    private void addTouchArea() {
        restoreViewTouchDelegate(this.mExpandView);
        int collapseState = getCollapseState();
        if (collapseState == 1) {
            expandViewTouchDelegate(this.mExpandView, 0, this.mExpandTouchArea, 0, 0);
        } else if (collapseState == 2) {
            expandViewTouchDelegate(this.mExpandView, 0, 0, 0, this.mExpandTouchArea);
        } else if (collapseState == 3) {
            expandViewTouchDelegate(this.mExpandView, 0, 0, this.mExpandTouchArea, 0);
        } else if (collapseState == 4) {
            expandViewTouchDelegate(this.mExpandView, this.mExpandTouchArea, 0, 0, 0);
        }
    }

    private void expandViewTouchDelegate(View view, int top, int bottom, int left, int right) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        layoutParams.setMargins(left, top, right, bottom);
        view.setLayoutParams(layoutParams);
        Rect bounds = new Rect();
        view.setEnabled(true);
        view.getHitRect(bounds);
        bounds.top -= top;
        bounds.bottom += bottom;
        bounds.left -= left;
        bounds.right += right;
        TouchDelegate touchDelegate = new TouchDelegate(bounds, view);
        if (View.class.isInstance(view.getParent())) {
            ((View) view.getParent()).setTouchDelegate(touchDelegate);
        }
    }

    private void restoreViewTouchDelegate(View view) {
        Rect bounds = new Rect();
        bounds.setEmpty();
        TouchDelegate touchDelegate = new TouchDelegate(bounds, view);
        if (View.class.isInstance(view.getParent())) {
            ((View) view.getParent()).setTouchDelegate(touchDelegate);
        }
    }

    private void refreshExpandViewStyle() {
        int currentState = getCollapseState();
        refreshExpandViewStyle(currentState);
    }

    private void refreshExpandViewStyle(int state) {
        if (state == 1) {
            ViewGroup.LayoutParams layoutParams = this.mExpandView.getLayoutParams();
            if (layoutParams.width != this.EXPAND_VIEW_WIDTH) {
                int i = layoutParams.height;
                int i2 = this.EXPAND_VIEW_HEIGHT;
                if (i != i2) {
                    layoutParams.width = this.EXPAND_VIEW_WIDTH;
                    layoutParams.height = i2;
                    this.mExpandView.setLayoutParams(layoutParams);
                }
            }
            this.mExpandView.setBackgroundResource(R.drawable.bg_navi_bar_expand);
            this.mExpandView.setImageResource(R.drawable.ic_expand);
        } else if (state == 2) {
            ViewGroup.LayoutParams layoutParams2 = this.mExpandView.getLayoutParams();
            if (layoutParams2.width != this.VERTICAL_EXPAND_VIEW_WIDTH) {
                int i3 = layoutParams2.height;
                int i4 = this.VERTICAL_EXPAND_VIEW_HEIGHT;
                if (i3 != i4) {
                    layoutParams2.width = this.VERTICAL_EXPAND_VIEW_WIDTH;
                    layoutParams2.height = i4;
                    this.mExpandView.setLayoutParams(layoutParams2);
                }
            }
            this.mExpandView.setBackgroundResource(R.drawable.bg_navi_bar_expand_left);
            this.mExpandView.setImageResource(R.drawable.ic_expand_left);
        } else if (state == 3) {
            ViewGroup.LayoutParams layoutParams3 = this.mExpandView.getLayoutParams();
            if (layoutParams3.width != this.VERTICAL_EXPAND_VIEW_WIDTH) {
                int i5 = layoutParams3.height;
                int i6 = this.VERTICAL_EXPAND_VIEW_HEIGHT;
                if (i5 != i6) {
                    layoutParams3.width = this.VERTICAL_EXPAND_VIEW_WIDTH;
                    layoutParams3.height = i6;
                    this.mExpandView.setLayoutParams(layoutParams3);
                }
            }
            this.mExpandView.setBackgroundResource(R.drawable.bg_navi_bar_expand_right);
            this.mExpandView.setImageResource(R.drawable.ic_expand_right);
            this.mWindowLayoutParams.x = (this.mWindowRect.right - this.VERTICAL_EXPAND_VIEW_WIDTH) - this.mExpandTouchArea;
            this.mWindowManager.updateViewLayout(this, this.mWindowLayoutParams);
        } else if (state == 4) {
            ViewGroup.LayoutParams layoutParams4 = this.mExpandView.getLayoutParams();
            if (layoutParams4.width != this.EXPAND_VIEW_WIDTH) {
                int i7 = layoutParams4.height;
                int i8 = this.EXPAND_VIEW_HEIGHT;
                if (i7 != i8) {
                    layoutParams4.width = this.EXPAND_VIEW_WIDTH;
                    layoutParams4.height = i8;
                    this.mExpandView.setLayoutParams(layoutParams4);
                }
            }
            this.mExpandView.setBackgroundResource(R.drawable.bg_navi_bar_collapse);
            this.mExpandView.setImageResource(R.drawable.ic_collapse);
            this.mWindowLayoutParams.y = (this.mWindowRect.bottom - this.EXPAND_VIEW_HEIGHT) - this.mExpandTouchArea;
            this.mWindowManager.updateViewLayout(this, this.mWindowLayoutParams);
        }
    }

    public int getCollapseState() {
        if (this.mWindowLayoutParams.x <= this.mWindowRect.left) {
            return 2;
        }
        if (this.mWindowLayoutParams.x >= this.mWindowRect.right - getWidth()) {
            return 3;
        }
        if (this.mWindowLayoutParams.y <= this.mWindowRect.top + this.mTouchSlop) {
            return 1;
        }
        if (this.mWindowLayoutParams.y >= (this.mWindowRect.bottom - this.NAVI_HEIGHT) - this.mTouchSlop) {
            return 4;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xui.widget.XRelativeLayout, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        onUserTouch();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xui.widget.XRelativeLayout, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mHandler.removeMessages(1);
    }

    private void init() {
        setLayerType(1, null);
        this.mWindowManager = (WindowManager) getContext().getSystemService(ConditionWindowPos.TYPE);
        initWindowLayoutParams();
        setOnTouchListener(this.mNaviBarTouchListener);
        setFocusableInTouchMode(true);
        this.mXAppModeController = XAppModeController.getInstance();
        this.mXAppModeViewManager = XAppModeViewManager.getInstance();
    }

    private void initWindowLayoutParams() {
        this.mWindowLayoutParams = new WindowManager.LayoutParams();
        WindowManager.LayoutParams layoutParams = this.mWindowLayoutParams;
        layoutParams.intentFlags = 32;
        layoutParams.privateFlags |= 64;
        WindowManager.LayoutParams layoutParams2 = this.mWindowLayoutParams;
        layoutParams2.type = 2038;
        layoutParams2.format = 1;
        layoutParams2.flags = 8;
        layoutParams2.gravity = 51;
        layoutParams2.width = -2;
        layoutParams2.height = -2;
    }

    private void updateWindowRect() {
        this.mIsFullWindow = xpPackageInfo.isFullscreen(this.mXpPackageInfo);
        this.mWindowRect = this.mWindowManager.getActivityBounds(this.mXpPackageInfo.packageName, this.mIsFullWindow);
        Global.saveWindowRect(this.mDisplayId, this.mWindowRect);
        if (this.mWindowRect == null) {
            LogUtil.w(TAG, "mWindowRect is null, please check!");
            return;
        }
        LogUtil.d(TAG, "updateWindowRect packageName:" + this.mXpPackageInfo.packageName + " &mIsFullWindow:" + this.mIsFullWindow + " &Rect:" + this.mWindowRect.toString());
    }

    private void updateWindowParams() {
        int[] lastPosition = getLastPosition();
        if (lastPosition != null) {
            this.mLastPosX = lastPosition[0];
            this.mLastPosY = lastPosition[1];
            isAppTypeChanged(lastPosition[2]);
            return;
        }
        this.mWindowLayoutParams.y = this.mWindowRect.top;
        this.mWindowLayoutParams.x = this.mWindowRect.centerX() - (getWidth() / 2);
        addPreDrawListener();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mControlView = findViewById(R.id.content);
        this.mMore = (ImageButton) findViewById(R.id.img_more);
        this.mMore.setVisibility(8);
        this.mMore.setOnClickListener(this);
        this.mBack = (ImageButton) findViewById(R.id.back);
        this.mBack.setOnClickListener(this);
        this.mSoundEffect = (ImageButton) findViewById(R.id.img_sound_effect);
        this.mSoundEffect.setOnClickListener(this);
        this.mExit = (ImageButton) findViewById(R.id.exit);
        this.mExit.setOnClickListener(this);
        this.mJoystickBtn = findViewById(R.id.img_joystick);
        this.mJoystickBtn.setVisibility(8);
        this.mJoystickBtn.setOnClickListener(this);
        this.mSharedDisplay = (ImageButton) findViewById(R.id.img_share);
        this.mSharedDisplay.setVisibility(8);
        this.mSharedDisplay.setOnClickListener(this);
        this.mCollapseView = findViewById(R.id.view_collapse);
        this.mCollapseView.setOnClickListener(this);
        this.mExpandView = (ImageView) findViewById(R.id.img_expand);
        this.mExpandView.setOnClickListener(this);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        onUserTouch();
        this.mXAppModeViewManager.cancelDelayDismissTask(this.mDisplayId);
        int action = ev.getAction();
        if (action == 0) {
            float moveX = ev.getX();
            this.mLastTouchX = moveX;
            this.mLastTouchY = ev.getY();
            return false;
        } else if (action != 2) {
            return false;
        } else {
            float moveX2 = ev.getX();
            float moveY = ev.getY();
            return Math.abs(moveX2 - this.mLastTouchX) >= this.mTouchSlop && Math.abs(moveY - this.mLastTouchY) >= this.mTouchSlop;
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        disableGuidanceMode();
        switch (v.getId()) {
            case R.id.back /* 2131296352 */:
                LogUtil.i(TAG, "backBtn be clicked");
                this.mXAppModeController.goBack(this.mDisplayId);
                return;
            case R.id.exit /* 2131296432 */:
                LogUtil.i(TAG, "exitBtn be clicked");
                this.mXAppModeController.goHome(this.mDisplayId);
                return;
            case R.id.img_expand /* 2131296467 */:
                setState(2);
                return;
            case R.id.img_joystick /* 2131296469 */:
                LogUtil.i(TAG, "joystick button be clicked");
                toggleJoystickView();
                return;
            case R.id.img_more /* 2131296471 */:
                LogUtil.i(TAG, "moreBtn be clicked");
                toggleQsPanel();
                return;
            case R.id.img_share /* 2131296472 */:
                LogUtil.i(TAG, "shared display button be clicked");
                startSharedDisplay();
                return;
            case R.id.img_sound_effect /* 2131296473 */:
                LogUtil.i(TAG, "img_sound_effect button be clicked");
                toggleKaraokeEffectDialog();
                return;
            case R.id.view_collapse /* 2131296667 */:
                XAppModeViewManager.getInstance().hideQsPanel(this.mDisplayId);
                autoCollapseView();
                return;
            default:
                return;
        }
    }

    private void toggleKaraokeEffectDialog() {
        this.mXAppModeViewManager.hideOrShowKaraokeEffectDialog(this.mXpPackageInfo.packageName, this.mDisplayId);
    }

    private void toggleQsPanel() {
        if (this.mWindowLayoutParams.y >= this.mWindowRect.bottom - this.NAVI_HEIGHT) {
            this.mWindowLayoutParams.y = this.mWindowRect.bottom - this.NAVI_HEIGHT;
        }
        int targetPositionX = this.mWindowLayoutParams.x + getMeasuredWidth();
        int targetPositionY = this.mWindowLayoutParams.y + getMeasuredHeight();
        this.mXAppModeViewManager.hideOrShowQsPanel(this.mXpPackageInfo, this.mDisplayId, targetPositionX, targetPositionY);
    }

    private void toggleJoystickView() {
        if (this.mWindowLayoutParams.y >= this.mWindowRect.bottom - this.NAVI_HEIGHT) {
            this.mWindowLayoutParams.y = this.mWindowRect.bottom - this.NAVI_HEIGHT;
        }
        int targetPositionX = this.mWindowLayoutParams.x + getMeasuredWidth();
        int targetPositionY = this.mWindowLayoutParams.y + getMeasuredHeight();
        this.mXAppModeViewManager.hideOrHideJoystickView(targetPositionX, targetPositionY);
    }

    private void startSharedDisplay() {
        int toDisplayId = this.mDisplayId == 0 ? 1 : 0;
        SharedDisplayManager.getInstance().startSharedDisplay(toDisplayId);
    }

    private boolean shouldShowJoystickIcon() {
        return XUIConfig.isFlyGameModeEnabled() && isCurrentAppEnableOctopusMode();
    }

    private boolean isCurrentAppEnableOctopusMode() {
        xpPackageInfo xppackageinfo = this.mXpPackageInfo;
        return (xppackageinfo == null || (xppackageinfo.controlFlags & 4) == 0) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ensureLayoutParamsLegal() {
        if (this.mWindowLayoutParams.x < this.mWindowRect.left) {
            this.mWindowLayoutParams.x = this.mWindowRect.left;
        }
        if (this.mWindowLayoutParams.x > this.mWindowRect.right - getWidth()) {
            this.mWindowLayoutParams.x = this.mWindowRect.right - getWidth();
        }
        if (this.mWindowLayoutParams.y < this.mWindowRect.top) {
            this.mWindowLayoutParams.y = this.mWindowRect.top;
        }
        if (this.mWindowLayoutParams.y > this.mWindowRect.bottom - getHeight()) {
            this.mWindowLayoutParams.y = this.mWindowRect.bottom - getHeight();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePosition() {
        XAppModeViewManager.getInstance().hideQsPanel(this.mDisplayId);
        this.mWindowManager.updateViewLayout(this, this.mWindowLayoutParams);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void attachToEdge() {
        if (this.mWindowLayoutParams.y <= this.mWindowRect.top || this.mWindowLayoutParams.y >= (this.mWindowRect.bottom - getHeight()) - this.mTouchSlop) {
            return;
        }
        int centerX = this.mWindowRect.centerX() - (getWidth() / 2);
        int centerY = this.mWindowRect.centerY() - (getHeight() / 2);
        float xDeviation = Math.abs(centerX - this.mWindowLayoutParams.x) / this.mWindowRect.width();
        float yDeviation = Math.abs(centerY - this.mWindowLayoutParams.y) / this.mWindowRect.height();
        LogUtil.d(TAG, "attachToEdge xDeviation:" + xDeviation + " &yDeviation:" + yDeviation);
        if (xDeviation > yDeviation) {
            if (this.mWindowLayoutParams.x < centerX) {
                this.mWindowLayoutParams.x = this.mWindowRect.left;
            } else {
                this.mWindowLayoutParams.x = this.mWindowRect.right - getWidth();
            }
        } else if (this.mWindowLayoutParams.y < centerY) {
            this.mWindowLayoutParams.y = this.mWindowRect.top;
        } else {
            this.mWindowLayoutParams.y = this.mWindowRect.bottom - getHeight();
        }
        updatePosition();
    }

    private void refreshSharedDisplayButton() {
        boolean hasSharedDisplayFeature = SharedDisplayManager.hasSharedDisplayFeature();
        if (hasSharedDisplayFeature && Config.SHARED_DISPLAY_BUTTON_ENABLE) {
            this.mSharedDisplay.setVisibility(0);
            boolean isAppSupport = SharedDisplayManager.getInstance().isSupportSharedDisplay(this.mXpPackageInfo.packageName);
            this.mSharedDisplay.setEnabled(isAppSupport);
            return;
        }
        this.mSharedDisplay.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPerParam(int mDisplayId) {
        if (mDisplayId == 0) {
            SystemProperties.set("persist.sys.donavipri", String.valueOf(mDisplayId));
        } else if (mDisplayId == 1) {
            SystemProperties.set("persist.sys.donavisec", String.valueOf(mDisplayId));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initPerParam() {
        SystemProperties.set("persist.sys.donavipri", "-100");
        SystemProperties.set("persist.sys.donavisec", "-100");
    }

    private int[] getLastPosition() {
        String key = this.mXpPackageInfo.packageName + "|" + this.mDisplayId + "|";
        String lastPositionInfo = SharedPreferencesUtil.getInstance().get(key, "");
        if (!TextUtils.isEmpty(lastPositionInfo)) {
            String newValue = lastPositionInfo.substring(1, lastPositionInfo.length() - 1);
            String[] array = newValue.split(",");
            int appType = array.length == 3 ? Integer.parseInt(array[2]) : -1;
            return new int[]{Integer.valueOf(array[0]).intValue(), Integer.valueOf(array[1]).intValue(), appType};
        }
        return null;
    }

    private void savePosition(int x, int y) {
        if (this.mXpPackageInfo != null) {
            String key = this.mXpPackageInfo.packageName + "|" + this.mDisplayId + "|";
            SharedPreferencesUtil.getInstance().put(key, "{" + x + "," + y + "," + this.mXpPackageInfo.appType + "}");
        }
    }

    public void onUserTouch() {
        setAlpha(0.9f);
        this.mHandler.removeMessages(1);
        this.mHandler.sendEmptyMessageDelayed(1, 5000L);
    }

    private void addPreDrawListener() {
        getViewTreeObserver().addOnPreDrawListener(this.mPreDrawListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removePreDrawListener() {
        getViewTreeObserver().removeOnPreDrawListener(this.mPreDrawListener);
    }

    private void refreshSoundEffectIcon() {
        xpPackageInfo xppackageinfo = this.mXpPackageInfo;
        boolean isShowEffectIcon = xppackageinfo != null && xppackageinfo.appType == 7;
        this.mSoundEffect.setVisibility(isShowEffectIcon ? 0 : 8);
    }

    private void saveGuidanceMode() {
        SharedPreferencesUtil.getInstance().putBoolean(NAVI_GUIDANCE_MODE, this.mIsGuidanceMode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateStateAndMode() {
        updateState();
        disableGuidanceMode();
    }

    private void updateState() {
        if (this.mState == 1) {
            setState(2);
        }
    }

    private void disableGuidanceMode() {
        this.mIsGuidanceMode = false;
    }

    public void cachePosition() {
        updateState();
        savePosition(this.mWindowLayoutParams.x, this.mWindowLayoutParams.y);
    }

    private void isAppTypeChanged(int appType) {
        if (this.mXpPackageInfo.appType != appType) {
            this.mLastPosY = this.mWindowRect.top;
            this.mLastPosX = this.mWindowRect.centerX() - (getWidth() / 2);
        }
    }

    private void isOutOfBounds() {
        if (!this.mIsFullWindow) {
            if (this.mWindowLayoutParams.y < this.mWindowRect.top || this.mWindowLayoutParams.y > this.mWindowRect.bottom || this.mWindowLayoutParams.x < this.mWindowRect.left || this.mWindowLayoutParams.x > this.mWindowRect.right) {
                addPreDrawListener();
            }
        }
    }
}
