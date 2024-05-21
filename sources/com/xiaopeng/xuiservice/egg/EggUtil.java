package com.xiaopeng.xuiservice.egg;

import android.text.TextUtils;
import com.alipay.mobile.aromeservice.RequestParams;
import com.xiaopeng.xuiservice.smart.action.Actions;
import com.xiaopeng.xuiservice.smart.condition.Condition;
import com.xiaopeng.xuiservice.smart.condition.ConditionCounting;
import com.xiaopeng.xuiservice.smart.condition.ConditionInterval;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionIG;
import com.xiaopeng.xuiservice.smart.condition.util.ConditionJsonUtil;
import com.xiaopeng.xuiservice.utils.FileUtils;
import com.xiaopeng.xuiservice.utils.SharedPreferencesUtil;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import java.io.File;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class EggUtil {

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class MapHolder {
        private static HashMap<Egg, Long> lastRunMap = new HashMap<>();
        private static HashMap<Egg, Integer> runCountMap = new HashMap<>();

        private MapHolder() {
        }
    }

    public static void cleanMemoryEggRunRecord() {
        MapHolder.lastRunMap.clear();
        MapHolder.runCountMap.clear();
    }

    public static void recordLastRun(Egg egg, long time) {
        MapHolder.lastRunMap.put(egg, Long.valueOf(time));
        SharedPreferencesUtil.getInstance().putLong(getLastRunKey(egg), time);
    }

    public static long getLastRunInStorage(Egg egg) {
        return SharedPreferencesUtil.getInstance().getLong(getLastRunKey(egg), 0L);
    }

    public static long getLastRunInMemory(Egg egg) {
        Long lastRun = (Long) MapHolder.lastRunMap.get(egg);
        if (lastRun == null) {
            return 0L;
        }
        return lastRun.longValue();
    }

    public static void increaseRunCount(Egg egg) {
        int runCount = getRunCountInMemory(egg);
        MapHolder.runCountMap.put(egg, Integer.valueOf(runCount + 1));
        int runCount2 = getRunCountInStorage(egg);
        SharedPreferencesUtil.getInstance().putInt(getRunCountKey(egg), runCount2 + 1);
    }

    public static int getRunCountInStorage(Egg egg) {
        return SharedPreferencesUtil.getInstance().getInt(getRunCountKey(egg), 0);
    }

    public static int getRunCountInMemory(Egg egg) {
        Integer count = (Integer) MapHolder.runCountMap.get(egg);
        if (count == null) {
            return 0;
        }
        return count.intValue();
    }

    private static String getLastRunKey(Egg egg) {
        return "egg_lastRun_" + egg.getId();
    }

    private static String getRunCountKey(Egg egg) {
        return "egg_runCount_" + egg.getId();
    }

    public static Egg parseFromFile(String eggFile) {
        if (eggFile == null) {
            return null;
        }
        File file = new File(eggFile, "egg.json");
        String eggMainFileContent = FileUtils.readFromFile(file);
        EggLog.INFO("Begin parse: " + file);
        if (TextUtils.isEmpty(eggMainFileContent)) {
            EggLog.INFO("File not exist: " + file);
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(eggMainFileContent);
            Egg egg = new Egg();
            egg.setEggDir(eggFile);
            egg.setId(jsonObject.getString("id"));
            egg.setVersion(jsonObject.optString("version"));
            egg.setDisplayName(jsonObject.optString("displayName"));
            egg.setImage(jsonObject.optString(Actions.ACTION_IMAGE));
            egg.setStartTime(ConditionJsonUtil.jsonObject2Timestamp(jsonObject.optJSONObject(RequestParams.REQUEST_KEY_START_TIME)));
            egg.setExpireTime(ConditionJsonUtil.jsonObject2Timestamp(jsonObject.optJSONObject("expireTime")));
            egg.setConditionJsonObject(jsonObject.getJSONObject("condition"));
            egg.setStopConditionJsonObject(jsonObject.optJSONObject("stopCondition"));
            egg.setActionJsonObject(jsonObject.getJSONObject("action"));
            EggLog.INFO("Parse success: file = " + file + ", egg = " + egg);
            return egg;
        } catch (Exception e) {
            EggLog.ERROR("Parse error, file: " + eggFile, e);
            return null;
        }
    }

    public static Condition createCondition(final Egg egg, JSONObject jsonObject) {
        return ConditionJsonUtil.jsonObject2Condition(jsonObject, new ConditionJsonUtil.ParserInterceptor() { // from class: com.xiaopeng.xuiservice.egg.-$$Lambda$EggUtil$UO3xYNDry9AlnidTV2HK32IZ56k
            @Override // com.xiaopeng.xuiservice.smart.condition.util.ConditionJsonUtil.ParserInterceptor
            public final Condition jsonObject2Condition(JSONObject jSONObject) {
                Condition parseLocalCondition;
                parseLocalCondition = EggUtil.parseLocalCondition(Egg.this, jSONObject);
                return parseLocalCondition;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Condition parseLocalCondition(final Egg egg, JSONObject jsonObject) {
        ConditionInterval.LastRun lastRun;
        ConditionCounting.Counting counting;
        try {
            String type = jsonObject.getString(SpeechConstants.KEY_COMMAND_TYPE);
            char c = 65535;
            int hashCode = type.hashCode();
            if (hashCode != -372017037) {
                if (hashCode == 570418373 && type.equals(ConditionInterval.TYPE)) {
                    c = 0;
                }
            } else if (type.equals(ConditionCounting.TYPE)) {
                c = 1;
            }
            if (c == 0) {
                String scope = jsonObject.optString("scope", Actions.ACTION_ALL);
                if (ConditionIG.TYPE.equalsIgnoreCase(scope)) {
                    lastRun = new ConditionInterval.LastRun() { // from class: com.xiaopeng.xuiservice.egg.-$$Lambda$EggUtil$fgMfBZBMfLGBfvxaPP7Ak4m7vWc
                        @Override // com.xiaopeng.xuiservice.smart.condition.ConditionInterval.LastRun
                        public final long getLastRun() {
                            long lastRunInMemory;
                            lastRunInMemory = EggUtil.getLastRunInMemory(Egg.this);
                            return lastRunInMemory;
                        }
                    };
                } else {
                    lastRun = new ConditionInterval.LastRun() { // from class: com.xiaopeng.xuiservice.egg.-$$Lambda$EggUtil$RZS6jacHwkD6nEfjxGXQG0jWz6M
                        @Override // com.xiaopeng.xuiservice.smart.condition.ConditionInterval.LastRun
                        public final long getLastRun() {
                            long lastRunInStorage;
                            lastRunInStorage = EggUtil.getLastRunInStorage(Egg.this);
                            return lastRunInStorage;
                        }
                    };
                }
                return new ConditionInterval(jsonObject.getString(ConditionInterval.TYPE), lastRun, scope);
            } else if (c != 1) {
                return null;
            } else {
                String scope2 = jsonObject.optString("scope", Actions.ACTION_ALL);
                if (ConditionIG.TYPE.equalsIgnoreCase(scope2)) {
                    counting = new ConditionCounting.Counting() { // from class: com.xiaopeng.xuiservice.egg.-$$Lambda$EggUtil$fDSRDnLyPLK0zhtyk1g1Ta0rHaU
                        @Override // com.xiaopeng.xuiservice.smart.condition.ConditionCounting.Counting
                        public final int getCounting() {
                            int runCountInMemory;
                            runCountInMemory = EggUtil.getRunCountInMemory(Egg.this);
                            return runCountInMemory;
                        }
                    };
                } else {
                    counting = new ConditionCounting.Counting() { // from class: com.xiaopeng.xuiservice.egg.-$$Lambda$EggUtil$iHVoarCLDeR96tzBt8NYue35urk
                        @Override // com.xiaopeng.xuiservice.smart.condition.ConditionCounting.Counting
                        public final int getCounting() {
                            int runCountInStorage;
                            runCountInStorage = EggUtil.getRunCountInStorage(Egg.this);
                            return runCountInStorage;
                        }
                    };
                }
                return new ConditionCounting(Integer.valueOf(jsonObject.getInt(ConditionCounting.TYPE)), counting, scope2);
            }
        } catch (JSONException e) {
            EggLog.ERROR("jsonObject2Condition error", e);
            return null;
        }
    }
}
