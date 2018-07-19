package com.link.cloud.network;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;


/**
 * Created by OFX002 on 2018/7/19.
 */

public class InterceptorUtil {
    public static String TAG="-------";

    public static HttpLoggingInterceptor LogInterceptor(){
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Logger.e(TAG+message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    public static Interceptor HeaderInterceptor(){
        return  new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request mRequest=chain.request();
                String code = "link";
                String datetime = "1512028642184";
                String key ="848ec6fa44ac6bae";
                String sign = "cc5224ee3e9f2e2089624f676d840524";
                Request.Builder requestBuilder = mRequest.newBuilder();
                JsonObject postBody = bodyToJsonObject(mRequest.body());
                if (postBody == null) {
                    postBody = new JsonObject();
                }
                postBody.addProperty("key", key);
                postBody.addProperty("datetime", datetime);
                postBody.addProperty("code", code);
                postBody.addProperty("sign", sign);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), postBody.toString());
                requestBuilder.post(requestBody);
                Request build = requestBuilder.build();
                return chain.proceed(build);
            }
        };

    }
    public static JsonObject bodyToJsonObject(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return null;
            return new JsonParser().parse(buffer.readUtf8()).getAsJsonObject();
        } catch (final Exception e) {
            return null;
        }
    }

}
