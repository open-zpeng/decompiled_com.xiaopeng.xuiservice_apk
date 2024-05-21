package com.loostone.libserver.version1.net;

import OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0O0.OooO0O0;
import OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0Oo.OooO00o;
import android.content.Context;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loostone.libserver.version1.entity.response.ResponseData;
import com.lzy.okgo.model.Progress;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes4.dex */
public class NetRequest {
    private static final String TAG = "NetRequest";
    private static RequestQueue mQueue;

    public static void cancelRequest(String str) {
        RequestQueue requestQueue = mQueue;
        if (requestQueue == null || str == null) {
            return;
        }
        requestQueue.cancelAll(str);
    }

    public static synchronized RequestQueue getQueue(Context context) {
        RequestQueue requestQueue;
        synchronized (NetRequest.class) {
            if (mQueue == null) {
                RequestQueue newRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
                mQueue = newRequestQueue;
                newRequestQueue.start();
            }
            requestQueue = mQueue;
        }
        return requestQueue;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void lambda$request$0(NetParam netParam, OooO0O0 oooO0O0, String str) {
        String msg = "response-success: url=" + netParam.url + ", response:" + str;
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> " + msg);
        }
        if (oooO0O0 != null) {
            OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0O0 oooO0O02 = (OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0O0) oooO0O0;
            OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0oO.OooO0O0 oooO0O03 = oooO0O02.OooO0O0;
            oooO0O03.getClass();
            Intrinsics.checkNotNullParameter(str, "<set-?>");
            oooO0O03.OooO0OO = str;
            oooO0O02.OooO0O0.OooO0Oo = System.currentTimeMillis();
            ResponseData OooO00o2 = ((OooO00o) oooO0O02.OooO0Oo.OooO0o0).OooO00o(str);
            if (OooO00o2.getUuid() != null && OooO00o2.getUuid().equals(oooO0O02.OooO0OO.getUuid())) {
                OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0oO.OooO0O0 oooO0O04 = oooO0O02.OooO0O0;
                oooO0O04.OooO0O0 = true;
                oooO0O02.OooO0Oo.OooO00o(oooO0O04);
                OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0O0.OooO00o oooO00o = oooO0O02.f255OooO00o;
                if (oooO00o != null) {
                    oooO00o.OooO00o(OooO00o2);
                    return;
                }
                return;
            }
            String tag = oooO0O02.OooO0Oo.OooO0O0;
            Intrinsics.checkNotNullParameter(tag, "tag");
            Intrinsics.checkNotNullParameter("----->无效的请求", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", tag + " -> ----->无效的请求");
            }
            OooO00o2.setState("-000002");
            try {
                OooO00o2.setMsg(URLEncoder.encode("无效的请求", "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                String tag2 = oooO0O02.OooO0Oo.OooO0O0;
                String msg2 = e.getMessage();
                Intrinsics.checkNotNullParameter(tag2, "tag");
                Intrinsics.checkNotNullParameter(msg2, "msg");
                if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 5) {
                    Log.e("LogTuning", tag2 + " -> " + msg2);
                }
            }
            OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0oO.OooO0O0 oooO0O05 = oooO0O02.OooO0O0;
            oooO0O05.OooO0O0 = true;
            oooO0O02.OooO0Oo.OooO00o(oooO0O05);
            OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0O0.OooO00o oooO00o2 = oooO0O02.f255OooO00o;
            if (oooO00o2 != null) {
                oooO00o2.OooO00o(OooO00o2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void lambda$request$1(NetParam netParam, OooO0O0 oooO0O0, VolleyError volleyError) {
        String msg = "response-error: url=" + netParam.url + " response error";
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 5) {
            Log.e("LogTuning", TAG + " -> " + msg);
        }
        if (oooO0O0 != null) {
            OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0O0 oooO0O02 = (OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0O0) oooO0O0;
            OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0O0.OooO00o oooO00o = oooO0O02.f255OooO00o;
            if (oooO00o != null) {
                oooO00o.OooO00o("无法连接服务器");
            }
            OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0oO.OooO0O0 oooO0O03 = oooO0O02.OooO0O0;
            oooO0O03.getClass();
            Intrinsics.checkNotNullParameter("无法连接服务器", "<set-?>");
            oooO0O03.OooO0OO = "无法连接服务器";
            OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0OO.OooO0oO.OooO0O0 oooO0O04 = oooO0O02.OooO0O0;
            oooO0O04.OooO0O0 = false;
            oooO0O04.OooO0Oo = System.currentTimeMillis();
            oooO0O02.OooO0Oo.OooO00o(oooO0O02.OooO0O0);
        }
    }

    public static synchronized StringRequest request(Context context, final NetParam netParam, final OooO0O0 oooO0O0) {
        MyStringRequest myStringRequest;
        synchronized (NetRequest.class) {
            if (mQueue == null) {
                mQueue = getQueue(context);
            }
            String msg = "request:url=" + netParam.url + ", param:" + netParam.param.toString();
            Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
            Intrinsics.checkNotNullParameter(msg, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", TAG + " -> " + msg);
            }
            myStringRequest = new MyStringRequest(1, netParam.url, new Response.Listener() { // from class: com.loostone.libserver.version1.net.-$$Lambda$NetRequest$QTDl291qtbDXi6o2RGKygZ8h4Ag
                @Override // com.android.volley.Response.Listener
                public final void onResponse(Object obj) {
                    NetRequest.lambda$request$0(NetParam.this, oooO0O0, (String) obj);
                }
            }, new Response.ErrorListener() { // from class: com.loostone.libserver.version1.net.-$$Lambda$NetRequest$axp7l-thZjjChZkgMLHu7VPpBgU
                @Override // com.android.volley.Response.ErrorListener
                public final void onErrorResponse(VolleyError volleyError) {
                    NetRequest.lambda$request$1(NetParam.this, oooO0O0, volleyError);
                }
            }) { // from class: com.loostone.libserver.version1.net.NetRequest.1
                @Override // com.android.volley.Request
                public byte[] getBody() {
                    NetParam netParam2 = netParam;
                    if (netParam2.isRawRequest) {
                        return netParam2.param.toString().getBytes();
                    }
                    return super.getBody();
                }

                @Override // com.android.volley.Request
                public Map<String, String> getHeaders() {
                    Map<String, String> map = netParam.header;
                    return map != null ? map : super.getHeaders();
                }

                @Override // com.android.volley.Request
                public Map<String, String> getParams() {
                    NetParam netParam2 = netParam;
                    if (netParam2.isRawRequest) {
                        return super.getParams();
                    }
                    return (Map) netParam2.param;
                }

                @Override // com.android.volley.Request
                public RetryPolicy getRetryPolicy() {
                    NetParam netParam2 = netParam;
                    return new DefaultRetryPolicy(netParam2.timeout, netParam2.retry, 1.0f);
                }
            };
            myStringRequest.setTag(netParam.requestTag);
            mQueue.add(myStringRequest);
        }
        return myStringRequest;
    }
}
