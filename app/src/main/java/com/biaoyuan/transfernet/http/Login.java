package com.biaoyuan.transfernet.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Title  :
 * Create : 2017/5/31
 * Author ：enmaoFu
 */

public interface Login {


    /**
     * 登录
     *
     * @param loginName
     * @param password
     * @return
     */
    @POST("login")
    @FormUrlEncoded
    Call<ResponseBody> login(@Field("loginName") String loginName, @Field("password") String password,
                             @Field("loginLng") double loginLng,@Field("loginLat") double loginLat,
                             @Field("loginAddress") String loginAddress,@Field("loginDeviceCode") String loginDeviceCode
    );

    /**
     * 发送验证码
     */
    @POST("unauth/sendSms")
    @FormUrlEncoded
    Call<ResponseBody> sendSms(@Field("staffTephone") String staffTephone, @Field("sendType") String sendType);


    /**
     * 注册第一步
     *
     * @param userLoginName
     * @param userTelphone
     * @param verificationCode
     * @return
     */
    @POST("unauth/findPasswordVerification")
    @FormUrlEncoded
    Call<ResponseBody> mobileVerification(@Field("userLoginName") String userLoginName, @Field("userTelphone") String userTelphone
            , @Field("verificationCode") String verificationCode
    );

    /**
     * 注册第二步
     */
    @POST("unauth/register")
    @FormUrlEncoded
    Call<ResponseBody> register(@Field("userLoginName") String userLoginName, @Field("userTelphone") String userTelphone
            , @Field("userPassword") String userPassword, @Field("reportUserPassword") String reportUserPassword
    );

    /**
     * 退出登录
     */
    @POST("unauth/logout")
    Call<ResponseBody> logout();
    /**
     * 找回密码第一步
     */
    @POST("unauth/findPasswordVerification")
    @FormUrlEncoded
    Call<ResponseBody> findPasswordVerification(@Field("staffTephone") String staffTephone, @Field("verificationCode") String verificationCode
    );

    /**
     * 找回密码第二步
     */
    @POST("unauth/findPassword")
    @FormUrlEncoded
    Call<ResponseBody> findPassword(@Field("staffTephone") String staffTephone, @Field("newStaffPassword") String newStaffPassword
            , @Field("reportNewStaffPasswprd") String reportNewStaffPasswprd
    );

}
