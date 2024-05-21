package com.xiaopeng.speech.protocol.node.fm;

import com.xiaopeng.speech.SpeechNode;
import com.xiaopeng.speech.annotation.SpeechAnnotation;
import com.xiaopeng.speech.protocol.event.FMEvent;
/* loaded from: classes.dex */
public class FMNode extends SpeechNode<FMListener> {
    /* JADX INFO: Access modifiers changed from: protected */
    @SpeechAnnotation(event = FMEvent.FM_LOCAL_ON)
    public void onFmLocalOn(String event, String data) {
        Object[] listenerList = this.mListenerList.collectCallbacks();
        if (listenerList != null) {
            for (Object obj : listenerList) {
                ((FMListener) obj).onFmLocalOn();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @SpeechAnnotation(event = FMEvent.FM_LOCAL_OFF)
    public void onFmLocalOff(String event, String data) {
        Object[] listenerList = this.mListenerList.collectCallbacks();
        if (listenerList != null) {
            for (Object obj : listenerList) {
                ((FMListener) obj).onFmLocalOff();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @SpeechAnnotation(event = FMEvent.FM_NETWORK_ON)
    public void onFmNetworkOn(String event, String data) {
        Object[] listenerList = this.mListenerList.collectCallbacks();
        if (listenerList != null) {
            for (Object obj : listenerList) {
                ((FMListener) obj).onFmNetworkOn();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @SpeechAnnotation(event = FMEvent.FM_NETWORK_OFF)
    public void onFmNetworkOff(String event, String data) {
        Object[] listenerList = this.mListenerList.collectCallbacks();
        if (listenerList != null) {
            for (Object obj : listenerList) {
                ((FMListener) obj).onFmNetworkOff();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @SpeechAnnotation(event = FMEvent.FM_PLAY_CHANNEL)
    public void onFmPlayChannel(String event, String data) {
        Object[] listenerList = this.mListenerList.collectCallbacks();
        if (listenerList != null) {
            for (Object obj : listenerList) {
                ((FMListener) obj).onFmPlayChannel(data);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @SpeechAnnotation(event = FMEvent.FM_PLAY_NAME)
    public void onFmPlayChannelName(String event, String data) {
        Object[] listenerList = this.mListenerList.collectCallbacks();
        if (listenerList != null) {
            for (Object obj : listenerList) {
                ((FMListener) obj).onFmPlayChannelName(event, data);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @SpeechAnnotation(event = FMEvent.FM_PLAY_COLLECT)
    public void onPlayCollectFM(String event, String data) {
        Object[] listenerList = this.mListenerList.collectCallbacks();
        if (listenerList != null) {
            for (Object obj : listenerList) {
                ((FMListener) obj).onPlayCollectFM();
            }
        }
    }
}
