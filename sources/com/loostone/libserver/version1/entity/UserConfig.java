package com.loostone.libserver.version1.entity;

import android.text.TextUtils;
/* loaded from: classes4.dex */
public class UserConfig {
    private static UserConfig m_Instance;
    private String TOKEN = "";
    private String TOKEN_ID = "";
    private String USER_ID = "";
    private String AGENT = "";
    private String HEAD_IMAGE = "";
    private String NICK_NAME = "";

    private UserConfig() {
    }

    public static synchronized UserConfig getInstance() {
        UserConfig userConfig;
        synchronized (UserConfig.class) {
            if (m_Instance == null) {
                m_Instance = new UserConfig();
            }
            userConfig = m_Instance;
        }
        return userConfig;
    }

    public String getHeadImage() {
        return this.HEAD_IMAGE;
    }

    public String getNickName() {
        return this.NICK_NAME;
    }

    public String getToken() {
        return this.TOKEN;
    }

    public String getTokenId() {
        return this.TOKEN_ID;
    }

    public String getUserId() {
        return this.USER_ID;
    }

    public boolean isAgent() {
        return "1".equals(this.AGENT);
    }

    public boolean isLogin() {
        return (TextUtils.isEmpty(this.TOKEN) || TextUtils.isEmpty(this.TOKEN_ID) || TextUtils.isEmpty(this.USER_ID)) ? false : true;
    }

    public void logout() {
        this.USER_ID = "";
        this.TOKEN_ID = "";
        this.TOKEN = "";
        this.AGENT = "";
        this.HEAD_IMAGE = "";
        this.NICK_NAME = "";
    }

    public void setLoginInfo(String str, String str2, String str3, String str4, String str5, String str6) {
        this.USER_ID = str;
        this.TOKEN_ID = str2;
        this.TOKEN = str3;
        this.AGENT = str4;
        this.HEAD_IMAGE = str5;
        this.NICK_NAME = str6;
    }

    public void setNickName(String str) {
        this.NICK_NAME = str;
    }
}
