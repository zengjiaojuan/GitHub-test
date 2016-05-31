package com.phb.puhuibao.entity;

public class AddRate {
	private int rateId; // id
	private String rateName; // 产品名称
	private Double annualizedRate; // 年化利率
	private String rateFlag; // equal  equalorgreaterthan  equalorsmallerthan
	private int ratePeriod; // 天数
	private int rateStatus;//rate_status   状态
	
	private String amountFlag; // 限制额度前面的符号
	private int rateAmount; // 限制额度
	
	 
	public String getAmountFlag() {
		return amountFlag;
	}
	public void setAmountFlag(String amountFlag) {
		this.amountFlag = amountFlag;
	}
	public int getRateAmount() {
		return rateAmount;
	}
	public void setRateAmount(int rateAmount) {
		this.rateAmount = rateAmount;
	}
	public int getRateStatus() {
		return rateStatus;
	}
	public void setRateStatus(int rateStatus) {
		this.rateStatus = rateStatus;
	}
	 
	public void setRatePeriod(int ratePeriod) {
		this.ratePeriod = ratePeriod;
	}
	public int getRateId() {
		return rateId;
	}
	public void setRateId(int rateId) {
		this.rateId = rateId;
	}
	public String getRateName() {
		return rateName;
	}
	public void setRateName(String rateName) {
		this.rateName = rateName;
	}
	public Double getAnnualizedRate() {
		return annualizedRate;
	}
	public void setAnnualizedRate(Double annualizedRate) {
		this.annualizedRate = annualizedRate;
	}
	public String getRateFlag() {
		return rateFlag;
	}
	public void setRateFlag(String rateFlag) {
		this.rateFlag = rateFlag;
	}
	public int getRatePeriod() {
		return ratePeriod;
	}
	public void setRatePerioud(int ratePerioud) {
		this.ratePeriod = ratePerioud;
	}
	 
 

}
