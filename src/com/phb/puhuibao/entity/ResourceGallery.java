package com.phb.puhuibao.entity;

import java.io.Serializable;

public class ResourceGallery implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8763722364336743871L;
	private String pictureId;
	private Integer resourceId;
	public String getPictureId() {
		return pictureId;
	}
	public void setPictureId(String pictureId) {
		this.pictureId = pictureId;
	}
	public Integer getResourceId() {
		return resourceId;
	}
	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}
}