package com.phb.puhuibao.entity;

import java.io.Serializable;

public class Version implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8934350040943201786L;
	private Integer versionId;
	private String versionIos;
	private String versionAndroid;
	private String updateIos;
	private String updateAndroid;
	private String contentIos;
	private String contentAndroid;
	private Integer statusAndroid;
	private Integer statusIos;
	
	public Integer getStatusAndroid() {
		return statusAndroid;
	}
	public void setStatusAndroid(Integer statusAndroid) {
		this.statusAndroid = statusAndroid;
	}
	public Integer getStatusIos() {
		return statusIos;
	}
	public void setStatusIos(Integer statusIos) {
		this.statusIos = statusIos;
	}
	public String getVersionIos() {
		return versionIos;
	}
	public void setVersionIos(String versionIos) {
		this.versionIos = versionIos;
	}
	public String getVersionAndroid() {
		return versionAndroid;
	}
	public void setVersionAndroid(String versionAndroid) {
		this.versionAndroid = versionAndroid;
	}
	public String getUpdateIos() {
		return updateIos;
	}
	public void setUpdateIos(String updateIos) {
		this.updateIos = updateIos;
	}
	public String getUpdateAndroid() {
		return updateAndroid;
	}
	public void setUpdateAndroid(String updateAndroid) {
		this.updateAndroid = updateAndroid;
	}
	public Integer getVersionId() {
		return versionId;
	}
	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}
	public String getContentIos() {
		return contentIos;
	}
	public void setContentIos(String contentIos) {
		this.contentIos = contentIos;
	}
	public String getContentAndroid() {
		return contentAndroid;
	}
	public void setContentAndroid(String contentAndroid) {
		this.contentAndroid = contentAndroid;
	}
}
