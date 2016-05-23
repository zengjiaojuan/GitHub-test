package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.Invite;

@Transactional
@Service("inviteService")
public class InviteServiceImpl extends DefaultBaseService<Invite, String> {
	@Resource(name = "inviteDao")
	public void setBaseDao(IBaseDao<Invite, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "inviteDao")
	public void setPagerDao(IPagerDao<Invite> pagerDao) {
		super.setPagerDao(pagerDao);
	}

}
