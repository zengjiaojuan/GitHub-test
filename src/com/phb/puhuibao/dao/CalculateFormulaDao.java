package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.CalculateFormula;
 

@Repository("calculateFormulaDao")
public class CalculateFormulaDao extends DefaultBaseDao<CalculateFormula,String>{
	@Override
	public String getNamespace() {
		return CalculateFormula.class.getName();
	}
}

