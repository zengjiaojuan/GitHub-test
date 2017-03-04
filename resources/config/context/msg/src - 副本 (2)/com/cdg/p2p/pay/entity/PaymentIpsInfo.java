package com.cddgg.p2p.pay.entity;


import com.cddgg.base.util.StringUtil;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.pay.util.ParameterIps;
import com.ips.security.utility.IpsCrypto;





/**
 * 环迅普通网管支付参数
 * 
 * @author RanQiBing 2014-04-16
 * 
 */
public class PaymentIpsInfo {

	/**
	 * 商户编号(说明：IPS支付平台分配给商户的唯一标识 )
	 */
	private String mer_code;
	/**
	 * 商户订单编号
	 */
	private String billno = "VIP" + StringUtil.pMerBillNo();
	/**
	 * 订单金额(规则：小数点后保留2位,若超过2位则在IPS平台上自动四舍五入 ,最大不超过10位)
	 */
	private String amount;
	/**
	 * 订单日期(YYYYMMDD)
	 */
	private String date = DateUtils.format("yyyyMMdd");
	/**
	 * 币种
	 */
	private String currency_Type = "RMB";
	/**
	 * 支付卡种(默认为01)
	 */
	private String gateway_Type = "01";
	/**
	 * 语言
	 */
	private String lang = "GB";
	/**
	 * 支付结果成功返回的商户URL
	 */
	private String merchanturl;
	/**
	 * 支付结果失败返回的商户URL
	 */
	private String failUrl="";
	/**
	 * 商户数据包(存放商户自己的信息，随订单传送到IPS平台，当订单返回的时候原封不动的返回给商户 规则：数字、字母或数字+字母)
	 */
	private String attach="";
	/**
	 * 订单支付接口加密方式(长度为1)
	 */
	private String orderEncodeType = "5";
	/**
	 * 交易返回接口加密方式 (长度为2)
	 */
	private String retEncodeType="17"; 
	/**
	 * 返回方式(长度为1)
	 */
	private String rettype = "1";
	/**
	 * Server  to Server 返回页面 
	 */
	private String serverUrl;
	/**
	 * 订单支付接口的Md5摘要 
	 */
	private String signMD5;
	
	public String getMer_code() {
		return ParameterIps.getMercode();
	}
	public String getBillno() {
		return billno;
	}
	public void setBillno(String billno) {
		this.billno = billno;
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
	public String getCurrency_Type() {
		return currency_Type;
	}
	public void setCurrency_Type(String currency_Type) {
		this.currency_Type = currency_Type;
	}
	public String getGateway_Type() {
		return gateway_Type;
	}
	public void setGateway_Type(String gateway_Type) {
		this.gateway_Type = gateway_Type;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getMerchanturl() {
		return ParameterIps.getUrl();
	}
	
	public String getFailUrl() {
		return failUrl;
	}
	public void setFailUrl(String failUrl) {
		this.failUrl = failUrl;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getOrderEncodeType() {
		return orderEncodeType;
	}
	public void setOrderEncodeType(String orderEncodeType) {
		this.orderEncodeType = orderEncodeType;
	}
	public String getRetEncodeType() {
		return retEncodeType;
	}
	public void setRetEncodeType(String retEncodeType) {
		this.retEncodeType = retEncodeType;
	}
	public String getRettype() {
		return rettype;
	}
	public void setRettype(String rettype) {
		this.rettype = rettype;
	}
	public String getServerUrl() {
		return ParameterIps.getAsynchronismurl();
	}
	public String getSignMD5() {
			//SignMD5明文=billno+订单编号+currencytype +支付币种+amount +金额(保留2位小数)+ date +日期+orderencodetype +【订单支付接口加密方式】+IPS证书 
			String signmd5 = "billno"+this.billno+"currencytype"+this.currency_Type+"amount"+this.amount+"date"+this.date+"orderencodetype"+this.orderEncodeType+ParameterIps.getCertificatem();
			this.signMD5 = IpsCrypto.md5Sign(signmd5).toLowerCase();
			
		return signMD5;
	}
	
	
	
}
