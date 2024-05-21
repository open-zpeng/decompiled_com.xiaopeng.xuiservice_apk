package com.xiaopeng.xuiservice.xapp.karaoke.handler;

import android.app.ActivityThread;
import android.content.Intent;
import android.net.Uri;
import androidx.exifinterface.media.ExifInterface;
import com.loostone.libtuning.channel.skyworth.old.ai.config.AiCmd;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.utils.BroadcastManager;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.xapp.Constants;
import org.opencv.videoio.Videoio;
/* loaded from: classes5.dex */
public class CBKaraokeHandler implements IKaraokeHandler {
    private static final String ACTION_CHANG_BA_CONTROL = "com.changba.tv.action.control";
    private static final String TAG = "CBKaraokeHandler";

    @Override // com.xiaopeng.xuiservice.xapp.karaoke.handler.IKaraokeHandler
    public void searchSong(int displayId, String artist, String song) {
        if (SharedDisplayManager.hasSharedDisplayFeature()) {
            SharedDisplayManager.getInstance().setSharedId(getPackageName(), displayId);
        }
        searchSongByScheme(artist, song);
    }

    private void searchSongByBroadcast(String artist, String song) {
        Intent intent = new Intent();
        intent.setAction(ACTION_CHANG_BA_CONTROL);
        intent.setPackage(getPackageName());
        intent.putExtra("Command", AiCmd.PLAY);
        intent.putExtra("Song", song);
        intent.putExtra(ExifInterface.TAG_ARTIST, artist);
        BroadcastManager.getInstance().sendBroadcast(intent);
    }

    private void searchSongByScheme(String artist, String song) {
        Intent intent = new Intent();
        Uri uri = new Uri.Builder().scheme("cbtv").authority("changba").appendPath("jump").appendQueryParameter("Command", AiCmd.PLAY).appendQueryParameter("Song", song).appendQueryParameter(ExifInterface.TAG_ARTIST, artist).build();
        LogUtil.d(TAG, "searchSongByScheme uri:" + uri.toString());
        intent.setData(uri);
        intent.putExtra("resizeable", false);
        intent.addFlags(Videoio.CAP_INTELPERC_IMAGE_GENERATOR);
        ActivityThread.currentActivityThread().getApplication().startActivity(intent);
    }

    @Override // com.xiaopeng.xuiservice.xapp.karaoke.handler.IKaraokeHandler
    public String getPackageName() {
        return Constants.PACKAGE_CHANGBA;
    }
}
