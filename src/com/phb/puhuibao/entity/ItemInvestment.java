package com.phb.puhuibao.entity;

import java.util.Date;

public class ItemInvestment {
	private int investmentId;
	private Integer mUserId;
	private String itemSN;
    private Long investmentAmount;
	private Date createTime;
	private Integer status;
	private Date incomeDate;
	private Double lastIncome;
	private String useage; // 产品名称
	private String guaranteeMethod; // 保障方式
	private Integer period; // 周期
	private Double annualizedRate; // 年化利率
	private Integer leftDays; // 到期天数
	private Date lastDate;
	
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
	public String getItemSN() {
		return itemSN;
	}
	public void setItemSN(String itemSN) {
		this.itemSN = itemSN;
	}
	public Long getInvestmentAmount() {
		return investmentAmount;
	}
	public void setInvestmentAmount(Long investmentAmount) {
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
	public Double getLastIncome() {
		return lastIncome;
	}
	public void setLastIncome(Double lastIncome) {
		this.lastIncome = lastIncome;
	}
	public String getUseage() {
		return useage;
	}
	public void setUseage(String useage) {
		this.useage = useage;
	}
	public String getGuaranteeMethod() {
		return guaranteeMethod;
	}
	public void setGuaranteeMethod(String guaranteeMethod) {
		this.guaranteeMethod = guaranteeMethod;
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
	public Date getLastDate() {
		return lastDate;
	}
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
}
