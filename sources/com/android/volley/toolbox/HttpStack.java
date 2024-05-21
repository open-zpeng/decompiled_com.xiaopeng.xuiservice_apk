package com.android.volley.toolbox;

import com.android.volley.Request;
import java.util.Map;
@Deprecated
/* loaded from: classes4.dex */
public interface HttpStack {
    org.apache.http.HttpResponse performRequest(Request<?> request, Map<String, String> map);
}
