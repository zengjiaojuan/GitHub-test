package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.MobileUserSignin;

@Transactional
@Service("mobileUserSigninService")
public class MobileUserSigninServiceImpl extends DefaultBaseService<MobileUserSignin, String> {
	@Resource(name = "mobileUserSigninDao")
	public void setBaseDao(IBaseDao<MobileUserSignin, String> baseDao) {
		super.setBaseDao(baseDao);
	}

}
