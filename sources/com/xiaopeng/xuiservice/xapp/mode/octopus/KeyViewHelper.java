package com.xiaopeng.xuiservice.xapp.mode.octopus;

import android.graphics.Point;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.xapp.mode.octopus.bean.KeyMapInfo;
import com.xiaopeng.xuiservice.xapp.mode.octopus.bean.KeyMapInfos;
import com.xiaopeng.xuiservice.xapp.util.ResourceUtil;
import java.util.List;
/* loaded from: classes5.dex */
public class KeyViewHelper {
    public static void getDefaultPosition(int keycode, Point point) {
        if (keycode == 8001) {
            point.set(ResourceUtil.getDimen(R.dimen.key_button_l_margin_left), ResourceUtil.getDimen(R.dimen.key_button_l_margin_top));
            return;
        }
        KeyMapInfos defaultMapsInfo = KeyConfigManager.getInstance().getDefaultKeyMapInfos();
        List<KeyMapInfo> keyMapInfoList = defaultMapsInfo.getKeyMap();
        for (KeyMapInfo keyMapInfo : keyMapInfoList) {
            if (keyMapInfo.keycode == keycode) {
                point.set(keyMapInfo.x, keyMapInfo.y);
                return;
            }
        }
    }
}
