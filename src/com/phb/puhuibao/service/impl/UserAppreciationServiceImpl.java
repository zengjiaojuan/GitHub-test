package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.UserAppreciation;

@Transactional
@Service("userAppreciationService")
public class UserAppreciationServiceImpl extends DefaultBaseService<UserAppreciation, String> {
	@Resource(name = "userAppreciationDao")
	public void setBaseDao(IBaseDao<UserAppreciation, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "userAppreciationDao")
	public void setPagerDao(IPagerDao<UserAppreciation> pagerDao) {
		super.setPagerDao(pagerDao);
	}

}
