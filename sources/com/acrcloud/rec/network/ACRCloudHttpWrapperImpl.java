package com.acrcloud.rec.network;

import com.acrcloud.rec.utils.ACRCloudException;
import com.acrcloud.rec.utils.ACRCloudLogger;
import com.android.volley.toolbox.JsonRequest;
import com.lzy.okgo.model.HttpHeaders;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
/* loaded from: classes4.dex */
public class ACRCloudHttpWrapperImpl implements IACRCloudHttpWrapper {
    public static final String BOUNDARY = "--*****2015.03.30.acrcloud.rec.copyright*****\r\n";
    public static final String BOUNDARYSTR = "*****2015.03.30.acrcloud.rec.copyright*****";
    public static final String HTTP_METHOD_GET = "GET";
    public static final String HTTP_METHOD_POST = "POST";
    private static final String TAG = "ACRCloudHttpWrapperImpl";

    @Override // com.acrcloud.rec.network.IACRCloudHttpWrapper
    public String doPost(String posturl, Map<String, Object> params, int timeout) throws ACRCloudException {
        String res = "";
        try {
            URL url = new URL(posturl);
            ACRCloudLogger.d(TAG, posturl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            conn.setRequestMethod(HTTP_METHOD_POST);
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept-Charset", JsonRequest.PROTOCOL_CHARSET);
            conn.setRequestProperty(HttpHeaders.HEAD_KEY_CONNECTION, HttpHeaders.HEAD_VALUE_CONNECTION_KEEP_ALIVE);
            conn.setRequestProperty("Content-type", "multipart/form-data;boundary=*****2015.03.30.acrcloud.rec.copyright*****");
            BufferedOutputStream out = null;
            try {
                try {
                    try {
                        conn.connect();
                        out = new BufferedOutputStream(conn.getOutputStream());
                        StringBuilder reqData = new StringBuilder();
                        if (params != null) {
                            for (String key : params.keySet()) {
                                Object value = params.get(key);
                                reqData.setLength(0);
                                if (!(value instanceof String) && !(value instanceof Integer)) {
                                    if (value instanceof byte[]) {
                                        reqData.append(BOUNDARY);
                                        reqData.append("Content-Disposition:form-data;");
                                        reqData.append("name=\"" + key + "\";");
                                        reqData.append("filename=\"janet.sig\"\r\n");
                                        reqData.append("Content-Type:application/octet-stream");
                                        reqData.append("\r\n\r\n");
                                        out.write(reqData.toString().getBytes());
                                        out.write((byte[]) value);
                                        out.write("\r\n".getBytes());
                                    }
                                }
                                reqData.append(BOUNDARY);
                                reqData.append("Content-Disposition:form-data;name=\"");
                                reqData.append(key);
                                reqData.append("\"\r\n\r\n");
                                reqData.append(value);
                                reqData.append("\r\n");
                                ACRCloudLogger.d(TAG, key + ":" + value);
                                out.write(reqData.toString().getBytes());
                            }
                            out.write("--*****2015.03.30.acrcloud.rec.copyright*****--\r\n\r\n".getBytes());
                        }
                        try {
                            out.flush();
                            out.close();
                            BufferedReader reader = null;
                            try {
                                try {
                                    int response = conn.getResponseCode();
                                    ACRCloudLogger.e(TAG, "" + response);
                                    if (response == 200) {
                                        BufferedReader reader2 = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                                        while (true) {
                                            String tmpRes = reader2.readLine();
                                            if (tmpRes != null) {
                                                if (tmpRes.length() > 0) {
                                                    res = res + tmpRes;
                                                }
                                            } else {
                                                ACRCloudLogger.d(TAG, res);
                                                try {
                                                    reader2.close();
                                                    return res;
                                                } catch (IOException e) {
                                                    ACRCloudException acrcExcept = new ACRCloudException(3000, "Http error " + e.getMessage());
                                                    throw acrcExcept;
                                                }
                                            }
                                        }
                                    } else {
                                        ACRCloudException acrcExcept2 = new ACRCloudException(3000, "server response code error, code=" + response);
                                        throw acrcExcept2;
                                    }
                                } catch (Exception e2) {
                                    ACRCloudException acrcExcept3 = new ACRCloudException(3000, "Http error " + e2.getMessage());
                                    throw acrcExcept3;
                                }
                            } catch (Throwable acrcExcept4) {
                                if (0 != 0) {
                                    try {
                                        reader.close();
                                    } catch (IOException e3) {
                                        ACRCloudException acrcExcept5 = new ACRCloudException(3000, "Http error " + e3.getMessage());
                                        throw acrcExcept5;
                                    }
                                }
                                throw acrcExcept4;
                            }
                        } catch (IOException e4) {
                            ACRCloudException acrcExcept6 = new ACRCloudException(3000, e4.getMessage());
                            throw acrcExcept6;
                        }
                    } catch (Throwable e5) {
                        if (out != null) {
                            try {
                                out.flush();
                                out.close();
                            } catch (IOException e6) {
                                ACRCloudException acrcExcept7 = new ACRCloudException(3000, e6.getMessage());
                                throw acrcExcept7;
                            }
                        }
                        throw e5;
                    }
                } catch (SocketTimeoutException e7) {
                    ACRCloudException acrcExcept8 = new ACRCloudException(2005, e7.getMessage());
                    throw acrcExcept8;
                }
            } catch (IOException e8) {
                ACRCloudException acrcExcept9 = new ACRCloudException(3000, e8.getMessage());
                throw acrcExcept9;
            }
        } catch (Exception e9) {
            ACRCloudException acrcExcept10 = new ACRCloudException(3000, e9.getMessage());
            throw acrcExcept10;
        }
    }

    @Override // com.acrcloud.rec.network.IACRCloudHttpWrapper
    public String doGet(String url, Map<String, String> params, int timeout) throws ACRCloudException {
        String result = "";
        BufferedReader in = null;
        if (params != null) {
            try {
                try {
                    StringBuilder tParams = new StringBuilder();
                    for (String key : params.keySet()) {
                        String value = params.get(key);
                        tParams.append(key + "=" + URLEncoder.encode(value, "UTF-8") + "&");
                    }
                    if (tParams.length() > 0) {
                        url = url + "?" + tParams.substring(0, tParams.length() - 1);
                    }
                } catch (Exception e) {
                    ACRCloudException acrcExcept = new ACRCloudException(3000, e.getMessage());
                    throw acrcExcept;
                }
            } catch (Throwable th) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                throw th;
            }
        }
        ACRCloudLogger.d(TAG, url);
        URL realUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        connection.setRequestMethod(HTTP_METHOD_GET);
        connection.connect();
        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while (true) {
            String line = in.readLine();
            if (line != null) {
                result = result + line;
            } else {
                try {
                    break;
                } catch (Exception e22) {
                    e22.printStackTrace();
                }
            }
        }
        in.close();
        return result;
    }
}
