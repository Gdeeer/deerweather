package com.deerweather.app.util;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by ${Deer} on 2015/6/22.
 */
public class HttpUtil {

    private static final String ENCODING = "UTF-8";
    private static final String MAC_NAME = "HmacSHA1";
    /**
     * 计算签名
     * @param publicKey 明文
     * @param privateKey 私钥
     * @return 秘钥
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    public static String computeKey(String publicKey, String privateKey)
            throws Exception {

        byte[] keyBytes = privateKey.getBytes();
        SecretKey secretKey = new SecretKeySpec(keyBytes, MAC_NAME);

        Mac mac = Mac.getInstance(MAC_NAME);
        mac.init(secretKey);

        byte[] text = publicKey.getBytes();

        byte[] text1 = mac.doFinal(text);
        String base64encoderStr = Base64.encodeToString(text1, Base64.NO_WRAP);

        return URLEncoder.encode(base64encoderStr, ENCODING);
    }

    public static void sendHttpRequest(final String address, final HttpCallBackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null){
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        // 回调onError()方法
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}

