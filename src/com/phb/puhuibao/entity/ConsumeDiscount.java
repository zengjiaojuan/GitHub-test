package com.phb.puhuibao.entity;

import java.util.Date;

public class ConsumeDiscount {
	private int discountId;
	private Integer appreciationId;
	private Double discount;
	private Integer level;
	private Date createTime;
	
	public int getDiscountId() {
		return discountId;
	}
	public void setDiscountId(int discountId) {
		this.discountId = discountId;
	}
	public Integer getAppreciationId() {
		return appreciationId;
	}
	public void setAppreciationId(Integer appreciationId) {
		this.appreciationId = appreciationId;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
