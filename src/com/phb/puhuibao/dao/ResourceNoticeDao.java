package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.ResourceNotice;

@Repository("resourceNoticeDao")
public class ResourceNoticeDao extends DefaultBaseDao<ResourceNotice,String> {
	@Override
	public String getNamespace() {
		return ResourceNotice.class.getName();
	}
}
