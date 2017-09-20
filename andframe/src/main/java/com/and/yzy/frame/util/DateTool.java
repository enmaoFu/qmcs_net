package com.and.yzy.frame.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/4/20.
 * 日期操作类
 */
public class DateTool {

    /**
     * 获取现在时间
     *
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
     */
    public static Date getNowDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }


    /**
     * 根据用户传入的时间表示格式，返回当前时间的格式 如果是yyyyMMdd，注意字母y不能大写。
     *
     * @param sformat yyyyMMddhhmmss
     * @return
     */
    public static String getformatDate(String sformat) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(sformat);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 将时间戳转为字符串单位为秒
     *
     * @param timestamp
     *            时间戳
     *  @param format 返回格式 可不传
     * @return String
     *//*
    public static String timestampToStrTime(String timestamp,String format) {
        if (null==format||format.length()==0){
            format="yyyy-MM-dd HH:mm:ss";
        }

        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        long lcc_time = Long.valueOf(timestamp);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }*/


    /**
     * 将短时间格式时间转换为字符串 yyyy-MM-dd
     *
     * @param dateDate
     * @param format   可不传，为默认格式
     * @return
     */
    public static String dateToStr(Date dateDate, String format) {
        if (null == format || format.length() == 0) {
            format = "yyyy-MM-dd HH:mm:ss";
        }

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将字符串转为时间戳
     *
     * @param strTime 时间字符串
     * @return String
     */
    public static long strTimeToTimestamp(String strTime) {
        // String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = null;
        try {
            d = sdf.parse(strTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d == null ? 0 : d.getTime();
    }

    /**
     * 将字符串转为时间戳
     *
     * @param strTime 时间字符串
     * @return String
     */
    public static long strTimeToTimestamp(String strTime, String format) {
        // String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d = null;
        try {
            d = sdf.parse(strTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d == null ? 0 : d.getTime();
    }


    /**
     * 得到现在分钟
     *
     * @return
     */
    public static String getNowMin() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        String min;
        min = dateString.substring(14, 16);
        return min;
    }

    /**
     * 得到现在小时
     *
     * @return
     */
    public static String getNowHour() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        String min;
        min = dateString.substring(11, 13);
        return min;
    }


    /**
     * 将时间戳转为字符串单位为毫秒
     *
     * @param timestamp 时间戳
     * @return String
     */
    public static String timesToStrTime(String timestamp, String format) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        long lcc_time = Long.valueOf(timestamp);
        re_StrTime = sdf.format(new Date(lcc_time));
        return re_StrTime;
    }


    /**
     * 得到今日 或者明日 或者其他时间
     * <p>
     * 时间戳
     *
     * @return String
     */
    public static String getTimeType(long time) {


        long nowTime=Long.parseLong(timesToStrTime(System.currentTimeMillis()+"","yyyyMMdd"));
        long curTime=Long.parseLong(timesToStrTime(time+"","yyyyMMdd"));


        if (nowTime==curTime){
            return "今日";
        }else if (curTime-nowTime==1){
            return "明日";
        }


        return null;
    }


}
