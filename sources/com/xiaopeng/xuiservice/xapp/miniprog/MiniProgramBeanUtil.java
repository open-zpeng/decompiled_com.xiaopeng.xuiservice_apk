package com.xiaopeng.xuiservice.xapp.miniprog;

import android.text.TextUtils;
import com.google.gson.reflect.TypeToken;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IResponse;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuimanager.xapp.MiniProgramData;
import com.xiaopeng.xuimanager.xapp.MiniProgramGroup;
import com.xiaopeng.xuimanager.xapp.MiniProgramResponse;
import com.xiaopeng.xuiservice.xapp.miniprog.bean.MiniBean;
import com.xiaopeng.xuiservice.xapp.miniprog.bean.MiniBeanContainer;
import com.xiaopeng.xuiservice.xapp.miniprog.bean.MiniConfig;
import com.xiaopeng.xuiservice.xapp.miniprog.bean.MiniGroupBean;
import com.xiaopeng.xuiservice.xapp.miniprog.bean.MiniGroupContainer;
import com.xiaopeng.xuiservice.xapp.util.GsonUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class MiniProgramBeanUtil {
    private static final String TAG = "MiniProgramBeanUtil";

    public static MiniProgramResponse toBean(int code) {
        MiniProgramResponse response = new MiniProgramResponse();
        response.setCode(code);
        return response;
    }

    public static MiniConfig parseMini(Map params) {
        MiniConfig miniConfig = new MiniConfig();
        if (params != null) {
            try {
                if (params.containsKey("config")) {
                    String config = (String) params.get("config");
                    if (!TextUtils.isEmpty(config)) {
                        miniConfig = (MiniConfig) GsonUtil.fromJson(config, (Class<Object>) MiniConfig.class);
                    }
                    LogUtil.d(TAG, "params config:" + miniConfig.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (params.containsKey("page")) {
                    String page = (String) params.get("page");
                    LogUtil.d(TAG, "params page:" + page);
                    miniConfig.setPage(page);
                }
                if (params.containsKey("query")) {
                    String query = (String) params.get("query");
                    LogUtil.d(TAG, "params query:" + query);
                    miniConfig.setPage(query);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return miniConfig;
    }

    public static MiniProgramResponse parseResponse(IResponse iResponse) {
        JSONObject body;
        JSONObject dataRoot;
        JSONObject dataGroup;
        String groupList;
        MiniGroupBean groupBean;
        JSONObject dataGroup2;
        JSONObject body2;
        JSONObject dataRoot2;
        MiniProgramResponse miniProgramResponse = new MiniProgramResponse();
        if (iResponse == null) {
            miniProgramResponse.setCode(0);
            return miniProgramResponse;
        }
        try {
            miniProgramResponse.setCode(iResponse.code());
            body = new JSONObject(iResponse.body());
            dataRoot = body.getJSONObject("data");
            dataGroup = dataRoot.getJSONObject("data");
            groupList = dataGroup.getString("list");
        } catch (Exception e) {
            LogUtil.d(TAG, "parse mini list json failure!!!" + e.getMessage());
        }
        if (groupList == null) {
            return miniProgramResponse;
        }
        List<MiniGroupContainer> miniGroupContainerList = (List) GsonUtil.fromJson(groupList, new TypeToken<List<MiniGroupContainer>>() { // from class: com.xiaopeng.xuiservice.xapp.miniprog.MiniProgramBeanUtil.1
        }.getType());
        List<MiniProgramGroup> miniProgramGroups = new ArrayList<>();
        if (miniGroupContainerList != null && miniGroupContainerList.size() > 0) {
            for (MiniGroupContainer miniGroupContainer : miniGroupContainerList) {
                MiniProgramGroup miniProgramGroup = new MiniProgramGroup();
                if (miniGroupContainer != null && (groupBean = miniGroupContainer.getMiniGroupBean()) != null) {
                    miniProgramGroup.setContentType(miniGroupContainer.getContentType());
                    miniProgramGroup.setId(groupBean.getId());
                    miniProgramGroup.setGroupName(groupBean.getGroupName());
                    List<MiniBeanContainer> miniBeanContainers = groupBean.getMiniBeanContainers();
                    List<MiniProgramData> miniProgramDatas = new ArrayList<>();
                    if (miniBeanContainers == null || miniBeanContainers.size() <= 0) {
                        dataGroup2 = dataGroup;
                        body2 = body;
                        dataRoot2 = dataRoot;
                    } else {
                        for (MiniBeanContainer miniBeanContainer : miniBeanContainers) {
                            if (miniBeanContainer != null) {
                                MiniProgramData miniProgramData = new MiniProgramData();
                                JSONObject dataGroup3 = dataGroup;
                                JSONObject body3 = body;
                                miniProgramData.setContentType(miniBeanContainer.getContentType());
                                MiniBean miniBean = miniBeanContainer.getMiniBean();
                                if (miniBean == null) {
                                    dataGroup = dataGroup3;
                                    body = body3;
                                } else {
                                    JSONObject dataRoot3 = dataRoot;
                                    miniProgramData.setMiniAppId(miniBean.getAliId());
                                    miniProgramData.setIconName(miniBean.getLogo());
                                    miniProgramData.setName(miniBean.getName());
                                    miniProgramData.setAlipayVersion(miniBean.getAlipayVersion());
                                    miniProgramData.setId(miniBean.getId());
                                    LogUtil.d(TAG, "miniProgramData  parse " + miniProgramData.toString());
                                    miniProgramDatas.add(miniProgramData);
                                    dataGroup = dataGroup3;
                                    dataRoot = dataRoot3;
                                    body = body3;
                                }
                            }
                        }
                        dataGroup2 = dataGroup;
                        body2 = body;
                        dataRoot2 = dataRoot;
                    }
                    miniProgramGroup.setData(miniProgramDatas);
                    LogUtil.d(TAG, "miniProgramGroup  parse " + miniProgramGroup.toString());
                    miniProgramGroups.add(miniProgramGroup);
                    dataGroup = dataGroup2;
                    dataRoot = dataRoot2;
                    body = body2;
                }
            }
        }
        miniProgramResponse.setMiniProgramGroups(miniProgramGroups);
        LogUtil.d(TAG, "miniProgramResponse  parse " + miniProgramResponse.toString());
        return miniProgramResponse;
    }
}
