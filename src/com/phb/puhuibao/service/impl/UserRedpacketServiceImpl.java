package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.dao.IPagerDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.UserRedpacket;

@Transactional
@Service("userRedpacketService")
public class UserRedpacketServiceImpl extends DefaultBaseService<UserRedpacket, String> {
	@Resource(name = "userRedpacketDao")
	public void setBaseDao(IBaseDao<UserRedpacket, String> baseDao) {
		super.setBaseDao(baseDao);
	}

	@Resource(name = "userRedpacketDao")
	public void setPagerDao(IPagerDao<UserRedpacket> pagerDao) {
		super.setPagerDao(pagerDao);
	}

}
