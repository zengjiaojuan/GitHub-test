package com.phb.puhuibao.entity;

public class UserAsset {
	private Integer mUserId;
	private String mUserName;
	private String mUserTel;
	private Double mUserMoney; // 用户现有资金
	private Double frozenMoney; // 用户冻结资金
	private Integer bidInvestmentAmount; // 理财投资金额
	private Integer itemInvestmentAmount; // 项目投资金额
	private Integer creditLoanAmount; // 授信贷款金额
	
	public Integer getmUserId() {
		return mUserId;
	}
	public void setmUserId(Integer mUserId) {
		this.mUserId = mUserId;
	}
	public String getmUserName() {
		return mUserName;
	}
	public void setmUserName(String mUserName) {
		this.mUserName = mUserName;
	}
	public String getmUserTel() {
		return mUserTel;
	}
	public void setmUserTel(String mUserTel) {
		this.mUserTel = mUserTel;
	}
	public Double getmUserMoney() {
		return mUserMoney;
	}
	public void setmUserMoney(Double mUserMoney) {
		this.mUserMoney = mUserMoney;
	}
	public Double getFrozenMoney() {
		return frozenMoney;
	}
	public void setFrozenMoney(Double frozenMoney) {
		this.frozenMoney = frozenMoney;
	}
	public Integer getBidInvestmentAmount() {
		return bidInvestmentAmount;
	}
	public void setBidInvestmentAmount(Integer bidInvestmentAmount) {
		this.bidInvestmentAmount = bidInvestmentAmount;
	}
	public Integer getItemInvestmentAmount() {
		return itemInvestmentAmount;
	}
	public void setItemInvestmentAmount(Integer itemInvestmentAmount) {
		this.itemInvestmentAmount = itemInvestmentAmount;
	}
	public Integer getCreditLoanAmount() {
		return creditLoanAmount;
	}
	public void setCreditLoanAmount(Integer creditLoanAmount) {
		this.creditLoanAmount = creditLoanAmount;
	}
}
