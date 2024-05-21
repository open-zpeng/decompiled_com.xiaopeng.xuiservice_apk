package com.loostone.libtuning.channel.skyworth.old.ai;
/* loaded from: classes4.dex */
public interface IAiControl {
    public static final String ACTION = "com.loostone.karaoke.voice";
    public static final String CMD_ENTER_KARAOKE_APP = "EnterKaraokeApp";
    public static final String CMD_ENTER_KARAOKE_PLAYER = "EnterKaraokePlayer";
    public static final String CMD_EXIT_KARAOKE_APP = "ExitKaraokeApp";
    public static final String CMD_EXIT_KARAOKE_PLAYER = "ExitKaraokePlayer";
    public static final String CMD_MIC_CHANGED = "MicChanged";
    public static final String CMD_MIC_EXIST = "MicExist";
    public static final String CMD_MIC_NOT_EXIST = "MicNotExist";
    public static final String CMD_VOICE_KEY_DOWN = "VoiceKeyDown";
    public static final String CMD_VOICE_KEY_UP = "VoiceKeyUp";

    void enterKaraokeApp(String str);

    void enterKaraokePlayer();

    void exitKaraokeApp();

    void exitKaraokePlayer();

    String getPackageName();

    boolean isEnterApp();

    boolean isEnterPlayer();

    void micChanged(int i);

    void onMicExist(int i);

    void onVoiceKey(int i);
}
