package com.xiaopeng.lib;
/* loaded from: classes.dex */
public class InitEventHolder implements HttpInitEventListener {
    private static volatile InitEventHolder sHttpInitEventListener = new InitEventHolder();
    private HttpInitEventListener mProxy;

    @Override // com.xiaopeng.lib.HttpInitEventListener
    public void onInitException(int code, String msg) {
        HttpInitEventListener httpInitEventListener = this.mProxy;
        if (httpInitEventListener != null) {
            httpInitEventListener.onInitException(code, msg);
        }
    }

    public static HttpInitEventListener get() {
        return sHttpInitEventListener;
    }

    public static void setProxy(HttpInitEventListener listener) {
        sHttpInitEventListener.mProxy = listener;
    }
}
