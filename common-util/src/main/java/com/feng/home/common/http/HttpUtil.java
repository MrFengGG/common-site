package com.feng.home.common.http;

import com.alibaba.fastjson.JSONObject;
import com.feng.home.common.collection.Dict;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * HTTP工具类
 */
public class HttpUtil {

    public static JSONObject get(String url, Dict header, Dict param) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        url = connectUrl(url, param);
        HttpGet httpget = new HttpGet(url);
        header.keySet().forEach(key -> {
            String strKey = String.valueOf(key);
            httpget.addHeader(strKey, header.getStr(strKey));
        });
        CloseableHttpResponse response = httpclient.execute(httpget);
        JSONObject jsonResult = null;
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            HttpEntity entity = response.getEntity();
            String strResult = EntityUtils.toString(entity, "utf-8");
            jsonResult =  JSONObject.parseObject(strResult);
        }
        return jsonResult;
    }

    private static String connectUrl(String url, Dict param){
        StringBuilder urlBuilder = new StringBuilder(url);
        if(!url.contains("?")){
            urlBuilder.append("?");
        }
        urlBuilder.append(param.keySet().stream().map(key ->{
            String strKey = String.valueOf(key);
            return strKey + "=" + param.getStr(strKey);
        }).collect(Collectors.joining()));
        return urlBuilder.toString();
    }

}
