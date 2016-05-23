package com.phb.puhuibao.entity;

import java.util.Date;
 

public class GroupChat {
	
	 
	private String   groupId;//`group_id`
	private String   groupName;//`group_name`
	private String   groupDescscription;//`group_descscription`
	private Integer  isPublic;//```is_public```
	private Integer  membersOnly;//``members_only``
	private Integer  allowInvites;//`allow_invites`
	private Integer  maxUsers;//`max_users`
	private Integer  affiliationsCount;//``affiliations_count``
	private String   affiliations;//``affiliations``
	private String   groupOwner;//``group_owner``
	private Integer  groupOwnerid;//group_ownerid
	private String   member;//````member````
	private Date     buildTime;//build_time
	
	
	
	public Integer getGroupOwnerid() {
		return groupOwnerid;
	}
	public void setGroupOwnerid(Integer groupOwnerid) {
		this.groupOwnerid = groupOwnerid;
	}
	public Date getBuildTime() {
		return buildTime;
	}
	public void setBuildTime(Date buildTime) {
		this.buildTime = buildTime;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupDescscription() {
		return groupDescscription;
	}
	public void setGroupDescscription(String groupDescscription) {
		this.groupDescscription = groupDescscription;
	}
	public Integer getIsPublic() {
		return isPublic;
	}
	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
	}
	public Integer getMembersOnly() {
		return membersOnly;
	}
	public void setMembersOnly(Integer membersOnly) {
		this.membersOnly = membersOnly;
	}
	public Integer getAllowInvites() {
		return allowInvites;
	}
	public void setAllowInvites(Integer allowInvites) {
		this.allowInvites = allowInvites;
	}
	public Integer getMaxUsers() {
		return maxUsers;
	}
	public void setMaxUsers(Integer maxUsers) {
		this.maxUsers = maxUsers;
	}
	public Integer getAffiliationsCount() {
		return affiliationsCount;
	}
	public void setAffiliationsCount(Integer affiliationsCount) {
		this.affiliationsCount = affiliationsCount;
	}
	public String getAffiliations() {
		return affiliations;
	}
	public void setAffiliations(String affiliations) {
		this.affiliations = affiliations;
	}
	public String getGroupOwner() {
		return groupOwner;
	}
	public void setGroupOwner(String groupOwner) {
		this.groupOwner = groupOwner;
	}
	public String getMember() {
		return member;
	}
	public void setMember(String member) {
		this.member = member;
	}
 
	
	 
	 
 
	
	
	 
 
	
	 
	
	
}
