package com.phb.puhuibao.entity;

import java.util.Date;

public class UserLoan {
	private Integer loanId;
	private Integer mUserId;
	private Date createTime;
	private Integer status; // 状态
	private Integer period; // 单位月
	private Integer useage; // 用途
	private Integer type; // 类型
	private Long amount; // 贷款额度
	private Double rate; // 贷款利率
	private Double lastReturn; // 总还款额
	private Date giveDate; // 放款日期
	private Integer repayment; // 每月可还
	private Date lastDate;
	
	public Date getLastDate() {
		return lastDate;
	}
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
	public Integer getRepayment() {
		return repayment;
	}
	public void setRepayment(Integer repayment) {
		this.repayment = repayment;
	}
	public Date getGiveDate() {
		return giveDate;
	}
	public void setGiveDate(Date giveDate) {
		this.giveDate = giveDate;
	}
	public Integer getLoanId() {
		return loanId;
	}
	public void setLoanId(Integer loanId) {
		this.loanId = loanId;
	}
	public Integer getmUserId() {
		return mUserId;
	}
	public void setmUserId(Integer mUserId) {
		this.mUserId = mUserId;
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
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
	public Integer getUseage() {
		return useage;
	}
	public void setUseage(Integer useage) {
		this.useage = useage;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Double getLastReturn() {
		return lastReturn;
	}
	public void setLastReturn(Double lastReturn) {
		this.lastReturn = lastReturn;
	}
}
