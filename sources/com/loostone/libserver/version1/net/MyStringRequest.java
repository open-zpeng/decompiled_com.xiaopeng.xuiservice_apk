package com.loostone.libserver.version1.net;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;
/* loaded from: classes4.dex */
public class MyStringRequest extends StringRequest {
    public MyStringRequest(int i, String str, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(i, str, listener, errorListener);
    }

    private String parseCharset(Map<String, String> map) {
        String[] split;
        String str = map.get("Content-Type");
        if (str == null || (split = str.split(";")) == null) {
            return "UTF-8";
        }
        for (int i = 1; i < split.length; i++) {
            String[] split2 = split[i].trim().split("=");
            if (split2 != null && split2.length == 2 && split2[0].equals("charset")) {
                return split2[1];
            }
        }
        return "UTF-8";
    }

    @Override // com.android.volley.toolbox.StringRequest, com.android.volley.Request
    public Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
        String str;
        try {
            str = new String(networkResponse.data, parseCharset(networkResponse.headers));
        } catch (UnsupportedEncodingException e) {
            str = new String(networkResponse.data);
        }
        return Response.success(str, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }

    public MyStringRequest(String str, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(str, listener, errorListener);
    }
}
