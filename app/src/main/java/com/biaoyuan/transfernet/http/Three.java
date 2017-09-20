package com.biaoyuan.transfernet.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/6/13.
 */

public interface Three {

    /**
     * 三方快件
     * @param limitStart
     * @param limitEnd
     * @return
     */
    @POST("third/getThirdOrders")
    @FormUrlEncoded
    Call<ResponseBody> getThirdOrders(@Field("limitStart") int limitStart, @Field("limitEnd") int limitEnd, @Field("affillId") long affillId);

    /**
     * 添加三方快递信息
     * @param orderId
     * @param expressName
     * @param expressNo
     * @return
     */
    @POST("third/expressInfo")
    @FormUrlEncoded
    Call<ResponseBody> expressInfo(@Field("orderId") long orderId, @Field("expressName") String expressName, @Field("expressId") String expressId,@Field("expressNo") String expressNo);

    /**
     * 三方快件详情
     * @param orderId
     * @return
     */
    @POST("third/thirdOrderInfo")
    @FormUrlEncoded
    Call<ResponseBody> thirdOrderInfo(@Field("orderId") long orderId);

    /**
     * 鲜花水果直送服务
     * @param affillId
     * @param limitStart
     * @param limitEnd
     * @return
     */
    @POST("flowerOrCalace/getTypeOrders")
    @FormUrlEncoded
    Call<ResponseBody> getTypeOrders(@Field("affillId") long affillId, @Field("limitStart") int limitStart, @Field("limitEnd") int limitEnd);

    /**
     * 签收鲜花蛋糕
     * @param orderId
     * @param signCode
     * @return
     */
    @POST("flowerOrCalace/updateAccept")
    @FormUrlEncoded
    Call<ResponseBody> updateAccept(@Field("orderId") long orderId, @Field("signCode") String signCode, @Field("lng") long lng, @Field("lat") long lat);

}
