package com.phb.puhuibao.entity;

public class WithdrawExceptionLog {
	private int withdrawLogId; // 提现日志id
	private int mUserId; // 用户id
	private double withdrawAmount; // 提现金额
	private int exceptinoType; // 提现异常类型
	public int getWithdrawLogId() {
		return withdrawLogId;
	}
	public void setWithdrawLogId(int withdrawLogId) {
		this.withdrawLogId = withdrawLogId;
	}
	public int getmUserId() {
		return mUserId;
	}
	public void setmUserId(int mUserId) {
		this.mUserId = mUserId;
	}
	public double getWithdrawAmount() {
		return withdrawAmount;
	}
	public void setWithdrawAmount(double withdrawAmount) {
		this.withdrawAmount = withdrawAmount;
	}
	public int getExceptinoType() {
		return exceptinoType;
	}
	public void setExceptinoType(int exceptinoType) {
		this.exceptinoType = exceptinoType;
	}
	public WithdrawExceptionLog(int withdrawLogId, int mUserId, double withdrawAmount, int exceptinoType) {
		super();
		this.withdrawLogId = withdrawLogId;
		this.mUserId = mUserId;
		this.withdrawAmount = withdrawAmount;
		this.exceptinoType = exceptinoType;
	}
	
	public WithdrawExceptionLog() {
		// TODO Auto-generated constructor stub
	} 
	

}
