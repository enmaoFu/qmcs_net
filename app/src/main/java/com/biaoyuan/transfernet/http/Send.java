package com.biaoyuan.transfernet.http;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * Title  :
 * Create : 2017/5/31
 * Author ：enmaoFu
 */

public interface Send {

    /**
     * 返回子网点
     *
     * @return
     */
    @POST("nw/viewAffiliate")
    @FormUrlEncoded
    Call<ResponseBody> viewAffiliate(@Field("contractor")int contractor);

    /**
     * 取件列表
     *
     * @return
     */
    @POST("nw/order")
    @FormUrlEncoded
    Call<ResponseBody> takeOrder(@Field("basicId")String basicId,@Field("pageSize")String pageSize,@Field("targetPage")String targetPage);
    /**
     * 查询取件详情确认
     *
     * @return
     */
    @POST("pickup/staffConfirm")
    @FormUrlEncoded
    Call<ResponseBody> staffConfirm(@Field("orderId") String orderId);

    /**
     * 确认接件
     *
     * @return
     */
    @POST("pickup/confirmConnector")
    @FormUrlEncoded
    Call<ResponseBody> confirmConnector(@Field("orderId") String orderId);

    /**
     * 确认取件
     *
     * @return
     */
    //TODO
    @POST("pickup/confirmTake")
    @FormUrlEncoded
    Call<ResponseBody> confirmTake(@Field("orderId") String orderId, @Field("deliveryLng") double deliveryLng, @Field("deliveryLat") double deliveryLat,
                                   @Field("orderTrackingCode") String orderTrackingCode);

    /**
     * 测试图片
     *
     * @return
     */
    @POST("po/poView")
    @Multipart
    Call<ResponseBody> poView(@PartMap Map<String, RequestBody> params);

    /**
     * 得到首页发布信息列表
     *
     * @return
     */
    @POST("release/getIndexRelease")
    @FormUrlEncoded
    Call<ResponseBody> getIndexRelease(@Field("basicId") String basicId, @Field("pageSize") int pageSize, @Field("targetPage") int targetPage);

    /**
     * 得到首页发布信息查看更多
     *
     * @return
     */
    @POST("release/getIndexReleaseDetailList")
    @FormUrlEncoded
    Call<ResponseBody> getIndexReleaseDetailList(@Field("basicCode") String basicCode);

    /**
     * 网点端首页去发布 得到发件网点和到达网点
     *
     * @return
     */
    /*@POST("release/goPublic")
    @FormUrlEncoded
    Call<ResponseBody> getIndexReleaseDetailList(@Field("orderAffiliateReceive") String orderAffiliateReceive,
                                                 @Field("orderAffiliateSend") String orderAffiliateSend);*/

    /**
     * 网点端首页确认发布
     *
     * @return
     */
    @POST("release/confirmPublic")
    @FormUrlEncoded
    Call<ResponseBody> confirmPublic(@Field("orderIds") String orderIds, @Field("packageWeight") int packageWeight, @Field("packageSize") int packageSize,
                                     @Field("packageCarrierReward") double packageCarrierReward, @Field("orderAffiliateReceive") String orderAffiliateReceive,
                                     @Field("orderAffiliateSend") String orderAffiliateSend, @Field("publishReqPickupTime") long publishReqPickupTime,
                                     @Field("publishReqDelivTime") long publishReqDelivTime);

    /**
     * 得到首页发布信息查看更多
     *
     * @return
     */
    @POST("nw/staMessage")
    @FormUrlEncoded
    Call<ResponseBody> staMessage(@Field("pageSize") String pageSize, @Field("targetPage") String targetPage);

    /**
     * 追加金额
     * @param orderId
     * @param weight
     * @param size
     * @return
     */
    @POST("pickup/additionalAmount")
    @FormUrlEncoded
    Call<ResponseBody> additionalAmount(@Field("orderId") String orderId, @Field("weight") int weight, @Field("size") int size);

    /**
     * 拒绝接收
     * @param orderId
     * @param excepRejectionReason
     * @param excepReason
     * @param excepPicUrl
     * @return
     */
    @POST("pickup/refuseToReceive")
    @FormUrlEncoded
    Call<ResponseBody> refuseToReceive(@Field("orderId") String orderId, @Field("excepRejectionReason") String excepRejectionReason,
                                       @Field("excepReason") String excepReason,@Field("excepPicUrl") String excepPicUrl);


    /**
     * 消息删除
     * @param messageId
     * @return
     */
    @POST("nw/deleteMessage")
    @FormUrlEncoded
    Call<ResponseBody> deleteMessage(@Field("messageId") String messageId);

    /**
     * 获取订单最新状态
     * @param orderNumber
     * @return
     */
    @POST("staff/queryOrderByOrderNumber")
    @FormUrlEncoded
    Call<ResponseBody> queryOrderByOrderNumber(@Field("orderNumber") String orderNumber);

}
