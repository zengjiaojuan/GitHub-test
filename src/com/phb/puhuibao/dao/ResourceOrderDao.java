package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.ResourceOrder;

@Repository("resourceOrderDao")
public class ResourceOrderDao extends DefaultBaseDao<ResourceOrder,String> {
	public String getNamespace() {
		return ResourceOrder.class.getName();
	}

}
