package com.biaoyuan.transfernet.util;

import java.text.DecimalFormat;

/**
 * Title  :
 * Create : 2017/6/5
 * Author ：chen
 */

public class MyNumberFormat {
    /**
     * 保留两位小数
     */
    public static String m2(double f) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(f);
    }

    /**
     * 隐藏电话号码
     * @param phone
     * @return
     */
    public static String toPwdPhone(long phone) {
        String strPhone=String.valueOf(phone);
        if (strPhone==null||strPhone.length()!=11){
            return  strPhone;
        }
        return strPhone.substring(0,4)+"****"+strPhone.substring(strPhone.length()-3,strPhone.length());
    }
}
