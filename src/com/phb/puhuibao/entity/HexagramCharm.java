package com.phb.puhuibao.entity;


import com.idp.pub.entity.annotation.MetaTable;

/**
 * @author wei
 *
 */
@MetaTable
public class HexagramCharm implements java.io.Serializable {
	private static final long serialVersionUID = 5736511926578194639L;
	
	private Integer mUserId;//`m_user_id`
	private Double photoNumber;//``photo_number``
	private Double fansNumber;//``fans_number``
	private Double friendsNumber;//``friends_number``
	private Double userLevel;//user_level
	
	public Integer getmUserId() {
		return mUserId;
	}
	public void setmUserId(Integer mUserId) {
		this.mUserId = mUserId;
	}
	public Double getPhotoNumber() {
		return photoNumber;
	}
	public void setPhotoNumber(Double photoNumber) {
		this.photoNumber = photoNumber;
	}
	public Double getFansNumber() {
		return fansNumber;
	}
	public void setFansNumber(Double fansNumber) {
		this.fansNumber = fansNumber;
	}
	public Double getFriendsNumber() {
		return friendsNumber;
	}
	public void setFriendsNumber(Double friendsNumber) {
		this.friendsNumber = friendsNumber;
	}
	public Double getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(Double userLevel) {
		this.userLevel = userLevel;
	}

	
	
	 
	
	
	

}
