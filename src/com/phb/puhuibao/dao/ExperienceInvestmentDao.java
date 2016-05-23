package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.ExperienceInvestment;

@Repository("experienceInvestmentDao")
public class ExperienceInvestmentDao extends DefaultBaseDao<ExperienceInvestment,String> {
	public String getNamespace() {
		return ExperienceInvestment.class.getName();
	}

}
