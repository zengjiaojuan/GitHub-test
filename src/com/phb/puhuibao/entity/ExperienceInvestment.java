package com.phb.puhuibao.entity;

import java.util.Date;

public class ExperienceInvestment {
	private Integer investmentId;
	private Integer mUserId;
	private String productSN;
    private Integer investmentAmount;
	private Date createTime;
	private Integer status;
	private Date incomeDate;
	private Date expireDate;
	//private Double todayIncome;
	private Double lastIncome;
	private String productName; // 产品名称
	private Integer period; // 周期
	private Double annualizedRate; // 年化利率
	private Integer leftDays; // 到期天数
	private String unit; // 产品 期限单位
	private Double totalIncome; // 预期总收益
	
	
	
	
	
 
	public Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	public Double getTotalIncome() {
		return totalIncome;
	}
	public void setTotalIncome(Double totalIncome) {
		this.totalIncome = totalIncome;
	}
	public void setInvestmentId(Integer investmentId) {
		this.investmentId = investmentId;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getInvestmentId() {
		return investmentId;
	}
	public void setInvestmentId(int investmentId) {
		this.investmentId = investmentId;
	}
	public Integer getmUserId() {
		return mUserId;
	}
	public void setmUserId(Integer mUserId) {
		this.mUserId = mUserId;
	}
	public String getProductSN() {
		return productSN;
	}
	public void setProductSN(String productSN) {
		this.productSN = productSN;
	}
	public Integer getInvestmentAmount() {
		return investmentAmount;
	}
	public void setInvestmentAmount(Integer investmentAmount) {
		this.investmentAmount = investmentAmount;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getIncomeDate() {
		return incomeDate;
	}
	public void setIncomeDate(Date incomeDate) {
		this.incomeDate = incomeDate;
	}
//	public Double getTodayIncome() {
//		return todayIncome;
//	}
//	public void setTodayIncome(Double todayIncome) {
//		this.todayIncome = todayIncome;
//	}
	public Double getLastIncome() {
		return lastIncome;
	}
	public void setLastIncome(Double lastIncome) {
		this.lastIncome = lastIncome;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
	public Double getAnnualizedRate() {
		return annualizedRate;
	}
	public void setAnnualizedRate(Double annualizedRate) {
		this.annualizedRate = annualizedRate;
	}
	public Integer getLeftDays() {
		return leftDays;
	}
	public void setLeftDays(Integer leftDays) {
		this.leftDays = leftDays;
	}
}
