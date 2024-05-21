package com.blankj.utilcode.util;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import java.lang.reflect.Field;
/* loaded from: classes4.dex */
public final class KeyboardUtils {
    private static final int TAG_ON_GLOBAL_LAYOUT_LISTENER = -8;
    private static long millis;
    private static int sDecorViewDelta = 0;

    /* loaded from: classes4.dex */
    public interface OnSoftInputChangedListener {
        void onSoftInputChanged(int i);
    }

    private KeyboardUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void showSoftInput() {
        InputMethodManager imm = (InputMethodManager) Utils.getApp().getSystemService("input_method");
        if (imm == null) {
            return;
        }
        imm.toggleSoftInput(2, 1);
    }

    public static void showSoftInput(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (!isSoftInputVisible(activity)) {
            toggleSoftInput();
        }
    }

    public static void showSoftInput(@NonNull View view) {
        if (view == null) {
            throw new NullPointerException("Argument 'view' of type View (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        showSoftInput(view, 0);
    }

    public static void showSoftInput(@NonNull View view, int flags) {
        if (view == null) {
            throw new NullPointerException("Argument 'view' of type View (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        InputMethodManager imm = (InputMethodManager) Utils.getApp().getSystemService("input_method");
        if (imm == null) {
            return;
        }
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        imm.showSoftInput(view, flags, new ResultReceiver(new Handler()) { // from class: com.blankj.utilcode.util.KeyboardUtils.1
            @Override // android.os.ResultReceiver
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == 1 || resultCode == 3) {
                    KeyboardUtils.toggleSoftInput();
                }
            }
        });
        imm.toggleSoftInput(2, 1);
    }

    public static void hideSoftInput(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        hideSoftInput(activity.getWindow());
    }

    public static void hideSoftInput(@NonNull Window window) {
        if (window == null) {
            throw new NullPointerException("Argument 'window' of type Window (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        View view = window.getCurrentFocus();
        if (view == null) {
            View decorView = window.getDecorView();
            View focusView = decorView.findViewWithTag("keyboardTagView");
            if (focusView == null) {
                view = new EditText(window.getContext());
                view.setTag("keyboardTagView");
                ((ViewGroup) decorView).addView(view, 0, 0);
            } else {
                view = focusView;
            }
            view.requestFocus();
        }
        hideSoftInput(view);
    }

    public static void hideSoftInput(@NonNull View view) {
        if (view == null) {
            throw new NullPointerException("Argument 'view' of type View (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        InputMethodManager imm = (InputMethodManager) Utils.getApp().getSystemService("input_method");
        if (imm == null) {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideSoftInputByToggle(Activity activity) {
        long nowMillis = SystemClock.elapsedRealtime();
        long delta = nowMillis - millis;
        if (Math.abs(delta) > 500 && isSoftInputVisible(activity)) {
            toggleSoftInput();
        }
        millis = nowMillis;
    }

    public static void toggleSoftInput() {
        InputMethodManager imm = (InputMethodManager) Utils.getApp().getSystemService("input_method");
        if (imm == null) {
            return;
        }
        imm.toggleSoftInput(0, 0);
    }

    public static boolean isSoftInputVisible(@NonNull Activity activity) {
        if (activity != null) {
            return getDecorViewInvisibleHeight(activity.getWindow()) > 0;
        }
        throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getDecorViewInvisibleHeight(@NonNull Window window) {
        if (window == null) {
            throw new NullPointerException("Argument 'window' of type Window (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        View decorView = window.getDecorView();
        Rect outRect = new Rect();
        decorView.getWindowVisibleDisplayFrame(outRect);
        Log.d("KeyboardUtils", "getDecorViewInvisibleHeight: " + (decorView.getBottom() - outRect.bottom));
        int delta = Math.abs(decorView.getBottom() - outRect.bottom);
        if (delta <= UtilsBridge.getNavBarHeight() + UtilsBridge.getStatusBarHeight()) {
            sDecorViewDelta = delta;
            return 0;
        }
        return delta - sDecorViewDelta;
    }

    public static void registerSoftInputChangedListener(@NonNull Activity activity, @NonNull OnSoftInputChangedListener listener) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (listener == null) {
            throw new NullPointerException("Argument 'listener' of type OnSoftInputChangedListener (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        registerSoftInputChangedListener(activity.getWindow(), listener);
    }

    public static void registerSoftInputChangedListener(@NonNull final Window window, @NonNull final OnSoftInputChangedListener listener) {
        if (window == null) {
            throw new NullPointerException("Argument 'window' of type Window (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (listener == null) {
            throw new NullPointerException("Argument 'listener' of type OnSoftInputChangedListener (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        int flags = window.getAttributes().flags;
        if ((flags & 512) != 0) {
            window.clearFlags(512);
        }
        FrameLayout contentView = (FrameLayout) window.findViewById(16908290);
        final int[] decorViewInvisibleHeightPre = {getDecorViewInvisibleHeight(window)};
        ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.blankj.utilcode.util.KeyboardUtils.2
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                int height = KeyboardUtils.getDecorViewInvisibleHeight(window);
                if (decorViewInvisibleHeightPre[0] != height) {
                    listener.onSoftInputChanged(height);
                    decorViewInvisibleHeightPre[0] = height;
                }
            }
        };
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        contentView.setTag(-8, onGlobalLayoutListener);
    }

    public static void unregisterSoftInputChangedListener(@NonNull Window window) {
        if (window == null) {
            throw new NullPointerException("Argument 'window' of type Window (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        View contentView = window.findViewById(16908290);
        if (contentView == null) {
            return;
        }
        Object tag = contentView.getTag(-8);
        if ((tag instanceof ViewTreeObserver.OnGlobalLayoutListener) && Build.VERSION.SDK_INT >= 16) {
            contentView.getViewTreeObserver().removeOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener) tag);
        }
    }

    public static void fixAndroidBug5497(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        fixAndroidBug5497(activity.getWindow());
    }

    public static void fixAndroidBug5497(@NonNull final Window window) {
        if (window == null) {
            throw new NullPointerException("Argument 'window' of type Window (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        int softInputMode = window.getAttributes().softInputMode;
        window.setSoftInputMode(softInputMode & (-17));
        FrameLayout contentView = (FrameLayout) window.findViewById(16908290);
        final View contentViewChild = contentView.getChildAt(0);
        final int paddingBottom = contentViewChild.getPaddingBottom();
        final int[] contentViewInvisibleHeightPre5497 = {getContentViewInvisibleHeight(window)};
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.blankj.utilcode.util.KeyboardUtils.3
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                int height = KeyboardUtils.getContentViewInvisibleHeight(window);
                if (contentViewInvisibleHeightPre5497[0] != height) {
                    View view = contentViewChild;
                    view.setPadding(view.getPaddingLeft(), contentViewChild.getPaddingTop(), contentViewChild.getPaddingRight(), paddingBottom + KeyboardUtils.getDecorViewInvisibleHeight(window));
                    contentViewInvisibleHeightPre5497[0] = height;
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getContentViewInvisibleHeight(Window window) {
        View contentView = window.findViewById(16908290);
        if (contentView == null) {
            return 0;
        }
        Rect outRect = new Rect();
        contentView.getWindowVisibleDisplayFrame(outRect);
        Log.d("KeyboardUtils", "getContentViewInvisibleHeight: " + (contentView.getBottom() - outRect.bottom));
        int delta = Math.abs(contentView.getBottom() - outRect.bottom);
        if (delta <= UtilsBridge.getStatusBarHeight() + UtilsBridge.getNavBarHeight()) {
            return 0;
        }
        return delta;
    }

    public static void fixSoftInputLeaks(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        fixSoftInputLeaks(activity.getWindow());
    }

    public static void fixSoftInputLeaks(@NonNull Window window) {
        if (window == null) {
            throw new NullPointerException("Argument 'window' of type Window (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        InputMethodManager imm = (InputMethodManager) Utils.getApp().getSystemService("input_method");
        if (imm == null) {
            return;
        }
        String[] leakViews = {"mLastSrvView", "mCurRootView", "mServedView", "mNextServedView"};
        for (String leakView : leakViews) {
            try {
                Field leakViewField = InputMethodManager.class.getDeclaredField(leakView);
                if (!leakViewField.isAccessible()) {
                    leakViewField.setAccessible(true);
                }
                Object obj = leakViewField.get(imm);
                if (obj instanceof View) {
                    View view = (View) obj;
                    if (view.getRootView() == window.getDecorView().getRootView()) {
                        leakViewField.set(imm, null);
                    }
                }
            } catch (Throwable th) {
            }
        }
    }

    public static void clickBlankArea2HideSoftInput() {
        Log.i("KeyboardUtils", "Please refer to the following code.");
    }
}
