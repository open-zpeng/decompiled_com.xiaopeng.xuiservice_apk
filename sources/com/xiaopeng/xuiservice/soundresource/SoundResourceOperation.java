package com.xiaopeng.xuiservice.soundresource;

import com.xiaopeng.xuimanager.XUIManager;
import com.xiaopeng.xuimanager.operation.IOperationListener;
import com.xiaopeng.xuimanager.operation.OperationManager;
import com.xiaopeng.xuimanager.operation.OperationResource;
import com.xiaopeng.xuimanager.soundresource.data.BootSoundResource;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.soundresource.SoundResourceLoader;
import com.xiaopeng.xuiservice.soundresource.SoundResourceOperation;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes5.dex */
public class SoundResourceOperation {
    private static final String BOOT_SOUND_FOLDER = "/bootsound";
    private static final String TAG = SoundResourceOperation.class.getSimpleName();
    private SoundResourceLoader.ResourceListener mListener;
    private IOperationListener mOperationListener;
    private OperationManager mOperationManager;
    private boolean moduleInit;

    /* synthetic */ SoundResourceOperation(AnonymousClass1 x0) {
        this();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xiaopeng.xuiservice.soundresource.SoundResourceOperation$1  reason: invalid class name */
    /* loaded from: classes5.dex */
    public class AnonymousClass1 implements IOperationListener {
        AnonymousClass1() {
        }

        public void onOperationSourceAdd(int type, final OperationResource operationResource) {
            String str = SoundResourceOperation.TAG;
            LogUtil.i(str, "onOperationSourceAdd,type=" + type + ",res=" + operationResource);
            XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.soundresource.-$$Lambda$SoundResourceOperation$1$dwpC7TwCV7V_YtwoxdIsbr6_4Wo
                @Override // java.lang.Runnable
                public final void run() {
                    SoundResourceOperation.AnonymousClass1.this.lambda$onOperationSourceAdd$0$SoundResourceOperation$1(operationResource);
                }
            });
        }

        public /* synthetic */ void lambda$onOperationSourceAdd$0$SoundResourceOperation$1(OperationResource operationResource) {
            SoundResourceOperation.this.loadOperationRes(operationResource);
        }

        public void onOperationSourceSelected(int type, OperationResource operationResource) {
            String str = SoundResourceOperation.TAG;
            LogUtil.i(str, "onOperationSourceSelected,res=" + operationResource);
            SoundResourceLoader.getInstance().setBootSoundResourceByOperationId(operationResource.getId());
        }

        public void onOperationSourceExpire(int type, OperationResource operationResource) {
            String str = SoundResourceOperation.TAG;
            LogUtil.i(str, "onOperationSourceExpire,type=" + type + ",res=" + operationResource);
            SoundResourceLoader.getInstance().restoreToLastBootSound(operationResource.getId());
        }

        public void onOperationSourceDelete(int type, OperationResource operationResource) {
            String str = SoundResourceOperation.TAG;
            LogUtil.i(str, "onOperationSourceDelete,type=" + type + ",res=" + operationResource);
        }

        public void onRemoteSourceQuerySuccess(int type, List<OperationResource> list) {
            String str = SoundResourceOperation.TAG;
            LogUtil.i(str, "onRemoteSourceQuerySuccess,type=" + type + ",list=" + list);
        }
    }

    /* loaded from: classes5.dex */
    private static class SoundResourceOperationHolder {
        private static final SoundResourceOperation instance = new SoundResourceOperation(null);

        private SoundResourceOperationHolder() {
        }
    }

    private SoundResourceOperation() {
        this.mListener = null;
        this.mOperationManager = null;
        this.moduleInit = false;
        this.mOperationListener = new AnonymousClass1();
    }

    public static SoundResourceOperation getInstance() {
        return SoundResourceOperationHolder.instance;
    }

    public void init() {
        if (BroadcastManager.isBootComplete()) {
            XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.soundresource.-$$Lambda$SoundResourceOperation$bAcEPKXliuM9BjLdDvqlSNGsvAM
                @Override // java.lang.Runnable
                public final void run() {
                    SoundResourceOperation.this.lambda$init$0$SoundResourceOperation();
                }
            }, 2000L);
        }
        BroadcastManager.getInstance().addBootCompleteTask(new Runnable() { // from class: com.xiaopeng.xuiservice.soundresource.-$$Lambda$SoundResourceOperation$_dLCAVqGpW_JAmPGMuS4Z0-MvDg
            @Override // java.lang.Runnable
            public final void run() {
                SoundResourceOperation.this.lambda$init$2$SoundResourceOperation();
            }
        });
    }

    public /* synthetic */ void lambda$init$2$SoundResourceOperation() {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.soundresource.-$$Lambda$SoundResourceOperation$Y_c9_pi8w228-Rfuh6w86Z6BsEs
            @Override // java.lang.Runnable
            public final void run() {
                SoundResourceOperation.this.lambda$init$1$SoundResourceOperation();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: doOperationInit */
    public synchronized void lambda$init$1$SoundResourceOperation() {
        if (this.moduleInit) {
            LogUtil.w(TAG, "doOperationInit, already init");
            return;
        }
        this.moduleInit = true;
        this.mOperationManager = (OperationManager) XUIManager.getInstance().getService("operation");
        if (this.mOperationManager == null) {
            LogUtil.w(TAG, "get OperationManager null!");
            return;
        }
        this.mOperationManager.registerListener(1080300, this.mOperationListener);
        final List<OperationResource> resourceList = this.mOperationManager.getResourceList(1080300);
        String str = TAG;
        LogUtil.d(str, "doOperationInit, find resource:" + resourceList);
        if (resourceList != null && !resourceList.isEmpty()) {
            XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.soundresource.-$$Lambda$SoundResourceOperation$np2_re4aD-eNHZuOHSlBIieyJC8
                @Override // java.lang.Runnable
                public final void run() {
                    SoundResourceOperation.this.lambda$doOperationInit$3$SoundResourceOperation(resourceList);
                }
            });
        }
    }

    public /* synthetic */ void lambda$doOperationInit$3$SoundResourceOperation(List resourceList) {
        Iterator it = resourceList.iterator();
        while (it.hasNext()) {
            OperationResource res = (OperationResource) it.next();
            loadOperationRes(res);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadOperationRes(OperationResource res) {
        if (res == null) {
            LogUtil.w(TAG, "loadOperationRes, null res");
            return;
        }
        long currentTime = System.currentTimeMillis();
        if (res.getEffectTime() > currentTime) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String curStr = sdf.format(Long.valueOf(currentTime));
            String effectStr = sdf.format(Long.valueOf(res.getEffectTime()));
            String str = TAG;
            LogUtil.w(str, "loadOperationRes, res is not in time,current:" + curStr + ",effect start:" + effectStr);
        } else if (res.getExpireTime() > 0 && res.getExpireTime() <= currentTime) {
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String curStr2 = sdf2.format(Long.valueOf(currentTime));
            String expireStr = sdf2.format(Long.valueOf(res.getExpireTime()));
            String str2 = TAG;
            LogUtil.w(str2, "loadOperationRes, res is over time,current:" + curStr2 + ",expire:" + expireStr);
        } else {
            List<BootSoundResource> bootSoundResourceList = SoundResourceLoader.loadBootRscFromDisk(res.getTargetPath() + BOOT_SOUND_FOLDER);
            String str3 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("loadOperationRes,count=");
            sb.append(bootSoundResourceList == null ? 0 : bootSoundResourceList.size());
            LogUtil.i(str3, sb.toString());
            if (bootSoundResourceList != null && !bootSoundResourceList.isEmpty()) {
                SoundResourceLoader.getInstance().setBootSoundResourceOperationIdMap(res.getId(), bootSoundResourceList.get(0).getResId());
                SoundResourceLoader.getInstance().tryActiveBootSoundResourceFromOperation();
            }
        }
    }
}
