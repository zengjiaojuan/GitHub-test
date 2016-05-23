package com.phb.puhuibao.entity;

import java.util.Date;

public class MuserFollow {
	
	 
	private Integer muserId;//`m_user_id`
	private Integer followUser;//`follow_user`
	private String followUname;//`follow_uname`
	private String followReporttype;//`follow_reporttype` `follow_reporttype`
	private String followReportcomment;//follow_reportcomment
	private String followIsblocked;//follow_isblocked
	private Date followTime;//`follow_time`
	private Date commentTime;//`comment_Time`
	private String photo;//follow_isblocked
	private String nickname;//nickname
	private Integer sex;//`sex`
	private Integer level;//`level`
	
	
	
	
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	 
	public Integer getMuserId() {
		return muserId;
	}
	public void setMuserId(Integer muserId) {
		this.muserId = muserId;
	}
	public Integer getFollowUser() {
		return followUser;
	}
	public void setFollowUser(Integer followUser) {
		this.followUser = followUser;
	}
	public String getFollowUname() {
		return followUname;
	}
	public void setFollowUname(String followUname) {
		this.followUname = followUname;
	}
	public String getFollowReporttype() {
		return followReporttype;
	}
	public void setFollowReporttype(String followReporttype) {
		this.followReporttype = followReporttype;
	}
	public String getFollowReportcomment() {
		return followReportcomment;
	}
	public void setFollowReportcomment(String followReportcomment) {
		this.followReportcomment = followReportcomment;
	}
	public String getFollowIsblocked() {
		return followIsblocked;
	}
	public void setFollowIsblocked(String followIsblocked) {
		this.followIsblocked = followIsblocked;
	}
	public Date getFollowTime() {
		return followTime;
	}
	public void setFollowTime(Date followTime) {
		this.followTime = followTime;
	}
	public Date getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(Date commentTime) {
		this.commentTime = commentTime;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
 
	
	 
	
	
}
