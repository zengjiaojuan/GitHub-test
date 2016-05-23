package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.Evaluate;

@Repository("evaluateDao")
public class EvaluateDao extends DefaultBaseDao<Evaluate, String> {
	public String getNamespace() {
		return Evaluate.class.getName();
	}

}
