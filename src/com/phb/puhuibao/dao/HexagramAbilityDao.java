package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.HexagramAbility;
 

@Repository("hexagramAbilityDao")
public class HexagramAbilityDao extends DefaultBaseDao<HexagramAbility,String>{
	@Override
	public String getNamespace() {
		return HexagramAbility.class.getName();
	}
}

