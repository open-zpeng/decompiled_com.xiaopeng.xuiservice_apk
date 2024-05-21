package com.xiaopeng.xuiservice.download.bean;

import android.os.Message;
import com.xiaopeng.xuiservice.download.download.DownloadListenerHelper;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
/* loaded from: classes5.dex */
public class DispatchStateHandler extends XuiWorkHandler {
    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        DownloadStateBean stateBean = (DownloadStateBean) msg.obj;
        DownloadListenerHelper.dispatchDownloadCallbackByType(stateBean);
    }
}
