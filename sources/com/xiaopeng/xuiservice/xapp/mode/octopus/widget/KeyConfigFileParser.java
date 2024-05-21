package com.xiaopeng.xuiservice.xapp.mode.octopus.widget;

import android.graphics.Point;
import android.text.TextUtils;
import android.util.SparseArray;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class KeyConfigFileParser {
    private static final String TAG = "KeyConfigFileParser";
    private static final String XP_KEYMAPS_DIR = "/data/xuiservice/keyMap/";
    private static final String XP_KEYMAPS_SUFFIX = ".key";
    private static volatile KeyConfigFileParser mInstance;
    private HashMap<String, SparseArray<Point>> mStringSparseArrayHashMap = new HashMap<>();

    private KeyConfigFileParser() {
    }

    public static KeyConfigFileParser getInstance() {
        if (mInstance == null) {
            synchronized (KeyConfigFileParser.class) {
                if (mInstance == null) {
                    mInstance = new KeyConfigFileParser();
                }
            }
        }
        return mInstance;
    }

    private void loaderKeyMapFromFile() {
        String[] list;
        LogUtil.d(TAG, "loaderKeyMapFromFile");
        File dir = new File(XP_KEYMAPS_DIR);
        if (dir.isDirectory() && (list = dir.list()) != null && list.length > 0) {
            for (String item : list) {
                String packageName = getKeyMapFilePackageName(item);
                loaderSingleKeyMapFromFile(packageName);
            }
        }
    }

    private String getKeyMapFilePackageName(String fileName) {
        int LENGTH = XP_KEYMAPS_SUFFIX.length();
        if (!TextUtils.isEmpty(fileName) && fileName.length() > LENGTH) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        }
        return null;
    }

    private void loaderSingleKeyMapFromFile(String pkgName) {
        LogUtil.d(TAG, "loaderSingleKeyMapFromFile");
        String file = XP_KEYMAPS_DIR + pkgName + XP_KEYMAPS_SUFFIX;
        String jsonContent = getContentFromFile(file);
        KeyTouchConfig keyTouchConfig = parseKeyTouchConfig(jsonContent);
        LogUtil.d(TAG, "keyTouchConfig: " + keyTouchConfig.toString());
        this.mStringSparseArrayHashMap.put(keyTouchConfig.pkgName, keyTouchConfig.mPointSparseArray);
    }

    private Point getKeyCodePoint(int keycode) {
        SparseArray<Point> keySparseArray = this.mStringSparseArrayHashMap.get("com.mark.apidemo");
        if (keySparseArray != null) {
            return keySparseArray.get(keycode);
        }
        return null;
    }

    private void addPointer(JSONArray jsonArray, int keycode, Point point) {
        JSONObject keyJsonObject = new JSONObject();
        try {
            keyJsonObject.put("keycode", keycode);
            keyJsonObject.put("pointX", point.x);
            keyJsonObject.put("pointY", point.y);
            jsonArray.put(keyJsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private KeyTouchConfig parseKeyTouchConfig(String jsonContent) {
        KeyTouchConfig keyTouchConfig = new KeyTouchConfig();
        if (TextUtils.isEmpty(jsonContent)) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(jsonContent);
            keyTouchConfig.pkgName = jsonObject.optString("pkgName");
            JSONArray jsonArray = jsonObject.getJSONArray("keyMap");
            SparseArray<Point> sparseArray = new SparseArray<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject keyValue = jsonArray.getJSONObject(i);
                int keycode = keyValue.optInt("keycode");
                int x = keyValue.optInt("x");
                int y = keyValue.optInt("y");
                sparseArray.put(keycode, new Point(x, y));
            }
            keyTouchConfig.mPointSparseArray = sparseArray;
            return keyTouchConfig;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getContentFromFile(String file) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
            BufferedReader br = new BufferedReader(isr);
            while (true) {
                String line = br.readLine();
                if (line != null) {
                    stringBuilder.append(line);
                } else {
                    br.close();
                    isr.close();
                    return stringBuilder.toString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class KeyTouchConfig {
        private SparseArray<Point> mPointSparseArray;
        private boolean needJoystickMotionEvent;
        private String pkgName;

        private KeyTouchConfig() {
        }

        public String toString() {
            return "KeyTouchConfig{pkgName='" + this.pkgName + "', mPointSparseArray=" + this.mPointSparseArray.toString() + '}';
        }
    }
}
