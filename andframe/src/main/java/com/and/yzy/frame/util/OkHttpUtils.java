package com.and.yzy.frame.util;

import com.and.yzy.frame.application.BaseApplication;
import com.and.yzy.frame.http.CommonParamsInterceptor;

import java.io.File;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


public class OkHttpUtils {
    private static OkHttpClient.Builder singleton;


    public static OkHttpClient getInstance() {
        if (singleton == null) {
            synchronized (OkHttpUtils.class) {
                if (singleton == null) {
                    if (singleton != null) {
                        singleton = null;
                    }

                    singleton = new OkHttpClient().newBuilder();
                    File cacheDir = new File(BaseApplication.getApplicationCotext().getCacheDir(), "okhttp/cache");

                    try {
                        singleton.cache(new Cache(cacheDir, 1024 * 1024 * 10));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    singleton.addInterceptor(httpLoggingInterceptor);
                    singleton.addInterceptor(new CommonParamsInterceptor());


                    singleton.connectTimeout(10, TimeUnit.SECONDS);
                    singleton.readTimeout(10, TimeUnit.SECONDS);
                    singleton.writeTimeout(20, TimeUnit.SECONDS);


                    //设置证书
                    try {
                        // 自定义一个信任所有证书的TrustManager，添加SSLSocketFactory的时候要用到
                        final X509TrustManager trustAllCert =
                                new X509TrustManager() {
                                    @Override
                                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                                    }

                                    @Override
                                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                                    }

                                    @Override
                                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                        return new java.security.cert.X509Certificate[]{};
                                    }
                                };
                        SSLSocketFactory sslSocketFactory = new SSLSocketFactoryCompat(trustAllCert);
                        singleton.sslSocketFactory(sslSocketFactory, trustAllCert);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }
        return singleton.build();
    }

   /* public static void setTokenUUId(final String token,final String uuid) {
        singleton.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .header("token", token) // <-- this is the important line
                        .header("uuid", uuid); // <-- this is the important line

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
    }*/

}
