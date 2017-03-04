package com.cddgg.p2p.huitou.model;

import java.text.ParseException;

import com.cddgg.commons.date.DateUtils;
import com.cddgg.commons.log.LOG;
import com.cddgg.commons.normal.Convert;

/**
 * 合同模板
 * @author dgg
 *
 */
public class LoanContract {

    /**
     * 合同编号
     */
    private String contractId;

    /**
     * 甲方（出借人）
     */
    private String partyA;
    /**
     * 甲方（出借人）生分证
     */
    private String idCardA;

    /**
     * 乙方 （借入者）
     */
    private String partyB;
    /**
     * 乙方 身份证
     */
    private String idCardB;

    /**
     * 甲方 借出的 金额（人民币 元） 阿拉伯数字
     */
    private double loanMoney;

    /**
     * 甲方 借出的 金额（人民币 元） 中文大写数字
     */
    private String loanMoneyUpper;

    /**
     * 借款期限(月份天数)
     */
    private String borrowMonth;

    /**
     * 借款年利润
     */
    private double rate;
    /**
     * 还款方式
     */
    private String bonaType;

    /**
     * 协议签订地点
     */
    private String signedAddress;

    /**
     * 协议签署日期(借款日期)
     */
    private String dateTime;

    /**
     * 还款日期
     */
    private String monthBack;

    /**
     * pdf的密码
     */
    private String pdfPassword;

    /**
     * contractId
     * 
     * @return contractId
     */
    public String getContractId() {
        return contractId;
    }

    /**
     * contractId
     * 
     * @param contractId
     *            contractId
     */
    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    /**
     * partyA
     * 
     * @return partyA
     */
    public String getPartyA() {
        return partyA;
    }

    /**
     * partyA
     * 
     * @param partyA
     *            partyA
     */
    public void setPartyA(String partyA) {
        this.partyA = partyA;
    }

    /**
     * idCardA
     * 
     * @return idCardA
     */
    public String getIdCardA() {
        return idCardA;
    }

    /**
     * idCardA
     * 
     * @param idCardA
     *            idCardA
     */
    public void setIdCardA(String idCardA) {
        this.idCardA = idCardA;
    }

    /**
     * partyB
     * 
     * @return partyB
     */
    public String getPartyB() {
        return partyB;
    }

    /**
     * partyB
     * 
     * @param partyB
     *            partyB
     */
    public void setPartyB(String partyB) {
        this.partyB = partyB;
    }

    /**
     * idCardB
     * 
     * @return idCardB
     */
    public String getIdCardB() {
        return idCardB;
    }

    /**
     * idCardB
     * 
     * @param idCardB
     *            idCardB
     */
    public void setIdCardB(String idCardB) {
        this.idCardB = idCardB;
    }

    /**
     * loanMoney
     * 
     * @return loanMoney
     */
    public double getLoanMoney() {
        return loanMoney;
    }

    /**
     * loanMoney
     * 
     * @param loanMoney
     *            loanMoney
     * @throws Exception
     *             Exception
     */
    public void setLoanMoney(double loanMoney) throws Exception {
        this.loanMoney = loanMoney;
        this.loanMoneyUpper = Convert.moneyUpper(loanMoney, false);
    }

    /**
     * loanMoneyUpper
     * 
     * @return loanMoneyUpper
     */
    public String getLoanMoneyUpper() {
        return loanMoneyUpper;
    }

    /**
     * loanMoneyUpper
     * 
     * @param loanMoneyUpper
     *            loanMoneyUpper
     */
    public void setLoanMoneyUpper(String loanMoneyUpper) {
        this.loanMoneyUpper = loanMoneyUpper;
    }

    /**
     * borrowMonth
     * 
     * @return borrowMonth
     */
    public String getBorrowMonth() {
        return borrowMonth;
    }

    /**
     * dateTime
     * 
     * @param dateTime
     *            dateTime
     * @param monthBack
     *            monthBack
     */
    public void setBorrowMonth(String dateTime, String monthBack) {

        int temp = 0;
        try {
            temp = DateUtils.differenceDate("yyyy年MM月dd日 HH时mm分", dateTime,
                    monthBack);
        } catch (ParseException e) {
            LOG.error("时间格式转化错误！", e);
        }

        this.borrowMonth = temp / 30 + "个月" + temp % 30 + "天";

    }

    /**
     * borrowMonth
     * 
     * @param borrowMonth
     *            borrowMonth
     */
    public void setBorrowMonth(String borrowMonth) {
        this.borrowMonth = borrowMonth;
    }

    /**
     * monthBack
     * @return  monthBack
     */
    public String getMonthBack() {
        return monthBack;
    }

    /**
     * monthBack
     * @param monthBack monthBack
     */
    public void setMonthBack(String monthBack) {
        this.monthBack = monthBack;
        if (this.dateTime != null && this.borrowMonth == null){
            setBorrowMonth(this.dateTime, this.monthBack);
        }
    }

    /**
     * rate
     * @return  rate
     */
    public double getRate() {
        return rate;
    }

    /**
     * rate
     * @param rate  rate
     */
    public void setRate(double rate) {
        this.rate = rate;
    }

    /**
     * bonaType
     * @return  bonaType
     */
    public String getBonaType() {
        return bonaType;
    }

    /**
     * bonaType
     * @param bonaType  bonaType
     */
    public void setBonaType(String bonaType) {
        this.bonaType = bonaType;
    }

    /**
     * signedAddress
     * @return  signedAddress
     */
    public String getSignedAddress() {
        return signedAddress;
    }

    /**
     * signedAddress
     * @param signedAddress signedAddress
     */
    public void setSignedAddress(String signedAddress) {
        this.signedAddress = signedAddress;
    }

    /**
     * dateTime
     * @return  dateTime
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * dateTime
     * @param dateTime  dateTime
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
        if (this.monthBack != null && this.borrowMonth == null)
            setBorrowMonth(this.dateTime, this.monthBack);

    }

    /**
     * pdfPassword
     * @return  pdfPassword
     */
    public String getPdfPassword() {
        return pdfPassword;
    }

    /**
     * pdfPassword
     * @param pdfPassword  pdfPassword
     */
    public void setPdfPassword(String pdfPassword) {
        this.pdfPassword = pdfPassword;
    }

}
