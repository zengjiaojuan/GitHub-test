package com.cddgg.p2p.pay.util;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

/**
 * 用公钥对环迅返回信息进行验证
 * 
 * @author RanQiBing 2014-03-26
 * 
 */
public class SignProvider {
	/**
	 * 验证是否为环迅返回的信息
	 * 
	 * @param pubKeyText
	 *            公钥
	 * @param plainText
	 *            明文
	 * @param signText
	 *            数字签名的密文
	 * @return <p>
	 *         true
	 *         </p>
	 *         是
	 *         <p>
	 *         true
	 *         </p>
	 *         否
	 */
	public static boolean verify(String pubKeyText, String plainText,
			String signText) {

		try {

			// 解密由base64编码的公钥,并构造X509EncodedKeySpec对象

			X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(Base64
					.decodeBase64String(pubKeyText).getBytes());

			// RSA对称加密算法

			KeyFactory keyFactory = KeyFactory.getInstance("RSA");

			// 取公钥匙对象
			PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);

			// 解密由base64编码的数字签名

			byte[] signed = Base64.decodeBase64String(signText).getBytes();

			Signature signatureChecker = Signature.getInstance("MD5withRSA");

			signatureChecker.initVerify(pubKey);

			signatureChecker.update(plainText.getBytes());

			// 验证签名是否正常
			if (signatureChecker.verify(signed)) {
				return true;
			} else {
				return false;
			}
		} catch (Throwable e) {

			System.out.println("校验签名失败");

			e.printStackTrace();

			return false;

		}

	}
}
