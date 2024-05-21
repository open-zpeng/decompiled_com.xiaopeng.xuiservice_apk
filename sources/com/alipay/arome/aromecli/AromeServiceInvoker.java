package com.alipay.arome.aromecli;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Keep;
import androidx.core.app.NotificationCompat;
import com.alipay.arome.aromecli.AromeServiceTask;
import com.alipay.arome.aromecli.requst.AromeActivateRequest;
import com.alipay.arome.aromecli.requst.AromeRequest;
import com.alipay.arome.aromecli.response.AromeResponse;
import com.alipay.mobile.aromeservice.ipc.App;
@Keep
/* loaded from: classes4.dex */
public class AromeServiceInvoker {

    @Keep
    /* loaded from: classes4.dex */
    public interface BridgeCallback {
        void callback(String str);
    }

    @Keep
    /* loaded from: classes4.dex */
    public interface BridgeExtension {
        void onCalled(String str, String str2, BridgeCallback bridgeCallback);
    }

    @Keep
    /* loaded from: classes4.dex */
    public interface OnCustomClickListener {
        void onClick(String str);
    }

    public static <Request extends AromeRequest, Response extends AromeResponse> void invoke(Request request, AromeServiceTask.Callback<Response> callback) {
        request.packageName = AromeInit.getApplicationContext().getPackageName();
        request.invokeToken = getPackageToken();
        if (request instanceof AromeActivateRequest) {
            b.a = new App.Builder().hostAppId(((AromeActivateRequest) request).hostAppId).build();
        }
        if (b.a != null) {
            request.hostAppId = b.a.hostAppId;
        }
        a.a(request.packageName + " - " + request.hostAppId + " request invokeToken: " + request.invokeToken + " invoke " + request.requestType());
        new AromeServiceTask().invoke(request, callback);
    }

    public static void registerBridgeExtension(final BridgeExtension bridgeExtension) {
        AromeInit.getApplicationContext().getContentResolver().registerContentObserver(Uri.parse("content://com.alipay.mobile.arome.provider/bridge"), true, new ContentObserver(new Handler(Looper.getMainLooper())) { // from class: com.alipay.arome.aromecli.AromeServiceInvoker.1
            @Override // android.database.ContentObserver
            public final void onChange(boolean selfChange, final Uri uri) {
                String[] columnNames;
                a.b("onChange " + uri);
                try {
                    Cursor cursor = AromeInit.getApplicationContext().getContentResolver().query(uri, null, null, null, null);
                    String params = null;
                    if (cursor != null) {
                        int index1 = cursor.getColumnIndex("action");
                        int index2 = cursor.getColumnIndex("params");
                        a.b("index1 " + index1 + ", index2 = " + index2 + ", params");
                        for (String name : cursor.getColumnNames()) {
                            a.b("columnName " + name);
                        }
                        String action = null;
                        while (cursor.moveToNext()) {
                            if (index1 >= 0) {
                                action = cursor.getString(index1);
                            }
                            if (index2 >= 0) {
                                params = cursor.getString(index2);
                            }
                        }
                        cursor.close();
                        a.b("onChange " + uri + ", action = " + action + ", params" + params);
                        bridgeExtension.onCalled(action, params, new BridgeCallback() { // from class: com.alipay.arome.aromecli.AromeServiceInvoker.1.1
                            @Override // com.alipay.arome.aromecli.AromeServiceInvoker.BridgeCallback
                            public final void callback(String result) {
                                ContentValues value = new ContentValues();
                                value.put("response", result);
                                a.b("bridge callback " + result);
                                AromeInit.getApplicationContext().getContentResolver().update(uri, value, null, null);
                            }
                        });
                    }
                } catch (Throwable throwable) {
                    a.a("registerBridgeCallback", throwable);
                }
            }
        });
    }

    public static void registerCustomClickListener(final String[] eventList, final OnCustomClickListener listener) {
        new Thread(new Runnable() { // from class: com.alipay.arome.aromecli.AromeServiceInvoker.2
            @Override // java.lang.Runnable
            public final void run() {
                try {
                    AromeInit.getRemoteService().registerOnCustomClickListener(eventList);
                } catch (Throwable throwable) {
                    a.a("register custom click event error", throwable);
                }
            }
        }).start();
        AromeInit.getApplicationContext().getContentResolver().registerContentObserver(Uri.parse("content://com.alipay.mobile.arome.provider/customClick"), true, new ContentObserver(new Handler(Looper.getMainLooper())) { // from class: com.alipay.arome.aromecli.AromeServiceInvoker.3
            @Override // android.database.ContentObserver
            public final void onChange(boolean selfChange, Uri uri) {
                String[] columnNames;
                a.b("onChange " + uri);
                try {
                    Cursor cursor = AromeInit.getApplicationContext().getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null) {
                        int index1 = cursor.getColumnIndex(NotificationCompat.CATEGORY_EVENT);
                        a.b("index1 " + index1);
                        for (String name : cursor.getColumnNames()) {
                            a.b("columnName " + name);
                        }
                        String event = null;
                        while (cursor.moveToNext()) {
                            if (index1 >= 0) {
                                event = cursor.getString(index1);
                            }
                        }
                        cursor.close();
                        a.b("onChange " + uri + ", event = " + event);
                        listener.onClick(event);
                        AromeInit.getApplicationContext().getContentResolver().delete(uri, null, null);
                    }
                } catch (Throwable throwable) {
                    a.a("registerBridgeCallback", throwable);
                }
            }
        });
    }

    private static String getPackageToken() {
        ContentResolver resolver = AromeInit.getApplicationContext().getContentResolver();
        Uri uri = Uri.parse("content://com.alipay.mobile.arome.provider/invokeToken");
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String token = cursor.getString(0);
            cursor.close();
            return token;
        }
        return "";
    }
}
