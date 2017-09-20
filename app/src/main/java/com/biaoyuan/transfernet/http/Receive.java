package com.biaoyuan.transfernet.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Title  :接收网点
 * Create : 2017/6/25
 * Author ：chen
 */

public interface Receive {

    /**
     * 扫描包裹信息详情
     *
     * @return
     */
    @POST("po/poView")
    @FormUrlEncoded
    Call<ResponseBody> getIndexReleaseDetailList(@Field("packageCode") String packageCode,@Field("StaffAffiliateId") String StaffAffiliateId);
    /**
     * 验收
     *
     * @return
     */
    @POST("po/poAcceptance")
    @FormUrlEncoded
    Call<ResponseBody> poAcceptance(@Field("packageId") String packageId,@Field("orderJsonArray") String orderJsonArray,@Field("StaffAffiliateId") String StaffAffiliateId);

    /**
     * 送达网点列表页
     * @param pageSize
     * @param targetPage
     * @param basicId
     * @return
     */
    @POST("po/sendOrderView")
    @FormUrlEncoded
    Call<ResponseBody> sendOrderView(@Field("pageSize") int pageSize, @Field("targetPage") int targetPage, @Field("basicId") int basicId);

    /**
     * 送达网点详情
     * @param orderId
     * @return
     */
    @POST("po/sendOrder")
    @FormUrlEncoded
    Call<ResponseBody> sendOrder(@Field("orderId") String orderId);

    /**
     * 拒绝签收
     * @param excepReason
     * @param excepType
     * @param excepPicUrl
     * @param orderId
     * @return
     */
    @POST("po/rejectionOrder")
    @FormUrlEncoded
    Call<ResponseBody> rejectionOrder(@Field("excepReason") String excepReason, @Field("excepType") String excepType, @Field("excepPicUrl") String excepPicUrl,
                                      @Field("orderId") String orderId);

    /**
     * 投递点签收
     * @param orderId
     * @param deliveryName
     * @param DeliveryCode
     * @param deliveryLng
     * @param deliveryLat
     * @return
     */
    @POST("po/deliveryOrder")
    @FormUrlEncoded
    Call<ResponseBody> deliveryOrder(@Field("orderId") String orderId, @Field("deliveryName") String deliveryName, @Field("DeliveryCode") String DeliveryCode,
                                     @Field("deliveryLng") String deliveryLng, @Field("deliveryLat") String deliveryLat);

    /**
     * 用户签收
     * @param orderId
     * @param orderSignCode
     * @param deliveryLng
     * @param deliveryLat
     * @return
     */
    @POST("po/userSignOrder")
    @FormUrlEncoded
    Call<ResponseBody> userSignOrder(@Field("orderId") String orderId, @Field("orderSignCode") String orderSignCode, @Field("deliveryLng") String deliveryLng,
                                     @Field("deliveryLat") String deliveryLat);

    /**
     * 获取派件员的信息
     * @return
     */
    @POST("nw/IdentityObject")
    Call<ResponseBody> IdentityObject();
}
