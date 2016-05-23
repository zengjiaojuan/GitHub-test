package com.phb.puhuibao.entity;

import java.util.Date;

public class Appreciation {
	private int appreciationId;
	private Integer mUserId;
    private String appreciationName;
    private String appreciationDesc;
    private String icon;
	private Double price;
	private Integer status;
	private Date createTime;
	private Double brokerageRate;
	private Integer catId;
    private String invalidIcon;
	private Date startDate;
	private Date endDate;
	
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
	public String getInvalidIcon() {
		return invalidIcon;
	}
	public void setInvalidIcon(String invalidIcon) {
		this.invalidIcon = invalidIcon;
	}
	public Integer getCatId() {
		return catId;
	}
	public void setCatId(Integer catId) {
		this.catId = catId;
	}
	public Double getBrokerageRate() {
		return brokerageRate;
	}
	public void setBrokerageRate(Double brokerageRate) {
		this.brokerageRate = brokerageRate;
	}
	public int getAppreciationId() {
		return appreciationId;
	}
	public void setAppreciationId(int appreciationId) {
		this.appreciationId = appreciationId;
	}
	public Integer getmUserId() {
		return mUserId;
	}
	public void setmUserId(Integer mUserId) {
		this.mUserId = mUserId;
	}
	public String getAppreciationName() {
		return appreciationName;
	}
	public void setAppreciationName(String appreciationName) {
		this.appreciationName = appreciationName;
	}
	public String getAppreciationDesc() {
		return appreciationDesc;
	}
	public void setAppreciationDesc(String appreciationDesc) {
		this.appreciationDesc = appreciationDesc;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
