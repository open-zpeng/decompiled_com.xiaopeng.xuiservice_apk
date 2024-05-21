package com.xiaopeng.xuiservice.xapp.miniprog;

import com.google.gson.annotations.SerializedName;
/* loaded from: classes5.dex */
public class MiniUserInfo {
    @SerializedName("isLogin")
    private boolean isLogin = false;
    @SerializedName("userAvatar")
    private String userAvatar = "";
    @SerializedName("userKey")
    private String userKey = "";
    @SerializedName("userNick")
    private String userNick = "";
    @SerializedName("userRoute")
    private String userRoute = "";

    public boolean isLogin() {
        return this.isLogin;
    }

    public void setLogin(boolean login) {
        this.isLogin = login;
    }

    public String getUserAvatar() {
        return this.userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserNick() {
        return this.userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getUserKey() {
        return this.userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getUserRoute() {
        return this.userRoute;
    }

    public void setUserRoute(String userRoute) {
        this.userRoute = userRoute;
    }
}
