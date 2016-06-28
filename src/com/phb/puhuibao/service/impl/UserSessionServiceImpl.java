package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.UserSession;

@Transactional
@Service("userSessionService")
public class UserSessionServiceImpl extends DefaultBaseService<UserSession, String>  {
	@Override
	@Resource(name = "userSessionDao")
	public void setBaseDao(IBaseDao<UserSession, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Override
	@Resource(name = "userSessionDao")
	public void setPagerDao(IPagerDao<UserSession> pagerDao) {
		super.setPagerDao(pagerDao);
	}

	 
	
  
}
