package com.phb.puhuibao.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.ResourceComplain;

@Transactional
@Service("resourceComplainService")
public class ResourceComplainServiceImpl extends DefaultBaseService<ResourceComplain, String> {
	@javax.annotation.Resource(name = "resourceComplainDao")
	public void setBaseDao(IBaseDao<ResourceComplain, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@javax.annotation.Resource(name = "resourceComplainDao")
	public void setPagerDao(IPagerDao<ResourceComplain> pagerDao) {
		super.setPagerDao(pagerDao);
	}

}
