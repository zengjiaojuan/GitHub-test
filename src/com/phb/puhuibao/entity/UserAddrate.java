package com.phb.puhuibao.entity;

import java.util.Date;

public class UserAddrate {
		private int recordId; // id
		private int muserId; // 用户
		private int rateId; // 加息劵id
		private int rateStatus; // 状态
		private Date lastDate; // 到期日
		private Date createTime;//领取日
	 private String rateName;      // 加息劵名字  
	 private Double annualizedRate;  // 加息劵利率
	 private String  rateFlag;      //期限前的标记 
	 private int ratePeriod;       //期限的数值  
	 private String amountFlag;  //额度前的标记     
	 private int rateAmount;     //额度的数值
	
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
	public void setRatePeriod(int ratePeriod) {
		this.ratePeriod = ratePeriod;
	}
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
	public int getRecordId() {
		return recordId;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public int getMuserId() {
		return muserId;
	}
	public void setMuserId(int muserId) {
		this.muserId = muserId;
	}
	public int getRateId() {
		return rateId;
	}
	public void setRateId(int rateId) {
		this.rateId = rateId;
	}
	public int getRateStatus() {
		return rateStatus;
	}
	public void setRateStatus(int rateStatus) {
		this.rateStatus = rateStatus;
	}
	public Date getLastDate() {
		return lastDate;
	}
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
 
 

}
