package com.xiaopeng.xuiservice.opensdk.manager;

import com.xiaopeng.speech.protocol.node.asrToText.AsrToTextListener;
import com.xiaopeng.speech.protocol.node.asrToText.AsrToTextNode;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.utils.PackageUtils;
import java.io.PrintWriter;
/* loaded from: classes5.dex */
public class VoiceManager extends BaseManager {
    private static final String TAG = VoiceManager.class.getSimpleName();
    private final AsrToTextListener asrToTextlistener;
    private IPipeListener mPipeListener;

    /* loaded from: classes5.dex */
    private static final class SingleInstanceHolder {
        private static final VoiceManager INSTANCE = new VoiceManager();

        private SingleInstanceHolder() {
        }
    }

    public static VoiceManager getInstance() {
        return SingleInstanceHolder.INSTANCE;
    }

    private VoiceManager() {
        this.asrToTextlistener = new AsrToTextListener() { // from class: com.xiaopeng.xuiservice.opensdk.manager.VoiceManager.1
            @Override // com.xiaopeng.speech.protocol.node.asrToText.AsrToTextListener
            public void onResult(String jsonResult) {
                String str = VoiceManager.TAG;
                LogUtil.i(str, "AsrToTextListener, onResult=" + jsonResult);
                if (VoiceManager.this.mPipeListener != null) {
                    VoiceManager.this.mPipeListener.onPipeBusNotify(com.car.opensdk.voice.VoiceManager.MODULE_NAME, "onResult", new String[]{jsonResult});
                }
            }
        };
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void init() {
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public int ioControl(String cmd, String[] params, int pid, int uid) {
        char c;
        switch (cmd.hashCode()) {
            case -1453910395:
                if (cmd.equals("voiceAsrStartRecord")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -877643761:
                if (cmd.equals("voiceAsrInitRecord")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 1138547613:
                if (cmd.equals("voiceAsrDestroyRecord")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1809276289:
                if (cmd.equals("voiceAsrStopRecord")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        if (c == 0) {
            AsrToTextNode.getInstance().initRecord(params[0], this.asrToTextlistener);
        } else if (c == 1) {
            AsrToTextNode.getInstance().startRecord(PackageUtils.getPackageName(pid), params[0]);
        } else if (c == 2) {
            AsrToTextNode.getInstance().stopRecord();
        } else if (c == 3) {
            AsrToTextNode.getInstance().destroyRecord();
        }
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public int ioControlWithPocket(String cmd, String[] params, String[] results, int pid, int uid) {
        return 0;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void addListener(IPipeListener listener) {
        this.mPipeListener = listener;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void removeListener() {
        this.mPipeListener = null;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public String getModuleName() {
        return com.car.opensdk.voice.VoiceManager.MODULE_NAME;
    }

    @Override // com.xiaopeng.xuiservice.opensdk.manager.BaseManager
    public void dump(PrintWriter pw, String[] args) {
    }
}
