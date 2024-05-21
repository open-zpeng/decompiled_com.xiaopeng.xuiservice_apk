package com.xiaopeng.xuiservice.xapp.speech;

import com.xiaopeng.speech.protocol.SpeechModel;
import com.xiaopeng.speech.protocol.query.appstore.AppAndAppletQuery;
import com.xiaopeng.speech.protocol.query.appstore.IAppAndAppletCaller;
import com.xiaopeng.speech.protocol.query.music.ISingQueryCaller;
import com.xiaopeng.speech.protocol.query.music.SingQuery;
import com.xiaopeng.speech.protocol.query.system.ControlPanelQuery;
import com.xiaopeng.speech.protocol.query.system.IControlPanelCaller;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.xapp.Constants;
/* loaded from: classes5.dex */
public class SpeechAppQueryModel extends SpeechModel implements IAppAndAppletCaller, ISingQueryCaller, IControlPanelCaller {
    private static final String TAG = "SpeechAppQueryModel";
    private SpeechController mSpeechController = new SpeechController();

    public void subscribe() {
        LogUtil.i(TAG, "subscribe SpeechAppQueryModel");
        subscribe(AppAndAppletQuery.class, this);
        subscribe(SingQuery.class, this);
        subscribe(ControlPanelQuery.class, this);
    }

    @Override // com.xiaopeng.speech.protocol.query.appstore.IAppAndAppletCaller
    public int onOpenAppStoreMainPage(String data) {
        int result = this.mSpeechController.startXpAppStore(data, Constants.ACTION_APP_LIST);
        LogUtil.d(TAG, "onOpenAppStoreMainPage result:" + result);
        return result;
    }

    @Override // com.xiaopeng.speech.protocol.query.appstore.IAppAndAppletCaller
    public int onCloseAppStoreMainPage() {
        int result = this.mSpeechController.closeXpAppStore();
        LogUtil.d(TAG, "onCloseAppStoreMainPage result:" + result);
        return result;
    }

    @Override // com.xiaopeng.speech.protocol.query.appstore.IAppAndAppletCaller
    public int onOpenAppletMainPage(String data) {
        int result = this.mSpeechController.startXpAppStore(data, Constants.ACTION_APPLET_LIST);
        LogUtil.d(TAG, "onOpenAppletMainPage result:" + result);
        return result;
    }

    @Override // com.xiaopeng.speech.protocol.query.appstore.IAppAndAppletCaller
    public int onCloseAppletMainPage() {
        int result = this.mSpeechController.closeXpAppStore();
        LogUtil.d(TAG, "onCloseAppletMainPage result:" + result);
        return result;
    }

    @Override // com.xiaopeng.speech.protocol.query.appstore.IAppAndAppletCaller
    public int onOpenAppshopPage(String data) {
        int result = this.mSpeechController.startXpAppStore(data, Constants.ACTION_APP_STORE);
        LogUtil.d(TAG, "onOpenAppshopPage result:" + result);
        return result;
    }

    @Override // com.xiaopeng.speech.protocol.query.appstore.IAppAndAppletCaller
    public int onCloseAppshopPage() {
        int result = this.mSpeechController.closeXpAppStore();
        LogUtil.d(TAG, "onCloseAppshopPage result:" + result);
        return result;
    }

    @Override // com.xiaopeng.speech.protocol.query.appstore.IAppAndAppletCaller
    public int getCurrentCloseStatus(String data) {
        int result = this.mSpeechController.closeCurrentApp(data);
        LogUtil.d(TAG, "getCurrentCloseStatus: " + data + " &result:" + result);
        return result;
    }

    @Override // com.xiaopeng.speech.protocol.query.appstore.IAppAndAppletCaller
    public int getAppOpenStatus(String data) {
        int result = this.mSpeechController.startApplication(data);
        LogUtil.d(TAG, "getAppOpenStatus: " + data + " &result:" + result);
        return result;
    }

    @Override // com.xiaopeng.speech.protocol.query.appstore.IAppAndAppletCaller
    public int getAppCloseStatus(String data) {
        int result = this.mSpeechController.stopApplication(data);
        LogUtil.d(TAG, "getAppCloseStatus: " + data + " &result:" + result);
        return result;
    }

    @Override // com.xiaopeng.speech.protocol.query.appstore.IAppAndAppletCaller
    public int getAppletOpenStatus(String data) {
        int result = this.mSpeechController.startApplet(data);
        LogUtil.d(TAG, "getAppletOpenStatus: " + data + " &result:" + result);
        return result;
    }

    @Override // com.xiaopeng.speech.protocol.query.appstore.IAppAndAppletCaller
    public int getAppletCloseStatus(String data) {
        int result = this.mSpeechController.stopApplet(data);
        LogUtil.d(TAG, "getAppletCloseStatus: " + data + " &result:" + result);
        return result;
    }

    @Override // com.xiaopeng.speech.protocol.query.appstore.IAppAndAppletCaller
    public boolean hasDialogCloseByHand() {
        boolean result = this.mSpeechController.isHaveNotCancelableDialog();
        LogUtil.i(TAG, "hasDialogCloseByHand: " + result);
        return result;
    }

    @Override // com.xiaopeng.speech.protocol.query.appstore.IAppAndAppletCaller
    public int getAppDownStatus(String data) {
        int result = this.mSpeechController.downloadApplication(data);
        LogUtil.d(TAG, "getAppDownStatus: " + data + " &result:" + result);
        return result;
    }

    @Override // com.xiaopeng.speech.protocol.query.appstore.IAppAndAppletCaller
    public int getCurrentBackStatus(String data) {
        int result = this.mSpeechController.currentBack(data);
        LogUtil.d(TAG, "getCurrentBackStatus: " + data + " &result:" + result);
        return result;
    }

    @Override // com.xiaopeng.speech.protocol.query.music.ISingQueryCaller
    public int getSingStatus(String data) {
        int result = this.mSpeechController.getSingStatus(data);
        LogUtil.d(TAG, "getSingStatus: " + data + " &result:" + result);
        return result;
    }

    @Override // com.xiaopeng.speech.protocol.query.system.IControlPanelCaller
    public int onBlueToothVolumeSet(int type) {
        int result = this.mSpeechController.adjustBluetoothVolume(type);
        LogUtil.d(TAG, "onBlueToothVolumeSet type: " + type + " &result:" + result);
        return result;
    }

    @Override // com.xiaopeng.speech.protocol.query.system.IControlPanelCaller
    public int onBlueToothVolumeSetValue(int value) {
        int result = this.mSpeechController.setBluetoothVolume(value);
        LogUtil.d(TAG, "onBlueToothVolumeSetValue value: " + value + " &result:" + result);
        return result;
    }
}
