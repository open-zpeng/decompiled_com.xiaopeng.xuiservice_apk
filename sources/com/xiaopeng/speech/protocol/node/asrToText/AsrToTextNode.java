package com.xiaopeng.speech.protocol.node.asrToText;

import android.text.TextUtils;
import com.blankj.utilcode.constant.TimeConstants;
import com.xiaopeng.speech.SpeechClient;
import com.xiaopeng.speech.asr.RecognizeListener;
import com.xiaopeng.speech.asr.Recognizer;
import com.xiaopeng.speech.common.SpeechConstant;
import com.xiaopeng.speech.common.util.LogUtils;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class AsrToTextNode {
    private AsrToTextListener mListener;
    private Recognizer mRecognizer;
    private String TAG = "AsrToTextNode";
    private final RecognizeListener recognizeListener = new RecognizeListener() { // from class: com.xiaopeng.speech.protocol.node.asrToText.AsrToTextNode.1
        volatile int mState;
        boolean isEnd = false;
        String resultStr = "";

        @Override // com.xiaopeng.speech.asr.RecognizeListener
        public void onResult(String result, boolean last) {
            String str = AsrToTextNode.this.TAG;
            LogUtils.i(str, "on result: " + result + ", last: " + last + ",state:" + this.mState);
            try {
                if (!TextUtils.isEmpty(result)) {
                    JSONObject resultObj = new JSONObject(result);
                    JSONObject asr_result = new JSONObject();
                    String text = resultObj.optString("text");
                    this.resultStr = replaceBlank(text);
                    if (this.mState == 3 && !last && TextUtils.isEmpty(text)) {
                        asr_result.put("text", text);
                        asr_result.put("last", true);
                        asr_result.put("messageCode", "401");
                    } else if (this.mState != 4 || !TextUtils.isEmpty(text)) {
                        asr_result.put("text", replaceBlank(text));
                        asr_result.put("last", last);
                        asr_result.put("messageCode", "200");
                    } else {
                        asr_result.put("text", text);
                        asr_result.put("last", true);
                        asr_result.put("messageCode", "401");
                    }
                    if (last) {
                        this.isEnd = true;
                        this.resultStr = "";
                    }
                    if (AsrToTextNode.this.mListener != null) {
                        AsrToTextNode.this.mListener.onResult(asr_result.toString());
                    }
                }
            } catch (JSONException e) {
                e.fillInStackTrace();
            }
        }

        private String replaceBlank(String str) {
            if (TextUtils.isEmpty(str)) {
                return "";
            }
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            String dest = m.replaceAll("");
            return dest;
        }

        @Override // com.xiaopeng.speech.asr.RecognizeListener
        public void onError(int code, String info) {
            String str = AsrToTextNode.this.TAG;
            LogUtils.e(str, "on error: " + code + ", info: " + info);
            try {
                JSONObject asr_result = new JSONObject();
                asr_result.put("messageCode", "" + code);
                if (AsrToTextNode.this.mListener != null) {
                    AsrToTextNode.this.mListener.onResult(asr_result.toString());
                }
            } catch (JSONException e) {
                e.fillInStackTrace();
            }
        }

        @Override // com.xiaopeng.speech.asr.RecognizeListener
        public void onState(int state, int code) {
            String str = AsrToTextNode.this.TAG;
            LogUtils.d(str, "on onState " + state + ", info: " + code);
            this.mState = state;
            if (state == 6) {
                try {
                    if (!this.isEnd) {
                        try {
                            JSONObject asr_result = new JSONObject();
                            asr_result.put("text", this.resultStr);
                            asr_result.put("last", true);
                            if (TextUtils.isEmpty(this.resultStr)) {
                                asr_result.put("messageCode", "401");
                            } else {
                                asr_result.put("messageCode", "200");
                            }
                            if (AsrToTextNode.this.mListener != null) {
                                AsrToTextNode.this.mListener.onResult(asr_result.toString());
                            }
                        } catch (JSONException e) {
                            e.fillInStackTrace();
                        }
                        return;
                    }
                    this.resultStr = "";
                    this.isEnd = false;
                } finally {
                    this.resultStr = "";
                }
            }
        }

        @Override // com.xiaopeng.speech.asr.RecognizeListener
        public void onExtra(int type, int arg1, int arg2, String info, byte[] buffer) {
        }
    };

    /* loaded from: classes.dex */
    private static class SingleHolder {
        static final AsrToTextNode INSTANCE = new AsrToTextNode();

        private SingleHolder() {
        }
    }

    public static AsrToTextNode getInstance() {
        return SingleHolder.INSTANCE;
    }

    public void initRecord(String param, AsrToTextListener listener) {
        String str = this.TAG;
        LogUtils.i(str, "initRecognizer:" + param);
        this.mRecognizer = SpeechClient.instance().getRecognizer();
        this.mListener = listener;
        if (TextUtils.isEmpty(param) || param.equals("null")) {
            initDefaultRecord();
            return;
        }
        try {
            JSONObject paramObj = new JSONObject(param);
            Iterator<String> iterator = paramObj.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Object value = paramObj.opt(key);
                if (value instanceof Integer) {
                    this.mRecognizer.setInt(key, ((Integer) value).intValue());
                } else if (value instanceof Boolean) {
                    this.mRecognizer.setBool(key, ((Boolean) value).booleanValue());
                } else if (value instanceof String) {
                    this.mRecognizer.setString(key, (String) value);
                }
            }
        } catch (JSONException e) {
            e.fillInStackTrace();
        }
    }

    public void initRecord(AsrToTextListener listener) {
        initRecord(null, listener);
    }

    public void startRecord(String packageName) {
        startRecord(packageName, null);
    }

    public void startRecord(String packageName, String param) {
        try {
            String str = this.TAG;
            LogUtils.i(str, "startRecord:" + param + ",packageName:" + packageName);
            String result = SpeechClient.instance().getSpeechState().canPerformASRToText(packageName);
            if (TextUtils.isEmpty(result)) {
                if (this.mListener != null) {
                    JSONObject asr_result = new JSONObject();
                    asr_result.put("messageCode", "501");
                    this.mListener.onResult(asr_result.toString());
                    return;
                }
                return;
            }
            String message = new JSONObject(result).optString("messageCode");
            if (TextUtils.isEmpty(message)) {
                if (this.mRecognizer == null) {
                    this.mRecognizer = SpeechClient.instance().getRecognizer();
                    initDefaultRecord();
                }
                if (this.mRecognizer != null && !this.mRecognizer.isListening()) {
                    startListen(param);
                }
            } else if (this.mListener != null) {
                this.mListener.onResult(result);
            }
        } catch (JSONException e) {
            e.fillInStackTrace();
        }
    }

    private void startListen(String param) {
        try {
            String str = this.TAG;
            LogUtils.i(str, "startListen param:" + param);
            if (!TextUtils.isEmpty(param) && !param.equals("null")) {
                JSONObject paramObj = new JSONObject(param);
                Iterator<String> iterator = paramObj.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    Object value = paramObj.opt(key);
                    if (value instanceof Integer) {
                        this.mRecognizer.setInt(key, ((Integer) value).intValue());
                    } else if (value instanceof Boolean) {
                        this.mRecognizer.setBool(key, ((Boolean) value).booleanValue());
                    } else if (value instanceof String) {
                        this.mRecognizer.setString(key, (String) value);
                    }
                }
                this.mRecognizer.startListening(this.recognizeListener);
            }
            this.mRecognizer.setString(Recognizer.AUDIO_SAVE_PATH, null);
            this.mRecognizer.setInt(Recognizer.EOS, 1000);
            this.mRecognizer.setInt(Recognizer.BOS, SpeechConstant.VAD_TIMEOUT);
            this.mRecognizer.setInt(Recognizer.MAX_ACTIVE_TIME, TimeConstants.MIN);
            this.mRecognizer.setBool(Recognizer.DISABLE_ASR, false);
            this.mRecognizer.startListening(this.recognizeListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void stopRecord() {
        LogUtils.i(this.TAG, "stopRecord");
        Recognizer recognizer = this.mRecognizer;
        if (recognizer != null && recognizer.isListening()) {
            this.mRecognizer.stopListening();
        }
    }

    public void destroyRecord() {
        LogUtils.i(this.TAG, "destroyRecord");
        Recognizer recognizer = this.mRecognizer;
        if (recognizer != null) {
            recognizer.cancel();
            this.mRecognizer = null;
        }
        this.mListener = null;
    }

    private void initDefaultRecord() {
        Recognizer recognizer = this.mRecognizer;
        if (recognizer != null) {
            recognizer.setString(Recognizer.AUDIO_SAVE_PATH, null);
            this.mRecognizer.setBool(Recognizer.KEEP_AUDIO_RECORD, true);
            this.mRecognizer.setBool(Recognizer.ENABLE_ASR_PUNCT, true);
            this.mRecognizer.setBool(Recognizer.ASR_BUFFER, false);
            this.mRecognizer.setInt(Recognizer.AUDIO_FORMAT, 1);
        }
    }
}
