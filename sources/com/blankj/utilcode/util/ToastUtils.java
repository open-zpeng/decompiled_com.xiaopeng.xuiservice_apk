package com.blankj.utilcode.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import com.blankj.utilcode.R;
import com.blankj.utilcode.util.Utils;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionWindowPos;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
/* loaded from: classes4.dex */
public final class ToastUtils {
    private static final int COLOR_DEFAULT = -16777217;
    private static final ToastUtils DEFAULT_MAKER = make();
    private static final String NOTHING = "toast nothing";
    private static final String NULL = "toast null";
    private static final String TAG_TOAST = "TAG_TOAST";
    private static WeakReference<IToast> sWeakToast;
    private String mMode;
    private int mGravity = -1;
    private int mXOffset = -1;
    private int mYOffset = -1;
    private int mBgColor = COLOR_DEFAULT;
    private int mBgResource = -1;
    private int mTextColor = COLOR_DEFAULT;
    private int mTextSize = -1;
    private boolean isLong = false;
    private Drawable[] mIcons = new Drawable[4];
    private boolean isNotUseSystemToast = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public interface IToast {
        void cancel();

        void setToastView(View view);

        void setToastView(CharSequence charSequence);

        void show(int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes4.dex */
    public @interface MODE {
        public static final String DARK = "dark";
        public static final String LIGHT = "light";
    }

    @NonNull
    public static ToastUtils make() {
        ToastUtils tmpTrauteVar1 = new ToastUtils();
        return tmpTrauteVar1;
    }

    @NonNull
    public final ToastUtils setMode(String mode) {
        this.mMode = mode;
        return this;
    }

    @NonNull
    public final ToastUtils setGravity(int gravity, int xOffset, int yOffset) {
        this.mGravity = gravity;
        this.mXOffset = xOffset;
        this.mYOffset = yOffset;
        return this;
    }

    @NonNull
    public final ToastUtils setBgColor(@ColorInt int backgroundColor) {
        this.mBgColor = backgroundColor;
        return this;
    }

    @NonNull
    public final ToastUtils setBgResource(@DrawableRes int bgResource) {
        this.mBgResource = bgResource;
        return this;
    }

    @NonNull
    public final ToastUtils setTextColor(@ColorInt int msgColor) {
        this.mTextColor = msgColor;
        return this;
    }

    @NonNull
    public final ToastUtils setTextSize(int textSize) {
        this.mTextSize = textSize;
        return this;
    }

    @NonNull
    public final ToastUtils setDurationIsLong(boolean isLong) {
        this.isLong = isLong;
        return this;
    }

    @NonNull
    public final ToastUtils setLeftIcon(@DrawableRes int resId) {
        ToastUtils tmpTrauteVar2 = setLeftIcon(ContextCompat.getDrawable(Utils.getApp(), resId));
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ToastUtils.setLeftIcon() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public final ToastUtils setLeftIcon(@Nullable Drawable drawable) {
        this.mIcons[0] = drawable;
        return this;
    }

    @NonNull
    public final ToastUtils setTopIcon(@DrawableRes int resId) {
        ToastUtils tmpTrauteVar2 = setTopIcon(ContextCompat.getDrawable(Utils.getApp(), resId));
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ToastUtils.setTopIcon() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public final ToastUtils setTopIcon(@Nullable Drawable drawable) {
        this.mIcons[1] = drawable;
        return this;
    }

    @NonNull
    public final ToastUtils setRightIcon(@DrawableRes int resId) {
        ToastUtils tmpTrauteVar2 = setRightIcon(ContextCompat.getDrawable(Utils.getApp(), resId));
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ToastUtils.setRightIcon() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public final ToastUtils setRightIcon(@Nullable Drawable drawable) {
        this.mIcons[2] = drawable;
        return this;
    }

    @NonNull
    public final ToastUtils setBottomIcon(int resId) {
        ToastUtils tmpTrauteVar2 = setBottomIcon(ContextCompat.getDrawable(Utils.getApp(), resId));
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ToastUtils.setBottomIcon() marked by @androidx.annotation.NonNull");
    }

    @NonNull
    public final ToastUtils setBottomIcon(@Nullable Drawable drawable) {
        this.mIcons[3] = drawable;
        return this;
    }

    @NonNull
    public final ToastUtils setNotUseSystemToast() {
        this.isNotUseSystemToast = true;
        return this;
    }

    @NonNull
    public static ToastUtils getDefaultMaker() {
        ToastUtils tmpTrauteVar2 = DEFAULT_MAKER;
        if (tmpTrauteVar2 != null) {
            return tmpTrauteVar2;
        }
        throw new NullPointerException("Detected an attempt to return null from a method com.blankj.utilcode.util.ToastUtils.getDefaultMaker() marked by @androidx.annotation.NonNull");
    }

    public final void show(@Nullable CharSequence text) {
        show(text, getDuration(), this);
    }

    public final void show(@StringRes int resId) {
        show(UtilsBridge.getString(resId), getDuration(), this);
    }

    public final void show(@StringRes int resId, Object... args) {
        show(UtilsBridge.getString(resId, args), getDuration(), this);
    }

    public final void show(@Nullable String format, Object... args) {
        show(UtilsBridge.format(format, args), getDuration(), this);
    }

    public final void show(@NonNull View view) {
        if (view == null) {
            throw new NullPointerException("Argument 'view' of type View (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        show(view, getDuration(), this);
    }

    private int getDuration() {
        return this.isLong ? 1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public View tryApplyUtilsToastView(CharSequence text) {
        if (!MODE.DARK.equals(this.mMode) && !MODE.LIGHT.equals(this.mMode)) {
            Drawable[] drawableArr = this.mIcons;
            if (drawableArr[0] == null && drawableArr[1] == null && drawableArr[2] == null && drawableArr[3] == null) {
                return null;
            }
        }
        View toastView = UtilsBridge.layoutId2View(R.layout.utils_toast_view);
        TextView messageTv = (TextView) toastView.findViewById(16908299);
        if (MODE.DARK.equals(this.mMode)) {
            GradientDrawable bg = (GradientDrawable) toastView.getBackground().mutate();
            bg.setColor(Color.parseColor("#BB000000"));
            messageTv.setTextColor(-1);
        }
        messageTv.setText(text);
        if (this.mIcons[0] != null) {
            View leftIconView = toastView.findViewById(R.id.utvLeftIconView);
            ViewCompat.setBackground(leftIconView, this.mIcons[0]);
            leftIconView.setVisibility(0);
        }
        if (this.mIcons[1] != null) {
            View topIconView = toastView.findViewById(R.id.utvTopIconView);
            ViewCompat.setBackground(topIconView, this.mIcons[1]);
            topIconView.setVisibility(0);
        }
        if (this.mIcons[2] != null) {
            View rightIconView = toastView.findViewById(R.id.utvRightIconView);
            ViewCompat.setBackground(rightIconView, this.mIcons[2]);
            rightIconView.setVisibility(0);
        }
        if (this.mIcons[3] != null) {
            View bottomIconView = toastView.findViewById(R.id.utvBottomIconView);
            ViewCompat.setBackground(bottomIconView, this.mIcons[3]);
            bottomIconView.setVisibility(0);
        }
        return toastView;
    }

    public static void showShort(@Nullable CharSequence text) {
        show(text, 0, DEFAULT_MAKER);
    }

    public static void showShort(@StringRes int resId) {
        show(UtilsBridge.getString(resId), 0, DEFAULT_MAKER);
    }

    public static void showShort(@StringRes int resId, Object... args) {
        show(UtilsBridge.getString(resId, args), 0, DEFAULT_MAKER);
    }

    public static void showShort(@Nullable String format, Object... args) {
        show(UtilsBridge.format(format, args), 0, DEFAULT_MAKER);
    }

    public static void showLong(@Nullable CharSequence text) {
        show(text, 1, DEFAULT_MAKER);
    }

    public static void showLong(@StringRes int resId) {
        show(UtilsBridge.getString(resId), 1, DEFAULT_MAKER);
    }

    public static void showLong(@StringRes int resId, Object... args) {
        show(UtilsBridge.getString(resId, args), 1, DEFAULT_MAKER);
    }

    public static void showLong(@Nullable String format, Object... args) {
        show(UtilsBridge.format(format, args), 1, DEFAULT_MAKER);
    }

    public static void cancel() {
        UtilsBridge.runOnUiThread(new Runnable() { // from class: com.blankj.utilcode.util.ToastUtils.1
            @Override // java.lang.Runnable
            public void run() {
                if (ToastUtils.sWeakToast != null) {
                    IToast iToast = (IToast) ToastUtils.sWeakToast.get();
                    if (iToast != null) {
                        iToast.cancel();
                    }
                    WeakReference unused = ToastUtils.sWeakToast = null;
                }
            }
        });
    }

    private static void show(@Nullable CharSequence text, int duration, ToastUtils utils) {
        show(null, getToastFriendlyText(text), duration, utils);
    }

    private static void show(@NonNull View view, int duration, ToastUtils utils) {
        if (view == null) {
            throw new NullPointerException("Argument 'view' of type View (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        show(view, null, duration, utils);
    }

    private static void show(@Nullable final View view, @Nullable final CharSequence text, final int duration, @NonNull ToastUtils utils) {
        if (utils == null) {
            throw new NullPointerException("Argument 'utils' of type ToastUtils (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        UtilsBridge.runOnUiThread(new Runnable() { // from class: com.blankj.utilcode.util.ToastUtils.2
            @Override // java.lang.Runnable
            public void run() {
                ToastUtils.cancel();
                IToast iToast = ToastUtils.newToast(ToastUtils.this);
                WeakReference unused = ToastUtils.sWeakToast = new WeakReference(iToast);
                View view2 = view;
                if (view2 != null) {
                    iToast.setToastView(view2);
                } else {
                    iToast.setToastView(text);
                }
                iToast.show(duration);
            }
        });
    }

    private static CharSequence getToastFriendlyText(CharSequence src) {
        if (src == null) {
            return NULL;
        }
        if (src.length() != 0) {
            return src;
        }
        return NOTHING;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static IToast newToast(ToastUtils toastUtils) {
        if (!toastUtils.isNotUseSystemToast && NotificationManagerCompat.from(Utils.getApp()).areNotificationsEnabled()) {
            if (Build.VERSION.SDK_INT < 23) {
                return new SystemToast(toastUtils);
            }
            if (!UtilsBridge.isGrantedDrawOverlays()) {
                return new SystemToast(toastUtils);
            }
        }
        if (Build.VERSION.SDK_INT < 25) {
            return new WindowManagerToast(toastUtils, 2005);
        }
        if (UtilsBridge.isGrantedDrawOverlays()) {
            if (Build.VERSION.SDK_INT >= 26) {
                return new WindowManagerToast(toastUtils, 2038);
            }
            return new WindowManagerToast(toastUtils, 2002);
        }
        return new ActivityToast(toastUtils);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static final class SystemToast extends AbsToast {
        SystemToast(ToastUtils toastUtils) {
            super(toastUtils);
            if (Build.VERSION.SDK_INT == 25) {
                try {
                    Field mTNField = Toast.class.getDeclaredField("mTN");
                    mTNField.setAccessible(true);
                    Object mTN = mTNField.get(this.mToast);
                    Field mTNmHandlerField = mTNField.getType().getDeclaredField("mHandler");
                    mTNmHandlerField.setAccessible(true);
                    Handler tnHandler = (Handler) mTNmHandlerField.get(mTN);
                    mTNmHandlerField.set(mTN, new SafeHandler(tnHandler));
                } catch (Exception e) {
                }
            }
        }

        @Override // com.blankj.utilcode.util.ToastUtils.IToast
        public void show(int duration) {
            if (this.mToast == null) {
                return;
            }
            this.mToast.setDuration(duration);
            this.mToast.show();
        }

        /* loaded from: classes4.dex */
        static class SafeHandler extends Handler {
            private Handler impl;

            SafeHandler(Handler impl) {
                this.impl = impl;
            }

            @Override // android.os.Handler
            public void handleMessage(@NonNull Message msg) {
                if (msg == null) {
                    throw new NullPointerException("Argument 'msg' of type Message (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
                }
                this.impl.handleMessage(msg);
            }

            @Override // android.os.Handler
            public void dispatchMessage(@NonNull Message msg) {
                if (msg == null) {
                    throw new NullPointerException("Argument 'msg' of type Message (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
                }
                try {
                    this.impl.dispatchMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static final class WindowManagerToast extends AbsToast {
        private WindowManager.LayoutParams mParams;
        private WindowManager mWM;

        WindowManagerToast(ToastUtils toastUtils, int type) {
            super(toastUtils);
            this.mParams = new WindowManager.LayoutParams();
            this.mWM = (WindowManager) Utils.getApp().getSystemService(ConditionWindowPos.TYPE);
            this.mParams.type = type;
        }

        WindowManagerToast(ToastUtils toastUtils, WindowManager wm, int type) {
            super(toastUtils);
            this.mParams = new WindowManager.LayoutParams();
            this.mWM = wm;
            this.mParams.type = type;
        }

        @Override // com.blankj.utilcode.util.ToastUtils.IToast
        public void show(int duration) {
            if (this.mToast == null) {
                return;
            }
            WindowManager.LayoutParams layoutParams = this.mParams;
            layoutParams.height = -2;
            layoutParams.width = -2;
            layoutParams.format = -3;
            layoutParams.windowAnimations = 16973828;
            layoutParams.setTitle("ToastWithoutNotification");
            WindowManager.LayoutParams layoutParams2 = this.mParams;
            layoutParams2.flags = 152;
            layoutParams2.packageName = Utils.getApp().getPackageName();
            this.mParams.gravity = this.mToast.getGravity();
            if ((this.mParams.gravity & 7) == 7) {
                this.mParams.horizontalWeight = 1.0f;
            }
            if ((this.mParams.gravity & 112) == 112) {
                this.mParams.verticalWeight = 1.0f;
            }
            this.mParams.x = this.mToast.getXOffset();
            this.mParams.y = this.mToast.getYOffset();
            this.mParams.horizontalMargin = this.mToast.getHorizontalMargin();
            this.mParams.verticalMargin = this.mToast.getVerticalMargin();
            try {
                if (this.mWM != null) {
                    this.mWM.addView(this.mToastView, this.mParams);
                }
            } catch (Exception e) {
            }
            UtilsBridge.runOnUiThreadDelayed(new Runnable() { // from class: com.blankj.utilcode.util.ToastUtils.WindowManagerToast.1
                @Override // java.lang.Runnable
                public void run() {
                    WindowManagerToast.this.cancel();
                }
            }, duration == 0 ? 2000L : 3500L);
        }

        @Override // com.blankj.utilcode.util.ToastUtils.AbsToast, com.blankj.utilcode.util.ToastUtils.IToast
        public void cancel() {
            try {
                if (this.mWM != null) {
                    this.mWM.removeViewImmediate(this.mToastView);
                    this.mWM = null;
                }
            } catch (Exception e) {
            }
            super.cancel();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static final class ActivityToast extends AbsToast {
        private static int sShowingIndex = 0;
        private IToast iToast;
        private Utils.ActivityLifecycleCallbacks mActivityLifecycleCallbacks;

        ActivityToast(ToastUtils toastUtils) {
            super(toastUtils);
        }

        @Override // com.blankj.utilcode.util.ToastUtils.IToast
        public void show(int duration) {
            if (this.mToast == null) {
                return;
            }
            if (!UtilsBridge.isAppForeground()) {
                this.iToast = showSystemToast(duration);
                return;
            }
            boolean hasAliveActivity = false;
            for (Activity activity : UtilsBridge.getActivityList()) {
                if (UtilsBridge.isActivityAlive(activity)) {
                    if (!hasAliveActivity) {
                        hasAliveActivity = true;
                        this.iToast = showWithActivityWindow(activity, duration);
                    } else {
                        showWithActivityView(activity, sShowingIndex, true);
                    }
                }
            }
            if (hasAliveActivity) {
                registerLifecycleCallback();
                UtilsBridge.runOnUiThreadDelayed(new Runnable() { // from class: com.blankj.utilcode.util.ToastUtils.ActivityToast.1
                    @Override // java.lang.Runnable
                    public void run() {
                        ActivityToast.this.cancel();
                    }
                }, duration == 0 ? 2000L : 3500L);
                sShowingIndex++;
                return;
            }
            this.iToast = showSystemToast(duration);
        }

        @Override // com.blankj.utilcode.util.ToastUtils.AbsToast, com.blankj.utilcode.util.ToastUtils.IToast
        public void cancel() {
            Window window;
            if (isShowing()) {
                unregisterLifecycleCallback();
                for (Activity activity : UtilsBridge.getActivityList()) {
                    if (UtilsBridge.isActivityAlive(activity) && (window = activity.getWindow()) != null) {
                        ViewGroup decorView = (ViewGroup) window.getDecorView();
                        StringBuilder sb = new StringBuilder();
                        sb.append(ToastUtils.TAG_TOAST);
                        sb.append(sShowingIndex - 1);
                        View toastView = decorView.findViewWithTag(sb.toString());
                        if (toastView != null) {
                            try {
                                decorView.removeView(toastView);
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }
            IToast iToast = this.iToast;
            if (iToast != null) {
                iToast.cancel();
                this.iToast = null;
            }
            super.cancel();
        }

        private IToast showSystemToast(int duration) {
            SystemToast systemToast = new SystemToast(this.mToastUtils);
            systemToast.mToast = this.mToast;
            systemToast.show(duration);
            return systemToast;
        }

        private IToast showWithActivityWindow(Activity activity, int duration) {
            WindowManagerToast wmToast = new WindowManagerToast(this.mToastUtils, activity.getWindowManager(), 99);
            wmToast.mToastView = getToastViewSnapshot(-1);
            wmToast.mToast = this.mToast;
            wmToast.show(duration);
            return wmToast;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void showWithActivityView(Activity activity, int index, boolean useAnim) {
            Window window = activity.getWindow();
            if (window != null) {
                ViewGroup decorView = (ViewGroup) window.getDecorView();
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-2, -2);
                lp.gravity = this.mToast.getGravity();
                lp.bottomMargin = this.mToast.getYOffset() + UtilsBridge.getNavBarHeight();
                lp.topMargin = this.mToast.getYOffset() + UtilsBridge.getStatusBarHeight();
                lp.leftMargin = this.mToast.getXOffset();
                View toastViewSnapshot = getToastViewSnapshot(index);
                if (useAnim) {
                    toastViewSnapshot.setAlpha(0.0f);
                    toastViewSnapshot.animate().alpha(1.0f).setDuration(200L).start();
                }
                decorView.addView(toastViewSnapshot, lp);
            }
        }

        private void registerLifecycleCallback() {
            final int index = sShowingIndex;
            this.mActivityLifecycleCallbacks = new Utils.ActivityLifecycleCallbacks() { // from class: com.blankj.utilcode.util.ToastUtils.ActivityToast.2
                @Override // com.blankj.utilcode.util.Utils.ActivityLifecycleCallbacks
                public void onActivityCreated(@NonNull Activity activity) {
                    if (activity != null) {
                        if (ActivityToast.this.isShowing()) {
                            ActivityToast.this.showWithActivityView(activity, index, false);
                            return;
                        }
                        return;
                    }
                    throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
                }
            };
            UtilsBridge.addActivityLifecycleCallbacks(this.mActivityLifecycleCallbacks);
        }

        private void unregisterLifecycleCallback() {
            UtilsBridge.removeActivityLifecycleCallbacks(this.mActivityLifecycleCallbacks);
            this.mActivityLifecycleCallbacks = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isShowing() {
            return this.mActivityLifecycleCallbacks != null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static abstract class AbsToast implements IToast {
        protected Toast mToast = new Toast(Utils.getApp());
        protected ToastUtils mToastUtils;
        protected View mToastView;

        AbsToast(ToastUtils toastUtils) {
            this.mToastUtils = toastUtils;
            if (this.mToastUtils.mGravity != -1 || this.mToastUtils.mXOffset != -1 || this.mToastUtils.mYOffset != -1) {
                this.mToast.setGravity(this.mToastUtils.mGravity, this.mToastUtils.mXOffset, this.mToastUtils.mYOffset);
            }
        }

        @Override // com.blankj.utilcode.util.ToastUtils.IToast
        public void setToastView(View view) {
            this.mToastView = view;
            this.mToast.setView(this.mToastView);
        }

        @Override // com.blankj.utilcode.util.ToastUtils.IToast
        public void setToastView(CharSequence text) {
            View utilsToastView = this.mToastUtils.tryApplyUtilsToastView(text);
            if (utilsToastView != null) {
                setToastView(utilsToastView);
                processRtlIfNeed();
                return;
            }
            this.mToastView = this.mToast.getView();
            View view = this.mToastView;
            if (view == null || view.findViewById(16908299) == null) {
                setToastView(UtilsBridge.layoutId2View(R.layout.utils_toast_view));
            }
            TextView messageTv = (TextView) this.mToastView.findViewById(16908299);
            messageTv.setText(text);
            if (this.mToastUtils.mTextColor != ToastUtils.COLOR_DEFAULT) {
                messageTv.setTextColor(this.mToastUtils.mTextColor);
            }
            if (this.mToastUtils.mTextSize != -1) {
                messageTv.setTextSize(this.mToastUtils.mTextSize);
            }
            setBg(messageTv);
            processRtlIfNeed();
        }

        private void processRtlIfNeed() {
            if (UtilsBridge.isLayoutRtl()) {
                setToastView(getToastViewSnapshot(-1));
            }
        }

        private void setBg(TextView msgTv) {
            if (this.mToastUtils.mBgResource != -1) {
                this.mToastView.setBackgroundResource(this.mToastUtils.mBgResource);
                msgTv.setBackgroundColor(0);
            } else if (this.mToastUtils.mBgColor != ToastUtils.COLOR_DEFAULT) {
                Drawable toastBg = this.mToastView.getBackground();
                Drawable msgBg = msgTv.getBackground();
                if (toastBg != null && msgBg != null) {
                    toastBg.mutate().setColorFilter(new PorterDuffColorFilter(this.mToastUtils.mBgColor, PorterDuff.Mode.SRC_IN));
                    msgTv.setBackgroundColor(0);
                } else if (toastBg != null) {
                    toastBg.mutate().setColorFilter(new PorterDuffColorFilter(this.mToastUtils.mBgColor, PorterDuff.Mode.SRC_IN));
                } else if (msgBg != null) {
                    msgBg.mutate().setColorFilter(new PorterDuffColorFilter(this.mToastUtils.mBgColor, PorterDuff.Mode.SRC_IN));
                } else {
                    this.mToastView.setBackgroundColor(this.mToastUtils.mBgColor);
                }
            }
        }

        @Override // com.blankj.utilcode.util.ToastUtils.IToast
        @CallSuper
        public void cancel() {
            Toast toast = this.mToast;
            if (toast != null) {
                toast.cancel();
            }
            this.mToast = null;
            this.mToastView = null;
        }

        View getToastViewSnapshot(int index) {
            Bitmap bitmap = UtilsBridge.view2Bitmap(this.mToastView);
            ImageView toastIv = new ImageView(Utils.getApp());
            toastIv.setTag(ToastUtils.TAG_TOAST + index);
            toastIv.setImageBitmap(bitmap);
            return toastIv;
        }
    }

    /* loaded from: classes4.dex */
    public static final class UtilsMaxWidthRelativeLayout extends RelativeLayout {
        private static final int SPACING = UtilsBridge.dp2px(80.0f);

        public UtilsMaxWidthRelativeLayout(Context context) {
            super(context);
        }

        public UtilsMaxWidthRelativeLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public UtilsMaxWidthRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override // android.widget.RelativeLayout, android.view.View
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int widthMaxSpec = View.MeasureSpec.makeMeasureSpec(UtilsBridge.getAppScreenWidth() - SPACING, Integer.MIN_VALUE);
            super.onMeasure(widthMaxSpec, heightMeasureSpec);
        }
    }
}
