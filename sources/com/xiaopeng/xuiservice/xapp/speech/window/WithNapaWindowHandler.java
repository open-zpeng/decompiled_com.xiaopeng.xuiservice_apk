package com.xiaopeng.xuiservice.xapp.speech.window;

import android.text.TextUtils;
import com.alipay.mobile.aromeservice.RequestParams;
import com.xiaopeng.app.xpDialogInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.utils.SharedDisplayManager;
import com.xiaopeng.xuiservice.xapp.Constants;
import com.xiaopeng.xuiservice.xapp.XAppManagerService;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class WithNapaWindowHandler extends TopWindowHandler {
    private static final String TAG = "WithNapaWindowHandler";

    @Override // com.xiaopeng.xuiservice.xapp.speech.window.TopWindowHandler
    public int closeTopWindow(int displayId) {
        LogUtil.d(TAG, "closeTopWindow displayId:" + displayId);
        xpDialogInfo dialogInfo = XAppManagerService.getInstance().getShowDialogByWms(displayId);
        if (dialogInfo != null) {
            return closeSystemDialog(displayId, dialogInfo);
        }
        if (!isNapaTop(displayId)) {
            return closeTopActivity(displayId);
        }
        return 4;
    }

    @Override // com.xiaopeng.xuiservice.xapp.speech.window.TopWindowHandler
    public boolean isHaveNotCancelableDialog(int displayId) {
        return this.mXAppManagerService.isHaveNotCancelableDialog(displayId);
    }

    @Override // com.xiaopeng.xuiservice.xapp.speech.window.TopWindowHandler
    public int closeDialog(int displayId) {
        xpDialogInfo dialogInfo = XAppManagerService.getInstance().getShowDialogByWms(displayId);
        if (dialogInfo != null && dialogInfo.hasFocus && !Constants.PACKAGE_NAPA.equals(dialogInfo.packageName)) {
            return closeSystemDialog(displayId, dialogInfo);
        }
        if (getNapaDialogInfo(displayId) != null) {
            return closeNapaInnerDialog(displayId);
        }
        return 2;
    }

    private int closeSystemDialog(int displayId, xpDialogInfo dialogInfo) {
        LogUtil.d(TAG, "closeSystemDialog displayId:" + displayId + " &dialogInfo:" + dialogInfo.toString());
        if (dialogInfo.closeOutside) {
            XAppManagerService.getInstance().dismissDialogAsync(displayId);
            return 1;
        }
        return 3;
    }

    private boolean isNapaTop(int displayId) {
        String topPackage = XAppManagerService.getInstance().getTopPackage(displayId);
        LogUtil.d(TAG, "isNapaTop topPackage:" + topPackage);
        return Constants.PACKAGE_NAPA.equals(topPackage);
    }

    private int closeNapaInnerWindow(int displayId) {
        return 1;
    }

    private int closeNapaInnerDialog(int displayId) {
        return 1;
    }

    private DialogInfo getNapaDialogInfo(int displayId) {
        String dialogString = SharedDisplayManager.getInstance().getNapaDialogString();
        if (!TextUtils.isEmpty(dialogString)) {
            try {
                JSONArray jsonArray = new JSONArray(dialogString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.optInt("id", -2) == displayId) {
                        DialogInfo dialogInfo = new DialogInfo();
                        dialogInfo.set(jsonObject);
                        return dialogInfo;
                    }
                }
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public class DialogInfo {
        public static final int NAPA_DIALOG_INVALID_ID = -2;
        public static final int NAPA_DIALOG_NOT_CANCELABLE = 10;
        public int height;
        public int id;
        public int mode;
        public String packageName;
        public int state;
        public int type;
        public int width;
        public int x;
        public int y;

        public DialogInfo() {
        }

        public void set(JSONObject jsonObject) {
            this.id = jsonObject.optInt("id", -2);
            this.type = jsonObject.optInt(SpeechConstants.KEY_COMMAND_TYPE, -1);
            this.mode = jsonObject.optInt(SpeechConstants.KEY_MODE, 0);
            this.state = jsonObject.optInt("state", 0);
            this.x = jsonObject.optInt("x", 0);
            this.y = jsonObject.optInt("y", 0);
            this.width = jsonObject.optInt("width", 0);
            this.height = jsonObject.optInt("height", 0);
            this.packageName = jsonObject.optString(RequestParams.REQUEST_KEY_PACKAGE_NAME, "");
        }
    }
}
