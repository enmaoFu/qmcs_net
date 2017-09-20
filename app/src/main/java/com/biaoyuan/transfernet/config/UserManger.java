package com.biaoyuan.transfernet.config;

import android.text.TextUtils;

import com.and.yzy.frame.config.HttpConfig;
import com.and.yzy.frame.config.UserInfoManger;
import com.and.yzy.frame.util.SPUtils;

/**
 * Title  :用户状态管理
 * Create : 2017/5/27
 * Author ：chen
 */

public class UserManger extends UserInfoManger {

    public static final String ADDRESS = "address";
    public static final String STAFFISCONTRACTOR = "staffIsContractor";
    public static final String BASEID = "baseId";
    public static final String PHONE = "phone";
    public static final String IMAGEURL = "imageurl";
    public static final String USERPHONE = "userphone";
    public static final String USERPOST = "userpost";
    public static final String USERDATE = "userdate";

    public static final String ACOUNT = "acount";
    public static final String PWD = "pwd";
    public static final String URL = "url";

    public static final String LAT = "lat";
    public static final String LNG = "lng";

    public static final String ISOPEN = "isOpen";
    public static final String STAFFIDENTITYNO = "staffIdentityNo";

    /**
     * 每页显示条数
     */
    public static final String pageSize = "10";


    /**
     * 上传图片的最大值
     */
    public static final int MAXSIZE = 100 * 1024;

    /**
     * 图片上传回调
     */
    public static final String IMGCALLBACK = "http://j17430q253.imwork.net/qmcs-nw/nwsts/osscallBack";

    /**
     * 设置是否打开语音提示，默认0 为开启
     *
     * @param isOpen
     */
    public static void setIsOpen(String isOpen) {
        SPUtils spUtils = new SPUtils(FILE);
        spUtils.put(ISOPEN, isOpen);
    }

    /**
     * 身份证
     */
    public static void setStaffIdentityNo(String staffIdentityNo) {
        SPUtils spUtils = new SPUtils(FILE);
        spUtils.put(STAFFIDENTITYNO, staffIdentityNo);
    }

    public static String getStaffIdentityNo() {
        SPUtils spUtils = new SPUtils(FILE);
        return (String) spUtils.get(STAFFIDENTITYNO, getUUid());
    }

    public static String getIsPOpen() {
        SPUtils spUtils = new SPUtils(FILE);
        return (String) spUtils.get(ISOPEN, "0");
    }

    public static void setUserDate(String date) {
        SPUtils spUtils = new SPUtils(FILE);
        spUtils.put(USERDATE, date);
    }

    public static String getUserDate() {
        SPUtils spUtils = new SPUtils(FILE);
        return (String) spUtils.get(USERDATE, "");
    }

    public static void setUserPost(String spot) {
        SPUtils spUtils = new SPUtils(FILE);
        spUtils.put(USERPOST, spot);
    }

    public static String getUserPost() {
        SPUtils spUtils = new SPUtils(FILE);
        return (String) spUtils.get(USERPOST, "");
    }

    public static void setUserPhone(String phone) {
        SPUtils spUtils = new SPUtils(FILE);
        spUtils.put(USERPHONE, phone);
    }

    public static String getUserPhone() {
        SPUtils spUtils = new SPUtils(FILE);
        return (String) spUtils.get(USERPHONE, "");
    }

    public static void setImageURL(String url) {
        SPUtils spUtils = new SPUtils(FILE);
        spUtils.put(IMAGEURL, url);
    }

    public static String getImageURL() {
        SPUtils spUtils = new SPUtils(FILE);
        return (String) spUtils.get(IMAGEURL, "");
    }

    public static void setURL(String url) {
        SPUtils spUtils = new SPUtils(FILE);
        spUtils.put(URL, url);
    }

    public static String getURL() {
        SPUtils spUtils = new SPUtils(FILE);
        return (String) spUtils.get(URL, HttpConfig.BASE_URL);
    }

    public static void setAcount(String acount) {
        SPUtils spUtils = new SPUtils(FILE);
        spUtils.put(ACOUNT, acount);
    }

    public static void setPwd(String pwd) {
        SPUtils spUtils = new SPUtils(FILE);
        spUtils.put(PWD, pwd);
    }

    public static String getAcount() {
        SPUtils spUtils = new SPUtils(FILE);
        return (String) spUtils.get(ACOUNT, "");
    }

    public static String getPwd() {
        SPUtils spUtils = new SPUtils(FILE);
        return (String) spUtils.get(PWD, "");
    }

    public static void setLat(String lat) {
        SPUtils spUtils = new SPUtils(FILE);
        spUtils.put(LAT, lat);
    }

    public static String getLat() {
        SPUtils spUtils = new SPUtils(FILE);
        return (String) spUtils.get(LAT, "29.35");
    }

    public static String getLng() {
        SPUtils spUtils = new SPUtils(FILE);
        return (String) spUtils.get(LNG, "106.33");
    }

    public static void setLng(String lng) {
        SPUtils spUtils = new SPUtils(FILE);
        spUtils.put(LNG, lng);
    }

    public static void setPhone(String phone) {
        SPUtils spUtils = new SPUtils(FILE);
        spUtils.put(PHONE, phone);
    }

    public static String getPhone() {
        SPUtils spUtils = new SPUtils(FILE);
        return (String) spUtils.get(PHONE, "");
    }


    public static void setStaffIsContractors(int staffIsContractor) {
        SPUtils spUtils = new SPUtils(FILE);
        spUtils.put(STAFFISCONTRACTOR, staffIsContractor);
    }

    public static int getStaffIsContractor() {
        SPUtils spUtils = new SPUtils(FILE);
        return (int) spUtils.get(STAFFISCONTRACTOR, 0);
    }

    public static void setAddress(String address) {
        SPUtils spUtils = new SPUtils(FILE);
        spUtils.put(ADDRESS, address);
    }

    public static void setBaseId(String baseId) {
        SPUtils spUtils = new SPUtils(FILE);
        spUtils.put(BASEID, baseId);
    }


    public static String getBaseId() {
        SPUtils spUtils = new SPUtils(FILE);

        if (TextUtils.isEmpty((String) spUtils.get(BASEID, "0"))) {
            return "0";
        }

        return (String) spUtils.get(BASEID, "0");
    }


    public static String getAddress() {
        SPUtils spUtils = new SPUtils(FILE);
        return (String) spUtils.get(ADDRESS, "");
    }


}
