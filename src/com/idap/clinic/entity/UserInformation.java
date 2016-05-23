package com.idap.clinic.entity;

import java.util.Date;

import com.idp.pub.entity.annotation.MetaTable;
import com.idp.pub.utils.RSAUtils;

@MetaTable
public class UserInformation implements java.io.Serializable {
	private static final long serialVersionUID = 5736511926578194639L;
	 
 	private String		mUserId 	;//	m_user_id 	M_USER_ID 
	private String		mUserName 	;//	m_user_name 	M_USER_NAME 
	private String		mUserPwd; 
	private String		mUserTel  	;//	m_user_tel  	M_USER_TEL 
	private Date mDate;
	private String departmentName;
	private int isDoctor;
	private String thirdParty;
	private String photo;
	private int sex;
	private int age;
	private String occupation;

	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getThirdParty() {
		return thirdParty;
	}
	public void setThirdParty(String thirdParty) {
		this.thirdParty = thirdParty;
	}
	public int getIsDoctor() {
		return isDoctor;
	}
	public void setIsDoctor(int isDoctor) {
		this.isDoctor = isDoctor;
	}
	public String getmUserId() {
		return mUserId;
	}
	public void setmUserId(String mUserId) {
		this.mUserId = mUserId;
	}
	public String getmUserName() {
		return mUserName;
	}
	public void setmUserName(String mUserName) {
		this.mUserName = mUserName;
	}
	public String getmUserPwd() {
		return mUserPwd;
	}
	public void setmUserPwd(String mUserPwd) {
		this.mUserPwd = mUserPwd;
	}
	public String getmUserTel() {
		return mUserTel;
	}
	public void setmUserTel(String mUserTel) {
		this.mUserTel = mUserTel;
	}
	public Date getmDate() {
		return mDate;
	}
	public void setmDate(Date mDate) {
		this.mDate = mDate;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
 
 
	 
	

}
