package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.ResourceComplain;

@Repository("resourceComplainDao")
public class ResourceComplainDao extends DefaultBaseDao<ResourceComplain,String> {
	public String getNamespace() {
		return ResourceComplain.class.getName();
	}

}
