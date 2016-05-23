package com.phb.puhuibao.entity;

import java.util.Date;

public class MobileUserSignin {
	private Integer mUserId;
	private Integer totalIntegral;
	private Integer usedIntegral;
	private Date signinDate;

	public Date getSigninDate() {
		return signinDate;
	}
	public void setSigninDate(Date signinDate) {
		this.signinDate = signinDate;
	}
	public Integer getmUserId() {
		return mUserId;
	}
	public void setmUserId(Integer mUserId) {
		this.mUserId = mUserId;
	}
	public Integer getTotalIntegral() {
		return totalIntegral;
	}
	public void setTotalIntegral(Integer totalIntegral) {
		this.totalIntegral = totalIntegral;
	}
	public Integer getUsedIntegral() {
		return usedIntegral;
	}
	public void setUsedIntegral(Integer usedIntegral) {
		this.usedIntegral = usedIntegral;
	}
}
