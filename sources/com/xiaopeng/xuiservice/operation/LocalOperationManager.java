package com.xiaopeng.xuiservice.operation;

import android.text.TextUtils;
import android.util.SparseArray;
import com.xiaopeng.xuimanager.operation.OperationResource;
import com.xiaopeng.xuimanager.operation.utils.ResourceBuild;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.operation.OperationHalService;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class LocalOperationManager implements OperationHalService.OperationHalListener {
    private static final String TAG = "LocalOperationManager";
    private static LocalOperationManager mInstance;
    private SparseArray<List<IEventListener>> mListenersSparseArray = new SparseArray<>();
    private OperationHalService mOperationHalService = OperationHalService.getInstance();

    /* loaded from: classes5.dex */
    public interface IEventListener {
        void onOperationSourceAdd(int i, OperationResource operationResource);

        void onOperationSourceDelete(int i, OperationResource operationResource);

        void onOperationSourceExpire(int i, OperationResource operationResource);

        void onRemoteSourceQuerySuccess(int i, List<OperationResource> list);
    }

    private LocalOperationManager() {
    }

    public static LocalOperationManager getInstance() {
        if (mInstance == null) {
            synchronized (LocalOperationManager.class) {
                if (mInstance == null) {
                    mInstance = new LocalOperationManager();
                }
            }
        }
        return mInstance;
    }

    public void setDownloadPath(int resourceType, String path) {
        this.mOperationHalService.setDownloadPath(resourceType, path);
    }

    public void registerListener(int type, IEventListener eventListener) {
        LogUtil.d(TAG, "registerListener type:" + type);
        synchronized (this.mListenersSparseArray) {
            List<IEventListener> listeners = this.mListenersSparseArray.get(type);
            if (listeners == null) {
                listeners = new ArrayList();
            }
            if (!listeners.contains(eventListener)) {
                listeners.add(eventListener);
                this.mListenersSparseArray.put(type, listeners);
            }
            if (this.mListenersSparseArray.size() > 0) {
                this.mOperationHalService.addEventListener(this);
            }
        }
    }

    public void unregisterListener(int type, IEventListener eventListener) {
        synchronized (this.mListenersSparseArray) {
            List<IEventListener> listeners = this.mListenersSparseArray.get(type);
            if (listeners != null && listeners.contains(eventListener)) {
                listeners.remove(eventListener);
                if (listeners.isEmpty()) {
                    this.mListenersSparseArray.remove(type);
                    if (this.mListenersSparseArray.size() == 0) {
                        this.mOperationHalService.removeEventListener(this);
                    }
                }
            }
        }
    }

    public String getResource(int type, String params) {
        return this.mOperationHalService.getResource(type, params);
    }

    @Override // com.xiaopeng.xuiservice.operation.OperationHalService.OperationHalListener
    public void onEvent(int code, String id, int type, String event) {
        LogUtil.d(TAG, "onEvent type:" + type + " & event:" + event);
        synchronized (this.mListenersSparseArray) {
            List<IEventListener> listeners = this.mListenersSparseArray.get(type);
            if (listeners != null && !listeners.isEmpty()) {
                OperationResource operationResource = getOperationResource(event);
                LogUtil.d(TAG, "onEvent:operationResource:" + operationResource);
                if (operationResource != null && code == 1) {
                    for (IEventListener listener : listeners) {
                        listener.onOperationSourceAdd(type, operationResource);
                    }
                }
            } else {
                LogUtil.d(TAG, "onEvent listeners is null");
            }
        }
    }

    private OperationResource getOperationResource(String event) {
        if (!TextUtils.isEmpty(event)) {
            try {
                JSONObject sourceObject = new JSONObject(event);
                OperationResource operationResource = ResourceBuild.fromJson(sourceObject);
                return operationResource;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override // com.xiaopeng.xuiservice.operation.OperationHalService.OperationHalListener
    public void onError(int errorCode, int operation) {
    }
}
