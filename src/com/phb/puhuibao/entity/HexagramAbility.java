package com.phb.puhuibao.entity;


import com.idp.pub.entity.annotation.MetaTable;

/**
 * @author wei
 *
 */
@MetaTable
public class HexagramAbility implements java.io.Serializable {
	private static final long serialVersionUID = 5736511926578194639L;
	
	private Integer mUserId;//`m_user_id`
	private Double abilityMumber;//`ability_number`
	private Double specialabilityNumber;//`specialability_number`
	private Double friendsNumber;//```friends_number```
	private Double messioncompliteNumber;//`messioncomplite_number`
	private Double userLevel;//user_level
	
	
	public Integer getmUserId() {
		return mUserId;
	}
	public void setmUserId(Integer mUserId) {
		this.mUserId = mUserId;
	}
	public Double getAbilityMumber() {
		return abilityMumber;
	}
	public void setAbilityMumber(Double abilityMumber) {
		this.abilityMumber = abilityMumber;
	}
	public Double getSpecialabilityNumber() {
		return specialabilityNumber;
	}
	public void setSpecialabilityNumber(Double specialabilityNumber) {
		this.specialabilityNumber = specialabilityNumber;
	}
	public Double getFriendsNumber() {
		return friendsNumber;
	}
	public void setFriendsNumber(Double friendsNumber) {
		this.friendsNumber = friendsNumber;
	}
	public Double getMessioncompliteNumber() {
		return messioncompliteNumber;
	}
	public void setMessioncompliteNumber(Double messioncompliteNumber) {
		this.messioncompliteNumber = messioncompliteNumber;
	}
	public Double getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(Double userLevel) {
		this.userLevel = userLevel;
	}
	
 
	

}
