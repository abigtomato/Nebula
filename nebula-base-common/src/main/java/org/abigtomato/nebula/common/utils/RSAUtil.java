package org.abigtomato.nebula.common.utils;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA加解密工具类
 *
 * @author abigtomato
 */
public class RSAUtil extends CoderUtil {

    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       加密数据
     * @param privateKey 私钥
     * @return 数字签名
     * @throws Exception e
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        // base64解码私钥
        byte[] keyBytes = decryptBase64Bytes(privateKey);
        // 构造PKCS8编码密钥规范
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        // 指定RSA加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 获取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 指定签名算法，并用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);
        // base64编码签名
        return encryptBase64(signature.sign());
    }

    /**
     * 用私钥对信息生成数字签名
     * <p>
     * - 私钥加密生成数字签名，公钥验证数据及签名。如果数据和签名不匹配则认为验证失败，数字签名的作用就是校验数据在传输过程中不被修改
     *
     * @param data       加密数据
     * @param privateKey 私钥
     * @return 数字签名
     * @throws Exception e
     */
    public static String sign(String data, String privateKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(decryptBase64Bytes(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        return encryptBase64(signature.sign());
    }

    /**
     * 用公钥校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return 成功返回true，失败返回false
     * @throws Exception e
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        // base64解码公钥
        byte[] keyBytes = decryptBase64Bytes(publicKey);
        // 构造X509编码密钥规范
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        // 指定RSA加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 获取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        // 指定签名算法，初始化公钥
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);
        // 验证签名是否正确
        return signature.verify(decryptBase64Bytes(sign));
    }

    /**
     * 用公钥校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return 成功返回true，失败返回false
     * @throws Exception e
     */
    public static boolean verify(String data, String publicKey, String sign) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decryptBase64Bytes(publicKey));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        return signature.verify(decryptBase64Bytes(sign));
    }

    /**
     * RSA私钥加密
     *
     * @param data 明文
     * @param key  私钥
     * @return 密文
     * @throws Exception e
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
        //  base64解码私钥
        byte[] keyBytes = decryptBase64Bytes(key);
        // 获取私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * RSA私钥加密
     *
     * @param data 明文
     * @param key  私钥
     * @return 密文
     * @throws Exception e
     */
    public static String encryptByPrivateKey(String data, String key) throws Exception {
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(decryptBase64Bytes(key));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return encryptBase64(cipher.doFinal(dataBytes));
    }

    /**
     * RSA公钥加密
     *
     * @param data 明文
     * @param key  公钥
     * @return 密文
     * @throws Exception e
     */
    public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
        // base64解码公钥
        byte[] keyBytes = decryptBase64Bytes(key);
        // 获取公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * RSA公钥加密
     *
     * @param data 明文
     * @param key  公钥
     * @return 密文
     * @throws Exception e
     */
    public static String encryptByPublicKey(String data, String key) throws Exception {
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(decryptBase64Bytes(key));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return encryptBase64(cipher.doFinal(dataBytes));
    }

    /**
     * RSA私钥解密
     *
     * @param data 密文
     * @param key  私钥
     * @return 明文
     * @throws Exception e
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
        // base64解码私钥
        byte[] keyBytes = decryptBase64Bytes(key);
        // 获取私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * RSA私钥解密
     *
     * @param data 密文
     * @param key  私钥
     * @return 明文
     * @throws Exception e
     */
    public static String decryptByPrivateKey(String data, String key) throws Exception {
        byte[] dataBytes = decryptBase64Bytes(data);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(decryptBase64Bytes(key));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(dataBytes));
    }

    /**
     * RSA公钥解密
     *
     * @param data 密文
     * @param key  公钥
     * @return 明文
     * @throws Exception e
     */
    public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception {
        // base64解码公钥
        byte[] keyBytes = decryptBase64Bytes(key);
        // 获取公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * RSA公钥解密
     *
     * @param data 密文
     * @param key  公钥
     * @return 明文
     * @throws Exception e
     */
    public static String decryptByPublicKey(String data, String key) throws Exception {
        byte[] dataBytes = decryptBase64Bytes(data);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(decryptBase64Bytes(key));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return new String(cipher.doFinal(dataBytes));
    }

    /**
     * 获取私钥
     *
     * @param keyMap 密钥集合
     * @return 私钥
     */
    public static String getPrivateKey(Map<String, Key> keyMap) {
        Key key = keyMap.get(PRIVATE_KEY);
        return encryptBase64(key.getEncoded());
    }

    /**
     * 获取公钥
     *
     * @param keyMap 密钥集合
     * @return 公钥
     */
    public static String getPublicKey(Map<String, Key> keyMap) {
        Key key = keyMap.get(PUBLIC_KEY);
        return encryptBase64(key.getEncoded());
    }

    /**
     * 初始化密钥
     *
     * @return 密钥集合
     * @throws Exception e
     */
    public static Map<String, Key> initKey() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Key> keyMap = new HashMap<>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }
}
