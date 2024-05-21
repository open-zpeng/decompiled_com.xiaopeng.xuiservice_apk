package com.xiaopeng.xuiservice.mediacenter.lyric;

import android.os.Bundle;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IResponse;
import com.xiaopeng.xuimanager.mediacenter.lyric.LyricInfo;
import com.xiaopeng.xuiservice.mediacenter.Constants;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class LyricUtil {
    private static final String TAG = "LyricUtil";

    public static LyricInfo parseLyricResponse(IResponse iResponse) {
        LyricInfo lyricInfo = new LyricInfo();
        if (iResponse != null) {
            try {
                JSONObject body = new JSONObject(iResponse.body());
                int code = body.optInt("code", 0);
                if (code == 200) {
                    JSONObject dataObject = body.getJSONObject("data");
                    String songName = dataObject.optString("songName", "");
                    String artistName = dataObject.optString(Constants.LyricRequest.KEY_RESPONSE_ARTIST, "");
                    String lyricType = dataObject.optString(Constants.LyricRequest.KEY_RESPONSE_LYRIC_TYPE, "lrc");
                    String lyricContent = dataObject.optString(Constants.LyricRequest.KEY_RESPONSE_LYRIC_CONTENT, "");
                    String style = dataObject.optString("style", "");
                    lyricInfo.setSongName(songName);
                    lyricInfo.setArtistName(artistName);
                    lyricInfo.setLyricType(lyricType);
                    lyricInfo.setLyricContent(lyricContent);
                    Bundle extra = new Bundle();
                    extra.putString("style", style);
                    lyricInfo.setExtras(extra);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return lyricInfo;
    }
}
