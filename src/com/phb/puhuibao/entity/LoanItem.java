package com.phb.puhuibao.entity;

import java.util.Date;

public class LoanItem {
	private Integer itemId;
	private Integer loanId;
	private String itemSN; // 项目编码
	private Long currentAmount; // 已投资额度
	private Long totalAmount; // 总投资额度
	private Integer status; // 投资状态
	private String paymentMethod; // 还款方式
	private String guaranteeMethod; // 保障方式
	private Integer investmentAmountMin; // 最小投资额
	private Double annualizedRate; // 年化利率
	private Integer period; // 投资期限，单位：月
	private Date createTime;
	private String itemDesc; // 项目介绍
	private String useage; // 用途
	private Double rate; // 贷款利率
	private Date startDate; // 加入期
	private Date endDate; // 到期日
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public String getUseage() {
		return useage;
	}
	public void setUseage(String useage) {
		this.useage = useage;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public Integer getLoanId() {
		return loanId;
	}
	public void setLoanId(Integer loanId) {
		this.loanId = loanId;
	}
	public String getItemSN() {
		return itemSN;
	}
	public void setItemSN(String itemSN) {
		this.itemSN = itemSN;
	}
	public Long getCurrentAmount() {
		return currentAmount;
	}
	public void setCurrentAmount(Long currentAmount) {
		this.currentAmount = currentAmount;
	}
	public Long getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getGuaranteeMethod() {
		return guaranteeMethod;
	}
	public void setGuaranteeMethod(String guaranteeMethod) {
		this.guaranteeMethod = guaranteeMethod;
	}
	public Integer getInvestmentAmountMin() {
		return investmentAmountMin;
	}
	public void setInvestmentAmountMin(Integer investmentAmountMin) {
		this.investmentAmountMin = investmentAmountMin;
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
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
}
