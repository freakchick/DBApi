package com.gitee.freakchicken.dbapi.basic.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;

import sun.misc.BASE64Encoder;

public class MD5 {

    public static void main(String[] args) {

        String s = DigestUtils.md5Hex("123456");
        System.out.println(s);

    }

    public static String encodeByMd5(String str) {
        try {
            // 确定计算方法
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            // 加密后的字符串
            String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
            return newstr;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断用户密码是否正确
     * newpasswd 用户输入的密码
     * oldpasswd 正确密码
     */
    public boolean checkpassword(String newpasswd, String oldpasswd)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (encodeByMd5(newpasswd).equals(oldpasswd))
            return true;
        else
            return false;
    }

}
