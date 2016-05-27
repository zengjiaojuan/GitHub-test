package com.phb.puhuibao.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.MobileUser;
import com.phb.puhuibao.entity.Resource;
import com.phb.puhuibao.entity.UserAccountLog;
import com.phb.puhuibao.service.ResourceService;

@Transactional
@Service("resourceService")
public class ResourceServiceImpl extends DefaultBaseService<Resource, String> implements ResourceService {
	@Override
	@javax.annotation.Resource(name = "resourceDao")
	public void setBaseDao(IBaseDao<Resource, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Override
	@javax.annotation.Resource(name = "resourceDao")
	public void setPagerDao(IPagerDao<Resource> pagerDao) {
		super.setPagerDao(pagerDao);
	}

	@javax.annotation.Resource(name = "mobileUserDao")
	private IBaseDao<MobileUser, String> mobileUserDao;

	@javax.annotation.Resource(name = "userAccountLogDao")
	private IBaseDao<UserAccountLog, String> userAccountLogDao;

	@Override
	public void pay(Resource resource) { }
}
