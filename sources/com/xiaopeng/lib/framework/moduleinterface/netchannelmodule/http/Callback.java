package com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http;
/* loaded from: classes.dex */
public interface Callback {
    void onFailure(IResponse iResponse);

    void onSuccess(IResponse iResponse);
}