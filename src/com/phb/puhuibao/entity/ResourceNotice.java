package com.phb.puhuibao.entity;

import java.util.Date;
 

public class ResourceNotice {
	
	 
	private Integer   noticeId;//``notice_id``
	private Integer   noticeUser;//``notice_user``
	private Integer   noticeResource;//`notice_resource`
	private String    noticeTitle;//``notice_title``
	private String    noticeMessage;//`notice_message`
	private Integer   isRead;//is_read
	private Date      noticeTime;//`notice_time`
	
	
	
	
	public Date getNoticeTime() {
		return noticeTime;
	}
	public void setNoticeTime(Date noticeTime) {
		this.noticeTime = noticeTime;
	}
	public Integer getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(Integer noticeId) {
		this.noticeId = noticeId;
	}
	public Integer getNoticeUser() {
		return noticeUser;
	}
	public void setNoticeUser(Integer noticeUser) {
		this.noticeUser = noticeUser;
	}
	public Integer getNoticeResource() {
		return noticeResource;
	}
	public void setNoticeResource(Integer noticeResource) {
		this.noticeResource = noticeResource;
	}
	public String getNoticeTitle() {
		return noticeTitle;
	}
	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}
	public String getNoticeMessage() {
		return noticeMessage;
	}
	public void setNoticeMessage(String noticeMessage) {
		this.noticeMessage = noticeMessage;
	}
	public Integer getIsRead() {
		return isRead;
	}
	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}
	
	 
	 
	
	
}
