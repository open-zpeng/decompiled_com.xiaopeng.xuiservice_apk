package com.xiaopeng.xuiservice.musicrecognize;

import com.xiaopeng.speech.protocol.bean.recommend.RecommendBean;
import com.xiaopeng.xuiservice.musicrecognize.AbsFindCover;
import com.xiaopeng.xuiservice.musicrecognize.IFindCover;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class FindCoverBy163Impl extends AbsFindCover {
    private static final String URL = "http://music.163.com/api/search/pc";

    @Override // com.xiaopeng.xuiservice.musicrecognize.IFindCover
    public void findSongCover(String songName, final IFindCover.Callback callback) {
        Map<String, String> map = new HashMap<>();
        map.put("s", songName);
        map.put("offset", "0");
        map.put("limit", "1");
        map.put(SpeechConstants.KEY_COMMAND_TYPE, "1");
        httpRequest(URL, map, new AbsFindCover.HttpCallback() { // from class: com.xiaopeng.xuiservice.musicrecognize.FindCoverBy163Impl.1
            @Override // com.xiaopeng.xuiservice.musicrecognize.AbsFindCover.HttpCallback
            public void onResponse(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    final String link = jsonObject.getJSONObject(RecommendBean.SHOW_TIME_RESULT).getJSONArray("songs").getJSONObject(0).getJSONObject("album").getString("picUrl");
                    AbsFindCover.runOnUiThread(new Runnable() { // from class: com.xiaopeng.xuiservice.musicrecognize.FindCoverBy163Impl.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            callback.findResult(link);
                        }
                    });
                } catch (JSONException e) {
                    AbsFindCover.runOnUiThread(new Runnable() { // from class: com.xiaopeng.xuiservice.musicrecognize.FindCoverBy163Impl.1.2
                        @Override // java.lang.Runnable
                        public void run() {
                            callback.findResult(null);
                        }
                    });
                    e.printStackTrace();
                }
            }

            @Override // com.xiaopeng.xuiservice.musicrecognize.AbsFindCover.HttpCallback
            public void onFail(String msg) {
                AbsFindCover.runOnUiThread(new Runnable() { // from class: com.xiaopeng.xuiservice.musicrecognize.FindCoverBy163Impl.1.3
                    @Override // java.lang.Runnable
                    public void run() {
                        callback.findResult(null);
                    }
                });
            }
        });
    }
}
