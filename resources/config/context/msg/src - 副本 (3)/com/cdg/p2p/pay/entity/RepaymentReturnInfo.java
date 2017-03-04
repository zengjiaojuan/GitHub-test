package com.cddgg.p2p.pay.entity;
/**
 * 还款返回信息
 * @author RanQiBing 2014-02-25
 *
 */
public class RepaymentReturnInfo {
    /**标号*/
    private String pBidNo;
    /**合同号*/
    private String pContractNo;
    /**还款日期*/
    private String pRepaymentDate;
    /**商户还款订单*/
    private String pMerBillNo;
    /**IPS还款订单号 */
    private String pIpsBillNo;
    /**备注*/
    private String pMemo1;
    /**备注*/
    private String pMemo2;
    /**备注*/
    private String pMemo3;
    
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
     * 
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
     * @return pRepaymentDate
     */
    public String getpRepaymentDate() {
        return pRepaymentDate;
    }
    /**
     * @param pRepaymentDate  pRepaymentDate
     */
    public void setpRepaymentDate(String pRepaymentDate) {
        this.pRepaymentDate = pRepaymentDate;
    }
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
    /**
     * pMemo1
     * @return pMemo1
     */
    public String getpMemo1() {
        return pMemo1;
    }
    /**
     * pMemo1
     * @param pMemo1 pMemo1
     */
    public void setpMemo1(String pMemo1) {
        this.pMemo1 = pMemo1;
    }
    /**
     * pMemo2
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
     * pMemo3
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
    
    
}
