package com.xiaopeng.xuiservice.download.bean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes5.dex */
public class DownloadState {
    public static final int TYPE_CANCEL = 5;
    public static final int TYPE_COMPLETED = 3;
    public static final int TYPE_ERROR = 6;
    public static final int TYPE_IN_PROGRESS = 2;
    public static final int TYPE_PAUSE = 4;
    public static final int TYPE_START = 1;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes5.dex */
    public @interface State {
    }
}
