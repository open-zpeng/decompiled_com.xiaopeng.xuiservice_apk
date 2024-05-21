package com.xiaopeng.xuiservice.xapp.miniprog;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes5.dex */
public class MiniProgType {
    public static final int TYPE_EXIT = 5;
    public static final int TYPE_INIT = 0;
    public static final int TYPE_LAUNCH = 3;
    public static final int TYPE_LAUNCH_SERVICE = 12;
    public static final int TYPE_LOGIN = 1;
    public static final int TYPE_LOGIN_INFO = 6;
    public static final int TYPE_LOGOUT = 2;
    public static final int TYPE_MINI_DETAIL = 9;
    public static final int TYPE_MINI_LIST = 7;
    public static final int TYPE_PRELOAD = 4;
    public static final int TYPE_UPLOAD_INFO = 13;
    public static final int TYPE_UPLOAD_LOG = 8;
    public static final int TYPE_VERIFY_IDENTITY = 10;
    public static final int TYPE_VERIFY_INFO = 11;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes5.dex */
    public @interface TYPE {
    }
}
