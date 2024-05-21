package com.xiaopeng.speech.protocol.query.media;

import com.alibaba.fastjson.parser.JSONLexer;
import com.xiaopeng.speech.annotation.IQueryProcessor;
import com.xiaopeng.speech.protocol.event.query.QueryMediaEvent;
/* loaded from: classes2.dex */
public class MediaQuery_Processor implements IQueryProcessor {
    private MediaQuery mTarget;

    public MediaQuery_Processor(MediaQuery target) {
        this.mTarget = target;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.xiaopeng.speech.annotation.IQueryProcessor
    public Object querySensor(String event, String data) {
        char c;
        switch (event.hashCode()) {
            case -2048472570:
                if (event.equals(QueryMediaEvent.MEDIA_MUSIC_CONTROL_PLAYLIST_HISTORY_PLAY)) {
                    c = 28;
                    break;
                }
                c = 65535;
                break;
            case -1975677602:
                if (event.equals(QueryMediaEvent.PLAY_MODE)) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case -1975656786:
                if (event.equals(QueryMediaEvent.NEXT)) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case -1975591185:
                if (event.equals(QueryMediaEvent.PLAY)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -1975585298:
                if (event.equals(QueryMediaEvent.PREV)) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case -1975493699:
                if (event.equals(QueryMediaEvent.STOP)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -1840709480:
                if (event.equals(QueryMediaEvent.MEDIA_MUSIC_CONTROL_PLAY)) {
                    c = 19;
                    break;
                }
                c = 65535;
                break;
            case -1762191375:
                if (event.equals(QueryMediaEvent.MEDIA_FM_PLAY_PERSONALITY)) {
                    c = 24;
                    break;
                }
                c = 65535;
                break;
            case -1713758055:
                if (event.equals(QueryMediaEvent.MEDIA_MUSIC_CONTROL_BLUETOOTH_PLAY_RANDOM)) {
                    c = 25;
                    break;
                }
                c = 65535;
                break;
            case -1613078351:
                if (event.equals(QueryMediaEvent.SPEECHSET)) {
                    c = 16;
                    break;
                }
                c = 65535;
                break;
            case -1570967877:
                if (event.equals(QueryMediaEvent.FORWARD)) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case -1211213464:
                if (event.equals(QueryMediaEvent.PLAY_MODE_CLOSE)) {
                    c = 29;
                    break;
                }
                c = 65535;
                break;
            case -1159670343:
                if (event.equals(QueryMediaEvent.CANCEL_COLLECT)) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case -1125776995:
                if (event.equals(QueryMediaEvent.CLOSE)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -1114093157:
                if (event.equals(QueryMediaEvent.PAUSE)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -965975055:
                if (event.equals(QueryMediaEvent.MEDIA_MUSIC_PLAY_LIST)) {
                    c = 17;
                    break;
                }
                c = 65535;
                break;
            case -931837998:
                if (event.equals(QueryMediaEvent.GET_INFO_QUERY)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -841982244:
                if (event.equals(QueryMediaEvent.MEDIA_MUSIC_CONTROL_COLLECT_PLAY)) {
                    c = 27;
                    break;
                }
                c = 65535;
                break;
            case -738040633:
                if (event.equals(QueryMediaEvent.MEDIA_XMLY_CONTROL_PLAY)) {
                    c = 20;
                    break;
                }
                c = 65535;
                break;
            case -655902248:
                if (event.equals(QueryMediaEvent.MEDIA_MUSIC_DAILYREC_PLAY)) {
                    c = 23;
                    break;
                }
                c = 65535;
                break;
            case -116254424:
                if (event.equals(QueryMediaEvent.RESUME)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 183299757:
                if (event.equals(QueryMediaEvent.BACKWARD)) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case 225059948:
                if (event.equals(QueryMediaEvent.SPEEDUP)) {
                    c = 14;
                    break;
                }
                c = 65535;
                break;
            case 282775051:
                if (event.equals(QueryMediaEvent.MEDIA_AUDIOBOOK_PLAY_LIST)) {
                    c = 18;
                    break;
                }
                c = 65535;
                break;
            case 542977935:
                if (event.equals(QueryMediaEvent.CONTROL_COLLECT)) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 559194844:
                if (event.equals(QueryMediaEvent.MEDIA_MUSIC_NEWS_PLAY)) {
                    c = 22;
                    break;
                }
                c = 65535;
                break;
            case 1092119077:
                if (event.equals(QueryMediaEvent.SETTIME)) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            case 1108993407:
                if (event.equals(QueryMediaEvent.MEDIA_FM_LOCAL_ON)) {
                    c = 21;
                    break;
                }
                c = 65535;
                break;
            case 1312662318:
                if (event.equals(QueryMediaEvent.MEDIA_MUSIC_CONTROL_PLAY_USB)) {
                    c = JSONLexer.EOI;
                    break;
                }
                c = 65535;
                break;
            case 1533741619:
                if (event.equals(QueryMediaEvent.SPEEDDOWN)) {
                    c = 15;
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
                return this.mTarget.getMediaInfo(event, data);
            case 1:
                return Integer.valueOf(this.mTarget.play(event, data));
            case 2:
                return Integer.valueOf(this.mTarget.pause(event, data));
            case 3:
                return Integer.valueOf(this.mTarget.resume(event, data));
            case 4:
                return Integer.valueOf(this.mTarget.stop(event, data));
            case 5:
                return Integer.valueOf(this.mTarget.close(event, data));
            case 6:
                return Integer.valueOf(this.mTarget.prev(event, data));
            case 7:
                return Integer.valueOf(this.mTarget.next(event, data));
            case '\b':
                return Integer.valueOf(this.mTarget.playMode(event, data));
            case '\t':
                return Integer.valueOf(this.mTarget.collect(event, data));
            case '\n':
                return Integer.valueOf(this.mTarget.cancelCollect(event, data));
            case 11:
                return Integer.valueOf(this.mTarget.forward(event, data));
            case '\f':
                return Integer.valueOf(this.mTarget.backward(event, data));
            case '\r':
                return Integer.valueOf(this.mTarget.setTime(event, data));
            case 14:
                return Integer.valueOf(this.mTarget.speedUp(event, data));
            case 15:
                return Integer.valueOf(this.mTarget.speedDown(event, data));
            case 16:
                return Integer.valueOf(this.mTarget.speedSet(event, data));
            case 17:
                return Integer.valueOf(this.mTarget.mediaListPlay(event, data));
            case 18:
                return Integer.valueOf(this.mTarget.mediaAudioBookListPlay(event, data));
            case 19:
                return Integer.valueOf(this.mTarget.mediaMusicPlay(event, data));
            case 20:
                return Integer.valueOf(this.mTarget.mediaAudioBookPlay(event, data));
            case 21:
                return Integer.valueOf(this.mTarget.mediaFmLocalOn(event, data));
            case 22:
                return Integer.valueOf(this.mTarget.mediaMusicNewsPlay(event, data));
            case 23:
                return Integer.valueOf(this.mTarget.mediaMusicDailyrecPlay(event, data));
            case 24:
                return Integer.valueOf(this.mTarget.mediaMusicPersonalityPlay(event, data));
            case 25:
                return Integer.valueOf(this.mTarget.mediaBluetoothPlay(event, data));
            case 26:
                return Integer.valueOf(this.mTarget.mediaUsbPlay(event, data));
            case 27:
                return Integer.valueOf(this.mTarget.mediaCollectPlay(event, data));
            case 28:
                return Integer.valueOf(this.mTarget.mediaHistoryListPlay(event, data));
            case 29:
                return Integer.valueOf(this.mTarget.playModeClose(event, data));
            default:
                return null;
        }
    }

    @Override // com.xiaopeng.speech.annotation.IQueryProcessor
    public String[] getQueryEvents() {
        return new String[]{QueryMediaEvent.GET_INFO_QUERY, QueryMediaEvent.PLAY, QueryMediaEvent.PAUSE, QueryMediaEvent.RESUME, QueryMediaEvent.STOP, QueryMediaEvent.CLOSE, QueryMediaEvent.PREV, QueryMediaEvent.NEXT, QueryMediaEvent.PLAY_MODE, QueryMediaEvent.CONTROL_COLLECT, QueryMediaEvent.CANCEL_COLLECT, QueryMediaEvent.FORWARD, QueryMediaEvent.BACKWARD, QueryMediaEvent.SETTIME, QueryMediaEvent.SPEEDUP, QueryMediaEvent.SPEEDDOWN, QueryMediaEvent.SPEECHSET, QueryMediaEvent.MEDIA_MUSIC_PLAY_LIST, QueryMediaEvent.MEDIA_AUDIOBOOK_PLAY_LIST, QueryMediaEvent.MEDIA_MUSIC_CONTROL_PLAY, QueryMediaEvent.MEDIA_XMLY_CONTROL_PLAY, QueryMediaEvent.MEDIA_FM_LOCAL_ON, QueryMediaEvent.MEDIA_MUSIC_NEWS_PLAY, QueryMediaEvent.MEDIA_MUSIC_DAILYREC_PLAY, QueryMediaEvent.MEDIA_FM_PLAY_PERSONALITY, QueryMediaEvent.MEDIA_MUSIC_CONTROL_BLUETOOTH_PLAY_RANDOM, QueryMediaEvent.MEDIA_MUSIC_CONTROL_PLAY_USB, QueryMediaEvent.MEDIA_MUSIC_CONTROL_COLLECT_PLAY, QueryMediaEvent.MEDIA_MUSIC_CONTROL_PLAYLIST_HISTORY_PLAY, QueryMediaEvent.PLAY_MODE_CLOSE};
    }
}
