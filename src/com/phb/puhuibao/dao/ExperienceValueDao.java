package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.ExperienceValue;

@Repository("experienceValueDao")
public class ExperienceValueDao extends DefaultBaseDao<ExperienceValue,String> {
	public String getNamespace() {
		return ExperienceValue.class.getName();
	}

}
