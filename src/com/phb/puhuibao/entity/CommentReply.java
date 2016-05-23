package com.phb.puhuibao.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CommentReply implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3332849936981219867L;
	private Integer replyId;
	private Integer commentId;
	private Integer mUserId;
	private Integer parentId;
	private String content; // 内容
	private String userName; // 用户名
	private Date createTime; // 内容
	private List<CommentReply> childReplyList; // 子回复列表
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<CommentReply> getChildReplyList() {
		return childReplyList;
	}
	public void setChildReplyList(List<CommentReply> childReplyList) {
		this.childReplyList = childReplyList;
	}
	public Integer getReplyId() {
		return replyId;
	}
	public void setReplyId(Integer replyId) {
		this.replyId = replyId;
	}
	public Integer getCommentId() {
		return commentId;
	}
	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}
	public Integer getmUserId() {
		return mUserId;
	}
	public void setmUserId(Integer mUserId) {
		this.mUserId = mUserId;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
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
