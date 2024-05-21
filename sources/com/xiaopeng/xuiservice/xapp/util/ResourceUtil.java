package com.xiaopeng.xuiservice.xapp.util;

import android.app.ActivityThread;
import android.util.SparseArray;
import com.qiyi.video.qsrbase.QYSConst;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.smart.condition.operator.Operators;
import com.xiaopeng.xuiservice.xapp.mode.octopus.Constants;
import com.xiaopeng.xuiservice.xapp.mode.octopus.bean.KeyViewBasicInfo;
/* loaded from: classes5.dex */
public class ResourceUtil {
    private static SparseArray mKeyViewInfo = new SparseArray();

    static {
        mKeyViewInfo.put(Constants.SpecialKeyEvent.KEYCODE_LEFT_NAVI, new KeyViewBasicInfo(Constants.SpecialKeyEvent.KEYCODE_LEFT_NAVI, R.drawable.selector_ic_game_l, "l", getDimen(R.dimen.key_button_medium_size), getDimen(R.dimen.key_button_medium_size)));
        mKeyViewInfo.put(Constants.SpecialKeyEvent.KEYCODE_RIGHT_NAVI, new KeyViewBasicInfo(Constants.SpecialKeyEvent.KEYCODE_RIGHT_NAVI, R.drawable.selector_ic_game_r, "r", getDimen(R.dimen.key_button_medium_size), getDimen(R.dimen.key_button_medium_size)));
        mKeyViewInfo.put(96, new KeyViewBasicInfo(96, R.drawable.selector_ic_game_a, "a", getDimen(R.dimen.key_button_small_size), getDimen(R.dimen.key_button_small_size)));
        mKeyViewInfo.put(97, new KeyViewBasicInfo(97, R.drawable.selector_ic_game_b, "b", getDimen(R.dimen.key_button_small_size), getDimen(R.dimen.key_button_small_size)));
        mKeyViewInfo.put(99, new KeyViewBasicInfo(99, R.drawable.selector_ic_game_x, "x", getDimen(R.dimen.key_button_small_size), getDimen(R.dimen.key_button_small_size)));
        mKeyViewInfo.put(100, new KeyViewBasicInfo(100, R.drawable.selector_ic_game_y, "y", getDimen(R.dimen.key_button_small_size), getDimen(R.dimen.key_button_small_size)));
        mKeyViewInfo.put(103, new KeyViewBasicInfo(103, R.drawable.selector_ic_game_rb, "rb", getDimen(R.dimen.key_button_small_size), getDimen(R.dimen.key_button_small_size)));
        mKeyViewInfo.put(105, new KeyViewBasicInfo(105, R.drawable.selector_ic_game_rt, "rt", getDimen(R.dimen.key_button_small_size), getDimen(R.dimen.key_button_small_size)));
        mKeyViewInfo.put(102, new KeyViewBasicInfo(102, R.drawable.selector_ic_game_lb, "lb", getDimen(R.dimen.key_button_small_size), getDimen(R.dimen.key_button_small_size)));
        mKeyViewInfo.put(104, new KeyViewBasicInfo(104, R.drawable.selector_ic_game_lt, Operators.OP_LT, getDimen(R.dimen.key_button_small_size), getDimen(R.dimen.key_button_small_size)));
        mKeyViewInfo.put(109, new KeyViewBasicInfo(109, R.drawable.selector_ic_game_select, QYSConst.DATA_TYPE_SELECT, getDimen(R.dimen.key_button_small_size), getDimen(R.dimen.key_button_small_size)));
        mKeyViewInfo.put(28, new KeyViewBasicInfo(28, R.drawable.selector_ic_game_clear, "clear", getDimen(R.dimen.key_button_small_size), getDimen(R.dimen.key_button_small_size)));
        mKeyViewInfo.put(108, new KeyViewBasicInfo(108, R.drawable.selector_ic_game_start, "start", getDimen(R.dimen.key_button_small_size), getDimen(R.dimen.key_button_small_size)));
    }

    public static int getDimen(int resId) {
        return ActivityThread.currentActivityThread().getApplication().getResources().getDimensionPixelOffset(resId);
    }

    public static String getString(int resId) {
        return ActivityThread.currentActivityThread().getApplication().getResources().getString(resId);
    }

    public static String[] getArrayString(int resId) {
        return ActivityThread.currentActivityThread().getApplication().getResources().getStringArray(resId);
    }

    public static KeyViewBasicInfo getViewInfoByKeyCode(int keycode) {
        return (KeyViewBasicInfo) mKeyViewInfo.get(keycode);
    }
}
