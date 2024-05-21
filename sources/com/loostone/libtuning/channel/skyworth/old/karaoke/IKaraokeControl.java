package com.loostone.libtuning.channel.skyworth.old.karaoke;
/* loaded from: classes4.dex */
public interface IKaraokeControl {
    void adjustEcho(String str);

    void adjustMic(String str);

    void adjustMusic(String str);

    void adjustTone(String str);

    void back();

    void closeApp();

    void deleteSong(String str);

    String getPackageName();

    void nextPage();

    void openApp(String str, String str2);

    void openSelectedSong();

    void openTuning();

    void pause();

    void play();

    void play(int i);

    void playNext();

    void prePage();

    void replay();

    void search(String str, String str2);

    void setPackageName(String str);

    void stop();

    void switchScore(String str);

    void switchVocal(String str);

    void topSong(String str);
}
