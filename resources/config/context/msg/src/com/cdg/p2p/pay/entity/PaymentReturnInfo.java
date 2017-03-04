package com.cddgg.p2p.pay.entity;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.cddgg.p2p.pay.util.ParameterIps;
import com.ips.security.utility.IpsCrypto;

/**
 * 普通支付网关支付返回信息
 * @author RanQiBing
 * 2014-04-17
 *
 */
public class PaymentReturnInfo {
	/**
	 * 订单编号 
	 */
	private String billno;
	/**
	 * 商户编号 
	 */
	private String mercode;  
	/**
	 *  币种
	 */
	private String Currency_type;
	/**
	 * 订单金额
	 */
	private String amount; 
	/**
	 * 订单日期
	 */
	private String date;
	/**
	 * 成功标志
	 */
	private String succ; 
	/**
	 * 发卡行的返回信息 
	 */
	private String msg;
	/**
	 * 商户数据包 
	 */
	private String attach;
	/**
	 * IPS 订单号 
	 */
	private String ipsbillno;
	/**
	 * 交易返回签名方式
	 */
	private String retencodetype;
	/**
	 * 数字签名摘要信息 
	 */
	private String signature;  
	/**
	 * 银行订单号
	 */
	private String bankbillno;
	/**
	 * 加密验证
	 */
	private String signmd5;
	
	public String getBillno() {
		return billno;
	}
	public void setBillno(String billno) {
		this.billno = billno;
	}
	public String getMercode() {
		return mercode;
	}
	public void setMercode(String mercode) {
		this.mercode = mercode;
	}
	public String getCurrency_type() {
		return Currency_type;
	}
	public void setCurrency_type(String currency_type) {
		Currency_type = currency_type;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSucc() {
		return succ;
	}
	public void setSucc(String succ) {
		this.succ = succ;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getIpsbillno() {
		return ipsbillno;
	}
	public void setIpsbillno(String ipsbillno) {
		this.ipsbillno = ipsbillno;
	}
	public String getRetencodetype() {
		return retencodetype;
	}
	public void setRetencodetype(String retencodetype) {
		this.retencodetype = retencodetype;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getBankbillno() {
		return bankbillno;
	}
	public void setBankbillno(String bankbillno) {
		this.bankbillno = bankbillno;
	}
	public String getSignmd5() {
			//SignMD5明文=billno+【订单编号】+currencytype+【币种】+amount+【订单金额】+date+【订单日期】+succ+【成功标志】+ipsbillno+【IPS订单编号】+retencodetype +【交易返回签名方式】+【商户内部证书】 
			String sign = "billno"+this.billno+"currencytype"+this.Currency_type+"amount"+this.amount+"date"+this.date+"succ"+this.succ+"ipsbillno"+this.ipsbillno+"retencodetype"+this.retencodetype+ParameterIps.getCertificatem();
			return IpsCrypto.md5Sign(sign).toLowerCase();
	}
	  
	 
	
}
