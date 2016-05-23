package com.phb.puhuibao.entity;

import java.util.Date;


/**
 * @author wei
 *
 */

import java.io.Serializable;
 

public class GroupRedpacket implements Serializable {
 
	private static final long serialVersionUID = 6949218547325191688L;
 
 
	private int      redpacketId;//redpacket_id
	private int      redpacketUser;//redpacket_user
 
	private String   redpacketDesc;//redpacket_desc
	private int      redpacketNumber;//redpacket_number
	private double   redpacketAmount;//redpacket_amount
	private int      redpacketStatus;//redpacket_left
	private int      redpacketType;//redpacket_type
 
	private Date     createTime;//create_time
	
	
	
	public int getRedpacketStatus() {
		return redpacketStatus;
	}
	public void setRedpacketStatus(int redpacketStatus) {
		this.redpacketStatus = redpacketStatus;
	}
	 
	public String getRedpacketDesc() {
		return redpacketDesc;
	}
	public void setRedpacketDesc(String redpacketDesc) {
		this.redpacketDesc = redpacketDesc;
	}
	public int getRedpacketId() {
		return redpacketId;
	}
	public void setRedpacketId(int redpacketId) {
		this.redpacketId = redpacketId;
	}
	public int getRedpacketUser() {
		return redpacketUser;
	}
	public void setRedpacketUser(int redpacketUser) {
		this.redpacketUser = redpacketUser;
	}
	 
	public int getRedpacketNumber() {
		return redpacketNumber;
	}
	public void setRedpacketNumber(int redpacketNumber) {
		this.redpacketNumber = redpacketNumber;
	}
	public double getRedpacketAmount() {
		return redpacketAmount;
	}
	public void setRedpacketAmount(double redpacketAmount) {
		this.redpacketAmount = redpacketAmount;
	}
 
	public int getRedpacketType() {
		return redpacketType;
	}
	public void setRedpacketType(int redpacketType) {
		this.redpacketType = redpacketType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	
	 
	
  
	 
	
	
}
