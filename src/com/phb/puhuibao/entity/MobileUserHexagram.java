package com.phb.puhuibao.entity;

import java.io.Serializable;

public class MobileUserHexagram implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6683804277204206998L;
	private Integer mUserId;
	private Double fortuneScore; // 财富积分
	private Double creditScore; // 信用积分
	private Double reputationScore; // 声望积分
	private Double charmScore; // 魅力积分
	private Double abilityScore; // 能力积分
	public Integer getmUserId() {
		return mUserId;
	}
	public void setmUserId(Integer mUserId) {
		this.mUserId = mUserId;
	}
	public Double getFortuneScore() {
		return fortuneScore;
	}
	public void setFortuneScore(Double fortuneScore) {
		this.fortuneScore = fortuneScore;
	}
	public Double getCreditScore() {
		return creditScore;
	}
	public void setCreditScore(Double creditScore) {
		this.creditScore = creditScore;
	}
	public Double getReputationScore() {
		return reputationScore;
	}
	public void setReputationScore(Double reputationScore) {
		this.reputationScore = reputationScore;
	}
	public Double getCharmScore() {
		return charmScore;
	}
	public void setCharmScore(Double charmScore) {
		this.charmScore = charmScore;
	}
	public Double getAbilityScore() {
		return abilityScore;
	}
	public void setAbilityScore(Double abilityScore) {
		this.abilityScore = abilityScore;
	}
	
}
