package com.xiaopeng.speech.protocol.node.fm;

import com.xiaopeng.speech.annotation.ICommandProcessor;
import com.xiaopeng.speech.protocol.event.FMEvent;
/* loaded from: classes.dex */
public class FMNode_Processor implements ICommandProcessor {
    private FMNode mTarget;

    public FMNode_Processor(FMNode target) {
        this.mTarget = target;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.xiaopeng.speech.annotation.ICommandProcessor
    public void performCommand(String event, String data) {
        char c;
        switch (event.hashCode()) {
            case -1616140812:
                if (event.equals(FMEvent.FM_LOCAL_OFF)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -627784393:
                if (event.equals(FMEvent.FM_NETWORK_OFF)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -297345769:
                if (event.equals(FMEvent.FM_NETWORK_ON)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -190680902:
                if (event.equals(FMEvent.FM_LOCAL_ON)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 616790465:
                if (event.equals(FMEvent.FM_PLAY_CHANNEL)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 1124241696:
                if (event.equals(FMEvent.FM_PLAY_COLLECT)) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 2103491480:
                if (event.equals(FMEvent.FM_PLAY_NAME)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                this.mTarget.onFmLocalOn(event, data);
                return;
            case 1:
                this.mTarget.onFmLocalOff(event, data);
                return;
            case 2:
                this.mTarget.onFmNetworkOn(event, data);
                return;
            case 3:
                this.mTarget.onFmNetworkOff(event, data);
                return;
            case 4:
                this.mTarget.onFmPlayChannel(event, data);
                return;
            case 5:
                this.mTarget.onFmPlayChannelName(event, data);
                return;
            case 6:
                this.mTarget.onPlayCollectFM(event, data);
                return;
            default:
                return;
        }
    }

    @Override // com.xiaopeng.speech.annotation.ICommandProcessor
    public String[] getSubscribeEvents() {
        return new String[]{FMEvent.FM_LOCAL_ON, FMEvent.FM_LOCAL_OFF, FMEvent.FM_NETWORK_ON, FMEvent.FM_NETWORK_OFF, FMEvent.FM_PLAY_CHANNEL, FMEvent.FM_PLAY_NAME, FMEvent.FM_PLAY_COLLECT};
    }
}
