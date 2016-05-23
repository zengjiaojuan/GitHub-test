package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.UserCard;

@Transactional
@Service("userCardService")
public class UserCardServiceImpl extends DefaultBaseService<UserCard, String> {
	@Resource(name = "userCardDao")
	public void setBaseDao(IBaseDao<UserCard, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "userCardDao")
	public void setPagerDao(IPagerDao<UserCard> pagerDao) {
		super.setPagerDao(pagerDao);
	}

	public UserCard save(UserCard entity) {
		return  this.getBaseDao().save(entity);
	}
}
