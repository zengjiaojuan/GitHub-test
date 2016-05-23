package com.phb.puhuibao.entity;

import java.util.Date;

public class UserAppreciation {
	private Integer id;
	private Integer appreciationId;
	private Integer mUserId;
	private Date createTime;

	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAppreciationId() {
		return appreciationId;
	}
	public void setAppreciationId(Integer appreciationId) {
		this.appreciationId = appreciationId;
	}
	public Integer getmUserId() {
		return mUserId;
	}
	public void setmUserId(Integer mUserId) {
		this.mUserId = mUserId;
	}
}
