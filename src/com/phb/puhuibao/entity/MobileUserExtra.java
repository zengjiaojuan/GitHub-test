package com.phb.puhuibao.entity;

import java.util.Date;

public class MobileUserExtra {
	private int mUserId;
	private String merchantName; // 商家名称
	private String merchantAddr; // 商家地址
	private String merchantTel; // 商家电话
	private String merchantRemark; // 备注
	private Double merchantLongitude; // 经度
	private Double merchantLatitude; // 纬度
	private Date createTime;
	private Double creditAmount; // 信用额度
	private Double usedAmount; // 已用额度
	
	public Double getCreditAmount() {
		return creditAmount;
	}
	public void setCreditAmount(Double creditAmount) {
		this.creditAmount = creditAmount;
	}
	public Double getUsedAmount() {
		return usedAmount;
	}
	public void setUsedAmount(Double usedAmount) {
		this.usedAmount = usedAmount;
	}
	public int getmUserId() {
		return mUserId;
	}
	public void setmUserId(int mUserId) {
		this.mUserId = mUserId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getMerchantAddr() {
		return merchantAddr;
	}
	public void setMerchantAddr(String merchantAddr) {
		this.merchantAddr = merchantAddr;
	}
	public String getMerchantTel() {
		return merchantTel;
	}
	public void setMerchantTel(String merchantTel) {
		this.merchantTel = merchantTel;
	}
	public String getMerchantRemark() {
		return merchantRemark;
	}
	public void setMerchantRemark(String merchantRemark) {
		this.merchantRemark = merchantRemark;
	}
	public Double getMerchantLongitude() {
		return merchantLongitude;
	}
	public void setMerchantLongitude(Double merchantLongitude) {
		this.merchantLongitude = merchantLongitude;
	}
	public Double getMerchantLatitude() {
		return merchantLatitude;
	}
	public void setMerchantLatitude(Double merchantLatitude) {
		this.merchantLatitude = merchantLatitude;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
