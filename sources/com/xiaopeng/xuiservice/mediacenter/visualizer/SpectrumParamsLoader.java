package com.xiaopeng.xuiservice.mediacenter.visualizer;

import android.text.TextUtils;
import android.util.SparseArray;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes5.dex */
public class SpectrumParamsLoader {
    private static final String KEY_FREQUENCY = "FrequencyValue";
    private static final String KEY_MEDIA_TYPE = "MediaType";
    private static final String KEY_MEDIA_TYPE_NAME = "MediaTypeName";
    private static final String KEY_RATIO_DELTA = "ratioDelta";
    private static final String KEY_WEIGHT = "WeightValue";
    private static final String POLICY_FILE_DATA = "/data/xuiservice/xui_spectrum_params.json";
    private static final String POLICY_FILE_SYSTEM = "/system/etc/xui_spectrum_params.json";
    private static final String TAG = "SpectrumParamsLoader";
    private static final float[] FREQUENCY_DEFAULT = {25.0f, 31.5f, 40.0f, 50.0f, 63.0f, 80.0f, 100.0f, 125.0f, 160.0f, 200.0f, 250.0f, 315.0f, 400.0f, 500.0f, 630.0f, 800.0f, 1000.0f, 1250.0f, 1600.0f, 2000.0f, 2500.0f, 3150.0f, 4000.0f, 5000.0f, 6300.0f, 8000.0f, 10000.0f, 12500.0f, 16000.0f, 20000.0f};
    private static final double[] K_DEFAULT = {3.38844156139202E-5d, 1.14815362149688E-4d, 3.46736850452532E-4d, 9.54992586021436E-4d, 0.00239883291901949d, 0.00562341325190349d, 0.0123026877081238d, 0.0245470891568503d, 0.0457088189614875d, 0.0812830516164099d, 0.138038426460288d, 0.218776162394955d, 0.331131121482591d, 0.478630092322638d, 0.645654229034656d, 0.831763771102671d, 1.0d, 1.14815362149688d, 1.25892541179417d, 1.31825673855641d, 1.34896288259165d, 1.31825673855641d, 1.25892541179417d, 1.12201845430196d, 0.977237220955811d, 0.776247116628692d, 0.562341325190349d, 0.371535229097173d, 0.218776162394955d, 0.117489755493953d};
    private Map<Integer, double[]> mWeightValueMap = new ConcurrentHashMap();
    private Map<Integer, float[]> mFrequencyValueMap = new ConcurrentHashMap();
    private SparseArray<Float> mRatioDeltaArray = new SparseArray<>();
    private List<SpectrumParams> mSpectrumParams = new ArrayList();

    public void load() {
        File dataFile = new File(POLICY_FILE_DATA);
        File systemFile = new File(POLICY_FILE_SYSTEM);
        String content = getFileContent(dataFile);
        if (TextUtils.isEmpty(content)) {
            content = getFileContent(systemFile);
            if (TextUtils.isEmpty(content)) {
                return;
            }
        }
        try {
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                SpectrumParams spectrumParams = new SpectrumParams();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                spectrumParams.setMediaTypeName(jsonObject.optString(KEY_MEDIA_TYPE_NAME, ""));
                double[] weightValue = parseWeightValue(jsonObject.optString(KEY_WEIGHT, ""));
                float[] frequencyValue = parseFrequencyValue(jsonObject.optString(KEY_FREQUENCY, ""));
                float ratioDelta = (float) jsonObject.optDouble(KEY_RATIO_DELTA, 0.25d);
                List<Integer> typeList = parseMediaType(jsonObject.optString(KEY_MEDIA_TYPE, ""));
                if (weightValue != null) {
                    spectrumParams.setWeightValues(weightValue);
                }
                if (frequencyValue != null) {
                    spectrumParams.setFrequencyValues(frequencyValue);
                }
                if (typeList != null) {
                    spectrumParams.setMediaType(typeList);
                }
                spectrumParams.setRatioDelta(ratioDelta);
                this.mSpectrumParams.add(spectrumParams);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initParamsMap();
    }

    private void initParamsMap() {
        for (SpectrumParams params : this.mSpectrumParams) {
            List<Integer> typeList = params.getMediaType();
            for (Integer type : typeList) {
                this.mWeightValueMap.put(type, params.getWeightValues());
                this.mFrequencyValueMap.put(type, params.getFrequencyValues());
                this.mRatioDeltaArray.put(type.intValue(), Float.valueOf(params.getRatioDelta()));
            }
        }
        for (Map.Entry<Integer, double[]> entry : this.mWeightValueMap.entrySet()) {
            LogUtil.d(TAG, "key:" + entry.getKey() + " &value[0]:" + String.valueOf(entry.getValue()[0]));
        }
    }

    public double[] getWeightValue(int mediaType) {
        double[] value = this.mWeightValueMap.get(Integer.valueOf(mediaType));
        if (value != null) {
            return value;
        }
        return K_DEFAULT;
    }

    public float[] getFrequencyValue(int mediaType) {
        float[] value = this.mFrequencyValueMap.get(Integer.valueOf(mediaType));
        if (value != null) {
            return value;
        }
        return FREQUENCY_DEFAULT;
    }

    public float getRatioDeltaValue(int mediaType) {
        return this.mRatioDeltaArray.get(mediaType, Float.valueOf(0.25f)).floatValue();
    }

    private List<Integer> parseMediaType(String value) {
        if (!TextUtils.isEmpty(value)) {
            String[] typeArray = value.split("\\|");
            List<Integer> typeList = new ArrayList<>();
            for (String str : typeArray) {
                typeList.add(Integer.valueOf(str));
            }
            return typeList;
        }
        return null;
    }

    private double[] parseWeightValue(String value) {
        if (!TextUtils.isEmpty(value)) {
            String[] weightArray = value.split("\\|");
            double[] weightValue = new double[weightArray.length];
            for (int i = 0; i < weightArray.length; i++) {
                weightValue[i] = Double.valueOf(weightArray[i]).doubleValue();
            }
            return weightValue;
        }
        return null;
    }

    private float[] parseFrequencyValue(String value) {
        if (!TextUtils.isEmpty(value)) {
            String[] frequencyArray = value.split("\\|");
            float[] frequencyValue = new float[frequencyArray.length];
            for (int i = 0; i < frequencyArray.length; i++) {
                frequencyValue[i] = Float.valueOf(frequencyArray[i]).floatValue();
            }
            return frequencyValue;
        }
        return null;
    }

    public String getFileContent(File file) {
        if (file != null) {
            try {
                if (file.exists() && file.isFile()) {
                    StringBuilder builder = new StringBuilder();
                    InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
                    BufferedReader br = new BufferedReader(reader);
                    while (true) {
                        String line = br.readLine();
                        if (line != null) {
                            builder.append(line);
                        } else {
                            br.close();
                            reader.close();
                            return builder.toString();
                        }
                    }
                } else {
                    return "";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }
}
