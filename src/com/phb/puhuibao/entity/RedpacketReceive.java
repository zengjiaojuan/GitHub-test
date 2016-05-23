package com.phb.puhuibao.entity;

import java.util.Date;
/**
 * @author wei  红包列表
 *
 */
public class RedpacketReceive {
 
	
	private int   receiveId;//receive_id;//
	private int   receiveUser;//receive_user;//
	private String   receiverName;//receive_user;//
	private String   receiverPhoto;//receive_user;//
	private int   receiveRedpacket;//receive_redpacket;//
	private double   receiveAmount;//receive_amount;//
	private Date   createTime;//create_time;//
	private int   receiveBest;//receive_best
	
	
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	 
	public String getReceiverPhoto() {
		return receiverPhoto;
	}
	public void setReceiverPhoto(String receiverPhoto) {
		this.receiverPhoto = receiverPhoto;
	}
	public int getReceiveBest() {
		return receiveBest;
	}
	public void setReceiveBest(int receiveBest) {
		this.receiveBest = receiveBest;
	}
	public int getReceiveId() {
		return receiveId;
	}
	public void setReceiveId(int receiveId) {
		this.receiveId = receiveId;
	}
	public int getReceiveUser() {
		return receiveUser;
	}
	public void setReceiveUser(int receiveUser) {
		this.receiveUser = receiveUser;
	}
	public int getReceiveRedpacket() {
		return receiveRedpacket;
	}
	public void setReceiveRedpacket(int receiveRedpacket) {
		this.receiveRedpacket = receiveRedpacket;
	}
	public double getReceiveAmount() {
		return receiveAmount;
	}
	public void setReceiveAmount(double receiveAmount) {
		this.receiveAmount = receiveAmount;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	
 
	
  
	 
	
	
}
