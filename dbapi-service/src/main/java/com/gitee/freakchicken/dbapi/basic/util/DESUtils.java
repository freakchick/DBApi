package com.gitee.freakchicken.dbapi.basic.util;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;

public class DESUtils {

    private final static String ENCODE = "UTF-8";

    public static void main(String[] args) {
        String pass = "root123456";


        try {
            String s = Base64.encodeBase64String(pass.getBytes(ENCODE));
            System.out.println(s);

            byte[] bytes = Base64.decodeBase64(s);
            String s1 = new String(bytes, ENCODE);
            System.out.println(s1);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * Description 根据键值进行加密
     *
     * @param data 待加密数据
     * @return
     * @throws Exception
     */
    public static String encrypt(String data) throws Exception {
        return Base64.encodeBase64String(data.getBytes(ENCODE));
    }

    /**
     * 根据键值进行解密
     *
     * @param data 待解密数据
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt(String data) throws Exception {
        if (data == null)
            return null;
        byte[] bytes = Base64.decodeBase64(data);
        return new String(bytes, ENCODE);
    }

}