package com.xiaopeng.xuiservice.smart.action.util;

import androidx.annotation.Nullable;
import com.alipay.mobile.aromeservice.ResponseParams;
import com.xiaopeng.speech.speechwidget.SpeechWidget;
import com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightCapability;
import com.xiaopeng.xuiservice.egg.EggLog;
import com.xiaopeng.xuiservice.smart.action.Action;
import com.xiaopeng.xuiservice.smart.action.Actions;
import com.xiaopeng.xuiservice.smart.action.impl.ActionAVAS;
import com.xiaopeng.xuiservice.smart.action.impl.ActionAmbientLight;
import com.xiaopeng.xuiservice.smart.action.impl.ActionAudio;
import com.xiaopeng.xuiservice.smart.action.impl.ActionDialog;
import com.xiaopeng.xuiservice.smart.action.impl.ActionImage;
import com.xiaopeng.xuiservice.smart.action.impl.ActionLightLanguage;
import com.xiaopeng.xuiservice.smart.action.impl.ActionTTS;
import com.xiaopeng.xuiservice.smart.action.impl.ActionToast;
import com.xiaopeng.xuiservice.smart.action.impl.ActionVideo;
import com.xiaopeng.xuiservice.utils.FileUtils;
import com.xiaopeng.xuiservice.utils.JsonUtil;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class ActionParser {

    /* loaded from: classes5.dex */
    public interface ParserInterceptor {
        Action parseAction(JSONObject jSONObject, String str, String str2) throws JSONException;
    }

    public static Action parseAction(JSONObject jsonObject, String fileRoot, ParserInterceptor interceptor) {
        Action action = null;
        if (jsonObject == null) {
            return null;
        }
        try {
            String type = jsonObject.getString(SpeechConstants.KEY_COMMAND_TYPE);
            if (interceptor != null) {
                action = interceptor.parseAction(jsonObject, type, fileRoot);
            }
            if (action == null) {
                action = parseActionByType(jsonObject, type, fileRoot, interceptor);
            }
            if (action != null) {
                if (jsonObject.has("duration")) {
                    action = Actions.delayedStop(action, jsonObject.getLong("duration"), TimeUnit.MILLISECONDS);
                }
                if (jsonObject.has("delay")) {
                    return Actions.delayedStart(action, jsonObject.getLong("delay"), TimeUnit.MILLISECONDS);
                }
                return action;
            }
            return action;
        } catch (Exception e) {
            EggLog.ERROR("parse json to Action failed, jsonObject = \n" + jsonObject, e);
            return null;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Nullable
    private static Action parseActionByType(JSONObject jsonObject, String type, String fileRoot, ParserInterceptor interceptor) throws JSONException {
        char c;
        char c2;
        switch (type.hashCode()) {
            case -1332085432:
                if (type.equals(Actions.ACTION_DIALOG)) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case -892145000:
                if (type.equals(Actions.ACTION_AMBIENT)) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 96673:
                if (type.equals(Actions.ACTION_ALL)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 96748:
                if (type.equals("any")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 107253:
                if (type.equals(Actions.ACTION_LLU)) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 112787:
                if (type.equals(Actions.ACTION_REF)) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case 115187:
                if (type.equals("tts")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 3006247:
                if (type.equals(Actions.ACTION_AVAS)) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 93166550:
                if (type.equals("audio")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 100313435:
                if (type.equals(Actions.ACTION_IMAGE)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 110532135:
                if (type.equals(Actions.ACTION_TOAST)) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case 112202875:
                if (type.equals("video")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1349547969:
                if (type.equals(Actions.ACTION_SEQUENCE)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
                JSONArray jsonArray = jsonObject.getJSONArray("actions");
                int length = jsonArray.length();
                ArrayList<Action> actions = new ArrayList<>(length);
                for (int i = 0; i < length; i++) {
                    Action subAction = parseAction(jsonArray.optJSONObject(i), fileRoot, interceptor);
                    if (subAction != null) {
                        actions.add(subAction);
                    }
                }
                int i2 = type.hashCode();
                if (i2 == 96673) {
                    if (type.equals(Actions.ACTION_ALL)) {
                        c2 = 1;
                    }
                    c2 = 65535;
                } else if (i2 != 96748) {
                    if (i2 == 1349547969 && type.equals(Actions.ACTION_SEQUENCE)) {
                        c2 = 0;
                    }
                    c2 = 65535;
                } else {
                    if (type.equals("any")) {
                        c2 = 2;
                    }
                    c2 = 65535;
                }
                if (c2 != 0) {
                    if (c2 != 1) {
                        if (c2 == 2) {
                            return Actions.parallelAny(actions);
                        }
                        return null;
                    }
                    return Actions.parallelAll(actions);
                }
                return Actions.sequence(actions);
            case 3:
                return new ActionVideo(buildEggResourcePath(fileRoot, jsonObject.getString("file")), jsonObject.optInt("repeat", 1), jsonObject.optInt("audioFocus", 1), jsonObject.optDouble("x", 0.0d), jsonObject.optDouble("y", 0.0d), jsonObject.optDouble("w", 1.0d), jsonObject.optDouble("h", 1.0d));
            case 4:
                return new ActionImage(buildEggResourcePath(fileRoot, jsonObject.getString("file")), jsonObject.optDouble("x", 0.0d), jsonObject.optDouble("y", 0.0d), jsonObject.optDouble("w", 1.0d), jsonObject.optDouble("h", 1.0d));
            case 5:
                return new ActionAudio(buildEggResourcePath(fileRoot, jsonObject.getString("file")), jsonObject.optInt("repeat", 1), jsonObject.optInt("audioFocus", 1));
            case 6:
                return new ActionTTS(jsonObject.getString("text"), jsonObject.optInt("repeat", 1));
            case 7:
                return new ActionAVAS(buildEggResourcePath(fileRoot, jsonObject.getString("file")), jsonObject.optInt("repeat", 1));
            case '\b':
                return new ActionAmbientLight(jsonObject.getString("effect"), JsonUtil.jsonArray2intArray(jsonObject.getJSONArray("color")), JsonUtil.jsonArray2intArray(jsonObject.getJSONArray("brightness")), JsonUtil.jsonArray2intArray(jsonObject.getJSONArray("fade")), jsonObject.optInt("executeTime", 10000), jsonObject.optInt(AmbientLightCapability.TIME_INTERVAL, 1000));
            case '\t':
                return new ActionLightLanguage(jsonObject.getString("effect"), buildEggResourcePath(fileRoot, jsonObject.getString("file")), jsonObject.optInt("repeat"));
            case '\n':
                return new ActionToast(jsonObject.getString("text"));
            case 11:
                return new ActionDialog(jsonObject.optString(SpeechWidget.WIDGET_TITLE), jsonObject.optString(ResponseParams.RESPONSE_KEY_MESSAGE), jsonObject.getString("confirmButton"), jsonObject.getString("cancelButton"), parseAction(jsonObject.optJSONObject("confirmAction"), fileRoot, interceptor), parseAction(jsonObject.optJSONObject("cancelAction"), fileRoot, interceptor));
            case '\f':
                String refFile = buildEggResourcePath(fileRoot, jsonObject.getString("file"));
                String refContent = FileUtils.readFromFile(refFile);
                return parseAction(new JSONObject(refContent), fileRoot, interceptor);
            default:
                return null;
        }
    }

    private static String buildEggResourcePath(String fileRoot, String file) {
        return new File(fileRoot, file).getAbsolutePath();
    }
}
