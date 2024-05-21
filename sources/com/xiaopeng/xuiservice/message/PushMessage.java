package com.xiaopeng.xuiservice.message;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import androidx.annotation.NonNull;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.message.PushMessage;
import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import java.util.function.Consumer;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class PushMessage {
    private static final int MESSAGE_CENTER_TYPE = 56;
    private static final int MSG_CLOUD = 1;
    private static final int MSG_INTERNAL = 2;
    private static final String TAG = "xuimessage";
    private PushMessageListener allMessageListener;
    private Handler handler;
    private HandlerThread handlerThread;
    private final ArrayMap<String, ArraySet<PushMessageListener>> mMessageType2ListenersMap;

    /* loaded from: classes5.dex */
    public interface PushMessageListener {
        void onReceivePushMessage(String str, String str2);
    }

    /* loaded from: classes5.dex */
    private static class SingletonHolder {
        private static final PushMessage instance = new PushMessage();

        private SingletonHolder() {
        }
    }

    private PushMessage() {
        this.mMessageType2ListenersMap = new ArrayMap<>();
        this.handlerThread = new HandlerThread("XUIService Message Dispatcher");
        this.handlerThread.start();
        this.handler = new Handler(this.handlerThread.getLooper()) { // from class: com.xiaopeng.xuiservice.message.PushMessage.1
            @Override // android.os.Handler
            public void handleMessage(@NonNull Message msg) {
                String messageType = null;
                String data = null;
                int i = msg.what;
                if (i == 1) {
                    String bundle = (String) msg.obj;
                    try {
                        JSONObject jsonObject = new JSONObject(bundle);
                        JSONObject contentJsonObject = new JSONObject(new JSONObject(jsonObject.getString("string_msg")).getString("content"));
                        messageType = contentJsonObject.getString(SpeechConstants.KEY_COMMAND_TYPE);
                        data = contentJsonObject.optString("data");
                    } catch (Exception e) {
                        LogUtil.i(PushMessage.TAG, "parse message error, " + e.getMessage());
                        LogUtil.i(PushMessage.TAG, "message bundle: " + bundle);
                    }
                } else if (i == 2) {
                    MessageTypeData pair = (MessageTypeData) msg.obj;
                    messageType = pair.messageType;
                    data = pair.data;
                }
                if (!TextUtils.isEmpty(messageType)) {
                    PushMessage.this.notifyMessage(messageType, data);
                }
            }
        };
    }

    public static PushMessage getInstance() {
        return SingletonHolder.instance;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAllMessageListener(PushMessageListener allMessageListener) {
        this.allMessageListener = allMessageListener;
    }

    public void registerMessage(String messageType, PushMessageListener listener) {
        if (TextUtils.isEmpty(messageType)) {
            return;
        }
        synchronized (this.mMessageType2ListenersMap) {
            ArraySet<PushMessageListener> listeners = this.mMessageType2ListenersMap.get(messageType);
            if (listeners == null) {
                listeners = new ArraySet<>();
                this.mMessageType2ListenersMap.put(messageType, listeners);
            }
            listeners.add(listener);
        }
    }

    public void unregisterMessage(String messageType, PushMessageListener listener) {
        if (TextUtils.isEmpty(messageType)) {
            return;
        }
        synchronized (this.mMessageType2ListenersMap) {
            ArraySet<PushMessageListener> listeners = this.mMessageType2ListenersMap.get(messageType);
            if (listeners != null) {
                listeners.remove(listener);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyMessage(final String messageType, final String data) {
        LogUtil.i(TAG, "notifyMessage messageType=" + messageType + ", data=" + data);
        synchronized (this.mMessageType2ListenersMap) {
            ArraySet<PushMessageListener> listeners = this.mMessageType2ListenersMap.get(messageType);
            if (listeners != null) {
                listeners.forEach(new Consumer() { // from class: com.xiaopeng.xuiservice.message.-$$Lambda$PushMessage$JPv9CFKuWPW0UmR0hyUlyst72u0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((PushMessage.PushMessageListener) obj).onReceivePushMessage(messageType, data);
                    }
                });
            }
        }
        PushMessageListener listener = this.allMessageListener;
        if (listener != null) {
            listener.onReceivePushMessage(messageType, data);
        }
    }

    public void dispatchCloudMessage(String bundle) {
        Message.obtain(this.handler, 1, bundle).sendToTarget();
    }

    public void dispatchInternalMessage(String messageType, String data) {
        Message.obtain(this.handler, 2, new MessageTypeData(messageType, data)).sendToTarget();
    }

    /* loaded from: classes5.dex */
    private static final class MessageTypeData {
        String data;
        String messageType;

        public MessageTypeData(String messageType, String data) {
            this.messageType = messageType;
            this.data = data;
        }
    }
}
