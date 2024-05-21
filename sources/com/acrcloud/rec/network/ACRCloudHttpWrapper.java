package com.acrcloud.rec.network;

import com.acrcloud.rec.utils.ACRCloudException;
import java.util.Map;
/* loaded from: classes4.dex */
public class ACRCloudHttpWrapper {
    private static IACRCloudHttpWrapper httpWrapper = new ACRCloudHttpWrapperImpl();

    public static String doPost(String posturl, Map<String, Object> params, int timeOut) throws ACRCloudException {
        return httpWrapper.doPost(posturl, params, timeOut);
    }

    public static String doGet(String url, Map<String, String> params, int timeout) throws ACRCloudException {
        return httpWrapper.doGet(url, params, timeout);
    }
}
