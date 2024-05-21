package com.acrcloud.rec.network;

import com.acrcloud.rec.utils.ACRCloudException;
import java.util.Map;
/* loaded from: classes4.dex */
public interface IACRCloudHttpWrapper {
    String doGet(String str, Map<String, String> map, int i) throws ACRCloudException;

    String doPost(String str, Map<String, Object> map, int i) throws ACRCloudException;
}
