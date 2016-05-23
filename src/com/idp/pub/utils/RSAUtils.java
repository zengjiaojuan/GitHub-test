package com.idp.pub.utils;

import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class RSAUtils {
	private final static String KEY_TYPE = "RSA";
	private final static PublicKey publicKey = getPubKey();
	private final static PrivateKey privateKey = getPrivateKey();

	public static PublicKey getPubKey() {
		String pubKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJPd8kRjByoz3miSskvIcgmCOutZJwUqg0KAJa1vyZP/C0EkxL1xYdaY339heq1p5htSouTlOFx9mmnSXWreF9cCAwEAAQ==";
		X509EncodedKeySpec bobPubKeySpec;
		try {
			bobPubKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(pubKey));
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_TYPE);
			return keyFactory.generatePublic(bobPubKeySpec);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static PrivateKey getPrivateKey() {
		String priKey = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAk93yRGMHKjPeaJKyS8hyCYI661knBSqDQoAlrW/Jk/8LQSTEvXFh1pjff2F6rWnmG1Ki5OU4XH2aadJdat4X1wIDAQABAkEAiByrESZzBvceCPbYZwgJaRVW9SNo1smOcB2UETWwwah/rI+ZFIZYX900VDRhJNFvqxsg8iz1qzM5/8I0isDfkQIhAN5H4PYTCkB7neQitC8/HLztiAlNBypatzVQkylI5iqJAiEAqkxCRLY+qPt1ygIHFiKqlaVLaZJxzpLmdVAw/aISF18CIAVuGhfI1UpH+s507pPs5cXTw6v+frtRJgKeaknq5vyJAiEAkikRV1fzP6VS2yYSXvUx424S43FFLD74fQduhMFtNZECIC1VK1lx3BIMKiskDoEAAApAvMuYMqwIj3tUgMIa5QFH";
		PKCS8EncodedKeySpec priPKCS8;
		try {
			priPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(priKey));
			KeyFactory keyf = KeyFactory.getInstance(KEY_TYPE);
			return keyf.generatePrivate(priPKCS8);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 加密
	 * @param data
	 * @return
	 */
	public static String encrypt(String data) {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
	        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
	        data = Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes("utf-8")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	/**
	 * 解密
	 * @param data
	 * @return
	 */
	public static String decrypt(String data) {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
	        cipher.init(Cipher.DECRYPT_MODE, privateKey);
	        data = new String(cipher.doFinal(Base64.getDecoder().decode(data)), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data.trim();
	}
	
	public static void main(String[] args) throws Exception {
		String str = "phb1225627";
		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
		// 设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        // 对数据进行加密
        byte[] enRsaBytes = cipher.doFinal(str.getBytes("UTF-8"));
        String enRsaStr = new String(enRsaBytes, "utf-8");
		System.out.println("加密后==" + enRsaStr);
		System.out.println(Base64.getEncoder().encode(enRsaBytes));
        //设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(enRsaBytes);
        System.out.println("解密后==" + new String(result, "utf-8"));

        
        str = "打雷了";
        str = "3232445549944";
		cipher = Cipher.getInstance("RSA/ECB/NoPadding");
		// 设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 对数据进行加密
        enRsaBytes = cipher.doFinal(str.getBytes("UTF-8"));
        enRsaStr = new String(enRsaBytes);
		System.out.println("加密后==" + enRsaStr);
		System.out.println(Base64.getEncoder().encodeToString(enRsaBytes));
		System.out.println(URLEncoder.encode(Base64.getEncoder().encodeToString(enRsaBytes), "utf-8"));
        
//        String s = "Q5MIcntAG1thzPDNBCIv0IubVbt9naHluourqxKysLiaaiYpbdmxBSk/liAtACQB1LkZxBtM4UKSQC6Z/trNTQ==";
//        cipher.init(Cipher.DECRYPT_MODE, privateKey);
//        result = cipher.doFinal(Base64.getDecoder().decode(s));
//        System.out.println("解密后==" + new String(result, "utf-8").trim());
		String data = "123456";
        System.out.println(data);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        data = Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes("utf-8")));
        System.out.println(URLEncoder.encode(data, "utf-8"));
        System.out.println(decrypt(data));
	}
}
