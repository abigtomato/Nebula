package org.abigtomato.nebula.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author abigtomato
 */
public class Md5Util {

    /**
     * MD5加密
     *
     * @param input 字符串
     * @return 32位长度
     */
    public static String md5(String input) {
        return DigestUtils.md5Hex(input);
    }

    /**
     * MD5加密
     *
     * @param input 字节数组
     * @return 32位长度
     */
    public static String md5(byte[] input) {
        return DigestUtils.md5Hex(input);
    }

    /**
     * MD5带盐加密
     *
     * @param input
     * @param salt  盐值
     * @return 32位长度
     */
    public static String md5(String input, String salt) {
        return DigestUtils.md5Hex(input + salt);
    }
}
