package com.xiaopeng.xuiservice;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.alipay.mobile.aromeservice.ResponseParams;
import com.android.internal.annotations.GuardedBy;
import com.xiaopeng.xuimanager.IXUIService;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.IXUIServiceImpl;
import com.xiaopeng.xuiservice.Manifest;
import com.xiaopeng.xuiservice.awareness.AwarenessService;
import com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightService;
import com.xiaopeng.xuiservice.capabilities.ambientlight.XpAmbientLightProxy;
import com.xiaopeng.xuiservice.capabilities.llu.XpLightlanuageProxy;
import com.xiaopeng.xuiservice.capabilities.makeup.XpMakeupLightProxy;
import com.xiaopeng.xuiservice.capabilities.smm.XpSeatMassagerProxy;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.condition.ConditionService;
import com.xiaopeng.xuiservice.contextinfo.ContextInfoService;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import com.xiaopeng.xuiservice.download.download.DownloadListenerBinder;
import com.xiaopeng.xuiservice.mediacenter.MediaCenterService;
import com.xiaopeng.xuiservice.message.PushMessageService;
import com.xiaopeng.xuiservice.musicrecognize.MusicRecognizeService;
import com.xiaopeng.xuiservice.operation.OperationService;
import com.xiaopeng.xuiservice.smart.SmartService;
import com.xiaopeng.xuiservice.smart.action.Actions;
import com.xiaopeng.xuiservice.soundresource.SoundResourceService;
import com.xiaopeng.xuiservice.themeoperation.ThemeOperationService;
import com.xiaopeng.xuiservice.userscenario.UserScenarioService;
import com.xiaopeng.xuiservice.utils.NativeCallHelper;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import com.xiaopeng.xuiservice.xapp.XAppService;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes5.dex */
public class IXUIServiceImpl extends IXUIService.Stub {
    @GuardedBy({"this"})
    private final XUIServiceBase[] mAllServices;
    private AmbientLightService mAmbientLightService;
    private XpAmbientLightProxy mAmbientService;
    private ConditionService mConditionService;
    private final Context mContext;
    private ContextInfoService mContextInfoService;
    private KaraokeManagerService mKaraokeManagerService;
    private XpLightlanuageProxy mLightlanuageService;
    private XpMakeupLightProxy mMakeupLightService;
    private MediaCenterService mMediaCenterService;
    private MusicRecognizeService mMusicRecognizeService;
    private OperationService mOperationService;
    private PushMessageService mPushMessageService;
    private XpSeatMassagerProxy mSeatMassagerService;
    private SmartService mSmartService;
    private SoundResourceService mSoundResourceService;
    private ThemeOperationService mThemeOperationService;
    private UserScenarioService mUserScenarioService;
    private XAppService mXAppService;
    private XUIConfig mXUIConfig;
    @GuardedBy({"IXUIServiceImpl.class"})
    private static IXUIServiceImpl sInstance = null;
    private static final String TAG = IXUIServiceImpl.class.getSimpleName();
    private DownloadListenerBinder mDownloadListenerBinder = null;
    private AwarenessService mAwarenessService = null;
    private final Map<IBinder, ListenerRecord> mListenerMap = new HashMap();
    private List<ClientObserver> mClientObserverList = new ArrayList();

    /* loaded from: classes5.dex */
    public interface ClientObserver {
        void clientDied(int i, int i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public final class ListenerRecord implements IBinder.DeathRecipient {
        private final String TAG = "IXUIServiceImpl";
        public final IBinder listener;
        public final int pid;
        public final int uid;

        ListenerRecord(IBinder listener, int pid, int uid) {
            this.listener = listener;
            this.pid = pid;
            this.uid = uid;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            LogUtil.w("IXUIServiceImpl", "binder died,pid=" + this.pid);
            release();
            synchronized (IXUIServiceImpl.this.mListenerMap) {
                IXUIServiceImpl.this.mListenerMap.remove(this.listener);
            }
            XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.-$$Lambda$IXUIServiceImpl$ListenerRecord$j4BeQS7VFuhJUMF-3JxpkJkakBU
                @Override // java.lang.Runnable
                public final void run() {
                    IXUIServiceImpl.ListenerRecord.this.lambda$binderDied$0$IXUIServiceImpl$ListenerRecord();
                }
            });
        }

        public /* synthetic */ void lambda$binderDied$0$IXUIServiceImpl$ListenerRecord() {
            synchronized (IXUIServiceImpl.this.mClientObserverList) {
                for (ClientObserver observer : IXUIServiceImpl.this.mClientObserverList) {
                    observer.clientDied(this.pid, this.uid);
                }
            }
        }

        public void release() {
            try {
                this.listener.unlinkToDeath(this, 0);
            } catch (Exception e) {
                LogUtil.w("IXUIServiceImpl", "ListenerRecord release e=" + e);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x00d8, code lost:
        if (com.xiaopeng.xuiservice.XUIConfig.hasFeature(com.xiaopeng.xuiservice.XUIConfig.PROPERTY_SVM) != false) goto L25;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public IXUIServiceImpl() {
        /*
            Method dump skipped, instructions count: 377
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xuiservice.IXUIServiceImpl.<init>():void");
    }

    public static synchronized IXUIServiceImpl getInstance(Context serviceContext) {
        IXUIServiceImpl iXUIServiceImpl;
        synchronized (IXUIServiceImpl.class) {
            if (sInstance == null) {
                sInstance = new IXUIServiceImpl();
                sInstance.init();
            }
            iXUIServiceImpl = sInstance;
        }
        return iXUIServiceImpl;
    }

    public static synchronized IXUIServiceImpl getInstance() {
        IXUIServiceImpl iXUIServiceImpl;
        synchronized (IXUIServiceImpl.class) {
            if (sInstance == null) {
                sInstance = new IXUIServiceImpl();
                sInstance.init();
            }
            iXUIServiceImpl = sInstance;
        }
        return iXUIServiceImpl;
    }

    public static synchronized void releaseInstance() {
        synchronized (IXUIServiceImpl.class) {
            if (sInstance == null) {
                return;
            }
            sInstance.release();
            sInstance = null;
        }
    }

    public void addClientObserver(ClientObserver observer) {
        synchronized (this.mClientObserverList) {
            this.mClientObserverList.add(observer);
        }
    }

    public static void assertPermission(Context context, String permission) {
    }

    private boolean isDownloadProviderInstalled(Context context) {
        return isPackageInstalled(context, "com.android.providers.downloads");
    }

    private boolean isPackageInstalled(Context context, String packageName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                packageManager.getPackageInfo(packageName, 0);
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }

    private void init() {
        XUIServiceBase[] xUIServiceBaseArr;
        for (XUIServiceBase service : this.mAllServices) {
            if (service != null) {
                service.init();
            }
        }
        CarClientManager.getInstance().init();
    }

    private void release() {
        for (int i = this.mAllServices.length - 1; i >= 0; i--) {
            XUIServiceBase[] xUIServiceBaseArr = this.mAllServices;
            if (xUIServiceBaseArr[i] != null) {
                xUIServiceBaseArr[i].release();
            }
        }
        CarClientManager.getInstance().release();
    }

    private void reinitServices() {
        XUIServiceBase[] xUIServiceBaseArr;
        for (int i = this.mAllServices.length - 1; i >= 0; i--) {
            XUIServiceBase[] xUIServiceBaseArr2 = this.mAllServices;
            if (xUIServiceBaseArr2[i] != null) {
                xUIServiceBaseArr2[i].release();
            }
        }
        for (XUIServiceBase service : this.mAllServices) {
            if (service != null) {
                service.init();
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public IBinder getXUIService(String serviceName) {
        char c;
        switch (serviceName.hashCode()) {
            case -1608162213:
                if (serviceName.equals("userscenario")) {
                    c = 14;
                    break;
                }
                c = 65535;
                break;
            case -1330509705:
                if (serviceName.equals("sndresource")) {
                    c = 16;
                    break;
                }
                c = 65535;
                break;
            case -1250402432:
                if (serviceName.equals("xpdownload")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -936045084:
                if (serviceName.equals(XUIConfig.PROPERTY_KARAOKE)) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case -892145000:
                if (serviceName.equals(Actions.ACTION_AMBIENT)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -861311717:
                if (serviceName.equals("condition")) {
                    c = 17;
                    break;
                }
                c = 65535;
                break;
            case -601874003:
                if (serviceName.equals("makeuplight")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case -452142163:
                if (serviceName.equals("lightlanuage")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case -102225187:
                if (serviceName.equals(XUIConfig.PROPERTY_CONTEXTINFO)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 3671721:
                if (serviceName.equals(XUIConfig.PROPERTY_XAPP)) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 45747532:
                if (serviceName.equals("seatmassager")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 106938681:
                if (serviceName.equals(XUIConfig.PROPERTY_MEDIACENTER)) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 109549001:
                if (serviceName.equals(XUIConfig.PROPERTY_SMART)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 649601406:
                if (serviceName.equals("ambientlight")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 954925063:
                if (serviceName.equals(ResponseParams.RESPONSE_KEY_MESSAGE)) {
                    c = 18;
                    break;
                }
                c = 65535;
                break;
            case 1260496309:
                if (serviceName.equals(XUIConfig.PROPERTY_AWARENESS)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1662702951:
                if (serviceName.equals("operation")) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            case 1727280137:
                if (serviceName.equals(XUIConfig.PROPERTY_MUSICRECOGNIZE)) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case 1883558334:
                if (serviceName.equals("themeoperation")) {
                    c = 15;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return this.mDownloadListenerBinder;
            case 1:
                assertPermission(this.mContext, Manifest.permission.XUI_CONTEXTINFO);
                return this.mContextInfoService;
            case 2:
                assertPermission(this.mContext, Manifest.permission.XUI_SMART);
                return this.mSmartService;
            case 3:
                assertPermission(this.mContext, Manifest.permission.XUI_AWARENESS);
                return this.mAwarenessService;
            case 4:
                assertPermission(this.mContext, Manifest.permission.XUI_AMBIENTLIGHT);
                return this.mAmbientLightService;
            case 5:
                assertPermission(this.mContext, Manifest.permission.XUI_AMBIENTLIGHT);
                return this.mAmbientService;
            case 6:
                assertPermission(this.mContext, Manifest.permission.XUI_LIGHTLANUAGE);
                return this.mLightlanuageService;
            case 7:
                assertPermission(this.mContext, Manifest.permission.XUI_SEATMASSAGER);
                return this.mSeatMassagerService;
            case '\b':
                assertPermission(this.mContext, Manifest.permission.XUI_MAKEUPLIGHT);
                return this.mMakeupLightService;
            case '\t':
                assertPermission(this.mContext, Manifest.permission.XUI_MEDIACENTER);
                return this.mMediaCenterService;
            case '\n':
                assertPermission(this.mContext, Manifest.permission.XUI_MUSICRECOGNIZE);
                return this.mMusicRecognizeService;
            case 11:
                assertPermission(this.mContext, Manifest.permission.XUI_XAPP);
                return this.mXAppService;
            case '\f':
                assertPermission(this.mContext, Manifest.permission.XUI_KARAOKE);
                return this.mKaraokeManagerService;
            case '\r':
                assertPermission(this.mContext, Manifest.permission.XUI_OPERATION);
                return this.mOperationService;
            case 14:
                return this.mUserScenarioService;
            case 15:
                return this.mThemeOperationService;
            case 16:
                return this.mSoundResourceService;
            case 17:
                return this.mConditionService;
            case 18:
                return this.mPushMessageService;
            default:
                LogUtil.w(XUILog.TAG_SERVICE, "getXUIService for unknown service:" + serviceName);
                return null;
        }
    }

    public void nativeCall() {
    }

    public void registerObserver(IBinder binder) {
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        String str = TAG;
        LogUtil.i(str, "registerObserver,binder-" + binder + ",pid-" + pid + ",uid" + uid + ",observers-" + this.mListenerMap.size());
        synchronized (this.mListenerMap) {
            if (this.mListenerMap.containsKey(binder)) {
                String str2 = TAG;
                LogUtil.w(str2, "repeat observer-" + binder);
                return;
            }
            ListenerRecord record = new ListenerRecord(binder, pid, uid);
            try {
                binder.linkToDeath(record, 0);
                this.mListenerMap.put(binder, record);
            } catch (RemoteException e) {
                String str3 = TAG;
                LogUtil.w(str3, "registerObserver e=" + e);
            }
        }
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        if (2 == code) {
            NativeCallHelper.getInstance().nativeCall(data, reply);
            return true;
        }
        return super.onTransact(code, data, reply, flags);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public Object getService(String serviceName) {
        char c;
        switch (serviceName.hashCode()) {
            case -936045084:
                if (serviceName.equals(XUIConfig.PROPERTY_KARAOKE)) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case -892145000:
                if (serviceName.equals(Actions.ACTION_AMBIENT)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -861311717:
                if (serviceName.equals("condition")) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case -452142163:
                if (serviceName.equals("lightlanuage")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -102225187:
                if (serviceName.equals(XUIConfig.PROPERTY_CONTEXTINFO)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 3671721:
                if (serviceName.equals(XUIConfig.PROPERTY_XAPP)) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 45747532:
                if (serviceName.equals("seatmassager")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 106938681:
                if (serviceName.equals(XUIConfig.PROPERTY_MEDIACENTER)) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 109549001:
                if (serviceName.equals(XUIConfig.PROPERTY_SMART)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 649601406:
                if (serviceName.equals("ambientlight")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 954925063:
                if (serviceName.equals(ResponseParams.RESPONSE_KEY_MESSAGE)) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            case 1260496309:
                if (serviceName.equals(XUIConfig.PROPERTY_AWARENESS)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 1662702951:
                if (serviceName.equals("operation")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 1727280137:
                if (serviceName.equals(XUIConfig.PROPERTY_MUSICRECOGNIZE)) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return this.mContextInfoService;
            case 1:
                return this.mSmartService;
            case 2:
                return this.mAwarenessService;
            case 3:
                return this.mAmbientLightService;
            case 4:
                return this.mAmbientService;
            case 5:
                return this.mLightlanuageService;
            case 6:
                return this.mSeatMassagerService;
            case 7:
                return this.mMediaCenterService;
            case '\b':
                return this.mMusicRecognizeService;
            case '\t':
                return this.mXAppService;
            case '\n':
                return this.mKaraokeManagerService;
            case 11:
                return this.mOperationService;
            case '\f':
                return this.mConditionService;
            case '\r':
                return this.mPushMessageService;
            default:
                LogUtil.w(XUILog.TAG_SERVICE, "getService for unknown service:" + serviceName);
                return null;
        }
    }

    public MediaCenterService getMediaCenterService() {
        return this.mMediaCenterService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter writer) {
        XUIServiceBase[] xUIServiceBaseArr;
        writer.println("*Dump all services*");
        for (XUIServiceBase service : this.mAllServices) {
            if (service != null) {
                service.dump(writer);
            }
        }
    }

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println("dump@" + this);
        if (args != null) {
            for (int opti = 0; opti < args.length; opti++) {
                String str = args[opti];
                char c = 65535;
                int hashCode = str.hashCode();
                if (hashCode != 1504) {
                    if (hashCode == 214175961 && str.equals("-module")) {
                        c = 1;
                    }
                } else if (str.equals("-m")) {
                    c = 0;
                }
                if (c == 0 || c == 1) {
                    if (opti < args.length - 1) {
                        int opti2 = opti + 1;
                        String module = args[opti2];
                        DumpDispatcher.dumpModule(pw, module, args, opti2 + 1);
                        return;
                    }
                    pw.println("lack module name...");
                    return;
                }
                pw.println("unknown arg:" + args[opti]);
            }
        }
    }
}
