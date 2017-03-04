package com.cddgg.p2p.huitou.util;

/**
 * 
 * 用于未收款明细对象存放
 * @author 冉启炳
 *
 */
public class LoanUncollected {
    private long loanId;    //标的id
	private String loanNumber;  //借款标号
	private String loanTitle;   //借款标题
	private String borrowername; //借款人
	private String tenderTime;  //投资日期
	private double tenderMoney; //投标金额
	private double rateYear;  //利率
	private String tzyj; //管理费
	private String refundDeadline; //总期数(还款期)
	private String sfyq;//是否逾期
	//2013-08-12增加属性
	private String fkrq;//放款日期
	private String hkrq;//还款日期
	private String tzsy;//投标收益
	private String interest;//利息
	private int overdueDay;//逾期天数
	private double overdueInterest;//逾期利息
	private String status;  //状态
	private String repayTime; //应收还款日期
	private double should;  //应收利息
	private double total;  //收款总额
	private double preInterest;  //预计还款利息
	private double totalReceived; //已收总额
	private double pandInterest; //已收本息
	private double interestReceivable; //待收利息
	public String getLoanNumber() {
		return loanNumber;
	}
	public void setLoanNumber(String loanNumber) {
		this.loanNumber = loanNumber;
	}
	public String getBorrowername() {
		return borrowername;
	}
	public void setBorrowername(String borrowername) {
		this.borrowername = borrowername;
	}
	public String getTenderTime() {
		return tenderTime;
	}
	public void setTenderTime(String tenderTime) {
		this.tenderTime = tenderTime;
	}
	public double getTenderMoney() {
		return tenderMoney;
	}
	public void setTenderMoney(double tenderMoney) {
		this.tenderMoney = tenderMoney;
	}
	public double getRateYear() {
		return rateYear;
	}
	public void setRateYear(double rateYear) {
		this.rateYear = rateYear;
	}
	public String getTzyj() {
		return tzyj;
	}
	public void setTzyj(String tzyj) {
		this.tzyj = tzyj;
	}
	public String getRefundDeadline() {
		return refundDeadline;
	}
	public void setRefundDeadline(String refundDeadline) {
		this.refundDeadline = refundDeadline;
	}
	public String getSfyq() {
		return sfyq;
	}
	public void setSfyq(String sfyq) {
		this.sfyq = sfyq;
	}
	public String getFkrq() {
		return fkrq;
	}
	public void setFkrq(String fkrq) {
		this.fkrq = fkrq;
	}
	public String getHkrq() {
		return hkrq;
	}
	public void setHkrq(String hkrq) {
		this.hkrq = hkrq;
	}
	public String getTzsy() {
		return tzsy;
	}
	public void setTzsy(String tzsy) {
		this.tzsy = tzsy;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public int getOverdueDay() {
		return overdueDay;
	}
	public void setOverdueDay(int overdueDay) {
		this.overdueDay = overdueDay;
	}
	public double getOverdueInterest() {
		return overdueInterest;
	}
	public void setOverdueInterest(double overdueInterest) {
		this.overdueInterest = overdueInterest;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRepayTime() {
		return repayTime;
	}
	public void setRepayTime(String repayTime) {
		this.repayTime = repayTime;
	}
	public double getShould() {
		return should;
	}
	public void setShould(double should) {
		this.should = should;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public double getTotalReceived() {
		return totalReceived;
	}
	public void setTotalReceived(double totalReceived) {
		this.totalReceived = totalReceived;
	}
	public double getPandInterest() {
		return pandInterest;
	}
	public void setPandInterest(double pandInterest) {
		this.pandInterest = pandInterest;
	}
	public double getInterestReceivable() {
		return interestReceivable;
	}
	public void setInterestReceivable(double interestReceivable) {
		this.interestReceivable = interestReceivable;
	}
    public String getLoanTitle() {
        return loanTitle;
    }
    public void setLoanTitle(String loanTitle) {
        this.loanTitle = loanTitle;
    }
    public long getLoanId() {
        return loanId;
    }
    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }
    public double getPreInterest() {
        return preInterest;
    }
    public void setPreInterest(double preInterest) {
        this.preInterest = preInterest;
    }
	
	
}
