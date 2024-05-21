package com.xiaopeng.xuiservice.capabilities;

import android.app.ActivityThread;
import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioConfig.AudioConfig;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaopeng.xuimanager.soundresource.ISoundResourceListener;
import com.xiaopeng.xuimanager.soundresource.data.BootSoundResource;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.soundresource.SoundResourceService;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.compress.archivers.zip.UnixStat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class XpWelcomeSoundManager {
    private static final String TAG = "XpWelcomeSoundManager";
    private final String WELCOME_MEDIA_PATH;
    private final String WELCOME_PATH;
    private long fadePadding;
    private boolean hasCompleteListener;
    private boolean isError;
    private AudioConfig mAudioConfig;
    private ChannelControl mChannelControl;
    private Context mContext;
    private int mCurrentPlayPos;
    private MediaPlayer mMusicPlayer;
    ISoundResourceListener mSoundResourceListener;
    private SoundResourceService mSoundResourceService;
    private TimRunnable mTRunnable;
    private TimeHandler mTimeHandler;
    private String mWelcomeAmpConfig;
    private ArrayList<String> mWelcomeConfigList;
    private String mWelcomeSound;
    private ArrayList<String> mWelcomeSoundList;
    private static final boolean SoundResourceActived = SystemProperties.getBoolean("persist.xiaopeng.soundresource.welcomeboot.active", true);
    private static int mStreamType = 3;
    private static Object mSourceListLock = new Object();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class XpWelcomeSoundManagerHolder {
        public static final XpWelcomeSoundManager instance = new XpWelcomeSoundManager();

        private XpWelcomeSoundManagerHolder() {
        }
    }

    public TimeHandler getTimeHandler() {
        if (this.mTimeHandler == null) {
            this.mTimeHandler = new TimeHandler();
        }
        return this.mTimeHandler;
    }

    public TimRunnable getTRunnable() {
        if (this.mTRunnable == null) {
            this.mTRunnable = new TimRunnable();
        }
        return this.mTRunnable;
    }

    public static XpWelcomeSoundManager getInstance() {
        return XpWelcomeSoundManagerHolder.instance;
    }

    private XpWelcomeSoundManager() {
        this.fadePadding = 100L;
        this.WELCOME_PATH = "/system/etc/xuiservice/welcome";
        this.WELCOME_MEDIA_PATH = "/system/media/audio/xiaopeng/cdu/boot";
        this.mWelcomeSound = null;
        this.mWelcomeAmpConfig = null;
        this.hasCompleteListener = false;
        this.mCurrentPlayPos = 0;
        this.isError = false;
        this.mSoundResourceListener = new ISoundResourceListener.Stub() { // from class: com.xiaopeng.xuiservice.capabilities.XpWelcomeSoundManager.1
            public void onResourceEvent(int resId, int event) throws RemoteException {
                LogUtil.i(XpWelcomeSoundManager.TAG, "onResourceEvent " + resId + " " + event);
            }
        };
        this.mContext = ActivityThread.currentActivityThread().getApplication();
        this.mAudioConfig = new AudioConfig(this.mContext);
        this.mMusicPlayer = new MediaPlayer();
        this.mSoundResourceService = SoundResourceService.getInstance();
        try {
            this.mSoundResourceService.registerListener(this.mSoundResourceListener);
        } catch (Exception e) {
        }
        loadAllWelcomeSnd();
    }

    private void loadAllWelcomeSnd() {
        synchronized (mSourceListLock) {
            if (this.mWelcomeConfigList == null) {
                this.mWelcomeConfigList = loadFileList("/system/etc/xuiservice/welcome", "json");
            }
            if (this.mWelcomeSoundList == null) {
                this.mWelcomeSoundList = loadFileList("/system/media/audio/xiaopeng/cdu/boot", "wav");
                if (this.mWelcomeSoundList == null) {
                    LogUtil.w(TAG, "loadAllWelcomeSnd,load from path null:/system/media/audio/xiaopeng/cdu/boot");
                    return;
                }
                this.mWelcomeSoundList.addAll(loadFileList("/system/media/audio/xiaopeng/cdu/boot", "mp3"));
            }
            if (this.mWelcomeConfigList != null && this.mWelcomeConfigList.size() > 0) {
                this.mWelcomeAmpConfig = this.mWelcomeConfigList.get(0);
            }
            if (this.mWelcomeSoundList != null && this.mWelcomeSoundList.size() > 0) {
                this.mWelcomeSound = this.mWelcomeSoundList.get(0);
            }
        }
    }

    private ArrayList<String> loadFileList(String path, String prefix) {
        File[] files;
        LogUtil.i(TAG, "loadFileList " + path + " " + prefix);
        ArrayList<String> s = new ArrayList<>();
        try {
            File file = new File(path);
            files = file.listFiles();
        } catch (Exception e) {
            LogUtil.e(TAG, "loadFileList ERROR:" + e);
        }
        if (files != null && files.length != 0) {
            List fileList = Arrays.asList(files);
            Collections.sort(fileList, new Comparator<File>() { // from class: com.xiaopeng.xuiservice.capabilities.XpWelcomeSoundManager.2
                @Override // java.util.Comparator
                public int compare(File o1, File o2) {
                    if (o1.isDirectory() && o2.isFile()) {
                        return -1;
                    }
                    if (o1.isFile() && o2.isDirectory()) {
                        return 1;
                    }
                    return o1.getName().compareTo(o2.getName());
                }
            });
            for (int i = 0; i < fileList.size(); i++) {
                File file_tmp = (File) fileList.get(i);
                LogUtil.i(TAG, "File path" + file_tmp.getAbsolutePath());
                String fileName = file_tmp.getName();
                String filePrefix = fileName.substring(fileName.lastIndexOf(".") + 1);
                LogUtil.i(TAG, "fileName:" + fileName + " filePrefix:" + filePrefix + " prefix:" + prefix);
                if (filePrefix.equals(prefix)) {
                    s.add(file_tmp.getAbsolutePath());
                }
            }
            return s;
        }
        LogUtil.e(TAG, "空目录");
        return null;
    }

    private String parseXml(String xmlPath) {
        LogUtil.i(TAG, "parseXml " + xmlPath);
        InputStream is = null;
        try {
            if (xmlPath != null) {
                File xmlFile = new File(xmlPath);
                is = new FileInputStream(xmlFile);
            } else {
                AssetManager am = this.mContext.getAssets();
                is = am.open("audioconfig.xml");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doParseXml(is);
    }

    private String doParseXml(InputStream is) {
        String json = "";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while (true) {
                int len = is.read(buffer);
                if (len == -1) {
                    break;
                }
                baos.write(buffer, 0, len);
            }
            json = baos.toString();
            baos.flush();
            is.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(json)) {
            setAudioConfigs(json);
        }
        return json;
    }

    private String parseJson(String jsonPath) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(new FileInputStream(jsonPath));
            BufferedReader br = new BufferedReader(reader);
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                stringBuilder.append(line);
            }
            br.close();
            reader.close();
            String json = stringBuilder.toString();
            if (!TextUtils.isEmpty(json)) {
                setAudioConfigs(json);
            }
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void setAudioConfigs(String json) {
        if (this.mChannelControl == null) {
            this.mChannelControl = new ChannelControl();
        }
        try {
            JSONObject jsonObject = new JSONObject(json);
            boolean isLoop = jsonObject.getBoolean("isLoop");
            JSONArray audiosArray = jsonObject.getJSONArray("audioConfig");
            String jsonString = audiosArray.toString();
            Gson gson = new Gson();
            List<ChannelBean> channelBrans = (List) gson.fromJson(jsonString, new TypeToken<List<ChannelBean>>() { // from class: com.xiaopeng.xuiservice.capabilities.XpWelcomeSoundManager.3
            }.getType());
            this.mChannelControl.setLoop(isLoop);
            this.mChannelControl.setAudioConfig(channelBrans);
        } catch (JSONException e) {
        }
    }

    public void setErrorListener(MediaPlayer.OnErrorListener listener) {
        MediaPlayer mediaPlayer = this.mMusicPlayer;
        if (mediaPlayer != null && listener != null) {
            mediaPlayer.setOnErrorListener(listener);
        }
    }

    public void setCompleteListener(MediaPlayer.OnCompletionListener listener) {
        MediaPlayer mediaPlayer = this.mMusicPlayer;
        if (mediaPlayer != null && listener != null) {
            this.hasCompleteListener = true;
            mediaPlayer.setOnCompletionListener(listener);
        }
    }

    public void setStreamType(int streamType) {
        mStreamType = streamType;
        MediaPlayer mediaPlayer = this.mMusicPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.setAudioStreamType(streamType);
        }
    }

    public boolean isWelcomeEnable() {
        File xmlFile = new File(this.mWelcomeAmpConfig);
        File mediaFile = new File(this.mWelcomeSound);
        if (SoundResourceActived) {
            return mediaFile.exists();
        }
        return xmlFile.exists() && mediaFile.exists();
    }

    private boolean selectSndEffect(int sndId) {
        if (sndId == -1) {
            return false;
        }
        synchronized (mSourceListLock) {
            if (SoundResourceActived) {
                if (this.mSoundResourceService == null) {
                    LogUtil.e(TAG, "selectSndEffect error mSoundResourceService == null");
                    return false;
                }
                try {
                    BootSoundResource mBootSoundResource = this.mSoundResourceService.getActiveBootSoundResource();
                    if (mBootSoundResource == null) {
                        LogUtil.e(TAG, "selectSndEffect error mBootSoundResource == null");
                        return selectDefaultSndEffect(sndId);
                    }
                    String[] resPath = mBootSoundResource.getResPath();
                    if (resPath != null) {
                        LogUtil.d(TAG, "selectSndEffect  ID:" + sndId + " resPath.length:" + resPath.length + " " + resPath[0]);
                        this.mWelcomeSound = resPath[0];
                        StringBuilder sb = new StringBuilder();
                        sb.append("selectSndEffect  ID:");
                        sb.append(sndId);
                        sb.append(" mWelcomeSound:");
                        sb.append(this.mWelcomeSound);
                        LogUtil.d(TAG, sb.toString());
                        return true;
                    }
                    LogUtil.e(TAG, "selectSndEffect error resPath == null");
                    return selectDefaultSndEffect(sndId);
                } catch (Exception e) {
                    LogUtil.e(TAG, "selectSndEffect ERROR:" + e);
                }
            } else {
                selectDefaultSndEffect(sndId);
            }
            LogUtil.d(TAG, "selectSndEffect  ID:" + sndId + " mWelcomeSound:" + this.mWelcomeSound);
            return true;
        }
    }

    private boolean selectDefaultSndEffect(int sndId) {
        if (sndId == -1) {
            return false;
        }
        ArrayList<String> arrayList = this.mWelcomeConfigList;
        if (arrayList != null) {
            int configNum = arrayList.size();
            if (sndId >= 0 && sndId < configNum) {
                this.mWelcomeAmpConfig = this.mWelcomeConfigList.get(sndId);
            }
        }
        ArrayList<String> arrayList2 = this.mWelcomeSoundList;
        if (arrayList2 != null) {
            int sndNum = arrayList2.size();
            if (sndId >= 0 && sndId < sndNum) {
                this.mWelcomeSound = this.mWelcomeSoundList.get(sndId);
            }
        }
        LogUtil.d(TAG, "selectDefaultSndEffect  ID:" + sndId + " mWelcomeAmpConfig:" + this.mWelcomeAmpConfig + " mWelcomeSound:" + this.mWelcomeSound);
        return true;
    }

    private String getFileName(String fileAbsolutePath) {
        if (fileAbsolutePath == null) {
            return null;
        }
        LogUtil.d(TAG, "getFileName fileAbsolutePath:" + fileAbsolutePath);
        String name = fileAbsolutePath.substring(fileAbsolutePath.lastIndexOf("/") + 1, fileAbsolutePath.lastIndexOf("."));
        LogUtil.d(TAG, "getFileName name:" + name);
        return name;
    }

    public boolean checkSoundCanPlay(int sndId) {
        if (selectSndEffect(sndId)) {
            LogUtil.d(TAG, "checkSoundCanPlay  ID:" + sndId + " mWelcomeAmpConfig:" + this.mWelcomeAmpConfig + " mWelcomeSound:" + this.mWelcomeSound);
            return getFileName(this.mWelcomeAmpConfig) != null && getFileName(this.mWelcomeAmpConfig).equals(getFileName(this.mWelcomeSound));
        }
        return false;
    }

    public void perpareMediaPlayer(String mediafile) {
        try {
            if (this.mMusicPlayer == null) {
                this.mMusicPlayer = new MediaPlayer();
            }
            if (this.mMusicPlayer != null) {
                this.mMusicPlayer.reset();
                this.mMusicPlayer.setDataSource(mediafile);
                AudioAttributes mAudioAttributes = new AudioAttributes.Builder().setLegacyStreamType(mStreamType).setFlags(3145728).build();
                this.mMusicPlayer.setAudioAttributes(mAudioAttributes);
                LogUtil.d(TAG, "perpareMediaPlayer  flag:" + mAudioAttributes.getAllFlags());
                if (!this.hasCompleteListener) {
                    this.mMusicPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.xiaopeng.xuiservice.capabilities.XpWelcomeSoundManager.4
                        @Override // android.media.MediaPlayer.OnCompletionListener
                        public void onCompletion(MediaPlayer mp) {
                            XpWelcomeSoundManager.this.stop();
                        }
                    });
                }
                this.mMusicPlayer.prepareAsync();
                this.mMusicPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.xiaopeng.xuiservice.capabilities.-$$Lambda$XpWelcomeSoundManager$6o7WZ8SsNyVJDBM6OmJ4XLv_B7Q
                    @Override // android.media.MediaPlayer.OnPreparedListener
                    public final void onPrepared(MediaPlayer mediaPlayer) {
                        XpWelcomeSoundManager.this.lambda$perpareMediaPlayer$0$XpWelcomeSoundManager(mediaPlayer);
                    }
                });
            }
        } catch (Exception e) {
        }
    }

    public /* synthetic */ void lambda$perpareMediaPlayer$0$XpWelcomeSoundManager(MediaPlayer mp) {
        doRealPlay();
    }

    public void play(int sndId) {
        LogUtil.d(TAG, "play " + sndId);
        if (!selectSndEffect(sndId)) {
            return;
        }
        LogUtil.d(TAG, "isHighConfig()" + isHighConfig());
        if (!SoundResourceActived && isHighConfig()) {
            parseJson(this.mWelcomeAmpConfig);
        }
        LogUtil.d(TAG, "mWelcomeSound:" + this.mWelcomeSound + " mWelcomeAmpConfig:" + this.mWelcomeAmpConfig);
        perpareMediaPlayer(this.mWelcomeSound);
    }

    public void play(String musicFile, String jsonFile) {
        LogUtil.d(TAG, "play " + musicFile);
        if (musicFile == null) {
            return;
        }
        LogUtil.d(TAG, "isHighConfig()" + isHighConfig());
        if (!SoundResourceActived && isHighConfig() && jsonFile != null) {
            parseJson(jsonFile);
        }
        LogUtil.d(TAG, "musicFile:" + musicFile + " jsonFile:" + jsonFile);
        perpareMediaPlayer(musicFile);
    }

    public void stop() {
        MediaPlayer mediaPlayer = this.mMusicPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        if (this.mChannelControl != null) {
            forceChangeToAmpChannel(UnixStat.PERM_MASK, UnixStat.PERM_MASK, 100, true);
        }
        ChannelControl channelControl = this.mChannelControl;
        if (channelControl != null) {
            channelControl.stop();
        }
        this.mCurrentPlayPos = 0;
    }

    private void doRealPlay() {
        LogUtil.d(TAG, "doRealPlay()");
        ChannelControl channelControl = this.mChannelControl;
        if (channelControl != null) {
            this.mCurrentPlayPos = 0;
            this.isError = false;
            channelControl.start();
            forceChangeToAmpChannel();
        }
        MediaPlayer mediaPlayer = this.mMusicPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void forceChangeToAmpChannel() {
        List<ChannelBean> channelBeans = this.mChannelControl.getAudioConfig();
        if (channelBeans != null && channelBeans.size() > 0 && this.mCurrentPlayPos < channelBeans.size()) {
            if (this.mChannelControl.needStop()) {
                return;
            }
            ChannelBean bean = channelBeans.get(this.mCurrentPlayPos);
            LogUtil.v(TAG, "-------" + this.mCurrentPlayPos + "-------");
            this.mCurrentPlayPos = this.mCurrentPlayPos + 1;
            startRun(bean);
            return;
        }
        LogUtil.d(TAG, "do end action");
        forceChangeToAmpChannel(UnixStat.PERM_MASK, UnixStat.PERM_MASK, 100, true);
    }

    private synchronized void forceChangeToAmpChannel(int channelbits, int activebits, int vol, boolean restore) {
        if (this.mAudioConfig != null) {
            if (this.isError) {
                this.mAudioConfig.forceChangeToAmpChannel((int) UnixStat.PERM_MASK, (int) UnixStat.PERM_MASK, 100, true);
            } else {
                this.mAudioConfig.forceChangeToAmpChannel(channelbits, activebits, vol, restore);
            }
        }
    }

    private void startRun(ChannelBean bean) {
        if (this.mAudioConfig == null) {
            this.mAudioConfig = new AudioConfig(this.mContext);
        }
        ArrayList<Integer> playAudios = bean.getPlayAudios();
        LogUtil.i(TAG, "startPlay playAudios = " + playAudios.toString());
        int channelBits = getChannelBits(playAudios);
        long playTime = bean.getPlayTime();
        forceChangeToAmpChannel(channelBits, channelBits, 100, false);
        LogUtil.i(TAG, "startPlay playTime=" + playTime + "|channelBits=" + channelBits);
        postDelayedForAction(playTime, bean);
    }

    private void postDelayedForAction(long delayMillis, ChannelBean bean) {
        long now = SystemClock.uptimeMillis();
        long next = now + delayMillis;
        getTimeHandler().postAtTime(getTRunnable(), next, bean);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static final class TimeHandler extends Handler {
        private TimeHandler() {
        }

        public void postAtTime(TimRunnable r, long uptimeMillis, ChannelBean bean) {
            r.setChannelBean(bean);
            postAtTime(r, uptimeMillis);
        }
    }

    /* loaded from: classes5.dex */
    public final class TimRunnable implements Runnable {
        private ChannelBean channelBean;

        public TimRunnable() {
        }

        public void setChannelBean(ChannelBean bean) {
            this.channelBean = bean;
        }

        @Override // java.lang.Runnable
        public void run() {
            LogUtil.v(XpWelcomeSoundManager.TAG, "-------end-------");
            XuiWorkHandler.getInstance().postDelayed(new Runnable() { // from class: com.xiaopeng.xuiservice.capabilities.XpWelcomeSoundManager.TimRunnable.1
                @Override // java.lang.Runnable
                public void run() {
                    XpWelcomeSoundManager.this.fadeOutAction(TimRunnable.this.channelBean);
                }
            }, 0L);
            XpWelcomeSoundManager.this.forceChangeToAmpChannel();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fadeOutAction(ChannelBean channelBean) {
        try {
            if (channelBean.isFade()) {
                channelChangeFade(channelBean);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void channelChangeFade(ChannelBean bean) throws InterruptedException {
        List<Integer> filterAudios = filterAudios(bean);
        if (filterAudios.size() == 0) {
            return;
        }
        int fadeBits = getChannelBits(filterAudios);
        int channelBits = getChannelBits(bean.getPlayAudios());
        ArrayList<Integer> fadeAudios = bean.getFadeAudios();
        LogUtil.v(TAG, "channelChangeFade fadeAudios = " + fadeAudios.toString());
        LogUtil.v(TAG, "channelChangeFade filterAudios = " + filterAudios.toString());
        int activeBits = getChannelBits(fadeAudios);
        long fadeTime = bean.getFadeTime();
        long j = fadeTime / this.fadePadding;
        for (int i = 1; i < 1 + j; i++) {
            int volume = (int) (100 - ((i * 100) / j));
            forceChangeToAmpChannel(fadeBits, channelBits | activeBits, volume, false);
            Thread.sleep(this.fadePadding - 30);
        }
    }

    private List<Integer> filterAudios(ChannelBean bean) {
        List filterList = new ArrayList();
        List<Integer> playAudios = bean.getPlayAudios();
        List<Integer> fadeAudios = bean.getFadeAudios();
        for (Integer one : playAudios) {
            if (!fadeAudios.contains(one)) {
                filterList.add(one);
            }
        }
        return filterList;
    }

    private int getChannelBits(List<Integer> audios) {
        int channelBits = 0;
        for (Integer audioId : audios) {
            int exponent = audioId.intValue() - 1;
            if (exponent >= 0) {
                int bits = (int) Math.pow(2.0d, exponent);
                channelBits += bits;
            }
        }
        return channelBits;
    }

    public void onCompletion() {
        LogUtil.v(TAG, "onCompletion");
        stop();
    }

    public void onError() {
        LogUtil.d(TAG, "onError()");
        this.isError = true;
        getTimeHandler().removeCallbacks(getTRunnable());
        AudioConfig audioConfig = this.mAudioConfig;
        if (audioConfig != null) {
            audioConfig.forceChangeToAmpChannel((int) UnixStat.PERM_MASK, (int) UnixStat.PERM_MASK, 100, true);
        }
    }

    public boolean isPlaying() {
        return this.mMusicPlayer.isPlaying();
    }

    public void pause() {
        this.mMusicPlayer.pause();
    }

    public void release() {
        if (this.mChannelControl != null) {
            forceChangeToAmpChannel(UnixStat.PERM_MASK, UnixStat.PERM_MASK, 100, true);
            this.mAudioConfig = null;
        }
        MediaPlayer mediaPlayer = this.mMusicPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            this.mMusicPlayer = null;
        }
        this.mCurrentPlayPos = 0;
    }

    public int getDuration() {
        return this.mMusicPlayer.getDuration();
    }

    private boolean isHighConfig() {
        return AudioConfig.isHighConfig();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class ChannelBean {
        private ArrayList<Integer> fadeAudios;
        private long fadeTime;
        private boolean isFade;
        private ArrayList<Integer> playAudios;
        private long playTime;

        private ChannelBean() {
        }

        public ArrayList<Integer> getPlayAudios() {
            return this.playAudios;
        }

        public void setPlayAudios(ArrayList<Integer> playAudios) {
            this.playAudios = playAudios;
        }

        public long getPlayTime() {
            return this.playTime;
        }

        public void setPlayTime(long playTime) {
            this.playTime = playTime;
        }

        public boolean isFade() {
            return this.isFade;
        }

        public void setFade(boolean fade) {
            this.isFade = fade;
        }

        public ArrayList<Integer> getFadeAudios() {
            return this.fadeAudios;
        }

        public void setFadeAudios(ArrayList<Integer> fadeAudios) {
            this.fadeAudios = fadeAudios;
        }

        public long getFadeTime() {
            return this.fadeTime;
        }

        public void setFadeTime(long fadeTime) {
            this.fadeTime = fadeTime;
        }

        public String toString() {
            return "ChannelBean{playAudios=" + this.playAudios + ", playTime=" + this.playTime + ", isFade=" + this.isFade + ", fadeAudios=" + this.fadeAudios + ", fadeTime=" + this.fadeTime + '}';
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class ChannelControl {
        private List<ChannelBean> audioConfig;
        private boolean isLoop;
        private boolean needStop;

        private ChannelControl() {
            this.needStop = false;
        }

        public boolean isLoop() {
            return this.isLoop;
        }

        public void setLoop(boolean loop) {
            this.isLoop = loop;
        }

        public boolean needStop() {
            return this.needStop;
        }

        public void stop() {
            this.needStop = true;
        }

        public void start() {
            this.needStop = false;
        }

        public List<ChannelBean> getAudioConfig() {
            return this.audioConfig;
        }

        public void setAudioConfig(List<ChannelBean> audioConfig) {
            this.audioConfig = audioConfig;
        }
    }
}
