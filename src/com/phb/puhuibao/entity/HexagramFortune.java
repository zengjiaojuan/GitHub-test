package com.phb.puhuibao.entity;


import com.idp.pub.entity.annotation.MetaTable;

/**
 * @author wei
 *
 */
@MetaTable
public class HexagramFortune implements java.io.Serializable {
	private static final long serialVersionUID = 5736511926578194639L;
	
	private Integer mUserId;//`m_user_id`
	private Double isBind;//``is_bind``
	private Double depositValue;//``deposit_value``
	private Double messionPublish;//````mession_publish````
	private Double investValue;//`invest_value`
	
	
	public Integer getmUserId() {
		return mUserId;
	}
	public void setmUserId(Integer mUserId) {
		this.mUserId = mUserId;
	}
	public Double getIsBind() {
		return isBind;
	}
	public void setIsBind(Double isBind) {
		this.isBind = isBind;
	}
	public Double getDepositValue() {
		return depositValue;
	}
	public void setDepositValue(Double depositValue) {
		this.depositValue = depositValue;
	}
	public Double getMessionPublish() {
		return messionPublish;
	}
	public void setMessionPublish(Double messionPublish) {
		this.messionPublish = messionPublish;
	}
	public Double getInvestValue() {
		return investValue;
	}
	public void setInvestValue(Double investValue) {
		this.investValue = investValue;
	}
	
	
	 
	

}
