package com.idap.clinic.entity;

import java.util.Date;

public class Share {
	private int id;
	private String title;
	private String content;
	private String type;
	private Date shareDatetime;
	private String muid;
	
	public String getMuid() {
		return muid;
	}
	public void setMuid(String muid) {
		this.muid = muid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getShareDatetime() {
		return shareDatetime;
	}
	public void setShareDatetime(Date shareDatetime) {
		this.shareDatetime = shareDatetime;
	}

}
