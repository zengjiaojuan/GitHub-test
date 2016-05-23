package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.MobileUserEasemob;

@Transactional
@Service("mobileUserEasemobService")
public class MobileUserEasemobServiceImpl extends DefaultBaseService<MobileUserEasemob, String> {
	@Resource(name = "mobileUserEasemobDao")
	public void setBaseDao(IBaseDao<MobileUserEasemob, String> baseDao) {
		super.setBaseDao(baseDao);
	}
}
