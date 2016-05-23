package com.phb.puhuibao.entity;

import java.util.Date;

public class UserExperience {
	private int experienceId;
	private Integer mUserId;
    private Integer experienceAmount;
	private Integer status;
	private Date lastDate;
	private Date createTime;
	
	public int getExperienceId() {
		return experienceId;
	}
	public void setExperienceId(int experienceId) {
		this.experienceId = experienceId;
	}
	public Integer getmUserId() {
		return mUserId;
	}
	public void setmUserId(Integer mUserId) {
		this.mUserId = mUserId;
	}
	public Integer getExperienceAmount() {
		return experienceAmount;
	}
	public void setExperienceAmount(Integer experienceAmount) {
		this.experienceAmount = experienceAmount;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
