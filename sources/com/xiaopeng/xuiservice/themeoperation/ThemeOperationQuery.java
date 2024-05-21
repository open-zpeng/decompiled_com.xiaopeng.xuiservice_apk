package com.xiaopeng.xuiservice.themeoperation;

import com.xiaopeng.xuimanager.XUIManager;
import com.xiaopeng.xuimanager.operation.IOperationListener;
import com.xiaopeng.xuimanager.operation.OperationManager;
import com.xiaopeng.xuimanager.operation.OperationResource;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.util.List;
/* loaded from: classes5.dex */
public class ThemeOperationQuery {
    private static final String TAG = ThemeOperationQuery.class.getSimpleName();
    private IOperationListener operationListener;
    private OperationManager operationManager;

    /* loaded from: classes5.dex */
    private static class ThemeOperationQueryHolder {
        private static final ThemeOperationQuery instance = new ThemeOperationQuery();

        private ThemeOperationQueryHolder() {
        }
    }

    private ThemeOperationQuery() {
        this.operationListener = null;
    }

    public static ThemeOperationQuery getInstance() {
        return ThemeOperationQueryHolder.instance;
    }

    public void init() {
        LogUtil.d(TAG, "init");
        XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.themeoperation.-$$Lambda$ThemeOperationQuery$tG06jMM2W-jTCZEvBu83HeHvz2s
            @Override // java.lang.Runnable
            public final void run() {
                ThemeOperationQuery.this.lambda$init$1$ThemeOperationQuery();
            }
        }, 3000L);
    }

    public /* synthetic */ void lambda$init$1$ThemeOperationQuery() {
        this.operationManager = (OperationManager) XUIManager.getInstance().getService("operation");
        this.operationManager.registerListener(1080100, this.operationListener);
        if (BroadcastManager.isBootComplete()) {
            lambda$init$0$ThemeOperationQuery();
        } else {
            BroadcastManager.getInstance().addBootCompleteTask(new Runnable() { // from class: com.xiaopeng.xuiservice.themeoperation.-$$Lambda$ThemeOperationQuery$aBnZaQfd4CHO-8hzXPpj8PvCyfc
                @Override // java.lang.Runnable
                public final void run() {
                    ThemeOperationQuery.this.lambda$init$0$ThemeOperationQuery();
                }
            });
        }
    }

    public void setListener(IOperationListener listener) {
        this.operationListener = listener;
    }

    /* renamed from: doRemoteQuery */
    public void lambda$init$0$ThemeOperationQuery() {
        LogUtil.d(TAG, "doRemoteQuery");
        if (!checkManagerAlive()) {
            return;
        }
        this.operationManager.getRemoteResourceList(1080100);
    }

    public List<OperationResource> getResourceList(int type) {
        if (!checkManagerAlive()) {
            return null;
        }
        return this.operationManager.getResourceList(type);
    }

    public void downloadResource(OperationResource resource) {
        if (!checkManagerAlive()) {
            return;
        }
        this.operationManager.downloadResource(false, resource);
    }

    public void deleteResource(OperationResource resource) {
        if (!checkManagerAlive()) {
            return;
        }
        this.operationManager.deleteResource(resource.getId());
    }

    private boolean checkManagerAlive() {
        if (this.operationManager == null) {
            LogUtil.w(TAG, "checkManagerAlive fail");
            return false;
        }
        return true;
    }
}
