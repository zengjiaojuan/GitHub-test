package com.phb.puhuibao.entity;


public class MuserImpression {
	
	private Integer impressionId;//`impression_id`
	private Integer fromuserId;//``fromuser_id``
	private Integer touserId;//``touser_id``
	private String impressionType;//impression_type
	private String impressionContent;//``impression_content``
	
	
	public Integer getImpressionId() {
		return impressionId;
	}
	public void setImpressionId(Integer impressionId) {
		this.impressionId = impressionId;
	}
	public Integer getFromuserId() {
		return fromuserId;
	}
	public void setFromuserId(Integer fromuserId) {
		this.fromuserId = fromuserId;
	}
	public Integer getTouserId() {
		return touserId;
	}
	public void setTouserId(Integer touserId) {
		this.touserId = touserId;
	}
	public String getImpressionType() {
		return impressionType;
	}
	public void setImpressionType(String impressionType) {
		this.impressionType = impressionType;
	}
	public String getImpressionContent() {
		return impressionContent;
	}
	public void setImpressionContent(String impressionContent) {
		this.impressionContent = impressionContent;
	}
 
 
}
