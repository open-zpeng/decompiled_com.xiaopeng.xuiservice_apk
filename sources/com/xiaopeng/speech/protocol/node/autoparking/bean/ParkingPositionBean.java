package com.xiaopeng.speech.protocol.node.autoparking.bean;

import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class ParkingPositionBean {
    private int position;

    public static final ParkingPositionBean fromJson(String data) {
        ParkingPositionBean positionBean = new ParkingPositionBean();
        try {
            JSONObject jsonObject = new JSONObject(data);
            positionBean.position = jsonObject.optInt("position");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return positionBean;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String toString() {
        return "ParkingPositionBean{position=" + this.position + '}';
    }
}
