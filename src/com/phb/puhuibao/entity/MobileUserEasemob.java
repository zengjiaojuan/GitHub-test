package com.phb.puhuibao.entity;

import java.io.Serializable;

public class MobileUserEasemob implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2515215280274289457L;
	private Integer mUserId;
	private String userName;
	private String password;
	public Integer getmUserId() {
		return mUserId;
	}
	public void setmUserId(Integer mUserId) {
		this.mUserId = mUserId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
