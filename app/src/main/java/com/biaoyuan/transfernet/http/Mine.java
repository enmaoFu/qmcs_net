package com.biaoyuan.transfernet.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/6/19.
 */

public interface Mine {

    /**
     * 我的取件分页查询
     * @param packageId
     * @param targetPage
     * @param orderStatus
     * @return
     */
    @POST("staff/getMyPickUp")
    @FormUrlEncoded
    Call<ResponseBody> getMyPickUp(@Field("pageSize") int packageId, @Field("targetPage") int targetPage, @Field("orderStatus") int orderStatus);

    /**
     * 我的发出包裹分页
     * @param packageId
     * @param targetPage
     * @param packageStatus
     * @return
     */
    @POST("staff/myParcelIsSentByType")
    @FormUrlEncoded
    Call<ResponseBody> myParcelIsSentByType(@Field("pageSize") int packageId, @Field("targetPage") int targetPage, @Field("packageStatus") int packageStatus,
                                            @Field("affilId") String affilId);

    /**
     * 我的发出包裹详情
     * @param packageId
     * @return
     */
    @POST("staff/myParcelIsSentDetail")
    @FormUrlEncoded
    Call<ResponseBody> myParcelIsSentDetail(@Field("packageId") String packageId);

    /**
     * 我的发出包裹货物详情
     * @param packageId
     * @return
     */
    @POST("staff/cargoDetails")
    @FormUrlEncoded
    Call<ResponseBody> cargoDetails(@Field("packageId") String packageId);

    /**
     * 我的待收包裹分页查询
     * @param pageSize
     * @param targetPage
     * @return
     */
    @POST("staff/myPackageIsWaiting")
    @FormUrlEncoded
    Call<ResponseBody> myPackageIsWaiting(@Field("pageSize") int pageSize, @Field("targetPage") int targetPage, @Field("affilId") String affilId);

    /**
     * 我的签收快件分页查询
     * @param pageSize
     * @param targetPage
     * @return
     */
    @POST("staff/mySignForExpress")
    @FormUrlEncoded
    Call<ResponseBody> mySignForExpress(@Field("pageSize") int pageSize, @Field("targetPage") int targetPage);

    /**
     * 我的签收快件(已派送)详情
     * @param orderId
     * @return
     */
    @POST("staff/mySignForExpressDetail")
    @FormUrlEncoded
    Call<ResponseBody> mySignForExpressDetail(@Field("orderId") String orderId);

    /**
     * 我的签收快件(已派送)货物详情
     * @param orderId
     * @return
     */
    @POST("staff/mySignForExpressGoodsDetail")
    @FormUrlEncoded
    Call<ResponseBody> mySignForExpressGoodsDetail(@Field("orderId") String orderId);

    /**
     * 我的代发快件分页查询
     * @param pageSize
     * @param targetPage
     * @param orderStatus
     * @return
     */
    @POST("staff/myExpressMail")
    @FormUrlEncoded
    Call<ResponseBody> myExpressMail(@Field("pageSize") int pageSize, @Field("targetPage") int targetPage, @Field("orderStatus") int orderStatus);

    /**
     * 我的代发快件详情
     * @param orderId
     * @return
     */
    @POST("staff/myExpressMailDetail")
    @FormUrlEncoded
    Call<ResponseBody> myExpressMailDetail(@Field("orderId") String orderId);

    /**
     * 我的异常快件分页查询
     * @param pageSize
     * @param targetPage
     * @param excepType
     * @return
     */
    @POST("staff/myAbnormalExpress")
    @FormUrlEncoded
    Call<ResponseBody> myAbnormalExpress(@Field("pageSize") int pageSize, @Field("targetPage") int targetPage ,@Field("excepType") int excepType);

    /**
     * 我的异常快件详情
     * @param excepId
     * @param excepType
     * @return
     */
    @POST("staff/myAbnormalExpressDetail")
    @FormUrlEncoded
    Call<ResponseBody> myAbnormalExpressDetail(@Field("excepId") String excepId, @Field("excepType") int excepType);

    /**
     * 我的异常快件(破损)详情
     * @param excepId
     * @return
     */
    @POST("staff/myAbnormalExpressBeDamagedDetail")
    @FormUrlEncoded
    Call<ResponseBody> myAbnormalExpressBeDamagedDetail(@Field("excepId") String excepId,@Field("orderType") String orderType);

    /**
     * 评论
     * @param pageSize
     * @param targetPage
     * @param basicId
     * @return
     */
    @POST("nw/viewComment")
    @FormUrlEncoded
    Call<ResponseBody> viewComment(@Field("pageSize") String pageSize, @Field("targetPage") String targetPage, @Field("basicId") String basicId,@Field("operand") String operand);

    /**
     * 评论详情
     * @param commentId
     * @return
     */
    @POST("nw/commentObject")
    @FormUrlEncoded
    Call<ResponseBody> commentObject(@Field("basicId") String basicId, @Field("commentId") String commentId);

   /**
     * 意见反馈
     * @param feedbackContent
     * @return
     */
    @POST("nw/addFeedback")
    @FormUrlEncoded
    Call<ResponseBody> addFeedback(@Field("feedbackContent") String feedbackContent);

    /**
     * 个人中心（我的）
     * @return
     */
    @POST("personalcenter/toPersonalCenter")
    @FormUrlEncoded
    Call<ResponseBody> toPersonalCenter(@Field("basicId") long basicId);

    /**
     * 修改手机号第一步，密码比对
     * @param password
     * @return
     */
    @POST("personalcenter/staffInPasswordVerification")
    @FormUrlEncoded
    Call<ResponseBody> staffInPasswordVerification(@Field("password") String password);

    /**
     * 设置用户基本信息
     * @param uuid
     * @param token
     * @param staffPortrait
     * @param staffNickname
     * @param staffTephone
     * @param phoneCode
     * @return
     */
    @POST("personalcenter/setStaffInformation")
    @FormUrlEncoded
    Call<ResponseBody> setStaffInformation(@Field("uuid") String uuid,@Field("token") String token, @Field("staffPortrait") String staffPortrait,
                                           @Field("staffNickname") String staffNickname,@Field("staffTephone") long staffTephone,
                                           @Field("phoneCode") String phoneCode);

    /**
     * 上传头像
     * @param uuid
     * @param token
     * @param staffPortrait
     * @param staffNickname
     * @param staffVersion
     * @param phoneCode
     * @return
     */
    @POST("personalcenter/setStaffInformation")
    @FormUrlEncoded
    Call<ResponseBody> setStaffInformationNew(@Field("uuid") String uuid,@Field("token") String token, @Field("staffPortrait") String staffPortrait,
                                           @Field("staffNickname") String staffNickname,@Field("staffVersion") int staffVersion,
                                           @Field("phoneCode") String phoneCode);

    /**
     * 修改手机号，发送短信
     * @param staffTephone
     * @return
     */
    @POST("unauth/sendSmsNotSignin")
    @FormUrlEncoded
    Call<ResponseBody> sendSmsNotSignin(@Field("staffTephone") String staffTephone);

    /**
     * 进入账号管理页面所需要的数据
     * @return
     */
    @POST("personalcenter/toAdministration")
    Call<ResponseBody> toAdministration();

    /**
     * 根据网点id修改对应的网点联系电话
     * @param basicId
     * @param basicTelphone
     * @return
     */
    @POST("personalsettings/updateAffiliateBasic")
    @FormUrlEncoded
    Call<ResponseBody> updateAffiliateBasic(@Field("basicId") long basicId, @Field("basicTelphone") String basicTelphone);

}
