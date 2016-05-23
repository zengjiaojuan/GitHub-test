package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.UserAccountLog;

@Transactional
@Service("userAccountLogService")
public class UserAccountLogServiceImpl extends DefaultBaseService<UserAccountLog, String> {
	@Resource(name = "userAccountLogDao")
	public void setBaseDao(IBaseDao<UserAccountLog, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "userAccountLogDao")
	public void setPagerDao(IPagerDao<UserAccountLog> pagerDao) {
		super.setPagerDao(pagerDao);
	}

	public UserAccountLog save(UserAccountLog entity) {
		return  this.getBaseDao().save(entity);
	}
}
