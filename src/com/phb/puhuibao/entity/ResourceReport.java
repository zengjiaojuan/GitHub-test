package com.phb.puhuibao.entity;

import java.io.Serializable;

public class ResourceReport implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3309921833375730200L;
	private Integer orderId;
	private Integer mUserId;
	private Integer resourceId;
	private Integer causeId;
	private String content;

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getmUserId() {
		return mUserId;
	}
	public void setmUserId(Integer mUserId) {
		this.mUserId = mUserId;
	}
	public Integer getResourceId() {
		return resourceId;
	}
	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}
	public Integer getCauseId() {
		return causeId;
	}
	public void setCauseId(Integer causeId) {
		this.causeId = causeId;
	}
}
