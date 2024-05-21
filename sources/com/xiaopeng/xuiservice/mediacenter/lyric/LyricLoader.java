package com.xiaopeng.xuiservice.mediacenter.lyric;

import android.app.ActivityThread;
import android.text.TextUtils;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IResponse;
import com.xiaopeng.xuimanager.mediacenter.MediaInfo;
import com.xiaopeng.xuimanager.mediacenter.lyric.LyricInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.contextinfo.IHttpManager;
import com.xiaopeng.xuiservice.mediacenter.Constants;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes5.dex */
public class LyricLoader {
    private static final int MAX_CACHE_SIZE = 10;
    private static final String TAG = "LyricLoader";
    private static LyricLoader mInstance;
    private static String mRequestUri = Constants.LyricRequest.getMusicMatchUri();
    private List<LyricInfo> mCacheLyricList = new ArrayList();

    /* loaded from: classes5.dex */
    public interface LoadCallback {
        void onLoadFailed(String str);

        void onLoadSuccess(LyricInfo lyricInfo);
    }

    private LyricLoader() {
        IHttpManager.init(ActivityThread.currentActivityThread().getApplication());
    }

    public static LyricLoader instance() {
        if (mInstance == null) {
            synchronized (LyricLoader.class) {
                if (mInstance == null) {
                    mInstance = new LyricLoader();
                }
            }
        }
        return mInstance;
    }

    public void loadLyric(final MediaInfo info, final LoadCallback callback) {
        LogUtil.d(TAG, "loadLyric with mediaInfo:" + info.toString());
        if (!TextUtils.isEmpty(info.getTitle()) && !TextUtils.isEmpty(info.getArtist())) {
            LyricInfo cachedInfo = getCachedLyric(info);
            if (cachedInfo != null) {
                if (callback != null) {
                    callback.onLoadSuccess(cachedInfo);
                    return;
                }
                return;
            }
            IHttpManager.getInstance().getHttp().bizHelper().get(mRequestUri).build().params("songName", info.getTitle()).params(Constants.LyricRequest.KEY_QUERY_PARAMS_ARTIST, info.getArtist()).execute(new Callback() { // from class: com.xiaopeng.xuiservice.mediacenter.lyric.LyricLoader.1
                @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback
                public void onSuccess(IResponse iResponse) {
                    String body = iResponse == null ? "" : iResponse.body();
                    LogUtil.d(LyricLoader.TAG, "loadLyric onSuccess body: " + body);
                    LyricInfo lyricInfo = LyricUtil.parseLyricResponse(iResponse);
                    if (callback != null && LyricLoader.this.isCorrectLyric(info, lyricInfo)) {
                        callback.onLoadSuccess(lyricInfo);
                        LyricLoader.this.cacheLyricInfo(lyricInfo);
                    }
                }

                @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback
                public void onFailure(IResponse iResponse) {
                    LogUtil.d(LyricLoader.TAG, "loadLyric failed:" + iResponse.getException());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCorrectLyric(MediaInfo mediaInfo, LyricInfo lyricInfo) {
        return (mediaInfo == null || lyricInfo == null || TextUtils.isEmpty(mediaInfo.getTitle()) || !mediaInfo.getTitle().equals(lyricInfo.getSongName())) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cacheLyricInfo(LyricInfo lyricInfo) {
        this.mCacheLyricList.add(lyricInfo);
        if (this.mCacheLyricList.size() > 10) {
            this.mCacheLyricList.remove(0);
        }
    }

    private LyricInfo getCachedLyric(MediaInfo mediaInfo) {
        synchronized (this.mCacheLyricList) {
            if (!this.mCacheLyricList.isEmpty()) {
                for (LyricInfo info : this.mCacheLyricList) {
                    if (mediaInfo.getTitle().equals(info.getSongName()) && mediaInfo.getArtist().equals(info.getArtistName())) {
                        return info;
                    }
                }
            }
            return null;
        }
    }
}
