package com.duyi.readingweb.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpClientUtil {
    public static String urlStr="https://www.ptwxz.com/html/8/8253/5144852.html";
    public static String urlStr1="https://www.ptwxz.com/html/8/8253/";
    public static void main(String[] args) {
        String str=doGet(urlStr);
        System.out.println(str);
    }
    public static String doGet(String urlStr) {
        CloseableHttpClient client=null;
        CloseableHttpResponse response=null;
        String result=null;
        client= HttpClients.createDefault();
        HttpGet httpGet=new HttpGet(urlStr);
//        httpGet.addHeader("Accept","application/json");
        RequestConfig config=RequestConfig.custom()
                .setConnectTimeout(15000)
                .setConnectionRequestTimeout(35000)
                .setSocketTimeout(60000)
                .build();
        httpGet.setConfig(config);

        try{
            response=client.execute(httpGet);
            HttpEntity httpEntity= response.getEntity();
            result= EntityUtils.toString(httpEntity,"gbk");
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            if(response!=null){
                try {
                    response.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(client!=null){
                try {
                    client.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return result;
    }
}
