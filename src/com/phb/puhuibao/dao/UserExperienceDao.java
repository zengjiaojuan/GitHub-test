package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.UserExperience;

@Repository("userExperienceDao")
public class UserExperienceDao extends DefaultBaseDao<UserExperience,String> {
	public String getNamespace() {
		return UserExperience.class.getName();
	}

}
