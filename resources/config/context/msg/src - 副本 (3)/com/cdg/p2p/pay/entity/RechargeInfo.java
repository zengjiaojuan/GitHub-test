package com.cddgg.p2p.pay.entity;

import com.cddgg.base.util.StringUtil;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.huitou.constant.Constant;

/**
 * 环讯充值
 * @author RanQiBing 2014-01-03
 *
 */
public class RechargeInfo {
    
    /**商户充值订单号  必填  商户系统唯一 */
	private String pMerBillNo = "CZ"+StringUtil.pMerBillNo(); 
	/**账户类型  必填  0#非托管（目前未开放） ；1#托管*/
	private String pAcctType = Constant.STATUES_ONE.toString();   
	/**证件号码  必填  真实身份证（个人）/企业机构号（商户）*/
	private String pIdentNo;    
	/**姓名  必填  真实姓名*/
	private String pRealName;   
	/**IPS账户号  必填  账户类型为1时，IPS 账号（个人/商户） */
	private String pIpsAcctNo; 
	/**充值日期  必填  格式：YYYYMMDD */
	private String pTrdDate = DateUtils.format("yyyyMMdd");  
	/**充值金额  必填  金额单位：元，丌能为负，丌允许为 0，保留 2*/
	private String pTrdAmt;   
	/**充值渠道种类  必填  1#网银充值；2#代扣充值 */
	private String PChannelType;  
	/**充值银行  可选  网银充值的银行列表由 IPS 提供，对应充值银行*/
	private String pTrdBnkCode;   
	/**平台手续费  必填  这里是平台收取的费用 金额单位：元，丌能为负，允许为 0，保留 2 位小数； 格式：12.00 */
	private String pMerFee = Constant.STATUES_ZERO.toString();  
	/**谁付 IPS手续费  必填  这里是IPS 收取的费用 1：平台支付 2：用户支付 */
	private String pIpsFeeType = Constant.STATUES_ONE.toString(); 
	/**状态返回地址1  必填  浏览器返回地址 */
	private String pWebUrl = Constant.RECHARGEURL;    	
	/**状态返回地址2  必填  S2S 返回地*/
	private String pS2SUrl = Constant.ASYNCHRONISMRECHARGE; 
	/**备注 */
	private String pMemo1 = ""; 
	/**备注 */
	private String pMemo2 = "";  
	/**备注 */
	private String pMemo3 = ""; 
	/**充值状态  必填  0000 成功； 状态1111：充值处理中；9999 失败；*/
	private String pErrCode; 
	/**IPS充值订单号(由IPS系统生成的唯一流水号)*/
	private String pIpsBillNo; 
	
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
	 * @return pAcctType
	 */
	public String getpAcctType() {
		return pAcctType;
	}
	/**
	 * @param pAcctType pAcctType
	 */
	public void setpAcctType(String pAcctType) {
		this.pAcctType = pAcctType;
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
	 * @return pTrdDate
	 */
	public String getpTrdDate() {
		return pTrdDate;
	}
	/**
	 * @param pTrdDate pTrdDate
	 */
	public void setpTrdDate(String pTrdDate) {
		this.pTrdDate = pTrdDate;
	}
	/**
	 * @return pTrdAmt
	 */
	public String getpTrdAmt() {
		return pTrdAmt;
	}
	/**
	 * @param pTrdAmt pTrdAmt
	 */
	public void setpTrdAmt(String pTrdAmt) {
		this.pTrdAmt = pTrdAmt;
	}
	/**
	 * @return pChannelType
	 */
	public String getPChannelType() {
		return PChannelType;
	}
	/**
	 * @param pChannelType pChannelType
	 */
	public void setPChannelType(String PChannelType) {
	    this.PChannelType = PChannelType;
	}
	/**
	 * @return pTrdBnkCode
	 */
	public String getpTrdBnkCode() {
		return pTrdBnkCode;
	}
	/**
	 * @param pTrdBnkCode pTrdBnkCode
	 */
	public void setpTrdBnkCode(String pTrdBnkCode) {
		this.pTrdBnkCode = pTrdBnkCode;
	}
	/**
	 * @return pMerFee
	 */
	public String getpMerFee() {
		return pMerFee;
	}
	/**
	 * @param pMerFee pMerFee
	 */
	public void setpMerFee(String pMerFee) {
		this.pMerFee = pMerFee;
	}
	/**
	 * @return  pIpsFeeType
	 */
	public String getpIpsFeeType() {
		return pIpsFeeType;
	}
	/**
	 * @param pIpsFeeType pIpsFeeType
	 */
	public void setpIpsFeeType(String pIpsFeeType) {
		this.pIpsFeeType = pIpsFeeType;
	}
	/**
	 * 
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
	 * @return pErrCode
	 */
	public String getpErrCode() {
		return pErrCode;
	}
	/**
	 * @param pErrCode pErrCode
	 */
	public void setpErrCode(String pErrCode) {
		this.pErrCode = pErrCode;
	}
	/**
	 * @return pIpsBillNo
	 */
	public String getpIpsBillNo() {
		return pIpsBillNo;
	}
	/**
	 * @param pIpsBillNo pIpsBillNo
	 */
	public void setpIpsBillNo(String pIpsBillNo) {
		this.pIpsBillNo = pIpsBillNo;
	}


	
	
}
