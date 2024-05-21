package com.xiaopeng.xuiservice.musicrecognize;

import com.xiaopeng.xuimanager.musicrecognize.MusicRecognizeEvent;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.HalServiceBase;
import com.xiaopeng.xuiservice.musicrecognize.IFindCover;
import com.xiaopeng.xuiservice.musicrecognize.MusicRecognizeEngine;
import java.io.PrintWriter;
/* loaded from: classes5.dex */
public class MusicRecognizeHalService extends HalServiceBase {
    private static final boolean DBG = true;
    private static final String TAG = "MusicRecognizeHalService";
    private IFindCover mFindCover;
    private MusicRecognizeHalListener mListener;
    private MusicRecognizeEngine mMusicRecognizeEngine;

    /* loaded from: classes5.dex */
    public interface MusicRecognizeHalListener {
        void onError(int i, int i2);

        void onFindCoverEvent(String str);

        void onRecognizeEvent(MusicRecognizeEvent musicRecognizeEvent);
    }

    public MusicRecognizeHalService() {
        LogUtil.d(TAG, "started MusicRecognizeHalService!");
    }

    public void setListener(MusicRecognizeHalListener listener) {
        LogUtil.d(TAG, "setListener(MusicRecognizeHalListener listener)");
        synchronized (this) {
            this.mListener = listener;
        }
        getMusicRecognizeEngine().setCallback(new MusicRecognizeEngine.Callback() { // from class: com.xiaopeng.xuiservice.musicrecognize.MusicRecognizeHalService.1
            @Override // com.xiaopeng.xuiservice.musicrecognize.MusicRecognizeEngine.Callback
            public void onSuccess(MusicRecognizeEvent musicRecognizeEvent) {
                MusicRecognizeHalService.this.stop();
                if (MusicRecognizeHalService.this.mListener != null) {
                    MusicRecognizeHalService.this.mListener.onRecognizeEvent(musicRecognizeEvent);
                }
            }

            @Override // com.xiaopeng.xuiservice.musicrecognize.MusicRecognizeEngine.Callback
            public void onVolumeChanged(double volume) {
                MusicRecognizeHalListener unused = MusicRecognizeHalService.this.mListener;
            }

            @Override // com.xiaopeng.xuiservice.musicrecognize.MusicRecognizeEngine.Callback
            public void onFail(String msg) {
                MusicRecognizeHalService.this.stop();
                MusicRecognizeHalListener unused = MusicRecognizeHalService.this.mListener;
            }
        });
    }

    @Override // com.xiaopeng.xuiservice.HalServiceBase
    public void init() {
        LogUtil.d(TAG, "init()");
    }

    @Override // com.xiaopeng.xuiservice.HalServiceBase
    public void release() {
        LogUtil.d(TAG, "release()");
        this.mListener = null;
    }

    public void handleHalEvents() {
        MusicRecognizeHalListener listener;
        synchronized (this) {
            listener = this.mListener;
        }
        if (listener != null) {
            dispatchEventToListener(listener);
        }
    }

    private void dispatchEventToListener(MusicRecognizeHalListener listener) {
        LogUtil.d(TAG, "handleHalEvents event: ");
    }

    @Override // com.xiaopeng.xuiservice.HalServiceBase
    public void dump(PrintWriter writer) {
        writer.println("*MusicRecognize HAL*");
    }

    public void start() {
        LogUtil.d(TAG, "handleHalEvents start()");
        getMusicRecognizeEngine().start();
    }

    public void stop() {
        getMusicRecognizeEngine().stop();
    }

    public void stopAndRecognize() {
        getMusicRecognizeEngine().stopAndRecognize();
    }

    public int getMode() {
        return getMusicRecognizeEngine().getMode();
    }

    public void setMode(int mode) {
        getMusicRecognizeEngine().setMode(mode);
    }

    public float getMinScore() {
        return getMusicRecognizeEngine().getMinScore();
    }

    public void setMinScore(float score) {
        getMusicRecognizeEngine().setMinScore(score);
    }

    public void findSongCover(String musicName) {
        getFindCover().findSongCover(musicName, new IFindCover.Callback() { // from class: com.xiaopeng.xuiservice.musicrecognize.MusicRecognizeHalService.2
            @Override // com.xiaopeng.xuiservice.musicrecognize.IFindCover.Callback
            public void findResult(String urlLink) {
                if (urlLink == null || "".equals(urlLink)) {
                    MusicRecognizeHalService.this.downgrade();
                }
                if (MusicRecognizeHalService.this.mListener != null) {
                    MusicRecognizeHalService.this.mListener.onFindCoverEvent(urlLink);
                }
            }
        });
    }

    private MusicRecognizeEngine getMusicRecognizeEngine() {
        if (this.mMusicRecognizeEngine == null) {
            LogUtil.d(TAG, "getMusicRecognizeEngine()");
            this.mMusicRecognizeEngine = MusicRecognizeEngineImpl.getInstance();
        }
        return this.mMusicRecognizeEngine;
    }

    private IFindCover getFindCover() {
        if (this.mFindCover == null) {
            this.mFindCover = new FindCoverBy163Impl();
        }
        return this.mFindCover;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void downgrade() {
        if (!(this.mFindCover instanceof FindCoverBy163Impl)) {
            this.mFindCover = new FindCoverBy163Impl();
        }
    }
}
