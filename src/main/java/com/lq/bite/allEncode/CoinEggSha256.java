package com.lq.bite.allEncode;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.lq.bite.common.Md5EnCode;
/**
 * 币蛋 256加密方式
 * @author l.q
 *
 */
public class CoinEggSha256 {

	public static String personSign(String publicKey, String privateKey) {
		String encryptKey;
		try {
			encryptKey = Md5EnCode.getMessageDigest(privateKey);
			System.out.println(" encryptKey : " + encryptKey);
			StringBuffer sb = new StringBuffer();
			sb.append("key=");
			sb.append(publicKey);
			sb.append("&nonce=");
			sb.append(123456);
			return sha256_HMAC(sb.toString(), encryptKey);
			//String str = sha256_HMAC("key=41axh-7sdgq-xtsw2-i2dwd-8cu4e-tvy52-883i6&nonce=1234567", encryptKey);
			//System.out.println(" getSignature : " + str);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static String commonSign(String publicKey, String privateKey,String param){
		String encryptKey;
		try {
			encryptKey = Md5EnCode.getMessageDigest(privateKey);
			System.out.println(" encryptKey : " + encryptKey);
			StringBuffer sb = new StringBuffer();
			sb.append("key=");
			sb.append(publicKey);
			sb.append("&nonce=");
			sb.append(123456);
			sb.append(param);
			return sha256_HMAC(sb.toString(), encryptKey);
			//String str = sha256_HMAC("key=41axh-7sdgq-xtsw2-i2dwd-8cu4e-tvy52-883i6&nonce=1234567", encryptKey);
			//System.out.println(" getSignature : " + str);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args) {
		String encryptKey;
		try {
			encryptKey = Md5EnCode.getMessageDigest("3~}k4-iAacs-kf5!^-)hMXj-78urd-~/%9~-)c$,y");
			System.out.println(" encryptKey : " + encryptKey);
			String str = sha256_HMAC("key=ngnbx-ut82d-63xw5-2rqqw-9ps1h-d483n-iq462&nonce=123456", encryptKey);
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
