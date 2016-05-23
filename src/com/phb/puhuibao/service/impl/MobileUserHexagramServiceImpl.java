package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.MobileUserHexagram;

@Transactional
@Service("mobileUserHexagramService")
public class MobileUserHexagramServiceImpl extends DefaultBaseService<MobileUserHexagram, String> {
	@Resource(name = "mobileUserHexagramDao")
	public void setBaseDao(IBaseDao<MobileUserHexagram, String> baseDao) {
		super.setBaseDao(baseDao);
	}

}
