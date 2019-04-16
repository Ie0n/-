package com.example.feng.version1.http;

import java.io.IOException;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpRequest {

    private volatile static HttpRequest instance = null;

    private OkHttpClient client;

    private HttpRequest(){
        client = new OkHttpClient();
    }

    public static HttpRequest getInstance(){
        if (instance == null){
            synchronized (HttpRequest.class){
                if (instance == null){
                    instance = new HttpRequest();
                }
            }
        }
        return instance;
    }


    public void post(String url, RequestBody body, Callback callback, String cookie){
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("cookie",cookie)
                .build();
         client.newCall(request).enqueue(callback);
    }

    public Response post(String url, RequestBody body, String cookie){
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("cookie",cookie)
                .build();
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

   public void get(String url,Callback callback,String cookie){
        Request request = new Request.Builder()
                .url(url)
                .header("Cookie",cookie)
                .build();
        client.newCall(request).enqueue(callback);
   }
}
