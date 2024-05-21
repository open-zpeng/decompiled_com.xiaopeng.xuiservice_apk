package com.alipay.arome.aromecli;

import android.os.Bundle;
import android.support.annotation.Keep;
import com.alipay.arome.aromecli.requst.AromeRequest;
import com.alipay.arome.aromecli.response.AromeResponse;
import com.alipay.mobile.aromeservice.AromeTaskParams;
import com.alipay.mobile.aromeservice.ResponseParams;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
@Keep
/* loaded from: classes4.dex */
public class AromeServiceTask<Request extends AromeRequest, Response extends AromeResponse> {
    private static final Bundle LOCAL_INVALID_RESULT;
    private static final ExecutorService THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(5, 9, 10, AromeTaskParams.KEEP_ALIVE_TIME_UNIT, new LinkedBlockingDeque(42));

    @Keep
    /* loaded from: classes4.dex */
    public interface Callback<T> {
        void onCallback(T t);
    }

    static {
        Bundle bundle = new Bundle();
        LOCAL_INVALID_RESULT = bundle;
        bundle.putBoolean(ResponseParams.RESPONSE_KEY_SUCCESS, false);
        LOCAL_INVALID_RESULT.putInt("code", 2);
    }

    private static boolean checkRequestValid(AromeRequest request) {
        return request != null && request.isValid();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void invoke(final Request request, final Callback<Response> callback) {
        Response createResult;
        if (!checkRequestValid(request) && (createResult = createResult(callback.getClass())) != null) {
            createResult.parse(LOCAL_INVALID_RESULT);
            callback.onCallback(createResult);
        }
        try {
            THREAD_POOL_EXECUTOR.submit(new Runnable() { // from class: com.alipay.arome.aromecli.AromeServiceTask.1
                @Override // java.lang.Runnable
                public final void run() {
                    try {
                        AromeServiceTask.this.onInvoke(request, callback);
                    } catch (Throwable throwable) {
                        a.a("task invoke error", throwable);
                    }
                }
            });
        } catch (Throwable throwable) {
            a.a("task onInvoke error", throwable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onInvoke(Request request, Callback<Response> callback) {
        Bundle resultParams = null;
        try {
            resultParams = AromeInit.getRemoteService().sendRequest(b.a, request.requestType(), request.requestParam());
        } catch (Throwable throwable) {
            a.a("task onInvoke error", throwable);
        }
        Response createResult = createResult(callback.getClass());
        if (createResult == null || resultParams == null) {
            callback.onCallback(null);
            return;
        }
        createResult.parse(resultParams);
        callback.onCallback(createResult);
    }

    private Response createResult(Class callbackClass) {
        Type[] types = callbackClass.getGenericInterfaces();
        try {
            return (Response) ((Class) ((ParameterizedType) types[0]).getActualTypeArguments()[0]).newInstance();
        } catch (Throwable throwable) {
            a.a("createResult", throwable);
            return null;
        }
    }
}
