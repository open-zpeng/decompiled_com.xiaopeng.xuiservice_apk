package com.xiaopeng.lib.http.tls;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import com.xiaopeng.lib.HttpInitEventListener;
import com.xiaopeng.lib.InitEventHolder;
import com.xiaopeng.lib.framework.module.Module;
import com.xiaopeng.lib.framework.moduleinterface.systemdelegate.ISystemDelegate;
import com.xiaopeng.lib.http.FileUtils;
import com.xiaopeng.lib.http.tls.SSLHelper;
import com.xiaopeng.lib.security.SecurityCommon;
import com.xiaopeng.lib.security.xmartv1.XmartV1Constants;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.system.delegate.module.SystemDelegateModuleEntry;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
/* loaded from: classes.dex */
public class LocalFileCert {
    public static final String CERT_PATH = "/private/sec/xp0109.png";
    private static final String KEY_STORE_PASSWORD = "chengzi";
    private static final String KEY_STORE_TRUST_PASSWORD = "chengzi";
    private static final String KEY_STORE_TRUST_PATH = "index_kstp.html";
    private static final String KEY_STORE_TYPE_BKS = "bks";
    private static final String KEY_STORE_TYPE_P12 = "PKCS12";
    private static final String TAG = "LocalFileCert";
    private static KeyManager[] sKeyManagers;
    private static TrustManager[] sTrustManagers;

    private static synchronized TrustManager[] getTrustManagers(Context context) {
        KeyStore trustStore;
        synchronized (LocalFileCert.class) {
            if (sTrustManagers != null) {
                return sTrustManagers;
            }
            try {
                try {
                    trustStore = KeyStore.getInstance(KEY_STORE_TYPE_BKS);
                    InputStream tsIn = context.getResources().getAssets().open(KEY_STORE_TRUST_PATH);
                    try {
                        try {
                            trustStore.load(tsIn, "chengzi".toCharArray());
                            tsIn.close();
                        } catch (Throwable th) {
                            try {
                                tsIn.close();
                            } catch (Exception e) {
                            }
                            throw th;
                        }
                    } catch (Exception e2) {
                        InitEventHolder.get().onInitException(HttpInitEventListener.CODE_FAILED_INIT_LOCAL_TRUST_MGR, e2.getLocalizedMessage());
                        e2.printStackTrace();
                        tsIn.close();
                    }
                } catch (Exception e3) {
                    InitEventHolder.get().onInitException(HttpInitEventListener.CODE_FAILED_INIT_LOCAL_TRUST_MGR, e3.getLocalizedMessage());
                    e3.printStackTrace();
                    return null;
                }
            } catch (Exception e4) {
            }
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(trustStore);
            sTrustManagers = tmf.getTrustManagers();
            return sTrustManagers;
        }
    }

    private static synchronized KeyManager[] getKeyManagers(Context context) {
        String certContent;
        synchronized (LocalFileCert.class) {
            if (sKeyManagers != null) {
                return sKeyManagers;
            }
            try {
                if (SecurityCommon.checkSystemUid(context)) {
                    LogUtils.i(TAG, "get cert content from file");
                    certContent = FileUtils.readTextFile(new File(CERT_PATH), 0, null);
                } else {
                    LogUtils.i(TAG, "get cert content from SystemDelegate");
                    ISystemDelegate systemDelegate = (ISystemDelegate) Module.get(SystemDelegateModuleEntry.class).get(ISystemDelegate.class);
                    certContent = systemDelegate.getCertificate();
                }
                if (!TextUtils.isEmpty(certContent)) {
                    KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE_P12);
                    InputStream ksIn = new ByteArrayInputStream(Base64.decode(certContent, 0));
                    try {
                        try {
                            try {
                                keyStore.load(ksIn, "chengzi".toCharArray());
                                ksIn.close();
                            } catch (Exception e) {
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            ksIn.close();
                        }
                        KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
                        kmf.init(keyStore, "chengzi".toCharArray());
                        sKeyManagers = kmf.getKeyManagers();
                        return sKeyManagers;
                    } catch (Throwable th) {
                        try {
                            ksIn.close();
                        } catch (Exception e3) {
                        }
                        throw th;
                    }
                }
                throw new RuntimeException("certContent can't be empty");
            } catch (Exception e4) {
                LogUtils.w(TAG, "getKeyManagers error!", e4);
                return null;
            }
        }
    }

    public static SSLSocketFactory getTLS2SocketFactory(Context context) {
        try {
            SSLContext sslContext = SSLContext.getInstance(XmartV1Constants.TLS_REVISION_1_2);
            sslContext.init(getKeyManagers(context), getTrustManagers(context), new SecureRandom());
            return new SSLHelper.TLS2SocketFactory(sslContext.getSocketFactory());
        } catch (Exception e) {
            InitEventHolder.get().onInitException(HttpInitEventListener.CODE_FAILED_INIT_LOCAL_SSL_FACTORY, e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static X509TrustManager getX509TrustManager(Context context) {
        try {
            TrustManager[] trustManagers = getTrustManagers(context);
            if (trustManagers != null) {
                for (TrustManager trustManager : trustManagers) {
                    if (trustManager instanceof X509TrustManager) {
                        return (X509TrustManager) trustManager;
                    }
                }
                return null;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
