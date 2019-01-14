package com.example.hasee.express.express.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * 构造HTTP请求类
 */
public class PostUtil {

    public static String TAG = "PostUtil";
    public static String REGISTER_ROUTE = "http://10.0.2.2:3000/register";
    public static String LOGIN_ROUTE = "http://10.0.2.2:3000/login";
    public static String ROOT_ROUTE = "http://10.0.2.2:3000/root";
    public static String DETAIL_ROUTE = "http://10.0.2.2/detail";
    public static String ADD_ROUTE = "http://10.0.2.2/add";
    public static String DELETE_ROUTE = "http://10.0.2.2/delete";

    private String url;
    private Map<String, String> paraMap;

    public PostUtil(String route, Map<String, String> map) {
        //url = new StringBuilder(route);
        url = route;
        paraMap = map;
    }

    /**
     * 发起网络请求
     * @return
     */
    public JSONObject post() {
        try {
            return new JSONObject(buildRequest());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 构造POST请求并发送
     * @return 服务器返回的String
     */
    private String buildRequest() {
        try {
            HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();

            //设置请求方式和超时信息
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            //设置运行输入,输出:
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //Post方式不能缓存,需手动设置为false
            conn.setUseCaches(false);
            //发送请求
            OutputStream out = conn.getOutputStream();
            out.write(addParameter().getBytes());
            out.flush();

            if (conn.getResponseCode() == 200) {
                InputStream in = conn.getInputStream();
                ByteArrayOutputStream response = new ByteArrayOutputStream();

                int bytesRead = 0;
                byte buffer[] = new byte[1024];
                while ((bytesRead = in.read(buffer)) > 0) {
                    response.write(buffer, 0, bytesRead);
                }

                in.close();
                response.close();

                return new String(response.toByteArray());
            }

            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 构造请求参数
     * @return POST的请求体
     */
    private String addParameter() {
        StringBuilder builder = new StringBuilder();

        for (String key: paraMap.keySet()) {
            builder.append("&")
                    .append(key).append("=")
                    .append(paraMap.get(key));
        }

        return builder.deleteCharAt(0).toString();
    }
}
