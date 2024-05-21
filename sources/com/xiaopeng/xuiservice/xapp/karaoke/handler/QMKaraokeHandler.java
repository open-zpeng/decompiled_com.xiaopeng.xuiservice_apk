package com.xiaopeng.xuiservice.xapp.karaoke.handler;

import android.app.ActivityThread;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import org.opencv.videoio.Videoio;
/* loaded from: classes5.dex */
public class QMKaraokeHandler implements IKaraokeHandler {
    private static final String TAG = "QMKaraokeHandler";
    private static final String URI_SEARCH_CONTENT = "tlkg://main/custom@custom=search&content=";
    private static final String URI_SEARCH_SINGER = "tlkg://main/custom@custom=search_singer&content=";

    @Override // com.xiaopeng.xuiservice.xapp.karaoke.handler.IKaraokeHandler
    public void searchSong(int displayId, String artist, String song) {
        String data = URI_SEARCH_CONTENT + artist + song;
        if (TextUtils.isEmpty(song)) {
            data = URI_SEARCH_SINGER + artist;
        }
        Uri uri = Uri.parse(data);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setScreenId(displayId);
        intent.setData(uri);
        intent.putExtra("resizeable", false);
        intent.setFlags(Videoio.CAP_INTELPERC_IMAGE_GENERATOR);
        ActivityThread.currentActivityThread().getApplication().startActivity(intent);
    }

    @Override // com.xiaopeng.xuiservice.xapp.karaoke.handler.IKaraokeHandler
    public String getPackageName() {
        return "com.tencent.wecar.karaoke";
    }
}
