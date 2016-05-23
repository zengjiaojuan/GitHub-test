package com.phb.puhuibao.entity;


import com.idp.pub.entity.annotation.MetaTable;

/**
 * @author wei
 *
 */
@MetaTable
public class HexagramCredit implements java.io.Serializable {
	private static final long serialVersionUID = 5736511926578194639L;
	
	private Integer mUserId;//`m_user_id`
	private Double idAuthentication;//`id_authentication`
	private Double resourceComment;//`resource_comment`
	private Double userLevel;//`user_level`
	
	
	
	public Integer getmUserId() {
		return mUserId;
	}
	public void setmUserId(Integer mUserId) {
		this.mUserId = mUserId;
	}
	public Double getIdAuthentication() {
		return idAuthentication;
	}
	public void setIdAuthentication(Double idAuthentication) {
		this.idAuthentication = idAuthentication;
	}
	public Double getResourceComment() {
		return resourceComment;
	}
	public void setResourceComment(Double resourceComment) {
		this.resourceComment = resourceComment;
	}
	public Double getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(Double userLevel) {
		this.userLevel = userLevel;
	}
	
	
	

}
