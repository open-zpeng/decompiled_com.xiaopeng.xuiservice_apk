package com.qiyi.video.client;

import com.qiyi.video.qys.IQYSRequest;
import com.qiyi.video.qys.IQYSResponse;
/* loaded from: classes4.dex */
public interface IQYSService {

    /* loaded from: classes4.dex */
    public interface Callback {
        void onResponse(IQYSResponse iQYSResponse);
    }

    void executeAsync(IQYSRequest iQYSRequest, Callback callback);

    IQYSResponse executeSync(IQYSRequest iQYSRequest);
}
