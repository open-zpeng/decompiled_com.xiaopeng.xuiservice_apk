package com.xiaopeng.xuiservice.xapp.miniprog;

import android.text.TextUtils;
import com.alipay.arome.aromecli.AromeServiceTask;
import com.alipay.arome.aromecli.response.AromeResponse;
import com.alipay.mobile.aromeservice.RequestParams;
import com.xiaopeng.speech.protocol.bean.recommend.RecommendBean;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuimanager.xapp.MiniProgramResponse;
import com.xiaopeng.xuiservice.xapp.XMiniProgService;
import com.xiaopeng.xuiservice.xapp.util.GsonUtil;
import java.util.Map;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class AromeServiceCallback<T extends AromeResponse> implements AromeServiceTask.Callback<AromeResponse> {
    private static final String TAG = AromeServiceCallback.class.getSimpleName();
    private AromeParam mAromeParam;
    private XMiniProgService mXMiniProgService;
    private MiniUserInfo miniUserInfo;

    public AromeServiceCallback(AromeParam param, XMiniProgService service) {
        this.mAromeParam = param;
        this.mXMiniProgService = service;
    }

    @Override // com.alipay.arome.aromecli.AromeServiceTask.Callback
    public void onCallback(AromeResponse t) {
        String tagResponse = t == null ? "" : t.toString();
        String str = TAG;
        LogUtil.d(str, "AromeResponse:" + tagResponse);
        if (this.mAromeParam.getType() != 8 && t != null) {
            MiniProgramResponse miniProgramResponse = new MiniProgramResponse();
            miniProgramResponse.setCode(t.code);
            miniProgramResponse.setMiniAppId(this.mAromeParam.getMiniAppId());
            miniProgramResponse.setParams(this.mAromeParam.getParams());
            String str2 = TAG;
            LogUtil.d(str2, "AromeParam type: " + this.mAromeParam.getType());
            int type = this.mAromeParam.getType();
            if (type != 6) {
                if (type == 10) {
                    this.miniUserInfo = getUserInfo(t);
                    MiniUserInfo miniUserInfo = this.miniUserInfo;
                    if (miniUserInfo != null) {
                        String paramKey = "";
                        String userKey = miniUserInfo.getUserKey();
                        Map map = this.mAromeParam.getParams();
                        map.put(RequestParams.REQUEST_KEY_USER_IDENTITY, false);
                        if (this.miniUserInfo.isLogin()) {
                            paramKey = (String) map.get("userKey");
                            if (!TextUtils.isEmpty(paramKey) && !TextUtils.isEmpty(userKey)) {
                                boolean result = paramKey.equals(userKey);
                                String str3 = TAG;
                                LogUtil.d(str3, "equal result: " + result);
                                map.put(RequestParams.REQUEST_KEY_USER_IDENTITY, Boolean.valueOf(result));
                            }
                        }
                        String str4 = TAG;
                        LogUtil.d(str4, " userKey: " + userKey + " paramKey: " + paramKey);
                        miniProgramResponse.setParams(map);
                    }
                } else if (type != 13) {
                    miniProgramResponse.setMessage(t.message);
                }
                this.mXMiniProgService.sendMiniProgMessage(this.mAromeParam.getType(), miniProgramResponse);
            }
            this.miniUserInfo = getUserInfo(t);
            MiniUserInfo miniUserInfo2 = this.miniUserInfo;
            if (miniUserInfo2 != null) {
                miniProgramResponse.setLogin(miniUserInfo2.isLogin());
                miniProgramResponse.setUserAvatar(this.miniUserInfo.getUserAvatar());
                miniProgramResponse.setUserKey(this.miniUserInfo.getUserKey());
                miniProgramResponse.setUserNick(this.miniUserInfo.getUserNick());
                miniProgramResponse.setUserRoute(this.miniUserInfo.getUserRoute());
            }
            this.mXMiniProgService.sendMiniProgMessage(this.mAromeParam.getType(), miniProgramResponse);
        }
    }

    private MiniUserInfo getUserInfo(AromeResponse response) {
        try {
            JSONObject bodyJSONObject = new JSONObject(response.message);
            String dataJSONObject = bodyJSONObject.getString(RecommendBean.SHOW_TIME_RESULT);
            return (MiniUserInfo) GsonUtil.fromJson(dataJSONObject, (Class<Object>) MiniUserInfo.class);
        } catch (Exception e) {
            String str = TAG;
            LogUtil.d(str, "json error:" + e);
            return null;
        }
    }
}
