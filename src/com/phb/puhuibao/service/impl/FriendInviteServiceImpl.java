package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.FriendInvite;
 

@Transactional
@Service("friendInviteService")
public class FriendInviteServiceImpl extends DefaultBaseService<FriendInvite, String>  {
	@Resource(name = "friendInviteDao")
	public void setBaseDao(IBaseDao<FriendInvite, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	public void setPagerDao(IPagerDao<FriendInvite> pagerDao) {
		super.setPagerDao(pagerDao);
	}
	

}
