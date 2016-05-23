package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.ResourceType;

@Transactional
@Service("resourceTypeService")
public class ResourceTypeServiceImpl extends DefaultBaseService<ResourceType, String> {
	@Resource(name = "resourceTypeDao")
	public void setBaseDao(IBaseDao<ResourceType, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "resourceTypeDao")
	public void setPagerDao(IPagerDao<ResourceType> pagerDao) {
		super.setPagerDao(pagerDao);
	}

}
