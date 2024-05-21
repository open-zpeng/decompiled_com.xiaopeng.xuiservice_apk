package com.xiaopeng.xuiservice.xapp.speech;

import android.text.TextUtils;
import com.xiaopeng.speech.protocol.SpeechModel;
import com.xiaopeng.speech.protocol.event.query.QueryAppAndAppletEvent;
import com.xiaopeng.speech.protocol.event.query.QueryVideoEvent;
import com.xiaopeng.speech.protocol.node.video.VideoListener;
import com.xiaopeng.speech.protocol.node.video.VideoNode;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.xapp.XAppManagerService;
import com.xiaopeng.xuiservice.xapp.XAppStartManager;
import com.xiaopeng.xuiservice.xapp.video.Callback;
import com.xiaopeng.xuiservice.xapp.video.VideoManager;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class SpeechVideoModel extends SpeechModel implements VideoListener {
    private static final String TAG = "SpeechVideoModel";
    private VideoManager mVideoManager = VideoManager.getInstance();

    public void subscribe() {
        LogUtil.d(TAG, "subscribe SpeechVideoModel");
        subscribe(VideoNode.class, this);
    }

    @Override // com.xiaopeng.speech.protocol.node.video.VideoListener
    public void onVideoPrev(String data) {
        LogUtil.d(TAG, "onVideoPrev data:" + data);
        String pkgName = getPkgName(data);
        if (!TextUtils.isEmpty(pkgName)) {
            VideoManager.getInstance().prev(pkgName, new Callback() { // from class: com.xiaopeng.xuiservice.xapp.speech.SpeechVideoModel.1
                @Override // com.xiaopeng.xuiservice.xapp.video.Callback
                public void onResult(int code) {
                    com.xiaopeng.speech.protocol.SpeechUtils.replyValue(QueryVideoEvent.VIDEO_PLAY_PREV, Integer.valueOf(code));
                }
            });
        }
    }

    @Override // com.xiaopeng.speech.protocol.node.video.VideoListener
    public void onVideoNext(String data) {
        LogUtil.d(TAG, "onVideoNext data:" + data);
        String pkgName = getPkgName(data);
        if (!TextUtils.isEmpty(pkgName)) {
            VideoManager.getInstance().next(pkgName, new Callback() { // from class: com.xiaopeng.xuiservice.xapp.speech.SpeechVideoModel.2
                @Override // com.xiaopeng.xuiservice.xapp.video.Callback
                public void onResult(int code) {
                    com.xiaopeng.speech.protocol.SpeechUtils.replyValue(QueryVideoEvent.VIDEO_PLAY_NEXT, Integer.valueOf(code));
                }
            });
        }
    }

    @Override // com.xiaopeng.speech.protocol.node.video.VideoListener
    public void onVideoSet(String data) {
        LogUtil.d(TAG, "onVideoSet data:" + data);
        if (!TextUtils.isEmpty(data)) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                String pkgName = jsonObject.optString(SpeechConstants.KEY_PACKAGE_NAME, "");
                int videoSet = jsonObject.optInt("value", 1);
                if (!TextUtils.isEmpty(pkgName)) {
                    VideoManager.getInstance().set(pkgName, videoSet, new Callback() { // from class: com.xiaopeng.xuiservice.xapp.speech.SpeechVideoModel.3
                        @Override // com.xiaopeng.xuiservice.xapp.video.Callback
                        public void onResult(int code) {
                            com.xiaopeng.speech.protocol.SpeechUtils.replyValue(QueryVideoEvent.VIDEO_PLAY_SET, Integer.valueOf(code));
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.xiaopeng.speech.protocol.node.video.VideoListener
    public void onVideoForward(String data) {
        LogUtil.d(TAG, "onVideoForward data:" + data);
        String pkgName = getPkgName(data);
        if (!TextUtils.isEmpty(pkgName)) {
            VideoManager.getInstance().forward(pkgName, new Callback() { // from class: com.xiaopeng.xuiservice.xapp.speech.SpeechVideoModel.4
                @Override // com.xiaopeng.xuiservice.xapp.video.Callback
                public void onResult(int code) {
                    com.xiaopeng.speech.protocol.SpeechUtils.replyValue(QueryVideoEvent.VIDEO_PLAY_FORWARD, Integer.valueOf(code));
                }
            });
        }
    }

    @Override // com.xiaopeng.speech.protocol.node.video.VideoListener
    public void onVideoBackward(String data) {
        LogUtil.d(TAG, "onVideoBackward data:" + data);
        String pkgName = getPkgName(data);
        if (!TextUtils.isEmpty(pkgName)) {
            VideoManager.getInstance().backward(pkgName, new Callback() { // from class: com.xiaopeng.xuiservice.xapp.speech.SpeechVideoModel.5
                @Override // com.xiaopeng.xuiservice.xapp.video.Callback
                public void onResult(int code) {
                    com.xiaopeng.speech.protocol.SpeechUtils.replyValue(QueryVideoEvent.VIDEO_PLAY_BACKWARD, Integer.valueOf(code));
                }
            });
        }
    }

    @Override // com.xiaopeng.speech.protocol.node.video.VideoListener
    public void onVideoSettime(String data) {
        LogUtil.d(TAG, "onVideoSetTime data:" + data);
        if (!TextUtils.isEmpty(data)) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                String pkgName = jsonObject.optString(SpeechConstants.KEY_PACKAGE_NAME, "");
                double videoSetTime = jsonObject.optDouble("time", 30.0d);
                if (!TextUtils.isEmpty(pkgName)) {
                    VideoManager.getInstance().setTime(pkgName, videoSetTime, new Callback() { // from class: com.xiaopeng.xuiservice.xapp.speech.SpeechVideoModel.6
                        @Override // com.xiaopeng.xuiservice.xapp.video.Callback
                        public void onResult(int code) {
                            com.xiaopeng.speech.protocol.SpeechUtils.replyValue(QueryVideoEvent.VIDEO_PLAY_SETTIME, Integer.valueOf(code));
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.xiaopeng.speech.protocol.node.video.VideoListener
    public void onVideoPause(String data) {
        LogUtil.d(TAG, "onVideoPause data:" + data);
        String pkgName = getPkgName(data);
        if (!TextUtils.isEmpty(pkgName)) {
            VideoManager.getInstance().pause(pkgName, new Callback() { // from class: com.xiaopeng.xuiservice.xapp.speech.SpeechVideoModel.7
                @Override // com.xiaopeng.xuiservice.xapp.video.Callback
                public void onResult(int code) {
                    com.xiaopeng.speech.protocol.SpeechUtils.replyValue(QueryVideoEvent.VIDEO_PLAY_PAUSE, Integer.valueOf(code));
                }
            });
        }
    }

    @Override // com.xiaopeng.speech.protocol.node.video.VideoListener
    public void onVideoResume(String data) {
        LogUtil.d(TAG, "onVideoResume data:" + data);
        String pkgName = getPkgName(data);
        if (!TextUtils.isEmpty(pkgName)) {
            VideoManager.getInstance().resume(pkgName, new Callback() { // from class: com.xiaopeng.xuiservice.xapp.speech.SpeechVideoModel.8
                @Override // com.xiaopeng.xuiservice.xapp.video.Callback
                public void onResult(int code) {
                    com.xiaopeng.speech.protocol.SpeechUtils.replyValue(QueryVideoEvent.VIDEO_PLAY_RESUME, Integer.valueOf(code));
                }
            });
        }
    }

    @Override // com.xiaopeng.speech.protocol.node.video.VideoListener
    public void onVideoSpeedUp(String data) {
        LogUtil.d(TAG, "onVideoSpeedUp data:" + data);
        String pkgName = getPkgName(data);
        if (!TextUtils.isEmpty(pkgName)) {
            VideoManager.getInstance().speedUp(pkgName, new Callback() { // from class: com.xiaopeng.xuiservice.xapp.speech.SpeechVideoModel.9
                @Override // com.xiaopeng.xuiservice.xapp.video.Callback
                public void onResult(int code) {
                    com.xiaopeng.speech.protocol.SpeechUtils.replyValue(QueryVideoEvent.VIDEO_PLAY_SPEED_UP, Integer.valueOf(code));
                }
            });
        }
    }

    @Override // com.xiaopeng.speech.protocol.node.video.VideoListener
    public void onVideoSpeedDown(String data) {
        LogUtil.d(TAG, "onVideoSpeedDown data:" + data);
        String pkgName = getPkgName(data);
        if (!TextUtils.isEmpty(pkgName)) {
            VideoManager.getInstance().speedDown(pkgName, new Callback() { // from class: com.xiaopeng.xuiservice.xapp.speech.SpeechVideoModel.10
                @Override // com.xiaopeng.xuiservice.xapp.video.Callback
                public void onResult(int code) {
                    com.xiaopeng.speech.protocol.SpeechUtils.replyValue(QueryVideoEvent.VIDEO_PLAY_SPEED_DOWN, Integer.valueOf(code));
                }
            });
        }
    }

    @Override // com.xiaopeng.speech.protocol.node.video.VideoListener
    public void onVideoSpeedSet(String data) {
        LogUtil.d(TAG, "onVideoSpeedSet data:" + data);
        if (!TextUtils.isEmpty(data)) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                String pkgName = jsonObject.optString(SpeechConstants.KEY_PACKAGE_NAME, "");
                double videoSpeedSet = jsonObject.optDouble("speed", 1.0d);
                if (!TextUtils.isEmpty(pkgName)) {
                    VideoManager.getInstance().speedSet(pkgName, videoSpeedSet, new Callback() { // from class: com.xiaopeng.xuiservice.xapp.speech.SpeechVideoModel.11
                        @Override // com.xiaopeng.xuiservice.xapp.video.Callback
                        public void onResult(int code) {
                            com.xiaopeng.speech.protocol.SpeechUtils.replyValue(QueryVideoEvent.VIDEO_PLAY_SPEED_SET, Integer.valueOf(code));
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.xiaopeng.speech.protocol.node.video.VideoListener
    public void onVideoEnd(String data) {
        LogUtil.d(TAG, "onVideoEnd data:" + data);
        String pkgName = getPkgName(data);
        if (!TextUtils.isEmpty(pkgName)) {
            VideoManager.getInstance().end(pkgName, new Callback() { // from class: com.xiaopeng.xuiservice.xapp.speech.SpeechVideoModel.12
                @Override // com.xiaopeng.xuiservice.xapp.video.Callback
                public void onResult(int code) {
                    com.xiaopeng.speech.protocol.SpeechUtils.replyValue(QueryVideoEvent.VIDEO_PLAY_END, Integer.valueOf(code));
                }
            });
        }
    }

    @Override // com.xiaopeng.speech.protocol.node.video.VideoListener
    public void onVideoClarityUP(String data) {
        LogUtil.d(TAG, "onVideoClarityUP data:" + data);
        String pkgName = getPkgName(data);
        if (!TextUtils.isEmpty(pkgName)) {
            VideoManager.getInstance().clarityUP(pkgName, new Callback() { // from class: com.xiaopeng.xuiservice.xapp.speech.SpeechVideoModel.13
                @Override // com.xiaopeng.xuiservice.xapp.video.Callback
                public void onResult(int code) {
                    com.xiaopeng.speech.protocol.SpeechUtils.replyValue(QueryVideoEvent.VIDEO_PLAY_CLARITY_UP, Integer.valueOf(code));
                }
            });
        }
    }

    @Override // com.xiaopeng.speech.protocol.node.video.VideoListener
    public void onVideoClarityDown(String data) {
        LogUtil.d(TAG, "onVideoClarityDown data:" + data);
        String pkgName = getPkgName(data);
        if (!TextUtils.isEmpty(pkgName)) {
            VideoManager.getInstance().clarityDown(pkgName, new Callback() { // from class: com.xiaopeng.xuiservice.xapp.speech.SpeechVideoModel.14
                @Override // com.xiaopeng.xuiservice.xapp.video.Callback
                public void onResult(int code) {
                    com.xiaopeng.speech.protocol.SpeechUtils.replyValue(QueryVideoEvent.VIDEO_PLAY_CLARITY_DOWN, Integer.valueOf(code));
                }
            });
        }
    }

    @Override // com.xiaopeng.speech.protocol.node.video.VideoListener
    public void onVideoClaritySet(String data) {
        LogUtil.d(TAG, "onVideoClaritySet data:" + data);
        if (!TextUtils.isEmpty(data)) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                String pkgName = jsonObject.optString(SpeechConstants.KEY_PACKAGE_NAME, "");
                String claritySet = jsonObject.optString("value", "");
                if (!TextUtils.isEmpty(pkgName) && !TextUtils.isEmpty(claritySet)) {
                    VideoManager.getInstance().claritySet(pkgName, claritySet, new Callback() { // from class: com.xiaopeng.xuiservice.xapp.speech.SpeechVideoModel.15
                        @Override // com.xiaopeng.xuiservice.xapp.video.Callback
                        public void onResult(int code) {
                            com.xiaopeng.speech.protocol.SpeechUtils.replyValue(QueryVideoEvent.VIDEO_PLAY_CLARITY_SET, Integer.valueOf(code));
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.xiaopeng.speech.protocol.node.video.VideoListener
    public void onVideoAudioMode(String data) {
        LogUtil.d(TAG, "onVideoAudioMode data:" + data);
        if (!TextUtils.isEmpty(data)) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                String pkgName = jsonObject.optString(SpeechConstants.KEY_PACKAGE_NAME, "");
                String mode = jsonObject.optString(SpeechConstants.KEY_MODE, "");
                if (!TextUtils.isEmpty(pkgName) && !TextUtils.isEmpty(mode)) {
                    if ("media".equals(mode)) {
                        VideoManager.getInstance().setMediaPlayMode(pkgName, new Callback() { // from class: com.xiaopeng.xuiservice.xapp.speech.SpeechVideoModel.16
                            @Override // com.xiaopeng.xuiservice.xapp.video.Callback
                            public void onResult(int code) {
                                com.xiaopeng.speech.protocol.SpeechUtils.replyValue(QueryVideoEvent.VIDEO_PLAY_AUDIO_MODE, Integer.valueOf(code));
                            }
                        });
                    } else if ("video".equals(mode)) {
                        VideoManager.getInstance().setVideoPlayMode(pkgName, new Callback() { // from class: com.xiaopeng.xuiservice.xapp.speech.SpeechVideoModel.17
                            @Override // com.xiaopeng.xuiservice.xapp.video.Callback
                            public void onResult(int code) {
                                com.xiaopeng.speech.protocol.SpeechUtils.replyValue(QueryVideoEvent.VIDEO_PLAY_AUDIO_MODE, Integer.valueOf(code));
                            }
                        });
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.xiaopeng.speech.protocol.node.video.VideoListener
    public void onVideoCollect(String data) {
        LogUtil.d(TAG, "onVideoCollect data:" + data);
        String pkgName = getPkgName(data);
        if (!TextUtils.isEmpty(pkgName)) {
            VideoManager.getInstance().collect(pkgName, new Callback() { // from class: com.xiaopeng.xuiservice.xapp.speech.SpeechVideoModel.18
                @Override // com.xiaopeng.xuiservice.xapp.video.Callback
                public void onResult(int code) {
                    com.xiaopeng.speech.protocol.SpeechUtils.replyValue(QueryVideoEvent.VIDEO_PLAY_COLLECT, Integer.valueOf(code));
                }
            });
        }
    }

    @Override // com.xiaopeng.speech.protocol.node.video.VideoListener
    public void onVideoCollectCancel(String data) {
        LogUtil.d(TAG, "onVideoCollectCancel data:" + data);
        String pkgName = getPkgName(data);
        if (!TextUtils.isEmpty(pkgName)) {
            VideoManager.getInstance().collectCancel(pkgName, new Callback() { // from class: com.xiaopeng.xuiservice.xapp.speech.SpeechVideoModel.19
                @Override // com.xiaopeng.xuiservice.xapp.video.Callback
                public void onResult(int code) {
                    com.xiaopeng.speech.protocol.SpeechUtils.replyValue(QueryVideoEvent.VIDEO_PLAY_COLLECT_CANCEL, Integer.valueOf(code));
                }
            });
        }
    }

    @Override // com.xiaopeng.speech.protocol.node.video.VideoListener
    public void onVideoSkipBegin(String data) {
        LogUtil.d(TAG, "onVideoSkipBegin data:" + data);
        String pkgName = getPkgName(data);
        if (!TextUtils.isEmpty(pkgName)) {
            VideoManager.getInstance().skipBegin(pkgName, new Callback() { // from class: com.xiaopeng.xuiservice.xapp.speech.SpeechVideoModel.20
                @Override // com.xiaopeng.xuiservice.xapp.video.Callback
                public void onResult(int code) {
                    com.xiaopeng.speech.protocol.SpeechUtils.replyValue(QueryVideoEvent.VIDEO_SKIP_BEGIN, Integer.valueOf(code));
                }
            });
        }
    }

    @Override // com.xiaopeng.speech.protocol.node.video.VideoListener
    public void onVideoSkipEnd(String data) {
        LogUtil.d(TAG, "onVideoSkipEnd data:" + data);
        String pkgName = getPkgName(data);
        if (!TextUtils.isEmpty(pkgName)) {
            VideoManager.getInstance().skipEnd(pkgName, new Callback() { // from class: com.xiaopeng.xuiservice.xapp.speech.SpeechVideoModel.21
                @Override // com.xiaopeng.xuiservice.xapp.video.Callback
                public void onResult(int code) {
                    com.xiaopeng.speech.protocol.SpeechUtils.replyValue(QueryVideoEvent.VIDEO_SKIP_END, Integer.valueOf(code));
                }
            });
        }
    }

    @Override // com.xiaopeng.speech.protocol.node.video.VideoListener
    public void onVideoDemand(String data) {
        LogUtil.d(TAG, "onVideoDemand data:" + data);
        if (!TextUtils.isEmpty(data)) {
            try {
                JSONObject object = new JSONObject(data);
                String pkgName = object.optString(SpeechConstants.KEY_PACKAGE_NAME, "");
                int displayId = SpeechUtils.getTargetDisplayId(object);
                if (TextUtils.isEmpty(pkgName)) {
                    pkgName = VideoManager.getInstance().getTargetApp(displayId);
                }
                if (!TextUtils.isEmpty(pkgName) && XAppManagerService.getInstance().checkAppInstalled(pkgName)) {
                    int startStatus = XAppStartManager.getInstance().checkAppStartStatus(pkgName, true, false, displayId);
                    if (startStatus == 0) {
                        String command = object.optString("command", "");
                        if (!TextUtils.isEmpty(command)) {
                            VideoManager.getInstance().play(displayId, pkgName, command, new Callback() { // from class: com.xiaopeng.xuiservice.xapp.speech.SpeechVideoModel.22
                                @Override // com.xiaopeng.xuiservice.xapp.video.Callback
                                public void onResult(int code) {
                                    int videoOperationCode = SpeechUtils.convertVideoOperationCode(code);
                                    com.xiaopeng.speech.protocol.SpeechUtils.replyValue(QueryAppAndAppletEvent.GUI_VIDEO_APP_DEMAND, Integer.valueOf(videoOperationCode));
                                }
                            });
                        }
                        return;
                    }
                    int videoDemandCode = SpeechUtils.convertStartCode(startStatus, pkgName, displayId);
                    com.xiaopeng.speech.protocol.SpeechUtils.replyValue(QueryAppAndAppletEvent.GUI_VIDEO_APP_DEMAND, Integer.valueOf(videoDemandCode));
                    return;
                }
                com.xiaopeng.speech.protocol.SpeechUtils.replyValue(QueryAppAndAppletEvent.GUI_VIDEO_APP_DEMAND, 4);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String getPkgName(String data) {
        if (TextUtils.isEmpty(data)) {
            return "";
        }
        try {
            JSONObject jsonObject = new JSONObject(data);
            String pkgName = jsonObject.optString(SpeechConstants.KEY_PACKAGE_NAME, "");
            return pkgName;
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}
