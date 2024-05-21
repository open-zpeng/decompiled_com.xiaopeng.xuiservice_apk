package com.xiaopeng.speech.protocol.node.navi.bean;

import android.text.TextUtils;
import com.alibaba.fastjson.parser.JSONLexer;
import com.xiaopeng.speech.common.util.DontProguardClass;
import com.xiaopeng.speech.common.util.LogUtils;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import org.json.JSONException;
import org.json.JSONObject;
@DontProguardClass
/* loaded from: classes.dex */
public class NaviPreferenceBean {
    public static final String ATMS = "atms";
    public static final int ATMS_ID = 300;
    public static final String AVOID_CARPOOL = "avoid carpool";
    public static final String AVOID_COUNTRY_BORDER = "avoid country border";
    public static final String AVOID_FERRIES = "avoid ferries";
    public static final String AVOID_HIGHWAY = "avoid highway";
    public static final String AVOID_TOLL = "avoid toll";
    public static final String AVOID_TUNNEL = "avoid tunnel";
    public static final String AVOID_UNPAVED = "avoid unpaved";
    public static final String CAFE = "coffee";
    public static final int CAFE_ID = 301;
    public static final String CARPOOL = "carpool lanes";
    public static final String CHARGING_STATION = "charging station";
    public static final int CHARGING_STATION_ID = 307;
    public static final String COUNTRY_BORDER = "country borders";
    public static final String ECO_FRIENDLY = "eco";
    public static final String FASTEST = "fastest";
    public static final String FAST_FOOD = "fast food";
    public static final int FAST_FOOD_ID = 304;
    public static final String FERRIES = "ferries";
    public static final String GROCERY = "grocery";
    public static final int GROCERY_ID = 303;
    public static final String HIGHWAY = "highways";
    public static final String HOTEL = "hotel";
    public static final int HOTEL_ID = 302;
    public static final String PARKING_LOT = "parking lot";
    public static final int PARKING_LOT_ID = 306;
    public static final int PATH_PREF_AVOID_CARPOOL = 207;
    public static final int PATH_PREF_AVOID_COUNTRY_BORDER = 213;
    public static final int PATH_PREF_AVOID_FERRIES = 205;
    public static final int PATH_PREF_AVOID_HIGHWAY = 209;
    public static final int PATH_PREF_AVOID_TOLL = 201;
    public static final int PATH_PREF_AVOID_TUNNEL = 203;
    public static final int PATH_PREF_AVOID_UNPAVED = 211;
    public static final int PATH_PREF_CARPOOL = 206;
    public static final int PATH_PREF_COUNTRY_BORDER = 212;
    public static final int PATH_PREF_ECO_FRIENDLY = 101;
    public static final int PATH_PREF_FASTEST = 100;
    public static final int PATH_PREF_FERRIES = 204;
    public static final int PATH_PREF_HIGHWAY = 208;
    public static final int PATH_PREF_SHORTEST = 102;
    public static final int PATH_PREF_TOLL = 200;
    public static final int PATH_PREF_TUNNEL = 202;
    public static final int PATH_PREF_UNPAVED = 210;
    public static final String RESTAURANT = "restaurant";
    public static final int RESTAURANT_ID = 305;
    public static final String SAFETY_ALERT = "safety alert";
    public static final String SHORTEST = "shortest";
    private static final String TAG = "NaviPreferenceBean";
    public static final String TOLL = "toll road";
    public static final String TOLL_GATE_ALERT = "toll gate alert";
    public static final String TRAFFIC_CAMERA_ALERT = "traffic camera alert";
    public static final String TRAFFIC_EVENT_ALERT = "traffic event alert";
    public static final String TUNNEL = "tunnels";
    public static final String UNPAVED = "unpaved road";
    private int mEnable;
    private String mPref;
    private int mPrefId;

    public static NaviPreferenceBean fromJson(String data) {
        NaviPreferenceBean pref = new NaviPreferenceBean();
        try {
            JSONObject json = new JSONObject(data);
            pref.mPref = json.optString(SpeechConstants.KEY_MODE);
            pref.mPrefId = prefNameToPrefId(pref.mPref);
            pref.mEnable = json.optInt("switch");
            LogUtils.d(TAG, "fromJson, pref:" + pref.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pref;
    }

    public String getPref() {
        return this.mPref;
    }

    public int getPrefId() {
        return this.mPrefId;
    }

    public boolean isEnable() {
        return this.mEnable == 1;
    }

    public static int prefNameToPrefId(String pref) {
        if (TextUtils.isEmpty(pref)) {
            return 0;
        }
        char c = 65535;
        switch (pref.hashCode()) {
            case -2097684128:
                if (pref.equals(AVOID_UNPAVED)) {
                    c = 19;
                    break;
                }
                break;
            case -1772467395:
                if (pref.equals(RESTAURANT)) {
                    c = 29;
                    break;
                }
                break;
            case -1749203455:
                if (pref.equals(AVOID_COUNTRY_BORDER)) {
                    c = 22;
                    break;
                }
                break;
            case -1722267927:
                if (pref.equals(PARKING_LOT)) {
                    c = 30;
                    break;
                }
                break;
            case -1684221946:
                if (pref.equals(HIGHWAY)) {
                    c = 14;
                    break;
                }
                break;
            case -1355030580:
                if (pref.equals(CAFE)) {
                    c = 25;
                    break;
                }
                break;
            case -1262772667:
                if (pref.equals(AVOID_CARPOOL)) {
                    c = '\b';
                    break;
                }
                break;
            case -1122290598:
                if (pref.equals("eco friendly")) {
                    c = 2;
                    break;
                }
                break;
            case -1077115990:
                if (pref.equals(FASTEST)) {
                    c = 0;
                    break;
                }
                break;
            case -969179893:
                if (pref.equals(TUNNEL)) {
                    c = '\t';
                    break;
                }
                break;
            case -963579080:
                if (pref.equals(FERRIES)) {
                    c = '\f';
                    break;
                }
                break;
            case -901578142:
                if (pref.equals(AVOID_HIGHWAY)) {
                    c = 16;
                    break;
                }
                break;
            case -862547864:
                if (pref.equals("tunnel")) {
                    c = '\n';
                    break;
                }
                break;
            case -597819771:
                if (pref.equals(CHARGING_STATION)) {
                    c = 31;
                    break;
                }
                break;
            case -342498422:
                if (pref.equals(SHORTEST)) {
                    c = 23;
                    break;
                }
                break;
            case -280604405:
                if (pref.equals("unpaved")) {
                    c = 18;
                    break;
                }
                break;
            case -104723179:
                if (pref.equals(UNPAVED)) {
                    c = 17;
                    break;
                }
                break;
            case -85459550:
                if (pref.equals(FAST_FOOD)) {
                    c = 28;
                    break;
                }
                break;
            case 100241:
                if (pref.equals(ECO_FRIENDLY)) {
                    c = 1;
                    break;
                }
                break;
            case 3004697:
                if (pref.equals(ATMS)) {
                    c = 24;
                    break;
                }
                break;
            case 3565883:
                if (pref.equals("toll")) {
                    c = 4;
                    break;
                }
                break;
            case 99467700:
                if (pref.equals(HOTEL)) {
                    c = JSONLexer.EOI;
                    break;
                }
                break;
            case 292882701:
                if (pref.equals(GROCERY)) {
                    c = 27;
                    break;
                }
                break;
            case 554307056:
                if (pref.equals("carpool")) {
                    c = 7;
                    break;
                }
                break;
            case 915501581:
                if (pref.equals("highway")) {
                    c = 15;
                    break;
                }
                break;
            case 924752343:
                if (pref.equals(CARPOOL)) {
                    c = 6;
                    break;
                }
                break;
            case 1157046643:
                if (pref.equals(AVOID_TUNNEL)) {
                    c = 11;
                    break;
                }
                break;
            case 1158255333:
                if (pref.equals(TOLL)) {
                    c = 3;
                    break;
                }
                break;
            case 1359855878:
                if (pref.equals(AVOID_TOLL)) {
                    c = 5;
                    break;
                }
                break;
            case 1410404157:
                if (pref.equals(COUNTRY_BORDER)) {
                    c = 20;
                    break;
                }
                break;
            case 1514308493:
                if (pref.equals(AVOID_FERRIES)) {
                    c = '\r';
                    break;
                }
                break;
            case 1569517558:
                if (pref.equals("country border")) {
                    c = 21;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return 100;
            case 1:
            case 2:
                return 101;
            case 3:
            case 4:
                return 200;
            case 5:
                return 201;
            case 6:
            case 7:
                return PATH_PREF_CARPOOL;
            case '\b':
                return PATH_PREF_AVOID_CARPOOL;
            case '\t':
            case '\n':
                return PATH_PREF_TUNNEL;
            case 11:
                return PATH_PREF_AVOID_TUNNEL;
            case '\f':
                return PATH_PREF_FERRIES;
            case '\r':
                return PATH_PREF_AVOID_FERRIES;
            case 14:
            case 15:
                return PATH_PREF_HIGHWAY;
            case 16:
                return PATH_PREF_AVOID_HIGHWAY;
            case 17:
            case 18:
                return PATH_PREF_UNPAVED;
            case 19:
                return PATH_PREF_AVOID_UNPAVED;
            case 20:
            case 21:
                return PATH_PREF_COUNTRY_BORDER;
            case 22:
                return PATH_PREF_AVOID_COUNTRY_BORDER;
            case 23:
                return 102;
            case 24:
                return 300;
            case 25:
                return 301;
            case 26:
                return 302;
            case 27:
                return 303;
            case 28:
                return 304;
            case 29:
                return 305;
            case 30:
                return 306;
            case 31:
                return 307;
            default:
                return 0;
        }
    }

    public String toString() {
        return "NaviPreferenceBean{pref='" + this.mPref + "', prefId='" + this.mPrefId + "', enable='" + this.mEnable + "'}";
    }
}
