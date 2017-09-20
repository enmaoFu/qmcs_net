package com.and.yzy.frame.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/4/11.
 */
public interface HttpCallBack {

    /**
     * 请求成功
     * @param result
     * @param call
     * @param response
     * @param what
     */
    void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what);

    /**
     * 请求失败
     * @param result
     * @param call
     * @param response
     * @param what
     */
    void onFailure(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what);

    /**
     * 网络错误
     * @param call
     * @param t
     * @param what
     */
    void onError(Call<ResponseBody> call, Throwable t, int what);

}
