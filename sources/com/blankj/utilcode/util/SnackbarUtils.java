package com.blankj.utilcode.util;

import android.os.Build;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
/* loaded from: classes4.dex */
public final class SnackbarUtils {
    private static final int COLOR_DEFAULT = -16777217;
    private static final int COLOR_ERROR = -65536;
    private static final int COLOR_MESSAGE = -1;
    private static final int COLOR_SUCCESS = -13912576;
    private static final int COLOR_WARNING = -16128;
    public static final int LENGTH_INDEFINITE = -2;
    public static final int LENGTH_LONG = 0;
    public static final int LENGTH_SHORT = -1;
    private static WeakReference<Snackbar> sWeakSnackbar;
    private View.OnClickListener actionListener;
    private CharSequence actionText;
    private int actionTextColor;
    private int bgColor;
    private int bgResource;
    private int bottomMargin;
    private int duration;
    private CharSequence message;
    private int messageColor;
    private View view;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes4.dex */
    public @interface Duration {
    }

    private SnackbarUtils(View parent) {
        setDefault();
        this.view = parent;
    }

    private void setDefault() {
        this.message = "";
        this.messageColor = COLOR_DEFAULT;
        this.bgColor = COLOR_DEFAULT;
        this.bgResource = -1;
        this.duration = -1;
        this.actionText = "";
        this.actionTextColor = COLOR_DEFAULT;
        this.bottomMargin = 0;
    }

    public static SnackbarUtils with(@NonNull View view) {
        if (view == null) {
            throw new NullPointerException("Argument 'view' of type View (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return new SnackbarUtils(view);
    }

    public SnackbarUtils setMessage(@NonNull CharSequence msg) {
        if (msg == null) {
            throw new NullPointerException("Argument 'msg' of type CharSequence (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        this.message = msg;
        return this;
    }

    public SnackbarUtils setMessageColor(@ColorInt int color) {
        this.messageColor = color;
        return this;
    }

    public SnackbarUtils setBgColor(@ColorInt int color) {
        this.bgColor = color;
        return this;
    }

    public SnackbarUtils setBgResource(@DrawableRes int bgResource) {
        this.bgResource = bgResource;
        return this;
    }

    public SnackbarUtils setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public SnackbarUtils setAction(@NonNull CharSequence text, @NonNull View.OnClickListener listener) {
        if (text == null) {
            throw new NullPointerException("Argument 'text' of type CharSequence (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (listener == null) {
            throw new NullPointerException("Argument 'listener' of type View.OnClickListener (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return setAction(text, COLOR_DEFAULT, listener);
    }

    public SnackbarUtils setAction(@NonNull CharSequence text, @ColorInt int color, @NonNull View.OnClickListener listener) {
        if (text == null) {
            throw new NullPointerException("Argument 'text' of type CharSequence (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (listener == null) {
            throw new NullPointerException("Argument 'listener' of type View.OnClickListener (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        this.actionText = text;
        this.actionTextColor = color;
        this.actionListener = listener;
        return this;
    }

    public SnackbarUtils setBottomMargin(@IntRange(from = 1) int bottomMargin) {
        this.bottomMargin = bottomMargin;
        return this;
    }

    public Snackbar show() {
        return show(false);
    }

    public Snackbar show(boolean isShowTop) {
        View view = this.view;
        if (view == null) {
            return null;
        }
        if (isShowTop) {
            ViewGroup suitableParent = findSuitableParentCopyFromSnackbar(view);
            View topSnackBarContainer = suitableParent.findViewWithTag("topSnackBarCoordinatorLayout");
            if (topSnackBarContainer == null) {
                CoordinatorLayout topSnackBarCoordinatorLayout = new CoordinatorLayout(view.getContext());
                topSnackBarCoordinatorLayout.setTag("topSnackBarCoordinatorLayout");
                topSnackBarCoordinatorLayout.setRotation(180.0f);
                if (Build.VERSION.SDK_INT >= 21) {
                    topSnackBarCoordinatorLayout.setElevation(100.0f);
                }
                suitableParent.addView(topSnackBarCoordinatorLayout, -1, -1);
                topSnackBarContainer = topSnackBarCoordinatorLayout;
            }
            view = topSnackBarContainer;
        }
        if (this.messageColor != COLOR_DEFAULT) {
            SpannableString spannableString = new SpannableString(this.message);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(this.messageColor);
            spannableString.setSpan(colorSpan, 0, spannableString.length(), 33);
            sWeakSnackbar = new WeakReference<>(Snackbar.make(view, spannableString, this.duration));
        } else {
            sWeakSnackbar = new WeakReference<>(Snackbar.make(view, this.message, this.duration));
        }
        Snackbar snackbar = sWeakSnackbar.get();
        Snackbar.SnackbarLayout snackbarView = (Snackbar.SnackbarLayout) snackbar.getView();
        if (isShowTop) {
            for (int i = 0; i < snackbarView.getChildCount(); i++) {
                View child = snackbarView.getChildAt(i);
                child.setRotation(180.0f);
            }
        }
        int i2 = this.bgResource;
        if (i2 != -1) {
            snackbarView.setBackgroundResource(i2);
        } else {
            int i3 = this.bgColor;
            if (i3 != COLOR_DEFAULT) {
                snackbarView.setBackgroundColor(i3);
            }
        }
        if (this.bottomMargin != 0) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snackbarView.getLayoutParams();
            params.bottomMargin = this.bottomMargin;
        }
        if (this.actionText.length() > 0 && this.actionListener != null) {
            int i4 = this.actionTextColor;
            if (i4 != COLOR_DEFAULT) {
                snackbar.setActionTextColor(i4);
            }
            snackbar.setAction(this.actionText, this.actionListener);
        }
        snackbar.show();
        return snackbar;
    }

    public void showSuccess() {
        showSuccess(false);
    }

    public void showSuccess(boolean isShowTop) {
        this.bgColor = COLOR_SUCCESS;
        this.messageColor = -1;
        this.actionTextColor = -1;
        show(isShowTop);
    }

    public void showWarning() {
        showWarning(false);
    }

    public void showWarning(boolean isShowTop) {
        this.bgColor = COLOR_WARNING;
        this.messageColor = -1;
        this.actionTextColor = -1;
        show(isShowTop);
    }

    public void showError() {
        showError(false);
    }

    public void showError(boolean isShowTop) {
        this.bgColor = -65536;
        this.messageColor = -1;
        this.actionTextColor = -1;
        show(isShowTop);
    }

    public static void dismiss() {
        WeakReference<Snackbar> weakReference = sWeakSnackbar;
        if (weakReference != null && weakReference.get() != null) {
            sWeakSnackbar.get().dismiss();
            sWeakSnackbar = null;
        }
    }

    public static View getView() {
        Snackbar snackbar = sWeakSnackbar.get();
        if (snackbar == null) {
            return null;
        }
        return snackbar.getView();
    }

    public static void addView(@LayoutRes int layoutId, @NonNull ViewGroup.LayoutParams params) {
        if (params == null) {
            throw new NullPointerException("Argument 'params' of type ViewGroup.LayoutParams (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        View view = getView();
        if (view != null) {
            view.setPadding(0, 0, 0, 0);
            Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) view;
            View child = LayoutInflater.from(view.getContext()).inflate(layoutId, (ViewGroup) null);
            layout.addView(child, -1, params);
        }
    }

    public static void addView(@NonNull View child, @NonNull ViewGroup.LayoutParams params) {
        if (child == null) {
            throw new NullPointerException("Argument 'child' of type View (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (params == null) {
            throw new NullPointerException("Argument 'params' of type ViewGroup.LayoutParams (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        View view = getView();
        if (view != null) {
            view.setPadding(0, 0, 0, 0);
            Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) view;
            layout.addView(child, params);
        }
    }

    private static ViewGroup findSuitableParentCopyFromSnackbar(View view) {
        ViewGroup fallback = null;
        while (!(view instanceof CoordinatorLayout)) {
            if (view instanceof FrameLayout) {
                if (view.getId() == 16908290) {
                    return (ViewGroup) view;
                }
                fallback = (ViewGroup) view;
            }
            if (view != null) {
                ViewParent parent = view.getParent();
                view = parent instanceof View ? (View) parent : null;
                continue;
            }
            if (view == null) {
                return fallback;
            }
        }
        return (ViewGroup) view;
    }
}
