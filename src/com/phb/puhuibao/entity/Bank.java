package com.phb.puhuibao.entity;

public class Bank {
	private Integer bankId;
	private String bankName;
	private String bankIcon;
	private Double orderLimit;//order_limit
	private Double dayLimit;//day_limit
	private Double monthLimit;//month_limit
	 
	
	public String getBankIcon() {
		return bankIcon;
	}
	public void setBankIcon(String bankIcon) {
		this.bankIcon = bankIcon;
	}
	public Double getOrderLimit() {
		return orderLimit;
	}
	public void setOrderLimit(Double orderLimit) {
		this.orderLimit = orderLimit;
	}
	public Double getDayLimit() {
		return dayLimit;
	}
	public void setDayLimit(Double dayLimit) {
		this.dayLimit = dayLimit;
	}
	public Double getMonthLimit() {
		return monthLimit;
	}
	public void setMonthLimit(Double monthLimit) {
		this.monthLimit = monthLimit;
	}
	public Integer getBankId() {
		return bankId;
	}
	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

}
