package com.phb.puhuibao.entity;

import java.io.Serializable;

public class ResourceType implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9186923646404881703L;
	private Integer typeId;
	private String name;
	private String icon;
	private String defaultImage;
	
	public String getDefaultImage() {
		return defaultImage;
	}
	public void setDefaultImage(String defaultImage) {
		this.defaultImage = defaultImage;
	}
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
}
