package OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0Oo;

import OooO00o.OooO0O0.OooO0O0.OooO0O0;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.loostone.libserver.version1.entity.PMConfig;
import com.loostone.libserver.version1.entity.response.ResponseData;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import kotlin.UByte;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes.dex */
public class OooO00o implements OooO0o {

    /* renamed from: OooO00o  reason: collision with root package name */
    public final String f258OooO00o = OooO00o.class.getSimpleName();
    public Class<?> OooO0O0;

    /* renamed from: OooO00o.OooO0O0.OooO0Oo.OooO0OO.OooO0Oo.OooO00o$OooO00o  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public class C0008OooO00o extends TypeReference<ResponseData> {
        public C0008OooO00o(OooO00o oooO00o) {
        }
    }

    public OooO00o(Class<?> cls) {
        this.OooO0O0 = cls;
    }

    public ResponseData OooO00o(Object obj) {
        Exception e;
        ResponseData responseData;
        if (obj instanceof String) {
            ResponseData responseData2 = new ResponseData();
            try {
                responseData = (ResponseData) JSON.parseObject(OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO00o(((String) obj).trim(), PMConfig.getToken()).trim(), new C0008OooO00o(this), new Feature[0]);
            } catch (Exception e2) {
                e = e2;
                responseData = responseData2;
            }
            try {
                if (responseData.getData() != null && (responseData.getData() instanceof JSONArray)) {
                    responseData.setData(JSON.parseArray(responseData.getData().toString(), this.OooO0O0));
                } else if (responseData.getData() != null && (responseData.getData() instanceof JSONObject)) {
                    responseData.setData(JSON.parseObject(responseData.getData().toString(), this.OooO0O0));
                }
            } catch (Exception e3) {
                e = e3;
                e.printStackTrace();
                String tag = this.f258OooO00o;
                String msg = e.getMessage();
                Intrinsics.checkNotNullParameter(tag, "tag");
                Intrinsics.checkNotNullParameter(msg, "msg");
                if (OooO0O0.OooO0O0 <= 5) {
                    Log.e("LogTuning", tag + " -> " + msg);
                }
                responseData.setState("-000001");
                responseData.setMsg("decrypt json failed");
                return responseData;
            }
            return responseData;
        }
        throw new IllegalArgumentException("the param must be instance of String");
    }

    public String OooO0O0(Object obj) {
        Field[] declaredFields;
        byte[] bArr;
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.getType() == String.class) {
                field.setAccessible(true);
                try {
                    if (field.get(obj) != null) {
                        field.set(obj, URLEncoder.encode(field.get(obj).toString()));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    String tag = this.f258OooO00o;
                    String msg = e.getMessage();
                    Intrinsics.checkNotNullParameter(tag, "tag");
                    Intrinsics.checkNotNullParameter(msg, "msg");
                    if (OooO0O0.OooO0O0 <= 5) {
                        Log.e("LogTuning", tag + " -> " + msg);
                    }
                }
            }
        }
        String jSONString = JSON.toJSONString(obj);
        String token = PMConfig.getToken();
        String OooO0O0 = OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO0O0(token);
        String OooO0OO = OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO0OO(token);
        String str = null;
        if (jSONString != null && jSONString.length() != 0) {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(OooO0O0.getBytes());
            SecretKeySpec secretKeySpec = new SecretKeySpec(OooO0OO.getBytes(), "AES");
            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
                cipher.init(1, secretKeySpec, ivParameterSpec);
                int length = 16 - (jSONString.length() % 16);
                String str2 = jSONString;
                for (int i = 0; i < length; i++) {
                    str2 = str2 + ' ';
                }
                bArr = cipher.doFinal(str2.getBytes());
            } catch (Exception e2) {
                e2.printStackTrace();
                bArr = null;
            }
            if (bArr != null) {
                int length2 = bArr.length;
                str = "";
                for (int i2 = 0; i2 < length2; i2++) {
                    str = (bArr[i2] & UByte.MAX_VALUE) < 16 ? str + "0" + Integer.toHexString(bArr[i2] & UByte.MAX_VALUE) : str + Integer.toHexString(bArr[i2] & UByte.MAX_VALUE);
                }
            }
        }
        return str;
    }
}
