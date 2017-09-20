package com.and.yzy.frame.http;

import com.alibaba.fastjson.JSON;
import com.and.yzy.frame.config.HttpConfig;
import com.and.yzy.frame.config.UserInfoManger;
import com.and.yzy.frame.util.MD5;

import java.io.IOException;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Title  :
 * Create : 2017/7/28
 * Author ：chen
 */

public class CommonParamsInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {

        Request original = chain.request();


        Request originRequest = chain.request();


        RequestBody body = originRequest.body();
        String mSignTime = getTime();
        String mSign = null;

        if (body != null && body instanceof FormBody) {

            FormBody formBody = (FormBody) body;
            TreeMap<String, String> formBodyParamMap = new TreeMap<>();

            int bodySize = formBody.size();


            for (int i = 0; i < bodySize; i++) {
                formBodyParamMap.put(formBody.name(i), formBody.value(i));
            }

            formBodyParamMap.put("signtime", mSignTime);

            mSign = JSON.toJSONString(formBodyParamMap);

        } else {

            TreeMap<String, String> formBodyParamMap = new TreeMap<>();

            formBodyParamMap.put("signtime", mSignTime);
            mSign = JSON.toJSONString(formBodyParamMap);

        }
        Request.Builder requestBuilder = original.newBuilder()
                .header("signtime", mSignTime) // <-- this is the important line
                .header("secretsign", MD5.getMD5(mSign + HttpConfig.APPSECRETKEY))
                .header("token", UserInfoManger.getToken()) // <-- this is the important line
                .header("uuid", UserInfoManger.getUUid());


        Request request = requestBuilder.build();
        return chain.proceed(request);
    }


    public synchronized static String getTime() {

        return String.valueOf(System.currentTimeMillis());
    }
}



