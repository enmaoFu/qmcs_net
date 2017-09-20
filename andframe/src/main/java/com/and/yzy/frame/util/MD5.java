package com.and.yzy.frame.util;

import com.orhanobut.logger.Logger;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * MD5 算法
 */
public class MD5 {


    public static String getMD5(String content) {
        Logger.v("content==" + content);
        String s = makeMD5(content);
        String s1 = null;
        if (s != null) {
            s1 = s.substring(0, 16);
        }
        String s2 = null;
        if (s != null) {
            s2 = s.substring(16, 32);
        }
        s1 = makeMD5(s1);
        s2 = makeMD5(s2);
        s = s1 + s2;
        for (int i = 0; i < 100; i++) {
            s = makeMD5(s);
        }
        Logger.v("s==" + s);
        return s;
    }

    private static String makeMD5(String content) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            try {
                messageDigest.update(content.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return getHashString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getHashString(MessageDigest messageDigest) {
        StringBuilder builder = new StringBuilder();
        for (byte b : messageDigest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.toString();
    }

}