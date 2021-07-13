package com.jq.dbapi.util;

import org.apache.commons.codec.digest.DigestUtils;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class MD5 {

    public static void main(String[] args) {

        String s = DigestUtils.md5Hex("123456");
        System.out.println(s);

    }

    public static String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密后的字符串
        String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

    /**
     * 判断用户密码是否正确
     * newpasswd 用户输入的密码
     * oldpasswd 正确密码
     */
    public boolean checkpassword(String newpasswd, String oldpasswd) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (EncoderByMd5(newpasswd).equals(oldpasswd))
            return true;
        else
            return false;
    }

}
