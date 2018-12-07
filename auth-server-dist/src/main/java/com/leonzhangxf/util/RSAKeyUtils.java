package com.leonzhangxf.util;

import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA公私钥处理工具类
 *
 * @author leonzhangxf 20171215
 */
public class RSAKeyUtils {

    private static final String KEY_ALGORITHM = "RSA";

    public static final String PUBLIC_KEY = "RSAPublicKey";

    public static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 生成RSA2私钥公钥并保存在一个map中
     * {@link RSAKeyUtils#PRIVATE_KEY} 私钥
     * {@link RSAKeyUtils#PUBLIC_KEY} 公钥
     */
    public static Map<String, Key> initKeyPair() throws Exception {
        // 获得对象 KeyPairGenerator 参数 RSA 2048个字节
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(2048);
        // 通过对象 KeyPairGenerator 获取对象KeyPair
        KeyPair keyPair = keyPairGen.generateKeyPair();
        return transRSAKeyPair(keyPair);
    }

    public static String initKeyPairFormatString() throws Exception {
        return transRSAKeyMapToFormatString(initKeyPair());
    }

    public static Map<String, Key> transRSAKeyPair(KeyPair keyPair) {
        // 通过对象 KeyPair 获取RSA公私钥对象RSAPublicKey RSAPrivateKey
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 公私钥对象存入map中
        Map<String, Key> keyMap = new HashMap<>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    public static String transRSAKeyPairToFormatString(KeyPair keyPair) {
        return transRSAKeyMapToFormatString(transRSAKeyPair(keyPair));
    }

    public static String transRSAKeyMapToFormatString(Map<String, Key> keyMap) {
        return String.format("%s: %s \n%s: %s", PRIVATE_KEY, base64Encode(keyMap.get(PRIVATE_KEY).getEncoded()),
                PUBLIC_KEY, base64Encode(keyMap.get(PUBLIC_KEY).getEncoded()));
    }

    /**
     * 从{@link RSAKeyUtils#initKeyPair()}生成的map中获得公钥
     */
    public static String getPublicKey(Map<String, Key> keyMap) {
        //获得map中的公钥对象 转为key对象
        Key key = keyMap.get(PUBLIC_KEY);
        //编码返回字符串
        return base64Encode(key.getEncoded());
    }

    /**
     * 从{@link RSAKeyUtils#initKeyPair()}生成的map中获得私钥
     */
    public static String getPrivateKey(Map<String, Key> keyMap) {
        //获得map中的私钥对象 转为key对象
        Key key = keyMap.get(PRIVATE_KEY);
        //编码返回字符串
        return base64Encode(key.getEncoded());
    }

    /**
     * 对Key进行base64 解码
     */
    public static byte[] base64Decode(String key) {
        return Base64Utils.decodeBase64(key.getBytes());
    }

    /**
     * 对Key进行base64 编码
     */
    public static String base64Encode(byte[] key) {
        return new String(Base64Utils.encodeBase64(key));
    }

    public static KeyPair generateKeyPair(String privateKey, String publicKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        PrivateKey privateKeyValue = getPrivateKey(privateKey);
        PublicKey publicKeyValue = getPublicKey(publicKey);
        return new KeyPair(publicKeyValue, privateKeyValue);
    }

    private static PrivateKey getPrivateKey(String privateKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(base64Decode(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }

    private static PublicKey getPublicKey(String publicKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(base64Decode(publicKey));
        // RSA对称加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取公钥匙对象
        return keyFactory.generatePublic(bobPubKeySpec);
    }

    public static void main(String[] args) throws Exception {
        // 生成并打印公私钥
        Map<String, Key> keyMap = initKeyPair();
        String privateKey = getPrivateKey(keyMap);
        System.out.println(String.format("RSAPrivateKey：%s", privateKey));
        String publicKey = getPublicKey(keyMap);
        System.out.println(String.format("RSAPublicKey：%s", publicKey));

        //根据公私钥反向生成KeyPair
        KeyPair keyPair = generateKeyPair(privateKey, publicKey);
        System.out.println(transRSAKeyPairToFormatString(keyPair));
    }
}
