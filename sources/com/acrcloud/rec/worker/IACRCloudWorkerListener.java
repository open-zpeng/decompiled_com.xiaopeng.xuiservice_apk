package com.acrcloud.rec.worker;

import com.acrcloud.rec.ACRCloudResult;
import java.util.Map;
/* loaded from: classes4.dex */
public interface IACRCloudWorkerListener {
    void onResult(ACRCloudResult aCRCloudResult);

    void onStartRecognize(Map<String, Object> map);
}
