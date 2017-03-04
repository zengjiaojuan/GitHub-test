package com.cddgg.p2p.pay.entity;

 import java.text.ParseException;

import com.cddgg.base.util.StringUtil;
import com.cddgg.commons.date.DateUtils;
import com.cddgg.p2p.huitou.constant.Constant;
import com.cddgg.p2p.huitou.entity.Loansign;
import com.cddgg.p2p.huitou.entity.Userbasicsinfo;

/**
 * 投标
 * @author RanQiBing 2014-01-03
 *
 */
public class BidInfo {
	
    /**标号 */
	private String pBidNo;
	/**合同号 */
	private String pContractNo; 
	/**借款标类型 */
	private String pBidType="8"; 	
	/**放款模式 */
	private String pOutType="1"; 	
	/**借款用途 */
	private String pUse="资金周转"; 		
	/**投标订单号 */
	private String pMerBillNo; 	
	/**投标日期 */
	private String pBidDate; 	
	/**满标期限 */
	private String pDeadLine="99999"; 	
	/**签约协议号 */
	private String pAgreementNo="";
	/**投资人账户类型 */
	private String pFAcctType="1"; 	
	/**投资人证件类型 */
	private String pFIdentType="1";
	/*** 投资人证件号码*/
	private String pFIdentNo; 	
	/**投资人姓名*/
	private String pFRealName; 	
	/**投资人IPS 账号*/
	private String pFIpsAcctNo; 
	/**借款人账户类型*/
	private String pTAcctType="1"; 		
	/**借款人证件类型*/
	private String pTIdentType="1"; 	
	/**借款人证件号码*/
	private String pTIdentNo; 		
	/**借款人姓名 */
	private String pTRealName; 		
	/**借款人IPS 账号*/
	private String pTIpsAcctNo; 	
	/**投标金额 */
	private String pTrdAmt; 		
	/**投标手续费 */
	private String pFTrdFee; 		
	/**借款手续费  */
	private String pTTrdFee; 		
	/**状态返回地址1*/
	private String pWebUrl = Constant.BID; 		
	/**状态返回地址2 */
	private String pS2SUrl = Constant.ASYNCHRONISMBID; 		
	/**备注 */
	private String pMemo1=""; 			
	/**备注 */
	private String pMemo2=""; 			
	/**备注 */
	private String pMemo3=""; 			
	/**IPS投标订单号 (由 IPS 系统生成的唯一流水号 )*/
	private String pIpsBillNo;		

	public BidInfo(){};
	
	public BidInfo(Userbasicsinfo user,String timePayStart,Double money,Loansign loan) throws ParseException{
	    
	    this.pBidNo = loan.getLoansignbasics().getpBidNo();
	    
	    this.pContractNo = loan.getLoansignbasics().getpContractNo();
	    this.pMerBillNo = "GB"+StringUtil.pMerBillNo()+loan.getId();
	    this.pBidDate = DateUtils.format(DateUtils.DEFAULT_DATE_FORMAT, timePayStart, "yyyyMMdd");
	    
	    this.pFIdentNo=user.getUserrelationinfo().getCardId();
	    this.pFRealName = user.getName();
	    this.pFIpsAcctNo = user.getUserfundinfo().getpIdentNo();
	    
	    this.pTIdentNo = loan.getUserbasicsinfo().getUserrelationinfo().getCardId();
	    this.pTRealName = loan.getUserbasicsinfo().getName();
	    this.pTIpsAcctNo= loan.getUserbasicsinfo().getUserfundinfo().getpIdentNo();
	    
	    this.pTrdAmt = String.valueOf(money);
	    
	    this.pMemo1 = loan.getId().toString();
	    this.pMemo2 = user.getId().toString();
//	    this.pMemo3 = String.valueOf(money);
	    
	    this.pFTrdFee = "0";
	    this.pTTrdFee = "0";
	}
	
	/**
	 * 
	 * @return pBidNo
	 */
	public String getpBidNo() {
		return pBidNo;
	}
	/**
	 * 
	 * @param pBidNo pBidNo
	 */
	public void setpBidNo(String pBidNo) {
		this.pBidNo = pBidNo;
	}
	/**
	 * pContractNo
	 * @return pContractNo
	 */
	public String getpContractNo() {
		return pContractNo;
	}
	
	/**
	 * 
	 * @param pContractNo pContractNo
	 */
	public void setpContractNo(String pContractNo) {
		this.pContractNo = pContractNo;
	}
	/**
	 * 
	 * @return pBidType
	 */
	public String getpBidType() {
		return pBidType;
	}
	/**
	 * 
	 * @param pBidType pBidType
	 */
	public void setpBidType(String pBidType) {
		this.pBidType = pBidType;
	}
	/**
	 * 
	 * @return pOutType
	 */
	public String getpOutType() {
		return pOutType;
	}
	/**
	 * 
	 * @param pOutType pOutType
	 */
	public void setpOutType(String pOutType) {
		this.pOutType = pOutType;
	}
	/**
	 * 
	 * @return pUse
	 */
	public String getpUse() {
		return pUse;
	}
	/**
	 * 
	 * @param pUse pUse
	 */
	public void setpUse(String pUse) {
		this.pUse = pUse;
	}
	/**
	 * 
	 * @return pMerBillNo
	 */
	public String getpMerBillNo() {
		return pMerBillNo;
	}
	/**
	 * 
	 * @param pMerBillNo pMerBillNo
	 */
	public void setpMerBillNo(String pMerBillNo) {
		this.pMerBillNo = pMerBillNo;
	}
	/**
	 * 
	 * @return pBidDate
	 */
	public String getpBidDate() {
		return pBidDate;
	}
	/**
	 * pBidDate
	 * @param pBidDate pBidDate
	 */
	public void setpBidDate(String pBidDate) {
		this.pBidDate = pBidDate;
	}
	/**
	 * 
	 * @return pDeadLine
	 */
	public String getpDeadLine() {
		return pDeadLine;
	}
	/**
	 * 
	 * @param pDeadLine pDeadLine
	 */
	public void setpDeadLine(String pDeadLine) {
		this.pDeadLine = pDeadLine;
	}
	/**
	 * 
	 * @return pAgreementNo
	 */
	public String getpAgreementNo() {
		return pAgreementNo;
	}
	/**
	 * 
	 * @param pAgreementNo pAgreementNo
	 */
	public void setpAgreementNo(String pAgreementNo) {
		this.pAgreementNo = pAgreementNo;
	}
	/**
	 * 
	 * @return pFAcctType
	 */
	public String getpFAcctType() {
		return pFAcctType;
	}
	/**
	 * 
	 * @param pFAcctType pFAcctType
	 */
	public void setpFAcctType(String pFAcctType) {
		this.pFAcctType = pFAcctType;
	}
	/**
	 * 
	 * @return pFIdentType
	 */
	public String getpFIdentType() {
		return pFIdentType;
	}
	/**
	 * 
	 * @param pFIdentType pFIdentType
	 */
	public void setpFIdentType(String pFIdentType) {
		this.pFIdentType = pFIdentType;
	}
	/**
	 * 
	 * @return pFIdentNo
	 */
	public String getpFIdentNo() {
		return pFIdentNo;
	}
	/**
	 * 
	 * @param pFIdentNo pFIdentNo
	 */
	public void setpFIdentNo(String pFIdentNo) {
		this.pFIdentNo = pFIdentNo;
	}
	/**
	 * 
	 * @return pFRealName
	 */
	public String getpFRealName() {
		return pFRealName;
	}
	/**
	 * 
	 * @param pFRealName pFRealName
	 */
	public void setpFRealName(String pFRealName) {
		this.pFRealName = pFRealName;
	}
	/**
	 * 
	 * @return pFIpsAcctNo
	 */
	public String getpFIpsAcctNo() {
		return pFIpsAcctNo;
	}
	/**
	 * 
	 * @param pFIpsAcctNo pFIpsAcctNo
	 */
	public void setpFIpsAcctNo(String pFIpsAcctNo) {
		this.pFIpsAcctNo = pFIpsAcctNo;
	}
	/**
	 * 
	 * @return pTAcctType
	 */
	public String getpTAcctType() {
		return pTAcctType;
	}
	/**
	 * 
	 * @param pTAcctType pTAcctType
	 */
	public void setpTAcctType(String pTAcctType) {
		this.pTAcctType = pTAcctType;
	}
	/**
	 * 
	 * @return pTIdentType
	 */
	public String getpTIdentType() {
		return pTIdentType;
	}
	/**
	 * 
	 * @param pTIdentType pTIdentType
	 */
	public void setpTIdentType(String pTIdentType) {
		this.pTIdentType = pTIdentType;
	}
	/**
	 * 
	 * @return pTIdentNo 
	 */
	public String getpTIdentNo() {
		return pTIdentNo;
	}
	/**
	 * 
	 * @param pTIdentNo pTIdentNo 
	 */
	public void setpTIdentNo(String pTIdentNo) {
		this.pTIdentNo = pTIdentNo;
	}
	/**
	 * 
	 * @return pTRealName
	 */
	public String getpTRealName() {
		return pTRealName;
	}
	/**
	 * 
	 * @param pTRealName pTRealName
	 */
	public void setpTRealName(String pTRealName) {
		this.pTRealName = pTRealName;
	}
	/**
	 * 
	 * @return pTIpsAcctNo
	 */
	public String getpTIpsAcctNo() {
		return pTIpsAcctNo;
	}
	/**
	 * 
	 * @param pTIpsAcctNo pTIpsAcctNo
	 */
	public void setpTIpsAcctNo(String pTIpsAcctNo) {
		this.pTIpsAcctNo = pTIpsAcctNo;
	}
	/**
	 * 
	 * @return pTrdAmt
	 */
	public String getpTrdAmt() {
		return pTrdAmt;
	}
	/**
	 * 
	 * @param pTrdAmt pTrdAmt
	 */
	public void setpTrdAmt(String pTrdAmt) {
		this.pTrdAmt = pTrdAmt;
	}
	/**
	 * 
	 * @return pFTrdFee
	 */
	public String getpFTrdFee() {
		return pFTrdFee;
	}
	/**
	 * 
	 * @param pFTrdFee pFTrdFee
	 */
	public void setpFTrdFee(String pFTrdFee) {
		this.pFTrdFee = pFTrdFee;
	}
	/**
	 * 
	 * @return pTTrdFee
	 */
	public String getpTTrdFee() {
		return pTTrdFee;
	}
	/**
	 * 
	 * @param pTTrdFee pTTrdFee
	 */
	public void setpTTrdFee(String pTTrdFee) {
		this.pTTrdFee = pTTrdFee;
	}
	/**
	 * 
	 * @return pWebUrl
	 */
	public String getpWebUrl() {
		return pWebUrl;
	}
	/**
	 * 
	 * @param pWebUrl pWebUrl
	 */
	public void setpWebUrl(String pWebUrl) {
		this.pWebUrl = pWebUrl;
	}
	/**
	 * 
	 * @return pS2SUrl
	 */
	public String getpS2SUrl() {
		return pS2SUrl;
	}
	/**
	 * 
	 * @param pS2SUrl pS2SUrl
	 */
	public void setpS2SUrl(String pS2SUrl) {
		this.pS2SUrl = pS2SUrl;
	}
	/**
	 * 
	 * @return pMemo1
	 */
	public String getpMemo1() {
		return pMemo1;
	}
	/**
	 * 
	 * @param pMemo1 pMemo1
	 */
	public void setpMemo1(String pMemo1) {
		this.pMemo1 = pMemo1;
	}
	/**
	 * 
	 * @return pMemo2
	 */
	public String getpMemo2() {
		return pMemo2;
	}
	/**
	 * 
	 * @param pMemo2 pMemo2
	 */
	public void setpMemo2(String pMemo2) {
		this.pMemo2 = pMemo2;
	}
	/**
	 * 
	 * @return pMemo3
	 */
	public String getpMemo3() {
		return pMemo3;
	}
	/**
	 * 
	 * @param pMemo3 pMemo3
	 */
	public void setpMemo3(String pMemo3) {
		this.pMemo3 = pMemo3;
	}
	/**
	 * 
	 * @return pIpsBillNo
	 */
	public String getpIpsBillNo() {
		return pIpsBillNo;
	}
	/**
	 * 
	 * @param pIpsBillNo pIpsBillNo
	 */
	public void setpIpsBillNo(String pIpsBillNo) {
		this.pIpsBillNo = pIpsBillNo;
	}
	
	
	
}
