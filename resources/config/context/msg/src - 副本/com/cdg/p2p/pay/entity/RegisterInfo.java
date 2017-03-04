package com.cddgg.p2p.pay.entity;

import com.cddgg.base.util.StringUtil;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.huitou.constant.Constant;

/**
 * 注册环讯平台账号对象
 * @author QanQiBing 2014-01-03
 *
 */
public class RegisterInfo {
	
    /**商户开户流水号;(KH+当前时间精确到秒+四位随机数)*/
	private String pMerBillNo = "KH"+StringUtil.pMerBillNo() ;  
	/**证件类型 默认为身份证类型 1 */
	private String pIdentType = Constant.STATUES_ONE.toString(); 
	/**证件号码;*/
	private String pIdentNo;	
	/**开户状态 (状态0000：成功)*/
	private String pStatus;		
	/**IPS账户号*/
	private String pIpsAcctNo;	
	/**IPS开户日期*/
	private String pIpsAcctDate = DateUtils.format("yyyyMMdd"); 
	/**姓名 */
	private String pRealName;	
	/**手机号 */
	private String pMobileNo;	
	/**激活邮件*/
	private String pEmail;		
	/**状态返回地址(即浏览器返回地址) */
	private String pWebUrl = Constant.REGISTRATION;		
	/**状态返回地址(即后台返回地址) */
	private String pS2SUrl = Constant.ASYNCHRONISMREGISTRATION;		
	/**备注1*/
	private String pMemo1 = "";		
	/**备注1*/
	private String pMemo2 = "";		
	/**备注1*/
	private String pMemo3 = "";  	
	
	/**
	 * @return pMerBillNo
	 */
	public String getpMerBillNo() {
		return pMerBillNo;
	}
	/**
	 * @param pMerBillNo pMerBillNo
	 */
	public void setpMerBillNo(String pMerBillNo) {
		this.pMerBillNo = pMerBillNo;
	}
	/**
	 * @return pIdentType
	 */
	public String getpIdentType() {
		return pIdentType;
	}
	/**
	 *  pIdentType
	 */
	public void setpIdentType() {
		this.pIdentType = "1";
	}
	/**
	 * @return pIdentNo
	 */
	public String getpIdentNo() {
		return pIdentNo;
	}
	/**
	 * @param pIdentNo pIdentNo
	 */
	public void setpIdentNo(String pIdentNo) {
		this.pIdentNo = pIdentNo;
	}
	/**
	 * @return pRealName
	 */ 
	public String getpRealName() {
		return pRealName;
	}
	/**
	 * @param pRealName pRealName
	 */
	public void setpRealName(String pRealName) {
		this.pRealName = pRealName;
	}
	/**
	 * @return pMobileNo
	 */
	public String getpMobileNo() {
		return pMobileNo;
	}
	/**
	 * @param pMobileNo pMobileNo
	 */
	public void setpMobileNo(String pMobileNo) {
		this.pMobileNo = pMobileNo;
	}
	/**
	 * @return pEmail
	 */
	public String getpEmail() {
		return pEmail;
	}
	/**
	 * @param pEmail pEmail
	 */
	public void setpEmail(String pEmail) {
		this.pEmail = pEmail;
	}
	/**
	 * @return pWebUrl
	 */
	public String getpWebUrl() {
		return pWebUrl;
	}
	/**
	 * @param pWebUrl pWebUrl
	 */
	public void setpWebUrl(String pWebUrl) {
		this.pWebUrl = pWebUrl;
	}
	/**
	 * @return pS2SUrl
	 */ 
	public String getpS2SUrl() {
		return pS2SUrl;
	}
	/**
	 * @param pS2SUrl pS2SUrl
	 */
	public void setpS2SUrl(String pS2SUrl) {
		this.pS2SUrl = pS2SUrl;
	}
	/**
	 * @return pMemo1
	 */
	public String getpMemo1() {
		return pMemo1;
	}
	/**
	 * @param pMemo1 pMemo1
	 */
	public void setpMemo1(String pMemo1) {
		this.pMemo1 = pMemo1;
	}
	/**
	 * @return pMemo2
	 */
	public String getpMemo2() {
		return pMemo2;
	}
	/**
	 * @param pMemo2 pMemo2
	 */
	public void setpMemo2(String pMemo2) {
		this.pMemo2 = pMemo2;
	}
	/**
	 * @return pMemo3
	 */
	public String getpMemo3() {
		return pMemo3;
	}
	/**
	 * @param pMemo3 pMemo3
	 */
	public void setpMemo3(String pMemo3) {
		this.pMemo3 = pMemo3;
	}  
	/**
	 * @return pStatus
	 */
	public String getpStatus() {
		return pStatus;
	}
	/**
	 * @param pStatus pStatus
	 */
	public void setpStatus(String pStatus) {
		this.pStatus = pStatus;
	}
	/**
	 * @return pIpsAcctNo
	 */
	public String getpIpsAcctNo() {
		return pIpsAcctNo;
	}
	/**
	 * @param pIpsAcctNo pIpsAcctNo
	 */
	public void setpIpsAcctNo(String pIpsAcctNo) {
		this.pIpsAcctNo = pIpsAcctNo;
	}
	/**
	 * @return pIpsAcctDate
	 */
	public String getpIpsAcctDate() {
		return pIpsAcctDate;
	}
	/**
	 * @param pIpsAcctDate pIdentType
	 */
	public void setpIpsAcctDate(String pIpsAcctDate) {
		this.pIpsAcctDate = pIpsAcctDate;
	}
	/**
	 * @param pIdentType pIdentType
	 */
	public void setpIdentType(String pIdentType) {
		this.pIdentType = pIdentType;
	}
	
}
