package com.phb.puhuibao.entity;

import java.util.Date;
 

/**
 * @author wei
 *
 */
public class CustomerManagement {
	
 
 
	
	private String userId;//`user_id` varchar(20) NOT NULL DEFAULT '' COMMENT '投资人身份证',
	private String userName;// `user_name` varchar(25) NOT NULL DEFAULT '' COMMENT '投资人姓名',
	private String userTel;// `user_tel` varchar(50) DEFAULT '' COMMENT '投资人电话',
	private String userMail;// `user_mail` varchar(50) DEFAULT '' COMMENT '电子邮件',
	private String userAddress;// `user_address` varchar(50) DEFAULT '' COMMENT '用户地址',
	private String userPost;// `user_post` varchar(32) DEFAULT '' COMMENT '用户邮编',
	private int userGender;// `user_gender` tinyint(1) DEFAULT NULL COMMENT '投资人性别',
	private String useridExdate;// `userid_exdate` date DEFAULT NULL COMMENT '身份证到期日',
	private String useridDep;//`userid_dep`
	private String emergencyContact;//`emergency_contact`
	private String emergencyContactid;//`emergency_contactid`
	private String emergencyContacttel;//`emergency_contacttel`
	private int    isDeleted;//``is_deleted``
 
	
	
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getUseridDep() {
		return useridDep;
	}
	public void setUseridDep(String useridDep) {
		this.useridDep = useridDep;
	}
	public String getEmergencyContact() {
		return emergencyContact;
	}
	public void setEmergencyContact(String emergencyContact) {
		this.emergencyContact = emergencyContact;
	}
	public String getEmergencyContactid() {
		return emergencyContactid;
	}
	public void setEmergencyContactid(String emergencyContactid) {
		this.emergencyContactid = emergencyContactid;
	}
	public String getEmergencyContacttel() {
		return emergencyContacttel;
	}
	public void setEmergencyContacttel(String emergencyContacttel) {
		this.emergencyContacttel = emergencyContacttel;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserTel() {
		return userTel;
	}
	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}
 
	public String getUserMail() {
		return userMail;
	}
	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}
	public String getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	public String getUserPost() {
		return userPost;
	}
	public void setUserPost(String userPost) {
		this.userPost = userPost;
	}
 
	public int getUserGender() {
		return userGender;
	}
	public void setUserGender(int userGender) {
		this.userGender = userGender;
	}
 
	public String getUseridExdate() {
		return useridExdate;
	}
	public void setUseridExdate(String useridExdate) {
		this.useridExdate = useridExdate;
	}

 
}
