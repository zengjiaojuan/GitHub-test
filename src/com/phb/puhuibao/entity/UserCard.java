package com.phb.puhuibao.entity;

public class UserCard {
	private String bankAccount;
	private Integer mUserId;
	private String bankPhone;
	private String prcptcd;
	
	public String getPrcptcd() {
		return prcptcd;
	}
	public void setPrcptcd(String prcptcd) {
		this.prcptcd = prcptcd;
	}
	public String getBankPhone() {
		return bankPhone;
	}
	public void setBankPhone(String bankPhone) {
		this.bankPhone = bankPhone;
	}
	public Integer getmUserId() {
		return mUserId;
	}
	public void setmUserId(Integer mUserId) {
		this.mUserId = mUserId;
	}
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
}
