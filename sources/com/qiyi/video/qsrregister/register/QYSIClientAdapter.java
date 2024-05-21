package com.qiyi.video.qsrregister.register;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.qiyi.video.client.IQYSManager;
import com.qiyi.video.client.IQYSService;
import com.qiyi.video.qsrbase.QYSExecResult;
import com.qiyi.video.qsrbase.QYSExecType;
import com.qiyi.video.qsrbase.QYSLog;
import com.qiyi.video.qsrbase.QYSVideoInfo;
import com.qiyi.video.qsrregister.listener.QYSListener;
import com.qiyi.video.qys.IQYSRequest;
import com.qiyi.video.qys.IQYSResponse;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes4.dex */
public class QYSIClientAdapter implements QYSIClient {
    private final Context mContext;
    private QYSListener mListener;

    public QYSIClientAdapter(Context context) {
        this.mContext = context;
    }

    public void register(@NotNull QYSListener listener) {
        this.mListener = listener;
        IQYSManager.getInstance().register(this.mContext, new IQYSManager.RegisterListener() { // from class: com.qiyi.video.qsrregister.register.QYSIClientAdapter.1
            @Override // com.qiyi.video.client.IQYSManager.RegisterListener
            public void onRegisterSuccess(IQYSService iqysService) {
                QYSListener l = QYSIClientAdapter.this.mListener;
                if (l != null) {
                    l.onQYSExecCallback(QYSExecType.BIND, QYSExecResult.RESULT_OK, null);
                }
            }

            @Override // com.qiyi.video.client.IQYSManager.RegisterListener
            public void onRegisterFailure(int code, String msg) {
                QYSListener l = QYSIClientAdapter.this.mListener;
                if (l != null) {
                    l.onQYSExecCallback(QYSExecType.BIND, QYSExecResult.RESULT_ERROR, null);
                }
            }
        });
    }

    public void unregister() {
        this.mListener = null;
        IQYSManager.getInstance().unRegister();
    }

    public void execScheme(@NotNull String scheme) {
        IQYSManager.getInstance().getService().executeAsync(new IQYSRequest(scheme), new IQYSService.Callback() { // from class: com.qiyi.video.qsrregister.register.QYSIClientAdapter.2
            @Override // com.qiyi.video.client.IQYSService.Callback
            public void onResponse(IQYSResponse response) {
                QYSLog.d("QYSIClient", "code=" + response.mCode + " ,msg=" + response.mMsg + " ,result" + response.mResult);
                QYSListener listener = QYSIClientAdapter.this.mListener;
                if (listener != null) {
                    QYSExecType type = QYSIClientAdapter.this.getQYSExecType(response);
                    if (response.mCode == 200) {
                        listener.onQYSExecCallback(type, QYSExecResult.RESULT_OK, QYSIClientAdapter.this.convert(response, type));
                    } else {
                        listener.onQYSExecCallback(type, QYSExecResult.RESULT_ERROR, null);
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public QYSExecType getQYSExecType(IQYSResponse response) {
        try {
            String cmd = response.mRequest.mCmd.get();
            Uri parse = Uri.parse(cmd);
            String command = parse.getQueryParameter("command");
            QYSExecType type = TextUtils.isEmpty(command) ? QYSExecType.UNKNOWN : QYSExecType.valueOf(command);
            return type;
        } catch (Throwable th) {
            QYSExecType type2 = QYSExecType.UNKNOWN;
            return type2;
        }
    }

    private String getParam(IQYSResponse response) {
        try {
            String cmd = response.mRequest.mCmd.get();
            Uri parse = Uri.parse(cmd);
            return parse.getQueryParameter("param");
        } catch (Throwable th) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<QYSVideoInfo> convert(IQYSResponse response, QYSExecType type) {
        if (type != QYSExecType.SEARCH) {
            return null;
        }
        String json = response.mResult.get();
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            JSONObject object = new JSONObject(json);
            List<QYSVideoInfo> list = new ArrayList<>();
            JSONArray arrays = object.optJSONArray("epgs");
            getParam(response);
            for (int i = 0; i < arrays.length(); i++) {
                JSONObject obj = arrays.optJSONObject(i);
                String name = obj.optString("name");
                String aid = obj.optString("albumId");
                String tvid = obj.optString("qipuId");
                String imgUrl = obj.optString("pic");
                long duration = obj.optLong("len", 0L);
                QYSVideoInfo info = new QYSVideoInfo(name, duration, 0L, aid, tvid, imgUrl);
                list.add(info);
            }
            return list;
        } catch (JSONException e) {
            return null;
        }
    }
}
