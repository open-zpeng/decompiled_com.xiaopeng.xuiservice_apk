package com.xiaopeng.xuiservice.capabilities.XpAmbientLightService;

import android.app.ActivityThread;
import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes5.dex */
public class LightEffectManager {
    private static final boolean DBG = true;
    private static final String TAG = "LightEffectManager";
    private ArrayList<LightEffect> mAmbientLightEffectList;
    private ArrayList<String> mAmbientLightEffectTypeList;
    private LightEffect mLightEffect;

    private LightEffectManager(Context context) {
        this.mAmbientLightEffectList = new ArrayList<>();
        this.mAmbientLightEffectTypeList = new ArrayList<>();
        parseLightEffectFromJsons();
    }

    public static LightEffectManager getInstance() {
        return InstanceHolder.sService;
    }

    public List<String> getFilesAllName(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) {
            LogUtil.e(TAG, "空目录");
            return null;
        }
        List<String> s = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            LogUtil.d(TAG, "File path: " + files[i].getAbsolutePath());
            s.add(files[i].getAbsolutePath());
        }
        return s;
    }

    private void parseLightEffectFromJsons() {
        List<String> effectFiles = getFilesAllName("/data/xuiservice/atl");
        this.mAmbientLightEffectList.clear();
        this.mAmbientLightEffectTypeList.clear();
        if (effectFiles != null) {
            for (int i = 0; i < effectFiles.size(); i++) {
                try {
                    this.mLightEffect = (LightEffect) new Gson().fromJson((Reader) new FileReader(effectFiles.get(i)), (Class<Object>) LightEffect.class);
                    this.mAmbientLightEffectList.add(this.mLightEffect);
                    this.mAmbientLightEffectTypeList.add(this.mLightEffect.getEffect_type());
                    LogUtil.d(TAG, "parseLightEffectFromJsons Effect_type: " + this.mLightEffect.getEffect_type());
                } catch (JsonIOException e) {
                    e.printStackTrace();
                } catch (JsonSyntaxException e2) {
                    e2.printStackTrace();
                } catch (FileNotFoundException e3) {
                    e3.printStackTrace();
                }
            }
        }
        List<String> effectFiles2 = getFilesAllName("/system/etc/xuiservice/atl");
        if (effectFiles2 == null) {
            return;
        }
        this.mAmbientLightEffectList.clear();
        this.mAmbientLightEffectTypeList.clear();
        for (int i2 = 0; i2 < effectFiles2.size(); i2++) {
            try {
                this.mLightEffect = (LightEffect) new Gson().fromJson((Reader) new FileReader(effectFiles2.get(i2)), (Class<Object>) LightEffect.class);
                if (!this.mAmbientLightEffectTypeList.contains(this.mLightEffect.getEffect_type())) {
                    this.mAmbientLightEffectList.add(this.mLightEffect);
                    this.mAmbientLightEffectTypeList.add(this.mLightEffect.getEffect_type());
                    LogUtil.d(TAG, "parseLightEffectFromJsons Effect_type: " + this.mLightEffect.getEffect_type());
                }
            } catch (JsonIOException e4) {
                e4.printStackTrace();
            } catch (JsonSyntaxException e5) {
                e5.printStackTrace();
            } catch (FileNotFoundException e6) {
                e6.printStackTrace();
            }
        }
    }

    public ArrayList<LightEffect> getAmbientLightEffectList() {
        return this.mAmbientLightEffectList;
    }

    public ArrayList<String> getAmbientLightEffectTypeList() {
        return this.mAmbientLightEffectTypeList;
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final LightEffectManager sService = new LightEffectManager(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }
}
