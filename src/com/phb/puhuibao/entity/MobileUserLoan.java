package com.phb.puhuibao.entity;

import java.util.Date;

public class MobileUserLoan {
	private Integer mUserId;
	private String creditLevel; // 商家名称
	private String jobCertification; // 商家地址
	private String incomeCertification; // 商家电话
	private String housingCertification; // 备注
	private String remark; // 经度
	private Date createTime;
	private String organization; // 单位名称
	private String orgAddress; // 单位地址
	private String orgProperty; // 企业性质 
	private double salary; // 月收入
	private Integer type;

	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public void setmUserId(Integer mUserId) {
		this.mUserId = mUserId;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getOrgAddress() {
		return orgAddress;
	}
	public void setOrgAddress(String orgAddress) {
		this.orgAddress = orgAddress;
	}
	public String getOrgProperty() {
		return orgProperty;
	}
	public void setOrgProperty(String orgProperty) {
		this.orgProperty = orgProperty;
	}
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	public int getmUserId() {
		return mUserId;
	}
	public void setmUserId(int mUserId) {
		this.mUserId = mUserId;
	}
	public String getCreditLevel() {
		return creditLevel;
	}
	public void setCreditLevel(String creditLevel) {
		this.creditLevel = creditLevel;
	}
	public String getJobCertification() {
		return jobCertification;
	}
	public void setJobCertification(String jobCertification) {
		this.jobCertification = jobCertification;
	}
	public String getIncomeCertification() {
		return incomeCertification;
	}
	public void setIncomeCertification(String incomeCertification) {
		this.incomeCertification = incomeCertification;
	}
	public String getHousingCertification() {
		return housingCertification;
	}
	public void setHousingCertification(String housingCertification) {
		this.housingCertification = housingCertification;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
