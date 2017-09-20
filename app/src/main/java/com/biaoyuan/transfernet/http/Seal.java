package com.biaoyuan.transfernet.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Title  :
 * Create : 2017/6/12
 * Author ：enmaoFu
 */

public interface Seal {

    /**
     * 得到网点端首页封包信息
     * @param basicId
     * @param pageSize
     * @param targetPage
     * @param packageStatus
     * @return
     */
    @POST("packet/getIndexPacket")
    @FormUrlEncoded
    Call<ResponseBody> getIndexPacket(@Field("basicId") String basicId, @Field("pageSize") int pageSize, @Field("targetPage") int targetPage,
                                      @Field("packageStatus") int packageStatus);

    /**
     * 得到网点端首页封包详细信息
     * @param packageId
     * @param packageStatus
     * @return
     */
    @POST("packet/getIndexPacketDetail")
    @FormUrlEncoded
    Call<ResponseBody> getIndexPacketDetail(@Field("packageId") String packageId, @Field("packageStatus") int packageStatus);

    /**
     * 我要加价
     * @param packageId
     * @param reqPickupTime
     * @param reqDelivTime
     * @param reward
     * @return
     */
    @POST("packet/parcelFareIncrease")
    @FormUrlEncoded
    Call<ResponseBody> parcelFareIncrease(@Field("packageId") String packageId, @Field("reqPickupTime") long reqPickupTime, @Field("reqDelivTime") long reqDelivTime,
                                          @Field("reward") double reward);

    /**
     * 再次发布
     * @param packageId
     * @return
     */
    @POST("packet/releaseAgain")
    @FormUrlEncoded
    Call<ResponseBody> releaseAgain(@Field("packageId") String packageId, @Field("reqPickupTime") long reqPickupTime, @Field("reqDelivTime") long reqDelivTime,
                                    @Field("reward") double reward);

    /**
     * 封包扫描
     * @param packageId
     * @return
     */
    @POST("packet/packetScanning")
    @FormUrlEncoded
    Call<ResponseBody> packetScanning(@Field("packageId") String packageId);

    /**
     * 删除包裹中的订单
     * @param packageOrderId
     * @return
     */
    @POST("packet/deletePackageOrder")
    @FormUrlEncoded
    Call<ResponseBody> deletePackageOrder(@Field("packageOrderId") String packageOrderId);


    /**
     * 验证打包码是否正确
     * @param packageCode
     * @return
     */
    @POST("packet/validatePackageCode")
    @FormUrlEncoded
    Call<ResponseBody> validatePackageCode(@Field("packageCode") String packageCode);

    /**
     * 封包中包裹新增订单
     * @param orderIds
     * @param packageId
     * @return
     */
    @POST("packet/addPackageOrder")
    @FormUrlEncoded
    Call<ResponseBody> addPackageOrder(@Field("orderIds") String orderIds, @Field("packageId") String packageId);

    /**
     * 封包

     */
    //TODO
    @POST("packet/packet")
    @FormUrlEncoded
    Call<ResponseBody> packet(@Field("packageId") String packageId, @Field("packageCode") String packageCode, @Field("publishDescription") String publishDescription,
                              @Field("orderIds") String orderIds, @Field("deliveryLng") double deliveryLng, @Field("deliveryLat") double deliveryLat,@Field("deletePoIdIds") String deletePackageOrderIds,
                              @Field("addPoOrderId") String addPackageOrderIds,@Field("publishPicsUrl") String publishPicsUrl
    );

}
