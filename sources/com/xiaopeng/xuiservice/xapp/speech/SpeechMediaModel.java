package com.xiaopeng.xuiservice.xapp.speech;

import android.os.Bundle;
import android.text.TextUtils;
import com.xiaopeng.speech.protocol.SpeechModel;
import com.xiaopeng.speech.protocol.event.MusicEvent;
import com.xiaopeng.speech.protocol.node.media.MediaTriggerListener;
import com.xiaopeng.speech.protocol.node.media.MediaTriggerNode;
import com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller;
import com.xiaopeng.speech.protocol.query.media.MediaQuery;
import com.xiaopeng.xuimanager.mediacenter.MediaInfo;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.mediacenter.Constants;
import com.xiaopeng.xuiservice.mediacenter.MediaCenterHalService;
import com.xiaopeng.xuiservice.mediacenter.utils.DialogUtils;
import com.xiaopeng.xuiservice.xapp.media.MediaManager;
import com.xiaopeng.xuiservice.xapp.util.ResourceUtil;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class SpeechMediaModel extends SpeechModel implements IMediaQueryCaller, MediaTriggerListener {
    private static final String TAG = "SpeechMediaModel";
    private MediaPlayCommandBean mDemandCommand;
    private int mBotDialogDisplayId = 0;
    private boolean mDemandMediaDialog = false;

    public void subscribe() {
        LogUtil.d(TAG, "subscribe SpeechMediaModel");
        subscribe(MediaQuery.class, this);
        subscribe(MediaTriggerNode.class, this);
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int play(String data) {
        int displayId = getTargetDisplayId(data);
        int currentStatus = MediaCenterHalService.getInstance().getCurrentPlayStatus(displayId);
        LogUtil.i(TAG, "onPlayCommand displayId:" + displayId + " &currentStatus:" + currentStatus);
        if (currentStatus != 0) {
            return executeCmd(displayId, 2);
        }
        return 0;
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int pause(String data) {
        int displayId = getTargetDisplayId(data);
        int currentStatus = MediaCenterHalService.getInstance().getCurrentPlayStatus(displayId);
        LogUtil.i(TAG, "onPauseCommand displayId:" + displayId + " &currentStatus:" + currentStatus);
        if (currentStatus == 0) {
            return executeCmd(displayId, 2);
        }
        return 0;
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int resume(String data) {
        int displayId = getTargetDisplayId(data);
        int currentStatus = MediaCenterHalService.getInstance().getCurrentPlayStatus(displayId);
        LogUtil.i(TAG, "onResumeCommand displayId:" + displayId + " &currentStatus:" + currentStatus);
        if (currentStatus != 0) {
            return executeCmd(displayId, 2);
        }
        return 0;
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int stop(String data) {
        int displayId = getTargetDisplayId(data);
        int currentStatus = MediaCenterHalService.getInstance().getCurrentPlayStatus(displayId);
        LogUtil.i(TAG, "onStopCommand displayId:" + displayId + " &currentStatus:" + currentStatus);
        if (currentStatus == 0) {
            return executeCmd(displayId, 2);
        }
        return 0;
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int close(String data) {
        int displayId = getTargetDisplayId(data);
        int currentStatus = MediaCenterHalService.getInstance().getCurrentPlayStatus(displayId);
        LogUtil.i(TAG, "onCloseCommand displayId:" + displayId + " &currentStatus:" + currentStatus);
        if (currentStatus == 0) {
            return executeCmd(displayId, 2);
        }
        return 0;
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int prev(String data) {
        int displayId = getTargetDisplayId(data);
        MediaCenterHalService.getInstance().getCurrentPlaybackState(displayId);
        return executeCmd(displayId, 7);
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int next(String data) {
        int displayId = getTargetDisplayId(data);
        MediaCenterHalService.getInstance().getCurrentPlaybackState(displayId);
        return executeCmd(displayId, 6);
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int playMode(String data) {
        if (!TextUtils.isEmpty(data)) {
            try {
                JSONObject object = new JSONObject(data);
                int displayId = getTargetDisplayId(object);
                String mode = object.optString(SpeechConstants.KEY_MODE, "");
                Bundle extra = new Bundle();
                extra.putString(Constants.KEY_TEXT_VALUE, mode);
                return executeCustomAction(displayId, 9, extra);
            } catch (JSONException e) {
                e.printStackTrace();
                return 1;
            }
        }
        return 1;
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int playModeClose(String data) {
        if (!TextUtils.isEmpty(data)) {
            try {
                JSONObject object = new JSONObject(data);
                int displayId = getTargetDisplayId(object);
                String mode = object.optString(SpeechConstants.KEY_MODE, "");
                Bundle extra = new Bundle();
                extra.putString(Constants.KEY_TEXT_VALUE, mode);
                return executeCustomAction(displayId, 17, extra);
            } catch (JSONException e) {
                e.printStackTrace();
                return 1;
            }
        }
        return 1;
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int collect(String data) {
        int displayId = getTargetDisplayId(data);
        return executeCmd(displayId, 8);
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int cancelCollect(String data) {
        int displayId = getTargetDisplayId(data);
        return executeCmd(displayId, 16);
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int forward(String data) {
        int displayId = getTargetDisplayId(data);
        MediaInfo mediaInfo = MediaCenterHalService.getInstance().getCurrentMediaInfo(displayId);
        if (mediaInfo.isXpMusic()) {
            MediaManager.getInstance().mediaPlay(displayId, Constants.PACKAGE_XP_MUSIC, MusicEvent.MUSIC_FORWARD, data);
            return 0;
        }
        return executeCmd(displayId, 14);
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int backward(String data) {
        int displayId = getTargetDisplayId(data);
        MediaCenterHalService.getInstance().getCurrentPlaybackState(displayId);
        MediaInfo mediaInfo = MediaCenterHalService.getInstance().getCurrentMediaInfo(displayId);
        if (mediaInfo.isXpMusic()) {
            MediaManager.getInstance().mediaPlay(displayId, Constants.PACKAGE_XP_MUSIC, MusicEvent.MUSIC_BACKWARD, data);
            return 0;
        }
        return executeCmd(displayId, 13);
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int speedDown(String data) {
        int displayId = getTargetDisplayId(data);
        MediaInfo mediaInfo = MediaCenterHalService.getInstance().getCurrentMediaInfo(displayId);
        if (mediaInfo.isXpMusic() && mediaInfo.getSource() == 2) {
            MediaManager.getInstance().mediaPlay(displayId, Constants.PACKAGE_XP_MUSIC, MusicEvent.MUSIC_SPEED_DOWN, "");
            return 0;
        }
        return 1;
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int speedSet(String data) {
        if (!TextUtils.isEmpty(data)) {
            try {
                JSONObject object = new JSONObject(data);
                int displayId = getTargetDisplayId(object);
                MediaInfo mediaInfo = MediaCenterHalService.getInstance().getCurrentMediaInfo(displayId);
                if (mediaInfo.isXpMusic() && mediaInfo.getSource() == 2) {
                    MediaManager.getInstance().mediaPlay(displayId, Constants.PACKAGE_XP_MUSIC, MusicEvent.MUSIC_SPEED_SET, data);
                    return 0;
                }
                double speed = object.optDouble("value", 0.0d);
                LogUtil.d(TAG, "speedSet data:" + data + " &speed:" + speed);
                Bundle extra = new Bundle();
                extra.putDouble(Constants.KEY_DOUBLE_VALUE, speed);
                return executeCustomAction(displayId, 5, extra);
            } catch (JSONException e) {
                e.printStackTrace();
                return 1;
            }
        }
        return 1;
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int speedUp(String data) {
        int displayId = getTargetDisplayId(data);
        MediaInfo mediaInfo = MediaCenterHalService.getInstance().getCurrentMediaInfo(displayId);
        if (mediaInfo.isXpMusic() && mediaInfo.getSource() == 2) {
            MediaManager.getInstance().mediaPlay(displayId, Constants.PACKAGE_XP_MUSIC, MusicEvent.MUSIC_SPEED_UP, "");
            return 0;
        }
        return 1;
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int setTime(String data) {
        if (!TextUtils.isEmpty(data)) {
            try {
                JSONObject object = new JSONObject(data);
                int displayId = getTargetDisplayId(object);
                long time = (long) object.optDouble("value", 0.0d);
                Bundle extra = new Bundle();
                extra.putLong(Constants.KEY_LONG_VALUE, time);
                MediaCenterHalService.getInstance().getCurrentPlaybackState(displayId);
                return executeCustomAction(displayId, 4, extra);
            } catch (JSONException e) {
                e.printStackTrace();
                return 1;
            }
        }
        return 1;
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int mediaMusicPlay(String data) {
        return mediaPlay(SpeechConstants.COMMAND_PLAY_MUSIC, data);
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int mediaAudioBookPlay(String data) {
        return mediaPlay(SpeechConstants.COMMAND_PLAY_BOOK, data);
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int mediaBluetoothPlay(String data) {
        return mediaPlay(SpeechConstants.COMMAND_PLAY_BLUETOOTH, data);
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int mediaCollectPlay(String data) {
        return mediaPlay(SpeechConstants.COMMAND_PLAY_COLLECT, data);
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int mediaHistoryListPlay(String data) {
        return mediaPlay(SpeechConstants.COMMAND_PLAY_HISTORY, data);
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int mediaFmLocalOn(String data) {
        return mediaPlay(SpeechConstants.COMMAND_PLAY_LOCAL_FM, data);
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int mediaMusicDailyrecPlay(String data) {
        return mediaPlay(SpeechConstants.COMMAND_PLAY_DAILY_RECOMMEND, data);
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int mediaMusicNewsPlay(String data) {
        return mediaPlay(SpeechConstants.COMMAND_PLAY_NEWS, data);
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int mediaMusicPersonalityPlay(String data) {
        return mediaPlay(SpeechConstants.COMMAND_PLAY_PERSON_CHANNEL, data);
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int mediaUsbPlay(String data) {
        return mediaPlay(SpeechConstants.COMMAND_PLAY_USB, data);
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int mediaListPlay(String data) {
        return mediaPlay(SpeechConstants.COMMAND_PLAY_MUSIC_LIST, data);
    }

    @Override // com.xiaopeng.speech.protocol.query.media.IMediaQueryCaller
    public int mediaAudioBookListPlay(String data) {
        return mediaPlay(SpeechConstants.COMMAND_PLAY_BOOK_LIST, data);
    }

    private int mediaPlay(String type, String data) {
        LogUtil.d(TAG, "mediaPlay type:" + type + " &data:" + data);
        StringBuilder sb = new StringBuilder();
        sb.append("command://");
        sb.append(type);
        String commandType = sb.toString();
        if (!TextUtils.isEmpty(data)) {
            try {
                JSONObject object = new JSONObject(data);
                int displayId = getTargetDisplayId(object);
                String command = object.optString(SpeechConstants.KEY_SONG_INFO);
                String pkgName = object.optString(SpeechConstants.KEY_PACKAGE_NAME);
                if (isShowSeizeDialog(displayId, pkgName)) {
                    this.mDemandMediaDialog = true;
                    this.mDemandCommand = new MediaPlayCommandBean(displayId, pkgName, commandType, command);
                    showBotDialog(displayId);
                    return 2;
                }
                MediaManager.getInstance().mediaPlay(displayId, pkgName, commandType, command);
                return 0;
            } catch (JSONException e) {
                e.printStackTrace();
                return 1;
            }
        }
        return 0;
    }

    private boolean isShowSeizeDialog(int displayId) {
        return MediaManager.getInstance().isShowSeizeDialog(displayId);
    }

    private boolean isShowSeizeDialog(int displayId, String pkgName) {
        return MediaManager.getInstance().isShowSeizeDialog(displayId, pkgName);
    }

    private void dismissBotDialog(boolean confirm) {
        LogUtil.d(TAG, "dismissBotDialog with:" + confirm);
        DialogUtils.dismissSeizeDialog(confirm);
    }

    private void showBotDialog(final int displayId) {
        LogUtil.d(TAG, "showBotDialog displayId:" + displayId);
        this.mBotDialogDisplayId = displayId;
        int msgId = displayId == 0 ? R.string.dialog_media_seize_primary_msg : R.string.dialog_media_seize_second_msg;
        if (displayId == 0) {
        }
        String ttsText = ResourceUtil.getString(msgId);
        int okTextId = displayId == 0 ? R.string.dialog_media_seize_primary_confirm_text : R.string.dialog_media_seize_second_confirm_text;
        String okText = ResourceUtil.getString(okTextId);
        String cancelText = ResourceUtil.getString(R.string.dialog_media_seize_cancel_text);
        ((MediaTriggerNode) com.xiaopeng.speech.protocol.SpeechUtils.getNode(MediaTriggerNode.class)).sendTrigger(ttsText, okText, cancelText);
        DialogUtils.showSeizeDialog(displayId, new DialogUtils.OnDialogConfirm() { // from class: com.xiaopeng.xuiservice.xapp.speech.SpeechMediaModel.1
            @Override // com.xiaopeng.xuiservice.mediacenter.utils.DialogUtils.OnDialogConfirm
            public void onConfirm() {
                SpeechMediaModel.this.onBotConfirm(displayId);
            }

            @Override // com.xiaopeng.xuiservice.mediacenter.utils.DialogUtils.OnDialogConfirm
            public void onDismiss() {
                LogUtil.d(SpeechMediaModel.TAG, "MediaTriggerListener leaveTrigger");
                ((MediaTriggerNode) com.xiaopeng.speech.protocol.SpeechUtils.getNode(MediaTriggerNode.class)).leaveTrigger();
            }
        });
    }

    @Override // com.xiaopeng.speech.protocol.node.media.MediaTriggerListener
    public void onSubmit() {
        LogUtil.d(TAG, "MediaTriggerListener onSubmit with displayId:" + this.mBotDialogDisplayId);
        onBotConfirm(this.mBotDialogDisplayId);
    }

    @Override // com.xiaopeng.speech.protocol.node.media.MediaTriggerListener
    public void onCancel() {
        LogUtil.d(TAG, "MediaTriggerListener onCancel with displayId:" + this.mBotDialogDisplayId);
        onBotCancel(this.mBotDialogDisplayId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBotConfirm(int displayId) {
        dismissBotDialog(true);
        if (!this.mDemandMediaDialog || this.mDemandCommand == null) {
            MediaManager.getInstance().enterCurrent(displayId);
            return;
        }
        MediaManager.getInstance().enterTargetPackage(displayId, this.mDemandCommand.pkgName);
        MediaManager.getInstance().mediaPlay(displayId, this.mDemandCommand.pkgName, this.mDemandCommand.commandType, this.mDemandCommand.command);
    }

    private void onBotCancel(int displayId) {
        dismissBotDialog(false);
    }

    private int executeCmd(String data, int cmd) {
        LogUtil.d(TAG, "executeCmd data:" + data + " &cmd:" + cmd);
        int displayId = getTargetDisplayId(data);
        if (isShowSeizeDialog(displayId)) {
            this.mDemandMediaDialog = false;
            showBotDialog(displayId);
            return 2;
        }
        MediaCenterHalService.getInstance().playbackControlInner(displayId, cmd, 0);
        return 0;
    }

    private int executeCmd(int displayId, int cmd) {
        LogUtil.i(TAG, "executeCmd displayId:" + displayId + " &cmd:" + cmd);
        if (isShowSeizeDialog(displayId)) {
            this.mDemandMediaDialog = false;
            showBotDialog(displayId);
            return 2;
        }
        boolean success = MediaCenterHalService.getInstance().playbackControlInner(displayId, cmd, 0);
        return !success;
    }

    private int executeCustomAction(int displayId, int cmd, Bundle extra) {
        LogUtil.i(TAG, "executeCmd displayId:" + displayId + " &cmd:" + cmd + " &extra:" + extra);
        if (isShowSeizeDialog(displayId)) {
            this.mDemandMediaDialog = false;
            showBotDialog(displayId);
            return 2;
        }
        boolean result = MediaCenterHalService.getInstance().sendCustomAction(displayId, cmd, extra);
        return !result;
    }

    private int getTargetDisplayId(JSONObject jsonObject) {
        return SpeechUtils.getTargetDisplayId(jsonObject);
    }

    private int getTargetDisplayId(String jsonString) {
        return SpeechUtils.getTargetDisplayId(jsonString);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class MediaPlayCommandBean {
        private String command;
        private String commandType;
        private int displayId;
        private String pkgName;

        public MediaPlayCommandBean(int id, String pkgName, String type, String detail) {
            this.displayId = id;
            this.pkgName = pkgName;
            this.commandType = type;
            this.command = detail;
        }
    }
}
