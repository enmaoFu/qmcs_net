package com.biaoyuan.transfernet.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Title  :
 * Create : 2017/6/20
 * Author ：chen
 */

public interface Image {

    /**
     * 获取图片token
     */
    @POST("nwsts/OrderAssumeRole")
    @FormUrlEncoded
    Call<ResponseBody> orderAssumeRole(@Field("stsObject") String stsObject);

    /**
     * 获取要删除的图片
     */
    @POST("nwsts/findKey")
    @FormUrlEncoded
    Call<ResponseBody> findKey(@Field("keyPrefix") String keyPrefix);


    /**
     * 轮播图
     */
    @POST("nwMent/listAdvertisement")
    @FormUrlEncoded
    Call<ResponseBody> listAdvertisement(@Field("adPosition") String adPosition, @Field("adPage") String adPage);
}
