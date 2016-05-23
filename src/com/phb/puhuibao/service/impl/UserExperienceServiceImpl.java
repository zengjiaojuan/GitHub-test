package com.phb.puhuibao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idp.pub.dao.IBaseDao;
import com.idp.pub.service.impl.DefaultBaseService;
import com.phb.puhuibao.entity.UserExperience;

@Transactional
@Service("userExperienceService")
public class UserExperienceServiceImpl extends DefaultBaseService<UserExperience, String> {
	@Resource(name = "userExperienceDao")
	public void setBaseDao(IBaseDao<UserExperience, String> baseDao) {
		super.setBaseDao(baseDao);
	}

}
