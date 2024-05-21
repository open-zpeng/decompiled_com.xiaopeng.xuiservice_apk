package com.xiaopeng.xuiservice.mediacenter.bluetooth;

import android.content.Context;
import com.xiaopeng.btservice.avrcp.AvrcpControl;
import com.xiaopeng.btservice.base.AbsAvrcpControlCallback;
import com.xiaopeng.btservice.util.BtBoxesUtil;
import com.xiaopeng.xuimanager.mediacenter.bluetooth.AvrcpEventListener;
import com.xiaopeng.xuimanager.mediacenter.bluetooth.AvrcpMeteData;
/* loaded from: classes5.dex */
public class AvrcpControlWrapper {
    private AvrcpControl mAvrcpControl;
    private AbsAvrcpControlCallback mAvrcpControlCallback = new AbsAvrcpControlCallback() { // from class: com.xiaopeng.xuiservice.mediacenter.bluetooth.AvrcpControlWrapper.1
        @Override // com.xiaopeng.btservice.base.AbsAvrcpControlCallback
        public void onAvrcpServiceReady() {
            super.onAvrcpServiceReady();
        }

        @Override // com.xiaopeng.btservice.base.AbsAvrcpControlCallback
        public void onAvrcpStateChanged(String address, int prevState, int newState) {
            super.onAvrcpStateChanged(address, prevState, newState);
        }
    };
    private AvrcpEventListener mAvrcpEventListener;

    public AvrcpControlWrapper(Context context) {
        this.mAvrcpControl = new AvrcpControl(context, this.mAvrcpControlCallback);
        BtBoxesUtil.getInstance().registerCallback(this.mAvrcpControl);
    }

    public void setAvrcpEventListener(AvrcpEventListener listener) {
        this.mAvrcpEventListener = listener;
    }

    public void play() {
        this.mAvrcpControl.play();
    }

    public void pause() {
        this.mAvrcpControl.pause();
    }

    public void next() {
        this.mAvrcpControl.forward();
    }

    public void previous() {
        this.mAvrcpControl.backward();
    }

    public AvrcpMeteData getMeteData() {
        return null;
    }

    public long[] getPosition() {
        long[] position = {0, 0};
        return position;
    }

    public int getPlayStatus() {
        return -1;
    }
}
