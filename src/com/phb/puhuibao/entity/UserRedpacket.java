package com.phb.puhuibao.entity;

import java.util.Date;

public class UserRedpacket {
	private int redpacketId;
	private Integer mUserId;
    private Integer redpacketAmount;
    private Double deductionRate;
	private Integer status;
	private Date lastDate;
	public int getRedpacketId() {
		return redpacketId;
	}
	public void setRedpacketId(int redpacketId) {
		this.redpacketId = redpacketId;
	}
	public Integer getmUserId() {
		return mUserId;
	}
	public void setmUserId(Integer mUserId) {
		this.mUserId = mUserId;
	}
	public Integer getRedpacketAmount() {
		return redpacketAmount;
	}
	public void setRedpacketAmount(Integer redpacketAmount) {
		this.redpacketAmount = redpacketAmount;
	}
	public Double getDeductionRate() {
		return deductionRate;
	}
	public void setDeductionRate(Double deductionRate) {
		this.deductionRate = deductionRate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getLastDate() {
		return lastDate;
	}
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
	
}
