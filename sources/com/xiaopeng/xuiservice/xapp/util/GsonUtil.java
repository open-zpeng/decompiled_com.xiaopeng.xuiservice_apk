package com.xiaopeng.xuiservice.xapp.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/* loaded from: classes5.dex */
public class GsonUtil {
    private static final String TAG = "GsonUtil";
    private static Gson sGSon = new GsonBuilder().registerTypeAdapterFactory(new CustomTypeAdapterFactory()).create();
    private static Gson sGsonExclude = new GsonBuilder().registerTypeAdapterFactory(new CustomTypeAdapterFactory()).excludeFieldsWithoutExposeAnnotation().create();
    private static Gson sGsonNoHTML = new GsonBuilder().disableHtmlEscaping().registerTypeAdapterFactory(new CustomTypeAdapterFactory()).create();
    private static Gson sGsonMap = new GsonBuilder().registerTypeAdapter(new TypeToken<Map<String, Object>>() { // from class: com.xiaopeng.xuiservice.xapp.util.GsonUtil.1
    }.getType(), new MapTypeAdapter()).create();

    public static Map<String, Object> fromJson2Map(String json) {
        return (Map) sGsonMap.fromJson(json, new TypeToken<Map<String, Object>>() { // from class: com.xiaopeng.xuiservice.xapp.util.GsonUtil.2
        }.getType());
    }

    public static String toJsonNoHTML(Object o) {
        return sGsonNoHTML.toJson(o);
    }

    public static String toJson(Object o) {
        return sGSon.toJson(o);
    }

    public static String toJson(Object o, boolean isExclude) {
        return (isExclude ? sGsonExclude : sGSon).toJson(o);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        try {
            T result = (T) sGSon.fromJson(json, (Class<Object>) classOfT);
            return result;
        } catch (Exception e) {
            LogUtil.w(TAG, "not json string: " + json);
            return null;
        }
    }

    public static <T> T fromJson(String json, Class<T> classOfT, boolean isExclude) {
        try {
            T result = (T) (isExclude ? sGsonExclude : sGSon).fromJson(json, (Class<Object>) classOfT);
            return result;
        } catch (Exception e) {
            LogUtil.w(TAG, "not json string: " + json);
            return null;
        }
    }

    public static <T> T fromJson(JsonElement json, Class<T> classOfT) {
        return (T) sGSon.fromJson(json, (Class<Object>) classOfT);
    }

    public static <T> T fromJson(JsonElement json, Type typeOfT) {
        return (T) sGSon.fromJson(json, typeOfT);
    }

    public static <T> T fromJson(JsonElement json, Class<T> classOfT, boolean isExclude) {
        return (T) (isExclude ? sGsonExclude : sGSon).fromJson(json, (Class<Object>) classOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return (T) sGSon.fromJson(json, typeOfT);
    }

    /* loaded from: classes5.dex */
    public static class FloatAdapter extends TypeAdapter<Float> {
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        public Float read(JsonReader reader) throws IOException {
            float result = 0.0f;
            if (reader.peek() == JsonToken.STRING || reader.peek() == JsonToken.NUMBER) {
                String value = reader.nextString();
                try {
                    BigDecimal bigDecimal = new BigDecimal(value);
                    result = bigDecimal.floatValue();
                } catch (Exception e) {
                    LogUtil.w(GsonUtil.TAG, "FloatAdapter json error value:" + value);
                }
            } else {
                reader.skipValue();
            }
            return Float.valueOf(result);
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter writer, Float value) throws IOException {
            writer.value(value);
        }
    }

    /* loaded from: classes5.dex */
    public static class DoubleAdapter extends TypeAdapter<Double> {
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.TypeAdapter
        public Double read(JsonReader reader) throws IOException {
            double result = 0.0d;
            if (reader.peek() == JsonToken.STRING || reader.peek() == JsonToken.NUMBER) {
                String value = reader.nextString();
                try {
                    BigDecimal bigDecimal = new BigDecimal(value);
                    result = bigDecimal.doubleValue();
                } catch (Exception e) {
                    LogUtil.w(GsonUtil.TAG, "DoubleAdapter json error value:" + value);
                }
            } else {
                reader.skipValue();
            }
            return Double.valueOf(result);
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter writer, Double value) throws IOException {
            writer.value(value);
        }
    }

    /* loaded from: classes5.dex */
    public static class CustomTypeAdapterFactory<T> implements TypeAdapterFactory {
        @Override // com.google.gson.TypeAdapterFactory
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<? super T> rawType = type.getRawType();
            if (rawType == Float.class || rawType == Float.TYPE) {
                return new FloatAdapter();
            }
            if (rawType == Double.class || rawType == Double.TYPE) {
                return new DoubleAdapter();
            }
            return null;
        }
    }

    /* loaded from: classes5.dex */
    public static class MapTypeAdapter extends TypeAdapter<Object> {
        @Override // com.google.gson.TypeAdapter
        public Object read(JsonReader in) throws IOException {
            JsonToken token = in.peek();
            switch (token) {
                case BEGIN_ARRAY:
                    List<Object> list = new ArrayList<>();
                    in.beginArray();
                    while (in.hasNext()) {
                        list.add(read(in));
                    }
                    in.endArray();
                    return list;
                case BEGIN_OBJECT:
                    Map<String, Object> map = new LinkedTreeMap<>();
                    in.beginObject();
                    while (in.hasNext()) {
                        map.put(in.nextName(), read(in));
                    }
                    in.endObject();
                    return map;
                case STRING:
                    return in.nextString();
                case NUMBER:
                    double dbNum = in.nextDouble();
                    if (dbNum > 9.223372036854776E18d) {
                        return Double.valueOf(dbNum);
                    }
                    long lngNum = (long) dbNum;
                    if (dbNum == lngNum) {
                        return Long.valueOf(lngNum);
                    }
                    return Double.valueOf(dbNum);
                case BOOLEAN:
                    return Boolean.valueOf(in.nextBoolean());
                case NULL:
                    in.nextNull();
                    return null;
                default:
                    throw new IllegalStateException();
            }
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, Object value) throws IOException {
        }
    }
}
