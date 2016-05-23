package com.phb.puhuibao.entity;


import com.idp.pub.entity.annotation.MetaTable;

/**
 * @author wei
 *
 */
@MetaTable
public class HexagramFame implements java.io.Serializable {
	private static final long serialVersionUID = 5736511926578194639L;
	
	private Integer mUserId;//`m_user_id`
	private Double validMessions;//```valid_messions```
	private Double shareTimes;//```share_times```
	private Double evaluateScore;//```evaluate_score```
	private Double multipersonMession;//`multiperson_mession`
	private Double userLevel;//user_level
	
	
	public Integer getmUserId() {
		return mUserId;
	}
	public void setmUserId(Integer mUserId) {
		this.mUserId = mUserId;
	}
	public Double getValidMessions() {
		return validMessions;
	}
	public void setValidMessions(Double validMessions) {
		this.validMessions = validMessions;
	}
	public Double getShareTimes() {
		return shareTimes;
	}
	public void setShareTimes(Double shareTimes) {
		this.shareTimes = shareTimes;
	}
	public Double getEvaluateScore() {
		return evaluateScore;
	}
	public void setEvaluateScore(Double evaluateScore) {
		this.evaluateScore = evaluateScore;
	}
	public Double getMultipersonMession() {
		return multipersonMession;
	}
	public void setMultipersonMession(Double multipersonMession) {
		this.multipersonMession = multipersonMession;
	}
	public Double getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(Double userLevel) {
		this.userLevel = userLevel;
	}
	
	
	
	
	
	 
	
	
	

}
