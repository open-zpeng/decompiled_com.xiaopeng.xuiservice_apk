package com.xiaopeng.xuiservice.carcontrol;

import android.content.Context;
import java.io.PrintWriter;
/* loaded from: classes5.dex */
public abstract class HalServiceBaseCarListener extends BaseCarListener {
    public abstract void dump(PrintWriter printWriter);

    public HalServiceBaseCarListener(Context context) {
        super(context);
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected boolean needConnectCar() {
        return false;
    }
}
