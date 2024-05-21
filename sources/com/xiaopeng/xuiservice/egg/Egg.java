package com.xiaopeng.xuiservice.egg;

import android.util.SparseIntArray;
import com.xiaopeng.xuiservice.smart.action.Action;
import com.xiaopeng.xuiservice.smart.action.util.ActionParser;
import com.xiaopeng.xuiservice.smart.condition.Condition;
import com.xiaopeng.xuiservice.utils.DateTimeFormatUtil;
import java.util.Date;
import java.util.Objects;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class Egg {
    private JSONObject actionJsonObject;
    private Condition condition;
    private JSONObject conditionJsonObject;
    private String displayName;
    private String eggDir;
    private long expireTime;
    private String id;
    private String image;
    private long startTime;
    private Condition stopCondition;
    private JSONObject stopConditionJsonObject;
    private String version;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getExpireTime() {
        return this.expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public String getEggDir() {
        return this.eggDir;
    }

    public void setEggDir(String eggDir) {
        this.eggDir = eggDir;
    }

    public JSONObject getConditionJsonObject() {
        return this.conditionJsonObject;
    }

    public void setConditionJsonObject(JSONObject conditionJsonObject) {
        this.conditionJsonObject = conditionJsonObject;
    }

    public JSONObject getStopConditionJsonObject() {
        return this.stopConditionJsonObject;
    }

    public void setStopConditionJsonObject(JSONObject stopConditionJsonObject) {
        this.stopConditionJsonObject = stopConditionJsonObject;
    }

    public JSONObject getActionJsonObject() {
        return this.actionJsonObject;
    }

    public void setActionJsonObject(JSONObject actionJsonObject) {
        this.actionJsonObject = actionJsonObject;
    }

    public void createCondition() {
        JSONObject jSONObject = this.conditionJsonObject;
        this.condition = jSONObject == null ? null : EggUtil.createCondition(this, jSONObject);
    }

    public Condition getCondition() {
        return this.condition;
    }

    public void createStopCondition() {
        JSONObject jSONObject = this.stopConditionJsonObject;
        this.stopCondition = jSONObject == null ? null : EggUtil.createCondition(this, jSONObject);
    }

    public Condition getStopCondition() {
        return this.stopCondition;
    }

    public Action createAction() {
        return ActionParser.parseAction(this.actionJsonObject, getEggDir(), new EggParserInterceptor());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class EggParserInterceptor implements ActionParser.ParserInterceptor {
        SparseIntArray idRecord;

        private EggParserInterceptor() {
            this.idRecord = new SparseIntArray();
        }

        /* JADX WARN: Removed duplicated region for block: B:10:0x001f A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:12:0x0021  */
        @Override // com.xiaopeng.xuiservice.smart.action.util.ActionParser.ParserInterceptor
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public com.xiaopeng.xuiservice.smart.action.Action parseAction(org.json.JSONObject r21, java.lang.String r22, java.lang.String r23) throws org.json.JSONException {
            /*
                r20 = this;
                r0 = r21
                r1 = r23
                int r2 = r22.hashCode()
                r3 = -341064690(0xffffffffebabc40e, float:-4.153043E26)
                if (r2 == r3) goto L10
                r3 = r22
                goto L1c
            L10:
                java.lang.String r2 = "resource"
                r3 = r22
                boolean r2 = r3.equals(r2)
                if (r2 == 0) goto L1c
                r2 = 0
                goto L1d
            L1c:
                r2 = -1
            L1d:
                if (r2 == 0) goto L21
                r2 = 0
                return r2
            L21:
                java.lang.String r2 = "resourceType"
                int r2 = r0.getInt(r2)
                com.xiaopeng.xuiservice.smart.action.impl.ActionResource r18 = new com.xiaopeng.xuiservice.smart.action.impl.ActionResource
                r15 = r20
                java.lang.String r5 = r15.generateResourceId(r2)
                java.lang.String r4 = "resourceName"
                java.lang.String r7 = r0.optString(r4)
                java.lang.String r4 = "effectTime"
                org.json.JSONObject r4 = r0.optJSONObject(r4)
                long r8 = com.xiaopeng.xuiservice.smart.condition.util.ConditionJsonUtil.jsonObject2Timestamp(r4)
                java.lang.String r4 = "expireTime"
                org.json.JSONObject r4 = r0.optJSONObject(r4)
                long r10 = com.xiaopeng.xuiservice.smart.condition.util.ConditionJsonUtil.jsonObject2Timestamp(r4)
                java.lang.String r4 = "extraData"
                java.lang.String r12 = r0.optString(r4)
                java.io.File r4 = new java.io.File
                java.lang.String r6 = "file"
                java.lang.String r6 = r0.getString(r6)
                r4.<init>(r1, r6)
                java.lang.String r13 = r4.getAbsolutePath()
                java.io.File r4 = new java.io.File
                java.lang.String r6 = "resourceIcon"
                java.lang.String r6 = r0.optString(r6)
                r4.<init>(r1, r6)
                java.lang.String r14 = r4.getAbsolutePath()
                java.lang.String r4 = "description"
                java.lang.String r16 = r0.optString(r4)
                java.lang.String r4 = "price"
                java.lang.String r17 = r0.optString(r4)
                r4 = 1
                java.lang.String r6 = "select"
                boolean r19 = r0.optBoolean(r6, r4)
                r4 = r18
                r6 = r2
                r15 = r16
                r16 = r17
                r17 = r19
                r4.<init>(r5, r6, r7, r8, r10, r12, r13, r14, r15, r16, r17)
                return r18
            */
            throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xuiservice.egg.Egg.EggParserInterceptor.parseAction(org.json.JSONObject, java.lang.String, java.lang.String):com.xiaopeng.xuiservice.smart.action.Action");
        }

        private String generateResourceId(int resourceType) {
            int id = this.idRecord.get(resourceType) + 1;
            this.idRecord.put(resourceType, id);
            return "EGG_" + Egg.this.getId() + "_" + resourceType + "_" + id;
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Egg egg = (Egg) o;
        return Objects.equals(this.id, egg.id);
    }

    public int hashCode() {
        return Objects.hash(this.id);
    }

    public String toString() {
        return "Egg{id='" + this.id + "', version='" + this.version + "', displayName='" + this.displayName + "', image='" + this.image + "', startTime=" + DateTimeFormatUtil.format(DateTimeFormatUtil.FULL_DATE_TIME_FORMAT, new Date(this.startTime)) + ", expireTime=" + DateTimeFormatUtil.format(DateTimeFormatUtil.FULL_DATE_TIME_FORMAT, new Date(this.expireTime)) + ", eggDir='" + this.eggDir + "'}";
    }
}
