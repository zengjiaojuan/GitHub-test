package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.ResourceType;

@Repository("resourceTypeDao")
public class ResourceTypeDao extends DefaultBaseDao<ResourceType, String> {
	public String getNamespace() {
		return ResourceType.class.getName();
	}

}
