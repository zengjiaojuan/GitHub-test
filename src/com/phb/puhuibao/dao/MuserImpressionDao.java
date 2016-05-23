package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.MuserImpression;

@Repository("muserImpressionDao")
public class MuserImpressionDao extends DefaultBaseDao<MuserImpression,String> {
	@Override
	public String getNamespace() {
		return MuserImpression.class.getName();
	}
}
