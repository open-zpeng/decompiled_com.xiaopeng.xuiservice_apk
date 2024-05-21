package com.xiaopeng.xuiservice.smart.condition.util;

import android.util.Pair;
import com.lzy.okgo.model.Progress;
import com.xiaopeng.xuiservice.egg.EggLog;
import com.xiaopeng.xuiservice.smart.condition.AND;
import com.xiaopeng.xuiservice.smart.condition.Condition;
import com.xiaopeng.xuiservice.smart.condition.ConditionCounting;
import com.xiaopeng.xuiservice.smart.condition.ConditionInterval;
import com.xiaopeng.xuiservice.smart.condition.Conditions;
import com.xiaopeng.xuiservice.smart.condition.Group;
import com.xiaopeng.xuiservice.smart.condition.OR;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionAccPedal;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionBattery;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionBeltStatus;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionBonnet;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionBrakeStatus;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionChargePort;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionDoorState;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionDrivingDistance;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionGear;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionIG;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionRearTrunk;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionSeatOccupancy;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionSpeed;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionSpeedBetween;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionSysReady;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionTime;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionTirePressure;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionWheelAngle;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionWindowPos;
import com.xiaopeng.xuiservice.smart.condition.operator.Operators;
import com.xiaopeng.xuiservice.utils.DateTimeFormatUtil;
import com.xiaopeng.xuiservice.utils.JsonUtil;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import java.util.ArrayList;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class ConditionJsonUtil {

    /* loaded from: classes5.dex */
    public interface ParserInterceptor {
        Condition jsonObject2Condition(JSONObject jSONObject);
    }

    public static Condition jsonObject2Condition(JSONObject jsonObject, ParserInterceptor interceptor) {
        Condition condition;
        try {
            String type = jsonObject.getString(SpeechConstants.KEY_COMMAND_TYPE);
            if (interceptor != null && (condition = interceptor.jsonObject2Condition(jsonObject)) != null) {
                return condition;
            }
            char c = 65535;
            switch (type.hashCode()) {
                case -1916213811:
                    if (type.equals(ConditionTirePressure.TYPE)) {
                        c = 14;
                        break;
                    }
                    break;
                case -1383413700:
                    if (type.equals(ConditionBonnet.TYPE)) {
                        c = '\f';
                        break;
                    }
                    break;
                case -787751952:
                    if (type.equals(ConditionWindowPos.TYPE)) {
                        c = '\n';
                        break;
                    }
                    break;
                case -737561380:
                    if (type.equals(ConditionDrivingDistance.TYPE)) {
                        c = '\b';
                        break;
                    }
                    break;
                case -722838928:
                    if (type.equals(ConditionRearTrunk.TYPE)) {
                        c = 11;
                        break;
                    }
                    break;
                case -331239923:
                    if (type.equals(ConditionBattery.TYPE)) {
                        c = '\t';
                        break;
                    }
                    break;
                case 2531:
                    if (type.equals(OR.TYPE)) {
                        c = 1;
                        break;
                    }
                    break;
                case 3358:
                    if (type.equals(ConditionIG.TYPE)) {
                        c = 3;
                        break;
                    }
                    break;
                case 64951:
                    if (type.equals(AND.TYPE)) {
                        c = 0;
                        break;
                    }
                    break;
                case 3020043:
                    if (type.equals(ConditionBeltStatus.TYPE)) {
                        c = 16;
                        break;
                    }
                    break;
                case 3089326:
                    if (type.equals(ConditionDoorState.TYPE)) {
                        c = 6;
                        break;
                    }
                    break;
                case 3168655:
                    if (type.equals(ConditionGear.TYPE)) {
                        c = 4;
                        break;
                    }
                    break;
                case 3526149:
                    if (type.equals(ConditionSeatOccupancy.TYPE)) {
                        c = 15;
                        break;
                    }
                    break;
                case 3560141:
                    if (type.equals("time")) {
                        c = 2;
                        break;
                    }
                    break;
                case 93997867:
                    if (type.equals(ConditionBrakeStatus.TYPE)) {
                        c = 19;
                        break;
                    }
                    break;
                case 109641799:
                    if (type.equals("speed")) {
                        c = 5;
                        break;
                    }
                    break;
                case 1417983317:
                    if (type.equals(ConditionChargePort.TYPE)) {
                        c = '\r';
                        break;
                    }
                    break;
                case 1622126136:
                    if (type.equals(ConditionWheelAngle.TYPE)) {
                        c = 17;
                        break;
                    }
                    break;
                case 1944675030:
                    if (type.equals(ConditionSysReady.TYPE)) {
                        c = 7;
                        break;
                    }
                    break;
                case 2128704889:
                    if (type.equals(ConditionAccPedal.TYPE)) {
                        c = 18;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                case 1:
                    JSONArray jsonArray = jsonObject.getJSONArray("conditions");
                    int length = jsonArray.length();
                    ArrayList<Condition> conditions = new ArrayList<>(length);
                    for (int i = 0; i < length; i++) {
                        Condition c2 = jsonObject2Condition(jsonArray.getJSONObject(i), interceptor);
                        if (c2 == null) {
                            return null;
                        }
                        conditions.add(c2);
                    }
                    return AND.TYPE.equals(type) ? Conditions.AND(conditions) : Conditions.OR(conditions);
                case 2:
                    String repeat = jsonObject.optString("repeat", ConditionTime.REPEAT_ONCE);
                    JSONArray jsonArray2 = jsonObject.getJSONArray(Operators.OP_BETWEEN);
                    Pair<Long, Long> timePair = Pair.create(Long.valueOf(jsonObject2Timestamp(jsonArray2.getJSONObject(0))), Long.valueOf(jsonObject2Timestamp(jsonArray2.getJSONObject(1))));
                    return new ConditionTime(timePair, repeat);
                case 3:
                    return new ConditionIG(Integer.valueOf(jsonObject.getInt(Operators.OP_EQ)));
                case 4:
                    return new ConditionGear(JsonUtil.jsonArray2StringArray(jsonObject.getJSONArray(Operators.OP_IN)));
                case 5:
                    if (jsonObject.has(Operators.OP_BETWEEN)) {
                        return new ConditionSpeedBetween(JsonUtil.jsonArray2DoublePair(jsonObject.getJSONArray(Operators.OP_BETWEEN)));
                    }
                    String opName = getCompareOP(jsonObject);
                    if (opName == null) {
                        return null;
                    }
                    return new ConditionSpeed(Double.valueOf(jsonObject.getDouble(opName)), opName);
                case 6:
                    return new ConditionDoorState(Integer.valueOf(jsonObject.getInt(Operators.OP_EQ)), jsonObject.getInt(ConditionDoorState.TYPE));
                case 7:
                    return new ConditionSysReady(Integer.valueOf(jsonObject.getInt(Operators.OP_EQ)));
                case '\b':
                    String opName2 = getCompareOP(jsonObject);
                    if (opName2 == null) {
                        return null;
                    }
                    return new ConditionDrivingDistance(Float.valueOf((float) jsonObject.getDouble(opName2)), opName2, jsonObject.getString(SpeechConstants.KEY_MODE));
                case '\t':
                    String opName3 = getCompareOP(jsonObject);
                    if (opName3 == null) {
                        return null;
                    }
                    return new ConditionBattery(Integer.valueOf(jsonObject.getInt(opName3)), opName3);
                case '\n':
                    String opName4 = getCompareOP(jsonObject);
                    if (opName4 == null) {
                        return null;
                    }
                    return new ConditionWindowPos(Float.valueOf((float) jsonObject.getDouble(opName4)), opName4, jsonObject.getInt(ConditionWindowPos.TYPE));
                case 11:
                    return new ConditionRearTrunk(Integer.valueOf(jsonObject.getInt(Operators.OP_EQ)));
                case '\f':
                    return new ConditionBonnet(Integer.valueOf(jsonObject.getInt(Operators.OP_EQ)));
                case '\r':
                    return new ConditionChargePort(Integer.valueOf(jsonObject.getInt(Operators.OP_EQ)), jsonObject.getInt("port"));
                case 14:
                    String opName5 = getCompareOP(jsonObject);
                    if (opName5 == null) {
                        return null;
                    }
                    return new ConditionTirePressure(Float.valueOf((float) jsonObject.getDouble(opName5)), opName5, jsonObject.getInt("tire"));
                case 15:
                    return new ConditionSeatOccupancy(Integer.valueOf(jsonObject.getInt(Operators.OP_EQ)), jsonObject.getInt(ConditionSeatOccupancy.TYPE));
                case 16:
                    return new ConditionBeltStatus(Integer.valueOf(jsonObject.getInt(Operators.OP_EQ)), jsonObject.getInt(ConditionBeltStatus.TYPE));
                case 17:
                    String opName6 = getCompareOP(jsonObject);
                    if (opName6 == null) {
                        return null;
                    }
                    return new ConditionWheelAngle(Float.valueOf((float) jsonObject.getDouble(opName6)), opName6);
                case 18:
                    String opName7 = getCompareOP(jsonObject);
                    if (opName7 == null) {
                        return null;
                    }
                    return new ConditionAccPedal(Integer.valueOf(jsonObject.getInt(opName7)), opName7);
                case 19:
                    return new ConditionBrakeStatus(Integer.valueOf(jsonObject.getInt(Operators.OP_EQ)));
                default:
                    EggLog.INFO("unknown condition type: " + type);
                    return null;
            }
        } catch (Exception e) {
            EggLog.ERROR("jsonObject2Condition error", e);
            return null;
        }
    }

    private static String getCompareOP(JSONObject jsonObject) {
        if (jsonObject.has(Operators.OP_EQ)) {
            return Operators.OP_EQ;
        }
        if (jsonObject.has(Operators.OP_NEQ)) {
            return Operators.OP_NEQ;
        }
        if (jsonObject.has(Operators.OP_LT)) {
            return Operators.OP_LT;
        }
        if (jsonObject.has(Operators.OP_LE)) {
            return Operators.OP_LE;
        }
        if (jsonObject.has(Operators.OP_GT)) {
            return Operators.OP_GT;
        }
        if (jsonObject.has(Operators.OP_GE)) {
            return Operators.OP_GE;
        }
        return null;
    }

    public static long jsonObject2Timestamp(JSONObject jsonObject) {
        if (jsonObject == null) {
            return 0L;
        }
        try {
        } catch (Exception e) {
            Conditions.ERROR("parse Date error", e);
        }
        if (jsonObject.has(Progress.DATE)) {
            Date date = DateTimeFormatUtil.parse(DateTimeFormatUtil.FULL_DATE_TIME_FORMAT, jsonObject.getString(Progress.DATE));
            if (date != null) {
                return date.getTime();
            }
            return 0L;
        }
        return jsonObject.getLong("timestamp");
    }

    private static JSONArray jsonArray(Object obj) throws JSONException {
        if (obj.getClass().isArray()) {
            return new JSONArray(obj);
        }
        if (obj instanceof Pair) {
            JSONArray jsonArray = new JSONArray();
            Pair<?, ?> pair = (Pair) obj;
            jsonArray.put(JSONObject.wrap(pair.first));
            jsonArray.put(JSONObject.wrap(pair.second));
            return jsonArray;
        }
        throw new JSONException("Not a primitive array: " + obj.getClass());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static JSONObject condition2JSONObject(Condition condition) {
        JSONObject jsonObject = new JSONObject();
        try {
        } catch (JSONException e) {
            EggLog.ERROR("condition2JSONObject error", e);
        }
        if (!(condition instanceof AND) && !(condition instanceof OR)) {
            if (condition instanceof ConditionTime) {
                jsonObject.put(SpeechConstants.KEY_COMMAND_TYPE, "time");
                ConditionTime conditionTime = (ConditionTime) condition;
                Pair<Long, Long> timePair = conditionTime.getTargetValue();
                JSONArray timeJsonArray = new JSONArray();
                JSONObject dateJsonObject = new JSONObject();
                dateJsonObject.put(Progress.DATE, DateTimeFormatUtil.format(DateTimeFormatUtil.FULL_DATE_TIME_FORMAT, new Date(((Long) timePair.first).longValue())));
                timeJsonArray.put(dateJsonObject);
                JSONObject dateJsonObject2 = new JSONObject();
                dateJsonObject2.put(Progress.DATE, DateTimeFormatUtil.format(DateTimeFormatUtil.FULL_DATE_TIME_FORMAT, new Date(((Long) timePair.second).longValue())));
                timeJsonArray.put(dateJsonObject2);
                jsonObject.put(Operators.OP_BETWEEN, timeJsonArray);
                jsonObject.put("repeat", conditionTime.getRepeat());
            } else if (condition instanceof ConditionIG) {
                jsonObject.put(SpeechConstants.KEY_COMMAND_TYPE, ConditionIG.TYPE);
                jsonObject.put(Operators.OP_EQ, ((ConditionIG) condition).getTargetValue());
            } else if (condition instanceof ConditionGear) {
                jsonObject.put(SpeechConstants.KEY_COMMAND_TYPE, ConditionGear.TYPE);
                jsonObject.put(Operators.OP_IN, jsonArray(((ConditionGear) condition).getTargetValue()));
            } else if (condition instanceof ConditionSpeedBetween) {
                jsonObject.put(SpeechConstants.KEY_COMMAND_TYPE, "speed");
                jsonObject.put(Operators.OP_BETWEEN, jsonArray(((ConditionSpeedBetween) condition).getTargetValue()));
            } else if (condition instanceof ConditionSpeed) {
                jsonObject.put(SpeechConstants.KEY_COMMAND_TYPE, "speed");
                jsonObject.put(((ConditionSpeed) condition).getCompareOpName(), ((ConditionSpeed) condition).getTargetValue());
            } else if (condition instanceof ConditionDoorState) {
                jsonObject.put(SpeechConstants.KEY_COMMAND_TYPE, ConditionDoorState.TYPE);
                jsonObject.put(Operators.OP_EQ, ((ConditionDoorState) condition).getTargetValue());
                jsonObject.put(ConditionDoorState.TYPE, ((ConditionDoorState) condition).getDoor());
            } else if (condition instanceof ConditionSysReady) {
                jsonObject.put(SpeechConstants.KEY_COMMAND_TYPE, ConditionSysReady.TYPE);
                jsonObject.put(Operators.OP_EQ, ((ConditionSysReady) condition).getTargetValue());
            } else if (condition instanceof ConditionDrivingDistance) {
                jsonObject.put(SpeechConstants.KEY_COMMAND_TYPE, ConditionDrivingDistance.TYPE);
                jsonObject.put(((ConditionDrivingDistance) condition).getCompareOpName(), ((ConditionDrivingDistance) condition).getTargetValue());
                jsonObject.put(SpeechConstants.KEY_MODE, ((ConditionDrivingDistance) condition).getMode());
            } else if (condition instanceof ConditionBattery) {
                jsonObject.put(SpeechConstants.KEY_COMMAND_TYPE, ConditionBattery.TYPE);
                jsonObject.put(((ConditionBattery) condition).getCompareOpName(), ((ConditionBattery) condition).getTargetValue());
            } else if (condition instanceof ConditionWindowPos) {
                jsonObject.put(SpeechConstants.KEY_COMMAND_TYPE, ConditionWindowPos.TYPE);
                jsonObject.put(((ConditionWindowPos) condition).getCompareOpName(), ((ConditionWindowPos) condition).getTargetValue());
                jsonObject.put(ConditionWindowPos.TYPE, ((ConditionWindowPos) condition).getWindow());
            } else if (condition instanceof ConditionRearTrunk) {
                jsonObject.put(SpeechConstants.KEY_COMMAND_TYPE, ConditionRearTrunk.TYPE);
                jsonObject.put(Operators.OP_EQ, ((ConditionRearTrunk) condition).getTargetValue());
            } else if (condition instanceof ConditionBonnet) {
                jsonObject.put(SpeechConstants.KEY_COMMAND_TYPE, ConditionBonnet.TYPE);
                jsonObject.put(Operators.OP_EQ, ((ConditionBonnet) condition).getTargetValue());
            } else if (condition instanceof ConditionChargePort) {
                jsonObject.put(SpeechConstants.KEY_COMMAND_TYPE, ConditionChargePort.TYPE);
                jsonObject.put(Operators.OP_EQ, ((ConditionChargePort) condition).getTargetValue());
            } else if (condition instanceof ConditionTirePressure) {
                jsonObject.put(SpeechConstants.KEY_COMMAND_TYPE, ConditionTirePressure.TYPE);
                jsonObject.put(((ConditionTirePressure) condition).getCompareOpName(), ((ConditionTirePressure) condition).getTargetValue());
                jsonObject.put("tire", ((ConditionTirePressure) condition).getTire());
            } else if (condition instanceof ConditionSeatOccupancy) {
                jsonObject.put(SpeechConstants.KEY_COMMAND_TYPE, ConditionSeatOccupancy.TYPE);
                jsonObject.put(Operators.OP_EQ, ((ConditionSeatOccupancy) condition).getTargetValue());
                jsonObject.put(ConditionSeatOccupancy.TYPE, ((ConditionSeatOccupancy) condition).getSeat());
            } else if (condition instanceof ConditionBeltStatus) {
                jsonObject.put(SpeechConstants.KEY_COMMAND_TYPE, ConditionBeltStatus.TYPE);
                jsonObject.put(Operators.OP_EQ, ((ConditionBeltStatus) condition).getTargetValue());
                jsonObject.put(ConditionBeltStatus.TYPE, ((ConditionBeltStatus) condition).getBelt());
            } else if (condition instanceof ConditionWheelAngle) {
                jsonObject.put(SpeechConstants.KEY_COMMAND_TYPE, ConditionWheelAngle.TYPE);
                jsonObject.put(((ConditionWheelAngle) condition).getCompareOpName(), ((ConditionWheelAngle) condition).getTargetValue());
            } else if (condition instanceof ConditionAccPedal) {
                jsonObject.put(SpeechConstants.KEY_COMMAND_TYPE, ConditionAccPedal.TYPE);
                jsonObject.put(((ConditionAccPedal) condition).getCompareOpName(), ((ConditionAccPedal) condition).getTargetValue());
            } else if (condition instanceof ConditionBrakeStatus) {
                jsonObject.put(SpeechConstants.KEY_COMMAND_TYPE, ConditionBrakeStatus.TYPE);
                jsonObject.put(Operators.OP_EQ, ((ConditionBrakeStatus) condition).getTargetValue());
            } else if (condition instanceof ConditionInterval) {
                jsonObject.put(SpeechConstants.KEY_COMMAND_TYPE, ConditionInterval.TYPE);
                jsonObject.put(ConditionInterval.TYPE, ((ConditionInterval) condition).getInterval());
                jsonObject.put("scope", ((ConditionInterval) condition).getScope());
            } else if (condition instanceof ConditionCounting) {
                jsonObject.put(SpeechConstants.KEY_COMMAND_TYPE, ConditionCounting.TYPE);
                jsonObject.put(ConditionCounting.TYPE, ((ConditionCounting) condition).getTargetValue());
                jsonObject.put("scope", ((ConditionCounting) condition).getScope());
            } else {
                jsonObject.put(SpeechConstants.KEY_COMMAND_TYPE, "unknown");
                jsonObject.put("class", condition.getClass().getSimpleName());
            }
            return jsonObject;
        }
        if (condition instanceof AND) {
            jsonObject.put(SpeechConstants.KEY_COMMAND_TYPE, AND.TYPE);
        } else {
            jsonObject.put(SpeechConstants.KEY_COMMAND_TYPE, OR.TYPE);
        }
        Group group = (Group) condition;
        JSONArray jsonArray = new JSONArray();
        for (Condition c : group.getConditions()) {
            JSONObject sub = condition2JSONObject(c);
            jsonArray.put(sub);
        }
        jsonObject.put("conditions", jsonArray);
        return jsonObject;
    }
}
