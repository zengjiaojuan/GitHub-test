package com.phb.puhuibao.entity;

import java.util.Date;

public class UserAddrate {
	private int record_id; // id
	private int muser_id; // 用户
	private int rateId; // 加息劵id
	private int rate_status; // 状态
	private Date last_date; // 到期日
	private Date create_time;//领取日
	
	
	public int getRecord_id() {
		return record_id;
	}
	public void setRecord_id(int record_id) {
		this.record_id = record_id;
	}
	public int getMuser_id() {
		return muser_id;
	}
	public void setMuser_id(int muser_id) {
		this.muser_id = muser_id;
	}
 
	public int getRateId() {
		return rateId;
	}
	public void setRateId(int rateId) {
		this.rateId = rateId;
	}
	public int getRate_status() {
		return rate_status;
	}
	public void setRate_status(int rate_status) {
		this.rate_status = rate_status;
	}
	public Date getLast_date() {
		return last_date;
	}
	public void setLast_date(Date last_date) {
		this.last_date = last_date;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	
 
 

}
