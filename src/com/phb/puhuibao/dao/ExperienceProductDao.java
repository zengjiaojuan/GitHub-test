package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.ExperienceProduct;

@Repository("experienceProductDao")
public class ExperienceProductDao extends DefaultBaseDao<ExperienceProduct,String> {
	public String getNamespace() {
		return ExperienceProduct.class.getName();
	}
}
