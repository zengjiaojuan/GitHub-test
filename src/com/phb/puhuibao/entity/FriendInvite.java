package com.phb.puhuibao.entity;
 

public class FriendInvite {
	
	 
	private Integer  inviteId;//``invite_id``
	private Integer  fromUser;//``from_user``
	private String   fromUsername;//`from_username`
	private Integer  toUser;//``to_user``
	private Integer  isAccepted;//`is_accepted`
	private String   inviteMessage;//invite_message
	private String   fromUserphoto;
	
	 
	
	
	public String getFromUserphoto() {
		return fromUserphoto;
	}
	public void setFromUserphoto(String fromUserphoto) {
		this.fromUserphoto = fromUserphoto;
	}
	public String getFromUsername() {
		return fromUsername;
	}
	public void setFromUsername(String fromUsername) {
		this.fromUsername = fromUsername;
	}
	public String getInviteMessage() {
		return inviteMessage;
	}
	public void setInviteMessage(String inviteMessage) {
		this.inviteMessage = inviteMessage;
	}
	public Integer getInviteId() {
		return inviteId;
	}
	public void setInviteId(Integer inviteId) {
		this.inviteId = inviteId;
	}
	public Integer getFromUser() {
		return fromUser;
	}
	public void setFromUser(Integer fromUser) {
		this.fromUser = fromUser;
	}
	public Integer getToUser() {
		return toUser;
	}
	public void setToUser(Integer toUser) {
		this.toUser = toUser;
	}
	public Integer getIsAccepted() {
		return isAccepted;
	}
	public void setIsAccepted(Integer isAccepted) {
		this.isAccepted = isAccepted;
	}
 
	
	
	 
 
	
	 
	
	
}
