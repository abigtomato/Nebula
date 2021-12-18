package org.abigtomato.nebula.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * 编码工具
 *
 * @author abigtomato
 */
public class CoderUtil {

    /**
     * 将byte[]转为各种进制的字符串
     *
     * @param bytes byte[]
     * @param radix 可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return 转换后的字符串
     */
    public static String binary(byte[] bytes, int radix) {
        // 1表示正数
        return new BigInteger(1, bytes).toString(radix);
    }

    /**
     * Base64编码
     * <p>
     * - Base64内容传送编码被设计用来把任意序列的8位字节描述为一种不易被人直接识别的形式
     * - Base加密后产生的字节位数是8的倍数，如果不够位数以=符号填充
     *
     * @param data 内容
     * @return 编码
     */
    public static String encryptBase64(byte[] data) {
        return Base64.encodeBase64String(data);
    }

    /**
     * Base64编码
     *
     * @param data 内容
     * @return 编码
     */
    public static String encryptBase64(String data) {
        return Base64.encodeBase64String(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Base64编码
     *
     * @param data 内容
     * @return 编码
     */
    public static byte[] encryptBase64Bytes(byte[] data) {
        return Base64.encodeBase64(data);
    }

    /**
     * Base64编码
     *
     * @param data 内容
     * @return 编码
     */
    public static byte[] encryptBase64Bytes(String data) {
        return Base64.encodeBase64(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Base64解码
     *
     * @param code 编码
     * @return 内容
     */
    public static String decryptBase64(byte[] code) {
        return new String(Base64.decodeBase64(code));
    }

    /**
     * Base64解码
     *
     * @param code 编码
     * @return 内容
     */
    public static String decryptBase64(String code) {
        return new String(Base64.decodeBase64(code));
    }

    /**
     * Base64解码
     *
     * @param code 编码
     * @return 内容
     */
    public static byte[] decryptBase64Bytes(byte[] code) {
        return Base64.decodeBase64(code);
    }

    /**
     * Base64解码
     *
     * @param code 编码
     * @return 内容
     */
    public static byte[] decryptBase64Bytes(String code) {
        return Base64.decodeBase64(code);
    }

    /**
     * Md5编码
     * <p>
     * - Md5信息-摘要算法，广泛用于加密和解密技术，常用于文件校验。不管文件多大，经过Md5后都能生成唯一的Md5值
     *
     * @param data 内容
     * @return 编码
     */
    public static String encryptMd5(byte[] data) {
        return DigestUtils.md5Hex(data);
    }

    /**
     * Md5编码
     *
     * @param data 内容
     * @return 编码
     */
    public static String encryptMd5(String data) {
        return DigestUtils.md5Hex(data);
    }

    /**
     * Md5编码加盐
     *
     * @param data 内容
     * @param salt 盐值
     * @return 编码
     */
    public static String encryptMd5(String data, String salt) {
        return DigestUtils.md5Hex(data + salt);
    }

    /**
     * Md5编码
     *
     * @param data 内容
     * @return 编码
     */
    public static byte[] encryptMd5Bytes(byte[] data) {
        return DigestUtils.md5(data);
    }

    /**
     * Md5编码
     *
     * @param data 内容
     * @return 编码
     */
    public static byte[] encryptMd5Bytes(String data) {
        return DigestUtils.md5(data);
    }

    /**
     * Md5编码加盐
     *
     * @param data 内容
     * @param salt 盐值
     * @return 编码
     */
    public static byte[] encryptMd5Bytes(String data, String salt) {
        return DigestUtils.md5(data + salt);
    }

    /**
     * Sha256编码
     * <p>
     * - Sha安全散列算法，数字签名等密码学应用中重要的工具，被广泛地应用于电子商务等信息安全领域
     *
     * @param data 内容
     * @return 编码
     */
    public static String encryptSha256(byte[] data) {
        return DigestUtils.sha256Hex(data);
    }

    /**
     * Sha256编码
     *
     * @param data 内容
     * @return 编码
     */
    public static String encryptSha256(String data) {
        return DigestUtils.sha256Hex(data);
    }

    /**
     * Sha256编码s
     *
     * @param data 内容
     * @return 编码
     */
    public static byte[] encryptSha256Bytes(byte[] data) {
        return DigestUtils.sha256(data);
    }

    /**
     * Sha256编码
     *
     * @param data 内容
     * @return 编码
     */
    public static byte[] encryptSha256Bytes(String data) {
        return DigestUtils.sha256(data);
    }
}
