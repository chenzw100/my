package com.example.demo.utils;

import cn.hutool.http.HttpUtil;

import java.util.Map;

/**
 * Created by ShaoFan
 */
public class HttpClientUtil {
    public static String doPost(String url, Map<String, Object> map) {
        return HttpUtil.post(url, map);
    }
    public static String doPostBody(String url, String body) {
        return HttpUtil.post(url, body);
    }
    public static String doGet(String url) {
        return HttpUtil.get(url);
    }

}
