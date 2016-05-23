package com.phb.puhuibao.entity;

import java.util.Date;

public class InvestmentAlert {
	
	private Integer alertId;
	private String alertTitle;
	private String alertProduct;
	private String alertContent;
	private Integer alertStatus;
	private String alertDeviceid;
	private Integer alertDevicetype;
	private Date pushTime;
	
	
	public String getAlertProduct() {
		return alertProduct;
	}
	public void setAlertProduct(String alertProduct) {
		this.alertProduct = alertProduct;
	}
	public Integer getAlertDevicetype() {
		return alertDevicetype;
	}
	public void setAlertDevicetype(Integer alertDevicetype) {
		this.alertDevicetype = alertDevicetype;
	}
	public Integer getAlertId() {
		return alertId;
	}
	public void setAlertId(Integer alertId) {
		this.alertId = alertId;
	}
	public String getAlertTitle() {
		return alertTitle;
	}
	public void setAlertTitle(String alertTitle) {
		this.alertTitle = alertTitle;
	}
	public String getAlertContent() {
		return alertContent;
	}
	public void setAlertContent(String alertContent) {
		this.alertContent = alertContent;
	}
	public Integer getAlertStatus() {
		return alertStatus;
	}
	public void setAlertStatus(Integer alertStatus) {
		this.alertStatus = alertStatus;
	}
	public String getAlertDeviceid() {
		return alertDeviceid;
	}
	public void setAlertDeviceid(String alertDeviceid) {
		this.alertDeviceid = alertDeviceid;
	}
	public Date getPushTime() {
		return pushTime;
	}
	public void setPushTime(Date pushTime) {
		this.pushTime = pushTime;
	}
	
	
	
}
