package com.loostone.libserver.version1.entity;

import com.google.gson.Gson;
/* loaded from: classes4.dex */
public class NetUrlConfig {
    private static final String TAG = "NetUrlConfig";
    private static NetAddress netAddress;

    /* loaded from: classes4.dex */
    public interface TYPE {
        public static final int APP_UPDATE = 1;
        public static final int CACHE_REFRESH = 5;
        public static final int COMMUNICATION_KERNEL = 6;
        public static final int KARAOKE_VOICE_APP = 7;
        public static final int KARAOKE_VOLUME = 8;
        public static final int MIC_COMM_INFO = 4;
        public static final int MIC_LIMIT_INFO = 2;
        public static final int STATISTICAL_MIC_INFO = 3;
    }

    public static String getAddress(int i) {
        NetAddress netAddress2 = netAddress;
        if (netAddress2 == null) {
            return "";
        }
        switch (i) {
            case 1:
                return netAddress2.getAppUpdate();
            case 2:
                return netAddress2.getMicLimitInfo();
            case 3:
                return netAddress2.getStatisticalMicInfo();
            case 4:
                return netAddress2.getMicCommInfo();
            case 5:
                return netAddress2.getCacheRefresh();
            case 6:
                return netAddress2.getCommunicationKernel();
            case 7:
                return netAddress2.getKaraokeVoiceApp();
            case 8:
                return netAddress2.getKaraokeVolume();
            default:
                return "";
        }
    }

    public static void init(String str) {
        if (netAddress == null) {
            netAddress = (NetAddress) new Gson().fromJson(str, (Class<Object>) NetAddress.class);
        }
    }
}
