package com.xiaopeng.speech.protocol.query.music;

import com.alibaba.fastjson.parser.JSONLexer;
import com.xiaopeng.speech.annotation.IQueryProcessor;
import com.xiaopeng.speech.protocol.event.query.QueryMusicEvent;
/* loaded from: classes2.dex */
public class MusicQuery_Processor implements IQueryProcessor {
    private MusicQuery mTarget;

    public MusicQuery_Processor(MusicQuery target) {
        this.mTarget = target;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.xiaopeng.speech.annotation.IQueryProcessor
    public Object querySensor(String event, String data) {
        char c;
        switch (event.hashCode()) {
            case -2139173272:
                if (event.equals(QueryMusicEvent.IS_SUPPORT_BT_PLAY)) {
                    c = 21;
                    break;
                }
                c = 65535;
                break;
            case -2135675479:
                if (event.equals(QueryMusicEvent.IS_PLAY_PODCAST)) {
                    c = 20;
                    break;
                }
                c = 65535;
                break;
            case -1560481235:
                if (event.equals(QueryMusicEvent.IS_KUGOU_AUTHED)) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case -1332544048:
                if (event.equals(QueryMusicEvent.IS_SUPPORT_SPOTIFY_PLAY)) {
                    c = 22;
                    break;
                }
                c = 65535;
                break;
            case -1316092175:
                if (event.equals(QueryMusicEvent.GET_INFO_QUERY)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -1275404518:
                if (event.equals(QueryMusicEvent.GET_SEARCH_RESULT)) {
                    c = 19;
                    break;
                }
                c = 65535;
                break;
            case -697358738:
                if (event.equals(QueryMusicEvent.IS_COLLECT_EMPTY)) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case -660651947:
                if (event.equals(QueryMusicEvent.IS_PLAY_HISTORY)) {
                    c = 18;
                    break;
                }
                c = 65535;
                break;
            case -638141205:
                if (event.equals(QueryMusicEvent.IS_PLAY_COLLECT)) {
                    c = 17;
                    break;
                }
                c = 65535;
                break;
            case -565545210:
                if (event.equals(QueryMusicEvent.MUSIC_GET_USB_STATE)) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            case -510014673:
                if (event.equals(QueryMusicEvent.IS_SUPPORT_RADIO_PLAY)) {
                    c = 25;
                    break;
                }
                c = 65535;
                break;
            case -481069680:
                if (event.equals(QueryMusicEvent.IS_CAN_COLLECTED)) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case -301186745:
                if (event.equals(QueryMusicEvent.IS_SUPPORT_SETTIME)) {
                    c = JSONLexer.EOI;
                    break;
                }
                c = 65535;
                break;
            case -289541544:
                if (event.equals(QueryMusicEvent.IS_HISTORY_EMPTY)) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 93854047:
                if (event.equals(QueryMusicEvent.IS_SUPPORT_SPEED)) {
                    c = 24;
                    break;
                }
                c = 65535;
                break;
            case 196105311:
                if (event.equals(QueryMusicEvent.MUSIC_ACCOUNT_LOGIN)) {
                    c = 14;
                    break;
                }
                c = 65535;
                break;
            case 291286140:
                if (event.equals(QueryMusicEvent.IS_XIMALAYA_ACCOUNT_LOGIN)) {
                    c = 16;
                    break;
                }
                c = 65535;
                break;
            case 506081708:
                if (event.equals(QueryMusicEvent.IS_PLAY_SIMILAR)) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 870300454:
                if (event.equals(QueryMusicEvent.GET_SPEED)) {
                    c = 23;
                    break;
                }
                c = 65535;
                break;
            case 1084784151:
                if (event.equals(QueryMusicEvent.GET_MUSIC_PLAYTYPE)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 1356318952:
                if (event.equals(QueryMusicEvent.IS_BT_CONNECTED)) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 1377565924:
                if (event.equals(QueryMusicEvent.GET_INFO_ARTIST)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 1429715250:
                if (event.equals(QueryMusicEvent.GET_INFO_ALBUM)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1447189787:
                if (event.equals(QueryMusicEvent.GET_INFO_TITLE)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 1732874385:
                if (event.equals(QueryMusicEvent.IS_CAN_OPEN_QUALITY_PAGE)) {
                    c = 15;
                    break;
                }
                c = 65535;
                break;
            case 2071296723:
                if (event.equals(QueryMusicEvent.IS_PLAYING)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 2146212582:
                if (event.equals(QueryMusicEvent.IS_HAS_BLUETOOTH_MUSICLIST)) {
                    c = 6;
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
                return this.mTarget.getPlayInfo(event, data);
            case 1:
                return this.mTarget.getInfoTite(event, data);
            case 2:
                return this.mTarget.getInfoArtist(event, data);
            case 3:
                return this.mTarget.getInfoAlbum(event, data);
            case 4:
                return Integer.valueOf(this.mTarget.getPlayType(event, data));
            case 5:
                return Boolean.valueOf(this.mTarget.isPlaying(event, data));
            case 6:
                return Boolean.valueOf(this.mTarget.hasBluetoothMusicList(event, data));
            case 7:
                return Boolean.valueOf(this.mTarget.isHistoryEmpty(event, data));
            case '\b':
                return Boolean.valueOf(this.mTarget.isPlaySimilar(event, data));
            case '\t':
                return Boolean.valueOf(this.mTarget.isCollectListEmpty(event, data));
            case '\n':
                return Boolean.valueOf(this.mTarget.isCanCollected(event, data));
            case 11:
                return Boolean.valueOf(this.mTarget.isBtConnected(event, data));
            case '\f':
                return Boolean.valueOf(this.mTarget.isKuGouAuthed(event, data));
            case '\r':
                return Integer.valueOf(this.mTarget.getUsbState(event, data));
            case 14:
                return Boolean.valueOf(this.mTarget.isMusicAccountLogin(event, data));
            case 15:
                return Boolean.valueOf(this.mTarget.isQualityPageOpend(event, data));
            case 16:
                return Boolean.valueOf(this.mTarget.isXimalayaAccountLogin(event, data));
            case 17:
                return Boolean.valueOf(this.mTarget.isPlayCollect(event, data));
            case 18:
                return Boolean.valueOf(this.mTarget.isPlayHistory(event, data));
            case 19:
                return this.mTarget.getSearchResult(event, data);
            case 20:
                return Boolean.valueOf(this.mTarget.isPlayPodcast(event, data));
            case 21:
                return Boolean.valueOf(this.mTarget.isSupportBtPlay(event, data));
            case 22:
                return Boolean.valueOf(this.mTarget.isSupportSpotifyPlay(event, data));
            case 23:
                return Double.valueOf(this.mTarget.getPlaySpeed(event, data));
            case 24:
                return Boolean.valueOf(this.mTarget.isSupportSpeed(event, data));
            case 25:
                return Boolean.valueOf(this.mTarget.isSupportRadioPlay(event, data));
            case 26:
                return Boolean.valueOf(this.mTarget.isSupportSettime(event, data));
            default:
                return null;
        }
    }

    @Override // com.xiaopeng.speech.annotation.IQueryProcessor
    public String[] getQueryEvents() {
        return new String[]{QueryMusicEvent.GET_INFO_QUERY, QueryMusicEvent.GET_INFO_TITLE, QueryMusicEvent.GET_INFO_ARTIST, QueryMusicEvent.GET_INFO_ALBUM, QueryMusicEvent.GET_MUSIC_PLAYTYPE, QueryMusicEvent.IS_PLAYING, QueryMusicEvent.IS_HAS_BLUETOOTH_MUSICLIST, QueryMusicEvent.IS_HISTORY_EMPTY, QueryMusicEvent.IS_PLAY_SIMILAR, QueryMusicEvent.IS_COLLECT_EMPTY, QueryMusicEvent.IS_CAN_COLLECTED, QueryMusicEvent.IS_BT_CONNECTED, QueryMusicEvent.IS_KUGOU_AUTHED, QueryMusicEvent.MUSIC_GET_USB_STATE, QueryMusicEvent.MUSIC_ACCOUNT_LOGIN, QueryMusicEvent.IS_CAN_OPEN_QUALITY_PAGE, QueryMusicEvent.IS_XIMALAYA_ACCOUNT_LOGIN, QueryMusicEvent.IS_PLAY_COLLECT, QueryMusicEvent.IS_PLAY_HISTORY, QueryMusicEvent.GET_SEARCH_RESULT, QueryMusicEvent.IS_PLAY_PODCAST, QueryMusicEvent.IS_SUPPORT_BT_PLAY, QueryMusicEvent.IS_SUPPORT_SPOTIFY_PLAY, QueryMusicEvent.GET_SPEED, QueryMusicEvent.IS_SUPPORT_SPEED, QueryMusicEvent.IS_SUPPORT_RADIO_PLAY, QueryMusicEvent.IS_SUPPORT_SETTIME};
    }
}
