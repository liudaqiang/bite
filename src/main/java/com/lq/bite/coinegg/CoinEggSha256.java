package com.lq.bite.coinegg;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.lq.bite.common.Md5EnCode;

public class CoinEggSha256 {
	public static final String PUBLIC_KEY = "41axh-7sdgq-xtsw2-i2dwd-8cu4e-tvy52-883i6";
    public static final String PRIVATE_KEY = "LV()e-S{!AZ-2(,x@-gOh4V-O@KL$-N,gSs-vk4qh";
    
    
    public static void main(String[] args) {
        String encryptKey;
        try {
            encryptKey = Md5EnCode.getMessageDigest(PRIVATE_KEY);
            System.out.println(" encryptKey : " + encryptKey);
            String str = sha256_HMAC("key=41axh-7sdgq-xtsw2-i2dwd-8cu4e-tvy52-883i6&nonce=123456", encryptKey);
            System.out.println(" getSignature : " + str);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 将加密后的字节数组转换成字符串
     * 
     * @param b
     *            字节数组
     * @return 字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    /**
     * sha256_HMAC加密
     * 
     * @param message
     *            消息
     * @param secret
     *            秘钥
     * @return 加密后字符串
     */
    private static String sha256_HMAC(String message, String secret) {
        String hash = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
            hash = byteArrayToHexString(bytes);
        } catch (Exception e) {
            System.out.println("Error HmacSHA256 ===========" + e.getMessage());
        }
        return hash;
    }
}
