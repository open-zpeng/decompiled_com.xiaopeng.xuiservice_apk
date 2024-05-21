package com.xiaopeng.xuiservice.xapp.speech;
/* loaded from: classes5.dex */
public class XAppSpeechService {
    private SpeechAppQueryModel mAppAndAppletQueryModel;
    private MediaQueryModel mMediaQueryModel;
    private SpeechMediaModel mSpeechMediaModel;
    private SpeechVideoModel mSpeechVideoModel;

    private XAppSpeechService() {
        this.mAppAndAppletQueryModel = new SpeechAppQueryModel();
        this.mSpeechVideoModel = new SpeechVideoModel();
        this.mSpeechMediaModel = new SpeechMediaModel();
        this.mMediaQueryModel = new MediaQueryModel();
    }

    public void start() {
        this.mAppAndAppletQueryModel.subscribe();
        this.mSpeechVideoModel.subscribe();
        this.mSpeechMediaModel.subscribe();
        this.mMediaQueryModel.subscribe();
    }

    public static XAppSpeechService getInstance() {
        return InstanceHolder.sService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class InstanceHolder {
        private static final XAppSpeechService sService = new XAppSpeechService();

        private InstanceHolder() {
        }
    }
}
