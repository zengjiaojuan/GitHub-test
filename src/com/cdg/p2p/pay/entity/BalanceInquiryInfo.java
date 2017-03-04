package com.cddgg.p2p.pay.entity;

/**
 * 用户账户余额查询
 * 
 * @author RanQiBing 2014-01-03
 * 
 */
public class BalanceInquiryInfo {

    /**
     * “平台”账号
     */
    private String pMerCode;
    /**
     * 返回状态
     */
    private String pErrCode;
    /**
     * 返回信息
     */
    private String pErrMsg;
    /**
     * IPS账户号
     */
    private String pIpsAcctNo;
    /**
     * 可用余额
     */
    private String pBalance;
    /**
     * 冻结余额
     */
    private String pLock;
    /**
     * 未结算余额
     */
    private String pNeedstl;
    /**
     * MD5 摘要
     */
    private String pSign;

    /**
     * 
     * @return pMerCode
     */
    public String getpMerCode() {
        return pMerCode;
    }

    /**
     * 
     * @param pMerCode
     *            pMerCode
     */
    public void setpMerCode(String pMerCode) {
        this.pMerCode = pMerCode;
    }

    /**
     * 
     * @return pErrCode
     */
    public String getpErrCode() {
        return pErrCode;
    }

    /**
     * 
     * @param pErrCode
     *            pErrCode
     */
    public void setpErrCode(String pErrCode) {
        this.pErrCode = pErrCode;
    }

    /**
     * 
     * @return pErrMsg
     */
    public String getpErrMsg() {
        return pErrMsg;
    }

    /**
     * 
     * @param pErrMsg
     *            pErrMsg
     */
    public void setpErrMsg(String pErrMsg) {
        this.pErrMsg = pErrMsg;
    }

    /**
     * 
     * @return pIpsAcctNo
     */
    public String getpIpsAcctNo() {
        return pIpsAcctNo;
    }

    /**
     * 
     * @param pIpsAcctNo
     *            pIpsAcctNo
     */
    public void setpIpsAcctNo(String pIpsAcctNo) {
        this.pIpsAcctNo = pIpsAcctNo;
    }

    /**
     * 
     * @return pBalance
     */
    public String getpBalance() {
        return pBalance;
    }

    /**
     * 
     * @param pBalance
     *            pBalance
     */
    public void setpBalance(String pBalance) {
        this.pBalance = pBalance;
    }

    /**
     * 
     * @return pLock
     */
    public String getpLock() {
        return pLock;
    }

    /**
     * 
     * @param pLock
     *            pLock
     */
    public void setpLock(String pLock) {
        this.pLock = pLock;
    }

    /**
     * 
     * @return pNeedstl
     */
    public String getpNeedstl() {
        return pNeedstl;
    }

    /**
     * 
     * @param pNeedstl
     *            pNeedstl
     */
    public void setpNeedstl(String pNeedstl) {
        this.pNeedstl = pNeedstl;
    }

    /**
     * 
     * @return pSign
     */
    public String getpSign() {
        return pSign;
    }

    /**
     * 
     * @param pSign
     *            pSign
     */
    public void setpSign(String pSign) {
        this.pSign = pSign;
    }

}
