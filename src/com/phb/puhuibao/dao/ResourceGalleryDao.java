package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.ResourceGallery;

@Repository("resourceGalleryDao")
public class ResourceGalleryDao extends DefaultBaseDao<ResourceGallery, String> {
	public String getNamespace() {
		return ResourceGallery.class.getName();
	}

}
