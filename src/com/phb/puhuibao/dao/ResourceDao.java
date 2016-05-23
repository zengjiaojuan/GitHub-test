package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.Resource;

@Repository("resourceDao")
public class ResourceDao extends DefaultBaseDao<Resource, String> {
	public String getNamespace() {
		return Resource.class.getName();
	}

}
