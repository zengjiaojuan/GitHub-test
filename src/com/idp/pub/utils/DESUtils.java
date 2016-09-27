package com.idp.pub.utils;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DESUtils {
    private final static String ALGORITHM = "DES";
    private final static SecretKey PUBLIC_KEY = getSecretKey();
    private static byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };

    public static SecretKey getSecretKey() {
		String pubKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJPd8kRjByoz3miSskvIcgmCOutZJwUqg0KAJa1vyZP/C0EkxL1xYdaY339heq1p5htSouTlOFx9mmnSXWreF9cCAwEAAQ==";
        DESKeySpec dks;
		try {
			dks = new DESKeySpec(pubKey.getBytes());
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
	        return keyFactory.generateSecret(dks);
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
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		try {
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
	        cipher.init(Cipher.ENCRYPT_MODE, PUBLIC_KEY, zeroIv);
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
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		try {
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
	        cipher.init(Cipher.DECRYPT_MODE, PUBLIC_KEY, zeroIv);
	        data = new String(cipher.doFinal(Base64.getDecoder().decode(data)), "utf-8");
		} catch (Exception e) {
			data="";
			e.printStackTrace();
		}
		return data.trim();
	}

    public static void main(String[] args) throws Exception {
		String data = "zhu5886";
        System.err.println(decrypt(data));
        System.err.println(decrypt(encrypt(data)));
        String clinicName = "D3IZpQhAR5PkVksv41SQBA==";
        System.err.println(decrypt(clinicName));
        System.err.println(encrypt("3232445549944"));
    }
}
