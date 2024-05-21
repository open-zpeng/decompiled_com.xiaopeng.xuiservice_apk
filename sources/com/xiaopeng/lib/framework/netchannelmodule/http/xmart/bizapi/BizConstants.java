package com.xiaopeng.lib.framework.netchannelmodule.http.xmart.bizapi;

import com.acrcloud.rec.network.ACRCloudHttpWrapperImpl;
import com.xiaopeng.lib.utils.config.EnvConfig;
import com.xiaopeng.lib.utils.info.BuildInfoUtils;
import com.xiaopeng.xuiservice.mediacenter.Constants;
/* loaded from: classes.dex */
public final class BizConstants {
    public static final String APPID_CDU = "xp_xmart_car";
    public static final String AUTHORIZATION_XPSIGN = "xpSign";
    public static final String CAR_SECRET;
    public static final String CLIENT_ENCODING_NONE = "None";
    public static final String CLIENT_TYPE_CDU = "3";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final int ENCRYPTION_TYPE_IRDETO = 1;
    public static final int ENCRYPTION_TYPE_NONE = 0;
    public static final String HEADER_APP_ID = "XP-Appid";
    public static final String HEADER_AUTHORIZATION = "XP-Authorization";
    public static final String HEADER_CLIENT = "XP-Client";
    public static final String HEADER_CLIENT_ENCODING = "XP-Client-Encoding";
    public static final String HEADER_CLIENT_TYPE = "XP-Client-Type";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_ENCRYPTION_TYPE = "XP-Encryption-Type";
    public static final String HEADER_NONCE = "XP-Nonce";
    public static final String HEADER_PREFIX = "XP-";
    public static final String HEADER_SIGNATURE = "XP-Signature";
    public static final String HEADER_UID = "XP-Uid";

    /* loaded from: classes.dex */
    public enum METHOD {
        GET(ACRCloudHttpWrapperImpl.HTTP_METHOD_GET),
        POST(ACRCloudHttpWrapperImpl.HTTP_METHOD_POST);
        
        private String value;

        METHOD(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    static {
        CAR_SECRET = (BuildInfoUtils.isLanVersion() || EnvConfig.hasValidConfig()) ? "ghepbrwmgcispjja" : Constants.LyricRequest.SECRET_OFFICIAL_MEDIA;
    }
}
