package com.acrcloud.rec.recognizer;

import com.acrcloud.rec.ACRCloudConfig;
import java.util.Map;
/* loaded from: classes4.dex */
public interface IACRCloudRecognizer {
    String recognize(byte[] bArr, int i, Map<String, String> map, boolean z, ACRCloudConfig.RecognizerType recognizerType);

    ACRCloudResponse resumeRecognize(byte[] bArr, int i, Map<String, Object> map, Map<String, String> map2, int i2);

    ACRCloudResponse startRecognize(Map<String, String> map);
}
