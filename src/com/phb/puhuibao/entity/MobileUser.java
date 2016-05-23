package com.phb.puhuibao.entity;

import java.io.Serializable;
import java.util.Date;

public class MobileUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1413950195129924301L;
	private int mUserId;
	private String mUserName;
	private String mUserTel;
	private String mUserEmail;
	private String mUserPwd;
	private String payPassword;
	private String thirdParty;
	private String photo;
	private Integer sex;
	private Integer age;
	private String occupation;
	private Date createTime;
	private Integer parentId;
	private Double mUserMoney; // 用户现有资金
	private Double frozenMoney; // 用户冻结资金
	private String idNumber;
	private String emergencyName;
	private String emergencyPhone;
	private String emergencyRelation;
	private String idPhoto;
	private String idPhotoBack;
	private String idPhotoHand;
	private Integer isAudit;
	private Integer level; // 级别
	private String nickname; // 昵称
	private String constellation; // 星座
	private Integer liveness; // 活跃度
	private String picturewall; // 照片墙
	private String socialStatus;//个人签名
	
	
	public Integer getLiveness() {
		return liveness;
	}
	public String getSocialStatus() {
		return socialStatus;
	}
	public void setSocialStatus(String socialStatus) {
		this.socialStatus = socialStatus;
	}
	public void setLiveness(Integer liveness) {
		this.liveness = liveness;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getConstellation() {
		return constellation;
	}
	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public int getmUserId() {
		return mUserId;
	}
	public void setmUserId(int mUserId) {
		this.mUserId = mUserId;
	}
	public String getmUserName() {
		return mUserName;
	}
	public void setmUserName(String mUserName) {
		this.mUserName = mUserName;
	}
	public String getmUserTel() {
		return mUserTel;
	}
	public void setmUserTel(String mUserTel) {
		this.mUserTel = mUserTel;
	}
	public String getmUserEmail() {
		return mUserEmail;
	}
	public void setmUserEmail(String mUserEmail) {
		this.mUserEmail = mUserEmail;
	}
	public String getmUserPwd() {
		return mUserPwd;
	}
	public void setmUserPwd(String mUserPwd) {
		this.mUserPwd = mUserPwd;
	}
	public String getPayPassword() {
		return payPassword;
	}
	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}
	public String getThirdParty() {
		return thirdParty;
	}
	public void setThirdParty(String thirdParty) {
		this.thirdParty = thirdParty;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Double getmUserMoney() {
		return mUserMoney;
	}
	public void setmUserMoney(Double mUserMoney) {
		this.mUserMoney = mUserMoney;
	}
	public Double getFrozenMoney() {
		return frozenMoney;
	}
	public void setFrozenMoney(Double frozenMoney) {
		this.frozenMoney = frozenMoney;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getEmergencyName() {
		return emergencyName;
	}
	public void setEmergencyName(String emergencyName) {
		this.emergencyName = emergencyName;
	}
	public String getEmergencyPhone() {
		return emergencyPhone;
	}
	public void setEmergencyPhone(String emergencyPhone) {
		this.emergencyPhone = emergencyPhone;
	}
	public String getEmergencyRelation() {
		return emergencyRelation;
	}
	public void setEmergencyRelation(String emergencyRelation) {
		this.emergencyRelation = emergencyRelation;
	}
	public String getIdPhoto() {
		return idPhoto;
	}
	public void setIdPhoto(String idPhoto) {
		this.idPhoto = idPhoto;
	}
	public String getIdPhotoBack() {
		return idPhotoBack;
	}
	public void setIdPhotoBack(String idPhotoBack) {
		this.idPhotoBack = idPhotoBack;
	}
	public String getIdPhotoHand() {
		return idPhotoHand;
	}
	public void setIdPhotoHand(String idPhotoHand) {
		this.idPhotoHand = idPhotoHand;
	}
	public Integer getIsAudit() {
		return isAudit;
	}
	public void setIsAudit(Integer isAudit) {
		this.isAudit = isAudit;
	}
	public String getPicturewall() {
		return picturewall;
	}
	public void setPicturewall(String picturewall) {
		this.picturewall = picturewall;
	}
}
