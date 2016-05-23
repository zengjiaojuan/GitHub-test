package com.phb.puhuibao.entity;

import java.util.Date;

public class ExperienceProduct {
	private int productId; // id
	private String productName; // 产品名称
	private String productSN; // 编号
	private Double annualizedRate; // 年化利率
	private Integer period; // 周期天
	private Date createTime;
	private Date updateTime;
	private Long totalAmount; // 总投资额度

	public String getProductSN() {
		return productSN;
	}
	public void setProductSN(String productSN) {
		this.productSN = productSN;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public Double getAnnualizedRate() {
		return annualizedRate;
	}
	public void setAnnualizedRate(Double annualizedRate) {
		this.annualizedRate = annualizedRate;
	}
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Long getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}

}
