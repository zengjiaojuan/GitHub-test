package com.phb.puhuibao.entity;

public class ThirdPayAccount {
	private String ybOrderId;
	private String orderId;
	private Double amount;
	private Double fee;
	private String payDate;
	private String type ;
	
	public String getYbOrderId() {
		return ybOrderId;
	}
	public void setYbOrderId(String ybOrderId) {
		this.ybOrderId = ybOrderId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getFee() {
		return fee;
	}
	public void setFee(Double fee) {
		this.fee = fee;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
