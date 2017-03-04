package com.cddgg.p2p.pay.entity;

import com.cddgg.base.util.StringUtil;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.huitou.constant.Constant;

/**
 * 提现
 * @author RanQiBing 2014-01-03
 *
 */
public class WithdrawalInfo {
	
    /**商户提现订单号(TX+当前时间精确到秒+四位随机数)*/
    private String pMerBillNo = "TX"+StringUtil.pMerBillNo();   
    /**账户类型 */
    private String pAcctType = Constant.STATUES_ONE.toString();   
    /**提现模式 */
    private String pOutType = Constant.STATUES_ONE.toString();   
    /**标号 */
    private String pBidNo = "";     
    /**合同号 */
    private String pContractNo =""; 
    /**提现去向 */
    private String pDwTo =""; 
    /**证件号码 */
    private String pIdentNo; 
    /**姓名 */
    private String pRealName;  
    /**IPS账户号 */
    private String pIpsAcctNo;    
    /**提现日期 */
    private String pDwDate = DateUtils.format("yyyyMMdd");   
    /**提现金额 */
    private String pTrdAmt;  
    /**平台手续费 */
    private String pMerFee = Constant.IPS_WITHDRAW;   
    /**IPS 手续费收取方 */
    private String pIpsFeeType = Constant.STATUES_ONE.toString();  
    /**状态返回地址1 */
    private String pWebUrl = Constant.WITHDRAWAL;  
    /**状态返回地址2*/
    private String pS2SUrl = Constant.WITHDRAWASYNCHRONOUS; 
    /**备注 */
    private String pMemo1 = "";  
    /**备注 */
    private String pMemo2 = "";  
    /**备注 */
    private String pMemo3 = "";  
    /**IPS提现订单号 */
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
     * @return pOutType
     */
    public String getpOutType() {
    	return pOutType;
    }
    /**
     * @param pOutType pOutType
     */
    public void setpOutType(String pOutType) {
    	this.pOutType = pOutType;
    }
    /**
     * @return pBidNo
     */
    public String getpBidNo() {
    	return pBidNo;
    }
    /**
     * @param pBidNo pBidNo
     */
    public void setpBidNo(String pBidNo) {
    	this.pBidNo = pBidNo;
    }
    /**
     * @return pContractNo
     */
    public String getpContractNo() {
    	return pContractNo;
    }
    /**
     * @param pContractNo pContractNo
     */
    public void setpContractNo(String pContractNo) {
    	this.pContractNo = pContractNo;
    }
    /**
     * @return pDwTo
     */
    public String getpDwTo() {
    	return pDwTo;
    }
    /**
     * @param pDwTo pDwTo
     */
    public void setpDwTo(String pDwTo) {
    	this.pDwTo = pDwTo;
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
     * 
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
     * @return pDwDate
     */
    public String getpDwDate() {
    	return pDwDate;
    }
    /**
     * @param pDwDate pDwDate
     */
    public void setpDwDate(String pDwDate) {
    	this.pDwDate = pDwDate;
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
     * @return pIpsFeeType
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
