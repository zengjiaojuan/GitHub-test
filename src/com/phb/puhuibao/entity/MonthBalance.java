package com.phb.puhuibao.entity;

public class MonthBalance {
    private String yyyymm; // 年月
    private Double payAmount; // 公司账户收入(充值)
    private Double investmentIncome; // 投资收益
    private Double loanInterest; // 授信贷款月息
    private Double totalAsset; // 用户总资产
    private Double totalAssetLast; // 上月用户总资产
    private Double withdrawAmount; // 公司账户支出(提现)
    private Double balance; // 公司账户余额
    private Integer status;
    
	public Double getTotalAsset() {
		return totalAsset;
	}
	public void setTotalAsset(Double totalAsset) {
		this.totalAsset = totalAsset;
	}
	public Double getTotalAssetLast() {
		return totalAssetLast;
	}
	public void setTotalAssetLast(Double totalAssetLast) {
		this.totalAssetLast = totalAssetLast;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Double getInvestmentIncome() {
		return investmentIncome;
	}
	public void setInvestmentIncome(Double investmentIncome) {
		this.investmentIncome = investmentIncome;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public String getYyyymm() {
		return yyyymm;
	}
	public void setYyyymm(String yyyymm) {
		this.yyyymm = yyyymm;
	}
	public Double getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}
	public Double getLoanInterest() {
		return loanInterest;
	}
	public void setLoanInterest(Double loanInterest) {
		this.loanInterest = loanInterest;
	}
	public Double getWithdrawAmount() {
		return withdrawAmount;
	}
	public void setWithdrawAmount(Double withdrawAmount) {
		this.withdrawAmount = withdrawAmount;
	}
}
