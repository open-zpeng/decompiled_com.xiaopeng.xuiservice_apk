package com.xiaopeng.xuiservice.operation.resource;

import android.app.ActivityThread;
import android.provider.Settings;
import android.text.TextUtils;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.operation.db.ResourceInfo;
import com.xiaopeng.xuiservice.utils.FileUtils;
import java.io.File;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class PresetFileProcessor {
    public static final String TAG = "PresetFileProcessor";

    public void start() {
        createOperationResourceDir();
        processDanceFile();
        processSpeechEngine();
        processTTSEngine();
    }

    private void createOperationResourceDir() {
        File file = new File("/data/operation/resource/");
        if (!file.exists()) {
            boolean result = file.mkdirs();
            FileUtils.setOperationResourcePermission(file.getParentFile().getPath());
            LogUtil.d(TAG, "createOperationResourceDir success:" + result);
        }
    }

    private void processDanceFile() {
        processDanceFileInner("/system/etc/xuiservice/dance/", "/data/xuiservice/rsc/llu/");
    }

    private void processSpeechEngine() {
        processZipFile("/system/speech/", "/sdcard/speech/", "key_unzipped_speech");
    }

    private void processTTSEngine() {
        processZipFile("/system/tts/", "/mnt/tts/", "key_unzipped_tts");
    }

    private void processDanceFileInner(String srcPath, String dstPath) {
        List<String> danceIds = findPresetDanceIdList();
        deleteInvalidDancePreset(danceIds);
        if (!danceIds.isEmpty()) {
            for (String id : danceIds) {
                String dst_file = dstPath + id + "/";
                savePresetResourceInfo(id, 1010101, id, dstPath, dst_file);
                String video_src_file = srcPath + id + ".mp4";
                String video_dst_file = dstPath + id + "/" + id + ".mp4";
                FileUtils.copyFile(video_src_file, video_dst_file);
                String png_src_file = srcPath + id + ".png";
                String png_dst_file = dstPath + id + "/" + id + ".png";
                FileUtils.copyFile(png_src_file, png_dst_file);
            }
        }
    }

    private void deleteInvalidDancePreset(List<String> presetDanceIds) {
        List<ResourceInfo> infoList = ResourceManager.getInstance().getLocalResourceByType(1010101);
        if (infoList != null && !infoList.isEmpty()) {
            for (ResourceInfo info : infoList) {
                if (isPresetFileInvalid(presetDanceIds, info.resourceId)) {
                    LogUtil.i(TAG, "deleteInvalidDancePreset info:" + info.toString());
                    ResourceManager.getInstance().deleteInfoFromDB(info);
                    FileUtils.deleteFile(info.targetPath + info.resourceId);
                }
            }
        }
    }

    private boolean isPresetFileInvalid(List<String> presetDanceIds, String id) {
        if (presetDanceIds == null || presetDanceIds.isEmpty()) {
            return true;
        }
        return true ^ presetDanceIds.contains(id);
    }

    private List<String> findPresetDanceIdList() {
        String presetDanceInfo = FileUtils.readFromFile("/system/etc/xuiservice/dance/LocalDance_Info.json");
        List<String> danceIds = new ArrayList<>();
        if (!TextUtils.isEmpty(presetDanceInfo)) {
            try {
                JSONArray jsonArray = new JSONArray(presetDanceInfo);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String id = object.optString("id", "");
                    if (!TextUtils.isEmpty(id)) {
                        danceIds.add(id);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return danceIds;
    }

    private void processZipFile(String rscPath, String targetPath, String keySettingProvider) {
        LogUtil.d(TAG, "start processZipFile rscPath:" + rscPath);
        File ttsFolder = new File(rscPath);
        File[] files = ttsFolder.listFiles();
        if (files == null || files.length <= 0) {
            LogUtil.w(TAG, "unzipFile empty folder");
            return;
        }
        File zipFile = files[0];
        String zipFileName = zipFile.getName();
        String zipFileBaseName = getFileBaseName(zipFileName);
        String savedUnZippedName = getUnZippedName(keySettingProvider);
        LogUtil.i(TAG, "zipFileName:" + zipFileBaseName + " &savedUnZippedName:" + savedUnZippedName);
        if (!TextUtils.equals(zipFileBaseName, savedUnZippedName)) {
            String targetFolderPath = targetPath + zipFileBaseName + "/";
            try {
                LogUtil.d(TAG, "FileUtils start unZip");
                FileUtils.unZip(zipFile, targetFolderPath);
                setZipFilePermission(targetFolderPath);
                saveUnZippedName(keySettingProvider, zipFileBaseName);
            } catch (Exception e) {
                LogUtil.d(TAG, "unZip tts zip exception");
                e.printStackTrace();
            }
        }
    }

    private String getFileBaseName(String name) {
        int dot = name.lastIndexOf(46);
        String base = dot == -1 ? name : name.substring(0, dot);
        LogUtil.i(TAG, "fileBaseName:" + base);
        return base;
    }

    private String getUnZippedName(String keySettingProvider) {
        return Settings.System.getString(ActivityThread.currentActivityThread().getApplication().getContentResolver(), keySettingProvider);
    }

    private void saveUnZippedName(String keySettingProvider, String value) {
        Settings.System.putString(ActivityThread.currentActivityThread().getApplication().getContentResolver(), keySettingProvider, value);
    }

    private void setZipFilePermission(String destDirPath) {
        LogUtil.d(TAG, "setZipFilePermission path:" + destDirPath);
        Set<PosixFilePermission> perms = new HashSet<>();
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);
        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_WRITE);
        perms.add(PosixFilePermission.GROUP_EXECUTE);
        perms.add(PosixFilePermission.OTHERS_READ);
        perms.add(PosixFilePermission.OTHERS_EXECUTE);
        File file = new File(destDirPath);
        FileUtils.setFilePermission(file, perms, perms);
        LogUtil.d(TAG, "setTTSFilePermission end");
    }

    private void savePresetResourceInfo(String id, int type, String sourceName, String targetPath, String fileFullPath) {
        String presetDanceInfo = FileUtils.readFromFile("/system/etc/xuiservice/dance/LocalDance_Info.json");
        ResourceInfo resourceInfo = new ResourceInfo();
        resourceInfo.resourceId = id;
        resourceInfo.resourceType = type;
        resourceInfo.resourceName = sourceName;
        resourceInfo.targetPath = targetPath;
        resourceInfo.resourceFrom = "preset_resource";
        resourceInfo.extraInfo = presetDanceInfo;
        ResourceManager.getInstance().saveFileInfoToDB(resourceInfo);
    }
}
