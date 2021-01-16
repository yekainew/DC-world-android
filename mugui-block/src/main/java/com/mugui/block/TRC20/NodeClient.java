package com.mugui.block.TRC20;

import com.google.gson.Gson;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NodeClient {
    private String base;
    private OkHttpClient client;
    private Gson gson;
    private MediaType mimeJson;
    private Logger logger;

    public NodeClient(String uri) {
        base = uri;
        client = new OkHttpClient();
        gson = new Gson();
        mimeJson = MediaType.get("application/json; charset=utf-8");

        logger = LoggerFactory.getLogger(NodeClient.class);
    }


    public <T> T get(Class<T> clazz, String api) throws Exception {
        return get(clazz, api, new HashMap<String, String>());
    }

    public <T> T get(Class<T> clazz, String api, Map<String, String> params) throws Exception {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(base).newBuilder().addPathSegments(api);
        params.forEach((k, v) -> urlBuilder.addQueryParameter(k, v));
        String url = urlBuilder.build().toString();
        logger.info("get url: {}", new Object[]{url});
        Request req = new Request.Builder().url(url).build();
        Response rsp = client.newCall(req).execute();
        if (rsp.code() != 200) {
            logger.error("{} {}", new Object[]{rsp.code(), rsp.message()});
            throw new Exception("get error. check logs to find out details of the failure.");
        }
        String content = rsp.body().string();
        logger.info("response: {}", new Object[]{content});
        return gson.fromJson(content, clazz);
    }


    public <T> T post(Class<T> clazz, String api) throws Exception {
        Map params = new HashMap<String, String>();
        return post(clazz, api, params);
    }

    public <T> T post(Class<T> clazz, String api, Object params) throws Exception {
        String url = HttpUrl.parse(base).newBuilder().addPathSegments(api).build().toString();
        String payload = gson.toJson(params);
        logger.info("request: {}", new Object[]{payload});
        RequestBody body = RequestBody.create(payload, mimeJson);
        Request req = new Request.Builder().url(url).post(body).build();
        Response rsp = client.newCall(req).execute();
        if (rsp.code() != 200) {
            logger.error("{} {}", new Object[]{rsp.code(), rsp.message()});
            throw new Exception("post error. check logs to find out details of the failure.");
        }
        String content = rsp.body().string();
        logger.info("response: {}", new Object[]{content});
        return gson.fromJson(content, clazz);
    }

    public <T> T posts(Class<T> clazz, String url, Map paramsMap) {  //这里没有返回，也可以返回string
        url = HttpUrl.parse(base).newBuilder().addPathSegments(url).build().toString();

        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        Set<Object> keySet = paramsMap.keySet();
        for (Object key : keySet) {
            String value = paramsMap.get(key).toString();
            formBodyBuilder.add(key.toString(), value);
        }
        FormBody formBody = formBodyBuilder.build();
        Request request = new Request
                .Builder()
                .post(formBody)
                .url(url)
                .build();
        String content = "{}";
        try (Response response = mOkHttpClient.newCall(request).execute()) {
            content = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gson.fromJson(content, clazz);

    }
}