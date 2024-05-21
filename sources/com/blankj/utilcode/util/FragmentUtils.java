package com.blankj.utilcode.util;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.AnimRes;
import androidx.annotation.AnimatorRes;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes4.dex */
public final class FragmentUtils {
    private static final String ARGS_ID = "args_id";
    private static final String ARGS_IS_ADD_STACK = "args_is_add_stack";
    private static final String ARGS_IS_HIDE = "args_is_hide";
    private static final String ARGS_TAG = "args_tag";
    private static final int TYPE_ADD_FRAGMENT = 1;
    private static final int TYPE_HIDE_FRAGMENT = 4;
    private static final int TYPE_REMOVE_FRAGMENT = 32;
    private static final int TYPE_REMOVE_TO_FRAGMENT = 64;
    private static final int TYPE_REPLACE_FRAGMENT = 16;
    private static final int TYPE_SHOW_FRAGMENT = 2;
    private static final int TYPE_SHOW_HIDE_FRAGMENT = 8;

    /* loaded from: classes4.dex */
    public interface OnBackClickListener {
        boolean onBackClick();
    }

    private FragmentUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void add(@NonNull FragmentManager fm, @NonNull Fragment add, @IdRes int containerId) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (add == null) {
            throw new NullPointerException("Argument 'add' of type Fragment (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        add(fm, add, containerId, (String) null, false, false);
    }

    public static void add(@NonNull FragmentManager fm, @NonNull Fragment add, @IdRes int containerId, boolean isHide) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (add == null) {
            throw new NullPointerException("Argument 'add' of type Fragment (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        add(fm, add, containerId, (String) null, isHide, false);
    }

    public static void add(@NonNull FragmentManager fm, @NonNull Fragment add, @IdRes int containerId, boolean isHide, boolean isAddStack) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (add == null) {
            throw new NullPointerException("Argument 'add' of type Fragment (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        add(fm, add, containerId, (String) null, isHide, isAddStack);
    }

    public static void add(@NonNull FragmentManager fm, @NonNull Fragment add, @IdRes int containerId, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (add == null) {
            throw new NullPointerException("Argument 'add' of type Fragment (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        add(fm, add, containerId, null, false, enterAnim, exitAnim, 0, 0);
    }

    public static void add(@NonNull FragmentManager fm, @NonNull Fragment add, @IdRes int containerId, boolean isAddStack, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (add == null) {
            throw new NullPointerException("Argument 'add' of type Fragment (#1 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        add(fm, add, containerId, null, isAddStack, enterAnim, exitAnim, 0, 0);
    }

    public static void add(@NonNull FragmentManager fm, @NonNull Fragment add, @IdRes int containerId, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim, @AnimRes @AnimatorRes int popEnterAnim, @AnimRes @AnimatorRes int popExitAnim) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (add == null) {
            throw new NullPointerException("Argument 'add' of type Fragment (#1 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        add(fm, add, containerId, null, false, enterAnim, exitAnim, popEnterAnim, popExitAnim);
    }

    public static void add(@NonNull FragmentManager fm, @NonNull Fragment add, @IdRes int containerId, boolean isAddStack, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim, @AnimRes @AnimatorRes int popEnterAnim, @AnimRes @AnimatorRes int popExitAnim) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 8, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (add == null) {
            throw new NullPointerException("Argument 'add' of type Fragment (#1 out of 8, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        add(fm, add, containerId, null, isAddStack, enterAnim, exitAnim, popEnterAnim, popExitAnim);
    }

    public static void add(@NonNull FragmentManager fm, @NonNull Fragment add, @IdRes int containerId, @NonNull View... sharedElements) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (add == null) {
            throw new NullPointerException("Argument 'add' of type Fragment (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (sharedElements == null) {
            throw new NullPointerException("Argument 'sharedElements' of type View[] (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        add(fm, add, containerId, (String) null, false, sharedElements);
    }

    public static void add(@NonNull FragmentManager fm, @NonNull Fragment add, @IdRes int containerId, boolean isAddStack, @NonNull View... sharedElements) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (add == null) {
            throw new NullPointerException("Argument 'add' of type Fragment (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (sharedElements == null) {
            throw new NullPointerException("Argument 'sharedElements' of type View[] (#4 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        add(fm, add, containerId, (String) null, isAddStack, sharedElements);
    }

    public static void add(@NonNull FragmentManager fm, @NonNull List<Fragment> adds, @IdRes int containerId, int showIndex) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (adds == null) {
            throw new NullPointerException("Argument 'adds' of type List<Fragment> (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        add(fm, (Fragment[]) adds.toArray(new Fragment[0]), containerId, (String[]) null, showIndex);
    }

    public static void add(@NonNull FragmentManager fm, @NonNull Fragment[] adds, @IdRes int containerId, int showIndex) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (adds == null) {
            throw new NullPointerException("Argument 'adds' of type Fragment[] (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        add(fm, adds, containerId, (String[]) null, showIndex);
    }

    public static void add(@NonNull FragmentManager fm, @NonNull Fragment add, @IdRes int containerId, String tag) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (add == null) {
            throw new NullPointerException("Argument 'add' of type Fragment (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        add(fm, add, containerId, tag, false, false);
    }

    public static void add(@NonNull FragmentManager fm, @NonNull Fragment add, @IdRes int containerId, String tag, boolean isHide) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (add == null) {
            throw new NullPointerException("Argument 'add' of type Fragment (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        add(fm, add, containerId, tag, isHide, false);
    }

    public static void add(@NonNull FragmentManager fm, @NonNull Fragment add, @IdRes int containerId, String tag, boolean isHide, boolean isAddStack) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (add == null) {
            throw new NullPointerException("Argument 'add' of type Fragment (#1 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        putArgs(add, new Args(containerId, tag, isHide, isAddStack));
        operateNoAnim(1, fm, null, add);
    }

    public static void add(@NonNull FragmentManager fm, @NonNull Fragment add, @IdRes int containerId, String tag, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (add == null) {
            throw new NullPointerException("Argument 'add' of type Fragment (#1 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        add(fm, add, containerId, tag, false, enterAnim, exitAnim, 0, 0);
    }

    public static void add(@NonNull FragmentManager fm, @NonNull Fragment add, @IdRes int containerId, String tag, boolean isAddStack, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (add == null) {
            throw new NullPointerException("Argument 'add' of type Fragment (#1 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        add(fm, add, containerId, tag, isAddStack, enterAnim, exitAnim, 0, 0);
    }

    public static void add(@NonNull FragmentManager fm, @NonNull Fragment add, @IdRes int containerId, String tag, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim, @AnimRes @AnimatorRes int popEnterAnim, @AnimRes @AnimatorRes int popExitAnim) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 8, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (add == null) {
            throw new NullPointerException("Argument 'add' of type Fragment (#1 out of 8, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        add(fm, add, containerId, tag, false, enterAnim, exitAnim, popEnterAnim, popExitAnim);
    }

    public static void add(@NonNull FragmentManager fm, @NonNull Fragment add, @IdRes int containerId, String tag, boolean isAddStack, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim, @AnimRes @AnimatorRes int popEnterAnim, @AnimRes @AnimatorRes int popExitAnim) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 9, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (add == null) {
            throw new NullPointerException("Argument 'add' of type Fragment (#1 out of 9, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        FragmentTransaction ft = fm.beginTransaction();
        putArgs(add, new Args(containerId, tag, false, isAddStack));
        addAnim(ft, enterAnim, exitAnim, popEnterAnim, popExitAnim);
        operate(1, fm, ft, null, add);
    }

    public static void add(@NonNull FragmentManager fm, @NonNull Fragment add, @IdRes int containerId, String tag, @NonNull View... sharedElements) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (add == null) {
            throw new NullPointerException("Argument 'add' of type Fragment (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (sharedElements == null) {
            throw new NullPointerException("Argument 'sharedElements' of type View[] (#4 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        add(fm, add, containerId, tag, false, sharedElements);
    }

    public static void add(@NonNull FragmentManager fm, @NonNull Fragment add, @IdRes int containerId, String tag, boolean isAddStack, @NonNull View... sharedElements) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (add == null) {
            throw new NullPointerException("Argument 'add' of type Fragment (#1 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (sharedElements == null) {
            throw new NullPointerException("Argument 'sharedElements' of type View[] (#5 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        FragmentTransaction ft = fm.beginTransaction();
        putArgs(add, new Args(containerId, tag, false, isAddStack));
        addSharedElement(ft, sharedElements);
        operate(1, fm, ft, null, add);
    }

    public static void add(@NonNull FragmentManager fm, @NonNull List<Fragment> adds, @IdRes int containerId, String[] tags, int showIndex) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (adds == null) {
            throw new NullPointerException("Argument 'adds' of type List<Fragment> (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        add(fm, (Fragment[]) adds.toArray(new Fragment[0]), containerId, tags, showIndex);
    }

    public static void add(@NonNull FragmentManager fm, @NonNull Fragment[] adds, @IdRes int containerId, String[] tags, int showIndex) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (adds == null) {
            throw new NullPointerException("Argument 'adds' of type Fragment[] (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (tags == null) {
            int i = 0;
            int len = adds.length;
            while (i < len) {
                putArgs(adds[i], new Args(containerId, null, showIndex != i, false));
                i++;
            }
        } else {
            int i2 = 0;
            int len2 = adds.length;
            while (i2 < len2) {
                putArgs(adds[i2], new Args(containerId, tags[i2], showIndex != i2, false));
                i2++;
            }
        }
        operateNoAnim(1, fm, null, adds);
    }

    public static void show(@NonNull Fragment show) {
        if (show == null) {
            throw new NullPointerException("Argument 'show' of type Fragment (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        putArgs(show, false);
        operateNoAnim(2, show.getFragmentManager(), null, show);
    }

    public static void show(@NonNull FragmentManager fm) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        List<Fragment> fragments = getFragments(fm);
        for (Fragment show : fragments) {
            putArgs(show, false);
        }
        operateNoAnim(2, fm, null, (Fragment[]) fragments.toArray(new Fragment[0]));
    }

    public static void hide(@NonNull Fragment hide) {
        if (hide == null) {
            throw new NullPointerException("Argument 'hide' of type Fragment (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        putArgs(hide, true);
        operateNoAnim(4, hide.getFragmentManager(), null, hide);
    }

    public static void hide(@NonNull FragmentManager fm) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        List<Fragment> fragments = getFragments(fm);
        for (Fragment hide : fragments) {
            putArgs(hide, true);
        }
        operateNoAnim(4, fm, null, (Fragment[]) fragments.toArray(new Fragment[0]));
    }

    public static void showHide(@NonNull Fragment show, @NonNull Fragment hide) {
        if (show == null) {
            throw new NullPointerException("Argument 'show' of type Fragment (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (hide == null) {
            throw new NullPointerException("Argument 'hide' of type Fragment (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        showHide(show, Collections.singletonList(hide));
    }

    public static void showHide(int showIndex, @NonNull Fragment... fragments) {
        if (fragments == null) {
            throw new NullPointerException("Argument 'fragments' of type Fragment[] (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        showHide(fragments[showIndex], fragments);
    }

    public static void showHide(@NonNull Fragment show, @NonNull Fragment... hide) {
        if (show == null) {
            throw new NullPointerException("Argument 'show' of type Fragment (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (hide == null) {
            throw new NullPointerException("Argument 'hide' of type Fragment[] (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        showHide(show, Arrays.asList(hide));
    }

    public static void showHide(int showIndex, @NonNull List<Fragment> fragments) {
        if (fragments == null) {
            throw new NullPointerException("Argument 'fragments' of type List<Fragment> (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        showHide(fragments.get(showIndex), fragments);
    }

    public static void showHide(@NonNull Fragment show, @NonNull List<Fragment> hide) {
        if (show == null) {
            throw new NullPointerException("Argument 'show' of type Fragment (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (hide == null) {
            throw new NullPointerException("Argument 'hide' of type List<Fragment> (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        Iterator<Fragment> it = hide.iterator();
        while (true) {
            boolean z = false;
            if (it.hasNext()) {
                Fragment fragment = it.next();
                if (fragment != show) {
                    z = true;
                }
                putArgs(fragment, z);
            } else {
                operateNoAnim(8, show.getFragmentManager(), show, (Fragment[]) hide.toArray(new Fragment[0]));
                return;
            }
        }
    }

    public static void showHide(@NonNull Fragment show, @NonNull Fragment hide, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim, @AnimRes @AnimatorRes int popEnterAnim, @AnimRes @AnimatorRes int popExitAnim) {
        if (show == null) {
            throw new NullPointerException("Argument 'show' of type Fragment (#0 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (hide == null) {
            throw new NullPointerException("Argument 'hide' of type Fragment (#1 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        showHide(show, Collections.singletonList(hide), enterAnim, exitAnim, popEnterAnim, popExitAnim);
    }

    public static void showHide(int showIndex, @NonNull List<Fragment> fragments, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim, @AnimRes @AnimatorRes int popEnterAnim, @AnimRes @AnimatorRes int popExitAnim) {
        if (fragments == null) {
            throw new NullPointerException("Argument 'fragments' of type List<Fragment> (#1 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        showHide(fragments.get(showIndex), fragments, enterAnim, exitAnim, popEnterAnim, popExitAnim);
    }

    public static void showHide(@NonNull Fragment show, @NonNull List<Fragment> hide, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim, @AnimRes @AnimatorRes int popEnterAnim, @AnimRes @AnimatorRes int popExitAnim) {
        if (show == null) {
            throw new NullPointerException("Argument 'show' of type Fragment (#0 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (hide == null) {
            throw new NullPointerException("Argument 'hide' of type List<Fragment> (#1 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        Iterator<Fragment> it = hide.iterator();
        while (true) {
            boolean z = false;
            if (!it.hasNext()) {
                break;
            }
            Fragment fragment = it.next();
            if (fragment != show) {
                z = true;
            }
            putArgs(fragment, z);
        }
        FragmentManager fm = show.getFragmentManager();
        if (fm != null) {
            FragmentTransaction ft = fm.beginTransaction();
            addAnim(ft, enterAnim, exitAnim, popEnterAnim, popExitAnim);
            operate(8, fm, ft, show, (Fragment[]) hide.toArray(new Fragment[0]));
        }
    }

    public static void replace(@NonNull Fragment srcFragment, @NonNull Fragment destFragment) {
        if (srcFragment == null) {
            throw new NullPointerException("Argument 'srcFragment' of type Fragment (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (destFragment == null) {
            throw new NullPointerException("Argument 'destFragment' of type Fragment (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        replace(srcFragment, destFragment, (String) null, false);
    }

    public static void replace(@NonNull Fragment srcFragment, @NonNull Fragment destFragment, boolean isAddStack) {
        if (srcFragment == null) {
            throw new NullPointerException("Argument 'srcFragment' of type Fragment (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (destFragment == null) {
            throw new NullPointerException("Argument 'destFragment' of type Fragment (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        replace(srcFragment, destFragment, (String) null, isAddStack);
    }

    public static void replace(@NonNull Fragment srcFragment, @NonNull Fragment destFragment, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim) {
        if (srcFragment == null) {
            throw new NullPointerException("Argument 'srcFragment' of type Fragment (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (destFragment == null) {
            throw new NullPointerException("Argument 'destFragment' of type Fragment (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        replace(srcFragment, destFragment, (String) null, false, enterAnim, exitAnim, 0, 0);
    }

    public static void replace(@NonNull Fragment srcFragment, @NonNull Fragment destFragment, boolean isAddStack, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim) {
        if (srcFragment == null) {
            throw new NullPointerException("Argument 'srcFragment' of type Fragment (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (destFragment == null) {
            throw new NullPointerException("Argument 'destFragment' of type Fragment (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        replace(srcFragment, destFragment, (String) null, isAddStack, enterAnim, exitAnim, 0, 0);
    }

    public static void replace(@NonNull Fragment srcFragment, @NonNull Fragment destFragment, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim, @AnimRes @AnimatorRes int popEnterAnim, @AnimRes @AnimatorRes int popExitAnim) {
        if (srcFragment == null) {
            throw new NullPointerException("Argument 'srcFragment' of type Fragment (#0 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (destFragment == null) {
            throw new NullPointerException("Argument 'destFragment' of type Fragment (#1 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        replace(srcFragment, destFragment, (String) null, false, enterAnim, exitAnim, popEnterAnim, popExitAnim);
    }

    public static void replace(@NonNull Fragment srcFragment, @NonNull Fragment destFragment, boolean isAddStack, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim, @AnimRes @AnimatorRes int popEnterAnim, @AnimRes @AnimatorRes int popExitAnim) {
        if (srcFragment == null) {
            throw new NullPointerException("Argument 'srcFragment' of type Fragment (#0 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (destFragment == null) {
            throw new NullPointerException("Argument 'destFragment' of type Fragment (#1 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        replace(srcFragment, destFragment, (String) null, isAddStack, enterAnim, exitAnim, popEnterAnim, popExitAnim);
    }

    public static void replace(@NonNull Fragment srcFragment, @NonNull Fragment destFragment, View... sharedElements) {
        if (srcFragment == null) {
            throw new NullPointerException("Argument 'srcFragment' of type Fragment (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (destFragment == null) {
            throw new NullPointerException("Argument 'destFragment' of type Fragment (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        replace(srcFragment, destFragment, (String) null, false, sharedElements);
    }

    public static void replace(@NonNull Fragment srcFragment, @NonNull Fragment destFragment, boolean isAddStack, View... sharedElements) {
        if (srcFragment == null) {
            throw new NullPointerException("Argument 'srcFragment' of type Fragment (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (destFragment == null) {
            throw new NullPointerException("Argument 'destFragment' of type Fragment (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        replace(srcFragment, destFragment, (String) null, isAddStack, sharedElements);
    }

    public static void replace(@NonNull FragmentManager fm, @NonNull Fragment fragment, @IdRes int containerId) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        replace(fm, fragment, containerId, (String) null, false);
    }

    public static void replace(@NonNull FragmentManager fm, @NonNull Fragment fragment, @IdRes int containerId, boolean isAddStack) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        replace(fm, fragment, containerId, (String) null, isAddStack);
    }

    public static void replace(@NonNull FragmentManager fm, @NonNull Fragment fragment, @IdRes int containerId, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        replace(fm, fragment, containerId, null, false, enterAnim, exitAnim, 0, 0);
    }

    public static void replace(@NonNull FragmentManager fm, @NonNull Fragment fragment, @IdRes int containerId, boolean isAddStack, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        replace(fm, fragment, containerId, null, isAddStack, enterAnim, exitAnim, 0, 0);
    }

    public static void replace(@NonNull FragmentManager fm, @NonNull Fragment fragment, @IdRes int containerId, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim, @AnimRes @AnimatorRes int popEnterAnim, @AnimRes @AnimatorRes int popExitAnim) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        replace(fm, fragment, containerId, null, false, enterAnim, exitAnim, popEnterAnim, popExitAnim);
    }

    public static void replace(@NonNull FragmentManager fm, @NonNull Fragment fragment, @IdRes int containerId, boolean isAddStack, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim, @AnimRes @AnimatorRes int popEnterAnim, @AnimRes @AnimatorRes int popExitAnim) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 8, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 8, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        replace(fm, fragment, containerId, null, isAddStack, enterAnim, exitAnim, popEnterAnim, popExitAnim);
    }

    public static void replace(@NonNull FragmentManager fm, @NonNull Fragment fragment, @IdRes int containerId, View... sharedElements) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        replace(fm, fragment, containerId, (String) null, false, sharedElements);
    }

    public static void replace(@NonNull FragmentManager fm, @NonNull Fragment fragment, @IdRes int containerId, boolean isAddStack, View... sharedElements) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        replace(fm, fragment, containerId, (String) null, isAddStack, sharedElements);
    }

    public static void replace(@NonNull Fragment srcFragment, @NonNull Fragment destFragment, String destTag) {
        if (srcFragment == null) {
            throw new NullPointerException("Argument 'srcFragment' of type Fragment (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (destFragment == null) {
            throw new NullPointerException("Argument 'destFragment' of type Fragment (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        replace(srcFragment, destFragment, destTag, false);
    }

    public static void replace(@NonNull Fragment srcFragment, @NonNull Fragment destFragment, String destTag, boolean isAddStack) {
        if (srcFragment == null) {
            throw new NullPointerException("Argument 'srcFragment' of type Fragment (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (destFragment == null) {
            throw new NullPointerException("Argument 'destFragment' of type Fragment (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        FragmentManager fm = srcFragment.getFragmentManager();
        if (fm == null) {
            return;
        }
        Args args = getArgs(srcFragment);
        replace(fm, destFragment, args.id, destTag, isAddStack);
    }

    public static void replace(@NonNull Fragment srcFragment, @NonNull Fragment destFragment, String destTag, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim) {
        if (srcFragment == null) {
            throw new NullPointerException("Argument 'srcFragment' of type Fragment (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (destFragment == null) {
            throw new NullPointerException("Argument 'destFragment' of type Fragment (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        replace(srcFragment, destFragment, destTag, false, enterAnim, exitAnim, 0, 0);
    }

    public static void replace(@NonNull Fragment srcFragment, @NonNull Fragment destFragment, String destTag, boolean isAddStack, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim) {
        if (srcFragment == null) {
            throw new NullPointerException("Argument 'srcFragment' of type Fragment (#0 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (destFragment == null) {
            throw new NullPointerException("Argument 'destFragment' of type Fragment (#1 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        replace(srcFragment, destFragment, destTag, isAddStack, enterAnim, exitAnim, 0, 0);
    }

    public static void replace(@NonNull Fragment srcFragment, @NonNull Fragment destFragment, String destTag, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim, @AnimRes @AnimatorRes int popEnterAnim, @AnimRes @AnimatorRes int popExitAnim) {
        if (srcFragment == null) {
            throw new NullPointerException("Argument 'srcFragment' of type Fragment (#0 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (destFragment == null) {
            throw new NullPointerException("Argument 'destFragment' of type Fragment (#1 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        replace(srcFragment, destFragment, destTag, false, enterAnim, exitAnim, popEnterAnim, popExitAnim);
    }

    public static void replace(@NonNull Fragment srcFragment, @NonNull Fragment destFragment, String destTag, boolean isAddStack, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim, @AnimRes @AnimatorRes int popEnterAnim, @AnimRes @AnimatorRes int popExitAnim) {
        if (srcFragment == null) {
            throw new NullPointerException("Argument 'srcFragment' of type Fragment (#0 out of 8, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (destFragment == null) {
            throw new NullPointerException("Argument 'destFragment' of type Fragment (#1 out of 8, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        FragmentManager fm = srcFragment.getFragmentManager();
        if (fm == null) {
            return;
        }
        Args args = getArgs(srcFragment);
        replace(fm, destFragment, args.id, destTag, isAddStack, enterAnim, exitAnim, popEnterAnim, popExitAnim);
    }

    public static void replace(@NonNull Fragment srcFragment, @NonNull Fragment destFragment, String destTag, View... sharedElements) {
        if (srcFragment == null) {
            throw new NullPointerException("Argument 'srcFragment' of type Fragment (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (destFragment == null) {
            throw new NullPointerException("Argument 'destFragment' of type Fragment (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        replace(srcFragment, destFragment, destTag, false, sharedElements);
    }

    public static void replace(@NonNull Fragment srcFragment, @NonNull Fragment destFragment, String destTag, boolean isAddStack, View... sharedElements) {
        if (srcFragment == null) {
            throw new NullPointerException("Argument 'srcFragment' of type Fragment (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (destFragment == null) {
            throw new NullPointerException("Argument 'destFragment' of type Fragment (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        FragmentManager fm = srcFragment.getFragmentManager();
        if (fm == null) {
            return;
        }
        Args args = getArgs(srcFragment);
        replace(fm, destFragment, args.id, destTag, isAddStack, sharedElements);
    }

    public static void replace(@NonNull FragmentManager fm, @NonNull Fragment fragment, @IdRes int containerId, String destTag) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        replace(fm, fragment, containerId, destTag, false);
    }

    public static void replace(@NonNull FragmentManager fm, @NonNull Fragment fragment, @IdRes int containerId, String destTag, boolean isAddStack) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        FragmentTransaction ft = fm.beginTransaction();
        putArgs(fragment, new Args(containerId, destTag, false, isAddStack));
        operate(16, fm, ft, null, fragment);
    }

    public static void replace(@NonNull FragmentManager fm, @NonNull Fragment fragment, @IdRes int containerId, String destTag, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        replace(fm, fragment, containerId, destTag, false, enterAnim, exitAnim, 0, 0);
    }

    public static void replace(@NonNull FragmentManager fm, @NonNull Fragment fragment, @IdRes int containerId, String destTag, boolean isAddStack, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        replace(fm, fragment, containerId, destTag, isAddStack, enterAnim, exitAnim, 0, 0);
    }

    public static void replace(@NonNull FragmentManager fm, @NonNull Fragment fragment, @IdRes int containerId, String destTag, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim, @AnimRes @AnimatorRes int popEnterAnim, @AnimRes @AnimatorRes int popExitAnim) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 8, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 8, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        replace(fm, fragment, containerId, destTag, false, enterAnim, exitAnim, popEnterAnim, popExitAnim);
    }

    public static void replace(@NonNull FragmentManager fm, @NonNull Fragment fragment, @IdRes int containerId, String destTag, boolean isAddStack, @AnimRes @AnimatorRes int enterAnim, @AnimRes @AnimatorRes int exitAnim, @AnimRes @AnimatorRes int popEnterAnim, @AnimRes @AnimatorRes int popExitAnim) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 9, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 9, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        FragmentTransaction ft = fm.beginTransaction();
        putArgs(fragment, new Args(containerId, destTag, false, isAddStack));
        addAnim(ft, enterAnim, exitAnim, popEnterAnim, popExitAnim);
        operate(16, fm, ft, null, fragment);
    }

    public static void replace(@NonNull FragmentManager fm, @NonNull Fragment fragment, @IdRes int containerId, String destTag, View... sharedElements) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        replace(fm, fragment, containerId, destTag, false, sharedElements);
    }

    public static void replace(@NonNull FragmentManager fm, @NonNull Fragment fragment, @IdRes int containerId, String destTag, boolean isAddStack, View... sharedElements) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        FragmentTransaction ft = fm.beginTransaction();
        putArgs(fragment, new Args(containerId, destTag, false, isAddStack));
        addSharedElement(ft, sharedElements);
        operate(16, fm, ft, null, fragment);
    }

    public static void pop(@NonNull FragmentManager fm) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        pop(fm, true);
    }

    public static void pop(@NonNull FragmentManager fm, boolean isImmediate) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (isImmediate) {
            fm.popBackStackImmediate();
        } else {
            fm.popBackStack();
        }
    }

    public static void popTo(@NonNull FragmentManager fm, Class<? extends Fragment> popClz, boolean isIncludeSelf) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        popTo(fm, popClz, isIncludeSelf, true);
    }

    public static void popTo(@NonNull FragmentManager fm, Class<? extends Fragment> popClz, boolean isIncludeSelf, boolean isImmediate) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (isImmediate) {
            fm.popBackStackImmediate(popClz.getName(), isIncludeSelf ? 1 : 0);
        } else {
            fm.popBackStack(popClz.getName(), isIncludeSelf ? 1 : 0);
        }
    }

    public static void popAll(@NonNull FragmentManager fm) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        popAll(fm, true);
    }

    public static void popAll(@NonNull FragmentManager fm, boolean isImmediate) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (fm.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry entry = fm.getBackStackEntryAt(0);
            if (isImmediate) {
                fm.popBackStackImmediate(entry.getId(), 1);
            } else {
                fm.popBackStack(entry.getId(), 1);
            }
        }
    }

    public static void remove(@NonNull Fragment remove) {
        if (remove == null) {
            throw new NullPointerException("Argument 'remove' of type Fragment (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        operateNoAnim(32, remove.getFragmentManager(), null, remove);
    }

    public static void removeTo(@NonNull Fragment removeTo, boolean isIncludeSelf) {
        if (removeTo == null) {
            throw new NullPointerException("Argument 'removeTo' of type Fragment (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        operateNoAnim(64, removeTo.getFragmentManager(), isIncludeSelf ? removeTo : null, removeTo);
    }

    public static void removeAll(@NonNull FragmentManager fm) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        List<Fragment> fragments = getFragments(fm);
        operateNoAnim(32, fm, null, (Fragment[]) fragments.toArray(new Fragment[0]));
    }

    private static void putArgs(Fragment fragment, Args args) {
        Bundle bundle = fragment.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            fragment.setArguments(bundle);
        }
        bundle.putInt(ARGS_ID, args.id);
        bundle.putBoolean(ARGS_IS_HIDE, args.isHide);
        bundle.putBoolean(ARGS_IS_ADD_STACK, args.isAddStack);
        bundle.putString(ARGS_TAG, args.tag);
    }

    private static void putArgs(Fragment fragment, boolean isHide) {
        Bundle bundle = fragment.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            fragment.setArguments(bundle);
        }
        bundle.putBoolean(ARGS_IS_HIDE, isHide);
    }

    private static Args getArgs(Fragment fragment) {
        Bundle bundle = fragment.getArguments();
        if (bundle == null) {
            bundle = Bundle.EMPTY;
        }
        return new Args(bundle.getInt(ARGS_ID, fragment.getId()), bundle.getBoolean(ARGS_IS_HIDE), bundle.getBoolean(ARGS_IS_ADD_STACK));
    }

    private static void operateNoAnim(int type, @Nullable FragmentManager fm, Fragment src, Fragment... dest) {
        if (fm == null) {
            return;
        }
        FragmentTransaction ft = fm.beginTransaction();
        operate(type, fm, ft, src, dest);
    }

    private static void operate(int type, @NonNull FragmentManager fm, FragmentTransaction ft, Fragment src, Fragment... dest) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (src != null && src.isRemoving()) {
            Log.e("FragmentUtils", src.getClass().getName() + " is isRemoving");
            return;
        }
        int i = 0;
        if (type == 1) {
            int length = dest.length;
            while (i < length) {
                Fragment fragment = dest[i];
                Bundle args = fragment.getArguments();
                if (args == null) {
                    return;
                }
                String name = args.getString(ARGS_TAG, fragment.getClass().getName());
                Fragment fragmentByTag = fm.findFragmentByTag(name);
                if (fragmentByTag != null && fragmentByTag.isAdded()) {
                    ft.remove(fragmentByTag);
                }
                ft.add(args.getInt(ARGS_ID), fragment, name);
                if (args.getBoolean(ARGS_IS_HIDE)) {
                    ft.hide(fragment);
                }
                if (args.getBoolean(ARGS_IS_ADD_STACK)) {
                    ft.addToBackStack(name);
                }
                i++;
            }
        } else if (type == 2) {
            int length2 = dest.length;
            while (i < length2) {
                ft.show(dest[i]);
                i++;
            }
        } else if (type == 4) {
            int length3 = dest.length;
            while (i < length3) {
                ft.hide(dest[i]);
                i++;
            }
        } else if (type == 8) {
            ft.show(src);
            int length4 = dest.length;
            while (i < length4) {
                Fragment fragment2 = dest[i];
                if (fragment2 != src) {
                    ft.hide(fragment2);
                }
                i++;
            }
        } else if (type == 16) {
            Bundle args2 = dest[0].getArguments();
            if (args2 == null) {
                return;
            }
            String name2 = args2.getString(ARGS_TAG, dest[0].getClass().getName());
            ft.replace(args2.getInt(ARGS_ID), dest[0], name2);
            if (args2.getBoolean(ARGS_IS_ADD_STACK)) {
                ft.addToBackStack(name2);
            }
        } else if (type == 32) {
            int i2 = dest.length;
            while (i < i2) {
                Fragment fragment3 = dest[i];
                if (fragment3 != src) {
                    ft.remove(fragment3);
                }
                i++;
            }
        } else if (type == 64) {
            int i3 = dest.length - 1;
            while (true) {
                if (i3 < 0) {
                    break;
                }
                Fragment fragment4 = dest[i3];
                if (fragment4 == dest[0]) {
                    if (src != null) {
                        ft.remove(fragment4);
                    }
                } else {
                    ft.remove(fragment4);
                    i3--;
                }
            }
        }
        ft.commitAllowingStateLoss();
        fm.executePendingTransactions();
    }

    private static void addAnim(FragmentTransaction ft, int enter, int exit, int popEnter, int popExit) {
        ft.setCustomAnimations(enter, exit, popEnter, popExit);
    }

    private static void addSharedElement(FragmentTransaction ft, View... views) {
        if (Build.VERSION.SDK_INT >= 21) {
            for (View view : views) {
                ft.addSharedElement(view, view.getTransitionName());
            }
        }
    }

    public static Fragment getTop(@NonNull FragmentManager fm) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getTopIsInStack(fm, null, false);
    }

    public static Fragment getTopInStack(@NonNull FragmentManager fm) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getTopIsInStack(fm, null, true);
    }

    private static Fragment getTopIsInStack(@NonNull FragmentManager fm, Fragment parentFragment, boolean isInStack) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        List<Fragment> fragments = getFragments(fm);
        for (int i = fragments.size() - 1; i >= 0; i--) {
            Fragment fragment = fragments.get(i);
            if (fragment != null) {
                if (isInStack) {
                    Bundle args = fragment.getArguments();
                    if (args != null && args.getBoolean(ARGS_IS_ADD_STACK)) {
                        return getTopIsInStack(fragment.getChildFragmentManager(), fragment, true);
                    }
                } else {
                    return getTopIsInStack(fragment.getChildFragmentManager(), fragment, false);
                }
            }
        }
        return parentFragment;
    }

    public static Fragment getTopShow(@NonNull FragmentManager fm) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getTopShowIsInStack(fm, null, false);
    }

    public static Fragment getTopShowInStack(@NonNull FragmentManager fm) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getTopShowIsInStack(fm, null, true);
    }

    private static Fragment getTopShowIsInStack(@NonNull FragmentManager fm, Fragment parentFragment, boolean isInStack) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        List<Fragment> fragments = getFragments(fm);
        for (int i = fragments.size() - 1; i >= 0; i--) {
            Fragment fragment = fragments.get(i);
            if (fragment != null && fragment.isResumed() && fragment.isVisible() && fragment.getUserVisibleHint()) {
                if (isInStack) {
                    Bundle args = fragment.getArguments();
                    if (args != null && args.getBoolean(ARGS_IS_ADD_STACK)) {
                        return getTopShowIsInStack(fragment.getChildFragmentManager(), fragment, true);
                    }
                } else {
                    return getTopShowIsInStack(fragment.getChildFragmentManager(), fragment, false);
                }
            }
        }
        return parentFragment;
    }

    public static List<Fragment> getFragments(@NonNull FragmentManager fm) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        List<Fragment> fragments = fm.getFragments();
        if (fragments == null || fragments.isEmpty()) {
            return Collections.emptyList();
        }
        return fragments;
    }

    public static List<Fragment> getFragmentsInStack(@NonNull FragmentManager fm) {
        Bundle args;
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        List<Fragment> fragments = getFragments(fm);
        List<Fragment> result = new ArrayList<>();
        for (Fragment fragment : fragments) {
            if (fragment != null && (args = fragment.getArguments()) != null && args.getBoolean(ARGS_IS_ADD_STACK)) {
                result.add(fragment);
            }
        }
        return result;
    }

    public static List<FragmentNode> getAllFragments(@NonNull FragmentManager fm) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getAllFragments(fm, new ArrayList());
    }

    private static List<FragmentNode> getAllFragments(@NonNull FragmentManager fm, List<FragmentNode> result) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        List<Fragment> fragments = getFragments(fm);
        for (int i = fragments.size() - 1; i >= 0; i--) {
            Fragment fragment = fragments.get(i);
            if (fragment != null) {
                result.add(new FragmentNode(fragment, getAllFragments(fragment.getChildFragmentManager(), new ArrayList())));
            }
        }
        return result;
    }

    public static List<FragmentNode> getAllFragmentsInStack(@NonNull FragmentManager fm) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return getAllFragmentsInStack(fm, new ArrayList());
    }

    private static List<FragmentNode> getAllFragmentsInStack(@NonNull FragmentManager fm, List<FragmentNode> result) {
        Bundle args;
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        List<Fragment> fragments = getFragments(fm);
        for (int i = fragments.size() - 1; i >= 0; i--) {
            Fragment fragment = fragments.get(i);
            if (fragment != null && (args = fragment.getArguments()) != null && args.getBoolean(ARGS_IS_ADD_STACK)) {
                result.add(new FragmentNode(fragment, getAllFragmentsInStack(fragment.getChildFragmentManager(), new ArrayList())));
            }
        }
        return result;
    }

    public static Fragment findFragment(@NonNull FragmentManager fm, Class<? extends Fragment> findClz) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return fm.findFragmentByTag(findClz.getName());
    }

    public static Fragment findFragment(@NonNull FragmentManager fm, @NonNull String tag) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (tag == null) {
            throw new NullPointerException("Argument 'tag' of type String (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return fm.findFragmentByTag(tag);
    }

    public static boolean dispatchBackPress(@NonNull Fragment fragment) {
        if (fragment != null) {
            return fragment.isResumed() && fragment.isVisible() && fragment.getUserVisibleHint() && (fragment instanceof OnBackClickListener) && ((OnBackClickListener) fragment).onBackClick();
        }
        throw new NullPointerException("Argument 'fragment' of type Fragment (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }

    public static boolean dispatchBackPress(@NonNull FragmentManager fm) {
        if (fm == null) {
            throw new NullPointerException("Argument 'fm' of type FragmentManager (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        List<Fragment> fragments = getFragments(fm);
        if (fragments == null || fragments.isEmpty()) {
            return false;
        }
        for (int i = fragments.size() - 1; i >= 0; i--) {
            Fragment fragment = fragments.get(i);
            if (fragment != null && fragment.isResumed() && fragment.isVisible() && fragment.getUserVisibleHint() && (fragment instanceof OnBackClickListener) && ((OnBackClickListener) fragment).onBackClick()) {
                return true;
            }
        }
        return false;
    }

    public static void setBackgroundColor(@NonNull Fragment fragment, @ColorInt int color) {
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        View view = fragment.getView();
        if (view != null) {
            view.setBackgroundColor(color);
        }
    }

    public static void setBackgroundResource(@NonNull Fragment fragment, @DrawableRes int resId) {
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        View view = fragment.getView();
        if (view != null) {
            view.setBackgroundResource(resId);
        }
    }

    public static void setBackground(@NonNull Fragment fragment, Drawable background) {
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        View view = fragment.getView();
        if (view == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    public static String getSimpleName(Fragment fragment) {
        return fragment == null ? "null" : fragment.getClass().getSimpleName();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class Args {
        final int id;
        final boolean isAddStack;
        final boolean isHide;
        final String tag;

        Args(int id, boolean isHide, boolean isAddStack) {
            this(id, null, isHide, isAddStack);
        }

        Args(int id, String tag, boolean isHide, boolean isAddStack) {
            this.id = id;
            this.tag = tag;
            this.isHide = isHide;
            this.isAddStack = isAddStack;
        }
    }

    /* loaded from: classes4.dex */
    public static class FragmentNode {
        final Fragment fragment;
        final List<FragmentNode> next;

        public FragmentNode(Fragment fragment, List<FragmentNode> next) {
            this.fragment = fragment;
            this.next = next;
        }

        public Fragment getFragment() {
            return this.fragment;
        }

        public List<FragmentNode> getNext() {
            return this.next;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.fragment.getClass().getSimpleName());
            sb.append("->");
            List<FragmentNode> list = this.next;
            sb.append((list == null || list.isEmpty()) ? "no child" : this.next.toString());
            return sb.toString();
        }
    }
}
