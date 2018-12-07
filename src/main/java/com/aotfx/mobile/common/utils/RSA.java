package com.aotfx.mobile.common.utils;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author kongqz
 */

/**
 * @Description 非对称加密算法RSA算法组件
 * 非对称算法一般是用来传送对称加密算法的密钥来使用的，相对于DH算法，RSA算法只需要一方构造密钥，不需要
 * 大费周章的构造各自本地的密钥对了。DH算法只能算法非对称算法的底层实现。而RSA算法算法实现起来较为简单
 * @auther xiutao li
 * @email hipilee@gamil.com leexiutao@foxmail.com
 * @create 2018-12-04 23:36
 */
public class RSA {
    //非对称密钥算法
    public static final String KEY_ALGORITHM = "RSA";

    private static byte[] privateKey = Base64.decodeBase64("MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAlOE94jgH/BDbHsFQQm766LudECrEgVbwgsVwviz2ts2eJENeNw1hifW/advkgfVXPKAoDmvoiEQQqPkR86YWnQIDAQABAkBr0/8oGwDdda/eRJDGrO6ZJVkKXo9YRgmIDUws0RAXMQ728QUBKaiJTCfZxYwXaX1/QGWrmQ4XmB43wK4e/z7ZAiEA9Y0P0ZkJx9oXT7jKfnud1LY9rhIqkb10gz2VGUsifmMCIQCbNxTZyLnHK1GOZBNy4Jc7i27zoy+QdEotIKqaZTKm/wIgEh+ALGMUMrjpAYxM/brX8kwlD0Tt+WW3RLx11bES8asCIBMiTCi4zOYXmfsyrQ8ZTqthefEA6ekl3MsJXt6foQnjAiEAgatyJca/plGiXgNK5HzOn2iHTaigNI9C7pZ42rxUi3I=");

    private static byte[] publicKey = Base64.decodeBase64("MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJThPeI4B/wQ2x7BUEJu+ui7nRAqxIFW8ILFcL4s9rbNniRDXjcNYYn1v2nb5IH1VzygKA5r6IhEEKj5EfOmFp0CAwEAAQ==");

    /**
     * 密钥长度，DH算法的默认密钥长度是1024
     * 密钥长度必须是64的倍数，在512到65536位之间
     */
    private static final int KEY_SIZE = 512;
    //公钥
    private static final String PUBLIC_KEY = "RSAPublicKey";

    //私钥
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 初始化密钥对
     *
     * @return Map 甲方密钥的Map
     */
    public static Map<String, Object> initKey() throws Exception {
        //实例化密钥生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        SecureRandom secureRandom = new SecureRandom(new Date().toString().getBytes());
        //初始化密钥生成器
        keyPairGenerator.initialize(KEY_SIZE, secureRandom);
        //生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //甲方公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        //甲方私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //将密钥存储在map中
        Map<String, Object> keyMap = new HashMap<String, Object>();
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;

    }


    /**
     * 私钥加密
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return byte[] 加密数据
     */
    public static byte[] encryptByPrivateKey(byte[] data, byte[] key) throws Exception {

        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        //数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥加密
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return byte[] 加密数据
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {

        //实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //初始化公钥
        //密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        //产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

        //数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    /**
     * 私钥解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[] 解密数据
     */
    public static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        //数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[] 解密数据
     */
    public static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {

        //实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //初始化公钥
        //密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        //产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        //数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    /**
     * 取得私钥
     *
     * @param keyMap 密钥map
     * @return byte[] 私钥
     */
    public static byte[] getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return key.getEncoded();
    }

    /**
     * 取得公钥
     *
     * @param keyMap 密钥map
     * @return byte[] 公钥
     */
    public static byte[] getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return key.getEncoded();
    }

    public static byte[] getAotfxPrivateKey() {
        return privateKey;
    }

    public static byte[] getAotfxPublicKey() {
        return publicKey;
    }


    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //初始化密钥
        //生成密钥对
        Map<String, Object> keyMap = RSA.initKey();
        //公钥
        byte[] publicKey = RSA.getPublicKey(keyMap);
        publicKey = Base64.decodeBase64("MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJThPeI4B/wQ2x7BUEJu+ui7nRAqxIFW8ILFcL4s9rbNniRDXjcNYYn1v2nb5IH1VzygKA5r6IhEEKj5EfOmFp0CAwEAAQ==");

        //私钥
        byte[] privateKey = RSA.getPrivateKey(keyMap);
        privateKey = Base64.decodeBase64("MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAlOE94jgH/BDbHsFQQm766LudECrEgVbwgsVwviz2ts2eJENeNw1hifW/advkgfVXPKAoDmvoiEQQqPkR86YWnQIDAQABAkBr0/8oGwDdda/eRJDGrO6ZJVkKXo9YRgmIDUws0RAXMQ728QUBKaiJTCfZxYwXaX1/QGWrmQ4XmB43wK4e/z7ZAiEA9Y0P0ZkJx9oXT7jKfnud1LY9rhIqkb10gz2VGUsifmMCIQCbNxTZyLnHK1GOZBNy4Jc7i27zoy+QdEotIKqaZTKm/wIgEh+ALGMUMrjpAYxM/brX8kwlD0Tt+WW3RLx11bES8asCIBMiTCi4zOYXmfsyrQ8ZTqthefEA6ekl3MsJXt6foQnjAiEAgatyJca/plGiXgNK5HzOn2iHTaigNI9C7pZ42rxUi3I=");
//        System.out.println("公钥：\n" + Base64.encodeBase64String(publicKey));
//
//        System.out.println("私钥：\n" + Base64.encodeBase64String(privateKey));

        System.out.println("================密钥对构造完毕,甲方将公钥公布给乙方，开始进行加密数据的传输=============");
        String str = "15708470013";
        System.out.println("\n===========甲方向乙方发送加密数据==============");
        System.out.println("原文:" + str);
        //甲方进行数据的加密
        byte[] code1 = RSA.encryptByPrivateKey(str.getBytes(), privateKey);
        System.out.println("加密后的数据：" + Base64.encodeBase64String(code1));
        System.out.println("===========乙方使用甲方提供的公钥对数据进行解密==============");
        //乙方进行数据的解密
        byte[] decode1 = RSA.decryptByPublicKey(code1, publicKey);
        System.out.println("乙方解密后的数据：" + new String(decode1) + "\n\n");

        System.out.println("===========反向进行操作，乙方向甲方发送数据==============\n\n");

        str = "18200285504";

        System.out.println("原文:" + str);

        //乙方使用公钥对数据进行加密
        byte[] code2 = RSA.encryptByPublicKey(str.getBytes(), publicKey);
        System.out.println("===========乙方使用公钥对数据进行加密==============");
        System.out.println("加密后的数据：" + Base64.encodeBase64String(code2));

        System.out.println("=============乙方将数据传送给甲方======================");
        System.out.println("===========甲方使用私钥对数据进行解密==============");

        //甲方使用私钥对数据进行解密
        byte[] decode2 = RSA.decryptByPrivateKey(code2, privateKey);

        System.out.println("甲方解密后的数据：" + new String(decode2));
    }
}
