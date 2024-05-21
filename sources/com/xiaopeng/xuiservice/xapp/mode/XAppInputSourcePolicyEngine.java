package com.xiaopeng.xuiservice.xapp.mode;

import android.content.Context;
import android.os.Handler;
import androidx.core.view.InputDeviceCompat;
import com.xiaopeng.app.xpPackageInfo;
import com.xiaopeng.input.xpInputManager;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes5.dex */
public class XAppInputSourcePolicyEngine extends XAppBaseModeEngine {
    private static final String TAG = "XAppInputSourcePolicyEngine";
    private List<Integer> mNeedJoystickList;

    public XAppInputSourcePolicyEngine(Context context, Handler handler) {
        super(context, handler);
        this.mNeedJoystickList = new ArrayList();
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
    }

    @Override // com.xiaopeng.xuiservice.xapp.mode.XAppBaseModeEngine
    protected synchronized void handleSharedPackageInfoChange(xpPackageInfo[] packageInfos) {
        LogUtil.d(TAG, "handleSharedPackageInfoChange");
        for (int i = 0; i < packageInfos.length; i++) {
            if (packageInfos[i] != null && packageInfos[i].supportGamePad == 1) {
                if (!this.mNeedJoystickList.contains(Integer.valueOf(i))) {
                    this.mNeedJoystickList.add(Integer.valueOf(i));
                }
            } else if (this.mNeedJoystickList.contains(Integer.valueOf(i))) {
                this.mNeedJoystickList.remove(Integer.valueOf(i));
            }
        }
        int joystickPolicy = -1;
        if (!this.mNeedJoystickList.isEmpty()) {
            joystickPolicy = this.mNeedJoystickList.get(0).intValue();
        }
        LogUtil.d(TAG, "setInputSourcePolicy sourceType:16777232 &policy:" + joystickPolicy);
        xpInputManager.setInputSourcePolicy((int) InputDeviceCompat.SOURCE_JOYSTICK, joystickPolicy);
    }
}
