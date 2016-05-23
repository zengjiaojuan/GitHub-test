package com.phb.puhuibao.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Comment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4845500218679447227L;
	private Integer commentId;
	private Integer resourceId;
	private Integer mUserId;
	private String content; // 内容
	private String userName; // 用户名
	private String photo; // 头像
	private Date createTime; // 内容
	private List<CommentReply> replyList; // 回复列表
	
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<CommentReply> getReplyList() {
		return replyList;
	}
	public void setReplyList(List<CommentReply> replyList) {
		this.replyList = replyList;
	}
	public Integer getCommentId() {
		return commentId;
	}
	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}
	public Integer getResourceId() {
		return resourceId;
	}
	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}
	public Integer getmUserId() {
		return mUserId;
	}
	public void setmUserId(Integer mUserId) {
		this.mUserId = mUserId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
