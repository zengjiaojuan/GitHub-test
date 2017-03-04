package com.cddgg.p2p.pay.util;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.cddgg.p2p.pay.entity.ReturnInfo;
import com.ips.security.utility.IpsCrypto;

/**
 * 获取ips资金托管参数
 * @author RanQiBing
 * 2014-04-18
 *
 */
public class ParameterIpsUrl {
	
	private static Properties pro = null;
	static{
		try {
			 pro = PropertiesLoaderUtils.loadAllProperties("config/context/pay/payurl.properties");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	public static String getRegistrationurl() {
		return pro.getProperty("REGISTRATIONTESTURL");
	}
	public static String getRechargeurl() {
		return pro.getProperty("RECHARGETESTURL");
	}
	public static String getBidurl() {
		return pro.getProperty("BIDTESTURL");
	}
	public static String getAutomaticbidurl() {
		return pro.getProperty("AUTOMATICBIDTESTURL");
	}
	public static String getRepaymenturl() {
		return pro.getProperty("REPAYMENTTESTURL");
	}
	public static String getWithdrawalurl() {
		return pro.getProperty("WITHDRAWALTESTURL");
	}
	public static String getBalabceinquiryurl() {
		return pro.getProperty("BALABCEINQUIRYTESTURL");
	}
	public static String getBanklistqeryurl() {
		return pro.getProperty("BANKLISTQUERYTESTURL");
	}
	
}
